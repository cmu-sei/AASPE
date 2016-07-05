# Eclipse Extensions
Custom attack vulnerability and propagation providers are supplied using Eclipse's extension mechanism.  This documentation assumes that the reader is familiar with writing extensions.  If not, then the reader is recommended to read through Eclipse's [Simple plug-in example](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwjyv9e_leTMAhXDox4KHessBDQQFggcMAA&url=http%3A%2F%2Fhelp.eclipse.org%2Fmars%2Ftopic%2Forg.eclipse.platform.doc.isv%2Fguide%2Ffirstplugin.htm&usg=AFQjCNHqkuzcPbsnS69WJL9b7uFLyz1LIQ&sig2=dvtVaWhMpEC2xu518-zUsg).

# Attack Vulnerability
To contribute a custom attack vulnerability provider, use the extension point `edu.cmu.sei.aaspe.vulnerability`.  An extention can list multiple `vulnerability` entries.  Each `vulnerability` must define a value for the `class` attribute which is a subtype of `edu.cmu.sei.aaspe.vulnerabilities.AbstractVulnerability`.  Here is an example of a vulnerability contribution in a `plugin.xml` file:
```xml
<extension
		point="edu.cmu.sei.aaspe.vulnerability">
	<vulnerability
		class="edu.cmu.sei.aaspe.vulnerabilities.Exposition">
	</vulnerability>
</extension>
```
When generating an attack impact model, the `findVulnerabilities(ComponentInstance)` method of each contributed vulnerability provider class will be called and the returned vulnerablities will be added to the attack impact model.

The following is an example of a vulnerability provider:
```java
public class Authentication extends AbstractVulnerability {
	@Override 
	public List<Vulnerability> findVulnerabilities(ComponentInstance component) {
		List<Vulnerability> vulnerabilities;
		vulnerabilities = new ArrayList<Vulnerability> ();

		/**
		 * Check that is the component is connected to another
		 * component through a physical bus, it encrypts the data
		 * sent over the bus.
		 * 
		 * We also check if the encryption algorithm is not too weak
		 * in case encryption is used.
		 */
		for (ConnectionInstance ci : component.getConnectionInstances()) {
			ComponentInstance componentDestination;
			ComponentInstance componentSource;
			
			/**
			 * now, we just take care of of port connection
			 */
			if (ci.getKind() != org.osate.aadl2.instance.ConnectionKind.PORT_CONNECTION) {
				continue;
			}

			componentSource = ci.getSource().getContainingComponentInstance();
			componentDestination = ci.getDestination().getContainingComponentInstance();

			/**
			 * isPhysical is a boolean that details if the connection is associated
			 * with a physical bus.
			 * 
			 * isEncrypted specifies if the connection is using an encryption
			 * algorithm. Which means associated with a virtual bus that specifies
			 * an encryption mechanism.
			 */
			boolean isPhysical = ComponentUtils.isPhysical (ci);
			boolean useAuthentication = ComponentUtils.useAuthentication(ci);
			

			if (isPhysical && (!useAuthentication ) ) {
				OsateDebug.osateDebug("Authentication", "connection " + ci.getName() + " does not use authentication");

				Vulnerability v = new Vulnerability();
				v.setType(Vulnerability.VULNERABILITY_AUTHENTICATION);
				v.setName ("Missing Authentication");
				v.setComment("missing authentication on connection " + ci.getName());
				v.setRelatedElement(componentSource);
				v.addPropagations(PropagationModel.getInstance().findPropagations (componentDestination));		
				vulnerabilities.add (v);
			}

			if ( isPhysical && useAuthentication && edu.cmu.sei.aaspe.utils.Utils.containsWeakAuthentication(ComponentUtils.getAuthenticationMethods(ci)))
			{
				Vulnerability v = new Vulnerability();
				v.setType(Vulnerability.VULNERABILITY_AUTHENTICATION);
				v.setName ("Weak Authentication");
				v.setComment("weak authentication method used on " + ci.getName());
				v.setRelatedElement(componentSource);
				v.addPropagations(PropagationModel.getInstance().findPropagations (componentDestination));				
				vulnerabilities.add (v);
			}
		}

		return vulnerabilities;
	}
}
```
When creating an instance of `Vulnerability`, it is important to call `PropagationModel.getInstance().findPropagations()` and pass the result to the new vulnerability's `addPropagations` method.

# Attack Propagation
To contribute a custom attack propagation provider, use the extension point `edu.cmu.sei.aaspe.propagation`.  An extension can list multiple `propagation` entries.  Each `propagation` must define a value for the `class` attribute which is a subtype of `edu.cmu.sei.aaspe.propagations.AbstractPropagation`.  Here is an example of a propagation contribution in a `plugin.xml` file:
```xml
<extension
		point="edu.cmu.sei.aaspe.propagation">
	<propagation
		class="edu.cmu.sei.aaspe.propagations.DataFlow">
	</propagation>
</extension>
```
When generating an attack impact model, the `getPropagations(ComponentInstance)` method of each contributed propagation provider class will be called and the returned propagations will be added to the attack impact model.

The following is an example of a propagation provider:
```java
public class SharedData extends AbstractPropagation {
	@Override
	public List<Propagation> getPropagations(ComponentInstance component)
	{
		ArrayList<Propagation> result;
		
		result = new ArrayList<Propagation> ();
		
		/**
		 * If the component impacted is a data, it can affect all components sharing the data.
		 */
		if (component.getCategory() == ComponentCategory.DATA)
		{
			for (ComponentInstance dataUser : ComponentUtils.getDataUsers(component))
			{
				Propagation newPropagation = new Propagation(component, Propagation.PROPAGATION_DATA, dataUser);
				if (! result.contains(newPropagation))
				{
					result.add(newPropagation);
				}
			}
		}
		
		return result;
	}
}
```