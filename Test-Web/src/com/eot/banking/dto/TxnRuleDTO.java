package com.eot.banking.dto;

public class TxnRuleDTO {
	
	private Integer ruleType;
	private Integer ruleLevel;
    private Long maxValueLimit;
    private Long maxCumValueLimit;
    private Long maxNumTimes;
    private Integer allowedPer;
    private Integer allowedPerUnit;
    private Long transactionRuleID;
    
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
	public Long getMaxValueLimit() {
		return maxValueLimit;
	}
	public void setMaxValueLimit(Long maxValueLimit) {
		this.maxValueLimit = maxValueLimit;
	}
	public Long getMaxCumValueLimit() {
		return maxCumValueLimit;
	}
	public void setMaxCumValueLimit(Long maxCumValueLimit) {
		this.maxCumValueLimit = maxCumValueLimit;
	}
	public Long getMaxNumTimes() {
		return maxNumTimes;
	}
	public void setMaxNumTimes(Long maxNumTimes) {
		this.maxNumTimes = maxNumTimes;
	}
	public Integer getAllowedPer() {
		return allowedPer;
	}
	public void setAllowedPer(Integer allowedPer) {
		this.allowedPer = allowedPer;
	}
	public Integer getAllowedPerUnit() {
		return allowedPerUnit;
	}
	public void setAllowedPerUnit(Integer allowedPerUnit) {
		this.allowedPerUnit = allowedPerUnit;
	}
	public Long getTransactionRuleID() {
		return transactionRuleID;
	}
	public void setTransactionRuleID(Long transactionRuleID) {
		this.transactionRuleID = transactionRuleID;
	}

}
