package com.eot.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eot.banking.dao.FileProcessDao;
import com.eot.banking.dto.FileUploadDTO;
import com.eot.banking.utils.Page;

@Service
public class FileProcessServiceImpl implements FileProcessService {
	
	@Autowired
	private FileProcessDao fileProcessDao;

	@Override
	public Page getFailFileDetailList(FileUploadDTO fileUploadDTO, int pageNumber) {
		
		Page page = fileProcessDao.getFileUploadDetails(fileUploadDTO, pageNumber);
		return  page ;
	}

}
