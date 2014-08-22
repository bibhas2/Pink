##Overview
Pink is a modern web framework for the Java EE 6 stack. For usage details and tutorials, go
to the [Project's Home Page](http://mobiarch.wordpress.com/pink/).

##Projects in this repository

- *Pink* - The framework.
- *DTS*- Sample app Defect Tracking System.
- *FileDemo* - Sample app that shows file upload.
- *StoreDemo* - Sample app online retail store.
- *PinkTest* - Various tests.
- *AjaxDemo* - Sample app that shows Ajax capabilities of Pink

##Building Instructions

Each project is a Maven Eclipse project. That means, you can build them from command line
or from Eclipse. Quickest way is command line.

First, clone the repository:

```
git clone https://github.com/bibhas2/Pink.git
```
	
Go into Pink folder and build/install the framework:

```
cd Pink/Pink
mvn clean install
```

Next build the sample applications. For example, to build FileDemo:

```
cd FileDemo
mvn clean package
```

Deploy the sample app to a full stack Java EE server, such as TomEE.

```
cp FileDemo-0.0.1-SNAPSHOT.war ~/Programs/apache-tomee-jaxrs-1.5.2/webapps/FileDemo.war
```

Read each sample project's README to find out how to setup and use the application.

##Creating a Project that Uses Pink
The quickest way to create a new Pink based web project is to use the Maven pink-archetype archetype.
See the [PinkArchetype] (https://github.com/bibhas2/PinkArchetype) repository for instructions.
