package com.mobiarch.ajax;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Contact {
	@Size(min=5, max=50, message="Name must be 5-50 characters long")
	private String name;
	@Pattern(regexp="^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message="Please enter a valid email address")
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
