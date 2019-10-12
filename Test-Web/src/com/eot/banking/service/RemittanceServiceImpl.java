/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: RemittanceServiceImpl.java
*
* Date Author Changes
* 2 Aug, 2016 Swadhin Created
*/
package com.eot.banking.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dto.RemittanceDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.Page;
import com.eot.entity.RemittanceCompaniesTransferType;
import com.eot.entity.RemittanceCompaniesTransferTypePK;
import com.eot.entity.RemittanceCompany;

/**
 * The Class RemittanceServiceImpl.
 */
@Service
public class RemittanceServiceImpl implements RemittanceService {

	/** The operator dao. */
	@Autowired
	private OperatorDao operatorDao;

	/* (non-Javadoc)
	 * @see com.eot.banking.service.RemittanceService#addRemittanceCompany(com.eot.banking.dto.RemittanceDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
	public void addRemittanceCompany(RemittanceDTO remittanceDTO) throws EOTException {

		RemittanceCompany remittanceCompany = operatorDao.getRemittanceCompanyByName(remittanceDTO.getRemittanceCompanyName());
		if(remittanceCompany != null){
			throw new EOTException(ErrorConstants.REMITTANCE_COMPANY_EXIST);
		}
		remittanceCompany = new RemittanceCompany();
		remittanceCompany.setRemittanceCompanyName(remittanceDTO.getRemittanceCompanyName());
		remittanceCompany.setStatus(remittanceDTO.getRemittanceStatus());
		operatorDao.save(remittanceCompany);

		for(int i = 0;i<remittanceDTO.getRemittanceTransferType().length;i++){
			RemittanceCompaniesTransferType remittanceCompaniesTransferType = new RemittanceCompaniesTransferType();
			RemittanceCompaniesTransferTypePK comp_id = new RemittanceCompaniesTransferTypePK();
			comp_id.setRemittanceCompanyId(remittanceCompany.getRemittanceCompanyId());
			comp_id.setTransferTypeId(remittanceDTO.getRemittanceTransferType()[i]);
			remittanceCompaniesTransferType.setComp_id(comp_id);
			remittanceCompaniesTransferType.setRemittanceCompany(remittanceCompany);
			operatorDao.save(remittanceCompaniesTransferType);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.RemittanceService#updateRemittanceCompany(com.eot.banking.dto.RemittanceDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
	public void updateRemittanceCompany(RemittanceDTO remittanceDTO) throws EOTException {

		RemittanceCompany remittanceCompany = operatorDao.getRemittanceCompanyById(remittanceDTO.getRemittanceCompanyId());
		remittanceCompany.setRemittanceCompanyName(remittanceDTO.getRemittanceCompanyName());
		remittanceCompany.setStatus(remittanceDTO.getRemittanceStatus());
		operatorDao.update(remittanceCompany);

		operatorDao.deleteRemittanceCompaniesTransferType(remittanceDTO.getRemittanceCompanyId());
		
		for(int i = 0;i<remittanceDTO.getRemittanceTransferType().length;i++){
			RemittanceCompaniesTransferType remittanceCompaniesTransferType = new RemittanceCompaniesTransferType();
			RemittanceCompaniesTransferTypePK comp_id = new RemittanceCompaniesTransferTypePK();
			comp_id.setRemittanceCompanyId(remittanceCompany.getRemittanceCompanyId());
			comp_id.setTransferTypeId(remittanceDTO.getRemittanceTransferType()[i]);
			remittanceCompaniesTransferType.setComp_id(comp_id);
			remittanceCompaniesTransferType.setRemittanceCompany(remittanceCompany);
			operatorDao.save(remittanceCompaniesTransferType);
		}
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.RemittanceService#getRemittanceCompany(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RemittanceDTO getRemittanceCompany(Integer remittanceCompanyId) throws EOTException {
		RemittanceCompany remittanceCompany = operatorDao.getRemittanceCompanyById(remittanceCompanyId);
		
		RemittanceDTO remittanceDTO = new RemittanceDTO();
		
		remittanceDTO.setRemittanceCompanyId(remittanceCompanyId);
		remittanceDTO.setRemittanceCompanyName(remittanceCompany.getRemittanceCompanyName());
		remittanceDTO.setRemittanceStatus(remittanceCompany.getStatus());
		
		Set<RemittanceCompaniesTransferType> companiesTransferTypes = remittanceCompany.getRemittanceCompaniesTransferTypes();
		
		if(companiesTransferTypes.size() != 0){
			Integer[] remittanceTransferType = new Integer[companiesTransferTypes.size()];
			int i=0;
			for(RemittanceCompaniesTransferType remittanceCompaniesTransferType : companiesTransferTypes){
				remittanceTransferType[i] = remittanceCompaniesTransferType.getComp_id().getTransferTypeId();
				i++;
			}
			remittanceDTO.setRemittanceTransferType(remittanceTransferType);
		}
		return remittanceDTO;
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.RemittanceService#getAllRemittanceCompanies(java.lang.Integer)
	 */
	@Override
	public Page getAllRemittanceCompanies(Integer pageNumber) {
		return operatorDao.getAllRemittanceCompanies(pageNumber);
	}
}
