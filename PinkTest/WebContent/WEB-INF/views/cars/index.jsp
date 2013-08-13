<%@ taglib uri="http://mobiarch" prefix="p" %>

<html>
<body>

<p:form action="cars/makeSelection">
<p:select name="selectedModel" items="availableModels" 
    itemValue="id" itemLabel="modelName" size="5" multiple="multiple"/><br/>
<input type="submit" value="Select Model"/>
</p:form>
<p>Nested property test</p>
<p:form action="cars/makeNestedSelection">
<p:select name="carModel.id" items="availableModels" 
    itemValue="id" itemLabel="modelName" size="5" multiple="multiple"/><br/>
<input type="submit" value="Select Model"/>
</p:form>

</body>
</html>