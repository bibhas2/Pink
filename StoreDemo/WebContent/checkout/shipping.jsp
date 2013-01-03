<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<t:starter-template title="Shipping">
<jsp:attribute name="body">
<h2>Shipping</h2>

<p:errors style="color: red"/>

<p:form action="checkout/shipping">
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
<p:input name="address.phone"/><br/><br/>

<p:a href="checkout/cart" class="btn">Back to Cart</p:a> <input type="submit" value="Next" class="btn btn-success"/>
</p:form>
</jsp:attribute>
</t:starter-template>