package org.openmrs.module.bahminischeduling;

public class MonthNameUtil {
	
	public static String getMonthNameUrdu(int number) {
		switch (number) {
			case 1:
				return "جنوری";
			case 2:
				return "فروری";
			case 3:
				return "مارچ";
			case 4:
				return "اپریل";
			case 5:
				return "مئی";
			case 6:
				return "جون";
			case 7:
				return "جولائی";
			case 8:
				return "اگست";
			case 9:
				return "ستمبر";
			case 10:
				return "اکتوبر";
			case 11:
				return "نومبر";
			case 12:
				return "دسمبر";
			default:
				return "";
		}
	}
}
