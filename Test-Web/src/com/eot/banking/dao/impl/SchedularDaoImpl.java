package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.dao.SchedularDAO;
import com.eot.entity.Customer;
import com.eot.entity.PendingTransaction;

//@Repository("schedularDao")
@Transactional
public class SchedularDaoImpl extends BaseDaoImpl implements SchedularDAO{
	
	@Override

	public List<PendingTransaction> getUnprocessedPendingTxn() {
		Query query=getSessionFactory().getCurrentSession().createQuery("from PendingTransaction pTxn where pTxn.status=:status")
                .setParameter("status",100);
		List<PendingTransaction> list=query.list();
		
		return list;
	}
	
	@Override
	@Transactional
	public void updatePendingTxnByIds(String transactionIds) {
		/*Query query=getSessionFactory().getCurrentSession().createSQLQuery("Update PendingTransactions set status=104  where transactionId in( "+transactionIds+" )");
		query.executeUpdate();*/
		Query query=getSessionFactory().getCurrentSession().createSQLQuery("Update PendingTransactions set status=:status  where transactionId in( :txnID )");
		query.setParameter("status", 104);
		query.setParameter("txnID", transactionIds);
		query.executeUpdate();
		
	}

}
