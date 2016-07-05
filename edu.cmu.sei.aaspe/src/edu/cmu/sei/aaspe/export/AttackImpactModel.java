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

package edu.cmu.sei.aaspe.export;

import java.util.HashMap;
import java.util.Map;

import org.osate.aadl2.NamedElement;

import edu.cmu.attackimpact.AttackImpactFactory;
import edu.cmu.attackimpact.Model;
import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.propagationType;
import edu.cmu.attackimpact.vulnerabilityType;
import edu.cmu.sei.aaspe.logic.AttackImpact;
import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.PropertyUtils;


public class AttackImpactModel {

	private AttackImpact analysis;
	private Model result;
	private Map<NamedElement,Node> aadlToNodes;
	private static final double SEVERITY_LIMIT = 10.0;

	
	public AttackImpactModel (AttackImpact a)
	{
		this.analysis = a;
		this.result = null;
		this.aadlToNodes = new HashMap<NamedElement,Node> ();
	}

	/**
	 * getNode retrieve the AttackImpact node related to the specific AADL
	 * named element. It makes sure that we have only one node per
	 * AADL named element.
	 * @param aadlElement - the AADL element
	 * @return            - the corresponding node
	 */
	private Node getNode (NamedElement aadlElement)
	{
		if (! this.aadlToNodes.containsKey(aadlElement))
		{
			Node newNode = AttackImpactFactory.eINSTANCE.createNode();
			newNode.setName(aadlElement.getName());
			newNode.setRelatedObject(aadlElement);


			/**
			 * We add the security domain to the node
			 */
			for (String domain : PropertyUtils.getSecurityDomains(aadlElement))
			{
				newNode.getDomains().add(domain);
			}

			this.aadlToNodes.put (aadlElement,newNode);

			/**
			 * Add the node to the model.
			 */
			this.result.getNodes().add(newNode);
		}

		return this.aadlToNodes.get(aadlElement);
	}

	private void process ()
	{
		result = AttackImpactFactory.eINSTANCE.createModel();
		result.setName(analysis.getSystemInstance().getName());
		result.setDescription("AttackImpact for the AADL system instance " + this.analysis.getSystemInstance().getName());

		for (Vulnerability vulnerability : this.analysis.getVulnerabilities())
		{
			edu.cmu.attackimpact.Vulnerability attackImpactVulnerability = AttackImpactFactory.eINSTANCE.createVulnerability();

			/**
			 * Get the node related to the model element.
			 */
			Node node = getNode(vulnerability.getRelatedElement());
			attackImpactVulnerability.setName(vulnerability.getName());
			attackImpactVulnerability.setDescription(vulnerability.getComment());
			node.getVulnerabilities().add(attackImpactVulnerability);


			switch (vulnerability.getType())
			{
				case Vulnerability.VULNERABILITY_AUTHENTICATION:
				{
					attackImpactVulnerability.setType(vulnerabilityType.AUTHENTICATION);
					break;
				}

				case Vulnerability.VULNERABILITY_CONCURRENCY:
				{
					attackImpactVulnerability.setType(vulnerabilityType.CONCURRENCY);
					break;
				}

				case Vulnerability.VULNERABILITY_ENCRYPTION:
				case Vulnerability.VULNERABILITY_SECURITY_DOMAIN:
				case Vulnerability.VULNERABILITY_SECURITY_LEVEL:
				{
					attackImpactVulnerability.setType(vulnerabilityType.ISOLATION);
					break;
				}

				case Vulnerability.VULNERABILITY_EXPOSITION:
				{
					attackImpactVulnerability.setType(vulnerabilityType.EXPOSURE);
					break;
				}

				case Vulnerability.VULNERABILITY_RESOURCE_DIMENSION:
				{
					attackImpactVulnerability.setType(vulnerabilityType.RESOURCE_ALLOCATION);
					break;
				}

				case Vulnerability.VULNERABILITY_TIMING:
				{
					attackImpactVulnerability.setType(vulnerabilityType.TIMING);
					break;
				}
			}

			for (Propagation propagation : vulnerability.getPropagations())
			{
				addPropagation (propagation);
			}
		}
	}

	private void addPropagation (Propagation propagation)
	{
		Node nodeSource = getNode (propagation.getSource());
		Node nodeDestination = getNode (propagation.getTarget());
		edu.cmu.attackimpact.Propagation attackImpactPropagation = AttackImpactFactory.eINSTANCE.createPropagation();

		switch (propagation.getType())
		{
			case Propagation.PROPAGATION_BUS:
			{
				attackImpactPropagation.setType(propagationType.BUS);
				break;
			}
			case Propagation.PROPAGATION_DATA:
			{
				attackImpactPropagation.setType(propagationType.DATA);
				break;
			}
			case Propagation.PROPAGATION_FLOW:
			{
				attackImpactPropagation.setType(propagationType.DATA_FLOW);
				break;
			}

			case Propagation.PROPAGATION_LOCAL:
			{
				attackImpactPropagation.setType(propagationType.LOCAL);
				break;
			}

			case Propagation.PROPAGATION_MEMORY:
			{
				attackImpactPropagation.setType(propagationType.MEMORY);
				break;
			}


			case Propagation.PROPAGATION_PROCESSOR:
			{
				attackImpactPropagation.setType(propagationType.PROCESSOR);
				break;
			}
		}



		attackImpactPropagation.getDestinations().add(nodeDestination);

		addPropagationToNode(nodeSource, attackImpactPropagation);

		for (Propagation next : propagation.getNextPropagations())
		{
			addPropagation (next);
		}
	}


	private static void addPropagationToNode (Node source, edu.cmu.attackimpact.Propagation propagation)
	{
		for (edu.cmu.attackimpact.Propagation existing : source.getPropagations())
		{
			if (
					(existing.getDestinations().containsAll(propagation.getDestinations()))
					&&
					(existing.getType() == propagation.getType())
				)
			{
				int actualSeverity = existing.getSeverity();
				int newSeverity = actualSeverity + 1;
				existing.setSeverity(newSeverity);
//				String str = "propagation " + propagation.hashCode() + " from " + source.getName()  + " to ";
//
//				for (Node d : existing.getDestinations())
//				{
//					str += " " + d.getName();
//				}
//				str += " old severity = " + actualSeverity + " new severity " + newSeverity;
//				OsateDebug.osateDebug("AttackImpactModel", str);

//				OsateDebug.osateDebug("AttackImpactModel", "get severity=" + existing.getSeverity());

				return;
			}
		}
		propagation.setSeverity(1);
		source.getPropagations().add (propagation);
	}

	public Model getAttackImpactModel (boolean normalizeSeverity)
	{
		if (this.result == null)
		{
			this.process();
		}

		if (normalizeSeverity)
		{
			double maxSeverity = getMaxPropagationSeverity();
			double increment = SEVERITY_LIMIT / maxSeverity;
			updateSeverity(increment);
		}
		return this.result;
	}

	private void updateSeverity (double increment)
	{
		for (Node node : this.result.getNodes())
		{
			for (edu.cmu.attackimpact.Propagation prop : node.getPropagations())
			{
				double newSeverity = prop.getSeverity() * increment;

				prop.setSeverity((int) Math.ceil(newSeverity));
//				OsateDebug.osateDebug("AttackImpactModel", "new severity = " + newSeverity);
			}
		}
	}

	private int getMaxPropagationSeverity ()
	{
		int severity = 0;
		for (Node node : this.result.getNodes())
		{
			for (edu.cmu.attackimpact.Propagation prop : node.getPropagations())
			{
				if (prop.getSeverity() > severity)
				{
					severity = prop.getSeverity();
				}
			}
		}
		return severity;
	}






}
