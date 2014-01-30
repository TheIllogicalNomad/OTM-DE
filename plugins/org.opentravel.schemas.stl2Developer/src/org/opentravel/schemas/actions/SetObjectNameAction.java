
package org.opentravel.schemas.actions;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.properties.ExternalizedStringProperties;
import org.opentravel.schemas.properties.StringProperties;
import org.opentravel.schemas.stl2developer.MainWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a prototype for migrating actions out of the OtmActions. At this point, i use OtmActions
 * because it has the text handlers. TODO - redo text handlers to support direct action dispatch.
 * 
 * @author Dave Hollander
 * 
 */
public class SetObjectNameAction extends OtmAbstractAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetObjectNameAction.class);

    private final static StringProperties propsDefault = new ExternalizedStringProperties(
            "action.setName");

    public SetObjectNameAction(final MainWindow mainWindow) {
        super(mainWindow, propsDefault);
    }

    /**
	 *
	 */
    public SetObjectNameAction(final MainWindow mainWindow, final StringProperties props) {
        super(mainWindow, props);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#runWithEvent(org.eclipse.swt.widgets.Event)
     */
    @Override
    public void runWithEvent(Event event) {
        String newName = "";
        if (event.widget instanceof Text) {
            newName = ((Text) event.widget).getText();
        }
        Node n = (Node) getMainController().getCurrentNode_FacetView();
        if (n != null) {
            // https://jira.sabre.com/browse/OTA-772
            if (n.isProperty())
                n = n.getOwningComponent(); // set postNode() in FacetView
            n.setName(newName);
            getMainController().refresh();
        }
        LOGGER.debug("Changed name to " + n);
    }
}
