
package org.opentravel.schemas.actions;

import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.node.NodeEditStatus;
import org.opentravel.schemas.node.ServiceNode;
import org.opentravel.schemas.properties.StringProperties;
import org.opentravel.schemas.stl2developer.DialogUserNotifier;
import org.opentravel.schemas.stl2developer.MainWindow;
import org.opentravel.schemas.stl2developer.OtmRegistry;
import org.opentravel.schemas.wizards.TypeSelectionWizard;

/**
 * @author Agnieszka Janowska
 * 
 */
public class AddCRUDQOperationsAction extends OtmAbstractAction {
    public static final String NO_VALID_SELECTION_MSG = "No valid selection";

    /**
	 *
	 */
    public AddCRUDQOperationsAction(final MainWindow mainWindow, final StringProperties props) {
        super(mainWindow, props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        addCRUDQOperations();
    }

    /**
     * Adds 5 operations to selected service node.
     */
    public void addCRUDQOperations() {
        final Node service = mc.getSelectedNode_NavigatorView();
        if (!service.isService()) {
            DialogUserNotifier.openWarning(NO_VALID_SELECTION_MSG,
                    "You can only add operations to services.");
            return;
        }

        // post a business object only Type Selection then pass the selected node.
        final TypeSelectionWizard wizard = new TypeSelectionWizard(service);
        if (wizard.run(OtmRegistry.getActiveShell())) {
            Node subject = wizard.getSelection();
            ((ServiceNode) service).addCRUDQ_Operations(subject);
            mc.refresh(service);
        }
    }

    @Override
    public boolean isEnabled() {
        Node node = getMainController().getSelectedNode_NavigatorView();
        if (node instanceof ServiceNode && node.getEditStatus().equals(NodeEditStatus.FULL))
            return true;
        return false;
    }

}
