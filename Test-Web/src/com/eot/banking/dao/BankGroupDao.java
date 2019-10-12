package com.eot.banking.dao;

import com.eot.banking.utils.Page;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;

public interface BankGroupDao extends BaseDao {

	Page getBankGroupList(Integer pageNumber);
	BankGroup getBankGroup(Integer bankGroupId);
	BankGroup getBankGroupByName(String bankGroupName);
	BankGroup getBankGroupByName(String bankGroupName,Integer bankGroupId);
	Page getBank(Integer bankGroupId,Integer pageNumber);
	BankGroupAdmin getGroupAdminByUsername(String userName);
	// change vineeth, 16-07-2018, bugno: 5696
	BankGroup getBankGroupShortName(String bankGroupShortName);
	// end vineeth
	
}
