package com.eot.banking.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eot.banking.common.EOTConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class BankDTO.
 */
public class BankDTO implements Serializable{
	
	/** The bank id. */
	private Integer bankId;

	/** The bank name. */
	@NotEmpty(message="NotEmpty.bankDTO.bankName")
	private String bankName;
	
	/** The swift code. */
	private String swiftCode;
	
	/** The swift branch. */
	private String swiftBranch;
	
    /** The eot card bank code. */
    private String eotCardBankCode;
	
	/** The terminal id. */
	private String terminalId;
	
	/** The outlet number. */
	private String outletNumber;
	
	/** The channel cost. */
	private String channelCost;
	
	/**
	 * Gets the channel cost.
	 *
	 * @return the channel cost
	 */
	public String getChannelCost() {
		return channelCost;
	}
	
	/**
	 * Sets the channel cost.
	 *
	 * @param channelCost the new channel cost
	 */
	public void setChannelCost(String channelCost) {
		this.channelCost = channelCost;
	}
	
	/** The bank short name. */
	@NotEmpty(message="NotEmpty.bankDTO.bankShortName")
    private String bankShortName;
	
	/** The bank code. */
	@NotEmpty(message="NotEmpty.bankDTO.bankCode")
    private String bankCode;
    
    /** The description. */
    private String description;
	
	/** The country id. */
	@NotNull(message="NotNull.bankDTO.countryId")
	private Integer countryId; 
	
	/** The country name. */
	private String countryName;
	
	/** The currency name. */
	private String currencyName;
	
	/** The currency id. */
	private Integer currencyId;
	
	/** The status. */
	private Integer status;
	
	/** The agreement model id. */
	private Integer agreementModelId[];
	
	
	/** The agreement from field. */
	@NotNull(message="NotNull.bankDTO.agreementFromField")
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date agreementFromField[];
	
	/** The agreement to field. */
	@NotNull(message="NotNull.bankDTO.agreementToField")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date agreementToField[];
	
	/** The timezone id. */
	@NotNull(message="NotNull.bankDTO.timezoneId")
	private Integer timezoneId;
	
	/** The time zone desc. */
	private String timeZoneDesc;
	
	/** The clearing houses. */
	@NotNull(message="NotNull.bankDTO.clearingHouses")
	private Integer[] clearingHouses;
	
	/** The clearing house priorities. */
	@NotNull(message="NotNull.bankDTO.clearingHousePriorities")
	private Integer[] clearingHousePriorities;
	
	/** The branch file. */
	private CommonsMultipartFile branchFile;
	
	/** The templete id. */
	private Integer templeteId = EOTConstants.BANK_UPLOAD_FILE_TEMPLETE;
	
	/** The branch error list. */
	private List<String> branchErrorList ;
	
	/** The branch code length. */
	private List<String> branchCodeLength;
	
	/** The duplicate branch. */
	private List<String> duplicateBranch;
	
	/** The duplicate branch code. */
	private List<String> duplicateBranchCode;
	
	/** The csvduplicate branch code. */
	private List<String> csvduplicateBranchCode;
	
	/** The csv invalid city. */
	private List<String> csvInvalidCity;
	
	/** The kyc flag. */
	private Integer kycFlag=0;
	
	/** The application name. */
	private String applicationName;
	
	/** The total agrmnt. */
	/*@Start Added by sudhanshu for checking total agreements and clearing house for new UI  @date: 2016-04-21*/
	private Integer totalAgrmnt;
	
	/** The total CH. */
	private Integer totalCH;
	/*@End Added by sudhanshu for checking total agreements and clearing house for new UI  @date: 2016-04-21*/
	/** The collaboration logo. */
	private String collaborationLogo;
	
	
	/**
	 * Gets the kyc flag.
	 *
	 * @return the kyc flag
	 */
	public Integer getKycFlag() {
		return kycFlag;
	}
	
	/**
	 * Sets the kyc flag.
	 *
	 * @param kycFlag the new kyc flag
	 */
	public void setKycFlag(Integer kycFlag) {
		this.kycFlag = kycFlag;
	}
	
	/**
	 * Gets the csv invalid city.
	 *
	 * @return the csv invalid city
	 */
	public List<String> getCsvInvalidCity() {
		return csvInvalidCity;
	}
	
	/**
	 * Sets the csv invalid city.
	 *
	 * @param csvInvalidCity the new csv invalid city
	 */
	public void setCsvInvalidCity(List<String> csvInvalidCity) {
		this.csvInvalidCity = csvInvalidCity;
	}
	
	/**
	 * Gets the csvduplicate branch code.
	 *
	 * @return the csvduplicate branch code
	 */
	public List<String> getCsvduplicateBranchCode() {
		return csvduplicateBranchCode;
	}
	
	/**
	 * Sets the csvduplicate branch code.
	 *
	 * @param csvduplicateBranchCode the new csvduplicate branch code
	 */
	public void setCsvduplicateBranchCode(List<String> csvduplicateBranchCode) {
		this.csvduplicateBranchCode = csvduplicateBranchCode;
	}
	
	/**
	 * Gets the duplicate branch.
	 *
	 * @return the duplicate branch
	 */
	public List<String> getDuplicateBranch() {
		return duplicateBranch;
	}
	
	/**
	 * Sets the duplicate branch.
	 *
	 * @param duplicateBranch the new duplicate branch
	 */
	public void setDuplicateBranch(List<String> duplicateBranch) {
		this.duplicateBranch = duplicateBranch;
	}
	
	/**
	 * Gets the duplicate branch code.
	 *
	 * @return the duplicate branch code
	 */
	public List<String> getDuplicateBranchCode() {
		return duplicateBranchCode;
	}
	
	/**
	 * Sets the duplicate branch code.
	 *
	 * @param duplicateBranchCode the new duplicate branch code
	 */
	public void setDuplicateBranchCode(List<String> duplicateBranchCode) {
		this.duplicateBranchCode = duplicateBranchCode;
	}
	
	/** The bank group id. */
	private Integer bankGroupId;
	
	/** The govt tax. */
	@NotNull(message="NotNull.bankDTO.govtTax")
	private Float govtTax;
	
	/** The eot charge. */
	@NotNull(message="NotNull.bankDTO.eotCharge")
	private Float eotCharge;
	
	/** The bank charge. */
	@NotNull(message="NotNull.bankDTO.bankCharge")
	private Float bankCharge;
	
	/** The stamp fee. */
	@NotNull(message="NotNull.bankDTO.stampFee")
	private Float stampFee;
	
	/** The agreements. */
	private Set agreements;
	
	/** The ch pool. */
	private Set chPool;
	
	/** The invalid branch code. */
	private List<String> invalidBranchCode;
	
	/**
	 * Gets the stamp fee.
	 *
	 * @return the stamp fee
	 */
	public Float getStampFee() {
		return stampFee;
	}
	
	/**
	 * Sets the stamp fee.
	 *
	 * @param stampFee the new stamp fee
	 */
	public void setStampFee(Float stampFee) {
		this.stampFee = stampFee;
	}
	
	/**
	 * Gets the invalid branch code.
	 *
	 * @return the invalid branch code
	 */
	public List<String> getInvalidBranchCode() {
		return invalidBranchCode;
	}
	
	/**
	 * Sets the invalid branch code.
	 *
	 * @param invalidBranchCode the new invalid branch code
	 */
	public void setInvalidBranchCode(List<String> invalidBranchCode) {
		this.invalidBranchCode = invalidBranchCode;
	}
	
	/**
	 * Gets the agreements.
	 *
	 * @return the agreements
	 */
	public Set getAgreements() {
		return agreements;
	}
	
	/**
	 * Sets the agreements.
	 *
	 * @param agreements the new agreements
	 */
	public void setAgreements(Set agreements) {
		this.agreements = agreements;
	}
	
	/**
	 * Gets the bank id.
	 *
	 * @return the bank id
	 */
	public Integer getBankId() {
		return bankId;
	}
	
	/**
	 * Sets the bank id.
	 *
	 * @param bankId the new bank id
	 */
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	
	/**
	 * Gets the bank name.
	 *
	 * @return the bank name
	 */
	public String getBankName() {
		return bankName;
	}
	
	/**
	 * Sets the bank name.
	 *
	 * @param bankName the new bank name
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * Gets the country id.
	 *
	 * @return the country id
	 */
	public Integer getCountryId() {
		return countryId;
	}
	
	/**
	 * Sets the country id.
	 *
	 * @param countryId the new country id
	 */
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	/**
	 * Gets the currency id.
	 *
	 * @return the currency id
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}
	
	/**
	 * Sets the currency id.
	 *
	 * @param currencyId the new currency id
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	
	/**
	 * Gets the agreement model id.
	 *
	 * @return the agreement model id
	 */
	public Integer[] getAgreementModelId() {
		return agreementModelId;
	}
	
	/**
	 * Sets the agreement model id.
	 *
	 * @param agreementModelId the new agreement model id
	 */
	public void setAgreementModelId(Integer[] agreementModelId) {
		this.agreementModelId = agreementModelId;
	}
	
	/**
	 * Gets the agreement from field.
	 *
	 * @return the agreement from field
	 */
	public Date[] getAgreementFromField() {
		return agreementFromField;
	}
	
	/**
	 * Gets the agreement to field.
	 *
	 * @return the agreement to field
	 */
	public Date[] getAgreementToField() {
		return agreementToField;
	}
	
	/**
	 * Gets the timezone id.
	 *
	 * @return the timezone id
	 */
	public Integer getTimezoneId() {
		return timezoneId;
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
	 * Sets the timezone id.
	 *
	 * @param timezoneId the new timezone id
	 */
	public void setTimezoneId(Integer timezoneId) {
		this.timezoneId = timezoneId;
	}
	
	/**
	 * Gets the clearing houses.
	 *
	 * @return the clearing houses
	 */
	public Integer[] getClearingHouses() {
		return clearingHouses;
	}
	
	/**
	 * Sets the clearing houses.
	 *
	 * @param clearingHouses the new clearing houses
	 */
	public void setClearingHouses(Integer[] clearingHouses) {
		this.clearingHouses = clearingHouses;
	}
	
	/**
	 * Gets the clearing house priorities.
	 *
	 * @return the clearing house priorities
	 */
	public Integer[] getClearingHousePriorities() {
		return clearingHousePriorities;
	}
	
	/**
	 * Sets the clearing house priorities.
	 *
	 * @param clearingHousePriorities the new clearing house priorities
	 */
	public void setClearingHousePriorities(Integer[] clearingHousePriorities) {
		this.clearingHousePriorities = clearingHousePriorities;
	}
	
	/**
	 * Gets the branch file.
	 *
	 * @return the branch file
	 */
	public CommonsMultipartFile getBranchFile() {
		return branchFile;
	}
	
	/**
	 * Sets the branch file.
	 *
	 * @param branchFile the new branch file
	 */
	public void setBranchFile(CommonsMultipartFile branchFile) {
		this.branchFile = branchFile;
	}
	
	/**
	 * Gets the branch error list.
	 *
	 * @return the branch error list
	 */
	public List<String> getBranchErrorList() {
		return branchErrorList;
	}
	
	/**
	 * Sets the branch error list.
	 *
	 * @param branchErrorList the new branch error list
	 */
	public void setBranchErrorList(List<String> branchErrorList) {
		this.branchErrorList = branchErrorList;
	}
	
	/**
	 * Gets the bank short name.
	 *
	 * @return the bankShortName
	 */
	public String getBankShortName() {
		return bankShortName;
	}
	
	/**
	 * Gets the bank code.
	 *
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
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
	 * Sets the bank short name.
	 *
	 * @param bankShortName the bankShortName to set
	 */
	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}
	
	/**
	 * Sets the bank code.
	 *
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the country name.
	 *
	 * @return the country name
	 */
	public String getCountryName() {
		return countryName;
	}
	
	/**
	 * Sets the country name.
	 *
	 * @param countryName the new country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	/**
	 * Gets the currency name.
	 *
	 * @return the currency name
	 */
	public String getCurrencyName() {
		return currencyName;
	}
	
	/**
	 * Sets the currency name.
	 *
	 * @param currencyName the new currency name
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	/**
	 * Gets the time zone desc.
	 *
	 * @return the time zone desc
	 */
	public String getTimeZoneDesc() {
		return timeZoneDesc;
	}
	
	/**
	 * Sets the time zone desc.
	 *
	 * @param timeZoneDesc the new time zone desc
	 */
	public void setTimeZoneDesc(String timeZoneDesc) {
		this.timeZoneDesc = timeZoneDesc;
	}
	
	/**
	 * Sets the agreement from field.
	 *
	 * @param agreementFromField the new agreement from field
	 */
	public void setAgreementFromField(Date[] agreementFromField) {
		this.agreementFromField = agreementFromField;
	}
	
	/**
	 * Sets the agreement to field.
	 *
	 * @param agreementToField the new agreement to field
	 */
	public void setAgreementToField(Date[] agreementToField) {
		this.agreementToField = agreementToField;
	}
	
	/**
	 * Gets the bank group id.
	 *
	 * @return the bank group id
	 */
	public Integer getBankGroupId() {
		return bankGroupId;
	}
	
	/**
	 * Sets the bank group id.
	 *
	 * @param bankGroupId the new bank group id
	 */
	public void setBankGroupId(Integer bankGroupId) {
		this.bankGroupId = bankGroupId;
	}
	
	/**
	 * Gets the govt tax.
	 *
	 * @return the govt tax
	 */
	public Float getGovtTax() {
		return govtTax;
	}
	
	/**
	 * Sets the govt tax.
	 *
	 * @param govtTax the new govt tax
	 */
	public void setGovtTax(Float govtTax) {
		this.govtTax = govtTax;
	}
	
	/**
	 * Gets the bank charge.
	 *
	 * @return the bank charge
	 */
	public Float getBankCharge() {
		return bankCharge;
	}
	
	/**
	 * Sets the bank charge.
	 *
	 * @param bankCharge the new bank charge
	 */
	public void setBankCharge(Float bankCharge) {
		this.bankCharge = bankCharge;
	}
	
	/**
	 * Gets the branch code length.
	 *
	 * @return the branch code length
	 */
	public List<String> getBranchCodeLength() {
		return branchCodeLength;
	}
	
	/**
	 * Sets the branch code length.
	 *
	 * @param branchCodeLength the new branch code length
	 */
	public void setBranchCodeLength(List<String> branchCodeLength) {
		this.branchCodeLength = branchCodeLength;
	}
	
	/**
	 * Gets the swift code.
	 *
	 * @return the swift code
	 */
	public String getSwiftCode() {
		return swiftCode;
	}
	
	/**
	 * Sets the swift code.
	 *
	 * @param swiftCode the new swift code
	 */
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	
	/**
	 * Gets the swift branch.
	 *
	 * @return the swift branch
	 */
	public String getSwiftBranch() {
		return swiftBranch;
	}
	
	/**
	 * Sets the swift branch.
	 *
	 * @param swiftBranch the new swift branch
	 */
	public void setSwiftBranch(String swiftBranch) {
		this.swiftBranch = swiftBranch;
	}
	
	/**
	 * Gets the ch pool.
	 *
	 * @return the ch pool
	 */
	public Set getChPool() {
		return chPool;
	}
	
	/**
	 * Sets the ch pool.
	 *
	 * @param chPool the new ch pool
	 */
	public void setChPool(Set chPool) {
		this.chPool = chPool;
	}
	
	/**
	 * Gets the eot card bank code.
	 *
	 * @return the eot card bank code
	 */
	public String getEotCardBankCode() {
		return eotCardBankCode;
	}
	
	/**
	 * Sets the eot card bank code.
	 *
	 * @param eotCardBankCode the new eot card bank code
	 */
	public void setEotCardBankCode(String eotCardBankCode) {
		this.eotCardBankCode = eotCardBankCode;
	}
	
	/**
	 * Gets the eot charge.
	 *
	 * @return the eot charge
	 */
	public Float getEotCharge() {
		return eotCharge;
	}
	
	/**
	 * Sets the eot charge.
	 *
	 * @param eotCharge the new eot charge
	 */
	public void setEotCharge(Float eotCharge) {
		this.eotCharge = eotCharge;
	}
	
	/**
	 * Gets the terminal id.
	 *
	 * @return the terminal id
	 */
	public String getTerminalId() {
		return terminalId;
	}
	
	/**
	 * Sets the terminal id.
	 *
	 * @param terminalId the new terminal id
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	/**
	 * Gets the outlet number.
	 *
	 * @return the outlet number
	 */
	public String getOutletNumber() {
		return outletNumber;
	}
	
	/**
	 * Sets the outlet number.
	 *
	 * @param outletNumber the new outlet number
	 */
	public void setOutletNumber(String outletNumber) {
		this.outletNumber = outletNumber;
	}
	
	/**
	 * Gets the application name.
	 *
	 * @return the application name
	 */
	public String getApplicationName() {
		return applicationName;
	}
	
	/**
	 * Sets the application name.
	 *
	 * @param applicationName the new application name
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	/**
	 * Gets the collaboration logo.
	 *
	 * @return the collaboration logo
	 */
	public String getCollaborationLogo() {
		return collaborationLogo;
	}
	
	/**
	 * Sets the collaboration logo.
	 *
	 * @param collaborationLogo the new collaboration logo
	 */
	public void setCollaborationLogo(String collaborationLogo) {
		this.collaborationLogo = collaborationLogo;
	}
	
	/**
	 * Gets the total agrmnt.
	 *
	 * @return the total agrmnt
	 */
	public Integer getTotalAgrmnt() {
		return totalAgrmnt;
	}
	
	/**
	 * Sets the total agrmnt.
	 *
	 * @param totalAgrmnt the new total agrmnt
	 */
	public void setTotalAgrmnt(Integer totalAgrmnt) {
		this.totalAgrmnt = totalAgrmnt;
	}
	
	/**
	 * Gets the total CH.
	 *
	 * @return the total CH
	 */
	public Integer getTotalCH() {
		return totalCH;
	}
	
	/**
	 * Sets the total CH.
	 *
	 * @param totalCH the new total CH
	 */
	public void setTotalCH(Integer totalCH) {
		this.totalCH = totalCH;
	}
	
	/**
	 * Gets the templete id.
	 *
	 * @return the templete id
	 */
	public Integer getTempleteId() {
		return templeteId;
	}
	
	/**
	 * Sets the templete id.
	 *
	 * @param templeteId the new templete id
	 */
	public void setTempleteId(Integer templeteId) {
		this.templeteId = templeteId;
	}

}
