/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.bahminischeduling.api;

import java.util.List;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.bahminischeduling.AppointmentReminderLog;
import org.openmrs.module.bahminischeduling.BahminischedulingConfig;
import org.openmrs.module.bahminischeduling.ConceptName;
import org.openmrs.module.bahminischeduling.DataLoad;
import org.openmrs.module.bahminischeduling.Item;
import org.openmrs.module.bahminischeduling.PatientAppointment;
import org.openmrs.module.bahminischeduling.PatientAppointmentReminder;
import org.openmrs.module.bahminischeduling.PersonAttribute;
import org.openmrs.module.bahminischeduling.PersonAttributeType;
import org.openmrs.module.bahminischeduling.PersonName;
import org.openmrs.module.bahminischeduling.SmsLanguage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.twilio.rest.api.v2010.account.Message;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
//@Component("bahminischeduling.BahminischedulingService")
public interface BahminischedulingService extends OpenmrsService {
	
	/**
	 * Bahmini api
	 */
	
	void insert(AppointmentReminderLog appointmentReminderLog);
	
	String getAppointmentServiceName(int appointmentServiceId);
	
	ConceptName getConceptNameByConceptId(int conceptId);
	
	String getDataLoadedCheck();
	
	void updateDataLoadedCheckYes();
	
	void updateDataLoadedCheckNo();
	
	void insertDataLoadedCheckYes();
	
	List<PatientAppointment> getPatientAppointmentsByStatus(String status);
	
	List<PatientAppointment> getPatientAppointmentsGreaterThanPatientaAppointmentId(int patientAppointmentId, String status);
	
	void updatePatientAppointment(int id, String status);
	
	List<PatientAppointmentReminder> getPatientAppointmentReminderListBySmsStatus();
	
	void insert(List<PatientAppointmentReminder> patientAppointmentReminderList);
	
	PatientAppointmentReminder getPatientAppointmentReminderByMaxValueOfPatientAppointmentId();
	
	PatientAppointmentReminder getPatientAppointmentReminderByMaxValueId();
	
	void updateSmsStatusByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus);
	
	List<PersonAttribute> getPersonAttributeByPersonId(int id);
	
	PersonAttribute getConsentByPersonId(int id, int personAttTypeId);
	
	void updatePersonAttributet(int id, String status);
	
	List<PersonAttributeType> getPersonAttributeTypeById(int id);
	
	PersonName getPersonNameByPersonId(int id);
	
	Message sendSmsService(String toNumber, String textMessage);
	
	List<DataLoad> getDataLoaded();
	
	void insertIntoDataLoad();
	
	SmsLanguage getSmsByLocaleAndDay(String locale, Integer day);
	
	void updateSmsStatusOneDayByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus);
	
	void updateSmsStatusSevenDayByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus);
	
}
