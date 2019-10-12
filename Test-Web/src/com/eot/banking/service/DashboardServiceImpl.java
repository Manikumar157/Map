package com.eot.banking.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.DashboardDao;
import com.eot.banking.dto.DashboardDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.DateUtil;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.WebUser;

@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	private DashboardDao dashboardDao;
	
	/** The web user service. */
	@Autowired
	private WebUserService webUserService;
	
	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;
	
	@Override
	public DashboardDTO getDailyTransaction(DashboardDTO dashboardDTO) {
		dashboardDTO.setTodaysDate(DateUtil.txnDate(new Date()));
		return dashboardDao.getDailyTransaction(dashboardDTO);
	}

	@Override
	public DashboardDTO getTransactionSummary(DashboardDTO dashboardDTO) {
		dashboardDTO.setTodaysDate(DateUtil.txnDate(new Date()));
		return dashboardDao.getTransactionSummary(dashboardDTO);
	}

	@Override
	public DashboardDTO getCommmissionSahre(DashboardDTO dashboardDTO) {
		dashboardDTO.setTodaysDate(DateUtil.txnDate(new Date()));
		return dashboardDao.getCommmissionSahre(dashboardDTO);
	}

	@Override
	public DashboardDTO getMobileUserStatistics(DashboardDTO dashboardDTO) {
		dashboardDTO.setTodaysDate(DateUtil.txnDate(new Date()));
		return dashboardDao.getMobileUserStatistics(dashboardDTO);
	}

	@Override
	public List<BusinessPartner> loadBusinessPartnerByType(Integer partnerType) throws Exception{
		String userName = SecurityContextHolder.getContext().getAuthentication().getName() ;
		WebUser  webUser =webUserService.getWebUserByUserName(userName);
		Integer bankId = null;
		if ( webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN
				|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER) {	
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
			if(teller == null){
				throw new EOTException(ErrorConstants.INVALID_TELLER);
			}
		 bankId = teller.getBank().getBankId();
		}		
		return dashboardDao.loadBusinessPartnerByType(partnerType,bankId);
	}

	@Override
	public List<Map<String, Object>> loadAccBalaceByUserId(String loginUser,Integer roleId) {
		return dashboardDao.loadAccBalaceByUserId(loginUser, roleId);
	}

}
