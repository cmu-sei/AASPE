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
 
 package edu.cmu.aaspe.codegeneration.sel4

import org.osate.aadl2.Aadl2Factory
import org.osate.aadl2.Aadl2Package
import org.osate.aadl2.AadlPackage
import org.osate.aadl2.DirectionType
import org.osate.aadl2.ProcessImplementation
import org.osate.aadl2.ProcessSubcomponent
import org.osate.aadl2.ProcessType
import org.osate.aadl2.PropertyAssociation
import org.osate.aadl2.ReferenceValue
import org.osate.aadl2.SystemImplementation
import org.osate.aadl2.ThreadSubcomponent
import org.osate.xtext.aadl2.properties.util.GetProperties
import org.osate.aadl2.SubprogramCallSequence
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getUserProcessSubcomponents
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getSchedulerProcessSubcomponent
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getPeriod
import org.osate.aadl2.VirtualProcessorType
import org.osate.aadl2.ListValue
import org.osate.aadl2.ProcessorSubcomponent

public class Refinement
{
	static val REFINEMENT_PREFIX = "_refined"
	static var AadlPackage currentPackage = null
	
//	SystemImplementation refinedSystem
//	 
//	new (SystemImplementation systemImplementation)
//	{
//		this.refinedSystem = systemImplementation	
//	}
//	
//	def AadlPackage getRefinedPackage ()
//	{
//		
//	}

	def static String computeClassifierNamed (String original)
	{
		var classifier = ""
		
		if(original.contains("."))
		{
			classifier = original.substring(0,original.indexOf('.')) + REFINEMENT_PREFIX +
						 original.substring(original.indexOf('.'), original.length)
		}
		else 
		{
			classifier = original + REFINEMENT_PREFIX
		}		
	}
		
	

	
	def static void createSchedulerConnections (SystemImplementation systemImplementation)
	{
		val schedulerProcessSubcomponent = systemImplementation.schedulerProcessSubcomponent
		
		systemImplementation.userProcessSubcomponents.forEach[process |
			process.componentType.ownedFeatures.filter[feat | feat.name.contains("activator")].forEach[ activatorFeature |
//println("searching for " + activatorFeature.name)
//				activatorFeature.get
				val featureName = process.name + "_" + activatorFeature.name
				var connEnd = schedulerProcessSubcomponent.componentType.ownedFeatures.findFirst[feat | feat.name.equalsIgnoreCase(featureName)]
				if (connEnd != null)
				{
					val conn = systemImplementation.createOwnedPortConnection
					conn.name = "scheduler_" + activatorFeature.name
					val connDest = conn.createDestination
					val connSrc = conn.createSource
					connDest.context = process
					connDest.connectionEnd = activatorFeature
					connSrc.context = schedulerProcessSubcomponent
					connSrc.connectionEnd = connEnd
					
				}				
			]
		]
		
	}
	
	def static void addSchedulerConnections (ProcessType existingType, ProcessImplementation existingImplementation,
		                                     ProcessType refinedType, ProcessImplementation refinedImplementation)
	{
		existingImplementation.allSubcomponents.filter(ThreadSubcomponent).forEach
		[
			thr | 
			      val threadComponentType = thr.componentType
			      val threadComponentImplementation = thr.componentImplementation
			      
			      
				  val newThreadType = Aadl2Factory.eINSTANCE.createThreadType
				  newThreadType.extended = threadComponentType
				  newThreadType.name = threadComponentType.name.computeClassifierNamed
				  
				  val newThreadImplementation = Aadl2Factory.eINSTANCE.createThreadImplementation
				  newThreadImplementation.extended = threadComponentImplementation
				  newThreadImplementation.name = threadComponentImplementation.name.computeClassifierNamed
				  newThreadImplementation.type = newThreadType
				  
				  currentPackage.publicSection.ownedClassifiers.add(newThreadImplementation)
				  currentPackage.publicSection.ownedClassifiers.add(newThreadType)
				 
//				 	thread implementation consumer_thread_refined.impl extends software::consumer_thread.impl
//	properties
//		Compute_Entrypoint_Call_Sequence => reference (mycalls) applies to activator;
//	end consumer_thread_refined.impl;

				val threadActivatorPort = newThreadType.createOwnedEventPort
				  threadActivatorPort.name = "activator"
				  threadActivatorPort.direction = DirectionType.IN
				 
				  val PropertyAssociation pa = newThreadImplementation.createOwnedPropertyAssociation 
				  pa.property = GetProperties.lookupPropertyDefinition(existingType, "Programming_Properties::Compute_Entrypoint_Call_Sequence")
				  pa.createOwnedValue => [ ov |
				  	ov.createOwnedValue(Aadl2Package.eINSTANCE.referenceValue) as ReferenceValue => [
				  		createPath => [
//				  			namedElement = threadComponentImplementation.findNamedElement("mycalls")
							namedElement = threadComponentImplementation.members.filter(SubprogramCallSequence).get(0)
				  		]
				  	]
				  ]
				  pa.createAppliesTo => [createPath => [
				  	namedElement = threadActivatorPort
				  ]]
				  
				  
			
				  val processActivatorPort = refinedType.createOwnedEventPort
			 	  processActivatorPort.name = thr.name + "_activator"
			 	  processActivatorPort.direction = DirectionType.IN
			 	  
			 	  val activatorConnection = refinedImplementation.createOwnedPortConnection
			 	  activatorConnection.name = "conn_" + thr.name + "_activator"
			 	  activatorConnection.createSource.connectionEnd = processActivatorPort
			 	  val dest = activatorConnection.createDestination
			 	  dest.context = thr
			 	  dest.connectionEnd = threadActivatorPort
			 	  
			 	  val refinedThread = refinedImplementation.createOwnedThreadSubcomponent
			 	  refinedThread.refined = thr
			 	  refinedThread.threadSubcomponentType = newThreadImplementation
			 	  refinedThread.name = thr.name
		]
	}


	def static void refine (ProcessSubcomponent process)
	{	
		val existingComponentType = process.componentType as ProcessType
		val existingComponentImplementation = process.componentImplementation as ProcessImplementation
		
		val existingTypeName = existingComponentType.name
		val existingImplementationName = existingComponentImplementation.name
		val newTypeName = existingTypeName.computeClassifierNamed
		val newImplementationName = existingImplementationName.computeClassifierNamed
		
//		println ("existing type" + existingTypeName)
//		println ("existing implementation " + existingImplementationName)
//		println ("new type" + newTypeName)
//		println ("new implementation " + newImplementationName)
				
		val newProcessType = Aadl2Factory.eINSTANCE.createProcessType;
		newProcessType.name = newTypeName
		newProcessType.extended = existingComponentType
		
		 
		val newProcessImplementation = Aadl2Factory.eINSTANCE.createProcessImplementation;
		newProcessImplementation.name = newImplementationName
		newProcessImplementation.extended = existingComponentImplementation
		newProcessImplementation.type = newProcessType
		
		currentPackage.publicSection.ownedClassifiers.add(newProcessType)
		currentPackage.publicSection.ownedClassifiers.add(newProcessImplementation)
	
		process.processSubcomponentType = newProcessImplementation
		
		addSchedulerConnections(existingComponentType,existingComponentImplementation,newProcessType,newProcessImplementation)
	}

	def static void refine(SystemImplementation systemImplementation)
	{
		val dstPackage = systemImplementation.owner.owner as AadlPackage
		currentPackage = dstPackage
		
		/**
		 * Start to refine all the existing processes
		 */		
		systemImplementation.allSubcomponents.filter(ProcessSubcomponent).forEach[ processSubcomponent | processSubcomponent.refine]
		
		
		val loaderProcessType = Aadl2Factory.eINSTANCE.createProcessType
		loaderProcessType.name = "loader"
		dstPackage.publicSection.ownedClassifiers.add(loaderProcessType)

		val loaderProcessImplementation = Aadl2Factory.eINSTANCE.createProcessImplementation
		loaderProcessImplementation.name = "loader.i"
		loaderProcessImplementation.type = loaderProcessType
		dstPackage.publicSection.ownedClassifiers.add(loaderProcessImplementation)
 
		
		val VirtualProcessorType virtualProcessorRuntime = Aadl2Factory.eINSTANCE.createVirtualProcessorType =>
		[
			name = "sel4_runtime"
			dstPackage.publicSection.ownedClassifiers.add(it)
		]
//		
		val schedulerRuntime = systemImplementation.createOwnedVirtualProcessorSubcomponent =>
		[
			name = "scheduler_runtime"
			virtualProcessorSubcomponentType = virtualProcessorRuntime
		]
//
		val loaderRuntime = systemImplementation.createOwnedVirtualProcessorSubcomponent =>
		[
			name = "loader_runtime"
			virtualProcessorSubcomponentType = virtualProcessorRuntime
		]
		
		


		/**
		 * Create the loader thread type and implementation
		 */
		val loaderThreadType = Aadl2Factory.eINSTANCE.createThreadType
		loaderThreadType.name = "loader_thread"
		dstPackage.publicSection.ownedClassifiers.add(loaderThreadType)
		
		val loaderThreadImplementation = Aadl2Factory.eINSTANCE.createThreadImplementation
		loaderThreadImplementation.name = "loader_thread.i"
		loaderThreadImplementation.type = loaderThreadType
		dstPackage.publicSection.ownedClassifiers.add(loaderThreadImplementation)

		val loaderThreadSubcomponent = loaderProcessImplementation.createOwnedThreadSubcomponent
		loaderThreadSubcomponent.name = "thr"
		loaderThreadSubcomponent.threadSubcomponentType = loaderThreadImplementation
		
				
		/**
		 * Create the scheduler process type and implementation
		 */
		val schedulerProcessType = Aadl2Factory.eINSTANCE.createProcessType
		schedulerProcessType.name = "scheduler"
		dstPackage.publicSection.ownedClassifiers.add(schedulerProcessType)
		
		val schedulerProcessImplementation = Aadl2Factory.eINSTANCE.createProcessImplementation
		schedulerProcessImplementation.name = "scheduler.i"
		schedulerProcessImplementation.type = schedulerProcessType
		dstPackage.publicSection.ownedClassifiers.add(schedulerProcessImplementation)
		
		/**
		 * Create the schedule thread type and implementation
		 */
		val schedulerThreadType = Aadl2Factory.eINSTANCE.createThreadType
		schedulerThreadType.name = "scheduler_thread"
		dstPackage.publicSection.ownedClassifiers.add(schedulerThreadType)
		
		val schedulerThreadImplementation = Aadl2Factory.eINSTANCE.createThreadImplementation
		schedulerThreadImplementation.name = "scheduler_thread.i"
		schedulerThreadImplementation.type = schedulerThreadType
		dstPackage.publicSection.ownedClassifiers.add(schedulerThreadImplementation)
//		
		val schedulerThreadSucbomponent = schedulerProcessImplementation.createOwnedThreadSubcomponent
		schedulerThreadSucbomponent.name = "thr"
		schedulerThreadSucbomponent.threadSubcomponentType = schedulerThreadImplementation
		
		/**
		 * Create port and connections in the scheduler process and thread subcomponents.
		 */
		systemImplementation.allSubcomponents.filter(ProcessSubcomponent).forEach[ processSubcomponent |
			val processImpl = processSubcomponent.componentImplementation as ProcessImplementation
			processImpl.allSubcomponents.filter(ThreadSubcomponent).forEach[ threadSubcomponent | 
				val portName = processSubcomponent.name + "_" + threadSubcomponent.name + "_activator"
				val processPort = schedulerProcessType.createOwnedEventPort
				processPort.direction = DirectionType.OUT
				processPort.name = portName
				
				val threadPort = schedulerThreadType.createOwnedEventPort
				threadPort.direction = DirectionType.OUT
				threadPort.name = portName
				
				val processConnection = schedulerProcessImplementation.createOwnedPortConnection
				processConnection.name = "conn_" + portName
				processConnection.bidirectional = false
				val src = processConnection.createDestination
				val dest = processConnection.createSource
				src.connectionEnd = processPort
				dest.context = schedulerThreadSucbomponent
				dest.connectionEnd = threadPort
			]
		]

		/**
		 * Create the loader process subcomponent
		 */
		val loaderProcess = systemImplementation.createOwnedProcessSubcomponent => [psSub |
			psSub.name = "loader"
			psSub.processSubcomponentType = loaderProcessImplementation
		]
		
		/**
		 * Add the binding between the loader to its runtime/virtual processor
		 */
		val PropertyAssociation loaderProcessBinding = systemImplementation.createOwnedPropertyAssociation
		loaderProcessBinding.property = GetProperties.lookupPropertyDefinition(loaderRuntime,
					"Deployment_Properties::Actual_Processor_Binding")

		loaderProcessBinding.createOwnedValue => [ ov |
			ov.createOwnedValue(Aadl2Package.eINSTANCE.listValue) as ListValue =>
			[ lv |
				val ref = lv.createOwnedListElement(Aadl2Package.eINSTANCE.referenceValue) as ReferenceValue => [
					createPath => [
						namedElement = loaderRuntime
					]
				]
			]
		]
		loaderProcessBinding.createAppliesTo => [
			createPath => [
				namedElement = loaderProcess
			]
		]
		
		
		/**
		 * Add the binding between the loader runtime/virtual processor to its CPU
		 */
		val PropertyAssociation loaderRuntimeBinding = systemImplementation.createOwnedPropertyAssociation
		loaderRuntimeBinding.property = GetProperties.lookupPropertyDefinition(loaderRuntime,
					"Deployment_Properties::Actual_Processor_Binding")

		loaderRuntimeBinding.createOwnedValue => [ ov |
			ov.createOwnedValue(Aadl2Package.eINSTANCE.listValue) as ListValue =>
			[ lv |
				val ref = lv.createOwnedListElement(Aadl2Package.eINSTANCE.referenceValue) as ReferenceValue => [
					createPath => [
						namedElement = systemImplementation.members.filter(ProcessorSubcomponent).get(0)
					]
				]
			]
		]
		loaderRuntimeBinding.createAppliesTo => [
			createPath => [
				namedElement = loaderRuntime
			]
		]
		
		/**
		 * Create the scheduler process subcomponent
		 */
		val schedulerProcess = systemImplementation.createOwnedProcessSubcomponent => [psSub |
			psSub.name = "scheduler"
			psSub.processSubcomponentType = schedulerProcessImplementation
		]
		
		/**
		 * Add the binding between the scheduler to its runtime/virtual processor
		 */
		val PropertyAssociation schedulerProcessBinding = systemImplementation.createOwnedPropertyAssociation
		schedulerProcessBinding.property = GetProperties.lookupPropertyDefinition(schedulerRuntime,
					"Deployment_Properties::Actual_Processor_Binding")

		schedulerProcessBinding.createOwnedValue => [ ov |
			ov.createOwnedValue(Aadl2Package.eINSTANCE.listValue) as ListValue =>
			[ lv |
				val ref = lv.createOwnedListElement(Aadl2Package.eINSTANCE.referenceValue) as ReferenceValue => [
					createPath => [
						namedElement = schedulerRuntime
					]
				]
			]
		]
		schedulerProcessBinding.createAppliesTo => [
			createPath => [
				namedElement = schedulerProcess
			]
		]
		
		
		/**
		 * Add the binding between the scheduler runtime/virtual processor to its CPU
		 */
		val PropertyAssociation schedulerRuntimeBinding = systemImplementation.createOwnedPropertyAssociation
		schedulerRuntimeBinding.property = GetProperties.lookupPropertyDefinition(schedulerRuntime,
					"Deployment_Properties::Actual_Processor_Binding")

		schedulerRuntimeBinding.createOwnedValue => [ ov |
			ov.createOwnedValue(Aadl2Package.eINSTANCE.listValue) as ListValue =>
			[ lv |
				val ref = lv.createOwnedListElement(Aadl2Package.eINSTANCE.referenceValue) as ReferenceValue => [
					createPath => [
						namedElement = systemImplementation.members.filter(ProcessorSubcomponent).get(0)
					]
				]
			]
		]
		schedulerRuntimeBinding.createAppliesTo => [
			createPath => [
				namedElement = schedulerRuntime
			]
		]		
		
		systemImplementation.createSchedulerConnections
	}
	
}