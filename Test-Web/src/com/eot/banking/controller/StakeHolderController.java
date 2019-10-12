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
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.dto.StakeHolderDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.StakeHolderService;

@Controller
public class StakeHolderController extends PageViewController {

	@Autowired
	private  StakeHolderService stakeholderService;
	@Autowired
	private LocaleResolver localeResolver;


	@RequestMapping("/showStakeHolderForm.htm")
	public String showStakeHolderForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){

		try {
			model.put("stakeHolderDTO", new StakeHolderDTO());
			model.put("message", request.getParameter("message"));
			model.put("masterData", stakeholderService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			/*int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
			Page page = stakeholderService.getStakeHolderList(pageNumber);
			page.requestPage = "showStakeHolderForm.htm";
			model.put("page",page);*/
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"StakeHolder");
		}	
		return "stakeholder";
	}


	@RequestMapping("/saveStakeHolderForm.htm")	
	public String saveStakeHolderForm(@Valid StakeHolderDTO stakeHolderDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException{

		try{	

			if(result.hasErrors()){					
				model.put("stakeHolderDTO",stakeHolderDTO);			
			}
			else if(stakeHolderDTO.getStakeholderId() == null){			
				stakeholderService.addStakeHolder(stakeHolderDTO);
				model.put("stakeHolderDTO", new StakeHolderDTO());
				model.put("message", "ADD_STAKE_SUCCESS");	
				//return "redirect:/showStakeHolderForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}else {				
				stakeholderService.updateStakeHolder(stakeHolderDTO);
				model.put("stakeHolderDTO", new StakeHolderDTO());
				model.put("message", "EDIT_STAKE_SUCCESS");
				//return "redirect:/showStakeHolderForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}		
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			model.put("stakeHolderDTO",stakeHolderDTO);
		} catch (Exception e) {
			e.printStackTrace();	
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("stakeHolderDTO",stakeHolderDTO);
		} finally{
			try {
				model.put("masterData", stakeholderService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				if(stakeHolderDTO.getCountryId()!=null)
					model.put("mobilenumberLength",stakeholderService.getMobileNumLength(stakeHolderDTO.getCountryId()) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}

		return "stakeholder";
	}	

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting

	@RequestMapping("/editStakeHolderForm.htm")
	public String editStakeHolderForm(StakeHolderDTO stakeHolderDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		
		try{
			stakeHolderDTO=stakeholderService.getStakeHolderDetails(stakeHolderDTO.getStakeholderId());
			if(stakeHolderDTO!=null)
			model.put("stakeHolderDTO",stakeHolderDTO);
			else
			model.put("stakeHolderDTO",new StakeHolderDTO());
		//@End	
		} catch(EOTException e){
			model.put("message", e.getErrorCode());
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {
				model.put("stakeHolderDTO",stakeHolderDTO);
				model.put("masterData", stakeholderService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				if(stakeHolderDTO.getCountryId()!=null)
					model.put("mobilenumberLength",stakeholderService.getMobileNumLength(stakeHolderDTO.getCountryId()) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditStakeHolder");
		}	
		return "stakeholder";
	}	
	@RequestMapping("/getStakeHolderMobileNumberLength.htm")
	public String getMobileNumberLength(@RequestParam Integer country, Map<String, Object> model)throws EOTException{
		
		
		model.put("mobileNumLength",stakeholderService.getMobileNumLength(country) );
		return "customerMobileNum";
	}

}
