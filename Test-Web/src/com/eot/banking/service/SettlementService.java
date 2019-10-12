package com.eot.banking.service;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.entity.Bank;
import com.eot.entity.ClearingHousePool;

public interface SettlementService {
	String createsettlementFileForPool(Integer  clearingHousePoolId,String settlementDate,ClearingHouseDTO clearingHouseDTO,String userName,HttpServletResponse response) throws EOTException;
	ClearingHouseDTO viewClearingHouseDetails(int poolId)throws EOTException;
	List<Bank> getBanksByPoolId(int poolId)throws EOTException;
	List<ClearingHousePool> getBankIdByUserName(String userName) throws EOTException;
	Bank getBankName(String userName)throws EOTException;
	String generateCSVFile(Integer poolId,String userName,Locale locale,ClearingHouseDTO clearingHouseDTO) throws EOTException;
	String generateSettlementSummaryCSVFile(Integer poolId,String userName,Locale locale,ClearingHouseDTO clearingHouseDTO)throws EOTException;
	List getSettlementNetBalance(ClearingHouseDTO clearingHouseDTO,String userName)throws EOTException;
	List getSettlementNetBalance1(ClearingHouseDTO clearingHouseDTO,String userName)throws EOTException;
}
