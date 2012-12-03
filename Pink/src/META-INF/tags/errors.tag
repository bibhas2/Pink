<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag dynamic-attributes="dynattrs" %><%@attribute name="property" required="false"%>
<c:if test="${empty property && !empty context.violations}">
<div <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach>>
Please fix these input errors:
<ul>
<c:forEach items="${context.violations}" var="v">
	<li> ${v.message}</li>
</c:forEach>
</ul>
</div>
</c:if>
<c:if test="${!empty property && !empty context.violations}">
<span <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach>>
<c:forEach items="${context.violations}" var="v" varStatus="s">
<c:if test="${v.propertyName == property}">
<c:if test="${!empty first}">,</c:if> ${v.message} 
<c:set var="first" value="first"/>
</c:if>
</c:forEach>
</span>
</c:if>