package com.smsSender.server.repositories;

import java.util.*;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smsSender.server.entities.Contact;




//import com.smsSender.server.entities.contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> 
{
    @Query(value="SELECT email FROM contacts c WHERE c.date_echeance = CURDATE() + ?1", nativeQuery=true)	
    public List<String> findWantedEmails(int daysNum);
    
    @Query(value="SELECT date_echeance FROM contacts c WHERE c.date_echeance = CURDATE() + ?1", nativeQuery=true)	
    public List<Date> findWantedDeadlines(int daysNum);
    
    @Query(value = "SELECT * FROM contacts c WHERE c.date_echeance = DATE_ADD(CURDATE(), INTERVAL ?1 DAY)", nativeQuery = true)
	public List<Contact> getContacts(int daysNum);

    @Query(value = "SELECT email FROM contacts c WHERE c.date_echeance = DATE_ADD(CURDATE(), INTERVAL ?1 DAY)", nativeQuery = true)
   	public List<String> getContactsEmails(int daysNum);
    

    @Query(value="SELECT phone_number FROM contacts c WHERE c.date_echeance = CURDATE() + ?1", nativeQuery=true)	
    public List<String> findWantedNums(int daysNum);
    
    @Query(value="SELECT matricule FROM contacts c WHERE c.date_echeance = CURDATE() + ?1", nativeQuery=true)	
    public List<String> findWantedMats(int daysNum);

    


}
