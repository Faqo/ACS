/*
 *    ALMA - Atacama Large Millimiter Array
 *    (c) European Southern Observatory, 2002
 *    Copyright by ESO (in the framework of the ALMA collaboration)
 *    and Cosylab 2002, All rights reserved
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, 
 *    MA 02111-1307  USA
 */
package com.cosylab.logging;

import javax.swing.JOptionPane;

import com.cosylab.logging.engine.ACS.ACSRemoteLogListener;
import com.cosylab.logging.engine.RemoteAccess;

/**
 * LCEngine defines output messages to the status panel whenever 
 * any of the control system components reports a message or an error occurs. 
 * This class also applies filters (criteria) to the logs.
 * 
 * Creation date: (10/31/2001 9:20:37 AM)
 * @author: 
 */
public class LCEngine implements Runnable {
	/**
	 * The connection is checked every CHECK_INTERVAL seconds
	 */
	private final int CHECK_INTERVAL = 15;
	
	// The boolean remeber if the client was connected before
	// checking for the connection (needed to understand if the
	// connection has been lost or never happened)
	private boolean wasConnected=false;
	
	private RemoteAccess remoteAccess = null;
	private ACSRemoteLogListener remoteResponseCallback = null;

	/** 
	 * A thread used to set and initialize RemoteAccess
	 *
	 * Constructor parameters:
	 *
	 * @accessType (String) name of the RemoteAccessClass e.g. ACS
	 * class has to be in the package com.cosylab.logging.engine."accessType"
	 * and has to be named "accessType"RemoteAccess
	 * e.g. com.cosylab.logging.engine.ACS.ACSRemoteAccess
	 */
	private class AccessSetter extends Thread {

		public void run() {
			disconnect();
			remoteResponseCallback.reportStatus("Connecting to " + accessType + " remote access...");
			try {
				
				Object[] parameters = { };
				String raName = accessType;
				if (accessType.indexOf(".") == -1)
					raName = "com.cosylab.logging.engine."
							+ accessType
							+ "."
							+ accessType
							+ "RemoteAccess"; // com.cosylab.logging.engine.ACS.ACSRemoteAccess
				//RemoteAccess ra = 
				//	(RemoteAccess) Class.forName(raName).getConstructors()[0].newInstance(
				//		parameters);
				remoteAccess = (RemoteAccess) Class.forName(raName).getConstructors()[0].newInstance(parameters);
				//if (remoteAccess == null)
				//	System.out.println("RemoteAccess == null");
				remoteAccess.initialize();
				remoteAccess.addLogRemoteConnListener(remoteResponseCallback);
			} catch (Throwable e) {
				remoteResponseCallback.reportStatus("Exception occurred when initializing " + accessType + " remote access.");
				System.out.println("Exception in LCEngine$AccessSetter::run(): " + e);
				return;
			}
			if (remoteAccess.isInitialized()) {
				remoteResponseCallback.reportStatus("Connected to " + accessType + " remote access.");
				LCEngine.this.wasConnected=true;
			}
			LoggingClient.getInstance().setConnectionStatus(remoteAccess.isInitialized());
		}
	}

	private java.lang.String accessType = "ACS";
	
	/**
	 * LCEngine constructor comment.
	 * @param logEventListener The lister for log events
	 */
	public LCEngine(ACSRemoteLogListener logEventListener) {
		if (logEventListener==null) {
			throw new IllegalArgumentException("Invalid null listener in constructor");
		}
		remoteResponseCallback=logEventListener;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * LCEngine starts an attempt to connect to the remote system.
	 * Connection is handled in a separate Thread
	 * @see LCEngine$AccessSetter
	 */
	public void connect() {
		new AccessSetter().start();
	}
	
	/**
	 * LCEngine starts an attempt to connect to the remote system.
	 * Connection is handled in a separate Thread
	 * @see LCEngine$AccessSetter
	 */
	public void connect(String accessTyp) {
		this.accessType = accessTyp;
		connect();
	}
	
	/**
	 * LCEngine starts an attempt to connect to the remote system.
	 * Connection is handled in a separate Thread
	 * @see LCEngine$AccessSetter
	 */
	public void disconnect() {
		if (remoteAccess != null && remoteAccess.isInitialized()) {
			try {
				remoteResponseCallback.reportStatus("Disconnecting from " + accessType + " remote access...");
				remoteAccess.destroy();
			} catch (Exception e) {
				remoteResponseCallback.reportStatus("Exception occurred when destroying " + accessType + " remote access.");
				System.out.println("Exception in LCEngine$AccessDestroyer::run(): " + e);
			}
			remoteResponseCallback.reportStatus("Disonnected from " + accessType + " remote access.");
	//		System.out.println("Disonnected from " + accessType + " remote access.");
		}
		remoteAccess = null;
		LoggingClient.getInstance().setConnectionStatus(false);
		LCEngine.this.wasConnected=false;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (2/9/2002 1:29:32 PM)
	 */
	public void exit() {
		disconnect();
		System.exit(0);	
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (2/18/2002 9:58:30 AM)
	 * @return java.lang.String
	 */
	public java.lang.String getAccessType() {
		return accessType;
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (2/18/2002 9:58:30 AM)
	 * @param newAccessType java.lang.String
	 */
	public void setAccessType(String newAccessType) {
		accessType = newAccessType;
	}
	
	/**
	 * 
	 * @return ture if the engine is connected to the notification channel
	 */
	public boolean isConnected() {
		if (remoteAccess==null) {
			return false;
		} else {
			return remoteAccess.isConnected();
		}
	}
	
	/**
	 * The thread that monitors the status of the connection
	 */
	public void run() {
		int attempts = 0;
		while (true) {
			// wait for CHECK_INTERVAL secs..
			try {
				Thread.sleep(CHECK_INTERVAL*1000);
				attempts=0;
			} catch (InterruptedException ie) {
				// If it not possible to sleep than it is better to stop 
				// the thread after 5 attempts
				System.err.println("Error in sleep: "+ie.getMessage());
				ie.printStackTrace(System.err);
				if (++attempts>5) {
					break;
				}
			}
			// Check the connection!
			boolean connected = isConnected();
			LoggingClient.getInstance().setConnectionStatus(connected);
			if (wasConnected && !connected) {
				remoteResponseCallback.reportStatus("Connection lost");
				wasConnected=false;
				// Better otherwise it tries to reconnect every time
				disconnect();
				JOptionPane.showMessageDialog(null,"Connection lost!","LoggingClient error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
