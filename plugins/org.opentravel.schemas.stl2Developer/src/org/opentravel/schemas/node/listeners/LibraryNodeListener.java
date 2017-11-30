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
package org.opentravel.schemas.node.listeners;

import org.opentravel.schemacompiler.event.OwnershipEvent;
import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.node.facets.ContextualFacetNode;
import org.opentravel.schemas.node.libraries.LibraryNode;
import org.opentravel.schemas.types.TypeUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Add or remove children from libraries.
 * 
 * @author Dave Hollander
 *
 */
public class LibraryNodeListener extends NodeIdentityListener implements INodeListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(LibraryNodeListener.class);

	public LibraryNodeListener(Node node) {
		super(node);
	}

	@Override
	public void processOwnershipEvent(OwnershipEvent<?, ?> event) {
		Node affectedNode = getAffectedNode(event);
		LOGGER.debug("Library Ownership event: " + event.getType() + " this = " + thisNode + " affected = "
				+ affectedNode);
		LibraryNode ln = (LibraryNode) thisNode;

		switch (event.getType()) {
		case MEMBER_ADDED:
			LOGGER.debug("Member adding Ownership change event: added " + affectedNode + " to " + thisNode);
			// No listener on TLObject - do nothing
			if (affectedNode == null)
				return;
			// In version 1.5 there will be an event for contextual facets, ignore them
			if (affectedNode instanceof ContextualFacetNode)
				if (!((ContextualFacetNode) affectedNode).canBeLibraryMember())
					break;
			ln.getChildrenHandler().add(affectedNode);
			// TODO - versions, aggregates and where used
			break;

		// // Add the affected node to this library
		// if (affectedNode instanceof ContextualFacetNode) {
		// if (affectedNode.getParent() != null)
		// break; // do nothing
		// if (!((ContextualFacetNode) affectedNode).canBeLibraryMember())
		// break; // do nothing
		// // else
		// // LOGGER.debug("Contextual facet with no parent.");
		// }
		// if (affectedNode instanceof VersionNode)
		// ln.linkMember(((VersionNode) affectedNode).getNewestVersion());
		// else
		// ln.linkMember(affectedNode);
		// if (ln.isInChain()) {
		// ln.getChain().add((ComponentNode) affectedNode);
		// }
		// break;

		case MEMBER_REMOVED:
			LOGGER.debug("Ownership change event: removing " + affectedNode + " from " + thisNode);
			// Remove affected from this library
			if (affectedNode == null || affectedNode.getParent() == null)
				return; // happens during deletes

			// Clear assigned types
			// FIXME - should this be part of delete()
			for (TypeUser n : affectedNode.getDescendants_TypeUsers())
				if (n.getAssignedType() != null)
					n.getAssignedType().getWhereAssignedHandler().remove(n);
			if (affectedNode instanceof TypeUser)
				if (((TypeUser) affectedNode).getAssignedType() != null)
					((TypeUser) affectedNode).getAssignedType().getWhereAssignedHandler()
							.remove((TypeUser) affectedNode);

			// FIXME - should remove be destructive? It is used in addMember to move and this breaks that.
			// affectedNode.delete();

			ln.getChildrenHandler().remove(affectedNode);
			// TODO - versions, aggregates
			break;

		// LOGGER.debug("ERROR -- null parent.");
		// Node parent = affectedNode.getParent();
		//
		// if (parent instanceof VersionNode)
		// parent = parent.getParent();
		// if (parent == null || parent instanceof ComponentNode) {
		// // LOGGER.debug("Library is not the parent for " + affectedNode);
		// // This must be a ContextualFacet (or similar) and will be removed with parent
		// break; // do nothing
		// }
		// if (ln.getChain() != null)
		// ln.getChain().removeAggregate((ComponentNode) affectedNode);
		//
		// affectedNode.unlinkNode();
		// break;

		default:
			// LOGGER.debug("Unhandled Ownership event: " + event.getType() + " this = " + thisNode + " affected = "
			// + affectedNode);
			break;
		}
	}
}
