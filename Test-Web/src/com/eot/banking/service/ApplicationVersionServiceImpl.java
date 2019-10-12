package com.eot.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.ApplicationVersionDao;
import com.eot.banking.dto.ApplicationVersionDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Version;
import com.eot.entity.VersionDetails;


@Service("applicationVersionService")
public class ApplicationVersionServiceImpl implements ApplicationVersionService{

	@Autowired
	private ApplicationVersionDao applicationVersionDao;

	@Override
	public Page getApplicationVersionList(Integer pageNumber) {
		return applicationVersionDao.getApplicationVersionList(pageNumber);
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addApplicationVersion(ApplicationVersionDTO versionDTO) throws EOTException {
		
		Version version = applicationVersionDao.getApplicationVersion(versionDTO.getVersionNumber());
		if(version != null){
			throw new EOTException(ErrorConstants.VERSION_NUMBER_EXIST);
		}

		version = new Version();

		Integer releaseNumber =  applicationVersionDao.getMaxReleaseNumber();
		
		version.setVersionNumber(versionDTO.getVersionNumber());
		version.setReleaseNumber(++releaseNumber);
		version.setReleaseDate(versionDTO.getReleaseDate());
		applicationVersionDao.save(version);

	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateApplicationVersion(ApplicationVersionDTO versionDTO) throws EOTException {

		Version version = applicationVersionDao.getApplicationVersion(versionDTO.getVersionNumber());

		version.setReleaseDate(versionDTO.getReleaseDate());
		applicationVersionDao.update(version);

	}

	@Override
	public ApplicationVersionDTO getApplicationVersion(String versionNumber) throws EOTException {

		Version version = applicationVersionDao.getApplicationVersion(versionNumber);

		ApplicationVersionDTO applicationVersionDTO = new ApplicationVersionDTO();

		applicationVersionDTO.setVersionNumber(version.getVersionNumber());
		applicationVersionDTO.setReleaseDate(version.getReleaseDate());
		applicationVersionDTO.setReleaseNumber(version.getReleaseNumber());

		return applicationVersionDTO;
	}

	@Override
	public Page getApplicationVersionDetailsList(String versionNumber,Integer pageNumber) {
		return applicationVersionDao.getApplicationVersionDetailsList(versionNumber, pageNumber);
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addApplicationVersionDetails(ApplicationVersionDTO versionDTO) throws EOTException {
		
		Version version = applicationVersionDao.getApplicationVersion(versionDTO.getVersionNumber().replace(",", ""));
		
		VersionDetails versionDetails = new VersionDetails();
		
		versionDetails.setChannel(versionDTO.getChannel());
		versionDetails.setCurrentVersion(versionDTO.getCurrentVersion());
		versionDetails.setFunctionalityName(versionDTO.getFunctionalityName());
		versionDetails.setModuleName(versionDTO.getModuleName());
		versionDetails.setChangeDescription(versionDTO.getDescription());
		versionDetails.setVersion(version);

		applicationVersionDao.save(versionDetails);

	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateApplicationVersionDetails(ApplicationVersionDTO versionDTO) throws EOTException {
		
		VersionDetails versionDetails = applicationVersionDao.getApplicationVersionDetails(versionDTO.getVersionId());
		
		versionDetails.setChannel(versionDTO.getChannel());
		versionDetails.setCurrentVersion(versionDTO.getCurrentVersion());
		versionDetails.setChangeDescription(versionDTO.getDescription());
		versionDetails.setFunctionalityName(versionDTO.getFunctionalityName());
		versionDetails.setModuleName(versionDTO.getModuleName());
		
		applicationVersionDao.update(versionDetails);

	}

	@Override
	public ApplicationVersionDTO getApplicationVersionDetails(Integer versionId) throws EOTException {
		
		VersionDetails versionDetails = applicationVersionDao.getApplicationVersionDetails(versionId);

		ApplicationVersionDTO applicationVersionDTO = new ApplicationVersionDTO();

		applicationVersionDTO.setVersionId(versionDetails.getVersionId());
		applicationVersionDTO.setChannel(versionDetails.getChannel());
		applicationVersionDTO.setCurrentVersion(versionDetails.getCurrentVersion());
		applicationVersionDTO.setDescription(versionDetails.getChangeDescription());
		applicationVersionDTO.setFunctionalityName(versionDetails.getFunctionalityName());
		applicationVersionDTO.setModuleName(versionDetails.getModuleName());
		applicationVersionDTO.setReleaseDate(versionDetails.getVersion().getReleaseDate());
		applicationVersionDTO.setReleaseNumber(versionDetails.getVersion().getReleaseNumber());

		applicationVersionDTO.setVersionNumber(versionDetails.getVersion().getVersionNumber());
		applicationVersionDTO.setReleaseDate(versionDetails.getVersion().getReleaseDate());
		applicationVersionDTO.setReleaseNumber(versionDetails.getVersion().getReleaseNumber());
		
		return applicationVersionDTO;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public String getApplicationVersionService() {
		String s=applicationVersionDao.getApplicationVersionDao();
		return s;
	}



}
