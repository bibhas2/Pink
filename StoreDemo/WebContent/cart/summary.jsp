<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<t:starter-template title="Order Summary">
<jsp:attribute name="body">
<h2>Order Summary</h2>
<p>Please carefully review your order and click <strong>Place Order</strong> to place your order.</p>
<legend>Shopping Cart</legend>
<table>
<c:forEach items="${cart.cart.cartItems}" var="item">
<tr style="border-bottom: solid 1px #cfcfcf">
<td width="250px">
<p><a href="/StoreDemo/app/catalog/product/${item.product.id}">${item.product.name}</a></p>
<p>${item.product.description}</p>
</td>
<td>
<p>
Quantity: ${item.quantity}<br/>
Total: $<fmt:formatNumber value="${item.itemTotal}" maxFractionDigits="2"/>
</p>
</td>
</tr>
</c:forEach>
<tr>
<td>Tax</td><td>$<fmt:formatNumber value="${cart.cart.productTax}" maxFractionDigits="2"/></td>
</tr>
<tr style="border-bottom: solid 1px #cfcfcf">
<td>Shipping</td><td>$<fmt:formatNumber value="${cart.cart.shipping}" maxFractionDigits="2"/></td>
</tr>
<tr>
<td>Grand total</td><td><h4>$<fmt:formatNumber value="${cart.cart.grandTotal}" maxFractionDigits="2"/></h4></td>
</tr>
</table>
<legend>Shipping Address</legend>
<p>
${cart.cart.shippingAddress.customerName}<br/>
${cart.cart.shippingAddress.street1}<br/>
${cart.cart.shippingAddress.street2}<br/>
${cart.cart.shippingAddress.city}, ${cart.cart.shippingAddress.state}<br/>
${cart.cart.shippingAddress.country}, ${cart.cart.shippingAddress.zip}<br/>
${cart.cart.shippingAddress.email}<br/>
${cart.cart.shippingAddress.phone}
</p>

<legend>Billing Information</legend>
<p>
${cart.payment.cardNumber} - ${cart.payment.cardType}<br/>
Expiry: ${cart.payment.expMonth}/${cart.payment.expYear}
<p/>
<p>
${cart.cart.shippingAddress.customerName}<br/>
${cart.cart.shippingAddress.street1}<br/>
${cart.cart.shippingAddress.street2}<br/>
${cart.cart.shippingAddress.city}, ${cart.cart.shippingAddress.state}<br/>
${cart.cart.shippingAddress.country}, ${cart.cart.shippingAddress.zip}<br/>
${cart.cart.shippingAddress.email}<br/>
${cart.cart.shippingAddress.phone}
</p>

<p><br/>
<p:form action="cart/place">
<a href="/StoreDemo/app/catalog/" class="btn">Back to shopping</a> <input type="submit" class="btn btn-success btn-large" value="Place Order"/>
</p:form>
</p>
</jsp:attribute>
</t:starter-template>