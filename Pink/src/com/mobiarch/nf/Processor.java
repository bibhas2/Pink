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
	private static ConcurrentHashMap<String, MethodInfo> methodCache = new ConcurrentHashMap<String, MethodInfo>();
	
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		context.setRequest(request);
		context.setResponse(response);
		context.setProcessor(this);
		contextVar.set(context);

		PathInfo pInfo = resolvePathInfo(request);
		Bean<?> bean = getBeanReference(pInfo.getBeanName());
		Object o = getBeanInstance(bean);
		MethodInfo mInfo = resolveMethod(pInfo, bean.getBeanClass());
		transferProperties(request, bean, o);
		String outcome = invokeMethod(mInfo, o);
		navigateTo(pInfo.getBeanName(), outcome.toString());
	}

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
					throw new IllegalArgumentException("Path annotation is misisng a value");
				}
				String parts[] = val.split("\\/");
				boolean isAbsolute = parts[0].length() == 0; //true if path starts with "/".
				if (isAbsolute) {
					logger.fine("Absolute path is set for method.");
					for (int j = 0; j < parts.length; ++j) {
						if (i == 0) {
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

	public PathInfo resolvePathInfo(HttpServletRequest request) {
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

	private String invokeMethod(MethodInfo mi, Object o)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		logger.info("Invoking method: " + mi.getMethod().getName());
		
		Object outcome = mi.getMethod().invoke(o);
		
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
