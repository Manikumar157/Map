package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.MerchantDTO;
import com.eot.banking.dto.OutletDTO;
import com.eot.banking.dto.TerminalDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.MerchantService;
import com.eot.banking.service.TransactionService;
import com.eot.banking.service.WebUserService;

@Controller
public class MerchantController extends PageViewController {

	@Autowired
	TransactionService transactionService;
	@Autowired
	private MerchantService merchantService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private MessageSource messageSource;
	
	

	@RequestMapping("/searchMerchant.htm")
	public String searchMerchant(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
		String merchantName = "";
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String countryId = "";
		String bankGroupId = "";
		Map<String,Object> masterData = null;
		try{
			
			session.removeAttribute("customerName");
			session.removeAttribute("mobileNumber");
			session.removeAttribute("bankId");
			session.removeAttribute("branchId");
			session.removeAttribute("countryId");
			session.removeAttribute("bankGroupId");
			
			String merchantAction = request.getParameter("merchantAction");
			if(merchantAction==null || merchantAction.equals("")){
			merchantName = request.getParameter("merchantName");
			mobileNumber = request.getParameter("mobileNumber");
			bankId = request.getParameter("bankId");
			branchId = request.getParameter("branchId");
			countryId = request.getParameter("countryId");
			bankGroupId = request.getParameter("bankGroupId");
			}
			

			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			model.put("merchantList",merchantService.searchMerchant(userName,bankGroupId,merchantName,mobileNumber,bankId,branchId,countryId));
				
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				masterData=customerService.getMasterData(localeResolver.resolveLocale(request).toString());
				model.put("masterData",masterData );
				model.put("language",localeResolver.resolveLocale(request) );
				if (branchId != null &&  branchId != ""){
					if(branchId.equalsIgnoreCase("select")){
						branchId = "";
					}
					if(bankId!=null&&bankId!=""&&bankId!="select")
						masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId)));
				}
				model.put("merchantName", merchantName );
				model.put("mobileNumber", mobileNumber );
				model.put("bankId", bankId );
				model.put("branchId", branchId );
				model.put("countryId", countryId );
				model.put("bankGroupId", bankGroupId );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchCustomer");
		}

		return "viewMerchants";

	}

	@RequestMapping("/showMerchantForm.htm")
	public String showMerchantRegistrationForm(HttpServletRequest request,Map<String, Object> model,HttpServletResponse response) throws EOTException{
		try {

			MerchantDTO merchantDTO = new MerchantDTO();

			model.put("merchantDTO",merchantDTO);
			model.put("action","ADD");
			model.put("message", request.getParameter("message"));
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
		} catch (EOTException e) {
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"Merchant");
			model.put("City",customerService.getCityList(EOTConstants.COUNTRY_SOUTH_SUDAN));
		}	

		return "merchant";

	}

	@RequestMapping("/saveMerchant.htm")
	public String saveMerchant(@Valid MerchantDTO merchantDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request){
		try {

			if(result.hasErrors()){
				model.put("merchantDTO",merchantDTO);				
				return "merchant";
			}		
			
			if(merchantDTO.getMerchantId() == null ){
				if(merchantService.checkIfEmailAlreadyExists(merchantDTO)) {
					model.put("merchantDTO", merchantDTO);
					model.put("message", ErrorConstants.EMAIL_ALREADY_EXISTS);
					return "merchant";
				}
				if(merchantService.checkIfMobileAlreadyExists(merchantDTO)) {
					model.put("merchantDTO", merchantDTO);
					model.put("message", "ERROR_5016");
					return "merchant";
				}
				Integer merchantId = merchantService.saveMerchant(merchantDTO);
				merchantDTO.setMerchantId(merchantId);
				model.put("message", "MERCHANT_REG_SUCCESS");
			} else{
				if(merchantService.checkIfUpdateEmailExists(merchantDTO) && merchantService.checkIfUpdateMobileExists(merchantDTO)) {
					merchantService.updateMerchant(merchantDTO);
					model.put("message", "MERCHANT_EDIT_SUCCESS");
					return "redirect:/viewMerchant.htm?merchantId="+merchantDTO.getMerchantId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
				}
				if(merchantService.checkIfUpdateEmailExists(merchantDTO)){
					if(!merchantService.checkIfMobileAlreadyExists(merchantDTO) && !merchantService.checkIfUpdateMobileExists(merchantDTO)) {
						merchantService.updateMerchant(merchantDTO);
						model.put("message", "MERCHANT_EDIT_SUCCESS");
						return "redirect:/viewMerchant.htm?merchantId="+merchantDTO.getMerchantId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
					}
				}
				if(merchantService.checkIfUpdateMobileExists(merchantDTO)){
					if(!merchantService.checkIfEmailAlreadyExists(merchantDTO) && !merchantService.checkIfUpdateEmailExists(merchantDTO)){
						merchantService.updateMerchant(merchantDTO);
						model.put("message", "MERCHANT_EDIT_SUCCESS");
						return "redirect:/viewMerchant.htm?merchantId="+merchantDTO.getMerchantId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
					}
				}
				if(merchantService.checkIfUpdateEmailExists(merchantDTO)==false && merchantService.checkIfEmailAlreadyExists(merchantDTO)) {
						model.put("merchantDTO", merchantDTO);
						model.put("message", ErrorConstants.EMAIL_ALREADY_EXISTS);
						return "merchant";
				}
				if(merchantService.checkIfUpdateMobileExists(merchantDTO)==false && merchantService.checkIfMobileAlreadyExists(merchantDTO)) {
					model.put("merchantDTO", merchantDTO);
					model.put("message", "ERROR_5016");
					return "merchant";
				}
				merchantService.updateMerchant(merchantDTO);
				model.put("message", "MERCHANT_EDIT_SUCCESS");
			}			
			return "redirect:/viewMerchant.htm?merchantId="+merchantDTO.getMerchantId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("customerDTO",merchantDTO);
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("customerDTO",merchantDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}  finally{
			try {
				model.put("customerDTO",merchantDTO);
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("City",merchantDTO.getCountryId() != null?customerService.getCityList(merchantDTO.getCountryId()):null);
				model.put("quarter",merchantDTO.getCityId() != null?customerService.getQuarterList(merchantDTO.getCityId()):null);
				model.put("mobileNumLength", customerService.getMobileNumLength(merchantDTO.getCountryId()));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "merchant";
	}

	@RequestMapping("/viewMerchant.htm")
	public String viewMerchant(@RequestParam Integer merchantId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			MerchantDTO dto = merchantService.getMerchantDetails(merchantId);
			//dto.setMerchantId(merchantId);

			model.put("merchantDTO",dto);
			model.put("City",customerService.getCityList(dto.getCountryId()));
			model.put("quarter",customerService.getQuarterList(dto.getCityId()));
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("message",request.getParameter("message"));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );

			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"ViewCustomer");
		}
		return "viewMerchant";
	}
	
	@RequestMapping("/editMerchant.htm")
	public String editMerchant(@RequestParam Integer merchantId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			MerchantDTO dto = merchantService.getMerchantDetails(merchantId);

			model.put("merchantDTO",dto);
			model.put("City",customerService.getCityList(dto.getCountryId()));
			model.put("quarter",customerService.getQuarterList(dto.getCityId()));
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("message",request.getParameter("message"));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );

			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditMerchant");
		}
		return "merchant";
	}
	
	@RequestMapping("/searchOutlet.htm")
	public String searchOutlet(@RequestParam Integer merchantId,Map<String,Object> model,HttpServletRequest request,
			HttpSession session,HttpServletResponse response, OutletDTO outletViewForm){
		
		String mobileNumber = "";
		String countryId = "";
		String location = "";
		String status = ""; 
		outletViewForm.setMerchantId(merchantId);
		Map<String,Object> masterData = null;
		try{
			mobileNumber = request.getParameter("mobileNumber");
			countryId = request.getParameter("countryId");
			location = request.getParameter("location");
			status = request.getParameter("status");
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			int tmpStatus=0;
			if(status != null && !status.equals(""))
				tmpStatus=Integer.parseInt(status);
			model.put("outletList",merchantService.searchOutlet(userName,mobileNumber,location,countryId,tmpStatus, merchantId));
			
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		
		}finally{
			try {
				
				masterData = customerService.getMasterData(localeResolver.resolveLocale(request).toString());
				model.put("masterData",masterData );
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("mobileNumber", mobileNumber );
				model.put("countryId", countryId );
			    model.put("status", status );
				model.put("location", location );
				model.put("merchantId", merchantId );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchOutlet");
		}

		return "viewOutlets";
	}
	// bugId-505 by:rajlaxmi for:showing datatable with active/deactive status
	@RequestMapping("/showAllOutlets.htm")
	public String showAllOutlets(@RequestParam Integer merchantId,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws EOTException{
		
		Map<String,Object> masterData = null;
		model.put("merchantId", merchantId);
		try{
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			model.put("outletList",merchantService.showAllOutlets(userName,null,null,null,null));
				
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				masterData = customerService.getMasterData(localeResolver.resolveLocale(request).toString());
				model.put("masterData",masterData );
				model.put("language",localeResolver.resolveLocale(request) );
			
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchOutlet");
		}

		return "viewOutlets";
	
	}
	
	
	@RequestMapping("/showMechantOutlets.htm")
	public String showMerchantOutlets(@RequestParam Integer merchantId,Map<String,Object> model,
			HttpServletRequest request,HttpSession session,HttpServletResponse response,OutletDTO outletViewForm) throws EOTException{
		
		Map<String,Object> masterData = null;
		model.put("merchantId", merchantId);
		outletViewForm.setMerchantId(merchantId);
		
		try{
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			model.put("outletList",merchantService.getOutletList(merchantId));
				
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				masterData = customerService.getMasterData(localeResolver.resolveLocale(request).toString());
				model.put("masterData",masterData );
				model.put("language",localeResolver.resolveLocale(request) );
			
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"SearchOutlet");
		}

		return "viewOutlets";
	
	}
	
	@RequestMapping("/showOutletForm.htm")
	public String showOutletRegistrationForm(Integer merchantId,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response) throws EOTException{
		try {

			OutletDTO outletDTO = new OutletDTO();
			outletDTO.setMerchantId(merchantId);
			model.put("outletDTO",outletDTO);
			model.put("action","ADD");
			model.put("message", request.getParameter("message"));
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("merchantId",merchantId);
			model.put("mccList",merchantService.getAllActiveMCC());
		} catch (EOTException e) {
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"Merchant");
			model.put("City",customerService.getCityList(EOTConstants.COUNTRY_SOUTH_SUDAN));
		}	

		return "outlet";

	}

	@RequestMapping("/saveOutlet.htm")
	public String saveOutlet(@Valid OutletDTO outletDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request){
		try {

			if(result.hasErrors()){
				model.put("OutletDTO",outletDTO);				
				return "outlet";
			}		

			if(outletDTO.getOutletId() == null ){
				Integer outletId = merchantService.saveOutlet(outletDTO);
				outletDTO.setOutletId(outletId);
				model.put("message", "MERCHANT_REG_SUCCESS");
			}else{
				merchantService.updateOutlet(outletDTO);
				model.put("message", "MERCHANT_EDIT_SUCCESS");
			}

			return "redirect:/viewOutlet.htm?outletId="+outletDTO.getOutletId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("customerDTO",outletDTO);
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("customerDTO",outletDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}  finally{
			try {
				model.put("customerDTO",outletDTO);
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("City",outletDTO.getCountryId() != null?customerService.getCityList(outletDTO.getCountryId()):null);
				model.put("quarter",outletDTO.getCityId() != null?customerService.getQuarterList(outletDTO.getCityId()):null);
				model.put("mobileNumLength", customerService.getMobileNumLength(outletDTO.getCountryId()));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "outlet";
	}
	
	@RequestMapping("/viewOutlet.htm")
	public String viewOutlet(@RequestParam Integer outletId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			OutletDTO dto = merchantService.getOutletDetails(outletId);

			model.put("outletDTO",dto);
			model.put("City",customerService.getCityList(dto.getCountryId()));
			model.put("quarter",customerService.getQuarterList(dto.getCityId()));
			model.put("message",request.getParameter("message"));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("mccList",merchantService.getAllActiveMCC());
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"ViewCustomer");
		}
		return "viewOutlet";
	}
	
	@RequestMapping("/editOutlet.htm")
	public String editOutlet(@RequestParam Integer outletId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			OutletDTO dto = merchantService.getOutletDetails(outletId);

			model.put("outletDTO",dto);
			model.put("City",customerService.getCityList(dto.getCountryId()));
			model.put("quarter",customerService.getQuarterList(dto.getCityId()));
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("message",request.getParameter("message"));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("mccList",merchantService.getAllActiveMCC());
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditOutlet");
		}
		return "outlet";
	}
	
	@RequestMapping("/searchTerminal.htm")
	public String searchTerminal(@RequestParam Integer outletId,@RequestParam Integer merchantId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		String mobileNumber = "";
		String bankId = "";
		String branchId = "";
		String status = "";
		Map<String,Object> masterData = null;
		try{
			mobileNumber = request.getParameter("mobileNumber");
			bankId = request.getParameter("bankId");
			branchId = request.getParameter("branchId");
			if(null != branchId && !branchId.isEmpty() && branchId.equalsIgnoreCase("select"))
				branchId = "";
			status = request.getParameter("status");
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			if(null!=bankId)
				model.put("terminalList",merchantService.searchTerminal(userName,outletId,mobileNumber,bankId.trim(),branchId,status));
			else
				model.put("terminalList",merchantService.searchTerminal(userName,outletId,mobileNumber,bankId,branchId,status));

		}catch(EOTException e){
			model.put("message",e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				masterData = customerService.getMasterData(localeResolver.resolveLocale(request).toString());
				model.put("masterData",masterData );
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("mobileNumber",mobileNumber );
				model.put("bankId",bankId );
				model.put("branchId",branchId );
				model.put("status",status );
				model.put("outletId", outletId);
				model.put("merchantId", merchantId);
				
				if(bankId!=null&&bankId!=""&&bankId!="select")
					masterData.put("branchList",webUserService.getAllBranchFromBank(Integer.parseInt(bankId.trim())));
				
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditMerchant");
		}
		return "searchTerminal";
	}
	@RequestMapping("/addTerminal.htm")
	public String addTerminal(@RequestParam Integer outletId, Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) throws EOTException{
		TerminalDTO terminalDTO = new TerminalDTO();
		OutletDTO dto = merchantService.getOutletDetails(outletId);

		terminalDTO.setMerchantId(dto.getMerchantId());
		terminalDTO.setOutletId(outletId);
		model.put("terminalDTO",terminalDTO);
		try {
			model.put("merchantList", merchantService.getMerchantList(1));
		} catch (EOTException e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "addTerminal";
	}
	@RequestMapping("/saveTerminal.htm")
	public String saveTerminal(@Valid TerminalDTO terminalDTO, BindingResult result, Map<String, Object> model,HttpServletRequest request){
		try {

			if(result.hasErrors()){
				model.put("terminalDTO",terminalDTO);				
				return "addTerminal";
			}		

			if(terminalDTO.getTerminalId() == null ){
				Integer terminalId = merchantService.saveTerminal(terminalDTO);
				terminalDTO.setTerminalId(terminalId);
				model.put("message", "TERMINAL_REG_SUCCESS");
			}else{
				merchantService.updateTerminal(terminalDTO);
				model.put("message", "TERMINAL_EDIT_SUCCESS");
			}

			return "redirect:/searchTerminal.htm?outletId="+terminalDTO.getOutletId()+"&merchantId="+terminalDTO.getMerchantId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("terminalDTO",terminalDTO);
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("terminalDTO",terminalDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}  finally{
			try {
				model.put("terminalDTO",terminalDTO);
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
				//model.put("mobileNumLength", customerService.getMobileNumLength(merchantDTO.getCountryId()));
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "searchTerminal";
	}
	@RequestMapping("/getOutletList.htm")
	public String getOutletList(@RequestParam Integer merchant, Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("entity", "outletId");
			model.put("id", "outletId");
			model.put("value", "location");
			model.put("list", merchantService.getOutletList(merchant));
		} catch (EOTException e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "combo";
	}
	
	@RequestMapping("/viewTerminal.htm")
	public String viewTerminal(@RequestParam Integer terminalId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			TerminalDTO dto = merchantService.getTerminalDetails(terminalId);

			model.put("terminalDTO",dto);
			model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("message",request.getParameter("message"));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );

			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"ViewCustomer");
		}
		return "viewTerminal";
	}
	@RequestMapping("/editTerminal.htm")
	public String editTerminal(@RequestParam Integer terminalId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			TerminalDTO dto = merchantService.getTerminalDetails(terminalId);

			model.put("terminalDTO",dto);
			model.put("merchantList", merchantService.getMerchantList(dto.getMerchantId()));
			model.put("location", merchantService.getOutletList(dto.getOutletId()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("message",request.getParameter("message"));

		}catch(EOTException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				model.put("masterData", customerService.getMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("language",localeResolver.resolveLocale(request) );
			} catch (EOTException e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditOutlet");
		}
		return "addTerminal";
	}
}

