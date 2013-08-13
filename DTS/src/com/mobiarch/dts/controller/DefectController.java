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
public class DefectController {
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
	private Project project = new Project();
	
	@SessionCheck
	public String index() {
		return "home";
	}
	@SessionCheck
	public String open() {
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
	
	@SessionCheck
	@Path("defect.id")
	public String show() {
		defect = dmgr.getDefectPopulated(defect.getId());
		
		if (defect == null) {
			return "defect_not_found";
		}
		
		return "view_defect";
	}
	
	@SessionCheck
	@Path("/query-form")
	public String queryForm() {
		userList = smgr.getAllUser();
		projectList = dmgr.getAllProject();

		return "query_defect";
	}
	@SessionCheck
	public String query() {
		defectList = dmgr.getDefectQuery(query);
		if (defectList.size() == 0) {
			return "defect_not_found";
		}
		return "query_result";
	}
	
	@SessionCheck
	@Path("/add-comment/defect.id")
	public String addComment() {
		dmgr.createComment(defect.getId(), commentText, session.getCurrentUser().getId());
		
		return "show/" + defect.getId();
	}

	@SessionCheck
	@Path("defect.id")
	public String edit() {
		if (context.isPostBack()) {
			dmgr.updateDefect(defect);
			return "show/" + defect.getId();
		} else {
			projectList = dmgr.getAllProject();
			defect = dmgr.getDefect(defect.getId());
			
			return "edit_defect";
		}
	}
	
	@SessionCheck
	@Path("defect.id")
	public String assign() {
		if (context.isPostBack()) {
			dmgr.assignDefect(defect.getId(), defect.getOwnerId());
			return "show/" + defect.getId();
		} else {
			userList = smgr.getAllUser();
			
			return "assign_form";
		}
	}
	
	@SessionCheck
	@Path("defect.id")
	public String accept() {
		dmgr.acceptDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	@SessionCheck
	@Path("defect.id")
	public String reject() {
		dmgr.rejectDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	@SessionCheck
	@Path("defect.id")
	public String complete() {
		dmgr.completeDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	
	@SessionCheck
	@Path("defect.id")
	public String verify() {
		dmgr.verifyDefect(defect.getId());
		
		return "@show/" + defect.getId();
	}
	@SessionCheck
	@Path("/project-list")
	public String projectList() {
		projectList = dmgr.getAllProjectPopulated();
		
		return "project_list";
	}
	@SessionCheck
	@Path("/edit-project/project.id")
	public String editProject() {
		if (context.isPostBack() && !context.isValidationFailed()) {
			dmgr.updateProject(project);
			return "project-list";
		} else {
			userList = smgr.getAllUser();
			project = dmgr.getProject(project.getId());
			
			return "edit_project";
		}
	}
	@SessionCheck
	@Path("/create-project")
	public String createProject() {
		if (context.isPostBack() && !context.isValidationFailed()) {
			dmgr.createProject(project);
			return "project-list";
		} else {
			userList = smgr.getAllUser();
			project.setOwnerId(1); //Default owner
			
			return "create_project";
		}
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
	public Project getProject() {
		return project;
	}
}
