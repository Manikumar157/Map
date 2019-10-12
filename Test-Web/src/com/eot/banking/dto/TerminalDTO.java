/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: TerminalDTO.java
*
* Date Author Changes
* 22 May, 2016 Swadhin Created
*/
package com.eot.banking.dto;

import java.io.Serializable;

/**
 * The Class TerminalDTO.
 */
public class TerminalDTO implements Serializable {

    /** The terminal id. */
    private Integer terminalId;

    /** The mobile number. */
    private String mobileNumber;

    /** The active. */
    private int active;

    /** The account number. */
    private String accountNumber;

    /** The outlet id. */
    private Integer outletId;

    /** The merchant id. */
    private Integer merchantId;

	/**
	 * Gets the terminal id.
	 *
	 * @return the terminal id
	 */
	public Integer getTerminalId() {
		return terminalId;
	}

	/**
	 * Sets the terminal id.
	 *
	 * @param terminalId the new terminal id
	 */
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile number
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber the new mobile number
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * Gets the account number.
	 *
	 * @return the account number
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the account number.
	 *
	 * @param accountNumber the new account number
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Gets the outlet id.
	 *
	 * @return the outlet id
	 */
	public Integer getOutletId() {
		return outletId;
	}

	/**
	 * Sets the outlet id.
	 *
	 * @param outletId the new outlet id
	 */
	public void setOutletId(Integer outletId) {
		this.outletId = outletId;
	}

	/**
	 * Gets the merchant id.
	 *
	 * @return the merchant id
	 */
	public Integer getMerchantId() {
		return merchantId;
	}

	/**
	 * Sets the merchant id.
	 *
	 * @param merchantId the new merchant id
	 */
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

}
