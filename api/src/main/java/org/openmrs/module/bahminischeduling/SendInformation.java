package org.openmrs.module.bahminischeduling;

import org.springframework.stereotype.Component;

@Component
public class SendInformation {
	
	private String contactNumber;
	
	private String Name;
	
	private String preferredLanguage;
	
	private String oneDayMessage;
	
	private String oneWeekMessage;
	
	private String startDateTime;
	
	private String endDateTime;
	
	private String patientAppointmentIdsForOneDay;
	
	private String patientAppointmentIdsForOneWeek;
	
	private int PatientId;
	
	private String consent;
	
	public String getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public String getPreferredLanguage() {
		return preferredLanguage;
	}
	
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	
	public String getOneDayMessage() {
		return oneDayMessage;
	}
	
	public void setOneDayMessage(String oneDayMessage) {
		this.oneDayMessage = oneDayMessage;
	}
	
	public String getOneWeekMessage() {
		return oneWeekMessage;
	}
	
	public void setOneWeekMessage(String oneWeekMessage) {
		this.oneWeekMessage = oneWeekMessage;
	}
	
	public String getStartDateTime() {
		return startDateTime;
	}
	
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public String getEndDateTime() {
		return endDateTime;
	}
	
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	public String getPatientAppointmentIdsForOneDay() {
		return patientAppointmentIdsForOneDay;
	}
	
	public void setPatientAppointmentIdsForOneDay(String patientAppointmentIdsForOneDay) {
		this.patientAppointmentIdsForOneDay = patientAppointmentIdsForOneDay;
	}
	
	public String getPatientAppointmentIdsForOneWeek() {
		return patientAppointmentIdsForOneWeek;
	}
	
	public void setPatientAppointmentIdsForOneWeek(String patientAppointmentIdsForOneWeek) {
		this.patientAppointmentIdsForOneWeek = patientAppointmentIdsForOneWeek;
	}
	
	public int getPatientId() {
		return PatientId;
	}
	
	public void setPatientId(int patientId) {
		PatientId = patientId;
	}
	
	public String getConsent() {
		return consent;
	}
	
	public void setConsent(String consent) {
		this.consent = consent;
	}
	
}
