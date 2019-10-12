package com.eot.banking.dto;

import java.util.Date;
import java.util.List;

public class SenelecBillSearchDTO {
	
	private String policyNo;
	private String customer;
	private String recordNo;
	private String status;
	private String fromDate;
	private String toDate;
	private String fromAmount;
	private String toAmount;
	private List billList;
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFromAmount() {
		return fromAmount;
	}
	public void setFromAmount(String fromAmount) {
		this.fromAmount = fromAmount;
	}
	public String getToAmount() {
		return toAmount;
	}
	public void setToAmount(String toAmount) {
		this.toAmount = toAmount;
	}
	/**
	 * @return the billList
	 */
	public List getBillList() {
		return billList;
	}
	/**
	 * @param billList the billList to set
	 */
	public void setBillList(List billList) {
		this.billList = billList;
	}
	
	
	

}
