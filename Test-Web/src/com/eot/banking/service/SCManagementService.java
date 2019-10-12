package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.SCSearchDTO;
import com.eot.banking.dto.ServiceChargeDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.Bank;
import com.eot.entity.CustomerProfiles;

public interface SCManagementService {

	public Map<String,List> getMasterData(String locale)throws EOTException;
	public List<CustomerProfiles> getCustomerProfiles()throws EOTException;
	public void saveOrUpdateServiceCharge(ServiceChargeDTO serviceChargeDTO) throws EOTException;	
	public void getServiceChargeRule(Long scRuleId,Map<String,Object> model,String locale) throws EOTException;
	public List<Bank> getBanksByGroupId(Integer bankGroupId) throws EOTException ;
	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId)throws EOTException;
	
	public void searchServiceChargeRules(String userName,SCSearchDTO scSearchDTO , Map<String,Object> model) throws EOTException;
	
}
