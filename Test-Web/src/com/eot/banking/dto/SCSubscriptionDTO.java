package com.eot.banking.dto;
public class SCSubscriptionDTO {
	
	private Long customerId;
	private Integer subscriptionType;
	private String packageName;
	private Double subscriptionCost;
	private int numberOfTxn;
	private com.eot.entity.ServiceChargeRule serviceChargeRule;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Integer getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(Integer subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Double getSubscriptionCost() {
		return subscriptionCost;
	}
	public void setSubscriptionCost(Double subscriptionCost) {
		this.subscriptionCost = subscriptionCost;
	}
	public int getNumberOfTxn() {
		return numberOfTxn;
	}
	public void setNumberOfTxn(int numberOfTxn) {
		this.numberOfTxn = numberOfTxn;
	}
	public com.eot.entity.ServiceChargeRule getServiceChargeRule() {
		return serviceChargeRule;
	}
	public void setServiceChargeRule(com.eot.entity.ServiceChargeRule serviceChargeRule) {
		this.serviceChargeRule = serviceChargeRule;
	}
	
}
