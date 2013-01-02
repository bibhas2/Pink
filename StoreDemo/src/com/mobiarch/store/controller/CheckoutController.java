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

@Named("checkout")
@RequestScoped
public class CheckoutController extends Controller {
	Cart cart;
	Address address = new Address();
	
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
	 * Shows the shopping cart.
	 * 
	 * @return
	 */
	public String cart() {
		int cartId = payment.getCartId();
		
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
		int cartId = cmgr.addToCart(payment.getCartId(), productId, quantity);
		
		payment.setCartId(cartId);
		
		return "cart"; //index
	}

	public String summary() {
		int cartId = payment.getCartId();
		
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
		int cartId = payment.getCartId();
		if (cartId == 0) {
			return ""; //Show empty shop cart
		}
		cmgr.updateQuantity(cartId, cartItemId, quantity);
		
		return "cart";
	}
	
	@Path("cartItemId")
	public String delete() {
		int cartId = payment.getCartId();
		
		if (cartId == 0) {
			return "@cart"; //Show empty shop cart
		}
		cmgr.deleteCartItem(cartId, cartItemId);
		
		return "@cart";
	}
	
	public String shipping() {
		int cartId = payment.getCartId();
		
		if (!context.isPostBack()) {
			if (cartId == 0) {
				return "@cart"; //Show empty shop cart
			}
			address = cmgr.getShippingAddress(cartId);
			if (address == null) {
				address = new Address();
			}
			
			return "shipping";
		} else {
			if (cartId == 0) {
				return "cart"; //Show empty shop cart
			}
			if (context.isValidationFailed()) {
				return "shipping";
			}
			cmgr.setShippingAddress(cartId, address);
			
			return "billing";
		}
	}
	public String billing() {
		int cartId = payment.getCartId();
		if (!context.isPostBack()) {
			if (cartId == 0) {
				return "@cart"; //Show empty shop cart
			}
			sameAsShipping = payment.isUseShipingAddressForBilling();
			address = cmgr.getBillingAddress(cartId);
			
			return "billing";
		} else {
			if (cartId == 0) {
				return "cart"; //Show empty shop cart
			}
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
	
	@Path("/place-order")
	public String placeOrder() {
		int cartId = payment.getCartId();
		
		if (cartId == 0) {
			return "cart"; //Show empty shop cart
		}

		try {
			cmgr.placeOrder(cartId);
			payment.setCartId(0);
			payment.setLastOrderId(cartId);
		} catch (Exception e) {
			context.addViolation(new PropertyViolation(e.getMessage(), ""));
			
			return "summary";
		}
		
		return "confirmation";
	}
	
	public String confirmation() {
		int lastOrderId = payment.getLastOrderId();
		
		if (lastOrderId == 0) {
			return "@cart"; //Empty cart
		}
		
		cart = cmgr.getCartPopulated(lastOrderId);
		
		return "confirmation";
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
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
