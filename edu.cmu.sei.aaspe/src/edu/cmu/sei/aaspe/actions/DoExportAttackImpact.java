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

import java.io.FileOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.util.OsateDebug;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;
import org.osate.ui.dialogs.Dialog;
import org.osgi.framework.Bundle;

import edu.cmu.attackimpact.Model;
import edu.cmu.sei.aaspe.Activator;
import edu.cmu.sei.aaspe.export.AttackImpactModel;
import edu.cmu.sei.aaspe.logic.AttackImpact;
import edu.cmu.sei.aaspe.model.PropagationModel;
import edu.cmu.sei.aaspe.utils.ComponentUtils;
import edu.cmu.sei.aaspe.utils.Utils;


public final class DoExportAttackImpact extends AaxlReadOnlyActionAsJob {
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
		/*
		 * Doesn't make sense to set the number of work units, because the whole
		 * point of this action is count the number of elements. To set the work
		 * units we would effectively have to count everything twice.
		 */
		monitor.beginTask("Export to Attack Impact Model", IProgressMonitor.UNKNOWN);
		
			// Get the system instance (if any)
		SystemInstance si;
		if (obj instanceof InstanceObject) {
			si = ((InstanceObject) obj).getSystemInstance();
		} else {
			si = null;
		}

		if (si != null) {
			long startTime = System.currentTimeMillis();
			
			OsateDebug.osateDebug("Export Attack Impact - starting");
			PropagationModel.getInstance().reset();
			AttackImpact ai = new AttackImpact(si, monitor, getErrorManager());
			ai.defaultTraversal(si);
			AttackImpactModel export = new AttackImpactModel (ai); 
			Model attackImpactModel = export.getAttackImpactModel (true);
			IFile ifile = ResourceUtil.getFile(si.eResource());
			String filename = ifile.getFullPath().removeFirstSegments(1).toString();
			
//			String filename = ifile.getName();
			filename = filename.replace("aaxl2", "attackimpact");
//			OsateDebug.osateDebug("Filename=" + filename);
//			OsateDebug.osateDebug("s=" + s);
 
			monitor.subTask("Writing attack model file");
			serializeAttackImpactModel(attackImpactModel, ResourceUtil.getFile(si.eResource())
					.getProject(), filename);
			long endTime = System.currentTimeMillis();
			OsateDebug.osateDebug("Export Attack Impact - finished in " + ((endTime - startTime) / 1000) + " s" );


		} else {
			Dialog.showError("System instance selection", "You must select a system instance to continue");
		}

		monitor.done();

	}
	
	private static void serializeAttackImpactModel(Model attackImpactModel, IProject activeProject, String filename) {
		IFile newFile = activeProject.getFile(filename);

		try {

			ResourceSet set = new ResourceSetImpl();
			Resource res = set.createResource(URI.createURI(newFile.toString()));

			res.getContents().add(attackImpactModel);

			FileOutputStream fos = new FileOutputStream(newFile.getRawLocation().toFile());
			res.save(fos, null); 
			OsateDebug.osateDebug("[AttackImpactModel]", "activeproject=" + activeProject.getName());

			activeProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
