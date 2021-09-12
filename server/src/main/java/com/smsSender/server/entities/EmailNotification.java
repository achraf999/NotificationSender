package com.smsSender.server.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@Data
@Entity
@ApiModel(value = "EmailNotificationModel", parent = Notification.class)
public class EmailNotification extends Notification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailNotification(long id, String subject, NotificationType type, String message, int daysNum, LocalDateTime localDateTime, Frequence frequence,
			Importation impoMethod) {
		super(id, subject, type, message, daysNum, localDateTime, frequence, impoMethod);
		// TODO Auto-generated constructor stub
	}
	
	public EmailNotification() {
		super();
	}

	@Override
	public String toString() {
		return "EmailNotification [getId()=" + getId() + ", getSubject()=" + getSubject() + ", getMessage()="
				+ getMessage() + ", getDaysNum()=" + getDaysNum() + ", getDate_envoi()=" + getDate_envoi()
				+ ", getStatus()=" + getStatus() + ", getFrequence()=" + getFrequence() + ", getImpoMethod()="
				+ getImpoMethod() + ", getDestinataires()=" + getDestinataires() + "]";
	}
	
	
}
