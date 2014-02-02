package com.mobiarch.pink.test;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.mobiarch.nf.Context;

public class CDITestRunner extends BlockJUnit4ClassRunner {
	Logger logger = Logger.getLogger(getClass().getName());
	
	public CDITestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}
	protected Object createTest() throws Exception {
		
		Context context = Context.getContext();
		BeanManager bmgr = context.getProcessor().getBeanManager();
		
		logger.fine("Creating test case instance: " + getTestClass().getJavaClass().getName());
		
		Set<Bean<?>> beans =  bmgr.getBeans(getTestClass().getJavaClass(), new AnnotationLiteral<Any>() {});
		if (!beans.iterator().hasNext()) {
			String msg = "Failed to get injectable class: " + getTestClass().getJavaClass().getName();
			
			logger.log(Level.SEVERE, msg);
			throw new IllegalArgumentException(msg);
		}
		Bean<?> b = beans.iterator().next();
		
		CreationalContext<?> cc = bmgr.createCreationalContext(b);
		Object o = bmgr.getReference(b, b.getBeanClass(), cc);
		
		return o;
	}

}
