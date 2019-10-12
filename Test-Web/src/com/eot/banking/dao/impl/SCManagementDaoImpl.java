package com.eot.banking.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.SCManagementDao;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.BusinessPartner;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.ServiceChargeRule;
import com.eot.entity.ServiceChargeRuleTxn;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.TransactionType;


public class SCManagementDaoImpl extends BaseDaoImpl implements SCManagementDao{

	@SuppressWarnings("unchecked")
	public List<ServiceChargeRule> getServiceChargeRuleList(){
		Query query=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeRule");
		return query.list();
	}

	public ServiceChargeRule getServiceChargeRule(Long serviceChargeRuleId){	
		return getHibernateTemplate().get(ServiceChargeRule.class,serviceChargeRuleId);	
//		Query query=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeRule scr where scr.serviceChargeRuleId=:serviceChargeRuleId order by").setParameter("serviceChargeRuleId", serviceChargeRuleId);
//		List<ServiceChargeRule> list = query.list();
//		ServiceChargeRule serviceChargeRule = list.get(0);
//		return serviceChargeRule!=null ? serviceChargeRule : null ;
	}
	public void deleteServiceChargeRuleTxns(Long serviceChargeRuleId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
				String qry = "delete from ServiceChargeRuleTxn as sc where sc.serviceChargeRule.serviceChargeRuleId=:serviceChargeRuleId";
				org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("serviceChargeRuleId", serviceChargeRuleId);
				//@End
		/*String qry = "delete from ServiceChargeRuleTxn as sc where sc.serviceChargeRule.serviceChargeRuleId="+serviceChargeRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();

	}
	public void deleteServiceChargeRuleValues(Long serviceChargeRuleId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
				String qry = "delete from ServiceChargeRuleValue as sc where sc.serviceChargeRule.serviceChargeRuleId=:serviceChargeRuleId";
				org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("serviceChargeRuleId", serviceChargeRuleId);
				//@End
		/*String qry = "delete from ServiceChargeRuleValue as sc where sc.serviceChargeRule.serviceChargeRuleId="+serviceChargeRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();

	}

	public void deleteScapplicableTimes(Long serviceChargeRuleId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from ScapplicableTime as sc where sc.serviceChargeRule.serviceChargeRuleId=:serviceChargeRuleId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("serviceChargeRuleId", serviceChargeRuleId);
		//@End
		/*String qry = "delete from ScapplicableTime as sc where sc.serviceChargeRule.serviceChargeRuleId="+serviceChargeRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();
	}

	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles cp where cp.bank.bankId=:bankId").setParameter("bankId", bankId);
		//@End
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles cp where cp.bank.bankId='"+bankId+"'");*/
		return query.list();		
	}
	public List<CustomerProfiles> getCustomerProfilesByBankIds(Set<Integer> bankIds){
		String qry="from CustomerProfiles cp where cp.bank.bankId IN  (:bankIds)" 	;
		Query query = getSessionFactory().getCurrentSession().createQuery(qry).setParameterList("bankIds", bankIds);
		return query.list();
	}

	public Integer getBankGroupIdByBankId(Integer bankId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		List list = getHibernateTemplate().find("select b.bankGroup.bankGroupId from Bank b  where b.bankId=?",bankId );
		//@End
		/*List list = getHibernateTemplate().find("select b.bankGroup.bankGroupId from Bank b  where b.bankId='"+bankId+"'");*/		
		return Integer.parseInt(list.get(0)+"" );
	}

	@Override
	public Page searchServiceChargeRules(int ruleLevel, int referenceId, int pageNumber) {

		Session session = getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(ServiceChargeRule.class);
		
		criteria.add(Restrictions.eq("ruleLevel",ruleLevel));
		criteria.add(Restrictions.eq("referenceId",referenceId));	
		
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(ServiceChargeRule.class);
		
		criteria1.add(Restrictions.eq("ruleLevel",ruleLevel));
		criteria1.add(Restrictions.eq("referenceId",referenceId));	
		
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber );

	}

	@Override
	public List<ServiceChargeSplit> getInterBankServiceCharges() {
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit scs where scs.isInter=2");*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit scs where scs.isInter=:isInter").setInteger("isInter", 2);
		return query.list();
	}

	@Override
	public List<ServiceChargeSplit> getInterBankServiceCharges(Integer bankId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit scs where scs.isInter=1 and scs.bank.bankId=:bankId").setParameter("bankId", bankId);*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit scs where scs.isInter=:isInter and scs.bank.bankId=:bankId").setParameter("isInter", 1).setParameter("bankId", bankId);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit scs where scs.isInter=1 and scs.bank.bankId="+bankId);*/
		return query.list();
	}
	
	public List<ServiceChargeRule> getServiceChargeRulesByRuleLevel(Integer ruleLevel,String ruleName){
		 Query query=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeRule where ruleLevel=:ruleLevel and serviceChargeRuleName=:scRuleLevel");
		 query.setParameter("ruleLevel",ruleLevel).setParameter("scRuleLevel", ruleName.replace("'", "''"));
		 return query.list();
	}
	@Override
	public List<ServiceChargeRuleTxn> getServiceChargeRulesByTxnType(Integer transactionType){
		 Query query=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeRuleTxn where transactionType.transactionType=:transactionType").setParameter("transactionType", transactionType);
        return query.list();
	}
	public List<ServiceChargeSplit> getStampFeeFromServiceChargeSplit(Integer bankId) {
		
		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();

		Query query = session.createQuery("from ServiceChargeSplit scs where " +
				"scs.bank.bankId=:bankId and scs.amountType='STAMP_FEE'" )
		.setParameter("bankId",bankId);

		return query.list();
	}

	@Override
	public void deleteSubscriptionChargeRuleValues(Long serviceChargeRuleId) throws EOTException {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from ServiceChargeSubscription as sc where sc.serviceChargeRule.serviceChargeRuleId=:serviceChargeRuleId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("serviceChargeRuleId", serviceChargeRuleId);
		//@End
		/*String qry = "delete from ServiceChargeSubscription as sc where sc.serviceChargeRule.serviceChargeRuleId="+serviceChargeRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();
		
	}

}
