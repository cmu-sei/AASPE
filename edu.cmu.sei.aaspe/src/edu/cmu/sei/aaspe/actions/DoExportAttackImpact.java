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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osate.aadl2.Element;
import org.osate.aadl2.SystemImplementation;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.instantiation.InstantiateModel;
import org.osate.aadl2.util.OsateDebug;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;
import org.osate.ui.dialogs.Dialog;
import org.osgi.framework.Bundle;

import edu.cmu.attackimpact.Model;
import edu.cmu.sei.aaspe.utils.SiriusUtil;
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
		SystemInstance si = null;
		
		if (obj instanceof InstanceObject) {
			si = ((InstanceObject) obj).getSystemInstance();
		}
		
		if (obj instanceof SystemImplementation)
		{
			try
			{
				si = InstantiateModel.buildInstanceModelFile ((SystemImplementation) obj);
			}
			catch (Exception e)
			{
				si = null;
			}
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
			
			URI newURI = EcoreUtil.getURI(si).trimFragment().trimSegments(2).appendSegment("attackimpact")
					.appendSegment("ai" + ".attackimpact");
			final IProject currentProject = ResourceUtil.getFile(si.eResource()).getProject();
			final URI modelURI = serializeAttackImpactModel(attackImpactModel, newURI, currentProject);
			autoOpenAttackImpactModel(modelURI, currentProject);
//			createAndOpenAttackImpact(currentProject, modelURI, monitor);
			long endTime = System.currentTimeMillis();
			OsateDebug.osateDebug("Export Attack Impact - finished in " + ((endTime - startTime) / 1000) + " s" );


		} else {
			Dialog.showError("System instance selection", "You must select a system instance or system implementation to continue");
		}

		monitor.done();

	}
	
	public void autoOpenAttackImpactModel(final URI newURI, final IProject activeProject) {

		try {

			Job attackImpactCreationJob = new Job("Creation of Attack Impact Graph") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {

					monitor.beginTask("Creation of Attack Impact Graph", 100);

					createAndOpenAttackImpact (activeProject, newURI, monitor);
					try {
						activeProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					} catch (CoreException e) {
						// Error while refreshing the project
					}
					monitor.done();

					return Status.OK_STATUS;
				}
			};
			attackImpactCreationJob.setUser(true);
			attackImpactCreationJob.schedule();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


	private void createAndOpenAttackImpact(final IProject project, final URI attackImpactURI, IProgressMonitor monitor) {
		SiriusUtil util = SiriusUtil.INSTANCE;
		URI attackImpactViewpointURI = URI.createURI("viewpoint:/attackimpact.design/AttackImpact");

		URI semanticResourceURI = URI.createPlatformResourceURI(attackImpactURI.toPlatformString(true), true);
		Session existingSession = util.getSessionForProjectAndResource(project, semanticResourceURI, monitor);
		if (existingSession == null) {
			// give it a second try. null was returned the first time due to a class cast exception at the end of
			// setting the Modeling perspective.
			existingSession = util.getSessionForProjectAndResource(project, semanticResourceURI, monitor);
		}
		if (existingSession != null) {
			util.saveSession(existingSession, monitor);
			ResourceSetImpl resset = new ResourceSetImpl();
			Model model = getAttackImpactModelFromSession(existingSession, semanticResourceURI);
			// XXX this next piece of code tries to compensate for a bug in Sirius where it cannot find the Model
			// It should be there since the getSessionForProjectandResource would have put it there.
			if (model == null) {
				OsateDebug.osateDebug(
						"Could not find semantic resource Attack Impact in session for URI " + semanticResourceURI.path());
				EObject res = resset.getEObject(attackImpactURI, true);
				if (res instanceof Model) {
					model = (Model) res;
				}
			}
			if (model == null) {
				OsateDebug.osateDebug("Could not find Attack Impact for URI " + attackImpactURI.path());
				return;
			}
			final Viewpoint attackImpactVP = util.getViewpointFromRegistry(attackImpactViewpointURI);
			final RepresentationDescription description = util.getRepresentationDescription(attackImpactVP, "AttackImpactDiagram");
			String representationName = model.getName() + " Graph";
			final DRepresentation rep = util.findRepresentation(existingSession, attackImpactVP, description,
					representationName);
			if (rep != null) {
				DialectUIManager.INSTANCE.openEditor(existingSession, rep, new NullProgressMonitor());
			} else {
				try {
					util.createAndOpenRepresentation(existingSession, attackImpactVP, description, representationName, model,
							monitor);
				} catch (Exception e) {
					OsateDebug.osateDebug("Could not create and open Attack Impact Model " + model.getName());
					return;
				}
			}

		}
	}


	private Model getAttackImpactModelFromSession(Session session, URI uri) {
		Resource resource = SiriusUtil.INSTANCE.getResourceFromSession(session, uri);
		if (resource != null) {
			for (EObject object : resource.getContents()) {
				if (object instanceof Model) {
					return (Model) object;
				}
			}
		}
		return null;
	}

	
	private static URI serializeAttackImpactModel(Model attackImpactModel,  final URI newURI, IProject activeProject) {

		try {

			ResourceSet set = new ResourceSetImpl();
			Resource res = set.createResource(newURI);

			res.getContents().add(attackImpactModel);

//			FileOutputStream fos = new FileOutputStream(newFile.getRawLocation().toFile());
			res.save(null); 
			OsateDebug.osateDebug("[AttackImpactModel]", "activeproject=" + activeProject.getName());
			activeProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			return EcoreUtil.getURI(attackImpactModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newURI;

	}
}
