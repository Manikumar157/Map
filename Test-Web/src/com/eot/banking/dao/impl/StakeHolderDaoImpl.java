package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.StakeHolderDao;
import com.eot.banking.dto.StakeHolderDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Account;
import com.eot.entity.ServiceChargeSplit;
import com.eot.entity.StakeHolder;

public class StakeHolderDaoImpl extends BaseDaoImpl implements StakeHolderDao {


	@SuppressWarnings("unchecked")
	@Override
	public List<StakeHolder> getStakeHolders() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from StakeHolder sth order by lower(sth.stakeholderOrganization)");
		return query.list();
	}

	@SuppressWarnings("finally")
	@Override
	public StakeHolder getStakeHolderDetails(Integer stakeholderId) {
/*		return getHibernateTemplate().get(StakeHolder.class,stakeholderId);
*/		Session session = null;
		Query query = null;
		StakeHolder stakeHolder= null;
		StakeHolder stakeHolder1= null;
		try {
			session = getSessionFactory().getCurrentSession();
			query = session.createQuery("from StakeHolder sth where sth.stakeholderId=:stakeholderId").setParameter("stakeholderId", stakeholderId);
			stakeHolder = (StakeHolder) query.list().get(0);
			stakeHolder1= (StakeHolder) session.merge(stakeHolder);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			 if(session.isOpen())
				 session.close();
		}
		return stakeHolder1;
	}
	
	@Override
	public Account getAccount(String accountNumber) {
		return getHibernateTemplate().get(Account.class,accountNumber);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceChargeSplit getServiceChargeSplit(String accountNumber, Integer transactionType,Integer bankId) {		
		Query query=getSessionFactory().getCurrentSession().createQuery("from ServiceChargeSplit sth where sth.accountNumber=:accountNumber and sth.transactionType.transactionType=:transactionType and sth.bank.bankId=:bankId")
				.setParameter("accountNumber",accountNumber).setParameter("transactionType",transactionType).setParameter("bankId", bankId);
		List<ServiceChargeSplit> list=query.list();
		return list.size() > 0 ? list.get(0) : null ; 
	}

	@SuppressWarnings("unchecked")
	@Override
	public StakeHolder getStakeHolderByName(String stakeholderOrganization) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from StakeHolder as stake where stake.stakeholderOrganization=:stakeholderOrganization")
				.setParameter("stakeholderOrganization",stakeholderOrganization);
		List<StakeHolder> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public StakeHolder getStakeHolderByMobileNumber(String contactMobile) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from StakeHolder as stake where stake.contactMobile=:contactMobile")
				.setParameter("contactMobile",contactMobile);
		List<StakeHolder> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	
	@Override
	public StakeHolder getUpdateStakeHolderByMobileNumber(StakeHolderDTO stakeHolderDTO) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from StakeHolder as stake where stake.contactMobile=:contactMobile and stake.stakeholderId=:stakeholderID")
				.setParameter("contactMobile",stakeHolderDTO.getContactMobile());
		query.setParameter("stakeholderID", stakeHolderDTO.getStakeholderId());
		List<StakeHolder> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	@Override
	public Page getStakeHolderList(Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(StakeHolder.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(StakeHolder.class);
		criteria1.addOrder(Order.asc("stakeholderOrganization"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public StakeHolder getStakeHolderByName(String stakeholderOrganization,Integer stakeholderId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from StakeHolder as stake where stake.stakeholderOrganization=:stakeholderOrganization and stake.stakeholderId !=:stakeholderId")
				.setParameter("stakeholderOrganization",stakeholderOrganization)
				.setParameter("stakeholderId", stakeholderId);
		List<StakeHolder> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}
	//Author Name:Abu kalam Azad Date:19/07/2018 purpouses:stake holder is not updated sucessfully
	//start..changed in query
	@Override
	public void updateStakeHolder(StakeHolder stakeholder) {
		String hql="update StakeHolder as sh set sh.stakeholderOrganization=:stakeholderOrganization ,"
				+ " sh.address=:address , sh.contactPersonName=:contactPersonName , sh.contactAddress=:contactAddress ,"
				+ " sh.contactPhone=:contactPhone , sh.contactMobile=:contactMobile , sh.emailAddress=:emailAddress ,"
				+ " sh.country=:country , sh.account=:account where sh.stakeholderId=:stakeholderId";
		//end
		Query query=getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("stakeholderOrganization", stakeholder.getStakeholderOrganization());
		query.setParameter("address",stakeholder.getAddress());
		query.setParameter("contactPersonName",stakeholder.getContactPersonName());
		query.setParameter("contactAddress",stakeholder.getContactAddress());
		query.setParameter("contactPhone",stakeholder.getContactPhone());
		query.setParameter("contactMobile",stakeholder.getContactMobile());
		query.setParameter("emailAddress",stakeholder.getEmailAddress());
		query.setParameter("country",stakeholder.getCountry());
		query.setParameter("account",stakeholder.getAccount());
		query.setParameter("stakeholderId",stakeholder.getStakeholderId());
		query.executeUpdate();
	}
}
