package com.eot.banking.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.dto.CommissionDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.CommissionService;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.TransactionService;
import com.eot.banking.utils.Page;
import com.eot.entity.CustomerProfiles;

@Controller
public class CommissionController  extends PageViewController {
	@Autowired
	TransactionService transactionService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private CommissionService commissionService;
	
	/******************************Added By for Commission***********************************/
	@RequestMapping("/searchMerchantCommission.htm")
	public String viewAddCommissionForm(HttpServletRequest request,Map<String, Object> model,CommissionDTO commissionDTO,HttpServletResponse response){
		try {
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = commissionService.loadCommisionSlabDetails(pageNumber ,commissionDTO );
			page.requestPage = "searchMerchantCommission.htm";
			model.put("page", page);
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2) );
			if(commissionDTO.getBankId() != null){
				//model.put("Profile", customerService.getCustomerProfiles(commissionDTO.getBankId()));
				model.put("Profile", commissionService.loadCustomerListForCommission(commissionDTO));
			}
		} catch (EOTException e) {
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				System.out.println(model);
			} catch (EOTException e1) {
				e1.printStackTrace();
			}
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2) );
			pageLogger(request,response,"Customer");
		}	
		model.put("commissionDTO",commissionDTO);
		return "addCommission";
	}
	
	@RequestMapping("/showAddCommissionForm.htm")
	public String showAddCommissionForm(Map<String,Object> model,CommissionDTO commissionDTO,HttpServletRequest request,HttpServletResponse response){
		try{      

			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2) );
			model.put("commissionDTO",commissionDTO);    
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);        
		}finally{
			model.put("lang",localeResolver.resolveLocale(request).toString().substring(0, 2) );	
			pageLogger(request,response,"addCommission");
		}
		return "addCommissionForm";
	}
	
	@RequestMapping("/editCommission.htm")
	public String modifyCommission(@RequestParam Long commissionId,Map<String,Object> model,CommissionDTO commissionDTO,HttpServletRequest request,HttpServletResponse response){
		try{      
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = commissionService.loadCommisionSlabDetails(pageNumber , commissionDTO);
			page.requestPage = "showAddCommissionForm.htm";
			model.put("page", page);
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2) );
			commissionDTO = commissionService.getCommisionDetails(commissionId);
			model.put("commissionDTO",commissionDTO);   //getCustomerListForCommission 
			//model.put("Profile", customerService.getCustomerProfiles(commissionDTO.getBankId()));
			model.put("Profile", commissionService.loadCustomerListForCommission(commissionDTO));
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);        
		}finally{
			model.put("lang",localeResolver.resolveLocale(request).toString().substring(0, 2) );	
			pageLogger(request,response,"EditCountry");
		}
		return "editCommission";
	}



	@RequestMapping("/updateCommissionSlabs.htm")
	public String updateMuniCommissionSlabs(CommissionDTO commissionDTO , Map<String, Object> model,HttpServletRequest request){
		try {
			System.out.println("Reached Update controller");
			commissionService.updateCommisionSlabs(commissionDTO);
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = commissionService.loadCommisionSlabDetails(pageNumber ,new CommissionDTO());
			page.requestPage = "showAddCommissionForm.htm";
			model.put("page", page);
			model.put("message", "COMMISION_SLAB_UPDATED_SUCCESSFULLY");
			model.put("commissionDTO", new CommissionDTO());

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("commissionDTO",commissionDTO);
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("commissionDTO",commissionDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2) );
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				//model.put("Profile",customerService.getCustomerProfiles(commissionDTO.getBankId()));
				model.put("Profile", commissionService.loadCustomerListForCommission(commissionDTO));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
				e.printStackTrace();
			}
		}
		return "addCommission";
	}
	
	@RequestMapping("/getCustomerProfilesForCommision.htm")
	public String loadMuniCustomerProfiles(@RequestParam Integer bankId, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			
			
			
			List<CustomerProfiles> customerProfileList= customerService.loadCustomerProfiles(bankId);
			System.out.println("before "+customerProfileList);
			for(int i=0;i<customerProfileList.size();i++)
			{
				CustomerProfiles customerProfile = customerProfileList.get(i);
				int profileId= (customerProfile.getProfileId()).intValue();
				/*if(profileId==1)
				{
					customerProfileList.remove(customerProfile);
				}*/
				
			}
			System.out.println("after "+customerProfileList);
			model.put("entity", "profileId");
			model.put("id", "profileId");
			model.put("value", "profileName");
			model.put("list", customerProfileList);
		} catch (EOTException e) {
		} finally{
			pageLogger(request,response,"getProfiles");
		}	
		return "combo";
	}
	
	@RequestMapping("/saveCommissionSlabs.htm")
	public String saveMuniCommissionSlabs(CommissionDTO commissionDTO , Map<String, Object> model,HttpServletRequest request){
		String viewPage = null;
		try {
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			
			commissionService.saveCommisionSlabs(commissionDTO);
			Page page = commissionService.loadCommisionSlabDetails(pageNumber ,new CommissionDTO() );
			page.requestPage = "searchMerchantCommission.htm";
			model.put("message", "COMMISION_SLAB_ADDED_SUCCESSFULLY");
			model.put("commissionDTO", new CommissionDTO());
			model.put("page", page);
			viewPage = "addCommission";


		} catch (EOTException e) {
			e.printStackTrace();
			if(commissionDTO.getTransactions()[0] !=null){
				Integer transactionTypeId = commissionDTO.getTransactions()[0];
				commissionDTO.setTransactionTypeId(transactionTypeId);
			}
			model.put("commissionDTO",commissionDTO);
			model.put("message", e.getErrorCode());
			viewPage = "addCommissionForm";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("commissionDTO",commissionDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
			viewPage = "addCommissionForm";
		}finally{
			try {
				
				List<CustomerProfiles> customerProfileList= customerService.loadCustomerProfiles(commissionDTO.getBankId());
				System.out.println("before "+customerProfileList);
				for(int i=0;i<customerProfileList.size();i++)
				{
					CustomerProfiles customerProfile = customerProfileList.get(i);
					int profileId= (customerProfile.getProfileId()).intValue();
					if(profileId==1)
					{
						customerProfileList.remove(customerProfile);
					}
					
				}
				System.out.println("after "+customerProfileList);
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2) );
				model.put("Profile", customerProfileList);
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
				e.printStackTrace();
			}

		}
		return viewPage ;
	}



}
