package com.smsSender.server.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import io.swagger.annotations.ApiModel;
import lombok.Data;



@Data
@ApiModel(value = "NotificationModel", discriminator = "type", subTypes = { 
EmailNotification.class,SmsNotification.class, WhatsAppNotification.class })
@MappedSuperclass
public abstract class Notification implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6186564766667495533L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String subject;
	
	private String message;
	
	private int daysNum;
	
	private LocalDateTime date_envoi;
	
	private String status = "Programmé";
	
	@Enumerated(EnumType.STRING)
	private NotificationType type;
	
	@Enumerated(EnumType.STRING)
	private Frequence frequence;
	
	@Enumerated(EnumType.STRING)
	private Importation impoMethod;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	//@JoinColumn(name="ro_fid", referencedColumnName = "id")
	@JoinColumn(name="ro_fid")
	private List<Destinataire> destinataires = new ArrayList<>();

	public Notification(long id, String subject, NotificationType type, String message, int daysNum, LocalDateTime date_envoi,
			Frequence frequence, Importation impoMethod) {
		super();
		this.id = id;
		this.type = type;
		this.subject = subject;
		this.message = message;
		this.daysNum = daysNum;
		this.date_envoi = date_envoi;
		this.frequence = frequence;
		this.impoMethod = impoMethod;
	}

	public Notification() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getDaysNum() {
		return daysNum;
	}

	public void setDaysNum(int daysNum) {
		this.daysNum = daysNum;
	}

	public LocalDateTime getDate_envoi() {
		return date_envoi;
	}

	public void setDate_envoi(LocalDateTime date_envoi) {
		this.date_envoi = date_envoi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Frequence getFrequence() {
		return frequence;
	}

	public void setFrequence(Frequence frequence) {
		this.frequence = frequence;
	}

	public Importation getImpoMethod() {
		return impoMethod;
	}

	public void setImpoMethod(Importation impoMethod) {
		this.impoMethod = impoMethod;
	}

	public List<Destinataire> getDestinataires() {
		return destinataires;
	}

	public void setDestinataires(List<Destinataire> destinataires) {
		this.destinataires = destinataires;
	}
	
	

}
