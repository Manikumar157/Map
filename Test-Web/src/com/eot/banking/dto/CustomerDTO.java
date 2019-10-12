package com.eot.banking.dto;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.eot.entity.Country;
import com.eot.entity.CustomerAccount;


public class CustomerDTO {

	private Long customerId;
	@NotEmpty(message="NotEmpty.customerDTO.mobileNumber")
	/*@Size(min = 6, max = 12 )*/
	private String mobileNumber;

	@NotEmpty(message="NotNull.customerDTO.title")
	private String title;

	@NotEmpty(message="NotNull.customerDTO.firstName")
	private String firstName;

	private String middleName;

	private String lastName;

	private String profession;

	@NotNull(message="NotNull.customerDTO.dob")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dob;

	@NotEmpty(message="NotNull.customerDTO.address")
	private String address;

	@Email
	private String emailAddress;

	private Integer questionId;
//	@NotNull(message="NotNull.customerDTO.questionId")
	private Integer question;

//	@NotEmpty(message="NotNull.customerDTO.answer")
	private String answer;
	private String placeOfBirth;
	private String gender = "Male";
//	@NotNull(message="NotNull.customerDTO.countryId")
	private Integer countryId ;
	private String country;

	private Integer cityId ;
	@NotNull(message="NotNull.customerDTO.cityId")
	private Integer city;
	private String cityName;
	private Long quarterId ;
	@NotNull(message="NotNull.customerDTO.quarterId")
	private Long quarter;
	private String quarterName;

	@NotEmpty(message="NotNull.customerDTO.language")
	private String language;

	private Integer type;

	private MultipartFile signature;

	private MultipartFile idProof;
	private MultipartFile profilePhoto;
	
	private MultipartFile addressProof;

	private Integer bankId;

	private Integer customerStatus = 40 ;

	private Integer appStatus;

	private Set<CustomerAccount> accountList;

	private Integer customerProfileId;

	private String profileName;

	private Integer isdCode;
	private String idType;

	private String idNumber; 
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date issueDate;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date expiryDate;

	private Date createdDate;
	
	private String placeOfIssue;

	private String others;
	private String IDTypeothers;
	private String selectedQuestion;

	private Integer custType;
	
	private Country customerCountry;
	
	private Integer isIDProofRequired;
	
	private String bankCustomerId;
	
	private Double commission;
	
	private Integer createdBy;
	
	private String onBordedBy;
	
	private String otp;
	
	private String actionType; 
	
	 /** Customer List for approve or reject *. */
    private Long[] manageCustomer;
	
    private Integer customerKycStatus;
    
    private String reasonForRejection;
    
    private String businessName;
    
    private String sortBy;
	private String sortColumn;
	
	private String newProfile;
	private String newSignature;
	private String newIDproof;
	private String newAddressProof;
    private String reasonForBlock;
    private String channel; 
    private String reasonForDeActivate;
    private String agentCode;
	
    public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getReasonForDeActivate() {
		return reasonForDeActivate;
	}

	public void setReasonForDeActivate(String reasonForDeActivate) {
		this.reasonForDeActivate = reasonForDeActivate;
	}

	public String getReasonForBlock() {
		return reasonForBlock;
	}

	public void setReasonForBlock(String reasonForBlock) {
		this.reasonForBlock = reasonForBlock;
	}

	public Integer getCustType() {
		return custType;
	}

	public void setCustType(Integer custType) {
		this.custType = custType;
	}

	public String getNewProfile() {
		return newProfile;
	}

	public void setNewProfile(String newProfile) {
		this.newProfile = newProfile;
	}

	public String getSelectedQuestion() {
		return selectedQuestion;
	}

	public void setSelectedQuestion(String selectedQuestion) {
		this.selectedQuestion = selectedQuestion;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}
	public String getIDTypeothers() {
		return IDTypeothers;
	}

	public void setIDTypeothers(String iDTypeothers) {
		IDTypeothers = iDTypeothers;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

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

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Integer getCustomerProfileId() {
		return customerProfileId;
	}

	public void setCustomerProfileId(Integer customerProfileId) {
		this.customerProfileId = customerProfileId;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(Integer customerStatus) {
		this.customerStatus = customerStatus;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getQuestion() {
		return question;
	}

	public void setQuestion(Integer question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Long getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(Long quarterId) {
		this.quarterId = quarterId;
	}

	public Long getQuarter() {
		return quarter;
	}

	public void setQuarter(Long quarter) {
		this.quarter = quarter;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public MultipartFile getSignature() {
		return signature;
	}

	public void setSignature(MultipartFile signature) {
		this.signature = signature;
	}

	public MultipartFile getIdProof() {
		return idProof;
	}

	public void setIdProof(MultipartFile idProof) {
		this.idProof = idProof;
	}

	public Integer getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(Integer appStatus) {
		this.appStatus = appStatus;
	}

	public Set<CustomerAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(Set<CustomerAccount> accountList) {
		this.accountList = accountList;
	}

	public String toString() {
		return new ToStringBuilder(this)
		.append("customerId", getCustomerId())
		.toString();
	}

	public Integer getIsdCode() {
		return isdCode;
	}

	public void setIsdCode(Integer isdCode) {
		this.isdCode = isdCode;
	}

	public Country getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(Country customerCountry) {
		this.customerCountry = customerCountry;
	}
	
	public Integer getIsIDProofRequired() {
		return isIDProofRequired;
	}

	public void setIsIDProofRequired(Integer isIDProofRequired) {
		this.isIDProofRequired = isIDProofRequired;
	}

	public String getBankCustomerId() {
		return bankCustomerId;
	}

	public void setBankCustomerId(String bankCustomerId) {
		this.bankCustomerId = bankCustomerId;
	}

	public MultipartFile getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(MultipartFile profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public MultipartFile getAddressProof() {
		return addressProof;
	}

	public void setAddressProof(MultipartFile addressProof) {
		this.addressProof = addressProof;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getOnBordedBy() {
		return onBordedBy;
	}

	public void setOnBordedBy(String onBordedBy) {
		this.onBordedBy = onBordedBy;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Long[] getManageCustomer() {
		return manageCustomer;
	}

	public void setManageCustomer(Long[] manageCustomer) {
		this.manageCustomer = manageCustomer;
	}

	public Integer getCustomerKycStatus() {
		return customerKycStatus;
	}

	public void setCustomerKycStatus(Integer customerKycStatus) {
		this.customerKycStatus = customerKycStatus;
	}

	public String getReasonForRejection() {
		return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
		this.reasonForRejection = reasonForRejection;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getNewSignature() {
		return newSignature;
	}

	public void setNewSignature(String newSignature) {
		this.newSignature = newSignature;
	}

	public String getNewIDproof() {
		return newIDproof;
	}

	public void setNewIDproof(String newIDproof) {
		this.newIDproof = newIDproof;
	}

	public String getNewAddressProof() {
		return newAddressProof;
	}

	public void setNewAddressProof(String newAddressProof) {
		this.newAddressProof = newAddressProof;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
