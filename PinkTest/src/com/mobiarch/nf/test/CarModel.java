package com.mobiarch.nf.test;

public class CarModel {
	int id;
	String modelName;
	
	public CarModel(int id, String name) {
		this.id = id;
		this.modelName = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String name) {
		this.modelName = name;
	}
}
