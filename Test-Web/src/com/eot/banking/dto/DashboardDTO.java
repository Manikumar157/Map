/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: DashboardDTO.java
*
* Date Author Changes
* 17 Dec, 2018 Sudhanshu Created
*/
package com.eot.banking.dto;

import com.eot.entity.WebUser;

/**
 * The Class DashboardDTO.
 */
public class DashboardDTO {
	
	/** The partner type. */
	private Integer partnerType;
	
	/** The partner id. */
	private Integer partnerId;
	
	/** The balance enquiry. */
	private String balanceEnquiry;
	
	/** The mini statement. */
	private String miniStatement;
	
	/** The cash deposit. */
	private String cashDeposit;
	
	/** The cash withdrawal. */
	private String cashWithdrawal;
	
	/** The sale. */
	private String sale;
	
	/** The bill payment. */
	private String billPayment;
	
	/** The topup. */
	private String topup;
	
	/** The send money. */
	private String sendMoney;
	
	/** The sms cash. */
	private String smsCash;
	
	/** The mgurush commission. */
	private String mgurushCommission;
	
	/** The agent sole merchant commssion. */
	private String agentSoleMerchantCommssion;
	
	/** The agent commission. */
	private String agentCommission;
	
	/** The sole merchant commission. */
	private String soleMerchantCommission;
	
	/** The agent sole merchant. */
	private String agentSoleMerchant;
	
	/** The sole merchant. */
	private String soleMerchant;
	
	/** The agent. */
	private String agent;
	
	/** The registered customer. */
	private String registeredCustomer;
	
	/** The signup customer. */
	private String signupCustomer;
	
	/** The total agent. */
	private String totalAgent;
	
	/** The total sole merchant. */
	private String totalSoleMerchant;
	
	/** The total agent sole merchant. */
	private String totalAgentSoleMerchant;
	
	/** The total registered customer. */
	private String totalRegisteredCustomer;
	
	/** The total signup customerr. */
	private String totalSignupCustomerr;
	
	/** The cum mgurush commission. */
	private String cumMgurushCommission;
	
	/** The cum sole merchant commssion. */
	private String cumSoleMerchantCommssion;
	
	/** The cum agent sole merchant commssion. */
	private String cumAgentSoleMerchantCommssion;
	
	/** The cum agent commission. */
	private String cumAgentCommission;
	
	/** The cum balance enquiry. */
	private String cumBalanceEnquiry;
	
	/** The cum mini statement. */
	private String cumMiniStatement;
	
	/** The cum cash deposit. */
	private String cumCashDeposit;
	
	/** The cum cash withdrawal. */
	private String cumCashWithdrawal;
	
	/** The cum sale. */
	private String cumSale;
	
	/** The cum top up. */
	private String cumTopUp;
	
	/** The cum sms cash. */
	private String cumSmsCash;
	
	/** The cum send money. */
	private String cumSendMoney;
	
	/** The cum bill payment. */
	private String cumBillPayment;
	
	/** The todays date. */
	private String todaysDate;
	
	/** The user name. */
	private String userName;
	
	/** The web user. */
	private WebUser webUser;
	
	/** The pay. */
	private String pay;
	
	/** The cum pay. */
	private String cumPay;
	
	/** The bank id. */
	private String bankId;
	
	/** The active agent. */
	private String activeAgent;
	
	/** The new agent. */
	private String newAgent;
	
	/** The active sole merchant. */
	private String activeSoleMerchant;
	
	/** The new sole merchant. */
	private String newSoleMerchant;
	
	/** The active agent sole merchant. */
	private String activeAgentSoleMerchant;
	
	/** The new agent sole merchant. */
	private String newAgentSoleMerchant;
	
	/** The active registered customer. */
	private String activeRegisteredCustomer;
	
	/** The new registered customer. */
	private String newRegisteredCustomer;
	
	/** The active sign up customer. */
	private String activeSignUpCustomer;
	
	/** The new sign up customer. */
	private String newSignUpCustomer;
	
	/** The float managment. */
	private String floatManagment;
	
	/** The cum float managment. */
	private String cumFloatManagment;
	
	/** The merchant payout. */
	private String merchantPayout;
	
	/** The cach in. */
	private String cashIn;
	
	/** The cash out. */
	private String cashOut;
	
	/** The transfer E money. */
	private String transferEMoney;
	
	/** The bulk payment. */
	private String bulkPayment;
	
	/** The kyc pending. */
	private String kycPending;
	
	/** The kyc approval pending. */
	private String kycApprovalPending;
	
	/** The kyc approved. */
	private String kycApproved;
	
	/** The kyc rejected. */
	private String kycRejected;
	
	/** The agent kyc pending. */
	private String agentKycPending;
	
	/** The agent kyc approval pending. */
	private String agentKycApprovalPending;
	
	/** The agent kyc approved. */
	private String agentKycApproved;
	
	/** The agent kyc rejected. */
	private String agentKycRejected;
	
	/** The merchant kyc pending. */
	private String merchantKycPending;
	
	/** The merchant kyc approval pending. */
	private String merchantKycApprovalPending;
	
	/** The merchant kyc approved. */
	private String merchantKycApproved;
	
	/** The merchant kyc rejected. */
	private String merchantKycRejected;
	
	/** The cum merchant payout. */
	private String cumMerchantPayout;
	
	/** The cum cash in. */
	private String cumCashIn;
	
	/** The cum cash out. */
	private String cumCashOut;
	
	/** The cum transfer E money. */
	private String cumTransferEMoney;
	
	/** The cum bulk payment. */
	private String cumBulkPayment;
	
	
	/**
	 * Gets the partner type.
	 *
	 * @return the partner type
	 */
	public Integer getPartnerType() {
		return partnerType;
	}

	/**
	 * Sets the partner type.
	 *
	 * @param partnerType the new partner type
	 */
	public void setPartnerType(Integer partnerType) {
		this.partnerType = partnerType;
	}

	/**
	 * Gets the partner id.
	 *
	 * @return the partner id
	 */
	public Integer getPartnerId() {
		return partnerId;
	}

	/**
	 * Sets the partner id.
	 *
	 * @param partnerId the new partner id
	 */
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * Gets the balance enquiry.
	 *
	 * @return the balance enquiry
	 */
	public String getBalanceEnquiry() {
		return balanceEnquiry;
	}

	/**
	 * Sets the balance enquiry.
	 *
	 * @param balanceEnquiry the new balance enquiry
	 */
	public void setBalanceEnquiry(String balanceEnquiry) {
		this.balanceEnquiry = balanceEnquiry;
	}

	/**
	 * Gets the mini statement.
	 *
	 * @return the mini statement
	 */
	public String getMiniStatement() {
		return miniStatement;
	}

	/**
	 * Sets the mini statement.
	 *
	 * @param miniStatement the new mini statement
	 */
	public void setMiniStatement(String miniStatement) {
		this.miniStatement = miniStatement;
	}

	/**
	 * Gets the cash deposit.
	 *
	 * @return the cash deposit
	 */
	public String getCashDeposit() {
		return cashDeposit;
	}

	/**
	 * Sets the cash deposit.
	 *
	 * @param cashDeposit the new cash deposit
	 */
	public void setCashDeposit(String cashDeposit) {
		this.cashDeposit = cashDeposit;
	}

	/**
	 * Gets the cash withdrawal.
	 *
	 * @return the cash withdrawal
	 */
	public String getCashWithdrawal() {
		return cashWithdrawal;
	}

	/**
	 * Sets the cash withdrawal.
	 *
	 * @param cashWithdrawal the new cash withdrawal
	 */
	public void setCashWithdrawal(String cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
	}

	/**
	 * Gets the sale.
	 *
	 * @return the sale
	 */
	public String getSale() {
		return sale;
	}

	/**
	 * Sets the sale.
	 *
	 * @param sale the new sale
	 */
	public void setSale(String sale) {
		this.sale = sale;
	}

	/**
	 * Gets the bill payment.
	 *
	 * @return the bill payment
	 */
	public String getBillPayment() {
		return billPayment;
	}

	/**
	 * Sets the bill payment.
	 *
	 * @param billPayment the new bill payment
	 */
	public void setBillPayment(String billPayment) {
		this.billPayment = billPayment;
	}

	/**
	 * Gets the topup.
	 *
	 * @return the topup
	 */
	public String getTopup() {
		return topup;
	}

	/**
	 * Sets the topup.
	 *
	 * @param topup the new topup
	 */
	public void setTopup(String topup) {
		this.topup = topup;
	}

	/**
	 * Gets the send money.
	 *
	 * @return the send money
	 */
	public String getSendMoney() {
		return sendMoney;
	}

	/**
	 * Sets the send money.
	 *
	 * @param sendMoney the new send money
	 */
	public void setSendMoney(String sendMoney) {
		this.sendMoney = sendMoney;
	}

	/**
	 * Gets the sms cash.
	 *
	 * @return the sms cash
	 */
	public String getSmsCash() {
		return smsCash;
	}

	/**
	 * Sets the sms cash.
	 *
	 * @param smsCash the new sms cash
	 */
	public void setSmsCash(String smsCash) {
		this.smsCash = smsCash;
	}

	/**
	 * Gets the mgurush commission.
	 *
	 * @return the mgurush commission
	 */
	public String getMgurushCommission() {
		return mgurushCommission;
	}

	/**
	 * Sets the mgurush commission.
	 *
	 * @param mgurushCommission the new mgurush commission
	 */
	public void setMgurushCommission(String mgurushCommission) {
		this.mgurushCommission = mgurushCommission;
	}

	/**
	 * Gets the agent sole merchant commssion.
	 *
	 * @return the agent sole merchant commssion
	 */
	public String getAgentSoleMerchantCommssion() {
		return agentSoleMerchantCommssion;
	}

	/**
	 * Sets the agent sole merchant commssion.
	 *
	 * @param agentSoleMerchantCommssion the new agent sole merchant commssion
	 */
	public void setAgentSoleMerchantCommssion(String agentSoleMerchantCommssion) {
		this.agentSoleMerchantCommssion = agentSoleMerchantCommssion;
	}

	/**
	 * Gets the agent commission.
	 *
	 * @return the agent commission
	 */
	public String getAgentCommission() {
		return agentCommission;
	}

	/**
	 * Sets the agent commission.
	 *
	 * @param agentCommission the new agent commission
	 */
	public void setAgentCommission(String agentCommission) {
		this.agentCommission = agentCommission;
	}

	/**
	 * Gets the sole merchant commission.
	 *
	 * @return the sole merchant commission
	 */
	public String getSoleMerchantCommission() {
		return soleMerchantCommission;
	}

	/**
	 * Sets the sole merchant commission.
	 *
	 * @param soleMerchantCommission the new sole merchant commission
	 */
	public void setSoleMerchantCommission(String soleMerchantCommission) {
		this.soleMerchantCommission = soleMerchantCommission;
	}

	/**
	 * Gets the agent sole merchant.
	 *
	 * @return the agent sole merchant
	 */
	public String getAgentSoleMerchant() {
		return agentSoleMerchant;
	}

	/**
	 * Sets the agent sole merchant.
	 *
	 * @param agentSoleMerchant the new agent sole merchant
	 */
	public void setAgentSoleMerchant(String agentSoleMerchant) {
		this.agentSoleMerchant = agentSoleMerchant;
	}

	/**
	 * Gets the sole merchant.
	 *
	 * @return the sole merchant
	 */
	public String getSoleMerchant() {
		return soleMerchant;
	}

	/**
	 * Sets the sole merchant.
	 *
	 * @param soleMerchant the new sole merchant
	 */
	public void setSoleMerchant(String soleMerchant) {
		this.soleMerchant = soleMerchant;
	}

	/**
	 * Gets the agent.
	 *
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * Sets the agent.
	 *
	 * @param agent the new agent
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * Gets the registered customer.
	 *
	 * @return the registered customer
	 */
	public String getRegisteredCustomer() {
		return registeredCustomer;
	}

	/**
	 * Sets the registered customer.
	 *
	 * @param registeredCustomer the new registered customer
	 */
	public void setRegisteredCustomer(String registeredCustomer) {
		this.registeredCustomer = registeredCustomer;
	}

	/**
	 * Gets the signup customer.
	 *
	 * @return the signup customer
	 */
	public String getSignupCustomer() {
		return signupCustomer;
	}

	/**
	 * Sets the signup customer.
	 *
	 * @param signupCustomer the new signup customer
	 */
	public void setSignupCustomer(String signupCustomer) {
		this.signupCustomer = signupCustomer;
	}

	/**
	 * Gets the total agent.
	 *
	 * @return the total agent
	 */
	public String getTotalAgent() {
		return totalAgent;
	}

	/**
	 * Sets the total agent.
	 *
	 * @param totalAgent the new total agent
	 */
	public void setTotalAgent(String totalAgent) {
		this.totalAgent = totalAgent;
	}

	/**
	 * Gets the total sole merchant.
	 *
	 * @return the total sole merchant
	 */
	public String getTotalSoleMerchant() {
		return totalSoleMerchant;
	}

	/**
	 * Sets the total sole merchant.
	 *
	 * @param totalSoleMerchant the new total sole merchant
	 */
	public void setTotalSoleMerchant(String totalSoleMerchant) {
		this.totalSoleMerchant = totalSoleMerchant;
	}

	/**
	 * Gets the total agent sole merchant.
	 *
	 * @return the total agent sole merchant
	 */
	public String getTotalAgentSoleMerchant() {
		return totalAgentSoleMerchant;
	}

	/**
	 * Sets the total agent sole merchant.
	 *
	 * @param totalAgentSoleMerchant the new total agent sole merchant
	 */
	public void setTotalAgentSoleMerchant(String totalAgentSoleMerchant) {
		this.totalAgentSoleMerchant = totalAgentSoleMerchant;
	}

	/**
	 * Gets the total registered customer.
	 *
	 * @return the total registered customer
	 */
	public String getTotalRegisteredCustomer() {
		return totalRegisteredCustomer;
	}

	/**
	 * Sets the total registered customer.
	 *
	 * @param totalRegisteredCustomer the new total registered customer
	 */
	public void setTotalRegisteredCustomer(String totalRegisteredCustomer) {
		this.totalRegisteredCustomer = totalRegisteredCustomer;
	}

	/**
	 * Gets the total signup customerr.
	 *
	 * @return the total signup customerr
	 */
	public String getTotalSignupCustomerr() {
		return totalSignupCustomerr;
	}

	/**
	 * Sets the total signup customerr.
	 *
	 * @param totalSignupCustomerr the new total signup customerr
	 */
	public void setTotalSignupCustomerr(String totalSignupCustomerr) {
		this.totalSignupCustomerr = totalSignupCustomerr;
	}

	/**
	 * Gets the cum mgurush commission.
	 *
	 * @return the cum mgurush commission
	 */
	public String getCumMgurushCommission() {
		return cumMgurushCommission;
	}

	/**
	 * Sets the cum mgurush commission.
	 *
	 * @param cumMgurushCommission the new cum mgurush commission
	 */
	public void setCumMgurushCommission(String cumMgurushCommission) {
		this.cumMgurushCommission = cumMgurushCommission;
	}

	/**
	 * Gets the cum sole merchant commssion.
	 *
	 * @return the cum sole merchant commssion
	 */
	public String getCumSoleMerchantCommssion() {
		return cumSoleMerchantCommssion;
	}

	/**
	 * Sets the cum sole merchant commssion.
	 *
	 * @param cumSoleMerchantCommssion the new cum sole merchant commssion
	 */
	public void setCumSoleMerchantCommssion(String cumSoleMerchantCommssion) {
		this.cumSoleMerchantCommssion = cumSoleMerchantCommssion;
	}

	/**
	 * Gets the cum agent sole merchant commssion.
	 *
	 * @return the cum agent sole merchant commssion
	 */
	public String getCumAgentSoleMerchantCommssion() {
		return cumAgentSoleMerchantCommssion;
	}

	/**
	 * Sets the cum agent sole merchant commssion.
	 *
	 * @param cumAgentSoleMerchantCommssion the new cum agent sole merchant commssion
	 */
	public void setCumAgentSoleMerchantCommssion(String cumAgentSoleMerchantCommssion) {
		this.cumAgentSoleMerchantCommssion = cumAgentSoleMerchantCommssion;
	}

	/**
	 * Gets the cum agent commission.
	 *
	 * @return the cum agent commission
	 */
	public String getCumAgentCommission() {
		return cumAgentCommission;
	}

	/**
	 * Sets the cum agent commission.
	 *
	 * @param cumAgentCommission the new cum agent commission
	 */
	public void setCumAgentCommission(String cumAgentCommission) {
		this.cumAgentCommission = cumAgentCommission;
	}

	/**
	 * Gets the cum balance enquiry.
	 *
	 * @return the cum balance enquiry
	 */
	public String getCumBalanceEnquiry() {
		return cumBalanceEnquiry;
	}

	/**
	 * Sets the cum balance enquiry.
	 *
	 * @param cumBalanceEnquiry the new cum balance enquiry
	 */
	public void setCumBalanceEnquiry(String cumBalanceEnquiry) {
		this.cumBalanceEnquiry = cumBalanceEnquiry;
	}

	/**
	 * Gets the cum mini statement.
	 *
	 * @return the cum mini statement
	 */
	public String getCumMiniStatement() {
		return cumMiniStatement;
	}

	/**
	 * Sets the cum mini statement.
	 *
	 * @param cumMiniStatement the new cum mini statement
	 */
	public void setCumMiniStatement(String cumMiniStatement) {
		this.cumMiniStatement = cumMiniStatement;
	}

	/**
	 * Gets the cum cash deposit.
	 *
	 * @return the cum cash deposit
	 */
	public String getCumCashDeposit() {
		return cumCashDeposit;
	}

	/**
	 * Sets the cum cash deposit.
	 *
	 * @param cumCashDeposit the new cum cash deposit
	 */
	public void setCumCashDeposit(String cumCashDeposit) {
		this.cumCashDeposit = cumCashDeposit;
	}

	/**
	 * Gets the cum cash withdrawal.
	 *
	 * @return the cum cash withdrawal
	 */
	public String getCumCashWithdrawal() {
		return cumCashWithdrawal;
	}

	/**
	 * Sets the cum cash withdrawal.
	 *
	 * @param cumCashWithdrawal the new cum cash withdrawal
	 */
	public void setCumCashWithdrawal(String cumCashWithdrawal) {
		this.cumCashWithdrawal = cumCashWithdrawal;
	}

	/**
	 * Gets the cum sale.
	 *
	 * @return the cum sale
	 */
	public String getCumSale() {
		return cumSale;
	}

	/**
	 * Sets the cum sale.
	 *
	 * @param cumSale the new cum sale
	 */
	public void setCumSale(String cumSale) {
		this.cumSale = cumSale;
	}

	/**
	 * Gets the cum top up.
	 *
	 * @return the cum top up
	 */
	public String getCumTopUp() {
		return cumTopUp;
	}

	/**
	 * Sets the cum top up.
	 *
	 * @param cumTopUp the new cum top up
	 */
	public void setCumTopUp(String cumTopUp) {
		this.cumTopUp = cumTopUp;
	}

	/**
	 * Gets the cum sms cash.
	 *
	 * @return the cum sms cash
	 */
	public String getCumSmsCash() {
		return cumSmsCash;
	}

	/**
	 * Sets the cum sms cash.
	 *
	 * @param cumSmsCash the new cum sms cash
	 */
	public void setCumSmsCash(String cumSmsCash) {
		this.cumSmsCash = cumSmsCash;
	}

	/**
	 * Gets the cum send money.
	 *
	 * @return the cum send money
	 */
	public String getCumSendMoney() {
		return cumSendMoney;
	}

	/**
	 * Sets the cum send money.
	 *
	 * @param cumSendMoney the new cum send money
	 */
	public void setCumSendMoney(String cumSendMoney) {
		this.cumSendMoney = cumSendMoney;
	}

	/**
	 * Gets the cum bill payment.
	 *
	 * @return the cum bill payment
	 */
	public String getCumBillPayment() {
		return cumBillPayment;
	}

	/**
	 * Sets the cum bill payment.
	 *
	 * @param cumBillPayment the new cum bill payment
	 */
	public void setCumBillPayment(String cumBillPayment) {
		this.cumBillPayment = cumBillPayment;
	}

	/**
	 * Gets the todays date.
	 *
	 * @return the todays date
	 */
	public String getTodaysDate() {
		return todaysDate;
	}

	/**
	 * Sets the todays date.
	 *
	 * @param todaysDate the new todays date
	 */
	public void setTodaysDate(String todaysDate) {
		this.todaysDate = todaysDate;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the web user.
	 *
	 * @return the web user
	 */
	public WebUser getWebUser() {
		return webUser;
	}

	/**
	 * Sets the web user.
	 *
	 * @param webUser the new web user
	 */
	public void setWebUser(WebUser webUser) {
		this.webUser = webUser;
	}

	/**
	 * Gets the pay.
	 *
	 * @return the pay
	 */
	public String getPay() {
		return pay;
	}

	/**
	 * Sets the pay.
	 *
	 * @param pay the new pay
	 */
	public void setPay(String pay) {
		this.pay = pay;
	}

	/**
	 * Gets the cum pay.
	 *
	 * @return the cum pay
	 */
	public String getCumPay() {
		return cumPay;
	}

	/**
	 * Sets the cum pay.
	 *
	 * @param cumPay the new cum pay
	 */
	public void setCumPay(String cumPay) {
		this.cumPay = cumPay;
	}

	/**
	 * Gets the bank id.
	 *
	 * @return the bank id
	 */
	public String getBankId() {
		return bankId;
	}

	/**
	 * Sets the bank id.
	 *
	 * @param bankId the new bank id
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	/**
	 * Gets the active agent.
	 *
	 * @return the active agent
	 */
	public String getActiveAgent() {
		return activeAgent;
	}

	/**
	 * Sets the active agent.
	 *
	 * @param activeAgent the new active agent
	 */
	public void setActiveAgent(String activeAgent) {
		this.activeAgent = activeAgent;
	}

	/**
	 * Gets the new agent.
	 *
	 * @return the new agent
	 */
	public String getNewAgent() {
		return newAgent;
	}

	/**
	 * Sets the new agent.
	 *
	 * @param newAgent the new new agent
	 */
	public void setNewAgent(String newAgent) {
		this.newAgent = newAgent;
	}

	/**
	 * Gets the active sole merchant.
	 *
	 * @return the active sole merchant
	 */
	public String getActiveSoleMerchant() {
		return activeSoleMerchant;
	}

	/**
	 * Sets the active sole merchant.
	 *
	 * @param activeSoleMerchant the new active sole merchant
	 */
	public void setActiveSoleMerchant(String activeSoleMerchant) {
		this.activeSoleMerchant = activeSoleMerchant;
	}

	/**
	 * Gets the new sole merchant.
	 *
	 * @return the new sole merchant
	 */
	public String getNewSoleMerchant() {
		return newSoleMerchant;
	}

	/**
	 * Sets the new sole merchant.
	 *
	 * @param newSoleMerchant the new new sole merchant
	 */
	public void setNewSoleMerchant(String newSoleMerchant) {
		this.newSoleMerchant = newSoleMerchant;
	}

	/**
	 * Gets the active agent sole merchant.
	 *
	 * @return the active agent sole merchant
	 */
	public String getActiveAgentSoleMerchant() {
		return activeAgentSoleMerchant;
	}

	/**
	 * Sets the active agent sole merchant.
	 *
	 * @param activeAgentSoleMerchant the new active agent sole merchant
	 */
	public void setActiveAgentSoleMerchant(String activeAgentSoleMerchant) {
		this.activeAgentSoleMerchant = activeAgentSoleMerchant;
	}

	/**
	 * Gets the new agent sole merchant.
	 *
	 * @return the new agent sole merchant
	 */
	public String getNewAgentSoleMerchant() {
		return newAgentSoleMerchant;
	}

	/**
	 * Sets the new agent sole merchant.
	 *
	 * @param newAgentSoleMerchant the new new agent sole merchant
	 */
	public void setNewAgentSoleMerchant(String newAgentSoleMerchant) {
		this.newAgentSoleMerchant = newAgentSoleMerchant;
	}

	/**
	 * Gets the active registered customer.
	 *
	 * @return the active registered customer
	 */
	public String getActiveRegisteredCustomer() {
		return activeRegisteredCustomer;
	}

	/**
	 * Sets the active registered customer.
	 *
	 * @param activeRegisteredCustomer the new active registered customer
	 */
	public void setActiveRegisteredCustomer(String activeRegisteredCustomer) {
		this.activeRegisteredCustomer = activeRegisteredCustomer;
	}

	/**
	 * Gets the new registered customer.
	 *
	 * @return the new registered customer
	 */
	public String getNewRegisteredCustomer() {
		return newRegisteredCustomer;
	}

	/**
	 * Sets the new registered customer.
	 *
	 * @param newRegisteredCustomer the new new registered customer
	 */
	public void setNewRegisteredCustomer(String newRegisteredCustomer) {
		this.newRegisteredCustomer = newRegisteredCustomer;
	}

	/**
	 * Gets the active sign up customer.
	 *
	 * @return the active sign up customer
	 */
	public String getActiveSignUpCustomer() {
		return activeSignUpCustomer;
	}

	/**
	 * Sets the active sign up customer.
	 *
	 * @param activeSignUpCustomer the new active sign up customer
	 */
	public void setActiveSignUpCustomer(String activeSignUpCustomer) {
		this.activeSignUpCustomer = activeSignUpCustomer;
	}

	/**
	 * Gets the new sign up customer.
	 *
	 * @return the new sign up customer
	 */
	public String getNewSignUpCustomer() {
		return newSignUpCustomer;
	}

	/**
	 * Sets the new sign up customer.
	 *
	 * @param newSignUpCustomer the new new sign up customer
	 */
	public void setNewSignUpCustomer(String newSignUpCustomer) {
		this.newSignUpCustomer = newSignUpCustomer;
	}

	/**
	 * Gets the float managment.
	 *
	 * @return the float managment
	 */
	public String getFloatManagment() {
		return floatManagment;
	}

	/**
	 * Sets the float managment.
	 *
	 * @param floatManagment the new float managment
	 */
	public void setFloatManagment(String floatManagment) {
		this.floatManagment = floatManagment;
	}

	/**
	 * Gets the cum float managment.
	 *
	 * @return the cum float managment
	 */
	public String getCumFloatManagment() {
		return cumFloatManagment;
	}

	/**
	 * Sets the cum float managment.
	 *
	 * @param cumFloatManagment the new cum float managment
	 */
	public void setCumFloatManagment(String cumFloatManagment) {
		this.cumFloatManagment = cumFloatManagment;
	}

	/**
	 * Gets the merchant payout.
	 *
	 * @return the merchantPayout
	 */
	public String getMerchantPayout() {
		return merchantPayout;
	}

	/**
	 * Sets the merchant payout.
	 *
	 * @param merchantPayout the merchantPayout to set
	 */
	public void setMerchantPayout(String merchantPayout) {
		this.merchantPayout = merchantPayout;
	}

	

	/**
	 * Gets the cash in.
	 *
	 * @return the cashIn
	 */
	public String getCashIn() {
		return cashIn;
	}

	/**
	 * Sets the cash in.
	 *
	 * @param cashIn the cashIn to set
	 */
	public void setCashIn(String cashIn) {
		this.cashIn = cashIn;
	}

	/**
	 * Gets the cash out.
	 *
	 * @return the cashOut
	 */
	public String getCashOut() {
		return cashOut;
	}

	/**
	 * Sets the cash out.
	 *
	 * @param cashOut the cashOut to set
	 */
	public void setCashOut(String cashOut) {
		this.cashOut = cashOut;
	}

	/**
	 * Gets the transfer E money.
	 *
	 * @return the transferEMoney
	 */
	public String getTransferEMoney() {
		return transferEMoney;
	}

	/**
	 * Sets the transfer E money.
	 *
	 * @param transferEMoney the transferEMoney to set
	 */
	public void setTransferEMoney(String transferEMoney) {
		this.transferEMoney = transferEMoney;
	}

	/**
	 * Gets the bulk payment.
	 *
	 * @return the bulkPayment
	 */
	public String getBulkPayment() {
		return bulkPayment;
	}

	/**
	 * Sets the bulk payment.
	 *
	 * @param bulkPayment the bulkPayment to set
	 */
	public void setBulkPayment(String bulkPayment) {
		this.bulkPayment = bulkPayment;
	}

	/**
	 * Gets the kyc pending.
	 *
	 * @return the kycPending
	 */
	public String getKycPending() {
		return kycPending;
	}

	/**
	 * Sets the kyc pending.
	 *
	 * @param kycPending the kycPending to set
	 */
	public void setKycPending(String kycPending) {
		this.kycPending = kycPending;
	}

	/**
	 * Gets the kyc approval pending.
	 *
	 * @return the kycApprovalPending
	 */
	public String getKycApprovalPending() {
		return kycApprovalPending;
	}

	/**
	 * Sets the kyc approval pending.
	 *
	 * @param kycApprovalPending the kycApprovalPending to set
	 */
	public void setKycApprovalPending(String kycApprovalPending) {
		this.kycApprovalPending = kycApprovalPending;
	}

	/**
	 * Gets the kyc approved.
	 *
	 * @return the kycApproved
	 */
	public String getKycApproved() {
		return kycApproved;
	}

	/**
	 * Sets the kyc approved.
	 *
	 * @param kycApproved the kycApproved to set
	 */
	public void setKycApproved(String kycApproved) {
		this.kycApproved = kycApproved;
	}

	/**
	 * Gets the kyc rejected.
	 *
	 * @return the kycRejected
	 */
	public String getKycRejected() {
		return kycRejected;
	}

	/**
	 * Sets the kyc rejected.
	 *
	 * @param kycRejected the kycRejected to set
	 */
	public void setKycRejected(String kycRejected) {
		this.kycRejected = kycRejected;
	}

	/**
	 * Gets the agent kyc pending.
	 *
	 * @return the agentKycPending
	 */
	public String getAgentKycPending() {
		return agentKycPending;
	}

	/**
	 * Sets the agent kyc pending.
	 *
	 * @param agentKycPending the agentKycPending to set
	 */
	public void setAgentKycPending(String agentKycPending) {
		this.agentKycPending = agentKycPending;
	}

	/**
	 * Gets the agent kyc approval pending.
	 *
	 * @return the agentKycApprovalPending
	 */
	public String getAgentKycApprovalPending() {
		return agentKycApprovalPending;
	}

	/**
	 * Sets the agent kyc approval pending.
	 *
	 * @param agentKycApprovalPending the agentKycApprovalPending to set
	 */
	public void setAgentKycApprovalPending(String agentKycApprovalPending) {
		this.agentKycApprovalPending = agentKycApprovalPending;
	}

	/**
	 * Gets the agent kyc approved.
	 *
	 * @return the agentKycApproved
	 */
	public String getAgentKycApproved() {
		return agentKycApproved;
	}

	/**
	 * Sets the agent kyc approved.
	 *
	 * @param agentKycApproved the agentKycApproved to set
	 */
	public void setAgentKycApproved(String agentKycApproved) {
		this.agentKycApproved = agentKycApproved;
	}

	/**
	 * Gets the agent kyc rejected.
	 *
	 * @return the agentKycRejected
	 */
	public String getAgentKycRejected() {
		return agentKycRejected;
	}

	/**
	 * Sets the agent kyc rejected.
	 *
	 * @param agentKycRejected the agentKycRejected to set
	 */
	public void setAgentKycRejected(String agentKycRejected) {
		this.agentKycRejected = agentKycRejected;
	}

	/**
	 * Gets the merchant kyc pending.
	 *
	 * @return the merchantKycPending
	 */
	public String getMerchantKycPending() {
		return merchantKycPending;
	}

	/**
	 * Sets the merchant kyc pending.
	 *
	 * @param merchantKycPending the merchantKycPending to set
	 */
	public void setMerchantKycPending(String merchantKycPending) {
		this.merchantKycPending = merchantKycPending;
	}

	/**
	 * Gets the merchant kyc approval pending.
	 *
	 * @return the merchantKycApprovalPending
	 */
	public String getMerchantKycApprovalPending() {
		return merchantKycApprovalPending;
	}

	/**
	 * Sets the merchant kyc approval pending.
	 *
	 * @param merchantKycApprovalPending the merchantKycApprovalPending to set
	 */
	public void setMerchantKycApprovalPending(String merchantKycApprovalPending) {
		this.merchantKycApprovalPending = merchantKycApprovalPending;
	}

	/**
	 * Gets the merchant kyc approved.
	 *
	 * @return the merchantKycApproved
	 */
	public String getMerchantKycApproved() {
		return merchantKycApproved;
	}

	/**
	 * Sets the merchant kyc approved.
	 *
	 * @param merchantKycApproved the merchantKycApproved to set
	 */
	public void setMerchantKycApproved(String merchantKycApproved) {
		this.merchantKycApproved = merchantKycApproved;
	}

	/**
	 * Gets the merchant kyc rejected.
	 *
	 * @return the merchantKycRejected
	 */
	public String getMerchantKycRejected() {
		return merchantKycRejected;
	}

	/**
	 * Sets the merchant kyc rejected.
	 *
	 * @param merchantKycRejected the merchantKycRejected to set
	 */
	public void setMerchantKycRejected(String merchantKycRejected) {
		this.merchantKycRejected = merchantKycRejected;
	}

	/**
	 * Gets the cum merchant payout.
	 *
	 * @return the cumMerchantPayout
	 */
	public String getCumMerchantPayout() {
		return cumMerchantPayout;
	}

	/**
	 * Sets the cum merchant payout.
	 *
	 * @param cumMerchantPayout the cumMerchantPayout to set
	 */
	public void setCumMerchantPayout(String cumMerchantPayout) {
		this.cumMerchantPayout = cumMerchantPayout;
	}

	/**
	 * Gets the cum cash in.
	 *
	 * @return the cumCashIn
	 */
	public String getCumCashIn() {
		return cumCashIn;
	}

	/**
	 * Sets the cum cash in.
	 *
	 * @param cumCashIn the cumCashIn to set
	 */
	public void setCumCashIn(String cumCashIn) {
		this.cumCashIn = cumCashIn;
	}

	/**
	 * Gets the cum cash out.
	 *
	 * @return the cumCashOut
	 */
	public String getCumCashOut() {
		return cumCashOut;
	}

	/**
	 * Sets the cum cash out.
	 *
	 * @param cumCashOut the cumCashOut to set
	 */
	public void setCumCashOut(String cumCashOut) {
		this.cumCashOut = cumCashOut;
	}

	/**
	 * Gets the cum transfer E money.
	 *
	 * @return the cumTransferEMoney
	 */
	public String getCumTransferEMoney() {
		return cumTransferEMoney;
	}

	/**
	 * Sets the cum transfer E money.
	 *
	 * @param cumTransferEMoney the cumTransferEMoney to set
	 */
	public void setCumTransferEMoney(String cumTransferEMoney) {
		this.cumTransferEMoney = cumTransferEMoney;
	}

	/**
	 * Gets the cum bulk payment.
	 *
	 * @return the cumBulkPayment
	 */
	public String getCumBulkPayment() {
		return cumBulkPayment;
	}

	/**
	 * Sets the cum bulk payment.
	 *
	 * @param cumBulkPayment the cumBulkPayment to set
	 */
	public void setCumBulkPayment(String cumBulkPayment) {
		this.cumBulkPayment = cumBulkPayment;
	}

}
