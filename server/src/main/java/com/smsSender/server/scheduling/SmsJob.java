package com.smsSender.server.scheduling;

import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.smsSender.server.repositories.ContactRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsJob extends QuartzJobBean {

	@Autowired
	ContactRepository fileRepo;

	private static final Logger logger = LoggerFactory.getLogger(WhatsappJob.class);

	private final String ACCOUNT_SID = "AC65e5eccc6ece8e7390fc28b3cc8c9e46";

	private final String AUTH_TOKEN = "cbae9d2a66874bb45830704b54b05258";

	private final String FROM_NUMBER = "+14092280547" + "";

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("Executing SMS Job with key {}", context.getJobDetail().getKey());
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String body = jobDataMap.getString("body");
		int daysNum = jobDataMap.getInt("numDays");
		String status = jobDataMap.getString("status");
		List<String> numbers = fileRepo.findWantedNums(daysNum);
		String[] nums = numbers.stream().toArray(String[]::new);
		
		try {
			for (String i : nums) {
				sendSms(new PhoneNumber(i), new PhoneNumber("MGe0bdd8ba4d02cead8612544ae0ed8748"), body);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendSms(PhoneNumber to, PhoneNumber from, String message) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Message message1 = Message.creator(to, from, message).create();
		System.out.println("here is my id:" + message1.getSid());
	}

}
