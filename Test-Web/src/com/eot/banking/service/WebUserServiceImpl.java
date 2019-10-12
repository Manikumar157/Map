package com.eot.banking.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.jpos.iso.ISOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.common.UrlConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.BankGroupDao;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.RoleAndPrivilegeDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.ForgotPasswordDTO;
import com.eot.banking.dto.OtpDTO;
import com.eot.banking.dto.PrivilegeDTO;
import com.eot.banking.dto.SmsResponseDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.entity.acegi.WebUserAcegi;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.AccessTypeEnum;
import com.eot.banking.utils.AddRemovePrivilegeFlagEnum;
import com.eot.banking.utils.ApplicationTypeEEnum;
import com.eot.banking.utils.EOTUtil;
import com.eot.banking.utils.HashUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.ReferenceTypeEEnum;
import com.eot.dtos.sms.MobileAppUserAlertDTO;
import com.eot.dtos.sms.SmsHeader;
import com.eot.dtos.sms.WebOTPAlertDTO;
import com.eot.dtos.sms.WebResetPasswordAlertDTO;
import com.eot.dtos.sms.WebUsernamePasswordAlertDTO;
import com.eot.entity.Bank;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.Country;
import com.eot.entity.LoginPrivilegeMapping;
import com.eot.entity.Otp;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;
import com.eot.smsclient.SmsServiceException;
import com.eot.smsclient.webservice.SmsServiceClientStub;

@Service("webUserService")
@Transactional(readOnly = true)
public class WebUserServiceImpl implements WebUserService, UserDetailsService {

	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private BankDao bankDao;
	/* @Autowired
	 * private SmsServiceClientStub smsServiceClientStub; */
	@Autowired
	private BankGroupDao bankGroupDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private RoleAndPrivilegeDao roleAndPrivilegeDao;
	@Autowired
	BusinessPartnerDao businessPartnerDao;
	
	@Autowired
	private RestTemplate restTemplate;

	public void setWebUserDao(WebUserDao webUserDao) {
		this.webUserDao = webUserDao;
	}

	public void setRoleAndPrivilegeDao(RoleAndPrivilegeDao roleAndPrivilegeDao) {
		this.roleAndPrivilegeDao = roleAndPrivilegeDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {

		WebUser webUser = webUserDao.getUser(userName);

		if (webUser != null) {
			WebUserAcegi acegi = new WebUserAcegi(webUser);
			acegi.setPrivileges(preparePrivilegesList(webUser));
			return acegi;
		} else {
			throw new UsernameNotFoundException("ERROR_5010");
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public String saveUser(WebUserDTO webUserDTO, Integer roleId, String username) throws EOTException {

		/*
		 * WebUser webUser = webUserDao.getUser(webUserDTO.getUserName());
		 * 
		 * if(webUser!=null){ throw new EOTException(ErrorConstants.USER_NAME_EXIST); }
		 */

		WebUser webUser = new WebUser();
		Bank bank = new Bank();
		Branch branch = new Branch();
		BankGroup bankGroup = new BankGroup();
		WebUserRole userRole = new WebUserRole();
		userRole.setRoleId(webUserDTO.getRoleId());
		webUser.setWebUserRole(userRole);
		String password = null;
		Country countryIsdCode = null;
		Integer loginPin  =null;

		try {

			String userId = webUserDao.getWebUserId(webUserDTO.getRoleId());
			userId = ISOUtil.zeropad(userId, 6);
			countryIsdCode = locationDao.getCountry(webUserDTO.getCountryId());

			/* @vinod joshi, 7/9/2018 Cheaking condition's And Logical Name of WebUsers */
			/* Strat */
			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_EOT_ADMIN || webUserDTO.getRoleId() == EOTConstants.ROLEID_CC_ADMIN || webUserDTO.getRoleId() == EOTConstants.ROLEID_CC_EXECUTIVE 
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_LEAD || webUserDTO.getRoleId() == EOTConstants.ROLEID_EOT_BACKOFFICE_EXEC 
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_EOT_SUPPORT_LEAD || webUserDTO.getRoleId() == EOTConstants.ROLEID_EOT_SUPPORT_EXEC 
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_BA_ADMIN || webUserDTO.getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUserDTO.getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS || webUserDTO.getRoleId() == EOTConstants.ROLEID_BULKPAYMENT_PARTNER) {
				
				if (roleId == EOTConstants.ROLEID_EOT_ADMIN) {
					// webUser.setUserName("EOT"+userId);
					webUser.setUserName("mGurush" + userId);
				} 
				else if (roleId == EOTConstants.ROLEID_BA_ADMIN || roleId == EOTConstants.ROLEID_BANK_ADMIN) {
					webUser.setUserName("BPL1" + userId);
				} else if (roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L1) {
					if (webUserDTO.getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS) {
						webUser.setUserName("SOF" + userId);
					} else {
						webUser.setUserName("BPL2" + userId);
					}
				} else if (roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
					if (webUserDTO.getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS) {
						webUser.setUserName("SOF" + userId);
					} else {
						webUser.setUserName("BPL3" + userId);
					}
				} else if (roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
					webUser.setUserName("SOF" + userId);
				} else {
					webUser.setUserName("EOT" + userId);
				}
				if(roleId == EOTConstants.ROLEID_BANK_ADMIN){
					if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BULKPAYMENT_PARTNER) {
						webUser.setUserName("BLKPP" + userId);
					}} 
				
			}
			/* End */

			bank = bankDao.getBankCode(webUserDTO.getBankId());
			branch = bankDao.getBranchCode(webUserDTO.getBranchId());

			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {

				bankGroup = bankGroupDao.getBankGroup(webUserDTO.getBankGroupId());
				webUser.setUserName(bankGroup.getBankGroupShortName().replace(" ", "") + userId);
			}

			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {
				bank = bankDao.getBankCode(webUserDTO.getBank());

				webUser.setUserName(bank.getBankCode().replace(" ", "") + userId);
			}

			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_ADMIN ||webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUserDTO.getRoleId() == EOTConstants.ROLEID_ACCOUNTING
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUserDTO.getRoleId() == EOTConstants.ROLEID_OPERATIONS
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER || webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER ) {
				switch (webUserDTO.getRoleId()) {
				case 2:
					webUser.setUserName(EOTConstants.BANK_ADMIN_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_BANK_ADMIN), 3)));
					break;
				case 3:	
					webUser.setUserName(EOTConstants.BANK_SUPERVISOR_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_BANK_TELLER), 3)));
					break;
				case 10:	
					webUser.setUserName(EOTConstants.BANK_ACCOUNTING_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_ACCOUNTING), 3)));
					break;
				case 16:	
					webUser.setUserName(EOTConstants.BANK_SUPPORT_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_SUPPORT_BANK), 3)));
					break;
				case 22:	
					webUser.setUserName(EOTConstants.BANK_OPERATION_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_OPERATIONS), 3)));
					break;
				case 29:	
					webUser.setUserName(EOTConstants.BANK_COMMERCIAL_OFF_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_COMMERCIAL_OFFICER), 3)));
					break;
				case 30:	
					webUser.setUserName(EOTConstants.BANK_SUPPORT_CALL_INITIAL.concat(ISOUtil.zeropad(webUserDao.getIdForBankAdmin(EOTConstants.ROLEID_SUPPORT_CALL_CENTER), 3)));
					break;
				default:
					break;
				}
				
			}

			if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER 
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_PARAMETER || webUserDTO.getRoleId() == EOTConstants.ROLEID_AUDIT  || webUserDTO.getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK 
					) {

				webUser.setUserName(bank.getBankCode().replace(" ", "").concat(branch == null ? "" : branch.getBranchCode()).replace(" ", "").concat(userId));

			}

			password = EOTUtil.generateWebUserPassword();
			String passwordHash = "";

			passwordHash = HashUtil.generateHash(password.getBytes(), EOTConstants.PIN_HASH_ALGORITHM);

			webUser.setPassword(passwordHash);
			webUser.setCredentialsExpired(webUserDTO.getStatus());
			webUser.setAccountLocked(EOTConstants.WEB_USER_UNLOCKED);
			webUser.setTFA(webUserDTO.getTFA());
			webUser.setFirstName(webUserDTO.getFirstName());
			webUser.setLastName(webUserDTO.getLastName());
			webUser.setMiddleName(webUserDTO.getMiddleName());
			webUser.setCreatedDate(new Date());
			webUser.setMobileNumber(webUserDTO.getMobileNumber());
			webUser.setAlternateNumber(EOTConstants.NEW_WEB_USER);
			webUser.setEmail(webUserDTO.getEmail());
			webUser.setDefaultLanguage(webUserDTO.getLanguage());
			webUser.setLoginAttempts(EOTConstants.LOGIN_ATTEMPTS);
			Country country = new Country();
			country.setCountryId(webUserDTO.getCountryId());
			webUser.setCountry(country);
			Serializable i = webUserDao.save(webUser);
			WebUser user = webUserDao.getUser(i + "");
			BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(username);

			loginPin = EOTUtil.generateLoginPin();
			
			/*@ date:08/02/19 by:vinod joshi for:Triggring the SMS based on access mode*/
			/*@ start*/
			
			if(webUserDTO.getAccessMode()!=0 && webUserDTO.getRoleId() != EOTConstants.ROLEID_SALES_OFFICERS && webUserDTO.getRoleId() != EOTConstants.ROLE_ID_SUPPORT
				&&	webUserDTO.getRoleId() != EOTConstants.ROLEID_ACCOUNTING && webUserDTO.getRoleId() != EOTConstants.ROLEID_OPERATIONS
				&&	webUserDTO.getRoleId() != EOTConstants.ROLEID_BANK_TELLER && webUserDTO.getRoleId() != EOTConstants.ROLEID_BANK_ADMIN && webUserDTO.getRoleId() != EOTConstants.ROLEID_BANK_GROUP_ADMIN
				&&	webUserDTO.getRoleId() != EOTConstants.ROLEID_COMMERCIAL_OFFICER &&	webUserDTO.getRoleId() != EOTConstants.ROLEID_SUPPORT_CALL_CENTER){
				if(webUserDTO.getAccessMode()==EOTConstants.ACCESS_MODE_ALL){
					System.out.println("Access-mode all");
						WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
						dto.setUsername(webUser.getUserName());
						dto.setLocale(webUserDTO.getLanguage());
						dto.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
						dto.setPassword(password);
						dto.setScheduleDate(Calendar.getInstance());

//						smsServiceClientStub.webUsernamePasswordAlert(dto);
						sendSMS(UrlConstants.WEB_USERNAME_PASSWORD_ALERT, dto);
						
						MobileAppUserAlertDTO mobileAppUserAlertDTO = new MobileAppUserAlertDTO();
						mobileAppUserAlertDTO.setDownloadLink("https://app.mgurush.com");
						mobileAppUserAlertDTO.setUserName(user.getUserName());
						mobileAppUserAlertDTO.setMobilePin(loginPin+"");
						mobileAppUserAlertDTO.setScheduleDate(Calendar.getInstance());
						mobileAppUserAlertDTO.setLocale(webUserDTO.getLanguage());
						mobileAppUserAlertDTO.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
//						smsServiceClientStub.mobileAppUserAlert(mobileAppUserAlertDTO);
						
						SmsResponseDTO responseDTO=sendSMS(UrlConstants.MOBILE_APP_USER_ALERT, mobileAppUserAlertDTO);
						if(responseDTO.getStatus().equalsIgnoreCase("0"))
							throw new EOTException(ErrorConstants.SERVICE_ERROR);
						//Have to call smsServiceClientStub.mobileAppUserAlert();
						

					
				
				}else if(webUserDTO.getAccessMode()==EOTConstants.ACCESS_MODE_MOBILE){
					System.out.println("Access-mode mobile");
					
					try {
						MobileAppUserAlertDTO mobileAppUserAlertDTO = new MobileAppUserAlertDTO();
						mobileAppUserAlertDTO.setDownloadLink("https://app.mgurush.com");
						mobileAppUserAlertDTO.setUserName(user.getUserName());
						mobileAppUserAlertDTO.setMobilePin(loginPin+"");
						mobileAppUserAlertDTO.setScheduleDate(Calendar.getInstance());
						mobileAppUserAlertDTO.setLocale(webUserDTO.getLanguage());
						mobileAppUserAlertDTO.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
//						smsServiceClientStub.mobileAppUserAlert(mobileAppUserAlertDTO);
						sendSMS(UrlConstants.MOBILE_APP_USER_ALERT, mobileAppUserAlertDTO);
						//Have to call smsServiceClientStub.mobileAppUserAlert();

					} catch (Exception e) {
						throw new EOTException(ErrorConstants.SERVICE_ERROR);
					}
					
				}else if (webUserDTO.getAccessMode()==EOTConstants.ACCESS_MODE_WEB) {
					System.out.println("Access-mode web");
						WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
						dto.setUsername(webUser.getUserName());
						dto.setLocale(webUserDTO.getLanguage());
						dto.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
						dto.setPassword(password);
						dto.setScheduleDate(Calendar.getInstance());

//						smsServiceClientStub.webUsernamePasswordAlert(dto);
						
						SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_USERNAME_PASSWORD_ALERT, dto);
						if(responseDTO.getStatus().equalsIgnoreCase("0"))
							throw new EOTException(ErrorConstants.SERVICE_ERROR);

				}
			}
			/* @End*/
			
			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_SALES_OFFICERS) {
				Integer id = businessPartnerUser.getBusinessPartner().getId();
				Long bid = id.longValue();
				BusinessPartner businessPartner = businessPartnerDao.getBusinessPartner(bid);
				businessPartnerUser = new BusinessPartnerUser();
				businessPartnerUser.setUserName(user.getUserName());
				// businessPartnerUser.setUserName(user);
				businessPartnerUser.setWebUser(webUser);
				businessPartnerUser.setBusinessPartner(businessPartner);
				businessPartnerUser.setAccessMode(webUserDTO.getAccessMode());
				businessPartnerUser.setStatus(10);
				businessPartnerUser.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

				// System.out.println("adding business partner");
				webUserDao.save(businessPartnerUser);
				
				try {
					MobileAppUserAlertDTO mobileAppUserAlertDTO = new MobileAppUserAlertDTO();
					mobileAppUserAlertDTO.setDownloadLink("https://app.mgurush.com");
					mobileAppUserAlertDTO.setUserName(user.getUserName());
					mobileAppUserAlertDTO.setMobilePin(loginPin+"");
					mobileAppUserAlertDTO.setScheduleDate(Calendar.getInstance());
					mobileAppUserAlertDTO.setLocale(webUserDTO.getLanguage());
					mobileAppUserAlertDTO.setMobileNo(webUserDTO.getMobileNumber());
				}
				catch (Exception e) {
					// TODO: handle exception
				}

			}
			/* changed by bidyut */

			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3 || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BA_ADMIN|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BULKPAYMENT_PARTNER ) {
				/* vinod chnage */
				businessPartnerUser = new BusinessPartnerUser();
				businessPartnerUser.setUserName(user.getUserName());
				// businessPartnerUser.setUserName(user);
				businessPartnerUser.setWebUser(webUser);
				if(webUserDTO.getBusinessPartnerId()!="" && webUserDTO.getBusinessPartnerId()!=null){
				businessPartnerUser.setBusinessPartner(businessPartnerDao.getBusinessPartnerbyId(webUserDTO.getBusinessPartnerId()));
				}else{
				businessPartnerUser.setBusinessPartner(businessPartnerDao.getBusinessPartnerbyId(webUserDTO.getBulkPaymentPartnerId()));	
				}
				businessPartnerUser.setAccessMode(webUserDTO.getAccessMode());
				businessPartnerUser.setStatus(10);
				businessPartnerUser.setLoginPin(HashUtil.generateHash(loginPin.toString().getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

				// System.out.println("adding business partner");
				webUserDao.save(businessPartnerUser);
			}
			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER 
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE 
					|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUserDTO.getRoleId() == EOTConstants.ROLEID_PARAMETER || webUserDTO.getRoleId() == EOTConstants.ROLEID_AUDIT
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUserDTO.getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_ADMIN 
					|| webUserDTO.getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUserDTO.getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER || webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {

				BankTellers teller = new BankTellers();

				bank.setBankId(webUserDTO.getBankId());
				teller.setBank(bank);

				branch.setBranchId(webUserDTO.getBranchId());
				teller.setBranch(branch);
				teller.setUserName(webUser.getUserName());
				teller.setWebUser(webUser);

				webUserDao.save(teller);
			}
			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {

				BankTellers teller = new BankTellers();
				bank.setBankId(webUserDTO.getBank());
				teller.setBank(bank);
				teller.setUserName(webUser.getUserName());
				teller.setWebUser(webUser);

				webUserDao.save(teller);
			}
			if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {

				BankGroupAdmin bankGroupAdmin = new BankGroupAdmin();

				bankGroup = bankGroupDao.getBankGroup(webUserDTO.getBankGroupId());
				bankGroup.setBankGroupId(webUserDTO.getBankGroupId());
				bankGroupAdmin.setBankGroup(bankGroup);
				bankGroupAdmin.setUserName(webUser.getUserName());

				webUserDao.save(bankGroupAdmin);
			}
			
			if(webUserDTO.getRoleId()==EOTConstants.ROLEID_BANK_GROUP_ADMIN || webUserDTO.getRoleId()==EOTConstants.ROLEID_BANK_ADMIN || webUserDTO.getRoleId()==EOTConstants.ROLEID_BANK_TELLER
					|| webUserDTO.getRoleId()==EOTConstants.ROLEID_ACCOUNTING || webUserDTO.getRoleId()==EOTConstants.ROLEID_OPERATIONS || webUserDTO.getRoleId()==EOTConstants.ROLE_ID_SUPPORT
					|| webUserDTO.getRoleId()==EOTConstants.ROLEID_SALES_OFFICERS || webUserDTO.getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER || webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER){
				WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
				dto.setUsername(webUser.getUserName());
				dto.setLocale(webUserDTO.getLanguage());
				dto.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
				dto.setPassword(password);
				dto.setScheduleDate(Calendar.getInstance());

//				smsServiceClientStub.webUsernamePasswordAlert(dto);
				SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_USERNAME_PASSWORD_ALERT, dto);
				if(responseDTO.getStatus().equalsIgnoreCase("0"))
					throw new EOTException(ErrorConstants.SERVICE_ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

		/*if (webUserDTO.getRoleId() != EOTConstants.ROLEID_SALES_OFFICERS) {
			try {
				WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
				dto.setUsername(webUser.getUserName());
				dto.setLocale(webUserDTO.getLanguage());
				dto.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
				dto.setPassword(password);
				dto.setScheduleDate(Calendar.getInstance());

				smsServiceClientStub.webUsernamePasswordAlert(dto);

			} catch (SmsServiceException e) {
				throw new EOTException(ErrorConstants.SERVICE_ERROR);
			}
		}
		if(webUserDTO.getRoleId() != EOTConstants.ROLEID_SALES_OFFICERS)
		{
			try {
				MobileAppUserAlertDTO mobileAppUserAlertDTO = new MobileAppUserAlertDTO();
				mobileAppUserAlertDTO.setDownloadLink("http://128.199.48.185:7070/mGurush-Web/");
				mobileAppUserAlertDTO.setUserName(webUser.getUserName());
				mobileAppUserAlertDTO.setMobilePin(loginPin+"");
				mobileAppUserAlertDTO.setScheduleDate(Calendar.getInstance());
				mobileAppUserAlertDTO.setLocale(webUserDTO.getLanguage());
				mobileAppUserAlertDTO.setMobileNo(webUserDTO.getMobileNumber());
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		}*/
		return webUser.getUserName();

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void updateUser(WebUserDTO webUserDTO) throws EOTException {

		WebUser webUser = webUserDao.getUser(webUserDTO.getUserName());
		String password = null;
		Country countryIsdCode = null;
		Integer loginPin  =null;
		password = EOTUtil.generateWebUserPassword();
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		 WebUser webUserlogin =getWebUserByUserName(userName);

		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		webUser.setFirstName(webUserDTO.getFirstName());
		webUser.setMiddleName(webUserDTO.getMiddleName());
		webUser.setLastName(webUserDTO.getLastName());
		webUser.setMobileNumber(webUserDTO.getMobileNumber());
		webUser.setAlternateNumber(EOTConstants.ACTIVE_WEB_USER);
		webUser.setEmail(webUserDTO.getEmail());
		webUser.setDefaultLanguage(webUserDTO.getLanguage());
		webUser.setCredentialsExpired(webUserDTO.getStatus());
		webUser.setTFA(webUserDTO.getTFA());
		WebUserRole userRole = new WebUserRole();
		userRole.setRoleId(webUserDTO.getRoleId());
		webUser.setWebUserRole(userRole);
		Country country = new Country();
		country.setCountryId(webUserDTO.getCountryId());
		webUser.setCountry(country);
		webUserDao.update(webUser);
		WebUser user = webUserDao.getUser(webUserDTO.getUserName());
		BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(webUserDTO.getUserName());
		String userId = webUserDao.getWebUserId(EOTConstants.ROLEID_BANK_ADMIN);
		countryIsdCode = locationDao.getCountry(webUserDTO.getCountryId());
		loginPin = EOTUtil.generateLoginPin();
		/*	@ date:08/02/19 by:vinod joshi for:Triggring the SMS based on access mode
		@ start*/
		if(webUserlogin.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN || webUserlogin.getWebUserRole().getRoleId()==EOTConstants.BUSINESS_PARTNER_TYPE_L1 || webUserlogin.getWebUserRole().getRoleId()==EOTConstants.BUSINESS_PARTNER_TYPE_L2
				||webUserlogin.getWebUserRole().getRoleId()==EOTConstants.BUSINESS_PARTNER_TYPE_L3){
		if(webUserDTO.getAccessMode()!=0  && webUserDTO.getRoleId() != EOTConstants.ROLEID_COMMERCIAL_OFFICER
				&& webUserDTO.getRoleId() != EOTConstants.ROLEID_SUPPORT_CALL_CENTER && webUserDTO.getRoleId() != EOTConstants.ROLEID_SUPPORT_CALL_CENTER && webUserDTO.getRoleId() != EOTConstants.ROLEID_BANK_TELLER
				&& webUserDTO.getRoleId() != EOTConstants.ROLEID_ACCOUNTING && webUserDTO.getRoleId() != EOTConstants.ROLEID_SUPPORT_BANK && webUserDTO.getRoleId() != EOTConstants.ROLEID_OPERATIONS){
		if(webUserDTO.getAccessMode()!=0 && webUserDTO.getRoleId() != EOTConstants.ROLEID_SALES_OFFICERS){
			if(businessPartnerUser.getAccessMode()!=webUserDTO.getAccessMode() && webUserDTO.getAccessMode()==EOTConstants.ACCESS_MODE_ALL){
				System.out.println("Access-mode all");
					WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
					dto.setUsername(webUser.getUserName());
					dto.setLocale(webUserDTO.getLanguage());
					dto.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
					dto.setPassword(password);
					dto.setScheduleDate(Calendar.getInstance());

//					smsServiceClientStub.webUsernamePasswordAlert(dto);
					sendSMS(UrlConstants.WEB_USERNAME_PASSWORD_ALERT, dto);
					
					MobileAppUserAlertDTO mobileAppUserAlertDTO = new MobileAppUserAlertDTO();
					mobileAppUserAlertDTO.setDownloadLink("https://app.mgurush.com");
					mobileAppUserAlertDTO.setUserName(user.getUserName());
					mobileAppUserAlertDTO.setMobilePin(loginPin+"");
					mobileAppUserAlertDTO.setScheduleDate(Calendar.getInstance());
					mobileAppUserAlertDTO.setLocale(webUserDTO.getLanguage());
					mobileAppUserAlertDTO.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
//					smsServiceClientStub.mobileAppUserAlert(mobileAppUserAlertDTO);
					
					SmsResponseDTO responseDTO=sendSMS(UrlConstants.MOBILE_APP_USER_ALERT, mobileAppUserAlertDTO);
					if(responseDTO.getStatus().equalsIgnoreCase("0"))
						throw new EOTException(ErrorConstants.SERVICE_ERROR);
					
				
			
			}else if(businessPartnerUser.getAccessMode()!=webUserDTO.getAccessMode() && webUserDTO.getAccessMode()==EOTConstants.ACCESS_MODE_MOBILE){
				System.out.println("Access-mode mobile");
				
					MobileAppUserAlertDTO mobileAppUserAlertDTO = new MobileAppUserAlertDTO();
					mobileAppUserAlertDTO.setDownloadLink("https://app.mgurush.com");
					mobileAppUserAlertDTO.setUserName(user.getUserName());
					mobileAppUserAlertDTO.setMobilePin(loginPin+"");
					mobileAppUserAlertDTO.setScheduleDate(Calendar.getInstance());
					mobileAppUserAlertDTO.setLocale(webUserDTO.getLanguage());
					mobileAppUserAlertDTO.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
//					smsServiceClientStub.mobileAppUserAlert(mobileAppUserAlertDTO);
					SmsResponseDTO responseDTO=sendSMS(UrlConstants.MOBILE_APP_USER_ALERT, mobileAppUserAlertDTO);
					if(responseDTO.getStatus().equalsIgnoreCase("0"))
						throw new EOTException(ErrorConstants.SERVICE_ERROR);
				
			}else if (businessPartnerUser.getAccessMode()!=webUserDTO.getAccessMode() && webUserDTO.getAccessMode()==EOTConstants.ACCESS_MODE_WEB) {
				System.out.println("Access-mode web");
					WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
					dto.setUsername(webUser.getUserName());
					dto.setLocale(webUserDTO.getLanguage());
					dto.setMobileNo(countryIsdCode.getIsdCode() + webUserDTO.getMobileNumber());
					dto.setPassword(password);
					dto.setScheduleDate(Calendar.getInstance());

//					smsServiceClientStub.webUsernamePasswordAlert(dto);
					SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_USERNAME_PASSWORD_ALERT, dto);
					if(responseDTO.getStatus().equalsIgnoreCase("0"))
						throw new EOTException(ErrorConstants.SERVICE_ERROR);
			}
		}
	/*	 @End*/
		businessPartnerUser.setAccessMode(webUserDTO.getAccessMode());
		webUserDao.update(businessPartnerUser);
			}
		}
	
		

		

		if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER 
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE 
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUserDTO.getRoleId() == EOTConstants.ROLEID_PARAMETER 
				|| webUserDTO.getRoleId() == EOTConstants.ROLEID_AUDIT || webUserDTO.getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUserDTO.getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK 
				|| webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUserDTO.getRoleId() == EOTConstants.ROLEID_OPERATIONS
				|| webUserDTO.getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER|| webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());

			if (teller == null) {
				teller = new BankTellers();
			}

			Bank bank = new Bank();
			bank.setBankId(webUserDTO.getBankId());
			teller.setBank(bank);
			Branch branch = new Branch();
			branch.setBranchId(new Long(webUserDTO.getBranchId()));
			teller.setBranch(branch);
			teller.setUserName(webUserDTO.getUserName());
			teller.setWebUser(webUser);

			webUserDao.merge(teller);

		} else {

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());

			if (teller != null) {
				webUserDao.delete(teller);
			}

		}
		if (webUserDTO.getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());

			if (teller == null) {
				teller = new BankTellers();
			}

			Bank bank = new Bank();
			bank.setBankId(webUserDTO.getBank());
			teller.setBank(bank);
			teller.setUserName(webUserDTO.getUserName());
			teller.setWebUser(webUser);

			webUserDao.merge(teller);
		}
		if (webUserDTO.getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin bankGroupAdmin = bankGroupDao.getGroupAdminByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				bankGroupAdmin = new BankGroupAdmin();
			}
			BankGroup bankGroup = new BankGroup();
			bankGroup.setBankGroupId(webUserDTO.getBankGroupId());
			bankGroupAdmin.setBankGroup(bankGroup);
			bankGroupAdmin.setUserName(webUserDTO.getUserName());
			bankGroupAdmin.setWebUser(webUser);
			webUserDao.merge(bankGroupAdmin);
		} else {

			BankGroupAdmin bankGroupAdmin = bankGroupDao.getGroupAdminByUsername(webUser.getUserName());

			if (bankGroupAdmin != null) {
				webUserDao.delete(bankGroupAdmin);
			}
		}
	}

	@Override
	public BusinessPartner getBusinessPartnerByUserName(String userName) {
		BusinessPartner businessPartner = webUserDao.getBusinessPartnerByUserName(userName);

		return businessPartner;
	}

	@Override
	public WebUser getWebUserByUserName(String userName) {

		WebUser webUser = webUserDao.getUser(userName);
		return webUser;
	}

	@Override
	public WebUserDTO getUser(String userName) throws EOTException {

		WebUser webUser = webUserDao.getUser(userName);
		if (webUser == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		Country country = locationDao.getCountry(webUser.getCountry().getCountryId());

		WebUserDTO udto = new WebUserDTO();
		udto.setMobilenumberLength(country.getMobileNumberLength());
		udto.setUserName(webUser.getUserName());
		udto.setFirstName(webUser.getFirstName());
		udto.setMiddleName(webUser.getMiddleName());
		udto.setLastName(webUser.getLastName());
		udto.setMobileNumber(webUser.getMobileNumber().trim());
		udto.setAlternateNumber(webUser.getAlternateNumber());
		udto.setEmail(webUser.getEmail());
		udto.setRoleId(webUser.getWebUserRole().getRoleId());
		udto.setLanguage(webUser.getDefaultLanguage());
		udto.setCreatedDate(webUser.getCreatedDate());
		udto.setStatus(webUser.getCredentialsExpired());
		udto.setAccountLock(webUser.getAccountLocked());
		udto.setTFA(webUser.getTFA());
		udto.setCountryId(webUser.getCountry().getCountryId());
		udto.setCountryName(webUser.getCountry().getCountry());
		udto.setRoleName(webUser.getWebUserRole().getDescription());
		udto.setCreatedDate(webUser.getCreatedDate());

		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER 
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE 
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER 
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING 
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN 
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_OPERATIONS || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_COMMERCIAL_OFFICER
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_CALL_CENTER) {

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			udto.setBankId(teller.getBank().getBankId());
			udto.setBranchId(teller.getBranch().getBranchId());

		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {

			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if (teller == null) {
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
			udto.setBank(teller.getBank().getBankId());

		} else if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {

			BankGroupAdmin bankGroupAdmin = bankGroupDao.getGroupAdminByUsername(webUser.getUserName());
			if (bankGroupAdmin == null) {
				throw new EOTException(ErrorConstants.INVALID_GROUPADMIN);
			}

			udto.setBankGroupId(bankGroupAdmin.getBankGroup().getBankGroupId());

		}
		if (webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BA_ADMIN) {
			return udto;
		}

		return udto;
	}

	@Override
	public Map<String, Object> getMasterData(String userName, int pageNumber) throws EOTException {

		Map<String, Object> model = new HashMap<String, Object>();

		WebUser user = webUserDao.getUser(userName);

		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN) {
			model.put("userRoleList", webUserDao.getRoleList());

			Page page = webUserDao.getUserList(pageNumber);
			page.requestPage = "showUserForm.htm";
			model.put("page", page);
			model.put("bankList", bankDao.getActiveBanks());
			model.put("bankGroupList", bankDao.getAllBankGroups());
		}

		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {

			model.put("userRoleList", webUserDao.getRoleList());
			BankTellers teller = webUserDao.getBankTeller(userName);

			Page page = webUserDao.getUsersByBankId(teller.getBank().getBankId(), pageNumber);
			page.requestPage = "showUserForm.htm";
			model.put("page", page);
			List<Bank> bankList = new ArrayList<Bank>();
			bankList.add(teller.getBank());
			model.put("bankList", bankList);
			model.put("branchList", teller.getBank().getBranches());

		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_CC_ADMIN) {

			model.put("userRoleList", webUserDao.getRoleList());

			Page page = webUserDao.getCCUsers(pageNumber);
			page.requestPage = "showUserForm.htm";
			model.put("page", page);

		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {

			BankGroupAdmin groupAdmin = webUserDao.getbankGroupAdmin(userName);
			List<Bank> bankList = bankDao.getBanksByGroupId(groupAdmin.getBankGroup().getBankGroupId());
			model.put("bankList", bankList);
			Page page = webUserDao.getUsersByBankGroupId(groupAdmin.getBankGroup().getBankGroupId(), pageNumber);
			page.requestPage = "showUserForm.htm";
			model.put("page", page);
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN) {

			model.put("userRoleList", webUserDao.getRoleList());
			BankTellers teller = webUserDao.getBankTeller(userName);

			Page page = webUserDao.getUsersByBankId(teller.getBank().getBankId(), pageNumber);
			page.requestPage = "showUserForm.htm";
			model.put("page", page);
			List<Bank> bankList = new ArrayList<Bank>();
			bankList.add(teller.getBank());
			model.put("bankList", bankList);

		}
		return model;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void changePassword(String username, String oldPassword, String newPassword) throws EOTException {

		WebUser user = webUserDao.getUser(username);

		if (user == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		String oldPasswordHash = "";
		String newPasswordHash = "";

		try {
			oldPasswordHash = HashUtil.generateHash(oldPassword.getBytes(), EOTConstants.PIN_HASH_ALGORITHM);
			newPasswordHash = HashUtil.generateHash(newPassword.getBytes(), EOTConstants.PIN_HASH_ALGORITHM);
		} catch (Exception e1) {
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

		if (!user.getPassword().equals(oldPasswordHash)) {
			throw new EOTException(ErrorConstants.PASSWORD_NOT_VALID);
		}
		user.setAlternateNumber(EOTConstants.ACTIVE_WEB_USER);
		user.setPassword(newPasswordHash);
		webUserDao.update(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Branch> getBranchList(Integer bankId) {
		return bankDao.getBank(bankId).getBranches();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, noRollbackFor = EOTException.class)
	public void verifyOTP(String otpassword, String userName) throws Exception {

		WebUser user = webUserDao.getUser(userName);

		if (user == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		if (user.getLoginAttempts() > 3) {
			throw new EOTException(ErrorConstants.USER_BLOCKED);
		}

		String otpHash = HashUtil.generateHash(otpassword.getBytes(), EOTConstants.PIN_HASH_ALGORITHM);
		OtpDTO otpDto = new OtpDTO();
		otpDto.setOtphash(otpHash);
		otpDto.setReferenceId(userName);
		otpDto.setReferenceType(EOTConstants.REFERENCE_TYPE_WEB_USER);
		otpDto.setOtpType(EOTConstants.OTP_TYPE_WEB_USER);

		Otp otp = webUserDao.verifyOTP(otpDto);

		if (otp == null) {

			int loginAttempts = user.getLoginAttempts();

			user.setLoginAttempts(++loginAttempts);

			if (loginAttempts > 3) {
				user.setAccountLocked(EOTConstants.WEB_USER_LOCKED);
			}

			webUserDao.update(user);

			throw new EOTException(ErrorConstants.INVALID_OTP);
		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public boolean generateOTPIfEnabled(String userName) throws SmsServiceException, EOTException {

		boolean otpFlag = false;

		WebUser user = webUserDao.getUser(userName);

		if (user == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		if (user.getLoginAttempts() > 0) {

			user.setLoginAttempts(0);
			user.setAccountLocked(EOTConstants.WEB_USER_CREDENTIALS_NON_EXPIRED);

			webUserDao.update(user);

		}

		if (user.getTFA() == EOTConstants.TFA_ENABLED) {

			WebOTPAlertDTO dto = new WebOTPAlertDTO();
			dto.setLocale(user.getDefaultLanguage());
			dto.setMobileNo(user.getCountry().getIsdCode() + user.getMobileNumber());
			dto.setOtpType(EOTConstants.OTP_TYPE_WEB_USER);
			dto.setReferenceId(user.getUserName());
			dto.setReferenceType(EOTConstants.REFERENCE_TYPE_WEB_USER);
			dto.setScheduleDate(Calendar.getInstance());

//			smsServiceClientStub.webOTPAlert(dto);
			sendSMS(UrlConstants.WEB_OTP_ALERT, dto);
			
			otpFlag = true;

		}

		return otpFlag;

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void resetPassword(String username) throws EOTException {
		WebUser user = webUserDao.getUser(username);
		if (user == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}

		try {

			String password = EOTUtil.generateWebUserPassword();

			user.setAccountLocked(EOTConstants.WEB_USER_UNLOCKED);
			user.setCredentialsExpired(EOTConstants.WEB_USER_CREDENTIALS_NON_EXPIRED);
			user.setPassword(HashUtil.generateHash(password.getBytes(), EOTConstants.PIN_HASH_ALGORITHM));

			webUserDao.update(user);

			WebResetPasswordAlertDTO dto = new WebResetPasswordAlertDTO();
			dto.setLocale(user.getDefaultLanguage());
			dto.setMobileNo(user.getCountry().getIsdCode() + user.getMobileNumber());
			dto.setPassword(password);
			dto.setScheduleDate(Calendar.getInstance());

//			smsServiceClientStub.webResetPasswordAlert(dto);
			SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_RESET_PASSWORD_ALERT, dto);

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void unBlockUser(String username) throws EOTException {

		WebUser user = webUserDao.getUser(username);

		if (user == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		try {

			user.setAccountLocked(EOTConstants.WEB_USER_UNLOCKED);
			webUserDao.update(user);

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void incrementFailedAttemptCounter(String username) {

		WebUser user = webUserDao.getUser(username);

		if (user != null) {

			int loginAttempts = user.getLoginAttempts();

			user.setLoginAttempts(++loginAttempts);

			if (loginAttempts >= 3) {
				user.setAccountLocked(EOTConstants.WEB_USER_LOCKED);
			}

			webUserDao.update(user);

		}

	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public void forgetPassword(ForgotPasswordDTO passworDTO) throws EOTException {

		WebUser user = webUserDao.getPassword(passworDTO);
		if (user == null) {
			throw new EOTException(ErrorConstants.INVALID_USER);
		}
		String password = EOTUtil.generateWebUserPassword();
		String passwordHash = "";

		try {
			passwordHash = HashUtil.generateHash(password.getBytes(), EOTConstants.PIN_HASH_ALGORITHM);
		} catch (Exception e) {
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}

		user.setPassword(passwordHash);
		webUserDao.update(user);

		WebUsernamePasswordAlertDTO dto = new WebUsernamePasswordAlertDTO();
		dto.setUsername(user.getUserName());
		dto.setLocale(user.getDefaultLanguage());
		dto.setMobileNo(user.getCountry().getIsdCode() + user.getMobileNumber());
		dto.setPassword(password);
		dto.setScheduleDate(Calendar.getInstance());
//			smsServiceClientStub.webUsernamePasswordAlert(dto);
			SmsResponseDTO responseDTO=sendSMS(UrlConstants.WEB_USERNAME_PASSWORD_ALERT, dto);
			if(responseDTO.getStatus().equalsIgnoreCase("0"))
				throw new EOTException(ErrorConstants.SERVICE_ERROR);
	}

	@Override
	public List<Country> getCountryList(String language) {

		return webUserDao.getCountryList(language);
	}

	@Override
	public Country getMobileNumberLength(Integer countryId) {

		return locationDao.getCountry(countryId);
	}

	@Override
	public List<Branch> getAllBranchFromBank(Integer bankId) {
		return bankDao.getAllBranchFromBank(bankId);
	}

	// vinod chnage
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
	public List<BusinessPartner> getAllBusniessPartnerName(Integer roleId, Integer seniorPartnerId, Integer bankId) {

		Integer partnerType = 0;
		/*
		 * if(roleId==EOTConstants.ROLEID_EOT_ADMIN) partnerType=1;
		 */
		if (roleId == EOTConstants.ROLEID_BA_ADMIN || roleId == EOTConstants.ROLEID_BANK_ADMIN)
			partnerType = 2;
		if (roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L1)
			partnerType = 3;
		if (roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L2)
			partnerType = 4;
		if (roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L3)
			partnerType = 5;

		return businessPartnerDao.getAllBusniessPartner(partnerType, seniorPartnerId, bankId);
	}
	// vinod chnage

	@Override
	public Map<String, Object> getSearchWebUserList(WebUserDTO webUserDTO, String userName, Integer pageNumber) throws EOTException {
		Map<String, Object> model = new HashMap<String, Object>();

		WebUser user = webUserDao.getUser(userName);
		Page page = null;

		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN) {
			model.put("userRoleList", webUserDao.getRoleList());

			model.put("bankList", bankDao.getActiveBanks());
			model.put("bankGroupList", bankDao.getAllBankGroups());
		}

		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {

			model.put("userRoleList", webUserDao.getRoleList());
			BankTellers teller = webUserDao.getBankTeller(userName);
			List<Bank> bankList = new ArrayList<Bank>();
			bankList.add(teller.getBank());
			model.put("bankList", bankList);
			model.put("branchList", teller.getBank().getBranches());

		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_CC_ADMIN) {
			model.put("userRoleList", webUserDao.getRoleList());
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin groupAdmin = webUserDao.getbankGroupAdmin(userName);
			List<Bank> bankList = bankDao.getBanksByGroupId(groupAdmin.getBankGroup().getBankGroupId());
			model.put("bankList", bankList);
		}
		model.put("loggedInUser", user.getUserName());
		model.put("loggedInId", user.getWebUserRole().getRoleId());

		return model;
	}

	@Override
	public List<Bank> getBanksByCountry(Integer countryId) {
		return webUserDao.getBanksByCountry(countryId);
	}

	@Override
	public Page getWebUserList(WebUserDTO webUserDTO, String userName, int pageNumber) throws EOTException {

		WebUser user = webUserDao.getUser(userName);
		Page page = null;
		Integer BProleID = null;
		Integer SOFroleID = null;
		String seniorPartner = "";

		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_EOT_ADMIN) {
			page = webUserDao.getSearchWebUserList(webUserDTO, pageNumber);
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {
			BankTellers teller = webUserDao.getBankTeller(userName);
			page = webUserDao.getUsersByBankId(teller.getBank().getBankId(), webUserDTO, pageNumber);
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_CC_ADMIN) {
			page = webUserDao.getCCUsers(pageNumber);
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN) {
			BankGroupAdmin groupAdmin = webUserDao.getbankGroupAdmin(userName);
			List<Bank> bankList = bankDao.getBanksByGroupId(groupAdmin.getBankGroup().getBankGroupId());
			page = webUserDao.getUserByBankGroupId(groupAdmin.getBankGroup().getBankGroupId(), pageNumber, webUserDTO);
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT) {
			BankTellers teller = webUserDao.getBankTeller(userName);
			page = webUserDao.getUsersByType(teller.getBank().getBankId(), webUserDTO, pageNumber);
		}

		/*
		 * if(user.getWebUserRole().getRoleId()==EOTConstants.ROLEID_BANK_ADMIN){
		 * BProleID = 24; page = webUserDao.getWebUserByRoleID(BProleID,pageNumber); }
		 */

		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L1) {
			BProleID = 25;
			BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(userName);
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
			page = webUserDao.getWebUserByRoleID(BProleID, pageNumber, webUserDTO, seniorPartner,businessPartnerUser.getBusinessPartner().getId());
		}
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2) {
			BProleID = 26;
			BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(userName);
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
			page = webUserDao.getWebUserByRoleID(BProleID, pageNumber, webUserDTO, seniorPartner,businessPartnerUser.getBusinessPartner().getId());
		}
		
		if (user.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
			BProleID = 27;
			BusinessPartnerUser businessPartnerUser = businessPartnerDao.getUser(userName);
			seniorPartner = businessPartnerUser.getBusinessPartner().getId() == null ? "" : businessPartnerUser.getBusinessPartner().getId().toString();
			page = webUserDao.getWebUserByRoleID(BProleID, pageNumber, webUserDTO, seniorPartner,businessPartnerUser.getBusinessPartner().getId());
		}
/*
		if (page.results.size() == 0) {
			throw new EOTException(ErrorConstants.WEB_USER_NOT_EXIST);
		}*/

		return page;
	}

	private List<String> preparePrivilegesList(WebUser loginUser) {

		List<LoginPrivilegeMapping> privilegeList = null;
		Map<String, PrivilegeDTO> map = new HashMap<String, PrivilegeDTO>();
		String key = null;
		List<String> privileges = new ArrayList<String>();
		Set<String> apps = new HashSet<String>();
		Set<String> topMenu = new HashSet<String>();
		Set<String> leftMenu = new HashSet<String>();
		List<PrivilegeDTO> privilegeDTOList = null;
		PrivilegeDTO prvDTO = null;

		// Load all selected privileges for post type
		PrivilegeDTO dto = new PrivilegeDTO();
		dto.setReferenceId(loginUser.getWebUserRole().getRoleId().toString());
		dto.setReferenceType(1);
		privilegeList = roleAndPrivilegeDao.loadAddAndRemovedPrivileges(dto);
		if (privilegeList != null) {
			privilegeDTOList = new ArrayList<PrivilegeDTO>();
			for (LoginPrivilegeMapping privilegeMapping : privilegeList) {
				prvDTO = new PrivilegeDTO();
				prvDTO.setAppTypeId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getApplicationType());
				prvDTO.setAccessType(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getAccessType());
				prvDTO.setTopMenuId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuId());
				prvDTO.setTopMenuName(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuName());
				prvDTO.setLeftMenuId(privilegeMapping.getLoginLeftMenu().getLeftMenuId());
				prvDTO.setLeftMenuName(privilegeMapping.getLoginLeftMenu().getLeftMenuName());
				prvDTO.setSubMenuId(privilegeMapping.getLoginSubMenu().getSubMenuId());
				prvDTO.setSubMenuName(privilegeMapping.getLoginSubMenu().getSubMenuName());
				prvDTO.setAddRemovalFlag(privilegeMapping.getAddRemovalFlag());
				privilegeDTOList.add(prvDTO);
			}
		}
		if (CollectionUtils.isNotEmpty(privilegeDTOList)) {
			for (PrivilegeDTO privilegeDTO : privilegeDTOList) {
				if (privilegeDTO.getAccessType().equals(AccessTypeEnum.WEB_ACCESS.getCode())) {
					apps.add(ApplicationTypeEEnum.getApplicationType(privilegeDTO.getAppTypeId()));
					topMenu.add(privilegeDTO.getTopMenuName() + ApplicationTypeEEnum.getApplicationType(privilegeDTO.getAppTypeId()));
					leftMenu.add(privilegeDTO.getLeftMenuName() + privilegeDTO.getTopMenuName() + ApplicationTypeEEnum.getApplicationType(privilegeDTO.getAppTypeId()));
					key = privilegeDTO.getAppTypeId() + "_" + privilegeDTO.getTopMenuId() + "_" + privilegeDTO.getLeftMenuId() + "_" + privilegeDTO.getSubMenuId();
					if (map.get(key) == null) {
						map.put(privilegeDTO.getAppTypeId() + "_" + privilegeDTO.getTopMenuId() + "_" + privilegeDTO.getLeftMenuId() + "_" + privilegeDTO.getSubMenuId(), privilegeDTO);
					}
				}
			}
		}

		// Load the removed privileges for post code
		dto.setReferenceId(loginUser.getUserName());
		dto.setReferenceType(ReferenceTypeEEnum.USER.getCode());
		privilegeList = roleAndPrivilegeDao.loadAddAndRemovedPrivileges(dto);
		if (privilegeList != null) {
			privilegeDTOList = new ArrayList<PrivilegeDTO>();
			for (LoginPrivilegeMapping privilegeMapping : privilegeList) {
				prvDTO = new PrivilegeDTO();
				prvDTO.setAppTypeId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getApplicationType());
				prvDTO.setAccessType(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getAccessType());
				prvDTO.setTopMenuId(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuId());
				prvDTO.setTopMenuName(privilegeMapping.getLoginLeftMenu().getLoginTopMenu().getTopMenuName());
				prvDTO.setLeftMenuId(privilegeMapping.getLoginLeftMenu().getLeftMenuId());
				prvDTO.setLeftMenuName(privilegeMapping.getLoginLeftMenu().getLeftMenuName());
				prvDTO.setSubMenuId(privilegeMapping.getLoginSubMenu().getSubMenuId());
				prvDTO.setSubMenuName(privilegeMapping.getLoginSubMenu().getSubMenuName());
				prvDTO.setAddRemovalFlag(privilegeMapping.getAddRemovalFlag());
				privilegeDTOList.add(prvDTO);
			}
		}
		if (CollectionUtils.isNotEmpty(privilegeDTOList)) {
			for (PrivilegeDTO privilegeDTO : privilegeDTOList) {
				key = privilegeDTO.getAppTypeId() + "_" + privilegeDTO.getTopMenuId() + "_" + privilegeDTO.getLeftMenuId() + "_" + privilegeDTO.getSubMenuId();
				if (privilegeDTO.getAddRemovalFlag().equals(AddRemovePrivilegeFlagEnum.REMOVED.getCode())) {
					map.remove(key);
				}
			}
		}

		// Prepare privilege list
		for (Entry<String, PrivilegeDTO> entry : map.entrySet()) {
			dto = entry.getValue();
			privileges.add(dto.getSubMenuName().toLowerCase() + dto.getLeftMenuName() + dto.getTopMenuName() + ApplicationTypeEEnum.getApplicationType(dto.getAppTypeId()));
		}
		privileges.addAll(apps);
		privileges.addAll(topMenu);
		privileges.addAll(leftMenu);
		privileges.add(loginUser.getWebUserRole().getRoleName());

		return privileges;
	}

	@Override
	public Integer getRoleId(String userName) {
		Integer roleId = webUserDao.getRoleId(userName);
		return roleId;
	}

	@Override
	public List<BusinessPartnerDTO> getAllBusniessPartnerUser() {
		return businessPartnerDao.getAllBusniessPartnerUser();
	}

	@Override
	public List<BusinessPartner> getAllBulkPaymentPartner(Integer roleId, Integer bankId) {
		Integer partnerType = 0;
		if (roleId == EOTConstants.ROLEID_BANK_ADMIN || roleId == EOTConstants.PARTNER_TYPE_BA_ADMIN)
			partnerType = EOTConstants.BULKPAYMENT_PARTNER_TYPE;

		return businessPartnerDao.getAllBulkBusinessPartner(partnerType,bankId);
	}
	
	public SmsResponseDTO sendSMS(String url,SmsHeader smsHeader)
	{
		SmsResponseDTO dto= new SmsResponseDTO();
		try {
			dto= 	restTemplate.postForObject(url, smsHeader, SmsResponseDTO.class);
		}catch (Exception e) {
			dto.setStatus("0");
			e.printStackTrace();
		}
		return dto;
	}
}
