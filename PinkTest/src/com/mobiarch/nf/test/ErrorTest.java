package com.mobiarch.nf.test;

import javax.inject.Named;

import com.mobiarch.nf.Controller;

@Named
public class ErrorTest extends Controller {
	public void throwError() throws Exception {
		throw new Exception("Sample error test");
	}
	public String errorInJSP() {
		return "bad";
	}
}
