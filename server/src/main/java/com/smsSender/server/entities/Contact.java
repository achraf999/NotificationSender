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

@Entity(name="contacts")
@NoArgsConstructor
@Getter
@Setter
public class Contact implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Contact(long id, String email, String phoneNumber, String identifiant, Date date_echeance) {
		this.id= id ;
		this.email = email ;
		this.phoneNumber = phoneNumber ;
		this.date_echeance = date_echeance ;
		this.matricule = identifiant ;
		// TODO Auto-generated constructor stub
	}
	
	
	public Contact() {
		super();
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;
	private String email;
	private String phoneNumber;
	private Date date_echeance;
	private String matricule;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Date getDate_echeance() {
		return date_echeance;
	}
	public void setDate_echeance(Date date_echeance) {
		this.date_echeance = date_echeance;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	
	
}
