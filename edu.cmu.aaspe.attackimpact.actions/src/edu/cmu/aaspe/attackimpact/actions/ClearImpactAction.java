package edu.cmu.aaspe.attackimpact.actions;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
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

import edu.cmu.attackimpact.AttackImpactFactory;
import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.Propagation;
import edu.cmu.attackimpact.Vulnerability;


public class ClearImpactAction implements org.eclipse.sirius.tools.api.ui.IExternalJavaAction {

	private DDiagram diagram = null;
	
	
	
	@Override
	public void execute(Collection<? extends EObject> selections, Map<String, Object> parameters) {

		System.out.println("[ClearImpactAction] calling execute");
		for (EObject eo : selections) {
			System.out.println("[ClearImpactAction] eo=" + eo);

			if (eo instanceof DDiagram)
			{
				Utils.clearDiagram ((DDiagram)eo);
			}
			
			if (eo instanceof DNode)
			{
				DNode dnode = (DNode) eo;
				
				Utils.clearDiagram(dnode.getParentDiagram());
			}

		}
	}

	@Override
	public boolean canExecute(Collection<? extends EObject> selections) {
		return true;
	}

}
