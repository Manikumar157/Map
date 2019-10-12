package com.eot.banking.dto;

import java.util.List;

public class BalanceDTO {
	
	private String gimMobileNumber;
	
	private List<String> accountList;
	
	private String account;
	
	private String customerName;
	
	private String availableBalance;
	
	private String description;

	public String getGimMobileNumber() {
		return gimMobileNumber;
	}

	public void setGimMobileNumber(String gimMobileNumber) {
		this.gimMobileNumber = gimMobileNumber;
	}

	public List<String> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<String> accountList) {
		this.accountList = accountList;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
