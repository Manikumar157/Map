package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Currency;

public interface ClearingHouseService {

	public Map<String,List> getMasterData(int pageNumber)throws EOTException;
	public void addClearingHouse(ClearingHouseDTO clearanceDTO) throws EOTException;
	public void updateClearingHouse(ClearingHouseDTO clearanceDTO) throws EOTException;
	public ClearingHouseDTO getClearingHouseDetails(Integer clearingPoolId) throws EOTException;
	public Page searchClearingHouse(ClearingHouseDTO clearingHouseDTO,int pageNumber)throws EOTException;
	public List<Currency> getCurrencyList();
	
}
