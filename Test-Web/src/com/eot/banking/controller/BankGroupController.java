package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.dto.BankGroupDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.ApplicationVersionService;
import com.eot.banking.service.BankGroupService;
import com.eot.banking.utils.Page;

@Controller
public class BankGroupController extends PageViewController {

	@Autowired
	private BankGroupService bankGroupService;	
	
	@RequestMapping("/showBankGroup.htm")
	public String showBankGroupForm(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){			
		try{
			model.put("bankGroupDTO",new BankGroupDTO());	
			model.put("message",request.getParameter("message"));
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
			Page page = bankGroupService.getBankGroupList(pageNumber);
			page.requestPage = "showBankGroup.htm";
			model.put("page",page);		   
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"BankGroup");
		}		
		return "bankGroup";	
	}	

	@RequestMapping("/saveBankGroup.htm")
	public String saveBankGroup(@Valid BankGroupDTO bankGroupDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException{
		try{			
			if(result.hasErrors()){
				model.put("bankGroupDTO",bankGroupDTO);
			}else if(bankGroupDTO.getBankGroupId()== null){		
				bankGroupService.addBankGroup(bankGroupDTO);	
				model.put("bankGroupDTO",new BankGroupDTO());
				model.put("message","ADD_BANKGROUP_SUCCESS");
				//return "redirect:/showBankGroup.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			   // return "bankGroup";
			}else{
				bankGroupService.updateBankGroup(bankGroupDTO);		
				model.put("bankGroupDTO",new BankGroupDTO());
				model.put("message","EDIT_BANKGROUP_SUCCESS");
				//return "redirect:/showBankGroup.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				//return "bankGroup";
			}
		}catch(EOTException e){
			model.put("message",e.getErrorCode());	
			model.put("bankGroupDTO",bankGroupDTO);
			return "bankGroup";
		}catch(Exception e){
			model.put("bankGroupDTO",bankGroupDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "bankGroup";
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
				Page page = bankGroupService.getBankGroupList(pageNumber);
				page.requestPage = "showBankGroup.htm";
				model.put("page",page);	
			}catch(Exception e){

				model.put("message",ErrorConstants.SERVICE_ERROR);
			}			  
		}
		return "bankGroup";			
	}

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	@RequestMapping("/editBankGroup.htm")
	public String editBankGroup(Map<String,Object> model,BankGroupDTO bankGroupDTO,HttpServletRequest request,HttpServletResponse response){		
		try{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = bankGroupService.getBank(bankGroupDTO.getBankGroupId(),pageNumber);
			model.put("bankGroupDTO", bankGroupService.getBankGroup(bankGroupDTO.getBankGroupId()));
			//@End
			model.put("page",page);		
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"EditBankGroup");
		}
		return "bankGroup";

	}	
}

