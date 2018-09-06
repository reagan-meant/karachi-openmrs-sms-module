package org.openmrs.module.bahminischeduling;

import java.util.Date;

public class AppointmentReminderLog {
	
	private int id;
	
	private String message;
	
	private int patient_id;
	
	private String sent_on;
	
	private String contactNumber;
	
	private String messagesid;
	
	private String messagingServiceSid;
	
	private int error_code;
	
	private String errorMessage;
	
	private String patient_appointment_ids_one_day;
	
	private String patient_appointment_ids_one_week;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getPatient_id() {
		return patient_id;
	}
	
	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}
	
	public String getSent_on() {
		return sent_on;
	}
	
	public void setSent_on(String sent_on) {
		this.sent_on = sent_on;
	}
	
	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getMessagesid() {
		return messagesid;
	}
	
	public void setMessagesid(String messagesid) {
		this.messagesid = messagesid;
	}
	
	public String getMessagingServiceSid() {
		return messagingServiceSid;
	}
	
	public void setMessagingServiceSid(String messagingServiceSid) {
		this.messagingServiceSid = messagingServiceSid;
	}
	
	public int getError_code() {
		return error_code;
	}
	
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getPatient_appointment_ids_one_day() {
		return patient_appointment_ids_one_day;
	}
	
	public void setPatient_appointment_ids_one_day(String patient_appointment_ids_one_day) {
		this.patient_appointment_ids_one_day = patient_appointment_ids_one_day;
	}
	
	public String getPatient_appointment_ids_one_week() {
		return patient_appointment_ids_one_week;
	}
	
	public void setPatient_appointment_ids_one_week(String patient_appointment_ids_one_week) {
		this.patient_appointment_ids_one_week = patient_appointment_ids_one_week;
	}
	
}
