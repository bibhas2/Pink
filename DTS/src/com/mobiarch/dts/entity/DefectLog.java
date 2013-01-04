package com.mobiarch.dts.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the DefectLog database table.
 * 
 */
@Entity
public class DefectLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String actionText;

	private String actorName;

	private int defectId;

	private Timestamp lastupdate;

	public DefectLog() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActionText() {
		return this.actionText;
	}

	public void setActionText(String actionText) {
		this.actionText = actionText;
	}

	public String getActorName() {
		return this.actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public int getDefectId() {
		return this.defectId;
	}

	public void setDefectId(int defectId) {
		this.defectId = defectId;
	}

	public Timestamp getLastupdate() {
		return this.lastupdate;
	}

	public void setLastupdate(Timestamp lastupdate) {
		this.lastupdate = lastupdate;
	}

}