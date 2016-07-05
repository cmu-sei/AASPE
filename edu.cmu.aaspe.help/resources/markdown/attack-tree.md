# Attack Tree Editor

## What it is?
An Attack-Tree is a graphical model that show all contributor of a successful compromising of a system. It uses a tree notation with the root being the system being compromised and the leafs being the vulnerabilities or the nodes that propagates the vulnerabilities or attacks.

Such a diagram shows all the contributors that can facilitate a successful attack against a given system. It also helps to understand the path from a vulnerability to the impacted system.


## Implementation Concerns
Our Attack Tree editor relies on an EMF meta-model of an attack tree as well as a Sirius representation for it. The Sirius representation generates the graphical representation of the tree from the meta-model.


## Examples of Attack Tree
To open an example of an Attack Tree, do the following

 * Create a new project by using *New* -> *Project*. Select the name of the project and click on *Finish*

![Creating a New Project in Eclipse](imgs/new-project.png "New Project")

 * Right click on the new project and select *New* -> *Other* and select

 ![Creating a New Project in Eclipse](imgs/new-project-others.png "New Project Other")

 * Select the *Attack Tree Wizard*

 ![Creating a New Project in Eclipse](imgs/attacktree-wizard1.png "New Project Other")

 * Choose the example you want, indicate a file name and click on Finish.

 ![Creating a New Project in Eclipse](imgs/attacktree-wizard2.png "New Project Other")


## Visualizing the Attack Tree

 * Make sure you have your Attack Tree file in your project. The file has the extension *attacktree*. See the previous section to create an attacktree example.
 * Switch to the Modeling Perspective. Check that the modeling perspective is activated as in the following picture. The icon is on the top right in the eclipse main window.

 ![Creating a New Project in Eclipse](imgs/modeling-perspective.png "New Project Other")

 * Make sure the project has the Modeling Nature. Right Click on your project and check the item *Configure* -> *Convert to Modeling Project*.

 ![Creating a New Project in Eclipse](imgs/convert-modeling.png "New Project Other")

 * Select the Attack Tree Viewpoint. Right click on te project and select the following menu

 ![Creating a New Project in Eclipse](imgs/viewpoint-selection.png "New Project Other")

 * Select the Attack Tree Viewpoint

 ![Creating a New Project in Eclipse](imgs/viewpoint-selection-attacktree.png "New Project Other")

 * Finally, open the Attack Tree Representation. Select the model element (the Attack Tree Model EObject) and right click on it. Select *New Representation* and then *new AttackTreeDiagram*.

  ![Creating a New Project in Eclipse](imgs/new-attack-tree-diagram.png "New Project Other")

 * You should then get the following diagram.

  ![Creating a New Project in Eclipse](imgs/medical-attracktree.png "New Project Other")

## Editing an Attack Tree

This section has to be completed.

## Contact and Help
For any help and contact, please send an e-mail to Julien Delange <jdelange@sei.cmu.edu>
