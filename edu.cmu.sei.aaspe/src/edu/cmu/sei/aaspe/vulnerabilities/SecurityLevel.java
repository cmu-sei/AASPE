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
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.ConnectionKind;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;
import edu.cmu.sei.aaspe.utils.PropertyUtils;

public class SecurityLevel extends AbstractVulnerability {


	@Override
	public List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability> ();

		/**
		 * Check that if components are communicating, they must share
		 * the same security levels unless the receiver has been verified.
		 */
		for (FeatureInstance fi : component.getFeatureInstances())
		{
			if (fi.getDirection() == DirectionType.IN)
			{
				continue;
			}
			
			for (ConnectionInstance ci : fi.getDstConnectionInstances())
			{
				if (ci.getKind() == ConnectionKind.PORT_CONNECTION)
				{
					ConnectionInstanceEnd source = ci.getSource();
					ConnectionInstanceEnd destination = ci.getDestination();

					ComponentInstance componentDestination;

					componentDestination = ComponentUtils.getComponentFromConnectionEnd(destination);

					if ( (PropertyUtils.isVerified(componentDestination) == false) &&
							(edu.cmu.sei.aaspe.utils.Utils.equalsSecurityLevelsOrDomain(ComponentUtils.getSecurityLevels (source), ComponentUtils.getSecurityLevels (destination)))) {
						Vulnerability v = new Vulnerability();
						v.setType(Vulnerability.VULNERABILITY_SECURITY_LEVEL);
						v.setName ("Security Levels");
						v.setComment("component " + component.getName() + " and " + componentDestination.getName() + " are connected and do not share the same security levels");
						v.setRelatedElement(component);
						v.addPropagations(PropagationModel.getInstance().findPropagations(component));		
						vulnerabilities.add(v);
					}
				}
			}
			
			
			
		}
		
		/**
		 * Check that if components are collocated on the same execution platform
		 * (processor), they need to share the same security level if the execution
		 * platform does not support isolation.
		 */

		ComponentInstance processor = GetProperties.getBoundProcessor(component);
		if ((processor != null ) && (processor.getCategory() == ComponentCategory.PROCESSOR))
		{
			for (ComponentInstance ci : ComponentUtils.getBoundProcessesToProcessor (processor))
			{
				if (ci == component)
				{
					continue;
				}
				if (edu.cmu.sei.aaspe.utils.Utils.equalsSecurityLevelsOrDomain (ComponentUtils.getSecurityLevels (ci), ComponentUtils.getSecurityLevels (component)) == false)
				{
					Vulnerability v = new Vulnerability();
					v.setType(Vulnerability.VULNERABILITY_SECURITY_LEVEL);
					v.setName ("Security Levels");
					v.setComment("component " + component.getName() + " and " + ci.getName() + " are on the same processor and does not share the same security levels");
					v.setRelatedElement(component);

					vulnerabilities.add(v);
				}
			}
		}
 
		/**
		 * Check that if components share a memory component, they have to share the same security
		 * level.
		 */
		List<ComponentInstance> memories = GetProperties.getActualMemoryBinding(component);
		for (ComponentInstance memory : memories)
		{
			for (ComponentInstance ci : ComponentUtils.getBoundComponentsToMemory (memory))
			{
				if (ci == component)
				{
					continue;
				}
				
				if (edu.cmu.sei.aaspe.utils.Utils.equalsSecurityLevelsOrDomain (ComponentUtils.getSecurityLevels (ci), ComponentUtils.getSecurityLevels (component)) == false)
				{
					Vulnerability v = new Vulnerability();
					v.setType(Vulnerability.VULNERABILITY_SECURITY_LEVEL);
					v.setName ("Security Levels");
					v.setComment("component " + component.getName() + " and " + ci.getName() + " share the same memory and does not share the same security levels");
					v.setRelatedElement(component);

					vulnerabilities.add(v);
				}
			}
		}


		return vulnerabilities;
	}
}
