package com.smsSender.server.servicesImpl;

import java.io.IOException;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smsSender.server.utils.CSVHelper;
import com.smsSender.server.entities.Contact;
import com.smsSender.server.repositories.ContactRepository;


@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository ;

    public void save(MultipartFile file) throws NumberFormatException, ParseException 
    {
        try
        {
            List<Contact> infos = CSVHelper.csvToContacts(file.getInputStream());
                contactRepository.saveAll(infos);
        } 
        catch (IOException e) { throw new RuntimeException("fail to store csv data: "+e.getMessage()); }
            //TODO: handle exception
        }
    }
