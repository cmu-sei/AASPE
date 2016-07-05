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
import org.osate.aadl2.instance.ConnectionKind;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;
import edu.cmu.sei.aaspe.utils.PropertyUtils;

public class SharedBus extends AbstractPropagation {


	public List<Propagation> getPropagations(ComponentInstance component)
	{
		ArrayList<Propagation> result;

		result = new ArrayList<Propagation> ();

		if ( (component.getCategory() != ComponentCategory.PROCESSOR) && (component.getCategory() != ComponentCategory.SYSTEM) && (component.getCategory() != ComponentCategory.DEVICE))
		{
			return result;
		}


		/**
		 * If the component is not a bus, then, we have to find if this component is connected
		 * with the other components.
		 * We are trying to find propagation through buses
		 */
		List <ConnectionInstance> connections = new ArrayList<ConnectionInstance> ();
		connections.addAll(component.getDstConnectionInstances());
		connections.addAll(component.getSrcConnectionInstances());

		for (ConnectionInstance connectionInstance : connections)
		{
			ComponentInstance destination = ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getDestination());

			if ((connectionInstance.getKind() == ConnectionKind.ACCESS_CONNECTION) && ((destination.getCategory() == ComponentCategory.BUS) || (component.getCategory() == ComponentCategory.BUS)))
			{
				addPropagation(result, component, Propagation.PROPAGATION_BUS, ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getSource()));
			}
		}


		for (FeatureInstance featureInstance : component.getFeatureInstances())
		{
			if (featureInstance.getCategory() == FeatureCategory.BUS_ACCESS)
			{
				for (ConnectionInstance connectionInstance : featureInstance.getDstConnectionInstances())
				{
					addPropagation(result,ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getSource()), Propagation.PROPAGATION_BUS, component);
					addPropagation(result,component, Propagation.PROPAGATION_BUS, ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getSource()));
				}

				for (ConnectionInstance connectionInstance : featureInstance.getSrcConnectionInstances())
				{
					
					addPropagation(result,ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getDestination()), Propagation.PROPAGATION_BUS, component);
					addPropagation(result,component, Propagation.PROPAGATION_BUS, ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getDestination()));
				}
			}
		}


		return result;
	}
}
