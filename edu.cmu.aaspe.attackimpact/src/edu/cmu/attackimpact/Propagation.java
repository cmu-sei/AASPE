/**
 */
package edu.cmu.attackimpact;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Propagation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link edu.cmu.attackimpact.Propagation#getDestinations <em>Destinations</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Propagation#getType <em>Type</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Propagation#getSeverity <em>Severity</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Propagation#getTags <em>Tags</em>}</li>
 * </ul>
 *
 * @see edu.cmu.attackimpact.AttackImpactPackage#getPropagation()
 * @model
 * @generated
 */
public interface Propagation extends EObject {
	/**
	 * Returns the value of the '<em><b>Destinations</b></em>' reference list.
	 * The list contents are of type {@link edu.cmu.attackimpact.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destinations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destinations</em>' reference list.
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getPropagation_Destinations()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	EList<Node> getDestinations();

	/**
	 * Returns the value of the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Severity</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Severity</em>' attribute.
	 * @see #setSeverity(int)
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getPropagation_Severity()
	 * @model required="true"
	 * @generated
	 */
	int getSeverity();

	/**
	 * Sets the value of the '{@link edu.cmu.attackimpact.Propagation#getSeverity <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Severity</em>' attribute.
	 * @see #getSeverity()
	 * @generated
	 */
	void setSeverity(int value);

	/**
	 * Returns the value of the '<em><b>Tags</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tags</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tags</em>' attribute list.
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getPropagation_Tags()
	 * @model
	 * @generated
	 */
	EList<String> getTags();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link edu.cmu.attackimpact.propagationType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see edu.cmu.attackimpact.propagationType
	 * @see #setType(propagationType)
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getPropagation_Type()
	 * @model
	 * @generated
	 */
	propagationType getType();

	/**
	 * Sets the value of the '{@link edu.cmu.attackimpact.Propagation#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see edu.cmu.attackimpact.propagationType
	 * @see #getType()
	 * @generated
	 */
	void setType(propagationType value);

} // Propagation
