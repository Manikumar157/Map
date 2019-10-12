package com.eot.banking.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eot.dtos.sms.ResponseDTO;

public class CountryDTO implements Serializable {
 
    private Integer countryId;     
    
    @NotEmpty(message="NotEmpty.countryDTO.countries")
    private String[] country;  
    
    @NotNull(message="NotNull.countryDTO.currencyId")
    private Integer currencyId;   
    
    @NotEmpty(message="NotEmpty.countryDTO.countryCodeAlpha2")
    private String countryCodeAlpha2;
    
    @NotEmpty(message="NotEmpty.countryDTO.countryCodeAlpha3")
    private String countryCodeAlpha3;
    
    @NotNull(message="NotNull.countryDTO.countryCodeNumeric")
    private Integer countryCodeNumeric;
    
    @NotNull(message="NotNull.countryDTO.isdCode")
    private Integer isdCode;
    
    @NotNull(message="NotNull.countryDTO.mobileNumberLength")
    private Integer mobileNumberLength;
  
    private String  languageCode[];
   
    private int walletCountryId;
    
    private int walletCurrencyId;
    
    private String language;
    
    private String fiCountry;
    
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
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
	public String[] getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String[] languageCode) {
		this.languageCode = languageCode;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String[] getCountry() {
		return country;
	}
	public void setCountry(String[] country) {
		this.country = country;
	}
	public String getFiCountry() {
		return fiCountry;
	}
	public void setFiCountry(String fiCountry) {
		this.fiCountry = fiCountry;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
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
	public Integer getCountryCodeNumeric() {
		return countryCodeNumeric;
	}
	public void setCountryCodeNumeric(Integer countryCodeNumeric) {
		this.countryCodeNumeric = countryCodeNumeric;
	}
	public Integer getIsdCode() {
		return isdCode;
	}
	public void setIsdCode(Integer isdCode) {
		this.isdCode = isdCode;
	}
	public Integer getMobileNumberLength() {
		return mobileNumberLength;
	}
	public void setMobileNumberLength(Integer mobileNumberLength) {
		this.mobileNumberLength = mobileNumberLength;
	}



}
