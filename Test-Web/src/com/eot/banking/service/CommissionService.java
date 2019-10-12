package com.eot.banking.service;

import java.io.EOFException;
import java.util.List;

import com.eot.banking.dto.CommisionSplitsDTO;
import com.eot.banking.dto.CommissionDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.CustomerProfiles;


public interface CommissionService {
	Page loadCommisionSlabDetails(Integer pageNumber , CommissionDTO commissionDTO) throws EOTException;
	public List<CustomerProfiles> loadCustomerListForCommission(CommissionDTO commissionDTO);
	public List<CustomerProfiles> loadCustomerProfiles(Integer bankId) throws EOFException ;
	CommissionDTO getCommisionDetails(Long commissionId) throws EOFException;

	CommissionDTO updateCommisionSlabs(CommissionDTO commissionDTO) throws EOFException;
	CommissionDTO saveCommisionSlabs(CommissionDTO commissionDTO) throws EOTException;
	void createCommissionSplits(CommisionSplitsDTO commisionSplitsDTO) throws EOTException;
	List<CommisionSplitsDTO> loadCommissionSplits() throws EOTException;
	void deleteOldCommission() throws EOTException;

}
