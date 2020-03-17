package com.mobiarch.nf;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.activation.UnsupportedDataTypeException;
import javax.inject.Inject;

import java.lang.IllegalStateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class PropertyManager {
	@Inject
	private Validator validator;

	public static final String FORMAT_ANNOTATION = "format_annotation";
	
	Logger logger = Logger.getLogger(getClass().getName());
	private static ConcurrentHashMap<String, Map<String, PropertyDescriptor>> descCache = new ConcurrentHashMap<String, Map<String,PropertyDescriptor>>();
	
	public void transferProperties(HttpServletRequest request,
			Class<?> cls,
			Object o, MethodInfo mi, PathInfo pi) throws Exception {
		//First transfer HTTP request parameters
		Map<String, PropertyDescriptor> descMap = getPropertyMap(cls);
		Map<String, String[]> inMap = request.getParameterMap();
		Iterator<Entry<String, String[]>> iter = inMap.entrySet().iterator();
		
		while (iter.hasNext()) {
			Entry<String, String[]> e = iter.next();
			String name = e.getKey();
			String val[] = e.getValue();
			
			setProperty(o, cls, descMap, name, val);
		}
		
		//Next transfer path parameters
		if (mi.getParameterNames().size() != pi.getPathParameters().size()) {
			logger.fine("Number of parameters in @Path annotation doesn't match the URI");
		}
		for (int i = 0; i < mi.getParameterNames().size(); ++i) {
			if (i == pi.getPathParameters().size()) {
				//We don't have any more path parameters
				break;
			}
			String paramName = mi.getParameterNames().get(i);
			String paramValue[] = {pi.getPathParameters().get(i)};
			
			setProperty(o, cls, descMap, paramName, paramValue);
		}
		
		//Transfer multi-part data
		String contentType = request.getContentType();
		
		if (contentType != null && contentType.startsWith("multipart/form-data")) {
			logger.fine("Multi-part request is detected.");
			Iterator<Part> partIter = request.getParts().iterator();
			while (partIter.hasNext()) {
				Part part = partIter.next();
				setMultipartValue(o, cls, descMap, part.getName(), part);
			}
		}
	}

	public void setProperty(Object o, Class<?> cls, Map<String, PropertyDescriptor> descMap, String name,
			String[] values) throws Exception {
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
			
			if (target == null) {
			  throw new IllegalStateException(
			      String.format("Can not set property: %s because %s is null.", name, propName));
			}
			
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
			Object newArray = Array.newInstance(desc.getPropertyType().getComponentType(), values.length);
			for (int i = 0; i < values.length; ++i) {
				Object convertedValue = convertFromString(values[i], desc.getPropertyType().getComponentType(), desc);
				Array.set(newArray, i, convertedValue);
			}
			desc.getWriteMethod().invoke(target, newArray);
		} else {
			try {
				Object arg = convertFromString(values[0], desc.getPropertyType(), desc);
				desc.getWriteMethod().invoke(target, arg);
			} catch (ParseException pe) {
				Format fmt = (Format) desc.getValue(FORMAT_ANNOTATION);
				String msg = null;
				
				if (fmt != null) {
					msg = fmt.message();
				}
				if (msg == null || msg.length() == 0) {
					msg = "Invalid input format for " + desc.getName();
				}
				PropertyViolation pv = new PropertyViolation(msg, desc.getName());
				Context.getContext().addViolation(pv);
			}
		}
	}
	public void setMultipartValue(Object o, Class<?> cls, Map<String, PropertyDescriptor> descMap, String name,
			Part mpPart) throws Exception {
		logger.fine("Setting multi-part property: " + name);
		//Check if this Part has a file content
		String disp = mpPart.getHeader("Content-Disposition");
		if (disp == null) {
			return;
		}
		if (disp.contains("filename=") == false) {
			logger.fine("Skipping non-file part: " + name);
			return;
		}
		
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
		logger.fine("Setting final multi-part property: " + name);
		if (desc.getPropertyType() == Part.class) {
			desc.getWriteMethod().invoke(target, mpPart);
		} else if (desc.getPropertyType() == java.io.InputStream.class) {
			desc.getWriteMethod().invoke(target, mpPart.getInputStream());
		} else {
			throw new UnsupportedDataTypeException("Input type file can be bound to Part of InputStream only.");
		}
	}

	public String getPartValue(Part part) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				part.getInputStream(), "UTF-8"));
		StringBuilder value = new StringBuilder();
		char[] buffer = new char[256];
		for (int length = 0; (length = reader.read(buffer)) > 0;) {
			value.append(buffer, 0, length);
		}
		return value.toString();
	}

	public void validateProperty(Class<?> cls, String name, Object value) {
		logger.fine("Validating URL parameter: " + name);
		Set<?> violations = validator.validateValue(cls, name, value);
		for (Object v : violations) {
			ConstraintViolation<?> vi = (ConstraintViolation<?>) v;
			
			Context.getContext().addViolation(new PropertyViolation(vi));
		}
	}
	
	public void validateBean(Object bean) {
		logger.fine("Validating: " + bean.getClass().getName());
		
		Set<?> violations = validator.validate(bean);
		
		for (Object v : violations) {
			ConstraintViolation<?> vi = (ConstraintViolation<?>) v;
			
			logger.fine("Invalid property: " + vi.getPropertyPath().toString());
			
			Context.getContext().addViolation(new PropertyViolation(vi));
		}
	}

	private Object getProperty(Object o, PropertyDescriptor desc, String name) throws Exception {
		if (desc == null) {
			throw new IllegalArgumentException("Invalid property name: " + name);
		}

		return desc.getReadMethod().invoke(o);
	}
	/**
	 * Returns the value of a property of a bean converted to String.
	 * 
	 * @param cls - The true class of the bean. For a CDI bean this may be different from what getClass() returns.
	 * @param o - The bean object.
	 * @param name - The name of the property. This can be nested with each property separated by a ".". Such as "customer.phone".
	 * @Param bConvert - If true then convert the property value to String. If the property is an array, then a String[] is
	 * returned with each element from the actual property array converted to String.
	 * @return The value of the property converted to String. IllegalArgumentException is thrown if property name is invalid.
	 * @throws Exception
	 */
	public Object getProperty(Class<?> cls, Object o, String name, boolean bConvert) throws Exception {
		logger.fine("GetProperty with conversion: " + bConvert);
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

		if (bConvert) {
			return convertToString(target, desc);
		} else {
			return target;
		}
	}

	/**
	 * Returns the descriptor of a potentially nested property.
	 * 
	 * @param cls - The root owner of the property.
	 * @param name - The name of the property which may be nested (contain ".").
	 * @return The descriptor of the property. If this is a nested property, the descriptor
	 * belongs to the nested bean that actually carries the property.
	 * @throws Exception
	 */
	public PropertyDescriptor getPropertyDescriptor(Class<?> cls, String name) throws Exception {
		String parts[] = name.split("\\.");
		Class<?> targetClass = cls;
		PropertyDescriptor desc = null;
		
		for (int idx = 0; idx < parts.length; ++idx) {
			String propName = parts[idx];
			logger.fine("Resolving target: " + propName);
			Map<String, PropertyDescriptor> targetDescMap = getPropertyMap(targetClass);
			desc = targetDescMap.get(propName);
			if (desc == null) {
				throw new IllegalArgumentException("Invalid property name: " + propName);
			}
			targetClass = desc.getPropertyType();
		}
		
		return desc;
	}

	public Object getProperty(Class<?> cls, Object o, String name) throws Exception {
		logger.fine("Default getProperty called");
		return getProperty(cls, o, name, true);
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
		  logger.fine(String.format("Saving property: %s for class: %s", desc.getName(), clsName));
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

	public Object convertFromString(String str, Class<?> type, PropertyDescriptor desc) throws ParseException, UnsupportedDataTypeException {
		Format fmt = (Format) desc.getValue(FORMAT_ANNOTATION);
		String formatStr = null;
		Context ctx = Context.getContext();
		NumberFormat nFmt = null;
		
		if (fmt != null) {
			formatStr = fmt.pattern();
			//Java annotation doesn't allow null attribute value.
			//Normalize an empty string to null.
			if (formatStr != null && formatStr.length() == 0) {
				formatStr = null;
			}
		}

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
		} else if (type == java.util.Date.class) {
			if (str == null || str.length() == 0) {
				return null;
			}
			return stringToDate(str, formatStr, ctx);
		} else if (type == java.sql.Date.class) {
			if (str == null || str.length() == 0) {
				return null;
			}
			java.util.Date d = stringToDate(str, formatStr, ctx);
			
			return new java.sql.Date(d.getTime());
		} else {
			throw new UnsupportedDataTypeException("Can not convert string to: " + type.getName());
		}
	}
	
	private java.util.Date stringToDate(String str, String formatStr, Context ctx) throws ParseException {
		if (formatStr == null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, ctx.getLocale());
			
			return df.parse(str);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr, ctx.getLocale());
			
			return sdf.parse(str);
		}
	}

	private Object convertToString(Object o, PropertyDescriptor desc) {
		Format fmt = (Format) desc.getValue(FORMAT_ANNOTATION);
		String formatStr = null;
		Context ctx = Context.getContext();
		NumberFormat nFmt = null;

		if (o == null) {
			return "";
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
			if (formatStr == null || formatStr.length() == 0) {
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
		} else if (cls == java.util.Date.class) {
			return dateToString((java.util.Date) o, formatStr);
		} else if (cls == java.sql.Date.class) {
			java.util.Date d = new java.util.Date(((java.sql.Date) o).getTime());
			return dateToString(d, formatStr);
		} else if (cls.isArray()) {
			logger.fine("Converting array property to String[]");
			if (cls.getComponentType() == String.class) {
				logger.fine("Property is String[]. Returning as is.");
				return o;
			}
			int length = Array.getLength(o);
			String strArray[] = new String[length];
			for (int i = 0; i < length; ++i) {
				Object item = Array.get(o, i);
				strArray[i] = (String) convertToString(item, desc);
			}
			//For array property, we return String[].
			return strArray;
		}
		
		return o.toString();
	}

	private String dateToString(java.util.Date d, String formatStr) {
		Context ctx = Context.getContext();
		
		if (formatStr == null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, ctx.getLocale());
			
			return df.format(d);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(formatStr, ctx.getLocale());
			
			return sdf.format(d);
		}
	}
}
