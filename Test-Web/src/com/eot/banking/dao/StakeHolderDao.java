package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.StakeHolderDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.Account;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.StakeHolder;

public interface StakeHolderDao extends BaseDao {
	
	public List<StakeHolder> getStakeHolders();
	public StakeHolder getStakeHolderDetails(Integer stakeholderId);
	public Account getAccount(String accountNumber);
	public ServiceChargeSplit getServiceChargeSplit(String accountNumber,Integer transactionType,Integer bankId);
	public StakeHolder getStakeHolderByName(String stakeholderOrganization);
	public StakeHolder getStakeHolderByName(String stakeholderOrganization,Integer stakeholderId);
	public Page getStakeHolderList(Integer pageNumber);
	public StakeHolder getStakeHolderByMobileNumber(String contactMobile);
	public StakeHolder getUpdateStakeHolderByMobileNumber(StakeHolderDTO stakeHolderDTO);
	public void updateStakeHolder(StakeHolder stakeHolder);
	
}
