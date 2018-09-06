package org.openmrs.module.bahminischeduling;

public class AppointmentService {
	
	private int appointment_service_id;
	
	private String name;
	
	public AppointmentService() {
		
	}
	
	public AppointmentService(int appointment_service_id, String name) {
		super();
		this.appointment_service_id = appointment_service_id;
		this.name = name;
	}
	
	public int getAppointment_service_id() {
		return appointment_service_id;
	}
	
	public void setAppointment_service_id(int appointment_service_id) {
		this.appointment_service_id = appointment_service_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
