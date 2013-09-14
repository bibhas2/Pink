package com.mobiarch.nf.test;

import java.util.logging.Logger;

import javax.inject.Named;

import com.mobiarch.nf.Path;

@Named("short-url")
public class ShortURLTest {
	String param1, param2;
	Logger logger = Logger.getLogger(getClass().getName());
	
	@Path("param1/param2")
	public String index() {
		logger.info("index() called with " + param1 + ":" + param2);
		
		return null;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}
}
