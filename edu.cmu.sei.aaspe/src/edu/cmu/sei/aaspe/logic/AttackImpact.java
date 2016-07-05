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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.EndToEndFlowInstance;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.instance.util.InstanceSwitch;
import org.osate.aadl2.modelsupport.errorreporting.AnalysisErrorReporterManager;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitchWithProgress;

import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public class AttackImpact extends AadlProcessingSwitchWithProgress {

	private List<Vulnerability> vulnerabilities;
	private SystemInstance systemInstance;
	private IProgressMonitor monitor;

	public AttackImpact(final SystemInstance si, final IProgressMonitor monitor, AnalysisErrorReporterManager errmgr) {
		super(monitor, PROCESS_PRE_ORDER_ALL, errmgr);
		this.monitor = monitor;
		vulnerabilities = new ArrayList<Vulnerability> ();
		this.systemInstance = si;
	}

	public List<Vulnerability> getVulnerabilities ()
	{
		return this.vulnerabilities;
	}

	public SystemInstance getSystemInstance ()
	{
		return this.systemInstance;
	}

	@Override
	protected final void initSwitches() {

		// We want to count instance model objects.
		instanceSwitch = new InstanceSwitch<String>() {
			@Override
			public String caseComponentInstance(ComponentInstance obj) {
				monitor.subTask("Analyzing component " + obj.getName());
				vulnerabilities.addAll(ComponentUtils.getAllVulnerabilities(obj));

				return DONE;
			}

			@Override
			public String caseConnectionInstance(ConnectionInstance ci) {
				if (ci.getKind() != org.osate.aadl2.instance.ConnectionKind.PORT_CONNECTION) {
					return DONE;
				}

				return DONE;
			}

			@Override
			public String caseEndToEndFlowInstance(EndToEndFlowInstance ci) {
				return DONE;
			}
		};

	}

}
