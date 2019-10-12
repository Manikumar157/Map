package com.eot.banking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eot.banking.dao.AccessLogDao;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.WebUser;


@Service("accessLogService")
public class AccessLogServiceImpl implements AccessLogService{
	
	
	@Autowired
	private AccessLogDao accessLogDao;	

	@Override
	public List<WebUser> getRoleList() {	
		return accessLogDao.getWebUserList();
	}
	
	@Override
	public Page getVisitedPage(String sessionId,Integer pageNumber) throws EOTException {		
		return accessLogDao.getVisitedPage(sessionId,pageNumber);
	}

	@Override
	public Page getSessionList(String userId, String fromDate,String toDate,Integer pageNumber) throws EOTException {
		Page page = null;
		page=accessLogDao.searchLog(userId,fromDate,toDate,pageNumber);
		
		if(page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
			throw new EOTException(ErrorConstants.NODATA_AVAILABLE);	
		
		return page;
	}
}
