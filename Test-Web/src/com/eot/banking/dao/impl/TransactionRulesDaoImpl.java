package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.TransactionRulesDao;
import com.eot.banking.dto.TxnRuleDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Bank;
import com.eot.entity.TransactionRule;
import com.eot.entity.TransactionRuleValues;
import com.eot.entity.TransactionType;

public class TransactionRulesDaoImpl extends BaseDaoImpl implements TransactionRulesDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionType> getTransactionTypesWithTxnRules(String locale) {
		
		Query query = getSessionFactory().getCurrentSession()
				//@Start, Optimized query, by Murari, dated : 23-07-2018
				
				
				/*.createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale and tr.hasTxnRules!='0' and tr.description!='Encash' order by lower(tr.description)" )
				.setParameter("locale", locale)*/
				.createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale and tr.hasTxnRules!=:hasTxnRules and tr.description!='Encash' order by lower(tr.description)" )
				.setParameter("locale", locale)
				.setParameter("hasTxnRules", 0)
				
				
				//@End
				/*.createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale='"+locale+"'  and tr.hasTxnRules!='0' and tr.description!='Encash' order by lower(tr.description)" )*/
				/*.setCacheable(true)*/;
		return query.list();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionType> getTransactionTypesWithServiceCharge(String locale) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale  and tr.hasServiceCharges!='0' and tr.description!='Encash' order by lower(tr.description)")
				.setParameter("locale", locale);*/
		Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale  and tr.hasServiceCharges!=:hasServiceCharges and tr.description!='Encash' order by lower(tr.description)")
				.setParameter("locale", locale).setParameter("hasServiceCharges", 0);
		
		
		//@End hasServiceCharges
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale='"+locale+"'  and tr.hasServiceCharges!='0' and tr.description!='Encash' order by lower(tr.description)");*/
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionRule> getTransactionRulesByRuleLevel(Integer ruleLevel) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("from TransactionRule where ruleLevel=:ruleLevel").setParameter("ruleLevel", ruleLevel);
		//@End
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from TransactionRule where ruleLevel='"+ruleLevel+"'");*/
		return query.list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getRevenueSharingBanks() {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("from Bank bank where bank.bankAgreementModel.agreementType=:agreementType")
				.setParameter("agreementType", EOTConstants.AGREEMENT_TYPE_ONE_TIME_PAYMENT);
		return query.list();
	}

	@Override
	public TransactionRule getTransactionRule(Long transactionRuleId){
		return getHibernateTemplate().get(TransactionRule.class, transactionRuleId);
	}

	@Override
	public Page searchTransactionRules( int ruleLevel, int referenceId, int pageNumber ) {

		Session session = getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(TransactionRule.class);
		
		criteria.add(Restrictions.eq("ruleLevel",ruleLevel));
		criteria.add(Restrictions.eq("referenceId",referenceId));	
		
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(TransactionRule.class);
		
		criteria1.add(Restrictions.eq("ruleLevel",ruleLevel));
		criteria1.add(Restrictions.eq("referenceId",referenceId));	
		/*
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber );


	}
	@Override
	public Page searchTransactionRulesWithProfile( int ruleLevel, int referenceId, int pageNumber ) {

		Session session = getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(TransactionRule.class);
		if(ruleLevel != 0)
		criteria.add(Restrictions.eq("ruleLevel",ruleLevel));
		/* criteria.add(Restrictions.eq("referenceId",referenceId)); */
		
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(TransactionRule.class);
		criteria1.addOrder(Order.desc("transactionRuleId"));
		if(ruleLevel != 0)
		criteria1.add(Restrictions.eq("ruleLevel",ruleLevel));
		/* criteria1.add(Restrictions.eq("referenceId",referenceId)); */
		/*
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber );


	}

	@SuppressWarnings("unchecked")
	@Override
	public TransactionRule getTransactionRule(Integer transactionType,Integer sourceType) { // Global	
		Query query = getSessionFactory().getCurrentSession()
				
				/*.createQuery("select tr.transactionRule from TransactionRuleTxn tr where tr.transactionType.transactionType=:transactionType and tr.sourceType=:sourceType and tr.transactionRule.ruleLevel=1")
				.setParameter("transactionType", transactionType)
				.setParameter("sourceType", sourceType);*/
				
				.createQuery("select tr.transactionRule from TransactionRuleTxn tr where tr.transactionType.transactionType=:transactionType and tr.sourceType=:sourceType and tr.transactionRule.ruleLevel=:ruleLevel")
				.setParameter("transactionType", transactionType)
				.setParameter("sourceType", sourceType)
				.setParameter("ruleLevel", 1);
				
		List<TransactionRule> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@SuppressWarnings("unchecked")
	public TransactionRule getTransactionRule(Integer transactionType,Integer sourceType, Integer bankId) { // Bank
		Query query = getSessionFactory().getCurrentSession()
				
				/*.createQuery("select tr.transactionRule from TransactionRuleTxn tr where tr.transactionType.transactionType=:transactionType and tr.sourceType=:sourceType and tr.transactionRule.ruleLevel=2 and tr.transactionRule.bank.bankId=:bankId")
				.setParameter("transactionType", transactionType)
				.setParameter("sourceType", sourceType)
				.setParameter("bankId", bankId);*/
				
				.createQuery("select tr.transactionRule from TransactionRuleTxn tr where tr.transactionType.transactionType=:transactionType and tr.sourceType=:sourceType and tr.transactionRule.ruleLevel=:ruleLevel and tr.transactionRule.bank.bankId=:bankId")
				.setParameter("transactionType", transactionType)
				.setParameter("sourceType", sourceType)
				.setParameter("ruleLevel", 2)
				.setParameter("bankId", bankId);
				
		List<TransactionRule> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@SuppressWarnings("unchecked")
	public TransactionRule getTransactionRule(Integer transactionType,Integer sourceType, String accountNumber) { // Customer
		Query query = getSessionFactory().getCurrentSession()
				
				/*.createQuery("select tr.transactionRule from TransactionRuleTxn tr where tr.transactionType.transactionType=:transactionType and tr.sourceType=:sourceType and tr.ruleLevel=3 and tr.account.accountNumber=:accountNumber")
				.setParameter("transactionType", transactionType)
				.setParameter("sourceType", sourceType)
				.setParameter("accountNumber", accountNumber);*/
				
				.createQuery("select tr.transactionRule from TransactionRuleTxn tr where tr.transactionType.transactionType=:transactionType and tr.sourceType=:sourceType and tr.ruleLevel=:ruleLevel and tr.account.accountNumber=:accountNumber")
				.setParameter("transactionType", transactionType)
				.setParameter("sourceType", sourceType)
				.setParameter("ruleLevel", 3)
				.setParameter("accountNumber", accountNumber);
				
		List<TransactionRule> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public void deleteTransactionRuleTxns(Long transactionRuleId)throws EOTException {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from TransactionRuleTxn as tr where tr.transactionRule.transactionRuleId=:transactionRuleId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("transactionRuleId", transactionRuleId);
		//@End
		/*String qry = "delete from TransactionRuleTxn as tr where tr.transactionRule.transactionRuleId="+transactionRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();

	}

	@Override
	public void deleteTransactionRuleValues(Long transactionRuleId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from TransactionRuleValues as tr where tr.transactionRule.transactionRuleId=:transactionRuleId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("transactionRuleId", transactionRuleId);
		//@End
		/*String qry = "delete from TransactionRuleValues as tr where tr.transactionRule.transactionRuleId="+transactionRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();

	}
	
	
	@Override
	public TxnRuleDTO getTransactionRules(Integer ruleLevel,Integer sourceType,Integer transactionType,Integer ruleType){
		
		Session session = getHibernateTemplate(). getSessionFactory().getCurrentSession();
		
		Query query = session.createSQLQuery(
				"select distinct(tr.transactionRuleId),tr.MaxValueLimit from TransactionRules tr " +
				"	left join TransactionRuleTxn trtxn on tr.TransactionRuleID=trtxn.TransactionRuleID " +
				"	left join TransactionRuleValues trv on tr.TransactionRuleID=trv.TransactionRuleID " +
				"	where trtxn.TransactionType="+transactionType+" and trtxn.sourceType="+ sourceType +" and tr.ruleLevel="+ruleLevel +
				" 	and tr.ruleType="+ruleType
		)
		.addScalar("TransactionRuleId",Hibernate.LONG)
		.addScalar("MaxValueLimit",Hibernate.LONG);
		
		List<Object[]> list = query.list();
		
		TxnRuleDTO txnRuledto = null;
		
		if(list.size()>0){
		
		 Object[] obj =(Object[]) query.list().get(0);
		 txnRuledto = new  TxnRuleDTO();
		 txnRuledto.setTransactionRuleID((Long)obj[0]);
		 txnRuledto.setMaxValueLimit((Long)obj[1]);
		 
		}
		 
		return txnRuledto;
	
	}
	
	public TransactionRuleValues getTransactionRuleValues(Long transactionRuleId,Integer allowedPerUnit){
	
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from TransactionRuleValues trv " +
			"where trv.transactionRule.transactionRuleId=:transactionRuleId and trv.allowedPerUnit=:allowedPerUnit")
			.setParameter("transactionRuleId", transactionRuleId)
			.setParameter("allowedPerUnit", allowedPerUnit);	
		List<TransactionRuleValues> list = query.list();
	
		return list.size()>0 ? list.get(0) : null ;
	
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionType> loadFilteredTransactionTypesWithServiceCharge(String locale) {
		/**
		 * Bidyut: change done on query for locate
		 */
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale and tr.transactionType in("+EOTConstants.TXN_ID_DEPOSIT+","+EOTConstants.TXN_ID_WITHDRAWAL+","+EOTConstants.TXN_ID_BILLPAYMENT+","+EOTConstants.TXN_ID_AGETNT_DEPOSIT+","+EOTConstants.TXN_ID_AGENT_WITHDRAWAL+")  and tr.hasServiceCharges!='0' order by lower(tr.description)").setParameter("locale", locale);*/
		Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale and tr.transactionType in("+EOTConstants.TXN_ID_DEPOSIT+","+EOTConstants.TXN_ID_WITHDRAWAL+","+EOTConstants.TXN_ID_BILLPAYMENT+","+EOTConstants.TXN_ID_AGETNT_DEPOSIT+","+EOTConstants.TXN_ID_AGENT_WITHDRAWAL+")  and tr.hasServiceCharges!=:hasServiceCharges order by lower(tr.description)").setParameter("locale", locale).setParameter("hasServiceCharges", 0);
		return query.list();
	}

	@Override
	public TransactionType getTransactionTypeById(Integer txnTypeID) {
		return getHibernateTemplate().get(TransactionType.class,txnTypeID);
	}
}
