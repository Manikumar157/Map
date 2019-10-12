package com.eot.banking.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

public class TxnRuleSearchDTO implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 677574037865677317L;

	private String ruleName;
	
	private Integer ruleLevel;
	
	private Integer bankId;
	
	private String accountNumber;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date applicableFrom;
	
	@DateTimeFormat(pattern="dd/MM/yyyy") 
	private Date applicableTo;
	
	private Integer pageNumber = 1 ;
	
	private Integer txnType;
	
	private Set<Integer> profileIds;
	
	private Integer profileId;
	
	private Integer bankGroupId;
	
	private Integer roleId;

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Integer getBankGroupId() {
		return bankGroupId;
	}

	public void setBankGroupId(Integer bankGroupId) {
		this.bankGroupId = bankGroupId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getRuleLevel() {
		return ruleLevel;
	}

	public void setRuleLevel(Integer ruleLevel) {
		this.ruleLevel = ruleLevel;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getApplicableFrom() {
		return applicableFrom;
	}

	public void setApplicableFrom(Date applicableFrom) {
		this.applicableFrom = applicableFrom;
	}

	public Date getApplicableTo() {
		return applicableTo;
	}

	public void setApplicableTo(Date applicableTo) {
		this.applicableTo = applicableTo;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getTxnType() {
		return txnType;
	}

	public void setTxnType(Integer txnType) {
		this.txnType = txnType;
	}

	public Set<Integer> getProfileIds() {
		return profileIds;
	}

	public void setProfileIds(Set<Integer> profileIds) {
		this.profileIds = profileIds;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
		
}
