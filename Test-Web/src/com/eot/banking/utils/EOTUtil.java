package com.eot.banking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class EOTUtil {
	
	public static Date stringToDate(String date){
		Date formattedDate = null;
		try {
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			formattedDate = sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}
	
	public static String generateUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "") ;
	}
	
	public static String generateAppID(){
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0,16);
	}
	
	public static Integer generateLoginPin(){
		String loginPin =  (UUID.randomUUID().getLeastSignificantBits()+"").substring(1,5) ;
		return new Integer(loginPin);
	}
	
	public static Integer generateTransactionPin(){
		String txnPin = (UUID.randomUUID().getLeastSignificantBits()+"").substring(1,5) ;
		return new Integer(txnPin);
	}
	
	public static String generateWebUserPassword(){
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0,10);
	}
	
	public static String generateAccountNumber(Long accountNumSeq){
		final int[][] sumTable = {{0,2,4,6,8,1,3,5,7,9}, {0,1,2,3,4,5,6,7,8,9}};
        int sum = 0, flip = 0;
 
        String accountSeq = accountNumSeq+"";
        for (int i = accountSeq.length() - 1; i >= 0; i--) {
            sum += sumTable[flip++ & 0x1][Character.digit(accountSeq.charAt(i), 10)];
        }
        int modulusResult = (sum % 10);
        int checkDigit = ((modulusResult==0)? modulusResult: (10-modulusResult));
        return accountSeq+checkDigit;
	}
	
}
