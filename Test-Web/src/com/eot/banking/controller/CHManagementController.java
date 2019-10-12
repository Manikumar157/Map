package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.ClearingHouseService;
import com.eot.banking.utils.Page;

@Controller
public class CHManagementController extends PageViewController {

	@Autowired
	private ClearingHouseService clearingHouseService;

	@RequestMapping("/showClearanceHouseForm.htm")
	public String processClearanceHouseManagement(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		ClearingHouseDTO clearingHouseDTO=null;
		try {
			clearingHouseDTO=new ClearingHouseDTO();
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			
			Page page=clearingHouseService.searchClearingHouse(clearingHouseDTO, pageNumber);
			page.requestPage = "searchClearingHouse.htm" ;
			model.put("page", page);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			session.setAttribute("clearingHouseDTO",clearingHouseDTO);
			model.put("currencyList", clearingHouseService.getCurrencyList());
			model.put("clearingHouseDTO", clearingHouseDTO);
			model.put("message", request.getParameter("message"));
		}
		return "clearingHouseManagement";

	}

	@RequestMapping("/saveClearanceHouse.htm")
	public String saveClearingHouse(@Valid ClearingHouseDTO clearanceDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request) {
		ClearingHouseDTO clearingHouseDTO=null;
		try {
			if(result.hasErrors()){		
				model.put("clearingHouseDTO", clearanceDTO);
			}else if(clearanceDTO.getClearingPoolId() == null ){
				clearingHouseService.addClearingHouse(clearanceDTO);
				model.put("clearingHouseDTO", new ClearingHouseDTO());
				model.put("message", "ADD_CHOUSE_SUCCESS");			
				//return "redirect:/showClearanceHouseForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				return "clearingHouseManagement";
			}else{
				clearingHouseService.updateClearingHouse(clearanceDTO);	
				model.put("clearingHouseDTO", new ClearingHouseDTO());
				model.put("message", "EDIT_CHOUSE_SUCCESS");			
				return "clearingHouseManagement";
				//return "redirect:/showClearanceHouseForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}			
		} catch (EOTException e) {
			model.put("message", e.getErrorCode());
			model.put("clearingHouseDTO", clearanceDTO);
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			model.put("clearingHouseDTO", clearanceDTO);
		} finally{
			try {
				model.put("masterData", clearingHouseService.getMasterData(1));
				clearingHouseDTO=new ClearingHouseDTO();
				int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
				Page page=clearingHouseService.searchClearingHouse(clearingHouseDTO, pageNumber);
				page.requestPage = "searchClearingHouse.htm" ;
				model.put("page", page);
			} catch (Exception e) {
				e.printStackTrace();
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "addClearingHouse";
	}	
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	@RequestMapping("/editClearanceHouse.htm")
	public String editClearingHouse(ClearingHouseDTO clearingHouseDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try {
			model.put("clearingHouseDTO", clearingHouseService.getClearingHouseDetails(clearingHouseDTO.getClearingPoolId()));
			//@End
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "redirect:/showClearanceHouseForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/showClearanceHouseForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}finally{
			try {
				model.put("masterData", clearingHouseService.getMasterData(1));
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditClearingHouse");
		}
		return "addClearingHouse";
	}	
	
	@RequestMapping("/settlemente.htm")
	public String viewsettlement(@RequestParam Integer clearingPoolId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try {
			model.put("clearingHouseDTO", clearingHouseService.getClearingHouseDetails(clearingPoolId));
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "redirect:/showClearanceHouseForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/showClearanceHouseForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}finally{
			try {
				model.put("masterData", clearingHouseService.getMasterData(1));
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			pageLogger(request,response,"EditClearingHouse");
		}
		return "viewChPoolInformation";
	}
	
	@RequestMapping("/searchClearingHouse.htm")
	public String showWebUser(ClearingHouseDTO clearingHouseDTO,HttpServletRequest request,Map<String,Object> model,HttpServletResponse response,HttpSession session){
		int pageNumber=1;	
			
		try {
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				clearingHouseDTO = (ClearingHouseDTO) session.getAttribute("clearingHouseDTO");
			} else {
				session.setAttribute("clearingHouseDTO",clearingHouseDTO);
			}	
			Page page=clearingHouseService.searchClearingHouse(clearingHouseDTO, pageNumber);
			page.requestPage = "searchClearingHouse.htm" ;
			model.put("page", page);
			model.put("message", request.getParameter("message"));
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ClearingHouse");
			model.put("clearingHouseDTO", clearingHouseDTO);		
			model.put("currencyList", clearingHouseService.getCurrencyList());
		}
		return "clearingHouseManagement";

	}
	@RequestMapping("/addClearanceHouseForm.htm")
	public String addClearanceHouse(Map<String,Object> model,ClearingHouseDTO clearanceDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("message", request.getParameter("message"));
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 1 ;
			model.put("masterData", clearingHouseService.getMasterData(pageNumber));
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			pageLogger(request,response,"ClearingHouse");
		}	
		return "addClearingHouse";

	}
	
}
