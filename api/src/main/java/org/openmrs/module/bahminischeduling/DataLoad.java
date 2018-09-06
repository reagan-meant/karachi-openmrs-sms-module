package org.openmrs.module.bahminischeduling;

public class DataLoad {
	
	private int id;
	
	private String data_loaded_check;
	
	public DataLoad() {
	}
	
	public DataLoad(int id, String data_loaded_check) {
		super();
		this.id = id;
		this.data_loaded_check = data_loaded_check;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getData_loaded_check() {
		return data_loaded_check;
	}
	
	public void setData_loaded_check(String data_loaded_check) {
		this.data_loaded_check = data_loaded_check;
	}
	
}
