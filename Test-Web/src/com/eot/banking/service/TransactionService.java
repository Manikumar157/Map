package com.eot.banking.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;

import com.eot.banking.dto.AccountDetailsDTO;
import com.eot.banking.dto.BankFloatDepositDTO;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.ExternalTransactionDTO;
import com.eot.banking.dto.NonRegUssdCustomerDTO;
import com.eot.banking.dto.RequestReinitDTO;
import com.eot.banking.dto.ReversalDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.dto.TransactionReceiptDTO;
import com.eot.banking.dto.TransactionVolumeDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.coreclient.EOTCoreException;
import com.eot.entity.Bank;
import com.eot.entity.CommissionReport;
import com.eot.entity.Country;
import com.eot.entity.Operator;
import com.eot.entity.PendingTransaction;
import com.eot.entity.TransactionType;

public interface TransactionService {
	
	AccountDetailsDTO getAccountDetails(String mobileNumber,Integer txnType,boolean flag) throws EOTException ;
	
	TransactionReceiptDTO processTransaction(TransactionParamDTO accountDetailsDTO,String userName,Locale resolveLocale,MessageSource messageSource) throws EOTException, NoSuchAlgorithmException, UnsupportedEncodingException  ;
	
	List<TransactionType> getTransactionType(String locale) throws EOTException ;
	
	Page getTransactions(Integer pageNumber,Long customerId,String userName) throws EOTException ;
	
	Page getRequests(Integer pageNumber,Long customerId) throws EOTException;
	
	RequestReinitDTO reinitiateRequest(Long requestId, String userName) throws EOTCoreException;
	
	 List getChartList(TxnSummaryDTO txnSummaryDTO,String userName)throws EOTException;
	 
	 byte[] getChartImageBytes(TxnSummaryDTO txnSummaryDTO)throws EOTException;
	 
	 List<Country> getCountryList(String language);
	 
	 Integer getCountryMobileNumLength(Integer isdCode)throws EOTException;
	 
	 List<Object> getAccountLedgerReport(String accountNumber,Date fromDate,Date toDate)throws EOTException;
	 
	 List<Object> getTrialBalance(TxnSummaryDTO txnSummaryDTO)throws EOTException;
	 
	 Map getAccountHeadList()throws EOTException;
	 
	 List<Bank> getBankByChPoolId(String chPoolId)throws EOTException;
	 
	 List<Map> getAccountHeadByBankId(String chPoolId)throws EOTException;
	 
	 byte[] getPieChartImageBytes(TxnSummaryDTO txnSummaryDTO)throws EOTException;

	 String getBankName();

	 Page getPendingTrnasactions(String customerName,String mobileNumber,String amount,String status,String txnDate,String txnType,String userName,String fromDate,String toDate,int pageNumber)throws EOTException;

	TransactionReceiptDTO approveTransaction(String referenceId,String userName,Long txnId,Locale locale) throws EOTException;

	void rejectTransaction(String referenceId, String userName,String comment,Long txnId) throws EOTException;

	void cancelTransaction(String referenceId, String userName,Long txnId) throws EOTException;

	PendingTransaction  getRejectedCustomer(Long txnId)throws EOTException;

	TransactionReceiptDTO processSupportTransaction(TransactionParamDTO transactionParamDTO, String name , Locale locale) throws EOTException;
	
	Page searchTxnSummary(TxnSummaryDTO txnSummaryDTO, int pageNumber,String language) throws EOTException;

	List exportToXLSForTransactionSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException;

	Map<String, Object> getMasterData(String language) throws EOTException ;

	List exportToXLSForPendingTransactionSummary(String customerName,String mobileNumber, String txnDate, String amount, String txnType,
			String status,String fromDate,String toDate,Map<String, Object> model) throws EOTException;

	List exportToXLSForTransactionSummaryForBankTellerEOD(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException;

	List exportToXLSForTransactionSummaryForTxnSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException;

	Page getCancellations(String customerName, String mobileNumber,String amount, String txnDate, String txnType, String name,String fromDate, String toDate, int pageNumber) throws EOTException;

	List exportToXLSForTransactionSummaryPerBank(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model) throws EOTException;
	
	void voidTransaction(Integer transactionId) throws EOTException;

	Page getAdjustmentTransactions(String customerName, String mobileNumber,String amount, String txnDate, String txnType,String fromDate, String toDate, int pageNumber) throws EOTException;

	void adjustmentTransaction(String transactionId, String adjustedAmount,String adjustedFee, String transactionType, String comment, String mobileNumber)throws EOTException;

	void uploadReversalDetails(ReversalDTO reversalDTO)throws EOTException;

	List<Operator> getOperatorList();

	Page searchExternalTxns(ExternalTransactionDTO externalTransactionDTO,int pageNumber) throws EOTException;

	List exportToXLSForExternalTransactionDetails(ExternalTransactionDTO externalTransactionDTO)throws EOTException;

	List exportToXlsExternalTransactionSummaryDetails(ExternalTransactionDTO externalTransactionDTO) throws EOTException;

	List<TransactionParamDTO> uploadTransactionDetails(TransactionParamDTO transactionParamDTO,  Locale locale, WebUserDTO webUserDTO, String timeStamp) throws EOTException;
	
	TransactionType getTransactionType (int type) throws EOTException;

	Page searchTxnSummaryForBusinessPartner(BusinessPartnerDTO businessPartnerDTO, int pageNumber, String language)
			throws EOTException;

	void initiateAdjustmentTransaction(String transactionId, String adjustedAmount, String adjustedFee,
			String transactionType, String comment) throws EOTException;

	AccountDetailsDTO getBusinessPartnerAccountDetails(String businessPartnerCode, Integer transctionType) throws EOTException;

	void downloadBulkPaymentFailFile() throws EOTException;

	public Map<Integer,TransactionType> getTxntypeMap (String locale);

	void saveCommissionData(CommissionReport commissionReport);

	TransactionReceiptDTO accountTransfer(TransactionParamDTO fundTransferDTO, String userName, Locale locale,
			MessageSource messageSource) throws EOTException;

	Page searchTxnMerchantData(TxnSummaryDTO txnSummaryDTO, Integer pageNumber, String string) throws EOTException;

	List<AccountDetailsDTO> getAccountsForAccountToAccount() throws EOTException;
	
	Page searchTxnCustomerRegistration(TxnSummaryDTO txnSummaryDTO, int pageNumber,String language) throws EOTException;

	AccountDetailsDTO getAccountsForAccToAcc(String fromAccountNo, String toAccountNo, Integer txnType, boolean flag)
			throws EOTException;

	Page searchBulkPayTxnReport(TxnSummaryDTO txnSummaryDTO, Integer pageNumber, String string) throws EOTException;

	Page BankFloatDepositReportData(BankFloatDepositDTO bankFloatDepositDTO, Integer pageNumber, String string);

	Page NonRegUssdCustomerReportData(NonRegUssdCustomerDTO nonRegUssdCustomerDTO, Integer pageNumber);
	
	Page transactionVolumeReportData( TransactionVolumeDTO transactionVolumeDTO, Integer pageNumber);
}
