package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.CardDto;
import com.eot.banking.dto.OperatorDTO;
import com.eot.banking.dto.OperatorDenominationDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.Country;
import com.eot.entity.Operator;
import com.eot.entity.OperatorDenomination;

public interface OperatorService {
	
	public Page getOperatorList(Integer pageNumber) throws EOTException;
	public List<Country> getCountryList(String language);
	public Page getDenominations(Long operatorId ,Integer pageNumber)throws EOTException;
	public Page getVouchers(Long denominationId,Integer pageNumber)throws EOTException;
	public void addOperator(OperatorDTO operatorDTO)throws EOTException;
	public void updateOperator(OperatorDTO operatorDTO)throws EOTException;
	public OperatorDTO getOperator(Long operatorId)throws EOTException;
	public void addDenomination(OperatorDenominationDTO operatorDenominationDTO) throws EOTException;
	public void updateDenomination(OperatorDenominationDTO operatorDenominationDTO)throws EOTException;
	public OperatorDenominationDTO getDenomination(Long denominationId)throws EOTException;
	public void  uploadVoucherDetails(OperatorDenominationDTO operatorDenominationDTO)throws EOTException;
	public Operator getOperatorName(Long OperatorId);
	public void checkDenomination(OperatorDenominationDTO operatorDenominationDTO)throws EOTException;
	public List<Operator> getOperatorListByCountry(Integer countryId);
	public List<OperatorDenomination> getDenominationList(Long operatorId);
    byte[] getChartImageBytes(OperatorDTO operatorDTO)throws EOTException;
	public List getChartList(OperatorDTO operatorDTO)throws EOTException;
	public byte[] getPieChartImageBytes(OperatorDTO operatorDTO);
	List<Bank> getBankList();
	public Page searchOperator(OperatorDTO operatorDTO, int pageNumber,
			String language) throws EOTException;
	public CardDto getCardDetailsByOperatorId(Integer operatorId);
	public void saveOrUpdateCard(CardDto cardDto,Map<String,Object> model)throws EOTException;
	

}
