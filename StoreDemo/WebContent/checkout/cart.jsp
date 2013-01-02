<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<t:starter-template title="Shopping Cart">
<jsp:attribute name="body">
<h2>Shopping Cart</h2>

<table class="table table-striped">
<c:forEach items="${checkout.cart.cartItems}" var="item">
<tr _style="border-bottom: solid 1px #cfcfcf">
<td width="250px">
<p><a href="/StoreDemo/app/catalog/product/${item.product.id}">${item.product.name}</a></p>
<p>${item.product.description}</p>
</td>
<td>
<p>Total: $<fmt:formatNumber value="${item.itemTotal}" maxFractionDigits="2"/></p>
<p:form action="checkout/update">
<c:set target="${checkout}" property="quantity" value="${item.quantity}"/>
<c:set target="${checkout}" property="cartItemId" value="${item.id}"/>
<p:input type="hidden" name="cartItemId"/>
<p:input name="quantity" style="width: 35px; margin-bottom: 0px"/> 
<input type="submit" class="btn" value="Update"/>
<a href="/StoreDemo/app/checkout/delete/${item.id}" class="btn btn-danger">Delete</a>
</p:form>
</td>
</tr>
</c:forEach>
<tr>
<td>Tax</td><td>$<fmt:formatNumber value="${checkout.cart.productTax}" maxFractionDigits="2"/></td>
</tr>
<tr _style="border-bottom: solid 1px #cfcfcf">
<td>Shipping</td><td>$<fmt:formatNumber value="${checkout.cart.shipping}" maxFractionDigits="2"/></td>
</tr>
<tr>
<td>Grand total</td><td><h4>$<fmt:formatNumber value="${checkout.cart.grandTotal}" maxFractionDigits="2"/></h4></td>
</tr>
</table>
<p><br/><a href="/StoreDemo/app/catalog/" class="btn">Back to shopping</a> <a href="/StoreDemo/app/checkout/shipping" class="btn btn-success">Checkout</a></p>
</jsp:attribute>
</t:starter-template>