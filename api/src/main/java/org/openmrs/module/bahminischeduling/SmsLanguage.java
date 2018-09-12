package org.openmrs.module.bahminischeduling;

public class SmsLanguage {
	
	private String textMessage;
	
	private String locale;
	
	public SmsLanguage(String textMessage, String locale) {
		this.textMessage = textMessage;
		this.locale = locale;
	}
	
	public String getTextMessage() {
		return textMessage;
	}
	
	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
