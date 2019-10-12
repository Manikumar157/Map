/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: BusinessPartnerController.java
*
* Date Author Changes
* Oct 16, 2018 Vinod Joshi Created
*/
package com.eot.banking.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.BankTellerService;
import com.eot.banking.service.BusinessPartnerService;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.DashboardService;
import com.eot.banking.service.TransactionService;
import com.eot.banking.service.WebUserService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.Page;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.Country;
import com.eot.entity.WebUser;

/**
 * The Class BusinessPartnerController.
 */
@Controller
public class BulkPaymentPartnerController extends PageViewController{
	
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

	/** The business partner service. */
	@Autowired
	private BusinessPartnerService businessPartnerService;
	
	/** The bank teller service. */
	@Autowired
	private BankTellerService bankTellerService; 
	
	/** The message source. */
	@Autowired
	private MessageSource messageSource;
	
	/** The wrapper. */
	@Autowired
	private ExcelWrapper wrapper;
	
	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;
	
	@Autowired
	private DashboardService dashBoardService;
	
	/**
	 * Adds the customer request.
	 *
	 * @param request the request
	 * @param model the model
	 * @param response the response
	 * @return the string
	 */
/*	@RequestMapping("/addBusinessPartner.htm")
	public String addCustomerRequest(HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){
	//  model.put("language",localeResolver.resolveLocale(request) );

		pageLogger(request,response,"addCustomerRequest");
		//	return "addCustomer";
		return "redirect:/showBusinessPartnerForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");

	}
	
	*//**
	 * Search buisness partner.
	 *
	 * @param model the model
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/bulkpaymentpartnerPartner.htm")
	public String searchBuisnessPartner(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		String name = "";
		String contactPerson = "";
		String contactNumber = "";
		String code = "";
		String fromDate = "";
		String toDate = "";
		String partnerType = "";
		String seniorPartner = "";
		Integer bankId = null;
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser webUser=customerService.getUser(userName);
		session.setAttribute("roleId", webUser.getWebUserRole().getRoleId());
		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			name = request.getParameter("nameV");
			contactPerson = request.getParameter("contactPersonV");
			contactNumber = request.getParameter("contactNumberV");
			code = request.getParameter("codeV");
			fromDate = request.getParameter("fromDateV");
			toDate = request.getParameter("toDateV");
			
			/*session.setAttribute("name", name);
			session.setAttribute("contactPerson", contactPerson);
			session.setAttribute("contactNumber", contactNumber);
			session.setAttribute("code", code );
			session.setAttribute("fromDate", fromDate );
			session.setAttribute("toDate", toDate);*/

	
	
	filterUserForBusinessPartner(model, name, contactPerson, contactNumber, code, fromDate, toDate, partnerType,
			seniorPartner, bankId, userName, webUser, pageNumber);	
	} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
	finally{
			try {
				model.put("BusinessPartnerDTO", new BusinessPartnerDTO());
				model.put("name", name);				
				model.put("contactNumber",contactNumber);
				model.put("fromDate",fromDate);
				model.put("toDate",toDate);	
				model.put("KycList",businessPartnerService.getKycList());
				model.put("message", request.getParameter("message") != null ? request.getParameter("message") : "");
				Integer country = webUser.getCountry().getCountryId();
				model.put("mobileNumLength", customerService.getMobileNumLength(country));
			/*} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}*/
						} catch (Exception e) {
							e.printStackTrace();
							model.put("message",ErrorConstants.SERVICE_ERROR);
						}
			pageLogger(request,response,"SearchBusinessPartner");
		}

		return "viewBulkPaymentPartners";

	}

	private void filterUserForBusinessPartner(Map<String, Object> model, String name, String contactPerson,
			String contactNumber, String code, String fromDate, String toDate, String partnerType, String seniorPartner,
			Integer bankId, String userName, WebUser webUser, Integer pageNumber) throws EOTException {
		if ( webUser.getWebUserRole().getRoleId() != EOTConstants.ROLEID_BUSINESS_PARTNER_L3 )
		{	
		 if ( 
					webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER ||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER) {
			 BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
				if(teller == null){
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
			 bankId = teller.getBank().getBankId();
			partnerType = "4";		
		 }
		else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ) {
			partnerType = "2";
			BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
		}
		else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ) {
			partnerType = "3";	
			BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
		}else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN ) {
			 partnerType = "1";
		}		 
		 if(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN 
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER){
		Page page=businessPartnerService.searchBusinessPartners(name,contactPerson,contactNumber,code,userName,pageNumber,fromDate,toDate,partnerType,seniorPartner,bankId);
				page.requestPage = "bulkpaymentpartnerPartner.htm";
				model.put("page",page);	
			/*}catch(EOTException e) {
				e.printStackTrace();
				model.put("message", e.getErrorCode());*/
		}
		else if(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BA_ADMIN){
			Page page = businessPartnerService.getAllBusinessPartner(pageNumber);
			model.put("page",page);	
		}
		 }
	}
	
	/**
	 * View business partner.
	 *
	 * @param businessPartnerDTO the business partner DTO
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/viewBulkpaymentpartner.htm")
	public String viewBusinessPartner(BusinessPartnerDTO businessPartnerDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		String name = "";
		String contactPerson = "";
		String contactNumber = "";
		String code = "";
		String fromDate = "";
		String toDate = "";
		String partnerType="";
		String seniorPartner=""	;
		String userName = "";
		Integer bankId = null;


		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			
			name = request.getParameter("nameV");
			contactNumber = request.getParameter("contactNumberV");
			fromDate = request.getParameter("fromDateV");
			toDate = request.getParameter("toDateV");
			code = request.getParameter("codeV");
			contactPerson = request.getParameter("contactPersonV");
			partnerType = request.getParameter("partnerType"); 
			String roleId = request.getParameter("roleId"); 
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			userName = auth.getName();
			WebUser webUser=customerService.getUser(userName);
			
			if (  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER 
					) {
			 BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
				if(teller == null){
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
			 bankId = teller.getBank().getBankId();
				
		 }
		else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 
								||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 
								
								) {
			BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
		
		}
			BusinessPartnerDTO dto = businessPartnerService.getBusinessPartnerDetails(Long.parseLong(businessPartnerDTO.getId().toString()),auth.getName());
			seniorPartner = dto.getId().toString();
			Page page=null;
			if(dto.getPartnerType() != 3) {
			 page=businessPartnerService.searchBusinessPartners(name,contactPerson,contactNumber,code,userName,pageNumber,fromDate,toDate,partnerType,seniorPartner,bankId);
			 page.requestPage = "businessPartner.htm";
			}
			model.put("page",page);	
			
			//@END
	//		dto.setLanguage(customerService.getLanguageDescription(dto.getLanguage()));
			model.put("BusinessPartnerDTO",dto);
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("message",request.getParameter("message"));
			model.put("seniorPartner", seniorPartner);
		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("name", name);				
				model.put("contactNumber",contactNumber);
				model.put("fromDate",fromDate);
				model.put("toDate",toDate);	
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("KycList",businessPartnerService.getKycList());
			} /*catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}*/
			catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"viewBusinessPartners");
		}
		return "viewBulkPaymentPartner";


	}
	
	
	
	/**
	 * Adds the business partner.
	 *
	 * @param businessPartnerDTO the business partner DTO
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @return the string
	 * @throws Exception the exception
	 */
	@RequestMapping("/saveBulkPaymentPartner.htm")
	public String addBusinessPartner(@Valid @ModelAttribute("businessPartnerDTO") BusinessPartnerDTO businessPartnerDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request) throws Exception{
		
		/*BusinessPartnerDTO businessPartnerDTO = new BusinessPartnerDTO();*/
		String name = "";
		String contactPerson = "";
		String contactNumber = "";
		String code = "";
		String fromDate = "";
		String toDate = "";
		String seniorPartner = "";
		Integer bankId = null;
		String userName = null;
		WebUser weUser = null;
		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		Boolean flag = false;
		try{	
			if(result.hasErrors()){
				model.put("BusinessPartnerDTO",businessPartnerDTO);				
				return "businesspartner";
			}
				userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			    weUser =webUserService.getWebUserByUserName(userName);
				BankTellers bankTellers = bankTellerService.findByUserName(userName);
				Integer SeniorID = null;
				if(bankTellers!=null){
					bankId = bankTellers.getBank().getBankId();
				}else {
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				
				if(businessPartnerUser!=null){
					SeniorID = businessPartnerUser.getBusinessPartner().getId();
					bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
					}
				}
				Integer roleId= weUser.getWebUserRole().getRoleId();
				if(roleId==EOTConstants.ROLEID_BA_ADMIN ||roleId==EOTConstants.ROLEID_BANK_ADMIN || roleId==EOTConstants.ROLEID_BANK_TELLER ){
					businessPartnerDTO.setPartnerType(EOTConstants.BULKPAYMENT_PARTNER_TYPE);
				}/*else if(roleId==EOTConstants.ROLEID_BUSINESS_PARTNER_L1) {
					businessPartnerDTO.setPartnerType(2);
				}else if(roleId==EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
					businessPartnerDTO.setPartnerType(3);
				}*//*else if(roleId==EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
					businessPartnerDTO.setPartnerType(4);
				}*/
				if(businessPartnerDTO.getId() == null ){
					businessPartnerService.saveBusinessPartner(businessPartnerDTO, SeniorID, bankId);					
					if(roleId==EOTConstants.ROLEID_BANK_ADMIN)
						model.put("message","ADD_BULKPAYMENT_PARTNER_SUCCESS");
					else 
						model.put("message","ADD_BULKPAYMENT_PARTNER_SUCCESS");
					//return "viewBusinessPartners";
				
				}else{
					businessPartnerService.updateBusinessPartner(businessPartnerDTO,SeniorID,bankId);
					if(roleId==EOTConstants.ROLEID_BANK_ADMIN)
						model.put("message","EDIT_BULKPAYMENT_PARTNER_SUCCESS");
					else 
						model.put("message", "EDIT_BULKPAYMENT_PARTNER_SUCCESS");
				//	String message = "EDIT_BUSINESSPARTNER_SUCCESS";
					//return "viewBusinessPartners";
					//return "redirect:/businessPartner.htm?message="+message+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
				}			
		}
		catch(EOTException e){
			flag = true;
			e.printStackTrace();
			model.put("message",e.getErrorCode());	 
			return "bulkpaymentpartner";
		}catch (Exception e) {
			flag = true;
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "bulkpaymentpartner";
		}finally {
			Integer country = weUser.getCountry().getCountryId();
			model.put("mobileNumLength", customerService.getMobileNumLength(country));
			filterUserForBusinessPartner(model, name, contactPerson, contactNumber, code, fromDate, toDate, businessPartnerDTO.getPartnerType().toString(),
					seniorPartner, bankId, userName, weUser, pageNumber);
			model.put("BusinessPartnerDTO", flag == false ? new BusinessPartnerDTO() : businessPartnerDTO);
			model.put("KycList",businessPartnerService.getKycList());
		}
		return "viewBulkPaymentPartners";
	}
	
	/**
	 * Show customer registration form.
	 *
	 * @param request the request
	 * @param model the model
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/showBulkPaymentForm.htm")
	public String showCustomerRegistrationForm(HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){
		BusinessPartnerDTO businessPartnerDTO = new BusinessPartnerDTO();
		try {
			//String mobileNumber = request.getParameter("mobileNumber");
			//customerDTO.setMobileNumber(mobileNumber);
			//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//customerDTO = customerService.getUserForProof(auth.getName(),customerDTO);
			//model.put("action","ADD");
			//model.put("message", request.getParameter("message"));
			//model.put("language",localeResolver.resolveLocale(request) );
		//} catch (EOTException e) {
			//model.put("message",e.getErrorCode());
			
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			WebUser weUser =webUserService.getWebUserByUserName(userName);
			Integer country = weUser.getCountry().getCountryId();
			model.put("mobileNumLength", customerService.getMobileNumLength(country));
			System.out.println("Show businssPartner");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			model.put("BusinessPartnerDTO",businessPartnerDTO);
			model.put("KycList",businessPartnerService.getKycList());
			pageLogger(request,response,"BusinessPartner");
			
		}	

		return "bulkpaymentpartner";

	}
	
	/**
	 * Search business partner.
	 *
	 * @param model the model
	 * @param request the request
	 * @param session the session
	 * @param response the response
	 * @return the string
	 */
	
	@RequestMapping("/searchBulkPaymentPartner.htm")
	public String searchBusinessPartner(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		String name = "";
		String contactNumber = "";
		String fromDate = "";
		String toDate = "";
		String code = "";
		String contactPerson = "";
		String partnerType = "";
		String seniorPartner=""	;
		Integer bankId=null;
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser webUser=customerService.getUser(userName);
		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			name = request.getParameter("nameV");
			contactNumber = request.getParameter("contactNumberV");
			fromDate = request.getParameter("fromDateV");
			toDate = request.getParameter("toDateV");
			code = request.getParameter("codeV");
			contactPerson = request.getParameter("contactPersonV");
			partnerType = request.getParameter("partnerType"); 

			/*if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN )
				partnerType = "1";		
			else*/ if ( 
					 webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				partnerType = "4";		
				BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
				if(teller == null){
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
			 bankId = teller.getBank().getBankId();
			}
			/*else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ) {
				partnerType = "2";
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();	
			}
			else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ) {
				partnerType = "3";
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
			}*/
			Page page=businessPartnerService.searchBusinessPartners(name,contactPerson,contactNumber,code,userName,pageNumber,fromDate,toDate, partnerType,seniorPartner,bankId);
			page.requestPage = "searchBusinessPartnerPage.htm";
			model.put("page",page);	
		}/*catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}*/ catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				model.put("language",localeResolver.resolveLocale(request));
				model.put("BusinessPartnerDTO", new BusinessPartnerDTO());
				model.put("name", name);
				model.put("contactNumber",contactNumber);
				model.put("fromDate",fromDate);
				model.put("toDate",toDate);	
				model.put("code",code);
				model.put("contactPerson",contactPerson);	
				Integer country = webUser.getCountry().getCountryId();
				model.put("mobileNumLength", customerService.getMobileNumLength(country));
			} /*catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}*/
			catch (Exception e) {
				e.printStackTrace();
			}
			pageLogger(request,response,"SearchBusinessPartner");
		}

		return "viewBulkPaymentPartners";
	}
	
	@RequestMapping("/editBulkPaymentPartner.htm")
	public String editCustomer(BusinessPartnerDTO businessPartnerDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		
		String name = "";
		String contactNumber = "";
		String fromDate = "";
		String toDate = "";
		String code = "";
		String contactPerson = "";
//		String partnerType = "";				

		BusinessPartnerDTO dto = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName() ;
		WebUser webUser=customerService.getUser(userName);
		try{
	//		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			name = request.getParameter("nameV");
			contactNumber = request.getParameter("contactNumberV");
			fromDate = request.getParameter("fromDateV");
			toDate = request.getParameter("toDateV");
			code = request.getParameter("codeV");
			contactPerson = request.getParameter("contactPersonV");
//			partnerType = request.getParameter("partnerType") == null ? (type == null ? "" : type+"") : request.getParameter("partnerType");

			
	//		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;			
			dto = businessPartnerService.getBusinessPartnerDetails(Long.parseLong(businessPartnerDTO.getId().toString()),auth.getName());
			
			
			model.put("BusinessPartnerDTO",dto);
	
	//		model.put("language",localeResolver.resolveLocale(request) );
		
			Integer country = webUser.getCountry().getCountryId();
			model.put("mobileNumLength", customerService.getMobileNumLength(country));

		}catch(EOTException e){
			e.printStackTrace();
			model.put("BusinessPartnerDTO",new BusinessPartnerDTO());
			model.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			model.put("name", name);
			model.put("contactNumber",contactNumber);
			model.put("fromDate",fromDate);
			model.put("toDate",toDate);	
			model.put("code",code);
			model.put("contactPerson",contactPerson);
			model.put("KycList",businessPartnerService.getKycList());
			
			
			try {
				model.put("language",localeResolver.resolveLocale(request));
				Integer country = webUser.getCountry().getCountryId();
				model.put("mobileNumLength", customerService.getMobileNumLength(country));	

			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditBusinessPartner");
		}
		return "bulkpaymentpartner";

	}
	
	/**
	 * Gets the photo.
	 *
	 * @param id the id
	 * @param type the type
	 * @param request the request
	 * @param response the response
	 * @return the photo
	 *//*
	@RequestMapping("/getBPPhoto.htm")
	public void getPhoto(@RequestParam Long id, @RequestParam String type, HttpServletRequest request, HttpServletResponse response){
		
		System.out.println(id +"   --  " + type );

		try {
			byte[] photo = businessPartnerService.getPhotoDetails(id, type) ;			
			OutputStream os = response.getOutputStream();
			String  kycImage= messageSource.getMessage("LABEL_ID_PROOF_NOT_FOUND",null,localeResolver.resolveLocale(request));
			if(photo!=null)
			{
				if(photo.length > 0){
					response.setContentType("image/jpeg");
					response.setStatus(HttpServletResponse.SC_OK);

					os.write(photo);
				}
				else if(type.equals("kycImage")&& photo.length <= 0 ){
					os.write(kycImage.getBytes());
				}
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	*//**
	 * Export to PDF business partners.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @param session the session
	 */
	@RequestMapping("/exportToPDFBulkPaymentPartners.htm")
	public void exportToPDFBusinessPartners(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
	
		List<BusinessPartner> list = null;

			String name = "";
			String contactPerson = "";
			String contactNumber = "";
			String code = "";
			String fromDate = "";
			String toDate = "";
			String partnerType = "";
			String seniorPartner = "";
			Integer bankId = null;
			Integer role = null;
			String header = "";
			
			
			try{
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
				String userName = auth.getName();
				
				WebUser webUser=customerService.getUser(userName);
				role = webUser.getWebUserRole().getRoleId();
				
				Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
				seniorPartner=request.getParameter("seniorPartnerV") != null ? request.getParameter("seniorPartnerV") : "";
				if(seniorPartner == "") {
					name = request.getParameter("nameV");
					contactPerson = request.getParameter("contactPersonV");
					contactNumber = request.getParameter("contactNumberV");
					code = request.getParameter("codeV");
					fromDate = request.getParameter("fromDateV");
					toDate = request.getParameter("toDateV");
				

	/*	if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN )
			partnerType = "1";		
		else */if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BA_ADMIN 
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			partnerType = "4";	
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if(teller == null){
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
		 bankId = teller.getBank().getBankId();
			model.put("entityCode",webUser.getUserName());
			role = EOTConstants.ROLEID_BANK_ADMIN;
		}
		else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ) {
			partnerType = "2";
			BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
			model.put("entityCode", businessPartnerService.getBusinessPartnerUser(userName).getBusinessPartner().getCode());
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
			role = EOTConstants.ROLEID_BUSINESS_PARTNER_L1;
		}
		else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ) {
			partnerType = "3";	
			BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
			 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
			model.put("entityCode", businessPartnerService.getBusinessPartnerUser(userName).getBusinessPartner().getCode());
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
			role = EOTConstants.ROLEID_BUSINESS_PARTNER_L2;
		}

				}else {
				BusinessPartnerDTO dto = businessPartnerService.getBusinessPartnerDetails(Long.parseLong(seniorPartner.toString()),auth.getName());				
				model.put("entityCode", dto.getCode());
				role = null;
				}
				header = null != role && role == EOTConstants.ROLEID_BANK_ADMIN ?EOTConstants.BULKPARTNER_DETAIL_PAGE_HEADER_PDF : EOTConstants.BULKPARTNER_DETAIL_PAGE_HEADER_PDF;			
				Page page=businessPartnerService.searchBusinessPartners(name,contactPerson,contactNumber,code,userName,pageNumber,fromDate,toDate,partnerType,seniorPartner,bankId);
	
		//		Object p1 = (Object)request.getParameter("pageV");
	   //		page = (Page)p1;
	  //		Page page = locationService.getCountriesList(pageNumber) ;
			page.requestPage = "businessPartner.htm";
			model.put("page",page);	
			model.put("header",header);
			model.put("role",role);
			model.put("lang",localeResolver.resolveLocale(request).toString());
			String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			list = page.getResults();

			model.put("userName", webUser.getUserName());
			model.put("date", dt1);

		}catch(Exception e){
			e.printStackTrace();
		}
			String fileName = null != role && role == EOTConstants.ROLEID_BANK_ADMIN ?EOTConstants.BULKAPAYMENT_PARTNER_REPORT_NAME : EOTConstants.BULKAPAYMENT_PARTNER_REPORT_NAME;
			generatePDFReport(EOTConstants.BULKPAYEMNT_PARTNER_JRXML_NAME, fileName, list, model, request, response);
	}

	/**
	 * Export excel.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @param session the session
	 */
	@RequestMapping("/bulkPaymentPartnerExcelReport.htm")
	public void exportExcel(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
		
		HSSFWorkbook wb = null;
		List<BusinessPartner> results=null;
		String name = "";
		String contactPerson = "";
		String contactNumber = "";
		String code = "";
		String fromDate = "";
		String toDate = "";
		String partnerType = "";
		String seniorPartner = "";
		String entityCode =null;
		Integer bankId = null;
		Integer role = null;
		String header = "";
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String userName = auth.getName() ;
			WebUser webUser=customerService.getUser(userName);
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			seniorPartner=request.getParameter("seniorPartnerV") != null ? request.getParameter("seniorPartnerV") : "";
			role = webUser.getWebUserRole().getRoleId();
			if(seniorPartner == "") {
				name = request.getParameter("nameV");
				contactPerson = request.getParameter("contactPersonV");
				contactNumber = request.getParameter("contactNumberV");
				code = request.getParameter("codeV");
				fromDate = request.getParameter("fromDateV");
				toDate = request.getParameter("toDateV");
				partnerType = "";
				seniorPartner = "";
							
	/*
			if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN )
				partnerType = "1";		
			else */if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BA_ADMIN 
						|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
						|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
				partnerType = "4";	
				BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
				if(teller == null){
					throw new EOTException(ErrorConstants.INVALID_TELLER);
				}
			 bankId = teller.getBank().getBankId();
				entityCode = webUser.getUserName();
				role = EOTConstants.ROLEID_BANK_ADMIN;
			}
			else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 ) {
				partnerType = "2";
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				entityCode = businessPartnerService.getBusinessPartnerUser(userName).getBusinessPartner().getCode();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
				role = EOTConstants.ROLEID_BUSINESS_PARTNER_L1;
			}
			else if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 ) {
				partnerType = "3";	
				BusinessPartnerUser businessPartnerUser = businessPartnerService.getBusinessPartnerUser(userName);
				 bankId = businessPartnerUser.getBusinessPartner().getBank().getBankId();
				entityCode = businessPartnerService.getBusinessPartnerUser(userName).getBusinessPartner().getCode();
				seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
				role = EOTConstants.ROLEID_BUSINESS_PARTNER_L2;
			}
			}else {
			BusinessPartnerDTO dto = businessPartnerService.getBusinessPartnerDetails(Long.parseLong(seniorPartner.toString()),auth.getName());
			 entityCode = dto.getCode();
			 role = null;
			}
			header = null != role && role == EOTConstants.ROLEID_BANK_ADMIN ?EOTConstants.BULKPARTNER_DETAIL_DETAILS_PAGE_HEADER : EOTConstants.BULKPARTNER_DETAIL_DETAILS_PAGE_HEADER;
			Page page=businessPartnerService.searchBusinessPartners(name,contactPerson,contactNumber,code,userName,pageNumber,fromDate,toDate,partnerType,seniorPartner,bankId);
			results = page.getResults();
			wb = wrapper.createSpreadSheetFromListForBulkPaymentPartner(results, localeResolver.resolveLocale(request), messageSource, webUser, header,entityCode,role);
			//String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			  String date = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
			String fileName = null != role && role == EOTConstants.ROLEID_BANK_ADMIN ?EOTConstants.BULKAPAYMENT_PARTNER_REPORT_NAME : EOTConstants.BULKAPAYMENT_PARTNER_REPORT_NAME;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename= "+ fileName
				+ date + "_" + System.currentTimeMillis() + "_report.xls");
		OutputStream os = response.getOutputStream();

		wb.write(os);
		os.flush();
		os.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}

	}
	
/*	@RequestMapping("/getPartnersByType.htm")
	public String getPartnersByType(@RequestParam Integer partnerType, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
		List<BusinessPartner> partnerList = dashBoardService.loadBusinessPartnerByType(partnerType);
		model.put("entity", "id");
		model.put("id", "id");
		model.put("value", "name");
		model.put("list", partnerList);
		
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "combo";
	}*/

}
