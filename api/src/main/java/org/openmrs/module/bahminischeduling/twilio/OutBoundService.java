package org.openmrs.module.bahminischeduling.twilio;

import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class OutBoundService implements IOutBoundService {
	
	public Message sendSmsService(String toNumber, String textMessage) {
		Message message = null;
		
		try {
			
			Twilio.init(Context.getAdministrationService().getGlobalProperty(ACCOUNT_SID), Context
			        .getAdministrationService().getGlobalProperty(AUTH_TOKEN));
			message = Message.creator(new PhoneNumber(toNumber),
			    new PhoneNumber(Context.getAdministrationService().getGlobalProperty(TWILIO_NUMBER)), textMessage).create();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return message;
	}
	
}
