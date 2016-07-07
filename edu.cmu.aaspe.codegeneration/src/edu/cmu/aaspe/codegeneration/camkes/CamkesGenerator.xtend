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
 
 package edu.cmu.aaspe.codegeneration.camkes

import org.osate.aadl2.SystemImplementation
import java.util.ArrayList
import java.util.List
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.osate.aadl2.ProcessSubcomponent
import edu.cmu.aaspe.codegeneration.Utils
import org.osate.aadl2.ThreadSubcomponent
import org.osate.aadl2.ComponentImplementation
import java.io.File
import org.osate.aadl2.DataPort
import org.osate.xtext.aadl2.properties.util.GetProperties
import org.osate.aadl2.ThreadImplementation
import org.osate.aadl2.SubprogramCallSequence
import org.osate.aadl2.SubprogramType
import java.util.Map
import java.util.HashMap
import org.osate.ui.dialogs.Dialog
import org.osate.aadl2.ConnectedElement

import org.osate.aadl2.Feature
import org.osate.aadl2.Port
import org.osate.aadl2.DirectionType
import org.osate.aadl2.Classifier
import org.osate.aadl2.EnumerationLiteral
import org.osate.aadl2.instance.SystemInstance
import org.osate.aadl2.ComponentCategory
import org.osate.aadl2.instance.ComponentInstance
import org.eclipse.uml2.uml.NamedElement
import org.osate.aadl2.instance.FeatureCategory
import org.osate.aadl2.instance.FeatureInstance
import org.osate.aadl2.instance.ConnectionInstanceEnd
import org.eclipse.xtext.parser.packrat.tokens.AssignmentToken.End
import org.osate.aadl2.DataImplementation
import org.osate.aadl2.DataSubcomponent
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.isPlatformx86
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.isPlatformKzm
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.isPlatformTegraK1
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.isPlatformBeagleBone
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getUserProcessSubcomponents
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getSchedulerProcessSubcomponent
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getPeriod
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.isPeriodic
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.isSporadic
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getPartitionPeriodInMillisecond
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getPartitionBudgetInMillisecond
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getBoundProcessor

import org.osate.aadl2.EventDataPort
import org.osate.aadl2.instance.ConnectionInstance
import org.osate.aadl2.DeviceImplementation

class CamkesGenerator implements IGenerator {
	def static boolean shouldBeGenerated (FeatureInstance feature)
	{
		if (feature.direction == DirectionType.OUT)
		{
			for (ci : feature.srcConnectionInstances)
			{
				if ( (ci.destination.containingComponentInstance.category == ComponentCategory.THREAD) ||
					(ci.destination.containingComponentInstance.category == ComponentCategory.DEVICE))
				{
					return true
				}
			}
		}
		
		if (feature.direction == DirectionType.IN)
		{
			for (ci : feature.dstConnectionInstances)
			{
				if ( (ci.source.containingComponentInstance.category == ComponentCategory.THREAD) || (ci.source.containingComponentInstance.category == ComponentCategory.DEVICE))
				{
					return true
				}
			}
		}
		return false
	}
	
	def static String generateTypeDefinition (Classifier classifier, List<String> alreadyGenerated)
	{
		val typeName = classifier.name.toCamkesName.toLowerCase
		
		if(alreadyGenerated.contains (typeName))
		{
			return ""
		}
		
		alreadyGenerated.add (typeName)
		
		val StringBuffer result = new StringBuffer()
		
//		println ("generate type definition for" + classifier.name)
		
		val sourceName = GetProperties.getSourceName(classifier)
		
		if (sourceName != null)
		{
			result.append('''typedef « sourceName» «classifier.name.toCamkesName.toLowerCase»;''')
			result.append ("\n\n")
		}
		
		val EnumerationLiteral dataRepresentation = GetProperties.getDataRepresentation (classifier)
		val List<String> enumerators = GetProperties.getDataEnumerators (classifier)
		if ((dataRepresentation != null) && (dataRepresentation.name.equalsIgnoreCase("enum")))
		{
			var n = 0
			
			result.append(
			'''
			typedef enum {
				«FOR enumerator : enumerators»
				« IF (n > 0)»,«ENDIF»«typeName»_«enumerator» = «n++»
				«ENDFOR»
			}«typeName»;
			''')
			result.append ("\n\n");
			return result.toString
		}
		
		if(classifier.name.equalsIgnoreCase("unsigned_8"))
		{
			result.append ('''typedef uint8_t «classifier.name.toCamkesName.toLowerCase»;''')
			result.append ("\n\n");
		}
		
		if(classifier.name.equalsIgnoreCase("boolean"))
		{
			result.append ('''typedef int «classifier.name.toCamkesName.toLowerCase»;''')
			result.append ("\n\n");
		}
		
		if (classifier instanceof DataImplementation)
		{
			val DataImplementation implementation = classifier as DataImplementation
			
			implementation.allSubcomponents.filter(DataSubcomponent).forEach[ ds | result.append(generateTypeDefinition(ds.classifier, alreadyGenerated))]
			
			result.append(
			'''
			typedef struct {
				«FOR subco : implementation.allSubcomponents.filter(DataSubcomponent)»
				«subco.classifier.name.toCamkesName.toLowerCase» «subco.name»;
				«ENDFOR»
			}«typeName»;
			''')
			result.append ("\n\n");
		}
		
		return result.toString
		
	}
	
	def static String getCamkesApplicationName (SystemInstance si)
	{
		return si.name.toCamkesName
	}
	
	
	def static String generateTimerComponent (SystemInstance si)
	{
'''
«IF (si.isPlatformBeagleBone || si.isPlatformKzm || si.isPlatformTegraK1)»
component Timerbase{
  hardware;
  dataport Buf reg;
  emits DataAvailable irq;
}
« ENDIF»

«IF (si.isPlatformx86)»
component Timerbase {
   hardware;
   provides IOPort command;
   provides IOPort channel0;
   emits PITIRQ irq;
}
« ENDIF»


component Timer {
«IF (si.isPlatformBeagleBone || si.isPlatformKzm || si.isPlatformTegraK1)»
  dataport Buf            reg;
  consumes DataAvailable  irq;
  has mutex				  timer;
«ENDIF»

«IF (si.isPlatformx86)»
  uses IOPort				pit_command;
  uses IOPort				pit_channel0;
  has mutex					timer;
  consumes DataAvailable 	irq;
«ENDIF»


««« We generate an activator signal for each thread that is periodic.

		«FOR subComponent : si.componentInstances.filter[category == ComponentCategory.PROCESS]»
			«FOR thread : subComponent.componentInstances.filter[category == ComponentCategory.THREAD]»
			«IF thread.isPeriodic»
  emits sig «subComponent.name.toCamkesName»_«thread.name.toCamkesName»_activator;
			«ENDIF»
			«ENDFOR»
		«ENDFOR»
		
		«FOR device : si.componentInstances.filter[category == ComponentCategory.DEVICE]»
			«IF device.isPeriodic»
  emits sig «device.name.toCamkesName»_activator;
			«ENDIF»
		«ENDFOR»		
}
'''
	}
	
	def static String generateTimerCode (SystemInstance si)
	{
'''
#include <stdio.h>
«IF (si.isPlatformKzm)»
#include <platsupport/mach/epit.h>
#include <platsupport/timer.h>
« ENDIF»

«IF (si.isPlatformBeagleBone || si.isPlatformTegraK1 )»
#include <platsupport/timer.h>
#include <platsupport/plat/timer.h>
#include <sel4platsupport/plat/timer.h>
#include <sel4platsupport/timer.h>
#include <sel4utils/util.h>
« ENDIF»

«IF (si.isPlatformx86)»
#include <stdio.h>

#include <assert.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdio.h>
#include <sel4/sel4.h>
#include <sel4/arch/constants.h>
#include <platsupport/plat/pit.h>
#include <platsupport/arch/tsc.h>
#include <utils/math.h>
#include <platsupport/plat/pit.h>
#include <platsupport/timer.h>
#include <sel4platsupport/timer.h>

#include <Timer.h>
« ENDIF»

#include <Timer.h>



pstimer_t *timer_drv = NULL;

«IF (si.isPlatformKzm || si.isPlatformBeagleBone || si.isPlatformTegraK1)»

volatile static uint64_t ticks = 0;
uint64_t period = 100000000;
« ENDIF »


«IF (si.isPlatformx86)»
static uint64_t tsc_frequency = 0;

static int pit_port_in(void *cookie, uint32_t port, int io_size, uint32_t *result) {
   seL4_IA32_IOPort_In8_t reply;
   if (io_size != 1) {
      return -1;
   }

   switch(port) {
      case 0x43:
         *result = pit_command_in8(port);
         return 0;
      case 0x40:
         *result = pit_channel0_in8(port);
         return 0;
      default:
         return -1;
   }
}

static int pit_port_out(void *cookie, uint32_t port, int io_size, uint32_t val) {
   if (io_size != 1) {
      return -1;
   }

   switch(port) {
      case 0x43:
         pit_command_out8(port, val);
         return 0;
      case 0x40:
         pit_channel0_out8(port, val);
         return 0;
      default:
         return -1;
   }
}


static uint64_t current_time_ns() {
       return muldivu64(rdtsc_pure(), NS_IN_S, tsc_frequency);
}

static uint64_t current_time_ms() {
       return muldivu64(rdtsc_pure(), 1000, tsc_frequency);
}

« ENDIF »

void irq_callback(void *_ UNUSED)
{
«IF (si.isPlatformKzm)»
    timer_handle_irq(timer_drv, EPIT2_INTERRUPT);
« ENDIF »

«IF (si.isPlatformBeagleBone)»
    timer_handle_irq(timer_drv, DMTIMER2_INTERRUPT);
« ENDIF »

«IF (si.isPlatformTegraK1)»
    timer_handle_irq(timer_drv, INT_NV_TMR1);
« ENDIF »

«IF (si.isPlatformKzm || si.isPlatformBeagleBone || si.isPlatformTegraK1)»
	ticks++;
« ENDIF »

«IF (si.isPlatformx86)»
    timer_handle_irq(timer_drv, 0); 
« ENDIF »

«IF (si.isPlatformBeagleBone || si.isPlatformKzm || si.isPlatformTegraK1)»
		«FOR subComponent : si.componentInstances.filter[category == ComponentCategory.PROCESS]»
			«FOR thread : subComponent.componentInstances.filter[category == ComponentCategory.THREAD]»
			« IF thread.isPeriodic»
			if ( ( ticks % « Math.round(GetProperties.getPeriodinMS(thread)/100)») == 0)
			{
				«subComponent.name.toCamkesName»_«thread.name.toCamkesName»_activator_emit();
			}
			« ENDIF »
			«ENDFOR»
		«ENDFOR»    
«ENDIF»

«IF (si.isPlatformx86)»
		uint64_t ms = current_time_ms();

		«FOR subComponent : si.componentInstances.filter[category == ComponentCategory.PROCESS]»
			«FOR thread : subComponent.componentInstances.filter[category == ComponentCategory.THREAD]»
			« IF thread.isPeriodic»
			if ( ( ms % « Math.round(GetProperties.getPeriodinMS(thread))») == 0)
			{
				«subComponent.name.toCamkesName»_«thread.name.toCamkesName»_activator_emit();
			}
			« ENDIF »
			«ENDFOR»
		«ENDFOR»    
«ENDIF»

    irq_reg_callback(irq_callback, NULL);
   }



«IF (si.isPlatformBeagleBone || si.isPlatformKzm || si.isPlatformTegraK1 )»
void post_init (void)
{
«IF (si.isPlatformKzm)»
    epit_config_t config;
    
    config.vaddr = (void*)reg;
    config.irq = EPIT2_INTERRUPT;
    config.prescaler = 0;
    
    timer_drv = epit_get_timer(&config);
    assert(timer_drv);
«ENDIF»

«IF (si.isPlatformTegraK1)»
	nv_tmr_config_t config;

	config.vaddr = (void*)reg + TMR1_OFFSET;
	config.tmrus_vaddr = (void*) reg + TMRUS_OFFSET;
	config.shared_vaddr = (void*) reg + TMR_SHARED_OFFSET;
	config.irq = INT_NV_TMR1;

	timer_drv = tk1_get_timer (&config);

    assert(timer_drv);
«ENDIF»

«IF (si.isPlatformBeagleBone)»
    timer_config_t config;
    
    config.vaddr = (void*)reg;
    config.irq = DMTIMER2_INTERRUPT;
    
    timer_drv = ps_get_timer(DMTIMER2, &config);
    assert(timer_drv);
«ENDIF»
    irq_reg_callback(irq_callback, NULL);

    timer_periodic(timer_drv, period);
	timer_start (timer_drv);
}
«ENDIF»

«IF (si.isPlatformx86)»

#define TIMER_FREQUENCY 500


void post_init ()
{
   timer_lock();
   ps_io_port_ops_t ops;
   ops.io_port_in_fn = pit_port_in;
   ops.io_port_out_fn = pit_port_out;

   timer_drv = pit_get_timer(&ops);

   tsc_frequency = tsc_calculate_frequency(timer_drv);

   irq_reg_callback(irq_callback, NULL);


   timer_start (timer_drv);
   timer_periodic (timer_drv, (NS_IN_S / 10) /TIMER_FREQUENCY);

//   timer_oneshot_relative(timer_drv, period);

   timer_unlock();
}
«ENDIF»
'''
	}
	
	def static List<String> getUsedComponentsType (SystemInstance instance)
	{
		val res = new ArrayList<String>()
		
		instance.componentInstances.filter[category == ComponentCategory.PROCESS].forEach
		[ process |
			process.componentInstances.filter[category == ComponentCategory.THREAD].forEach
			[ thr |
				val s = thr.componentClassifier.name.toCamkesName
				if (! res.contains (s))
				{
					res.add(s)
				}
			]
		]
		
		instance.componentInstances.filter[category == ComponentCategory.DEVICE].forEach
		[ device |
			val s = device.componentClassifier.name.toCamkesName
			if (! res.contains (s))
			{
				res.add(s)
			}
		]
		
		
		return res
	}
	
	def static String toCamkesName (String source)
	{
		var String dest = source
		
		dest = dest.substring(0,1).toUpperCase + dest.substring(1,dest.length())
		
		while (dest.indexOf('.') > 0)
		{
			val idx = dest.indexOf('.')
			dest = dest.substring(0,idx) + dest.substring(idx + 1, idx+2).toUpperCase + dest.substring(idx + 2 , dest.length)
		}
		
		while (dest.indexOf('_') > 0)
		{
			val idx = dest.indexOf('_')
			dest = dest.substring(0,idx) + dest.substring(idx + 1, idx+2).toUpperCase + dest.substring(idx + 2 , dest.length)
		}
		return dest
	}
	
	/**
	 * Generates the header C file with the types
	 */
	def static String generateAssemblyTypes (SystemInstance instance)
	{
		val List<String> alreadyGenerated = new ArrayList<String>()
'''
#ifndef __ASSEMBLY_GENERATED_TYPES_H__
#define __ASSEMBLY_GENERATED_TYPES_H__

#include <stdint.h>

««« Generate types for all data and event data ports.
«FOR subComponent : instance.componentInstances.filter[category == ComponentCategory.PROCESS]»
	«FOR thread : subComponent.componentInstances.filter[category == ComponentCategory.THREAD]»
		«FOR dataPort : thread.featureInstances.filter[category == FeatureCategory.DATA_PORT]»
			«(dataPort.feature as DataPort).classifier.generateTypeDefinition(alreadyGenerated)»
«««			typedef «GetProperties.getSourceName(dataPort.classifier)» «dataPort.classifier.name.toCamkesName.toLowerCase»;
		«ENDFOR»
		«FOR dataPort : thread.featureInstances.filter[category == FeatureCategory.EVENT_DATA_PORT]»
			«(dataPort.feature as EventDataPort).classifier.generateTypeDefinition(alreadyGenerated)»
«««			typedef «GetProperties.getSourceName(dataPort.classifier)» «dataPort.classifier.name.toCamkesName.toLowerCase»;
		«ENDFOR»	
	«ENDFOR»
«ENDFOR»
#endif
'''
	}

	
	def static String toAssemblyConnectionEndString (ConnectionInstanceEnd connectionInstanceEnd)
	{
//		val StringBuffer sb = new StringBuffer();

		if (connectionInstanceEnd.containingComponentInstance.category == ComponentCategory.DEVICE)
		{
			return connectionInstanceEnd.containingComponentInstance.name.toCamkesName + "." + connectionInstanceEnd.name.toLowerCase
		}

		if (connectionInstanceEnd.containingComponentInstance.category == ComponentCategory.THREAD)
		{
		
			val threadComponent = connectionInstanceEnd.containingComponentInstance
			val processComponent = threadComponent.containingComponentInstance
			
			if ((threadComponent == null) || (threadComponent.category != ComponentCategory.THREAD))
			{
				return "unknownThread"
			}
			
			if ((processComponent == null) || (processComponent.category != ComponentCategory.PROCESS))
			{
				return "unknownProcess"
			}		
			return processComponent.name.toCamkesName + "_" + threadComponent.name.toCamkesName + "." + connectionInstanceEnd.name.toLowerCase	
		}
		
		
	}
	
	def static String generateAssembly (SystemInstance element)
	{
		var int connId = 0;
		var int connIdConfiguration = 0;
		var int notificationId = 0;
		'''
import <std_connector.camkes>;
import "components/timer/Timer.camkes";
«FOR type : element.usedComponentsType»
import "components/«type»/«type».camkes";
«ENDFOR»
assembly {
  composition {
    component Timerbase timerbase;
    component Timer timer;
		
		«FOR subComponent : element.componentInstances.filter[category == ComponentCategory.PROCESS]»
			«FOR thread : subComponent.componentInstances.filter[category == ComponentCategory.THREAD]»
		component «thread.componentClassifier.name.toCamkesName» «subComponent.name.toCamkesName»_«thread.name.toCamkesName»;
			«ENDFOR»
		«ENDFOR»
		
		«FOR device : element.componentInstances.filter[category == ComponentCategory.DEVICE]»
    component «device.componentClassifier.name.toCamkesName» «device.name.toCamkesName»;
    component «device.componentClassifier.name.toCamkesName»_hw «device.name.toCamkesName»_hw;
		«ENDFOR»
		
		
«««		We connect the data of data port and event data port
		«FOR connection : element.connectionInstances.filter[((destination as FeatureInstance).category == FeatureCategory.DATA_PORT) && 
			                                                  ( (destination.containingComponentInstance.category == ComponentCategory.THREAD) || (destination.containingComponentInstance.category == ComponentCategory.DEVICE) )&& 
			                                                  ( (source.containingComponentInstance.category == ComponentCategory.THREAD) || (source.containingComponentInstance.category == ComponentCategory.DEVICE))
		]»
    connection seL4SharedData connection«connId++»(from «connection.source.toAssemblyConnectionEndString», to «connection.destination.toAssemblyConnectionEndString»);
		«ENDFOR»
		
		
«««		We connect devices with processors
		«FOR device : element.componentInstances.filter[category == ComponentCategory.DEVICE]»
			«FOR runtimePort : device.boundProcessor.featureInstances.filter[category == FeatureCategory.DATA_PORT]»
		    connection seL4HardwareMMIO connection«connId++»(from «device.name.toCamkesName».«runtimePort.name.toLowerCase», to «device.name.toCamkesName»_hw.«runtimePort.name.toLowerCase»);
		    «ENDFOR»
		«ENDFOR»
		
«««     We connect the events of the event data ports connections
		«FOR connection : element.connectionInstances.filter[((destination as FeatureInstance).category == FeatureCategory.EVENT_DATA_PORT) && (destination.containingComponentInstance.category == ComponentCategory.THREAD) && (source.containingComponentInstance.category == ComponentCategory.THREAD)]»
    connection seL4SharedData connection«connId++»(from «connection.source.toAssemblyConnectionEndString», to «connection.destination.toAssemblyConnectionEndString»);    
    connection seL4Notification connection«connId++»(from «connection.source.toAssemblyConnectionEndString»_event_signal, to «connection.destination.toAssemblyConnectionEndString»_event_signal);
		«ENDFOR»
		
		
		«FOR subComponent : element.componentInstances.filter[category == ComponentCategory.PROCESS]»
			«FOR thread : subComponent.componentInstances.filter[(category == ComponentCategory.THREAD) && isPeriodic]»
    connection seL4Notification notification«notificationId++» (from timer.«subComponent.name.toCamkesName»_«thread.name.toCamkesName»_activator, to «subComponent.name.toCamkesName»_«thread.name.toCamkesName».activator);
			«ENDFOR»
		«ENDFOR»
		«FOR device : element.componentInstances.filter[category == ComponentCategory.DEVICE]»
		    connection seL4Notification notification«notificationId++» (from timer.«device.name.toCamkesName»_activator, to «device.name.toCamkesName».activator);
		«ENDFOR»
		

« IF (element.isPlatformKzm || element.isPlatformBeagleBone || element.isPlatformTegraK1 )»
        connection seL4HardwareMMIO timer_mem (from timer.reg, to timerbase.reg);
« ENDIF »
        connection seL4HardwareInterrupt timer_irq (from timerbase.irq, to timer.irq);
« IF (element.isPlatformx86)»
		connection seL4HardwareIOPort pit_command(from timer.pit_command, to timerbase.command);
		connection seL4HardwareIOPort pit_channel0(from timer.pit_channel0, to timerbase.channel0);
« ENDIF»

«««		connection seL4SharedData channel1(from pi.outport, to po.inport);
	}
	
	configuration {
		
«««		We configure the memory area of devices
		«FOR device : element.componentInstances.filter[category == ComponentCategory.DEVICE]»
		   «FOR runtimePort : device.boundProcessor.featureInstances.filter[category == FeatureCategory.DATA_PORT]»
		   «device.name.toCamkesName»_hw.«runtimePort.name.toLowerCase»_attributes = "0x«GetProperties.getBaseAddress(runtimePort.dstConnectionInstances.findFirst[true].source)»:0x1000";
			«ENDFOR»
		«ENDFOR»

««« Configure security aspects, sender
««« can write on sharer ports, receiver cannot
«FOR subComponent : element.componentInstances.filter[category == ComponentCategory.PROCESS]»
«FOR thread : subComponent.componentInstances.filter[category == ComponentCategory.THREAD]»
		«subComponent.name.toCamkesName»_«thread.name.toCamkesName»._period = «subComponent.getPartitionPeriodInMillisecond»;
		«subComponent.name.toCamkesName»_«thread.name.toCamkesName»._budget = «subComponent.getPartitionBudgetInMillisecond»;
«ENDFOR»		
«ENDFOR»

«FOR connection : element.connectionInstances.filter[((destination as FeatureInstance).category == FeatureCategory.DATA_PORT) && (destination.containingComponentInstance.category == ComponentCategory.THREAD) && (source.containingComponentInstance.category == ComponentCategory.THREAD)]»
		connection«connIdConfiguration».from_access = "W";
		connection«connIdConfiguration++».to_access = "R";
«ENDFOR»

«FOR connection : element.connectionInstances.filter[((destination as FeatureInstance).category == FeatureCategory.DATA_PORT) && (destination.containingComponentInstance.category == ComponentCategory.THREAD) && (source.containingComponentInstance.category == ComponentCategory.THREAD)]»
		connection«connIdConfiguration».from_access = "W";
		connection«connIdConfiguration++».to_access = "R";
«ENDFOR»		

««« Configure the runtime, access to devices and shared resources
« IF (element.isPlatformKzm)»
        timerbase.reg_attributes = "0x53F98000:0x1000";
        timerbase.irq_attributes = 27;
« ENDIF »
« IF (element.isPlatformx86)»
        timerbase.irq_attributes = 0;
        timerbase.command_attributes = "0x43:0x43";
      	timerbase.channel0_attributes = "0x40:0x40";
« ENDIF »

« IF (element.isPlatformBeagleBone)»
        timerbase.reg_attributes = "0x48040000:0x1000";
        timerbase.irq_attributes = 68;        
« ENDIF »
« IF (element.isPlatformTegraK1)»
        timerbase.reg_attributes = "0x60005000:0x1000";
        timerbase.irq_attributes = 32;        
« ENDIF »
    }
} 
'''
	}
	
	
	
	/**
	 * Generates the component definition in camkes
	 */
	
	def static String generateProcessDefinition (ComponentInstance component)
	{		
'''
component «component.componentClassifier.name.toCamkesName»
{
	include "generatedtypes.h";

	control;

««« For all data ports that should be generated, we add a buffer.
««« If we have an event data port, we will generate extra stuff
««« in order to activate the task.
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.DATA_PORT) && shouldBeGenerated] »
	dataport «(dataport.feature as DataPort).classifier.name.toCamkesName.toLowerCase» «dataport.name»;
«ENDFOR»
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.EVENT_DATA_PORT) && shouldBeGenerated] »
	dataport «(dataport.feature as EventDataPort).classifier.name.toCamkesName.toLowerCase» «dataport.name»;
«ENDFOR»

««« If we have a periodic task, we consume an activator event to activate the task
« IF component.isPeriodic»
	consumes sig activator;
« ENDIF »

««« If we have a sporadic task, we are generating either an activator or another
««« artifact to activate the task when a data is available.

« IF component.isSporadic»
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.EVENT_DATA_PORT)&& shouldBeGenerated] »
«IF dataport.direction == DirectionType.IN»
	consumes sig «dataport.name»_event_signal;
« ENDIF »
«IF dataport.direction == DirectionType.OUT»
	emits sig «dataport.name»_event_signal;
« ENDIF »
«ENDFOR»
«ENDIF »

« IF component.isPeriodic»
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.EVENT_DATA_PORT)&& shouldBeGenerated] »
«IF dataport.direction == DirectionType.OUT»
	emits sig «dataport.name»_event_signal;
« ENDIF »
«ENDFOR»
«ENDIF »

}
'''
	}
	
	def static String generateDeviceHardwareDefinition (ComponentInstance component)
	{		
'''
component «component.componentClassifier.name.toCamkesName»_hw
{

	hardware;
«FOR dataport : component.getBoundProcessor.featureInstances.filter[(category == FeatureCategory.DATA_PORT)] »
	dataport Buf «dataport.name»;
«ENDFOR»
}


'''		
	}
	
	def static String generateDeviceDriverDefinition (ComponentInstance component)
	{		
'''
component «component.componentClassifier.name.toCamkesName»
{
	include "generatedtypes.h";

	control;

«FOR dataport : component.getBoundProcessor.featureInstances.filter[(category == FeatureCategory.DATA_PORT)] »
	dataport Buf «dataport.name»;
«ENDFOR»

««« For all data ports that should be generated, we add a buffer.
««« If we have an event data port, we will generate extra stuff
««« in order to activate the task.
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.DATA_PORT) && shouldBeGenerated] »
	dataport «(dataport.feature as DataPort).classifier.name.toCamkesName.toLowerCase» «dataport.name»;
«ENDFOR»
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.EVENT_DATA_PORT) && shouldBeGenerated] »
	dataport «(dataport.feature as EventDataPort).classifier.name.toCamkesName.toLowerCase» «dataport.name»;
«ENDFOR»

««« If we have a periodic task, we consume an activator event to activate the task
« IF component.isPeriodic»
	consumes sig activator;
« ENDIF »

««« If we have a sporadic task, we are generating either an activator or another
««« artifact to activate the task when a data is available.

« IF component.isSporadic»
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.EVENT_DATA_PORT)&& shouldBeGenerated] »
«IF dataport.direction == DirectionType.IN»
	consumes sig «dataport.name»_event_signal;
« ENDIF »
«IF dataport.direction == DirectionType.OUT»
	emits sig «dataport.name»_event_signal;
« ENDIF »
«ENDFOR»
«ENDIF »

« IF component.isPeriodic»
«FOR dataport : component.featureInstances.filter[(category == FeatureCategory.EVENT_DATA_PORT)&& shouldBeGenerated] »
«IF dataport.direction == DirectionType.OUT»
	emits sig «dataport.name»_event_signal;
« ENDIF »
«ENDFOR»
«ENDIF »

}
'''		
	}
	
	
		def static String generateDeviceDefinition (ComponentInstance component)
	{		
	return component.generateDeviceHardwareDefinition + component.generateDeviceDriverDefinition
	}


	
	def static String generateAssemblyMakefile (SystemInstance systemInstance)
	{
		val Map<String,String> srcs = new HashMap<String,String>()
		
		systemInstance.componentInstances.filter[category == ComponentCategory.PROCESS].forEach
		[ pr |
			pr.componentInstances.filter[category == ComponentCategory.THREAD].forEach
			[
				thr |
				val StringBuffer sourceList = new StringBuffer()
				val ThreadImplementation threadImpl = (thr.componentClassifier as ThreadImplementation)
				
				sourceList.append ("components" + File.separatorChar + threadImpl.name.toCamkesName + File.separatorChar + "src" + File.separatorChar + threadImpl.name.toCamkesName + ".c " )
				
				
				thr.getUsedFiles.forEach[
					sourceList.append (" components" + File.separatorChar + threadImpl.name.toCamkesName + File.separatorChar + "src" + File.separatorChar + it )
					
				]
				srcs.put(thr.componentClassifier.name.toCamkesName.toLowerCase, sourceList.toString())
				
			]
		]
		
		systemInstance.componentInstances.filter[category == ComponentCategory.DEVICE].forEach
		[ device |
				val DeviceImplementation = device.componentClassifier
				val StringBuffer sourceList = new StringBuffer()
				
				sourceList.append ("components" + File.separatorChar + DeviceImplementation.name.toCamkesName + File.separatorChar + "src" + File.separatorChar + DeviceImplementation.name.toCamkesName + ".c " )
				
				
				device.getUsedFiles.forEach[
					sourceList.append (" components" + File.separatorChar + DeviceImplementation.name.toCamkesName + File.separatorChar + "src" + File.separatorChar + it )
					
				]
				srcs.put(device.componentClassifier.name.toCamkesName.toLowerCase, sourceList.toString())
				
			
		]

'''
TARGETS := «systemInstance.name.toCamkesName.toLowerCase».cdl
ADL := assembly.camkes

«FOR comp : systemInstance.usedComponentsType»
«comp»_CFILES = «srcs.get(comp.toLowerCase)»
«comp»_HFILES = components/«comp»/include/generatedtypes.h
«ENDFOR»
Timer_CFILES = components/timer/src/timer.c

include ${SOURCE_DIR}/../../tools/camkes/camkes.mk
'''
	}	
	
	def static String generateAssemblyKbuild (SystemInstance instance)
	{
'''
apps-$(CONFIG_APP_«instance.camkesApplicationName.toUpperCase») += «instance.camkesApplicationName.toLowerCase»
    «instance.camkesApplicationName.toLowerCase»: libsel4 libmuslc libsel4platsupport libsel4muslccamkes libsel4sync libsel4debug libsel4bench
'''
	}		
	
	def static String generateAssemblyKconfig (SystemInstance instance)
	{
'''
config APP_«instance.camkesApplicationName.toUpperCase»
    bool "Generated CAmkES «instance.camkesApplicationName» application"
    default n
        help
            Application generated from AADL
'''
	}		
	
	def static List<String> getCallSequenceCode (ComponentInstance instance)
	{
		val callseqs = new ArrayList<String>()
		
		if (instance.category == ComponentCategory.DEVICE)
		{
			val List<String> args = new ArrayList<String>()
			
			(instance.componentClassifier as DeviceImplementation).type.ownedFeatures.filter(DataPort).forEach[

				dataport |
				var arg = ""
				if (args.size() > 0)
				{
					arg += ","
				}
				
				if (dataport.direction == DirectionType.IN)
				{
					arg += "(*" + dataport.name.toLowerCase + ")";
				}
				if (dataport.direction == DirectionType.OUT)
				{
					arg += "(" + dataport.name.toLowerCase + ")";
				}
				args.add(arg)
			]
			
			var res = "driver(";
			for (String a  : args)
			{
				res += a;
			}
			res += ");"
			callseqs.add(res)
		}
		
		if (instance.category == ComponentCategory.THREAD)
		{
			val ThreadImplementation threadImplementation = instance.componentClassifier as ThreadImplementation
			
			threadImplementation.ownedSubprogramCallSequences.forEach [ callSeq|
				callSeq.ownedSubprogramCalls.forEach [ call |
					var subprogramName = GetProperties.getSourceName(call.calledSubprogram as SubprogramType)
					if (subprogramName == null)
					{
						subprogramName =(call.calledSubprogram as SubprogramType).name.toCamkesName.toLowerCase
					}
					
					val List<String> args = new ArrayList<String>()
					
					threadImplementation.ownedConnections.forEach[conn |
						var arg = "0"
						var boolean toAdd = false
						
						if (conn.destination.context == call)
						{
							arg = "(*"+ conn.source.connectionEnd.name.toLowerCase + ")"
							if (instance.featureInstances.filter[name.equalsIgnoreCase(conn.destination.connectionEnd.name)].get(0).shouldBeGenerated)
							{
								toAdd = true
							}
						}
						
						if (conn.source.context == call)
						{
							arg = "("+ conn.destination.connectionEnd.name.toLowerCase + ")"
							if (instance.featureInstances.filter[name.equalsIgnoreCase(conn.source.connectionEnd.name)].get(0).shouldBeGenerated)
							{
								toAdd = true
							}
						}
						if (args.size() > 0)
						{
							arg = "," + arg
						}
						if (toAdd)
						{
							args += arg
							
						}
					]
					val callString = '''«subprogramName» («FOR a : args»«a»«ENDFOR» );'''
					callseqs.add(callString)
				]
			]	
		}
		
		return callseqs
	}
	
	
	
//			«FOR connection : element.connectionInstances.filter[((destination as FeatureInstance).category == FeatureCategory.EVENT_DATA_PORT) && (destination.containingComponentInstance.category == ComponentCategory.THREAD) && (source.containingComponentInstance.category == ComponentCategory.THREAD)]»
//    connection seL4SharedData connection«connId++»(from «connection.source.toAssemblyConnectionEndString»_event_signal, to «connection.destination.toAssemblyConnectionEndString»_event_signal);
//		«ENDFOR»
//		
	
	def static String generateComponentSource (ComponentInstance instance)
	{
		val ti = instance.componentClassifier
'''
 #include <camkes.h>

#include <generatedtypes.h>
#include <stdio.h>
#include <string.h>

int run(void)
{
  while (1)
  {
«IF ti.isPeriodic»
    activator_wait();
«ENDIF»
«IF ti.isSporadic»
««««FOR feat : ti.getAllFeatures.filter(EventDataPort).findFirst[direction == DirectionType.IN]»
«««    «f.getName»_event_activator_wait();
«FOR feature : ti.getAllFeatures.filter(EventDataPort).filter[direction == DirectionType.IN]»
    «feature.getName»_event_signal_wait();
«ENDFOR»
«ENDIF»

«FOR seq : instance.callSequenceCode»
« seq »
«ENDFOR»




«FOR feature : ti.getAllFeatures.filter(EventDataPort).filter[direction == DirectionType.OUT]»
    «feature.getName»_event_signal_emit();
«ENDFOR»
   } 
   return 0;
}
'''
	}
	
	def static String generateComponentTypes (ComponentInstance instance)
	{
		val List<String> listofTypes = new ArrayList<String> ()
'''
#ifndef __COMPONENT_GENERATED_TYPES_H__
#define __COMPONENT_GENERATED_TYPES_H__

#include <stdint.h>
«FOR dataPort : instance.featureInstances.filter[category == FeatureCategory.DATA_PORT]»
«generateTypeDefinition ((dataPort.feature as DataPort).classifier, listofTypes)»
«ENDFOR»
#endif
'''
	}
	
	def static void generate (SystemInstance instance)
	{
		val appname = instance.camkesApplicationName.toLowerCase
		println ("Generate camkes application " + appname)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "assembly.camkes" , instance.generateAssembly)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "include" + "types.h" , instance.generateAssemblyTypes)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "Makefile" , instance.generateAssemblyMakefile)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "Kbuild" , instance.generateAssemblyKbuild)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "Kconfig" , instance.generateAssemblyKconfig)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "components" +  File.separator + "timer" +  File.separator + "Timer.camkes", instance.generateTimerComponent)
		Utils.write (instance, "camkes"+ File.separator + appname + File.separator + "components" +  File.separator + "timer" +  File.separator + "src" + File.separator + "timer.c", instance.generateTimerCode)		
		
		instance.componentInstances.filter[category == ComponentCategory.PROCESS].forEach[ pr |
			pr.componentInstances.filter[category == ComponentCategory.THREAD].forEach[ thr |
				val threadType = thr.componentClassifier
				
				Utils.write (instance, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + threadType.name.toCamkesName +  File.separator + threadType.name.toCamkesName + ".camkes" , thr.generateProcessDefinition)
				Utils.write (instance, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + threadType.name.toCamkesName +  File.separator + "src" + File.separator + threadType.name.toCamkesName + ".c" , thr.generateComponentSource)
				Utils.write (instance, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + threadType.name.toCamkesName +  File.separator + "include" + File.separator + "generatedtypes.h" , instance.generateAssemblyTypes)
				
				thr.getUsedFiles.forEach[
					Utils.copy (instance, it, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + threadType.name.toCamkesName +  File.separator + "src" + File.separator)
				]
			]
		]
		
		instance.componentInstances.filter[category == ComponentCategory.DEVICE].forEach[ device |	
			val deviceType = device.componentClassifier
			Utils.write (instance, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + deviceType.name.toCamkesName +  File.separator + deviceType.name.toCamkesName + ".camkes" , device.generateDeviceDefinition)
			Utils.write (instance, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + deviceType.name.toCamkesName +  File.separator + "src" + File.separator + deviceType.name.toCamkesName + ".c" , device.generateComponentSource)
			Utils.write (instance, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + deviceType.name.toCamkesName +  File.separator + "include" + File.separator + "generatedtypes.h" , instance.generateAssemblyTypes)
				
			device.getUsedFiles.forEach[
				Utils.copy (instance, it, "camkes" +  File.separator + appname + File.separator + "components" +  File.separator + deviceType.name.toCamkesName +  File.separator + "src" + File.separator)
			]
		]
	
		Utils.refreshWorkspace(null)
	}
	
	def static List<String> getUsedFiles (ComponentInstance component)
	{
		val List<String> res = new ArrayList<String>()
//		println("ti=" + ti.name)

		if (component.category == ComponentCategory.THREAD)
		{
			val ti = component.componentClassifier as ThreadImplementation
			ti.ownedSubprogramCallSequences.forEach
					[
	//					println(it)
						ownedSubprogramCalls.forEach[
	//						println(it)
							res.addAll(GetProperties.getSourceText((calledSubprogram as SubprogramType)))
						]
					]
		}
		
		if (component.category == ComponentCategory.DEVICE)
		{
			res.addAll (GetProperties.getSourceText(component));
		}
		return res
	}
	
	override doGenerate(Resource input, IFileSystemAccess fsa) {
//		println("Try to generate file")
		for (systemImplementation : input.allContents.toIterable.filter(typeof(SystemImplementation)))
		{
//			fsa.generateFile("camkes/" + systemImplementation.name, systemImplementation.generateAssembly)
		}
	}
	
}