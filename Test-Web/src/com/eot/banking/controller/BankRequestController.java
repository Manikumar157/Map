package com.eot.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eot.banking.dto.LimitUpdateReqDTO;
import com.eot.banking.dto.LimitUpdateRespDTO;
import com.eot.banking.service.BankRequstService;
import com.eot.banking.service.BankRequstServiceImpl;

/**
 * @author Bidyut
 * 
 * */
@Controller
//@RequestMapping(value="/rest/service/")
public class BankRequestController extends PageViewController{
	
	@Autowired
	private BankRequstService bankRequestService;
	
	@ResponseBody
	@RequestMapping(value="transferRequest.htm", method=RequestMethod.POST)
	public LimitUpdateRespDTO validateAndUpdateTransaferRequest(@RequestBody LimitUpdateReqDTO limitUpdateReqDTO)
	{
		return bankRequestService.processLimitDepostiTransaction(limitUpdateReqDTO);
	}
	
	@ResponseBody
	@RequestMapping(value="fetchDetails.htm", method=RequestMethod.POST)
	public LimitUpdateRespDTO fetchBillerDetails(@RequestBody LimitUpdateReqDTO limitUpdateReqDTO)
	{
		return bankRequestService.fetchDetails(limitUpdateReqDTO);
	}

}
