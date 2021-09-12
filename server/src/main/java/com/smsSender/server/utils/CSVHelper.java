package com.smsSender.server.utils;




import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import com.smsSender.server.entities.Contact;



@Component
public class CSVHelper 
{

	
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "id", "email", "phone", "matricule", "dateEcheance"};

  //This method is used to check if the  file format is CSV or not.
  public static boolean hasCSVFormat(MultipartFile file) 
  {
    if (TYPE.equals(file.getContentType()) || file.getContentType().equals("application/vnd.ms-excel"))
    {
      return true;
    }

    return false;
  }

  
  //This method is completely used for reading the CSV File data

  public static List<Contact> csvToContacts(InputStream is) throws NumberFormatException, ParseException 
  {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    		
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) 
    {
    	
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	
      List<Contact> contactsList = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
      
 


      for (CSVRecord csvRecord : csvRecords) 
      {
    	  
    	 String deadline =  csvRecord.get("dateEcheance");
         Date date = formatter.parse(deadline);
    	  
    	 Contact contact = new Contact(Long.parseLong(csvRecord.get("id")), csvRecord.get("email"), csvRecord.get("phone"), csvRecord.get("matricule"), date );
    	 
    	 contactsList.add(contact);
      }

      return contactsList;
      
    }
    catch (IOException e) { throw new RuntimeException("fail to parse CSV file: " + e.getMessage()); }
  }
  
  
	/*
	 * //in case we want to get the email addresses directly from the CSV file not
	 * from the DB public List<String> csvToEmails() throws NumberFormatException,
	 * ParseException { InputStream is =
	 * ClassLoader.getSystemClassLoader().getResourceAsStream("smsender.csv");
	 * 
	 * try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is,
	 * "UTF-8")); CSVParser csvParser = new CSVParser(fileReader,
	 * CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()
	 * );) {
	 * 
	 * 
	 * List<String> emails = new ArrayList<>(); Iterable<CSVRecord> csvRecords =
	 * csvParser.getRecords(); for (CSVRecord csvRecord : csvRecords) { String email
	 * = csvRecord.get("email"); emails.add(email); } return emails; } catch
	 * (IOException e) { throw new RuntimeException("fail to get emails: " +
	 * e.getMessage()); } }
	 * 
	 */
  

  

  //This method is used to write the data in the CSV file from the MySQL database table.
	/*
	 * public static ByteArrayInputStream contactsToCSV(List<ContactsFile> fileList)
	 * { final CSVFormat format =
	 * CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
	 * 
	 * try (ByteArrayOutputStream out = new ByteArrayOutputStream(); CSVPrinter
	 * csvPrinter = new CSVPrinter(new PrintWriter(out), format);) { for
	 * (ContactsFile contactsFile : fileList) { List<String> data = Arrays.asList(
	 * String.valueOf(contactsFile.getId()), contactsFile.getEmailAddress(),
	 * contactsFile.getDeadlineDate()
	 * 
	 * );
	 * 
	 * csvPrinter.printRecord(data); }
	 * 
	 * csvPrinter.flush(); return new ByteArrayInputStream(out.toByteArray()); }
	 * catch (IOException e) { throw new
	 * RuntimeException("fail to import data to CSV file: " + e.getMessage()); } }
	 */
}