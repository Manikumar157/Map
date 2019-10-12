package com.eot.banking.controller;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.dto.BillerDTO;
import com.eot.banking.dto.SenelecBillSearchDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.service.BillerService;
import com.eot.banking.utils.Page;

@Controller
public class BillerController extends PageViewController {
	
	@Autowired 
	BillerService billerService;

	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	private AppConfigurations appConfigurations ;

	@RequestMapping("/searchBiller.htm")
	public String searchBiller(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response,HttpSession session){
		BillerDTO billerDTO=null;
		try {
			billerDTO=new BillerDTO();
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;				
		
			Page page=billerService.getBillersList(pageNumber);
			page.requestPage="searchBillerPage.htm";
			model.put("page",page);
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("billerDTO",billerDTO);
			model.put("message",e.getErrorCode());
		} catch (Exception e) {
			model.put("billerDTO",billerDTO);
			model.put("message","ERROR_9999");
		}finally{
			session.setAttribute("billerDTO", billerDTO);
			model.put("billerDTO", billerDTO);
			model.put("countryList", billerService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("billerTypeList", billerService.getBillerTypes());	
			model.put("bankList", billerService.getBankList());
			model.put("message", request.getParameter("message"));
		}
		model.put("language",localeResolver.resolveLocale(request) );
		pageLogger(request,response,"Biller");
		return "biller";

	}

	@RequestMapping("/searchBillerPage.htm")
	public String searchOperator(BillerDTO billerDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		int pageNumber=1;
		try{	
		
			if( request.getParameter("pageNumber") != null ){
				pageNumber = new Integer(request.getParameter("pageNumber"));
				billerDTO = (BillerDTO) session.getAttribute("billerDTO");
			} else {
				session.setAttribute("billerDTO", billerDTO);
			}	
		
			Page page=billerService.getBillersList(billerDTO, pageNumber);
			page.requestPage="searchBillerPage.htm";
			model.put("page",page);	
		
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("billerDTO",billerDTO);
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("billerDTO",billerDTO);
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"Biller");
			model.put("billerDTO", billerDTO);
			model.put("countryList", billerService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("billerTypeList", billerService.getBillerTypes());	
			model.put("bankList", billerService.getBankList());
		}

		return "biller";

	}
	@RequestMapping("/showBillersForm.htm")
	public String showBiller(HttpServletRequest request,Map<String,Object> model,HttpServletResponse response){
		int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
		model.put("billerDTO", new BillerDTO());
		model.put("countryList", billerService.getCountryList(localeResolver.resolveLocale(request).toString()));
		model.put("billerTypeList", billerService.getBillerTypes());	
		model.put("bankList", billerService.getBankList());
		model.put("message", request.getParameter("message"));
		//	model.put("page", billerService.getBillersList(pageNumber));
		model.put("language",localeResolver.resolveLocale(request) );
		pageLogger(request,response,"Biller");
		return "addBiller";

	}
	@RequestMapping("/saveBillersForm.htm")
	public String saveBiller(@Valid BillerDTO billerDTO,BindingResult result,HttpServletRequest request,Map<String,Object> model){
		
		boolean flag=false;
		try {

			if(result.hasErrors()){
				model.put("bankDTO",billerDTO);
			}else  if(billerDTO.getBillerId() == null ){
				billerService.addBillerDetails(billerDTO);
				model.put("message", "ADD_BILLER_SUCCESS");
				/*return "redirect:/showBillersForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
				model.put("billerDTO", new BillerDTO());
			//	return "addBiller";
			}
			else{
				billerService.updateBillerDetails(billerDTO);	
				model.put("message", "EDIT_BILLER_SUCCESS");
				model.put("billerDTO", new BillerDTO());
				/*return "redirect:/showBillersForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");*/
			//	return "addBiller";
			}

		} catch (EOTException e) {
			flag=true;
			model.put("billerDTO",billerDTO);
			model.put("message",e.getErrorCode());
			return "addBiller";
		} catch (Exception e) {
			flag=true;
			e.printStackTrace();
			model.put("billerDTO",billerDTO);
			model.put("message","ERROR_9999");
			return "addBiller";
		} finally{
			try{
				int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;	
				if(flag==false) {			
				//	Page page=billerService.getBillersList(new BillerDTO(), 0);
					billerDTO=new BillerDTO();
					Page page=billerService.getBillersList(pageNumber);
					page.requestPage="searchBillerPage.htm";
					model.put("page",page);	
				}
			}catch(EOTException e) {
				e.printStackTrace();
				model.put("message", e.getErrorCode());
			} catch (Exception e) {
				e.printStackTrace();
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			model.put("language",localeResolver.resolveLocale(request) );
			model.put("countryList", billerService.getCountryList(localeResolver.resolveLocale(request).toString()));
			model.put("billerTypeList", billerService.getBillerTypes());	
			model.put("bankList", billerService.getBankList());	
			
		}

		return "biller";

	}
	@RequestMapping("/editBiller.htm")
	public String editBiller(HttpServletRequest request ,BillerDTO billerDTO,Map<String,Object> model,HttpServletResponse response){
		try{
			//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting 
			model.put("billerDTO",billerService.getBillerDetails(billerDTO.getBillerId()));
			//@End
		} catch (EOTException e) {
			model.put("message",e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message","ERROR_9999");
		} finally{
			pageLogger(request,response,"EditBiller");
			try {
				model.put("language",localeResolver.resolveLocale(request) );
				model.put("countryList", billerService.getCountryList(localeResolver.resolveLocale(request).toString()));
				model.put("billerTypeList", billerService.getBillerTypes());	
				model.put("bankList", billerService.getBankList());
				int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
				Page page=billerService.getBillersList(pageNumber);
				page.requestPage="searchBiller.htm";
				model.put("page",page);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return "addBiller";
	}

	@RequestMapping("/getSenelecBills.htm")
	public String searchSenelecBill(Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		try{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page=billerService.getSenelecBills(pageNumber);
			page.requestPage = "getSenelecBills.htm";
			model.put("page",page);	
			model.put("message",request.getParameter("message") != null ?request.getParameter("message"):null);
			model.put("senelecBillSearchDTO", new SenelecBillSearchDTO());
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"SenelecBills");
		}

		return "viewSenelecBills";

	}

	@RequestMapping("/searchSenelecBill.htm")
	public String searchSenelecBill(SenelecBillSearchDTO senelecBillSearchDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		try{
			if(request.getParameter("pageNumber") != null ){
				senelecBillSearchDTO = (SenelecBillSearchDTO) session.getAttribute("senelecBillSearchDTO");
			} else {
				session.setAttribute("senelecBillSearchDTO", senelecBillSearchDTO);
			}	
			String recordNo=senelecBillSearchDTO.getRecordNo().trim();
			senelecBillSearchDTO.setRecordNo(recordNo);
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			if(!senelecBillSearchDTO.getRecordNo().isEmpty() && !(Long.parseLong(senelecBillSearchDTO.getRecordNo())<=2147483647)) {
				throw new EOTException(ErrorConstants.BILL_NOT_FOUND);
			}
			Page page=billerService.searchSenelecBill(senelecBillSearchDTO,pageNumber);
			page.requestPage = "searchSenelecBill.htm";
			model.put("page",page);	
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			model.put("senelecBillSearchDTO",senelecBillSearchDTO);
			pageLogger(request,response,"SearchSenelecBill");
		}

		return "viewSenelecBills";

	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting 
	@RequestMapping("/viewDetailsByPolicyNo.htm")
	public String viewDetailsByPolicyNo(SenelecBillSearchDTO senelecBillSearchDTO,Map<String,Object> model,HttpServletRequest request,HttpSession session,HttpServletResponse response){

		try{
			/*String policyNo=request.getParameter("pno");
			Integer recordNo=Integer.valueOf(request.getParameter("recordNo"));*/
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			billerService.getPolicyDetailsByPolicyNo(model,pageNumber,senelecBillSearchDTO.getPolicyNo(),Integer.valueOf(senelecBillSearchDTO.getRecordNo()));
//@End
		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		} finally{
			pageLogger(request,response,"ViewPolicyDetails");
		}

		return "viewPolicyDetails";

	}

	//Author Name:Abu Kalam Azad Date:23/07/2018 purpouses:Bill Payment pdf data should be based on criteria..(5761)
	//start...
	@RequestMapping("/senelecPdfReport.htm")
	public String senelecBillPdfGeneration(SenelecBillSearchDTO senelecBillSearchDTO ,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;
		try{
            
			//List billList=billerService.getAllPaidBills();
			List billList =billerService.searchSenelecBillForPdf(senelecBillSearchDTO);
			
			if(billList==null || billList.isEmpty() || billList.size()==0)
				throw new EOTException(ErrorConstants.BILL_NOT_FOUND);
			
			//...End
			jasperDesign = JRXmlLoader.load(appConfigurations.getJasperPath()+"/"+"CodePrestataire_trx_en_US.jrxml");			
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			
			
			SenelecBillSearchDTO searchDTO=new SenelecBillSearchDTO();
			searchDTO.setBillList(billList);

			List result=new ArrayList();
			result.add(searchDTO);

			model.put("totalTxn", billList.size());
		    model.put("totalTxnAmount",billerService.getTotalTransactionAmount());            
			model.put("SUBREPORT_DIR", appConfigurations.getJasperPath());
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(result);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,model, datasource);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();

			String dt = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String filename = "CodePrestataire-trx-"+ "_" + dt + ".pdf";

			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.setContentType("application/pdf");

			response.setContentLength(baos.size());
			ServletOutputStream outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			outputStream.flush();


		}catch(EOTException e) {
			e.printStackTrace();
			model.put("message", e.getErrorCode());
			return "redirect:/getSenelecBills.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "redirect:/getSenelecBills.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		} finally{
			pageLogger(request,response,"SenelecPdfReport");
		}
		return null;
	}

}
