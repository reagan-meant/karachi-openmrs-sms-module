package org.openmrs.module.bahminischeduling;

public class PersonAttribute {
	
	int person_attribute_id;
	
	int person_id;
	
	String value;
	
	int person_attribute_type_id;
	
	public PersonAttribute() {
		
	}
	
	public PersonAttribute(int person_attribute_id, int person_id, String value, int person_attribute_type_id) {
		this.person_attribute_id = person_attribute_id;
		this.person_id = person_id;
		this.value = value;
		this.person_attribute_type_id = person_attribute_type_id;
		
	}
	
	public int getPerson_attribute_id() {
		return person_attribute_id;
	}
	
	public void setPerson_attribute_id(int person_attribute_id) {
		this.person_attribute_id = person_attribute_id;
	}
	
	public int getPerson_id() {
		return person_id;
	}
	
	public void setPerson_id(int person_id) {
		this.person_id = person_id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getPerson_attribute_type_id() {
		return person_attribute_type_id;
	}
	
	public void setPerson_attribute_type_id(int person_attribute_type_id) {
		this.person_attribute_type_id = person_attribute_type_id;
	}
	
}
