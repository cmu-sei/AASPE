package edu.cmu.aaspe.attackimpact.wizards;

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

public class AttackImpactWizardNewfilePage extends org.eclipse.ui.dialogs.WizardNewFileCreationPage {
	private Button useWebserver;
	private Button useRouter;

	public AttackImpactWizardNewfilePage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
	}

	public AttackImpactWizardNewfilePage(IWorkbench workbench, IStructuredSelection selection) {
		super("Attack Impact New File", selection);
		this.setMessage("Select the model you want to copy and the output directory");
		this.setDescription("Select the model you want to copy and the output directory");
		this.setTitle("Attack Impact Model example");

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
		group.setText("Attack Impact to add");
		group.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		// sample section generation checkboxes
		useWebserver = new Button(group, SWT.RADIO);
		useWebserver.setText("Webserver example");
		useWebserver.setSelection(true);
//		useComputer.addListener(SWT.Selection, this);

		useRouter = new Button(group, SWT.RADIO);
		useRouter.setText("Router - IoT example");
		useRouter.setSelection(false);
//		useIsolette.addListener(SWT.Selection, this);
	}

	public boolean canFinish() {
//		System.out.println("[EmftaWizardNewfilePage] canFinish() invoked");

		return (useWebserver.isEnabled() || useRouter.isEnabled());
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
		if (this.useWebserver.getSelection()) {
			toCopy = "webserver.attackimpact";
		}
		if (this.useRouter.getSelection()) {
			toCopy = "router.attackimpact";
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

		if (!destinationFile.endsWith(".attackimpact")) {
			destinationFile = destinationFile + ".attackimpact";
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