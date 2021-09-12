package com.smsSender.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smsSender.server.entities.SmsNotification;
import com.smsSender.server.entities.WhatsAppNotification;

@Repository
public interface SmsNotificationRepository extends JpaRepository<SmsNotification, Long> {
	public List<SmsNotification> findByStatus(String status);
	public List<SmsNotification> findByFrequence(String status);
}
