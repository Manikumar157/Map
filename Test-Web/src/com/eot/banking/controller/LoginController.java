package com.eot.banking.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dto.ApplicationVersionDTO;
import com.eot.banking.dto.DashboardDTO;
import com.eot.banking.dto.ForgotPasswordDTO;
import com.eot.banking.dto.LoginUserDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.ApplicationVersionService;
import com.eot.banking.service.BankTellerService;
import com.eot.banking.service.BrandingService;
import com.eot.banking.service.DashboardService;
import com.eot.banking.service.LocationService;
import com.eot.banking.service.TxnSummaryService;
import com.eot.banking.service.WebUserService;
import com.eot.entity.BankTellers;
import com.eot.entity.Branding;
import com.eot.entity.WebUser;

@Controller
public class LoginController extends PageViewController {

	@Autowired
	private WebUserService webUserService;
	@Autowired
	private TxnSummaryService txnSummaryService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private ApplicationVersionService applicationVersionService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private BankTellerService bankTellerService;
	@Autowired
	private BrandingService brandingService;
	@Autowired
	private DashboardService dashboardServic;
	private DashboardService dashBoardService;
	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;

	@RequestMapping("/welcome.htm")
	public String login(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response,TxnSummaryDTO txnSummaryDTO){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HttpSession session=request.getSession(true);
		String version="";
		version = applicationVersionService.getApplicationVersionService();
		session.setAttribute("version", version);
		if( auth!=null ){

			try {
				WebUserDTO webUserDTO = webUserService.getUser(auth.getName());

				if(webUserDTO.getAlternateNumber().equals(EOTConstants.NEW_WEB_USER)){
					return "redirect:/changePasswordForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				}
				boolean otpFlag = webUserService.generateOTPIfEnabled(auth.getName());
				session.setAttribute("username", auth.getName());

				if(otpFlag){
					model.put("message", "PLEASE_ENTER_OTP");
					return "otp";
				}else{
					try{
						session.removeAttribute("txnBankId");
						session.removeAttribute("txnCountryId");
						session.removeAttribute("transactionType");
						session.removeAttribute("fromDate");
						session.removeAttribute("toDate");
						model.put("masterData", txnSummaryService.getTxnSummaryMasterData(localeResolver.resolveLocale(request).toString()));
						model.put("txnSummaryDTO",new TxnSummaryDTO());
						
						Branding branding=null;
						
						if (!webUserDTO.getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN) && !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BANK_GROUP_ADMIN )
								&& !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BA_ADMIN) && !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1 )
								&& !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2 )&& !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3 )
								&& !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_SALES_OFFICERS) && !webUserDTO.getRoleId().equals(EOTConstants.ROLEID_BULKPAYMENT_PARTNER)) {
							branding = brandingService.findByBankId(bankTellerService
									.findByUserName(webUserDTO.getUserName()).getBank().getBankId());
						}request.getSession(false).getAttribute("buttonColor");
						if(null !=branding){
							request.getSession(false).setAttribute(EOTConstants.BANK_THEME, branding.getBankThemeColor()!=null?branding.getBankThemeColor():EOTConstants.BANK_DEFAULT_THEME);
							request.getSession(false).setAttribute(EOTConstants.BANK_BTN_COLOR, branding.getButtonColor()!=null?branding.getButtonColor():EOTConstants.BANK_DEFAULT_BTN);
							request.getSession(false).setAttribute(EOTConstants.BANK_MENU_COLOR, branding.getMenuColor()!=null?branding.getMenuColor():EOTConstants.BANK_DEFAULT_MENU_COLOR);
							request.getSession(false).setAttribute(EOTConstants.BANK_SUB_MENU_COLOR, branding.getSubMenuColor()!=null?branding.getSubMenuColor():EOTConstants.BANK_DEFAULT_SUB_MENU_COLOR);
							try {
								if(null != branding.getLogo()){
								byte[]entityLogo=branding.getLogo().getBytes(1, (int)branding.getLogo().length());
								byte[] encodeBase64 = Base64.encodeBase64(entityLogo);
					            String base64Encoded = new String(encodeBase64, "UTF-8");
								request.getSession(false).setAttribute(EOTConstants.BANK_LOGO, base64Encoded);
								}else{
									request.getSession(false).setAttribute(EOTConstants.BANK_LOGO, EOTConstants.BANK_DEFAULT_LOGO);
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}else{
								request.getSession(false).setAttribute(EOTConstants.BANK_THEME, EOTConstants.BANK_DEFAULT_THEME);
								request.getSession(false).setAttribute(EOTConstants.BANK_BTN_COLOR, EOTConstants.BANK_DEFAULT_BTN);
								request.getSession(false).setAttribute(EOTConstants.BANK_LOGO, EOTConstants.BANK_DEFAULT_LOGO);
								request.getSession(false).setAttribute(EOTConstants.BANK_MENU_COLOR, EOTConstants.BANK_DEFAULT_MENU_COLOR);
								request.getSession(false).setAttribute(EOTConstants.BANK_SUB_MENU_COLOR, EOTConstants.BANK_DEFAULT_SUB_MENU_COLOR);
							}
						
						
						return "redirect:/txnSummary.htm";
					}catch(EOTException e){
						e.printStackTrace();
						model.put("txnSummaryDTO",new TxnSummaryDTO());
						model.put("message",e.getErrorCode());
						return "redirect:/txnSummary.htm";
					}
				}


			} catch (EOTException e) {
				e.printStackTrace();
				model.put("message",e.getErrorCode());
				return "redirect:/txnSummary.htm";
			}catch (Exception e) {
				e.printStackTrace();
				model.put("message", "ERROR_5028");
				return "redirect:/txnSummary.htm";
			}finally{
				model.put("language", locationService.getLanguageList());
				model.put("lang", localeResolver.resolveLocale(request).toString());
			}


		}else{
			model.put("language", locationService.getLanguageList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
			model.put("message", "ERROR_5029");
			return "login";
		}

	} 
	@RequestMapping("/viewTxnSummary.htm")
	public String viewTxnSummary(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response,TxnSummaryDTO txnSummaryDTO){

		try{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Map map=txnSummaryService.getTxnSummaryData(pageNumber,"txnSummary.htm",txnSummaryDTO,request);
			model.put("pageSummData", map.get("txnSumm"));
			model.put("pageListData", map.get("txnList"));
			model.put("txnSummaryDTO",txnSummaryDTO);
			return "redirect:/txnSummary.htm";
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message",e.getErrorCode());
			try{
				model.put("lang", localeResolver.resolveLocale(request).toString());
				model.put("masterData", txnSummaryService.getTxnSummaryMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("txnSummaryDTO",txnSummaryDTO);
				return "redirect:/txnSummary.htm";
			}catch(EOTException ex){
				ex.printStackTrace();
				model.put("txnSummaryDTO",txnSummaryDTO);
				model.put("message",e.getErrorCode());
				return "redirect:/txnSummary.htm";
			}
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message", ErrorConstants.SERVICE_ERROR);
			return "redirect:/txnSummary.htm";
		}
		finally{
			model.put("language", localeResolver.resolveLocale(request).toString().substring(0,2));
			pageLogger(request,response,"TransactionSummary");
		}
	} 

	@RequestMapping("/txnSummary.htm")
	public String txnSummary(Map<String, Object> model,HttpServletRequest request,HttpServletResponse response,TxnSummaryDTO txnSummaryDTO, ApplicationVersionDTO versionDTO,DashboardDTO dashBoardDTO){
		WebUser webUser = null;
		DashboardDTO dashboardDTO=new DashboardDTO();
		try{
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			//String userName = authentication.getName();
			webUser =(WebUser)authentication.getPrincipal();
			request.getSession().setAttribute("roleAccess", webUser.getWebUserRole().getRoleId());
		//	model.put("roleAccess", webUser.getWebUserRole().getRoleId());
			dashBoardDTO.setWebUser(webUser);
			List<Map<String, Object>> list = dashboardServic.loadAccBalaceByUserId(webUser.getUserName(),webUser.getWebUserRole().getRoleId());
			Double floatBalance = 0.0;
			Double commissionBalance =0.0;
			Map<Integer,Double> m = new HashMap<Integer,Double>();
			if(CollectionUtils.isNotEmpty(list)) {
			for(Map<String, Object> map: list) {
				if(null != map.get("aliasType"))
					m.put((Integer)map.get("aliasType"),(Double)map.get("CurrentBalance"));
				else
					m.put(1,(Double)map.get("CurrentBalance"));
			}
			floatBalance = m.get(EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT);
			commissionBalance =m.get(EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);
			}
			if(!webUser.getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BULKPAYMENT_PARTNER)){
			dashboardDTO = dashboardServic.getDailyTransaction(dashBoardDTO);
			dashboardDTO.setPartnerId(dashBoardDTO.getPartnerId());
			dashboardDTO.setPartnerType(dashBoardDTO.getPartnerType());
			DashboardDTO musDashboardDTO = dashboardServic.getMobileUserStatistics(dashBoardDTO);
			model.put("txnSummaryDTO",txnSummaryDTO);
			model.put("musDashboardDTO",musDashboardDTO);
			model.put("floatBalance",floatBalance);
			model.put("commissionBalance",commissionBalance);
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Map map=txnSummaryService.getTxnSummaryData(pageNumber,"txnSummary.htm",txnSummaryDTO,request);
			Object chartData = map.get("txnSumm");
			model.put("pageSummData", new ObjectMapper().writeValueAsString(chartData));
			//model.put("pageSummData", map.get("txnSumm"));
			model.put("pageListData", map.get("txnList"));
			}
			/*return "dashBoard"; added new dash board*/
			if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
				model.put("floatBalance",floatBalance);
				model.put("commissionBalance",commissionBalance);
				return "BulkPayDashBoard";
			}
			return "txnDashBoard";
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message",e.getErrorCode());
			try{
				model.put("lang", localeResolver.resolveLocale(request).toString());
				model.put("masterData", txnSummaryService.getTxnSummaryMasterData(localeResolver.resolveLocale(request).toString()));
				model.put("txnSummaryDTO",txnSummaryDTO);
				if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
					return "BulkPayDashBoard";
				}
				return "txnDashBoard";
			}catch(EOTException ex){
				ex.printStackTrace();
				model.put("txnSummaryDTO",txnSummaryDTO);
				model.put("message",e.getErrorCode());
				if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
					return "BulkPayDashBoard";
				}
				return "txnDashBoard";
			}
		}catch (Exception e) {
			e.printStackTrace();
			model.put("txnSummaryDTO",txnSummaryDTO);
			model.put("message", ErrorConstants.SERVICE_ERROR);
			/*return "dashBoard";*/
			if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
				return "BulkPayDashBoard";
			}
			return "txnDashBoard";
		}
		finally{
			model.put("dashBoardDTO",dashboardDTO);
			model.put("language", localeResolver.resolveLocale(request).toString().substring(0,2));
			pageLogger(request,response,"Login");
			try {
				model.put("partners", dashboardServic.loadBusinessPartnerByType(dashBoardDTO.getPartnerType()));
			} catch (Exception e) {
				e.printStackTrace();
				if(webUser.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BULKPAYMENT_PARTNER){
					return "BulkPayDashBoard";
				}
				return "txnDashBoard";
			}
		}
	} 
	@RequestMapping("/logout.htm")
	public String logout(Map<String, Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
		try{
			model.put("version",session.getAttribute("version"));		
			closeSession(request,response,"Logout");
			SecurityContextHolder.clearContext();			
			model.put("language", locationService.getLanguageList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
			model.put("message", "LOG_OUT_SUCCESS");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"Logout");
		}
		return "login";
	}

	@RequestMapping("/home.htm")
	public String home(Map<String,Object> model,TxnSummaryDTO txnSummaryDTO,HttpServletRequest request,HttpSession session,HttpServletResponse response){
		try{
			session.removeAttribute("bankId");
			session.removeAttribute("countryId");
			session.removeAttribute("transactionType");
			session.removeAttribute("fromDate");
			session.removeAttribute("toDate");
			model.put("lang", localeResolver.resolveLocale(request).toString());
			model.put("language", localeResolver.resolveLocale(request).toString().substring(0,2));
			model.put("masterData", txnSummaryService.getTxnSummaryMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("txnSummaryDTO",new TxnSummaryDTO());
			return "welcome";
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
			return "welcome";
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message", "ERROR_5028");
			return "welcome";
		}finally{
			pageLogger(request,response,"Home");
		}
	}

	@RequestMapping("/changePasswordForm.htm")
	public String changePasswordForm(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{	
			model.put("loginUserDTO",new LoginUserDTO());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"ChangePassword");
		}
		return "changePassword";
	}

	@RequestMapping("/saveChangePasswordForm.htm")
	public String saveChangepasswordForm(@Valid LoginUserDTO loginUserDTO,BindingResult result,@RequestParam String newPassword,@RequestParam String oldPassword,Map<String,Object> model){
		try {
			if(result.hasErrors()){
				model.put("loginUserDTO",loginUserDTO);
			}else{

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String userName = auth.getName();
				webUserService.changePassword(userName,oldPassword,newPassword);
				model.put("message","PASSWORD_CHANGE_SUCCESSFUL");
				model.put("loginUserDTO",new LoginUserDTO());	
			}
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "login";
	}

	@RequestMapping("/changePassword.htm")
	public String changePassword(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("loginUserDTO",new LoginUserDTO());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"ChangePassword");
		}
		return "changePasswordForm";
	}

	@RequestMapping("/saveChangePassword.htm")
	public String saveChangepassword(@Valid LoginUserDTO loginUserDTO,BindingResult result,@RequestParam String newPassword,@RequestParam String oldPassword,Map<String,Object> model){
		try {
			if(result.hasErrors()){
				model.put("loginUserDTO",loginUserDTO);
			}else{
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String userName = auth.getName();
				webUserService.changePassword(userName,oldPassword,newPassword);
				model.put("message","PASSWORD_CHANGE_SUCCESSFUL");
				model.put("loginUserDTO",new LoginUserDTO());	
			}
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "login";
	}
	@RequestMapping("/verifyOTPForm.htm")
	public String verifyOTP(@RequestParam String otp,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) throws Exception{

		try{
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			webUserService.verifyOTP(otp,userName);
			model.put("lang", localeResolver.resolveLocale(request).toString());
			model.put("masterData", txnSummaryService.getTxnSummaryMasterData(localeResolver.resolveLocale(request).toString()));
			model.put("txnSummaryDTO",new TxnSummaryDTO());
			return "redirect:/txnSummary.htm";
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
			return "otp";
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "otp";
		}finally{
			pageLogger(request,response,"VerifyOTP");
		}
	}

	@RequestMapping("/loginFailure.htm")
	protected String authenticateFailure(Map<String,Object> model, HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		
		try{
			AuthenticationException exception = (AuthenticationException) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

			if(exception instanceof LockedException){
				model.put("message", "ERROR_8025");
			}else if(exception instanceof BadCredentialsException){
				model.put("message", "ERROR_8027");
				webUserService.incrementFailedAttemptCounter(exception.getAuthentication().getName());
			}else if(exception instanceof CredentialsExpiredException){
				model.put("message", "ERROR_8026");
			}

		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			String version="";
			version = applicationVersionService.getApplicationVersionService();
			model.put("version",version);
			model.put("language", locationService.getLanguageList());
			model.put("lang", localeResolver.resolveLocale(request).toString());
			pageLogger(request,response,"LoginFailure");
		}
		return "login";
	}

	@RequestMapping("/changeLocale.htm")
	public ModelAndView changeLocale(@RequestParam String lang,HttpServletRequest request,HttpServletResponse response,Map<String,Object> model) {	

		try{
			model.put("language", locationService.getLanguageList());
			model.put("lang", lang);
			model.put("message",request.getParameter("message"));
			model.put("sessionexpired",request.getParameter("session"));
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			String version="";
			version = applicationVersionService.getApplicationVersionService();
			model.put("version",version);
			pageLogger(request,response,"ChangeLocale");
		}
		return new ModelAndView("login");
	}

	@RequestMapping("/showForgotPasswordForm.htm")
	public String showForgetPasswordForm(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try{
			System.out.println("/showForgotPasswordForm.htm");
			ForgotPasswordDTO passwordDTO=new ForgotPasswordDTO();
			model.put("passwordDTO",passwordDTO);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			String version="";
			version = applicationVersionService.getApplicationVersionService();
			model.put("version",version);
			pageLogger(request,response,"ForgotPassword");
		}
		return "forgotPassword";
	}

	@RequestMapping("/forgotPassword.htm")
	public String forgetPassword(@Valid ForgotPasswordDTO passwordDTO,Map<String,Object> model)throws EOTException{
		String version="";
		version = applicationVersionService.getApplicationVersionService();
		model.put("version",version);
		try{
			webUserService.forgetPassword(passwordDTO);
			model.put("message","PASSWORD_RESEND_SUCCESS");
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("passwordDTO",passwordDTO);
			model.put("message",e.getErrorCode());
			return "forgotPassword";
		}catch(Exception e){
			e.printStackTrace();
			model.put("passwordDTO",passwordDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "forgotPassword";
		}
		return "redirect:/changeLocale.htm?lang=en_US";

	}

	@RequestMapping("/pageNotFound.htm")
	public String pageNotFound(HttpServletRequest request, Map<String, Object> model){
		return "pageNotFound";
	}

	@RequestMapping("/internalServerError.htm")
	public String internalServerError(HttpServletRequest request, Map<String, Object> model){
		
		return "errorsUncaughtable";
	}
	@RequestMapping("/sessionTimeOut.htm")
	public String sessionTimeOut(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model){
		String version="";
		version = applicationVersionService.getApplicationVersionService();
		model.put("version",version);
		model.put("language", locationService.getLanguageList());
		model.put("lang", localeResolver.resolveLocale(request).toString());
		model.put("message","SESSION_EXPIRED");
		return "login";
	}

	@RequestMapping("/forwardLogin.htm")
	public String getLoginPage(HttpServletRequest request,HttpServletResponse response,Map<String,Object> model,@Valid ForgotPasswordDTO passwordDTO){
		String version="";
		version = applicationVersionService.getApplicationVersionService();
		model.put("version",version);
		String view="";
		try{			
			String flag="";
			flag= request.getParameter("forgetPage");
			if(flag!=null && flag.equalsIgnoreCase("showForgotPasswordForm")) {
				//System.out.println("/showForgotPasswordForm.htm");
				passwordDTO=new ForgotPasswordDTO();
				model.put("passwordDTO",passwordDTO);
				pageLogger(request,response,"ForgotPassword");
				view =  "forgotPassword";
			}else if(flag!=null && flag.equalsIgnoreCase("forgotPassword")) {
				try{
					webUserService.forgetPassword(passwordDTO);
					model.put("message","PASSWORD_RESEND_SUCCESS");
				}catch (EOTException e) {
					e.printStackTrace();
					model.put("passwordDTO",passwordDTO);
					model.put("message",e.getErrorCode());
					view =  "forgotPassword";
				}catch(Exception e){
					e.printStackTrace();
					model.put("passwordDTO",passwordDTO);
					model.put("message",ErrorConstants.SERVICE_ERROR);
					view =  "forgotPassword";
				}
				return "redirect:/changeLocale.htm?lang=en_US";
			}else {
				model.put("language", locationService.getLanguageList());
				model.put("lang", localeResolver.resolveLocale(request).toString());
				model.put("message",request.getParameter("message"));
				view =  "login";
				//model.put("sessionexpired",request.getParameter("session"));
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	//	return new ModelAndView().setViewName(view);
		return view;
	}

}


