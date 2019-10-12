package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.ServiceChargeSplitService;

@Controller
public class ServiceChargeSplitController extends PageViewController {

	@Autowired
	private ServiceChargeSplitService serviceChargeSplitService ;
	@Autowired
	private LocaleResolver localeResolver;
	@RequestMapping("/showServiceChargeSplitForm.htm")
	public String showServiceChargeSplitForm(@RequestParam Integer bankId, HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){

		try{
			model.put("message", request.getParameter("message"));
			model.put("bank",serviceChargeSplitService.getBank(bankId));
			model.put("txnTypes", serviceChargeSplitService.getSCApplicableTransactionTypes(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("serviceChargeMap", serviceChargeSplitService.getServiceCharges(bankId));
			model.put("stakeHolderList", serviceChargeSplitService.getstakeHolders());
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"ServiceChargeSplit");
		}

		return "serviceChargeSplit";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/saveServiceChargeSplit.htm")
	public String saveServiceChargeSplit(@RequestParam Integer bankId, HttpServletRequest request,Map<String,Object> model){

		try{
			Map<String,String[]> reqParam = request.getParameterMap();
			model.put("bankId", bankId);
			serviceChargeSplitService.saveServiceChargeSplit(bankId, reqParam);

			model.put("message", "SC_PERCENTAGE_UPDATED");
			
			return "redirect:showServiceChargeSplitForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");

		} catch (EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "redirect:showServiceChargeSplitForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			
		}
		return "serviceChargeSplit";
	}

}
