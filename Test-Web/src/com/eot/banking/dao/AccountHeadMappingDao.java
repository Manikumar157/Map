package com.eot.banking.dao;

import com.eot.entity.Account;
import com.eot.entity.ClearingHousePool;

public interface AccountHeadMappingDao {

	
	String getAccountByHeadId( int accountHeaderId, Integer bankId );
	public String getAccountByHeadId( int accountHeadId );
	public com.eot.banking.dto.SettlementDto getAccountHeadIDsForAccounts( String debitAccountNo, String creditAccountNo);
	public ClearingHousePool getguaranteeAccountNumber(Integer poolID);
}
