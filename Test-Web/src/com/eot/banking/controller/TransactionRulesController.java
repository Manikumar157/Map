package com.eot.banking.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.TransactionRulesDTO;
import com.eot.banking.dto.TxnRuleSearchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.TransactionRulesService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.Page;
import com.eot.entity.ServiceChargeRuleTxn;
import com.eot.entity.TransactionRule;
import com.eot.entity.TransactionRuleTxn;

@Controller
public class TransactionRulesController extends PageViewController{

	@Autowired
	private TransactionRulesService transactionRulesService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private ExcelWrapper wrapper;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/showTransactionRuleForm.htm")
	public String showTransactionRuleForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){
		try{
			model.put("message", request.getParameter("message"));
			String ruleLevelstr = request.getParameter("ruleLevel") ;
			TransactionRulesDTO transactionRulesDTO = new TransactionRulesDTO();
			transactionRulesDTO.setRuleLevel(ruleLevelstr !=null ? Integer.parseInt(ruleLevelstr):1);
			model.put("transactionRulesDTO", transactionRulesDTO );
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("masterData", transactionRulesService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("customerProfileList",transactionRulesService.getCustomerProfiles());
			/*
			 * if(ruleLevelstr.equals("3"))
			 * model.put("customerProfileList",transactionRulesService.getCustomerProfiles()
			 * );
			 */
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"TransactionRule");
		}

		return "transactionRuleForm";
	}

	@RequestMapping("/editTransactionRule.htm")
	public String editTransactionRule(@RequestParam Long trRuleId,@RequestParam Integer ruleLevel1,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			
			transactionRulesService.getTransactionRuleDetails(trRuleId,model,localeResolver.resolveLocale(request).toString());
//			if(ruleLevel1==3)
			model.put("customerProfileList",transactionRulesService.getCustomerProfiles());
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			pageLogger(request,response,"EditTransactionRule");
		}

		return "transactionRuleForm";

	}

	@RequestMapping("/viewTransactionRule.htm")
	public String viewTransactionRule(@RequestParam Long trRuleId, Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			transactionRulesService.getTransactionRuleDetails(trRuleId,model,localeResolver.resolveLocale(request).toString());
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			pageLogger(request,response,"ViewTransactionRule");
		}

		return "viewTransactionRule";

	}

	@RequestMapping("/saveTransactionRule.htm")
	public String saveTransactionRule(@Valid TransactionRulesDTO transactionRulesDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request){
		try {
			
			if(result.hasErrors()){
				
				model.put("transactionRulesDTO", transactionRulesDTO);
			}else{
				transactionRulesService.saveOrUpdateTransactionRules(transactionRulesDTO);

				if( transactionRulesDTO.getBankId() !=null ){
					model.put("bankId", transactionRulesDTO.getBankId() );
					model.put("bankName", transactionRulesDTO.getBankName() );
				}
				if( transactionRulesDTO.getAccountNumber() !=null ){
					model.put("bankId", transactionRulesDTO.getBankId() );
					model.put("accountNumber", transactionRulesDTO.getAccountNumber()  );
				}
				model.put("message",transactionRulesDTO.getTransactionRuleId()==null?"ADD_TXN_RULES_SUCCESS":"UPDATE_TXN_RULES_SUCCESS");
				model.put("transactionRulesDTO", new TransactionRulesDTO());
				return "transactionRuleForm";
				//return "redirect:/showTransactionRuleForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		} catch (EOTException e) {
			model.put("transactionRulesDTO", transactionRulesDTO);
			e.printStackTrace();
			model.put("message", e.getErrorCode());

		} catch (Exception e) {
			model.put("transactionRulesDTO", transactionRulesDTO);
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				model.put("masterData", transactionRulesService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("ruleLevel", transactionRulesDTO.getRuleLevel());
				if(transactionRulesDTO.getRuleLevel()==3)
				model.put("customerProfileList",transactionRulesService.getCustomerProfiles());
				
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}

		return "transactionRuleForm";

	}

	@RequestMapping("/listTransactionRules.htm")
	public String listTransactionRules(@RequestParam Integer pageNumber,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){
		try {				
			
			TxnRuleSearchDTO trSearchDTO = (TxnRuleSearchDTO) session.getAttribute("trSearchDTO" );
			trSearchDTO.setPageNumber(pageNumber);
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			transactionRulesService.searchTransactionRules(userName,trSearchDTO, model);
					
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			pageLogger(request,response,"ListTransactionRules");
		}

		return "listTransactionRules";
	}
	
	@RequestMapping("/searchTransactionRules.htm")
	public String searchTransactionRules(TxnRuleSearchDTO trSearchDTO ,@RequestParam Integer ruleLevel,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){
		
		try {				
			//System.out.println("gid :: "+trSearchDTO.getBankGroupId());
			//System.out.println("bid :: "+trSearchDTO.getBankId());
			//System.out.println("cpid :: "+trSearchDTO.getProfileId());
			//System.out.println("rlevel :: "+trSearchDTO.getRuleLevel());
			session.setAttribute("trSearchDTO", trSearchDTO );
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			transactionRulesService.searchTransactionRules(userName,trSearchDTO, model);
					
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("masterData", transactionRulesService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			pageLogger(request,response,"TransactionRule");
			}catch (EOTException e) {
				
				
			}
		}

		return "listTransactionRules";
		
	}
	
	@RequestMapping("/getBanks.htm")
	public String getBanks(@RequestParam Integer bankGroupId, Map<String, Object> model){
		
			try {
				model.put("entity", "bankName");
				model.put("id", "bankId");
				model.put("value", "bankName");
				model.put("list", transactionRulesService.getBanksByGroupId(bankGroupId));
			} catch (EOTException e) {
			}
		
			return "combo";
	}
	
	@RequestMapping("/getCustomerProfiles.htm")
	public String getCustomerProfiles(@RequestParam Integer bankId, Map<String, Object> model){
		
			try {
				model.put("entity", "profileName");
				model.put("id", "profileId");
				model.put("value", "profileName");
				model.put("list", transactionRulesService.getCustomerProfilesByBankId(bankId));
			} catch (EOTException e) {
			}
		
			return "combo";
	}
	
	@RequestMapping("/exportToPdfForTxnRules.htm")
	public void exportToPDFForRxnRules(TxnRuleSearchDTO trSearchDTO ,@RequestParam Integer ruleLevel,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		
		List<TransactionRule> results=null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		String txnRuleName="";
		List<String> txTypeNameList = new ArrayList<String>();
		
		try {				
			transactionRulesService.searchTransactionRules(userName,trSearchDTO, model);
			Object object = model.get("page"); 	
			Page page=(Page)object;
			results=page.getResults();
			
			for (int i = 0; i < results.size(); i++) {
				
				//serviceChargeRuleName = results.get(i).getServiceChargeRuleName();
				Set<TransactionRuleTxn> txnRuleTxnsSet = results.get(i).getTransactionRuleTxns();
				
				for (TransactionRuleTxn txnRule : txnRuleTxnsSet) {
					
					if (txnRule.getSourceType().intValue() == 1) {
						txnRuleName=txnRule.getTransactionType().getDescription() +" - Wallet";
					}else if (txnRule.getSourceType().intValue() ==  2) {
						txnRuleName=txnRule.getTransactionType().getDescription() +" - Card";
					}else if (txnRule.getSourceType().intValue() ==  3) {
						txnRuleName=txnRule.getTransactionType().getDescription() +" - Bank Account";
					}else if (txnRule.getSourceType().intValue() ==  4) {
						txnRuleName=txnRule.getTransactionType().getDescription() +" - FI";
					}else {
						txnRuleName=txnRule.getTransactionType().getDescription() +" - Others";
					}
					txTypeNameList.add(txnRuleName);
				}
			}
			
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", userName);
			model.put("date", dt1);
			model.put("txTypeNameList", txTypeNameList);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}

		generatePDFReport(EOTConstants.JASPER_TXN_RULE_JRXML_NAME, EOTConstants.TXN_REPORT_NAME, results, model, request, response);

	
	}
	
	@RequestMapping("/exportToXlsForTxnRules.htm")
	public void exportToXlsForTxnRules(TxnRuleSearchDTO trSearchDTO ,@RequestParam Integer ruleLevel,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		int pageNumber=1;
		List<TransactionRule> results=null;
		HSSFWorkbook wb = null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		try {				
			transactionRulesService.searchTransactionRules(userName,trSearchDTO, model);
			Object object = model.get("page"); 	
			Page page=(Page)object;
			results=page.getResults();
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", userName);
			model.put("date", dt1);
			
			wb = wrapper.createSpreadSheetFromListForTxnRules(results, localeResolver.resolveLocale(request), messageSource, userName, EOTConstants.TXR_DETAILS_PAGE_HEADER);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.TXN_REPORT_NAME
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
		}


	
	}
}
