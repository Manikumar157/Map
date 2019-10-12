package com.eot.banking.dto;

import java.io.Serializable;

public class BankAccountDTO implements Serializable{

	private Long slNo;
	
	private String customerBankAccountNumber;
	
	private String accountAlias;
	
	private Integer status=2;
	
	private Long branchId;
	
	private Long customerId;
	
	private String accountHolderName;
	
	private Integer referenceType;
	
	private String branchName;
	
	
	
	
	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(Integer referenceType) {
		this.referenceType = referenceType;
	}

	public Long getSlNo() {
		return slNo;
	}

	public void setSlNo(Long slNo) {
		this.slNo = slNo;
	}

	public String getCustomerBankAccountNumber() {
		return customerBankAccountNumber;
	}

	public void setCustomerBankAccountNumber(String customerBankAccountNumber) {
		this.customerBankAccountNumber = customerBankAccountNumber;
	}

	public String getAccountAlias() {
		return accountAlias;
	}

	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

}
