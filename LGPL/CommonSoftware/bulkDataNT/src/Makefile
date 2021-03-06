#*******************************************************************************
# ALMA - Atacama Large Millimiter Array
# (c) European Southern Observatory, 2011 
# 
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
# 
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#
# "@(#) $Id: Makefile,v 1.48 2013/02/11 18:37:33 rbourtem Exp $"
#
# Makefile of ........
#
# who       when      what
# --------  --------  ----------------------------------------------
# bjeram  2011-04-19  created
#

#*******************************************************************************
# This Makefile follows VLT Standards (see Makefile(5) for more).
#*******************************************************************************
# REMARKS
#    None
#------------------------------------------------------------------------

# at this moment we use bulk data (NT) just in C++, and there is just C++ implementation
#MAKE_ONLY=C++,Python
MAKE_NOIFR_CHECK = on # it is DDS IDL, and we do not need to check it

################################################################################
#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#  the code from here till  ... endif
#  will built just if NDDSHOME is defeined i.e. RTI DDS is isntalled !!!!!!!
################################################################################
ifdef NDDSHOME

ARCH = $(shell uname -m)
GCC_VERSION_MAJOR = $(shell echo __GNUC__ | gcc -E -xc - | tail -n 1)
ifneq ($(GCC_VERSION_MAJOR),4)
        echo "ERROR: not supported gcc version for RTI DDS: $(GCC_VERSION_MAJOR)"
endif
GCC_VERSION_MINOR = $(shell echo __GNUC_MINOR__ | gcc -E -xc - | tail -n 1)
ifeq ($(GCC_VERSION_MINOR),1)
        RTIDDS_GCC_VER=4.1.2
else
        RTIDDS_GCC_VER=4.4.5
endif


# user definable C-compilation flags
USER_CFLAGS = -DRTI_UNIX -DRTI_LINUX -g -O0

#
# additional include and library search paths
USER_INC = -I$(NDDSHOME)/include -I$(NDDSHOME)/include/ndds
ifeq ($(ARCH),x86_64)
	USER_LIB = -L$(NDDSHOME)/lib/x64Linux2.6gcc$(RTIDDS_GCC_VER)
else
	USER_LIB = -L$(NDDSHOME)/lib/i86Linux2.6gcc$(RTIDDS_GCC_VER)
endif
#
# MODULE CODE DESCRIPTION:
# ------------------------
# As a general rule:  public file are "cleaned" and "installed"  
#                     local (_L) are not "installed".

#
# C programs (public and local)
# -----------------------------
EXECUTABLES     =  bulkDataNTGenSender bulkDataNTGenReceiver bulkDataNTGenReceiverSender ddsPubTest ddsSubTest
EXECUTABLES_L   = 

#
# <brief description of xxxxx program>
xxxxx_OBJECTS   =	
xxxxx_LDFLAGS   =
xxxxx_LIBS      =

#
# special compilation flags for single c sources
#yyyyy_CFLAGS   = 

#
# Includes (.h) files (public only)
# ---------------------------------
INCLUDES        = bulkDataNTCallback.h bulkDataNTDDS.h bulkDataNTDDSPublisher.h bulkDataNTDDSSubscriber.h \
				bulkDataNT.h bulkDataNTPlugin.h bulkDataNTReaderListener.h bulkDataNTReceiverFlow.h \
				bulkDataNTReceiverImpl.h bulkDataNTReceiverImpl.i bulkDataNTReceiverStream.h bulkDataNTReceiverStream.i \
				bulkDataNTSenderFlow.h bulkDataNTSenderImpl.h bulkDataNTSenderStream.h bulkDataNTStream.h bulkDataNTSupport.h \
				bulkDataNTFlow.h bulkDataNTWriterListener.h bulkDataNTDDSLoggable.h bulkDataNTSenderFlowCallback.h \
				bulkDataNTConfiguration.h bulkDataNTConfigurationParser.h \
				bulkDataNTArrayThread.h bulkDataNTPosixHelper.h bulkDataNTThreadSyncGuard.h \
				bulkDataNTGenEx.h


#
# Libraries (public and local)
# ----------------------------
LIBRARIES       = bulkDataNT bulkDataNTSender bulkDataNTReceiver bulkDataNTSenderImpl bulkDataNTThreadUtils bulkDataNTEx	
LIBRARIES_L     =

#
# <brief description of lllll library>
bulkDataNT_OBJECTS   = bulkDataNTDDS bulkDataNTStream bulkDataNTConfiguration bulkDataNTConfigurationParser \
						bulkDataNT bulkDataNTSupport bulkDataNTPlugin bulkDataNTDDSLoggable bulkDataNTLibMgmt
 
bulkDataNT_LIBS = ACSErrTypeCommon ACS_BD_Errors ACS_DDS_Errors xerces-c  nddscpp nddsc nddscore acsnc

bulkDataNTSender_OBJECTS    = bulkDataNTSenderStream bulkDataNTSenderFlow bulkDataNTDDSPublisher \
							  bulkDataNTWriterListener bulkDataNTSenderFlowCallback
bulkDataNTSender_LIBS = bulkDataNT bulkDataSenderStubs baci acsnc

bulkDataNTSenderImpl_OBJECTS = bulkDataNTSenderImpl
bulkDataNTSenderImpl_LIBS = bulkDataNTSender acsnc

bulkDataNTThreadUtils_OBJECTS = bulkDataNTPosixHelper bulkDataNTThreadSyncGuard
bulkDataNTThreadUtils_LIBS = pthread ACE logging acsThread acstime

bulkDataNTEx_OBJECTS = bulkDataNTGenEx
bulkDataNTEx_LD_FLAGS = 
bulkDataNTEx_LIBS = acsnc 

bulkDataNTReceiver_OBJECTS   = bulkDataNTReceiverFlow bulkDataNTDDSSubscriber \
							   bulkDataNTReaderListener bulkDataNTCallback
bulkDataNTReceiver_LIBS = bulkDataNT RepeatGuard bulkDataStubs


bulkDataNTGenSender_OBJECTS = bulkDataNTGenSender
bulkDataNTGenSender_LIBS= bulkDataNTSender acsnc bulkDataStubs

bulkDataNTGenReceiver_OBJECTS = bulkDataNTGenReceiver
bulkDataNTGenReceiver_LIBS= bulkDataNTReceiver 

bulkDataNTGenReceiverSender_OBJECTS = bulkDataNTGenReceiverSender bulkDataNTArrayThread bulkDataNTGenStreamerThread
bulkDataNTGenReceiverSender_LIBS= bulkDataNTSender bulkDataNTReceiver bulkDataNTThreadUtils bulkDataNTEx acsnc

ddsPubTest_OBJECTS=ddsPublisher 
ddsPubTest_LIBS=nddscpp nddsc nddscore pthread ACSErrTypeCommon
ddsSubTest_OBJECTS=ddsSubscriber
ddsSubTest_LIBS=nddscpp nddsc nddscore pthread ACSErrTypeCommon

#
# Scripts (public and local)
# ----------------------------
SCRIPTS         =
SCRIPTS_L       =

#
# TCL scripts (public and local)
# ------------------------------
TCL_SCRIPTS     =
TCL_SCRIPTS_L   =

#
# Python stuff (public and local)
# ----------------------------
PY_SCRIPTS         = bulkDataNTremoteTest
PY_SCRIPTS_L       =

PY_MODULES         =
PY_MODULES_L       =

PY_PACKAGES        =
PY_PACKAGES_L      =
pppppp_MODULES	   =

#
# <brief description of tttttt tcl-script>
tttttt_OBJECTS  =
tttttt_TCLSH    = 
tttttt_LIBS     = 

#
# TCL libraries (public and local)
# ------------------------------
TCL_LIBRARIES   =
TCL_LIBRARIES_L =

#
# <brief description of tttlll library>
tttlll_OBJECTS  = 

endif #ifdef NDDSHOME
################################################################################
#!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#  the code from ifdef NDDSHOME till  here
#  .... will built just if NDDSHOME is defeined i.e. RTI DDS is isntalled !!!!!!!
################################################################################

#
# Configuration Database Files
# ----------------------------
CDB_SCHEMAS = BulkDataNTReceiver BulkDataNTSender acs_rti_dds_qos_profiles

# 
# IDL Files and flags
# 
IDL_FILES = bulkData bulkDataReceiver bulkDataSender bulkDataDistributer
TAO_IDLFLAGS =
bulkDataStubs_LIBS = TAO_AV
bulkDataReceiverStubs_LIBS = baciStubs ACSBulkDataError ACSBulkDataStatus bulkDataStubs
bulkDataSenderStubs_LIBS = baciStubs ACSBulkDataError bulkDataStubs bulkDataReceiverStubs
bulkDataDistributerStubs_LIBS = baciStubs ACSErrTypeCommon ACSBulkDataError bulkDataStubs bulkDataReceiverStubs bulkDataSenderStubs
USER_IDL =
#
# Jarfiles and their directories
#
JARFILES= 
jjj_DIRS=
jjj_EXTRAS= 

ACSERRDEF = ACS_BD_Errors ACS_DDS_Errors ACSBulkDataError ACSBulkDataStatus


#
# java sources in Jarfile on/off
DEBUG= 
#
# ACS XmlIdl generation on/off
#
XML_IDL= 
#
# Java Component Helper Classes generation on/off
#
COMPONENT_HELPERS=
#
# Java Entity Classes generation on/off
#
XSDBIND=
#
# Schema Config files for the above
#
XSDBIND_INCLUDE=
# man pages to be done
# --------------------
MANSECTIONS =
MAN1 =
MAN3 =
MAN5 =
MAN7 =
MAN8 =

#
# local man pages
# ---------------
MANl =

#
# ASCII file to be converted into Framemaker-MIF
# --------------------
ASCII_TO_MIF = 

#
# other files to be installed
#----------------------------
INSTALL_FILES =

#
# list of all possible C-sources (used to create automatic dependencies)
# ------------------------------
CSOURCENAMES = \
	$(foreach exe, $(EXECUTABLES) $(EXECUTABLES_L), $($(exe)_OBJECTS)) \
	$(foreach rtos, $(RTAI_MODULES) , $($(rtos)_OBJECTS)) \
	$(foreach lib, $(LIBRARIES) $(LIBRARIES_L), $($(lib)_OBJECTS))

#
#>>>>> END OF standard rules

#
# INCLUDE STANDARDS
# -----------------

MAKEDIRTMP := $(shell searchFile include/acsMakefile)
ifneq ($(MAKEDIRTMP),\#error\#)
   MAKEDIR := $(MAKEDIRTMP)/include
   include $(MAKEDIR)/acsMakefile
endif


#
# TARGETS
# -------

ifdef NDDSHOME
	
all: do_dds_gen	do_all
	@echo " . . . 'all' done" 

clean : clean_all dds_clean
	@echo " . . . clean done"

install : install_all
	$(AT)cp -f ../config/bulkDataNTDefaultQosProfiles.*.xml $(PRJTOP)/config
	$(AT)chmod $(P644) $(PRJTOP)/config/bulkDataNTDefaultQosProfiles.*.xml
	@echo " . . . installation done"
	
else

.DEFAULT_GOAL := all	
all: do_all
	@echo -e "\e[0;31mWARNING: NDDSHOME is not defined, and thus only code common to old and new BD will be build (ACSERR and IDL) for target all!!!!\e[0m" 

clean : clean_all
	@echo " . . . clean done"

install : install_all
	@echo -e "\e[0;31mWARNING: NDDSHOME is not defined, and thus only code common to old and new BD will be installed (ACSERR and IDL) for target install!!!!\e[0m"
	
endif
clean_dist : clean_all clean_dist_all 
	@echo " . . . clean_dist done"

man   : do_man 
	@echo " . . . man page(s) done"

	
	
do_dds_gen: bulkDataNT.cpp bulkDataNTSupport.cpp bulkDataNTPlugin.cpp bulkDataNT.h  bulkDataNTSupport.h bulkDataNTPlugin.h
	
bulkDataNT.cpp bulkDataNTSupport.cpp bulkDataNTPlugin.cpp bulkDataNT.h  bulkDataNTSupport.h bulkDataNTPlugin.h : ../idl/bulkDataNT.idl
	$(NDDSHOME)/scripts/rtiddsgen -namespace -replace -d ../src ../idl/bulkDataNT.idl; \
	rename .cxx .cpp *.cxx; \
	mv *.h ../include

dds_clean:
	@rm -f bulkDataNT.cpp bulkDataNTSupport.cpp bulkDataNTPlugin.cpp; \
	rm -f ../include/bulkDataNT.h  ../include/bulkDataNTSupport.h ../include/bulkDataNTPlugin.h
#___oOo___
