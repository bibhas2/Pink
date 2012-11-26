<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag dynamic-attributes="dynattrs" %> 
<%@tag import="com.mobiarch.nf.PropertyManager, java.lang.reflect.Array"%>
<%@attribute name="name" required="true"  %>
<%@attribute name="items" required="true"  %>
<%@attribute name="itemValue" required="true"  %>
<%@attribute name="itemLabel" required="true"  %>
<%
Object formBean = request.getAttribute("formBean");
Class<?> cls = (Class<?>) request.getAttribute("formBeanClass");
PropertyManager pm = new PropertyManager();
Object val = pm.getProperty(cls, formBean, name, false);
boolean isArray = false;
int arrayLength = 0;
if (val != null) {
	isArray = val.getClass().isArray();
	if (isArray) {
		arrayLength = Array.getLength(val);
	}
}
%>
<select <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> name="${name}">
	<c:forEach items="${formBean[items]}" var="itemObject">
<%
jspContext.setAttribute("selectionFlag", false);
Object itemObj = jspContext.getAttribute("itemObject");
Object rowValue = pm.getProperty(itemObj.getClass(), itemObj, itemValue, false);
if (isArray) {
	for (int i = 0; i < arrayLength; ++i) {
		if (Array.get(val, i).equals(rowValue)) {
			jspContext.setAttribute("selectionFlag", true);
		}
	}
} else {
	if (rowValue.equals(val)) {
		jspContext.setAttribute("selectionFlag", true);
	}
}
jspContext.setAttribute("rowValue", rowValue);
%>
	<option value="${rowValue}" <c:if test="${selectionFlag}">selected="selected"</c:if>>${itemObject[itemLabel]}</option>
	</c:forEach>
</select>