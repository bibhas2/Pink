package com.mobiarch.store.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.nf.Controller;
import com.mobiarch.nf.Path;
import com.mobiarch.store.model.CatalogManager;
import com.mobiarch.store.model.Product;

@Named("catalog")
@RequestScoped
public class CatalogController extends Controller {
	@EJB
	CatalogManager cmgr;
	int productId;
	int quantity = 1;

	@Inject
	CartController cartController;
	
	List<Product> products;
	Product product;
	
	@Path("/product/productId")
	public String show() {
		product = cmgr.getProduct(productId);
		
		return "product";
	}

	public String index() {
		products = cmgr.getAllProducts();
		
		return "all_products";
	}
	
	public String addToCart() {
		cartController.addToCart(productId, quantity);
		
		return "/cart"; //index
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
