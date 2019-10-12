package com.eot.banking.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class BranchDTO implements Serializable{
	
	private String serialNum;
	
	@NotEmpty(message="NotEmpty.branchDTO.location")
	private String location;
	@NotEmpty(message="NotEmpty.branchDTO.address")
	private String address;
	private String quarter;
	private String city;
	private String description;
	private Integer bankId;
	private Long branchId;
	private Integer countryId;
	@NotNull(message="NotNull.branchDTO.cityId")
	private Integer cityId;
	@NotNull(message="NotNull.branchDTO.quarterId")
	private Long quarterId;
	private String branchCode;
	public Long getBranchId() {
		return branchId;
	}
	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getQuarter() {
		return quarter;
	}
	public String getCity() {
		return city;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Long getQuarterId() {
		return quarterId;
	}
	public void setQuarterId(Long quarterId) {
		this.quarterId = quarterId;
	}
	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}
	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

}
