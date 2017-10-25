/*
 * Copyright 2016 Carnegie Mellon University All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," WITH NO WARRANTIES WHATSOEVER. CARNEGIE
 * MELLON UNIVERSITY EXPRESSLY DISCLAIMS TO THE FULLEST EXTENT PERMITTEDBY LAW
 * ALL EXPRESS, IMPLIED, AND STATUTORY WARRANTIES, INCLUDING, WITHOUT
 * LIMITATION, THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, AND NON-INFRINGEMENT OF PROPRIETARY RIGHTS.

 * This Program is distributed under a BSD license.  Please see LICENSE file
 * or permission@sei.cmu.edu for more information.
 * 
 * DM-0003520
 */
 
 package edu.cmu.aaspe.codegeneration

import com.google.inject.Inject
import com.google.inject.Provider
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.util.Map
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IncrementalProjectBuilder
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.OperationCanceledException
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.XtextResourceSet
import org.osate.aadl2.AadlPackage
import org.osate.aadl2.modelsupport.resources.PredeclaredProperties
import org.osate.xtext.aadl2.ui.MyAadl2Activator
import org.osate.aadl2.Data
import java.util.List
import static extension edu.cmu.aaspe.codegeneration.camkes.CamkesGenerator.generateTypeDefinition
import java.util.ArrayList
import org.osate.aadl2.Classifier
import org.osate.aadl2.SubprogramType
import org.osate.aadl2.SubprogramImplementation
import org.osate.aadl2.Parameter
import org.osate.aadl2.DirectionType
import org.osate.aadl2.DataType

class Builder extends IncrementalProjectBuilder {
	@Inject
	Provider<XtextResourceSet> resourceSetProvider
	
	val public static String BUILDER_ID = "edu.cmu.aaspe.codegeneration.builder"
	
	new() {
		MyAadl2Activator.instance.getInjector(MyAadl2Activator.ORG_OSATE_XTEXT_AADL2_AADL2).injectMembers(this)
	} 
	
	override protected build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
//		if (project.name != PredeclaredProperties.PLUGIN_RESOURCES_PROJECT_NAME) {
			buildIContainer(project, monitor)
//		}
		null
	}
	
	def private void buildIContainer(IContainer container, IProgressMonitor monitor) {
		container.members.forEach[
			if (monitor.canceled) {
				throw new OperationCanceledException
			}
			switch it {
				IContainer: buildIContainer(monitor)
				IFile case fullPath.fileExtension == "aadl": {
					val uri = URI.createPlatformResourceURI(fullPath.toString, false)
					val resource = resourceSetProvider.get.getResource(uri, true)
					val rootElement = resource?.contents?.head
					if (rootElement instanceof AadlPackage) {
						buildTypes(rootElement, projectRelativePath, monitor)
						buildSubprograms(rootElement, projectRelativePath, monitor)
					}
				}
			}
		]
	}
	
	def static String generateTypesFile (AadlPackage aadlPackage)
	{
		val StringBuffer result = new StringBuffer()
		val List<String> existing = new ArrayList()
		
		result.append ("#ifndef __" + aadlPackage.name.toUpperCase + "_H__\n")
		result.append ("#define __" + aadlPackage.name.toUpperCase + "_H__\n\n")
		
		aadlPackage.publicSection.ownedClassifiers.forEach[ d | result.append(generateTypeDefinition(d,existing))]
		result.append ("#endif\n")
		return result.toString
	}
	
	def static String generateSubprogramsFile (AadlPackage aadlPackage)
	{
		val StringBuffer result = new StringBuffer()
		val List<String> existing = new ArrayList()
		
		aadlPackage.publicSection.ownedClassifiers.filter[ d | (d instanceof SubprogramType) || (d instanceof SubprogramImplementation)].forEach[ spg |
			var paramid = 0
			var String s =
''' 
void «spg.name» («FOR f : spg.allFeatures.filter(Parameter)» «IF (paramid++ > 0)»,«ENDIF»«(f as Parameter).dataFeatureClassifier.name »«IF ((f as Parameter).direction == DirectionType.OUT)»* «ENDIF»  «f.name» «ENDFOR» )
{
	
}
'''
			result.append(s)
		]

		return result.toString
	}	
	
	
	/**
	 * This method generates the header file with all the types.
	 * We generate a header file only if we have more than a single type.
	 */
	def private buildTypes(AadlPackage aadlPackage, IPath packagePath, IProgressMonitor monitor) {
		
		/*
		 * Count the number of types in this package
		 */
		val nbTypes = aadlPackage.publicSection.ownedClassifiers.filter(DataType).size
			
		if (nbTypes > 0)
		{
			val classifierNamesStream = new ByteArrayInputStream(aadlPackage.generateTypesFile.getBytes(StandardCharsets.UTF_8))
			val outputFile = project.getFile(packagePath.removeFileExtension.addFileExtension("h"))
			if (outputFile.exists) {
				outputFile.setContents(classifierNamesStream, false, true, monitor)
			} else {
				outputFile.create(classifierNamesStream, false, monitor)
			}
			
		}
	}
	
	
	/**
	 * This method create the c files containing all the subprograms.
	 */
	def private buildSubprograms(AadlPackage aadlPackage, IPath packagePath, IProgressMonitor monitor) {
		val classifierNamesStream = new ByteArrayInputStream(aadlPackage.generateSubprogramsFile.getBytes(StandardCharsets.UTF_8))
		val outputFile = project.getFile(packagePath.removeFileExtension.addFileExtension("c"))
		
		/**
		 * We generate the file only if there is something to generate (so, basically, if
		 * there are more than one byte in the inputsteam buffer.
		 */
		if (classifierNamesStream.available > 1)
		{
			if (outputFile.exists) {
				outputFile.setContents(classifierNamesStream, false, true, monitor)
			} else {
				outputFile.create(classifierNamesStream, false, monitor)
			}
		}
	}
	
}