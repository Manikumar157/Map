package com.eot.banking.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


public class StakeHolderDTO {	
	  
    private Integer stakeholderId;  
    
    @NotEmpty(message="NotEmpty.stakeHolderDTO.stakeholderOrganization")
    private String stakeholderOrganization;    
    @NotEmpty(message="NotEmpty.stakeHolderDTO.address")
    private String address;
    @NotEmpty(message="NotEmpty.stakeHolderDTO.contactPersonName")
    private String contactPersonName;
    @NotEmpty(message="NotEmpty.stakeHolderDTO.contactAddress")
    private String contactAddress;
   
    private String contactPhone;
    
    private String contactMobile;
   
    private String emailAddress;
    @NotNull(message="NotEmpty.stakeHolderDTO.countryId")
    private Integer countryId;    
    private String accountNumber;


	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}


	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	/**
	 * @return the stakeholderId
	 */
	public Integer getStakeholderId() {
		return stakeholderId;
	}


	/**
	 * @return the stakeholderOrganaization
	 */
	public String getStakeholderOrganization() {
		return stakeholderOrganization;
	}


	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}


	/**
	 * @return the contactPersonName
	 */
	public String getContactPersonName() {
		return contactPersonName;
	}


	/**
	 * @return the contactAddress
	 */
	public String getContactAddress() {
		return contactAddress;
	}


	/**
	 * @return the contactPhone
	 */
	public String getContactPhone() {
		return contactPhone;
	}


	/**
	 * @return the contactMobile
	 */
	public String getContactMobile() {
		return contactMobile;
	}


	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}


	/**
	 * @return the countryId
	 */
	public Integer getCountryId() {
		return countryId;
	}


	/**
	 * @param stakeholderId the stakeholderId to set
	 */
	public void setStakeholderId(Integer stakeholderId) {
		this.stakeholderId = stakeholderId;
	}


	/**
	 * @param stakeholderOrganaization the stakeholderOrganaization to set
	 */
	public void setStakeholderOrganization(String stakeholderOrganization) {
		this.stakeholderOrganization = stakeholderOrganization;
	}


	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}


	/**
	 * @param contactPersonName the contactPersonName to set
	 */
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}


	/**
	 * @param contactAddress the contactAddress to set
	 */
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}


	/**
	 * @param contactPhone the contactPhone to set
	 */
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}


	/**
	 * @param contactMobile the contactMobile to set
	 */
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}


	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}


	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}


	public StakeHolderDTO(Integer stakeholderId,
			String stakeholderOrganization, String address,
			String contactPersonName, String contactAddress,
			String contactPhone, String contactMobile, String emailAddress,
			Integer countryId) {
		
		this.stakeholderId = stakeholderId;
		this.stakeholderOrganization = stakeholderOrganization;
		this.address = address;
		this.contactPersonName = contactPersonName;
		this.contactAddress = contactAddress;
		this.contactPhone = contactPhone;
		this.contactMobile = contactMobile;
		this.emailAddress = emailAddress;
		this.countryId = countryId;
	}


	public StakeHolderDTO() {
		
	}

	
}
