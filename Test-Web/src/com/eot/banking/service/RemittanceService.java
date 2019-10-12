/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: RemittanceService.java
*
* Date Author Changes
* 2 Aug, 2016 Swadhin Created
*/
package com.eot.banking.service;

import com.eot.banking.dto.RemittanceDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;

/**
 * The Interface RemittanceService.
 */
public interface RemittanceService {

	/**
	 * Adds the remittance company.
	 *
	 * @param remittanceDTO the remittance dto
	 * @throws EOTException the eOT exception
	 */
	void addRemittanceCompany(RemittanceDTO remittanceDTO) throws EOTException;
	
	/**
	 * Update remittance company.
	 *
	 * @param remittanceDTO the remittance dto
	 * @throws EOTException the eOT exception
	 */
	void updateRemittanceCompany(RemittanceDTO remittanceDTO) throws EOTException;
	
	/**
	 * Gets the remittance company.
	 *
	 * @param remittanceCompanyId the remittance company id
	 * @return the remittance company
	 * @throws EOTException the eOT exception
	 */
	RemittanceDTO getRemittanceCompany(Integer remittanceCompanyId) throws EOTException;
	
	/**
	 * Gets the all remittance companies.
	 *
	 * @param pageNumber the page number
	 * @return the all remittance companies
	 */
	Page getAllRemittanceCompanies(Integer pageNumber);
}
