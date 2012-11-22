package com.mobiarch.nf;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
@Named
@RequestScoped
public class Context {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private List<PropertyViolation> violations;
	private Processor processor;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public boolean isPostBack() {
		return ("POST".equals(request.getMethod()));
	}
	public boolean isValidationFailed() {
		return violations != null && !violations.isEmpty();
	}
	public List<PropertyViolation> getViolations() {
		return violations;
	}
	public void addViolation(PropertyViolation violation) {
		if (violations == null) {
			violations = new ArrayList<PropertyViolation>();
		}
		violations.add(violation);
	}
	public Processor getProcessor() {
		return processor;
	}
	public void setProcessor(Processor processor) {
		this.processor = processor;
	}
	//For non-CDI clients
	public static Context getContext() {
		return Processor.getContext();
	}
	
	public Locale getLocale() {
		//Try to get the JSTL locale
		HttpSession session = request.getSession(false);
		if (session != null) {
			Locale l = (Locale) Config.get(session, Config.FMT_LOCALE);
			if (l != null) {
				logger.fine("Using JSTL locale: " + l.toString());
				
				return l;
			}
		}
		//Try to get the browser's locale or a default locale of the server's OS
		Locale l = request.getLocale();
		logger.fine("Using browser's locale: " + l.toString());
		
		return l;
	}
}
