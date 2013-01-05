<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Defect Query Result">
<jsp:attribute name="body">
<h3>Defect Query Result</h3>

<table class="table table-striped">
<thead>
<tr>
<th>ID</th>
<th>Description</th>
<th>State</th>
</tr>
</thead>
<tbody>
<c:forEach items="${defects.defectList}" var="defect">
<tr>
<td>${defect.id}</td>
<td><p:a href="defects/show/${defect.id}">${defect.shortDescription}</p:a></td>
<td>${defect.stateId}</td>
</tr>
</c:forEach>
</tbody>
</table>

</jsp:attribute>
</t:template>