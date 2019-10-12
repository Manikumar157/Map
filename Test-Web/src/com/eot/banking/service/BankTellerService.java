package com.eot.banking.service;

import com.eot.entity.BankTellers;

public interface BankTellerService {
	
	public BankTellers findByUserName(String userName);

}
