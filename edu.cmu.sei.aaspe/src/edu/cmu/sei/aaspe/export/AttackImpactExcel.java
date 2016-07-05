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

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.ui.util.ResourceUtil;
import org.osate.aadl2.instance.SystemInstance;

import edu.cmu.sei.aaspe.logic.AttackImpact;
import edu.cmu.sei.aaspe.model.Propagation;
import edu.cmu.sei.aaspe.model.Vulnerability;

public class AttackImpactExcel {

	private AttackImpact analysis;
	Sheet vulnerabilitySheet;
	short rowId;
	Font bigBold;
	Font simpleBold;
	Font simpleItalic;
	CreationHelper creationHelper;

	public AttackImpactExcel (AttackImpact a)
	{
		this.analysis = a;
	}

	public void printPropagations (Stack<Propagation> stack)
	{
		Propagation propagation = stack.peek();

		if (propagation.getNextPropagations().size() == 0)
		{
			short n = 1;
			Row row;
			row = vulnerabilitySheet.createRow(rowId);
			for (Propagation p : stack)
			{
				if (n == 1)
				{
					row.createCell(n).setCellValue(p.getSource().getName());
					n++;
				}

				row.createCell(n).setCellValue(p.getTarget().getName());
				n++;

			}
			rowId++;
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
		RichTextString str;
		Row row;
		rowId = 1;

		for (Vulnerability v : analysis.getVulnerabilities())
		{
			row = vulnerabilitySheet.createRow(rowId);

			str = creationHelper.createRichTextString(v.getRelatedElement().getName());
			str.applyFont(simpleBold);
			row.createCell((short)0).setCellValue(str);
			row.createCell(1).setCellValue(v.getName());
			row.createCell(2).setCellValue(v.getComment());

			int propagationNb = 1;
			rowId++;

			if (v.getPropagations().size() > 0)
			{
				for (Propagation propagation : v.getPropagations())
				{
					row = vulnerabilitySheet.createRow(rowId);
					row.createCell((short)0).setCellValue("Propagation path #" + propagationNb);
					rowId++;
					Stack<Propagation> propagationStack = new Stack<Propagation> ();
					propagationStack.push (propagation);
					printPropagations(propagationStack);
					propagationNb++;
				}
			}
			else
			{
				row = vulnerabilitySheet.createRow(rowId);
				str = creationHelper.createRichTextString("No propagation path to show");
				str.applyFont(simpleItalic);
				row.createCell((short)0).setCellValue(str);

			}
			rowId++;
			rowId++;
		}
	}




	public void export ()
	{
		SystemInstance systemInstance = analysis.getSystemInstance();

		try {
			Workbook wb = new XSSFWorkbook();
	        creationHelper = wb.getCreationHelper();

		    FileOutputStream fileOut;
		    String fileName;

			fileName = ResourceUtil.getFile(systemInstance.eResource()).getName();
			fileName = fileName.replace(".aaxl2", "") + "-attackimpact.xlsx";


			URI uri = EcoreUtil.getURI(systemInstance);
			IPath ipath = new Path(uri.toPlatformString(true));
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(ipath);
			String path = file.getRawLocation().removeLastSegments(1).toOSString();
			path = path + File.separator + fileName;

			fileOut = new FileOutputStream(path);

			vulnerabilitySheet = wb.createSheet("Vulnerabilities");

			// Create a row and put some cells in it. Rows are 0 based.

			Row row = vulnerabilitySheet.createRow(0);
			// Create a cell and put a value in it.

			RichTextString str = creationHelper.createRichTextString("Component");
			bigBold = wb.createFont();
//			font.setItalic(true);
			bigBold.setBold(true);
			bigBold.setUnderline(Font.U_SINGLE);
//			bigBold.setFontHeight((short)14);
			bigBold.setFontHeightInPoints((short)12);

			simpleBold = wb.createFont();
			simpleBold.setBold(true);
			simpleBold.setUnderline(Font.U_SINGLE);
//			simpleBold.setFontHeight((short)14);
			simpleBold.setFontHeightInPoints((short)10);

			simpleItalic = wb.createFont();
			simpleItalic.setFontHeightInPoints((short)8);
			simpleItalic.setItalic(true);


			str.applyFont(bigBold);
			row.createCell((short)0).setCellValue(str);

			str = creationHelper.createRichTextString("Vulnerability Name");
			str.applyFont(bigBold);
			row.createCell(1).setCellValue(str);

			str = creationHelper.createRichTextString("Details and comments");
			str.applyFont(bigBold);
			row.createCell(2).setCellValue(str);


			printVulnerabilities();

			//numeric value
//			row.createCell(1).setCellValue(1.2);

			//plain string value
//			row.createCell(2).setCellValue("This is a string cell");

			//rich text string
//			RichTextString str = creationHelper.createRichTextString("Apache");
//			Font font = wb.createFont();
//			font.setItalic(true);
//			font.setUnderline(Font.U_SINGLE);
//			str.applyFont(font);
//			row.createCell(3).setCellValue(str);



			wb.write(fileOut);

			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		edu.cmu.sei.aaspe.utils.Utils.refreshWorkspace(null);
	}
}
