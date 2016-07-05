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

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.osate.aadl2.NamedElement

/**
 * This class contains utility methods to create/copy/modify files inside the Eclipse
 * workspace. These methods are not related to the management of the AADL model, they
 * are only related to management of Eclipse resources.
 * 
 * For helper methods dealing with the AADL elements, please look at AadlHelper.xtend
 */
public class Utils
{
	def static void copy (NamedElement element, String fileSource, String fileDestination)
	{
		copy(element, fileSource, fileDestination, true)
	}
	
	/**
	 * Copy a file.
	 * The element is given in order to get the path. We usually give a SystemInstance component.
	 * So, the source and destination file locations are relative to the location of the element
	 * component in eclipse.
	 * The force argument force to replace/overwrite the destination file.
	 */
	def static void copy (NamedElement element, String fileSource, String fileDestination, boolean force)
	{
		val URI uri = EcoreUtil.getURI(element);
		val IPath ipath = new Path(uri.toPlatformString(true));
		val IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
		val String path = file.getRawLocation().removeLastSegments(1).toOSString();

		var pathSource = path + File.separator + fileSource
		var pathDestination = path + File.separator + fileDestination + fileSource
//		println("copy from " + pathSource + " to " + pathDestination)
		var File fileObjSource = new File (pathSource)
		
		if (! fileObjSource.exists)
		{
			pathSource = path + File.separator + ".." + File.separator + fileSource
			fileObjSource = new File (pathSource)
		}
		
		val File fileObjDestination = new File (pathDestination)
		
		createDirRecursive (fileObjDestination.parentFile)

		if (force && fileObjDestination.exists)
		{
			fileObjDestination.delete();
		}
		
		if (! fileObjDestination.exists)
		{
			Files.copy(fileObjSource.toPath(), fileObjDestination.toPath());
		}
	}
	
	
	/**
	 * Write a file. The element argument is there to indicate in what directory
	 * we want to write the text. The code argument is the content
	 * of the new created file. The fileName argument is the name of the file.
	 * The file will be created in the same directory as the element model element.
	 */
	def static void write  (NamedElement element, String fileName, String code)
	{
		var FileOutputStream fileOut;

		var URI uri = EcoreUtil.getURI(element);
		var IPath ipath = new Path(uri.toPlatformString(true));
		var IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
		var String path = file.getRawLocation().removeLastSegments(1).toOSString();
		path = path + File.separator + fileName;
	
		var File tmp = new File (path);
	
		createDirRecursive (tmp.parentFile)
		fileOut = new FileOutputStream(path);
		fileOut.write(code.getBytes());
		
		fileOut.close();
		
	}
	
	/**
	 * Create a directory recursively. Do the same thing as
	 * mkdir -p
	 */
	def static void createDirRecursive (File f)
	{
		if (f == null)
		{
			return
		}
		
		if (!f.exists)
		{
			createDirRecursive(f.parentFile)
			f.mkdir	
		}
	}
	
	
	/**
	 * refresh the eclipse workspace, useful to use
	 * after creating new files.
	 */
	def static refreshWorkspace (IProgressMonitor monitor)
	{
		for (IProject ip : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				ip.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}
	
	
}