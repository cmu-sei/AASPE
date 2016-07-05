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

import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.util.OsateDebug;

import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public class Authentication extends AbstractVulnerability {
	@Override 
	public List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability> ();

		/**
		 * Check that is the component is connected to another
		 * component through a physical bus, it encrypts the data
		 * sent over the bus.
		 * 
		 * We also check if the encryption algorithm is not too weak
		 * in case encryption is used.
		 */
		for (ConnectionInstance ci : component.getConnectionInstances()) {
			ComponentInstance componentDestination;
			ComponentInstance componentSource;
			
			/**
			 * now, we just take care of of port connection
			 */
			if (ci.getKind() != org.osate.aadl2.instance.ConnectionKind.PORT_CONNECTION) {
				continue;
			}

			componentSource = ci.getSource().getContainingComponentInstance();
			componentDestination = ci.getDestination().getContainingComponentInstance();

			/**
			 * isPhysical is a boolean that details if the connection is associated
			 * with a physical bus.
			 * 
			 * isEncrypted specifies if the connection is using an encryption
			 * algorithm. Which means associated with a virtual bus that specifies
			 * an encryption mechanism.
			 */
			boolean isPhysical = ComponentUtils.isPhysical (ci);
			boolean useAuthentication = ComponentUtils.useAuthentication(ci);
			

			if (isPhysical && (!useAuthentication ) ) {
				OsateDebug.osateDebug("Authentication", "connection " + ci.getName() + " does not use authentication");

				Vulnerability v = new Vulnerability();
				v.setType(Vulnerability.VULNERABILITY_AUTHENTICATION);
				v.setName ("Missing Authentication");
				v.setComment("missing authentication on connection " + ci.getName());
				v.setRelatedElement(componentSource);
				v.addPropagations(PropagationModel.getInstance().findPropagations (componentDestination));		
				vulnerabilities.add (v);
			}

			if ( isPhysical && useAuthentication && edu.cmu.sei.aaspe.utils.Utils.containsWeakAuthentication(ComponentUtils.getAuthenticationMethods(ci)))
			{
				Vulnerability v = new Vulnerability();
				v.setType(Vulnerability.VULNERABILITY_AUTHENTICATION);
				v.setName ("Weak Authentication");
				v.setComment("weak authentication method used on " + ci.getName());
				v.setRelatedElement(componentSource);
				v.addPropagations(PropagationModel.getInstance().findPropagations (componentDestination));				
				vulnerabilities.add (v);
			}
		}

		return vulnerabilities;
	}
}
