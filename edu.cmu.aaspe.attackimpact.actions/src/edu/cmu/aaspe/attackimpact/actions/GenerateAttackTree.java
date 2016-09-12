package edu.cmu.aaspe.attackimpact.actions;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
import org.eclipse.emf.edit.provider.StyledString.Style.BorderStyle;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.LineStyle;
import org.eclipse.sirius.diagram.NodeStyle;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.SquareSpec;
import org.eclipse.sirius.diagram.description.style.SquareDescription;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.RGBValues;
import org.eclipse.sirius.viewpoint.Style;
import org.eclipse.sirius.viewpoint.description.ColorDescription;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.SystemColor;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.description.style.StyleDescription;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osate.aadl2.util.OsateDebug;

import edu.cmu.attackimpact.AttackImpactFactory; 

import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.Propagation;
import edu.cmu.attackimpact.Vulnerability;
import edu.cmu.sei.aaspe.utils.SiriusUtil;


public class GenerateAttackTree implements org.eclipse.sirius.tools.api.ui.IExternalJavaAction {

	private DDiagram diagram = null;
	private List<Node> browsedNodes;
	

	private void fillAttackTreeNodeFromAttackImpactNode (edu.cmu.attacktree.Node at,  edu.cmu.attackimpact.Node ai, Stack<edu.cmu.attackimpact.Node> stack)
	{
		stack.push(ai);
//		System.out.println("[GenerateAttackTree] fillAttackTreeNodeFromAttackImpactNode on " + ai.getName());

		at.setName(ai.getName());
		at.setDescription(ai.getDescription());
		
		edu.cmu.attackimpact.Model attackImpactModel = (edu.cmu.attackimpact.Model) ai.eContainer();
		
		for (Vulnerability v : ai.getVulnerabilities())
		{
			edu.cmu.attacktree.Vulnerability attackTreeVulnerability = edu.cmu.attacktree.AttackTreeFactory.eINSTANCE.createVulnerability();
			attackTreeVulnerability.setName(v.getName());
			attackTreeVulnerability.setDescription(v.getDescription());
			attackTreeVulnerability.setSeverity(v.getSeverity());
			at.getVulnerabilities().add(attackTreeVulnerability);
		}
		
		for (Node n : attackImpactModel.getNodes())
		{
			if (n == ai)
			{
				continue;
			}
			
			if (Utils.canImpact(n, ai, false) && (stack.contains(n) == false))
			{
				
				edu.cmu.attacktree.Node attackTreeNode;
				attackTreeNode = edu.cmu.attacktree.AttackTreeFactory.eINSTANCE.createNode();
				fillAttackTreeNodeFromAttackImpactNode(attackTreeNode, n, stack);
				at.getSubNodes().add(attackTreeNode);
				
			}
		}
		stack.pop();
	}
	

	private edu.cmu.attacktree.Model  generateAttackTreeModel (Node root)
	{
		edu.cmu.attacktree.Model attackTreeModel = edu.cmu.attacktree.AttackTreeFactory.eINSTANCE.createModel();
		edu.cmu.attacktree.Node attackTreeNode;
		attackTreeNode = edu.cmu.attacktree.AttackTreeFactory.eINSTANCE.createNode();
		fillAttackTreeNodeFromAttackImpactNode(attackTreeNode, root, new Stack<edu.cmu.attackimpact.Node> ());
		attackTreeModel.setRootNode(attackTreeNode);
		return attackTreeModel;
	}

	
	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {

		System.out.println("[GenerateAttackTree] calling execute");
		browsedNodes = new ArrayList<Node>();
		for (EObject eo : selections) {
			EObject target = null;

			if (eo instanceof DNode)
			{
				DNode dnode = (DNode) eo;

				Utils.clearDiagram(dnode.getParentDiagram());

				this.diagram = dnode.getParentDiagram();
				
				
				if (dnode.getTarget() instanceof Node)
				{
					long startTime = System.currentTimeMillis();
					Node attackImpactNode = (Node)dnode.getTarget();
					edu.cmu.attacktree.Model attackTreeModel;
					attackTreeModel = generateAttackTreeModel(attackImpactNode);
//					System.out.println("[GenerateAttackTree] model=" + attackTreeModel);
					attackTreeModel.setName("AttackTree for " + attackImpactNode.getName());
					IFile ifile = ResourceUtil.getFile(dnode.getTarget().eResource());
					String filename = ifile.getFullPath().removeFirstSegments(1).toString();
					
//					String filename = ifile.getName();
					filename = filename.replace(".attackimpact", "");
					filename = filename + "_" + attackImpactNode.getName().toLowerCase() + ".attacktree";
					OsateDebug.osateDebug("Filename=" + filename);
				
//					monitor.subTask("Writing attack model file");
//					serializeAttackTreeModel(attackTreeModel, ResourceUtil.getFile(dnode.eResource())
//							.getProject(), filename);
					URI aiURI = EcoreUtil.getURI(eo);
					
					URI newURI = EcoreUtil.getURI(eo).trimFragment().trimSegments(1).appendSegment("attacktree")
							.appendSegment("at" + ".attacktree");
					final IProject currentProject = ResourceUtil.getFile(eo.eResource()).getProject();
					final URI modelURI = serializeAttackTreeModel(attackTreeModel, newURI, currentProject);
					autoOpenAttackTreeModel(modelURI, currentProject);
					
					long endTime = System.currentTimeMillis();
					
					long totalTime = (endTime - startTime) / 1000;
					
					System.out.println("[GenerateAttackTree] done in " + totalTime + "s");

				}
			}

		}
	}

	public void autoOpenAttackTreeModel(final URI newURI, final IProject activeProject) {

		try {

			Job attackTreeCreationJob = new Job("Creation of Attack Tree Graph") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {

					monitor.beginTask("Creation of Attack Impact Graph", 100);

					createAndOpenAttackTree (activeProject, newURI, monitor);
					try {
						activeProject.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					} catch (CoreException e) {
						// Error while refreshing the project
					}
					monitor.done();

					return Status.OK_STATUS;
				}
			};
			attackTreeCreationJob.setUser(true);
			attackTreeCreationJob.schedule();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


	private void createAndOpenAttackTree(final IProject project, final URI attackTreeURI, IProgressMonitor monitor) {
		SiriusUtil util = SiriusUtil.INSTANCE;
		URI attackImpactViewpointURI = URI.createURI("viewpoint:/attracktree.design/AttackTree");

		URI semanticResourceURI = URI.createPlatformResourceURI(attackTreeURI.toPlatformString(true), true);
		Session existingSession = util.getSessionForProjectAndResource(project, semanticResourceURI, monitor);
		if (existingSession == null) {
			// give it a second try. null was returned the first time due to a class cast exception at the end of
			// setting the Modeling perspective.
			existingSession = util.getSessionForProjectAndResource(project, semanticResourceURI, monitor);
		}
		if (existingSession != null) {
			util.saveSession(existingSession, monitor);
			ResourceSetImpl resset = new ResourceSetImpl();
			edu.cmu.attacktree.Model model = getAttackTreeModelFromSession(existingSession, semanticResourceURI);
			// XXX this next piece of code tries to compensate for a bug in Sirius where it cannot find the Model
			// It should be there since the getSessionForProjectandResource would have put it there.
			if (model == null) {
				OsateDebug.osateDebug(
						"Could not find semantic resource Attack Impact in session for URI " + semanticResourceURI.path());
				EObject res = resset.getEObject(attackTreeURI, true);
				if (res instanceof edu.cmu.attacktree.Model) {
					model = (edu.cmu.attacktree.Model) res;
				}
			}
			if (model == null) {
				OsateDebug.osateDebug("Could not find Attack Impact for URI " + attackTreeURI.path());
				return;
			}
			final Viewpoint attackTreeVP = util.getViewpointFromRegistry(attackImpactViewpointURI);
			final RepresentationDescription description = util.getRepresentationDescription(attackTreeVP, "AttackTreeDiagram");
			String representationName = model.getName() + " Graph";
			final DRepresentation rep = util.findRepresentation(existingSession, attackTreeVP, description,
					representationName);
			if (rep != null) {
				DialectUIManager.INSTANCE.openEditor(existingSession, rep, new NullProgressMonitor());
			} else {
				try {
					util.createAndOpenRepresentation(existingSession, attackTreeVP, description, representationName, model,
							monitor);
				} catch (Exception e) {
					OsateDebug.osateDebug("Could not create and open Attack Tree Model " + model.getName());
					return;
				}
			}

		}
	}
	
	
	private edu.cmu.attacktree.Model getAttackTreeModelFromSession(Session session, URI uri) {
		Resource resource = SiriusUtil.INSTANCE.getResourceFromSession(session, uri);
		if (resource != null) {
			for (EObject object : resource.getContents()) {
				if (object instanceof edu.cmu.attacktree.Model) {
					return (edu.cmu.attacktree.Model) object;
				}
			}
		}
		return null;
	}
	
	private static URI serializeAttackTreeModel(edu.cmu.attacktree.Model attackTreeModel,  final URI newURI, IProject activeProject) {

		try {

			ResourceSet set = new ResourceSetImpl();
			Resource res = set.createResource(newURI);

			res.getContents().add(attackTreeModel);

			res.save(null); 
			activeProject.refreshLocal(IResource.DEPTH_INFINITE, null);
			return EcoreUtil.getURI(attackTreeModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newURI;

	}
	

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		System.out.println("[GenerateAttackTree] calling canExecute");
		for (EObject eo : selections) {
			EObject target = null;

			if (eo instanceof DNode)
			{
				DNode dnode = (DNode) eo;
				if (dnode.getTarget() instanceof Node)
				{
					return true;
				}
				
			}
		}
		return false;
	}

}
