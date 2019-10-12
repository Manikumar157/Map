package com.eot.banking.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.eot.entity.TransactionRuleValues;


public class TransactionRulesDTO{	
	
	private String ruleName;
	
	private Integer timeZone;	
	@NotNull(message="NotNull.transactionRulesDTO.transactions")
	private Integer  transactions;
	
	private String transDesc;
	
	@NotNull(message="NotNull.transactionRulesDTO.maxValueLimit")
	private Long maxValueLimit;
	
	private Integer ruleType;
	
	private Integer ruleLevel;
	
	private Long approvalLimit;
	
	private Integer bankId;
	
	private String bankName;
	
	private String accountNumber;

    private Long transactionRuleId;
   
    private Long[] maxCumValueLimit;
    
    private Long[] maxNumTimes;
   
    private Integer[] allowedPer;
    
    private Integer[] allowedPerUnit;
    
    private Integer[] sourceType;
    
    private Set<TransactionRuleValues> trRuleValues;
    
    private Integer profileId;

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getTransactions() {
		return transactions;
	}

	public void setTransactions(Integer transactions) {
		this.transactions = transactions;
	}

	public String getTransDesc() {
		return transDesc;
	}

	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}

	public Long getTransactionRuleId() {
		return transactionRuleId;
	}

	public void setTransactionRuleId(Long transactionRuleId) {
		this.transactionRuleId = transactionRuleId;
	}

	public Long getMaxValueLimit() {
		return maxValueLimit;
	}

	public void setMaxValueLimit(Long maxValueLimit) {
		this.maxValueLimit = maxValueLimit;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getRuleLevel() {
		return ruleLevel;
	}

	public void setRuleLevel(Integer ruleLevel) {
		this.ruleLevel = ruleLevel;
	}


	public Long getApprovalLimit() {
		return approvalLimit;
	}

	public void setApprovalLimit(Long approvalLimit) {
		this.approvalLimit = approvalLimit;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Long[] getMaxCumValueLimit() {
		return maxCumValueLimit;
	}

	public void setMaxCumValueLimit(Long[] maxCumValueLimit) {
		this.maxCumValueLimit = maxCumValueLimit;
	}

	public Long[] getMaxNumTimes() {
		return maxNumTimes;
	}

	public void setMaxNumTimes(Long[] maxNumTimes) {
		this.maxNumTimes = maxNumTimes;
	}

	public Integer[] getAllowedPer() {
		return allowedPer;
	}

	public void setAllowedPer(Integer[] allowedPer) {
		this.allowedPer = allowedPer;
	}

	public Integer[] getAllowedPerUnit() {
		return allowedPerUnit;
	}

	public void setAllowedPerUnit(Integer[] allowedPerUnit) {
		this.allowedPerUnit = allowedPerUnit;
	}

	public Set<TransactionRuleValues> getTrRuleValues() {
		return trRuleValues;
	}

	public void setTrRuleValues(Set<TransactionRuleValues> trRuleValues) {
		this.trRuleValues = trRuleValues;
	}

	/**
	 * @return the sourceType
	 */
	public Integer[] getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(Integer[] sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
    
}
