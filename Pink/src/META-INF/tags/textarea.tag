<%@tag import="com.mobiarch.nf.PropertyManager"%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag dynamic-attributes="dynattrs" %> <%@attribute name="name" required="true"%>
<%Object formBean = request.getAttribute("formBean");
Class<?> cls = (Class<?>) request.getAttribute("formBeanClass");
PropertyManager pm = new PropertyManager();
Object val = pm.getProperty(cls, formBean, name);
val = val != null ? val : "";
jspContext.setAttribute("val", val);%>
<textarea<c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> name="${name}"><c:out value="${val}"/></textarea>