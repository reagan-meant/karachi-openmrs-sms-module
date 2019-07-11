package org.openmrs.module.bahminischeduling.twilio;

import org.springframework.stereotype.Component;
import com.twilio.rest.api.v2010.account.Message;

@Component
public interface IOutBoundService {
	
	public static String TWILIO_NUMBER = "twilioNumber";
	
	//	public static String TWILIO_NUMBER = "+15134404250";
	// Find your Account Sid and Token at twilio.com/user/account
	/*	public static final String ACCOUNT_SID = "AC81d2a1a6edf0c9180b73cb60940aa930";
		public static final String AUTH_TOKEN = "dcceb8cec790b9e9db5a31c471acb6de";*/
	
	public static final String ACCOUNT_SID = "accountSid";
	
	public static final String AUTH_TOKEN = "authToken";
	
	public static final String COUNTRY_CODE = "countryCode";
	
	public Message sendSmsService(String toNumber, String textMessage);
	
}
