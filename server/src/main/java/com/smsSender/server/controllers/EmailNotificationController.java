package com.smsSender.server.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smsSender.server.entities.EmailNotification;
import com.smsSender.server.entities.Journal;
import com.smsSender.server.repositories.ContactRepository;
import com.smsSender.server.scheduling.EmailJob;
import com.smsSender.server.scheduling.ScheduleNotiResponse;
import com.smsSender.server.servicesImpl.EmailNotificationService;
import com.smsSender.server.servicesImpl.JournalService;

@RestController
@RequestMapping("/api/email")
public class EmailNotificationController {

	// logger
	private static final Logger logger = LoggerFactory.getLogger(EmailNotificationController.class);

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Autowired
	private Scheduler scheduler;

	@Autowired
	ContactRepository fileRepo;

	@Autowired
	JournalService journalService;

	// --------------------- get All Notifications -----------------------------
	// @PreAuthorize("hasRole('user')")
	@GetMapping
	public ResponseEntity<List<EmailNotification>> findAll() {
		logger.info("Returning EmailNotification list from database.");

		// SecurityContextHolder.getContext().getAuthentication();
		List<EmailNotification> emailNotifs = emailNotificationService.getAll();
		if (emailNotifs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(emailNotifs, HttpStatus.OK);
	}

	// ----------------------- get single EmailNotification
	// -----------------------------

	@GetMapping(value = "/{id}")
	public ResponseEntity<EmailNotification> getEmailNotification(@PathVariable("id") long id) {
		logger.info("Returning the EmailNotification with the ID = " + id);
		EmailNotification EmailNotification = (EmailNotification) emailNotificationService.getById(id);
		return new ResponseEntity<EmailNotification>(EmailNotification, HttpStatus.OK);
	}
	
	
	@GetMapping(value="/count")
	public long[] count() {
		return emailNotificationService.count();
	}

	// ---------------------- Create a new EmailNotification
	// -----------------------------
	@PostMapping
	public ResponseEntity<EmailNotification> createEmailNotification(@RequestBody EmailNotification emailNotification) {
		logger.info("Creating a new EmailNotification and saving it to database.");
		emailNotification = emailNotificationService.add(emailNotification);

		try {
			String msg = "Creating a new email notification";
			
			saveToJournal(emailNotification, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<EmailNotification>(emailNotification, HttpStatus.CREATED);
	}

	// -------------------- Update a EmailNotification
	// ----------------------------------
	@PutMapping
	public ResponseEntity<EmailNotification> updateEmailNotification(@RequestBody EmailNotification emailNotification) {
		logger.info("Updating a EmailNotification .");
		emailNotification = (EmailNotification) emailNotificationService.update(emailNotification);
		try {
			String msg = "Updating an email notification with ID = " + emailNotification.getId();
			saveToJournal(emailNotification, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<EmailNotification>(emailNotification, HttpStatus.OK);
	}

	// -------------------------Delete a EmailNotification
	// --------------------------------
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<HttpStatus> deleteEmailNotification(@PathVariable long id) {
		logger.info("deleting EmailNotification from database.");
		emailNotificationService.deleteById(id);
		try {
			String msg = "deleting an email notification with ID = " + id;
			saveToJournal(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	// --------------Delete all Notifications -----------------------------
	@DeleteMapping
	public ResponseEntity<HttpStatus> deleteAllEmailNotification() {
		logger.info("deleting all EmailNotifications from database.");
		emailNotificationService.deleteAll();
		try {
			String msg = "deleting All email notifications ";
			saveToJournal(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ---------------------- Send a EmailNotification Now
	// -----------------------------
	@PostMapping("/sendNow")
	public ResponseEntity<EmailNotification> sendEmailNotificationNow(
			@RequestBody EmailNotification emailNotification) {
		logger.info("Creating a new Email EmailNotification Now and saving it to database.");
		emailNotification = (EmailNotification) emailNotificationService.sendNow(emailNotification);
		try {
			String msg = "sending an email notification with ID = " + emailNotification.getId();
			saveToJournal(emailNotification, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<EmailNotification>(emailNotification, HttpStatus.CREATED);
	}

	// ------------------------Scheduling--------------------------------------------

	public ResponseEntity<ScheduleNotiResponse> sendScheduleEmail(
			@Valid @RequestBody EmailNotification scheduleEmailRequest) throws Exception {
		// LocalDate localDateTime = LocalDate.now();

		LocalDateTime localTime = scheduleEmailRequest.getDate_envoi();
		// sending time

		try {
			ZonedDateTime dateTime = ZonedDateTime.of(localTime, ZoneId.of("Africa/Casablanca"));
			if (dateTime.isBefore(ZonedDateTime.now())) {
				ScheduleNotiResponse scheduleEmailResponse = new ScheduleNotiResponse(false,
						"dateTime must be after current time");
				return ResponseEntity.badRequest().body(scheduleEmailResponse);
			}

			JobDetail jobDetail = buildJobDetail(scheduleEmailRequest);

			if (scheduleEmailRequest.getFrequence().toString() == "NON_PERIODIQUE") {
				Trigger trigger = buildJobTrigger(jobDetail, dateTime);
				scheduler.scheduleJob(jobDetail, trigger);
			}

			else if (scheduleEmailRequest.getFrequence().toString() == "PERIODIQUE") {
				Trigger trigger = buildJobTriggerForPeriodique(jobDetail, dateTime);
				scheduler.scheduleJob(jobDetail, trigger);

			}
			ScheduleNotiResponse scheduleEmailResponse = new ScheduleNotiResponse(true, jobDetail.getKey().getName(),
					jobDetail.getKey().getGroup(), "Email Scheduled Successfully!");
			return ResponseEntity.ok(scheduleEmailResponse);

		} catch (SchedulerException ex) {
			logger.error("Error scheduling email", ex);
			scheduleEmailRequest.setStatus("Envoi échoué");

			ScheduleNotiResponse scheduleEmailResponse = new ScheduleNotiResponse(false,
					"Error scheduling email. Please try later!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleEmailResponse);
		}
	}

	private JobDetail buildJobDetail(EmailNotification scheduleEmailRequest) throws Exception {
		JobDataMap jobDataMap = new JobDataMap();

		List<String> recipients = fileRepo.findWantedEmails(scheduleEmailRequest.getDaysNum());

		if (recipients.isEmpty()) {
			String msg = "No recipients found = " + scheduleEmailRequest.getId();
			saveToJournal(scheduleEmailRequest, msg);
			throw new RuntimeException("Aucun destinataire trouvé");

		}
		String[] to = recipients.stream().toArray(String[]::new);

		jobDataMap.put("recipients", to);
		jobDataMap.put("subject", scheduleEmailRequest.getSubject());
		jobDataMap.put("body", scheduleEmailRequest.getMessage());
		jobDataMap.put("daysNum", scheduleEmailRequest.getDaysNum());
		jobDataMap.put("status", scheduleEmailRequest.getStatus());

		
		


		return JobBuilder.newJob(EmailJob.class).withIdentity(UUID.randomUUID().toString(), "email-jobs")
				.withDescription("Send Email Job").usingJobData(jobDataMap).storeDurably().build();
	}

	private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
		logger.info("oneshot process");
		return TriggerBuilder.newTrigger().forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "email-triggers").withDescription("Send Email Trigger")
				.startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
	}

	private Trigger buildJobTriggerForPeriodique(JobDetail jobDetail, ZonedDateTime startAt) {
		logger.info("periodic process");
		return TriggerBuilder.newTrigger().forJob(jobDetail)
				.withIdentity(jobDetail.getKey().getName(), "email-triggers")
				.withDescription("Send Periodic Email Trigger").startAt(Date.from(startAt.toInstant()))
				.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24).withMisfireHandlingInstructionFireNow())
				.build();
	}

	public Journal saveToJournal(EmailNotification emailNotification, String msg) throws Exception {

		// save to journals
		Journal jrn = new Journal();
		jrn.setAction(msg);
		jrn.setDate_log(new Date());

		ResponseEntity<ScheduleNotiResponse> rsp = sendScheduleEmail(emailNotification);
		if (rsp.getStatusCode().is5xxServerError()) {
			jrn.setErrorMessage("Erreur Systeme");
			jrn.setStatus(false);
		} else if (rsp.getStatusCode().is4xxClientError()) {
			jrn.setErrorMessage("Erreur Client");
			jrn.setStatus(false);
		} else if (rsp.getStatusCode().is2xxSuccessful()) {
			jrn.setErrorMessage("None");
			jrn.setStatus(true);
		}
		return journalService.add(jrn);

	}

	public Journal saveToJournal(String msg) throws Exception {

		// save to journals
		Journal jrn = new Journal();
		jrn.setAction(msg);
		jrn.setDate_log(new Date());
		jrn.setErrorMessage("None");
		jrn.setStatus(true);
		return journalService.add(jrn);

	}

}
