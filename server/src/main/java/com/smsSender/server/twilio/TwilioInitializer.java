package com.smsSender.server.twilio;



import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

@Configuration
public class TwilioInitializer {
	
	private final TwilioConfiguration twilioproperties;
	
	public TwilioInitializer(TwilioConfiguration twilioproperties)
	{
		this.twilioproperties=twilioproperties;
		Twilio.init(twilioproperties.getAccountSid(), twilioproperties.getAuthToken());
		System.out.println("twilio intialized with account: "+twilioproperties.getAccountSid());
	}

}