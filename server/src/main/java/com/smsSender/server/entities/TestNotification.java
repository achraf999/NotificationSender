package com.smsSender.server.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToMany;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@Entity
@ApiModel(value = "TestNotificationModel")
public class TestNotification  implements Serializable {
	
	// add manuelly
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String subject;
	
	private String message;
	
	private Date date_envoi;
	
	private String status;
	
	@Enumerated(EnumType.STRING)
	private Frequence frequence;
	
	@Enumerated(EnumType.STRING)
	private Importation impoMethod;
	
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="ro_fid", referencedColumnName = "id")
	private List<Destinataire> destinataires = new ArrayList<>();
}
