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
 package edu.cmu.aaspe.codegeneration

import org.osate.aadl2.ComponentCategory
import org.osate.aadl2.NamedElement
import org.osate.aadl2.ProcessSubcomponent
import org.osate.aadl2.SystemImplementation
import org.osate.aadl2.instance.SystemInstance
import org.osate.xtext.aadl2.properties.util.GetProperties
import org.osate.aadl2.instance.ComponentInstance
import org.osate.xtext.aadl2.properties.util.ARINC653ScheduleWindow

/**
 * This class contains all helpers methods to deal with the AADL components.
 * For helper methods about the management of Eclipse resources, look at the
 * Utils.xtend class.
 */
public class AadlHelper
{
	/**
	 * Get the period of an element (such as a thread or device).
	 * It retrieve the value of the period property in milliseconds.
	 */
	def static double getPeriod (NamedElement element)
	{
		return GetProperties.getPeriodinMS(element)
	}
	
	def static double getHyperPeriod (SystemImplementation systemImplementation)
	{ 
		return 0.0
	}

	/**
	 * returns all the process component which name is not scheduler.
	 */	
	def static getUserProcessSubcomponents (SystemImplementation systemImplementation)
	{	
		return systemImplementation.allSubcomponents.filter(ProcessSubcomponent).filter[prSub | prSub.name.contains("scheduler") == false]
	}
	
	/**
	 * Return the component whose name is scheduler inside the system implementation.
	 */
	def static getSchedulerProcessSubcomponent (SystemImplementation systemImplementation)
	{
		return systemImplementation.allSubcomponents.filter[sub | sub.name.equals("scheduler")].get(0)
	}
	
	
	/**
	 * returns true if the first processor of the system instance
	 * is a kzm platform
	 */
	def static isPlatformKzm (SystemInstance systemInstance)
	{
		return systemInstance.platform?.equalsIgnoreCase("kzm")
	}
	
	
	/**
	 * returns true if the processor in the system instance is a beaglebone
	 * platform.
	 */
	def static isPlatformBeagleBone (SystemInstance systemInstance)
	{
		return systemInstance.platform?.equalsIgnoreCase("beaglebone")
	}
	

	
	/**
	 * Is the platform an x86 platform?
	 * To do so, it takes the first processor component inside the system
	 */
	def static isPlatformx86 (SystemInstance systemInstance)
	{
		return (systemInstance.platform?.equalsIgnoreCase("x86") || systemInstance.platform?.equalsIgnoreCase("ia32"))
	}
	
	
	/**
	 * Is the platform an tegra k1 platform?
	 * To do so, it takes the first processor component inside the system
	 */
	def static isPlatformTegraK1 (SystemInstance systemInstance)
	{
		return systemInstance.platform?.equalsIgnoreCase("tegrak1")
	}
	
	/**
	 * returns the first processor in a system instance component.
	 */
	def static getPlatform (SystemInstance systemInstance)
	{
		val s = GetProperties.getPlatform(systemInstance.componentInstances.filter[category == ComponentCategory.PROCESSOR].get(0))
		return s
	} 
	
	/**
	 * returns true if the system instance can be generated.
	 * Check the following rules:
	 *   1. the system instance has one processor
	 *   2. the processor is annotated with a platform property to know what is the target
	 */
	 def static canBeGenerated (SystemInstance systemInstance)
	{
		return ( ( systemInstance.componentInstances.filter[category == ComponentCategory.PROCESSOR].size == 1) &&
		         ( (systemInstance.isPlatformBeagleBone) || (systemInstance.isPlatformKzm) || (systemInstance.isPlatformx86) || (systemInstance.isPlatformTegraK1)) &&
		         ( systemInstance.componentInstances.filter[category == ComponentCategory.PROCESSOR].forall[ cpu | GetProperties.getModuleSchedule(cpu).size > 0] )
		       );
	}
	
	
	def static isPeriodic (NamedElement ne)
	{
		val el = GetProperties.getDispatchProtocol(ne)
		return el?.name.equalsIgnoreCase("periodic")
	}
	
	def static isSporadic (NamedElement ne)
	{
		val el = GetProperties.getDispatchProtocol(ne)
		return el?.name.equalsIgnoreCase("sporadic")
	}
	
	def static getBoundProcessor (ComponentInstance component)
	{
		return component.systemInstance.allComponentInstances.findFirst[ c | GetProperties.getActualProcessorBinding(component).contains(c)]
	}
	
	
	/**
	 * The period corresponds to the major frame: at which period the schedule
	 * is repeated. We sum all the window slots of the schedule.
	 */
	def static getPartitionPeriodInMillisecond (ComponentInstance partition)
	{
		val runtime = partition.getBoundProcessor
		val processor = runtime.boundProcessor
		var Double total = 0.0
		
		for (ARINC653ScheduleWindow schedule : GetProperties.getModuleSchedule(processor))
		{
			total = total + schedule.time
		}
		return total 
	}
	
	
	/**
	 * This corresponds to the budget/period of the parttion. We just get the schedule of the
	 * current partition.
	 */
	def static getPartitionBudgetInMillisecond (ComponentInstance partition)
	{
		val runtime = partition.getBoundProcessor
		val processor = runtime.boundProcessor
		val schedule = GetProperties.getModuleSchedule(processor)?.findFirst[ schedule | schedule.partition == runtime]
		return schedule.time
	}
}