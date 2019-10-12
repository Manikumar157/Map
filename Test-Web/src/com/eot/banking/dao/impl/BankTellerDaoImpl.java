package com.eot.banking.dao.impl;

import org.hibernate.Query;

import com.eot.banking.dao.BankTellerDao;
import com.eot.entity.BankTellers;

public class BankTellerDaoImpl extends BaseDaoImpl implements BankTellerDao {

	@Override
	public BankTellers findByUserName(String userName) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
				Query query = getSessionFactory().getCurrentSession().createQuery("from BankTellers bt where bt.webUser.userName=?").setParameter(0, userName);
				//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from BankTellers bt where bt.webUser="+"'"+userName+"'");*/
		return query.list().size()>0 ? (BankTellers) query.list().get(0) : null;
	}
}
