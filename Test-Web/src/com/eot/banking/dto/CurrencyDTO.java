package com.eot.banking.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class CurrencyDTO extends com.eot.banking.dto.ResponseDTO{
	
	    private Integer currencyId;
	    
	    @NotEmpty(message="NotEmpty.currencyDTO.currencyName")
	    private String currencyName;
	    
	    @NotEmpty(message="NotEmpty.currencyDTO.currencyCode")
	    private String currencyCode;
	    
	    @NotNull(message="NotNull.currencyDTO.currencyCodeNumeric")
	    private Integer currencyCodeNumeric;
	    
	    private int walletCurrencyId;
	    
	    private String language;
	    
		public Integer getCurrencyId() {
			return currencyId;
		}
		public void setCurrencyId(Integer currencyId) {
			this.currencyId = currencyId;
		}
		public String getCurrencyName() {
			return currencyName;
		}
		public void setCurrencyName(String currencyName) {
			this.currencyName = currencyName;
		}
		public String getCurrencyCode() {
			return currencyCode;
		}
		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}
		public Integer getCurrencyCodeNumeric() {
			return currencyCodeNumeric;
		}
		public void setCurrencyCodeNumeric(Integer currencyCodeNumeric) {
			this.currencyCodeNumeric = currencyCodeNumeric;
		}
		public int getWalletCurrencyId() {
			return walletCurrencyId;
		}
		public void setWalletCurrencyId(int walletCurrencyId) {
			this.walletCurrencyId = walletCurrencyId;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}

}
