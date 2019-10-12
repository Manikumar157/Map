package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.BranchDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.Branch;
import com.eot.entity.Quarter;

public interface BranchService {
	
	public void addBranchDetails(BranchDTO branchDTO) throws EOTException;
	public void updateBranchDetails(BranchDTO branchDTO) throws EOTException;
	public Map<String,Object> getMasterData(Integer bankId,int pageNumber);
	public BranchDTO getBranchDetails(Long branchId)throws EOTException;	
	public Bank getBank(Integer bankId) throws EOTException;
	public List<Quarter> getQuarterList(Integer cityId);
	public Page searchBranch(BranchDTO branchDTO, int pageNumber)throws EOTException;
	public List<Branch> getBranchList();
}
