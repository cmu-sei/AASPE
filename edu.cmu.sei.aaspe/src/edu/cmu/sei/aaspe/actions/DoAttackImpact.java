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

package edu.cmu.sei.aaspe.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.util.OsateDebug;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;
import org.osate.ui.dialogs.Dialog;
import org.osgi.framework.Bundle;

import edu.cmu.sei.aaspe.Activator;
import edu.cmu.sei.aaspe.export.AttackImpactCsv;
import edu.cmu.sei.aaspe.export.AttackImpactExcel;
import edu.cmu.sei.aaspe.logic.AttackImpact;
import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.model.Vulnerability;
import edu.cmu.sei.aaspe.utils.ComponentUtils;

public final class DoAttackImpact extends AaxlReadOnlyActionAsJob {
	@Override
	protected Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}

	@Override
	protected String getActionName() {
		return "Generate Attack Impact";
	}
 
	@Override
	public void doAaxlAction(IProgressMonitor monitor, Element obj) {
		long startTime = System.currentTimeMillis();
		
		OsateDebug.osateDebug("Attack Impact - starting");
		
		PropagationModel.getInstance().reset();

		monitor.beginTask("Generate Attack Impact", IProgressMonitor.UNKNOWN);
		// Get the root object of the model
//		Element root = obj.getElementRoot();

		// Get the system instance (if any)
		SystemInstance si;
		if (obj instanceof InstanceObject) {
			si = ((InstanceObject) obj).getSystemInstance();
		} else {
			si = null;
		}

		if (si != null) {
			AttackImpact ai = new AttackImpact(si, monitor, getErrorManager());
			ai.defaultTraversal(si);
			OsateDebug.osateDebug("AttackImpact", "List of vulnerabilities");


//			for (Vulnerability v : ai.getVulnerabilities()) {
//				OsateDebug.osateDebug("AttackImpact", v.toString());
//			}
			long endTime = System.currentTimeMillis();

			monitor.subTask("Writing attack impact spreadsheet file");

			AttackImpactExcel report = new AttackImpactExcel (ai);
			report.export();
	AttackImpactCsv csv = new AttackImpactCsv(ai);
	csv.export();
			OsateDebug.osateDebug("Attack Impact - finished in " + ((endTime - startTime) / 1000) + " s" );
			
			monitor.done();
		} else {
			Dialog.showError("System instance selection", "You must select a system instance to continue");
		}

		

	}
}
