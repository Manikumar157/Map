package com.eot.banking.dto;


public class CountryRequest extends ResponseDTO {
	
	 private String country;

    private String countryCodeAlpha2;

    private String countryCodeAlpha3;

    private int countryCodeNumeric;

    private int isdcode;

    private int mobileNumberLength;
    
    private int walletCountryId;
    
    private int walletCurrencyId;
    
    private String language;
    
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCodeAlpha2() {
		return countryCodeAlpha2;
	}

	public void setCountryCodeAlpha2(String countryCodeAlpha2) {
		this.countryCodeAlpha2 = countryCodeAlpha2;
	}

	public String getCountryCodeAlpha3() {
		return countryCodeAlpha3;
	}

	public void setCountryCodeAlpha3(String countryCodeAlpha3) {
		this.countryCodeAlpha3 = countryCodeAlpha3;
	}

	public int getCountryCodeNumeric() {
		return countryCodeNumeric;
	}

	public void setCountryCodeNumeric(int countryCodeNumeric) {
		this.countryCodeNumeric = countryCodeNumeric;
	}

	public int getIsdcode() {
		return isdcode;
	}

	public void setIsdcode(int isdcode) {
		this.isdcode = isdcode;
	}

	public int getMobileNumberLength() {
		return mobileNumberLength;
	}

	public void setMobileNumberLength(int mobileNumberLength) {
		this.mobileNumberLength = mobileNumberLength;
	}

	public int getWalletCountryId() {
		return walletCountryId;
	}

	public void setWalletCountryId(int walletCountryId) {
		this.walletCountryId = walletCountryId;
	}

	public int getWalletCurrencyId() {
		return walletCurrencyId;
	}

	public void setWalletCurrencyId(int walletCurrencyId) {
		this.walletCurrencyId = walletCurrencyId;
	}
    
}
