package com.mobiarch.nf.test;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Context;

/**
 * This is the root controller that is mapped to
 * "/". Pink will automatically look for a bean caled "home" to
 * rout this request.
 * 
 * @author bibhas
 *
 */
@Named("home")
public class HomePageController {
	@Inject
	Context ctx;
	Logger logger = Logger.getLogger(getClass().getName());
	
	public String index() {
		logger.fine("Context path: " + ctx.getRequest().getContextPath());
		return "index";
	}
}
