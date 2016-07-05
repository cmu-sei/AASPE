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

package edu.cmu.sei.aaspe.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.Status;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.util.OsateDebug;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import edu.cmu.sei.aaspe.Activator;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.vulnerabilities.AbstractVulnerability;

public class ComponentUtils {



	public static boolean isEncrypted(ConnectionInstance connection) {
		boolean result;

		result = false;

		List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(connection);
		for (ComponentInstance bus : buses) {
			if (useEncryption(bus)) {
				result = true;
			}
		}
		return result;
	}



	public static String getEncryptionAlgorithm(ConnectionInstance connection) {
		List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(connection);
		for (ComponentInstance bus : buses) {
			String s = getEncryptionAlgorithm (bus);
			if (s != null)
			{
				return s;
			}
		}

		return null;
	}


	public static boolean isPhysicalBus (ComponentInstance bus)
	{
		if (bus.getCategory() == org.osate.aadl2.ComponentCategory.BUS)
		{
			return true;
		}
		List<ComponentInstance> subBuses = GetProperties.getActualConnectionBinding (bus);
		for (ComponentInstance inst : subBuses)
		{
			if (isPhysicalBus (inst))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isPhysical(ConnectionInstance connection) {
		List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(connection);
		for (ComponentInstance inst : buses)
		{
			if (isPhysicalBus (inst))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean useEncryption(ComponentInstance component) {
		boolean result;
		String encryptionAlgorithm;

		result = false;

		encryptionAlgorithm = PropertyUtils.getEncryptionValue(component, "algorithm");

		if (encryptionAlgorithm != null) {
			OsateDebug.osateDebug("ComponentUtils", "encryption algorithm=" + encryptionAlgorithm);
			result = true;
		}

		if (component.getCategory() == ComponentCategory.BUS)
		{
			List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(component);
			for (ComponentInstance subBus : buses) {
				if (useEncryption(subBus)) {
					result = true;
				}
			}
		}
		return result;
	}

	public static String getEncryptionAlgorithm(ComponentInstance bus) {
		String encryptionAlgorithm;


		encryptionAlgorithm = PropertyUtils.getEncryptionValue(bus, "algorithm");

		if (encryptionAlgorithm != null) {
			OsateDebug.osateDebug("ComponentUtils", "encryption algorithm=" + encryptionAlgorithm);
			return encryptionAlgorithm;
		}

		List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(bus);
		for (ComponentInstance subBus : buses) {
			String s = getEncryptionAlgorithm (subBus);
			if (s != null) {
				return s;
			}
		}

		return null;
	}

	public static boolean isSharedData (ComponentInstance data)
	{
		ComponentInstance topLevel;
		int nbConnection = 0;
		topLevel = data.getContainingComponentInstance();

		for (ConnectionInstance ci : topLevel.getConnectionInstances())
		{
			if ( (ci.getSource() == data) || (ci.getDestination() == data))
			{
				nbConnection = nbConnection + 1;
			}
		}

		return (nbConnection > 1);
	}

	public static ComponentInstance getComponentFromConnectionEnd (ConnectionInstanceEnd ce)
	{
		if (ce instanceof FeatureInstance)
		{
			return ((FeatureInstance)ce).getContainingComponentInstance();
		}
		if (ce instanceof ComponentInstance)
		{
			return (ComponentInstance) ce;
		}
		return null;
	}

	

	/**
	 * It will list of vulnerabilities for a component:
	 *    - Potential buffer overflow with communication queues (inconsistent timing requirements)
	 *    - Potential concurrency issue with shared data
	 *    - Connection with components at a different security levels (inconsistent security levels)
	 *    - Connections bound to a physical bus and not using an encryption mechanism
	 *    - Processor executing several processes without having separation mechanisms
	 *    - Memory shared with components are different security levels
	 *    - Buses that are used by several process at different security levels
	 *    - Use of a weak encryption protocol
	 * @param component
	 * @return
	 */
	public static List<Vulnerability> getAllVulnerabilities(ComponentInstance component) {

		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability>();

		OsateDebug.osateDebug("Analyzing component " + component.getName());

		/**
		 * In that case, we do not return anything because these types of components
		 * do not need to show any vulnerability.
		 */
		if ( (component instanceof SystemInstance) ||
		   (component.getCategory() == ComponentCategory.VIRTUAL_BUS) ||
		   ( ( component.getCategory() == ComponentCategory.SYSTEM) && (component.getComponentInstances().size() > 0))
		   )
		{
			return vulnerabilities;
		}



//		OsateDebug.osateDebug("ComponentUtils", "findVulnerabilities analyzing component " + component.getName());
		//		OsateDebug.osateDebug("ComponentUtils", "root= " + root);

		String pluginId = Activator.getDefault().getBundle().getSymbolicName();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry != null) {
			IExtensionPoint point = registry.getExtensionPoint(pluginId + ".vulnerability");
			if (point != null) {
				for (IExtension extension : point.getExtensions()) {
					for (IConfigurationElement configElement : extension.getConfigurationElements()) {
						try {
							AbstractVulnerability vulnerabilityProvider = (AbstractVulnerability)configElement.createExecutableExtension("class");
							vulnerabilities.addAll(vulnerabilityProvider.findVulnerabilities(component));
						} catch (CoreException e) {
							Activator.getDefault().getLog().log(new Status(Status.ERROR, pluginId, e.getMessage(), e));
						}
					}
				}
			}
		}

		return vulnerabilities;
	}



	public static List<Integer> getSecurityLevels (ConnectionInstanceEnd cie)
	{
		List<Integer> levels;

		levels = PropertyUtils.getSecurityLevels(cie);


		if ( (cie instanceof FeatureInstance) && (levels.size() == 0))
		{
			levels = PropertyUtils.getSecurityLevels(cie.getContainingComponentInstance());
		}

		return levels;
	}

	/**
	 * Returns the value of the security domains on a connection end (feature, access, etc.).
	 * If the property is not defined on the features, it tries to take the value
	 * from the component where it is defined.
	 * @param cie - the connection end (feature, access, etc.)
	 * @return - the list of security domains
	 */
	public static List<String> getSecurityDomains (ConnectionInstanceEnd cie)
	{
		List<String> levels;

		levels = PropertyUtils.getSecurityDomains(cie);


		if ( (cie instanceof FeatureInstance) && (levels.size() == 0))
		{
			levels = PropertyUtils.getSecurityDomains(cie.getContainingComponentInstance());
		}

		return levels;
	}


	public static NamedElement getPhysicalbus(ConnectionInstance ci) {
		List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(ci);

		for (ComponentInstance bus : buses)
		{
			while ((bus.getCategory() != ComponentCategory.BUS) && (bus != null))
			{
				List<ComponentInstance> subbuses = GetProperties.getActualConnectionBinding(bus);
				if (subbuses.size() > 0)
				{
					bus = subbuses.get(0);
				}
				else
				{
					bus = null;
				}
			}

			if ((bus != null) && (bus.getCategory() == ComponentCategory.BUS))
			{
				return bus;
			}
		}

		return null;
	}

	private static List<ComponentInstance> getBoundVirtualProcessorsToProcessorRec (ComponentInstance processor, ComponentInstance ci)
	{
		List<ComponentInstance> components;
		components = new ArrayList<ComponentInstance> ();

		if (ci.getCategory() == ComponentCategory.VIRTUAL_PROCESSOR)
		{
			ComponentInstance boundProcessor = GetProperties.getBoundProcessor(ci);

			if (boundProcessor == processor)
			{
					components.add (ci);
			}
		}

		for (ComponentInstance sub : ci.getComponentInstances())
		{
			components.addAll(getBoundVirtualProcessorsToProcessorRec(processor, sub));
		}

		return components;
	}

	public static List<ComponentInstance> getBoundVirtualProcessorsToProcessor (ComponentInstance processor)
	{
		return getBoundVirtualProcessorsToProcessorRec(processor, processor.getSystemInstance());
	}

	private static List<ComponentInstance> getBoundComponentsToProcessorRec (ComponentInstance processor, ComponentInstance ci)
	{
		List<ComponentInstance> components;
		components = new ArrayList<ComponentInstance> ();

		if ((ci.getCategory() == ComponentCategory.PROCESS) && (GetProperties.getBoundProcessor(ci) == processor))
		{
			components.add (ci);
		}

		for (ComponentInstance sub : ci.getComponentInstances())
		{
			components.addAll(getBoundComponentsToProcessorRec(processor, sub));
		}

		return components;
	}

	public static List<ComponentInstance> getBoundProcessesToProcessor (ComponentInstance processor)
	{
		return getBoundComponentsToProcessorRec(processor, processor.getSystemInstance());
	}

	private static List<ComponentInstance> getBoundComponentsToMemoryRec (ComponentInstance memory, ComponentInstance ci)
	{
		List<ComponentInstance> components;
		components = new ArrayList<ComponentInstance> ();

		for (ComponentInstance foo : GetProperties.getActualMemoryBinding(ci))
		{
			if (foo == memory)
			{
				components.add (ci);
			}
		}


		for (ComponentInstance sub : ci.getComponentInstances())
		{
			components.addAll(getBoundComponentsToMemoryRec(memory, sub));
		}

		return components;
	}

	public static List<ComponentInstance> getBoundComponentsToMemory (ComponentInstance memory)
	{
		return getBoundComponentsToMemoryRec(memory, memory.getSystemInstance());
	}

	/**
	 * get the list of all components that access a particular data. This is a recursive
	 * function used internally.
	 * @param data - the data being accessed
	 * @param ci   - the component being analyzed.
	 * @return
	 */
	private static List<ComponentInstance> getDataUsersRec (ComponentInstance data, ComponentInstance ci)
	{
		List<ComponentInstance> components;
		components = new ArrayList<ComponentInstance> ();

		for (FeatureInstance fi : ci.getFeatureInstances())
		{
			for (ConnectionInstance conni : fi.getDstConnectionInstances())
			{
				ComponentInstance dest = getComponentFromConnectionEnd(conni.getSource());
				if (dest == data)
				{
					components.add(ci);
				}
			}

			for (ConnectionInstance conni : fi.getSrcConnectionInstances())
			{
				ComponentInstance src = getComponentFromConnectionEnd(conni.getDestination());
				if (src == data)
				{
					components.add(ci);
				}
			}
		}


		for (ComponentInstance sub : ci.getComponentInstances())
		{
			components.addAll(getDataUsersRec(data, sub));
		}

		return components;
	}

	/**
	 * Get the list of all components that are accessing a particular data.
	 * @param data - the data being accessed.
	 * @return
	 */
	public static List<ComponentInstance> getDataUsers (ComponentInstance data)
	{
		return getDataUsersRec(data, data.getSystemInstance());
	}


	/**
	 * get all the authentication methods for a component. Basically, here
	 * we are getting all the authentication methods of a bus. If the bus
	 * is bound to another one, we retrieve the value from the bound buses.
	 * @param component
	 * @return
	 */
	public static List<String> getAuthenticationMethods(ComponentInstance component) {

		List<ComponentInstance> buses;
		List<String> methods;

		methods = PropertyUtils.getAuthenticationMethods(component);

		if ((methods != null) && (methods.size() > 0))
		{
			return methods;
		}

		buses = GetProperties.getActualConnectionBinding(component);

		for (ComponentInstance bus : buses) {
			methods = getAuthenticationMethods (bus);
			if ((methods != null) && (methods.size() > 0))
			{
				return methods;
			}
		}

		return null;
	}

	/**
	 * Get the authentication for a connection. It will then call the
	 * method with the same name that will retrieve the methods
	 * on associated virtual buses and potentially nested virtual
	 * buses componnets.
	 * @param connection
	 * @return
	 */
	public static List<String> getAuthenticationMethods(ConnectionInstance connection) {

		List<ComponentInstance> buses = GetProperties.getActualConnectionBinding(connection);
		for (ComponentInstance bus : buses) {
			List<String> methods = getAuthenticationMethods (bus);
			if ((methods != null) && (methods.size() > 0))
			{
				return methods;
			}
		}

		return null;
	}

	/**
	 * Return wether or not the connection uses an authentication
	 * method.
	 * @param ci - the connection method to be tested.
	 * @return
	 */
	public static boolean useAuthentication(ConnectionInstance ci) {
		List<String> methods;

		methods = getAuthenticationMethods(ci);

		if (methods == null)
		{
			return false;
		}

		if (methods.size() == 0)
		{
			return false;
		}

		return true;
	}
}
