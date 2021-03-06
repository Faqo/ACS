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
/*
 * Created on Jun 27, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package alma.acs.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * Is for <code>OutputStream</code>s what <code>StringWriter</code>
 * is for <code>Writer</code>s.
 * 
 * @author hsommer
 *
 */
public class StringOutputStream extends OutputStream
{

	private StringWriter m_stringwriter;

	/**
	 * 
	 */
	public StringOutputStream()
	{
		super();
		m_stringwriter = new StringWriter();
	}


	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException
	{
		m_stringwriter.write(b);
	}

	/**
	 * Return the buffer's current value as a string.
	 */
	public String toString() 
	{
		return m_stringwriter.toString();
	}

}
