<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="projects">
<jsp:attribute name="body">
<div class="row">
<div class="span6">
<h3>Projects</h3>

<table class="table table-striped">
<thead>
<tr>
<th>Name</th>
<th>Owner</th>
</tr>
</thead>
<tbody>
<c:forEach items="${defects.projectList}" var="project">
<tr>
<td><p:a href="defects/edit-project/${project.id}">${project.name}</p:a></td>
<td>${project.owner.fullName}</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
</div>
</jsp:attribute>
</t:template>