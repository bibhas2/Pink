<%@tag import="java.util.logging.Logger"%>
<%@tag import="com.mobiarch.nf.PropertyManager,java.lang.reflect.Array"%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag dynamic-attributes="dynattrs" %> <%@attribute name="name" required="true"%><%@attribute name="type" required="false"%><%@attribute name="value" required="false"%><%@attribute name="id" required="false"%><%@attribute name="label" required="false"%>
<%
Logger logger = Logger.getLogger("com.mobiarch.nf.tags");
logger.fine("Rendering input tag. Name: " + name);
Object formBean = request.getAttribute("formBean");
Class<?> cls = (Class<?>) request.getAttribute("formBeanClass");
PropertyManager pm = new PropertyManager();
Object propertyValue = pm.getProperty(cls, formBean, name);
Object useValue = null;

if (type == null) {
	type = "text";
	jspContext.setAttribute("type", type);
}
String qualifier = "";
if (type.equals("checkbox") || type.equals("radio")) {
	logger.fine("Special processing for checkbox and radio. Name: " + name);
	if (value == null) {
		throw new IllegalStateException("Must supply value attribute for checkbox and radio input types");
	}
	//For checkbox and radio property value may be an array.
	if (propertyValue != null && propertyValue.getClass().isArray()) {
		logger.fine("Property is array.");
		//Iterate through the property values and look for a match
		int length = Array.getLength(propertyValue);
		for (int i = 0; i < length; ++i) {
			Object item = Array.get(propertyValue, i);
			if (item != null && item.equals(value)) {
				qualifier = "checked=\"checked\"";
				break;
			}
		}
	} else if (value != null && value.equals(propertyValue)) {
		logger.fine("Property not array.");
		qualifier = "checked=\"checked\"";
	}
	//Use the value supplied in input tag.
	//This only happens for checkbox and radio.
	useValue = value;
	
	if (label != null) {
		//We need an ID for label. Generate one if needed.
		if (id == null || id.length() == 0) {
			id = name + "." + value;
			jspContext.setAttribute("id", id);
		}
	}
} else {
	if (propertyValue != null && propertyValue.getClass().isArray()) {
		throw new IllegalStateException("Array property can be bound to checkbox and radio input types only.");
	}
	useValue = propertyValue;
}
if (useValue == null) {
	useValue = "";
}
%>
<input<c:if test="${!empty id}"> id="<%=id%>"</c:if><c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> <%=qualifier%> type="${type}" value="<%=useValue%>" name="${name}"/>
<c:if test="${!empty label}"><label for="<%=id%>">${label}</label></c:if>