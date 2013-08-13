<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Open a New Defect">
<jsp:attribute name="body">
<h3>Open a New Defect</h3>
<p:errors class="text-error"/>
<p:form class="form-horizontal" action="defects/open">
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
	    <label class="control-label">Short description</label>
	    <div class="controls">
	      <p:input name="defect.shortDescription" size="35" class="span6"/>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Long description</label>
	    <div class="controls">
	      <p:textarea name="defect.longDescription" rows="10" cols="35" class="span6"/>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label"></label>
	    <div class="controls">
	      <input type="submit" class="btn btn-success" value="Add Defect"/>
	    </div>
	</div>	
</p:form>
</jsp:attribute>
</t:template>