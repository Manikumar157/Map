package com.eot.banking.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.TransactionDao;
import com.eot.banking.dto.BankFloatDepositDTO;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.dto.ExternalTransactionDTO;
import com.eot.banking.dto.NonRegUssdCustomerDTO;
import com.eot.banking.dto.TransactionDetailsDTO;
import com.eot.banking.dto.TransactionVolumeDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.dtos.basic.Transaction;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.ExternalTransaction;
import com.eot.entity.FileUploadDetail;
import com.eot.entity.MobileRequest;
import com.eot.entity.Operator;
import com.eot.entity.PendingTransaction;
import com.eot.entity.SettlementJournal;
import com.eot.entity.TransactionRuleTxn;
import com.eot.entity.TransactionType;
import com.eot.entity.WebUser;

/**
 * The Class TransactionDaoImpl.
 */
public class TransactionDaoImpl extends BaseDaoImpl implements TransactionDao {

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getTrnsactionList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionType> getTrnsactionList(String locale) {
		// vineeth, changed the query on 21/06/2018, as discussed "Add Payee" transaction type is hidden from the application and bug no:5294, 06/07/2018

		/*Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale='"+locale+"' and tr.transactionType!=81 and  tr.transactionType!=85 and tr.transactionType!=100 and tr.transactionType!=0 and tr.transactionType!=5 and tr.transactionType!=37"
				+ "and tr.transactionType!=40 and tr.transactionType!=45 and tr.transactionType!=86 and tr.transactionType!=50 and tr.transactionType!=33 and tr.transactionType!=70 and tr.transactionType!=75 and tr.transactionType!=101 and tr.transactionType!=102 and tr.transactionType!=103 and tr.transactionType!=104 and tr.transactionType!=31 order by lower(tr.description)");
		return query.list();*/
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = getUser(userName);

		StringBuilder queryString=new StringBuilder("select txn from TransactionType AS txn ,TransactionTypesDesc as td where txn.transactionType=td.comp_id.transactionType and td.comp_id.locale='"+locale+"' and txn.transactionType!=:txnType1 and  txn.transactionType!=:txnType2 and txn.transactionType!=:txnType3 and txn.transactionType!=:txnType4 and txn.transactionType!=:txnType5 and txn.transactionType!=:txnType6 "
				+ "and txn.transactionType!=:txnType7 and txn.transactionType!=:txnType8 and txn.transactionType!=:txnType9 and txn.transactionType!=:txnType10 and txn.transactionType!=:txnType11 and txn.transactionType!=:txnType12 and txn.transactionType!=:txnType13 and txn.transactionType!=:txnType14 and txn.transactionType!=:txnType15 and txn.transactionType!=:txnType16 and txn.transactionType!=:txnType17 and txn.transactionType!=:txnType18 "
				+ "and txn.transactionType!=:txnType19 and txn.transactionType!=:txnType20 and txn.transactionType!=:txnType21 and txn.transactionType!=:txnType22 and txn.transactionType!=:txnType23 "
				+ "and txn.transactionType!=:txnType24 and txn.transactionType!=:txnType25 and txn.transactionType!=:txnType26 and txn.transactionType!=:txnType27 "
				+ "and txn.transactionType!=:txnType28 and txn.transactionType!=:txnType29 and txn.transactionType!=:txnType30 and txn.transactionType!=:txnType31 and txn.transactionType!=:txnType32 ");
				if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
					queryString.append(" and txn.transactionType!=:txnType33 and txn.transactionType!=:txnType34 and txn.transactionType!=:txnType35 and txn.transactionType!=:txnType36 and txn.transactionType!=:txnType37 "
							+ " and txn.transactionType!=:txnType38 and txn.transactionType!=:txnType39 and txn.transactionType!=:txnType40 and txn.transactionType!=:txnType41 and txn.transactionType!=:txnType42 and txn.transactionType!=:txnType43 and txn.transactionType!=:txnType44 "
							+ " and txn.transactionType!=:txnType45 and txn.transactionType!=:txnType46 and txn.transactionType!=:txnType47 and txn.transactionType!=:txnType48 and txn.transactionType!=:txnType49 ");
				}
				queryString.append(" order by lower(txn.description)");
		
		Query query=getSessionFactory().getCurrentSession().createQuery(queryString.toString());		
		query.setParameter("txnType1", 81);
		query.setParameter("txnType2", 85);
		query.setParameter("txnType3", 100);
		query.setParameter("txnType4", 0);
		query.setParameter("txnType5", 5);
		query.setParameter("txnType6", 37);
		query.setParameter("txnType7", 40);
		query.setParameter("txnType8", 45);
		query.setParameter("txnType9", 86);

		query.setParameter("txnType10", 50);
		query.setParameter("txnType11", 33);
		query.setParameter("txnType12", 70);
		query.setParameter("txnType13", 75);
		query.setParameter("txnType14", 101);
		query.setParameter("txnType15", 102);
		query.setParameter("txnType16", 103);
		query.setParameter("txnType17", 104);
		query.setParameter("txnType18", 31);

		query.setParameter("txnType19", 39);
		query.setParameter("txnType20", 139);
		query.setParameter("txnType21", 136);
		query.setParameter("txnType22", 134);
		query.setParameter("txnType23", 132);
		query.setParameter("txnType24", 130);
		query.setParameter("txnType25", 129);
		query.setParameter("txnType26", 127);
		query.setParameter("txnType27", 125);
		query.setParameter("txnType28", 124);
		query.setParameter("txnType29", 123);
		query.setParameter("txnType30", 122);
		query.setParameter("txnType31", 118);
		query.setParameter("txnType32", 119);
		
		if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
			query.setParameter("txnType33", 144);
			query.setParameter("txnType34", 141);
			query.setParameter("txnType35", 95);
			query.setParameter("txnType36", 117);
			query.setParameter("txnType37", 146);
			query.setParameter("txnType38", 65);
			query.setParameter("txnType39", 61);
			query.setParameter("txnType40", 90);
			query.setParameter("txnType41", 55);
			query.setParameter("txnType42", 83);
			query.setParameter("txnType43", 126);
			query.setParameter("txnType44", 128);
			query.setParameter("txnType45", 108);
			query.setParameter("txnType46", 143);
			query.setParameter("txnType47", 145);
			query.setParameter("txnType48", 99);
			query.setParameter("txnType49", 147);
		}
		List<TransactionType> list = query.list();

		return list;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCustomer(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Customer getCustomer(String mobileNumber) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer cs where concat(cs.country.isdCode,mobileNumber)=:mobileNumber")
				.setParameter("mobileNumber",mobileNumber);
		List<Customer>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCustomerAccount(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerAccount> getCustomerAccount(Long customerId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount acc where acc.customer.customerId=:customerId")
				.setParameter("customerId",customerId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerAccount> getAgentAccount(Long customerId,int aliasType) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount acc where acc.customer.customerId=:customerId and acc.account.aliasType=:aliasType")
				.setParameter("customerId",customerId)
				.setParameter("aliasType", aliasType);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCustomerAccountFromAlias(java.lang.Long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CustomerAccount getCustomerAccountFromAlias(Long customerId,String alias) {

		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount acc where acc.customer.customerId=:customerId and acc.account.alias=:alias")
				.setParameter("customerId",customerId)
				.setParameter("alias",alias);		                               
		List<CustomerAccount>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;  

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getBankTeller(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BankTellers getBankTeller(String tellerName) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from BankTellers bt where bt.webUser.userName=:tellerName")
				.setParameter("tellerName",tellerName);
		List<BankTellers>list=query.list();
		return list.get(0);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getClearingHouse(java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ClearingHousePoolMember> getClearingHouse(Integer customerBank,Integer otherPartyBank){

		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery("select * from ClearingHousePoolMembers ch1 inner join ClearingHousePoolMembers ch2 " +
				"on ch1.clearingpoolid = ch2.clearingpoolid and ch1.bankid=:customerBank and ch2.bankid=:otherPartyBank")
				.setParameter("customerBank", customerBank)
				.setParameter("otherPartyBank", otherPartyBank);
		/*Query query = session.createSQLQuery("select * from ClearingHousePoolMembers ch1 inner join ClearingHousePoolMembers ch2 " +
				"on ch1.clearingpoolid = ch2.clearingpoolid and ch1.bankid="+customerBank+" and ch2.bankid="+otherPartyBank);*/

		List<ClearingHousePoolMember> chPoolList = query.list();

		return chPoolList.size()>0 ? chPoolList : null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getTransactionDetails(java.lang.Integer, java.lang.Long, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getTransactionDetails(Integer pageNumber,Long referenceId,Integer referenceType ) {

		String queryString ="select TransactionID,TransactionType,Alias,TransactionDate,sum(Amount) as Amount,sum(SC) as ServiceCharge,Status,TxnType,cusType from ( " +
				"select txn.TransactionID,txn.TransactionType,acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,txn.Status,'C' AS TxnType,txn.CustomerAccountType AS cusType " +
				" from TransactionJournals txj,Transactions txn,Account acc where  acc.ReferenceID=:ReferenceID and acc.ReferenceType=:ReferenceType and " +
				" txn.TransactionID=txj.TransactionID and txj.CreditAccount=acc.AccountNumber and " +
				" txn.Status=:status and txj.JournalType=0 " +
				" union all " +
				"select txn.TransactionID,txn.TransactionType, acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,txn.Status,'D' AS TxnType,txn.CustomerAccountType AS cusType " +
				" from TransactionJournals txj,Transactions txn,Account acc where  acc.ReferenceID=:ReferenceID and acc.ReferenceType=:ReferenceType and " +
				" txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and " +
				" txn.Status=:status and txj.JournalType=0 " +  
				" union all " +
				"select txn.TransactionID,txn.TransactionType, acc.Alias as Alias,txn.TransactionDate, 0 as Amount, sum(txj.amount) as SC,txn.Status,'D' AS TxnType,txn.CustomerAccountType AS cusType " +
				" from TransactionJournals txj,Transactions txn,Account acc where  acc.ReferenceID=:ReferenceID and acc.ReferenceType=:ReferenceType and " +
				" txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and " +
				" txn.Status=:status and txj.JournalType=1 group by txn.TransactionID" + 
				")a group by TransactionID order by TransactionID desc" ;


		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery( queryString );

		query.setParameter("ReferenceID", referenceId+"");
		query.setParameter("ReferenceType", referenceType);
		query.setParameter("status", EOTConstants.TXN_NO_ERROR);

		query.addScalar("TransactionID",Hibernate.LONG);
		query.addScalar("TransactionType",Hibernate.INTEGER);
		query.addScalar("Alias",Hibernate.STRING);
		query.addScalar("TransactionDate",Hibernate.TIMESTAMP);
		query.addScalar("Amount",Hibernate.DOUBLE);
		query.addScalar("ServiceCharge",Hibernate.DOUBLE);
		query.addScalar("status",Hibernate.INTEGER);
		query.addScalar("TxnType",Hibernate.STRING);
		query.addScalar("cusType",Hibernate.INTEGER);

		List<Object[]> list = query.list();
		List<TransactionDetailsDTO> dtoList = new ArrayList<TransactionDetailsDTO>();

		for(Object[] obj : list){

			TransactionDetailsDTO dto = new TransactionDetailsDTO();

			dto.setTransactionID((Long)obj[0]);
			dto.setTransactionType((Integer)obj[1]);
			dto.setAlias((String)obj[2]);
			dto.setTransactionDate((Date)obj[3]);
			dto.setTransactionAmount(((Double)obj[4]).longValue());
			dto.setServiceCharge(((Double)obj[5]).longValue());
			dto.setStatus((Integer)obj[6]);
			dto.setType((String)obj[7]);
			dto.setCusType((Integer)obj[8]);

			dtoList.add(dto) ;

		}

		/*StringBuffer qryStr = null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		qryStr = new StringBuffer("SELECT transactionID,transactionType,Alias,transactionDate,Amount,ServiceCharge,STATUS,TxnType,accountType FROM (SELECT transactions.TransactionID,transactions.amount,transactions.TransactionType,transactions.TransactionDate,transactions.Status,acc.alias,'C' AS TxnType," +
				" (SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=1)" +
				" AS ServiceCharge,transactions.CustomerAccountType AS accountType FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID" +
				" JOIN Account AS acc ON acc.AccountNumber=custacc.AccountNumber  JOIN TransactionJournals AS tj ON tj.TransactionID=transactions.TransactionID AND tj.CreditAccount=acc.AccountNumber " +
				" AND customer.customerId = '"+referenceId+"' AND customer.ProfileID=custprof.ProfileID AND transactions.Status=2000 " +
				" UNION ALL " +
				" SELECT transactions.TransactionID,transactions.amount,transactions.TransactionType,transactions.TransactionDate,transactions.Status,acc.alias,'D' AS TxnType,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=1) AS ServiceCharge,transactions.CustomerAccountType AS accountType " +
				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID" +
				" JOIN Account AS acc ON acc.AccountNumber=custacc.AccountNumber JOIN TransactionJournals AS tj ON tj.TransactionID=transactions.TransactionID AND tj.DebitAccount=acc.AccountNumber" +
				" AND customer.customerId = '"+referenceId+"' AND customer.ProfileID=custprof.ProfileID AND transactions.Status=2000 GROUP BY transactions.TransactionID)a GROUP BY TransactionID ORDER BY TransactionDate DESC  ");

		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());


		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List list= qryResult1.list();*/


		return PaginationHelper.getPage(dtoList,appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getTransactionDetailsByAccountNumber(java.lang.Integer, java.lang.Long, java.lang.Integer, java.lang.String)
	 */
	@Override
	public Page getTransactionDetailsByAccountNumber( Integer pageNumber, Long referenceId, Integer referenceType,String accountNumber) {

		String queryString ="select TransactionID,TransactionType,Alias,TransactionDate,sum(Amount) as Amount,sum(SC) as ServiceCharge,Status,TxnType,cusType from ( " +
				"select txn.TransactionID,txn.TransactionType, acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,txn.Status,'C' AS TxnType,txn.CustomerAccountType AS cusType " +
				" from TransactionJournals txj,Transactions txn,Account acc where  acc.ReferenceID=:ReferenceID and acc.ReferenceType=:ReferenceType and " +
				" txj.CreditAccount=:accountNumber and txn.TransactionID=txj.TransactionID and txj.CreditAccount=acc.AccountNumber and " +
				" txn.status=:status and txj.JournalType=0 " +
				" union all " +
				"select txn.TransactionID,txn.TransactionType, acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,txn.Status,'D' AS TxnType,txn.CustomerAccountType AS cusType " +
				" from TransactionJournals txj,Transactions txn,Account acc where  acc.ReferenceID=:ReferenceID and acc.ReferenceType=:ReferenceType and " +
				" txj.DebitAccount=:accountNumber and txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and " +
				" txn.status=:status and txj.JournalType=0 " +  
				" union all " +
				"select txn.TransactionID,txn.TransactionType, acc.Alias as Alias,txn.TransactionDate, 0 as Amount, sum(txj.amount) as SC,txn.Status,'D' AS TxnType,txn.CustomerAccountType AS cusType " +
				" from TransactionJournals txj,Transactions txn,Account acc where  acc.ReferenceID=:ReferenceID and acc.ReferenceType=:ReferenceType and " +
				" txj.DebitAccount=:accountNumber and txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and " +
				" txn.status=:status and txj.JournalType=1 group by txn.TransactionID" + 
				") a group by TransactionID order by TransactionID desc" ;


		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery( queryString );

		query.setParameter("ReferenceID", referenceId+"");
		query.setParameter("ReferenceType", referenceType);
		query.setParameter("accountNumber", accountNumber);
		query.setParameter("status", EOTConstants.TXN_NO_ERROR);

		query.addScalar("TransactionID",Hibernate.LONG);
		query.addScalar("TransactionType",Hibernate.INTEGER);
		query.addScalar("Alias",Hibernate.STRING);
		query.addScalar("TransactionDate",Hibernate.TIMESTAMP);
		query.addScalar("Amount",Hibernate.DOUBLE);
		query.addScalar("ServiceCharge",Hibernate.DOUBLE);
		query.addScalar("Status",Hibernate.INTEGER);
		query.addScalar("TxnType",Hibernate.STRING);
		query.addScalar("cusType",Hibernate.INTEGER);

		List<Object[]> list = query.list();

		List<TransactionDetailsDTO> dtoList = new ArrayList<TransactionDetailsDTO>();

		for(Object[] obj : list){

			TransactionDetailsDTO dto = new TransactionDetailsDTO();

			dto.setTransactionID((Long)obj[0]);
			dto.setTransactionType((Integer)obj[1]);
			dto.setAlias((String)obj[2]);
			dto.setTransactionDate((Date)obj[3]);
			dto.setTransactionAmount(((Double)obj[4]).longValue());
			dto.setServiceCharge(((Double)obj[5]).longValue());
			dto.setStatus((Integer)obj[6]);
			dto.setType((String)obj[7]);
			dto.setCusType((Integer)obj[8]);
			dtoList.add(dto) ;

		}

		/*StringBuffer qryStr = null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		qryStr = new StringBuffer("SELECT transactionID,transactionType,Alias,transactionDate,Amount,ServiceCharge,STATUS,TxnType,accountType FROM (SELECT transactions.TransactionID,transactions.amount,transactions.TransactionType,transactions.TransactionDate,transactions.Status,acc.alias,'C' AS TxnType," +
				" (SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=1)" +
				" AS ServiceCharge,transactions.CustomerAccountType AS accountType FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID" +
				" JOIN Account AS acc ON acc.AccountNumber=custacc.AccountNumber  JOIN TransactionJournals AS tj ON tj.TransactionID=transactions.TransactionID AND tj.CreditAccount=acc.AccountNumber " +
				" AND customer.customerId = '"+referenceId+"' AND customer.ProfileID=custprof.ProfileID AND custacc.AccountNumber='"+accountNumber+"' and transactions.Status=2000" +
				" UNION ALL " +
				" SELECT transactions.TransactionID,transactions.amount,transactions.TransactionType,transactions.TransactionDate,transactions.Status,acc.alias,'D' AS TxnType,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=1) AS ServiceCharge,transactions.CustomerAccountType AS accountType" +
				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID" +
				" JOIN Account AS acc ON acc.AccountNumber=custacc.AccountNumber JOIN TransactionJournals AS tj ON tj.TransactionID=transactions.TransactionID AND tj.DebitAccount=acc.AccountNumber" +
				" AND customer.customerId = '"+referenceId+"' AND customer.ProfileID=custprof.ProfileID AND custacc.AccountNumber='"+accountNumber+"' and transactions.Status=2000 GROUP BY transactions.TransactionID)a GROUP BY TransactionID ORDER BY TransactionDate DESC  ");

		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());


		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		List list= qryResult1.list();*/

		return PaginationHelper.getPage(dtoList, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getRequests(java.lang.Integer, java.lang.Long, java.lang.Integer)
	 */
	@Override
	public Page getRequests(Integer pageNumber,Long referenceId, Integer referenceType) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from MobileRequest req where req.referenceId=:referenceId and " +
				"req.referenceType=:referenceType and req.status!=:status order by req.requestId desc");
		query.setParameter("referenceId", referenceId+"");
		query.setParameter("referenceType", referenceType);
		query.setParameter("status", EOTConstants.MOBREQUEST_STATUS_SUCCESS);
		return PaginationHelper.getPage(query.list(), appConfig.getResultsPerPage(), pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getRequest(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public MobileRequest getRequest(Long requestId) {

		Query query = getSessionFactory().getCurrentSession().createQuery("from MobileRequest req where req.requestId=:requestId");
		query.setParameter("requestId", requestId);

		List<MobileRequest> list = query.list() ;

		return list.size() > 0 ? list.get(0) : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getChartList(com.eot.banking.dto.TxnSummaryDTO)
	 */
	@Override
	public List getChartList(TxnSummaryDTO txnSummaryDTO) {

		if(txnSummaryDTO.getBankId()!=null){
			StringBuffer sb=new StringBuffer();
			if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgCount")){
				sb.append("select b.bankname,count(TransactionID),sum(amount) from Transactions tr,Bank b where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}else if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgValue")){
				sb.append("select b.bankname,sum(amount) from Transactions tr,Bank b where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}	
			if(txnSummaryDTO!=null){
				if(txnSummaryDTO.getTransactionType()!=null){
					sb.append(" and TransactionType="+txnSummaryDTO.getTransactionType());
				}
			}
			sb.append(" and CustomerAccount in(select AccountNumber from CustomerAccounts");
			sb.append(" where BankID in(select BankID from Bank where bankid=b.bankid and bankid="+txnSummaryDTO.getBankId()+")) and tr.status="+EOTConstants.TXN_NO_ERROR+" group by b.bankid");
			SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery(sb.toString());
			if(txnSummaryDTO!=null){ 				
				if(txnSummaryDTO.getFromDate()!=null){
					query.setString(0, DateUtil.formatDate(txnSummaryDTO.getFromDate()));
				}
				if(txnSummaryDTO.getToDate()!=null){
					query.setString(1, DateUtil.formatDate(txnSummaryDTO.getToDate()));
				}

			}
			return query.list();
		}

		else if(txnSummaryDTO.getChartType().equals("gim")){
			StringBuffer sb=new StringBuffer();
			if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgCount")){
				sb.append("select 'GIM',count(TransactionID),sum(amount) from Transactions where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}else if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgValue")){
				sb.append("select 'GIM',sum(amount),count(TransactionID) from Transactions where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}

			if(txnSummaryDTO!=null){
				if(txnSummaryDTO.getTransactionType()!=null){
					sb.append(" and TransactionType="+txnSummaryDTO.getTransactionType());					
				}
			}
			sb.append(" and status="+ EOTConstants.TXN_NO_ERROR);
			sb.append(" order by TransactionID");
			SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery(sb.toString());			
			if(txnSummaryDTO!=null){
				if(txnSummaryDTO.getFromDate()!=null){
					query.setString(0, DateUtil.formatDate(txnSummaryDTO.getFromDate()));
				}
				if(txnSummaryDTO.getToDate()!=null){
					query.setString(1, DateUtil.formatDate(txnSummaryDTO.getToDate()));
				}				
			}			
			return query.list();
		}else if(txnSummaryDTO.getChartType().equals("country")){

			StringBuffer sb=new StringBuffer();
			if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgCount")){
				sb.append("select cd.country,count(TransactionID),sum(amount) from Transactions tr,Country cd where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}else if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgValue")){
				sb.append("select cd.country,sum(amount) from Transactions tr,Country cd where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}

			if(txnSummaryDTO!=null){
				if(txnSummaryDTO.getTransactionType()!=null){
					sb.append(" and TransactionType="+txnSummaryDTO.getTransactionType());
				}
			}
			sb.append(" and CustomerAccount in(select AccountNumber from CustomerAccounts");
			sb.append(" where BankID in(select BankID from Bank where CountryID in(select c.CountryID from Country c where c.CountryID=cd.CountryID))) and tr.status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");
			SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery(sb.toString());
			if(txnSummaryDTO!=null){

				if(txnSummaryDTO.getFromDate()!=null){
					query.setString(0, DateUtil.formatDate(txnSummaryDTO.getFromDate()));
				}
				if(txnSummaryDTO.getToDate()!=null){
					query.setString(1, DateUtil.formatDate(txnSummaryDTO.getToDate()));
				}

			}
			return query.list();
		}else if(txnSummaryDTO.getChartType().equals("bankGroup")){
			StringBuffer sb=new StringBuffer();
			if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgCount")){
				sb.append("select bg.bankgroupname,count(TransactionID),sum(amount) from Transactions tr,BankGroups bg where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}else if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgValue")){
				sb.append("select bg.bankgroupname,sum(amount) from Transactions tr,BankGroups bg where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?" );
			}

			if(txnSummaryDTO!=null){
				if(txnSummaryDTO.getTransactionType()!=null){
					sb.append(" and TransactionType="+txnSummaryDTO.getTransactionType());
				}
			}
			sb.append(" and CustomerAccount in(select AccountNumber from CustomerAccounts");
			sb.append(" where BankID in(select BankID from Bank where bankGroupID in(select b.bankGroupID from BankGroups b where b.bankGroupID=bg.bankGroupID))) and tr.status="+EOTConstants.TXN_NO_ERROR+" group by bg.bankGroupID");
			SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery(sb.toString());
			if(txnSummaryDTO!=null){

				if(txnSummaryDTO.getFromDate()!=null){
					query.setString(0, DateUtil.formatDate(txnSummaryDTO.getFromDate()));
				}
				if(txnSummaryDTO.getToDate()!=null){
					query.setString(1, DateUtil.formatDate(txnSummaryDTO.getToDate()));
				}

			}
			return query.list();
		}else if(txnSummaryDTO.getChartType().equals("bank")){
			StringBuffer sb=new StringBuffer();
			if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgCount")){
				sb.append("select b.bankname,count(TransactionID),sum(amount) from Transactions tr,Bank b where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}else if(txnSummaryDTO.getImgType()!=null && txnSummaryDTO.getImgType().equals("imgValue")){
				sb.append("select b.bankname,sum(amount) from Transactions tr,Bank b where DATE(TransactionDate)>=? and DATE(TransactionDate)<=?");
			}	
			if(txnSummaryDTO!=null){
				if(txnSummaryDTO.getTransactionType()!=null){
					sb.append(" and TransactionType="+txnSummaryDTO.getTransactionType());
				}
			}
			sb.append(" and CustomerAccount in(select AccountNumber from CustomerAccounts");
			sb.append(" where BankID in(select BankID from Bank where bankid=b.bankid)) and tr.status="+EOTConstants.TXN_NO_ERROR+" group by b.bankid");
			SQLQuery query=getSessionFactory().getCurrentSession().createSQLQuery(sb.toString());
			if(txnSummaryDTO!=null){ 				
				if(txnSummaryDTO.getFromDate()!=null){
					query.setString(0, DateUtil.formatDate(txnSummaryDTO.getFromDate()));
				}
				if(txnSummaryDTO.getToDate()!=null){
					query.setString(1, DateUtil.formatDate(txnSummaryDTO.getToDate()));
				}

			}
			return query.list();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getMobileNumLength(java.lang.Integer)
	 */
	public Integer getMobileNumLength(Integer isdCode){
		return (Integer)getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createQuery("select mobileNumberLength from Country where isdCode=?").setInteger(0, isdCode).uniqueResult();

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getAccountLedgerReport(java.lang.String, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAccountLedgerReport(String accountNumber,Date fromDate,Date toDate) {
		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		//Calendar calendar = Calendar.getInstance();
		//calendar.setTime( toDate );
		//calendar.add( Calendar.DATE, 1);

		Query query = session.createSQLQuery(
				"select myresult.*, txn.TransactionDate from (  " + 
						"select TransactionID, Amount as Amount, 'CR' as Type,JournalType,CreditDesc as Descript " + 
						"from TransactionJournals where CreditAccount=:accountNumber and JournalType= 0   " + 
						"UNION ALL     " + 
						"select TransactionID, Amount as Amount, 'DR' AS Type, JournalType,DebitDesc as Descript " + 
						"from TransactionJournals where DebitAccount=:accountNumber and JournalType= 0    " + 
						"UNION ALL     " + 
						"select TransactionID, sum(Amount) as Amount, 'DR' AS Type, JournalType,DebitDesc as Descript    " + 
						"from TransactionJournals where DebitAccount=:accountNumber and journaltype= 1    " + 
						"group by TransactionId, JournalType   " + 
						")  myresult,Transactions txn   " + 
						"                where myresult.TransactionID=txn.TransactionID " + 
						"                       and (DATE(txn.TransactionDate) between :fromDateString and :toDateString) " + 
						"                ORDER BY txn.TransactionDate desc "
				)
				.addScalar("TransactionDate",Hibernate.TIMESTAMP).addScalar("Descript",Hibernate.STRING)
				.addScalar("Type",Hibernate.STRING).addScalar("Amount",Hibernate.LONG)

				.setParameter("accountNumber", accountNumber , Hibernate.STRING)
				.setParameter("fromDateString", DateUtil.formatDate(fromDate))
				.setParameter("toDateString", DateUtil.formatDate(toDate));

		List<Object[]> list = query.list();

		List<Object> transactionList = new ArrayList<Object>();
		for(Object[] obj : list) {

			Map map=new HashMap();
			Calendar calendarObj = Calendar.getInstance();
			calendarObj.setTime( (Date)obj[0] );
			map.put("transDate", calendarObj.getTime());
			map.put("transDesc", (String)obj[1]);
			map.put("transType", (String)obj[2]);
			map.put("amount", (Long)obj[3]);

			transactionList.add(map);
		}
		Object accAliasName= session.createQuery("select alias from Account where accountNumber=?").setString(0, accountNumber).uniqueResult();
		transactionList.add(accAliasName);
		return transactionList;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getTrialBalance(java.util.List, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getTrialBalance(List<Map> accountNumbers,Date fromDate,Date toDate) {
		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		//Calendar calendar = Calendar.getInstance();
		//calendar.setTime( toDate );
		//calendar.add( Calendar.DATE, 1);
		List<Object> transactionList = new ArrayList<Object>();
		for(Map accMap:accountNumbers){
			Query query = session.createSQLQuery(
					"select Alias,accAmtResult.* from (select sum(amount) as Amount,Type from (select myresult.*, txn.TransactionDate from ("+ 
							"select TransactionId,Amount as Amount, 'CR' as Type,JournalType from TransactionJournals where CreditAccount=:accountNumber and JournalType= 0 "+   
							"UNION ALL select TransactionId,Amount as Amount, 'DR' AS Type, JournalType "+
							"from TransactionJournals where DebitAccount=:accountNumber and JournalType= 0 "+    
							"UNION ALL select TransactionId,sum(Amount) as Amount, 'DR' AS Type, JournalType "+ 
							"from TransactionJournals where DebitAccount=:accountNumber and journaltype= 1 group by TransactionId, JournalType"+   
							")  myresult,Transactions txn where myresult.TransactionID=txn.TransactionID "+ 
							"and (DATE(txn.TransactionDate) between :fromDateString and :toDateString) ORDER BY txn.TransactionDate desc) res "+ 
							"group by type) accAmtResult,Account where AccountNumber=:accountNumber"
					)

					.addScalar("Alias",Hibernate.STRING)
					.addScalar("Amount",Hibernate.LONG).addScalar("Type",Hibernate.STRING)

					.setParameter("accountNumber", accMap.get("accountNumber").toString() , Hibernate.STRING)
					.setParameter("fromDateString", DateUtil.formatDate(fromDate))
					.setParameter("toDateString", DateUtil.formatDate(toDate));

			List<Object[]> list = query.list();

			Long crAmount=0L;
			Long drAmount=0L;
			for(Object[] obj : list) {

				if(((String)obj[2]).equals("CR")){
					crAmount=(Long)obj[1];
				}else if(((String)obj[2]).equals("DR")){
					drAmount=(Long)obj[1];
				}

			}

			Map map=new HashMap();

			if(list!=null && !list.isEmpty()){
				map.put("accountName", accMap.get("accountHead"));
				if(crAmount>drAmount){
					map.put("crAmount", crAmount-drAmount);

				}else if(drAmount>crAmount){
					map.put("drAmount", drAmount-crAmount);

				}
				transactionList.add(map);
			}

		}
		return transactionList;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getAccountHeadList(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getAccountHeadList(Integer bankId) {
		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		if(bankId!=null){
			return session.createQuery("select ahm.accountNumber as accountNumber,ah.accountHead as accountHead from AccountHeadMapping ahm,AccountHead ah where " +
					"ahm.bank.bankId=? and ahm.accountHead.headerId=ah.headerId and ah.accountBook.bookId=2 order by ah.headerId").setInteger(0, bankId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}else{
			return session.createQuery("select ahm.accountNumber as accountNumber,ah.accountHead as accountHead from AccountHeadMapping ahm,AccountHead ah where " +
					"ahm.accountHead.headerId=ah.headerId and ah.accountBook.bookId=3 order by ah.headerId").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		}

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getBankByChPoolId(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getBankByChPoolId(String chPoolId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria.createAlias("clearingHousePoolMembers","chPoolMembers")
		.add(Restrictions.eq("chPoolMembers.clearingPoolId", Integer.parseInt(chPoolId)))
		.addOrder(Order.asc("bankName"));
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getPendingTransactions(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, int, java.lang.Long)
	 */
	@Override
	public Page getPendingTransactions(String customerName,String mobileNumber,String amount,String status,String txnDate,String txnType,Integer bankId,String fromDate,String toDate,int pageNumber,Long branchId) {

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(PendingTransaction.class);
		criteria.add(Restrictions.eq("bank.bankId", bankId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria.createAlias("customer", "customer");

		if( customerName!= null && ! "".equals(customerName) ){
			criteria.add(Restrictions.like("customer.firstName","%"+customerName+"%"));
		}if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			criteria.add(Restrictions.like("customer.mobileNumber","%"+mobileNumber+"%"));
		}
		if( amount!= null && ! "".equals(amount) ){
			criteria.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}	
		if( txnDate!= null && ! "".equals(txnDate) ){
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+txnDate+"'"));
		}
		if( txnType!= null && ! "".equals(txnType) && !txnType.equals("select")){
			criteria.add(Restrictions.eq("transactionType.transactionType", Integer.parseInt(txnType)));
		}
		if(status!=null && ! "".equals(status)){
			criteria.add(Restrictions.eq("status", Integer.parseInt(status)));
		}

		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'"));
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+toDate.toUpperCase()+"'"));
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'"));
		}
		if(branchId!=null){			
			criteria.createCriteria("customer.customerAccounts","custAcc");
			criteria.add(Restrictions.eq("custAcc.branch.branchId", branchId));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(PendingTransaction.class);
		criteria1.add(Restrictions.eq("bank.bankId", bankId));
		criteria1.createAlias("customer", "customer");

		if( customerName!= null && ! "".equals(customerName) ){
			criteria1.add(Restrictions.like("customer.firstName","%"+customerName+"%"));
		}if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			criteria1.add(Restrictions.like("customer.mobileNumber","%"+mobileNumber+"%"));
		}
		if( amount!= null && ! "".equals(amount) ){
			criteria1.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}	
		if( txnDate!= null && ! "".equals(txnDate) ){
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+txnDate+"'"));
		}
		if( txnType!= null && ! "".equals(txnType) && !txnType.equals("select")){
			criteria1.add(Restrictions.eq("transactionType.transactionType", Integer.parseInt(txnType)));	
		}
		if(status!=null && ! "".equals(status)){
			criteria1.add(Restrictions.eq("status", Integer.parseInt(status)));
		}

		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'"));
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+toDate.toUpperCase()+"'"));
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'"));
		}
		if(branchId!=null){
			criteria1.createCriteria("customer.customerAccounts","custAcc");
			criteria1.add(Restrictions.eq("custAcc.branch.branchId", branchId));
		}
		criteria1.addOrder(Order.desc("transactionDate"));
		//criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#verifyCustomer(java.lang.Long)
	 */
	@Override
	public Customer verifyCustomer(Long customerId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer cust where cust.customerId=:customerId")
				.setParameter("customerId",customerId);
		List<Customer>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCustomerPendingTransaction(java.lang.Long, java.lang.Long)
	 */
	@Override
	public PendingTransaction getCustomerPendingTransaction(Long referenceId,Long transactionId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from PendingTransaction pt where pt.customer.customerId=:referenceId and pt.transactionId=:transactionId")
				.setParameter("referenceId",referenceId)
				.setParameter("transactionId",transactionId);
		List<PendingTransaction>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getRejectCustomer(java.lang.Long)
	 */
	@Override
	public PendingTransaction getRejectCustomer(Long transactionId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from PendingTransaction pending  where pending.transactionId=:transactionId")
				.setParameter("transactionId",transactionId);
		List<PendingTransaction>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getPendingTransactionsByStatus(java.lang.Integer, int)
	 */
	@Override
	public Page getPendingTransactionsByStatus(Integer bankId,int pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(PendingTransaction.class);
		criteria.add( Restrictions.eq("bank.bankId",bankId));
		criteria.add( Restrictions.eq("status",0));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());


		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(PendingTransaction.class);
		criteria1.add( Restrictions.eq("bank.bankId",bankId));
		criteria1.add( Restrictions.eq("status",0));
		criteria1.addOrder(Order.desc("transactionDate"));
		//criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getTransactionRule(java.lang.Long, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public TransactionRuleTxn getTransactionRule(Long transactionAmount,Integer transactionType,Integer profileId,Integer groupId) {
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from TransactionRuleTxn trt where trt.transactionType.transactionType=:transactionType and trt.transactionRule.approvalLimit<=:transactionAmount " +
				"and trt.transactionRule.maxValueLimit >=:transactionAmount and trt.sourceType=1 AND (trt.transactionRule.referenceId=0 OR trt.transactionRule.referenceId=:profileId OR trt.transactionRule.referenceId=:groupId) order by trt.transactionRule.maxValueLimit")
				.setParameter("transactionType",transactionType)
				.setParameter("transactionAmount",transactionAmount)
				.setParameter("profileId",profileId)
				.setParameter("groupId",groupId);*/

		Query query=getSessionFactory().getCurrentSession().createQuery("from TransactionRuleTxn trt where trt.transactionType.transactionType=:transactionType and trt.transactionRule.approvalLimit<=:transactionAmount " +
				"and trt.transactionRule.maxValueLimit >=:transactionAmount and trt.sourceType=:sourceType AND (trt.transactionRule.referenceId=0 OR trt.transactionRule.referenceId=:profileId OR trt.transactionRule.referenceId=:groupId) order by trt.transactionRule.maxValueLimit")
				.setParameter("transactionType",transactionType)
				.setParameter("transactionAmount",transactionAmount)
				.setParameter("sourceType", 1)
				.setParameter("profileId",profileId)
				.setParameter("groupId",groupId);

		List<TransactionRuleTxn> transactionRuleTxns=query.list();

		return transactionRuleTxns.size()>0 ? transactionRuleTxns.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCustomerAliasFromAccount(java.lang.Long, java.lang.String)
	 */
	@Override
	public CustomerAccount getCustomerAliasFromAccount(Long customerId,
			String accountNumber) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount acc where acc.customer.customerId=:customerId and acc.account.accountNumber=:accountNumber")
				.setParameter("customerId",customerId)
				.setParameter("accountNumber",accountNumber);  
		List<CustomerAccount>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;  
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCustomerAccountBalance(java.lang.String)
	 */
	@Override
	public CustomerAccount getCustomerAccountBalance(String customerId) {

		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount acc where acc.customer.customerId=:customerId")
				.setParameter("customerId",Long.parseLong(customerId));
		List<CustomerAccount>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;  
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getLastTransactions(java.lang.String, int)
	 */
	public List<Transaction> getLastTransactions(String accountNumber,int noOfTransactions) {

		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();

		Query query = session.createSQLQuery("select myresult.*, txn.TransactionDate from (  " +
				"select TransactionID, Amount as Amount, 'CR' as Type,JournalType,CreditDesc as Descript " +
				"from TransactionJournals where CreditAccount=:accountNumber and JournalType= 0  and BookID=1 " +
				"UNION ALL     " +
				"select TransactionID, Amount as Amount, 'DR' AS Type, JournalType,DebitDesc as Descript " +
				"from TransactionJournals where DebitAccount=:accountNumber and JournalType= 0  and BookID=1  " +
				"UNION ALL     " +
				"select TransactionID, sum(Amount) as Amount, 'DR' AS Type, JournalType,DebitDesc as Descript    " +
				"from TransactionJournals where DebitAccount=:accountNumber and journaltype= 1  and BookID=1  " +
				"group by TransactionId, JournalType   " +
				")  myresult, Transactions txn   " +
				"where  myresult.TransactionID=txn.TransactionID ORDER BY txn.TransactionDate desc limit 0,:numberOfTxns")
				.addScalar("TransactionDate",Hibernate.TIMESTAMP).addScalar("Descript",Hibernate.STRING)
				.addScalar("Type",Hibernate.STRING).addScalar("Amount",Hibernate.DOUBLE)
				.setParameter("accountNumber", accountNumber , Hibernate.STRING)
				.setParameter("numberOfTxns", noOfTransactions , Hibernate.INTEGER) ;

		List<Object[]> list = query.list();

		List<Transaction> transactionList = new ArrayList<Transaction>();

		for(Object[] obj : list) {

			Transaction transaction = new Transaction();
			Calendar calendarObj = Calendar.getInstance();
			calendarObj.setTime( (Date)obj[0] );
			transaction.setTransDate( calendarObj );
			transaction.setTransDesc((String)obj[1]);
			transaction.setTransType((String)obj[2]);
			transaction.setAmount((Double)obj[3]);

			transactionList.add(transaction);

		}
		return transactionList;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getLastTransactions(java.lang.String, java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> getLastTransactions(String accountNumber,Date fromDate, Date toDate) {

		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( toDate );
		//calendar.add( Calendar.DATE, 1);

		Query query = session.createSQLQuery(
				"select myresult.*, txn.TransactionDate from (  " + 
						"select TransactionID, Amount as Amount, 'C' as Type,JournalType,CreditDesc as Descript " + 
						"from TransactionJournals where CreditAccount=:accountNumber and JournalType= 0 and BookID=1  " + 
						"UNION ALL     " + 
						"select TransactionID, Amount as Amount, 'D' AS Type, JournalType,DebitDesc as Descript " + 
						"from TransactionJournals where DebitAccount=:accountNumber and JournalType= 0 and BookID=1   " + 
						"UNION ALL     " + 
						"select TransactionID, sum(Amount) as Amount, 'D' AS Type, JournalType,DebitDesc as Descript    " + 
						"from TransactionJournals where DebitAccount=:accountNumber and journaltype= 1 and BookID=1   " + 
						"group by TransactionId, JournalType   " + 
						")  myresult, Transactions txn   " + 
						"                where myresult.TransactionID=txn.TransactionID " + 
						"                       and (txn.TransactionDate between :fromDateString and :toDateString) " + 
						"                ORDER BY txn.TransactionDate desc "
				)
				.addScalar("TransactionDate",Hibernate.TIMESTAMP).addScalar("Descript",Hibernate.STRING)
				.addScalar("Type",Hibernate.STRING).addScalar("Amount",Hibernate.DOUBLE)
				.setParameter("accountNumber", accountNumber , Hibernate.STRING)
				.setParameter("fromDateString", fromDate , Hibernate.TIMESTAMP)
				.setParameter("toDateString", toDate , Hibernate.TIMESTAMP);

		List<Object[]> list = query.list();

		List<Transaction> transactionList = new ArrayList<Transaction>();

		for(Object[] obj : list) {

			Transaction transaction = new Transaction();
			Calendar calendarObj = Calendar.getInstance();
			calendarObj.setTime( (Date)obj[0] );
			transaction.setTransDate( calendarObj );
			transaction.setTransDesc((String)obj[1]);
			transaction.setTransType((String)obj[2]);
			transaction.setAmount((Double)obj[3]);

			transactionList.add(transaction);
		}
		return transactionList;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#searchTxnSummary(java.lang.Integer, java.lang.Integer, com.eot.banking.dto.TxnSummaryDTO, int, java.lang.Long)
	 */
	@Override
	public Page searchTxnSummary(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO, int pageNumber,Long branchId) {

		boolean isLimitRequired = true;
		StringBuffer qryStr = null;

		String mobileNumber= "";
		String benfOrCustMobileNumber="";
		String agentCode="";
		String name="";
		String partnerId="";
		String profileId="";
		String partnerType= "";
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		WebUser webUser = getUser(userName);
		if(txnSummaryDTO.getPartnerId() != null && !"".equals(txnSummaryDTO.getPartnerId())){
			isLimitRequired = false;
//			if(tTypesList.contains(txnSummaryDTO.getTransactionType())){
//				partnerId= " and mechant.partnerId=:partnerId";
//			}else if(tTypesList1.contains(txnSummaryDTO.getTransactionType())) {
//				partnerId= "";
//			}else if(!txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_TXNSTATEMENT))
//				partnerId= " and customer.partnerId=:partnerId"; 	
			partnerId= " and usbp.Id=:partnerId"; 	
		}
		
		if(txnSummaryDTO.getPartnerType() != null && !"".equals(txnSummaryDTO.getPartnerType())){
			isLimitRequired = false;
			partnerType= " and usbp.partnerType=:partnerType"; 				
		}
		if(null != txnSummaryDTO.getTransactionType() && !"".equals(txnSummaryDTO.getTransactionType()+"")){	
			Integer[] tTypes = new Integer[]{115,116,140,90,133,128,135,55,30,35,146};
			Integer[] tTypes1 = new Integer[]{121,137,138,141,143};
			Integer[] tTypes2 = new Integer[]{98,137,138,141};
			Integer[] tTypes3 = new Integer[]{115,116,140,90,133,137,138,141,128,135,55,98,146};
			isLimitRequired = false;

			// Convert Integer Array to List
			List<Integer> tTypesList = Arrays.asList(tTypes);
			List<Integer> tTypesList1 = Arrays.asList(tTypes1);
			List<Integer> tTypesList2 = Arrays.asList(tTypes2);
			List<Integer> tTypesList3 = Arrays.asList(tTypes3);

			if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
				isLimitRequired = false;
				if(tTypesList.contains(txnSummaryDTO.getTransactionType())){
					agentCode= " and mechant.AgentCode=:agentCode";
				}else if(tTypesList1.contains(txnSummaryDTO.getTransactionType())) {
					agentCode= " and usbp.Code=:agentCode";
				}else if(!txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_TXNSTATEMENT))
					agentCode= " and customer.AgentCode=:agentCode";	
			}
			if(txnSummaryDTO.getName() != null && !"".equals(txnSummaryDTO.getName())){
				isLimitRequired = false;
				if(tTypesList.contains(txnSummaryDTO.getTransactionType())){
					name= " and CONCAT(mechant.FirstName, \" \", mechant.LastName) LIKE :name";
				}else if(tTypesList1.contains(txnSummaryDTO.getTransactionType())) {
					name= " and usbp.Name LIKE :name";
				}else if(!txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_TXNSTATEMENT)) {
					name= " AND CONCAT(customer.FirstName, \" \", customer.LastName) LIKE :name";
				}
			}
			if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
				isLimitRequired = false;
				if(tTypesList.contains(txnSummaryDTO.getTransactionType())){
					mobileNumber= " and mechant.mobileNumber=:mobileNumber";
				}else if(!tTypesList2.contains(txnSummaryDTO.getTransactionType())) {
					mobileNumber= " and customer.mobileNumber=:mobileNumber";
				}	
			}
			if(txnSummaryDTO.getBenfOrCustMobileNumber() != null && !"".equals(txnSummaryDTO.getBenfOrCustMobileNumber())){
				isLimitRequired = false;
				if(tTypesList3.contains(txnSummaryDTO.getTransactionType())){
					benfOrCustMobileNumber= " and customer.mobileNumber=:benfOrCustMobileNumber";
				}else if(txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_SMS_CASH_RECV) || txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_SMSCASH)) {
					benfOrCustMobileNumber= " and txn.OtherAccount=:benfOrCustMobileNumber";
				}else if(txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_TRANSFER_EMONEY)  || txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_TYPE_LIMIT_UPDATE))
					benfOrCustMobileNumber= " and partner.Code=:benfOrCustMobileNumber";
				else if(!txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_BALANCE_ENQUIRY)  || !txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_MINISTATEMENT))
					benfOrCustMobileNumber= " and mechant.mobileNumber=:benfOrCustMobileNumber";
			}



			if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId()+"")){
				isLimitRequired = false;
				if(tTypesList.contains(txnSummaryDTO.getTransactionType())){
					profileId= " and mechant.ProfileID=:profileId";
				}else if(tTypesList1.contains(txnSummaryDTO.getTransactionType())) {
					profileId= "";
				}else if(!txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_TXNSTATEMENT))
					profileId= " and customer.ProfileID=:profileId"; 				
			}

		}


		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer qry = new StringBuffer("SELECT count(DISTINCT(txn.TransactionID)) FROM " +
				"( SELECT txn.TransactionID,txn.TransactionType,txn.TransactionDate,txn.customerAccount,txn.referenceId,txn.requestChannel,txn.Status FROM Transactions AS txn where txn.TransactionType not in (60,84,120,31,10,20,120,135,121) ");
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getStatus() != null &&
					txnSummaryDTO.getStatus().equals("2"))
				qry.append(" and txn.Status NOT IN(:status,:status1)"); 
			else
				qry.append(" and txn.Status IN(:status,:status1)");
			if(txnSummaryDTO.getTransactionType()!=null){
				qry.append(" and txn.TransactionType=:Txn ");
			}
			if(txnSummaryDTO.getFromDate()!=null){
				qry.append(" and DATE(txn.TransactionDate)>=:fromDate and DATE(txn.TransactionDate)<=:toDate ");
			}
			if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
				qry.append(" and txn.requestChannel=:requestChannel"); 
			}
			if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
				qry.append(" and txn.TransactionID=:txnId"); 
			}
		}

		qry.append(") txn " +
				"JOIN TransactionTypes tt ON tt.TransactionType = txn.TransactionType  \r\n" +  
				"LEFT JOIN Customer mechant ON txn.referenceId = mechant.customerId\r\n" + 
				"LEFT JOIN WebRequests wr ON wr.TransactionID = txn.TransactionID\r\n" + 
				"LEFT JOIN BusinessPartner AS bp ON bp.Id = mechant.PartnerId\r\n" + 
				"LEFT JOIN BusinessPartnerUser AS bpu ON bpu.UserName = wr.UserName\r\n" + 
				"LEFT JOIN BusinessPartner AS usbp ON usbp.id = bpu.partnerId\r\n" + 
				"LEFT JOIN BusinessPartner AS partner ON partner.Code = txn.referenceId\r\n" + 
				"LEFT JOIN CustomerAccounts AS ca ON ca.AccountNumber = txn.customerAccount " + 
				"LEFT JOIN Customer customer ON customer.CustomerID = ca.CustomerID");




		if (txnSummaryDTO != null) {
			if (txnSummaryDTO.getTransactionType() == null) {
				if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
					qry.append(" where txn.TransactionType not in (60,84,120,31,10,20,120,135,121,144,141,95,117,146,65,61,90,55,83,126,128,108,143,145,99,147)");
				}else
					qry.append(" where txn.TransactionType not in (60,84,120,31,10,20,120,135,121)");				
			}
		}

		if (txnSummaryDTO != null) {
			if (txnSummaryDTO.getTransactionType() != null) {
				qry.append(" where txn.TransactionType=:Txn");
			}
		}

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qry.append(mobileNumber);
		}
		if(txnSummaryDTO.getBenfOrCustMobileNumber() != null && !"".equals(txnSummaryDTO.getBenfOrCustMobileNumber())){	

			qry.append(benfOrCustMobileNumber);
		}
		if(txnSummaryDTO.getName() != null && !"".equals(txnSummaryDTO.getName())){	

			qry.append(name);
		}

		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){

			qry.append(" JOIN WebRequests as webrequests ON txn.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		}
		if(!profileId.equals("")){

			qry.append(profileId);  
		} 

		if(!partnerId.equals("")){	

			qry.append(partnerId);
		}
		if(!partnerType.equals("")){
			qry.append(partnerType);				
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){

			qry.append(agentCode);   
		}

		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){

			qry.append(" and bp.code=:code"); 
		}

		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qry.append(" and  bp.Name LIKE :superAgentName"); 
		}
		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qry.append(" AND CONCAT(customer.FirstName, \" \", customer.LastName) LIKE :customerName");
		}
		if(txnSummaryDTO.getWebUserCode() != null && !"".equals(txnSummaryDTO.getWebUserCode())){
			qry.append(" AND wr.UserName LIKE :userName");
		}

		SQLQuery qryResult = session.createSQLQuery(qry.toString());

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
			qryResult.setParameter("mobileNumber",txnSummaryDTO.getMobileNumber()); 
		}
		if(txnSummaryDTO.getBenfOrCustMobileNumber() != null && !"".equals(txnSummaryDTO.getBenfOrCustMobileNumber())){{
			qryResult.setParameter("benfOrCustMobileNumber", txnSummaryDTO.getBenfOrCustMobileNumber());
		}
		}
		if(txnSummaryDTO.getName() != null && !"".equals(txnSummaryDTO.getName())){	
			qryResult.setParameter("name", "%"+txnSummaryDTO.getName()+"%");
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult.setParameter("userID",txnSummaryDTO.getUserId()+"%");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){
			qryResult.setParameter("profileId",txnSummaryDTO.getProfileId());
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("Txn", txnSummaryDTO.getTransactionType());	
			}
		}
		qryResult.setParameter("status", EOTConstants.TXN_NO_ERROR);
		qryResult.setParameter("status1", EOTConstants.TXN_REVERSAL);
		if(txnSummaryDTO.getPartnerId() != null && !"".equals(txnSummaryDTO.getPartnerId())){
			qryResult.setParameter("partnerId", txnSummaryDTO.getPartnerId());
		}
		if(!partnerType.equals("")){
			qryResult.setParameter("partnerType", txnSummaryDTO.getPartnerType());				
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult.setParameter("requestChannel", "WEB");
			break;
			case "2": qryResult.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult.setParameter("requestChannel", "USSD");
			break;
			}						
		}

		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			qryResult.setParameter("code", txnSummaryDTO.getSuperAgentCode());
		}
		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}

		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryResult.setParameter("txnId",txnSummaryDTO.getTxnId()); 
		}
		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qryResult.setParameter("superAgentName", "%"+txnSummaryDTO.getSuperAgentName()+"%"); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryResult.setParameter("customerName", "%"+txnSummaryDTO.getCustomerName()+"%"); 
		}
		if(txnSummaryDTO.getWebUserCode() != null && !"".equals(txnSummaryDTO.getWebUserCode())){
			qryResult.setParameter("userName", "%"+txnSummaryDTO.getWebUserCode()+"%"); 
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		qryStr = new StringBuffer("SELECT DISTINCT(txn.TransactionID),SUBSTRING_INDEX(SUBSTRING_INDEX(tj.CreditDesc,'/',2),'/',-1) AS Ref_TXN_ID,wr.UserName,\r\n" + 
				"CASE\r\n" + 
				"WHEN txn.TransactionType IN (115,116,140,90,133,128,135,55,30,35,146) THEN mechant.agentCode\r\n" + 
				"WHEN txn.TransactionType IN (121,137,138,141,143) THEN usbp.Code\r\n" + 
				"WHEN txn.TransactionType IN (98) THEN '' \r\n" + 
				"ELSE customer.agentCode \r\n" + 
				"END AS BenfCode,\r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType IN (115,116,140,90,133,128,135,55,30,35,146) THEN CONCAT(mechant.FirstName, \" \", mechant.LastName) \r\n" + 
				"WHEN txn.TransactionType IN (121,137,138,141,143) THEN usbp.Name\r\n" + 
				"WHEN txn.TransactionType IN (98) THEN '' \r\n" + 
				"ELSE  CONCAT(customer.FirstName, \" \", customer.LastName) \r\n" + 
				"END AS InitName,\r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType IN (115,116,140,90,133,128,135,55,30,35,146) THEN mechant.mobileNumber \r\n" + 
				"WHEN txn.TransactionType IN (98,137,138,141) THEN ''\r\n" + 
				"ELSE  customer.mobileNumber \r\n" + 
				"END AS InitMobile,\r\n" + 
				"CASE\r\n" + 
				"WHEN txn.TransactionType IN (115,116,140,90,133,137,138,141,128,135,55,98,146) THEN customer.firstname \r\n" + 
				"WHEN txn.TransactionType IN (30,35,83,126) THEN '' \r\n" + 
				"WHEN txn.TransactionType IN (121,143) THEN partner.Name \r\n" + 
				"ELSE  mechant.firstname\r\n" + 
				"END AS BenfName, \r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType IN (115,116,140,90,133,137,138,141,128,135,55,98,146) THEN customer.mobileNumber \r\n" + 
				"WHEN txn.TransactionType IN (30,35) THEN '' \r\n" + 
				"WHEN txn.TransactionType IN (83,126) THEN txn.OtherAccount\r\n" + 
				"WHEN txn.TransactionType IN (121,143) THEN partner.Code \r\n" + 
				"ELSE  mechant.mobileNumber \r\n" + 
				"END AS BenfMobile,\r\n" + 
				"txn.TransactionDate,tt.TransactionType,tt.Description,txn.Status,\r\n" + 
				"txn.Amount ,(SELECT Amount FROM TransactionJournals WHERE TransactionID=txn.TransactionID AND JournalType=1) AS SC,\r\n" + 
				"txn.requestChannel,bp.Name , bp.Code \r\n" + 
				"FROM ( SELECT txn.* FROM Transactions AS txn ");
		if (txnSummaryDTO != null) {
			if (txnSummaryDTO.getTransactionType() == null) {
				if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
					qryStr.append(" where txn.TransactionType not in (60,84,120,31,10,20,120,135,121,144,141,95,117,146,65,61,90,55,83,126,128,108,143,145,99,147)");
				}else
					qryStr.append(" where txn.TransactionType not in (60,84,120,31,10,20,120,135,121)");				
			}
		}

		if (txnSummaryDTO != null) {
			if (txnSummaryDTO.getTransactionType() != null) {
				qryStr.append(" where txn.TransactionType=:Txn");
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getStatus() != null &&
					txnSummaryDTO.getStatus().equals("2"))
				qryStr.append(" and txn.Status NOT IN(:status,:status1)"); 
			else
				qryStr.append(" and txn.Status IN(:status,:status1)");
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and txn.TransactionType=:Txn ");
			}
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" and DATE(txn.TransactionDate)>=:fromDate and DATE(txn.TransactionDate)<=:toDate ");
			}
			if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
				qryStr.append(" and txn.requestChannel=:requestChannel"); 
			}
			if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
				qryStr.append(" and txn.TransactionID=:txnId"); 
			}
		}

//		if (!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType())) {
//			qryStr.append(" ORDER BY txn.transactionid desc "); 
//		}
		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			isLimitRequired = false;
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			isLimitRequired = false;
		}
		if(!profileId.equals("")){
			isLimitRequired = false; 
		} 

		if(!partnerId.equals("")){	
			isLimitRequired = false;
		}	
		if(!partnerType.equals("")){
			isLimitRequired = false;			
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			isLimitRequired = false; 
		}

		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			isLimitRequired = false;
		}

		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			isLimitRequired = false;
		}
		if(txnSummaryDTO.getWebUserCode() != null && !"".equals(txnSummaryDTO.getWebUserCode())){
			isLimitRequired = false;
		}
		if(!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType()) && isLimitRequired) {
			qryStr.append(" ORDER BY txn.TransactionDate desc "); 
			qryStr.append("  LIMIT :offset,:limit ");
		}
		qryStr.append(") txn " +
				"JOIN TransactionTypes tt ON tt.TransactionType = txn.TransactionType  \r\n" + 
				"LEFT JOIN TransactionJournals tj ON tj.TransactionID=txn.TransactionID AND txn.TransactionType=61\r\n" + 
				"LEFT JOIN Customer mechant ON txn.referenceId = mechant.customerId\r\n" + 
				"LEFT JOIN WebRequests wr ON wr.TransactionID = txn.TransactionID\r\n" + 
				"LEFT JOIN BusinessPartner AS bp ON bp.Id = mechant.PartnerId\r\n" + 
				"LEFT JOIN BusinessPartnerUser AS bpu ON bpu.UserName = wr.UserName\r\n" + 
				"LEFT JOIN BusinessPartner AS usbp ON usbp.id = bpu.partnerId\r\n" + 
				"LEFT JOIN BusinessPartner AS partner ON \r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType IN (121,143) THEN partner.Code = txn.referenceId\r\n" + 
				"END\r\n" + 
				"LEFT JOIN CustomerAccounts AS ca ON \r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType IN (115,116,140,90,126,137,138,135,83,98,30,35) THEN ca.AccountNumber = txn.customerAccount\r\n" + 
				"ELSE ca.AccountNumber = txn.OtherAccount\r\n" + 
				"END\r\n" + 
				"LEFT JOIN Customer customer ON customer.CustomerID = ca.CustomerID");


		if (txnSummaryDTO != null) {
			if (txnSummaryDTO.getTransactionType() == null) {
				if(null != webUser && webUser.getWebUserRole().getRoleId() == EOTConstants.ROLEID_BUSINESS_PARTNER_L2){
					qryStr.append(" where txn.TransactionType not in (60,84,120,31,10,20,120,135,121,144,141,95,117,146,65,61,90,55,83,126,128,108,143,145,99,147)");
				}else
					qryStr.append(" where txn.TransactionType not in (60,84,120,31,10,20,120,135,121)");
			}
		}

		if (txnSummaryDTO != null) {
			if (txnSummaryDTO.getTransactionType() != null) {
				isLimitRequired = false;
				qryStr.append(" where txn.TransactionType=:Txn");
			}
		}

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
			qryStr.append(mobileNumber);
		}
		if(txnSummaryDTO.getBenfOrCustMobileNumber() != null && !"".equals(txnSummaryDTO.getBenfOrCustMobileNumber())){
			qryStr.append(benfOrCustMobileNumber);
		}
		if(txnSummaryDTO.getName() != null && !"".equals(txnSummaryDTO.getName())){	

			qryStr.append(name);
		}

		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			isLimitRequired = false;
			qryStr.append(" JOIN WebRequests as webrequests ON txn.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		}
		if(!profileId.equals("")){
			isLimitRequired = false;
			qryStr.append(profileId);  
		} 

		if(!partnerId.equals("")){	
			isLimitRequired = false;
			qryStr.append(partnerId);
		}	
		if(!partnerType.equals("")){
			isLimitRequired = false;
			qryStr.append(partnerType);				
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			isLimitRequired = false;
			qryStr.append(agentCode);   
		}

		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			isLimitRequired = false;
			qryStr.append(" and bp.code=:code"); 
		}

		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			isLimitRequired = false;
			qryStr.append(" and  bp.Name LIKE :superAgentName"); 
		}
		if(txnSummaryDTO.getWebUserCode() != null && !"".equals(txnSummaryDTO.getWebUserCode())){
			isLimitRequired = false;
			qryStr.append(" AND wr.UserName LIKE :userName");
		}


		/*
		 * if (null!= txnSummaryDTO.getSortBy() &&
		 * txnSummaryDTO.getSortBy().equals("asc")) { qryStr.append("  ORDER BY ")
		 * .append(txnSummaryDTO.getSortColumn()) .append(" ASC"); } else {
		 * qryStr.append("  ORDER BY ") .append(txnSummaryDTO.getSortColumn())
		 * .append(" DESC"); }
		 */
		
		if(!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType()) &&  !isLimitRequired) {
			qryStr.append(" ORDER BY txn.TransactionDate desc "); 
			qryStr.append("  LIMIT :offset,:limit ");
		}
		if(EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType())) {
			qryStr.append(" ORDER BY txn.TransactionDate desc "); 
		}
		Query qryResult1 = session.createSQLQuery(qryStr.toString()); 

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){{
			qryResult1.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		}
		if(txnSummaryDTO.getBenfOrCustMobileNumber() != null && !"".equals(txnSummaryDTO.getBenfOrCustMobileNumber())){{
			qryResult1.setParameter("benfOrCustMobileNumber", txnSummaryDTO.getBenfOrCustMobileNumber());
		}
		}
		if(txnSummaryDTO.getName() != null && !"".equals(txnSummaryDTO.getName())){	
			//qryResult1.setParameter("name", txnSummaryDTO.getName());
			qryResult1.setParameter("name", "%"+txnSummaryDTO.getName()+"%");
		}
		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult1.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult1.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}

		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult1.setParameter("userID",txnSummaryDTO.getUserId()+"%");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId()+"")){
			qryResult1.setParameter("profileId",txnSummaryDTO.getProfileId());
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult1.setParameter("Txn", txnSummaryDTO.getTransactionType());
			}
		}
		if(txnSummaryDTO.getPartnerId() != null && !"".equals(txnSummaryDTO.getPartnerId())){
			qryResult1.setParameter("partnerId", txnSummaryDTO.getPartnerId());
		}
		if(!partnerType.equals("")){
			qryResult1.setParameter("partnerType", txnSummaryDTO.getPartnerType());				
		}

		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult1.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		qryResult1.setParameter("status", EOTConstants.TXN_NO_ERROR);
		qryResult1.setParameter("status1", EOTConstants.TXN_REVERSAL);
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult1.setParameter("requestChannel", "Web");
			break;
			case "2": qryResult1.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult1.setParameter("requestChannel", "USSD");
			break;
			}						
		}	

		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			qryResult1.setParameter("code", txnSummaryDTO.getSuperAgentCode());
		}
		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryResult1.setParameter("txnId",txnSummaryDTO.getTxnId()); 
		}
		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qryResult1.setParameter("superAgentName", "%"+txnSummaryDTO.getSuperAgentName()+"%"); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryResult1.setParameter("customerName", "%"+txnSummaryDTO.getCustomerName()+"%"); 
		}
		if(txnSummaryDTO.getWebUserCode() != null && !"".equals(txnSummaryDTO.getWebUserCode())){
			qryResult1.setParameter("userName", "%"+txnSummaryDTO.getWebUserCode()+"%"); 
		}

		if (!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType())) {
			qryResult1.setParameter("offset", (pageNumber-1)*appConfig.getResultsPerPage()); 
			qryResult1.setParameter("limit", appConfig.getResultsPerPage()); 

			/*
			 * qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
			 * qryResult1.setMaxResults(appConfig.getResultsPerPage());
			 */

		}

		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#exportToXLSForTransactionSummary(java.lang.Integer, java.lang.Integer, com.eot.banking.dto.TxnSummaryDTO, java.lang.Long)
	 */
	@Override
	public List exportToXLSForTransactionSummary(Integer bankGroupId,Integer bankId,TxnSummaryDTO txnSummaryDTO,Long branchId) {

		/*String benificiaryMobileNumber = (null != txnSummaryDTO.getTransactionType() && txnSummaryDTO.getTransactionType().equals(EOTConstants.TXN_ID_AGETNT_DEPOSIT))
				? " (SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND (transactions.TransactionType=:txn OR transactions.TransactionType=:tnxn2 OR transactions.TransactionType=:tnxn1  OR transactions.TransactionType=:tnxn5 OR transactions.TransactionType=:tnxn6 ))) AS BenificiaryMobileNumber,(SELECT bank.BankName FROM Bank AS bank WHERE bank.BankID IN(SELECT ca.BankID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.DebitAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType=:txn1))" 
				: " (SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND (transactions.TransactionType=:txn  OR transactions.TransactionType=:tnxn1  OR transactions.TransactionType=:tnxn5 OR transactions.TransactionType=:tnxn6 ))) AS BenificiaryMobileNumber,(SELECT bank.BankName FROM Bank AS bank WHERE bank.BankID IN(SELECT ca.BankID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.DebitAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType=:txn1))";
		 */		
		StringBuffer  qryStr = new StringBuffer("SELECT customer.FirstName,customer.MobileNumber,transactions.amount,transactions.TransactionType,transactions.TransactionDate,bank.BankName,branch.Location," +
				" country.CountryID,customer.ProfileID,customer.ProfileID,customer.LastName,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=:jounralType) AS SC," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd) AND tj.TransactionID=transactions.TransactionID ) AS stampFee," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd1) AND tj.TransactionID=transactions.TransactionID ) AS tax," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd2) AND tj.TransactionID=transactions.TransactionID) AS bankShare," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd3) AND tj.TransactionID=transactions.TransactionID) AS gimShare,(SELECT bank.BankName FROM Bank AS bank WHERE bank.BankID IN(SELECT ca.BankID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType=:txn)) AS BenificiaryBank," +
				" (SELECT branch.Location FROM Branch AS branch WHERE branch.BranchID IN(SELECT ca.BranchID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType=:txn)) AS BenificiaryBranch," +
				//benificiaryMobileNumber +
				"(SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND (transactions.TransactionType=:txn OR transactions.TransactionType=:tnxn2 OR transactions.TransactionType=:tnxn1 OR transactions.TransactionType=:tnxn7 ))) AS BenificiaryMobileNumber," +
				"(SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.DebitAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND (transactions.TransactionType=:tnxn6  ))) AS MerchPayOutBenificiaryMobile," +
				"(SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND (transactions.TransactionType=:tnxn5 ))) AS FloatMgmtBenificiaryMobile," +
				"(SELECT bank.BankName FROM Bank AS bank WHERE bank.BankID IN(SELECT ca.BankID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.DebitAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType=:txn1))"+

				" AS BenificiaryBankforsale,(SELECT branch.Location FROM Branch AS branch WHERE branch.BranchID IN(SELECT ca.BranchID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.DebitAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType=:txn1)) AS BenificiaryBranchforsale," +
				" (SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.DebitAccount= transactions.CustomerAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType IN(:txn1,:tnxn2,:tnxn3))) AS BenificiaryMobileNumberforsale,(SELECT transactions.OtherAccount FROM TransactionJournals tj WHERE transactions.TransactionID=tj.TransactionID AND (transactions.TransactionType=:txnType OR transactions.TransactionType=:tnxn4) AND tj.JournalType=:jounralType) AS BenificiaryMobileNumberforSMSCash,transactions.CustomerAccountType, (SELECT wr.TransactionType FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID AND transactions.TransactionType IN (:trns1,:trns2)) AS RealType  FROM Transactions AS transactions " +

				" INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID " +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID " +
				" JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID ");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}	    
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId())){

			qryStr.append(" and country.CountryID=:countryID");
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankgroupID");  
		} 

		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){

			qryStr.append(" and customer.ProfileID =:profileID");  
		} 
		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankgroupID"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankID");   
		} 

		if(branchId != null){

			qryStr.append(" and branch.BranchID='"+branchId+"'");   
		} 

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				// Removed Add Payee from the Transaction Details report in MIS. bug no:5702, by vineeth on 26-07-2018
				qryStr.append(" and transactions.TransactionType !=:trans3 and transactions.TransactionType !=:trans4 and transactions.TransactionType !=:trans5  ");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and transactions.TransactionType=:trans6");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate ");
			}
		}

		qryStr.append(" and customer.ProfileID=custprof.ProfileID and transactions.Status=:status"); 
		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){	

			qryStr.append(" and customer.partnerId=:partnerID");
		}

		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){

			qryStr.append(" and customer.AgentCode=:agentCode");   
		}
		if(txnSummaryDTO.getStatus() != null && txnSummaryDTO.getStatus().equals("2"))
			qryStr.append(" and transactions.Status!=:status");   
		else 
			qryStr.append(" and transactions.Status=:status"); 
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
			qryStr.append(" and transactions.requestChannel=:requestChannel"); 
		}
		qryStr.append(" ORDER BY transactions.TransactionDate DESC"); 
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());
		qryResult.setParameter("jounralType", 1);
		qryResult.setParameter("ahd", 63);
		qryResult.setParameter("ahd1", 54);
		qryResult.setParameter("ahd2", 55);
		qryResult.setParameter("ahd3", 102);
		qryResult.setParameter("txn", 55);
		qryResult.setParameter("txn1", 90);

		qryResult.setParameter("tnxn1", 128);
		qryResult.setParameter("tnxn2", 115);
		qryResult.setParameter("tnxn3", 116);
		qryResult.setParameter("tnxn4", 126);
		qryResult.setParameter("tnxn5", 133);
		qryResult.setParameter("tnxn6", 140);
		qryResult.setParameter("tnxn7", 135);

		qryResult.setParameter("txnType", 83);
		qryResult.setParameter("trns1", 60);
		qryResult.setParameter("trns2", 61);
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
			qryResult.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId())){
			qryResult.setParameter("countryID", txnSummaryDTO.getCountryId());	
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId())){
			qryResult.setParameter("bankID", txnSummaryDTO.getBankId());	
		}
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	
			qryResult.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());	
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId())){
			qryResult.setParameter("branchID", txnSummaryDTO.getBranchId());	
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryResult.setParameter("bankgroupID", txnSummaryDTO.getBankGroupId());	
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){
			qryResult.setParameter("profileID", txnSummaryDTO.getProfileId());	
		}
		if(bankGroupId != null){
			qryResult.setParameter("bankgroupID",bankGroupId);	
		}
		if(bankId != null){
			qryResult.setParameter("bankID", bankId);	
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult.setParameter("trans3", 60);
				qryResult.setParameter("trans4", 84);
				qryResult.setParameter("trans5", 31);
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("trans6", txnSummaryDTO.getTransactionType());		
			}
		}

		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){
			qryResult.setParameter("partnerID", txnSummaryDTO.getPartnerId());
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult.setParameter("requestChannel", "WEB");
			break;
			case "2": qryResult.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult.setParameter("requestChannel", "USSD");
			break;
			}						
		}
		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}
		qryResult.setParameter("status", EOTConstants.TXN_NO_ERROR);

		return qryResult.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#exportToXLSForPendingTransactionSummary(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Long)
	 */
	@Override
	public List exportToXLSForPendingTransactionSummary(String customerName,String mobileNumber, String txnDate, String amount, String txnType,String status,String fromDate,String toDate,Integer bankId,Long branchId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(PendingTransaction.class);
		criteria.add(Restrictions.eq("bank.bankId", bankId));

		if( customerName!= null && ! "".equals(customerName) ){
			criteria.createAlias("customer", "customer");
			criteria.add(Restrictions.like("customer.firstName","%"+customerName+"%"));
		}if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			criteria.createAlias("customer", "customer");
			criteria.add(Restrictions.like("customer.mobileNumber","%"+mobileNumber+"%"));
		}
		if( amount!= null && ! "".equals(amount) ){
			criteria.add(Restrictions.eq("amount", Double.valueOf(amount)));
		}	
		if( txnDate!= null && ! "".equals(txnDate) ){
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+txnDate+"'"));
		}
		if( txnType!= null && ! "".equals(txnType) && !txnType.equals("select")){
			criteria.add(Restrictions.eq("transactionType.transactionType", Integer.parseInt(txnType)));	
		}
		if(status!=null && ! "".equals(status)){
			criteria.add(Restrictions.eq("status", Integer.parseInt(status)));
		}
		if(branchId!=null){	
			criteria.createAlias("customer", "customer");
			criteria.createCriteria("customer.customerAccounts","custAcc");
			criteria.add(Restrictions.eq("custAcc.branch.branchId", branchId));
		}
		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'"));
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\") like '"+toDate.toUpperCase()+"'"));
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(TransactionDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'"));
		}
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#exportToXLSForTransactionSummaryForBankTellerEOD(java.lang.Integer, java.lang.Integer, com.eot.banking.dto.TxnSummaryDTO, java.lang.Long)
	 */
	@Override
	public List exportToXLSForTransactionSummaryForBankTellerEOD(Integer bankGroupId, Integer bankId, TxnSummaryDTO txnSummaryDTO,Long branchId) {
		StringBuffer  qryStr = new StringBuffer("SELECT customer.FirstName,customer.MobileNumber,transactions.amount,transactions.TransactionType,transactions.TransactionDate,bank.BankName,branch.Location,country.CountryID," +
				"webrequests.UserName,customer.ProfileID,customer.LastName,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=:journalType)AS SC," +
				"webuser.RoleId,webuser.FirstName AS WebuserName,(SELECT wr.TransactionType FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID AND transactions.TransactionType IN (:txnType1,:txnType2)) AS RealType FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN" +
				" CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID JOIN WebRequests AS webrequests ON transactions.TransactionID=webrequests.TransactionID JOIN WebUsers AS webuser ON webuser.UserName=webrequests.UserName ");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}	    
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId())){

			qryStr.append(" and country.CountryID=:countryID");
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupID");  
		} 
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){

			qryStr.append(" and webrequests.UserName=:userID");  
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){

			qryStr.append(" and customer.ProfileID =:profileID");  
		} 
		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupId"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankId");   
		} 
		if(branchId != null){

			qryStr.append(" and branch.BranchID=:branchId");   
		} 

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryStr.append(" and transactions.TransactionType !=:txnType3 and transactions.TransactionType !=:txnType4 ");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and transactions.TransactionType=:txn");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate ");
			}
		}

		qryStr.append(" and customer.ProfileID=custprof.ProfileID and transactions.Status=:status"); 
		qryStr.append(" ORDER BY transactions.TransactionDate DESC");
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());
		qryResult.setParameter("journalType", 1);	
		qryResult.setParameter("txnType1", 60);
		qryResult.setParameter("txnType2", 61);
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	
			qryResult.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId())){
			qryResult.setParameter("countryID", txnSummaryDTO.getCountryId());
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId())){
			qryResult.setParameter("bankID", txnSummaryDTO.getBankId());
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId())){
			qryResult.setParameter("branchID", txnSummaryDTO.getBranchId());
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryResult.setParameter("bankGroupID", txnSummaryDTO.getBankGroupId());
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult.setParameter("userID", txnSummaryDTO.getUserId());
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){
			qryResult.setParameter("profileID", txnSummaryDTO.getProfileId());
		}
		if(bankGroupId != null){
			qryResult.setParameter("bankGroupId", bankGroupId);
		}
		if(bankId != null){
			qryResult.setParameter("bankId", bankId);
		}
		if(branchId != null){
			qryResult.setParameter("branchId", branchId);
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult.setParameter("txnType3", 60);
				qryResult.setParameter("txnType4", 84);
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("txn", txnSummaryDTO.getTransactionType());	
			}
		}

		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}
		qryResult.setParameter("status", EOTConstants.TXN_NO_ERROR);

		return qryResult.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#exportToXLSForTransactionSummaryForTxnSummary(java.lang.Integer, java.lang.Integer, com.eot.banking.dto.TxnSummaryDTO, java.lang.Long)
	 */
	@Override
	public List exportToXLSForTransactionSummaryForTxnSummary(Integer bankGroupId, Integer bankId, TxnSummaryDTO txnSummaryDTO,Long branchId) {

		String fromDate=DateUtil.formatDate(txnSummaryDTO.getFromDate());
		String toDate=DateUtil.formatDate(txnSummaryDTO.getToDate());

		StringBuffer  qryStr = new StringBuffer("SELECT SUM(transactions.amount),COUNT(transactions.TransactionID),transactions.TransactionType,branch.Location,(SELECT SUM(amount) FROM TransactionJournals WHERE DATE(JournalTime)>=:fromDate AND DATE(JournalTime)<=:toDate and Active!=:active and TransactionID IN(SELECT txn.TransactionID FROM Transactions txn" +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND  branch1.Location=branch.Location AND JournalType=:journalType)) AS SC,(SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount" +
				" IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd and  DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS tax," +
				" (SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd1 and DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active "  +
				" AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID" +
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID" +
				" JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS stampFee," +
				" (SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd2 and  DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active " +
				" AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND  branch1.Location=branch.Location)) AS gimShare," +
				" (SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd3 and  DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn" +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS bankShare," +
				" transactions.TransactionDate,(SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn1 AND wr.TransactionType=:txn2) AS RealDepositAmount," +
				" (SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn1 AND wr.TransactionType=:txn3) AS RealWithdrwalAmount,(SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn4 AND wr.TransactionType=:txn2) AS CancelRealDepositAmount," +
				" (SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn4 AND wr.TransactionType=:txn3) AS CancelRealWithdrwalAmount FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID" +
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID " +
				" JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID ");


		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qryStr.append(" and customer.ProfileID =:profileID");  
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankgroupID");  
		} 	

		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupId"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankID");   
		} 
		if(branchId != null){

			qryStr.append(" and branch.BranchID=:branchID");   
		} 

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryStr.append(" and transactions.TransactionType !=:txn4 and transactions.TransactionType !=:txn5  and transactions.TransactionType !=:txn6  and transactions.TransactionType !=:txn8  and transactions.TransactionType !=:txn9 ");
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and transactions.TransactionType=:txnType");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate ");
			}
		}

		qryStr.append(" and customer.ProfileID=custprof.ProfileID and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' GROUP BY branch.Location,transactions.TransactionType"); 
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());
		qryResult.setParameter("fromDate", fromDate);
		qryResult.setParameter("toDate", toDate);
		qryResult.setParameter("active", 10);
		qryResult.setParameter("journalType", 1);
		qryResult.setParameter("ahd", 54);
		qryResult.setParameter("ahd1", 63);
		qryResult.setParameter("ahd2", 102);
		qryResult.setParameter("ahd3", 55);
		qryResult.setParameter("txn1", 61);
		qryResult.setParameter("txn2", 95);
		qryResult.setParameter("txn3", 99);
		qryResult.setParameter("txn4", 60);

		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){
			qryResult.setParameter("bankID", txnSummaryDTO.getBank());
		}
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	
			qryResult.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){
			qryResult.setParameter("profileID", txnSummaryDTO.getProfileId());
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){
			qryResult.setParameter("branchID", txnSummaryDTO.getBranchId());
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qryResult.setParameter("bankgroupID", txnSummaryDTO.getBankGroupId());
		}
		if(bankGroupId != null){
			qryResult.setParameter("bankGroupId", bankGroupId);
		}
		if(bankId != null){
			qryResult.setParameter("bankID", bankId);
		}
		if(branchId != null){
			qryResult.setParameter("branchID", branchId);
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult.setParameter("txn5", 84);
				qryResult.setParameter("txn6", 31);
				qryResult.setParameter("txn8", 137);
				qryResult.setParameter("txn9", 138);
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("txnType", txnSummaryDTO.getTransactionType());
			}
		}

		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}


		return qryResult.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#exportToXLSForTransactionSummaryPerBank(java.lang.Integer, java.lang.Integer, com.eot.banking.dto.TxnSummaryDTO)
	 */
	@Override
	public List exportToXLSForTransactionSummaryPerBank(Integer bankGroupId, Integer bankId, TxnSummaryDTO txnSummaryDTO) {
		String fromDate=DateUtil.formatDate(txnSummaryDTO.getFromDate());
		String toDate=DateUtil.formatDate(txnSummaryDTO.getToDate());
		StringBuffer  qryStr = new StringBuffer("SELECT SUM(transactions.amount),COUNT(transactions.TransactionID),transactions.TransactionType,bank.BankName,(SELECT SUM(amount) FROM TransactionJournals WHERE DATE(JournalTime)>=:fromDate AND DATE(JournalTime)<=:toDate and Active!=:active and  TransactionID IN(SELECT txn.TransactionID FROM Transactions txn" +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank1 ON custacc.BankID=bank1.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
				" ON bank1.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND  bank1.BankName=bank.BankName AND JournalType=:journalType)) AS SC,(SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount" +
				" IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd and  DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank1 ON custacc.BankID=bank1.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank1.BankID=custprof.BankID WHERE txn.TransactionType=transactions.TransactionType AND bank1.BankName=bank.BankName)) AS tax," +
				" (SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd1 and  DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active " +
				" AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID" +
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank1 ON custacc.BankID=bank1.BankID" +
				" JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof ON bank1.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND bank1.BankName=bank.BankName)) AS stampFee," +
				" (SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd2 and  DATE(tj.JournalTime)>=:fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active " +
				" AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank1 ON custacc.BankID=bank1.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank1.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND  bank1.BankName=bank.BankName)) AS gimShare," +
				" (SELECT SUM(tj.amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:ahd3 and  DATE(tj.JournalTime)>=fromDate AND DATE(tj.JournalTime)<=:toDate) and Active!=:active AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn" +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank1 ON custacc.BankID=bank1.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank1.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND bank1.BankName=bank.BankName)) AS bankShare," +
				" transactions.TransactionDate,(SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=toDate AND transactions.TransactionType =:txn1 AND wr.TransactionType=:txn2) AS RealDepositAmount," +
				" (SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn1 AND wr.TransactionType=:txn3) AS RealWithdrwalAmount,(SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn4 AND wr.TransactionType=:txn2) AS CancelRealDepositAmount," +
				" (SELECT SUM(transactions.amount) FROM WebRequests wr JOIN Transactions transactions WHERE wr.TransactionID=transactions.TransactionID AND DATE(wr.TransactionTime)>=:fromDate AND DATE(wr.TransactionTime)<=:toDate AND transactions.TransactionType =:txn4 AND wr.TransactionType=::txn3) AS CancelRealWithdrwalAmount FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID" +
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID " +
				" JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID ");


		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qryStr.append(" and customer.ProfileID =:profileID");  
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankgroupID");  
		} 	

		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankgroupID"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankID");   
		} 
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){			
				qryStr.append(" and transactions.TransactionType !=:txn5  and transactions.TransactionType !=:txn6  and transactions.TransactionType !=:txn7 ");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and transactions.TransactionType=:txnType");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=toDate ");
			}
		}

		qryStr.append(" and customer.ProfileID=custprof.ProfileID and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' GROUP BY bank.BankName,transactions.TransactionType"); 
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());
		qryResult.setParameter("fromDate", fromDate);
		qryResult.setParameter("toDate", toDate);
		qryResult.setParameter("active", 10);
		qryResult.setParameter("journalType", 1);
		qryResult.setParameter("ahd", 54);
		qryResult.setParameter("ahd1", 63);
		qryResult.setParameter("ahd2", 102);
		qryResult.setParameter("ahd3", 55);

		qryResult.setParameter("txn1", 61);
		qryResult.setParameter("txn2", 95);
		qryResult.setParameter("txn3", 99);
		qryResult.setParameter("txn4", 60);
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){
			qryResult.setParameter("bankID", txnSummaryDTO.getBankId());
		}
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
			qryResult.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){
			qryResult.setParameter("profileID", txnSummaryDTO.getProfileId());	
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){
			qryResult.setParameter("branchID", txnSummaryDTO.getBranchId());	
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qryResult.setParameter("bankgroupID", txnSummaryDTO.getBankGroupId());
		}
		if(bankGroupId != null){
			qryResult.setParameter("bankgroupID",bankGroupId );
		}
		if(bankId != null){
			qryResult.setParameter("bankID",bankId );
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult.setParameter("txn5",60 );	
				qryResult.setParameter("txn6",84 );	
				qryResult.setParameter("txn7",31 );	
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("txnType",txnSummaryDTO.getTransactionType() );
			}
		}


		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}


		return qryResult.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#exportToXLSForTransactionSummaryForBankTeller(java.lang.Integer, java.lang.Integer, com.eot.banking.dto.TxnSummaryDTO, java.lang.String)
	 */
	@Override
	public List exportToXLSForTransactionSummaryForBankTeller(Integer bankGroupId, Integer bankId, TxnSummaryDTO txnSummaryDTO,String userName) {
		StringBuffer  qryStr = new StringBuffer("SELECT customer.FirstName,customer.MobileNumber,transactions.amount,transactions.TransactionType,transactions.TransactionDate,bank.BankName,branch.Location,country.CountryID," +
				" webrequests.UserName,customer.ProfileID,customer.LastName,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=:journalType) AS SC,webuser.RoleId,webuser.FirstName AS WebuserName,(SELECT wr.TransactionType FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID AND transactions.TransactionType IN (:txn1,:txn2)) AS RealType FROM Transactions AS transactions" +
				" INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID" +
				" JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID" +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID JOIN WebRequests AS webrequests ON transactions.TransactionID=webrequests.TransactionID JOIN WebUsers AS webuser ON webuser.UserName=webrequests.UserName ");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}	    
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId())){

			qryStr.append(" and country.CountryID=:countryID");
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankgroupID");  
		} 
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){

			qryStr.append(" and webrequests.UserName=:userID");  
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){

			qryStr.append(" and customer.ProfileID =:profileID");  
		} 
		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupId"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankID");   
		} 

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and transactions.TransactionType=:txnType");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate ");
			}
		}

		qryStr.append(" and customer.ProfileID=custprof.ProfileID and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' and webuser.UserName='"+userName+"' and transactions.TransactionType!='"+EOTConstants.TXN_ID_REVERSAL+"' and transactions.TransactionType!='"+EOTConstants.TXN_ID_CANCEL+"' and transactions.TransactionType!='"+EOTConstants.TXN_ID_SMSCASH_OTHERS+"'"); 
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());
		qryResult.setParameter("journalType", 1);
		qryResult.setParameter("txn1", 60);
		qryResult.setParameter("txn2", 61);
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	
			qryResult.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId())){
			qryResult.setParameter("countryID", txnSummaryDTO.getCountryId());
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId())){
			qryResult.setParameter("bankID", txnSummaryDTO.getBankId());	
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId())){
			qryResult.setParameter("branchID", txnSummaryDTO.getBranchId());
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryResult.setParameter("bankgroupID", txnSummaryDTO.getBankGroupId());
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult.setParameter("userID", txnSummaryDTO.getUserId());
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){
			qryResult.setParameter("profileID", txnSummaryDTO.getProfileId());
		}
		if(bankGroupId != null){
			qryResult.setParameter("bankGroupId", bankGroupId);
		}
		if(bankId != null){
			qryResult.setParameter("bankID", bankId);
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("txnType", txnSummaryDTO.getTransactionType());	
			}
		}

		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}


		return qryResult.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getCancellations(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, int, java.lang.Long)
	 */
	@Override
	public Page getCancellations(String customerName, String mobileNumber,String amount, String txnDate, String txnType, Integer bankId,String fromDate, String toDate, int pageNumber,Long branchId) {
		StringBuffer qryStr = null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		StringBuffer qry = new StringBuffer("SELECT COUNT(DISTINCT(transactions.TransactionID)) FROM Transactions AS transactions " +
				"INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID" +
				"	JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID JOIN SettlementJournals AS sj ON sj.TransactionID=transactions.TransactionID " +
				"AND (transactions.TransactionType='"+EOTConstants.TXN_ID_DEPOSIT+"' OR transactions.TransactionType='"+EOTConstants.TXN_ID_WITHDRAWAL+"') AND transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' ");

		if(customerName != null && !"".equals(customerName)){

			qry.append(" and customer.FirstName='"+customerName+"'");
		}
		if(mobileNumber != null && !"".equals(mobileNumber)){

			qry.append(" and customer.MobileNumber='"+mobileNumber+"'");
		}
		if(amount != null && !"".equals(amount)){

			qry.append(" and transactions.amount='"+amount+"'");
		}       
		if(bankId != null){

			qry.append(" and bank.BankID='"+bankId+"'");   
		}    
		if(branchId != null){

			qry.append(" and branch.BranchID='"+branchId+"'");   
		}  

		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			qry.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'");
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			qry.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\") like '"+toDate.toUpperCase()+"'");
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qry.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'");
		}

		if(txnType!=null && !"".equals(txnType)){
			if(txnType.equals("95")){
				qry.append(" and transactions.TransactionType='"+EOTConstants.TXN_ID_DEPOSIT+"' ");  
			}
			if(txnType.equals("99")){
				qry.append(" and transactions.TransactionType='"+EOTConstants.TXN_ID_WITHDRAWAL+"' ");  
			}		
		}

		qry.append(" and transactions.Status!=10 and customer.ProfileID=custprof.ProfileID AND sj.BookID=2 AND sj.BatchID IS NULL");
		SQLQuery qryResult = session.createSQLQuery(qry.toString());		 


		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());


		qryStr = new StringBuffer("SELECT DISTINCT(transactions.TransactionID),customer.FirstName,customer.LastName,customer.MobileNumber,transactions.TransactionType,transactions.TransactionDate,transactions.amount FROM Transactions AS transactions " +
				"INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID" +
				"	JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID JOIN SettlementJournals AS sj ON sj.TransactionID=transactions.TransactionID " +
				"AND (transactions.TransactionType='"+EOTConstants.TXN_ID_DEPOSIT+"' OR transactions.TransactionType='"+EOTConstants.TXN_ID_WITHDRAWAL+"') AND transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' ");
		if(customerName != null && !"".equals(customerName)){

			qryStr.append(" and customer.FirstName='"+customerName+"'");
		}
		if(mobileNumber != null && !"".equals(mobileNumber)){

			qryStr.append(" and customer.MobileNumber='"+mobileNumber+"'");
		}
		if(amount != null && !"".equals(amount)){

			qryStr.append(" and transactions.amount='"+amount+"'");
		}	

		if(bankId != null){

			qryStr.append(" and bank.BankID='"+bankId+"'");   
		} 
		if(branchId != null){

			qryStr.append(" and branch.BranchID='"+branchId+"'");   
		}  
		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			qryStr.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'");
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			qryStr.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")like '"+toDate.toUpperCase()+"'");
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qryStr.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'");
		}
		if(txnType!=null && !"".equals(txnType)){
			if(txnType.equals("95")){
				qryStr.append(" and transactions.TransactionType='"+EOTConstants.TXN_ID_DEPOSIT+"' ");  
			}
			if(txnType.equals("99")){
				qryStr.append(" and transactions.TransactionType='"+EOTConstants.TXN_ID_WITHDRAWAL+"' ");  
			}			
		}
		qryStr.append(" and transactions.Status!=10 and customer.ProfileID=custprof.ProfileID AND sj.BookID=2 AND sj.BatchID IS NULL ORDER BY transactions.TransactionDate DESC");
		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());


		//qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//qryResult1.setMaxResults(appConfig.getResultsPerPage());
		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getTransactionByTxnId(java.lang.Integer)
	 */
	@Override
	public com.eot.entity.Transaction getTransactionByTxnId(Integer transactionId) {

		Query query=getSessionFactory().getCurrentSession().createQuery("from Transaction where transactionId=:transactionId")
				.setParameter("transactionId",transactionId.longValue());
		List<com.eot.entity.Transaction>list=query.list();
		return list.size() > 0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getAdjustmentTransactions(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.Integer)
	 */
	@Override
	public Page getAdjustmentTransactions(String customerName, String mobileNumber,String amount, String txnDate, String txnType,String fromDate, String toDate, int pageNumber, Integer bankId) {
		StringBuffer qryStr = null;
		String fisrtName="";
		String lastName="";
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		if(customerName!= null){
			String name= customerName.trim();
			String[] output = name.split(" ");
			if(output.length > 1)
			{
				fisrtName =output[0];
				lastName = output[1];
			}
			else
			{
				fisrtName =output[0];
			}
		}

		StringBuffer qry = new StringBuffer("SELECT COUNT(DISTINCT(transactions.TransactionID)) FROM Transactions AS transactions " +
				"INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID" +
				"	JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID JOIN SettlementJournals AS sj ON sj.TransactionID=transactions.TransactionID " +
				/*"	JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID " +*/
				"AND (transactions.TransactionType=:txnidDepo OR transactions.TransactionType=:txnidwith) AND transactions.Status=:status");//OR transactions.TransactionType=:txnidreve

		if(fisrtName != null && !"".equals(fisrtName)){
			qry.append(" and customer.FirstName=:fisrtName");
		}	
		if(lastName != null && !"".equals(lastName)){
			qry.append(" and customer.LastName=:lastName");
		}
		if((fisrtName != null && !"".equals(fisrtName)) && (lastName != null && !"".equals(lastName))){
			qry.append(" and customer.FirstName=:fisrtName and customer.LastName=:lastName");
		}

		if(mobileNumber != null && !"".equals(mobileNumber)){

			qry.append(" and customer.MobileNumber=:mobileNumber");
		}
		if(amount != null && !"".equals(amount)){

			qry.append(" and transactions.amount=:amount");
		}      

		if(bankId != null){

			qry.append(" and bank.BankID=:bankId");   
		} 

		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			qry.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\") like :fromDate");
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			qry.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")like :toDate");
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qry.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")  between :fromDate and :toDate ");
		}

		if(txnType!=null && !"".equals(txnType)){
			if(txnType.equals("128")){
				qry.append(" and transactions.TransactionType=:txnType1 ");  
			}
			if(txnType.equals("90")){
				qry.append(" and transactions.TransactionType=:txnType2");  
			}
			/*if(txnType.equals("55")){
				qry.append(" and transactions.TransactionType=:txnType3");  
			}*/
		}

		qry.append(" and transactions.Status!=:notstatus and customer.ProfileID=custprof.ProfileID AND sj.BookID=:bookID AND sj.BatchID IS NULL");
		/*qry.append(" and customer.ProfileID=custprof.ProfileID ");*/

		SQLQuery qryResult = session.createSQLQuery(qry.toString());
		qryResult.setParameter("txnidDepo", EOTConstants.TXN_ID_PAY);
		qryResult.setParameter("txnidwith", EOTConstants.TXN_ID_SALE);
		//	qryResult.setParameter("txnidreve", EOTConstants.TXN_ID_TRFDIRECT);
		qryResult.setParameter("status", EOTConstants.TXN_NO_ERROR);

		if(fisrtName != null && !"".equals(fisrtName)){
			qryResult.setParameter("fisrtName", fisrtName);
		}
		if(lastName != null && !"".equals(lastName)){
			qryResult.setParameter("lastName", lastName);
		}
		if((fisrtName != null && !"".equals(fisrtName)) && (lastName != null && !"".equals(lastName))){
			qryResult.setParameter("fisrtName", fisrtName);
			qryResult.setParameter("lastName", lastName);
		}
		if(mobileNumber != null && !"".equals(mobileNumber)){
			qryResult.setParameter("mobileNumber", mobileNumber);
		}
		if(amount != null && !"".equals(amount)){
			qryResult.setParameter("amount", amount);
		}
		if(bankId != null){
			qryResult.setParameter("bankId", bankId);
		}
		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){
			qryResult.setParameter("fromDate", fromDate.toUpperCase()+"%");
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){
			qryResult.setParameter("toDate", toDate.toUpperCase()+"%");
		}
		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qryResult.setParameter("fromDate", fromDate.toUpperCase());
			qryResult.setParameter("toDate", toDate.toUpperCase());
		}
		if(txnType!=null && !"".equals(txnType)){
			if(txnType.equals("128")){
				qryResult.setParameter("txnType1", EOTConstants.TXN_ID_PAY);	
			}
			if(txnType.equals("90")){
				qryResult.setParameter("txnType2", EOTConstants.TXN_ID_SALE);
			}
			/*if(txnType.equals("55")){
			qryResult.setParameter("txnType3", EOTConstants.TXN_ID_TRFDIRECT);
		}*/
		}
		qryResult.setParameter("notstatus",10);
		qryResult.setParameter("bookID", 2);


		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());


		qryStr = new StringBuffer("SELECT DISTINCT(transactions.TransactionID),customer.FirstName,customer.LastName,customer.MobileNumber,transactions.TransactionType,transactions.TransactionDate,transactions.amount FROM Transactions AS transactions " +
				"INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID" +
				"	JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID JOIN SettlementJournals AS sj ON sj.TransactionID=transactions.TransactionID " +
				/*"	JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID " +*/
				"AND (transactions.TransactionType=:txnidDepo OR transactions.TransactionType=:txnidwith) AND transactions.Status=:status");// OR transactions.TransactionType=:txnidreve
		if(fisrtName != null && !"".equals(fisrtName)){
			qryStr.append(" and customer.FirstName=:fisrtName");
		}	
		if(lastName != null && !"".equals(lastName)){
			qryStr.append(" and customer.LastName=:lastName");
		}
		if((fisrtName != null && !"".equals(fisrtName))&& (lastName != null && !"".equals(lastName))){
			qryStr.append(" and customer.FirstName=:fisrtName and customer.LastName=:lastName");
		}

		if(mobileNumber != null && !"".equals(mobileNumber)){

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}
		if(amount != null && !"".equals(amount)){

			qryStr.append(" and transactions.amount=:amount");
		}	

		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankId ");   
		} 
		String fromDate1=null;
		String toDate1=null;
		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			fromDate1=DateUtil.txnDate(DateUtil.stringToDate(fromDate));
			toDate1=DateUtil.txnDate(DateUtil.stringToDate(toDate)); 

		}

		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			qryStr.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\") like :fromDate");
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			qryStr.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")like :toDate");
		}		

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qryStr.append(" where DATE_FORMAT(transactions.TransactionDate,\"%d/%m/%Y\")  between :fromDate and :toDate ");
		}

		/*if((fromDate1!=null && ! "".equals( fromDate1)) && (toDate1!=null && ! "".equals( toDate1))){
			qryStr.append("where DATE(transactions.TransactionDate)>=:fromDate AND DATE(transactions.TransactionDate)<=:toDate");
		}*/
		if(txnType!=null && !"".equals(txnType)){
			if(txnType.equals("128")){
				qryStr.append(" and transactions.TransactionType=:txnType1");  
			}
			if(txnType.equals("90")){
				qryStr.append(" and transactions.TransactionType=:txnType2");  
			}
			/*if(txnType.equals("55")){
				qryStr.append(" and transactions.TransactionType=:txnType3");  
			}*/
		}
		qryStr.append(" and transactions.Status!=:notstatus and customer.ProfileID=custprof.ProfileID AND sj.BookID=:bookID AND sj.BatchID IS NULL ORDER BY transactions.TransactionDate DESC");
		/*qryStr.append(" and customer.ProfileID=custprof.ProfileID ORDER BY transactions.TransactionDate DESC");*/

		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());
		qryResult1.setParameter("txnidDepo", EOTConstants.TXN_ID_PAY);
		qryResult1.setParameter("txnidwith", EOTConstants.TXN_ID_SALE);
		//	qryResult1.setParameter("txnidreve", EOTConstants.TXN_ID_TRFDIRECT);
		qryResult1.setParameter("status", EOTConstants.TXN_NO_ERROR);


		if(fisrtName != null && !"".equals(fisrtName)){
			qryResult1.setParameter("fisrtName", fisrtName);
		}
		if(lastName != null && !"".equals(lastName)){
			qryResult1.setParameter("lastName", lastName);
		}
		if((fisrtName != null && !"".equals(fisrtName))&& (lastName != null && !"".equals(lastName))){
			qryResult1.setParameter("fisrtName", fisrtName);
			qryResult1.setParameter("lastName", lastName);
		}
		if(mobileNumber != null && !"".equals(mobileNumber)){
			qryResult1.setParameter("mobileNumber", mobileNumber);
		}
		if(amount != null && !"".equals(amount)){
			qryResult1.setParameter("amount", amount);
		}
		if(bankId != null){
			qryResult1.setParameter("bankId", bankId);
		}
		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){
			qryResult1.setParameter("fromDate", fromDate.toUpperCase()+"%");
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){
			qryResult1.setParameter("toDate", toDate.toUpperCase()+"%");
		}
		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qryResult1.setParameter("fromDate", fromDate.toUpperCase());
			qryResult1.setParameter("toDate", toDate.toUpperCase());
		}

		if(txnType!=null && !"".equals(txnType)){
			if(txnType.equals("128")){
				qryResult1.setParameter("txnType1", EOTConstants.TXN_ID_PAY);
			}
			if(txnType.equals("90")){
				qryResult1.setParameter("txnType2", EOTConstants.TXN_ID_SALE);		

			}	
			/*if(txnType.equals("55")){
				qryResult1.setParameter("txnType3", EOTConstants.TXN_ID_TRFDIRECT);		

			}	*/
		}
		qryResult1.setParameter("notstatus",10);
		qryResult1.setParameter("bookID", 2);
		//qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//qryResult1.setMaxResults(appConfig.getResultsPerPage());

		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getExternalTransactions(java.lang.Integer)
	 */
	@Override
	public Page getExternalTransactions(Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ExternalTransaction.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(ExternalTransaction.class);
		//criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getOperatorList()
	 */
	@Override
	public List<Operator> getOperatorList() {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Operator.class);
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#searchExternalTxns(com.eot.banking.dto.ExternalTransactionDTO, int)
	 */
	@Override
	public Page searchExternalTxns(ExternalTransactionDTO externalTransactionDTO, int pageNumber) {	


		StringBuffer qryStr = null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer qry = new StringBuffer("SELECT COUNT(transactions.transactionId) FROM Transactions AS transactions INNER JOIN Operator AS ot ON transactions.ReferenceId=ot.OperatorID JOIN " +
				" Bank AS bank ON ot.BankID=bank.BankID JOIN Country AS country ON bank.CountryID=country.CountryID " +
				" JOIN ExternalTransaction AS et ON et.TransactionID=transactions.TransactionID ");

		if(externalTransactionDTO.getMobileNumber()!= null && ! "".equals(externalTransactionDTO.getMobileNumber()) ){
			qry.append(" and et.MobileNumber like '%"+ externalTransactionDTO.getMobileNumber()+"%'");
		}		
		if(externalTransactionDTO.getBeneficiaryMobileNumber()!= null && ! "".equals(externalTransactionDTO.getBeneficiaryMobileNumber()) ){
			qry.append(" and et.BeneficiaryMobileNumber like '%"+ externalTransactionDTO.getBeneficiaryMobileNumber()+"%'");
		}
		if(externalTransactionDTO.getExternalEntityId()!= null && ! "".equals(externalTransactionDTO.getExternalEntityId()) ){
			//externalTransactionDTO.setExternalEntityId(1L);
			qry.append(" and et.ExternalEntityID='"+externalTransactionDTO.getExternalEntityId()+"'");
		}

		if(externalTransactionDTO.getTransactionId()!=null){

			qry.append(" and transactions.TransactionID='"+externalTransactionDTO.getTransactionId()+"'");
		}

		if(externalTransactionDTO.getFromDate()!=null){
			String fromDate=DateUtil.formatDateAndTime(externalTransactionDTO.getFromDate());

			String toDate=DateUtil.formatDateAndTime(externalTransactionDTO.getToDate());
			qry.append(" where DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"'");
		}


		SQLQuery qryResult = session.createSQLQuery(qry.toString());

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		qryStr = new StringBuffer("SELECT transactions.status,transactions.TransactionID,et.EntityName,et.MobileNumber,et.BeneficiaryMobileNumber,et.ServiceChargeAmount,transactions.amount,transactions.TransactionType,transactions.TransactionDate,bank.BankName," +
				" country.Country,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=1) AS SC," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=54) AND " +
				" tj.TransactionID=transactions.TransactionID ) AS tax," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=55) AND " +
				" tj.TransactionID=transactions.TransactionID) AS bankShare," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=102) " +
				" AND tj.TransactionID=transactions.TransactionID) AS gimShare " +
				" FROM Transactions AS transactions INNER JOIN Operator AS ot ON transactions.ReferenceId=ot.OperatorID JOIN " +
				" Bank AS bank ON ot.BankID=bank.BankID JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN ExternalTransaction AS et ON et.TransactionID=transactions.TransactionID");

		if(externalTransactionDTO.getMobileNumber()!= null && ! "".equals(externalTransactionDTO.getMobileNumber()) ){

			qryStr.append(" and et.MobileNumber like '%"+ externalTransactionDTO.getMobileNumber()+"%'");
		}		
		if(externalTransactionDTO.getBeneficiaryMobileNumber()!= null && ! "".equals(externalTransactionDTO.getBeneficiaryMobileNumber()) ){
			qryStr.append(" and et.BeneficiaryMobileNumber like '%"+ externalTransactionDTO.getBeneficiaryMobileNumber()+"%'");
		}
		if(externalTransactionDTO.getExternalEntityId()!= null && ! "".equals(externalTransactionDTO.getExternalEntityId()) ){
			qryStr.append(" and et.ExternalEntityID='"+externalTransactionDTO.getExternalEntityId()+"'");
		}

		if(externalTransactionDTO.getTransactionId()!=null){
			qryStr.append(" and transactions.TransactionID='"+externalTransactionDTO.getTransactionId()+"'");
		}
		//Code commented for Bug no 208 External Transactions - Search through Operator Name 


		if(externalTransactionDTO.getFromDate()!=null){
			String fromDate=DateUtil.formatDate(externalTransactionDTO.getFromDate());
			String toDate=DateUtil.formatDate(externalTransactionDTO.getToDate());
			qryStr.append(" where DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"'");
		}
		qryStr.append(" ORDER BY transactions.TransactionDate DESC");
		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());

		//qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//qryResult1.setMaxResults(appConfig.getResultsPerPage());
		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getExternalTransactionDetailsForReport(com.eot.banking.dto.ExternalTransactionDTO)
	 */
	@Override
	public List getExternalTransactionDetailsForReport(ExternalTransactionDTO externalTransactionDTO) {

		String fromDate=DateUtil.formatDate(externalTransactionDTO.getFromDate());
		String toDate=DateUtil.formatDate(externalTransactionDTO.getToDate());


		StringBuffer  qryStr = new StringBuffer("SELECT transactions.status,transactions.TransactionID,et.EntityName,et.MobileNumber,et.BeneficiaryMobileNumber,transactions.amount,transactions.TransactionType,transactions.TransactionDate,bank.BankName," +
				" country.Country,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType=1) AS SC," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=54) AND " +
				" tj.TransactionID=transactions.TransactionID ) AS tax," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=55) AND " +
				" tj.TransactionID=transactions.TransactionID) AS bankShare," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=102) " +
				" AND tj.TransactionID=transactions.TransactionID) AS gimShare " +
				" FROM Transactions AS transactions INNER JOIN Operator AS ot ON transactions.ReferenceId=ot.OperatorID JOIN " +
				" Bank AS bank ON ot.BankID=bank.BankID JOIN Country AS country ON bank.CountryID=country.CountryID JOIN ExternalTransaction AS et ON et.TransactionID=transactions.TransactionID ");

		if(externalTransactionDTO.getMobileNumber()!= null && ! "".equals(externalTransactionDTO.getMobileNumber()) ){
			qryStr.append(" and et.MobileNumber like '%"+ externalTransactionDTO.getMobileNumber()+"%'");

		}	

		if(externalTransactionDTO.getBeneficiaryMobileNumber()!= null && ! "".equals(externalTransactionDTO.getBeneficiaryMobileNumber()) ){
			qryStr.append(" and et.BeneficiaryMobileNumber like '%"+ externalTransactionDTO.getBeneficiaryMobileNumber()+"%'");
		}

		/*if(externalTransactionDTO.getExternalEntityId()!= null && ! "".equals(externalTransactionDTO.getExternalEntityId()) ){
			qryStr.append(" and et.ExternalEntityID='"+externalTransactionDTO.getExternalEntityId()+"'");
		}
		code commented for the defect 110
		 */

		if(externalTransactionDTO.getTransactionId()!=null){
			qryStr.append(" and transactions.TransactionID='"+externalTransactionDTO.getTransactionId()+"'");
		}

		if(externalTransactionDTO.getFromDate()!=null){
			qryStr.append(" where DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"'");
		}

		qryStr.append(" and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' ORDER BY transactions.TransactionDate DESC"); 

		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());

		return qryResult.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.TransactionDao#getExternalTransactionSummaryDetailsForReport(com.eot.banking.dto.ExternalTransactionDTO)
	 */
	@Override
	public List getExternalTransactionSummaryDetailsForReport(ExternalTransactionDTO externalTransactionDTO) {

		String fromDate=DateUtil.formatDate(externalTransactionDTO.getFromDate());
		String toDate=DateUtil.formatDate(externalTransactionDTO.getToDate());

		StringBuffer  qryStr = new StringBuffer("SELECT et.EntityName,SUM(transactions.amount),COUNT(transactions.TransactionID),transactions.TransactionType,bank.BankName,country.CountryID," +
				" (SELECT SUM(amount) FROM TransactionJournals WHERE TransactionID IN " +
				" (SELECT transactions.TransactionID FROM Transactions AS transactions WHERE DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"' AND transactions.ReferenceId=ot.OperatorID) AND JournalType=1 ) AS SC, " +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=54) AND  TransactionID IN " +
				" (SELECT transactions.TransactionID FROM Transactions AS transactions where DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"' AND transactions.ReferenceId=ot.OperatorID)) AS tax," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=55) AND " +
				" TransactionID IN (SELECT transactions.TransactionID FROM Transactions AS transactions where DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"' AND transactions.ReferenceId=ot.OperatorID)) AS bankShare," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=102) AND TransactionID IN " +
				" (SELECT transactions.TransactionID FROM Transactions AS transactions where DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"' AND transactions.ReferenceId=ot.OperatorID)) AS gimShare " +
				" FROM Transactions AS transactions INNER JOIN Operator AS ot ON transactions.ReferenceId=ot.OperatorID JOIN " +
				" Bank AS bank ON ot.BankID=bank.BankID JOIN Country AS country ON bank.CountryID=country.CountryID" +
				" JOIN ExternalTransaction AS et ON et.TransactionID=transactions.TransactionID");


		if(externalTransactionDTO.getTransactionId()!=null){
			qryStr.append(" and transactions.TransactionID='"+externalTransactionDTO.getTransactionId()+"'");
		}
		if(externalTransactionDTO.getExternalEntityId()!= null && ! "".equals(externalTransactionDTO.getExternalEntityId()) ){
			qryStr.append(" and et.ExternalEntityID='"+externalTransactionDTO.getExternalEntityId()+"'");
		}
		if(externalTransactionDTO.getFromDate()!=null){
			qryStr.append(" AND DATE(transactions.TransactionDate)>='"+fromDate+"' and DATE(transactions.TransactionDate)<='"+toDate+"'"); 
		}

		qryStr.append(" and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"'  GROUP BY transactions.TransactionType , et.ExternalEntityID "); 
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());

		return qryResult.list();
	}

	@Override
	@Transactional
	public int updateCommissionProcessedTxnByAgnetId(Long agentID,String date,int status,long transactionId) {
		Query query =getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("update Transactions set commissionStatus=:status where transactiondate like :date  and referenceId=:agentID and transactionType in(:txn1,:txn2,:txn3,:txn4) and commissionStatus is null and transactionId=:transactionId ");
		query.setParameter("status", status);
		query.setParameter("date", date+"%");
		query.setParameter("agentID", agentID);
		query.setParameter("txn1", 115);
		query.setParameter("txn2", 116);
		query.setParameter("txn3", 90);
		query.setParameter("txn4", 126);
		query.setParameter("transactionId", transactionId);
		return query.executeUpdate();
	}
	@Override
	@Transactional
	public int updateCommissionProcessedTxnByTxnId(Long agentID,String date,int status,long transactionId) {
		Query query =getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("update Transactions set commissionStatus=:status where commissionStatus is null and transactionId=:transactionId ");
		query.setParameter("status", status);
		query.setParameter("transactionId", transactionId);
		return query.executeUpdate();
	}

	@Override
	@Transactional
	public int updateCommissionProcessedTxnByAgnetId(Long agentID,String date,int status) {
		Query query =getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("update Transactions set commissionStatus=:status where transactiondate like :date  and referenceId=:agentID and transactionType in(:txn1,:txn2,:txn3,:txn4) and commissionStatus is null");
		query.setParameter("status", status);
		query.setParameter("date", date+"%");
		query.setParameter("agentID", agentID);
		query.setParameter("txn1", 115);
		query.setParameter("txn2", 116);
		query.setParameter("txn3", 90);
		query.setParameter("txn4", 126);
		return query.executeUpdate();
	}

	@Override
	public TransactionType getTransactionType(int type) {
		List list = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(TransactionType.class)
				.add(Restrictions.eq("transactionType", type))
				.list();
		return CollectionUtils.isNotEmpty(list) ? (TransactionType)list.get(0) : null;
	}
	@Override
	public Page searchBusinessPartnerTxnSummary(Integer bankId,BusinessPartnerDTO businessPartnerDTO, int pageNumber, String appendString) {
		String date1 = null;
		String date2 = null;
		if(businessPartnerDTO.getFromDate()!= null && businessPartnerDTO.getToDate()!= null && businessPartnerDTO.getFromDate()!= "" && businessPartnerDTO.getToDate()!= ""){ 
			try {					
	
				Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(businessPartnerDTO.getFromDate());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				date1 = formatter.format(initDate);
				Date initDate1 = new SimpleDateFormat("dd/MM/yyyy").parse(businessPartnerDTO.getToDate());
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
				date2 = formatter1.format(initDate1); 			
	
			} catch (ParseException e) {
				e.printStackTrace();
			}}

	StringBuffer qryStr = null;
	Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		/*
		 * StringBuffer qry = new
		 * StringBuffer("SELECT COUNT(CommissionReport.TransactionID) FROM CommissionReport"
		 * );
		 * 
		 * if(businessPartnerDTO!=null){ if(businessPartnerDTO.getFromDate()!=null &&
		 * businessPartnerDTO.getFromDate()!="" ){ qry.
		 * append(" where DATE(CommissionReport.TransactionDate)>=:searchfromDate and DATE(CommissionReport.TransactionDate)<=:searchtoDate"
		 * ); } }
		 * 
		 * if(businessPartnerDTO.getContactNumber() != null &&
		 * !"".equals(businessPartnerDTO.getContactNumber())){
		 * 
		 * qry.append(" and CommissionReport.mobileNumber=:mobileNumber"); }
		 * 
		 * if(businessPartnerDTO.getTransactionId() != null &&
		 * !"".equals(businessPartnerDTO.getTransactionId())){
		 * 
		 * qry.append(" and CommissionReport.TransactionID=:TransactionID"); }
		 * 
		 * if(businessPartnerDTO.getName() != null &&
		 * !"".equals(businessPartnerDTO.getName())){
		 * 
		 * qry.append(" and CommissionReport.PartnerName=:PartnerName"); }
		 * 
		 * if(businessPartnerDTO.getTransactionType() != null &&
		 * !"".equals(businessPartnerDTO.getTransactionType())){
		 * 
		 * qry.append(" and CommissionReport.TransactionType=:TransactionType"); }
		 * 
		 * if(businessPartnerDTO.getRefTransactionId() != null &&
		 * !"".equals(businessPartnerDTO.getRefTransactionId())){
		 * 
		 * qry.append(" and CommissionReport.RefTransactionID=:RefTransactionID"); }
		 * if(businessPartnerDTO.getPartnerCode() != null &&
		 * !"".equals(businessPartnerDTO.getPartnerCode())){
		 * qry.append(" and CommissionReport.PartnerCode=:PartnerCode"); }
		 * 
		 * if(appendString != null && !"".equals(appendString)){
		 * 
		 * qry.append(" and "+appendString); } SQLQuery qryResult =
		 * session.createSQLQuery(qry.toString());
		 * if(businessPartnerDTO.getContactNumber() != null &&
		 * !"".equals(businessPartnerDTO.getContactNumber())){
		 * qryResult.setParameter("mobileNumber",
		 * businessPartnerDTO.getContactNumber()); } if(businessPartnerDTO.getName() !=
		 * null && !"".equals(businessPartnerDTO.getName())){
		 * qryResult.setParameter("PartnerName", businessPartnerDTO.getName()); }
		 * if(businessPartnerDTO.getTransactionId() != null &&
		 * !"".equals(businessPartnerDTO.getTransactionId())){
		 * qryResult.setParameter("TransactionID",
		 * businessPartnerDTO.getTransactionId()); }
		 * 
		 * if(businessPartnerDTO.getTransactionType() != null &&
		 * !"".equals(businessPartnerDTO.getTransactionType())){
		 * qryResult.setParameter("TransactionType",
		 * businessPartnerDTO.getTransactionType()); }
		 * if(businessPartnerDTO.getRefTransactionId() != null &&
		 * !"".equals(businessPartnerDTO.getRefTransactionId())){
		 * qryResult.setParameter("RefTransactionID",
		 * businessPartnerDTO.getRefTransactionId()); }
		 * if(businessPartnerDTO.getPartnerCode() != null &&
		 * !"".equals(businessPartnerDTO.getPartnerCode())){
		 * qryResult.setParameter("PartnerCode", businessPartnerDTO.getPartnerCode()); }
		 * 
		 * if(businessPartnerDTO!=null){ if(businessPartnerDTO.getFromDate()!=null &&
		 * !"".equals(businessPartnerDTO.getFromDate())){
		 * qryResult.setParameter("searchfromDate",date1); }
		 * if(businessPartnerDTO.getToDate()!=null &&
		 * !"".equals(businessPartnerDTO.getToDate())){
		 * qryResult.setParameter("searchtoDate",date2); } }
		 * System.out.println(qryResult.toString()); int totalCount =
		 * Integer.parseInt(qryResult.list().get(0).toString());
		 */

	/*qryStr = new StringBuffer("SELECT transactions.TransactionID,transactions.transactionType,trxjr.amount,bp.name,cust.mobileNumber AS mobileNumber,trxjr.JournalTime AS transactionTime FROM  Transactions AS transactions " + 
			"INNER JOIN BusinessPartner AS bp ON SUBSTRING(transactions.ReferenceId FROM 4 FOR (SELECT LOCATE('_',transactions.ReferenceId,4))-4)=bp.Id " + 
			"INNER JOIN Bank AS bnk ON bnk.bankId =bp.bank " + 
			"INNER JOIN TransactionJournals AS trxjr ON transactions.TransactionID=trxjr.TransactionID " + 
			"INNER JOIN Customer AS cust ON cust.customerId=SUBSTRING(transactions.ReferenceId FROM (SELECT LOCATE('_',transactions.ReferenceId,4)+1)) ");*/

//	qryStr = new StringBuffer("SELECT CommissionReport.TransactionID AS TransactionID, CommissionReport.CommissionAmount AS CommissionAmount," 
//			+" CommissionReport.PartnerCode AS PartnerCode, CommissionReport.PartnerName AS PartnerName, CommissionReport.RefTransactionID" 
//			+" AS RefTransactionID, CommissionReport.ServiceCharge AS ServiceCharge, CommissionReport.Status AS Status," 
//			+" CommissionReport.TransactionDate AS TransactionDate, CommissionReport.TransactionType AS TransactionType FROM CommissionReport");

	qryStr = new StringBuffer("SELECT cr.TransactionID AS TransactionID, cr.CommissionAmount AS CommissionAmount, \r\n" + 
			"cr.PartnerCode AS PartnerCode, cr.PartnerName AS PartnerName, cr.RefTransactionID\r\n" + 
			"AS RefTransactionID, cr.ServiceCharge AS ServiceCharge, cr.Status AS STATUS,\r\n" + 
			"cr.TransactionDate AS TransactionDate, cr.TransactionType AS TransactionType FROM CommissionReport\r\n" + 
			" AS cr "); 
	if(StringUtils.isNotBlank(appendString)) {
		qryStr.append(appendString);
	}
			
	if(businessPartnerDTO!=null){
		if(businessPartnerDTO.getFromDate()!=null && !"".equals(businessPartnerDTO.getFromDate())){
			if(StringUtils.isNotBlank(appendString)) {
				qryStr.append(" and");
			}else {
				qryStr.append(" where");
			}
			qryStr.append(" DATE(cr.TransactionDate)>=:fromDate AND DATE(cr.TransactionDate)<=:toDate ");
		}
	}

	if(businessPartnerDTO.getContactNumber() != null && !"".equals(businessPartnerDTO.getContactNumber())){	

		qryStr.append(" and cr.MobileNumber=:mobileNumber");
	}	

	if(businessPartnerDTO.getTransactionId() != null && !"".equals(businessPartnerDTO.getTransactionId())){	

		qryStr.append(" and cr.TransactionID=:TransactionID");
	}	

	if(businessPartnerDTO.getName() != null && !"".equals(businessPartnerDTO.getName())){	

		qryStr.append(" and cr.PartnerName=:PartnerName");
	}
	if(businessPartnerDTO.getTransactionType() != null && !"".equals(businessPartnerDTO.getTransactionType()+"")){	

		qryStr.append(" and cr.TransactionType=:TransactionType");
	}
	if(businessPartnerDTO.getRefTransactionId() != null && !"".equals(businessPartnerDTO.getRefTransactionId())){	

		qryStr.append(" and cr.RefTransactionID=:RefTransactionID");
	}
	if(businessPartnerDTO.getPartnerCode() != null && !"".equals(businessPartnerDTO.getPartnerCode())){	
		qryStr.append(" and cr.PartnerCode=:PartnerCode");
	}
	/*	if(bankId != null){

		qryStr.append(" and bp.Bank=:bankId");   
	} */

	/*qryStr.append(" and customer.ProfileID=custprof.ProfileID and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"'"); */
	//qryStr.append("  and transactions.ReferenceId LIKE 'BP_%'");
//	if(appendString != null && !"".equals(appendString)){	
//
//		qryStr.append(" and "+appendString);
//	}
	qryStr.append("  ORDER BY TransactionDate DESC");

	SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());
	if(businessPartnerDTO.getContactNumber() != null && !"".equals(businessPartnerDTO.getContactNumber())){
		qryResult1.setParameter("mobileNumber", businessPartnerDTO.getContactNumber());
	}
	if(businessPartnerDTO.getName() != null && !"".equals(businessPartnerDTO.getName())){
		qryResult1.setParameter("PartnerName", businessPartnerDTO.getName());
	}

	if(businessPartnerDTO.getTransactionId() != null && !"".equals(businessPartnerDTO.getTransactionId())){	
		qryResult1.setParameter("TransactionID", businessPartnerDTO.getTransactionId());
	}	

	if(businessPartnerDTO.getTransactionType() != null && !"".equals(businessPartnerDTO.getTransactionType())){	
		qryResult1.setParameter("TransactionType", businessPartnerDTO.getTransactionType());
	}
	if(businessPartnerDTO.getRefTransactionId() != null && !"".equals(businessPartnerDTO.getRefTransactionId())){	
		qryResult1.setParameter("RefTransactionID", businessPartnerDTO.getRefTransactionId());
	}
	if(businessPartnerDTO.getPartnerCode() != null && !"".equals(businessPartnerDTO.getPartnerCode())){	
		qryResult1.setParameter("PartnerCode", businessPartnerDTO.getPartnerCode());
	}

	/*if(bankId != null){
		qryResult1.setParameter("bankId", bankId);
	}*/


	if(businessPartnerDTO!=null){ 				
		if(businessPartnerDTO.getFromDate()!=null && !"".equals(businessPartnerDTO.getFromDate())){
			qryResult1.setParameter("fromDate", date1);
		}
		if(businessPartnerDTO.getToDate()!=null && !"".equals(businessPartnerDTO.getToDate())){
			qryResult1.setParameter("toDate", date2);
		}
	}
	//qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
	//qryResult1.setMaxResults(appConfig.getResultsPerPage());
	qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	return PaginationHelper.getPage(qryResult1.list(), qryResult1.list().size(), appConfig.getResultsPerPage(), pageNumber);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SettlementJournal> getSettlementJournals(Long transactionId) {

		List<SettlementJournal> settlementJournals = getHibernateTemplate().find("from SettlementJournal sj where sj.transaction.transactionId=?",transactionId);

		return settlementJournals.size()>0 ? settlementJournals : null;
	}

	@Override
	public void updateSettlementJournals(long transactionId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query query = session.createQuery("update SettlementJournal as sj set sj.status=:status where sj.transaction.transactionId=:transactionId");
		query.setParameter("status", EOTConstants.TXN_INITIATE_ADJUSTMENT);
		query.setParameter("transactionId", transactionId);

		query.executeUpdate();

	}

	@Override
	public void updateTransactionJournals(long transactionId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query query = session.createQuery("update TransactionJournal as tj set tj.active=:active where tj.transaction.transactionId=:transactionId");
		query.setParameter("active", EOTConstants.TXN_INITIATE_ADJUSTMENT);
		query.setParameter("transactionId", transactionId);

		query.executeUpdate();

	}

	@Override
	public void updateTransaction(long transactionId) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query query = session.createQuery("update Transaction set status=:status where transactionId=:transactionId");
		query.setParameter("status", EOTConstants.TXN_INITIATE_ADJUSTMENT);
		query.setParameter("transactionId", transactionId);

		query.executeUpdate();

	}

	@Override
	public Account getBusinessPartnerAccount(String accountNumber) {
		return getHibernateTemplate().get(Account.class,accountNumber);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FileUploadDetail getLastProcessedFile() {
		SQLQuery qryResult = getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery("SELECT * FROM file_upload_detail ORDER BY `file_upload_detail_id` DESC LIMIT 1");
		qryResult.addScalar("file_upload_detail_id", Hibernate.INTEGER).addScalar("file_name", Hibernate.STRING)
		.addScalar("created_date", Hibernate.DATE).addScalar("status", Hibernate.LONG)
		.addScalar("user_id", Hibernate.STRING).addScalar("fail_count", Hibernate.INTEGER)
		.addScalar("success_count", Hibernate.INTEGER).addScalar("total_count", Hibernate.INTEGER)
		.addScalar("failed_amount", Hibernate.DOUBLE).addScalar("success_amount", Hibernate.DOUBLE);
		List<Object[]> lastProcessedFile = qryResult.list();
		Object[] object = lastProcessedFile.get(0);
		FileUploadDetail fileUploadFile = new FileUploadDetail();
		fileUploadFile.setFileUploadDetailID((Integer) object[0]);
		fileUploadFile.setFileName((String) object[1]);
		fileUploadFile.setCreatedDate((Date) object[2]);
		fileUploadFile.setStatus((Long) object[3]);
		fileUploadFile.setUserId((String) object[4]);
		fileUploadFile.setFailCount((Integer) object[5]);
		fileUploadFile.setSuccessCount((Integer) object[6]);
		fileUploadFile.setTotalCount((Integer) object[7]);
		fileUploadFile.setFailedAmount((Double) object[8]);
		fileUploadFile.setSuccessAmount((Double) object[9]);
		return fileUploadFile;
	}

	@Override
	public Map<Integer, TransactionType> getTxntypeMap(String locale) {
		List<TransactionType> transactionTypeList = getTrnsactionList(locale);

		Map<Integer,TransactionType> txTypeMap=new HashMap<>();
		for (TransactionType transactionType : transactionTypeList) {
			txTypeMap.put(transactionType.getTransactionType(), transactionType);
		}
		return txTypeMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Account getAccountByAccountNumber(String accountNumber) {

		List<Account> accountList = getHibernateTemplate().find("from Account acc where acc.accountNumber=?",accountNumber);

		return accountList.size()>0 ? accountList.get(0) : null;
	}

	@Override
	public Page searchTxnMerchantData(Integer bankGroupId, Integer bankId, TxnSummaryDTO txnSummaryDTO, int pageNumber,
			Long branchId) {
		StringBuffer qryStr = null;

		/*
		 * StringBuffer agentCell= new
		 * StringBuffer(" AND ( SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135  )  ) AND cust.MobileNumber=:agentCell) "
		 * ); StringBuffer agentCode= new
		 * StringBuffer(" AND ( SELECT cust.AgentCode FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID  AND ( transactions.TransactionType = 135  ) ) AND cust.AgentCode=:agentCode)"
		 * ); StringBuffer superAgentCode= new
		 * StringBuffer(" AND ( SELECT bp.Code FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu  WHERE cu.CustomerID=customer.CustomerID )  AND bp.Code=:superAgentCode  )"
		 * ); StringBuffer superAgentName= new
		 * StringBuffer(" HAVING SuperAgentName LIKE :superAgentName"); StringBuffer
		 * agentName= new StringBuffer(" HAVING AgentName LIKE :agentName");


		if((txnSummaryDTO.getAgentName() != null && !"".equals(txnSummaryDTO.getAgentName())) &&
				(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName()))){

			superAgentName = new StringBuffer(" AND ( SELECT bp.Name FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu WHERE cu.CustomerID = customer.CustomerID ) ) LIKE :superAgentName");

		}
		 */

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer qry = new StringBuffer("SELECT Count(txn.transactionid)" +
				"FROM Transactions txn \r\n" + 
				"JOIN TransactionTypes tt ON tt.TransactionType = txn.TransactionType\r\n" + 
				"INNER JOIN Customer mechant ON txn.referenceId = mechant.customerId\r\n" + 
				"JOIN CustomerAccounts AS ca ON \r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType = 128 THEN ca.AccountNumber = txn.OtherAccount\r\n" + 
				"WHEN txn.TransactionType = 90 THEN ca.AccountNumber = txn.customerAccount\r\n" + 
				"END\r\n" + 
				"JOIN Customer customer ON customer.CustomerID = ca.CustomerID where");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qry.append(" (customer.MobileNumber=:MobileNumber OR mechant.MobileNumber=:MobileNumber) and");
			//	qry.append(" (customer.MobileNumber=:MobileNumber) and");
		}

		//		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
		//
		//			qry.append(" JOIN WebRequests as webrequests ON txn.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		//		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qry.append(" customer.ProfileID =:ProfileID and");  
		} 

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qry.append(" (txn.TransactionType =:Txn1 or txn.TransactionType =:Txn2)");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qry.append(" txn.TransactionType=:Txn");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qry.append(" and DATE(txn.TransactionDate)>=:fromDate and DATE(txn.TransactionDate)<=:toDate");
			}
		}

		if(txnSummaryDTO.getStatus() != null && txnSummaryDTO.getStatus().equals("2"))
			qry.append(" and txn.Status!=:status");   
		else 
			qry.append(" and txn.Status=:status"); 

		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){	

			qry.append(" and customer.partnerId=:partnerID");
		}

		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){

			qry.append(" and (customer.AgentCode=:agentCode or mechant.AgentCode=:agentCode)");  
		}
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
			qry.append(" and txn.requestChannel=:requestChannel"); 
		}

		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qry.append(" and txn.TransactionID=:txnId"); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qry.append(" AND CONCAT(customer.FirstName, \" \", customer.LastName) LIKE :customerName");
		}
		SQLQuery qryResult = session.createSQLQuery(qry.toString());

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
			qryResult.setParameter("MobileNumber",txnSummaryDTO.getMobileNumber()); 
		}

		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult.setParameter("userID",txnSummaryDTO.getUserId()+"%");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){
			qryResult.setParameter("ProfileID",txnSummaryDTO.getProfileId());
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult.setParameter("Txn1", 90);
				qryResult.setParameter("Txn2", 128);
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("Txn", txnSummaryDTO.getTransactionType());	
			}
		}
		qryResult.setParameter("status", EOTConstants.TXN_NO_ERROR);
		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){
			qryResult.setParameter("partnerID", txnSummaryDTO.getPartnerId());
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult.setParameter("requestChannel", "WEB");
			break;
			case "2": qryResult.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult.setParameter("requestChannel", "USSD");
			break;
			}						
		}

		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}

		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryResult.setParameter("txnId",txnSummaryDTO.getTxnId()); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryResult.setParameter("customerName", "%"+txnSummaryDTO.getCustomerName()+"%"); 
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		/*
		 * qryStr = new
		 * StringBuffer("SELECT distinct(transactions.TransactionID), customer.FirstName,customer.MobileNumber,customer.AgentCode,customer.partnerId, transactions.amount,transactions.TransactionType,transactions.TransactionDate,transactions.status,transactions.requestChannel,bank.BankName,branch.Location,country.CountryID,"
		 * +
		 * "customer.ProfileID,customer.ProfileID,customer.LastName,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID"
		 * +
		 * " AND JournalType=:journalType) AS SC,(SELECT SUM(sj.amount) FROM SettlementJournals sj WHERE sj.TransactionID=transactions.TransactionID AND sj.BookID=:bookID2) AS bankShare,"
		 * +
		 * "(SELECT SUM(sj.amount) FROM SettlementJournals sj WHERE sj.TransactionID=transactions.TransactionID AND sj.BookID=:bookID3) AS gimShare, "
		 * +
		 * 
		 * "( SELECT CONCAT(cust.FirstName, \" \", cust.LastName) FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS AgentName,(SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN (SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS AgentMobileNumber, ( SELECT cust.AgentCode FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS Agent_Code, ( SELECT bp.Code FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu WHERE cu.CustomerID=customer.CustomerID ) ) AS SuperAgentCode, ( SELECT bp.Name FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu WHERE cu.CustomerID=customer.CustomerID ) ) AS SuperAgentName FROM"
		 * +
		 * 
		 * " Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc"
		 * +
		 * " ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID"
		 * +
		 * " JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID "
		 * );
		 */
		qryStr = new StringBuffer("SELECT txn.transactionid,\r\n" + 
				"CASE \r\n" + 
				"	WHEN txn.TransactionType = 90 THEN mechant.agentCode\r\n" + 
				"	ELSE customer.agentCode\r\n" + 
				"END AS MerchantCode,\r\n" + 
				"CASE \r\n" + 
				"	WHEN txn.TransactionType = 90 THEN mechant.firstname \r\n" + 
				"	ELSE  customer.firstname \r\n" + 
				"END AS MerchantName,\r\n" + 
				"CASE \r\n" + 
				"	WHEN txn.TransactionType = 90 THEN mechant.BusinessName \r\n" + 
				"	ELSE  customer.BusinessName \r\n" + 
				"END AS BusinessName,\r\n" + 
				"CASE \r\n" + 	
				"	WHEN txn.TransactionType = 90 THEN mechant.mobileNumber \r\n" + 
				"	ELSE  customer.mobileNumber \r\n" + 
				"	END AS MerchantMobile,\r\n" + 
				"CASE \r\n" + 
				"	WHEN txn.TransactionType = 90 THEN customer.firstname \r\n" + 
				"	ELSE  mechant.firstname \r\n" + 
				"END AS customerName,\r\n" + 
				"CASE \r\n" + 
				"	WHEN txn.TransactionType = 90 THEN customer.mobileNumber \r\n" + 
				"	ELSE  mechant.mobileNumber \r\n" + 
				"END AS customerMobile,\r\n" + 
				"txn.transactiondate AS TransactionDate,tt.Description,txn.status,\r\n" + 
				"txn.amount AS Amount,(SELECT amount FROM TransactionJournals WHERE TransactionID=txn.TransactionID AND JournalType=1) AS ServiceCharges,\r\n" + 
				"txn.requestChannel\r\n" + 
				"FROM Transactions txn \r\n" + 
				"JOIN TransactionTypes tt ON tt.TransactionType = txn.TransactionType\r\n" + 
				"INNER JOIN Customer mechant ON txn.referenceId = mechant.customerId\r\n" + 
				"JOIN CustomerAccounts AS ca ON \r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType = 128 THEN ca.AccountNumber = txn.OtherAccount\r\n" + 
				"WHEN txn.TransactionType = 90 THEN ca.AccountNumber = txn.customerAccount\r\n" + 
				"END\r\n" + 
				"JOIN Customer customer ON customer.CustomerID = ca.CustomerID where");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" (customer.MobileNumber=:MobileNumber OR mechant.MobileNumber=:MobileNumber) and");
			//	qry.append(" (customer.MobileNumber=:MobileNumber) and");
		}	

		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId().toString())){

			qryStr.append(" country.CountryID=:countryID and");
		}

		//		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
		//
		//			qryStr.append(" JOIN WebRequests as webrequests ON txn.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		//		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qryStr.append(" customer.ProfileID =:ProfileID and");  
		} 

		/*Author name <vinod joshi>, Date<6/22/2018>, purpose of change <Date format is not working > ,*/
		/*Start*/
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryStr.append(" (txn.TransactionType =:Txn1 or txn.TransactionType =:Txn2)");
			}
		}
		/*End*/

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" txn.TransactionType=:Txn");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" and DATE(txn.TransactionDate)>=:fromDate and DATE(txn.TransactionDate)<=:toDate ");
			}
		}

		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){	

			qryStr.append(" and customer.partnerId=:partnerID");
		}	

		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){

			qryStr.append(" and (customer.AgentCode=:agentCode or mechant.AgentCode=:agentCode)");   
		}

		if(txnSummaryDTO.getStatus() != null && txnSummaryDTO.getStatus().equals("2"))
			qryStr.append(" and txn.Status!=:status");   
		else 
			qryStr.append(" and txn.Status=:status"); 	
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
			qryStr.append(" and txn.requestChannel=:requestChannel"); 
		}

		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryStr.append(" and txn.TransactionID=:txnId"); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryStr.append(" AND CONCAT(customer.FirstName, \" \", customer.LastName) LIKE :customerName");
		}

		if (null!= txnSummaryDTO.getSortBy() && txnSummaryDTO.getSortBy().equals("asc")) {
			qryStr.append("  ORDER BY ")
			.append(txnSummaryDTO.getSortColumn())
			.append(" ASC");
		} else {
			qryStr.append("  ORDER BY ")
			.append(txnSummaryDTO.getSortColumn())
			.append(" DESC");
		}
		//qryStr.append("  ORDER BY TransactionDate DESC");

		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString()); 
		//		    qryResult1.setParameter("journalType", 1);
		//		    qryResult1.setParameter("bookID2", 2);
		//		    qryResult1.setParameter("bookID3", 3);
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){{
			qryResult1.setParameter("MobileNumber", txnSummaryDTO.getMobileNumber());
		}
		}
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId().toString())){
			qryResult1.setParameter("countryID", txnSummaryDTO.getCountryId());	
		}

		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult1.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult1.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult1.setParameter("userID",txnSummaryDTO.getUserId()+"%");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){
			qryResult1.setParameter("ProfileID",txnSummaryDTO.getProfileId());
		}
		//		if(bankGroupId != null){
		//				qryResult1.setParameter("bankGroupID", bankGroupId);
		//		}
		//		if(bankId != null){
		//			qryResult1.setParameter("bankID", bankId);
		//		}
		//		if(branchId != null){
		//			qryResult1.setParameter("branchID", branchId);
		//		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult1.setParameter("Txn1", 90);
				qryResult1.setParameter("Txn2", 128);
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult1.setParameter("Txn", txnSummaryDTO.getTransactionType());
			}
		}
		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){
			qryResult1.setParameter("partnerID", txnSummaryDTO.getPartnerId());
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult1.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		qryResult1.setParameter("status", EOTConstants.TXN_NO_ERROR);
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult1.setParameter("requestChannel", "Web");
			break;
			case "2": qryResult1.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult1.setParameter("requestChannel", "USSD");
			break;
			}						
		}	

		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryResult1.setParameter("txnId",txnSummaryDTO.getTxnId()); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryResult1.setParameter("customerName", "%"+txnSummaryDTO.getCustomerName()+"%"); 
		}

		if (!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType())) {
			qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
			qryResult1.setMaxResults(appConfig.getResultsPerPage());
		}

		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public Page searchTxnSummaryCustomerRegistration(Integer bankGroupId, Integer bankId, TxnSummaryDTO txnSummaryDTO,
			int pageNumber, Long branchId) {
		StringBuffer qryStr = null;

		StringBuffer agentCell= new StringBuffer(" AND ( SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135  )  ) AND cust.MobileNumber=:agentCell) ");
		StringBuffer agentCode= new StringBuffer(" AND ( SELECT cust.AgentCode FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID  AND ( transactions.TransactionType = 135  ) ) AND cust.AgentCode=:agentCode)");
		StringBuffer superAgentCode= new StringBuffer(" AND ( SELECT bp.Code FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu  WHERE cu.CustomerID=customer.CustomerID )  AND bp.Code=:superAgentCode  )");
		StringBuffer superAgentName= new StringBuffer(" HAVING SuperAgentName LIKE :superAgentName");
		StringBuffer agentName= new StringBuffer(" HAVING AgentName LIKE :agentName");

		if((txnSummaryDTO.getAgentName() != null && !"".equals(txnSummaryDTO.getAgentName())) &&
				(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName()))){

			superAgentName = new StringBuffer(" AND ( SELECT bp.Name FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu WHERE cu.CustomerID = customer.CustomerID ) ) LIKE :superAgentName");

		}


		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer qry = new StringBuffer("SELECT COUNT(distinct(transactions.TransactionID)) FROM  Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID " +
				" JOIN  BusinessPartner AS bp ON bp.Id=customer.PartnerId");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qry.append(" and customer.MobileNumber=:MobileNumber");
		}
		if(null!=txnSummaryDTO.getTransactionType() && txnSummaryDTO.getTransactionType().intValue()==120)//120 is for commission
			qry.append(" join Account acc on acc.accountNumber= custacc.accountNumber and (acc.aliasType=1 or acc.aliasType=10 ) ");
		else
			qry.append(" join Account acc on acc.accountNumber= custacc.accountNumber and acc.aliasType=1");

		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId().toString())){

			qry.append(" and country.CountryID=:CountryID");
		}
		else {
			qry.append(" and country.CountryID=:CountryDF");
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){

			qry.append(" and bank.BankID=:BankID");   
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){

			qry.append(" and branch.BranchID=:BranchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qry.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qry.append(" and bankgroups.BankGroupID=:BankGroupID");  
		} 
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){

			qry.append(" JOIN WebRequests as webrequests ON transactions.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qry.append(" and customer.ProfileID =:ProfileID");  
		} 
		if(bankGroupId != null){
			qry.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qry.append(" and bankgroups.BankGroupID=:BankGroupID"); 
		} 
		if(bankId != null){

			qry.append(" and bank.BankID=:BankID");   
		} 

		if(branchId != null){

			qry.append(" and branch.BranchID=:BranchID");   
		} 

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qry.append(" and transactions.TransactionType !=:Txn1 and transactions.TransactionType !=:Txn2 and transactions.TransactionType !=:Txn3 and transactions.TransactionType !=:Txn4 and transactions.TransactionType !=:Txn5");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qry.append(" and transactions.TransactionType=:Txn");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qry.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate");
			}
		}

		if(txnSummaryDTO!=null){
			if(StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber()) && StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber())){
				qry.append(" AND transactions.CustomerAccount=:accNumber");
			}
		}

		qry.append(" and customer.ProfileID=custprof.ProfileID");

		if(txnSummaryDTO.getStatus() != null && txnSummaryDTO.getStatus().equals("2"))
			qry.append(" and transactions.Status!=:status");   
		else 
			qry.append(" and transactions.Status=:status"); 

		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){	

			qry.append(" and customer.partnerId=:partnerID");
		}

		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){

			qry.append(" and customer.AgentCode=:agentCode");   
		}
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
			qry.append(" and transactions.requestChannel=:requestChannel"); 
		}
		if(txnSummaryDTO.getAgentMobileNumber() != null && !"".equals(txnSummaryDTO.getAgentMobileNumber())){
			qry.append(agentCell); 
		}
		if(txnSummaryDTO.getMerchantCode() != null && !"".equals(txnSummaryDTO.getMerchantCode())){
			qry.append(agentCode); 
		}
		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			qry.append(superAgentCode); 
		}
		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qry.append(" and transactions.TransactionID=:txnId"); 
		}

		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qry.append(" and  bp.Name LIKE :countSuperAgentName"); 
		}

		if(txnSummaryDTO.getAgentName() != null && !"".equals(txnSummaryDTO.getAgentName())){
			qry.append(" AND ( SELECT CONCAT(cust.FirstName, \" \", cust.LastName) FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) LIKE :countAgentName"); 

		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qry.append(" AND CONCAT(customer.FirstName, \" \", customer.LastName) LIKE :customerName");
		}
		SQLQuery qryResult = session.createSQLQuery(qry.toString());

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){
			qryResult.setParameter("MobileNumber",txnSummaryDTO.getMobileNumber()); 
		}
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId().toString())){
			qryResult.setParameter("CountryID", txnSummaryDTO.getCountryId());
		}else{ 
			qryResult.setParameter("CountryDF", EOTConstants.DEFAULT_COUNTRY);
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){
			qryResult.setParameter("BankID",txnSummaryDTO.getBankId());
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){
			qryResult.setParameter("BranchID",txnSummaryDTO.getBranchId());
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qryResult.setParameter("BankGroupID", txnSummaryDTO.getBankGroupId());
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult.setParameter("userID",txnSummaryDTO.getUserId()+"%");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){
			qryResult.setParameter("ProfileID",txnSummaryDTO.getProfileId());
		}
		if(bankGroupId != null){
			qryResult.setParameter("BankGroupID",bankGroupId);
		}
		if(bankId != null){
			qryResult.setParameter("BankID", bankId);
		}
		if(branchId != null){
			qryResult.setParameter("BranchID", branchId);
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult.setParameter("Txn1", 60);
				qryResult.setParameter("Txn2", 84);
				qryResult.setParameter("Txn3", 137);
				qryResult.setParameter("Txn4", 138);
				qryResult.setParameter("Txn5", 31);
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult.setParameter("Txn", txnSummaryDTO.getTransactionType());	
			}
		}
		qryResult.setParameter("status", EOTConstants.TXN_NO_ERROR);
		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){
			qryResult.setParameter("partnerID", txnSummaryDTO.getPartnerId());
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult.setParameter("requestChannel", "WEB");
			break;
			case "2": qryResult.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult.setParameter("requestChannel", "USSD");
			break;
			}						
		}

		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}
		if(txnSummaryDTO!=null){ 
			if(StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber()) && StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber())){
				qryResult.setParameter("accNumber", txnSummaryDTO.getAccountNumber());
			}
		}

		if(txnSummaryDTO.getAgentMobileNumber() != null && !"".equals(txnSummaryDTO.getAgentMobileNumber())){
			qryResult.setParameter("agentCell", txnSummaryDTO.getAgentMobileNumber());
		}
		if(txnSummaryDTO.getMerchantCode() != null && !"".equals(txnSummaryDTO.getMerchantCode())){

			qryResult.setParameter("agentCode", txnSummaryDTO.getMerchantCode());
		}
		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			qryResult.setParameter("superAgentCode", txnSummaryDTO.getSuperAgentCode());
		}
		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryResult.setParameter("txnId",txnSummaryDTO.getTxnId()); 
		}
		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qryResult.setParameter("countSuperAgentName",  "%"+txnSummaryDTO.getSuperAgentName()+"%"); 
		}

		if(txnSummaryDTO.getAgentName() != null && !"".equals(txnSummaryDTO.getAgentName())){
			qryResult.setParameter("countAgentName", "%"+txnSummaryDTO.getAgentName()+"%"); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryResult.setParameter("customerName", "%"+txnSummaryDTO.getCustomerName()+"%"); 
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		qryStr = new StringBuffer("SELECT distinct(transactions.TransactionID), customer.FirstName,customer.MobileNumber,customer.AgentCode,customer.partnerId,customer.partnerId, transactions.amount,transactions.TransactionType,transactions.TransactionDate,transactions.status,transactions.requestChannel,bank.BankName,branch.Location,country.CountryID," +
				"customer.ProfileID,customer.ProfileID,customer.LastName,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID" +
				" AND JournalType=:journalType) AS SC,(SELECT SUM(sj.amount) FROM SettlementJournals sj WHERE sj.TransactionID=transactions.TransactionID AND sj.BookID=:bookID2) AS bankShare," +
				"(SELECT SUM(sj.amount) FROM SettlementJournals sj WHERE sj.TransactionID=transactions.TransactionID AND sj.BookID=:bookID3) AS gimShare, " +

				"( SELECT CONCAT(cust.FirstName, \" \", cust.LastName) FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS AgentName,(SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN (SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS AgentMobileNumber,(SELECT cust.BusinessName FROM Customer cust WHERE cust.CustomerID IN (SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS BusinessName, ( SELECT cust.AgentCode FROM Customer cust WHERE cust.CustomerID IN ( SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.CustomerAccount AND tj.CreditAccount = transactions.CustomerAccount AND transactions.TransactionID = tj.TransactionID AND ( transactions.TransactionType = 135 ) ) ) AS Agent_Code, ( SELECT bp.Code FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu WHERE cu.CustomerID=customer.CustomerID ) ) AS SuperAgentCode, ( SELECT bp.Name FROM BusinessPartner bp WHERE bp.Id IN ( SELECT cu.PartnerId FROM Customer cu WHERE cu.CustomerID=customer.CustomerID ) ) AS SuperAgentName FROM" +

				" Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID  JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID ");

		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}	
		if(null!=txnSummaryDTO.getTransactionType() && txnSummaryDTO.getTransactionType().intValue()==120)//120 is for commission
			qryStr.append(" join Account acc on acc.accountNumber= custacc.accountNumber and (acc.aliasType=1 or acc.aliasType=10 ) ");
		else
			qryStr.append(" join Account acc on acc.accountNumber= custacc.accountNumber and acc.aliasType=1");

		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId().toString())){

			qryStr.append(" and country.CountryID=:countryID");
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId().toString())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupID");  
		} 
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){

			qryStr.append(" JOIN WebRequests as webrequests ON transactions.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qryStr.append(" and customer.ProfileID =:ProfileID");  
		} 
		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupID"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankID");   
		} 
		if(branchId != null){

			qryStr.append(" and branch.BranchID=:branchID");   
		} 

		/*if(txnSummaryDTO.getBusinessName() != null && !"".equals(txnSummaryDTO.getBusinessName())){	

			qryStr.append(" and customer.BusinessName=:businessName");
		}*/

		/*Author name <vinod joshi>, Date<6/22/2018>, purpose of change <Date format is not working > ,*/
		/*Start*/
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryStr.append(" and transactions.TransactionType !=:Txn1 and transactions.TransactionType !=:Txn2 and transactions.TransactionType !=:Txn3 and transactions.TransactionType !=:Txn4 and transactions.TransactionType !=:Txn5");
			}
		}
		/*End*/

		/*if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qry.append(" and transactions.TransactionType != 31 ");
			}
		}*/

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryStr.append(" and transactions.TransactionType=:Txn");
			}
		}

		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getFromDate()!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate ");
			}
		}

		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){	

			qryStr.append(" and customer.partnerId=:partnerID");
		}	

		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){

			qryStr.append(" and customer.AgentCode=:agentCode");   
		}

		qryStr.append(" and customer.ProfileID=custprof.ProfileID"); 

		if(txnSummaryDTO.getStatus() != null && txnSummaryDTO.getStatus().equals("2"))
			qryStr.append(" and transactions.Status!=:status");   
		else 
			qryStr.append(" and transactions.Status=:status"); 	
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
			qryStr.append(" and transactions.requestChannel=:requestChannel"); 
		}
		if(txnSummaryDTO!=null){
			if(StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber()) && StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber())){
				qryStr.append(" AND transactions.CustomerAccount=:accNumber");
			}
		}

		if(txnSummaryDTO.getAgentMobileNumber() != null && !"".equals(txnSummaryDTO.getAgentMobileNumber())){
			qryStr.append(agentCell); 
		}
		if(txnSummaryDTO.getMerchantCode() != null && !"".equals(txnSummaryDTO.getMerchantCode())){
			qryStr.append(agentCode); 
		}
		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			qryStr.append(superAgentCode); 
		}

		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryStr.append(" and transactions.TransactionID=:txnId"); 
		}
		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qryStr.append(superAgentName); 
		}

		if(txnSummaryDTO.getAgentName() != null && !"".equals(txnSummaryDTO.getAgentName())){
			qryStr.append(agentName); 
		}
		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryStr.append(" AND CONCAT(customer.FirstName, \" \", customer.LastName) LIKE :customerName");
		}

		if (null!= txnSummaryDTO.getSortBy() && txnSummaryDTO.getSortBy().equals("asc")) {
			qryStr.append("  ORDER BY ")
			.append(txnSummaryDTO.getSortColumn())
			.append(" ASC");
		} else {
			qryStr.append("  ORDER BY ")
			.append(txnSummaryDTO.getSortColumn())
			.append(" DESC");
		}
		//qryStr.append("  ORDER BY TransactionDate DESC");

		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString()); 
		qryResult1.setParameter("journalType", 1);
		qryResult1.setParameter("bookID2", 2);
		qryResult1.setParameter("bookID3", 3);
		if(txnSummaryDTO.getMobileNumber() != null && !"".equals(txnSummaryDTO.getMobileNumber())){{
			qryResult1.setParameter("mobileNumber", txnSummaryDTO.getMobileNumber());
		}
		}
		if(txnSummaryDTO.getCountryId() != null && !"".equals(txnSummaryDTO.getCountryId().toString())){
			qryResult1.setParameter("countryID", txnSummaryDTO.getCountryId());	
		}
		if(txnSummaryDTO.getBankId() != null && !"".equals(txnSummaryDTO.getBankId().toString())){
			qryResult1.setParameter("bankID", txnSummaryDTO.getBankId());
		}
		if(txnSummaryDTO.getBranchId() != null && !"".equals(txnSummaryDTO.getBranchId().toString())){
			qryResult1.setParameter("branchID", txnSummaryDTO.getBranchId());
		}
		/*if(txnSummaryDTO.getBusinessName() != null && !"".equals(txnSummaryDTO.getBusinessName())){	
			qryResult1.setParameter("businessName", txnSummaryDTO.getBusinessName());			
		}*/
		if(txnSummaryDTO!=null){ 				
			if(txnSummaryDTO.getFromDate()!=null){
				qryResult1.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
			}
			if(txnSummaryDTO.getToDate()!=null){
				qryResult1.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
			}
		}
		if(txnSummaryDTO.getBankGroupId() != null && !"".equals(txnSummaryDTO.getBankGroupId())){
			qryResult1.setParameter("bankGroupID", txnSummaryDTO.getBankGroupId());
		}
		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
			qryResult1.setParameter("userID",txnSummaryDTO.getUserId()+"%");
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){
			qryResult1.setParameter("ProfileID",txnSummaryDTO.getProfileId());
		}
		if(bankGroupId != null){
			qryResult1.setParameter("bankGroupID", bankGroupId);
		}
		if(bankId != null){
			qryResult1.setParameter("bankID", bankId);
		}
		if(branchId != null){
			qryResult1.setParameter("branchID", branchId);
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qryResult1.setParameter("Txn1", 60);
				qryResult1.setParameter("Txn2", 84);
				qryResult1.setParameter("Txn3", 31);
				qryResult1.setParameter("Txn4", 137);
				qryResult1.setParameter("Txn5", 138);
			}
		}
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()!=null){
				qryResult1.setParameter("Txn", txnSummaryDTO.getTransactionType());
			}
		}
		if(StringUtils.isNotEmpty(txnSummaryDTO.getPartnerType()) && StringUtils.isNotEmpty(txnSummaryDTO.getPartnerId())){
			qryResult1.setParameter("partnerID", txnSummaryDTO.getPartnerId());
		}
		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
			qryResult1.setParameter("agentCode", txnSummaryDTO.getAgentCode());
		}
		qryResult1.setParameter("status", EOTConstants.TXN_NO_ERROR);
		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){

			switch(txnSummaryDTO.getRequestChannel()) {
			case "1": qryResult1.setParameter("requestChannel", "Web");
			break;
			case "2": qryResult1.setParameter("requestChannel", "Mobile"); 
			break;
			case "3": qryResult1.setParameter("requestChannel", "USSD");
			break;
			}						
		}	
		if(txnSummaryDTO!=null){ 
			if(StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber()) && StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber())){
				qryResult1.setParameter("accNumber", txnSummaryDTO.getAccountNumber());
			}
		}

		if(txnSummaryDTO.getAgentMobileNumber() != null && !"".equals(txnSummaryDTO.getAgentMobileNumber())){
			qryResult1.setParameter("agentCell", txnSummaryDTO.getAgentMobileNumber());
		}
		if(txnSummaryDTO.getMerchantCode() != null && !"".equals(txnSummaryDTO.getMerchantCode())){

			qryResult1.setParameter("agentCode", txnSummaryDTO.getMerchantCode());
		}
		if(txnSummaryDTO.getSuperAgentCode() != null && !"".equals(txnSummaryDTO.getSuperAgentCode())){
			qryResult1.setParameter("superAgentCode", txnSummaryDTO.getSuperAgentCode());
		}
		if(txnSummaryDTO.getTxnId() != null && txnSummaryDTO.getTxnId()!=""){
			qryResult1.setParameter("txnId",txnSummaryDTO.getTxnId()); 
		}
		if(txnSummaryDTO.getSuperAgentName() != null && !"".equals(txnSummaryDTO.getSuperAgentName())){
			qryResult1.setParameter("superAgentName", "%"+txnSummaryDTO.getSuperAgentName()+"%"); 
		}

		if(txnSummaryDTO.getAgentName() != null && !"".equals(txnSummaryDTO.getAgentName())){
			qryResult1.setParameter("agentName", "%"+txnSummaryDTO.getAgentName()+"%"); 
		}

		if(txnSummaryDTO.getCustomerName() != null && !"".equals(txnSummaryDTO.getCustomerName())){
			qryResult1.setParameter("customerName", "%"+txnSummaryDTO.getCustomerName()+"%"); 
		}

		if (!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType())) {
			qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
			qryResult1.setMaxResults(appConfig.getResultsPerPage());
		}

		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public Page searchBulkPayTxnReport(TxnSummaryDTO txnSummaryDTO, Integer pageNumber) {

		StringBuilder queryCountBuilder = new StringBuilder("SELECT COUNT(DISTINCT(txn.TransactionID)) FROM Transactions txn JOIN TransactionTypes tt ON tt.TransactionType = txn.TransactionType AND txn.TransactionType=141 "
				+ "INNER JOIN TransactionJournals tj ON tj.TransactionID=txn.TransactionID AND JournalType=1 "
				+ "INNER JOIN Customer cust ON txn.referenceId = cust.customerId "
				+ "INNER JOIN BusinessPartner bp ON bp.AccountNumber=txn.CustomerAccount "
				+ "INNER JOIN BusinessPartnerUser bpu ON bpu.partnerId=bp.Id "
				+ "INNER JOIN WebUsers wu ON wu.UserName=bpu.UserName AND bpu.UserName=:loginPartner ");
		
		if(StringUtils.isNotEmpty(txnSummaryDTO.getMobileNumber())){
			queryCountBuilder.append(" AND cust.MobileNumber =:mobileN ");
		}
		if(txnSummaryDTO.getFromDate() != null && txnSummaryDTO.getToDate() != null){
			queryCountBuilder.append(" AND txn.TransactionDate >=:fromDate AND txn.TransactionDate <= :toDate ");
		}
		SQLQuery qryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryCountBuilder.toString());
		qryResult.setParameter("loginPartner", txnSummaryDTO.getLoginUserName());
		if(StringUtils.isNotEmpty(txnSummaryDTO.getMobileNumber())){
			qryResult.setParameter("mobileN", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getFromDate() != null && txnSummaryDTO.getToDate() != null){
			qryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate())+" 00:00:00");
			qryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate())+" 23:59:59");
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT(txn.TransactionID) AS TransactionID, CONCAT(cust.FirstName,' ',cust.LastName) AS Name, cust.MobileNumber AS MobileNumber,txn.TransactionDate AS TransactionDate, txn.Amount, (SELECT amount FROM TransactionJournals WHERE TransactionID=txn.TransactionID AND JournalType=1) AS SC, "
				+ "txn.Status AS Status FROM Transactions txn JOIN TransactionTypes tt ON tt.TransactionType = txn.TransactionType AND txn.TransactionType=141 "
				+ "INNER JOIN TransactionJournals tj ON tj.TransactionID=txn.TransactionID "
				+ "INNER JOIN Customer cust ON txn.referenceId = cust.customerId "
				+ "INNER JOIN BusinessPartner bp ON bp.AccountNumber=txn.CustomerAccount "
				+ "INNER JOIN BusinessPartnerUser bpu ON bpu.partnerId=bp.Id "
				+ "INNER JOIN WebUsers wu ON wu.UserName=bpu.UserName AND bpu.UserName=:loginPartner ");
		if(StringUtils.isNotEmpty(txnSummaryDTO.getMobileNumber())){
			queryBuilder.append(" AND cust.MobileNumber=:mobileN ");
		}
		if(txnSummaryDTO.getFromDate() != null &&  txnSummaryDTO.getToDate() != null){
			queryBuilder.append(" AND txn.TransactionDate >=:fromDate AND txn.TransactionDate <= :toDate ");
		}
		queryBuilder.append(" ORDER BY txn.TransactionID DESC");
		SQLQuery queryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryBuilder.toString());
		queryResult.setParameter("loginPartner", txnSummaryDTO.getLoginUserName());
		if(StringUtils.isNotEmpty(txnSummaryDTO.getMobileNumber())){
			queryResult.setParameter("mobileN", txnSummaryDTO.getMobileNumber());
		}
		if(txnSummaryDTO.getFromDate() != null &&  txnSummaryDTO.getToDate()!= null){
			queryResult.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate())+" 00:00:00");
			queryResult.setParameter("toDate", DateUtil.formatDate(txnSummaryDTO.getToDate())+" 23:59:59");
		}
		if(pageNumber > 0){
			queryResult.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
			queryResult.setMaxResults(appConfig.getResultsPerPage());
		}
		
		queryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(queryResult.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page BankFloatDepositReportData(BankFloatDepositDTO bankFloatDepositDTO, Integer pageNumber) {
		StringBuilder query = new StringBuilder("SELECT \r\n" + 
				"COUNT(DISTINCT(txn.TransactionID))\r\n" + 
				"FROM Transactions txn \r\n" + 
				"JOIN Account acc ON acc.AccountNumber = txn.OtherAccount\r\n" + 
				"LEFT JOIN Bank bank ON \r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 5 THEN bank.BankID = acc.ReferenceID \r\n" + 
				"END \r\n" + 
				"LEFT JOIN Customer agent ON \r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 1 THEN agent.CustomerID = acc.ReferenceID\r\n" + 
				"END\r\n" + 
				"LEFT JOIN BusinessPartner bp ON \r\n" + 
				"CASE\r\n" + 
				"WHEN acc.ReferenceType = 8 THEN bp.Id = acc.ReferenceID \r\n" + 
				"END\r\n" + 
				"WHERE txn.TransactionType = 121");
		

		if(bankFloatDepositDTO.getFromDate() != null && bankFloatDepositDTO.getToDate() != null){
			query.append(" AND txn.TransactionDate >=:fromDate AND txn.TransactionDate <= :toDate ");
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getCode())){
			query.append("AND CASE " +
							"WHEN acc.ReferenceType = 5 THEN bank.BankCode=:code " +
							"WHEN acc.ReferenceType = 1 THEN agent.AgentCode=:code " +
							"WHEN acc.ReferenceType = 8 THEN bp.Code=:code " + 
							"END");
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getTxnId())){
			query.append(" AND txn.TransactionID =:txnId ");
		}
		SQLQuery qryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(query.toString());
		if(bankFloatDepositDTO.getFromDate() != null && bankFloatDepositDTO.getToDate() != null){
			qryResult.setParameter("fromDate", DateUtil.formatDate(bankFloatDepositDTO.getFromDate())+" 00:00:00");
			qryResult.setParameter("toDate", DateUtil.formatDate(bankFloatDepositDTO.getToDate())+" 23:59:59");
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getCode())){
			qryResult.setParameter("code", bankFloatDepositDTO.getCode());
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getTxnId())){
			qryResult.setParameter("txnId", bankFloatDepositDTO.getTxnId());
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		StringBuilder queryStr = new StringBuilder("SELECT \r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 5 THEN bank.BankName\r\n" + 
				"WHEN acc.ReferenceType = 1 THEN CONCAT(agent.FirstName,\" \",agent.LastName)\r\n" + 
				"WHEN acc.ReferenceType = 8 THEN bp.Name\r\n" + 
				"END AS NAME,\r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 5 THEN bank.BankCode\r\n" + 
				"WHEN acc.ReferenceType = 1 THEN agent.AgentCode\r\n" + 
				"WHEN acc.ReferenceType = 8 THEN bp.Code\r\n" + 
				"END AS CODE,\r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 5 THEN \"Service Provider\"\r\n" + 
				"WHEN acc.ReferenceType = 1 THEN \"Agent\"\r\n" + 
				"WHEN bp.PartnerType = 1 THEN \"Principal Agent\"\r\n" + 
				"WHEN bp.PartnerType = 2 THEN \"Super Agent\"\r\n" + 
				"WHEN bp.PartnerType = 4 THEN \"BulkPay Partner\"\r\n" + 
				"END AS TYPE,\r\n" + 
				"acc.AccountNumber,txn.TransactionID,txn.Amount,\r\n" + 
				"txn.TransactionDate,txn.Status FROM Transactions txn \r\n" + 
				"JOIN Account acc ON acc.AccountNumber = txn.OtherAccount\r\n" + 
				"LEFT JOIN Bank bank ON \r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 5 THEN bank.BankID = acc.ReferenceID \r\n" + 
				"END \r\n" + 
				"LEFT JOIN Customer agent ON \r\n" + 
				"CASE \r\n" + 
				"WHEN acc.ReferenceType = 1 THEN agent.CustomerID = acc.ReferenceID\r\n" + 
				"END\r\n" + 
				"LEFT JOIN BusinessPartner bp ON \r\n" + 
				"CASE\r\n" + 
				"WHEN acc.ReferenceType = 8 THEN bp.Id = acc.ReferenceID \r\n" + 
				"END\r\n" + 
				"WHERE txn.TransactionType = 121");

		if(bankFloatDepositDTO.getFromDate() != null &&  bankFloatDepositDTO.getToDate() != null){
			queryStr.append(" AND txn.TransactionDate >=:fromDate AND txn.TransactionDate <= :toDate ");
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getCode())){
			queryStr.append("AND CASE " +
							"WHEN acc.ReferenceType = 5 THEN bank.BankCode=:code " +
							"WHEN acc.ReferenceType = 1 THEN agent.AgentCode=:code " +
							"WHEN acc.ReferenceType = 8 THEN bp.Code=:code " + 
							"END");
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getTxnId())){
			queryStr.append(" AND txn.TransactionID =:txnId ");
		}
		queryStr.append(" ORDER BY txn.TransactionDate DESC");
		SQLQuery queryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryStr.toString());

		if(bankFloatDepositDTO.getFromDate() != null &&  bankFloatDepositDTO.getToDate()!= null){
			queryResult.setParameter("fromDate", DateUtil.formatDate(bankFloatDepositDTO.getFromDate())+" 00:00:00");
			queryResult.setParameter("toDate", DateUtil.formatDate(bankFloatDepositDTO.getToDate())+" 23:59:59");
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getCode())){
			queryResult.setParameter("code", bankFloatDepositDTO.getCode());
		}
		if(StringUtils.isNotBlank(bankFloatDepositDTO.getTxnId())){
			queryResult.setParameter("txnId", bankFloatDepositDTO.getTxnId());
		}
//		if(pageNumber > 0 && !EOTConstants.ACTION_EXPORT.equals(bankFloatDepositDTO.getActionType())){
//			queryResult.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
//			queryResult.setMaxResults(appConfig.getResultsPerPage());
//		}
		queryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(queryResult.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page NonRegUssdCustomerReportData(NonRegUssdCustomerDTO nonRegUssdCustomerDTO, Integer pageNumber) {
		StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT(usd.msisdn)) FROM `ussd_session` usd,Customer cust \r\n" + 
				"WHERE usd.msisdn!=CONCAT(211,cust.MobileNumber) AND STATUS=99");
		

		if(nonRegUssdCustomerDTO.getFromDate() != null && nonRegUssdCustomerDTO.getToDate() != null){
			query.append(" AND usd.createddate >=:fromDate AND usd.createddate <= :toDate ");
		}

		if(StringUtils.isNotBlank(nonRegUssdCustomerDTO.getMobileNumber())){
			query.append(" AND usd.msisdn =:mobileNumber ");
		}
		
		
		SQLQuery qryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(query.toString());
		if(nonRegUssdCustomerDTO.getFromDate() != null && nonRegUssdCustomerDTO.getToDate() != null){
			qryResult.setParameter("fromDate", DateUtil.formatDate(nonRegUssdCustomerDTO.getFromDate())+" 00:00:00");
			qryResult.setParameter("toDate", DateUtil.formatDate(nonRegUssdCustomerDTO.getToDate())+" 23:59:59");
		}
		if(StringUtils.isNotBlank(nonRegUssdCustomerDTO.getMobileNumber())){
			qryResult.setParameter("mobileNumber", nonRegUssdCustomerDTO.getMobileNumber());
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		StringBuilder queryStr = new StringBuilder("SELECT DISTINCT(usd.msisdn) as MSISDN,usd.createddate as createdDate FROM `ussd_session` usd,Customer cust \r\n" + 
				"WHERE usd.msisdn!=CONCAT(211,cust.MobileNumber) AND STATUS=99");

		if(nonRegUssdCustomerDTO.getFromDate() != null &&  nonRegUssdCustomerDTO.getToDate() != null){
			queryStr.append(" AND usd.createddate >=:fromDate AND usd.createddate <= :toDate ");
		}
		if(StringUtils.isNotBlank(nonRegUssdCustomerDTO.getMobileNumber())){
			queryStr.append(" AND usd.msisdn =:mobileNumber ");
		}
		queryStr.append(" GROUP BY usd.msisdn ORDER BY usd.createddate DESC");
		SQLQuery queryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryStr.toString());

		if(nonRegUssdCustomerDTO.getFromDate() != null &&  nonRegUssdCustomerDTO.getToDate()!= null){
			queryResult.setParameter("fromDate", DateUtil.formatDate(nonRegUssdCustomerDTO.getFromDate())+" 00:00:00");
			queryResult.setParameter("toDate", DateUtil.formatDate(nonRegUssdCustomerDTO.getToDate())+" 23:59:59");
		}
		if(StringUtils.isNotBlank(nonRegUssdCustomerDTO.getMobileNumber())){
			queryResult.setParameter("mobileNumber", nonRegUssdCustomerDTO.getMobileNumber());
		}

		queryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(queryResult.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page transactionVolumeReportData(TransactionVolumeDTO transactionVolumeDTO, Integer pageNumber) {	

		StringBuilder queryStr = new StringBuilder("SELECT Name,Code,AgentName,MobileNumber,AgentCode,TransactionDate,\r\n" + 
				"SUM(TotalDeposit) AS TotalDeposit,SUM(DepositVolume) AS DepositVolume,\r\n" + 
				"SUM(TotalWithDrawal) AS TotalWithDrawal,SUM(WithdrawalVolume) AS WithdrawalVolume,CurrentBalance,CommissionBalance\r\n" + 
				"FROM (\r\n" + 
				"SELECT bp.Name AS Name,bp.Code AS Code,CONCAT(agent.FirstName,\" \",agent.LastName) AS AgentName,\r\n" + 
				"agent.MobileNumber AS MobileNumber,agent.agentCode AS AgentCode,txn.TransactionDate AS TransactionDate,\r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType=115 THEN COUNT(txn.TransactionID)\r\n" + 
				"END AS TotalDeposit,\r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType=115 THEN SUM(txn.Amount) \r\n" + 
				"END AS DepositVolume,\r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType=116 THEN COUNT(txn.TransactionID)\r\n" + 
				"END AS TotalWithDrawal,\r\n" + 
				"CASE \r\n" + 
				"WHEN txn.TransactionType=116 THEN SUM(txn.Amount) \r\n" + 
				"END AS WithdrawalVolume,\r\n" + 
				"(SELECT CurrentBalance FROM Account acc WHERE acc.aliasType=10 AND acc.ReferenceID = agent.CustomerID AND acc.ReferenceType = 1) AS CommissionBalance,\r\n" + 
				"CASE \r\n" + 
				"WHEN acc.aliasType=1 THEN acc.CurrentBalance\r\n" + 
				"END AS CurrentBalance "+
				"FROM Transactions AS txn  \r\n" + 
				"JOIN Customer agent ON agent.CustomerID = txn.ReferenceId AND agent.Type=1 \r\n" + 
				"JOIN BusinessPartner AS bp ON bp.Id = agent.PartnerId \r\n" + 
				"JOIN Account acc ON acc.ReferenceID = agent.CustomerID AND acc.ReferenceType = 1 AND acc.aliasType = 1\r\n" + 
				"WHERE PartnerType=2 AND txn.Status=2000 AND txn.TransactionType IN (115,116)"); 

				

		if(transactionVolumeDTO.getFromDate() != null &&  transactionVolumeDTO.getToDate() != null){
			queryStr.append(" AND txn.TransactionDate >=:fromDate AND txn.TransactionDate <= :toDate ");
		}
		if(StringUtils.isNotBlank(transactionVolumeDTO.getCode())){
			queryStr.append(" AND bp.code =:code ");
		}
		if(StringUtils.isNotBlank(transactionVolumeDTO.getAgentCode())){
			queryStr.append(" AND agent.agentCode =:agentCode ");
		}
		queryStr.append(" GROUP BY txn.TransactionType,agent.CustomerID) AS restult GROUP BY AgentCode ORDER BY TransactionDate DESC");
		SQLQuery queryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryStr.toString());

		if(transactionVolumeDTO.getFromDate() != null &&  transactionVolumeDTO.getToDate()!= null){
			queryResult.setParameter("fromDate", DateUtil.formatDate(transactionVolumeDTO.getFromDate())+" 00:00:00");
			queryResult.setParameter("toDate", DateUtil.formatDate(transactionVolumeDTO.getToDate())+" 23:59:59");
		}
		if(StringUtils.isNotBlank(transactionVolumeDTO.getCode())){
			queryResult.setParameter("code", transactionVolumeDTO.getCode());
		}
		if(StringUtils.isNotBlank(transactionVolumeDTO.getAgentCode())){
			queryResult.setParameter("agentCode", transactionVolumeDTO.getAgentCode());
		}

		queryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(queryResult.list(), queryResult.list().size(), appConfig.getResultsPerPage(), pageNumber);
	
	}
	public WebUser getUser(String userName) {
		return getHibernateTemplate().get(WebUser.class,userName);
	}

	@SuppressWarnings("unchecked")
	public AppMaster getApplicationType(String applicationId) {

		List<AppMaster> appList = getHibernateTemplate().findByNamedParam("from AppMaster app where app.appId=:appId","appId", applicationId);

		return appList.size() > 0 ? appList.get(0) : null ;

	}
	
	
}
