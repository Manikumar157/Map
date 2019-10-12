package com.eot.banking.dao;

import com.eot.banking.dto.FileUploadDTO;
import com.eot.banking.utils.Page;

public interface FileProcessDao extends BaseDao{
	
	public Page getFileUploadDetails(FileUploadDTO fileUploadDTO, Integer pageNumber);
}
