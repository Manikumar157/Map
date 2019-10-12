package com.eot.banking.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eot.banking.dto.AccessLogDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.AccessLogService;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.SessionLog;



@Controller
public class AccessLogController {
	
	@Autowired
	private AccessLogService accessLogService;
	
	@RequestMapping("/showAccessLog.htm")
	public String showAccessLogForm(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response,AccessLogDTO accessLogDTO){	
				
		String userId="";
		String fromDate="";
		String toDate="";
	try{		
		userId=request.getParameter("userId");
		fromDate=request.getParameter("fromDate");
		toDate=request.getParameter("toDate");		
		
		session.setAttribute("userId", userId);
		session.setAttribute("fromDate", fromDate );
		session.setAttribute("toDate", toDate);
		
		/*Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;	
		Page page=accessLogService.getSessionList(userId,fromDate,toDate,pageNumber);
		page.requestPage = "searchAccessPage.htm";
		model.put("page",page);	*/	
			
	}/*catch(EOTException e) {
		e.printStackTrace();
		model.put("message", e.getErrorCode());
	}*/catch (Exception e) {
		e.printStackTrace();
		model.put("message",ErrorConstants.SERVICE_ERROR);
	}finally{
		model.put("webUserRoleList",accessLogService.getRoleList());	
		model.put("userId",userId);
		model.put("fromDate",fromDate);
		model.put("toDate",toDate);		
	}	
	return "accessLog";	
	}
	
	@RequestMapping("/searchAccessPage.htm")
	public String searchAccessPage(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response,AccessLogDTO accessLogDTO){

		try{
			Integer pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			
			String userId = request.getAttribute("userId") == null ? "" : request.getAttribute("userId").toString() ;
			String fromDate =  request.getAttribute("fromDate") == null ? "" : request.getAttribute("fromDate").toString() ;
			String toDate =  request.getAttribute("toDate") == null ? "" : request.getAttribute("toDate").toString() ;	
			
			Page page=accessLogService.getSessionList(userId,fromDate,toDate,pageNumber);
			page.requestPage = "searchAccessPage.htm";
			model.put("page",page);
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{			
			model.put("webUserRoleList",accessLogService.getRoleList());			
		}

		return "accessLog";
	}	
	
	@RequestMapping("/viewPageVisited.htm")
	public String viewPageVisitedForm(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
	try{	
		String sessionId=request.getParameter("sessionId");
		session.setAttribute("sessionId", sessionId);
		int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		Page page = accessLogService.getVisitedPage(sessionId,pageNumber);
		page.requestPage = "viewVisitedPage.htm";
		model.put("page",page);	
	}catch(EOTException e) {
		e.printStackTrace();
		model.put("message", e.getErrorCode());
	} catch (Exception e) {
		e.printStackTrace();
		model.put("message",ErrorConstants.SERVICE_ERROR);
	}
		
		return "viewPageVisited";		
	}
	
	@RequestMapping("/viewVisitedPage.htm")
	public String viewVisitedPage(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
	try{
		String sessionId =  session.getAttribute("sessionId") == null ? "" : session.getAttribute("sessionId").toString() ;
		
		int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
		Page page = accessLogService.getVisitedPage(sessionId,pageNumber);
		page.requestPage = "viewVisitedPage.htm";
		model.put("page",page);		
	}catch(EOTException e) {
		e.printStackTrace();
		model.put("message", e.getErrorCode());
	} catch (Exception e) {
		e.printStackTrace();
		model.put("message",ErrorConstants.SERVICE_ERROR);
	}
		return "viewPageVisited";		
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/datashowAccessLog.htm")
	public @ResponseBody String datashowAccessLogForm(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response,AccessLogDTO accessLogDTO){	
		Integer pageNumber=1;		
		String userId="";
		String fromDate="";
		String toDate="";
		JSONObject json = null;
	try{		
		userId=request.getParameter("userId");
		fromDate=request.getParameter("fromDate");
		toDate=request.getParameter("toDate");		
		
		session.setAttribute("userId", userId);
		session.setAttribute("fromDate", fromDate );
		session.setAttribute("toDate", toDate);
		
		pageNumber = request.getParameter("pageNumber") !=null ? new Integer(request.getParameter("pageNumber")) : 1 ;	
		response.setContentType("application/json");
		json = new JSONObject();
		Page page=accessLogService.getSessionList(userId,fromDate,toDate,pageNumber);
		json.put("recordsTotal", page.getTotalCount());
		json.put("recordsFiltered", page.getTotalCount());
		JSONArray array = new JSONArray();
		if(CollectionUtils.isNotEmpty(page.getResults())){
			for (SessionLog sessionLog : (List<SessionLog>)page.getResults()) {
				JSONObject item = new JSONObject();
				item.put("sessionId", sessionLog.getSessionId());
				item.put("UserId", sessionLog.getUserName());
			//	item.put("LoginTime", DateUtil.formatDateAndTime(sessionLog.getLoginTime()));
				String loginTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sessionLog.getLoginTime());
				String logoutTime = sessionLog.getLogoutTime()!= null ? new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sessionLog.getLogoutTime()) : "Session Timed Out";
				item.put("LoginTime",loginTime);
				item.put("LogoutTime",logoutTime);
			//	item.put("LogoutTime", sessionLog.getLogoutTime()!= null ?DateUtil.formatDateAndTime(sessionLog.getLogoutTime()):"Session Timed Out");
				item.put("Action", "View");
				array.add(item);
			}
		}
		json.put("data", array);
			
	}catch(EOTException e) {
		e.printStackTrace();
		model.put("message", e.getErrorCode());
		Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
		json.put("recordsTotal", page.getTotalCount());
		json.put("recordsFiltered", page.getTotalCount());
		JSONArray array = new JSONArray();
		json.put("data", array);
		System.out.println(json);
		return json.toJSONString();
	}catch (Exception e) {
		e.printStackTrace();
		model.put("message",ErrorConstants.SERVICE_ERROR);
	}finally{
		model.put("webUserRoleList",accessLogService.getRoleList());	
		model.put("userId",userId);
		model.put("fromDate",fromDate);
		model.put("toDate",toDate);		
	}
	System.out.println(json);
	return json.toJSONString();	
	}
	
	
}
