package com.eot.banking.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.exceptions.EOTException;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private AppConfigurations appConfig;
	@Override
	public void fileUploadInstructions(HttpServletRequest request,HttpServletResponse response , Integer templeteId) throws EOTException {
	
		response.setContentType("text/csv");
	    response.setHeader("Content-Disposition", "attachment; filename=upload_instructions.csv");
	    try
	    {
	    	String fileName = "";
	    	switch(templeteId)
	    	{
	    	case 1:
	         fileName	 = appConfig.getJasperPath()+"/branch_instructions.csv";
	//         fileName	 = request.getSession().getServletContext().getRealPath(appConfig.getJasperPath())+"/branch_instructions.csv";
   //    	fileName = "D:\\home\\wallet\\reports\\branch_instructions.csv";
	    	break;
	    	case 2:
	    	fileName = appConfig.getJasperPath()+"/operator_voucher_instructions.csv";
	 //   	  fileName	 = request.getSession().getServletContext().getRealPath(appConfig.getJasperPath())+"/operator_voucher_instructions.csv";
	//    	fileName = "D:\\home\\wallet\\reports\\operator_voucher_instructions.csv";
	    	break ; 
	    	case 3:
	    	fileName = appConfig.getJasperPath()+"/transaction_support_instructions.csv";
	   // 	  fileName	 = request.getSession().getServletContext().getRealPath(appConfig.getJasperPath())+"/transaction_support_instructions.csv";
	 //   	fileName = "D:\\home\\wallet\\reports\\transaction_support_instructions.csv";
		   	break ; 
	    	case 4:
		    	fileName = appConfig.getJasperPath()+"/mGurushbulk_payment.csv";
		   // 	  fileName	 = request.getSession().getServletContext().getRealPath(appConfig.getJasperPath())+"/transaction_support_instructions.csv";
		 //   	fileName = "D:\\home\\wallet\\reports\\transaction_support_instructions.csv";
			   	break ; 
		    
	    	}
	    	File file = new File(fileName);
	    	@SuppressWarnings("resource")
			FileInputStream fileIn = new FileInputStream(file);
	    	ServletOutputStream out = response.getOutputStream();

	    	byte[] outputByte = new byte[4096];
	    	//copy binary contect to output stream
	    	
	    	 while(fileIn.read(outputByte, 0, 4096) != -1)
	    	{
	    		 /* commented by vineeth, on 16-07-2018, as same data is appended on the file multiple times.*/
	    	//	out.write(outputByte, 0, 4096);
	    	}
	    	
	        OutputStream outputStream = response.getOutputStream();
	        response.getBufferSize();
	        outputStream.write(outputByte);
	        outputStream.flush();
	        outputStream.close();
	    }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	    	System.out.println(e.toString());
	    }
}

}
