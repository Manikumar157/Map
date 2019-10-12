package com.eot.banking.dao;

import com.eot.banking.utils.Page;
import com.eot.entity.Version;
import com.eot.entity.VersionDetails;

public interface ApplicationVersionDao extends BaseDao {

	Page getApplicationVersionList(Integer pageNumber);
	Page getApplicationVersionDetailsList(String versionNumber,Integer pageNumber);
	Version getApplicationVersion(String versionNumber);
	Integer getMaxReleaseNumber();
	VersionDetails getApplicationVersionDetails(Integer versionId);
	String getApplicationVersionDao();
	
}
