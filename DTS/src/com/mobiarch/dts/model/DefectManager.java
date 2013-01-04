package com.mobiarch.dts.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.mobiarch.dts.entity.AppUser;
import com.mobiarch.dts.entity.Defect;
import com.mobiarch.dts.entity.DefectLog;
import com.mobiarch.dts.entity.Project;

@Stateless
public class DefectManager {
	@PersistenceContext(name = "DTS")
	EntityManager em;
	Logger logger = Logger.getLogger(getClass().getName());
	@EJB
	SecurityManager smgr;
	
	public static final String STATE_OPEN      = "Open";
	public static final String STATE_ASSIGNED  = "Assigned";
	public static final String STATE_COMPLETED = "Completed";
	public static final String STATE_RETURNED  = "Returned";
	public static final String STATE_ACCEPTED  = "Accepted";
	public static final String STATE_VERIFIED  = "Verified";
	public static final String STATE_DEFERRED  = "Deferred";
	public static final String STATE_CANCELLED = "Cancelled";
	public static final String STATE_REJECTED  = "Rejected";

	public void createProject(Project obj) {
		logger.fine("Creating a defect tracking project: " +
			obj.getName() +
			" Owner: " +
			obj.getOwnerId());
		em.persist(obj);
	}
	public List<Project> getAllProject() {
		TypedQuery<Project> q = em.createQuery("select p from Project p", Project.class);
		
		return q.getResultList();
	}
	public Project getProject(int id) {
		return em.find(Project.class, id);
	}
	
	public void createDefect(Defect obj) {
		obj.setStateId(STATE_OPEN);
		
		em.persist(obj);
		
		AppUser user = smgr.getUser(obj.getOriginatorId());
		createDefectLog(obj.getId(), "Defect opened", user.getFullName());
	}
	
	public void createDefectLog(int defectId, String action, String actor) {
		DefectLog dl = new DefectLog();
		
		dl.setActionText(action);
		dl.setDefectId(defectId);
		dl.setActorName(actor);
		dl.setLastupdate(new Timestamp((new Date()).getTime()));
		em.persist(dl);
	}
}
