package com.eot.banking.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.eot.entity.ScapplicableTime;
import com.eot.entity.ServiceChargeRuleTxn;
import com.eot.entity.ServiceChargeRuleValue;
import com.eot.entity.ServiceChargeSubscription;
import com.eot.entity.TimeZone;

public class ServiceChargeDTO{	
	
	private Long serviceChargeRuleId; 
	
	private Long subscriptionChargeRuleId;
	
	@NotEmpty(message="NotEmpty.serviceChargeDTO.ruleName")
	private String ruleName;
	
	private Integer ruleLevel = 1 ;
	
	private Integer bankId;
	
	private String bankName;
	
	private String accountNumber;
	
	@NotNull(message="NotNull.serviceChargeDTO.transactions")
	private Integer[]  transactions;
	
	//@NotNull(message="NotNull.serviceChargeDTO.applicableFrom")
	//@DateTimeFormat(pattern="dd/MM/yyyy") 
	private String applicableFrom;
	
	//@NotNull(message="NotNull.serviceChargeDTO.applicableTo")
	//@DateTimeFormat(pattern="dd/MM/yyyy") 
	private String applicableTo;
	
	@NotNull(message="NotNull.serviceChargeDTO.timeZoneId")
	private Integer timeZoneId;	

	private Integer imposedOn;

	private Long[] minTxnValue;
	
	private Long[] maxTxnValue;
	
	private Float[] scPercentage;
	
	private Long[] scFixed;

	private Long[] discountLimit;
	
	private Long[] minSC;
	
	private Long[] maxSC;
	
	private Integer[] sourceType;
	
	private Integer[] days;	
		
	private Integer[] fromHours;
			
	private Integer[] toHours;
	
	private Integer[] subscription;
	
	private Double[] costPerPackage;
	
	private Integer[] noOfTxn;
	
	private List<ServiceChargeRuleValue> scRuleValue;
	
	private Set<ScapplicableTime> scDays;
	
	private Set<ServiceChargeRuleTxn> scRuleTxns;
	
	private Set<ServiceChargeSubscription> scSubscriptions;
	
	private TimeZone timeZone ;
	
	private Integer profileId;

	public Long getServiceChargeRuleId() {
		return serviceChargeRuleId;
	}

	public void setServiceChargeRuleId(Long serviceChargeRuleId) {
		this.serviceChargeRuleId = serviceChargeRuleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer[] getTransactions() {
		return transactions;
	}

	public void setTransactions(Integer[] transactions) {
		this.transactions = transactions;
	}

	public String getApplicableFrom() {
		return applicableFrom;
	}

	public void setApplicableFrom(String applicableFrom) {
		this.applicableFrom = applicableFrom;
	}

	public String getApplicableTo() {
		return applicableTo;
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

	public void setApplicableTo(String applicableTo) {
		this.applicableTo = applicableTo;
	}

	public Integer getImposedOn() {
		return imposedOn;
	}

	public void setImposedOn(Integer imposedOn) {
		this.imposedOn = imposedOn;
	}

	public Long[] getMinTxnValue() {
		return minTxnValue;
	}

	public void setMinTxnValue(Long[] minTxnValue) {
		this.minTxnValue = minTxnValue;
	}

	public Long[] getMaxTxnValue() {
		return maxTxnValue;
	}

	public void setMaxTxnValue(Long[] maxTxnValue) {
		this.maxTxnValue = maxTxnValue;
	}

	public Float[] getScPercentage() {
		return scPercentage;
	}

	public void setScPercentage(Float[] scPercentage) {
		this.scPercentage = scPercentage;
	}

	public Long[] getScFixed() {
		return scFixed;
	}

	public void setScFixed(Long[] scFixed) {
		this.scFixed = scFixed;
	}

	public Long[] getDiscountLimit() {
		return discountLimit;
	}

	public void setDiscountLimit(Long[] discountLimit) {
		this.discountLimit = discountLimit;
	}

	public Long[] getMinSC() {
		return minSC;
	}

	public void setMinSC(Long[] minSC) {
		this.minSC = minSC;
	}

	public Long[] getMaxSC() {
		return maxSC;
	}

	public void setMaxSC(Long[] maxSC) {
		this.maxSC = maxSC;
	}

	public Integer[] getDays() {
		return days;
	}

	public void setDays(Integer[] days) {
		this.days = days;
	}

	public Integer[] getFromHours() {
		return fromHours;
	}

	public void setFromHours(Integer[] fromHours) {
		this.fromHours = fromHours;
	}

	public Integer[] getToHours() {
		return toHours;
	}

	public void setToHours(Integer[] toHours) {
		this.toHours = toHours;
	}
	
	public List<ServiceChargeRuleValue> getScRuleValue() {
		return scRuleValue;
	}

	public void setScRuleValue(List<ServiceChargeRuleValue> scRuleValue) {
		this.scRuleValue = scRuleValue;
	}

	public Set<ScapplicableTime> getScDays() {
		return scDays;
	}

	public void setScDays(Set<ScapplicableTime> scDays) {
		this.scDays = scDays;
	}

	public Set<ServiceChargeRuleTxn> getScRuleTxns() {
		return scRuleTxns;
	}

	public void setScRuleTxns(Set<ServiceChargeRuleTxn> scRuleTxns) {
		this.scRuleTxns = scRuleTxns;
	}

	public Integer getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(Integer timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	public Integer getRuleLevel() {
		return ruleLevel;
	}

	public void setRuleLevel(Integer ruleLevel) {
		this.ruleLevel = ruleLevel;
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

	public Long getSubscriptionChargeRuleId() {
		return subscriptionChargeRuleId;
	}

	public void setSubscriptionChargeRuleId(Long subscriptionChargeRuleId) {
		this.subscriptionChargeRuleId = subscriptionChargeRuleId;
	}



	public Double[] getCostPerPackage() {
		return costPerPackage;
	}

	public void setCostPerPackage(Double[] costPerPackage) {
		this.costPerPackage = costPerPackage;
	}

	public Integer[] getNoOfTxn() {
		return noOfTxn;
	}

	public void setNoOfTxn(Integer[] noOfTxn) {
		this.noOfTxn = noOfTxn;
	}

	public Set<ServiceChargeSubscription> getScSubscriptions() {
		return scSubscriptions;
	}

	public void setScSubscriptions(Set<ServiceChargeSubscription> scSubscriptions) {
		this.scSubscriptions = scSubscriptions;
	}

	public Integer[] getSubscription() {
		return subscription;
	}

	public void setSubscription(Integer[] subscription) {
		this.subscription = subscription;
	}

	
}
