<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag dynamic-attributes="dynattrs" %> 

<%@attribute name="action" required="true"  %>

<%
	String parts[] = action.split("/");
	
	String formBeanName = parts[0];
	
	com.mobiarch.nf.Processor p = com.mobiarch.nf.Processor.getProcessor();
	javax.enterprise.inject.spi.Bean<?> cdiBean = p.getBeanReference(formBeanName);
	request.setAttribute("formBean", p.getBeanInstance(cdiBean));
	request.setAttribute("formBeanClass", cdiBean.getBeanClass());
	
	//String action = null;
	action = request.getContextPath() + "/app/" + action;
%>

<form <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> action="<%=action%>">
	<jsp:doBody/>
</form>

<%
	request.removeAttribute("formBean");
%>
