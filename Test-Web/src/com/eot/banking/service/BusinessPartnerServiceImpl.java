/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: BusinessPartnerServiceImpl.java
*
* Date Author Changes
* Oct 16, 2018 Vinod Joshi Created
*/
package com.eot.banking.service;

import java.io.EOFException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.City;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerDocument;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.CustomerSecurityAnswer;
import com.eot.entity.KycType;
import com.eot.entity.Quarter;
import com.eot.entity.SecurityQuestion;
import com.eot.entity.WebUser;
import com.logica.smpp.Data;

/**
 * The Class BusinessPartnerServiceImpl.
 */
@Service("BusinessPartnerService")
@Transactional(readOnly=true)
public class BusinessPartnerServiceImpl implements BusinessPartnerService{

	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;
	
	/** The location dao. */
	@Autowired
	private LocationDao locationDao ;
	
	@Autowired
	private CustomerDao customerDao;
	
	/** The business partner dao. */
	@Autowired
	private BusinessPartnerDao businessPartnerDao;
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#saveBusinessPartner(com.eot.banking.dto.BusinessPartnerDTO, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void saveBusinessPartner(BusinessPartnerDTO businessPartnerDTO,Integer SeniorID, Integer BankID) throws Exception {
		
		
		BusinessPartner businessPartner = businessPartnerDao.getBusinessPartnerByContactNumber(businessPartnerDTO.getContactNumber());
		if(businessPartner!=null){
			throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
		}
		
	/*	businessPartner = businessPartnerDao.getBusinessPartnerByName(businessPartnerDTO.getName());
		 if( businessPartner != null ){
			throw new EOTException(ErrorConstants.BUSINESS_PARTNER_NAME_REGISTERED_ALREADY);
		}*/
		
	/*	if(!businessPartnerDTO.getCode().isEmpty()){
			businessPartner  = businessPartnerDao.getBusinessPartnerByCode(businessPartnerDTO.getCode());
			if(businessPartner != null){
				throw new EOTException(ErrorConstants.BP_CODE_ALREADY_EXISTS);
			}
		}*/
		
		/*if(!businessPartnerDTO.getEmailId().isEmpty()) {
			businessPartner = businessPartnerDao.getBusinessPartnerByEmailAddress(businessPartnerDTO.getEmailId());
			if(businessPartner != null) {
				throw new EOTException(ErrorConstants.EMAIL_ALREADY_EXISTS);
			}
		}*/
		businessPartner = new BusinessPartner();
		businessPartner.setSeniorPartner(SeniorID);
		businessPartner.setName(businessPartnerDTO.getName());
		businessPartner.setCode(businessPartnerDTO.getCode());
		businessPartner.setContactNumber(businessPartnerDTO.getOrganizationNumber());
		businessPartner.setEmailId(businessPartnerDTO.getOrganizationEmailId());
		businessPartner.setContactPerson(businessPartnerDTO.getContactPerson());
		businessPartner.setPartnerType(businessPartnerDTO.getPartnerType());
		businessPartner.setContactPersonPhone(businessPartnerDTO.getContactNumber());
		businessPartner.setContactPersonEmail(businessPartnerDTO.getEmailId());
		businessPartner.setContactPersonAddress(businessPartnerDTO.getAddress());
		businessPartner.setCommissionPercent(businessPartnerDTO.getCommissionPercent());
		businessPartner.setAddress(businessPartnerDTO.getOrganizationAddress());
		businessPartner.setKycIdNumber(businessPartnerDTO.getKycIdNumber());
		Country country = locationDao.getCountry(EOTConstants.DEFAULT_COUNTRY);
		businessPartner.setCountry(country);
	/*	businessPartner.setPartnerType(businessPartner.getPartnerType());*/
		BigDecimal bd = businessPartnerDTO.getBusinessEntityLimit();
		double businessentitylimit = bd.doubleValue();
		businessPartner.setBusinessEntityLimit(businessentitylimit);
		businessPartner.setKycImage(Hibernate.createBlob(businessPartnerDTO.getKycImage().getBytes()));
		businessPartner.setCreatedDate(new Date());
		businessPartner.setUpdatedDate(new Date());
		businessPartner.setStatus(1);
		
		String businessPartnerCode = customerDao.getBusinessPartnerCode();		
		Integer numericCode=0;
		numericCode = businessPartnerCode.length()>=4 ? Integer.parseInt(businessPartnerCode)+1:Integer.parseInt(businessPartnerCode)+1000;
		businessPartnerCode=numericCode.toString();
		businessPartnerCode = ISOUtil.zeropad(businessPartnerCode, 4);
		businessPartner.setCode(businessPartnerCode);
	
		Bank bank = new Bank();
		bank.setBankId(BankID);
		int kycid = Integer.parseInt(businessPartnerDTO.getKycTypeId());
		KycType kycType = businessPartnerDao.getkycbyId(kycid);
		kycType.setKycTypeId(kycid);
		businessPartner.setKycType(kycType);
		businessPartner.setBank(bank);
		businessPartner.setDesignation(businessPartnerDTO.getDesignation());
		businessPartnerDao.save(businessPartner);
		
		businessPartner=businessPartnerDao.getBusinessPartnerByCode(businessPartnerCode);
		if(businessPartner==null)
		{
			throw new EOTException(ErrorConstants.PROBLEM_LOADING_BUSINESS_PARTNER);
		}
		
		long accountSeq=bankDao.getNextAccountNumberSequence();
      		
		Account account = new Account();
		String businessPartnerAccount =EOTUtil.generateAccountNumber(accountSeq);
		
		account.setAccountNumber(businessPartnerAccount);
		account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		String alias = "BUSINESS_PARTNER_ACC" ;
		account.setAlias(alias);
		account.setCurrentBalance(0.0);
		account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		account.setReferenceType(EOTConstants.REFRENCE_TYPE_BUSINESSPARTNER);
		account.setReferenceId(businessPartner.getId()+"");
		account.setAliasType(EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT);
		businessPartnerDao.save(account);
		
		Account businessPartnerCommissionAC = new Account();
		String businessPartnerCommissionAccount =EOTUtil.generateAccountNumber(accountSeq+1);
		
		businessPartnerCommissionAC.setAccountNumber(businessPartnerCommissionAccount);
		businessPartnerCommissionAC.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		businessPartnerCommissionAC.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		businessPartnerCommissionAC.setAlias(EOTConstants.BUSINESS_PARTNER_COMMISSION_ACCOUNT_ALIAS);
		businessPartnerCommissionAC.setAliasType(EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);
		businessPartnerCommissionAC.setCurrentBalance(0.0);
		businessPartnerCommissionAC.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		businessPartnerCommissionAC.setReferenceType(EOTConstants.REFRENCE_TYPE_BUSINESSPARTNER);
		businessPartnerCommissionAC.setReferenceId(businessPartner.getId()+"");
		businessPartnerDao.save(businessPartnerCommissionAC);
		
//		businessPartnerDao.saveOrUpdate(businessPartner);
		businessPartner.setCommissionAccount(businessPartnerCommissionAccount);
		businessPartner.setAccountNumber(businessPartnerAccount);
		businessPartnerDao.update(businessPartner);
		
		businessPartner.setAccountNumber(account.getAccountNumber());
		
		
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#getBusinessPartnerUser(java.lang.String)
	 */
	@Override
	public BusinessPartnerUser getBusinessPartnerUser(String userName) {
		
		BusinessPartnerUser businessPartnerUser= businessPartnerDao.getUser(userName);
		return businessPartnerUser;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#searchBusinessPartners(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Page searchBusinessPartners(String name, String contactPerson, String contactNumber, String code, String userName, Integer pageNumber, String fromDate, String toDate, String partnerType, String seniorPartner, Integer bankId) {	

		Page page=businessPartnerDao.searchBusinessPartner(name,contactPerson,contactNumber,code,pageNumber,fromDate,toDate,partnerType,seniorPartner,bankId);

		return page;
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#getBusinessPartnerDetails(java.lang.Long, java.lang.String)
	 */
	@Override
	public BusinessPartnerDTO getBusinessPartnerDetails(Long id,String userName) throws EOTException {

		BusinessPartner businessPartner=businessPartnerDao.getBusinessPartner(id);
		
		if(businessPartner==null){
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		BusinessPartnerDTO cdto=new BusinessPartnerDTO();

		cdto.setId(businessPartner.getId());
		cdto.setName(businessPartner.getName());
		cdto.setOrganizationNumber(businessPartner.getContactNumber());
		cdto.setOrganizationEmailId(businessPartner.getEmailId());
		cdto.setOrganizationAddress(businessPartner.getAddress());
		cdto.setCode(businessPartner.getCode());
		cdto.setCreatedDate(businessPartner.getCreatedDate());
		cdto.setUpdatedDate(businessPartner.getUpdatedDate());
		cdto.setName(businessPartner.getName());
		cdto.setContactNumber(businessPartner.getContactPersonPhone());
		cdto.setPartnerType(businessPartner.getPartnerType());
		cdto.setEmailId(businessPartner.getContactPersonEmail());
		cdto.setCommissionPercent(businessPartner.getCommissionPercent());
		cdto.setStatus(businessPartner.getStatus());
		cdto.setSeniorPartner(businessPartner.getSeniorPartner());
		cdto.setCountry(businessPartner.getCountry());
		cdto.setCurrency(businessPartner.getCurrency());
		cdto.setContactPerson(businessPartner.getContactPerson());	
		cdto.setAddress(businessPartner.getContactPersonAddress());
		cdto.setCommissionPercent(businessPartner.getCommissionPercent());
		if(businessPartner.getBusinessEntityLimit()!=null && businessPartner.getKycIdNumber()!=null && 
				businessPartner.getKycType().getKycTypeId().toString()!=null){
		double bd = businessPartner.getBusinessEntityLimit();
		BigDecimal businessentitylimit = new BigDecimal(bd);
		cdto.setBusinessEntityLimit(businessentitylimit);
		cdto.setKycIdNumber(businessPartner.getKycIdNumber());
		cdto.setKycTypeId(businessPartner.getKycType().getKycTypeId().toString());
		cdto.setDesignation(businessPartner.getDesignation());
		}
		return cdto;
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#updateBusinessPartner(com.eot.banking.dto.BusinessPartnerDTO, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateBusinessPartner(BusinessPartnerDTO businessPartnerDTO, Integer seniorID,Integer BankID) throws Exception {
		
		BusinessPartner businessPartner=businessPartnerDao.getBusinessPartner(Long.parseLong(businessPartnerDTO.getId().toString()));

		if(businessPartner==null){
			throw new EOTException(ErrorConstants.INVALID_CUSTOMER);
		}
		
		// changing country to default country SouthSudan as discussed with sanjeeb. on 22-11-2018 by vineeth
		//	Country country = locationDao.getCountry(businessPartnerDTO.getCountry().getCountryId());
			Country country=locationDao.getCountry(EOTConstants.DEFAULT_COUNTRY); 
			BusinessPartner businessPartner1 = null;
			if( ! businessPartner.getContactNumber().equals(businessPartnerDTO.getContactNumber())){
				businessPartner1 = businessPartnerDao.getBusinessPartnerByMobileNumber(country.getIsdCode()+businessPartnerDTO.getContactNumber());
				if( businessPartner1 != null ){
					throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
				}
			}
			/*if( ! businessPartner.getName().equals(businessPartnerDTO.getName())){
				businessPartner1 = businessPartnerDao.getBusinessPartnerByName(businessPartnerDTO.getName());
				 if( businessPartner1 != null ){
						throw new EOTException(ErrorConstants.BUSINESS_PARTNER_NAME_REGISTERED_ALREADY);
					}
			}*/
			
			
		businessPartner.setId(businessPartnerDTO.getId());
		businessPartner.setName(businessPartnerDTO.getName());
//		businessPartner.setSeniorPartner(businessPartnerDTO.getSeniorPartner());  
		businessPartner.setSeniorPartner(seniorID);
		businessPartner.setCode(businessPartnerDTO.getCode());
		businessPartner.setContactNumber(businessPartnerDTO.getOrganizationNumber());
		businessPartner.setEmailId(businessPartnerDTO.getOrganizationEmailId());
		businessPartner.setContactPerson(businessPartnerDTO.getContactPerson());
		businessPartner.setPartnerType(businessPartnerDTO.getPartnerType());
		businessPartner.setContactPersonPhone(businessPartnerDTO.getContactNumber());
		businessPartner.setContactPersonEmail(businessPartnerDTO.getEmailId());
		businessPartner.setCommissionPercent(businessPartnerDTO.getCommissionPercent());
		//businessPartner.setContactPersonAddress(businessPartnerDTO.getAddress());
		businessPartner.setAddress(businessPartnerDTO.getOrganizationAddress());
		//businessPartner.setContactPersonAddress(businessPartnerDTO.getOrganizationAddress());
		businessPartner.setPartnerType(businessPartnerDTO.getPartnerType());
		businessPartner.setKycIdNumber(businessPartnerDTO.getKycIdNumber());
		BigDecimal bd = businessPartnerDTO.getBusinessEntityLimit();
		double businessentitylimit = bd.doubleValue();
		businessPartner.setBusinessEntityLimit(businessentitylimit);
		businessPartner.setKycImage(Hibernate.createBlob(businessPartnerDTO.getKycImage().getBytes()));
		businessPartner.setCountry(country);
	//	businessPartner.setCreatedDate(businessPartnerDTO.getCreatedDate());
		businessPartner.setUpdatedDate(new Date());
		businessPartner.setStatus(1);
		Bank bank = new Bank();
		bank.setBankId(BankID);
		int kycid = Integer.parseInt(businessPartnerDTO.getKycTypeId());
		KycType kycType = businessPartnerDao.getkycbyId(kycid);
		kycType.setKycTypeId(kycid);
		businessPartner.setKycType(kycType);
		businessPartner.setBank(bank);
		businessPartner.setDesignation(businessPartnerDTO.getDesignation());
	//	businessPartner.setStatus(businessPartnerDTO.getStatus());
	//	businessPartner.setCountry(businessPartnerDTO.getCountry());
	//	businessPartner.setAccountNumber(businessPartnerDTO.getAccountNumber());
	//	businessPartner.setCurrency(businessPartnerDTO.getCurrency());
		businessPartnerDao.saveOrUpdate(businessPartner);

	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#getKycList()
	 */
	@Override
	public List<KycType> getKycList() {
		
		return businessPartnerDao.getKycList();
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.BusinessPartnerService#getPhotoDetails(java.lang.Long, java.lang.String)
	 */
	@Override
	public byte[] getPhotoDetails(Long Businesspartnerid, String type) throws EOTException {
		BusinessPartner businessPartner = businessPartnerDao.getBusinessPartner(Businesspartnerid);
		byte[] kycImage= null;
		if(businessPartner == null){
			throw new EOTException(ErrorConstants.INVALID_BUSINESS_PARTNER);
		}
		else{
			 kycImage= null;
			
			try{
				if(null!=businessPartner.getKycImage())
					kycImage = businessPartner.getKycImage().getBytes(1, (int) businessPartner.getKycImage().length());
			}catch (SQLException e) {
				e.printStackTrace();
				throw new EOTException(ErrorConstants.SERVICE_ERROR);
			}
			
		}
		return kycImage;
		
		
	}

	@Override
	public Page getAllBusinessPartner(int pageNumber) {
		
		return businessPartnerDao.getAllBusniessPartner(pageNumber);
	}

	@Override
	public BusinessPartner loadBusinessPartnerByCode(String businessPartnerCode) {
		return businessPartnerDao.getBusinessPartnerByCode(businessPartnerCode);
	}

	@Override
	public BusinessPartner getBusinessPartner(String userName){
		BusinessPartnerUser businessPartnerUser = businessPartnerDao.getBusinessPartnerUserByName(userName);
		BusinessPartner bp = businessPartnerUser.getBusinessPartner();
		//System.out.println(bp.getName());
		return bp;
	}


	

	
	
}
