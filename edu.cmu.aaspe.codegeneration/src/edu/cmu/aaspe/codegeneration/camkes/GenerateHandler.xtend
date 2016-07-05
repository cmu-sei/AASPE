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
 
 package edu.cmu.aaspe.codegeneration.camkes

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
import static extension edu.cmu.aaspe.codegeneration.camkes.CamkesGenerator.generate
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.canBeGenerated
import org.osate.ui.dialogs.Dialog
import org.osate.aadl2.ComponentImplementation
import org.osate.aadl2.instance.SystemInstance
import org.osate.aadl2.instantiation.InstantiateModel

/**
 * This handler handles the action that generates the code.
 * The main class to generate code is CamkesGenerator.xtend
 */
class GenerateHandler extends AbstractHandler {
	override execute(ExecutionEvent event) throws ExecutionException {
		val selection = event.currentSelection as IStructuredSelection
		val selectedNode = selection.firstElement as IOutlineNode
		
		
		
		Job.create("seL4/CAmkES generation", [monitor |
			selectedNode.readOnly([EObject srcImpl |
				//Get new filename and create resource
				val SystemImplementation sys = srcImpl as SystemImplementation
				
				if (sys instanceof ComponentImplementation) {
					try {
						val SystemInstance si = InstantiateModel.buildInstanceModelFile(sys);
						
						if (! si.canBeGenerated)
						{
							Dialog.showError("Code Generation", "Cannot generate the model, please check it is compliant with code generation patterns");
						}
						else
						{
							si.generate
							
						}
					} catch (Exception e) {
						e.printStackTrace
						Dialog.showError("Model Instantiate", "Error while re-instantiating the model: " + e.getMessage());
						return Status.CANCEL_STATUS;
					}
				} else {
					Dialog.showError("Model Instantiate", "You must select a Component Implementation to instantiate");
					return Status.CANCEL_STATUS;
				}
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