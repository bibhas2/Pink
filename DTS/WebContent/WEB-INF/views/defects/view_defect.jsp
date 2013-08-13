<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Defect Details">
<jsp:attribute name="body">
<h3>Defect ${defects.defect.id} - ${defects.defect.shortDescription}</h3>
<dl class="dl-horizontal">
<dt>Status</dt> <dd>${defects.defect.stateId}</dd>
<dt>Severity</dt> <dd>${defects.defect.severity}</dd>
<dt>Priority</dt> <dd>${defects.defect.priority}</dd>
<dt>Opened by</dt> <dd>${defects.defect.originator.fullName}</dd>
<dt>Current owner</dt> <dd>${defects.defect.owner.fullName}</dd>
<dt>Project</dt> <dd>${defects.defect.project.name}</dd>
</dl>

<p>
<textarea readonly="readonly" rows="10" class="span5">${defects.defect.longDescription}</textarea>
</p>
<div>
<div class="btn-group">
<c:if test="${defects.defect.originatorId == sessionData.currentUser.id}"><p:a href="defects/edit/${defects.defect.id}" class="btn">Edit</p:a></c:if>
<c:if test="${defects.defect.stateId == 'Open'}"><p:a href="defects/assign/${defects.defect.id}" class="btn">Assign</p:a></c:if>
<c:if test="${defects.defect.stateId == 'Assigned' && defects.defect.ownerId == sessionData.currentUser.id}"><p:a href="defects/accept/${defects.defect.id}" class="btn">Accept</p:a></c:if>
<c:if test="${defects.defect.stateId == 'Assigned' && defects.defect.ownerId == sessionData.currentUser.id}"><p:a href="defects/reject/${defects.defect.id}" class="btn">Reject</p:a></c:if>
<c:if test="${defects.defect.stateId == 'Accepted' && defects.defect.ownerId == sessionData.currentUser.id}"><p:a href="defects/complete/${defects.defect.id}" class="btn">Complete</p:a></c:if>
<c:if test="${defects.defect.stateId == 'Completed' && defects.defect.originatorId == sessionData.currentUser.id}"><p:a href="defects/verify/${defects.defect.id}" class="btn">Verify</p:a></c:if>
</div>
</div>
<div class="span6">
<legend>Comments</legend>
<c:forEach items="${defects.defect.commentList}" var="comment">
<p>
<strong>${comment.user.fullName} on ${comment.lastupdate}</strong><br/>
<textarea readonly="readonly" rows="10" class="span12">${comment.description}</textarea>
</p>
</c:forEach>
<p:form action="defects/add-comment/${defects.defect.id}">
<p:textarea name="commentText" rows="5" class="span12"/>
<br/>
<input type="submit" class="btn btn-success" value="Add Comment"/>
</p:form>

<legend>Audit Log</legend>
<table class="table table-striped">
<thead>
<tr>
<th>Description</th>
<th>User</th>
<th>Date</th>
</tr>
</thead>
<tbody>
<c:forEach items="${defects.defect.logList}" var="log">
<tr>
<td>${log.actionText}</td>
<td>${log.actorName}</td>
<td>${log.lastupdate}</td>
</tr>
</c:forEach>
</tbody>
</table>
</div>
</jsp:attribute>
</t:template>