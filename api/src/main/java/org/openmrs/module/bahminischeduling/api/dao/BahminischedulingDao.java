/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.bahminischeduling.api.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.bahminischeduling.AppointmentReminderLog;
import org.openmrs.module.bahminischeduling.AppointmentService;
import org.openmrs.module.bahminischeduling.BahminischedulingActivator;
import org.openmrs.module.bahminischeduling.ConceptName;
import org.openmrs.module.bahminischeduling.DataLoad;
import org.openmrs.module.bahminischeduling.Item;
import org.openmrs.module.bahminischeduling.PatientAppointment;
import org.openmrs.module.bahminischeduling.PatientAppointmentReminder;
import org.openmrs.module.bahminischeduling.PersonAttribute;
import org.openmrs.module.bahminischeduling.PersonAttributeType;
import org.openmrs.module.bahminischeduling.PersonName;
import org.openmrs.module.bahminischeduling.SmsLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

@Repository
public class BahminischedulingDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	/*@Autowired
	JdbcTemplate jdbcTemplate;*/
	
	public static Logger LOGGER = Logger.getLogger(BahminischedulingDao.class);
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/*public Item getItemByUuid(String uuid) {
		return (Item) getSession().createCriteria(Item.class).add(Restrictions.eq("uuid", uuid)).uniqueResult();
	}*/
	
	public Item saveItem(Item item) {
		getSession().saveOrUpdate(item);
		return item;
	}
	
	/**
	 * Bahmini api
	 */
	
	public void insert(AppointmentReminderLog appointmentReminderLog) {
		BahminischedulingActivator.jdbcTemplate.update("INSERT INTO openmrs.appointment_reminder_log (message, "
		        + " patient_appointment_ids_one_day , " + " patient_appointment_ids_one_week , " + " patient_id,"
		        + " sent_on," + " contactNumber," + " messagesid," + " messagingServiceSid," + " error_code,"
		        + " errorMessage)" + " VALUES (?,?,?,?,?,?,?,?,?,?)", appointmentReminderLog.getMessage(),
		    appointmentReminderLog.getPatient_appointment_ids_one_day(),
		    appointmentReminderLog.getPatient_appointment_ids_one_week(), appointmentReminderLog.getPatient_id(),
		    appointmentReminderLog.getSent_on(), appointmentReminderLog.getContactNumber(),
		    appointmentReminderLog.getMessagesid(), appointmentReminderLog.getMessagingServiceSid(),
		    appointmentReminderLog.getError_code(), appointmentReminderLog.getErrorMessage());
	}
	
	public   String getAppointmentServiceName(int appointmentServiceId)
		{
			  System.out.println("METHOD : getAppointmentServiceName  appointmentServiceId="+appointmentServiceId);
	
			  
			  
			  List<AppointmentService> appointmentServices=new ArrayList<AppointmentService>();
	
			  BahminischedulingActivator.jdbcTemplate.query(
		                "SELECT * FROM  appointment_service where appointment_service_id = ?", 
		                new Object[] { appointmentServiceId },
		                (rs, rowNum) -> new AppointmentService(
		                		rs.getInt("appointment_service_id"), 
		                		rs.getString("name")
		                	
		                	)
		        )
			
			  .forEach(appointmentService -> appointmentServices.add(appointmentService));
			
			  
			  
			  AppointmentService appointmentService = new AppointmentService();
			  
			  if(appointmentServices.size() >0 )
			   { appointmentService = appointmentServices.get(0) ; }
			
			  
			  
			  return appointmentService.getName() ;
		}
	
	public  ConceptName getConceptNameByConceptId(int conceptId)
	 {
		 System.out.println("METHOD : getConceptNameByConceptId  ");
	
		 List<ConceptName> conceptNameList=new ArrayList<ConceptName>();
	
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  concept_name where concept_id = ?", 
				 new Object[] { conceptId },
				 (rs, rowNum) -> new ConceptName(rs.getInt("concept_name_id"), 
						 rs.getInt("concept_id"), 
						 rs.getString("name"))
		 )
			
		 .forEach(conceptName -> conceptNameList.add(conceptName));
			
			  
			  
		 ConceptName conceptName = new ConceptName();
			  
		 if(conceptNameList.size() >0 ){ 
			 conceptName = conceptNameList.get(0); 
		 }
			
		 return conceptName ;
	 }	public  String getDataLoadedCheck(){
		 String dataLoadedCheck ="";
		 System.out.println("METHOD : getDataLoadedCheck  ");
		 List<DataLoad> dataLoadList=new ArrayList<DataLoad>();
	
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  data_load ", 
				 (rs, rowNum) -> new DataLoad(rs.getInt("id"), 
						 rs.getString("data_loaded_check"))
				 )
				 .forEach(objDataloaded -> dataLoadList.add(objDataloaded));
			
		 if(dataLoadList.size()>0){
			 dataLoadedCheck = dataLoadList.get(0).getData_loaded_check();
		 }
		 return dataLoadedCheck;
	 }
	
	public void updateDataLoadedCheckYes() {
		System.out.println(" start updateDataLoadedCheckYes ");
		BahminischedulingActivator.jdbcTemplate.update("update data_load set data_loaded_check = ? where id = ?", "YES", 1);
	}
	
	public void insertDataLoadedCheckYes() {
		BahminischedulingActivator.jdbcTemplate.update("INSERT INTO data_load ( id,  data_loaded_check )" + " VALUES (?,?)",
		    1, "YES"
		
		);
	}
	
	public  List<DataLoad>  getDataLoaded(){
		 String dataLoadedCheck ="";
		 System.out.println("METHOD : getDataLoaded  ");
		 
		 List<DataLoad> dataLoadList=new ArrayList<DataLoad>();
	
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  data_load ", 
				 (rs, rowNum) -> new DataLoad(rs.getInt("id"), 
						 rs.getString("data_loaded_check"))
				 )
				 .forEach(objDataloaded -> dataLoadList.add(objDataloaded));			  
		 return dataLoadList;
	 }
	
	public  List<PatientAppointment> getPatientAppointmentsByStatus(String status){
		 LOGGER.info(" METHOD :  getPatientAppointmentsByStatus start");
		 
			  System.out.println("METHOD : query  ");
	
			  List<PatientAppointment> patientAppList=new ArrayList<PatientAppointment>();
	
			  BahminischedulingActivator.jdbcTemplate.query(
		                "SELECT * FROM  patient_appointment where status = ?", 
		                new Object[] { status },
		                (rs, rowNum) -> new PatientAppointment(rs.getInt("patient_appointment_id"), 
		                		rs.getInt("provider_id"), 
		                		rs.getInt("appointment_number"), 
		                		rs.getInt("patient_id"), 
		                		rs.getInt("appointment_service_id"), 
		                		rs.getInt("appointment_service_type_id"), 
		                		rs.getString("status"), 
		                		rs.getString("appointment_kind"), 
		                		rs.getString("start_date_time") , 
		                		rs.getString("end_date_time"))
		        )
			
			  .forEach(patientApp -> patientAppList.add(patientApp) );
			
			  LOGGER.info(" METHOD :  getPatientAppointmentsByStatus End");
			
			  return patientAppList;
		}
	
	public  List<PatientAppointment> getPatientAppointmentsGreaterThanPatientaAppointmentId(int patientAppointmentId , String status)
		{
			  System.out.println("METHOD : getPatientAppointmentsGreaterThanPatientaAppointmentId start patientAppointmentId="+patientAppointmentId);
	
			  LOGGER.info(" METHOD :  getPatientAppointmentsByStatus start");
			  LOGGER.info(" METHOD :  getPatientAppointmentsByStatus patientAppointmentId="+patientAppointmentId);
			  LOGGER.info(" METHOD :  getPatientAppointmentsByStatus status="+status);
			 
			  List<PatientAppointment> patientAppList=new ArrayList<PatientAppointment>();
	
			  BahminischedulingActivator.jdbcTemplate.query(
		                "SELECT * FROM  patient_appointment where patient_appointment_id > ? and status = ? ", 
		                new Object[] { patientAppointmentId , status },
		                (rs, rowNum) -> new PatientAppointment(rs.getInt("patient_appointment_id"), 
		                		rs.getInt("provider_id"), 
		                		rs.getInt("appointment_number"), 
		                		rs.getInt("patient_id"), 
		                		rs.getInt("appointment_service_id"), 
		                		rs.getInt("appointment_service_type_id"), 
		                		rs.getString("status"), 
		                		rs.getString("appointment_kind"), 
		                		rs.getString("start_date_time") , 
		                		rs.getString("end_date_time"))
		        )
			
			  .forEach(patientApp -> patientAppList.add(patientApp) );
			
			
			  LOGGER.info(" METHOD :  getPatientAppointmentsByStatus End");
			  
			  return patientAppList;
		}
	
	public void updatePatientAppointment(int id, String status) {
		
		System.out.println(" start updatePatientAppointment ");
		BahminischedulingActivator.jdbcTemplate.update(
		    "update patient_appointment set status = ? where patient_appointment_id = ?", status, id);
	}
	
	public  List<PatientAppointmentReminder> getPatientAppointmentReminderListBySmsStatus(){
			 
		 LOGGER.info("METHOD  : getPatientAppointmentReminderListBySmsStatus start ");
	
		 List<PatientAppointmentReminder> patientAppointmentReminderList=new ArrayList<PatientAppointmentReminder>();
	
		 BahminischedulingActivator.jdbcTemplate.query(
//				 "SELECT * FROM  patient_appointment_reminder  where (sms_status_one_day !='success' or sms_status_seven_day != 'success') and appointment_service_id != 7", 
				 "SELECT * FROM  patient_appointment_reminder  where ( (DATEDIFF(start_date_time,CURDATE()) = 1 and sms_status_one_day != 'success')  OR  (DATEDIFF(start_date_time,CURDATE()) = 7 and sms_status_seven_day != 'success') ) and appointment_service_id != 7",
				 (rs, rowNum) -> new PatientAppointmentReminder(rs.getInt("id"),
						 rs.getInt("patient_appointment_id"), 
						 rs.getInt("patient_id"),
						 rs.getString("start_date_time"),
						 rs.getString("end_date_time"),
						 rs.getInt("appointment_service_id"),
						 rs.getInt("appointment_service_type_id"),
						 rs.getString("status"),
						 rs.getString("consent"),
						 rs.getString("sms_status_one_day"),
						 rs.getString("sms_status_seven_day")		                		
						 )
				 )
			
				 .forEach(conceptName -> patientAppointmentReminderList.add(conceptName));	
		 LOGGER.info("METHOD  : getPatientAppointmentReminderListBySmsStatus End ");
		 return patientAppointmentReminderList;
	 }
	
	public void insert(List<PatientAppointmentReminder> patientAppointmentReminderList) {
		LOGGER.info("METHOD  : insert start ");
		try {
			String sql = "insert into patient_appointment_reminder ( " + " id ," + " patient_appointment_id ,"
			        + " patient_id , " + " start_date_time ," + " end_date_time , " + " appointment_service_id , "
			        + " appointment_service_type_id ," + " status ," + " consent ," + " sms_status_one_day ,"
			        + "sms_status_seven_day" + " ) values (?,?,?,?,?,?,?,?,?,?,?)";
			
			int[][] updateCounts = BahminischedulingActivator.jdbcTemplate.batchUpdate(sql, patientAppointmentReminderList,
			    patientAppointmentReminderList.size(),
			    
			    new ParameterizedPreparedStatementSetter<PatientAppointmentReminder>() {
				    
				    @Override
				    public void setValues(PreparedStatement ps, PatientAppointmentReminder patientAppointmentReminder) {
					    try {
						    ps.setInt(1, patientAppointmentReminder.getId());
						    ps.setInt(2, patientAppointmentReminder.getPatient_appointment_id());
						    ps.setInt(3, patientAppointmentReminder.getPatient_id());
						    ps.setString(4, patientAppointmentReminder.getStart_date_time());
						    ps.setString(5, patientAppointmentReminder.getEnd_date_time());
						    ps.setInt(6, patientAppointmentReminder.getAppointment_service_id());
						    ps.setInt(7, patientAppointmentReminder.getAppointment_service_type_id());
						    ps.setString(8, patientAppointmentReminder.getStatus());
						    ps.setString(9, patientAppointmentReminder.getConsent());
						    ps.setString(10, patientAppointmentReminder.getSmsStatusOneDay());
						    ps.setString(11, patientAppointmentReminder.getSmsStatusSevenDay());
					    }
					    catch (SQLException e) {
						    // TODO Auto-generated catch block
						    e.printStackTrace();
					    }
				    }
			    });
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("METHOD  : insert End ");
	}
	
	public PatientAppointmentReminder  getPatientAppointmentReminderByMaxValueOfPatientAppointmentId()
	 {
		 LOGGER.info("METHOD  : getPatientAppointmentReminderByMaxValueOfPatientAppointmentId start "); 
		 System.out.println("METHOD : getPatientAppointmentReminderByMaxValueOfPatientAppointmentId  ");			
		 List<PatientAppointmentReminder> patientAppointmentReminderList=new ArrayList<PatientAppointmentReminder>();

		 PatientAppointmentReminder patientAppointmentReminder=new PatientAppointmentReminder();
		  
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  patient_appointment_reminder order by patient_appointment_id DESC LIMIT 1    ", 
				 (rs, rowNum) -> new PatientAppointmentReminder(rs.getInt("id"),
						 rs.getInt("patient_appointment_id"), 
						 rs.getInt("patient_id"),
						 rs.getString("start_date_time"),
						 rs.getString("end_date_time"),
						 rs.getInt("appointment_service_id"),
						 rs.getInt("appointment_service_type_id"),
						 rs.getString("status"),
						 rs.getString("consent"),
						 rs.getString("sms_status_one_day"),
						 rs.getString("sms_status_seven_day")
						 )
				 )
				 .forEach(patientAppointmentReminderTemp -> patientAppointmentReminderList.add(patientAppointmentReminderTemp));

		 if(patientAppointmentReminderList.size()>0)
		 { System.out.println("METHOD : getPatientAppointmentReminderByMaxValueOfPatientAppointmentId  patientAppointmentReminderList.size()="+patientAppointmentReminderList.size() );
			  
		 patientAppointmentReminder = patientAppointmentReminderList.get(0);
		    
		 System.out.println("METHOD : getPatientAppointmentReminderByMaxValueOfPatientAppointmentId  maXID="+patientAppointmentReminder.getPatient_appointment_id());
		 LOGGER.info("METHOD  : getPatientAppointmentReminderByMaxValueOfPatientAppointmentId maXID="+patientAppointmentReminder.getPatient_appointment_id() );
		 return patientAppointmentReminder ;
		 }
		 else
		 { LOGGER.info("METHOD  : getPatientAppointmentReminderByMaxValueOfPatientAppointmentId End ");
		 return patientAppointmentReminder ;
		 }	 
	 }
	
	public PatientAppointmentReminder  getPatientAppointmentReminderByMaxValueId()
	 {
		 LOGGER.info(" METHOD :  getPatientAppointmentReminderByMaxValueId start");
		 System.out.println("METHOD : getLastPatientAppointmentId  ");
			
		 List<PatientAppointmentReminder> patientAppointmentReminderList=new ArrayList<PatientAppointmentReminder>();

		 PatientAppointmentReminder patientAppointmentReminder=new PatientAppointmentReminder();
		  
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  patient_appointment_reminder order by id DESC LIMIT 1    ", 
				 (rs, rowNum) -> new PatientAppointmentReminder(rs.getInt("id"),
						 rs.getInt("patient_appointment_id"), 
						 rs.getInt("patient_id"),
						 rs.getString("start_date_time"),
						 rs.getString("end_date_time"),
						 rs.getInt("appointment_service_id"),
						 rs.getInt("appointment_service_type_id"),
						 rs.getString("status"),
						 rs.getString("consent"),
						 rs.getString("sms_status_one_day"),
						 rs.getString("sms_status_seven_day")
						 )
				 )
				 .forEach(patientAppointmentReminderTemp -> patientAppointmentReminderList.add(patientAppointmentReminderTemp));
		 if(patientAppointmentReminderList.size()>0)
		 {
			 patientAppointmentReminder = patientAppointmentReminderList.get(0);   
			 LOGGER.info(" METHOD :  getPatientAppointmentReminderByMaxValueId END");
			 return patientAppointmentReminder ;
		 }
		 else{
			 LOGGER.info(" METHOD :  getPatientAppointmentReminderByMaxValueId END");
			 return patientAppointmentReminder ;
		 }	 
	 }
	
	public void updateSmsStatusByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus) {
		LOGGER.info(" METHOD :  updateSmsStatusByPatientAppointmentId START");
		try {
			String sql = " update  patient_appointment_reminder " + "set sms_status='" + smsStatus + "'"
			        + " where  patient_appointment_id = ? ";
			int[][] updateCounts = BahminischedulingActivator.jdbcTemplate.batchUpdate(sql, patientAppointmentReminderList,
			    patientAppointmentReminderList.size(),
			    
			    new ParameterizedPreparedStatementSetter<PatientAppointmentReminder>() {
				    
				    @Override
				    public void setValues(PreparedStatement ps, PatientAppointmentReminder patientAppointmentReminder) {
					    try {
						    ps.setInt(1, patientAppointmentReminder.getPatient_appointment_id());
					    }
					    catch (SQLException e) {
						    // TODO Auto-generated catch block
						    e.printStackTrace();
					    }
				    }
			    });
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(" METHOD :  updateSmsStatusByPatientAppointmentId End");
	}
	
	public  List<PersonAttribute> getPersonAttributeByPersonId(int id){
		 System.out.println("METHOD : getPersonAttributeByPersonId  ");
		 List<PersonAttribute> personAttributesList=new ArrayList<PersonAttribute>();	
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  person_attribute where person_id = ?", 
				 new Object[] { id },
				 (rs, rowNum) -> new PersonAttribute(rs.getInt("person_attribute_id"), 
						 rs.getInt("person_id"), 
						 rs.getString("value"), 
						 rs.getInt("person_attribute_type_id"))
				 )
				 .forEach(patientApp -> personAttributesList.add(patientApp));			
		 return personAttributesList;
	 }
	
	public  PersonAttribute getConsentByPersonId(int id, int personAttTypeId){
		 System.out.println("METHOD : getPersonAttributeByPersonId  ");
		 List<PersonAttribute> personAttributesList=new ArrayList<PersonAttribute>();
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  person_attribute where person_id = ? and person_attribute_type_id=?", 
				 new Object[] { id,personAttTypeId },
				 (rs, rowNum) -> new PersonAttribute(rs.getInt("person_attribute_id"), 
						 rs.getInt("person_id"), 
						 rs.getString("value"), 
						 rs.getInt("person_attribute_type_id"))
				 )
				 .forEach(patientApp -> personAttributesList.add(patientApp));			
		 return personAttributesList.get(0);
	 }
	
	public void updatePersonAttributet(int id, String status) {
		System.out.println(" start updatePersonAttributet ");
	}
	
	public  List<PersonAttributeType> getPersonAttributeTypeById(int id){
		 System.out.println("METHOD : getPersonAttributeTypeById  ");
		 List<PersonAttributeType> personAttributeTypeList=new ArrayList<PersonAttributeType>();
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  person_attribute_type where person_attribute_type_id = ?", 
				 new Object[] { id },
				 (rs, rowNum) -> new PersonAttributeType(rs.getInt("person_attribute_type_id"), 
						 rs.getString("name"), 
						 rs.getString("description"), 
						 rs.getString("format"), 
						 rs.getInt("foreign_key")
						 )
				 )
				 .forEach(patientApp -> personAttributeTypeList.add(patientApp));
		 return personAttributeTypeList;
	 }
	
	public PersonName getPersonNameByPersonId(int id){
		 System.out.println("METHOD : getPersonNameByPersonId  ");		
		 List<PersonName> personNameList=new ArrayList<PersonName>();	
		 BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM  openmrs.person_name where person_id = ?", 
				 new Object[] { id },
				 (rs, rowNum) -> new PersonName(rs.getInt("person_name_id"), 
						 rs.getInt("person_id"), 
						 rs.getString("given_name")
						 )
				 )
				 .forEach(patientApp -> personNameList.add(patientApp));
		 return personNameList.get(0);
	 }
	
	public void insertIntoDataLoad() {
		BahminischedulingActivator.jdbcTemplate.execute(" INSERT INTO  data_load " + " (id, " + " data_loaded_check) "
		        + " VALUES " + " ( 1, " + " 'NO') ");
	}
	
	public SmsLanguage getSmsByLocaleAndDay(String locale, Integer day) {
		System.out.println("METHOD : getDataLoaded  ");
		List<SmsLanguage> smsLanguageList = new ArrayList<SmsLanguage>();
		BahminischedulingActivator.jdbcTemplate.query(
				 "SELECT * FROM sms_language where locale=? and day=?", 
				 new Object[] { locale,day },
				 (rs, rowNum) -> new SmsLanguage( 
						 rs.getString("text_message"),
						 rs.getString("locale"))
				 )
				 .forEach(objSms -> smsLanguageList.add(objSms));			  
		 return smsLanguageList.get(0);
	}
	
	public void updateSmsStatusOneDayByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus) {
		LOGGER.info(" METHOD :  updateSmsStatusByPatientAppointmentId START");
		try {
			String sql = " update  patient_appointment_reminder " + "set sms_status_one_day='" + smsStatus + "'"
			        + " where  patient_appointment_id = ? ";
			int[][] updateCounts = BahminischedulingActivator.jdbcTemplate.batchUpdate(sql, patientAppointmentReminderList,
			    patientAppointmentReminderList.size(),
			    
			    new ParameterizedPreparedStatementSetter<PatientAppointmentReminder>() {
				    
				    @Override
				    public void setValues(PreparedStatement ps, PatientAppointmentReminder patientAppointmentReminder) {
					    try {
						    ps.setInt(1, patientAppointmentReminder.getPatient_appointment_id());
					    }
					    catch (SQLException e) {
						    // TODO Auto-generated catch block
						    e.printStackTrace();
					    }
				    }
			    });
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(" METHOD :  updateSmsStatusByPatientAppointmentId End");
	}
	
	public void updateSmsStatusSevenDayByPatientAppointmentId(
	        List<PatientAppointmentReminder> patientAppointmentReminderList, String smsStatus) {
		LOGGER.info(" METHOD :  updateSmsStatusByPatientAppointmentId START");
		try {
			String sql = " update  patient_appointment_reminder " + "set sms_status_seven_day='" + smsStatus + "'"
			        + " where  patient_appointment_id = ? ";
			int[][] updateCounts = BahminischedulingActivator.jdbcTemplate.batchUpdate(sql, patientAppointmentReminderList,
			    patientAppointmentReminderList.size(),
			    
			    new ParameterizedPreparedStatementSetter<PatientAppointmentReminder>() {
				    
				    @Override
				    public void setValues(PreparedStatement ps, PatientAppointmentReminder patientAppointmentReminder) {
					    try {
						    ps.setInt(1, patientAppointmentReminder.getPatient_appointment_id());
					    }
					    catch (SQLException e) {
						    // TODO Auto-generated catch block
						    e.printStackTrace();
					    }
				    }
			    });
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(" METHOD :  updateSmsStatusByPatientAppointmentId End");
	}
}
