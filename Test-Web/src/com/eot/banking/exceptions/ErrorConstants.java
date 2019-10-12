/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: ErrorConstants.java
*
* Date Author Changes
* 24 May, 2016 Swadhin Created
*/
package com.eot.banking.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Interface ErrorConstants.
 */
public interface ErrorConstants {
	
	/** The invalid bank. */
	String INVALID_BANK = "ERROR_8001";
	
	/** The invalid branch file. */
	String INVALID_BRANCH_FILE = "ERROR_8002";
	
	/** The invalid clearing house. */
	String INVALID_CLEARING_HOUSE = "ERROR_8003";
	
	/** The invalid stake holder. */
	String INVALID_STAKE_HOLDER = "ERROR_8004";
	
	/** The invalid bank sc perc. */
	String INVALID_BANK_SC_PERC = "ERROR_8005";
	
	/** The invalid stakeholder sc perc. */
	String INVALID_STAKEHOLDER_SC_PERC = "ERROR_8006";
	
	/** The invalid branch. */
	String INVALID_BRANCH = "ERROR_8007";
	
	/** The insufficient privileges. */
	String INSUFFICIENT_PRIVILEGES = "ERROR_8008";
	
	/** The invalid operator. */
	String INVALID_OPERATOR = "ERROR_8009";
	
	/** The invalid denomination. */
	String INVALID_DENOMINATION = "ERROR_8010";
	
	/** The invalid country. */
	String INVALID_COUNTRY="ERROR_8011";
	
	/** The invalid city. */
	String INVALID_CITY="ERROR_8012";
	
	/** The invalid quarter. */
	String INVALID_QUARTER="ERROR_8013";
	
	/** The operator name exist. */
	String OPERATOR_NAME_EXIST="ERROR_8014";	
	
	/** The stakeholder name exist. */
	String STAKEHOLDER_NAME_EXIST="ERROR_8015";
	
	/** The clearinghouse name exist. */
	String CLEARINGHOUSE_NAME_EXIST="ERROR_8016";
	
	/** The txn rule already defined. */
	String TXN_RULE_ALREADY_DEFINED = "ERROR_8017";
	
	/** The country name exist. */
	String COUNTRY_NAME_EXIST = "ERROR_8018";
	
	/** The city name exist. */
	String CITY_NAME_EXIST = "ERROR_8019";
	
	/** The quarter name exist. */
	String QUARTER_NAME_EXIST = "ERROR_8020";
	
	/** The invalid txn type. */
	String INVALID_TXN_TYPE = "ERROR_8021";
	
	/** The invalid voucher file. */
	String INVALID_VOUCHER_FILE="ERROR_8022";
	
	/** The invalid voucher values. */
	String INVALID_VOUCHER_VALUES="ERROR_8023";
	
	/** The operator inactive. */
	String OPERATOR_INACTIVE ="ERROR_8024";
	
	/** The invalid bankgroup. */
	String INVALID_BANKGROUP="ERROR_8030";
	
	/** The bankgroup name exist. */
	String BANKGROUP_NAME_EXIST="ERROR_8031";
	
	// change vineeth, 16-07-2018, bugno: 5696
	/** The bankgroup name exist. */
	String BANKGROUPSHORT_NAME_EXIST="ERROR_9931";
	// change over
	
	/** The currency code exist. */
	String CURRENCY_CODE_EXIST="ERROR_8032";
	
	/** The invalid currency. */
	String INVALID_CURRENCY="ERROR_8033";
	
	/** The invalid groupadmin. */
	String INVALID_GROUPADMIN="ERROR_8034";
	
	/** The invalid biller. */
	String INVALID_BILLER="ERROR_8035";
	
	/** The biller name exist. */
	String BILLER_NAME_EXIST="ERROR_8036";
	
	/** The branch code exist. */
	String BRANCH_CODE_EXIST="ERROR_8037";
	
	/** The duplicate branch code. */
	String DUPLICATE_BRANCH_CODE="ERROR_8038";
	
	/** The country code exist. */
	String COUNTRY_CODE_EXIST="ERROR_8039";
	
	/** The branch code length. */
	String BRANCH_CODE_LENGTH="ERROR_8040";
	
	/* vineeth changes, on 07-08-2018, bug no: 5847*/
	
	/** The branch address length. */
	String BRANCH_ADDRESS_LENGTH="ERROR_8763";
	
	// vineeth changes over
	
	/** The currency code numeric exist. */
	String CURRENCY_CODE_NUMERIC_EXIST="ERROR_8041";
	
	/** The COUNTR y_ cod e3_ exist. */
	String COUNTRY_CODE3_EXIST="ERROR_8042";
	
	/** The country code numeric exist. */
	String COUNTRY_CODE_NUMERIC_EXIST="ERROR_8043";
	
	/** The country isdcode exist. */
	String COUNTRY_ISDCODE_EXIST="ERROR_8044";
	
	/** The nodata available. */
	String NODATA_AVAILABLE="ERROR_8045";
	
	/** The version number exist. */
	String VERSION_NUMBER_EXIST="ERROR_8046";
	
	/** The pending txn not avialable. */
	String PENDING_TXN_NOT_AVIALABLE="ERROR_8047";
	
	/** The invalid branch code values. */
	String INVALID_BRANCH_CODE_VALUES = "ERROR_8048";
	
	/** The invalid customer. */
	String INVALID_CUSTOMER= "ERROR_1016"; 
	
	/** The inactive customer. */
	String INACTIVE_CUSTOMER= "ERROR_1041"; 
	
	/** The inactive customer. */
	String INACTIVE_AGENT= "ERROR_1087"; 
	
	/** The invalid customer account. */
	String INVALID_CUSTOMER_ACCOUNT= "ERROR_1017";
	
	/** The customer profile exist. */
	String CUSTOMER_PROFILE_EXIST= "ERROR_1028";
	
	/** The invalid bank account. */
	String INVALID_BANK_ACCOUNT= "ERROR_1018";
	
	/** The mobile number registered already. */
	String MOBILE_NUMBER_REGISTERED_ALREADY = "ERROR_1019";
	
	/** The mobile number registered already. */
	String BUSINESS_PARTNER_NAME_REGISTERED_ALREADY = "ERROR_1127";
	
	/** The non numeric value. */
	String NON_NUMERIC_VALUE = "ERROR_1020";
	
	/** The bank code exist. */
	String BANK_CODE_EXIST = "ERROR_1021";
	
	/** The swift code exist. */
	String SWIFT_CODE_EXIST = "ERROR_1050";
	
	/** The inactive bank. */
	String INACTIVE_BANK= "ERROR_9024";
	
	/** The invalid ch pool. */
	String INVALID_CH_POOL= "ERROR_1022"; 
	
	/** The invalid ch pool. */
	String INVALID_CH_POOL_AGENT= "ERROR_1211"; 
	
	/** The denomination exist. */
	String DENOMINATION_EXIST="ERROR_1023";
	
	/** The invalid application. */
	String INVALID_APPLICATION="ERROR_1024";
	
	/** The no txns found. */
	String NO_TXNS_FOUND = "ERROR_1025";
	
	/** The no txns found. */
	String NO_RECORDS_FOUND = "ERROR_1335";
	
	String NO_REVERSAL_TXNS_FOUND = "ERROR_1051";
	/** The no txns found for period. */
	String NO_TXNS_FOUND_FOR_PERIOD = "ERROR_1026";
	
	/** The core connection error. */
	String CORE_CONNECTION_ERROR = "ERROR_1027";
	
	/** The invalid signature size. */
	String INVALID_SIGNATURE_SIZE = "ERROR_1030";
	
	/** The invalid idproof size. */
	String INVALID_IDPROOF_SIZE = "ERROR_1031";
	
	/** The service charge not defined. */
	String SERVICE_CHARGE_NOT_DEFINED= "ERROR_6001";
	
	/** The txn rule not defined. */
	String TXN_RULE_NOT_DEFINED= "ERROR_7001";
	
	/** The smslog unavailable. */
	String SMSLOG_UNAVAILABLE = "ERROR_9001";
	
	/** The invalid user. */
	String INVALID_USER= "ERROR_4001";
	
	/** The user name exist. */
	String USER_NAME_EXIST= "ERROR_4002";
	
	/** The password not valid. */
	String PASSWORD_NOT_VALID= "ERROR_4003";
	
	/** The invalid otp. */
	String INVALID_OTP="ERROR_4004";
	
	/** The invalid teller. */
	String INVALID_TELLER= "ERROR_3001";
	
	/** The invalid teller. */
	String INVALID_TERMINAL= "ERROR_1015";
	
	/** The user blocked. */
	String USER_BLOCKED = "ERROR_8025";
	
	/** The credentials expired. */
	String CREDENTIALS_EXPIRED = "ERROR_8026";
	
	/** The invalid credentials. */
	String INVALID_CREDENTIALS = "ERROR_8027";
	
	/** The invalid request. */
	String INVALID_REQUEST = "ERROR_4005";
	
	/** The service error. */
	String SERVICE_ERROR= "ERROR_9999";
	
	String SYSTEM_ERROR = "System Error.Please Try Again.";
	/** The db error. */
	String DB_ERROR="ERROR_9998";
	
	/** The no topup found. */
	String NO_TOPUP_FOUND="ERROR_8028";
	
	/** The controlfile txn not found. */
	String CONTROLFILE_TXN_NOT_FOUND="ERROR_9001";
	
	/** The bill not found. */
	String BILL_NOT_FOUND="ERROR_9002";
	
	/** The sc rule exist. */
	String SC_RULE_EXIST = "ERROR_9010";
	
	/** The sc applicable date exist. */
	String SC_APPLICABLE_DATE_EXIST = "ERROR_9011";
	
	/** The sc txn value range exist. */
	String SC_TXN_VALUE_RANGE_EXIST = "ERROR_9012";
	
	/** The tr rule exist. */
	String TR_RULE_EXIST = "ERROR_9013";
	
	/** The max cumval exceed week. */
	String MAX_CUMVAL_EXCEED_WEEK = "ERROR_9014";
	
	/** The max nooftimes exceed week. */
	String MAX_NOOFTIMES_EXCEED_WEEK = "ERROR_9015";
	
	/** The max cumval exceed month. */
	String MAX_CUMVAL_EXCEED_MONTH = "ERROR_9016";
	
	/** The max nooftimes exceed month. */
	String MAX_NOOFTIMES_EXCEED_MONTH = "ERROR_9017";
	
	/** The max cumval greater day. */
	String MAX_CUMVAL_GREATER_DAY = "ERROR_9018";
	
	/** The max nooftimes greater day. */
	String MAX_NOOFTIMES_GREATER_DAY = "ERROR_9019";
	
	/** The max cumval greater week. */
	String MAX_CUMVAL_GREATER_WEEK = "ERROR_9020";
	
	/** The max nooftimes greater week. */
	String MAX_NOOFTIMES_GREATER_WEEK = "ERROR_9021";
	
	/** The customer card exist. */
	String CUSTOMER_CARD_EXIST = "ERROR_9022";
	
	/** The invalid date. */
	String INVALID_DATE = "ERROR_9023";
	
	/** The global rule not found. */
	String GLOBAL_RULE_NOT_FOUND = "ERROR_9030";
	
	/** The group rule not found. */
	String GROUP_RULE_NOT_FOUND = "ERROR_9031";
	
	/** The global rule max value exceed. */
	String GLOBAL_RULE_MAX_VALUE_EXCEED = "ERROR_9032";
	
	/** The global rule max cum value exceed. */
	String GLOBAL_RULE_MAX_CUM_VALUE_EXCEED = "ERROR_9033";
	
	/** The global rule max num times exceed. */
	String GLOBAL_RULE_MAX_NUM_TIMES_EXCEED = "ERROR_9034";
	
	/** The group rule max value exceed. */
	String GROUP_RULE_MAX_VALUE_EXCEED = "ERROR_9035";
	
	/** The group rule max cum value exceed. */
	String GROUP_RULE_MAX_CUM_VALUE_EXCEED = "ERROR_9036";
	
	/** The group rule max num times exceed. */
	String GROUP_RULE_MAX_NUM_TIMES_EXCEED = "ERROR_9037";
	
	/** The no entries found. */
	String NO_ENTRIES_FOUND = "ERROR_9038";
	

	/** The approval pending. */
	String APPROVAL_PENDING = "ERROR_9051";
	
	/** The not authorize pending txn. */
	String NOT_AUTHORIZE_PENDING_TXN = "ERROR_9052";
	
	/** The branch location exist. */
	String BRANCH_LOCATION_EXIST = "ERROR_9053";
	
	String BRANCH_NAME_EXIST = "ERROR_9050";

	/** The duplicate branch location. */
	String DUPLICATE_BRANCH_LOCATION="ERROR_8054";
	
	/** The bank not exist. */
	String BANK_NOT_EXIST="ERROR_9054";
	
	/** The branch not exist. */
	String BRANCH_NOT_EXIST="ERROR_9055";
	
	/** The biller not exist. */
	String BILLER_NOT_EXIST="ERROR_9056";
	
	/** The operator not exist. */
	String OPERATOR_NOT_EXIST = "ERROR_9057";
	
	/** The eot card bank code exist. */
	String EOT_CARD_BANK_CODE_EXIST = "ERROR_9058";

	/** The settlement journal not exist. */
	String SETTLEMENT_JOURNAL_NOT_EXIST = "ERROR_9059";
	
	/** The bank name exist. */
	String BANK_NAME_EXIST = "ERROR_9060";
	
	/** The currency name exist. */
	String CURRENCY_NAME_EXIST = "ERROR_9061";
	
	/** The web user not exist. */
	String WEB_USER_NOT_EXIST = "ERROR_9062";
	
	/** The clearing house not exist. */
	String CLEARING_HOUSE_NOT_EXIST = "ERROR_9063";
	
	/** The settlement data not available. */
	String SETTLEMENT_DATA_NOT_AVAILABLE = "ERROR_9064";
	
	/** The cancelled transaction. */
	String CANCELLED_TRANSACTION = "ERROR_9065"; 
	
	/** The processed transaction. */
	String PROCESSED_TRANSACTION = "ERROR_9066";
	
	/** The invalid txn id. */
	String INVALID_TXN_ID = "ERROR_9080";
	
	/** The invalid adjusted amount. */
	String INVALID_ADJUSTED_AMOUNT = "ERROR_9081";
	
	/** The invalid adjusted fee. */
	String INVALID_ADJUSTED_FEE = "ERROR_9082";
	
	/** The invalid adjusted txntype. */
	String INVALID_ADJUSTED_TXNTYPE = "ERROR_9083";
	
	/** The invalid comment. */
	String INVALID_COMMENT = "ERROR_9084";
	
	/** The reversal txn error. */
	String REVERSAL_TXN_ERROR = "ERROR_9085";
	
	/** The cbs account notexist. */
	String CBS_ACCOUNT_NOTEXIST = "ERROR_10001";
	
	/** The cbs account already exist. */
	String CBS_ACCOUNT_ALREADY_EXIST = "ERROR_10002";
	
	/** The invalid bank customer id. */
	String INVALID_BANK_CUSTOMER_ID = "ERROR_10003";
	
	/** The external txn notavialable. */
	String EXTERNAL_TXN_NOTAVIALABLE = "ERROR_10004";
	
	/** The invalid card details. */
	String INVALID_CARD_DETAILS = "ERROR_10005";
	
	/** The invalid mobile number. */
	String INVALID_MOBILE_NUMBER = "ERROR_10006";
	
	/** The invalid merchant. */
	String INVALID_MERCHANT = "ERROR_10007";
	
	/** The invalid outlet. */
	String INVALID_OUTLET = "ERROR_10008";

	/** The terminal registered already. */
	String TERMINAL_REGISTERED_ALREADY = "ERROR_10009";
	
	/** The provenience name exist. */
	String PROVENIENCE_NAME_EXIST = "ERROR_10010";
	
	/** The invalid provenience. */
	String INVALID_PROVENIENCE = "ERROR_10011";

	/** The sms alert rule exist. */
	String SMS_ALERT_RULE_EXIST = "ERROR_10012";

	/** The remittance company exist. */
	String REMITTANCE_COMPANY_EXIST = "ERROR_10013";

	String EMAIL_ALREADY_EXISTS = "ERROR_10014";
	
	String THEME_NOT_EXISTS="ERROR_7001";
	
	String BANK_CONFIG_ALREADY_EXISTS = "ERROR_8001";
	
	String BANK_PROFILE_CONFIG_ALREADY_EXISTS = "ERROR_8002";

	String COMMISSION_ALREDY_EXISTS = "ERROR_10015";
	
	// ADDED BY VINEETH
	String INVALID_AGENT = "ERROR_10025";
	
	/** The invalid AGENT account. */
	String INVALID_AGENT_ACCOUNT= "ERROR_1217";
	
	String INVALID_SOLEMERCHANT = "ERROR_10026";
	
	String INVALID_AGENT_SOLEMERCHANT = "ERROR_10027";
	
	String BP_CODE_ALREADY_EXISTS = "ERROR_10028";
	
	String INVALID_BUSINESS_PARTNER = "ERROR_10029";
	
	String INVALID_PRINCIPAL_AGENT = "ERROR_10030";
	
	String GENERIC_EXC_MESSAGE = "ERROR_778899";
	
	String SC_RULE_ALREADY_EXIST = "ERROR_10030";
	
	String CUST_ACCOUNT_NOT_FOUND = "ERROR_20030";
	
	String INVALID_CUSTOMER_OTP = "ERROR_6020";
	
	String SMS_ALERT_FAILED = "ERROR_6018";
	
	String PROBLEM_LOADING_BUSINESS_PARTNER="ERROR_10031";
	
	String MOBILE_NO_EMPTY = "ERROR_10017";
	
	String  ACC_ALIAS_EMPTY = "ERROR_10009"	;
	
	String AMT_EMPTY ="ERROR_10015";
	
	String  SERVICE_CHARGE_MAX_TXN="ERROR_2024";
	
	/** The invalid voucher file. */
	String INVALID_COLUMN_Sl_No="ERROR_8372";
	
	/** The invalid voucher file. */
	String INVALID_COLUMN_Mobile_Number="ERROR_8373";
	
	/** The invalid voucher file. */
	String INVALID_COLUMN_Account_Alias="ERROR_8374";
	
	/** The invalid voucher file. */
	String INVALID_COLUMN_Name="ERROR_8375";
	
	/** The invalid voucher file. */
	String INVALID_COLUMN_Amount="ERROR_8376";
	
	/** The invalid voucher file. */
	String INVALID_COLUMN_Description="ERROR_8377";
	
	String DUPLICATE_MOBILE_NUMBER = "ERROR_2041";
	
	String  AGENT_ACC_DEACTIVATED = "ERROR_2042";
	
	String AGENT_ACC_BLOCKED = "ERROR_2043";
	
	/** The Merchant Code registered already. */
	String MERCHANT_CODE_REGISTERED_ALREADY = "ERROR_1167";

	/** The invalid customer. */
	String INVALID_AGENT_CASH_OUT= "ERROR_10128"; 
	
	String INVALID_AGENT_OTP = "ERROR_6033";
	
}
