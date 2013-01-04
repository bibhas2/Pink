<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="p" uri="http://mobiarch"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<t:template title="Defect Details">
<jsp:attribute name="body">
<h3>Defect ${defects.defect.id} - ${defects.defect.shortDescription}</h3>
<p>
Severity: ${defects.defect.severity}<br/>
Priority: ${defects.defect.priority}<br/>
Opened by: ${defects.defect.originator.fullName}<br/>
Current owner: ${defects.defect.owner.fullName}<br/>
Project: ${defects.defect.project.name}<br/>
</p>
<p>
<textarea readonly="readonly" rows="10" class="span5">${defects.defect.longDescription}</textarea>
</p>
</jsp:attribute>
</t:template>