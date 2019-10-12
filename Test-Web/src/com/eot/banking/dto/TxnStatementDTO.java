package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

public class TxnStatementDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7907050802665361523L;

	private Date transDate;

    private String transDesc;

    private String transType;

    private Long amount;
    
    private Double fromAccountBalance;
    
    private Double toAccountBalance;
    
    public TxnStatementDTO() {
	
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getTransDesc() {
		return transDesc;
	}

	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Double getFromAccountBalance() {
		return fromAccountBalance;
	}

	public void setFromAccountBalance(Double fromAccountBalance) {
		this.fromAccountBalance = fromAccountBalance;
	}

	public Double getToAccountBalance() {
		return toAccountBalance;
	}

	public void setToAccountBalance(Double toAccountBalance) {
		this.toAccountBalance = toAccountBalance;
	}

}
