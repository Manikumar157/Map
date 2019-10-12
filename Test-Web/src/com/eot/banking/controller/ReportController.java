package com.eot.banking.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.ReportService;


// TODO: Auto-generated Javadoc
/**
 * The Class ReportController.
 */

@Controller
public class ReportController extends PageViewController {

   /** The report service. */
   @Autowired
	private ReportService reportService;
	
   /**
    * File upload instructions.
    *
    * @param transactionParamDTO the transaction param DTO
    * @param model the model
    * @param request the request
    * @param response the response
    * @return the string
    */
   @RequestMapping("/fileUploadInstructions.htm")
	public String fileUploadInstructions(@RequestParam Integer templeteId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			reportService.fileUploadInstructions(request , response , templeteId);
			
		}catch(EOTException e){
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);	
			e.printStackTrace();
			return "addBank";
		}

		return "addBank";
	}
}
