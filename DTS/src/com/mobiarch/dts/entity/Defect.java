package com.mobiarch.dts.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


/**
 * The persistent class for the Defect database table.
 * 
 */
@Entity
public class Defect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Size(min=5, max=1024, message="Please enter a valid long description. Mimimum 5 characters.")
	private String longDescription;

	private int originatorId;

	private int ownerId;

	private int priority;

	private int projectId;

	private int severity;

	@Size(min=5, max=1024, message="Please enter a valid short description. Mimimum 5 characters.")
	private String shortDescription;

	private String stateId;

	@Transient
	private AppUser owner;
	@Transient
	private AppUser originator;
	@Transient
	private Project project;
	@Transient
	List<DefectComment> commentList;
	@Transient
	List<DefectLog> logList;
	
	public Defect() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLongDescription() {
		return this.longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public int getOriginatorId() {
		return this.originatorId;
	}

	public void setOriginatorId(int originatorId) {
		this.originatorId = originatorId;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getProjectId() {
		return this.projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getSeverity() {
		return this.severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getShortDescription() {
		return this.shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getStateId() {
		return this.stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

	public AppUser getOriginator() {
		return originator;
	}

	public void setOriginator(AppUser originator) {
		this.originator = originator;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<DefectComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<DefectComment> commentList) {
		this.commentList = commentList;
	}

	public List<DefectLog> getLogList() {
		return logList;
	}

	public void setLogList(List<DefectLog> logList) {
		this.logList = logList;
	}
}