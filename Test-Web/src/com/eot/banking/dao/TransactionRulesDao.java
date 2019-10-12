package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.TxnRuleDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.TransactionRule;
import com.eot.entity.TransactionRuleValues;
import com.eot.entity.TransactionType;

public interface TransactionRulesDao extends BaseDao {
	
	public List<TransactionType> getTransactionTypesWithTxnRules(String locale);
	public List<TransactionType> getTransactionTypesWithServiceCharge(String locale);
	public List<Bank> getRevenueSharingBanks();
	public TransactionRule getTransactionRule(Long transactionRuleId);
	public TransactionRule getTransactionRule(Integer transactionType,Integer sourceType);
	public Page searchTransactionRules(int ruleLevel, int referenceId, int pageNumber);	
	public void deleteTransactionRuleTxns(Long transactionRuleId) throws EOTException;
	public void deleteTransactionRuleValues(Long transactionRuleId);	
	public List<TransactionRule> getTransactionRulesByRuleLevel(Integer ruleLevel);
	public TransactionRuleValues getTransactionRuleValues(Long transactionRuleId,Integer allowedPerUnit);
	TxnRuleDTO getTransactionRules(Integer ruleLevel, Integer sourceType,Integer transactionType, Integer ruleType);
	List<TransactionType> loadFilteredTransactionTypesWithServiceCharge(String locale);
	public TransactionType getTransactionTypeById(Integer txnTypeID);
	Page searchTransactionRulesWithProfile(int ruleLevel, int referenceId, int pageNumber);
	
	
}
