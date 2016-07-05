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

package edu.cmu.sei.aaspe.model;

import java.util.ArrayList;
import java.util.List;

import org.osate.aadl2.instance.ComponentInstance;

public class Propagation {

	public final static int PROPAGATION_BUS = 0;
	public final static int PROPAGATION_LOCAL = 1;
	public final static int PROPAGATION_FLOW = 2;
	public final static int PROPAGATION_MEMORY = 3;
	public final static int PROPAGATION_PROCESSOR = 4;
	public final static int PROPAGATION_DATA = 5;
	
	private int type;
	private ComponentInstance source;
	private ComponentInstance target;
	private List<Propagation> nextPropagations;


	public Propagation()
	{
		nextPropagations = new ArrayList<Propagation>();
	}

	public Propagation (ComponentInstance src, int t, ComponentInstance destination)
	{
		this();
		this.source = src;
		this.type = t;
		this.target = destination;
	}

	public void addNextPropagation (Propagation p)
	{
		if (! this.nextPropagations.contains(p))
		{
			this.nextPropagations.add(p);
		}
	}

	public void addNextPropagations (List<Propagation> ps)
	{
		this.nextPropagations.addAll(ps);
	}

	public List<Propagation> getNextPropagations ()
	{
		return this.nextPropagations;
	}
	
	/**
	 * Copy the current object 
	 * @param copyNext - indicate if we also copy the next propagations
	 * @return
	 */
	public Propagation copy (boolean copyNext)
	{
		Propagation newPropagation = new Propagation(this.source, this.type, this.target);
		
		if (copyNext)
		{
			for (Propagation next : this.nextPropagations)
			{
				newPropagation.addNextPropagation(next.copy(true));
			}
		}
		return newPropagation;
	}

	public void setType (int t)
	{
		this.type = t;
	}

	public void setSource (ComponentInstance s)
	{
		this.source = s;
	}

	public void setTarget (ComponentInstance s)
	{
		this.target = s;
	}

	public ComponentInstance getTarget ()
	{
		return this.target;
	}

	public ComponentInstance getSource ()
	{
		return this.source;
	}



	public int getType ()
	{
		return this.type;
	}

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof Propagation)
        {
            Propagation other = (Propagation) object;
            sameSame = ((other.getSource() == this.source) &&
            			(other.getTarget() == this.target) &&
            			(other.getType() == this.type));
        }

        return sameSame;
    }

//	@Override
//	public String toString ()
//	{
//		return toString (this, new Stack<Propagation>());
//	}


	@Override
	public String toString ()
	{
		String str;
		
		str = source.getName();
		switch (type)
		{
			case PROPAGATION_BUS:
			{
				str += " -[bus]-> ";
				break;
			}

			case PROPAGATION_LOCAL:
			{
				str += " -[local]-> ";
				break;
			}

			case PROPAGATION_FLOW:
			{
				str += " -[flow]-> ";
				break;
			}

			case PROPAGATION_MEMORY:
			{
				str += " -[memory]-> ";
				break;
			}

			case PROPAGATION_PROCESSOR:
			{
				str += " -[processor]-> ";
				break;
			}
			
			case PROPAGATION_DATA:
			{
				str += " -[data]-> ";
				break;
			}			

			default:
			{
				str += " -[unknown]-> ";
				break;
			}
		}

		str += target.getName();
		return str;
	}
	
	
	public String toStringComplete ()
	{
		return toStringLevelComplete(0);
	}

	public String toStringLevelComplete (int n)
	{
		String str;
		str = "";


			str += "\n";

		for (int i = 0 ; i < n ; i++)
		{
			str += "   ";
		}
		str += source.getName();
		switch (type)
		{
			case PROPAGATION_BUS:
			{
				str += " -[bus]-> ";
				break;
			}

			case PROPAGATION_LOCAL:
			{
				str += " -[local]-> ";
				break;
			}

			case PROPAGATION_FLOW:
			{
				str += " -[flow]-> ";
				break;
			}

			case PROPAGATION_MEMORY:
			{
				str += " -[memory]-> ";
				break;
			}

			case PROPAGATION_PROCESSOR:
			{
				str += " -[processor]-> ";
				break;
			}
			
			case PROPAGATION_DATA:
			{
				str += " -[data]-> ";
				break;
			}			

			default:
			{
				str += " -[unknown]-> ";
				break;
			}
		}

		str += target.getName();

		for (Propagation p : this.nextPropagations)
		{
			str += p.toStringLevelComplete(n+1);
		}
		return str;
	}


//	public static String toString (Propagation propagation, Stack<Propagation> stack)
//	{
//		String str = "";
//
//
//		if (propagation.getNextPropagations().size() == 0)
//		{
//			for (Propagation p : stack)
//			{
//				str += p.toStringBasic() + " => " + propagation.toStringBasic();
//			}
//			str += "\n";
//
//		}
//		else
//		{
//			stack.push (propagation);
//			for (Propagation sub : propagation.getNextPropagations())
//			{
//				str += toString(sub, stack);
//			}
//			stack.pop();
//		}
//
//		return str;
//	}
}
