/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: QRCodeDTO.java
*
* Date Author Changes
* Apr 23, 2019 Sudhanshu Created
*/
package com.eot.banking.dto;

import java.io.Serializable;

/**
 * The Class QRCodeDTO.
 */
public class QRCodeDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1093308946863777661L;
	
	/** The agent code. */
	private String agentCode;
	
	/** The mobile number. */
	private String mobileNumber;
	
	/** The name. */
	private String name;
	
	/** The state. */
	private String state;
	
	/** The city. */
	private String city;
	
	/** The qr code. */
	private String qrCode;
	
	/** The type. */
	private String custType;
	
	/** The address. */
	private String address;
	
	private String customerId;

	/**
	 * Gets the agent code.
	 *
	 * @return the agent code
	 */
	public String getAgentCode() {
		return agentCode;
	}

	/**
	 * Sets the agent code.
	 *
	 * @param agentCode the new agent code
	 */
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the qr code.
	 *
	 * @return the qr code
	 */
	public String getQrCode() {
		return qrCode;
	}

	/**
	 * Sets the qr code.
	 *
	 * @param qrCode the new qr code
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	

	/**
	 * Gets the cust type.
	 *
	 * @return the cust type
	 */
	public String getCustType() {
		return custType;
	}

	/**
	 * Sets the cust type.
	 *
	 * @param custType the new cust type
	 */
	public void setCustType(String custType) {
		this.custType = custType;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
