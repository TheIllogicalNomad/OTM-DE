
package org.opentravel.schemas.controllers;

import java.util.List;

import org.opentravel.schemas.node.Node;

/**
 * Base Interface for all controllers.
 * 
 * @author Dave Hollander
 * 
 */
public interface OtmController {

    /**
     * Command the view to refresh its contents.
     */
    void refresh();

    /**
     * @return selected node. When more than one node is selected, it determines the best selection
     *         to return.
     */
    Node getSelected();

    /**
     * @return list of selected nodes.
     */
    List<Node> getSelections();

}
