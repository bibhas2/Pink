package com.mobiarch.store.model;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class CartManager {
	@PersistenceContext(name = "StoreDemo")
	EntityManager em;
	public static final String STATUS_PENDING = "P";
	public static final String STATUS_CONFIRMED = "C";
	public static final String STATUS_CANCELLED = "X";
	public static final String STATUS_PAYMENT_DEPOSITED = "D";
	public static final String STATUS_PAYMENT_FAILED = "F";
	public static final String STATUS_SHIPPED = "S";
	
	@EJB
	CatalogManager catalogMgr;
	
	/**
	 * Adds a product to the cart. Creates a cart if one doesn't exist.
	 * 
	 * @param cartId - The cart to add to. May be 0 if no cart exists.
	 * @param productId - The product to add.
	 * @param quantity - The amount.
	 * @return The id of the cart where the product was added. This may be a newly created cart.
	 */
	public int addToCart(int cartId, int productId, int quantity) {
		if (cartId <= 0) {
			//Create a new cart
			Cart c = new Cart();
			
			c.setStatus(STATUS_PENDING);
			em.persist(c);
			
			cartId = c.getId();
		}
		//If product was already in cart, just increase quantity
		List<CartItem> list = getCartItems(cartId);
		for (CartItem i : list) {
			if (i.getProductId() == productId) {
				i.setQuantity(quantity + i.getQuantity());
				return cartId;
			}
		}
		
		CartItem ci = new CartItem();
		
		ci.setCartId(cartId);
		ci.setProductId(productId);
		ci.setQuantity(quantity);
		
		em.persist(ci);
		
		return cartId;
	}
	public Cart getCart(int cartId) {
		return em.find(Cart.class, cartId);
	}
	public List<CartItem> getCartItems(int cartId) {
		TypedQuery<CartItem> q = em.createQuery("select ci from CartItem ci where ci.cartId=?1",
				CartItem.class);
		q.setParameter(1, cartId);
		
		return q.getResultList();
	}
	public CartItem getCartItem(int cartId, int cartItemId) {
		TypedQuery<CartItem> q = em.createQuery("select ci from CartItem ci where ci.cartId=?1 and ci.id=?2",
				CartItem.class);
		q.setParameter(1, cartId);
		q.setParameter(2, cartItemId);
		
		return q.getSingleResult();
	}
	
	public Cart getCartPopulated(int cartId) {
		Cart cart = getCart(cartId);
		List<CartItem> cartItems = getCartItems(cartId);
		
		for (CartItem ci : cartItems) {
			ci.setProduct(catalogMgr.getProduct(ci.getProductId()));
		}
		cart.setCartItems(cartItems);
		
		//Get the addresses
		cart.setShippingAddress(getShippingAddress(cartId));
		cart.setBillingAddress(getBillingAddress(cartId));

		computeTotal(cart);
		
		return cart;
	}
	/**
	 * Computes the grand total for a cart, inclusive of all tax and shipping.
	 * 
	 * @param cart The cart for which total has to be calculated.
	 */
	public void computeTotal(Cart cart) {
		BigDecimal grandTotal = new BigDecimal(0.00);
		
		for (CartItem ci : cart.getCartItems()) {
			BigDecimal lineTotal = ci.getProduct().getPrice().multiply(new BigDecimal(ci.getQuantity()));
			
			ci.setItemTotal(lineTotal);
			
			grandTotal = grandTotal.add(lineTotal);
		}
		
		cart.setGrandTotal(grandTotal);
	}
	
	public void updateQuantity(int cartId, int cartItemId, int quantity) {
		CartItem ci = getCartItem(cartId, cartItemId);
		
		if (quantity <= 0) {
			//Delete
			em.remove(ci);
		} else {
			ci.setQuantity(quantity);
		}
	}
	public void deleteCartItem(int cartId, int cartItemId) {
		CartItem ci = getCartItem(cartId, cartItemId);
		
		em.remove(ci);
	}
	public Address getShippingAddress(int cartId) {
		Cart cart = getCart(cartId);
		
		if (cart.getShippingAddressId() != null) {
			return getAddress(cart.getShippingAddressId());
		}
		return null;
	}
	
	public Address getBillingAddress(int cartId) {
		Cart cart = getCart(cartId);
		
		if (cart.getBillingAddressId() != null) {
			return getAddress(cart.getBillingAddressId());
		}
		return null;
	}
	
	public Address getAddress(Integer addressId) {
		return em.find(Address.class, addressId);
	}
	public void setShippingAddress(int cartId, Address address) {
		em.persist(address);
		Cart cart = getCart(cartId);
		
		cart.setShippingAddressId(address.getId());
	}
	public void setBillingAddressFromShipping(int cartId) {
		Cart cart = getCart(cartId);
		Address shippingAddress = getAddress(cart.getShippingAddressId());
		Address address = new Address();
		
		address.setCustomerName(shippingAddress.getCustomerName());
		address.setStreet1(shippingAddress.getStreet1());
		address.setStreet2(shippingAddress.getStreet2());
		address.setCity(shippingAddress.getCity());
		address.setState(shippingAddress.getState());
		address.setZip(shippingAddress.getZip());
		address.setCountry(shippingAddress.getCountry());
		address.setEmail(shippingAddress.getEmail());
		address.setPhone(shippingAddress.getPhone());
		
		em.persist(address);
		cart.setBillingAddressId(address.getId());
	}
	public void setBillingAddress(int cartId, Address address) {
		Cart cart = getCart(cartId);
		em.persist(address);
		cart.setBillingAddressId(address.getId());
	}
	public void placeOrder(int cartId) {
		Cart cart = getCart(cartId);
		
		//Process payment
		
		cart.setStatus(STATUS_PAYMENT_DEPOSITED);
	}
}
