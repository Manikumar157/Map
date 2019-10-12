package com.eot.banking.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TransactionVolumeDTO {
	
	private String code;
	
	private String agentCode;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date fromDate;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date toDate;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

}
