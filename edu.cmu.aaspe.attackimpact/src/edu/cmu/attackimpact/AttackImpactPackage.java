/**
 */
package edu.cmu.attackimpact;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see edu.cmu.attackimpact.AttackImpactFactory
 * @model kind="package"
 * @generated
 */
public interface AttackImpactPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "attackimpact";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/attackimpact";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "attackimpact";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AttackImpactPackage eINSTANCE = edu.cmu.attackimpact.impl.AttackImpactPackageImpl.init();

	/**
	 * The meta object id for the '{@link edu.cmu.attackimpact.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.cmu.attackimpact.impl.NodeImpl
	 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Vulnerabilities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__VULNERABILITIES = 2;

	/**
	 * The feature id for the '<em><b>Propagations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__PROPAGATIONS = 3;

	/**
	 * The feature id for the '<em><b>Related Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__RELATED_OBJECT = 4;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__TAGS = 5;

	/**
	 * The feature id for the '<em><b>Domains</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__DOMAINS = 6;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link edu.cmu.attackimpact.impl.VulnerabilityImpl <em>Vulnerability</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.cmu.attackimpact.impl.VulnerabilityImpl
	 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getVulnerability()
	 * @generated
	 */
	int VULNERABILITY = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY__TYPE = 2;

	/**
	 * The feature id for the '<em><b>Propagations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY__PROPAGATIONS = 3;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY__TAGS = 4;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY__SEVERITY = 5;

	/**
	 * The number of structural features of the '<em>Vulnerability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Vulnerability</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VULNERABILITY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link edu.cmu.attackimpact.impl.PropagationImpl <em>Propagation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.cmu.attackimpact.impl.PropagationImpl
	 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getPropagation()
	 * @generated
	 */
	int PROPAGATION = 2;

	/**
	 * The feature id for the '<em><b>Destinations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPAGATION__DESTINATIONS = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPAGATION__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPAGATION__SEVERITY = 2;

	/**
	 * The feature id for the '<em><b>Tags</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPAGATION__TAGS = 3;

	/**
	 * The number of structural features of the '<em>Propagation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPAGATION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Propagation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPAGATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link edu.cmu.attackimpact.impl.ModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.cmu.attackimpact.impl.ModelImpl
	 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getModel()
	 * @generated
	 */
	int MODEL = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL__NODES = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link edu.cmu.attackimpact.vulnerabilityType <em>vulnerability Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.cmu.attackimpact.vulnerabilityType
	 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getvulnerabilityType()
	 * @generated
	 */
	int VULNERABILITY_TYPE = 4;

	/**
	 * The meta object id for the '{@link edu.cmu.attackimpact.propagationType <em>propagation Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see edu.cmu.attackimpact.propagationType
	 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getpropagationType()
	 * @generated
	 */
	int PROPAGATION_TYPE = 5;


	/**
	 * Returns the meta object for class '{@link edu.cmu.attackimpact.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see edu.cmu.attackimpact.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Node#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see edu.cmu.attackimpact.Node#getName()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Name();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Node#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see edu.cmu.attackimpact.Node#getDescription()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link edu.cmu.attackimpact.Node#getVulnerabilities <em>Vulnerabilities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vulnerabilities</em>'.
	 * @see edu.cmu.attackimpact.Node#getVulnerabilities()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Vulnerabilities();

	/**
	 * Returns the meta object for the containment reference list '{@link edu.cmu.attackimpact.Node#getPropagations <em>Propagations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Propagations</em>'.
	 * @see edu.cmu.attackimpact.Node#getPropagations()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Propagations();

	/**
	 * Returns the meta object for the reference '{@link edu.cmu.attackimpact.Node#getRelatedObject <em>Related Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Related Object</em>'.
	 * @see edu.cmu.attackimpact.Node#getRelatedObject()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_RelatedObject();

	/**
	 * Returns the meta object for the attribute list '{@link edu.cmu.attackimpact.Node#getTags <em>Tags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Tags</em>'.
	 * @see edu.cmu.attackimpact.Node#getTags()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Tags();

	/**
	 * Returns the meta object for the attribute list '{@link edu.cmu.attackimpact.Node#getDomains <em>Domains</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Domains</em>'.
	 * @see edu.cmu.attackimpact.Node#getDomains()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Domains();

	/**
	 * Returns the meta object for class '{@link edu.cmu.attackimpact.Vulnerability <em>Vulnerability</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vulnerability</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability
	 * @generated
	 */
	EClass getVulnerability();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Vulnerability#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability#getName()
	 * @see #getVulnerability()
	 * @generated
	 */
	EAttribute getVulnerability_Name();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Vulnerability#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability#getDescription()
	 * @see #getVulnerability()
	 * @generated
	 */
	EAttribute getVulnerability_Description();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Vulnerability#getSeverity <em>Severity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Severity</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability#getSeverity()
	 * @see #getVulnerability()
	 * @generated
	 */
	EAttribute getVulnerability_Severity();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Vulnerability#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability#getType()
	 * @see #getVulnerability()
	 * @generated
	 */
	EAttribute getVulnerability_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link edu.cmu.attackimpact.Vulnerability#getPropagations <em>Propagations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Propagations</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability#getPropagations()
	 * @see #getVulnerability()
	 * @generated
	 */
	EReference getVulnerability_Propagations();

	/**
	 * Returns the meta object for the attribute list '{@link edu.cmu.attackimpact.Vulnerability#getTags <em>Tags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Tags</em>'.
	 * @see edu.cmu.attackimpact.Vulnerability#getTags()
	 * @see #getVulnerability()
	 * @generated
	 */
	EAttribute getVulnerability_Tags();

	/**
	 * Returns the meta object for class '{@link edu.cmu.attackimpact.Propagation <em>Propagation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Propagation</em>'.
	 * @see edu.cmu.attackimpact.Propagation
	 * @generated
	 */
	EClass getPropagation();

	/**
	 * Returns the meta object for the reference list '{@link edu.cmu.attackimpact.Propagation#getDestinations <em>Destinations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Destinations</em>'.
	 * @see edu.cmu.attackimpact.Propagation#getDestinations()
	 * @see #getPropagation()
	 * @generated
	 */
	EReference getPropagation_Destinations();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Propagation#getSeverity <em>Severity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Severity</em>'.
	 * @see edu.cmu.attackimpact.Propagation#getSeverity()
	 * @see #getPropagation()
	 * @generated
	 */
	EAttribute getPropagation_Severity();

	/**
	 * Returns the meta object for the attribute list '{@link edu.cmu.attackimpact.Propagation#getTags <em>Tags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Tags</em>'.
	 * @see edu.cmu.attackimpact.Propagation#getTags()
	 * @see #getPropagation()
	 * @generated
	 */
	EAttribute getPropagation_Tags();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Propagation#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see edu.cmu.attackimpact.Propagation#getType()
	 * @see #getPropagation()
	 * @generated
	 */
	EAttribute getPropagation_Type();

	/**
	 * Returns the meta object for class '{@link edu.cmu.attackimpact.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see edu.cmu.attackimpact.Model
	 * @generated
	 */
	EClass getModel();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Model#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see edu.cmu.attackimpact.Model#getName()
	 * @see #getModel()
	 * @generated
	 */
	EAttribute getModel_Name();

	/**
	 * Returns the meta object for the attribute '{@link edu.cmu.attackimpact.Model#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see edu.cmu.attackimpact.Model#getDescription()
	 * @see #getModel()
	 * @generated
	 */
	EAttribute getModel_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link edu.cmu.attackimpact.Model#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see edu.cmu.attackimpact.Model#getNodes()
	 * @see #getModel()
	 * @generated
	 */
	EReference getModel_Nodes();

	/**
	 * Returns the meta object for enum '{@link edu.cmu.attackimpact.vulnerabilityType <em>vulnerability Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>vulnerability Type</em>'.
	 * @see edu.cmu.attackimpact.vulnerabilityType
	 * @generated
	 */
	EEnum getvulnerabilityType();

	/**
	 * Returns the meta object for enum '{@link edu.cmu.attackimpact.propagationType <em>propagation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>propagation Type</em>'.
	 * @see edu.cmu.attackimpact.propagationType
	 * @generated
	 */
	EEnum getpropagationType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AttackImpactFactory getAttackImpactFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link edu.cmu.attackimpact.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.cmu.attackimpact.impl.NodeImpl
		 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__NAME = eINSTANCE.getNode_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__DESCRIPTION = eINSTANCE.getNode_Description();

		/**
		 * The meta object literal for the '<em><b>Vulnerabilities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__VULNERABILITIES = eINSTANCE.getNode_Vulnerabilities();

		/**
		 * The meta object literal for the '<em><b>Propagations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__PROPAGATIONS = eINSTANCE.getNode_Propagations();

		/**
		 * The meta object literal for the '<em><b>Related Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__RELATED_OBJECT = eINSTANCE.getNode_RelatedObject();

		/**
		 * The meta object literal for the '<em><b>Tags</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__TAGS = eINSTANCE.getNode_Tags();

		/**
		 * The meta object literal for the '<em><b>Domains</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__DOMAINS = eINSTANCE.getNode_Domains();

		/**
		 * The meta object literal for the '{@link edu.cmu.attackimpact.impl.VulnerabilityImpl <em>Vulnerability</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.cmu.attackimpact.impl.VulnerabilityImpl
		 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getVulnerability()
		 * @generated
		 */
		EClass VULNERABILITY = eINSTANCE.getVulnerability();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VULNERABILITY__NAME = eINSTANCE.getVulnerability_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VULNERABILITY__DESCRIPTION = eINSTANCE.getVulnerability_Description();

		/**
		 * The meta object literal for the '<em><b>Severity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VULNERABILITY__SEVERITY = eINSTANCE.getVulnerability_Severity();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VULNERABILITY__TYPE = eINSTANCE.getVulnerability_Type();

		/**
		 * The meta object literal for the '<em><b>Propagations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VULNERABILITY__PROPAGATIONS = eINSTANCE.getVulnerability_Propagations();

		/**
		 * The meta object literal for the '<em><b>Tags</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VULNERABILITY__TAGS = eINSTANCE.getVulnerability_Tags();

		/**
		 * The meta object literal for the '{@link edu.cmu.attackimpact.impl.PropagationImpl <em>Propagation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.cmu.attackimpact.impl.PropagationImpl
		 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getPropagation()
		 * @generated
		 */
		EClass PROPAGATION = eINSTANCE.getPropagation();

		/**
		 * The meta object literal for the '<em><b>Destinations</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPAGATION__DESTINATIONS = eINSTANCE.getPropagation_Destinations();

		/**
		 * The meta object literal for the '<em><b>Severity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPAGATION__SEVERITY = eINSTANCE.getPropagation_Severity();

		/**
		 * The meta object literal for the '<em><b>Tags</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPAGATION__TAGS = eINSTANCE.getPropagation_Tags();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPAGATION__TYPE = eINSTANCE.getPropagation_Type();

		/**
		 * The meta object literal for the '{@link edu.cmu.attackimpact.impl.ModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.cmu.attackimpact.impl.ModelImpl
		 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getModel()
		 * @generated
		 */
		EClass MODEL = eINSTANCE.getModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL__NAME = eINSTANCE.getModel_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODEL__DESCRIPTION = eINSTANCE.getModel_Description();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODEL__NODES = eINSTANCE.getModel_Nodes();

		/**
		 * The meta object literal for the '{@link edu.cmu.attackimpact.vulnerabilityType <em>vulnerability Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.cmu.attackimpact.vulnerabilityType
		 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getvulnerabilityType()
		 * @generated
		 */
		EEnum VULNERABILITY_TYPE = eINSTANCE.getvulnerabilityType();

		/**
		 * The meta object literal for the '{@link edu.cmu.attackimpact.propagationType <em>propagation Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see edu.cmu.attackimpact.propagationType
		 * @see edu.cmu.attackimpact.impl.AttackImpactPackageImpl#getpropagationType()
		 * @generated
		 */
		EEnum PROPAGATION_TYPE = eINSTANCE.getpropagationType();

	}

} //AttackImpactPackage
