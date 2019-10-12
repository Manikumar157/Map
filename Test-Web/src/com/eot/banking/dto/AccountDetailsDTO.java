package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.eot.entity.CustomerAccount;

public class AccountDetailsDTO implements BaseDTO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8856553534752131377L;

	@NotEmpty
	private String mobileNumber;

	private List<CustomerAccount> accountList;

	private List<String> cardList;

	private String customerName;

	private String customerId;

	private int transactionType;

	private Long amount;
	private String idType;

	private String accountNumber;
	private String accountAlias;
	
	private String fromAccountNumber;
	private String fromAccountAlias;
	private String toAccountNumber;
	private String toAccountAlias;
	
	
	

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getFromAccountAlias() {
		return fromAccountAlias;
	}

	public void setFromAccountAlias(String fromAccountAlias) {
		this.fromAccountAlias = fromAccountAlias;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getToAccountAlias() {
		return toAccountAlias;
	}

	public void setToAccountAlias(String toAccountAlias) {
		this.toAccountAlias = toAccountAlias;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountAlias() {
		return accountAlias;
	}

	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	private String idNumber;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date expiryDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date issueDate;

	/** The businessPartner code. */
	private String businessPartnerCode;

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	private String placeOfIssue;

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public List<String> getCardList() {
		return cardList;
	}

	public void setCardList(List<String> cardList) {
		this.cardList = cardList;
	}

	public List<CustomerAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<CustomerAccount> accountList) {
		this.accountList = accountList;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getBusinessPartnerCode() {
		return businessPartnerCode;
	}

	public void setBusinessPartnerCode(String businessPartnerCode) {
		this.businessPartnerCode = businessPartnerCode;
	}

}
