<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<t:starter-template title="Billing">
<jsp:attribute name="body">
<h2>Billing</h2>

<p:errors style="color: red"/>

<p:form action="checkout/billing" id="billingForm">
<legend>Credit Card</legend>
Card Type:<br/>
<p:select name="payment.cardType">
	<p:option value="MC">MasterCard</p:option> 
	<p:option value="VI">Visa</p:option> 
	<p:option value="AM">Amex</p:option> 
</p:select><br/>
Card Number:<br/>
<p:input name="payment.cardNumber"/><br/> 
Expiry month: <p:input name="payment.expMonth" placeholder="MM" style="width: 25px"/> Year: <p:input name="payment.expYear" placeholder="YY"  style="width: 25px"/><br/>
Security Code:<br/>
<p:input name="payment.securityCode"/><br/> 

<legend>Billing Address</legend>
<!-- Bootstrap does checkbox label in a strange way. But, Pink is flexible enough
to handle it. -->
<label class="checkbox">
<p:input type="checkbox" id="sameAsShipping" name="sameAsShipping" value="true"/> Same as shipping address
</label>
<div id="billingAddressDiv">
Name:<br/>
<p:input name="address.customerName"/><br/> 
Street address:<br/>
<p:input name="address.street1"/><br/>
Street address:<br/>
<p:input name="address.street2"/><br/>
City:<br/>
<p:input name="address.city"/><br/>
State:<br/>
<p:input name="address.state"/><br/>
ZIP:<br/>
<p:input name="address.zip"/><br/>
Country:<br/>
<p:input name="address.country"/><br/><br/>
E-mail:<br/>
<p:input name="address.email"/><br/>
Phone:<br/>
<p:input name="address.phone"/><br/>
</div>

<p:a href="checkout/cart" class="btn">Back to Cart</p:a> <input type="submit" value="Next" class="btn btn-success"/>
</p:form>

<script>
$(function() {
	$("#sameAsShipping").change(manageBillingAddress);
	$("#billingForm").submit(function() {
		if ($("#sameAsShipping")[0].checked) {
			//Remove all billoing address input fields so
			//that they are not validated.
			$("#billingAddressDiv").remove();
		}

		return true;
	});
});

function manageBillingAddress() {
	$("#billingAddressDiv").toggle($("#sameAsShipping")[0].checked == false);
}
manageBillingAddress();
</script>
</jsp:attribute>
</t:starter-template>