package com.smsSender.server.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smsSender.server.entities.Destinataire;


@Repository
public interface RecipientsRepository extends JpaRepository<Destinataire, Long> {
	
@Query(value="SELECT r.email FROM destinataires r INNER JOIN email_notification e  ON r.ro_fid = e.id", nativeQuery = true)
List<String> getWantedEmails(long id);

}


