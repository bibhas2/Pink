package com.mobiarch.ajax;

import java.util.List;

import com.mobiarch.nf.PropertyViolation;

public class Response {
	private String status;
	private List<PropertyViolation> violations;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PropertyViolation> getViolations() {
		return violations;
	}
	public void setViolations(List<PropertyViolation> violations) {
		this.violations = violations;
	}
	public Response(String status, List<PropertyViolation> violations) {
		super();
		this.status = status;
		this.violations = violations;
	}
	
	public Response(String status) {
		super();
		this.status = status;
	}
}
