/**
 */
package edu.cmu.attackimpact;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link edu.cmu.attackimpact.Node#getName <em>Name</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Node#getDescription <em>Description</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Node#getVulnerabilities <em>Vulnerabilities</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Node#getPropagations <em>Propagations</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Node#getRelatedObject <em>Related Object</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Node#getTags <em>Tags</em>}</li>
 *   <li>{@link edu.cmu.attackimpact.Node#getDomains <em>Domains</em>}</li>
 * </ul>
 *
 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode()
 * @model
 * @generated
 */
public interface Node extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_Name()
	 * @model default=""
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link edu.cmu.attackimpact.Node#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_Description()
	 * @model default=""
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link edu.cmu.attackimpact.Node#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Vulnerabilities</b></em>' containment reference list.
	 * The list contents are of type {@link edu.cmu.attackimpact.Vulnerability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vulnerabilities</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vulnerabilities</em>' containment reference list.
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_Vulnerabilities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Vulnerability> getVulnerabilities();

	/**
	 * Returns the value of the '<em><b>Propagations</b></em>' containment reference list.
	 * The list contents are of type {@link edu.cmu.attackimpact.Propagation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Propagations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Propagations</em>' containment reference list.
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_Propagations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Propagation> getPropagations();

	/**
	 * Returns the value of the '<em><b>Related Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Related Object</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Related Object</em>' reference.
	 * @see #setRelatedObject(EObject)
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_RelatedObject()
	 * @model
	 * @generated
	 */
	EObject getRelatedObject();

	/**
	 * Sets the value of the '{@link edu.cmu.attackimpact.Node#getRelatedObject <em>Related Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Related Object</em>' reference.
	 * @see #getRelatedObject()
	 * @generated
	 */
	void setRelatedObject(EObject value);

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
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_Tags()
	 * @model
	 * @generated
	 */
	EList<String> getTags();

	/**
	 * Returns the value of the '<em><b>Domains</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domains</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Domains</em>' attribute list.
	 * @see edu.cmu.attackimpact.AttackImpactPackage#getNode_Domains()
	 * @model
	 * @generated
	 */
	EList<String> getDomains();

} // Node
