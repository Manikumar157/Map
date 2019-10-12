package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.SMSLogService;
import com.eot.banking.utils.Page;

@Controller
public class AllSMSLogController extends PageViewController {

	@Autowired
	private SMSLogService smsLogService;

	@RequestMapping("/showAllSmsLogForm.htm")
	public String showSMSLogForm(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			Page page=smsLogService.getSMSLogList(1);
			page.requestPage = "viewAllSMSLog.htm";			
			model.put("page",page);		
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"SmsLog");
		}
		return "viewAllSMSLog";
	}

	@RequestMapping("/searchAllSMSLogByMobileNo.htm")
	public String searchSMSLogByMobileNo(@RequestParam String mobno,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{			
			Page page=smsLogService.getSMSLogByMobileNo(mobno,1);
			page.requestPage = "viewAllSMSLog.htm";			
			model.put("page",page);	
			model.put("mobno",mobno);
		}catch (EOTException e) {
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"SearchAllSmsLog");
		}
		return "viewAllSMSLog";

	}

	@RequestMapping("/viewAllSMSLog.htm")
	public String list(@RequestParam Integer pageNumber,@RequestParam String mobno,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{		
			model.put("mobno",mobno);
			Page page=mobno==""?smsLogService.getSMSLogList(pageNumber):smsLogService.getSMSLogByMobileNo(mobno,pageNumber);			
			page.requestPage = "viewAllSMSLog.htm";			
			model.put("page",page);		
		}catch (EOTException e) {
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ViewAllSMSLog");
		}
		return "viewAllSMSLog";

	}

	/*@RequestMapping("/changeSmsLogStatus.htm")
	public String changeSMSLogStatus(@RequestParam Integer msgId,@RequestParam String mobno,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{				
			model.put("mobno",mobno);
			smsLogService.chageSMSLogStatus(msgId);	
			Page page=smsLogService.getSMSLogByMobileNo(mobno,1);
			page.requestPage = "viewAllSMSLog.htm";			
			model.put("page",page);		
			model.put("message","RESEND_SUCCESS");
		}catch (Exception e) {
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ChangeSmsLogStatus");
		}	

		return "viewAllSMSLog";
	}*/


}
