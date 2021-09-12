package com.smsSender.server.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="destinataires")
@NoArgsConstructor
@Getter
@Setter
public class Destinataire implements Serializable {

	private static final long serialVersionUID = 9191985209103471047L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private long id;
	private String email;
	private String phoneNumber;
	private Date date_echeance;
	private String identifiant;
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
	public String getIdentifiant() {
		return identifiant;
	}
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	@Override
	public String toString() {
		return "Destinataire [id=" + id + ", email=" + email + ", phoneNumber=" + phoneNumber + ", date_echeance="
				+ date_echeance + ", identifiant=" + identifiant + "]";
	}
	
	
		

}
