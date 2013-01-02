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
<c:forEach items="${checkout.cart.cartItems}" var="item">
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
<td>Tax</td><td>$<fmt:formatNumber value="${checkout.cart.productTax}" maxFractionDigits="2"/></td>
</tr>
<tr style="border-bottom: solid 1px #cfcfcf">
<td>Shipping</td><td>$<fmt:formatNumber value="${checkout.cart.shipping}" maxFractionDigits="2"/></td>
</tr>
<tr>
<td>Grand total</td><td><h4>$<fmt:formatNumber value="${checkout.cart.grandTotal}" maxFractionDigits="2"/></h4></td>
</tr>
</table>
<legend>Shipping Address</legend>
<p>
${checkout.cart.shippingAddress.customerName}<br/>
${checkout.cart.shippingAddress.street1}<br/>
${checkout.cart.shippingAddress.street2}<br/>
${checkout.cart.shippingAddress.city}, ${checkout.cart.shippingAddress.state}<br/>
${checkout.cart.shippingAddress.country}, ${checkout.cart.shippingAddress.zip}<br/>
${checkout.cart.shippingAddress.email}<br/>
${checkout.cart.shippingAddress.phone}
</p>

<legend>Billing Information</legend>
<p>
${checkout.payment.cardNumber} - ${checkout.payment.cardType}<br/>
Expiry: ${checkout.payment.expMonth}/${checkout.payment.expYear}
<p/>
<p>
${checkout.cart.billingAddress.customerName}<br/>
${checkout.cart.billingAddress.street1}<br/>
${checkout.cart.billingAddress.street2}<br/>
${checkout.cart.billingAddress.city}, ${checkout.cart.billingAddress.state}<br/>
${checkout.cart.billingAddress.country}, ${checkout.cart.billingAddress.zip}<br/>
${checkout.cart.billingAddress.email}<br/>
${checkout.cart.billingAddress.phone}
</p>

<p><br/>
<p:form action="checkout/place-order">
<a href="/StoreDemo/app/catalog/" class="btn">Back to shopping</a> <input type="submit" class="btn btn-success btn-large" value="Place Order"/>
</p:form>
</p>
</jsp:attribute>
</t:starter-template>