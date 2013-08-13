<html>
<body>
<%
for (int i = 0; i < 1000; ++i) {
%>
<p>Reached counter <%=i%></p>
<%
}
if (true)
	throw new Exception("Error inside a JSP");
%>
</body>
</html>