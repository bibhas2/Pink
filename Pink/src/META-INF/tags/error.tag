<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag dynamic-attributes="dynattrs" %> 
<c:if test="${!empty context.violations}">
<div <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach>>
Please fix these input errors:
<ul>
<c:forEach items="${context.violations}" var="v">
	<li> ${v.message}</li>
</c:forEach>
</ul>
</div>
</c:if>