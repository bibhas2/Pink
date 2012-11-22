<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
<a href="register">Add new customer</a>
<h2>Customer list</h2>
<c:forEach  items="${customers.customerList}" var="c">
<p>
Name: ${c.fullName}<br/>
E-mail: ${c.email}<br/>
<a href="show?customer.id=${c.id}">View</a> <a href="update?customer.id=${c.id}">Edit</a> <a href="delete?customer.id=${c.id}">Delete</a>
</p>
</c:forEach>
</body>
</html>