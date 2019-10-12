package com.eot.banking.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginUserDTO {
	
	
	@NotEmpty
	private String oldPassword;
	
	@NotEmpty
	private String newPassword;
	
	@NotEmpty
	private String rePassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
    public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	
	
	
	
}
