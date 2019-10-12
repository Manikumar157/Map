package com.eot.banking.service;

import java.util.List;

import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.WebUser;

public interface AccessLogService {	

	List<WebUser> getRoleList();	

	Page getVisitedPage(String sessionId,Integer pageNumber) throws EOTException;

	Page getSessionList(String userId, String fromDate, String toDate,Integer pageNumber) throws EOTException;

}
