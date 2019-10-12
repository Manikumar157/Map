package com.eot.banking.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eot.banking.dao.CommissionDao;
import com.eot.banking.dto.CommissionDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Bank;
import com.eot.entity.CommisionSplits;
import com.eot.entity.Commission;
import com.eot.entity.Currency;
import com.eot.entity.CustomerProfiles;
import com.eot.entity.ExchangeRate;

@Repository("commissionDao")
public class CommissionDaoImpl extends BaseDaoImpl implements CommissionDao  {
	
	/**************Added By Bidyut*****************/
	@Override
	public List<CustomerProfiles> loadCustomerProfiles(Integer bankId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CustomerProfiles.class);
		criteria1.addOrder(Order.asc("profileId"));
		criteria1.createCriteria("bank").add(Restrictions.eq("bankId",bankId));
		return criteria1.list();
	}
	
	/*@Added by Vinod Joshi, This Method not in use.*/
	@Override
	public Page loadCommissionSlabDetails(Integer pageNumber , CommissionDTO commissionDTO) {
		/*Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Commission.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Commission.class);
		criteria1.addOrder(Order.asc("commissionId"));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);*/

		StringBuffer queryString=new StringBuffer();

		queryString.append( "SELECT c.commission_id cId , c.commission_per cper , b.bankName bn, txt.description descs ,cp.profileName profName "+
				"FROM Commission c ,Bank b , TransactionTypes txt ,CustomerProfiles cp "+
				"WHERE c.bank_Id = b.bankId AND "+ 
				" c.profile_Id = cp.profileId AND "+
				" c.transaction_type_id = txt.transactionType " );
		if(commissionDTO.getBankId() != null){
			if(!commissionDTO.getBankId().equals("")){
				queryString.append(" AND c.bank_Id ='" +commissionDTO.getBankId()+"'");
			}
		}
		if(commissionDTO.getProfileId() != null){
			if(!commissionDTO.getProfileId().equals("")){
				queryString.append(" AND c.profile_Id = '" + commissionDTO.getProfileId() +"'");
			}
		}
		if(commissionDTO.getTransactionTypeId() != null){
			if(!commissionDTO.getTransactionTypeId().equals("")){
				queryString.append(" AND c.transaction_type_id = '" + commissionDTO.getTransactionTypeId()+ "'");
			}
		}
		
		queryString.append(" order by cId desc");
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery( queryString.toString() );
		
		query.addScalar("cId",Hibernate.LONG);
		query.addScalar("cper",Hibernate.DOUBLE);
		query.addScalar("bn",Hibernate.STRING);
		query.addScalar("descs",Hibernate.STRING);
		query.addScalar("profName",Hibernate.STRING);

		List<Object[]> list = query.list();

		List<CommissionDTO> dtoList = new ArrayList<CommissionDTO>();

		for(Object[] obj : list){

			CommissionDTO dto = new CommissionDTO();

			dto.setCommisionId((Long)obj[0]);
			dto.setCommission((Double)obj[1]);
			dto.setBankName((String)obj[2]);
			dto.setTransactionTypeName((String)obj[3]);
			dto.setProfileName((String)obj[4]);

			dtoList.add(dto) ;

		}
		return PaginationHelper.getPage(dtoList,appConfig.getResultsPerPage(), pageNumber);
	}
	
	@Override
	public Commission loadCommissionDetailsByCommissionId(Long commissionId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Commission com where com.commissionId=:commissionId").setParameter("commissionId", commissionId);
		List<Commission> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	
	@Override
	public void deleteCommissionSlab(Long commisionId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("delete Commission cu  where cu.commissionId=:commissionId")
				.setParameter("commissionId", commisionId);		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Commission> loadCommisionSlab(int transactionType, Integer bankId, Integer profileId) {
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Commission.class);
		criteria1.addOrder(Order.asc("profileId"));
		criteria1.add(Restrictions.eq("transactionTypeId", transactionType));
		criteria1.add(Restrictions.eq("bankId", bankId));
		criteria1.add(Restrictions.eq("profileId", profileId));
		return criteria1.list().size() >0? criteria1.list() : null;
	}

	@Override
	public Page getExchangeRateByBankId(int pageNumber, Bank bankId) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ExchangeRate.class).add(Restrictions.eq("bank", bankId));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(ExchangeRate.class).add(Restrictions.eq("bank", bankId));
		criteria1.addOrder(Order.asc("updatedDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
		}
	
	@Override
	public Page getAllExchangeRate(int pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ExchangeRate.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(ExchangeRate.class);
		criteria1.addOrder(Order.asc("updatedDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getCurrencies() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency currency order by currency.currencyName");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommisionSplits> loadCommissionSplits() {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CommisionSplits.class);
		return criteria.list();
	}



}
