package com.smsSender.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smsSender.server.entities.Journal;

import com.smsSender.server.servicesImpl.JournalService;

@RestController
@RequestMapping("/api/logs")
public class JournalController {

	@Autowired
	JournalService journal;
	

	//GET ALL LOGS
	@GetMapping
	public ResponseEntity<List<Journal>> findAll(){
		
		List<Journal> logs =  journal.getAll();
		if (logs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(logs, HttpStatus.OK);
	}
	
	//GET LOG BY ID
	@GetMapping(value="/{id}")
	public ResponseEntity<Journal> getlog(@PathVariable ("id") long id) {
		Journal log = (Journal) journal.getById(id);
		return new ResponseEntity<Journal>(log, HttpStatus.OK);
	}
	
	
	//DELETE ALL LOGS
	@DeleteMapping
	public ResponseEntity<HttpStatus> deleteAllLogs(){
		 try {
			 journal.deleteAll();
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);

	}
	
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<HttpStatus> deleteLog(@PathVariable long id) {
		 try {
			 	journal.deleteById(id);
				return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	
	
	
}


