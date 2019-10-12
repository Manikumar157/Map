package com.eot.banking.dto;

import com.eot.entity.MobileRequest;

public class RequestReinitDTO {
	
	private MobileRequest request ;
	
	private int status ;
	
	private String message ;

	public MobileRequest getRequest() {
		return request;
	}

	public void setRequest(MobileRequest request) {
		this.request = request;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
