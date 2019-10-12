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
import com.eot.banking.dto.SCSearchDTO;
import com.eot.banking.dto.ServiceChargeDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.SCManagementService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.Page;
import com.eot.entity.ServiceChargeRule;
import com.eot.entity.ServiceChargeRuleTxn;

@Controller
public class SCManagementController extends PageViewController{

	@Autowired
	private SCManagementService scManagementService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private ExcelWrapper wrapper;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/showServiceChargeRuleForm.htm")
	public String showServiceChargeRuleForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){
		try{			
			model.put("message", request.getParameter("message"));
			String ruleLevelstr = request.getParameter("ruleLevel1") ;
			ServiceChargeDTO serviceChargeDTO = new ServiceChargeDTO();
			serviceChargeDTO.setRuleLevel(ruleLevelstr !=null ? Integer.parseInt(ruleLevelstr):1);
			model.put("serviceChargeDTO", serviceChargeDTO );
			model.put("masterData", scManagementService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			if(ruleLevelstr!=null && ruleLevelstr.equals("3"))
			model.put("customerProfileList",scManagementService.getCustomerProfiles());
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			pageLogger(request,response,"ServiceChargeRule");
		}

		return "serviceChargeRuleForm";
	}	
	
	@RequestMapping("/saveServiceChargeRule.htm")
	public String saveServiceChargeRule(@Valid ServiceChargeDTO serviceChargeDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request){
		try {
			if(result.hasErrors()){
				
				model.put("serviceChargeDTO",serviceChargeDTO);
				System.out.println(result.hasErrors());
			}else{
				scManagementService.saveOrUpdateServiceCharge(serviceChargeDTO);
			
			String message = serviceChargeDTO.getServiceChargeRuleId()==null?"SERVICE_CHARGE_SUCCESS":"SC_UPDATE_SUCCESS" ;
			model.put("message",message );
			model.put("ruleLevel", serviceChargeDTO.getRuleLevel());
			return "serviceChargeRuleForm";
			/*return "redirect:/showServiceChargeRuleForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
			}
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				model.put("masterData", scManagementService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				if(serviceChargeDTO.getRuleLevel() ==3)
					model.put("customerProfileList",scManagementService.getCustomerProfiles());
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
				e.printStackTrace();
			}
		}

		return "serviceChargeRuleForm";

	}
	
	@RequestMapping("/editServiceChargeRule.htm")
	public String getServiceChargeRule(SCSearchDTO scSearchDTO, Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		Integer roleId = scSearchDTO.getRoleId();
		Long scRuleId=roleId.longValue();
		Integer ruleLevel = scSearchDTO.getRuleLevel();
		try{
			scManagementService.getServiceChargeRule(scRuleId,model,localeResolver.resolveLocale(request).toString().substring(0, 2));
			if(ruleLevel==3)
			model.put("customerProfileList",scManagementService.getCustomerProfiles());
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			pageLogger(request,response,"EditServiceChargeRule");
		}
		
		return "serviceChargeRuleForm";
		
	}

	@RequestMapping("/viewServiceChargeRule.htm")
	public String viewServiceChargeRule(@RequestParam Long scRuleId, Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		
		try{
			scManagementService.getServiceChargeRule(scRuleId,model,localeResolver.resolveLocale(request).toString().substring(0, 2));
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		finally{
			try {
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("masterData", scManagementService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			pageLogger(request,response,"ViewServiceChargeRule");
			} catch (EOTException e) {
				e.printStackTrace();
			}
		}
		
		return "viewServiceChargeRule";
		
	}
	
	@RequestMapping("/searchServiceChargeRules.htm")
	public String searchServiceChargeRules(SCSearchDTO scSearchDTO,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){
		
		try {				
			
			session.setAttribute("scSearchDTO", scSearchDTO );
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			scManagementService.searchServiceChargeRules(userName,scSearchDTO, model);
			
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			model.put("masterData", scManagementService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			pageLogger(request,response,"ServiceChargeRules");
			}catch(EOTException e){
				
			}
		}

		return "listServiceChargeRules";
		
	}
	
	@RequestMapping("/listServiceChargeRules.htm")
	public String listServiceChargeRules(@RequestParam Integer pageNumber,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){
		try {				
			
			SCSearchDTO scSearchDTO = (SCSearchDTO) session.getAttribute("scSearchDTO" );
			scSearchDTO.setPageNumber(pageNumber);
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			scManagementService.searchServiceChargeRules(userName,scSearchDTO, model);
					
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				model.put("masterData", scManagementService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				pageLogger(request,response,"ServiceChargeRules");
				}catch(EOTException e){
					
				}
			pageLogger(request,response,"ListServiceChargeRules");
		}

		return "listServiceChargeRules";
	}
	
	
	@RequestMapping("/exportToPdfForSCR.htm")
	public void exportToPDFServiceChargerules(SCSearchDTO scSearchDTO,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response) {
	
		List<ServiceChargeRule> results=null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		String serviceChargeRuleName="";
		List<String> txTypeNameList = new ArrayList<String>();
	
		try {				
			
			session.setAttribute("scSearchDTO", scSearchDTO );
			scManagementService.searchServiceChargeRules(userName,scSearchDTO, model);
			Object object = model.get("page"); 	
			Page page=(Page)object;
			results=page.getResults();
			
			for (int i = 0; i < results.size(); i++) {
				serviceChargeRuleName="";
				//serviceChargeRuleName = results.get(i).getServiceChargeRuleName();
				Set<ServiceChargeRuleTxn> serviceChargeRuleTxnsSet = results.get(i).getServiceChargeRuleTxns();
				
				for (ServiceChargeRuleTxn serviceChargeRuleTxn : serviceChargeRuleTxnsSet) {
					
					if (serviceChargeRuleTxn.getSourceType().intValue() == 1) {
						serviceChargeRuleName=serviceChargeRuleName + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Wallet \n";
					}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  2) {
						serviceChargeRuleName=serviceChargeRuleName + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Card \n";
					}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  3) {
						serviceChargeRuleName=serviceChargeRuleName + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Bank Account \n";
					}else if (serviceChargeRuleTxn.getSourceType().intValue() ==  4) {
						serviceChargeRuleName=serviceChargeRuleName + serviceChargeRuleTxn.getTransactionType().getDescription() +" - FI \n";
					}else {
						serviceChargeRuleName=serviceChargeRuleName + serviceChargeRuleTxn.getTransactionType().getDescription() +" - Others \n";
					}					
				}
				txTypeNameList.add(serviceChargeRuleName);
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

		generatePDFReport(EOTConstants.JASPER_SCR_JRXML_NAME, EOTConstants.SCR_REPORT_NAME, results, model, request, response);

	
	}
	
	
	@RequestMapping("/exportToXlsForSCR.htm")
	public void exportToXlsFServiceChargerules(SCSearchDTO scSearchDTO,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response) {
	
		List<ServiceChargeRule> results=null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		HSSFWorkbook wb = null;
		
		try {				
			
			session.setAttribute("scSearchDTO", scSearchDTO );
			scManagementService.searchServiceChargeRules(userName,scSearchDTO, model);
			Object object = model.get("page"); 	
			Page page=(Page)object;
			results=page.getResults();
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", userName);
			model.put("date", dt1);
			wb = wrapper.createSpreadSheetFromListForSCR(results, localeResolver.resolveLocale(request), messageSource, userName, EOTConstants.SCR_DETAILS_PAGE_HEADER);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ EOTConstants.WEB_USER_REPORT_NAME
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
	
	}
	
}
