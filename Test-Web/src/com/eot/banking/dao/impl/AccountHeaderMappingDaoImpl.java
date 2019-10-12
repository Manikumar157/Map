package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import com.eot.banking.dao.AccountHeadMappingDao;
import com.eot.banking.dto.SettlementDto;
import com.eot.entity.AccountHeadMapping;
import com.eot.entity.ClearingHousePool;

public class AccountHeaderMappingDaoImpl extends BaseDaoImpl implements
AccountHeadMappingDao {

	@SuppressWarnings("unchecked")
	@Override
	public String getAccountByHeadId(int accountHeadId, Integer bankId) {

		List<AccountHeadMapping> accountHeadMappings = getHibernateTemplate().find(
				"from AccountHeadMapping am where am.accountHead.headerId=? and am.bank.bankId=?", accountHeadId, bankId);
		if( accountHeadMappings.size() > 0 ) {

			return accountHeadMappings.get(0).getAccountNumber();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getAccountByHeadId(int accountHeadId) {

		List<AccountHeadMapping> accountHeadMappings = getHibernateTemplate().find(
				"from AccountHeadMapping am where am.accountHead.headerId=? and am.bank.bankId is null", accountHeadId);
		if( accountHeadMappings.size() > 0 ) {

			return accountHeadMappings.get(0).getAccountNumber();
		}
		return null;
	}

	public SettlementDto getAccountHeadIDsForAccounts( String debitAccountNo, String creditAccountNo ) {

		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(
				"select ah1.AccountHeadID as debitAccountId, ah2.AccountHeadID as creditAccountId, b.SwiftCode as swiftCode " +
						"from AccountHeadMapping ah1, AccountHeadMapping ah2 " +
						"left join Bank b on b.BankID=ah2.BankID " +
				"where ah1.AccountNumber=:debitAccountNo and ah2.AccountNumber=:creditAccountNo")
				.addScalar("debitAccountId",Hibernate.INTEGER)
				.addScalar("creditAccountId",Hibernate.INTEGER)
				.addScalar("swiftCode",Hibernate.STRING);
		query.setParameter("debitAccountNo", debitAccountNo);
		query.setParameter("creditAccountNo", creditAccountNo);
		SettlementDto settlementDto = null;
		List<Object[]> list = query.list();
		
		if( list.size() > 0 ) {

			Object[] objects = list.get(0);
			settlementDto = new SettlementDto();
			settlementDto.setSettlementParty(objects[2] == null ? null : objects[2] + "");
			settlementDto.setFromAccountHeadId((Integer)objects[0]);
			settlementDto.setToAccountHeadId((Integer)objects[1]);
			settlementDto.setFromAccountNo( debitAccountNo );
			settlementDto.setToAccountNo( creditAccountNo );
		}
		return settlementDto;
	}

	@Override
	public ClearingHousePool getguaranteeAccountNumber(Integer poolID) {
		
		return getHibernateTemplate().get(ClearingHousePool.class,poolID);
	}
}
