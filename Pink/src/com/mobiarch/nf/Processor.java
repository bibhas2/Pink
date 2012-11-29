package com.mobiarch.nf;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	private static ConcurrentHashMap<String, MethodInfo> methodCache = new ConcurrentHashMap<String, MethodInfo>();
	/**
	 * <p>This is the core HTTP request processing routine. Every request 
	 * meant for a CDI managed bean method is processed by this function.</p>
	 * 
	 * <p>Each HTTP request is processed in this sequence:<br/>
	 * 1. Inspect the URI path which is of the format /BEAN_NAME/METHOD_PATH/PROPERTY1/PROPERTY2/PROPERTY_N. 
	 * Where, BEAN_NAME is the name of the CDI bean. It is the only mandatory part of a URI. METHOD_PATH is the path of a public method
	 * of that bean. By default, it is the name of the method but can be overridden using the @Path
	 * annotation. If a METHOD_PATH is absent then "index" is assumed to be the method name. PROPERTY1, PROPERTY2 and so on
	 * are bean property values. <br/>
	 * 2. Obtain a reference to the CDI bean instance from its appropriate scope.<br/>
	 * 3. Resolve the method of the bean to be invoked using the METHOD_PATH portion of the URI path.<br/>
	 * 4. Copy values from URL request parameters, URI path and request body to the properties of the bean. Values
	 * are converted from String input data to the appropriate data types of properties. Validation
	 * is performed after properties have been updated. Any error in validation or data conversion is saved in the context.<br/>
	 * 5. Invoke the method of the bean. This is done even if there is validation or conversion errors. The bean method should 
	 * call context.isValidationFailed() to take appropriate action. The method returns an outcome object.<br/>
	 * 6. Navigation is performed based on the outcome. If outcome is null, no navigation is performed. 
	 * If the outcome is not a String then it is serialized as a JSON document into the output stream. 
	 * The content type is then set to "application/json" in that case. Otherwise, if the outcome is a String then a redirection
	 * or forwarding to JSP is performed using the following rules.
	 * If the request is a POST and there is no validation error then outcome is assumed to be a METHOD_PATH or /BEAN_NAME/METHOD_PATH. Systems redirects the browser to that path. 
	 * If the request is a POST and there are validation errors, then outcome is assumed to be a JSP file name, without the .jsp. System
	 * forwards to that JSP file. If the request is a GET then the outcome is assumed to be a JSP file name and system
	 * forwards to that file. 
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		context.setRequest(request);
		context.setResponse(response);
		context.setProcessor(this);
		contextVar.set(context); //Set the context for this thread

		try {
			PathInfo pInfo = compilePathInfo(request);
			Bean<?> bean = getBeanReference(pInfo.getBeanName());
			Object o = getBeanInstance(bean);
			MethodInfo mInfo = resolveMethod(pInfo, bean.getBeanClass());
			transferProperties(request, bean, o, mInfo, pInfo);
			Object outcome = invokeMethod(mInfo, o);
			navigateTo(pInfo.getBeanName(), outcome);
		} finally {
			contextVar.remove(); //Remove the context
		}
	}

	/**
	 * <p>Determines the method to be invoked from the URI path. 
	 * A URI has the syntax BEAN_NAME/METHOD_PATH. The method path
	 * is by default the name of a public method in the bean class. 
	 * Optionally, you can use the @Path annotation to change the path
	 * name of a method. For example: @Path("/newMethodPath").</p>
	 * 
	 * <p>It also builds a cache of method information for the bean class the 
	 * first time.</p>
	 * 
	 * @param pi - Contains information about the URI path.
	 * @param cls - Class of the bean where we will search for a matching method. This must be the true class for a 
	 * CDI managed bean.
	 * @return
	 */
	public MethodInfo resolveMethod(PathInfo pi, Class<?> cls) {
		String methodKey = pi.getBeanName() + "/" + pi.getMethodPath();
		MethodInfo mi = methodCache.get(methodKey);
		
		if (mi != null) {
			logger.fine("Got a hit in method cache");
			return mi;
		}
		/*
		 * Multiple threads may lookup the same class. The duplicate work can
		 * only happen in the beginning of life of an app. This is quite harmless.
		 */
		
		//Build the entire method database for the class.
		Method list[] = cls.getDeclaredMethods();
		for (int i = 0; i < list.length; ++i) {
			Method m = list[i];
			
			if ((m.getModifiers() & Modifier.PUBLIC) == 0) {
				logger.fine("Skipping private method: " + m.getName());
				continue;
			}
			logger.fine("Inspecting method: " + m.getName());
			//Let's see if the method has annotation
			Path p = m.getAnnotation(Path.class);
			if (p == null) {
				String key = pi.getBeanName() + "/" + m.getName();
				MethodInfo tmpMi = new MethodInfo();
				
				tmpMi.setMethod(m);
				methodCache.putIfAbsent(key, tmpMi);
			} else {
				MethodInfo tmpMi = new MethodInfo();
				String val = p.value();
				
				if (val == null || val.length() == 0) {
					throw new IllegalArgumentException("Path annotation is missing a value");
				}
				String parts[] = val.split("\\/");
				boolean isAbsolute = parts[0].length() == 0; //true if path starts with "/".
				if (isAbsolute) {
					logger.fine("Absolute path is set for method.");
					for (int j = 0; j < parts.length; ++j) {
						if (j == 0) {
							continue;
						} else if (j == 1) {
							tmpMi.setPath(parts[j]);
						} else {
							tmpMi.addParameterName(parts[j]);
						}
					}
					
					String key = pi.getBeanName() + "/" + tmpMi.getPath();
					tmpMi.setMethod(m);
					methodCache.putIfAbsent(key, tmpMi);
				} else {
					logger.fine("Relative path is set for method.");
					for (int j = 0; j < parts.length; ++j) {
						tmpMi.addParameterName(parts[j]);
					}
					
					String key = pi.getBeanName() + "/" + m.getName();
					tmpMi.setMethod(m);
					methodCache.putIfAbsent(key, tmpMi);
				}
			}
		}
		
		mi = methodCache.get(methodKey);
		
		if (mi == null) {
			throw new IllegalArgumentException("Invalid method path: " + pi.getMethodPath());
		}
		return mi;
	}

	/**
	 * This method gathers all available information from the URI path. A URI path is
	 * of the format: /BEAN_NAME/METHOD_PATH/PROPERTY1/PROPERTY2/PROPERTY_N. 
	 * Where, BEAN_NAME is the name of the CDI bean. It is the only mandatory part of a URI. METHOD_PATH is the path of a public method
	 * of that bean. By default, it is the name of the method but can be overridden using the @Path
	 * annotation. If a METHOD_PATH is absent then "index" is assumed to be the method name. PROPERTY1, PROPERTY2 and so on
	 * are bean property values. They can be used in place of query strings for a more SEO friendly URL.
	 * 
	 * @param request
	 * @return
	 */
	public PathInfo compilePathInfo(HttpServletRequest request) {
		String path = request.getPathInfo();
		logger.fine("Processing URI: " + path);
		if (path == null) {
			throw new IllegalArgumentException("Incomplete URI");
		}
		// Break path in name and method
		String parts[] = path.split("\\/");
		PathInfo pi = new PathInfo();

		pi.setMethodPath("index"); // Default method name
		
		for (int i = 0; i < parts.length; ++i) {
			if (i == 0) {
				continue; //Skip the first empty part
			} else if (i == 1) {
				pi.setBeanName(parts[i]);
			} else if (i == 2) {
				pi.setMethodPath(parts[i]);
			} else {
				pi.addPathParameter(parts[i]);
			}
		}
		if (pi.getBeanName() == null) {
			throw new IllegalArgumentException("Invalid URI");
		}
		return pi;
	}

	/**
	 * Invokes the requested method of a CDI bean. The method must be public and take no arguments.
	 * 
	 * @param mi
	 * @param o
	 * @return Returns the outcome object returned by the bean method.
	 * 
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object invokeMethod(MethodInfo mi, Object o)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		logger.fine("Invoking method: " + mi.getMethod().getName());
		
		Object outcome = mi.getMethod().invoke(o);
		
		return outcome;
	}

	/**
	 * Obtain a CDI bean reference using its name.
	 * 
	 * @param name - The name of the CDI bean as declared in @Named annotation.
	 * 
	 * @return - The javax.enterprise.inject.spi.Bean representing the CDI bean.
	 */
	public Bean<?> getBeanReference(String name) {
		logger.fine("Looking for bean: " + name);

		Set<Bean<?>> beans = beanManager.getBeans(name);
		if (!beans.iterator().hasNext()) {
			throw new IllegalArgumentException("Invalid bean name: " + name);
		}
		Bean<?> b = beans.iterator().next();

		/*
		 * Make sure that this bean is exposed.
		 */
		if (!Controller.class.isAssignableFrom(b.getBeanClass())) {
			logger.severe("CDI bean class is not exposed to the web by extending the Controller class: " + b.getBeanClass().getName());
			throw new IllegalAccessError();
		}
		logger.fine("Found bean: " + b.getBeanClass().getName());
		
		return b;
	}
	/**
	 * Obtain an instance of a CDI bean. The effect is same as injecting a bean instance, but using API.
	 * 
	 * @param b - The Bean object as obtained from getBeanReference(String name).
	 * 
	 * @return - An instance of a CDI bean.
	 */
	public Object getBeanInstance(Bean<?> b) {
		CreationalContext<?> cc = beanManager.createCreationalContext(b);
		Object o = beanManager.getReference(b, b.getBeanClass(), cc);

		return o;
	}
	
	/**
	 * Obtain a reference to a CDI bean instance by its name. This is a convenience function
	 * that calls getBeanReference() and getBeanInstance().
	 * 
	 * @param name
	 * @return - An instance of a CDI bean.
	 */
	public Object getBeanInstanceByName(String name) {
		return getBeanInstance(getBeanReference(name));
	}
	
	/**
	 * <p>The central method that manages site navigation. It applies certain rules to the outcome
	 * object returned by a CDI bean method to determine how navigation should be performed.</p>
	 * 
	 * <p>If outcome is null, no navigation is performed. This is usually done when
	 * the bean method takes care of sending a response back. 
	 * If the outcome is not a String then it is serialized as a JSON document into the output stream. 
	 * The content type is then set to "application/json" in that case. Otherwise, if the outcome is a String then a redirection
	 * or forwarding to JSP is performed using the following rules.
	 * If the request is a POST and there is no validation error then outcome is assumed to be a METHOD_PATH or /BEAN_NAME/METHOD_PATH. 
	 * For example: "next_method" or "/next_bean/next_method". Systems redirects the browser to that path. 
	 * If the request is a POST and there are validation errors, then outcome is assumed to be a JSP file name, without the .jsp. System
	 * forwards to that JSP file. If the request is a GET then the outcome is assumed to be a JSP file name and system
	 * forwards to that file. In all cases, JSP file must be located in a folder by the same name as the bean within the web module root. 
	 * In some cases, a redirection may be required after a GET request. In that case start the outcome with a "@". For example:
	 * "@next_method" or "@/next_bean/next_method".</p>
	 * 
	 * @param beanName - The name of the bean that handled this request.
	 * @param outcome - The outcome object returned by the request handler method.
	 * 
	 * @throws Exception
	 */
	protected void navigateTo(String beanName, Object outcome) throws Exception {
		if (outcome == null) {
			// Do nothing.
		} else {
			if (outcome instanceof String) {
				String outcomeStr = (String) outcome;
				if (context.isPostBack() && !context.isValidationFailed()) {
					// Always redirect after a POST
					// outcome = beanName + "/" + outcome;
					redirect(beanName, outcomeStr);
				} else if (outcomeStr.startsWith("@")) {
					outcomeStr = outcomeStr.substring(1);
					// outcome = beanName + "/" + outcome;
					redirect(beanName, outcomeStr);
				} else {
					// Do a forward
					forward(beanName, outcomeStr);
				}
			} else {
				//Respond with JSON
				outputJSON(outcome);
			}
		}
	}

	public void forward(String beanName, String outcome)
			throws ServletException, IOException {
		if (outcome.startsWith("/")) {
			//Absolute outcome. Keep the directory structure
			outcome = outcome + ".jsp";
		} else {
			//Relative outcome. Get JSP from bean's own folder.
			outcome = "/" + beanName + "/" + outcome + ".jsp";
		}
		logger.fine("Forwarding to: " + outcome);
		context.getRequest().getRequestDispatcher(outcome)
				.forward(context.getRequest(), context.getResponse());
	}

	/**
	 * Converts an object to a JSON document and outputs it in the HTTP response stream.
	 * The content type of the response is set to "application/json".
	 * 
	 * @param o - The object to be converted to JSON.
	 */
	public void outputJSON(Object o) throws Exception {
		logger.fine("Sending JSON document back.");
		context.getResponse().setContentType("application/json");
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.writeValue(context.getResponse().getOutputStream(), o);
	}

	/**
	 * Redirects the browser to the next bean handler method.
	 * 
	 * @param beanName - The name of the bean that handled the current request.
	 * @param outcome - The outcome has to be of the form METHOD_PATH or /BEAN_NAME/METHOD_PATH.
	 * The former uses a relative form. In this case, a CDI bean is redirecting to its own method.
	 * The latter uses the absolute form (starting with "/"). This is used when a CDI bean wishes to redirect to
	 * the method of a different CDI bean. 
	 * 
	 * @throws IOException
	 */
	private void redirect(String beanName, String outcome) throws IOException {
		if (outcome.startsWith("/")) {
			outcome = context.getRequest().getContextPath()
					+ context.getRequest().getServletPath()
					+ outcome;
		} else {
			outcome = context.getRequest().getContextPath()
				+ context.getRequest().getServletPath() + "/" + beanName + "/"
				+ outcome;
		}
		logger.fine("Redirecting to: " + outcome);
		context.getResponse().sendRedirect(outcome);
	}

	/**
	 * Core routine that copies input data from all available sources to the target CDI bean.
	 * 
	 * @param request
	 * @param bean
	 * @param o
	 * @param mi
	 * @param pi
	 * @throws Exception
	 */
	protected void transferProperties(HttpServletRequest request,
			Bean<?> bean,
			Object o, MethodInfo mi, PathInfo pi) throws Exception {
		PropertyManager pmgr = new PropertyManager();
		
		pmgr.transferProperties(request, bean.getBeanClass(), o, mi, pi);
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