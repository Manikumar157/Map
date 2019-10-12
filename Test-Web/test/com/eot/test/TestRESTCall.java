package com.eot.test;

import java.io.ByteArrayOutputStream;

import com.eot.banking.dto.CountryDTO;
import com.eot.banking.dto.CurrencyDTO;
import com.eot.banking.utils.RESTCall;

public class TestRESTCall {
	
	public static void main(String[] args) {
		
		//addCurrency();
		addCountry();
		
	}

	private static void addCurrency() {
		CurrencyDTO currencyDTO = new CurrencyDTO();
		
		currencyDTO.setCurrencyName("US Dollar");
		currencyDTO.setCurrencyCode("USD");
		currencyDTO.setCurrencyCodeNumeric(840);
		currencyDTO.setWalletCurrencyId(43);
		currencyDTO.setLanguage("English");
		
		com.eot.banking.utils.JSONAdaptor adaptor = new com.eot.banking.utils.JSONAdaptor() ;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		String payload = adaptor.toJSON(currencyDTO);

		buffer = RESTCall.sendPOSTRequest(payload, "http://localhost:8080/MobileChannelHandler/rest/service/currency");
		
		CurrencyDTO reponse = adaptor.fromJSON(buffer.toString(), CurrencyDTO.class);
		
		System.out.println("Response: "+buffer.toString());
	}
	
	private static void addCountry() {
		
		CountryDTO countryDTO = new CountryDTO();
		
		countryDTO.setFiCountry("Ivory Coast");
		countryDTO.setCountryCodeAlpha2("CI");
		countryDTO.setCountryCodeAlpha3("CIV");
		countryDTO.setCountryCodeNumeric(889);
		countryDTO.setIsdCode(225);
		countryDTO.setMobileNumberLength(9);
		countryDTO.setWalletCurrencyId(1);
		countryDTO.setWalletCountryId(1);
		countryDTO.setLanguage("English");
		
		com.eot.banking.utils.JSONAdaptor adaptor = new com.eot.banking.utils.JSONAdaptor() ;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		String payload = adaptor.toJSON(countryDTO);

		buffer = RESTCall.sendPOSTRequest(payload, "http://localhost:8080/MobileChannelHandler/rest/service/country");
		
		System.out.println("Response: "+buffer.toString());
	}
	


}
