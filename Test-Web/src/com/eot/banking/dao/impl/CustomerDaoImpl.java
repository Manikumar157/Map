package com.eot.banking.dao.impl;

//Sql Injection is done by vineeth,on 17-10-2018

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import com.cbs.entity.CBSAccount;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.CustomerDao;
import com.eot.banking.dto.OtpDTO;
import com.eot.banking.dto.QRCodeDTO;
import com.eot.banking.dto.SCSubscriptionDTO;
import com.eot.banking.dto.SmsSubscriptionDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Account;
import com.eot.entity.AppMaster;
import com.eot.entity.Bank;
import com.eot.entity.Branch;
import com.eot.entity.BusinessPartner;
import com.eot.entity.City;
import com.eot.entity.CommisionSplits;
import com.eot.entity.Country;
import com.eot.entity.Customer;
import com.eot.entity.CustomerAccount;
import com.eot.entity.CustomerBankAccount;
import com.eot.entity.CustomerCard;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.CustomerScsubscription;
import com.eot.entity.CustomerSmsRuleDetail;
import com.eot.entity.Language;
import com.eot.entity.Otp;
import com.eot.entity.PendingTransaction;
import com.eot.entity.Quarter;
import com.eot.entity.ServiceChargeSubscription;
import com.eot.entity.SmsAlertRuleValue;

public class CustomerDaoImpl extends BaseDaoImpl implements CustomerDao{


	@Override
	@SuppressWarnings("unchecked")
	public Customer getCustomerByMobileNumber(String mobileNumber) {		
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from Customer as cust where cust.mobileNumber=:mobileNumber")
		                               .setParameter("mobileNumber",mobileNumber);*/
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer cs where concat(cs.country.isdCode,mobileNumber)=:mobileNumber")
				.setParameter("mobileNumber",mobileNumber);
		List<Customer> list=query.list();
		Customer cust = null;
		if(list.size()!=0) {
			cust=list.get(0);
			BusinessPartner bp= cust.getBusinessPartner();
			cust.setBusinessPartner(bp);
		}
		return cust;
	}

	@Override
	public Customer getCustomerByEmailAddress(String emailAddress) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer cs where concat(cs.country.isdCode,emailAddress)=:emailAddress")
				.setParameter("emailAddress",emailAddress);
		List<Customer> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Customer> getCustomerList(){		
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer as cust order by lower(cust.firstName)");
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Customer getCustomer(Long customerId) {				
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer as cust where cust.customerId=:customerId")
				.setParameter("customerId",customerId);
		List<Customer> list=query.list();

		Customer cust = list.size() > 0 ? list.get(0) : null;
		//System.out.println(cust.getCity().getCity());
		return  cust ; 
	}

	@Override
	@SuppressWarnings("unchecked")
	public Customer getAgentByAgentCode(String agentCode) {				
		Query query=getSessionFactory().getCurrentSession().createQuery("from Customer as cust where cust.agentCode=:agentCode")
				.setParameter("agentCode",agentCode);
		List<Customer> list=query.list();

		Customer cust = list.size() > 0 ? list.get(0) : null;

		return  cust ; 
	}
	@Override
	@SuppressWarnings("unchecked")
	public Customer getCustomerByAccountNumber(String accountNumber) {			
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount as ca where ca.accountNumber=:accountNumber")
				.setParameter("accountNumber",accountNumber);
		List<CustomerAccount> list=query.list();
		return  list.size()>0 ? list.get(0).getCustomer() : null ;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public Page searchCustomer(Integer bankId,Integer bankGroupId, String firstName, String middleName, String lastName, String mobileNumber,String bankID,String branchID,String countryId,String fromDate,String toDate,Integer pageNumber,String custType,Long brId,Integer partnerId, String onBoardedBy,String businessName, Integer kycStatus,String channel) {

		/*
		 * Session session =
		 * getHibernateTemplate().getSessionFactory().getCurrentSession(); Criteria
		 * criteria = session.createCriteria(Customer.class);
		 * criteria.setProjection(Projections.projectionList().add(Projections.rowCount(
		 * ))); criteria.addOrder(Order.desc("createdDate")); Date
		 * date1=null,date2=null; if(fromDate!= null && toDate!= null &&
		 * !"".equals(fromDate) && !"".equals(toDate)){ try { date1=new
		 * SimpleDateFormat("dd/MM/yyyy").parse(fromDate); SimpleDateFormat sdf = new
		 * SimpleDateFormat("dd/MM/yyyy"); Calendar c = Calendar.getInstance();
		 * c.setTime(sdf.parse(toDate)); c.add(Calendar.DAY_OF_MONTH, 1); String newDate
		 * = sdf.format(c.getTime()); date2=new
		 * SimpleDateFormat("dd/MM/yyyy").parse(newDate);
		 * 
		 * } catch (ParseException e) { e.printStackTrace(); }} if(null != firstName &&
		 * !"".equals(firstName)) {
		 * criteria.add(Restrictions.like("firstName","%"+firstName+"%")); } if(null !=
		 * middleName && !"".equals(middleName)) {
		 * criteria.add(Restrictions.like("middleName","%"+middleName+"%")); } if(null
		 * != lastName && !"".equals(lastName)) {
		 * criteria.add(Restrictions.like("lastName","%"+lastName+"%")); } if(null !=
		 * onBoardedBy && !"".equals(onBoardedBy)) {
		 * criteria.add(Restrictions.like("onbordedBy","%"+onBoardedBy+"%")); } if(null
		 * != businessName && !"".equals(businessName)) {
		 * criteria.add(Restrictions.like("businessName","%"+businessName+"%")); }
		 * if(null != kycStatus) { criteria.add(Restrictions.eq("kycStatus",kycStatus));
		 * } if( mobileNumber!= null && ! "".equals(mobileNumber) ){
		 * criteria.add(Restrictions.like("mobileNumber","%"+mobileNumber+"%")); } if(
		 * custType!= null && ! "".equals(custType) ){
		 * criteria.add(Restrictions.eq("type",Integer.parseInt(custType))); } if(
		 * partnerId!= null && ! "".equals(partnerId) ){
		 * criteria.add(Restrictions.eq("businessPartner.id",partnerId)); } if(
		 * countryId!= null && ! "".equals(countryId) ){
		 * criteria.createCriteria("country").add(Restrictions.eq("countryId",Integer.
		 * valueOf(countryId))); }
		 * criteria.createCriteria("customerAccounts","custAcc");
		 * criteria.createCriteria("custAcc.account").add(Restrictions.eq("aliasType",
		 * EOTConstants.ALIAS_TYPE_WALLET_ACCOUNT)); if( bankID!= null && !
		 * "".equals(bankID) ){ criteria.add(Restrictions.eq("custAcc.bank.bankId",
		 * Integer.valueOf(bankID))); } if( branchID!= null && ! "".equals(branchID) &&
		 * !branchID.equals("select")){
		 * criteria.createCriteria("custAcc.branch").add(Restrictions.eq("branchId",
		 * Long.valueOf(branchID))); } if(bankId!=null && ! "".equals(bankId)){
		 * criteria.add(Restrictions.eq("custAcc.bank.bankId", bankId)); } if(brId!=null
		 * && ! "".equals(brId)){
		 * criteria.add(Restrictions.eq("custAcc.branch.branchId", brId)); }
		 * if(bankGroupId!=null && ! "".equals(bankGroupId)){
		 * criteria.createCriteria("custAcc.bank").add(Restrictions.eq(
		 * "bankGroup.bankGroupId", bankGroupId)); } if(fromDate!=null && !
		 * "".equals(fromDate) && toDate!=null && "".equals(toDate)){
		 * criteria.add(Restrictions.
		 * sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like ?",fromDate.
		 * toUpperCase()+"%",Hibernate.DATE)); } if(fromDate!=null && "".equals(
		 * fromDate) && toDate!=null && !"".equals( toDate)){ criteria.add(Restrictions.
		 * sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like ?",toDate.
		 * toUpperCase()+"%",Hibernate.STRING)); }
		 * 
		 * if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && !
		 * "".equals( toDate))){ criteria.add(Restrictions.between("createdDate", date1,
		 * date2)); }
		 * 
		 * int totalCount = Integer.parseInt(criteria.list().get(0).toString());
		 */
		
		String joinType = "JOIN";
		
		if(custType!= null && ! "".equals(custType)){
			if(!"0".equals(custType)) 
				joinType = "LEFT JOIN";
		}
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer qry = new StringBuffer("SELECT CONCAT(this_.FirstName,\" \" ,this_.LastName) AS CustomerName,this_.CustomerID, this_.MobileNumber AS MobileNumber,"
				+ "this_.Active,this_.Gender AS Gender,this_.CreatedDate,country1_.Country AS Country,city1_.City,this_.Type,this_.AgentCode,this_.BusinessName,this_.ApprovalDate,this_.ApprovedBy,"
				+ "this_.OnbordedBy,CONCAT(agent.FirstName,\" \" ,agent.LastName) AS AgentName,bp.Code,bp.Name,"
				+ "this_.kyc_status FROM Customer this_ JOIN Country country1_ ON this_.CountryID = country1_.CountryID " 
				+ "JOIN City city1_ ON this_.CityID = city1_.CityID joinType Customer agent ON this_.OnbordedBy = agent.AgentCode "
				+ "joinType BusinessPartner bp ON this_.PartnerId = bp.Id where this_.Type =:custType"); 
	 if(StringUtils.isNotBlank(channel) && channel.equals("2")) {
		 qry = new StringBuffer("SELECT CONCAT(this_.FirstName,\" \" ,this_.LastName) AS CustomerName,this_.CustomerID, this_.MobileNumber AS MobileNumber,"
					+ "this_.Active,this_.Gender AS Gender,this_.CreatedDate,country1_.Country AS Country,city1_.City,this_.Type,this_.AgentCode,this_.ApprovalDate,this_.ApprovedBy,"
					+ "this_.OnbordedBy,"
					+ "this_.kyc_status FROM Customer this_ JOIN Country country1_ ON this_.CountryID = country1_.CountryID " 
					+ "JOIN City city1_ ON this_.CityID = city1_.CityID "
					+ "JOIN WebUsers wu on this_.OnbordedBy = wu.UserName where this_.Type =:custType"); 
	 }

		if(null != firstName && !"".equals(firstName)) {
			qry.append(" and this_.FirstName  LIKE :firstName");
		}
		if(null != middleName && !"".equals(middleName)) {
			qry.append(" and this_.middleName  LIKE :middleName");
		}
		if(null != lastName && !"".equals(lastName)) {
			qry.append(" and this_.LastName  LIKE :lastName");
		}
		if(null != onBoardedBy  && !"".equals(onBoardedBy)) {
			qry.append(" and this_.OnbordedBy  LIKE :onBoardedBy");
		}
		if(null != businessName  && !"".equals(businessName)) {
			qry.append(" and this_.businessName  LIKE :businessName");
		}
		if(null != kycStatus) {
			qry.append(" and this_.kyc_status =:kycStatus");
		}
		if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			qry.append(" and this_.mobileNumber  LIKE :mobileNumber");
		}

		if( partnerId!= null && ! "".equals(partnerId) ){
			qry.append(" and (bp.ID =:partnerId OR bp.SeniorPartner=:partnerId)");

		}
		if( countryId!= null && ! "".equals(countryId) ){
			qry.append(" and country1_.CountryID =:countryId");
		}	
		
		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qry.append(" and DATE(this_.CreatedDate)>=:fromDate and DATE(this_.CreatedDate)<=:toDate ");
		}
		qry.append("  ORDER BY this_.CreatedDate DESC");
		
		SQLQuery qryResult = session.createSQLQuery(qry.toString().replace("joinType", joinType));
		
		if( custType!= null && ! "".equals(custType) ){
			qryResult.setParameter("custType", custType);
		}
		if(null != firstName && !"".equals(firstName)) {
			qryResult.setParameter("firstName", "%"+firstName+"%");
		}
		if(null != lastName && !"".equals(lastName)) {
			qryResult.setParameter("lastName", "%"+lastName+"%");
		}
		if(null != middleName && !"".equals(middleName)) {
			qryResult.setParameter("middleName", "%"+middleName+"%");
		}
		if(null != onBoardedBy  && !"".equals(onBoardedBy)) {
			qryResult.setParameter("onBoardedBy", "%"+onBoardedBy+"%");
		}
		
		if(null != businessName  && !"".equals(businessName)) {
			qryResult.setParameter("businessName", "%"+businessName+"%");
		}
		if(null != kycStatus) {
			qryResult.setParameter("kycStatus", kycStatus);
		}
		if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			qryResult.setParameter("mobileNumber", "%"+mobileNumber+"%");
		}
		
		
		if( partnerId!= null && ! "".equals(partnerId+"") ){
			qryResult.setParameter("partnerId", partnerId);
		}
		if( countryId!= null && ! "".equals(countryId) ){
			qryResult.setParameter("countryId", countryId);
		}

		if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
			qryResult.setParameter("fromDate", DateUtil.formatDateWithSlash(fromDate,0,0,0));
			qryResult.setParameter("toDate",  DateUtil.formatDateWithSlash(toDate,0,0,0));
		}

		qryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(qryResult.list(), qryResult.list().size(), appConfig.getResultsPerPage(), pageNumber);

	}

	@SuppressWarnings("unchecked")
	@Override
	public AppMaster getApplication(Long referenceId,Integer referenceType) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from AppMaster where referenceId=:referenceId and referenceType=:referenceType and status!=:status");
		query.setParameter("referenceId", referenceId+"");
		query.setParameter("referenceType", referenceType);
		query.setParameter("status", EOTConstants.APP_STATUS_DEACTIVE);
		List<AppMaster> list = query.list();
		return list.size() >0 ? list.get(0) : null ;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public CustomerAccount findCustomerAccount(Long customerId,Integer bankId) {		
		List<CustomerAccount> list=null;		
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount as acc where acc.bank.bankId=:bankId and acc.customer.customerId=:customerId");
		query.setParameter("bankId",bankId);
		query.setParameter("customerId",customerId);		
		list = query.list();
		CustomerAccount customerAccount=null;
		if(list.size()>0)
			customerAccount=list.get(0);
		return customerAccount;

	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public CustomerAccount findAgentCommissionAccount(Long customerId,Integer bankId) {		
		List<CustomerAccount> list=null;		
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount as acc where acc.bank.bankId=:bankId and acc.customer.customerId=:customerId and acc.account.aliasType=:aliasType");
		query.setParameter("bankId",bankId);
		query.setParameter("customerId",customerId);	
		query.setParameter("aliasType", EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);
		list = query.list();
		CustomerAccount customerAccount=null;
		if(list.size()>0)
			customerAccount=list.get(0);
		System.out.println("===============> "+customerAccount.getBank().getBankName());
		return customerAccount;

	}

	@SuppressWarnings("unchecked")
	public Page getCustomerProfiles(Integer bankId,int pageNumber){
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CustomerProfiles.class);
		criteria.add(Restrictions.eq("bank.bankId", bankId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CustomerProfiles.class);
		criteria1.add(Restrictions.eq("bank.bankId", bankId));
		criteria1.addOrder(Order.asc("profileName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<CustomerProfiles> customerProfiles = criteria1.list();

		return PaginationHelper.getPage(customerProfiles, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	public CustomerProfiles editCustomerProfile(Integer profileId){
		return (CustomerProfiles)getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CustomerProfiles.class)
				.add(Restrictions.eq("profileId", profileId)).uniqueResult();
	}
	public Integer getMobileNumLength(Integer countryId){
		return (Integer)getHibernateTemplate().getSessionFactory().getCurrentSession()
				//	.createQuery("select mobileNumberLength from Country where countryId=?").setInteger(0, countryId).uniqueResult();
				.createQuery("select mobileNumberLength from Country where countryId=:countryId").setInteger("countryId", countryId).uniqueResult();
	}

	public List<CustomerProfiles> getCustomerProfilesByBankId(Integer bankId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles cp where cp.bank.bankId=:bankId order by profileName").setParameter("bankId", bankId);
		//@End
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles cp where cp.bank.bankId="+bankId+" order by profileName");*/
		return query.list();		
	}

	public List<CustomerProfiles> getCustomerProfilesByBankIds(Set<Integer> bankIds){
		String qry="from CustomerProfiles cp where cp.bank.bankId IN (:bankIds)" 	;
		Query query = getSessionFactory().getCurrentSession().createQuery(qry).setParameterList("bankIds", bankIds);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<CustomerProfiles> getCustomerProfileByName(String customerProfileName,Integer bankId){
		return getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(CustomerProfiles.class)
				.add(Restrictions.eq("profileName", customerProfileName)).
				add(Restrictions.eq("bank.bankId", bankId)).list();
	}

	@Override
	public CustomerProfiles getCustomerProfileByName(String customerProfileName, Integer bankId, Integer profileId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles as cp where cp.profileName=:profileName and cp.bank.bankId=:bankId and cp.profileId !=:profileId")
				.setParameter("profileName", customerProfileName)
				.setParameter("bankId", bankId)
				.setParameter("profileId", profileId);
		List<CustomerProfiles> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public AppMaster getUpdateAppType(Long referenceId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from AppMaster where referenceId=:referenceId and status!=:status");
		query.setParameter("referenceId", referenceId+"");

		query.setParameter("status", EOTConstants.APP_STATUS_DEACTIVE);
		List<AppMaster> list = query.list();
		return list.size() >0 ? list.get(0) : null ;
	}

	/*@This Method not in use, No Required SQL Injection.*/ 
	@Override
	public List exportToXLSForCustomerDetails(Integer bankId,Integer bankGroupId,String customerName,String mobileNumber, String bankID, String branchId,String countryId,String fromDate,String toDate,Long brId) {


		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Customer.class);
		criteria.addOrder(Order.asc("createdDate"));
		//@Start, by Murari, dated : 03-08-2018, purpose bug 5716
		Date date1=null,date2=null;
		if(fromDate!= null && toDate!= null){ 
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
		//--@End---
		if( customerName!= null && ! "".equals(customerName) ) {
			criteria.add(
					Restrictions.or(
							Restrictions.sqlRestriction("lower(concat(FirstName,' ',MiddleName,' ',LastName)) like '%"+customerName.trim().replaceAll(" ", "%")+"%'"),
							Restrictions.or(
									Restrictions.or(
											Restrictions.like("firstName", "%"+customerName.toLowerCase().trim()+"%"),
											Restrictions.like("middleName", "%"+customerName.toLowerCase().trim()+"%")
											),
									Restrictions.like("lastName", "%"+customerName.toLowerCase().trim()+"%")
									)
							)
					);
		}if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			criteria.add(Restrictions.like("mobileNumber", "%"+mobileNumber+"%"));
		}
		if( countryId!= null && ! "".equals(countryId) ){
			criteria.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
		}	
		criteria.createCriteria("customerAccounts","custAcc");

		if( bankID!= null && ! "".equals(bankID) ){
			criteria.add(Restrictions.eq("custAcc.bank.bankId", Integer.valueOf(bankID)));		
		}
		if( branchId!= null && ! "".equals(branchId) && !branchId.equals("select")){
			criteria.createCriteria("custAcc.branch").add(Restrictions.eq("branchId", Long.valueOf(branchId)));		
		}		
		if(bankId!=null){
			criteria.add(Restrictions.eq("custAcc.bank.bankId", bankId));
		}
		if(brId!=null){
			criteria.add(Restrictions.eq("custAcc.branch.branchId", brId));
		}
		if(bankGroupId!=null && ! "".equals(bankGroupId)){
			criteria.createCriteria("custAcc.bank").add(Restrictions.eq("bankGroup.bankGroupId", bankGroupId));
		}		
		if(fromDate!=null && ! "".equals(fromDate) && toDate!=null &&  "".equals(toDate)){	      
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like '"+fromDate.toUpperCase()+"'"));
		}
		if(fromDate!=null &&  "".equals( fromDate) && toDate!=null &&  !"".equals( toDate)){	      
			criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\") like '"+toDate.toUpperCase()+"'"));
		}		
		//@Start, by Murari, dated : 03-08-2018, purpose bug 5716
		/*if((fromDate!=null && ! "".equals( fromDate)) && (toDate!=null && ! "".equals( toDate))){
	            criteria.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +"and '"+toDate.toUpperCase()+"'"));
	       }*/

		if(fromDate!=null && ! "".equals( fromDate) && toDate!=null && ! "".equals( toDate)){
			/*  criteria1.add(Restrictions.sqlRestriction("DATE_FORMAT(CreatedDate,\"%d/%m/%Y\")  between '"+fromDate.toUpperCase()+"' " +
		    		"and '"+toDate.toUpperCase()+"'"));*/

			criteria.add(Restrictions.between("createdDate", date1, date2));   
			//--@End---

		}
		return criteria.list();
	}

	@Override
	public List<Customer> getCusomers() {
		Query query=getSessionFactory().getCurrentSession().createQuery("select cust.firstName from Customer as cust");
		return query.list();
	}

	@Override
	@Transactional
	public Account getAccount(String accountNumber) {

		Query query=getSessionFactory().getCurrentSession().createQuery("from Account as acc where acc.accountNumber=:accountNumber");
		query.setParameter("accountNumber",accountNumber);
		List<Account> list= query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public CustomerAccount findCustomerAccountByBankBranch(Long customerId,
			Integer bankId, Long branchId) {
		List<CustomerAccount> list=null;		
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount as acc where acc.bank.bankId=:bankId and acc.customer.customerId=:customerId and acc.branch.branchId=:branchId");
		query.setParameter("bankId",bankId);
		query.setParameter("customerId",customerId);	
		query.setParameter("branchId",branchId);
		list = query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public CBSAccount getCbsAccountDetails(String accountNumber,String bankCode,String branchCode) {

		Query query=getSessionFactory().getCurrentSession().createQuery("from CBSAccount as cbsAcc where cbsAcc.accountNumber=:accountNumber and cbsAcc.bankCode=:bankCode and cbsAcc.branchCode=:branchCode");
		query.setParameter("accountNumber",accountNumber);
		query.setParameter("bankCode",bankCode);
		query.setParameter("branchCode",branchCode);
		List<CBSAccount> list= query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public CustomerBankAccount getCustomerBankAccountDetails(String accountNumber) {

		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerBankAccount as cba where cba.bankAccountNumber=:accountNumber");
		query.setParameter("accountNumber",accountNumber);
		List<CustomerBankAccount> list= query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public Page getCustomerBankAccountDetails(Long customerId, int pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CustomerBankAccount.class);
		criteria.add(Restrictions.eq("referenceId",customerId.toString()));

		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CustomerBankAccount.class);
		criteria1.add(Restrictions.eq("referenceId",customerId.toString()));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
		List<CustomerBankAccount> custBankAcc = criteria1.list();

		return PaginationHelper.getPage(custBankAcc, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public CustomerBankAccount getCustomerBankDetails(Long slNo) {
		return getHibernateTemplate().get(CustomerBankAccount.class, slNo);
	}

	@Override
	public Branch getBranchDetails(Long branchId) {
		return getHibernateTemplate().get(Branch.class, branchId);
	}

	@Override
	public Branch getBranchDetailsByCode(String branchCode) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Branch as branch where branch.branchCode=:branchCode");
		query.setParameter("branchCode",branchCode);
		List<Branch> list= query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public Page getCustomerCardDetails(Long customerId, int pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CustomerCard.class);
		criteria.add(Restrictions.eq("referenceId",customerId.toString()));

		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CustomerCard.class);
		criteria1.add(Restrictions.eq("referenceId",customerId.toString()));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<CustomerBankAccount> custBankAcc = criteria1.list();

		return PaginationHelper.getPage(custBankAcc, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public CustomerCard getCustomerCard(Long cardId,String cardNumber) {
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardId!=:cardId and cc.cardNumber=:cardNumber and cc.status!=3");
		query.setParameter("cardId", cardId);
		query.setParameter("cardNumber",cardNumber);*/
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardId!=:cardId and cc.cardNumber=:cardNumber and cc.status!=:status");
		query.setParameter("cardId", cardId);
		query.setParameter("cardNumber",cardNumber);
		query.setParameter("status", 3);
		List<CustomerCard> list= query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public CustomerCard getCustomerCard(String cardNo) {
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardNumber=:cardNo and cc.status=2");*/
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardNumber=:cardNo and cc.status=:=status");
		query.setParameter("cardNo",cardNo);
		query.setParameter("status", 2);
		List<CustomerCard> list= query.list();	
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public CustomerCard getCustomerCard(Long cardId) {
		return getHibernateTemplate().get(CustomerCard.class, cardId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SmsAlertRuleValue> getSmsPackageList() {
		/*date:05/08/16 by:rajlaxmi for:showing only subscribed sms packages details changed query*/
		Query query=getSessionFactory().getCurrentSession().createQuery("from SmsAlertRuleValue GROUP BY smsalertrule.smsAlertRuleId"); 
		List<SmsAlertRuleValue>packageList = query.list();
		return packageList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SmsSubscriptionDTO> smsPackageDetails(String packageId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from SmsAlertRule where SmsAlertRuleId=:packageId"); 
		query.setParameter("packageId",Long.parseLong(packageId));
		List<SmsSubscriptionDTO>SmsAlertRuleValueList=query.list();
		return SmsAlertRuleValueList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomerSmsRuleDetail getCurrentSubscription(String customerID) {
		//	List<CustomerSmsRuleDetail>  customerSmsRuleDetails = getHibernateTemplate().find("from CustomerSmsRuleDetail csrd where csrd.customer.customerId=? and csrd.status=?",Long.valueOf(customerID),EOTConstants.ACTIVE);

		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerSmsRuleDetail csrd where csrd.customer.customerId=:customerId and csrd.status=:status"); 
		query.setParameter("customerId",Long.valueOf(customerID));
		query.setParameter("status",EOTConstants.ACTIVE);
		List<CustomerSmsRuleDetail>  customerSmsRuleDetails=query.list();

		return customerSmsRuleDetails.size()>0 ? customerSmsRuleDetails.get(0) : null;
	}

	// vineeth, Sql Injection, on 17-10-2018
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerSmsRuleDetail> getAllExistingSubscription(Long customerId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerSmsRuleDetail csrd where csrd.customer.customerId=:customerId and csrd.status=:status");
		query.setParameter("customerId",customerId);
		query.setParameter("status",EOTConstants.ACTIVE);
		List<CustomerSmsRuleDetail> customerSmsRuleDetails = query.list();
		//	List<CustomerSmsRuleDetail> customerSmsRuleDetails = getHibernateTemplate().find("from CustomerSmsRuleDetail csrd where csrd.customer.customerId=? and csrd.status=? ",customerId,EOTConstants.ACTIVE);
		return customerSmsRuleDetails;
	}
	// end vineeth code.
	@Override
	public List<ServiceChargeSubscription> getSCPackageList() {
		/*date:05/08/16 by:rajlaxmi for:showing only subscribed sc packages details changed query*/
		Query query=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSubscription GROUP BY serviceChargeRule.serviceChargeRuleId"); 
		List<ServiceChargeSubscription>packageList = query.list();
		return packageList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<SCSubscriptionDTO> scPackageDetails(String packageId) {
		Query query =getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSubscription as scs where scs.serviceChargeRule.serviceChargeRuleId =:packageId");
		query.setParameter("packageId", Long.parseLong(packageId));
		List<SCSubscriptionDTO> list =query.list();
		return list;

	}
	// vineeth chnages for Sql Injection on 17-10-2018
	@SuppressWarnings("unchecked")
	@Override
	public CustomerScsubscription getSCCurrentSubscription(String customerID) {
		//	List<CustomerScsubscription>  customerScsubscription = getHibernateTemplate().find("from CustomerScsubscription css where css.customer.customerId=? and css.status=?",Long.valueOf(customerID),EOTConstants.ACTIVE);
		Query query =getSessionFactory().getCurrentSession().createQuery("from CustomerScsubscription css where css.customer.customerId=:customerId and css.status=:status");
		query.setParameter("customerId",Long.valueOf(customerID));
		query.setParameter("status",EOTConstants.ACTIVE);
		List<CustomerScsubscription>  customerScsubscription =query.list();
		return customerScsubscription.size()>0 ? customerScsubscription.get(0) : null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerScsubscription> getSCSubscribedList(String customerID) {
		//	List<CustomerScsubscription>  customerScsubscription = getHibernateTemplate().find("from CustomerScsubscription css where css.customer.customerId=? and css.status=?",Long.valueOf(customerID),EOTConstants.ACTIVE);

		Query query =getSessionFactory().getCurrentSession().createQuery("from CustomerScsubscription css where css.customer.customerId=:customerId and css.status=:status");
		query.setParameter("customerID",Long.valueOf(customerID));
		query.setParameter("status",EOTConstants.ACTIVE);
		List<CustomerScsubscription>  customerScsubscription =query.list();

		return customerScsubscription.size()>0 ? customerScsubscription : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getLanguageDescription (String languageCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Language la where la.languageCode=:languageCode order by LanguageCode ").setParameter("languageCode", languageCode);
		Language language = (Language)query.list().get(0);
		return language.getDescription();
	}

	@Override
	public List<CustomerProfiles> loadCustomerProfiles(Integer bankId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CustomerProfiles.class);
		criteria1.addOrder(Order.asc("profileId"));
		criteria1.createCriteria("bank").add(Restrictions.eq("bankId",bankId));
		return criteria1.list();
	}
	@Override
	@Transactional
	public List<Customer> loadAgentProcessTxnByDate(String date) {

		//		Query query=getSessionFactory().getCurrentSession().createSQLQuery("select * from Customer as cust where cust.customerId in(SELECT DISTINCT(trxn.referenceId) FROM Transactions trxn INNER JOIN Customer cust1 where trxn.referenceId= cust1.customerId and trxn.transactionType IN(115,116) AND trxn.commissionStatus is null And trxn.transactionDate  LIKE '%"+date+"%' AND cust1.type IN(1,2,3))");


		Query query=getSessionFactory().getCurrentSession().createSQLQuery("select * from Customer as cust where cust.customerId in(SELECT DISTINCT(trxn.referenceId) FROM Transactions trxn INNER JOIN Customer cust1 where trxn.referenceId= cust1.customerId and trxn.transactionType IN(:TxnType1,:TxnType2,:TxnType3,:TxnType4) AND trxn.commissionStatus is null And trxn.Status=:Status And trxn.transactionDate   LIKE :date AND cust1.type IN(:type1,:type2,:type3))")
				.setParameter("TxnType1", 115)
				.setParameter("TxnType2", 116)
				.setParameter("TxnType3", 90)
				.setParameter("TxnType4", 126)
				.setParameter("Status", 2000)
				.setParameter("date","%"+date+"%")
				.setParameter("type1", 1)
				.setParameter("type2", 2)
				.setParameter("type3", 3);	

		/*
		 *  1=merchant
			2=sole marchant
		  	3=agent sole marchatn
		 */

		List<Object[]> list = query.list();
		List<Customer> customerList=new ArrayList<Customer>();
		for(Object[] obj : list)
		{
			Customer customer= new Customer();
			customer.setCustomerId(((BigInteger)obj[0]).longValue());
			customer.setMobileNumber((String)obj[1]);
			customer.setAppId((String)obj[2]);
			customer.setFirstName((String)obj[3]);
			customer.setMiddleName((String)obj[4]);
			customer.setLastName((String)obj[5]);
			customer.setDob((Date)obj[6]);
			customer.setAddress((String)obj[7]);
			customer.setEmailAddress((String)obj[8]);
			customer.setActive((Integer)obj[9]);
			customer.setTitle((String)obj[10]);
			customer.setGender((String)obj[11]);
			customer.setPlaceOfBirth((String)obj[12]);
			customer.setDefaultLanguage((String)obj[13]);
			customer.setLoginPin((String)obj[14]);
			customer.setTransactionPin((String)obj[15]);
			customer.setCreatedDate((Date)obj[16]);
			customer.setType((Integer)obj[17]);
			customer.setAgentCode((String)obj[27]);

			CustomerProfiles customerProfiles=new CustomerProfiles();
			customerProfiles.setProfileId((Integer)obj[18]);
			customer.setCustomerProfiles(customerProfiles);

			City city= new City();
			city.setCityId((Integer)obj[19]);
			customer.setCity(city);

			Country country = new Country();
			country.setCountryId((Integer)obj[20]);
			customer.setCountry(country);

			Quarter quarter = new Quarter();
			quarter.setQuarterId(((BigInteger)obj[21]).longValue());
			customer.setQuarter(quarter);
			customer.setProfession((String)obj[22]);
			customer.setBankCustomerId((String)obj[23]);
			customer.setOnbordedBy((String)obj[24]);
			customer.setCommission((Double)obj[25]);

			BusinessPartner businessPartner = new BusinessPartner();
			businessPartner.setId((Integer)obj[26]);
			customer.setBusinessPartner(businessPartner);
			customerList.add(customer);
		}

		return customerList;
	}

	@Override
	@Transactional
	public List<Customer> loadAgentProcessDepoTxnByDate(String date) {

		//		Query query=getSessionFactory().getCurrentSession().createSQLQuery("select * from Customer as cust where cust.customerId in(SELECT DISTINCT(trxn.referenceId) FROM Transactions trxn INNER JOIN Customer cust1 where trxn.referenceId= cust1.customerId and trxn.transactionType IN(115,116) AND trxn.commissionStatus is null And trxn.transactionDate  LIKE '%"+date+"%' AND cust1.type IN(1,2,3))");


		Query query=getSessionFactory().getCurrentSession().createSQLQuery("select * from Customer as cust where cust.customerId in(SELECT DISTINCT(trxn.referenceId) FROM Transactions trxn INNER JOIN Customer cust1 where trxn.referenceId= cust1.customerId and trxn.transactionType IN(:TxnType1) AND trxn.commissionStatus is null And trxn.Status=:Status And trxn.transactionDate   LIKE :date AND cust1.type IN(:type1,:type2,:type3))")
				.setParameter("TxnType1", 115)
				.setParameter("Status", 2000)
				.setParameter("date","%"+date+"%")
				.setParameter("type1", 1)
				.setParameter("type2", 2)
				.setParameter("type3", 3);	

		/*
		 *  1=merchant
			2=sole marchant
		  	3=agent sole marchatn
		 */

		List<Object[]> list = query.list();
		List<Customer> customerList=new ArrayList<Customer>();
		for(Object[] obj : list)
		{
			Customer customer= new Customer();
			customer.setCustomerId(((BigInteger)obj[0]).longValue());
			customer.setMobileNumber((String)obj[1]);
			customer.setAppId((String)obj[2]);
			customer.setFirstName((String)obj[3]);
			customer.setMiddleName((String)obj[4]);
			customer.setLastName((String)obj[5]);
			customer.setDob((Date)obj[6]);
			customer.setAddress((String)obj[7]);
			customer.setEmailAddress((String)obj[8]);
			customer.setActive((Integer)obj[9]);
			customer.setTitle((String)obj[10]);
			customer.setGender((String)obj[11]);
			customer.setPlaceOfBirth((String)obj[12]);
			customer.setDefaultLanguage((String)obj[13]);
			customer.setLoginPin((String)obj[14]);
			customer.setTransactionPin((String)obj[15]);
			customer.setCreatedDate((Date)obj[16]);
			customer.setType((Integer)obj[17]);

			CustomerProfiles customerProfiles=new CustomerProfiles();
			customerProfiles.setProfileId((Integer)obj[18]);
			customer.setCustomerProfiles(customerProfiles);

			City city= new City();
			city.setCityId((Integer)obj[19]);
			customer.setCity(city);

			Country country = new Country();
			country.setCountryId((Integer)obj[20]);
			customer.setCountry(country);

			Quarter quarter = new Quarter();
			quarter.setQuarterId(((BigInteger)obj[21]).longValue());
			customer.setQuarter(quarter);
			customer.setProfession((String)obj[22]);
			customer.setBankCustomerId((String)obj[23]);
			customer.setOnbordedBy((String)obj[24]);
			customer.setCommission((Double)obj[25]);

			BusinessPartner businessPartner = new BusinessPartner();
			businessPartner.setId((Integer)obj[26]);
			customer.setBusinessPartner(businessPartner);
			customerList.add(customer);
		}

		return customerList;
	}

	@Override
	@Transactional
	public Double loadTotalSharableAmountByAgent(Long agentId, String date,String bankRevnueAccount)
	{
		Query query=getSessionFactory().getCurrentSession().createSQLQuery("SELECT SUM(txnJr.Amount) FROM TransactionJournals txnJr INNER JOIN Transactions txn ON txnJr.TransactionID = txn.transactionID WHERE txn.ReferenceId=:ReferenceId and txn.transactionDate LIKE:date AND txnJr.creditAccount=:bankRevnueAccount AND txn.commissionStatus IS NULL and txn.Status=:status AND txn.transactionType IN(:txn2,:txn4) ")
				.setParameter("date","%"+date+"%")
				.setParameter("ReferenceId", agentId)
				.setParameter("bankRevnueAccount", bankRevnueAccount)
				.setParameter("status", EOTConstants.TXN_NO_ERROR)
				//				.setParameter("txn1", 115)
				.setParameter("txn2", 116)
				//				.setParameter("txn3", 90)
				.setParameter("txn4", 126);
		List<Double> list = query.list();
		Double CommissionAmount=0d;
		if(!list.isEmpty())
		{
			/*Object obj[]=list.get(0);
			CommissionAmount=(Double)obj[0];*/

			CommissionAmount=list.get(0);
		}
		if(CommissionAmount==null)
			CommissionAmount=0d;
		return CommissionAmount;
	}

	@Override
	@Transactional
	public Double loadTotalSharableAmountDeposite(Long agentId, String date,String bankRevnueAccount)
	{
		Query query=getSessionFactory().getCurrentSession().createSQLQuery("SELECT SUM(txnJr.Amount) FROM TransactionJournals txnJr INNER JOIN Transactions txn ON txnJr.TransactionID = txn.transactionID WHERE txn.ReferenceId=:ReferenceId and txn.transactionDate LIKE:date AND txnJr.creditAccount=:bankRevnueAccount AND txn.commissionStatus IS NULL and txn.Status=:status AND txn.transactionType IN(:txn1) ")
				.setParameter("date","%"+date+"%")
				.setParameter("ReferenceId", agentId)
				.setParameter("bankRevnueAccount", bankRevnueAccount)
				.setParameter("status", EOTConstants.TXN_NO_ERROR)
				.setParameter("txn1", 115);
		//				.setParameter("txn2", 116)
		//				.setParameter("txn3", 90)
		//				.setParameter("txn4", 126);
		List<Double> list = query.list();
		Double CommissionAmount=0d;
		if(!list.isEmpty())
		{
			/*Object obj[]=list.get(0);
			CommissionAmount=(Double)obj[0];*/

			CommissionAmount=list.get(0);
		}
		if(CommissionAmount==null)
			CommissionAmount=0d;
		return CommissionAmount;
	}

	@Override
	public BusinessPartner getbusinessPartnere(long customerId) {
		Long custid = customerId;
		int id = custid.intValue();
		Query query=getSessionFactory().getCurrentSession().createQuery("from BusinessPartner as businessPartner where businessPartner.id=:id")
				.setParameter("id",id);
		List<BusinessPartner> list=query.list();
		return  list.size()>0 ? list.get(0) : null ; 
	}

	@Override
	public Country getCountry(Integer countryId) {
		Country  country = getHibernateTemplate().load(Country.class, countryId);
		return country;
	}

	public void flusing() {
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
		getHibernateTemplate().getSessionFactory().getCurrentSession().clear();
	}

	public List<CustomerProfiles> getCustomerProfilesByType(Integer bankId,String type){

		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles cp where cp.bank.bankId=:bankId and cp.customerType=:customerType order by profileName").setParameter("bankId", bankId).setParameter("customerType", Integer.parseInt(type));

		/*Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerProfiles cp where cp.bank.bankId="+bankId+" order by profileName");*/
		return query.list();		
	}

	@Override
	public City getCity(Integer cityId) {
		City  city = getHibernateTemplate().load(City.class, cityId);
		return city;
	}

	@Override
	public Quarter getQuarter(Long quarterId) {
		Quarter  quarter = getHibernateTemplate().load(Quarter.class, quarterId);
		return quarter;
	}

	@Override
	public CustomerProfiles getCustomerProfiles(Integer customerProfileId) {
		CustomerProfiles  customerProfile = getHibernateTemplate().load(CustomerProfiles.class, customerProfileId);
		return customerProfile;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public CustomerAccount findCustomerAccountBy(Long customerId) {		
		List<CustomerAccount> list=null;		
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount as acc where  acc.customer.customerId=:customerId");
		query.setParameter("customerId",customerId);		
		list = query.list();
		CustomerAccount customerAccount=null;
		if(list.size()>0)
			customerAccount=list.get(0);
		Account account=customerAccount.getBank().getAccount();
		Bank bank= customerAccount.getBank();
		bank.setAccount(account);

		return customerAccount;

	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public CustomerAccount findAgentCommissionAccountBy(Long customerId) {		
		List<CustomerAccount> list=null;		
		Query query=getSessionFactory().getCurrentSession().createQuery("from CustomerAccount as acc where  acc.customer.customerId=:customerId and acc.account.aliasType=:aliasType");
		query.setParameter("customerId",customerId);		
		query.setParameter("aliasType",EOTConstants.ALIAS_TYPE_COMMISSION_ACCOUNT);		
		list = query.list();
		CustomerAccount customerAccount=null;
		if(list.size()>0)
			customerAccount=list.get(0);
		Account account=customerAccount.getBank().getAccount();
		Bank bank= customerAccount.getBank();
		bank.setAccount(account);

		return customerAccount;

	}

	@Override
	public String getAgentCode(Integer custType) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("SELECT MAX(AgentCode)+1 AS seq FROM Customer Where Type=:custType");
		query.setParameter("custType", custType);
		List list = query.list();
		return list.get(0) == null ? "1" :list.get(0).toString();
	}

	@Override
	public String getBusinessPartnerCode() {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("SELECT MAX(code) AS seq FROM BusinessPartner");
		List list = query.list();
		return list.get(0) == null ? "1" :list.get(0).toString();
	}


	@Override
	public Otp verifyOTP(OtpDTO otpDTO) {

		String[] params = new String[]{"referenceId","referenceType", "otpHash","otpType" } ;
		Object[] values = new Object[]{otpDTO.getReferenceId(),otpDTO.getReferenceType(),otpDTO.getOtphash(),otpDTO.getOtpType()};

		List<Otp> list= getHibernateTemplate().findByNamedParam("from Otp as otp where otp.comp_id.referenceId=:referenceId and " +
				"otp.comp_id.referenceType=:referenceType and otp.otpHash=:otpHash and otp.otpType=:otpType",params,values);

		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public AppMaster getApplicationByAppId(String appId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from AppMaster where appId=:appId");
		query.setParameter("appId", appId);
		List<AppMaster> list = query.list();
		return list.size() >0 ? list.get(0) : null ;
	}

	@Override
	public Account getAccountByAliasAndRef(String alias, String referenceId) {
		List<Account> list=null;
		Query query=getSessionFactory().getCurrentSession().createQuery("from Account as acc where  acc.alias=:alias and acc.referenceId=:referenceId");
		query.setParameter("alias",alias);		
		query.setParameter("referenceId",referenceId);
		list = query.list();
		Account account=null;
		if(list.size()>0)
			account=list.get(0);
		return account;
	}

	@Override
	public Page getCustomersQRCode(QRCodeDTO qrCodeDTO, Integer pageNumber) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Customer.class, "cust");
		if(StringUtils.isNotBlank(qrCodeDTO.getCustType()))
			criteria.add(Restrictions.eq("cust.type",Integer.parseInt(qrCodeDTO.getCustType())));
		else
			criteria.add(Restrictions.ne("cust.type",EOTConstants.CUSTOMER_TYPE_CUSTOMER));
		if(StringUtils.isNotEmpty(qrCodeDTO.getState()))
			criteria.add(Restrictions.eq("cust.city.cityId",Integer.parseInt(qrCodeDTO.getState())));
		if(StringUtils.isNotEmpty(qrCodeDTO.getCity()))
			criteria.add(Restrictions.eq("cust.quarter.quarterId",Long.parseLong(qrCodeDTO.getCity())));
		if(StringUtils.isNotEmpty(qrCodeDTO.getAgentCode()))
			criteria.add(Restrictions.eq("cust.agentCode",qrCodeDTO.getAgentCode()));
		if(StringUtils.isNotEmpty(qrCodeDTO.getMobileNumber()))
			criteria.add(Restrictions.eq("cust.mobileNumber",qrCodeDTO.getMobileNumber()));


		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		criteria.addOrder(Order.desc("createdDate"));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = session.createCriteria(Customer.class, "cust");
		if(StringUtils.isNotBlank(qrCodeDTO.getCustType()))
			criteria1.add(Restrictions.eq("cust.type",Integer.parseInt(qrCodeDTO.getCustType())));
		else
			criteria1.add(Restrictions.ne("cust.type",EOTConstants.CUSTOMER_TYPE_CUSTOMER));
		if(StringUtils.isNotEmpty(qrCodeDTO.getState()))
			criteria1.add(Restrictions.eq("cust.city.cityId",Integer.parseInt(qrCodeDTO.getState())));
		if(StringUtils.isNotEmpty(qrCodeDTO.getCity()))
			criteria1.add(Restrictions.eq("cust.quarter.quarterId",Long.parseLong(qrCodeDTO.getCity())));
		if(StringUtils.isNotEmpty(qrCodeDTO.getAgentCode()))
			criteria1.add(Restrictions.eq("cust.agentCode",qrCodeDTO.getAgentCode()));
		if(StringUtils.isNotEmpty(qrCodeDTO.getMobileNumber()))
			criteria1.add(Restrictions.eq("cust.mobileNumber",qrCodeDTO.getMobileNumber()));		criteria1.addOrder(Order.desc("createdDate"));
			/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

			return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}


	@Override
	@Transactional
	public List<Object[]> loadTotalCommissionByAgentId(Long agentId, String date,String bankRevnueAccount)
	{
		Query query=getSessionFactory().getCurrentSession().createSQLQuery("SELECT txn.TransactionType,txnJr.Amount,txn.transactionID "
				+ "FROM TransactionJournals txnJr INNER JOIN Transactions txn ON txnJr.TransactionID = txn.transactionID "
				+ "WHERE txn.ReferenceId=:ReferenceId and txn.transactionDate LIKE:date AND txnJr.creditAccount=:bankRevnueAccount "
				+ "AND txn.commissionStatus IS NULL and txn.Status=:status")
				.setParameter("date","%"+date+"%")
				.setParameter("ReferenceId", agentId)
				.setParameter("bankRevnueAccount", bankRevnueAccount)
				.setParameter("status", EOTConstants.TXN_NO_ERROR);
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<CommisionSplits> getCommissionPercentage(Integer txnType){

		List<CommisionSplits> commisionSplits = (List<CommisionSplits>)getHibernateTemplate()
				.find("from CommisionSplits cs where cs.transactionType.transactionType = ?",txnType);

		return commisionSplits.size() >0 ? commisionSplits : null ;

	}
	
	@SuppressWarnings("unchecked")
	public Double getCommissionPercentageAgent(Integer txnType){

		List<CommisionSplits> commisionSplits = (List<CommisionSplits>)getHibernateTemplate()
				.find("from CommisionSplits cs where cs.transactionType.transactionType = ? and cs.businessPartner = ?",txnType,"AGENT");

		return commisionSplits.size() >0 ? commisionSplits.get(0).getCommisionPct() : 0D ;

	}

	@Override
	public Page searchCustomerWithTxnDate(Integer bankId, Integer bankGroupId, String firstName, String middleName,String lastName, String mobileNumber, String bankID2, String branchID, String countryId, String fromDate,
			String toDate, Integer pageNumber, String custType, Long brId, Integer partnerId, String onBoardedBy,
			String businessName) {
		
		StringBuffer qryStr = null;
		Integer countryID = EOTConstants.COUNTRY_SOUTH_SUDAN;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		qryStr = new StringBuffer("SELECT distinct(transactions.TransactionID), customer.FirstName,customer.MobileNumber,customer.AgentCode,customer.customerKycStatus,customer.businessName,customer.partnerId,customer.gender,transactions.TransactionDate,transactions.status,transactions.requestChannel,bank.BankName,branch.Location,country.CountryID," +
				"customer.LastName,(SELECT amount FROM TransactionJournals WHERE TransactionID=transactions.TransactionID" +
				" AND JournalType=:journalType) AS SC,(SELECT SUM(sj.amount) FROM SettlementJournals sj WHERE sj.TransactionID=transactions.TransactionID AND sj.BookID=:bookID2) AS bankShare," +
				"(SELECT SUM(sj.amount) FROM SettlementJournals sj WHERE sj.TransactionID=transactions.TransactionID AND sj.BookID=:bookID3) AS gimShare FROM" +
				" Transactions AS transactions INNER JOIN Customer AS customer ON transactions.ReferenceId=customer.CustomerID JOIN CustomerAccounts AS custacc" +
				" ON customer.CustomerID=custacc.CustomerID JOIN Bank AS bank ON custacc.BankID=bank.BankID JOIN Branch AS branch ON custacc.BranchID=branch.BranchID" +
				" JOIN Country AS country ON bank.CountryID=country.CountryID");

		if(mobileNumber != null && !"".equals(mobileNumber)){	

			qryStr.append(" and customer.MobileNumber=:mobileNumber");
		}	
		
		if(onBoardedBy != null && !"".equals(onBoardedBy)){	

			qryStr.append(" and customer.onBoardedBy=:onBoardedBy");
		}	
		
//		if(null!=txnSummaryDTO.getTransactionType() && txnSummaryDTO.getTransactionType().intValue()==120)//120 is for commission
//			qryStr.append(" join Account acc on acc.accountNumber= custacc.accountNumber and (acc.aliasType=1 or acc.aliasType=10 ) ");
//		else
//			qryStr.append(" join Account acc on acc.accountNumber= custacc.accountNumber and acc.aliasType=1");
		
		if(countryId!= null && !"".equals(countryId.toString())){

			qryStr.append(" and country.CountryID=:countryID");
		}
		if(bankId != null && !"".equals(bankId.toString())){

			qryStr.append(" and bank.BankID=:bankID");   
		}
		if(branchID!= null && !"".equals(branchID.toString())){

			qryStr.append(" and branch.BranchID=:branchID");  
		} 
		if(bankGroupId!= null && !"".equals(bankGroupId.toString())){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupID");  
		} 
		/*if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){

			qryStr.append(" JOIN WebRequests as webrequests ON transactions.TransactionID=webrequests.TransactionID and webrequests.UserName like:userID");  
		}
		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId().toString())){

			qryStr.append(" and customer.ProfileID =:ProfileID");  
		} */
		if(bankGroupId != null){
			qryStr.append(" JOIN BankGroups as bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID=:bankGroupID"); 
		} 
		if(bankId != null){

			qryStr.append(" and bank.BankID=:bankID");   
		} 
		if(branchID != null){

			qryStr.append(" and branch.BranchID=:branchID");   
		} 

		/*Author name <vinod joshi>, Date<6/22/2018>, purpose of change <Date format is not working > ,*/
		/*Start*/
//		if(txnSummaryDTO!=null){
//			if(txnSummaryDTO.getTransactionType()==null){
//				qryStr.append(" and transactions.TransactionType !=:Txn1 and transactions.TransactionType !=:Txn2 and transactions.TransactionType !=:Txn3 and transactions.TransactionType !=:Txn4 and transactions.TransactionType !=:Txn5");
//			}
//		}
		/*End*/
		
		/*if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getTransactionType()==null){
				qry.append(" and transactions.TransactionType != 31 ");
			}
		}*/

//		if(txnSummaryDTO!=null){
//			if(txnSummaryDTO.getTransactionType()!=null){
//				qryStr.append(" and transactions.TransactionType=:Txn");
//			}
//		}

		
			if(fromDate!=null){
				qryStr.append(" where DATE(transactions.TransactionDate)>=:fromDate and DATE(transactions.TransactionDate)<=:toDate ");
			}
		

		if(StringUtils.isNotEmpty(partnerId.toString())){	
			
			qryStr.append(" and customer.partnerId=:partnerID");
		}	
		
//		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
//
//			qryStr.append(" and customer.AgentCode=:agentCode");   
//		}
//		
//		qryStr.append(" and customer.ProfileID=custprof.ProfileID"); 
		
//		if(txnSummaryDTO.getStatus() != null && txnSummaryDTO.getStatus().equals("2"))
//			qryStr.append(" and transactions.Status!=:status");   
//		else 
//			qryStr.append(" and transactions.Status=:status"); 	
//		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
//			qryStr.append(" and transactions.requestChannel=:requestChannel"); 
//		}
//		if(txnSummaryDTO!=null){
//			if(StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber()) && StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber())){
//				qryStr.append(" AND transactions.CustomerAccount=:accNumber");
//			}
//		}
//		if (null!= txnSummaryDTO.getSortBy() && txnSummaryDTO.getSortBy().equals("asc")) {
//			qryStr.append("  ORDER BY ")
//			       .append(txnSummaryDTO.getSortColumn())
//			       .append(" ASC");
//		} else {
//			qryStr.append("  ORDER BY ")
//		       .append(txnSummaryDTO.getSortColumn())
//		       .append(" DESC");
//		}
		//qryStr.append("  ORDER BY TransactionDate DESC");

		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString()); 
		    qryResult1.setParameter("journalType", 1);
		    qryResult1.setParameter("bookID2", 2);
		    qryResult1.setParameter("bookID3", 3);
		if(mobileNumber != null && !"".equals(mobileNumber)){{
		    qryResult1.setParameter("mobileNumber", mobileNumber);
		   }
		}
		if(onBoardedBy != null && !"".equals(onBoardedBy)){	

			qryStr.append(" and customer.onBoardedBy=:onBoardedBy");
		}	
		if(countryId != null && !"".equals(countryId)){
		    qryResult1.setParameter("countryID", countryId);	
		}
		if(bankId != null && !"".equals(bankId.toString())){
			qryResult1.setParameter("bankID", bankId);
		}
		if(branchID != null && !"".equals(branchID)){
			qryResult1.setParameter("branchID", branchID);
		}

			if(fromDate!=null){
				qryResult1.setParameter("fromDate", DateUtil.formatDate(fromDate));
			}
			if(toDate!=null){
				qryResult1.setParameter("toDate", DateUtil.formatDate(toDate));
			}
		
		if(bankGroupId != null && !"".equals(bankGroupId)){
			qryResult1.setParameter("bankGroupID",bankGroupId);
		}
//		if(txnSummaryDTO.getUserId() != null && !"".equals(txnSummaryDTO.getUserId())){
//			qryResult1.setParameter("userID",txnSummaryDTO.getUserId()+"%");
//			}
//		if(txnSummaryDTO.getProfileId() != null && !"".equals(txnSummaryDTO.getProfileId())){
//				qryResult1.setParameter("ProfileID",txnSummaryDTO.getProfileId());
//			}
//		if(bankGroupId != null){
//				qryResult1.setParameter("bankGroupID", bankGroupId);
//		}
		if(bankId != null){
			qryResult1.setParameter("bankID", bankId);
		}
		if(branchID != null){
			qryResult1.setParameter("branchID", branchID);
		}
//		if(txnSummaryDTO!=null){
//			if(txnSummaryDTO.getTransactionType()==null){
//			qryResult1.setParameter("Txn1", 60);
//			qryResult1.setParameter("Txn2", 84);
//			qryResult1.setParameter("Txn3", 31);
//			qryResult1.setParameter("Txn4", 137);
//			qryResult1.setParameter("Txn5", 138);
//		}
//		}
		
//		if(txnSummaryDTO!=null){
//			if(txnSummaryDTO.getTransactionType()!=null){
//				qryResult1.setParameter("Txn", txnSummaryDTO.getTransactionType());
//		}
//		}
		
		
		if(StringUtils.isNotEmpty(partnerId.toString())){
			qryResult1.setParameter("partnerID", partnerId.toString());
		}
//		if(txnSummaryDTO.getAgentCode() != null && !"".equals(txnSummaryDTO.getAgentCode())){
//			qryResult1.setParameter("agentCode", txnSummaryDTO.getAgentCode());
//		}
		qryResult1.setParameter("status", EOTConstants.TXN_NO_ERROR);
//		if(txnSummaryDTO.getRequestChannel() != null && !"".equals(txnSummaryDTO.getRequestChannel())){
//			
//			switch(txnSummaryDTO.getRequestChannel()) {
//			case "1": qryResult1.setParameter("requestChannel", "Web");
//						break;
//			case "2": qryResult1.setParameter("requestChannel", "Mobile"); 
//						break;
//			case "3": qryResult1.setParameter("requestChannel", "USSD");
//						break;
//			}						
//		}	
//		if(txnSummaryDTO!=null){ 
//			if(StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber()) && StringUtils.isNotEmpty(txnSummaryDTO.getAccountNumber())){
//				qryResult1.setParameter("accNumber", txnSummaryDTO.getAccountNumber());
//			}
//		}
//		if (!EOTConstants.ACTION_EXPORT.equals(txnSummaryDTO.getActionType())) {
//			qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
//			qryResult1.setMaxResults(appConfig.getResultsPerPage());
//		}
		
		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(qryResult1.list(),  appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page searchBlockedCustomers(String fromDate, String toDate, Integer pageNumber, String custType) {
		
//		StringBuffer qry = new StringBuffer("SELECT cust.* FROM Customer cust \n" + 
//				"JOIN AppMaster app ON app.appId = cust.appId\n" + 
//				"WHERE app.status =:status and cust.type=:custType");
		StringBuffer qry = new StringBuffer("SELECT CONCAT(cust.FirstName,\"\",cust.LastName) AS CustomerName,cust.CustomerID, cust.MobileNumber AS MobileNumber,\n" + 
				"cust.Active,cust.Gender AS Gender,cust.CreatedDate,country1_.Country AS Country,city1_.City,cust.Type,cust.AgentCode,cust.BusinessName,cust.reason_for_block,\n" + 
				"cust.OnbordedBy,CONCAT(agent.FirstName,\"\",agent.LastName) AS AgentName,bp.Code,bp.Name,\n" + 
				"cust.kyc_status FROM Customer cust JOIN Country country1_ ON cust.CountryID = country1_.CountryID \n" + 
				"JOIN City city1_ ON cust.CityID = city1_.CityID \n" + 
				"LEFT JOIN WebUsers wu ON cust.OnbordedBy = wu.UserName\n" + 
				"LEFT JOIN Customer agent ON cust.OnbordedBy = agent.AgentCode \n" + 
				"LEFT JOIN BusinessPartner bp ON cust.PartnerId = bp.Id\n" + 
				"JOIN AppMaster app ON app.appId = cust.appId\n" + 
				"WHERE cust.Type =:custType AND app.status =:status");
		if( StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
			qry.append(" and DATE(cust.CreatedDate)>=:fromDate and DATE(cust.CreatedDate)<=:toDate ");
		}
		qry.append("  ORDER BY cust.CreatedDate DESC");

		SQLQuery qryResult = getSessionFactory().getCurrentSession().createSQLQuery(qry.toString());
		
		qryResult.setParameter("status",EOTConstants.APP_STATUS_BLOCKED);
		qryResult.setParameter("custType", custType);
		if( StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
			qryResult.setParameter("fromDate", DateUtil.formatDateWithSlash(fromDate,0,0,0));
			qryResult.setParameter("toDate",  DateUtil.formatDateWithSlash(toDate,0,0,0));
		}

		qryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(qryResult.list(), qryResult.list().size(), appConfig.getResultsPerPage(), pageNumber);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> getCustomerEnrolledUnder90DaysByKYCStatus(Integer kycStatus) {
		StringBuilder queryBuilder = new StringBuilder("select * from  Customer where createdDate <= :createdDate AND kyc_status!=:kycRejected AND kyc_status != :kycApproved");
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryBuilder.toString());
		query.setParameter("createdDate", DateUtil.formatDate(DateUtil.addDays(new Date(), -90))).setParameter("kycRejected", EOTConstants.KYC_STATUS_REGEJETED).setParameter("kycApproved", kycStatus);
		return CollectionUtils.isNotEmpty(query.list())? query.list():null;
	}

	@Override
	public void updateCustomer(Long customerId, Integer status) {
		StringBuilder updateBuilder = new StringBuilder("UPDATE Customer SET Active =:activeStatus WHERE CustomerID=:customerId");
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(updateBuilder.toString());
		query.setParameter("activeStatus", status).setParameter("customerId", customerId);
		query.executeUpdate();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> lowBlanceCheckForAgent() {
		StringBuilder queryBuilder = new StringBuilder("SELECT cust.MobileNumber, cust.CustomerID, AuthorizedAmount, CurrentBalance, ROUND(( AuthorizedAmount*25/100 ),0) AS RecomandedBalance, (CurrentBalance < ROUND(( AuthorizedAmount*25/100 ),0)) AS IsBalanceShortage "
				+ "FROM  Customer cust "
				+ "INNER JOIN CustomerProfiles cp ON cp.ProfileID=cust.ProfileID "
				+ "INNER JOIN Account acc ON acc.ReferenceID = cust.CustomerID AND cust.Type=:custType AND acc.Alias=:accAlias");
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(queryBuilder.toString());
		query.setParameter("custType", 1).setParameter("accAlias", "m-Gurush Payments - mGurush");
		return CollectionUtils.isNotEmpty(query.list())? query.list():null;
	}
	
	@Override
	public Boolean getMerchant(Integer custType,String merchantCode) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("FROM Customer Where Type=:custType and AgentCode=:merchantCode");
		query.setParameter("custType", custType);
		query.setParameter("merchantCode", merchantCode);
		List list = query.list();
		return CollectionUtils.isNotEmpty(query.list())? true:false;
	}

	@Override
	public Page searchAgentCashOutData(TransactionParamDTO transactionParamDTO, Integer pageNumber) {
		
		StringBuffer qry = new StringBuffer("select * FROM PendingTransactions pt where pt.Status=:status");
		if(null != transactionParamDTO) {
			if(null != transactionParamDTO.getFromDate() && null != transactionParamDTO.getToDate()){
				qry.append(" and DATE(pt.TransactionDate)>=:fromDate and DATE(pt.TransactionDate)<=:toDate");
			}
			if( StringUtils.isNotBlank(transactionParamDTO.getCustomerCode())){
				qry.append(" and pt.CustomerCode=:customerCode");
			}
			if( StringUtils.isNotBlank(transactionParamDTO.getCustomerMobileNo())){
				qry.append(" and pt.CustomerMobileNo=:customerMobileNo");
			}		
		}
		qry.append(" ORDER BY pt.TransactionDate DESC");

		SQLQuery qryResult = getSessionFactory().getCurrentSession().createSQLQuery(qry.toString());
		
		qryResult.setParameter("status", EOTConstants.TRANSACTION_INITIATED_FOR_APPROVAL);
		if( StringUtils.isNotBlank(transactionParamDTO.getCustomerCode())){
			qryResult.setParameter("customerCode", transactionParamDTO.getCustomerCode());
		}
		if( StringUtils.isNotBlank(transactionParamDTO.getCustomerMobileNo())){
			qryResult.setParameter("customerMobileNo", transactionParamDTO.getCustomerMobileNo());
		}
		if(null != transactionParamDTO.getFromDate() && null != transactionParamDTO.getToDate()){
			qryResult.setParameter("fromDate", transactionParamDTO.getFromDate());
			qryResult.setParameter("toDate",  transactionParamDTO.getToDate());
		}

		qryResult.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return PaginationHelper.getPage(qryResult.list(), qryResult.list().size(), appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public PendingTransaction getPendingTransaction(Long transactionId) {
		return (PendingTransaction)getHibernateTemplate().getSessionFactory().getCurrentSession().get(PendingTransaction.class, transactionId);
	}
		
}