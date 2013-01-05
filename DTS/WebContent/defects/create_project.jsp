<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Create Project">
<jsp:attribute name="body">
<div class="row">
<div class="span6">
<h3>Create Project</h3>
<p:errors style="color: red"/>
<p:form action="defects/create-project" class="form-horizontal">
	<div class="control-group">
	    <label class="control-label">Name</label>
	    <div class="controls">
	      <p:input name="project.name"/>
	    </div>
	</div>
	<div class="control-group">
	    <label class="control-label">Owner</label>
	    <div class="controls">
	      <p:select name="project.ownerId" items="userList" itemValue="id" itemLabel="fullName" size="5"/>
	    </div>
	</div>

	<div class="control-group">
	    <label class="control-label"></label>
	    <div class="controls">
	      <input type="submit" class="btn btn-success" value="Create Project"/>
	    </div>
	</div>	

</p:form>
</div>
</div>
</jsp:attribute>
</t:template>