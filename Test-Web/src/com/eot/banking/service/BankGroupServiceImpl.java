package com.eot.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BankGroupDao;
import com.eot.banking.dto.BankGroupDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.BankGroup;


@Service("bankGroupService")

public class BankGroupServiceImpl implements BankGroupService{

	@Autowired
	private BankGroupDao bankGroupDao;

	@Override
	public Page getBankGroupList(Integer pageNumber) {		
		return bankGroupDao.getBankGroupList(pageNumber);
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addBankGroup(BankGroupDTO bankGroupDTO)throws EOTException {		
		BankGroup bankGroup= bankGroupDao.getBankGroupByName(bankGroupDTO.getBankGroupName());
		if(bankGroup!=null){
			throw new EOTException(ErrorConstants.BANKGROUP_NAME_EXIST);			
		}
		// change vineeth, 16-07-2018, bugno: 5696, Bank Group Short Name should be unique.
		BankGroup bankGroupShortName= bankGroupDao.getBankGroupShortName(bankGroupDTO.getBankGroupShortName());
		if(bankGroupShortName!=null){
			throw new EOTException(ErrorConstants.BANKGROUPSHORT_NAME_EXIST);			
		}
		// change over vineeth
		bankGroup=new BankGroup();
		bankGroup.setBankGroupName(bankGroupDTO.getBankGroupName());
		bankGroup.setBankGroupShortName(bankGroupDTO.getBankGroupShortName().toUpperCase());
		bankGroupDao.save(bankGroup);
	}


	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateBankGroup(BankGroupDTO bankGroupDTO)throws EOTException {
		BankGroup bankGroup= bankGroupDao.getBankGroupByName(bankGroupDTO.getBankGroupName(),bankGroupDTO.getBankGroupId());
		if(bankGroup!=null){
			throw new EOTException(ErrorConstants.BANKGROUP_NAME_EXIST);			
		}
		// change vineeth, 14-08-2018, bugno: 5696, Bank Group Short Name should be unique.
				BankGroup bankGroupShortName= bankGroupDao.getBankGroupShortName(bankGroupDTO.getBankGroupShortName());
				if(bankGroupShortName!=null){
					throw new EOTException(ErrorConstants.BANKGROUPSHORT_NAME_EXIST);			
				}
				// change over vineeth
		bankGroup=bankGroupDao.getBankGroup(bankGroupDTO.getBankGroupId());	
		if(bankGroup==null){
			throw new EOTException(ErrorConstants.INVALID_BANKGROUP);
		}else{
			bankGroup.setBankGroupName(bankGroupDTO.getBankGroupName());
			bankGroup.setBankGroupShortName(bankGroupDTO.getBankGroupShortName().toUpperCase());
			bankGroupDao.update(bankGroup);
		}
	}

	@Override
	public BankGroupDTO getBankGroup(Integer bankGroupId) throws EOTException {

		BankGroup bankGroup=bankGroupDao.getBankGroup(bankGroupId);
		if(bankGroup==null){
			throw new EOTException(ErrorConstants.INVALID_BANKGROUP);
		}else{		
			BankGroupDTO bankGroupDTO=new BankGroupDTO();
			bankGroupDTO.setBankGroupId(bankGroup.getBankGroupId());
			bankGroupDTO.setBankGroupName(bankGroup.getBankGroupName());
			bankGroupDTO.setBankGroupShortName(bankGroup.getBankGroupShortName());	

			return bankGroupDTO;
		}
	}

	@Override
	public Page getBank(Integer bankGroupId,Integer pageNumber) {	
		return bankGroupDao.getBank(bankGroupId, pageNumber);
	}

}
