package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.BankDTO;
import com.eot.banking.dto.BranchDTO;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.AccountHead;
import com.eot.entity.Bank;
import com.eot.entity.BankAgreementModel;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.ClearingHousePool;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.Currency;
import com.eot.entity.CustomerCard;
import com.eot.entity.ServiceChargeSplit;

public interface BankDao extends BaseDao{
	
	Page getAllBanks(int pageNumber);
	List<Bank> getActiveBanks();
	List<Bank> getBanksByType(Integer agreementType) ;
	List<Currency> getCurrencies();
	Page getClearingHouses(int pageNumber);
	List<ClearingHousePool> getActiveClearingHouses();
	List<BankAgreementModel> getBankAgreementModels();
	Bank getBank(Integer bankId);
	BankTellers getTellerByUsername(String userName);
	ClearingHousePool getClearingHouseDetails(Integer clearingPoolId);
	Bank getBankCode(String bankCode);
	Bank getBankBySwiftCode(String swiftCode);
	Bank getBankBySwiftCode(Integer bankId,String swiftCode);
	
	Long getNextAccountNumberSequence();
	
	Page getAllBranchList(BranchDTO branchDTO,int pageNumber);
	Branch getBranchDetails(Long branchId);
	ClearingHousePool getClearingHouseByName(String clearingPoolName);
	List<BankGroup> getAllBankGroups();
	List<ServiceChargeSplit> getServiceChargeSplits(Integer bankId);
	List<BankAgreementModel> viewBankAgreementDates();
	List<Bank> getBanksByGroupId(Integer bankGroupId);
	Page getBanksByGroupId(Integer bankGroupId,Integer pageNumber);
	BankGroupAdmin getBankGroupByUsername(String userName);
	List<Bank> getAllBanksByName();
	
	List<AccountHead> getAccountHeadByBank();
	List<AccountHead> getAccountHeadsForBook(Integer bookId) ;
	Branch verifyBranchCode(String branchCode, Integer countryId);
	Bank getBankByIdCode(Integer bankId, String bankCode);
	Branch getBranchByCode(String branchCode,Integer bankId);
	Branch getBranchFromLocation(String location,Integer bankId);
	Branch getBranchFromLocation(Long branchId,String location,Integer bankId);
	Branch getBranchesByIdCode(Long branchId, String branchCode,Integer bankId);
	
	CustomerCard getCardDetailsByBankId(Integer bankId);
	CustomerCard getCardDetailsByBankId(Integer bankId,String cardNo);
	CustomerCard getBankVertualCard(String cardNumber);
	Integer getMaxPoolPriority();
	ClearingHousePoolMember getClearingHousePoolMember(Integer clearingPoolId);
	Bank getBankByName(String bankName);
	Bank getBankByName(Integer bankId,String bankName);
	Bank getBankCode(Integer bankId);
	Bank getEOTCardBankCode(String eotCardBankCode);
	Bank getEOTCardBankCode(Integer bankId,String eotCardBankCode);
	Branch getBranchCode(Long branchId);
	List<Branch> getAllBranchFromBank(Integer bankId);
	Page searchBank(Integer bankGroupId,BankDTO bankDTO, int pageNumber);
	Page getBranchList(Integer bankId, int pageNumber);
	Page getSearchClearingHouseList(ClearingHouseDTO clearingHouseDTO,int pageNumber);
	List<Bank> getBankList();
	List<Branch> getBranchList();
	void updateSettlementBatchList(Integer poolId);
	List<Branch> getbranchList(Integer bankId);
	ClearingHousePool getClearingHouseByMobilenumber(String mobileNumber);
}
