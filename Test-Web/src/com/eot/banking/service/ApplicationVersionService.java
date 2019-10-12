package com.eot.banking.service;

import com.eot.banking.dto.ApplicationVersionDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Version;

public interface ApplicationVersionService {

	public Page getApplicationVersionList(Integer pageNumber);
	public Page getApplicationVersionDetailsList(String versionNumber,Integer pageNumber);
	public void addApplicationVersion(ApplicationVersionDTO versionDTO) throws EOTException;
	public void updateApplicationVersion(ApplicationVersionDTO versionDTO) throws EOTException;
	public ApplicationVersionDTO getApplicationVersion(String versionNumber) throws EOTException;
	public void addApplicationVersionDetails(ApplicationVersionDTO versionDTO) throws EOTException;
	public void updateApplicationVersionDetails(ApplicationVersionDTO versionDTO) throws EOTException;
	public ApplicationVersionDTO getApplicationVersionDetails(Integer versionId) throws EOTException;
	public String getApplicationVersionService();

}
