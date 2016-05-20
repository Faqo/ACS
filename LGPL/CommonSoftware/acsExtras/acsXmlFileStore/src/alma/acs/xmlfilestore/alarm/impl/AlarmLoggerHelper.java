/*
ALMA - Atacama Large Millimiter Array
* Copyright (c) European Southern Observatory, 2016 
* 
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
* 
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
*/
package alma.acs.xmlfilestore.alarm.impl;

import java.util.logging.Logger;

import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ComponentHelper;
import alma.xmlFileStore.AlarmsXmlStorePOATie;
import alma.xmlFileStore.AlarmsXmlStoreOperations;
/** 
 * @author  acaproni
 * @since   ACS 2016.6
 */
public class AlarmLoggerHelper extends ComponentHelper {
	
	/**
	 * Constructor
	 * 
	 * @param containerLogger
	 *            logger used only by the parent class.
	 */
	public AlarmLoggerHelper(Logger containerLogger) {
		super(containerLogger);
	}

	/**
	 * @see alma.acs.container.ComponentHelper#_createComponentImpl()
	 */
	protected ComponentLifecycle _createComponentImpl() {
		return new XmlFileStoreAlarmLoggerImpl();
	}

	/**
	 * @see alma.acs.container.ComponentHelper#_getPOATieClass()
	 */
	protected Class _getPOATieClass() {
		return AlarmsXmlStorePOATie.class;
	}

	/**
	 * @see alma.acs.container.ComponentHelper#getOperationsInterface()
	 */
	protected Class _getOperationsInterface() {
		return AlarmsXmlStoreOperations.class;
	}

}

