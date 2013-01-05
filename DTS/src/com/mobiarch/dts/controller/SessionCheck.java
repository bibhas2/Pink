package com.mobiarch.dts.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * CDI interceptor annotation. Checks for existence of a valid user session.
 * Redirects to the login screen if session is not setup.
 * 
 * This annotation can be used with any controller method.
 * 
 * @author wasadmin
 *
 */
@InterceptorBinding
@Target(value={ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionCheck {
}
