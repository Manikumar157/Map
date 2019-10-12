package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.BillerDTO;
import com.eot.banking.dto.SenelecBillSearchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.BillerTypes;
import com.eot.entity.Country;

public interface BillerService {
	 List<Country> getCountryList(String language);
	 List<BillerTypes> getBillerTypes();
	 List<Bank> getBankList();
	 void addBillerDetails(BillerDTO billerDTO) throws EOTException;
	 void updateBillerDetails(BillerDTO billerDTO ) throws EOTException;
	 Page getBillersList(BillerDTO billerDTO,Integer pageNumber) throws EOTException;
	 BillerDTO getBillerDetails(Integer billerId) throws EOTException;
	 Page getSenelecBills(Integer pageNumber) throws EOTException;
	 Page searchSenelecBill(SenelecBillSearchDTO senelecBillSearchDTO,Integer pageNumber) throws EOTException;
	 List<SenelecBillSearchDTO> searchSenelecBillForPdf(SenelecBillSearchDTO senelecBillSearchDTO) throws EOTException;
	 void getPolicyDetailsByPolicyNo(Map<String,Object> model,Integer pageNumber,String policyNo,Integer recordNo)throws EOTException;
	 public List<Object> getAllPaidBills() throws EOTException;
	 public double getTotalTransactionAmount() throws EOTException;
	 Page getBillersList(int pageNumber)throws EOTException;
	 
}
