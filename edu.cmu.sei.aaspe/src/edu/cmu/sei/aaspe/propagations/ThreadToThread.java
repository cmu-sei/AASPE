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

package edu.cmu.sei.aaspe.propagations;

import java.util.ArrayList;
import java.util.List;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.instance.ComponentInstance;

import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public class ThreadToThread extends AbstractPropagation {
	
	
	public List<Propagation> getPropagations(ComponentInstance component)
	{
		ArrayList<Propagation> result;
		
		result = new ArrayList<Propagation> ();
		
		/**
		 * If this is a thread, the thread can then impact all
		 * other threads contained in the same address space
		 * unless the process is verified. We check that the process
		 * is verified before in this code (see beginning of the function).
		 */
		if (component.getCategory() == ComponentCategory.THREAD)
		{
			ComponentInstance relatedProcess;
			relatedProcess = component.getContainingComponentInstance();

			if (relatedProcess.getCategory() == ComponentCategory.PROCESS)
			{
				for (ComponentInstance containedComponent : relatedProcess.getAllComponentInstances())
				{
					if  ( (containedComponent.getCategory() == ComponentCategory.THREAD) && (containedComponent != component))
					{
						addPropagation(result, component, Propagation.PROPAGATION_LOCAL, containedComponent);
					}
				}
			}

		}
		
		return result;
	}
}
