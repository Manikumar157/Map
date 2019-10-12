package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.AccessLogDao;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.PageLog;
import com.eot.entity.SessionLog;
import com.eot.entity.WebUser;


public class AccessLogDaoImpl extends BaseDaoImpl implements AccessLogDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<WebUser> getWebUserList() {		
		Query query = getSessionFactory().getCurrentSession().createQuery("select wb.userName from WebUser wb" );
		return query.list();
	}	
	@Override
	public Page sessionLog(int pageNumber) {		

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SessionLog.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SessionLog.class);
		criteria1.addOrder(Order.desc("loginTime"));
//		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
//		criteria1.setMaxResults(appConfig.getResultsPerPage());
		List<SessionLog> sessionlog = criteria1.list();

		return PaginationHelper.getPage(sessionlog, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public Page getVisitedPage(String sessionId,Integer pageNumber) {	
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(PageLog.class);
		criteria.createAlias("sessionLog","sessionLog");
		criteria.add(Restrictions.eq("sessionLog.sessionId", sessionId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(PageLog.class);
		criteria1.createAlias("sessionLog","sessionLog");
		criteria1.add(Restrictions.eq("sessionLog.sessionId", sessionId));	
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<PageLog> pageLog = criteria1.list();

		return PaginationHelper.getPage(pageLog, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page searchLog(String userId,String fromDate,String toDate,Integer pageNumber) {
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();	
		Criteria criteria=session.createCriteria(SessionLog.class);		
		
		if(userId!=null && ! "".equals(userId)){			
			criteria.add(Restrictions.sqlRestriction("user_name like ?",userId+"%",Hibernate.STRING));
		}
		String fDate = null;
		String tDate = null;
		if(fromDate!=null && toDate!=null){
			fDate = DateUtil.formatDateWithSlash(fromDate, 00, 00, 00) ;
			tDate=DateUtil.formatDateWithSlash(toDate, 23, 59, 59) ;
		}
	    if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
	        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\") like ?",fromDate.toUpperCase()+"%",Hibernate.STRING));
	     }
	    if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
	        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\") like ?",toDate.toUpperCase()+"%",Hibernate.STRING));
	     }		
		
       if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
            criteria.add(Restrictions.ge("loginTime",DateUtil.mySQLdateAndTime(fDate)));
            criteria.add(Restrictions.lt("loginTime",DateUtil.mySQLdateAndTime(tDate)));
       }
       criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
       int totalCount = Integer.parseInt(criteria.list().get(0).toString());
       
       Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SessionLog.class);
      
		if(userId!=null && ! "".equals(userId)){			
			criteria1.add(Restrictions.sqlRestriction("user_name like ?",userId+"%",Hibernate.STRING));		
		}
		/* if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
		        criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'"));
		     }
		    if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
		        criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\") like '"+toDate.toUpperCase()+"'"));
		     }	*/		
		
      if(fromDate!=null && ! "".equals( fromDate) && toDate!=null && ! "".equals( toDate)){
    	  /*criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +
      		"and '"+toDate.toUpperCase()+"'"));*/
    	  criteria1.add(Restrictions.ge("loginTime",DateUtil.mySQLdateAndTime(fDate)));
          criteria1.add(Restrictions.lt("loginTime",DateUtil.mySQLdateAndTime(tDate)));
      }
       criteria1.addOrder(Order.desc("loginTime"));
       criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
	   criteria1.setMaxResults(appConfig.getResultsPerPage());

	   return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

}
