/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: EOTConstants.java
*
* Date Author Changes
* 19 May, 2016 Swadhin Created
*/
package com.eot.banking.common;


// TODO: Auto-generated Javadoc
/**
 * The Interface EOTConstants.
 */
public interface EOTConstants {

	/** The key version. */
	String KEY_VERSION = "1.0";
	
	/** The key owner. */
	String KEY_OWNER = "eot";
	
	/** Account refrence type */
	int REFRENCE_TYPE_BUSINESSPARTNER=8;
	
	/** The pin hash algorithm. */
	String PIN_HASH_ALGORITHM = "SHA-512";
	
	/** The request status success. */
	int REQUEST_STATUS_SUCCESS = 1 ;
	
	/** The account type personal. */
	int ACCOUNT_TYPE_PERSONAL = 1;
	
	/** The account type wallet. */
	int ACCOUNT_TYPE_WALLET = 1;
	
	/** The account type nominal. */
	int ACCOUNT_TYPE_NOMINAL = 2;
	
	/** The account type real. */
	int ACCOUNT_TYPE_REAL = 3;
	
	/** The bookid customer. */
	int BOOKID_CUSTOMER = 1 ;
	
	/** The bookid bank. */
	int BOOKID_BANK = 2 ;
	
	/** The bookid eot. */
	int BOOKID_EOT = 3 ;
	
	/** The account head bank account. */
	int ACCOUNT_HEAD_BANK_ACCOUNT = 6 ;
	
	/** The alias type mobile acc. */
	int ALIAS_TYPE_MOBILE_ACC = 1;
	int ALIAS_TYPE_WALLET_ACCOUNT = 1;
	
	int ALIAS_TYPE_COMMISSION_ACCOUNT = 10;
	int SOURCE_TYPE_WALLET = 1;
	
	/** The alias type card acc. */
	int ALIAS_TYPE_CARD_ACC = 2;
	
	/** The alias type bank acc. */
	int ALIAS_TYPE_BANK_ACC = 0;
	
	int ACCOUT_TYPE_BUSINESSPARTNER = 8;

	/** The account status inactive. */
	int ACCOUNT_STATUS_INACTIVE  = 0;
	
	/** The account status active. */
	int ACCOUNT_STATUS_ACTIVE  = 1;
	
	/** The app status new. */
	int APP_STATUS_NEW = 10 ;
	
	/** The app status downloaded. */
	int APP_STATUS_DOWNLOADED = 20 ;
	
	/** The app status sc debited. */
	int APP_STATUS_SC_DEBITED = 30 ;
	
	/** The app status activated. */
	int APP_STATUS_ACTIVATED = 40 ;
	
	int APP_STATUS_ACTIVATED_TEMP_KYC_APPROVE = 20 ;
	
	/** The app status reset pin verified. */
	int APP_STATUS_RESET_PIN_VERIFIED = 50 ;
	
	/** The app status reset pin sc debited. */
	int APP_STATUS_RESET_PIN_SC_DEBITED = 60;
	
	/** The app status new pin sent. */
	int APP_STATUS_NEW_PIN_SENT = 70;
	
	/** The app status blocked. */
	int APP_STATUS_BLOCKED = 80;
	
	/** The app status deactive. */
	int APP_STATUS_DEACTIVE = 90;
	
	/** The account balance type debit. */
	int ACCOUNT_BALANCE_TYPE_DEBIT = 0;
	
	/** The account balance type credit. */
	int ACCOUNT_BALANCE_TYPE_CREDIT = 1;
	
	/** The account alias bank. */
	String ACCOUNT_ALIAS_BANK = "Bank Account";
	
	/** The account alias customer. */
	String ACCOUNT_ALIAS_CUSTOMER = "m-Gurush Payments";
	
	String ACCOUNT_ALIAS_MOB_CUSTOMER = "mGURUSH Mobile";
	
	String ACCOUNT_ALIAS_COMMISSION="Agent Commission Account";
	String ACCOUNT_ALIAS_COMMISSION_CASHOUT="Agent Commission Account - mGurush";
	/** The account alias stake holder. */
	String ACCOUNT_ALIAS_STAKE_HOLDER = "Stakeholder Account";
	
	/** The reference type customer. */
	int REFERENCE_TYPE_CUSTOMER = 0;
	
	/** The reference type merchant. */
//	int REFERENCE_TYPE_MERCHANT = 2;
	
	/** The reference type merchant. */
	int REFERENCE_TYPE_AGENT = 1;
	
	/** The reference type sole merchant. */
	int REFERENCE_TYPE_MERCHANT = 2;
	
	/** The reference type sole merchant. */
	int REFERENCE_TYPE_AGENT_SOLE_MERCHANT = 3;
	
	/** The reference type stake holder. */
	int REFERENCE_TYPE_STAKE_HOLDER = 3;
	
	/** The reference type transaction. */
	int REFERENCE_TYPE_TRANSACTION = 4;
	
	/** The reference type bank. */
	int REFERENCE_TYPE_BANK = 5;
	
	/** The reference type web user. */
	int REFERENCE_TYPE_WEB_USER = 6;
	
	/** The reference type operator. */
	int REFERENCE_TYPE_OPERATOR = 7;
	
	/** The amt type tax amount. */
	String AMT_TYPE_TAX_AMOUNT = "TAX_AMT" ;
	
	/** The amt type eot share. */
	String AMT_TYPE_EOT_SHARE = "EOT_SC_SHARE" ;
	
	/** The amt type bank rev. */
	String AMT_TYPE_BANK_REV = "BANK_REVENUE" ;
	
	/** The amt type inter bank fee. */
	String AMT_TYPE_INTER_BANK_FEE = "INTER_BANK_FEE" ;
	
	/** The amt type stamp fee. */
	String AMT_TYPE_STAMP_FEE="STAMP_FEE";
	
	/** The txn type intra bank. */
	int TXN_TYPE_INTRA_BANK = 1 ; 
	
	/** The txn type inter bank. */
	int TXN_TYPE_INTER_BANK = 2 ;
	
	// global acc= 7
	
	/** The customer status new. */
	int CUSTOMER_STATUS_NEW = 10;
	
	/** The customer status active. */
	int CUSTOMER_STATUS_ACTIVE = 20;
	
	/** The customer status inactive. */
	int CUSTOMER_STATUS_INACTIVE = 30;
	
	/** The customer status deactivated. */
	int CUSTOMER_STATUS_DEACTIVATED = 40;
	
	/** The application id. */
	String APPLICATION_ID = "1";
	
	/** The confirmation code. */
	int CONFIRMATION_CODE = 1;
	
	/** The bank id. */
	int BANK_ID = 1;
	
	/** The eot channel. */
	String EOT_CHANNEL = "1";
	
	/** The txn id activation. */
	int TXN_ID_ACTIVATION = 10;
	
	/*Murari, 10/07/2018, Adding transaction type 31 "ADD_PAYEE--- start---*/
	/** The txn id add payee. */
	int TXN_ID_ADD_PAYEE = 31;
	/*end*/
	
	/** The txn id pinchange. */
	int TXN_ID_PINCHANGE = 15;
	
	/** The txn id txnpinchange. */
	int TXN_ID_TXNPINCHANGE = 20;
	
	/** The txn id balance enquiry. */
	int TXN_ID_BALANCE_ENQUIRY = 30;
	
	/** The txn id ministatement. */
	int TXN_ID_MINISTATEMENT = 35;
	
	/** The txn id trfdirect. */
	int TXN_ID_TRFDIRECT = 55;
	
	/** The txn id reactivation. */
	int TXN_ID_REACTIVATION = 65;
	
	/** The txn id reset pin. */
	int TXN_ID_RESET_PIN = 70;
	
	/** The txn id reset txnpin. */
	int TXN_ID_RESET_TXNPIN = 75;
	
	/** The txn id topup. */
	int TXN_ID_TOPUP = 80;
	
	/** The txn id sale. */
	int TXN_ID_SALE = 90;
	
	/** The txn id deposit. */
	int TXN_ID_DEPOSIT = 95;
	int TXN_ID_AGETNT_DEPOSIT = 115;
	int TXN_ID_AGENT_WITHDRAWAL = 116;
	int TXN_ID_COMMISSION_SHARE = 120;
	int TXN_ID_CUSTOMER_APPROVAL = 135;
	
	int TXN_ID_SMS_CASH_RECV = 126;
	int TXN_ID_PAY = 128;
	
	/** The txn id txnstatement. */
	int TXN_ID_TXNSTATEMENT = 98;
	
	/** The txn id withdrawal. */
	int TXN_ID_WITHDRAWAL = 99;
	
	/** The txn id billpayment. */
	int TXN_ID_BILLPAYMENT = 82;
	
	/** The txn id smscash. */
	int TXN_ID_SMSCASH = 83;
	
	/** The txn id addcard. */
	int TXN_ID_ADDCARD = 40;
	
	/** The txn id confirmcard. */
	int TXN_ID_CONFIRMCARD = 45;
	
	/** The txn id deletecard. */
	int TXN_ID_DELETECARD = 50;
	
	/** The txn id paymenthistory. */
	int TXN_ID_PAYMENTHISTORY = 86;
	
	/** The txn id reversal. */
	int TXN_ID_REVERSAL = 61;
	
	/** The txn id cancel. */
	int TXN_ID_CANCEL = 60;
	
	/** The txn id smscash others. */
	int TXN_ID_SMSCASH_OTHERS = 84;
	
	/** The txn id smscash others. */
	int TXN_ID_FLOAT_MANAGEMENT = 133;
	
	/** The customer pin change. */
	int TXN_ID_CUSTOMER_PIN_CHANGE = 108;
	
	/** The txn id add bank account. */
	int TXN_ID_ADD_BANK_ACCOUNT = 37;
	
	/** The txn id cheque status. */
	int TXN_ID_CHEQUE_STATUS = 39;

	/** The roleid eot admin. */
	int ROLEID_EOT_ADMIN = 1 ;
	
	/** The roleid bank admin. */
	int ROLEID_BANK_ADMIN = 2 ;
	
	/** The roleid bank teller. */
	int ROLEID_BANK_TELLER = 3 ;
	
	/** The roleid cc admin. */
	int ROLEID_CC_ADMIN = 4 ;
	
	/** The roleid cc executive. */
	int ROLEID_CC_EXECUTIVE = 5 ;
	
	/** The roleid bank group admin. */
	int ROLEID_BANK_GROUP_ADMIN = 7 ;
	
	/** The roleid bank teller manager. */
	int ROLEID_BANK_TELLER_MANAGER=6;
	
	/** The roleid parameter. */
	int ROLEID_PARAMETER = 8;
	
	/** The roleid audit. */
	int ROLEID_AUDIT = 9;
	
	/** The roleid accounting. */
	int ROLEID_ACCOUNTING = 10;
	
	
	/** The roleid branch manager. */
	int ROLEID_BRANCH_MANAGER = 12 ;
	
	/** The roleid relationship manager. */
	int ROLEID_RELATIONSHIP_MANAGER = 13 ;
	
	/** The roleid personal relationship exce. */
	int ROLEID_PERSONAL_RELATIONSHIP_EXCE = 14 ;
	
	/** The roleid super admin. */
	int ROLEID_SUPER_ADMIN = 15 ;
	
	/** The roleid support bank. */
	int ROLEID_SUPPORT_BANK = 16 ;
	
	/** The roleid eot backoffice lead. */
	int ROLEID_EOT_BACKOFFICE_LEAD = 17;
	
	/** The roleid eot backoffice exec. */
	int ROLEID_EOT_BACKOFFICE_EXEC = 18;
	
	/** The roleid eot support lead. */
	int ROLEID_EOT_SUPPORT_LEAD = 19;
	
	/** The roleid eot support exec. */
	int ROLEID_EOT_SUPPORT_EXEC = 20;
	
	/** The roleid information desk. */
	int ROLEID_INFORMATION_DESK = 21;
	
	/** The roleid operations. */
	int ROLEID_OPERATIONS = 22;
	

	/** The roleid Business Partner. */
	//int ROLEID_BUSINESS_PARTNER = 24;

	/** The roleid baAdmin. */
	int ROLEID_BA_ADMIN = 23;
	
	/** The roleid baAdmin. */
	int ROLEID_BUSINESS_PARTNER_L1 = 24;
	
	/** The roleid baAdmin. */
	int ROLEID_BUSINESS_PARTNER_L2 = 25;
	
	/** The roleid baAdmin. */
	int ROLEID_BUSINESS_PARTNER_L3 = 26;
	
	/** The roleid sales Officer. */
	int ROLEID_SALES_OFFICERS = 27;
	
	/** The BulkPaymet Partner. */
	int ROLEID_BULKPAYMENT_PARTNER = 28 ;
	
	/** The BulkPaymet Partner. */
	int ROLEID_COMMERCIAL_OFFICER = 29 ;
	
	/** The BulkPaymet Partner. */
	int ROLEID_SUPPORT_CALL_CENTER = 30 ;
	
	/** The web user locked. */
	String WEB_USER_LOCKED = "Y" ;
	
	/** The web user unlocked. */
	String WEB_USER_UNLOCKED = "N" ;
	
	/** The web user credentials expired. */
	String WEB_USER_CREDENTIALS_EXPIRED = "Y" ;
	
	/** The web user credentials non expired. */
	String WEB_USER_CREDENTIALS_NON_EXPIRED = "N" ;
	
	/** The customer virtual card. */
	String CUSTOMER_VIRTUAL_CARD = "Virtual Card";
	
	/** The rule level global. */
	int RULE_LEVEL_GLOBAL = 1 ;
	
	/** The rule level bank group. */
	int RULE_LEVEL_BANK_GROUP = 2 ;
	
	/** The rule level customer profile. */
	int RULE_LEVEL_CUSTOMER_PROFILE = 3 ;
	
	/** The rule level inter bank. */
	int RULE_LEVEL_INTER_BANK = 4 ;
	
	/** The agreement type rev sharing. */
	int AGREEMENT_TYPE_REV_SHARING = 1;
	
	/** The agreement type one time payment. */
	int AGREEMENT_TYPE_ONE_TIME_PAYMENT = 2;
	
	/** The otp type web user. */
	int OTP_TYPE_WEB_USER = 1;
	
	/** The otp type customer. */
	int OTP_TYPE_CUSTOMER = 2;
	
	/** The RE f_ typ e_ customer. */
	int REF_TYPE_CUSTOMER = 0;
	
	/** The RE f_ typ e_ customer. */
	int REF_TYPE_AGENT = 1;
	
	/** The mobrequest status logged. */
	int MOBREQUEST_STATUS_LOGGED = 0;
	
	/** The mobrequest status success. */
	int MOBREQUEST_STATUS_SUCCESS = 1;
	
	/** The mobrequest status failure. */
	int MOBREQUEST_STATUS_FAILURE = 2;
	
	/** The webrequest status logged. */
	int WEBREQUEST_STATUS_LOGGED = 0;
	
	/** The webrequest status success. */
	int WEBREQUEST_STATUS_SUCCESS = 1;
	
	/** The webrequest status failure. */
	int WEBREQUEST_STATUS_FAILURE = 2;
	
	/** The txn no error. */
	int TXN_NO_ERROR = 2000;
	
	int TXN_REVERSAL = 10;
	
	/** The tfa disabled. */
	int TFA_DISABLED = 0 ;
	
	/** The tfa enabled. */
	int TFA_ENABLED = 1 ;
	
	/** The login attempts. */
	int LOGIN_ATTEMPTS=0;
	
	/** The default page. */
	int DEFAULT_PAGE=1;
	
	/** The book type customer. */
	int BOOK_TYPE_CUSTOMER=1;
	
	/** The book type bank. */
	int BOOK_TYPE_BANK=2;
	
	/** The book type eot. */
	int BOOK_TYPE_EOT=3;
	
	/** The inactive bank. */
	int INACTIVE_BANK=0;
	
	/** The txn pending. */
	int TXN_PENDING=0;
	
	/** The txn approve. */
	int TXN_APPROVE=1;
	
	/** The txn reject. */
	int TXN_REJECT=2;
	
	/** The txn cancel. */
	int TXN_CANCEL=3;
	
	/** The new web user. */
	String NEW_WEB_USER="0";
	
	/** The active web user. */
	String ACTIVE_WEB_USER="1";
	
	/** The journal type. */
	int JOURNAL_TYPE=1;
	
	/** The chpool file format. */
	String CHPOOL_FILE_FORMAT="MT971";
	
	/** The pending. */
	int PENDING =0;
	
	/** The approved. */
	int APPROVED =1;
	
	/** The rejected. */
	int REJECTED =2;
	
	/** The canceled. */
	int CANCELED =3;
	
	/** The default bank. */
	Integer DEFAULT_BANK = 1;
	
	/** The default bank. */
	Integer DEFAULT_COUNTRY = 1;
	
	/** The default branch. */
	Long DEFAULT_BRANCH = 1L;
	
	/** The active. */
	Integer ACTIVE = 10;

	Integer BANK_UPLOAD_FILE_TEMPLETE = 1;

	Integer UPLOAD_VOUCHERS_TEMPLETE_ID = 2;

	Integer TRANSACTION_SUPPORT_TEMPLATE_ID = 3;

	Integer MOBILE_MENU_TAB=1;

	Integer MOBILE_MENU_GRID=2;

	String REINSTALLED_APP_SUCCESS = "New download link sent to the customer successfully";
	
	String BANK_THEME = "bankTheme";

	String BANK_BTN_COLOR = "buttonColor";

	String BANK_LOGO = "logo";

	String BANK_DEFAULT_THEME = "237827";
	
	String BANK_DEFAULT_BTN = "2d6ca2";
	
	String BANK_DEFAULT_LOGO="Core-Wallet";

	String BANK_MENU_COLOR = "sideMenuColor";

	String BANK_DEFAULT_MENU_COLOR = "3E4E68";

	String BANK_SUB_MENU_COLOR = "sideSubMenuColor";
	
	String BANK_DEFAULT_SUB_MENU_COLOR = "3E4E68";
	
	/*Author - Murari -- Date - 13-07-2018 ---Added Account Type --- start ---*/
	/** The account type personal. */
	String ACC_TYPE_PERSONAL = "Personal";
	
	/** The account type nominal. */
	String ACC_TYPE_NOMINAL = "Nominal";
	
	/** The account type real. */
	String ACC_TYPE_REAL = "Real";
	/*---end---*/
	
	Integer PARTNER_TYPE_BA_ADMIN=2;
	Integer PARTNER_TYPE_BA_PARTNER_L1=3;
	Integer PARTNER_TYPE_BA_PARTNER_L2=4;
	Integer PARTNER_TYPE_BA_PARTNER_L3=5;
	
	Integer BUSINESS_PARTNER_TYPE_L1=1;
	Integer BUSINESS_PARTNER_TYPE_L2=2;
	Integer BUSINESS_PARTNER_TYPE_L3=3;
	
	int ACCOUNT_HEAD_BANK_REV_ACCOUNT=55;
	
	int STATUS_COMMISSION_SUCCESS=10;
	
	int STATUS_COMMISSION_FAIL=99;
	int STATUS_COMMISSION_UNKNOWN=98;
	int ACCOUNT_HEAD_ID_COMMISSION_SHARE = 105;
	
	/*Excel Wrapper Constants*/
	
	String WORKBOOK_SHEET_NAME= "m-GURUSH";
	String WORKBOOK_SHEET_NAME_NEW = "mGurush";
	
	/*Jasper Reports Constants*/
	
	String JASPER_FILE_DATE_FORMAT = "ddMMyyyyHHmmss";
	String JASPER_PATH = "jasperPath";
	
	String JASPER_LOCATION_JRXML_NAME = "location_report_en_US.jrxml";
	String LOCATION_REPORT_NAME = "locations";
	
	String JASPER_BUSINESS_PARTNER_JRXML_NAME = "business_partner_report_en_US.jrxml";
	String BULKPAYEMNT_PARTNER_JRXML_NAME = "bulkpayment_partner_en_US.jrxml";
	String SUPER_AGENT_REPORT_NAME = "super_agent";
	String PRINCIPAL_AGENT_REPORT_NAME = "principal_agent";
	String BULKAPAYMENT_PARTNER_REPORT_NAME = "bulkpayment partner";
	
	String JASPER_BUSINESS_PARTNER_COMMISSION_JRXML_NAME = "business_partner_commision_report_en_US.jrxml";
	String BUSINESS_PARTNER_COMMISSION_REPORT_NAME = "business_partner_commission";
	
	String JASPER_MIS_JRXML_NAME = "mis_details_report_en_US.jrxml";
	String MIS_REPORT_NAME = "mis_report";
	
	String JASPER_MERCHANT_COMMISSION_JRXML_NAME = "merchant_commission_details_report_en_US.jrxml";
	String MERCHANT_COMMISSION_REPORT_NAME = "merchant_commission_report";
	
	String JASPER_CUS_REG_COMM_JRXML_NAME = "cus_reg_report_en_US.jrxml";
	
	String JASPER_WEB_USER_JRXML_NAME = "web_user_details_report_en_US.jrxml";
	String WEB_USER_REPORT_NAME = "web_user_details_report";
	
	String JASPER_SCR_JRXML_NAME = "scr_details_report_en_US.jrxml";
	String SCR_REPORT_NAME = "scr_details_report";
	
	String JASPER_TXN_RULE_JRXML_NAME = "txn_rule_details_report_en_US.jrxml";
	String TXN_REPORT_NAME = "txn_rule_details_report";
	
	String JASPER_CUSTOMER_DETAILS_JRXML_NAME = "customers_details_report_en_US.jrxml";
	String JASPER_AGENTS_DETAILS_JRXML_NAME = "agent_details_report_en_US.jrxml";
	
	String AGENT_DETAILS_REPORT_NAME = "agent_details_report";
	String CUSTOMER_DETAILS_REPORT_NAME = "customer_details_report";
	String SOLE_MERCHANT_DETAILS_REPORT_NAME = "merchant_details_report";
	String AGENT_SOLE_MERCHANT_DETAILS_REPORT_NAME = "agent_sole_merchant_details_report";
	
	String AGENT_DETAILS_PAGE_HEADER ="AGENT_DETAILS";
	String SOLE_MERCHANT_PAGE_HEADER ="SOLE_MERCHANT_DETAILS";
	String AGENT_SOLE_MERCHANT_PAGE_HEADER ="AGENT_SOLEMERCHANT_DETAILS";
	String CUSTOMER_PAGE_HEADER ="CUSTOMER_DETAILS";
	String WEB_USER_PAGE_HEADER= "WEB_USER_DETAILS";
	String BUSINESS_PARTNER_DETAILS_PAGE_HEADER= "BUSINESS_PARTNER_DETAILS";
	String PRINCIPAL_AGENT_DETAILS_PAGE_HEADER= "PRINCIPAL_AGENT_DETAILS";
	String MIS_DETAILS_PAGE_HEADER= "MIS_DETAILS";
	String SCR_DETAILS_PAGE_HEADER= "SERVICE_CHARGE_RULE_DETAILS";
	String TXR_DETAILS_PAGE_HEADER= "TRANSACTION_RULE_DETAILS";
	String LOCATION_DETAILS_PAGE_HEADER= "LOCATION_DETAILS";
	String BUSINESS_PARTNER_COMMISSION_DETAILS_PAGE_HEADER= "BUSINESS_PARTNER_COMMISSION_DETAILS";
	String BULK_PAY_TXN_PAGE_HEADER= "BULK_PAY_TXN";
	String BULK_PAY_TXN_REPORT_NAME = "bulk_pay_txn";
	String MERCHANT_COMMISSION_DETAILS_PAGE_HEADER= "MERCHANT_COMMISSION_DETAILS";
	
	String SUPER_AGENT_DETAILS_PAGE_HEADER_PDF= "Super Agent Details";
	String PRINCIPAL_AGENT_DETAILS_PAGE_HEADER_PDF= "Principal Agent Details";
	String CUS_REG_COMM_DETAILS_HEADER = "CUS_REG_COMM_DETAILS_";
	String BULKPARTNER_DETAIL_PAGE_HEADER_PDF = "BulkPayemnt Details";
	String BULKPARTNER_DETAIL_DETAILS_PAGE_HEADER= "BULK_PAYMENT_DETAILS";
	
	Integer TXN_TYPE_LIMIT_UPDATE=121;
	
	String BANK_ADMIN_INITIAL="mgurushadm";
	String BANK_SUPERVISOR_INITIAL = "mgurushsup";
	String BANK_ACCOUNTING_INITIAL = "mgurushacn";
	String BANK_SUPPORT_INITIAL    = "mgurushscr";
	String BANK_OPERATION_INITIAL  = "mgurushopn";
	String BANK_COMMERCIAL_OFF_INITIAL = "mgurushcmo";
	String BANK_SUPPORT_CALL_INITIAL   = "mgurushscc";
	
	String SSP_CURRENCY_CODE="SSP";
	
	String ACTION_EXPORT="Export";
	
	String ACTION_SEARCH="Search";
	
	String FATAL_ERROR = "System error";
	String MISSING_PARAMETERS_ERROR = "System error";
	String INVALID_PARAMETERS_ERROR = "System error";
	String MERCHANT_ACC_SAME_CUST_ACC_ERROR= "Merchant Account and Customer Account same Error";
	String OPERATION_NOT_SUPPORTED_ERROR = "Operation not supported Error";
	String SETTLEMENT_FILE_GENERATION_ERROR = "Settlement File generation Error";
	String MERCHANT_NOT_ENOUGH_BALANCE_ERROR = "Merchant does not have enough balance";
	String CUSTOMER_NOT_ENOUGH_BALANCE_ERROR = "Customer does not have enough balance";
	String PAYEE_NOT_ENOUGH_BALANCE_ERROR = "Payee account does not have enough balance";
	String NO_VOUCHER_AVAILABLE_ERROR = "Voucher not available";
	String SERVICE_CHARGE_NOT_DEFINED_ERROR = "Service Charge not defined";
	String TXN_RULE_NOT_DEFINED_ERROR = "Transaction Rule not defined";
	String TXN_LIMIT_EXCEEDED_ERROR = "Transaction amount exceeds the allowed limit";
	String CUM_TXN_LIMIT_EXCEEDED_ERROR = "Cumulative Transaction Limit Exceeded";
	String TXN_NUM_EXCEEDED_ERROR = "Number of transactions allowed for the period exceeded";
	String INVALID_TXN_RULE_ERROR = "Invalid Transaction Rule";
	String NO_BALANCE_IN_WALLET_ERROR = "No Balance in Wallet";
	String TXN_DECLINED_FROM_HPS_ERROR = "Transaction Declined from HPS";
	String INVALID_CH_POOL_ERROR = "Invalid CH-Pool";
	String UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR = "Unable to connect to Eot Card System";
	String SETTLED_TRANSACTION_ERROR = "Transaction is already settled,so cannot be reversed";
	String PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR = "Account balance exceeded,Please contact customer care";
	String TXN_DETAILS_NOT_AVAILABLE_ERROR = "Transaction Details not available";
	String BALANCE_DETAILS_NOT_AVAILABLE_ERROR = "Balance Details not available";
	String CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR = "Customer and Merchant not in same Bank";
	String INVALID_SIGNATURE_SIZE_ERROR = "Invalid Signature size";
	String INVALID_IDPROOF_SIZE_ERROR = "Invalid Id-Proof size";
	String MOBILE_NUMBER_REGISTERED_ALREADY_ERROR = "Mobile Number Registered already";
	String REVERSAL_STATUS = "Reverted";
	
	
	String BUSINESS_PARTNER_COMMISSION_ACCOUNT_ALIAS="Partner Commission Account";
	public static final String SETTLED_TRANSACTION = "Transaction already settled";
	
	public static Integer TXN_INITIATE_ADJUSTMENT = 11;
	
	int ACCESS_MODE_WEB = 10;
	int ACCESS_MODE_MOBILE = 20;
	int ACCESS_MODE_ALL = 30;
	
	int ROLE_ID_SUPPORT = 11;	
	
	Integer APPROVED_CUSTOMER_PROFILE = 2;
	
	int KYC_STATUS_PENDING=0;
	int KYC_STATUS_APPROVE_PENDING=1;
	int KYC_STATUS_APPROVED=11;
	int KYC_STATUS_REGEJETED=21;

	int TXN_ID_CASH_IN = 137;
	int TXN_ID_TRANSFER_EMONEY = 143;
	int TXN_ID_CASH_OUT = 138;
	Integer COUNTRY_SOUTH_SUDAN = 1;
	Integer CUSTOMER_TYPE_CUSTOMER = 0;
	String COUNTRY_CODE_SOUTH_SUDAN = "211";
	
	int TXN_ID_MERCHANT_PAY_OUT = 140;

	String TYPE_CUSTOMER = "Customer";
	String TYPE_AGENT = "Agent";
	String TYPE_MERCHANT = "Merchant";
	
	String APP_TYPE_CUSTOMER = "0eb49y";
	String APP_TYPE_AGENT = "br159y";
	String APP_TYPE_MERCHANT = "as159y";
	
	Integer BULKPAYMENT_PARTNER_TYPE = 4;
	
	int TXN_ID_BULK_PAY = 141;
	int API_STATUS_NEW = 0;
	int API_STATUS_SUCCESS = 10;
	int API_STATUS_FAIL = 99;
	
	String BULK_PAYMENT_PROCESSED ="Processed";
	String BULK_PAYMENT_UPLOADED ="Uploaded";
	String DEFAULT_LOCALE = "en_us";
	int TXN_ID_ACCOUNT_TRANSFER = 144;
	
	String REFRESH_BUTTON_FLOAT_BALANCE ="1";
	String REFRESH_BUTTON_COMMISSION_BALANCE ="2";
	
	String BUSINESS_PARTNER_L1 = "Principal Agent Admin";
	String BUSINESS_PARTNER_L2 = "Super Agent";
	String BULKPAYMENT_PARTNER = "BulkPay Partner Admin";
	
	String BANK_FLOAT_DEPOSIT_DETAILS_PAGE_HEADER= "FLOAT_DEPOSIT_DETAILS";
	String JASPER_BANK_FLOAT_DEPOSIT_DETAILS_JRXML_NAME = "bank_float_deposit_details_report_en_US.jrxml";
	String BANK_FLOAT_DEPOSIT_REPORT_NAME = "bank_float_deposit_report";
	
	String NON_REG_USSD_CUSTOMER_DETAILS_PAGE_HEADER= "NON_REG_USSD_CUSTOMER_DETAILS";
	String JASPER_NON_REG_USSD_CUSTOMER_DETAILS_JRXML_NAME = "non_reg_ussd_customer_details_report_en_US.jrxml";
	String NON_REG_USSD_CUSTOMER_REPORT_NAME = "non_reg_ussd_customer_report";
	
	String TRANSACTION_VOLUME_REPORT_DETAILS_PAGE_HEADER= "TRANSACTION_VOLUME_REPORT_DETAILS";
	String JASPER_TRANSACTION_VOLUME_REPORT_DETAILS_JRXML_NAME = "transaction_volume_report_details_report_en_US.jrxml";
	String TRANSACTION_VOLUME_REPORT_NAME = "transaction_volume_report";
	
	String BLOCKED = "Blocked";
	String REPORT_DETAILS = "Report_Details";
	String BLOCKED_APPLICATION_REPORT = "blocked_application";
	
	Integer CUSTOMER_STATUS_SUSPENDED = 3;
	
	String TARGET_TYPE_ALL = "All";
	String TARGET_TYPE_PRINCIPAL_AGENT = "PrincipalAgent";
	String TARGET_TYPE_SUPER_AGENT = "SuperAgent";
	String TARGET_TYPE_MERCHANT = "Merchant";
	String TARGET_TYPE_AGENT = "Agent";
	String TARGET_TYPE_CUSTOMER = "Customer";
	String TARGET_TYPE_CUSTOM = "Custom";
	Integer MESSAGE_TYPE_BULK_MESSAGE = 125;
	String SMS_CAMPAIGN_ADDED_SUCCESSFULLY = "SMS Campaign added successfully";
	
	Integer TRANSACTION_INITIATED_FOR_APPROVAL = 100;
	Integer TRANSACTION_STATUS_SUCCESS = 101;
	Integer TRANSACTION_STATUS_FAIL = 105;
	Integer TRANSACTION_STATUS_REJECTED = 107;
	String TXN_TYPE_AGENT_CASH_OUT= "138";
	/** The reference type cash out. */
	int REFERENCE_TYPE_CASH_OUT= 4;
}