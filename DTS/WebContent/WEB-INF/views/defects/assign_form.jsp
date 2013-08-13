<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Assign Defect">
<jsp:attribute name="body">
<h3>Assign Defect</h3>
<p:errors class="text-error"/>
<p:form class="form-horizontal" action="defects/assign/${defects.defect.id}">
	<div class="control-group">
	    <label class="control-label">Assign to</label>
	    <div class="controls">
	      <p:select name="defect.ownerId" items="userList" itemValue="id" itemLabel="fullName" size="5"/>
	    </div>
	</div>

	<div class="control-group">
	    <label class="control-label"></label>
	    <div class="controls">
	      <input type="submit" class="btn btn-success" value="Assign"/>
	    </div>
	</div>	
</p:form>
</jsp:attribute>
</t:template>