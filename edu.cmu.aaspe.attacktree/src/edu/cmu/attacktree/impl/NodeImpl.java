/**
 */
package edu.cmu.attacktree.impl;

import edu.cmu.attacktree.AttackTreePackage;
import edu.cmu.attacktree.Node;
import edu.cmu.attacktree.Vulnerability;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getName <em>Name</em>}</li>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getVulnerabilities <em>Vulnerabilities</em>}</li>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getSubNodes <em>Sub Nodes</em>}</li>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getRelatedObject <em>Related Object</em>}</li>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getTags <em>Tags</em>}</li>
 *   <li>{@link edu.cmu.attacktree.impl.NodeImpl#getDomains <em>Domains</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NodeImpl extends MinimalEObjectImpl.Container implements Node {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVulnerabilities() <em>Vulnerabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVulnerabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<Vulnerability> vulnerabilities;

	/**
	 * The cached value of the '{@link #getSubNodes() <em>Sub Nodes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubNodes()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> subNodes;

	/**
	 * The cached value of the '{@link #getRelatedObject() <em>Related Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelatedObject()
	 * @generated
	 * @ordered
	 */
	protected EObject relatedObject;

	/**
	 * The cached value of the '{@link #getTags() <em>Tags</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTags()
	 * @generated
	 * @ordered
	 */
	protected EList<String> tags;

	/**
	 * The cached value of the '{@link #getDomains() <em>Domains</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDomains()
	 * @generated
	 * @ordered
	 */
	protected EList<String> domains;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttackTreePackage.Literals.NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttackTreePackage.NODE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttackTreePackage.NODE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Vulnerability> getVulnerabilities() {
		if (vulnerabilities == null) {
			vulnerabilities = new EObjectContainmentEList<Vulnerability>(Vulnerability.class, this, AttackTreePackage.NODE__VULNERABILITIES);
		}
		return vulnerabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getSubNodes() {
		if (subNodes == null) {
			subNodes = new EObjectContainmentEList<Node>(Node.class, this, AttackTreePackage.NODE__SUB_NODES);
		}
		return subNodes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getRelatedObject() {
		if (relatedObject != null && relatedObject.eIsProxy()) {
			InternalEObject oldRelatedObject = (InternalEObject)relatedObject;
			relatedObject = eResolveProxy(oldRelatedObject);
			if (relatedObject != oldRelatedObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AttackTreePackage.NODE__RELATED_OBJECT, oldRelatedObject, relatedObject));
			}
		}
		return relatedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetRelatedObject() {
		return relatedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRelatedObject(EObject newRelatedObject) {
		EObject oldRelatedObject = relatedObject;
		relatedObject = newRelatedObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttackTreePackage.NODE__RELATED_OBJECT, oldRelatedObject, relatedObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getTags() {
		if (tags == null) {
			tags = new EDataTypeUniqueEList<String>(String.class, this, AttackTreePackage.NODE__TAGS);
		}
		return tags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getDomains() {
		if (domains == null) {
			domains = new EDataTypeUniqueEList<String>(String.class, this, AttackTreePackage.NODE__DOMAINS);
		}
		return domains;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AttackTreePackage.NODE__VULNERABILITIES:
				return ((InternalEList<?>)getVulnerabilities()).basicRemove(otherEnd, msgs);
			case AttackTreePackage.NODE__SUB_NODES:
				return ((InternalEList<?>)getSubNodes()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AttackTreePackage.NODE__NAME:
				return getName();
			case AttackTreePackage.NODE__DESCRIPTION:
				return getDescription();
			case AttackTreePackage.NODE__VULNERABILITIES:
				return getVulnerabilities();
			case AttackTreePackage.NODE__SUB_NODES:
				return getSubNodes();
			case AttackTreePackage.NODE__RELATED_OBJECT:
				if (resolve) return getRelatedObject();
				return basicGetRelatedObject();
			case AttackTreePackage.NODE__TAGS:
				return getTags();
			case AttackTreePackage.NODE__DOMAINS:
				return getDomains();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AttackTreePackage.NODE__NAME:
				setName((String)newValue);
				return;
			case AttackTreePackage.NODE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case AttackTreePackage.NODE__VULNERABILITIES:
				getVulnerabilities().clear();
				getVulnerabilities().addAll((Collection<? extends Vulnerability>)newValue);
				return;
			case AttackTreePackage.NODE__SUB_NODES:
				getSubNodes().clear();
				getSubNodes().addAll((Collection<? extends Node>)newValue);
				return;
			case AttackTreePackage.NODE__RELATED_OBJECT:
				setRelatedObject((EObject)newValue);
				return;
			case AttackTreePackage.NODE__TAGS:
				getTags().clear();
				getTags().addAll((Collection<? extends String>)newValue);
				return;
			case AttackTreePackage.NODE__DOMAINS:
				getDomains().clear();
				getDomains().addAll((Collection<? extends String>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AttackTreePackage.NODE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AttackTreePackage.NODE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case AttackTreePackage.NODE__VULNERABILITIES:
				getVulnerabilities().clear();
				return;
			case AttackTreePackage.NODE__SUB_NODES:
				getSubNodes().clear();
				return;
			case AttackTreePackage.NODE__RELATED_OBJECT:
				setRelatedObject((EObject)null);
				return;
			case AttackTreePackage.NODE__TAGS:
				getTags().clear();
				return;
			case AttackTreePackage.NODE__DOMAINS:
				getDomains().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AttackTreePackage.NODE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AttackTreePackage.NODE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case AttackTreePackage.NODE__VULNERABILITIES:
				return vulnerabilities != null && !vulnerabilities.isEmpty();
			case AttackTreePackage.NODE__SUB_NODES:
				return subNodes != null && !subNodes.isEmpty();
			case AttackTreePackage.NODE__RELATED_OBJECT:
				return relatedObject != null;
			case AttackTreePackage.NODE__TAGS:
				return tags != null && !tags.isEmpty();
			case AttackTreePackage.NODE__DOMAINS:
				return domains != null && !domains.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(", tags: ");
		result.append(tags);
		result.append(", domains: ");
		result.append(domains);
		result.append(')');
		return result.toString();
	}

} //NodeImpl
