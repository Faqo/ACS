/*
 * $Id: StopCapability.java,v 1.2 2006/09/25 08:52:36 acaproni Exp $
 *
 * $Date: 2006/09/25 08:52:36 $
 * $Revision: 1.2 $
 * $Author: acaproni $
 *
 * Copyright CERN, All Rights Reserved.
 */
package cern.gp.capabilities;

import cern.gp.nodes.GPNode;

/**
 * Capability an object implements to give the possibility to be stopped.
 *
 * @version $Revision: 1.2 $  $Date: 2006/09/25 08:52:36 $
 * @author Lionel Mestre
 */
public interface StopCapability extends Capability {
 
  /** 
   * Indicates to this object that it has to be stopped. The object 
   * has to interpret what does stop means in its own context.
   * @param node the node representing the bean on which the capability has been activated
   */
  public void stop(GPNode node);
  
}
