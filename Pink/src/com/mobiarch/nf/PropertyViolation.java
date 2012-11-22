package com.mobiarch.nf;

import javax.validation.ConstraintViolation;


public class PropertyViolation {
	private String message;
	private String propertyName;
	
	public PropertyViolation(String message, String propertyName) {
		super();
		this.message = message;
		this.propertyName = propertyName;
	}
	
	public PropertyViolation(ConstraintViolation<?> vi) {
		this.message = vi.getMessage();
		this.propertyName = vi.getPropertyPath().toString();
	}
	public PropertyViolation() {
		
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
