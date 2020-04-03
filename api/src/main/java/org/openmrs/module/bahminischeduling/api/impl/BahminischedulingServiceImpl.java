/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.bahminischeduling.api.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.bahminischeduling.AppointmentReminderLog;
import org.openmrs.module.bahminischeduling.ConceptName;
import org.openmrs.module.bahminischeduling.DataLoad;
import org.openmrs.module.bahminischeduling.PatientAppointment;
import org.openmrs.module.bahminischeduling.PatientAppointmentReminder;
import org.openmrs.module.bahminischeduling.PersonAttribute;
import org.openmrs.module.bahminischeduling.PersonAttributeType;
import org.openmrs.module.bahminischeduling.PersonName;
import org.openmrs.module.bahminischeduling.SmsLanguage;
import org.openmrs.module.bahminischeduling.api.BahminischedulingService;
import org.openmrs.module.bahminischeduling.api.dao.BahminischedulingDao;
import org.openmrs.module.bahminischeduling.twilio.IOutBoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class BahminischedulingServiceImpl extends BaseOpenmrsService implements BahminischedulingService {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	BahminischedulingDao dao;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(BahminischedulingDao dao) {
		this.dao = dao;
	}
	
	@Override
	public void insert(AppointmentReminderLog appointmentReminderLog) {
		dao.insert(appointmentReminderLog);
	}
	
	@Override
	public String getAppointmentServiceName(int appointmentServiceId) {
		return dao.getAppointmentServiceName(appointmentServiceId);
	}
	
	@Override
	public ConceptName getConceptNameByConceptId(int conceptId) {
		return dao.getConceptNameByConceptId(conceptId);
	}
	
	@Override
	public String getDataLoadedCheck() {
		return dao.getDataLoadedCheck();
	}
	
	@Override
	public void updateDataLoadedCheckYes() {
		dao.updateDataLoadedCheckYes();
	}
	
	@Override
	public void updateDataLoadedCheckNo() {
		dao.updateDataLoadedCheckNo();
	}
	
	@Override
	public void insertDataLoadedCheckYes() {
		dao.insertDataLoadedCheckYes();
	}
	
	@Override
	public List<PatientAppointment> getPatientAppointmentsByStatus(String status) {
		return dao.getPatientAppointmentsByStatus(status);
	}
	
	@Override
	public List<PatientAppointment> getPatientAppointmentsGreaterThanPatientaAppointmentId(int patientAppointmentId,
	        String status) {
		return dao.getPatientAppointmentsGreaterThanPatientaAppointmentId(patientAppointmentId, status);
	}
	
	@Override
	public void updatePatientAppointment(int id, String status) {
		dao.updatePatientAppointment(id, status);
	}
	
	@Override
	public List<PatientAppointmentReminder> getPatientAppointmentReminderListBySmsStatus() {
		return dao.getPatientAppointmentReminderListBySmsStatus();
	}
	
	@Override
	public void insert(List<PatientAppointmentReminder> patientAppointmentReminderList) {
		dao.insert(patientAppointmentReminderList);
	}
	
	@Override
	public PatientAppointmentReminder getPatientAppointmentReminderByMaxValueOfPatientAppointmentId() {
		return dao.getPatientAppointmentReminderByMaxValueOfPatientAppointmentId();
	}
	
	@Override
	public PatientAppointmentReminder getPatientAppointmentReminderByMaxValueId() {
		return dao.getPatientAppointmentReminderByMaxValueId();
	}
	
	@Override
	public void updateSmsStatusByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus) {
		dao.updateSmsStatusByPatientAppointmentId(patientAppointmentReminderList, smsStatus);
	}
	
	@Override
	public List<PersonAttribute> getPersonAttributeByPersonId(int id) {
		return dao.getPersonAttributeByPersonId(id);
	}
	
	@Override
	public PersonAttribute getConsentByPersonId(int id, int personAttTypeId) {
		return dao.getConsentByPersonId(id, personAttTypeId);
	}
	
	@Override
	public void updatePersonAttributet(int id, String status) {
		dao.updatePersonAttributet(id, status);
	}
	
	@Override
	public List<PersonAttributeType> getPersonAttributeTypeById(int id) {
		return dao.getPersonAttributeTypeById(id);
	}
	
	@Override
	public PersonName getPersonNameByPersonId(int id) {
		return dao.getPersonNameByPersonId(id);
	}
	
	public Message sendSmsService(String toNumber, String textMessage) {
		Message message = null;
		
		try {
			
			Twilio.init(Context.getAdministrationService().getGlobalProperty(IOutBoundService.ACCOUNT_SID), Context
			        .getAdministrationService().getGlobalProperty(IOutBoundService.AUTH_TOKEN));
			message = Message.creator(new PhoneNumber(toNumber),
			    new PhoneNumber(Context.getAdministrationService().getGlobalProperty(IOutBoundService.TWILIO_NUMBER)),
			    textMessage).create();
			
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return message;
	}
	
	public List<DataLoad> getDataLoaded() {
		return dao.getDataLoaded();
	}
	
	public void insertIntoDataLoad() {
		dao.insertIntoDataLoad();
	}
	
	public SmsLanguage getSmsByLocaleAndDay(String locale, Integer day) {
		return dao.getSmsByLocaleAndDay(locale, day);
	}
	
	@Override
	public void updateSmsStatusOneDayByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus) {
		dao.updateSmsStatusOneDayByPatientAppointmentId(patientAppointmentReminderList, smsStatus);
	}
	
	@Override
	public void updateSmsStatusSevenDayByPatientAppointmentId(
	        List<PatientAppointmentReminder> patientAppointmentReminderList, String smsStatus) {
		dao.updateSmsStatusSevenDayByPatientAppointmentId(patientAppointmentReminderList, smsStatus);
	}
	
	@Override
	public List<PatientAppointment> getPatientAppointmentsScheduledMissed() {
		return dao.getPatientAppointmentsScheduledMissed();
	}
}
