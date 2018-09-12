package org.openmrs.module.bahminischeduling;

import java.util.Date;

import org.joda.time.DateTime;

public class PatientAppointmentReminder {
	
	private int id;
	
	private int patient_appointment_id;
	
	private int patient_id;
	
	private String start_date_time;
	
	private String end_date_time;
	
	private int appointment_service_id;
	
	private int appointment_service_type_id;
	
	private String status;
	
	private String consent;
	
	private String smsStatusOneDay;
	
	private String smsStatusSevenDay;
	
	public PatientAppointmentReminder() {
	}
	
	public PatientAppointmentReminder(int id, int patient_appointment_id, int patient_id, String start_date_time,
	    String end_date_time, int appointment_service_id, int appointment_service_type_id, String status, String consent,
	    String smsStatusOneDay, String smsStatusSevenDay) {
		super();
		this.id = id;
		this.patient_appointment_id = patient_appointment_id;
		this.patient_id = patient_id;
		this.start_date_time = start_date_time;
		this.end_date_time = end_date_time;
		this.appointment_service_id = appointment_service_id;
		this.appointment_service_type_id = appointment_service_type_id;
		this.status = status;
		this.consent = consent;
		this.smsStatusOneDay = smsStatusOneDay;
		this.smsStatusSevenDay = smsStatusSevenDay;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getPatient_appointment_id() {
		return patient_appointment_id;
	}
	
	public void setPatient_appointment_id(int patient_appointment_id) {
		this.patient_appointment_id = patient_appointment_id;
	}
	
	public int getPatient_id() {
		return patient_id;
	}
	
	public void setPatient_id(int patient_id) {
		this.patient_id = patient_id;
	}
	
	public String getStart_date_time() {
		return start_date_time;
	}
	
	public void setStart_date_time(String start_date_time) {
		this.start_date_time = start_date_time;
	}
	
	public String getEnd_date_time() {
		return end_date_time;
	}
	
	public void setEnd_date_time(String end_date_time) {
		this.end_date_time = end_date_time;
	}
	
	public int getAppointment_service_id() {
		return appointment_service_id;
	}
	
	public void setAppointment_service_id(int appointment_service_id) {
		this.appointment_service_id = appointment_service_id;
	}
	
	public int getAppointment_service_type_id() {
		return appointment_service_type_id;
	}
	
	public void setAppointment_service_type_id(int appointment_service_type_id) {
		this.appointment_service_type_id = appointment_service_type_id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getConsent() {
		return consent;
	}
	
	public void setConsent(String consent) {
		this.consent = consent;
	}
	
	public String getSmsStatusOneDay() {
		return this.smsStatusOneDay;
	}
	
	public void setSmsStatusOneDay(String smsStatusOneDay) {
		this.smsStatusOneDay = smsStatusOneDay;
	}
	
	public String getSmsStatusSevenDay() {
		return this.smsStatusSevenDay;
	}
	
	public void setSmsStatusSevenDay(String smsStatusSevenDay) {
		this.smsStatusSevenDay = smsStatusSevenDay;
	}
	
}
