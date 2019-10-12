package com.eot.banking.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.Type;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.WebUserDao;
import com.eot.banking.dto.ForgotPasswordDTO;
import com.eot.banking.dto.OtpDTO;
import com.eot.banking.dto.TxnSummaryDTO;
import com.eot.banking.dto.WebUserDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Bank;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.BusinessPartnerUser;
import com.eot.entity.Country;
import com.eot.entity.Otp;
import com.eot.entity.WebRequest;
import com.eot.entity.WebUser;
import com.eot.entity.WebUserRole;


public class WebUserDaoImpl extends BaseDaoImpl implements WebUserDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<WebUserRole> getRoleList() {
		Query query=getSessionFactory().getCurrentSession().createQuery("from WebUserRole");
		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getUserList(Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria1.addOrder(Order.asc("userName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public WebUser getUser(String userName) {
		return getHibernateTemplate().get(WebUser.class,userName);
	}
	@SuppressWarnings("all")
	@Override
	public BusinessPartner getBusinessPartnerByUserName(String userName) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from BusinessPartnerUser as businesspartuser where businesspartuser.userName=:userName")
				.setParameter("userName", userName).setCacheable(true);
		List<BusinessPartnerUser> list = query.list();
		BusinessPartner bp = list.get(0).getBusinessPartner();
		System.out.println(bp.getSeniorPartner());
		return list.size()>0 ? list.get(0).getBusinessPartner() : null ;
	}

//	@Override
//	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
//
//		WebUser webUser = getHibernateTemplate().get(WebUser.class,userName);
//
//		if(webUser!=null){
//			return new WebUserAcegi(webUser);
//		}else{
//			throw new UsernameNotFoundException("ERROR_5010");
//		}
//
//	}

	@SuppressWarnings("unchecked")
	@Override
	public BankTellers getBankTeller(String userName) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from BankTellers bt where bt.userName=:userName")
				.setParameter("userName", userName ) ;
		List user= query.list();

		return (BankTellers) (user.size()> 0 ? user.get(0) : null) ;
	}

	@SuppressWarnings("unchecked")
	public Page getUsersByBankId(Integer bankId,Integer pageNumber){	

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BankTellers.class);
		criteria.add( Restrictions.eq("bank.bankId",bankId))
		.setProjection(Projections.projectionList().add(Projections.property("webUser")));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(BankTellers.class);
		criteria1.add( Restrictions.eq("bank.bankId",bankId))
		.setProjection(Projections.projectionList().add(Projections.property("webUser")));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getCCUsers(Integer pageNumber) {
		/*
	Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from WebUser user where user.webUserRole.roleId="+ EOTConstants.ROLEID_CC_ADMIN+
												" or user.webUserRole.roleId=" + EOTConstants.ROLEID_CC_EXECUTIVE+"  order by lower(user.userName)");*/

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		// criteria.add( Restrictions.eq("webUserRole.roleId",EOTConstants.ROLEID_CC_ADMIN));
		criteria.add( Restrictions.or(Restrictions.eq("webUserRole.roleId",EOTConstants.ROLEID_CC_EXECUTIVE), Restrictions.eq("webUserRole.roleId",EOTConstants.ROLEID_CC_ADMIN)));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		// criteria1.add( Restrictions.eq("webUserRole.roleId",EOTConstants.ROLEID_CC_ADMIN));
		criteria1.add( Restrictions.or(Restrictions.eq("webUserRole.roleId",EOTConstants.ROLEID_CC_EXECUTIVE), Restrictions.eq("webUserRole.roleId",EOTConstants.ROLEID_CC_ADMIN)));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Otp verifyOTP(OtpDTO otpDTO) {

		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from Otp as otp where otp.comp_id.referenceId=:referenceId and otp.comp_id.referenceType=:referenceType and otp.otpHash=:otpHash and otp.otpType=:otpType")
				.setParameter("referenceId", otpDTO.getReferenceId() )
				.setParameter("referenceType", otpDTO.getReferenceType() )
				.setParameter("otpHash", otpDTO.getOtphash() )
				.setParameter("otpType", otpDTO.getOtpType() );
		List<Otp> list= query.list();

		return list.size()>0 ? list.get(0) : null ;

	}
	@SuppressWarnings("unchecked")
	public Page getTxnSummary(TxnSummaryDTO txnSummaryDTO,Integer pageNumber){

		String str="";
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getBankId()!=null){
				str=" and txn.CustomerAccount in(select AccountNumber from CustomerAccounts where BankID=:bankID)";
			}
			if(txnSummaryDTO.getCountryId()!=null){
				str+=" and txn.CustomerAccount in(select AccountNumber from CustomerAccounts where BankID in(select BankID from Bank where CountryID=:countryID))";
			}
			if(txnSummaryDTO.getTransactionType()!=null){
				str+=" and ttypes.TransactionType=:txnType";
			}
			if(txnSummaryDTO.getFromDate()!=null){
				str+=" and DATE(txn.TransactionDate)>=:fromDate";
			}
			if(txnSummaryDTO.getToDate()!=null){
				str+=" and DATE(txn.TransactionDate)<=:toDate";
			}
		}
		StringBuffer sb=new StringBuffer();
		sb.append("select count(*) from (select TransactionID from (select txn.TransactionID,ttypes.Description,acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,");
		sb.append("txn.Status from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,Account acc where txn.TransactionID=txj.TransactionID and txj.CreditAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and "); 
		sb.append("txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType and txn.TransactionType=ttypes.TransactionType");
		sb.append(str);

		sb.append(" union all select txn.TransactionID,ttypes.Description, acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,txn.Status ");
		sb.append("from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,Account acc where ");
		sb.append("txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and txn.TransactionType=ttypes.TransactionType and ");
		sb.append("txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType"); 
		sb.append(str);

		sb.append(" union all select txn.TransactionID,ttypes.Description, acc.Alias as Alias,txn.TransactionDate, 0 as Amount, sum(txj.amount) as SC,txn.Status "); 
		sb.append("from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,Account acc where ");
		sb.append("txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and txn.TransactionType=ttypes.TransactionType and ");
		sb.append("txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType1");
		sb.append(str);

		sb.append(" group by txn.TransactionID)rslt group by TransactionID order by TransactionID desc)totalres");

		SQLQuery query1 = getSessionFactory().getCurrentSession().createSQLQuery( sb.toString() );
		
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getBankId()!=null){
		  query1.setParameter("bankID", txnSummaryDTO.getBankId());
		}}
		if(txnSummaryDTO.getCountryId()!=null){
		  query1.setParameter("countryID", txnSummaryDTO.getCountryId());
		}
		if(txnSummaryDTO.getTransactionType()!=null){
		  query1.setParameter("txnType", txnSummaryDTO.getTransactionType());
		}
		if(txnSummaryDTO.getFromDate()!=null){
		  query1.setParameter("fromDate",DateUtil.formatDate(txnSummaryDTO.getFromDate()));	
		}
		if(txnSummaryDTO.getToDate()!=null){
		  query1.setParameter("toDate",DateUtil.formatDate(txnSummaryDTO.getToDate()));	
		}
		  query1.setParameter("status", EOTConstants.TXN_NO_ERROR);
	      query1.setParameter("transactionType", 84);
		  query1.setParameter("JournalType", 0);
		  query1.setParameter("JournalType1", 1);

		int totalCount = Integer.parseInt(query1.list().get(0).toString());

		sb=new StringBuffer();
		sb.append("select TransactionID as txnId,Description as txnName,Alias as alias,TransactionDate as txnDate,sum(Amount) as txnAmount,sum(SC) as servCharge,Status as txnStatus from ");
		sb.append("(select txn.TransactionID,ttypes.Description,acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,");
		sb.append("txn.Status from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,Account acc where txn.TransactionID=txj.TransactionID and txj.CreditAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and "); 
		sb.append("txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType and txn.TransactionType=ttypes.TransactionType");
		sb.append(str);

		sb.append(" union all select txn.TransactionID,ttypes.Description, acc.Alias as Alias,txn.TransactionDate, txj.Amount as Amount , 0 as SC ,txn.Status ");
		sb.append("from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,Account acc where ");
		sb.append("txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and txn.TransactionType=ttypes.TransactionType and ");
		sb.append("txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType"); 
		sb.append(str);

		sb.append(" union all select txn.TransactionID,ttypes.Description, acc.Alias as Alias,txn.TransactionDate, 0 as Amount, sum(txj.amount) as SC,txn.Status "); 
		sb.append("from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,Account acc where ");
		sb.append("txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and txn.TransactionType=ttypes.TransactionType and ");
		sb.append("txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType1");
		sb.append(str);

		sb.append(" group by txn.TransactionID)rslt group by TransactionID order by TransactionID desc");

		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery( sb.toString() );
		
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getBankId()!=null){
			query.setParameter("bankID", txnSummaryDTO.getBankId());
		}}
		if(txnSummaryDTO.getCountryId()!=null){
			query.setParameter("countryID", txnSummaryDTO.getCountryId());
		}
		if(txnSummaryDTO.getTransactionType()!=null){
			query.setParameter("txnType", txnSummaryDTO.getTransactionType());
		}
		if(txnSummaryDTO.getFromDate()!=null){
			query.setParameter("fromDate",DateUtil.formatDate(txnSummaryDTO.getFromDate()));	
		}
		if(txnSummaryDTO.getToDate()!=null){
			query.setParameter("toDate",DateUtil.formatDate(txnSummaryDTO.getToDate()));	
		}
		
		query.setParameter("status", EOTConstants.TXN_NO_ERROR);
		query.setParameter("transactionType", 84);
		query.setParameter("JournalType", 0);
		query.setParameter("JournalType1", 1);

		query.addScalar("txnId",Hibernate.LONG);
		query.addScalar("txnName",Hibernate.STRING);
		query.addScalar("alias",Hibernate.STRING);
		query.addScalar("txnDate",Hibernate.TIMESTAMP);
		query.addScalar("txnAmount",Hibernate.STRING);
		query.addScalar("servCharge",Hibernate.DOUBLE);
		query.addScalar("txnStatus",Hibernate.INTEGER);

		//query.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//query.setMaxResults(appConfig.getResultsPerPage());

		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();


		return PaginationHelper.getPage(list, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public List getTxnSummaryList(TxnSummaryDTO txnSummaryDTO) {

		String str="";
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getBankId()!=null){
				str=" and txn.CustomerAccount in(select AccountNumber from CustomerAccounts where BankID=:bankID)";
			}
			if(txnSummaryDTO.getCountryId()!=null){
				str+=" and txn.CustomerAccount in(select AccountNumber from CustomerAccounts where BankID in(select BankID from Bank where CountryID=:countryID))";
			}
			if(txnSummaryDTO.getTransactionType()!=null){
				str+=" and ttypes.TransactionType=:txnType";
			}
			if(txnSummaryDTO.getFromDate()!=null){
				str+=" and DATE(txn.TransactionDate)>=:fromDate";
			}
			if(txnSummaryDTO.getToDate()!=null){
				str+=" and DATE(txn.TransactionDate)<=:toDate";
			}
		}
		StringBuffer sb=new StringBuffer();

		sb.append("select count(txnId) as noOfTrns,txnName,sum(txnAmount) as sum from "); 
		sb.append("(select TransactionID as txnId,Description as txnName,sum(Amount) as txnAmount,sum(SC) as servCharge from "); 
		sb.append("(select txn.TransactionID,ttypes.Description,txj.Amount as Amount , 0 as SC from TransactionTypes "); 
		sb.append("ttypes,TransactionJournals txj,Transactions txn,Account acc where txn.TransactionID=txj.TransactionID and ");
		sb.append("txj.CreditAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType and txn.TransactionType=ttypes.TransactionType"); 
		sb.append(str);

		sb.append(" union all select txn.TransactionID,ttypes.Description,txj.Amount as Amount , 0 as SC from TransactionTypes ttypes,TransactionJournals "); 
		sb.append("txj,Transactions txn,Account acc where txn.TransactionID=txj.TransactionID and "); 
		sb.append("txj.DebitAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and txn.TransactionType=ttypes.TransactionType and txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType"); 
		sb.append(str);

		sb.append(" union all select txn.TransactionID,ttypes.Description, 0 as Amount,"); 
		sb.append("sum(txj.amount) as SC from TransactionTypes ttypes,TransactionJournals txj,Transactions txn,");
		sb.append("Account acc where txn.TransactionID=txj.TransactionID and txj.DebitAccount=acc.AccountNumber and acc.AccountNumber=txn.CustomerAccount and "); 
		sb.append("txn.TransactionType=ttypes.TransactionType and txn.Status=:status and txn.transactionType!=:transactionType and txj.JournalType=:JournalType1");
		sb.append(str);

		sb.append(" group by txn.TransactionID)rslt group by TransactionID order by TransactionID desc)totalresult group by txnName");

		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery( sb.toString() );
		if(txnSummaryDTO!=null){
			if(txnSummaryDTO.getBankId()!=null){
		  query.setParameter("bankID", txnSummaryDTO.getBankId());		
		}
		}
		if(txnSummaryDTO.getCountryId()!=null){
		  query.setParameter("countryID", txnSummaryDTO.getCountryId());	
		}
		if(txnSummaryDTO.getTransactionType()!=null){
		  query.setParameter("txnType", txnSummaryDTO.getTransactionType());
		}
		if(txnSummaryDTO.getFromDate()!=null){
			query.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getFromDate()));
		}
		if(txnSummaryDTO.getToDate()!=null){
			query.setParameter("fromDate", DateUtil.formatDate(txnSummaryDTO.getToDate()));
		}
		 query.setParameter("status", EOTConstants.TXN_NO_ERROR);
		 query.setParameter("transactionType", 84);
		 query.setParameter("JournalType", 0);
		 query.setParameter("JournalType1", 1);

		 query.addScalar("noOfTrns",Hibernate.LONG);
		 query.addScalar("txnName",Hibernate.STRING);
		 query.addScalar("sum",Hibernate.STRING);

		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	public BankGroupAdmin getbankGroupAdmin(String userName) {

		Session session = getSessionFactory().getCurrentSession();

		Query query = session.createQuery("from BankGroupAdmin ba where ba.userName=:userName")

				.setParameter("userName", userName ) ;

		List user= query.list();

		return (BankGroupAdmin) (user.size()> 0 ? user.get(0) : null) ;

	}

	public List<Bank> getBankByGroupId(Integer groupId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank b where b.bankGroup.bankGroupId=:groupId").setParameter("groupId", groupId).setCacheable(true);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Bank b where b.bankGroup.bankGroupId="+groupId).setCacheable(true);*/

		return query.list();

	}

	@Override
	public Page getUsersByBankGroupId(Integer bankGroupId, Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria.add(
				/**
				 * Bidyut: change done on query for sql Injection
				 */
				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID in(select BankID from Bank where BankGroupID=?))",bankGroupId,Hibernate.INTEGER)

				);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria1.add(

				/**
				 * Bidyut: change done on query for sql Injection
				 */
				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID in(select BankID from Bank where BankGroupID=?))",bankGroupId,Hibernate.INTEGER)

				);
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public WebUser getPassword(ForgotPasswordDTO passworDTO) {
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();


		Query query = session.createQuery("from WebUser as user where user.userName=:userName and user.mobileNumber=:mobileNumber and user.email=:email")
				.setParameter("userName", passworDTO.getUserName() )
				.setParameter("mobileNumber",passworDTO.getMobileNumber())
				.setParameter("email",passworDTO.getEmail());

		List<WebUser> list= query.list();

		return list.size()>0 ? list.get(0) : null ;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WebUser> getWebUserList() {	

		List<WebUser> list = getHibernateTemplate().find("select wb.userName from WebUser wb");
		return list.size()>0 ? list : null ;
	}

	@Override
	public String getWebUserId(Integer roleId) {
		
		/*Query query = getSessionFactory().getCurrentSession()
				.createSQLQuery("SELECT MAX(SUBSTRING(UserName,-6,6))+1 AS seq FROM WebUsers wu where wu.webUserRole.roleId=:roleId")
				.addScalar("seq",Hibernate.LONG);
		return query.list().get(0).toString();*/
		
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("SELECT MAX(SUBSTRING(UserName,-6,6))+1 AS seq FROM WebUser wu where wu.webUserRole.roleId=:roleId")
				.setParameter("roleId", roleId);
				List list = query.list();
				return list.get(0) == null ? "1" :list.get(0).toString();
	}

	@Override
	public List<Country> getCountryList(String language) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("select country from Country as country,CountryNames as countryNames  where country.countryId= countryNames.comp_id.countryId and countryNames.comp_id.languageCode=:language order by lower(countryNames.countryName)").setParameter("language", language).setCacheable(true);
		//@end
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select country from Country as country,CountryNames as countryNames  where country.countryId= countryNames.comp_id.countryId and countryNames.comp_id.languageCode='"+language+" ' order by lower(countryNames.countryName)").setCacheable(true);*/

		return query.list();
	}

	@Override
	public Page getSearchWebUserList(WebUserDTO webUserDTO, Integer pageNumber) {

		System.out.println("Web User name : " + webUserDTO.getStatus());
		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(WebUser.class);
		criteria.addOrder(Order.desc("createdDate"));

		
		/*Integer[] roleId = {EOTConstants.ROLEID_GIM_ADMIN,EOTConstants.ROLEID_SUPER_ADMIN,EOTConstants.ROLEID_CC_EXECUTIVE,EOTConstants.ROLEID_BANK_GROUP_ADMIN,EOTConstants.ROLEID_GIM_BACKOFFICE_LEAD,EOTConstants.ROLEID_GIM_BACKOFFICE_EXEC,EOTConstants.ROLEID_GIM_SUPPORT_LEAD,EOTConstants.ROLEID_GIM_SUPPORT_EXEC};
		criteria.add(

				Restrictions.in("webUserRole.roleId",roleId)
	);*/
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		
		/*criteria1.add(

				Restrictions.in("webUserRole.roleId",roleId)
	);*/

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria1.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria1.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria1.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria1.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		criteria1.addOrder(Order.desc("createdDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	
	public Page getUsersByBankId(Integer bankId,WebUserDTO webUserDTO,Integer pageNumber){	

		Criteria criteria1=null;
		Type []type= {Hibernate.INTEGER,Hibernate.INTEGER};
		Integer [] intValue= {bankId,bankId};
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria.add(

				/**
				 * Bidyut: change done on query for sql Injection
				 */
				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID =? union select UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id where  BusinessPartner.Bank=?)",intValue,type)

				);	
		Integer[] roleId = {EOTConstants.ROLEID_BANK_TELLER,EOTConstants.ROLEID_BRANCH_MANAGER,EOTConstants.ROLEID_RELATIONSHIP_MANAGER,EOTConstants.ROLEID_PERSONAL_RELATIONSHIP_EXCE,
				EOTConstants.ROLEID_BANK_ADMIN,EOTConstants.ROLEID_SUPPORT_BANK,EOTConstants.ROLEID_PARAMETER,EOTConstants.ROLEID_AUDIT,EOTConstants.ROLEID_ACCOUNTING,
				EOTConstants.ROLEID_INFORMATION_DESK,EOTConstants.ROLEID_OPERATIONS,EOTConstants.ROLEID_BUSINESS_PARTNER_L1,EOTConstants.ROLEID_BUSINESS_PARTNER_L2,
				EOTConstants.ROLEID_BUSINESS_PARTNER_L3,EOTConstants.ROLEID_SALES_OFFICERS,EOTConstants.ROLEID_BULKPAYMENT_PARTNER,EOTConstants.ROLEID_COMMERCIAL_OFFICER,EOTConstants.ROLEID_SUPPORT_CALL_CENTER};
				
		criteria.add(

				Restrictions.in("webUserRole.roleId",roleId)
	);
		
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria1.add(
				/**
				 * Bidyut: change done on query for sql Injection
				 */
				//Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID =?)",bankId,Hibernate.INTEGER)
				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID =? union select UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id where  BusinessPartner.Bank=?)",intValue,type)
				);
		criteria1.add(

				Restrictions.in("webUserRole.roleId",roleId)
	);
		
		

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria1.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria1.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria1.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria1.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		//criteria1.addOrder(Order.asc("userName"));
		criteria1.addOrder(Order.desc("createdDate"));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
	//	criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public List<Bank> getBanksByCountry(Integer countryId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank b where b.country.countryId=:countryId").setParameter("countryId", countryId).setCacheable(true);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Bank b where b.country.countryId="+countryId).setCacheable(true);*/
		return query.list();

		
	}

	@Override
	public WebRequest getUser(Long transactionId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query = getSessionFactory().getCurrentSession().createQuery("from WebRequest wr where wr.transaction.transactionId=:transactionId").setParameter("transactionId", transactionId).setCacheable(true);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from WebRequest wr where wr.transaction.transactionId="+transactionId).setCacheable(true);*/
		return query.list().size()>0 ? (WebRequest) query.list().get(0) : null;
	}

	@Override
	public Page getUsersByType(Integer bankId, WebUserDTO webUserDTO,int pageNumber) {
		Criteria criteria1=null;
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria.add(
				/**
				 * Bidyut: change done on query for sql Injection
				 */
				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID =?)",bankId,Hibernate.INTEGER)

				);
		Integer[] roleId = {EOTConstants.ROLEID_SUPER_ADMIN,EOTConstants.ROLEID_BANK_ADMIN};
		criteria.add(

				Restrictions.in("webUserRole.roleId",roleId)
	);
		
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria1.add(

				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID ="+bankId+")")

				);
	
		criteria1.add(

				Restrictions.in("webUserRole.roleId",roleId)
	);
		

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria1.add(Restrictions.sqlRestriction("userName like '%"+webUserDTO.getUserName()+"%'"));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria1.add(Restrictions.sqlRestriction("email like '%"+webUserDTO.getEmail()+"%'"));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria1.add(Restrictions.sqlRestriction("mobileNumber like '%"+webUserDTO.getMobileNumber()+"%'"));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria1.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		criteria1.addOrder(Order.asc("userName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@Override
	public Page getUserByBankGroupId(Integer bankGroupId, int pageNumber, WebUserDTO webUserDTO) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria.add(

				/**
				 * Bidyut: chage done on query for sql injection
				 */
				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID in(select BankID from Bank where BankGroupID=?))",bankGroupId,Hibernate.INTEGER)

				);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		
		
		
		

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%", Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}
		
		
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria1.add(

				Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID in(select BankID from Bank where BankGroupID=?))",bankGroupId,Hibernate.INTEGER)

				);
		
		
		

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria1.add(Restrictions.sqlRestriction("userName like ?",webUserDTO.getUserName()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria1.add(Restrictions.sqlRestriction("email like ?",webUserDTO.getEmail()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria1.add(Restrictions.sqlRestriction("mobileNumber like ?",webUserDTO.getMobileNumber()+"%",Hibernate.STRING));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria1.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Integer getRoleId(String userName) {
	//	Query query=getSessionFactory().getCurrentSession().createQuery("from Customer as cust order by lower(cust.firstName)");
		
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery("from WebUser as user where user.userName=:userName")
				.setParameter("userName", userName);				
		List<WebUser> list= query.list();
		return list.size()>0 ? list.get(0).getWebUserRole().getRoleId() : null ;
		
	}

	@Override
	public Page getWebUserByRoleID(Integer BProleID, Integer pageNumber, WebUserDTO webUserDTO, String seniorPartner,Integer id) {
		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Page page = null;
		try {
		Criteria criteria = session.createCriteria(WebUser.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		Integer seniorPartnerType = Integer.parseInt(seniorPartner);
		Integer[] itype= {seniorPartnerType,BProleID,seniorPartnerType,EOTConstants.ROLEID_SALES_OFFICERS};
		Type[] tipos = {Hibernate.INTEGER, Hibernate.INTEGER,Hibernate.INTEGER,Hibernate.INTEGER};
		
		if( BProleID!= null  ){
			criteria.add(
					/**
					 * @Bidyut: on Sql injection
					 */
			//		Restrictions.sqlRestriction("UserName in(select UserName from WebUsers where roleId =?)",BProleID,Hibernate.INTEGER)
					Restrictions.sqlRestriction("UserName in(select BusinessPartnerUser.UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id  inner join WebUsers  on BusinessPartnerUser.username = WebUsers.username where (BusinessPartner.seniorPartner=? and WebUsers.roleId=?) or (BusinessPartner.id=? and WebUsers.roleId=?))",itype, tipos)

					);
			//criteria.add(Restrictions.sqlRestriction("roleId like '%"+BProleID+"%'"));
		}
	/*	if( seniorPartner!= null  ){
		criteria1.add(				
				//Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID =?)",bankId,Hibernate.INTEGER)
			//	Restrictions.sqlRestriction("UserName in(select UserName from BankTellers where BankID =? union select UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id where  BusinessPartner.Bank=?)",intValue,type)
				
				Restrictions.sqlRestriction("UserName in(select UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id where BusinessPartnerUser.BusinessPartner.seniorPartner=?)",Integer.parseInt(seniorPartner),Hibernate.INTEGER)
				);
		}*/
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria.add(Restrictions.sqlRestriction("userName like '%"+webUserDTO.getUserName()+"%'"));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria.add(Restrictions.sqlRestriction("email like '%"+webUserDTO.getEmail()+"%'"));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria.add(Restrictions.sqlRestriction("mobileNumber like '%"+webUserDTO.getMobileNumber()+"%'"));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}		
		
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(WebUser.class);
		criteria1.addOrder(Order.desc("createdDate"));
		
		if( BProleID!= null  ){
			criteria1.add(

				//	Restrictions.sqlRestriction("UserName in(select UserName from WebUsers where roleId =?)",BProleID,Hibernate.INTEGER)
					//Restrictions.sqlRestriction("UserName in(select UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id where BusinessPartner.seniorPartner=?)",seniorPartnerType,Hibernate.INTEGER)
					Restrictions.sqlRestriction("UserName in(select BusinessPartnerUser.UserName from BusinessPartnerUser inner join BusinessPartner on BusinessPartnerUser.partnerId=BusinessPartner.id  inner join WebUsers  on BusinessPartnerUser.username = WebUsers.username where (BusinessPartner.seniorPartner=? and WebUsers.roleId=?) or (BusinessPartner.id=? and WebUsers.roleId=?))",itype, tipos)
					);
			//criteria.add(Restrictions.sqlRestriction("roleId like '%"+BProleID+"%'"));
		}

		if( webUserDTO.getUserName()!= null && ! "".equals(webUserDTO.getUserName()) ){
			criteria1.add(Restrictions.sqlRestriction("userName like '%"+webUserDTO.getUserName()+"%'"));
		}
		if( webUserDTO.getEmail()!= null && ! "".equals(webUserDTO.getEmail()) ){
			criteria1.add(Restrictions.sqlRestriction("email like '%"+webUserDTO.getEmail()+"%'"));
		}
		if( webUserDTO.getMobileNumber()!= null && ! "".equals(webUserDTO.getMobileNumber()) ){
			criteria1.add(Restrictions.sqlRestriction("mobileNumber like '%"+webUserDTO.getMobileNumber()+"%'"));
		}
		if( webUserDTO.getCountryId()!= null && ! "".equals(webUserDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", webUserDTO.getCountryId()));
		}
		if( webUserDTO.getStatus()!= null && ! "".equals(webUserDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("credentialsExpired", webUserDTO.getStatus()));
		}
		if( webUserDTO.getRoleId()!= null && ! "".equals(webUserDTO.getRoleId()) ){
			criteria1.add(Restrictions.eq("webUserRole.roleId", webUserDTO.getRoleId()));
		}

		criteria1.addOrder(Order.asc("userName"));
		page = PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public String getIdForBankAdmin(Integer roleId) {
		

		Query query = getSessionFactory().getCurrentSession()
				.createQuery("SELECT MAX(SUBSTRING(UserName,-3,3))+1 AS seq FROM WebUser wu where wu.webUserRole.roleId=:roleId")
				.setParameter("roleId", roleId);	
		
		List list = query.list();
		return list.get(0) == null ? "1" :list.get(0).toString();
	
	}
}
