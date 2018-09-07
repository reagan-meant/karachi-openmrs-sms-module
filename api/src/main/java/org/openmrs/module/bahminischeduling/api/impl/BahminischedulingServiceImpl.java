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

import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.bahminischeduling.AppointmentReminderLog;
import org.openmrs.module.bahminischeduling.ConceptName;
import org.openmrs.module.bahminischeduling.Item;
import org.openmrs.module.bahminischeduling.PatientAppointment;
import org.openmrs.module.bahminischeduling.PatientAppointmentReminder;
import org.openmrs.module.bahminischeduling.PersonAttribute;
import org.openmrs.module.bahminischeduling.PersonAttributeType;
import org.openmrs.module.bahminischeduling.PersonName;
import org.openmrs.module.bahminischeduling.api.BahminischedulingService;
import org.openmrs.module.bahminischeduling.api.dao.BahminischedulingDao;
import org.openmrs.module.bahminischeduling.twilio.IOutBoundService;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class BahminischedulingServiceImpl extends BaseOpenmrsService implements BahminischedulingService {
	
	BahminischedulingDao dao;
	
	UserService userService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(BahminischedulingDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Item getItemByUuid(String uuid) throws APIException {
		return dao.getItemByUuid(uuid);
	}
	
	@Override
	public Item saveItem(Item item) throws APIException {
		if (item.getOwner() == null) {
			item.setOwner(userService.getUser(1));
		}
		
		return dao.saveItem(item);
	}
	
	@Override
	public void insert(AppointmentReminderLog appointmentReminderLog) {
		// TODO Auto-generated method stub
		dao.insert(appointmentReminderLog);
	}
	
	@Override
	public String getAppointmentServiceName(int appointmentServiceId) {
		// TODO Auto-generated method stub
		return dao.getAppointmentServiceName(appointmentServiceId);
	}
	
	@Override
	public ConceptName getConceptNameByConceptId(int conceptId) {
		// TODO Auto-generated method stub
		return dao.getConceptNameByConceptId(conceptId);
	}
	
	@Override
	public String getDataLoadedCheck() {
		// TODO Auto-generated method stub
		return dao.getDataLoadedCheck();
	}
	
	@Override
	public void updateDataLoadedCheckYes() {
		// TODO Auto-generated method stub
		dao.updateDataLoadedCheckYes();
	}
	
	@Override
	public void insertDataLoadedCheckYes() {
		// TODO Auto-generated method stub
		dao.insertDataLoadedCheckYes();
	}
	
	@Override
	public List<PatientAppointment> getPatientAppointmentsByStatus(String status) {
		// TODO Auto-generated method stub
		return dao.getPatientAppointmentsByStatus(status);
	}
	
	@Override
	public List<PatientAppointment> getPatientAppointmentsGreaterThanPatientaAppointmentId(int patientAppointmentId,
	        String status) {
		// TODO Auto-generated method stub
		return dao.getPatientAppointmentsGreaterThanPatientaAppointmentId(patientAppointmentId, status);
	}
	
	@Override
	public void updatePatientAppointment(int id, String status) {
		// TODO Auto-generated method stub
		dao.updatePatientAppointment(id, status);
	}
	
	@Override
	public List<PatientAppointmentReminder> getPatientAppointmentReminderListBySmsStatus() {
		// TODO Auto-generated method stub
		return dao.getPatientAppointmentReminderListBySmsStatus();
	}
	
	@Override
	public void insert(List<PatientAppointmentReminder> patientAppointmentReminderList) {
		// TODO Auto-generated method stub
		dao.insert(patientAppointmentReminderList);
	}
	
	@Override
	public PatientAppointmentReminder getPatientAppointmentReminderByMaxValueOfPatientAppointmentId() {
		// TODO Auto-generated method stub
		return dao.getPatientAppointmentReminderByMaxValueOfPatientAppointmentId();
	}
	
	@Override
	public PatientAppointmentReminder getPatientAppointmentReminderByMaxValueId() {
		// TODO Auto-generated method stub
		return dao.getPatientAppointmentReminderByMaxValueId();
	}
	
	@Override
	public void updateSmsStatusByPatientAppointmentId(List<PatientAppointmentReminder> patientAppointmentReminderList,
	        String smsStatus) {
		// TODO Auto-generated method stub
		dao.updateSmsStatusByPatientAppointmentId(patientAppointmentReminderList, smsStatus);
	}
	
	@Override
	public List<PersonAttribute> getPersonAttributeByPersonId(int id) {
		// TODO Auto-generated method stub
		return dao.getPersonAttributeByPersonId(id);
	}
	
	@Override
	public PersonAttribute getConsentByPersonId(int id, int personAttTypeId) {
		// TODO Auto-generated method stub
		return dao.getConsentByPersonId(id, personAttTypeId);
	}
	
	@Override
	public void updatePersonAttributet(int id, String status) {
		// TODO Auto-generated method stub
		dao.updatePersonAttributet(id, status);
	}
	
	@Override
	public List<PersonAttributeType> getPersonAttributeTypeById(int id) {
		// TODO Auto-generated method stub
		return dao.getPersonAttributeTypeById(id);
	}
	
	@Override
	public PersonName getPersonNameByPersonId(int id) {
		// TODO Auto-generated method stub
		return dao.getPersonNameByPersonId(id);
	}
	
	public Message sendSmsService(String toNumber, String textMessage) {
		
		System.out.println("METHOD : sendSmsService start toNumber=" + toNumber + "----textMessage=" + textMessage);
		
		Message message = null;
		
		try {
			
			Twilio.init(Context.getAdministrationService().getGlobalProperty(IOutBoundService.ACCOUNT_SID), Context
			        .getAdministrationService().getGlobalProperty(IOutBoundService.AUTH_TOKEN));
			message = Message.creator(new PhoneNumber(toNumber),
			    new PhoneNumber(Context.getAdministrationService().getGlobalProperty(IOutBoundService.TWILIO_NUMBER)),
			    textMessage).create();
			
		}
		catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
		}
		
		return message;
	}
}
