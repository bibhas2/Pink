package com.mobiarch.nf;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodInfo {
	private String path;
	private ArrayList<String> parameterNames = new ArrayList<String>();
	private Method method;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public ArrayList<String> getParameterNames() {
		return parameterNames;
	}
	public void setParameterNames(ArrayList<String> parameterNames) {
		this.parameterNames = parameterNames;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public void addParameterName(String name) {
		parameterNames.add(name);
	}
}
