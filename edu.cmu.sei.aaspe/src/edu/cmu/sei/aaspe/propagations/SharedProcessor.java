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
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public class SharedProcessor extends AbstractPropagation {
	
	
	public List<Propagation> getPropagations(ComponentInstance component)
	{
		ArrayList<Propagation> result;
		
		result = new ArrayList<Propagation> ();
		
		/**
		 * If the component impacted is a processor, it impacts all components bound to it.
		 */
		if (component.getCategory() == ComponentCategory.PROCESSOR)
		{
			for (ComponentInstance boundComponent : ComponentUtils.getBoundProcessesToProcessor(component))
			{
				addPropagation(result, component, Propagation.PROPAGATION_PROCESSOR, boundComponent);
			}

			for (ComponentInstance boundComponent : ComponentUtils.getBoundVirtualProcessorsToProcessor(component))
			{
				addPropagation(result, component, Propagation.PROPAGATION_PROCESSOR, boundComponent);
			}
		}


		if (component.getCategory() == ComponentCategory.VIRTUAL_PROCESSOR)
		{
			for (ComponentInstance boundComponent : ComponentUtils.getBoundProcessesToProcessor(component))
			{
				addPropagation(result, component, Propagation.PROPAGATION_PROCESSOR, boundComponent);
			}
		}
		
		return result;
	}
}
