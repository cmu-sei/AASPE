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

package edu.cmu.sei.aaspe.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.ComponentUI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.osate.aadl2.ConnectionKind;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.EndToEndFlowInstance;
import org.osate.aadl2.instance.util.InstanceSwitch;
import org.osate.aadl2.modelsupport.errorreporting.AnalysisErrorReporterManager;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;
import org.osate.aadl2.util.OsateDebug;
import org.osate.xtext.aadl2.properties.util.GetProperties;

import com.sun.javafx.collections.MappingChange.Map;

import edu.cmu.sei.aaspe.utils.ComponentUtils;
import edu.cmu.sei.aaspe.utils.PropertyUtils;

public class AttackSurface extends AadlProcessingSwitchWithProgress {

	private HashMap<ComponentInstance,List<ComponentInstance>> processorsComponents;
	
	public AttackSurface(final IProgressMonitor monitor) {
		super(monitor, PROCESS_PRE_ORDER_ALL);
		processorsComponents = new HashMap<ComponentInstance,List<ComponentInstance>>();
	}

	public AttackSurface(final IProgressMonitor monitor, AnalysisErrorReporterManager errmgr) {
		super(monitor, PROCESS_PRE_ORDER_ALL, errmgr);
		processorsComponents = new HashMap<ComponentInstance,List<ComponentInstance>>();
	}
	
	public void addComponentToProcessor (ComponentInstance component, ComponentInstance processor)
	{
		if (processorsComponents.get(processor) == null)
		{
			processorsComponents.put(processor, new ArrayList<ComponentInstance> ());
		}
		processorsComponents.get(processor).add(component);	
	}

	protected final void initSwitches() {

		// We want to count instance model objects.
		instanceSwitch = new InstanceSwitch<String>() {
			public String caseComponentInstance(ComponentInstance obj) {
				long exposure;

				switch (obj.getCategory()) {

				case THREAD:
					return DONE;

				case PROCESS:
					/**
					 * For each process, we add the process in a list that map
					 * the processor with all the process/device they control.
					 * It might then help to identify all the resources associated
					 * with a processors and see process/device executing on the same
					 * processor with different security levels.
					 */
					for (ComponentInstance cpu : GetProperties.getActualProcessorBinding(obj))
					{
						addComponentToProcessor (obj, cpu);
					}
					return DONE;

				case PROCESSOR:
					exposure = PropertyUtils.getExposure(obj);
					
					OsateDebug.osateDebug("AttackSurface", "processor exposure=" + exposure);
					return DONE;

				case VIRTUAL_PROCESSOR:
					exposure = PropertyUtils.getExposure(obj);

					OsateDebug.osateDebug("AttackSurface", "virtual processor exposure=" + exposure);
					return DONE;

				case MEMORY:
					exposure = PropertyUtils.getExposure(obj);
					OsateDebug.osateDebug("AttackSurface", "device exposure=" + exposure);
					return DONE;

				case BUS:
					exposure = PropertyUtils.getExposure(obj);

					OsateDebug.osateDebug("AttackSurface", "bus exposure=" + exposure);
					return DONE;

				case VIRTUAL_BUS:
					exposure = PropertyUtils.getExposure(obj);

					OsateDebug.osateDebug("AttackSurface", "virtual bus exposure=" + exposure);
					return DONE;

				case DEVICE:
					exposure = PropertyUtils.getExposure(obj);
					OsateDebug.osateDebug("AttackSurface", "device exposure=" + exposure);
					return DONE;
				}
				return DONE;
			}

			public String caseConnectionInstance(ConnectionInstance ci) {
				if (ci.getKind() != org.osate.aadl2.instance.ConnectionKind.PORT_CONNECTION)
				{
					return DONE;
				}
				
				OsateDebug.osateDebug("AttackSurface", "Will analyze connection " + ci.getName() + " ...");
				if (ComponentUtils.isEncrypted(ci))
				{
					OsateDebug.osateDebug("AttackSurface", "connection " + ci.getName() + " IS encrypted");
				}
				else
				{
					OsateDebug.osateDebug("AttackSurface", "connection " + ci.getName() + " is *** NOT ***  encrypted");
				}
				return DONE;
			}

			public String caseEndToEndFlowInstance(EndToEndFlowInstance ci) {
				return DONE;
			}
		};

	}

}
