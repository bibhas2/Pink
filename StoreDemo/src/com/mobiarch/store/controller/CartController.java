package com.mobiarch.store.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;
import com.mobiarch.nf.Path;
import com.mobiarch.nf.PropertyViolation;
import com.mobiarch.store.model.Address;
import com.mobiarch.store.model.Cart;
import com.mobiarch.store.model.CartManager;

@Named("cart")
@RequestScoped
public class CartController extends Controller {
	Cart cart;
	Address address = new Address();
	
	int orderId;
	int cartItemId;
	int productId;
	int quantity = 1;
	boolean sameAsShipping = false;
	
	@EJB
	CartManager cmgr;
	
	@Inject
	Context context;
	@Inject
	EphemeralCheckoutData payment;
	
	/**
	 * Default method. Shows the shopping cart.
	 * 
	 * @return
	 */
	public String index() {
		int cartId = getCartId();
		
		if (cartId == 0) {
			//No cart
			return "empty_cart";
		}
		cart = cmgr.getCartPopulated(cartId);
		if (cart.getCartItems().size() == 0) {
			return "empty_cart";
		}
		return "cart";
	}

	public String add() {
		int cartId = cmgr.addToCart(getCartId(), productId, quantity);
		
		setCartId(cartId);
		
		return ""; //index
	}

	public String summary() {
		int cartId = getCartId();
		
		if (cartId == 0) {
			//No cart
			return "empty_cart";
		}
		cart = cmgr.getCartPopulated(cartId);
		if (cart.getCartItems().size() == 0) {
			return "empty_cart";
		}
		return "summary";
	}

	public String update() {
		int cartId = getCartId();
		
		cmgr.updateQuantity(cartId, cartItemId, quantity);
		
		return "";
	}
	@Path("cartItemId")
	public String delete() {
		int cartId = getCartId();
		
		cmgr.deleteCartItem(cartId, cartItemId);
		
		return "@";
	}
	
	public String shipping() {
		int cartId = getCartId();
		
		if (!context.isPostBack()) {
			address = cmgr.getShippingAddress(cartId);
			if (address == null) {
				address = new Address();
			}
			
			return "shipping";
		} else {
			if (context.isValidationFailed()) {
				return "shipping";
			}
			cmgr.setShippingAddress(cartId, address);
			
			return "billing";
		}
	}
	public String billing() {
		int cartId = getCartId();
		if (!context.isPostBack()) {
			sameAsShipping = payment.isUseShipingAddressForBilling();
			address = cmgr.getBillingAddress(cartId);
			
			return "billing";
		} else {
			if (context.isValidationFailed()) {
				return "billing";
			}
			
			payment.setUseShipingAddressForBilling(sameAsShipping);
			
			if (sameAsShipping) {
				cmgr.setBillingAddressFromShipping(cartId);
			} else {
				cmgr.setBillingAddress(cartId, address);
			}
			return "summary";
		}
	}
	
	public String place() {
		int cartId = getCartId();
		
		try {
			cmgr.placeOrder(cartId);
		} catch (Exception e) {
			context.addViolation(new PropertyViolation(e.getMessage(), ""));
			
			return "summary";
		}
		//Clear ephemeral data in session
		setCartId(0);
		payment.reset();
		
		return "confirmation/" + cartId;
	}
	
	@Path("orderId")
	public String confirmation() {
		/*
		 * TODO: Need to do a security check to make sure that
		 * the user owns this order.
		 */
		
		
		cart = cmgr.getCartPopulated(orderId);
		
		return "confirmation";
	}

	public int getCartId() {
		Integer cartId = (Integer) context.getRequest().getSession().getAttribute("cartId");
		
		if (cartId != null) {
			return cartId.intValue();
		}
		return 0;
	}

	public void setCartId(int cartId) {
		context.getRequest().getSession().setAttribute("cartId", new Integer(cartId));
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public int getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(int cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public EphemeralCheckoutData getPayment() {
		return payment;
	}
	public boolean isSameAsShipping() {
		return sameAsShipping;
	}
	public void setSameAsShipping(boolean sameAsShipping) {
		this.sameAsShipping = sameAsShipping;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
