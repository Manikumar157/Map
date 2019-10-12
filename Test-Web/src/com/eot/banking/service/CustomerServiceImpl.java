package com.eot.banking.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.hibernate.Hibernate;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cbs.entity.CBSAccount;
import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.CoreUrls;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.common.FieldExecutiveEnum;
import com.eot.banking.common.KycStatusEnum;
import com.eot.banking.common.UrlConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.TransactionRulesDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.BankAccountDTO;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.CustomerProfileDTO;
import com.eot.banking.dto.QRCodeDTO;
import com.eot.banking.dto.QRModel;
import com.eot.banking.dto.SCSubscriptionDTO;
import com.eot.banking.dto.SmsResponseDTO;
import com.eot.banking.dto.SmsSubscriptionDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.HashUtil;
import com.eot.banking.utils.ImageUtil;
import com.eot.banking.utils.JSONAdaptor;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.QRCodeUtil;
import com.eot.coreclient.EOTCoreException;
import com.eot.coreclient.webservice.UtilityServicesCleintSub;
import com.eot.dtos.common.Fault;
import com.eot.dtos.common.Header;
import com.eot.dtos.sms.AppLinkAlertDTO;
import com.eot.dtos.sms.BlockedAppDTO;
import com.eot.dtos.sms.InitialTxnPinLoginPinAlertDTO;
import com.eot.dtos.sms.KYCApproveAlertDTO;
import com.eot.dtos.sms.ResetLoginPinAlertDTO;
import com.eot.dtos.sms.ResetTxnPinAlertDTO;
import com.eot.dtos.sms.SmsHeader;
import com.eot.dtos.sms.WebOTPAlertDTO;
import com.eot.dtos.utilities.ServiceChargeDebitDTO;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.City;
import com.eot.entity.CommisionSplits;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerBankAccount;
import com.eot.entity.CustomerCard;
import com.eot.entity.CustomerDocument;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.CustomerScsubscription;
import com.eot.entity.CustomerSecurityAnswer;
import com.eot.entity.CustomerSmsRuleDetail;
import com.eot.entity.Otp;
import com.eot.entity.PendingTransaction;
import com.eot.entity.Quarter;
import com.eot.entity.SecurityQuestion;
import com.eot.entity.ServiceChargeRule;
import com.eot.entity.ServiceChargeSubscription;
import com.eot.entity.SmsAlertRule;
import com.eot.entity.SmsAlertRuleValue;
import com.eot.entity.SmsLog;
import com.eot.entity.TransactionType;
import com.eot.entity.WebRequest;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;
import com.eot.smsclient.SmsServiceException;
import com.eot.smsclient.webservice.SmsServiceClientStub;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.logica.smpp.Data;
import com.logica.smpp.Session;
import com.logica.smpp.TCPIPConnection;
import com.logica.smpp.pdu.Address;
import com.logica.smpp.pdu.BindRequest;
import com.logica.smpp.pdu.BindResponse;
import com.logica.smpp.pdu.BindTransmitter;
import com.logica.smpp.pdu.WrongLengthOfStringException;
import com.thinkways.kms.KMS;
import com.thinkways.kms.security.KMSSecurityException;
import com.thinkways.util.HexString;

@Service("customerService")
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private LocationDao locationDao;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private BusinessPartnerDao businessPartnerDao;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BankDao bankDao;
//	@Autowired
//	private SmsServiceClientStub smsServiceClientStub;
	@Autowired
	private UtilityServicesCleintSub utilityServicesCleintSub;
	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private AppConfigurations appConfigurations;
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private TransactionRulesDao transactionRulesDao;
	

	private KMS kmsHandle;

	public void setKmsHandle(KMS kmsHandle) {

		this.kmsHandle = kmsHandle;
	}

	public void setUtilityServicesCleintSub(UtilityServicesCleintSub utilityServicesCleintSub) {
		this.utilityServicesCleintSub = utilityServicesCleintSub;
	}

	@Override
	public Map<String, Object> getMasterData(String language) throws EOTException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("countryList", operatorDao.getCountries(language));
		model.put("bankGroupList", bankDao.getAllBankGroups());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.put("transTypeList", transactionRulesDao.loadFilteredTransactionTypesWithServiceCharge(language));

		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = webUserDao.getbankGroupAdmin(webUser.getUserName());

			model.put("bankList", bankGroupAdmin.getBankGroup().getBank());
		} else {
			model.put("bankList", bankDao.getAllBanksByName());
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE) {
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			model.put("branchList", webUserService.getAllBranchFromBank(teller.getBank().getBankId()));
			model.put("customerProfileList", customerDao.getCustomerProfilesByBankId(teller.getBank().getBankId()));
		}

		return model;
	}

	@Override
	public Map<String, Object> getMasterDataByPartner(String language, String type) throws EOTException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("countryList", operatorDao.getCountries(language));
		model.put("bankGroupList", bankDao.getAllBankGroups());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.put("transTypeList", transactionRulesDao.loadFilteredTransactionTypesWithServiceCharge(language));

		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(auth.getName());
		Bank bank = businessPartnerUser.getBusinessPartner().getBank();
		model.put("branchList", webUserService.getAllBranchFromBank(bank.getBankId()));
		model.put("customerProfileList", customerDao.getCustomerProfilesByType(bank.getBankId(), type));

		// model.put("customerProfileList",
		// customerDao.getCustomerProfilesByBankId(bankDao.getBank(EOTConstants.DEFAULT_BANK).getBankId()));
		return model;
	}

	@Override
	public Customer getCustomerByMobileNumber(String mobileNumber) throws EOTException {
		return customerDao.getCustomerByMobileNumber(mobileNumber);
	}

	@Override
	public CustomerAccount findCustomerAccount(Long customerId, String userName) throws EOTException {

		BankTellers teller = bankDao.getTellerByUsername(userName);
		return customerDao.findCustomerAccount(customerId, teller.getBank().getBankId());

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public Long saveCustomer(CustomerDTO customerDTO) throws Exception {

		if (customerDTO.getSignature().getName() == "" && customerDTO.getSignature().getSize() == 0) {
			throw new EOTException(ErrorConstants.INVALID_SIGNATURE_SIZE);
		}

		if (customerDTO.getIdProof().getName() == "" && customerDTO.getIdProof().getSize() <= 0) {
			throw new EOTException(ErrorConstants.INVALID_IDPROOF_SIZE);
		}

		// changing country to default country SouthSudan as discussed with sanjeeb. on
		// 09-11-2018 by vineeth
		// Country country=locationDao.getCountry(customerDTO.getCountryId());
		Country country = locationDao.getCountry(EOTConstants.DEFAULT_COUNTRY);
		String mobileWithISD = country.getIsdCode().toString().concat(customerDTO.getMobileNumber());

		Customer cust = customerDao.getCustomerByMobileNumber(country.getIsdCode() + customerDTO.getMobileNumber());
		if (cust != null) {
			throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
		}
		if (!customerDTO.getEmailAddress().isEmpty()) {
			cust = customerDao.getCustomerByEmailAddress(country.getIsdCode() + customerDTO.getEmailAddress());
			if (cust != null) {
				throw new EOTException(ErrorConstants.EMAIL_ALREADY_EXISTS);
			}
		}
		if (customerDTO.getType() == EOTConstants.REFERENCE_TYPE_CUSTOMER) {
			validateOtp(customerDTO, mobileWithISD);
		}
		cust = new Customer();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		Bank bank = null;
		if (roleId == 24 || roleId == 25 || roleId == 26) {
			BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(auth.getName());
			bank = businessPartnerUser.getBusinessPartner().getBank();
		}
		Integer loginPin = EOTUtil.generateLoginPin();
		Integer txnPin = EOTUtil.generateTransactionPin();
		String appID = EOTUtil.generateAppID();
		String uuid = EOTUtil.generateUUID();

		AppMaster app = new AppMaster();
		app.setAppId(appID);
		app.setReferenceId(cust.getCustomerId() + "");
		app.setReferenceType(customerDTO.getType());
		app.setStatus(EOTConstants.APP_STATUS_NEW);
		app.setUuid(uuid);
		app.setAppVersion("1.0");

		Calendar cal = Calendar.getInstance();
		app.setCreatedDate(cal.getTime());
		Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);
		app.setExpiryDate(cal.getTime());

		customerDao.save(app);

		City city = customerDao.getCity(customerDTO.getCityId());// new City();
		Quarter quarter = customerDao.getQuarter(customerDTO.getQuarterId());// new Quarter();

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		BusinessPartner businessPartner = businessPartnerDao.getBusinessPartner(userName);

		String customerMobileNumber=customerDTO.getMobileNumber();
		if(customerMobileNumber.startsWith("0"))
			customerMobileNumber=customerMobileNumber.substring(1);
		
		cust.setBusinessPartner(businessPartner);
		cust.setMobileNumber(customerMobileNumber);
		cust.setFirstName(customerDTO.getFirstName());
		cust.setMiddleName(customerDTO.getMiddleName());
		cust.setLastName(customerDTO.getLastName());
		cust.setDob(customerDTO.getDob());
		cust.setLoginAttempts(0);
		cust.setBusinessName(StringUtils.isNotBlank(customerDTO.getBusinessName())?customerDTO.getBusinessName():null);
		if(customerDTO.getType().equals(EOTConstants.REF_TYPE_CUSTOMER)) {
			if(customerDTO.getIdProof().getSize() != 0 && customerDTO.getProfilePhoto().getSize() != 0) {
				cust.setKycStatus(EOTConstants.KYC_STATUS_APPROVE_PENDING);
			}else
				cust.setKycStatus(EOTConstants.KYC_STATUS_PENDING);
		}else
			cust.setKycStatus(EOTConstants.KYC_STATUS_PENDING);

		CustomerProfiles custProfile = new CustomerProfiles();
		custProfile.setProfileId(customerDTO.getCustomerProfileId());
		cust.setCustomerProfiles(custProfile);
		if (customerDTO.getProfession().equalsIgnoreCase("Others") && customerDTO.getOthers() != "") {
			cust.setProfession(customerDTO.getOthers());
		} else if (customerDTO.getProfession().length() > 0 && customerDTO.getProfession() != "") {
			cust.setProfession(customerDTO.getProfession());
		}

		cust.setAddress(customerDTO.getAddress());
		cust.setCommission(customerDTO.getCommission());
		cust.setCountry(country);
		city.setCityId(customerDTO.getCity());
		cust.setCity(city);
		quarter.setQuarterId(customerDTO.getQuarter());
		cust.setQuarter(quarter);
		cust.setEmailAddress(customerDTO.getEmailAddress());
		cust.setType(customerDTO.getType());
		if (customerDTO.getType() != null && customerDTO.getType().equals(1)) {
			String agentCode = customerDao.getAgentCode(customerDTO.getType());
			Integer numericCode = 0;
			if (customerDTO.getType().equals(1)) {
				numericCode = agentCode.length() >= 6 ? Integer.parseInt(agentCode.substring(1)) + 100000 : Integer.parseInt(agentCode) + 100000;
			} else if (customerDTO.getType().equals(2)) {
				numericCode = agentCode.length() >= 6 ? Integer.parseInt(agentCode.substring(1)) + 700000 : Integer.parseInt(agentCode) + 700000;
			}
			agentCode = numericCode.toString();
			agentCode = ISOUtil.zeropad(agentCode, 6);
			cust.setAgentCode(agentCode);
		}else if(customerDTO.getType() != null && customerDTO.getType().equals(2)) {
			Boolean flag = customerDao.getMerchant(customerDTO.getType(),customerDTO.getAgentCode());
			if (flag) {
				throw new EOTException(ErrorConstants.MERCHANT_CODE_REGISTERED_ALREADY);
			}
			cust.setAgentCode(customerDTO.getAgentCode());			
		}
		
		cust.setActive(EOTConstants.CUSTOMER_STATUS_NEW);

		cust.setTitle(customerDTO.getTitle());
		cust.setGender(customerDTO.getGender());
		cust.setOnbordedBy(auth.getName());
		cust.setPlaceOfBirth(customerDTO.getPlaceOfBirth());
		cust.setDefaultLanguage(customerDTO.getLanguage());

		cust.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		//cust.setTransactionPin(HashUtil.generateHash(txnPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		cust.setTransactionPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

		cust.setAppId(appID);

		cust.setCreatedDate(new Date());
		cust.setBankCustomerId(customerDTO.getBankCustomerId());
		customerDao.save(cust);
		app.setReferenceId(cust.getCustomerId().toString());
		customerDao.update(app);

		CustomerDocument customerDocument = new CustomerDocument();

		customerDocument.setSignaturePhoto(Hibernate.createBlob(!customerDTO.getNewSignature().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewSignature()):"".getBytes()));
		customerDocument.setIdproofPhoto(Hibernate.createBlob(!customerDTO.getNewIDproof().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewIDproof()):"".getBytes()));
		customerDocument.setProfilePhoto(Hibernate.createBlob(!customerDTO.getNewProfile().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewProfile()):"".getBytes()));
		customerDocument.setAddressProof(Hibernate.createBlob(!customerDTO.getNewAddressProof().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewAddressProof()):"".getBytes()));
		
		customerDocument.setCustomerId(cust.getCustomerId());
		customerDocument.setCustomer(cust);
		customerDocument.setIssueDate(customerDTO.getIssueDate());
		customerDocument.setPlaceOfIssue(customerDTO.getPlaceOfIssue());
		customerDocument.setExpiryDate(customerDTO.getExpiryDate());
		customerDocument.setIdNumber(customerDTO.getIdNumber());

		if (customerDTO.getIdType().equalsIgnoreCase("Others") && customerDTO.getIDTypeothers() != "") {
			customerDocument.setIdType(customerDTO.getOthers());
		} else if (customerDTO.getIdType().length() > 0 && customerDTO.getIdType() != "") {
			customerDocument.setIdType(customerDTO.getIdType());
		}

		customerDao.save(customerDocument);

		/*
		 * CustomerSecurityAnswer answer = new CustomerSecurityAnswer();
		 * answer.setAnswer(customerDTO.getAnswer()); answer.setCustomer(cust);
		 * SecurityQuestion securityQuestion = new SecurityQuestion();
		 * securityQuestion.setQuestionId(customerDTO.getQuestion());
		 * answer.setSecurityQuestion(securityQuestion);
		 * 
		 * customerDao.save(answer);
		 */

		// Integer roleId = webUserService.getRoleId(auth.getName());

		String alias = "";
		String commissionAccountAlias="";

		if (roleId != EOTConstants.ROLEID_BUSINESS_PARTNER_L1 && roleId != EOTConstants.ROLEID_BUSINESS_PARTNER_L2 && roleId != EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
			BankTellers teller = bankDao.getTellerByUsername(auth.getName());
			if (teller.getBank().getStatus() == EOTConstants.INACTIVE_BANK)
				throw new EOTException(ErrorConstants.INACTIVE_BANK);
			if(customerDTO.getType().equals(EOTConstants.REF_TYPE_CUSTOMER)) {
				alias = EOTConstants.ACCOUNT_ALIAS_MOB_CUSTOMER + " - " + teller.getBank().getBankName();
			}else{
				alias = EOTConstants.ACCOUNT_ALIAS_CUSTOMER + " - " + teller.getBank().getBankName();
			}
			commissionAccountAlias=EOTConstants.ACCOUNT_ALIAS_COMMISSION+ " - " + teller.getBank().getBankName();
		} else {
			/*
			 * BusinessPartnerUser businessPartnerUser =
			 * businessPartnerDao.getUser(auth.getName()); Bank bank =
			 * businessPartnerUser.getBusinessPartner().getBank();
			 */
			if (bank.getStatus() == EOTConstants.INACTIVE_BANK)
				throw new EOTException(ErrorConstants.INACTIVE_BANK);
			alias = EOTConstants.ACCOUNT_ALIAS_CUSTOMER + " - " + bank.getBankName();
			//alias = EOTConstants.ACCOUNT_ALIAS_MOB_CUSTOMER + " - " + bank.getBankName();
			commissionAccountAlias = EOTConstants.ACCOUNT_ALIAS_COMMISSION + " - " + bank.getBankName();
			// Bank bank = bankDao.getBank(EOTConstants.DEFAULT_BANK);
		}
		
		long accountSeq=bankDao.getNextAccountNumberSequence();
		Account account = new Account();

		account.setAccountNumber(EOTUtil.generateAccountNumber(accountSeq));
		account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);

		account.setAliasType(EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT);
		account.setAlias(alias);
		account.setCurrentBalance(0.0);
		account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		account.setReferenceId(cust.getCustomerId().toString());
		account.setReferenceType(customerDTO.getType());

		customerDao.save(account);
		
		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAccount(account);
		customerAccount.setAccountNumber(account.getAccountNumber());
		customerAccount.setCustomer(cust);
		
		
		

		

		// teller = bankDao.getTellerByUsername(auth.getName());

		if (roleId != 24 && roleId != 25 && roleId != 26) {
			BankTellers teller = bankDao.getTellerByUsername(auth.getName());
			customerAccount.setBank(teller.getBranch().getBank());
			customerAccount.setBranch(teller.getBranch());
		} else {
			customerAccount.setBank(bank);
			List<Branch> branches = new ArrayList<Branch>(bank.getBranches());
			customerAccount.setBranch(branches.get(0));
		}
		customerDao.save(customerAccount);

		// bellow code is for commissio account only for agent
		if (customerDTO.getType() != null && customerDTO.getType().intValue() == EOTConstants.REFERENCE_TYPE_AGENT) {
			Account agentCommissionAccount = new Account();

			agentCommissionAccount.setAccountNumber(EOTUtil.generateAccountNumber(accountSeq + 1));
			agentCommissionAccount.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
			agentCommissionAccount.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
			agentCommissionAccount.setAliasType(EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);
			agentCommissionAccount.setAlias(commissionAccountAlias);
			agentCommissionAccount.setCurrentBalance(0.0);
			agentCommissionAccount.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
			agentCommissionAccount.setReferenceId(cust.getCustomerId().toString());
			agentCommissionAccount.setReferenceType(customerDTO.getType());

			customerDao.save(agentCommissionAccount);

			CustomerAccount commissionAccount = new CustomerAccount();
			commissionAccount.setAccount(agentCommissionAccount);
			commissionAccount.setAccountNumber(agentCommissionAccount.getAccountNumber());
			commissionAccount.setCustomer(cust);
			commissionAccount.setBank(bank);
			List<Branch> branches = new ArrayList<Branch>(bank.getBranches());
			commissionAccount.setBranch(branches.get(0));
			customerDao.save(commissionAccount);
		}

		/*
		 * if(customerDTO.getType().equals(EOTConstants.REFERENCE_TYPE_MERCHANT)){
		 * 
		 * WebUserDTO webUserDTO = new WebUserDTO();
		 * webUserDTO.setFirstName(customerDTO.getFirstName());
		 * webUserDTO.setMiddleName(customerDTO.getMiddleName());
		 * webUserDTO.setLastName(customerDTO.getLastName());
		 * webUserDTO.setMobileNumber(customerDTO.getMobileNumber());
		 * webUserDTO.setLanguage(customerDTO.getLanguage()); webUserDTO.setBankId(1);
		 * webUserDTO.setBranchId(1L); webUserDTO.setCountryId(1);
		 * webUserDTO.setEmail(customerDTO.getEmailAddress());
		 * createMerchantWebLogin(webUserDTO);
		 * 
		 * }
		 */
		
		  AppLinkAlertDTO appAlertDto = new AppLinkAlertDTO();
		  String appType = cust.getType().equals(EOTConstants.REFERENCE_TYPE_CUSTOMER) ? EOTConstants.APP_TYPE_CUSTOMER
							: cust.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT) ? EOTConstants.APP_TYPE_AGENT
							: cust.getType().equals(EOTConstants.REFERENCE_TYPE_MERCHANT) ? EOTConstants.APP_TYPE_MERCHANT:"";
		  appAlertDto.setDownloadLink(appConfigurations.getAppDownloadURL()+appType);
		  appAlertDto.setLocale(cust.getDefaultLanguage());
		  appAlertDto.setMobileNo(country.getIsdCode()+cust.getMobileNumber());
		  appAlertDto.setScheduleDate(Calendar.getInstance());
		  appAlertDto.setApplicationName("m-GURUSH"); 
//		  smsServiceClientStub.appLinkAlert(appAlertDto);
		  sendSMS(UrlConstants.APP_LINGK_ALERT,appAlertDto);

		// subscribeCustomerForUSSD(country.getIsdCode()+cust.getMobileNumber(),cust.getDefaultLanguage().split("_")[0].toUpperCase(),(cust.getType()==0?1:cust.getType()));
		  InitialTxnPinLoginPinAlertDTO pinDto = new InitialTxnPinLoginPinAlertDTO();
		  pinDto.setLocale(cust.getDefaultLanguage());
		  pinDto.setLoginPIN(loginPin.toString());
		  pinDto.setMobileNo(country.getIsdCode()+cust.getMobileNumber());
		  pinDto.setTxnPIN(loginPin.toString());
		  pinDto.setScheduleDate(Calendar.getInstance());
		  
//		  smsServiceClientStub.initialTxnPinLoginPinAlert(pinDto);
		  sendSMS(UrlConstants.INITIAL_LOGIN_AND_TXN_PIN_ALERT,pinDto);
		return cust.getCustomerId();

	}

	private void validateOtp(CustomerDTO customerDTO, String mobileWithISD) throws NoSuchAlgorithmException, UnsupportedEncodingException, EOTException {
		com.eot.banking.dto.OtpDTO otpDto = new com.eot.banking.dto.OtpDTO();

		otpDto.setOtphash(HashUtil.generateHash(customerDTO.getOtp().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		otpDto.setReferenceId(mobileWithISD);
		otpDto.setReferenceType(EOTConstants.REF_TYPE_CUSTOMER);
		otpDto.setOtpType(EOTConstants.OTP_TYPE_CUSTOMER);
		Otp otp = customerDao.verifyOTP(otpDto);
		if (otp == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER_OTP);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void createCustomerAccount(String mobileNumber) throws Exception {

		Customer cust = customerDao.getCustomerByMobileNumber(mobileNumber);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		BankTellers teller = bankDao.getTellerByUsername(auth.getName());

		Account account = new Account();

		account.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
		account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		//String alias = EOTConstants.ACCOUNT_ALIAS_CUSTOMER + " - " + teller.getBank().getBankName();
		String alias = EOTConstants.ACCOUNT_ALIAS_MOB_CUSTOMER + " - " + teller.getBank().getBankName();
		account.setAlias(alias);
		account.setCurrentBalance(0.0);
		account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		account.setReferenceId(cust.getCustomerId().toString());
		account.setReferenceType(cust.getType());

		customerDao.save(account);

		CustomerAccount customerAccount = new CustomerAccount();
		customerAccount.setAccount(account);
		customerAccount.setAccountNumber(account.getAccountNumber());
		customerAccount.setCustomer(cust);

		customerAccount.setBank(teller.getBranch().getBank());
		customerAccount.setBranch(teller.getBranch());

		customerDao.save(customerAccount);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void updateCustomer(CustomerDTO customerDTO) throws Exception {

		Customer customer = customerDao.getCustomer(customerDTO.getCustomerId());

		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		// changing country to default country SouthSudan as discussed with sanjeeb. on
		// 09-11-2018 by vineeth
		// Country country = locationDao.getCountry(customerDTO.getCountryId());
		Country country = locationDao.getCountry(EOTConstants.DEFAULT_COUNTRY);

		if (!customer.getMobileNumber().equals(customerDTO.getMobileNumber())) {
			Customer cust = customerDao.getCustomerByMobileNumber(country.getIsdCode() + customerDTO.getMobileNumber());
			if (cust != null) {
				throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
			}
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		Bank bank = null;
		if (roleId == 24 || roleId == 25 || roleId == 26) {
			BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(auth.getName());
			bank = businessPartnerUser.getBusinessPartner().getBank();
		}

		if (roleId != 24 && roleId != 25 && roleId != 26) {
			BankTellers teller = bankDao.getTellerByUsername(auth.getName());
			if (teller.getBank().getStatus() == EOTConstants.INACTIVE_BANK)
				throw new EOTException(ErrorConstants.INACTIVE_BANK);
		} else {

			if (bank.getStatus() == EOTConstants.INACTIVE_BANK)
				throw new EOTException(ErrorConstants.INACTIVE_BANK);
			// Bank bank = bankDao.getBank(EOTConstants.DEFAULT_BANK);
		}

		/*
		 * if(!customer.getMobileNumber().equals(customerDTO.getMobileNumber())){
		 * unSubscribeCustomerForUSSD(customer.getCountry().getIsdCode()+customer.
		 * getMobileNumber());
		 * unSubcribeFromLiveProfile(customer.getCountry().getIsdCode()+customer.
		 * getMobileNumber()); }
		 */

		customer.setCustomerId(customerDTO.getCustomerId());
		customer.setTitle(customerDTO.getTitle());
		customer.setFirstName(customerDTO.getFirstName());
		customer.setMiddleName(customerDTO.getMiddleName());
		customer.setLastName(customerDTO.getLastName());
		customer.setMobileNumber(customerDTO.getMobileNumber());
		customer.setPlaceOfBirth(customerDTO.getPlaceOfBirth());
		customer.setDob(customerDTO.getDob());
		CustomerProfiles custProfile = new CustomerProfiles();
		custProfile.setProfileId(customerDTO.getCustomerProfileId());
		customer.setCustomerProfiles(custProfile);
		customer.setGender(customerDTO.getGender());
		customer.setDefaultLanguage(customerDTO.getLanguage());
		customer.setType(customerDTO.getType());
		customer.setEmailAddress(customerDTO.getEmailAddress());
		customer.setAddress(customerDTO.getAddress());
		customer.setBusinessName(StringUtils.isNotBlank(customerDTO.getBusinessName())?customerDTO.getBusinessName():null);
		customer.setCommission(customerDTO.getCommission());
		customer.setCountry(country);
		City city = customerDao.getCity(customerDTO.getCityId());// new City();

		city.setCityId(customerDTO.getCity());
		customer.setCity(city);
		Quarter quarter = customerDao.getQuarter(customerDTO.getQuarterId());// new Quarter();
		quarter.setQuarterId(customerDTO.getQuarter());
		customer.setQuarter(quarter);
		customer.setBankCustomerId(customerDTO.getBankCustomerId());

		if (customerDTO.getProfession().equalsIgnoreCase("Others") && customerDTO.getOthers() != "") {
			customer.setProfession(customerDTO.getOthers());
		} else if (customerDTO.getProfession().length() > 0 && customerDTO.getProfession() != "") {
			customer.setProfession(customerDTO.getProfession());
		}
		

		/*
		 * CustomerSecurityAnswer securityAnswer= customer.getCustomerSecurityAnswer();
		 * if(securityAnswer != null) {
		 * securityAnswer.setAnswer(customerDTO.getAnswer());
		 * securityAnswer.setCustomer(customer); SecurityQuestion securityQuestion = new
		 * SecurityQuestion();
		 * securityQuestion.setQuestionId(customerDTO.getQuestion());
		 * securityAnswer.setSecurityQuestion(securityQuestion);
		 * 
		 * customerDao.update(securityAnswer);
		 * 
		 * }
		 */

		CustomerDocument customerDocument = customer.getCustomerDocument();

		if (customerDocument == null) {
			customerDocument = new CustomerDocument();
			customerDocument.setCustomerId(customer.getCustomerId());
			customerDocument.setCustomer(customer);

		}

		customerDocument.setIssueDate(customerDTO.getIssueDate());
		customerDocument.setPlaceOfIssue(customerDTO.getPlaceOfIssue());
		customerDocument.setExpiryDate(customerDTO.getExpiryDate());
		customerDocument.setIdNumber(customerDTO.getIdNumber());

		if (customerDTO.getIdType().equalsIgnoreCase("Others") && customerDTO.getIDTypeothers() != "") {
			customerDocument.setIdType(customerDTO.getIDTypeothers());
		} else if (customerDTO.getIdType().length() > 0 && customerDTO.getIdType() != "") {
			customerDocument.setIdType(customerDTO.getIdType());
		}
		
//		customerDocument.setSignaturePhoto(Hibernate.createBlob(null==customerDTO.getNewSignature()?"".getBytes():!customerDTO.getNewSignature().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewSignature()):"".getBytes()));
//		customerDocument.setIdproofPhoto(Hibernate.createBlob(null==customerDTO.getNewIDproof()?"".getBytes():!customerDTO.getNewIDproof().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewIDproof()):"".getBytes()));
//		customerDocument.setProfilePhoto(Hibernate.createBlob(null==customerDTO.getNewProfile()?"".getBytes():!customerDTO.getNewProfile().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewProfile()):"".getBytes()));
//		customerDocument.setAddressProof(Hibernate.createBlob(null==customerDTO.getNewAddressProof()?"".getBytes():!customerDTO.getNewAddressProof().isEmpty()?ImageUtil.stringtoBase64(customerDTO.getNewAddressProof()):"".getBytes()));
//		
		if(StringUtils.isNotBlank(customerDTO.getNewSignature()))
			customerDocument.setSignaturePhoto(Hibernate.createBlob(ImageUtil.stringtoBase64(customerDTO.getNewSignature())));
		if(StringUtils.isNotBlank(customerDTO.getNewIDproof()))
			customerDocument.setIdproofPhoto(Hibernate.createBlob(ImageUtil.stringtoBase64(customerDTO.getNewIDproof())));
		if(StringUtils.isNotBlank(customerDTO.getNewProfile()))
			customerDocument.setProfilePhoto(Hibernate.createBlob(ImageUtil.stringtoBase64(customerDTO.getNewProfile())));
		if(StringUtils.isNotBlank(customerDTO.getNewAddressProof()))
			customerDocument.setAddressProof(Hibernate.createBlob(ImageUtil.stringtoBase64(customerDTO.getNewAddressProof())));

		if(customer.getType().equals(EOTConstants.REF_TYPE_CUSTOMER)) {
			if(customer.getKycStatus().equals(EOTConstants.KYC_STATUS_PENDING) && customerDocument.getIdproofPhoto().length() != 0 && customerDocument.getProfilePhoto().length() != 0) {
				customer.setKycStatus(EOTConstants.KYC_STATUS_APPROVE_PENDING);
			}else if(customer.getKycStatus().equals(EOTConstants.KYC_STATUS_REGEJETED) && StringUtils.isNotBlank(customerDTO.getNewIDproof()) && StringUtils.isNotBlank(customerDTO.getNewProfile())){
				customer.setKycStatus(EOTConstants.KYC_STATUS_APPROVE_PENDING);
			}
		}
		customerDao.update(customer);
		customerDao.flush();
		customerDao.saveOrUpdate(customerDocument);

		AppMaster app = customerDao.getUpdateAppType(customer.getCustomerId());
		app.setReferenceType(customerDTO.getType());
		customerDao.update(app);

		// subscribeCustomerForUSSD(country.getIsdCode()+customer.getMobileNumber(),customer.getDefaultLanguage().split("_")[0].toUpperCase(),(customer.getType()==0?1:customer.getType()));

	}

	@Override
	public byte[] getPhotoDetails(Long customerId, String type) throws EOTException {

		Customer customer = customerDao.getCustomer(customerId);

		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		} else {
			byte[] customerSignature = null;
			byte[] customerIdProof = null;
			byte[] profilePhoto = null;
			byte[] addressProof = null;

			try {
				if (customer.getCustomerDocument() != null) {
					if (null != customer.getCustomerDocument().getSignaturePhoto())
						customerSignature = customer.getCustomerDocument().getSignaturePhoto().getBytes(1, (int) customer.getCustomerDocument().getSignaturePhoto().length());
					if (null != customer.getCustomerDocument().getIdproofPhoto())
						customerIdProof = customer.getCustomerDocument().getIdproofPhoto().getBytes(1, (int) customer.getCustomerDocument().getIdproofPhoto().length());
					if (null != customer.getCustomerDocument().getAddressProof())
						addressProof = customer.getCustomerDocument().getAddressProof().getBytes(1, (int) customer.getCustomerDocument().getAddressProof().length());
					if (null != customer.getCustomerDocument().getProfilePhoto())
						profilePhoto = customer.getCustomerDocument().getProfilePhoto().getBytes(1, (int) customer.getCustomerDocument().getProfilePhoto().length());
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new EOTException(ErrorConstants.SERVICE_ERROR);
			}
			if ("signature".equals(type)) {
				return customerSignature;
			} else if ("idproof".equals(type)) {
				return customerIdProof;
			} else if ("profilePhoto".equals(type)) {
				return profilePhoto;
			} else {
				return addressProof;
			}

			/*
			 * try { customerSignature =
			 * customer.getCustomerDocument().getSignaturePhoto().getBytes(1, (int)
			 * customer.getCustomerDocument().getSignaturePhoto().length());
			 * 
			 * customerIdProof =
			 * customer.getCustomerDocument().getIdproofPhoto().getBytes(1, (int)
			 * customer.getCustomerDocument().getIdproofPhoto().length());
			 * 
			 * profilePhoto = customer.getCustomerDocument().getIdproofPhoto().getBytes(1,
			 * (int) customer.getCustomerDocument().getIdproofPhoto().length());
			 * addressProof = customer.getCustomerDocument().getIdproofPhoto().getBytes(1,
			 * (int) customer.getCustomerDocument().getIdproofPhoto().length());
			 * 
			 * } catch (SQLException e) { e.printStackTrace(); throw new
			 * EOTException(ErrorConstants.SERVICE_ERROR); } return "signature".equals(type)
			 * ? customerSignature : customerIdProof;
			 */
		}
	}

	@SuppressWarnings("unused")
	@Override
	public CustomerDTO getCustomerDetails(Long customerId, String userName) throws EOTException {

		Customer cust = customerDao.getCustomer(customerId);
		System.out.println(cust.getCity().getCity());
		if (cust == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		CustomerDTO cdto = new CustomerDTO();

		cdto.setCustomerId(cust.getCustomerId());
		cdto.setTitle(cust.getTitle());
		cdto.setFirstName(cust.getFirstName());
		cdto.setMiddleName(cust.getMiddleName());
		cdto.setLastName(cust.getLastName());
		cdto.setCustomerKycStatus(cust.getKycStatus());
		cdto.setReasonForRejection(cust.getReasonForRejection());

		cdto.setIsdCode(cust.getCountry().getIsdCode());
		cdto.setMobileNumber(cust.getMobileNumber());
		cdto.setGender(cust.getGender());
		cdto.setDob(cust.getDob());
		cdto.setCreatedDate(cust.getCreatedDate());
		cdto.setPlaceOfBirth(cust.getPlaceOfBirth());
		cdto.setLanguage(cust.getDefaultLanguage());
		cdto.setType(cust.getType());
		cdto.setEmailAddress(cust.getEmailAddress());
		cdto.setCountryId(cust.getCountry().getCountryId());
		cdto.setCountry(cust.getCountry().getCountry());
		cdto.setCustomerCountry(cust.getCountry());
		cdto.setCityId(cust.getCity().getCityId());
		cdto.setCity(cust.getCity().getCityId());
		cdto.setOnBordedBy(cust.getOnbordedBy());
		cdto.setCityName(cust.getCity().getCity());
		cdto.setAddress(cust.getAddress());
		cdto.setAddress(cust.getAddress());
		cdto.setCommission(cust.getCommission());
		cdto.setBusinessName(cust.getBusinessName());
		cdto.setReasonForBlock(cust.getReasonForBlock());
		cdto.setReasonForDeActivate(cust.getReasonForDeActivate());
		cdto.setAgentCode(cust.getAgentCode());
		if (cust.getQuarter() != null) {
			cdto.setQuarterId(cust.getQuarter().getQuarterId());
			cdto.setQuarter(cust.getQuarter().getQuarterId());
			cdto.setQuarterName(cust.getQuarter().getQuarter());
		}
		cdto.setProfileName(cust.getCustomerProfiles().getProfileName());
		cdto.setCustomerProfileId(cust.getCustomerProfiles().getProfileId());
		cdto.setBankCustomerId(cust.getBankCustomerId());
		cdto.setCustomerStatus(cust.getActive());

		if (cust.getCustomerDocument() != null) {
			cdto.setExpiryDate(cust.getCustomerDocument().getExpiryDate());
			cdto.setIssueDate(cust.getCustomerDocument().getIssueDate());
			cdto.setIdNumber(cust.getCustomerDocument().getIdNumber());
			cdto.setIdType(cust.getCustomerDocument().getIdType());
			cdto.setPlaceOfIssue(cust.getCustomerDocument().getPlaceOfIssue());
			cdto.setExpiryDate(cust.getCustomerDocument().getExpiryDate());
		}
		boolean flag = null != cust.getProfession() ? setProfessionValue(cust.getProfession()) : false;
		if (null != cust.getProfession() && flag == false) {
			cdto.setOthers(cust.getProfession());
			cdto.setProfession("Others");
		}else
			cdto.setProfession(cust.getProfession());
		BankTellers teller = bankDao.getTellerByUsername(userName);

		if (teller != null) {
			Set<CustomerAccount> list = new HashSet<CustomerAccount>();
			list.add(customerDao.findCustomerAccount(customerId, teller.getBank().getBankId()));
			cdto.setAccountList(list);
		} else {
			cdto.setAccountList(cust.getCustomerAccounts());
		}

		CustomerSecurityAnswer securityAnswer = cust.getCustomerSecurityAnswer();
		if (securityAnswer != null) {
			cdto.setAnswer(securityAnswer.getAnswer());
			cdto.setQuestion(securityAnswer.getSecurityQuestion().getQuestionId());
			cdto.setQuestionId(securityAnswer.getSecurityQuestion().getQuestionId());
			cdto.setSelectedQuestion(securityAnswer.getSecurityQuestion().getQuestion());
		}

		AppMaster app = customerDao.getApplication(cust.getCustomerId(), cust.getType());
		if (app != null) {
			cdto.setAppStatus(app.getStatus());
		}

		return cdto;
	}

	@Override
	public List<City> getCityList(Integer countryId) {
		return locationDao.getAllCities(countryId);
	}

	@Override
	public List<Quarter> getQuarterList(Integer cityId) {
		return locationDao.getAllQuarters(cityId);
	}

	public Page searchCustomers(String userName, String bankGroupId, String firstName, String middleName, String lastName, String mobileNumber, String bankId, String branchId, String countryId, String fromDate, String toDate, Integer pageNumber, String custType, String onBoardedBy,String businessName, Integer kycStatus,String channel) throws EOTException {

		Page page = null;
		Integer groupId = null;
		Integer partnerId = null;

		if (bankGroupId != null && bankGroupId != "") {
			groupId = Integer.parseInt(bankGroupId);
		}
		try {

			WebUser webUser = webUserDao.getUser(userName);

			if (webUser == null) {
				throw new EOTException(ErrorConstants.INVALID_USER);
			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {

				BankTellers teller = webUserDao.getBankTeller(userName);

				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_USER);
				}

				page = customerDao.searchCustomer(teller.getBank().getBankId(), null, firstName, middleName, lastName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, pageNumber, custType, null, partnerId,onBoardedBy,businessName,kycStatus, channel);
			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {

				BankTellers teller = webUserDao.getBankTeller(userName);

				if (teller == null) {
					throw new EOTException(ErrorConstants.INVALID_USER);
				}

				page = customerDao.searchCustomer(teller.getBank().getBankId(), null, firstName, middleName, lastName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, pageNumber, custType, teller.getBranch().getBranchId(), partnerId,onBoardedBy,businessName,kycStatus, channel);
				
			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
				BankGroupAdmin bankGroupAdmin = webUserDao.getbankGroupAdmin(webUser.getUserName());

				page = customerDao.searchCustomer(null, bankGroupAdmin.getBankGroup().getBankGroupId(), firstName, middleName, lastName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, pageNumber, custType, null, partnerId,onBoardedBy,businessName,kycStatus, channel);			
			} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
				BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(userName);
				partnerId = businessPartnerUser.getBusinessPartner().getId();
				Bank bank = businessPartnerUser.getBusinessPartner().getBank();
				List<Branch> branches = new ArrayList<Branch>(bank.getBranches());
				// Branch branch = branches.get(0);
				Branch branch = null;
				Long branchId1 = null;
				if (branch != null)
					branchId1 = branch.getBranchId() != null ? branch.getBranchId() : null;
				page = customerDao.searchCustomer(bank.getBankId(), null, firstName, middleName, lastName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, pageNumber, custType, branchId1, partnerId,onBoardedBy,businessName,kycStatus, channel);
				
			} else {
				page = customerDao.searchCustomer(null, groupId, firstName, middleName, lastName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, pageNumber, custType, null, partnerId,onBoardedBy,businessName,kycStatus, channel);
				
			}
			/*
			 * if(page.getResults().size() == 0 || page.getResults().isEmpty() ||
			 * page.getResults().equals("")) { if(custType.equals("0")){ throw new
			 * EOTException(ErrorConstants.INVALID_CUSTOMER); }else
			 * if(custType.equals("1")){ throw new
			 * EOTException(ErrorConstants.INVALID_AGENT); }else if(custType.equals("2")){
			 * throw new EOTException(ErrorConstants.INVALID_SOLEMERCHANT); }else
			 * if(custType.equals("3")){ throw new
			 * EOTException(ErrorConstants.INVALID_AGENT_SOLEMERCHANT); } }
			 */
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.DB_ERROR);
		}
		// Page page= PaginationHelper.getPage(customerList,
		// appConfigurations.getResultsPerPage(), pageNumber);

		return page;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void resetCustomerPin(Long customerId) throws Exception {

		/*
		 * Customer cust=customerDao.getCustomer(customerId);
		 * 
		 * if(cust==null){ throw new EOTException(ErrorConstants.INVALID_CUSTOMER); }
		 * 
		 * AppMaster app = customerDao.getApplication(customerId, cust.getType());
		 * 
		 * if(app == null ){ throw new EOTException(ErrorConstants.INVALID_APPLICATION);
		 * }
		 * 
		 * app.setStatus(EOTConstants.APP_STATUS_RESET_PIN_VERIFIED);
		 * 
		 * customerDao.update(app);
		 */

		Customer cust = customerDao.getCustomer(customerId);

		if (cust == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		AppMaster app = customerDao.getApplication(cust.getCustomerId(), cust.getType());

		if (app == null) {
			throw new EOTException(ErrorConstants.INVALID_APPLICATION);
		}

		/*
		 * app.setStatus(EOTConstants.APP_STATUS_DEACTIVE); customerDao.update(app);
		 */

		Integer loginPin = EOTUtil.generateLoginPin();
		// Integer txnPin = EOTUtil.generateTransactionPin();
		/*
		 * String appID = EOTUtil.generateAppID(); String uuid = EOTUtil.generateUUID();
		 * 
		 * AppMaster newApp = new AppMaster(); newApp.setAppId(appID);
		 * newApp.setReferenceId(cust.getCustomerId()+"");
		 * newApp.setReferenceType(cust.getType());
		 * newApp.setStatus(EOTConstants.APP_STATUS_NEW); newApp.setUuid(uuid);
		 * newApp.setAppVersion("1.0");
		 * 
		 * Calendar cal = Calendar.getInstance(); newApp.setCreatedDate(cal.getTime());
		 * Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		 * cal.set(Calendar.DAY_OF_MONTH, dayOfMonth+1);
		 * newApp.setExpiryDate(cal.getTime());
		 * 
		 * customerDao.save(newApp);
		 * 
		 * cust.setAppId(appID);
		 */
		cust.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		cust.setTransactionPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

		customerDao.update(cust);

		/*
		 * AppLinkAlertDTO appAlertDto = new AppLinkAlertDTO();
		 * appAlertDto.setDownloadLink(appConfigurations.getAppDownloadURL()+uuid);
		 * appAlertDto.setLocale(cust.getDefaultLanguage());
		 * appAlertDto.setMobileNo(cust.getCountry().getIsdCode()+cust.getMobileNumber()
		 * ); appAlertDto.setScheduleDate(Calendar.getInstance());
		 * smsServiceClientStub.appLinkAlert(appAlertDto);
		 */
		/*
		 * InitialTxnPinLoginPinAlertDTO pinDto = new InitialTxnPinLoginPinAlertDTO();
		 * pinDto.setLocale(cust.getDefaultLanguage());
		 * pinDto.setLoginPIN(loginPin.toString());
		 * pinDto.setMobileNo(cust.getCountry().getIsdCode()+cust.getMobileNumber());
		 * //pinDto.setTxnPIN(txnPin.toString());
		 * pinDto.setScheduleDate(Calendar.getInstance());
		 */

		ResetLoginPinAlertDTO resetPinDTO = new ResetLoginPinAlertDTO();
		resetPinDTO.setLocale(cust.getDefaultLanguage());
		resetPinDTO.setLoginPIN(loginPin.toString());
		resetPinDTO.setMobileNo(cust.getCountry().getIsdCode() + cust.getMobileNumber());
		// pinDto.setTxnPIN(txnPin.toString());
		resetPinDTO.setScheduleDate(Calendar.getInstance());
		// smsServiceClientStub.initialTxnPinLoginPinAlert(pinDto);
//		smsServiceClientStub.resetLoginPinAlert(resetPinDTO);
		sendSMS(UrlConstants.RESET_LOGIN_PIN_ALERT,resetPinDTO);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void changeApplicationStatus(Long customerId, Integer status,String reasonForBlock) throws EOTException {

		Customer cust = customerDao.getCustomer(customerId);

		if (cust == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		AppMaster app = customerDao.getApplication(cust.getCustomerId(), cust.getType());

		if (app == null) {
			throw new EOTException(ErrorConstants.INVALID_APPLICATION);
		} else {
			if(null != reasonForBlock && !"".equals(reasonForBlock)) {
				cust.setReasonForBlock(reasonForBlock);
			}
			cust.setLoginAttempts(0);
			customerDao.update(cust);
			app.setStatus(status);
			customerDao.update(app);
			// sms for block need to send hare
			// customer name , mobile number, language
			// smsServiceClientStub.

			BlockedAppDTO dto = new BlockedAppDTO();
			dto.setLocale(cust.getDefaultLanguage());
			dto.setMobileNo(cust.getCountry().getIsdCode() + cust.getMobileNumber());
			dto.setStatus(status);
			
			sendSMS(UrlConstants.BLOCKED_APP_ALERT,dto);
				
			/* try {
			 * smsServiceClientStub.blockedAppAlert(dto);
			 * } catch (SmsServiceException e) {
			 * // TODO Auto-generated catch block
			 * e.printStackTrace();
			 * } */

		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void changeCustomerStatus(Long customerId, Integer status, String reasonForDeActivate) throws EOTException {

		Customer cust = customerDao.getCustomer(customerId);

		if (cust == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		} else {
			if(null != reasonForDeActivate && !"".equals(reasonForDeActivate)) {
				cust.setReasonForDeActivate(reasonForDeActivate);
			}
			cust.setActive(status);
			customerDao.update(cust);

			/*
			 * Commented For Bug no. 6234. As for now below code is of no significance.
			 * Acknowledge by the team if(status == EOTConstants.CUSTOMER_STATUS_ACTIVE){
			 * 
			 * subscribeCustomerForUSSD(cust.getCountry().getIsdCode()+cust.getMobileNumber(
			 * ),cust.getDefaultLanguage().split("_")[0].toUpperCase(),(cust.getType()==0?1:
			 * cust.getType()));
			 * 
			 * }
			 * 
			 * if(status == EOTConstants.CUSTOMER_STATUS_DEACTIVATED){
			 * 
			 * unSubscribeCustomerForUSSD(cust.getCountry().getIsdCode()+cust.
			 * getMobileNumber());
			 * unSubcribeFromLiveProfile(cust.getCountry().getIsdCode()+cust.getMobileNumber
			 * ());
			 * 
			 * }
			 */
		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void reinstallApplication(Long customerId) throws Exception {

		Customer cust = customerDao.getCustomer(customerId);

		if (cust == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		AppMaster app = customerDao.getApplication(cust.getCustomerId(), cust.getType());

		if (app == null) {
			throw new EOTException(ErrorConstants.INVALID_APPLICATION);
		}

		app.setStatus(EOTConstants.APP_STATUS_DEACTIVE);
		customerDao.update(app);

		Integer loginPin = EOTUtil.generateLoginPin();
		//Integer txnPin = EOTUtil.generateTransactionPin();
		String appID = EOTUtil.generateAppID();
		String uuid = EOTUtil.generateUUID();

		AppMaster newApp = new AppMaster();
		newApp.setAppId(appID);
		newApp.setReferenceId(cust.getCustomerId() + "");
		newApp.setReferenceType(cust.getType());
		newApp.setStatus(EOTConstants.APP_STATUS_NEW);
		newApp.setUuid(uuid);
		newApp.setAppVersion("1.0");

		Calendar cal = Calendar.getInstance();
		newApp.setCreatedDate(cal.getTime());
		Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);
		newApp.setExpiryDate(cal.getTime());

		customerDao.save(newApp);

		cust.setAppId(appID);
		cust.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		cust.setTransactionPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

		customerDao.update(cust);

		AppLinkAlertDTO appAlertDto = new AppLinkAlertDTO();
		String appType = cust.getType().equals(EOTConstants.REFERENCE_TYPE_CUSTOMER) ? EOTConstants.APP_TYPE_CUSTOMER
				: cust.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT) ? EOTConstants.APP_TYPE_AGENT
				: cust.getType().equals(EOTConstants.REFERENCE_TYPE_MERCHANT) ? EOTConstants.APP_TYPE_MERCHANT:"";
		appAlertDto.setApplicationName("m-GURUSH");
		appAlertDto.setDownloadLink(appConfigurations.getAppDownloadURL() + appType);
		appAlertDto.setLocale(cust.getDefaultLanguage());
		appAlertDto.setMobileNo(cust.getCountry().getIsdCode() + cust.getMobileNumber());
		appAlertDto.setScheduleDate(Calendar.getInstance());
//		smsServiceClientStub.appLinkAlert(appAlertDto);
		sendSMS(UrlConstants.APP_LINGK_ALERT,appAlertDto);

		InitialTxnPinLoginPinAlertDTO pinDto = new InitialTxnPinLoginPinAlertDTO();
		pinDto.setLocale(cust.getDefaultLanguage());
		pinDto.setLoginPIN(loginPin.toString());
		pinDto.setMobileNo(cust.getCountry().getIsdCode() + cust.getMobileNumber());
		pinDto.setTxnPIN(loginPin.toString());
		pinDto.setScheduleDate(Calendar.getInstance());
//		smsServiceClientStub.initialTxnPinLoginPinAlert(pinDto);
		sendSMS(UrlConstants.INITIAL_LOGIN_AND_TXN_PIN_ALERT,pinDto);

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public Page getCustomerProfiles(Integer pageNumber, String requestPage) throws EOTException {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			WebUser webUser = webUserDao.getUser(auth.getName());
			if (webUser == null) {
				throw new EOTException(ErrorConstants.INVALID_USER);
			}

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}

			Page page = customerDao.getCustomerProfiles(teller.getBank().getBankId(), pageNumber);
			page.requestPage = requestPage;
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public String saveCustomerProfile(CustomerProfileDTO customerProfileDTO) throws EOTException {
		CustomerProfiles customerProfiles = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
		if (teller == null) {
			throw new EOTException(ErrorConstants.INVALID_TELLER);
		}
		if (customerProfileDTO.getProfileId() != null && !customerProfileDTO.getProfileId().equals("")) {
			CustomerProfiles customerProfile = customerDao.getCustomerProfileByName(customerProfileDTO.getProfileName(), teller.getBank().getBankId(), Integer.parseInt(customerProfileDTO.getProfileId()));
			if (customerProfile != null) {
				throw new EOTException(ErrorConstants.CUSTOMER_PROFILE_EXIST);
			}
			customerProfiles = customerDao.editCustomerProfile(Integer.parseInt(customerProfileDTO.getProfileId()));
			customerProfiles.setProfileName(customerProfileDTO.getProfileName());
			customerProfiles.setDescription(customerProfileDTO.getDescription());
			customerProfiles.setAuthorizedAmount(customerProfileDTO.getAuthorizedAmount());
			customerProfiles.setCustomerType(customerProfileDTO.getProfileType());
			customerDao.update(customerProfiles);
			return "CUSTOMER_PROFILE_EDIT_SUCCESS";
		} else {
			List<CustomerProfiles> customerProfilesList = customerDao.getCustomerProfileByName(customerProfileDTO.getProfileName(), teller.getBank().getBankId());
			if (customerProfilesList != null && !customerProfilesList.isEmpty()) {
				throw new EOTException(ErrorConstants.CUSTOMER_PROFILE_EXIST);
			}
			customerProfiles = new CustomerProfiles();
			customerProfiles.setProfileName(customerProfileDTO.getProfileName());
			customerProfiles.setDescription(customerProfileDTO.getDescription());
			customerProfiles.setBank(teller.getBank());
			customerProfiles.setAuthorizedAmount(customerProfileDTO.getAuthorizedAmount());
			customerProfiles.setCustomerType(customerProfileDTO.getProfileType());
			customerDao.save(customerProfiles);
			return "CUSTOMER_PROFILE_SAVE_SUCCESS";
		}

	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public CustomerProfileDTO editCustomerProfile(CustomerProfileDTO customerProfileDTO) throws EOTException {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			WebUser webUser = webUserDao.getUser(auth.getName());
			if (webUser == null) {
				throw new EOTException(ErrorConstants.INVALID_USER);
			}

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}

			CustomerProfiles customerProfiles = customerDao.editCustomerProfile(Integer.parseInt(customerProfileDTO.getProfileId()));
			if (customerProfiles != null) {
				customerProfileDTO.setProfileId(String.valueOf(customerProfiles.getProfileId()));
				customerProfileDTO.setProfileName(customerProfiles.getProfileName());
				customerProfileDTO.setDescription(customerProfiles.getDescription());
				customerProfileDTO.setProfileType(customerProfiles.getCustomerType());
				customerProfileDTO.setAuthorizedAmount(customerProfiles.getAuthorizedAmount());
				System.out.println("customerProfiles.getAuthorizedAmount(): ### " + customerProfiles.getAuthorizedAmount());
			}

			return customerProfileDTO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	public Integer getMobileNumLength(Integer countryId) throws EOTException {
		try {
			return customerDao.getMobileNumLength(countryId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	private void subscribeCustomerForUSSD(String mobileNumber, String defaultLanguage, Integer customerType) {

		URL url;
		try {
			url = new URL(appConfigurations.getLiveProfile());

			String params = "name=gim&index=%2B" + mobileNumber + "&fields=lang,usertype&values=" + defaultLanguage + "," + customerType + "&service=EOTPROD&restriction=wl&login=supernet&password=" + appConfigurations.getUssdPassword();

			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setAllowUserInteraction(false);

			DataOutputStream dstream = new DataOutputStream(uc.getOutputStream());

			dstream.write(params.getBytes());

			System.out.println("Params : " + params);

			// Read Response
			InputStream in = uc.getInputStream();
			int x;
			while ((x = in.read()) != -1) {
				System.out.write(x);
			}
			in.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void unSubscribeCustomerForUSSD(String mobileNumber) {

		URL url;
		try {
			url = new URL(appConfigurations.getWhiteList());

			// http://10.172.30.221:8083/ubc/ussdgtw/whitelist?login=supernet&password=superNet!0&name=gimprod&msisdn=%2B22501585002&todo=delete

			String params = "login=supernet&password=" + appConfigurations.getUssdPassword() + "&name=gimprod&msisdn=%2B" + mobileNumber + "&todo=delete";

			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setAllowUserInteraction(false);

			DataOutputStream dstream = new DataOutputStream(uc.getOutputStream());

			dstream.write(params.getBytes());

			System.out.println("Params : " + params);

			// Read Response
			InputStream in = uc.getInputStream();
			int x;
			while ((x = in.read()) != -1) {
				System.out.write(x);
			}
			in.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void unSubcribeFromLiveProfile(String mobileNumber) {

		URL url;
		try {
			url = new URL("http://10.172.30.221:8083/ubc/ussdgtw/profile?");

			// http://10.172.30.221:8083/ubc/ussdgtw/profile?login=supernet&password=superNet!0&index=%2B22501585002&name=gim&todo=delete

			String params = "login=supernet&password=" + appConfigurations.getUssdPassword() + "&index=%2B" + mobileNumber + "&name=gim&todo=delete";

			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setAllowUserInteraction(false);

			DataOutputStream dstream = new DataOutputStream(uc.getOutputStream());

			dstream.write(params.getBytes());

			System.out.println("Params : " + params);

			// Read Response
			InputStream in = uc.getInputStream();
			int x;
			while ((x = in.read()) != -1) {
				System.out.write(x);
			}
			in.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List exportToXLSForCustomerDetails(String customerName, String mobileNumber, String bankId, String branchId, String countryId, String bankGroupId, String fromDate, String toDate, Map<String, Object> model) throws EOTException {

		List list = null;
		Integer groupId = null;
		if (bankGroupId != null && bankGroupId != "") {
			groupId = Integer.parseInt(bankGroupId);
		}

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = webUserDao.getUser(userName);
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER) {
			BankTellers teller = webUserDao.getBankTeller(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_USER);
			}
			list = customerDao.exportToXLSForCustomerDetails(teller.getBank().getBankId(), null, customerName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, null);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = webUserDao.getBankTeller(userName);
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_USER);
			}
			list = customerDao.exportToXLSForCustomerDetails(teller.getBank().getBankId(), null, customerName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, teller.getBranch().getBranchId());
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = webUserDao.getbankGroupAdmin(webUser.getUserName());

			list = customerDao.exportToXLSForCustomerDetails(null, bankGroupAdmin.getBankGroup().getBankGroupId(), customerName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, null);
		} else {
			list = customerDao.exportToXLSForCustomerDetails(null, groupId, customerName, mobileNumber, bankId, branchId, countryId, fromDate, toDate, null);
		}

		if (list == null || list.size() == 0) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);

		}

		return list;
	}

	@Override
	public List<Customer> getCustomerList() {
		return customerDao.getCusomers();
	}

	@Override
	public List<SecurityQuestion> getCustomerQuestions(String locale) {

		return locationDao.getQuestions(locale);
	}

	@Override
	public WebUser getUser(String userName) {
		return webUserDao.getUser(userName);
	}

	@Override
	public BankTellers getBankTeller(String userName) {
		return webUserDao.getBankTeller(userName);
	}

	@Override
	public CustomerDTO getUserForProof(String name, CustomerDTO customerDTO) throws EOTException {

		BankTellers teller = webUserDao.getBankTeller(name);
		if (teller == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		Bank bank = teller.getBank();

		customerDTO.setIsIDProofRequired(bank.getKycFlag());

		return customerDTO;

	}

	@Override
	public CustomerDTO getUserForProofByPartner(String name, CustomerDTO customerDTO) throws EOTException {

		BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(name);
		Bank bank = businessPartnerUser.getBusinessPartner().getBank();

		// Bank bank = bankDao.getBank(EOTConstants.DEFAULT_BANK);

		customerDTO.setIsIDProofRequired(bank.getKycFlag());

		return customerDTO;

	}

	@Override
	public List<Branch> getBranchListForBank(String name) {

		BankTellers teller = webUserDao.getBankTeller(name);

		return bankDao.getbranchList(teller.getBank().getBankId());
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void addBankAccountDetails(BankAccountDTO bankAccountDTO, String name) throws EOTException {

		Branch branch = customerDao.getBranchDetails(bankAccountDTO.getBranchId());

		CBSAccount cbsAccount = customerDao.getCbsAccountDetails(bankAccountDTO.getCustomerBankAccountNumber(), branch.getBank().getBankCode(), branch.getBranchCode());

		if (cbsAccount == null) {
			throw new EOTException(ErrorConstants.CBS_ACCOUNT_NOTEXIST);
		}
		Customer customer = customerDao.getCustomer(bankAccountDTO.getCustomerId());

		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		if (customer.getBankCustomerId() == null) {
			throw new EOTException(ErrorConstants.INVALID_BANK_CUSTOMER_ID);
		}
		if (!customer.getBankCustomerId().equalsIgnoreCase(cbsAccount.getCustomerId())) {
			throw new EOTException(ErrorConstants.INVALID_BANK_CUSTOMER_ID);
		}

		CustomerBankAccount customerBankAccount = customerDao.getCustomerBankAccountDetails(bankAccountDTO.getCustomerBankAccountNumber());

		if (customerBankAccount != null) {
			throw new EOTException(ErrorConstants.CBS_ACCOUNT_ALREADY_EXIST);
		}

		Set<CustomerAccount> custAcc = customer.getCustomerAccounts();

		CustomerAccount customerAccount = custAcc.iterator().next();

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();

		accountDto.setAccountNO(customerAccount.getAccount().getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(customerAccount.getBank().getBankId().toString());
		accountDto.setBranchCode(customerAccount.getBranch().getBranchId().toString());
		ServiceChargeDebitDTO serviceChargeDebitDTO = new ServiceChargeDebitDTO();

		serviceChargeDebitDTO.setCustomerAccount(accountDto);
		serviceChargeDebitDTO.setReferenceID(customer.getCustomerId().toString());
		serviceChargeDebitDTO.setReferenceType(customer.getType());
		serviceChargeDebitDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		serviceChargeDebitDTO.setTransactionType(EOTConstants.TXN_ID_ADD_BANK_ACCOUNT + "");
		serviceChargeDebitDTO.setAmount(0D);

		try {
			utilityServicesCleintSub.serviceChargeDebit(serviceChargeDebitDTO);
		} catch (EOTCoreException e) {
			e.printStackTrace();
			throw new EOTException("ERROR_" + e.getMessageKey());
		}

		customerBankAccount = new CustomerBankAccount();
		customerBankAccount.setAccountHolderName(bankAccountDTO.getAccountHolderName());
		customerBankAccount.setAlias(bankAccountDTO.getAccountAlias());

		BankTellers teller = webUserDao.getBankTeller(name);
		customerBankAccount.setBankAccountNumber(bankAccountDTO.getCustomerBankAccountNumber());
//		customerBankAccount.setBank(teller.getBank());
		customerBankAccount.setCreatedDate(new Date());
		customerBankAccount.setUpdatedDate(new Date());
		customerBankAccount.setReferenceId(bankAccountDTO.getCustomerId().toString());
		customerBankAccount.setReferenceType(bankAccountDTO.getReferenceType());
		customerBankAccount.setStatus(2);

		customerDao.save(customerBankAccount);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void updateBranchDetails(BankAccountDTO bankAccountDTO) throws EOTException {

		Branch branch = customerDao.getBranchDetails(bankAccountDTO.getBranchId());

		CBSAccount cbsAccount = customerDao.getCbsAccountDetails(bankAccountDTO.getCustomerBankAccountNumber(), branch.getBank().getBankCode(), branch.getBranchCode());

		if (cbsAccount == null) {
			throw new EOTException(ErrorConstants.CBS_ACCOUNT_NOTEXIST);
		}

		CustomerBankAccount customerBankAccount = customerDao.getCustomerBankDetails(bankAccountDTO.getSlNo());

		customerBankAccount.setAlias(bankAccountDTO.getAccountAlias());
		customerBankAccount.setUpdatedDate(new Date());
		customerBankAccount.setStatus(bankAccountDTO.getStatus());

		customerDao.update(customerBankAccount);

	}

	@Override
	public Page getCustomerBankAccountDetails(Long customerId, int pageNumber, Map<String, Object> model) {
		Page page = customerDao.getCustomerBankAccountDetails(customerId, pageNumber);

		return page;
	}

	@Override
	public BankAccountDTO getCustomerBankAccountDetails(Long slNo) {

		CustomerBankAccount customerBankAccount = customerDao.getCustomerBankDetails(slNo);
		BankAccountDTO bankAccountDTO = new BankAccountDTO();
		bankAccountDTO.setAccountAlias(customerBankAccount.getAlias());
		bankAccountDTO.setAccountHolderName(customerBankAccount.getAccountHolderName());
		bankAccountDTO.setCustomerBankAccountNumber(customerBankAccount.getBankAccountNumber());
		bankAccountDTO.setStatus(customerBankAccount.getStatus());
		bankAccountDTO.setSlNo(customerBankAccount.getSlno());
		bankAccountDTO.setCustomerId(Long.parseLong(customerBankAccount.getReferenceId()));

		// Branch branch =
		// customerDao.getBranchDetailsByCode(customerBankAccount.getCbsaccount().getBranchCode());
		//
		// bankAccountDTO.setBranchId(branch.getBranchId());

		return bankAccountDTO;
	}

	@Override
	public Page getCustomerCardDetails(Long customerId, int pageNumber, Map<String, Object> model) {
		Page page = customerDao.getCustomerCardDetails(customerId, pageNumber);

		return page;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void addCustomerCard(CardDto cardDto, String name) throws EOTException {

		Customer customer = customerDao.getCustomer(cardDto.getCustomerId());
		BankTellers teller = webUserDao.getBankTeller(name);

		try {
			CustomerCard customerCard = customerDao.getCustomerCard(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardNo().getBytes(), true)));

			if (customerCard != null) {
				throw new EOTException(ErrorConstants.CUSTOMER_CARD_EXIST);
			}

			customerCard = new CustomerCard();
			customerCard.setCardNumber(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardNo().getBytes(), true)));
			customerCard.setAlias(cardDto.getAlias());
			customerCard.setBank(teller.getBank());
			customerCard.setCardExpiry(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardExpiry().getBytes(), true)));
			customerCard.setCvv(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCvv().getBytes(), true)));
			customerCard.setReferenceId(customer.getCustomerId().toString());
			customerCard.setReferenceType(customer.getType());
			customerCard.setStatus(cardDto.getStatus());

			customerDao.save(customerCard);

		} catch (KMSSecurityException e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void updateCustomerCard(CardDto cardDto) throws EOTException {

		try {
			CustomerCard customerCard = customerDao.getCustomerCard(cardDto.getCardId());
			if (customerCard == null) {
				throw new EOTException(ErrorConstants.INVALID_CARD_DETAILS);
			}
			CustomerCard card = customerDao.getCustomerCard(cardDto.getCardId(), HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardNo().getBytes(), true)));
			if (card != null) {
				throw new EOTException(ErrorConstants.CUSTOMER_CARD_EXIST);
			}
			customerCard.setCardNumber(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardNo().getBytes(), true)));
			customerCard.setAlias(cardDto.getAlias());
			customerCard.setCardExpiry(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardExpiry().getBytes(), true)));
			customerCard.setCvv(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCvv().getBytes(), true)));
			customerCard.setStatus(cardDto.getStatus());

			customerDao.update(customerCard);
		} catch (KMSSecurityException e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

	}

	@Override
	public CardDto getCustomerCardDetails(Long cardId) throws EOTException {
		CardDto cardDto = new CardDto();
		try {
			CustomerCard customerCard = customerDao.getCustomerCard(cardId);

			cardDto.setCardNo(new String(kmsHandle.externalDbDesOperation(HexString.hexToBuffer(customerCard.getCardNumber()), false)));
			cardDto.setCvv(new String(kmsHandle.externalDbDesOperation(HexString.hexToBuffer(customerCard.getCvv()), false)));
			cardDto.setCardExpiry(new String(kmsHandle.externalDbDesOperation(HexString.hexToBuffer(customerCard.getCardExpiry()), false)));
			cardDto.setAlias(customerCard.getAlias());
			cardDto.setStatus(customerCard.getStatus());
			cardDto.setCardId(customerCard.getCardId());
			cardDto.setCustomerId(Long.parseLong(customerCard.getReferenceId()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

		return cardDto;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public void processNewCard(CardDto cardDto) throws EOTException {

		Customer customer = customerDao.getCustomer(cardDto.getCustomerId());

		if (customer == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		try {
			CustomerCard customerCard = customerDao.getCustomerCard(HexString.bufferToHex(kmsHandle.externalDbDesOperation(cardDto.getCardNo().getBytes(), true)));

			Set<CustomerAccount> custAcc = customer.getCustomerAccounts();

			CustomerAccount customerAccount = custAcc.iterator().next();

			com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();

			accountDto.setAccountNO(customerCard.getCardNumber());
			accountDto.setAccountType(EOTConstants.ALIAS_TYPE_CARD_ACC + "");
			accountDto.setBankCode(customerAccount.getBank().getBankId().toString());
			accountDto.setBranchCode(customerAccount.getBranch().getBranchId().toString());

			ServiceChargeDebitDTO serviceChargeDebitDTO = new ServiceChargeDebitDTO();

			serviceChargeDebitDTO.setCustomerAccount(accountDto);
			serviceChargeDebitDTO.setReferenceID(customer.getCustomerId().toString());
			serviceChargeDebitDTO.setReferenceType(customer.getType());
			serviceChargeDebitDTO.setChannelType(EOTConstants.EOT_CHANNEL);
			serviceChargeDebitDTO.setTransactionType(EOTConstants.TXN_ID_ADDCARD + "");
			serviceChargeDebitDTO.setAmount(0D);

			try {
				utilityServicesCleintSub.serviceChargeDebit(serviceChargeDebitDTO);
			} catch (EOTCoreException e) {
				e.printStackTrace();
				customerDao.delete(customerCard);
				throw new EOTException("ERROR_" + e.getMessageKey());
			} catch (Exception e) {
				e.printStackTrace();
				customerDao.delete(customerCard);
				throw new EOTException(ErrorConstants.SERVICE_ERROR);
			}
		} catch (KMSSecurityException e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	private void createMerchantWebLogin(WebUserDTO webUserDTO) throws EOTException {

		WebUser webUser = new WebUser();
		Bank bank = new Bank();
		Branch branch = new Branch();
		WebUserRole userRole = new WebUserRole();
		userRole.setRoleId(3);
		webUser.setWebUserRole(userRole);
		String password = null;
		Country countryIsdCode = null;

		try {
			String userId = webUserDao.getWebUserId(EOTConstants.ROLEID_BANK_ADMIN);
			userId = ISOUtil.zeropad(userId, 6);
			countryIsdCode = locationDao.getCountry(webUserDTO.getCountryId());
			bank = bankDao.getBankCode(webUserDTO.getBankId());
			branch = bankDao.getBranchCode(webUserDTO.getBranchId());
			password = EOTUtil.generateWebUserPassword();
			String passwordHash = "";

			passwordHash = HashUtil.generateHash(password.getBytes(), EOTConstants.PIN_HASH_ALGORITHM);

			webUser.setUserName(bank.getBankCode().replace(" ", "").concat(branch == null ? "" : branch.getBranchCode()).replace(" ", "").concat(userId));
			webUser.setPassword(passwordHash);
			webUser.setCredentialsExpired(EOTConstants.WEB_USER_CREDENTIALS_NON_EXPIRED);
			webUser.setAccountLocked(EOTConstants.WEB_USER_UNLOCKED);
			webUser.setTFA(0);
			webUser.setFirstName(webUserDTO.getFirstName());
			webUser.setLastName(webUserDTO.getLastName());
			webUser.setMiddleName(webUserDTO.getMiddleName());
			webUser.setCreatedDate(new Date());
			webUser.setMobileNumber(webUserDTO.getMobileNumber());
			webUser.setAlternateNumber(EOTConstants.NEW_WEB_USER);
			webUser.setEmail(webUserDTO.getEmail());
			webUser.setDefaultLanguage(webUserDTO.getLanguage());
			webUser.setLoginAttempts(EOTConstants.LOGIN_ATTEMPTS);
			Country country = new Country();
			country.setCountryId(webUserDTO.getCountryId());
			webUser.setCountry(country);
			webUserDao.save(webUser);

			BankTellers teller = new BankTellers();
			bank.setBankId(webUserDTO.getBankId());
			teller.setBank(bank);
			branch.setBranchId(webUserDTO.getBranchId());
			teller.setBranch(branch);
			teller.setUserName(webUser.getUserName());
			teller.setWebUser(webUser);
			webUserDao.save(teller);

			/*
			 * WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
			 * dto.setUsername(webUser.getUserName());
			 * dto.setLocale(webUserDTO.getLanguage());
			 * dto.setMobileNo(countryIsdCode.getIsdCode()+ webUserDTO.getMobileNumber());
			 * dto.setPassword(password); dto.setScheduleDate(Calendar.getInstance());
			 * smsServiceClientStub.webUsernamePasswordAlert(dto);
			 */

		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	private Session getSession(String smscHost, int smscPort, String smscUsername, String smscPassword) throws Exception {
		Map<String, Session> sessionMap = new HashMap<String, Session>();

		BindRequest request = new BindTransmitter();
		request.setSystemId(smscUsername);
		request.setPassword(smscPassword);
		// request.setSystemType(systemType);
		// request.setAddressRange(addressRange);
		request.setInterfaceVersion((byte) 0x34); // SMPP protocol version

		TCPIPConnection connection = new TCPIPConnection(smscHost, smscPort);
		// connection.setReceiveTimeout(BIND_TIMEOUT);
		Session session = new Session(connection);
		sessionMap.put(smscUsername, session);
		BindResponse response = session.bind(request);
		return session;
	}

	private Address createAddress(String address) throws WrongLengthOfStringException {
		Address addressInst = new Address();
		addressInst.setTon((byte) 5); // national ton
		addressInst.setNpi((byte) 0); // numeric plan indicator
		addressInst.setAddress(address, Data.SM_ADDR_LEN);
		return addressInst;
	}

	@Override
	/* date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details */
	public List<SmsAlertRuleValue> getSmsPackageList() throws EOTException {
		List<SmsAlertRuleValue> smsPackageList = customerDao.getSmsPackageList();
		return smsPackageList;
	}

	@Override
	public List<SmsSubscriptionDTO> smsPackageDetails(String packageId) throws EOTException {
		List<SmsSubscriptionDTO> smsPackageList = customerDao.smsPackageDetails(packageId);
		return smsPackageList;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void subscribedSMSPackage(String customerId, String packageId, String subscriptionType, String noOfSms) throws EOTException {
		List<CustomerSmsRuleDetail> customerSmsRuleDetails = customerDao.getAllExistingSubscription(Long.valueOf(customerId));
		for (CustomerSmsRuleDetail smsRuleDetail : customerSmsRuleDetails) {
			smsRuleDetail.setStatus(EOTConstants.CUSTOMER_STATUS_INACTIVE);
			customerDao.update(smsRuleDetail);
		}
		CustomerSmsRuleDetail customerSmsRuleDetail = new CustomerSmsRuleDetail();
		Customer customer = new Customer();
		customer.setCustomerId(Long.parseLong(customerId));
		customerSmsRuleDetail.setCustomer(customer);
		SmsAlertRule smsAlertRule = new SmsAlertRule();
		smsAlertRule.setSmsAlertRuleId(Long.parseLong(packageId));
		customerSmsRuleDetail.setSmsalertrule(smsAlertRule);
		customerSmsRuleDetail.setSubscriptionType(Integer.parseInt(subscriptionType));
		customerSmsRuleDetail.setNoofSubscribedSms(Integer.parseInt(noOfSms));
		customerSmsRuleDetail.setSubscriptionStartDate(new Date());
		if (subscriptionType.equals("1"))
			customerSmsRuleDetail.setSubscriptionEndDate(new Date(new Date().getTime()));
		else if (subscriptionType.equals("2"))
			customerSmsRuleDetail.setSubscriptionEndDate(DateUtil.addDays(new Date(), 7));
		else if (subscriptionType.equals("3"))
			customerSmsRuleDetail.setSubscriptionEndDate(DateUtil.addDays(new Date(), 30));
		else if (subscriptionType.equals("4"))
			customerSmsRuleDetail.setSubscriptionEndDate(DateUtil.addDays(new Date(), 365));
		customerSmsRuleDetail.setNoofSmsUsed(0);
		customerSmsRuleDetail.setStatus(EOTConstants.ACTIVE);
		customerDao.save(customerSmsRuleDetail);

	}

	@Override
	public CustomerSmsRuleDetail getCurrentSubscription(String customerID) throws EOTException {
		CustomerSmsRuleDetail customerSmsRuleDetail = customerDao.getCurrentSubscription(customerID);

		return customerSmsRuleDetail;
	}

	@Override
	/* date:05/08/16 by:rajlaxmi for:showing only subscribed sc packages details */
	public List<ServiceChargeSubscription> getSCPackageList() throws EOTException {
		List<ServiceChargeSubscription> scPackageList = customerDao.getSCPackageList();
		return scPackageList;
	}

	@Override
	public List<SCSubscriptionDTO> scPackageDetails(String packageId) throws EOTException {
		List<SCSubscriptionDTO> smsPackageList = customerDao.scPackageDetails(packageId);
		return smsPackageList;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void subscribeSCPackage(String customerId, Long serviceChargeRuleId, String subscriptionType, String numberOfTxn) {

		Customer customer = new Customer();
		customer.setCustomerId(Long.valueOf(customerId));

		ServiceChargeRule serviceChargeRule = new ServiceChargeRule();
		serviceChargeRule.setServiceChargeRuleId(serviceChargeRuleId);

		CustomerScsubscription customerSCsubscription = new CustomerScsubscription();
		customerSCsubscription.setCustomer(customer);
		customerSCsubscription.setServiceChargeRule(serviceChargeRule);
		customerSCsubscription.setNoofSubscribedTxn(Integer.parseInt(numberOfTxn));
		customerSCsubscription.setSubscriptionStartDate(new Date());
		customerSCsubscription.setSubscriptionType(Integer.parseInt(subscriptionType));
		if (subscriptionType.equals("1"))
			customerSCsubscription.setSubscriptionEndDate(new Date(new Date().getTime()));
		else if (subscriptionType.equals("2"))
			customerSCsubscription.setSubscriptionEndDate(DateUtil.addDays(new Date(), 7));
		else if (subscriptionType.equals("3"))
			customerSCsubscription.setSubscriptionEndDate(DateUtil.addDays(new Date(), 30));
		else if (subscriptionType.equals("4"))
			customerSCsubscription.setSubscriptionEndDate(DateUtil.addDays(new Date(), 365));
		customerSCsubscription.setStatus(EOTConstants.ACTIVE);
		customerDao.save(customerSCsubscription);
	}

	@Override
	public CustomerScsubscription getSCCurrentSubscription(String customerId) {
		CustomerScsubscription customerScsubscription = customerDao.getSCCurrentSubscription(customerId);

		return customerScsubscription;
	}

	@Override
	public List<CustomerScsubscription> getSCSubscribedList(String customerId) {
		return customerDao.getSCSubscribedList(customerId);
	}

	@Override
	public String getLanguageDescription(String languageCode) {
		return customerDao.getLanguageDescription(languageCode);
	}

	@Override
	public List<CustomerProfiles> loadCustomerProfiles(Integer bankId) throws EOTException {

		return customerDao.loadCustomerProfiles(bankId);
	}

	@Override
	public Country getCountry(Integer countryId) {
		Country country = customerDao.getCountry(countryId);
		return country;
	}

	@Override
	public Map<String, Object> getMasterDataByType(String language, String type) throws EOTException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("countryList", operatorDao.getCountries(language));
		model.put("bankGroupList", bankDao.getAllBankGroups());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.put("transTypeList", transactionRulesDao.loadFilteredTransactionTypesWithServiceCharge(language));

		WebUser webUser = webUserDao.getUser(auth.getName());
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = webUserDao.getbankGroupAdmin(webUser.getUserName());

			model.put("bankList", bankGroupAdmin.getBankGroup().getBank());
		} else {
			model.put("bankList", bankDao.getAllBanksByName());
		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE) {
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			model.put("branchList", webUserService.getAllBranchFromBank(teller.getBank().getBankId()));
			model.put("customerProfileList", customerDao.getCustomerProfilesByType(teller.getBank().getBankId(), type));
		}

		return model;
	}

	@Override
	public City getCity(Integer cityId) {
		return customerDao.getCity(cityId);
	}

	@Override
	public Customer getCustomerByCustomerId(Long customerId) {
		return customerDao.getCustomer(customerId);
	}

	@Override
	public Quarter getQuarter(Long quarterId) {
		return customerDao.getQuarter(quarterId);
	}

	@Override
	public CustomerProfiles getCustomerProfiles(Integer customerProfileId) {
		return customerDao.getCustomerProfiles(customerProfileId);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void populateDataForJmeter() {

		Integer numberOfCustomers = 2001;
		long startTime = System.currentTimeMillis();

		for (int i = 1000, mobileNumber = 2000998; i < numberOfCustomers; i++, mobileNumber++) {

			// int referenceType=FieldExecutiveEnum.CUSTOMER.getCode();
			int referenceType = FieldExecutiveEnum.AGENT.getCode();
			String profileName = FieldExecutiveEnum.getEnumText(referenceType);
			String loggedInUserId = "7866163392000003";
			int bankId = 8;
			int profileId = 17;

			Country country = locationDao.getCountry(EOTConstants.DEFAULT_COUNTRY);
			City city = new City();
			city.setCityId(2);
			city.setCountry(country);

			Quarter quarter = new Quarter();
			quarter.setQuarterId(1L);
			quarter.setCity(city);

			// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Integer roleId = webUserService.getRoleId(loggedInUserId);
			Bank bank = null;
			Customer cust = new Customer();

			if (roleId == 24 || roleId == 25 || roleId == 26) {
				BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(loggedInUserId);
				bank = businessPartnerUser.getBusinessPartner().getBank();
			}

			Integer loginPin = EOTUtil.generateLoginPin();
			Integer txnPin = EOTUtil.generateTransactionPin();
			String appID = EOTUtil.generateAppID();
			String uuid = EOTUtil.generateUUID();

			AppMaster app = new AppMaster();
			app.setAppId(appID);
			app.setReferenceId("");
			app.setReferenceType(referenceType);
			app.setStatus(EOTConstants.APP_STATUS_NEW);
			app.setUuid(uuid);
			app.setAppVersion("1.0");

			Calendar cal = Calendar.getInstance();
			app.setCreatedDate(cal.getTime());
			Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth + 1);
			app.setExpiryDate(cal.getTime());

			customerDao.save(app);

			// String userName =
			// SecurityContextHolder.getContext().getAuthentication().getName() ;
			BusinessPartner businessPartner = businessPartnerDao.getBusinessPartner(loggedInUserId);

			cust.setBusinessPartner(businessPartner);
			cust.setMobileNumber(mobileNumber + "");
			cust.setFirstName("M" + i);

			cust.setMiddleName("S" + i);
			cust.setLastName("D" + i);
			cust.setDob(DateUtil.dateAndTime1("1986-07-07"));

			CustomerProfiles custProfile = new CustomerProfiles();
			custProfile.setProfileId(profileId);
			cust.setCustomerProfiles(custProfile);

			cust.setAddress("Address");
			cust.setCountry(country);
			city.setCityId(city.getCityId());
			cust.setCity(city);
			quarter.setQuarterId(quarter.getQuarterId());
			cust.setQuarter(quarter);
			cust.setEmailAddress("agent" + i + "@dummy.com");

			cust.setType(referenceType);
			cust.setActive(EOTConstants.CUSTOMER_STATUS_NEW);

			cust.setTitle("Mr");
			cust.setGender("Male");
			cust.setOnbordedBy(loggedInUserId);
			cust.setDefaultLanguage("en_US");

			try {
				cust.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
				cust.setTransactionPin(HashUtil.generateHash(txnPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			cust.setAppId(appID);

			cust.setCreatedDate(new Date());
			cust.setBankCustomerId(bankId + "");
			customerDao.save(cust);
			app.setReferenceId(cust.getCustomerId().toString());
			customerDao.update(app);

			CustomerDocument customerDocument = new CustomerDocument();

			customerDocument.setSignaturePhoto(Hibernate.createBlob(new byte[2]));
			customerDocument.setIdproofPhoto(Hibernate.createBlob(new byte[2]));
			customerDocument.setProfilePhoto(Hibernate.createBlob(new byte[2]));
			customerDocument.setAddressProof(Hibernate.createBlob(new byte[2]));

			customerDocument.setCustomerId(cust.getCustomerId());
			customerDocument.setCustomer(cust);
			customerDocument.setIssueDate(null);
			customerDocument.setPlaceOfIssue(null);
			customerDocument.setExpiryDate(null);
			customerDocument.setIdNumber(null);

			/*
			 * if(customerDTO.getIdType().equalsIgnoreCase("Others") &&
			 * customerDTO.getIDTypeothers()!=""){
			 * customerDocument.setIdType(customerDTO.getOthers()); }else
			 * if(customerDTO.getIdType().length()>0 && customerDTO.getIdType()!=""){
			 * customerDocument.setIdType(customerDTO.getIdType()); }
			 */

			customerDao.save(customerDocument);

			String alias = "";

			try {
				if (roleId != 24 && roleId != 25 && roleId != 26) {
					BankTellers teller = bankDao.getTellerByUsername(loggedInUserId);
					if (teller.getBank().getStatus() == EOTConstants.INACTIVE_BANK)
						throw new EOTException(ErrorConstants.INACTIVE_BANK);
					alias = EOTConstants.ACCOUNT_ALIAS_CUSTOMER + " - " + teller.getBank().getBankName();
				} else {
					if (bank.getStatus() == EOTConstants.INACTIVE_BANK)
						throw new EOTException(ErrorConstants.INACTIVE_BANK);
					alias = EOTConstants.ACCOUNT_ALIAS_CUSTOMER + " - " + bank.getBankName();
				}
			} catch (EOTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Account account = new Account();

			account.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
			account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
			account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);

			account.setAlias(alias);
			account.setCurrentBalance(20000.0);
			account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
			account.setReferenceId(cust.getCustomerId().toString());
			account.setReferenceType(referenceType);

			customerDao.save(account);

			CustomerAccount customerAccount = new CustomerAccount();
			customerAccount.setAccount(account);
			customerAccount.setAccountNumber(account.getAccountNumber());
			customerAccount.setCustomer(cust);

			if (roleId != 24 && roleId != 25 && roleId != 26) {
				BankTellers teller = bankDao.getTellerByUsername(loggedInUserId);
				customerAccount.setBank(teller.getBranch().getBank());
				customerAccount.setBranch(teller.getBranch());
			} else {
				customerAccount.setBank(bank);
				List<Branch> branches = new ArrayList<Branch>(bank.getBranches());
				customerAccount.setBranch(branches.get(0));
			}
			customerDao.save(customerAccount);

			System.out.println("Adding " + i);
		}

		long endTime = System.currentTimeMillis();
		System.out.println("It took " + TimeUnit.MILLISECONDS.toMinutes(endTime - startTime) + " minutes to save" + numberOfCustomers + " records");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void resetTxnPin(Long customerId) throws Exception {

		Customer cust = customerDao.getCustomer(customerId);

		if (cust == null) {
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}

		AppMaster app = customerDao.getApplication(cust.getCustomerId(), cust.getType());

		if (app == null) {
			throw new EOTException(ErrorConstants.INVALID_APPLICATION);
		}

		/*
		 * app.setStatus(EOTConstants.APP_STATUS_DEACTIVE); customerDao.update(app);
		 */

		// Integer loginPin = EOTUtil.generateLoginPin() ;
		Integer txnPin = EOTUtil.generateTransactionPin();
		/*
		 * String appID = EOTUtil.generateAppID(); String uuid = EOTUtil.generateUUID();
		 * 
		 * AppMaster newApp = new AppMaster(); newApp.setAppId(appID);
		 * newApp.setReferenceId(cust.getCustomerId()+"");
		 * newApp.setReferenceType(cust.getType());
		 * newApp.setStatus(EOTConstants.APP_STATUS_NEW); newApp.setUuid(uuid);
		 * newApp.setAppVersion("1.0");
		 * 
		 * Calendar cal = Calendar.getInstance(); newApp.setCreatedDate(cal.getTime());
		 * Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		 * cal.set(Calendar.DAY_OF_MONTH, dayOfMonth+1);
		 * newApp.setExpiryDate(cal.getTime());
		 * 
		 * customerDao.save(newApp);
		 * 
		 * cust.setAppId(appID);
		 * cust.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(),
		 * EOTConstants.PIN_HASH_ALGORITHM));
		 */
		cust.setTransactionPin(HashUtil.generateHash(txnPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

		customerDao.update(cust);

		/*
		 * AppLinkAlertDTO appAlertDto = new AppLinkAlertDTO();
		 * appAlertDto.setDownloadLink(appConfigurations.getAppDownloadURL()+uuid);
		 * appAlertDto.setLocale(cust.getDefaultLanguage());
		 * appAlertDto.setMobileNo(cust.getCountry().getIsdCode()+cust.getMobileNumber()
		 * ); appAlertDto.setScheduleDate(Calendar.getInstance());
		 * smsServiceClientStub.appLinkAlert(appAlertDto);
		 */
		ResetTxnPinAlertDTO pinDto = new ResetTxnPinAlertDTO();
		pinDto.setLocale(cust.getDefaultLanguage());
		// pinDto.setLoginPIN(loginPin.toString());
		pinDto.setMobileNo(cust.getCountry().getIsdCode() + cust.getMobileNumber());
		pinDto.setTxnPIN(txnPin.toString());
		pinDto.setScheduleDate(Calendar.getInstance());
//		smsServiceClientStub.resetTxnPinAlert(pinDto);
		sendSMS(UrlConstants.RESET_TXN_PIN_ALERT,pinDto);

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = EOTException.class)
	public String apprOrRejectCust(String type, String rejectComment, CustomerDTO customerDTO) throws EOTException {
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser webUser=webUserDao.getUser(userName);
		Long[] manageCustomer = customerDTO.getManageCustomer();
		for (Long customerId : manageCustomer) {

			if (null != customerId) {
				String gender=null;
				Customer customer = customerDao.getCustomer(customerId);
				gender=customer.getGender();
				AppMaster custApp = customerDao.getApplicationByAppId(customer.getAppId());
				if ("Approve".equals(type)) {
					CustomerProfiles customerProfile = new CustomerProfiles();
					//customerProfile.setProfileId(EOTConstants.APPROVED_CUSTOMER_PROFILE);
					customerProfile.setProfileId(customer.getCustomerProfiles().getProfileId());
					customer.setCustomerProfiles(customerProfile);
					customer.setKycStatus(KycStatusEnum.KYC_APPROVED.getCode());
					customer.setApprovedBy(webUser.getUserName());
					customer.setApprovalDate(new Date());

					if (customer.getActive().equals(EOTConstants.CUSTOMER_STATUS_DEACTIVATED)) {
						customer.setActive(EOTConstants.CUSTOMER_STATUS_ACTIVE);
						// custApp.setStatus(EOTConstants.APP_STATUS_ACTIVATED);
					}
					if (custApp.getStatus() == EOTConstants.APP_STATUS_BLOCKED) {
						 custApp.setStatus(EOTConstants.APP_STATUS_ACTIVATED);
					}

					String onboardedBy = customer.getOnbordedBy();
					//Customer agent = customerDao.getCustomerByMobileNumber(customer.getCountry().getIsdCode() + onboardedBy);
					Customer agent = customerDao.getAgentByAgentCode(onboardedBy);

					// Crediting customer registration fee to agent
					CustomerAccount agentAccount = null;
					if (agent != null && agent.getType().equals(FieldExecutiveEnum.AGENT.getCode())) {
						
						 agentAccount = customerDao.findAgentCommissionAccountBy(agent.getCustomerId());
						 /*Set<CustomerAccount> customerAccounts = agent.getCustomerAccounts();
						if (customerAccounts.iterator().hasNext()) {
							agentAccount = customerAccounts.iterator().next();
						}*/

						Account fromAccount = customerDao.getAccountByAliasAndRef("Sharable Commission amount", "0");
						Double commissionAmount= customerDao.getCommissionPercentageAgent(EOTConstants.TXN_ID_CUSTOMER_APPROVAL);
						if(commissionAmount > 0)
							creditCustomerRegistrationFee(customer, fromAccount, agentAccount, commissionAmount);
					}

					//Integer loginPin = EOTUtil.generateLoginPin();
					//Integer txnPin = EOTUtil.generateTransactionPin();
					try {
						//customer.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
						//customer.setTransactionPin(HashUtil.generateHash(txnPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
						if(gender!=null)//0 meansa pending
							customerDao.save(customer);

						/*InitialTxnPinLoginPinAlertDTO pinDto = new InitialTxnPinLoginPinAlertDTO();
						pinDto.setLocale(customer.getDefaultLanguage());
						pinDto.setLoginPIN(loginPin.toString());
						pinDto.setMobileNo(customer.getCountry().getIsdCode() + customer.getMobileNumber());
						pinDto.setTxnPIN(txnPin.toString());
						pinDto.setScheduleDate(Calendar.getInstance());

						smsServiceClientStub.initialTxnPinLoginPinAlert(pinDto);
						
						AppLinkAlertDTO aplDTO = new AppLinkAlertDTO();
						String appType = customer.getType().equals(EOTConstants.REFERENCE_TYPE_CUSTOMER) ? EOTConstants.APP_TYPE_CUSTOMER
										: customer.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT) ? EOTConstants.APP_TYPE_AGENT
										: customer.getType().equals(EOTConstants.REFERENCE_TYPE_MERCHANT) ? EOTConstants.APP_TYPE_MERCHANT:"";
						aplDTO.setApplicationName("m-GURUSH");
						aplDTO.setDownloadLink(appConfigurations.getAppDownloadURL() +appType);
						aplDTO.setLocale(customer.getDefaultLanguage());
						aplDTO.setMobileNo(customer.getCountry().getIsdCode()+ customer.getMobileNumber());

						smsServiceClientStub.appLinkAlert(aplDTO);*/
						
						SmsLog smsLog = new SmsLog();
						smsLog.setMobileNumber(customer.getCountry().getIsdCode() + customer.getMobileNumber());
						smsLog.setMessageType(100);
						smsLog.setEncoding(1);
						smsLog.setCreatedDate(new Date());
						smsLog.setMessage("Congratulations. Your KYC has been approved by m-GURUSH Admin.");
						smsLog.setScheduledDate(new Date());
						smsLog.setStatus(0);
						customerDao.save(smsLog);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if ("Reject".equals(type)) {
					customer.setReasonForRejection(rejectComment);
					customer.setActive(EOTConstants.CUSTOMER_STATUS_DEACTIVATED);
					customer.setKycStatus(KycStatusEnum.KYC_REJECTED.getCode());
					custApp.setStatus(EOTConstants.APP_STATUS_BLOCKED);
				}

				customerDao.update(customer);
				customerDao.update(custApp);
				customerDTO.setCustomerKycStatus(customer.getKycStatus());
			}
		}

		return type;

	}

	private void creditCustomerRegistrationFee(Customer customer, Account fromAccount, CustomerAccount agentAccount, Double commissionAmount) throws EOTException {
		
		//Double commissionAmount= customerDao.getCommissionPercentageAgent(EOTConstants.TXN_ID_CUSTOMER_APPROVAL);
		
		Account toAcc = agentAccount.getAccount();
		WebRequest request = new WebRequest();

		com.eot.dtos.common.Account accountDto = new com.eot.dtos.common.Account();
		accountDto.setAccountAlias(toAcc.getAlias());
		accountDto.setAccountNO(toAcc.getAccountNumber());
		accountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		accountDto.setBankCode(agentAccount.getBank().getBankId().toString());
		accountDto.setBranchCode(agentAccount.getBranch().getBranchId().toString());

		com.eot.dtos.common.Account bankAccountDto = new com.eot.dtos.common.Account();

		bankAccountDto.setAccountAlias(fromAccount.getAlias());
		bankAccountDto.setAccountNO(fromAccount.getAccountNumber());
		bankAccountDto.setAccountType(EOTConstants.ALIAS_TYPE_MOBILE_ACC + "");
		// bankAccountDto.setBankCode(bankTellers.getBank().getBankId().toString());
		// bankAccountDto.setBranchCode(bankTellers.getBranch().getBranchId().toString());

		request.setReferenceId(customer.getCustomerId() + "");
		request.setReferenceType(EOTConstants.REFERENCE_TYPE_CUSTOMER);
		request.setStatus(EOTConstants.WEBREQUEST_STATUS_FAILURE);
		TransactionType txnType = new TransactionType();
		txnType.setTransactionType(EOTConstants.TXN_ID_DEPOSIT);
		request.setTransactionType(txnType);
		request.setTransactionTime(new Date());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebUser webUser = webUserDao.getUser(auth.getName());
		request.setUserName(webUser);
		customerDao.save(request);
		
		

		Header headerDTO = new Header();

		headerDTO.setReferenceID(customer.getCustomerId().toString());
		headerDTO.setAmount(commissionAmount);
//		headerDTO.setAmount((new Double(100)));
		headerDTO.setChannelType(EOTConstants.EOT_CHANNEL);
		headerDTO.setRequestChannel("WEB");
		headerDTO.setCustomerAccount(accountDto);
		// headerDTO.setOtherAccount(bankAccountDto);
		headerDTO.setTransactionType(EOTConstants.TXN_ID_CUSTOMER_APPROVAL + "");

		try {
			headerDTO = processRequest(CoreUrls.CUSTOMER_APPROVAL_URL, headerDTO, com.eot.dtos.common.Header.class);
			if (headerDTO.getErrorCode() != 0) {
				throw new EOTException("ERROR_" + headerDTO.getErrorCode());
			}
			request.setStatus(EOTConstants.WEBREQUEST_STATUS_SUCCESS);
			com.eot.entity.Transaction txn = new com.eot.entity.Transaction();
			txn.setTransactionId(new Long(headerDTO.getTransactionNO()));
			request.setTransaction(txn);
		} catch (EOTException e) {
			e.printStackTrace();
			request.setStatus(new Integer(headerDTO.getErrorCode()));
			throw new EOTException(e.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EOTException(ErrorConstants.CORE_CONNECTION_ERROR);
		} finally {
			customerDao.update(request);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = EOTException.class)
	public String sendOTP(CustomerDTO customerDTO) throws EOTException {
		WebOTPAlertDTO dto = new WebOTPAlertDTO();
		String message = "";

		Country country = locationDao.getCountry(customerDTO.getCountryId());
		String mobileNumber = country.getIsdCode().toString().concat(customerDTO.getMobileNumber());

		Customer cust = customerDao.getCustomerByMobileNumber(mobileNumber);
		if (cust != null) {
			throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
		}

		dto.setLocale("en_US");
		dto.setMobileNo(mobileNumber);
		dto.setOtpType(EOTConstants.OTP_TYPE_CUSTOMER);
		dto.setReferenceId(mobileNumber);
		dto.setReferenceType(EOTConstants.REF_TYPE_CUSTOMER);
		dto.setScheduleDate(Calendar.getInstance());

		SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_OTP_ALERT,dto);
		message = "OTP sent successfully to: " + customerDTO.getMobileNumber();
		if(responseDTO.getStatus().equalsIgnoreCase("0")) {
			message = "Error occured while sending OTP";
			throw new EOTException(ErrorConstants.SMS_ALERT_FAILED);
		}
		


		return message;
	}
	/*
	 * public static void main(String[] args) { String str = "200094";
	 * System.out.println(str.substring(1)); }
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
	
	private boolean setProfessionValue(String profession) {
		switch(profession){
			case "sdsd" : return true;
			case "Employee" : return true;
			case "Farmer" : return true;
			case "Health Staff Magistrate" : return true;
			case "Journalist" : return true;
			case "Lawer" : return true;
			case "Magistrate" : return true;
			case "Managing Director" : return true;
			case "Military" : return true;
			case "Officer" : return true;
			case "Pharmacist" : return true;
			case "Policeman" : return true;
			case "Retired" : return true;
			case "Student" : return true;
			case "Teacher" : return true;
			case "Unemployed" : return true;
			case "Worker" : return true;
			case "Doctor" : return true;
			case "Nurse" : return true;
			default : return false;
		}
	}
	
		@Override
	public Page getCustomersWithQRCode(QRCodeDTO qrCodeDTO, Integer pageNumber) throws EOTException{
		Page page = null;
		page = customerDao.getCustomersQRCode(qrCodeDTO, pageNumber);
		return page;
	}

		@Override
		public void exportQRCodeInPDF(String customerId, HttpServletResponse response)
				throws EOTException {
			
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			OutputStream os=null;
			QRCodeDTO qrCodeDTO=new QRCodeDTO();
			Customer customer = customerDao.getCustomer(Long.parseLong(customerId));
			qrCodeDTO.setAgentCode(customer.getAgentCode());
			qrCodeDTO.setName(customer.getFirstName()+" "+customer.getLastName());
			qrCodeDTO.setState(customer.getCity().getCity());
			qrCodeDTO.setCity(customer.getQuarter().getQuarter());
			qrCodeDTO.setMobileNumber(customer.getMobileNumber());
			qrCodeDTO.setAddress(customer.getAddress());
			
			QRModel qrModel = new QRModel();
			if(customer.getType() == EOTConstants.CUSTOMER_TYPE_CUSTOMER)
				qrModel.setMobile(customer.getMobileNumber());
			else
				qrModel.setMobile(customer.getAgentCode());
			
			qrModel.setCode(customer.getCountry().getIsdCode().toString());
			qrModel.setPatch(customer.getType().toString());
			String qrCodeData = new JSONAdaptor().toJSON(qrModel);
			
			String charset = "UTF-8";

			PDDocument document = new PDDocument();
			PDFont fontNormal = PDType1Font.COURIER;
	        PDFont fontBold = PDType1Font.COURIER_BOLD;
			Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
			try {
				BufferedImage bitImage = QRCodeUtil.createQRCode(qrCodeData, charset, hintMap, 350, 350);
			
				populateQRCodePDF(qrCodeDTO, document, fontNormal, fontBold, bitImage);
				//in.close();

				//fileName = appConfigurations.getQrCodePath()+customer.getAgentCode()+"_QRCode.pdf";
				document.save(byteStream);
				document.close();
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "attachment; filename=" + customer.getAgentCode()+ "_QR_Code.pdf");
				os = response.getOutputStream();
				os.write(byteStream.toByteArray());
				
			} catch (WriterException | IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if (os!=null) {
						os.flush();
						os.close();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void populateQRCodePDF(QRCodeDTO qrCodeDTO, PDDocument document, PDFont fontNormal, PDFont fontBold,
				BufferedImage bitImage) throws IOException {
			float width = bitImage.getWidth()+100f;
			float height = bitImage.getHeight()+100f;
			
			PDPage page = new PDPage(new PDRectangle(width, height));
			document.addPage(page); 
			PDImageXObject img = JPEGFactory.createFromImage(document, bitImage);
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			
			contentStream.setFont( PDType1Font.COURIER, 12 );  
			contentStream.drawImage(img, 0, 100);
			contentStream.setLeading(15f);
			contentStream.beginText();
			
			//Setting Name
			contentStream.newLineAtOffset(39,110);
			contentStream.setFont(fontBold, 12);
			contentStream.showText("Name: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getName());
			
			//Setting Code
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("Code: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getAgentCode());
			
			//Setting MobileNumber
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("MobileNumber: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getMobileNumber());
			
			//Setting State
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("State: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getState());
			
			//Setting City
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("City: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getCity());
			
			//Setting Address
			contentStream.newLine();
			contentStream.setFont(fontBold, 12);
			contentStream.showText("Address: ");
			contentStream.setFont(fontNormal, 12);
			contentStream.showText(qrCodeDTO.getAddress().replaceAll("\\r\\n|\\r|\\n", " "));
			
			
			contentStream.endText();
			contentStream.close();
		}

		@Override
		public void bulkQRCodePDF(String customerId, HttpServletResponse response, ZipOutputStream zipStream ) throws EOTException {

			
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename= \"Agent_QR_Code.zip\"");
			
			QRCodeDTO qrCodeDTO=new QRCodeDTO();
			Customer customer = customerDao.getCustomer(Long.parseLong(customerId));
			qrCodeDTO.setAgentCode(customer.getAgentCode());
			qrCodeDTO.setName(customer.getFirstName()+" "+customer.getLastName());
			qrCodeDTO.setState(customer.getCity().getCity());
			qrCodeDTO.setCity(customer.getQuarter().getQuarter());
			qrCodeDTO.setMobileNumber(customer.getMobileNumber());
			qrCodeDTO.setAddress(customer.getAddress());
			
			QRModel qrModel = new QRModel();
			if(customer.getType() == EOTConstants.CUSTOMER_TYPE_CUSTOMER)
				qrModel.setMobile(customer.getMobileNumber());
			else
				qrModel.setMobile(customer.getAgentCode());
			qrModel.setCode(customer.getCountry().getIsdCode().toString());
			qrModel.setPatch(customer.getType().toString());
			String qrCodeData = new JSONAdaptor().toJSON(qrModel);
			String charset = "UTF-8";

			PDDocument document = new PDDocument();
			PDFont fontNormal = PDType1Font.COURIER;
	        PDFont fontBold = PDType1Font.COURIER_BOLD;
			Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
			try {
				
				ZipEntry ze = new ZipEntry(customer.getAgentCode()+"QR_Code" + ".pdf");
				zipStream.putNextEntry(ze);
				
				BufferedImage bitImage = QRCodeUtil.createQRCode(qrCodeData, charset, hintMap, 350, 350);
				populateQRCodePDF(qrCodeDTO, document, fontNormal, fontBold, bitImage);

				//fileName = appConfigurations.getQrCodePath()+customer.getAgentCode()+"_QRCode.pdf";
				document.save(byteStream);
				zipStream.write(byteStream.toByteArray());
				
			} catch (WriterException | IOException e) {
				e.printStackTrace();
			}
		 
		}

		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
		public void processChangeCustomerStatus(Long customerId, Integer status) throws EOTException {
			Customer cust = null;
			cust = customerDao.getCustomer(customerId);
			cust.setActive(status);
			customerDao.update(cust);
			System.out.println("-----Hello------");
			
		}

		public SmsResponseDTO sendSMS(String url,SmsHeader smsHeader)
		{
			SmsResponseDTO dto= new SmsResponseDTO();
			try {
				dto= 	restTemplate.postForObject(url, smsHeader, SmsResponseDTO.class);
			}catch (Exception e) {
				dto.setStatus("0");
				e.printStackTrace();
			}
			return dto;
		}

		@Override
		public Page searchBlockedCustomers(String fromDate, String toDate, Integer pageNumber, String custType) {
			return customerDao.searchBlockedCustomers(fromDate,toDate,pageNumber,custType);
		}

		@Override
		public Page getAgentCashOutData(TransactionParamDTO transactionParamDTO, Integer pageNumber) {
			return customerDao.searchAgentCashOutData(transactionParamDTO,pageNumber);
		}
		
		@Override
		@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = EOTException.class)
		public void sendOTPForAgentCashOut(TransactionParamDTO transactionParamDTO) throws EOTException {
			WebOTPAlertDTO dto = new WebOTPAlertDTO();
			String mobileNumber = transactionParamDTO.getCustomerMobileNo();

			dto.setLocale("en_US");
			dto.setMobileNo(EOTConstants.COUNTRY_CODE_SOUTH_SUDAN+mobileNumber);
			dto.setOtpType(EOTConstants.REFERENCE_TYPE_CASH_OUT);
			dto.setReferenceId(transactionParamDTO.getCustomer().getCustomerId()+"");
			dto.setReferenceType(EOTConstants.REF_TYPE_AGENT);
			dto.setScheduleDate(Calendar.getInstance());
			dto.setAmount(transactionParamDTO.getAmount());
			dto.setTransactionType(EOTConstants.TXN_TYPE_AGENT_CASH_OUT);

			SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_OTP_ALERT,dto);
			if(responseDTO.getStatus().equalsIgnoreCase("0")) {
				throw new EOTException(ErrorConstants.SMS_ALERT_FAILED);
			}

		}

		@Override
		public TransactionParamDTO getAgentCashOutDetails(TransactionParamDTO transactionParamDTO) throws EOTException {
			
			PendingTransaction pendingTransaction = customerDao.getPendingTransaction(transactionParamDTO.getTransactionId());
			if (pendingTransaction == null) {
				throw new EOTException(ErrorConstants.INVALID_AGENT_CASH_OUT);
			}
			
			transactionParamDTO.setTransactionId(pendingTransaction.getTransactionId()); 
			transactionParamDTO.setAmount(pendingTransaction.getAmount().longValue());  
			transactionParamDTO.setApprovedBy(pendingTransaction.getApprovedBy());  
			transactionParamDTO.setBank(pendingTransaction.getBank());  
			transactionParamDTO.setComment(pendingTransaction.getComment()); 
			transactionParamDTO.setCustomer(pendingTransaction.getCustomer());  
			transactionParamDTO.setCustomerAccount( pendingTransaction.getCustomerAccount()); 
			transactionParamDTO.setCustomerAccountType(pendingTransaction.getCustomerAccountType());  
			transactionParamDTO.setCustomerCode(pendingTransaction.getCustomerCode()); 
			transactionParamDTO.setMobileNumber(EOTConstants.COUNTRY_CODE_SOUTH_SUDAN+pendingTransaction.getCustomerMobileNo());  
			transactionParamDTO.setCustomerMobileNo(EOTConstants.COUNTRY_CODE_SOUTH_SUDAN+pendingTransaction.getCustomerMobileNo());  
			transactionParamDTO.setInitiatedBy( pendingTransaction.getInitiatedBy()	); 
			transactionParamDTO.setName(pendingTransaction.getName());  
			transactionParamDTO.setOtherAccount(pendingTransaction.getOtherAccount());  
			transactionParamDTO.setOtherAccountType(pendingTransaction.getOtherAccountType());  
			transactionParamDTO.setReferenceType(pendingTransaction.getReferenceType());  
			transactionParamDTO.setStatus(pendingTransaction.getStatus()+"");  
			transactionParamDTO.setTransactionDate(pendingTransaction.getTransactionDate());  
			transactionParamDTO.setTransactionJournals(pendingTransaction.getTransactionJournals());  
			transactionParamDTO.setTransactionNo(pendingTransaction.getTransactionNo());  
			transactionParamDTO.setTransactionType(pendingTransaction.getTransactionType().getTransactionType().intValue());  
			
			return transactionParamDTO;
		}
}
