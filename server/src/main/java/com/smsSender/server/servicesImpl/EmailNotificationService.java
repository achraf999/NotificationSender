package com.smsSender.server.servicesImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.smsSender.server.entities.Contact;
import com.smsSender.server.entities.Destinataire;
import com.smsSender.server.entities.EmailNotification;
import com.smsSender.server.repositories.ContactRepository;
import com.smsSender.server.repositories.EmailNotificationRepository;
import com.smsSender.server.repositories.RecipientsRepository;
import com.smsSender.server.repositories.SmsNotificationRepository;
import com.smsSender.server.repositories.WhatsAppNotificationRepository;
import com.smsSender.server.services.INotification;


@Service
public class EmailNotificationService implements INotification<EmailNotification> {
	@Autowired
	ContactRepository contactRepo;

	@Autowired
	EmailNotificationRepository emailNotiRepo;
	
	@Autowired
	WhatsAppNotificationRepository wtspNotiRepo;
	
	@Autowired
	SmsNotificationRepository smsNotiRepo;

	@Autowired
	JavaMailSender sender;

	@Autowired
	RecipientsRepository recipientsRepo;
	
	
	// logger 
			private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

	@Override
	public EmailNotification add(EmailNotification notif) {
		try {
			List<Contact> wantedEmails = contactRepo.getContacts(notif.getDaysNum());
			System.out.println("list length :"+ wantedEmails.size());
			List<Destinataire> destins = new ArrayList<>();
			
			
			/*
			 * EmailNotification emailNotification = new EmailNotification(notif.getId(),
			 * notif.getSubject(), notif.getMessage(), notif.getDaysNum(),
			 * notif.getDate_envoi(), notif.getFrequence(), notif.getImpoMethod());
			 */
			
			
			for (Contact con : wantedEmails) {
				Destinataire recipient = new Destinataire();
				recipient.setEmail(con.getEmail());
				recipient.setPhoneNumber(con.getPhoneNumber());
				recipient.setIdentifiant(con.getMatricule());
				recipient.setDate_echeance(con.getDate_echeance());
				System.out.println("Destinataire :"+ recipient.toString());
				destins.add(recipient);
				notif.getDestinataires().add(recipient);
				}
			notif.setStatus("Programmé");
			
			emailNotiRepo.save(notif);
			System.out.println("Destinataire   :"+ emailNotiRepo.findById(notif.getId()).toString());
			//recipientsRepo.save(emailNotification.getDestinataires().get(0));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return notif;
	}

	@Override
	public void deleteById(long id) {
		try {
			emailNotiRepo.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteAll() {
		try {
			emailNotiRepo.deleteAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<EmailNotification> getAll() {
		return emailNotiRepo.findAll();
	}

	@Override
	public EmailNotification getById(long id) {
		return emailNotiRepo.findById(id).orElseThrow(RuntimeException::new);

	}

	@Override
	public List<EmailNotification> getByStatus(String status) {
		return emailNotiRepo.findByStatus(status);
	}

	@Override
	public List<EmailNotification> getByFrequence(String freq) {
		return emailNotiRepo.findByFrequence(freq);
	}

	@Override
	public EmailNotification sendNow(EmailNotification notif) {
		try {
			JavaMailSenderImpl jms = (JavaMailSenderImpl) sender;
			MimeMessage message = jms.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			List<String> recipients = recipientsRepo.getWantedEmails(notif.getId());
			String[] to = recipients.stream().toArray(String[]::new);

			helper.setFrom("smsender4@gmail.com");
			helper.setTo(to);
			helper.setText(notif.getMessage(), true);
			helper.setSubject("Test Mail");
			sender.send(message);
			System.out.print("Message envoyé avec suucces aux: " + Arrays.toString(to));
		} catch (MessagingException e) {
			throw new RuntimeException("fail to send emails: " + e.getMessage());
		}
		return notif;
	}

	@Override
	public EmailNotification update(EmailNotification notif) {
		EmailNotification updatedNotif = getById(notif.getId());
		updatedNotif.setMessage(notif.getMessage());
		updatedNotif.setSubject(notif.getSubject());
		updatedNotif.setDate_envoi(notif.getDate_envoi());
		updatedNotif.setDaysNum(notif.getDaysNum());
		return updatedNotif;
	}

	@Override
	public long[] count() {
		long[] num = { emailNotiRepo.count(), wtspNotiRepo.count(), smsNotiRepo.count() };
		return num;
	}

}
