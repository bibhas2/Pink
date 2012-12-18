<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag dynamic-attributes="dynattrs" %> 
<%@tag import="com.mobiarch.nf.PropertyManager, java.lang.reflect.Array"%>
<%@attribute name="name" required="true"  %>
<%@attribute name="items" required="false"  %>
<%@attribute name="itemValue" required="false"  %>
<%@attribute name="itemLabel" required="false"  %>
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
if (items == null) {
	//We will not render <option> from here. They will be
	//rendered by <p:option> tag.
	if (val != null) {
		request.setAttribute("selectValue", val);
		request.setAttribute("selectValueDesc", pm.getPropertyDescriptor(cls, name));
	}
}
%>
<select <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> name="${name}">
<c:if test="${!empty items}">
	<c:forEach items="${formBean[items]}" var="itemObject">
<%
jspContext.setAttribute("selectionFlag", false);
Object itemObj = jspContext.getAttribute("itemObject");
Object rowValue = pm.getProperty(itemObj.getClass(), itemObj, itemValue, false);
//Compare the value and determin the selection flag
if (val != null) {
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
}
jspContext.setAttribute("rowValue", rowValue);
%>
	<option value="${rowValue}" <c:if test="${selectionFlag}">selected="selected"</c:if>>${itemObject[itemLabel]}</option>
	</c:forEach>
</c:if>
<jsp:doBody/>
</select>
<%
if (items == null) {
	if (val != null) {
		request.removeAttribute("selectValue");
		request.removeAttribute("selectValueDesc");
	}
}
%>