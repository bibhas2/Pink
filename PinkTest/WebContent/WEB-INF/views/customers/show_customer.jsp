<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<a href="../list">Back to List</a>
<p>
ID: ${customers.customer.id}<br/>
Name: ${customers.customer.fullName}<br/>
E-mail: ${customers.customer.email}<br/>
Department: <br/>
<c:forEach items="${customers.customer.department}" var="d">
Department number ${d}<br/>
</c:forEach>
Level: ${customers.customer.level}<br/>
Send e-mail: ${customers.customer.sendEmail}<br/>
</p>
</body>
</html>