/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: MerchantDao.java
*
* Date Author Changes
* 23 May, 2016 Swadhin Created
*/
package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.MerchantDTO;
import com.eot.entity.Merchant;
import com.eot.entity.MerchantCategory;
import com.eot.entity.Outlet;
import com.eot.entity.Terminal;

/**
 * The Interface MerchantDao.
 */
public interface MerchantDao extends BaseDao{

	/**
	 * Search merchant.
	 *
	 * @param bankId the bank id
	 * @param bankGroupId the bank group id
	 * @param merchantName the merchant name
	 * @param mobileNumber the mobile number
	 * @param branchId the branch id
	 * @param countryId the country id
	 * @return the page
	 */
	List<Merchant> searchMerchant(Integer bankId, Integer bankGroupId, String merchantName,
			String mobileNumber, String branchId,
			String countryId);

	/**
	 * Gets the merchant by mobile number.
	 *
	 * @param primaryContactMobile the primary contact mobile
	 * @return the merchant by mobile number
	 */
	Merchant getMerchantByMobileNumber(String primaryContactMobile);

	/**
	 * Gets the merchant from merchant id.
	 *
	 * @param merchantId the merchant id
	 * @return the merchant from merchant id
	 */
	Merchant getMerchantFromMerchantID(Integer merchantId);
	
	/**
	 * Search outlet.
	 *
	 * @param mobileNumber the mobile number
	 * @param bankId the bank id
	 * @param countryId the country id
	 * @param status the status
	 * @param location 
	 * @return the list
	 */
	List<Outlet> searchOutlet(String mobileNumber, Integer bankId, String countryId, Integer status, String location, Integer merchantId);

	/**
	 * Gets the outlet by mobile number.
	 *
	 * @param primaryContactMobile the primary contact mobile
	 * @return the outlet by mobile number
	 */
	Outlet getOutletByMobileNumber(String primaryContactMobile);

	/**
	 * Gets the outlet from outlet id.
	 *
	 * @param outletId the outlet id
	 * @return the outlet from outlet id
	 */
	Outlet getOutletFromOutletId(Integer outletId);

	/**
	 * Gets the all active mcc.
	 *
	 * @return the all active mcc
	 */
	List<MerchantCategory> getAllActiveMCC();
	/**
	 * Search terminal.
	 *
	 * @param bankId the bank id
	 * @param bankGroupId the bank group id
	 * @param mobileNumber the mobile number
	 * @param branchId the branch id
	 * @param status the status
	 * @param status2 
	 * @return the list
	 */
	List<Terminal> searchTerminal(Integer outletId, Integer bankId, Integer bankGroupId,
			String mobileNumber, String branchId, String status);

	/**
	 * Gets the terminal by id.
	 *
	 * @param terminalId the terminal id
	 * @return the terminal by id
	 */
	Terminal getTerminalByID(Integer terminalId);

	/**
	 * Gets the merchant list from merchant id.
	 *
	 * @param merchantId the merchant id
	 * @return the merchant list from merchant id
	 */
	List<Merchant> getMerchantListFromMerchantID(Integer merchantId);

	/**
	 * Gets the outlet list.
	 *
	 * @param merchant the merchant
	 * @return the outlet list
	 */
	List<Outlet> getOutletList(Integer merchant);

	// bugId-505 by:rajlaxmi for:showing datatable with active/deactive status
	List<Outlet> showAllOutlets(String mobileNumber, Object object, String countryId, String status, String location);

	boolean checkIfEmailExists(MerchantDTO merchantDTO);

	boolean checkForUpdateEmail(MerchantDTO merchantDTO);

	boolean checkIfMobileExists(MerchantDTO merchantDTO);

	boolean checkForUpdateMobile(MerchantDTO merchantDTO);
	

	/**
	 * Gets the terminal mobileNumber.
	 *
	 * @param terminalId the mobileNumber
	 * @return the terminal by mobileNumber
	 */
	Terminal getTerminalByMobileNumber(String terminalMobileNumber);
}
