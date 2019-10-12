package com.eot.banking.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.eot.banking.dto.BankAccountDTO;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.CustomerProfileDTO;
import com.eot.banking.dto.SCSubscriptionDTO;
import com.eot.banking.dto.SMSAlertDTO;
import com.eot.banking.dto.SmsSubscriptionDTO;
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
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerDocument;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.Quarter;
import com.eot.entity.SmsAlertRuleValue;
import com.eot.entity.WebUser;

@Controller
public class CustomerController extends PageViewController {

	@Autowired
	TransactionService transactionService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private ExcelWrapper wrapper;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonService commonService;

/*	@RequestMapping("/addCustomerRequest.htm")
	public String addCustomerRequest(CustomerDTO customerDTO, HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){
		model.put("language",localeResolver.resolveLocale(request) );
		model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString()));
	//	pageLogger(request,response,"addCustomerRequest");
		//	return "addCustomer";
		return "redirect:/showCustomerForm.htm?type="+customerDTO.getType()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
	

	}*/
	@RequestMapping("/searchMobileNo.htm")
	public String searchMobileNumber(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{			

			model.put("mobileNumber",request.getParameter("mobileNumber"));

			String mobileNumber =  request.getParameter("isdCode") + request.getParameter("mobileNumber");

			Customer customer = customerService.getCustomerByMobileNumber(mobileNumber);
			if(customer==null){
				//				model.put("message", "PROCEED_CUSTOMER_REGISTRATION");
				return "redirect:/showCustomerForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}			

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			CustomerAccount customerAccount = customerService.findCustomerAccount(customer.getCustomerId(),auth.getName());
			if( customerAccount != null ){
				model.put("message", "ERROR_5016");
				return "addCustomer";
			}else{
				CustomerDTO dto = customerService.getCustomerDetails(customer.getCustomerId(),auth.getName());			
				model.put("customerDTO",dto);
				return "customerDetails";
			}
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"SearchMobileNo");
		}	
		return "addCustomer";
	}

	@RequestMapping("/createAccount.htm")
	public String createAccountForExistingCustomer(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			String mobileNumber = request.getParameter("mobileNumber");
			customerService.createCustomerAccount(mobileNumber);
			model.put("message", "ACCOUNT_CREATE_SUCCESS");			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Customer customer = customerService.getCustomerByMobileNumber(mobileNumber);
			CustomerDTO dto = customerService.getCustomerDetails(customer.getCustomerId(),auth.getName());		

			model.put("customerDTO",dto);
			return "redirect:/viewCustomer.htm?customerId="+dto.getCustomerId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"CreateAccount");
		}	

		return "addCustomer";

	}

	@RequestMapping("/showCustomerForm.htm")
	public String showCustomerRegistrationForm(CustomerDTO customerDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		try {
			model.put("countryList",transactionService.getCountryList(localeResolver.resolveLocale(request).toString()));		
			String mobileNumber = request.getParameter("mobileNumber");
			customerDTO.setMobileNumber(mobileNumber);
			if(null != request.getParameter("type"))
				customerDTO.setType(Integer.parseInt( request.getParameter("type")));							
			if( roleId != 24 && roleId != 25 && roleId != 26) 
				customerDTO = customerService.getUserForProof(auth.getName(),customerDTO);				
			else 
				customerDTO = customerService.getUserForProofByPartner(auth.getName(),customerDTO);	
			model.put("role",roleId);
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
				model.put("City",customerService.getCityList(countryId));
				model.put("mobileNumLength", countryId != null?customerService.getMobileNumLength(countryId):null);
				pageLogger(request,response,"Customer");
				} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
				}
		}	
		return "customer";
	}
	@RequestMapping("/saveCustomer.htm")
	public String saveCustomer(@Valid CustomerDTO customerDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		CustomerDTO dto = null;
		boolean flag=false;
		try {

			if(result.hasErrors()){
				model.put("customerDTO",customerDTO);				
				return "customer";
			}					
			if(customerDTO.getCustomerId() == null ){
				String otp=null != request.getParameter("otp")?request.getParameter("otp"):"";
				customerDTO.setOtp(otp);
				Long customerId = customerService.saveCustomer(customerDTO);
				customerDTO.setCustomerId(customerId);
				model.put("message", "CUSTOMER_REG_SUCCESS");
				customerDTO.setOnBordedBy(auth.getName());
				model.put("customerDTO",customerDTO);
			}else{
				customerService.updateCustomer(customerDTO);
				dto = customerService.getCustomerDetails(customerDTO.getCustomerId(),auth.getName());
				dto.setLanguage(customerService.getLanguageDescription(dto.getLanguage()));
				customerDTO.setCustomerKycStatus(dto.getCustomerKycStatus());
				model.put("customerDTO",dto);
				model.put("message", "CUSTOMER_EDIT_SUCCESS");
			}
		} catch (EOTException e) {
			flag = true;
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "customer";
		} catch (Exception e) {
			flag = true;
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "customer";
		}  finally{
			if(flag == true){
				try {
					if(roleId != 24 && roleId != 25 && roleId != 26) {
						model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),customerDTO.getType().toString()));
					}else {
						model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),customerDTO.getType().toString()));
					}
					model.put("language",localeResolver.resolveLocale(request) );
					Integer countryId = customerDTO.getCountryId() != null ? customerDTO.getCountryId() : EOTConstants.DEFAULT_COUNTRY ;
					model.put("City",customerService.getCityList(countryId));
					model.put("quarter",customerDTO.getCity() != null?customerService.getQuarterList(customerDTO.getCity()):null);
					model.put("questionList",customerDTO.getLanguage() != null?customerService.getCustomerQuestions(customerDTO.getLanguage()):null); 
					model.put("mobileNumLength", customerService.getMobileNumLength(countryId));
					model.put("customerDTO",new CustomerDTO());
				} catch (EOTException e) {
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
		//	model.put("customerDTO",customerDTO);
		}
		return "viewCustomer";
	}
	@RequestMapping("/mobileNumLenthRequest.htm")
	public String getMobileNumLength(@RequestParam Integer country,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("mobileNumLength", customerService.getMobileNumLength(country));
		}catch(EOTException e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			pageLogger(request,response,"mobileNumLength");
		}	
		return "customerMobileNum";
	}

	@RequestMapping("/editCustomer.htm")
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
			//	model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				if(roleId != 24 && roleId != 25 && roleId != 26) {
					model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),dto.getType().toString()));
				}else {
					model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),dto.getType().toString()));
				}
				Integer countryId = dto.getCountryId() != null ? dto.getCountryId() : EOTConstants.DEFAULT_COUNTRY ;
				model.put("City",customerService.getCityList(countryId));
				model.put("quarter",customerService.getQuarterList(dto.getCityId()));				
				model.put("mobileNumLength",customerService.getMobileNumLength(countryId));
				/*commenting to make a default language as English, by vineeth on 15-11-2018
				model.put("questionList",customerService.getCustomerQuestions(dto.getLanguage()));
				model.put("language",localeResolver.resolveLocale(request));*/
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditCustomer");
		}
		return "customer";
	}

	 //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/viewCustomer.htm")
	public String viewCustomer(CustomerDTO customerDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		try{			
			CustomerDTO dto = customerService.getCustomerDetails(customerDTO.getCustomerId(),auth.getName());
			dto.setLanguage(customerService.getLanguageDescription(dto.getLanguage()));
			model.put("customerDTO",dto);
			model.put("message",request.getParameter("message"));
		}catch(EOTException e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"ViewCustomer");
		}
		return "viewCustomer";
	}

	@RequestMapping("/searchCustomer.htm")
	public String searchCustomer(@RequestParam Integer type,CustomerDTO customerDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		if(null == customerDTO) {
			customerDTO = new CustomerDTO();
		}
		int pageNumber=1;
		
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
		Integer kycStatus = null;
		String channel = "";
		
		Map<String,Object> masterData = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		
		try{
			//Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
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
			onBoardedBy = request.getParameter("onBoardedBy");
			businessName = request.getParameter("businessName");
			String status = request.getParameter("customerKycStatus");
			channel = request.getParameter("channel");
			
			if(null != status && !"".equals(status)) {
				kycStatus = Integer.parseInt(status);
			}
			if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
				toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			}
			session.setAttribute("custType", custType);
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName,kycStatus,channel);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
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
				model.put("language",localeResolver.resolveLocale(request) );
				if (branchId != null &&  branchId != ""){
					if(branchId.equalsIgnoreCase("select")){
						branchId = "";
					}
					if(bankId!=null&&bankId!=""&&bankId!="select")
						masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId)));
				}
			//	session.setAttribute("customerDTO", customerDTO);
				model.put("masterData",masterData );
				model.put("customerDTO", customerDTO);
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
				model.put("onBoardedBy",onBoardedBy);
				model.put("businessName",businessName);
				model.put("customerKycStatus",kycStatus);
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchCustomer");
		}
		return "viewCustomers";
	}

	@RequestMapping("/searchCustomerPage.htm")
	public String searchCustomerPage(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

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
		Integer kycStatus = null;
		 String channel =null;
		
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
			 custType =  session.getAttribute("custType") == null ? "" : session.getAttribute("custType").toString() ;
			 onBoardedBy = request.getParameter("onBoardedBy") == null ? "" : session.getAttribute("onBoardedBy").toString() ;
			 businessName = request.getParameter("businessName") == null ? "" : session.getAttribute("businessName").toString() ;
			 String status = request.getParameter("customerKycStatus");
			 channel = request.getParameter("channel");
				
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
			Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName,kycStatus,channel);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			page.requestPage = "searchCustomerPage.htm";
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
				model.put("language",localeResolver.resolveLocale(request) );
				if (branchId != null &&  branchId != ""){
					if(branchId.equalsIgnoreCase("select")){
						branchId = "";
					}
					if(bankId!=null&&bankId!=""&&bankId!="select")
						masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId)));
				}
				model.put("masterData",masterData );
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
				model.put("onBoardedBy",onBoardedBy);
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchCustomerPage");
		}
		return "viewCustomers";
	}

	@RequestMapping(value = "/exportToXLSForCustomerDetails.htm")
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
			String custType = session.getAttribute("custType") == null ? "" : session.getAttribute("custType").toString() ;

			list = customerService.exportToXLSForCustomerDetails(customerName,mobileNumber,bankId,branchId,countryId,bankGroupId,fromDate,toDate,model);

			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser webUser=customerService.getUser(userName);

			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER	|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK){
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
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return viewPage;
	}

	@RequestMapping(value = "/exportToXLSForCustomerAccountDetails.htm")
	public String exportToXLSForCustomerAccountDetails(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session)throws Exception {

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
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return viewPage;
	}



	@RequestMapping("/resetLoginPin.htm")
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
	
	
	@RequestMapping("/resetTxnPin.htm")
	@ResponseBody
	public String resetTxnPin(@RequestParam Long customerId ,HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){

		try{
			customerService.resetTxnPin(customerId);
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
	public String changeApplicationStatus(CustomerDTO cuDto,HttpServletRequest request , Map<String,Object> model,HttpServletResponse response) throws EOTException{
		Long customerId = cuDto.getCustomerId();
		Integer status = cuDto.getAppStatus();
		String msg = "" ;
		String view = "" ;
		String reasonForBlock = "" ;
		
		try{
			if(request.getParameter("custType").equals(EOTConstants.REF_TYPE_CUSTOMER+""))
				 view = "viewCustomer" ;
			else
				view = "viewAgent" ;
			reasonForBlock = request.getParameter("blockComment");			
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
		return view;
		/*return "redirect:viewCustomer.htm?customerId="+customerId+"&message="+msg+"&csrfToken=" + request.getSession().getAttribute("csrfToken");*/
	}

	@RequestMapping("/changeCustomerStatus.htm")
	public String changeCustomerStatus(CustomerDTO cuDto, Map<String,Object> model,HttpServletRequest request ,HttpServletResponse response) throws EOTException{
		Long customerId = cuDto.getCustomerId();
		Integer status = cuDto.getAppStatus();
		String msg = "" ;
		String reasonForDeActivate = "" ;
		Customer cust = customerService.getCustomerByCustomerId(customerId);
		try{

			reasonForDeActivate = request.getParameter("deActivateComment");
			if(status!=null){
				customerService.changeCustomerStatus(customerId,status,reasonForDeActivate);
			
			if(cust.getType().equals(EOTConstants.CUSTOMER_TYPE_CUSTOMER)){
			   if(status == EOTConstants.CUSTOMER_STATUS_ACTIVE){
				msg = "CUSTOMER_ACTIVATED_SUCCESS" ;	
			    }
			   if(status == EOTConstants.CUSTOMER_STATUS_DEACTIVATED){
				msg = "CUSTOMER_DEACTIVATED_SUCCESS" ;	
			}}
			
			else if(cust.getType().equals(EOTConstants.REFERENCE_TYPE_AGENT)){
				if(status == EOTConstants.CUSTOMER_STATUS_ACTIVE){
					msg = "AGENT_ACTIVATED_SUCCESS" ;	
				}
				if(status == EOTConstants.CUSTOMER_STATUS_DEACTIVATED){
					msg = "AGENT_DEACTIVATED_SUCCESS" ;	
				}
			}else{
				if(status == EOTConstants.CUSTOMER_STATUS_ACTIVE){
					msg = "MARCHANT_ACTIVATED_SUCCESS" ;	
				}
				if(status == EOTConstants.CUSTOMER_STATUS_DEACTIVATED){
					msg = "MARCHANT_DEACTIVATED_SUCCESS" ;	
				}
			}}
			
		}catch(EOTException e) {
			e.printStackTrace();
			msg =  e.getErrorCode() ;
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
			pageLogger(request,response,"ChangeCustomerStatus");
		}
		if(cust.getType().equals(EOTConstants.CUSTOMER_TYPE_CUSTOMER)){	
		    return "viewCustomer";
		}else{
			return "viewAgent";
		}/*return "redirect:viewCustomer.htm?customerId="+customerId+"&message="+msg+"&csrfToken=" + request.getSession().getAttribute("csrfToken");*/
	}

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
					os.write(profilePhoto.getBytes());
				}
				else if (type.equals("addressProof") && photo.length <= 0){
					os.write(addressProof.getBytes());
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
	}
	
	@RequestMapping("/editCustomerProfiles.htm")
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

	@RequestMapping("/saveCustomerProfile.htm")
	public String saveCustomerProfiles(Map<String,Object> model,HttpServletRequest request,CustomerProfileDTO customerProfileDTO, HttpServletResponse response){

		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			model.put("page",customerService.getCustomerProfiles(pageNumber,"showCustomerProfiles.htm"));	
			model.put("message",customerService.saveCustomerProfile(customerProfileDTO));
			model.put("customerProfileDTO", new CustomerProfileDTO());
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("customerProfileDTO", customerProfileDTO);
			model.put("message", e.getErrorCode());
			return "customerProfiles";
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;

			try {
				model.put("page",customerService.getCustomerProfiles(pageNumber,"showCustomerProfiles.htm"));
			} catch (EOTException e) {
				e.printStackTrace();
			}
			pageLogger(request,response,"CustomerProfile");
		}
		return "customerProfiles";
		/*return "redirect:/showCustomerProfiles.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
	}

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

	@RequestMapping("/saveCustomerBankAccount.htm")
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

	@RequestMapping("/editCustomerBankAccount.htm")
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
		/*date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details*/
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
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		try {
			session.setAttribute("currentSubscription", customerService.getCurrentSubscription(customerId));
		} catch (EOTException e) {
			e.printStackTrace();
		}
		finally{
			try {
				
				CustomerDTO dto = customerService.getCustomerDetails(Long.parseLong(customerId),auth.getName());
				model.put("customerDTO",dto);
				model.put("City",customerService.getCityList(dto.getCountryId()));
				model.put("quarter",customerService.getQuarterList(dto.getCityId()));
		//		model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				if(roleId != 24 && roleId != 25 && roleId != 26) {
					model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),dto.getIdType()));
				}else {
					model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),dto.getIdType()));
				}
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
	/*date:05/08/16 by:rajlaxmi for:showing only subscribed sc packages details*/
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
	
	@RequestMapping(value = "/apprOrRejectCust.htm")
	public String apprOrRejectCust(@RequestParam("approvalType") String approvalType, @RequestParam("rejectComment") String rejectComment, @RequestParam Integer type, @RequestParam("kycStatus") String kycStatus, CustomerDTO customerDTO, HttpServletRequest request, HttpServletResponse response, Map<String, Object> model,HttpSession session)  {
		
		String custType = "";
		String countryId = "";
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String bankGroupId = "";
		String fromDate = "";
		String toDate = "";
		String onBoardedBy = "";
		String businessName = "";
		Map<String,Object> masterData = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		Integer pageNumber =1;
		String viewPage="";
		Integer kycStatus1 = null;
		String channel = null;
		
		try {
			pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			custType = request.getParameter("custTypeCode") == null ? (type == null ? "" : type+"") : request.getParameter("custTypeCode");
			countryId = request.getParameter("countryId") != null ? request.getParameter("countryId") : EOTConstants.DEFAULT_COUNTRY.toString() ;
			String action = customerService.apprOrRejectCust(kycStatus, rejectComment, customerDTO);
			if (custType.equals(EOTConstants.REF_TYPE_CUSTOMER + "")) {
				if (action.equals("Approve")) {
					model.put("message", "CUSTOMER_APPROVED");
				} else if (action.equals("Reject")) {
					model.put("message", "CUSTOMER_REJECTED");
				}
				if (approvalType.equals("Single")) {
					//viewPage = "customer";
					viewPage = "viewCustomers";
				} else {
					viewPage = "viewCustomers";
				}
			}else if (custType.equals(EOTConstants.REFERENCE_TYPE_AGENT + "")) {

				if (action.equals("Approve")) {
					model.put("message", "AGENT_APPROVED");
				} else if (action.equals("Reject")) {
					model.put("message", "AGENT_REJECTED");
				}
				if (approvalType.equals("Single")) {
					viewPage = "agent";
				} else {
					viewPage = "viewAgents";
				}
			
			}
			else if (custType.equals(EOTConstants.REFERENCE_TYPE_MERCHANT + "")) {

				if (action.equals("Approve")) {
					model.put("message", "MERCHANT_APPROVED");
				} else if (action.equals("Reject")) {
					model.put("message", "MERCHANT_REJECTED");
				}
				if (approvalType.equals("Single")) {
					viewPage = "agent";
				} else {
					viewPage = "viewAgents";
				}
			
			}
			
			
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
			custType = request.getParameter("custTypeCode") == null ? (type == null ? "" : type+"") : request.getParameter("custTypeCode");
			onBoardedBy = request.getParameter("onBoardedBy");
			businessName = request.getParameter("businessName");
			channel = request.getParameter("channel");
			
			String status = request.getParameter("customerKycStatus");
			
			if(null != status && !"".equals(status)) {
				kycStatus1 = Integer.parseInt(status);
			}

			if(/*!approvalType.equals("Single") &&*/ StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)) {
				toDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				final Calendar cal = Calendar.getInstance();
			    cal.add(Calendar.DATE, -1);
			    fromDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
			}

			
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
//			session.setAttribute("onBoardedBy", onBoardedBy);
//			session.setAttribute("businessName", businessName);
			
		} catch (EOTException e) {
			e.printStackTrace();
		}
		finally {
			
			try {
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
				Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName,kycStatus1,channel);
				model.put("masterData",masterData );
				if (approvalType.equals("Single")) {
					customerDTO.setReasonForRejection(rejectComment);
					model.put("customerDTO", customerDTO);
					
				} else {
					CustomerDTO c = new CustomerDTO();
					c.setManageCustomer(null);
					c.setCustomerKycStatus(kycStatus1);
					c.setChannel(channel);
					model.put("customerDTO", c);
				}
				
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
				model.put("City",customerService.getCityList(new Integer(countryId)));
				model.put("quarter",customerService.getQuarterList(customerDTO.getCityId()));				
				model.put("mobileNumLength",customerService.getMobileNumLength(new Integer(countryId)));
				model.put("custType",custType);	
				model.put("onBoardedBy",onBoardedBy);	
				model.put("page",page);	
			}catch(EOTException e) {
				e.printStackTrace();
				model.put("message", e.getErrorCode());
			}
			catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}

			
			pageLogger(request,response,"Approve or Reject Customer");
		
		}
		return viewPage;
	}
	
	@RequestMapping(value = "/sendOTP.htm",method = RequestMethod.POST)
	public  String sendOTP(CustomerDTO customerDTO,
			Map<String, Object> map, HttpServletRequest request, Map<String, Object> model) {
		String jsonResponse = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Integer roleId = webUserService.getRoleId(auth.getName());
		try {
			String message = customerService.sendOTP(customerDTO);
			model.put("message", "OTP_SUCCESS");
		//JSONAdaptor adaptor= new JSONAdaptor();
		//	jsonResponse = adaptor.toJSON(message);
		} catch (EOTException e) {
			model.put("message", ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
			e.printStackTrace();
		}finally {
			
			model.put("customerDTO",customerDTO);
			try {
				if( roleId != 24 && roleId != 25 && roleId != 26) 					
					model.put("masterData", customerService.getMasterDataByType(localeResolver.resolveLocale(request).toString(),request.getParameter("type")));
				else 				
					model.put("masterData", customerService.getMasterDataByPartner(localeResolver.resolveLocale(request).toString(),request.getParameter("type")));								

				model.put("City",customerService.getCityList(customerDTO.getCountryId()));
				model.put("quarter",customerService.getQuarterList(customerDTO.getCityId()));
				model.put("mobileNumLength", customerDTO.getCountryId() != null?customerService.getMobileNumLength(customerDTO.getCountryId()):null);
			} catch (EOTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return "customer";
	}
	
	@SuppressWarnings("finally")
	@RequestMapping("/getOTPRequest.htm")
	public String getOTPRequest(@RequestParam String mobileNumber,@RequestParam Integer countryId, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setMobileNumber(mobileNumber);
			customerDTO.setCountryId(countryId);
			customerService.sendOTP(customerDTO);			
			model.put("message1", "OTP_SUCCESS");
		}  catch (EOTException e) {
			model.put("message1", ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message1", "OTP_FAIL");
			e.printStackTrace();
		} finally{
			model.put("otp", "otp");
			return "combo";
		}			
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchCustomerData.htm", method=RequestMethod.POST)
	public @ResponseBody String searchCustomerData(CustomerDTO customerDTO, Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
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
		String onBoardedBy = "";
		
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
			String status = request.getParameter("customerKycStatus");
			onBoardedBy = request.getParameter("onBoardedBy");
			
			if(null != status && !"".equals(status)) {
				kycStatus = Integer.parseInt(status);
			}
			
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			
			customerDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "TransactionDate" : sortColumn);
			customerDTO.setSortBy(sortBy);
			JSONObject json = null;
			
			try{			
				Page page=customerService.searchCustomers(userName,bankGroupId,firstName,middleName,lastName,mobileNumber,bankId,branchId,countryId,fromDate,toDate,pageNumber,custType,onBoardedBy,businessName,kycStatus,null);
				response.setContentType("application/json");
				json = new JSONObject();
				json.put("recordsTotal", page!=null ? page.getTotalCount() : 0);
				json.put("recordsFiltered", page!=null ? page.getTotalCount() : 0);
				JSONArray array = new JSONArray();
				CustomerDocument custDoc= null;
				if (page !=null && page.getResults()!=null) {
					for (HashMap map : (List<HashMap>) page.getResults()) {
						JSONObject item = new JSONObject();
						
						item.put("Appove/Reject", "");
						item.put("SerNo", ++index);
						item.put("Name", map.get("Name"));
//						item.put("MobileNumber", cust.getMobileNumber());
//						item.put("Status", cust.getActive().equals(40)?"Inactive":"Active");
//						item.put("Gender", cust.getGender());
//						String createdDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cust.getCreatedDate());
//						item.put("RegisteredDate", createdDate);
//						item.put("State", cust.getCity().getCity());
//						item.put("OnBoardedBy",cust.getOnbordedBy());
//						item.put("KYCStatus", cust.getKycStatus().equals(0)?"KYC Pending":cust.getKycStatus().equals(11)?"KYC Approved":"KYC Rejected");
//						item.put("Action", "View");
//						item.put("Id", cust.getCustomerId());
//						item.put("Kyc", cust.getKycStatus());
						item.put("MobileNumber", "");
						item.put("Status", "");
				//		item.put("Gender", "");
					//	String createdDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cust.getCreatedDate());
						item.put("RegisteredDate", "");
						item.put("State", "");
						item.put("OnBoardedBy","");
						item.put("KYCStatus", "");
						item.put("Action", "View");
						item.put("Id", "");
						item.put("Kyc","");
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
						
						item.put("Appove/Reject", "");
						item.put("SerNo", ++index);
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
	
}
	

