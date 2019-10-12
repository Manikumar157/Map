package com.eot.banking.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eot.banking.exceptions.EOTException;

/**
 * The Interface ReportService.
 */
public interface ReportService {
	

	/**
	 * File upload instructions.
	 *
	 * @param request the request
	 * @param response the response
	 * @param templeteId 
	 * @throws EOTException the EOT exception
	 */
	void fileUploadInstructions(HttpServletRequest request, HttpServletResponse response, Integer templeteId)  throws EOTException;

}
