package org.openmrs.module.bahminischeduling;

public class PersonAttributeType {
	
	private int person_attribute_type_id;
	
	private String name;
	
	private String description;
	
	private String format;
	
	private int foreign_key;
	
	public PersonAttributeType() {
	}
	
	public PersonAttributeType(int person_attribute_type_id, String name, String description, String format, int foreign_key) {
		
		this.person_attribute_type_id = person_attribute_type_id;
		this.name = name;
		this.description = description;
		this.format = format;
		this.foreign_key = foreign_key;
		
	}
	
	public int getPerson_attribute_type_id() {
		return person_attribute_type_id;
	}
	
	public void setPerson_attribute_type_id(int person_attribute_type_id) {
		this.person_attribute_type_id = person_attribute_type_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public int getForeign_key() {
		return foreign_key;
	}
	
	public void setForeign_key(int foreign_key) {
		this.foreign_key = foreign_key;
	}
	
}
