package com.eot.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BankTellerDao;
import com.eot.entity.BankTellers;

@Service("bankTellerService")
@Transactional(readOnly=true)
public class BankTellerServiceImpl implements BankTellerService {
	
	@Autowired
	BankTellerDao bankTellerDao;
	
	@Override
	public BankTellers findByUserName(String userName) {
		return bankTellerDao.findByUserName(userName);
	}

}
