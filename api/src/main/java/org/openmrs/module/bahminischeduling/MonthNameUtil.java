package org.openmrs.module.bahminischeduling;

public class MonthNameUtil {
	
	public static String getMonthNameUrdu(int number) {
		switch (number) {
			case 0:
				return "جنوری";
			case 1:
				return "فروری";
			case 2:
				return "مارچ";
			case 3:
				return "اپریل";
			case 4:
				return "مئی";
			case 5:
				return "جون";
			case 6:
				return "جولائی";
			case 7:
				return "اگست";
			case 8:
				return "ستمبر";
			case 9:
				return "اکتوبر";
			case 10:
				return "نومبر";
			case 11:
				return "دسمبر";
			default:
				return "";
		}
	}
}
