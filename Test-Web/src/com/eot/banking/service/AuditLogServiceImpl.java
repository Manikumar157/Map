package com.eot.banking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.AuditLogDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.AuditLogDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.WebUser;

@Service("auditLogService")
@Transactional(readOnly=true)
public class AuditLogServiceImpl implements AuditLogService {

	@Autowired
	private WebUserDao webUserDao;
	
	@Autowired
	private AuditLogDao auditLogDao; 
	
	public List<WebUser> getWebUserList(){
		return webUserDao.getWebUserList();
	}
	
	public Page getAuditLogs(AuditLogDTO auditLogDTO,Integer pageNumber) throws EOTException {
		
		Page page = auditLogDao.getAuditLogs(auditLogDTO,pageNumber);		
		if(page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
			throw new EOTException(ErrorConstants.NO_ENTRIES_FOUND);
		
		return page;
		
	}
	
}
