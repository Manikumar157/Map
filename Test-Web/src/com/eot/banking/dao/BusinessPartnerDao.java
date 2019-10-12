/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: BusinessPartnerDao.java
*
* Date Author Changes
* Oct 16, 2018 Vinod Joshi Created
*/
package com.eot.banking.dao;


import java.util.List;

import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.ApiCredientials;
import com.eot.entity.ApiLogs;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.KycType;



/**
 * The Interface BusinessPartnerDao.
 */
public interface BusinessPartnerDao extends BaseDao{

	
	/**
	 * Gets the business partner.
	 *
	 * @param userName the user name
	 * @return the business partner
	 */
	BusinessPartner getBusinessPartner(String userName);
	
	/**
	 * Gets the business partnerby id.
	 *
	 * @param id the id
	 * @return the business partnerby id
	 */
	BusinessPartner getBusinessPartnerbyId(String id);
	
	/**
	 * Search business partner.
	 *
	 * @param name the name
	 * @param contactPerson the contact person
	 * @param contactNumber the contact number
	 * @param code the code
	 * @param pageNumber the page number
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param partnerType the partner type
	 * @param seniorPartner the senior partner
	 * @param bankId the bank id
	 * @return the page
	 */
	Page searchBusinessPartner(String name, String contactPerson, String contactNumber, String code, Integer pageNumber, String fromDate, String toDate, String partnerType, String seniorPartner, Integer bankId);
	
	/**
	 * Gets the user.
	 *
	 * @param name the name
	 * @return the user
	 */
	BusinessPartnerUser getUser(String name);
	
	/**
	 * Gets the all busniess partner.
	 *
	 * @param partnerType the partner type
	 * @param seniorPartnerId the senior partner id
	 * @return the all busniess partner
	 */
	List<BusinessPartner> getAllBusniessPartner(Integer partnerType, Integer seniorPartnerId,Integer bankId);
	
	/**
	 * Gets the business partner.
	 *
	 * @param id the id
	 * @return the business partner
	 */
	BusinessPartner getBusinessPartner(Long id);
	
	/**
	 * Gets the business partner by mobile number.
	 *
	 * @param contactNumber the contact number
	 * @return the business partner by mobile number
	 */
	BusinessPartner getBusinessPartnerByMobileNumber(String contactNumber);
	
	/**
	 * Gets the business partner by contact number.
	 *
	 * @param contactNumber the contact number
	 * @return the business partner by contact number
	 */
	BusinessPartner getBusinessPartnerByContactNumber(String contactNumber);
	
	/**
	 * Gets the business partner by email address.
	 *
	 * @param emailId the email id
	 * @return the business partner by email address
	 */
	BusinessPartner getBusinessPartnerByEmailAddress(String emailId);
	
	/**
	 * Gets the business partner by code.
	 *
	 * @param code the code
	 * @return the business partner by code
	 */
	BusinessPartner getBusinessPartnerByCode(String code);
	
	/**
	 * Gets the kyc list.
	 *
	 * @return the kyc list
	 */
	public List<KycType> getKycList();
	
	/**
	 * Gets the kycby id.
	 *
	 * @param kycid the kycid
	 * @return the kycby id
	 */
	public KycType getkycbyId(int kycid);
	
	
	Page getAllBusniessPartner(int pageNumber);

	List<BusinessPartnerDTO> getAllBusniessPartnerUser();

	BusinessPartner getBusinessPartnerByName(String name);

	BusinessPartner getBusinessPartnerDetails(String partnerType, Integer bankId, String seniorPartner,
			String businessPartnerCode);
	
	BusinessPartnerUser getBusinessPartnerUserByName(String userName);
	
	/**
	 * Gets the all busniess partner.
	 *
	 * @param partnerType the partner type
	 * @param seniorPartnerId the senior partner id
	 * @return the all busniess partner
	 */
	List<BusinessPartner> getAllBulkBusinessPartner(Integer partnerType,Integer bankId);

	ApiCredientials varifyApiRequest(String username, String password);

	ApiLogs fetchApiLogs(String referenceId);
	
	public List<Object[]> lowBlanceCheckForBusinessPartner ();

}
