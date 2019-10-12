package com.eot.banking.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String formatDateAndTime(Date date){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sd.format(date);
	}

	public static String formatDate(Date date){

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		return sd.format(date);
	}

	public static Date stringToDateTime(String date){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		try {
			return sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date stringToDate(String date){
		Date formattedDate = null;
		try {
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			formattedDate = sd.parse(date);
		} catch (ParseException e) {
			System.out.println("<<<<error >>>>"+e.getMessage());
			e.printStackTrace();
		}
		return formattedDate;
	}
	
	
	public static String formatDate(String date){
		/* Function to change the date format from mm-dd-yy to yy-mm-dd*/
		String invDt1[] = date.toString().split("-");
		String invDt = invDt1[2]+"-"+invDt1[1]+"-"+invDt1[0];
		System.out.println("<<<<date>>>> "+invDt);
		return invDt;
	}
	
	public static String formatDateWithSlash(String date,int HH, int mm, int ss){
		/* Function to change the date format from mm-dd-yy to yy-mm-dd*/
		String invDt1[] = date.toString().split("/");
		String invDt = invDt1[2]+"-"+invDt1[1]+"-"+invDt1[0];
		//System.out.println("<<<<date>>>> "+invDt+" "+HH+":"+mm+":"+ss);
		return invDt+" "+HH+":"+mm+":"+ss;
	}

	public static String formatDateToStr(Date date){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(date);
	}
	public static String formatDateToStr1(Date date){
		SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
		return sd.format(date);
	}

	public static String dateAndTime(Date date){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sd.format(date);
	}
	

	public static Date sqlFormatDate(String date){
		java.sql.Date sqltDate=null;
		try {
			DateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date parsedUtilDate = formater.parse(date);
			sqltDate= new java.sql.Date(parsedUtilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqltDate;

	}

	public static String txnDate(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		return sd.format(date);
	}
	
	public static Date strToSqlDate(String date){
		java.sql.Date sqltDate=null;
		try {
			DateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date parsedUtilDate = formater.parse(date);
			sqltDate= new java.sql.Date(parsedUtilDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqltDate;

	}
	
	public static Date dateAndTime(String date){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HHmm");
		try {
			return sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Date dateAndTime1(String date){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date mySQLdateAndTime(String date){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
                 
        return cal.getTime();
    }
	public static String dateToString(Date date){
		String  formatDate1 = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			formatDate1 = formatter.format(date);
		} catch (Exception e) {
			System.out.println("<<<<error >>>>" + e.getMessage());
			e.printStackTrace();
		}
		return formatDate1;
	}
	public static Date stringToDatewithoutTime(String date){
		Date formattedDate = null;

		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			formattedDate = sd.parse(date);
		} catch (ParseException e) {
			System.out.println("<<<<error >>>>" + e.getMessage());
			e.printStackTrace();
		}

		return formattedDate;
	}
	public static Date formatDateWithoutTime(Date date){
		Date parsedDate =null;
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			String formateDate= sd.format(date);
			SimpleDateFormat formatedDate = new SimpleDateFormat("yyyy-MM-dd");
			parsedDate =formatedDate.parse(formateDate);
		} catch (ParseException e) {
			System.out.println("<<<<error >>>>" + e.getMessage());
			e.printStackTrace();
		}
		return parsedDate;
	}
	public static String StringDateToString(String date){
		String stringDate=null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsedUtilDate = formater.parse(date);
			stringDate = formater.format(parsedUtilDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return stringDate;
}
	
	
	public static String formatDateWithHyphen(String date){
		//Incoming Date type 2019-05-27 09:29:31.0
		date=date.substring(0, 11);
		String invDt1[] = date.trim().split("-");
		String invDt = invDt1[2]+"-"+invDt1[1]+"-"+invDt1[0];
		//System.out.println(invDt);
		return invDt;
	}
	
public static String formatDate_DD_MM_YYYY(Date date){
		
		SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
		return sd.format(date);
	}
/*public static void main(String[] args) {
	System.out.println(addDays(new Date(), -90));
}*/
}