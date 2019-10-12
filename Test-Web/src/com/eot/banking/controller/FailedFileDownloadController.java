package com.eot.banking.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.FailFilesDownloadDTO;
import com.eot.banking.dto.FileUploadDTO;
import com.eot.banking.service.BusinessPartnerService;
import com.eot.banking.service.FileProcessService;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.BusinessPartner;
import com.eot.entity.FileUploadDetail;

@Controller
public class FailedFileDownloadController extends PageViewController {

	/** The file process service. */
	@Autowired
	private FileProcessService fileProcessService;
	
	@Autowired
	private AppConfigurations appConfigurations ;
	
	@Autowired
	private BusinessPartnerService businessPartnerService;

	@RequestMapping(value="/showFailedFiles.htm", method = RequestMethod.POST)//HttpServletRequest request,Map<String,Object> model,HttpServletResponse response
	public String showFailedFiles(Map<String, Object> model,HttpServletRequest request,	HttpServletResponse response,FileUploadDTO fileUploadDTO){
		
		ArrayList<FailFilesDownloadDTO> fileList = new ArrayList<FailFilesDownloadDTO>();
		int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
		Page page=fileProcessService.getFailFileDetailList(fileUploadDTO, pageNumber);
			
			model.put("page", page);
		
		/*if(fileList.size()==0)
			model.put(FIConstants.FAILURE_MESSAGE, MessageConstants.NO_RECORDS_FOUND);*/

		return "failedFilesListForm";
	}
	
	@RequestMapping(value="/downloadFailedFiles.htm", method = RequestMethod.POST)
	public void downloadFailedFiles(Map<String, Object> model,HttpServletRequest request,	HttpServletResponse response,FileUploadDTO fileUploadDTO) throws Exception{
		
		FileInputStream fIn =null;
		ServletOutputStream outputStream = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		String proFileName = fileUploadDTO.getFailedFileName();
		
		BusinessPartner businessPartner = businessPartnerService.getBusinessPartner(userName);
		//String dirName =path+businessPartner.getName()+File.separator+"Processed";
		//String fileName = userName+"_"+System.currentTimeMillis()+".xls";
		try {
			String path = appConfigurations.getBulkPaymentReportDownloadPath();
			String partnerName = businessPartner.getName();
			partnerName = partnerName.contains(" ")?partnerName.replace(" ", "_"):partnerName;
			path=path+partnerName+File.separator+EOTConstants.BULK_PAYMENT_PROCESSED+File.separator;
			fIn = new FileInputStream(path+proFileName.substring(0,proFileName.indexOf("."))+".xls");
			byte[] bytes = IOUtils.toByteArray(fIn);
			baos.write(bytes);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileUploadDTO.getFailedFileName().substring(0,fileUploadDTO.getFailedFileName().indexOf("."))+".xls");
			response.setContentLength(baos.size());
			outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			fileUploadDTO.setFailedFileName(null);
			
		}	
		/*catch(FileNotFoundException e){
			model.put(FIConstants.FAILURE_MESSAGE, MessageConstants.FILE_DOWNLOAD_FAIL);

		}*/ catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			model.put("fileUploadDTO",fileUploadDTO);
			if (fIn!=null) {
				fIn.close();
			}
			if (baos!=null) {
				baos.flush();
				baos.close();
			}
			if (outputStream!=null) {
				outputStream.flush();
				outputStream.close();
			}
			
		}	
	}

}
