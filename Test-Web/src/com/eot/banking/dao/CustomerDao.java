package com.eot.banking.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cbs.entity.CBSAccount;
import com.eot.banking.dto.OtpDTO;
import com.eot.banking.dto.QRCodeDTO;
import com.eot.banking.dto.SCSubscriptionDTO;
import com.eot.banking.dto.SmsSubscriptionDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Branch;
import com.eot.entity.BusinessPartner;
import com.eot.entity.City;
import com.eot.entity.CommisionSplits;
//import com.eot.entity.CommisionSplits;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerBankAccount;
import com.eot.entity.CustomerCard;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.CustomerScsubscription;
import com.eot.entity.CustomerSmsRuleDetail;
import com.eot.entity.Otp;
import com.eot.entity.PendingTransaction;
import com.eot.entity.Quarter;
import com.eot.entity.ServiceChargeSubscription;
import com.eot.entity.SmsAlertRuleValue;

// TODO: Auto-generated Javadoc
/**
 * The Interface CustomerDao.
 */
public interface CustomerDao extends BaseDao{

	/**
	 * Gets the customer by mobile number.
	 *
	 * @param mobileNumber the mobile number
	 * @return the customer by mobile number
	 */
	public Customer getCustomerByMobileNumber(String mobileNumber);
	
	/**
	 * Gets the customer by account number.
	 *
	 * @param accountNumber the account number
	 * @return the customer by account number
	 */
	public Customer getCustomerByAccountNumber(String accountNumber);
	
	/**
	 * Gets the application.
	 *
	 * @param referenceId the reference id
	 * @param referenceType the reference type
	 * @return the application
	 */
	public AppMaster getApplication(Long referenceId,Integer referenceType);
	
	/**
	 * Gets the customer list.
	 *
	 * @return the customer list
	 */
	public List<Customer> getCustomerList();
	
	/**
	 * Gets the customer.
	 *
	 * @param customerId the customer id
	 * @return the customer
	 */
	public Customer getCustomer(Long customerId);
	
	/**
	 * Search customer.
	 *
	 * @param bankId the bank id
	 * @param bankGroupId the bank group id
	 * @param customerName the customer name
	 * @param mobileNumber the mobile number
	 * @param bankID the bank id
	 * @param branchID the branch id
	 * @param countryId the country id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param toDate2 
	 * @param fromDate2 
	 * @param pageNumber the page number
	 * @param custType the cust type
	 * @param brId the br id
	 * @return the page
	 */
	public Page searchCustomer(Integer bankId,Integer bankGroupId, String firstName,String middleName, String lastName, String mobileNumber,String bankID,String branchID,String countryId,String fromDate,String toDate, Integer pageNumber,String custType,Long brId,Integer partnerId, String onBoardedBy,String businessName, Integer kycStatus,String channel);
	
	/**
	 * Find customer account.
	 *
	 * @param customerId the customer id
	 * @param bankId the bank id
	 * @return the customer account
	 */
	public CustomerAccount findCustomerAccount(Long customerId,Integer bankId);
	
	/**
	 * Gets the customer profiles.
	 *
	 * @param bankId the bank id
	 * @param pageNumber the page number
	 * @return the customer profiles
	 */
	public Page getCustomerProfiles(Integer bankId,int pageNumber);
	
	/**
	 * Edits the customer profile.
	 *
	 * @param profileId the profile id
	 * @return the customer profiles
	 */
	public CustomerProfiles editCustomerProfile(Integer profileId);
	
	/**
	 * Gets the mobile num length.
	 *
	 * @param countryId the country id
	 * @return the mobile num length
	 */
	public Integer getMobileNumLength(Integer countryId);
	
	/**
	 * Gets the customer profiles by bank id.
	 *
	 * @param bankId the bank id
	 * @return the customer profiles by bank id
	 */
	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId);
	
	/**
	 * Gets the customer profiles by bank ids.
	 *
	 * @param bankIds the bank ids
	 * @return the customer profiles by bank ids
	 */
	public List<CustomerProfiles> getCustomerProfilesByBankIds(Set<Integer> bankIds);
	
	/**
	 * Gets the customer profile by name.
	 *
	 * @param customerProfileName the customer profile name
	 * @param bankId the bank id
	 * @return the customer profile by name
	 */
	public List<CustomerProfiles> getCustomerProfileByName(String customerProfileName,Integer bankId);
	
	/**
	 * Gets the customer profile by name.
	 *
	 * @param customerProfileName the customer profile name
	 * @param bankId the bank id
	 * @param profileId the profile id
	 * @return the customer profile by name
	 */
	public CustomerProfiles getCustomerProfileByName(String customerProfileName,Integer bankId,Integer profileId);
	
	/**
	 * Gets the update app type.
	 *
	 * @param referenceId the reference id
	 * @return the update app type
	 */
	public AppMaster getUpdateAppType(Long referenceId);
	
	/**
	 * Export to xls for customer details.
	 *
	 * @param bankId the bank id
	 * @param bankGroupId the bank group id
	 * @param customerName the customer name
	 * @param mobileNumber the mobile number
	 * @param bankID the bank id
	 * @param branchId the branch id
	 * @param countryId the country id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param brId the br id
	 * @return the list
	 */
	public List exportToXLSForCustomerDetails(Integer bankId,Integer bankGroupId,String customerName,String mobileNumber, String bankID, String branchId,
			String countryId,String fromDate,String toDate,Long brId);
	
	/*public List exportToXLSForCustomerDetails(Integer bankId,Integer bankGroupId,String customerName,String mobileNumber, String bankID, String branchId,
			String countryId,String fromDate,String toDate,String custType,Long brId);*/
	
	/**
	 * Gets the cusomers.
	 *
	 * @return the cusomers
	 */
	public List<Customer> getCusomers();
	
	/**
	 * Gets the account.
	 *
	 * @param accountNumber the account number
	 * @return the account
	 */
	public Account getAccount(String accountNumber);
	
	/**
	 * Find customer account by bank branch.
	 *
	 * @param customerId the customer id
	 * @param bankId the bank id
	 * @param branchId the branch id
	 * @return the customer account
	 */
	public CustomerAccount findCustomerAccountByBankBranch(Long customerId,
			Integer bankId,Long branchId);
	
	/**
	 * Gets the cbs account details.
	 *
	 * @param customerBankAccountNumber the customer bank account number
	 * @param bankCode the bank code
	 * @param branchCode the branch code
	 * @return the cbs account details
	 */
	public CBSAccount getCbsAccountDetails(String customerBankAccountNumber,String bankCode,String branchCode);
	
	/**
	 * Gets the customer bank account details.
	 *
	 * @param customerBankAccountNumber the customer bank account number
	 * @return the customer bank account details
	 */
	public CustomerBankAccount getCustomerBankAccountDetails(
			String customerBankAccountNumber);
	
	/**
	 * Gets the customer bank account details.
	 *
	 * @param customerId the customer id
	 * @param pageNumber the page number
	 * @return the customer bank account details
	 */
	public Page getCustomerBankAccountDetails(Long customerId, int pageNumber);
	
	/**
	 * Gets the customer bank details.
	 *
	 * @param slNo the sl no
	 * @return the customer bank details
	 */
	public CustomerBankAccount getCustomerBankDetails(Long slNo);
	
	/**
	 * Gets the branch details.
	 *
	 * @param branchId the branch id
	 * @return the branch details
	 */
	public Branch getBranchDetails(Long branchId);
	
	/**
	 * Gets the branch details by code.
	 *
	 * @param branchCode the branch code
	 * @return the branch details by code
	 */
	public Branch getBranchDetailsByCode(String branchCode);
	
	/**
	 * Gets the customer card details.
	 *
	 * @param customerId the customer id
	 * @param pageNumber the page number
	 * @return the customer card details
	 */
	public Page getCustomerCardDetails(Long customerId, int pageNumber);
	
	/**
	 * Gets the customer card.
	 *
	 * @param cardId the card id
	 * @param cardNumber the card number
	 * @return the customer card
	 */
	public CustomerCard getCustomerCard(Long cardId,String cardNumber);
	
	/**
	 * Gets the customer card.
	 *
	 * @param cardId the card id
	 * @return the customer card
	 */
	public CustomerCard getCustomerCard(Long cardId);
	
	/**
	 * Gets the customer card.
	 *
	 * @param cardNo the card no
	 * @return the customer card
	 */
	public CustomerCard getCustomerCard(String cardNo);
	
	/**
	 * Gets the sms package list.
	 *
	 * @return the sms package list
	 */
	/*date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details*/
	public List<SmsAlertRuleValue> getSmsPackageList();
	
	/**
	 * Sms package details.
	 *
	 * @param packageId the package id
	 * @return the list
	 */
	public List<SmsSubscriptionDTO> smsPackageDetails(String packageId);
	
	
	/**
	 * Gets the current subscription.
	 *
	 * @param customerID the customer id
	 * @return the current subscription
	 */
	public CustomerSmsRuleDetail getCurrentSubscription(String customerID);

	/**
	 * Gets the all existing subscription.
	 *
	 * @param customerId the customer id
	 * @return the all existing subscription
	 */
	public List<CustomerSmsRuleDetail> getAllExistingSubscription(Long customerId);
	/*date:05/08/16 by:rajlaxmi for:showing only subscribed sc packages details*/
	public List<ServiceChargeSubscription> getSCPackageList();

	public List<SCSubscriptionDTO> scPackageDetails(String packageId);

	public CustomerScsubscription getSCCurrentSubscription(String customerId);
	public List<CustomerScsubscription> getSCSubscribedList(String customerID);
	public Customer getCustomerByEmailAddress(String emailAddress);

	public String getLanguageDescription(String languageCode);

	List<CustomerProfiles> loadCustomerProfiles(Integer bankId);
	List<Customer> loadAgentProcessTxnByDate(String date);


	Double loadTotalSharableAmountByAgent(Long agentId, String date,String bankRevinueAccount);
	
	public BusinessPartner getbusinessPartnere(long customerId);

	public Country getCountry(Integer countryId);

	public Object getCustomerProfilesByType(Integer bankId, String type);

	public City getCity(Integer cityId);

	public Quarter getQuarter(Long quarterId);

	public CustomerProfiles getCustomerProfiles(Integer customerProfileId);

	CustomerAccount findCustomerAccountBy(Long customerId);
	
	public String getAgentCode(Integer custTupe);

	Customer getAgentByAgentCode(String agentCode);
	
	public Otp verifyOTP(OtpDTO otpDTO) ;
	
	public AppMaster getApplicationByAppId(String appId);
	
	Account getAccountByAliasAndRef(String alias, String referenceId);

	String getBusinessPartnerCode();

	CustomerAccount findAgentCommissionAccount(Long customerId, Integer bankId);

	CustomerAccount findAgentCommissionAccountBy(Long customerId);

	public Page getCustomersQRCode(QRCodeDTO qrCodeDTO, Integer pageNumber);

	List<Customer> loadAgentProcessDepoTxnByDate(String date);

	Double loadTotalSharableAmountDeposite(Long agentId, String date, String bankRevnueAccount);
	
	List<Object[]> loadTotalCommissionByAgentId(Long agentId, String date,String bankRevnueAccount);
	
	List<CommisionSplits> getCommissionPercentage(Integer txnType);
	
	public Page searchCustomerWithTxnDate(Integer bankId,Integer bankGroupId, String firstName,String middleName, String lastName, String mobileNumber,String bankID,String branchID,String countryId,String fromDate,String toDate, Integer pageNumber,String custType,Long brId,Integer partnerId, String onBoardedBy,String businessName);

	Double getCommissionPercentageAgent(Integer txnIdCustomerApproval);

	public Page searchBlockedCustomers(String fromDate, String toDate, Integer pageNumber, String custType);
	
	public List<Object[]> getCustomerEnrolledUnder90DaysByKYCStatus(Integer kycStatus);
	
	public void updateCustomer(Long customerId, Integer status);
	
	public List<Object[]> lowBlanceCheckForAgent ();

	Boolean getMerchant(Integer custType, String merchantCode);

	public Page searchAgentCashOutData(TransactionParamDTO transactionParamDTO, Integer pageNumber);

	public PendingTransaction getPendingTransaction(Long transactionId);
}
