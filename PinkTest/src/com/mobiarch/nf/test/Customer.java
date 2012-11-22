package com.mobiarch.nf.test;

import javax.validation.constraints.Size;

import com.mobiarch.nf.Format;

public class Customer {
	private String id;
	@Size(min=3, max=15, message="The name must be at least 3 and maximum 15 characters long")
	private String fullName;
	@Size(min=3, max=25, message="Please enter a valid e-mail")
	private String email;
	private int department;
	private String level;
	private String sendEmail;
	private int age;
	
	@Format(pattern="#.##", message="Please enter a valid salary")
	private float salary;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Customer() {
		
	}
	public Customer(String id, String fullName, String email) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getDepartment() {
		return department;
	}
	public void setDepartment(int department) {
		this.department = department;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
}
