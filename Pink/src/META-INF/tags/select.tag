<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag dynamic-attributes="dynattrs" %> 
<%@tag import="com.mobiarch.nf.PropertyManager"%>
<%@attribute name="name" required="true"  %>
<%@attribute name="items" required="true"  %>
<%@attribute name="itemValue" required="true"  %>
<%@attribute name="itemLabel" required="true"  %>
<%
Object formBean = request.getAttribute("formBean");
Class<?> cls = (Class<?>) request.getAttribute("formBeanClass");
PropertyManager pm = new PropertyManager();
Object val = pm.getProperty(cls, formBean, name);
request.setAttribute("selValue", val);
%>
<select <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> name="${name}">
	<c:forEach items="${formBean[items]}" var="o">
	<option value="${o[itemValue]}" <c:if test="${selValue == o[itemValue]}">selected="selected"</c:if>>${o[itemLabel]}</option>
	</c:forEach>
</select>