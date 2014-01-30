
package org.opentravel.schemas.trees.type;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.node.ProjectNode;

import org.opentravel.schemacompiler.model.AbstractLibrary;

public class TypeTreeNamespaceFilter extends ViewerFilter {
    private Class<? extends AbstractLibrary> library = null;

    public TypeTreeNamespaceFilter() {
    }

    @Override
    public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
        if (library == null) {
            return true;
        }
        if (element == null || !(element instanceof Node)) {
            return false;
        }
        if (element instanceof ProjectNode)
            return true; // they contain libraries to be checked.
        final Node n = (Node) element;
        final AbstractLibrary libClass = n.getLibrary().getTLaLib();
        final boolean isInstance = library.isInstance(libClass);
        return isInstance;
    }

    public void setLibrary(final Class<? extends AbstractLibrary> lib) {
        library = lib;
    }
}
