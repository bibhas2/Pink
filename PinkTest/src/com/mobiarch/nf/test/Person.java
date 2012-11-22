package com.mobiarch.nf.test;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Named
@SessionScoped
class Person implements Serializable {
	@Size(min=3, max=25, message="Please enter a valid name")
	@NotNull
	private String firstName;

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}