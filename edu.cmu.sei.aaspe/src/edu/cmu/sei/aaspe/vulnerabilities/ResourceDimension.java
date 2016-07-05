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

package edu.cmu.sei.aaspe.vulnerabilities;

import java.util.ArrayList;
import java.util.List;

import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;


public class ResourceDimension extends AbstractVulnerability {


	@Override
	public List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability> ();

		/**
		 * Find potential buffer overflow issues with the connections
		 * where the queue size of the receiver is not correctly
		 * allocated/dimensioned. This kind of issue can be found
		 * for system, threads and process.
		 */
		
		if (!((component.getCategory() == ComponentCategory.PROCESS) ||
	  	     (component.getCategory() == ComponentCategory.SYSTEM) ||
		     (component.getCategory() == ComponentCategory.THREAD)))
		{
			return vulnerabilities;
		}
		
		for (ConnectionInstance ci : component.getConnectionInstances()) {
			ComponentInstance componentSource;
			ComponentInstance componentDestination;
			FeatureInstance featureSource;
			FeatureInstance featureDestination;

			/**
			 * now, we just take care of of port connection
			 */
			if (ci.getKind() != org.osate.aadl2.instance.ConnectionKind.PORT_CONNECTION) {
				continue;
			}

			featureSource = (FeatureInstance) ci.getSource();
			componentSource = ci.getSource().getContainingComponentInstance();
			featureDestination = (FeatureInstance) ci.getDestination();
			componentDestination = ci.getDestination().getContainingComponentInstance();

			if ( (featureSource.getCategory() == FeatureCategory.EVENT_DATA_PORT) ||
					(featureSource.getCategory() == FeatureCategory.EVENT_PORT))
			{
				long srcQueueSize = GetProperties.getQueueSize(featureSource);
				long dstQueueSize = GetProperties.getQueueSize(featureDestination);

				double periodSource = GetProperties.getPeriodinMS(componentSource);
				double periodDestination = GetProperties.getPeriodinMS(componentDestination);

				double sendingPeriod = periodSource / srcQueueSize;
				double receivingPeriod = periodDestination / dstQueueSize;

				if (sendingPeriod < receivingPeriod)
				{
					Vulnerability v = new Vulnerability();
					v.setType(Vulnerability.VULNERABILITY_RESOURCE_DIMENSION);
					v.setName ("Resource Dimension");
					v.setComment("Component " + componentSource.getName() + " sends data too fast to component " + componentDestination);
					v.setRelatedElement(component);
					v.addPropagations(PropagationModel.getInstance().findPropagations(component));		

					vulnerabilities.add (v);
				}

			}
		}
		return vulnerabilities;
	}
}
