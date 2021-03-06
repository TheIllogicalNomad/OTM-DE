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
