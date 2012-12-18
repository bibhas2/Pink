package com.mobiarch.store.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;


/**
 * The persistent class for the Product database table.
 * 
 */
@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String description;

	private String imageLink;

	private String name;

	private BigDecimal price;

	private String sku;

	public Product() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLink() {
		return this.imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}