package com.eot.banking.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class BankGroupDTO {
	
	private Integer bankGroupId;
	
	@NotEmpty(message="NotEmpty.bankGroupDTO.bankGroupName")
    private String bankGroupName;	
	@NotEmpty(message="NotEmpty.bankGroupDTO.bankGroupShortName")
    private String bankGroupShortName;
    
	public Integer getBankGroupId() {
		return bankGroupId;
	}
	public void setBankGroupId(Integer bankGroupId) {
		this.bankGroupId = bankGroupId;
	}
	public String getBankGroupName() {
		return bankGroupName;
	}
	public void setBankGroupName(String bankGroupName) {
		this.bankGroupName = bankGroupName;
	}
	public String getBankGroupShortName() {
		return bankGroupShortName;
	}
	public void setBankGroupShortName(String bankGroupShortName) {
		this.bankGroupShortName = bankGroupShortName;
	}

}
