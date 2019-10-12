/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: SMSAlertDTO.java
*
* Date Author Changes
* 17 Jun, 2016 Swadhin Created
*/
package com.eot.banking.dto;

import java.util.Set;

import com.eot.entity.SmsAlertRuleValue;
import com.eot.entity.SmsAlertRulesTxn;

// TODO: Auto-generated Javadoc
/**
 * The Class SMSAlertDTO.
 */
public class SMSAlertDTO{	
	
	/** The sms alert rule id. */
	private Long smsAlertRuleId;
	
	/** The rule level. */
	private Integer ruleLevel ;
	
	/** The page number. */
	private Integer pageNumber = -1 ;
	
	/** The package name. */
	private String packageName;
	
	/** The cost per sms. */
	private Double costPerSms;
	
	/** The subscription type. */
	private Integer subscriptionType;
	
	/** The transactions. */
	private Integer[] transactions;
	
	/** The subscription type. */
	private Integer[] subscription;
	
	/** The cost per package. */
	private Double[] costPerPackage;
	
	/** The number of sms. */
	private Integer[] numberOfSMS;

    /** The smsalertrulevalues. */
    private Set<SmsAlertRuleValue> smsalertrulevalues;

    /** The smsalertrulestxns. */
    private Set<SmsAlertRulesTxn> smsalertrulestxns;
    
	/**
	 * Gets the package name.
	 *
	 * @return the package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Sets the package name.
	 *
	 * @param packageName the new package name
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Gets the cost per sms.
	 *
	 * @return the cost per sms
	 */
	public Double getCostPerSms() {
		return costPerSms;
	}

	/**
	 * Sets the cost per sms.
	 *
	 * @param costPerSms the new cost per sms
	 */
	public void setCostPerSms(Double costPerSms) {
		this.costPerSms = costPerSms;
	}

	/**
	 * Gets the transactions.
	 *
	 * @return the transactions
	 */
	public Integer[] getTransactions() {
		return transactions;
	}

	/**
	 * Sets the transactions.
	 *
	 * @param transactions the new transactions
	 */
	public void setTransactions(Integer[] transactions) {
		this.transactions = transactions;
	}

	/**
	 * Gets the sms alert rule id.
	 *
	 * @return the sms alert rule id
	 */
	public Long getSmsAlertRuleId() {
		return smsAlertRuleId;
	}

	/**
	 * Sets the sms alert rule id.
	 *
	 * @param smsAlertRuleId the new sms alert rule id
	 */
	public void setSmsAlertRuleId(Long smsAlertRuleId) {
		this.smsAlertRuleId = smsAlertRuleId;
	}

	/**
	 * Gets the cost per package.
	 *
	 * @return the cost per package
	 */
	public Double[] getCostPerPackage() {
		return costPerPackage;
	}

	/**
	 * Sets the cost per package.
	 *
	 * @param costPerPackage the new cost per package
	 */
	public void setCostPerPackage(Double[] costPerPackage) {
		this.costPerPackage = costPerPackage;
	}

	/**
	 * Gets the number of sms.
	 *
	 * @return the number of sms
	 */
	public Integer[] getNumberOfSMS() {
		return numberOfSMS;
	}

	/**
	 * Sets the number of sms.
	 *
	 * @param numberOfSMS the new number of sms
	 */
	public void setNumberOfSMS(Integer[] numberOfSMS) {
		this.numberOfSMS = numberOfSMS;
	}

	/**
	 * Gets the subscription.
	 *
	 * @return the subscription
	 */
	public Integer[] getSubscription() {
		return subscription;
	}

	/**
	 * Sets the subscription.
	 *
	 * @param subscription the new subscription
	 */
	public void setSubscription(Integer[] subscription) {
		this.subscription = subscription;
	}

	/**
	 * Gets the smsalertrulevalues.
	 *
	 * @return the smsalertrulevalues
	 */
	public Set<SmsAlertRuleValue> getSmsalertrulevalues() {
		return smsalertrulevalues;
	}

	/**
	 * Sets the smsalertrulevalues.
	 *
	 * @param smsalertrulevalues the new smsalertrulevalues
	 */
	public void setSmsalertrulevalues(Set<SmsAlertRuleValue> smsalertrulevalues) {
		this.smsalertrulevalues = smsalertrulevalues;
	}

	/**
	 * Gets the smsalertrulestxns.
	 *
	 * @return the smsalertrulestxns
	 */
	public Set<SmsAlertRulesTxn> getSmsalertrulestxns() {
		return smsalertrulestxns;
	}

	/**
	 * Sets the smsalertrulestxns.
	 *
	 * @param smsalertrulestxns the new smsalertrulestxns
	 */
	public void setSmsalertrulestxns(Set<SmsAlertRulesTxn> smsalertrulestxns) {
		this.smsalertrulestxns = smsalertrulestxns;
	}

	/**
	 * Gets the rule level.
	 *
	 * @return the rule level
	 */
	public Integer getRuleLevel() {
		return ruleLevel;
	}

	/**
	 * Sets the rule level.
	 *
	 * @param ruleLevel the new rule level
	 */
	public void setRuleLevel(Integer ruleLevel) {
		this.ruleLevel = ruleLevel;
	}

	/**
	 * Gets the page number.
	 *
	 * @return the page number
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * Sets the page number.
	 *
	 * @param pageNumber the new page number
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * Gets the subscription type.
	 *
	 * @return the subscription type
	 */
	public Integer getSubscriptionType() {
		return subscriptionType;
	}

	/**
	 * Sets the subscription type.
	 *
	 * @param subscriptionType the new subscription type
	 */
	public void setSubscriptionType(Integer subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

}
