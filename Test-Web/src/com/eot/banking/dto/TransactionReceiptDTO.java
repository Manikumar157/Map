package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.eot.dtos.basic.Transaction;

public class TransactionReceiptDTO implements BaseDTO,Serializable {
	
	private String customerId;
	
	private String customerName;
	
	@NotEmpty
	private String mobileNumber;
	
	private int transactionType;
	
	private String accountNumber;
	
	private String accountAlias;
	
	private Long amount ;
	
	private Date fromDate ;
	
	private Date toDate ;
	
	private int numOfTxns ;
	
	private String authCode ;
	
	private String balance ;
	
	private String description ;
	
	List<Transaction> txnList ;
	
	List<TxnStatementDTO> txnStmList;
	
	/**
	 * This field is for jasper lable
	 */
	private String balanceLable;
	
	/** The businessPartner code. */
	private String businessPartnerCode;
	
	public String getBalanceLable() {
		return balanceLable;
	}

	public void setBalanceLable(String balanceLable) {
		this.balanceLable = balanceLable;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
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

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getNumOfTxns() {
		return numOfTxns;
	}

	public void setNumOfTxns(int numOfTxns) {
		this.numOfTxns = numOfTxns;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Transaction> getTxnList() {
		return txnList;
	}

	public void setTxnList(List<Transaction> txnList) {
		this.txnList = txnList;
	}

	public List<TxnStatementDTO> getTxnStmList() {
		return txnStmList;
	}

	public void setTxnStmList(List<TxnStatementDTO> txnStmList) {
		this.txnStmList = txnStmList;
	}

	public String getBusinessPartnerCode() {
		return businessPartnerCode;
	}

	public void setBusinessPartnerCode(String businessPartnerCode) {
		this.businessPartnerCode = businessPartnerCode;
	}
	

}
