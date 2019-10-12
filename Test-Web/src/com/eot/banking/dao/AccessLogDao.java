package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.utils.Page;
import com.eot.entity.WebUser;

public interface AccessLogDao {

	List<WebUser> getWebUserList();	

	Page sessionLog(int pageNumber);

	Page getVisitedPage(String sessionId, Integer pageNumber);

	Page searchLog(String userId, String fromDate, String toDate,
			Integer pageNumber);

}
