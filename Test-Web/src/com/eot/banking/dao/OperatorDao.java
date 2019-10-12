/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: OperatorDao.java
*
* Date Author Changes
* 2 Aug, 2016 Swadhin Created
*/
package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.OperatorDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.Country;
import com.eot.entity.CustomerCard;
import com.eot.entity.Operator;
import com.eot.entity.OperatorDenomination;
import com.eot.entity.OperatorVoucher;
import com.eot.entity.RemittanceCompany;

/**
 * The Interface OperatorDao.
 */
public interface OperatorDao extends BaseDao {
	
	/**
	 * Gets the operator list.
	 *
	 * @param pageNumber the page number
	 * @return the operator list
	 */
	public Page getOperatorList(Integer pageNumber, Integer bankId);
	
	/**
	 * Gets the denominations.
	 *
	 * @param operatorId the operator id
	 * @param pageNumber the page number
	 * @return the denominations
	 */
	public Page getDenominations(Long operatorId,Integer pageNumber);
	
	/**
	 * Gets the vouchers.
	 *
	 * @param denominationId the denomination id
	 * @param pageNumber the page number
	 * @return the vouchers
	 */
	public Page getVouchers(Long denominationId,Integer pageNumber);
	
	/**
	 * Gets the countries.
	 *
	 * @param language the language
	 * @return the countries
	 */
	public List<Country> getCountries(String language);
	
	/**
	 * Gets the operator.
	 *
	 * @param operatorId the operator id
	 * @return the operator
	 */
	public Operator getOperator(Long operatorId);
	
	/**
	 * Verify denomination.
	 *
	 * @param denomination the denomination
	 * @param OperatorId the operator id
	 * @return the operator denomination
	 */
	public OperatorDenomination verifyDenomination(Long denomination,Long OperatorId); 
	
	/**
	 * Gets the denomination.
	 *
	 * @param denominationId the denomination id
	 * @return the denomination
	 */
	OperatorDenomination getDenomination(Long denominationId);
	
	/**
	 * Gets the operator name.
	 *
	 * @param opearatorId the opearator id
	 * @return the operator name
	 */
	public Operator getOperatorName(Long opearatorId);
	
	/**
	 * Check denomination.
	 *
	 * @param denomination the denomination
	 * @param operatorId the operator id
	 * @return the operator denomination
	 */
	public OperatorDenomination checkDenomination(Long denomination,Long operatorId);
	
	/**
	 * Check denomination.
	 *
	 * @param denomination the denomination
	 * @param operatorId the operator id
	 * @param denominationId the denomination id
	 * @return the operator denomination
	 */
	public OperatorDenomination checkDenomination(Long denomination,Long operatorId,Long denominationId);
	
	/**
	 * Gets the operator name by name.
	 *
	 * @param operatorName the operator name
	 * @param countryId the country id
	 * @return the operator name by name
	 */
	public Operator getOperatorNameByName(String operatorName,Integer countryId);
	
	/**
	 * Gets the operator name by name.
	 *
	 * @param operatorName the operator name
	 * @param countryId the country id
	 * @param operatorId the operator id
	 * @return the operator name by name
	 */
	public Operator getOperatorNameByName(String operatorName,Integer countryId,Long operatorId);
	
	/**
	 * Gets the operator status.
	 *
	 * @param operatorId the operator id
	 * @return the operator status
	 */
	public Operator getOperatorStatus(Long operatorId);
	
	/**
	 * Gets the operator list by country.
	 *
	 * @param countryId the country id
	 * @return the operator list by country
	 */
	public List<Operator> getOperatorListByCountry(Integer countryId);
	
	/**
	 * Gets the denomination list.
	 *
	 * @param operatorId the operator id
	 * @return the denomination list
	 */
	public List<OperatorDenomination> getDenominationList(Long operatorId);
	
	/**
	 * Gets the chart list.
	 *
	 * @param operatorDTO the operator dto
	 * @return the chart list
	 */
	List getChartList(OperatorDTO operatorDTO);
	
	/**
	 * Verify voucher sl number.
	 *
	 * @param voucherSlNum the voucher sl num
	 * @param operatorId the operator id
	 * @return the operator voucher
	 */
	OperatorVoucher verifyVoucherSlNumber(String voucherSlNum,Long operatorId);
	
	/**
	 * Verify voucher num.
	 *
	 * @param voucherNum the voucher num
	 * @param operatorId the operator id
	 * @return the operator voucher
	 */
	OperatorVoucher verifyVoucherNum(String voucherNum,Long operatorId);
	
	/**
	 * Search operator.
	 *
	 * @param operatorDTO the operator dto
	 * @param pageNumber the page number
	 * @return the page
	 */
	public Page searchOperator(OperatorDTO operatorDTO, int pageNumber, Integer bankId);
	
	/**
	 * Gets the card details by operator id.
	 *
	 * @param operatorId the operator id
	 * @return the card details by operator id
	 */
	public CustomerCard getCardDetailsByOperatorId(Integer operatorId);
	
	/**
	 * Gets the card details by operator id.
	 *
	 * @param operatorId the operator id
	 * @param cardNo the card no
	 * @return the card details by operator id
	 */
	public CustomerCard getCardDetailsByOperatorId(Integer operatorId, String cardNo);
	
	/**
	 * Gets the operator vertual card.
	 *
	 * @param cardNumber the card number
	 * @return the operator vertual card
	 */
	public CustomerCard getOperatorVertualCard(String cardNumber);
	
	/**
	 * Gets the remittance company by name.
	 *
	 * @param remittanceCompanyName the remittance company name
	 * @return the remittance company by name
	 */
	public RemittanceCompany getRemittanceCompanyByName(String remittanceCompanyName);

	/**
	 * Gets the remittance company by id.
	 *
	 * @param remittanceCompanyId the remittance company id
	 * @return the remittance company by id
	 */
	public RemittanceCompany getRemittanceCompanyById(Integer remittanceCompanyId);

	/**
	 * Delete remittance companies transfer type.
	 *
	 * @param remittanceCompanyId the remittance company id
	 */
	public void deleteRemittanceCompaniesTransferType(Integer remittanceCompanyId);

	/**
	 * Gets the all remittance companies.
	 *
	 * @param pageNumber the page number
	 * @return the all remittance companies
	 */
	public Page getAllRemittanceCompanies(Integer pageNumber);
	
	public Page getOperatorListByPageNumber(Integer pageNumber);
}

