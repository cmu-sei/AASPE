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

package edu.cmu.sei.aaspe.utils;

import java.util.ArrayList;
import java.util.List;

import org.osate.aadl2.BooleanLiteral;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.EnumerationLiteral;
import org.osate.aadl2.IntegerLiteral;
import org.osate.aadl2.ListValue;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.NamedValue;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyConstant;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.RecordValue;
import org.osate.aadl2.StringLiteral;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.aadl2.properties.PropertyDoesNotApplyToHolderException;
import org.osate.aadl2.properties.PropertyNotPresentException;
import org.osate.aadl2.util.OsateDebug;
import org.osate.xtext.aadl2.properties.util.GetProperties;

public class PropertyUtils {
	public static String SECURITY_PROPERTIES_NAME 			= "security_properties";
	public static String PROPERTY_EXPOSURE_NAME 			= "exposure";
	public static String PROPERTY_ENCRYPTION_NAME 			= "encryption";
	public static String PROPERTY_AUTHENTICATION_METHOD 	= "authentication_method";
	public static String PROPERTY_SECURITY_LEVELS 			= "security_levels";
	public static String PROPERTY_SECURITY_DOMAINS 			= "domains";
//	public static String PROPERTY_IS_VERIFIED 				= "is_verified";
	public static String PROPERTY_TRUST						= "trust";

	private static long EXPOSURE_DEFAULT 					= 100;
	private static long TRUST_DEFAULT 						= 0;
	
	/**
	 * We get the exposure of a component. We need to browse the
	 * component hierarchy. If the parent component is less exposed
	 * (protected), then, we get its value.
	 * 
	 * @param aadlElement
	 * @return
	 */
	public static long getExposure(final NamedElement aadlElement) {
		try {
			Property exposure;
			NamedElement el;
			long result;
			long tmp;
			
			result = 100;
			el = aadlElement;
			
			/**
			 * We get the lowest value up while browsing the
			 * component hierarchy backwards.
			 */
			while (!(el instanceof SystemInstance))
			{
				exposure = GetProperties.lookupPropertyDefinition(aadlElement, SECURITY_PROPERTIES_NAME,
						PROPERTY_EXPOSURE_NAME);
				tmp = org.osate.xtext.aadl2.properties.util.PropertyUtils.getIntegerValue(el, exposure);
				
				if (tmp < result)
				{
					result = tmp;
				}
				el = ((InstanceObject) el).getContainingComponentInstance();
			}
			
			return result;
		} catch (PropertyNotPresentException|PropertyDoesNotApplyToHolderException|NullPointerException e) {
//			e.printStackTrace();
			OsateDebug.osateDebug("PropertyUtils", "Property Exception");
			return EXPOSURE_DEFAULT;
		}
	}

	/**
	 * get a record field from the security_properties::encryption property
	 * @param aadlElement - the element associated with the property
	 * @param recordField - the name of the record field
	 * @return
	 */
	public static String getEncryptionValue(final NamedElement aadlElement, final String recordField) {
		String result;
		PropertyExpression pe;

		result = null;
		pe = null;

		Property encryption = GetProperties.lookupPropertyDefinition(aadlElement, SECURITY_PROPERTIES_NAME,
				PROPERTY_ENCRYPTION_NAME);

		try {
			pe = org.osate.xtext.aadl2.properties.util.PropertyUtils.getSimplePropertyValue(aadlElement,
					encryption);

			if (pe instanceof RecordValue) {
				RecordValue rv = (RecordValue) pe;
				PropertyExpression recordFieldPropertyExpression = org.osate.xtext.aadl2.properties.util.PropertyUtils
						.getRecordFieldValue(rv, recordField);
	//			OsateDebug.osateDebug("PropertyUtils", "PropertyExpression=" + recordFieldPropertyExpression);
				if (recordFieldPropertyExpression instanceof NamedValue) {
					NamedValue nv = (NamedValue) recordFieldPropertyExpression;
					if (nv.getNamedValue() instanceof EnumerationLiteral) {
						EnumerationLiteral el = (EnumerationLiteral) nv.getNamedValue();
	//					OsateDebug.osateDebug("PropertyUtils", "el=" + el.getName());
						result = el.getName();
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}


	/**
	 * get all the security levels associated with a component. It returns
	 * a list of integer.
	 * @param aadlElement - the component associated with the security levels
	 * @return
	 */
	public static List<Integer> getSecurityLevels (NamedElement aadlElement)
	{
		List<Integer> levels;
		levels = new ArrayList<Integer>();

		PropertyExpression pe;

		pe = null;

		Property securityLevels = GetProperties.lookupPropertyDefinition(aadlElement, SECURITY_PROPERTIES_NAME,
				PROPERTY_SECURITY_LEVELS);
		pe = org.osate.xtext.aadl2.properties.util.PropertyUtils.getSimplePropertyValue(aadlElement,
				securityLevels);

		ListValue lv = (ListValue)pe;
		for (PropertyExpression element : lv.getOwnedListElements())
		{
			IntegerLiteral il;

			/**
			 * It can be a namedvalue if this is a constant. In that case,
			 * we retrieve the integerliteral associated with the constant.
			 */
			if (element instanceof NamedValue)
			{
				NamedValue nv = (NamedValue) element;
				PropertyConstant pc = (PropertyConstant) nv.getNamedValue();
				OsateDebug.osateDebug("PropertyUtils", "const=" + pc);
				il = (IntegerLiteral)pc.getConstantValue();

			}
			else
			{
				il = (IntegerLiteral)element;
			}
			long sl = il.getValue();
			levels.add((int)sl);


		}

		return levels;
	}


	/**
	 * return the value of the security_properties::domains property
	 * @param aadlElement - the element that is associated with the property
	 * @return
	 */
	public static List<String> getSecurityDomains (NamedElement aadlElement)
	{
		List<String> domains;
		domains = new ArrayList<String>();

		PropertyExpression pe;

		pe = null;



		Property securityDomains = GetProperties.lookupPropertyDefinition(aadlElement, SECURITY_PROPERTIES_NAME,
				PROPERTY_SECURITY_DOMAINS);

		if (! GetProperties.isAssignedPropertyValue (aadlElement, securityDomains))
		{
			return domains;
		}

		pe = org.osate.xtext.aadl2.properties.util.PropertyUtils.getSimplePropertyValue(aadlElement,
				securityDomains);

		ListValue lv = (ListValue)pe;
		for (PropertyExpression element : lv.getOwnedListElements())
		{
//			OsateDebug.osateDebug("PropertyUtils", "element=" + element);
			StringLiteral sl = (StringLiteral) element;
			domains.add(sl.getValue());
		}

		return domains;
	}


	public static List<String> getAuthenticationMethods (NamedElement aadlElement)
	{
		List<String> authenticationMethods;
		authenticationMethods = new ArrayList<String>();

		PropertyExpression pe;

		pe = null;

		Property securityLevels = GetProperties.lookupPropertyDefinition(aadlElement, SECURITY_PROPERTIES_NAME,
				PROPERTY_AUTHENTICATION_METHOD);
		try
		{
			pe = org.osate.xtext.aadl2.properties.util.PropertyUtils.getSimplePropertyValue(aadlElement,
					securityLevels);

			ListValue lv = (ListValue)pe;
			for (PropertyExpression element : lv.getOwnedListElements())
			{
				OsateDebug.osateDebug("PropertyUtils", "element=" + element);

				if (element instanceof NamedValue)
				{
					NamedValue nv = (NamedValue) element;
					if (nv.getNamedValue() instanceof EnumerationLiteral)
					{
						EnumerationLiteral el = (EnumerationLiteral) nv.getNamedValue();
//						OsateDebug.osateDebug("PropertyUtils", "name=" + el.getName());
						authenticationMethods.add(el.getName());
//						el.getName();
					}
				}
			}
		}
		catch (PropertyNotPresentException e)
		{
			OsateDebug.osateDebug("PropertyUtils", "Property not defined");
		}

		return authenticationMethods;
	}


	/**
	 * return the value of the security_properties::is_verified property
	 * @param component - the element that has the property
	 * @return
	 */
	public static boolean isVerified (ComponentInstance component)
	{
		ComponentInstance parent;
		PropertyExpression pe;
		Property trustProperty;
		long result;
		
		pe = null;
		parent = component.getContainingComponentInstance();
		
		trustProperty = GetProperties.lookupPropertyDefinition(component, SECURITY_PROPERTIES_NAME,
				PROPERTY_TRUST);
		pe = org.osate.xtext.aadl2.properties.util.PropertyUtils.getSimplePropertyValue(component,
				trustProperty);
		result = ((IntegerLiteral) pe).getValue();

		if (component.getCategory() == ComponentCategory.THREAD)
		{
//			OsateDebug.osateDebug ("component = "+ component.getName() + " ; parent = "+ parent.getName());

			return isVerified (parent);
		}
		
		if ( (component.getCategory() == ComponentCategory.PROCESS) || (component.getCategory() == ComponentCategory.DEVICE))
		{
//			OsateDebug.osateDebug ("component = " + component.getName() + " ; parent = "+ parent.getName());
			return (result == 100) || isVerified (parent);
		}
		
		return (result == 100);
	}
}
