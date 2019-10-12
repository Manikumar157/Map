package com.eot.banking.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eot.banking.dao.BrandingDao;
import com.eot.entity.Bank;
import com.eot.entity.Branding;

@Repository("brandingDao")
public class BrandingDaoImpl  extends BaseDaoImpl implements BrandingDao{

	@Override
	public Branding findByBankId(Integer bankId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		//Query query = getSessionFactory().getCurrentSession().createQuery("from Branding m where m.bank.bankId=:bankId").setParameter("bankId", bankId);
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Branding m where m.bank.bankId="+bankId);*/
		//@End
		Criteria query = getSessionFactory().getCurrentSession().createCriteria(Branding.class);
		query.add(Restrictions.eq("bank.bankId", bankId));
		return query.list().size()>0 ? (Branding) query.list().get(0) : null;
	}

}
