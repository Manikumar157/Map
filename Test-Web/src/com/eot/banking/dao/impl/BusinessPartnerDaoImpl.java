/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: BusinessPartnerDaoImpl.java
*
* Date Author Changes
* Oct 16, 2018 Vinod Joshi Created
*/
package com.eot.banking.dao.impl;

//Sql Injection is done by vineeth,on 17-10-2018

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BusinessPartnerDao;
import com.eot.banking.dto.BusinessPartnerDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.ApiCredientials;
import com.eot.entity.ApiLogs;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.KycType;

/**
 * The Class BusinessPartnerDaoImpl.
 */
@SuppressWarnings("unchecked")
@Transactional(readOnly = false)
public class BusinessPartnerDaoImpl extends BaseDaoImpl implements BusinessPartnerDao{

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartner(java.lang.String)
	 */
	@Override
	public BusinessPartner getBusinessPartner(String userName) {
	//	BusinessPartnerUser businessPartnerUser = getHibernateTemplate().get(BusinessPartnerUser.class,userName);
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from BusinessPartnerUser as user where user.userName=:userName")
				.setParameter("userName", userName);
		List<BusinessPartnerUser> list = query.list();
		return list.size()>0 ? list.get(0).getBusinessPartner() : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getAllBusniessPartner(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<BusinessPartner> getAllBusniessPartner(Integer partnerType,Integer seniorPartnerId,Integer bankId) {
		Query query =null;
		/*if(partnerType==EOTConstants.ROLEID_EOT_ADMIN || partnerType==EOTConstants.ROLEID_BANK_ADMIN )
		{
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType");
			query.setParameter("partnerType", 1);
			//query.setParameter("seniorPartner", seniorPartnerId);
		}*/
		
		 if(partnerType==EOTConstants.PARTNER_TYPE_BA_ADMIN || partnerType==EOTConstants.ROLEID_BANK_ADMIN )
		{
			
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType");
			query.setParameter("partnerType", 1);
			//query.setParameter("seniorPartner", seniorPartnerId);
			if(bankId != null && bankId.toString() != "") {
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and businesspart.bank.bankId=:bankId");
			query.setParameter("partnerType", 1);
			query.setParameter("bankId", bankId);
			}
		}
		else if(partnerType==EOTConstants.PARTNER_TYPE_BA_PARTNER_L1)
		{
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and  businesspart.seniorPartner=:seniorPartner");
			query.setParameter("partnerType", 2);
			query.setParameter("seniorPartner", seniorPartnerId);
			if(bankId != null && bankId.toString() != "") {
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and  businesspart.seniorPartner=:seniorPartner and businesspart.bank.bankId=:bankId");
			query.setParameter("partnerType", 2);
			query.setParameter("seniorPartner", seniorPartnerId);
			query.setParameter("bankId", bankId);
			}
			
		}
		else if(partnerType==EOTConstants.PARTNER_TYPE_BA_PARTNER_L2)
		{
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and  businesspart.seniorPartner=:seniorPartner");
			query.setParameter("partnerType", 3);
			query.setParameter("seniorPartner", seniorPartnerId);
			if(bankId != null && bankId.toString() != "") {
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and  businesspart.seniorPartner=:seniorPartner and businesspart.bank.bankId=:bankId");
			query.setParameter("partnerType", 3);
			query.setParameter("seniorPartner", seniorPartnerId);
			query.setParameter("bankId", bankId);
			}
		}
		else if(partnerType==EOTConstants.PARTNER_TYPE_BA_PARTNER_L3)
		{
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and  businesspart.seniorPartner=:seniorPartner");
			query.setParameter("partnerType", 4);
			query.setParameter("seniorPartner", seniorPartnerId);
			if(bankId != null && bankId.toString() != "") {
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and  businesspart.seniorPartner=:seniorPartner and businesspart.bank.bankId=:bankId");
			query.setParameter("partnerType", 4);
			query.setParameter("seniorPartner", seniorPartnerId);
			query.setParameter("bankId", bankId);
			}
		}
			 
		List<BusinessPartner> list = query.list();
		return list.size()>0 ? list : null ;
		
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartnerbyId(java.lang.String)
	 */
	@Override
	@Transactional
	public BusinessPartner getBusinessPartnerbyId(String id) {
			int baid = Integer.parseInt(id);

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from BusinessPartner as businesspart where businesspart.id=:id")
				.setParameter("id", baid);
		List<BusinessPartner> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#searchBusinessPartner(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Page searchBusinessPartner(String name, String contactPerson, String contactNumber, String code, Integer pageNumber, String fromDate, String toDate, String partnerType, String seniorPartner, Integer bankId1) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(BusinessPartner.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria.addOrder(Order.desc("createdDate"));
			Date date1=null,date2=null;
			String bankId = "";
			
		 bankId = bankId1 !=null ? bankId1.toString() : null ;
			if(fromDate!= null && toDate!= null && fromDate!= "" && toDate!= ""){ 
		try {
			date1=new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			 c.setTime(sdf.parse(toDate));
			 c.add(Calendar.DAY_OF_MONTH, 1);  
				String newDate = sdf.format(c.getTime());
				date2=new SimpleDateFormat("dd/MM/yyyy").parse(newDate);
				
		} catch (ParseException e) {
			e.printStackTrace();
		}}
		
			if(null != name && ! "".equals(name)) {
			criteria.add(Restrictions.like("name","%"+name+"%"));
			//	criteria.add(Restrictions.eq("name",name));
		}
		if(null != contactPerson && ! "".equals(contactPerson)) {
			criteria.add(Restrictions.like("contactPerson","%"+contactPerson+"%"));
		}
		if(null != code && ! "".equals(code)) {
			criteria.add(Restrictions.like("code","%"+code+"%"));
		}
		if( contactNumber!= null && ! "".equals(contactNumber) ){
			criteria.add(Restrictions.like("contactPersonPhone","%"+contactNumber+"%"));
		}
		if( bankId1!= null && ! "".equals(bankId1)){
	//		criteria.add(Restrictions.like("bank.bankId","%"+bankId1+"%"));
			criteria.add(Restrictions.eq("bank.bankId",bankId1));
		}

		if( partnerType!= null && ! "".equals(partnerType) ){
			criteria.add(Restrictions.eq("partnerType",Integer.parseInt(partnerType)));
		}
		if( seniorPartner!= null && ! "".equals(seniorPartner) ){
			criteria.add(Restrictions.eq("seniorPartner",Integer.parseInt(seniorPartner)));
		}
		 if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
		        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like ?",fromDate.toUpperCase()+"%",Hibernate.DATE));
		     }
		    if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
		        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like ?",toDate.toUpperCase()+"%",Hibernate.DATE));
		     }		
			
	       /*if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
	            criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'"));
	       }*/
		    if(fromDate!=null && ! "".equals( fromDate) && toDate!=null && ! "".equals( toDate)){
				criteria.add(Restrictions.between("createdDate", date1, date2));   		 	  
		   }

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(BusinessPartner.class);
		criteria1.addOrder(Order.desc("createdDate"));
		
		if(null != name && ! "".equals(name)) {
			criteria1.add(Restrictions.like("name","%"+name+"%"));
		//	criteria.add(Restrictions.eq("name",name));
		}
		if(null != contactPerson && ! "".equals(contactPerson)) {
			criteria1.add(Restrictions.like("contactPerson","%"+contactPerson+"%"));
		}
		if(null != code && ! "".equals(code)) {
			criteria1.add(Restrictions.like("code","%"+code+"%"));
		}
		if( contactNumber!= null && ! "".equals(contactNumber) ){
			criteria1.add(Restrictions.like("contactPersonPhone", "%"+contactNumber+"%"));
		}
		if( bankId!= null && ! "".equals(bankId)  ){
		//	criteria1.add(Restrictions.like("bank.bankId","%"+bankId+"%"));
			criteria1.add(Restrictions.eq("bank.bankId",bankId1));
		}
		/*if(null != partnerType) {
			criteria1.add(Restrictions.like("partnerType","%"+partnerType+"%"));
		}*/
		if( partnerType!= null && ! "".equals(partnerType) ){
			criteria1.add(Restrictions.eq("partnerType",Integer.parseInt(partnerType)));
		}
		if( seniorPartner!= null && ! "".equals(seniorPartner) ){
			criteria1.add(Restrictions.eq("seniorPartner",Integer.parseInt(seniorPartner)));
		}
		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
		    criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like ?",fromDate.toUpperCase()+"%",Hibernate.DATE));
		 }
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
		    criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like ?",toDate.toUpperCase()+"%",Hibernate.DATE));
		}			
		
		if(fromDate!=null && ! "".equals( fromDate) && toDate!=null && ! "".equals( toDate)){
			criteria1.add(Restrictions.between("createdDate", date1, date2));   
	 	
	 	  
	   }
			criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//	criteria1.setMaxResults(appConfig.getResultsPerPage());
	   
			return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

   }
	
	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getUser(java.lang.String)
	 */
	@Override
	public BusinessPartnerUser getUser(String name) {
		return getHibernateTemplate().get(BusinessPartnerUser.class,name);
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartner(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BusinessPartner getBusinessPartner(Long id) {				
		Query query=getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businessPartner where businessPartner.id=:id")
		                               .setParameter("id",Integer.parseInt(id.toString()));
		List<BusinessPartner> list=query.list();
		return  list.size()>0 ? list.get(0) : null ; 
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartnerByMobileNumber(java.lang.String)
	 */
	@Override
	public BusinessPartner getBusinessPartnerByMobileNumber(String contactNumber) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where concat(bp.country.isdCode,contactNumber)=:contactNumber")
                .setParameter("contactNumber",contactNumber);
		List<BusinessPartner> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartnerByContactNumber(java.lang.String)
	 */
	@Override
	public BusinessPartner getBusinessPartnerByContactNumber(String contactNumber) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where contactNumber=:contactNumber")
                .setParameter("contactNumber",contactNumber);
		List<BusinessPartner> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartnerByEmailAddress(java.lang.String)
	 */
	@Override
	public BusinessPartner getBusinessPartnerByEmailAddress(String emailId) {
	Query query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where emailId=:emailId")
                .setParameter("emailId",emailId);
		List<BusinessPartner> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getBusinessPartnerByCode(java.lang.String)
	 */
	@Override
	public BusinessPartner getBusinessPartnerByCode(String code) {
		
		Query query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where code=:code")
                .setParameter("code",code);
		List<BusinessPartner> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getKycList()
	 */
	@Override
	public List<KycType> getKycList() {
		Query query=getSessionFactory().getCurrentSession().createQuery("from KycType");
		List<KycType> list=query.list();
		return list.size()>0 ? list :null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.BusinessPartnerDao#getkycbyId(int)
	 */
	@Override
	public KycType getkycbyId(int kycid) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from KycType kt where kt.kycTypeId=:kycTypeId")
                .setParameter("kycTypeId",kycid);
		List<KycType> list=query.list();
		return list.size()>0 ? list.get(0) :null;
		
	}

	@Override
	public Page getAllBusniessPartner(int pageNumber) {

		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(BusinessPartner.class);
		List<BusinessPartner> list = criteria.list();
		return  PaginationHelper.getPage(list, CollectionUtils.isNotEmpty(list)? criteria.list().size() : 0, appConfig.getResultsPerPage(), pageNumber);
		
	}
	@Override
	public List<BusinessPartnerDTO> getAllBusniessPartnerUser() {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(BusinessPartnerUser.class);
		List<BusinessPartnerUser> list = criteria.list();
		
		List<BusinessPartnerDTO> businessPartnerDtoList= new ArrayList<>();
		BusinessPartnerDTO businessPartner =null;
		
		for (BusinessPartnerUser bpu : list) {
			businessPartner= new BusinessPartnerDTO();
			businessPartner.setBusinessPartnerUserName(bpu.getBusinessPartner().getName());
			businessPartner.setWebUserName(bpu.getUserName());
			businessPartnerDtoList.add(businessPartner);
		}
		return  businessPartnerDtoList;
	}

	@Override
	public BusinessPartner getBusinessPartnerByName(String name) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where name=:name")
                .setParameter("name",name);
		List<BusinessPartner> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public BusinessPartner getBusinessPartnerDetails(String partnerType, Integer bankId, String seniorPartner,String businessPartnerCode) {
		Query query=null;
		if(null !=seniorPartner) {
		 query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where partnerType=:partnerType and bp.bank.bankId=:bankId and seniorPartner=:seniorPartner and code=:businessPartnerCode")
                .setParameter("partnerType",Integer.parseInt(partnerType))
				.setParameter("bankId",bankId)
				.setParameter("seniorPartner",Integer.parseInt(seniorPartner))
				.setParameter("businessPartnerCode",businessPartnerCode);		
		}else {
			 query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner bp where partnerType=:partnerType and bp.bank.bankId=:bankId and code=:businessPartnerCode")
	                .setParameter("partnerType",Integer.parseInt(partnerType))
					.setParameter("bankId",bankId)
					.setParameter("businessPartnerCode",businessPartnerCode);
		}
		List<BusinessPartner> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public BusinessPartnerUser getBusinessPartnerUserByName(String userName) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from BusinessPartnerUser as user where user.userName=:userName")
				.setParameter("userName", userName);
		List<BusinessPartnerUser> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public List<BusinessPartner> getAllBulkBusinessPartner(Integer partnerType,Integer bankId) {
		Query query =null;
		query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType");
			query.setParameter("partnerType", 4);
			//query.setParameter("seniorPartner", seniorPartnerId);
			if(bankId != null && bankId.toString() != "") {
			query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businesspart where businesspart.partnerType=:partnerType and businesspart.bank.bankId=:bankId");
			query.setParameter("partnerType", 4);
			query.setParameter("bankId", bankId);
			}
		
		 List<BusinessPartner> list = query.list();
			return list.size()>0 ? list : null ;
	}
	@Override
	public ApiCredientials varifyApiRequest(String username,String password)
	{
		Query query =getSessionFactory().getCurrentSession().createQuery("from ApiCredientials as cred where cred.username=:username and  cred.password=:password");
			query.setParameter("username", username);
			query.setParameter("password", password);
			List<ApiCredientials>list=query.list();
			if(list.isEmpty())
				return null;
			else
				return list.get(0);
	}
	@Override
	public ApiLogs fetchApiLogs(String referenceId)
	{
		Query query =getSessionFactory().getCurrentSession().createQuery("from ApiLogs as apiLogs where apiLogs.bankReference=:bankReference");
			query.setParameter("bankReference", referenceId);
			List<ApiLogs>list=query.list();
			if(list.isEmpty())
				return null;
			else
				return list.get(0);
	}
	
	@Override
	public List<Object[]> lowBlanceCheckForBusinessPartner(){
		StringBuilder queryBuilder = new StringBuilder("SELECT bp.ContactPersonPhone AS MobileNumber, bp.Id AS UserId, bp.PartnerType AS PartnerType, bp.BusinessEntityLimit AS AuthorizedLimit, acc.CurrentBalance AS CurrentBalance, "
				+ "ROUND(( bp.BusinessEntityLimit*25/100 ),0) AS RecomandedBalance, (acc.CurrentBalance < ROUND(( bp.BusinessEntityLimit*25/100 ),0)) AS IsBalanceShortage "
				+ "FROM BusinessPartner bp "
				+ "INNER JOIN Account acc ON acc.ReferenceID = bp.Id AND acc.Alias=:accAlias AND bp.PartnerType IN(:BPL1,:BPL2)");
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryBuilder.toString())
				.addScalar("MobileNumber", Hibernate.STRING).addScalar("UserId",Hibernate.BIG_INTEGER).addScalar("PartnerType", Hibernate.INTEGER).addScalar("AuthorizedLimit",Hibernate.DOUBLE)
				.addScalar("CurrentBalance", Hibernate.DOUBLE).addScalar("RecomandedBalance",Hibernate.DOUBLE).addScalar("IsBalanceShortage", Hibernate.INTEGER);
		query.setParameter("BPL1", 1).setParameter("BPL2", 2).setParameter("accAlias", "BUSINESS_PARTNER_ACC");
		
		return CollectionUtils.isNotEmpty(query.list())? query.list():null;
	}
}
	
	


