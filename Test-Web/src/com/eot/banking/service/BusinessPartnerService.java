/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: BusinessPartnerService.java
*
* Date Author Changes
* Oct 16, 2018 Vinod Joshi Created
*/
package com.eot.banking.service;

import java.util.List;

import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.KycType;

/**
 * The Interface BusinessPartnerService.
 */
public interface BusinessPartnerService {

	/**
	 * Save BusinessPartner.
	 *
	 * @param name the name
	 * @param contactPerson the contact person
	 * @param contactNumber the contact number
	 * @param code the code
	 * @param userName the user name
	 * @param pageNumber the page number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param partnerType the partner type
	 * @param seniorPartner the senior partner
	 * @param bankId the bank id
	 * @return the page
	 */

	Page searchBusinessPartners(String name, String contactPerson, String contactNumber, String code, String userName, Integer pageNumber, String fromDate, String toDate, String partnerType, String seniorPartner, Integer bankId);

	/**
	 * Gets the business partner user.
	 *
	 * @param userName the user name
	 * @return the business partner user
	 */
	BusinessPartnerUser getBusinessPartnerUser(String userName);

	/**
	 * Save business partner.
	 *
	 * @param businessPartnerDTO the business partner DTO
	 * @param SeniorID the senior ID
	 * @param BankID the bank ID
	 * @throws Exception the exception
	 */
	void saveBusinessPartner(BusinessPartnerDTO businessPartnerDTO, Integer SeniorID, Integer BankID) throws Exception;

	/**
	 * Gets the business partner details.
	 *
	 * @param id the id
	 * @param userName the user name
	 * @return the business partner details
	 * @throws EOTException the EOT exception
	 */
	BusinessPartnerDTO getBusinessPartnerDetails(Long id, String userName) throws EOTException;

	/**
	 * Update business partner.
	 *
	 * @param businessPartnerDTO the business partner DTO
	 * @param seniorID the senior ID
	 * @param BankID the bank ID
	 * @throws Exception the exception
	 */
	void updateBusinessPartner(BusinessPartnerDTO businessPartnerDTO, Integer seniorID,Integer BankID)throws Exception;
	
	/**
	 * Gets the kyc list.
	 *
	 * @return the kyc list
	 */
	public List<KycType> getKycList();
	
	/**
	 * Gets the photo details.
	 *
	 * @param Businesspartnerid the businesspartnerid
	 * @param type the type
	 * @return the photo details
	 * @throws EOTException the EOT exception
	 */
	byte[] getPhotoDetails(Long Businesspartnerid, String type) throws EOTException ;
	
	 Page getAllBusinessPartner(int pageNumber);

	BusinessPartner loadBusinessPartnerByCode(String parameter);
	
	public BusinessPartner getBusinessPartner(String userName) ;
	
}
