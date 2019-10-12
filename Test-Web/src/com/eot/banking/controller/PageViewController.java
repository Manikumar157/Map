package com.eot.banking.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.PageService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReportFont;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignReportFont;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


@Controller
public abstract class PageViewController extends MultiActionController{
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private AppConfigurations appConfigurations;

	public void pageLogger(HttpServletRequest request, HttpServletResponse response, String page) {
		pageService.savePageDetails(request, response,page);
	}

	public void closeSession(HttpServletRequest request, HttpServletResponse response, String page) {
		pageService.closeSession( request, response,page);
	}
	
/*	
 * 
 * public void generateExcelReport(String reportName, String fileName, List dataList, 
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;
		ServletOutputStream outputStream = null;
		File file = null;
		try {
			
			//Uncomment below for QA or prod
			//String path = request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+ "/";
			String path =appConfigurations.getJasperPath()+ "/";
			jasperDesign = JRXmlLoader.load(path + reportName);
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			String image = (String)model.get("entityNameImg");
			file =new File(path+image+".jpg");
			if(file.exists()){
				model.put(EOTConstants.JASPER_PATH, path+image+".jpg");
			}else{
				model.put(EOTConstants.JASPER_PATH, path+"KIFIYA.jpg");
			}
			 //NO need of currency code
			//model.put("currencyCode", commonService.getCurrencyCode(request));
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(
					dataList);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,
					model, datasource);
			report.setOrientation(OrientationEnum.LANDSCAPE);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// coding For Excel:
			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT,
					report);
			exporterXLS
					.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
			exporterXLS
					.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
							Boolean.FALSE);
			exporterXLS
			.setParameter(JRXlsExporterParameter.MAXIMUM_ROWS_PER_SHEET,
					Integer.decode("65000"));
			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.TRUE);
			exporterXLS.setParameter(
					JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.FALSE);
			exporterXLS
					.setParameter(
							JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
							Boolean.FALSE);

			exporterXLS.exportReport();

			String dt = new SimpleDateFormat(EOTConstants.JASPER_FILE_DATE_FORMAT)
					.format(new Date());
			fileName = fileName + "_" + dt + ".xls";

			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
			response.setContentType("application/vnd.ms-excel");

			response.setContentLength(baos.size());
			outputStream = response.getOutputStream();
			baos.writeTo(outputStream);

		} catch (JRException e) {
			e.printStackTrace();
			//handleException(request, response, e);
		} catch (IOException e) {
			e.printStackTrace();
			//handleException(request, response, e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				//handleException(request, response, e);
			}
		}

	}*/
	
	public void generatePDFReport(String reportName, String fileName, List dataList, 
			Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) {
		
		JasperReport jasperReport = null;
		JasperDesign jasperDesign = null;
		ServletOutputStream outputStream = null;
		File file = null;
		try {
			//Uncomment below for QA or prod
		//	String path = request.getSession().getServletContext().getRealPath(appConfigurations.getJasperPath())+ "/";
			//Uncomment below to run locally
			String path = appConfigurations.getJasperPath()+ "/";
			System.out.println("Path=======> "+path);
			System.out.println("jasperPath=======> "+appConfigurations.getJasperPath());
			jasperDesign = JRXmlLoader.load(path + reportName);
			jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			/*JRReportFont font = new JRDesignReportFont();
            font.setPdfFontName(path+"nyala.ttf");
            font.setPdfEncoding(com.lowagie.text.pdf.BaseFont.IDENTITY_H);
            font.setPdfEmbedded(true);*/
            
            
		/*	String image = (String)model.get("entityNameImg");
			file =new File(path+image+".jpg");
			if(file.exists()){
				model.put(EOTConstants.JASPER_PATH, path+image+".jpg");
			}else{
				model.put(EOTConstants.JASPER_PATH, path+"KIFIYA.jpg");
			}*/
			//model.put("currencyCode", commonService.getCurrencyCode(request));
			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(
					dataList);
			JasperPrint report = JasperFillManager.fillReport(jasperReport,
					model, datasource);
			// report.setDefaultFont(font);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
			//Set default zoom size of pdf
			pdfExporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.zoom = 100;");
			pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			pdfExporter.exportReport();

			String dt = new SimpleDateFormat(EOTConstants.JASPER_FILE_DATE_FORMAT).format(new Date());
			fileName = fileName + "_" + dt + ".pdf";

			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
			response.setContentType("application/pdf");

			response.setContentLength(baos.size());
			outputStream = response.getOutputStream();
			baos.writeTo(outputStream);

		} catch (JRException e) {
			e.printStackTrace();
			//handleException(request, response, e);
		} catch (IOException e) {
			e.printStackTrace();
			//handleException(request, response, e);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				//handleException(request, response, e);
			}
		}
	}
	
		@ExceptionHandler(value={Exception.class})
		public void handleException(HttpServletRequest request,
				Exception ex, Map<String, Object> map) {
			ex.printStackTrace();
		}
		
	

}
