package com.mobiarch.dts.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mobiarch.dts.entity.Defect;
import com.mobiarch.dts.entity.Project;
import com.mobiarch.dts.model.DefectManager;
import com.mobiarch.dts.model.SecurityManager;
import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;

@Named("defect")
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
			
			return null;
		} else {
			//Display form
			projectList = dmgr.getAllProject();
			defect.setProjectId(1); //Select a project by default
			
			return "open_defect";
		}
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
}
