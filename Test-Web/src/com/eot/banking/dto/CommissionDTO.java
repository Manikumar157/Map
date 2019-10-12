package com.eot.banking.dto;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class CommissionDTO.
 */
public class CommissionDTO {
	
	/** The commision id. */
	private Long  commisionId ;
	
	/** The bank id. */
	private Integer bankId;
	
	/** The profile id. */
	private Integer profileId;
	
	/** The transactions. */
	private Integer[]  transactions;
	
	/** The commission. */
	private double commission;
	
	/** The profile name. */
	private String profileName;
	
	/** The bank name. */
	private String bankName;
	
	/** The transaction type name. */
	private String transactionTypeName;
	
    /** The transaction type id. */
    private Integer transactionTypeId;  
    
	
	/**
	 * Gets the profile name.
	 *
	 * @return the profile name
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * Sets the profile name.
	 *
	 * @param profileName the new profile name
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * Gets the bank name.
	 *
	 * @return the bank name
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * Sets the bank name.
	 *
	 * @param bankName the new bank name
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * Gets the transaction type name.
	 *
	 * @return the transaction type name
	 */
	public String getTransactionTypeName() {
		return transactionTypeName;
	}

	/**
	 * Sets the transaction type name.
	 *
	 * @param transactionTypeName the new transaction type name
	 */
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}

	
	
	/**
	 * Gets the bank id.
	 *
	 * @return the bank id
	 */
	public Integer getBankId() {
		return bankId;
	}
	
	/**
	 * Sets the bank id.
	 *
	 * @param bankId the new bank id
	 */
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	
	/**
	 * Gets the profile id.
	 *
	 * @return the profile id
	 */
	public Integer getProfileId() {
		return profileId;
	}
	
	/**
	 * Sets the profile id.
	 *
	 * @param profileId the new profile id
	 */
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets the transactions.
	 *
	 * @return the transactions
	 */
	public Integer[] getTransactions() {
		return transactions;
	}
	
	/**
	 * Sets the transactions.
	 *
	 * @param transactions the new transactions
	 */
	public void setTransactions(Integer[] transactions) {
		this.transactions = transactions;
	}
	
	/**
	 * Gets the commission.
	 *
	 * @return the commission
	 */
	public double getCommission() {
		return commission;
	}
	
	/**
	 * Sets the commission.
	 *
	 * @param commission the new commission
	 */
	public void setCommission(double commission) {
		this.commission = commission;
	}

	/**
	 * Gets the commision id.
	 *
	 * @return the commision id
	 */
	public Long getCommisionId() {
		return commisionId;
	}

	/**
	 * Sets the commision id.
	 *
	 * @param commisionId the new commision id
	 */
	public void setCommisionId(Long commisionId) {
		this.commisionId = commisionId;
	}

	/**
	 * Gets the transaction type id.
	 *
	 * @return the transaction type id
	 */
	public Integer getTransactionTypeId() {
		return transactionTypeId;
	}

	/**
	 * Sets the transaction type id.
	 *
	 * @param transactionTypeId the new transaction type id
	 */
	public void setTransactionTypeId(Integer transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

}
