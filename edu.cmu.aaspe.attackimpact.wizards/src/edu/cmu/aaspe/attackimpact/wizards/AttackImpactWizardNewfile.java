package edu.cmu.aaspe.attackimpact.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class AttackImpactWizardNewfile extends Wizard implements INewWizard {
	private IWorkbench workbench;
	private IStructuredSelection selection;
	private AttackImpactWizardNewfilePage mainPage;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle("New AttackImpact File");
	}

	public void addPages() {
		mainPage = new AttackImpactWizardNewfilePage(workbench, selection);
		addPage(mainPage);
	}

	public boolean canFinish() {
		return mainPage.canFinish();
	}

	@Override
	public boolean performFinish() {

		return mainPage.finish();
	}

}