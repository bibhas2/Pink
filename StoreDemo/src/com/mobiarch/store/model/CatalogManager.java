package com.mobiarch.store.model;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class CatalogManager {
	@PersistenceContext(name = "StoreDemo")
	EntityManager em;

	public List<Product> getAllProducts() {
		TypedQuery<Product> q = em.createQuery("select p from Product p",
				Product.class);

		return q.getResultList();
	}

	public Product getProduct(int productId) {
		return em.find(Product.class, productId);
	}
}
