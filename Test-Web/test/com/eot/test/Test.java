package com.eot.test;

public class Test {
	
	public static void main(String[] args) {
		
		/*Test test = new Test();
		String s1="+211 974104075";
		System.out.println(s1.trim());
		
		String isdCode = s1.trim().substring(1, 4);
		String mobileNumber = s1.trim().substring(s1.length()-9, s1.length());
		System.out.println(isdCode+mobileNumber);
		System.out.println(test.padSequence(2, 3));*/
	//	Test.formatDateWithHyphen("2019-05-27 09:29:31.0");
		int i=0;
		Integer i1=200;
		i=i1;
		System.out.println(i);
	}
	
	public static String formatDateWithHyphen(String date){
		//Incoming Date type 2019-05-27 09:29:31.0
		date=date.substring(0, 11);
		String invDt1[] = date.trim().split("-");
		String invDt = invDt1[2]+"-"+invDt1[1]+"-"+invDt1[0];
		System.out.println(invDt);
		return invDt;
	}
	
	private String padSequence( int sequence, int noOfDigits ) {

		String ret = sequence+1 + "";
		while( ret.length() < noOfDigits ) {

			ret = "0" + ret;
		}
		return ret;
	}

}
