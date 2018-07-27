package edu.cmu.sei.aaspe.tests

import com.google.common.io.CharStreams
import com.rockwellcollins.atc.resolute.analysis.execution.EvaluationContext
import com.rockwellcollins.atc.resolute.analysis.execution.FeatureToConnectionsMap
import com.rockwellcollins.atc.resolute.analysis.execution.NamedElementComparator
import com.rockwellcollins.atc.resolute.analysis.execution.ResoluteInterpreter
import com.rockwellcollins.atc.resolute.resolute.ResolutePackage
import com.rockwellcollins.atc.resolute.resolute.ResoluteSubclause
import com.rockwellcollins.atc.resolute.validation.BaseType
import java.io.InputStreamReader
import java.util.SortedSet
import org.eclipse.core.resources.IContainer
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.ui.actions.WorkspaceModifyOperation
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.osate.aadl2.AadlPackage
import org.osate.aadl2.ComponentImplementation
import org.osate.aadl2.NamedElement
import org.osate.aadl2.instance.ComponentInstance
import org.osate.aadl2.instance.ConnectionInstance
import org.osate.aadl2.instantiation.InstantiateModel
import org.osate.aadl2.modelsupport.resources.OsateResourceUtil
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.XtextRunner;

import static extension org.junit.Assert.assertEquals
import static extension org.osate.annexsupport.AnnexUtil.getAllAnnexSubclauses
import org.osate.testsupport.Aadl2InjectorProvider
import com.itemis.xtext.testing.XtextTest
import com.google.inject.Inject
import org.osate.testsupport.TestHelper

@RunWith(XtextRunner)
@InjectWith(Aadl2InjectorProvider)
class ResoluteTest extends XtextTest {
	val static NE_COMPARATOR = new NamedElementComparator
	
	//Test File: resolute/security_cwe131_queues.aadl
	
	@Test
	def void testSecurityCWE131Queues_MainIncorrect() {
		testResolute("resolute/security_cwe131_queues.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityCWE131Queues_MainCorrect() {
		testResolute("resolute/security_cwe131_queues.aadl", "main.correct", true)
	}
	
	
	
	//Test File: resolute/security_cwe131_timing.aadl
	
	@Test
	def void testSecurityCWE131Timing_MainIncorrect() {
		testResolute("resolute/security_cwe131_timing.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityCWE131Timing_MainCorrect() {
		testResolute("resolute/security_cwe131_timing.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_cwe311.aadl
	
	@Test
	def void testSecurityCWE311_MainSuccess1() {
		testResolute("resolute/security_cwe311.aadl", "main.success1", true)
	}
	
	@Test
	def void testSecurityCWE311_MainSuccess2() {
		testResolute("resolute/security_cwe311.aadl", "main.success2", true)
	}
	
	@Test
	def void testSecurityCWE311_MainFailure1() {
		testResolute("resolute/security_cwe311.aadl", "main.failure1", false)
	}
	
	
	
	//Test file: resolute/security_cwe362.aadl
	
	@Test
	def void testSecurityCWE362_MainIncorrect() {
		testResolute("resolute/security_cwe362.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityCWE362_MainCorrect() {
		testResolute("resolute/security_cwe362.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_cwe805.aadl
	
	@Test
	def void testSecurityCWE805_MainSuccessInt() {
		testResolute("resolute/security_cwe805.aadl", "main.success_int", true)
	}
	
	@Test
	def void testSecurityCWE805_MainFailureInt() {
		testResolute("resolute/security_cwe805.aadl", "main.failure_int", false)
	}
	
	@Test
	def void testSecurityCWE805_MainSuccessFloat() {
		testResolute("resolute/security_cwe805.aadl", "main.success_float", true)
	}
	
	@Test
	def void testSecurityCWE805_MainFailureFloat() {
		testResolute("resolute/security_cwe805.aadl", "main.failure_float", false)
	}
	
	@Test
	def void testSecurityCWE805_MainSuccessArray() {
		testResolute("resolute/security_cwe805.aadl", "main.success_array", true)
	}
	
	@Test
	def void testSecurityCWE805_MainFailureArray() {
		testResolute("resolute/security_cwe805.aadl", "main.failure_array", false)
	}
	
	
	
	//Test file: resolute/security_r0.aadl
	
	@Test
	def void testSecurityR0_MainIncorrect() {
		testResolute("resolute/security_r0.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityR0_MainCorrect() {
		testResolute("resolute/security_r0.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_r1.aadl
	
	@Test
	def void testSecurityR1_MainIncorrect() {
		testResolute("resolute/security_r1.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityR1_MainCorrect() {
		testResolute("resolute/security_r1.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_r2.aadl
	
	@Test
	def void testSecurityR2_MainIncorrect() {
		testResolute("resolute/security_r2.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityR2_MainCorrect() {
		testResolute("resolute/security_r2.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_r3.aadl
	
	@Test
	def void testSecurityR3_MainIncorrect1() {
		testResolute("resolute/security_r3.aadl", "main.incorrect1", false)
	}
	
	@Test
	def void testSecurityR3_MainIncorrect2() {
		testResolute("resolute/security_r3.aadl", "main.incorrect2", false)
	}
	
	@Test
	def void testSecurityR3_MainCorrect1() {
		testResolute("resolute/security_r3.aadl", "main.correct1", true)
	}
	
	@Test
	def void testSecurityR3_MainCorrect2() {
		testResolute("resolute/security_r3.aadl", "main.correct2", true)
	}
	
	
	
	//Test file: resolute/security_r4.aadl
	
	@Test
	def void testSecurityR4_MainIncorrect() {
		testResolute("resolute/security_r4.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityR4_MainCorrect1() {
		testResolute("resolute/security_r4.aadl", "main.correct1", true)
	}
	
	@Test
	def void testSecurityR4_MainCorrect2() {
		testResolute("resolute/security_r4.aadl", "main.correct2", true)
	}
	
	
	
	//Test file: resolute/security_r5.aadl
	
	@Test
	def void testSecurityR5_MainIncorrect1() {
		testResolute("resolute/security_r5.aadl", "main.incorrect1", false)
	}
	
	@Test
	def void testSecurityR5_MainIncorrect2() {
		testResolute("resolute/security_r5.aadl", "main.incorrect2", false)
	}
	
	@Test
	def void testSecurityR5_MainIncorrect3() {
		testResolute("resolute/security_r5.aadl", "main.incorrect3", false)
	}
	
	@Test
	def void testSecurityR5_MainIncorrect4() {
		testResolute("resolute/security_r5.aadl", "main.incorrect4", false)
	}
	
	@Test
	def void testSecurityR5_MainCorrect() {
		testResolute("resolute/security_r5.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_r6.aadl
	
	@Test
	def void testSecurityR6_MainIncorrect() {
		testResolute("resolute/security_r6.aadl", "main.incorrect", false)
	}
	
	@Test
	def void testSecurityR6_MainCorrect() {
		testResolute("resolute/security_r6.aadl", "main.correct", true)
	}
	
	
	
	//Test file: resolute/security_r7.aadl
	
	@Test
	def void testSecurityR7_IntegrationSuccess1() {
		testResolute("resolute/security_r7.aadl", "integration.success1", true)
	}
	
	@Test
	def void testSecurityR7_IntegrationSuccess2() {
		testResolute("resolute/security_r7.aadl", "integration.success2", true)
	}
	
	@Test
	def void testSecurityR7_IntegrationFail1() {
		testResolute("resolute/security_r7.aadl", "integration.fail1", false)
	}
	
	@Test
	def void testSecurityR7_IntegrationFail2() {
		testResolute("resolute/security_r7.aadl", "integration.fail2", false)
	}
	
	@Inject
	TestHelper<AadlPackage> testHelper
	
	def private testResolute(String fileName, String implementationName, boolean expected) {
		
		val testFileResult = issues = testHelper.testFile("edu.cmu.aaspe.examples/"+fileName)
		
		val implementation = (testFileResult.resource.contents.head as AadlPackage).publicSection.ownedClassifiers.filter(ComponentImplementation).findFirst[name == implementationName]
		Assert.assertNotNull('''Could not find implementation �implementationName�''', implementation)
		val instance = InstantiateModel.buildInstanceModelFile(implementation)
		val subclauses = implementation.getAllAnnexSubclauses(ResolutePackage.eINSTANCE.resoluteSubclause)
		val proveStatements = subclauses.map[(it as ResoluteSubclause).proves].flatten.toList
		Assert.assertFalse('''No prove statements found in �implementationName�''', proveStatements.empty)
		
		val allComponents = (instance.eAllContents.filter(ComponentInstance).toIterable + #[instance]).toList
		val categorySets = allComponents.groupBy[new BaseType(category).toString].mapValues[
			<NamedElement>newTreeSet(NE_COMPARATOR, it)
		]
		val componentSet = <NamedElement>newTreeSet(NE_COMPARATOR, allComponents)
		val connectionSet = newTreeSet(NE_COMPARATOR, instance.eAllContents.filter(ConnectionInstance).toSet)
		val sets = <String, SortedSet<NamedElement>>newHashMap(categorySets.entrySet.map[key -> value] + #["component" -> componentSet, "connection" -> connectionSet])
		
		val interpreter = new ResoluteInterpreter(new EvaluationContext(instance, sets, new FeatureToConnectionsMap(instance)))
		expected.assertEquals(proveStatements.forall[interpreter.evaluateProveStatement(it).valid])
	}
}