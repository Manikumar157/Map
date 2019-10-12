package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.TransactionRulesDTO;
import com.eot.banking.dto.TxnRuleSearchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.Bank;
import com.eot.entity.CustomerProfiles;

public interface TransactionRulesService {

	public Map<String,List> getMasterData(String locale)throws EOTException;
	public List<CustomerProfiles> getCustomerProfiles()throws EOTException;
	public void saveOrUpdateTransactionRules(TransactionRulesDTO transactionRulesDTO) throws EOTException;	
	public void getTransactionRuleDetails(Long trRuleId,Map<String,Object> model,String locale) throws EOTException;
	public List<Bank> getBanksByGroupId(Integer bankGroupId) throws EOTException ;
	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId)throws EOTException;
	
	public void searchTransactionRules(String userName,TxnRuleSearchDTO scSearchDTO , Map<String,Object> model) throws EOTException;
	
}
