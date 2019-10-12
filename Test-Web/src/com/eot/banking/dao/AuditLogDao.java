package com.eot.banking.dao;

import com.eot.banking.dto.AuditLogDTO;
import com.eot.banking.utils.Page;

public interface AuditLogDao extends BaseDao{
	
	Page getAuditLogs(AuditLogDTO auditLogDTO,Integer pageNumber);

}
