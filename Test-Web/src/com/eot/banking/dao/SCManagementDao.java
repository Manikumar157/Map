package com.eot.banking.dao;

import java.util.List;
import java.util.Set;

import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.ServiceChargeRule;
import com.eot.entity.ServiceChargeRuleTxn;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.TransactionType;

// TODO: Auto-generated Javadoc
/**
 * The Interface SCManagementDao.
 */
public interface SCManagementDao extends BaseDao{	

	/**
	 * Search service charge rules.
	 *
	 * @param ruleLevel the rule level
	 * @param referenceId the reference id
	 * @param pageNumber the page number
	 * @return the page
	 */
	public Page searchServiceChargeRules(int ruleLevel, int referenceId, int pageNumber);
	
	/**
	 * Gets the customer profiles by bank id.
	 *
	 * @param bankId the bank id
	 * @return the customer profiles by bank id
	 * @throws EOTException the EOT exception
	 */
	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId) throws EOTException;
	
	/**
	 * Gets the customer profiles by bank ids.
	 *
	 * @param bankIds the bank ids
	 * @return the customer profiles by bank ids
	 * @throws EOTException the EOT exception
	 */
	public List<CustomerProfiles> getCustomerProfilesByBankIds(Set<Integer> bankIds) throws EOTException;
	
	/**
	 * Gets the bank group id by bank id.
	 *
	 * @param bankId the bank id
	 * @return the bank group id by bank id
	 * @throws EOTException the EOT exception
	 */
	public Integer getBankGroupIdByBankId(Integer bankId) throws EOTException;
	
	/**
	 * Gets the service charge rule.
	 *
	 * @param scRuleId the sc rule id
	 * @return the service charge rule
	 * @throws EOTException the EOT exception
	 */
	public ServiceChargeRule getServiceChargeRule(Long scRuleId) throws EOTException;
	
	/**
	 * Delete service charge rule txns.
	 *
	 * @param scRuleId the sc rule id
	 * @throws EOTException the EOT exception
	 */
	public void deleteServiceChargeRuleTxns(Long scRuleId) throws EOTException;
	
	/**
	 * Delete service charge rule values.
	 *
	 * @param scRuleId the sc rule id
	 * @throws EOTException the EOT exception
	 */
	public void deleteServiceChargeRuleValues(Long scRuleId) throws EOTException;
	
	/**
	 * Delete scapplicable times.
	 *
	 * @param scRuleId the sc rule id
	 * @throws EOTException the EOT exception
	 */
	public void deleteScapplicableTimes(Long scRuleId) throws EOTException;
	
	/**
	 * Gets the inter bank service charges.
	 *
	 * @return the inter bank service charges
	 */
	public List<ServiceChargeSplit> getInterBankServiceCharges();
	
	/**
	 * Gets the inter bank service charges.
	 *
	 * @param bankId the bank id
	 * @return the inter bank service charges
	 */
	public List<ServiceChargeSplit> getInterBankServiceCharges(Integer bankId);
	
	/**
	 * Gets the service charge rules by rule level.
	 *
	 * @param ruleLevel the rule level
	 * @param ruleName the rule name
	 * @return the service charge rules by rule level
	 */
	public List<ServiceChargeRule> getServiceChargeRulesByRuleLevel(Integer ruleLevel,String ruleName);
	
	/**
	 * Gets the stamp fee from service charge split.
	 *
	 * @param bankId the bank id
	 * @return the stamp fee from service charge split
	 */
	public List<ServiceChargeSplit> getStampFeeFromServiceChargeSplit(Integer bankId );
	
	/**
	 * Delete subscription charge rule values.
	 *
	 * @param serviceChargeRuleId the service charge rule id
	 * @throws EOTException the EOT exception
	 */
	public void deleteSubscriptionChargeRuleValues(Long serviceChargeRuleId)throws EOTException;

	List<ServiceChargeRuleTxn> getServiceChargeRulesByTxnType(Integer transactionType);

}
