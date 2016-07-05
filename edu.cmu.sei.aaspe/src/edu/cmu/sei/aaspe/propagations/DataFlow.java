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
import org.osate.aadl2.instance.EndToEndFlowInstance;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.FlowElementInstance;

import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;
import edu.cmu.sei.aaspe.utils.PropertyUtils;

public class DataFlow extends AbstractPropagation {
	
	
	public List<Propagation> getPropagations(ComponentInstance component)
	{
		ArrayList<Propagation> result;
		result = new ArrayList<Propagation> ();
		
		if (component == component.getSystemInstance())
		{
			return result;
		}
		
		/**
		 * Find if the component is in the beginning of an end to end flow. We are trying
		 * to find propagation through flows
		 */
		for (EndToEndFlowInstance etef : component.getSystemInstance().getEndToEndFlows())
		{
			FlowElementInstance source = etef.getFlowElements().get(0);
			FlowElementInstance destination = etef.getFlowElements().get(etef.getFlowElements().size() - 1);
			
			ComponentInstance flowSource = source.getComponentInstance();
			ComponentInstance flowDestination = destination.getComponentInstance();

			if ((flowSource == component) && ( ! PropertyUtils.isVerified(flowDestination)))
			{
				addPropagation(result, component, Propagation.PROPAGATION_FLOW, flowDestination);
			}
		}
		
		/**
		 * Trying to find propagation through port connections.
		 */
		for (FeatureInstance featureInstance : component.getFeatureInstances())
		{
			if ( (featureInstance.getCategory() != FeatureCategory.EVENT_PORT) && (featureInstance.getCategory() != FeatureCategory.EVENT_DATA_PORT) &&  (featureInstance.getCategory() != FeatureCategory.DATA_PORT))
			{
				continue;
			}

			for (ConnectionInstance connectionInstance : featureInstance.getDstConnectionInstances())
			{
				ComponentInstance source = ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getSource());
				if (source == component.getSystemInstance())
				{
					continue;
				}
				addPropagation(result, component, Propagation.PROPAGATION_FLOW, source);
			}

			for (ConnectionInstance connectionInstance : featureInstance.getSrcConnectionInstances())
			{
				ComponentInstance destination = ComponentUtils.getComponentFromConnectionEnd(connectionInstance.getDestination());
				if (destination == component.getSystemInstance())
				{
					continue;
				}
				addPropagation(result, component, Propagation.PROPAGATION_FLOW, destination);
			}

		}
		
		return result;
	}
}
