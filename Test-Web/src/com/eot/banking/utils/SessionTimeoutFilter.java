package com.eot.banking.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionTimeoutFilter implements Filter {


	 private String timeoutPage = "sessionTimeOut.jsp";

	 public void init(FilterConfig filterConfig) throws ServletException {
		 System.out.println("filter is calling ");
		 String testParam= filterConfig.getInitParameter("logoutPage");
		
         
	        //Print the init parameter
	        System.out.println("Test Param: " + testParam);
	 }

	 public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
	   ServletException {

	  if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
	   HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	   HttpServletResponse httpServletResponse = (HttpServletResponse) response;
	   
	  
	   // is session expire control required for this request?
	   if (isSessionControlRequiredForThisResource(httpServletRequest)) {
	    
	    // is session invalid?
	    if (isSessionInvalid(httpServletRequest)) {
	     String timeoutUrl = httpServletRequest.getContextPath() + "/" + getTimeoutPage();
	     httpServletResponse.sendRedirect(timeoutUrl);
	     //httpServletRequest.getRequestDispatcher("timeoutUrl").forward(httpServletRequest, httpServletResponse);
	     return;
	    }
	   }
	  }
	  filterChain.doFilter(request, response);
	 }

	 /**
	  * 
	  * session shouldn't be checked for some pages. For example: for timeout page..
	  * Since we're redirecting to timeout page from this filter,
	  * if we don't disable session control for it, filter will again redirect to it
	  * and this will be result with an infinite loop... 
	  */
	 private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
	  String requestPath = httpServletRequest.getRequestURI();
	  
	  boolean controlRequired = true;//!StringUtils.contains(requestPath, getTimeoutPage());
	  
	  return controlRequired;
	 }

	 private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
	  boolean sessionInValid = (httpServletRequest.getRequestedSessionId() != null)
	    && !httpServletRequest.isRequestedSessionIdValid();
	  return sessionInValid;
	 }

	 public void destroy() {
		 System.out.println("session destroyed");
	 }

	 public String getTimeoutPage() {
	  return timeoutPage;
	 }

	 public void setTimeoutPage(String timeoutPage) {
	  this.timeoutPage = timeoutPage;
	 }
	 
	}
