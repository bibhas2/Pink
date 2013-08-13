<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Edit Defect">
<jsp:attribute name="body">
<h3>Edit Defect</h3>
<p:errors class="text-error"/>
<p:form class="form-horizontal" action="defects/edit/${defects.defect.id}">
	<div class="control-group">
	    <label class="control-label">Project</label>
	    <div class="controls">
	      <p:select name="defect.projectId" items="projectList" itemValue="id" itemLabel="name" size="5"/>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Severity</label>
	    <div class="controls">
	      <p:select name="defect.severity">
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
	      <p:select name="defect.priority">
	      	<p:option value="1">1 - High</p:option>
	      	<p:option value="2">2 - Medium</p:option>
	      	<p:option value="3">3 - Low</p:option>
	      </p:select>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label"></label>
	    <div class="controls">
	      <input type="submit" class="btn btn-success" value="Update Defect"/>
	    </div>
	</div>	
</p:form>
</jsp:attribute>
</t:template>