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

import com.eot.banking.dto.BankDTO;
import com.eot.banking.dto.BranchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.BranchService;
import com.eot.banking.utils.Page;

@Controller
public class BranchController extends PageViewController {

	@Autowired
	private BranchService branchService;

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	@RequestMapping("/searchBranch.htm")
	public String searchBranch(BranchDTO branchDTO,BankDTO bankDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		int pageNumber=1;
		try{
			model.put("bankId", bankDTO.getBankId());
			model.put("countryId", request.getParameter("countryId"));
			model.put("message", request.getParameter("message"));
			if( branchDTO.getCityId()!= null && ! "".equals(branchDTO.getCityId()) ){
				model.put("quarterList",branchService.getQuarterList(branchDTO.getCityId()));
				//@End
			}
			 pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));	
			
			Page page=branchService.searchBranch(branchDTO,pageNumber);
			page.requestPage="branchSearch.htm";
			model.put("page",page);		
			
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		}finally{
			model.put("masterData", branchService.getMasterData(branchDTO.getBankId(),pageNumber));
			model.put("branchList",branchService.getBranchList());
			pageLogger(request,response,"Branch");
			session.setAttribute("branchDTO",branchDTO);			
		}	
		return "searchBranch";
	}
	
	@RequestMapping("/branchSearch.htm")
	public String branchSearch(BranchDTO branchDTO,@RequestParam Integer bankId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response,HttpSession session){
		int pageNumber=1;
				
			try{				
				if( request.getParameter("pageNumber") != null ){
					pageNumber = new Integer(request.getParameter("pageNumber"));
					branchDTO = (BranchDTO) session.getAttribute("branchDTO");
				} else {
					session.setAttribute("branchDTO", branchDTO);
				}	
			model.put("bankId", bankId);
			model.put("countryId", request.getParameter("countryId"));
			model.put("message", request.getParameter("message"));
			if( branchDTO.getCityId()!= null && ! "".equals(branchDTO.getCityId()) ){
				model.put("quarterList",branchService.getQuarterList(branchDTO.getCityId()));
			}
			 
			Page page=branchService.searchBranch(branchDTO,pageNumber);
			page.requestPage="branchSearch.htm";
			model.put("page",page);		
			
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		}finally{
			model.put("masterData", branchService.getMasterData(branchDTO.getBankId(),pageNumber));
			pageLogger(request,response,"Branch");
			model.put("branchDTO",branchDTO);
		}	
		return "searchBranch";
	}

	@RequestMapping("/AddBranchForm.htm")
	public String showAddBranchForm(@RequestParam Integer bankId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			BranchDTO branchDTO = new BranchDTO();
			branchDTO.setBankId(bankId);
			model.put("branchDTO", branchDTO );
			model.put("message", request.getParameter("message"));
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
			model.put("masterData", branchService.getMasterData(branchDTO.getBankId(),pageNumber));
		}catch (Exception e) {	
			model.put("message","ERROR_9999");
		}finally{
			pageLogger(request,response,"Branch");
		}	
		return "addBranch";
	}

	@RequestMapping("/saveBranch.htm")	
	public String saveBranchDetails(@Valid BranchDTO branchDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException {
		try{
			if(result.hasErrors()){					
				model.put("branchDTO",branchDTO);			
			}			
			else if(branchDTO.getBranchId() == null){				
				branchService.addBranchDetails(branchDTO);	
				model.put("branchDTO",new BranchDTO());	
				model.put("bankId",branchDTO.getBankId());
				model.put("message","ADD_BRANCH_SUCCESS");
				//return "redirect:/AddBranchForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			} 
			else{					
				branchService.updateBranchDetails(branchDTO);
				model.put("branchDTO",new BranchDTO());
				model.put("bankId",branchDTO.getBankId());
				model.put("message","EDIT_BRANCH_SUCCESS");
				//return "redirect:/AddBranchForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		} catch (EOTException e) {
			model.put("branchDTO",branchDTO);
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("branchDTO",branchDTO);
			model.put("message","ERROR_9999");
		} finally{
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			model.put("quarterList", branchDTO.getCityId() !=null ?branchService.getQuarterList(branchDTO.getCityId()):null);
			model.put("masterData", branchService.getMasterData(branchDTO.getBankId(),pageNumber));
		}
		return "addBranch";
	}

	@RequestMapping("/editBranchDetails.htm")
	public String editBranchDetails(@RequestParam Long branchId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		BranchDTO branchDTO = null;
		try{	     
			branchDTO = branchService.getBranchDetails(branchId) ;
			model.put("branchDTO", branchDTO );			
			model.put("quarterList", branchService.getQuarterList(branchDTO.getCityId()));
			model.put("masterData", branchService.getMasterData(branchDTO.getBankId(),1));
		} catch (EOTException e) {
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		}finally{
			pageLogger(request,response,"EditBranch");
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			model.put("quarterList", branchDTO.getCityId() !=null ?branchService.getQuarterList(branchDTO.getCityId()):null);
			model.put("masterData", branchService.getMasterData(branchDTO.getBankId(),pageNumber));
		}
		return "addBranch";
	}
}
