package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.BankGroupDao;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Bank;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;

public class BankGroupDaoImpl extends BaseDaoImpl implements BankGroupDao {

	@SuppressWarnings("unchecked")
	@Override
	public Page getBankGroupList(Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BankGroup.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(BankGroup.class);
		criteria1.addOrder(Order.asc("bankGroupName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<BankGroup> bankGroups = criteria1.list();

		return PaginationHelper.getPage(bankGroups, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public BankGroup getBankGroup(Integer bankGroupId) {		
		return getHibernateTemplate().get(BankGroup.class,bankGroupId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BankGroup getBankGroupByName(String bankGroupName) {		
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankGroup as bg where bg.bankGroupName=:bankGroupName").setParameter("bankGroupName", bankGroupName);
		List<BankGroup> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	// change vineeth, 16-07-2018, bugno: 5696
	@SuppressWarnings("unchecked")
	@Override
	public BankGroup getBankGroupShortName(String bankGroupShortName) {		
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankGroup as bg where bg.bankGroupShortName=:bankGroupShortName").setParameter("bankGroupShortName", bankGroupShortName);
		List<BankGroup> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	// end vineeth

	@Override
	public Page getBank(Integer bankGroupId,Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria.add(Restrictions.eq("bankGroup.bankGroupId", bankGroupId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria1.add(Restrictions.eq("bankGroup.bankGroupId", bankGroupId));
		criteria1.addOrder(Order.asc("bankName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Bank> banks = criteria1.list();

		return PaginationHelper.getPage(banks, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public BankGroupAdmin getGroupAdminByUsername(String userName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankGroupAdmin as ba where ba.userName=:userName").setParameter("userName", userName);
		List<BankGroupAdmin> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public BankGroup getBankGroupByName(String bankGroupName,Integer bankGroupId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankGroup as bg where bg.bankGroupName=:bankGroupName and bg.bankGroupId !=:bankGroupId")
				.setParameter("bankGroupName", bankGroupName)
				.setParameter("bankGroupId", bankGroupId);
		List<BankGroup> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

}
