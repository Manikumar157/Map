package com.eot.banking.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BankDao;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.ClearingHousePool;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.Currency;

@Service("clearingHouseService")
@Transactional(readOnly=true)
public class ClearingHouseServiceImpl implements ClearingHouseService {

	@Autowired
	private BankDao bankDao;

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void addClearingHouse( ClearingHouseDTO clearanceDTO) throws EOTException{	
		ClearingHousePool chp=bankDao.getClearingHouseByName(clearanceDTO.getClearingHouseName());
		if(chp!=null){
			throw new EOTException(ErrorConstants.CLEARINGHOUSE_NAME_EXIST);
		}
		ClearingHousePool chpMbl=bankDao.getClearingHouseByMobilenumber(clearanceDTO.getMobileNumber());
		if(chpMbl!=null)
		{
			throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
			
		}
		chp=new ClearingHousePool();
		chp.setDescription(clearanceDTO.getDescription());
		chp.setOutputFileNameFormat(clearanceDTO.getFileFormat());
		chp.setMessageType(clearanceDTO.getMessageType());
		chp.setClearingPoolName(clearanceDTO.getClearingHouseName());
		Currency currency = new Currency();
		currency.setCurrencyId(clearanceDTO.getCurrency());
		chp.setCurrency(currency);
		chp.setStatus(clearanceDTO.getStatus());
		chp.setSettlementAccount(clearanceDTO.getSettlementAccount());
		chp.setEmailID(clearanceDTO.getEmailID());
		chp.setGuaranteeAccount(clearanceDTO.getGuaranteeAccount());
		chp.setEOTSwiftCode(clearanceDTO.getEOTSwiftCode());
		chp.setMobileNumber(clearanceDTO.getMobileNumber());
		chp.setContactPerson(clearanceDTO.getContactPerson());
		chp.setCentralBankAccount(clearanceDTO.getCentralBankAccount());
		chp.setMsgSender(clearanceDTO.getMessageSender());

		String date = DateUtil.formatDate(new Date());

		try {
			date= date+" "+ISOUtil.zeropad(clearanceDTO.getStartUpTimeHours(), 2)+ISOUtil.zeropad(clearanceDTO.getStartUpTimeMinutes(), 2);

		} catch (Exception e) {
			e.printStackTrace();
		}

		chp.setStartDateTime(DateUtil.dateAndTime(date));

		Integer interval = clearanceDTO.getSettlementIntervalHours() * 60 + clearanceDTO.getSettlementIntervalMins();
		chp.setSettlementInterval(interval);	
		bankDao.save(chp);		

		for(Integer bankId:clearanceDTO.getBanks()){
			ClearingHousePoolMember chpm = new ClearingHousePoolMember();
			Bank bank = new Bank();
			bank.setBankId(bankId);
			chpm.setBank(bank);
			chpm.setClearingPoolId(chp.getClearingPoolId());
			Integer chpm1=bankDao.getMaxPoolPriority();
			if(chpm1==null){
				chpm.setPoolPriority(1);
			}else{
			chpm.setPoolPriority(chpm1);
			}
			bankDao.save(chpm);
		}

	}

	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class )
	public void updateClearingHouse(ClearingHouseDTO clearanceDTO) throws EOTException {

		ClearingHousePool chp = bankDao.getClearingHouseDetails(clearanceDTO.getClearingPoolId());
		if(chp == null){
			throw new EOTException(ErrorConstants.INVALID_CH_POOL);
		}
		
		ClearingHousePool chpMbl=bankDao.getClearingHouseByMobilenumber(clearanceDTO.getMobileNumber());
		
		//Mobile Number Validation in case of adding new clearing house
		if(clearanceDTO.getClearingPoolId() == null) {
			if(chpMbl!=null)
			{
				throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
				
			}
		}
		// For updating existing clearing house
			else{
				// If entered mobile no. and the registered mobile no is same then skipping otherwise validating mobile no.
				boolean check= chpMbl!=null && chpMbl.getMobileNumber().equals(clearanceDTO.getMobileNumber()) 
						&& chpMbl.getClearingPoolId().equals(clearanceDTO.getClearingPoolId());
				if(!check){
					ClearingHousePool chpMb2=bankDao.getClearingHouseByMobilenumber(clearanceDTO.getMobileNumber());
					if(chpMb2!=null)
					{
						throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
						
					}
				}
		}
		
		if(!chp.getClearingPoolName().equalsIgnoreCase(clearanceDTO.getClearingHouseName().trim())){
			ClearingHousePool clearingHousePool=bankDao.getClearingHouseByName(clearanceDTO.getClearingHouseName());
			if(clearingHousePool!=null){
				throw new EOTException(ErrorConstants.CLEARINGHOUSE_NAME_EXIST);
			}
		}

		chp.setOutputFileNameFormat(clearanceDTO.getFileFormat());
		chp.setMessageType(clearanceDTO.getMessageType());
		chp.setClearingPoolName(clearanceDTO.getClearingHouseName());
		chp.setDescription(clearanceDTO.getDescription());
		Currency currency = new Currency();
		currency.setCurrencyId(clearanceDTO.getCurrency());
		chp.setCurrency(currency);
		chp.setStatus(clearanceDTO.getStatus());
		chp.setSettlementAccount(clearanceDTO.getSettlementAccount());
		chp.setEmailID(clearanceDTO.getEmailID());
		chp.setGuaranteeAccount(clearanceDTO.getGuaranteeAccount());
		chp.setEOTSwiftCode(clearanceDTO.getEOTSwiftCode());
		chp.setMobileNumber(clearanceDTO.getMobileNumber());
		chp.setContactPerson(clearanceDTO.getContactPerson());
		chp.setCentralBankAccount(clearanceDTO.getCentralBankAccount());
		chp.setMsgSender(clearanceDTO.getMessageSender());
		
		String date = DateUtil.formatDate(new Date());

		try {
			System.out.println(ISOUtil.zeropad(clearanceDTO.getStartUpTimeHours(), 2));
			date= date+" "+ISOUtil.zeropad(clearanceDTO.getStartUpTimeHours(), 2)+ISOUtil.zeropad(clearanceDTO.getStartUpTimeMinutes(), 2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(! DateUtil.dateAndTime(chp.getStartDateTime()).equals(DateUtil.dateAndTime(DateUtil.dateAndTime(date)))){
			
			bankDao.updateSettlementBatchList(chp.getClearingPoolId());
		}

		chp.setStartDateTime(DateUtil.dateAndTime(date));

		Integer interval = clearanceDTO.getSettlementIntervalHours() * 60 + clearanceDTO.getSettlementIntervalMins();
		chp.setSettlementInterval(interval);

		bankDao.update(chp);
		int priorityId=1;
		Set<ClearingHousePoolMember> cpm=chp.getPoolMembers();
		for (ClearingHousePoolMember ch : cpm) {
			priorityId=ch.getPoolPriority();
			break;
		}
		bankDao.deleteList(chp.getPoolMembers());

		for(Integer bankId:clearanceDTO.getBanks()){
			ClearingHousePoolMember chpm = new ClearingHousePoolMember();			
			Bank bank = new Bank();	
			bank.setBankId(bankId);
			chpm.setBank(bank);
			chpm.setClearingPoolId(chp.getClearingPoolId());
			chpm.setPoolPriority(priorityId);
			bankDao.saveOrUpdate(chpm);				
		}			

	}
	@Override
	public ClearingHouseDTO getClearingHouseDetails(Integer clearingPoolId)throws EOTException {

		ClearingHousePool chp = bankDao.getClearingHouseDetails(clearingPoolId);
		if(chp == null){
			throw new EOTException(ErrorConstants.INVALID_CLEARING_HOUSE);
		}
		ClearingHouseDTO dto = new ClearingHouseDTO();
		dto.setClearingPoolId(chp.getClearingPoolId());
		dto.setClearingHouseName(chp.getClearingPoolName());
		dto.setCurrency(chp.getCurrency().getCurrencyId());
		dto.setDescription(chp.getDescription());
		dto.setFileFormat(chp.getOutputFileNameFormat());
		dto.setMessageType(chp.getMessageType());
		dto.setStatus(chp.getStatus());
		dto.setSettlementAccount(chp.getSettlementAccount());
		dto.setEmailID(chp.getEmailID());
		dto.setGuaranteeAccount(chp.getGuaranteeAccount());
		dto.setEOTSwiftCode(chp.getEOTSwiftCode());
		dto.setMobileNumber(chp.getMobileNumber());
		dto.setContactPerson(chp.getContactPerson());
		dto.setCentralBankAccount(chp.getCentralBankAccount());
		dto.setMessageSender(chp.getMsgSender());
		Integer intervalHours = chp.getSettlementInterval() / 60 ;
		Integer intervalMins = chp.getSettlementInterval() % 60;
		dto.setSettlementIntervalHours(intervalHours);
		dto.setSettlementIntervalMins(intervalMins);
		dto.setMessageType(chp.getMessageType());
		String startDateTime = DateUtil.formatDateAndTime(chp.getStartDateTime());
		System.out.println("Start Date: " + chp.getStartDateTime());
		
		dto.setStartUpTimeHours(startDateTime.split(" ")[1].split(":")[0]);
		dto.setStartUpTimeMinutes(startDateTime.split(" ")[1].split(":")[1]);
		// vineeth changes, taking mobile number length dynamically.
		dto.setMobileNumberLength(chp.getMobileNumber().length());
		// vineeth change over
		int i = 0 ;
		Set<ClearingHousePoolMember> members  = chp.getPoolMembers();
		Integer[] banks = new Integer[members.size()];
		for (ClearingHousePoolMember chpm : members) {
			banks[i++] = chpm.getBank().getBankId();
		}
		dto.setBanks(banks);
		
		return dto;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List> getMasterData(int pageNumber) throws EOTException {

		Map model = new HashMap();		
		model.put("currencyList", bankDao.getCurrencies());
		model.put("bankList", bankDao.getActiveBanks());	

		//Page page = PaginationHelper.getPage(bankDao.getClearingHouses(), appConfigurations.getResultsPerPage(), pageNumber);

		Page page = bankDao.getClearingHouses(pageNumber) ;

		page.requestPage="showClearanceHouseForm.htm";
		model.put("page",page);
		return model;		
	}

	@Override
	public Page searchClearingHouse(ClearingHouseDTO clearingHouseDTO,int pageNumber) throws EOTException{
		
		Page page = bankDao.getSearchClearingHouseList(clearingHouseDTO, pageNumber);		
		if(page.results.size()==0){
			throw new EOTException(ErrorConstants.CLEARING_HOUSE_NOT_EXIST);
		}
		
		return page;
	}

	@Override
	public List<Currency> getCurrencyList() {
		List<Currency> currencyList=bankDao.getCurrencies();
		return currencyList;
	}	
}
