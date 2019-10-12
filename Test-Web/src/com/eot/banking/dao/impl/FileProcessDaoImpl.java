package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eot.banking.dao.FileProcessDao;
import com.eot.banking.dto.FileUploadDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.FileUploadDetail;

public class FileProcessDaoImpl extends BaseDaoImpl implements FileProcessDao {
	
	@Override
	public Page getFileUploadDetails(FileUploadDTO fileUploadDTO, Integer pageNumber) {
        DetachedCriteria fileUploadDetail = DetachedCriteria.forClass(FileUploadDetail.class);
        
        String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
        
		fileUploadDetail.add(Restrictions.eq("userId", userName));

		fileUploadDetail.addOrder(Order.desc("createdDate"));
		
		if(null != fileUploadDTO.getFromDate()){
			fileUploadDetail.add(Restrictions.sqlRestriction("this_.created_date>='"+DateUtil.formatDate(fileUploadDTO.getFromDate())+" 00:00:00'"));
		}
		if(null != fileUploadDTO.getToDate()){
			fileUploadDetail.add(Restrictions.sqlRestriction("this_.created_date<='"+DateUtil.formatDate(fileUploadDTO.getToDate())+" 23:59:59'"));
		}
		if (null != fileUploadDTO.getFailedFileName()) {
			if(!fileUploadDTO.getFailedFileName().equals("")){
			fileUploadDetail.add(Restrictions.sqlRestriction("file_name like '"+fileUploadDTO.getFailedFileName() +"%'"));
			}
		}
		List<FileUploadDetail> list = getHibernateTemplate().findByCriteria(fileUploadDetail);
		return PaginationHelper.getPage(list, list.size(), appConfig.getResultsPerPage(), pageNumber);
	}

}
