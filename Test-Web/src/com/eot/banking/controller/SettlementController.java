package com.eot.banking.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.ClearingHouseService;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.SettlementService;
import com.eot.banking.service.WebUserService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.entity.BankTellers;

@Controller
public class SettlementController {
	@Autowired
	SettlementService settleMentService;
	@Autowired
	private ClearingHouseService clearingHouseService;
	@Autowired
	private LocaleResolver localeResolver;
	@Autowired
	private WebUserService webUserService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AppConfigurations appConfigurations ;
	@Autowired
	private ExcelWrapper wrapper;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("generateSettlementReport.htm")
	public String generateSettlementReport(@RequestParam Integer clearingPoolId,HttpServletRequest request,Map<String,Object> model,ClearingHouseDTO clearingHouseDTO,HttpServletResponse response){

		//String fromDate=request.getParameter("fromDate");
		String settlementDate=request.getParameter("sDate");
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		try{

			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String fileData=settleMentService.createsettlementFileForPool(clearingPoolId,settlementDate,clearingHouseDTO,userName,response);
			if(fileData.length()>0){ 
				clearingHouseDTO=clearingHouseService.getClearingHouseDetails(clearingPoolId);
				byte[] stringByte = fileData.getBytes();
				OutputStream os = response.getOutputStream();
				//String filename = "MT"+clearingHouseDTO.getMessageType()+"_" + clearingHouseDTO.getClearingHouseName() +"_" + settlementDate+ ".xml";
				response.setHeader("Content-Disposition", "attachment; filename=MT"+clearingHouseDTO.getMessageType()+"_"+clearingHouseDTO.getClearingHouseName() +"_" + settlementDate+ ".xml" );
				response.setContentType("application/xml");
				os.write(stringByte);
				os.flush();
				os.close();

				//return "viewChPoolInformation";
			}
		}catch (EOTException e) {
			e.printStackTrace();
			//model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "viewChPoolInformation";
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			try{				
				model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingPoolId));
				model.put("bankList", settleMentService.getBanksByPoolId(clearingPoolId));
				model.put("language",localeResolver.resolveLocale(request).toString());
				model.put("settlementDate", settlementDate);
			}catch(EOTException e){

			}
		}
		return null;
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	@RequestMapping("viewClearingHousePool.htm")
	public String viewClearingHousePool(ClearingHouseDTO clearingHouseDTO,Map<String,Object> model,HttpServletRequest request){
		try {

			model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingHouseDTO.getClearingPoolId()));
			model.put("message", request.getParameter("message"));
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
				model.put("bankList", settleMentService.getBanksByPoolId(clearingHouseDTO.getClearingPoolId()));
				//@End
				model.put("language",localeResolver.resolveLocale(request).toString());
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}

		}
		return "viewChPoolInformation";
	}	
	@RequestMapping("getSettlementReport.htm")
	public String getSettlementReport(@RequestParam Integer clearingPoolId,@RequestParam String settlementDate,HttpServletRequest request,Map<String,Object> model,ClearingHouseDTO clearingHouseDTO){
		String message=null;
		try{
			model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingPoolId));
			model.put("bankList", settleMentService.getBanksByPoolId(clearingPoolId));
			/*String fromDate=request.getParameter("fromDate");
			String toDate=request.getParameter("toDate");*/

			//settleMentService.createsettlementFileForPool(clearingPoolId,fromDate,toDate,clearingHouseDTO);

			//message= request.getParameter("message");
			model.put("message", request.getParameter("message"));

		}catch (EOTException e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "viewChPoolInformation";
		} catch(Exception e){
			e.printStackTrace();
		}
		return "viewChPoolInformation";
	}	
	@RequestMapping("settlementDetails.htm")
	public String getSettlementDetails(HttpServletRequest request,Map<String,Object> model,ClearingHouseDTO clearingHouseDTO){
		String message=null;
		try{
			String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
			model.put("poolList", settleMentService.getBankIdByUserName(userName));


		}catch (EOTException e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "ClearingHousePoolList";
		} catch(Exception e){
			e.printStackTrace();
		}
		return "ClearingHousePoolList";
	}	

	@RequestMapping("viewCHPool.htm")
	public String viewCHPool(@RequestParam Integer clearingPoolId,Map<String,Object> model,HttpServletRequest request){

		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		try {

			model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingPoolId));
			model.put("message", request.getParameter("message"));
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
				model.put("bankName", settleMentService.getBankName(userName));
				model.put("language",localeResolver.resolveLocale(request).toString());
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}

		}
		return "viewCHPool";
	}

	@RequestMapping("generateCSVFile.htm")
	public String generateCSVFile(@RequestParam Integer clearingPoolId,ClearingHouseDTO clearingHouseDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		List list = null;
		try {

			String csv = settleMentService.generateCSVFile(clearingPoolId,userName,localeResolver.resolveLocale(request),clearingHouseDTO);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			model.put("message", "SETTLEMENT_FILE_SUCCESSFULLY");
			response.setContentType("application/vnd.csv");
			response.setHeader("Content-Disposition", "attachment; filename=GMO010_IN_TRANS_DETAILS_MEMBER"+date+"_"+System.currentTimeMillis()+ ".csv");
			OutputStream os = response.getOutputStream();
			os.write(csv.getBytes());
			os.flush();
			os.close();
			
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "viewCHPool";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "viewCHPool";
		}finally{
			try {
				model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingPoolId));
				model.put("bankName", settleMentService.getBankName(userName));
				model.put("language",localeResolver.resolveLocale(request).toString());
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}

		}
		return null;
	}	
	@RequestMapping("generateSettlementSummaryCSVFile.htm")
	public String generateSettlementSummaryCSVFile(@RequestParam Integer clearingPoolId,ClearingHouseDTO clearingHouseDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){

		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		List list = null;
		try {

			String csv = settleMentService.generateSettlementSummaryCSVFile(clearingPoolId,userName,localeResolver.resolveLocale(request),clearingHouseDTO);
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			model.put("message", "SETTLEMENT_FILE_SUCCESSFULLY");
			response.setContentType("application/vnd.csv");
			response.setHeader("Content-Disposition", "attachment; filename=GMO010_IN_TRANS_TOTAUX_MEMBRE"+date+"_"+System.currentTimeMillis()+ ".csv");
			OutputStream os = response.getOutputStream();
			os.write(csv.getBytes());
			os.flush();
			os.close();
			
		} catch (EOTException e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "viewCHPool";
		} catch (Exception e) {
			e.printStackTrace();
			model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "viewCHPool";
		}finally{
			try {
				model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingPoolId));
				model.put("bankName", settleMentService.getBankName(userName));
				model.put("language",localeResolver.resolveLocale(request).toString());
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}

		}
		return null;
	}
	
	@RequestMapping("/viewNetSettlementForm.htm")
	public String viewNetSettlementForm(Map<String,Object> model,HttpServletRequest request){
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
	    BankTellers teller = customerService.getBankTeller(userName);
	    model.put("bankName",teller.getBank().getBankName());
		model.put("clearingHouseDTO",new ClearingHouseDTO());
		
		
		
		return "viewNetSettlementForm";
	}
	
	
	
	
	
	@RequestMapping("/settlementNetBalance.htm")
	public String settlementNetBalance(ClearingHouseDTO clearingHouseDTO,Map<String, Object> model,HttpServletRequest request,HttpServletResponse response){
		
	    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
	    BankTellers teller = customerService.getBankTeller(userName);
		

	    /* JasperReport jasperReport = null;
         JasperDesign jasperDesign = null;*/
         
         List<Map<String,String>> list = null;
 		HSSFWorkbook wb = null;
        
        try{
        	list=settleMentService.getSettlementNetBalance(clearingHouseDTO,userName); 
        	wb = wrapper.createSpreadSheetForsettlementNetBalance(list, localeResolver.resolveLocale(request), messageSource,clearingHouseDTO);
               /* jasperDesign = JRXmlLoader.load(appConfigurations.getJasperPath()+"/"+"settlementNetReport_"+(localeResolver.resolveLocale(request).toString().equals("en_US")?"en_US.jrxml":"fr_FR.jrxml"));	
    
                jasperReport = JasperCompileManager.compileReport(jasperDesign);
                
                List list=settleMentService.getSettlementNetBalance(clearingHouseDTO,userName);       
               
                List list1=settleMentService.getSettlementNetBalance1(clearingHouseDTO,userName);  
                
                model.put("fromDate",clearingHouseDTO.getDate());
                model.put("BankName",teller.getBank().getBankName()); 
                model.put("Issuer",0.00);
                model.put("Acquirer",0.00);
                model.put("SUBREPORT_DIR", appConfigurations.getJasperPath() );
                    
                JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(list);
                JasperPrint report = JasperFillManager.fillReport(jasperReport,model, datasource);
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                exporter.exportReport();*/
                
                /*String dt = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
                String filename = "Settlement_NetBalance"+ "_" + dt + ".pdf";
                
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                response.setContentType("application/pdf");
                
                response.setContentLength(baos.size());
                ServletOutputStream outputStream = response.getOutputStream();
                baos.writeTo(outputStream);
                outputStream.flush();*/
        	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ date + "_" + System.currentTimeMillis() + "_report.xls");
			OutputStream os = response.getOutputStream();

			wb.write(os);
			os.flush();
			os.close();
        }catch (EOTException e) {
        	e.printStackTrace();
        	model.put("clearingHouseDTO", new ClearingHouseDTO());
			model.put("message", e.getErrorCode());
			return "viewCHPool";            
        }catch (Exception e) {
            e.printStackTrace();
            model.put("clearingHouseDTO", new ClearingHouseDTO());
            model.put("message",ErrorConstants.SERVICE_ERROR);
            return "viewCHPool";
               
        }finally{
			try {
				model.put("clearingHouseDTO", settleMentService.viewClearingHouseDetails(clearingHouseDTO.getClearingPoolId()));
				model.put("bankName", settleMentService.getBankName(userName));
				model.put("language",localeResolver.resolveLocale(request).toString());
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}

		}

		return null;
		
		
	}
	
	
}
