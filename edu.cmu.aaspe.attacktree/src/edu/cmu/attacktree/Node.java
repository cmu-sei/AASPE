/**
 */
package edu.cmu.attacktree;

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
 *   <li>{@link edu.cmu.attacktree.Node#getName <em>Name</em>}</li>
 *   <li>{@link edu.cmu.attacktree.Node#getDescription <em>Description</em>}</li>
 *   <li>{@link edu.cmu.attacktree.Node#getVulnerabilities <em>Vulnerabilities</em>}</li>
 *   <li>{@link edu.cmu.attacktree.Node#getSubNodes <em>Sub Nodes</em>}</li>
 *   <li>{@link edu.cmu.attacktree.Node#getRelatedObject <em>Related Object</em>}</li>
 *   <li>{@link edu.cmu.attacktree.Node#getTags <em>Tags</em>}</li>
 *   <li>{@link edu.cmu.attacktree.Node#getDomains <em>Domains</em>}</li>
 * </ul>
 *
 * @see edu.cmu.attacktree.AttackTreePackage#getNode()
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
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_Name()
	 * @model default=""
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link edu.cmu.attacktree.Node#getName <em>Name</em>}' attribute.
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
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_Description()
	 * @model default=""
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link edu.cmu.attacktree.Node#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Vulnerabilities</b></em>' containment reference list.
	 * The list contents are of type {@link edu.cmu.attacktree.Vulnerability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vulnerabilities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vulnerabilities</em>' containment reference list.
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_Vulnerabilities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Vulnerability> getVulnerabilities();

	/**
	 * Returns the value of the '<em><b>Sub Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link edu.cmu.attacktree.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Nodes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Nodes</em>' containment reference list.
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_SubNodes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getSubNodes();

	/**
	 * Returns the value of the '<em><b>Related Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Related Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Related Object</em>' reference.
	 * @see #setRelatedObject(EObject)
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_RelatedObject()
	 * @model
	 * @generated
	 */
	EObject getRelatedObject();

	/**
	 * Sets the value of the '{@link edu.cmu.attacktree.Node#getRelatedObject <em>Related Object</em>}' reference.
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
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_Tags()
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
	 * @see edu.cmu.attacktree.AttackTreePackage#getNode_Domains()
	 * @model
	 * @generated
	 */
	EList<String> getDomains();

} // Node
