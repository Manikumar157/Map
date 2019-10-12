package com.eot.banking.service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.StakeHolderDao;
import com.eot.banking.dto.StakeHolderDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.Country;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.StakeHolder;

@Service("stakeholderService")
@Transactional(readOnly=true)
public class StakeHolderServiceImpl implements StakeHolderService {
	
	@Autowired
	private StakeHolderDao stakeHolderDao;	
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private BankDao bankDao;
	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private CustomerDao customerDao;
	@Override	
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void addStakeHolder(StakeHolderDTO stakeHolderDTO)	throws EOTException {		
		StakeHolder stakeHolder=stakeHolderDao.getStakeHolderByName(stakeHolderDTO.getStakeholderOrganization());
		if(stakeHolder!=null){
			throw new EOTException(ErrorConstants.STAKEHOLDER_NAME_EXIST);
		}
		if(stakeHolderDTO.getContactMobile()!=null && stakeHolderDTO.getContactMobile().trim()!="") {
			StakeHolder stakeHolder1=stakeHolderDao.getStakeHolderByMobileNumber(stakeHolderDTO.getContactMobile());
			if(stakeHolder1!=null){
				throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
			}
		}
		stakeHolder=new StakeHolder();
		stakeHolder.setStakeholderOrganization(stakeHolderDTO.getStakeholderOrganization());
		stakeHolder.setStakeholderId(stakeHolderDTO.getStakeholderId());
		stakeHolder.setAddress(stakeHolderDTO.getAddress());
		stakeHolder.setContactPersonName(stakeHolderDTO.getContactPersonName());
		stakeHolder.setContactAddress(stakeHolderDTO.getContactAddress());
		stakeHolder.setContactMobile(stakeHolderDTO.getContactMobile());
		stakeHolder.setContactPhone(stakeHolderDTO.getContactPhone());
		stakeHolder.setEmailAddress(stakeHolderDTO.getEmailAddress());		
		
		Country country=new Country();
		country.setCountryId(stakeHolderDTO.getCountryId());
		stakeHolder.setCountry(country);		
		
		Account account = new Account();
		
		account.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
		account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		account.setAlias(EOTConstants.ACCOUNT_ALIAS_STAKE_HOLDER+"-"+stakeHolderDTO.getStakeholderOrganization());
		account.setCurrentBalance(0.0);
		account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		account.setReferenceType(EOTConstants.REFERENCE_TYPE_STAKE_HOLDER);
		account.setReferenceId(" ");
		
		stakeHolderDao.save(account);
		
		stakeHolder.setAccount(account); 
		
		stakeHolderDao.save(stakeHolder);
		
		account.setReferenceId(stakeHolder.getStakeholderId().toString());
		stakeHolderDao.update(account);
		
	}

	@Override
	
	public StakeHolderDTO getStakeHolderDetails(Integer stakeholderId) throws EOTException{			
		StakeHolder stakeHolder=stakeHolderDao.getStakeHolderDetails(stakeholderId);
		
		if(stakeHolder == null){
			throw new EOTException(ErrorConstants.INVALID_STAKE_HOLDER);
		}
		StakeHolderDTO stakeHolderDTO= new StakeHolderDTO();	
		stakeHolderDTO.setStakeholderId(stakeHolder.getStakeholderId());
		stakeHolderDTO.setStakeholderOrganization(stakeHolder.getStakeholderOrganization());
		stakeHolderDTO.setAddress(stakeHolder.getAddress());
		stakeHolderDTO.setContactPersonName(stakeHolder.getContactPersonName());
		stakeHolderDTO.setContactAddress(stakeHolder.getContactAddress());
		stakeHolderDTO.setContactPhone(stakeHolder.getContactPhone());
		stakeHolderDTO.setContactMobile(stakeHolder.getContactMobile());
		stakeHolderDTO.setEmailAddress(stakeHolder.getEmailAddress());
		stakeHolderDTO.setCountryId(stakeHolder.getCountry().getCountryId());
		stakeHolderDTO.setAccountNumber(stakeHolder.getAccount().getAccountNumber());		
		return stakeHolderDTO;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateStakeHolder(StakeHolderDTO stakeHolderDTO)throws EOTException {
		
		StakeHolder stakeHolder=stakeHolderDao.getStakeHolderByName(stakeHolderDTO.getStakeholderOrganization(),stakeHolderDTO.getStakeholderId());
		if(stakeHolder!=null){
			throw new EOTException(ErrorConstants.STAKEHOLDER_NAME_EXIST);
		}
		if(stakeHolderDTO.getContactMobile()!=null && stakeHolderDTO.getContactMobile().trim()!="") {
			StakeHolder stakeHolder1 = stakeHolderDao.getUpdateStakeHolderByMobileNumber(stakeHolderDTO);
			if(stakeHolder1==null){
				StakeHolder stakeHolder2=stakeHolderDao.getStakeHolderByMobileNumber(stakeHolderDTO.getContactMobile());
				if(stakeHolder2!=null){
					throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
				}
			}
		}
		stakeHolder=stakeHolderDao.getStakeHolderDetails(stakeHolderDTO.getStakeholderId());
		
		if(stakeHolder == null){
			throw new EOTException(ErrorConstants.INVALID_STAKE_HOLDER);
		}
		stakeHolder.setStakeholderId(stakeHolderDTO.getStakeholderId());
		stakeHolder.setStakeholderOrganization(stakeHolderDTO.getStakeholderOrganization());
		stakeHolder.setAddress(stakeHolderDTO.getAddress());
		stakeHolder.setContactPersonName(stakeHolderDTO.getContactPersonName());
		stakeHolder.setContactAddress(stakeHolderDTO.getContactAddress());
		stakeHolder.setContactPhone(stakeHolderDTO.getContactPhone());
		stakeHolder.setContactMobile(stakeHolderDTO.getContactMobile());
		stakeHolder.setEmailAddress(stakeHolderDTO.getEmailAddress());
		stakeHolder.setAccount(stakeHolder.getAccount());
		
		Country country=new Country();
		country.setCountryId(stakeHolderDTO.getCountryId());
		stakeHolder.setCountry(country);
		stakeHolderDao.updateStakeHolder(stakeHolder);		
		}

	@Override
	public Map<String, List> getMasterData(String language) throws EOTException {
		Map<String,List> model = new HashMap<String,List>();
		model.put("countryList",operatorDao.getCountries(language));
	    model.put("stakeHolderList",stakeHolderDao.getStakeHolders());	
	    
		return model;
	}

	@Override
	public Page getStakeHolderList(Integer pageNumber) throws EOTException {
		return stakeHolderDao.getStakeHolderList(pageNumber);
		
	}

	@Override
	public Integer getMobileNumLength(Integer countryId) throws EOTException{
		try{
			return customerDao.getMobileNumLength(countryId);
		}catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

}
