package com.mobiarch.dts.controller;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@SessionCheck
@Interceptor
public class SessionCheckImpl {
	@Inject
	SessionData session;
	
	Logger logger = Logger.getLogger(getClass().getName());
	@AroundInvoke 
	public Object manageTransaction(InvocationContext ctx) throws Exception {
		if (session == null || session.getCurrentUser() == null) {
			logger.fine("Session not setup. Redirecting to login");
			return "@/login";
		} else {
			logger.fine("Session is setup. Proceeding.");
		}
		
		return ctx.proceed();
	}
}
