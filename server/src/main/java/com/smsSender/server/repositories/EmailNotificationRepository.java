package com.smsSender.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsSender.server.entities.EmailNotification;

@Repository
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {

	public List<EmailNotification> findByStatus(String status);
	public List<EmailNotification> findByFrequence(String status);
}
