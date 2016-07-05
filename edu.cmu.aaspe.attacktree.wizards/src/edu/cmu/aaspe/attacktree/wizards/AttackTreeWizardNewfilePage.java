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

package edu.cmu.aaspe.attacktree.wizards;

import java.io.File;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Bundle;

public class AttackTreeWizardNewfilePage extends org.eclipse.ui.dialogs.WizardNewFileCreationPage {
	private Button useMedicalDevice;
	private Button useTelevision;

	public AttackTreeWizardNewfilePage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
	}

	public AttackTreeWizardNewfilePage(IWorkbench workbench, IStructuredSelection selection) {
		super("Attack Tree New File", selection);
		this.setMessage("Select the model you want to copy and the output directory");
		this.setDescription("Select the model you want to copy and the output directory");
		this.setTitle("Attack Tree Model example");

	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		// inherit default container and name specification widgets
		super.createControl(parent);
		Composite composite = (Composite) getControl();

		// sample section generation group
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout());
		group.setText("Attack Tree to add");
		group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		// sample section generation checkboxes
		useMedicalDevice = new Button(group, SWT.RADIO);
		useMedicalDevice.setText("Medical Device example");
		useMedicalDevice.setSelection(true);
//		useComputer.addListener(SWT.Selection, this);

		useTelevision = new Button(group, SWT.RADIO);
		useTelevision.setText("Television - IoT example");
		useTelevision.setSelection(false);
//		useIsolette.addListener(SWT.Selection, this);
	}

	public boolean canFinish() {
//		System.out.println("[EmftaWizardNewfilePage] canFinish() invoked");

		return (useMedicalDevice.isEnabled() || useTelevision.isEnabled());
	}

	public boolean finish() {

		if (canFinish()) {
//			System.out.println("[EmftaWizardNewfilePage] finishing");
			try {
				copyFile();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public void copyFile() throws Exception {
		IPath targetPath = this.getContainerFullPath();
		String toCopy = "unknown";

		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		if (bundle == null) {
			throw new Exception("plugin wizard is not found");
		}

		URL rootURL = bundle.getEntry("examples");
		if (rootURL == null) {
			throw new Exception("file or directory examples is not found");
		}

		File examplesDirectory = new File(FileLocator.toFileURL(rootURL).getFile());


//		System.out.println ("plop1=" + this.useWebserver.getSelection());
//		System.out.println ("plop2=" + this.useRouter.getSelection());
		if (this.useMedicalDevice.getSelection()) {
			toCopy = "medical-device.attacktree";
		}
		if (this.useTelevision.getSelection()) {
			toCopy = "television.attacktree";
		}

//		System.out.println("[EmftaWizardNewfilePage] tocopy" + toCopy);
//		System.out.println("[EmftaWizardNewfilePage] destination" + this.getFileName());
		String sourceFile = examplesDirectory + File.separator + toCopy;

//		System.out.println("[EmftaWizardNewfilePage] seg counts" + targetPath.segmentCount());

		IWorkspace ws = ResourcesPlugin.getWorkspace();
		String destinationFile;
		IProject relatedProject;
		if (targetPath.segmentCount() == 1) {
			relatedProject = ws.getRoot().getProject(targetPath.lastSegment());
//			System.out.println("[EmftaWizardNewfilePage] get raw location" + relatedProject.getLocation());

			destinationFile = relatedProject.getLocation().toOSString() + File.separator + this.getFileName();

		} else {
			IFile ifile;
			ifile = ws.getRoot().getFile(targetPath);
			relatedProject = ws.getRoot().getProject(targetPath.segment(0));

			destinationFile = ifile.getLocation().toOSString() + File.separator + this.getFileName();

		}

//		System.out.println("[EmftaWizardNewfilePage] related project=" + relatedProject.getName());

		if (!destinationFile.endsWith(".attacktree")) {
			destinationFile = destinationFile + ".attacktree";
		}
//
//		System.out.println("[EmftaWizardNewfilePage] source=" + sourceFile);
//		System.out.println("[EmftaWizardNewfilePage] destination=" + destinationFile);
		com.google.common.io.Files.copy(new File(sourceFile), new File(destinationFile));
		try {
			relatedProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}