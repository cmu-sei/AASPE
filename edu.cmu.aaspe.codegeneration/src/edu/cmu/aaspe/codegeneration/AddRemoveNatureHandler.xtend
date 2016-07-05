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

import java.util.ArrayList
import org.eclipse.core.commands.AbstractHandler
import org.eclipse.core.commands.ExecutionEvent
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IAdaptable
import org.eclipse.jface.viewers.IStructuredSelection

import static extension org.eclipse.ui.handlers.HandlerUtil.getCurrentSelection

/**
 * This class is used to enable/disable the code generation nature
 * of a project. This is used/specified inside the plugin.xml file.
 */

class AddRemoveNatureHandler extends AbstractHandler {
	override execute(ExecutionEvent event) throws ExecutionException {
		val selection = event.currentSelection
		if (selection instanceof IStructuredSelection) {
			val projects = selection.iterator.map[Object it | switch it {
				IProject: it
				IAdaptable: getAdapter(IProject)
			}].filterNull
			projects.forEach[try {
				val natures = new ArrayList(description.natureIds)
				//This toggles the nature. Try to remove it and if that fails, then add it.
				if (!(natures -= Nature.NATURE_ID)) {
					natures += Nature.NATURE_ID
				}
				setDescription(description => [natureIds = natures], null)
			} catch (CoreException e) {
				throw new ExecutionException("Failed to toggle nature", e)
			}]
		}
		null
	}
}