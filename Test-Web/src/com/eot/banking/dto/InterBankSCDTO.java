package com.eot.banking.dto;

public class InterBankSCDTO {
	
	private Float tax ;
	
	private Float gimShare ;
	
	private Float issuerBank ;
	
	private Float aquirerBank ;
	
	private Float stampFee;
	public Float getStampFee() {
		return stampFee;
	}

	public void setStampFee(Float stampFee) {
		this.stampFee = stampFee;
	}

	public Float getTax() {
		return tax;
	}

	public void setTax(Float tax) {
		this.tax = tax;
	}

	public Float getGimShare() {
		return gimShare;
	}

	public void setGimShare(Float gimShare) {
		this.gimShare = gimShare;
	}

	public Float getIssuerBank() {
		return issuerBank;
	}

	public void setIssuerBank(Float issuerBank) {
		this.issuerBank = issuerBank;
	}

	public Float getAquirerBank() {
		return aquirerBank;
	}

	public void setAquirerBank(Float aquirerBank) {
		this.aquirerBank = aquirerBank;
	}

}
