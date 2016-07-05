The seL4 code generator creates code for the seL4 kernel from AADL models. seL4 is the first formally verified kernel and is designed to build reliable, safe and secure applications.

# How it works?
When generating the code, the generator produces two types of code:
 * **CAmkES Architecture**: corresponds to the system description specified with the CAmkES ADL. There is only one CAmkES description per processor. The CAmkES description contains the system timing requirements (period, deadline) as well as the communication (connections) between partitions.
 * **Partition code**: the code for each partition that receives data from other partitions (is there some incoming data ports), execute the subprograms and finally, send output data.

# Requirements
In order to use the code generator, you need:
 * OSATE: if you read this, you probably already have a version with OSATE that supports our code generator features.
 * A working development environment for compiling an CAmkES/seL4 application. You can read more about CAmkES and setting up a [development environment on the CAmkES website](https://wiki.sel4.systems/CAmkES).

# Supported Platforms
Because the code generator must produce platform-dependent code, it only supports few platforms:
 * **x86**
 * **Beaglebone Black**
 * **ARM kzm**
 * **Tegra K1**

The platform used is specified in the AADL model with the property *sei::platform* on the processor component. For more details, please review the code generator patterns.


# Implementation Considerations
The code generator leverages the Xtend and Xtext generation framework. Our seL4 code generator is based on the Xtext code generator templates. You do not need any third party tool to generate the code. Once you generate the code, you need to import it
