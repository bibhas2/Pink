package com.mobiarch.dts.controller;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.mobiarch.dts.entity.AppUser;
import com.mobiarch.dts.model.SecurityManager;
import com.mobiarch.nf.Context;
import com.mobiarch.nf.Controller;
import com.mobiarch.nf.PropertyViolation;

@Named("login")
@RequestScoped
public class LoginController extends Controller {
	@Size(min=2, max=128, message="Please enter a valid e-mail address<br/>")
	@Pattern(regexp="^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message="Please enter a valid e-mail address<br/>")
	String email;
	@Size(min=2, max=128, message="Please enter a valid password<br/>")
	String password;
	
	@EJB
	SecurityManager smgr;
	@Inject
	Context context;
	
	@Inject
	SessionData session;
	
	public String index() {
		if (context.isPostBack() && !context.isValidationFailed()) {
			AppUser user = smgr.authenticateUser(email, password);
			if (user == null) {
				context.addViolation(new PropertyViolation("Login failed. Please enter a valid e-mail and password.<br/>", "login"));
				
				return "login_form";
			}
			
			session.setCurrentUser(user);
			
			return "/defect";
		} else {
			return "login_form";
		}
	}

	public String signout() {
		
		context.getRequest().getSession().invalidate();
		
		return "@"; //Go to login screen
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
