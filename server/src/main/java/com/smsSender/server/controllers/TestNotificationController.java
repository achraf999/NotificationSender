package com.smsSender.server.controllers;


import java.util.List;

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
import com.smsSender.server.entities.Notification;
import com.smsSender.server.entities.TestNotification;
import com.smsSender.server.servicesImpl.TestNotificationService;

@RestController
@RequestMapping("/api/test")
public class TestNotificationController {
	
		// logger 
		private static final Logger logger = LoggerFactory.getLogger(TestNotificationController.class);
		
		@Autowired
		private TestNotificationService emailNotificationService;
		
		
		
		//--------------------- get All Notifications -----------------------------
		//@PreAuthorize("hasRole('user')")
		@GetMapping
		public ResponseEntity<List<TestNotification>> findAll(){
			logger.info("Returning EmailNotification list from database.");
			
			//SecurityContextHolder.getContext().getAuthentication();
			List<TestNotification> EmailNotifications =  emailNotificationService.GetAll();
			return new ResponseEntity<>(EmailNotifications, HttpStatus.OK);
		}
		
		//----------------------- get single EmailNotification -----------------------------
		
		@GetMapping(value="/{id}")
		public ResponseEntity<TestNotification> getEmailNotification(@PathVariable ("id") long id) {
			logger.info("Returning the EmailNotification with the ID="+id);
			TestNotification EmailNotification =  emailNotificationService.GetById(id);
			return new ResponseEntity<TestNotification>(EmailNotification, HttpStatus.OK);
		}
		
		//---------------------- Create a new EmailNotification -----------------------------
		@PostMapping
		public ResponseEntity<TestNotification> createEmailNotification(@RequestBody TestNotification emailNotification ){
			logger.info("Creating a new EmailNotification and saving it to database.");
			 emailNotification =  emailNotificationService.add(emailNotification);
			 return new ResponseEntity<TestNotification>(emailNotification, HttpStatus.CREATED);
		}
		
		//-------------------- Update a EmailNotification ----------------------------------
		@PutMapping
		public ResponseEntity<TestNotification> updateEmailNotification(@RequestBody TestNotification emailNotification ){
			logger.info("Updating a EmailNotification .");
			emailNotification = emailNotificationService.update(emailNotification);
			return new ResponseEntity<TestNotification>(emailNotification, HttpStatus.OK);
		}
		
		//-------------------------Delete a EmailNotification --------------------------------
		@DeleteMapping(value="/{id}")
		public ResponseEntity<HttpStatus> deleteEmailNotification(@PathVariable long id) {
			logger.info("deleting TestNotification from database.");
			emailNotificationService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
			
		}
		//--------------Delete all Notifications -----------------------------
		@DeleteMapping
		public ResponseEntity<HttpStatus> deleteAllEmailNotification(){
			logger.info("deleting all TestNotifications from database.");
			emailNotificationService.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		//---------------------- Send a EmailNotification Now -----------------------------
		@PostMapping("/sendNow")
		public ResponseEntity<TestNotification> sendEmailNotificationNow(@RequestBody TestNotification emailNotification ){
			logger.info("Creating a new Email EmailNotification Now and saving it to database.");
			emailNotification =  emailNotificationService.sendNow(emailNotification);
			 return new ResponseEntity<TestNotification>(emailNotification, HttpStatus.CREATED);
		}
}
