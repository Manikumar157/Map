/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: BusinessPartnerDTO.java
*
* Date Author Changes
* Oct 16, 2018 Vinod Joshi Created
*/
package com.eot.banking.dto;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eot.entity.Bank;
import com.eot.entity.KycType;

/**
 * The Class BusinessPartnerDTO.
 */
public class BusinessPartnerDTO {
	
		
    /** The id. */
    private Integer id;

    /** The code. */
    private String code;

    /** The name. */
    private String name;

    /** The address. */
    private String address;

    /** The created date. */
    private Date createdDate;

    /** The updated date. */
    private Date updatedDate;

    /** The status. */
    private Integer status;

    /** The contact person. */
    private String contactPerson;

    /** The email id. */
    private String emailId;

    /** The contact number. */
    private String contactNumber;

    /** The partner type. */
    private Integer partnerType;

    /** The created by. */
    private Integer createdBy;

    /** The commission percent. */
    private Double commissionPercent;

    /** The account number. */
    private String accountNumber;

    /** The currency. */
    private com.eot.entity.Currency currency;

    /** The country. */
    private com.eot.entity.Country country;

    /** The customers. */
    private List customers;

    /** The business partner users. */
    private List businessPartnerUsers;
    
    /** The senior partner. */
    private Integer seniorPartner;
    
    /** The bank. */
    private Bank bank;
    
    /** The organization number. */
    private String organizationNumber;
    
    /** The organization email id. */
    private String organizationEmailId;
    
    /** The organization address. */
    private String organizationAddress;
    
    /** The business entity limit. *//*
    private Double businessEntityLimit;*/
    
    private BigDecimal businessEntityLimit;
    
    private String businessPartnerUserName;
    private String webUserName;
    /** The kyc id number. */
    private String kycIdNumber;
    
    private String fromDate;
    private String toDate;
    
    private String designation;
    
    private String transactionId;
    private String transactionType;
	private String refTransactionId;
	private String partnerCode;
    
	
    public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getRefTransactionId() {
		return refTransactionId;
	}

	public void setRefTransactionId(String refTransactionId) {
		this.refTransactionId = refTransactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getWebUserName() {
		return webUserName;
	}

	public void setWebUserName(String webUserName) {
		this.webUserName = webUserName;
	}

	public String getBusinessPartnerUserName() {
		return businessPartnerUserName;
	}

	public void setBusinessPartnerUserName(String businessPartnerUserName) {
		this.businessPartnerUserName = businessPartnerUserName;
	}

	/** The kyc image. */
    private  MultipartFile kycImage; 
    
    /** The kyc type id. */
    private String kycTypeId;
    
    /**
     * Gets the kyc type id.
     *
     * @return the kyc type id
     */
    public String getKycTypeId() {
		return kycTypeId;
	}

	/**
	 * Sets the kyc type id.
	 *
	 * @param kycTypeId the new kyc type id
	 */
	public void setKycTypeId(String kycTypeId) {
		this.kycTypeId = kycTypeId;
	}

	/*private KycType kycType;
    
    public KycType getKycType() {
		return kycType;
	}

	public void setKycType(KycType kycType) {
		this.kycType = kycType;
	}
*/
	

	/**
	 * Gets the kyc id number.
	 *
	 * @return the kyc id number
	 */
	public String getKycIdNumber() {
		return kycIdNumber;
	}

	/**
	 * Sets the kyc id number.
	 *
	 * @param kycIdNumber the new kyc id number
	 */
	public void setKycIdNumber(String kycIdNumber) {
		this.kycIdNumber = kycIdNumber;
	}

	/**
	 * Gets the kyc image.
	 *
	 * @return the kyc image
	 */
	public MultipartFile getKycImage() {
		return kycImage;
	}

	/**
	 * Sets the kyc image.
	 *
	 * @param kycImage the new kyc image
	 */
	public void setKycImage(MultipartFile kycImage) {
		this.kycImage = kycImage;
	}

	/**
	 * Gets the business entity limit.
	 *
	 * @return the business entity limit
	 */
	/*public Double getBusinessEntityLimit() {
		return businessEntityLimit;
	}

	*//**
	 * Sets the business entity limit.
	 *
	 * @param businessEntityLimit the new business entity limit
	 *//*
	public void setBusinessEntityLimit(Double businessEntityLimit) {
		this.businessEntityLimit = businessEntityLimit;
	}
*/
	/**
	 * Gets the organization address.
	 *
	 * @return the organization address
	 */
	public String getOrganizationAddress() {
		return organizationAddress;
	}

	public BigDecimal getBusinessEntityLimit() {
		return businessEntityLimit;
	}

	public void setBusinessEntityLimit(BigDecimal businessEntityLimit) {
		this.businessEntityLimit = businessEntityLimit;
	}

	/**
	 * Sets the organization address.
	 *
	 * @param organizationAddress the new organization address
	 */
	public void setOrganizationAddress(String organizationAddress) {
		this.organizationAddress = organizationAddress;
	}

	/**
	 * Gets the organization email id.
	 *
	 * @return the organization email id
	 */
	public String getOrganizationEmailId() {
		return organizationEmailId;
	}

	/**
	 * Sets the organization email id.
	 *
	 * @param organizationEmailId the new organization email id
	 */
	public void setOrganizationEmailId(String organizationEmailId) {
		this.organizationEmailId = organizationEmailId;
	}

	/**
	 * Gets the organization number.
	 *
	 * @return the organization number
	 */
	public String getOrganizationNumber() {
		return organizationNumber;
	}

	/**
	 * Sets the organization number.
	 *
	 * @param organizationNumber the new organization number
	 */
	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}

	/**
	 * Gets the bank.
	 *
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * Sets the bank.
	 *
	 * @param bank the new bank
	 */
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	 * Gets the contact person.
	 *
	 * @return the contact person
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * Sets the contact person.
	 *
	 * @param contactPerson the new contact person
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * Gets the email id.
	 *
	 * @return the email id
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * Sets the email id.
	 *
	 * @param emailId the new email id
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * Gets the contact number.
	 *
	 * @return the contact number
	 */
	public String getContactNumber() {
		return contactNumber;
	}

	/**
	 * Sets the contact number.
	 *
	 * @param contactNumber the new contact number
	 */
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	/**
	 * Gets the partner type.
	 *
	 * @return the partner type
	 */
	public Integer getPartnerType() {
		return partnerType;
	}

	/**
	 * Sets the partner type.
	 *
	 * @param partnerType the new partner type
	 */
	public void setPartnerType(Integer partnerType) {
		this.partnerType = partnerType;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy the new created by
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the commission percent.
	 *
	 * @return the commission percent
	 */
	public Double getCommissionPercent() {
		return commissionPercent;
	}

	/**
	 * Sets the commission percent.
	 *
	 * @param commissionPercent the new commission percent
	 */
	public void setCommissionPercent(Double commissionPercent) {
		this.commissionPercent = commissionPercent;
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
	 * Gets the currency.
	 *
	 * @return the currency
	 */
	public com.eot.entity.Currency getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 *
	 * @param currency the new currency
	 */
	public void setCurrency(com.eot.entity.Currency currency) {
		this.currency = currency;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public com.eot.entity.Country getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(com.eot.entity.Country country) {
		this.country = country;
	}

	/**
	 * Gets the customers.
	 *
	 * @return the customers
	 */
	public List getCustomers() {
		return customers;
	}

	/**
	 * Sets the customers.
	 *
	 * @param customers the new customers
	 */
	public void setCustomers(List customers) {
		this.customers = customers;
	}

	/**
	 * Gets the business partner users.
	 *
	 * @return the business partner users
	 */
	public List getBusinessPartnerUsers() {
		return businessPartnerUsers;
	}

	/**
	 * Sets the business partner users.
	 *
	 * @param businessPartnerUsers the new business partner users
	 */
	public void setBusinessPartnerUsers(List businessPartnerUsers) {
		this.businessPartnerUsers = businessPartnerUsers;
	}

	/**
	 * Gets the senior partner.
	 *
	 * @return the senior partner
	 */
	public Integer getSeniorPartner() {
		return seniorPartner;
	}

	/**
	 * Sets the senior partner.
	 *
	 * @param seniorPartner the new senior partner
	 */
	public void setSeniorPartner(Integer seniorPartner) {
		this.seniorPartner = seniorPartner;
	}
    
    
    

}
