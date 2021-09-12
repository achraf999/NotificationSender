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

import com.smsSender.server.entities.Journal;
import com.smsSender.server.entities.Notification;
import com.smsSender.server.entities.SmsNotification;
import com.smsSender.server.entities.WhatsAppNotification;
import com.smsSender.server.repositories.ContactRepository;
import com.smsSender.server.scheduling.ScheduleNotiResponse;
import com.smsSender.server.scheduling.SmsJob;
import com.smsSender.server.scheduling.WhatsappJob;
import com.smsSender.server.services.INotification;
import com.smsSender.server.servicesImpl.JournalService;
import com.smsSender.server.servicesImpl.SmsNotificationService;
import com.smsSender.server.servicesImpl.WhatsAppNotificationService;

@RestController
@RequestMapping("/api/sms")
public class SmsNotificationController {
	
		// logger 
		private static final Logger logger = LoggerFactory.getLogger(SmsNotificationController.class);
		
		@Autowired
		private SmsNotificationService smsNotificationService;
		
		@Autowired
		ContactRepository fileRepo;
		
		
		@Autowired
		JournalService journalService;
		
		@Autowired
		private Scheduler scheduler;
		
		
		
		//--------------------- get All Notifications -----------------------------
		//@PreAuthorize("hasRole('user')")
		@GetMapping
		public ResponseEntity<List<SmsNotification>> findAll(){
			logger.info("Returning SmsNotification list from database.");
			
			//SecurityContextHolder.getContext().getAuthentication();
			List<SmsNotification> smsNotifications = smsNotificationService.getAll();
			if (smsNotifications.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(smsNotifications, HttpStatus.OK);
		}
		
		//----------------------- get single Notification -----------------------------
		
		@GetMapping(value="/{id}")
		public ResponseEntity<Notification> getSmsNotification(@PathVariable ("id") long id) {
			logger.info("Returning the SmsNotification with the ID="+id);
			Notification SmsNotification = smsNotificationService.getById(id);
			return new ResponseEntity<Notification>(SmsNotification, HttpStatus.OK);
		}
		
		//---------------------- Create a new Notification -----------------------------
		@PostMapping
		public ResponseEntity<Notification> createSmsNotification(@RequestBody SmsNotification smsNotification ){
			logger.info("Creating a new SmsNotification and saving it to database.");
			 smsNotification = smsNotificationService.add(smsNotification);
			 try {
					String msg = "Creating a new Whatsapp notification";
					saveToJournal(smsNotification, msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 return new ResponseEntity<Notification>(smsNotification, HttpStatus.CREATED);
		}
		
		//-------------------- Update a Notification ----------------------------------
		@PutMapping
		public ResponseEntity<Notification> updateSmsNotification(@RequestBody SmsNotification smsNotification ){
			logger.info("Updating a SmsNotification .");
			smsNotification = smsNotificationService.update(smsNotification);

			try {
				String msg = "Updating a new Whatsapp notification";
				saveToJournal(smsNotification, msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<Notification>(smsNotification, HttpStatus.OK);
		}
		
		//-------------------------Delete a Notification --------------------------------
		@DeleteMapping(value="/{id}")
		public ResponseEntity<HttpStatus> deleteSmsNotification(@PathVariable long id) {
			logger.info("deleting SmsNotification from database.");
			smsNotificationService.deleteById(id);
			try {
				String msg = "deleting an SMS notification with ID = " + id;
				saveToJournal(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		//--------------Delete all Notifications -----------------------------
		@DeleteMapping
		public ResponseEntity<HttpStatus> deleteAllSmsNotification(){
			logger.info("deleting all SmsNotifications from database.");
			smsNotificationService.deleteAll();
			try {
				String msg = "deleting all sms notification";
				saveToJournal(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		//---------------------- Send a Notification Now -----------------------------
		@PostMapping("/sendNow")
		public ResponseEntity<SmsNotification> sendSmsNotificationNow(@RequestBody SmsNotification smsNotification ){
			logger.info("Creating a new SMS Notification Now and saving it to database.");
			smsNotification = smsNotificationService.sendNow(smsNotification);
			try {
				String msg = "sending an SMS notification with ID = " + smsNotification.getId();
				saveToJournal(smsNotification, msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return new ResponseEntity<SmsNotification>(smsNotification, HttpStatus.CREATED);
		}
		
		//-------------------------JOURNAL-------------------------//
				public Journal saveToJournal(SmsNotification smsNotification, String msg) throws Exception {

					// save to journals
					Journal jrn = new Journal();
					jrn.setAction(msg);
					jrn.setDate_log(new Date());

					ResponseEntity<ScheduleNotiResponse> rsp = sendScheduleSMS(smsNotification);
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
				
				public ResponseEntity<ScheduleNotiResponse> sendScheduleSMS(
						@Valid @RequestBody SmsNotification scheduleSmsRequest) throws Exception {
					// LocalDate localDateTime = LocalDate.now();

					LocalDateTime localTime = scheduleSmsRequest.getDate_envoi();
					// sending time

					try {
						ZonedDateTime dateTime = ZonedDateTime.of(localTime, ZoneId.of("Africa/Casablanca"));
						if (dateTime.isBefore(ZonedDateTime.now())) {
							ScheduleNotiResponse scheduleWtspResponse = new ScheduleNotiResponse(false,
									"dateTime must be after current time");
							return ResponseEntity.badRequest().body(scheduleWtspResponse);
						}

						JobDetail jobDetail = buildJobDetail(scheduleSmsRequest);

						if (scheduleSmsRequest.getFrequence().toString() == "NON_PERIODIQUE") {
							Trigger trigger = buildJobTrigger(jobDetail, dateTime);
							scheduler.scheduleJob(jobDetail, trigger);
							scheduleSmsRequest.setStatus("Envoyé");
						}

						else if (scheduleSmsRequest.getFrequence().toString() == "PERIODIQUE") {
							Trigger trigger = buildJobTriggerForPeriodique(jobDetail, dateTime);
							scheduler.scheduleJob(jobDetail, trigger);
							scheduleSmsRequest.setStatus("Envoyé");

						}
						ScheduleNotiResponse scheduleWtspResponse = new ScheduleNotiResponse(true, jobDetail.getKey().getName(),
								jobDetail.getKey().getGroup(), "SMS Notification Scheduled Successfully!");
						return ResponseEntity.ok(scheduleWtspResponse);

					} catch (SchedulerException ex) {
						logger.error("Error scheduling SMS notification", ex);
						scheduleSmsRequest.setStatus("Envoi échoué");
						ScheduleNotiResponse scheduleWtspResponse = new ScheduleNotiResponse(false,
								"Error scheduling SMS notification. Please try later!");
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleWtspResponse);
					}
				}

				private JobDetail buildJobDetail(SmsNotification scheduleSmsRequest) {
					JobDataMap jobDataMap = new JobDataMap();

					List<String> nums = fileRepo.findWantedNums(scheduleSmsRequest.getDaysNum());

					if (nums.isEmpty()) {
						throw new RuntimeException("Aucun destinataire trouvé");
					}
					jobDataMap.put("body", scheduleSmsRequest.getMessage());
					jobDataMap.put("numDays", scheduleSmsRequest.getDaysNum());

					return JobBuilder.newJob(SmsJob.class).withIdentity(UUID.randomUUID().toString(), "sms-jobs")
							.withDescription("Send SMS Job").usingJobData(jobDataMap).storeDurably().build();
				}

				private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {

					return TriggerBuilder.newTrigger().forJob(jobDetail)
							.withIdentity(jobDetail.getKey().getName(), "sms-triggers").withDescription("Send SMS Trigger")
							.startAt(Date.from(startAt.toInstant()))
							.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
				}

				private Trigger buildJobTriggerForPeriodique(JobDetail jobDetail, ZonedDateTime startAt) {

					return TriggerBuilder.newTrigger().forJob(jobDetail)
							.withIdentity(jobDetail.getKey().getName(), "sms-triggers")
							.withDescription("Send Periodic SMS Trigger").startAt(Date.from(startAt.toInstant()))
							.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24).withMisfireHandlingInstructionFireNow())
							.build();
				}
				
		
}
