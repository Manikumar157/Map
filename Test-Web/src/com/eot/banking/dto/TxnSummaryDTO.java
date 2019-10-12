package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Admin PC
 *
 */
public class TxnSummaryDTO implements Serializable {

	private static final long serialVersionUID = 5857062752082412924L;

	private Integer bankId;
	private Integer bank;
	private Integer countryId;
	private Integer transactionType;
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date fromDate;
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date toDate;
	private String chartType;
	private String imgType;
	private String accountNumber;
	private Integer reportType=0;
	private Integer clearingPoolId;
	private String bankName;

	private String mobileNumber;
	private String customerName;
	private Integer bankGroupId;
	private Integer profileId;
	private Long branchId;
	private Long branch;
	private String amount;
	private String userFirstName;
	
	private String partnerType;
	private String partnerId;
	private String actionType; 
	private String agentCode;
	private String status;
	private String requestChannel;
	private String sortBy;
	private String sortColumn;
	private int index;
	private String txnId;
	
	private String superAgentCode;
	private String superAgentName;
	private String agentName;
	private String agentMobileNumber;
	private String merchantCode;
	
	private String name;
	private String webUserCode;
	private String benfOrCustMobileNumber;
	private String businessName;
	private String loginUserName;
	
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public Integer getBank() {
		return bank;
	}
	public void setBank(Integer bank) {
		this.bank = bank;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Long getBranch() {
		return branch;
	}
	public void setBranch(Long branch) {
		this.branch = branch;
	}
	public String userId;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getBankGroupId() {
		return bankGroupId;
	}
	public void setBankGroupId(Integer bankGroupId) {
		this.bankGroupId = bankGroupId;
	}
	public Integer getProfileId() {
		return profileId;
	}
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Integer getClearingPoolId() {
		return clearingPoolId;
	}
	public void setClearingPoolId(Integer clearingPoolId) {
		this.clearingPoolId = clearingPoolId;
	}
	public Integer getReportType() {
		return reportType;
	}
	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Integer getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
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
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public String getPartnerType() {
		return partnerType;
	}
	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRequestChannel() {
		return requestChannel;
	}
	public void setRequestChannel(String requestChannel) {
		this.requestChannel = requestChannel;
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
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getSuperAgentCode() {
		return superAgentCode;
	}
	public void setSuperAgentCode(String superAgentCode) {
		this.superAgentCode = superAgentCode;
	}
	public String getSuperAgentName() {
		return superAgentName;
	}
	public void setSuperAgentName(String superAgentName) {
		this.superAgentName = superAgentName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentMobileNumber() {
		return agentMobileNumber;
	}
	public void setAgentMobileNumber(String agentMobileNumber) {
		this.agentMobileNumber = agentMobileNumber;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWebUserCode() {
		return webUserCode;
	}
	public void setWebUserCode(String webUserCode) {
		this.webUserCode = webUserCode;
	}
	public String getBenfOrCustMobileNumber() {
		return benfOrCustMobileNumber;
	}
	public void setBenfOrCustMobileNumber(String benfOrCustMobileNumber) {
		this.benfOrCustMobileNumber = benfOrCustMobileNumber;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	/**
	 * @return the loginUserName
	 */
	public String getLoginUserName() {
		return loginUserName;
	}
	/**
	 * @param loginUserName the loginUserName to set
	 */
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	
}
