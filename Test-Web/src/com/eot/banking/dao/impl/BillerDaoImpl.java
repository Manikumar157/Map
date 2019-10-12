package com.eot.banking.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.BillerDao;
import com.eot.banking.dto.BillerDTO;
import com.eot.banking.dto.SenelecBillSearchDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Biller;
import com.eot.entity.BillerTypes;
import com.eot.entity.Country;
import com.eot.entity.SenelecBills;
import com.eot.entity.SenelecControlFile;

public class BillerDaoImpl extends  BaseDaoImpl implements BillerDao {

	@Override
	public List<BillerTypes> getBillerTypes() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from BillerTypes bt" ).setCacheable(true);
		return query.list();

	}

	public Page getBillersList(BillerDTO billerDTO,Integer pageNumber,Integer bankId) {
		
		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(Biller.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( billerDTO.getBillerName()!= null && ! "".equals(billerDTO.getBillerName()) ){
			criteria.add(Restrictions.sqlRestriction("billerName like ?",billerDTO.getBillerName()+"%",Hibernate.STRING));
		}
		if( billerDTO.getBillerTypeId()!= null && ! "".equals(billerDTO.getBillerTypeId()) ){
			criteria.add(Restrictions.eq("billerType.billerTypeId",billerDTO.getBillerTypeId()));
		}
		/*if( billerDTO.getBankId() != null && ! "".equals(billerDTO.getBankId()) ){
			criteria.add(Restrictions.eq("bank.bankId", billerDTO.getBankId()));
		}*/
		if( bankId != null && ! "".equals(bankId) ){
			criteria.add(Restrictions.eq("bank.bankId",bankId));
		}
		if( billerDTO.getCountryId()!= null && ! "".equals(billerDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", billerDTO.getCountryId()));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(Biller.class);
		
		if( billerDTO.getBillerName()!= null && ! "".equals(billerDTO.getBillerName()) ){
			criteria1.add(Restrictions.sqlRestriction("billerName like ?",billerDTO.getBillerName()+"%",Hibernate.STRING));
		}
		if( billerDTO.getBillerTypeId()!= null && ! "".equals(billerDTO.getBillerTypeId()) ){
			criteria1.add(Restrictions.eq("billerType.billerTypeId",billerDTO.getBillerTypeId()));
		}
		/*if( billerDTO.getBankId() != null && ! "".equals(billerDTO.getBankId()) ){
			criteria1.add(Restrictions.eq("bank.bankId", billerDTO.getBankId()));
		}*/
		if( bankId != null && ! "".equals(bankId) ){
			criteria1.add(Restrictions.eq("bank.bankId",bankId));
		}
		if( billerDTO.getCountryId()!= null && ! "".equals(billerDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", billerDTO.getCountryId()));
		}

		criteria1.addOrder(Order.asc("billerName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
*/
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
		
	}

	@Override
	public Biller getBillerDeatils(Integer billerId) {

		return getHibernateTemplate().get(Biller.class,billerId);

	}

	@Override
	public Biller getBillerNameByName(String billerName, Integer countryId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Biller as biller where biller.billerName=:billerName and biller.country.countryId=:countryId")
				.setParameter("billerName", billerName)
				.setParameter("countryId", countryId);		
		List<Biller> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	public Page getSenegalBills(Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SenelecBills.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SenelecBills.class);
		criteria1.addOrder(Order.asc("billId"));
		//criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	public Page searchSenelecBill(SenelecBillSearchDTO senelecBillSearchDTO,Integer pageNumber){

		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(SenelecBills.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( senelecBillSearchDTO.getPolicyNo()!= null && ! "".equals(senelecBillSearchDTO.getPolicyNo()) ){
			criteria.add(Restrictions.eq("senelecCustomers.policyNumber", senelecBillSearchDTO.getPolicyNo()));
		}
		if( senelecBillSearchDTO.getCustomer()!= null && ! "".equals(senelecBillSearchDTO.getCustomer()) ){
			criteria.add(Restrictions.sqlRestriction("senelecCustomers.customerName like ?",senelecBillSearchDTO.getCustomer()+"%",Hibernate.STRING));
		}
		if( senelecBillSearchDTO.getRecordNo()!= null && ! "".equals(senelecBillSearchDTO.getRecordNo()) ){
			criteria.add(Restrictions.eq("recordNumber", Integer.valueOf(senelecBillSearchDTO.getRecordNo())));
		}
		if( senelecBillSearchDTO.getStatus()!= null && ! "".equals(senelecBillSearchDTO.getStatus()) ){
			criteria.add(Restrictions.eq("status", Integer.valueOf(senelecBillSearchDTO.getStatus())));
		}
		if( senelecBillSearchDTO.getFromDate()!= null && ! "".equals(senelecBillSearchDTO.getFromDate()) ){
			criteria.add(Restrictions.between("paymentDate",DateUtil.stringToDate(senelecBillSearchDTO.getFromDate()),DateUtil.stringToDate(senelecBillSearchDTO.getToDate())));
		}
		if( senelecBillSearchDTO.getFromAmount()!= null && ! "".equals(senelecBillSearchDTO.getFromAmount()) ){
			criteria.add(Restrictions.between("recordAmount",Double.valueOf(senelecBillSearchDTO.getFromAmount()),Double.valueOf(senelecBillSearchDTO.getToAmount())));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(SenelecBills.class);

		if( senelecBillSearchDTO.getPolicyNo()!= null && ! "".equals(senelecBillSearchDTO.getPolicyNo()) ){
			criteria1.add(Restrictions.eq("senelecCustomers.policyNumber", senelecBillSearchDTO.getPolicyNo()));
		}
		if( senelecBillSearchDTO.getCustomer()!= null && ! "".equals(senelecBillSearchDTO.getCustomer()) ){
			criteria1.add(Restrictions.sqlRestriction("senelecCustomers.customerName like ?",senelecBillSearchDTO.getCustomer()+"%",Hibernate.STRING));
		}
		if( senelecBillSearchDTO.getRecordNo()!= null && ! "".equals(senelecBillSearchDTO.getRecordNo()) ){
			criteria1.add(Restrictions.eq("recordNumber", Integer.valueOf(senelecBillSearchDTO.getRecordNo())));
		}
		if( senelecBillSearchDTO.getStatus()!= null && ! "".equals(senelecBillSearchDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("status", Integer.valueOf(senelecBillSearchDTO.getStatus())));
		}
		if( senelecBillSearchDTO.getFromDate()!= null && ! "".equals(senelecBillSearchDTO.getFromDate()) ){
			criteria1.add(Expression.between("paymentDate",DateUtil.stringToDate(senelecBillSearchDTO.getFromDate()),DateUtil.stringToDate(senelecBillSearchDTO.getToDate())));
		}
		if( senelecBillSearchDTO.getFromAmount()!= null && ! "".equals(senelecBillSearchDTO.getFromAmount()) ){
			criteria1.add(Expression.between("recordAmount",Double.valueOf(senelecBillSearchDTO.getFromAmount()),Double.valueOf(senelecBillSearchDTO.getToAmount())));
		}

		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}
	////Author Name:Abu Kalam Azad Date:23/07/2018 purpouses:Bill Payment pdf data should be based on criteria..(5761)
	//start...
	public List<SenelecBillSearchDTO> searchSenelecBillForPdf(SenelecBillSearchDTO senelecBillSearchDTO){
		
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SenelecBills.class);

		if( senelecBillSearchDTO.getPolicyNo()!= null && ! "".equals(senelecBillSearchDTO.getPolicyNo()) ){
			criteria1.add(Restrictions.eq("senelecCustomers.policyNumber", senelecBillSearchDTO.getPolicyNo()));
		}
		if( senelecBillSearchDTO.getCustomer()!= null && ! "".equals(senelecBillSearchDTO.getCustomer()) ){
			criteria1.add(Restrictions.sqlRestriction("senelecCustomers.customerName like '"+senelecBillSearchDTO.getCustomer()+"'"));
		}
		if( senelecBillSearchDTO.getRecordNo()!= null && ! "".equals(senelecBillSearchDTO.getRecordNo()) ){
			criteria1.add(Restrictions.eq("recordNumber", Integer.valueOf(senelecBillSearchDTO.getRecordNo())));
		}
		if( senelecBillSearchDTO.getStatus()!= null && ! "".equals(senelecBillSearchDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("status", Integer.valueOf(senelecBillSearchDTO.getStatus())));
		}
		if( senelecBillSearchDTO.getFromDate()!= null && ! "".equals(senelecBillSearchDTO.getFromDate()) ){
			criteria1.add(Expression.between("paymentDate",DateUtil.stringToDate(senelecBillSearchDTO.getFromDate()),DateUtil.stringToDate(senelecBillSearchDTO.getToDate())));
		}
		if( senelecBillSearchDTO.getFromAmount()!= null && ! "".equals(senelecBillSearchDTO.getFromAmount()) ){
			criteria1.add(Expression.between("recordAmount",Double.valueOf(senelecBillSearchDTO.getFromAmount()),Double.valueOf(senelecBillSearchDTO.getToAmount())));
		}

		
		return criteria1.list();
	}
	//...End

	public Page getPolicyDetailsByPolicyNo(Integer pageNumber,String policyNo,Integer recordNo){

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SenelecControlFile.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria.add(Restrictions.eq("senelecCustomers.policyNumber", policyNo));
		criteria.add(Restrictions.eq("recordNumber", recordNo));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SenelecControlFile.class);
		criteria1.addOrder(Order.asc("billId"));
		criteria1.add(Restrictions.eq("senelecCustomers.policyNumber", policyNo));
		criteria1.add(Restrictions.eq("recordNumber", recordNo));
		//criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	public Page getSenelecBillByPolicyNo(Integer pageNumber,String policyNo,Integer recordNo){

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SenelecBills.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria.add(Restrictions.eq("senelecCustomers.policyNumber", policyNo));
		criteria.add(Restrictions.eq("recordNumber", recordNo));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(SenelecBills.class);
		criteria1.addOrder(Order.asc("billId"));
		criteria1.add(Restrictions.eq("senelecCustomers.policyNumber", policyNo));
		criteria1.add(Restrictions.eq("recordNumber", recordNo));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}
	
	/*This method not in use, SQL Injection not required.*/
	@Override
	public List<Object> getAllPaidBills() {

		List<Object>  billList = getHibernateTemplate().find("from SenelecBills sb where sb.paymentDate like'"+DateUtil.txnDate(new Date())+"%' and (sb.status = 1 or sb.status=2)");

		return  billList.size()>0 ? billList : null;
	}

	@Override
	public double getTotalTransactionAmount() {		

		/*Query query = getSessionFactory().getCurrentSession().createQuery("select sum(AmountPaid) from SenelecBills where PaymentDate like'"+DateUtil.txnDate(new Date())+"%' and (status = 1 or status=2)");*/
		Query query = getSessionFactory().getCurrentSession().
				createQuery("select sum(AmountPaid) from SenelecBills where PaymentDate like :date and (status = :status1 or status= :status2)")
				.setParameter("date", DateUtil.txnDate(new Date())+"%")
				.setParameter("status1", 1)
				.setParameter("status2", 2);
				
		List list=query.list();
         if(list!=null && list.get(0)!=null) 
       		 return Double.parseDouble(query.list().get(0).toString());
         
		return 0.0;
//...end
	}

	@Override
	public Biller getBillerNameByName(Integer billerId, String billerName,Integer countryId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Biller as biller where biller.billerName=:billerName and biller.country.countryId=:countryId and biller.billerId !=:billerId")
				.setParameter("billerName", billerName)
				.setParameter("countryId", countryId)
				.setParameter("billerId", billerId);
		List<Biller> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Page getBillersList(int pageNumber,Integer  bankId) {
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Biller.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Biller.class);
		if( bankId != null && ! "".equals(bankId) ){
			criteria1.add(Restrictions.eq("bank.bankId",bankId));
		}
		criteria1.addOrder(Order.asc("billerName"));
		  //criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		 // criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public Country loadCountry(Integer countryId) {
		Country country = (Country)getHibernateTemplate().load(Country.class, countryId);
		return country;
	}

	@Override
	public BillerTypes loadBillerType(Integer billerTypeId) {
		BillerTypes billerTypes = (BillerTypes)getHibernateTemplate().load(BillerTypes.class, billerTypeId);
		return billerTypes;
	}


}
