package com.mobiarch.dts.model;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.mobiarch.dts.entity.AppUser;

@Stateless
public class SecurityManager {
	@PersistenceContext(name = "DTS")
	EntityManager em;
	Logger logger = Logger.getLogger(getClass().getName());

	public AppUser getUser(int id) {
		return em.find(AppUser.class, id);
	}
	
	public AppUser authenticateUser(String email, String password) {
		try {
			byte hash[] = getPasswordHash(password);
			TypedQuery<AppUser> q = em.createQuery("select u from AppUser u where email=:email", AppUser.class);
			
			q.setParameter("email", email.toLowerCase());
			
			AppUser u = q.getSingleResult();
			
			if (Arrays.equals(hash, u.getPassword())) {
				return u;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Login failed", e);
		}
		return null;
	}
	
	public List<AppUser> getAllUser() {
		TypedQuery<AppUser> q = em.createQuery("select u from AppUser u order by fullName", AppUser.class);

		return q.getResultList();
	}
	
	private byte[] getPasswordHash(String password) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
	    digest.reset();
	    byte[] hash = digest.digest(password.getBytes("UTF-8"));
	    
	    return hash;
	}
}
