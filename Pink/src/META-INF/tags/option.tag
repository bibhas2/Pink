<%@tag import="java.beans.PropertyDescriptor,com.mobiarch.nf.PropertyManager, java.lang.reflect.Array"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag dynamic-attributes="dynattrs" %><%@attribute name="value" required="true"%><%
boolean isArray = false;
int arrayLength = 0;
Object selectValue = request.getAttribute("selectValue");
jspContext.setAttribute("selectionFlag", false);
//Compare the value and determin the selection flag
if (selectValue != null) {
	PropertyDescriptor desc = (PropertyDescriptor) request.getAttribute("selectValueDesc");
	//Convert the String value to the data type of select's bound property
	PropertyManager pm = new PropertyManager();
	Class<?> propertyType = desc.getPropertyType();
	if (propertyType.isArray()) {
		propertyType = propertyType.getComponentType();
		isArray = true;
		arrayLength = Array.getLength(selectValue);
	}
	Object valueConverted = pm.convertFromString(value, propertyType, desc);
	if (isArray) {
		for (int i = 0; i < arrayLength; ++i) {
			if (Array.get(selectValue, i).equals(valueConverted)) {
				jspContext.setAttribute("selectionFlag", true);
				break;
			}
		}
	} else {
		if (selectValue.equals(valueConverted)) {
			jspContext.setAttribute("selectionFlag", true);
		}
	}
}
%><option<c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> value="${value}"<c:if test="${selectionFlag}"> selected="selected"</c:if>><jsp:doBody/></option>