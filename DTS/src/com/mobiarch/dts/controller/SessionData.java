package com.mobiarch.dts.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.mobiarch.dts.entity.AppUser;

@Named
@SessionScoped
public class SessionData implements Serializable {
	private static final long serialVersionUID = 6779637574198445668L;
	
	private AppUser currentUser;

	public AppUser getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(AppUser currentUser) {
		this.currentUser = currentUser;
	}
}
