/**
 */
package edu.cmu.attackimpact.impl;

import edu.cmu.attackimpact.AttackImpactPackage;
import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.Propagation;
import edu.cmu.attackimpact.propagationType;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Propagation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link edu.cmu.attackimpact.impl.PropagationImpl#getDestinations <em>Destinations</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.PropagationImpl#getType <em>Type</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.PropagationImpl#getSeverity <em>Severity</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.impl.PropagationImpl#getTags <em>Tags</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropagationImpl extends MinimalEObjectImpl.Container implements Propagation {
	/**
	 * The cached value of the '{@link #getDestinations() <em>Destinations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinations()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> destinations;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final propagationType TYPE_EDEFAULT = propagationType.BUS;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected propagationType type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSeverity() <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeverity()
	 * @generated
	 * @ordered
	 */
	protected static final int SEVERITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSeverity() <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeverity()
	 * @generated
	 * @ordered
	 */
	protected int severity = SEVERITY_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropagationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttackImpactPackage.Literals.PROPAGATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getDestinations() {
		if (destinations == null) {
			destinations = new EObjectResolvingEList<Node>(Node.class, this, AttackImpactPackage.PROPAGATION__DESTINATIONS);
		}
		return destinations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public propagationType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(propagationType newType) {
		propagationType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttackImpactPackage.PROPAGATION__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSeverity(int newSeverity) {
		int oldSeverity = severity;
		severity = newSeverity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttackImpactPackage.PROPAGATION__SEVERITY, oldSeverity, severity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getTags() {
		if (tags == null) {
			tags = new EDataTypeUniqueEList<String>(String.class, this, AttackImpactPackage.PROPAGATION__TAGS);
		}
		return tags;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AttackImpactPackage.PROPAGATION__DESTINATIONS:
				return getDestinations();
			case AttackImpactPackage.PROPAGATION__TYPE:
				return getType();
			case AttackImpactPackage.PROPAGATION__SEVERITY:
				return getSeverity();
			case AttackImpactPackage.PROPAGATION__TAGS:
				return getTags();
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
			case AttackImpactPackage.PROPAGATION__DESTINATIONS:
				getDestinations().clear();
				getDestinations().addAll((Collection<? extends Node>)newValue);
				return;
			case AttackImpactPackage.PROPAGATION__TYPE:
				setType((propagationType)newValue);
				return;
			case AttackImpactPackage.PROPAGATION__SEVERITY:
				setSeverity((Integer)newValue);
				return;
			case AttackImpactPackage.PROPAGATION__TAGS:
				getTags().clear();
				getTags().addAll((Collection<? extends String>)newValue);
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
			case AttackImpactPackage.PROPAGATION__DESTINATIONS:
				getDestinations().clear();
				return;
			case AttackImpactPackage.PROPAGATION__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case AttackImpactPackage.PROPAGATION__SEVERITY:
				setSeverity(SEVERITY_EDEFAULT);
				return;
			case AttackImpactPackage.PROPAGATION__TAGS:
				getTags().clear();
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
			case AttackImpactPackage.PROPAGATION__DESTINATIONS:
				return destinations != null && !destinations.isEmpty();
			case AttackImpactPackage.PROPAGATION__TYPE:
				return type != TYPE_EDEFAULT;
			case AttackImpactPackage.PROPAGATION__SEVERITY:
				return severity != SEVERITY_EDEFAULT;
			case AttackImpactPackage.PROPAGATION__TAGS:
				return tags != null && !tags.isEmpty();
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
		result.append(" (type: ");
		result.append(type);
		result.append(", severity: ");
		result.append(severity);
		result.append(", tags: ");
		result.append(tags);
		result.append(')');
		return result.toString();
	}

} //PropagationImpl
