package org.openmrs.module.bahminischeduling;

import java.util.Date;

public class ConceptName {
	
	private int concept_name_id;
	
	private int concept_id;
	
	private String name;
	
	public ConceptName() {
		
	}
	
	public ConceptName(int concept_name_id, int concept_id, String name) {
		this.concept_name_id = concept_name_id;
		this.concept_id = concept_id;
		this.name = name;
		
	}
	
	public int getConcept_name_id() {
		return concept_name_id;
	}
	
	public void setConcept_name_id(int concept_name_id) {
		this.concept_name_id = concept_name_id;
	}
	
	public int getConcept_id() {
		return concept_id;
	}
	
	public void setConcept_id(int concept_id) {
		this.concept_id = concept_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
