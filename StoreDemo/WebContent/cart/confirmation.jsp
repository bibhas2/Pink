<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<t:starter-template title="Order Confirmation">
<jsp:attribute name="body">
<h2>Order Confirmation</h2>
<p>Thank you for your order. Please keep a copy of the following information for your reference.</p>
<h4>Order #${cart.cart.id}</h4>
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

<p><br/><a href="javascript:window.print();" class="btn btn-success btn-large">Print</a></p>
</jsp:attribute>
</t:starter-template>