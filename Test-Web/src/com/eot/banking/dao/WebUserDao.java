package com.eot.banking.dao;

import java.util.List;

import com.eot.banking.dto.ForgotPasswordDTO;
import com.eot.banking.dto.OtpDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.Country;
import com.eot.entity.Otp;
import com.eot.entity.WebRequest;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;

public interface WebUserDao extends  BaseDao/*,UserDetailsService*/ {

	List<WebUserRole> getRoleList();

	Page getUserList(Integer pageNumber);

	WebUser getUser(String userName);
	
	BankTellers getBankTeller(String userName);
	
	Page getUsersByBankId(Integer bankId,Integer pageNumber);
	
	Page getCCUsers(Integer pageNumber);
	
	Otp verifyOTP(OtpDTO otpDTO);
	
	List getTxnSummaryList(TxnSummaryDTO txnSummaryDTO);
	
	Page getTxnSummary(TxnSummaryDTO txnSummaryDTO,Integer pageNumber);
	
	List<Bank> getBankByGroupId(Integer groupId);
	
	BankGroupAdmin getbankGroupAdmin(String userName);
	
	Page getUsersByBankGroupId(Integer bankGroupId, Integer pageNumber);
	
	WebUser getPassword(ForgotPasswordDTO passworDTO);
	
	String getWebUserId(Integer roleId);
	
	String getIdForBankAdmin(Integer roleId);

	List<WebUser> getWebUserList();
	List<Country> getCountryList(String language);
	
	Page getSearchWebUserList(WebUserDTO webUserDTO , Integer pageNumber);
	Page getUsersByBankId(Integer bankId,WebUserDTO webUserDTO,Integer pageNumber);

	List<Bank> getBanksByCountry(Integer countryId);
	
	WebRequest getUser(Long transactionId);

	Page getUsersByType(Integer bankId, WebUserDTO webUserDTO, int pageNumber);

	Page getUserByBankGroupId(Integer bankGroupId, int pageNumber, WebUserDTO webUserDTO);

	Integer getRoleId(String userName);

	BusinessPartner getBusinessPartnerByUserName(String userName);
	
	Page getWebUserByRoleID(Integer BProleID, Integer pageNumber, WebUserDTO webUserDTO, String seniorPartner,Integer id);
	
}
