/*
 * TestTreeExplorer.java
 *
 * Created on September 20, 2002, 5:02 PM
 */

package cern.gp.explorer.test;

import cern.gp.explorer.ListExplorer;
import cern.gp.explorer.test.helpers.SimpleDemoBean;
import cern.gp.nodes.NodeFactory;
import cern.gp.windows.WindowUtils;
import cern.gp.nodes.GPNode;

/**
 * A simple test driver for the TreeTableExplorer
 * @todo test all functionality of the TreeableExplorer
 * 
 * @version $Revision: 1.2 $ $Date: 2006/09/25 08:52:36 $ $Author: acaproni $
 * @author  Vito Baggiolini
 */
public class TestListExplorer {
  
  /** Creates a new instance of TestTreeExplorer */
  public TestListExplorer() {
  }

  public static void main(String[] args) throws Exception {
    int numberOfBeans = 6;

    // create the beans you want to display
    SimpleDemoBean[] beans = new SimpleDemoBean[numberOfBeans];
    for (int ix=0; ix<numberOfBeans; ix++) {
      beans[ix] = new SimpleDemoBean("bean_" + ix);
    }
    
    // create nodes associated with the beans
    GPNode[] nodes = NodeFactory.createNode(beans);

    // create the explorer and set the columns it shall display
    ListExplorer expl = new ListExplorer(nodes);

    //  open the explorer in a NetBeans "Mode"
    WindowUtils.openInMode(expl, "TestListExplorer");
    expl.requestFocus();
  }
}