/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: CommisionSplitsDTO.java
*
* Date Author Changes
* May 28, 2019 Sudhanshu Created
*/
package com.eot.banking.dto;

import java.io.Serializable;
import java.util.List;

/**
 * The Class CommisionSplitsDTO.
 */
public class CommisionSplitsDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5175141559114305669L;

	/** The commision split ID. */
	private Integer commisionSplitID;

	/** The business partner. */
	private String businessPartner;

	/** The commision pct. */
	private float commisionPct = 0;

	/** The transaction type. */
	private String transactionType;
	
	private List<CommisionSplitsDTO> commissionSplitList;
	
	private String commdata;

	/**
	 * @return the commdata
	 */
	public String getCommdata() {
		return commdata;
	}

	/**
	 * @param commdata the commdata to set
	 */
	public void setCommdata(String commdata) {
		this.commdata = commdata;
	}

	/**
	 * Gets the commision split ID.
	 *
	 * @return the commisionSplitID
	 */
	public Integer getCommisionSplitID() {
		return commisionSplitID;
	}

	/**
	 * Sets the commision split ID.
	 *
	 * @param commisionSplitID
	 *            the commisionSplitID to set
	 */
	public void setCommisionSplitID(Integer commisionSplitID) {
		this.commisionSplitID = commisionSplitID;
	}

	/**
	 * Gets the business partner.
	 *
	 * @return the businessPartner
	 */
	public String getBusinessPartner() {
		return businessPartner;
	}

	/**
	 * Sets the business partner.
	 *
	 * @param businessPartner
	 *            the businessPartner to set
	 */
	public void setBusinessPartner(String businessPartner) {
		this.businessPartner = businessPartner;
	}

	/**
	 * Gets the commision pct.
	 *
	 * @return the commisionPct
	 */
	public float getCommisionPct() {
		return commisionPct;
	}

	/**
	 * Sets the commision pct.
	 *
	 * @param commisionPct
	 *            the commisionPct to set
	 */
	public void setCommisionPct(float commisionPct) {
		this.commisionPct = commisionPct;
	}

	/**
	 * Gets the transaction type.
	 *
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transaction type.
	 *
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the commissionSplitList
	 */
	public List<CommisionSplitsDTO> getCommissionSplitList() {
		return commissionSplitList;
	}

	/**
	 * @param commissionSplitList the commissionSplitList to set
	 */
	public void setCommissionSplitList(List<CommisionSplitsDTO> commissionSplitList) {
		this.commissionSplitList = commissionSplitList;
	}

}
