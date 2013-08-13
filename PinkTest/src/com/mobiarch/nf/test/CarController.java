package com.mobiarch.nf.test;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("cars")
@RequestScoped
public class CarController {
	ArrayList<CarModel> availableModels;
	int selectedModel[];
	CarModel carModel = new CarModel(0, null);
	
	@PostConstruct
	public void init() {
		availableModels = new ArrayList<CarModel>();
		availableModels.add(new CarModel(1, "Honda Civic"));
		availableModels.add(new CarModel(2, "Toyota Camry"));
		availableModels.add(new CarModel(3, "GLK 350"));
	}

	public ArrayList<CarModel> getAvailableModels() {
		return availableModels;
	}

	public int[] getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(int[] selectedModel) {
		this.selectedModel = selectedModel;
	}
	public String index() {
		return "index";
	}
	public String makeSelection() {
		for (int id : selectedModel) {
			System.out.println("Selected model: " + id);
		}
		
		return "";
	}
	public String makeNestedSelection() {
		System.out.println("Selected model: " + carModel.id);
		
		return "index";
	}

	public CarModel getCarModel() {
		return carModel;
	}

	public void setCarModel(CarModel carModel) {
		this.carModel = carModel;
	}
}
