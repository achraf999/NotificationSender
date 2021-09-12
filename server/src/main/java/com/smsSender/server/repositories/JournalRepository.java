package com.smsSender.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsSender.server.entities.EmailNotification;
import com.smsSender.server.entities.Journal;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

	public List<Journal> findByStatus(boolean status);
	
}
