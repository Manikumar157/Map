package com.eot.banking.dto;

import java.util.Set;

import com.eot.entity.SmsAlertRuleValue;
import com.eot.entity.SmsAlertRulesTxn;

// TODO: Auto-generated Javadoc
/**
 * The Class SmsSubscriptionDTO.
 */
public class SmsSubscriptionDTO {
	
	/** The customer id. */
	private Long customerId;  
	  /** The sms alert rule id. */
  	private Long smsAlertRuleId;
	  
	  /** The sms alert rule name. */
  	private String smsAlertRuleName;
	  
	  /** The cost per sms. */
  	private double costPerSms;
	  
	  /** The rule level. */
  	private int ruleLevel;
	  
	  /** The reference id. */
  	private int referenceId;
	  
	  /** The smsalertrulevalues. */
  	private Set<SmsAlertRuleValue> smsalertrulevalues;
	  
	  /** The smsalertrulestxns. */
  	private Set<SmsAlertRulesTxn> smsalertrulestxns;
  	

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public Long getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customer id.
	 *
	 * @param customerId the new customer id
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
	 * Gets the sms alert rule name.
	 *
	 * @return the sms alert rule name
	 */
	public String getSmsAlertRuleName() {
		return smsAlertRuleName;
	}

	/**
	 * Sets the sms alert rule name.
	 *
	 * @param smsAlertRuleName the new sms alert rule name
	 */
	public void setSmsAlertRuleName(String smsAlertRuleName) {
		this.smsAlertRuleName = smsAlertRuleName;
	}

	/**
	 * Gets the cost per sms.
	 *
	 * @return the cost per sms
	 */
	public double getCostPerSms() {
		return costPerSms;
	}

	/**
	 * Sets the cost per sms.
	 *
	 * @param costPerSms the new cost per sms
	 */
	public void setCostPerSms(double costPerSms) {
		this.costPerSms = costPerSms;
	}

	/**
	 * Gets the rule level.
	 *
	 * @return the rule level
	 */
	public int getRuleLevel() {
		return ruleLevel;
	}

	/**
	 * Sets the rule level.
	 *
	 * @param ruleLevel the new rule level
	 */
	public void setRuleLevel(int ruleLevel) {
		this.ruleLevel = ruleLevel;
	}

	/**
	 * Gets the reference id.
	 *
	 * @return the reference id
	 */
	public int getReferenceId() {
		return referenceId;
	}

	/**
	 * Sets the reference id.
	 *
	 * @param referenceId the new reference id
	 */
	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
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
	  
}
