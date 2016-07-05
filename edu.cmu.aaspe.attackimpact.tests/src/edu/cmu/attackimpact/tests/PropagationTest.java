/**
 */
package edu.cmu.attackimpact.tests;

import edu.cmu.attackimpact.AttackImpactFactory;
import edu.cmu.attackimpact.Propagation;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Propagation</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class PropagationTest extends TestCase {

	/**
	 * The fixture for this Propagation test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Propagation fixture = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(PropagationTest.class);
	}

	/**
	 * Constructs a new Propagation test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropagationTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Propagation test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void setFixture(Propagation fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Propagation test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Propagation getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		setFixture(AttackImpactFactory.eINSTANCE.createPropagation());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

} //PropagationTest
