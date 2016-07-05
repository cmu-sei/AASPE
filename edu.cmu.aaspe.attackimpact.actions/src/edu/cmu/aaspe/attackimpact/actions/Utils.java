package edu.cmu.aaspe.attackimpact.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;

import edu.cmu.attackimpact.Node;
import edu.cmu.attackimpact.Propagation;

public class Utils {

	public static void clearDiagram (DDiagram diagram)
	{
		for (DDiagramElement el : diagram.getDiagramElements())
		{
			el.getStyle().getCustomFeatures().clear();
		}
	}
	
	private static Map<Node,Boolean> impactCache;
	
	public static boolean canImpact (Node source, Node destination, boolean recursive)
	{
		impactCache = new HashMap<Node, Boolean>();
		
		if (source == destination)
		{
			return false;
		}
//		System.out.println("canImpact source= " + source.getName());
//		System.out.println("canImpact destination= " + destination.getName());

		return canImpactReal(source, destination, recursive);
	}
	
	public static boolean canImpactReal (Node source, Node destination, boolean recursive)
	{
//		System.out.println("canImpactreal source= " + source.getName());
//		System.out.println("canImpactreal destination= " + destination.getName());
//		System.out.println("canImpactreal equals= " + (source == destination));

		
		if (impactCache.containsKey(source))
		{
			return impactCache.get(source);
		}
		
		
		if (source == destination)
		{
			impactCache.put(source, true);
			return true;
		}
		else
		{
			
			for (Propagation propagation : source.getPropagations())
			{
				for (Node propagationDestination : propagation.getDestinations())
				{
					if (propagationDestination == destination)
					{
						impactCache.put(source, true);
						return true;
					}
					
					if (recursive)
					{
						/**
						 * We temporary put the value to false. Otherwise, we enter
						 * into a infinite recursion which triggers a stack overflow.
						 */
						impactCache.put(source, false);
						if (canImpactReal(propagationDestination, destination, true) == true)
						{
							impactCache.put(source, true);
							return true;
						}
					}
				}
			}
		}
		impactCache.put(source, false);

		return false;
	}
}
