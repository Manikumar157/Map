package com.eot.banking.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.BankTellerService;
import com.eot.banking.service.BusinessPartnerService;
import com.eot.banking.service.CommonService;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.LocationService;
import com.eot.banking.service.WebUserService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.Page;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.WebUser;

@Controller
public class WebUserManageMentController extends PageViewController{

	@Autowired
	private WebUserService webUserService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ExcelWrapper wrapper;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private BusinessPartnerService businessPartnerService; 
	@Autowired
	private CommonService commonService;
	/** The bank teller service. */
	@Autowired
	private BankTellerService bankTellerService; 
	
	@RequestMapping("/showUserForm.htm")
	public String showUserManagementForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){

		try {
			Integer bankId = null;
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser weUser =webUserService.getWebUserByUserName(userName);
			
			Integer roleId= weUser.getWebUserRole().getRoleId();
			
			Integer seniorPartnerId=null;
			BusinessPartner businessPartner=null;
			if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2|| weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L3
					|| weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN)
				businessPartner= webUserService.getBusinessPartnerByUserName(userName);
			
			if(businessPartner!=null)
			{
				seniorPartnerId= businessPartner.getId()!=null?businessPartner.getId():null;
			}
			
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			model.put("masterData", webUserService.getMasterData(userName,pageNumber));
			model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("lang",localeResolver.resolveLocale(request).toString());	
			model.put("language", locationService.getLanguageList());
			model.put("webUserDTO", new WebUserDTO());
			model.put("message", request.getParameter("message"));
			model.put("userId", request.getParameter("userId"));
			model.put("action", "add");
			if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L3|| 
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN) {
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
				model.put("bulkPaymentPartnerList",  webUserService.getAllBulkPaymentPartner(roleId, bankId));
			}
		} catch (EOTException e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{			
			try {
				model.put("mobilenumberLength",customerService.getMobileNumLength(EOTConstants.DEFAULT_COUNTRY));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"WebUser");
		}
		return "addWebUser";

	}
	
	@RequestMapping("/showWebUserForm.htm")
	public String showBankingManagementForm(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response,HttpSession session){
		WebUserDTO webUserDTO=null;
		int pageNumber=1;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		try {
			webUserDTO=new WebUserDTO();	
			Page page=webUserService.getWebUserList(webUserDTO, userName, pageNumber);
			page.requestPage = "searchWebUser.htm" ;
			model.put("page", page);		
		
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
			ex.printStackTrace();
		}finally{
			try {
				model.put("masterData", webUserService.getSearchWebUserList(webUserDTO, userName, pageNumber));
			} catch (EOTException e) {
				e.printStackTrace();
			}
			model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("lang",localeResolver.resolveLocale(request).toString());	
			model.put("language", locationService.getLanguageList());
			model.put("message", request.getParameter("message"));
			model.put("userId", request.getParameter("userId"));
			model.put("action", "add");
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("webUserDTO",webUserDTO);
			model.put("businessPartnerUsers",webUserService.getAllBusniessPartnerUser());
			session.setAttribute("webUserDTO",webUserDTO);
			
		}
		return "webUser";
	}
	
	@RequestMapping("/searchWebUser.htm")
	public String showWebUser(WebUserDTO webUserDTO,HttpServletRequest request,Map<String,Object> model,HttpServletResponse response,HttpSession session){
       int pageNumber=1;
       String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		try {
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				webUserDTO = (WebUserDTO) session.getAttribute("webUserDTO");
			} else {
				session.setAttribute("webUserDTO", webUserDTO);
			}	
			
			Page page=webUserService.getWebUserList(webUserDTO, userName, pageNumber);
			page.requestPage = "searchWebUser.htm" ;
			model.put("page", page);
			model.put("message", request.getParameter("message"));
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				model.put("masterData", webUserService.getSearchWebUserList(webUserDTO, userName, pageNumber));
			} catch (EOTException e) {
				e.printStackTrace();
			}
			model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("lang",localeResolver.resolveLocale(request).toString());	
			model.put("language", locationService.getLanguageList());
			model.put("webUserDTO", webUserDTO);
			model.put("businessPartnerUsers",webUserService.getAllBusniessPartnerUser());
			model.put("userId", request.getParameter("userId"));
			model.put("action", "add");
			pageLogger(request,response,"WebUser");
		}
		return "webUser";

	}

	@RequestMapping("/saveUser.htm")
	public String saveUser(@Valid WebUserDTO webUserDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		WebUserDTO webUserDTO1=null;
		WebUser webUser = null;
		Integer bankId = null;
		Integer seniorPartnerId=null;
		BusinessPartnerUser businessPartnerUser  = null;
		boolean flag=false;
		try {
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			webUser =webUserService.getWebUserByUserName(userName);
			
			Integer roleId= webUser.getWebUserRole().getRoleId();
			if(result.hasErrors()){
				model.put("webUserDTO",webUserDTO);
				model.put("action", "add");
			}else{
				String userId=webUserService.saveUser(webUserDTO,roleId,userName);
				model.put("message", "SAVE_USER_SUCCESS");
				model.put("userId", userId);
				model.put("webUserDTO", new WebUserDTO());
				//return "redirect:/showUserForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		}catch (EOTException e) {
			flag=true;
			e.printStackTrace();
			model.put("message",e.getErrorCode());
			model.put("action", "add");
			return "addWebUser";
		}catch (Exception e) {
			flag=true;
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("action", "add");
			return "addWebUser";
		} finally{
			try {
				String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
				model.put("language", locationService.getLanguageList());
				model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
				model.put("lang",localeResolver.resolveLocale(request).toString());	
				model.put("mobilenumberLength",customerService.getMobileNumLength(webUserDTO.getCountryId()) );
				model.put("businessPartnerUsers",webUserService.getAllBusniessPartnerUser());
				if(flag==true) {
					Map<String, Object> masterData =webUserService.getMasterData(userName,1);
					masterData.put("branchList",webUserDTO.getBankId() != null?webUserService.getAllBranchFromBank(webUserDTO.getBankId()):null);
					model.put("masterData", masterData );
				}else {
				//	model.put("masterData",webUserService.getMasterData(userName,1));
					model.put("masterData", webUserService.getSearchWebUserList(webUserDTO, userName, 1));
				}

				if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN||
						webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN) {
					BankTellers bankTellers = bankTellerService.findByUserName(userName);
					if(bankTellers!=null){
						bankId = bankTellers.getBank().getBankId();
					} 
				}else {
					 businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
					 if(businessPartnerUser!=null){
					 seniorPartnerId= businessPartnerUser.getBusinessPartner().getId()!=null?businessPartnerUser.getBusinessPartner().getId():null;
					 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
					 model.put("businessNameList",  webUserService.getAllBusniessPartnerName(webUser.getWebUserRole().getRoleId(),seniorPartnerId,bankId));
					 model.put("bulkPaymentPartnerList",  webUserService.getAllBulkPaymentPartner(webUser.getWebUserRole().getRoleId(), bankId));
				}
				
				}
				int pageNumber=1;
					webUserDTO1=new WebUserDTO();	
					Page page=webUserService.getWebUserList(webUserDTO1, userName, pageNumber);
					page.requestPage = "searchWebUser.htm" ;
					model.put("page", page);
		
			} catch (EOTException e) {				
				model.put("message",ErrorConstants.SERVICE_ERROR);
				return "addWebUser";
			}
		}
		return "webUser";

	}

	@RequestMapping("/updateUser.htm")
	public String updateUser(@Valid WebUserDTO webUserDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request) {
		WebUserDTO webUserDTO1=null;
		boolean flag=false;
		try {
			if(result.hasErrors()){
				model.put("webUserDTO",webUserDTO);
				model.put("action", "edit");
			}else{
				webUserService.updateUser(webUserDTO);
				model.put("message", "UPDATE_USER_SUCCESS");
				model.put("webUserDTO", new WebUserDTO());			
				//return "redirect:/showUserForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		}catch (EOTException e) {
			 flag=true;
			e.printStackTrace();
			model.put("message",e.getErrorCode());
			model.put("action", "edit");
			return "addWebUser";
		} catch (Exception e) {
			flag=true;
			e.printStackTrace();
			model.put("action", "edit");
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "addWebUser";
		}finally{
			try {
				String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
				if(flag==true) {
					Map<String, Object> masterData =webUserService.getMasterData(userName,1);
					masterData.put("branchList",webUserDTO.getBankId() != null?webUserService.getAllBranchFromBank(webUserDTO.getBankId()):null);
					model.put("masterData", masterData );
				}else {
					model.put("masterData", webUserService.getSearchWebUserList(webUserDTO, userName, 1));
				}

				model.put("language", locationService.getLanguageList());
				model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
				model.put("lang",localeResolver.resolveLocale(request).toString());	
				model.put("mobilenumberLength",customerService.getMobileNumLength(webUserDTO.getCountryId()) );
				model.put("businessPartnerUsers",webUserService.getAllBusniessPartnerUser());
			
				int pageNumber=1;
					webUserDTO1=new WebUserDTO();	
					Page page=webUserService.getWebUserList(webUserDTO1, userName, pageNumber);
					page.requestPage = "searchWebUser.htm" ;
					model.put("page", page);	
			
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
				return "addWebUser";
			}
		}
		return "webUser";

	}

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting 
	@RequestMapping("/editUser.htm")
	public String editUser(WebUserDTO webUserDTO,Map<String, Object> model,WebUserDTO user,HttpServletRequest request,HttpServletResponse response) {

		try{
            Integer bankId = null;
			user = webUserService.getUser(webUserDTO.getUserName());
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			WebUser weUser =webUserService.getWebUserByUserName(userName);
			String bpUser = webUserDTO.getUserName();
			Integer roleId= weUser.getWebUserRole().getRoleId();
			
			Integer seniorPartnerId=null;
			BusinessPartner businessPartner=null;
			BusinessPartnerUser businessPartnerUser  = null;
			businessPartnerUser = businessPartnerService.getBusinessPartnerUser(bpUser);
			if(businessPartnerUser!=null) {
				model.put("businessPartnerName",  businessPartnerUser.getBusinessPartner());
				user.setAccessMode(businessPartnerUser.getAccessMode());
			}
			
			if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2|| 
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L3
					|| weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN)
				businessPartner= webUserService.getBusinessPartnerByUserName(userName);
			
			if(businessPartner!=null)
			{
				seniorPartnerId= businessPartner.getId()!=null?businessPartner.getId():null;	
			}
			if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L2||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BUSINESS_PARTNER_L3|| 
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN||
					weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN) {
				if(weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BA_ADMIN||
						weUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN) {
					BankTellers bankTellers = bankTellerService.findByUserName(userName);
					if(bankTellers!=null){
						bankId = bankTellers.getBank().getBankId();
					}
				}else {
					 businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
					 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				}
				model.put("businessNameList",  webUserService.getAllBusniessPartnerName(roleId,seniorPartnerId,bankId));
			}
			//@End
			
			model.put("webUserDTO",user );
			model.put("action", "edit");

		}catch (EOTException e) { 
			model.put("action", "edit");
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch(Exception e){ 
			model.put("action", "edit");
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			try {

				String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName() ;
				Map<String, Object> masterData = webUserService.getMasterData(loggedInUser,1) ;
				if(user.getRoleId()!=null){
					if( user.getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
							|| user.getRoleId() == EOTConstants.ROLEID_BANK_TELLER || user.getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
							|| user.getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER){
						masterData.put("branchList",webUserService.getAllBranchFromBank(user.getBankId()));
					}}
				model.put("masterData", masterData );
				model.put("language", locationService.getLanguageList());
				model.put("mobilenumberLength",customerService.getMobileNumLength(user.getCountryId()) );
				//model.put("businessNameList",  webUserService.getAllBusniessPartnerName(roleId,seniorPartnerId));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
				model.put("countryList", webUserService.getCountryList(localeResolver.resolveLocale(request).toString()));
				model.put("lang",localeResolver.resolveLocale(request).toString());	
				
				pageLogger(request,response,"EditWebUser");
			}
		}

		return "addWebUser";
	}

	@RequestMapping("/resetUserPassword.htm")
	public String resetUserPassword(@RequestParam String userName,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response) {

		try{
			System.out.println("UserName: "+userName);
			//webUserService.resetPassword(userName.substring(0,userName.lastIndexOf(",")));
			webUserService.resetPassword(userName);
			commonService.showUserManagementForm(request, model, response);
			model.put("message","PASSWORD_RESET_SUCCESS");
		}catch (EOTException e) { 
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch(Exception e){ 
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{	
			model.put("webUserDTO", new WebUserDTO());
			pageLogger(request,response,"ResetUserPassword");
		} 
		return "addWebUser";
		/*return "redirect:/showUserForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
	}
	
	@RequestMapping("/unblockUser.htm")
	public String unBlockUser(@RequestParam String userName,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response) {

		try{
			webUserService.unBlockUser(userName);
			model.put("message","UNBLOCK_USER_SUCCESS");
		}catch (EOTException e) { 
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch(Exception e){ 
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"UnblockUser");
		} 

		return "redirect:/showUserForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
	}

	
	@RequestMapping("/getBranches.htm")
	public String getBranches(@RequestParam Integer bankId, Map<String, Object> model){

		model.put("entity", "branchId");
		model.put("id", "branchId");
		model.put("value", "location");
		model.put("list", webUserService.getAllBranchFromBank(bankId));
		return "combo";
	}
	
	/*@RequestMapping("/getBusinessPartner.htm")
	public String getBusiness(Map<String, Object> model){
		//List<BusinessPartner> businessNameList = webUserService.getAllBusniessPartnerName();
		model.put("businessNameList",  webUserService.getAllBusniessPartnerName());
		return "businessNameList";
	}*/
	
	@RequestMapping("/getMobileNumberLength.htm")
	public String getMobileNumberLength(@RequestParam Integer country, Map<String, Object> model)throws EOTException{
		
		
		model.put("mobileNumLength",customerService.getMobileNumLength(country) );
		return "customerMobileNum";
	}
	
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/viewUser.htm")
	public String viewUser(WebUserDTO webUserDTO,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response) {

		try{
			
			WebUserDTO user = webUserService.getUser(webUserDTO.getUserName()) ;
			BusinessPartnerUser businessPartnerUser  = null;
			businessPartnerUser = businessPartnerService.getBusinessPartnerUser(webUserDTO.getUserName());
			if(businessPartnerUser!=null) {
					model.put("businessPartnerName",  businessPartnerUser.getBusinessPartner());		
			}
			//@End
			model.put("webUserDTO",user );
			model.put("action", "edit");

		}catch (EOTException e) { 
			model.put("action", "edit");
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch(Exception e){ 
			model.put("action", "edit");
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} 

		return "viewWebUser";
	}
	
	@RequestMapping("/exportToPdfWebUser.htm")
	public void exportToPDFWebUser(WebUserDTO webUserDTO,TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		int pageNumber=1;
		List results=null;
		List<String> businessPartners = new ArrayList<String>();

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		
		try {
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				webUserDTO = (WebUserDTO) session.getAttribute("webUserDTO");
			} else {
				session.setAttribute("webUserDTO", webUserDTO);
			}	
			
			Page page=webUserService.getWebUserList(webUserDTO, userName, pageNumber);
			results=page.getResults();
			page.requestPage = "searchWebUser.htm" ;
			model.put("page", page);
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			List<BusinessPartnerDTO> businessPartnerUsers= webUserService.getAllBusniessPartnerUser();
			Integer  tObject = null;
			
			
			for(tObject=0; tObject<results.size(); tObject++ ){
				WebUser Bpuser = (WebUser) results.get(tObject);
				Boolean flag=false;
			for(BusinessPartnerDTO bp:businessPartnerUsers){				
				if(bp.getWebUserName().equals(Bpuser.getUserName())){
					businessPartners.add(bp.getBusinessPartnerUserName());
					flag=true;
				//	model.put("businessPartner", bp.getBusinessPartnerUserName());
				}
			 }
			if(flag==false)
				businessPartners.add("");			
			}
			model.put("businessPartner", businessPartners);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}

		generatePDFReport(EOTConstants.JASPER_WEB_USER_JRXML_NAME, EOTConstants.WEB_USER_REPORT_NAME, results, model, request, response);

	
	}
	
	@RequestMapping("/exportToXlsWebUser.htm")
	public void exportToXlsWebUser(WebUserDTO webUserDTO,TxnSummaryDTO txnSummaryDTO,Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		int pageNumber=1;
		List<WebUser> results=null;
		HSSFWorkbook wb = null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser=customerService.getUser(userName);	
		
		try {
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				webUserDTO = (WebUserDTO) session.getAttribute("webUserDTO");
			} else {
				session.setAttribute("webUserDTO", webUserDTO);
			}	
			
			Page page=webUserService.getWebUserList(webUserDTO, userName, pageNumber);
			results=page.getResults();
			model.put("page", page);
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			model.put("userName", webUser.getUserName());
			model.put("date", dt1);
			List<BusinessPartnerDTO> businessPartnerUsers= webUserService.getAllBusniessPartnerUser();
			
			wb = wrapper.createSpreadSheetFromListForWebUsers(businessPartnerUsers,results, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.WEB_USER_PAGE_HEADER);
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
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}


	
	}
}
