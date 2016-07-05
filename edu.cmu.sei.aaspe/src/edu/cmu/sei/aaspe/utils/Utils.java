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

package edu.cmu.sei.aaspe.utils;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.attackimpact.Model;


public class Utils {
	
	public static final String[] weakAuthenticationMethods = { "userpassword"};
	public static final String[] weakEncryptionMethods = { "userpassword"};

	public static boolean equalsSecurityLevelsOrDomain (List<?> levels1, List<?> levels2)
	{
		return ((levels1.containsAll(levels2)) && (levels2.containsAll(levels1)));
	}

	public static boolean isWeakEncryption (String mechanism)
	{
		for (String s : weakEncryptionMethods)
		{
			if(mechanism.equalsIgnoreCase(s))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isWeakAuthentication (String mechanism)
	{
		for (String s : weakAuthenticationMethods)
		{
			if(mechanism.equalsIgnoreCase(s))
			{
				return true;
			}
		}
		return false;
	}

	public static void refreshWorkspace(IProgressMonitor monitor) {
		for (IProject ip : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				ip.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return whether or not the parameters contains a name of an authentication
	 * method that is considered weak.
	 * @param authenticationMethods - the list of authentication methods to test
	 * @return
	 */
	public static boolean containsWeakAuthentication(List<String> authenticationMethods) {
		for (String method : authenticationMethods)
		{
			if (isWeakAuthentication(method))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isIsolated (ComponentInstance component)
	{
		
		if ( (component.getCategory() == ComponentCategory.PROCESS) || (component.getCategory() == ComponentCategory.DEVICE))
		{
			for (ComponentInstance cpu : GetProperties.getActualProcessorBinding(component))
			{
				if (ComponentUtils.getBoundProcessesToProcessor (cpu).size() > 1)
				{
					return false;
				}
			}
			return true;
		}
		
		if (component.getCategory() == ComponentCategory.THREAD)
		{
			return isIsolated(component.getContainingComponentInstance());
		}
		
		return false;
	}
	


}
