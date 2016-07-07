
# Modeling Rules

## General
The following rules applies all system being generated:

 * Each process **must be** bound to a **virtual processor**
 * A **virtual processor** must be bound to a processor
 * The **processor** component must declare the scheduling policy using the ARINC653 properties: **arinc653::module_major_frame** and **arinc653::module_schedule**. Each virtual processor must be specified.
 * Each **virtual processor** is bound only to exactly **one** process
 * **Data** are communicated using data ports connected among **thread**, **process** and **device** components.
 * Every system being generated must contain only **one processor**
 * Data must specify the **source_name** property that specifies the C type they are mapped to.

## Partition Specific
 * Each **thread** component must declare the following properties: **Period**, **Deadline**
 * Each **thread** can be only periodic or sporadic. The *dispath_protocol* property **must be** set to one of these values.
 * Each **process** can contain **only one** thread
 * Subprograms components must define the following properties **source_language**, **source_name** and **source_text**.

## Device specific
 * An AADL device component is mapped into a hardware and a regular component.
 * The AADL device specifies its memory/configuration requirements (memory, etc.) using data ports connected between the **virtual processor** bound to the device and the **processor** that represents the underlying operating system. The port on the processor must have the property **Base_Address** defined in order to specify the memory address of the device register.
 * An example of a use of a device driver can be found with the [beaglebone-uart example](https://github.com/cmu-sei/AASPE/blob/master/edu.cmu.aaspe.examples/code-generation/beaglebone-serial.aadl). This example defines a system that interact through a serial driver.
 * A device must defines the **Period** and **Deadline** properties
 * The device must specify the c source file that implement the driver using the **source_text** property
 * In the subprograms that implement the device, there must be a function called **driver()**. It takes the same arguments that the AADL component.

# Transformation Patterns

## CAmkES transformation
The CAmkES description specifies the architecture with the organization of the partitions and their interaction. One CAmkES description is produced for each processor. The following elements are used when creating the CAmKES description for a particular processor:

 * A partition is created for each virtual processor bound to the processor.
 * Each data connection between AADL processes is transformed into a CAmkES connection between partitions. The receiving partition has the right to read only while the sending partition can only send through this connection
 * The period and execution time is specified for each partitions using the timing description of the system (properties *arinc653::module_schedule* and *arinc653::module_major_frame*)

In addition, the code generator produces the following extra code (not described in the AADL model)
 * A partition that manage the time/clock and activate every partition
 * A connection between the time management partition and other partitions to activate them.

## Partition code
The partition code receives the data from other partitions, execute subprograms and send the data to other partitions. A partition is specified with an AADL *process* component bound to an AADL *virtual processor* component.

The generator creates one directory and one CAmkES component for each partition. It specified the CAmkES description of the partition as well as the C source code that implements the partition.

The main loop of the code is as follows
```c

void run ()
{
   while (1)
   {
      wait_for_activation();
      receive_inputs();
      execute_subprograms();
      send_output();
   }
}
```

The goal of the *wait_for_activation()* part is to wait for a signal from the time management partition to activate the task.

The goal of the *receive_inputs()* part is to receive data **before** executing the subprograms. The data from the subprograms might depend on this data.

The goal of the *execute_subprograms()* part is to execute all the subprograms specified in the AADL model.

The goal of the *send_output()* part is to send data to the other partitions before the partition completes its period.


# Unsupported AADL elements
The following AADL elements are not used by the code generator. They will just be ignored by the AADL model.
 * *event ports* between threads
 * AADL modes
 * *event data ports* between threads
 * *memory* components bound to a process
 * *device* components
 * *data* shared between threads or processes
 * Only C subprograms are supported
