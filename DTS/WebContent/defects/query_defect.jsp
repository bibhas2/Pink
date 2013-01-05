<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Query Defect">
<jsp:attribute name="body">
<h3>Query Defect</h3>
<p:errors class="text-error"/>
<!-- Query type forms must use GET to generate display markup in response -->
<p:form class="form-horizontal" action="defects/do-query" method="get">
	<div class="control-group">
	    <label class="control-label">Defect IDs</label>
	    <div class="controls">
	      <p:input name="query.defectIdList" placeholder="Comma separated defect IDs"/>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Project</label>
	    <div class="controls">
	      <p:select name="query.projectId" size="5">
	      	<p:option value="0">Any project</p:option>
<c:forEach items="${defects.projectList}" var="project">
			<p:option value="${project.id}">${project.name}</p:option>
</c:forEach>	      	
	      </p:select>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Severity</label>
	    <div class="controls">
	      <p:select name="query.severity">
	      	<p:option value="0">Any severity</p:option>
	      	<p:option value="1">1 - Critical</p:option>
	      	<p:option value="2">2 - Mustfix</p:option>
	      	<p:option value="3">3 - Major</p:option>
	      	<p:option value="4">4 - Minor</p:option>
	      	<p:option value="5">5 - Feature Enhancement</p:option>
	      </p:select>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Priority</label>
	    <div class="controls">
	      <p:select name="query.priority">
	      	<p:option value="0">Any priority</p:option>
	      	<p:option value="1">1 - High</p:option>
	      	<p:option value="2">2 - Medium</p:option>
	      	<p:option value="3">3 - Low</p:option>
	      </p:select>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">State</label>
	    <div class="controls">
	      <p:select name="query.state" multiple="multiple">
	      	<p:option value="Open">Open</p:option>
	      	<p:option value="Assigned">Assigned</p:option>
	      	<p:option value="Accepted">Accepted</p:option>
	      	<p:option value="Rejected">Rejected</p:option>
	      	<p:option value="Completed">Completed</p:option>
	      	<p:option value="Verified">Verified</p:option>
	      </p:select>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Defect opened by</label>
	    <div class="controls">
	      <p:select name="query.originatorId" size="5">
	      	<p:option value="0">Any user</p:option>
<c:forEach items="${defects.userList}" var="user">
			<p:option value="${user.id}">${user.fullName}</p:option>
</c:forEach>	      	
	      </p:select>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Defect owned by</label>
	    <div class="controls">
	      <p:select name="query.ownerId" size="5">
	      	<p:option value="0">Any user</p:option>
<c:forEach items="${defects.userList}" var="user">
			<p:option value="${user.id}">${user.fullName}</p:option>
</c:forEach>	      	
	      </p:select>
	    </div>
	</div>
	
	<div class="control-group">
	    <label class="control-label"></label>
	    <div class="controls">
	      <input type="submit" class="btn btn-success" value="Query Defect"/>
	    </div>
	</div>	
</p:form>
</jsp:attribute>
</t:template>