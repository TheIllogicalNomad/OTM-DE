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
package org.opentravel.schemas.types.whereused;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.node.interfaces.ExtensionOwner;
import org.opentravel.schemas.node.interfaces.WhereUsedNodeInterface;
import org.opentravel.schemas.node.libraries.LibraryNode;
import org.opentravel.schemas.types.TypeProvider;
import org.opentravel.schemas.types.TypeUser;

/**
 * Represents a library (owner) that uses types from a provider library.
 * 
 * @author Dave Hollander
 * 
 */
public class LibraryUserNode extends WhereUsedNode<LibraryNode> implements WhereUsedNodeInterface {
	// private static final Logger LOGGER = LoggerFactory.getLogger(LibraryUserNode.class);

	/**
	 * 
	 * Owner will always be the head of the chain.
	 * 
	 * @param userLibrary
	 *            - owner of this node is the library that uses types from the other library
	 * @param providerLibrary
	 *            is the library that provides types to the user library
	 */
	public LibraryUserNode(LibraryNode userLibrary, LibraryNode providerLibrary) {
		super(userLibrary, providerLibrary);
		if (userLibrary.getChain() != null && userLibrary.getChain().getHead() != null)
			owner = userLibrary.getChain().getHead();
		// this.parent = providerLibrary;
		labelProvider = simpleLabelProvider(userLibrary.getName());
		imageProvider = nodeImageProvider(providerLibrary.getOwningComponent());
	}

	@Override
	public String getDecoration() {
		String decoration = "  ";
		if (owner.getChain() != null)
			decoration += "(version " + owner.getVersion_Major() + "+)";
		decoration += "  " + this.getClass().getSimpleName();
		return decoration;
	}

	@Override
	public String getLabel() {
		return owner instanceof TypeProvider ? labelProvider.getLabel() + " ("
				+ ((TypeProvider) owner).getWhereUsedAndDescendantsCount() + ")" : labelProvider.getLabel();
	}

	/**
	 * Get all of the components that use any any types from of the owning library or chain.
	 * 
	 * @return new list of children
	 */
	@Override
	public List<Node> getChildren() {
		List<Node> users = new ArrayList<Node>();
		if (owner == null)
			return Collections.emptyList();
		for (Node l : ((LibraryNode) parent).getWhereUsedHandler().getUsersOfTypesFromOwnerLibrary(owner, true))
			if (l instanceof TypeUser)
				users.add(new TypeUserNode((TypeUser) l));
			else if (l instanceof ExtensionOwner)
				users.add(new ExtensionUserNode((ExtensionOwner) l));
		return users;
	}

	@Override
	public boolean hasChildren() {
		return false;
		// return true; // fixme - only type OWNER has children
	}

}