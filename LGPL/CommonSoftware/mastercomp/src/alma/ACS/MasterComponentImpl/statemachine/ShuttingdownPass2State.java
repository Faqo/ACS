/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2011
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/
package alma.ACS.MasterComponentImpl.statemachine;

import alma.ACS.SUBSYSSTATE_SHUTTINGDOWN_PASS2;
import alma.acs.genfw.runtime.sm.AcsDoActivity;
import alma.acs.genfw.runtime.sm.AcsSimpleState;
import alma.acs.genfw.runtime.sm.AcsState;
import alma.acs.genfw.runtime.sm.AcsStateActionException;
import alma.acs.logging.AcsLogger;

public class ShuttingdownPass2State extends OfflineSubStateAbstract implements AcsSimpleState
{
	/**
	 * Would be nice to mark this field final, but the AcsDoActivity requires the nextState
	 * which may not be constructed yet when this class's ctor is called.
	 */
	private AcsDoActivity m_doActivity;
	
    public ShuttingdownPass2State(AlmaSubsystemContext superContext, OfflineState context, AcsLogger logger) {
        super(superContext, context, logger);
    }
    
	public String stateName() {
		return SUBSYSSTATE_SHUTTINGDOWN_PASS2.value;
	}

	/**
	 * @see alma.ACS.MasterComponentImpl.statemachine.AcsState#getStateHierarchy()
	 */
	public AcsState[] getStateHierarchy() {
		return new AcsState[] {this};
	}

	public void activate(String eventName) {
		synchronized (m_superContext) {		
			m_offlineContext.setSubstate(this, eventName);
		}
	}
	

	public void entry() {
		if (m_doActivity == null) {
			m_doActivity = new AcsDoActivity(
					"ShuttingdownPass2", 
					m_superContext.m_stateShutdown, m_superContext.m_stateError, 
					logger, m_superContext.getSharedActivityExecutor() ) 
					{
						public void runActions() throws AcsStateActionException  {
							m_superContext.shutDownSubsysPass2();
						}
					};			
		}
		// perform do/ action asynchronously
		m_doActivity.execute();
	}

	public void exit() {
		m_doActivity.terminateActions();
	}
}
