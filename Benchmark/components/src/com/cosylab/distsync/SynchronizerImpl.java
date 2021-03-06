/*
 * Copyright (c) 2003 by Cosylab d.o.o.
 *
 * The full license specifying the redistribution, modification, usage and other
 * rights and obligations is included with the distribution of this project in
 * the file license.html. If the license is not included you may find a copy at
 * http://www.cosylab.com/legal/abeans_license.htm or may write to Cosylab, d.o.o.
 *
 * THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND, NOT EVEN THE
 * IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR OF THIS SOFTWARE, ASSUMES
 * _NO_ RESPONSIBILITY FOR ANY CONSEQUENCE RESULTING FROM THE USE, MODIFICATION,
 * OR REDISTRIBUTION OF THIS SOFTWARE.
 */

package com.cosylab.distsync;

import java.util.Set;
import java.util.TreeSet;


/**
 * DOCUMENT ME!
 *
 * @author $Author: dfugate $
 * @version $Revision: 1.1 $
 */
public class SynchronizerImpl implements Synchronizer
{
	private Set tokens = new TreeSet();

	/* (non-Javadoc)
	 * @see com.cosylab.distsync.Synchronizer#enter(java.lang.String)
	 */
	synchronized public void enter(String token)
	{
		this.tokens.add(token);
	}

	/* (non-Javadoc)
	 * @see com.cosylab.distsync.Synchronizer#waitAndLeave(java.lang.String)
	 */
	synchronized public void waitAndLeave(String token)
	{
		this.tokens.remove(token);
		System.out.println("TOKENS: " + this.tokens.size());

		if (this.tokens.size() == 0) {
			this.notifyAll();
		} else {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}
	}
}

/* __oOo__ */
