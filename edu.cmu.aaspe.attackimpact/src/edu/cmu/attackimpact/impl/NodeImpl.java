/**
 */
package edu.cmu.attackimpact.impl;

import edu.cmu.attackimpact.AttackImpactPackage;
import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.Propagation;
import edu.cmu.attackimpact.Vulnerability;

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
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getName <em>Name</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getVulnerabilities <em>Vulnerabilities</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getPropagations <em>Propagations</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getRelatedObject <em>Related Object</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getTags <em>Tags</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.NodeImpl#getDomains <em>Domains</em>}</li>
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
	 * The cached value of the '{@link #getPropagations() <em>Propagations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropagations()
	 * @generated
	 * @ordered
	 */
	protected EList<Propagation> propagations;

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
		return AttackImpactPackage.Literals.NODE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AttackImpactPackage.NODE__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AttackImpactPackage.NODE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Vulnerability> getVulnerabilities() {
		if (vulnerabilities == null) {
			vulnerabilities = new EObjectContainmentEList<Vulnerability>(Vulnerability.class, this, AttackImpactPackage.NODE__VULNERABILITIES);
		}
		return vulnerabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Propagation> getPropagations() {
		if (propagations == null) {
			propagations = new EObjectContainmentEList<Propagation>(Propagation.class, this, AttackImpactPackage.NODE__PROPAGATIONS);
		}
		return propagations;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AttackImpactPackage.NODE__RELATED_OBJECT, oldRelatedObject, relatedObject));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AttackImpactPackage.NODE__RELATED_OBJECT, oldRelatedObject, relatedObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getTags() {
		if (tags == null) {
			tags = new EDataTypeUniqueEList<String>(String.class, this, AttackImpactPackage.NODE__TAGS);
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
			domains = new EDataTypeUniqueEList<String>(String.class, this, AttackImpactPackage.NODE__DOMAINS);
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
			case AttackImpactPackage.NODE__VULNERABILITIES:
				return ((InternalEList<?>)getVulnerabilities()).basicRemove(otherEnd, msgs);
			case AttackImpactPackage.NODE__PROPAGATIONS:
				return ((InternalEList<?>)getPropagations()).basicRemove(otherEnd, msgs);
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
			case AttackImpactPackage.NODE__NAME:
				return getName();
			case AttackImpactPackage.NODE__DESCRIPTION:
				return getDescription();
			case AttackImpactPackage.NODE__VULNERABILITIES:
				return getVulnerabilities();
			case AttackImpactPackage.NODE__PROPAGATIONS:
				return getPropagations();
			case AttackImpactPackage.NODE__RELATED_OBJECT:
				if (resolve) return getRelatedObject();
				return basicGetRelatedObject();
			case AttackImpactPackage.NODE__TAGS:
				return getTags();
			case AttackImpactPackage.NODE__DOMAINS:
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
			case AttackImpactPackage.NODE__NAME:
				setName((String)newValue);
				return;
			case AttackImpactPackage.NODE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case AttackImpactPackage.NODE__VULNERABILITIES:
				getVulnerabilities().clear();
				getVulnerabilities().addAll((Collection<? extends Vulnerability>)newValue);
				return;
			case AttackImpactPackage.NODE__PROPAGATIONS:
				getPropagations().clear();
				getPropagations().addAll((Collection<? extends Propagation>)newValue);
				return;
			case AttackImpactPackage.NODE__RELATED_OBJECT:
				setRelatedObject((EObject)newValue);
				return;
			case AttackImpactPackage.NODE__TAGS:
				getTags().clear();
				getTags().addAll((Collection<? extends String>)newValue);
				return;
			case AttackImpactPackage.NODE__DOMAINS:
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
			case AttackImpactPackage.NODE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AttackImpactPackage.NODE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case AttackImpactPackage.NODE__VULNERABILITIES:
				getVulnerabilities().clear();
				return;
			case AttackImpactPackage.NODE__PROPAGATIONS:
				getPropagations().clear();
				return;
			case AttackImpactPackage.NODE__RELATED_OBJECT:
				setRelatedObject((EObject)null);
				return;
			case AttackImpactPackage.NODE__TAGS:
				getTags().clear();
				return;
			case AttackImpactPackage.NODE__DOMAINS:
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
			case AttackImpactPackage.NODE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AttackImpactPackage.NODE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case AttackImpactPackage.NODE__VULNERABILITIES:
				return vulnerabilities != null && !vulnerabilities.isEmpty();
			case AttackImpactPackage.NODE__PROPAGATIONS:
				return propagations != null && !propagations.isEmpty();
			case AttackImpactPackage.NODE__RELATED_OBJECT:
				return relatedObject != null;
			case AttackImpactPackage.NODE__TAGS:
				return tags != null && !tags.isEmpty();
			case AttackImpactPackage.NODE__DOMAINS:
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
