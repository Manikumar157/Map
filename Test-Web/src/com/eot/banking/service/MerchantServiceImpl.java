/* Copyright � EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: MerchantServiceImpl.java
 *
 * Date Author Changes
 * 23 May, 2016 Swadhin Created
 */
package com.eot.banking.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.MerchantDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.MerchantDTO;
import com.eot.banking.dto.OutletDTO;
import com.eot.banking.dto.TerminalDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.HashUtil;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.City;
import com.eot.entity.Country;
import com.eot.entity.Merchant;
import com.eot.entity.MerchantCategory;
import com.eot.entity.Outlet;
import com.eot.entity.Quarter;
import com.eot.entity.Terminal;
import com.eot.entity.WebUser;

/**
 * The Class MerchantServiceImpl.
 */
@Service("merchantService")
@Transactional(readOnly=true)
public class MerchantServiceImpl implements MerchantService {

	/** The web user dao. */
	@Autowired
	private WebUserDao webUserDao;

	/** The merchant dao. */
	@Autowired
	private MerchantDao merchantDao;
	
	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#searchMerchant(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Merchant> searchMerchant(String userName, String bankGroupId,
			String merchantName, String mobileNumber, String bankId,
			String branchId, String countryId) throws EOTException {
		List<Merchant> merchants= null;
		Integer groupId = null;
		if(bankGroupId != null && bankGroupId !=""){
			groupId = Integer.parseInt(bankGroupId);
		}
		WebUser webUser = webUserDao.getUser(userName);

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN  || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || 
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS ||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			if(teller==null){
				throw new EOTException(ErrorConstants.INVALID_USER);
			}

			merchants = merchantDao.searchMerchant(null,null, merchantName, mobileNumber,branchId,countryId);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			if(teller != null){
				merchants = merchantDao.searchMerchant(null,null, merchantName, mobileNumber,branchId,countryId);
			}
		}else if(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN){
			BankGroupAdmin bankGroupAdmin=webUserDao.getbankGroupAdmin(webUser.getUserName());

			merchants = merchantDao.searchMerchant(null,bankGroupAdmin.getBankGroup().getBankGroupId(), merchantName, mobileNumber,branchId,countryId);
		}else {
			merchants = merchantDao.searchMerchant(null,groupId, merchantName, mobileNumber,branchId,countryId);
		}
		if(merchants.size() == 0 || merchants.isEmpty() || merchants.equals(""))
			throw new EOTException(ErrorConstants.INVALID_MERCHANT);

		return merchants;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#saveMerchant(com.eot.banking.dto.MerchantDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public Integer saveMerchant(MerchantDTO merchantDTO) throws EOTException {
		Merchant merchant = merchantDao.getMerchantByMobileNumber(merchantDTO.getPrimaryContactMobile());
		if( merchant != null ){
			throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
		}
		merchant = new Merchant();

		merchant.setActive(merchantDTO.getActive());
		merchant.setAddress(merchantDTO.getAddress());
		merchant.setAlternateContactAddress(merchantDTO.getAlternateContactAddress());
		merchant.setAlternateContactMobile(merchantDTO.getAlternateContactMobile());
		merchant.setAlternateContactName(merchantDTO.getAlternateContactName());
		merchant.setAlternateContactPhone(merchantDTO.getAlternateContactPhone());
		merchant.setAlternateeMailAddress(merchantDTO.getAlternateeMailAddress());
		merchant.setMerchantName(merchantDTO.getMerchantName());
		merchant.setPrimaryContactAddress(merchantDTO.getPrimaryContactAddress());
		merchant.setPrimaryContactMobile(merchantDTO.getPrimaryContactMobile());
		merchant.setPrimaryContactName(merchantDTO.getPrimaryContactName());
		merchant.setPrimaryContactPhone(merchantDTO.getPrimaryContactPhone());
		merchant.setPrimaryeMailAddress(merchantDTO.getPrimaryeMailAddress());

		Country country = new Country();
		country.setCountryId(merchantDTO.getCountryId());

		City city = new City();
		city.setCityId(merchantDTO.getCityId());

		Quarter quarter = new Quarter();
		quarter.setQuarterId(merchantDTO.getQuarterId());

		merchant.setCountry(country);
		merchant.setCity(city);
		merchant.setQuarter(quarter);
		merchantDao.save(merchant);

		return merchant.getMerchantId();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#updateMerchant(com.eot.banking.dto.MerchantDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateMerchant(MerchantDTO merchantDTO) throws EOTException {

		Merchant merchant = merchantDao.getMerchantFromMerchantID(merchantDTO.getMerchantId());

		merchant.setActive(merchantDTO.getActive());
		merchant.setAddress(merchantDTO.getAddress());
		merchant.setAlternateContactAddress(merchantDTO.getAlternateContactAddress());
		merchant.setAlternateContactMobile(merchantDTO.getAlternateContactMobile());
		merchant.setAlternateContactName(merchantDTO.getAlternateContactName());
		merchant.setAlternateContactPhone(merchantDTO.getAlternateContactPhone());
		merchant.setAlternateeMailAddress(merchantDTO.getAlternateeMailAddress());
		merchant.setMerchantName(merchantDTO.getMerchantName());
		merchant.setPrimaryContactAddress(merchantDTO.getPrimaryContactAddress());
		merchant.setPrimaryContactMobile(merchantDTO.getPrimaryContactMobile());
		merchant.setPrimaryContactName(merchantDTO.getPrimaryContactName());
		merchant.setPrimaryContactPhone(merchantDTO.getPrimaryContactPhone());
		merchant.setPrimaryeMailAddress(merchantDTO.getPrimaryeMailAddress());

		Country country = new Country();
		country.setCountryId(merchantDTO.getCountryId());

		City city = new City();
		city.setCityId(merchantDTO.getCityId());

		Quarter quarter = new Quarter();
		quarter.setQuarterId(merchantDTO.getQuarterId());

		merchant.setCountry(country);
		merchant.setCity(city);
		merchant.setQuarter(quarter);
		merchantDao.update(merchant);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#getMerchantDetails(java.lang.Integer)
	 */
	@Override
	public MerchantDTO getMerchantDetails(Integer merchantId) throws EOTException {
		Merchant merchant = merchantDao.getMerchantFromMerchantID(merchantId);
		if(merchant == null){
			throw new EOTException(ErrorConstants.INVALID_MERCHANT);
		}
		MerchantDTO merchantDTO = new MerchantDTO();

		merchantDTO.setMerchantId(merchant.getMerchantId());
		merchantDTO.setActive(merchant.getActive());
		merchantDTO.setAddress(merchant.getAddress());
		merchantDTO.setAlternateContactAddress(merchant.getAlternateContactAddress());
		merchantDTO.setAlternateContactMobile(merchant.getAlternateContactMobile());
		merchantDTO.setAlternateContactName(merchant.getAlternateContactName());
		merchantDTO.setAlternateContactPhone(merchant.getAlternateContactPhone());
		merchantDTO.setAlternateeMailAddress(merchant.getAlternateeMailAddress());
		merchantDTO.setMerchantName(merchant.getMerchantName());
		merchantDTO.setPrimaryContactAddress(merchant.getPrimaryContactAddress());
		merchantDTO.setPrimaryContactMobile(merchant.getPrimaryContactMobile());
		merchantDTO.setPrimaryContactName(merchant.getPrimaryContactName());
		merchantDTO.setPrimaryContactPhone(merchant.getPrimaryContactPhone());
		merchantDTO.setPrimaryeMailAddress(merchant.getPrimaryeMailAddress());
		merchantDTO.setCityName(merchant.getCity().getCity());
		merchantDTO.setQuarterName(merchant.getQuarter().getQuarter());
		merchantDTO.setMerchantCountry(merchant.getCountry());
		merchantDTO.setCountryId(merchant.getCountry().getCountryId());
		merchantDTO.setCityId(merchant.getCity().getCityId());
		merchantDTO.setQuarterId(merchant.getQuarter().getQuarterId());

		return merchantDTO;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#searchOutlet(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Outlet> searchOutlet(String userName, String mobileNumber, String location, String countryId, Integer status, Integer merchantId) throws EOTException {
		List<Outlet> outlets= null;
		WebUser webUser = webUserDao.getUser(userName);

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN  || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || 
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS ||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			if(teller==null){
				throw new EOTException(ErrorConstants.INVALID_USER);
			}

			outlets = merchantDao.searchOutlet(mobileNumber, null, countryId, status,location, merchantId);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			if(teller != null){
				outlets = merchantDao.searchOutlet(mobileNumber, null, countryId, status,location, merchantId);
			}
		}else {
			/*if(status.isEmpty()){
				status = null;
			}*/
			outlets = merchantDao.searchOutlet(mobileNumber, null, countryId, status,location,merchantId);
		}
		if(outlets.size() == 0 || outlets.isEmpty() || outlets.equals(""))
			throw new EOTException(ErrorConstants.INVALID_OUTLET);

		return outlets;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#saveOutlet(com.eot.banking.dto.OutletDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public Integer saveOutlet(OutletDTO outletDTO) throws EOTException {
		Outlet outlet = merchantDao.getOutletByMobileNumber(outletDTO.getPrimaryContactMobile());
		if( outlet != null ){
			throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
		}
		outlet = new Outlet();

		outlet.setActive(outletDTO.getActive());
		outlet.setAddress(outletDTO.getAddress());
		outlet.setAlternateContactAddress(outletDTO.getAlternateContactAddress());
		outlet.setAlternateContactMobile(outletDTO.getAlternateContactMobile());
		outlet.setAlternateContactName(outletDTO.getAlternateContactName());
		outlet.setAlternateContactPhone(outletDTO.getAlternateContactPhone());
		outlet.setAlternateeMailAddress(outletDTO.getAlternateeMailAddress());
		outlet.setLocation(outletDTO.getLocation());
		outlet.setPrimaryContactAddress(outletDTO.getPrimaryContactAddress());
		outlet.setPrimaryContactMobile(outletDTO.getPrimaryContactMobile());
		outlet.setPrimaryContactName(outletDTO.getPrimaryContactName());
		outlet.setPrimaryContactPhone(outletDTO.getPrimaryContactPhone());
		outlet.setPrimaryeMailAddress(outletDTO.getPrimaryeMailAddress());

		Country country = new Country();
		country.setCountryId(outletDTO.getCountryId());

		City city = new City();
		city.setCityId(outletDTO.getCityId());

		Quarter quarter = new Quarter();
		quarter.setQuarterId(outletDTO.getQuarterId());
		
		Merchant merchant = new Merchant();
		merchant.setMerchantId(outletDTO.getMerchantId());
		
		MerchantCategory merchantCategory = new MerchantCategory();
		merchantCategory.setMcc(outletDTO.getMcc());
		
		outlet.setCountry(country);
		outlet.setCity(city);
		outlet.setQuarter(quarter);
		outlet.setMerchant(merchant);
		outlet.setMerchantcategory(merchantCategory);
		merchantDao.save(outlet);

		return outlet.getOutletId();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#updateOutlet(com.eot.banking.dto.OutletDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateOutlet(OutletDTO outletDTO) throws EOTException {
		Outlet outlet = merchantDao.getOutletFromOutletId(outletDTO.getOutletId());
		
		outlet.setActive(outletDTO.getActive());
		outlet.setAddress(outletDTO.getAddress());
		outlet.setAlternateContactAddress(outletDTO.getAlternateContactAddress());
		outlet.setAlternateContactMobile(outletDTO.getAlternateContactMobile());
		outlet.setAlternateContactName(outletDTO.getAlternateContactName());
		outlet.setAlternateContactPhone(outletDTO.getAlternateContactPhone());
		outlet.setAlternateeMailAddress(outletDTO.getAlternateeMailAddress());
		outlet.setLocation(outletDTO.getLocation());
		outlet.setPrimaryContactAddress(outletDTO.getPrimaryContactAddress());
		outlet.setPrimaryContactMobile(outletDTO.getPrimaryContactMobile());
		outlet.setPrimaryContactName(outletDTO.getPrimaryContactName());
		outlet.setPrimaryContactPhone(outletDTO.getPrimaryContactPhone());
		outlet.setPrimaryeMailAddress(outletDTO.getPrimaryeMailAddress());

		Country country = new Country();
		country.setCountryId(outletDTO.getCountryId());

		City city = new City();
		city.setCityId(outletDTO.getCityId());

		Quarter quarter = new Quarter();
		quarter.setQuarterId(outletDTO.getQuarterId());
		
		Merchant merchant = new Merchant();
		merchant.setMerchantId(outletDTO.getMerchantId());
		
		MerchantCategory merchantCategory = new MerchantCategory();
		merchantCategory.setMcc(outletDTO.getMcc());
		
		outlet.setCountry(country);
		outlet.setCity(city);
		outlet.setQuarter(quarter);
		outlet.setMerchant(merchant);
		outlet.setMerchantcategory(merchantCategory);
		
		merchantDao.update(outlet);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#getOutletDetails(java.lang.Integer)
	 */
	@Override
	public OutletDTO getOutletDetails(Integer outletId) throws EOTException {
		Outlet outlet = merchantDao.getOutletFromOutletId(outletId);
		if(outlet == null){
			throw new EOTException(ErrorConstants.INVALID_MERCHANT);
		}
		OutletDTO outletDTO = new OutletDTO();

		outletDTO.setOutletId(outletId);
		outletDTO.setMerchantId(outlet.getMerchant().getMerchantId());
		outletDTO.setActive(outlet.getActive());
		outletDTO.setAddress(outlet.getAddress());
		outletDTO.setAlternateContactAddress(outlet.getAlternateContactAddress());
		outletDTO.setAlternateContactMobile(outlet.getAlternateContactMobile());
		outletDTO.setAlternateContactName(outlet.getAlternateContactName());
		outletDTO.setAlternateContactPhone(outlet.getAlternateContactPhone());
		outletDTO.setAlternateeMailAddress(outlet.getAlternateeMailAddress());
		outletDTO.setLocation(outlet.getLocation());
		outletDTO.setPrimaryContactAddress(outlet.getPrimaryContactAddress());
		outletDTO.setPrimaryContactMobile(outlet.getPrimaryContactMobile());
		outletDTO.setPrimaryContactName(outlet.getPrimaryContactName());
		outletDTO.setPrimaryContactPhone(outlet.getPrimaryContactPhone());
		outletDTO.setPrimaryeMailAddress(outlet.getPrimaryeMailAddress());
		outletDTO.setCityName(outlet.getCity().getCity());
		outletDTO.setQuarterName(outlet.getQuarter().getQuarter());
		outletDTO.setMerchantCountry(outlet.getCountry());
		outletDTO.setCountryId(outlet.getCountry().getCountryId());
		outletDTO.setCityId(outlet.getCity().getCityId());
		outletDTO.setQuarterId(outlet.getQuarter().getQuarterId());
		outletDTO.setMcc(outlet.getMerchantcategory().getMcc());
		
		return outletDTO;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#getAllActiveMCC()
	 */
	@Override
	public List<MerchantCategory> getAllActiveMCC() throws EOTException {
		return merchantDao.getAllActiveMCC();
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#searchTerminal(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Terminal> searchTerminal(String userName, Integer outletId, String mobileNumber,
			String bankId, String branchId, String status) throws EOTException {
		List<Terminal> terminalList= null;
		WebUser webUser = webUserDao.getUser(userName);

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN  || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || 
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || 
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS ||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER){
			BankTellers teller = webUserDao.getBankTeller(userName);
			if(teller==null){
				throw new EOTException(ErrorConstants.INVALID_USER);
			}
			terminalList = merchantDao.searchTerminal(outletId,teller.getBank().getBankId(),null, mobileNumber,branchId,status);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			if(teller != null){
				terminalList = merchantDao.searchTerminal(outletId,teller.getBank().getBankId(),null, mobileNumber,branchId,status);
			}
		} else {
			if(bankId != null && !bankId.equals("")){
				terminalList = merchantDao.searchTerminal(outletId,Integer.parseInt(bankId),null, mobileNumber,branchId,status);
			}else{
				terminalList = merchantDao.searchTerminal(outletId,null,null, mobileNumber,branchId,status);
			}
			
		}
		if(terminalList.size() == 0 || terminalList.isEmpty() || terminalList.equals(""))
			throw new EOTException(ErrorConstants. INVALID_TERMINAL);
		return terminalList;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#saveTerminal(com.eot.banking.dto.TerminalDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public Integer saveTerminal(TerminalDTO terminalDTO) throws Exception {
		Terminal terminal = merchantDao.getTerminalByID(terminalDTO.getTerminalId());
		if( terminal != null ){
			throw new EOTException(ErrorConstants.TERMINAL_REGISTERED_ALREADY);
		}
		terminal = merchantDao.getTerminalByMobileNumber(terminalDTO.getMobileNumber());
		
		if( terminal != null ){
			throw new EOTException(ErrorConstants.DUPLICATE_MOBILE_NUMBER);
		}
		
		Integer loginPin = EOTUtil.generateLoginPin() ;
		Integer txnPin = EOTUtil.generateTransactionPin();
		String appID = EOTUtil.generateAppID();
		String uuid = EOTUtil.generateUUID();
		
		terminal = new Terminal();
		terminal.setMobileNumber(terminalDTO.getMobileNumber());
		terminal.setActive(terminalDTO.getActive());
		terminal.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		terminal.setTransactionPin(HashUtil.generateHash(txnPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));
		Bank bank = new Bank();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		BankTellers teller = webUserDao.getBankTeller(userName);
		if(teller==null){
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		bank.setBankId(teller.getBank().getBankId());
		terminal.setBank(bank);
		Branch branch = new Branch();
		branch.setBranchId(teller.getBranch().getBranchId());
		terminal.setBranch(branch);
		Merchant merchant = new Merchant();
		merchant.setMerchantId(terminalDTO.getMerchantId());
		terminal.setMerchant(merchant);
		Outlet outlet = new Outlet();
		outlet.setOutletId(terminalDTO.getOutletId());
		terminal.setOutlet(outlet);
		merchantDao.save(terminal);
		
		Account account = new Account();

		account.setAccountNumber(EOTUtil.generateAccountNumber(bankDao.getNextAccountNumberSequence()));
		account.setAccountType(EOTConstants.ACCOUNT_TYPE_PERSONAL);
		account.setActive(EOTConstants.ACCOUNT_STATUS_ACTIVE);
		String alias = EOTConstants.ACCOUNT_ALIAS_CUSTOMER + " - " +teller.getBank().getBankName() ;
		account.setAlias(alias);
		account.setCurrentBalance(0.0);
		account.setCurrentBalanceType(EOTConstants.ACCOUNT_BALANCE_TYPE_CREDIT);
		account.setReferenceId(terminal.getTerminalId().toString());
		account.setReferenceType(EOTConstants.REFERENCE_TYPE_MERCHANT);
		merchantDao.save(account);
		terminal.setAccount(account);
		merchantDao.update(terminal);
		
		AppMaster app = new AppMaster();
		app.setAppId(appID);
		app.setReferenceId(terminal.getTerminalId().toString());
		app.setReferenceType(EOTConstants.REFERENCE_TYPE_MERCHANT);
		app.setStatus(EOTConstants.APP_STATUS_NEW);
		app.setUuid(uuid);
		app.setAppVersion("1.0");

		Calendar cal = Calendar.getInstance();
		app.setCreatedDate(cal.getTime());
		Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth+1);
		app.setExpiryDate(cal.getTime());
		merchantDao.save(app);
		
		return terminal.getTerminalId();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#updateTerminal(com.eot.banking.dto.TerminalDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateTerminal(TerminalDTO terminalDTO) throws EOTException {
		Terminal terminal = merchantDao.getTerminalByID(terminalDTO.getTerminalId());
		//terminal.setAccount(terminalDTO.getAccountNumber());
		terminal.setActive(terminalDTO.getActive());
		terminal.setMobileNumber(terminalDTO.getMobileNumber());
		merchantDao.update(terminal);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#getMerchantList(java.lang.Integer)
	 */
	@Override
	public List<Merchant> getMerchantList(Integer merchantId) throws EOTException {
		return merchantDao.getMerchantListFromMerchantID(merchantId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#getOutletList(java.lang.Integer)
	 */
	@Override
	public List<Outlet> getOutletList(Integer merchant) throws EOTException {
		return merchantDao.getOutletList(merchant);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.MerchantService#getTerminalDetails(java.lang.Integer)
	 */
	@Override
	public TerminalDTO getTerminalDetails(Integer terminalId) throws EOTException {
		Terminal terminal = merchantDao.getTerminalByID(terminalId);
		if(terminal == null){
			throw new EOTException(ErrorConstants.INVALID_MERCHANT);
		}
		TerminalDTO terminalDTO = new TerminalDTO();
		terminalDTO.setTerminalId(terminal.getTerminalId());
		terminalDTO.setAccountNumber(terminal.getAccount().getAccountNumber());
		terminalDTO.setActive(terminal.getActive());
		terminalDTO.setMerchantId(terminal.getMerchant().getMerchantId());
		terminalDTO.setMobileNumber(terminal.getMobileNumber());
		terminalDTO.setOutletId(terminal.getOutlet().getOutletId());
		
		return terminalDTO;
	}

	@Override
	// bugId-505 by:rajlaxmi for:showing datatable with active/deactive status
	public List<Outlet> showAllOutlets(String userName, String mobileNumber, String location, String countryId,
			String status) {
		List<Outlet> outlets= null;
		WebUser webUser = webUserDao.getUser(userName);

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN  || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ||
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || 
				webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS ||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			
			outlets = merchantDao.showAllOutlets(mobileNumber, null, countryId, status,location);
		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER){

			BankTellers teller = webUserDao.getBankTeller(userName);

			if(teller != null){
				outlets = merchantDao.showAllOutlets(mobileNumber, null, countryId, status,location);
			}
		}else {
			/*if(status.isEmpty()){
				status = null;
			}*/
			outlets = merchantDao.showAllOutlets(mobileNumber, null, countryId, status,location);
		}
		

		return outlets;
	
	}

	@Override
	public boolean checkIfEmailAlreadyExists(MerchantDTO merchantDTO) {
		return merchantDao.checkIfEmailExists(merchantDTO);
	}

	@Override
	public boolean checkIfUpdateEmailExists(MerchantDTO merchantDTO) {
		return merchantDao.checkForUpdateEmail(merchantDTO);
	}

	@Override
	public boolean checkIfMobileAlreadyExists(MerchantDTO merchantDTO) {
		return merchantDao.checkIfMobileExists(merchantDTO);
	}

	@Override
	public boolean checkIfUpdateMobileExists(MerchantDTO merchantDTO) {
		return merchantDao.checkForUpdateMobile(merchantDTO);

	}
}
