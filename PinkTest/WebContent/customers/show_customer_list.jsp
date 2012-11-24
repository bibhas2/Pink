<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
<a href="register">Add new customer</a>
<h2>Customer List</h2>
<c:forEach  items="${customers.customerList}" var="c">
<p>
Name: ${c.fullName}<br/>
E-mail: ${c.email}<br/>
<a href="show/${c.id}">View</a> <a href="update/${c.id}">Edit</a> <a href="delete/${c.id}">Delete</a>
</p>
</c:forEach>
</body>
</html>