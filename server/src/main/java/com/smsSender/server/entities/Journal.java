package com.smsSender.server.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="journal")
public class Journal implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
			
	private long id;
	private String action;
	private boolean status;
	private Date date_log;
	private String errorMessage;
		
	public Journal() {
		super();
	}

	public Journal(long id, String action, boolean status, Date date_log, String errorMessage) {
		super();
		this.id = id;
		this.action = action;
		this.status = status;
		this.date_log = date_log;
		this.errorMessage = errorMessage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDate_log() {
		return date_log;
	}

	public void setDate_log(Date date_log) {
		this.date_log = date_log;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Journal [id=" + id + ", action=" + action + ", statut=" + status + ", date_log=" + date_log
				+ ", errorMessage=" + errorMessage + "]";
	}

	
}
