package com.smsSender.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsSender.server.entities.EmailNotification;
import com.smsSender.server.entities.WhatsAppNotification;

@Repository
public interface WhatsAppNotificationRepository extends JpaRepository<WhatsAppNotification, Long> {
	public List<WhatsAppNotification> findByStatus(String status);
	public List<WhatsAppNotification> findByFrequence(String status);

}
