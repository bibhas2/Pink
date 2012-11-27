package com.mobiarch.nf;

/**
 * CDI beans that are exposed to HTTP request must extend this class. The primary purpose of
 * this class is to distinguish an exposed CDI bean from all other CDI beans. This prevents
 * the possibility of malicious users executing methods of arbitrary CDI beans.
 * 
 * @author wasadmin
 *
 */
public abstract class Controller {

}
