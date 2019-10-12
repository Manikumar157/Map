package com.eot.banking.common;

public interface CoreUrls {
	final static String PROTOCOL="http://";
	final static String HTTP_PORT="7070";
	final static String HOST="localhost";
	
	final static String BALANCE_ENQ_WALLET=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/basicBanking/balanceEnquiry";
	final static String MINI_STATEMENT=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/basicBanking/miniStatement";
	final static String TRANSACTION_STATEMENT=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/basicBanking/transactionStatement";
	
	
	final static String DEPOSITE_TXN_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/deposit";
	final static String WITHDRAWAL_TXN_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/withdrawal";
	final static String TRANSFER_DIRECT_TXN_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/transferDirect";
	final static String VOID_TXN_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/voidTransaction";
	final static String SMS_CASH_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/smsCash";
	final static String ADJUSTMENT_TXN_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/adjustmentTransaction";
	final static String COMMISSION_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/commissionShare";
	final static String SALE_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/sale";
	final static String LIMIT_POSTING_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/limitUpdate";
	final static String TRANSFER_EMONEY_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/transferEmoney";
	
	final static String SERVICE_CHARGE_DEBIT_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/utility/serviceChargeDebitReq";
	final static String BILL_PAYMENT_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/utility/billPayment";
	final static String TOPUP_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/utility/topUp";
	
	final static String INITIATE_ADJUSTMENT_TXN_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/initiateAdjustmentTransaction";
	final static String CUSTOMER_APPROVAL_URL=PROTOCOL+HOST+":"+HTTP_PORT+"/EOT-Banking-Core/rest/banking/customerApprovalCommission";
	
}
