package org.openmrs.module.bahminischeduling.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

public class CustomDate {
	
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	
	public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy hh:mm:ss";
	
	public static Date getcurrentDateInFormat(String format) {
		
		System.out.println("METHOD : getcurrentDateInFormat  ");
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date resultDate = new Date();
		sdf.format(resultDate);
		
		return resultDate;
		
	}
	
	public static Date convertDateInStringToDate(String date, String format) {
		
		System.out.println("METHOD : getDateInString  ");
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date resultDate = new Date();
		
		try {
			resultDate = sdf.parse(date);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultDate;
		
	}
	
	public static Date formatDateToSpecifiedFormat(Date date, String format) {
		
		System.out.println("METHOD : getDateInString  ");
		Date resultDate = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			
			resultDate = sdf.parse(sdf.format(date));
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultDate;
	}
	
	public static Map<String, Integer> getDateTimeComponent(String dateTime) {
		
		System.out.println("METHOD : getDateTimeComponent dateTime=" + dateTime);
		
		Map<String, Integer> dateTimeComponentMap = new HashMap<String, Integer>();
		
		String[] splitDateTime = dateTime.split(" ");
		String date = splitDateTime[0];
		String time = splitDateTime[1];
		
		System.out.println("METHOD : date=" + date);
		System.out.println("METHOD : time =" + time);
		
		String[] splitedDate = date.split("-");
		String[] splitedTime = time.split(":");
		
		dateTimeComponentMap.put("YEAR", Integer.parseInt(splitedDate[0]));
		dateTimeComponentMap.put("MONTH", Integer.parseInt(splitedDate[1]));
		dateTimeComponentMap.put("DAY", Integer.parseInt(splitedDate[2]));
		
		dateTimeComponentMap.put("HOUR", Integer.parseInt(splitedTime[0]));
		dateTimeComponentMap.put("MINUTE", Integer.parseInt(splitedTime[1]));
		
		System.out.println("dateTimeComponentMap-----" + dateTimeComponentMap);
		
		return dateTimeComponentMap;
		
	}
	
	public static String getCurrentDateTimeInString(String format) {
		
		System.out.println("METHOD : getCurrentDateTimeInString format=" + format);
		
		Date currentDate = new Date();
		String currentDateInString = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		currentDateInString = sdf.format(currentDate);
		
		return currentDateInString;
		
	}
	
	public static String getCurrentDateInString(String format) {
		
		System.out.println("METHOD : getCurrentDateTimeInString format=" + format);
		
		Date currentDate = new Date();
		String currentDateInString = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		currentDateInString = sdf.format(currentDate);
		String[] splitedDate = currentDateInString.split(" ");
		
		return splitedDate[0];
		
	}
	
	public static String getCurrentTimeInString(String format) {
		
		System.out.println("METHOD : getCurrentDateTimeInString format=" + format);
		
		Date currentDate = new Date();
		String currentTimeInString = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		currentTimeInString = sdf.format(currentDate);
		String[] splitedDateTime = currentTimeInString.split(" ");
		
		return splitedDateTime[1];
		
	}
	
}
