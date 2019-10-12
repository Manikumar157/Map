package com.eot.banking.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.controller.PageViewController;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.WebUser;

@Service("commonService")
public class CommonServiceImpl extends PageViewController implements CommonService {
	
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private LocationService locationService;
	/** The bank teller service. */
	@Autowired
	private BankTellerService bankTellerService;
	@Autowired
	private BusinessPartnerService businessPartnerService; 
	
	@Override
	public CustomerDTO viewCustomer(Long customerId, Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response, String viewName) {
			
		CustomerDTO dto = null;
		try{

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			dto = customerService.getCustomerDetails(customerId,auth.getName());
			dto.setLanguage(customerService.getLanguageDescription(dto.getLanguage()));
			model.put("customerDTO",dto);

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,viewName);
		}
		return dto;
	}
	
	public void showUserManagementForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response) throws EOTException,Exception{

		try {
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser weUser =webUserService.getWebUserByUserName(userName);
			
			Integer roleId= weUser.getWebUserRole().getRoleId();
			
			Integer bankId = null;
			Integer seniorPartnerId=null;
			BusinessPartner businessPartner=null;
			if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2|| weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L3
					|| weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN)
				businessPartner= webUserService.getBusinessPartnerByUserName(userName);
			
			if(businessPartner!=null)
			{
				seniorPartnerId= businessPartner.getId()!=null?businessPartner.getId():null;
			}
			
			int pageNumber = request.getParameter("pageNumber") != null && request.getParameter("pageNumber") != ""? new Integer(request.getParameter("pageNumber")) : 1 ;
			model.put("masterData", webUserService.getMasterData(userName,pageNumber));
			model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("lang",localeResolver.resolveLocale(request).toString());	
			model.put("language", locationService.getLanguageList());
			model.put("userId", request.getParameter("userId"));
			model.put("action", "add");
			if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L3|| 
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN)
			{
				if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN||
						weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN) {
					BankTellers bankTellers = bankTellerService.findByUserName(userName);
					if(bankTellers!=null){
						bankId = bankTellers.getBank().getBankId();
					}
				}else {
					BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
					 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				}
				model.put("businessNameList",  webUserService.getAllBusniessPartnerName(roleId,seniorPartnerId,bankId));
			}
		}finally{
			model.put("mobilenumberLength",customerService.getMobileNumLength(EOTConstants.DEFAULT_COUNTRY));
		}

	}

}
