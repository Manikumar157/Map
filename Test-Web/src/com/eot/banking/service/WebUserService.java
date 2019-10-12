package com.eot.banking.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.ForgotPasswordDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Bank;
import com.eot.entity.Branch;
import com.eot.entity.BusinessPartner;
import com.eot.entity.Country;
import com.eot.entity.WebUser;
import com.eot.smsclient.SmsServiceException;

public interface WebUserService {

	String saveUser(WebUserDTO webUserDTO, Integer roleId, String username)throws EOTException;

	void updateUser(WebUserDTO webUserDTO)throws EOTException;

	WebUserDTO getUser(String userName)throws EOTException;

	Map<String,Object> getMasterData(String username,int pageNumber)throws EOTException;
	
	public Set<Branch> getBranchList(Integer bankId);
	
	public List<Branch> getAllBranchFromBank(Integer bankId);
	
	public List<BusinessPartner> getAllBusniessPartnerName(Integer roleId,Integer seniorPartnerId,Integer bankId);
	
	void incrementFailedAttemptCounter(String userName);
	
	void changePassword(String username, String oldPassword, String newPassword) throws EOTException;
	
	void resetPassword(String username) throws EOTException;
	
	void unBlockUser(String username) throws EOTException;
	
	boolean generateOTPIfEnabled(String userName) throws SmsServiceException, EOTException;
	
	void verifyOTP(String otp,String userName) throws Exception;
	
	void forgetPassword(ForgotPasswordDTO passworDTO)throws EOTException;
	List<Country> getCountryList(String language);
	Country getMobileNumberLength(Integer countryId);
	
	Map<String,Object> getSearchWebUserList(WebUserDTO webUserDTO,String userName,Integer pageNumber) throws EOTException;

	List<Bank> getBanksByCountry(Integer countryId);

	Page getWebUserList(WebUserDTO webUserDTO, String userName, int pageNumber)throws EOTException;

	Integer getRoleId(String name);

	BusinessPartner getBusinessPartnerByUserName(String userName);

	WebUser getWebUserByUserName(String userName);

	List<BusinessPartnerDTO> getAllBusniessPartnerUser();
	
	public List<BusinessPartner> getAllBulkPaymentPartner(Integer roleId,Integer bankId);
	
}
