package com.eot.banking.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.OperatorDTO;
import com.eot.banking.dto.OperatorDenominationDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.OperatorService;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;

@Controller
public class OperatorController extends PageViewController {

	@Autowired
	private OperatorService operatorService;
	@Autowired
	private LocaleResolver localeResolver;
	@RequestMapping("/showOperators.htm")
	public String showOperators(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response,HttpSession session){	
		OperatorDTO operatorDTO = null;
		try {	

			operatorDTO = new OperatorDTO();
		
			int pageNumber = request.getParameter("pageNumber") != null && request.getParameter("pageNumber") != "" ? new Integer(request.getParameter("pageNumber")) : 0 ;
			model.put("operatorDTO",operatorDTO);
			model.put("message", request.getParameter("message"));

			Page page = operatorService.getOperatorList(pageNumber);
			page.requestPage="searchOperators.htm";
			model.put("page",page);
			model.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("bankList", operatorService.getBankList());
			model.put("language",localeResolver.resolveLocale(request));
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			session.setAttribute("operatorDTO",operatorDTO);
			pageLogger(request,response,"Operator");
		}
		return "operator";		
	}
	
	@RequestMapping("/addOperators.htm")
	public String addOperators(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){	
		try {	

			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			
			model.put("operatorDTO",new OperatorDTO());
			model.put("message", request.getParameter("message"));

			Page page = operatorService.getOperatorList(pageNumber);
			model.put("page",page);
			model.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("bankList", operatorService.getBankList());
			model.put("language",localeResolver.resolveLocale(request));
		} catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"Operator");
		}
		return "addOperator";		
	}
	
	@RequestMapping("/searchOperators.htm")
	public String searchOperator(OperatorDTO operatorDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		int pageNumber=1;
		try{	
		
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				operatorDTO = (OperatorDTO) session.getAttribute("operatorDTO");
			} else {
				session.setAttribute("operatorDTO", operatorDTO);
			}	
		
			Page page = operatorService.searchOperator(operatorDTO,pageNumber,localeResolver.resolveLocale(request).toString());
			page.requestPage = "searchOperators.htm";
			model.put("page",page);	
			model.put("language",localeResolver.resolveLocale(request) );
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"Operator");
			model.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("bankList", operatorService.getBankList());
			model.put("language",localeResolver.resolveLocale(request));
			model.put("operatorDTO",operatorDTO);
		}

		return "operator";

	}

	@RequestMapping("/saveOperator.htm")
	public String saveOperator(@ModelAttribute OperatorDTO operatorDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request, HttpServletResponse response, HttpSession session )throws EOTException{
		try{

			if(result.hasErrors()){
				model.put("operatorDTO",operatorDTO);
			} else if(operatorDTO.getOperatorId()==null){
				operatorService.addOperator(operatorDTO);
				//model.put("operatorDTO",new OperatorDTO());
				model.put("message","ADD_OPERATOR_SUCCESS");
				return "operator";
				//return "redirect:/addOperators.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			} else{
				operatorService.updateOperator(operatorDTO);
				//model.put("operatorDTO",new OperatorDTO());			
				model.put("message","EDIT_OPERATOR_SUCCESS");
				return "operator";
				//return "redirect:/addOperators.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		} catch(EOTException e){
			e.printStackTrace();
			model.put("message",e.getErrorCode());	
			return "addOperator";
		} catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);		
			return "addOperator";
		} finally{
			int pageNumber = request.getParameter("pageNumber") != null && request.getParameter("pageNumber") != "" ? new Integer(request.getParameter("pageNumber")) : 0 ;
			model.put("operatorDTO",new OperatorDTO());
			model.put("message", request.getParameter("message"));

			Page page = operatorService.getOperatorList(pageNumber);
			model.put("page",page);
			model.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("bankList", operatorService.getBankList());
			model.put("language",localeResolver.resolveLocale(request));
			//showOperators(request, model, response, session);
		}
		return "operator";
	}

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/editOperator.htm")
	public String editOperator(OperatorDTO operatorDTO,HttpServletRequest request,Map<String,Object> model,HttpServletResponse response)throws EOTException{

		try{

			model.put("operatorDTO",operatorService.getOperator(operatorDTO.getOperatorId()));
			//@end
		} catch(EOTException e){
			model.put("message",e.getErrorCode());	
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			model.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("bankList", operatorService.getBankList());
			model.put("language",localeResolver.resolveLocale(request));
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = operatorService.getOperatorList(pageNumber);
			page.requestPage="showOperators.htm";
			model.put("page",page);	
			pageLogger(request,response,"EditOperator");

		}
		return "addOperator";
	}	
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/viewDenominations.htm")
	public String viewDenominations(OperatorDTO operatorDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){	
		try{
			OperatorDenominationDTO operatorDenominationDTO=new OperatorDenominationDTO();
			operatorDenominationDTO.setOperatorId(operatorDTO.getOperatorId());
			Page page = operatorService.getDenominations(operatorDTO.getOperatorId(),Integer.parseInt(request.getParameter("pageNumber")));
			model.put("page", page);
			model.put("operatorDenominationDTO", operatorDenominationDTO);
			model.put("message", request.getParameter("message"));
		} catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			model.put("operatoName",operatorService.getOperatorName(operatorDTO.getOperatorId()));
			//@End
			pageLogger(request,response,"Denomination");
		}
		return "operatorDenomination";
	}

	@RequestMapping("/saveDenomination.htm")
	public String saveDenomination(HttpServletRequest request , @Valid OperatorDenominationDTO operatorDenominationDTO,BindingResult result,Map<String,Object> model)throws EOTException{
		try{
			if(result.hasErrors()){
				return "operatorDenomination";
			} else if(operatorDenominationDTO.getDenominationId()==null){
				operatorService.checkDenomination(operatorDenominationDTO);
				operatorService.addDenomination(operatorDenominationDTO);
				model.put("operatorDTO",new OperatorDTO());
				model.put("message","ADD_DENOMINATION_SUCCESS");
			} else{
				operatorService.checkDenomination(operatorDenominationDTO);
				operatorService.updateDenomination(operatorDenominationDTO);
				model.put("operatorDTO",new OperatorDTO());			
				model.put("message","EDIT_DENOMINATION_SUCCESS");
			}


		} catch(EOTException e){
			model.put("message",e.getErrorCode());	
			return "operatorDenomination";
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}
		finally{
			model.put("operatoName",operatorService.getOperatorName(operatorDenominationDTO.getOperatorId())) ;
			Page page = operatorService.getDenominations(operatorDenominationDTO.getOperatorId(),EOTConstants.DEFAULT_PAGE);
			model.put("page", page);


		}
		return "operatorDenomination";
		//return "redirect:/viewDenominations.htm?pageNumber=1&operatorId="+operatorDenominationDTO.getOperatorId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/editDenomination.htm")
	public String editDenomination(OperatorDenominationDTO operatorDenominationDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		
		try{
			operatorDenominationDTO = operatorService.getDenomination(operatorDenominationDTO.getDenominationId());
			//@End
			model.put("operatoName",operatorService.getOperatorName(operatorDenominationDTO.getOperatorId())) ;
			model.put("operatorDenominationDTO",operatorDenominationDTO);
			//model.put("denomination",operatorService.getDenominations(operatorDenominationDTO.getOperatorId(),EOTConstants.DEFAULT_PAGE));
		} catch(EOTException e){
			model.put("message",e.getErrorCode());	
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			try{
				Page page = operatorService.getDenominations(operatorDenominationDTO.getOperatorId(),EOTConstants.DEFAULT_PAGE);
				model.put("page", page);
			}catch(EOTException e){
				model.put("message",e.getErrorCode());	
			} 
			pageLogger(request,response,"EditDenomination");
		}
		return "operatorDenomination";
	}

	@RequestMapping("/viewVouchers.htm")
	public String viewVouchers(@RequestParam Long operatorId,@RequestParam int pageNumber,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){	

		try{
			OperatorDenominationDTO denominationDTO=new OperatorDenominationDTO();
			denominationDTO.setOperatorId(operatorId);
			model.put("vouchers",operatorService.getVouchers(operatorId,pageNumber).getResults());
			model.put("denominationDTO",denominationDTO);
			model.put("operatorName",operatorService.getOperatorName(operatorId)) ;
		} catch(EOTException e){
			model.put("message",e.getErrorCode());	
			return "operator";
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			pageLogger(request,response,"OperatorVouchers");
		}
		return "operatorVoucher";
	}

	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/uploadVoucherForm.htm")
	public String uploadVouchers(@RequestParam Long operatorId,OperatorDTO operatorDTO,@RequestParam int pageNumber, Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			OperatorDenominationDTO operatorDenominationDTO=new OperatorDenominationDTO();
			if(operatorDTO.getOperatorId()!=null) {

			operatorDenominationDTO.setOperatorId(operatorDTO.getOperatorId());
			Page page = operatorService.getVouchers(operatorDTO.getOperatorId(),pageNumber);
			model.put("page",page);
		//	model.put("operatoName",operatorService.getOperatorName(operatorDTO.getOperatorId())) ;
			//@End
			model.put("operatorDenominationDTO", operatorDenominationDTO);
				
			}
			// added by vineeth, on 17-08-2018. bug no:5933
			else {
				operatorDenominationDTO.setOperatorId(operatorId);
				Page page = operatorService.getVouchers(operatorId,pageNumber);
				model.put("page",page);
				model.put("operatoName",operatorService.getOperatorName(operatorId)) ;
				//@End
				model.put("operatorDenominationDTO", operatorDenominationDTO);
				model.put("message","VOUCHER_FILE_UPLOAD_SUCCESS");	
			}
			// vineeth changes end
		} catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			pageLogger(request,response,"UploadVoucher");
		}
		return "uploadVouchers";
	}

	@RequestMapping("/uploadVouchersFile.htm")
	public String uploadVouchers(OperatorDenominationDTO operatorDenominationDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		try{
			operatorService.uploadVoucherDetails(operatorDenominationDTO);
			model.put("operatorDenominationDTO", new OperatorDenominationDTO());
			model.put("operatoName",operatorService.getOperatorName(operatorDenominationDTO.getOperatorId())) ;
		} catch(EOTException e){
			model.put("message",e.getErrorCode());	
			e.getStackTrace();
			return "uploadVouchers";
		} catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);	
			e.printStackTrace();
			return "uploadVouchers";
		} finally{
			model.put("operatoName",operatorService.getOperatorName(operatorDenominationDTO.getOperatorId())) ;
			try {
				Page page = operatorService.getVouchers(operatorDenominationDTO.getOperatorId(),EOTConstants.DEFAULT_PAGE);
				model.put("page",page);
			} catch (EOTException e) {
				e.printStackTrace();
			}
			pageLogger(request,response,"UploadVouchersFile");
		}
	//	return "redirect:/viewVouchers.htm?operatorId="+operatorDenominationDTO.getOperatorId()+"&pageNumber=1&csrfToken=" + request.getSession().getAttribute("csrfToken");
		return "redirect:/uploadVoucherForm.htm?operatorId="+operatorDenominationDTO.getOperatorId()+"&pageNumber=1&csrfToken=" + request.getSession().getAttribute("csrfToken");
	}
	@RequestMapping("/showReport.htm")
	public String showTopUpReport(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){
		
	try{	
		model.put("operatorDTO",new OperatorDTO());
		model.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
		model.put("language",localeResolver.resolveLocale(request));
		model.put("imgHide", "imgHide");
	}catch(Exception e){
		e.printStackTrace();		
	}finally{
		pageLogger(request,response,"showReport");
	}
		return "topUpReport";
	}
	@RequestMapping("/searchBarReport.htm")
	public String searchBarReport(OperatorDTO operatorDTO,HttpServletRequest request,HttpServletResponse response,Map<String,Object> model){

		try{			
			byte[] chartImg=operatorService.getChartImageBytes(operatorDTO);
			response.setContentType("image/png");
			response.getOutputStream().write(chartImg); 		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"searchBarReport");
		}		
		return null;		
	}

	@RequestMapping("/barReportDataValidation.htm")
	public String barChartDataValidation(OperatorDTO operatorDTO,HttpServletRequest request,HttpServletResponse response,ModelMap map){		
		try{
			operatorDTO.setImgType("imgCount");				
			List list=operatorService.getChartList(operatorDTO);
			map.put("fromDate", DateUtil.formatDateToStr(operatorDTO.getFromDate()));
			map.put("toDate", DateUtil.formatDateToStr(operatorDTO.getToDate()));			
			map.put("imgUrl","searchBarReport.htm"); 	
			map.put("countryId",operatorDTO.getCountryId());
			map.put("operatorId",operatorDTO.getOperatorId());
			map.put("denominationId",operatorDTO.getDenominationId());
		}catch(EOTException e){
			e.printStackTrace();
			map.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			map.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			map.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			map.put("language",localeResolver.resolveLocale(request));        	 
		}
		return "topUpReport";
	}
	@RequestMapping("/searchPieReport.htm")
	public String searchPieReport(OperatorDTO operatorDTO,HttpServletRequest request,HttpServletResponse response,Map<String,Object> model){

		try{			
			byte[] chartImg=operatorService.getPieChartImageBytes(operatorDTO);
			response.setContentType("image/png");
			response.getOutputStream().write(chartImg); 		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pageLogger(request,response,"searchPieReport");
		}			
		return null;		
	}

	@RequestMapping("/pieReportDataValidation.htm")
	public String pieChartDataValidation(OperatorDTO operatorDTO,HttpServletRequest request,HttpServletResponse response,ModelMap map){		
		try{
			operatorDTO.setImgType("imgCount");				
			List list=operatorService.getChartList(operatorDTO);
			map.put("fromDate", DateUtil.formatDateToStr(operatorDTO.getFromDate()));
			map.put("toDate", DateUtil.formatDateToStr(operatorDTO.getToDate()));			
			map.put("imgUrl","searchPieReport.htm"); 	
			map.put("countryId",operatorDTO.getCountryId());
			map.put("operatorId",operatorDTO.getOperatorId());
			map.put("denominationId",operatorDTO.getDenominationId());
			map.put("barReport","barReport");		   
		}catch(EOTException e){
			e.printStackTrace();
			map.put("message", e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			map.put("message", ErrorConstants.SERVICE_ERROR);
		}finally{
			map.put("countryList",operatorService.getCountryList(localeResolver.resolveLocale(request).toString()));
			map.put("language",localeResolver.resolveLocale(request));        	 
		}
		return "topUpReport";
	}
	@RequestMapping("/getOperators.htm")
	public String getOperators(@RequestParam Integer country, Map<String, Object> model){		
		model.put("entity", "operatorName");
		model.put("id", "operatorId");
		model.put("value", "operatorName");
		model.put("status",1);
		model.put("list", operatorService.getOperatorListByCountry(country));		
		return "combo";
	}
	@RequestMapping("/getDenominations.htm")
	public String getDenominations(@RequestParam Long operator, Map<String, Object> model){		
		model.put("entity", "denomination");
		model.put("id", "denominationId");
		model.put("value", "denomination");
		model.put("list", operatorService.getDenominationList(operator));		
		return "combo";
	}
	
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/addOperatorCard.htm")
	public String showAddCardForm(OperatorDTO operatorDTO, HttpServletRequest request, Map<String,Object> model,HttpServletResponse response){
		try{
			model.put("cardDto",operatorService.getCardDetailsByOperatorId((int)(long)operatorDTO.getOperatorId()));
//@End
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		} finally{
			pageLogger(request,response,"AddCard");
		}

		return "addOperatorCard";
	}
	
	@RequestMapping("/saveOperatorCard.htm")
	public String saveCard(CardDto cardDto,HttpServletRequest request, Map<String,Object> model){
		try{
			operatorService.saveOrUpdateCard(cardDto,model);
			
		}catch (EOTException e) {
			e.printStackTrace();
			model.put("message",e.getErrorCode());
		}catch (Exception e1) {
			e1.printStackTrace();
			model.put("message","ERROR_9999");
		} 

		return "addOperatorCard";
	}

}
