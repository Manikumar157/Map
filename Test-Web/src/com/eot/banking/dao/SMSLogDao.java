/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: SMSLogDao.java
*
* Date Author Changes
* 17 Jun, 2016 Swadhin Created
*/
package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.SMSCampaignDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.SmsAlertRule;

/**
 * The Interface SMSLogDao.
 */
public interface SMSLogDao extends BaseDao{
	
	/**
	 * Gets the sMS log list.
	 *
	 * @param pageNumber the page number
	 * @return the sMS log list
	 * @throws EOTException the eOT exception
	 */
	public Page getSMSLogList(Integer pageNumber) throws EOTException;
	
	/**
	 * Gets the sMS log by mobile no.
	 *
	 * @param mobileNo the mobile no
	 * @param pageNumber the page number
	 * @return the sMS log by mobile no
	 * @throws EOTException the eOT exception
	 */
	public Page getSMSLogByMobileNo(String mobileNo,Integer pageNumber) throws EOTException;
	
	/**
	 * Chage sms log status.
	 *
	 * @param msgId the msg id
	 * @throws EOTException the eOT exception
	 */
	public void chageSMSLogStatus(Integer msgId) throws EOTException;
	
	/**
	 * Gets the sms alert rules by rule name.
	 *
	 * @param packageName the package name
	 * @return the sms alert rules by rule name
	 */
	public List<SmsAlertRule> getSmsAlertRulesByRuleName(String packageName);

	/**
	 * Gets the sms alert rules by rule id.
	 *
	 * @param smsAlertRuleId the sms alert rule id
	 * @return the sms alert rules by rule id
	 */
	public SmsAlertRule getSmsAlertRulesByRuleId(Long smsAlertRuleId);

	/**
	 * Delete sms alert value.
	 *
	 * @param smsAlertRuleId the sms alert rule id
	 */
	public void deleteSmsAlertValue(Long smsAlertRuleId);

	/**
	 * Delete sms alert rules txn.
	 *
	 * @param smsAlertRuleId the sms alert rule id
	 */
	public void deleteSmsAlertRulesTxn(Long smsAlertRuleId);

	/**
	 * Search sms alert rules.
	 *
	 * @param ruleLevel the rule level
	 * @param referenceId the reference id
	 * @param subscriptionType the subscription type
	 * @param pageNumber the page number
	 * @return the page
	 */
	public Page searchSmsAlertRules(Integer ruleLevel, int referenceId, Integer subscriptionType, Integer pageNumber);

	/**
	 * Gets the transaction types with sms alert.
	 *
	 * @param locale the locale
	 * @return the transaction types with sms alert
	 */
	public List getTransactionTypesWithSmsAlert(String locale);


	Page getSMSLogDetails(String mobileNo, String fromDate, String toDate, Integer pageNumber);

	public List<String> getMobileNumbersSmsCampaign(String target);

	public Page getSmsCampaignData(SMSCampaignDTO smsCampaignDTO, Integer pageNumber);
}
