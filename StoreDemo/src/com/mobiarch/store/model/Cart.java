package com.mobiarch.store.model;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Cart database table.
 * 
 */
@Entity
public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private Integer billingAddressId;

	private BigDecimal grandTotal;

	@Temporal(TemporalType.DATE)
	private Date placedOn;

	private BigDecimal productTax;

	private BigDecimal productTotal;

	private BigDecimal shipping;

	private Integer shippingAddressId;

	private BigDecimal shippingTax;

	private String status;

	@Transient
	List<CartItem> cartItems;
	@Transient
	Address shippingAddress;
	@Transient
	Address billingAddress;
	
	public Cart() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getBillingAddressId() {
		return this.billingAddressId;
	}

	public void setBillingAddressId(Integer billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public BigDecimal getGrandTotal() {
		return this.grandTotal;
	}

	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Date getPlacedOn() {
		return this.placedOn;
	}

	public void setPlacedOn(Date placedOn) {
		this.placedOn = placedOn;
	}

	public BigDecimal getProductTax() {
		return this.productTax;
	}

	public void setProductTax(BigDecimal productTax) {
		this.productTax = productTax;
	}

	public BigDecimal getProductTotal() {
		return this.productTotal;
	}

	public void setProductTotal(BigDecimal productTotal) {
		this.productTotal = productTotal;
	}

	public BigDecimal getShipping() {
		return this.shipping;
	}

	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}

	public Integer getShippingAddressId() {
		return this.shippingAddressId;
	}

	public void setShippingAddressId(Integer shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public BigDecimal getShippingTax() {
		return this.shippingTax;
	}

	public void setShippingTax(BigDecimal shippingTax) {
		this.shippingTax = shippingTax;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}
}