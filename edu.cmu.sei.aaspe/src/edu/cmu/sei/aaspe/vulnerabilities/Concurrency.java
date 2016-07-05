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
import org.osate.aadl2.util.OsateDebug;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public class Concurrency extends AbstractVulnerability {
	@Override
	public List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		List<ComponentInstance> dataUsers;

		vulnerabilities = new ArrayList<Vulnerability> ();

		if (component.getCategory() != ComponentCategory.DATA)
		{
			return vulnerabilities;
		}

		dataUsers = ComponentUtils.getDataUsers (component);

		if ((dataUsers.size()> 1) && (GetProperties.getConcurrencyControlProtocol(component) == null) )
		{ 
			OsateDebug.osateDebug("Concurrency", "foobar");
			Vulnerability v = new Vulnerability();
			v.setType(Vulnerability.VULNERABILITY_CONCURRENCY);
			v.setName ("No Concurrency mechanism");
			v.setComment("missing concurrency mechanism on shared data " + component.getName());
			v.setRelatedElement(component);
			v.addPropagations(PropagationModel.getInstance().findPropagations(component));		
			vulnerabilities.add (v);
		}


		return vulnerabilities;
	}
}
