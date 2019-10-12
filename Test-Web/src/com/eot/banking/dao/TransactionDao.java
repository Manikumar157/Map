package com.eot.banking.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.eot.banking.dto.BankFloatDepositDTO;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.ExternalTransactionDTO;
import com.eot.banking.dto.NonRegUssdCustomerDTO;
import com.eot.banking.dto.TransactionVolumeDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.utils.Page;
import com.eot.dtos.basic.Transaction;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.FileUploadDetail;
import com.eot.entity.MobileRequest;
import com.eot.entity.Operator;
import com.eot.entity.PendingTransaction;
import com.eot.entity.SettlementJournal;
import com.eot.entity.TransactionRuleTxn;
import com.eot.entity.TransactionType;

public interface TransactionDao extends BaseDao{

	List<TransactionType> getTrnsactionList(String locale);
	MobileRequest getRequest(Long requestId);
	Customer getCustomer(String mobileNumber);
	List<CustomerAccount> getCustomerAccount(Long customerId);
	CustomerAccount getCustomerAccountFromAlias(Long customerId,String accountNumber);
	BankTellers getBankTeller(String tellerName);
	List<ClearingHousePoolMember> getClearingHouse(Integer customerBank,Integer otherPartyBank);
	
	Page getTransactionDetails(Integer pageNumber,Long referenceId,Integer referenceType );
	Page getTransactionDetailsByAccountNumber(Integer pageNumber,Long referenceId,Integer referenceType,String accountNumber);
	Page getRequests(Integer pageNumber,Long referenceId,Integer referenceType );
	
	List getChartList(TxnSummaryDTO txnSummaryDTO);
	Integer getMobileNumLength(Integer isdCode);
	List<Object> getAccountLedgerReport(String accountNumber,Date fromDate,Date toDate);
	List<Object> getTrialBalance(List<Map> accountNumbers,Date fromDate,Date toDate);
	List<Map> getAccountHeadList(Integer bankId);
	
	List<Bank> getBankByChPoolId(String chPoolId);
	Page getPendingTransactions(String customerName,String mobileNumber,String amount,String status,String txnDate,String txnType,Integer bankId,String fromDate,String toDate,int pageNumber,Long branchId);
	Customer verifyCustomer(Long referenceId);
	PendingTransaction getCustomerPendingTransaction(Long referenceId,Long txnId);
	PendingTransaction getRejectCustomer(Long txnId);
	Page getPendingTransactionsByStatus(Integer bankId,int pageNumber);
	
	TransactionRuleTxn getTransactionRule(Long transactionAmount,Integer transactionType,Integer profileId,Integer groupId);
	CustomerAccount getCustomerAliasFromAccount(Long referenceId,String accountNumber);
	CustomerAccount getCustomerAccountBalance(String customerId);
    List<Transaction> getLastTransactions(String accountNumber,int noOfTransactions);
    List<Transaction> getLastTransactions(String accountNumber,Date fromDate, Date toDate);
    Page searchTxnSummary(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO,int pageNumber,Long branchId);
	List exportToXLSForTransactionSummary(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO,Long branchId);
	List exportToXLSForPendingTransactionSummary(String customerName,String mobileNumber, String txnDate, String amount, String txnType,String status,String fromDate,String toDate,Integer bankId,Long branchId);
	List exportToXLSForTransactionSummaryForBankTellerEOD(Integer bankGroupId,Integer bankId, TxnSummaryDTO txnSummaryDTO,Long branchId);
	List exportToXLSForTransactionSummaryForTxnSummary(Integer bankGroupId,Integer bankId, TxnSummaryDTO txnSummaryDTO,Long branchId);
	List exportToXLSForTransactionSummaryPerBank(Integer bankGroupId,Integer bankId, TxnSummaryDTO txnSummaryDTO);
	List exportToXLSForTransactionSummaryForBankTeller(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO,String userName);
	Page getCancellations(String customerName, String mobileNumber,String amount, String txnDate, String txnType, Integer bankId,String fromDate, String toDate, int pageNumber,Long branchId);
	com.eot.entity.Transaction getTransactionByTxnId(Integer transactionId);
	Page getAdjustmentTransactions(String customerName, String mobileNumber,String amount, String txnDate, String txnType,String fromDate, String toDate, int pageNumber, Integer bankId);
	Page getExternalTransactions(Integer pageNumber);
	List<Operator> getOperatorList();
	Page searchExternalTxns(ExternalTransactionDTO externalTransactionDTO,int pageNumber);
	List getExternalTransactionDetailsForReport(ExternalTransactionDTO externalTransactionDTO);
	List getExternalTransactionSummaryDetailsForReport(ExternalTransactionDTO externalTransactionDTO);
	int updateCommissionProcessedTxnByAgnetId(Long agentID, String date,int status);
	TransactionType getTransactionType(int type);
	Page searchBusinessPartnerTxnSummary(Integer bankId, BusinessPartnerDTO businessPartnerDTO, int pageNumber,String appendString);
	
	public List<SettlementJournal> getSettlementJournals(Long transactionId);
	void updateSettlementJournals(long parseLong);
	void updateTransactionJournals(long parseLong);
	void updateTransaction(long parseLong);
	Account getBusinessPartnerAccount(String accountNumber);
	List<CustomerAccount> getAgentAccount(Long customerId, int aliasType);
	FileUploadDetail getLastProcessedFile();
	public Map<Integer, TransactionType> getTxntypeMap(String locale);
	int updateCommissionProcessedTxnByAgnetId(Long agentID, String date, int status, long transactionId);
	Account getAccountByAccountNumber(String accountNumber);
	Page searchTxnMerchantData(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO,int pageNumber,Long branchId);
	Page searchTxnSummaryCustomerRegistration(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO,int pageNumber,Long branchId);
	int updateCommissionProcessedTxnByTxnId(Long agentID, String date, int status, long transactionId);
	Page searchBulkPayTxnReport(TxnSummaryDTO txnSummaryDTO, Integer pageNumber);
	Page BankFloatDepositReportData(BankFloatDepositDTO bankFloatDepositDTO, Integer pageNumber);
	Page NonRegUssdCustomerReportData(NonRegUssdCustomerDTO nonRegUssdCustomerDTO, Integer pageNumber);
	Page transactionVolumeReportData( TransactionVolumeDTO transactionVolumeDTO, Integer pageNumber);
	AppMaster getApplicationType(String applicationId);
}
