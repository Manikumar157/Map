/* Copyright � EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: OperatorDaoImpl.java
*
* Date Author Changes
* 2 Aug, 2016 Swadhin Created
*/
package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dto.OperatorDTO;
import com.eot.banking.utils.DateUtil;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Country;
import com.eot.entity.Currency;
import com.eot.entity.CustomerCard;
import com.eot.entity.Operator;
import com.eot.entity.OperatorDenomination;
import com.eot.entity.OperatorVoucher;
import com.eot.entity.RemittanceCompaniesTransferType;
import com.eot.entity.RemittanceCompany;

/**
 * The Class OperatorDaoImpl.
 */
public class OperatorDaoImpl extends BaseDaoImpl implements OperatorDao {

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorList(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getOperatorList(Integer pageNumber, Integer bankId) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Operator.class);
		//criteria.add(Restrictions.eq("active", 1));
		if( bankId != null && ! "".equals(bankId) ){
			criteria.add(Restrictions.eq("bank.bankId",bankId));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Operator.class);
		//criteria1.add(Restrictions.eq("active", 1));
		if( bankId != null && ! "".equals(bankId) ){
			criteria1.add(Restrictions.eq("bank.bankId",bankId));
		}
		criteria1.addOrder(Order.asc("operatorName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getDenominations(java.lang.Long, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getDenominations(Long operatorId,Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(OperatorDenomination.class);
		criteria.add(Restrictions.eq("operator.operatorId",operatorId));
		criteria.addOrder(Order.asc("denomination"));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(OperatorDenomination.class);
		criteria1.add(Restrictions.eq("operator.operatorId",operatorId));
		criteria1.addOrder(Order.asc("denomination"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/


		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}	

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getCountries(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Country> getCountries(String language) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query=getSessionFactory().getCurrentSession().createQuery("select country from Country as country,CountryNames as countryNames  where country.countryId= countryNames.comp_id.countryId and countryNames.comp_id.languageCode=:language order by lower(countryNames.countryName)").setParameter("language", language).setCacheable(true);
		//@End
		/*Query query=getSessionFactory().getCurrentSession().createQuery("select country from Country as country,CountryNames as countryNames  where country.countryId= countryNames.comp_id.countryId and countryNames.comp_id.languageCode='"+language+" ' order by lower(countryNames.countryName)").setCacheable(true);*/


		return query.list();

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperator(java.lang.Long)
	 */
	@Override
	public Operator getOperator(Long operatorId) {
		return getHibernateTemplate().get(Operator.class,operatorId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getDenomination(java.lang.Long)
	 */
	@Override
	public OperatorDenomination getDenomination(Long denominationId) {
		return getHibernateTemplate().get(OperatorDenomination.class,denominationId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#verifyDenomination(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OperatorDenomination verifyDenomination(Long denomination,Long operatorId) {		
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from OperatorDenomination od where od.denomination=:denomination and od.operator.operatorId=:operatorId and od.active=1");*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from OperatorDenomination od where od.denomination=:denomination and od.operator.operatorId=:operatorId and od.active=:active");
		query.setParameter("denomination", denomination);
		query.setParameter("operatorId", operatorId);
		query.setParameter("active", 1);
		List<OperatorDenomination> list = query.list();
		return list.size()>0?list.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorName(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Operator getOperatorName(Long operatorId) {			
		Query query=getSessionFactory().getCurrentSession().createQuery("from Operator operator where operator.operatorId=:operatorId").setParameter("operatorId",operatorId);
		List<Operator> list=query.list();
		return list.size()>0 ? list.get(0) : null ; 

	}
	
	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getVouchers(java.lang.Long, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getVouchers(Long operatorId,Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(OperatorVoucher.class);
		criteria.add(Restrictions.eq("operator.operatorId",operatorId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(OperatorVoucher.class);
		criteria1.add(Restrictions.eq("operator.operatorId",operatorId));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
	// commented by vineeth as pagination is not showing records more than 10. , bug no:5934
//		criteria1.setMaxResults(appConfig.getResultsPerPage());


		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorNameByName(java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Operator getOperatorNameByName(String operatorName,Integer countryId) {		
		Query query = getSessionFactory().getCurrentSession().createQuery("from Operator as operator where operator.operatorName=:operatorName and operator.country.countryId=:countryId")
				.setParameter("operatorName", operatorName)
				.setParameter("countryId", countryId);		
		List<Operator> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}


	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#checkDenomination(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OperatorDenomination checkDenomination(Long denomination,Long operatorId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from OperatorDenomination as denomination where denomination.denomination=:denomination and denomination.operator.operatorId=:operatorId ");
		query.setParameter("denomination", denomination);
		query.setParameter("operatorId", operatorId);		
		List<OperatorDenomination> denominationList = query.list();
		return denominationList.size()>0?denominationList.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorStatus(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Operator getOperatorStatus(Long operatorId) {
		/*Query query=getSessionFactory().getCurrentSession().createQuery("from Operator as operator where operator.operatorId=:operatorId and operator.active=1").setParameter("operatorId",operatorId);*/
		Query query=getSessionFactory().getCurrentSession().createQuery("from Operator as operator where operator.operatorId=:operatorId and operator.active=:active").setParameter("active", 1).setParameter("operatorId",operatorId);
		List<Operator>list=query.list();
		return list.size()>0?list.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorListByCountry(java.lang.Integer)
	 */
	@Override
	public List<Operator> getOperatorListByCountry(Integer countryId) {		
		Query query=getSessionFactory().getCurrentSession().createQuery("from Operator operator where operator.country.countryId=:countryId").setParameter("countryId",countryId);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getDenominationList(java.lang.Long)
	 */
	@Override
	public List<OperatorDenomination> getDenominationList(Long operatorId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from OperatorDenomination od where od.operator.operatorId=:operatorId").setParameter("operatorId",operatorId);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getChartList(com.eot.banking.dto.OperatorDTO)
	 */
	@Override
	public List getChartList(OperatorDTO operatorDTO) {
		SQLQuery query=null;
		if(operatorDTO.getCountryId()==0){	
			if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgCount")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,count(TransactionID),sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers where operatorId in(select operatorId from Operator"
						+" where  cd.CountryID=CountryID)) and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");			

			}else if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgValue")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers where operatorId in(select operatorId from Operator"
						+" where  cd.CountryID=CountryID)) and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");			

			}	
		}else if(operatorDTO.getOperatorId()==0){	
			if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgCount")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,count(TransactionID),sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers ov where countryId in(select countryId from Operator"
						+" where ov.operatorId=operatorId and countryId="+operatorDTO.getCountryId()+")) and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");			

			}else if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgValue")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers ov where countryId in(select countryId from Operator"
						+" where ov.operatorId=operatorId and countryId="+operatorDTO.getCountryId()+")) and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");					
			}	
		}else if(operatorDTO.getOperatorId()!=null && operatorDTO.getDenominationId()==null ){	
			if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgCount")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,count(TransactionID),sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers where operatorId in(select operatorId from Operator"
						+" where OperatorID="+operatorDTO.getOperatorId()+" and CountryID="+operatorDTO.getCountryId()+" and cd.CountryID=CountryID)) and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");			

			}else if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgValue")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers where operatorId in(select operatorId from Operator"
						+" where OperatorID="+operatorDTO.getOperatorId()+" and CountryID="+operatorDTO.getCountryId()+" and cd.CountryID=CountryID)) and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");			

			}	
		}else if(operatorDTO.getDenominationId()!=null){	
			if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgCount")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,count(TransactionID),sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers where operatorId in(select operatorId from Operator"
						+" where OperatorID="+operatorDTO.getOperatorId()+" and CountryID="+operatorDTO.getCountryId()+" and cd.CountryID=CountryID) and denominationId="+operatorDTO.getDenominationId()+") and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");			

			}else if(operatorDTO.getImgType()!=null && operatorDTO.getImgType().equals("imgValue")){
				query=getSessionFactory().getCurrentSession().createSQLQuery("select cd.Country,sum(amount) from Transactions,Country cd where DATE(TransactionDate)>=?" 
						+" and DATE(TransactionDate)<=? and TransactionID in(select TransactionID from OperatorVouchers where operatorId in(select operatorId from Operator"
						+" where OperatorID="+operatorDTO.getOperatorId()+" and CountryID="+operatorDTO.getCountryId()+" and cd.CountryID=CountryID) and denominationId="+operatorDTO.getDenominationId()+")and transactiontype="+EOTConstants.TXN_ID_TOPUP+" and status="+EOTConstants.TXN_NO_ERROR+" group by cd.CountryID");					
			}	
		}

		if(operatorDTO!=null){
			if(operatorDTO.getFromDate()!=null){
				query.setString(0, DateUtil.formatDate(operatorDTO.getFromDate()));
			}
			if(operatorDTO.getToDate()!=null){
				query.setString(1, DateUtil.formatDate(operatorDTO.getToDate()));
			}
		}
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#verifyVoucherSlNumber(java.lang.String, java.lang.Long)
	 */
	@Override
	public OperatorVoucher verifyVoucherSlNumber(String voucherSerialNumber,Long operatorId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from OperatorVoucher as  voucher where voucher.voucherSerialNumber=:voucherSerialNumber and voucher.operator.operatorId=:operatorId")
				.setParameter("voucherSerialNumber", voucherSerialNumber)
				.setParameter("operatorId", operatorId);
		List<OperatorVoucher> voucherList=query.list();

		return voucherList.size()>0 ? voucherList.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#verifyVoucherNum(java.lang.String, java.lang.Long)
	 */
	@Override
	public OperatorVoucher verifyVoucherNum(String voucherNumber,Long operatorId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from OperatorVoucher as  voucher where voucher.voucherNumber=:voucherNumber and voucher.operator.operatorId=:operatorId" )
				.setParameter("voucherNumber", voucherNumber)
				.setParameter("operatorId", operatorId);
		List<OperatorVoucher> voucherList=query.list();

		return voucherList.size()>0 ? voucherList.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorNameByName(java.lang.String, java.lang.Integer, java.lang.Long)
	 */
	@Override
	public Operator getOperatorNameByName(String operatorName,Integer countryId, Long operatorId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Operator as operator where operator.operatorName=:operatorName and operator.country.countryId=:countryId and operator.operatorId !=:operatorId")
				.setParameter("operatorName", operatorName)
				.setParameter("countryId", countryId)
				.setParameter("operatorId", operatorId);
		List<Operator> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#checkDenomination(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public OperatorDenomination checkDenomination(Long denomination,
			Long operatorId, Long denominationId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from OperatorDenomination as denomination where denomination.denomination=:denomination and denomination.operator.operatorId=:operatorId and " +
				"denomination.denominationId !=:denominationId");
		query.setParameter("denomination", denomination);
		query.setParameter("operatorId", operatorId);
		query.setParameter("denominationId", denominationId);
		List<OperatorDenomination> denominationList = query.list();
		return denominationList.size()>0?denominationList.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#searchOperator(com.eot.banking.dto.OperatorDTO, int)
	 */
	@Override
	public Page searchOperator(OperatorDTO operatorDTO, int pageNumber, Integer bankId) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Operator.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));

		if( operatorDTO.getOperatorName()!= null && ! "".equals(operatorDTO.getOperatorName()) ){
			criteria.add(Restrictions.sqlRestriction("operatorName like '%"+ operatorDTO.getOperatorName()+"%'"));
		}		
		if( operatorDTO.getCountryId()!= null && ! "".equals(operatorDTO.getCountryId()) ){
			criteria.createAlias("country","country");
			criteria.add(Restrictions.eq("country.countryId", operatorDTO.getCountryId()));
		}
		/*if( operatorDTO.getBankId()!= null && ! "".equals(operatorDTO.getBankId()) ){
			criteria.createAlias("bank","bank");
			criteria.add(Restrictions.eq("bank.bankId", operatorDTO.getBankId()));
		}*/
		if( bankId != null && ! "".equals(bankId) ){
			criteria.createAlias("bank","bank");
			criteria.add(Restrictions.eq("bank.bankId",bankId));
		}
		if( operatorDTO.getActive()!= null && ! "".equals(operatorDTO.getActive()) ){
			criteria.add(Restrictions.eq("active", operatorDTO.getActive()));
		}		

		int totalCount = Integer.parseInt(criteria.list().get(0).toString());		

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Operator.class);

		if( operatorDTO.getOperatorName()!= null && ! "".equals(operatorDTO.getOperatorName()) ){
			criteria1.add(Restrictions.sqlRestriction("operatorName like '%"+ operatorDTO.getOperatorName()+"%'"));
		}		
		if( operatorDTO.getCountryId()!= null && ! "".equals(operatorDTO.getCountryId()) ){
			criteria1.createAlias("country","country");
			criteria1.add(Restrictions.eq("country.countryId", operatorDTO.getCountryId()));
		}
		/*if( operatorDTO.getBankId()!= null && ! "".equals(operatorDTO.getBankId()) ){
			criteria.createAlias("bank","bank");
			criteria1.add(Restrictions.eq("bank.bankId", operatorDTO.getBankId()));
		}*/
		if( bankId != null && ! "".equals(bankId) ){
			criteria1.createAlias("bank","bank");
			criteria1.add(Restrictions.eq("bank.bankId",bankId));
		}
		if( operatorDTO.getActive()!= null && ! "".equals(operatorDTO.getActive()) ){
			criteria1.add(Restrictions.eq("active", operatorDTO.getActive()));
		}


		//criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		//criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getCardDetailsByOperatorId(java.lang.Integer)
	 */
	@Override
	public CustomerCard getCardDetailsByOperatorId(Integer operatorId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.referenceId=:operatorId and cc.referenceType=:refType")
				.setParameter("operatorId", operatorId+"")
				.setParameter("refType", EOTConstants.REFERENCE_TYPE_OPERATOR);
		List<CustomerCard> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getCardDetailsByOperatorId(java.lang.Integer, java.lang.String)
	 */
	@Override
	public CustomerCard getCardDetailsByOperatorId(Integer operatorId,String cardNo) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardNumber=:cardNo and cc.referenceType=:refType and cc.referenceId!=:operatorId ")
				.setParameter("cardNo", cardNo)
				.setParameter("refType", EOTConstants.REFERENCE_TYPE_OPERATOR)
				.setParameter("operatorId", operatorId+"");

		List<CustomerCard> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getOperatorVertualCard(java.lang.String)
	 */
	@Override
	public CustomerCard getOperatorVertualCard(String cardNumber) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CustomerCard as cc where cc.cardNumber=:cardNumber and cc.referenceType=:refType")
				.setParameter("cardNumber", cardNumber)
				.setParameter("refType", EOTConstants.REFERENCE_TYPE_OPERATOR);

		List<CustomerCard> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getRemittanceCompanyByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RemittanceCompany getRemittanceCompanyByName(String remittanceCompanyName) {
		List<RemittanceCompany> remittanceCompanies = getHibernateTemplate().find
				("from RemittanceCompany rc where rc.remittanceCompanyName=?", remittanceCompanyName);
		return remittanceCompanies.size()>0 ? remittanceCompanies.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getRemittanceCompanyById(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RemittanceCompany getRemittanceCompanyById(Integer remittanceCompanyId) {
		List<RemittanceCompany> remittanceCompanies = getHibernateTemplate().find
				("from RemittanceCompany rc where rc.remittanceCompanyId=?", remittanceCompanyId);
		return remittanceCompanies.size()>0 ? remittanceCompanies.get(0):null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#deleteRemittanceCompaniesTransferType(java.lang.Integer)
	 */
	@Override
	public void deleteRemittanceCompaniesTransferType(Integer remittanceCompanyId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from RemittanceCompaniesTransferType as rct where rct.remittanceCompany.remittanceCompanyId=:remittanceCompanyId";
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry).setParameter("remittanceCompanyId", remittanceCompanyId);
		//@End
		/*String qry = "delete from RemittanceCompaniesTransferType as rct where rct.remittanceCompany.remittanceCompanyId="+remittanceCompanyId;
		org.hibernate.Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.OperatorDao#getAllRemittanceCompanies(java.lang.Integer)
	 */
	@Override
	public Page getAllRemittanceCompanies(Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RemittanceCompany.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
	
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(RemittanceCompany.class);
		criteria1.addOrder(Order.asc("remittanceCompanyName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
*/
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page getOperatorListByPageNumber(Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Operator.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Operator.class);
		criteria1.addOrder(Order.asc("operatorName"));
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
}
