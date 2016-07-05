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

import java.util.List;

import org.osate.aadl2.instance.ComponentInstance;

import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public abstract class AbstractPropagation {
	public abstract List<Propagation> getPropagations(ComponentInstance component);
	
	
	public static void addPropagation (List<Propagation> list, ComponentInstance source, int kind, ComponentInstance destination)
	{
		Propagation newPropagationDestination = new Propagation(source, kind, ComponentUtils.getComponentFromConnectionEnd(destination));
		if (! list.contains(newPropagationDestination))
		{
			list.add(newPropagationDestination);
		}

	}
}
