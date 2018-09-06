package org.openmrs.module.bahminischeduling;

public class PersonName {
	
	private int person_name_id;
	
	private int person_id;
	
	private String given_name;
	
	public PersonName() {
		
	}
	
	public PersonName(int person_name_id, int person_id, String given_name) {
		this.person_name_id = person_name_id;
		this.person_id = person_id;
		this.given_name = given_name;
		
	}
	
	public int getPerson_name_id() {
		return person_name_id;
	}
	
	public void setPerson_name_id(int person_name_id) {
		this.person_name_id = person_name_id;
	}
	
	public int getPerson_id() {
		return person_id;
	}
	
	public void setPerson_id(int person_id) {
		this.person_id = person_id;
	}
	
	public String getGiven_name() {
		return given_name;
	}
	
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}
	
}
