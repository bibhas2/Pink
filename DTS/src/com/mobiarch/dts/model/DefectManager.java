package com.mobiarch.dts.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.activity.InvalidActivityException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.mobiarch.dts.controller.SessionData;
import com.mobiarch.dts.entity.AppUser;
import com.mobiarch.dts.entity.Defect;
import com.mobiarch.dts.entity.DefectComment;
import com.mobiarch.dts.entity.DefectLog;
import com.mobiarch.dts.entity.Project;

@Stateless
public class DefectManager {
	@PersistenceContext(name = "DTS")
	EntityManager em;
	Logger logger = Logger.getLogger(getClass().getName());
	@EJB
	SecurityManager smgr;
	@Inject
	SessionData session;
	
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
	
	public List<Project> getAllProjectPopulated() {
		List<Project> list = getAllProject();
		
		for (Project p : list) {
			p.setOwner(smgr.getUser(p.getOwnerId()));
		}
		
		return list;
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
	public void updateDefect(Defect mod) {
		Defect old = getDefect(mod.getId());
		
		old.setProjectId(mod.getProjectId());
		old.setSeverity(mod.getSeverity());
		old.setPriority(mod.getPriority());
	}
	public void createDefectLog(int defectId, String action, String actor) {
		DefectLog dl = new DefectLog();
		
		dl.setActionText(action);
		dl.setDefectId(defectId);
		dl.setActorName(actor);
		dl.setLastupdate(new Timestamp((new Date()).getTime()));
		em.persist(dl);
	}
	
	List<DefectComment> getCommentsForDefect(int defectId) {
		TypedQuery<DefectComment> q = em.createQuery("select c from DefectComment c where c.defectId=:defectId order by c.lastupdate asc", DefectComment.class);
		
		q.setParameter("defectId", defectId);
		
		List<DefectComment> list = q.getResultList();
		
		for (DefectComment d : list) {
			d.setUser(smgr.getUser(d.getUserId()));
		}
		
		return list;
	}
	
	List<DefectLog> getLogForDefect(int defectId) {
		TypedQuery<DefectLog> q = em.createQuery("select c from DefectLog c where c.defectId=:defectId order by c.lastupdate asc", DefectLog.class);
		
		q.setParameter("defectId", defectId);
		
		return q.getResultList();
	}

	public Defect getDefectPopulated(int id) {
		Defect d = em.find(Defect.class, id);
		
		if (d == null) {
			return null;
		}
		
		d.setOriginator(smgr.getUser(d.getOriginatorId()));
		d.setOwner(smgr.getUser(d.getOwnerId()));
		d.setProject(getProject(d.getProjectId()));
		d.setCommentList(getCommentsForDefect(id));
		d.setLogList(getLogForDefect(id));
		
		return d;
	}
	
	public Defect getDefect(int id) {
		return em.find(Defect.class, id);
	}
	
	public List<Defect> getDefectQuery(DefectQuery dq) {
		StringBuffer sql = new StringBuffer();
		
		if (dq.getProjectId() > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			sql.append("projectId=" + dq.getProjectId());
		}
		if (dq.getOwnerId() > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			sql.append("ownerId=" + dq.getOwnerId());
		}
		if (dq.getOriginatorId() > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			sql.append("originatorId=" + dq.getOriginatorId());
		}
		if (dq.getSeverity() > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			sql.append("severity=" + dq.getSeverity());
		}
		if (dq.getPriority() > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			sql.append("priority=" + dq.getPriority());
		}

		if (dq.getState() != null && dq.getState().length > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			sql.append("stateid in (");
			for (int i = 0; i < dq.getState().length; ++i) {
				sql.append("'" + dq.getState()[i] + "'");
				if (i < dq.getState().length - 1) {
					sql.append(", ");
				}
			}
			sql.append(")");
		}
		if (dq.getDefectIdList() != null && dq.getDefectIdList().length() > 0) {
			if (sql.length() > 0) {
				sql.append(" and ");
			}
			String ids[] = dq.getDefectIdList().split(",");
			sql.append("id in (");
			for (int i = 0; i < ids.length; ++i) {
				sql.append(ids[i]);
				if (i < ids.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(")");
		}	
		
		String whereClause = sql.toString();
		logger.fine(whereClause);
		String str = "select * from Defect" + (whereClause.length() > 0 ? (" where " + whereClause) : "");
		Query q = em.createNativeQuery(str, Defect.class);
		
		return (List<Defect>) q.getResultList();
	}
	
	public void createComment(int defectId, String commentText, int userId) {
		DefectComment dc = new DefectComment();
		
		dc.setDefectId(defectId);
		dc.setDescription(commentText);
		dc.setUserId(userId);
		
		em.persist(dc);
	}
	
	public void assignDefect(int id, int ownerId) {
		Defect d = getDefect(id);
		AppUser u = smgr.getUser(ownerId);
		
		if (d.getStateId().equals(STATE_OPEN) == false) {
			throw new IllegalAccessError("Defect must be in open state before assignment");
		}
		
		d.setStateId(STATE_ASSIGNED);
		d.setOwnerId(u.getId());
		
		createDefectLog(d.getId(), "Defect assigned to " + u.getFullName(), session.getCurrentUser().getFullName());
	}
	public void acceptDefect(int id) {
		Defect d = getDefect(id);
		
		if (d.getStateId().equals(STATE_ASSIGNED) == false) {
			throw new IllegalAccessError("Defect must be in assigned to accept");
		}
		if (d.getOwnerId() != session.getCurrentUser().getId()) {
			throw new IllegalAccessError("Defect not assigned to user");
		}
		
		d.setStateId(STATE_ACCEPTED);
		
		createDefectLog(d.getId(), "Defect accepted", session.getCurrentUser().getFullName());
	}
	public void rejectDefect(int id) {
		Defect d = getDefect(id);
		
		if (d.getStateId().equals(STATE_ASSIGNED) == false) {
			throw new IllegalAccessError("Defect must be in assigned to reject");
		}
		if (d.getOwnerId() != session.getCurrentUser().getId()) {
			throw new IllegalAccessError("Defect not assigned to user");
		}
		
		d.setStateId(STATE_REJECTED);
		
		createDefectLog(d.getId(), "Defect rejected", session.getCurrentUser().getFullName());
	}
	public void completeDefect(int id) {
		Defect d = getDefect(id);
		
		if (d.getStateId().equals(STATE_ACCEPTED) == false) {
			throw new IllegalAccessError("Defect must be in accepted to complete");
		}
		if (d.getOwnerId() != session.getCurrentUser().getId()) {
			throw new IllegalAccessError("Defect not assigned to user");
		}
		
		d.setStateId(STATE_COMPLETED);
		d.setOwnerId(d.getOriginatorId());
		
		createDefectLog(d.getId(), "Defect completed", session.getCurrentUser().getFullName());
	}
	public void verifyDefect(int id) {
		Defect d = getDefect(id);
		
		if (d.getStateId().equals(STATE_COMPLETED) == false) {
			throw new IllegalAccessError("Defect must be in open completed to verify");
		}
		if (d.getOriginatorId() != session.getCurrentUser().getId()) {
			throw new IllegalAccessError("Not the originator of the defect");
		}
		
		d.setStateId(STATE_VERIFIED);
		
		createDefectLog(d.getId(), "Defect verified", session.getCurrentUser().getFullName());
	}

	public void updateProject(Project project) {
		Project oldProject = getProject(project.getId());
		
		oldProject.setName(project.getName());
		oldProject.setOwnerId(project.getOwnerId());
	}
}
