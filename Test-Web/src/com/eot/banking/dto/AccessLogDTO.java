package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AccessLogDTO implements Serializable {
	
	private String module;
	private String userId;
	private String failureLogin;
	
	private String fromDate;
	
	private String toDate;
	
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
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFailureLogin() {
		return failureLogin;
	}
	public void setFailureLogin(String failureLogin) {
		this.failureLogin = failureLogin;
	}

}
