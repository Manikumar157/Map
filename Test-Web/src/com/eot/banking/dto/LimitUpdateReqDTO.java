package com.eot.banking.dto;

public class LimitUpdateReqDTO extends AuthDTO {
	
	private String billNumber;
	private Double billAmount;
	private String customerRefNumber;
	private String  bankreference;
	private String  paymentMode;
	private String  transactionDate;
	private String  phoneNumber;
	private String  debitaccount;
	private String  debitcustname;
	private String  tranParticular;
	private String  phonenumber;
	
	
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	
	public String getBankreference() {
		return bankreference;
	}
	public void setBankreference(String bankreference) {
		this.bankreference = bankreference;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getDebitaccount() {
		return debitaccount;
	}
	public void setDebitaccount(String debitaccount) {
		this.debitaccount = debitaccount;
	}
	public String getDebitcustname() {
		return debitcustname;
	}
	public void setDebitcustname(String debitcustname) {
		this.debitcustname = debitcustname;
	}
	public String getCustomerRefNumber() {
		return customerRefNumber;
	}
	public void setCustomerRefNumber(String customerRefNumber) {
		this.customerRefNumber = customerRefNumber;
	}
	public String getTranParticular() {
		return tranParticular;
	}
	public void setTranParticular(String tranParticular) {
		this.tranParticular = tranParticular;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	
	

}
