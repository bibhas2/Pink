package com.mobiarch.nf.test;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Context;

@Named("array")
@RequestScoped
public class ArrayTestController {

	private String strArray[];
	private int intArray[];
	@Inject
	Context context;
	Logger logger = Logger.getLogger(getClass().getName());
	
	public String index() {
		if (context.isPostBack()) {
			for (String str : strArray) {
				logger.fine("Incoming string value: " + str);
			}
			for (int i : intArray) {
				logger.fine("Incoming int value: " + i);
			}
			return "";
		} else {
			String arr[] = {"One", "Two", "Three"};
			strArray = arr;
			int iArr[] = {2, 3};
			intArray = iArr;
		}
		
		return "index";
	}
	public String[] getStrArray() {
		return strArray;
	}
	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}
	public int[] getIntArray() {
		return intArray;
	}
	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}
}
