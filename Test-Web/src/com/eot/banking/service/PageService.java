package com.eot.banking.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageService {
	
	public void savePageDetails(HttpServletRequest request,HttpServletResponse response,String page);
	public void closeSession(HttpServletRequest request,HttpServletResponse response,String page) ;

}
