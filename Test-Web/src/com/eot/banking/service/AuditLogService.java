package com.eot.banking.service;

import java.util.List;

import com.eot.banking.dto.AuditLogDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.WebUser;

public interface AuditLogService {
	
	List<WebUser> getWebUserList();
	Page getAuditLogs(AuditLogDTO auditLogDTO,Integer pageNumber) throws EOTException;
	
}
