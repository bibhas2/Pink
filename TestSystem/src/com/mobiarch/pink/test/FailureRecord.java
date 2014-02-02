package com.mobiarch.pink.test;

import java.io.Serializable;

public class FailureRecord implements Serializable {
	private static final long serialVersionUID = -8432205176583009773L;

	private String className;
	private String method;
	private String message;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
