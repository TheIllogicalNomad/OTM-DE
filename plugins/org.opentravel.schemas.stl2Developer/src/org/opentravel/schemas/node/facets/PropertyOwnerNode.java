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
package org.opentravel.schemas.node.facets;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.opentravel.schemacompiler.event.ModelElementListener;
import org.opentravel.schemacompiler.model.TLAttribute;
import org.opentravel.schemacompiler.model.TLFacet;
import org.opentravel.schemacompiler.model.TLFacetType;
import org.opentravel.schemacompiler.model.TLModelElement;
import org.opentravel.schemacompiler.model.TLProperty;
import org.opentravel.schemas.node.CoreObjectNode;
import org.opentravel.schemas.node.ModelNode;
import org.opentravel.schemas.node.Node;
import org.opentravel.schemas.node.TypeProviderBase;
import org.opentravel.schemas.node.VWA_Node;
import org.opentravel.schemas.node.interfaces.INode;
import org.opentravel.schemas.node.interfaces.LibraryMemberInterface;
import org.opentravel.schemas.node.libraries.LibraryNode;
import org.opentravel.schemas.node.listeners.InheritanceDependencyListener;
import org.opentravel.schemas.node.properties.AttributeNode;
import org.opentravel.schemas.node.properties.ElementNode;
import org.opentravel.schemas.node.properties.PropertyNode;
import org.opentravel.schemas.node.properties.PropertyNodeType;
import org.opentravel.schemas.node.properties.PropertyOwnerInterface;
import org.opentravel.schemas.properties.Images;
import org.opentravel.schemas.types.TypeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Property Owners are any "Facet" that can contain children which are properties (elements, attributes, indicators,
 * ect.).
 * 
 * This controller implements the PropertyOwner Interface and represents a variety of TL objects which can contain
 * properties. Property Owners can contain properties as well as other facets. Extension points, simple facets on VWA
 * and Core objects, Operation RQ, RS and Notif messages are also modeled with PropertyOwnerNode.
 * 
 * @author Dave Hollander
 * 
 */
public abstract class PropertyOwnerNode extends TypeProviderBase implements PropertyOwnerInterface, TypeProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(FacetNode.class);

	public PropertyOwnerNode() {
	}

	// Needed for sub-classes
	public PropertyOwnerNode(final TLModelElement obj) {
		super(obj);
		// TLFacet - FacetMO - FacetNode
		// TLListFacet - ListFacetMO - ListFacetNode
		// TLRoleEnumeration - RoleEnumberationMO - RoleFacetNode
		// TLValueWithAttributesFacet - ValueWithAttributesAttributeFacetMO - VWA_AttributeFacetNode
		// TLSimpleFacet - SimpleFacetNode
	}

	@Override
	public void add(final PropertyNode pn, final int index) {
		// Add to children list
		pn.setParent(this);
		// if (index >= 0)
		// linkChild(pn, index);
		// else
		// linkChild(pn);

		// Add to the tl model
		if (index < 0)
			pn.addToTL(this);
		else
			pn.addToTL(this, index);

		// Events are not being thrown (10/14/2017) so force their result
		childrenHandler.clear();
		// clear handlers on any inherited "ghost" facets
		for (ModelElementListener l : getTLModelObject().getListeners())
			if (l instanceof InheritanceDependencyListener)
				((InheritanceDependencyListener) l).run();
		// ((InheritanceDependencyListener) l).run_childrenChanged();
	}

	/**
	 * ******************************************* Base Class Methods
	 */
	@Override
	public void addProperties(List<Node> properties, boolean clone) {
		PropertyNode np;
		for (Node p : properties) {
			if (!(p instanceof PropertyNode))
				continue;
			np = (PropertyNode) p;
			if (clone)
				np = (PropertyNode) p.clone(null, null); // add to clone not parent
			if (isValidParentOf(np.getPropertyType()))
				addProperty(np);
		}
	}

	@Override
	public void addProperty(PropertyNode property) {
		// super.addProperty(property);
		add(property, -1);
	}

	/**
	 * Make a copy of all the properties of the source facet and add to this facet. If the property is of the wrong
	 * type, it is changed into an attribute.
	 * 
	 * @param sourceFacet
	 */
	public void copyFacet(PropertyOwnerNode sourceFacet) {
		PropertyNode newProperty = null;
		for (Node p : sourceFacet.getChildren()) {
			if (p instanceof PropertyNode) {
				newProperty = ((PropertyNode) p).clone(this, null);
				if (newProperty == null)
					continue; // ERROR
				if (!this.isValidParentOf(newProperty.getPropertyType()))
					newProperty = newProperty.changePropertyRole(PropertyNodeType.ATTRIBUTE);
				newProperty.addToTL(this);
			}
		}
		getChildrenHandler().clear(); // flush parent children cache
	}

	@Override
	public INode createProperty(final Node type) {
		PropertyNode pn = null;
		if (this instanceof AttributeFacetNode)
			pn = new AttributeNode(new TLAttribute(), this);
		else
			pn = new ElementNode(new TLProperty(), this);
		pn.setDescription(type.getDescription());
		if (type instanceof TypeProvider)
			pn.setAssignedType((TypeProvider) type);
		pn.setName(type.getName());
		return pn;
	}

	@Override
	public PropertyNode findChildByName(String name) {
		return (PropertyNode) super.findChildByName(name);
	}

	@Override
	public INode.CommandType getAddCommand() {
		return INode.CommandType.PROPERTY;
	}

	// /**
	// * Each access of children is sorting them based on order of MO's children.
	// */
	// @Override
	// public List<Node> getChildren() {
	// if (childrenHandler != null)
	// return childrenHandler.get();
	// return synchChildrenWithMO(super.getChildren());
	// }

	@Override
	public abstract String getComponentType();

	@Override
	public abstract TLFacetType getFacetType();

	@Override
	public Image getImage() {
		return Images.getImageRegistry().get(Images.Facet);
	}

	@Override
	public LibraryNode getLibrary() {
		// contextual facets are property owners but are also library members in version 1.6
		if (this instanceof LibraryMemberInterface)
			return getLibrary();

		if (getOwningComponent() == null || getOwningComponent() == this)
			return null;
		return getOwningComponent().getLibrary();
	}

	@Override
	public abstract String getName();

	@Override
	public LibraryMemberInterface getOwningComponent() {
		if (getParent() == null)
			return null;
		if (!(getParent() instanceof LibraryMemberInterface))
			return getParent().getOwningComponent();
		return (LibraryMemberInterface) getParent();
	}

	/**
	 * ******************************************* Abstract Methods
	 */
	@Override
	public abstract TLModelElement getTLModelObject();

	// @Override
	// public List<Node> getTreeChildren(boolean deep) {
	// if (childrenHandler != null)
	// return childrenHandler.getTreeChildren(deep);
	//
	// List<Node> navChildren = getNavChildren(deep);
	// navChildren.addAll(getInheritedChildren());
	// navChildren.add(getWhereUsedNode());
	// return navChildren;
	// }

	@Override
	public boolean hasChildren_TypeProviders() {
		return isXsdType() ? false : true;
	}

	@Override
	public boolean hasTreeChildren(boolean deep) {
		return getWhereUsedCount() > 0 ? true : false; // where used node
	}

	@Override
	public boolean isAssignable() {
		if (getParent() == null)
			return false;
		return (isComplexAssignable() || isSimpleAssignable()) ? true : false;
	}

	@Override
	public boolean isAssignableToElementRef() {
		return false;
	}

	@Override
	public boolean isAssignableToSimple() {
		if (!isSimpleListFacet())
			return false;
		if (getOwningComponent() instanceof CoreObjectNode)
			if (((CoreObjectNode) getOwningComponent()).getAssignedType() != ModelNode.getEmptyNode())
				return true;
		return false;
	}

	@Override
	public boolean isAssignableToVWA() {
		return isAssignableToSimple();
	}

	@Override
	public boolean isAssignedByReference() {
		if (getOwningComponent() == null) {
			// LOGGER.equals("No owning component for this facet: " + this);
			return false;
		}
		if (isSimpleListFacet())
			return false;
		return getOwningComponent() instanceof VWA_Node ? false : true;
	}

	public boolean isComplexAssignable() {
		return true;
	}

	@Override
	public boolean isDefaultFacet() {
		if (getOwningComponent() instanceof VWA_Node)
			return true;
		return false;
	}

	@Override
	public boolean isDeleteable() {
		return false;
	}

	public boolean isDetailFacet() {
		return false;
	}

	public boolean isIDFacet() {
		return false;
	}

	@Override
	public boolean isNamedEntity() {
		return getParent() != null ? true : false;
	}

	// @Override
	// public boolean isNamedType() {
	// return this instanceof ExtensionPointNode ? true : false;
	// }

	@Override
	public boolean isNavChild(boolean deep) {
		return true;
	}

	@Override
	public boolean isRenameableWhereUsed() {
		if (getOwningComponent() == null || getOwningComponent() == this)
			return false;
		return ((TypeProvider) getOwningComponent()).isRenameableWhereUsed();
	}

	/**
	 * Facets assigned to core object list types have no model objects but may be page1-assignable.
	 */
	@Override
	public boolean isSimpleAssignable() {
		return false;
	}

	public boolean isSimpleListFacet() {
		return false;
	}

	public boolean isSummaryFacet() {
		return false;
	}

	@Override
	@Deprecated
	public boolean isValidParentOf(PropertyNode pn) {
		if (pn == null)
			return false;
		return PropertyNodeType.getAllTypedPropertyTypes().contains(pn.getPropertyType());
	}

	@Override
	public boolean isValidParentOf(PropertyNodeType type) {
		return PropertyNodeType.getAllTypedPropertyTypes().contains(type);
	}

	/**
	 * Remove the properties in the list from this node and underlying tl model object. Use to move the property to a
	 * different facet.
	 * 
	 * @param property
	 */
	@Override
	public void removeProperty(final Node property) {
		((PropertyNode) property).removeProperty();
	}

	@Deprecated
	public void setContext(final String context) {
		if (!isEditable_newToChain())
			return;
		final Object ne = getTLModelObject();
		if (ne instanceof TLFacet) {
			((TLFacet) ne).setContext(context);
		}
	}

	@Override
	public void setName(String n) {
	}

	// @Override
	// public void sort() {
	// Collections.sort(getChildren(), new StringComparator<Node>() {
	//
	// @Override
	// protected String getString(Node object) {
	// return object.getName();
	// }
	// });
	// modelObject.sort();
	// }

	/**
	 * Return true if the node is delete-able using the version and managed state information used by Node. Used by
	 * sub-types that are deleteable.
	 */
	protected boolean isDeleteable(boolean deletable) {
		return deletable ? super.isDeleteable() : false;
	}

	// /**
	// * Synchronize order of children with ModelObject children order.
	// *
	// * @param children
	// * @return sorted list of children based on order of ModelObject.
	// */
	// @Deprecated
	// protected List<Node> synchChildrenWithMO(List<Node> children) {
	// if (getModelObject() == null)
	// return Collections.emptyList(); // happens during delete.
	// final List<?> tlChildrenOrder = getModelObject().getChildren();
	// Collections.sort(children, new Comparator<Node>() {
	//
	// @Override
	// public int compare(Node o1, Node o2) {
	// Integer idx1 = tlChildrenOrder.indexOf(o1.getTLModelObject());
	// Integer idx2 = tlChildrenOrder.indexOf(o2.getTLModelObject());
	// // Integer idx1 = tlChildrenOrder.indexOf(o1.getModelObject().getTLModelObj());
	// // Integer idx2 = tlChildrenOrder.indexOf(o2.getModelObject().getTLModelObj());
	// return idx1.compareTo(idx2);
	// }
	// });
	// return children;
	// }

}
