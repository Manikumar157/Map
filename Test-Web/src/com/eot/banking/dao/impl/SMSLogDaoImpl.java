/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: SMSLogDaoImpl.java
*
* Date Author Changes
* 17 Jun, 2016 Swadhin Created
*/
package com.eot.banking.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.SMSLogDao;
import com.eot.banking.dto.SMSCampaignDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.SmsAlertRule;
import com.eot.entity.SmsLog;

/**
 * The Class SMSLogDaoImpl.
 */
public class SMSLogDaoImpl extends BaseDaoImpl implements SMSLogDao{

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#getSMSLogList(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getSMSLogList(Integer pageNumber){

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SmsLog.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SmsLog.class);
		criteria1.addOrder(Order.desc("scheduledDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#getSMSLogByMobileNo(java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getSMSLogByMobileNo(String mobileNo,Integer pageNumber){

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SmsLog.class);
		criteria.add( Restrictions.eq("mobileNumber",mobileNo) );
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SmsLog.class);
		criteria1.add( Restrictions.eq("mobileNumber",mobileNo) );
		criteria1.addOrder(Order.desc("scheduledDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}	

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#chageSMSLogStatus(java.lang.Integer)
	 */
	public void chageSMSLogStatus(Integer msgId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018

		/*Query query=getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("update SmsLog as sms set sms.status='0' where sms.messageId=:msgId").setParameter("msgId", msgId);*/
		Query query=getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("update SmsLog as sms set sms.status=:status where sms.messageId=:msgId").setParameter("status", 0).setParameter("msgId", msgId);
		
		//@End
		/*Query query=getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("update SmsLog as sms set sms.status='0' where sms.messageId='"+msgId+"'");*/	
		query.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#getSmsAlertRulesByRuleName(java.lang.String)
	 */
	@Override
	public List<SmsAlertRule> getSmsAlertRulesByRuleName(String packageName) {
		 Query query=getSessionFactory().getCurrentSession().createQuery("from SmsAlertRule where smsAlertRuleName=:smsAlertRuleName");
		 query.setParameter("smsAlertRuleName", packageName.replace("'", "''"));
         return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#getSmsAlertRulesByRuleId(java.lang.Long)
	 */
	@Override
	public SmsAlertRule getSmsAlertRulesByRuleId(Long smsAlertRuleId) {
		return getHibernateTemplate().get(SmsAlertRule.class,smsAlertRuleId);	
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#deleteSmsAlertValue(java.lang.Long)
	 */
	@Override
	public void deleteSmsAlertValue(Long smsAlertRuleId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from SmsAlertRuleValue as sarv where sarv.smsalertrule.smsAlertRuleId=:smsAlertRuleId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("smsAlertRuleId", smsAlertRuleId);
		//@End
		/*String qry = "delete from SmsAlertRuleValue as sarv where sarv.smsalertrule.smsAlertRuleId="+smsAlertRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#deleteSmsAlertRulesTxn(java.lang.Long)
	 */
	@Override
	public void deleteSmsAlertRulesTxn(Long smsAlertRuleId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from SmsAlertRulesTxn as sart where sart.smsAlertRule.smsAlertRuleId=:smsAlertRuleId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("smsAlertRuleId", smsAlertRuleId);
		//@End
		/*String qry = "delete from SmsAlertRulesTxn as sart where sart.smsAlertRule.smsAlertRuleId="+smsAlertRuleId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.SMSLogDao#searchSmsAlertRules(java.lang.Integer, int, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page searchSmsAlertRules(Integer ruleLevel, int referenceId, Integer subscriptionType, Integer pageNumber) {
		
		List<SmsAlertRule> smsAlertRules = null;
		int totalCount = 0;
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(SmsAlertRule.class,"smr");
			
			criteria.add(Restrictions.eq("ruleLevel",ruleLevel));
			criteria.add(Restrictions.eq("referenceId",referenceId));
			criteria.setFetchMode("smsalertrulevalues", FetchMode.JOIN);
			criteria.createAlias("smr.smsalertrulevalues","smsalertrulevalues",CriteriaSpecification.LEFT_JOIN);
			if(subscriptionType != null){
				criteria.add(Restrictions.eq("smsalertrulevalues.subscriptionType",subscriptionType));	
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			smsAlertRules = (List<SmsAlertRule>) getHibernateTemplate().findByCriteria(criteria);
			criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
			totalCount = smsAlertRules.size();

			DetachedCriteria criteria1 = DetachedCriteria.forClass(SmsAlertRule.class,"smr");
			
			criteria1.add(Restrictions.eq("ruleLevel",ruleLevel));
			criteria1.add(Restrictions.eq("referenceId",referenceId));
			criteria1.setFetchMode("smsalertrulevalues", FetchMode.JOIN);
			criteria1.createAlias("smr.smsalertrulevalues","smsalertrulevalues",CriteriaSpecification.LEFT_JOIN);
			if(subscriptionType != null){
				criteria1.add(Restrictions.eq("smsalertrulevalues.subscriptionType",subscriptionType));	
			}
			criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			smsAlertRules = (List<SmsAlertRule>) getHibernateTemplate().findByCriteria(criteria1);
			criteria1.getExecutableCriteria(getSessionFactory().getCurrentSession()).setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
			criteria1.getExecutableCriteria(getSessionFactory().getCurrentSession()).setMaxResults(appConfig.getResultsPerPage());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return PaginationHelper.getPage( smsAlertRules, totalCount, appConfig.getResultsPerPage(), pageNumber );
	}

	@Override
	public List getTransactionTypesWithSmsAlert(String locale) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale  and tr.hasSmsAlert!='0' order by lower(tr.description)").setParameter("locale", locale);*/
		Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale=:locale  and tr.hasSmsAlert!=:hasSmsAlert order by lower(tr.description)").setParameter("locale", locale).setParameter("hasSmsAlert", 0);
		
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select tr from TransactionType as tr ,TransactionTypesDesc as td where tr.transactionType=td.comp_id.transactionType and td.comp_id.locale='"+locale+"'  and tr.hasSmsAlert!='0' order by lower(tr.description)");*/
		//@End
		return query.list();
	}
	
	
	@Override
	public Page getSMSLogDetails(String mobileNo,String fromDate,String toDate,Integer pageNumber){

		Date dateFrom=null; 
		Date dateTo=null;
		try {
		dateFrom=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(toDate));
		c.add(Calendar.DAY_OF_MONTH, 1);  
		String newDate = sdf.format(c.getTime());
		dateTo=new SimpleDateFormat("yyyy-MM-dd").parse(newDate);
		}catch (Exception e) {
			// TODO: handle exception
		}
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SmsLog.class);
		if(mobileNo!=null && mobileNo!="")
		criteria.add( Restrictions.eq("mobileNumber","211"+mobileNo) );
		
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria.add(Restrictions.between("createdDate", dateFrom,dateTo));
		criteria.add(Restrictions.in("messageType", new Integer[]{3,4,17}));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SmsLog.class);
		if(mobileNo!=null && mobileNo!="")
			criteria1.add( Restrictions.eq("mobileNumber","211"+mobileNo) );
			
		criteria1.addOrder(Order.desc("createdDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
//		criteria1.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria1.add(Restrictions.between("createdDate", dateFrom, dateTo));
		criteria1.add(Restrictions.in("messageType", new Integer[]{3,4,17,6}));
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public List<String> getMobileNumbersSmsCampaign(String target) {
		
		String queryString = "";
		switch(target){		
			case EOTConstants.TARGET_TYPE_ALL :	queryString = "SELECT mobileNumber AS Mobile FROM Customer "
															  +	"UNION ALL "
															  +	"SELECT mobileNumber AS Mobile FROM WebUser wu where wu.webUserRole.roleId=25";
												break;
				
			case EOTConstants.TARGET_TYPE_PRINCIPAL_AGENT : queryString = "SELECT mobileNumber AS Mobile FROM WebUser wu where wu.webUserRole.roleId=24";
															break;
				
			case EOTConstants.TARGET_TYPE_SUPER_AGENT : queryString = "SELECT mobileNumber AS Mobile FROM WebUser wu where wu.webUserRole.roleId=25";
														break;
				
			case EOTConstants.TARGET_TYPE_MERCHANT : queryString = "SELECT mobileNumber FROM Customer where type=2";
													 break;
			case EOTConstants.TARGET_TYPE_AGENT : queryString = "SELECT mobileNumber AS Mobile FROM Customer where type=1";
												  break;
				
			case EOTConstants.TARGET_TYPE_CUSTOMER : queryString = "SELECT mobileNumber AS Mobile FROM Customer where type=0";
													 break;				
			default :break;
		}
		Query query=getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(queryString.toString());
		List<String> list = query.list();
		return CollectionUtils.isNotEmpty(list)?list:null;
	}

	@Override
	public Page getSmsCampaignData(SMSCampaignDTO smsCampaignDTO, Integer pageNumber) {
		StringBuilder queryStr = new StringBuilder("select * from SmsCampaign ORDER BY CreatedDate DESC");
		SQLQuery queryResult = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryStr.toString());
		queryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(queryResult.list(), queryResult.list().size(), appConfig.getResultsPerPage(), pageNumber);		
	}	

}
