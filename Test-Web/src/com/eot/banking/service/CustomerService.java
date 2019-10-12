/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: CustomerService.java
*
* Date Author Changes
* 23 May, 2016 Swadhin Created
*/
package com.eot.banking.service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.eot.banking.dto.BankAccountDTO;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.CustomerProfileDTO;
import com.eot.banking.dto.QRCodeDTO;
import com.eot.banking.dto.SCSubscriptionDTO;
import com.eot.banking.dto.SmsSubscriptionDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.City;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.CustomerScsubscription;
import com.eot.entity.CustomerSmsRuleDetail;
import com.eot.entity.Quarter;
import com.eot.entity.SecurityQuestion;
import com.eot.entity.ServiceChargeSubscription;
import com.eot.entity.SmsAlertRuleValue;
import com.eot.entity.WebUser;

/**
 * The Interface CustomerService.
 */
public interface CustomerService {

	/**
	 * Save customer.
	 *
	 * @param customerDTO the customer dto
	 * @param roleId 
	 * @return the long
	 * @throws Exception the exception
	 */
	Long saveCustomer(CustomerDTO customerDTO) throws Exception;

	/**
	 * Update customer.
	 *
	 * @param customerDTO the customer dto
	 * @throws Exception the exception
	 */
	void updateCustomer(CustomerDTO customerDTO) throws Exception;

	/**
	 * Gets the photo details.
	 *
	 * @param customerId the customer id
	 * @param type the type
	 * @return the photo details
	 * @throws EOTException the eOT exception
	 */
	byte[] getPhotoDetails(Long customerId,String type) throws EOTException;

	/**
	 * Reset customer pin.
	 *
	 * @param customerId the customer id
	 * @throws EOTException the eOT exception
	 */
	void resetCustomerPin(Long customerId)throws Exception;

	/**
	 * Reinstall application.
	 *
	 * @param customerId the customer id
	 * @throws Exception the exception
	 */
	void reinstallApplication(Long customerId)throws Exception;

	/**
	 * Change application status.
	 *
	 * @param customerId the customer id
	 * @param status the status
	 * @throws EOTException the eOT exception
	 */
	void changeApplicationStatus(Long customerId, Integer status,String reasonForBlock) throws EOTException;

	/**
	 * Change customer status.
	 *
	 * @param customerId the customer id
	 * @param status the status
	 * @throws EOTException the eOT exception
	 */
	void changeCustomerStatus(Long customerId,Integer status, String reasonForDeActivate)throws EOTException;

	/**
	 * Gets the master data.
	 *
	 * @param language the language
	 * @return the master data
	 * @throws EOTException the eOT exception
	 */
	Map<String,Object> getMasterData(String language)throws EOTException;

	/**
	 * Gets the city list.
	 *
	 * @param countryId the country id
	 * @return the city list
	 * @throws EOTException the eOT exception
	 */
	List<City> getCityList(Integer countryId)throws EOTException;

	/**
	 * Gets the quarter list.
	 *
	 * @param cityId the city id
	 * @return the quarter list
	 * @throws EOTException the eOT exception
	 */
	List<Quarter> getQuarterList(Integer cityId)throws EOTException;

	/**
	 * Gets the customer details.
	 *
	 * @param customerId the customer id
	 * @param userName the user name
	 * @return the customer details
	 * @throws EOTException the eOT exception
	 */
	CustomerDTO getCustomerDetails(Long customerId,String userName) throws EOTException;

	/**
	 * Search customers.
	 *
	 * @param userName the user name
	 * @param bankGroupId the bank group id
	 * @param customerName the customer name
	 * @param mobileNumber the mobile number
	 * @param bankId the bank id
	 * @param branchId the branch id
	 * @param countryId the country id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param pageNumber the page number
	 * @param custType the cust type
	 * @return the page
	 * @throws EOTException the eOT exception
	 */
	Page searchCustomers(String userName, String bankGroupId,String firstName,String middleName,String lastName,String mobileNumber,String bankId,String branchId,String countryId,String fromDate,String toDate,Integer pageNumber,String custType,String onBoardedBy,String businessName,Integer kycStatus,String channel) throws EOTException;

	/**
	 * Gets the customer by mobile number.
	 *
	 * @param mobileNumber the mobile number
	 * @return the customer by mobile number
	 * @throws EOTException the eOT exception
	 */
	Customer getCustomerByMobileNumber(String mobileNumber) throws EOTException;

	/**
	 * Find customer account.
	 *
	 * @param customerId the customer id
	 * @param userName the user name
	 * @return the customer account
	 * @throws EOTException the eOT exception
	 */
	CustomerAccount findCustomerAccount(Long customerId,String userName) throws EOTException;

	/**
	 * Creates the customer account.
	 *
	 * @param mobileNumber the mobile number
	 * @throws Exception the exception
	 */
	void createCustomerAccount(String mobileNumber) throws Exception;

	/**
	 * Gets the customer profiles.
	 *
	 * @param pageNumber the page number
	 * @param requestPage the request page
	 * @return the customer profiles
	 * @throws EOTException the eOT exception
	 */
	public Page getCustomerProfiles(Integer pageNumber,String requestPage) throws EOTException;

	/**
	 * Save customer profile.
	 *
	 * @param customerProfileDTO the customer profile dto
	 * @return the string
	 * @throws EOTException the eOT exception
	 */
	public String saveCustomerProfile(CustomerProfileDTO customerProfileDTO) throws EOTException;

	/**
	 * Edits the customer profile.
	 *
	 * @param customerProfileDTO the customer profile dto
	 * @return the customer profile dto
	 * @throws EOTException the eOT exception
	 */
	public CustomerProfileDTO editCustomerProfile(CustomerProfileDTO customerProfileDTO)throws EOTException;

	/**
	 * Gets the mobile num length.
	 *
	 * @param countryId the country id
	 * @return the mobile num length
	 * @throws EOTException the eOT exception
	 */
	public Integer getMobileNumLength(Integer countryId) throws EOTException;

	/**
	 * Export to xls for customer details.
	 *
	 * @param customerName the customer name
	 * @param mobileNumber the mobile number
	 * @param bankId the bank id
	 * @param branchId the branch id
	 * @param countryId the country id
	 * @param bankGroupId the bank group id
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param model the model
	 * @return the list
	 * @throws EOTException the eOT exception
	 */
	List exportToXLSForCustomerDetails(String customerName,String mobileNumber, String bankId, String branchId,
			String countryId, String bankGroupId,String fromDate,String toDate, Map<String, Object> model) throws EOTException;

	/**
	 * Gets the customer list.
	 *
	 * @return the customer list
	 */
	List<Customer> getCustomerList();
	
	/**
	 * Gets the customer questions.
	 *
	 * @param locale the locale
	 * @return the customer questions
	 * @throws EOTException the eOT exception
	 */
	List<SecurityQuestion> getCustomerQuestions(String locale)throws EOTException;

	/**
	 * Gets the user.
	 *
	 * @param userName the user name
	 * @return the user
	 */
	WebUser getUser(String userName);

	/**
	 * Gets the bank teller.
	 *
	 * @param userName the user name
	 * @return the bank teller
	 */
	BankTellers getBankTeller(String userName);

	/**
	 * Gets the user for proof.
	 *
	 * @param name the name
	 * @param customerDTO the customer dto
	 * @return the user for proof
	 * @throws EOTException 
	 */
	CustomerDTO getUserForProof(String name,CustomerDTO customerDTO) throws EOTException;

	/**
	 * Gets the branch list for bank.
	 *
	 * @param name the name
	 * @return the branch list for bank
	 */
	public List<Branch> getBranchListForBank(String name);

	/**
	 * Adds the bank account details.
	 *
	 * @param bankAccountDTO the bank account dto
	 * @param name the name
	 * @throws EOTException the eOT exception
	 */
	void addBankAccountDetails(BankAccountDTO bankAccountDTO,String name)throws EOTException;

	/**
	 * Update branch details.
	 *
	 * @param bankAccountDTO the bank account dto
	 * @throws EOTException the eOT exception
	 */
	void updateBranchDetails(BankAccountDTO bankAccountDTO)throws EOTException;

	/**
	 * Gets the customer bank account details.
	 *
	 * @param customerId the customer id
	 * @param pageNumber the page number
	 * @param model the model
	 * @return the customer bank account details
	 */
	Page getCustomerBankAccountDetails(Long customerId, int pageNumber,Map<String,Object> model);

	/**
	 * Gets the customer bank account details.
	 *
	 * @param slNo the sl no
	 * @return the customer bank account details
	 */
	BankAccountDTO getCustomerBankAccountDetails(Long slNo);

	/**
	 * Gets the customer card details.
	 *
	 * @param customerId the customer id
	 * @param pageNumber the page number
	 * @param model the model
	 * @return the customer card details
	 */
	Page getCustomerCardDetails(Long customerId, int pageNumber,Map<String, Object> model);

	/**
	 * Adds the customer card.
	 *
	 * @param cardDto the card dto
	 * @param name the name
	 * @throws EOTException the eOT exception
	 */
	void addCustomerCard(CardDto cardDto, String name) throws EOTException;

	/**
	 * Update customer card.
	 *
	 * @param cardDto the card dto
	 * @throws EOTException the eOT exception
	 */
	void updateCustomerCard(CardDto cardDto) throws EOTException;

	/**
	 * Gets the customer card details.
	 *
	 * @param cardId the card id
	 * @return the customer card details
	 * @throws EOTException the eOT exception
	 */
	CardDto getCustomerCardDetails(Long cardId) throws EOTException;

	/**
	 * Process new card.
	 *
	 * @param cardDto the card dto
	 * @throws EOTException the eOT exception
	 */
	void processNewCard(CardDto cardDto) throws EOTException;

	/**
	 * Gets the sms package list.
	 *
	 * @return the sms package list
	 * @throws EOTException the eOT exception
	 */
	/*date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details*/
	List<SmsAlertRuleValue> getSmsPackageList()throws EOTException;

	/**
	 * Sms package details.
	 *
	 * @param packageId the package id
	 * @return the list
	 * @throws EOTException the eOT exception
	 */
	List<SmsSubscriptionDTO> smsPackageDetails(String packageId)throws EOTException;

	/**
	 * Subscribed sms package.
	 *
	 * @param customerId the customer id
	 * @param packageId the package id
	 * @param subscriptionType the subscription type
	 * @param noOfSms the no of sms
	 * @throws EOTException the eOT exception
	 */
	void subscribedSMSPackage(String customerId,String packageId, String subscriptionType, String noOfSms)throws EOTException;

	/**
	 * Gets the current subscription.
	 *
	 * @param customerId the customer id
	 * @return the current subscription
	 * @throws EOTException the eOT exception
	 */
	CustomerSmsRuleDetail getCurrentSubscription(String customerId)throws EOTException;
	/*date:05/08/16 by:rajlaxmi for:showing only subscribed sc packages details*/
	List<ServiceChargeSubscription> getSCPackageList() throws EOTException;

	List<SCSubscriptionDTO> scPackageDetails(String packageId) throws EOTException;

	void subscribeSCPackage(String customerId, Long serviceChargeRuleId, String subscriptionType, String numberOfTxn);

	CustomerScsubscription getSCCurrentSubscription(String customerId);

	List<CustomerScsubscription> getSCSubscribedList(String customerId);

	String getLanguageDescription(String languageCode);

	List<CustomerProfiles> loadCustomerProfiles(Integer bankId) throws EOTException;

	CustomerDTO getUserForProofByPartner(String name, CustomerDTO customerDTO) throws EOTException;

	Map<String, Object> getMasterDataByPartner(String language,String type) throws EOTException;

	Country getCountry(Integer countryId);

	Map<String, Object> getMasterDataByType(String language, String type) throws EOTException;

	City getCity(Integer cityId);

	Customer getCustomerByCustomerId(Long customerId);

	Quarter getQuarter(Long quarterId);

	CustomerProfiles getCustomerProfiles(Integer customerProfileId);

	public void populateDataForJmeter();
	
	void resetTxnPin(Long customerId)throws Exception;
	
	String apprOrRejectCust(String type, String rejectComment, CustomerDTO customerDTO)
			throws EOTException;
	
	String sendOTP (CustomerDTO customerDTO) throws EOTException;

	Page getCustomersWithQRCode(QRCodeDTO qrCodeDTO, Integer pageNumber) throws EOTException;

	void exportQRCodeInPDF(String customerId, HttpServletResponse response) throws EOTException;
	
	void bulkQRCodePDF(String customerId, HttpServletResponse response, ZipOutputStream zipStream ) throws EOTException;
	
	void processChangeCustomerStatus(Long customerId,Integer status)throws EOTException;

	Page searchBlockedCustomers(String fromDate, String toDate, Integer pageNumber, String custType);

	Page getAgentCashOutData(TransactionParamDTO transactionParamDTO, Integer pageNumber);

	void sendOTPForAgentCashOut(TransactionParamDTO transactionParamDTO) throws EOTException;

	TransactionParamDTO getAgentCashOutDetails(TransactionParamDTO transactionParamDTO) throws EOTException;
	
	/*Page searchCustomersWithTxnDate(String userName, String bankGroupId,String firstName,String middleName,String lastName,String mobileNumber,String bankId,String branchId,String countryId,String fromDate,String toDate,Integer pageNumber,String custType,String onBoardedBy,String businessName) throws EOTException;*/
	
}
