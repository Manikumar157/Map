/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: SMSLogService.java
*
* Date Author Changes
* 17 Jun, 2016 Swadhin Created
*/
package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.SMSAlertDTO;
import com.eot.banking.dto.SMSCampaignDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;

/**
 * The Interface SMSLogService.
 */
public interface SMSLogService {
	
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
	 * @param mobNo the mob no
	 * @param pageNumber the page number
	 * @return the sMS log by mobile no
	 * @throws EOTException the eOT exception
	 */
	public Page getSMSLogByMobileNo(String mobNo,Integer pageNumber) throws EOTException;
	
	/**
	 * Chage sms log status.
	 *
	 * @param msgId the msg id
	 * @throws EOTException the eOT exception
	 */
	public void chageSMSLogStatus(Integer msgId) throws EOTException;
	
	/**
	 * Save or update sms alerts.
	 *
	 * @param smsAlertDTO the sms alert dto
	 * @throws EOTException the eOT exception
	 */
	public void saveOrUpdateSmsAlerts(SMSAlertDTO smsAlertDTO) throws EOTException;

	/**
	 * Search sms alert rules.
	 *
	 * @param userName the user name
	 * @param smsAlertDTO the sms alert dto
	 * @param model the model
	 * @throws EOTException the eOT exception
	 */
	void searchSmsAlertRules(String userName, SMSAlertDTO smsAlertDTO,Map<String, Object> model) throws EOTException;
	
	/**
	 * Gets the sms alert rule.
	 *
	 * @param smsAlertRuleId the sms alert rule id
	 * @return the sms alert rule
	 */
	public SMSAlertDTO getSmsAlertRule(Long smsAlertRuleId);

	/**
	 * Gets the master data.
	 *
	 * @param locale the locale
	 * @return the master data
	 * @throws EOTException the EOT exception
	 */
	public Map<String, List> getMasterData(String locale) throws EOTException;

	Page getSmsDetails(String mobileNumber, String fromDate, String toDate, Integer pageNumber) throws EOTException;

	public void sendSmsCampaign(SMSCampaignDTO smsCampaignDTO) throws EOTException,Exception;

	public Page getSmsCampaignData(SMSCampaignDTO smsCampaignDTO, Integer pageNumber);

}
