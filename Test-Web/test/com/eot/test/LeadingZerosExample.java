package com.eot.test;

public class LeadingZerosExample {
	public static void main(String[] args) {

		System.out.println(removeLeadingZeros("0000000000005000.00"));
	}

	public static String removeLeadingZeros(String str){
		char[] chars = str.toCharArray();
		int index = 0;
		for (; index < str.length(); index++) 
		{       
			if (chars[index] != '0') 
			{            
				break;       
			}    
		}    
		return (index == 0) ? str : str.substring(index);
	}
}