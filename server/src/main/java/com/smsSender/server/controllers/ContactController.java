package com.smsSender.server.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smsSender.server.utils.CSVHelper;
import com.smsSender.server.dto.CSVResponseMessage;
import com.smsSender.server.entities.Contact;
import com.smsSender.server.entities.User;
import com.smsSender.server.repositories.ContactRepository;
import com.smsSender.server.repositories.UserRepository;
import com.smsSender.server.servicesImpl.ContactService;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

	@Autowired
	ContactService fileService;
	
	@Autowired
	ContactRepository contactRepository;
	
	// logger 
	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
		
	@PostMapping()
	public ResponseEntity<CSVResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		// check if the file had the right format
		if (CSVHelper.hasCSVFormat(file)) {
			try {
				fileService.save(file);
				message = "Uploaded the file successfully:" + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new CSVResponseMessage(message));
			} catch (Exception e) {
				e.printStackTrace();
				message = "Could not upload the file:" + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new CSVResponseMessage(message));
				// TODO: handle exception
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CSVResponseMessage(message));
	}
	
	@GetMapping("")
	public ResponseEntity<?> getALlContacts(){
		logger.info("Returning Contacts list from database.");
		
		//SecurityContextHolder.getContext().getAuthentication();
		List<Contact> contacts =  contactRepository.findAll();
		return new ResponseEntity<>(contacts, HttpStatus.OK);
	}
}