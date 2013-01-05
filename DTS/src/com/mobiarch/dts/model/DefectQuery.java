package com.mobiarch.dts.model;

public class DefectQuery {
	String defectIdList;
	int projectId;
	int ownerId;
	int originatorId;
	String state[];
	int severity;
	int priority;
	
	public DefectQuery() {
		//A value zero or less means exclude from query.
		projectId = ownerId = originatorId = severity = priority = 0;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getOriginatorId() {
		return originatorId;
	}

	public void setOriginatorId(int originatorId) {
		this.originatorId = originatorId;
	}

	public String[] getState() {
		return state;
	}

	public void setState(String[] state) {
		this.state = state;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDefectIdList() {
		return defectIdList;
	}

	public void setDefectIdList(String defectIdList) {
		this.defectIdList = defectIdList;
	}
}
