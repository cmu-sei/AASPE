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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.provider.StyledString.Style.BorderStyle;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.LineStyle;
import org.eclipse.sirius.diagram.NodeStyle;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.SquareSpec;
import org.eclipse.sirius.diagram.description.style.SquareDescription;
import org.eclipse.sirius.viewpoint.RGBValues;
import org.eclipse.sirius.viewpoint.Style;
import org.eclipse.sirius.viewpoint.description.ColorDescription;
import org.eclipse.sirius.viewpoint.description.SystemColor;
import org.eclipse.sirius.viewpoint.description.style.StyleDescription;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osate.aadl2.util.OsateDebug;

import edu.cmu.attackimpact.AttackImpactFactory; 
import edu.cmu.attackimpact.Model;
import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.Propagation;
import edu.cmu.attackimpact.Vulnerability;


public class GenerateAttackTree implements org.eclipse.sirius.tools.api.ui.IExternalJavaAction {

	private DDiagram diagram = null;
	private List<Node> browsedNodes;
	

	private void fillAttackTreeNodeFromAttackImpactNode (edu.cmu.attacktree.Node at,  edu.cmu.attackimpact.Node ai, Stack<edu.cmu.attackimpact.Node> stack)
	{
		stack.push(ai);
//		System.out.println("[GenerateAttackTree] fillAttackTreeNodeFromAttackImpactNode on " + ai.getName());

		at.setName(ai.getName());
		at.setDescription(ai.getDescription());
		
		Model attackImpactModel = (Model) ai.eContainer();
		
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
					serializeAttackTreeModel(attackTreeModel, ResourceUtil.getFile(dnode.eResource())
							.getProject(), filename);
					long endTime = System.currentTimeMillis();
//					OsateDebug.osateDebug("Export Attack Impact - finished in " + ((endTime - startTime) / 1000) + " s" );
					long totalTime = (endTime - startTime) / 1000;
					
					System.out.println("[GenerateAttackTree] done in " + totalTime + "s");

				}
			}

		}
	}
	
	private static void serializeAttackTreeModel(edu.cmu.attacktree.Model attackTreeModel, IProject activeProject, String filename) {
		IFile newFile = activeProject.getFile(filename);

		try {

			ResourceSet set = new ResourceSetImpl();
			Resource res = set.createResource(URI.createURI(newFile.toString()));

			res.getContents().add(attackTreeModel);

			FileOutputStream fos = new FileOutputStream(newFile.getRawLocation().toFile());
			res.save(fos, null); 
			OsateDebug.osateDebug("[GenerateAttackTree]", "activeproject=" + activeProject.getName());

			activeProject.refreshLocal(IResource.DEPTH_INFINITE, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

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
