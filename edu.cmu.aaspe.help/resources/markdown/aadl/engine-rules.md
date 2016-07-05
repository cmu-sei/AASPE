The following sections explains the rules used by the engine to create and populate the Attack Impact model from an AADL model. First, we explain all the vulnerabilities detected by the engine and what are the modeling patterns under analysis. Then, we detail how the engine find vulnerability propagation paths within the architecture


# Vulnerabilities


The rules are implemented in the package *edu.cmu.sei.aaspe.vulnerabilities*. There is one class per type of vulnerability. Currently, the tool detect the following vulnerabilities with these associated rules:

 * **Authentication**: when a port connection using a physical bus (e.g. Ethernet) is not using authentication mechanisms, the tool reports a vulnerability. Authentication is specified using the property *security_properties::authentication_method* on a port connection bound to a physical bus component.
 * **Concurrency**: when a data component shared between several thread is not protected through locking (e.g. mutex) mechanisms, the tool reports a vulnerability. The use of a locking mechanism is specified using the core AADL property *Concurrency_Control_Protocol*.
 * **Encryption**: when a port connection using a physical bus (e.g. Ethernet) is not using encryption mechanisms, the tool reports a vulnerability. Encryption is specified using the property *security_properties::encryption* on a port connection bound to a physical bus component.
 * **Exposition**: when a component is not physically totally protected, the tool reports an error. The physical exposure is specified with the property *security_properties::exposure*. A value of 0 means there is no physical exposure and will not create any vulnerability. Any value between 0 and 100 will generate a new vulnerability.
 * **ResourceDimension**: when components are connected through *event* ports or *event data* ports, the tool check that the receiving component has a queue with the correct dimensions and is running at a sufficient rate to receive all data. The tool computes the number of data items sent by the sender. Then, it computes the number of items the receiver can process according to the queue_size of the incoming port and the execution period of the receiving component. If the receiving component might miss a data value, a vulnerability is created. The period is specified usign the core property *period*. The queue size of the port is specified using the property *queue_size*.
 * **SecurityDomain**: when components share different security domains but are not trusted, the tool generates a vulnerability. The rationale is that components sharing different security levels must be trusted so that they enforce the security policy and the isolation requirements between domains. If the component is not trusted, the tool generates a vulnerability. The level of trust of the component is specified using the property *security_properties::trust*. A value of 100 means the component is trusted while a value of 0 means the component cannot be trusted.
 * **SecurityLevel**: when components using different security levels are communicating, the tool check that the receiving components has a similar or higher security level than the sending component. The security level is specified using the property *security_properties::security_levels*. The property value is a list of all levels handled by the component. The value 0 means the component is highly classified while a value of 100 means the component is unclassified.

# Propagations


The rules are implemented in the package edu.cmu.sei.aaspe.propagations. There is one class per type of propagation. The current tool detect the following propagations using these associated rules:

 * **DataFlow**: a propagation is added between every component connected using AADL ports (AADL data ports, etc.). Is shows that a vulnerability can be propagated through data flows.
 * **Process to Thread**: a propagation is added between every process and their contained thread.
 * **Shared Bus**: a propagation is added to every component that shares a bus using bus access features.
 * **Shared Data**: a propagation is added to every component that share a data (global variable) using data accesses features.
 * **Shared Memory**: a propagation is added to every component that shares the same memory component. To detect the use of a memory component by a particular component, the engine uses the *Actual_Memory_Binding* property.
 * **Shared Processor**: a propagation is added to every process that shares the same processor. To detect how processes are associated to a processor, the engine uses the *Actual_Processoring_Binding* AADL property.
 * **Thread to Thread**: a propagation is added to every thread in the same process.
