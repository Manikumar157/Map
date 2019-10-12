package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dto.BankDTO;
import com.eot.banking.dto.BranchDTO;
import com.eot.banking.dto.ClearingHouseDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.AccountHead;
import com.eot.entity.Bank;
import com.eot.entity.BankAgreementModel;
import com.eot.entity.BankGroup;
import com.eot.entity.BankGroupAdmin;
import com.eot.entity.BankTellers;
import com.eot.entity.Branch;
import com.eot.entity.ClearingHousePool;
import com.eot.entity.ClearingHousePoolMember;
import com.eot.entity.Currency;
import com.eot.entity.CustomerCard;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.SettlementBatch;

public class BankDaoImpl extends BaseDaoImpl implements BankDao{

	@SuppressWarnings("unchecked")
	@Override
	public Page getAllBanks(int pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());


		Query query=getSessionFactory().getCurrentSession().createQuery("from Bank  order by lower(bankName)");
		query.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		query.setMaxResults(appConfig.getResultsPerPage());
		List<Bank> banks = query.list();

		return PaginationHelper.getPage(banks, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	@SuppressWarnings("unchecked")
	public List<Bank> getAllBanksByName() {
		List<Bank> list=getSessionFactory().getCurrentSession().createQuery("from Bank order by lower(bankName)").list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getActiveBanks() {
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Bank b where b.status=1 order by lower(b.bankName)" ).setCacheable(true);*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank b where b.status=:status order by lower(b.bankName)" ).setParameter("status", 1).setCacheable(true);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankAgreementModel> getBankAgreementModels() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankAgreementModel" ).setCacheable(true);

		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getCurrencies() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency currency order by lower(currency.currencyName)" ).setCacheable(true);
		return query.list();
	}

	public Page getClearingHouses(int pageNumber){

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ClearingHousePool.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(ClearingHousePool.class);
		criteria1.addOrder(Order.asc("clearingPoolName"));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);


	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ClearingHousePool> getActiveClearingHouses() {		
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from ClearingHousePool ch  where ch.status=1 order by lower(ch.clearingPoolName) ").setCacheable(true);*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from ClearingHousePool ch  where ch.status=:status order by lower(ch.clearingPoolName) ").setParameter("status", 1).setCacheable(true);
		return query.list();
	}
	@Override
	public ClearingHousePool getClearingHouseDetails(Integer clearingPoolId) {
		return getHibernateTemplate().get(ClearingHousePool.class, clearingPoolId);
	}

	@Override
	public Bank getBank(Integer bankId) {
		return getHibernateTemplate().get(Bank.class,bankId);
	}

	@Override
	public BankTellers getTellerByUsername(String userName) {
		return getHibernateTemplate().get(BankTellers.class,userName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Bank getBankCode(String bankCode) {
		Query query = getSessionFactory().getCurrentSession().
				createQuery("from Bank as b where b.bankCode=:bankCode")
				.setParameter("bankCode", bankCode);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Long getNextAccountNumberSequence() {
		Query query = getSessionFactory().getCurrentSession()
				.createSQLQuery("select IFNULL(max(mid(AccountNumber,1,12)),100000000000)+10 as seq from Account")
				.addScalar("seq",Hibernate.LONG);
		return new Long( query.list().get(0).toString() ) ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getBranchList(Integer bankId,int pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Branch.class);
		criteria.add( Restrictions.eq("bank.bankId",bankId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Branch.class);
		criteria1.add( Restrictions.eq("bank.bankId",bankId));
		criteria1.addOrder(Order.asc("location"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
*/
		System.out.println("criteria1.list() :" + criteria1.list().size());
		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Branch br where br.bank.bankId=:bankId ORDER BY lower(br.location)")
		.setParameter("bankId", bankId);
		return query.list();*/
	}

	@Override
	public Branch getBranchDetails(Long branchId) {		
		return getHibernateTemplate().get(Branch.class,branchId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getBanksByType(Integer agreementType) {
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as b where b.status=1 and b.bankAgreementModel.agreementType=:agreementType").setParameter("agreementType", agreementType);*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as b where b.status=:status and b.bankAgreementModel.agreementType=:agreementType").setParameter("status", 1).setParameter("agreementType", agreementType);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClearingHousePool getClearingHouseByName(String clearingPoolName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from ClearingHousePool as b where b.clearingPoolName=:clearingPoolName").setParameter("clearingPoolName",clearingPoolName);
		List<ClearingHousePool> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ClearingHousePool getClearingHouseByMobilenumber(String mobileNumber) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from ClearingHousePool as b where b.mobileNumber=:mobileNumber").setParameter("mobileNumber",mobileNumber);
		List<ClearingHousePool> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	@SuppressWarnings("unchecked")
	public List<ServiceChargeSplit> getServiceChargeSplits(Integer bankId){
		//@Start, Optimized query, by Murari, dated : 23-07-2018
				List<ServiceChargeSplit> list=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit as scs where scs.bank.bankId=:bankId").setParameter("bankId", bankId).list();
		//@End
		/*List<ServiceChargeSplit> list=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit as scs where scs.bank.bankId="+bankId).list();*/
		return list ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankGroup> getAllBankGroups() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankGroup bg order by lower(bg.bankGroupName)" ).setCacheable(true);
		return query.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankAgreementModel> viewBankAgreementDates() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from BankAgreementModel bam" );
		return query.list();
	}

	@Override
	public Page getBanksByGroupId(Integer bankGroupId,Integer pageNumber) {		

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria.add( Restrictions.eq("bankGroup.bankGroupId",bankGroupId));	
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria1.add( Restrictions.eq("bankGroup.bankGroupId",bankGroupId));	
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public BankGroupAdmin getBankGroupByUsername(String userName) {
		return getHibernateTemplate().get(BankGroupAdmin.class,userName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bank> getBanksByGroupId(Integer bankGroupId) {
		/*Query query = getSessionFactory().getCurrentSession()
				.createQuery("from Bank as b where b.status=1 and b.bankGroup.bankGroupId=:bankGroupId")
				.setParameter("bankGroupId", bankGroupId);
		return query.list();*/
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("from Bank as b where b.status=:status and b.bankGroup.bankGroupId=:bankGroupId")
				.setParameter("status", 1)
				.setParameter("bankGroupId", bankGroupId);
		return query.list();
	}
	@Override
	public List<AccountHead> getAccountHeadByBank() {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query = getSessionFactory().getCurrentSession().createQuery("from AccountHead ac where ac.accountBook.bookId=:bookId")
				.setParameter("bookId", EOTConstants.BOOK_TYPE_BANK);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from AccountHead ac where ac.accountBook.bookId="+EOTConstants.BOOK_TYPE_BANK);*/
		return query.list();

	}  

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountHead> getAccountHeadsForBook(Integer bookId) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("from AccountHead as b where b.accountBook.bookId=:bookId")
				.setParameter("bookId", bookId);
		return query.list();
	}

	@Override
	public Branch verifyBranchCode(String branchCode,Integer countryId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Branch as b where b.branchCode=:branchCode and b.country.countryId=:countryId").setParameter("branchCode", branchCode).setParameter("countryId", countryId);

		List<Branch> branchList=query.list();
		return branchList.size()>0 ? branchList.get(0): null;

	}

	@Override
	public Bank getBankByIdCode(Integer bankId, String bankCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as b where b.bankId!=:bankId and b.bankCode=:bankCode")
				.setParameter("bankId", bankId)
				.setParameter("bankCode", bankCode);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	@Override
	public Branch getBranchByCode(String branchCode,Integer bankId) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("from Branch as branch where branch.branchCode=:branchCode and branch.bank.bankId=:bankId")
				.setParameter("branchCode", branchCode)
				.setParameter("bankId", bankId);
		List<Branch> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Branch getBranchesByIdCode(Long branchId, String branchCode,Integer bankId) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("from Branch as branch where branch.branchId!=:branchId and branch.bank.bankId=:bankId and branch.branchCode=:branchCode")
				.setParameter("branchId", branchId)
				.setParameter("bankId", bankId)
				.setParameter("branchCode", branchCode);
		List<Branch> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Bank getBankBySwiftCode(String swiftCode) {

		Query query = getSessionFactory().getCurrentSession().
				createQuery("from Bank as bank where bank.swiftCode=:swiftCode")
				.setParameter("swiftCode", swiftCode);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Bank getBankBySwiftCode(Integer bankId, String swiftCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as bank where bank.bankId!=:bankId and bank.swiftCode=:swiftCode")
				.setParameter("bankId", bankId)
				.setParameter("swiftCode", swiftCode);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public CustomerCard getCardDetailsByBankId(Integer bankId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.referenceId=:bankId and cc.referenceType=:refType")
				.setParameter("bankId", bankId+"")
				.setParameter("refType", 5);
		List<CustomerCard> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	@Override
	public CustomerCard getCardDetailsByBankId(Integer bankId,String cardNo) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardNumber=:cardNo and cc.referenceType=:refType and cc.referenceId!=:bankId ")
				.setParameter("cardNo", cardNo)
				.setParameter("refType", 5)
				.setParameter("bankId", bankId+"");

		List<CustomerCard> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	@Override
	public CustomerCard getBankVertualCard(String cardNumber) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardNumber=:cardNumber and cc.referenceType=:refType")
				.setParameter("cardNumber", cardNumber)
				.setParameter("refType", 5);

		List<CustomerCard> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	@Override
	public Integer getMaxPoolPriority() {
		Query query = getSessionFactory().getCurrentSession().createQuery("select max(poolPriority)+1 from ClearingHousePoolMember");

		List<Integer> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public ClearingHousePoolMember getClearingHousePoolMember(Integer clearingPoolId) {

		Query query = getSessionFactory().getCurrentSession().createQuery("from ClearingHousePoolMember as chpm where chpm.clearingPoolId=:clearingPoolId")
				.setParameter("clearingPoolId", clearingPoolId);

		List<ClearingHousePoolMember> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Bank getBankCode(Integer bankId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as bank where bank.bankId=:bankId")
				.setParameter("bankId", bankId);

		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Branch getBranchCode(Long branchId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Branch as branch where branch.branchId=:branchId ")
				.setParameter("branchId", branchId);
	
		List<Branch> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	@Override
	public List<Branch> getAllBranchFromBank(Integer bankId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Branch as branch where branch.bank.bankId=:bankId order by location")
				.setParameter("bankId", bankId);

		List<Branch> list = query.list();
		return list.size()>0 ? list : null ;
	}

	@Override
	public Branch getBranchFromLocation(String location,Integer bankId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Branch as branch where branch.location=:location and branch.bank.bankId=:bankId")
				.setParameter("location", location)
				.setParameter("bankId", bankId);

		List<Branch> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Branch getBranchFromLocation(Long branchId, String location,Integer bankId) {
		Query query = getSessionFactory().getCurrentSession()
				.createQuery("from Branch as branch where branch.branchId!=:branchId and branch.location=:location and branch.bank.bankId=:bankId")
				.setParameter("branchId", branchId)
				.setParameter("bankId", bankId)
				.setParameter("location", location);
		List<Branch> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* @Override
	public Page searchBank(Integer bankGroupId,BankDTO bankDTO, int pageNumber) {

		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(Bank.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( bankDTO.getSwiftCode()!= null && ! "".equals(bankDTO.getSwiftCode()) ){
			criteria.add(Restrictions.eq("swiftCode", bankDTO.getSwiftCode()));
		}
		if( bankDTO.getBankName()!= null && ! "".equals(bankDTO.getBankName()) ){
			criteria.add(Restrictions.sqlRestriction("bankName like '%"+bankDTO.getBankName()+"%'"));
		}
		if( bankDTO.getBankCode()!= null && ! "".equals(bankDTO.getBankCode()) ){
			criteria.add(Restrictions.eq("bankCode", bankDTO.getBankCode()));
		}
		if( bankDTO.getCountryId()!= null && ! "".equals(bankDTO.getCountryId()) ){
			criteria.add(Restrictions.eq("country.countryId", Integer.valueOf(bankDTO.getCountryId())));
		}
		if( bankDTO.getCurrencyId()!= null && ! "".equals(bankDTO.getCurrencyId()) ){
			criteria.add(Restrictions.eq("currency.currencyId", Integer.valueOf(bankDTO.getCurrencyId())));
		}
		if( bankDTO.getStatus()!= null && ! "".equals(bankDTO.getStatus()) ){
			criteria.add(Restrictions.eq("status", Integer.valueOf(bankDTO.getStatus())));
		}
		if(bankGroupId!=null){
			criteria.add(Restrictions.eq("bankGroup.bankGroupId",bankGroupId));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		System.out.println("Total Bank Data : " +totalCount);
		criteria1 = getSessionFactory().getCurrentSession().createCriteria(Bank.class);
		criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if( bankDTO.getSwiftCode()!= null && ! "".equals(bankDTO.getSwiftCode()) ){
			criteria1.add(Restrictions.eq("swiftCode", bankDTO.getSwiftCode()));
		}
		if( bankDTO.getBankName()!= null && ! "".equals(bankDTO.getBankName()) ){
			criteria1.add(Restrictions.sqlRestriction("bankName like '%"+bankDTO.getBankName()+"%'"));
		}
		if( bankDTO.getBankCode()!= null && ! "".equals(bankDTO.getBankCode()) ){
			criteria1.add(Restrictions.eq("bankCode", bankDTO.getBankCode()));
		}
		if( bankDTO.getCountryId()!= null && ! "".equals(bankDTO.getCountryId()) ){
			criteria1.add(Restrictions.eq("country.countryId", Integer.valueOf(bankDTO.getCountryId())));
		}
		if( bankDTO.getCurrencyId()!= null && ! "".equals(bankDTO.getCurrencyId()) ){
			criteria1.add(Restrictions.eq("currency.currencyId", Integer.valueOf(bankDTO.getCurrencyId())));
		}
		if( bankDTO.getStatus()!= null && ! "".equals(bankDTO.getStatus()) ){
			criteria1.add(Restrictions.eq("status", Integer.valueOf(bankDTO.getStatus())));
		}
		if(bankGroupId!=null){
			criteria1.add(Restrictions.eq("bankGroup.bankGroupId",bankGroupId));
		}
		criteria1.list();
		criteria1.addOrder(Order.asc("bankName"));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());

		System.out.println("criteria1.list() : "+ criteria1.list().size());

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}*/

	@Override
	public Page searchBank(Integer bankGroupId,BankDTO bankDTO, int pageNumber) {

		StringBuffer qryStr = null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer qry = new StringBuffer("SELECT count(bank.BankID)" +
				" FROM Bank AS bank INNER JOIN Currency AS currency ON bank.CurrencyID=currency.CurrencyID JOIN Country AS " +
				"country ON bank.CountryID=country.CountryID JOIN TimeZone AS  timezone ON bank.TimeZoneID=timezone.TimeZoneID");

		if( bankDTO.getSwiftCode()!= null && ! "".equals(bankDTO.getSwiftCode()) ){
			qry.append(" and swiftCode=:swiftCode");
		}
		if( bankDTO.getBankName()!= null && ! "".equals(bankDTO.getBankName()) ){
			qry.append(" and bankName like :bankName");
		}
		if( bankDTO.getBankCode()!= null && ! "".equals(bankDTO.getBankCode()) ){
			qry.append(" and bankCode= :bankCode");
		}
		if( bankDTO.getCountryId()!= null && ! "".equals(bankDTO.getCountryId()) ){
			qry.append(" and country.CountryID=:countryId");
		}
		if( bankDTO.getCurrencyId()!= null && ! "".equals(bankDTO.getCurrencyId()) ){
			qry.append(" and currency.currencyID= :currencyId");
		}
		if( bankDTO.getStatus()!= null && ! "".equals(bankDTO.getStatus()) ){
			qry.append(" and status=:status");
		}
		if( bankGroupId!= null){
			qry.append(" JOIN BankGroups AS bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qry.append(" and bankgroups.BankGroupID= :bankGroupId ");
		}

		Query qryResult = session.createSQLQuery(qry.toString());	    
		
		if( bankDTO.getSwiftCode()!= null && ! "".equals(bankDTO.getSwiftCode()) ){
			qryResult.setParameter("swiftCode", bankDTO.getSwiftCode());
		}
		if( bankDTO.getBankName()!= null && ! "".equals(bankDTO.getBankName()) ){
			qryResult.setParameter("bankName", "%"+bankDTO.getBankName()+"%");
		}
		if( bankDTO.getBankCode()!= null && ! "".equals(bankDTO.getBankCode()) ){
			qryResult.setParameter("bankCode", bankDTO.getBankCode());
		}
		if( bankDTO.getCountryId()!= null && ! "".equals(bankDTO.getCountryId()) ){
			qryResult.setParameter("countryId", Integer.valueOf(bankDTO.getCountryId()));
		}
		if( bankDTO.getCurrencyId()!= null && ! "".equals(bankDTO.getCurrencyId()) ){
			qryResult.setParameter("currencyId", Integer.valueOf(bankDTO.getCurrencyId()));
		}
		if( bankDTO.getStatus()!= null && ! "".equals(bankDTO.getStatus()) ){
			qryResult.setParameter("status", Integer.valueOf(bankDTO.getStatus()));
		}
		if( bankGroupId!= null){
			qryResult.setParameter("bankGroupId", bankGroupId);
		}

		int totalCount = Integer.parseInt(qryResult.list().get(0).toString());

		qryStr = new StringBuffer("SELECT bank.BankID,bank.BankName,bank.SwiftCode,bank.BankCode,bank.status,currency.CurrencyCode,country.Country,timezone.TimeZoneDesc" +
				" FROM Bank AS bank INNER JOIN Currency AS currency ON bank.CurrencyID=currency.CurrencyID JOIN Country AS " +
				"country ON bank.CountryID=country.CountryID JOIN TimeZone AS  timezone ON bank.TimeZoneID=timezone.TimeZoneID ");

		if( bankDTO.getSwiftCode()!= null && ! "".equals(bankDTO.getSwiftCode()) ){
			qryStr.append(" and swiftCode=:swiftCode1");
		}
		if( bankDTO.getBankName()!= null && ! "".equals(bankDTO.getBankName()) ){
			qryStr.append(" and bankName like :bankName1");
		}
		if( bankDTO.getBankCode()!= null && ! "".equals(bankDTO.getBankCode()) ){
			qryStr.append(" and bankCode= :bankCode1");
		}
		if( bankDTO.getCountryId()!= null && ! "".equals(bankDTO.getCountryId()) ){
			qryStr.append(" and country.CountryID=:countryId1");
		}
		if( bankDTO.getCurrencyId()!= null && ! "".equals(bankDTO.getCurrencyId()) ){
			qryStr.append(" and currency.currencyID= :currencyId1");
		}
		if( bankDTO.getStatus()!= null && ! "".equals(bankDTO.getStatus()) ){
			qryStr.append(" and status=:status1");
		}
		if( bankGroupId!= null){
			qryStr.append(" JOIN BankGroups AS bankgroups ON bank.BankGroupID=bankgroups.BankGroupID");
			qryStr.append(" and bankgroups.BankGroupID= :bankGroupId1 ");
		}  		
		qryStr.append(" ORDER BY bank.BankName asc");
		SQLQuery qryResult1 = session.createSQLQuery(qryStr.toString());
		
		if( bankDTO.getSwiftCode()!= null && ! "".equals(bankDTO.getSwiftCode()) ){
			qryResult1.setParameter("swiftCode1", bankDTO.getSwiftCode());
		}
		if( bankDTO.getBankName()!= null && ! "".equals(bankDTO.getBankName()) ){
			qryResult1.setParameter("bankName1", "%"+bankDTO.getBankName()+"%");
		}
		if( bankDTO.getBankCode()!= null && ! "".equals(bankDTO.getBankCode()) ){
			qryResult1.setParameter("bankCode1", bankDTO.getBankCode());
		}
		if( bankDTO.getCountryId()!= null && ! "".equals(bankDTO.getCountryId()) ){
			qryResult1.setParameter("countryId1", Integer.valueOf(bankDTO.getCountryId()));
		}
		if( bankDTO.getCurrencyId()!= null && ! "".equals(bankDTO.getCurrencyId()) ){
			qryResult1.setParameter("currencyId1", Integer.valueOf(bankDTO.getCurrencyId()));
		}
		if( bankDTO.getStatus()!= null && ! "".equals(bankDTO.getStatus()) ){
			qryResult1.setParameter("status1", Integer.valueOf(bankDTO.getStatus()));
		}
		if( bankGroupId!= null){
			qryResult1.setParameter("bankGroupId1", bankGroupId);
		}

		/*qryResult1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		qryResult1.setMaxResults(appConfig.getResultsPerPage());*/
		qryResult1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


		return PaginationHelper.getPage(qryResult1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	
	/*This method not in use, SQL Injection not required*/
	@Override
	public Page getAllBranchList(BranchDTO branchDTO, int pageNumber) {

		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(Branch.class);
		criteria.add( Restrictions.eq("bank.bankId",branchDTO.getBankId()));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( branchDTO.getLocation()!= null && ! "".equals(branchDTO.getLocation()) ){
			criteria.add(Restrictions.sqlRestriction("location like '%"+branchDTO.getLocation()+"%'"));
		}
		if( branchDTO.getBranchCode()!= null && ! "".equals(branchDTO.getBranchCode()) ){
			criteria.add(Restrictions.sqlRestriction("branchCode like '%"+branchDTO.getBranchCode()+"%'"));
		}
		if( branchDTO.getCityId()!= null && ! "".equals(branchDTO.getCityId()) ){
			criteria.add(Restrictions.eq("city.cityId", branchDTO.getCityId()));
		}
		if( branchDTO.getQuarterId()!= null && ! "".equals(branchDTO.getQuarterId()) ){
			criteria.add(Restrictions.eq("quarter.quarterId", branchDTO.getQuarterId()));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(Branch.class);
		criteria1.add( Restrictions.eq("bank.bankId",branchDTO.getBankId()));
		if( branchDTO.getLocation()!= null && ! "".equals(branchDTO.getLocation()) ){
			criteria1.add(Restrictions.sqlRestriction("location like '%"+branchDTO.getLocation()+"%'"));
		}
		if( branchDTO.getBranchCode()!= null && ! "".equals(branchDTO.getBranchCode()) ){
			criteria1.add(Restrictions.sqlRestriction("branchCode like '%"+branchDTO.getBranchCode()+"%'"));
		}
		if( branchDTO.getCityId()!= null && ! "".equals(branchDTO.getCityId()) ){
			criteria1.add(Restrictions.eq("city.cityId", branchDTO.getCityId()));
		}
		if( branchDTO.getQuarterId()!= null && ! "".equals(branchDTO.getQuarterId()) ){
			criteria1.add(Restrictions.eq("quarter.quarterId", Long.valueOf(branchDTO.getQuarterId())));
		}

		criteria1.addOrder(Order.asc("location"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
*/
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Bank getEOTCardBankCode(String eotCardBankCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as bank where bank.eotCardBankCode=:eotCardBankCode")
				.setParameter("eotCardBankCode", eotCardBankCode);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Bank getEOTCardBankCode(Integer bankId, String eotCardBankCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as bank where bank.bankId!=:bankId and bank.eotCardBankCode=:eotCardBankCode")
				.setParameter("bankId", bankId)
				.setParameter("eotCardBankCode", eotCardBankCode);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Bank getBankByName(String bankName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as bank where bank.bankName=:bankName")
				.setParameter("bankName", bankName);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null;
	}

	@Override
	public Bank getBankByName(Integer bankId, String bankName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Bank as bank where bank.bankId!=:bankId and bank.bankName=:bankName")
				.setParameter("bankId", bankId)
				.setParameter("bankName", bankName);
		List<Bank> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Page getSearchClearingHouseList(ClearingHouseDTO clearingHouseDTO,int pageNumber) {

		Criteria criteria1=null;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(ClearingHousePool.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( clearingHouseDTO.getClearingHouseName()!= null && ! "".equals(clearingHouseDTO.getClearingHouseName()) ){
			criteria.add(Restrictions.sqlRestriction("clearingPoolName like ?",clearingHouseDTO.getClearingHouseName()+"%",Hibernate.STRING));
		}
		if( clearingHouseDTO.getCurrency()!= null && ! "".equals(clearingHouseDTO.getCurrency()) ){
			criteria.add(Restrictions.eq("currency.currencyId",clearingHouseDTO.getCurrency()));
		}
		if( clearingHouseDTO.getCentralBankAccount()!= null && ! "".equals(clearingHouseDTO.getCentralBankAccount()) ){
			criteria.add(Restrictions.eq("centralBankAccount",clearingHouseDTO.getCentralBankAccount()));
		}
		if( clearingHouseDTO.getGuaranteeAccount()!= null && ! "".equals(clearingHouseDTO.getGuaranteeAccount()) ){
			criteria.add(Restrictions.eq("guaranteeAccount", clearingHouseDTO.getGuaranteeAccount()));
		}

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		criteria1 = getSessionFactory().getCurrentSession().createCriteria(ClearingHousePool.class);

		if( clearingHouseDTO.getClearingHouseName()!= null && ! "".equals(clearingHouseDTO.getClearingHouseName()) ){
			criteria1.add(Restrictions.sqlRestriction("clearingPoolName like ?",clearingHouseDTO.getClearingHouseName()+"%",Hibernate.STRING));
		}
		if( clearingHouseDTO.getCurrency()!= null && ! "".equals(clearingHouseDTO.getCurrency()) ){
			criteria1.add(Restrictions.eq("currency.currencyId",clearingHouseDTO.getCurrency()));
		}
		if( clearingHouseDTO.getCentralBankAccount()!= null && ! "".equals(clearingHouseDTO.getCentralBankAccount()) ){
			criteria1.add(Restrictions.eq("centralBankAccount",clearingHouseDTO.getCentralBankAccount()));
		}
		if( clearingHouseDTO.getGuaranteeAccount()!= null && ! "".equals(clearingHouseDTO.getGuaranteeAccount()) ){
			criteria1.add(Restrictions.eq("guaranteeAccount", clearingHouseDTO.getGuaranteeAccount()));
		}
		criteria1.list();
		criteria1.addOrder(Order.asc("clearingPoolName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
*/
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);


	}

	@Override
	public List<Bank> getBankList() {
		List<Bank> list=getSessionFactory().getCurrentSession().createQuery("select bank.bankName from Bank bank").list();
		return list;
	}

	@Override
	public List<Branch> getBranchList() {
		List<Branch> list=getSessionFactory().getCurrentSession().createQuery("select branch.location from Branch branch").list();
		return list;
	}

	@Override
	public void updateSettlementBatchList(Integer poolId) {

		List<SettlementBatch> list = getSessionFactory().getCurrentSession().createQuery("select sb.batchId from SettlementBatch sb" +
				" where sb.clearingHousePool.clearingPoolId=:poolId").setParameter("poolId", poolId).list();

		if(list.size()>0){
			
			Query query = getSessionFactory().getCurrentSession().createQuery("update SettlementBatchDetail set status=5 where BatchID in(:list)");
			query.setParameterList("list", list);

			query.executeUpdate();
			
		}

	}

	@Override
	public List<Branch> getbranchList(Integer bankId) {
		Query query = getSessionFactory().getCurrentSession().
				createQuery("from Branch as br where br.bank.bankId=:bankId")
				.setParameter("bankId", bankId);
		List<Branch> list = query.list();
		return list.size()>0 ? list : null ;
	}



}
