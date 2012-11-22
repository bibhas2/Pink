package com.mobiarch.nf;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

@Named
@RequestScoped
public class Processor {
	@Inject
	BeanManager beanManager;
	@Inject
	Context context;
	Logger logger = Logger.getLogger(getClass().getName());
	@Inject
	private Validator validator; 

	private static final ThreadLocal<Context> contextVar = new ThreadLocal<Context>();
	
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		context.setRequest(request);
		context.setResponse(response);
		context.setProcessor(this);
		contextVar.set(context);

		String path = request.getPathInfo();
		logger.fine("Processing URI: " + path);
		if (path == null) {
			logger.severe("Incomplete URI");
			return;
		}
		// Strip the leading /
		path = path.substring(1);
		// Break path in name and method
		String parts[] = path.split("\\/");
		String name = null;
		String method = null;

		if (parts.length == 2) {
			name = parts[0];
			method = parts[1];
		} else if (parts.length == 1) {
			name = parts[0];
			method = "index";
		} else {
			throw new Exception("Invalid URI");
		}
		Bean<?> bean = getBeanReference(name);
		Object o = getBeanInstance(bean);
		transferProperties(request, bean, o);
		String outcome = invokeMethod(method, o);
		navigateTo(name, outcome.toString());
	}

	private String invokeMethod(String method, Object o)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		logger.info("Invoking method: " + method);
		Method m = o.getClass().getMethod(method);
		
		Object outcome = m.invoke(o);
		
		return outcome.toString();
	}

	public Bean<?> getBeanReference(String name) {
		logger.fine("Looking for bean: " + name);

		Set<Bean<?>> beans = beanManager.getBeans(name);
		Bean<?> b = beans.iterator().next();

		logger.info("Found bean: " + b.getBeanClass().getName());
		
		return b;
	}
	
	public Object getBeanInstance(Bean<?> b) {
		CreationalContext<?> cc = beanManager.createCreationalContext(b);
		Object o = beanManager.getReference(b, b.getBeanClass(), cc);

		return o;
	}
	
	public Object getBeanInstanceByName(String name) {
		return getBeanInstance(getBeanReference(name));
	}
	
	protected void navigateTo(String beanName, String outcome) throws Exception {
		if (outcome == null) {
			// Re-load the last page
		} else {
			if (context.isPostBack() && !context.isValidationFailed()) {
				// Always redirect after a POST
				// outcome = beanName + "/" + outcome;
				redirect(beanName, outcome);
			} else if (outcome.startsWith("@")) {
				outcome = outcome.substring(1);
				// outcome = beanName + "/" + outcome;
				redirect(beanName, outcome);
			} else {
				// Do a forward
				outcome = "/" + beanName + "/" + outcome + ".jsp";
				logger.fine("Forwarding to: " + outcome);
				context.getRequest().getRequestDispatcher(outcome)
						.forward(context.getRequest(), context.getResponse());
			}
		}
	}

	private void redirect(String beanName, String outcome) throws IOException {
		outcome = context.getRequest().getContextPath()
				+ context.getRequest().getServletPath() + "/" + beanName + "/"
				+ outcome;
		logger.fine("Redirecting to: " + outcome);
		context.getResponse().sendRedirect(outcome);
	}

	protected void transferProperties(HttpServletRequest request,
			Bean<?> bean,
			Object o) throws Exception {
		PropertyManager pmgr = new PropertyManager();
		
		pmgr.transferProperties(request, bean.getBeanClass(), o);
	}

	//For non CDI clients to get context
	public static Context getContext() {
		return contextVar.get();
	}
	
	public static Processor getProcessor() {
		return getContext().getProcessor();
	}
	
	public Validator getValidator() {
		return validator;
	}
}
