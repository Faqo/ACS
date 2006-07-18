# @(#) $Id: Container.py,v 1.24 2006/07/18 21:52:57 dfugate Exp $
#
# Copyright (C) 2001
# Associated Universities, Inc. Washington DC, USA.
#
# Produced for the ALMA project
#
# This library is free software; you can redistribute it and/or modify it under
# the terms of the GNU Library General Public License as published by the Free
# Software Foundation; either version 2 of the License, or (at your option) any
# later version.
#
# This library is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY FITNESS
# FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
# details.
#
# You should have received a copy of the GNU Library General Public License
# along with this library; if not, write to the Free Software Foundation, Inc.,
# 675 Massachusetts Ave, Cambridge, MA 02139, USA.  Correspondence concerning
# ALMA should be addressed as follows:
#
# Internet email: alma-sw-admin@nrao.edu
# "@(#) $Id: Container.py,v 1.24 2006/07/18 21:52:57 dfugate Exp $"
#
# who       when        what
# --------  ----------  ----------------------------------------------
# dfugate   2003/08/04  Created.
#------------------------------------------------------------------------------
'''
This module includes the implementation of a Python Container for the
maci::Container IDL interface.

TODO LIST:
- integrate with the new ACS Error System
- mutex lock methods (if needed)
- fix the interfaces param to ComponentInfo in activate_component(...)
- a ComponentLifecycleException has been defined in IDL now...
'''

__revision__ = "$Id: Container.py,v 1.24 2006/07/18 21:52:57 dfugate Exp $"


#--REGULAR IMPORTS-------------------------------------------------------------
from time      import sleep
from signal    import signal, SIGINT
from new       import instance
from traceback import print_exc
#--CORBA STUBS-----------------------------------------------------------------
import PortableServer
import maci__POA
from CORBA import FALSE
from maci  import ComponentInfo
from ACS   import OffShoot
import ACS
from ACSErrTypeCommonImpl              import CORBAProblemExImpl, CouldntCreateObjectExImpl
#--ACS Imports-----------------------------------------------------------------
from Acspy.Common.Log                       import getLogger
from Acspy.Common.CDBAccess                 import CDBaccess
from Acspy.Clients.BaseClient               import BaseClient
from Acspy.Servants.ContainerServices       import ContainerServices
from Acspy.Servants.ComponentLifecycle      import ComponentLifecycle
from Acspy.Servants.ComponentLifecycle      import ComponentLifecycleException
from Acspy.Servants.ACSComponent            import ACSComponent
from Acspy.Servants.CharacteristicComponent import CharacteristicComponent
from Acspy.Util                             import ACSCorba
from AcsutilPy.FindFile                     import findFile
#--GLOBALS---------------------------------------------------------------------
#Manager commands to this container
ACTIVATOR_RELOAD = 0
ACTIVATOR_REBOOT = 1
ACTIVATOR_EXIT   = 2

#The fields of a component dictionary entry
HANDLE      = 'HANDLE'
NAME        = 'NAME'
EXE         = 'EXE'
TYPE        = 'TYPE'
POA         = 'POA'
PYCLASS     = 'PYCLASS'
PYREF       = 'PYREF'
CORBAREF    = 'CORBAREF'
COMPONENTINFO     = 'COMPONENTINFO'
POAOFFSHOOT = 'POAOFFSHOOT'
COMPMODULE  = 'COMPMODULE'
#------------------------------------------------------------------------------
class Container(maci__POA.Container, BaseClient):
    '''
    The Python implementation of a MACI Container.

    This implies it implements both the Container and Client interfaces. It is
    multi-threaded and currently supports components derived from
    ACSComponent, ContainerServices, and the LifeCycle classes.
    '''
    #--------------------------------------------------------------------------
    def __init__ (self, name):
        '''
        Constructor.

        Initializes member variables and CORBA
        
        Parameters: name is the stringified name of this container.

        Raises: ???
        '''
        
        #Member variables
        self.running = 1  #As long as this is true, container is not shutdown
        self.name = name  #Container Name
        self.canRecover = 0  #Whether this container is capable of recovery
        self.components = {}  #A dict where components are referenced by name
        self.compHandles = {}  #A dict where comp names are referenced by handles
        self.shutdownHandles = []
        self.containerPOA = None  #POA to activate this container
        self.componentPOA = None  #POA to create POAs for components
        self.compPolicies = []  #Policy[] for components
        self.offShootPolicies = []  #Policy[] for offshoots
        self.corbaRef = None  #reference to this object's CORBA reference
        self.logger = getLogger(name)

        self.cdbAccess = CDBaccess()
        self.cdbContainerInfo = {}
        self.autoLoadPackages = []
        #dictionary which maps package names to the number of active components
        #using said package
        self.compModuleCount = {}

        self.logger.logTrace('Starting Container: ' + self.name)

        #get info from the CDB
        self.getCDBInfo()
        
        #Configure CORBA
        self.configCORBA()

        #call superclass constructor
        BaseClient.__init__(self, self.name)
        
        self.logger.logTrace('CORBA configured for Container: ' + self.name)

        #Run everything
        self.logger.logInfo('Container ' + self.name + ' waiting for requests')
        
    #--CLIENT IDL--------------------------------------------------------------
    def disconnect(self):
        '''
        Disconnect from manager.
        
        oneway void disconnect ();
        '''
        self.logger.logTrace('Shutdown called for Container: ' + self.name)
        self.shutdown(ACTIVATOR_EXIT<<8)
        return
    #--ACTIVATOR IDL-----------------------------------------------------------
    def activate_component(self, h, name, exe, idl_type):
        '''
        Activates a component (or returns a reference to it if already exists).

        Parameters:
        - h is the handle the component will be given
        - name is simply the components name
        - exe is the name of the Python module that has to be imported for the
        components implementation
        - idl_type is the the IR Location for the component

        Returns: a ComponentInfo structure for manager to use.
        
        activate_component(in Handle h,in string name,in string exe,in string idl_type)
        '''
        #Check to see if this Component already exists
        comp = self.getExistingComponent(name)
        if comp != None:
            return comp[COMPONENTINFO]

        #Create a dictionary record for this component
        self.components[name] = None
        temp = {}
        try:
            temp[HANDLE] = h  #Handle of the component that is being activated
            temp[NAME] = name  #Name-redundant but possibly useful
            temp[EXE] = exe  #Python module containing servant implementation
            temp[TYPE] = idl_type  #The type of the component to instantiate
            temp[POA] = self.createPOAForComponent(name)  #POA for this component
            temp[POAOFFSHOOT] = temp[POA].create_POA("OffShootPOA", ACSCorba.getPOAManager(), self.offShootPolicies)
            temp[PYCLASS] = None  #Class object used for this component
            temp[PYREF] = None  #Reference to the python object
            temp[CORBAREF] = None  #Reference to the CORBA object
            temp[COMPONENTINFO] = None  #An IDL struct given to manager
            temp[PYCLASS] = temp[TYPE].split(':')[1].split('/').pop() #get class name
            temp[COMPMODULE] = __import__(temp[EXE], globals(), locals(), [temp[PYCLASS]]) #get module
            
            try:
                temp[PYCLASS] = temp[EXE].split('.').pop() #get class name
                temp[PYCLASS] = temp[COMPMODULE].__dict__.get(temp[PYCLASS]) #get class
                temp[PYREF] = instance(temp[PYCLASS]) #create Python object
            except Exception, e:
                temp[PYCLASS] = temp[COMPMODULE].__dict__.get(temp[PYCLASS]) #get class
                temp[PYREF] = instance(temp[PYCLASS]) #create Python object
                
        except Exception, e:
            self.logger.logWarning("Failed to create Python object for: " + name)
            print_exc()
            self.failedActivation(temp)
            return None

        #these are some non-standard members needed by the component simulator
        temp[PYREF].ir = temp[TYPE]
        temp[PYREF].library = temp[EXE]

        #instance(...) does not call the constructor!
        try:
            temp[PYREF].__init__()
        except:
            print_exc()
            self.logger.logWarning("Standard constructor does not exist for: " + name)

        #Check to see if its derived from ContainerServices
        if isinstance(temp[PYREF], ContainerServices):
            temp[PYREF].setAll(temp[NAME],  #string-name of component
                               self.token,   #Security handle from manager
                               temp[HANDLE],  #Security handle from manager
                               self.activateOffShoot  #Container's method
                               )

        #Check to see if it's an ACSComponent next
        if isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent):
            temp[PYREF].setName(temp[NAME])
            
        #Check to see if it's derived from ComponentLifeCycle next!!!
        #If it is, we have to mess with the state model and invoke the lifecycle
        #methods accordingly.
        if isinstance(temp[PYREF], ComponentLifecycle):
            try:
                #Have to mess with the state model
                if isinstance(temp[PYREF], ACSComponent):
                    #this assumes the component's constructor will NOT change
                    #the state!
                    temp[PYREF].setComponentState(ACS.COMPSTATE_INITIALIZING)
                #actually initialize the sucker
                temp[PYREF].initialize()
                #if it's an ACSComponent...
                temp_state = temp[PYREF]._get_componentState()
                if (isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent)) and (temp_state==ACS.COMPSTATE_INITIALIZED or temp_state==ACS.COMPSTATE_INITIALIZING):
                    #good...initialize did not fail.
                    temp[PYREF].setComponentState(ACS.COMPSTATE_INITIALIZED)
                elif isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent):
                    #bad...the developer has changed the state from the initialize method.  warn the user but continue
                    self.logger.logWarning("initialize method of " + 
                                           "ComponentLifecycle failed for the '" +
                                           temp[NAME] + "' component changed the component's state to something unexpected!")
                    
            except ComponentLifecycleException, e:
                print_exc()
                self.logger.logWarning("initializeComponent method of " + 
                                       "ComponentLifecycle failed for the '" +
                                       temp[NAME] + "' component with this message: " +
                                       str(e.args))
                #Have to mess with the state model
                if isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent):
                    temp[PYREF].setComponentState(ACS.COMPSTATE_ERROR)
                    
            except Exception, e:
                print_exc()
                self.logger.logCritical("initializeComponent method of " + 
                                        "ComponentLifecycle failed for the '" +
                                        temp[NAME] + "'.\n" + str(e.args) + "\nDestroying!")
                self.failedActivation(temp)
                return None

            try:
                temp[PYREF].execute()
                
                #Have to mess with the state model
                temp_state = temp[PYREF]._get_componentState()
                if (isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent)) and (temp_state==ACS.COMPSTATE_INITIALIZED or temp_state==ACS.COMPSTATE_OPERATIONAL):
                    temp[PYREF].setComponentState(ACS.COMPSTATE_OPERATIONAL)
                elif isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent):
                    #bad...the developer has changed the state from the initialize method.  warn the user but continue
                    self.logger.logWarning("execute method of " + 
                                           "ComponentLifecycle failed for the '" +
                                           temp[NAME] + "' component: changed the component's state to something unexpected!")
            except ComponentLifecycleException, e:
                print_exc()
                self.logger.logWarning("executeComponent method of " + 
                                       "ComponentLifecycle failed for the '" +
                                       temp[NAME] + "' component with this message: " +
                                       str(e.args))
                #Have to mess with the state model
                if isinstance(temp[PYREF], ACSComponent) or isinstance(temp[PYREF], CharacteristicComponent):
                    temp[PYREF].setComponentState(ACS.COMPSTATE_ERROR)

                
            except Exception, e:
                print_exc()
                self.logger.logCritical("executeComponent method of " + 
                                        "ComponentLifecycle failed for the '" +
                                        temp[NAME] + "'.\n" + str(e.args) + "\nDestroying!")
                self.failedActivation(temp)
                return None


        #DWF-should check to see if it's derived from CharacteristicComponent next!!!
            
        #Next activate it as a CORBA object.
        try:
            temp[POA].activate_object_with_id(temp[NAME], temp[PYREF])
            temp[CORBAREF] = temp[POA].servant_to_reference(temp[PYREF])
            if(temp[CORBAREF]==None):
                self.logger.logWarning("CORBA object Nil for: " + name)
                self.failedActivation(temp)
                return None
            #hack to give the component access to it's own CORBA reference
            temp[PYREF]._corbaRef = temp[CORBAREF]
        except:
            print_exc()
            self.logger.logWarning("Failed to create CORBA object for: " + name)
            self.failedActivation(temp)
            return None
            
        #Create the structure and give it to manager
        #DWF-FIX ME!!! The next line screws everything up for some reason!
        #temp[PYREF]._get_interface()
        #DWF-warning...this assumes temp[TYPE] is the IR ID...
        interfaces = ["IDL:omg.org/CORBA/Object:1.0", temp[TYPE]]
        temp[COMPONENTINFO] = ComponentInfo(temp[TYPE],  #string type;
                                            temp[EXE],  #string code;
                                            temp[CORBAREF],  #Object reference;
                                            name,  #string name;
                                            [],  #HandleSeq clients;
                                            self.token.h,  #Handle activator;
                                            self.name,   #string container_name;
                                            temp[HANDLE],  #Handle h;
                                            0,  #AccessDescriptor access;
                                            interfaces  #stringSeq interfaces;
                                            )

	    #Make a copy of everything for the container
        self.compHandles[temp[HANDLE]] = temp[NAME]
        self.components[name] = temp

        #keep track of how many components are using the package
        if not self.compModuleCount.has_key(temp[COMPMODULE]):
            self.compModuleCount[temp[COMPMODULE]] = 1
        else:
            self.compModuleCount[temp[COMPMODULE]] = self.compModuleCount[temp[COMPMODULE]] + 1

        self.logger.logInfo("Activated component: " + name)
        
        return self.components[name][COMPONENTINFO]

    #--------------------------------------------------------------------------
    def failedActivation(self, comp_entry):
        '''
        Helper method used to destroy various things if the component cannot
        be created.

        Parameters:
        comp_entry - dictionary describing the component
        '''
        #release the corba reference
        try:
            comp_entry[CORBAREF]._release()
        except:
            pass
        
        #destroy the Offshoot POA
        try:
            comp_entry[POAOFFSHOOT].destroy(FALSE, FALSE)
        except:
            pass
        
        #deactivate the component's underlying CORBA object
        try:
            comp_entry[POA].deactivate_object(comp_entry[NAME])
        except:
            pass
        
        #destroy the component's "personal" POA
        try:
            comp_entry[POA].destroy(FALSE, FALSE)
        except:
            pass

        del self.components[comp_entry[NAME]]
        
    #--ACTIVATOR IDL-----------------------------------------------------------
    def deactivate_components(self, handle_list):
        '''
        Deactivate all components whose handles are given.

        Deactivation is the inverse process of activation: component is detached from
        the POA, and thus made unavailable through CORBA, and its resources are
        freed. If its code-base is no longer used, it is unloaded from memory.

        Parameters: handle_list is a list of integers specifies component handles

        void deactivate_components (in HandleSeq h)
        '''
        
        for handle in handle_list:
            try:
                comp_entry = self.components[self.compHandles[handle]]
            except:
                self.logger.logWarning("No entry for handle: " + str(handle))
                print_exc()
                continue
            self.logger.logInfo("Deactivating component: " + comp_entry[NAME])

            #release the corba reference
            comp_entry[CORBAREF]._release()

            #destroy the Offshoot POA
            comp_entry[POAOFFSHOOT].destroy(FALSE, FALSE)

            #deactivate the component's underlying CORBA object
            comp_entry[POA].deactivate_object(comp_entry[NAME])
            
            #Have to mess with the state model
            if isinstance(comp_entry[PYREF], ACSComponent) or isinstance(comp_entry[PYREF], CharacteristicComponent):
                comp_entry[PYREF].setComponentState(ACS.COMPSTATE_DESTROYING)
                
            try:  #Invoke the cleanUp method if implemented...
                comp_entry[PYREF].cleanUp()
            except Exception, e:
                self.logger.logAlert("Failed to invoke 'cleanUp' LifeCycle method of: " + comp_entry[NAME])
                print_exc()

            #destroy the component's "personal" POA
            comp_entry[POA].destroy(FALSE, FALSE)
            
            #Have to mess with the state model
            if isinstance(comp_entry[PYREF], ACSComponent) or isinstance(comp_entry[PYREF], CharacteristicComponent):
                comp_entry[PYREF].setComponentState(ACS.COMPSTATE_DEFUNCT)
            
            #remove one from the container's list of modules
            self.compModuleCount[comp_entry[COMPMODULE]] = self.compModuleCount[comp_entry[COMPMODULE]] - 1
            
            #if the number of references to this module falls to zero, it should be reloaded
            if self.compModuleCount[comp_entry[COMPMODULE]] == 0:
                try:
                    reload(comp_entry[COMPMODULE])
                except:
                    self.logger.logWarning("Unable to reload:" + str(comp_entry[COMPMODULE]))
                    print_exc()
                    
                #remove it from the container's list
                del self.compModuleCount[comp_entry[COMPMODULE]]
                
            #Finally delete our references so the garbage collector can be used
            del self.components[self.compHandles[handle]]
            del self.compHandles[handle]
            
        return
    #--CONTAINER IDL-----------------------------------------------------------
    def shutdown(self, action):
        '''
        Shutdown the Container.

        Normally invoked via CORBA but can also "self terminate" so to speak.

        Parameters:
        - action is an encrypted value that tells the container what action to take
        
        oneway void shutdown (in unsigned long action)
        '''        
        action = (action >> 8) & 0xFF

        if (action == ACTIVATOR_EXIT) or (action == ACTIVATOR_REBOOT) or (action == ACTIVATOR_RELOAD):
            
            self.logger.logTrace("Shutting down container: " + self.name)

            #Logout from manager
            ACSCorba.getManager().logout(self.token.h)

            if (action == ACTIVATOR_REBOOT) or (action == ACTIVATOR_RELOAD):
                print "Container.shutdown(): Action may not work correctly...-", str(action)
                self.__init__(self.name)
            else:
                #tell the main thread of execution to stop
                self.running = 0
        else:
            self.logger.logWarning("Unable to process 'shutdown' request at this time: " + str(action))
        
    #----------------------------------------------------------------------------
    def set_component_shutdown_order(self, handles):
        '''
        Set component shutdown order.

        void set_component_shutdown_order(in HandleSeq h);
        '''
        self.shutdownHandles = handles 
    #----------------------------------------------------------------------------
    def get_component_info(self, handles):
        '''
        Returns information about a subset of components that are currently hosted by
        the Container.
        
        Note:  If the list of handles is empty, information about all components hosted
        by the activator is returned!

        Parmaters: handles is a sequence of integers specifiying component handles.
        Return: Information about the selected components.
        
        ComponentInfoSeq get_component_info (in HandleSeq h);
        '''
        return_seq=[]
        
        if (handles == None) or (handles == []):
            for record in self.components.keys():
                return_seq.append(self.components[record][COMPONENTINFO])
            return return_seq

        for handle in handles:
            if self.compHandles.has_key(handle):
                return_seq.append(self.components[self.compHandles[handle]][COMPONENTINFO])
            else:
                self.logger.logWarning("Container has no components with handle:" + str(handle))
        return return_seq
    #--------------------------------------------------------------------------
    def getCDBInfo(self):
        '''
        getCDBInfo is a helper method which is responsible for retrieving info
        from the CDB associated with this container.

        Parameters: None

        Return: None

        Raises: ???
        '''
        #obtain generic container information
        try:
            self.cdbContainerInfo = self.cdbAccess.getElement("MACI/Containers/"  + self.name, "Container")
        except:
            self.logger.logDebug("No container information found in the CDB")
            return

        #get a list of libraries to preload
        # [{'string': 'baci'}]
        temp_list = self.cdbAccess.getElement("MACI/Containers/" + self.name, "Container/Autoload/cdb:_")
        
        #get rid of libraries that can't be found!
        for temp_dict in temp_list:
            package = temp_dict['string']
            package = findFile("bin/" + str(package))[0]
            if package != "":
                #if it really exists add it
                self.autoLoadPackages.append(package)
            else:
                self.logger.logAlert("The '" + str(temp_dict['string']) + "' Python script specified by this container's CDB Autoload element cannot be found!")
                
        #now try loading the packages!
        for temp_package in self.autoLoadPackages:
            try:
                execfile(temp_package)
            except:
                self.logger.logCritical("There was a problem autoloading the '" + str(temp_package) + "' Python script!")
                print_exc()
            
    #--------------------------------------------------------------------------
    def configCORBA(self):
        '''
        configCORBA is a helper method responsible for initializing the ORB,
        POAs, etc.

        Parameters: None

        Return: None

        Raises: ???
        '''

        #Create the Container's POA
        try:
            cont_policies = []  #CORBA.PolicyList
            cont_policies.append(ACSCorba.getPOARoot().create_id_assignment_policy(PortableServer.USER_ID))
            cont_policies.append(ACSCorba.getPOARoot().create_lifespan_policy(PortableServer.PERSISTENT))
            cont_policies.append(ACSCorba.getPOARoot().create_request_processing_policy(PortableServer.USE_ACTIVE_OBJECT_MAP_ONLY))
            cont_policies.append(ACSCorba.getPOARoot().create_servant_retention_policy(PortableServer.RETAIN))
            self.containerPOA = ACSCorba.getPOARoot().create_POA("ContainerPOA", ACSCorba.getPOAManager(), cont_policies)
            for policy in cont_policies:
                policy.destroy()
        except Exception, e:
            self.logger.logWarning("Unable to create the container's POA - " + str(e))
            print_exc()
            raise CouldntCreateObjectExImpl()
            
        #Create the Components POA
        try:
            self.compPolicies.append(ACSCorba.getPOARoot().create_id_assignment_policy(PortableServer.USER_ID))
            self.compPolicies.append(ACSCorba.getPOARoot().create_lifespan_policy(PortableServer.PERSISTENT))
            self.compPolicies.append(ACSCorba.getPOARoot().create_request_processing_policy(PortableServer.USE_SERVANT_MANAGER))
            self.compPolicies.append(ACSCorba.getPOARoot().create_servant_retention_policy(PortableServer.RETAIN))
            self.componentPOA = ACSCorba.getPOARoot().create_POA("ComponentPOA", ACSCorba.getPOAManager(), self.compPolicies)
        except Exception, e:
            self.logger.logWarning("Unable to create the components' POA - " + str(e))
            print_exc()
            raise CouldntCreateObjectExImpl()

        #Create the Offshoot Policies
        try:
            self.offShootPolicies.append(ACSCorba.getPOARoot().create_id_assignment_policy(PortableServer.SYSTEM_ID))
            self.offShootPolicies.append(ACSCorba.getPOARoot().create_lifespan_policy(PortableServer.TRANSIENT))
            self.offShootPolicies.append(ACSCorba.getPOARoot().create_request_processing_policy(PortableServer.USE_ACTIVE_OBJECT_MAP_ONLY))
            self.offShootPolicies.append(ACSCorba.getPOARoot().create_servant_retention_policy(PortableServer.RETAIN))
        except Exception, e:
            self.logger.logWarning("Unable to create the OffShoots' POA - " + str(e))
            print_exc()
            raise CouldntCreateObjectExImpl()
        
        # register this object with the Container POA and have
        # it come alive
        try:
            self.containerPOA.activate_object_with_id(self.name, self)
        except Exception, e:
            self.logger.logWarning("Unable to activate this container as a CORBA servant - " +
                                   str(e))
            print_exc()
            raise CORBAProblemExImpl()
    #--------------------------------------------------------------------------
    def getManagerHost(self):
        '''
        Helper function returns a string consisting of managers host.

        Return: hostname where manager is running.

        Raises: ???
        '''
        temp = ACSCorba.getManagerCorbaloc()
        # words end up as ['corbaloc', '', 'condor', 'xxxx/Manager']
        words = temp.split(':')
        return words[2]
    #--------------------------------------------------------------------------
    def run(self):
        '''
        Runs the container until a sig-int is caught.

        This is a blocking call!

        Raises: ???
        '''
        signal(SIGINT, self.handler)
        while(self.running):
            sleep(1)
        self.destroyCORBA()
        print "Goodbye"
    #--------------------------------------------------------------------------
    def handler(self, signum, frame):
        '''
        Catches SIGINTs and shuts down the container.

        Used only by the run method.

        Parameters: signum is the signal being caught
        frame is the execution frame.

        Return: None

        Raises: ???
        '''
        #to make pychecker happy
        signum = None

        #to make pychecker happy
        frame = None
        
        print "-->Signal Interrupt caught...shutting everything down cleanly"

        #Destroy what manager has told us about first
        self.deactivate_components(self.shutdownHandles)
        self.shutdownHandles = []
        #Double-check to see if there's any extra components manager did not
        #let us know about!
        self.deactivate_components(self.compHandles.keys())
        
        self.shutdown(ACTIVATOR_EXIT<<8)
    #--------------------------------------------------------------------------
    def createPOAForComponent(self, comp_name):
        '''
        Creates a new POA that is responsible for exactly one component and
        the new POA is created as a child of the ComponentPOA.

        Parameters: comp_name is the components stringified name.

        Return: a new POA.

        Raises: ???
        '''
        return self.componentPOA.create_POA("ComponentPOA" + comp_name, ACSCorba.getPOAManager(), self.compPolicies)
    #--------------------------------------------------------------------------   
    def destroyCORBA(self):
        '''
        Helper function designed to shutdown/destroy all CORBA associated with
        this specific container.

        Raises: ???
        '''
        for policy in self.compPolicies:
            policy.destroy()

        for policy in self.offShootPolicies:
            policy.destroy()

        try:
            self.corbaRef._release()
            self.componentPOA.destroy(FALSE, FALSE)
            self.containerPOA.destroy(FALSE, FALSE)
        except Exception, e:
            self.logger.logWarning("Failed to destroy the container's CORBA object: " + str(e))
            print_exc()
    #--------------------------------------------------------------------------
    def getExistingComponent(self, name):
        '''
        Searches to see if the component "name" has already been activated by
        this container.

        Parameters: name of the component.

        Return: component record if found; else None.
        '''
        if self.components.has_key(name):
            return self.components[name]
        else:
            return None
    #--------------------------------------------------------------------------
    #--CONTAINER SERVICES METHODS----------------------------------------------
    #--------------------------------------------------------------------------
    def activateOffShoot(self, comp_name, os_corba_ref):
        '''
        Activates an OffShoot derived object.

        Actually this will work on any CORBA object because its Python.

        Parameters:
        - comp_name is the components name.
        - os_corba_ref is a reference to the Python object to become a CORBA object.

        Return: a reference to the CORBA object that almost definitely needs to
        be narrowed to the correct type.  If anything goes wrong though, returns
        None.

        Raises: ???
        '''
        comp = self.getExistingComponent(comp_name)
        if comp == None:
            self.logger.logWarning("Component '" + comp_name + "' does not exist")
            return None
        elif not isinstance(os_corba_ref, OffShoot):
            #Not an offshoot but try activating it anyways!
            self.logger.logWarning("Not an OffShoot '" + str(os_corba_ref) + "'")
            
        try:
            comp[POAOFFSHOOT].activate_object(os_corba_ref)
            return comp[POAOFFSHOOT].servant_to_reference(os_corba_ref)
        except Exception, e:
            self.logger.logWarning("Unable to activate '" + str(os_corba_ref) + "'")
            print_exc()
            return None    
    #--------------------------------------------------------------------------
    def getMyCorbaRef(self):
        '''
        Overriden from BaseClient
        '''
        
        #if this object has not already been activated as a CORBA object...
        if self.corbaRef == None:
            try:
                #...activate it using the default POA
                self.corbaRef = self.containerPOA.servant_to_reference(self)
            except Exception, e:
                self.logger.logWarning("Cannot activate self as a CORBA servant")
                print_exc()
                raise CORBAProblemExImpl()

            #sanity check
            if self.corbaRef == None:
                # without a client, we can't go on
                self.logger.logWarning("Cannot activate self as a CORBA servant")
                raise CORBAProblemExImpl()
        
            #OK to return at this point
            return self.corbaRef
        
        #otherwise return the saved reference
        else:
            return self.corbaRef
    #--------------------------------------------------------------------------
    def getCode(self):
        '''
        Overriden from BaseClient
        '''
        if self.canRecover:
            return "AR"
        else:
            return "A"
    #--------------------------------------------------------------------------
