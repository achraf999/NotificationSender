package com.smsSender.server.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smsSender.server.entities.Journal;
import com.smsSender.server.repositories.JournalRepository;
import com.smsSender.server.services.IJournal;

@Service
public class JournalService implements IJournal {
	
	@Autowired
	JournalRepository journalRepository;

	@Override
	public Journal add(Journal journal) {
		return journalRepository.save(journal); 
	}

	@Override
	public void deleteById(long id) {
		journalRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		journalRepository.deleteAll();
	}

	@Override
	public List<Journal> getAll() {
		return journalRepository.findAll();
	}

	@Override
	public Journal getById(long id) {
		return journalRepository.findById(id).get();
	}

	@Override
	public List<Journal> getByStatus(boolean status) {
		return journalRepository.findByStatus(status);
	}

}
