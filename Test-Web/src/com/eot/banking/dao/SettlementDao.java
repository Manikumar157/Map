package com.eot.banking.dao;

import java.util.Date;
import java.util.List;

import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.entity.Bank;
import com.eot.entity.ClearingHousePool;
import com.eot.entity.SettlementJournal;

public interface SettlementDao extends BaseDao{

	List<SettlementJournal> getUnSettledEntries(Integer clearingPoolID,Date  settlementDate);
	List<Bank> getBanksByPoolId(int poolId);
	List<ClearingHousePool> getClearingHousesByBankId(Integer bankID);
	Bank getBankName(String userName);
	List generateCSVFile(Integer poolId,Integer bankId,ClearingHouseDTO clearingHouseDTO);
	List generateSettlementSummaryCSVFile(Integer poolId,Integer bankId,ClearingHouseDTO clearingHouseDTO);
	List getSettlementNetBalance(ClearingHouseDTO clearingHouseDTO, Integer bankId);
	List getSettlementNetBalance1(ClearingHouseDTO clearingHouseDTO,Integer bankId);

}
