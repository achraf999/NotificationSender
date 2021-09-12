package com.smsSender.server.scheduling;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.smsSender.server.repositories.ContactRepository;

import java.util.List;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhatsappJob extends QuartzJobBean {
	private static final Logger logger = LoggerFactory.getLogger(WhatsappJob.class);
	
	@Autowired
	ContactRepository fileRepo;
	
    private final String ACCOUNT_SID ="AC65e5eccc6ece8e7390fc28b3cc8c9e46";

    private final String AUTH_TOKEN = "cbae9d2a66874bb45830704b54b05258";

    private final String FROM_NUMBER = "+14092280547"
    		+ "";

	/*
	 * public void sendSms(SmsRequest sms) { Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	 * 
	 * Message message = Message.creator(new PhoneNumber(sms.getTo()), new
	 * PhoneNumber(FROM_NUMBER), sms.getMessage()) .create();
	 * System.out.println("here is my id:"+message.getSid());
	 * 
	 * }
	 */


	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("Executing whatsapp Job with key {}", context.getJobDetail().getKey());
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String body = jobDataMap.getString("body");
		int daysNum = jobDataMap.getInt("numDays");
		List<String> numbers = fileRepo.findWantedNums(daysNum);
		String[] nums = numbers.stream().toArray(String[]::new);
		String status = jobDataMap.getString("status");
		try { 
			for (String i: nums) {
				sendWhatsapp(new PhoneNumber("whatsapp:" + i), new PhoneNumber("whatsapp:+14155238886"), body);
			}
		}
		catch(Exception e ) {
			e.printStackTrace();
		}

		
	}
	
	private void sendWhatsapp(PhoneNumber to, PhoneNumber from, String message) {
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	       
        Message message1 = Message.creator(to, from, message)
                .create();
        System.out.println("here is my id:" + message1.getSid());
	}




}
