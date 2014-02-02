package com.mobiarch.pink.test;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

@RequestScoped
@Named("pink-unit")
public class TestController {
	private String classes[];
	
	public Object index() throws Exception {
		Class<?> list[] = new Class[classes.length];
		
		for (int i = 0; i < classes.length; ++i) {
			list[i] = Class.forName(classes[i]);
		}
		Result r = JUnitCore.runClasses(list);
		
		return new TestResult(r);
	}

	public String[] getClasses() {
		return classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}
}
