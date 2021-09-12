package com.smsSender.server.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
@Entity
@ApiModel(value = "SmsNotificationModel", parent = Notification.class)
public class SmsNotification extends Notification {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	public SmsNotification() {
		super();
		// TODO Auto-generated constructor stub
	}



	public SmsNotification(long id, String subject, NotificationType type, String message, int daysNum, LocalDateTime date_envoi,
			Frequence frequence, Importation impoMethod) {
		super(id, subject, type, message, daysNum, date_envoi, frequence, impoMethod);
		// TODO Auto-generated constructor stub
	}

}
