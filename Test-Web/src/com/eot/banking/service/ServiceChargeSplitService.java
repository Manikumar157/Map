package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.exceptions.EOTException;
import com.eot.entity.Bank;
import com.eot.entity.StakeHolder;
import com.eot.entity.TransactionType;

public interface ServiceChargeSplitService {
	
	public List<TransactionType> getSCApplicableTransactionTypes(String locale) throws EOTException;
	public void saveServiceChargeSplit(Integer bankId,Map<String,String[]> reqParam) throws EOTException;
	public Map<String,Float> getServiceCharges(Integer bankId) throws EOTException;
	public Bank getBank(Integer bankId) throws EOTException;
	public List<StakeHolder> getstakeHolders();
}
