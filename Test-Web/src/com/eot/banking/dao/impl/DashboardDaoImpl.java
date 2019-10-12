/* Copyright  EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: DashboardDaoImpl.java
*
* Date Author Changes
* 18 Dec, 2018 Sudhanshu Created
*/
package com.eot.banking.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.DashboardDao;
import com.eot.banking.dto.DashboardDTO;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;

/**
 * The Class DashboardDaoImpl.
 */
public class DashboardDaoImpl extends BaseDaoImpl implements DashboardDao {

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.DashboardDao#getDailyTransaction()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DashboardDTO getDailyTransaction(DashboardDTO dashBoardDTO) {
		StringBuilder queryString = new StringBuilder("SELECT SUM(CASE WHEN txn.TransactionType = :dTxnType THEN 1 ELSE 0 END) AS Deposit, SUM(CASE WHEN txn.TransactionType = :wTxnType THEN 1 ELSE 0 END) AS Withdraw, SUM(CASE WHEN txn.TransactionType = :sTxtxnType THEN 1 ELSE 0 END) AS Sale, "
				+ "SUM(CASE WHEN txn.TransactionType = :bpTxnType THEN 1 ELSE 0 END) AS BillPayment,SUM(CASE WHEN TransactionType = :toupTxnType THEN 1 ELSE 0 END) AS TopUp,"
				+ "SUM(CASE WHEN txn.TransactionType = :msTxnType THEN 1 ELSE 0 END) AS MiniStatement, SUM(CASE WHEN txn.TransactionType = :beTxnType THEN 1 ELSE 0 END) AS BalanceEnquiry, SUM(CASE WHEN txn.TransactionType = :smsTxnType THEN 1 ELSE 0 END) AS SMSCash,"
				+ "SUM(CASE WHEN txn.TransactionType = :smTxnType THEN 1 ELSE 0 END) AS SendMoney,  "
				+ "SUM(CASE WHEN txn.TransactionType = :payTxnType THEN 1 ELSE 0 END) AS Pay,  "
				+ "SUM(CASE WHEN txn.TransactionType = :floatTxnType THEN 1 ELSE 0 END) AS FloatManagement,"
				+ "SUM(CASE WHEN txn.TransactionType = :merchantPayoutType THEN 1 ELSE 0 END) AS MerchantPayout,"
				+ "SUM(CASE WHEN txn.TransactionType = :cashInType THEN 1 ELSE 0 END) AS CashIn,"
				+ "SUM(CASE WHEN txn.TransactionType = :cashOutType THEN 1 ELSE 0 END) AS CashOut,"
				+ "SUM(CASE WHEN txn.TransactionType = :trfrEMonryType THEN 1 ELSE 0 END) AS TransferEMoney,"
				+ "SUM(CASE WHEN txn.TransactionType = :bulkPaymentType THEN 1 ELSE 0 END) AS BulkPaymnet "
				+ "FROM Transactions txn ");
				//+ "FROM Transactions txn INNER JOIN Customer cust ON cust.CustomerId = txn.referenceId ");
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			queryString.append(" INNER JOIN Customer cust ON cust.CustomerId = txn.referenceId ");	
			queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BusinessPartnerUser bpu ON bpu.UserName=wsr.UserName INNER JOIN BusinessPartner bpt ON bpt.Id= bpu.partnerId AND bpt.Id=cust.PartnerId");
		if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
			queryString.append( " AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
		}
		
	}
	if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_COMMERCIAL_OFFICER)
			||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_ACCOUNTING) ||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_TELLER)){
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) ||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_COMMERCIAL_OFFICER)
				||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_ACCOUNTING) ||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_TELLER))
		//queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BankTellers bt ON bt.UserName=wsr.UserName INNER JOIN CustomerAccounts cact ON cact.CustomerID=cust.CustomerId AND cact.BankID=bt.BankId AND wsr.UserName=:loginUser");
			queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BankTellers bt ON bt.UserName=wsr.UserName AND wsr.UserName=:loginUser");
		if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
			//queryString.append( " INNER JOIN BusinessPartner bpt ON bpt.Id= cust.partnerId  AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			queryString.append( " INNER JOIN BusinessPartner bpt ON bpt.Id= cust.partnerId  AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
		}
		if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
			queryString.append( " INNER JOIN Customer cust ON cust.CustomerId = txn.referenceId AND cust.Type !=:cutType ");
		}
	}
queryString.append(" WHERE txn.Status=:status2000 AND txn.TransactionDate LIKE :todayDate ");
if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3))
	queryString.append(" AND wsr.UserName=:createdBy");
		Query query=getSessionFactory().getCurrentSession().createSQLQuery(queryString.toString());
		query.setParameter("dTxnType", 115).setParameter("wTxnType", 116).setParameter("sTxtxnType", 90).setParameter("bpTxnType", 82)
		.setParameter("toupTxnType", 80).setParameter("msTxnType", 35).setParameter("beTxnType", 30).setParameter("smsTxnType", 83).setParameter("smTxnType", 55)
		.setParameter("payTxnType", 128).setParameter("floatTxnType", 133).setParameter("merchantPayoutType", 140).setParameter("cashInType", 137)
		.setParameter("cashOutType", 138).setParameter("trfrEMonryType", 143).setParameter("bulkPaymentType", 141);
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN) ||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_COMMERCIAL_OFFICER)){
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				query.setParameter("partnerType", dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				query.setParameter("cutType",0);
			}
		}
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) ||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_COMMERCIAL_OFFICER)
				||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_ACCOUNTING) ||  dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_TELLER))
			query.setParameter("loginUser", dashBoardDTO.getWebUser().getUserName());
		query.setParameter("status2000", 2000).setParameter("todayDate", dashBoardDTO.getTodaysDate()+"%");
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3))
			query.setParameter("createdBy",dashBoardDTO.getWebUser().getUserName());
		List<Object[]> list = query.list();
		DashboardDTO dashboardDTO  = new DashboardDTO();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] obj : list)
			{
				dashboardDTO.setCashDeposit(obj[0]!=null?obj[0].toString():"0");
				dashboardDTO.setCashWithdrawal(obj[1]!=null?obj[1].toString():"0");
				dashboardDTO.setSale(obj[2]!=null?obj[2].toString():"0");
				dashboardDTO.setBillPayment(obj[3]!=null?obj[3].toString():"0");
				dashboardDTO.setTopup(obj[4]!=null?obj[4].toString():"0");
				dashboardDTO.setMiniStatement(obj[5]!=null?obj[5].toString():"0");
				dashboardDTO.setBalanceEnquiry(obj[6]!=null?obj[6].toString():"0");
				dashboardDTO.setSmsCash(obj[7]!=null?obj[7].toString():"0");
				dashboardDTO.setSendMoney(obj[8]!=null?obj[8].toString():"0");
				dashboardDTO.setPay(obj[9]!=null?obj[9].toString():"0");
				dashboardDTO.setFloatManagment(obj[10]!=null?obj[10].toString():"0");
				dashboardDTO.setMerchantPayout(obj[11]!=null?obj[11].toString():"0");
				dashboardDTO.setCashIn(obj[12]!=null?obj[12].toString():"0");
				dashboardDTO.setCashOut(obj[13]!=null?obj[13].toString():"0");
				dashboardDTO.setTransferEMoney(obj[14]!=null?obj[14].toString():"0");
				dashboardDTO.setBulkPayment(obj[15]!=null?obj[15].toString():"0");
			}
		}
		return dashboardDTO;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.DashboardDao#getTransactionSummary()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DashboardDTO getTransactionSummary(DashboardDTO dashBoardDTO) {
		
		StringBuilder queryString = new StringBuilder("SELECT SUM(CASE WHEN txn.TransactionType = :txnType95 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS Deposit,SUM(CASE WHEN txn.TransactionType = :txnType95 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cDeposit,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType99 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS Withdraw,SUM(CASE WHEN txn.TransactionType = :txnType99 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cWithdraw,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType90 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS Sale, SUM(CASE WHEN txn.TransactionType = :txnType90 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cSale,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType82 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS BillPayment, SUM(CASE WHEN txn.TransactionType = :txnType82 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cBillPayment,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType80 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS TopUp, SUM(CASE WHEN txn.TransactionType = :txnType80 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cTopUp,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType35 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS MiniStatement, SUM(CASE WHEN txn.TransactionType = :txnType35 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cMiniStatement,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType30 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS BalanceEnquiry, SUM(CASE WHEN txn.TransactionType = :txnType30 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cBalanceEnquary,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType83 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS SMSCash, SUM(CASE WHEN txn.TransactionType = :txnType83 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cSMSCash,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType55 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS SendMoney,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType55 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cSendMoney, "
				+ "SUM(CASE WHEN txn.TransactionType = :txnType128 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS Pay,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType128 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cPay, "
				+ "SUM(CASE WHEN txn.TransactionType = :txnType133 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS FloatManagement,"
				+ "SUM(CASE WHEN txn.TransactionType = :txnType133 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cFloatManagement, "
				
				+ "SUM(CASE WHEN txn.TransactionType = :merchantPayoutType140 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS MerchantPayout,"
				+ "SUM(CASE WHEN txn.TransactionType = :merchantPayoutType140 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cMerchantPayout,"
				
				+ "SUM(CASE WHEN txn.TransactionType = :cashInType137 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS CashIn,"
				+ "SUM(CASE WHEN txn.TransactionType = :cashInType137 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cCashIn,"
				
				+ "SUM(CASE WHEN txn.TransactionType = :cashOutType138 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS CashOut,"
				+ "SUM(CASE WHEN txn.TransactionType = :cashOutType138 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cCashOut,"
				
				+ "SUM(CASE WHEN txn.TransactionType = :trfrEMonryType143 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS TransferEMoney,"
				+ "SUM(CASE WHEN txn.TransactionType = :trfrEMonryType143 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cTransferEMoney,"
				
				+ "SUM(CASE WHEN txn.TransactionType = :bulkPaymentType141 AND txn.TransactionDate LIKE :todaysDate THEN txn.Amount ELSE 0 END) AS BulkPaymnet, "
				+ "SUM(CASE WHEN txn.TransactionType = :bulkPaymentType141 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.Amount ELSE 0 END) AS cBulkPaymnet "
				+ "FROM Transactions txn ");
				//+ "INNER JOIN Customer cust ON cust.CustomerId = txn.referenceId ");
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			queryString.append(" INNER JOIN Customer cust ON cust.CustomerId = txn.referenceId INNER JOIN WebUsers wsr INNER JOIN BusinessPartnerUser bpu ON bpu.UserName=wsr.UserName AND bpu.UserName=cust.OnbordedBy INNER JOIN BusinessPartner bpt ON bpt.Id= bpu.partnerId ");
		if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
			queryString.append( " AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			}
		}
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN))
			//queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BankTellers bt ON bt.UserName=wsr.UserName INNER JOIN CustomerAccounts cact ON cact.CustomerID=cust.CustomerId AND cact.BankID=bt.BankId AND wsr.UserName=:loginUser INNER JOIN Account  acc ON cact.accountnumber=acc.accountnumber AND acc.aliastype=1");
				queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BankTellers bt ON bt.UserName=wsr.UserName AND wsr.UserName=:loginUser ");
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				queryString.append( " INNER JOIN Customer cust ON cust.CustomerId = txn.referenceId INNER JOIN BusinessPartner bpt ON bpt.Id= cust.partnerId  AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			}
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				queryString.append( " AND cust.Type !=:cutType ");
			}
		}
		
		if(!dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) && !dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN))
			queryString.append(" AND cust.OnbordedBy=:createdBy");
		
		queryString.append(" WHERE txn.Status=:status");
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString.toString());
		query.setParameter("txnType95", 115).setParameter("txnType99", 116).setParameter("txnType90", 90).setParameter("txnType82", 82)
		.setParameter("txnType80", 80).setParameter("txnType35", 35).setParameter("txnType30", 30).setParameter("txnType83", 83)
		.setParameter("txnType55", 55).setParameter("todaysDate", dashBoardDTO.getTodaysDate()+"%").setParameter("currentDate", dashBoardDTO.getTodaysDate())
		.setParameter("txnType128", 128).setParameter("txnType133", 133).setParameter("merchantPayoutType140", 140).setParameter("cashInType137", 137).setParameter("cashOutType138", 138)
		.setParameter("trfrEMonryType143", 143).setParameter("bulkPaymentType141", 141);
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
			query.setParameter("partnerType", dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
		}
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN))
			query.setParameter("loginUser", dashBoardDTO.getWebUser().getUserName());
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
			query.setParameter("partnerType", dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				query.setParameter("cutType",0);
			}
		}
		if(!dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) && !dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN))
			query.setParameter("createdBy",dashBoardDTO.getWebUser().getUserName());
		query.setParameter("status", 2000);
		List<Object[]> list = query.list();
		DashboardDTO dashboardDTO  = new DashboardDTO();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] obj : list)
			{
				dashboardDTO.setCashDeposit(obj[0]!=null?obj[0].toString():"0");
				dashboardDTO.setCumCashDeposit(obj[1]!=null?obj[1].toString():"0");
				dashboardDTO.setCashWithdrawal(obj[2]!=null?obj[2].toString():"0");
				dashboardDTO.setCumCashWithdrawal(obj[3]!=null?obj[3].toString():"0");
				dashboardDTO.setSale(obj[4]!=null?obj[4].toString():"0");
				dashboardDTO.setCumSale(obj[5]!=null?obj[5].toString():"0");
				dashboardDTO.setBillPayment(obj[6]!=null?obj[6].toString():"0");
				dashboardDTO.setCumBillPayment(obj[7]!=null?obj[7].toString():"0");
				dashboardDTO.setTopup(obj[8]!=null?obj[8].toString():"0");
				dashboardDTO.setCumTopUp(obj[9]!=null?obj[9].toString():"0");
				dashboardDTO.setMiniStatement(obj[10]!=null?obj[10].toString():"0");
				dashboardDTO.setCumMiniStatement(obj[11]!=null?obj[11].toString():"0");
				dashboardDTO.setBalanceEnquiry(obj[12]!=null?obj[12].toString():"0");
				dashboardDTO.setCumBalanceEnquiry(obj[13]!=null?obj[13].toString():"0");
				dashboardDTO.setSmsCash(obj[14]!=null?obj[14].toString():"0");
				dashboardDTO.setCumSmsCash(obj[15]!=null?obj[15].toString():"0");
				dashboardDTO.setSendMoney(obj[16]!=null?obj[16].toString():"0");
				dashboardDTO.setCumSendMoney(obj[17]!=null?obj[17].toString():"0");
				dashboardDTO.setPay(obj[18]!=null?obj[18].toString():"0");
				dashboardDTO.setCumPay(obj[19]!=null?obj[19].toString():"0");
				dashboardDTO.setFloatManagment(obj[20]!=null?obj[20].toString():"0");
				dashboardDTO.setCumFloatManagment(obj[21]!=null?obj[21].toString():"0");
				dashboardDTO.setMerchantPayout(obj[22]!=null?obj[22].toString():"0");
				dashboardDTO.setCumMerchantPayout(obj[23]!=null?obj[23].toString():"0");
				dashboardDTO.setCashIn(obj[24]!=null?obj[24].toString():"0");
				dashboardDTO.setCumCashIn(obj[25]!=null?obj[25].toString():"0");
				dashboardDTO.setCashOut(obj[26]!=null?obj[26].toString():"0");
				dashboardDTO.setCumCashOut(obj[27]!=null?obj[27].toString():"0");
				dashboardDTO.setTransferEMoney(obj[28]!=null?obj[28].toString():"0");
				dashboardDTO.setCumTransferEMoney(obj[29]!=null?obj[29].toString():"0");
				dashboardDTO.setBulkPayment(obj[30]!=null?obj[30].toString():"0");
				dashboardDTO.setCumBulkPayment(obj[31]!=null?obj[31].toString():"0");
				
			}
		}
		return dashboardDTO;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.DashboardDao#getCommmissionSahre()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DashboardDTO getCommmissionSahre(DashboardDTO dashBoardDTO) {
		StringBuilder queryString = new StringBuilder("SELECT SUM(CASE WHEN cust.Type = :custType1 AND  txn.transactionType=:TxnType120 AND txn.TransactionDate LIKE :todaysDate THEN txn.amount ELSE 0 END) AS AgentCommission,"
				+ "SUM(CASE WHEN cust.Type = :custType2 AND  txn.transactionType=:TxnType120 AND txn.TransactionDate LIKE :todaysDate THEN txn.amount ELSE 0 END) AS SoleMerchantCommission,"
				+ "SUM(CASE WHEN cust.Type = :custType3 AND  txn.transactionType=:TxnType120 AND txn.TransactionDate LIKE :todaysDate THEN txn.amount ELSE 0 END) AS AgentSoleMerchantCommission,"
				+ "(SUM(CASE WHEN txnJr.CreditAccount=:creditAcc AND txnJr.JournalTime LIKE :todaysDate THEN txnJr.Amount ELSE 0 END)-SUM(CASE WHEN txnJr.DebitAccount=:debitAcc AND txnJr.JournalTime LIKE :todaysDate THEN txnJr.Amount ELSE 0 END)) AS mGurushCommsion,"
				+ "SUM(CASE WHEN cust.Type = :custType1 AND  txn.transactionType=:TxnType120 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.amount ELSE 0 END) AS cumAgentCommission,"
				+ "SUM(CASE WHEN cust.Type = :custType2 AND  txn.transactionType=:TxnType120 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.amount ELSE 0 END) AS cumSoleMerchantCommission,"
				+ "SUM(CASE WHEN cust.Type = :custType3 AND  txn.transactionType=:TxnType120 AND DATE(txn.TransactionDate) <= :currentDate THEN txn.amount ELSE 0 END) AS cumAgentSoleMerchantCommission,"
				+ "(SUM(CASE WHEN txnJr.CreditAccount=:creditAcc AND DATE(txnJr.JournalTime) <= :currentDate THEN txnJr.Amount ELSE 0 END)-SUM(CASE WHEN txnJr.DebitAccount=:debitAcc AND DATE(txnJr.JournalTime) <= :currentDate THEN txnJr.Amount ELSE 0 END)) AS cumMGurushCommsion "
				+ "FROM Transactions txn "
				+ "INNER JOIN TransactionJournals txnJr ON txn.transactionId=txnJr.transactionId "
				+ "INNER JOIN Customer cust ON txn.referenceId=cust.CustomerId ");
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BusinessPartnerUser bpu ON bpu.UserName=wsr.UserName AND bpu.UserName=cust.OnbordedBy INNER JOIN BusinessPartner bpt ON bpt.Id= bpu.partnerId ");
		if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
			queryString.append( " AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			}
		}
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN))
			queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BankTellers bt ON bt.UserName=wsr.UserName INNER JOIN CustomerAccounts cact ON cact.CustomerID=cust.CustomerId AND cact.BankID=bt.BankId AND wsr.UserName=:loginUser");
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				queryString.append( " INNER JOIN BusinessPartner bpt ON bpt.Id= cust.partnerId  AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			}
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				queryString.append( " AND cust.Type !=:cutType ");
			}
		}
		
		if(!dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) && !dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN))
			queryString.append(" AND cust.OnbordedBy=:createdBy");
		
		queryString.append(" WHERE txn.Status=:status");
		
		Query query = getSessionFactory().getCurrentSession().createSQLQuery(queryString.toString());
		
		query.setParameter("custType1", 1).setParameter("custType2", 2).setParameter("custType3", 3).setParameter("creditAcc", "1000000000107").setParameter("TxnType120", 120).setParameter("todaysDate", dashBoardDTO.getTodaysDate()+"%")
		.setParameter("debitAcc", "1000000000107").setParameter("currentDate", dashBoardDTO.getTodaysDate());
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				query.setParameter( "partnerType",dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
		}
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN))
				query.setParameter("loginUser",dashBoardDTO.getWebUser().getUserName());
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				query.setParameter( "partnerType",dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				query.setParameter("cutType",0);
			}
		}
		
		if(!dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) && !dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN))
			query.setParameter("createdBy", dashBoardDTO.getWebUser().getUserName());
		
		query.setParameter("status", 2000);
		
		List<Object[]> list = query.list();
		DashboardDTO dashboardDTO  = new DashboardDTO();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] obj : list)
			{
				dashboardDTO.setAgentCommission(obj[0]!=null?obj[0].toString():"0");
				dashboardDTO.setSoleMerchantCommission(obj[1]!=null?obj[1].toString():"0");
				dashboardDTO.setAgentSoleMerchantCommssion(obj[2]!=null?obj[2].toString():"0");
				dashboardDTO.setMgurushCommission(obj[3]!=null?obj[3].toString():"0");
				dashboardDTO.setCumAgentCommission(obj[4]!=null?obj[4].toString():"0");
				dashboardDTO.setCumSoleMerchantCommssion(obj[5]!=null?obj[5].toString():"0");
				dashboardDTO.setCumAgentSoleMerchantCommssion(obj[6]!=null?obj[6].toString():"0");
				dashboardDTO.setCumMgurushCommission(obj[7]!=null?obj[7].toString():"0");
			}
		}
		return dashboardDTO;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.DashboardDao#getMobileUserStatistics()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DashboardDTO getMobileUserStatistics(DashboardDTO dashBoardDTO) {
		/*StringBuilder queryString = new StringBuilder("SELECT SUM(CASE WHEN cust.TYPE = :customer AND cust.CreatedDate LIKE :todayDate AND cust.Active IN(:ActiveStatus,:PendingStatus) THEN 1 ELSE 0 END) AS Customer,"
				+ "SUM(CASE WHEN cust.TYPE = :tCustomer AND DATE(cust.CreatedDate) <= :currentDate AND cust.Active IN(:ActiveStatus,:PendingStatus)  THEN 1 ELSE 0 END) AS cCustomer,"
				
				+ "SUM(CASE WHEN cust.TYPE = :agent AND cust.CreatedDate LIKE :todayDate AND cust.Active IN(:ActiveStatus,:PendingStatus)  THEN 1 ELSE 0 END) AS Agnet,"
				+ "SUM(CASE WHEN cust.TYPE = :tAgent AND DATE(cust.CreatedDate) <= :currentDate AND cust.Active IN(:ActiveStatus,:PendingStatus)  THEN 1 ELSE 0 END) AS cAgnet,"
				
				+ "SUM(CASE WHEN cust.TYPE = :soleMerchant AND cust.CreatedDate LIKE :todayDate AND cust.Active IN(:ActiveStatus,:PendingStatus)  THEN 1 ELSE 0 END) AS SoleMerchant,"
				+ "SUM(CASE WHEN cust.TYPE = :tSoleMerchant AND DATE(cust.CreatedDate) <= :currentDate AND cust.Active IN(:ActiveStatus,:PendingStatus)  THEN 1 ELSE 0 END) AS cSoleMerchant,"
				
				+ "SUM(CASE WHEN cust.TYPE = :agentSoleMerchant AND cust.CreatedDate LIKE :todayDate AND cust.Active IN(:ActiveStatus,:PendingStatus)  THEN 1 ELSE 0 END) AS AgnetSoleMerchant,"
				+ "SUM(CASE WHEN cust.TYPE = :tAgnetSoleMerchant AND DATE(cust.CreatedDate) <= :currentDate AND cust.Active IN(:ActiveStatus,:PendingStatus) THEN 1 ELSE 0 END) AS cAgnetSoleMerchant,"
				
				+ "SUM(CASE WHEN cust.TYPE = :signUp AND cust.CreatedDate LIKE :todayDate AND cust.OnbordedBy= :onBoardedBy AND cust.Active IN(:ActiveStatus,:PendingStatus) THEN 1 ELSE 0 END) AS SignupCustomer,"
				+ "SUM(CASE WHEN cust.TYPE = :tsignUp AND DATE(cust.CreatedDate) <= :currentDate AND cust.OnbordedBy= :onBoardedBy AND cust.Active IN(:ActiveStatus,:PendingStatus) THEN 1 ELSE 0 END) AS cSignupCustomer, "
				
				+ "SUM(CASE WHEN cust.TYPE = :tCustomer AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:ActiveStatus THEN 1 ELSE 0 END) AS ActiveCustomer,"
				+ "SUM(CASE WHEN cust.TYPE = :tCustomer AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:PendingStatus THEN 1 ELSE 0 END) AS PendingCustomer,"

				+ "SUM(CASE WHEN cust.TYPE = :tAgent AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:ActiveStatus THEN 1 ELSE 0 END) AS ActiveAgnet,"
				+ "SUM(CASE WHEN cust.TYPE = :tAgent AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:PendingStatus THEN 1 ELSE 0 END) AS PendingAgnet,"
				
				+ "SUM(CASE WHEN cust.TYPE = :tSoleMerchant AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:ActiveStatus THEN 1 ELSE 0 END) AS ActiveSoleMerchant,"
				+ "SUM(CASE WHEN cust.TYPE = :tSoleMerchant AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:PendingStatus THEN 1 ELSE 0 END) AS PendingSoleMerchant,"
				
				+ "SUM(CASE WHEN cust.TYPE = :tAgnetSoleMerchant AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:ActiveStatus THEN 1 ELSE 0 END) AS ActiveAgnetSoleMerchant,"
				+ "SUM(CASE WHEN cust.TYPE = :tAgnetSoleMerchant AND DATE(cust.CreatedDate) <= :currentDate AND  cust.Active=:PendingStatus THEN 1 ELSE 0 END) AS PendingAgnetSoleMerchant,"

				+ "SUM(CASE WHEN cust.TYPE = :tsignUp AND DATE(cust.CreatedDate) <= :currentDate AND cust.OnbordedBy= :onBoardedBy AND  cust.Active=:ActiveStatus THEN 1 ELSE 0 END) AS ActiveSignupCustomer,"
				+ "SUM(CASE WHEN cust.TYPE = :tsignUp AND DATE(cust.CreatedDate) <= :currentDate AND cust.OnbordedBy= :onBoardedBy AND  cust.Active=:PendingStatus THEN 1 ELSE 0 END) AS PendingSignupCustomer "

				+ "FROM Customer cust ");*/
		
		StringBuilder queryString = new StringBuilder(""
				+ "SELECT SUM(CASE WHEN cust.TYPE = :customer AND cust.CreatedDate LIKE :todayDate THEN 1 ELSE 0 END) AS Customer,"
				+ "SUM(CASE WHEN cust.TYPE = :customer AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycPending  THEN 1 ELSE 0 END) AS kycPending,"
				+ "SUM(CASE WHEN cust.TYPE = :customer AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycApprovPending  THEN 1 ELSE 0 END) AS kycApprovalPending,"
				+ "SUM(CASE WHEN cust.TYPE = :customer AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycApprove  THEN 1 ELSE 0 END) AS kycApprove,"
				+ "SUM(CASE WHEN cust.TYPE = :customer AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycRejected  THEN 1 ELSE 0 END) AS kycRejected,"
				
				+ "SUM(CASE WHEN cust.TYPE = :agent AND cust.CreatedDate LIKE :todayDate THEN 1 ELSE 0 END) AS Agent,"
				+ "SUM(CASE WHEN cust.TYPE = :agent AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycPending  THEN 1 ELSE 0 END) AS agentKycPending,"
				+ "SUM(CASE WHEN cust.TYPE = :agent AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycApprovPending  THEN 1 ELSE 0 END) AS agentKycApprovalPending,"
				+ "SUM(CASE WHEN cust.TYPE = :agent AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycApprove  THEN 1 ELSE 0 END) AS agentKycApprove,"
				+ "SUM(CASE WHEN cust.TYPE = :agent AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycRejected  THEN 1 ELSE 0 END) AS agentKycRejected,"
				
				+ "SUM(CASE WHEN cust.TYPE = :merchant AND cust.CreatedDate LIKE :todayDate THEN 1 ELSE 0 END) AS Merchant,"
				+ "SUM(CASE WHEN cust.TYPE = :merchant AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycPending  THEN 1 ELSE 0 END) AS merchantKycPending,"
				+ "SUM(CASE WHEN cust.TYPE = :merchant AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycApprovPending  THEN 1 ELSE 0 END) AS merchantKycApprovalPending,"
				+ "SUM(CASE WHEN cust.TYPE = :merchant AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycApprove  THEN 1 ELSE 0 END) AS merchantKycApprove,"
				+ "SUM(CASE WHEN cust.TYPE = :merchant AND DATE(cust.CreatedDate) <= :currentDate AND cust.kyc_status =:kycRejected  THEN 1 ELSE 0 END) AS merchantKycRejected,"
				
				+ "SUM(CASE WHEN cust.TYPE = :customer AND DATE(cust.CreatedDate) <= :currentDate THEN 1 ELSE 0 END) AS totalCustomer,"
				+ "SUM(CASE WHEN cust.TYPE = :agent AND DATE(cust.CreatedDate) <= :currentDate THEN 1 ELSE 0 END) AS totalAgent,"
				+ "SUM(CASE WHEN cust.TYPE = :merchant AND DATE(cust.CreatedDate) <= :currentDate THEN 1 ELSE 0 END) AS totalMerchant "
				
				+"FROM Customer cust ");
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BusinessPartnerUser bpu ON bpu.UserName=wsr.UserName "
					+ " LEFT JOIN Customer agent ON cust.OnbordedBy=agent.AgentCode INNER JOIN BusinessPartner bpt ON bpt.Id= bpu.partnerId AND bpt.Id=cust.PartnerId AND wsr.UserName=:loginUser ");
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				queryString.append( " AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			}
		}
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN))
			//queryString.append(" INNER JOIN WebUsers wsr INNER JOIN BankTellers bt ON bt.UserName=wsr.UserName INNER JOIN CustomerAccounts cact ON cact.CustomerID=cust.CustomerId AND cact.BankID=bt.BankId AND wsr.UserName=:loginUser");
				queryString.append(" INNER JOIN WebUsers wsr WHERE wsr.UserName=:loginUser");
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				queryString.append( " INNER JOIN BusinessPartner bpt ON bpt.Id= cust.partnerId  AND bpt.PartnerType =:partnerType AND bpt.Id=:partnerId");
			}
			
		}

		//queryString.append(" WHERE cust.Active=:ActiveStatus");
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				queryString.append( " AND cust.Type !=:cutType ");
			}
		}

		Query query=getSessionFactory().getCurrentSession().createSQLQuery(queryString.toString());
		query.setParameter("customer", 0).setParameter("todayDate", dashBoardDTO.getTodaysDate()+"%").setParameter("currentDate", dashBoardDTO.getTodaysDate())
		.setParameter("agent", 1).setParameter("merchant", 2)
		.setParameter("kycPending", EOTConstants.KYC_STATUS_PENDING).setParameter("kycApprovPending", EOTConstants.KYC_STATUS_APPROVE_PENDING)
		.setParameter("kycApprove", EOTConstants.KYC_STATUS_APPROVED).setParameter("kycRejected", EOTConstants.KYC_STATUS_REGEJETED);
		
		
		/*query.setParameter("customer", 0).setParameter("todayDate", dashBoardDTO.getTodaysDate()+"%").setParameter("currentDate", dashBoardDTO.getTodaysDate()).setParameter("tCustomer", 0)
		.setParameter("agent", 1).setParameter("tAgent", 1).setParameter("soleMerchant", 2).setParameter("tSoleMerchant", 2).setParameter("agentSoleMerchant", 3)
		.setParameter("tAgnetSoleMerchant", 3).setParameter("signUp", 0).setParameter("tsignUp", 0).setParameter("onBoardedBy", "self").setParameter("ActiveStatus", EOTConstants.CUSTOMER_STATUS_ACTIVE)
		.setParameter("PendingStatus", EOTConstants.CUSTOMER_STATUS_NEW);*/
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_EOT_ADMIN)){
			if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BANK_ADMIN)){
				query.setParameter("loginUser", dashBoardDTO.getWebUser().getUserName());
			}
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				query.setParameter( "partnerType",dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
			if(null!=dashBoardDTO.getPartnerType() && dashBoardDTO.getPartnerType().equals(11)){
				query.setParameter("cutType",0);
			}
		}
		
		if(dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L1) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L2) || dashBoardDTO.getWebUser().getWebUserRole().getRoleId().equals(EOTConstants.ROLEID_BUSINESS_PARTNER_L3)){
			query.setParameter("loginUser", dashBoardDTO.getWebUser().getUserName());
			if(null != dashBoardDTO.getPartnerId() && dashBoardDTO.getPartnerId() != 0){
				query.setParameter( "partnerType",dashBoardDTO.getPartnerType()).setParameter("partnerId", dashBoardDTO.getPartnerId());
			}
		}
		
		//query.setParameter("ActiveStatus", EOTConstants.CUSTOMER_STATUS_NEW);	
		List<Object[]> list = query.list();
		DashboardDTO dashboardDTO  = new DashboardDTO();
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] obj : list)
			{
				dashboardDTO.setRegisteredCustomer(obj[0]!=null?obj[0].toString():"0");
				dashboardDTO.setKycPending(obj[1]!=null?obj[1].toString():"0");
				dashboardDTO.setKycApprovalPending(obj[2]!=null?obj[2].toString():"0");
				dashboardDTO.setKycApproved(obj[3]!=null?obj[3].toString():"0");
				dashboardDTO.setKycRejected(obj[4]!=null?obj[4].toString():"0");
				
				dashboardDTO.setAgent(obj[5]!=null?obj[5].toString():"0");
				dashboardDTO.setAgentKycPending(obj[6]!=null?obj[6].toString():"0");
				dashboardDTO.setAgentKycApprovalPending(obj[7]!=null?obj[7].toString():"0");
				dashboardDTO.setAgentKycApproved(obj[8]!=null?obj[8].toString():"0");
				dashboardDTO.setAgentKycRejected(obj[9]!=null?obj[9].toString():"0");
				
				dashboardDTO.setSoleMerchant(obj[10]!=null?obj[10].toString():"0");
				dashboardDTO.setMerchantKycPending(obj[11]!=null?obj[11].toString():"0");
				dashboardDTO.setMerchantKycApprovalPending(obj[12]!=null?obj[12].toString():"0");
				dashboardDTO.setMerchantKycApproved(obj[13]!=null?obj[13].toString():"0");
				dashboardDTO.setMerchantKycRejected(obj[14]!=null?obj[14].toString():"0");
				
				dashboardDTO.setTotalRegisteredCustomer(obj[15]!=null?obj[15].toString():"0");
				dashboardDTO.setTotalAgent(obj[16]!=null?obj[16].toString():"0");
				dashboardDTO.setTotalSoleMerchant(obj[17]!=null?obj[17].toString():"0");
				
				/*dashboardDTO.setRegisteredCustomer(obj[0]!=null?obj[0].toString():"0");
				dashboardDTO.setTotalRegisteredCustomer(obj[1]!=null?obj[1].toString():"0");
				dashboardDTO.setAgent(obj[2]!=null?obj[2].toString():"0");
				dashboardDTO.setTotalAgent(obj[3]!=null?obj[3].toString():"0");
				dashboardDTO.setSoleMerchant(obj[4]!=null?obj[4].toString():"0");
				dashboardDTO.setTotalSoleMerchant(obj[5]!=null?obj[5].toString():"0");
				dashboardDTO.setAgentSoleMerchant(obj[6]!=null?obj[6].toString():"0");
				dashboardDTO.setTotalAgentSoleMerchant(obj[7]!=null?obj[7].toString():"0");
				dashboardDTO.setSignupCustomer(obj[8]!=null?obj[8].toString():"0");
				dashboardDTO.setTotalSignupCustomerr(obj[9]!=null?obj[9].toString():"0");
				dashboardDTO.setActiveRegisteredCustomer(obj[10]!=null?obj[10].toString():"0");
				dashboardDTO.setNewRegisteredCustomer(obj[11]!=null?obj[11].toString():"0");
				dashboardDTO.setActiveAgent(obj[12]!=null?obj[12].toString():"0");
				dashboardDTO.setNewAgent(obj[13]!=null?obj[13].toString():"0");
				dashboardDTO.setActiveSoleMerchant(obj[14]!=null?obj[14].toString():"0");
				dashboardDTO.setNewSoleMerchant(obj[15]!=null?obj[15].toString():"0");
				dashboardDTO.setActiveAgentSoleMerchant(obj[16]!=null?obj[16].toString():"0");
				dashboardDTO.setNewAgentSoleMerchant(obj[17]!=null?obj[17].toString():"0");
				dashboardDTO.setActiveSignUpCustomer(obj[18]!=null?obj[18].toString():"0");
				dashboardDTO.setNewSignUpCustomer(obj[19]!=null?obj[19].toString():"0");*/
			}
		}
		return dashboardDTO;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BusinessPartner> loadBusinessPartnerByType(Integer partnerType, Integer bankId){
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BusinessPartner.class);
		criteria.add(Restrictions.eq("partnerType",partnerType));
		if( bankId!= null && ! "".equals(bankId)  ){
			criteria.add(Restrictions.eq("bank.bankId",bankId));
		}
		List<BusinessPartner> partnerList = criteria.list();
		return partnerList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> loadAccBalaceByUserId(String loginUser,Integer roleId) {
		StringBuilder queryString = new StringBuilder();
		if(roleId == EOTConstants.ROLEID_BULKPAYMENT_PARTNER || roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L1
				|| roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L2 || roleId == EOTConstants.ROLEID_BUSINESS_PARTNER_L3) {
		 queryString = queryString.append("SELECT acc.CurrentBalance as CurrentBalance,acc.aliasType as aliasType FROM Account acc INNER JOIN BusinessPartner bpt ON (bpt.AccountNumber= acc.AccountNumber OR bpt.CommissionAccount= acc.AccountNumber)"
				+ "INNER JOIN BusinessPartnerUser bptu ON bptu.partnerId= bpt.Id INNER JOIN WebUsers wbusr ON wbusr.UserName = bptu.UserName"
				+ " AND wbusr.UserName=:userName");
		}else {
			queryString = queryString.append("SELECT acc.aliasType AS aliasType,acc.CurrentBalance AS CurrentBalance FROM Account acc " + 
					"INNER JOIN Bank bank ON bank.AccountNumber= acc.AccountNumber " + 
					"INNER JOIN BankTellers bankteller ON (bankteller.bankId= bank.bankId) " + 
					"INNER JOIN WebUsers wbusr ON wbusr.UserName = bankteller.UserName AND wbusr.UserName=:userName");
		}
		Query query=getSessionFactory().getCurrentSession().createSQLQuery(queryString.toString()).addScalar("CurrentBalance",Hibernate.DOUBLE).addScalar("aliasType", Hibernate.INTEGER);
		query.setParameter("userName", loginUser).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.list();
	//	return CollectionUtils.isNotEmpty(list)?list.get(0):0D;
		return list;
	}

}
