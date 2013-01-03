<%@tag import="com.mobiarch.nf.*"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag dynamic-attributes="dynattrs" %> 
<%@attribute name="href" required="true"%>
<%
	Context context = Context.getContext();
	href = request.getContextPath() + context.getRequest().getServletPath() + "/" + href;
	jspContext.setAttribute("href", href);
%>
<a <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> href="${href}"><jsp:doBody/></a>