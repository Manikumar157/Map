/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: MerchantService.java
*
* Date Author Changes
* 23 May, 2016 Swadhin Created
*/
package com.eot.banking.service;

import java.util.List;

import com.eot.banking.dto.MerchantDTO;
import com.eot.banking.dto.OutletDTO;
import com.eot.banking.dto.TerminalDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.Merchant;
import com.eot.entity.MerchantCategory;
import com.eot.entity.Outlet;
import com.eot.entity.Terminal;

/**
 * The Interface MerchantService.
 */
public interface MerchantService {

	/**
	 * Search merchant.
	 *
	 * @param userName the user name
	 * @param bankGroupId the bank group id
	 * @param merchantName the merchant name
	 * @param mobileNumber the mobile number
	 * @param bankId the bank id
	 * @param branchId the branch id
	 * @param countryId the country id
	 * @return the page
	 * @throws EOTException the eOT exception
	 */
	List<Merchant> searchMerchant(String userName, String bankGroupId,
			String merchantName, String mobileNumber, String bankId,
			String branchId, String countryId) throws EOTException;

	/**
	 * Save merchant.
	 *
	 * @param merchantDTO the merchant dto
	 * @return the integer
	 * @throws EOTException the eOT exception
	 */
	Integer saveMerchant(MerchantDTO merchantDTO)throws EOTException;

	/**
	 * Update merchant.
	 *
	 * @param merchantDTO the merchant dto
	 * @throws EOTException the eOT exception
	 */
	void updateMerchant(MerchantDTO merchantDTO) throws EOTException;

	/**
	 * Gets the merchant details.
	 *
	 * @param merchantId the merchant id
	 * @return the merchant details
	 * @throws EOTException the eOT exception
	 */
	MerchantDTO getMerchantDetails(Integer merchantId) throws EOTException;

	/**
	 * Search outlet.
	 *
	 * @param userName the user name
	 * @param mobileNumber the mobile number
	 * @param location the location
	 * @param countryId the country id
	 * @param status the status
	 * @return the list
	 * @throws EOTException the eOT exception
	 */
	List<Outlet> searchOutlet(String userName, String mobileNumber, String location, String countryId, Integer status, Integer merchantId) throws EOTException;

	/**
	 * Save outlet.
	 *
	 * @param outletDTO the outlet dto
	 * @return the integer
	 * @throws EOTException the eOT exception
	 */
	Integer saveOutlet(OutletDTO outletDTO) throws EOTException;

	/**
	 * Update outlet.
	 *
	 * @param outletDTO the outlet dto
	 * @throws EOTException the eOT exception
	 */
	void updateOutlet(OutletDTO outletDTO) throws EOTException;

	/**
	 * Gets the outlet details.
	 *
	 * @param outletId the outlet id
	 * @return the outlet details
	 * @throws EOTException the eOT exception
	 */
	OutletDTO getOutletDetails(Integer outletId) throws EOTException;

	/**
	 * Gets the all active mcc.
	 *
	 * @return the all active mcc
	 * @throws EOTException the eOT exception
	 */
	List<MerchantCategory> getAllActiveMCC()throws EOTException;
	
	/**
	 * Search terminal.
	 *
	 * @param userName the user name
	 * @param outletId 
	 * @param mobileNumber the mobile number
	 * @param bankId the bank id
	 * @param branchId the branch id
	 * @param status the status
	 * @return the list
	 * @throws EOTException the eOT exception
	 */
	List<Terminal> searchTerminal(String userName, Integer outletId, String mobileNumber, String bankId,
			String branchId, String status)throws EOTException;

	/**
	 * Save terminal.
	 *
	 * @param terminalDTO the terminal dto
	 * @return the integer
	 * @throws EOTException the eOT exception
	 */
	Integer saveTerminal(TerminalDTO terminalDTO)throws Exception;

	/**
	 * Update terminal.
	 *
	 * @param terminalDTO the terminal dto
	 * @throws EOTException the eOT exception
	 */
	void updateTerminal(TerminalDTO terminalDTO)throws Exception;
	
	/**
	 * Gets the merchant list.
	 *
	 * @param merchantId the merchant id
	 * @return the merchant list
	 * @throws EOTException the eOT exception
	 */
	public List<Merchant> getMerchantList(Integer merchantId)throws EOTException;

	/**
	 * Gets the outlet list.
	 *
	 * @param merchant the merchant
	 * @return the outlet list
	 * @throws EOTException the eOT exception
	 */
	List<Outlet> getOutletList(Integer merchant)throws EOTException ;

	/**
	 * Gets the terminal details.
	 *
	 * @param terminalId the terminal id
	 * @return the terminal details
	 * @throws EOTException the eOT exception
	 */
	TerminalDTO getTerminalDetails(Integer terminalId)throws EOTException ;

	// bugId-505 by:rajlaxmi for:showing datatable with active/deactive status
	List<Outlet> showAllOutlets(String userName, String mobileNumber, String location, String countryId, String status);

	boolean checkIfEmailAlreadyExists(MerchantDTO merchantDTO);

	boolean checkIfUpdateEmailExists(MerchantDTO merchantDTO);

	boolean checkIfMobileAlreadyExists(MerchantDTO merchantDTO);

	boolean checkIfUpdateMobileExists(MerchantDTO merchantDTO);

}
