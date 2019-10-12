package com.eot.banking.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dao.TransactionDao;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.Page;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.WebUser;


@Service("txnSummaryService")
@Transactional(readOnly=true)
public class TxnSummaryServiceImpl implements TxnSummaryService {

	@Autowired
	private BankDao bankDao;
	@Autowired
	private WebUserDao webUserDao;
	@Autowired
	private OperatorDao operatorDao;
	@Autowired
	private TransactionDao transactionDao;
	

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List> getTxnSummaryMasterData(String language) throws EOTException {
		try{
			Map model = new HashMap();

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String userName = auth.getName();
			WebUser webUser = webUserDao.getUser(userName);
			if(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_GROUP_ADMIN ){
				BankGroupAdmin bankGroupAdmin = webUserDao.getbankGroupAdmin(userName);
				model.put("bankList", bankDao.getBanksByGroupId(bankGroupAdmin.getBankGroup().getBankGroupId()));
			}else{
				model.put("bankList", bankDao.getActiveBanks());	
			}
			model.put("countryList",operatorDao.getCountries(language));
			model.put("transTypeList", transactionDao.getTrnsactionList(language.substring(0, 2)));

			return model;
		}catch (Exception e) {
			e.printStackTrace();
			throw new EOTException(ErrorConstants.SERVICE_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map getTxnSummaryData(int pageNumber,String requestPage,TxnSummaryDTO txnSummaryDTO,HttpServletRequest request) throws EOTException {

		HttpSession session=request.getSession(true);

		if(txnSummaryDTO.getBankId()==null && txnSummaryDTO.getCountryId()==null && txnSummaryDTO.getTransactionType()==null
					&& txnSummaryDTO.getFromDate()==null && txnSummaryDTO.getToDate()==null){
				txnSummaryDTO.setBankId(session.getAttribute("txnBankId") == null ? null : Integer.valueOf(session.getAttribute("txnBankId").toString()));
				txnSummaryDTO.setCountryId(session.getAttribute("txnCountryId") == null ? null : Integer.valueOf(session.getAttribute("txnCountryId").toString()));
				txnSummaryDTO.setTransactionType(session.getAttribute("transactionType") == null ? null : Integer.valueOf(session.getAttribute("transactionType").toString()));
				Date fdate=(Date)session.getAttribute("fromDate");
				Date tdate=(Date)session.getAttribute("toDate");
				txnSummaryDTO.setFromDate((fdate));
				txnSummaryDTO.setToDate(tdate);
			}else{
				session.setAttribute("txnBankId",txnSummaryDTO.getBankId());
				session.setAttribute("txnCountryId",txnSummaryDTO.getCountryId());
				session.setAttribute("transactionType",txnSummaryDTO.getTransactionType());
				session.setAttribute("fromDate",txnSummaryDTO.getFromDate());
				session.setAttribute("toDate",txnSummaryDTO.getToDate());
			}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		WebUser webUser = webUserDao.getUser(userName);
		if(webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BANK_TELLER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPER_ADMIN || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PARAMETER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_AUDIT || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_ACCOUNTING || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_SUPPORT_BANK ||  webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_INFORMATION_DESK
			|| webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BRANCH_MANAGER || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE || webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_RELATIONSHIP_MANAGER){
			BankTellers teller = bankDao.getTellerByUsername(webUser.getUserName());
     		//koushik Date:03/08/2018 purpose:txn Details not coming according to login user bug :5787
			txnSummaryDTO.setBankId(teller.getBank().getBankId());
			//@end
		}
		Map model = new HashMap();
		List txnSummList=webUserDao.getTxnSummaryList(txnSummaryDTO);
		if(txnSummList==null || txnSummList.isEmpty()){
			txnSummaryDTO.setBankId(null);
			throw new EOTException(ErrorConstants.NO_TXNS_FOUND);
		}
		//vineeth, commenting below line of code because we already have the result of below line and its unneccesary to call same method() agian and using existing txnSummList value.
//		model.put("txnSumm", webUserDao.getTxnSummaryList(txnSummaryDTO));
		model.put("txnSumm", txnSummList);
		Page page = webUserDao.getTxnSummary(txnSummaryDTO,pageNumber);
		page.requestPage=requestPage;
		model.put("txnList", page);
		return model;

	}

}
