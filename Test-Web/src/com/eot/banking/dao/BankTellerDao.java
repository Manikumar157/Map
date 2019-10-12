package com.eot.banking.dao;

import com.eot.entity.BankTellers;

public interface BankTellerDao extends BaseDao {
	
	public BankTellers findByUserName(String userName);
}
