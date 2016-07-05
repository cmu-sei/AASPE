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

import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.PropertyUtils;

public class Exposition extends AbstractVulnerability {

	


	@Override
	public  List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		long exposure;
		
		vulnerabilities = new ArrayList<Vulnerability> ();
		
		/*
		 * If the component is exposed physically AND is connected to a critical
		 * system, its might an issue. By physically exposed, we consider
		 * the following:
		 *   - Access to a memory
		 *   - Access to a bus
		 *   - Access to a system
		 *   - Access to a device
		 *   - Access to a processor
		 */

		/**
		 * 
		 */
		if (!((component.getCategory() == ComponentCategory.PROCESSOR) ||
		     (component.getCategory() == ComponentCategory.DEVICE) ||	
			 (component.getCategory() == ComponentCategory.BUS) ||
			 (component.getCategory() == ComponentCategory.SYSTEM) ||
			 (component.getCategory() == ComponentCategory.MEMORY)))
		{
			return vulnerabilities;
		}
		
		exposure = PropertyUtils.getExposure(component);
		if (exposure > 0) {
//			OsateDebug.osateDebug("[Exposition]", component.getName() + " exposure " + exposure);

			Vulnerability exposureVulnerability = new Vulnerability();
			exposureVulnerability.setType(Vulnerability.VULNERABILITY_EXPOSITION);
			exposureVulnerability.setName ("Physical Exposure");
			exposureVulnerability.setComment("component is physically exposed");
			exposureVulnerability.setRelatedElement(component);
			exposureVulnerability.addPropagations(PropagationModel.getInstance().findPropagations(component));		
			vulnerabilities.add (exposureVulnerability);
		}

		return vulnerabilities;
	}
}
