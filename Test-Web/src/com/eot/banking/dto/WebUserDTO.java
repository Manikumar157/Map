package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class WebUserDTO implements Serializable{

	
	private String userName;
    private String roleName;  
	private String password;
	private String description;
	
	private String middleName;

	@NotEmpty(message="NotEmpty.webUserDTO.firstName")
	private String firstName;
		
	private String lastName;
        
    private String email;

    @NotEmpty(message="NotEmpty.webUserDTO.mobileNumber")
    private String mobileNumber;

    
	private String alternateNumber;

    @NotNull(message="NotEmpty.webUserDTO.roleId")
	private Integer roleId;
    
    private String language;
    
    private Integer bankId;
    
    private Integer bank;
    
  

	private Long branchId;
    
	private List branchList;
	
	private String status ;
	
	private String accountLock;
	
	private java.util.Date createdDate;
	
	private String businessPartnerName;
	
	private int TFA;
	private Integer bankGroupId;
	private Integer countryId;
	private Integer mobilenumberLength;
	private String countryName;
	private int accessMode;
	
	private String businessPartnerId;
	
	private String bulkPaymentPartnerId;
	
	public int getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(int accessMode) {
		this.accessMode = accessMode;
	}
	
	public String getBusinessPartnerId() {
		return businessPartnerId;
	}

	public void setBusinessPartnerId(String businessPartnerId) {
		this.businessPartnerId = businessPartnerId;
	}

	public Integer getBank() {
			return bank;
		}

		public void setBank(Integer bank) {
			this.bank = bank;
		}
	
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getMobilenumberLength() {
		return mobilenumberLength;
	}

	public void setMobilenumberLength(Integer mobilenumberLength) {
		this.mobilenumberLength = mobilenumberLength;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List getBranchList() {
		return branchList;
	}

	public void setBranchList(List branchList) {
		this.branchList = branchList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAlternateNumber() {
		return alternateNumber;
	}

	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTFA() {
		return TFA;
	}

	public void setTFA(int tfa) {
		TFA = tfa;
	}

	

	public Integer getBankGroupId() {
		return bankGroupId;
	}

	public void setBankGroupId(Integer bankGroupId) {
		this.bankGroupId = bankGroupId;
	}

	public String getAccountLock() {
		return accountLock;
	}

	public void setAccountLock(String accountLock) {
		this.accountLock = accountLock;
	}

	public String getBusinessPartnerName() {
		return businessPartnerName;
	}

	public void setBusinessPartnerName(String businessPartnerName) {
		this.businessPartnerName = businessPartnerName;
	}

	public String getBulkPaymentPartnerId() {
		return bulkPaymentPartnerId;
	}

	public void setBulkPaymentPartnerId(String bulkPaymentPartnerId) {
		this.bulkPaymentPartnerId = bulkPaymentPartnerId;
	}

	

}
