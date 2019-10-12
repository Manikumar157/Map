package com.eot.banking.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class NonRegUssdCustomerDTO {
	
	private String mobileNumber;
	
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

}
