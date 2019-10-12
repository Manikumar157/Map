/**
 * Copyright � Altimetrik 2013. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Altimetrik. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms and conditions
 * entered into with Altimetrik.
 *
 * Id: ExternalTransactionDTO.java,v 1.1
 *
 * Date Author Changes
 * 25 Oct, 2014, 5:10:29 PM Swadhin Created
 */
package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * The Class ExternalTransactionDTO.
 */
public class ExternalTransactionDTO implements Serializable {
	  
	  /** The external txn id. */
  	private Long externalTxnId;
	  
	  /** The external entity id. */
  	private Long externalEntityId;
	  
	  /** The stan. */
  	private Long stan;
	  
	  /** The mobile number. */
  	private String mobileNumber;
	  
	  /** The customer name. */
  	private String customerName;
	  
	  /** The beneficiary mobile number. */
  	private String beneficiaryMobileNumber;
	  
	  /** The beneficiary name. */
  	private String beneficiaryName;
	  
	  /** The transaction type. */
  	private Integer transactionType;
	  
	  /** The transaction date. */
  	@DateTimeFormat(pattern="dd/MM/yyyy") 
	  private Date transactionDate;
	  
	  /** The amount. */
  	private double amount;
	  
	  /** The transaction id. */
  	private String transactionId;
	  
	  /** The status. */
  	private Integer status;
	  
	  /** The from date. */
  	@DateTimeFormat(pattern="dd/MM/yyyy") 
	  private Date fromDate;
	  
	  /** The to date. */
  	@DateTimeFormat(pattern="dd/MM/yyyy") 
	  private Date toDate;
	  
	  /** The user name. */
  	private String userName;
	  
	  /** The service charge amount. */
  	private double serviceChargeAmount;
	  
	/**
	 * Gets the from date.
	 *
	 * @return the from date
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * Sets the from date.
	 *
	 * @param fromDate the new from date
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets the to date.
	 *
	 * @return the to date
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * Sets the to date.
	 *
	 * @param toDate the new to date
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * Gets the external txn id.
	 *
	 * @return the external txn id
	 */
	public Long getExternalTxnId() {
		return externalTxnId;
	}

	/**
	 * Sets the external txn id.
	 *
	 * @param externalTxnId the new external txn id
	 */
	public void setExternalTxnId(Long externalTxnId) {
		this.externalTxnId = externalTxnId;
	}

	/**
	 * Gets the external entity id.
	 *
	 * @return the external entity id
	 */
	public Long getExternalEntityId() {
		return externalEntityId;
	}

	/**
	 * Sets the external entity id.
	 *
	 * @param externalEntityId the new external entity id
	 */
	public void setExternalEntityId(Long externalEntityId) {
		this.externalEntityId = externalEntityId;
	}

	/**
	 * Gets the stan.
	 *
	 * @return the stan
	 */
	public Long getStan() {
		return stan;
	}

	/**
	 * Sets the stan.
	 *
	 * @param stan the new stan
	 */
	public void setStan(Long stan) {
		this.stan = stan;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile number
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the customer name.
	 *
	 * @return the customer name
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Sets the customer name.
	 *
	 * @param customerName the new customer name
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Gets the beneficiary mobile number.
	 *
	 * @return the beneficiary mobile number
	 */
	public String getBeneficiaryMobileNumber() {
		return beneficiaryMobileNumber;
	}

	/**
	 * Sets the beneficiary mobile number.
	 *
	 * @param beneficiaryMobileNumber the new beneficiary mobile number
	 */
	public void setBeneficiaryMobileNumber(String beneficiaryMobileNumber) {
		this.beneficiaryMobileNumber = beneficiaryMobileNumber;
	}

	/**
	 * Gets the beneficiary name.
	 *
	 * @return the beneficiary name
	 */
	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	/**
	 * Sets the beneficiary name.
	 *
	 * @param beneficiaryName the new beneficiary name
	 */
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	/**
	 * Gets the transaction type.
	 *
	 * @return the transaction type
	 */
	public Integer getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transaction type.
	 *
	 * @param transactionType the new transaction type
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the transaction date.
	 *
	 * @return the transaction date
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Sets the transaction date.
	 *
	 * @param transactionDate the new transaction date
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the transaction id.
	 *
	 * @param transactionId the new transaction id
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the service charge amount.
	 *
	 * @return the service charge amount
	 */
	public double getServiceChargeAmount() {
		return serviceChargeAmount;
	}

	/**
	 * Sets the service charge amount.
	 *
	 * @param serviceChargeAmount the new service charge amount
	 */
	public void setServiceChargeAmount(double serviceChargeAmount) {
		this.serviceChargeAmount = serviceChargeAmount;
	}

	  
}
