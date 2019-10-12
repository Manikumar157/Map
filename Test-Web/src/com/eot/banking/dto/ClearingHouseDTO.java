package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

public class ClearingHouseDTO implements Serializable{
	
	private Integer clearingPoolId;	
	@NotEmpty(message="NotEmpty.clearingHouseDTO.clearingHouseName")
	private String clearingHouseName;	
	private String fileFormat;
	
	
	@NotNull(message="NotNull.clearingHouseDTO.settlementIntervalHours")
	private Integer settlementIntervalHours;	
	
	@NotNull(message="NotNull.clearingHouseDTO.settlementIntervalMins")
	private Integer settlementIntervalMins;
	
	@NotNull(message="NotNull.clearingHouseDTO.startUpTimeHours")
	private String startUpTimeHours;	
	
	@NotNull(message="NotNull.clearingHouseDTO.startUpTimeMinutes")
	private String startUpTimeMinutes;	
	
	@NotNull(message="NotNull.clearingHouseDTO.currency")
	private Integer currency;
	
	private Integer status = 1;	
	private String description;

	private Integer[] banks;
	@NotEmpty(message="NotEmpty.clearingHouseDTO.settlementAccount")
	private String settlementAccount;
	private String guaranteeAccount;
	private String centralBankAccount;
	private String EOTSwiftCode;
	private String contactPerson;
	private String mobileNumber;
	private String emailID;
	private String messageType;
	
	// vineeth changes
	
	private Integer mobileNumberLength;
	
	public Integer getMobileNumberLength() {
		return mobileNumberLength;
	}
	public void setMobileNumberLength(Integer mobileNumberLength) {
		this.mobileNumberLength = mobileNumberLength;
	}
	
	// vineeth changes over
	
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date date;
	
	private String messageSender;
	
	
	
	public String getMessageSender() {
		return messageSender;
	}
	public void setMessageSender(String messageSender) {
		this.messageSender = messageSender;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getSettlementAccount() {
		return settlementAccount;
	}
	public void setSettlementAccount(String settlementAccount) {
		this.settlementAccount = settlementAccount;
	}
	public String getGuaranteeAccount() {
		return guaranteeAccount;
	}
	public void setGuaranteeAccount(String guaranteeAccount) {
		this.guaranteeAccount = guaranteeAccount;
	}
	public String getCentralBankAccount() {
		return centralBankAccount;
	}
	public void setCentralBankAccount(String centralBankAccount) {
		this.centralBankAccount = centralBankAccount;
	}
	public String getEOTSwiftCode() {
		return EOTSwiftCode;
	}
	public void setEOTSwiftCode(String eOTSwiftCode) {
		EOTSwiftCode = eOTSwiftCode;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public Integer getClearingPoolId() {
		return clearingPoolId;
	}
	public void setClearingPoolId(Integer clearingPoolId) {
		this.clearingPoolId = clearingPoolId;
	}
	public String getClearingHouseName() {
		return clearingHouseName;
	}
	public void setClearingHouseName(String clearingHouseName) {
		this.clearingHouseName = clearingHouseName;
	}
	public String getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}
	public Integer getSettlementIntervalHours() {
		return settlementIntervalHours;
	}
	public void setSettlementIntervalHours(Integer settlementIntervalHours) {
		this.settlementIntervalHours = settlementIntervalHours;
	}
	public Integer getSettlementIntervalMins() {
		return settlementIntervalMins;
	}
	public void setSettlementIntervalMins(Integer settlementIntervalMins) {
		this.settlementIntervalMins = settlementIntervalMins;
	}
	public Integer getCurrency() {
		return currency;
	}
	public void setCurrency(Integer currency) {
		this.currency = currency;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer[] getBanks() {
		return banks;
	}
	public void setBanks(Integer[] banks) {
		this.banks = banks;
	}
	public String getStartUpTimeHours() {
		return startUpTimeHours;
	}
	public void setStartUpTimeHours(String startUpTimeHours) {
		this.startUpTimeHours = startUpTimeHours;
	}
	public String getStartUpTimeMinutes() {
		return startUpTimeMinutes;
	}
	public void setStartUpTimeMinutes(String startUpTimeMinutes) {
		this.startUpTimeMinutes = startUpTimeMinutes;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

}
