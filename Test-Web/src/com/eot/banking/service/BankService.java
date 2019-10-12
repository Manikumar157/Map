package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.BankDTO;
import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.InterBankSCDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;

public interface BankService  {

	public void addBankDetails(BankDTO bankDTO,String locale) throws EOTException;
	
	public void updateBankDetails(BankDTO bankDTO,String locale) throws EOTException;

	public BankDTO getBankDetails(Integer bankId,String locale) throws EOTException;
	
	public Map getMasterData(int pageNumber,String language) throws EOTException;
	
	public void saveInterBankServiceCharges(InterBankSCDTO dto,String locale);
	
	public InterBankSCDTO getInterBankServiceCharges() ;
	
	public CardDto getCardDetailsByBankId(Integer bankId);
	
	public void saveOrUpdateCard(CardDto cardDto,Map<String,Object> model)throws EOTException;

	public Page searchBank(BankDTO bankDTO, int pageNumber,String language) throws EOTException;

	public List<Bank> getBankList();

}
