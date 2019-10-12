package com.eot.test;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class BusinessPartnerCodeTest {
	
	public static void main(String[] args) throws ISOException {
		
	//	String businessPartnerCode = customerDao.getBusinessPartnerCode();
		String businessPartnerCode = "10";
		Integer numericCode=0;
		numericCode = businessPartnerCode.length()>=4 ? Integer.parseInt(businessPartnerCode)+1:Integer.parseInt(businessPartnerCode)+1000;
		businessPartnerCode=numericCode.toString();
		businessPartnerCode = ISOUtil.zeropad(businessPartnerCode, 4);
	//	businessPartner.setCode(businessPartnerCode);
		System.out.println("------------------------------------------");
		System.out.println(businessPartnerCode);
		System.out.println("------------------------------------------");
		
	}


}
