
package org.opentravel.schemas.trees.type;

import org.eclipse.jface.viewers.Viewer;
import org.opentravel.schemas.node.Node;

/**
 * Filter types for Id Reference Objects. For release 2.2 it will allow type assignment with list of
 * VWA, Core and Business objects and their aliases.
 * 
 * @author Pawel Jedruch
 * 
 */
public class TypeTreeIdReferenceTypeOnlyFilter extends TypeSelectionFilter {

    @Override
    public boolean isValidSelection(Node n) {
        return n.isValueWithAttributes() || n.isCoreObject() || n.isBusinessObject() || n.isAlias();
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element == null || !(element instanceof Node)) {
            return false;
        }
        Node n = (Node) element;
        return isValidSelection(n) || hasValidChildren(n);
    }

}
