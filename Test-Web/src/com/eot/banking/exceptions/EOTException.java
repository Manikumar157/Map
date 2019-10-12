package com.eot.banking.exceptions;


public class EOTException extends Exception {

	private static final long serialVersionUID = 1L;
	private String errorCode;
   
	public EOTException(String errorCode) {
        this.errorCode = errorCode;
    }
	public String getErrorCode() {
		return errorCode;
	}

}
