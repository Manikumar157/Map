package com.eot.banking.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class BillerDTO implements Serializable{
	private Integer billerId;
	@NotEmpty(message="NotEmpty.billerDTO.billerName")
	private String  billerName;
	@NotNull(message="NotNull.billerDTO.billerTypeId")
	private Integer billerTypeId;
	@NotNull(message="NotNull.billerDTO.countryId")
	private Integer countryId;
	private Integer partialPayments=1;
	//@NotNull(message="NotNull.billerDTO.bankId")
	private Integer bankId;
	private Long branchId;
	
	public Integer getBillerId() {
		return billerId;
	}
	public void setBillerId(Integer billerId) {
		this.billerId = billerId;
	}
	public String getBillerName() {
		return billerName;
	}
	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}
	public Integer getBillerTypeId() {
		return billerTypeId;
	}
	public void setBillerTypeId(Integer billerTypeId) {
		this.billerTypeId = billerTypeId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Integer getPartialPayments() {
		return partialPayments;
	}
	public void setPartialPayments(Integer partialPayments) {
		this.partialPayments = partialPayments;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	
}
