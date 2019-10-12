package com.eot.banking.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BankFloatDepositDTO {
	
	private String code;

	private String txnId;
	
	private String ActionType;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date fromDate;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date toDate;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getActionType() {
		return ActionType;
	}

	public void setActionType(String actionType) {
		ActionType = actionType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
