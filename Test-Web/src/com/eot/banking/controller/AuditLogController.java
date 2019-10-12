package com.eot.banking.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.eot.banking.dto.AuditLogDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.AuditLogService;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.JSONAdaptor;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.AuditLog;

@Controller
public class AuditLogController extends PageViewController {

	@Autowired
	AuditLogService auditLogService;
	
	@RequestMapping("/searchAuditLogForm.htm")
	public String showSearchAuditLogForm(AuditLogDTO auditLogDTO, HttpServletRequest request,Map<String, Object> model,HttpServletResponse response,HttpSession session){
		int pageNumber = 1;
	try{
		if( request.getParameter("pageNumber") != null ){
			pageNumber = new Integer(request.getParameter("pageNumber"));
			auditLogDTO = (AuditLogDTO) session.getAttribute("auditLogDTO");
		} else {
			session.setAttribute("auditLogDTO", auditLogDTO);
		}
		/*Page page=auditLogService.getAuditLogs(auditLogDTO,pageNumber);
		page.requestPage = "searchAuditLog.htm";
		model.put("page",page);	*/
	}/*catch(EOTException e) {
		e.printStackTrace();
		model.put("message", e.getErrorCode());
	}*/ catch (Exception e) {
		e.printStackTrace();
		model.put("message",ErrorConstants.SERVICE_ERROR);
	} finally{
		pageLogger(request,response,"AuditLogs");
		session.setAttribute("auditLogDTO", auditLogDTO);
		model.put("auditLogDTO", auditLogDTO);
		model.put("userList",auditLogService.getWebUserList());
	}
		return "searchAuditLog";

	}
	
	@RequestMapping("/searchAuditLog.htm")
	public String SearchAuditLog(AuditLogDTO auditLogDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response,HttpSession session){
		int pageNumber = 1;
		try{	
		
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				auditLogDTO = (AuditLogDTO) session.getAttribute("auditLogDTO");
			} else {
				session.setAttribute("auditLogDTO", auditLogDTO);
			}
		
		Page page=auditLogService.getAuditLogs(auditLogDTO,pageNumber);
		page.requestPage = "searchAuditLog.htm";
		model.put("page",page);	
		model.put("auditLogDTO", auditLogDTO);
		
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"AuditLogs");
			model.put("userList",auditLogService.getWebUserList());
		}
		
		
		return "searchAuditLog";

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/datasearchAuditLogForm.htm")
	public @ResponseBody String dataSearchAuditLog(AuditLogDTO auditLogDTO,HttpServletRequest request,Map<String, Object> model,HttpServletResponse response,HttpSession session){
		int pageNumber = 1;
		JSONObject jsonObj=null;
		try{	
		request.getParameter("length");
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				auditLogDTO = (AuditLogDTO) session.getAttribute("auditLogDTO");
				String sortColumn = request.getParameter("sortColumn");
				String sortBy = request.getParameter("sortBy");
				auditLogDTO.setSortColumn(sortColumn == null || sortColumn.equals("") ? "id" : sortColumn);
				auditLogDTO.setSortBy(sortBy);
			} else {
				session.setAttribute("auditLogDTO", auditLogDTO);
			}
		
		jsonObj = new JSONObject();
		Page page=auditLogService.getAuditLogs(auditLogDTO,pageNumber);
		//List<AuditLog> auditLog = (List<AuditLog>)page.getResults();
		jsonObj.put("recordsTotal", page.getTotalCount());
		jsonObj.put("recordsFiltered", page.getTotalCount());
		JSONArray array = new JSONArray();
		if(CollectionUtils.isNotEmpty(page.getResults())){
			for (AuditLog auditLog : (List<AuditLog>)page.getResults()) {
				JSONObject item = new JSONObject();
				item.put("updatedBy", auditLog.getUpdatedBy());
				String updatedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(auditLog.getUpdatedDate());
				item.put("updatedDate", updatedDate);
		//		item.put("updatedDate", DateUtil.formatDateAndTime(auditLog.getUpdatedDate()));
				item.put("entityId", auditLog.getEntityId());
				item.put("entityName", auditLog.getEntityName());
				// changes			
				item.put("oldValue", formatString(auditLog.getOldValue()));
				item.put("newValue",  formatString(auditLog.getNewValue()));
				// over
			//	item.put("oldValue", auditLog.getOldValue().replace("[", "-"));
			//	item.put("newvalue", auditLog.getNewValue().replace("[", "-"));
				item.put("entityAttribute", auditLog.getEntityAttribute());
				item.put("message", auditLog.getMessage());
				array.add(item);
			}
		}
		jsonObj.put("data", array);
		}catch(EOTException e) {	
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			Page page = PaginationHelper.getPage(new ArrayList(), 0, 10, pageNumber);
			jsonObj.put("recordsTotal", page.getTotalCount());
			jsonObj.put("recordsFiltered", page.getTotalCount());
			JSONArray array = new JSONArray();
			jsonObj.put("data", array);
			System.out.println(jsonObj);
			return jsonObj.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"AuditLogs");
			model.put("userList",auditLogService.getWebUserList());
		}
		System.out.println(jsonObj);
		return jsonObj.toJSONString();

	}
	String formatString(String str) {
		
		String str1 ="";
		for(int i=1;i<=str.length();i++) {
			if(i%15==0) {
				str1 = str1 +"-"+ str.charAt(i-1);
			}else {
				str1 = str1 + str.charAt(i-1);
			}
		}
		return str1;
	}
	
}

