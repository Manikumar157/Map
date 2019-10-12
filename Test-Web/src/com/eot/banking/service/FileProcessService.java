package com.eot.banking.service;

import com.eot.banking.dto.FileUploadDTO;
import com.eot.banking.utils.Page;

public interface FileProcessService {
	
	public Page getFailFileDetailList(FileUploadDTO fileUploadDTO, int pageNumber);
}
