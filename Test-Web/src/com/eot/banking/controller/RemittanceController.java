package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.dto.RemittanceDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.RemittanceService;

@Controller
public class RemittanceController extends PageViewController {
	@Autowired
	private RemittanceService remittanceService;
	@Autowired
	private LocaleResolver localeResolver;

	@RequestMapping("/remittanceCompany.htm")
	public String showRemittance(Map<String,Object> model,HttpServletRequest request, HttpServletResponse response){
		model.put("remittanceDTO", new RemittanceDTO());
		int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
		model.put("page", remittanceService.getAllRemittanceCompanies(pageNumber));
		model.put("message", request.getParameter("message"));
		return "remittance";
	}
	
	@RequestMapping("/addRemittanceCompany.htm")
	public String addRemittance(@Valid RemittanceDTO remittanceDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request, HttpServletResponse response){
		try {
			if(result.hasErrors()){
				model.put("remittanceDTO", remittanceDTO);
			}else if(remittanceDTO.getRemittanceCompanyId()== null){
				remittanceService.addRemittanceCompany(remittanceDTO);
				return "redirect:/remittanceCompany.htm?csrfToken=" + request.getSession().getAttribute("csrfToken")+"&message=ADD_REMITTANCE_SUCCESS";
			}else{
				remittanceService.updateRemittanceCompany(remittanceDTO);
				return "redirect:/remittanceCompany.htm?csrfToken=" + request.getSession().getAttribute("csrfToken")+"&message=UPDATE_REMITTANCE_SUCCESS";
			}
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "remittance";
	}
	@RequestMapping("/editRemittanceCompany.htm")
	public String editRemittance(Integer remittanceCompanyId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try {
			model.put("remittanceDTO", remittanceService.getRemittanceCompany(remittanceCompanyId));
			model.put("language",localeResolver.resolveLocale(request).toString().substring(0, 2));
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		}finally {
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			model.put("page", remittanceService.getAllRemittanceCompanies(pageNumber));
		}
		return "remittance";
	}
}
