package com.eot.banking.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.CoreUrls;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.FileProcessDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.SCManagementDao;
import com.eot.banking.dao.TransactionDao;
import com.eot.banking.dao.WebUserDao;
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
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.FileUtil;
import com.eot.banking.utils.HashUtil;
import com.eot.banking.utils.Page;
import com.eot.coreclient.EOTCoreException;
import com.eot.coreclient.webservice.BankingServiceClientStub;
import com.eot.coreclient.webservice.BasicBankingServiceClientStub;
import com.eot.dtos.banking.AdjustmentTransactionDTO;
import com.eot.dtos.banking.DepositDTO;
import com.eot.dtos.banking.LimitDTO;
import com.eot.dtos.banking.TransferDirectDTO;
import com.eot.dtos.banking.VoidTransactionDTO;
import com.eot.dtos.banking.WithdrawalDTO;
import com.eot.dtos.basic.BalanceEnquiryDTO;
import com.eot.dtos.basic.MiniStatementDTO;
import com.eot.dtos.basic.Transaction;
import com.eot.dtos.basic.TxnStatementDTO;
import com.eot.dtos.common.Fault;
import com.eot.dtos.sms.DebitAlertDTO;
import com.eot.dtos.sms.ResetTxnPinAlertDTO;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.CommissionReport;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerCard;
import com.eot.entity.FileUploadDetail;
import com.eot.entity.MobileRequest;
import com.eot.entity.Operator;
import com.eot.entity.Otp;
import com.eot.entity.PendingTransaction;
import com.eot.entity.SettlementJournal;
import com.eot.entity.SmsLog;
import com.eot.entity.TransactionJournal;
import com.eot.entity.TransactionRuleTxn;
import com.eot.entity.TransactionType;
import com.eot.entity.WebRequest;
import com.eot.entity.WebUser;
import com.eot.smsclient.webservice.SmsServiceClientStub;
import com.keypoint.PngEncoder;
import com.thinkways.util.TLVUtil;

@Service("transactionService")
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionDao transactionDao;
	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private BankingServiceClientStub bankingServiceClientStub;
	@Autowired
	private BasicBankingServiceClientStub basicBankingServiceClientStub;
	@Autowired
	private AppConfigurations appConfigurations;
	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private SCManagementDao scManagementDao;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	public MessageSource messageSource;
	@Autowired
	private BusinessPartnerService businessPartnerService;
	@Autowired
	private FileProcessDao fileProcessDao;
	/** The customer service. */
	@Autowired
	private CustomerService customerService;
	
	/* @Autowired
	 * private SmsServiceClientStub smsServiceClientStub; */

	/** The business partner dao. */
	@Autowired
	private BusinessPartnerDao businessPartnerDao;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public List<TransactionType> getTransactionType(String locale) throws EOTException {

		List<TransactionType> transactionTypes = transactionDao.getTrnsactionList(locale);
		return transactionTypes;
	}

	@SuppressWarnings("unused")
	@Override
	public AccountDetailsDTO getAccountDetails(String mobileNumber,Integer txnType, boolean flag) throws EOTException {

		Customer customer = transactionDao.getCustomer(mobileNumber);
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_AGENT);
		}
		AppMaster appMaster = transactionDao.getApplicationType(customer.getAppId());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		
		if (flag == true) {
			if (customer == null) {
				throw new EOTException(ErrorConstants.INVALID_AGENT);
			}
			if (!customer.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT)) {
				throw new EOTException(ErrorConstants.INVALID_AGENT);
			}
			if (customer.getActive() == EOTConstants.CUSTOMER_STATUS_DEACTIVATED) {
				throw new EOTException(ErrorConstants.INACTIVE_AGENT);
			}
			if(appMaster.getStatus() == EOTConstants.APP_STATUS_BLOCKED){
				throw new EOTException(ErrorConstants.AGENT_ACC_BLOCKED);
			}
		} else {
			if (customer == null) {
				throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
			}
			if (customer.getActive() == EOTConstants.CUSTOMER_STATUS_DEACTIVATED) {
				throw new EOTException(ErrorConstants.INACTIVE_CUSTOMER);
			}
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
				
				if (!(customer.getType() == EOTConstants.REFERENCE_TYPE_AGENT) ) 
					throw new EOTException(ErrorConstants.INVALID_MOBILE_NUMBER);
				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {			
					BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(auth.getName());
					if(!customer.getBusinessPartner().getId().equals(businessPartnerUser.getBusinessPartner().getId()))
							throw new EOTException(ErrorConstants.INVALID_MOBILE_NUMBER);							
				}
			}
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				if (!customer.getType().equals(Integer.parseInt(EOTConstants.REFERENCE_TYPE_CUSTOMER +"")) && !customer.getType().equals(Integer.parseInt(EOTConstants.REFERENCE_TYPE_MERCHANT +"")) ) {
					throw new EOTException(ErrorConstants.INVALID_MOBILE_NUMBER);
				}
			}
		}
		List<CustomerAccount> accountList = transactionDao.getCustomerAccount(customer.getCustomerId());
		
		if (customer.getType() == EOTConstants.REFERENCE_TYPE_AGENT ) {
			/*
			 * if(txnType==null) { accountList =
			 * transactionDao.getAgentAccount(customer.getCustomerId(),
			 * EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT); } else
			 */ if (null !=txnType && txnType == EOTConstants.TXN_ID_CASH_IN)
				accountList = transactionDao.getAgentAccount(customer.getCustomerId(), EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT);
			else if (null !=txnType && txnType == EOTConstants.TXN_ID_CASH_OUT)
				accountList = transactionDao.getAgentAccount(customer.getCustomerId(), EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);
		}
		
		AccountDetailsDTO dto = new AccountDetailsDTO();

		dto.setMobileNumber(mobileNumber);
		if (customer.getMiddleName() == null) {
			customer.setMiddleName("");
		}
		if (customer.getLastName() == null) {
			customer.setLastName("");
		}
		dto.setCustomerName(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
		dto.setCustomerId(customer.getCustomerId() + "");
		dto.setAccountList(accountList);
		if (customer.getCustomerDocument() != null) {
			dto.setExpiryDate(customer.getCustomerDocument().getExpiryDate());
			dto.setIdType(customer.getCustomerDocument().getIdType());
			dto.setIdNumber(customer.getCustomerDocument().getIdNumber());
			dto.setPlaceOfIssue(customer.getCustomerDocument().getPlaceOfIssue());
			dto.setIssueDate(customer.getCustomerDocument().getIssueDate());
		}
		return dto;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public TransactionReceiptDTO processTransaction(TransactionParamDTO transactionParamDTO, String userName, Locale resolveLocale, MessageSource messageSource) throws EOTException, NoSuchAlgorithmException, UnsupportedEncodingException {

		if ((null == transactionParamDTO.getBusinessPartnerCode() || "".equals(transactionParamDTO.getBusinessPartnerCode())) && transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_CASH_IN && transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_CASH_OUT && transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_TRANSFER_EMONEY)
			switch (transactionParamDTO.getTransactionType()) {

			case EOTConstants.TXN_ID_DEPOSIT:
				return processDeposit(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_WITHDRAWAL:
				return processWithdrawal(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_MINISTATEMENT:
				return processMiniStatement(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_TXNSTATEMENT:
				return processTransactionStatement(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_BALANCE_ENQUIRY:
				return processBalanceEnq(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_ACCOUNT_TRANSFER:
				return accountTransfer(transactionParamDTO, userName, resolveLocale, messageSource);
			
			default:
				throw new EOTException(ErrorConstants.INVALID_TXN_TYPE);
			}
		else
			switch (transactionParamDTO.getTransactionType()) {

			case EOTConstants.TXN_ID_MINISTATEMENT:
				return processBusinessPartnerMiniStatement(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_TXNSTATEMENT:
				return processBusinessPartnerTransactionStatement(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_BALANCE_ENQUIRY:
				return processBalanceEnqForBusinessPartner(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_CASH_IN:
				return processCashIn(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_CASH_OUT:
				return processCashOut(transactionParamDTO, userName, resolveLocale, messageSource);
			case EOTConstants.TXN_ID_TRANSFER_EMONEY:
				return processTransferEmoney(transactionParamDTO, userName, resolveLocale, messageSource);
			default:
				throw new EOTException(ErrorConstants.INVALID_TXN_TYPE);
			}
	}

	private TransactionReceiptDTO processDeposit(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), transactionParamDTO.getAccountAlias());
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}

		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		Account bankAccount = bankTellers.getBank().getAccount();

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_DEPOSIT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(bankTellers.getWebUser());

		transactionDao.save(request);

		Integer groupId = account.getBank().getBankGroup() != null ? account.getBank().getBankGroup().getBankGroupId() : null;

		TransactionRuleTxn transactionRuleTxn = transactionDao.getTransactionRule(transactionParamDTO.getAmount(), transactionParamDTO.getTransactionType(), customer.getCustomerProfiles().getProfileId(), groupId);

		if (transactionRuleTxn != null) {

			if (account.getBank().getBankId() != bankTellers.getBank().getBankId()) {

				throw new EOTException(ErrorConstants.NOT_AUTHORIZE_PENDING_TXN);
			}

			PendingTransaction pendingTransaction = new PendingTransaction();

			TransactionType transactionType = new TransactionType();
			transactionType.setTransactionType(transactionParamDTO.getTransactionType());

			pendingTransaction.setAmount((double) transactionParamDTO.getAmount());
			pendingTransaction.setBank(account.getBank());
			pendingTransaction.setCustomerAccount(account.getAccountNumber());
			pendingTransaction.setCustomerAccountType(account.getAccount().getAccountType());
			pendingTransaction.setOtherAccount(bankAccount.getAccountNumber());
			pendingTransaction.setOtherAccountType(bankAccount.getAccountType());
			pendingTransaction.setReferenceType(customer.getType());
			pendingTransaction.setStatus(0);
			pendingTransaction.setTransactionDate(new Date());
			pendingTransaction.setInitiatedBy(bankTellers.getWebUser().getFirstName());
			pendingTransaction.setTransactionType(transactionType);
			pendingTransaction.setCustomer(customer);

			transactionDao.save(pendingTransaction);

			throw new EOTException(ErrorConstants.APPROVAL_PENDING);

		} else {

			com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
			accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
			accountDto.setAccountNO(account.getAccountNumber());
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
			accountDto.setBankCode(account.getBank().getBankId().toString());
			accountDto.setBranchCode(account.getBranch().getBranchId().toString());

			com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

			bankAccountDto.setAccountAlias(bankAccount.getAlias());
			bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
			bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

			DepositDTO depositDTO = new DepositDTO();

			depositDTO.setReferenceID(customer.getCustomerId().toString());
			depositDTO.setAmount(transactionParamDTO.getAmount().doubleValue());
			depositDTO.setChannelType(EOTConstants.EOT_CHANNEL);
			depositDTO.setCustomerAccount(accountDto);
			depositDTO.setOtherAccount(bankAccountDto);
			depositDTO.setTransactionType(EOTConstants.TXN_ID_DEPOSIT + "");

			try {
				// spring web service call to core : commented by bidyut
				// depositDTO = bankingServiceClientStub.deposit(depositDTO);

				// rest call updated by bidyut
				depositDTO = processRequest(CoreUrls.DEPOSITE_TXN_URL, depositDTO, com.eot.dtos.banking.DepositDTO.class);
				if (depositDTO.getErrorCode() != 0) {
					throw new EOTException("ERROR_" + depositDTO.getErrorCode());
				}

				request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
				com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
				txn.setTransactionId(new Long(depositDTO.getTransactionNO()));
				request.setTransaction(txn);
			} /*
				 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
				 * Integer(e.getMessageKey())); throw new
				 * EOTException("ERROR_"+e.getMessageKey()); }
				 */catch (Fault ex) {
				ex.printStackTrace();
				throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
			} finally {
				transactionDao.update(request);
			}

			Double serviceCharge = depositDTO.getServiceChargeAmt();
			Double stampFee = scManagementDao.getStampFeeFromServiceChargeSplit(bankTellers.getBank().getBankId()).get(0).getServiceChargePct().doubleValue();

			TransactionReceiptDTO receipt = new TransactionReceiptDTO();
			receipt.setAccountAlias(account.getAccount().getAlias());
			receipt.setAuthCode(depositDTO.getTransactionNO());
			receipt.setBalance(new DecimalFormat("#0.00").format(depositDTO.getBalance()));
			receipt.setDescription(messageSource.getMessage("Deposit", null, locale));
			Double amount = depositDTO.getAmount() - serviceCharge;
			receipt.setAmount(amount.longValue());

			return receipt;

		}
	}

	private TransactionReceiptDTO processWithdrawal(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), transactionParamDTO.getAccountAlias());
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		Account bankAccount = bankTellers.getBank().getAccount();

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_WITHDRAWAL);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(bankTellers.getWebUser());

		transactionDao.save(request);

		Integer groupId = account.getBank().getBankGroup() != null ? account.getBank().getBankGroup().getBankGroupId() : null;

		TransactionRuleTxn transactionRuleTxn = transactionDao.getTransactionRule(transactionParamDTO.getAmount(), transactionParamDTO.getTransactionType(), customer.getCustomerProfiles().getProfileId(), groupId);

		if (transactionRuleTxn != null) {

			if (account.getBank().getBankId() != bankTellers.getBank().getBankId()) {

				throw new EOTException(ErrorConstants.NOT_AUTHORIZE_PENDING_TXN);
			}

			PendingTransaction pendingTransaction = new PendingTransaction();

			TransactionType transactionType = new TransactionType();
			transactionType.setTransactionType(transactionParamDTO.getTransactionType());

			pendingTransaction.setAmount((double) transactionParamDTO.getAmount());
			pendingTransaction.setBank(account.getBank());
			pendingTransaction.setCustomerAccount(account.getAccountNumber());
			pendingTransaction.setCustomerAccountType(account.getAccount().getAccountType());
			pendingTransaction.setOtherAccount(bankAccount.getAccountNumber());
			pendingTransaction.setOtherAccountType(bankAccount.getAccountType());
			pendingTransaction.setReferenceType(customer.getType());
			pendingTransaction.setStatus(0);
			pendingTransaction.setInitiatedBy(bankTellers.getWebUser().getFirstName());
			pendingTransaction.setTransactionDate(new Date());
			pendingTransaction.setTransactionType(transactionType);
			pendingTransaction.setCustomer(customer);

			transactionDao.save(pendingTransaction);

			throw new EOTException(ErrorConstants.APPROVAL_PENDING);

		} else {

			com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
			accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
			accountDto.setAccountNO(account.getAccountNumber());
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
			accountDto.setBankCode(account.getBank().getBankId().toString());
			accountDto.setBranchCode(account.getBranch().getBranchId().toString());

			com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

			bankAccountDto.setAccountAlias(bankAccount.getAlias());
			bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
			bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

			WithdrawalDTO withdrawalDTO = new WithdrawalDTO();

			withdrawalDTO.setReferenceID(customer.getCustomerId().toString());
			withdrawalDTO.setAmount(transactionParamDTO.getAmount().doubleValue());
			withdrawalDTO.setChannelType(EOTConstants.EOT_CHANNEL);
			withdrawalDTO.setCustomerAccount(accountDto);
			withdrawalDTO.setOtherAccount(bankAccountDto);
			withdrawalDTO.setTransactionType(EOTConstants.TXN_ID_WITHDRAWAL + "");

			try {
				// spring web service call commented by bidyut
				// withdrawalDTO = bankingServiceClientStub.withdrawal(withdrawalDTO);

				// rest call updated by bidyut
				withdrawalDTO = processRequest(CoreUrls.WITHDRAWAL_TXN_URL, withdrawalDTO, com.eot.dtos.banking.WithdrawalDTO.class);
				if (withdrawalDTO.getErrorCode() != 0) {
					throw new EOTException("ERROR_" + withdrawalDTO.getErrorCode());
				}

				com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
				txn.setTransactionId(new Long(withdrawalDTO.getTransactionNO()));
				request.setTransaction(txn);

			} /*
				 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
				 * Integer(e.getMessageKey())); throw new
				 * EOTException("ERROR_"+e.getMessageKey()); }
				 */ catch (Fault ex) {
				ex.printStackTrace();
				throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
			} finally {
				transactionDao.update(request);
			}

			TransactionReceiptDTO receipt = new TransactionReceiptDTO();
			receipt.setAccountAlias(account.getAccount().getAlias());
			receipt.setAuthCode(withdrawalDTO.getTransactionNO());
			receipt.setBalance(new DecimalFormat("#0.00").format(withdrawalDTO.getBalance()));
			receipt.setDescription(messageSource.getMessage("Withdrawl", null, locale));
			receipt.setAmount(withdrawalDTO.getAmount().longValue());

			return receipt;

		}

	}

	private TransactionReceiptDTO processBalanceEnq(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), transactionParamDTO.getAccountAlias());
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		
		List<ClearingHousePoolMember> chPool = new ArrayList<ClearingHousePoolMember>();
		Account bankAccount = new Account();
		BankTellers bankTellers = new BankTellers();
		BusinessPartnerUser businessPartnerUser = new BusinessPartnerUser();
		
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLE_ID_SUPPORT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {
			
			bankTellers = transactionDao.getBankTeller(userName);

			bankAccount = bankTellers.getBank().getAccount();

			if (bankAccount == null) {
				throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
			}

			chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
			if (chPool == null) {
				throw new EOTException(ErrorConstants.INVALID_CH_POOL);
			}	
			
		}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2
				 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS ){
			
			businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			
			/*
			 * if (webUser.getWebUserRole().getRoleId() ==
			 * EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ||
			 * webUser.getWebUserRole().getRoleId() ==
			 * EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
			 * if(!customer.getBusinessPartner().getId().equals(businessPartnerUser.
			 * getBusinessPartner().getId())) throw new
			 * EOTException(ErrorConstants.INVALID_MOBILE_NUMBER); }
			 */
			bankAccount = transactionDao.getBusinessPartnerAccount(account.getAccountNumber());
			
			
			if (bankAccount == null) {
				throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
			}

			chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), businessPartnerUser.getBusinessPartner().getBank().getBankId());
			if (chPool == null) {
				throw new EOTException(ErrorConstants.INVALID_CH_POOL);
			}
		}

		
		// end

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_BALANCE_ENQUIRY);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUser);

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(account.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(account.getBank().getBankId().toString());
		accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLE_ID_SUPPORT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {
			
		bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
		bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2
				 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS){
			bankAccountDto.setBankCode(businessPartnerUser.getBusinessPartner().getBank().getBankId().toString());
		//	bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		}
		
		BalanceEnquiryDTO balanceEnquiryDTO = new BalanceEnquiryDTO();

		balanceEnquiryDTO.setReferenceID(customer.getCustomerId().toString());
		balanceEnquiryDTO.setAmount(0D);
		balanceEnquiryDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		balanceEnquiryDTO.setCustomerAccount(accountDto);
		balanceEnquiryDTO.setTransactionType(EOTConstants.TXN_ID_BALANCE_ENQUIRY + "");
		balanceEnquiryDTO.setOtherAccount(bankAccountDto);

		try {
			// balanceEnquiryDTO =
			// basicBankingServiceClientStub.balanceEnquiry(balanceEnquiryDTO);

			// rest call updated by bidyut
			balanceEnquiryDTO = processRequest(CoreUrls.BALANCE_ENQ_WALLET, balanceEnquiryDTO, com.eot.dtos.basic.BalanceEnquiryDTO.class);
			if (balanceEnquiryDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + balanceEnquiryDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(balanceEnquiryDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */ finally {
			transactionDao.update(request);
		}

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setAuthCode(balanceEnquiryDTO.getTransactionNO());
		receipt.setBalance(new DecimalFormat("#0.00").format(balanceEnquiryDTO.getBalance()));
		receipt.setDescription(messageSource.getMessage("Balance_Enquiry", null, locale));

		return receipt;

	}

	private TransactionReceiptDTO processMiniStatement(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), transactionParamDTO.getAccountAlias());
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		// change		

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				WebUser webUser = webUserDao.getUser(auth.getName());
				
				List<ClearingHousePoolMember> chPool = new ArrayList<ClearingHousePoolMember>();
				Account bankAccount = new Account();
				BankTellers bankTellers = new BankTellers();
				BusinessPartnerUser businessPartnerUser = new BusinessPartnerUser();
				
				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
						|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLE_ID_SUPPORT|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING
						|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
						|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {
					
					bankTellers = transactionDao.getBankTeller(userName);

					bankAccount = bankTellers.getBank().getAccount();

					if (bankAccount == null) {
						throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
					}

					chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
					if (chPool == null) {
						throw new EOTException(ErrorConstants.INVALID_CH_POOL);
					}	
					
				}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2
						 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS ){
					
					businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			/*
			 * if (webUser.getWebUserRole().getRoleId() ==
			 * EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ||
			 * webUser.getWebUserRole().getRoleId() ==
			 * EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
			 * if(!customer.getBusinessPartner().getId().equals(businessPartnerUser.
			 * getBusinessPartner().getId())) throw new
			 * EOTException(ErrorConstants.INVALID_MOBILE_NUMBER); }
			 */
					
					bankAccount = transactionDao.getBusinessPartnerAccount(account.getAccountNumber());
					
					
					if (bankAccount == null) {
						throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
					}

					chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), businessPartnerUser.getBusinessPartner().getBank().getBankId());
					if (chPool == null) {
						throw new EOTException(ErrorConstants.INVALID_CH_POOL);
					}
				}
				
				// end
		/*
		 * BankTellers bankTellers = transactionDao.getBankTeller(userName);
		 * 
		 * Account bankAccount = bankTellers.getBank().getAccount();
		 * 
		 * if (bankAccount == null) { throw new
		 * EOTException(ErrorConstants.INVALID_BANK_ACCOUNT); }
		 * 
		 * List<ClearingHousePoolMember> chPool =
		 * transactionDao.getClearingHouse(account.getBank().getBankId(),
		 * bankTellers.getBank().getBankId()); if (chPool == null) { throw new
		 * EOTException(ErrorConstants.INVALID_CH_POOL); }
		 */

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_MINISTATEMENT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUser);

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(account.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(account.getBank().getBankId().toString());
		accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");		
	//	bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
	//	bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLE_ID_SUPPORT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {
			
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2
					 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS ){
				bankAccountDto.setBankCode(businessPartnerUser.getBusinessPartner().getBank().getBankId().toString());
			//	bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		}
		
		
		MiniStatementDTO miniStatementDTO = new MiniStatementDTO();

		miniStatementDTO.setCustomerAccount(accountDto);
		miniStatementDTO.setReferenceID(customer.getCustomerId().toString());
		miniStatementDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		miniStatementDTO.setAmount(0D);
		miniStatementDTO.setTransactionType(EOTConstants.TXN_ID_MINISTATEMENT + "");
		miniStatementDTO.setOtherAccount(bankAccountDto);

		try {
			// web service call to core : commented by bidyut
			// miniStatementDTO =
			// basicBankingServiceClientStub.miniStatement(miniStatementDTO);

			// rest call updated by bidyut
			miniStatementDTO = processRequest(CoreUrls.MINI_STATEMENT, miniStatementDTO, com.eot.dtos.basic.MiniStatementDTO.class);
			if (miniStatementDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + miniStatementDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(miniStatementDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */finally {
			transactionDao.update(request);
		}

		if (miniStatementDTO.getTransactionList() == null) {
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
		}

		List<Transaction> txnList = Arrays.asList(miniStatementDTO.getTransactionList());

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();

		receipt.setNumOfTxns(miniStatementDTO.getTransactionList().length);
		receipt.setAuthCode(miniStatementDTO.getTransactionNO());
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setTxnList(txnList);
		receipt.setDescription(messageSource.getMessage("Mini_Statement", null, locale));

		return receipt;

	}

	private TransactionReceiptDTO processTransactionStatement(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), transactionParamDTO.getAccountAlias());
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		// change		

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		
		List<ClearingHousePoolMember> chPool = new ArrayList<ClearingHousePoolMember>();
		Account bankAccount = new Account();
		BankTellers bankTellers = new BankTellers();
		BusinessPartnerUser businessPartnerUser = new BusinessPartnerUser();
		
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLE_ID_SUPPORT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK  || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {
			
			bankTellers = transactionDao.getBankTeller(userName);

			bankAccount = bankTellers.getBank().getAccount();

			if (bankAccount == null) {
				throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
			}

			chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
			if (chPool == null) {
				throw new EOTException(ErrorConstants.INVALID_CH_POOL);
			}	
			
		}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2
				 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS){
			
			businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			/*
			 * if (webUser.getWebUserRole().getRoleId() ==
			 * EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ||
			 * webUser.getWebUserRole().getRoleId() ==
			 * EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
			 * if(!customer.getBusinessPartner().getId().equals(businessPartnerUser.
			 * getBusinessPartner().getId())) throw new
			 * EOTException(ErrorConstants.INVALID_MOBILE_NUMBER); }
			 */
			
			bankAccount = transactionDao.getBusinessPartnerAccount(account.getAccountNumber());
			
			
			if (bankAccount == null) {
				throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
			}

			chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), businessPartnerUser.getBusinessPartner().getBank().getBankId());
			if (chPool == null) {
				throw new EOTException(ErrorConstants.INVALID_CH_POOL);
			}
		}
		
		// end
		/*
		 * BankTellers bankTellers = transactionDao.getBankTeller(userName);
		 * 
		 * Account bankAccount = bankTellers.getBank().getAccount();
		 * 
		 * if (bankAccount == null) { throw new
		 * EOTException(ErrorConstants.INVALID_BANK_ACCOUNT); }
		 * 
		 * List<ClearingHousePoolMember> chPool =
		 * transactionDao.getClearingHouse(account.getBank().getBankId(),
		 * bankTellers.getBank().getBankId()); if (chPool == null) { throw new
		 * EOTException(ErrorConstants.INVALID_CH_POOL); }
		 */

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_TXNSTATEMENT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUser);

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(account.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(account.getBank().getBankId().toString());
		accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
	//	bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
	//	bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLE_ID_SUPPORT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {
			
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2
					 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3  || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS){
				bankAccountDto.setBankCode(businessPartnerUser.getBusinessPartner().getBank().getBankId().toString());
			//	bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		}
		
		
		
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTime(transactionParamDTO.getFromDate());
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);

		Calendar toDate = Calendar.getInstance();
		toDate.setTime(transactionParamDTO.getToDate());
		toDate.set(Calendar.HOUR_OF_DAY, 23);
		toDate.set(Calendar.MINUTE, 59);
		toDate.set(Calendar.SECOND, 59);

		if (toDate.after(Calendar.getInstance())) {
			toDate = Calendar.getInstance();
		}

		transactionParamDTO.setFromDate(fromDate.getTime());
		transactionParamDTO.setToDate(toDate.getTime());

		TxnStatementDTO txnStatementDTO = new TxnStatementDTO();

		txnStatementDTO.setCustomerAccount(accountDto);
		txnStatementDTO.setReferenceID(customer.getCustomerId().toString());
		txnStatementDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		txnStatementDTO.setAmount(0D);
		txnStatementDTO.setFromDate(fromDate);
		txnStatementDTO.setToDate(toDate);
		txnStatementDTO.setTransactionType(EOTConstants.TXN_ID_TXNSTATEMENT + "");
		txnStatementDTO.setOtherAccount(bankAccountDto);

		try {
			// txnStatementDTO =
			// basicBankingServiceClientStub.txnStatement(txnStatementDTO);
			//
			txnStatementDTO = processRequest(CoreUrls.TRANSACTION_STATEMENT, txnStatementDTO, com.eot.dtos.basic.TxnStatementDTO.class);
			if (txnStatementDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + txnStatementDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(txnStatementDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */ finally {
			transactionDao.update(request);
		}

		if (txnStatementDTO.getTransactionList() == null) {
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND_FOR_PERIOD);
		}

		List<Transaction> txnList = Arrays.asList(txnStatementDTO.getTransactionList());

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAuthCode(txnStatementDTO.getTransactionNO());
		receipt.setNumOfTxns(txnStatementDTO.getTransactionList().length);
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setTxnList(txnList);
		receipt.setDescription(messageSource.getMessage("Txn_Statement", null, locale));

		return receipt;

	}

	@Override
	public Page getTransactions(Integer pageNumber, Long customerId, String userName) throws EOTException {

		Customer customer = customerDao.getCustomer(customerId);

		BankTellers user = webUserDao.getBankTeller(userName);

		Page page = null;

		if (user == null) {
			// Author : Koushik
			// Date : 20/07/2018
			// Purpose : 5104*/
			// Start...
			if (customer.getType() == EOTConstants.REFERENCE_TYPE_AGENT_SOLE_MERCHANT)
				page = transactionDao.getTransactionDetails(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_MERCHANT);
			else if (customer.getType() == EOTConstants.REFERENCE_TYPE_MERCHANT)
				page = transactionDao.getTransactionDetails(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_MERCHANT);
			else if (customer.getType() == EOTConstants.REFERENCE_TYPE_MERCHANT)
				page = transactionDao.getTransactionDetails(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_AGENT);
			else
				page = transactionDao.getTransactionDetails(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_CUSTOMER);
			// ....End
			/*
			 * page = transactionDao.getTransactionDetails(pageNumber,customerId,
			 * customer.getType());
			 */
		} else {
			CustomerAccount custAcc = null;
			if (user.getUserName().equals(customer.getOnbordedBy()))
				custAcc = customerDao.findCustomerAccountByBankBranch(customerId, user.getBank().getBankId(), user.getBranch().getBranchId());
			else
				custAcc = customerDao.findCustomerAccountBy(customerId);
			if (custAcc == null) {
				throw new EOTException(ErrorConstants.CUST_ACCOUNT_NOT_FOUND);
			}
			// Author : Koushik
			// Date : 20/07/2018
			// Purpose : 5104*/
			// Start..
			if (customer.getType() == EOTConstants.REFERENCE_TYPE_AGENT_SOLE_MERCHANT)
				page = transactionDao.getTransactionDetails(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_AGENT_SOLE_MERCHANT);
			else if (customer.getType() == EOTConstants.REFERENCE_TYPE_MERCHANT)
				page = transactionDao.getTransactionDetailsByAccountNumber(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_MERCHANT, custAcc.getAccountNumber());
			else if (customer.getType() == EOTConstants.REFERENCE_TYPE_AGENT)
				page = transactionDao.getTransactionDetails(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_AGENT);
			else
				page = transactionDao.getTransactionDetailsByAccountNumber(pageNumber, customerId, EOTConstants.REFERENCE_TYPE_CUSTOMER, custAcc.getAccountNumber());
			// ...End
		}

		if (page.results.size() == 0)
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

		return page;
	}

	@Override
	public Page getRequests(Integer pageNumber, Long customerId) throws EOTException {

		Customer customer = customerDao.getCustomer(customerId);

		Page page = transactionDao.getRequests(pageNumber, customerId, customer.getType());
		if (page.results.size() == 0)
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

		return page;
	}

	@Override
	public RequestReinitDTO reinitiateRequest(Long requestId, String userName) throws EOTCoreException {

		try {
			MobileRequest request = transactionDao.getRequest(requestId);

			byte[][] requestData = new byte[3][];

			requestData[0] = request.getTransactionType().getTransactionType().toString().getBytes();
			requestData[1] = request.getAppMaster().getAppId().getBytes();

			Blob req = request.getRequestString();

			requestData[2] = req.getBytes(1, (int) req.length());

			HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(appConfigurations.getMrhURL()).openConnection();

			httpURLConnection.setRequestProperty("user-name", userName);

			httpURLConnection.setDoOutput(true);

			OutputStream os = httpURLConnection.getOutputStream();
			os.write(TLVUtil.getLVArrayFromMultipleData(requestData));
			os.flush();

			int responseCode = httpURLConnection.getResponseCode();

			StringBuffer buf = new StringBuffer();

			if (responseCode == 200) {

				InputStream in = httpURLConnection.getInputStream();
				int n = 0;
				while ((n = in.read()) != -1) {
					buf.append((char) n);
				}

			}

			byte[][] response = TLVUtil.getAllDataFromLVArray(buf.toString().getBytes(), 0);

			System.out.println("response 1 - " + response[0]);
			System.out.println("response 2 - " + response[1]);

			RequestReinitDTO dto = new RequestReinitDTO();

			dto.setRequest(transactionDao.getRequest(requestId));
			dto.setStatus(Integer.parseInt(new String(response[0])));
			dto.setMessage(new String(response[1]));

			return dto;

		} catch (Exception ex) {
			throw new EOTCoreException(ErrorConstants.SERVICE_ERROR);
		}
	}

	@Override
	public List getChartList(TxnSummaryDTO txnSummaryDTO, String userName) throws EOTException {
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			txnSummaryDTO.setBankId(teller.getBank().getBankId());
		}
		List list = transactionDao.getChartList(txnSummaryDTO);
		if (txnSummaryDTO.getChartType() == null) {
			if (list == null || list.isEmpty() || list.get(0).equals(0)) {
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		} else if (txnSummaryDTO.getChartType().equals("gim")) {
			Object[] obj = (Object[]) list.get(0);
			if (list == null || list.isEmpty() || list.get(0).equals(0) || obj[1] == null) {
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		} else {
			if (list == null || list.isEmpty() || list.get(0).equals(0)) {
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		}
		return list;
	}

	@Override
	public byte[] getChartImageBytes(TxnSummaryDTO txnSummaryDTO) throws EOTException {

		String catAxis = "";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List list = transactionDao.getChartList(txnSummaryDTO);

		String transactionDetails = "";
		if (txnSummaryDTO.getImgType() != null && txnSummaryDTO.getImgType().equals("imgCount")) {
			transactionDetails = "No of Transactions";
		} else if (txnSummaryDTO.getImgType() != null && txnSummaryDTO.getImgType().equals("imgValue")) {
			transactionDetails = "Sum of Transactions";
		}

		if (txnSummaryDTO.getChartType().equals("gim")) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				dataset.setValue(Double.parseDouble(obj[0].toString()), "", new String("EOT"));
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				dataset.setValue(Double.parseDouble(obj[1].toString()), "", obj[0] != null ? obj[0].toString() : "");
			}
		}

		JFreeChart chart = ChartFactory.createBarChart("", "EOT", transactionDetails, dataset, PlotOrientation.VERTICAL, false, true, false);

		CategoryAxis categoryAxis = new CategoryAxis(catAxis);
		chart.setBackgroundPaint(Color.WHITE);
		chart.getTitle().setPaint(Color.BLACK);
		CategoryPlot p = chart.getCategoryPlot();
		p.setRangeGridlinePaint(Color.red);
		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		renderer.setMaximumBarWidth(0.07);

		p.setRenderer(renderer);
		categoryAxis.setLowerMargin(0.05);
		categoryAxis.setCategoryMargin(0.7);
		categoryAxis.setUpperMargin(0.05);
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		// TO KEEP Y AXIS SIZE SHOULD BE FIXED
		// NumberAxis axis = (NumberAxis) p.getRangeAxis();
		// axis.setRange(90, 100);
		// axis.setAutoRangeIncludesZero(false);

		BarRenderer rendere = (BarRenderer) p.getRenderer();
		rendere.setBaseItemLabelsVisible(true);
		rendere.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		rendere.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_CENTER));
		p.setDomainAxis(categoryAxis);
		p.setRenderer(rendere);
		ChartRenderingInfo info = null;
		info = new ChartRenderingInfo(new StandardEntityCollection());

		BufferedImage chartImage = chart.createBufferedImage(680, 370, info);
		PngEncoder encoder = new PngEncoder(chartImage, false, 0, 3);

		return encoder.pngEncode();

	}

	@Override
	public List<Country> getCountryList(String language) {

		return operatorDao.getCountries(language);
	}

	@Override
	public Integer getCountryMobileNumLength(Integer isdCode) throws EOTException {

		try {
			return transactionDao.getMobileNumLength(isdCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	@Override
	public byte[] getPieChartImageBytes(TxnSummaryDTO txnSummaryDTO) throws EOTException {

		String catAxis = "";
		DecimalFormat decimalFormat = new DecimalFormat("0");
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		List list = transactionDao.getChartList(txnSummaryDTO);

		if (txnSummaryDTO.getChartType().equals("gim")) {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				// pieDataset.setValue( new
				// String("EOT")+":"+obj[0].toString()+":"+obj[1].toString(),Double.parseDouble(obj[0].toString()));
				pieDataset.setValue(new String("EOT") + ":" + obj[0].toString() + ":" + decimalFormat.format(obj[1]), Double.parseDouble(obj[0].toString()));
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				// pieDataset.setValue( obj[0] != null ?
				// obj[0].toString()+":"+obj[1].toString()+":"+obj[2].toString() :
				// "",Double.parseDouble(obj[1].toString()));
				pieDataset.setValue(obj[0] != null ? obj[0].toString() + ":" + decimalFormat.format(obj[1]) + ":" + decimalFormat.format(obj[2]) : "", Double.parseDouble(obj[1].toString()));
			}
		}
		JFreeChart chart = ChartFactory.createPieChart("", pieDataset, false, true, true);

		chart.setBackgroundPaint(new Color(222, 222, 255));
		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setCircular(true);
		plot.setNoDataMessage("No data available");

		// add the chart to a panel...
		ChartRenderingInfo info = null;
		info = new ChartRenderingInfo(new StandardEntityCollection());
		BufferedImage chartImage = chart.createBufferedImage(680, 370, info);
		PngEncoder encoder = new PngEncoder(chartImage, false, 0, 3);
		return encoder.pngEncode();

	}

	@Override
	public List<Object> getAccountLedgerReport(String accountNumber, Date fromDate, Date toDate) throws EOTException {
		List<Object> list = transactionDao.getAccountLedgerReport(accountNumber, fromDate, toDate);
		return list;
	}

	public List<Object> getTrialBalance(TxnSummaryDTO txnSummaryDTO) throws EOTException {
		List<Map> accList = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING) {
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}

			accList = transactionDao.getAccountHeadList(teller.getBank().getBankId());
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			if (txnSummaryDTO.getBankId() != null && txnSummaryDTO.getBankId() != -1) {
				accList = transactionDao.getAccountHeadList(txnSummaryDTO.getBankId());
			} else {
				accList = transactionDao.getAccountHeadList(null);
			}

		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			accList = transactionDao.getAccountHeadList(txnSummaryDTO.getBankId());
		}
		List<Object> list = transactionDao.getTrialBalance(accList, txnSummaryDTO.getFromDate(), txnSummaryDTO.getToDate());

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getAccountHeadList() throws EOTException {
		Map masterData = new HashMap();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING) {
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			masterData.put("accountHeadList", transactionDao.getAccountHeadList(teller.getBank().getBankId()));

		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			masterData.put("accountHeadList", transactionDao.getAccountHeadList(null));
			masterData.put("chPoolList", bankDao.getActiveClearingHouses());
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
			masterData.put("bankList", bankDao.getBanksByGroupId(bankGroupAdmin.getBankGroup().getBankGroupId()));
		}

		return masterData;
	}

	@Override
	public List<Bank> getBankByChPoolId(String chPoolId) throws EOTException {
		List<Bank> list = transactionDao.getBankByChPoolId(chPoolId);
		return list;
	}

	public List<Map> getAccountHeadByBankId(String chPoolId) throws EOTException {
		List<Map> list = null;
		if (!chPoolId.equals("null")) {
			list = transactionDao.getAccountHeadList(Integer.parseInt(chPoolId));
		} else {
			list = transactionDao.getAccountHeadList(null);
		}

		return list;
	}

	public String getBankName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN) {
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			return teller.getBank().getBankName();
		}
		return "";
	}

	@Override
	public Page getPendingTrnasactions(String customerName, String mobileNumber, String amount, String status, String txnDate, String txnType, String userName, String fromDate, String toDate, int pageNumber) throws EOTException {
		Page page = new Page();
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		if (bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT) {
			page = transactionDao.getPendingTransactions(customerName, mobileNumber, amount, status, txnDate, txnType, bankTellers.getBank().getBankId(), fromDate, toDate, pageNumber, null);
			if (page.results.size() == 0 || page.results.isEmpty()) {
				throw new EOTException(ErrorConstants.PENDING_TXN_NOT_AVIALABLE);
			}
		}
		if (bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			page = transactionDao.getPendingTransactions(customerName, mobileNumber, amount, status, txnDate, txnType, bankTellers.getBank().getBankId(), fromDate, toDate, pageNumber, null);
			if (page.results.size() == 0 || page.results.isEmpty()) {
				throw new EOTException(ErrorConstants.PENDING_TXN_NOT_AVIALABLE);
			}
		}
		if (bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE) {
			page = transactionDao.getPendingTransactions(customerName, mobileNumber, amount, status, txnDate, txnType, bankTellers.getBank().getBankId(), fromDate, toDate, pageNumber, bankTellers.getBranch().getBranchId());
			if (page.results.size() == 0 || page.results.isEmpty()) {
				throw new EOTException(ErrorConstants.PENDING_TXN_NOT_AVIALABLE);
			}
		}

		return page;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public TransactionReceiptDTO approveTransaction(String referenceId, String userName, Long txnId, Locale locale) throws EOTException {

		Customer customer = transactionDao.verifyCustomer(Long.parseLong(referenceId.trim()));
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		PendingTransaction pendingTransaction = transactionDao.getCustomerPendingTransaction(customer.getCustomerId(), txnId);

		if (pendingTransaction.getStatus() == EOTConstants.TXN_CANCEL) {
			throw new EOTException(ErrorConstants.CANCELLED_TRANSACTION);
		}

		CustomerAccount account = transactionDao.getCustomerAliasFromAccount(pendingTransaction.getCustomer().getCustomerId(), pendingTransaction.getCustomerAccount());

		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}

		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		Account bankAccount = bankTellers.getBank().getAccount();

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		if (pendingTransaction.getTransactionType().getTransactionType() == EOTConstants.TXN_ID_DEPOSIT) {
			WebRequest request = new WebRequest();

			request.setReferenceId(customer.getCustomerId() + "");
			request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
			request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
			TransactionType txnType = new TransactionType();
			txnType.setTransactionType(EOTConstants.TXN_ID_DEPOSIT);
			request.setTransactionType(txnType);
			request.setTransactionTime(new Date());
			request.setUserName(bankTellers.getWebUser());

			transactionDao.save(request);

			com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
			accountDto.setAccountAlias(account.getAccount().getAlias());
			accountDto.setAccountNO(account.getAccountNumber());
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
			accountDto.setBankCode(account.getBank().getBankId().toString());
			accountDto.setBranchCode(account.getBranch().getBranchId().toString());

			com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

			bankAccountDto.setAccountAlias(bankAccount.getAlias());
			bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
			bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

			DepositDTO depositDTO = new DepositDTO();

			depositDTO.setReferenceID(customer.getCustomerId().toString());
			depositDTO.setAmount((new Double(pendingTransaction.getAmount())));
			depositDTO.setChannelType(EOTConstants.EOT_CHANNEL);
			depositDTO.setCustomerAccount(accountDto);
			depositDTO.setOtherAccount(bankAccountDto);
			depositDTO.setTransactionType(EOTConstants.TXN_ID_DEPOSIT + "");

			try {
				// old soap call is commented: bidyut
				// depositDTO = bankingServiceClientStub.deposit(depositDTO);
				// new rest call to core
				depositDTO = processRequest(CoreUrls.DEPOSITE_TXN_URL, depositDTO, com.eot.dtos.banking.DepositDTO.class);
				if (depositDTO.getErrorCode() != 0) {
					throw new EOTException("ERROR_" + depositDTO.getErrorCode());
				}
				request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
				com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
				txn.setTransactionId(new Long(depositDTO.getTransactionNO()));
				request.setTransaction(txn);
			} catch (EOTException e) {
				e.printStackTrace();
				request.setStatus(new Integer(depositDTO.getErrorCode()));
				throw new EOTException(e.getErrorCode());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
			} finally {
				pendingTransaction.setStatus(EOTConstants.TXN_APPROVE);
				pendingTransaction.setApprovedBy(bankTellers.getWebUser().getFirstName());
				transactionDao.update(pendingTransaction);
				transactionDao.update(request);

			}

			receipt = new TransactionReceiptDTO();
			receipt.setAccountAlias(account.getAccount().getAlias());
			receipt.setAuthCode(depositDTO.getTransactionNO() + "");
			receipt.setBalance(new DecimalFormat("#0.00").format(depositDTO.getBalance()));
			receipt.setDescription(messageSource.getMessage("Deposit", null, locale));
			receipt.setCustomerName(customer.getFirstName() + customer.getMiddleName() + customer.getLastName());
			receipt.setMobileNumber(customer.getMobileNumber());

		} else {
			WebRequest request = new WebRequest();

			request.setReferenceId(customer.getCustomerId() + "");
			request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
			request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
			TransactionType txnType1 = new TransactionType();
			txnType1.setTransactionType(EOTConstants.TXN_ID_WITHDRAWAL);
			request.setTransactionType(txnType1);
			request.setTransactionTime(new Date());
			request.setUserName(bankTellers.getWebUser());

			transactionDao.save(request);
			com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
			accountDto.setAccountAlias(account.getAccount().getAlias());
			accountDto.setAccountNO(account.getAccountNumber());
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
			accountDto.setBankCode(account.getBank().getBankId().toString());
			accountDto.setBranchCode(account.getBranch().getBranchId().toString());

			com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

			bankAccountDto.setAccountAlias(bankAccount.getAlias());
			bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
			bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
			WithdrawalDTO withdrawalDTO = new WithdrawalDTO();

			withdrawalDTO.setReferenceID(customer.getCustomerId().toString());
			withdrawalDTO.setAmount((new Double(pendingTransaction.getAmount())));
			withdrawalDTO.setChannelType(EOTConstants.EOT_CHANNEL);
			withdrawalDTO.setCustomerAccount(accountDto);
			withdrawalDTO.setOtherAccount(bankAccountDto);
			withdrawalDTO.setTransactionType(EOTConstants.TXN_ID_WITHDRAWAL + "");

			try {
				// withdrawalDTO = bankingServiceClientStub.withdrawal(withdrawalDTO);

				withdrawalDTO = processRequest(CoreUrls.WITHDRAWAL_TXN_URL, withdrawalDTO, com.eot.dtos.banking.WithdrawalDTO.class);
				if (withdrawalDTO.getErrorCode() != 0) {
					throw new EOTException("ERROR_" + withdrawalDTO.getErrorCode());
				}

				com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
				txn.setTransactionId(new Long(withdrawalDTO.getTransactionNO()));
				request.setTransaction(txn);

			} catch (EOTException e) {
				e.printStackTrace();
				request.setStatus(withdrawalDTO.getErrorCode());
				throw new EOTException(e.getErrorCode());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
			} finally {
				pendingTransaction.setStatus(EOTConstants.TXN_APPROVE);
				pendingTransaction.setApprovedBy(bankTellers.getWebUser().getFirstName());
				transactionDao.update(pendingTransaction);
				transactionDao.update(request);
			}

			receipt = new TransactionReceiptDTO();

			receipt.setAccountAlias(account.getAccount().getAlias());
			receipt.setAuthCode(withdrawalDTO.getTransactionNO() + "");
			receipt.setBalance(new DecimalFormat("#0.00").format(withdrawalDTO.getBalance()));
			receipt.setDescription(messageSource.getMessage("Withdrawl", null, locale));
			receipt.setCustomerName(customer.getFirstName() + customer.getMiddleName() + customer.getLastName());
			receipt.setMobileNumber(customer.getMobileNumber());
		}
		return receipt;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void rejectTransaction(String referenceId, String userName, String comment, Long txnId) throws EOTException {

		Customer customer = transactionDao.verifyCustomer(Long.parseLong(referenceId));
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		PendingTransaction pendingCustomer = transactionDao.getCustomerPendingTransaction(customer.getCustomerId(), txnId);

		if (pendingCustomer.getStatus() == EOTConstants.TXN_CANCEL) {
			throw new EOTException(ErrorConstants.CANCELLED_TRANSACTION);
		}

		CustomerAccount account = transactionDao.getCustomerAliasFromAccount(pendingCustomer.getCustomer().getCustomerId(), pendingCustomer.getCustomerAccount());

		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}

		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		Account bankAccount = bankTellers.getBank().getAccount();

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}
		pendingCustomer.setStatus(EOTConstants.TXN_REJECT);
		pendingCustomer.setComment(comment);
		pendingCustomer.setApprovedBy(bankTellers.getWebUser().getFirstName());
		transactionDao.update(pendingCustomer);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void cancelTransaction(String referenceId, String userName, Long txnId) throws EOTException {

		Customer customer = transactionDao.verifyCustomer(Long.parseLong(referenceId));
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		PendingTransaction pendingCustomer = transactionDao.getCustomerPendingTransaction(customer.getCustomerId(), txnId);

		if (pendingCustomer.getStatus() == EOTConstants.TXN_APPROVE || pendingCustomer.getStatus() == EOTConstants.TXN_REJECT) {
			throw new EOTException(ErrorConstants.PROCESSED_TRANSACTION);
		}

		CustomerAccount account = transactionDao.getCustomerAliasFromAccount(pendingCustomer.getCustomer().getCustomerId(), pendingCustomer.getCustomerAccount());

		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}

		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		Account bankAccount = bankTellers.getBank().getAccount();

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}
		pendingCustomer.setStatus(EOTConstants.TXN_CANCEL);
		transactionDao.update(pendingCustomer);

	}

	@Override
	public PendingTransaction getRejectedCustomer(Long txnId) {

		return transactionDao.getRejectCustomer(txnId);
	}

	@Override
	public TransactionReceiptDTO processSupportTransaction(TransactionParamDTO transactionParamDTO, String name, Locale locale) throws EOTException {

		CustomerAccount customerAccount = transactionDao.getCustomerAccountBalance(transactionParamDTO.getCustomerId());

		if (customerAccount.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();

		if (transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_BALANCE_ENQUIRY) {

			receipt.setAccountAlias(customerAccount.getAccount().getAlias());
			receipt.setBalance(new Double(customerAccount.getAccount().getCurrentBalance()).longValue() + "");
			receipt.setAuthCode("Support Transaction");
			receipt.setDescription(messageSource.getMessage("Balance_Enquiry", null, locale));

		} else if (transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_MINISTATEMENT) {
			List<Transaction> transactionList = transactionDao.getLastTransactions(customerAccount.getAccountNumber(), 5);

			receipt.setNumOfTxns(5);
			receipt.setAuthCode("Support Transaction");
			receipt.setAccountAlias(customerAccount.getAccount().getAlias());
			receipt.setTxnList(transactionList);
			receipt.setDescription(messageSource.getMessage("Mini_Statement", null, locale));

		} else {

			Calendar fromDate = Calendar.getInstance();
			fromDate.setTime(transactionParamDTO.getFromDate());
			fromDate.set(Calendar.HOUR_OF_DAY, 0);
			fromDate.set(Calendar.MINUTE, 0);
			fromDate.set(Calendar.SECOND, 0);

			Calendar toDate = Calendar.getInstance();
			toDate.setTime(transactionParamDTO.getToDate());
			toDate.set(Calendar.HOUR_OF_DAY, 23);
			toDate.set(Calendar.MINUTE, 59);
			toDate.set(Calendar.SECOND, 59);

			if (toDate.after(Calendar.getInstance())) {
				toDate = Calendar.getInstance();
			}

			transactionParamDTO.setFromDate(fromDate.getTime());
			transactionParamDTO.setToDate(toDate.getTime());

			List<Transaction> txnList = transactionDao.getLastTransactions(customerAccount.getAccountNumber(), transactionParamDTO.getFromDate(), transactionParamDTO.getToDate());

			receipt.setAuthCode("Support Transaction");
			receipt.setNumOfTxns(txnList.size());
			receipt.setAccountAlias(customerAccount.getAccount().getAlias());
			receipt.setTxnList(txnList);
			receipt.setDescription(messageSource.getMessage("Txn_Statement", null, locale));

		}

		return receipt;
	}

	@Override
	public Page searchTxnSummary(TxnSummaryDTO txnSummaryDTO, int pageNumber, String language) throws EOTException {

		Page page = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		try {
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
				page = transactionDao.searchTxnSummary(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
				BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
				if (bankGroupAdmin == null) {
					throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
				}
				page = transactionDao.searchTxnSummary(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchTxnSummary(null, teller.getBank().getBankId(), txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

				/*
				 * }else
				 * if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_TELLER){
				 * BankTellers teller = bankDao.getTellerByUsername(userName); if(teller ==
				 * null){ throw new EOTException(ErrorConstants.INVALID_TELLER); } page =
				 * transactionDao.searchTxnSummary(null,teller.getBank().getBankId(),
				 * txnSummaryDTO,pageNumber,null); if(page.getResults().size() == 0 ||
				 * page.getResults().isEmpty() || page.getResults().equals("")) throw new
				 * EOTException(ErrorConstants.NO_TXNS_FOUND);
				 */

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchTxnSummary(null, teller.getBank().getBankId(), txnSummaryDTO, pageNumber, teller.getBranch().getBranchId());
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
				page = transactionDao.searchTxnSummary(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			}else {
				txnSummaryDTO.setTransactionType(141);
				page = transactionDao.searchTxnSummary(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}

	@Override
	public List exportToXLSForTransactionSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException {

		List list = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			list = transactionDao.exportToXLSForTransactionSummary(null, null, txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}
			list = transactionDao.exportToXLSForTransactionSummary(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummary(null, teller.getBank().getBankId(), txnSummaryDTO, null);
			/*
			 * }else
			 * if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_TELLER){
			 * BankTellers teller = bankDao.getTellerByUsername(userName); if(teller ==
			 * null){ throw new EOTException(ErrorConstants.INVALID_TELLER); }
			 * list=transactionDao.exportToXLSForTransactionSummary(null,teller.getBank().
			 * getBankId(),txnSummaryDTO,null);
			 */
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummary(null, teller.getBank().getBankId(), txnSummaryDTO, teller.getBranch().getBranchId());
		}
		if (list == null || list.size() == 0)
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
		return list;
	}

	@Override
	public Map<String, Object> getMasterData(String language) throws EOTException {
		Map<String, Object> model = new HashMap<String, Object>();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			model.put("bankGroupList", bankDao.getAllBankGroups());
			model.put("bankList", bankDao.getAllBanksByName());
			model.put("countryList", operatorDao.getCountries(language));
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}
			model.put("bankList", bankDao.getBanksByGroupId(bankGroupAdmin.getBankGroup().getBankGroupId()));
			model.put("countryList", operatorDao.getCountries(language));
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			model.put("bankId", teller.getBank().getBankId());
			model.put("branchList", bankDao.getAllBranchFromBank(teller.getBank().getBankId()));
			model.put("profileList", customerDao.getCustomerProfilesByBankId(teller.getBank().getBankId()));
			model.put("countryList", operatorDao.getCountries(language));
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			model.put("branch", teller.getBranch().getLocation());
			model.put("branchList", bankDao.getAllBranchFromBank(teller.getBank().getBankId()));
			model.put("profileList", customerDao.getCustomerProfilesByBankId(teller.getBank().getBankId()));
			model.put("countryList", operatorDao.getCountries(language));
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE) {
			model.put("countryList", operatorDao.getCountries(language));
		}
		return model;
	}

	@Override
	public List exportToXLSForPendingTransactionSummary(String customerName, String mobileNumber, String txnDate, String amount, String txnType, String status, String fromDate, String toDate, Map<String, Object> model) throws EOTException {
		List list = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);

		BankTellers teller = bankDao.getTellerByUsername(userName);
		if (teller == null) {
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE) {
			list = transactionDao.exportToXLSForPendingTransactionSummary(customerName, mobileNumber, txnDate, amount, txnType, status, fromDate, toDate, teller.getBank().getBankId(), teller.getBranch().getBranchId());
		} else {
			list = transactionDao.exportToXLSForPendingTransactionSummary(customerName, mobileNumber, txnDate, amount, txnType, status, fromDate, toDate, teller.getBank().getBankId(), null);
		}
		if (list == null || list.size() == 0)
			throw new EOTException(ErrorConstants.PENDING_TXN_NOT_AVIALABLE);
		return list;
	}

	@Override
	public List exportToXLSForTransactionSummaryForBankTellerEOD(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException {

		List list = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			list = transactionDao.exportToXLSForTransactionSummaryForBankTellerEOD(null, null, txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForBankTellerEOD(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForBankTellerEOD(null, teller.getBank().getBankId(), txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForBankTeller(null, teller.getBank().getBankId(), txnSummaryDTO, userName);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForBankTellerEOD(null, teller.getBank().getBankId(), txnSummaryDTO, teller.getBranch().getBranchId());
		}
		/*
		 * if(list==null || list.size() ==0) throw new
		 * EOTException(ErrorConstants.NO_TXNS_FOUND);
		 */
		return list;
	}

	@Override
	public List exportToXLSForTransactionSummaryForTxnSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException {
		List list = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			list = transactionDao.exportToXLSForTransactionSummaryForTxnSummary(null, null, txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForTxnSummary(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForTxnSummary(null, teller.getBank().getBankId(), txnSummaryDTO, null);
			/*
			 * }else
			 * if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_TELLER){
			 * BankTellers teller = bankDao.getTellerByUsername(userName); if(teller ==
			 * null){ throw new EOTException(ErrorConstants.INVALID_TELLER); }
			 * list=transactionDao.exportToXLSForTransactionSummaryForTxnSummary(null,teller
			 * .getBank().getBankId(),txnSummaryDTO,null);
			 */
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryForTxnSummary(null, teller.getBank().getBankId(), txnSummaryDTO, teller.getBranch().getBranchId());
		}
		if (list == null || list.size() == 0)
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
		return list;
	}

	@Override
	public List exportToXLSForTransactionSummaryPerBank(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model) throws EOTException {
		List list = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
			list = transactionDao.exportToXLSForTransactionSummaryPerBank(null, null, txnSummaryDTO);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}
			list = transactionDao.exportToXLSForTransactionSummaryPerBank(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryPerBank(null, teller.getBank().getBankId(), txnSummaryDTO);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = bankDao.getTellerByUsername(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			list = transactionDao.exportToXLSForTransactionSummaryPerBank(null, teller.getBank().getBankId(), txnSummaryDTO);
		}
		if (list == null || list.size() == 0)
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
		return list;
	}

	@Override
	public Page getCancellations(String customerName, String mobileNumber, String amount, String txnDate, String txnType, String userName, String fromDate, String toDate, int pageNumber) throws EOTException {
		Page page = new Page();
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		if (bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {
			page = transactionDao.getCancellations(customerName, mobileNumber, amount, txnDate, txnType, bankTellers.getBank().getBankId(), fromDate, toDate, pageNumber, null);
			if (page.results.size() == 0 || page.results.isEmpty()) {
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		}

		if (bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || bankTellers.getWebUser().getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER) {
			page = transactionDao.getCancellations(customerName, mobileNumber, amount, txnDate, txnType, bankTellers.getBank().getBankId(), fromDate, toDate, pageNumber, bankTellers.getBranch().getBranchId());
			if (page.results.size() == 0 || page.results.isEmpty()) {
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		}
		return page;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public void voidTransaction(Integer transactionId) throws EOTException {

		com.eot.entity.Transaction transaction = transactionDao.getTransactionByTxnId(transactionId);

		VoidTransactionDTO voidTransactionDTO = new VoidTransactionDTO();

		Account customerAccount = customerDao.getAccount(transaction.getCustomerAccount());

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(customerAccount.getAlias());
		accountDto.setAccountNO(customerAccount.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		/*
		 * accountDto.setBankCode(account.getBank().getBankId().toString());
		 * accountDto.setBranchCode(account.getBranch().getBranchId().toString());
		 */

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");

		/*
		 * bankAccountDto.setAccountAlias(bankAccount.getAlias());
		 * bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		 * bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC+"");
		 * bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
		 * bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString()
		 * );
		 */

		voidTransactionDTO.setRequestID(transactionId + "");
		voidTransactionDTO.setReferenceID(customerAccount.getReferenceId());
		voidTransactionDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		voidTransactionDTO.setTransactionType("60");
		voidTransactionDTO.setCustomerAccount(accountDto);
		voidTransactionDTO.setOtherAccount(bankAccountDto);
		voidTransactionDTO.setAmount(transaction.getAmount());

		WebRequest webRequest = webUserDao.getUser(Long.parseLong(transactionId + ""));

		WebRequest request = new WebRequest();
		TransactionType txnType = new TransactionType();

		request.setReferenceId(transaction.getReferenceId());
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);

		if (transaction.getTransactionType().getTransactionType() == EOTConstants.TXN_ID_DEPOSIT) {
			txnType.setTransactionType(EOTConstants.TXN_ID_WITHDRAWAL);
		} else {
			txnType.setTransactionType(transaction.getTransactionType().getTransactionType());
		}
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		// @Start, if txn done from web, we will get the user name from web request else
		// take from login user, by Sudhanshu, dated : 11-07-2018
		if (null != webRequest) {
			BankTellers bankTellers = transactionDao.getBankTeller(webRequest.getUserName().getUserName());
			request.setUserName(bankTellers.getWebUser());
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			WebUser webUser = webUserDao.getUser(auth.getName());
			request.setUserName(webUser);
		}
		// @End, if txn done from web, we will get the user name from web request else
		// take from login user, by Sudhanshu, dated : 11-07-2018
		transactionDao.save(request);

		try {
			// voidTransactionDTO =
			// bankingServiceClientStub.voidTransaction(voidTransactionDTO);
			// rest call to core
			voidTransactionDTO = processRequest(CoreUrls.VOID_TXN_URL, voidTransactionDTO, com.eot.dtos.banking.VoidTransactionDTO.class);
			if (voidTransactionDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + voidTransactionDTO.getErrorCode());
			}
			request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(voidTransactionDTO.getTransactionNO()));
			request.setTransaction(txn);
		} catch (EOTException e) {
			e.printStackTrace();
			throw new EOTException(e.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
		}
	}

	@Override
	public Page getAdjustmentTransactions(String customerName, String mobileNumber, String amount, String txnDate, String txnType, String fromDate, String toDate, int pageNumber) throws EOTException {
		Page page = new Page();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers bankTellers = transactionDao.getBankTeller(userName);
			page = transactionDao.getAdjustmentTransactions(customerName, mobileNumber, amount, txnDate, txnType, fromDate, toDate, pageNumber, bankTellers.getBank().getBankId());
			if (page.results.size() == 0 || page.results.isEmpty()) {
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		}
		/*
		 * page=transactionDao.getAdjustmentTransactions(customerName,mobileNumber,
		 * amount,txnDate, txnType,fromDate,toDate,pageNumber,null);
		 * if(page.results.size()==0 ||page.results.isEmpty()){ throw new
		 * EOTException(ErrorConstants.NO_REVERSAL_TXNS_FOUND); }
		 */
		return page;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public void adjustmentTransaction(String transactionId, String adjustedAmount, String adjustedFee, String transactionType, String comment, String mobileNumber) throws EOTException {

		com.eot.entity.Transaction transaction = transactionDao.getTransactionByTxnId(Integer.parseInt(transactionId));

		AdjustmentTransactionDTO adjustmentTransactionDTO = new AdjustmentTransactionDTO();

		Account customerAccount = customerDao.getAccount(transaction.getCustomerAccount());

		WebRequest webRequest = webUserDao.getUser(Long.parseLong(transactionId));
		// @Start, if txn done from web, we will get the user name from web request else
		// take from login user, by Sudhanshu, dated : 11-07-2018
		BankTellers bankTellers = null;
		if (null != webRequest) {
			bankTellers = transactionDao.getBankTeller(webRequest.getUserName().getUserName());
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			WebUser webUser = webUserDao.getUser(auth.getName());
			bankTellers = transactionDao.getBankTeller(webUser.getUserName());
		}
		// @End, if txn done from web, we will get the user name from web request else
		// take from login user, by Sudhanshu, dated : 11-07-2018
		// Account bankAccount = bankTellers.getBank().getAccount();
		Account bankAccount = customerDao.getAccount(transaction.getOtherAccount());// Agent account
		WebRequest request = new WebRequest();

		request.setReferenceId(transaction.getReferenceId());
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_REVERSAL);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(bankTellers.getWebUser());

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(customerAccount.getAlias());
		accountDto.setAccountNO(customerAccount.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(customerAccount.getCustomerAccount().getBank().getBankId().toString());
		accountDto.setBranchCode(customerAccount.getCustomerAccount().getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();
		// bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC+"");
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");// Agent Account
		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		// bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		bankAccountDto.setBankCode(bankAccount.getCustomerAccount().getBank().getBankId().toString());
		bankAccountDto.setBranchCode(bankAccount.getCustomerAccount().getBranch().getBranchId().toString());

		adjustmentTransactionDTO.setTransactionTypeRef(transactionType);
		adjustmentTransactionDTO.setTransactionId(transactionId);
		adjustmentTransactionDTO.setAmount(Double.parseDouble(adjustedAmount));
		adjustmentTransactionDTO.setFee(adjustedFee);
		adjustmentTransactionDTO.setDescription(comment);
		adjustmentTransactionDTO.setReferenceID(customerAccount.getReferenceId());
		adjustmentTransactionDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		adjustmentTransactionDTO.setTransactionType("61");
		adjustmentTransactionDTO.setCustomerAccount(accountDto);
		adjustmentTransactionDTO.setOtherAccount(bankAccountDto);
		adjustmentTransactionDTO.setRequestID(transaction.getCustomerAccount());
		adjustmentTransactionDTO.setMobileNumber(mobileNumber);
		// adjustmentTransactionDTO.setClearingPoolId(((TransactionJournal)transaction.getTransactionJournals().iterator().next()).getClearingHousePool().getClearingPoolId());

		try {
			// adjustmentTransactionDTO =
			// bankingServiceClientStub.adjustmentTransaction(adjustmentTransactionDTO);

			adjustmentTransactionDTO = processRequest(CoreUrls.ADJUSTMENT_TXN_URL, adjustmentTransactionDTO, com.eot.dtos.banking.AdjustmentTransactionDTO.class);
			if (adjustmentTransactionDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + adjustmentTransactionDTO.getErrorCode());
			}

			request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(adjustmentTransactionDTO.getTransactionId()));
			request.setTransaction(txn);
		} catch (EOTException e) {
			e.printStackTrace();
			request.setStatus(adjustmentTransactionDTO.getErrorCode());
			throw new EOTException(e.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public void uploadReversalDetails(ReversalDTO reversalDTO) throws EOTException {

		List<String> invalidTxnId = new ArrayList<String>();
		List<String> invalidAdjustedAmount = new ArrayList<String>();
		List<String> invalidAdjustedFee = new ArrayList<String>();
		List<String> invalidTxnType = new ArrayList<String>();

		List<ReversalDTO> reversalFileList = FileUtil.parseReversalFile(reversalDTO.getReversalFile());

		int j = 0;
		for (j = 0; j < reversalFileList.size(); j++) {

			if (Pattern.matches("[0-9]*", reversalFileList.get(j).getTransactionId())) {
				continue;
			} else {
				invalidTxnId.add(reversalFileList.get(j).getTransactionId());
			}

			if (invalidTxnId.size() > 0) {
				throw new EOTException(ErrorConstants.INVALID_TXN_ID);
			}
		}

		for (j = 0; j < reversalFileList.size(); j++) {

			if (Pattern.matches("[0-9]*", reversalFileList.get(j).getAdjustedAmount())) {
				continue;
			} else {
				invalidAdjustedAmount.add(reversalFileList.get(j).getAdjustedAmount());
			}

			if (invalidAdjustedAmount.size() > 0) {
				throw new EOTException(ErrorConstants.INVALID_ADJUSTED_AMOUNT);
			}
		}

		for (j = 0; j < reversalFileList.size(); j++) {

			if (Pattern.matches("[0-9]*", reversalFileList.get(j).getAdjustedFee())) {
				continue;
			} else {
				invalidAdjustedFee.add(reversalFileList.get(j).getAdjustedFee());
			}

			if (invalidAdjustedFee.size() > 0) {
				throw new EOTException(ErrorConstants.INVALID_ADJUSTED_FEE);
			}
		}
		// actual
		/*
		 * for(j=0;j<reversalFileList.size();j++){
		 * 
		 * if(Pattern.matches("[0-9]*", reversalFileList.get(j).getReversedTxnType())){
		 * continue; }else{
		 * invalidTxnType.add(reversalFileList.get(j).getReversedTxnType()); }
		 * 
		 * if(invalidTxnType.size()>0){ throw new
		 * EOTException(ErrorConstants.INVALID_TXN_TYPE); } }
		 */
		// actual end
		// vineeth change

		for (j = 0; j < reversalFileList.size(); j++) {

			if (Pattern.matches("[0-9]*", reversalFileList.get(j).getReversedTxnType())) {
				continue;
			} else if (reversalFileList.get(j).getReversedTxnType().equalsIgnoreCase("Deposit") || reversalFileList.get(j).getReversedTxnType().equalsIgnoreCase("Withdrawal")) {
				if (reversalFileList.get(j).getReversedTxnType().equals("Deposit"))
					reversalFileList.get(j).setReversedTxnType("95");
				else
					reversalFileList.get(j).setReversedTxnType("99");

			} else
				invalidTxnType.add(reversalFileList.get(j).getReversedTxnType());

			if (invalidTxnType.size() > 0) {
				throw new EOTException(ErrorConstants.INVALID_TXN_TYPE);
			}
		}

		// vineeth change end
		for (int i = 0; i < reversalFileList.size(); i++) {

			ReversalDTO reversal = (ReversalDTO) reversalFileList.get(i);

			com.eot.entity.Transaction transaction = transactionDao.getTransactionByTxnId(Integer.parseInt(reversal.getTransactionId()));

			AdjustmentTransactionDTO adjustmentTransactionDTO = new AdjustmentTransactionDTO();

			Account customerAccount = customerDao.getAccount(transaction.getCustomerAccount());

			WebRequest webRequest = webUserDao.getUser(Long.parseLong(reversal.getTransactionId()));

			if (webRequest == null) {
				throw new EOTException(ErrorConstants.REVERSAL_TXN_ERROR);
			}

			BankTellers bankTellers = transactionDao.getBankTeller(webRequest.getUserName().getUserName());

			Account bankAccount = bankTellers.getBank().getAccount();

			WebRequest request = new WebRequest();

			request.setReferenceId(transaction.getReferenceId());
			request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
			request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
			TransactionType txnType = new TransactionType();
			txnType.setTransactionType(EOTConstants.TXN_ID_DEPOSIT);
			request.setTransactionType(txnType);
			request.setTransactionTime(new Date());
			request.setUserName(bankTellers.getWebUser());

			transactionDao.save(request);

			com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
			accountDto.setAccountAlias(customerAccount.getAlias());
			accountDto.setAccountNO(customerAccount.getAccountNumber());
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
			accountDto.setBankCode(customerAccount.getCustomerAccount().getBank().getBankId().toString());
			accountDto.setBranchCode(customerAccount.getCustomerAccount().getBranch().getBranchId().toString());

			com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();
			bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");

			bankAccountDto.setAccountAlias(bankAccount.getAlias());
			bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
			bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
			bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
			bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

			adjustmentTransactionDTO.setTransactionTypeRef(reversal.getReversedTxnType());
			adjustmentTransactionDTO.setTransactionId(reversal.getTransactionId());
			adjustmentTransactionDTO.setAmount(Double.parseDouble(reversal.getAdjustedAmount()));
			adjustmentTransactionDTO.setFee(reversal.getAdjustedFee());
			adjustmentTransactionDTO.setDescription(reversal.getComment());
			adjustmentTransactionDTO.setReferenceID(customerAccount.getReferenceId());
			adjustmentTransactionDTO.setChannelType(EOTConstants.EOT_CHANNEL);
			adjustmentTransactionDTO.setTransactionType("61");
			adjustmentTransactionDTO.setCustomerAccount(accountDto);
			adjustmentTransactionDTO.setOtherAccount(bankAccountDto);
			adjustmentTransactionDTO.setClearingPoolId(((TransactionJournal) transaction.getTransactionJournals().iterator().next()).getClearingHousePool().getClearingPoolId());

			try {
				adjustmentTransactionDTO = bankingServiceClientStub.adjustmentTransaction(adjustmentTransactionDTO);
				request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
				com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
				txn.setTransactionId(new Long(adjustmentTransactionDTO.getTransactionNO()));
				request.setTransaction(txn);
			} catch (EOTCoreException e) {
				e.printStackTrace();
				request.setStatus(new Integer(e.getMessageKey()));
				throw new EOTException("ERROR_" + e.getMessageKey());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
			} finally {
				transactionDao.update(request);
			}
		}
	}

	@Override
	public List<Operator> getOperatorList() {
		return transactionDao.getOperatorList();
	}

	@Override
	public Page searchExternalTxns(ExternalTransactionDTO externalTransactionDTO, int pageNumber) throws EOTException {

		Page page = transactionDao.searchExternalTxns(externalTransactionDTO, pageNumber);

		if (page.results.size() == 0 || page.results.isEmpty()) {
			throw new EOTException(ErrorConstants.EXTERNAL_TXN_NOTAVIALABLE);
		}
		return page;
	}

	@Override
	public List exportToXLSForExternalTransactionDetails(ExternalTransactionDTO externalTransactionDTO) throws EOTException {

		List list = transactionDao.getExternalTransactionDetailsForReport(externalTransactionDTO);

		if (list == null || list.size() == 0) {
			throw new EOTException(ErrorConstants.EXTERNAL_TXN_NOTAVIALABLE);
		}

		return list;
	}

	@Override
	public List exportToXlsExternalTransactionSummaryDetails(ExternalTransactionDTO externalTransactionDTO) throws EOTException {

		List list = transactionDao.getExternalTransactionSummaryDetailsForReport(externalTransactionDTO);

		if (list == null || list.size() == 0) {
			throw new EOTException(ErrorConstants.EXTERNAL_TXN_NOTAVIALABLE);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public List<TransactionParamDTO> uploadTransactionDetails(TransactionParamDTO transactionParamDTO, Locale locale, WebUserDTO webUserDTO, String timeStamp) throws EOTException {String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
	List<TransactionParamDTO> txnFileList = FileUtil.parseTransactionFile(transactionParamDTO.getTxnFile());
	List<TransactionParamDTO> bulkList=new ArrayList<>();
	//Map<String, TransactionParamDTO> errorMap=new HashMap<>();
	List<TransactionParamDTO> errorList=new ArrayList<>();
	
	double processedAmount = 0;
	double sucessedAmount = 0;
	Integer successRecordCount = 0;
	Integer failedRecord = 0;
	double failedAmount = 0;
	double serviceChargeAmount=0;
	double serviceChargeCumalative=0;
	
	int j = 0;
	BusinessPartner fileProcessor = null;
	Account fileProcessorAccount = null;
	if(null != webUserDTO && (webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3) || webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BULKPAYMENT_PARTNER))){
		
		fileProcessor = businessPartnerDao.getBusinessPartner(webUserDTO.getUserName());
		fileProcessorAccount = transactionDao.getBusinessPartnerAccount(fileProcessor.getAccountNumber());
	}
	
	for (j = 0; j < txnFileList.size(); j++) {
		
		processedAmount += txnFileList.get(j).getAmount();
		if (basicDataValidation(txnFileList.get(j), errorList, locale, processedAmount)) {
			if(businessValidation(txnFileList.get(j), errorList, locale)) {
				
				bulkList.add(txnFileList.get(j));
			}else{
				errorList.add(txnFileList.get(j));
				failedAmount = failedAmount+ txnFileList.get(j).getAmount();
			}
		}else {
			failedAmount = failedAmount+ txnFileList.get(j).getAmount();
			errorList.add(txnFileList.get(j));
		}
	}
	failedRecord = errorList.size();
	transactionParamDTO.setTotalProcessedRecord(txnFileList.size());
	transactionParamDTO.setTotalProcessedValue(processedAmount);
	transactionParamDTO.setTotalSuccessValue(sucessedAmount);
	transactionParamDTO.setTotalSuccessRecord(successRecordCount);
	transactionParamDTO.setTotalFailedRecord(failedRecord);
	transactionParamDTO.setTotalFailedValue(failedAmount);
	
	for (TransactionParamDTO dto : bulkList) {

		Customer customer = transactionDao.getCustomer(dto.getMobileNumber());
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), EOTConstants.ACCOUNT_ALIAS_MOB_CUSTOMER+" - mGurush");
		Bank bank = bankDao.getBank(account.getBank().getBankId());
		//Account bankAccount = bank.getAccount(); ///commented bank account to use login user account
		Account bankAccount = fileProcessorAccount;
		Branch branch = bankDao.getBranchCode(account.getBranch().getBranchId());

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_BULK_PAY);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		WebUser webUser = new WebUser();
		webUser.setUserName(webUserDTO.getUserName());
		request.setUserName(webUser);

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(dto.getAccountAlias());
		accountDto.setAccountNO(account.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT + "");
		accountDto.setBankCode(account.getBank().getBankId().toString());
		accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT + "");
		bankAccountDto.setBankCode(bank.getBankId().toString());
		bankAccountDto.setBranchCode(branch.getBranchId().toString());

		/*DepositDTO depositDTO = new DepositDTO();

		depositDTO.setReferenceID(customer.getCustomerId().toString());
		depositDTO.setAmount(dto.getAmount().doubleValue());
		depositDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		depositDTO.setCustomerAccount(accountDto);
		depositDTO.setOtherAccount(bankAccountDto);
		depositDTO.setTransactionType(EOTConstants.TXN_ID_DEPOSIT + "");*/
		TransferDirectDTO transferDirectDTO = new TransferDirectDTO();
		transferDirectDTO.setAmount(dto.getAmount().doubleValue());
		transferDirectDTO.setBusinessPartnerCode(dto.getBusinessPartnerCode());
		transferDirectDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		transferDirectDTO.setOtherAccount(accountDto);
		transferDirectDTO.setCustomerAccount(bankAccountDto);
		transferDirectDTO.setReferenceID(customer.getCustomerId().toString());
		//transferDirectDTO.setTransactionType(EOTConstants.TXN_ID_TRFDIRECT+"");
		transferDirectDTO.setTransactionType(EOTConstants.TXN_ID_BULK_PAY+"");
		
		try {
			 //DepositDTO response = processRequest(CoreUrls.DEPOSITE_TXN_URL, depositDTO, DepositDTO.class);
			TransferDirectDTO response = processRequest(CoreUrls.TRANSFER_DIRECT_TXN_URL, transferDirectDTO, TransferDirectDTO.class);
			//depositDTO = bankingServiceClientStub.deposit(depositDTO);
			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			if (null != response && null == response.getErrorMsg() && 0 == response.getErrorCode()) {
				request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
				txn.setTransactionId(new Long(response.getTransactionNO()));
				request.setTransaction(txn);
				dto.setStatus("Success");
				serviceChargeAmount =  response.getServiceChargeAmt();
				serviceChargeCumalative = serviceChargeCumalative + response.getServiceChargeAmt();
				dto.setServiceCharge(serviceChargeAmount);
				successRecordCount++;
				sucessedAmount =  sucessedAmount + response.getAmount();
			}else{
				request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
				if(response!= null && response.getTransactionNO() != null)
				txn.setTransactionId(new Long(response.getTransactionNO()));
				request.setTransaction(txn);
				dto.setStatus("Failed");
				serviceChargeAmount=response.getServiceChargeAmt() == null ? 0 :serviceChargeAmount + response.getServiceChargeAmt();
				dto.setServiceCharge(serviceChargeAmount);
				failedRecord++;
				failedAmount = failedAmount+ transferDirectDTO.getAmount();
				dto.setErrorDescription(messageSource.getMessage("ERROR_"+response.getErrorMsg(), null, locale));
			}
		}  catch (Exception ex) {
			failedRecord = txnFileList.size();
			failedAmount = processedAmount;
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
			transactionParamDTO.setTotalProcessedRecord(txnFileList.size());
			transactionParamDTO.setTotalProcessedValue(processedAmount);
			transactionParamDTO.setTotalSuccessValue(sucessedAmount);
			transactionParamDTO.setTotalSuccessRecord(successRecordCount);
			transactionParamDTO.setTotalFailedRecord(failedRecord);
			transactionParamDTO.setTotalFailedValue(failedAmount);
		}
	}
	
	bulkList.addAll(errorList);
	//String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	FileUploadDetail fileUploadDetail=new FileUploadDetail();
	fileUploadDetail.setCreatedDate(new Date());
	fileUploadDetail.setFileName(userName+"_"+timeStamp+".csv");
	fileUploadDetail.setFailCount(failedRecord);
	fileUploadDetail.setFailedAmount(failedAmount);
	fileUploadDetail.setStatus(1L);
	fileUploadDetail.setSuccessAmount(sucessedAmount);
	fileUploadDetail.setSuccessCount(successRecordCount);
	fileUploadDetail.setTotalCount(txnFileList.size());
	fileUploadDetail.setServiceCharge(serviceChargeCumalative);
	fileUploadDetail.setUserId(userName);
	
	Serializable id = fileProcessDao.save(fileUploadDetail);
	if(sucessedAmount+serviceChargeCumalative >0) {
	
	try {
//		DebitAlertDTO debitAlertDTO = new DebitAlertDTO();
//		debitAlertDTO.setLocale(EOTConstants.DEFAULT_LOCALE);
//		// pinDto.setLoginPIN(loginPin.toString());
//		debitAlertDTO.setMobileNo(fileProcessor.getCountry().getIsdCode() + fileProcessor.getContactPersonPhone());
//		debitAlertDTO.setAmount(new Double(sucessedAmount+serviceChargeCumalative).longValue());
//		debitAlertDTO.setBalance(new Double(fileProcessorAccount.getCurrentBalance()-sucessedAmount-serviceChargeCumalative));
//		debitAlertDTO.setScheduleDate(Calendar.getInstance());
//		debitAlertDTO.setAccountAlias(fileProcessorAccount.getAlias());
//		debitAlertDTO.setTransactionType(EOTConstants.TXN_ID_BULK_PAY+"");
//		debitAlertDTO.setTransactionId(new Integer(id.toString()));
//		smsServiceClientStub.debitAlert(debitAlertDTO);

		String amount = sucessedAmount+serviceChargeCumalative >0? new DecimalFormat("#0.00").format(sucessedAmount+serviceChargeCumalative):"0.00";
		String balance = fileProcessorAccount.getCurrentBalance()-sucessedAmount-serviceChargeCumalative >0 ? new DecimalFormat("#0.00").format(fileProcessorAccount.getCurrentBalance()-sucessedAmount-serviceChargeCumalative):"0.00";
		SmsLog smsLog = new SmsLog();
		smsLog.setMobileNumber(fileProcessor.getCountry().getIsdCode() + fileProcessor.getContactPersonPhone());
		smsLog.setMessageType(39);
		smsLog.setEncoding(1);
		smsLog.setCreatedDate(new Date());
		smsLog.setMessage("File Upload ID:"+id+" Confirmed. SSP "+amount+" Debited from your Wallet on "+new SimpleDateFormat("dd/MM/yyyy 'at' h:mm aa").format(Calendar.getInstance().getTime())+". New m-GURUSH Float balance is SSP "+balance);
		smsLog.setScheduledDate(new Date());
		smsLog.setStatus(0);
		customerDao.save(smsLog);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	}
	
	return bulkList;
	}

	/**
	 * @author bidyut kalita rest service call to core
	 */
	public <T extends com.eot.dtos.common.Header> T processRequest(String url, T obj, Class<T> type) throws Fault {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		obj.setRequestChannel("WEB");
		obj = restTemplate.postForObject(url, obj, type);

		return obj;
	}

	@Override
	public TransactionType getTransactionType(int type) throws EOTException {
		return transactionDao.getTransactionType(type);
	}

	// Transaction for Business Partner
	@Override
	public Page searchTxnSummaryForBusinessPartner(BusinessPartnerDTO businessPartnerDTO, int pageNumber, String language) throws EOTException {

		Page page = null;
		String seniorPartner = "";
		String appendString = "";
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);

		try {
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
				page = transactionDao.searchBusinessPartnerTxnSummary(null, businessPartnerDTO, pageNumber, seniorPartner);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} /*
				 * else
				 * if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_GROUP_ADMIN
				 * ){ BankGroupAdmin
				 * bankGroupAdmin=bankDao.getBankGroupByUsername(webUser.getUserName());
				 * if(bankGroupAdmin==null){ throw new
				 * EOTException(ErrorConstants.INVALID_GROUPADMIN); } page =
				 * transactionDao.searchTxnSummary(bankGroupAdmin.getBankGroup().getBankGroupId(
				 * ),null,txnSummaryDTO,pageNumber,null); if(page.getResults().size() == 0 ||
				 * page.getResults().isEmpty() || page.getResults().equals("")) throw new
				 * EOTException(ErrorConstants.NO_TXNS_FOUND);
				 * 
				 * }
				 */else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchBusinessPartnerTxnSummary(teller.getBank().getBankId(), businessPartnerDTO, pageNumber, seniorPartner);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchBusinessPartnerTxnSummary(teller.getBank().getBankId(), businessPartnerDTO, pageNumber, seniorPartner);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchBusinessPartnerTxnSummary(teller.getBank().getBankId(), businessPartnerDTO, pageNumber, seniorPartner);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
				String date1 = null;
				String date2 = null;
				if(businessPartnerDTO.getFromDate()!= null && businessPartnerDTO.getToDate()!= null && businessPartnerDTO.getFromDate()!= "" && businessPartnerDTO.getToDate()!= ""){ 
					try {					
			
						Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(businessPartnerDTO.getFromDate());
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						date1 = formatter.format(initDate);
						Date initDate1 = new SimpleDateFormat("dd/MM/yyyy").parse(businessPartnerDTO.getToDate());
						SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
						date2 = formatter1.format(initDate1); 			
			
					} catch (ParseException e) {
						e.printStackTrace();
					}}

				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				Integer bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
				String code = businessPartnerUser.getBusinessPartner().getCode();
				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
//					appendString = "bp.id IN(\n" + "(SELECT Id FROM  BusinessPartner WHERE id=" + seniorPartner + "\n" + "UNION\n" + "SELECT Id FROM  BusinessPartner WHERE SeniorPartner =" + seniorPartner + "\n" + "UNION \n" + "SELECT Id FROM  BusinessPartner WHERE SeniorPartner IN(SELECT Id FROM  BusinessPartner WHERE SeniorPartner =" + seniorPartner + ")\n" + "))";
//				} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
//					appendString = "bp.id IN(\n" + "SELECT Id FROM  BusinessPartner WHERE SeniorPartner =" + seniorPartner + "\n" + "UNION \n" + "SELECT Id FROM  BusinessPartner WHERE SeniorPartner IN(SELECT Id FROM  BusinessPartner WHERE SeniorPartner =" + seniorPartner + ")\n" + ")";
//				} else {
//					appendString = "bp.id IN(\n" + "SELECT Id FROM  BusinessPartner WHERE SeniorPartner IN(SELECT Id FROM  BusinessPartner WHERE SeniorPartner =" + seniorPartner + ")\n" + "))";
//				}
				appendString = "JOIN Customer agent ON agent.agentCode = cr.PartnerCode AND agent.Type IN (1,2)\r\n" + 
						"JOIN BusinessPartner bp ON  bp.Id=agent.PartnerId AND bp.PartnerType=2 \r\n" + 
						"\r\n" + 
						"WHERE bp.Code="+code;
						if(StringUtils.isNotBlank(businessPartnerDTO.getPartnerCode())) {
							appendString = appendString + " and cr.PartnerCode=:PartnerCode";
						} /*
						 * else { appendString = appendString + " AND cr.PartnerCode="+code; }
						 */
						if(StringUtils.isNotBlank(businessPartnerDTO.getName())) {
							appendString = appendString + " and cr.PartnerName=:PartnerName";
						} 
						if(StringUtils.isNotBlank(businessPartnerDTO.getContactNumber())) {
							appendString = appendString + " and cr.MobileNumber=:mobileNumber";
						}
						if(null != businessPartnerDTO.getTransactionId() && StringUtils.isNotBlank(businessPartnerDTO.getTransactionId())) {
							appendString = appendString + " and cr.TransactionID=:TransactionID";
						}
						if(StringUtils.isNotBlank(businessPartnerDTO.getTransactionType())) {
							appendString = appendString + " and cr.TransactionType=:TransactionType";
						}
						if(null != businessPartnerDTO.getRefTransactionId() && StringUtils.isNotBlank(businessPartnerDTO.getRefTransactionId())) {
							appendString = appendString + " and cr.RefTransactionID=:RefTransactionID";
						}
						if(businessPartnerDTO.getFromDate()!=null && !"".equals(businessPartnerDTO.getFromDate())){
								appendString = appendString + " and DATE(cr.TransactionDate)>=:fromDate AND DATE(cr.TransactionDate)<=:toDate";
						}

						appendString = appendString + " UNION ALL \r\n" + 
						" \r\n" + 
						"SELECT cr.TransactionID AS TransactionID, cr.CommissionAmount AS CommissionAmount, \r\n" + 
						"cr.PartnerCode AS PartnerCode, cr.PartnerName AS PartnerName, cr.RefTransactionID\r\n" + 
						"AS RefTransactionID, cr.ServiceCharge AS ServiceCharge, cr.Status AS STATUS,\r\n" + 
						"cr.TransactionDate AS TransactionDate, cr.TransactionType AS TransactionType FROM CommissionReport\r\n" + 
						" AS cr  WHERE cr.PartnerCode="+code;
				page = transactionDao.searchBusinessPartnerTxnSummary(bankId, businessPartnerDTO, pageNumber, appendString);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}}
		} catch (Exception e) {
			e.getStackTrace();
		}

		return page;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public void initiateAdjustmentTransaction(String transactionId, String adjustedAmount, String adjustedFee, String transactionType, String comment) throws EOTException {

		com.eot.entity.Transaction transaction = transactionDao.getTransactionByTxnId(Integer.parseInt(transactionId));
		List<SettlementJournal> settlementJournals = transactionDao.getSettlementJournals(Long.parseLong(transactionId));

		if (CollectionUtils.isNotEmpty(settlementJournals) && settlementJournals.get(0).getSettlementBatch() != null) {
			throw new EOTException(EOTConstants.SETTLED_TRANSACTION);
		}

		WebRequest webRequest = webUserDao.getUser(Long.parseLong(transactionId));
		BankTellers bankTellers = null;
		if (null != webRequest) {
			bankTellers = transactionDao.getBankTeller(webRequest.getUserName().getUserName());
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			WebUser webUser = webUserDao.getUser(auth.getName());
			bankTellers = transactionDao.getBankTeller(webUser.getUserName());
		}
		WebRequest request = new WebRequest();

		request.setReferenceId(transaction.getReferenceId());
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(Integer.parseInt(transactionType));
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(bankTellers.getWebUser());

		transactionDao.save(request);

		transactionDao.updateSettlementJournals(Long.parseLong(transactionId));
		transactionDao.updateTransactionJournals(Long.parseLong(transactionId));
		transactionDao.updateTransaction(Long.parseLong(transactionId));

		request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
		com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
		txn.setTransactionId(new Long(transactionId));
		request.setTransaction(txn);
		transactionDao.update(request);
	}

	@Override
	public AccountDetailsDTO getBusinessPartnerAccountDetails(String businessPartnerCode,Integer transactionType) throws EOTException {

	//	BusinessPartner businessPartner = businessPartnerDao.getBusinessPartnerByCode(businessPartnerCode);
		String partnerType = "";
		Integer bankId = null;
		String seniorPartner = "";
		String code="";
		BusinessPartner businessPartner =null;
		BankTellers bankTellers = null;
		Account bankAccount = null;
/*		
		if (businessPartner == null) {
			throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
		}*/
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser webUser=webUserDao.getUser(userName);
		 if ( !(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN )) {
		if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
			 BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			 code = businessPartnerUser.getBusinessPartner().getCode();
		 }
		if(!code.equals(businessPartnerCode)) {
		 if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ) {
				partnerType = "2";
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
		 }
		 else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ) {
				partnerType = "3";	
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
		 }
		
		 businessPartner = businessPartnerDao.getBusinessPartnerDetails(partnerType,bankId,seniorPartner,businessPartnerCode);
		}else if(transactionType.equals(EOTConstants.TXN_ID_TRANSFER_EMONEY)) {
			throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
		}else {
			 businessPartner = businessPartnerDao.getBusinessPartnerByCode(businessPartnerCode);
			}
		
		 }else {
			 partnerType = "1";	
			 bankTellers = transactionDao.getBankTeller(userName); 
			 bankAccount = bankTellers.getBank().getAccount();
			 businessPartner = businessPartnerDao.getBusinessPartnerDetails(partnerType,bankTellers.getBank().getBankId(),null,businessPartnerCode);
		 }
		 
		 if (businessPartner == null) {
			 if ( !(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN )) 
				 throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
			 else
				 throw new EOTException(ErrorConstants.INVALID_PRINCIPAL_AGENT);
		 }

		/*
		 * if(customer.getActive() == EOTConstants.CUSTOMER_STATUS_DEACTIVATED){ throw
		 * new EOTException(ErrorConstants.INACTIVE_CUSTOMER); }
		 */

		List<CustomerAccount> accountList = new ArrayList<CustomerAccount>();
		CustomerAccount customerAccount = new CustomerAccount();		
		customerAccount.setAccountNumber(businessPartner.getAccountNumber());
		customerAccount.setBank(businessPartner.getBank());
		customerAccount.setAccount(transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber()));

		accountList.add(customerAccount);

		AccountDetailsDTO dto = new AccountDetailsDTO();

		// dto.setMobileNumber(mobileNumber);

		dto.setCustomerName(businessPartner.getName());
		// dto.setCustomerId(customer.getCustomerId()+"");
		dto.setAccountList(accountList);
		dto.setBusinessPartnerCode(businessPartnerCode);
		/*
		 * if(customer.getCustomerDocument() != null) {
		 * dto.setExpiryDate(customer.getCustomerDocument().getExpiryDate());
		 * dto.setIdType(customer.getCustomerDocument().getIdType());
		 * dto.setIdNumber(customer.getCustomerDocument().getIdNumber());
		 * dto.setPlaceOfIssue(customer.getCustomerDocument().getPlaceOfIssue());
		 * dto.setIssueDate(customer.getCustomerDocument().getIssueDate()); }
		 */
		return dto;
	}

	private TransactionReceiptDTO processBalanceEnqForBusinessPartner(TransactionParamDTO transactionParamDTO, String userName, Locale resolveLocale, MessageSource messageSource2) throws EOTException {

		BusinessPartner businessPartner = businessPartnerDao.getBusinessPartnerByCode(transactionParamDTO.getBusinessPartnerCode());

		if (businessPartner == null) {
			throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
		}
		CustomerAccount account = new CustomerAccount();
		account.setAccountNumber(businessPartner.getAccountNumber());
		account.setBank(businessPartner.getBank());
		account.setAccount(transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber()));

		if (businessPartner.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		Account bankAccount = transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber());

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(businessPartner.getBank().getBankId(), businessPartner.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}
			
		WebRequest request = new WebRequest();

		request.setReferenceId(businessPartner.getCode());
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_BALANCE_ENQUIRY);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUserDao.getUser(userName));
		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(businessPartner.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(businessPartner.getBank().getBankId().toString());
		// accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
		bankAccountDto.setBankCode(businessPartner.getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		BalanceEnquiryDTO balanceEnquiryDTO = new BalanceEnquiryDTO();

		balanceEnquiryDTO.setReferenceID(businessPartner.getId().toString());
		balanceEnquiryDTO.setAmount(0D);
		balanceEnquiryDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		balanceEnquiryDTO.setCustomerAccount(accountDto);
		balanceEnquiryDTO.setTransactionType(EOTConstants.TXN_ID_BALANCE_ENQUIRY + "");
		balanceEnquiryDTO.setOtherAccount(bankAccountDto);
		balanceEnquiryDTO.setBusinessPartnerCode(transactionParamDTO.getBusinessPartnerCode());

		try {
			// balanceEnquiryDTO =
			// basicBankingServiceClientStub.balanceEnquiry(balanceEnquiryDTO);

			// rest call updated by bidyut
			balanceEnquiryDTO = processRequest(CoreUrls.BALANCE_ENQ_WALLET, balanceEnquiryDTO, com.eot.dtos.basic.BalanceEnquiryDTO.class);
			if (balanceEnquiryDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + balanceEnquiryDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(balanceEnquiryDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */ finally {
			transactionDao.update(request);
		}

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setAuthCode(balanceEnquiryDTO.getTransactionNO());
		receipt.setBalance(new DecimalFormat("#0.00").format(balanceEnquiryDTO.getBalance()));
		receipt.setDescription(messageSource.getMessage("Balance_Enquiry", null, resolveLocale));

		return receipt;
	}

	private TransactionReceiptDTO processBusinessPartnerMiniStatement(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		BusinessPartner businessPartner = businessPartnerDao.getBusinessPartnerByCode(transactionParamDTO.getBusinessPartnerCode());

		if (businessPartner == null) {
			throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
		}
		CustomerAccount account = new CustomerAccount();
		account.setAccountNumber(businessPartner.getAccountNumber());
		account.setBank(businessPartner.getBank());
		account.setAccount(transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber()));

		if (businessPartner.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		Account bankAccount = transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber());

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(businessPartner.getBank().getBankId(), businessPartner.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

		request.setReferenceId(businessPartner.getCode());
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_MINISTATEMENT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUserDao.getUser(userName));

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(businessPartner.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(businessPartner.getBank().getBankId().toString());
		// accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
		bankAccountDto.setBankCode(businessPartner.getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		MiniStatementDTO miniStatementDTO = new MiniStatementDTO();

		miniStatementDTO.setCustomerAccount(accountDto);
		miniStatementDTO.setReferenceID(businessPartner.getId().toString());
		miniStatementDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		miniStatementDTO.setAmount(0D);
		miniStatementDTO.setTransactionType(EOTConstants.TXN_ID_MINISTATEMENT + "");
		miniStatementDTO.setOtherAccount(bankAccountDto);
		miniStatementDTO.setBusinessPartnerCode(transactionParamDTO.getBusinessPartnerCode());

		try {
			// web service call to core : commented by bidyut
			// miniStatementDTO =
			// basicBankingServiceClientStub.miniStatement(miniStatementDTO);

			// rest call updated by bidyut
			miniStatementDTO = processRequest(CoreUrls.MINI_STATEMENT, miniStatementDTO, com.eot.dtos.basic.MiniStatementDTO.class);
			if (miniStatementDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + miniStatementDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(miniStatementDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */finally {
			transactionDao.update(request);
		}

		if (miniStatementDTO.getTransactionList() == null) {
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
		}

		List<Transaction> txnList = Arrays.asList(miniStatementDTO.getTransactionList());

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();

		receipt.setNumOfTxns(miniStatementDTO.getTransactionList().length);
		receipt.setAuthCode(miniStatementDTO.getTransactionNO());
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setTxnList(txnList);
		receipt.setDescription(messageSource.getMessage("Mini_Statement", null, locale));

		return receipt;
	}

	private TransactionReceiptDTO processBusinessPartnerTransactionStatement(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		BusinessPartner businessPartner = businessPartnerDao.getBusinessPartnerByCode(transactionParamDTO.getBusinessPartnerCode());

		if (businessPartner == null) {
			throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
		}
		CustomerAccount account = new CustomerAccount();
		account.setAccountNumber(businessPartner.getAccountNumber());
		account.setBank(businessPartner.getBank());
		account.setAccount(transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber()));

		if (businessPartner.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		Account bankAccount = transactionDao.getBusinessPartnerAccount(businessPartner.getAccountNumber());

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(businessPartner.getBank().getBankId(), businessPartner.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

		request.setReferenceId(businessPartner.getId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_TXNSTATEMENT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUserDao.getUser(userName));

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(businessPartner.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(businessPartner.getBank().getBankId().toString());
		// accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(bankAccount.getAlias());
		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
		bankAccountDto.setBankCode(businessPartner.getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		Calendar fromDate = Calendar.getInstance();
		fromDate.setTime(transactionParamDTO.getFromDate());
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);

		Calendar toDate = Calendar.getInstance();
		toDate.setTime(transactionParamDTO.getToDate());
		toDate.set(Calendar.HOUR_OF_DAY, 23);
		toDate.set(Calendar.MINUTE, 59);
		toDate.set(Calendar.SECOND, 59);

		if (toDate.after(Calendar.getInstance())) {
			toDate = Calendar.getInstance();
		}

		transactionParamDTO.setFromDate(fromDate.getTime());
		transactionParamDTO.setToDate(toDate.getTime());

		TxnStatementDTO txnStatementDTO = new TxnStatementDTO();

		txnStatementDTO.setCustomerAccount(accountDto);
		txnStatementDTO.setReferenceID(businessPartner.getId().toString());
		txnStatementDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		txnStatementDTO.setAmount(0D);
		txnStatementDTO.setFromDate(fromDate);
		txnStatementDTO.setToDate(toDate);
		txnStatementDTO.setTransactionType(EOTConstants.TXN_ID_TXNSTATEMENT + "");
		txnStatementDTO.setOtherAccount(bankAccountDto);
		txnStatementDTO.setBusinessPartnerCode(transactionParamDTO.getBusinessPartnerCode());

		try {
			// txnStatementDTO =
			// basicBankingServiceClientStub.txnStatement(txnStatementDTO);
			//
			txnStatementDTO = processRequest(CoreUrls.TRANSACTION_STATEMENT, txnStatementDTO, com.eot.dtos.basic.TxnStatementDTO.class);
			if (txnStatementDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + txnStatementDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(txnStatementDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */ finally {
			transactionDao.update(request);
		}

		if (txnStatementDTO.getTransactionList() == null) {
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND_FOR_PERIOD);
		}

		List<Transaction> txnList = Arrays.asList(txnStatementDTO.getTransactionList());

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAuthCode(txnStatementDTO.getTransactionNO());
		receipt.setNumOfTxns(txnStatementDTO.getTransactionList().length);
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setTxnList(txnList);
		receipt.setDescription(messageSource.getMessage("Txn_Statement", null, locale));

		return receipt;

	}

	private TransactionReceiptDTO processCashIn(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_AGENT);
		}
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), transactionParamDTO.getAccountAlias());
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_AGENT_ACCOUNT);
		}

		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}
		BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
		Account businessPartnerAccount = transactionDao.getBusinessPartnerAccount(businessPartnerUser.getBusinessPartner().getAccountNumber());

		/*
		 * BankTellers bankTellers = transactionDao.getBankTeller(userName); Account
		 * bankAccount = bankTellers.getBank().getAccount();
		 */

		if (businessPartnerAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), businessPartnerUser.getBusinessPartner().getBank().getBankId());

		if (chPool == null) {
			if (customer.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT))
				throw new EOTException(ErrorConstants.INVALID_CH_POOL_AGENT);
			else
				throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_AGENT);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_CASH_IN);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUserDao.getUser(userName));

		transactionDao.save(request);

		Integer groupId = account.getBank().getBankGroup() != null ? account.getBank().getBankGroup().getBankGroupId() : null;

		TransactionRuleTxn transactionRuleTxn = transactionDao.getTransactionRule(transactionParamDTO.getAmount(), transactionParamDTO.getTransactionType(), customer.getCustomerProfiles().getProfileId(), groupId);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());
		accountDto.setAccountNO(account.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(account.getBank().getBankId().toString());
		accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(businessPartnerAccount.getAlias());
		bankAccountDto.setAccountNO(businessPartnerAccount.getAccountNumber());
		// bankAccountDto.setAccountType(EOTConstants.ACCOUT_TYPE_BUSINESSPARTNER+"");
		bankAccountDto.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL + "");
		bankAccountDto.setBankCode(businessPartnerUser.getBusinessPartner().getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		DepositDTO depositDTO = new DepositDTO();

		depositDTO.setReferenceID(customer.getCustomerId().toString());
		depositDTO.setAmount(null != transactionParamDTO.getAmount() ? transactionParamDTO.getAmount().doubleValue():0);
		depositDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		depositDTO.setCustomerAccount(accountDto);
		depositDTO.setOtherAccount(bankAccountDto);
		depositDTO.setTransactionType(EOTConstants.TXN_ID_CASH_IN + "");

		try {
			// spring web service call to core : commented by bidyut
			// depositDTO = bankingServiceClientStub.deposit(depositDTO);

			// rest call updated by bidyut
			depositDTO = processRequest(CoreUrls.DEPOSITE_TXN_URL, depositDTO, com.eot.dtos.banking.DepositDTO.class);
			if (depositDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + depositDTO.getErrorCode());
			}

			request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(depositDTO.getTransactionNO()));
			request.setTransaction(txn);
		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */catch (Fault ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
		}

		Double serviceCharge = depositDTO.getServiceChargeAmt();
		Double stampFee = scManagementDao.getStampFeeFromServiceChargeSplit(businessPartnerUser.getBusinessPartner().getBank().getBankId()).get(0).getServiceChargePct().doubleValue();

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setAuthCode(depositDTO.getTransactionNO());
		receipt.setTransactionType(Integer.parseInt(depositDTO.getTransactionType()));
		receipt.setBalance(new DecimalFormat("#0.00").format(depositDTO.getBalance()));
		receipt.setDescription(messageSource.getMessage("Cash_In", null, locale));
		Double amount = depositDTO.getAmount() - serviceCharge;
		receipt.setAmount(amount.longValue());

		return receipt;

	}

	private TransactionReceiptDTO processCashOut(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException, NoSuchAlgorithmException, UnsupportedEncodingException {

		transactionParamDTO = customerService.getAgentCashOutDetails(transactionParamDTO);
		com.eot.banking.dto.OtpDTO otpDto = new com.eot.banking.dto.OtpDTO();

		otpDto.setOtphash(HashUtil.generateHash(transactionParamDTO.getOtp().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		otpDto.setReferenceId(transactionParamDTO.getCustomer().getCustomerId()+"");
		otpDto.setReferenceType(EOTConstants.REF_TYPE_AGENT);
		otpDto.setOtpType(EOTConstants.REFERENCE_TYPE_CASH_OUT);
		Otp otp = customerDao.verifyOTP(otpDto);
		if (otp == null) {
			throw new EOTException(ErrorConstants.INVALID_AGENT_OTP);
		}
		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());

		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_AGENT);
		}
		if (!customer.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT)) {
			throw new EOTException(ErrorConstants.INVALID_AGENT);
		}
		if (customer.getActive() == EOTConstants.CUSTOMER_STATUS_DEACTIVATED) {
			throw new EOTException(ErrorConstants.INACTIVE_AGENT);
		}

		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), EOTConstants.ACCOUNT_ALIAS_COMMISSION_CASHOUT);
		if (account == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
			throw new EOTException(ErrorConstants.INACTIVE_BANK);
		}

		BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
		Account businessPartnerAccount = transactionDao.getBusinessPartnerAccount(businessPartnerUser.getBusinessPartner().getAccountNumber());

		/*
		 * BankTellers bankTellers = transactionDao.getBankTeller(userName);
		 * 
		 * Account bankAccount = bankTellers.getBank().getAccount();
		 */

		if (businessPartnerAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), businessPartnerUser.getBusinessPartner().getBank().getBankId());

		if (chPool == null) {
			if (customer.getType().equals(1))
				throw new EOTException(ErrorConstants.INVALID_CH_POOL_AGENT);
			else
				throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REF_TYPE_AGENT);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_CASH_OUT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUserDao.getUser(userName));

		transactionDao.save(request);

		Integer groupId = account.getBank().getBankGroup() != null ? account.getBank().getBankGroup().getBankGroupId() : null;

		TransactionRuleTxn transactionRuleTxn = transactionDao.getTransactionRule(transactionParamDTO.getAmount(), transactionParamDTO.getTransactionType(), customer.getCustomerProfiles().getProfileId(), groupId);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(EOTConstants.ACCOUNT_ALIAS_COMMISSION_CASHOUT);
		accountDto.setAccountNO(account.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(account.getBank().getBankId().toString());
		accountDto.setBranchCode(account.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(businessPartnerAccount.getAlias());
		bankAccountDto.setAccountNO(businessPartnerAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL + "");
		bankAccountDto.setBankCode(businessPartnerUser.getBusinessPartner().getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		WithdrawalDTO withdrawalDTO = new WithdrawalDTO();

		withdrawalDTO.setReferenceID(customer.getCustomerId().toString());
		withdrawalDTO.setAmount(transactionParamDTO.getAmount().doubleValue());
		withdrawalDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		withdrawalDTO.setCustomerAccount(accountDto);
		withdrawalDTO.setOtherAccount(bankAccountDto);
		withdrawalDTO.setTransactionType(EOTConstants.TXN_ID_CASH_OUT + "");

		try {
			// spring web service call commented by bidyut
			// withdrawalDTO = bankingServiceClientStub.withdrawal(withdrawalDTO);

			// rest call updated by bidyut
			withdrawalDTO = processRequest(CoreUrls.WITHDRAWAL_TXN_URL, withdrawalDTO, com.eot.dtos.banking.WithdrawalDTO.class);
			if (withdrawalDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + withdrawalDTO.getErrorCode());
			}

			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(withdrawalDTO.getTransactionNO()));
			request.setTransaction(txn);

		} /*
			 * catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); }
			 */ catch (Fault ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
		}
		PendingTransaction pendingTransaction = customerDao.getPendingTransaction(transactionParamDTO.getTransactionId());
		pendingTransaction.setStatus(EOTConstants.TRANSACTION_STATUS_SUCCESS);
		transactionDao.update(pendingTransaction);

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAccountAlias(account.getAccount().getAlias());
		receipt.setAmount(withdrawalDTO.getAmount().longValue());
		receipt.setAuthCode(withdrawalDTO.getTransactionNO());
		receipt.setBalance(new DecimalFormat("#0.00").format(withdrawalDTO.getBalance()));
		receipt.setDescription(messageSource.getMessage("Cash_Out", null, locale));
		receipt.setCustomerName(transactionParamDTO.getName());
		receipt.setAccountAlias(EOTConstants.ACCOUNT_ALIAS_COMMISSION_CASHOUT);
		transactionParamDTO.setCustomerMobileNo("");
		transactionParamDTO.setCustomerCode("");

		return receipt;

	}
	
	private TransactionReceiptDTO processTransferEmoney(TransactionParamDTO transactionParamDTO, String userName, Locale locale, MessageSource messageSource) throws EOTException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		BusinessPartner account = new BusinessPartner();
		BankTellers bankTellers = null;
		Account bankAccount = null;
		BusinessPartnerUser businessPartnerUserfrom =null;
		Account businessPartnerAccountfrom = null;
		
			BusinessPartner businessPartnerto = businessPartnerDao.getBusinessPartnerByCode(transactionParamDTO.getBusinessPartnerCode());

			if (businessPartnerto == null) {
				throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
			}
			
			account.setAccountNumber(businessPartnerto.getAccountNumber());
			account.setBank(businessPartnerto.getBank());
			account.setCode(businessPartnerto.getCode());

			if (businessPartnerto.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {
				throw new EOTException(ErrorConstants.INACTIVE_BANK);
			}

			bankAccount = transactionDao.getBusinessPartnerAccount(businessPartnerto.getAccountNumber());

			if (bankAccount == null) {
				throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
			}	
		/*
		 * if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ){
		 * bankTellers = transactionDao.getBankTeller(userName); bankAccount =
		 * bankTellers.getBank().getAccount(); if (bankAccount == null) { throw new
		 * EOTException(ErrorConstants.INVALID_BANK_ACCOUNT); } }
		 */
		// change over
		 if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ){

			 	 bankTellers = transactionDao.getBankTeller(userName);
				 businessPartnerAccountfrom = transactionDao.getBusinessPartnerAccount(bankTellers.getBank().getAccount().getAccountNumber());

				if (businessPartnerAccountfrom == null) {
					throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
				}
				List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), bankTellers.getBank().getBankId());

				if (chPool == null) {
						throw new EOTException(ErrorConstants.INVALID_CH_POOL);
				}

		 }else {
				
				 businessPartnerUserfrom = businessPartnerService.getBusinessPartnerUser(userName);
				 businessPartnerAccountfrom = transactionDao.getBusinessPartnerAccount(businessPartnerUserfrom.getBusinessPartner().getAccountNumber());

				if (businessPartnerAccountfrom == null) {
					throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
				}
				List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(account.getBank().getBankId(), businessPartnerUserfrom.getBusinessPartner().getBank().getBankId());

				if (chPool == null) {
						throw new EOTException(ErrorConstants.INVALID_CH_POOL);
				}

		 }



		WebRequest request = new WebRequest();
		
		 if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ){
			 request.setReferenceId(bankTellers.getBank().getBankId().toString());
			 request.setReferenceType(EOTConstants.REFERENCE_TYPE_BANK);
		 }else {
			 request.setReferenceId(account.getCode());
			 request.setReferenceType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		 }		
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_TRANSFER_EMONEY);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(webUserDao.getUser(userName));

		transactionDao.save(request);

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();

			accountDto.setAccountAlias(transactionParamDTO.getAccountAlias());	
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
			accountDto.setAccountNO(account.getAccountNumber());
			accountDto.setBankCode(account.getBank().getBankId().toString());
		 

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		 if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ){
			 bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());				
		 }else {
			 bankAccountDto.setBankCode(businessPartnerUserfrom.getBusinessPartner().getBank().getBankId().toString());				
		 }	
		 bankAccountDto.setAccountAlias(businessPartnerAccountfrom.getAlias());
		 bankAccountDto.setAccountNO(businessPartnerAccountfrom.getAccountNumber());

		bankAccountDto.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL + "");

		// limit
		

		LimitDTO limit_businessPartnerL1_acc = new LimitDTO();
		limit_businessPartnerL1_acc.setCustomerAccount(bankAccountDto);
		limit_businessPartnerL1_acc.setAmount(transactionParamDTO.getAmount().doubleValue());
		limit_businessPartnerL1_acc.setOtherAccount(accountDto);
		limit_businessPartnerL1_acc.setLimit_cr_desc("Limit credited " + accountDto);
		limit_businessPartnerL1_acc.setLimit_dr_desc("Limit debited for acc" + bankAccountDto);
		limit_businessPartnerL1_acc.setTransactionType(EOTConstants.TXN_ID_TRANSFER_EMONEY + "");
		limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
		limit_businessPartnerL1_acc.setReferenceID(account.getCode());
		limit_businessPartnerL1_acc.setChannelType(EOTConstants.EOT_CHANNEL);
		limit_businessPartnerL1_acc.setBusinessPartnerCode(account.getCode());
	//	limit_businessPartnerL1_acc.setHeaderList(new ArrayList<LimitDTO>());

		try {
			limit_businessPartnerL1_acc = processRequest(CoreUrls.TRANSFER_EMONEY_URL, limit_businessPartnerL1_acc, com.eot.dtos.banking.LimitDTO.class);
		if (limit_businessPartnerL1_acc.getErrorCode() != 0) {
			throw new EOTException("ERROR_" + limit_businessPartnerL1_acc.getErrorCode());
		}
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
		com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
		txn.setTransactionId(new Long(limit_businessPartnerL1_acc.getTransactionNO()));
		request.setTransaction(txn);
		}catch (Fault ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
		}
		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAccountAlias(transactionParamDTO.getAccountAlias());
		receipt.setAuthCode(limit_businessPartnerL1_acc.getTransactionNO());
		receipt.setTransactionType(Integer.parseInt(limit_businessPartnerL1_acc.getTransactionType()));
	//	receipt.setBalance(new DecimalFormat("#0.00").format(limit_businessPartnerL1_acc.getBalance()));
		receipt.setBalance(new DecimalFormat("#0.00").format(businessPartnerAccountfrom.getCurrentBalance()-transactionParamDTO.getAmount().doubleValue()));
		receipt.setDescription(messageSource.getMessage("Transfer_eMoney", null, locale));
		Double amount = transactionParamDTO.getAmount().doubleValue();
		receipt.setAmount(amount.longValue());

		return receipt;
		
		
		// limit
		
		/*
		 * DepositDTO depositDTO = new DepositDTO();
		 * 
		 * if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ){
		 * depositDTO.setReferenceID(bankTellers.getBank().getBankId().toString());
		 * }else { depositDTO.setReferenceID(account.getCode()); }
		 * 
		 * depositDTO.setAmount(transactionParamDTO.getAmount().doubleValue());
		 * depositDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		 * depositDTO.setCustomerAccount(accountDto);
		 * depositDTO.setOtherAccount(bankAccountDto);
		 * depositDTO.setTransactionType(EOTConstants.TXN_ID_TRANSFER_EMONEY + "");
		 * depositDTO.setBusinessPartnerCode(transactionParamDTO.getBusinessPartnerCode(
		 * ));
		 */
		/*
		 * try { // spring web service call to core : commented by bidyut // depositDTO
		 * = bankingServiceClientStub.deposit(depositDTO);
		 * 
		 * // rest call updated by bidyut depositDTO =
		 * processRequest(CoreUrls.DEPOSITE_TXN_URL, depositDTO,
		 * com.eot.dtos.banking.DepositDTO.class); if (depositDTO.getErrorCode() != 0) {
		 * throw new EOTException("ERROR_" + depositDTO.getErrorCode()); }
		 * 
		 * request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
		 * com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
		 * txn.setTransactionId(new Long(depositDTO.getTransactionNO()));
		 * request.setTransaction(txn); } catch (Fault ex) { ex.printStackTrace(); throw
		 * new EOTException(ErrorConstants.CORE_CONNECTION_ERROR); } finally {
		 * transactionDao.update(request); }
		 */

	//	Double serviceCharge = depositDTO.getServiceChargeAmt();
	//	Double stampFee = scManagementDao.getStampFeeFromServiceChargeSplit(businessPartnerUserfrom.getBusinessPartner().getBank().getBankId()).get(0).getServiceChargePct().doubleValue();

		/*
		 * TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		 * receipt.setAccountAlias(transactionParamDTO.getAccountAlias());
		 * receipt.setAuthCode(depositDTO.getTransactionNO());
		 * receipt.setTransactionType(Integer.parseInt(depositDTO.getTransactionType()))
		 * ; receipt.setBalance(new
		 * DecimalFormat("#0.00").format(depositDTO.getBalance()));
		 * receipt.setDescription(messageSource.getMessage("Transfer_eMoney", null,
		 * locale)); Double amount = depositDTO.getAmount() - serviceCharge;
		 * receipt.setAmount(amount.longValue());
		 * 
		 * return receipt;
		 */

	}

	
private boolean basicDataValidation(TransactionParamDTO transactionParamDTO,List<TransactionParamDTO> errorList, Locale locale, double processedAmount) {
		
		boolean check=true;
		
		if ( transactionParamDTO.getMobileNumber() == null || transactionParamDTO.getMobileNumber().equals("")) {
			
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.MOBILE_NO_EMPTY, null, locale));
			//errorMap.put(transactionParamDTO.getMobileNumber(), transactionParamDTO);
			check=false;
		}
		

		/*if ( transactionParamDTO.getAccountAlias() == null || transactionParamDTO.getAccountAlias().equals("")) {
			
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.ACC_ALIAS_EMPTY, null, locale));
			//errorMap.put(transactionParamDTO.getMobileNumber(), transactionParamDTO);
			check=false;
		}*/
		

		if ( transactionParamDTO.getAmount() == null || transactionParamDTO.getAmount() == 0) {
			
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.AMT_EMPTY, null, locale));
			//errorMap.put(transactionParamDTO.getMobileNumber(), transactionParamDTO);
			check=false;
		}
		
		if (!Pattern.matches("[0-9]*", transactionParamDTO.getMobileNumber())) {
			
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.INVALID_MOBILE_NUMBER, null, locale));
			//errorMap.put(transactionParamDTO.getMobileNumber(), transactionParamDTO);
			check=false;
		}
		
		if (!Pattern.matches("[0-9]*", transactionParamDTO.getAmount().toString())) {
			
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.INVALID_ADJUSTED_AMOUNT, null, locale));
			//errorMap.put(transactionParamDTO.getMobileNumber(), transactionParamDTO);
			check=false;
		}else{
			processedAmount += processedAmount+transactionParamDTO.getAmount();
		}
		
		return check;
	}
	
	private boolean  businessValidation(TransactionParamDTO transactionParamDTO,List<TransactionParamDTO> errorList, Locale locale) {
		
		Customer customer = transactionDao.getCustomer(transactionParamDTO.getMobileNumber());
		
		if (customer == null) {
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.INVALID_CUSTOMER, null, locale));
			return false;
		}
		
		CustomerAccount account = transactionDao.getCustomerAccountFromAlias(customer.getCustomerId(), EOTConstants.ACCOUNT_ALIAS_MOB_CUSTOMER+" - mGurush");
		
		if (account == null) {
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.INVALID_CUSTOMER_ACCOUNT, null, locale));
			return false;
		}

		if (account.getBank().getStatus() == EOTConstants.INACTIVE_BANK) {

			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.INVALID_CUSTOMER_ACCOUNT, null, locale));
			return false;
		}
		
		Bank bank = bankDao.getBank(account.getBank().getBankId());
		Account bankAccount = bank.getAccount();

		if (bankAccount == null) {
			
			setErrorDesc(transactionParamDTO,messageSource.getMessage(ErrorConstants.INVALID_BANK_ACCOUNT, null, locale));
			return false;
		}
		return true;
	}

	private void setErrorDesc(TransactionParamDTO transactionParamDTO, String description) {
		transactionParamDTO.setErrorDescription( description);
		transactionParamDTO.setStatus("Failed");
		transactionParamDTO.setErrorDate(new Date());
	}
	
	
	@Override
	public void downloadBulkPaymentFailFile() throws EOTException {
		FileUploadDetail fileUploadDetail = transactionDao.getLastProcessedFile();
		TransactionParamDTO transactionParamDTO = new TransactionParamDTO();
		
		//System.out.println(fileUploadDetail.getFileName());
	}

	@Override
	public Map<Integer, TransactionType> getTxntypeMap(String locale) {
		return transactionDao.getTxntypeMap(locale);
	}
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public void saveCommissionData(CommissionReport commissionReport) {
		transactionDao.save(commissionReport);
	}
	
	
	
	@Override
	public TransactionReceiptDTO accountTransfer(TransactionParamDTO fundTransferDTO, String userName, Locale locale,
			MessageSource messageSource) throws EOTException {


		Account fromAccount= transactionDao.getAccountByAccountNumber(fundTransferDTO.getFromAccountNumber());

		if (fromAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_ACCOUNT);
		}
		
		BankTellers bankTellers = transactionDao.getBankTeller(userName);

		Account bankAccount = bankTellers.getBank().getAccount();

		if (bankAccount == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_ACCOUNT);
		}

		List<ClearingHousePoolMember> chPool = transactionDao.getClearingHouse(bankTellers.getBank().getBankId(),
				bankTellers.getBank().getBankId());
		if (chPool == null) {
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}

		WebRequest request = new WebRequest();

//		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_DEPOSIT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());
		request.setUserName(bankTellers.getWebUser());

		transactionDao.save(request);

//		Integer groupId = account.getBank().getBankGroup() != null ? account.getBank().getBankGroup().getBankGroupId()
//				: null;
//
//		TransactionRuleTxn transactionRuleTxn = transactionDao.getTransactionRule(fundTransferDTO.getAmount(),
//				fundTransferDTO.getTransactionType(), customer.getCustomerProfiles().getProfileId(), groupId);

		//==========================================================================================
		com.eot.dtos.common.Account fromAccDTO = new com.eot.dtos.common.Account();
		fromAccDTO.setAccountAlias(fromAccount.getAlias());
		fromAccDTO.setAccountType(fromAccount.getAccountType().toString());
		fromAccDTO.setAccountNO(fromAccount.getAccountNumber());
		fromAccDTO.setBankCode(bankTellers.getBank().getBankId().toString());
		fromAccDTO.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		
		Account toAccount= transactionDao.getAccountByAccountNumber(fundTransferDTO.getToAccountNo());


		com.eot.dtos.common.Account toAccDTO = new com.eot.dtos.common.Account();
		toAccDTO.setAccountAlias(toAccount.getAlias());
		toAccDTO.setAccountNO(toAccount.getAccountNumber());
		toAccDTO.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		toAccDTO.setBankCode(bankTellers.getBank().getBankId().toString());
		toAccDTO.setBranchCode(bankTellers.getBranch().getBranchId().toString());
		
//		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();
//
//		bankAccountDto.setAccountAlias(bankAccount.getAlias());
//		bankAccountDto.setAccountNO(bankAccount.getAccountNumber());
//		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_BANK_ACC + "");
//		bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
//		bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		//==========================================================================================

		TransferDirectDTO transferDirectDTO = new TransferDirectDTO();

		transferDirectDTO.setCustomerAccount(fromAccDTO);
		transferDirectDTO.setOtherAccount(toAccDTO);
		transferDirectDTO.setAmount(fundTransferDTO.getAmount().doubleValue());
		transferDirectDTO.setChannelType(EOTConstants.EOT_CHANNEL);
//		transferDirectDTO.setRequestID(requestID.toString());
		transferDirectDTO.setReferenceID(bankTellers.getBank().getBankId().toString());
		//			transferDirectDTO.setReferenceType(referenceType);
		transferDirectDTO.setTransactionType(fundTransferDTO.getTransactionType() + "");
		//			transferDirectDTO.setPayeeName(otherAccount.getCustomer().getFirstName());

		try {
			// spring web service call to core : commented by bidyut
			// depositDTO = bankingServiceClientStub.deposit(depositDTO);

			// rest call updated by bidyut
			transferDirectDTO = processRequest(CoreUrls.TRANSFER_DIRECT_TXN_URL, transferDirectDTO,
					com.eot.dtos.banking.TransferDirectDTO.class);
			if (transferDirectDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + transferDirectDTO.getErrorCode());
			}

			request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(transferDirectDTO.getTransactionNO()));
			request.setTransaction(txn);
		} /* catch (EOTCoreException e) { e.printStackTrace(); request.setStatus(new
			 * Integer(e.getMessageKey())); throw new
			 * EOTException("ERROR_"+e.getMessageKey()); } */catch (Fault ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			transactionDao.update(request);
		}

		Double serviceCharge = transferDirectDTO.getServiceChargeAmt();
		Double stampFee = scManagementDao.getStampFeeFromServiceChargeSplit(bankTellers.getBank().getBankId()).get(0)
				.getServiceChargePct().doubleValue();

		TransactionReceiptDTO receipt = new TransactionReceiptDTO();
		receipt.setAccountAlias(fromAccount.getAlias());
		receipt.setAuthCode(transferDirectDTO.getTransactionNO());
		receipt.setDescription(messageSource.getMessage("accountTransfer", null, locale));
		Double amount = fundTransferDTO.getAmount() - serviceCharge;
		receipt.setAmount(amount.longValue());

		return receipt;

	}

	@Override
	public Page searchTxnMerchantData(TxnSummaryDTO txnSummaryDTO, Integer pageNumber, String string) throws EOTException  {
		Page page = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		try {
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
				page = transactionDao.searchTxnMerchantData(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
				BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
				if (bankGroupAdmin == null) {
					throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
				}
				page = transactionDao.searchTxnMerchantData(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchTxnMerchantData(null, teller.getBank().getBankId(), txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchTxnMerchantData(null, teller.getBank().getBankId(), txnSummaryDTO, pageNumber, teller.getBranch().getBranchId());
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			}else {
				txnSummaryDTO.setTransactionType(141);
				page = transactionDao.searchTxnMerchantData(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}

	@Override
	public Page searchTxnCustomerRegistration(TxnSummaryDTO txnSummaryDTO, int pageNumber, String language)
			throws EOTException {
		Page page = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		try {
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD) {
				page = transactionDao.searchTxnSummaryCustomerRegistration(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
				BankGroupAdmin bankGroupAdmin = bankDao.getBankGroupByUsername(webUser.getUserName());
				if (bankGroupAdmin == null) {
					throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
				}
				page = transactionDao.searchTxnSummaryCustomerRegistration(bankGroupAdmin.getBankGroup().getBankGroupId(), null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchTxnSummaryCustomerRegistration(null, teller.getBank().getBankId(), txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

				/*
				 * }else
				 * if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_TELLER){
				 * BankTellers teller = bankDao.getTellerByUsername(userName); if(teller ==
				 * null){ throw new EOTException(ErrorConstants.INVALID_TELLER); } page =
				 * transactionDao.searchTxnSummary(null,teller.getBank().getBankId(),
				 * txnSummaryDTO,pageNumber,null); if(page.getResults().size() == 0 ||
				 * page.getResults().isEmpty() || page.getResults().equals("")) throw new
				 * EOTException(ErrorConstants.NO_TXNS_FOUND);
				 */

			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				BankTellers teller = bankDao.getTellerByUsername(userName);
				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
				page = transactionDao.searchTxnSummaryCustomerRegistration(null, teller.getBank().getBankId(), txnSummaryDTO, pageNumber, teller.getBranch().getBranchId());
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			}else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
				page = transactionDao.searchTxnSummaryCustomerRegistration(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			}else {
				txnSummaryDTO.setTransactionType(141);
				page = transactionDao.searchTxnSummaryCustomerRegistration(null, null, txnSummaryDTO, pageNumber, null);
				if (page.getResults().size() == 0 || page.getResults().isEmpty())
					throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}
	@Override
	public List<AccountDetailsDTO> getAccountsForAccountToAccount() throws EOTException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		
		List<AccountDetailsDTO> accountList = new ArrayList<AccountDetailsDTO>();
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
				BusinessPartner bp=businessPartnerService.getBusinessPartner(webUser.getUserName());
				AccountDetailsDTO acc =  new AccountDetailsDTO();
				acc.setAccountNumber(bp.getAccountNumber());
				acc.setAccountAlias("wallet account");
				accountList.add(acc);
				
				AccountDetailsDTO bpCommissionAcc =  new AccountDetailsDTO();
				bpCommissionAcc.setAccountNumber(bp.getCommissionAccount());
				bpCommissionAcc.setAccountAlias("Commission Account");
				accountList.add(bpCommissionAcc);
				
				
			}
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN) {
				BankTellers bnkTaller =  bankDao.getTellerByUsername(webUser.getUserName());
				AccountDetailsDTO acc =  new AccountDetailsDTO();
				acc.setAccountNumber(bnkTaller.getBank().getAccount().getAccountNumber());
				acc.setAccountAlias("mGurush account");
				accountList.add(acc);
				
				AccountDetailsDTO bpCommissionAcc =  new AccountDetailsDTO();
				bpCommissionAcc.setAccountNumber("1000000000107");
				bpCommissionAcc.setAccountAlias("Commission Account");
				accountList.add(bpCommissionAcc);
			}
			return accountList;
	}

	@Override
	public AccountDetailsDTO getAccountsForAccToAcc(String fromAccountNo,String toAccountNo,Integer txnType, boolean flag) throws EOTException {

		String fromAccountAlias="";
		String toAccountAlias="";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		
		Account fromAccount= customerDao.getAccount(fromAccountNo);
		Account toAccount= customerDao.getAccount(toAccountNo);
		if (webUser.getWebUserRole().getRoleId() == 2) {
			if (fromAccount.getReferenceType().equals("7")) {
				fromAccountAlias = "Commission Account";
			} else {
				fromAccountAlias = "wallet Account";
			}

			if (!toAccount.getReferenceType().equals("7")) {
				toAccountAlias = "Commission Account";
			} else {
				toAccountAlias = "wallet Account";
			}
		} else {
			if (fromAccount.getAliasType() == EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT) {
				fromAccountAlias = "Commission Account";
			} else {
				fromAccountAlias = "wallet Account";
			}

			if ( toAccount.getAliasType() == EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT) {
				toAccountAlias = "Commission Account";
			} else {
				toAccountAlias = "wallet Account";
			}
		}
		
		
		AccountDetailsDTO dto = new AccountDetailsDTO();

		dto.setFromAccountAlias(fromAccountAlias);
		dto.setFromAccountNumber(fromAccountNo);
		dto.setToAccountAlias(toAccountAlias);
		dto.setToAccountNumber(toAccountNo);
		return dto;
	}

	@Override
	public Page searchBulkPayTxnReport(TxnSummaryDTO txnSummaryDTO, Integer pageNumber, String string)
			throws EOTException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		txnSummaryDTO.setLoginUserName(auth.getName());
		Page page = transactionDao.searchBulkPayTxnReport( txnSummaryDTO, pageNumber);
		return page;
	}

	@Override
	public Page BankFloatDepositReportData(BankFloatDepositDTO bankFloatDepositDTO, Integer pageNumber, String string) {
		Page page = transactionDao.BankFloatDepositReportData( bankFloatDepositDTO, pageNumber);
		return page;
	}

	@Override
	public Page NonRegUssdCustomerReportData(NonRegUssdCustomerDTO nonRegUssdCustomerDTO, Integer pageNumber) {			
		Page page = transactionDao.NonRegUssdCustomerReportData( nonRegUssdCustomerDTO, pageNumber);
		return page;
	}

	@Override
	public Page transactionVolumeReportData(TransactionVolumeDTO transactionVolumeDTO, Integer pageNumber) {
		Page page = transactionDao.transactionVolumeReportData( transactionVolumeDTO, pageNumber);
		return page;
	}
	
	
}
