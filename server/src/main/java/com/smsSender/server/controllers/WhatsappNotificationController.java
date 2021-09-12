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
import com.smsSender.server.entities.WhatsAppNotification;
import com.smsSender.server.repositories.ContactRepository;
import com.smsSender.server.scheduling.ScheduleNotiResponse;
import com.smsSender.server.scheduling.WhatsappJob;
import com.smsSender.server.servicesImpl.JournalService;
import com.smsSender.server.servicesImpl.WhatsAppNotificationService;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappNotificationController {
	
		// logger 
		private static final Logger logger = LoggerFactory.getLogger(WhatsappNotificationController.class);
		
		@Autowired
		private WhatsAppNotificationService whatsappNotificationService;
		
		@Autowired
		ContactRepository fileRepo;
		
		@Autowired
		JournalService journalService;
		
		@Autowired
		private Scheduler scheduler;
		
		
		
		//--------------------- get All Notifications -----------------------------
		//@PreAuthorize("hasRole('user')")
		@GetMapping
		public ResponseEntity<List<WhatsAppNotification>> findAll(){
			logger.info("Returning WhatsappNotification list from database.");
			
			//SecurityContextHolder.getContext().getAuthentication();
			List<WhatsAppNotification> whatsappNotifications = whatsappNotificationService.getAll();
			return new ResponseEntity<>(whatsappNotifications, HttpStatus.OK);
		}
		
		//----------------------- get single Notification -----------------------------
		
		@GetMapping(value="/{id}")
		public ResponseEntity<WhatsAppNotification> getWhatsappNotification(@PathVariable ("id") long id) {
			logger.info("Returning the WhatsappNotification with the ID="+id);
			WhatsAppNotification whatsappNotification = whatsappNotificationService.getById(id);
			return new ResponseEntity<WhatsAppNotification>(whatsappNotification, HttpStatus.OK);
		}
		
		//---------------------- Create a new Notification -----------------------------
		@PostMapping
		public ResponseEntity<WhatsAppNotification> createWhatsappNotification(@RequestBody WhatsAppNotification whatsappNotification ) {
			logger.info("Creating a new WhatsappNotification and saving it to database.");
			whatsappNotification = whatsappNotificationService.add(whatsappNotification);

			try {
				String msg = "Creating a new Whatsapp notification";
				saveToJournal(whatsappNotification, msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return new ResponseEntity<WhatsAppNotification>(whatsappNotification, HttpStatus.CREATED);
		}
		
		//-------------------- Update a Notification ----------------------------------
		@PutMapping
		public ResponseEntity<WhatsAppNotification> updateWhatsappNotification(@RequestBody WhatsAppNotification whatsappNotification ){
			logger.info("Updating a Whatsapp Notification .");
			whatsappNotification = (WhatsAppNotification) whatsappNotificationService.update(whatsappNotification);
			try {
				String msg = "Updating a Whatsapp notification with ID = " + whatsappNotification.getId();
				saveToJournal(whatsappNotification, msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<WhatsAppNotification>(whatsappNotification, HttpStatus.OK);
		}
		
		//-------------------------Delete a Notification --------------------------------
		@DeleteMapping(value="/{id}")
		public ResponseEntity<HttpStatus> deleteWhatsappNotification(@PathVariable long id) {
			logger.info("deleting WhatsappNotification from database.");
			whatsappNotificationService.deleteById(id);
			try {
				String msg = "deleting a Whatsapp notification with ID = " + id;
				saveToJournal(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
		//--------------Delete all Notifications -----------------------------
		@DeleteMapping
		public ResponseEntity<HttpStatus> deleteAllWhatsappNotification(){
			logger.info("deleting all WhatsappNotifications from database.");
			whatsappNotificationService.deleteAll();
			try {
				String msg = "deleting All Whatsapp notifications ";
				saveToJournal(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		//---------------------- Send a Notification Now -----------------------------
		@PostMapping("/sendNow")
		public ResponseEntity<WhatsAppNotification> sendWhatsappNotificationNow(@RequestBody WhatsAppNotification whatsappNotification ){
			logger.info("Creating a new WhatsappNotification and saving it to database.");
			whatsappNotification = (WhatsAppNotification) whatsappNotificationService.sendNow(whatsappNotification);
			try {
				String msg = "sending a Whatsapp notification with ID = " + whatsappNotification.getId();
				saveToJournal(whatsappNotification, msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new ResponseEntity<WhatsAppNotification>(whatsappNotification, HttpStatus.CREATED);
		}
		
		
		//-------------------------JOURNAL-------------------------//
		public Journal saveToJournal(WhatsAppNotification whatsappNotification, String msg) throws Exception {

			// save to journals
			Journal jrn = new Journal();
			jrn.setAction(msg);
			jrn.setDate_log(new Date());

			ResponseEntity<ScheduleNotiResponse> rsp = sendScheduleWhatsapp(whatsappNotification);
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
		
		
		//SCHEDULing
		
		public ResponseEntity<ScheduleNotiResponse> sendScheduleWhatsapp(
				@Valid @RequestBody WhatsAppNotification scheduleWhatsappRequest) throws Exception {
			// LocalDate localDateTime = LocalDate.now();

			LocalDateTime localTime = scheduleWhatsappRequest.getDate_envoi();
			// sending time

			try {
				ZonedDateTime dateTime = ZonedDateTime.of(localTime, ZoneId.of("Africa/Casablanca"));
				if (dateTime.isBefore(ZonedDateTime.now())) {
					ScheduleNotiResponse scheduleWtspResponse = new ScheduleNotiResponse(false,
							"dateTime must be after current time");
					return ResponseEntity.badRequest().body(scheduleWtspResponse);
				}

				JobDetail jobDetail = buildJobDetail(scheduleWhatsappRequest);

				if (scheduleWhatsappRequest.getFrequence().toString() == "NON_PERIODIQUE") {
					Trigger trigger = buildJobTrigger(jobDetail, dateTime);
					scheduler.scheduleJob(jobDetail, trigger);
					scheduleWhatsappRequest.setStatus("Envoyé");
				}

				else if (scheduleWhatsappRequest.getFrequence().toString() == "PERIODIQUE") {
					Trigger trigger = buildJobTriggerForPeriodique(jobDetail, dateTime);
					scheduler.scheduleJob(jobDetail, trigger);
					scheduleWhatsappRequest.setStatus("Envoyé");

				}
				ScheduleNotiResponse scheduleWtspResponse = new ScheduleNotiResponse(true, jobDetail.getKey().getName(),
						jobDetail.getKey().getGroup(), "Whatsapp Notification Scheduled Successfully!");
				return ResponseEntity.ok(scheduleWtspResponse);

			} catch (SchedulerException ex) {
				logger.error("Error scheduling Whatsapp notification", ex);
				scheduleWhatsappRequest.setStatus("Envoi échoué");

				ScheduleNotiResponse scheduleWtspResponse = new ScheduleNotiResponse(false,
						"Error scheduling Whatsapp notification. Please try later!");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scheduleWtspResponse);
			}
		}

		private JobDetail buildJobDetail(WhatsAppNotification scheduleWhatsappRequest) {
			JobDataMap jobDataMap = new JobDataMap();

			List<String> nums = fileRepo.findWantedNums(scheduleWhatsappRequest.getDaysNum());

			if (nums.isEmpty()) {
				throw new RuntimeException("Aucun destinataire trouvé");
			}
			jobDataMap.put("body", scheduleWhatsappRequest.getMessage());
			jobDataMap.put("numDays", scheduleWhatsappRequest.getDaysNum());

			return JobBuilder.newJob(WhatsappJob.class).withIdentity(UUID.randomUUID().toString(), "sms-jobs")
					.withDescription("Send SMS Job").usingJobData(jobDataMap).storeDurably().build();
		}

		private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {

			return TriggerBuilder.newTrigger().forJob(jobDetail)
					.withIdentity(jobDetail.getKey().getName(), "whatsapp-triggers").withDescription("Send whatsapp Trigger")
					.startAt(Date.from(startAt.toInstant()))
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()).build();
		}

		private Trigger buildJobTriggerForPeriodique(JobDetail jobDetail, ZonedDateTime startAt) {

			return TriggerBuilder.newTrigger().forJob(jobDetail)
					.withIdentity(jobDetail.getKey().getName(), "whatsapp-triggers")
					.withDescription("Send Periodic Whatsapp Trigger").startAt(Date.from(startAt.toInstant()))
					.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(24).withMisfireHandlingInstructionFireNow())
					.build();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
