package com.eot.banking.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SMSCampaignDTO {
	
	private Integer campaignId;
	private String target;
	private String title;
	private String message;
	private String mobileNumbers;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date fromDate;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date toDate;

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getMobileNumbers() {
		return mobileNumbers;
	}

	public void setMobileNumbers(String mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}
	
	
}
