<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:hero title="Pink Store Demo">
<jsp:attribute name="body">
<div class="row">
	<c:forEach items="${catalog.products}" var="product">
	<div class="span6">
	<h2>${product.name}</h2>
	<p>${product.description}</p>
	<p><p:a class="btn" href="catalog/product/${product.id}">View details</p:a></p>
	</div>
	</c:forEach>
</div>
</jsp:attribute>
</t:hero>