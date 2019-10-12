package com.eot.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BrandingDao;
import com.eot.entity.Branding;

@Service("brandingService")
@Transactional(readOnly=true)
public class BrandingServiceImpl implements BrandingService {
	
	@Autowired
	BrandingDao brandingDao;
	
	@Override
	public Branding findByBankId(Integer bankId) {
		
		return brandingDao.findByBankId(bankId);
	}

}
