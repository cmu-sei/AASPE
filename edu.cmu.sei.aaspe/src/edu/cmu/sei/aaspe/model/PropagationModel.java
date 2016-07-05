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

package edu.cmu.sei.aaspe.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.Status;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.util.OsateDebug;

import edu.cmu.sei.aaspe.Activator;
import edu.cmu.sei.aaspe.propagations.AbstractPropagation;
import edu.cmu.sei.aaspe.utils.PropertyUtils;

/**
 * This class works as a cache for all potential impacts. The goal
 * is to have a singleton class that process a model and returns
 * all the propagation paths between components.
 *
 * When using the class, you have to get it like this:
 * PropagationModel.getInstance().<method_to_invoke>
 *
 * To reset the cache, just to PropagationModel.getInstance().reset()
 *
 * @author julien
 *
 */
public class PropagationModel {

	private Map<NamedElement,List<Propagation>> propagationCache;
	private Map<NamedElement,List<Propagation>> propagationCacheFull;
	private static PropagationModel INSTANCE;

	/**
	 * Private method that indicates if we should add a propagation
	 * on the stack or not. The method will then check if the propagation
	 * was already added OR if the progagation is in the next propagation
	 * of a propagation in the stack.
	 * @param propagation - the propagation that might be added
	 * @param stack       - the stack containing all existing propagations.
	 * @return
	 */
	private static boolean shouldAdd (Propagation propagation, Stack<Propagation> stack)
	{
//		OsateDebug.osateDebug("shouldadd toadd="+ propagation);

		for (Propagation p : stack)
		{
			if (shouldAdd (propagation, p) == false)
			{
				return false;
			}
		}


		return true;
	}

	/**
	 * check if a propagation should be added considering another existing
	 * propagation. The method will then look for all propagation at all
	 * levels (next propagations) and return false if the propagation that is
	 * being already exist.
	 * @param toAdd - the propagation that is trying to be added
	 * @param existing - the existing propagation
	 * @return
	 */
	private static boolean shouldAdd (Propagation toAdd, Propagation existing)
	{

//		if (existing.getSource() == toAdd.getTarget())
//		{
//			OsateDebug.osateDebug("[PropagationModel] shouldAdd returns false existing source = " + existing.getSource().getName() + " ; target = " + toAdd.getTarget().getName());
//			return false;
//		}

		if (existing.equals(toAdd))
		{
			OsateDebug.osateDebug("[PropagationModel] existing equals toAdd");
			return false;
		}

		for (Propagation subprop : existing.getNextPropagations())
		{
			if (shouldAdd (toAdd, subprop) == false)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * This is a singleton method - this method returns the instance
	 * @return the instance of the class
	 */
	public static PropagationModel getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new PropagationModel();
			INSTANCE.reset();
		}
		return INSTANCE;
	}

	/**
	 * Reset the cache
	 */
	public void reset ()
	{
		this.propagationCache = new HashMap <NamedElement,List<Propagation>> ();
		this.propagationCacheFull = new HashMap <NamedElement,List<Propagation>> ();
	}

	/**
	 * find all propagations for a particular components
	 * @param ci - the component that is the source of the propagation
	 * @return - list of all propagations
	 */
	public List<Propagation> findPropagations (NamedElement ci)
	{
		List<Propagation> result = new ArrayList<Propagation> ();

		if (this.propagationCacheFull.containsKey(ci))
		{
			return this.propagationCacheFull.get(ci);
		}

		for (Propagation propagation : getPropagationsFirstLevel(ci))
		{
			Propagation newProp = propagation.copy(false);
			result.add(newProp);
			fillNextPropagations (newProp, new Stack<Propagation>());
		}

		this.propagationCacheFull.put(ci, result);
		return result;
	}

	private void fillNextPropagations (Propagation propagation, Stack<Propagation> stack)
	{
		if (stack.contains(propagation))
		{
			return;
		}


//		OsateDebug.osateDebug("WANT TO ADD: " + propagation);

		String dbg;
		dbg = "";

		for (Propagation ps : stack)
		{
			dbg += " " + ps.toString();
		}
//		OsateDebug.osateDebug("STACK: " + dbg);

		stack.push(propagation);
		for (Propagation nextPropagation : getPropagationsFirstLevel(propagation.getTarget()))
		{
			if ( (!stack.contains(nextPropagation)) && (shouldAdd(nextPropagation, stack)))
			{
//				Propagation newProp = new Propagation (nextPropagation.getSource(), nextPropagation.getType(), nextPropagation.getTarget());
				Propagation newProp = nextPropagation.copy(false);
				fillNextPropagations (newProp, stack);
				propagation.addNextPropagation(newProp);
			}
		}
		stack.pop();
	}

	private List<Propagation> getPropagationsFirstLevel (NamedElement element)
	{
		if (element instanceof ComponentInstance)
		{
			return getPropagationsFirstLevel((ComponentInstance) element);
		}

		OsateDebug.osateDebug("ERROR NOT HANDLED");
		return new ArrayList<Propagation> ();
	}



	private List<Propagation> getPropagationsFirstLevel (ComponentInstance ci)
	{
		if (propagationCache.containsKey(ci))
		{

			return propagationCache.get(ci);
		}

		List<Propagation> list = new ArrayList<Propagation> ();

		if (PropertyUtils.isVerified(ci))
		{
			propagationCache.put(ci, list);
			return list;
		}

		String pluginId = Activator.getDefault().getBundle().getSymbolicName();
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		if (registry != null) {
			IExtensionPoint point = registry.getExtensionPoint(pluginId + ".propagation");
			if (point != null) {
				for (IExtension extension : point.getExtensions()) {
					for (IConfigurationElement configElement : extension.getConfigurationElements()) {
						try {
							AbstractPropagation propagationProvider = (AbstractPropagation)configElement.createExecutableExtension("class");
							list.addAll(propagationProvider.getPropagations(ci));
						} catch (CoreException e) {
							Activator.getDefault().getLog().log(new Status(Status.ERROR, pluginId, e.getMessage(), e));
						}
					}
				}
			}
		}



		propagationCache.put(ci, list);

		for (ComponentInstance subci : ci.getComponentInstances())
		{
			getPropagationsFirstLevel (subci);
		}
		return list;
	}

}
