package com.mobiarch.store.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * The persistent class for the Address database table.
 * 
 */
@Entity
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Size(min=2, message="Please enter a valid city name")
	private String city;

	@Size(min=2, message="Please enter a valid country name")
	private String country;

	@Size(min=2, message="Please enter a valid customer name")
	private String customerName;
	@Pattern(regexp="^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message="Please enter a valid email address")
	private String email;

	private String phone;

	@Size(min=2, message="Please enter a valid state name")
	private String state;

	@Size(min=2, message="Please enter a valid street address")
	private String street1;

	private String street2;

	@Size(min=2, message="Please enter a valid ZIP code")
	private String zip;

	public Address() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet1() {
		return this.street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return this.street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

}