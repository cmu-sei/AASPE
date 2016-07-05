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
 
 package edu.cmu.aaspe.codegeneration.sel4


import org.osate.aadl2.NamedElement
import org.osate.aadl2.SystemImplementation
import org.osate.aadl2.AadlPackage
import org.osate.aadl2.ProcessorSubcomponent
import org.osate.aadl2.ProcessSubcomponent
import org.osate.aadl2.ProcessType
import org.osate.aadl2.Aadl2Factory
import org.osate.aadl2.ProcessImplementation
import org.osate.aadl2.ThreadSubcomponent
import org.osate.aadl2.ThreadSubcomponentType
import org.osate.aadl2.DirectionType
import org.osate.aadl2.ThreadType

import org.eclipse.emf.common.util.URI
import org.osate.aadl2.ThreadImplementation
import java.util.List
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getUserProcessSubcomponents
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getSchedulerProcessSubcomponent
import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getPeriod
import java.io.FileOutputStream
import org.eclipse.xtext.ui.util.ResourceUtil
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Path 
import org.osate.xtext.aadl2.properties.util.GetProperties
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.ResourcesPlugin
import java.io.File
import edu.cmu.aaspe.codegeneration.Utils

public class Scheduler
{
	

	
	def static void generateSchedulerCode(SystemImplementation systemImplementation)
	{
		val StringBuffer code = new StringBuffer()

		code.append ("#include <stdio.h>\n");
		code.append ("#include <assert.h>\n");
		code.append ("#include <stdlib.h>\n");
		
		code.append ("#include <sel4/sel4.h>\n");
		
		code.append ("#include <inttypes.h>\n");
		code.append ("#include <math.h>\n");
		code.append ("#include <stdio.h>\n");
		code.append ("#include <time.h>\n");
		
		code.append ("int main(int argc, char **argv) {\n");
		code.append ("    seL4_MessageInfo_t tag;\n");
		code.append ("    seL4_Word msg;\n");
		code.append ("    uint64_t ticks;\n");
		code.append ("    char* s = malloc(100);\n");
		
		code.append ("    printf(\"timer client: hey hey hey\\n\");\n");
		
		code.append ("    tag = seL4_MessageInfo_new(0, 0, 0, 1);\n");
		code.append ("    seL4_SetMR(0, MSG_DATA);\n");
		
		code.append ("    tag = seL4_Call(EP_CPTR, tag);\n");
		
		    /* check that we got the expected repy */
		code.append ("    assert(seL4_MessageInfo_get_length(tag) == 1);\n");
		code.append ("    msg = seL4_GetMR(0);\n");
		    
		code.append ("    printf(\"client: narf!\\n\");\n");
		
		
		code.append ("    int start_time = 0;\n");
		code.append ("    int num_prints = 0;\n");
		code.append ("    while (1) {\n");
		code.append ("        wait_for_tick();\n");
		
		systemImplementation.userProcessSubcomponents.forEach [processSubcomponent |
			
		val processImpl = processSubcomponent.componentImplementation as ProcessImplementation
		if(processSubcomponent.name.equals("scheduler") || processSubcomponent.name.equals("loader"))
		{
			return
		}
		
		processImpl.allSubcomponents.filter(ThreadSubcomponent).forEach[ threadSubcomponent | 
		
		
		code.append ("        /* schedule task " + threadSubcomponent.name + " at rate " + threadSubcomponent.period + "*/\n")		
		code.append ("        if ( (ticks % " + threadSubcomponent.period + ") == 0 {\n");
		code.append ("            printf(\"Activate thread "+ threadSubcomponent.name +"\\n\");\n");
		code.append ("        }\n");				
				
		
			]
		]


		code.append ("    }\n");		
		code.append ("    return 0;");
		code.append ("}");






		Utils.write(systemImplementation, "scheduler" + File.separator +  "scheduler.c" , code.toString)
	}
	
	
	def static void generateSchedulerMakefile (SystemImplementation systemImplementation)
	{
		val StringBuffer code = new StringBuffer()
		code.append ("TARGETS := $(notdir $(SOURCE_DIR)).bin\n");
		code.append ("LDFLAGS += -u __vsyscall_ptr\n");
		code.append ("CFILES :=  $(patsubst $(SOURCE_DIR)/%,%,$(wildcard $(SOURCE_DIR)/src/*.c))");
		code.append ("LIBS := c sel4 sel4muslcsys utils");
		code.append ("include $(SEL4_COMMON)/common.mk");
		 
		Utils.write(systemImplementation, "scheduler" + File.separator + "Makefile" , code.toString)
		
		
		val StringBuffer kbuild = new StringBuffer()
		kbuild.append("components-$(CONFIG_APP_scheduler) += scheduler\n");
		kbuild.append("scheduler: common libsel4 libmuslc libsel4muslcsys libutils\n");
		Utils.write(systemImplementation, "scheduler" + File.separator + "Kbuild", kbuild.toString);


		val StringBuffer kconfig = new StringBuffer()
		kconfig.append("menuconfig APP_SCHDULER\n");
    	kconfig.append("\tbool \"seL4 scheduler for generated system\"\n");
    	kconfig.append("\tdefault n\n");
    	kconfig.append("\tdepends on LIB_SEL4 && (LIB_MUSL_C || LIB_SEL4_C) && LIB_SEL4_MUSLC_SYS && LIB_UTILS\n");
    	kconfig.append("\thelp\n");
		kconfig.append("\t\tGenerated scheduler\n");
		Utils.write(systemImplementation, "scheduler" + File.separator + "Kconfig", kconfig.toString);
	}
	
}