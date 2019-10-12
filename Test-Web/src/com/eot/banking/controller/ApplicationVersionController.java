package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.dto.ApplicationVersionDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.ApplicationVersionService;
import com.eot.banking.utils.Page;

@Controller
public class ApplicationVersionController extends PageViewController {

	@Autowired
	private ApplicationVersionService applicationVersionService;

	@RequestMapping("/showVersionFrom.htm")
	public String processVersion(Map<String,Object> model,ApplicationVersionDTO versionDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("message", request.getParameter("message"));
			model.put("versionDTO", new ApplicationVersionDTO());
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionList(pageNumber);
			page.requestPage = "showVersionFrom.htm";
			model.put("page",page);	
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"ApplicationVersion");
		}

		return "applicationVersion";
	}

	@RequestMapping("/saveVersion.htm")
	public String saveVersion(@Valid ApplicationVersionDTO versionDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(result.hasErrors()){		
				model.put("versionDTO", versionDTO);
			}else{
				applicationVersionService.addApplicationVersion(versionDTO);
				model.put("message", "ADD_VERSION_SUCCESS");			
				model.put("versionDTO",new ApplicationVersionDTO());
				return "applicationVersion";
				//return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				/*}else{
				applicationVersionService.updateApplicationVersion(versionDTO);	
				model.put("message", "EDIT_VERSION_SUCCESS");			
				return "redirect:/showVersionFrom.htm";*/
			}			
		} catch (EOTException e) {
			model.put("message", e.getErrorCode());
			model.put("versionDTO", versionDTO);
			return "applicationVersion";
			//return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("versionDTO", versionDTO);
			return "applicationVersion";
			//return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} finally{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionList(pageNumber);
			page.requestPage = "showVersionFrom.htm";
			model.put("page",page);	
			pageLogger(request,response,"ApplicationVersion");
		}

		return "applicationVersion";
	}

	@RequestMapping("/updateVersion.htm")
	public String updateVersion(@Valid ApplicationVersionDTO versionDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) {
		try {
			if(result.hasErrors()){
				model.put("versionDTO", versionDTO);
			}else{
				applicationVersionService.updateApplicationVersion(versionDTO);	
				model.put("message", "EDIT_VERSION_SUCCESS");			
				model.put("versionDTO",new ApplicationVersionDTO());
				return "applicationVersion";
				//return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				/*}else{
				applicationVersionService.updateApplicationVersion(versionDTO);	
				model.put("message", "EDIT_VERSION_SUCCESS");			
				return "redirect:/showVersionFrom.htm";*/
			}			
		} catch (EOTException e) {
			model.put("message", e.getErrorCode());
			model.put("versionDTO", versionDTO);
			model.put("versionDTO",new ApplicationVersionDTO());
			return "applicationVersion";
			//return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("versionDTO", versionDTO);
			model.put("versionDTO",new ApplicationVersionDTO());
			return "applicationVersion";
			//return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}  finally{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionList(pageNumber);
			page.requestPage = "showVersionFrom.htm";
			model.put("page",page);	
			pageLogger(request,response,"ApplicationVersion");
		}

		//return "applicationVersion?csrfToken=" + request.getSession().getAttribute("csrfToken");
		return "applicationVersion";
	}	

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/getVersion.htm")
	public String getVersion(ApplicationVersionDTO versionDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try {
			model.put("versionDTO", applicationVersionService.getApplicationVersion(versionDTO.getVersionNumber()));
			//@End
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionList(pageNumber);
			page.requestPage = "showVersionFrom.htm";
			model.put("page",page);	
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("versionDTO", new ApplicationVersionDTO());
			model.put("message", e.getErrorCode());
			return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("versionDTO", new ApplicationVersionDTO());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/showVersionFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}finally{
			pageLogger(request,response,"ApplicationVersion");
		}

		return "applicationVersion";
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/showVersionDetailsFrom.htm")
	public String processVersionDetails(Map<String,Object> model,ApplicationVersionDTO versionDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("message", request.getParameter("message"));
			/*versionDTO = new ApplicationVersionDTO();
			versionDTO.setVersionNumber(versionNumber);*/
			model.put("versionDTO",versionDTO);
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionDetailsList(versionDTO.getVersionNumber(), pageNumber);
//End
			page.requestPage = "showVersionDetailsFrom.htm";
			model.put("page",page);	
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ApplicationVersionDetails");
		}

		return "applicationVersionDetails";
	}

	@RequestMapping("/saveVersionDetails.htm")
	public String saveVersionDetails(@Valid ApplicationVersionDTO versionDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response) {
		String versonNumber = versionDTO.getVersionNumber();

		ApplicationVersionDTO applicationVersionDTO = new ApplicationVersionDTO();
		try {
			if(result.hasErrors()){
				model.put("versionDTO", versionDTO);
			}else if(versionDTO.getVersionId() == null){
				applicationVersionService.addApplicationVersionDetails(versionDTO);
				model.put("message", "ADD_VERSION_SUCCESS");	
				
				return "applicationVersionDetails";
				//return "redirect:/showVersionDetailsFrom.htm?versionNumber="+versionDTO.getVersionNumber()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}else{
				applicationVersionService.updateApplicationVersionDetails(versionDTO);	
				model.put("message", "EDIT_VERSION_SUCCESS");	
				model.put("versionDTO", applicationVersionDTO);
				return "applicationVersionDetails";
				//return "redirect:/showVersionDetailsFrom.htm?versionNumber="+versionDTO.getVersionNumber()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}			
		} catch (EOTException e) {
			model.put("message", e.getErrorCode());
			model.put("versionDTO", versionDTO);
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("versionDTO", versionDTO);
		} finally{
			applicationVersionDTO.setVersionNumber(versonNumber);
			model.put("versionDTO", applicationVersionDTO);
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionDetailsList(versionDTO.getVersionNumber(), pageNumber);
//End
			page.requestPage = "showVersionDetailsFrom.htm";
			model.put("page",page);	
			pageLogger(request,response,"ApplicationVersionDetails");
		}

		return "applicationVersionDetails";
	}

	@RequestMapping("/getVersionDetails.htm")
	public String getVersionDetails(@RequestParam Integer versionId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try {
			model.put("versionDTO", applicationVersionService.getApplicationVersionDetails(versionId));
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			Page page = applicationVersionService.getApplicationVersionDetailsList(request.getParameter("versionNumber"), pageNumber);
			page.requestPage = "showVersionDetailsFrom.htm";
			model.put("page",page);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("versionDTO", new ApplicationVersionDTO());
			model.put("message", e.getErrorCode());
			return "redirect:/showVersionDetailsFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("versionDTO", new ApplicationVersionDTO());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/showVersionDetailsFrom.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}finally{
			pageLogger(request,response,"ApplicationVersionDetails");
		}

		return "applicationVersionDetails";
	}

}