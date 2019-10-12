/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: ReversalDTO.java
*
* Date Author Changes
* 7 Mar, 2016 Swadhin Created
*/
package com.eot.banking.dto;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * The Class ReversalDTO.
 */
public class ReversalDTO {


	/** The transaction id. */
	private String transactionId;
	
	/** The reversed txn type. */
	private String reversedTxnType;
	
	/** The adjusted amount. */
	private String adjustedAmount;
	
	/** The adjusted fee. */
	private String adjustedFee;
	
	/** The comment. */
	private String comment;
	
	/** The reversal file. */
	private CommonsMultipartFile reversalFile;
	
	/**
	 * Gets the reversal file.
	 *
	 * @return the reversal file
	 */
	public CommonsMultipartFile getReversalFile() {
		return reversalFile;
	}
	
	/**
	 * Sets the reversal file.
	 *
	 * @param reversalFile the new reversal file
	 */
	public void setReversalFile(CommonsMultipartFile reversalFile) {
		this.reversalFile = reversalFile;
	}
	
	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}
	
	/**
	 * Sets the transaction id.
	 *
	 * @param transactionId the new transaction id
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	/**
	 * Gets the reversed txn type.
	 *
	 * @return the reversed txn type
	 */
	public String getReversedTxnType() {
		return reversedTxnType;
	}
	
	/**
	 * Sets the reversed txn type.
	 *
	 * @param reversedTxnType the new reversed txn type
	 */
	public void setReversedTxnType(String reversedTxnType) {
		this.reversedTxnType = reversedTxnType;
	}
	
	/**
	 * Gets the adjusted amount.
	 *
	 * @return the adjusted amount
	 */
	public String getAdjustedAmount() {
		return adjustedAmount;
	}
	
	/**
	 * Sets the adjusted amount.
	 *
	 * @param adjustedAmount the new adjusted amount
	 */
	public void setAdjustedAmount(String adjustedAmount) {
		this.adjustedAmount = adjustedAmount;
	}
	
	/**
	 * Gets the adjusted fee.
	 *
	 * @return the adjusted fee
	 */
	public String getAdjustedFee() {
		return adjustedFee;
	}
	
	/**
	 * Sets the adjusted fee.
	 *
	 * @param adjustedFee the new adjusted fee
	 */
	public void setAdjustedFee(String adjustedFee) {
		this.adjustedFee = adjustedFee;
	}
	
	/**
	 * Gets the comment.
	 *
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the comment.
	 *
	 * @param comment the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}	
	
}
