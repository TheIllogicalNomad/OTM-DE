
package org.opentravel.schemas.trees.type;

import org.eclipse.jface.viewers.ViewerSorter;
import org.opentravel.schemas.node.Node;

/**
 * @author Dave Hollander
 * 
 */
public class TypeTreeSorter extends ViewerSorter {
    @Override
    public int category(final Object element) {
        // System.out.println("TypeTreeSorter:category() - n = "+((Node)element).getName());
        final Node n = (Node) element;
        if (n.isBuiltIn()) {
            return 3;
        }
        if (n.isXSDSchema()) {
            return 3;
        }
        if (n.isLibrary()) {
            return 1;
        }
        return 0;
    }

}
