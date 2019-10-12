package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.StakeHolderDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;

public interface StakeHolderService {
	
	public void addStakeHolder(StakeHolderDTO stakeHolderDTO)throws EOTException;
	public void updateStakeHolder(StakeHolderDTO stakeHolderDTO)throws EOTException;
	public StakeHolderDTO getStakeHolderDetails(Integer stakeholderId)throws EOTException;
	public Map<String,List> getMasterData(String language)throws EOTException;
	public Page getStakeHolderList(Integer pageNumber) throws EOTException;
	public Integer getMobileNumLength(Integer countryId)throws EOTException;
	
}
