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

import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.emf.ecore.EObject
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.eclipse.xtext.util.concurrent.IUnitOfWork
import org.osate.aadl2.AadlPackage
import org.osate.aadl2.SystemImplementation

import static extension org.eclipse.emf.ecore.util.EcoreUtil.copy
import static extension org.eclipse.emf.ecore.util.EcoreUtil.getURI
import static extension org.eclipse.ui.handlers.HandlerUtil.getCurrentSelection
import static extension org.eclipse.xtext.EcoreUtil2.getContainerOfType
import static extension edu.cmu.aaspe.codegeneration.sel4.Refinement.refine
import static extension edu.cmu.aaspe.codegeneration.sel4.Loader.generateLoaderCode
import static extension edu.cmu.aaspe.codegeneration.sel4.Loader.generateLoaderMakefile
import static extension edu.cmu.aaspe.codegeneration.sel4.Scheduler.generateSchedulerCode
import static extension edu.cmu.aaspe.codegeneration.sel4.Scheduler.generateSchedulerMakefile

class GenerateHandler extends AbstractHandler {
	override execute(ExecutionEvent event) throws ExecutionException {
		val selection = event.currentSelection as IStructuredSelection
		val selectedNode = selection.firstElement as IOutlineNode
		
		Job.create("Copy Package", [monitor |
			selectedNode.readOnly([EObject srcImpl |
				//Get new filename and create resource
				val srcURI = srcImpl.eResource.getURI
				val dstURI = srcURI.trimSegments(1).appendSegment(srcURI.trimFileExtension.lastSegment + "-refined").appendFileExtension(srcURI.fileExtension)
				val plop = srcImpl.eResource.resourceSet.getResource(dstURI, false)
				if (plop != null)
				{
					plop.delete(null)
				}
				
				val dstResource = srcImpl.eResource.resourceSet.createResource(dstURI)
				
				//Copy AadlPackage to new resource
				val dstPackage = srcImpl.getContainerOfType(AadlPackage).copy
				dstResource.contents += dstPackage
				
				//Create new ProcessSubcomponents
				dstResource.getEObject(srcImpl.getURI.fragment) as SystemImplementation => [dstImpl |
					dstImpl.refine
					dstImpl.generateLoaderCode
					dstImpl.generateLoaderMakefile
					dstImpl.generateSchedulerCode
					dstImpl.generateSchedulerMakefile
				]
				
				//Set new name of the copied AadlPackage
				dstPackage.name = dstPackage.name + "_refined"
				
				//Serialize new AadlPackage
				dstResource.save(null)
			] as IUnitOfWork.Void<EObject>)
			if (monitor.canceled) {
				Status.CANCEL_STATUS
			} else {
				Status.OK_STATUS
			}
		]).schedule
		null
	}
}