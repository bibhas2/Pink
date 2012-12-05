package com.mobiarch.nf.test;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;

@Named("select")
@RequestScoped
public class SelectTest extends Controller {
	int selectedValue[] = null;
	Logger logger = Logger.getLogger(getClass().getName());
	String singleSelectedValue = "TH";
	@Inject
	Context context;
	
	@PostConstruct
	public void init() {
		if (!context.isPostBack()) {
			int tmp[] = {1, 3};
			selectedValue = tmp;
		}
	}
	public String optionTest() {
		if (selectedValue == null) {
			logger.severe("Value is null");
		} else {
			for (int val : selectedValue) {
				logger.info("Selected: " + val);
			}
		}
		logger.info("Single selection: " + singleSelectedValue);
		
		return "index";
	}
	public String index() {
		return "index";
	}

	public int[] getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(int[] selectedValue) {
		this.selectedValue = selectedValue;
	}
	public String getSingleSelectedValue() {
		return singleSelectedValue;
	}
	public void setSingleSelectedValue(String singleSelectedValue) {
		this.singleSelectedValue = singleSelectedValue;
	}
}
