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

import edu.cmu.aaspe.codegeneration.Utils
import java.io.File
import org.osate.aadl2.ProcessImplementation
import org.osate.aadl2.SystemImplementation

import static extension edu.cmu.aaspe.codegeneration.AadlHelper.getUserProcessSubcomponents

public class Loader
{ 
	
	def static void generateLoaderCode(SystemImplementation systemImplementation)
	{
		val StringBuffer code = new StringBuffer()

		code.append("#include <autoconf.h>\n");
		code.append("\n");
		code.append("#include <stdio.h>\n");
		code.append("#include <assert.h>\n");
		code.append("\n");
		code.append("#include <sel4/sel4.h>\n");
		code.append("\n");
		code.append("#include <simple/simple.h>\n");
		code.append("#include <simple-default/simple-default.h>\n");
		code.append("\n");
		code.append("#include <vka/object.h>\n");
		code.append("#include <vka/object_capops.h>\n");
		code.append("\n");
		code.append("#include <allocman/allocman.h>\n");
		code.append("#include <allocman/bootstrap.h>\n");
		code.append("#include <allocman/vka.h>\n");
		code.append("\n");
		code.append("#include <vspace/vspace.h>\n");
		code.append("\n");
		code.append("#include <sel4utils/vspace.h>\n");
		code.append("#include <sel4utils/mapping.h>\n");
		code.append("#include <sel4utils/process.h>\n");
		code.append("\n");
		code.append("#include <sel4platsupport/timer.h>\n");
		code.append("#include <platsupport/plat/timer.h>\n");
		code.append("\n");
		code.append("/* constants */\n");
		code.append("#define IPCBUF_FRAME_SIZE_BITS 12 // use a 4K frame for the IPC buffer\n");
		code.append("#define IPCBUF_VADDR 0x7000000 // arbitrary (but free) address for IPC buffer\n");
		code.append("\n");
		code.append("#define EP_BADGE 0x61 // arbitrary (but unique) number for a badge\n");
		code.append("#define MSG_DATA 0x6161 // arbitrary data to send\n");
		code.append("\n");
		code.append("#define APP_PRIORITY seL4_MaxPrio\n");
		code.append("#define APP_IMAGE_NAME \"printer-client\"\n");
		code.append("\n");
		code.append("/* global environment variables */\n");
		code.append("seL4_BootInfo *info;\n");
		code.append("simple_t simple;\n");
		code.append("vka_t vka;\n");
		code.append("allocman_t *allocman;\n");
		code.append("vspace_t vspace;\n");
		code.append("seL4_timer_t *timer;\n");
		code.append("\n");
		code.append("vka_object_t ep_object;\n");
		code.append("cspacepath_t ep_cap_path;\n");
		code.append("\n");
		code.append("int tot_num_ticks = 0;\n");
		code.append("\n");
		code.append("/* static memory for the allocator to bootstrap with */\n");
		code.append("#define ALLOCATOR_STATIC_POOL_SIZE ((1 << seL4_PageBits) * 10)\n");
		code.append("UNUSED static char allocator_mem_pool[ALLOCATOR_STATIC_POOL_SIZE];\n");
		code.append("\n");
		code.append("/* dimensions of virtual memory for the allocator to use */\n");
		code.append("#define ALLOCATOR_VIRTUAL_POOL_SIZE ((1 << seL4_PageBits) * 100)\n");
		code.append("\n");
		code.append("/* static memory for virtual memory bootstrapping */\n");
		code.append("UNUSED static sel4utils_alloc_data_t data;\n");
		code.append("\n");
		code.append("/* stack for the new thread */\n");
		code.append("#define THREAD_2_STACK_SIZE 4096\n");
		code.append("UNUSED static int thread_2_stack[THREAD_2_STACK_SIZE];\n");
		code.append("\n");
		code.append("/* convenience function */\n");
		code.append("extern void name_thread(seL4_CPtr tcb, char *name);\n");
		code.append("\n");
		code.append("\n");
		code.append("/* function to run in the new thread */\n");
		code.append("void thread_2(void) {\n");
		code.append("\n");
		code.append("printf(\"thread_2: hallo worldn\\n\");\n");
		code.append("\n");
		code.append("\n");
		code.append("seL4_Word sender_badge;\n");
		code.append("seL4_MessageInfo_t tag;\n");
		code.append("seL4_Word msg;\n");
		code.append("\n");
		code.append("while (1) {\n");
		code.append("/* wait for a message */\n");
		code.append("//printf(\"path2: %in\", ep_cap_path.capPtr);\n");
		code.append("tag = seL4_Wait(ep_cap_path.capPtr, &sender_badge);\n");
		code.append("\n");
		code.append("/* make sure it is what we expected */\n");
		code.append("assert(sender_badge == EP_BADGE);\n");
		code.append("assert(seL4_MessageInfo_get_length(tag) == 1);\n");
		code.append("\n");
		code.append("/* get the message stored in the first message register */\n");
		code.append("msg = seL4_GetMR(0);\n");
		code.append("//printf(\"Got a message from %#x with content %un\", sender_badge, msg);\n");
		code.append("\n");
		code.append("/* modify the message */\n");
		code.append("seL4_SetMR(0, tot_num_ticks);\n");
		code.append("\n");
		code.append("/* send the modified message back */\n");
		code.append("//seL4_ReplyWait(ep_cap_path.capPtr, tag, &sender_badge);\n");
		code.append("seL4_Reply(tag);\n");
		code.append("}\n");
		code.append("\n");
		code.append("}\n");
		code.append("\n");
		code.append("\n");
		code.append("void launch_thread() {\n");
		code.append("int error;\n");
		code.append("/* get our cspace root cnode */\n");
		code.append("seL4_CPtr cspace_cap;\n");
		code.append("cspace_cap = simple_get_cnode(&simple);\n");
		code.append("\n");
		code.append("/* get our vspace root page directory */\n");
		code.append("seL4_CPtr pd_cap;\n");
		code.append("pd_cap = simple_get_pd(&simple);\n");
		code.append("\n");
		code.append("/* create a new TCB */\n");
		code.append("vka_object_t tcb_object = {0};\n");
		code.append("error = vka_alloc_tcb(&vka, &tcb_object);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("\n");
		code.append("vka_object_t ipc_frame_object = {0};\n");
		code.append("error = vka_alloc_frame(&vka, IPCBUF_FRAME_SIZE_BITS, &ipc_frame_object);\n");
		code.append("assert(!error);\n");
		code.append("\n");
		code.append("/*\n");
		code.append("* map the frame into the vspace at ipc_buffer_vaddr.\n");
		code.append("* To do this we first try to map it in to the root page directory.\n");
		code.append("* If there is already a page table mapped in the appropriate slot in the\n");
		code.append("* page diretory where we can insert this frame, then this will succeed.\n");
		code.append("* Otherwise we first need to create a page table, and map it in to\n");
		code.append("* the page directory, before we can map the frame in. */\n");
		code.append("\n");
		code.append("seL4_Word ipc_buffer_vaddr = IPCBUF_VADDR;\n");
		code.append("\n");
		code.append("error = seL4_ARCH_Page_Map(\n");
		code.append("ipc_frame_object.cptr,\n");
		code.append("pd_cap,\n");
		code.append("ipc_buffer_vaddr,\n");
		code.append("seL4_AllRights,\n");
		code.append("seL4_ARCH_Default_VMAttributes);\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("if (error != 0) {\n");
		code.append("\n");
		code.append("printf(\"Creating page table...\\n\");\n");
		code.append("vka_object_t pt_object = {0};\n");
		code.append("error = vka_alloc_page_table(&vka, &pt_object);\n");
		code.append("assert(!error);\n");
		code.append("\n");
		code.append("\n");
		code.append("error = seL4_ARCH_PageTable_Map(\n");
		code.append("pt_object.cptr,\n");
		code.append("pd_cap,\n");
		code.append("ipc_buffer_vaddr,\n");
		code.append("seL4_ARCH_Default_VMAttributes);\n");
		code.append("assert(!error);\n");
		code.append("\n");
		code.append("\n");
		code.append("error = seL4_ARCH_Page_Map(\n");
		code.append("ipc_frame_object.cptr,\n");
		code.append("pd_cap,\n");
		code.append("ipc_buffer_vaddr,\n");
		code.append("seL4_AllRights,\n");
		code.append("seL4_ARCH_Default_VMAttributes);\n");
		code.append("\n");
		code.append("} else {\n");
		code.append("printf(\"Initial page map succeeded...\\n\");\n");
		code.append("}\n");
		code.append("\n");
		code.append("/* set the IPC buffer's virtual address in a field of the IPC buffer */\n");
		code.append("seL4_IPCBuffer *ipcbuf = (seL4_IPCBuffer*)ipc_buffer_vaddr;\n");
		code.append("ipcbuf->userData = ipc_buffer_vaddr;\n");
		code.append("\n");
		code.append("\n");
		code.append("// error = vka_alloc_endpoint(&vka, &ep_object);\n");
		code.append("// assert(!error);\n");
		code.append("\n");
		code.append("//\n");
		code.append("// error = vka_mint_object(&vka, &ep_object, &ep_cap_path, seL4_AllRights,\n");
		code.append("//     seL4_CapData_Badge_new(EP_BADGE));\n");
		code.append("// assert(!error);\n");
		code.append("\n");
		code.append("/* initialise the new TCB */\n");
		code.append("error = seL4_TCB_Configure(tcb_object.cptr, seL4_CapNull, seL4_MaxPrio,\n");
		code.append("cspace_cap, seL4_NilData, pd_cap, seL4_NilData,\n");
		code.append("ipc_buffer_vaddr, ipc_frame_object.cptr);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("/* give the new thread a name */\n");
		code.append("name_thread(tcb_object.cptr, \"hello-3: thread_2\");\n");
		code.append("\n");
		code.append("seL4_UserContext regs = {0};\n");
		code.append("size_t regs_size = sizeof(seL4_UserContext) / sizeof(seL4_Word);\n");
		code.append("\n");
		code.append("/* set instruction pointer where the thread shoud start running */\n");
		code.append("sel4utils_set_instruction_pointer(&regs, (seL4_Word)thread_2);\n");
		code.append("\n");
		code.append("/* check that stack is aligned correctly */\n");
		code.append("uintptr_t thread_2_stack_top = (uintptr_t)thread_2_stack + sizeof(thread_2_stack);\n");
		code.append("assert(thread_2_stack_top % (sizeof(seL4_Word) * 2) == 0);\n");
		code.append("\n");
		code.append("/* set stack pointer for the new thread. remember the stack grows down */\n");
		code.append("sel4utils_set_stack_pointer(&regs, thread_2_stack_top);\n");
		code.append("\n");
		code.append("/* set the gs register for thread local storage */\n");
		code.append("regs.gs = IPCBUF_GDT_SELECTOR;\n");
		code.append("\n");
		code.append("/* actually write the TCB registers. */\n");
		code.append("error = seL4_TCB_WriteRegisters(tcb_object.cptr, 0, 0, regs_size, &regs);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("/* start the new thread running */\n");
		code.append("error = seL4_TCB_Resume(tcb_object.cptr);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("}\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("int main(void)\n");
		code.append("{\n");
		code.append("UNUSED int error;\n");
		code.append("\n");
		code.append("/* give us a name: useful for debugging if the thread faults */\n");
		code.append("name_thread(seL4_CapInitThreadTCB, \"hello-timer\");\n");
		code.append("\n");
		code.append("/* get boot info */");
		code.append("info = seL4_GetBootInfo();\n");
		code.append("\n");
		code.append("/* init simple */\n");
		code.append("simple_default_init_bootinfo(&simple, info);\n");
		code.append("\n");
		code.append("/* print out bootinfo and other info about simple */\n");
		code.append("simple_print(&simple);\n");
		code.append("\n");
		code.append("/* create an allocator */\n");
		code.append("allocman = bootstrap_use_current_simple(&simple, ALLOCATOR_STATIC_POOL_SIZE,\n");
		code.append("allocator_mem_pool);\n");
		code.append("assert(allocman);\n");
		code.append("\n");
		code.append("/* create a vka (interface for interacting with the underlying allocator) */\n");
		code.append("allocman_make_vka(&vka, allocman);\n");
		code.append("\n");
		code.append("/* create a vspace object to manage our vspace */\n");
		code.append("error = sel4utils_bootstrap_vspace_with_bootinfo_leaky(&vspace,\n");
		code.append("&data, simple_get_pd(&simple), &vka, info);\n");
		code.append("\n");
		code.append("/* fill the allocator with virtual memory */\n");
		code.append("void *vaddr;\n");
		code.append("UNUSED reservation_t virtual_reservation;\n");
		code.append("virtual_reservation = vspace_reserve_range(&vspace,\n");
		code.append("ALLOCATOR_VIRTUAL_POOL_SIZE, seL4_AllRights, 1, &vaddr);\n");
		code.append("assert(virtual_reservation.res);\n");
		code.append("bootstrap_configure_virtual_pool(allocman, vaddr,\n");
		code.append("ALLOCATOR_VIRTUAL_POOL_SIZE, simple_get_pd(&simple));\n");
		code.append("\n");
		code.append("/* use sel4utils to make a new process */\n");
		code.append("sel4utils_process_t new_process;\n");
		code.append("error = sel4utils_configure_process(&new_process, &vka, &vspace,\n");
		code.append("APP_PRIORITY, APP_IMAGE_NAME);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("/* give the new process's thread a name */\n");
		code.append("name_thread(new_process.thread.tcb.cptr, \"Thread for talking with launched processess\");\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("/* create an endpoint */\n");
		code.append("//vka_object_t ep_object = {0};\n");
		code.append("error = vka_alloc_endpoint(&vka, &ep_object);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("/* make a cspacepath for the new endpoint cap */\n");
		code.append("seL4_CPtr new_ep_cap;\n");
		code.append("vka_cspace_make_path(&vka, ep_object.cptr, &ep_cap_path);\n");
		code.append("\n");
		code.append("/* copy the endpont cap and add a badge to the new cap */\n");
		code.append("new_ep_cap = sel4utils_mint_cap_to_process(&new_process, ep_cap_path,\n");
		code.append("seL4_AllRights, seL4_CapData_Badge_new(EP_BADGE));\n");
		code.append("assert(new_ep_cap != 0);\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		code.append("/* spawn the process */\n");
		code.append("error = sel4utils_spawn_process_v(&new_process, &vka, &vspace, 0, NULL, 1);\n");
		code.append("assert(error == 0);\n");
		code.append("\n");
		code.append("vka_object_t aep_object = {0};\n");
		code.append("error = vka_alloc_async_endpoint(&vka, &aep_object);\n");
		code.append("assert(!error);\n");
		code.append("\n");
		code.append("seL4_timer_t* timer = sel4platsupport_get_default_timer(\n");
		code.append("&vka,\n");
		code.append("&vspace,\n");
		code.append("&simple,\n");
		code.append("aep_object.cptr);\n");
		code.append("\n");
		code.append("\n");
		code.append("/* we are done, say hello */\n");
		code.append("printf(\"main: hello worldn\");\n");
		code.append("\n");
		code.append("launch_thread();\n");
		code.append("\n");
		code.append("/*\n");
		code.append("* now wait for a message from the new process, then send a reply back\n");
		code.append("*/\n");
		code.append("printf(\"path: %in\", ep_cap_path.capPtr);\n");
		code.append("\n");
		code.append("// seL4_Word sender_badge;\n");
		code.append("// seL4_MessageInfo_t tag;\n");
		code.append("// seL4_Word msg;\n");
		code.append("\n");
		code.append("// /* wait for a message */\n");
		code.append("// tag = seL4_Wait(ep_cap_path.capPtr, &sender_badge);\n");
		code.append("\n");
		code.append("// /* make sure it is what we expected */\n");
		code.append("// assert(sender_badge == EP_BADGE);\n");
		code.append("// assert(seL4_MessageInfo_get_length(tag) == 1);\n");
		code.append("\n");
		code.append("// /* get the message stored in the first message register */\n");
		code.append("// msg = seL4_GetMR(0);\n");
		code.append("// printf(\"main: got a message from %#x to sleep %u seconds\\n\", sender_badge, msg);\n");
		code.append("\n");
		code.append("// /* modify the message */\n");
		code.append("// seL4_SetMR(0, tot_num_ticks);\n");
		code.append("\n");
		code.append("// /* send the modified message back */\n");
		code.append("// seL4_ReplyWait(ep_cap_path.capPtr, tag, &sender_badge);\n");
		code.append("\n");
		code.append("seL4_Word sender;\n");
		code.append("\n");
		code.append("/*\n");
		code.append("* The PIT only has 16-bit count down register, we cannot make it\n");
		code.append("* sleep for 1 second, instead, we make PIT to fire an interrupt every\n");
		code.append("* one millisecond, and count for 1000 times to make one second.\n");
		code.append("*/\n");
		code.append("int time_to_sleep_milli = 200;\n");
		code.append("int num_prints = 0;\n");
		code.append("while (1) {\n");
		code.append("int num_ticks = 0;\n");
		code.append("while (1) {\n");
		code.append("timer_oneshot_relative(timer->timer, 1000*1000 /*nanoseconds*/);\n");
		code.append("seL4_Wait(aep_object.cptr, &sender);\n");
		code.append("sel4_timer_handle_single_irq(timer);\n");
		code.append("num_ticks++;\n");
		code.append("tot_num_ticks++;\n");
		code.append("if (num_ticks == time_to_sleep_milli) break;\n");
		code.append("}\n");
		code.append("printf(\"Hello %4i\\n\", ++num_prints);\n");
		code.append("}\n");
		code.append("\n");
		
		systemImplementation.userProcessSubcomponents.forEach [processSubcomponent |	
		val processImpl = processSubcomponent.componentImplementation as ProcessImplementation
		if (processSubcomponent.name.equalsIgnoreCase("loader") || processSubcomponent.name.equalsIgnoreCase("scheduler"))
		{
			return;
		}
		
		code.append ("  /* load partition " + processImpl.name + "*/\n")		
		code.append ("	launch_partition (\"processImpl\");\n");			
		
		]
		
		code.append("	return 0;\n");
		code.append("}\n");
		code.append("\n");		 
		systemImplementation.userProcessSubcomponents.forEach [userProcess |
			code.append ("/*loading user process " + userProcess.name + "\n*/")
 		]
		
		Utils.write(systemImplementation, "loader" + File.separator + "loader.c" , code.toString)
	}
	
	def static void generateLoaderMakefile (SystemImplementation systemImplementation)
	{
		val StringBuffer makefile = new StringBuffer()
		
		makefile.append("TARGETS := $(notdir $(SOURCE_DIR)).bin\n");

		makefile.append("ENTRY_POINT := _sel4_start\n");

		makefile.append("CFILES   := $(patsubst $(SOURCE_DIR)/%,%,$(wildcard $(SOURCE_DIR)/src/*.c))\n");
		makefile.append("CFILES   += $(patsubst $(SOURCE_DIR)/%,%,$(wildcard $(SOURCE_DIR)/src/plat/${PLAT}/*.c))\n");
		makefile.append("CFILES   += $(patsubst $(SOURCE_DIR)/%,%,$(wildcard $(SOURCE_DIR)/src/arch/${ARCH}/*.c))\n");

		makefile.append("OFILES := archive.o\n");

		makefile.append("ASMFILES := $(patsubst $(SOURCE_DIR)/%,%,$(wildcard $(SOURCE_DIR)/src/arch/${ARCH}/*.S))\n");
		makefile.append("ASMFILES += $(patsubst $(SOURCE_DIR)/%,%,$(wildcard $(SOURCE_DIR)/src/plat/${PLAT}/*.S))\n");

		makefile.append(
			"LIBS = c sel4 sel4muslcsys sel4simple sel4vka sel4allocman sel4vspace sel4platsupport platsupport sel4utils cpio elf utils\n"
		);

		makefile.append("ifdef CONFIG_KERNEL_STABLE\n");
		makefile.append("LIBS += sel4simple-stable\n");
		makefile.append("else\n");
		makefile.append("LIBS += sel4simple-default\n");
		makefile.append("endif\n");

		makefile.append("CFLAGS += -g\n");
		makefile.append("#CFLAGS += -Werror -g\n");

		makefile.append("include $(SEL4_COMMON)/common.mk\n");

		makefile.append("${COMPONENTS}:\n");
		makefile.append("\tfalse\n");

		makefile.append("archive.o: ${COMPONENTS}\n");
		makefile.append("\t$(Q)mkdir -p $(dir $@)\n");
		makefile.append("\n${COMMON_PATH}/files_to_obj.sh $@ _cpio_archive $^\n");
		Utils.write(systemImplementation, "loader" + File.separator + "Makefile", makefile.toString);


		val StringBuffer kbuild = new StringBuffer()
		kbuild.append("apps-$(CONFIG_APP_LOADER) += one-printer\n");

		kbuild.append("one-printer-y = common libsel4 libmuslc libsel4muslcsys libsel4simple libsel4vka libsel4allocman libsel4vspace libsel4platsupport libplatsupport libsel4utils libcpio libelf libutils\n");


		kbuild.append("ifdef CONFIG_KERNEL_STABLE\n");
		kbuild.append("loader-$(CONFIG_LIB_SEL4_SIMPLE_STABLE) += libsel4simple-stable\n");
		kbuild.append("else\n");
		kbuild.append("loader-$(CONFIG_LIB_SEL4_SIMPLE_DEFAULT) += libsel4simple-default\n");
		kbuild.append("endif\n");

		kbuild.append("loader-components-y += scheduler\n");
		kbuild.append("loader-components = $(addprefix $(STAGE_BASE)/bin/, $(loader-components-y))\n");

		kbuild.append("loader: export COMPONENTS=${loader-components}\n");
		kbuild.append("loader: ${loader-components-y} kernel_elf $(loader-y)\n");
		
		Utils.write(systemImplementation, "loader" + File.separator + "Kbuild", kbuild.toString);


		val StringBuffer kconfig = new StringBuffer()
		kconfig.append("menuconfig APP_LOADER\n");
    	kconfig.append("\tbool \"seL4 Loader for generated app\"\n");
    	kconfig.append("\tdefault n\n");
    	kconfig.append("\tdepends on LIB_SEL4 && (LIB_MUSL_C || LIB_SEL4_C) && LIB_SEL4_SIMPLE && LIB_SEL4_VKA && LIB_SEL4_ALLOCMAN && LIB_SEL4_VSPACE && LIB_SEL4_PLAT_SUPPORT && LIB_PLATSUPPORT && LIB_SEL4_UTILS && LIB_CPIO && LIB_ELF && LIB_UTILS\n");
    	kconfig.append("\thelp\n");
		kconfig.append("\t\tMain application that will load other partitions \n");
		Utils.write(systemImplementation, "loader" + File.separator + "Kconfig", kconfig.toString);
	}	
}