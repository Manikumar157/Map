package com.eot.banking.common;

public interface UrlConstants {
	final static String PROTOCOL="http://";
	final static String HTTP_PORT="7070";
	final static String HOST="localhost";
	final static String BASE_URL="/EOT-SMS";
	
	final static String NEW_TXN_PIN_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleNewTxnPinAlertRequest";
	final static String NEW_LOGIN_PIN_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleNewLoginPinAlertRequest";
	final static String INITIAL_LOGIN_AND_TXN_PIN_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleInitialTxnPinLoginPinAlertRequest";
	final static String APP_LINGK_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleAppLinkAlertRequest";
	
	final static String WEB_USERNAME_PASSWORD_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleWebUsernamePasswordAlertRequest";
	final static String WEB_RESET_PASSWORD_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleWebResetPasswordAlertRequest";
	final static String WEB_OTP_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleWebOTPAlertRequest";
	final static String CREDIT_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleCreditAlertRequest";
	final static String DEBIT_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleDebitAlertRequest";
	final static String WEEKLY_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleWeeklyAlertRequest";
	final static String ADD_CARD_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleAddCardAlertRequest";
	final static String SMS_CASH_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleSMSCashAlertRequest";

	final static String RESET_TXN_PIN_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleResetTxnPinAlertRequest";
	final static String RESET_LOGIN_PIN_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleResetLoginPinAlertRequest";
	
	final static String BLOCKED_APP_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleBlockedAppAlertRequest";
	final static String MOBILE_APP_USER_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleMobileAppUserAlertRequest";
	final static String CUSTOM_MESSAGE_ALERT=PROTOCOL+HOST+":"+HTTP_PORT+BASE_URL+"/services/rest/SmsServices/handleCustomMsgAlertRequest";
	
	

}
