<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:starter-template title="Shopping Cart">
<jsp:attribute name="body">
<h2>Shopping Cart</h2>
<p>Your cart is empty.</p>
<p><a href="/StoreDemo/store/catalog/" class="btn">Back to shopping</a></p>
</jsp:attribute>
</t:starter-template>