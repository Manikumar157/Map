/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: TransactionDetailsDTO.java
*
* Date Author Changes
* 7 Mar, 2016 Swadhin Created
*/
package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class TransactionDetailsDTO.
 */
public class TransactionDetailsDTO implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5351565957511377534L;

	/** The transaction id. */
	private Long transactionID ;
	
	/** The transaction type. */
	private Integer transactionType ;
	
	/** The alias. */
	private String alias ;
	
	/** The transaction date. */
	private Date transactionDate ;
	
	/** The transaction amount. */
	private Long transactionAmount ;
	
	/** The service charge. */
	private Long serviceCharge ;
	
	/** The status. */
	private Integer status ;
	
	/** The type. */
	private String type;
	
	/** The cus type. */
	private Integer cusType;	

	/**
	 * Gets the cus type.
	 *
	 * @return the cus type
	 */
	public Integer getCusType() {
		return cusType;
	}

	/**
	 * Sets the cus type.
	 *
	 * @param cusType the new cus type
	 */
	public void setCusType(Integer cusType) {
		this.cusType = cusType;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public Long getTransactionID() {
		return transactionID;
	}

	/**
	 * Sets the transaction id.
	 *
	 * @param transactionID the new transaction id
	 */
	public void setTransactionID(Long transactionID) {
		this.transactionID = transactionID;
	}

	/**
	 * Gets the transaction type.
	 *
	 * @return the transaction type
	 */
	public Integer getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transaction type.
	 *
	 * @param transactionType the new transaction type
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets the alias.
	 *
	 * @param alias the new alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Gets the transaction date.
	 *
	 * @return the transaction date
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Sets the transaction date.
	 *
	 * @param transactionDate the new transaction date
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Gets the transaction amount.
	 *
	 * @return the transaction amount
	 */
	public Long getTransactionAmount() {
		return transactionAmount;
	}

	/**
	 * Sets the transaction amount.
	 *
	 * @param transactionAmount the new transaction amount
	 */
	public void setTransactionAmount(Long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	/**
	 * Gets the service charge.
	 *
	 * @return the service charge
	 */
	public Long getServiceCharge() {
		return serviceCharge;
	}

	/**
	 * Sets the service charge.
	 *
	 * @param serviceCharge the new service charge
	 */
	public void setServiceCharge(Long serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

}
