<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:starter-template title="Pink Store Demo">
<jsp:attribute name="body">
<h2>${catalog.product.name}</h2>
<p>${catalog.product.description}</p>
<p:form action="catalog/addToCart">
<p:input type="hidden" name="productId"/>
<p:input name="quantity" style="width: 35px; margin-bottom: 0px"/>
<input type="submit" class="btn btn-primary" value="Add to Cart"/>
</p:form>
</jsp:attribute>
</t:starter-template>