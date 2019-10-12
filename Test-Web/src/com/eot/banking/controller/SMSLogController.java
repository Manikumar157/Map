/* Copyright � EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: SMSLogController.java
 *
 * Date Author Changes
 * 17 Jun, 2016 Swadhin Created
 */
package com.eot.banking.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.SMSAlertDTO;
import com.eot.banking.dto.SMSCampaignDTO;
import com.eot.banking.dto.SmsDetailsDto;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.SCManagementService;
import com.eot.banking.service.SMSLogService;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.SmsLog;

/**
 * The Class SMSLogController.
 */
@Controller
public class SMSLogController extends PageViewController {

	/** The sms log service. */
	@Autowired
	private SMSLogService smsLogService;

	/** The sc management service. */
	@Autowired
	private SCManagementService scManagementService;

	/** The locale resolver. */
	@Autowired
	private LocaleResolver localeResolver;

	/**
	 * Show sms log form.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/showSmsLogForm.htm")
	public String showSMSLogForm(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			Page page=smsLogService.getSMSLogList(1);
			page.requestPage = "viewSMSLog.htm";			
			model.put("page",page);		
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"SmsLog");
		}
		return "viewSMSLog";
	}

	/**
	 * Search sms log by mobile no.
	 *
	 * @param mobno the mobno
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/searchSMSLogByMobileNo.htm")
	public String searchSMSLogByMobileNo(@RequestParam String mobno,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{			
			Page page=smsLogService.getSMSLogByMobileNo(mobno,1);
			page.requestPage = "viewSMSLog.htm";			
			model.put("page",page);	
			model.put("mobno",mobno);
		}catch (EOTException e) {
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"SearchSmsLog");
		}
		return "viewSMSLog";

	}

	/**
	 * List.
	 *
	 * @param pageNumber the page number
	 * @param mobno the mobno
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/viewSMSLog.htm")
	public String list(@RequestParam Integer pageNumber,@RequestParam String mobno,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{		
			model.put("mobno",mobno);
			Page page=mobno==""?smsLogService.getSMSLogList(pageNumber):smsLogService.getSMSLogByMobileNo(mobno,pageNumber);			
			page.requestPage = "viewSMSLog.htm";			
			model.put("page",page);		
		}catch (EOTException e) {
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ViewSMSLog");
		}
		return "viewSMSLog";

	}

	/**
	 * Change sms log status.
	 *
	 * @param msgId the msg id
	 * @param mobno the mobno
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/changeSmsLogStatus.htm")
	public String changeSMSLogStatus(@RequestParam Integer msgId,@RequestParam String mobno,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{				
			model.put("mobno",mobno);
			smsLogService.chageSMSLogStatus(msgId);	
			Page page=smsLogService.getSMSLogByMobileNo(mobno,1);
			//page.requestPage = "viewSMSLog.htm";			
			model.put("page",page);		
			model.put("message","RESEND_SUCCESS");
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ChangeSmsLogStatus");
		}	

		return "viewSMSLog";
	}

	@RequestMapping("/searchSmsAlertRules.htm")
	public String searchSmsAlertRules(SMSAlertDTO smsAlertDTO,Map<String,Object> model,HttpSession session,HttpServletRequest request,HttpServletResponse response){

		try {				
			smsAlertDTO.setRuleLevel(1);
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			smsLogService.searchSmsAlertRules(userName, smsAlertDTO, model);

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
				model.put("masterData", smsLogService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				pageLogger(request,response,"ServiceChargeRules");
			}catch(EOTException e){

			}
		}

		return "showSmsAlertPackages";
	}

	/**
	 * Show sms alert form.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/showSMSAlertForm.htm")
	public String showSMSAlertForm(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("message", request.getParameter("message"));
			model.put("smsAletDTO", new SMSAlertDTO());
			model.put("masterData", smsLogService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
		} catch (EOTException e) {
			e.printStackTrace();	
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "addSMSAlert";
	}

	/**
	 * Adds the sms alert.
	 *
	 * @param smsAlertDTO the sms alert dto
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/addSMSAlerts.htm")
	public String addSMSAlert(SMSAlertDTO smsAlertDTO , Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			smsLogService.saveOrUpdateSmsAlerts(smsAlertDTO);
			String message = smsAlertDTO.getSmsAlertRuleId() == null?"SMS_ALERT_SUCCESS":"SMS_ALERT_UPDATE_SUCCESS" ;
			model.put("message", message);
			model.put("smsAletDTO", new SMSAlertDTO());
		} catch (EOTException e) {
			e.printStackTrace();  
			model.put("smsAletDTO", smsAlertDTO);
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("smsAletDTO", smsAlertDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try {
				model.put("masterData", smsLogService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
				model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			} catch (EOTException e) {
				e.printStackTrace();
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "addSMSAlert";
	}
	
	@RequestMapping("/getSmsAlertRule.htm")
	public String getSmsAlertRule(@RequestParam Long smsAlertRuleId , Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("smsAletDTO", smsLogService.getSmsAlertRule(smsAlertRuleId));
			model.put("masterData", smsLogService.getMasterData(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
		} catch (EOTException e) {
			e.printStackTrace();   
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "addSMSAlert";
	}
	
	@RequestMapping("/viewSmsDetails.htm")
	public String getSmsDetails(SmsDetailsDto smsDetailsDto,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			String mobileNo= request.getParameter("mobileNumber");
			String fromDate= request.getParameter("fromDate");
			String toDate= request.getParameter("toDate");
			
			if(fromDate==null || toDate==null)
			{
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				String date=sf.format(new Date());
				fromDate=date;
				toDate=date;
			}else
			{
				SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
				Date date1=sf.parse(fromDate); 
				Date date2=sf.parse(toDate); 
				 
				sf = new SimpleDateFormat("yyyy-MM-dd");
				sf.format(date1);
				fromDate=sf.format(date1);
				toDate=sf.format(date2);
			}
			
//			model.put("smsAletDTO", smsLogService.getSmsAlertRule(smsAlertRuleId));
			model.put("page", smsLogService.getSmsDetails(mobileNo,fromDate,toDate,pageNumber));
//			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
			
		} catch (EOTException e) {
			e.printStackTrace();   
			model.put("message", e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally {
			model.put("smsDetailsDto", new SmsDetailsDto());
		}
		return "viewSmsDetails";
	}
	
	@RequestMapping("/addSmsCampaign.htm")
	public String addSMSCampaign(SMSCampaignDTO smsCampaignDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response){

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		
		try {	
			Page page=smsLogService.getSmsCampaignData(smsCampaignDTO, pageNumber);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
			model.put("smsCampaignDTO", new SMSCampaignDTO());
			}catch(EOTException e){
				model.put("smsCampaignDTO", smsCampaignDTO);
				model.put("message1", ErrorConstants.NO_RECORDS_FOUND);
				e.printStackTrace();
			}catch(Exception e){
				model.put("smsCampaignDTO", smsCampaignDTO);
				e.printStackTrace();
				model.put("message1", ErrorConstants.SERVICE_ERROR);
			}
		return "addSmsCampaign";
	}
	
	@RequestMapping(value = "/sendSmsCampaign.htm")
	public String sendSmsCampaign(@Valid SMSCampaignDTO smsCampaignDTO,ModelMap model,HttpServletRequest request,HttpServletResponse response,HttpSession session) throws EOTException{

		Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		try {
			smsLogService.sendSmsCampaign(smsCampaignDTO); 
			Page page=smsLogService.getSmsCampaignData(smsCampaignDTO, pageNumber);
			if (page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
				throw new EOTException(ErrorConstants.NO_RECORDS_FOUND);
			model.put("page",page );
			model.put("smsCampaignDTO", new SMSCampaignDTO());
			model.put("message1", "SMS_CAMPAIGN_ADDED_SUCCESSFULLY");
			}catch(EOTException e){
				model.put("smsCampaignDTO", smsCampaignDTO);
				model.put("message1", ErrorConstants.NO_RECORDS_FOUND);
				e.printStackTrace();
				return "addSmsCampaign";
			}catch(Exception e){
				model.put("smsCampaignDTO", smsCampaignDTO);
				e.printStackTrace();
				model.put("message1", ErrorConstants.SERVICE_ERROR);
				return "addSmsCampaign";
			}
		return "addSmsCampaign";
	}

}
