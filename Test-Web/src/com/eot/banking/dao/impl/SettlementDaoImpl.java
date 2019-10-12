package com.eot.banking.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.SettlementDao;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.entity.Bank;
import com.eot.entity.ClearingHousePool;
import com.eot.entity.SettlementJournal;

public class SettlementDaoImpl extends BaseDaoImpl implements SettlementDao{

	@Override
	public List<SettlementJournal> getUnSettledEntries(Integer clearingPoolID,Date settlementDate) {

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();	
		Criteria criteria=session.createCriteria(SettlementJournal.class);
		criteria.add(Restrictions.gt("amount",0D));	

		if(clearingPoolID!=null && ! "".equals(clearingPoolID)){			
			criteria.add(Restrictions.eq("clearingHousePool.clearingPoolId",clearingPoolID));		
		}

		if((settlementDate!=null && ! "".equals( settlementDate))){

			//criteria.add(Restrictions.sqlRestriction(" JournalTime like ?",DateUtil.formatDate(settlementDate)+"%",Hibernate.DATE));
			criteria.add(Restrictions.sqlRestriction(" JournalTime like ?",DateUtil.formatDate(settlementDate)+"%",Hibernate.STRING));

		}

		return criteria.list();
	}

	@Override
	public List<Bank> getBanksByPoolId(int clearingPoolId) {

		Query query = getSessionFactory().getCurrentSession().createQuery("select cpmember.bank from ClearingHousePoolMember as cpmember where cpmember.clearingPoolId=:clearingPoolId ")
				.setParameter("clearingPoolId", clearingPoolId);

		List<Bank> list = query.list();
		return list.size()>0 ? list : null ;
	}

	@Override
	public List<ClearingHousePool> getClearingHousesByBankId(Integer bankId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("select cp from ClearingHousePool as cp,ClearingHousePoolMember cpm where cp.clearingPoolId=cpm.clearingPoolId and cpm.bank.bankId=:bankId ")
				.setParameter("bankId", bankId);

		List<ClearingHousePool> list = query.list();
		return list.size()>0 ? list : null ;
	}

	@Override
	public Bank getBankName(String userName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("select bank from Bank as bank,BankTellers as teller where bank.bankId=teller.bank.bankId and teller.userName=:userName ")
				.setParameter("userName", userName);

		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public List generateCSVFile(Integer poolId, Integer bankId,ClearingHouseDTO clearingHouseDTO) {
		StringBuffer  qryStr = new StringBuffer("SELECT  DISTINCT(transactions.TransactionID),customer.CustomerID,customer.FirstName,transactions.CustomerAccountType,country.CountryCodeNumeric,customer.MobileNumber,bank.BankCode,branch.BranchCode,transactions.amount,transactions.TransactionType,transactions.TransactionDate," +
				" (SELECT Amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID AND JournalType= :journalType ) AS SC,(SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=:accountHeadID) AND tj.TransactionID=transactions.TransactionID ) AS stampFee," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID= :accountHeadID54) AND tj.TransactionID=transactions.TransactionID ) AS tax," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID= :accountHeadID55) AND tj.TransactionID=transactions.TransactionID ) AS bankShare," +
				" (SELECT tj.Amount FROM TransactionJournals tj WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID= :accountHeadID102) AND tj.TransactionID=transactions.TransactionID ) AS gimShare," +
				" (SELECT bank.BankCode FROM Bank AS bank WHERE bank.BankID IN(SELECT ca.BankID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType= :transactionType55)) AS BenificiaryBankCode," +
				" (SELECT branch.BranchCode FROM Branch AS branch WHERE branch.BranchID IN(SELECT ca.BranchID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType= :transactionType55)) AS BenificiaryBranchCode," +
				" (SELECT cust.MobileNumber FROM Customer cust WHERE cust.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType= :transactionType55)) AS BenificiaryCustomerMobileNumber," +
				" (SELECT country.CountryCodeNumeric FROM Country country WHERE country.CountryID IN(SELECT customer.CountryID FROM Customer AS customer WHERE customer.CustomerID IN(SELECT ca.CustomerID FROM CustomerAccounts ca JOIN TransactionJournals tj WHERE ca.AccountNumber = transactions.OtherAccount AND tj.CreditAccount= transactions.OtherAccount AND transactions.TransactionID=tj.TransactionID AND transactions.TransactionType= :transactionType55))) AS BenificiaryCountryISDCode, " +
				" (SELECT bank.BankCode FROM Bank bank WHERE bank.BankID IN(SELECT BankID FROM BankTellers bt WHERE bt.UserName IN(SELECT wr.UserName FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID))) AS AcquirerBankCode," +
				" (SELECT branch.BranchCode FROM Branch branch WHERE branch.BranchID IN(SELECT BranchID FROM BankTellers bt WHERE bt.UserName IN(SELECT wr.UserName FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID))) AS AcquirerBranchCode,(SELECT transactions.OtherAccount FROM TransactionJournals tj WHERE transactions.TransactionID=tj.TransactionID AND transactions.TransactionType= :transactionType83 AND tj.JournalType=1) AS BenificiaryMobileNumberforSMSCash" +
				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN TransactionJournals AS tj ON transactions.TransactionID=tj.TransactionID JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID " +
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID AND bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON customer.CountryID=country.CountryID" +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID ");		

		if(clearingHouseDTO.getDate()!=null){
			qryStr.append(" where DATE(tj.JournalTime)= ? ");
		}

		qryStr.append(" AND chp.ClearingPoolID= :poolId  and transactions.Status= :txnError and transactions.TransactionType != :transactionType60 and transactions.TransactionType != :transactionType84 AND (bank.BankID=:bankId OR (SELECT bank.BankID FROM Bank bank WHERE bank.BankID IN(SELECT BankID FROM BankTellers bt WHERE bt.UserName IN(SELECT wr.UserName FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID)))= :bankId)"); 
		Query qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());


		if(clearingHouseDTO.getDate()!=null){
			qryResult.setString(0, DateUtil.formatDate(clearingHouseDTO.getDate()));
		}
		
		qryResult.setParameter("journalType", 1);
		qryResult.setParameter("accountHeadID", 63);
		qryResult.setParameter("accountHeadID54", 54);
		qryResult.setParameter("accountHeadID55", 55);
		qryResult.setParameter("accountHeadID102", 102);
		qryResult.setParameter("transactionType55", 55);
		qryResult.setParameter("transactionType83", 83);
		qryResult.setParameter("transactionType60", 60);
		qryResult.setParameter("transactionType84", 84);
		qryResult.setParameter("poolId", poolId);
		qryResult.setParameter("txnError", EOTConstants.TXN_NO_ERROR);
		qryResult.setParameter("bankId", bankId);

		return qryResult.list();
	}
	
	// tj.Active != 10 has been removed from the below query due to settlement summary file amount mismatch

/*	@Override
	public List generateSettlementSummaryCSVFile(Integer poolId, Integer bankId,ClearingHouseDTO clearingHouseDTO) {

		String date=DateUtil.formatDate(clearingHouseDTO.getDate());

		StringBuffer  qryStr = new StringBuffer("SELECT  COUNT(transactions.TransactionID),SUM(transactions.amount),transactions.TransactionType,transactions.CustomerAccountType," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"' AND tj.Active!=10 AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn INNER JOIN Customer AS customer" +
				" ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location AND JournalType=1 )) AS SC," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.Active!=10 AND tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=63 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS stampFee," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.Active!=10 AND tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=54 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS tax," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.Active!=10 AND tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=55 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID " +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS bankShare," +
				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.Active!=10 AND tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=102 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location) ) AS gimShare" +
				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID " +
				" JOIN TransactionJournals AS tj ON transactions.TransactionID=tj.TransactionID JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID " +
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID " +
				" JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID " +
				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID AND customer.ProfileID=custprof.ProfileID AND tj.BookID=1 AND tj.JournalType=1 ");		

		if(clearingHouseDTO.getDate()!=null){
			qryStr.append(" where DATE(transactions.TransactionDate)= ? ");
		}

		qryStr.append(" AND chp.ClearingPoolID='"+poolId+"' and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' and transactions.TransactionType != 60 and transactions.TransactionType != 84 AND (bank.BankID='"+bankId+"' OR " +
				"(SELECT bank.BankID FROM Bank bank WHERE bank.BankID IN(SELECT BankID FROM BankTellers bt WHERE bt.UserName IN(SELECT wr.UserName FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID)))='"+bankId+"') GROUP BY transactions.TransactionType"); 
		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());


		if(clearingHouseDTO.getDate()!=null){
			qryResult.setString(0, DateUtil.formatDate(clearingHouseDTO.getDate()));
		}


		return qryResult.list();
	}*/
	
	@Override
	public List generateSettlementSummaryCSVFile(Integer poolId, Integer bankId,ClearingHouseDTO clearingHouseDTO) {

		String date=DateUtil.formatDate(clearingHouseDTO.getDate());

//		StringBuffer  qryStr = new StringBuffer("SELECT  COUNT(transactions.TransactionID),SUM(transactions.amount),transactions.TransactionType,transactions.CustomerAccountType," +
//				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"' AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn INNER JOIN Customer AS customer" +
//				" ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID" +
//				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location AND JournalType=1 )) AS SC," +
//				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=63 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
//				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
//				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
//				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS stampFee," +
//				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=54 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
//				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
//				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
//				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS tax," +
//				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=55 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
//				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID " +
//				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
//				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location)) AS bankShare," +
//				" (SELECT SUM(tj.Amount) FROM TransactionJournals tj JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID WHERE tj.CreditAccount IN(SELECT AccountNumber FROM AccountHeadMapping WHERE AccountHeadID=102 AND DATE(tj.JournalTime)='"+date+"' AND chp.ClearingPoolID='"+poolId+"') AND tj.TransactionID IN(SELECT txn.TransactionID FROM Transactions txn " +
//				" INNER JOIN Customer AS customer ON txn.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID" +
//				" JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch1 ON custacc.BranchID=branch1.BranchID JOIN CustomerProfiles AS custprof" +
//				" ON bank.BankID=custprof.BankID  WHERE txn.TransactionType=transactions.TransactionType AND branch1.Location=branch.Location) ) AS gimShare" +
//				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID " +
//				" JOIN TransactionJournals AS tj ON transactions.TransactionID=tj.TransactionID JOIN ClearingHousePool AS chp ON chp.ClearingPoolID=tj.ClearingPoolID " +
//				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID " +
//				" JOIN Branch AS branch ON custacc.BranchID=branch.BranchID JOIN Country AS country ON bank.CountryID=country.CountryID " +
//				" JOIN CustomerProfiles AS custprof ON bank.BankID=custprof.BankID AND customer.ProfileID=custprof.ProfileID AND tj.BookID=1 ");
//				/*" AND tj.JournalType=1 ");*/	
//
//		if(clearingHouseDTO.getDate()!=null){
//			qryStr.append(" where DATE(transactions.TransactionDate)= ? ");
//		}
//
//		qryStr.append(" AND chp.ClearingPoolID='"+poolId+"' and transactions.Status='"+EOTConstants.TXN_NO_ERROR+"' and transactions.TransactionType != 60 and transactions.TransactionType != 84 AND (bank.BankID='"+bankId+"' OR " +
//				"(SELECT bank.BankID FROM Bank bank WHERE bank.BankID IN(SELECT BankID FROM BankTellers bt WHERE bt.UserName IN(SELECT wr.UserName FROM WebRequests wr WHERE wr.TransactionID=transactions.TransactionID)))='"+bankId+"') GROUP BY transactions.TransactionType");
		
		StringBuffer  qryStr = new StringBuffer("SELECT SUM(TransactionID) AS TransactionID, SUM(amount) AS amount,TransactionType,CustomerAccountType, SUM(SC) AS SC, SUM(stampFee) AS stampFee, "+ 
				" SUM(tax) AS tax,SUM(bankShare) AS bankShare,SUM(gimShare) AS gimShare,TransactionDate "+ 

				"FROM "+ 

				"( "+ 
				"SELECT  "+ 
				"0 AS TransactionID,  0 AS amount, "+ 
				"t.TransactionType, t.CustomerAccountType, SUM(t.SC) AS SC, SUM(t.tax) AS tax, "+ 
				"SUM(t.stampFee) AS stampFee, SUM(t.gimShare) AS gimShare, SUM(t.bankShare) AS bankShare,t.TransactionDate "+ 
				"FROM ( "+ 

				"SELECT  0 AS TransactionID, 0 AS amount,  transactions.TransactionType,transactions.CustomerAccountType, transactions.TransactionDate AS TransactionDate, "+ 

				"CASE WHEN tj1.JournalType= :journalType THEN "+ 
				"  tj1.amount  "+ 
				"  ELSE "+ 
				"  0 "+ 
				" END AS SC, "+ 

				"CASE WHEN ahm.AccountHeadID=:accountHeadID54 THEN "+ 
				"  tj1.amount  "+ 
				"  ELSE "+ 
				"  0 "+ 
				" END AS tax, "+ 
				 
				" CASE WHEN ahm.AccountHeadID=:accountHeadID63 THEN "+ 
				"  tj1.amount  "+ 
				"  ELSE "+ 
				"  0 "+ 
				" END AS stampFee, "+ 
				 
				" CASE WHEN ahm.AccountHeadID=:accountHeadID102 THEN "+ 
				"  tj1.amount  "+ 
				"  ELSE "+ 
				"  0 "+ 
				" END AS gimShare, "+ 

				" CASE WHEN ahm.AccountHeadID=:accountHeadID105 THEN "+ 
				"  tj1.amount  "+ 
				"  ELSE "+ 
				"  0 "+ 
				" END AS bankShare "+ 

				" FROM Transactions AS transactions  "+ 
				" LEFT JOIN TransactionJournals AS tj1 ON tj1.TransactionID = transactions.TransactionID  "+ 
				" INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID  "+ 
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID  "+ 
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID   "+ 
				" LEFT JOIN AccountHeadMapping ahm ON ahm.AccountNumber = tj1.CreditAccount "+ 
				" AND bank.BankID= :bankId AND transactions.TransactionType != :transactionType60 AND transactions.TransactionType != :transactionType84   "+ 
				" WHERE DATE(transactions.TransactionDate)=? "+ 
				" AND transactions.Status= :txnError AND (tj1.ClearingPoolID=:poolId OR tj1.ClearingPoolID IS NULL) "+ 
				" ) AS t  "+ 
				" GROUP BY t.TransactionType  "+ 

				" UNION  "+ 

				"SELECT COUNT(transactions.TransactionID) AS TransactionID, SUM(transactions.amount),   "+ 
				"TransactionType AS TransactionType, 0 AS CustomerAccountType, "+ 
				"0 AS SC, "+ 
				"0 AS tax, "+ 
				"0 AS stampFee, "+ 
				"0 AS gimShare, "+ 
				"0 bankShare, "+ 
				"TransactionDate "+
				"FROM Transactions AS transactions  "+ 
				" INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID  "+ 
				" JOIN CustomerAccounts AS custacc ON customer.CustomerID=custacc.CustomerID  "+ 
				" JOIN Bank AS bank ON custacc.BankID=bank.BankID   "+ 
				" AND bank.BankID= :bankId AND transactions.TransactionType != :transactionType60 AND transactions.TransactionType != :transactionType84   "+ 
				" WHERE DATE(transactions.TransactionDate)=? "+ 
				" AND transactions.Status=:txnError  "+ 
				" GROUP BY transactions.TransactionType ) AS t1  "+ 
				" GROUP BY TransactionType");
		
		Query qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());
		
		qryResult.setParameter("journalType", 1);
		qryResult.setParameter("accountHeadID54", 54);
		qryResult.setParameter("accountHeadID63", 63);
		qryResult.setParameter("accountHeadID102", 102);
		qryResult.setParameter("accountHeadID105", 105);
		qryResult.setParameter("bankId", bankId);
		qryResult.setParameter("transactionType60", 60);
		qryResult.setParameter("transactionType84", 84);
		qryResult.setParameter("poolId", poolId);
		qryResult.setParameter("txnError", EOTConstants.TXN_NO_ERROR);
		


		if(clearingHouseDTO.getDate()!=null){
			qryResult.setString(0, DateUtil.formatDate(clearingHouseDTO.getDate()));
			qryResult.setString(1, DateUtil.formatDate(clearingHouseDTO.getDate()));
		}


		return qryResult.list();
	}

	@Override
	public List getSettlementNetBalance(ClearingHouseDTO clearingHouseDTO,Integer bankId) {		

		String date=DateUtil.formatDate(clearingHouseDTO.getDate());

		StringBuffer  qryStr = new StringBuffer("SELECT SUM(sj.Amount)AS Amount,sj.JournalTime,(SELECT SUM(sj.Amount) FROM SettlementJournals sj WHERE sj.BookID = :bookID3  AND sj.ClearingPoolID= :clearingPoolId AND DATE(sj.JournalTime)= :date AND sj.TransactionID IN" +
				" (SELECT sj.TransactionID FROM SettlementJournals sj JOIN Transactions AS transactions ON sj.TransactionID = transactions.TransactionID" +
				" INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS ca ON ca.CustomerID = customer.CustomerID" +
				" WHERE sj.BookID= :bookID2 AND DATE(sj.JournalTime)= :date AND sj.Amount != :amount AND ca.BankID= :bankId GROUP BY sj.TransactionID HAVING COUNT(*)=1)) AS GIMIntra " +
				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN SettlementJournals AS sj ON sj.TransactionID=transactions.TransactionID " +
				" AND transactions.Status=:txStatus AND sj.BookID IN (:bookingId) AND sj.ClearingPoolID=:clearingPoolId AND DATE(sj.JournalTime)=? and transactions.TransactionType != :transactionType60 and transactions.TransactionType != :transactionType84 and sj.Status!=:status ");		

		Query qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());


		if(clearingHouseDTO.getDate()!=null){
			qryResult.setString(0, DateUtil.formatDate(clearingHouseDTO.getDate()));
		}
		
		qryResult.setParameter("bookID3", 3);
		qryResult.setParameter("clearingPoolId", clearingHouseDTO.getClearingPoolId());
		qryResult.setParameter("date", date);
		qryResult.setParameter("bookID2", 2);
		qryResult.setParameter("bankId", bankId);
		qryResult.setParameter("amount", 0);
		qryResult.setParameter("txStatus", 2000);
		qryResult.setParameter("bookingId", "2,3");
		qryResult.setParameter("transactionType60", 60);
		qryResult.setParameter("transactionType84", 84);
		qryResult.setParameter("status", 10);
		
		
		qryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		return qryResult.list();
	}

	//@ Added by:- Vinod Joshi ,This method not Using anywhere, No required SQL injection.
	@Override
	public List getSettlementNetBalance1(ClearingHouseDTO clearingHouseDTO,Integer bankId) {		

		String date=DateUtil.formatDate(clearingHouseDTO.getDate());

		StringBuffer  qryStr = new StringBuffer("SELECT SUM(sj.Amount)AS Amount,sj.JournalTime,(SELECT SUM(sj.Amount) FROM SettlementJournals sj WHERE sj.BookID = 3  AND sj.ClearingPoolID='"+clearingHouseDTO.getClearingPoolId()+"' AND DATE(sj.JournalTime)='"+date+"' AND sj.TransactionID IN" +
				" (SELECT sj.TransactionID FROM SettlementJournals sj JOIN Transactions AS transactions ON sj.TransactionID = transactions.TransactionID" +
				" INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS ca ON ca.CustomerID = customer.CustomerID" +
				" WHERE sj.BookID=2 AND DATE(sj.JournalTime)= '"+date+"' AND sj.Amount !=0 AND ca.BankID="+bankId+" GROUP BY sj.TransactionID HAVING COUNT(*)=1)) AS GIMIntra " +
				" FROM Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN SettlementJournals AS sj ON sj.TransactionID=transactions.TransactionID " +
				" AND transactions.Status=2000 AND sj.BookID IN (2,3) AND sj.ClearingPoolID='"+clearingHouseDTO.getClearingPoolId()+"' AND DATE(sj.JournalTime)=? and transactions.TransactionType != 60 and transactions.TransactionType != 84 and sj.Status!=10 ");		

		SQLQuery qryResult=getSessionFactory().getCurrentSession().createSQLQuery(qryStr.toString());



		if(clearingHouseDTO.getDate()!=null){
			qryResult.setString(0, DateUtil.formatDate(clearingHouseDTO.getDate()));
		}


		return qryResult.list();
	}

}
