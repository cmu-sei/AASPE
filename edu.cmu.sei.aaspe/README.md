Automated Assurance of Security Policy Enforcement (AASPE)
=========================================================

Overview
--------
This plugin implements AADL analysis tools for the Automated
Assurance of Security Policy Enforcement project. The tool
implements the following capabilities:
* Generate of Surface Attack
* Generate of Attack Impact Analysis

# Usage
To use the tool, select a system instance (AADL model being
instantiated previously) and invoke the security tools.

# TODO
* Propagations for memories

# Attack Surface
The attack surface determines what are the entry point of your system
that can be used by potential attackers. The tool automatically
generates the attack surface from the AADL model. The attack surface
generation tool processes the AADL and for each component
that may be a weakness in the system and could expose the system
to a security threat. For each component being considered as a
security threat, the tool analyzes the impact of a potential attack
and how the compromised component could then impact the rest of the
system through its connections, bindings and other
association within the architecture.

## Weaknesses under consideration
The following weaknesses are being considered by the attack surface
generation tool:
* Physical exposure of a component, especially processor, device,
  bus or memory (property security_properties::exposure)
* No use of encryption on a connection (e.g. data being communicated
  can be compromised) - use of security_properties::encryption
* Process on the same processor with different security levels: the
  lower security-level process can then use the communication
  from the higher security levels
* Processor with an operating system having known vulnerabilities
