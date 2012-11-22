<%@tag import="com.mobiarch.nf.PropertyManager"%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag dynamic-attributes="dynattrs" %> <%@attribute name="name" required="true"%><%@attribute name="type" required="false"%><%@attribute name="value" required="false"%><%@attribute name="id" required="false"%><%@attribute name="label" required="false"%>
<%Object formBean = request.getAttribute("formBean");
Class<?> cls = (Class<?>) request.getAttribute("formBeanClass");
PropertyManager pm = new PropertyManager();
Object val = pm.getProperty(cls, formBean, name);
val = val != null ? val : "";
if (type == null) {
	type = "text";
	jspContext.setAttribute("type", type);
}
String qualifier = "";
if (type.equals("checkbox") || type.equals("radio")) {
	if (value != null && value.equals(val)) {
		qualifier = "checked=\"checked\"";
	}
	//Prserve value if supplied
	if (value != null) {
		val = value;
	}
	if (label != null) {
		//We need an ID for label. Generate one if needed.
		if (id == null || id.length() == 0) {
			id = name + "." + val;
			jspContext.setAttribute("id", id);
		}
	}
}%>
<input <c:if test="${!empty id}">id="<%=id%>"</c:if><c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> <%=qualifier%> type="${type}" value="<%=val%>" name="${name}"/>
<c:if test="${!empty label}"><label for="<%=id%>">${label}</label></c:if>