package com.eot.banking.dto;

public class OtpDTO {
	private int  referenceType;
	private String referenceId;
	private String otphash;
	private int otpType;
	
	public int getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(int referenceType) {
		this.referenceType = referenceType;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	
	public String getOtphash() {
		return otphash;
	}
	public void setOtphash(String otphash) {
		this.otphash = otphash;
	}
	public int getOtpType() {
		return otpType;
	}
	public void setOtpType(int otpType) {
		this.otpType = otpType;
	}

}
