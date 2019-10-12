/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: AgentController.java
*
* Date Author Changes
* Oct 16, 2018 K Vineeth Created
*/
package com.eot.banking.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.common.FieldExecutiveEnum;
import com.eot.banking.dto.BankAccountDTO;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.CustomerProfileDTO;
import com.eot.banking.dto.QRCodeDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.CommonService;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.TransactionService;
import com.eot.banking.service.WebUserService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.BankTellers;
import com.eot.entity.City;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.Quarter;
import com.eot.entity.WebUser;


/**
 * The Class AgentController.
 *
 * @author K Vineeth
 */
@Controller
public class AgentController extends PageViewController {

	/** The transaction service. */
	@Autowired
	TransactionService transactionService;
	
	/** The customer service. */
	@Autowired
	private CustomerService customerService;
	
	/** The web user service. */
	@Autowired
	private WebUserService webUserService;
	
	/** The locale resolver. */
	@Autowired
	private LocaleResolver localeResolver;
	
	/** The wrapper. */
	@Autowired
	private ExcelWrapper wrapper;
	
	/** The message source. */
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CommonService commonService;

	
	// vineeth changes, on 29-08-2018
/*	@RequestMapping("/showAgentForm.htm")
	public String addAgentRequest(CustomerDTO customerDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){
		
	//	model.put("type",customerDTO.getType());
	//	request.setAttribute("type", customerDTO.getType());
		
		//	return "addCustomer";
	///	request.setAttribute("type",customerDTO.getType());
     //   request.setAttribute("param2", "bar");
		return "redirect:/showAgentForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		
	}*/
	
	/**
	 * Show agent registration form.
	 *
	 * @param customerDTO the customer DTO
	 * @param request the request
	 * @param model the model
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/addAgentRequest.htm")
	public String showAgentRegistrationForm(CustomerDTO customerDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		try {			
			String mobileNumber = request.getParameter("mobileNumber");
			customerDTO.setMobileNumber(mobileNumber);								
			if( roleId != 24 && roleId != 25 && roleId != 26) 
				customerDTO = customerService.getUserForProof(auth.getName(),customerDTO);				
			else 
				customerDTO = customerService.getUserForProofByPartner(auth.getName(),customerDTO);				
			model.put("action","ADD");
			model.put("message", request.getParameter("message"));
			model.put("language",localeResolver.resolveLocale(request) );
		} catch (EOTException e) {
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {
				if( roleId != 24 && roleId != 25 && roleId != 26) 					
					model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),request.getParameter("type")));
				else 				
					model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),request.getParameter("type")));				
				model.put("customerDTO",customerDTO);
				Integer countryId = customerDTO.getCountryId() != null ? customerDTO.getCountryId() : EOTConstants.DEFAULT_COUNTRY ;
				model.put("City",countryId != null?customerService.getCityList(countryId):null);
				model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
				/*commenting to make a default country as South Sudan, by vineeth on 14-11-2018
				model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString()));*/
				pageLogger(request,response,"Customer");			
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}	
		return "addAgent";
	}
	
	/**
	 * Save Agent.
	 *
	 * @param customerDTO the customer DTO
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	// vineeth changes over
	@RequestMapping("/saveAgent.htm")
	public String saveCustomer(@Valid CustomerDTO customerDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		String messageRegister="";
		String messageUpdation="";
		String message="";
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String countryId = "";
		String bankGroupId = "";
		String fromDate = "";
		String toDate = "";
		String businessName = "";
		String custType = customerDTO.getType()+"";
		boolean flag=false;	
		Integer kycStatus = null;
		try {
			message = customerDTO.getType()==0?"CUSTOMER":customerDTO.getType()==1?"AGENT":customerDTO.getType()==2?"SOLE_MERCHANT":customerDTO.getType()==3?"AGENT_SOLEMERCHANT":"";
			if(result.hasErrors()){
				model.put("customerDTO",customerDTO);				
				return "addAgent";
			}					
			if(customerDTO.getCustomerId() == null ){
				Long customerId = customerService.saveCustomer(customerDTO);
				customerDTO.setCustomerId(customerId);
				messageRegister = message+"_REG_SUCCESS";
				customerDTO.setOnBordedBy(auth.getName());
				model.put("message",messageRegister);		
			}else{
				customerService.updateCustomer(customerDTO);
				messageUpdation =  message+"_EDIT_SUCCESS";
				model.put("message",messageUpdation);
			}
			if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
				toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			}
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,null,businessName,kycStatus,null);
			model.put("page",page );
			customerDTO.setBusinessName(null);
		} catch (EOTException e) {
			flag = true;
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "addAgent";
		} catch (Exception e) {
			flag = true;
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "addAgent";
		} finally{	
			
			if(flag == true){
				try {
					if( roleId != 24 && roleId != 25 && roleId != 26) {
						model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),customerDTO.getType().toString()));
					}else {
							model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),customerDTO.getType().toString()));
					}
					Integer countryID = customerDTO.getCountryId() != null ? customerDTO.getCountryId() : EOTConstants.DEFAULT_COUNTRY ;
					model.put("City",customerService.getCityList(countryID));
					model.put("quarter",customerDTO.getCity() != null?customerService.getQuarterList(customerDTO.getCity()):null);
					model.put("questionList",customerDTO.getLanguage() != null?customerService.getCustomerQuestions(customerDTO.getLanguage()):null); 
					model.put("mobileNumLength",customerService.getMobileNumLength(countryID));
					
				}catch (EOTException e) {
					model.put("message",ErrorConstants.SERVICE_ERROR);
				}												
			}else {
				/*Customer customer=customerService.getCustomerByCustomerId(customerDTO.getCustomerId());
				customerDTO.setCityName(customer.getCity().getCity());
				customerDTO.setQuarterName(customer.getQuarter().getQuarter());
				customerDTO.setProfileName(customer.getCustomerProfiles().getProfileName());*/
				City city = customerService.getCity(customerDTO.getCityId());
				Quarter quarter = customerService.getQuarter(customerDTO.getQuarterId());
				CustomerProfiles CustomerProfiles = customerService.getCustomerProfiles(customerDTO.getCustomerProfileId());				
				customerDTO.setCityName(city.getCity());
				customerDTO.setQuarterName(quarter.getQuarter());
				customerDTO.setProfileName(CustomerProfiles.getProfileName());
			}
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);
			model.put("language",localeResolver.resolveLocale(request));
			model.put("custType",customerDTO.getType());
			model.put("customerDTO",customerDTO);
		}
		/*return "viewAgent";*/
		return "viewAgents";
	}

/**
 * Edits the customer.
 *
 * @param model the model
 * @param request the request
 * @param response the response
 * @return the string
 */
/*	@RequestMapping("/mobileNumLenthRequest.htm")
	public String getMobileNumLength(@RequestParam Integer country,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			model.put("mobileNumLength", customerService.getMobileNumLength(country));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			pageLogger(request,response,"mobileNumLength");
		}	

		return "customerMobileNum";


	}
*/
	@RequestMapping("/editAgent.htm")
	public String editCustomer(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		CustomerDTO dto = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
		Integer roleId = webUserService.getRoleId(auth.getName());
		try{			
			dto = customerService.getCustomerDetails(Long.parseLong(request.getParameter("custiId")),auth.getName());		
			if(roleId != 24 && roleId != 25 && roleId != 26) 
				dto = customerService.getUserForProof(auth.getName(),dto);
			else 
				dto = customerService.getUserForProofByPartner(auth.getName(),dto);			
			model.put("customerDTO",dto);
		}catch(EOTException e){
			e.printStackTrace();
			model.put("customerDTO",new CustomerDTO());
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {		
				if( roleId != 24 && roleId != 25 && roleId != 26) 		
					model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),dto.getType().toString()));
				else					
					model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),dto.getType().toString()));				
				model.put("City",customerService.getCityList(dto.getCountryId()));
				model.put("quarter",customerService.getQuarterList(dto.getCityId()));
				model.put("mobileNumLength",customerService.getMobileNumLength(dto.getCountryId()));
				/*commenting to make a default language as English, by vineeth on 14-11-2018
				model.put("language",localeResolver.resolveLocale(request));
				model.put("questionList",customerService.getCustomerQuestions(dto.getLanguage())); */
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditAgent");
		}
		return "agent";
	}

	 /**
 	 * View customer.
 	 *
 	 * @param customerDTO the customer DTO
 	 * @param model the model
 	 * @param request the request
 	 * @param response the response
 	 * @return the string
 	 */
 
	@RequestMapping("/viewAgent.htm")
	public String viewCustomer(CustomerDTO customerDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){	
		CustomerDTO dto =new CustomerDTO();
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			dto = customerService.getCustomerDetails(customerDTO.getCustomerId(),auth.getName());
			dto.setLanguage(customerService.getLanguageDescription(dto.getLanguage()));
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			model.put("customerDTO",dto);
			model.put("custType",customerDTO.getType());
			pageLogger(request,response,"viewAgent");
		}
		return "viewAgent";
	}

	/**
	 * Search Agent.
	 *
	 * @param type the type
	 * @param model the model
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 * @return the string
	 */
//	@RequestMapping("/searchAgent.htm")
//	public String searchCustomer(@RequestParam Integer type,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
//		//String customerName = "";
//		String firstName = "";
//		String middleName = "";
//		String lastName = "";
//		String mobileNumber = "";
//		String bankId = "";
//		String branchId = "";
//		String countryId = "";
//		String bankGroupId = "";
//		String fromDate = "";
//		String toDate = "";
//		String custType = "";
//		String businessName = "";
//		
//		Map<String,Object> masterData = null;
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
//		Integer roleId = webUserService.getRoleId(auth.getName());
//		try{
//			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
//
//			firstName = request.getParameter("firstName");
//			lastName = request.getParameter("lastName");
//			middleName = request.getParameter("middleName");
//			mobileNumber = request.getParameter("mobileNumber");
//			bankId = request.getParameter("bankId");
//			branchId = request.getParameter("branchId");
//			countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
//			bankGroupId = request.getParameter("bankGroupId");
//			fromDate = request.getParameter("fromDate");
//			toDate = request.getParameter("toDate");
//			custType = request.getParameter("custTypeCode") != null ?request.getParameter("custTypeCode") :  (type == null ? "" : type+"");
//			businessName = request.getParameter("businessName");
//			
//			session.setAttribute("firstName", firstName);
//			session.setAttribute("middleName", middleName);
//			session.setAttribute("lastName", lastName);
//			session.setAttribute("mobileNumber", mobileNumber );
//			session.setAttribute("bankId", bankId );
//			session.setAttribute("branchId", branchId );
//			session.setAttribute("countryId", countryId );
//			session.setAttribute("bankGroupId", bankGroupId );
//			session.setAttribute("fromDate", fromDate );
//			session.setAttribute("toDate", toDate);
//			session.setAttribute("custType", custType);
//			session.setAttribute("businessName", businessName);
//
//			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
//			Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,null,businessName);
//			page.requestPage = "searchAgentPage.htm";
//			model.put("page",page);	
//		}catch(EOTException e) {
//			e.printStackTrace();
//			model.put("message", e.getErrorCode());
//		} catch (Exception e) {
//			e.printStackTrace();
//			model.put("message",ErrorConstants.SERVICE_ERROR);
//		}finally{
//			try {
//				if(roleId != 24 && roleId != 25 && roleId != 26) {
//					masterData = customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),custType);
//				}else {
//					masterData = customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),custType);
//				}			
//				model.put("language",localeResolver.resolveLocale(request) );
//				if (branchId != null &&  branchId != ""){
//					if(branchId.equalsIgnoreCase("select")){
//						branchId = "";
//					}
//					if(bankId!=null&&bankId!=""&&bankId!="select")
//						masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId)));
//				}
//				model.put("masterData",masterData);
//				model.put("customerDTO", new CustomerDTO());
//				model.put("firstName", firstName);
//				model.put("middleName", middleName);
//				model.put("lastName", lastName);
//				model.put("mobileNumber",mobileNumber);
//				model.put("bankId",bankId);
//				model.put("branchId",branchId);
//				model.put("countryId", countryId);
//				model.put("bankGroupId", bankGroupId);
//				model.put("fromDate",fromDate);
//				model.put("toDate",toDate);	
//				model.put("customerList",customerService.getCustomerList());
//				model.put("custType",custType);	
//			} catch (EOTException e) {
//				model.put("message",ErrorConstants.SERVICE_ERROR);
//			}
//			pageLogger(request,response,"SearchCustomer");
//		}
//		return "viewAgents";
//	}
	
	@RequestMapping("/searchAgent.htm")
	public String searchCustomer(@RequestParam Integer type,CustomerDTO customerDTO ,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
		
		if(null == customerDTO)
			customerDTO = new CustomerDTO();
		int pageNumber=1;

		Map<String,Object> masterData = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
		Integer roleId = webUserService.getRoleId(auth.getName());

		String firstName = "";
		String middleName = "";
		String lastName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String countryId = "";
		String bankGroupId = "";
		String fromDate = "";
		String toDate = "";
		String custType = "";
		String businessName = "";
		Integer kycStatus = null;

		try {
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			middleName = request.getParameter("middleName");
			mobileNumber = request.getParameter("mobileNumber");
			bankId = request.getParameter("bankId");
			branchId = request.getParameter("branchId");
			countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
			bankGroupId = request.getParameter("bankGroupId");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			custType = request.getParameter("custType") != null ?request.getParameter("custType") :  (type == null ? "" : type+"");
			businessName = request.getParameter("businessName");
			
			String status = request.getParameter("customerKycStatus");
			
			if(null != status && !"".equals(status)) {
				kycStatus = Integer.parseInt(status);
			}
			if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
				toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			}
			if(roleId != 24 && roleId != 25 && roleId != 26) {
				masterData = customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),custType);
			}else {
				masterData = customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),custType);
			}			
			model.put("language",localeResolver.resolveLocale(request) );
			if (branchId != null &&  branchId != ""){
				if(branchId.equalsIgnoreCase("select")){
					branchId = "";
				}
				if(bankId!=null&&bankId!=""&&bankId!="select")
					masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId)));
			}
			
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,null,businessName,kycStatus,null);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
			
		} catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}
		catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			model.put("customerDTO",customerDTO);
			//session.setAttribute("customerDTO", customerDTO);

			model.put("masterData",masterData);
			model.put("firstName", firstName);
			model.put("middleName", middleName);
			model.put("lastName", lastName);
			model.put("mobileNumber",mobileNumber);
			model.put("bankId",bankId);
			model.put("branchId",branchId);
			model.put("countryId", countryId);
			model.put("bankGroupId", bankGroupId);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
			model.put("customerList",customerService.getCustomerList());
			model.put("custType",custType);
			
		}
		pageLogger(request,response,"SearchCustomer");
		return "viewAgents";
	}

	/**
	 * Search agent page.
	 *
	 * @param model the model
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/searchAgentPage.htm")
	public String searchAgentPage(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

	
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String countryId = "";
		String bankGroupId = "";
		String fromDate = "";
		String toDate = "";
		String custType = "";
		String businessName = "";
		Integer kycStatus = null;
		
		Map<String,Object> masterData = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
		Integer roleId = webUserService.getRoleId(auth.getName());
		try{
			 Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;			 
			 firstName = session.getAttribute("firstName") == null ? "" : session.getAttribute("firstName").toString() ;
			 middleName = session.getAttribute("middleName") == null ? "" : session.getAttribute("middleName").toString() ;
			 lastName = session.getAttribute("lastName") == null ? "" : session.getAttribute("lastName").toString() ;
			 mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			 bankId =  session.getAttribute("bankId") == null ? "" : session.getAttribute("bankId").toString() ;
			 branchId =  session.getAttribute("branchId") == null ? "" : session.getAttribute("branchId").toString() ;
			 countryId = session.getAttribute("countryId") == null ? "" : session.getAttribute("countryId").toString() ;
			 bankGroupId = session.getAttribute("bankGroupId") == null ? "" : session.getAttribute("bankGroupId").toString() ;
			 fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
		     toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;
			 businessName = session.getAttribute("businessName") == null ? "" : session.getAttribute("businessName").toString() ;
			 custType =  request.getParameter("custType") == null ? "" : request.getParameter("custType").toString();
			 String status = request.getParameter("customerKycStatus");
				
			if(null != status && !"".equals(status)) {
				kycStatus = Integer.parseInt(status);
			}
			if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
				toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			}
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,null,businessName,kycStatus,null);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			page.requestPage = "searchAgentPage.htm";
			model.put("page",page);	
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
			
				if(roleId != 24 && roleId != 25 && roleId != 26) {
					masterData = customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),custType);
				}else {
					masterData = customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),custType);
				}
				model.put("masterData",masterData );
				model.put("language",localeResolver.resolveLocale(request) );
				if (branchId != null &&  branchId != ""){
					if(branchId.equalsIgnoreCase("select")){
						branchId = "";
					}
					if(bankId!=null&&bankId!=""&&bankId!="select")
						masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId)));
				}
				model.put("masterData", masterData);
				model.put("customerDTO", new CustomerDTO());
				model.put("firstName", firstName);
				model.put("middleName", middleName);
				model.put("lastName", lastName);
				model.put("mobileNumber",mobileNumber);
				model.put("bankId",bankId);
				model.put("branchId",branchId);
				model.put("countryId", countryId);
				model.put("bankGroupId", bankGroupId);
				model.put("fromDate",fromDate);
				model.put("toDate",toDate);	
				model.put("customerList",customerService.getCustomerList());
				model.put("custType",custType);	
				model.put("businessName",businessName);	
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchAgentPage");
		}

		return "viewAgents";

	}

	/*@RequestMapping(value = "/exportToXLSForAgentDetails.htm")
	public String exportToXLSForCustomerDetails(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws EOTException {

		String viewPage = null;
		List list = null;
		HSSFWorkbook wb = null;

		try {
			String customerName =  session.getAttribute("customerName") == null ? "" : session.getAttribute("customerName").toString() ;
			String mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			String bankId =  session.getAttribute("bankId") == null ? "" : session.getAttribute("bankId").toString() ;
			String branchId =  session.getAttribute("branchId") == null ? "" : session.getAttribute("branchId").toString() ;
			String countryId = session.getAttribute("countryId") == null ? "" : session.getAttribute("countryId").toString() ;
			String bankGroupId = session.getAttribute("bankGroupId") == null ? "" : session.getAttribute("bankGroupId").toString() ;
			String fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
			String toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;
			//change vinod
			String custType = session.getAttribute("custType") == null ? "" : session.getAttribute("custType").toString() ;
			//change vinod
			list = customerService.exportToXLSForCustomerDetails(customerName,mobileNumber,bankId,branchId,countryId,bankGroupId,fromDate,toDate,model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK){
				BankTellers teller = customerService.getBankTeller(userName);
				wb = wrapper.createSpreadSheetFromListForCustomerDetails(list,localeResolver.resolveLocale(request),messageSource,teller.getBank().getBankName(),fromDate,toDate,webUser.getFirstName());
			}else{
				wb = wrapper.createSpreadSheetFromListForCustomerDetails(list,localeResolver.resolveLocale(request),messageSource,null,fromDate,toDate,webUser.getFirstName());
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
			return "viewCustomers";
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//@Start, by Murari, dated : 03-08-2018, purpose bug 5716
				model.put("customerDTO", new CustomerDTO());
				//@End
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return viewPage;
	}*/

	/**
	 * Export to XLS for customer account details.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @param session the session
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/exportToXLSForAgentAccountDetails.htm")
	public String exportToXLSForCustomerAccountDetails(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws Exception {

		String viewPage = null;
		List list = null;
		HSSFWorkbook wb = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
		Integer roleId = webUserService.getRoleId(auth.getName());
		String custType ="";
		try {
			String customerName =  session.getAttribute("customerName") == null ? "" : session.getAttribute("customerName").toString() ;
			String mobileNumber = session.getAttribute("mobileNumber") == null ? "" : session.getAttribute("mobileNumber").toString() ;
			String bankId =  session.getAttribute("bankId") == null ? "" : session.getAttribute("bankId").toString() ;
			String branchId =  session.getAttribute("branchId") == null ? "" : session.getAttribute("branchId").toString() ;
			String countryId = session.getAttribute("countryId") == null ? "" : session.getAttribute("countryId").toString() ;
			String bankGroupId = session.getAttribute("bankGroupId") == null ? "" : session.getAttribute("bankGroupId").toString() ;
			String fromDate = session.getAttribute("fromDate") == null ? "" : session.getAttribute("fromDate").toString() ;
			String toDate = session.getAttribute("toDate") == null ? "" : session.getAttribute("toDate").toString() ;
			custType =  session.getAttribute("custType") == null ? "" : session.getAttribute("custType").toString() ;
			list = customerService.exportToXLSForCustomerDetails(customerName,mobileNumber,bankId,branchId,countryId,bankGroupId,fromDate,toDate,model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK){
				BankTellers teller = customerService.getBankTeller(userName);
				wb = wrapper.createSpreadSheetFromListForCustomerAccountDetails(list,localeResolver.resolveLocale(request),messageSource,teller.getBank().getBankName(),fromDate,toDate,webUser.getFirstName());
			}else{
				wb = wrapper.createSpreadSheetFromListForCustomerAccountDetails(list,localeResolver.resolveLocale(request),messageSource,null,fromDate,toDate,webUser.getFirstName());
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
			return "viewCustomers";
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//@Start, by Murari, dated : 03-08-2018, purpose bug 5716
				model.put("customerDTO", new CustomerDTO());
				//@End
				if(roleId != 24 && roleId != 25 && roleId != 26) {
					model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),custType));
				}else {
					model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),custType));
				}
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return viewPage;
	}



/**
 * Change customer status.
 *
 * @param customerId the customer id
 * @param status the status
 * @param model the model
 * @param request the request
 * @param response the response
 * @return the string
 */
/*	@RequestMapping("/resetLoginPin.htm")
	@ResponseBody
	public String resetLoginPin(@RequestParam Long customerId ,HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){

		try{
			customerService.resetCustomerPin(customerId);
			return "RESET_PIN_SUCCESS";	
		}catch(EOTException e) {
			e.printStackTrace();
			if(e.getErrorCode().equals(ErrorConstants.INVALID_CUSTOMER))
			{
				 return "No Customer Found.";
			}
			if(e.getErrorCode().equals(ErrorConstants.INVALID_APPLICATION))
			{
				return "Invalid application.";
			}else {
				return e.getErrorCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			//model.put( "message" , ErrorConstants.SERVICE_ERROR ) ;
			return  "System Error.Please Try Again." ;
		} finally{
			pageLogger(request,response,"ResetLoginPin");
		}	

		
	}

	@RequestMapping("/reinstallApplication.htm")
	@ResponseBody
	public String reinstallApplication(@RequestParam Long customerId,HttpServletRequest request , Map<String,Object> model,HttpServletResponse response){
		
		try{
			customerService.reinstallApplication(customerId);
			//model.put( "message" , "REINSTALLED_APP_SUCCESS") ;	
			return EOTConstants.REINSTALLED_APP_SUCCESS;
		}catch(EOTException e) {
			e.printStackTrace();
			//model.put( "message" ,e.getErrorCode() ) ;
			if(e.getErrorCode().equals("ERROR_1024"))
				return "Invalid application.";
			return e.getErrorCode();
		} catch (Exception e) {
			e.printStackTrace();
			//model.put( "message" , ErrorConstants.SERVICE_ERROR ) ;
			return ErrorConstants.SYSTEM_ERROR;
		} finally{
			pageLogger(request,response,"ReinstallApplication");
		}	

	}

	@RequestMapping("/changeApplicationStatus.htm")
	public String changeApplicationStatus(@RequestParam Long customerId, @RequestParam Integer status ,HttpServletRequest request , Map<String,Object> model,HttpServletResponse response){

		String msg = "" ;
		try{

			customerService.changeApplicationStatus(customerId,status);

			if(status == EOTConstants.APP_STATUS_BLOCKED){
				msg = "APP_BLOCKED_SUCCESS" ;	
			}
			if(status == EOTConstants.APP_STATUS_ACTIVATED){
				msg = "APP_UNBLOCKED_SUCCESS" ;	
			}

		}catch(EOTException e) {
			e.printStackTrace();
			msg = e.getErrorCode() ;
		} catch (Exception e) {
			e.printStackTrace();
			msg = ErrorConstants.SERVICE_ERROR ;
		} finally{
			pageLogger(request,response,"ReinstallApplication");
		}	


		return "redirect:viewCustomer.htm?customerId="+customerId+"&message="+msg+"&csrfToken=" + request.getSession().getAttribute("csrfToken");

	}
*/
	@RequestMapping("/changeAgentStatus.htm")
	public String changeCustomerStatus(CustomerDTO cuDto, Map<String,Object> model,HttpServletRequest request ,HttpServletResponse response){
		Long customerId = cuDto.getCustomerId();
		Integer status = cuDto.getAppStatus();
		String msg = "" ;
		String view="viewAgent";
		String reasonForDeActivate = "" ;
		try{
			reasonForDeActivate = request.getParameter("deActivateComment");
			customerService.changeCustomerStatus(customerId,status,reasonForDeActivate);

			if(status == EOTConstants.CUSTOMER_STATUS_ACTIVE){
				msg = "ACTIVATED_SUCCESS" ;	
			}
			if(status == EOTConstants.CUSTOMER_STATUS_DEACTIVATED){
				msg = "DE_ACTIVATED_SUCCESS" ;	
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			cuDto = customerService.getCustomerDetails(customerId,auth.getName());
			cuDto.setLanguage(customerService.getLanguageDescription(cuDto.getLanguage()));
		}catch(EOTException e) {
			e.printStackTrace();
			msg =  e.getErrorCode() ;
		} catch (Exception e) {
			e.printStackTrace();
			msg = ErrorConstants.SERVICE_ERROR ;
		} finally{
			model.put("customerDTO",cuDto);
			model.put("message", msg);
			pageLogger(request,response,"ChangeCustomerStatus");
			
		}	
		
		//commonService.viewCustomer(customerId,model, request,response,"viewCustomer");
		
//		return "redirect:viewCustomer.htm?customerId="+customerId+"&message="+msg+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
		return view;
	}
	
	@RequestMapping("/changeAgentApplicationStatus.htm")
	public String changeApplicationStatus(CustomerDTO cuDto,HttpServletRequest request , Map<String,Object> model,HttpServletResponse response) throws EOTException{
		Long customerId = cuDto.getCustomerId();
		Integer status = cuDto.getAppStatus();
		String msg = "" ;
		String reasonForBlock = "" ;
		try{
			reasonForBlock = request.getParameter("rejectComment");
			customerService.changeApplicationStatus(customerId,status,reasonForBlock);

			if(status == EOTConstants.APP_STATUS_BLOCKED){
				msg = "APP_BLOCKED_SUCCESS" ;	
			}
			if(status == EOTConstants.APP_STATUS_ACTIVATED){
				msg = "APP_UNBLOCKED_SUCCESS" ;	
			}

		}catch(EOTException e) {
			e.printStackTrace();
			msg = e.getErrorCode() ;
		} catch (Exception e) {
			e.printStackTrace();
			msg = ErrorConstants.SERVICE_ERROR ;
		} finally{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Integer roleId = webUserService.getRoleId(auth.getName());
					
				CustomerDTO dto = customerService.getCustomerDetails(cuDto.getCustomerId(),auth.getName());
				dto.setLanguage(customerService.getLanguageDescription(dto.getLanguage()));
				model.put("customerDTO",dto);
				model.put("message",msg);
			pageLogger(request,response,"ReinstallApplication");
		}
		return "viewAgent";
		/*return "redirect:viewCustomer.htm?customerId="+customerId+"&message="+msg+"&csrfToken=" + request.getSession().getAttribute("csrfToken");*/
	}

/**
 * Edits the customer profiles.
 *
 * @param model the model
 * @param request the request
 * @param customerProfileDTO the customer profile DTO
 * @param response the response
 * @return the string
 */
/*
	@RequestMapping("/getPhoto.htm")
	public void getPhoto(@RequestParam Long customerId, @RequestParam String type, HttpServletRequest request, HttpServletResponse response){

		System.out.println(customerId +"   --  " + type );


		try {
			byte[] photo = customerService.getPhotoDetails(customerId, type) ;			

			OutputStream os = response.getOutputStream();
			String idProof=messageSource.getMessage("LABEL_ID_PROOF_NOT_FOUND",null,localeResolver.resolveLocale(request));
			String signature=messageSource.getMessage("LABEL_SIGNATURE_NOT_FOUND",null,localeResolver.resolveLocale(request));
			String profilePhoto=messageSource.getMessage("LABEL_PROFILE_PHOTO_NOT_FOUND",null,localeResolver.resolveLocale(request));
			String addressProof=messageSource.getMessage("LABEL_ADDRESS_PROOF_NOT_FOUND",null,localeResolver.resolveLocale(request));
			if(photo!=null)
			{
				if(photo.length > 0){
					response.setContentType("image/jpeg");
					response.setStatus(HttpServletResponse.SC_OK);

					os.write(photo);
				}
				else if(type.equals("idproof") && photo.length <= 0){
					os.write(idProof.getBytes());
				}
				else if (type.equals("signature") && photo.length <= 0){
					os.write(signature.getBytes());
				}
				else if (type.equals("profilePhoto") && photo.length <= 0){
					os.write(signature.getBytes());
				}
				else if (type.equals("addressProof") && photo.length <= 0){
					os.write(signature.getBytes());
				}
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/getCities.htm")
	public String getCities(@RequestParam Integer country, Map<String, Object> model ,HttpServletRequest request,HttpServletResponse response){
		if(country != null){

			try {
				model.put("entity", "city");
				model.put("id", "cityId");
				model.put("value", "city");
				model.put("list", customerService.getCityList(country));
				model.put("mobileNumLength", customerService.getMobileNumLength(country));

			} catch (EOTException e) {
			} finally{
				pageLogger(request,response,"getCities");
			}
		}
		return "combo";
	}

	@RequestMapping("/getQuarters.htm")
	public String getQuarters(@RequestParam Integer city, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		if(city != null){
			try {
				model.put("entity", "quarter");
				model.put("id", "quarterId");
				model.put("value", "quarter");
				model.put("list", customerService.getQuarterList(city));
			} catch (EOTException e) {
			} finally{
				pageLogger(request,response,"getQuarters");
			}
		}
		return "combo";
	}

	@RequestMapping("/getCityList.htm")
	public String getCityList(@RequestParam Integer country, Map<String, Object> model ,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("entity", "cityId");
			model.put("id", "cityId");
			model.put("value", "city");
			model.put("list", customerService.getCityList(country));
			model.put("mobileNumLength", customerService.getMobileNumLength(country));

		} catch (EOTException e) {
		} finally{
			pageLogger(request,response,"getCities");
		}	
		return "combo";
	}

	@RequestMapping("/getQuartersList.htm")
	public String getQuartersList(@RequestParam Integer city, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {

			model.put("entity", "quarterId");
			model.put("id", "quarterId");
			model.put("value", "quarter");
			model.put("list", customerService.getQuarterList(city));
		} catch (EOTException e) {
		} finally{
			pageLogger(request,response,"getQuarters");
		}	
		return "combo";
	}

	@RequestMapping(value="/showCustomerProfiles.htm")
	public String showCustomerProfiles(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			model.put("customerProfileDTO",new CustomerProfileDTO());	
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			model.put("page",customerService.getCustomerProfiles(pageNumber,"showCustomerProfiles.htm"));	

			model.put("message", request.getParameter("message"));
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"CustomerProfile");
		}	

		return "customerProfiles";

	}*/
	@RequestMapping("/editAgentProfiles.htm")
	public String editCustomerProfiles(Map<String,Object> model,HttpServletRequest request,CustomerProfileDTO customerProfileDTO,HttpServletResponse response){

		try{

			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			model.put("page",customerService.getCustomerProfiles(pageNumber,"showCustomerProfiles.htm"));	
			model.put("customerProfileDTO",customerService.editCustomerProfile(customerProfileDTO));	

		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"editCustomerProfile");
		}	

		return "customerProfiles";

	}

	/**
	 * Save customer profiles.
	 *
	 * @param model the model
	 * @param request the request
	 * @param customerProfileDTO the customer profile DTO
	 * @return the string
	 */
	@RequestMapping("/saveAgentProfile.htm")
	public String saveCustomerProfiles(Map<String,Object> model,HttpServletRequest request,CustomerProfileDTO customerProfileDTO){

		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			model.put("page",customerService.getCustomerProfiles(pageNumber,"showCustomerProfiles.htm"));	
			model.put("message",customerService.saveCustomerProfile(customerProfileDTO));
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("customerProfileDTO", customerProfileDTO);
			model.put("message", e.getErrorCode());
			return "customerProfiles";
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}

		return "redirect:/showCustomerProfiles.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");

	}

/**
 * Save customer bank account.
 *
 * @param bankAccountDTO the bank account DTO
 * @param result the result
 * @param model the model
 * @param request the request
 * @return the string
 * @throws EOTException the EOT exception
 */
/*
	@RequestMapping("/getQustionList.htm")
	public String getQuestionList(@RequestParam String  language, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {

			model.put("entity", "questionId");
			model.put("id", "questionId");
			model.put("value", "question");
			model.put("list", customerService.getCustomerQuestions(language));
		} catch (EOTException e) {
		} finally{
			//pageLogger(request,response,"getQuarters");
		}	
		return "combo";
	}

	@RequestMapping("/addBankAccount.htm")
	public String addBankAccount(@RequestParam Long customerId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		CustomerDTO dto = null;
		BankAccountDTO bankAccountDTO = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try{

			dto = customerService.getCustomerDetails(customerId,auth.getName());
			model.put("customerDTO",dto);
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = customerService.getCustomerBankAccountDetails(customerId,pageNumber,model);
			page.requestPage = "addBankAccount.htm";
			model.put("page", page);

			model.put("message", request.getParameter("message"));
			bankAccountDTO = new BankAccountDTO();
			bankAccountDTO.setCustomerId(customerId);
			bankAccountDTO.setAccountHolderName(dto.getFirstName());
			bankAccountDTO.setReferenceType(dto.getType());

		}catch(EOTException e){
			model.put("bankAccountDTO",bankAccountDTO);
			e.printStackTrace();
		}catch(Exception e){
			model.put("bankAccountDTO",bankAccountDTO);
			e.printStackTrace();
		}finally{
			model.put("bankAccountDTO",bankAccountDTO);
			model.put("branchList",customerService.getBranchListForBank(auth.getName()));
		}
		return "addBankAccount";
	}
*/
	@RequestMapping("/saveAgentBankAccount.htm")
	public String saveCustomerBankAccount(@Valid BankAccountDTO bankAccountDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try{
			if(result.hasErrors()){					
				model.put("bankAccountDTO",bankAccountDTO);			
			}			
			else if(bankAccountDTO.getSlNo() == null){				
				customerService.addBankAccountDetails(bankAccountDTO,auth.getName());	
				model.put("bankAccountDTO",new BankAccountDTO());	
				model.put("message","ADD_CUSTOMERBANK_ACCOUNT_SUCCESS");
				return "redirect:/addBankAccount.htm?customerId="+bankAccountDTO.getCustomerId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			} 
			else{					
				customerService.updateBranchDetails(bankAccountDTO);
				model.put("message","EDIT_CUSTOMERBANK_ACCOUNT_SUCCESS");
				return "redirect:/addBankAccount.htm?customerId="+bankAccountDTO.getCustomerId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		} catch (EOTException e) {
			model.put("bankAccountDTO",bankAccountDTO);
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("bankAccountDTO",bankAccountDTO);
			model.put("message","ERROR_9999");
		} finally{
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = customerService.getCustomerBankAccountDetails(bankAccountDTO.getCustomerId(),pageNumber,model);
			page.requestPage = "addBankAccount.htm";
			model.put("page", page);
			model.put("branchList",customerService.getBranchListForBank(auth.getName()));
		}
		return "addBankAccount";
	}

	/**
	 * Edits the cities.
	 *
	 * @param slNo the sl no
	 * @param referenceId the reference id
	 * @param model the model
	 * @param bankAccountDTO the bank account DTO
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/editAgentBankAccount.htm")
	public String editCities(@RequestParam Long slNo,@RequestParam Long referenceId,Map<String,Object> model,BankAccountDTO bankAccountDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			model.put("bankAccountDTO",customerService.getCustomerBankAccountDetails(slNo));				
			model.put("branchList",customerService.getBranchListForBank(auth.getName()));
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = customerService.getCustomerBankAccountDetails(referenceId,pageNumber,model);
				page.requestPage = "addBankAccount.htm";
				model.put("page", page);

			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
			}	
		}		
		return "addBankAccount";
	}
/*
	@RequestMapping("/addCustomerCard.htm")
	public String addCustomerCard(@RequestParam Long customerId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		CustomerDTO dto = null;
		CardDto cardDto = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try{

			dto = customerService.getCustomerDetails(customerId,auth.getName());
			model.put("customerDTO",dto);
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = customerService.getCustomerCardDetails(customerId,pageNumber,model);
			page.requestPage = "addCustomerCard.htm";
			model.put("page", page);

			model.put("message", request.getParameter("message"));
			cardDto = new CardDto();
			cardDto.setCustomerId(customerId);

		}catch(EOTException e){
			model.put("cardDto",cardDto);
			e.printStackTrace();
		}catch(Exception e){
			model.put("cardDto",cardDto);
			e.printStackTrace();
		}finally{
			model.put("cardDto",cardDto);
		}
		return "addCustomerCard";
	}

	@RequestMapping("/saveCustomerCard.htm")
	public String saveCustomerCard(@Valid CardDto cardDto,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try{
			if(result.hasErrors()){					
				model.put("cardDto",cardDto);			
			}			
			else if(cardDto.getCardId() == null){				
				customerService.addCustomerCard(cardDto,auth.getName());
				customerService.processNewCard(cardDto);
				model.put("bankAccountDTO",new BankAccountDTO());	
				model.put("message","ADD_CARD_SUCCESS");
				return "redirect:/addCustomerCard.htm?customerId="+cardDto.getCustomerId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			} 
			else{					
				customerService.updateCustomerCard(cardDto);
				model.put("message","UPDATE_CARD_SUCCESS");
				return "redirect:/addCustomerCard.htm?customerId="+cardDto.getCustomerId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		} catch (EOTException e) {
			model.put("cardDto",cardDto);
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("cardDto",cardDto);
			model.put("message","ERROR_9999");
		} finally{
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = customerService.getCustomerCardDetails(cardDto.getCustomerId(),pageNumber,model);
			page.requestPage = "addCustomerCard.htm";
			model.put("page", page);
		}
		return "addCustomerCard";
	}

	@RequestMapping("/editCustomerCard.htm")
	public String editCustomerCard(@RequestParam Long cardId,@RequestParam Long referenceId,Map<String,Object> model,CardDto cardDto,HttpServletRequest request,HttpServletResponse response){
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			model.put("cardDto",customerService.getCustomerCardDetails(cardId));				
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = customerService.getCustomerCardDetails(referenceId,pageNumber,model);
				page.requestPage = "addCustomerCard.htm";
				model.put("page", page);

			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
			}	
		}		
		return "addCustomerCard";
	}
	@RequestMapping("suscribedSMS.htm")
	public String smsSubscription(@RequestParam String customerId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details
		List<SmsAlertRuleValue>smsPackageList = null;
		try {
			smsPackageList = customerService.getSmsPackageList();
			model.put("SmsPackageList", smsPackageList);
			model.put("currentSubscription", customerService.getCurrentSubscription(customerId));
			model.put("customerId", customerId);
		} catch (EOTException e) {
			e.printStackTrace();
		}
		return "smsSubscription";
	}
	
	@RequestMapping("smsPackageDetails.htm")
	public String smsPackageDetails(@RequestParam String packageId,@RequestParam String  customerId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			List<SmsSubscriptionDTO>smsPackageList = customerService.smsPackageDetails(packageId);
			model.put("SmsSubscriptionDTO", smsPackageList);
			model.put("customerId", customerId);
		} catch (EOTException e) {
			e.printStackTrace();
		}
		return "smsPackageDetails";
	}
	@RequestMapping("subscribeSMSPackage.htm")
	public String subscribeSMSPackage(@RequestParam String customerId,@RequestParam String packageId,@RequestParam String subscriptionType,@RequestParam String noOfSms ,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			customerService.subscribedSMSPackage(customerId,packageId,subscriptionType,noOfSms);
			model.put("message", "SMS_SUBSCRIPTION_SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/getCurrentSubcription.htm?customerId="+customerId+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
	}
	@RequestMapping("getCurrentSubcription.htm")
	public String smsSubscribedDetails(@RequestParam String customerId,Map<String,Object> model,SMSAlertDTO smsAlertDTO,HttpServletRequest request,HttpServletResponse response){
		HttpSession session = request.getSession();
		try {
			session.setAttribute("currentSubscription", customerService.getCurrentSubscription(customerId));
		} catch (EOTException e) {
			e.printStackTrace();
		}
		finally{
			try {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				CustomerDTO dto = customerService.getCustomerDetails(Long.parseLong(customerId),auth.getName());
				model.put("customerDTO",dto);
				model.put("City",customerService.getCityList(dto.getCountryId()));
				model.put("quarter",customerService.getQuarterList(dto.getCityId()));
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (EOTException e) {
				e.printStackTrace();
			}
		}
		
		return "smsSubscribedDetails";
		
	}
	

	@RequestMapping("subscribedSC.htm")
	date:05/08/16 by:rajlaxmi for:showing only subscribed sc packages details
	public String scSubscription(@RequestParam String customerId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) throws EOTException{
		model.put("scPackageList", customerService.getSCPackageList());
		model.put("currentSubscriptions", customerService.getSCCurrentSubscription(customerId));
		model.put("customerId", customerId);
		return "scSubscription";
		
	}
	
	@RequestMapping("scPackageDetails.htm")
	public String scPackageDetails(@RequestParam String packageId,@RequestParam String  customerId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
	   try {
		List<SCSubscriptionDTO> packageList = customerService.scPackageDetails(packageId);
		model.put("SCSubscriptionDTO", packageList);
		model.put("customerId", customerId);
		System.out.println(packageList);
	} catch (EOTException e) {
		e.printStackTrace();
	}
		return "scPackageDetails";
	}
	
	@RequestMapping("subscribeSC.htm")
	public String currentSCDetails(@RequestParam String customerId,@RequestParam Long serviceChargeRuleId,@RequestParam String subscriptionType,@RequestParam String numberOfTxn ,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		customerService.subscribeSCPackage(customerId,serviceChargeRuleId,subscriptionType,numberOfTxn);
		model.put("message", "SERVICE_CHARGE_SUBSCRIPTION_SUCCESS");
		
		return "redirect:/getSCCurrentSubcription.htm?customerId="+customerId+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
		
	}
	@RequestMapping("getSCCurrentSubcription.htm")
	public String getSCCurrentSubscription(@RequestParam String customerId,Map<String,Object> model,SCSubscriptionDTO scSubscriptionDTO,HttpServletRequest request,HttpServletResponse response){
			model.put("currentSCSubscription", customerService.getSCCurrentSubscription(customerId));
		return "currentSCSubscription";
	}
	
	@RequestMapping("getSCSubscribedList.htm")
     public String getSCSubscribedList(@RequestParam String customerId,Map<String,Object> model,SCSubscriptionDTO scSubscriptionDTO,HttpServletRequest request,HttpServletResponse response){
		   System.out.println("scSubscriptionDTO: "+scSubscriptionDTO);
			model.put("currentSCSubscriptionList", customerService.getSCSubscribedList(customerId));
		
		return "currentSCSubscription";
	}
	
	public void test(@RequestParam MultipartFile signature){
		
	}
	*/
	
	/**
 * Export to pdf for agent details.
 *
 * @param type the type
 * @param model the model
 * @param request the request
 * @param session the session
 * @param response the response
 */
@SuppressWarnings("unchecked")
	@RequestMapping("/exportToPdfForAgentDetails.htm")
	public void exportToPdfForAgentDetails(@RequestParam Integer type,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
	
		List<Country> list = null;
		List<String> reportCaption = getReportNameAndHeader(type);
//		List<Customer> listCustomer = null;
//		List<String> locationsList= new ArrayList<String>();
		Locale resolveLocale = localeResolver.resolveLocale(request);
			String firstName = "";
			String middleName = "";
			String lastName = "";
			String mobileNumber = "";
			String bankId = "";
			String branchId = "";
			String countryId = "";
			String bankGroupId = "";
			String fromDate = "";
			String toDate = "";
			String custType = "";
			String onBoardedBy = "";
			String businessName = "";
			Integer pageNumber =null;
			Integer kycStatus =null;
			String channel = "";
			
			try{
				pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
				String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
				WebUser webUser=customerService.getUser(userName);
				firstName = request.getParameter("firstName");
				lastName = request.getParameter("lastName");
				middleName = request.getParameter("middleName");
				mobileNumber = request.getParameter("mobileNumber");
				bankId = request.getParameter("bankId");
				branchId = request.getParameter("branchId");
				countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
				bankGroupId = request.getParameter("bankGroupId");
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				custType = request.getParameter("custType") == null ? (type == null ? "" : type+"") : request.getParameter("custType");
				onBoardedBy = request.getParameter("onBoardedBy");
				businessName = request.getParameter("businessName");
				String status = request.getParameter("customerKycStatus");
				channel = request.getParameter("channel");
				
				if(null != status && !"".equals(status)) {
					kycStatus = Integer.parseInt(status);
				}
//				session.setAttribute("firstName", firstName);
//				session.setAttribute("middleName", middleName);
//				session.setAttribute("lastName", lastName);
//				session.setAttribute("mobileNumber", mobileNumber );
//				session.setAttribute("bankId", bankId );
//				session.setAttribute("branchId", branchId );
//				session.setAttribute("countryId", countryId );
//				session.setAttribute("bankGroupId", bankGroupId );
//				session.setAttribute("fromDate", fromDate );
//				session.setAttribute("toDate", toDate);
//				session.setAttribute("custType", custType);
//				session.setAttribute("onBoardedBy", onBoardedBy);
//				session.setAttribute("businessName", businessName);

				Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName,kycStatus,channel);
				page.requestPage = "searchAgentPage.htm";
				model.put("page",page);	
			
			model.put("lang",resolveLocale.toString());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			list = page.getResults();
//			listCustomer = page.getResults();
//			
//			for (int i = 0; i < listCustomer.size(); i++) {
//				Set<CustomerAccount> customerAccountSet = listCustomer.get(i).getCustomerAccounts();
//				for (CustomerAccount customerAccount : customerAccountSet) {
//					locationsList.add(customerAccount.getBranch().getLocation());
//				}				
//			}

			model.put("customerDTO", new CustomerDTO());
			model.put("firstName", firstName);
			model.put("middleName", middleName);
			model.put("lastName", lastName);
			model.put("mobileNumber",mobileNumber);
			model.put("bankId",bankId);
			model.put("branchId",branchId);
			model.put("countryId", countryId);
			model.put("bankGroupId", bankGroupId);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
			model.put("customerList",customerService.getCustomerList());
			model.put("custType",custType);	
			model.put("businessName",businessName);
			
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			model.put("pageHeader",  messageSource.getMessage(reportCaption.get(1),null,resolveLocale));
//			model.put("locationsList", locationsList);
			
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}
			catch(Exception e){
			e.printStackTrace();
		}
			String reportName = type == 0 ? EOTConstants.JASPER_CUSTOMER_DETAILS_JRXML_NAME : EOTConstants.JASPER_AGENTS_DETAILS_JRXML_NAME;
			generatePDFReport(reportName,reportCaption.get(0), list, model, request, response);
}
	
/**
 * Export to pdf for agent details with TransactionDate.
 *
 * @param type the type
 * @param model the model
 * @param request the request
 * @param session the session
 * @param response the response
 *//*

@SuppressWarnings("unchecked")
@RequestMapping("/exportToPdfForAgentDetailsTxnDate.htm")
public void exportToPdfForAgentDetailsTxnDate(@RequestParam Integer type,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

	List<Country> list = null;
	List<String> reportCaption = getReportNameAndHeader(type);
	List<Customer> listCustomer = null;
	List<String> locationsList= new ArrayList<String>();
	Locale resolveLocale = localeResolver.resolveLocale(request);
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String countryId = "";
		String bankGroupId = "";
		String fromDate = "";
		String toDate = "";
		String custType = "";
		String onBoardedBy = "";
		String businessName = "";
		
		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser webUser=customerService.getUser(userName);
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			middleName = request.getParameter("middleName");
			mobileNumber = request.getParameter("mobileNumber");
			bankId = request.getParameter("bankId");
			branchId = request.getParameter("branchId");
			countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
			bankGroupId = request.getParameter("bankGroupId");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			custType = request.getParameter("custType") == null ? (type == null ? "" : type+"") : request.getParameter("custType");
			onBoardedBy = request.getParameter("onBoardedBy");
			businessName = request.getParameter("businessName");
			
			session.setAttribute("firstName", firstName);
			session.setAttribute("middleName", middleName);
			session.setAttribute("lastName", lastName);
			session.setAttribute("mobileNumber", mobileNumber );
			session.setAttribute("bankId", bankId );
			session.setAttribute("branchId", branchId );
			session.setAttribute("countryId", countryId );
			session.setAttribute("bankGroupId", bankGroupId );
			session.setAttribute("fromDate", fromDate );
			session.setAttribute("toDate", toDate);
			session.setAttribute("custType", custType);
			session.setAttribute("onBoardedBy", onBoardedBy);
			session.setAttribute("businessName", businessName);

			Page page=customerService.searchCustomersWithTxnDate(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName);
			page.requestPage = "searchAgentPage.htm";
			model.put("page",page);	
		
		model.put("lang",resolveLocale.toString());
		String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		list = page.getResults();
		listCustomer = page.getResults();
		
		for (int i = 0; i < listCustomer.size(); i++) {
			Set<CustomerAccount> customerAccountSet = listCustomer.get(i).getCustomerAccounts();
			for (CustomerAccount customerAccount : customerAccountSet) {
				locationsList.add(customerAccount.getBranch().getLocation());
			}				
		}

		model.put("userName", webUser.getUserName());
		model.put("date", dt1);
		model.put("custType", custType);
		model.put("pageHeader",  messageSource.getMessage(reportCaption.get(1),null,resolveLocale));
		model.put("locationsList", locationsList);
		
	}catch(Exception e){
		e.printStackTrace();
	}
		String reportName = type == 0 ? EOTConstants.JASPER_CUSTOMER_DETAILS_JRXML_NAME : EOTConstants.JASPER_AGENTS_DETAILS_JRXML_NAME;
		generatePDFReport(reportName,reportCaption.get(0), list, model, request, response);
}*/

	
	/**
	 * Export to XLS location.
	 *
	 * @param type the type
	 * @param model the model
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 */
	@RequestMapping("/exportToXlsForAgentDetails.htm")
	public void exportToXLSLocation(@RequestParam Integer type,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) {

		HSSFWorkbook wb = null;
		List<Customer> list = null;
		List<String> reportCaption = getReportNameAndHeader(type);
		
			String firstName = "";
			String middleName = "";
			String lastName = "";
			String mobileNumber = "";
			String bankId = "";
			String branchId = "";
			String countryId = "";
			String bankGroupId = "";
			String fromDate = "";
			String toDate = "";
			String custType = "";
			String onBoardedBy = "";
			String businessName = "";
			Integer pageNumber = null;
			Integer kycStatus = null;
			String channel = "";
			
			try{
				pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
				String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
				WebUser webUser=customerService.getUser(userName);
//				customerName = request.getParameter("customerName");
				firstName = request.getParameter("firstName");
				lastName = request.getParameter("lastName");
				middleName = request.getParameter("middleName");
				mobileNumber = request.getParameter("mobileNumber");
				bankId = request.getParameter("bankId");
				branchId = request.getParameter("branchId");
		//		countryId = request.getParameter("countryId");
				countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
				bankGroupId = request.getParameter("bankGroupId");
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				custType = request.getParameter("custType") == null ? (type == null ? "" : type+"") : request.getParameter("custType");				
				onBoardedBy = request.getParameter("onBoardedBy");
				businessName = request.getParameter("businessName");
				String status = request.getParameter("customerKycStatus");
				channel = request.getParameter("channel");
				
				if(null != status && !"".equals(status)) {
					kycStatus = Integer.parseInt(status);
				}

				Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName,kycStatus,channel);
				page.requestPage = "searchAgentPage.htm";
				model.put("page",page);	
			
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			list = page.getResults();

			model.put("customerDTO", new CustomerDTO());
			model.put("firstName", firstName);
			model.put("middleName", middleName);
			model.put("lastName", lastName);
			model.put("mobileNumber",mobileNumber);
			model.put("bankId",bankId);
			model.put("branchId",branchId);
			model.put("countryId", countryId);
			model.put("bankGroupId", bankGroupId);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
			model.put("customerList",customerService.getCustomerList());
			model.put("custType",custType);	
			model.put("businessName",businessName);
			
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			model.put("pageHeader", reportCaption.get(1));
			
			wb = wrapper.createSpreadSheetFromListForCustomerDetails(list, localeResolver.resolveLocale(request), messageSource, webUser, reportCaption.get(1),custType,false);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ reportCaption.get(0)
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
			
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Export to XLS location with TRANSACTION DATE.
	 *
	 * @param type the type
	 * @param model the model
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 *//*
	@RequestMapping("/exportToXlsForAgentDetailsTxnDate.htm")
	public void exportToXLSLocationTxnDate(@RequestParam Integer type,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) {

		HSSFWorkbook wb = null;
		List<Customer> list = null;
		List<String> reportCaption = getReportNameAndHeader(type);
		
			String firstName = "";
			String middleName = "";
			String lastName = "";
			String mobileNumber = "";
			String bankId = "";
			String branchId = "";
			String countryId = "";
			String bankGroupId = "";
			String fromDate = "";
			String toDate = "";
			String custType = "";
			String onBoardedBy = "";
			String businessName = "";
			
			try{
				Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
				String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
				WebUser webUser=customerService.getUser(userName);
//				customerName = request.getParameter("customerName");
				firstName = request.getParameter("firstName");
				lastName = request.getParameter("lastName");
				middleName = request.getParameter("middleName");
				mobileNumber = request.getParameter("mobileNumber");
				bankId = request.getParameter("bankId");
				branchId = request.getParameter("branchId");
		//		countryId = request.getParameter("countryId");
				countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
				bankGroupId = request.getParameter("bankGroupId");
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				custType = request.getParameter("custType") == null ? (type == null ? "" : type+"") : request.getParameter("custType");				
				onBoardedBy = request.getParameter("onBoardedBy");
				businessName = request.getParameter("businessName");
				
				//session.setAttribute("customerName", customerName );
				session.setAttribute("firstName", firstName);
				session.setAttribute("middleName", middleName);
				session.setAttribute("lastName", lastName);
				session.setAttribute("mobileNumber", mobileNumber );
				session.setAttribute("bankId", bankId );
				session.setAttribute("branchId", branchId );
				session.setAttribute("countryId", countryId );
				session.setAttribute("bankGroupId", bankGroupId );
				session.setAttribute("fromDate", fromDate );
				session.setAttribute("toDate", toDate);
				session.setAttribute("custType", custType);
				session.setAttribute("onBoardedBy", onBoardedBy);
				session.setAttribute("businessName", businessName);

				Page page=customerService.searchCustomersWithTxnDate(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName);
				page.requestPage = "searchAgentPage.htm";
				model.put("page",page);	
			
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			list = page.getResults();

			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			model.put("pageHeader", reportCaption.get(1));
			
			wb = wrapper.createSpreadSheetFromListForCustomerDetails(list, localeResolver.resolveLocale(request), messageSource, webUser, reportCaption.get(1),custType);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ reportCaption.get(0)
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	
	
/**
 * Gets the report name and header.
 *
 * @param type the type
 * @return the report name and header
 */
private List<String> getReportNameAndHeader(Integer type) {
	
	List<String> reportCaptions =new ArrayList<>();
	 String reportName, reportHeader;
	
	if (type.equals(FieldExecutiveEnum.CUSTOMER.getCode())) {
		
		reportName = EOTConstants.CUSTOMER_DETAILS_REPORT_NAME;
		reportHeader = EOTConstants.CUSTOMER_PAGE_HEADER;
		
	}else if (type.equals(FieldExecutiveEnum.AGENT.getCode())) {
		
		reportName = EOTConstants.AGENT_DETAILS_REPORT_NAME;
		reportHeader = EOTConstants.AGENT_DETAILS_PAGE_HEADER;
		
	}else if (type.equals(FieldExecutiveEnum.SOLE_MERCHANT.getCode())) {
		
		reportName = EOTConstants.SOLE_MERCHANT_DETAILS_REPORT_NAME;
		reportHeader = EOTConstants.SOLE_MERCHANT_PAGE_HEADER;
		
	} else {
		
		reportName = EOTConstants.AGENT_SOLE_MERCHANT_DETAILS_REPORT_NAME;
		reportHeader = EOTConstants.AGENT_SOLE_MERCHANT_PAGE_HEADER;
	}
	
	reportCaptions.add(reportName);
	reportCaptions.add(reportHeader);
	
	return reportCaptions;
}

@RequestMapping("/searchQRCodeForm.htm")
public String searchQRCodeForm(Map<String,Object> model,QRCodeDTO  qrCodeDTO,HttpServletRequest request,HttpServletResponse response){
	try{
		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		model.put("stateList", customerService.getCityList(EOTConstants.COUNTRY_SOUTH_SUDAN));	
		Page page = customerService.getCustomersWithQRCode(qrCodeDTO, pageNumber);
		model.put("page", page);
	}catch(Exception e){
		model.put("message",ErrorConstants.SERVICE_ERROR);
	}finally{
		try{
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			/*Page page = customerService.getCustomerBankAccountDetails(referenceId,pageNumber,model);
			page.requestPage = "addBankAccount.htm";
			model.put("page", page);*/
			model.put("qrCodeDTO",  qrCodeDTO);

		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
		}	
	}		
	return "searchQRCodeForm";
}

	@RequestMapping("/exportQRCode.htm")
	public void exportQRCodeInPDF(QRCodeDTO qrCodeDTO, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		try {
			customerService.exportQRCodeInPDF(qrCodeDTO.getCustomerId(), response);

		} catch (EOTException e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping("/bulkExportQRCode.htm")
	public void bulkExportQRCodeInPDF(QRCodeDTO qrCodeDTO, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
			
		ZipOutputStream zipStream =null;
		try {
			
			zipStream = new ZipOutputStream(response.getOutputStream());
			Page page = customerService.getCustomersWithQRCode(qrCodeDTO, 1);
			List<Customer> results = page.getResults();
			for (Customer customer : results) {
				customerService.bulkQRCodePDF(customer.getCustomerId().toString(), response, zipStream);
			}
			
		} catch (EOTException | IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (zipStream!=null) {
					
					zipStream.finish();
					zipStream.flush();
					zipStream.closeEntry();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchAgentData.htm", method=RequestMethod.POST)
	public @ResponseBody String searchAgentData(CustomerDTO customerDTO, Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
		//String customerName = "";
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String countryId = "";
		String bankGroupId = "";
		String fromDate = "";
		String toDate = "";
		String custType = "";
		String businessName = "";
		Integer kycStatus = null;
		
		Map<String,Object> masterData = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();			
		Integer roleId = webUserService.getRoleId(auth.getName());
		
		String sortColumn = request.getParameter("sortColumn");
		String sortBy = request.getParameter("sortBy");
		String serialIndex = request.getParameter("index");
		int index=0;
		if (serialIndex !=null && !serialIndex.equals("")) {
			index = Integer.parseInt(serialIndex);
		}
		
		Integer pageNumber = null;
		
		if( request.getParameter("pageNumber") != null ){
			pageNumber = new Integer(request.getParameter("pageNumber"));
			customerDTO = (CustomerDTO) session.getAttribute("customerDTO");
		} else { 
			session.setAttribute("customerDTO", customerDTO);
		}
		
		//	Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			middleName = request.getParameter("middleName");
			mobileNumber = request.getParameter("mobileNumber");
			bankId = request.getParameter("bankId");
			branchId = request.getParameter("branchId");
			countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
			bankGroupId = request.getParameter("bankGroupId");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
		//	custType = request.getParameter("custTypeCode") != null ?request.getParameter("custTypeCode") :  (type == null ? "" : type+"");
			custType = null != request.getParameter("custType")?request.getParameter("custType"):"" ;
			businessName = request.getParameter("businessName");
			String status = request.getParameter("customerKycStatus");
			
			if(null != status && !"".equals(status)) {
				kycStatus = Integer.parseInt(status);
			}
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			
			customerDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
			customerDTO.setSortBy(sortBy);
			JSONObject json = null;
			
			try{			
				Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,null,businessName,kycStatus,null);
				response.setContentType("application/json");
				json = new JSONObject();
				json.put("recordsTotal", page!=null ? page.getTotalCount() : 0);
				json.put("recordsFiltered", page!=null ? page.getTotalCount() : 0);
				JSONArray array = new JSONArray();
				
				if (page !=null && page.getResults()!=null) {
					for (Customer cust : (List<Customer>) page.getResults()) {
						JSONObject item = new JSONObject();
						
						item.put("Appove/Reject", "");
						item.put("SerNo", ++index);
						item.put("AgentCode", cust.getAgentCode());
						item.put("BusinessName",null != cust.getBusinessName()?cust.getBusinessName():"");
						item.put("Name", cust.getFirstName() + " "+cust.getLastName());
						item.put("MobileNumber", cust.getMobileNumber());
						item.put("Status", cust.getActive().equals(40)?"Inactive":"Active");
						item.put("Gender", cust.getGender());
						String createdDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cust.getCreatedDate());
						item.put("RegisteredDate", createdDate);
						item.put("State", cust.getCity().getCity());
						item.put("OnBoardedBy",cust.getOnbordedBy());
						item.put("KYCStatus", cust.getKycStatus().equals(0)?"KYC Pending":cust.getKycStatus().equals(11)?"KYC Approved":"KYC Rejected");
						item.put("Action", "View");
						item.put("Id", cust.getCustomerId());
						item.put("Kyc", cust.getKycStatus());
						array.add(item);
						
					}	
				}
			json.put("data", array);
			}catch (EOTException e) {
				Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
				json.put("recordsTotal", page.getTotalCount());
				json.put("recordsFiltered", page.getTotalCount());
				JSONArray array = new JSONArray();
				if (page !=null && page.getResults()!=null) {
					for (Customer cust : (List<Customer>) page.getResults()) {
						JSONObject item = new JSONObject();
						item.put("AgentCode", cust.getAgentCode());
						item.put("BusinessName",null != cust.getBusinessName()?cust.getBusinessName():"");
						item.put("Name", cust.getFirstName() + " "+cust.getLastName());
						item.put("MobileNumber", cust.getMobileNumber());
						item.put("Status", cust.getActive().equals(40)?"Inactive":"Active");
						item.put("Gender", cust.getGender());
						item.put("RegisteredDate", cust.getCreatedDate()+"");
						item.put("State", cust.getCity().getCity());
						item.put("OnBoardedBy",cust.getOnbordedBy());
						item.put("KYCStatus", cust.getKycStatus().equals(0)?"KYC Pending":cust.getKycStatus().equals(11)?"KYC Approved":"KYC Rejected");
						item.put("Action", "View");
						item.put("Id", cust.getCustomerId());
						item.put("Kyc", cust.getKycStatus());
						array.add(item);
						
					}
			}
		json.put("data", array);
		e.printStackTrace();
		System.out.println("Problem in loading agent list : "+e.getMessage());
		pageLogger(request,response,"SearchCustomer");
		return json.toString();

	}catch(Exception e){
		PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
		e.printStackTrace();
		model.put("message", ErrorConstants.SERVICE_ERROR);
	}finally {
		model.put("custType",custType);

	}
	return json.toString();
  }
	
	@RequestMapping("/exportToXlsForBlockedApplicationDetails.htm")
	public void exportToXLSBlockedApplication(@RequestParam Integer type,CustomerDTO customerDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) {

		HSSFWorkbook wb = null;
		List<Customer> list = null;

			String fromDate = "";
			String toDate = "";
			String custType = "";
			Integer pageNumber = null;

			try{
				pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
				String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
				WebUser webUser=customerService.getUser(userName);

				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				custType = request.getParameter("custType") == null ? (type == null ? "" : type+"") : request.getParameter("custType");				

				Page page=customerService.searchBlockedCustomers(fromDate,toDate,pageNumber,custType);
				page.requestPage = "searchAgentPage.htm";
				model.put("page",page);	
			
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			list = page.getResults();
			String header = custType.equals(EOTConstants.REFERENCE_TYPE_CUSTOMER+"") ? EOTConstants.TYPE_CUSTOMER 
						:custType.equals(EOTConstants.REFERENCE_TYPE_AGENT+"") ? EOTConstants.TYPE_AGENT
						:custType.equals(EOTConstants.REFERENCE_TYPE_MERCHANT+"") ? EOTConstants.TYPE_MERCHANT : "";	

			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
			model.put("customerList",customerService.getCustomerList());
			model.put("custType",custType);	
			
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			model.put("pageHeader",  EOTConstants.BLOCKED + header + EOTConstants.REPORT_DETAILS );
			
			wb = wrapper.createSpreadSheetFromListForCustomerDetails(list, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.BLOCKED + header + EOTConstants.REPORT_DETAILS,custType,true);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+  EOTConstants.BLOCKED_APPLICATION_REPORT
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/searchAgentCashOut.htm")
	public String searchAgentCashOut(TransactionParamDTO transactionParamDTO ,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		
		try {	
			Page page=customerService.getAgentCashOutData(transactionParamDTO, pageNumber);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
			}catch(EOTException e){
				model.put("message", ErrorConstants.NO_RECORDS_FOUND);
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
				model.put("message", ErrorConstants.SERVICE_ERROR);
			}finally {
				model.put("transactionParamDTO", transactionParamDTO);
			}		
		return "addAgentCashOut";
	}
	
	@RequestMapping("/validateAgentCashOut.htm")
	public String addSMSCampaign(TransactionParamDTO transactionParamDTO ,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		
		try {	
				transactionParamDTO = customerService.getAgentCashOutDetails(transactionParamDTO);
				customerService.sendOTPForAgentCashOut(transactionParamDTO);
				model.put("transactionParamDTO", transactionParamDTO);
			}catch(EOTException e){
				Page page=customerService.getAgentCashOutData(transactionParamDTO, pageNumber);
				model.put("page",page );
				model.put("message",e.getErrorCode());
				model.put("transactionParamDTO",  new TransactionParamDTO());
				e.printStackTrace();
				return "addAgentCashOut";
			}catch(Exception e){
				Page page=customerService.getAgentCashOutData(transactionParamDTO, pageNumber);
				model.put("page",page );
				e.printStackTrace();
				model.put("message", ErrorConstants.SERVICE_ERROR);
				model.put("transactionParamDTO", new TransactionParamDTO());
				return "addAgentCashOut";
			}		
		return "confirmAgentCashOut";
	}
	
}

	

