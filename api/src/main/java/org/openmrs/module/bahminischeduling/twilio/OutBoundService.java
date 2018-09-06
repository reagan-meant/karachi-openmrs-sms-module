package org.openmrs.module.bahminischeduling.twilio;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahminischeduling.twilio.IOutBoundService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class OutBoundService implements IOutBoundService {
	
	public Message sendSmsService(String toNumber, String textMessage) {
		
		System.out.println("METHOD : sendSmsService start toNumber=" + toNumber + "----textMessage=" + textMessage);
		
		Message message = null;
		
		try {
			
			Twilio.init(Context.getAdministrationService().getGlobalProperty(ACCOUNT_SID), Context
			        .getAdministrationService().getGlobalProperty(AUTH_TOKEN));
			message = Message.creator(new PhoneNumber(toNumber),
			    new PhoneNumber(Context.getAdministrationService().getGlobalProperty(TWILIO_NUMBER)), textMessage).create();
			
		}
		catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
		}
		
		return message;
	}
	
}
