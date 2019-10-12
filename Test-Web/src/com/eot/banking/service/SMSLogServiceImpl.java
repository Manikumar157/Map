/* Copyright EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: SMSLogServiceImpl.java
 *
 * Date Author Changes
 * 17 Jun, 2016 Swadhin Created
 */
package com.eot.banking.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.SMSLogDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.SMSAlertDTO;
import com.eot.banking.dto.SMSCampaignDTO;
import com.eot.banking.dto.SmsDetailsDto;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.Page;
import com.eot.entity.SmsAlertRule;
import com.eot.entity.SmsAlertRuleValue;
import com.eot.entity.SmsAlertRulesTxn;
import com.eot.entity.SmsCampaign;
import com.eot.entity.SmsLog;
import com.eot.entity.TransactionType;
import com.eot.entity.WebUser;

/**
 * The Class SMSLogServiceImpl.
 */
@Service("smsLogService")
@Transactional(readOnly=true)
public class SMSLogServiceImpl implements SMSLogService {

	/** The sms log dao. */
	@Autowired
	private SMSLogDao smsLogDao;	

	/** The web user dao. */
	@Autowired
	private WebUserDao webUserDao;
	/** The app configurations. */
	@Autowired
	private AppConfigurations appConfigurations;

	/* (non-Javadoc)
	 * @see com.eot.banking.service.SMSLogService#getSMSLogList(java.lang.Integer)
	 */
	public Page getSMSLogList(Integer pageNumber) throws EOTException{
		Page page=smsLogDao.getSMSLogList(pageNumber);		
		if(page.results.size()==0)
			throw new EOTException(ErrorConstants.SMSLOG_UNAVAILABLE);
		//		Page page=PaginationHelper.getPage(smslog, appConfigurations.getResultsPerPage(), pageNumber);

		return page;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.SMSLogService#getSMSLogByMobileNo(java.lang.String, java.lang.Integer)
	 */
	public Page getSMSLogByMobileNo(String mobNo,Integer pageNumber) throws EOTException{
		Page page=smsLogDao.getSMSLogByMobileNo(mobNo, pageNumber);
		if(page.results.size()==0)
			throw new EOTException(ErrorConstants.SMSLOG_UNAVAILABLE);
		//		Page page=PaginationHelper.getPage(smslog, appConfigurations.getResultsPerPage(), pageNumber);
		return page;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.SMSLogService#chageSMSLogStatus(java.lang.Integer)
	 */
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
	public void chageSMSLogStatus(Integer msgId) throws EOTException{
		smsLogDao.chageSMSLogStatus(msgId);
	}

	/**
	 * Save or update sms alerts.
	 *
	 * @param smsAlertDTO the sms alert dto
	 * @throws EOTException the eOT exception
	 */
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
	public void saveOrUpdateSmsAlerts(SMSAlertDTO smsAlertDTO) throws EOTException {

		List<SmsAlertRule> smsAlertRules = smsLogDao.getSmsAlertRulesByRuleName(smsAlertDTO.getPackageName());
		SmsAlertRule smsAlertRule = null;

		if(smsAlertDTO.getSmsAlertRuleId()!=null){
			smsAlertRule = smsLogDao.getSmsAlertRulesByRuleId(smsAlertDTO.getSmsAlertRuleId());
			if(!smsAlertRule.getSmsAlertRuleName().equalsIgnoreCase(smsAlertDTO.getPackageName())){
				if(smsAlertRules.size()>0){
					throw new EOTException(ErrorConstants.SMS_ALERT_RULE_EXIST);}
			}			
		} else{
			if(smsAlertRules.size()>0){
				throw new EOTException(ErrorConstants.SMS_ALERT_RULE_EXIST);
			}
		}
		if(smsAlertDTO.getSmsAlertRuleId() == null){
			smsAlertRule = new SmsAlertRule();
		} else {
			smsAlertRule = smsLogDao.getSmsAlertRulesByRuleId(smsAlertDTO.getSmsAlertRuleId());
			smsLogDao.deleteSmsAlertValue(smsAlertDTO.getSmsAlertRuleId());
			smsLogDao.deleteSmsAlertRulesTxn(smsAlertDTO.getSmsAlertRuleId());
		}
		smsAlertRule.setCostPerSms(smsAlertDTO.getCostPerSms());
		smsAlertRule.setReferenceId(0);
		smsAlertRule.setRuleLevel(EOTConstants.RULE_LEVEL_GLOBAL);
		smsAlertRule.setSmsAlertRuleName(smsAlertDTO.getPackageName());

		smsLogDao.saveOrUpdate(smsAlertRule);

		saveSmsAlertValueList(smsAlertDTO.getSubscription(), smsAlertDTO.getCostPerPackage(), smsAlertDTO.getNumberOfSMS(), smsAlertRule);
		saveSmsAlertRulesTxn(smsAlertDTO.getTransactions(), smsAlertRule);
	}

	/**
	 * Save sms alert value list.
	 *
	 * @param subscriptionType the subscription type
	 * @param costPerPackage the cost per package
	 * @param numberOfSMS the number of sms
	 * @param smsAlertRule the sms alert rule
	 */
	private void saveSmsAlertValueList(Integer[] subscriptionType, Double[] costPerPackage, Integer[] numberOfSMS, SmsAlertRule smsAlertRule){

		for (int i = 0; i < subscriptionType.length; i++){

			SmsAlertRuleValue smsAlertRuleValue = new SmsAlertRuleValue();

			smsAlertRuleValue.setSubscriptionType(subscriptionType[i]);
			smsAlertRuleValue.setCostPerPackage(costPerPackage[i]);
			smsAlertRuleValue.setNumberOfSms(numberOfSMS[i]);
			smsAlertRuleValue.setSmsalertrule(smsAlertRule);

			smsLogDao.save(smsAlertRuleValue);
		}
	}

	/**
	 * Save sms alert rules txn.
	 *
	 * @param transactions the transactions
	 * @param smsAlertRule the sms alert rule
	 */
	private void saveSmsAlertRulesTxn(Integer[] transactions, SmsAlertRule smsAlertRule){

		for(int i = 0; i < transactions.length; i++){

			SmsAlertRulesTxn smsAlertRulesTxn = new SmsAlertRulesTxn();

			TransactionType transactionType = new TransactionType();
			transactionType.setTransactionType(transactions[i]);

			smsAlertRulesTxn.setTransactionType(transactionType);
			smsAlertRulesTxn.setSmsAlertRule(smsAlertRule);
			smsLogDao.save(smsAlertRulesTxn);
		}
	}

	/**
	 * Gets the sms alert rule.
	 *
	 * @param smsAlertRuleId the sms alert rule id
	 * @return the sms alert rule
	 */
	@SuppressWarnings("unchecked")
	public SMSAlertDTO getSmsAlertRule(Long smsAlertRuleId){

		SmsAlertRule smsAlertRule = smsLogDao.getSmsAlertRulesByRuleId(smsAlertRuleId);

		SMSAlertDTO smsAlertDTO = new SMSAlertDTO();
		smsAlertDTO.setCostPerSms(smsAlertRule.getCostPerSms());
		smsAlertDTO.setPackageName(smsAlertRule.getSmsAlertRuleName());
		smsAlertDTO.setSmsAlertRuleId(smsAlertRuleId);
		smsAlertDTO.setSmsalertrulestxns(smsAlertRule.getSmsalertrulestxns());
		smsAlertDTO.setSmsalertrulevalues(smsAlertRule.getSmsalertrulevalues());
		
		Set<SmsAlertRulesTxn> transactions=smsAlertRule.getSmsalertrulestxns();
		if(transactions.size()!=0){
			Integer[] txntype=new Integer[transactions.size()];int i=0;
			for (SmsAlertRulesTxn txn : transactions) {			
				txntype[i]=txn.getTransactionType().getTransactionType();			
				i++;
			}
			smsAlertDTO.setTransactions(txntype);	
		}
		return smsAlertDTO;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.SMSLogService#searchSmsAlertRules(java.lang.String, com.eot.banking.dto.SMSAlertDTO, java.util.Map)
	 */
	@Override
	public void searchSmsAlertRules(String userName, SMSAlertDTO smsAlertDTO, Map<String, Object> model) throws EOTException {

		WebUser user = webUserDao.getUser(userName);
		if( user == null ){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		int referenceId = 0 ;

		Page page = smsLogDao.searchSmsAlertRules(smsAlertDTO.getRuleLevel(),referenceId,smsAlertDTO.getSubscriptionType(),smsAlertDTO.getPageNumber());
		page.requestPage = "searchSmsAlertRules.htm";
		model.put("page", page);
		model.put("smsAlertDTO", smsAlertDTO);
	}

	@Override
	public Map<String, List> getMasterData(String locale) throws EOTException {
		Map<String,List> model = new HashMap<String,List>();
		System.out.println("transactionTypeList: "+smsLogDao.getTransactionTypesWithSmsAlert(locale));
		model.put("transTypeList", smsLogDao.getTransactionTypesWithSmsAlert(locale));		
		return model;
	}
	
	
	@Override
	public Page getSmsDetails(String mobileNumber, String fromDate,String toDate, Integer pageNumber) throws EOTException {
		return smsLogDao.getSMSLogDetails(mobileNumber,fromDate,toDate, pageNumber);		
	}

	@Override
	@Transactional(readOnly=false)
	public void sendSmsCampaign(SMSCampaignDTO smsCampaignDTO) throws EOTException,Exception {
				
		
		List<String> mobileNumbers=new ArrayList<String>();
		if(!smsCampaignDTO.getTarget().equalsIgnoreCase(EOTConstants.TARGET_TYPE_CUSTOM)) {
			mobileNumbers = smsLogDao.getMobileNumbersSmsCampaign(smsCampaignDTO.getTarget());
		}else {
			String[] mobile = smsCampaignDTO.getMobileNumbers().split(",");
			for(String number:mobile) {
				mobileNumbers.add(number);
			}
		}
		for(String number:mobileNumbers) {
			SmsLog smsLog = new SmsLog();
			smsLog.setMobileNumber(EOTConstants.COUNTRY_CODE_SOUTH_SUDAN+number);
			smsLog.setMessageType(EOTConstants.MESSAGE_TYPE_BULK_MESSAGE);
			smsLog.setEncoding(1);
			smsLog.setCreatedDate(new Date());
			smsLog.setMessage(smsCampaignDTO.getMessage());
			smsLog.setScheduledDate(new Date());
			smsLog.setStatus(0);
			smsLogDao.save(smsLog);
		}
		SmsCampaign smsCampaign = new SmsCampaign();
		smsCampaign.setTitle(smsCampaignDTO.getTitle());
		smsCampaign.setMessage(smsCampaignDTO.getMessage());
		smsCampaign.setTarget(smsCampaignDTO.getTarget());
		smsCampaign.setCreatedDate(new Date());
		smsLogDao.save(smsCampaign);
	}

	@Override
	public Page getSmsCampaignData(SMSCampaignDTO smsCampaignDTO, Integer pageNumber) {
		Page page = smsLogDao.getSmsCampaignData(smsCampaignDTO,pageNumber);
		return page;
	}
}
