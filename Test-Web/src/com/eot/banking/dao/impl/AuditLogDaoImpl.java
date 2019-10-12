package com.eot.banking.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.AuditLogDao;
import com.eot.banking.dto.AuditLogDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.AuditLog;

public class AuditLogDaoImpl extends BaseDaoImpl implements AuditLogDao{
	
	@Override
	public Page getAuditLogs(AuditLogDTO auditLogDTO,Integer pageNumber) {		
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AuditLog.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		
		if( auditLogDTO.getModule()!= null && ! "".equals(auditLogDTO.getModule()) ){
			criteria.add(Restrictions.eq("entityName", auditLogDTO.getModule()));
		}
		
		if( auditLogDTO.getUserId()!= null && ! "".equals(auditLogDTO.getUserId()) ){
			criteria.add(Restrictions.eq("updatedBy", auditLogDTO.getUserId()));
		}
		String fromDate = null;
		String toDate = null;
		if(auditLogDTO.getFromDate()!=null && auditLogDTO.getToDate()!=null && !auditLogDTO.getToDate().equals("") ){
		fromDate = DateUtil.formatDateWithSlash(auditLogDTO.getFromDate(), 00, 00, 00) ;
		toDate=DateUtil.formatDateWithSlash(auditLogDTO.getToDate(), 23, 59, 59) ;
		
	   // toDate = auditLogDTO.getToDate();
		//toDate=toDate+" 23:59:59";
		}
		/*if( (auditLogDTO.getFromDate()!= null && ! "".equals(auditLogDTO.getFromDate()) ) &&
				(auditLogDTO.getToDate()!= null && ! "".equals(auditLogDTO.getToDate()) ) ){
			@Added by Vinod joshi, Purpuse:-Sql Injection and exception handling 
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(updatedDate,\"%d/%m/%Y\")  between '"+auditLogDTO.getFromDate()+"' " +"and '"+auditLogDTO.getToDate()+"'"));
			DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
			Date fromDate = null;
			Date toDate = null;
			try {
				 fromDate  =df.parse(auditLogDTO.getFromDate());
				 toDate    = df.parse(auditLogDTO.getToDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			criteria.add(Restrictions.between("updatedDate",fromDate, toDate));
			 
		} else if((auditLogDTO.getFromDate()!= null && ! "".equals(auditLogDTO.getFromDate()) ) &&
				(auditLogDTO.getToDate() != null && "".equals(auditLogDTO.getToDate()) ) ){
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(updatedDate,\"%d/%m/%Y\") like ?",auditLogDTO.getFromDate()+"%",Hibernate.DATE));
		
		}  else if((auditLogDTO.getFromDate()!= null &&  "".equals(auditLogDTO.getFromDate()) ) &&
				(auditLogDTO.getToDate() != null && !"".equals(auditLogDTO.getToDate()) ) ){
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(updatedDate,\"%d/%m/%Y\") like ?",auditLogDTO.getToDate()+"%",Hibernate.DATE));
		} */
		
		 if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
		        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\") like ?",fromDate.toUpperCase()+"%",Hibernate.STRING));
		     }
		    if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
		        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\") like ?",toDate.toUpperCase()+"%",Hibernate.STRING));
		     }		
			
	       if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
	            criteria.add(Restrictions.ge("updatedDate",DateUtil.mySQLdateAndTime(fromDate)));
	            criteria.add(Restrictions.lt("updatedDate",DateUtil.mySQLdateAndTime(toDate)));
	       }
	       criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());
		
		
		
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(AuditLog.class);
		
		if (auditLogDTO.getSortBy().equals("asc")) {
			criteria1.addOrder(Order.asc(auditLogDTO.getSortColumn()));
		} else {
			criteria1.addOrder(Order.desc(auditLogDTO.getSortColumn()));
		}
		
		if( auditLogDTO.getModule()!= null && ! "".equals(auditLogDTO.getModule()) ){
			criteria1.add(Restrictions.eq("entityName", auditLogDTO.getModule()));
		}
		
		if( auditLogDTO.getUserId()!= null && ! "".equals(auditLogDTO.getUserId()) ){
			criteria1.add(Restrictions.eq("updatedBy", auditLogDTO.getUserId()));
		}
		
		/*if( (auditLogDTO.getFromDate()!= null && ! "".equals(auditLogDTO.getFromDate()) ) &&
				(auditLogDTO.getToDate()!= null && ! "".equals(auditLogDTO.getToDate()) ) ){
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(updatedDate,\"%d/%m/%Y\")  between '"+auditLogDTO.getFromDate()+"' " +"and '"+auditLogDTO.getToDate()+"'"));
			DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
			Date fromDate = null;
			Date toDate = null;
			try {
				 fromDate  =df.parse(auditLogDTO.getFromDate());
				 toDate    = df.parse(auditLogDTO.getToDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			criteria1.add(Restrictions.between("updatedDate",fromDate, toDate));
		} else if((auditLogDTO.getFromDate()!= null && ! "".equals(auditLogDTO.getFromDate()) ) &&
				(auditLogDTO.getToDate() != null && "".equals(auditLogDTO.getToDate()) ) ){
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(updatedDate,\"%d/%m/%Y\") like '"+auditLogDTO.getFromDate()+"'"));
		
		}  else if((auditLogDTO.getFromDate()!= null &&  "".equals(auditLogDTO.getFromDate()) ) &&
				(auditLogDTO.getToDate() != null && !"".equals(auditLogDTO.getToDate()) ) ){
			criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(updatedDate,\"%d/%m/%Y\") like '"+auditLogDTO.getToDate()+"'"));
		} */
		
		 if(fromDate!=null && ! "".equals( fromDate) && toDate!=null && ! "".equals( toDate)){
	    	  /*criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(login_time,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +
	      		"and '"+toDate.toUpperCase()+"'"));*/
	    	  criteria1.add(Restrictions.ge("updatedDate",DateUtil.mySQLdateAndTime(fromDate)));
	          criteria1.add(Restrictions.lt("updatedDate",DateUtil.mySQLdateAndTime(toDate)));
	      }
		
		
		  criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		  criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
		
		
	}
	
}


