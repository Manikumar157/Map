package com.eot.banking.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.exceptions.EOTException;

public interface CommonService {
	
	CustomerDTO viewCustomer(Long customerId ,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response,String viewName);

	void showUserManagementForm(HttpServletRequest request, Map<String, Object> model, HttpServletResponse response) throws EOTException,Exception;

}
