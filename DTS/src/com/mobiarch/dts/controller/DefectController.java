package com.mobiarch.dts.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.dts.entity.AppUser;
import com.mobiarch.dts.entity.Defect;
import com.mobiarch.dts.entity.Project;
import com.mobiarch.dts.model.DefectManager;
import com.mobiarch.dts.model.DefectQuery;
import com.mobiarch.dts.model.SecurityManager;
import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;
import com.mobiarch.nf.Path;

@Named("defects")
@RequestScoped
public class DefectController extends Controller {
	@EJB
	SecurityManager smgr;
	@EJB
	DefectManager dmgr;
	@Inject
	Context context;
	@Inject
	SessionData session;

	//Properties
	private List<Project> projectList;
	private Defect defect = new Defect();
	private String commentText;
	private List<AppUser> userList;
	private DefectQuery query = new DefectQuery();
	private List<Defect> defectList;
	
	public String index() {
		if (noSession()) {
			return "@/login";
		}
		
		return "home";
	}
	public String open() {
		if (noSession()) {
			return "@/login";
		}
		
		if (context.isPostBack() && !context.isValidationFailed()) {
			defect.setOriginatorId(session.getCurrentUser().getId());
			defect.setOwnerId(session.getCurrentUser().getId());
			
			dmgr.createDefect(defect);
			
			return "show/" + defect.getId();
		} else {
			//Display form
			projectList = dmgr.getAllProject();
			defect.setProjectId(1); //Select a project by default
			
			return "open_defect";
		}
	}
	
	@Path("defect.id")
	public String show() {
		if (noSession()) {
			return "@/login";
		}
		
		defect = dmgr.getDefectPopulated(defect.getId());
		
		if (defect == null) {
			return "defect_not_found";
		}
		
		return "view_defect";
	}
	
	public String query() {
		userList = smgr.getAllUser();
		projectList = dmgr.getAllProject();

		return "query_defect";
	}
	@Path("/do-query")
	public String doQuery() {
		defectList = dmgr.getDefectQuery(query);
		if (defectList.size() == 0) {
			return "defect_not_found";
		}
		return "query_result";
	}
	
	@Path("/add-comment/defect.id")
	public String addComment() {
		dmgr.createComment(defect.getId(), commentText, session.getCurrentUser().getId());
		
		return "show/" + defect.getId();
	}
	@Path("defect.id")
	public String edit() {
		if (noSession()) {
			return "@/login";
		}
		
		if (context.isPostBack()) {
			dmgr.updateDefect(defect);
			return "show/" + defect.getId();
		} else {
			projectList = dmgr.getAllProject();
			defect = dmgr.getDefect(defect.getId());
			
			return "edit_defect";
		}
	}
	
	@Path("defect.id")
	public String assign() {
		if (noSession()) {
			return "@/login";
		}
		
		if (context.isPostBack()) {
			dmgr.assignDefect(defect.getId(), defect.getOwnerId());
			return "show/" + defect.getId();
		} else {
			userList = smgr.getAllUser();
			
			return "assign_form";
		}
	}
	
	@Path("defect.id")
	public String accept() {
		if (noSession()) {
			return "@/login";
		}
		
		dmgr.acceptDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	@Path("defect.id")
	public String reject() {
		if (noSession()) {
			return "@/login";
		}
		
		dmgr.rejectDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	@Path("defect.id")
	public String complete() {
		if (noSession()) {
			return "@/login";
		}
		
		dmgr.completeDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	@Path("defect.id")
	public String verify() {
		if (noSession()) {
			return "@/login";
		}
		
		dmgr.verifyDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	private boolean noSession() {
		return session == null || session.getCurrentUser() == null;
	}
	public List<Project> getProjectList() {
		return projectList;
	}
	public Defect getDefect() {
		return defect;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public List<AppUser> getUserList() {
		return userList;
	}
	public DefectQuery getQuery() {
		return query;
	}
	public List<Defect> getDefectList() {
		return defectList;
	}
}
