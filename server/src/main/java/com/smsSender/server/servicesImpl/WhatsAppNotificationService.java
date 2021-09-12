package com.smsSender.server.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smsSender.server.entities.Contact;
import com.smsSender.server.entities.Destinataire;
import com.smsSender.server.entities.EmailNotification;
import com.smsSender.server.entities.WhatsAppNotification;
import com.smsSender.server.repositories.ContactRepository;
import com.smsSender.server.repositories.EmailNotificationRepository;
import com.smsSender.server.repositories.RecipientsRepository;
import com.smsSender.server.repositories.WhatsAppNotificationRepository;
import com.smsSender.server.services.INotification;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class WhatsAppNotificationService implements INotification<WhatsAppNotification> {

	@Autowired
	WhatsAppNotificationRepository wtspNotiRepo;

	@Autowired
	ContactRepository contactRepo;

	@Autowired
	RecipientsRepository recipientsRepo;

	private static final Logger logger = LoggerFactory.getLogger(WhatsAppNotificationService.class);

	private final String ACCOUNT_SID = "AC65e5eccc6ece8e7390fc28b3cc8c9e46";

	private final String AUTH_TOKEN = "cbae9d2a66874bb45830704b54b05258";

	private final String FROM_NUMBER = "+14092280547" + "";

	@Override
	public WhatsAppNotification add(WhatsAppNotification notif) {
		try {
			List<Contact> wantedNums = contactRepo.getContacts(notif.getDaysNum());
			System.out.println("list length :" + wantedNums.size());
			List<Destinataire> destins = new ArrayList<>();

			/*
			 * EmailNotification emailNotification = new EmailNotification(notif.getId(),
			 * notif.getSubject(), notif.getMessage(), notif.getDaysNum(),
			 * notif.getDate_envoi(), notif.getFrequence(), notif.getImpoMethod());
			 */

			for (Contact con : wantedNums) {
				Destinataire recipient = new Destinataire();
				recipient.setEmail(con.getEmail());
				recipient.setPhoneNumber(con.getPhoneNumber());
				recipient.setIdentifiant(con.getMatricule());
				recipient.setDate_echeance(con.getDate_echeance());
				System.out.println("Destinataire :" + recipient.toString());
				destins.add(recipient);
				notif.getDestinataires().add(recipient);
			}

			notif.setStatus("Programmé");
			wtspNotiRepo.save(notif);
			System.out.println("Destinataire   :" + wtspNotiRepo.findById(notif.getId()).toString());
			// recipientsRepo.save(emailNotification.getDestinataires().get(0));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return notif;
	}

	@Override
	public void deleteById(long id) {
		try {
			wtspNotiRepo.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteAll() {
		try {
			wtspNotiRepo.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public WhatsAppNotification update(WhatsAppNotification notif) {
		WhatsAppNotification updatedNotif = getById(notif.getId());
		updatedNotif.setMessage(notif.getMessage());
		updatedNotif.setSubject(notif.getSubject());
		updatedNotif.setDate_envoi(notif.getDate_envoi());
		updatedNotif.setDaysNum(notif.getDaysNum());
		return updatedNotif;
	}

	@Override
	public List<WhatsAppNotification> getAll() {
		return wtspNotiRepo.findAll();
	}

	@Override
	public WhatsAppNotification getById(long id) {
		return wtspNotiRepo.findById(id).orElseThrow(RuntimeException::new);

	}

	@Override
	public List<WhatsAppNotification> getByStatus(String status) {
		return wtspNotiRepo.findByStatus(status);

	}

	@Override
	public List<WhatsAppNotification> getByFrequence(String freq) {
		return wtspNotiRepo.findByFrequence(freq);
	}

	@Override
	public WhatsAppNotification sendNow(WhatsAppNotification notif) {
		try {
			List<String> recipients = recipientsRepo.getWantedEmails(notif.getId());
			String[] to = recipients.stream().toArray(String[]::new);
			for (String i : to) {
				sendSms(new PhoneNumber(i), FROM_NUMBER, notif.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("fail to send whatsapp: " + e.getMessage());
		}
		return notif;
	}

	private void sendSms(PhoneNumber to, String from, String message) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message message1 = Message.creator(to, new PhoneNumber(FROM_NUMBER), message).create();
		System.out.println("here is my id:" + message1.getSid());
	}

	@Override
	public long[] count() {
		// TODO Auto-generated method stub
		return null;
	}



}
