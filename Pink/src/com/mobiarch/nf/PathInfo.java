package com.mobiarch.nf;

import java.util.ArrayList;

public class PathInfo {
	private String beanName;
	private String methodPath;
	private ArrayList<String> pathParameters = new ArrayList<String>();

	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getMethodPath() {
		return methodPath;
	}
	public void setMethodPath(String methodName) {
		this.methodPath = methodName;
	}
	public ArrayList<String> getPathParameters() {
		return pathParameters;
	}
	public void setPathParameters(ArrayList<String> pathParameters) {
		this.pathParameters = pathParameters;
	}
	public void addPathParameter(String str) {
		pathParameters.add(str);
	}
}
