package com.eot.banking.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dto.BranchDTO;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.Branch;
import com.eot.entity.City;
import com.eot.entity.Quarter;

@Service("branchService")
@Transactional(readOnly=true)

public class BranchServiceImpl implements BranchService {

	@Autowired 
	private BankDao bankDao ;
	@Autowired
	private LocationDao locationDao;

	@Override
	@Transactional(readOnly=false, propagation = Propagation.REQUIRES_NEW, rollbackFor = EOTException.class)
	public void addBranchDetails(BranchDTO branchDTO) throws EOTException{

		Branch branch=bankDao.getBranchFromLocation(branchDTO.getLocation(),branchDTO.getBankId());

		if(branch!=null){			 
			throw new EOTException(ErrorConstants.BRANCH_NAME_EXIST);
		}	

		branch=bankDao.getBranchByCode(branchDTO.getBranchCode(),branchDTO.getBankId());

		if(branch!=null){			 
			throw new EOTException(ErrorConstants.BRANCH_CODE_EXIST);
		}	

		branch=new Branch();
		branch.setLocation(branchDTO.getLocation());
		branch.setBranchCode(branchDTO.getBranchCode().toUpperCase());
		branch.setAddress(branchDTO.getAddress());
		branch.setDescription(branchDTO.getDescription());		
		City city=new City();
		city.setCityId(branchDTO.getCityId());
		branch.setCity(city);

		Quarter quarter=new Quarter();
		quarter.setQuarterId(branchDTO.getQuarterId());
		branch.setQuarter(quarter);

		Bank bank = bankDao.getBank(branchDTO.getBankId());
		bank.setBankId(branchDTO.getBankId());
		branch.setBank(bank);	
		branch.setCountry(bank.getCountry());	

		bankDao.save(branch);			
	}

	@Override
	public BranchDTO getBranchDetails(Long branchId)throws EOTException {
		Branch branch=bankDao.getBranchDetails(branchId);	

		if(branch==null){
			throw new EOTException(ErrorConstants.INVALID_BRANCH);
		}
		BranchDTO branchDTO=new BranchDTO();		
		branchDTO.setBranchId(branch.getBranchId());		
		branchDTO.setLocation(branch.getLocation());
		branchDTO.setBranchCode(branch.getBranchCode());
		branchDTO.setAddress(branch.getAddress());
		branchDTO.setDescription(branch.getDescription());
		branchDTO.setCityId(branch.getCity().getCityId());
		branchDTO.setQuarterId(branch.getQuarter().getQuarterId());
		branchDTO.setBankId(branch.getBank().getBankId());
		branchDTO.setCountryId(branch.getCountry().getCountryId());		

		return branchDTO;
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class )
	public void updateBranchDetails(BranchDTO branchDTO) throws EOTException {

		Branch branch=bankDao.getBranchFromLocation(branchDTO.getBranchId(),branchDTO.getBranchCode(),branchDTO.getBankId());

		if(branch!=null){
			throw new EOTException(ErrorConstants.BRANCH_LOCATION_EXIST);
		}

		branch=bankDao.getBranchesByIdCode(branchDTO.getBranchId(),branchDTO.getBranchCode(),branchDTO.getBankId());	

		if(branch!=null){			 
			throw new EOTException(ErrorConstants.BRANCH_CODE_EXIST);
		}else{	
			branch=bankDao.getBranchDetails(branchDTO.getBranchId());		
			branch.setLocation(branchDTO.getLocation());
			branch.setAddress(branchDTO.getAddress());
			branch.setDescription(branchDTO.getDescription());
			branch.setBranchCode(branchDTO.getBranchCode().toUpperCase());

			City city=new City();
			city.setCityId(branchDTO.getCityId());
			branch.setCity(city);

			Quarter quarter=new Quarter();
			quarter.setQuarterId(branchDTO.getQuarterId());
			branch.setQuarter(quarter);

			bankDao.update(branch);		
		}
	}
	public Bank getBank(Integer bankId) throws EOTException{
		Bank bank = bankDao.getBank(bankId);
		if(bank == null){
			throw new EOTException(ErrorConstants.INVALID_BANK);
		}
		return bank;
	}

	@Override
	public Map<String,Object> getMasterData(Integer bankId,int pageNumber) {
		Bank bank = bankDao.getBank(bankId);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("cityList",locationDao.getAllCities(bank.getCountry().getCountryId()));
		model.put("bank",bank);
		return model;
	}

	@Override
	public List<Quarter> getQuarterList(Integer cityId) {
		return locationDao.getAllQuarters(cityId);
	}



	@Override
	public Page searchBranch(BranchDTO branchDTO, int pageNumber) throws EOTException{
		Page page = bankDao.getAllBranchList(branchDTO,pageNumber);
		if(page.getResults().size() == 0 || page.getResults().isEmpty() || page.getResults().equals(""))
			throw new EOTException(ErrorConstants.BRANCH_NOT_EXIST);

		return page;
	}

	@Override
	public List<Branch> getBranchList() {
		return bankDao.getBranchList();
	}


}
