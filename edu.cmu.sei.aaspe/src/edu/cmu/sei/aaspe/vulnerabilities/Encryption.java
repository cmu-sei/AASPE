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
import org.osate.aadl2.util.OsateDebug;

import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public class Encryption extends AbstractVulnerability {



	public List<Vulnerability> findVulnerabilitiesForMemory(ComponentInstance memory) {
		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability> ();

		boolean isEncrypted = ComponentUtils.useEncryption(memory);

		if (isEncrypted == false)
		{
			Vulnerability v = new Vulnerability();
			v.setType(Vulnerability.VULNERABILITY_ENCRYPTION);
			v.setName ("Missing Encryption");
			v.setComment("missing encryption on memory " + memory.getName());
			v.setRelatedElement(memory);
			v.addPropagations(PropagationModel.getInstance().findPropagations(memory));
			vulnerabilities.add (v);
		}

		if ( isEncrypted && edu.cmu.sei.aaspe.utils.Utils.isWeakEncryption(ComponentUtils.getEncryptionAlgorithm(memory)))
		{
			Vulnerability v = new Vulnerability();
			v.setType(Vulnerability.VULNERABILITY_ENCRYPTION);
			v.setName ("Weak Encryption");
			v.setComment("weak encryption algorithm on " + memory.getName());
			v.setRelatedElement(memory);
			v.addPropagations(PropagationModel.getInstance().findPropagations(memory));
			vulnerabilities.add (v);
		}

		return vulnerabilities;
	}


	@Override
	public List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability> ();

		/**
		 * We check if memories are encrypted or not. If they are not encrypted, it
		 * is a security issues. If they are encrypted, we check that the security
		 * protocol is not too weak.
		 */
//		for (ComponentInstance memory : GetProperties.getActualMemoryBinding(component))
//		{
//			vulnerabilities.addAll(findVulnerabilitiesForMemory(memory));
//		}


		//if ( (component.getCategory() == ComponentCategory.MEMORY) || (component.getCategory() == ComponentCategory.VIRTUAL_MEMORY))
		if (component.getCategory() == ComponentCategory.MEMORY)

		{
			vulnerabilities.addAll(findVulnerabilitiesForMemory(component));
		}

		/**
		 * Check that is the component is connected to another
		 * component through a physical bus, it encrypts the data
		 * sent over the bus.
		 *
		 * We also check if the encryption algorithm is not too weak
		 * in case encryption is used.
		 */
		for (ConnectionInstance ci : component.getConnectionInstances()) {
//			ComponentInstance componentSource;
			ComponentInstance componentDestination;
			ComponentInstance componentSource;
//			FeatureInstance featureSource;
//			FeatureInstance featureDestination;

			/**
			 * now, we just take care of of port connection
			 */
			if (ci.getKind() != org.osate.aadl2.instance.ConnectionKind.PORT_CONNECTION) {
				continue;
			}

//			featureSource = (FeatureInstance) ci.getSource();
//			componentSource = ci.getSource().getContainingComponentInstance();
//			featureDestination = (FeatureInstance) ci.getDestination();
			componentDestination = ci.getDestination().getContainingComponentInstance();
			componentSource = ci.getSource().getContainingComponentInstance();

			/**
			 * isPhysical is a boolean that details if the connection is associated
			 * with a physical bus.
			 *
			 * isEncrypted specifies if the connection is using an encryption
			 * algorithm. Which means associated with a virtual bus that specifies
			 * an encryption mechanism.
			 */
			boolean isPhysical = ComponentUtils.isPhysical (ci);
			boolean isEncrypted = ComponentUtils.isEncrypted(ci);

			if (isPhysical && isEncrypted)
			{
				OsateDebug.osateDebug("Encryption", "connection " + ci.getName() + " is protected with encryption");
			}

			if (isPhysical && (! isEncrypted ) ) {
				OsateDebug.osateDebug("Encryption", "connection " + ci.getName() + " is NOT protected with encryption");

				Vulnerability v = new Vulnerability();
				v.setType(Vulnerability.VULNERABILITY_ENCRYPTION);
				v.setName ("Missing Encryption");
				v.setComment("missing encryption on connection " + ci.getName());
				v.setRelatedElement(componentSource);
				v.addPropagations(PropagationModel.getInstance().findPropagations (ci));
				vulnerabilities.add (v);
			}

			if ( isPhysical && isEncrypted && edu.cmu.sei.aaspe.utils.Utils.isWeakEncryption(ComponentUtils.getEncryptionAlgorithm(ci)))
			{
				Vulnerability v = new Vulnerability();
				v.setType(Vulnerability.VULNERABILITY_ENCRYPTION);
				v.setName ("Weak Encryption");
				v.setComment("weak encryption algorithm on " + ci.getName());
				v.setRelatedElement(componentSource);
				v.addPropagations(PropagationModel.getInstance().findPropagations (ci));
				vulnerabilities.add (v);
			}
		}


		return vulnerabilities;
	}
}
