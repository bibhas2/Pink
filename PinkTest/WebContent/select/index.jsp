<%@ taglib uri="http://mobiarch" prefix="p" %>

<html>
<body>

<p:form action="select/optionTest">
Multi selection:<br/>
<p:select name="selectedValue" size="5" multiple="multiple">
	<p:option value="1">One</p:option>
	<p:option value="2">Two</p:option>
	<p:option value="3">Three</p:option>
	<p:option value="4">Four</p:option>
</p:select>
<br/>
Single selection: <br/>
<p:select name="singleSelectedValue" size="5">
	<p:option value="ON">One</p:option>
	<p:option value="TW">Two</p:option>
	<p:option value="TH">Three</p:option>
	<p:option value="FO">Four</p:option>
</p:select>

<input type="submit" value="Select Test"/>
</p:form>
</body>
</html>