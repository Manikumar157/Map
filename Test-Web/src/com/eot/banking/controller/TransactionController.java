package com.eot.banking.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.internal.matchers.SubstringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dto.AccountDetailsDTO;
import com.eot.banking.dto.BankFloatDepositDTO;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.DashboardDTO;
import com.eot.banking.dto.ExternalTransactionDTO;
import com.eot.banking.dto.NonRegUssdCustomerDTO;
import com.eot.banking.dto.RequestReinitDTO;
import com.eot.banking.dto.ReversalDTO;
import com.eot.banking.dto.SMSCampaignDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.dto.TransactionReceiptDTO;
import com.eot.banking.dto.TransactionVolumeDTO;
import com.eot.banking.dto.TxnStatementDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.BusinessPartnerService;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.DashboardService;
import com.eot.banking.service.TransactionRulesService;
import com.eot.banking.service.TransactionService;
import com.eot.banking.service.WebUserService;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.FileUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.dtos.basic.Transaction;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.CommissionReport;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.TransactionType;
import com.eot.entity.TransactionTypesDesc;
import com.eot.entity.TransactionTypesDescPK;
import com.eot.entity.WebUser;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Controller
public class TransactionController extends PageViewController {

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private TransactionRulesService transactionRulesService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AppConfigurations appConfigurations ;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private ExcelWrapper wrapper;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DashboardService dashBoardService;
	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;
	/** The business partner service. */
	@Autowired
	private BusinessPartnerService businessPartnerService;

	@RequestMapping("/selectTransactionForm.htm")
	public String selectTransactionForm(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("txnList",transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"TransactionServices");
		}
		return "selectTransactionForm";
	}

	@RequestMapping("/getAccountDetails.htm")
	public String getAccountDetails(HttpServletRequest request,ModelMap model,HttpSession session,HttpServletResponse response){

		try {

			System.out.println(Integer.parseInt(request.getParameter("transactionType")));
			AccountDetailsDTO accountDetailsDTO = transactionService.getAccountDetails(request.getParameter("mobileNumber"),null,false);
			accountDetailsDTO.setTransactionType(Integer.parseInt(request.getParameter("transactionType")));

			model.put("accountDetailsDTO", accountDetailsDTO );
			session.setAttribute("accountDetailsDTO", accountDetailsDTO);

			return "transactionDetailsForm";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "selectTransactionForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "selectTransactionForm";
		}finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"AccountDetails");
		}

	}

	@RequestMapping("/processNextTransaction.htm")
	public String processNextTransaction(HttpServletRequest request,ModelMap model,HttpSession session,HttpServletResponse response){

		try {
			System.out.println(request.getParameter("mobileNumber"));
			System.out.println(Integer.parseInt(request.getParameter("transactionType")));
			AccountDetailsDTO accountDetailsDTO = transactionService.getAccountDetails(request.getParameter("mobileNumber"),null,false);
			accountDetailsDTO.setTransactionType(Integer.parseInt(request.getParameter("transactionType")));
			accountDetailsDTO.setAmount(Long.valueOf(request.getParameter("amount")));
			model.put("accountDetailsDTO", accountDetailsDTO );
			session.setAttribute("accountDetailsDTO", accountDetailsDTO);

			return "confirmTransactionDetailsForm";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "selectTransactionForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "selectTransactionForm";
		}finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"AccountDetails");
		}

	}

	@RequestMapping("/processTransaction.htm")
	public String processTransaction(TransactionParamDTO transactionParamDTO ,Map<String, Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){

		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			
			TransactionReceiptDTO receipt = transactionService.processTransaction(transactionParamDTO,auth.getName(),localeResolver.resolveLocale(request),messageSource);
			receipt.setCustomerId(transactionParamDTO.getCustomerId());
			if(transactionParamDTO.getTransactionType()!=EOTConstants.TXN_ID_CASH_OUT) {
				receipt.setCustomerName(transactionParamDTO.getName());
				receipt.setAccountAlias(transactionParamDTO.getAccountAlias());
			}			
			receipt.setMobileNumber(transactionParamDTO.getMobileNumber());
			receipt.setTransactionType(transactionParamDTO.getTransactionType());
			receipt.setBusinessPartnerCode(transactionParamDTO.getBusinessPartnerCode());
			//receipt.setAmount(transactionParamDTO.getAmount());

			if(transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_TXNSTATEMENT){ 
				session.setAttribute("txnList", receipt.getTxnList());
				session.setAttribute("customerDetails", transactionParamDTO);
			}
			session.setAttribute("transactionReceipt", receipt);
			//model.put("transactionReceipt", receipt);
			if((null == transactionParamDTO.getBusinessPartnerCode() || "".equals(transactionParamDTO.getBusinessPartnerCode()))
					&& transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_CASH_IN && transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_CASH_OUT
							&& transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_TRANSFER_EMONEY
							&& transactionParamDTO.getTransactionType() != EOTConstants.TXN_ID_ACCOUNT_TRANSFER) 
				return "transactionPrintReceipt";
			else if(transactionParamDTO.getTransactionType()==EOTConstants.TXN_ID_ACCOUNT_TRANSFER) {
				model.put("accountsList", transactionService.getAccountsForAccountToAccount());
				return "accountTransferReceipt";
			}else if(transactionParamDTO.getTransactionType()==EOTConstants.TXN_ID_CASH_OUT) {
				  Page page=customerService.getAgentCashOutData(new TransactionParamDTO(), 1);				  
				  model.put("page",page );				  
				  model.put("transactionParamDTO",transactionParamDTO ); return
				  "agentCashOutReceipt";
			}else
				return "transactionBusinessPartnerPrintReceipt";			

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			
			if(transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_DEPOSIT || transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_WITHDRAWAL
				||	transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_CASH_IN){
				AccountDetailsDTO accountDetailsDTO = (AccountDetailsDTO)session.getAttribute("accountDetailsDTO");
				accountDetailsDTO.setAmount(transactionParamDTO.getAmount());
				model.put("accountDetailsDTO", accountDetailsDTO);
				return "confirmTransactionDetailsForm";
			}else if(transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_CASH_OUT){
				Page page=customerService.getAgentCashOutData(transactionParamDTO, 1);
				model.put("page",page );
				model.put("transactionParamDTO",transactionParamDTO );
				return "confirmAgentCashOut";
			}else{
				AccountDetailsDTO accountDetailsDTO = (AccountDetailsDTO)session.getAttribute("accountDetailsDTO");
				accountDetailsDTO.setAmount(transactionParamDTO.getAmount());
				model.put("accountDetailsDTO", accountDetailsDTO);
				return "transactionDetailsForm";
			}  
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("accountDetailsDTO", session.getAttribute("accountDetailsDTO"));
			if(transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_DEPOSIT || transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_WITHDRAWAL
				||	transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_CASH_IN){
				return "confirmTransactionDetailsForm";
			}else if(transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_CASH_OUT){
				Page page=customerService.getAgentCashOutData(transactionParamDTO, 1);
				model.put("page",page );
				model.put("transactionParamDTO",transactionParamDTO );
				return "confirmAgentCashOut";
			}else{
				return "transactionDetailsForm";
			}
		}
		finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"ProcessTransaction");
		}

	}

	@RequestMapping("/selectTransactionSupportForm.htm")
	public String selectTransactionSupportForm(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("txnList",transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"TransactionServices");
			
			// adding to make a defalut country as SouthSudan, by vineeth on 13-11-2018
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			// end
		}
		return "selectTransactionSupportForm";
	}

	@RequestMapping("/getAccountDetailsForSupport.htm")
	public String getAccountDetailsForSupport(HttpServletRequest request,ModelMap model,HttpSession session,HttpServletResponse response){

		try {
			System.out.println(request.getParameter("mobileNumber"));
			System.out.println(Integer.parseInt(request.getParameter("transactionType")));
			AccountDetailsDTO accountDetailsDTO = transactionService.getAccountDetails(request.getParameter("mobileNumber"),null,false);
			accountDetailsDTO.setTransactionType(Integer.parseInt(request.getParameter("transactionType")));

			model.put("accountDetailsDTO", accountDetailsDTO );
			session.setAttribute("accountDetailsDTO", accountDetailsDTO);

			return "transactionSupportForm";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "selectTransactionSupportForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "selectTransactionSupportForm";
		}finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"AccountDetails");
			
		}

	}

	@RequestMapping("/processSupportTransaction.htm")
	public String processSupportTransaction(TransactionParamDTO transactionParamDTO ,Map<String, Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){

		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			TransactionReceiptDTO receipt = transactionService.processSupportTransaction(transactionParamDTO,auth.getName(),localeResolver.resolveLocale(request));
			receipt.setCustomerId(transactionParamDTO.getCustomerId());
			receipt.setCustomerName(transactionParamDTO.getCustomerName());
			receipt.setAccountAlias(transactionParamDTO.getAccountAlias());
			receipt.setMobileNumber(transactionParamDTO.getMobileNumber());
			receipt.setTransactionType(transactionParamDTO.getTransactionType());
			receipt.setAmount(transactionParamDTO.getAmount());

			if(transactionParamDTO.getTransactionType() == EOTConstants.TXN_ID_TXNSTATEMENT){
				session.setAttribute("txnList", receipt.getTxnList());
				session.setAttribute("customerDetails", transactionParamDTO);
			}
			session.setAttribute("transactionReceipt", receipt);
			//model.put("transactionReceipt", receipt);
			return "transactionSupportReceipt";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			model.put("accountDetailsDTO", session.getAttribute("accountDetailsDTO"));
			return "transactionSupportForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("accountDetailsDTO", session.getAttribute("accountDetailsDTO"));
			return "transactionSupportForm";
		}
		finally{
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
		// adding to make a defalut country as SouthSudan, by vineeth on 13-11-2018
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
		// end
			pageLogger(request,response,"ProcessTransaction");
		}

	}

	//	@RequestMapping("/generatePDFTxnStatement.htm")
	//	public void generatePDFTxnStatement(Map<String, Object> model,HttpSession session,HttpServletResponse response){
	//		
	//		try {
	//			
	//			List<Transaction> txnList = (List<Transaction>) session.getAttribute("txnList");
	//
	//			response.setContentType("application/pdf");
	//			response.setHeader("Content-Disposition", "attachment;filename=statement.pdf");
	//			
	//			PdfFileGenerator pdfFileGenerator=new PdfFileGenerator();
	//			pdfFileGenerator.genaratePdfFile( txnList , response.getOutputStream() );
	//
	//
	//		}catch (Exception e) {
	//			e.printStackTrace();
	//			model.put("message",ErrorConstants.SERVICE_ERROR);
	//		}
	//		
	//	}

	@RequestMapping("/viewTransactions.htm")
	public String viewTransactions(@RequestParam Long customerId,@RequestParam Integer pageNumber, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
			Page page = transactionService.getTransactions(pageNumber, customerId, auth.getName());

			model.put("page", page);
			page.requestPage = "viewTransactions.htm";
			model.put("customerId", customerId);

		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"ViewTransactions");
		}
		return "viewTransactions";

	}	
	@SuppressWarnings("unchecked")
	@RequestMapping("/generatePDFTxnStatement.htm")
	public void generateJasperPDFTxnStatement(Map<String, Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){

		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;

		try{

			List<Transaction> txnList=(List<Transaction>) session.getAttribute("txnList");
			TransactionParamDTO txnParamDTO=(TransactionParamDTO)session.getAttribute("customerDetails");	
			Authentication auth = SecurityContextHolder.getContext().getAuthentication() ;
		//	CustomerDTO customerDTO=customerService.getCustomerDetails(Long.valueOf(txnParamDTO.getCustomerId()),auth.getName());
	
					// vineeth changes, to check locally
			

			//Uncomment below to run locally
		//	String path = request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+ "/";
			//Uncomment below for QA or prod
			String path = appConfigurations.getJasperPath()+ "/";
		//	System.out.println("Path=======> "+path);
		//	System.out.println("jasperPath=======> "+appConfigurations.getJasperPath());
			jasperDesign = JRXmlLoader.load(path + "txn_statement_en_US.jrxml");
			
			// changes over

		//	jasperDesign = JRXmlLoader.load(appConfigurations.getJasperPath()+"/"+"txn_statement_"+(customerDTO.getLanguage().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));		
			//change vinod
			/*jasperDesign = JRXmlLoader.load("D:\\opt\\home\\wallet\\reports"+"/"+"txn_statement_"+(customerDTO.getLanguage().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));*/
			jasperReport = JasperCompileManager.compileReport(jasperDesign);

			List<TxnStatementDTO> list=new ArrayList<TxnStatementDTO>();				

			for (Transaction transaction : txnList) {
				TxnStatementDTO txnStatementDTO=new TxnStatementDTO();					
				txnStatementDTO.setTransDate(transaction.getTransDate().getTime());
				Integer subStringIndex = transaction.getTransDesc().indexOf("\n");
				if(subStringIndex == -1)
					txnStatementDTO.setTransDesc(transaction.getTransDesc());
				else {
					String subString1 = transaction.getTransDesc().substring(0,subStringIndex);
					String subString2 = transaction.getTransDesc().substring(subStringIndex+1);
					txnStatementDTO.setTransDesc(subString1.concat(subString2));			
				}
				txnStatementDTO.setTransType(transaction.getTransType());
				txnStatementDTO.setAmount(transaction.getAmount().longValue());		
				txnStatementDTO.setFromAccountBalance(transaction.getFromAccountBalalnce());
				//txnStatementDTO.setToAccountBalance(transaction.getToAccountBalalnce());
				list.add(txnStatementDTO);
			}

			TransactionReceiptDTO dto=new TransactionReceiptDTO();
			dto.setTxnStmList(list);
			dto.setCustomerName(txnParamDTO.getCustomerName());
			dto.setAccountAlias(txnParamDTO.getAccountAlias());
			dto.setMobileNumber(txnParamDTO.getMobileNumber());
			dto.setFromDate(txnParamDTO.getFromDate());
			dto.setToDate(txnParamDTO.getToDate());
			dto.setBusinessPartnerCode(txnParamDTO.getBusinessPartnerCode());

			List<TransactionReceiptDTO> txnSmtList = new ArrayList<TransactionReceiptDTO>();
			txnSmtList.add(dto);
			//change vinod
			/*model.put("SUBREPORT_DIR", "D:\\opt\\home\\wallet\\reports" );*/
		//	model.put("SUBREPORT_DIR", path );
			model.put("SUBREPORT_DIR", appConfigurations.getJasperPath() );

			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(txnSmtList);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,model, datasource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();

			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String filename = txnParamDTO.getCustomerName().replaceAll(" ", "")+ "_" + dt + ".pdf";

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/pdf");

			response.setContentLength(baos.size());
			ServletOutputStream outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			outputStream.flush();

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"GeneratePDFTxnStatement");
		}



	}

	@RequestMapping("/viewRequests.htm")
	public String viewRequests(@RequestParam Long customerId,@RequestParam Integer pageNumber, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){

		try{

			Page page = transactionService.getRequests(pageNumber, customerId);

			model.put("page", page);
			model.put("customerId", customerId);

		}catch(EOTException e){
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"viewRequests");
		}
		return "viewRequests";

	}

	@RequestMapping("/reinitiateRequest.htm")
	public String reinitiateRequest(@RequestParam Long requestId , Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){

		try{

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			RequestReinitDTO dto = transactionService.reinitiateRequest(requestId,auth.getName());

			model.put( "response", dto );

		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ReinitiateRequest");
		}
		return "reinitiateRequest";

	}
	@RequestMapping("/saveTxnStatement.htm")
	public void saveTxnStatement(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;

		try{
			HttpSession hs=request.getSession();
			TransactionReceiptDTO dtoReceipt=  (TransactionReceiptDTO)hs.getAttribute("transactionReceipt");

			List<TxnStatementDTO> list=new ArrayList<TxnStatementDTO>();				
			if(dtoReceipt.getTxnList()!=null && !dtoReceipt.getTxnList().isEmpty()){
				for (Transaction transaction : dtoReceipt.getTxnList()) {
					TxnStatementDTO txnStatementDTO=new TxnStatementDTO();					
					txnStatementDTO.setTransDate(transaction.getTransDate().getTime());
					Integer subStringIndex = transaction.getTransDesc().indexOf("\n");
					if(subStringIndex == -1)
						txnStatementDTO.setTransDesc(transaction.getTransDesc());
					else {
						String subString1 = transaction.getTransDesc().substring(0,subStringIndex);
						String subString2 = transaction.getTransDesc().substring(subStringIndex+1);
						txnStatementDTO.setTransDesc(subString1.concat(subString2));			
					}
				//	txnStatementDTO.setTransDesc(transaction.getTransDesc());
					txnStatementDTO.setTransType(transaction.getTransType());
					txnStatementDTO.setAmount(transaction.getAmount().longValue());		
					list.add(txnStatementDTO);
				}
			}
			dtoReceipt.setTxnStmList(list);
			// vineeth changes, to check locally
			
			//Uncomment below for QA or prod
		//	String path = request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+ "/";
			//Uncomment below to run locally
			String path = appConfigurations.getJasperPath()+ "/";
			System.out.println("Path=======> "+path);
			System.out.println("jasperPath=======> "+appConfigurations.getJasperPath());
			jasperDesign = JRXmlLoader.load(path + "txn_receipt_en_US.jrxml");
			
			// chnages over
		//	jasperDesign = JRXmlLoader.load(appConfigurations.getJasperPath()+"/"+"txn_receipt_en_US.jrxml");			
			jasperReport = JasperCompileManager.compileReport(jasperDesign);

			List<TransactionReceiptDTO> txnSmtList = new ArrayList<TransactionReceiptDTO>();
			txnSmtList.add(dtoReceipt);
		//	model.put("SUBREPORT_DIR", path);
			model.put("SUBREPORT_DIR", appConfigurations.getJasperPath());
			Integer roleAccess = (Integer) hs.getAttribute("roleAccess");
			if(roleAccess.equals(EOTConstants.ROLEID_BANK_ADMIN))
				model.put("roleAccess", "Principal Agent Code");
			else
				model.put("roleAccess", "Super Agent Code");

			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(txnSmtList);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,model, datasource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();

			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String filename = dtoReceipt.getCustomerName().replaceAll(" ", "")+ "_" + dt + ".pdf";

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/pdf");

			response.setContentLength(baos.size());
			ServletOutputStream outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			outputStream.flush();

		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}


	}

	@RequestMapping("/showCharts.htm")
	public String showChart(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("masterData", transactionService.getAccountHeadList());			
			model.put("txnSummaryDTO", new TxnSummaryDTO());
			model.put("imgHide", "imgHide");
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));

		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				pageLogger(request,response,"TransactionReport");
			}catch(EOTException e){

			}
		}
		return "report";
	}
	@RequestMapping("/searchBarChart.htm")
	public String searchChart(TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpServletResponse response){		
		try{
			byte[] chartImg=transactionService.getChartImageBytes(txnSummaryDTO);
			response.setContentType("image/png");
			response.getOutputStream().write(chartImg); 		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"SearchBarReport");
		}
		return null;
	}
	@RequestMapping("/barChartDataValidation.htm")
	public String chartDataValidation(TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpServletResponse response,ModelMap map){

		try{		
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
			map.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			txnSummaryDTO.setImgType("imgCount");
			List list=transactionService.getChartList(txnSummaryDTO,auth.getName());
			String chartData = new ObjectMapper().writeValueAsString(list);
			System.out.println(chartData);
			map.put("chartData", chartData);
			map.put("fromDate", DateUtil.formatDateToStr(txnSummaryDTO.getFromDate()));
			map.put("toDate", DateUtil.formatDateToStr(txnSummaryDTO.getToDate()));
			map.put("imgUrl","searchBarChart.htm");
			map.put("chartType",txnSummaryDTO.getChartType());
			map.put("transactionType",txnSummaryDTO.getTransactionType());
			map.put("bankId",txnSummaryDTO.getBankId());
		}catch(EOTException e){
			e.printStackTrace();
			map.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			map.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				map.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				map.put("masterData", transactionService.getAccountHeadList());
			}catch(Exception e){        	
				e.printStackTrace();
			}
			finally{
				pageLogger(request,response,"SearchBarReport");
			}
		}
		return "report";
	}

	@RequestMapping("/searchPieChart.htm")
	public String searchPieChart(TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpServletResponse response){		
		try{
			byte[] chartImg=transactionService.getPieChartImageBytes(txnSummaryDTO);
			response.setContentType("image/png");
			response.getOutputStream().write(chartImg); 		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"SearchPieReport");
		}
		return null;
	}
	@RequestMapping("/pieChartDataValidation.htm")
	public String pieChartDataValidation(TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpServletResponse response,ModelMap map){

		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			map.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			txnSummaryDTO.setImgType("imgCount");
			List list=transactionService.getChartList(txnSummaryDTO,auth.getName());
			String chartData = new ObjectMapper().writeValueAsString(list);
			System.out.println(chartData);
			map.put("chartData", chartData);
			map.put("fromDate", DateUtil.formatDateToStr(txnSummaryDTO.getFromDate()));
			map.put("toDate", DateUtil.formatDateToStr(txnSummaryDTO.getToDate()));
			map.put("imgUrl","searchPieChart.htm");
			map.put("chartType",txnSummaryDTO.getChartType());
			map.put("transactionType",txnSummaryDTO.getTransactionType());
			map.put("bankId",txnSummaryDTO.getBankId());
			map.put("barReport","barReport");		    
		}catch(EOTException e){
			e.printStackTrace();
			map.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			map.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				map.put("masterData", transactionService.getAccountHeadList());
			}catch(Exception e){        	
				e.printStackTrace();
			}
			finally{
				map.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				pageLogger(request,response,"SearchPieReport");
			}
		}
		return "report";
	}

	@RequestMapping("/txnReports.htm")
	public String showTxnReports(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("message",request.getParameter("message"));
			model.put("txnSummaryDTO", new TxnSummaryDTO());
			model.put("masterData", transactionService.getAccountHeadList());

		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"AccountReports");
		}
		return "transactionReports";
	}

	@RequestMapping("/getBrankByChPoolID.htm")
	public String getBankByChPoolId(ModelMap model,HttpServletRequest request){
		try{
			List<Bank> list=transactionService.getBankByChPoolId(request.getParameter("clearingPoolId").toString());
			model.put("entity","bankId");
			model.put("id", "bankId");
			model.put("value", "bankName");
			model.put("list",list);
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		return "combo";
	}

	@RequestMapping("/getAccountHeadByBankId.htm")
	public String getAccountHeadByBankId(ModelMap model,HttpServletRequest request){
		try{
			List<Map> list=transactionService.getAccountHeadByBankId(request.getParameter("bankId").toString());
			model.put("entity","accountNumber");
			model.put("id", "accountNumber");
			model.put("value", "accountHead");
			model.put("list",list);
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		return "combo";
	}

	@RequestMapping("/accountLedgerReport.htm")
	public String saveAccountLedgerReport(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;
		try{
			// String path = request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+ "/";
			jasperDesign = JRXmlLoader.load(appConfigurations.getJasperPath()+"/"+"account_ledger_report_"+(localeResolver.resolveLocale(request).toString().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));	
		//	jasperDesign = JRXmlLoader.load(request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+"/"+"account_ledger_report_"+(localeResolver.resolveLocale(request).toString().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));	
			jasperReport = JasperCompileManager.compileReport(jasperDesign);

			List<Object> txnSmtList = transactionService.getAccountLedgerReport(txnSummaryDTO.getAccountNumber(),txnSummaryDTO.getFromDate(),txnSummaryDTO.getToDate());

			String accAliasName=txnSmtList.get(txnSmtList.size()-1)!= null ? txnSmtList.get(txnSmtList.size()-1).toString() : null;
			txnSmtList.remove(txnSmtList.size()-1);

			Long totalDRAmt=0L;
			Long totalCRAmt=0L;
			for(Object obj:txnSmtList){
				if(((String)((Map)obj).get("transType")).equals("DR")){
					totalDRAmt += (Long)((Map)obj).get("amount");
				}else if(((String)((Map)obj).get("transType")).equals("CR")){
					totalCRAmt += (Long)((Map)obj).get("amount");
				}
			}
			if(totalCRAmt>totalDRAmt){
				model.put("forwardMessage", "Balance carried forward");
				model.put("toDRAmt", totalCRAmt-totalDRAmt);
				model.put("grandTotal", totalCRAmt);
			}else if(totalDRAmt>totalCRAmt){
				model.put("forwardMessage", "Balance brought down");
				model.put("toCRAmt", totalDRAmt-totalCRAmt);
				model.put("grandTotal", totalDRAmt);
			}
			model.put("fromDate", txnSummaryDTO.getFromDate());
			model.put("toDate", txnSummaryDTO.getToDate());
		// change vineeth, on 30-07-2018
			model.put("accAliasName",  accAliasName+"OUNT");			
	    //	model.put("accAliasName", accAliasName);
		// change over vineeth
			if(txnSmtList==null || txnSmtList.isEmpty())
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			
			model.put("jasperPath", appConfigurations.getJasperPath());
	//		model.put("jasperPath", request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+"/");
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(txnSmtList);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,model, datasource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();

			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String filename = "account_ledger"+ "_" + dt + ".pdf";

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/pdf");

			response.setContentLength(baos.size());
			ServletOutputStream outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			outputStream.flush();

		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			//return "redirect:/txnReports.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			return "transactionReports";
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "transactionReports";
			//return "redirect:/txnReports.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}finally{
			pageLogger(request,response,"AccountLedgerReport");
		}

		return null;
	}

	@RequestMapping("/trialBalanceReport.htm")
	public String saveTrialBalanceReport(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;
		try{
			// String path = request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+ "/";
			jasperDesign = JRXmlLoader.load(appConfigurations.getJasperPath()+"/"+"trial_balance_"+(localeResolver.resolveLocale(request).toString().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));	
		//	jasperDesign = JRXmlLoader.load(request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+"/"+"trial_balance_"+(localeResolver.resolveLocale(request).toString().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));
			jasperReport = JasperCompileManager.compileReport(jasperDesign);

			List<Object> txnSmtList = transactionService.getTrialBalance(txnSummaryDTO);

			if(txnSmtList==null || txnSmtList.isEmpty())
				throw new EOTException(ErrorConstants.NO_TXNS_FOUND);

			Long totalDRAmt=0L;
			Long totalCRAmt=0L;
			for(Object obj:txnSmtList){

				totalDRAmt += ((Map)obj).get("drAmount")!=null ? (Long)((Map)obj).get("drAmount"):0L;

				totalCRAmt += ((Map)obj).get("crAmount")!=null ? (Long)((Map)obj).get("crAmount"):0L;

			}
			if(txnSummaryDTO.getBankName()==null || txnSummaryDTO.getBankName().equals("")){
				txnSummaryDTO.setBankName(transactionService.getBankName());
			}
			model.put("bankName", txnSummaryDTO.getBankName());
			model.put("fromDate", txnSummaryDTO.getFromDate());
			model.put("toDate", txnSummaryDTO.getToDate());
			model.put("totalDRAmt", totalDRAmt);
			model.put("totalCRAmt", totalCRAmt);
			model.put("jasperPath", appConfigurations.getJasperPath());
	//		model.put("jasperPath", request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+"/");

			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(txnSmtList);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,model, datasource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();

			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String filename = "trial_balance"+ "_" + dt + ".pdf";

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/pdf");

			response.setContentLength(baos.size());
			ServletOutputStream outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			outputStream.flush();

		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "redirect:/txnReports.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/txnReports.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}finally{
			pageLogger(request,response,"TrialBalanceReport");
		}
		return null;

	}

	@RequestMapping("/showPendingTransactions.htm")
	public String showPendingTransactions(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String status = "";
		String fromDate = "";
		String toDate = "";

		try{
			customerName = request.getParameter("customerName");
			mobileNumber = request.getParameter("mobileNumber");
			txnDate = request.getParameter("txnDate");
			amount = request.getParameter("amount");
			txnType = request.getParameter("txnType");
			status = request.getParameter("status");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");	

			session.setAttribute("customerName", customerName);
			session.setAttribute("mobileNumber", mobileNumber);
			session.setAttribute("txDate", txnDate );
			session.setAttribute("amount", amount );
			session.setAttribute("txnType", txnType );
			session.setAttribute("status", status );
			session.setAttribute("fromDate", fromDate );
			session.setAttribute("toDate", toDate);

			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page=transactionService.getPendingTrnasactions(customerName, mobileNumber, amount, status, txnDate, txnType, auth.getName(),fromDate,toDate,pageNumber);
			page.requestPage = "searchPendingTxns.htm";
			model.put("page",page);
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			e.printStackTrace();

			model.put("message", e.getErrorCode());

		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("customerName", customerName);
			model.put("mobileNumber", mobileNumber);
			model.put("txDate", txnDate);
			model.put("amount", amount);
			model.put("txnType", txnType);
			model.put("status", status);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
		}
		return "pendingTransactions";
	}

	@RequestMapping("/searchPendingTxns.htm")
	public String searchPendingTxns(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	

		try{		
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			String customerName =  session.getAttribute("customerName") == null ? "" : session.getAttribute("customerName").toString() ;
			String mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			String txnDate =  session.getAttribute("txnDate") == null ? "" : session.getAttribute("txnDate").toString() ;
			String amount =  session.getAttribute("amount") == null ? "" : session.getAttribute("amount").toString() ;
			String txnType = session.getAttribute("txnType") == null ? "" : session.getAttribute("txnType").toString() ;
			String status = session.getAttribute("status") == null ? "" : session.getAttribute("status").toString() ;			
			String fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
			String toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;


			Page page=transactionService.getPendingTrnasactions(customerName, mobileNumber, amount, status, txnDate, txnType, auth.getName(),fromDate,toDate, pageNumber);
			page.requestPage = "searchPendingTxns.htm";
			model.put("page",page);
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "pendingTransactions";
	}

	@RequestMapping(value = "/exportToXLSForPendingTransactionSummary.htm")
	public String exportToXLSForPendingTransactionSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws EOTException {

		String viewPage ="pendingTransactions";
		List list = null;
		HSSFWorkbook wb = null;

		try {
			String customerName =  session.getAttribute("customerName") == null ? "" : session.getAttribute("customerName").toString() ;
			String mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			String txnDate =  session.getAttribute("txnDate") == null ? "" : session.getAttribute("txnDate").toString() ;
			String amount =  session.getAttribute("amount") == null ? "" : session.getAttribute("amount").toString() ;
			String txnType = session.getAttribute("txnType") == null ? "" : session.getAttribute("txnType").toString() ;
			String status = session.getAttribute("status") == null ? "" : session.getAttribute("status").toString() ;	
			String fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
			String toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;

			list = transactionService.exportToXLSForPendingTransactionSummary(customerName,mobileNumber,txnDate,amount,txnType,status,fromDate,toDate,model);
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	){
				BankTellers teller = customerService.getBankTeller(userName);

				wb = wrapper.createSpreadSheetFromListForPendingTransactionSummary(list,localeResolver.resolveLocale(request),messageSource,teller.getBank().getBankName(),fromDate,toDate);
			}else{
				BankTellers teller = customerService.getBankTeller(userName);
				wb = wrapper.createSpreadSheetFromListForPendingTransactionSummary(list,localeResolver.resolveLocale(request),messageSource,teller.getBranch().getLocation(),fromDate,toDate);
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		} catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request));
			model.put("txnSummaryDTO",txnSummaryDTO);
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
		}
		return viewPage;
	}

	@RequestMapping("/txnApprove.htm")
	public String approveTransactions(@RequestParam String referenceId,@RequestParam Long txnId,ModelMap model,HttpSession session,HttpServletRequest request,HttpServletResponse response)throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;

		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String status = "";
		String fromDate = "";
		String toDate = "";

		try {
			TransactionReceiptDTO receipt= transactionService.approveTransaction(referenceId,auth.getName(),txnId,localeResolver.resolveLocale(request));
			session.removeAttribute("transactionReceipt");
			session.removeAttribute("txnList");
			session.removeAttribute("customerDetails");
			session.setAttribute("transactionReceipt", receipt);
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "redirect:/showPendingTransactions.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/showPendingTransactions.htm";
		}finally{
			//pageLogger(request,response,"TransactionServices");
			model.put("page",transactionService.getPendingTrnasactions(customerName, mobileNumber, amount, status, txnDate, txnType, auth.getName(),fromDate,toDate, pageNumber));
		}
		return "approveTxnPopup";
	}
	@RequestMapping("/txnReject.htm")
	public String rejectTransactions(@RequestParam String referenceId,@RequestParam Long txnId,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String status = "";
		String fromDate = "";
		String toDate = "";
		try {
			model.put("rejectTxn", transactionService.getRejectedCustomer(txnId));
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("page",transactionService.getPendingTrnasactions(customerName, mobileNumber, amount, status, txnDate, txnType, auth.getName(),fromDate,toDate, pageNumber));
		}
		return "viewRejectTransaction";
	}
	@RequestMapping("/txnCancel.htm")
	public String cancelTransactions(@RequestParam String referenceId,@RequestParam Long txnId,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			transactionService.cancelTransaction(referenceId,auth.getName(),txnId);
			model.put("message", "TRANSACTION_CANCELLED");
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			//pageLogger(request,response,"TransactionServices");
			//model.put("page",transactionService.getPendingTrnasactions(auth.getName(),pageNumber));
		}
		return "redirect:/showPendingTransactions.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
	}
	@RequestMapping("/viewRejectTransaction.htm")
	public String viewRejectTransaction(@RequestParam Long txnId,@RequestParam String referenceId,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			String commnet=request.getParameter("comment");
			transactionService.rejectTransaction(referenceId,auth.getName(),commnet,txnId);
			model.put("message", "TRANSACTION_REJECTED");
		} catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "viewRejectTransaction";
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "viewRejectTransaction";
		}finally{
			//pageLogger(request,response,"TransactionServices");
			model.put("rejectTxn", transactionService.getRejectedCustomer(txnId));
			//model.put("page",transactionService.getPendingTrnasactions(auth.getName(), pageNumber));
		}
		return "redirect:/showPendingTransactions.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
	}

	@RequestMapping("/showTxnSummary.htm")
	public String showTxnSummary(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		TxnSummaryDTO txnSummaryDTO = new TxnSummaryDTO();
		int pageNumber=1;

		/*try{			
			txnSummaryDTO=new TxnSummaryDTO();

			Page page=transactionService.searchTxnSummary(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{*/
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));			
			model.put("txnSummaryDTO",txnSummaryDTO );
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			model.put("bankList", webUserService.getBanksByCountry(countryId));
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			pageLogger(request,response,"TransactionSummary");
		//}
		return "transactionReport";
	}

	@RequestMapping("/searchTxnSummary.htm")
	public String searchTxnSummary(TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response,HttpSession session) throws NumberFormatException, Exception{
		int pageNumber = 1;
		WebUser webUser=null;
		try{
			
			//txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {

				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				webUser=customerService.getUser(userName);	

				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
			//	txnSummaryDTO.setBankId(txnSummaryDTO.getBank()); 
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}			
			/*Page page=transactionService.searchTxnSummary(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);	*/
			model.put("language",localeResolver.resolveLocale(request));
		}/*catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} */catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{	
			model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("txnSummaryDTO",txnSummaryDTO);
			model.put("language",localeResolver.resolveLocale(request));
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
			if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()))
				model.put("partners",dashBoardService.loadBusinessPartnerByType(Integer.parseInt(txnSummaryDTO.getPartnerType())));		
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			webUser=customerService.getUser(userName);
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
				Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
				model.put("bankList",webUserService.getBanksByCountry(countryId));
			}else if(txnSummaryDTO.getCountryId()!=null){
				model.put("bankList",webUserService.getBanksByCountry(txnSummaryDTO.getCountryId()));
			}
		/*	
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);*/
		}	

		return "transactionReport";
	}	

	@RequestMapping(value = "/exportToXLSForTransactionSummary.htm")
	public String exportToXLSForTransactionSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws EOTException {

		String viewPage ="transactionReport";
		List list = null;
		HSSFWorkbook wb = null;

		try {	

			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			list = transactionService.exportToXLSForTransactionSummary(txnSummaryDTO, model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			txnSummaryDTO.setUserFirstName(webUser.getFirstName());


			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	){
				BankTellers teller = customerService.getBankTeller(userName);

				wb = wrapper.createSpreadSheetFromListForTransactionSummary(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,teller.getBank().getBankName());
			}else{
				wb = wrapper.createSpreadSheetFromListForTransactionSummary(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,null);
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		} catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request));
			model.put("txnSummaryDTO",txnSummaryDTO);
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
		}
		return viewPage;
	}

	@RequestMapping(value = "/exportToXLSForTransactionSummaryForBankTellerEOD.htm")
	public String exportToXLSForTransactionSummaryForBankTellerEOD(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws EOTException {
		
		//System.out.println("******************Inside exportToXLSForTransactionSummaryForBankTellerEOD*********************");

		String viewPage ="transactionReport";
		List list = null;
		HSSFWorkbook wb = null;

		try {	

			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			list = transactionService.exportToXLSForTransactionSummaryForBankTellerEOD(txnSummaryDTO, model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);
			txnSummaryDTO.setUserFirstName(webUser.getFirstName());

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN){
				BankTellers teller = customerService.getBankTeller(userName);
				if(null !=list)
				wb = wrapper.createSpreadSheetFromListForTransactionSummaryForBankTellerEOD(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,teller.getBank().getBankName());
			}else{
				if(null != list)
				wb = wrapper.createSpreadSheetFromListForTransactionSummaryForBankTellerEOD(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,null);
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();
			if(null != wb)
			wb.write(os);
			os.flush();
			os.close();
			System.out.println("******************end exportToXLSForTransactionSummaryForBankTellerEOD*********************");
		} catch(EOTException e){
			e.printStackTrace();
			System.out.println("*****************e.getErrorCode()************" + e.getErrorCode());
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("*****************e.getMessage()************" + e.getMessage());
		}finally{
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request));
			model.put("txnSummaryDTO",txnSummaryDTO);
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
		}
		return null;
	}

	@RequestMapping(value = "/exportToXLSForTransactionSummaryForTxnSummary.htm")
	public String exportToXLSForTransactionSummaryForTxnSummary(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws EOTException {

		String viewPage ="transactionReport";
		List list = null;
		HSSFWorkbook wb = null;

		try {	

			//txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			list = transactionService.exportToXLSForTransactionSummaryForTxnSummary(txnSummaryDTO, model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);
			txnSummaryDTO.setUserFirstName(webUser.getFirstName());

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	){
				BankTellers teller = customerService.getBankTeller(userName);

				wb = wrapper.createSpreadSheetFromListForTransactionSummaryForTxnSummary(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,teller.getBank().getBankName());
			}else{
				wb = wrapper.createSpreadSheetFromListForTransactionSummaryForTxnSummary(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,null);
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		} catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request));
			model.put("txnSummaryDTO",txnSummaryDTO);
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
		}
		return viewPage;
	}

	@RequestMapping(value = "/exportToXLSForTransactionSummaryPerBank.htm")
	public String exportToXLSForTransactionSummaryPerBank(TxnSummaryDTO txnSummaryDTO, Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws EOTException {

		String viewPage ="transactionReport";
		List list = null;
		HSSFWorkbook wb = null;

		try {	

			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			list = transactionService.exportToXLSForTransactionSummaryPerBank(txnSummaryDTO, model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);
			txnSummaryDTO.setUserFirstName(webUser.getFirstName());

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	){
				BankTellers teller = customerService.getBankTeller(userName);

				wb = wrapper.createSpreadSheetFromListForTransactionSummaryPerBank(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,teller.getBank().getBankName());
			}else{
				wb = wrapper.createSpreadSheetFromListForTransactionSummaryPerBank(list,localeResolver.resolveLocale(request),messageSource,txnSummaryDTO,null);
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		} catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request));
			model.put("txnSummaryDTO",txnSummaryDTO);
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
		}
		return viewPage;
	}

	@RequestMapping("/getCustomerProfilesForTxnReport.htm")
	public String getCustomerProfiles(@RequestParam Integer bankId, Map<String, Object> model){

		try {
			model.put("entity", "profileName");
			model.put("id", "profileId");
			model.put("value", "profileName");
			model.put("list", transactionRulesService.getCustomerProfilesByBankId(bankId));
		} catch (EOTException e) {
		}

		return "combo1";
	}

	@RequestMapping("/getBranchesForTxnReport.htm")
	public String getBranches(@RequestParam Integer bankId, Map<String, Object> model){

		model.put("entity", "location");
		model.put("id", "branchId");
		model.put("value", "location");
		model.put("list", webUserService.getAllBranchFromBank(bankId));
		return "combo";
	}		

	@RequestMapping("/getBanksByCountry.htm")
	public String getBanksByCountry(@RequestParam Integer countryId, Map<String, Object> model){

		model.put("entity", "bankId");
		model.put("id", "bankId");
		model.put("value", "bankName");
		model.put("list", webUserService.getBanksByCountry(countryId));
		return "combo";
	}
	@RequestMapping("/getRejectedReason.htm")
	public String getRejectedReason(@RequestParam String referenceId,@RequestParam Long txnId,ModelMap model,HttpServletRequest request,HttpServletResponse response)throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String status = "";
		String fromDate = "";
		String toDate = "";
		try {
			model.put("rejectTxn", transactionService.getRejectedCustomer(txnId));
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("page",transactionService.getPendingTrnasactions(customerName, mobileNumber, amount, status, txnDate, txnType, auth.getName(),fromDate,toDate, pageNumber));
		}
		return "viewRejectedReason";
	}

	@RequestMapping("/cancellation.htm")
	public String conciliationForm(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String fromDate = "";
		String toDate = "";

		try{
			customerName = request.getParameter("customerName");
			mobileNumber = request.getParameter("mobileNumber");
			txnDate = request.getParameter("txnDate");
			amount = request.getParameter("amount");
			txnType = request.getParameter("txnType");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");	

			session.setAttribute("customerName", customerName);
			session.setAttribute("mobileNumber", mobileNumber);
			session.setAttribute("txDate", txnDate );
			session.setAttribute("amount", amount );
			session.setAttribute("txnType", txnType );
			session.setAttribute("fromDate", fromDate );
			session.setAttribute("toDate", toDate);

			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page=transactionService.getCancellations(customerName, mobileNumber, amount,txnDate, txnType, auth.getName(),fromDate,toDate,pageNumber);
			page.requestPage = "searchCancellations.htm";
			model.put("page",page);
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			e.printStackTrace();

			model.put("message", e.getErrorCode());

		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("customerName", customerName);
			model.put("mobileNumber", mobileNumber);
			model.put("txDate", txnDate);
			model.put("amount", amount);
			model.put("txnType", txnType);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
		}

		return "cancellation";		
	}

	@RequestMapping("/searchCancellations.htm")
	public String searchCancellations(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	

		try{		
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			String customerName =  session.getAttribute("customerName") == null ? "" : session.getAttribute("customerName").toString() ;
			String mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			String txnDate =  session.getAttribute("txnDate") == null ? "" : session.getAttribute("txnDate").toString() ;
			String amount =  session.getAttribute("amount") == null ? "" : session.getAttribute("amount").toString() ;
			String txnType = session.getAttribute("txnType") == null ? "" : session.getAttribute("txnType").toString() ;
			String fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
			String toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;


			Page page=transactionService.getCancellations(customerName, mobileNumber, amount,txnDate, txnType, auth.getName(),fromDate,toDate,pageNumber);
			page.requestPage = "searchCancellations.htm";
			model.put("page",page);
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "cancellation";
	}

	@RequestMapping("/voidTransaction.htm")
	public String voidTransaction(@RequestParam Integer transactionId,ModelMap model,HttpServletRequest request,HttpServletResponse response){

		try {
			transactionService.voidTransaction(transactionId);
			model.put("message", "REVERSAL_SUCCESS");
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{

		}		
		return "redirect:/cancellation.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");		
	}

	@RequestMapping("/showAdjustmentForm.htm") 	
	public String showAdjustmentForm(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){

		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String fromDate = "";
		String toDate = "";
		try{

			customerName = request.getParameter("customerName");
			mobileNumber = request.getParameter("mobileNumber");
			txnDate = request.getParameter("txnDate");
			amount = request.getParameter("amount");
			txnType = request.getParameter("txnType");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");	

			session.setAttribute("customerName", customerName);
			session.setAttribute("mobileNumber", mobileNumber);
			session.setAttribute("txDate", txnDate );
			session.setAttribute("amount", amount );
			session.setAttribute("txnType", txnType );
			session.setAttribute("fromDate", fromDate );
			session.setAttribute("toDate", toDate);

			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page=transactionService.getAdjustmentTransactions(customerName, mobileNumber, amount,txnDate, txnType,fromDate,toDate,pageNumber);
			page.requestPage = "searchAdjustmentTransactions.htm";
			model.put("page",page);
			model.put("message",request.getParameter("message"));	
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			/*model.put("message",request.getParameter("message"));*/
			model.put("customerName", customerName);
			model.put("mobileNumber", mobileNumber);
			model.put("txDate", txnDate);
			model.put("amount", amount);
			model.put("txnType", txnType);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
		}

		return "adjustmentForm";

	}

	@RequestMapping("/searchAdjustmentTransactions.htm")
	public String searchAdjustmentTransactions(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){

		try{		
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			String customerName =  session.getAttribute("customerName") == null ? "" : session.getAttribute("customerName").toString() ;
			String mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			String txnDate =  session.getAttribute("txnDate") == null ? "" : session.getAttribute("txnDate").toString() ;
			String amount =  session.getAttribute("amount") == null ? "" : session.getAttribute("amount").toString() ;
			String txnType = session.getAttribute("txnType") == null ? "" : session.getAttribute("txnType").toString() ;
			String fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
			String toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;

			Page page=transactionService.getAdjustmentTransactions(customerName, mobileNumber, amount,txnDate, txnType,fromDate,toDate,pageNumber);
			page.requestPage = "searchAdjustmentTransactions.htm";
			model.put("page",page);
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}

		return "adjustmentForm";	   
	}

	@RequestMapping("/viewAdjustmentTransaction.htm")
	public String viewAdjustmentTransaction(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session){

		String transactionId="";
		String amount = "";
		String customerName = ""; 
		String mobileNumber = ""; 
		String transactionType = ""; 
		String txnDate = "";
		try{	
			transactionId=request.getParameter("transactionId");
			amount = request.getParameter("amount");
			customerName = request.getParameter("customerName"); 
			mobileNumber = request.getParameter("mobileNumber"); 
			transactionType = request.getParameter("transactionType"); 
			txnDate = request.getParameter("txnDate"); 

			session.setAttribute("transactionId", transactionId);
			session.setAttribute("amount", amount);
			session.setAttribute("customerName", customerName );
			session.setAttribute("mobileNumber", mobileNumber );
			session.setAttribute("transactionType", transactionType );		
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("transactionId",transactionId);
			model.put("amount",amount);
			model.put("customerName",customerName);
			model.put("mobileNumber",mobileNumber);
			model.put("transactionType",transactionType);
			model.put("txnDate",txnDate);
		}			
		return "viewAdjustmentTransaction";		
	}

	@RequestMapping("/submitAdjustmentTxn.htm")
	public String submitAdjustmentTxn(ModelMap model,HttpServletRequest request,HttpServletResponse response){

		String transactionId=request.getParameter("txnType");
		String adjustedAmount = request.getParameter("adjustedAmount");
		String adjustedFee = request.getParameter("adjustedFee"); 
		String transactionType = request.getParameter("adjustmentTxnType"); 
		String comment = request.getParameter("comment"); 
		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String fromDate = "";
		String toDate = "";

			customerName = request.getParameter("customerName");
			mobileNumber = request.getParameter("mobileNumber");
			txnDate = request.getParameter("txnDate");
			amount = request.getParameter("amount");
			txnType = request.getParameter("txnType");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");	
		try{
			transactionService.adjustmentTransaction(transactionId,adjustedAmount,adjustedFee,transactionType,comment,mobileNumber);
			model.put("message", "ADJUSTMENT_SUCCESS");
			
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page=transactionService.getAdjustmentTransactions(customerName, mobileNumber, amount,txnDate, txnType,fromDate,toDate,pageNumber);
			page.requestPage = "searchAdjustmentTransactions.htm";
			model.put("page",page);
			
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("transactionId",transactionId);
			model.put("adjustedAmount",adjustedAmount);
			model.put("adjustedFee",adjustedFee);
			model.put("comment",comment);
			model.put("transactionType",transactionType);
			
			model.put("customerName","");
			model.put("mobileNumber","");
			model.put("amount","");
		}	   
		/*return "redirect:/showAdjustmentForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
		return "adjustmentForm";
	}

	@RequestMapping("/uploadReversalDetails.htm")
	public String uploadReversalDetails(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			ReversalDTO reversalDTO=new ReversalDTO();
			model.put("reversalDTO", reversalDTO);

		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
		}
		return "uploadReversalDetails";
	}

	@RequestMapping("/uploadReversalFile.htm")
	public String uploadReversalFiles(ReversalDTO reversalDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			transactionService.uploadReversalDetails(reversalDTO);
			model.put("message", "ADJUSTMENT_SUCCESS");

		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);	
			e.printStackTrace();
			return "uploadReversalDetails";
		}

		return "redirect:/showAdjustmentForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
	}

	@RequestMapping("/showExternalTransactions.htm")
	public String showExternalTransactions(HttpServletRequest request,Map<String, Object> model,HttpServletResponse response,HttpSession session){
		ExternalTransactionDTO externalTransactionDTO = null;

		try{		
			externalTransactionDTO = new ExternalTransactionDTO();
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;	
			Page page=transactionService.searchExternalTxns(externalTransactionDTO,pageNumber);
			page.requestPage = "searchExternalTxns.htm";
			model.put("page",page);		

		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			session.setAttribute("externalTransactionDTO",externalTransactionDTO);
			model.put("message", request.getParameter("message"));
			model.put("externalTransactionDTO",externalTransactionDTO);
			model.put("operatorList",transactionService.getOperatorList());	
		}	

		return "externalTransactions";
	}

	@RequestMapping("/searchExternalTxns.htm")
	public String searchExternalTxns(ExternalTransactionDTO externalTransactionDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		int pageNumber=1;
		try{	

			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				externalTransactionDTO = (ExternalTransactionDTO) session.getAttribute("externalTransactionDTO");
			} else {
				session.setAttribute("externalTransactionDTO", externalTransactionDTO);
			}	

			Page page = transactionService.searchExternalTxns(externalTransactionDTO,pageNumber);
			page.requestPage = "searchExternalTxns.htm";
			model.put("page",page);	
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"External Transactionss");
			model.put("operatorList",transactionService.getOperatorList());	
			model.put("externalTransactionDTO",externalTransactionDTO);
			session.setAttribute("externalTransactionDTO", externalTransactionDTO);
		}

		return "externalTransactions";

	}

	@RequestMapping("exportToXlsExternalTransactionDetails.htm")
	public String exportToXlsExternalTransactionDetails(ExternalTransactionDTO externalTransactionDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session){

		List list = null;
		HSSFWorkbook wb = null;
		OutputStream os =null;
		try {
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			externalTransactionDTO = (ExternalTransactionDTO) session.getAttribute("externalTransactionDTO");
			externalTransactionDTO.setUserName(webUser.getFirstName());
			list = transactionService.exportToXLSForExternalTransactionDetails(externalTransactionDTO);

			wb = wrapper.createSpreadSheetFromListForExternalTransactions(list,localeResolver.resolveLocale(request),messageSource,externalTransactionDTO);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			 os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			model.put("operatorList",transactionService.getOperatorList());	
			model.put("externalTransactionDTO",externalTransactionDTO);
			if(null != os)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return "externalTransactions";
	}


	@RequestMapping("exportToXlsExternalTransactionSummaryDetails.htm")
	public String exportToXlsExternalTransactionSummaryDetails(ExternalTransactionDTO externalTransactionDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session){

		List list = null;
		HSSFWorkbook wb = null;
		try {
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			externalTransactionDTO = (ExternalTransactionDTO) session.getAttribute("externalTransactionDTO");
			externalTransactionDTO.setUserName(webUser.getFirstName());
			list = transactionService.exportToXlsExternalTransactionSummaryDetails(externalTransactionDTO);

			wb = wrapper.createSpreadSheetFromListForExternalTransactionSummaryDetails(list,localeResolver.resolveLocale(request),messageSource,externalTransactionDTO);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			model.put("operatorList",transactionService.getOperatorList());	
			model.put("externalTransactionDTO",externalTransactionDTO);
		}

		return "externalTransactions";
	}

	@RequestMapping("/uploadTransactionDetails.htm")
	public String uploadTransactionDetails(@RequestParam int templetID, Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			TransactionParamDTO transactionParamDTO=new TransactionParamDTO();
			transactionParamDTO.setTemplateId(templetID);
			model.put("transactionParamDTO", transactionParamDTO);

		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			pageLogger(request,response,"Upload Bulk Payment");
		}
		return "uploadTransactionDetails";
	}

	@RequestMapping("/uploadTrnsactionFile.htm")
	public String uploadTrnsactionFile(TransactionParamDTO transactionParamDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		
		List list = null;
		HSSFWorkbook wb = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		//Long timeStamp=System.currentTimeMillis();
		String timeStamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String path = appConfigurations.getBulkPaymentReportDownloadPath();
		BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
		String dirName =path+businessPartner.getName();
		String fileName = userName+"_"+timeStamp;
		
		try{
			FileUtil.validateUploadTrnsactionFile(transactionParamDTO.getTxnFile());

			writeFile(transactionParamDTO, dirName, fileName);
			WebUserDTO webUserDTO = webUserService.getUser(userName);
			list=transactionService.uploadTransactionDetails(transactionParamDTO, localeResolver.resolveLocale(request), webUserDTO, timeStamp);
			
			HttpSession session = request.getSession();
			session.setAttribute("UploadFileDetails", transactionParamDTO);
			session.setAttribute("processedList", list); 
				
			wb = wrapper.createCSVForFailedBulkUpload(list, localeResolver.resolveLocale(request), messageSource);
			dirName = dirName.contains(" ")?dirName.replace(" ", "_"):dirName;
			dirName=dirName+File.separator+EOTConstants.BULK_PAYMENT_PROCESSED;
			fileName=fileName+".xls";
			//String dirName ="F:/"+userName; 
			
			File dir = new File (dirName);
			if(!dir.exists()) {
				dir.mkdirs();
			}	
			 File actualFile = new File (dir, fileName);
			 FileOutputStream outputStream = new FileOutputStream(actualFile);
			 wb.write(outputStream);
			 outputStream.flush();
			 outputStream.close();
			 
			  
			/*if (list != null && list.size()>0) {
				wb = wrapper.createCSVForFailedBulkUpload(list, localeResolver.resolveLocale(request), messageSource);
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + date + "_" + System.currentTimeMillis() + "_report.xls");
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
			}*/
			
			model.put("message", "TRANSACTION_SUCCESS");
			
	}catch(EOTException e ){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			model.put("UploadFileDetails", transactionParamDTO);
			return "uploadTransactionDetails";
		}catch(IOException e){
			model.put("message",e.getMessage());	
			model.put("UploadFileDetails", transactionParamDTO);
			e.printStackTrace();
			return "uploadTransactionDetails";
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);	
			model.put("UploadFileDetails", transactionParamDTO);
			e.printStackTrace();
			return "uploadTransactionDetails";
		}finally{
			pageLogger(request,response,"Upload Bulk Payment");
		}

		return "uploadTransactionDetails";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportToPdfMIS.htm")
	public void exportToPDFMIS(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		int pageNumber=1;
		List results=null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		try{	
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {


				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}
			if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
				BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
				if (businessPartner != null) {
					txnSummaryDTO.setSuperAgentCode(businessPartner.getCode());
				}
			}
			Page page=transactionService.searchTxnSummary(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();

			DecimalFormat format = new DecimalFormat("#0.00");
			
			for(int i=0;i<results.size();i++) {
				
				HashMap<String, Object> map = (HashMap<String, Object>)results.get(i);
				for (Map.Entry<String, Object> entry : map.entrySet()) {					
						if(entry.getKey().equals("Amount")) {
							String amount = format.format(entry.getValue());
							amount = amount.equals(".00") == true ? "0.00" : amount;							
							entry.setValue(amount);							
						}
						if(entry.getKey().equals("SC")) {
							if(entry.getValue()!=null) {
								String serviceCharge = format.format(map.get("SC"));
								serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
								entry.setValue(serviceCharge);							
							}else
								entry.setValue("");							
						}
				}				
				results.set(i, map);
			}			
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}

		generatePDFReport(EOTConstants.JASPER_MIS_JRXML_NAME, EOTConstants.MIS_REPORT_NAME, results, model, request, response);
	}
	
	@RequestMapping("/exportToXlsMIS.htm")
	public void exportToXlsMIS(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	
		int pageNumber=1;
		List results=null;
		HSSFWorkbook wb = null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		try{	
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {


				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}
			if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
				BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
				if (businessPartner != null) {
					txnSummaryDTO.setSuperAgentCode(businessPartner.getCode());
				}
			}
			Page page=transactionService.searchTxnSummary(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetFromListForMIS(results, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.MIS_DETAILS_PAGE_HEADER);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.MIS_REPORT_NAME
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
	
	}
	
	
	@RequestMapping("/showTxnBusinessPartner.htm")
	public String showTxnBusinessPartner(ModelMap model,BusinessPartnerDTO businessPartnerDTO,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

		int pageNumber=1;
		try{			
			if(StringUtils.isBlank(businessPartnerDTO.getFromDate()) && StringUtils.isBlank(businessPartnerDTO.getToDate())) {
				String toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    String fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			    businessPartnerDTO.setFromDate(fromDate);
			    businessPartnerDTO.setToDate(toDate);
			}
			Page page=transactionService.searchTxnSummaryForBusinessPartner(businessPartnerDTO, pageNumber, localeResolver.resolveLocale(request).toString());
	//		page.requestPage="showTxnBusinessPartner.htm";
			model.put("page",page);				
		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("name", businessPartnerDTO.getName());				
			model.put("contactNumber",businessPartnerDTO.getContactNumber());
			model.put("fromDate",businessPartnerDTO.getFromDate());
			model.put("toDate",businessPartnerDTO.getToDate());
			model.put("transactionId",businessPartnerDTO.getTransactionId());
			model.put("transactionType",businessPartnerDTO.getTransactionType());
			model.put("refTransactionId",businessPartnerDTO.getRefTransactionId());
			model.put("partnerCode",businessPartnerDTO.getPartnerCode());
			//model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));			
			model.put("BusinessPartnerDTO",businessPartnerDTO );
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			//Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			//model.put("bankList", webUserService.getBanksByCountry(countryId));
			session.setAttribute("businessPartnerDTO", businessPartnerDTO);
			//pageLogger(request,response,"TransactionSummary");
		}
		return "viewBusinessPartnersCommission";
	}
	

	/**
	 * Export to PDF business partners Commission.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @param session the session
	 */
	@RequestMapping("/exportToPDFBusinessPartnersCommission.htm")
	public void exportToPDFBusinessPartners(Map<String, Object> model,BusinessPartnerDTO businessPartnerDTO,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	
		List list=null;
		int pageNumber=1;
		try{
			if(StringUtils.isBlank(businessPartnerDTO.getFromDate()) && StringUtils.isBlank(businessPartnerDTO.getToDate())) {
				String toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    String fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			    businessPartnerDTO.setFromDate(fromDate);
			    businessPartnerDTO.setToDate(toDate);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String userName = auth.getName() ;
			WebUser webUser=customerService.getUser(userName);
			Page page=transactionService.searchTxnSummaryForBusinessPartner(businessPartnerDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage="showTxnBusinessPartner.htm";
			model.put("page",page);				
			list = page.getResults();
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			list = page.getResults();

			model.put("userName", webUser.getUserName());
			model.put("date", dt1);

		}catch(Exception e){
			e.printStackTrace();
		}
			generatePDFReport(EOTConstants.JASPER_BUSINESS_PARTNER_COMMISSION_JRXML_NAME, EOTConstants.BUSINESS_PARTNER_COMMISSION_REPORT_NAME, list, model, request, response);
	}

	
	/**
	 * Export excel.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @param session the session
	 */
	@SuppressWarnings({ "unchecked", "all" })
	@RequestMapping("/businessPartnerCommissionExcelReport.htm")
	public void exportExcel(Map<String, Object> model,BusinessPartnerDTO businessPartnerDTO,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		
		HSSFWorkbook wb = null;
		List<Map<String,Object>> results=null;

		List<Customer> cust=  new ArrayList<Customer>();
		List<BusinessPartner> businessPartnerNames= new ArrayList<BusinessPartner>();
		//List<com.eot.entity.Transaction> transaction=new ArrayList<com.eot.entity.Transaction>();
		List<com.eot.entity.CommissionReport> reports = new ArrayList<com.eot.entity.CommissionReport>();
		int pageNumber=1;
		try{
			if(StringUtils.isBlank(businessPartnerDTO.getFromDate()) && StringUtils.isBlank(businessPartnerDTO.getToDate())) {
				String toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    String fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			    businessPartnerDTO.setFromDate(fromDate);
			    businessPartnerDTO.setToDate(toDate);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String userName = auth.getName() ;
			WebUser webUser=customerService.getUser(userName);
			Page page=transactionService.searchTxnSummaryForBusinessPartner(businessPartnerDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage="showTxnBusinessPartner.htm";
			model.put("page",page);				
			results = page.getResults();
			
			/*for(int i=0;i<results.size();i++) {
				BusinessPartner businessPartner = new BusinessPartner();
				Customer customer = new Customer();
				com.eot.entity.Transaction transaction1 = new com.eot.entity.Transaction();
				TransactionType transactionType = new TransactionType();
			Map<String, Object> m1 = results.get(i);
			for (Map.Entry<String, Object> entry : m1.entrySet()) {
				if(entry.getKey().equals("name"))
					businessPartner.setName(entry.getValue().toString());
				else if(entry.getKey().equals("TransactionID")) 				
					transaction1.setTransactionId(((BigInteger)entry.getValue()).longValue());
				else if(entry.getKey().equals("transactionType")) { 				
					transactionType.setTransactionType((Integer) entry.getValue());
					transaction1.setTransactionType(transactionType);
				}else if(entry.getKey().equals("amount"))
					transaction1.setAmount((double) entry.getValue());
				else if(entry.getKey().equals("mobileNumber"))					
					customer.setMobileNumber(entry.getValue().toString());
				else if(entry.getKey().equals("transactionTime"))	
					transaction1.setTransactionDate((Date) entry.getValue());
			}
			transaction.add(transaction1);
			businessPartnerNames.add(businessPartner);
			cust.add(customer);
		}*/
			
			for(int i=0;i<results.size();i++) {
				BusinessPartner businessPartner = new BusinessPartner();
				Customer customer = new Customer();
				//com.eot.entity.Transaction transaction1 = new com.eot.entity.Transaction();
				com.eot.entity.CommissionReport report = new CommissionReport();
				TransactionType transactionType = new TransactionType();
			Map<String, Object> m1 = results.get(i);
			for (Map.Entry<String, Object> entry : m1.entrySet()) {
				if(entry.getKey().equals("Status")){report.setStatus(((Integer) entry.getValue()));}
				else if(entry.getKey().equals("TransactionID")){report.setTransactionId(((BigInteger)entry.getValue()).longValue());}
				else if(entry.getKey().equals("TransactionType")){report.setTransactionType(entry.getValue().toString());}
				else if(entry.getKey().equals("CommissionAmount")){report.setCommissionAmount((Double)entry.getValue());}
				else if(entry.getKey().equals("ServiceCharge")){report.setServiceCharge((Double)entry.getValue());}
				else if(entry.getKey().equals("RefTransactionID")){report.setRefTransactionId(((BigInteger)entry.getValue()).longValue());}
				else if(entry.getKey().equals("ServiceCharge")){report.setServiceCharge((Double)entry.getValue());}
				else if(entry.getKey().equals("PartnerName")){report.setPartnerName(entry.getValue().toString());}
				else if(entry.getKey().equals("PartnerCode")){
					if(entry.getValue()!=null)
					{
						report.setPartnerCode(entry.getValue().toString());
					}
					else{report.setPartnerCode("");}}
				else if(entry.getKey().equals("TransactionDate")){report.setTransactionDate((Date)entry.getValue());}
				
			}
			reports.add(report);
			businessPartnerNames.add(businessPartner);
			cust.add(customer);
		}
			
			
			wb = wrapper.createSpreadSheetFromListForBusinessPartnerCommission(results,reports,businessPartnerNames,cust,localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.BUSINESS_PARTNER_COMMISSION_DETAILS_PAGE_HEADER,null);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.BUSINESS_PARTNER_COMMISSION_REPORT_NAME
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		}catch(EOTException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}

	}
	
	@RequestMapping("/getTxnSummaryDashBoard.htm")
	public String getTransactionSummary(ModelMap model,DashboardDTO dashboardDTO,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName() ;
		WebUser webUser=customerService.getUser(userName);
		Integer bankId=null;
		if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {	
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if(teller == null){
				try {
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				} catch (EOTException e) {					
					e.printStackTrace();
				}
			}
		 bankId = teller.getBank().getBankId();
		}
		dashboardDTO.setWebUser(webUser);
		DashboardDTO dashBoardDTO = dashBoardService.getTransactionSummary(dashboardDTO);
		DashboardDTO comissionShareDashBoardDTO = dashBoardService.getCommmissionSahre(dashboardDTO);
		dashBoardDTO.setPartnerId(dashboardDTO.getPartnerId());
		dashBoardDTO.setPartnerType(dashboardDTO.getPartnerType());
		try {
			model.put("partners", dashBoardService.loadBusinessPartnerByType(dashboardDTO.getPartnerType()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.put("dashBoardDTO", dashBoardDTO);
		model.put("comissionShareDashBoardDTO", comissionShareDashBoardDTO);
		return "txnSummaryDashBoard";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getAccBalanceByUserId.htm",method = RequestMethod.POST)
	public @ResponseBody Double getAccBalanceByUserId(@RequestParam String balanceId, ModelMap model,DashboardDTO dashboardDTO,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName() ;
		WebUser webUser=customerService.getUser(userName);
		dashboardDTO.setWebUser(webUser);
		JSONObject json = null;
	//	Double partnerBalance = dashBoardService.loadAccBalaceByUserId(userName);
		List<Map<String, Object>> list = dashBoardService.loadAccBalaceByUserId(webUser.getUserName(),webUser.getWebUserRole().getRoleId());
		Double floatBalance = 0.0;
		Double commissionBalance =0.0;
		Map<Integer,Double> m = new HashMap<Integer,Double>();
		if(CollectionUtils.isNotEmpty(list)) {
		for(Map<String, Object> map: list) {
			if(null != map.get("aliasType"))
				m.put((Integer)map.get("aliasType"),(Double)map.get("CurrentBalance"));
			else
				m.put(1,(Double)map.get("CurrentBalance"));
		}
		floatBalance = m.get(EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT);
		commissionBalance =m.get(EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);
		}
		if(balanceId.equals(EOTConstants.REFRESH_BUTTON_FLOAT_BALANCE)) {
			return floatBalance;
		}else if(balanceId.equals(EOTConstants.REFRESH_BUTTON_COMMISSION_BALANCE) && list.size()>1){
			return commissionBalance;
		}
		/*json = new JSONObject();
		json.put("partnerBalance", partnerBalance);*/
		return floatBalance;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/showTxnSummaryDataCustomerRegistration.htm", method=RequestMethod.POST)
	public @ResponseBody String showTxnSummaryDataCustomerRegistration(TxnSummaryDTO txnSummaryDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		String serialIndex = request.getParameter("index");
		int index=0;
		if (serialIndex !=null && !serialIndex.equals("")) {
			index = Integer.parseInt(serialIndex);
		}
		
		Integer pageNumber = null;//request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		String locale = localeResolver.resolveLocale(request).toString().substring(0, 2);
		Map<Integer, TransactionType> txntypeMap = transactionService.getTxntypeMap(locale);
		
		if( request.getParameter("pageNumber") != null ){
			pageNumber = new Integer(request.getParameter("pageNumber"));
			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
		} else {

			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
				txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
			}
		//	txnSummaryDTO.setBankId(txnSummaryDTO.getBank()); 
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
		if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
			BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
			if (businessPartner != null) {
				//txnSummaryDTO.setPartnerId(businessPartner.getId().toString());
				//txnSummaryDTO.setPartnerType(businessPartner.getPartnerType().toString());
				txnSummaryDTO.setAccountNumber(businessPartner.getAccountNumber());
			}
		}
		if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
			BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
			if (businessPartner != null) {
				txnSummaryDTO.setSuperAgentCode(businessPartner.getCode());
			}
		}
		
		txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
		txnSummaryDTO.setSortBy(sortBy);
		JSONObject json = null;
		try{			
			//txnSummaryDTO=new TxnSummaryDTO();
			response.setContentType("application/json");
			json = new JSONObject();
			Page page=transactionService.searchTxnCustomerRegistration(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			
			json.put("recordsTotal", page!=null ? page.getTotalCount() : 0);
			json.put("recordsFiltered", page!=null ? page.getTotalCount() : 0);
			JSONArray array = new JSONArray();
			
			
			if (page !=null && page.getResults()!=null) {
				
				for (HashMap map : (List<HashMap>) page.getResults()) {
					JSONObject item = new JSONObject();
					
					item.put("SerNo", ++index);
					item.put("TransactionID", map.get("TransactionID"));
					DecimalFormat format = new DecimalFormat("#0.00");
					String amount = null !=map.get("amount")? format.format(map.get("amount")):"0.00";
					amount = amount.equals(".00") == true ? "0.00" : amount;
					item.put("amount", amount);
					item.put("BusinessName", map.get("BusinessName"));
					item.put("FirstName", map.get("FirstName").toString().concat(" " + map.get("LastName").toString()));
					item.put("MobileNumber", map.get("MobileNumber"));
					item.put("AgentName", map.get("AgentName"));
					item.put("Agent_Code", map.get("Agent_Code"));
					/*item.put("BankName", map.get("BankName"));
					item.put("Location", map.get("Location"));
					item.put("Agent_Code", map.get("AgentCode"));*/
					item.put("AgentMobileNumber", map.get("AgentMobileNumber"));
					item.put("SuperAgentName", map.get("SuperAgentName"));
					item.put("SuperAgentCode", map.get("SuperAgentCode"));
					

					
					
					Set<TransactionTypesDesc> transactionTypesDescs = txntypeMap.get(map.get("TransactionType")).getTransactionTypesDescs();
					String txnType="";
					for (TransactionTypesDesc transactionTypesDesc : transactionTypesDescs) {
						
						if(transactionTypesDesc.getComp_id().getLocale().equals(locale)) {
							txnType=transactionTypesDesc.getDescription();
							break;
						}
					}
					item.put("TransactionType", txnType);
					
					/*if (map.get("TransactionType").equals(0))
						item.put("TransactionType", "Currency Converter");
					else if (map.get("TransactionType").equals(5))
						item.put("TransactionType", "Login");
					else if (map.get("TransactionType").equals(95))
						item.put("TransactionType", "Deposit");
					else if (map.get("TransactionType").equals(99))
						item.put("TransactionType", "Withdrawl");
					else if (map.get("TransactionType").equals(30))
						item.put("TransactionType", "Balance Enquiry");
					else if (map.get("TransactionType").equals(35))
						item.put("TransactionType", "Mini Statement");
					else if (map.get("TransactionType").equals(98))
						item.put("TransactionType", "Transaction Statement");
					else if (map.get("TransactionType").equals(10))
						item.put("TransactionType", "Activation");
					else if (map.get("TransactionType").equals(20))
						item.put("TransactionType", "Transaction Pin Change");
					else if (map.get("TransactionType").equals(55))
						item.put("TransactionType", "Transfer Direct");
					else if (map.get("TransactionType").equals(70))
						item.put("TransactionType", "Reset Pin");
					else if (map.get("TransactionType").equals(75))
						item.put("TransactionType", "Reset Transaction Pin");
					else if (map.get("TransactionType").equals(90))
						item.put("TransactionType", "Sale");
					else if (map.get("TransactionType").equals(80))
						item.put("TransactionType", "Buy Airtime");
					else if (map.get("TransactionType").equals(65))
						item.put("TransactionType", "Re-Activation");
					else if (map.get("TransactionType").equals(82))
						item.put("TransactionType", "Bill Payment");
					else if (map.get("TransactionType").equals(83))
						item.put("TransactionType", "SMS Cash");
					else if (map.get("TransactionType").equals(15))
						item.put("TransactionType", "Login Pin Change");
					else if (map.get("TransactionType").equals(60))
						item.put("TransactionType", "Void");
					else if (map.get("TransactionType").equals(61))
						item.put("TransactionType", "Reversal");
					else if (map.get("TransactionType").equals(37))
						item.put("TransactionType", "Add Bank Account");
					else if (map.get("TransactionType").equals(40))
						item.put("TransactionType", "Add Card");
					else if (map.get("TransactionType").equals(39))
						item.put("TransactionType", "Cheque Status");

					else if (map.get("TransactionType").equals(104))
						item.put("TransactionType", "Currency Converter");
					else if (map.get("TransactionType").equals(103))
						item.put("TransactionType", "Locate Us");
					else if (map.get("TransactionType").equals(102))
						item.put("TransactionType", "Exchange Rate");
					else if (map.get("TransactionType").equals(101))
						item.put("TransactionType", "Customer Registration");
					else if (map.get("TransactionType").equals(100))
						item.put("TransactionType", "Encash");

					else if (map.get("TransactionType").equals(115))
						item.put("TransactionType", "Cash Deposit");
					else if (map.get("TransactionType").equals(116))
						item.put("TransactionType", "Cash Withdrawal");
					else if (map.get("TransactionType").equals(117))
						item.put("TransactionType", "Load Withdrawal");
					else if (map.get("TransactionType").equals(118))
						item.put("TransactionType", "Withdrawal Rejected");
					else if (map.get("TransactionType").equals(119))
						item.put("TransactionType", "Approve Withdrawal");
					else if (map.get("TransactionType").equals(121))
						item.put("TransactionType", "Limit");
					else if (map.get("TransactionType").equals(120))
						item.put("TransactionType", "Commission Share");
					else if (map.get("TransactionType").equals(122))
						item.put("TransactionType", "Receive Recipient");
					else if (map.get("TransactionType").equals(123))
						item.put("TransactionType", "Fetch Customer");
					else if (map.get("TransactionType").equals(124))
						item.put("TransactionType", "Update KYC");
					else if (map.get("TransactionType").equals(125))
						item.put("TransactionType", "Show SMS cash");
					else if (map.get("TransactionType").equals(126))
						item.put("TransactionType", "SMS Cash Receiv");
					else if (map.get("TransactionType").equals(127))
						item.put("TransactionType", "Validate m-GURUSH Pay");
					else if (map.get("TransactionType").equals(128))
						item.put("TransactionType", "m-GURUSH Pay");
					else if (map.get("TransactionType").equals(133))
						item.put("TransactionType", "Float Management");
					else if (map.get("TransactionType").equals(108))
						item.put("TransactionType", "Customer Login");
					else if (map.get("TransactionType").equals(137))
						item.put("TransactionType", "Cash In");
					else if (map.get("TransactionType").equals(138))
						item.put("TransactionType", "Cash Out");
					else if (map.get("TransactionType").equals(139))
						item.put("TransactionType", "Set Up Application");
					else if (map.get("TransactionType").equals(140))
						item.put("TransactionType", "Merchant Payout");
					else if (map.get("TransactionType").equals(141))
						item.put("TransactionType", "Bulk Payment");
					else if (map.get("TransactionType").equals(143))
						item.put("TransactionType", "Transfer eMoney");
					else {
						item.put("TransactionType", map.get("TransactionType"));
					}*/

				//	item.put("TransactionDate", DateUtil.formatDateWithHyphen(map.get("TransactionDate").toString()));
					String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
					item.put("TransactionDate", TransactionDate);
					
					/*item.put("Status", map.get("status").equals(2000) ? "Success" : "Failed");
					if (map.get("status").equals(2000) == true)
						item.put("status", "Success");
					else {
						if (map.get("status").equals(2001))
							item.put("status", EOTConstants.FATAL_ERROR);
						else if (map.get("status").equals(2002))
							item.put("status", EOTConstants.MISSING_PARAMETERS_ERROR);
						else if (map.get("status").equals(2003))
							item.put("status", EOTConstants.INVALID_PARAMETERS_ERROR);
						else if (map.get("status").equals(2004))
							item.put("status", EOTConstants.OPERATION_NOT_SUPPORTED_ERROR);
						else if (map.get("status").equals(2005))
							item.put("status", EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR);
						else if (map.get("status").equals(2006))
							item.put("status", EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR);
						else if (map.get("status").equals(2020))
							item.put("status", EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2021))
							item.put("status", EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2022))
							item.put("status", EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2023))
							item.put("status", EOTConstants.NO_VOUCHER_AVAILABLE_ERROR);
						else if (map.get("status").equals(2024))
							item.put("status", EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR);
						else if (map.get("status").equals(2025))
							item.put("status", EOTConstants.TXN_RULE_NOT_DEFINED_ERROR);
						else if (map.get("status").equals(2026))
							item.put("status", EOTConstants.TXN_LIMIT_EXCEEDED_ERROR);
						else if (map.get("status").equals(2027))
							item.put("status", EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR);
						else if (map.get("status").equals(2028))
							item.put("status", EOTConstants.TXN_NUM_EXCEEDED_ERROR);
						else if (map.get("status").equals(2029))
							item.put("status", EOTConstants.INVALID_TXN_RULE_ERROR);
						else if (map.get("status").equals(2030))
							item.put("status", EOTConstants.NO_BALANCE_IN_WALLET_ERROR);
						else if (map.get("status").equals(2031))
							item.put("status", EOTConstants.TXN_DECLINED_FROM_HPS_ERROR);
						else if (map.get("status").equals(2032))
							item.put("status", EOTConstants.INVALID_CH_POOL_ERROR);
						else if (map.get("status").equals(2033))
							item.put("status", EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR);
						else if (map.get("status").equals(2034))
							item.put("status", EOTConstants.SETTLED_TRANSACTION_ERROR);
						else if (map.get("status").equals(2035))
							item.put("status", EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR);
						else if (map.get("status").equals(2036))
							item.put("status", EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR);
						else if (map.get("status").equals(2037))
							item.put("status", EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR);
						else if (map.get("status").equals(2038))
							item.put("status", EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR);
						else if (map.get("status").equals(2039))
							item.put("status", EOTConstants.INVALID_SIGNATURE_SIZE_ERROR);
						else if (map.get("status").equals(2040))
							item.put("status", EOTConstants.INVALID_IDPROOF_SIZE_ERROR);
						else if (map.get("status").equals(2041))
							item.put("status", EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR);
					}

					if (map.get("SC") != null && !map.get("TransactionType").equals(128)) {
						String serviceCharge = format.format(map.get("SC"));
						serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
						item.put("SC", serviceCharge);
					} else
						item.put("SC", "");
					//item.put("SC",map.get("SC")!=null?map.get("SC"):"");
					item.put("RequestChannel", map.get("requestChannel"));*/
					array.add(item);
				} 
			}
			json.put("data", array);
			//System.out.println(json);
		}catch(EOTException e){
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			json.put("recordsTotal", page.getTotalCount());
			json.put("recordsFiltered", page.getTotalCount());
			JSONArray array = new JSONArray();
			
			for (HashMap map : (List<HashMap>)page.getResults()) {
				JSONObject item = new JSONObject();
				item.put("TransactionID", map.get("TransactionID"));
				DecimalFormat format = new DecimalFormat("#0.00");
				//String amount = format.format(map.get("amount"));
				String amount = null !=map.get("amount")? format.format(map.get("amount")):"0.00";
				amount = amount.equals(".00") == true ? "0.00" : amount;
				item.put("amount", amount);
				item.put("BusinessName", map.get("BusinessName"));
				item.put("FirstName", map.get("FirstName").toString().concat(" " + map.get("LastName").toString()));
				item.put("MobileNumber", map.get("MobileNumber"));
				item.put("AgentName", map.get("AgentName"));
				item.put("Agent_Code", map.get("Agent_Code"));
				/*item.put("BankName", map.get("BankName"));
				item.put("Location", map.get("Location"));
				item.put("Agent_Code", map.get("AgentCode"));*/
				item.put("AgentMobileNumber", map.get("AgentMobileNumber"));
				item.put("SuperAgentName", map.get("SuperAgentName"));
				item.put("SuperAgentCode", map.get("SuperAgentCode"));
				
				Set<TransactionTypesDesc> transactionTypesDescs = txntypeMap.get(map.get("TransactionType")).getTransactionTypesDescs();
				String txnType="";
				for (TransactionTypesDesc transactionTypesDesc : transactionTypesDescs) {
					
					if(transactionTypesDesc.getComp_id().getLocale().equals(locale)) {
						txnType=transactionTypesDesc.getDescription();
						break;
					}
				}
				item.put("TransactionType", txnType);
				
				/*if (map.get("TransactionType").equals(0))
					item.put("TransactionType", "Currency Converter");
				else if (map.get("TransactionType").equals(5))
					item.put("TransactionType", "Login");
				else if (map.get("TransactionType").equals(95))
					item.put("TransactionType", "Deposit");
				else if (map.get("TransactionType").equals(99))
					item.put("TransactionType", "Withdrawl");
				else if (map.get("TransactionType").equals(30))
					item.put("TransactionType", "Balance Enquiry");
				else if (map.get("TransactionType").equals(35))
					item.put("TransactionType", "Mini Statement");
				else if (map.get("TransactionType").equals(98))
					item.put("TransactionType", "Transaction Statement");
				else if (map.get("TransactionType").equals(10))
					item.put("TransactionType", "Activation");
				else if (map.get("TransactionType").equals(20))
					item.put("TransactionType", "Transaction Pin Change");
				else if (map.get("TransactionType").equals(55))
					item.put("TransactionType", "Transfer Direct");
				else if (map.get("TransactionType").equals(70))
					item.put("TransactionType", "Reset Pin");
				else if (map.get("TransactionType").equals(75))
					item.put("TransactionType", "Reset Transaction Pin");
				else if (map.get("TransactionType").equals(90))
					item.put("TransactionType", "Sale");
				else if (map.get("TransactionType").equals(80))
					item.put("TransactionType", "Buy Airtime");
				else if (map.get("TransactionType").equals(65))
					item.put("TransactionType", "Re-Activation");
				else if (map.get("TransactionType").equals(82))
					item.put("TransactionType", "Bill Payment");
				else if (map.get("TransactionType").equals(83))
					item.put("TransactionType", "SMS Cash");
				else if (map.get("TransactionType").equals(15))
					item.put("TransactionType", "Login Pin Change");
				else if (map.get("TransactionType").equals(60))
					item.put("TransactionType", "Void");
				else if (map.get("TransactionType").equals(61))
					item.put("TransactionType", "Reversal");
				else if (map.get("TransactionType").equals(37))
					item.put("TransactionType", "Add Bank Account");
				else if (map.get("TransactionType").equals(40))
					item.put("TransactionType", "Add Card");
				else if (map.get("TransactionType").equals(39))
					item.put("TransactionType", "Cheque Status");

				else if (map.get("TransactionType").equals(104))
					item.put("TransactionType", "Currency Converter");
				else if (map.get("TransactionType").equals(103))
					item.put("TransactionType", "Locate Us");
				else if (map.get("TransactionType").equals(102))
					item.put("TransactionType", "Exchange Rate");
				else if (map.get("TransactionType").equals(101))
					item.put("TransactionType", "Customer Registration");
				else if (map.get("TransactionType").equals(100))
					item.put("TransactionType", "Encash");

				else if (map.get("TransactionType").equals(115))
					item.put("TransactionType", "Cash Deposit");
				else if (map.get("TransactionType").equals(116))
					item.put("TransactionType", "Cash Withdrawal");
				else if (map.get("TransactionType").equals(117))
					item.put("TransactionType", "Load Withdrawal");
				else if (map.get("TransactionType").equals(118))
					item.put("TransactionType", "Withdrawal Rejected");
				else if (map.get("TransactionType").equals(119))
					item.put("TransactionType", "Approve Withdrawal");
				else if (map.get("TransactionType").equals(121))
					item.put("TransactionType", "Limit");
				else if (map.get("TransactionType").equals(120))
					item.put("TransactionType", "Commission Share");
				else if (map.get("TransactionType").equals(122))
					item.put("TransactionType", "Receive Recipient");
				else if (map.get("TransactionType").equals(123))
					item.put("TransactionType", "Fetch Customer");
				else if (map.get("TransactionType").equals(124))
					item.put("TransactionType", "Update KYC");
				else if (map.get("TransactionType").equals(125))
					item.put("TransactionType", "Show SMS cash");
				else if (map.get("TransactionType").equals(126))
					item.put("TransactionType", "SMS Cash Receiv");
				else if (map.get("TransactionType").equals(127))
					item.put("TransactionType", "Validate m-GURUSH Pay");
				else if (map.get("TransactionType").equals(128))
					item.put("TransactionType", "m-GURUSH Pay");
				else if (map.get("TransactionType").equals(133))
					item.put("TransactionType", "Float Management");
				else if (map.get("TransactionType").equals(108))
					item.put("TransactionType", "Customer Login");
				else if (map.get("TransactionType").equals(137))
					item.put("TransactionType", "Cash In");
				else if (map.get("TransactionType").equals(138))
					item.put("TransactionType", "Cash Out");
				else if (map.get("TransactionType").equals(139))
					item.put("TransactionType", "Set Up Application");
				else if (map.get("TransactionType").equals(140))
					item.put("TransactionType", "Merchant Payout");
				else if (map.get("TransactionType").equals(141))
					item.put("TransactionType", "Bulk Payment");
				else if (map.get("TransactionType").equals(143))
					item.put("TransactionType", "Transfer eMoney");
				else {
					item.put("TransactionType", map.get("TransactionType"));
				}*/

			//	item.put("TransactionDate", DateUtil.formatDateWithHyphen(map.get("TransactionDate").toString()));
				String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
				item.put("TransactionDate", TransactionDate);
				
				/*item.put("Status", map.get("status").equals(2000) ? "Success" : "Failed");
				if (map.get("status").equals(2000) == true)
					item.put("status", "Success");
				else {
					if (map.get("status").equals(2001))
						item.put("status", EOTConstants.FATAL_ERROR);
					else if (map.get("status").equals(2002))
						item.put("status", EOTConstants.MISSING_PARAMETERS_ERROR);
					else if (map.get("status").equals(2003))
						item.put("status", EOTConstants.INVALID_PARAMETERS_ERROR);
					else if (map.get("status").equals(2004))
						item.put("status", EOTConstants.OPERATION_NOT_SUPPORTED_ERROR);
					else if (map.get("status").equals(2005))
						item.put("status", EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR);
					else if (map.get("status").equals(2006))
						item.put("status", EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR);
					else if (map.get("status").equals(2020))
						item.put("status", EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR);
					else if (map.get("status").equals(2021))
						item.put("status", EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR);
					else if (map.get("status").equals(2022))
						item.put("status", EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR);
					else if (map.get("status").equals(2023))
						item.put("status", EOTConstants.NO_VOUCHER_AVAILABLE_ERROR);
					else if (map.get("status").equals(2024))
						item.put("status", EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR);
					else if (map.get("status").equals(2025))
						item.put("status", EOTConstants.TXN_RULE_NOT_DEFINED_ERROR);
					else if (map.get("status").equals(2026))
						item.put("status", EOTConstants.TXN_LIMIT_EXCEEDED_ERROR);
					else if (map.get("status").equals(2027))
						item.put("status", EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR);
					else if (map.get("status").equals(2028))
						item.put("status", EOTConstants.TXN_NUM_EXCEEDED_ERROR);
					else if (map.get("status").equals(2029))
						item.put("status", EOTConstants.INVALID_TXN_RULE_ERROR);
					else if (map.get("status").equals(2030))
						item.put("status", EOTConstants.NO_BALANCE_IN_WALLET_ERROR);
					else if (map.get("status").equals(2031))
						item.put("status", EOTConstants.TXN_DECLINED_FROM_HPS_ERROR);
					else if (map.get("status").equals(2032))
						item.put("status", EOTConstants.INVALID_CH_POOL_ERROR);
					else if (map.get("status").equals(2033))
						item.put("status", EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR);
					else if (map.get("status").equals(2034))
						item.put("status", EOTConstants.SETTLED_TRANSACTION_ERROR);
					else if (map.get("status").equals(2035))
						item.put("status", EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR);
					else if (map.get("status").equals(2036))
						item.put("status", EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR);
					else if (map.get("status").equals(2037))
						item.put("status", EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR);
					else if (map.get("status").equals(2038))
						item.put("status", EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR);
					else if (map.get("status").equals(2039))
						item.put("status", EOTConstants.INVALID_SIGNATURE_SIZE_ERROR);
					else if (map.get("status").equals(2040))
						item.put("status", EOTConstants.INVALID_IDPROOF_SIZE_ERROR);
					else if (map.get("status").equals(2041))
						item.put("status", EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR);
				}

				if (map.get("SC") != null && !map.get("TransactionType").equals(128)) {
					String serviceCharge = format.format(map.get("SC"));
					serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
					item.put("SC", serviceCharge);
				} else
					item.put("SC", "");
				//item.put("SC",map.get("SC")!=null?map.get("SC"):"");
				item.put("RequestChannel", map.get("requestChannel"));*/
				array.add(item);
			}
			json.put("data", array);
			return json.toString();
		}catch(Exception e){
			PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		return json.toString();
	}
	
	@RequestMapping("/initiateAdjustmentTxn.htm")
	public String initiateAdjustmentTxn(ModelMap model,HttpServletRequest request,HttpServletResponse response){

		String transactionId=request.getParameter("txnType");
		String adjustedAmount = request.getParameter("adjustedAmount");
		String adjustedFee = request.getParameter("adjustedFee"); 
		String transactionType = request.getParameter("adjustmentTxnType"); 
		String comment = request.getParameter("comment"); 
		String customerName = "";
		String mobileNumber = "";
		String txnDate = "";
		String amount = "";
		String txnType = "";
		String fromDate = "";
		String toDate = "";
		try{

			customerName = request.getParameter("customerName");
			mobileNumber = request.getParameter("mobileNumber");
			txnDate = request.getParameter("txnDate");
			amount = request.getParameter("amount");
			txnType = request.getParameter("txnType");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");	
			transactionService.initiateAdjustmentTransaction(transactionId,adjustedAmount,adjustedFee,transactionType,comment);
			model.put("message", "INITIATE_ADJUSTMENT_SUCCESS");

			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page=transactionService.getAdjustmentTransactions(customerName, mobileNumber, amount,txnDate, txnType,fromDate,toDate,pageNumber);
			page.requestPage = "searchAdjustmentTransactions.htm";
			model.put("page",page);
			
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			
			model.put("transactionId",transactionId);
			model.put("adjustedAmount",adjustedAmount);
			model.put("adjustedFee",adjustedFee);
			model.put("comment",comment);
			model.put("transactionType",transactionType);
		}	   
		return "adjustmentForm";
		/*return "redirect:/showAdjustmentForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
	}
	
	/**
	 * vineeth
	 * rounding the double values to 2 decimal places
	 * @param value
	 * @param places
	 * @return double value
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	@RequestMapping("/getBusinessPartnerDetails.htm")
	public String getBusinessPartnerDetails(HttpServletRequest request,ModelMap model,HttpSession session,HttpServletResponse response){

		try {
			AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
			System.out.println(Integer.parseInt(request.getParameter("transactionType")));
	//		BusinessPartner businessPartner = businessPartnerService.loadBusinessPartnerByCode(request.getParameter("businessPartnerCode"));
			
			Integer transctionType = Integer.parseInt(request.getParameter("transactionType"));
			String businessPartnerCode = request.getParameter("businessPartnerCode");
			String mobileNumber = request.getParameter("mobileNumber");
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			
			if(null != mobileNumber && !mobileNumber.equals(""))
				mobileNumber = country.getIsdCode()+ mobileNumber;
			
			if(null != businessPartnerCode && !businessPartnerCode.equals(""))
				accountDetailsDTO = transactionService.getBusinessPartnerAccountDetails(businessPartnerCode,transctionType);
			else
				accountDetailsDTO = transactionService.getAccountDetails(mobileNumber,transctionType,true);
			accountDetailsDTO.setTransactionType(Integer.parseInt(request.getParameter("transactionType")));

			model.put("accountDetailsDTO", accountDetailsDTO );
			session.setAttribute("accountDetailsDTO", accountDetailsDTO);

			return "transactionDetailsForm";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "selectBusinessPartnerTransactionForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "selectBusinessPartnerTransactionForm";
		}finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"AccountDetails");
		}

	}
	
	@RequestMapping("/businessPartnerTransaction.htm")
	public String selectBuisnessPartnerTransactionForm(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("txnList",transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
	//		model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
/*			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());*/
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"BusinessPartnerTransactionServices");
		}
		return "selectBusinessPartnerTransactionForm";
	}
	
	@RequestMapping("downloadBulkPaymentFailFile.htm")
	public String downloadBulkPaymentFailFile(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		List processedList = (List)session.getAttribute("processedList");
		if (CollectionUtils.isNotEmpty(processedList)) {
			HSSFWorkbook wb = wrapper.createCSVForFailedBulkUpload(processedList, localeResolver.resolveLocale(request),
					messageSource);
			try {
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + date + "_" + System.currentTimeMillis() + "_report.xls");
				OutputStream os = response.getOutputStream();
				wb.write(os);
				os.flush();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			model.put("message", "NO_FILE_PROCESSED");
		}
		model.put("transactionParamDTO", new TransactionParamDTO());
		return "uploadTransactionDetails";
	}
	
	private void writeFile(TransactionParamDTO transactionParamDTO
			, String dirName, String fileName) {
		CommonsMultipartFile txnFile = transactionParamDTO.getTxnFile();
		dirName = dirName.contains(" ") ?dirName.replace(" ", "_"):dirName;
		dirName =dirName+File.separator+EOTConstants.BULK_PAYMENT_UPLOADED;
		;
		File dir = new File (dirName);
		if(!dir.exists()) {
			dir.mkdirs();
		}	
		//Path filepath = Paths.get(dirName.toString(), txnFile.getOriginalFilename());
		Path filepath = Paths.get(dirName.toString(), fileName+".csv");
		try (OutputStream os = Files.newOutputStream(filepath)) {
	        os.write(txnFile.getBytes());
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bulkPayTxnReport.htm", method=RequestMethod.POST)
	public String showBulkPayTxnReport(TxnSummaryDTO txnSummaryDTO,  ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		//TxnSummaryDTO txnSummaryDTO = new TxnSummaryDTO();
			
			txnSummaryDTO.setTransactionType(EOTConstants.TXN_ID_BULK_PAY);
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
			//model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));			
			model.put("txnSummaryDTO",txnSummaryDTO );
			//Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			//model.put("bankList", webUserService.getBanksByCountry(countryId));
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			pageLogger(request,response,"TransactionSummary");
		
			return "bulkPayTxnReport";
	}
	
	@RequestMapping("/xlsForBulkPayTxn.htm")
	public void exportToXlSBulkPayTxn(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	
		int pageNumber=1;
		List results=null;
		HSSFWorkbook wb = null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		try{	
			pageNumber=0;
			Page page=transactionService.searchBulkPayTxnReport(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();
			page.requestPage="bulkPayTxnReport.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetForBulkPayTxn(results, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.BULK_PAY_TXN_PAGE_HEADER);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.BULK_PAY_TXN_REPORT_NAME
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
	
	}
	
	
	@RequestMapping("/cusAppCommReport.htm")
	public String viewCustomerAppReport(ModelMap model,TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

		txnSummaryDTO.setTransactionType(EOTConstants.TXN_ID_CUSTOMER_APPROVAL);
		model.put("language",localeResolver.resolveLocale(request));
		model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
		Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
		Country country = customerService.getCountry(countryId);
		model.put("mobileNumLength", country.getMobileNumberLength());
		session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		pageLogger(request,response,"CustomerRegistrationApprovalReport");
		
		return "customerAppReport";
	}
	
	@RequestMapping("/xlsCusRegReport.htm")
	public void exportCustomerRegReport(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	
		int pageNumber=1;
		List results=null;
		HSSFWorkbook wb = null;
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		try{	
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {


				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}	
			if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
				BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
				if (businessPartner != null) {
					txnSummaryDTO.setSuperAgentCode(businessPartner.getCode());
				}
			}
			txnSummaryDTO.setTransactionType(EOTConstants.TXN_ID_CUSTOMER_APPROVAL);
			txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
			txnSummaryDTO.setSortBy(sortBy);
			Page page=transactionService.searchTxnCustomerRegistration(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetForCustomerReg(results, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.CUS_REG_COMM_DETAILS_HEADER);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.CUS_REG_COMM_DETAILS_HEADER
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
	
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/pdfCusRegReport.htm")
	public void pdfExportForCustomerRegReport(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		
		int pageNumber=1;
		List results=null;
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		try{	
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {


				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}
			if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
				BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
				if (businessPartner != null) {
					txnSummaryDTO.setSuperAgentCode(businessPartner.getCode());
				}
			}
			txnSummaryDTO.setTransactionType(EOTConstants.TXN_ID_CUSTOMER_APPROVAL);
			txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
			txnSummaryDTO.setSortBy(sortBy);
			Page page=transactionService.searchTxnCustomerRegistration(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();

			DecimalFormat format = new DecimalFormat("#0.00");
			
			for(int i=0;i<results.size();i++) {
				
				HashMap<String, Object> map = (HashMap<String, Object>)results.get(i);
				for (Map.Entry<String, Object> entry : map.entrySet()) {					
						if(entry.getKey().equals("amount")) {
							String amount = format.format(entry.getValue());
							amount = amount.equals(".00") == true ? "0.00" : amount;							
							entry.setValue(amount);							
						}
						if(entry.getKey().equals("SC")) {
							if(entry.getValue()!=null && !map.get("TransactionType").toString().equals(EOTConstants.TXN_ID_PAY+"")) {
								String serviceCharge = format.format(map.get("SC"));
								serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
								entry.setValue(serviceCharge);							
							}else
								entry.setValue("");							
						}
				}				
				results.set(i, map);
			}			
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}

		generatePDFReport(EOTConstants.JASPER_CUS_REG_COMM_JRXML_NAME, EOTConstants.CUS_REG_COMM_DETAILS_HEADER, results, model, request, response);
	}
	
	@RequestMapping("/accountTransfer.htm")
	public String accountTransfer(ModelMap model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("txnList",transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
	//		model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("accountsList", transactionService.getAccountsForAccountToAccount());
/*			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
		//	model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());*/
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"BusinessPartnerTransactionServices");
		}
		return "accountTransfer";
	}
	@RequestMapping("/getAccountForAccountTransfer.htm")
	public String getAccountForAccountTransfer(HttpServletRequest request,ModelMap model,HttpSession session,HttpServletResponse response){

		try {

			System.out.println(Integer.parseInt(request.getParameter("transactionType")));
			AccountDetailsDTO accountDetailsDTO = transactionService.getAccountDetails(request.getParameter("mobileNumber"),null,false);
			accountDetailsDTO.setTransactionType(Integer.parseInt(request.getParameter("transactionType")));

			model.put("accountDetailsDTO", accountDetailsDTO );
			session.setAttribute("accountDetailsDTO", accountDetailsDTO);

			return "transactionDetailsForm";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "selectTransactionForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "selectTransactionForm";
		}finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"AccountDetails");
		}

	}
	
	@RequestMapping("/merchantReport.htm")
	public String viewMerchantAppReport(ModelMap model,TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

	//	txnSummaryDTO.setTransactionType(EOTConstants.TXN_ID_CUSTOMER_APPROVAL);
		model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
		model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));			
		model.put("language",localeResolver.resolveLocale(request));
		model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
		Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
		Country country = customerService.getCountry(countryId);
		model.put("mobileNumLength", country.getMobileNumberLength());
		session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		pageLogger(request,response,"MerchantReport");
		
		return "merchantAppReport";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/showTxnMerchantData.htm", method=RequestMethod.POST)
	public @ResponseBody String showTxnMerchantData(TxnSummaryDTO txnSummaryDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		String serialIndex = request.getParameter("index");
		session.setAttribute("sortColumn", sortColumn);
		session.setAttribute("index", serialIndex);
		session.setAttribute("sortBy", sortBy);
		int index=0;
		if (serialIndex !=null && !serialIndex.equals("")) {
			index = Integer.parseInt(serialIndex);
		}
		
		Integer pageNumber = null;//request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		String locale = localeResolver.resolveLocale(request).toString().substring(0, 2);
		Map<Integer, TransactionType> txntypeMap = transactionService.getTxntypeMap(locale);
		
		if( request.getParameter("pageNumber") != null ){
			pageNumber = new Integer(request.getParameter("pageNumber"));
			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
		} else {

			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
				txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
			}
		//	txnSummaryDTO.setBankId(txnSummaryDTO.getBank()); 
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
		
		if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
			BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
			if (businessPartner != null) {
				//txnSummaryDTO.setPartnerId(businessPartner.getId().toString());
				//txnSummaryDTO.setPartnerType(businessPartner.getPartnerType().toString());
				txnSummaryDTO.setAccountNumber(businessPartner.getAccountNumber());
			}
		}
		
		txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
		txnSummaryDTO.setSortBy(sortBy);
		JSONObject json = null;
		try{			
			//txnSummaryDTO=new TxnSummaryDTO();
			response.setContentType("application/json");
			json = new JSONObject();
			Page page=transactionService.searchTxnMerchantData(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			
			json.put("recordsTotal", page!=null ? page.getTotalCount() : 0);
			json.put("recordsFiltered", page!=null ? page.getTotalCount() : 0);
			JSONArray array = new JSONArray();
			
			
			if (page !=null && page.getResults()!=null) {
				
				for (HashMap map : (List<HashMap>) page.getResults()) {
					JSONObject item = new JSONObject();
					
					item.put("SerNo", ++index);
					item.put("TransactionID", map.get("transactionid"));
					item.put("MerchantCode", map.get("MerchantCode"));
					item.put("BusinessName", map.get("BusinessName"));
					item.put("MerchantName", map.get("MerchantName"));
					item.put("MerchantMobile", map.get("MerchantMobile"));
					item.put("customerName", map.get("customerName"));
					item.put("customerMobile", map.get("customerMobile"));
//					item.put("TransactionDate", DateUtil.formatDateWithHyphen(map.get("TransactionDate").toString()));
					String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
					item.put("TransactionDate", TransactionDate);
//					Set<TransactionTypesDesc> transactionTypesDescs = txntypeMap.get(map.get("Description")).getTransactionTypesDescs();
//					String txnType="";
//					for (TransactionTypesDesc transactionTypesDesc : transactionTypesDescs) {
//						
//						if(transactionTypesDesc.getComp_id().getLocale().equals(locale)) {
//							txnType=transactionTypesDesc.getDescription();
//							break;
//						}
//					}
					item.put("TransactionType", map.get("Description")+"");
					item.put("Status", map.get("status").equals(2000) ? "Success" : "Failed");
					if (map.get("status").equals(2000) == true)
						item.put("Description", "Success");
					else {
						if (map.get("status").equals(2001))
							item.put("Description", EOTConstants.FATAL_ERROR);
						else if (map.get("status").equals(2002))
							item.put("Description", EOTConstants.MISSING_PARAMETERS_ERROR);
						else if (map.get("status").equals(2003))
							item.put("Description", EOTConstants.INVALID_PARAMETERS_ERROR);
						else if (map.get("status").equals(2004))
							item.put("Description", EOTConstants.OPERATION_NOT_SUPPORTED_ERROR);
						else if (map.get("status").equals(2005))
							item.put("Description", EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR);
						else if (map.get("status").equals(2006))
							item.put("Description", EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR);
						else if (map.get("status").equals(2020))
							item.put("Description", EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2021))
							item.put("Description", EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2022))
							item.put("Description", EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2023))
							item.put("Description", EOTConstants.NO_VOUCHER_AVAILABLE_ERROR);
						else if (map.get("status").equals(2024))
							item.put("Description", EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR);
						else if (map.get("status").equals(2025))
							item.put("Description", EOTConstants.TXN_RULE_NOT_DEFINED_ERROR);
						else if (map.get("status").equals(2026))
							item.put("Description", EOTConstants.TXN_LIMIT_EXCEEDED_ERROR);
						else if (map.get("status").equals(2027))
							item.put("Description", EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR);
						else if (map.get("status").equals(2028))
							item.put("Description", EOTConstants.TXN_NUM_EXCEEDED_ERROR);
						else if (map.get("status").equals(2029))
							item.put("Description", EOTConstants.INVALID_TXN_RULE_ERROR);
						else if (map.get("status").equals(2030))
							item.put("Description", EOTConstants.NO_BALANCE_IN_WALLET_ERROR);
						else if (map.get("status").equals(2031))
							item.put("Description", EOTConstants.TXN_DECLINED_FROM_HPS_ERROR);
						else if (map.get("status").equals(2032))
							item.put("Description", EOTConstants.INVALID_CH_POOL_ERROR);
						else if (map.get("status").equals(2033))
							item.put("Description", EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR);
						else if (map.get("status").equals(2034))
							item.put("Description", EOTConstants.SETTLED_TRANSACTION_ERROR);
						else if (map.get("status").equals(2035))
							item.put("Description", EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR);
						else if (map.get("status").equals(2036))
							item.put("Description", EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR);
						else if (map.get("status").equals(2037))
							item.put("Description", EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR);
						else if (map.get("status").equals(2038))
							item.put("Description", EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR);
						else if (map.get("status").equals(2039))
							item.put("Description", EOTConstants.INVALID_SIGNATURE_SIZE_ERROR);
						else if (map.get("status").equals(2040))
							item.put("Description", EOTConstants.INVALID_IDPROOF_SIZE_ERROR);
						else if (map.get("status").equals(2041))
							item.put("Description", EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR);
						else if (map.get("status").equals(10))
							item.put("Description", EOTConstants.REVERSAL_STATUS);
					}
					DecimalFormat format = new DecimalFormat("#0.00");
					String amount = format.format(map.get("Amount"));
					amount = amount.equals(".00") == true ? "0.00" : amount;
					item.put("amount", amount);
					String serviceCharge ="";
					if (map.get("ServiceCharges") != null) {
						serviceCharge = format.format(map.get("ServiceCharges"));
						serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
						item.put("ServiceCharges", serviceCharge);
					} else
						item.put("ServiceCharges", "");
					//item.put("SC",map.get("SC")!=null?map.get("SC"):"");
					
						if(serviceCharge != "") {
							Double settlementAmount = Double.parseDouble(amount)-Double.parseDouble(serviceCharge);
							item.put("SettlementAmount", settlementAmount.toString());
						}else	
							item.put("SettlementAmount", amount);

					item.put("RequestChannel", map.get("requestChannel"));
					array.add(item);
				} 
			}
			json.put("data", array);
			//System.out.println(json);
		}catch(EOTException e){
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			json.put("recordsTotal", page.getTotalCount());
			json.put("recordsFiltered", page.getTotalCount());
			JSONArray array = new JSONArray();
			
			for (HashMap map : (List<HashMap>)page.getResults()) {
				JSONObject item = new JSONObject();
				item.put("TransactionID", map.get("TransactionID"));
				item.put("MerchantCode", map.get("MerchantCode"));
				item.put("BusinessName", map.get("BusinessName"));
				item.put("MerchantName", map.get("MerchantName"));
				item.put("MerchantMobile", map.get("MerchantMobile"));
				item.put("customerName", map.get("customerName"));
				item.put("customerMobile", map.get("customerMobile"));
				
				String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
				item.put("TransactionDate", TransactionDate);
				/*
				 * Set<TransactionTypesDesc> transactionTypesDescs =
				 * txntypeMap.get(map.get("TransactionType")).getTransactionTypesDescs(); String
				 * txnType=""; for (TransactionTypesDesc transactionTypesDesc :
				 * transactionTypesDescs) {
				 * 
				 * if(transactionTypesDesc.getComp_id().getLocale().equals(locale)) {
				 * txnType=transactionTypesDesc.getDescription(); break; } }
				 */
					item.put("TransactionType", map.get("Description")+"");
					item.put("status", map.get("status").equals(2000) ? "Success" : "Failed");
					if (map.get("status").equals(2000) == true)
						item.put("Description", "Success");
					else {
						if (map.get("status").equals(2001))
							item.put("Description", EOTConstants.FATAL_ERROR);
						else if (map.get("status").equals(2002))
							item.put("Description", EOTConstants.MISSING_PARAMETERS_ERROR);
						else if (map.get("status").equals(2003))
							item.put("Description", EOTConstants.INVALID_PARAMETERS_ERROR);
						else if (map.get("status").equals(2004))
							item.put("Description", EOTConstants.OPERATION_NOT_SUPPORTED_ERROR);
						else if (map.get("status").equals(2005))
							item.put("Description", EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR);
						else if (map.get("status").equals(2006))
							item.put("Description", EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR);
						else if (map.get("status").equals(2020))
							item.put("Description", EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2021))
							item.put("Description", EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2022))
							item.put("Description", EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR);
						else if (map.get("status").equals(2023))
							item.put("Description", EOTConstants.NO_VOUCHER_AVAILABLE_ERROR);
						else if (map.get("status").equals(2024))
							item.put("Description", EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR);
						else if (map.get("status").equals(2025))
							item.put("Description", EOTConstants.TXN_RULE_NOT_DEFINED_ERROR);
						else if (map.get("status").equals(2026))
							item.put("Description", EOTConstants.TXN_LIMIT_EXCEEDED_ERROR);
						else if (map.get("status").equals(2027))
							item.put("Description", EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR);
						else if (map.get("status").equals(2028))
							item.put("Description", EOTConstants.TXN_NUM_EXCEEDED_ERROR);
						else if (map.get("status").equals(2029))
							item.put("Description", EOTConstants.INVALID_TXN_RULE_ERROR);
						else if (map.get("status").equals(2030))
							item.put("Description", EOTConstants.NO_BALANCE_IN_WALLET_ERROR);
						else if (map.get("status").equals(2031))
							item.put("Description", EOTConstants.TXN_DECLINED_FROM_HPS_ERROR);
						else if (map.get("status").equals(2032))
							item.put("Description", EOTConstants.INVALID_CH_POOL_ERROR);
						else if (map.get("status").equals(2033))
							item.put("Description", EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR);
						else if (map.get("status").equals(2034))
							item.put("Description", EOTConstants.SETTLED_TRANSACTION_ERROR);
						else if (map.get("status").equals(2035))
							item.put("Description", EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR);
						else if (map.get("status").equals(2036))
							item.put("Description", EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR);
						else if (map.get("status").equals(2037))
							item.put("Description", EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR);
						else if (map.get("status").equals(2038))
							item.put("Description", EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR);
						else if (map.get("status").equals(2039))
							item.put("Description", EOTConstants.INVALID_SIGNATURE_SIZE_ERROR);
						else if (map.get("status").equals(2040))
							item.put("Description", EOTConstants.INVALID_IDPROOF_SIZE_ERROR);
						else if (map.get("status").equals(2041))
							item.put("Description", EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR);
						else if (map.get("status").equals(10))
							item.put("Description", EOTConstants.REVERSAL_STATUS);
					}
					DecimalFormat format = new DecimalFormat("#0.00");
					String amount = format.format(map.get("Amount"));
					amount = amount.equals(".00") == true ? "0.00" : amount;
					item.put("amount", amount);
					String serviceCharge = "";
					if (map.get("ServiceCharges") != null) {
						serviceCharge = format.format(map.get("ServiceCharges"));
						serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
						item.put("ServiceCharges", serviceCharge);
					} else
						item.put("ServiceCharges", "");
					//item.put("SC",map.get("SC")!=null?map.get("SC"):"");

						if(serviceCharge != "") {
							Double settlementAmount = Double.parseDouble(amount)-Double.parseDouble(serviceCharge);
							item.put("SettlementAmount", settlementAmount.toString());
						}else	
							item.put("SettlementAmount", amount);

					item.put("RequestChannel", map.get("requestChannel"));
				array.add(item);
			}
			json.put("data", array);
			return json.toString();
		}catch(Exception e){
			PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		return json.toString();
	}
	
	@RequestMapping("/showTransactionMIS.htm")
	public String showTransactionMIS(ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		TxnSummaryDTO txnSummaryDTO = new TxnSummaryDTO();
		int pageNumber=1;

		/*try{			
			txnSummaryDTO=new TxnSummaryDTO();

			Page page=transactionService.searchTxnSummary(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{*/
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));			
			model.put("txnSummaryDTO",txnSummaryDTO );
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			model.put("bankList", webUserService.getBanksByCountry(countryId));
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			pageLogger(request,response,"TransactionSummary");
		//}
		return "transactionReport";
	}
	
	@RequestMapping("/searchTxnMerchantData.htm")
	public String searchTxnMerchantData(TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response,HttpSession session) throws NumberFormatException, Exception{
		int pageNumber = 1;
		WebUser webUser=null;
		try{
			
			//txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {

				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				webUser=customerService.getUser(userName);	

				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}			
			model.put("language",localeResolver.resolveLocale(request));
		}/*catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} */catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{	
			model.put("locale",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("masterData",transactionService.getMasterData(localeResolver.resolveLocale(request).toString()));	
			model.put("transTypeList", transactionService.getTransactionType(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("txnSummaryDTO",txnSummaryDTO);
			model.put("language",localeResolver.resolveLocale(request));
			if(txnSummaryDTO.getBankId()!=null){
				model.put("branchList",webUserService.getAllBranchFromBank(txnSummaryDTO.getBankId()));
				model.put("profileList",transactionRulesService.getCustomerProfilesByBankId(txnSummaryDTO.getBankId()));
			}
			if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()))
				model.put("partners",dashBoardService.loadBusinessPartnerByType(Integer.parseInt(txnSummaryDTO.getPartnerType())));		
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			webUser=customerService.getUser(userName);
			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
				Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
				model.put("bankList",webUserService.getBanksByCountry(countryId));
			}else if(txnSummaryDTO.getCountryId()!=null){
				model.put("bankList",webUserService.getBanksByCountry(txnSummaryDTO.getCountryId()));
			}
		}	

		return "merchantAppReport";
	}	
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportToPdfMerchantCommisionReport.htm")
	public void exportToPDFMerchantCommisionReport(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		int pageNumber=1;
		List results=null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		String sortColumn = (String) session.getAttribute("sortColumn");
		String serialIndex = (String)session.getAttribute("index");
		String sortBy  = (String)session.getAttribute("sortBy");	
		try{	
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {


				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}	
			txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
			txnSummaryDTO.setSortBy(sortBy);
			Page page=transactionService.searchTxnMerchantData(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();

			DecimalFormat format = new DecimalFormat("#0.00");
			
			for(int i=0;i<results.size();i++) {
				
				HashMap<String, Object> map = (HashMap<String, Object>)results.get(i);
				for (Map.Entry<String, Object> entry : map.entrySet()) {					
						if(entry.getKey().equals("Amount")) {
							String amount = format.format(entry.getValue());
							amount = null != amount ? (amount.equals(".00") == true ? "0.00" : amount) : "0.00";							
							entry.setValue(amount);							
						}
						if(entry.getKey().equals("ServiceCharges")) {
							if(entry.getValue()!=null) {
								String serviceCharge = format.format(map.get("ServiceCharges"));
								serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
								entry.setValue(serviceCharge);							
							}else
								entry.setValue("0.00");							
						}
				}				
				results.set(i, map);
			}			
			page.requestPage="merchantReport.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}

		generatePDFReport(EOTConstants.JASPER_MERCHANT_COMMISSION_JRXML_NAME, EOTConstants.MERCHANT_COMMISSION_REPORT_NAME, results, model, request, response);
	}
	
	@RequestMapping("/exportToXlsMerchantCommisionReport.htm")
	public void exportToXlsMerchantCommisionReport(TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	
		int pageNumber=1;
		List results=null;
		HSSFWorkbook wb = null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		txnSummaryDTO.setActionType(EOTConstants.ACTION_EXPORT);
		String sortColumn = (String) session.getAttribute("sortColumn");
		String serialIndex = (String)session.getAttribute("index");
		String sortBy  = (String)session.getAttribute("sortBy");
		try{	
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
			} else {


				if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
					txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
				}
				session.setAttribute("txnSummaryDTO", txnSummaryDTO);
			}	
			
			txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
			txnSummaryDTO.setSortBy(sortBy);
			Page page=transactionService.searchTxnMerchantData(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			results = page.getResults();
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetFromListForMerchantCommisionReport(results, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.MERCHANT_COMMISSION_DETAILS_PAGE_HEADER);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.MERCHANT_COMMISSION_REPORT_NAME
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			txnSummaryDTO.setActionType(null);
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
	
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/showTxnSummaryData.htm", method=RequestMethod.POST)
	public @ResponseBody String showTxnSummaryData(TxnSummaryDTO txnSummaryDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		String serialIndex = request.getParameter("index");
		int index=0;
		if (serialIndex !=null && !serialIndex.equals("")) {
			index = Integer.parseInt(serialIndex);
		}
		
		Integer pageNumber = null;//request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		String locale = localeResolver.resolveLocale(request).toString().substring(0, 2);
		Map<Integer, TransactionType> txntypeMap = transactionService.getTxntypeMap(locale);
		
		if( request.getParameter("pageNumber") != null ){
			pageNumber = new Integer(request.getParameter("pageNumber"));
			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
		} else {

			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
				txnSummaryDTO.setBranchId(txnSummaryDTO.getBranch());
			}
		//	txnSummaryDTO.setBankId(txnSummaryDTO.getBank()); 
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
		
		if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
			BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
			if (businessPartner != null) {
				//txnSummaryDTO.setPartnerId(businessPartner.getId().toString());
				//txnSummaryDTO.setPartnerType(businessPartner.getPartnerType().toString());
				txnSummaryDTO.setAccountNumber(businessPartner.getAccountNumber());
			}
		}
		if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
			BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
			if (businessPartner != null) {
				txnSummaryDTO.setSuperAgentCode(businessPartner.getCode());
			}
		}
		txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
		txnSummaryDTO.setSortBy(sortBy);
		JSONObject json = null;
		try{			
			//txnSummaryDTO=new TxnSummaryDTO();
			response.setContentType("application/json");
			json = new JSONObject();
			Page page=transactionService.searchTxnSummary(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			
			json.put("recordsTotal", page!=null ? page.getTotalCount() : 0);
			json.put("recordsFiltered", page!=null ? page.getTotalCount() : 0);
			JSONArray array = new JSONArray();
			
			
			if (page !=null && page.getResults()!=null) {
				
				for (HashMap map : (List<HashMap>) page.getResults()) {
					JSONObject item = new JSONObject();
					
					item.put("SerNo", ++index);
					
					if(null != map.get("TransactionDate")) {
						String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
						item.put("TransactionDate", TransactionDate);
					}else
						item.put("TransactionDate", "");
					item.put("TransactionID", map.get("TransactionID"));
					item.put("TransactionType", map.get("Description"));

					String RefTranID = map.get("Ref_TXN_ID")+"";
					item.put("RefTranID", !RefTranID.equals("null") ? RefTranID : "");
					
					item.put("InitiaterName", map.get("InitName"));
					item.put("InitiaterMobileNumber", map.get("InitMobile"));
					item.put("UserName", map.get("UserName"));
					item.put("AgentOrMerchantOrWebUserCode", map.get("BenfCode"));
					
					DecimalFormat format = new DecimalFormat("#0.00");
					String amount = format.format(map.get("Amount"));
					amount = amount.equals(".00") == true ? "0.00" : amount;
					item.put("amount", amount);
					if (map.get("SC") != null) {
						String serviceCharge = format.format(map.get("SC"));
						serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
						item.put("SC", serviceCharge);
					} else
						item.put("SC", "");
					item.put("BenOrCustomerName", map.get("BenfName"));
					item.put("BenOrCustomerMobileNumber", map.get("BenfMobile"));
					item.put("SuperAgentName", map.get("Name"));
					item.put("SuperAgentCode", map.get("Code"));					
						if (null == map.get("Status")) {
							item.put("Description","");
							item.put("Status","");
						}else {
						item.put("Status", map.get("Status").equals(2000) ? "Success" :map.get("Status").equals(10)?"Success": "Failed");
	
						if (map.get("Status").equals(2000) == true)
							item.put("Description", "Success");
						else {
							if (map.get("Status").equals(2001))
								item.put("Description", EOTConstants.FATAL_ERROR);
							else if (map.get("Status").equals(2002))
								item.put("Description", EOTConstants.MISSING_PARAMETERS_ERROR);
							else if (map.get("Status").equals(2003))
								item.put("Description", EOTConstants.INVALID_PARAMETERS_ERROR);
							else if (map.get("Status").equals(2004))
								item.put("Description", EOTConstants.OPERATION_NOT_SUPPORTED_ERROR);
							else if (map.get("Status").equals(2005))
								item.put("Description", EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR);
							else if (map.get("Status").equals(2006))
								item.put("Description", EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR);
							else if (map.get("Status").equals(2020))
								item.put("Description", EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR);
							else if (map.get("Status").equals(2021))
								item.put("Description", EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR);
							else if (map.get("Status").equals(2022))
								item.put("Description", EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR);
							else if (map.get("Status").equals(2023))
								item.put("Description", EOTConstants.NO_VOUCHER_AVAILABLE_ERROR);
							else if (map.get("Status").equals(2024))
								item.put("Description", EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR);
							else if (map.get("Status").equals(2025))
								item.put("Description", EOTConstants.TXN_RULE_NOT_DEFINED_ERROR);
							else if (map.get("Status").equals(2026))
								item.put("Description", EOTConstants.TXN_LIMIT_EXCEEDED_ERROR);
							else if (map.get("Status").equals(2027))
								item.put("Description", EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR);
							else if (map.get("Status").equals(2028))
								item.put("Description", EOTConstants.TXN_NUM_EXCEEDED_ERROR);
							else if (map.get("Status").equals(2029))
								item.put("Description", EOTConstants.INVALID_TXN_RULE_ERROR);
							else if (map.get("Status").equals(2030))
								item.put("Description", EOTConstants.NO_BALANCE_IN_WALLET_ERROR);
							else if (map.get("Status").equals(2031))
								item.put("Description", EOTConstants.TXN_DECLINED_FROM_HPS_ERROR);
							else if (map.get("Status").equals(2032))
								item.put("Description", EOTConstants.INVALID_CH_POOL_ERROR);
							else if (map.get("Status").equals(2033))
								item.put("Description", EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR);
							else if (map.get("Status").equals(2034))
								item.put("Description", EOTConstants.SETTLED_TRANSACTION_ERROR);
							else if (map.get("Status").equals(2035))
								item.put("Description", EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR);
							else if (map.get("Status").equals(2036))
								item.put("Description", EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR);
							else if (map.get("Status").equals(2037))
								item.put("Description", EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR);
							else if (map.get("Status").equals(2038))
								item.put("Description", EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR);
							else if (map.get("Status").equals(2039))
								item.put("Description", EOTConstants.INVALID_SIGNATURE_SIZE_ERROR);
							else if (map.get("Status").equals(2040))
								item.put("Description", EOTConstants.INVALID_IDPROOF_SIZE_ERROR);
							else if (map.get("Status").equals(2041))
								item.put("Description", EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR);
							else if (map.get("Status").equals(10))
								item.put("Description", EOTConstants.REVERSAL_STATUS);
							else
								item.put("Description", "");
						
					}
					}
					item.put("RequestChannel", map.get("requestChannel"));
					array.add(item);
				} 
			}
			json.put("data", array);
			//System.out.println(json);
		}catch(EOTException e){
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			json.put("recordsTotal", page.getTotalCount());
			json.put("recordsFiltered", page.getTotalCount());
			JSONArray array = new JSONArray();
			
			for (HashMap map : (List<HashMap>)page.getResults()) {
				JSONObject item = new JSONObject();
				if(null !=map.get("TransactionDate") && !"".equals(map.get("TransactionDate"))) {
					String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
					item.put("TransactionDate", TransactionDate);
				}else
					item.put("TransactionDate", "");
				item.put("TransactionID", map.get("TransactionID"));
				item.put("TransactionType", map.get("Description"));

				String RefTranID = map.get("Ref_TXN_ID")+"";
				item.put("RefTranID", !RefTranID.equals("null") ? RefTranID : "");
				
				item.put("InitiaterName", map.get("InitName"));
				item.put("InitiaterMobileNumber", map.get("InitMobile"));
				item.put("UserName", map.get("UserName"));
				item.put("AgentOrMerchantOrWebUserCode", map.get("BenfCode"));
				
				DecimalFormat format = new DecimalFormat("#0.00");
				String amount = format.format(map.get("Amount"));
				amount = amount.equals(".00") == true ? "0.00" : amount;
				item.put("amount", amount);
				if (map.get("SC") != null) {
					String serviceCharge = format.format(map.get("SC"));
					serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
					item.put("SC", serviceCharge);
				} else
					item.put("SC", "");
				item.put("BenOrCustomerName", map.get("BenfName"));
				item.put("BenOrCustomerMobileNumber", map.get("BenfMobile"));
				item.put("SuperAgentName", map.get("Name"));
				item.put("SuperAgentCode", map.get("Code"));					
				if (null != map.get("Status")) {
				item.put("Status", map.get("Status").equals(2000) ? "Success" : "Failed");

				if (map.get("Status").equals(2000) == true)
					item.put("Description", "Success");
				else {
					if (map.get("Status").equals(2001))
						item.put("Description", EOTConstants.FATAL_ERROR);
					else if (map.get("Status").equals(2002))
						item.put("Description", EOTConstants.MISSING_PARAMETERS_ERROR);
					else if (map.get("Status").equals(2003))
						item.put("Description", EOTConstants.INVALID_PARAMETERS_ERROR);
					else if (map.get("Status").equals(2004))
						item.put("Description", EOTConstants.OPERATION_NOT_SUPPORTED_ERROR);
					else if (map.get("Status").equals(2005))
						item.put("Description", EOTConstants.SETTLEMENT_FILE_GENERATION_ERROR);
					else if (map.get("Status").equals(2006))
						item.put("Description", EOTConstants.MERCHANT_ACC_SAME_CUST_ACC_ERROR);
					else if (map.get("Status").equals(2020))
						item.put("Description", EOTConstants.MERCHANT_NOT_ENOUGH_BALANCE_ERROR);
					else if (map.get("Status").equals(2021))
						item.put("Description", EOTConstants.CUSTOMER_NOT_ENOUGH_BALANCE_ERROR);
					else if (map.get("Status").equals(2022))
						item.put("Description", EOTConstants.PAYEE_NOT_ENOUGH_BALANCE_ERROR);
					else if (map.get("Status").equals(2023))
						item.put("Description", EOTConstants.NO_VOUCHER_AVAILABLE_ERROR);
					else if (map.get("Status").equals(2024))
						item.put("Description", EOTConstants.SERVICE_CHARGE_NOT_DEFINED_ERROR);
					else if (map.get("Status").equals(2025))
						item.put("Description", EOTConstants.TXN_RULE_NOT_DEFINED_ERROR);
					else if (map.get("Status").equals(2026))
						item.put("Description", EOTConstants.TXN_LIMIT_EXCEEDED_ERROR);
					else if (map.get("Status").equals(2027))
						item.put("Description", EOTConstants.CUM_TXN_LIMIT_EXCEEDED_ERROR);
					else if (map.get("Status").equals(2028))
						item.put("Description", EOTConstants.TXN_NUM_EXCEEDED_ERROR);
					else if (map.get("Status").equals(2029))
						item.put("Description", EOTConstants.INVALID_TXN_RULE_ERROR);
					else if (map.get("Status").equals(2030))
						item.put("Description", EOTConstants.NO_BALANCE_IN_WALLET_ERROR);
					else if (map.get("Status").equals(2031))
						item.put("Description", EOTConstants.TXN_DECLINED_FROM_HPS_ERROR);
					else if (map.get("Status").equals(2032))
						item.put("Description", EOTConstants.INVALID_CH_POOL_ERROR);
					else if (map.get("Status").equals(2033))
						item.put("Description", EOTConstants.UNABLE_TO_CONNECT_TO_EOT_CARD_ERROR);
					else if (map.get("Status").equals(2034))
						item.put("Description", EOTConstants.SETTLED_TRANSACTION_ERROR);
					else if (map.get("Status").equals(2035))
						item.put("Description", EOTConstants.PREPAID_ACCOUNT_BALANCE_EXCEEDED_ERROR);
					else if (map.get("Status").equals(2036))
						item.put("Description", EOTConstants.TXN_DETAILS_NOT_AVAILABLE_ERROR);
					else if (map.get("Status").equals(2037))
						item.put("Description", EOTConstants.BALANCE_DETAILS_NOT_AVAILABLE_ERROR);
					else if (map.get("Status").equals(2038))
						item.put("Description", EOTConstants.CUSTOMER_MERCHANT_NOT_IN_SAME_BANK_ERROR);
					else if (map.get("Status").equals(2039))
						item.put("Description", EOTConstants.INVALID_SIGNATURE_SIZE_ERROR);
					else if (map.get("Status").equals(2040))
						item.put("Description", EOTConstants.INVALID_IDPROOF_SIZE_ERROR);
					else if (map.get("Status").equals(2041))
						item.put("Description", EOTConstants.MOBILE_NUMBER_REGISTERED_ALREADY_ERROR);
				}
				}else {
					item.put("Description","");
					item.put("Status","");
				}
				item.put("RequestChannel", map.get("requestChannel"));
				array.add(item);
			}
			json.put("data", array);
			return json.toString();
		}catch(Exception e){
			PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		return json.toString();
	}
	
	@RequestMapping("/confAccountToAccount.htm")
	public String confAccountToAccount(HttpServletRequest request,ModelMap model,HttpSession session,HttpServletResponse response){

		try {

			System.out.println(request.getParameter("fromAccount"));
			System.out.println(request.getParameter("toAccount"));
//			AccountDetailsDTO accountDetailsDTO = transactionService.getAccountDetails(request.getParameter("mobileNumber"),null,false);
			AccountDetailsDTO accountDetailsDTO = transactionService.getAccountsForAccToAcc(request.getParameter("fromAccount"),request.getParameter("toAccount"),null,false);
//			accountDetailsDTO.setTransactionType(Integer.parseInt(request.getParameter("transactionType")));
			accountDetailsDTO.setTransactionType(144);

			model.put("accountDetailsDTO", accountDetailsDTO );
			session.setAttribute("accountDetailsDTO", accountDetailsDTO);

			return "accountToAccountDetailForm";

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "accountTransfer";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "accountTransfer";
		}finally{
			Integer countryId = EOTConstants.DEFAULT_COUNTRY ;
			Country country = customerService.getCountry(countryId);
			model.put("mobileNumLength", country.getMobileNumberLength());
			model.put("isdCode", country.getIsdCode());
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString() ));
			model.put("language",localeResolver.resolveLocale(request) );
			pageLogger(request,response,"AccountDetails");
		}

	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/searchBulkPayTxnReportData.htm", method=RequestMethod.POST)
	public @ResponseBody String searchBulkPayTxnReportData(TxnSummaryDTO txnSummaryDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		String serialIndex = request.getParameter("index");
		int index=0;
		if (serialIndex !=null && !serialIndex.equals("")) {
			index = Integer.parseInt(serialIndex);
		}
		
		Integer pageNumber = null;
		String locale = localeResolver.resolveLocale(request).toString().substring(0, 2);
		if( request.getParameter("pageNumber") != null ){
			pageNumber = new Integer(request.getParameter("pageNumber"));
			txnSummaryDTO = (TxnSummaryDTO) session.getAttribute("txnSummaryDTO");
		} else {
			session.setAttribute("txnSummaryDTO", txnSummaryDTO);
		}
		
		txnSummaryDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
		txnSummaryDTO.setSortBy(sortBy);
		JSONObject json = null;
		try{			
			response.setContentType("application/json");
			json = new JSONObject();
			Page page=transactionService.searchBulkPayTxnReport(txnSummaryDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			
			json.put("recordsTotal", page!=null ? page.getTotalCount() : 0);
			json.put("recordsFiltered", page!=null ? page.getTotalCount() : 0);
			JSONArray array = new JSONArray();
			
			
			if (page !=null && page.getResults()!=null) {
				
				for (HashMap map : (List<HashMap>) page.getResults()) {
					JSONObject item = new JSONObject();
					
					item.put("SerNo", ++index);
					
					if(null != map.get("TransactionDate")) {
						String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
						item.put("TransactionDate", TransactionDate);
					}else
						item.put("TransactionDate", "");
					item.put("TransactionID", map.get("TransactionID"));
					
					item.put("Name", map.get("Name"));
					item.put("MobileNumber", map.get("MobileNumber"));
					
					DecimalFormat format = new DecimalFormat("#0.00");
					String amount = format.format(map.get("Amount"));
					amount = amount.equals(".00") == true ? "0.00" : amount;
					item.put("amount", amount);
					if (map.get("SC") != null) {
						String serviceCharge = format.format(map.get("SC"));
						serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
						item.put("SC", serviceCharge);
					} else
						item.put("SC", "");
										
						if (null == map.get("Status")) {
							item.put("Status","");
						}else {
							item.put("Status", map.get("Status").equals(2000) ? "Success" :map.get("Status").equals(10)?"Success": "Failed");
					array.add(item);
				} 
			}
			}
			json.put("data", array);
		}catch(EOTException e){
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			json.put("recordsTotal", page.getTotalCount());
			json.put("recordsFiltered", page.getTotalCount());
			JSONArray array = new JSONArray();
			
			for (HashMap map : (List<HashMap>)page.getResults()) {
				JSONObject item = new JSONObject();
				if(null !=map.get("TransactionDate") && !"".equals(map.get("TransactionDate"))) {
					String TransactionDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(map.get("TransactionDate"));
					item.put("TransactionDate", TransactionDate);
				}else
					item.put("TransactionDate", "");
				item.put("TransactionID", map.get("TransactionID"));
				
				item.put("Name", map.get("Name"));
				item.put("MobileNumber", map.get("MobileNumber"));
				
				DecimalFormat format = new DecimalFormat("#0.00");
				String amount = format.format(map.get("Amount"));
				amount = amount.equals(".00") == true ? "0.00" : amount;
				item.put("amount", amount);
				if (map.get("SC") != null) {
					String serviceCharge = format.format(map.get("SC"));
					serviceCharge = serviceCharge.equals(".00") == true ? "0.00" : serviceCharge;
					item.put("SC", serviceCharge);
				} else
					item.put("SC", "");
									
				if (null != map.get("Status")) {
				item.put("Status", map.get("Status").equals(2000) ? "Success" : "Failed");

				}
				array.add(item);
			}
			json.put("data", array);
			return json.toString();
		}catch(Exception e){
			PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
		}
		return json.toString();
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bankFloatDeposit.htm", method=RequestMethod.POST)
	public String bankFloatDepositReportData( BankFloatDepositDTO bankFloatDepositDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		bankFloatDepositDTO.setActionType("");

		try {
			Page page=transactionService.BankFloatDepositReportData(bankFloatDepositDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
		}catch(EOTException e){
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			return "bankFloatDeposit";
		}catch(Exception e){
			PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
			return "bankFloatDeposit";
		}finally {
			model.put("bankFloatDepositDTO", bankFloatDepositDTO);
		}
		return "bankFloatDeposit";
	}
	
	@RequestMapping("/exportToXlsForBankFloatDeposit.htm")
	public void exportToXLSBankFloatDepositDTO(BankFloatDepositDTO bankFloatDepositDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException {

		HSSFWorkbook wb = null;
		Integer pageNumber = null;
		bankFloatDepositDTO.setActionType(EOTConstants.ACTION_EXPORT);
		
		try{ 
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);

			Page page=transactionService.BankFloatDepositReportData(bankFloatDepositDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage = "searchAgentPage.htm";
			model.put("page",page);	
			
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetFromListForBankFloatDepositReportData(page.getResults(),localeResolver.resolveLocale(request),messageSource,bankFloatDepositDTO,EOTConstants.BANK_FLOAT_DEPOSIT_DETAILS_PAGE_HEADER,userName);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}finally {
			bankFloatDepositDTO.setActionType(null);
			model.put("bankFloatDepositDTO", bankFloatDepositDTO);	
		}
		return ;
	}
	
	@RequestMapping("/exportToPdfForBankFloatDeposit.htm")
	public void exportToPdfBankFloatDepositDTO(BankFloatDepositDTO bankFloatDepositDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException {

		Integer pageNumber = null;
		bankFloatDepositDTO.setActionType(EOTConstants.ACTION_EXPORT);
		Page page=null;
		try{ 
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);

			page=transactionService.BankFloatDepositReportData(bankFloatDepositDTO, pageNumber, localeResolver.resolveLocale(request).toString());
			page.requestPage="searchTxnSummary.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}finally {
			bankFloatDepositDTO.setActionType(null);
			model.put("bankFloatDepositDTO", bankFloatDepositDTO);
		}
		String reportName =EOTConstants.JASPER_BANK_FLOAT_DEPOSIT_DETAILS_JRXML_NAME;
		generatePDFReport(reportName, EOTConstants.BANK_FLOAT_DEPOSIT_REPORT_NAME, page.getResults(), model, request, response);
		return ;
	}
	
	@RequestMapping(value = "/nonRegUssdCustomer.htm", method=RequestMethod.POST)
	public String nonRegUssdCustomerReportData( NonRegUssdCustomerDTO nonRegUssdCustomerDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		
		try {
			if(null == nonRegUssdCustomerDTO.getFromDate() && null == nonRegUssdCustomerDTO.getToDate()) {
				String toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    String fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
//			    nonRegUssdCustomerDTO.setFromDate(new SimpleDateFormat("dd/MM/yyyy").parse(fromDate));
//			    nonRegUssdCustomerDTO.setToDate(new SimpleDateFormat("dd/MM/yyyy").parse(toDate));
			    
			    nonRegUssdCustomerDTO.setFromDate(cal.getTime());
			    nonRegUssdCustomerDTO.setToDate(new Date());
			}
			Page page=transactionService.NonRegUssdCustomerReportData(nonRegUssdCustomerDTO, pageNumber);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", ErrorConstants.NO_RECORDS_FOUND);
			return "nonRegUssdCustomer";
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
			return "nonRegUssdCustomer";
		}finally {
			model.put("nonRegUssdCustomerDTO", nonRegUssdCustomerDTO);
		}
		return "nonRegUssdCustomer";
	}
	
	@RequestMapping("/exportToXlsForNonRegUssdCustomer.htm")
	public void exportToXLSNonRegUssdCustomer( NonRegUssdCustomerDTO nonRegUssdCustomerDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException {

		HSSFWorkbook wb = null;
		Integer pageNumber = null;
		
		try{ 
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);

			Page page=transactionService.NonRegUssdCustomerReportData(nonRegUssdCustomerDTO, pageNumber);
			page.requestPage = "nonRegUssdCustomer.htm";
			model.put("page",page);	
			
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetFromListForNonRegUssdCustomerReportData(page.getResults(),localeResolver.resolveLocale(request),messageSource,nonRegUssdCustomerDTO,EOTConstants.NON_REG_USSD_CUSTOMER_DETAILS_PAGE_HEADER,userName);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			model.put("nonRegUssdCustomerDTO", nonRegUssdCustomerDTO);
		}
	}
	
	@RequestMapping("/exportToPdfForNonRegUssdCustomer.htm")
	public void exportToPdfNonRegUssdCustomer(NonRegUssdCustomerDTO nonRegUssdCustomerDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException {

		Integer pageNumber = null;

		Page page=null;
		try{ 
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);

			page=transactionService.NonRegUssdCustomerReportData(nonRegUssdCustomerDTO, pageNumber);
			page.requestPage="nonRegUssdCustomer.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			model.put("nonRegUssdCustomerDTO", nonRegUssdCustomerDTO);
		}
		String reportName =EOTConstants.JASPER_NON_REG_USSD_CUSTOMER_DETAILS_JRXML_NAME;
		generatePDFReport(reportName, EOTConstants.NON_REG_USSD_CUSTOMER_REPORT_NAME, page.getResults(), model, request, response);
	
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/transactionVolume.htm", method=RequestMethod.POST)
	public String transactionVolumeReportData( TransactionVolumeDTO transactionVolumeDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser webUser=customerService.getUser(userName);
		try {
			 if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
				 BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 transactionVolumeDTO.setCode(businessPartnerUser.getBusinessPartner().getCode());
			 }
			Page page=transactionService.transactionVolumeReportData(transactionVolumeDTO, pageNumber);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
		}catch(EOTException e){
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			return "transactionVolumeReport";
		}catch(Exception e){
			PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
			return "transactionVolumeReport";
		}finally {
			model.put("transactionVolumeDTO", transactionVolumeDTO);
		}
		return "transactionVolumeReport";
	}
	
	@RequestMapping("/exportToXlsForTransactionVolume.htm")
	public void exportToXLSTransactionVolume( TransactionVolumeDTO transactionVolumeDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException {

		HSSFWorkbook wb = null;
		Integer pageNumber = null;
		
		try{ 
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);

			 if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
				 BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 transactionVolumeDTO.setCode(businessPartnerUser.getBusinessPartner().getCode());
			 }
			Page page=transactionService.transactionVolumeReportData(transactionVolumeDTO, pageNumber);
			page.requestPage = "transactionVolume.htm";
			model.put("page",page);	
			
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetFromListForTransactionVolumeReportData(page.getResults(),localeResolver.resolveLocale(request),messageSource,transactionVolumeDTO,EOTConstants.TRANSACTION_VOLUME_REPORT_DETAILS_PAGE_HEADER,userName);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			model.put("transactionVolumeDTO", transactionVolumeDTO);
		}
	}
	
	@RequestMapping("/exportToPdfForTransactionVolume.htm")
	public void exportToPdfTransactionVolume(TransactionVolumeDTO transactionVolumeDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException {

		Integer pageNumber = null;

		Page page=null;
		try{ 
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);

			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
				 BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 transactionVolumeDTO.setCode(businessPartnerUser.getBusinessPartner().getCode());
			 }
			page=transactionService.transactionVolumeReportData(transactionVolumeDTO, pageNumber);
			page.requestPage="transactionVolume.htm";
			model.put("page",page);				
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			model.put("transactionVolumeDTO", transactionVolumeDTO);
		}
		String reportName =EOTConstants.JASPER_TRANSACTION_VOLUME_REPORT_DETAILS_JRXML_NAME;
		generatePDFReport(reportName, EOTConstants.TRANSACTION_VOLUME_REPORT_NAME, page.getResults(), model, request, response);
	
	}
	
}