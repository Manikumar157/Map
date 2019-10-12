package com.eot.banking.dao;

import com.eot.entity.Branding;

public interface BrandingDao extends BaseDao  {
	
	public Branding findByBankId(Integer bankId);
}
