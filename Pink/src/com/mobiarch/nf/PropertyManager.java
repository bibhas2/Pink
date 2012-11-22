package com.mobiarch.nf;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class PropertyManager {
	public static final String FORMAT_ANNOTATION = "format_annotation";
	
	Logger logger = Logger.getLogger(getClass().getName());
	private static ConcurrentHashMap<String, Map<String, PropertyDescriptor>> descCache = new ConcurrentHashMap<String, Map<String,PropertyDescriptor>>();
	
	public void transferProperties(HttpServletRequest request,
			Class<?> cls,
			Object o) throws Exception {
		Map<String, PropertyDescriptor> descMap = getPropertyMap(cls);
		Map<String, String[]> inMap = request.getParameterMap();
		Iterator<Entry<String, String[]>> iter = inMap.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, String[]> e = iter.next();
			String name = e.getKey();
			String val[] = e.getValue();
			
			setProperty(o, cls, descMap, name, val);
		}
	}

	public void setProperty(Object o, Class<?> cls, Map<String, PropertyDescriptor> descMap, String name,
			String[] val) throws Exception {
		logger.fine("Setting property: " + name);
		
		String parts[] = name.split("\\.");
		Object target = o;
		Map<String, PropertyDescriptor> targetDescMap = descMap;
		Class<?> targetClass = cls;
		int idx = 0;
		
		while (idx < parts.length - 1) {
			String propName = parts[idx];
			logger.fine("Resolving target: " + propName);
			target = getProperty(target, targetDescMap.get(propName), propName);
			targetClass = target.getClass();
			targetDescMap = getPropertyMap(targetClass);
			++idx;
		}
		
		name = parts[idx];
		PropertyDescriptor desc = targetDescMap.get(name);
		
		if (desc == null) {
			logger.fine("Invalid property name: " + name);
			return;
		}
		logger.fine("Setting final property: " + name);
		if (desc.getPropertyType().isArray()) {
			Object arg[] = {val};
			desc.getWriteMethod().invoke(target, arg);
		} else {
			try {
				Object arg = convertFromString(val[0], desc);
				desc.getWriteMethod().invoke(target, arg);
				validateProperty(targetClass, desc.getName(), arg);
			} catch (ParseException pe) {
				Format fmt = (Format) desc.getValue(FORMAT_ANNOTATION);
				String msg = null;
				
				if (fmt != null) {
					msg = fmt.message();
				}
				if (msg == null) {
					msg = "Invalid input format for " + desc.getName();
				}
				PropertyViolation pv = new PropertyViolation(msg, desc.getName());
				Context.getContext().addViolation(pv);
			}
		}
	}

	public void validateProperty(Class<?> cls, String name, Object value) {
		Validator validator = Processor.getProcessor().getValidator();
		logger.fine("Validating URL parameter: " + name);
		Set<?> violations = validator.validateValue(cls, name, value);
		for (Object v : violations) {
			ConstraintViolation<?> vi = (ConstraintViolation<?>) v;
			
			Context.getContext().addViolation(new PropertyViolation(vi));
		}
	}

	private Object getProperty(Object o, PropertyDescriptor desc, String name) throws Exception {
		if (desc == null) {
			throw new IllegalArgumentException("Invalid property name: " + name);
		}

		return desc.getReadMethod().invoke(o);
	}
	
	public Object getProperty(Class<?> cls, Object o, String name) throws Exception {
		String parts[] = name.split("\\.");
		Object target = o;
		Class<?> targetClass = cls;
		Map<String, PropertyDescriptor> targetDescMap = null;
		PropertyDescriptor desc = null;
		
		for (int idx = 0; idx < parts.length; ++idx) {
			String propName = parts[idx];
			logger.fine("Resolving target: " + propName);
			targetDescMap = getPropertyMap(targetClass);
			desc = targetDescMap.get(propName);
			target = getProperty(target, desc, propName);
			if (target != null) {
				logger.fine("Found target: " + target.getClass().getName());
			} else {
				return null;
			}
			targetClass = target.getClass();
		}

		return convertToString(target, desc);
	}

	public Map<String, PropertyDescriptor> getPropertyMap(Class<?> cls) throws Exception {
		String clsName = cls.getName();
		Map<String, PropertyDescriptor> map = descCache.get(clsName);
		
		if (map != null) {
			logger.fine("Got a cache hit.");
			return map;
		}
		
		/*
		 * It is OK if multiple threads are loading
		 * property descriptor for the same class at the same time.
		 * The duplicate work will only happen at the beginning of an application's lifetime.
		 */
		BeanInfo bi = Introspector.getBeanInfo(cls);
		PropertyDescriptor descList[] = bi.getPropertyDescriptors();
		map = new ConcurrentHashMap<String, PropertyDescriptor>(descList.length);
		
		for (PropertyDescriptor desc : descList) {
			map.put(desc.getName(), desc);
			//Save the Format annotation if present
			try {
				Field f = cls.getDeclaredField(desc.getName());
				Format fmt = f.getAnnotation(Format.class);
				if (fmt != null) {
					desc.setValue(FORMAT_ANNOTATION, fmt);
				}
			} catch (NoSuchFieldException ex) {
				//No field matching property name
				logger.fine("No field for property: " + desc.getName());
			}
		}
		
		descCache.putIfAbsent(clsName, map);
		
		return map;
	}

	public Object convertFromString(String str, PropertyDescriptor desc) throws ParseException {
		Class<?> type = desc.getPropertyType();
		Context ctx = Context.getContext();
		NumberFormat nFmt = null;
		
		if (type == String.class) {
			return str;
		} else if (type == Integer.class || type == int.class) {
			nFmt = NumberFormat.getIntegerInstance(ctx.getLocale());
			
			return nFmt.parse(str).intValue();
		} else if (type == Float.class || type == float.class) {
			nFmt = NumberFormat.getNumberInstance(ctx.getLocale());
						
			return nFmt.parse(str).floatValue();
		} else if (type == Double.class || type == double.class) {
			nFmt = NumberFormat.getNumberInstance(ctx.getLocale());
			
			return nFmt.parse(str).doubleValue();
		} else if (type == Boolean.class || type == boolean.class) {
			//Only case insensitive "true" evaluates to true. All else is false.
			return new Boolean(str);
		}
		return str;
	}
	
	public String convertToString(Object o, PropertyDescriptor desc) {
		String val = "";
		Format fmt = (Format) desc.getValue(FORMAT_ANNOTATION);
		String formatStr = null;
		Context ctx = Context.getContext();
		NumberFormat nFmt = null;

		if (o == null) {
			return val;
		}
		
		if (fmt != null) {
			formatStr = fmt.pattern();
		}

		Class<?> cls = o.getClass();
		
		logger.fine("Converting from: " + cls.getName());
		
		if (cls == String.class) {
			return (String) o;
		}
		
		if (cls == Integer.class) {
			nFmt = NumberFormat.getIntegerInstance(ctx.getLocale());
			return nFmt.format(o);
		} else if (cls == Float.class || cls == Double.class) {
			if (formatStr == null) {
				nFmt = NumberFormat.getNumberInstance(ctx.getLocale());
				
				return nFmt.format(o);
			} else {
				nFmt = NumberFormat.getNumberInstance(ctx.getLocale());
				if (nFmt instanceof DecimalFormat) {
					DecimalFormat dfmt = (DecimalFormat) nFmt;
					
					dfmt.applyPattern(formatStr);
					
					return dfmt.format(o);
				}
				return nFmt.format(o);
			}
		}
		
		return o.toString();
	}
}
