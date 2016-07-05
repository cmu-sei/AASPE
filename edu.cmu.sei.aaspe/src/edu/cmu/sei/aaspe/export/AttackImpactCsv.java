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

package edu.cmu.sei.aaspe.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.util.OsateDebug;

import edu.cmu.sei.aaspe.logic.AttackImpact;
import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;

public class AttackImpactCsv {

	private AttackImpact analysis;
	private StringBuffer result;

	public AttackImpactCsv (AttackImpact a)
	{
		this.analysis = a;
		result = new StringBuffer ();
		
	}

	public void printPropagations (Stack<Propagation> stack)
	{
		Propagation propagation = stack.peek();
//OsateDebug.osateDebug("SIZE="+ stack.size());
		if ( (propagation.getNextPropagations().size() == 0) )
		{
			
			
			for (Propagation p : stack)
			{
				this.result.append(p.toString());
				this.result.append(",");
				
			}
			this.result.append("\n");
		}
		else
		{
			for (Propagation p : propagation.getNextPropagations())
			{
				stack.push(p);
				printPropagations (stack);
				stack.pop();
			}
		}

	}

	public void printVulnerabilities ()
	{

		for (Vulnerability v : analysis.getVulnerabilities())
		{
			this.result.append(v.getRelatedElement().getName() + "," + v.getName() + " - " + v.getComment() + "\n");


			if (v.getPropagations().size() > 0)
			{
				for (Propagation propagation : v.getPropagations())
				{

					Stack<Propagation> propagationStack = new Stack<Propagation> ();
					propagationStack.push (propagation);
					printPropagations(propagationStack);
					
				}
			}
			else
			{
				this.result.append("No propagation path to show");
			}

			this.result.append("\n");

			this.result.append("\n");
		}
	}




	public void export ()
	{
		SystemInstance systemInstance = analysis.getSystemInstance();

		try {
			


			printVulnerabilities();

			
		    FileOutputStream fileOut;
		    String fileName;

			fileName = ResourceUtil.getFile(systemInstance.eResource()).getName();
			fileName = fileName.replace(".aaxl2", "") + "-attackimpact.csv";


			URI uri = EcoreUtil.getURI(systemInstance);
			IPath ipath = new Path(uri.toPlatformString(true));
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
			String path = file.getRawLocation().removeLastSegments(1).toOSString();
			path = path + File.separator + fileName;

			fileOut = new FileOutputStream(path);
			fileOut.write(result.toString().getBytes());
			
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		edu.cmu.sei.aaspe.utils.Utils.refreshWorkspace(null);
	}
}
