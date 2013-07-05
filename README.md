# Domain Browser

A small administrative application that allows you to browse the existing domain objects, and view their contents and relations.

---
## Change Logs:

### v1.4.0

**New Features:**  
	The input field that searches an object by its ID now allows to search for class names (including packages)  

**Enhancements:**  
	Added a header and labels to explain the columns of the table in the DomainClassListView  



### v1.3.0

**New Features:**  
	Added a new view that displays a list of all existing domain classes, the number of instances, predicates and inconsistencies for each class.  

**Enhancements:**  
	Simpified the code of the DomainBrowser and the Quote for clarity, and added a few more quotes  



### v1.2.0  
**Enhancements:**  
	Upgraded to the Fenix-Framework 2 API

### v1.1.0

**New Features:**  
	The DomainObjectView component now shows all the consistency predicates of an object, and their result  
	The predicates of an object now display the list of depended objects, if any  

**Bug Fixes:**  
	Prevented an infinite loop bug in the attach method of the DomainBrowser component  
	Prevented an exception while trying to browse objects with incoming unidirectional relations  

**Enhancements:**  
	Minor presentation improvements  



### v1.0.0

**New Features:**  
	Added a button to browse a domain object, given it's ExternalID  
	Added a diagram to display the class hierarchy of the object being shown  
	Added a table to display the value types of the object being shown  
	Added a table to display the to-one-relations, with links to browse the objects on the other end of each relation  
	Added a table to display the playsRole names of the to-many-relations, with links to view the contents of each relation  
	Added a table to display the contents of the to-many-relations, with links to browse the objects on the other end of the relation  

**Enhancements:**  
	Formatted and refactored the code, renamed a few classes and methods for clarity  
	Added the name of the relation list that is being displayed, and made the relation list contents table lazy-loaded.  
	Added a random quote-generator  

