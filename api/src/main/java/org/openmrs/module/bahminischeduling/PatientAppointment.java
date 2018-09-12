package org.openmrs.module.bahminischeduling;

import java.util.Date;

import org.joda.time.DateTime;

public class PatientAppointment {
	
	private int patient_appointment_id;
	
	private int provider_id;
	
	private int appointment_number;
	
	private int patient_id;
	
	private String start_date_time;
	
	private String end_date_time;
	
	private int appointment_service_id;
	
	private int appointment_service_type_id;
	
	private String status;
	
	private String appointment_kind;
	
	public PatientAppointment() {
		
	}
	
	public PatientAppointment(int patient_appointment_id, int provider_id, int appointment_number, int patient_id,
	    int appointment_service_id, int appointment_service_type_id, String status, String appointment_kind,
	    String start_date_time, String end_date_time) {
		this.patient_appointment_id = patient_appointment_id;
		this.provider_id = provider_id;
		this.appointment_number = appointment_number;
		this.patient_id = patient_id;
		this.appointment_service_id = appointment_service_id;
		this.appointment_service_type_id = appointment_service_type_id;
		this.status = status;
		this.appointment_kind = appointment_kind;
		this.start_date_time = start_date_time;
		this.end_date_time = end_date_time;
		
	}
	
	public int getPatient_appointment_id() {
		return patient_appointment_id;
	}
	
	public void setPatient_appointment_id(int patient_appointment_id) {
		this.patient_appointment_id = patient_appointment_id;
	}
	
	public int getProvider_id() {
		return provider_id;
	}
	
	public void setProvider_id(int provider_id) {
		this.provider_id = provider_id;
	}
	
	public int getAppointment_number() {
		return appointment_number;
	}
	
	public void setAppointment_number(int appointment_number) {
		this.appointment_number = appointment_number;
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
	
	public String getAppointment_kind() {
		return appointment_kind;
	}
	
	public void setAppointment_kind(String appointment_kind) {
		this.appointment_kind = appointment_kind;
	}
	
}
