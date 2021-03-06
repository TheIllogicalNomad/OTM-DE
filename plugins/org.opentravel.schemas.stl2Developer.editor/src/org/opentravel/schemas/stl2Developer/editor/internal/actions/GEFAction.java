/**
 * Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opentravel.schemas.stl2Developer.editor.internal.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.parts.AbstractEditPartViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.opentravel.schemas.stl2Developer.editor.model.Diagram;

/**
 * @author Pawel Jedruch
 * 
 */
public abstract class GEFAction extends Action {

    private final AbstractEditPartViewer viewer;

    public GEFAction(AbstractEditPartViewer viewer) {
        this(viewer, null);
    }

    public GEFAction(AbstractEditPartViewer viewer, String label) {
        this.viewer = viewer;
        setText(label);
    }

    protected AbstractEditPartViewer getViewer() {
        return viewer;
    }

    protected IStructuredSelection getSelection() {
        return (IStructuredSelection) viewer.getSelection();
    }

    protected Diagram getInput() {
        return (Diagram) viewer.getContents().getModel();
    }

    protected void execute(Command cmd) {
        getViewer().getEditDomain().getCommandStack().execute(cmd);
    }

}
