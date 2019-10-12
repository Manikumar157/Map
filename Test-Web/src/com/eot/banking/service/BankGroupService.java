package com.eot.banking.service;

import com.eot.banking.dto.BankGroupDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;

public interface BankGroupService {

	public Page getBankGroupList(Integer pageNumber);
	public void addBankGroup(BankGroupDTO bankGroupDTO) throws EOTException;
	public void updateBankGroup(BankGroupDTO bankGroupDTO) throws EOTException;
	public BankGroupDTO getBankGroup(Integer bankGroupId) throws EOTException;
	public Page getBank(Integer bankGroupId,Integer pageNumber);
	

}
