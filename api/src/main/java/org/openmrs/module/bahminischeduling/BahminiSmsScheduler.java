package org.openmrs.module.bahminischeduling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.log;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.bahminischeduling.api.BahminischedulingService;
import org.openmrs.module.bahminischeduling.template.LanguageTemplate;
import org.openmrs.module.bahminischeduling.twilio.IOutBoundService;
import org.openmrs.module.bahminischeduling.twilio.OutBoundService;
import org.openmrs.module.bahminischeduling.utilities.CustomDate;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.springframework.beans.factory.annotation.Autowired;

import com.twilio.rest.api.v2010.account.Message;

public class BahminiSmsScheduler extends AbstractTask {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	BahminischedulingService service;
	
	@Autowired
	SendInformation sendInformation;
	
	@Autowired
	OutBoundService outBoundService;
	
	AppointmentReminderLog appointmentReminderLog;
	
	@Override
	public void execute() {
		service = BSContext.getBahminischedulingService();
		
		super.startExecuting();
		
		insertIntoDataLoad();
		
		//List<PatientAppointment> patientAppointmentList = patientAppointmentQuery.getPatientAppointmentsByStatus("Scheduled");
		insertDataIntoPatientAppointmentReminer();
		List<PatientAppointmentReminder> patientAppointmentReminderList = service
		        .getPatientAppointmentReminderListBySmsStatus();
		List<PatientAppointment> patientAppointmentList = getPatientAppointmentDataFromReminders(patientAppointmentReminderList);
		
		SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat desiredFormat = new SimpleDateFormat("dd-MMMM-yyyy hh:mm a");
		
		log.info("***************** Running SMS scheduler for " + patientAppointmentReminderList.size()
		        + " SMSes ***********");
		try {
			while (patientAppointmentList.size() > 0) {
				List<PatientAppointment> specificPatientAppointmentList = new ArrayList<PatientAppointment>();
				PatientAppointment specificPatientAppointment = new PatientAppointment();
				
				specificPatientAppointment = patientAppointmentList.get(0);
				int currentPateintId = specificPatientAppointment.getPatient_id();
				specificPatientAppointmentList.add(patientAppointmentList.get(0));
				
				if (sendInformation == null) {
					sendInformation = new SendInformation();
				}
				
				sendInformation.setPatientId(specificPatientAppointmentList.get(0).getPatient_id());
				
				log.debug("METHOD  : sendSMSForAppointment     patientAppointmentList.get(0)="
				        + patientAppointmentList.get(0).getPatient_id());
				patientAppointmentList.remove(0);
				
				// list patient appointment of specific patient id 
				for (int i = 0; i < patientAppointmentList.size(); i++) {
					if (currentPateintId == patientAppointmentList.get(i).getPatient_id()) {
						specificPatientAppointmentList.add(patientAppointmentList.get(i));
						patientAppointmentList.remove(i);
						i--;
					}
				}
				sendInformation = getSendInformation(specificPatientAppointmentList.get(0), sendInformation);
				if (sendInformation.getConsent().equals("1")) {
					
					log.debug("METHOD  : sendSMSForAppointment inside condition true     : sendInformation.getConsent()="
					        + sendInformation.getConsent());
					
					// Check for One Day one Week for specific patient
					List<PatientAppointment> specificPatientAppointmentListForSendSmsForOneDay = new ArrayList<PatientAppointment>();
					List<PatientAppointment> specificPatientAppointmentListForSendSmsForOneWeek = new ArrayList<PatientAppointment>();
					String messageOneDay = "";
					String messageOneWeek = "";
					String startMessageOneDay = setStartMessageInPreferredLanguage(sendInformation);
					String startMessageOneWeek = setStartMessageInPreferredLanguage(sendInformation);
					String patientAppointmentIdsForOneDay = "";
					String patientAppointmentIdsForOneWeek = "";
					for (int i = 0; i < specificPatientAppointmentList.size(); i++) {
						log.debug("METHOD  : sendSMSForAppointment specificPatientAppointmentList.size="
						        + specificPatientAppointmentList.size());
						
						if (checkDateForOneDay(specificPatientAppointmentList.get(i).getStart_date_time()) == true) {
							specificPatientAppointmentListForSendSmsForOneDay.add(specificPatientAppointmentList.get(i));
							
							patientAppointmentIdsForOneDay = patientAppointmentIdsForOneDay + ","
							        + specificPatientAppointmentList.get(i).getPatient_appointment_id();
							
							String tempMessage = service.getSmsByLocaleAndDay(sendInformation.getPreferredLanguage(), 1)
							        .getTextMessage();
							if (sendInformation.getPreferredLanguage().equalsIgnoreCase("urdu")) {
								String aptNameGp = Context.getAdministrationService().getGlobalProperty(
								    service.getAppointmentServiceName(specificPatientAppointmentList.get(i)
								            .getAppointment_service_id()));
								if (aptNameGp != null && !aptNameGp.isEmpty()) {
									try {
										tempMessage = tempMessage.replaceAll(
										    "سروس",
										    Context.getAdministrationService().getGlobalProperty(
										        service.getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id())));
									}
									catch (APIException e) {
										e.printStackTrace();
									}
								}
								
								String smsTitleGp = Context.getAdministrationService().getGlobalProperty("smsTitleUrdu");
								if (smsTitleGp != null && !smsTitleGp.isEmpty()) {
									try {
										tempMessage = tempMessage.replace("ٹائٹل", smsTitleGp);
									}
									catch (APIException e) {
										e.printStackTrace();
									}
								}
								tempMessage = tempMessage.concat("\n"
								        + desiredFormat.format(sourceFormat.parse(specificPatientAppointmentList.get(i)
								                .getStart_date_time().toString())));
							} else {
								try {
									tempMessage = tempMessage.replaceAll(
									    "DATE",
									    desiredFormat.format(sourceFormat.parse(specificPatientAppointmentList.get(i)
									            .getStart_date_time().toString())));
								}
								catch (Exception e) {
									tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
									        .getStart_date_time().toString());
								}
								String smsTitleGp = Context.getAdministrationService().getGlobalProperty("smsTitleEnglish");
								if (smsTitleGp != null && !smsTitleGp.isEmpty()) {
									try {
										tempMessage = tempMessage.replace("Title", smsTitleGp);
									}
									catch (APIException e) {
										e.printStackTrace();
									}
								}
								tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
								        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
								                .getAppointment_service_id()));
								
							}
							messageOneDay = messageOneDay + "\n\n" + tempMessage;
						} else if (checkDateForOneWeek(specificPatientAppointmentList.get(i).getStart_date_time()) == true) {
							specificPatientAppointmentListForSendSmsForOneWeek.add(specificPatientAppointmentList.get(i));
							log.debug("METHOD  : sendSMSForAppointment true for oneweek  checkDateForOneWeek(specificPatientAppointmentList.get(i).getStart_date_time()) condition is true now inside it  ");
							patientAppointmentIdsForOneWeek = patientAppointmentIdsForOneWeek + ","
							        + specificPatientAppointmentList.get(i).getPatient_appointment_id();
							
							String tempMessage = service.getSmsByLocaleAndDay(sendInformation.getPreferredLanguage(), 7)
							        .getTextMessage();
							
							if (sendInformation.getPreferredLanguage().equalsIgnoreCase("urdu")) {
								String aptNameGp = Context.getAdministrationService().getGlobalProperty(
								    service.getAppointmentServiceName(specificPatientAppointmentList.get(i)
								            .getAppointment_service_id()));
								if (aptNameGp != null && !aptNameGp.isEmpty()) {
									try {
										tempMessage = tempMessage.replaceAll(
										    "سروس",
										    Context.getAdministrationService().getGlobalProperty(
										        service.getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id())));
									}
									catch (APIException e) {
										e.printStackTrace();
									}
								}
								
								String smsTitleGp = Context.getAdministrationService().getGlobalProperty("smsTitleUrdu");
								if (smsTitleGp != null && !smsTitleGp.isEmpty()) {
									try {
										tempMessage = tempMessage.replace("ٹائٹل", smsTitleGp);
									}
									catch (APIException e) {
										e.printStackTrace();
									}
								}
								tempMessage = tempMessage.concat("\n"
								        + desiredFormat.format(sourceFormat.parse(specificPatientAppointmentList.get(i)
								                .getStart_date_time().toString())));
							} else {
								try {
									tempMessage = tempMessage.replaceAll(
									    "DATE",
									    desiredFormat.format(sourceFormat.parse(specificPatientAppointmentList.get(i)
									            .getStart_date_time().toString())));
								}
								catch (Exception e) {
									tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
									        .getStart_date_time().toString());
								}
								String smsTitleGp = Context.getAdministrationService().getGlobalProperty("smsTitleEnglish");
								if (smsTitleGp != null && !smsTitleGp.isEmpty()) {
									try {
										tempMessage = tempMessage.replace("Title", smsTitleGp);
									}
									catch (APIException e) {
										e.printStackTrace();
									}
								}
								tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
								        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
								                .getAppointment_service_id()));
							}
							messageOneWeek = messageOneWeek + "\n\n" + tempMessage;
						}
					}
					sendInformation.setPatientAppointmentIdsForOneDay(patientAppointmentIdsForOneDay.trim());
					sendInformation.setPatientAppointmentIdsForOneWeek(patientAppointmentIdsForOneWeek.trim());
					sendInformation.setOneDayMessage(messageOneDay);
					sendInformation.setOneWeekMessage(messageOneWeek);
					
					log.debug("METHOD  : sendSMSForAppointment sendInformation.getContactNumber()="
					        + sendInformation.getContactNumber());
					log.debug("METHOD  : sendSMSForAppointment sendInformation.messageOneDay()="
					        + sendInformation.getOneDayMessage());
					log.debug("METHOD  : sendSMSForAppointment sendInformation.getPatientAppointmentIdsForOneDay()="
					        + sendInformation.getPatientAppointmentIdsForOneDay());
					log.debug("METHOD  : sendSMSForAppointment sendInformation.getPatientAppointmentIdsForOneWeek()="
					        + sendInformation.getPatientAppointmentIdsForOneWeek());
					sendSmsAndLogging(sendInformation, startMessageOneDay, startMessageOneWeek);
					sendInformation = null;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			
			System.exit(0);
		}
		log.debug("METHOD  : sendSMSForAppointment End ***************************************** ");
	}
	
	private SendInformation getSendInformation(PatientAppointment patientAppointment, SendInformation sendInformation) {
		String contactNumber = "";
		String preferredLanguageConceptId = "";
		String consentId = "";
		String preferredLanguageName = "";
		String consentValue = "";
		List<PersonAttribute> personAttributeList = service.getPersonAttributeByPersonId(patientAppointment.getPatient_id());
		PersonName personName = new PersonName();
		personName = service.getPersonNameByPersonId(patientAppointment.getPatient_id());
		sendInformation.setName(personName.getGiven_name());
		
		for (int j = 0; j < personAttributeList.size(); j++) {
			if (personAttributeList.get(j).getPerson_attribute_type_id() == 16) {
				contactNumber = personAttributeList.get(j).getValue();
				sendInformation.setContactNumber(contactNumber);
			} else if (personAttributeList.get(j).getPerson_attribute_type_id() == 18) {
				preferredLanguageConceptId = personAttributeList.get(j).getValue();
			} else if (personAttributeList.get(j).getPerson_attribute_type_id() == 17) {
				consentId = personAttributeList.get(j).getValue();
				sendInformation.setConsent(consentId);
			}
			if (contactNumber != "" && preferredLanguageConceptId != "" && consentId != "") {
				break;
			}
		}
		try {
			if (preferredLanguageConceptId != null && !preferredLanguageConceptId.isEmpty()) {
				ConceptName conceptNameForLanguage = service.getConceptNameByConceptId(Integer
				        .parseInt(preferredLanguageConceptId.trim()));
				preferredLanguageName = conceptNameForLanguage.getName();
			} else {
				preferredLanguageName = "Urdu";
			}
		}
		catch (Exception e) {
			//			ConceptName conceptNameForLanguage = service.getConceptNameByConceptId(164);
			preferredLanguageName = "Urdu";
		}
		sendInformation.setPreferredLanguage(preferredLanguageName);
		return sendInformation;
	}
	
	private String setStartMessageInPreferredLanguage(SendInformation sendInformation) {
		String tempMessage = "";
		
		if (sendInformation.getPreferredLanguage().equals("Urdu")) {
			tempMessage = LanguageTemplate.START_MESSAGE_URDU;
		} else if (sendInformation.getPreferredLanguage().equals("Sindhi")) {
			tempMessage = LanguageTemplate.START_MESSAGE_SINDHI;
		} else if (sendInformation.getPreferredLanguage().equals("Pashtu")) {
			tempMessage = LanguageTemplate.START_MESSAGE_PASHTU;
		} else if (sendInformation.getPreferredLanguage().equals("Bengali")) {
			tempMessage = LanguageTemplate.START_MESSAGE_BENGALI;
		}
		return tempMessage;
	}
	
	private void sendSmsAndLogging(SendInformation sendInformation, String startOneDayMessage, String startOneWeekMessage) {
		if (sendInformation.getOneDayMessage() != "" && sendInformation.getOneDayMessage() != null) {
			String smsStatus = "success";
			
			String contactNumberTemp = "";
			contactNumberTemp = sendInformation.getContactNumber().replaceAll("-", "");
			contactNumberTemp = contactNumberTemp.replaceFirst("0",
			    Context.getAdministrationService().getGlobalProperty(IOutBoundService.COUNTRY_CODE));
			
			sendInformation.setContactNumber(contactNumberTemp);
			
			Message messageResponse = service.sendSmsService(sendInformation.getContactNumber(),
			    sendInformation.getOneDayMessage());
			
			if (messageResponse != null) {
				List<PatientAppointmentReminder> patientAppointmentReminderList = new ArrayList<PatientAppointmentReminder>();
				
				sendInformation.setOneDayMessage(startOneDayMessage + " " + sendInformation.getOneDayMessage());
				
				appointmentReminderLog = new AppointmentReminderLog();
				appointmentReminderLog.setMessage(sendInformation.getOneDayMessage());
				
				if (messageResponse != null) {
					if (messageResponse.getSid() != null) {
						appointmentReminderLog.setMessagesid(messageResponse.getSid());
					}
					if (messageResponse.getMessagingServiceSid() != null) {
						appointmentReminderLog.setMessagingServiceSid(messageResponse.getMessagingServiceSid());
					}
					if (messageResponse.getErrorCode() != null) {
						appointmentReminderLog.setError_code(messageResponse.getErrorCode());
						smsStatus = messageResponse.getErrorCode().toString();
					}
					
					if (messageResponse.getErrorMessage() != null) {
						appointmentReminderLog.setErrorMessage(messageResponse.getErrorMessage());
					}
				}
				appointmentReminderLog.setContactNumber(sendInformation.getContactNumber());
				appointmentReminderLog.setPatient_id(sendInformation.getPatientId());
				appointmentReminderLog.setSent_on(CustomDate.getCurrentDateInString(CustomDate.DATE_FORMAT_YYYY_MM_DD));
				appointmentReminderLog.setPatient_appointment_ids_one_day(sendInformation
				        .getPatientAppointmentIdsForOneDay());
				service.insert(appointmentReminderLog);
				
				List<Integer> patientAIdsAL = getPatientAppointmentIdsList(sendInformation
				        .getPatientAppointmentIdsForOneDay());
				
				for (Integer pAId : patientAIdsAL) {
					PatientAppointmentReminder patientAppointmentReminder = new PatientAppointmentReminder();
					patientAppointmentReminder.setPatient_appointment_id(pAId.intValue());
					patientAppointmentReminder.setStatus(smsStatus);
					patientAppointmentReminderList.add(patientAppointmentReminder);
					patientAppointmentReminder = null;
				}
				if (messageResponse.getErrorCode() == null) {
					service.updateSmsStatusOneDayByPatientAppointmentId(patientAppointmentReminderList, smsStatus);
				}
			}
		}
		
		if (sendInformation.getOneWeekMessage() != "" && sendInformation.getOneWeekMessage() != null) {
			String smsStatus = "success";
			sendInformation.setOneWeekMessage(startOneWeekMessage + " " + sendInformation.getOneWeekMessage());
			if (sendInformation.getContactNumber().contains("-")) {
				String contactNumberTemp = "";
				contactNumberTemp = sendInformation.getContactNumber().replaceAll("-", "");
				contactNumberTemp = contactNumberTemp.replaceFirst("0", Context.getAdministrationService()
				        .getGlobalProperty(IOutBoundService.COUNTRY_CODE));
				sendInformation.setContactNumber(contactNumberTemp);
			}
			Message messageResponse = service.sendSmsService(sendInformation.getContactNumber(),
			    sendInformation.getOneWeekMessage());
			
			if (messageResponse != null) {
				List<PatientAppointmentReminder> patientAppointmentReminderList = new ArrayList<PatientAppointmentReminder>();
				appointmentReminderLog = new AppointmentReminderLog();
				appointmentReminderLog.setMessage(sendInformation.getOneWeekMessage());
				
				if (messageResponse != null) {
					if (messageResponse.getSid() != null) {
						appointmentReminderLog.setMessagesid(messageResponse.getSid());
					}
					if (messageResponse.getMessagingServiceSid() != null) {
						appointmentReminderLog.setMessagingServiceSid(messageResponse.getMessagingServiceSid());
					}
					if (messageResponse.getErrorCode() != null) {
						log.debug("METHOD  : sendSmsAndLogging getOneWeekMessage messageResponse.getErrorCode()="
						        + messageResponse.getErrorCode());
						appointmentReminderLog.setError_code(messageResponse.getErrorCode());
						smsStatus = messageResponse.getErrorCode().toString();
					}
					if (messageResponse.getErrorMessage() != null) {
						appointmentReminderLog.setErrorMessage(messageResponse.getErrorMessage());
					}
				}
				appointmentReminderLog.setContactNumber(sendInformation.getContactNumber());
				appointmentReminderLog.setPatient_id(sendInformation.getPatientId());
				appointmentReminderLog.setSent_on(CustomDate
				        .getCurrentDateTimeInString(CustomDate.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
				appointmentReminderLog.setPatient_appointment_ids_one_week(sendInformation
				        .getPatientAppointmentIdsForOneWeek());
				service.insert(appointmentReminderLog);
				
				List<Integer> patientAIdsAL = getPatientAppointmentIdsList(sendInformation
				        .getPatientAppointmentIdsForOneWeek());
				
				for (Integer pAId : patientAIdsAL) {
					PatientAppointmentReminder patientAppointmentReminder = new PatientAppointmentReminder();
					patientAppointmentReminder.setPatient_appointment_id(pAId.intValue());
					patientAppointmentReminder.setStatus(smsStatus);
					patientAppointmentReminderList.add(patientAppointmentReminder);
					patientAppointmentReminder = null;
				}
				if (messageResponse.getErrorCode() == null) {
					service.updateSmsStatusSevenDayByPatientAppointmentId(patientAppointmentReminderList, smsStatus);
				}
			}
		}
	}
	
	private Boolean checkDateForOneDay(String dateInStringP) {
		Boolean condition = false;
		
		Date currentDate = CustomDate.getcurrentDateInFormat(CustomDate.DATE_FORMAT_YYYY_MM_DD);
		Date appointmentDate = CustomDate.convertDateInStringToDate(dateInStringP, CustomDate.DATE_FORMAT_YYYY_MM_DD);
		
		DateTime currentJodaDate = new DateTime(currentDate);
		DateTime appJodaDate = new DateTime(appointmentDate);
		
		System.out.println(currentJodaDate.toDateMidnight());
		System.out.println(appJodaDate.toDateMidnight());
		int daysBetween = Days.daysBetween(currentJodaDate.toDateMidnight(), appJodaDate.toDateMidnight()).getDays();
		
		long diff = appointmentDate.getTime() - currentDate.getTime();
		
		log.info(" Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		
		if (daysBetween == 1) {
			condition = true;
		}
		
		return condition;
	}
	
	private Boolean checkDateForOneWeek(String dateInStringP) {
		Boolean condition = false;
		
		Date currentDate = CustomDate.getcurrentDateInFormat(CustomDate.DATE_FORMAT_YYYY_MM_DD);
		Date appointmentDate = CustomDate.convertDateInStringToDate(dateInStringP, CustomDate.DATE_FORMAT_YYYY_MM_DD);
		
		DateTime currentJodaDate = new DateTime(currentDate);
		DateTime appJodaDate = new DateTime(appointmentDate);
		
		int daysBetween = Days.daysBetween(currentJodaDate.toDateMidnight(), appJodaDate.toDateMidnight()).getDays();
		
		long diff = appointmentDate.getTime() - currentDate.getTime();
		if (daysBetween == 7) {
			condition = true;
		}
		return condition;
	}
	
	private void insertDataIntoPatientAppointmentReminer() {
		// initial data check 	
		String dataLoadedCheck = "";
		dataLoadedCheck = service.getDataLoadedCheck();
		
		if (dataLoadedCheck.equals("NO")) {
			List<PatientAppointment> patientAppointmentList = service.getPatientAppointmentsByStatus("Scheduled");
			
			//  save into patient appointment reminder initial Data	
			insertDataInPatientAppointmentReminder(patientAppointmentList);
			service.updateDataLoadedCheckYes();
		}
		
		// Getting max value of patient appointment id  	
		PatientAppointmentReminder patientAppointmentReminder = service
		        .getPatientAppointmentReminderByMaxValueOfPatientAppointmentId();
		// if data  exist 
		if (patientAppointmentReminder.getPatient_appointment_id() != 0) {
			List<PatientAppointment> patientAppointmentList = service
			        .getPatientAppointmentsGreaterThanPatientaAppointmentId(
			            patientAppointmentReminder.getPatient_appointment_id(), "Scheduled");
			List<PatientAppointmentReminder> patientAppointmentReminderList = new ArrayList<PatientAppointmentReminder>();
			
			int maxPatientAppointmentReminderId = 1;
			PatientAppointmentReminder maxPatientAppointmentReminder = service.getPatientAppointmentReminderByMaxValueId();
			if (maxPatientAppointmentReminder.getId() != 0) {
				maxPatientAppointmentReminderId = maxPatientAppointmentReminder.getId();
				maxPatientAppointmentReminderId++;
			}
			
			// getting data from patient appointment
			for (int i = 0; i < patientAppointmentList.size(); i++) {
				PatientAppointmentReminder patientAppointmentReminderTemp = new PatientAppointmentReminder();
				PatientAppointment patientAppointment = patientAppointmentList.get(i);
				
				patientAppointmentReminderTemp.setId(maxPatientAppointmentReminderId);
				patientAppointmentReminderTemp.setPatient_appointment_id(patientAppointment.getPatient_appointment_id());
				patientAppointmentReminderTemp.setPatient_id(patientAppointment.getPatient_id());
				patientAppointmentReminderTemp.setStart_date_time(patientAppointment.getStart_date_time());
				patientAppointmentReminderTemp.setEnd_date_time(patientAppointment.getEnd_date_time());
				patientAppointmentReminderTemp.setAppointment_service_id(patientAppointment.getAppointment_service_id());
				patientAppointmentReminderTemp.setAppointment_service_type_id(patientAppointment
				        .getAppointment_service_type_id());
				patientAppointmentReminderTemp.setStatus(patientAppointment.getStatus());
				patientAppointmentReminderTemp.setSmsStatusOneDay("valueNotSet");
				patientAppointmentReminderTemp.setSmsStatusSevenDay("valueNotSet");
				
				// SAVED CONSENT TO DB
				patientAppointmentReminderTemp.setConsent(service.getConsentByPersonId(patientAppointment.getPatient_id(),
				    17).getValue());
				
				patientAppointmentReminderList.add(patientAppointmentReminderTemp);
				patientAppointmentReminderTemp = null;
				maxPatientAppointmentReminderId++;
			}
			service.insert(patientAppointmentReminderList);
			patientAppointmentReminderList = null;
		} else {
			service.updateDataLoadedCheckNo();
		}
	}
	
	private List<PatientAppointment> getPatientAppointmentDataFromReminders(
	        List<PatientAppointmentReminder> patientAppointmentReminderList) {
		List<PatientAppointment> patientAppointmentList = new ArrayList<PatientAppointment>();
		for (int i = 0; i < patientAppointmentReminderList.size(); i++) {
			PatientAppointment patientAppointment = new PatientAppointment();
			patientAppointment.setPatient_appointment_id(patientAppointmentReminderList.get(i).getPatient_appointment_id());
			patientAppointment.setPatient_id(patientAppointmentReminderList.get(i).getPatient_id());
			patientAppointment.setStart_date_time(patientAppointmentReminderList.get(i).getStart_date_time());
			patientAppointment.setEnd_date_time(patientAppointmentReminderList.get(i).getEnd_date_time());
			patientAppointment.setAppointment_service_id(patientAppointmentReminderList.get(i).getAppointment_service_id());
			patientAppointment.setAppointment_service_type_id(patientAppointmentReminderList.get(i)
			        .getAppointment_service_type_id());
			patientAppointment.setStatus(patientAppointmentReminderList.get(i).getStatus());
			patientAppointmentList.add(patientAppointment);
			patientAppointment = null;
		}
		
		return patientAppointmentList;
	}
	
	// initial dataLoading
	private void insertDataInPatientAppointmentReminder(List<PatientAppointment> patientAppointmentList) {
		List<PatientAppointmentReminder> patientAppointmentReminderList = new ArrayList<PatientAppointmentReminder>();
		for (int i = 0; i < patientAppointmentList.size(); i++) {
			PatientAppointmentReminder patientAppointmentReminderTemp = new PatientAppointmentReminder();
			PatientAppointment patientAppointment = patientAppointmentList.get(i);
			patientAppointmentReminderTemp.setId(i + 1);
			patientAppointmentReminderTemp.setPatient_appointment_id(patientAppointment.getPatient_appointment_id());
			patientAppointmentReminderTemp.setPatient_id(patientAppointment.getPatient_id());
			patientAppointmentReminderTemp.setStart_date_time(patientAppointment.getStart_date_time());
			patientAppointmentReminderTemp.setEnd_date_time(patientAppointment.getEnd_date_time());
			patientAppointmentReminderTemp.setAppointment_service_id(patientAppointment.getAppointment_service_id());
			patientAppointmentReminderTemp.setAppointment_service_type_id(patientAppointment
			        .getAppointment_service_type_id());
			patientAppointmentReminderTemp.setStatus(patientAppointment.getStatus());
			patientAppointmentReminderTemp.setSmsStatusOneDay("valueNotSet");
			patientAppointmentReminderTemp.setSmsStatusSevenDay("valueNotSet");
			/*TODO adding consent*/
			patientAppointmentReminderTemp.setConsent(service.getConsentByPersonId(patientAppointment.getPatient_id(), 17)
			        .getValue());
			patientAppointmentReminderList.add(patientAppointmentReminderTemp);
			patientAppointmentReminderTemp = null;
		}
		service.insert(patientAppointmentReminderList);
	}
	
	public List<Integer> getPatientAppointmentIdsList(String patientAppointmentIds) {
		List<Integer> patientAppointmentIdList = new ArrayList<Integer>();
		String[] splitedIds = patientAppointmentIds.split(",");
		for (String id : splitedIds) {
			if (id != null && !id.equals("") && !id.equals(",") && !id.equals("")) {
				patientAppointmentIdList.add(Integer.parseInt(id));
			}
		}
		return patientAppointmentIdList;
	}
	
	public void insertIntoDataLoad() {
		if (service.getDataLoaded().size() == 0) {
			service.insertIntoDataLoad();
		}
	}
	
	@Override
	public void shutdown() {
		this.stopExecuting();
	}
	
}
