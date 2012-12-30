package com.mobiarch.store.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.mobiarch.nf.Format;
/**
 * This class contains data gathered during checkout but never
 * saved in the database. This is mainly Credit Card data that is 
 * never stored for PCI compliance.
 * 
 * @author wasadmin
 *
 */
@Named
@SessionScoped
public class EphemeralCheckoutData implements Serializable {
	private static final long serialVersionUID = 1266861259607101377L;
	private String cardType;
	private String cardNumber;
	@Size(min=2, max=2, message="Please enter a valid expiry month")
	private String expMonth;
	@Size(min=2, max=2, message="Please enter a valid expiry year")
	private String expYear;
	@Size(min=2, message="Please enter a valid security code")
	private String securityCode;
	private boolean useShipingAddressForBilling = false;
	
	private int cartId;
	private int lastOrderId;
	
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getExpMonth() {
		return expMonth;
	}
	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}
	public String getExpYear() {
		return expYear;
	}
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public boolean isUseShipingAddressForBilling() {
		return useShipingAddressForBilling;
	}
	public void setUseShipingAddressForBilling(boolean useShipingAddressForBilling) {
		this.useShipingAddressForBilling = useShipingAddressForBilling;
	}
	public void reset() {
		useShipingAddressForBilling = false;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public int getLastOrderId() {
		return lastOrderId;
	}
	public void setLastOrderId(int lastOrderId) {
		this.lastOrderId = lastOrderId;
	}
}
