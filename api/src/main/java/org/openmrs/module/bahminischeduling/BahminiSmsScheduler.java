package org.openmrs.module.bahminischeduling;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.log;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.impl.AdministrationServiceImpl;
import org.openmrs.module.bahminischeduling.api.impl.BahminischedulingServiceImpl;
import org.openmrs.module.bahminischeduling.template.LanguageTemplate;
import org.openmrs.module.bahminischeduling.twilio.OutBoundService;
import org.openmrs.module.bahminischeduling.utilities.CustomDate;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.openmrs.api.context.Context;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.twilio.rest.api.v2010.account.Message;

public class BahminiSmsScheduler extends AbstractTask {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/*@Autowired
	PatientAppointmentQuery patientAppointmentQuery;

	@Autowired
	PersonAttributeQuery personAttributeQuery ;*/
	
	@Autowired
	BahminischedulingServiceImpl service;
	
	/*	@Autowired
		AppointmentReminderLogQueries appointmentReminderLogQueries ;
			
		@Autowired
		PersonNameQueries personNameQueries ;
		
		@Autowired
		SendInformation sendInformation ;

		@Autowired
		OutBoundService outBoundService ;
		
		AppointmentReminderLog appointmentReminderLog ;

		@Autowired
		PatientAppointmentReminderQuery patientAppointmentReminderQuery ;
		
		@Autowired
		DataLoadQueries dataLoadQueries ;
		
		@Autowired
		AppointmentServiceQuery  appointmentServiceQuery ;*/
	
	/*@Autowired
	JdbcTemplate jdbcTemplate;*/
	
	@Autowired
	SendInformation sendInformation;
	
	@Autowired
	OutBoundService outBoundService;
	
	AppointmentReminderLog appointmentReminderLog;
	
	@Override
	public void execute() {
		log.info("***************** Inside scheduler *********************");
		super.startExecuting();
		System.out.println("***************** '" + Context.getAdministrationService().getGlobalProperty("twilioNumber")
		        + "' *********************");
		/*		log.info("METHOD  : sendSMSForAppointment start ********************************");
				//		 createTables();
				
				//List<PatientAppointment> patientAppointmentList = patientAppointmentQuery.getPatientAppointmentsByStatus("Scheduled");
				insertDataIntoPatientAppointmentReminer();
				List<PatientAppointmentReminder> patientAppointmentReminderList = service
				        .getPatientAppointmentReminderListBySmsStatus();
				List<PatientAppointment> patientAppointmentList = getPatientAppointmentDataFromReminders(patientAppointmentReminderList);
				log.info(" METHOD  : sendSMSForAppointment patientAppointmentReminderList" + patientAppointmentReminderList.size());
				try {
					while (patientAppointmentList.size() > 0) {
						log.info("METHOD  : sendSMSForAppointment 	while(patientAppointmentList.size() > 0 ) ="
						        + patientAppointmentList.size());
						
						List<PatientAppointment> specificPatientAppointmentList = new ArrayList<PatientAppointment>();
						PatientAppointment specificPatientAppointment = new PatientAppointment();
						
						specificPatientAppointment = patientAppointmentList.get(0);
						int currentPateintId = specificPatientAppointment.getPatient_id();
						specificPatientAppointmentList.add(patientAppointmentList.get(0));
						
						if (sendInformation == null) {
							sendInformation = new SendInformation();
						}
						
						sendInformation.setPatientId(specificPatientAppointmentList.get(0).getPatient_id());
						log.info("METHOD  : sendSMSForAppointment     patientAppointmentList.get(0)="
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
							log.info("METHOD  : sendSMSForAppointment inside condition true     : sendInformation.getConsent()="
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
								log.info("METHOD  : sendSMSForAppointment specificPatientAppointmentList.size="
								        + specificPatientAppointmentList.size());
								
								if (checkDateForOneDay(specificPatientAppointmentList.get(i).getStart_date_time()) == true) {
									specificPatientAppointmentListForSendSmsForOneDay.add(specificPatientAppointmentList.get(i));
									
									log.info("METHOD  : sendSMSForAppointment true for oneday ");
									
									patientAppointmentIdsForOneDay = patientAppointmentIdsForOneDay + ","
									        + specificPatientAppointmentList.get(i).getPatient_appointment_id();
									
									if (sendInformation.getPreferredLanguage().equals("Urdu")) {
										
										String tempMessage = LanguageTemplate.URDU;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneDay = messageOneDay + " " + tempMessage;
									} else if (sendInformation.getPreferredLanguage().equals("Sindhi")) {
										String tempMessage = LanguageTemplate.SINDHI;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneDay = messageOneDay + " " + tempMessage;
									} else if (sendInformation.getPreferredLanguage().equals("Pashtu")) {
										String tempMessage = LanguageTemplate.PASHTU;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneDay = messageOneDay + " " + tempMessage;
									} else if (sendInformation.getPreferredLanguage().equals("Bengali")) {
										String tempMessage = LanguageTemplate.BENGALI;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneDay = messageOneDay + " " + tempMessage;
									}
								} else if (checkDateForOneWeek(specificPatientAppointmentList.get(i).getStart_date_time()) == true) {
									specificPatientAppointmentListForSendSmsForOneWeek.add(specificPatientAppointmentList.get(i));
									log.info("METHOD  : sendSMSForAppointment true for oneweek  checkDateForOneWeek(specificPatientAppointmentList.get(i).getStart_date_time()) condition is true now inside it  ");
									patientAppointmentIdsForOneWeek = patientAppointmentIdsForOneWeek + ","
									        + specificPatientAppointmentList.get(i).getPatient_appointment_id();
									
									if (sendInformation.getPreferredLanguage().equals("Urdu")) {
										String tempMessage = LanguageTemplate.URDU;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneWeek = messageOneWeek + " " + tempMessage;
									} else if (sendInformation.getPreferredLanguage().equals("Sindhi")) {
										String tempMessage = LanguageTemplate.SINDHI;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneWeek = messageOneWeek + " " + tempMessage;
									} else if (sendInformation.getPreferredLanguage().equals("Pashtu")) {
										String tempMessage = LanguageTemplate.PASHTU;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneWeek = messageOneWeek + " " + tempMessage;
									} else if (sendInformation.getPreferredLanguage().equals("Bengali")) {
										String tempMessage = LanguageTemplate.BENGALI;
										tempMessage = tempMessage.replaceAll("DATE", specificPatientAppointmentList.get(i)
										        .getStart_date_time());
										tempMessage = tempMessage.replaceAll("APPOINTMENT_SERVICE", service
										        .getAppointmentServiceName(specificPatientAppointmentList.get(i)
										                .getAppointment_service_id()));
										messageOneWeek = messageOneWeek + " " + tempMessage;
									}
								}
							}
							sendInformation.setPatientAppointmentIdsForOneDay(patientAppointmentIdsForOneDay.trim());
							sendInformation.setPatientAppointmentIdsForOneWeek(patientAppointmentIdsForOneWeek.trim());
							sendInformation.setOneDayMessage(messageOneDay);
							sendInformation.setOneWeekMessage(messageOneWeek);
							
							log.info("METHOD  : sendSMSForAppointment sendInformation.getContactNumber()="
							        + sendInformation.getContactNumber());
							log.info("METHOD  : sendSMSForAppointment sendInformation.messageOneDay()="
							        + sendInformation.getOneDayMessage());
							log.info("METHOD  : sendSMSForAppointment sendInformation.getPatientAppointmentIdsForOneDay()="
							        + sendInformation.getPatientAppointmentIdsForOneDay());
							log.info("METHOD  : sendSMSForAppointment sendInformation.getPatientAppointmentIdsForOneWeek()="
							        + sendInformation.getPatientAppointmentIdsForOneWeek());
							sendSmsAndLogging(sendInformation, startMessageOneDay, startMessageOneWeek);
							sendInformation = null;
						}
					}
				}
				catch (Exception e) {
					// TODO: handle exception
					log.info("METHOD  : sendSMSForAppointment Exception Caught start ");
					
					e.printStackTrace();
					log.info(" METHOD  : sendSMSForAppointment  e.getMessage()=" + e.getMessage());
					log.info(" METHOD  : sendSMSForAppointment  e.getStackTrace()=" + e.getStackTrace());
					log.info(" METHOD  : sendSMSForAppointment  e.getLocalizedMessage()=" + e.getLocalizedMessage());
					log.info(" METHOD  : sendSMSForAppointment  e.getCause()=" + e.getCause());
					log.info(" METHOD  : sendSMSForAppointment  e.toString()=" + e.toString());
					log.info(" METHOD  : sendSMSForAppointment  e=" + e);
					
					System.exit(0);
				}
				log.info("METHOD  : sendSMSForAppointment End ***************************************** ");
			}
			
			private SendInformation getSendInformation(PatientAppointment patientAppointment, SendInformation sendInformation) {
				log.info(" METHOD  : getSendInformation start ");
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
					log.info("METHOD  : sendSMSForAppointment   personAttributeList.size()=" + personAttributeList.size());
					if (personAttributeList.get(j).getPerson_attribute_type_id() == 16) {
						contactNumber = personAttributeList.get(j).getValue();
						log.info("METHOD  : contactNumber=" + contactNumber);
						sendInformation.setContactNumber(contactNumber);
					} else if (personAttributeList.get(j).getPerson_attribute_type_id() == 18) {
						preferredLanguageConceptId = personAttributeList.get(j).getValue();
						log.info("METHOD  : preferredLanguageConceptId=" + preferredLanguageConceptId);
					} else if (personAttributeList.get(j).getPerson_attribute_type_id() == 17) {
						consentId = personAttributeList.get(j).getValue();
						sendInformation.setConsent(consentId);
						log.info("METHOD  : consent=" + consentId);
					}
					if (contactNumber != "" && preferredLanguageConceptId != "" && consentId != "") {
						break;
					}
				}
				ConceptName conceptNameForLanguage = service.getConceptNameByConceptId(Integer.parseInt(preferredLanguageConceptId
				        .trim()));
				preferredLanguageName = conceptNameForLanguage.getName();
				//	ConceptName conceptNameForConsent = conceptNameQueries.getConceptNameByConceptId(Integer.parseInt( consentId.trim()));
				//	consentValue= conceptNameForConsent.getName();
				sendInformation.setPreferredLanguage(preferredLanguageName);
				log.info(" preferredLanguageName=" + preferredLanguageName);
				log.info(" METHOD  : getSendInformation End ");
				return sendInformation;
			}
			
			private String setStartMessageInPreferredLanguage(SendInformation sendInformation) {
				log.info(" METHOD  : setStartMessageInPreferredLanguage Start ");
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
				log.info(" METHOD  : setStartMessageInPreferredLanguage End ");
				return tempMessage;
			}
			
			private void sendSmsAndLogging(SendInformation sendInformation, String startOneDayMessage, String startOneWeekMessage) {
				log.info(" METHOD  : sendSmsAndLogging start ");
				
				if (sendInformation.getOneDayMessage() != "" && sendInformation.getOneDayMessage() != null) {
					String smsStatus = "success";
					
					log.info(" METHOD  : sendSmsAndLogging  getOneDayMessage outbound service condition true");
					
					String contactNumberTemp = "";
					contactNumberTemp = sendInformation.getContactNumber().replaceAll("-", "");
					contactNumberTemp = contactNumberTemp.replaceFirst("0", "+92");
					
					sendInformation.setContactNumber(contactNumberTemp);
					
					Message messageResponse = outBoundService.sendSmsService(sendInformation.getContactNumber(),
					    sendInformation.getOneDayMessage());
					
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
					appointmentReminderLog.setPatient_appointment_ids_one_day(sendInformation.getPatientAppointmentIdsForOneDay());
					service.insert(appointmentReminderLog);
					
					List<Integer> patientAIdsAL = getPatientAppointmentIdsList(sendInformation.getPatientAppointmentIdsForOneDay());
					
					for (Integer pAId : patientAIdsAL) {
						log.info(" METHOD  : sendSmsAndLogging pAId=" + pAId);
						PatientAppointmentReminder patientAppointmentReminder = new PatientAppointmentReminder();
						patientAppointmentReminder.setPatient_appointment_id(pAId.intValue());
						patientAppointmentReminder.setStatus(smsStatus);
						patientAppointmentReminderList.add(patientAppointmentReminder);
						patientAppointmentReminder = null;
					}
					if (messageResponse.getErrorCode() == null) {
						service.updateSmsStatusByPatientAppointmentId(patientAppointmentReminderList,
						    smsStatus);
					}
				}
				log.info("sendInformation.getContactNumber()=" + sendInformation.getContactNumber());
				log.info("sendInformation.getOneWeekMessage()=" + sendInformation.getOneWeekMessage());
				
				if (sendInformation.getOneWeekMessage() != "" && sendInformation.getOneWeekMessage() != null) {
					String smsStatus = "success";
					log.info("METHOD  : sendSmsAndLogging getOneWeekMessage  outbound service condition true");
					sendInformation.setOneWeekMessage(startOneWeekMessage + " " + sendInformation.getOneWeekMessage());
					if (sendInformation.getContactNumber().contains("-")) {
						String contactNumberTemp = "";
						contactNumberTemp = sendInformation.getContactNumber().replaceAll("-", "");
						contactNumberTemp = contactNumberTemp.replaceFirst("0", "+92");
						sendInformation.setContactNumber(contactNumberTemp);
					}
					Message messageResponse = outBoundService.sendSmsService(sendInformation.getContactNumber(),
					    sendInformation.getOneWeekMessage());
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
							log.info("METHOD  : sendSmsAndLogging getOneWeekMessage messageResponse.getErrorCode()="
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
					appointmentReminderLog.setPatient_appointment_ids_one_week(sendInformation.getPatientAppointmentIdsForOneWeek());
					service.insert(appointmentReminderLog);
					
					List<Integer> patientAIdsAL = getPatientAppointmentIdsList(sendInformation.getPatientAppointmentIdsForOneWeek());
					log.info(" METHOD  : sendSmsAndLogging patientAIdsAL=" + patientAIdsAL);
					
					for (Integer pAId : patientAIdsAL) {
						log.info(" METHOD  : sendSmsAndLogging pAId=" + pAId);
						PatientAppointmentReminder patientAppointmentReminder = new PatientAppointmentReminder();
						patientAppointmentReminder.setPatient_appointment_id(pAId.intValue());
						patientAppointmentReminder.setStatus(smsStatus);
						patientAppointmentReminderList.add(patientAppointmentReminder);
						patientAppointmentReminder = null;
					}
					if (messageResponse.getErrorCode() == null) {
						service.updateSmsStatusByPatientAppointmentId(patientAppointmentReminderList,
						    smsStatus);
					}
				}
				log.info(" METHOD  : sendSmsAndLogging end ");*/
	}
	
	private Boolean checkDateForOneDay(String dateInStringP) {
		log.info(" METHOD  : checkDateForOneDay start ");
		log.info("METHOD  : checkDateForOneDay start dateInStringP=" + dateInStringP);
		
		Boolean condition = false;
		
		//	Integer appointmentMonth = appointmentDateInHashMap.get("MONTH");
		//	Integer currentMonth = currentDateInHashMap.get("MONTH");
		
		Date currentDate = CustomDate.getcurrentDateInFormat(CustomDate.DATE_FORMAT_YYYY_MM_DD);
		Date appointmentDate = CustomDate.convertDateInStringToDate(dateInStringP, CustomDate.DATE_FORMAT_YYYY_MM_DD);
		
		DateTime currentJodaDate = new DateTime(currentDate);
		DateTime appJodaDate = new DateTime(appointmentDate);
		
		System.out.println(currentJodaDate.toDateMidnight());
		System.out.println(appJodaDate.toDateMidnight());
		int daysBetween = Days.daysBetween(currentJodaDate.toDateMidnight(), appJodaDate.toDateMidnight()).getDays();
		
		log.info(" METHOD  : checkDateForOneDay  currentDate=" + currentDate);
		log.info(" METHOD  : checkDateForOneDay  appointmentDate=" + appointmentDate);
		long diff = appointmentDate.getTime() - currentDate.getTime();
		
		log.info(" Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		
		//		if(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) ==1 )
		if (daysBetween == 1) {
			log.info(" condition true day difference 1 ");
			condition = true;
			log.info("METHOD  : checkDateForOneDay  condition true");
		}
		
		log.info("METHOD  : checkDateForOneDay  End");
		return condition;
	}
	
	private Boolean checkDateForOneWeek(String dateInStringP) {
		log.info(" METHOD  : checkDateForOneWeek start ");
		log.info("METHOD  : checkDateForOneWeek start dateInStringP=" + dateInStringP);
		Boolean condition = false;
		
		//	Integer oneWeek  =    appointmentDateInHashMap.get("DAY") -  currentDateInHashMap.get("DAY")  ;  
		//	 log.info("METHOD  : checkDateForOneWeek  oneWeek.intValue()="+oneWeek.intValue());	
		
		Date currentDate = CustomDate.getcurrentDateInFormat(CustomDate.DATE_FORMAT_YYYY_MM_DD);
		Date appointmentDate = CustomDate.convertDateInStringToDate(dateInStringP, CustomDate.DATE_FORMAT_YYYY_MM_DD);
		
		DateTime currentJodaDate = new DateTime(currentDate);
		DateTime appJodaDate = new DateTime(appointmentDate);
		
		int daysBetween = Days.daysBetween(currentJodaDate.toDateMidnight(), appJodaDate.toDateMidnight()).getDays();
		
		log.info(" METHOD  : checkDateForOneWeek  currentDate=" + currentDate);
		log.info(" METHOD  : checkDateForOneWeek  appointmentDate=" + appointmentDate);
		long diff = appointmentDate.getTime() - currentDate.getTime();
		log.info(" Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		//		if(  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) ==7 )
		if (daysBetween == 7) {
			condition = true;
			log.info("METHOD  : checkDateForOneWeek  condition true");
		}
		log.info(" METHOD  : checkDateForOneWeek End ");
		return condition;
	}
	
	private void insertDataIntoPatientAppointmentReminer() {
		
		log.info(" METHOD :  insertDataIntoPatientAppointmentReminer start");
		
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
			log.info("METHOD  : insertDataIntoPatientAppointmentReminer  patientAppointmentReminder.getPatient_appointment_id()="
			        + patientAppointmentReminder.getPatient_appointment_id());
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
			log.info(" METHOD :  insertDataIntoPatientAppointmentReminer patientAppointmentList.size()="
			        + patientAppointmentList.size());
			for (int i = 0; i < patientAppointmentList.size(); i++) {
				
				log.info(" METHOD :  insertDataIntoPatientAppointmentReminer i=" + i);
				log.info(" METHOD :  insertDataIntoPatientAppointmentReminer patientAppointmentList.get(i).getPatient_appointment_id()="
				        + patientAppointmentList.get(i).getPatient_appointment_id());
				
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
				patientAppointmentReminderTemp.setSms_status("valueNotSet");
				/*TODO adding consent*/
				//    	 List<PersonAttribute> personAttributeList = personAttributeQuery.getPersonAttributeByPersonId(patientAppointment.getPatient_id());
				//    	 for(PersonAttribute pa: personAttributeList){
				//    		 if(pa.getPerson_attribute_type_id() == 17){
				//            	 patientAppointmentReminderTemp.setConsent(pa.getValue()); 
				//    		 }
				//    	 }      		
				patientAppointmentReminderTemp.setConsent(service.getConsentByPersonId(patientAppointment.getPatient_id(),
				    17).getValue());
				
				patientAppointmentReminderList.add(patientAppointmentReminderTemp);
				patientAppointmentReminderTemp = null;
				maxPatientAppointmentReminderId++;
			}
			service.insert(patientAppointmentReminderList);
			patientAppointmentReminderList = null;
		}
		log.info("METHOD : insertDataIntoPatientAppointmentReminer End ");
	}
	
	private List<PatientAppointment> getPatientAppointmentDataFromReminders(
	        List<PatientAppointmentReminder> patientAppointmentReminderList) {
		log.info("METHOD :  getPatientAppointmentDataFromReminders  start");
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
		
		log.info("METHOD : getPatientAppointmentDataFromReminders  END ");
		return patientAppointmentList;
	}
	
	// initial dataLoading
	private void insertDataInPatientAppointmentReminder(List<PatientAppointment> patientAppointmentList) {
		log.info("METHOD : insertDataInPatientAppointmentReminder  start ");
		List<PatientAppointmentReminder> patientAppointmentReminderList = new ArrayList<PatientAppointmentReminder>();
		for (int i = 0; i < patientAppointmentList.size(); i++) {
			log.info("METHOD : insertDataInPatientAppointmentReminder loop   ");
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
			patientAppointmentReminderTemp.setSms_status("valueNotSet");
			/*TODO adding consent*/
			patientAppointmentReminderTemp.setConsent(service.getConsentByPersonId(patientAppointment.getPatient_id(), 17)
			        .getValue());
			patientAppointmentReminderList.add(patientAppointmentReminderTemp);
			patientAppointmentReminderTemp = null;
		}
		service.insert(patientAppointmentReminderList);
		log.info("METHOD  insertDataInPatientAppointmentReminder End ");
	}
	
	public List<Integer> getPatientAppointmentIdsList(String patientAppointmentIds) {
		log.info("METHOD  getPatientAppointmentIdsList  start ");
		log.info("METHOD  getPatientAppointmentIdsList  patientAppointmentIds=" + patientAppointmentIds);
		List<Integer> patientAppointmentIdList = new ArrayList<Integer>();
		String[] splitedIds = patientAppointmentIds.split(",");
		for (String id : splitedIds) {
			log.info("METHOD  getPatientAppointmentIdsList " + id);
			if (id != null && !id.equals("") && !id.equals(",") && !id.equals("")) {
				log.info("METHOD  getPatientAppointmentIdsList id=" + id);
				patientAppointmentIdList.add(Integer.parseInt(id));
				log.info("METHOD  getPatientAppointmentIdsList Integer.parseInt(id)=" + Integer.parseInt(id));
			}
		}
		log.info("METHOD getPatientAppointmentIdsList  End ");
		return patientAppointmentIdList;
	}
	
	@Override
	public void shutdown() {
		log.info("shutting down scheduled task");
		this.stopExecuting();
	}
	
}
