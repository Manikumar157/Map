package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.CommissionDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.CommisionSplits;
import com.eot.entity.Commission;
import com.eot.entity.Currency;
import com.eot.entity.CustomerProfiles;

public interface CommissionDao extends BaseDao{

	List<Commission> loadCommisionSlab(int transactionType, Integer bankId, Integer profileId);

	List<CustomerProfiles> loadCustomerProfiles(Integer bankId);

	Commission loadCommissionDetailsByCommissionId(Long commissionId);

	void deleteCommissionSlab(Long commisionId);

	Page getAllExchangeRate(int pageNumber);

	List<Currency> getCurrencies();

	Page loadCommissionSlabDetails(Integer pageNumber, CommissionDTO commissionDTO);

	Page getExchangeRateByBankId(int pageNumber, Bank bankId);

	List<CommisionSplits> loadCommissionSplits();

}
