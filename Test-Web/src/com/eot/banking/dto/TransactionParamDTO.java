/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: TransactionParamDTO.java
*
* Date Author Changes
* 7 Mar, 2016 Swadhin Created
*/
package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eot.banking.common.EOTConstants;
import com.eot.entity.Bank;
import com.eot.entity.Customer;

/**
 * The Class TransactionParamDTO.
 */
public class TransactionParamDTO implements BaseDTO,Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9001879703993104762L;

	/** The customer id. */
	private String customerId;
	
	/** The customer name. */
	private String customerName;
	
	/** The mobile number. */
	@NotEmpty
	private String mobileNumber;
	
	/** The transaction type. */
	private int transactionType;
	
	/** The account number. */
	private String accountNumber;
	
	/** The account alias. */
	private String accountAlias;
	
	/** The amount. */
	private Long amount ;
	
	/** The from date. */
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date fromDate ;
	
	/** The to date. */
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date toDate ;
	
	/** The num of txns. */
	private int numOfTxns ;
	
	/** The txn file. */
	private CommonsMultipartFile txnFile;
	
	/** The template id. */
	private Integer templateId = EOTConstants.TRANSACTION_SUPPORT_TEMPLATE_ID;
	
	/** The businessPartner code. */
	private String businessPartnerCode;
	
	/** The description. */
	private String description;
	
	/** The error date. */
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date errorDate ;
	
	/** The error description. */
	private String errorDescription;
	
	/** The total processed record. */
	private Integer totalProcessedRecord;
	
	/** The total processed value. */
	private Double totalProcessedValue;
	
	/** The total success record. */
	private Integer totalSuccessRecord;
	
	/** The total success value. */
	private Double totalSuccessValue;
	
	/** The total failed record. */
	private Integer totalFailedRecord;
	
	/** The total failed value. */
	private Double totalFailedValue;
	
	private String status;
	
	private Double serviceCharge;
	
	private String fromAccountNumber;
	private String toAccountNo;
	private String customerCode;
	private String customerMobileNo;
	private String name;
	private Long transactionId;
	private String otp;
	
	private Date transactionDate;
	private Integer referenceType;
	private String customerAccount;
	private Integer customerAccountType;
	private String otherAccount;
	private Integer otherAccountType;
	private String transactionNo;
	private String comment;
	private String initiatedBy;
	private String approvedBy;
	private Bank bank;
	private Set transactionJournals;
	private Customer customer;

	
	
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customer id.
	 *
	 * @param customerId the new customer id
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	 * Gets the transaction type.
	 *
	 * @return the transaction type
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transaction type.
	 *
	 * @param transactionType the new transaction type
	 */
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the account number.
	 *
	 * @return the account number
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the account number.
	 *
	 * @param accountNumber the new account number
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * Gets the account alias.
	 *
	 * @return the account alias
	 */
	public String getAccountAlias() {
		return accountAlias;
	}

	/**
	 * Sets the account alias.
	 *
	 * @param accountAlias the new account alias
	 */
	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public Long getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(Long amount) {
		this.amount = amount;
	}

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
	 * Gets the num of txns.
	 *
	 * @return the num of txns
	 */
	public int getNumOfTxns() {
		return numOfTxns;
	}

	/**
	 * Sets the num of txns.
	 *
	 * @param numOfTxns the new num of txns
	 */
	public void setNumOfTxns(int numOfTxns) {
		this.numOfTxns = numOfTxns;
	}

	/**
	 * Gets the txn file.
	 *
	 * @return the txn file
	 */
	public CommonsMultipartFile getTxnFile() {
		return txnFile;
	}

	/**
	 * Sets the txn file.
	 *
	 * @param txnFile the new txn file
	 */
	public void setTxnFile(CommonsMultipartFile txnFile) {
		this.txnFile = txnFile;
	}

	/**
	 * Gets the template id.
	 *
	 * @return the template id
	 */
	public Integer getTemplateId() {
		return templateId;
	}

	/**
	 * Sets the template id.
	 *
	 * @param templateId the new template id
	 */
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	/**
	 * Gets the business partner code.
	 *
	 * @return the business partner code
	 */
	public String getBusinessPartnerCode() {
		return businessPartnerCode;
	}

	/**
	 * Sets the business partner code.
	 *
	 * @param businessPartnerCode the new business partner code
	 */
	public void setBusinessPartnerCode(String businessPartnerCode) {
		this.businessPartnerCode = businessPartnerCode;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the error date.
	 *
	 * @return the error date
	 */
	public Date getErrorDate() {
		return errorDate;
	}

	/**
	 * Sets the error date.
	 *
	 * @param errorDate the new error date
	 */
	public void setErrorDate(Date errorDate) {
		this.errorDate = errorDate;
	}

	/**
	 * Gets the error description.
	 *
	 * @return the error description
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Sets the error description.
	 *
	 * @param errorDescription the new error description
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * Gets the total processed record.
	 *
	 * @return the totalProcessedRecord
	 */
	public Integer getTotalProcessedRecord() {
		return totalProcessedRecord;
	}

	/**
	 * Sets the total processed record.
	 *
	 * @param totalProcessedRecord the totalProcessedRecord to set
	 */
	public void setTotalProcessedRecord(Integer totalProcessedRecord) {
		this.totalProcessedRecord = totalProcessedRecord;
	}

	/**
	 * Gets the total processed value.
	 *
	 * @return the totalProcessedValue
	 */
	public Double getTotalProcessedValue() {
		return totalProcessedValue;
	}

	/**
	 * Sets the total processed value.
	 *
	 * @param totalProcessedValue the totalProcessedValue to set
	 */
	public void setTotalProcessedValue(Double totalProcessedValue) {
		this.totalProcessedValue = totalProcessedValue;
	}

	/**
	 * Gets the total success record.
	 *
	 * @return the totalSuccessRecord
	 */
	public Integer getTotalSuccessRecord() {
		return totalSuccessRecord;
	}

	/**
	 * Sets the total success record.
	 *
	 * @param totalSuccessRecord the totalSuccessRecord to set
	 */
	public void setTotalSuccessRecord(Integer totalSuccessRecord) {
		this.totalSuccessRecord = totalSuccessRecord;
	}

	/**
	 * Gets the total success value.
	 *
	 * @return the totalSuccessValue
	 */
	public Double getTotalSuccessValue() {
		return totalSuccessValue;
	}

	/**
	 * Sets the total success value.
	 *
	 * @param totalSuccessValue the totalSuccessValue to set
	 */
	public void setTotalSuccessValue(Double totalSuccessValue) {
		this.totalSuccessValue = totalSuccessValue;
	}

	/**
	 * Gets the total failed record.
	 *
	 * @return the totalFailedRecord
	 */
	public Integer getTotalFailedRecord() {
		return totalFailedRecord;
	}

	/**
	 * Sets the total failed record.
	 *
	 * @param totalFailedRecord the totalFailedRecord to set
	 */
	public void setTotalFailedRecord(Integer totalFailedRecord) {
		this.totalFailedRecord = totalFailedRecord;
	}

	/**
	 * Gets the total failed value.
	 *
	 * @return the totalFailedValue
	 */
	public Double getTotalFailedValue() {
		return totalFailedValue;
	}

	/**
	 * Sets the total failed value.
	 *
	 * @param totalFailedValue the totalFailedValue to set
	 */
	public void setTotalFailedValue(Double totalFailedValue) {
		this.totalFailedValue = totalFailedValue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(Double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerMobileNo() {
		return customerMobileNo;
	}

	public void setCustomerMobileNo(String customerMobileNo) {
		this.customerMobileNo = customerMobileNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Integer getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(Integer referenceType) {
		this.referenceType = referenceType;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public Integer getCustomerAccountType() {
		return customerAccountType;
	}

	public void setCustomerAccountType(Integer customerAccountType) {
		this.customerAccountType = customerAccountType;
	}

	public String getOtherAccount() {
		return otherAccount;
	}

	public void setOtherAccount(String otherAccount) {
		this.otherAccount = otherAccount;
	}

	public Integer getOtherAccountType() {
		return otherAccountType;
	}

	public void setOtherAccountType(Integer otherAccountType) {
		this.otherAccountType = otherAccountType;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public Set getTransactionJournals() {
		return transactionJournals;
	}

	public void setTransactionJournals(Set transactionJournals) {
		this.transactionJournals = transactionJournals;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
