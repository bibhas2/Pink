package com.mobiarch.pink.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8589694276948659453L;
	
	private int testCount, failureCount;
	private boolean failed;
	private List<FailureRecord> failures;
	
	public TestResult() {
	}
	public TestResult(Result r) {
		testCount = r.getRunCount();
		failureCount = r.getFailureCount();
		failed = !r.wasSuccessful();
		
		if (failed) {
			ArrayList<FailureRecord> list = new ArrayList<FailureRecord>(failureCount);
			for (Failure f : r.getFailures()) {
				FailureRecord fr = new FailureRecord();
				
				fr.setClassName(f.getDescription().getClassName());
				fr.setMethod(f.getDescription().getMethodName());
				fr.setMessage(f.getMessage());
				
				list.add(fr);
			}
			
			failures = list;
		}
	}
	
	public int getTestCount() {
		return testCount;
	}
	public void setTestCount(int testCount) {
		this.testCount = testCount;
	}
	public int getFailureCount() {
		return failureCount;
	}
	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	public List<FailureRecord> getFailures() {
		return failures;
	}
	public void setFailures(List<FailureRecord> failures) {
		this.failures = failures;
	}
}
