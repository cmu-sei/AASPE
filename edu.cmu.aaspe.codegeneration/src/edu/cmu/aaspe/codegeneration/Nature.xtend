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

import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IProjectNature
import org.eclipse.core.runtime.CoreException
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * This class is there to add the code generation nature to the project.
 */
class Nature implements IProjectNature {
	val public static String NATURE_ID = "edu.cmu.aaspe.codegeneration.nature"
	
	@Accessors
	IProject project
	
	override configure() throws CoreException {
		val desc = project.description
		val commands = desc.buildSpec
		if (!commands.exists[builderName == Builder.BUILDER_ID]) {
			desc.buildSpec = commands + #[desc.newCommand => [builderName = Builder.BUILDER_ID]]
			project.setDescription(desc, null)
		}
	}
	
	override deconfigure() throws CoreException {
		val description = project.description
		description.buildSpec = description.buildSpec.filter[builderName != Builder.BUILDER_ID]
		project.setDescription(description, null)
	}
}