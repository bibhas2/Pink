<%@tag import="com.mobiarch.nf.*"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@tag dynamic-attributes="dynattrs" %> 
<%@attribute name="action" required="true"  %><%@attribute name="method" required="false"%>
<%
	String parts[] = action.split("/");
	
	String formBeanName = parts[0];
	
	Context context = Context.getContext();
	Processor p = context.getProcessor();
	javax.enterprise.inject.spi.Bean<?> cdiBean = p.getBeanReference(formBeanName);
	request.setAttribute("formBean", p.getBeanInstance(cdiBean));
	request.setAttribute("formBeanClass", cdiBean.getBeanClass());
	
	//String action = null;
	action = request.getContextPath() + "/" + action;
	jspContext.setAttribute("action", action);
	
	if (method == null) {
		jspContext.setAttribute("method", "post");
	}
%>
<form <c:forEach items="${dynattrs}" var="a"> ${a.key}="${a.value}"</c:forEach> action="${action}" method="${method}">
	<jsp:doBody/>
</form>
<%
	request.removeAttribute("formBean");
%>
