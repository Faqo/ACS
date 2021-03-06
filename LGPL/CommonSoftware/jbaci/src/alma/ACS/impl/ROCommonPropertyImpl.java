/*******************************************************************************
 * ALMA - Atacama Large Millimiter Array
 * (c) European Southern Observatory, 2002
 * Copyright by ESO (in the framework of the ALMA collaboration)
 * and Cosylab 2002, All rights reserved
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

package alma.ACS.impl;

import alma.ACS.jbaci.DataAccess;
import alma.ACS.jbaci.MemoryDataAccess;
import alma.ACS.jbaci.PropertyInitializationFailed;

/**
 * Implementation of read-only common property, i.e. type of <code>java.lang.Object</code>.
 * @author <a href="mailto:matej.sekoranjaATcosylab.com">Matej Sekoranja</a>
 * @version $id$
 */
public abstract class ROCommonPropertyImpl extends CommonPropertyImpl {

	/**
	 * Constructor with memory data access.
	 * @param propertyType		property <code>Class</code> type, non-<code>null</code>.
	 * @param name				property name, non-<code>null</code>.
	 * @param parentComponent	parent component, non-<code>null</code>.
	 * @throws PropertyInitializationFailed exception is thrown on failure
	 */
	public ROCommonPropertyImpl(
		Class propertyType,
		String name,
		CharacteristicComponentImpl parentComponent)
		throws PropertyInitializationFailed {
		this(propertyType, name, parentComponent, new MemoryDataAccess());
	}

	/**
	 * Constructor.
	 * @param propertyType		property <code>Class</code> type, non-<code>null</code>.
	 * @param name				property name, non-<code>null</code>.
	 * @param parentComponent	parent component, non-<code>null</code>.
	 * @param dataAccess		data access to be use, non-<code>null</code>.
	 * @throws PropertyInitializationFailed exception is thrown on failure
	 */
	public ROCommonPropertyImpl(
		Class propertyType,
		String name,
		CharacteristicComponentImpl parentComponent,
		DataAccess dataAccess)
		throws PropertyInitializationFailed {
		super(propertyType, name, parentComponent, dataAccess);
	}

}
