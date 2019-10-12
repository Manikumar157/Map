package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.BankDTO;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.InterBankSCDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.BankService;
import com.eot.banking.service.LocationService;
import com.eot.banking.utils.Page;

@Controller
public class BankManagementController extends PageViewController {

	@Autowired
	private BankService bankService;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	private LocationService locationService;
	
	@RequestMapping("/showBankManagementForm.htm")
	public String showBankingManagementForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response,HttpSession session){
		BankDTO bankDTO=null;
		int pageNumber=1;
		try {
			bankDTO=new BankDTO();		
			
			Page page=bankService.searchBank(bankDTO,pageNumber,localeResolver.resolveLocale(request).toString());	
			page.requestPage="searchBank.htm";
			model.put("page",page);
		
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
			ex.printStackTrace();
		}finally{
			try {
				model.put("masterData", bankService.getMasterData(pageNumber,localeResolver.resolveLocale(request).toString()));
			} catch (EOTException e) {
				model.put("message", e.getErrorCode());
				e.printStackTrace();
			}
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("bankDTO",bankDTO);
			session.setAttribute("bankDTO",bankDTO);
			pageLogger(request,response,"Bank");
			model.put("bankList",bankService.getBankList());
			model.put("message", request.getParameter("message"));
			
		}
		return "bankManagement";
	}
	
	@RequestMapping("/searchBank.htm")
	public String searchBank(BankDTO bankDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		int pageNumber = 1;
		try{		

			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				bankDTO = (BankDTO) session.getAttribute("bankDTO");
			} else {
				session.setAttribute("bankDTO", bankDTO);
			}	
			Page page=bankService.searchBank(bankDTO,pageNumber,localeResolver.resolveLocale(request).toString());	
			page.requestPage="searchBank.htm";
			model.put("page",page);
			model.put("language",localeResolver.resolveLocale(request) );
			
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {
				model.put("masterData", bankService.getMasterData(pageNumber,localeResolver.resolveLocale(request).toString()));
			} catch (EOTException e) {
				model.put("message", e.getErrorCode());
				e.printStackTrace();
			}
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("bankList",bankService.getBankList());
			model.put("bankDTO",bankDTO);
			pageLogger(request,response,"Bank");
		}

		return "bankManagement";

	}
	
	@RequestMapping("/addBankForm.htm")
	public String showAddBankForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){
		try {
			model.put("bankDTO",new BankDTO());
			model.put("message", request.getParameter("message"));
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;

			model.put("masterData", bankService.getMasterData(pageNumber,localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
			ex.printStackTrace();
		}finally{
			pageLogger(request,response,"Bank");
		}
		return "addBank";
	}


	@RequestMapping("/saveBank.htm")
	public String saveBank(@Valid BankDTO bankDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request) throws EOTException{
		BankDTO bankDTO1=null;
		try {
			
			bankDTO.setCurrencyId(locationService.getCurrencyByCurrencyCode(EOTConstants.SSP_CURRENCY_CODE).getCurrencyId());
			if(result.hasErrors()){
				model.put("bankDTO",bankDTO);
			} else if(bankDTO.getBankId() == null ){

				bankService.addBankDetails(bankDTO,localeResolver.resolveLocale(request).toString());
				model.put("message", "ADD_BANK_SUCCESS");
				//return "redirect:/showBankManagementForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				return "bankManagement";	
			}else{
				bankService.updateBankDetails(bankDTO,localeResolver.resolveLocale(request).toString());	
				model.put("message", "EDIT_BANK_SUCCESS");
				//return "redirect:/showBankManagementForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				return "bankManagement";
			}

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("bankDTO",bankDTO);
			model.put("message",e.getErrorCode());
			
		} catch (Exception e) {
			e.printStackTrace();
			model.put("bankDTO",bankDTO);
			model.put("message","ERROR_9999");
		} finally{
			try {
				int pageNumber=1;
				model.put("bankDTO",new BankDTO());
				bankDTO1 = new BankDTO();
				Page page=bankService.searchBank(bankDTO1,pageNumber,localeResolver.resolveLocale(request).toString());	
				page.requestPage="searchBank.htm";
				model.put("page",page);
				model.put("masterData", bankService.getMasterData(1,localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (EOTException e) {
			}
		}

		return "addBank";
	} 
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/editBankForm.htm")
	public String editBankDetails(BankDTO bankDTO,HttpServletRequest request, Map<String,Object> model,HttpServletResponse response){
		try{
			model.put("bankDTO",bankService.getBankDetails(bankDTO.getBankId(),localeResolver.resolveLocale(request).toString()));
			//@End
		} catch (EOTException e) {
			model.put("message",e.getMessage());
		} catch (Exception e) {
			model.put("message","ERROR_9999");
		} finally{
		
			pageLogger(request,response,"EditBank");
			try {
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("masterData", bankService.getMasterData(1,localeResolver.resolveLocale(request).toString()));

			} catch (EOTException e) {

			}
		}

		return "addBank";
	}

	@RequestMapping("/interBankForm.htm")
	public String showInterBankFeeForm(HttpServletRequest request, Map<String,Object> model,HttpServletResponse response){
		try{
			model.put("interBankFeeDTO",bankService.getInterBankServiceCharges());

		} catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		} finally{
			pageLogger(request,response,"InterBankForm");
		}

		return "interBankFee";
	}

	@RequestMapping("/saveInterBankFee.htm")
	public String saveInterBankFee(InterBankSCDTO interBankFeeDTO,HttpServletRequest request, Map<String,Object> model){
		try{
			
			bankService.saveInterBankServiceCharges(interBankFeeDTO,localeResolver.resolveLocale(request).toString());

			model.put("interBankFeeDTO",interBankFeeDTO);

			model.put("message","INTERBANK_CHARGES_UPDATED");

		} catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		} 

		return "interBankFee";
	}
	
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	@RequestMapping("/addCard.htm")
	public String showAddCardForm(BankDTO bankDTO, HttpServletRequest request, Map<String,Object> model,HttpServletResponse response){
		try{
			model.put("cardDto",bankService.getCardDetailsByBankId(bankDTO.getBankId()));
			//@End

		} catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		} finally{
			pageLogger(request,response,"AddCard");
		}

		return "addCard";
	}
	
	@RequestMapping("/saveCard.htm")
	public String saveCard(CardDto cardDto,HttpServletRequest request, Map<String,Object> model){
		try{

			bankService.saveOrUpdateCard(cardDto,model);
			
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch (Exception e1) {
			e1.printStackTrace();
			model.put("message","ERROR_9999");
		} 

		return "addCard";
	}
	
}
