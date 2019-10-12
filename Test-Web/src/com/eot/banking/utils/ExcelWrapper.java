package com.eot.banking.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.type.CustomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.common.FieldExecutiveEnum;
import com.eot.banking.common.KycStatusEnum;
import com.eot.banking.dto.BankFloatDepositDTO;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.dto.ExternalTransactionDTO;
import com.eot.banking.dto.NonRegUssdCustomerDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.dto.TransactionVolumeDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.TransactionService;
import com.eot.entity.BusinessPartner;
import com.eot.entity.CommissionReport;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.PendingTransaction;
import com.eot.entity.ServiceChargeRule;
import com.eot.entity.ServiceChargeRuleTxn;
import com.eot.entity.TransactionRule;
import com.eot.entity.TransactionRuleTxn;
import com.eot.entity.WebUser;

/**
 * The Class ExcelWrapper.
 */
public class ExcelWrapper{

	@Autowired
	public MessageSource messageSource ;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Autowired
	public TransactionService transactionService;
	
	public List<String> initializeHeaderForTransactionSummary(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("Bank",null,locale));
		/*header.add(messageSource.getMessage("Branch",null,locale));*/
		header.add(messageSource.getMessage("Txn_Type",null,locale));
//		header.add(messageSource.getMessage("account_Type",null,locale));
		header.add(messageSource.getMessage("Txn_Date_Time",null,locale));
		header.add(messageSource.getMessage("Debit",null,locale));
		header.add(messageSource.getMessage("Credit",null,locale));
		header.add(messageSource.getMessage("Benf_Mob",null,locale));
//		header.add(messageSource.getMessage("Benf_Bank",null,locale));
		/*header.add(messageSource.getMessage("Benf_Branch",null,locale));*/
		header.add(messageSource.getMessage("Fee",null,locale));
		header.add(messageSource.getMessage("Tax",null,locale));
		//header.add(messageSource.getMessage("Stamp_Fee",null,locale));
		header.add(messageSource.getMessage("Bank_Share_Fee",null,locale));
		//header.add(messageSource.getMessage("GIM_Share_Fee",null,locale));

		return header;
	}

	public List<String> initializeHeaderForTransactionSummaryForBankTellerEOD(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("Bank",null,locale));
		/*header.add(messageSource.getMessage("Branch",null,locale));*/
		header.add(messageSource.getMessage("User_Type",null,locale));
		header.add(messageSource.getMessage("WebUser",null,locale));
		header.add(messageSource.getMessage("WebUser_Name",null,locale));
		header.add(messageSource.getMessage("Txn_Type",null,locale));
		header.add(messageSource.getMessage("Txn_Date_Time",null,locale));
		header.add(messageSource.getMessage("Debit",null,locale));
		header.add(messageSource.getMessage("Credit",null,locale));

		return header;
	}

	private List<String> initializeHeaderForCustomerDetails(Locale locale,MessageSource messageSource, String reportHeader,String type,Boolean blockFlag) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));	
	//	if(!reportHeader.equals(EOTConstants.CUSTOMER_PAGE_HEADER))
		if(!type.equals("0"))
			if(type.equals("1")){
			header.add(messageSource.getMessage("Agent_Code",null,locale));
			}else{
				header.add(messageSource.getMessage("Merchant_code",null,locale));
			}
		if(type.equals(EOTConstants.REFERENCE_TYPE_MERCHANT+"") || type.equals(EOTConstants.REFERENCE_TYPE_AGENT+""))
			header.add(messageSource.getMessage("Business_Name",null,locale));
		header.add(messageSource.getMessage("Customer_Name",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("Status",null,locale));	
		header.add(messageSource.getMessage("Gender",null,locale));	
		header.add(messageSource.getMessage("Registered_Date",null,locale));
		header.add(messageSource.getMessage("Country",null,locale));
		header.add(messageSource.getMessage("City",null,locale));
		header.add(messageSource.getMessage("Onboarded_By",null,locale));
		if(type.equals(EOTConstants.REFERENCE_TYPE_CUSTOMER+"")){
			header.add(messageSource.getMessage("LABEL_AGENT_NAME",null,locale));	
			header.add(messageSource.getMessage("LABEL_BUSINESS_PARTNER_CODE",null,locale));
			header.add(messageSource.getMessage("LABEL_BUSINESS_PARTNER_NAME",null,locale));
		}
		
		header.add(messageSource.getMessage("KYC_Status",null,locale));
		header.add(messageSource.getMessage("LABEL_APPROVED_BY",null,locale));
		header.add(messageSource.getMessage("LABEL_APPROVAL_DATE",null,locale));
		if(type.equals(EOTConstants.REFERENCE_TYPE_AGENT+"")){
			header.add(messageSource.getMessage("LABEL_BUSINESS_PARTNER_CODE",null,locale));
			header.add(messageSource.getMessage("LABEL_BUSINESS_PARTNER_NAME",null,locale));
		}
		if(blockFlag){
			header.add(messageSource.getMessage("LABEL_BLOCK_REASON",null,locale));
		}

		return header;
	}
	
	private List<String> initializeHeaderForMISReport(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("TXN_Date_Time",null,locale));
		header.add(messageSource.getMessage("Transaction_ID",null,locale));
		header.add(messageSource.getMessage("LABEL_TXN_TYPE",null,locale));
		header.add(messageSource.getMessage("REF_TRAN_ID",null,locale));
		header.add(messageSource.getMessage("Customer_Name",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("User_Name",null,locale));
		header.add(messageSource.getMessage("LABEL_AGENT_MERCHANT_WEBUSER_CODE",null,locale));
		header.add(messageSource.getMessage("Amount",null,locale));
		header.add(messageSource.getMessage("Service_Charges",null,locale));
		header.add(messageSource.getMessage("LABEL_BENEFICIARY_CUSTOMER_NAME",null,locale));
		header.add(messageSource.getMessage("LABEL_BENEFICIARY_CUSTOMER_MOBILE_NUMBER",null,locale));
		header.add(messageSource.getMessage("SUPER_AGENT_NAME",null,locale));
		header.add(messageSource.getMessage("SUPER_AGENT_CODE",null,locale));
		header.add(messageSource.getMessage("Status",null,locale));
		header.add(messageSource.getMessage("Description",null,locale));
		header.add(messageSource.getMessage("Request_Channel",null,locale));
		
		return header;
	}
	
	private List<String> initializeHeaderForMerchantCommisionReport(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Transaction_ID",null,locale));
		header.add(messageSource.getMessage("Merchant_Code",null,locale));
		header.add(messageSource.getMessage("LABEL_BUSS_NAME",null,locale));
		header.add(messageSource.getMessage("Merchant_Name",null,locale));
		header.add(messageSource.getMessage("Merchant_Mobile_Number",null,locale));		
		header.add(messageSource.getMessage("CUST_NAME",null,locale));
		header.add(messageSource.getMessage("Customer_Mobile_Number",null,locale));
		header.add(messageSource.getMessage("TXN_Date_Time",null,locale));
		header.add(messageSource.getMessage("LABEL_TXN_TYPE",null,locale));
		header.add(messageSource.getMessage("Status",null,locale));
		header.add(messageSource.getMessage("Description",null,locale));
		header.add(messageSource.getMessage("Amount",null,locale));
		header.add(messageSource.getMessage("Service_Charges",null,locale));
		header.add(messageSource.getMessage("Settlement_Amount",null,locale));
		header.add(messageSource.getMessage("Request_Channel",null,locale));
		
		return header;
	}
	
	private List<String> initializeHeaderForCusRegReport(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Transaction_ID",null,locale));
		header.add(messageSource.getMessage("COMM_AMOUNT",null,locale));
		header.add(messageSource.getMessage("LABEL_BUSS_NAME",null,locale));
		header.add(messageSource.getMessage("Cus_Name",null,locale));
		header.add(messageSource.getMessage("CUS_MO",null,locale));
		header.add(messageSource.getMessage("AG_NAME",null,locale));	
		header.add(messageSource.getMessage("Agent_Code",null,locale));	
		header.add(messageSource.getMessage("AG_MO",null,locale));
		header.add(messageSource.getMessage("SUPER_AGENT_NAME",null,locale));
		header.add(messageSource.getMessage("SUPER_AGENT_CODE",null,locale));
		header.add(messageSource.getMessage("LABEL_TXN_TYPE",null,locale));	
		header.add(messageSource.getMessage("TXN_Date_Time",null,locale));
		
		return header;
	}
	
	private List<String> initializeHeaderForBulkPay(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Transaction_ID",null,locale));
		header.add(messageSource.getMessage("Customer_Name",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		
		header.add(messageSource.getMessage("TXN_Date",null,locale));
		header.add(messageSource.getMessage("TRANSACTION_AMOUNT",null,locale));	
		//header.add(messageSource.getMessage("LABEL_TXN_TYPE",null,locale));	
		header.add(messageSource.getMessage("Service_Charge",null,locale));
		header.add(messageSource.getMessage("Status",null,locale));
		
		
		return header;
	}
	
	private List<String> initializeHeaderForWebUsers(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("UserId",null,locale));
		header.add(messageSource.getMessage("First_Name",null,locale));
		header.add(messageSource.getMessage("Last_Name",null,locale));	
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("User_Type",null,locale));
		header.add(messageSource.getMessage("Business_Partner",null,locale));
		header.add(messageSource.getMessage("Created_Date",null,locale));	
		header.add(messageSource.getMessage("Status",null,locale));
		

		return header;
	}
	
	private List<String> initializeHeaderForBusinessPartner(Locale locale,MessageSource messageSource,Integer role) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));
		if (null != role && role == EOTConstants.ROLEID_BANK_ADMIN) {
			header.add(messageSource.getMessage("Principal_Agent_Code",null,locale));
			header.add(messageSource.getMessage("Principal_Agent_Name",null,locale));
			header.add(messageSource.getMessage("Principal_Agent_Type",null,locale));
		}else {
			header.add(messageSource.getMessage("Super_Agent_Code",null,locale));
			header.add(messageSource.getMessage("Business_Partner_Name",null,locale));
			header.add(messageSource.getMessage("Business_Partner_Type",null,locale));
		}
		header.add(messageSource.getMessage("Contact_Number",null,locale));	
		header.add(messageSource.getMessage("Created_Date",null,locale));	

		return header;
	}
	
	private List<String> initializeHeaderForBulkPaymentPartner(Locale locale,MessageSource messageSource,Integer role) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));
		if (null != role && role == EOTConstants.ROLEID_BANK_ADMIN) {
			header.add(messageSource.getMessage("BULK_PAYMENT_CODE",null,locale));
			header.add(messageSource.getMessage("BULK_PAYMENT_NAME",null,locale));
			header.add(messageSource.getMessage("BULK_PAYMENT_TYPE",null,locale));
		}/*else {
			header.add(messageSource.getMessage("Super_Agent_Code",null,locale));
			header.add(messageSource.getMessage("Business_Partner_Name",null,locale));
			header.add(messageSource.getMessage("Business_Partner_Type",null,locale));
		}*/
		header.add(messageSource.getMessage("Contact_Number",null,locale));	
		header.add(messageSource.getMessage("Created_Date",null,locale));	

		return header;
	}
	
	
	private List<String> initializeHeaderForBankFloatDepositReportData(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));
		header.add(messageSource.getMessage("LABEL_CUSTOMER_NAME",null,locale));
		header.add(messageSource.getMessage("LABEL_CODE",null,locale));
		header.add(messageSource.getMessage("LABEL_ACCTYPE",null,locale));
		header.add(messageSource.getMessage("LABEL_ACCOUNTNUMBER",null,locale));	
		header.add(messageSource.getMessage("LABEL_TXNID",null,locale));	
		header.add(messageSource.getMessage("LABEL_AMOUNT",null,locale));	
		header.add(messageSource.getMessage("LABEL_TRANSACTION_DATE",null,locale));	
		header.add(messageSource.getMessage("LABEL_STATUS",null,locale));	

		return header;
	}
	
	private List<String> initializeHeaderForNonRegUssdCustomerReportData(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("LABEL_TXNDATE",null,locale));
	

		return header;
	}
	
	private List<String> initializeHeaderForTransactionVolumeReportData(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));
		header.add(messageSource.getMessage("LABEL_NAME",null,locale));
		header.add(messageSource.getMessage("LABEL_CODE",null,locale));
		header.add(messageSource.getMessage("AG_NAME",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("LABEL_AGENT_CODE",null,locale));
		header.add(messageSource.getMessage("LABEL_TOTAL_DEPOSIT",null,locale));
		header.add(messageSource.getMessage("LABEL_DEPOSIT_VOLUME",null,locale));
		header.add(messageSource.getMessage("LABEL_TOTAL_WITHDRAWAL",null,locale));
		header.add(messageSource.getMessage("LABEL_WITHDRAWAL_VOLUME",null,locale));
		header.add(messageSource.getMessage("LABEL_BALANCE_tXN",null,locale));
		header.add(messageSource.getMessage("LABEL_COMMISSION_BALANCE",null,locale));
		return header;
	}
	
	private List<String> initializeHeaderForBusinessPartnerCommission(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));
		header.add(messageSource.getMessage("Transaction_ID",null,locale));
		header.add(messageSource.getMessage("REF_TRAN_TYPE",null,locale));
		header.add(messageSource.getMessage("REF_TRAN_ID",null,locale));
		header.add(messageSource.getMessage("SERVICE_CHARGES_REPORT",null,locale));	
		header.add(messageSource.getMessage("COMMISSION_AMOUNT_REPORT",null,locale));	
		header.add(messageSource.getMessage("PARTNER_AGENT_NAME",null,locale));	
		header.add(messageSource.getMessage("PARTNER_AGENT_CODE",null,locale));	
		header.add(messageSource.getMessage("TRANSACTION_DATE_TIME",null,locale));	

		return header;
	}
	
	private List<String> initializeHeaderForSettlementBalance(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add("Sl_No");
		header.add("Amount");	
		header.add("JournalTime");
		header.add("GIM_Intra");
		

		return header;
	}
	private List<String> initializeHeaderForExternalTransactions(Locale locale, MessageSource messageSource) {
		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Operator",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("Benificiary",null,locale));
		header.add(messageSource.getMessage("Amount",null,locale));
		header.add(messageSource.getMessage("TransactionID",null,locale));
		header.add(messageSource.getMessage("Txn_Type",null,locale));
		header.add(messageSource.getMessage("Txn_Date_Time",null,locale));
		header.add(messageSource.getMessage("Fee",null,locale));
		header.add(messageSource.getMessage("Tax",null,locale));
		header.add(messageSource.getMessage("Bank_Share_Fee",null,locale));
		header.add(messageSource.getMessage("GIM_Share_Fee",null,locale));

		return header;
	}
	
	private List<String> initializeHeaderForExternalTransactionSummary(Locale locale, MessageSource messageSource) {
		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Operator",null,locale));
		header.add(messageSource.getMessage("Amount",null,locale));
		header.add(messageSource.getMessage("Txn_Type",null,locale));
		header.add(messageSource.getMessage("Fee",null,locale));
		header.add(messageSource.getMessage("Tax",null,locale));
		header.add(messageSource.getMessage("Bank_Share_Fee",null,locale));
		header.add(messageSource.getMessage("GIM_Share_Fee",null,locale));

		return header;
	}
	
	private List<String> initializeHeaderForFailedBulkUpload(Locale locale, MessageSource messageSource) {
		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		//header.add(messageSource.getMessage("ACCOUNT_ALIAS",null,locale));
		header.add(messageSource.getMessage("Customer_Name",null,locale));
		header.add(messageSource.getMessage("Amount",null,locale));
		header.add(messageSource.getMessage("Service_Charge",null,locale));
		header.add(messageSource.getMessage("Description",null,locale));
		header.add(messageSource.getMessage("Status",null,locale));
//		header.add(messageSource.getMessage("PRINTED_DATE",null,locale));
		header.add(messageSource.getMessage("ERROR_DESC",null,locale));

		return header;
	}


	private List<String> initializeHeaderForCustomerAccountDetails(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Customer_Name",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		header.add(messageSource.getMessage("Bank",null,locale));
		/*header.add(messageSource.getMessage("Branch",null,locale));*/
		header.add(messageSource.getMessage("Customer_Profile",null,locale));
		header.add(messageSource.getMessage("Customer_Type",null,locale));
		header.add(messageSource.getMessage("Balance",null,locale));		

		return header;
	}

	private List<String> initializeHeaderForPendingTxnDetails(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Customer_Name",null,locale));
		header.add(messageSource.getMessage("Mobile_Number",null,locale));
		/*header.add(messageSource.getMessage("Branch",null,locale));*/
		header.add(messageSource.getMessage("Txn_Type",null,locale));
		header.add(messageSource.getMessage("Txn_Date",null,locale));		
		header.add(messageSource.getMessage("Initiated_By",null,locale));
		header.add(messageSource.getMessage("Approved_By",null,locale));
		header.add(messageSource.getMessage("Debit",null,locale));
		header.add(messageSource.getMessage("Credit",null,locale));
		header.add(messageSource.getMessage("Status",null,locale));	

		return header;
	}


	private List<String> initializeHeaderForTxnSummary(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));		
		/*header.add(messageSource.getMessage("Branch",null,locale));*/
		header.add(messageSource.getMessage("Txn_Type",null,locale));
		header.add(messageSource.getMessage("No_of_Txns",null,locale));
		header.add(messageSource.getMessage("Debit",null,locale));
		header.add(messageSource.getMessage("Credit",null,locale));
		header.add(messageSource.getMessage("Fee",null,locale));	
		header.add(messageSource.getMessage("Tax",null,locale));
		/*header.add(messageSource.getMessage("Stamp_Fee",null,locale));*/
		header.add(messageSource.getMessage("Bank_Share_Fee",null,locale));
		//header.add(messageSource.getMessage("GIM_Share_Fee",null,locale));	

		return header;
	}
	
	private List<String> initializeHeaderForTxnSummaryPerBank(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();

		header.add(messageSource.getMessage("SL_NO",null,locale));		
		header.add(messageSource.getMessage("Bank",null,locale));
		header.add(messageSource.getMessage("Txn_Type",null,locale));
		header.add(messageSource.getMessage("No_of_Txns",null,locale));
		header.add(messageSource.getMessage("Debit",null,locale));
		header.add(messageSource.getMessage("Credit",null,locale));
		header.add(messageSource.getMessage("Fee",null,locale));	
		header.add(messageSource.getMessage("Tax",null,locale));
		header.add(messageSource.getMessage("Stamp_Fee",null,locale));
		header.add(messageSource.getMessage("Bank_Share_Fee",null,locale));
		header.add(messageSource.getMessage("GIM_Share_Fee",null,locale));	

		return header;
	}
	
	
	private List<String> initializeHeaderForLocations(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		
		header.add(messageSource.getMessage("SL_NO",null,locale));	
		header.add(messageSource.getMessage("Country_Name",null,locale));		
		header.add(messageSource.getMessage("Alpha(2)",null,locale));
		header.add(messageSource.getMessage("ISD_Code",null,locale));
		header.add(messageSource.getMessage("Mobile_Number_Length",null,locale));
		header.add(messageSource.getMessage("Currency",null,locale));
		return header;
	}
	
	private List<String> initializeHeaderForSCR(Locale locale,MessageSource messageSource) {

		ArrayList<String> header = new ArrayList<String>();
		
		header.add(messageSource.getMessage("Sl_No",null,locale));
		header.add(messageSource.getMessage("Rule_Level",null,locale));	
		header.add(messageSource.getMessage("Rule_Name",null,locale));		
		header.add(messageSource.getMessage("Transaction_Type",null,locale));	
		header.add(messageSource.getMessage("Applicable_From",null,locale));
		header.add(messageSource.getMessage("Applicable_To",null,locale));
		header.add(messageSource.getMessage("TimeZone",null,locale));
		header.add(messageSource.getMessage("ImposedOn",null,locale));
		return header;
	}
	

	/**
	 * Sets the title.
	 * 
	 * @param workBook
	 *            the work book
	 * @param header
	 *            the header
	 */
	public void setTitle(HSSFWorkbook workBook, List<String> header) {

		// *************** Title ******************//
		// create a Title row
		HSSFSheet sheet = workBook.getSheetAt(0);
		HSSFRow titleRow = sheet.createRow(0);
		// define the area for the Title
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, header.size() - 1));

		// create the header data cell
		HSSFCell titleCell = titleRow.createCell(0);
		// add the data to the header cell
		titleCell.setCellValue("m-GURUSH");
		// creating a custom palette for the workbook
		HSSFPalette palette = workBook.getCustomPalette();
		// replacing the standard TAN with cutomized TAN
		palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 222, // RGB red
				// (0-255)
				(byte) 203, // RGB green
				(byte) 193 // RGB blue
				);
		titleCell.setCellStyle(getTitleStyle(workBook));
		// ***************End of Title******************//
	}

	/**
	 * Gets the title style.
	 * 
	 * @param workBook
	 *            the work book
	 * @return the title style
	 */
	public HSSFCellStyle getTitleStyle(HSSFWorkbook workBook) {

		// create a style for the Title
		HSSFCellStyle titleStyle = workBook.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setFillForegroundColor(HSSFColor.INDIGO.index);
		titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		HSSFFont font = workBook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 15);
		font.setColor(HSSFColor.BLUE.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		titleStyle.setFont(font);

		return titleStyle;
	}

	/**
	 * Gets the header style.
	 * 
	 * @param workBook
	 *            the work book
	 * @return the header style
	 */
	public HSSFCellStyle getHeaderStyle(HSSFWorkbook workBook) {

		//HSSFSheet sheet = workBook.getSheetAt(0);
		//sheet.createFreezePane(15, 6);

		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = workBook.createCellStyle();
		labelHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// creating a custom palette for the workbook
		HSSFPalette palette = workBook.getCustomPalette();
		// replacing the standard PALE_BLUE with cutomized PALE_BLUE
		palette.setColorAtIndex(HSSFColor.PALE_BLUE.index, (byte) 179, // RGB
				// red
				// (0-255)
				(byte) 217, // RGB green
				(byte) 255 // RGB blue
				);

		labelHeaderStyle.setFillForegroundColor(IndexedColors.PALE_BLUE
				.getIndex());
		labelHeaderStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		HSSFFont labelHeaderfont = workBook.createFont();
		labelHeaderfont.setFontName("Verdana");
		labelHeaderfont.setFontHeightInPoints((short) 10);
		labelHeaderfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		labelHeaderStyle.setFont(labelHeaderfont);

		return labelHeaderStyle;
	}

	public HSSFCellStyle getHeaderStyle1(HSSFWorkbook workBook) {

		//HSSFSheet sheet = workBook.getSheetAt(0);
		//sheet.createFreezePane(15, 6);

		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = workBook.createCellStyle();
		labelHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// creating a custom palette for the workbook
		// replacing the standard PALE_BLUE with cutomized PALE_BLUE


		labelHeaderStyle.setFillForegroundColor(IndexedColors.BLACK
				.getIndex());
		HSSFFont labelHeaderfont = workBook.createFont();
		labelHeaderfont.setFontName("Verdana");
		labelHeaderfont.setFontHeightInPoints((short) 15);
		labelHeaderfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		labelHeaderStyle.setFont(labelHeaderfont);

		return labelHeaderStyle;
	}

	/**
	 * Gets the column data style.
	 * 
	 * @param workBook
	 *            the work book
	 * @return the column data style
	 */
	public HSSFCellStyle getColumnDataStyle(HSSFWorkbook workBook) {

		// create a style for the Column data except Amount column
		HSSFCellStyle dataCellStyle = workBook.createCellStyle();
		dataCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataCellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON
				.getIndex());
		dataCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		HSSFFont dataCellFont = workBook.createFont();
		dataCellFont.setFontName("Calibri");
		dataCellFont.setFontHeightInPoints((short) 11);
		dataCellStyle.setFont(dataCellFont);

		return dataCellStyle;
	}

	/**
	 * Insert brand picture.
	 * 
	 * @param workBook
	 *            the work book
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void insertBrandPicture(HSSFWorkbook workBook,List<String> header) throws IOException {

		WebApplicationContext ctx = ContextLoader
				.getCurrentWebApplicationContext();
		String fileloc = ctx.getServletContext().getRealPath("");
		// Note: take the brand image as per headers auto column width size
		InputStream is = new FileInputStream("D:\\header_01.jpg");
		byte[] bytes = IOUtils.toByteArray(is);
		int pictureIdx = workBook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		is.close();
		CreationHelper helper = workBook.getCreationHelper();
		// Create the drawing patriarch. This is the top level container for all
		// shapes.
		HSSFSheet sheet = workBook.getSheetAt(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, header.size() - 1));
		Drawing drawing = sheet.createDrawingPatriarch();

		// add a picture shape
		ClientAnchor anchor = helper.createClientAnchor();

		Picture pict = drawing.createPicture(anchor, pictureIdx);



		// auto-size picture relative to its top-left corner
		pict.resize();
	}




	public HSSFWorkbook createSpreadSheetFromListForTransactionSummary(List list, Locale resolveLocale,MessageSource messageSource,TxnSummaryDTO txnSummaryDTO,String bankName) throws ParseException {

		List<String> header = initializeHeaderForTransactionSummary(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Transaction_Details_Sharing");

		//insertBrandPicture(workBook,header);

		setTitle(workBook, header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		if(bankName!=null){
			functionalityHeadingCell.setCellValue(bankName+" - "+messageSource.getMessage("TXN_DETAILS_SHARING",null,resolveLocale));
		}else{
			functionalityHeadingCell.setCellValue(messageSource.getMessage("TXN_DETAILS_SHARING",null,resolveLocale));	
		}
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));

		HSSFRow functionalityHeadingRow1 = sheet.createRow(6);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		//HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(1);
		HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(0);
		functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
		HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(1);
		//HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(2);
		if(txnSummaryDTO.getFromDate()!=null){
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getFromDate()));	
		}else{
			Object[] obj=(Object[])list.get(0);
			Date date=DateUtil.dateAndTime1(obj[4].toString());
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
		}		
		//HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(4);
		HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(2);
		functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
		HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(3);
		//HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(5);		
		if(txnSummaryDTO.getFromDate()!=null){
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getToDate()));
		}else{
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
		}
		
		//HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(7);
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(4);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(5);
		//HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(8);	
		functionalityHeadingCell7.setCellValue(txnSummaryDTO.getUserFirstName());	

		//HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(10);
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(7);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(8);
		//HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(11);		

		functionalityHeadingCell5.setCellValue(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));	
	//	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	


		functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));


		// create an header row
		HSSFRow headerRow = sheet.createRow(8);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	
		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			Double totalCreditBalance = 0D;
			Double totalDebitBalance = 0D;
			Double totalServiceChargeFee = 0D;
			Double totalBankServiceChargeFee = 0D;
			Double totalGIMServiceChargeFee = 0D;
			Double totalStampFee = 0D;
			Double totalTax = 0D;
			DecimalFormat dec = new DecimalFormat("0.00");

			// *************** Column data ******************//
			for (int k = 9, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);

				int x=0;

				// Column: Sl.no.
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);

				Object[] obj=(Object[])list.get(tObject);


				++x;
				// Column: Mobile Number
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[1]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(String.valueOf(obj[1]));
				}

				++x;
				// Column: Bank
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[5]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(String.valueOf(obj[5]));
				}

				/*++x;
				// Column: Branch
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[6]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(String.valueOf(obj[6]));
				}*/


				++x;
				// Column: Txn Type
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (obj[3]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(obj[3].equals(EOTConstants.TXN_ID_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("Deposit",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("Withdrawl",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)){
						cells[x].setCellValue(messageSource.getMessage("Balance_Enquiry",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_MINISTATEMENT)){
						cells[x].setCellValue(messageSource.getMessage("Mini_Statement",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_TXNSTATEMENT)){
						cells[x].setCellValue(messageSource.getMessage("Txn_Statement",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_ACTIVATION)){
						cells[x].setCellValue(messageSource.getMessage("Activation",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_TXNPINCHANGE)){
						cells[x].setCellValue(messageSource.getMessage("Txn_Pin_Change",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_TRFDIRECT)){
						cells[x].setCellValue(messageSource.getMessage("TRF_DIRECT",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_RESET_PIN)){
						cells[x].setCellValue(messageSource.getMessage("RESET_PIN",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_SALE)){
						cells[x].setCellValue(messageSource.getMessage("SALE",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_TOPUP)){
						cells[x].setCellValue(messageSource.getMessage("TOP_UP",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_RESET_TXNPIN)){
						cells[x].setCellValue(messageSource.getMessage("RESET_TXN_PIN",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_REACTIVATION)){
						cells[x].setCellValue(messageSource.getMessage("REACTIVATION",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_BILLPAYMENT)){
						cells[x].setCellValue(messageSource.getMessage("BILL_PAYMENT",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_SMSCASH)){
						cells[x].setCellValue(messageSource.getMessage("SMS_CASH",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_CUSTOMER_APPROVAL)){
						cells[x].setCellValue(messageSource.getMessage("CUST_APPROVAL_COMM",null,resolveLocale));
					}
					
					else if(obj[3].equals(EOTConstants.TXN_ID_ADDCARD)){
						cells[x].setCellValue(messageSource.getMessage("ADD_CARD",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_CONFIRMCARD)){
						cells[x].setCellValue(messageSource.getMessage("CONFIRM_CARD",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_DELETECARD)){
						cells[x].setCellValue(messageSource.getMessage("DELETE_CARD",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_PAYMENTHISTORY)){
						cells[x].setCellValue(messageSource.getMessage("PAYMENT_HOSTORY",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_PINCHANGE)){
						cells[x].setCellValue(messageSource.getMessage("PIN_CHANGE",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_REVERSAL)){
						cells[x].setCellValue(messageSource.getMessage("Adjustment",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_CANCEL)){
						cells[x].setCellValue(messageSource.getMessage("Void",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_ADD_BANK_ACCOUNT)){
						cells[x].setCellValue(messageSource.getMessage("Add_Bank_Acc",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_CHEQUE_STATUS)){
						cells[x].setCellValue(messageSource.getMessage("CHEQUE_STATUS",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_AGETNT_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("AGETNT_DEPOSIT",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_AGENT_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("AGENT_WITHDRAWAL",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_COMMISSION_SHARE)){
						cells[x].setCellValue(messageSource.getMessage("COMMISSION_SHARE",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_TYPE_LIMIT_UPDATE)){
						cells[x].setCellValue(messageSource.getMessage("LIMIT",null,resolveLocale));
					}
					
					else if(obj[3].equals(EOTConstants.TXN_ID_SMS_CASH_RECV)){
						cells[x].setCellValue(messageSource.getMessage("SMS_CASH_RECV",null,resolveLocale));
					}
					
					else if(obj[3].equals(EOTConstants.TXN_ID_PAY)){
						cells[x].setCellValue(messageSource.getMessage("TXN_ID_PAY",null,resolveLocale));
					}
					
					else if(obj[3].equals(EOTConstants.TXN_ID_FLOAT_MANAGEMENT)){
						cells[x].setCellValue(messageSource.getMessage("FLOAT_MANAGEMENT",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_CUSTOMER_PIN_CHANGE)){
						cells[x].setCellValue(messageSource.getMessage("CUSTOMER_PIN_CHANGE",null,resolveLocale));
					}
					else if(obj[3].equals(EOTConstants.TXN_ID_MERCHANT_PAY_OUT)){
						cells[x].setCellValue(messageSource.getMessage("MERCHANT_PAY_OUT",null,resolveLocale));
					}
					
				}

				//++x;
				// Column: account type
				/*cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (obj[23]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(obj[23].equals(1)){
						cells[x].setCellValue(messageSource.getMessage("GIM_MOBILE",null,resolveLocale));
					}else if(obj[23].equals(2)){
						cells[x].setCellValue(messageSource.getMessage("GIM_CARD",null,resolveLocale));	
					}else{
						cells[x].setCellValue(messageSource.getMessage("BANK_ACCOUNT",null,resolveLocale));	
					}
				}*/
				
				++x;
				// Column: Txn Date
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (obj[4]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
				//	cells[x].setCellValue(obj[4].toString().split("\\.")[0]);
					String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj[4]);
					cells[x].setCellValue(createdDate);
				}

				++x;				
				if(obj[3].equals(EOTConstants.TXN_ID_FLOAT_MANAGEMENT) || obj[3].equals(EOTConstants.TXN_ID_WITHDRAWAL) || obj[3].equals(EOTConstants.TXN_ID_SMS_CASH_RECV) || obj[3].equals(EOTConstants.TXN_ID_PAY) || obj[3].equals(EOTConstants.TXN_ID_TRFDIRECT) || obj[3].equals(EOTConstants.TXN_ID_TOPUP) || obj[3].equals(EOTConstants.TXN_ID_BILLPAYMENT)|| obj[3].equals(EOTConstants.TXN_ID_AGETNT_DEPOSIT) || obj[3].equals(EOTConstants.TXN_ID_SMSCASH) 
						|| (obj[24]!=null && obj[24].equals(EOTConstants.TXN_ID_WITHDRAWAL))){
					// Column: Debit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[2]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[2];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[2];
						totalDebitBalance = (double) (totalDebitBalance+amount);
					}
				}
				++x;
				if(obj[3].equals(EOTConstants.TXN_ID_COMMISSION_SHARE) || obj[3].equals(EOTConstants.TXN_ID_REVERSAL) ||obj[3].equals(EOTConstants.TXN_TYPE_LIMIT_UPDATE) || obj[3].equals(EOTConstants.TXN_ID_CUSTOMER_APPROVAL) || obj[3].equals(EOTConstants.TXN_ID_MERCHANT_PAY_OUT) || obj[3].equals(EOTConstants.TXN_ID_DEPOSIT) || obj[3].equals(EOTConstants.TXN_ID_SALE)||obj[3].equals(EOTConstants.TXN_ID_AGENT_WITHDRAWAL) || (obj[24]!=null && obj[24].equals(EOTConstants.TXN_ID_DEPOSIT))){
					// Column: Credit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[2]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[2];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[2];
						totalCreditBalance = (double) (totalCreditBalance+amount);
					}
				}
				
				++x;
				// Column: Benificiary Tel no
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				
				if (obj[18]  == null && obj[21]  == null && obj[22]  == null && obj[20]==null  && obj[19]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				}else if(obj[18]  == null && obj[22] == null && obj[19]  == null && obj[20]==null  && obj[21]!=null){					
					cells[x].setCellValue(String.valueOf(obj[21]));
				}else if(obj[21]  == null && obj[20]==null && obj[22] == null && obj[19]  == null && obj[18]!=null){					
					cells[x].setCellValue(String.valueOf(obj[18]));
				}else if(obj[18]  == null && obj[20]==null && obj[21] == null && obj[19]  == null && obj[22]!=null){					
					cells[x].setCellValue(String.valueOf(obj[22]));
				}else if(obj[18]  == null  && obj[20]==null && obj[21] == null && obj[22]  == null && obj[19]!=null){					
					cells[x].setCellValue(String.valueOf(obj[19]));
				}
				else if(obj[18]  == null && obj[21] == null && obj[22]  == null && obj[19]==null && obj[20]!=null){					
					cells[x].setCellValue(String.valueOf(obj[20]));
				}
				
				
				
				/*++x;
				// Column: Benificiary Bank
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[16]  == null && obj[19]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else if(obj[19]  == null){
					cells[x].setCellValue(String.valueOf(obj[16]));
				}else if(obj[16]  == null){
					cells[x].setCellValue(String.valueOf(obj[19]));
				}		*/			
					
							
				
				
			/*	++x;
				// Column: Benificairty Branch
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[17]  == null && obj[20]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else if(obj[20]  == null){
					cells[x].setCellValue(String.valueOf(obj[17]));
				}else if(obj[17]  == null) {
					cells[x].setCellValue(String.valueOf(obj[20]));
				}*/				
						
				

				++x;
				// Column: Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[11]  == null) {
					cells[x].setCellValue((dec.format(0)));
				} else {					
					Double amount1= (Double) obj[11];
					cells[x].setCellValue((dec.format(amount1)));
					Double serChargeFee = (Double) obj[11];
					totalServiceChargeFee = (double) (totalServiceChargeFee+serChargeFee);

				}



				++x;
				// Column: Tax Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[13]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[13];					
					cells[x].setCellValue((dec.format(amount1)));					
					Double GIMShareFee = (Double) obj[13];
					totalTax = (double) (totalTax+GIMShareFee);

				}

				/*++x;
				// Column: Stamp Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[12]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[12];					
					cells[x].setCellValue((dec.format(amount1)));

					Double stampFee = (Double) obj[12];
					totalStampFee = (double) (totalStampFee+stampFee);
				}*/

				++x;
				// Column: Bank Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[14]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double bankShare= (Double) obj[14];
					Double gimShare= (Double) obj[15];
					Double totalBankShare=bankShare-(null != gimShare ? gimShare : 0);
					cells[x].setCellValue((dec.format(totalBankShare)));
					totalBankServiceChargeFee = (double) (totalBankServiceChargeFee+totalBankShare);
				}

				/*++x;
				// Column: GIM Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[15]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double gimShare= (Double) obj[15];
					cells[x].setCellValue((dec.format(gimShare)));
					Double GIMShareFee = (Double) obj[15];
					totalGIMServiceChargeFee = (double) (totalGIMServiceChargeFee+GIMShareFee);

				}*/

				if(j==list.size()+1){

					HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
					cells[4] = row1.createCell(4);			
					cells[4].setCellStyle(getHeaderStyle(workBook));
					cells[4].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));		

					cells[5] = row1.createCell(5);	
					cells[5].setCellStyle(rightCellStyle);
					cells[5].setCellValue(totalDebitBalance);

					cells[6] = row1.createCell(6);	
					cells[6].setCellStyle(rightCellStyle);
					cells[6].setCellValue(totalCreditBalance);

					/*cells[9] = row1.createCell(9);	
					cells[9].setCellStyle(rightCellStyle);
					cells[9].setCellValue((dec.format(totalServiceChargeFee)));*/
					
					cells[8] = row1.createCell(8);	
					cells[8].setCellStyle(rightCellStyle);
					cells[8].setCellValue((dec.format(totalServiceChargeFee)));

					cells[9] = row1.createCell(9);	
					cells[9].setCellStyle(rightCellStyle);
					cells[9].setCellValue((dec.format(totalTax)));

					/*cells[11] = row1.createCell(11);	
					cells[11].setCellStyle(rightCellStyle);
					cells[11].setCellValue((dec.format(totalStampFee)));*/

					cells[10] = row1.createCell(10);	
					cells[10].setCellStyle(rightCellStyle);
					cells[10].setCellValue((dec.format(totalBankServiceChargeFee)));

					/*cells[11] = row1.createCell(11);	
					cells[11].setCellStyle(rightCellStyle);
					cells[11].setCellValue((dec.format(totalGIMServiceChargeFee)));*/


				}


			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}


	public HSSFWorkbook createSpreadSheetFromListForTransactionSummaryForBankTellerEOD(List list, Locale resolveLocale,MessageSource messageSource,TxnSummaryDTO txnSummaryDTO,String bankName) throws ParseException {

		//System.out.println("******************Inside createSpreadSheetFromListForTransactionSummaryForBankTellerEOD*********************");
		HSSFWorkbook workBook;
		workBook = new HSSFWorkbook();
		try {
			List<String> header = initializeHeaderForTransactionSummaryForBankTellerEOD(resolveLocale,messageSource);
			HSSFSheet sheet = workBook.createSheet("Transaction Summary");

			//insertBrandPicture(workBook,header);

			setTitle(workBook, header);

			sheet.createRow(2);
			HSSFRow functionalityHeadingRow = sheet.createRow(3);
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
			HSSFCell functionalityHeadingCell = functionalityHeadingRow
					.createCell(0);
			if(bankName!=null){
				functionalityHeadingCell.setCellValue(bankName+" - "+messageSource.getMessage("WEBUSERS_TXNS",null,resolveLocale));
			}else{
				functionalityHeadingCell.setCellValue(messageSource.getMessage("WEBUSERS_TXNS",null,resolveLocale));	
			}
			functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));

			HSSFRow functionalityHeadingRow1 = sheet.createRow(6);
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
			HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(0);
			functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
			HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(1);
			if(txnSummaryDTO.getFromDate()!=null){
				functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getFromDate()));	
			}else{
				Object[] obj=(Object[])list.get(0);
				Date date=DateUtil.dateAndTime1(obj[4].toString());
				functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
			}		
			HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(2);
			functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
			HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(3);		
			if(txnSummaryDTO.getFromDate()!=null){
				functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getToDate()));
			}else{
				functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
			}
			
			HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(4);
			functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
			HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(5);	
			functionalityHeadingCell7.setCellValue(txnSummaryDTO.getUserFirstName());	

			HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(6);
			functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
			HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(7);		

		//	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	
			functionalityHeadingCell5.setCellValue(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));


			functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
			functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
			functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
			functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));


			// create an header row
			HSSFRow headerRow = sheet.createRow(8);
			// create a style for the Column Headers
			HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

			HSSFCell cells[] = new HSSFCell[header.size() + 1];
			HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

			// create a style for the Column data except Amount column
			HSSFCellStyle amountCellStyle = workBook.createCellStyle();
			amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

			HSSFCellStyle rightCellStyle = workBook.createCellStyle();
			rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

			HSSFCellStyle centerCellStyle = workBook.createCellStyle();
			centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	
			for (Iterator<String> it = header.iterator(); it.hasNext();) {
				// *************** Start of Column Headers ******************//
				for (int i = 0; i < header.size(); i++) {
					cells[i] = headerRow.createCell(i);
					cells[i].setCellValue(it.next());
					cells[i].setCellStyle(labelHeaderStyle);
				}
				// *************** End of Column Headers ******************//

				Long totalCreditBalance = 0L;
				Long totalDebitBalance = 0L;

				// *************** Column data ******************//
				for (int k = 9, j = 1, tObject = 0; k < (list.size() + (headerRow
						.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
					HSSFRow row = sheet.createRow(k);

					int x=0;

					// Column: Sl.no.
					cells[x] = row.createCell(x);
					cells[x].setCellValue(j++);
					cells[x].setCellStyle(centerCellStyle);

					Object[] obj=(Object[])list.get(tObject);



					++x;
					// Column: Mobile Number
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (obj[1]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(String.valueOf(obj[1]));
					}

					++x;
					// Column: Bank
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (obj[5]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(String.valueOf(obj[5]));
					}

					/*++x;
					// Column: Branch
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (obj[6]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(String.valueOf(obj[6]));
					}*/

					++x;
					// Column: User type
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(centerCellStyle);
					if (obj[12]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						if(obj[12].equals(EOTConstants.ROLEID_BANK_ADMIN)){
							cells[x].setCellValue("Organization Admin");
						}else if(obj[12].equals(EOTConstants.ROLEID_BANK_TELLER)){
							cells[x].setCellValue("Supervisor");
						}else if(obj[12].equals(EOTConstants.ROLEID_BRANCH_MANAGER)){
							cells[x].setCellValue("Branch Manager");
						}else if(obj[12].equals(EOTConstants.ROLEID_RELATIONSHIP_MANAGER)){
							cells[x].setCellValue("Relationship Manager");
						}else if(obj[12].equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1)){
							cells[x].setCellValue("Principle Agent");
						}else if(obj[12].equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2)){
							cells[x].setCellValue("Super Agent");
						}else if(obj[12].equals(EOTConstants.ROLEID_SALES_OFFICERS)){
							cells[x].setCellValue("Sales Officer");
						}else if(obj[12].equals(EOTConstants.ROLEID_ACCOUNTING)){
							cells[x].setCellValue("Accounting");
						}else if(obj[12].equals(EOTConstants.ROLEID_COMMERCIAL_OFFICER)){
							cells[x].setCellValue("Commercial Officer");
						}else if(obj[12].equals(EOTConstants.ROLEID_SUPPORT_CALL_CENTER)){
							cells[x].setCellValue("Support Call center");
						}else if(obj[12].equals(EOTConstants.ROLEID_SUPPORT_BANK)){
							cells[x].setCellValue("Support Customer Care");
						}
					}

					++x;
					// Column: webuser
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(centerCellStyle);
					if (obj[8]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(String.valueOf(obj[8]));
					}

					++x;
					// Column:webuser name
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(centerCellStyle);
					if (obj[13]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(String.valueOf(obj[13]));
					}

					++x;
					// Column: Txn Type
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(centerCellStyle);
					if (obj[3]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						if(obj[3].equals(EOTConstants.TXN_ID_DEPOSIT)){
							cells[x].setCellValue(messageSource.getMessage("Deposit",null,resolveLocale));
						}
						else if(obj[3].equals(EOTConstants.TXN_ID_WITHDRAWAL)){
							cells[x].setCellValue(messageSource.getMessage("Withdrawl",null,resolveLocale));
						}
						else if(obj[3].equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)){
							cells[x].setCellValue(messageSource.getMessage("Balance_Enquiry",null,resolveLocale));
						}
						else if(obj[3].equals(EOTConstants.TXN_ID_MINISTATEMENT)){
							cells[x].setCellValue(messageSource.getMessage("Mini_Statement",null,resolveLocale));
						}
						else if(obj[3].equals(EOTConstants.TXN_ID_TXNSTATEMENT)){
							cells[x].setCellValue(messageSource.getMessage("Txn_Statement",null,resolveLocale));
						}
						else if(obj[3].equals(EOTConstants.TXN_ID_REVERSAL)){
							cells[x].setCellValue(messageSource.getMessage("Adjustment",null,resolveLocale));
						}
						else if(obj[3].equals(EOTConstants.TXN_ID_CANCEL)){
							cells[x].setCellValue(messageSource.getMessage("Void",null,resolveLocale));
						}
					}

					++x;
					// Column: Txn Date
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(centerCellStyle);
					if (obj[4]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
//						cells[x].setCellValue(obj[4].toString().split("\\.")[0]);
						String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(obj[4]);
						cells[x].setCellValue(createdDate);
					}

					++x;				
					if(obj[3].equals(99) || (obj[14]!=null && obj[14].equals(EOTConstants.TXN_ID_WITHDRAWAL))){
						// Column: Debit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[2]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[2];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[2];
							totalDebitBalance = (long) (totalDebitBalance+amount);
						}
					}
					++x;
					if(obj[3].equals(95) || (obj[14]!=null && obj[14].equals(EOTConstants.TXN_ID_DEPOSIT))){
						// Column: Credit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[2]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[2];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[2];
							totalCreditBalance = (long) (totalCreditBalance+amount);
						}
					}



					if(j==list.size()+1){

						HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
						cells[7] = row1.createCell(7);			
						cells[7].setCellStyle(getHeaderStyle(workBook));
						cells[7].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));		

						cells[8] = row1.createCell(8);	
						cells[8].setCellStyle(rightCellStyle);
						cells[8].setCellValue(totalDebitBalance);

						cells[9] = row1.createCell(9);	
						cells[9].setCellStyle(rightCellStyle);
						cells[9].setCellValue(totalCreditBalance);


					}


				}// *************** End of Column data ******************//
			}

			for (int i = 0; i < header.size(); i++) {
				sheet.autoSizeColumn(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("******************end createSpreadSheetFromListForTransactionSummaryForBankTellerEOD*********************");
		return workBook;
	}



	public HSSFWorkbook createSpreadSheetFromListForCustomerDetails(List<Customer> list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader,String custType,Boolean blockFlag) {
		
		List<String> header = initializeHeaderForCustomerDetails(resolveLocale,messageSource,reportHeader,custType,blockFlag);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		
		HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
		// insertBrandPicture(workBook);
		setTitle(workBook,header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);
	
		functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
		functionalityHeadingCell7.setCellValue(webUser.getUserName());
		
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));

		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

		sheet.createRow(4);

		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			// *************** Column data ******************//
			int x;
			for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);
				// Column: Sl.no.
				cells[0] = row.createCell(0);
				cells[0].setCellValue(j++);
				cells[0].setCellStyle(centerCellStyle);
		//		Customer customerDetail = (Customer) list.get(tObject);
				Map<String, Object> m = (Map<String, Object>) list.get(tObject);
				x=1;
				// Column: Agent Code
				if(!custType.equals(EOTConstants.REFERENCE_TYPE_CUSTOMER+"")){
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("AgentCode") == null)
					cells[x].setCellValue(String.valueOf(""));
				else{
						cells[x].setCellValue(m.get("AgentCode").toString());
				}
				++x;
				}
				
				// Column: Business Name
				if(m.get("Type").equals(EOTConstants.REFERENCE_TYPE_MERCHANT) || m.get("Type").equals(EOTConstants.REFERENCE_TYPE_AGENT)){
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("BusinessName") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(String.valueOf(m.get("BusinessName")));
				}}
				// Column: Customer Name
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("CustomerName") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(m.get("CustomerName").toString());
				}
				
				// Column: Mobile Number
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("MobileNumber") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(m.get("MobileNumber").toString());
				}
				
				// Column: Status
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (m.get("Active") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					if(((Integer) m.get("Active")).intValue() == 40){
						cells[x++].setCellValue("Inactive");
					}else{
						cells[x++].setCellValue("Active");
					}

				}		
				
				// Column: Gender
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (m.get("Gender") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
						cells[x++].setCellValue(m.get("Gender").toString());
				}	
				
				// Column: Registered Date
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (m.get("CreatedDate") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
//					String date=DateUtil.formatDateToStr(m.get("CreatedDate").getCreatedDate());	
					String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("CreatedDate"));
					cells[x++].setCellValue(date);
				}					
				
				// Column: Country
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("Country") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(String.valueOf(m.get("Country")));
				}
				
				// Column: City
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("City") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(String.valueOf(m.get("City")));
				}
				
				// Column: Onboarded by
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("OnbordedBy") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(String.valueOf(m.get("OnbordedBy")));
				}
				
				if(custType.equals(EOTConstants.REFERENCE_TYPE_CUSTOMER+"")){
					// Column: Agent Name
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (m.get("AgentName") == null) {
						cells[x++].setCellValue(String.valueOf(""));
					} else {
						cells[x++].setCellValue(String.valueOf(m.get("AgentName")));
					}
					
					// Column: Super Agent Code
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (m.get("Code") == null) {
						cells[x++].setCellValue(String.valueOf(""));
					} else {
						cells[x++].setCellValue(String.valueOf(m.get("Code")));
					}
					
					// Column: Super Agent Name
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (m.get("Name") == null) {
						cells[x++].setCellValue(String.valueOf(""));
					} else {
						cells[x++].setCellValue(String.valueOf(m.get("Name")));
					}
									
				}
				
				// Column: KYC Status
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (m.get("kyc_status") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					if(((Integer) m.get("kyc_status")).intValue() == 0){
						cells[x++].setCellValue(KycStatusEnum.KYC_PENDING.getName());
					}else if (((Integer) m.get("kyc_status")).intValue() == 1) {
						cells[x++].setCellValue(KycStatusEnum.KYC_APPROVE_PENDING.getName());
					}else if (((Integer) m.get("kyc_status")).intValue() == 11) {
						cells[x++].setCellValue(KycStatusEnum.KYC_APPROVED.getName());
					}
					else{
						cells[x++].setCellValue(KycStatusEnum.KYC_REJECTED.getName());
					}

				}
				// Column: Approved By
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (m.get("ApprovedBy") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
					cells[x++].setCellValue(String.valueOf(m.get("ApprovedBy")));
				}
				
				// Column: Approval Date
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (m.get("ApprovalDate") == null) {
					cells[x++].setCellValue(String.valueOf(""));
				} else {
//					String date=DateUtil.formatDateToStr(m.get("ApprovalDate").getCreatedDate());	
					String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("ApprovalDate"));
					cells[x++].setCellValue(date);
				}
				
				if(custType.equals(EOTConstants.REFERENCE_TYPE_AGENT+"")) {
					// Column: Super Agent Code
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (m.get("Code") == null) {
						cells[x++].setCellValue(String.valueOf(""));
					} else {
						cells[x++].setCellValue(String.valueOf(m.get("Code")));
					}
					
					// Column: Super Agent Name
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (m.get("Name") == null) {
						cells[x++].setCellValue(String.valueOf(""));
					} else {
						cells[x++].setCellValue(String.valueOf(m.get("Name")));
					}
				}
				
				if(blockFlag) {
					// Column: Reason for Block
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(amountCellStyle);
					if (m.get("reason_for_block") == null) {
						cells[x++].setCellValue(String.valueOf(""));
					} else {
						cells[x++].setCellValue(String.valueOf(m.get("reason_for_block")));
					}
				}

			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;

	}


	public HSSFWorkbook createSpreadSheetFromListForCustomerAccountDetails(List list, Locale resolveLocale,MessageSource messageSource,String bankName,String fromDate,String toDate,String firstName) {
		List<String> header = initializeHeaderForCustomerAccountDetails(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		HSSFSheet sheet = workBook.createSheet("Customer_Account_Details");
		// insertBrandPicture(workBook);
		setTitle(workBook, header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		if(bankName != null){
			functionalityHeadingCell.setCellValue(bankName+" - "+messageSource.getMessage("CUSTOMER_ACC_DETAILS",null,resolveLocale));
		}else{
			functionalityHeadingCell.setCellValue(messageSource.getMessage("CUSTOMER_ACC_DETAILS",null,resolveLocale));
		}
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));

		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);			

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(1);
		functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
		HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(2);
		if(fromDate!=""){			
			Date date=DateUtil.stringToDate(fromDate);
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
		}else{
			Customer customerDetail = (Customer) list.get(0);
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(customerDetail.getCreatedDate()));	
		}		
		HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(3);
		functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
		HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(4);		
		if(toDate!=""){
			Date date=DateUtil.stringToDate(toDate);
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(date));
		}else{
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
		}

		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(5);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(6);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	

		functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));

		sheet.createRow(4);
		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column

		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	



		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);				
			}
			// *************** End of Column Headers ******************//

			Long totalBalance = 0L;
			// *************** Column data ******************//
			for (int k = 8, j = 1, tObject = 0; k < (list.size() + (headerRow.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {

				//System.out.println("headerRow : " + headerRow.getRowNum());

				HSSFRow row = sheet.createRow(k);

				int x=0;
				// Column: Sl.no.
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);
				Customer customerDetail = (Customer) list.get(tObject);

				++x;
				// Column: Customer Name
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (customerDetail.getFirstName() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(customerDetail.getFirstName()+" "+customerDetail.getLastName());
				}

				++x;
				// Column: Mobile Number
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (customerDetail.getMobileNumber() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(customerDetail.getMobileNumber());
				}
				
				++x;
				// Column: Bank
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (customerDetail.getCustomerAccounts() == null)
					cells[x].setCellValue(String.valueOf(""));
				else{
					Set<CustomerAccount> custAccount=customerDetail.getCustomerAccounts();
					for(CustomerAccount cus :custAccount){
						cells[x].setCellValue(cus.getBank().getBankName());
					}
				}

				/*++x;
				// Column: Branch
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (customerDetail.getCustomerAccounts() == null)
					cells[x].setCellValue(String.valueOf(""));
				else{
					Set<CustomerAccount> custAccount=customerDetail.getCustomerAccounts();
					for(CustomerAccount cus :custAccount){
						cells[x].setCellValue(cus.getBranch().getLocation());
					}
				}
*/
				++x;
				// Column: Customer profile
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (null == customerDetail.getCustomerProfiles() && customerDetail.getCustomerProfiles().getProfileName() == null) {
					cells[x].setCellValue("");
				} else {
					cells[x].setCellValue(customerDetail.getCustomerProfiles().getProfileName());
				}

				++x;
				// Column: Customer Type
				//@Start, by Murari, dated : 03-08-2018, purpose bug 5716
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (customerDetail.getType() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(Integer.valueOf(customerDetail.getType()).equals(FieldExecutiveEnum.CUSTOMER.getCode())){
						cells[2].setCellValue("Customer");
					}else if(Integer.valueOf(customerDetail.getType()).equals(FieldExecutiveEnum.AGENT.getCode())){
						cells[2].setCellValue("Agent");
					}else if(Integer.valueOf(customerDetail.getType()).equals(FieldExecutiveEnum.SOLE_MERCHANT.getCode())){
						cells[2].setCellValue("Sole Merchant");
					}
					else if(Integer.valueOf(customerDetail.getType()).equals(FieldExecutiveEnum.ASM.getCode())){
						cells[2].setCellValue("Agent SoleMerchamt");
					}else {
						cells[2].setCellValue(String.valueOf(""));
					}
				}
				//--@End---

				++x;
				// Column: Balance
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (customerDetail.getType() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {	
					Set<CustomerAccount> custAccount=customerDetail.getCustomerAccounts();
					for(CustomerAccount cus :custAccount){
						cells[x].setCellValue(cus.getAccount().getCurrentBalance());
						totalBalance = (long) (totalBalance+cus.getAccount().getCurrentBalance());
					}					
				}

				if(j==list.size()+1){

					HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
					cells[5] = row1.createCell(5);			
					cells[5].setCellStyle(getHeaderStyle(workBook));
					cells[5].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));
					cells[6] = row1.createCell(6);	
					cells[6].setCellStyle(rightCellStyle);
					Set<CustomerAccount> custAccount=customerDetail.getCustomerAccounts();
					for(CustomerAccount cus :custAccount){
						cells[6].setCellValue(totalBalance);

					}		

				}


			}// *************** End of Column data ******************//



		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}


	public HSSFWorkbook createSpreadSheetFromListForPendingTransactionSummary(List list, Locale resolveLocale,MessageSource messageSource,String bankName,String fromDate,String toDate) {

		List<String> header = initializeHeaderForPendingTxnDetails(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		HSSFSheet sheet = workBook.createSheet("Pending_Transaction_Summary");
		// insertBrandPicture(workBook);
		setTitle(workBook, header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		functionalityHeadingCell.setCellValue(bankName+" - "+messageSource.getMessage("PENDING_TXN_DETAILS",null,resolveLocale));
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));


		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);			

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(1);
		functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
		HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(2);
		if(fromDate!=""){
			Date date=DateUtil.stringToDate(fromDate);
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
		}else{
			PendingTransaction pendingTransaction = (PendingTransaction) list.get(0);
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(pendingTransaction.getTransactionDate()));	
		}		
		HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(4);
		functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
		HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(5);		
		if(toDate!=""){
			Date date=DateUtil.stringToDate(toDate);
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(date));
		}else{
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
		}

		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(8);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(9);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	

		functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));

		sheet.createRow(4);
		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

		HSSFPalette palette = workBook.getCustomPalette();
		// replacing the standard PALE_BLUE with cutomized PALE_BLUE
		palette.setColorAtIndex(HSSFColor.PALE_BLUE.index, (byte) 179, // RGB
				// red
				// (0-255)
				(byte) 217, // RGB green
				(byte) 255 // RGB blue
				);


		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			// *************** Column data ******************//

			Long totalCreditBalance = 0L;
			Long totalDebitBalance = 0L;

			for (int k = 8, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);
				int x=0;
				// Column: Sl.no.
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);
				PendingTransaction pendingTransaction = (PendingTransaction) list.get(tObject);

				++x;
				// Column: Customer Name
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (pendingTransaction.getCustomer().getFirstName() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(pendingTransaction.getCustomer().getFirstName()+" "+pendingTransaction.getCustomer().getLastName());
				}

				++x;
				// Column: Mobile Number
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (pendingTransaction.getCustomer().getMobileNumber() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(pendingTransaction.getCustomer().getMobileNumber());
				}

				/*++x;
				// Column: Branch
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (pendingTransaction.getCustomer().getMobileNumber() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {					
					Set<CustomerAccount> custAccount=pendingTransaction.getCustomer().getCustomerAccounts();
					for(CustomerAccount cus :custAccount){
						cells[x].setCellValue(cus.getBranch().getLocation());						
					}					
				}*/

				++x;
				// Column: Txn Type
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (pendingTransaction.getTransactionType().getTransactionType()  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(pendingTransaction.getTransactionType().getTransactionType().equals(EOTConstants.TXN_ID_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("Deposit",null,resolveLocale));
					}
					else if(pendingTransaction.getTransactionType().getTransactionType().equals(EOTConstants.TXN_ID_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("Withdrawl",null,resolveLocale));
					}

				}		

				++x;
				// Column: Date and Time of Operation
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(centerCellStyle);
				if (pendingTransaction.getTransactionDate() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
				//	cells[x].setCellValue(pendingTransaction.getTransactionDate().toString().split("\\.")[0]);
					String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(pendingTransaction.getTransactionDate());
					cells[x].setCellValue(createdDate);
				}

				++x;
				// Column: Initiated By
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (pendingTransaction.getInitiatedBy() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(pendingTransaction.getInitiatedBy());
				}

				++x;
				// Column: Approved By
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (pendingTransaction.getApprovedBy() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(pendingTransaction.getApprovedBy());
				}

				++x;
				if(pendingTransaction.getTransactionType().getTransactionType().equals(99)){
					// Column: Debit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (pendingTransaction.getAmount()  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(pendingTransaction.getAmount());
						totalDebitBalance = (long) (totalDebitBalance+pendingTransaction.getAmount());

					}
				}

				++x;
				if(pendingTransaction.getTransactionType().getTransactionType().equals(95)){
					// Column: Credit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (pendingTransaction.getAmount()  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						cells[x].setCellValue(pendingTransaction.getAmount());
						totalCreditBalance = (long) (totalCreditBalance+pendingTransaction.getAmount());

					}
				}




				++x;
				// Column: Status
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (pendingTransaction.getStatus() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(pendingTransaction.getStatus()==EOTConstants.REJECTED){
						cells[x].setCellValue(messageSource.getMessage("Rejected",null,resolveLocale));
					}else if(pendingTransaction.getStatus()==EOTConstants.PENDING){
						cells[x].setCellValue(messageSource.getMessage("Pending",null,resolveLocale));
					}else if(pendingTransaction.getStatus()==EOTConstants.APPROVED){
						cells[x].setCellValue(messageSource.getMessage("Approved",null,resolveLocale));
					}else if(pendingTransaction.getStatus()==EOTConstants.CANCELED){
						cells[x].setCellValue(messageSource.getMessage("Cancelled",null,resolveLocale));
					}
				}

				if(j==list.size()+1){			


					HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
					cells[7] = row1.createCell(7);			
					cells[7].setCellStyle(getHeaderStyle(workBook));
					cells[7].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));						

					cells[8] = row1.createCell(8);	
					cells[8].setCellStyle(rightCellStyle);
					cells[8].setCellValue(totalDebitBalance);

					cells[9] = row1.createCell(9);	
					cells[9].setCellStyle(rightCellStyle);
					cells[9].setCellValue(totalCreditBalance);


				}


			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;


	}
	public HSSFWorkbook createSpreadSheetFromListForTransactionSummaryForTxnSummary(List list, Locale resolveLocale, MessageSource messageSource,
			TxnSummaryDTO txnSummaryDTO, String bankName) {
		List<String> header = initializeHeaderForTxnSummary(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Transaction_Summary");

		
		//insertBrandPicture(workBook,header);


		setTitle(workBook, header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		if(bankName!=null){
			functionalityHeadingCell.setCellValue(bankName+" - "+messageSource.getMessage("TXN_SUMMARY",null,resolveLocale));
		}else{
			functionalityHeadingCell.setCellValue(messageSource.getMessage("TXN_SUMMARY",null,resolveLocale));	
		}
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));

		HSSFRow functionalityHeadingRow1 = sheet.createRow(6);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(0);
		functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
		//HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(2);
		HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(1);
		if(txnSummaryDTO.getFromDate()!=null){
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getFromDate()));	
		}else{
			Object[] obj=(Object[])list.get(0);
			Date date=DateUtil.dateAndTime1(obj[9].toString());
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
		}
		//HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(3);
		HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(2);
		functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
		//HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(4);		
		HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(3);
		if(txnSummaryDTO.getFromDate()!=null){
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getToDate()));
		}else{
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
		}

		//HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(6);
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(4);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(5);
		//HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(7);		

		functionalityHeadingCell7.setCellValue(txnSummaryDTO.getUserFirstName());	

		//HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(8);
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(6);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(7);
		//HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(9);		

//		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
		functionalityHeadingCell5.setCellValue(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));	
		


		functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));


		// create an header row
		HSSFRow headerRow = sheet.createRow(8);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	
		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			Double totalCreditBalance = 0D;
			Double totalDebitBalance = 0D;
			Double totalServiceChargeFee = 0D;
			Double totalBankServiceChargeFee = 0D;
			Double totalGIMServiceChargeFee = 0D;
			Double totalNoofTxns = 0D;
			Double totalStampFee = 0D;
			Double totalTax = 0D;
			DecimalFormat dec = new DecimalFormat("0.00");

			// *************** Column data ******************//
			for (int k = 9, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);

				int x=0;

				// Column: Sl.no. vinod
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);

				Object[] obj=(Object[])list.get(tObject);			


			/*	++x;
				// Column: Branch
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[3]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(String.valueOf(obj[3]));
				}*/


				++x;
				// Column: Txn Type
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[2]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(obj[2].equals(EOTConstants.TXN_ID_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("Deposit",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("Withdrawl",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)){
						cells[x].setCellValue(messageSource.getMessage("Balance_Enquiry",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_MINISTATEMENT)){
						cells[x].setCellValue(messageSource.getMessage("Mini_Statement",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TXNSTATEMENT)){
						cells[x].setCellValue(messageSource.getMessage("Txn_Statement",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_ACTIVATION)){
						cells[x].setCellValue(messageSource.getMessage("Activation",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TXNPINCHANGE)){
						cells[x].setCellValue(messageSource.getMessage("Txn_Pin_Change",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TRFDIRECT)){
						cells[x].setCellValue(messageSource.getMessage("TRF_DIRECT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_RESET_PIN)){
						cells[x].setCellValue(messageSource.getMessage("RESET_PIN",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_RESET_TXNPIN)){
						cells[x].setCellValue(messageSource.getMessage("RESET_TXN_PIN",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_SALE)){
						cells[x].setCellValue(messageSource.getMessage("SALE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TOPUP)){
						cells[x].setCellValue(messageSource.getMessage("TOP_UP",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_REACTIVATION)){
						cells[x].setCellValue(messageSource.getMessage("REACTIVATION",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_BILLPAYMENT)){
						cells[x].setCellValue(messageSource.getMessage("BILL_PAYMENT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_SMSCASH)){
						cells[x].setCellValue(messageSource.getMessage("SMS_CASH",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_ADDCARD)){
						cells[x].setCellValue(messageSource.getMessage("ADD_CARD",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CONFIRMCARD)){
						cells[x].setCellValue(messageSource.getMessage("CONFIRM_CARD",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_DELETECARD)){
						cells[x].setCellValue(messageSource.getMessage("DELETE_CARD",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_PAYMENTHISTORY)){
						cells[x].setCellValue(messageSource.getMessage("PAYMENT_HOSTORY",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_PINCHANGE)){
						cells[x].setCellValue(messageSource.getMessage("PIN_CHANGE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){
						cells[x].setCellValue(messageSource.getMessage("Adjustment",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){
						cells[x].setCellValue(messageSource.getMessage("Void",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_ADD_BANK_ACCOUNT)){
						cells[x].setCellValue(messageSource.getMessage("Add_Bank_Acc",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CHEQUE_STATUS)){
						cells[x].setCellValue(messageSource.getMessage("CHEQUE_STATUS",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_AGETNT_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("AGETNT_DEPOSIT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_AGENT_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("AGENT_WITHDRAWAL",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_COMMISSION_SHARE)){
						cells[x].setCellValue(messageSource.getMessage("COMMISSION_SHARE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_TYPE_LIMIT_UPDATE)){
						cells[x].setCellValue(messageSource.getMessage("LIMIT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_SMS_CASH_RECV)){
						cells[x].setCellValue(messageSource.getMessage("SMS_CASH_RECV",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_PAY)){
						cells[x].setCellValue(messageSource.getMessage("TXN_ID_PAY",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_FLOAT_MANAGEMENT)){
						cells[x].setCellValue(messageSource.getMessage("FLOAT_MANAGEMENT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CUSTOMER_PIN_CHANGE)){
						cells[x].setCellValue(messageSource.getMessage("CUSTOMER_PIN_CHANGE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_MERCHANT_PAY_OUT)){
						cells[x].setCellValue(messageSource.getMessage("MERCHANT_PAY_OUT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CUSTOMER_APPROVAL)){
						cells[x].setCellValue(messageSource.getMessage("CUST_APPROVAL_COMM",null,resolveLocale));
					}
				}

				++x;
				// Column: No of Txns
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[1]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Integer totalTxns1 = ((BigInteger)obj[1]).intValue();
					cells[x].setCellValue(totalTxns1);				
					Integer totalTxns = ((BigInteger)obj[1]).intValue();
					totalNoofTxns = (double) (totalNoofTxns+totalTxns);
				}

				++x;				
				if(obj[2].equals(EOTConstants.TXN_ID_WITHDRAWAL) || obj[2].equals(EOTConstants.TXN_ID_TRFDIRECT) || obj[2].equals(EOTConstants.TXN_ID_TOPUP) 
						|| obj[2].equals(EOTConstants.TXN_ID_BILLPAYMENT) || obj[2].equals(EOTConstants.TXN_ID_SMSCASH) || obj[2].equals(EOTConstants.TXN_ID_REVERSAL)
						|| (obj[2].equals(EOTConstants.TXN_ID_SMS_CASH_RECV) || (obj[2].equals(EOTConstants.TXN_ID_PAY) || obj[2].equals(EOTConstants.TXN_ID_AGETNT_DEPOSIT)
						||  obj[2].equals(EOTConstants.TXN_ID_FLOAT_MANAGEMENT)))){
					
					if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){
						
						// Column: Debit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[11]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[11];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[11];
							totalDebitBalance = (double) (totalDebitBalance+amount);

						}
						
					}else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){					
					// Column: Debit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[13]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[13];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[13];
						totalDebitBalance = (double) (totalDebitBalance+amount);

					}
				}else{
					// Column: Debit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[0]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[0];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[0];
						totalDebitBalance = (double) (totalDebitBalance+amount);

					}
				}
				}
				++x;
				if(obj[2].equals(EOTConstants.TXN_ID_MERCHANT_PAY_OUT) || obj[2].equals(EOTConstants.TXN_ID_DEPOSIT) || obj[2].equals(EOTConstants.TXN_ID_SALE) ||obj[2].equals(EOTConstants.TXN_ID_AGENT_WITHDRAWAL) || obj[2].equals(EOTConstants.TXN_ID_REVERSAL) || obj[2].equals(EOTConstants.TXN_ID_CANCEL)
						|| obj[2].equals(EOTConstants.TXN_ID_COMMISSION_SHARE) || obj[2].equals(EOTConstants.TXN_TYPE_LIMIT_UPDATE) || obj[2].equals(EOTConstants.TXN_ID_CUSTOMER_APPROVAL)){
					
				if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){					
					// Column: Credit
					/*cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[10]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[10];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[10];
						totalCreditBalance = (double) (totalCreditBalance+amount);

					}*/
					
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[0]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[0];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[0];
						totalCreditBalance = (double) (totalCreditBalance+amount);

					}
					
				}else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){					
					// Column: Credit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[12]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[12];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[12];
						totalCreditBalance = (double) (totalCreditBalance+amount);
					}				
				}else{
					// Column: Credit
					cells[x] = row.createCell(x);
					cells[x].setCellStyle(rightCellStyle);
					if (obj[0]  == null) {
						cells[x].setCellValue(String.valueOf(""));
					} else {
						Double amount1= (Double) obj[0];
						cells[x].setCellValue(amount1);
						Double amount= (Double) obj[0];
						totalCreditBalance = (double) (totalCreditBalance+amount);
					}				
				}
				}

				++x;
				// Column: Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[4]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[4];
					cells[x].setCellValue((dec.format(amount1)));				
					Double serChargeFee = (Double) obj[4];
					totalServiceChargeFee = (double) (totalServiceChargeFee+serChargeFee);
				}

				++x;
				// Column: Tax
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[5]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[5];
					cells[x].setCellValue((dec.format(amount1)));	
					Double tax = (Double) obj[5];
					totalTax = (double) (totalTax+tax);
				}

				/*++x;
				// Column: Stamp Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[6]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[6];
					cells[x].setCellValue((dec.format(amount1)));		
					Double stampFee = (Double) obj[6];
					totalStampFee = (double) (totalStampFee+stampFee);
				}*/

				++x;
				// Column: Bank Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[8]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double bankShare= (Double) obj[8];
					Double gimShare= (Double) obj[7];
					Double totalBankShare = bankShare-(null != gimShare ? gimShare : 0);
					cells[x].setCellValue((dec.format(totalBankShare)));
					totalBankServiceChargeFee = (double) (totalBankServiceChargeFee+totalBankShare);
				}
/*
				++x;
				// Column: GIM Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[7]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double gimShare= (Double) obj[7];
					cells[x].setCellValue((dec.format(gimShare)));
					Double GIMShareFee = (Double) obj[7];
					totalGIMServiceChargeFee = (double) (totalGIMServiceChargeFee+GIMShareFee);
				}*/



				if(j==list.size()+1){

					HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
					cells[1] = row1.createCell(1);			
					cells[1].setCellStyle(getHeaderStyle(workBook));
					cells[1].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));		

					cells[2] = row1.createCell(2);	
					cells[2].setCellStyle(rightCellStyle);
					cells[2].setCellValue(totalNoofTxns);

					cells[3] = row1.createCell(3);	
					cells[3].setCellStyle(rightCellStyle);
					cells[3].setCellValue(totalDebitBalance);

					cells[4] = row1.createCell(4);	
					cells[4].setCellStyle(rightCellStyle);
					cells[4].setCellValue(totalCreditBalance);

					cells[5] = row1.createCell(5);	
					cells[5].setCellStyle(rightCellStyle);
					cells[5].setCellValue((dec.format(totalServiceChargeFee)));

					cells[6] = row1.createCell(6);	
					cells[6].setCellStyle(rightCellStyle);
					cells[6].setCellValue((dec.format(totalTax)));

					/*cells[7] = row1.createCell(7);	
					cells[7].setCellStyle(rightCellStyle);
					cells[7].setCellValue((dec.format(totalStampFee)));*/

					cells[7] = row1.createCell(7);	
					cells[7].setCellStyle(rightCellStyle);
					cells[7].setCellValue((dec.format(totalBankServiceChargeFee)));

					/*cells[7] = row1.createCell(7);	
					cells[7].setCellStyle(rightCellStyle);
					cells[7].setCellValue((dec.format(totalGIMServiceChargeFee)));*/

				}


			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	
	public HSSFWorkbook createSpreadSheetFromListForTransactionSummaryPerBank(List list, Locale resolveLocale, MessageSource messageSource,
			TxnSummaryDTO txnSummaryDTO, String bankName) {
		List<String> header = initializeHeaderForTxnSummaryPerBank(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet("Transaction_Summary_Per_Bank");


		//insertBrandPicture(workBook,header);


		setTitle(workBook, header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		if(bankName!=null){
			functionalityHeadingCell.setCellValue(bankName+" - "+messageSource.getMessage("TXN_SUMMARY_BANK",null,resolveLocale));
		}else{
			functionalityHeadingCell.setCellValue(messageSource.getMessage("TXN_SUMMARY_BANK",null,resolveLocale));	
		}
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));

		HSSFRow functionalityHeadingRow1 = sheet.createRow(6);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(2);
		functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
		HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(3);
		if(txnSummaryDTO.getFromDate()!=null){
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getFromDate()));	
		}else{
			Object[] obj=(Object[])list.get(0);
			Date date=DateUtil.dateAndTime1(obj[9].toString());
			functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
		}
		HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(4);
		functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
		HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(5);		
		if(txnSummaryDTO.getFromDate()!=null){
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(txnSummaryDTO.getToDate()));
		}else{
			functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
		}
		
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(7);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(8);		

		functionalityHeadingCell7.setCellValue(txnSummaryDTO.getUserFirstName());	


		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(9);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(10);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	


		functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));


		// create an header row
		HSSFRow headerRow = sheet.createRow(8);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	
		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			Double totalCreditBalance = 0D;
			Double totalDebitBalance = 0D;
			Double totalServiceChargeFee = 0D;
			Double totalBankServiceChargeFee = 0D;
			Double totalGIMServiceChargeFee = 0D;
			Double totalNoofTxns = 0D;
			Double totalStampFee = 0D;
			Double totalTax = 0D;
			DecimalFormat dec = new DecimalFormat("0.00");

			// *************** Column data ******************//
			for (int k = 9, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);

				int x=0;

				// Column: Sl.no.
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);

				Object[] obj=(Object[])list.get(tObject);			


				++x;
				// Column: Branch
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[3]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(String.valueOf(obj[3]));
				}


				++x;
				// Column: Txn Type
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[2]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					if(obj[2].equals(EOTConstants.TXN_ID_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("Deposit",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("Withdrawl",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)){
						cells[x].setCellValue(messageSource.getMessage("Balance_Enquiry",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_MINISTATEMENT)){
						cells[x].setCellValue(messageSource.getMessage("Mini_Statement",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TXNSTATEMENT)){
						cells[x].setCellValue(messageSource.getMessage("Txn_Statement",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_ACTIVATION)){
						cells[x].setCellValue(messageSource.getMessage("Activation",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TXNPINCHANGE)){
						cells[x].setCellValue(messageSource.getMessage("Txn_Pin_Change",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TRFDIRECT)){
						cells[x].setCellValue(messageSource.getMessage("TRF_DIRECT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_RESET_PIN)){
						cells[x].setCellValue(messageSource.getMessage("RESET_PIN",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_RESET_TXNPIN)){
						cells[x].setCellValue(messageSource.getMessage("RESET_TXN_PIN",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_SALE)){
						cells[x].setCellValue(messageSource.getMessage("SALE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_TOPUP)){
						cells[x].setCellValue(messageSource.getMessage("TOP_UP",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_REACTIVATION)){
						cells[x].setCellValue(messageSource.getMessage("REACTIVATION",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_BILLPAYMENT)){
						cells[x].setCellValue(messageSource.getMessage("BILL_PAYMENT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_SMSCASH)){
						cells[x].setCellValue(messageSource.getMessage("SMS_CASH",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_ADDCARD)){
						cells[x].setCellValue(messageSource.getMessage("ADD_CARD",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CONFIRMCARD)){
						cells[x].setCellValue(messageSource.getMessage("CONFIRM_CARD",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_DELETECARD)){
						cells[x].setCellValue(messageSource.getMessage("DELETE_CARD",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_PAYMENTHISTORY)){
						cells[x].setCellValue(messageSource.getMessage("PAYMENT_HOSTORY",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_PINCHANGE)){
						cells[x].setCellValue(messageSource.getMessage("PIN_CHANGE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){
						cells[x].setCellValue(messageSource.getMessage("Adjustment",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){
						cells[x].setCellValue(messageSource.getMessage("Void",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_ADD_BANK_ACCOUNT)){
						cells[x].setCellValue(messageSource.getMessage("Add_Bank_Acc",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_CHEQUE_STATUS)){
						cells[x].setCellValue(messageSource.getMessage("CHEQUE_STATUS",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_AGETNT_DEPOSIT)){
						cells[x].setCellValue(messageSource.getMessage("AGETNT_DEPOSIT",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_AGENT_WITHDRAWAL)){
						cells[x].setCellValue(messageSource.getMessage("AGENT_WITHDRAWAL",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_ID_COMMISSION_SHARE)){
						cells[x].setCellValue(messageSource.getMessage("COMMISSION_SHARE",null,resolveLocale));
					}
					else if(obj[2].equals(EOTConstants.TXN_TYPE_LIMIT_UPDATE)){
						cells[x].setCellValue(messageSource.getMessage("LIMIT",null,resolveLocale));
					}
				}

				++x;
				// Column: No of Txns
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[1]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Integer totalTxns1 = ((BigInteger)obj[1]).intValue();
					cells[x].setCellValue(totalTxns1);				
					Integer totalTxns = ((BigInteger)obj[1]).intValue();
					totalNoofTxns = (double) (totalNoofTxns+totalTxns);
				}


				++x;				
				if(obj[2].equals(EOTConstants.TXN_ID_WITHDRAWAL) || obj[2].equals(EOTConstants.TXN_ID_TRFDIRECT) || obj[2].equals(EOTConstants.TXN_ID_TOPUP) || obj[2].equals(EOTConstants.TXN_ID_BILLPAYMENT) || obj[2].equals(EOTConstants.TXN_ID_SMSCASH) || obj[2].equals(EOTConstants.TXN_ID_REVERSAL) || obj[2].equals(EOTConstants.TXN_ID_CANCEL)){
					
					if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){
						
						// Column: Debit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[11]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[11];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[11];
							totalDebitBalance = (double) (totalDebitBalance+amount);

						}
					}else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){					
						// Column: Credit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[13]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[13];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[13];
							totalDebitBalance = (double) (totalDebitBalance+amount);
						}				
					}else{						
						// Column: Debit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[0]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[0];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[0];
							totalDebitBalance = (double) (totalDebitBalance+amount);

						}
				}
				}			
				
				++x;
				if(obj[2].equals(EOTConstants.TXN_ID_DEPOSIT) || obj[2].equals(EOTConstants.TXN_ID_SALE) || obj[2].equals(EOTConstants.TXN_ID_REVERSAL) || obj[2].equals(EOTConstants.TXN_ID_CANCEL) ){
					if(obj[2].equals(EOTConstants.TXN_ID_REVERSAL)){					
						// Column: Credit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[10]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[10];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[10];
							totalCreditBalance = (double) (totalCreditBalance+amount);

						}
						
					}else if(obj[2].equals(EOTConstants.TXN_ID_CANCEL)){					
						// Column: Credit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[12]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[12];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[12];
							totalCreditBalance = (double) (totalCreditBalance+amount);
						}				
					}else{					
						// Column: Credit
						cells[x] = row.createCell(x);
						cells[x].setCellStyle(rightCellStyle);
						if (obj[0]  == null) {
							cells[x].setCellValue(String.valueOf(""));
						} else {
							Double amount1= (Double) obj[0];
							cells[x].setCellValue(amount1);
							Double amount= (Double) obj[0];
							totalCreditBalance = (double) (totalCreditBalance+amount);
						}				
					}
					}

				++x;
				// Column: Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[4]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[4];
					cells[x].setCellValue((dec.format(amount1)));				
					Double serChargeFee = (Double) obj[4];
					totalServiceChargeFee = (double) (totalServiceChargeFee+serChargeFee);
				}

				++x;
				// Column: Tax
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[5]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[5];
					cells[x].setCellValue((dec.format(amount1)));	
					Double tax = (Double) obj[5];
					totalTax = (double) (totalTax+tax);
				}

				++x;
				// Column: Stamp Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[6]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[6];
					cells[x].setCellValue((dec.format(amount1)));	
					Double stampFee = (Double) obj[6];
					totalStampFee = (double) (totalStampFee+stampFee);
				}

				++x;
				// Column: Bank Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[8]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double bankShare= (Double) obj[8];
					Double gimShare= (Double) obj[7];
					Double totalBankShare = bankShare-gimShare;
					cells[x].setCellValue((dec.format(totalBankShare)));
					totalBankServiceChargeFee = (double) (totalBankServiceChargeFee+totalBankShare);
				}

				++x;
				// Column: GIM Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[7]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double gimShare= (Double) obj[7];
					cells[x].setCellValue((dec.format(gimShare)));
					Double GIMShareFee = (Double) obj[7];
					totalGIMServiceChargeFee = (double) (totalGIMServiceChargeFee+GIMShareFee);
				}



				if(j==list.size()+1){

					HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
					cells[2] = row1.createCell(2);			
					cells[2].setCellStyle(getHeaderStyle(workBook));
					cells[2].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));		

					cells[3] = row1.createCell(3);	
					cells[3].setCellStyle(rightCellStyle);
					cells[3].setCellValue(totalNoofTxns);

					cells[4] = row1.createCell(4);	
					cells[4].setCellStyle(rightCellStyle);
					cells[4].setCellValue(totalDebitBalance);

					cells[5] = row1.createCell(5);	
					cells[5].setCellStyle(rightCellStyle);
					cells[5].setCellValue(totalCreditBalance);

					cells[6] = row1.createCell(6);	
					cells[6].setCellStyle(rightCellStyle);
					cells[6].setCellValue((dec.format(totalServiceChargeFee)));

					cells[7] = row1.createCell(7);	
					cells[7].setCellStyle(rightCellStyle);
					cells[7].setCellValue((dec.format(totalTax)));

					cells[8] = row1.createCell(8);	
					cells[8].setCellStyle(rightCellStyle);
					cells[8].setCellValue((dec.format(totalStampFee)));

					cells[9] = row1.createCell(9);	
					cells[9].setCellStyle(rightCellStyle);
					cells[9].setCellValue((dec.format(totalBankServiceChargeFee)));

					cells[10] = row1.createCell(10);	
					cells[10].setCellStyle(rightCellStyle);
					cells[10].setCellValue((dec.format(totalGIMServiceChargeFee)));


				}


			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	public HSSFWorkbook createSpreadSheetFromListForExternalTransactions(List list, Locale resolveLocale,MessageSource messageSource,
			ExternalTransactionDTO externalTransactionDTO) {
		List<String> header = initializeHeaderForExternalTransactions(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		HSSFSheet sheet = workBook.createSheet("External_Transactions");
		// insertBrandPicture(workBook);
		setTitle(workBook,header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		
		functionalityHeadingCell.setCellValue(messageSource.getMessage("EXTERNAL_TXN_DETAILS",null,resolveLocale));
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);			

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		
		
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(5);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(6);		

		functionalityHeadingCell7.setCellValue(externalTransactionDTO.getUserName());

		
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(8);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(9);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	

		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

		sheet.createRow(4);

		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//
			Double totalServiceChargeFee = 0D;
			Double totalBankServiceChargeFee = 0D;
			Double totalGIMServiceChargeFee = 0D;
			Double totalOperatorShare = 0D;
			Double totalTax = 0D;
			Double totalAmt = 0D;
			DecimalFormat dec = new DecimalFormat("0.00");

			// *************** Column data ******************//
			for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);
				
				int x=0;
				// Column: Sl.no.
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);
				Object[] obj=(Object[])list.get(tObject);
				
                ++x;
				// Column: Operator
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[2] == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj[2].toString());
				}
				
				 ++x;

				// Column: Mobile Number
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[3] == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj[3].toString());
				}
				
				 ++x;

				// Column: Benificairy
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[4] == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj[4].toString());
				}
				
				 ++x;

				// Column: Amount
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[5] == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount= (Double) obj[5];
					cells[x].setCellValue((dec.format(amount)));
					Double amt = (Double) obj[5];
					totalAmt = (double) (totalAmt+amt);
				}
				 ++x;
				// Column: Transaction ID
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[1] == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj[1].toString());
				}
				++x;

				// Column: Transaction Type
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[6].equals(EOTConstants.TXN_ID_SMSCASH_OTHERS)) {
					cells[x].setCellValue(messageSource.getMessage("SMS_CASH_OTHERS",null,resolveLocale));
				} else {
					cells[x].setCellValue(messageSource.getMessage("TRF_DIRECT",null,resolveLocale));
				}
				
				++x;
				// Column: Date
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj[7] == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj[7].toString());
				}

				

				++x;
				// Column: Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[10]  == null) {
					cells[x].setCellValue((dec.format(0)));
				} else {					
					Double amount1= (Double) obj[10];
					cells[x].setCellValue((dec.format(amount1)));
					Double serChargeFee = (Double) obj[10];
					totalServiceChargeFee = (double) (totalServiceChargeFee+serChargeFee);

				}



				++x;
				// Column: Tax Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[11]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double amount1= (Double) obj[11];					
					cells[x].setCellValue((dec.format(amount1)));					
					Double GIMShareFee = (Double) obj[11];
					totalTax = (double) (totalTax+GIMShareFee);

				}


				++x;
				// Column: Bank Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[12]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double bankShare= (Double) obj[12];
					Double gimShare= (Double) obj[13];
					Double totalBankShare=bankShare-gimShare;
					cells[x].setCellValue((dec.format(totalBankShare)));
					totalBankServiceChargeFee = (double) (totalBankServiceChargeFee+totalBankShare);
				}

				++x;
				// Column: GIM Share Fee
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj[13]  == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					Double gimShare= (Double) obj[13];
					cells[x].setCellValue((dec.format(gimShare)));
					Double GIMShareFee = (Double) obj[13];
					totalGIMServiceChargeFee = (double) (totalGIMServiceChargeFee+GIMShareFee);

				}
				
				if(j==list.size()+1){

					HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
					cells[3] = row1.createCell(3);			
					cells[3].setCellStyle(getHeaderStyle(workBook));
					cells[3].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));	
					
					cells[4] = row1.createCell(4);			
					cells[4].setCellStyle(rightCellStyle);
					cells[4].setCellValue((dec.format(totalAmt)));	

					cells[8] = row1.createCell(8);	
					cells[8].setCellStyle(rightCellStyle);
					cells[8].setCellValue((dec.format(totalServiceChargeFee)));

					cells[9] = row1.createCell(9);	
					cells[9].setCellStyle(rightCellStyle);
					cells[9].setCellValue((dec.format(totalTax)));

					cells[10] = row1.createCell(10);	
					cells[10].setCellStyle(rightCellStyle);
					cells[10].setCellValue((dec.format(totalBankServiceChargeFee)));

					cells[11] = row1.createCell(11);	
					cells[11].setCellStyle(rightCellStyle);
					cells[11].setCellValue((dec.format(totalGIMServiceChargeFee)));


				}

				



			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	public HSSFWorkbook createCSVForFailedBulkUpload(List list, Locale resolveLocale, MessageSource messageSource) {
		
		List<String> header = initializeHeaderForFailedBulkUpload(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		HSSFSheet sheet = workBook.createSheet("TXN_SUPPORT_INST");
		// insertBrandPicture(workBook);
		setTitle(workBook,header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		
		functionalityHeadingCell.setCellValue(messageSource.getMessage("TXN_FILE_UPLOADING",null,resolveLocale));
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	


		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		
		
		/*HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(5);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(6);	

		functionalityHeadingCell7.setCellValue(externalTransactionDTO.getUserName());

		
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(7);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(8);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));		

		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));*/

		sheet.createRow(4);

		// create an header row
		HSSFRow headerRow = sheet.createRow(5);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	
		DecimalFormat df = new DecimalFormat("#.##");

		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			// *************** Column data ******************//
			for (int k =6, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);
				
				int x=0;
				// Column: Sl.no.
				cells[x] = row.createCell(x);
				cells[x].setCellValue(j++);
				cells[x].setCellStyle(centerCellStyle);
				TransactionParamDTO obj=(TransactionParamDTO)list.get(tObject);
				
                ++x;
				// Column: Mobile number
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj.getMobileNumber() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getMobileNumber().toString());
				}
				
				 ++x;

				// Column: Account Alias
				/*cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj.getAccountAlias() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getAccountAlias().toString());
				}
				
				++x;*/

				// Column: Name
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(amountCellStyle);
				if (obj.getCustomerName() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getCustomerName().toString());
				}
				
				
				++x;
				// Column: Amount
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj.getAmount() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getAmount().toString());
				}
				
				++x;
				// Column: Service Charge
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj.getAmount() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(null != obj.getServiceCharge() ? df.format(obj.getServiceCharge()):"0");
				}
				
				++x;
				// Column: Desc
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj.getDescription() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getDescription().toString());
				}
				
				++x;
				// Column: Status
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj.getAmount() == null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getStatus());
				}
				
			
				++x;
				// Column: Error Desc
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				if (obj.getErrorDescription()== null) {
					cells[x].setCellValue(String.valueOf(""));
				} else {
					cells[x].setCellValue(obj.getErrorDescription().toString());
				}
				

				/*++x;
				// Column:Date
				cells[x] = row.createCell(x);
				cells[x].setCellStyle(rightCellStyle);
				Date date=DateUtil.dateAndTime1(new Date().toString());
				cells[x].setCellValue(DateUtil.formatDateToStr(date));*/
				
			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	
	
	public HSSFWorkbook createSpreadSheetForsettlementNetBalance(List list, Locale resolveLocale,MessageSource messageSource,ClearingHouseDTO clearingHouseDTO)
	{
	
		List<String> header = initializeHeaderForSettlementBalance(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		
		HSSFSheet sheet = workBook.createSheet("Settlement_Net_Balance");
		// insertBrandPicture(workBook);
		setTitle(workBook,header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		
		functionalityHeadingCell.setCellValue("SETTLEMENT_BALANCE_DETAILS");
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);			

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(1);
		functionalityHeadingCell4.setCellValue("PRINTED_DATE");
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(2);		

		functionalityHeadingCell6.setCellValue(DateUtil.dateAndTime(new Date()));	

		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		/*functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));
*/
		sheet.createRow(4);

		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	


		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			// *************** Column data ******************//
			for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);
				// Column: Sl.no.
				cells[0] = row.createCell(0);
				cells[0].setCellStyle(centerCellStyle);
				cells[0].setCellValue(j++);
				Map<String, Object> m = (Map<String, Object>) list.get(tObject);
								
			
				cells[1] = row.createCell(1);
				cells[1].setCellStyle(amountCellStyle);
				cells[1].setCellValue((Double) m.get("Amount"));

				cells[2] = row.createCell(2);
				cells[2].setCellStyle(amountCellStyle);
				
				String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("JournalTime"));
			    cells[2].setCellValue(createdDate);


				cells[3] = row.createCell(3);
				cells[3].setCellStyle(amountCellStyle);
				cells[3].setCellValue((Double) m.get("GIMIntra"));
				
				
				
			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	
	
	public HSSFWorkbook createSpreadSheetForLocations(List list, Locale resolveLocale,MessageSource messageSource, WebUser webUser, String pageHeader)
	{
	
		List<String> header = initializeHeaderForLocations(resolveLocale,messageSource);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		
		HSSFSheet sheet = workBook.createSheet("m-GURUSH");
		// insertBrandPicture(workBook);
		setTitle(workBook,header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow
				.createCell(0);
		
		functionalityHeadingCell.setCellValue(messageSource.getMessage(pageHeader,null,resolveLocale));
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);			

		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		
		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
		functionalityHeadingCell7.setCellValue(webUser.getUserName());
		
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	

		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));
		/*functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));
*/
		sheet.createRow(4);

		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	


		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			// *************** Column data ******************//
			for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);

				// Column: Sl.no.
				cells[0] = row.createCell(0);
				cells[0].setCellStyle(centerCellStyle);
				cells[0].setCellValue(j++);
				Country location = (Country) list.get(tObject);
				
				// Column: Country Name
				cells[1] = row.createCell(1);
				cells[1].setCellStyle(amountCellStyle);
				if (location.getCountry() == null || "".equals(location.getCountry())) {
					cells[1].setCellValue(String.valueOf(""));
				} else {
					cells[1].setCellValue(location.getCountry());
				}
				

				// Column: Alpha(2)
				cells[2] = row.createCell(2);
				cells[2].setCellStyle(amountCellStyle);
				if (location.getCountryCodeAlpha2() == null || location.getCountryCodeAlpha2().equals("")) {
					cells[2].setCellValue(String.valueOf(""));
				} else {
					cells[2].setCellValue(location.getCountryCodeAlpha2());
				}


				// Column: ISD Code
				cells[3] = row.createCell(3);
				cells[3].setCellStyle(amountCellStyle);
				if (location.getIsdCode() == null) {
					cells[3].setCellValue(String.valueOf(""));
				} else {
					cells[3].setCellValue(location.getIsdCode().toString());
				}
			    
				// Column: Mobile number Length
				cells[4] = row.createCell(4);
				cells[4].setCellStyle(amountCellStyle);
				if (location.getMobileNumberLength() == null) {
					cells[4].setCellValue(String.valueOf(""));
				} else {
					cells[4].setCellValue(location.getMobileNumberLength().toString());
				}
				
				// Column: Currency
				cells[5] = row.createCell(5);
				cells[5].setCellStyle(amountCellStyle);
				if (location.getCurrency() == null) {
					cells[5].setCellValue(String.valueOf(""));
				} else {
					cells[5].setCellValue(location.getCurrency().getCurrencyName());
				}
				
			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;
	}
	
public HSSFWorkbook createSpreadSheetFromListForBusinessPartner(List<BusinessPartner> list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader, String entityCode, Integer role) {
		
		List<String> header = initializeHeaderForBusinessPartner(resolveLocale,messageSource,role);
		HSSFWorkbook workBook = new HSSFWorkbook();
		// create a new worksheet
		
		HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
		// insertBrandPicture(workBook);
		setTitle(workBook,header);

		sheet.createRow(2);
		HSSFRow functionalityHeadingRow = sheet.createRow(3);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);
	
		functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
		functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

		HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
		

		HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
		functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
		HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
		functionalityHeadingCell7.setCellValue(webUser.getUserName());
		
		HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
		functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
		HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

		functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
		
		//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
		//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
		functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

		sheet.createRow(4);

		// create an header row
		HSSFRow headerRow = sheet.createRow(7);
		// create a style for the Column Headers
		HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

		HSSFCell cells[] = new HSSFCell[header.size() + 1];
		HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

		// create a style for the Column data except Amount column
		HSSFCellStyle amountCellStyle = workBook.createCellStyle();
		amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

		HSSFCellStyle rightCellStyle = workBook.createCellStyle();
		rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

		HSSFCellStyle centerCellStyle = workBook.createCellStyle();
		centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

		for (Iterator<String> it = header.iterator(); it.hasNext();) {
			// *************** Start of Column Headers ******************//
			for (int i = 0; i < header.size(); i++) {
				cells[i] = headerRow.createCell(i);
				cells[i].setCellValue(it.next());
				cells[i].setCellStyle(labelHeaderStyle);
			}
			// *************** End of Column Headers ******************//

			// *************** Column data ******************//
			for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
					.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
				HSSFRow row = sheet.createRow(k);
				// Column: Sl.no.
				cells[0] = row.createCell(0);
				cells[0].setCellValue(j++);
				cells[0].setCellStyle(centerCellStyle);
				BusinessPartner user = (BusinessPartner) list.get(tObject);
				
				// Column: Super Agent Code
				cells[1] = row.createCell(1);
				cells[1].setCellStyle(amountCellStyle);
				if (user.getCode() == null) {
					cells[1].setCellValue(String.valueOf(""));
				} else {
					cells[1].setCellValue(user.getCode());
				}
				
				// Column: Business partner
				cells[2] = row.createCell(2);
				cells[2].setCellStyle(amountCellStyle);
				if (user.getName() == null) {
					cells[2].setCellValue(String.valueOf(""));
				} else {
					cells[2].setCellValue(user.getName());
				}
				
				
				// Column: Business partner type
				cells[3] = row.createCell(3);
				cells[3].setCellStyle(amountCellStyle);
				Integer partnerType = user.getPartnerType();
				String type="";
				if (partnerType == null)
					cells[3].setCellValue(String.valueOf(""));
				else{
					if (partnerType.intValue() == 1) {
						type="Principal Agent";
					}else if (partnerType.intValue() == 2) {
						type="Super Agent ";
					}else {
						type="Super Agent L2";
					}
					cells[3].setCellValue(type);
					}
				
				// Column: Contact Number
				cells[4] = row.createCell(4);
				cells[4].setCellStyle(amountCellStyle);
				if (user.getContactNumber() == null)
					cells[4].setCellValue(String.valueOf(""));
				else{
					cells[4].setCellValue(user.getContactPersonPhone());
					}

				
				// Column: Created Date
				cells[5] = row.createCell(5);
				cells[5].setCellStyle(centerCellStyle);
				if (user.getCreatedDate() == null) {
					cells[5].setCellValue(String.valueOf(""));
				} else {
				//	String date1=DateUtil.formatDateToStr(user.getCreatedDate());
					String date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(user.getCreatedDate());
					cells[5].setCellValue(date1);

				}	
				
			}// *************** End of Column data ******************//
		}

		for (int i = 0; i < header.size(); i++) {
			sheet.autoSizeColumn(i);
		}
		return workBook;

	}


public HSSFWorkbook createSpreadSheetFromListForMIS(List list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader) {
	
	List<String> header = initializeHeaderForMISReport(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			
			/// Column: TXN Date
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(amountCellStyle);
		//	String createdDate = new SimpleDateFormat("dd-MM-yyyy").format(m.get("TransactionDate"));
			if(null != m.get("TransactionDate")) {
				String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("TransactionDate"));
				cells[1].setCellValue(createdDate);
			}else
				cells[1].setCellValue("");
			
			// Column: Transaction Id
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(centerCellStyle);
			cells[2].setCellValue(null !=m.get("TransactionID")?m.get("TransactionID").toString():"");
			
			/// Column: TXN Type
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(amountCellStyle);
			cells[3].setCellValue(null !=m.get("Description")?m.get("Description").toString():"");
//			try {
//				cells[3].setCellValue(transactionService.getTransactionType((int)m.get("TransactionType")).getDescription());				
//			} catch (EOTException e) {
//				e.printStackTrace();
//			}
			
			// Column: Ref_TXN_ID
			String RefTranID = m.get("Ref_TXN_ID")+"";
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			cells[4].setCellValue(!RefTranID.equals("null") ? RefTranID : "");
			
			// Column: name
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(amountCellStyle);
			cells[5].setCellValue(null !=m.get("InitName")?m.get("InitName").toString():"");
			
			// Column: Mobile Number
			cells[6] = row.createCell(6);
			cells[6].setCellStyle(amountCellStyle);
			cells[6].setCellValue(null !=m.get("InitMobile")?m.get("InitMobile").toString():"");

			
			// Column: User Name
			cells[7] = row.createCell(7);
			cells[7].setCellStyle(amountCellStyle);
			cells[7].setCellValue(m.get("UserName") == null ? " " : m.get("UserName").toString());
			
			// Column: Agent Code
			cells[8] = row.createCell(8);
			cells[8].setCellStyle(amountCellStyle);
			cells[8].setCellValue(m.get("BenfCode") == null ? " " : m.get("BenfCode").toString());
			
			/// Column: Amount
			DecimalFormat format = new DecimalFormat("#0.00");
			cells[9] = row.createCell(9);
			cells[9].setCellStyle(rightCellStyle);
			String amount = format.format(m.get("Amount"));
			amount = amount.equals(".00") == true ? "0.00" : amount;
			cells[9].setCellValue(amount);
			
		  /// Column: Service Charges
			cells[10] = row.createCell(10);
			cells[10].setCellStyle(rightCellStyle);
			
			if (m.get("SC") != null) {
				String serviceCharge = format.format(m.get("SC"));
				serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;							
				cells[10].setCellValue(serviceCharge);
			}
			else
				cells[10].setCellValue("");	
			
			// Column: BenOrCustomerName
			cells[11] = row.createCell(11);
			cells[11].setCellStyle(amountCellStyle);
			cells[11].setCellValue(null !=m.get("BenfName")?m.get("BenfName").toString():"");
			
			// Column: BenOrCustomerMobileNumber
			cells[12] = row.createCell(12);
			cells[12].setCellStyle(amountCellStyle);
			cells[12].setCellValue(null !=m.get("BenfMobile")?m.get("BenfMobile").toString():"");
	
			// Column: SuperAgentName
			String name = m.get("Name")+"";
			cells[13] = row.createCell(13);
			cells[13].setCellStyle(amountCellStyle);
			cells[13].setCellValue(!name.equals("null") ? name : "");
			
			// Column: SuperAgentCode
			String code = m.get("Code")+"";
			cells[14] = row.createCell(14);
			cells[14].setCellStyle(amountCellStyle);
			cells[14].setCellValue(!code.equals("null") ? code : "");
			
			/// Column: Status
			cells[15] = row.createCell(15);
			cells[15].setCellStyle(amountCellStyle);
			if (null != m.get("Status")) {
				if (m.get("Status").equals(2000)) 
					cells[15].setCellValue("Success");
				else       
					cells[15].setCellValue("Failed");
			}else
				cells[15].setCellValue("");
			/// Column: Description
			cells[16] = row.createCell(16);
			cells[16].setCellStyle(amountCellStyle);
				if (null != m.get("Status")) {
				if (m.get("Status").equals(2000)) 
					cells[16].setCellValue("Success");
				else {
					String description = "";
					if(m.get("Status").equals(2001))
						description = EOTConstants.FATAL_ERROR;
					else if(m.get("Status").equals(2002))
						description = EOTConstants.MISSING_PARAMETERS_ERROR;
					else if(m.get("Status").equals(2003))
						description = EOTConstants.INVALID_PARAMETERS_ERROR;
					else if(m.get("Status").equals(2004))
						description = EOTConstants.OPERATION_NOT_SUPPORTED_ERROR;
					else if(m.get("Status").equals(2005))
						description = EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR;
					else if(m.get("Status").equals(2006))
						description = EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR;
					else if(m.get("Status").equals(2020))
						description = EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR;
					else if(m.get("Status").equals(2021))
						description = EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR;
					else if(m.get("Status").equals(2022))
						description = EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR;
				else if(m.get("Status").equals(2023))
					description = EOTConstants.NO_VOUCHER_AVAILABLE_ERROR;
				else if(m.get("Status").equals(2024))
					description = EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR;
				else if(m.get("Status").equals(2025))
					description = EOTConstants.TXN_RULE_NOT_DEFINED_ERROR;
				else if(m.get("Status").equals(2026))
					description = EOTConstants.TXN_LIMIT_EXCEEDED_ERROR;
				else if(m.get("Status").equals(2027))
					description = EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR;
				else if(m.get("Status").equals(2028))
					description = EOTConstants.TXN_NUM_EXCEEDED_ERROR;
				else if(m.get("Status").equals(2029))
					description = EOTConstants.INVALID_TXN_RULE_ERROR;
				else if(m.get("Status").equals(2030))
					description = EOTConstants.NO_BALANCE_IN_WALLET_ERROR;
				else if(m.get("Status").equals(2031))
					description = EOTConstants.TXN_DECLINED_FROM_HPS_ERROR;
				else if(m.get("Status").equals(2032))
					description = EOTConstants.INVALID_CH_POOL_ERROR;
				else if(m.get("Status").equals(2033))
					description = EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR;
				else if(m.get("Status").equals(2034))
					description = EOTConstants.SETTLED_TRANSACTION_ERROR;
				else if(m.get("Status").equals(2035))
					description = EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR;
				else if(m.get("Status").equals(2036))
					description = EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR;
				else if(m.get("Status").equals(2037))
					description = EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR;
				else if(m.get("Status").equals(2038))
					description = EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR;
				else if(m.get("Status").equals(2039))
					description = EOTConstants.INVALID_SIGNATURE_SIZE_ERROR;
				else if(m.get("Status").equals(2040))
					description = EOTConstants.INVALID_IDPROOF_SIZE_ERROR;
				else if(m.get("Status").equals(2041))
					description = EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR;
				cells[16].setCellValue(description);	
				}				
			}else
				cells[16].setCellValue("");
			/// Column: Request Channel
			cells[17] = row.createCell(17);
			cells[17].setCellStyle(centerCellStyle);
			       
			cells[17].setCellValue(m.get("requestChannel") == null ? " " : m.get("requestChannel").toString());
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForSCR(List<ServiceChargeRule> list,Locale resolveLocale,MessageSource messageSource, String userName, String reportHeader) {
	
	List<String> header = initializeHeaderForSCR(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(userName);
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			ServiceChargeRule rule = (ServiceChargeRule) list.get(tObject);

			// Column: Rule Level
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(amountCellStyle);
			String ruleLevel="";
			if (rule.getRuleLevel() == 1) {
				ruleLevel="Global";
			}else if (rule.getRuleLevel() == 2) {
				ruleLevel="Bank Group";
			}else if (rule.getRuleLevel() == 3) {
				ruleLevel="Customer Profile";
			}else {
				ruleLevel="Inter Bank";
			}
			cells[1].setCellValue(ruleLevel);
			
			
			// Column: Rule Name
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(amountCellStyle);
			String serviceChargeRuleName = rule.getServiceChargeRuleName();
			if (serviceChargeRuleName == null)
				cells[2].setCellValue(String.valueOf(""));
			else{
				cells[2].setCellValue(serviceChargeRuleName);
				}
			
		// Column: Transaction Type Name
		cells[3] = row.createCell(3);
		cells[3].setCellStyle(amountCellStyle);
		Set<ServiceChargeRuleTxn> serviceChargeRuleTxns = rule.getServiceChargeRuleTxns();
		if (serviceChargeRuleTxns == null)
			cells[3].setCellValue(String.valueOf(""));
		else{	
				/*for (ServiceChargeRuleTxn serviceChargeRuleTxn : serviceChargeRuleTxns) {
					if (serviceChargeRuleTxn.getSourceType().intValue() == 1) {
						serviceChargeRuleName=serviceChargeRuleName +" - Wallet";
					}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  2) {
						serviceChargeRuleName=serviceChargeRuleName +" - Card";
					}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  3) {
						serviceChargeRuleName=serviceChargeRuleName +" - Bank Account";
					}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  4) {
						serviceChargeRuleName=serviceChargeRuleName +" - FI";
					}else {
						serviceChargeRuleName=serviceChargeRuleName +" - Others";
					}
				}*/
			// vineeth changes for Transaction Type is Displaying same as Rule Name in reports. 
			String TransactionsType = "";
			for (ServiceChargeRuleTxn serviceChargeRuleTxn : serviceChargeRuleTxns) {
				if (serviceChargeRuleTxn.getSourceType().intValue() == 1) {
					TransactionsType=TransactionsType + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Wallet ";					
				}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  2) {
					TransactionsType=TransactionsType + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Card ";
				}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  3) {
					TransactionsType=TransactionsType + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Bank Account ";
				}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  4) {
					TransactionsType=TransactionsType + serviceChargeRuleTxn.getTransactionType().getDescription() +" - FI ";
				}else {
					TransactionsType=TransactionsType + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Others ";
				}
			}
			cells[3].setCellValue(TransactionsType);
			}
			// changes end.
			// Column: Applicable From
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			if (rule.getApplicableFrom()== null) {
				cells[4].setCellValue(String.valueOf(""));
			} else {
				Date date=new Date();
			//	String date1=DateUtil.formatDateToStr(rule.getApplicableFrom());
				String date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(rule.getApplicableFrom());
				cells[4].setCellValue(date1);		
				}
			
			// Column: Applicable To
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(amountCellStyle);
			if (rule.getApplicableTo()== null) {
				cells[5].setCellValue(String.valueOf(""));
			} else {
				Date date=new Date();
			//	String date1=DateUtil.formatDateToStr(rule.getApplicableTo());
				String date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(rule.getApplicableTo());
				cells[5].setCellValue(date1);		
			}
			
			
			// Column: Timezone
			cells[6] = row.createCell(6);
			cells[6].setCellStyle(centerCellStyle);
			if (rule.getTimeZone()== null) {
				cells[6].setCellValue(String.valueOf(""));
			} else {
				cells[6].setCellValue(rule.getTimeZone().getTimeZoneDesc());
			}	
			
			
			// Column: Imposed on
			cells[7] = row.createCell(7);
			cells[7].setCellStyle(centerCellStyle);
			cells[7].setCellValue(rule.getImposedOn() == 0 ? "Customer" : "Other Party");
			
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForWebUsers(List<BusinessPartnerDTO> businessPartnerUsers,List<WebUser> list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader) {
	
	List<String> header = initializeHeaderForWebUsers(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	/*HSSFCell functionalityHeadingCell0 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell0.setCellValue(messageSource.getMessage("FROM",null,resolveLocale));
	HSSFCell functionalityHeadingCell1 = functionalityHeadingRow1.createCell(3);
	if(fromDate!=""){			
		Date date=DateUtil.stringToDate(fromDate);
		functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(date));	
	}else{
		Customer customerDetail = (Customer) list.get(0);
		functionalityHeadingCell1.setCellValue(DateUtil.formatDateToStr(customerDetail.getCreatedDate()));	
	}		
	HSSFCell functionalityHeadingCell2 = functionalityHeadingRow1.createCell(4);
	functionalityHeadingCell2.setCellValue(messageSource.getMessage("TO",null,resolveLocale));
	HSSFCell functionalityHeadingCell3 = functionalityHeadingRow1.createCell(5);		
	if(toDate!=""){
		Date date=DateUtil.stringToDate(toDate);
		functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(date));
	}else{
		functionalityHeadingCell3.setCellValue(DateUtil.formatDateToStr(new Date()));	
	}*/

	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
	//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			WebUser user = (WebUser) list.get(tObject);

			// Column: UserId
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(amountCellStyle);
			if (user.getUserName() == null) {
				cells[1].setCellValue(String.valueOf(""));
			} else {
				cells[1].setCellValue(user.getUserName());
			}
			
			
			// Column: First Name
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(amountCellStyle);
			if (user.getFirstName() == null)
				cells[2].setCellValue(String.valueOf(""));
			else{
				cells[2].setCellValue(user.getFirstName());
				}
			
		// Column: Last Name
		cells[3] = row.createCell(3);
		cells[3].setCellStyle(amountCellStyle);
		if (user.getLastName() == null)
			cells[3].setCellValue(String.valueOf(""));
		else{
			cells[3].setCellValue(user.getLastName());
			}

			// Column: Mobile Number
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			if (user.getMobileNumber()== null) {
				cells[4].setCellValue(String.valueOf(""));
			} else {
				cells[4].setCellValue(user.getMobileNumber());
			}
			
			// Column: User Type
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(amountCellStyle);
			if (user.getWebUserRole() == null) {
				cells[5].setCellValue(String.valueOf(""));
			} else {
				if(user.getWebUserRole().getRoleId()==EOTConstants.ROLEID_SUPPORT_BANK){
					cells[5].setCellValue("Support Customer Care");
				}else if(user.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1){
					cells[5].setCellValue(EOTConstants.BUSINESS_PARTNER_L1);
				}else if(user.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
					cells[5].setCellValue(EOTConstants.BUSINESS_PARTNER_L2);
				}else if(user.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
					cells[5].setCellValue(EOTConstants.BULKPAYMENT_PARTNER);
				}else
					cells[5].setCellValue(user.getWebUserRole().getDescription());
			}
			
			// Business Partner
						cells[6] = row.createCell(6);
						cells[6].setCellStyle(centerCellStyle);
						for(BusinessPartnerDTO bp:businessPartnerUsers){
						if (user.getCredentialsExpired() == null) {
							cells[6].setCellValue(String.valueOf(""));
						} else {
							if(bp.getWebUserName().equals(user.getUserName())){
							cells[6].setCellValue(bp.getBusinessPartnerUserName());
						}}}
						
						
			// Column: Created Date
			cells[7] = row.createCell(7);
			cells[7].setCellStyle(centerCellStyle);
			if (user.getCreatedDate() == null) {
				cells[7].setCellValue(String.valueOf(""));
			} else {
				Date date=new Date();
			//	String date1=DateUtil.formatDateToStr(user.getCreatedDate());
				String date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(user.getCreatedDate());
				cells[7].setCellValue(date1);

			}	
			
			
			// Column: Status
			cells[8] = row.createCell(8);
			cells[8].setCellStyle(centerCellStyle);
			if (user.getCredentialsExpired() == null) {
				cells[8].setCellValue(String.valueOf(""));
			} else {
				cells[8].setCellValue(user.getCredentialsExpired().matches("N") ? "Active" : "Inactive");
			}
			
			
			
			
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForTxnRules(List<TransactionRule> list, Locale resolveLocale,
		MessageSource messageSource, String userName, String txrDetailsPageHeader) {
	
	List<String> header = initializeHeaderForTxnRule(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(txrDetailsPageHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(userName);
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			TransactionRule rule = (TransactionRule) list.get(tObject);

			// Column: Rule Level
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(centerCellStyle);
			String ruleLevel="";
			if (rule.getRuleLevel() == 1) {
				ruleLevel="Global";
			}else if (rule.getRuleLevel() == 2) {
				ruleLevel="Bank Group";
			}else if (rule.getRuleLevel() == 3) {
				ruleLevel="Customer Profile";
			}else {
				ruleLevel="Inter Bank";
			}
			cells[1].setCellValue(ruleLevel);
			
			
		// Column: Transaction Type 
		cells[2] = row.createCell(2);
		cells[2].setCellStyle(amountCellStyle);
		Set<TransactionRuleTxn> serviceChargeRuleTxns = rule.getTransactionRuleTxns();
		if (serviceChargeRuleTxns == null)
			cells[2].setCellValue(String.valueOf(""));
		else{	
				String type="";
				for (TransactionRuleTxn Txn : serviceChargeRuleTxns) {
					if (Txn.getSourceType().intValue() == 1) {
						type=Txn.getTransactionType().getDescription() +" - Wallet";
					}else if (Txn.getSourceType().intValue() ==  2) {
						type=Txn.getTransactionType().getDescription() +" - Card";
					}else if (Txn.getSourceType().intValue() ==  3) {
						type=Txn.getTransactionType().getDescription() +" - Bank Account";
					}else if (Txn.getSourceType().intValue() ==  4) {
						type=Txn.getTransactionType().getDescription() +" - FI";
					}else {
						type=Txn.getTransactionType().getDescription() +" - Others";
					}
				}
			cells[2].setCellValue(type);
			}

			// Column: Max value limit
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(rightCellStyle);
			if (rule.getMaxValueLimit() == null) {
				cells[3].setCellValue(String.valueOf(""));
			} else {
				
				cells[3].setCellValue(rule.getMaxValueLimit());
			}
			
			// Column: calculate on
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(centerCellStyle);
			cells[4].setCellValue(rule.getRuleType().intValue() == 0 ? "Debit" : "Credit");
			
			//Column: Approval Limit
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(rightCellStyle);
			if (rule.getApprovalLimit() == null) {
				cells[5].setCellValue(String.valueOf(""));
			} else {
				
				cells[5].setCellValue(rule.getApprovalLimit());
			}
			
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

private List<String> initializeHeaderForTxnRule(Locale locale,MessageSource messageSource) {
	ArrayList<String> header = new ArrayList<String>();
	
	header.add(messageSource.getMessage("SL_NO",null,locale));
	header.add(messageSource.getMessage("Rule_Level",null,locale));	
	header.add(messageSource.getMessage("LABEL_TXN_TYPE",null,locale));		
	header.add(messageSource.getMessage("Max_ValLimit",null,locale));	
	header.add(messageSource.getMessage("Calculated_On",null,locale));
	header.add(messageSource.getMessage("Approval_Limit",null,locale));
	return header;
}


@SuppressWarnings("unused")
public HSSFWorkbook createSpreadSheetFromListForBusinessPartnerCommission(List list,List<com.eot.entity.CommissionReport> commissionReports,List<BusinessPartner> businessPartner,List<Customer> customer,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader, String entityCode) {
	
	List<String> header = initializeHeaderForBusinessPartnerCommission(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME_NEW);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	

	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
	//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	
	
	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			CommissionReport report= commissionReports.get(tObject);
		/*	// Column: Entity Code
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(amountCellStyle);
			if (entityCode == null) {
				cells[1].setCellValue(String.valueOf(""));
			} else {
				cells[1].setCellValue(entityCode);
			}*/
			
			// Column: Transaction ID
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(amountCellStyle);			
			if (report.getTransactionId() == null) {
				cells[1].setCellValue(String.valueOf(""));
			} else {
				cells[1].setCellValue(report.getTransactionId());
			}
			
			
			// Column: Ref Transaction Type
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(amountCellStyle);
				if (report.getTransactionType() == null) {
					cells[2].setCellValue(String.valueOf(""));
				}else {
				cells[2].setCellValue(report.getTransactionType());
				}
			
			// Column: Ref Transaction ID
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(amountCellStyle);
			if (report.getRefTransactionId()==null)
				cells[3].setCellValue(String.valueOf(""));
			else{
				cells[3].setCellValue(report.getRefTransactionId());
				}
			
			// Column:Service Charges
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			if (report.getServiceCharge()==null)
			cells[4].setCellValue(String.valueOf(""));
			else{
				cells[4].setCellValue(report.getServiceCharge());
				}
			
			// Column:Commission Amount
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(amountCellStyle);
				if (report.getCommissionAmount()==null)
					cells[5].setCellValue(String.valueOf(""));
					else{
						cells[5].setCellValue(report.getCommissionAmount());
						}
			
				// Column:Partner Name
				cells[6] = row.createCell(6);
				cells[6].setCellStyle(amountCellStyle);
					if (report.getPartnerName()==null)
						cells[6].setCellValue(String.valueOf(""));
						else{
							cells[6].setCellValue(report.getPartnerName());
							}
					
					// Column:Partner Code 
					cells[7] = row.createCell(7);
					cells[7].setCellStyle(amountCellStyle);
						if (report.getPartnerCode()==null)
							cells[7].setCellValue(String.valueOf(""));
							else{
								cells[7].setCellValue(report.getPartnerCode());
								}		
						
						// Column: Transaction Date
						cells[8] = row.createCell(8);
						cells[8].setCellStyle(amountCellStyle);
							if (report.getTransactionDate()==null)
								cells[8].setCellValue(String.valueOf(""));
								else{
									String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(report.getTransactionDate());
									cells[8].setCellValue(createdDate);
									}				
			
			/*// Column: Service Charges
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(centerCellStyle);
			if (report.getServiceCharge() == null) {
				cells[4].setCellValue(String.valueOf(""));
			} else {
				String transactionType= "";
				if (transaction.get(j-2).getTransactionType().getTransactionType() == 120) {
					transactionType = "Commission Share";
				}
				cells[4].setCellValue(transactionType);

			}	
			
			// Column: Amount
						cells[5] = row.createCell(5);
						cells[5].setCellStyle(centerCellStyle);
						if (transaction.get(j-2).getAmount()+"" == null) {
							cells[5].setCellValue(String.valueOf(""));
						} else {
							cells[5].setCellValue(transaction.get(j-2).getAmount());
						}
						
						// Column: Transaction Date
						cells[6] = row.createCell(6);
						cells[6].setCellStyle(centerCellStyle);
						if (transaction.get(j-2).getTransactionDate() == null) {
							cells[6].setCellValue(String.valueOf(""));
						} else {
							Date date=new Date();
						//	String date1=DateUtil.formatDateToStr(transaction.get(j-2).getTransactionDate());
							String date1=transaction.get(j-2).getTransactionDate().toString();
							cells[6].setCellValue(date1);

						}*/
			
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForExternalTransactionSummaryDetails(List list, Locale resolveLocale, MessageSource messageSource,ExternalTransactionDTO externalTransactionDTO) {
	
	List<String> header = initializeHeaderForExternalTransactionSummary(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	HSSFSheet sheet = workBook.createSheet("External_Txn_Summary_Details");
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow
			.createCell(0);
	
	functionalityHeadingCell.setCellValue(messageSource.getMessage("EXTERNAL_SUMMARY_TXN_DETAILS",null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);			

	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(5);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(6);		

	functionalityHeadingCell7.setCellValue(externalTransactionDTO.getUserName());

	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(7);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(8);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));	

	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//
		Double totalServiceChargeFee = 0D;
		Double totalBankServiceChargeFee = 0D;
		Double totalGIMServiceChargeFee = 0D;
		Double totalOperatorShare = 0D;
		Double totalTax = 0D;
		Double totalAmt = 0D;
		DecimalFormat dec = new DecimalFormat("0.00");

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			
			int x=0;
			// Column: Sl.no.
			cells[x] = row.createCell(x);
			cells[x].setCellValue(j++);
			cells[x].setCellStyle(centerCellStyle);
			Object[] obj=(Object[])list.get(tObject);
			
            ++x;
			// Column: Operator
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (obj[0] == null) {
				cells[x].setCellValue(String.valueOf(""));
			} else {
				cells[x].setCellValue(obj[0].toString());
			}
			
			 ++x;

			// Column: Amount
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (obj[1] == null) {
				cells[x].setCellValue(String.valueOf(""));
			} else {
				Double amount= (Double) obj[1];
				cells[x].setCellValue((dec.format(amount)));
				Double amt = (Double) obj[1];
				totalAmt = (double) (totalAmt+amt);
			}
			
			++x;

			// Column: Transaction Type
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (obj[3].equals(EOTConstants.TXN_ID_SMSCASH_OTHERS)) {
				cells[x].setCellValue(messageSource.getMessage("SMS_CASH_OTHERS",null,resolveLocale));
			} else {
				cells[x].setCellValue(messageSource.getMessage("TRF_DIRECT",null,resolveLocale));
			}
			

			++x;
			// Column: Fee
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(rightCellStyle);
			if (obj[6]  == null) {
				cells[x].setCellValue((dec.format(0)));
			} else {					
				Double amount1= (Double) obj[6];
				cells[x].setCellValue((dec.format(amount1)));
				Double serChargeFee = (Double) obj[6];
				totalServiceChargeFee = (double) (totalServiceChargeFee+serChargeFee);

			}



			++x;
			// Column: Tax Fee
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(rightCellStyle);
			if (obj[7]  == null) {
				cells[x].setCellValue(String.valueOf(""));
			} else {
				Double amount1= (Double) obj[7];					
				cells[x].setCellValue((dec.format(amount1)));					
				Double GIMShareFee = (Double) obj[7];
				totalTax = (double) (totalTax+GIMShareFee);

			}

		
			++x;
			// Column: Bank Share Fee
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(rightCellStyle);
			if (obj[8]  == null) {
				cells[x].setCellValue(String.valueOf(""));
			} else {
				Double bankShare= (Double) obj[8];
				Double gimShare= (Double) obj[9];
				Double totalBankShare=bankShare-gimShare;
				cells[x].setCellValue((dec.format(totalBankShare)));
				totalBankServiceChargeFee = (double) (totalBankServiceChargeFee+totalBankShare);
			}

			++x;
			// Column: GIM Share Fee
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(rightCellStyle);
			if (obj[9]  == null) {
				cells[x].setCellValue(String.valueOf(""));
			} else {
				Double gimShare= (Double) obj[9];
				cells[x].setCellValue((dec.format(gimShare)));
				Double GIMShareFee = (Double) obj[9];
				totalGIMServiceChargeFee = (double) (totalGIMServiceChargeFee+GIMShareFee);

			}
			
			if(j==list.size()+1){

				HSSFRow row1 = sheet.createRow(list.size() + (headerRow.getRowNum() + 2));	
				cells[1] = row1.createCell(1);			
				cells[1].setCellStyle(getHeaderStyle(workBook));
				cells[1].setCellValue(messageSource.getMessage("Total_Amount",null,resolveLocale));	
				
				cells[2] = row1.createCell(2);			
				cells[2].setCellStyle(rightCellStyle);
				cells[2].setCellValue((dec.format(totalAmt)));	

				cells[4] = row1.createCell(4);	
				cells[4].setCellStyle(rightCellStyle);
				cells[4].setCellValue((dec.format(totalServiceChargeFee)));

				cells[5] = row1.createCell(5);	
				cells[5].setCellStyle(rightCellStyle);
				cells[5].setCellValue((dec.format(totalTax)));

				cells[6] = row1.createCell(6);	
				cells[6].setCellStyle(rightCellStyle);
				cells[6].setCellValue((dec.format(totalBankServiceChargeFee)));

				cells[7] = row1.createCell(7);	
				cells[7].setCellStyle(rightCellStyle);
				cells[7].setCellValue((dec.format(totalGIMServiceChargeFee)));


			}


		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;
}

public HSSFWorkbook createSpreadSheetForBulkPayTxn(List list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader) {
	
	List<String> header = initializeHeaderForBulkPay(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			
			// Column: Transaction Id
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(centerCellStyle);
			cells[1].setCellValue(m.get("TransactionID").toString());
			
			// Column: Customer name
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(amountCellStyle);
			cells[2].setCellValue(m.get("Name").toString());
			
			// Column: Mobile Number
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(amountCellStyle);
			cells[3].setCellValue(m.get("MobileNumber").toString());

			
			// Column: Agent Code
			/*cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			cells[4].setCellValue(m.get("AgentCode") == null ? " " : m.get("AgentCode").toString());*/
			
			/*// Column: Branch
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			cells[4].setCellValue(m.get("Location").toString());*/
			
			/// Column: TXN Date
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("TransactionDate"));
		    cells[4].setCellValue(createdDate);
			
			
			/// Column: Txn value
			DecimalFormat format = new DecimalFormat("#0.00");
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(rightCellStyle);
			String amount = format.format(m.get("Amount"));
			amount = amount.equals(".00") == true ? "0.00" : amount;
			cells[5].setCellValue(amount);	
			
			
			/// Column: Service Charges
			cells[6] = row.createCell(6);
			cells[6].setCellStyle(rightCellStyle);
			
			if (m.get("SC") != null) {
				String serviceCharge = format.format(m.get("SC"));
				serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;							
				cells[6].setCellValue(serviceCharge);
			}
			else
				cells[6].setCellValue("");	
			/// Column: TXN Type
			/*cells[6] = row.createCell(6);
			cells[6].setCellStyle(amountCellStyle);
			try {
				cells[6].setCellValue(transactionService.getTransactionType((int)m.get("TransactionType")).getDescription());
			} catch (EOTException e) {
				e.printStackTrace();
			}*/
			
			
		    
			/// Column: Status
			cells[7] = row.createCell(7);
			cells[7].setCellStyle(amountCellStyle);
			if (m.get("Status").equals(2000)) 
				cells[7].setCellValue("Success");
			else 
				cells[7].setCellValue("Failed");
			
			/// Column: Description
			/*cells[9] = row.createCell(9);
			cells[9].setCellStyle(amountCellStyle);
			if (m.get("status").equals(2000)) 
				cells[9].setCellValue("Success");
			else {
				String description = "";
				if(m.get("status").equals(2001))
					description = EOTConstants.FATAL_ERROR;
				else if(m.get("status").equals(2002))
					description = EOTConstants.MISSING_PARAMETERS_ERROR;
				else if(m.get("status").equals(2003))
					description = EOTConstants.INVALID_PARAMETERS_ERROR;
				else if(m.get("status").equals(2004))
					description = EOTConstants.OPERATION_NOT_SUPPORTED_ERROR;
				else if(m.get("status").equals(2005))
					description = EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR;
				else if(m.get("status").equals(2006))
					description = EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR;
				else if(m.get("status").equals(2020))
					description = EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR;
				else if(m.get("status").equals(2021))
					description = EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR;
				else if(m.get("status").equals(2022))
					description = EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR;
			else if(m.get("status").equals(2023))
				description = EOTConstants.NO_VOUCHER_AVAILABLE_ERROR;
			else if(m.get("status").equals(2024))
				description = EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR;
			else if(m.get("status").equals(2025))
				description = EOTConstants.TXN_RULE_NOT_DEFINED_ERROR;
			else if(m.get("status").equals(2026))
				description = EOTConstants.TXN_LIMIT_EXCEEDED_ERROR;
			else if(m.get("status").equals(2027))
				description = EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR;
			else if(m.get("status").equals(2028))
				description = EOTConstants.TXN_NUM_EXCEEDED_ERROR;
			else if(m.get("status").equals(2029))
				description = EOTConstants.INVALID_TXN_RULE_ERROR;
			else if(m.get("status").equals(2030))
				description = EOTConstants.NO_BALANCE_IN_WALLET_ERROR;
			else if(m.get("status").equals(2031))
				description = EOTConstants.TXN_DECLINED_FROM_HPS_ERROR;
			else if(m.get("status").equals(2032))
				description = EOTConstants.INVALID_CH_POOL_ERROR;
			else if(m.get("status").equals(2033))
				description = EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR;
			else if(m.get("status").equals(2034))
				description = EOTConstants.SETTLED_TRANSACTION_ERROR;
			else if(m.get("status").equals(2035))
				description = EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR;
			else if(m.get("status").equals(2036))
				description = EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR;
			else if(m.get("status").equals(2037))
				description = EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR;
			else if(m.get("status").equals(2038))
				description = EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR;
			else if(m.get("status").equals(2039))
				description = EOTConstants.INVALID_SIGNATURE_SIZE_ERROR;
			else if(m.get("status").equals(2040))
				description = EOTConstants.INVALID_IDPROOF_SIZE_ERROR;
			else if(m.get("status").equals(2041))
				description = EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR;
			cells[9].setCellValue(description);	
			}				*/
		    
			
			/*/// Column: Request Channel
			cells[11] = row.createCell(11);
			cells[11].setCellStyle(centerCellStyle);
			
			cells[11].setCellValue(m.get("requestChannel") == null ? " " : m.get("requestChannel").toString());*/
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetForCustomerReg(List list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader) {
	
	List<String> header = initializeHeaderForCusRegReport(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			
			// Column: Transaction Id
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(centerCellStyle);
			cells[1].setCellValue(m.get("TransactionID").toString());
			
			/// Column: Commisssion Amount
			DecimalFormat format = new DecimalFormat("#0.00");
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(rightCellStyle);
		//	String amount = format.format(m.get("amount"));
			String amount = null !=m.get("amount")? format.format(m.get("amount")):"0.00";
			amount = amount.equals(".00") == true ? "0.00" : amount;
			cells[2].setCellValue(amount);	
			
			// Column: Customer name
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(amountCellStyle);
			cells[3].setCellValue(null !=m.get("BusinessName")?m.get("BusinessName").toString():"");
			
			// Column: Customer name
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			cells[4].setCellValue(m.get("FirstName")+ " " + m.get("LastName"));
			
			// Column: Customer Mobile Number
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(amountCellStyle);
			cells[5].setCellValue(m.get("MobileNumber").toString());
			
			/*// Column: Customer Mobile Number
			cells[6] = row.createCell(6);
			cells[6].setCellStyle(amountCellStyle);	
			cells[6].setCellValue(m.get("MobileNumber").toString());*/
			
			// Column: Agent Name
			cells[6] = row.createCell(6);
			cells[6].setCellStyle(amountCellStyle);
			cells[6].setCellValue(m.get("AgentName").toString());

			// Column: Agent Code
			cells[7] = row.createCell(7);
			cells[7].setCellStyle(amountCellStyle);
			cells[7].setCellValue(m.get("Agent_Code") == null ? " " : m.get("Agent_Code").toString());
			
			
			// Column: Agent Mobile Number
			cells[8] = row.createCell(8);
			cells[8].setCellStyle(amountCellStyle);
			cells[8].setCellValue(m.get("AgentMobileNumber") == null ? " " : m.get("AgentMobileNumber").toString());
			
			// Column: Super Agent Name
			cells[9] = row.createCell(9);
			cells[9].setCellStyle(amountCellStyle);
			cells[9].setCellValue(null !=m.get("SuperAgentName")?m.get("SuperAgentName").toString():"");
			
			// Column: Super Agent Code
			cells[10] = row.createCell(10);
			cells[10].setCellStyle(amountCellStyle);
			cells[10].setCellValue(null !=m.get("SuperAgentCode")?m.get("SuperAgentCode").toString():"");
		//	cells[10].setCellValue(m.get("SuperAgentCode").toString());
			
			/// Column: TXN Type
			cells[11] = row.createCell(11);
			cells[11].setCellStyle(amountCellStyle);
			try {
				cells[11].setCellValue(transactionService.getTransactionType((int)m.get("TransactionType")).getDescription());
			} catch (EOTException e) {
				e.printStackTrace();
			}
			
			/// Column: TXN Date
			cells[12] = row.createCell(12);
			cells[12].setCellStyle(amountCellStyle);
			String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("TransactionDate"));
			cells[12].setCellValue(createdDate);
		    
		
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}
public HSSFWorkbook createSpreadSheetFromListForMerchantCommisionReport(List list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader) {
	
	List<String> header = initializeHeaderForMerchantCommisionReport(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			
			// Column: Transaction Id
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(centerCellStyle);
			cells[1].setCellValue(m.get("transactionid").toString());
			
			
			// Column: Merchant Code
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(amountCellStyle);
			cells[2].setCellValue(m.get("MerchantCode")+"");
			
			// Column: BusinessName Code
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(amountCellStyle);
			cells[3].setCellValue(null !=m.get("BusinessName")?m.get("BusinessName").toString():"");
			
			// Column: Merchant Name
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			cells[4].setCellValue(m.get("MerchantName")+"");
			
			// Column: Merchant_Mobile_Number
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(amountCellStyle);
			cells[5].setCellValue(m.get("MerchantMobile")+"");
			
			// Column: Customer_Name
			cells[6] = row.createCell(6);
			cells[6].setCellStyle(amountCellStyle);
			cells[6].setCellValue(m.get("customerName")+"");
			
			// Column: Customer_Mobile_Number
			cells[7] = row.createCell(7);
			cells[7].setCellStyle(amountCellStyle);
			cells[7].setCellValue(m.get("customerMobile")+"");

			/// Column: TXN_Date_Time
			cells[8] = row.createCell(8);
			cells[8].setCellStyle(amountCellStyle);
		//	String createdDate = new SimpleDateFormat("dd-MM-yyyy").format(m.get("TransactionDate"));
			String createdDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("TransactionDate"));
			cells[8].setCellValue(createdDate);
			
			/// Column: TXN Type
			cells[9] = row.createCell(9);
			cells[9].setCellStyle(amountCellStyle);
			cells[9].setCellValue(m.get("Description")+"");
			
			/// Column: Status
			cells[10] = row.createCell(10);
			cells[10].setCellStyle(amountCellStyle);
			if (m.get("status").equals(2000)) 
				cells[10].setCellValue("Success");
			else 
				cells[10].setCellValue("Failed");
			
			/// Column: Description
			cells[11] = row.createCell(11);
			cells[11].setCellStyle(amountCellStyle);
			if (m.get("status").equals(2000)) 
				cells[11].setCellValue("Success");
			else {
				String description = "";
				if(m.get("status").equals(2001))
					description = EOTConstants.FATAL_ERROR;
				else if(m.get("status").equals(2002))
					description = EOTConstants.MISSING_PARAMETERS_ERROR;
				else if(m.get("status").equals(2003))
					description = EOTConstants.INVALID_PARAMETERS_ERROR;
				else if(m.get("status").equals(2004))
					description = EOTConstants.OPERATION_NOT_SUPPORTED_ERROR;
				else if(m.get("status").equals(2005))
					description = EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR;
				else if(m.get("status").equals(2006))
					description = EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR;
				else if(m.get("status").equals(2020))
					description = EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR;
				else if(m.get("status").equals(2021))
					description = EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR;
				else if(m.get("status").equals(2022))
					description = EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR;
			else if(m.get("status").equals(2023))
				description = EOTConstants.NO_VOUCHER_AVAILABLE_ERROR;
			else if(m.get("status").equals(2024))
				description = EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR;
			else if(m.get("status").equals(2025))
				description = EOTConstants.TXN_RULE_NOT_DEFINED_ERROR;
			else if(m.get("status").equals(2026))
				description = EOTConstants.TXN_LIMIT_EXCEEDED_ERROR;
			else if(m.get("status").equals(2027))
				description = EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR;
			else if(m.get("status").equals(2028))
				description = EOTConstants.TXN_NUM_EXCEEDED_ERROR;
			else if(m.get("status").equals(2029))
				description = EOTConstants.INVALID_TXN_RULE_ERROR;
			else if(m.get("status").equals(2030))
				description = EOTConstants.NO_BALANCE_IN_WALLET_ERROR;
			else if(m.get("status").equals(2031))
				description = EOTConstants.TXN_DECLINED_FROM_HPS_ERROR;
			else if(m.get("status").equals(2032))
				description = EOTConstants.INVALID_CH_POOL_ERROR;
			else if(m.get("status").equals(2033))
				description = EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR;
			else if(m.get("status").equals(2034))
				description = EOTConstants.SETTLED_TRANSACTION_ERROR;
			else if(m.get("status").equals(2035))
				description = EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR;
			else if(m.get("status").equals(2036))
				description = EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR;
			else if(m.get("status").equals(2037))
				description = EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR;
			else if(m.get("status").equals(2038))
				description = EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR;
			else if(m.get("status").equals(2039))
				description = EOTConstants.INVALID_SIGNATURE_SIZE_ERROR;
			else if(m.get("status").equals(2040))
				description = EOTConstants.INVALID_IDPROOF_SIZE_ERROR;
			else if(m.get("status").equals(2041))
				description = EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR;
			cells[11].setCellValue(description);	
			}	

			/// Column: Amount
			DecimalFormat format = new DecimalFormat("#0.00");
			cells[12] = row.createCell(12);
			cells[12].setCellStyle(rightCellStyle);
			String amount = format.format(m.get("Amount"));
			amount = amount.equals(".00") == true ? "0.00" : amount;
			cells[12].setCellValue(amount);	

		  /// Column: Service Charges
			cells[13] = row.createCell(13);
			cells[13].setCellStyle(rightCellStyle);
			String serviceCharge ="";
			if (m.get("ServiceCharges") != null) {
				serviceCharge = format.format(m.get("ServiceCharges"));
				serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;							
				cells[13].setCellValue(serviceCharge);
			}
			else
				cells[13].setCellValue("");	
			
			
			/// Column: settlement Amount
			
			cells[14] = row.createCell(14);
			cells[14].setCellStyle(rightCellStyle);

				if(serviceCharge != "") {
					Double settlementAmount = Double.parseDouble(amount)-Double.parseDouble(serviceCharge);
					cells[14].setCellValue(settlementAmount);
				}else
					cells[14].setCellValue(amount);
			
			/// Column: Request Channel
			cells[15] = row.createCell(15);
			cells[15].setCellStyle(centerCellStyle);
			       
			cells[15].setCellValue(m.get("requestChannel") == null ? " " : m.get("requestChannel").toString());
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForBulkPaymentPartner(List<BusinessPartner> list,Locale resolveLocale,MessageSource messageSource, WebUser webUser, String reportHeader, String entityCode, Integer role) {
	
	List<String> header = initializeHeaderForBulkPaymentPartner(resolveLocale,messageSource,role);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	

	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(webUser.getUserName());
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.dateAndTime(new Date()));
	
	//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
	//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
			BusinessPartner user = (BusinessPartner) list.get(tObject);
			
			// Column: Super Agent Code
			cells[1] = row.createCell(1);
			cells[1].setCellStyle(amountCellStyle);
			if (user.getCode() == null) {
				cells[1].setCellValue(String.valueOf(""));
			} else {
				cells[1].setCellValue(user.getCode());
			}
			
			// Column: Business partner
			cells[2] = row.createCell(2);
			cells[2].setCellStyle(amountCellStyle);
			if (user.getName() == null) {
				cells[2].setCellValue(String.valueOf(""));
			} else {
				cells[2].setCellValue(user.getName());
			}
			
			
			// Column: Business partner type
			cells[3] = row.createCell(3);
			cells[3].setCellStyle(amountCellStyle);
			Integer partnerType = user.getPartnerType();
			String type="";
			if (partnerType == null)
				cells[3].setCellValue(String.valueOf(""));
			else{
				if (partnerType.intValue() == 4) {
					type="Bulk Payment Partner";
				}else if (partnerType.intValue() == 2) {
					type="Super Agent ";
				}else if (partnerType.intValue() == 2) {
					type="Super Agent L2";
				}
				cells[3].setCellValue(type);
				}
			
			// Column: Contact Number
			cells[4] = row.createCell(4);
			cells[4].setCellStyle(amountCellStyle);
			if (user.getContactNumber() == null)
				cells[4].setCellValue(String.valueOf(""));
			else{
				cells[4].setCellValue(user.getContactPersonPhone());
				}

			
			// Column: Created Date
			cells[5] = row.createCell(5);
			cells[5].setCellStyle(centerCellStyle);
			if (user.getCreatedDate() == null) {
				cells[5].setCellValue(String.valueOf(""));
			} else {
			//	String date1=DateUtil.formatDateAndTime(user.getCreatedDate());
				String date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(user.getCreatedDate());
				cells[5].setCellValue(date1);

			}	
			
		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForBankFloatDepositReportData(List list, Locale resolveLocale,
		MessageSource messageSource, BankFloatDepositDTO bankFloatDepositDTO, String reportHeader, String userName) {
	
	List<String> header = initializeHeaderForBankFloatDepositReportData(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(userName);
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.formatDateToStr1(new Date()));
	
	//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
	//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		int x;
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
	//		Customer customerDetail = (Customer) list.get(tObject);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			x=1;

			// Column: Name
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("NAME") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("NAME").toString());
			}
			// Column: Code
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("CODE") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("CODE").toString());
			}

			// Column: Type
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("TYPE") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(String.valueOf(m.get("TYPE")));
			}
			
			// Column: Account Number
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("AccountNumber") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("AccountNumber").toString());
			}
	
			
			// Column: TransactionId
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(centerCellStyle);
			if (m.get("TransactionID") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
					cells[x++].setCellValue(m.get("TransactionID").toString());
			}	
			
			
			// Column: Amount
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("Amount") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(String.valueOf(m.get("Amount")));
			}
			
			
			// Column: Transaction Date
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(centerCellStyle);
			if (m.get("TransactionDate") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
//				String date=DateUtil.formatDateToStr(m.get("CreatedDate").getCreatedDate());	
				String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("TransactionDate"));
				cells[x++].setCellValue(date);
			}					

			/// Column: Description
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(centerCellStyle);
			if (m.get("Status") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else if (m.get("Status").equals(2000)) {
				cells[x++].setCellValue("Success");
			}else {
//				String description = "";
//				if(m.get("Status").equals(2001))
//					description = EOTConstants.FATAL_ERROR;
//				else if(m.get("Status").equals(2002))
//					description = EOTConstants.MISSING_PARAMETERS_ERROR;
//				else if(m.get("Status").equals(2003))
//					description = EOTConstants.INVALID_PARAMETERS_ERROR;
//				else if(m.get("Status").equals(2004))
//					description = EOTConstants.OPERATION_NOT_SUPPORTED_ERROR;
//				else if(m.get("Status").equals(2005))
//					description = EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR;
//				else if(m.get("Status").equals(2006))
//					description = EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR;
//				else if(m.get("Status").equals(2020))
//					description = EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR;
//				else if(m.get("Status").equals(2021))
//					description = EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR;
//				else if(m.get("Status").equals(2022))
//					description = EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR;
//			else if(m.get("Status").equals(2023))
//				description = EOTConstants.NO_VOUCHER_AVAILABLE_ERROR;
//			else if(m.get("Status").equals(2024))
//				description = EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR;
//			else if(m.get("Status").equals(2025))
//				description = EOTConstants.TXN_RULE_NOT_DEFINED_ERROR;
//			else if(m.get("Status").equals(2026))
//				description = EOTConstants.TXN_LIMIT_EXCEEDED_ERROR;
//			else if(m.get("Status").equals(2027))
//				description = EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR;
//			else if(m.get("Status").equals(2028))
//				description = EOTConstants.TXN_NUM_EXCEEDED_ERROR;
//			else if(m.get("Status").equals(2029))
//				description = EOTConstants.INVALID_TXN_RULE_ERROR;
//			else if(m.get("Status").equals(2030))
//				description = EOTConstants.NO_BALANCE_IN_WALLET_ERROR;
//			else if(m.get("Status").equals(2031))
//				description = EOTConstants.TXN_DECLINED_FROM_HPS_ERROR;
//			else if(m.get("Status").equals(2032))
//				description = EOTConstants.INVALID_CH_POOL_ERROR;
//			else if(m.get("Status").equals(2033))
//				description = EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR;
//			else if(m.get("Status").equals(2034))
//				description = EOTConstants.SETTLED_TRANSACTION_ERROR;
//			else if(m.get("Status").equals(2035))
//				description = EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR;
//			else if(m.get("Status").equals(2036))
//				description = EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR;
//			else if(m.get("Status").equals(2037))
//				description = EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR;
//			else if(m.get("Status").equals(2038))
//				description = EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR;
//			else if(m.get("Status").equals(2039))
//				description = EOTConstants.INVALID_SIGNATURE_SIZE_ERROR;
//			else if(m.get("Status").equals(2040))
//				description = EOTConstants.INVALID_IDPROOF_SIZE_ERROR;
//			else if(m.get("Status").equals(2041))
//				description = EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR;
			cells[x++].setCellValue("Failed");	
			}	


		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;

}

public HSSFWorkbook createSpreadSheetFromListForNonRegUssdCustomerReportData(List list, Locale resolveLocale,
		MessageSource messageSource, NonRegUssdCustomerDTO nonRegUssdCustomerDTO, String reportHeader, String userName) {
	
	List<String> header = initializeHeaderForNonRegUssdCustomerReportData(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(userName);
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.formatDateToStr1(new Date()));
	
	//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
	//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		int x;
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
	//		Customer customerDetail = (Customer) list.get(tObject);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			x=1;
			
			// Column: Mobile Number
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("MSISDN") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("MSISDN").toString());
			}
			
			// Column: Date
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(centerCellStyle);
			if (m.get("createdDate") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {	
				String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(m.get("createdDate"));
				cells[x++].setCellValue(date);
			}					

		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;
}

public HSSFWorkbook createSpreadSheetFromListForTransactionVolumeReportData(List list, Locale resolveLocale,
		MessageSource messageSource, TransactionVolumeDTO transactionVolumeDTO, String reportHeader, String userName) {
	
	List<String> header = initializeHeaderForTransactionVolumeReportData(resolveLocale,messageSource);
	HSSFWorkbook workBook = new HSSFWorkbook();
	// create a new worksheet
	
	HSSFSheet sheet = workBook.createSheet(EOTConstants.WORKBOOK_SHEET_NAME);
	// insertBrandPicture(workBook);
	setTitle(workBook,header);

	sheet.createRow(2);
	HSSFRow functionalityHeadingRow = sheet.createRow(3);
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	HSSFCell functionalityHeadingCell = functionalityHeadingRow.createCell(0);

	functionalityHeadingCell.setCellValue(messageSource.getMessage(reportHeader,null,resolveLocale));
	functionalityHeadingCell.setCellStyle(getTitleStyle(workBook));	

	HSSFRow functionalityHeadingRow1 = sheet.createRow(5);				
	sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, header.size() - 1));
	
	HSSFCell functionalityHeadingCell6 = functionalityHeadingRow1.createCell(0);
	functionalityHeadingCell6.setCellValue(messageSource.getMessage("PRINTED_BY",null,resolveLocale));
	HSSFCell functionalityHeadingCell7 = functionalityHeadingRow1.createCell(1);		
	functionalityHeadingCell7.setCellValue(userName);
	
	HSSFCell functionalityHeadingCell4 = functionalityHeadingRow1.createCell(2);
	functionalityHeadingCell4.setCellValue(messageSource.getMessage("PRINTED_DATE",null,resolveLocale));
	HSSFCell functionalityHeadingCell5 = functionalityHeadingRow1.createCell(3);		

	functionalityHeadingCell5.setCellValue(DateUtil.formatDateToStr1(new Date()));
	
	//functionalityHeadingCell0.setCellStyle(getHeaderStyle(workBook));
	//functionalityHeadingCell2.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell4.setCellStyle(getHeaderStyle(workBook));
	functionalityHeadingCell6.setCellStyle(getHeaderStyle(workBook));

	sheet.createRow(4);

	// create an header row
	HSSFRow headerRow = sheet.createRow(7);
	// create a style for the Column Headers
	HSSFCellStyle labelHeaderStyle = getHeaderStyle(workBook);

	HSSFCell cells[] = new HSSFCell[header.size() + 1];
	HSSFCellStyle dataCellStyle = getColumnDataStyle(workBook);

	// create a style for the Column data except Amount column
	HSSFCellStyle amountCellStyle = workBook.createCellStyle();
	amountCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);	

	HSSFCellStyle rightCellStyle = workBook.createCellStyle();
	rightCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);	

	HSSFCellStyle centerCellStyle = workBook.createCellStyle();
	centerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);	

	for (Iterator<String> it = header.iterator(); it.hasNext();) {
		// *************** Start of Column Headers ******************//
		for (int i = 0; i < header.size(); i++) {
			cells[i] = headerRow.createCell(i);
			cells[i].setCellValue(it.next());
			cells[i].setCellStyle(labelHeaderStyle);
		}
		// *************** End of Column Headers ******************//

		// *************** Column data ******************//
		int x;
		for (int k =8, j = 1, tObject = 0; k < (list.size() + (headerRow
				.getRowNum() + 1)) && tObject < list.size(); k++, tObject++) {
			HSSFRow row = sheet.createRow(k);
			// Column: Sl.no.
			cells[0] = row.createCell(0);
			cells[0].setCellValue(j++);
			cells[0].setCellStyle(centerCellStyle);
	//		Customer customerDetail = (Customer) list.get(tObject);
			Map<String, Object> m = (Map<String, Object>) list.get(tObject);
			x=1;
			
			// Column: Name
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("Name") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("Name").toString());
			}
			
			// Column: Code
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("Code") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("Code").toString());
			}			
			
			// Column:Agent Name
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("AgentName") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("AgentName").toString());
			}
			
			// Column: Mobile Number
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("MobileNumber") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("MobileNumber").toString());
			}
			
			// Column: Agent Code
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("AgentCode") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("AgentCode").toString());
			}			
			
			// Column:Total Deposit
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("TotalDeposit") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("TotalDeposit").toString());
			}
			
			// Column:Deposit Volume
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("DepositVolume") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("DepositVolume").toString());
			}
			
			// Column:Total WithDrawal
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("TotalWithDrawal") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("TotalWithDrawal").toString());
			}
			
			// Column:Withdrawal Volume
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("WithdrawalVolume") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("WithdrawalVolume").toString());
			}
			
			// Column:Balance
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("CurrentBalance") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(m.get("CurrentBalance").toString());
			}
			
			// Column:Commission Balance
			cells[x] = row.createCell(x);
			cells[x].setCellStyle(amountCellStyle);
			if (m.get("CommissionBalance") == null) {
				cells[x++].setCellValue(String.valueOf(""));
			} else {
				cells[x++].setCellValue(new DecimalFormat("#0.00").format(((Double)m.get("CommissionBalance")).doubleValue()));
			}

		}// *************** End of Column data ******************//
	}

	for (int i = 0; i < header.size(); i++) {
		sheet.autoSizeColumn(i);
	}
	return workBook;
}

}