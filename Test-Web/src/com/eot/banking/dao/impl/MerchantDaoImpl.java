/* Copyright � EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: MerchantDaoImpl.java
 *
 * Date Author Changes
 * 23 May, 2016 Swadhin Created
 */
package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.MerchantDao;
import com.eot.banking.dto.MerchantDTO;
import com.eot.entity.Merchant;
import com.eot.entity.MerchantCategory;
import com.eot.entity.Outlet;
import com.eot.entity.Terminal;

/**
 * The Class MerchantDaoImpl.
 */
@Repository
public class MerchantDaoImpl extends BaseDaoImpl implements MerchantDao {

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#searchMerchant(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> searchMerchant(Integer bankId, Integer bankGroupId,
			String merchantName, String mobileNumber,String branchId,
			String countryId) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Merchant.class, "mer");
		criteria.setFetchMode("mer", FetchMode.JOIN);
		criteria.createAlias("mer.terminals", "ter", CriteriaSpecification.LEFT_JOIN);

		if( merchantName!= null && ! "".equals(merchantName) ) {
			criteria.add(Restrictions.like("merchantName","%"+merchantName.toLowerCase().trim()+"%"));
		}if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			criteria.add(Restrictions.like("primaryContactMobile","%"+mobileNumber+"%"));
		}
		if( countryId!= null && ! "".equals(countryId) ){
			criteria.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
		}
		if( bankId!= null && ! "".equals(bankId) ){
			criteria.add(Restrictions.eq("ter.bank.bankId", Integer.valueOf(bankId)));		
		}
		if( branchId!= null && ! "".equals(branchId) && !branchId.equals("select")){
			criteria.createCriteria("ter.branch").add(Restrictions.eq("branchId", Long.valueOf(branchId)));		
		}
		if(bankGroupId!=null){
			criteria.createCriteria("ter.bank").add(Restrictions.eq("bankGroup.bankGroupId", bankGroupId));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Merchant> merchantList = (List<Merchant>) getHibernateTemplate().findByCriteria(criteria);
		return merchantList;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getMerchantByMobileNumber(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Merchant getMerchantByMobileNumber(String primaryContactMobile) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Merchant mer where concat(mer.country.isdCode,primaryContactMobile)=:mobileNumber")
				.setParameter("mobileNumber",primaryContactMobile);
		List<Merchant> list=query.list();
		return list.size()>0 ? list.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getMerchantFromMerchantID(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Merchant getMerchantFromMerchantID(Integer merchantId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Merchant as mer where mer.merchantId=:merchantId")
				.setParameter("merchantId",merchantId);
		List<Merchant> list=query.list();
		return  list.size()>0 ? list.get(0) : null ; 
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#searchOutlet(java.lang.String, java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Outlet> searchOutlet(String mobileNumber, Integer bankId,String countryId, Integer status,String location, Integer merchantId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outlet.class, "outlet");
		criteria.setFetchMode("outlet", FetchMode.JOIN);
		criteria.createAlias("outlet.terminals", "ter", CriteriaSpecification.LEFT_JOIN);

		if( mobileNumber!= null && ! "".equals(mobileNumber) ){
			criteria.add(Restrictions.like("primaryContactMobile","%"+mobileNumber+"%"));
		}
		if( countryId!= null && ! "".equals(countryId) ){
			criteria.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
		}
		if( bankId!= null && ! "".equals(bankId) ){
			criteria.add(Restrictions.eq("ter.bank.bankId", Integer.valueOf(bankId)));		
		}
		if( status!= null && ! "".equals(status) && !status.equals("select") && status!=0){
			criteria.add(Restrictions.eq("active", status));	
		}
		if(location!=null && !"".equals(location)){
			criteria.add(Restrictions.eq("location", location));
		}
		if(merchantId!=null && !"".equals(merchantId)){
			criteria.add(Restrictions.eq("merchant.merchantId", merchantId));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Outlet> outlets = (List<Outlet>) getHibernateTemplate().findByCriteria(criteria);
		return outlets;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getOutletByMobileNumber(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Outlet getOutletByMobileNumber(String primaryContactMobile) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Outlet outlet where concat(outlet.country.isdCode,primaryContactMobile)=:mobileNumber")
				.setParameter("mobileNumber",primaryContactMobile);
		List<Outlet> list=query.list();
		return list.size()>0 ? list.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getOutletFromOutletId(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Outlet getOutletFromOutletId(Integer outletId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Outlet as outlet where outlet.outletId=:outletId")
				.setParameter("outletId",outletId);
		List<Outlet> list=query.list();
		return  list.size()>0 ? list.get(0) : null ; 
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getAllActiveMCC()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantCategory> getAllActiveMCC() {
		List<MerchantCategory> mccList = getHibernateTemplate().find("from MerchantCategory mc where mc.status = ?",EOTConstants.ACTIVE);
		return mccList;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#searchTerminal(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Terminal> searchTerminal(Integer outletId, Integer bankId, Integer bankGroupId,
			String mobileNumber, String branchId, String status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Terminal.class, "terminal");
		if(null != mobileNumber && !mobileNumber.equals("")){
			criteria.add(Restrictions.eq("mobileNumber",mobileNumber));
		}
		if(null != bankId && !bankId.equals("") && !bankId.equals("select")){
			criteria.add(Restrictions.eq("terminal.bank.bankId",Integer.valueOf(bankId)));
		}
		if(null != branchId && !branchId.equals("")&& !branchId.equals("select")){
			criteria.add(Restrictions.eq("terminal.branch.branchId",Long.valueOf(branchId)));
		}
		if(null != status && !status.equals("")){
			criteria.add(Restrictions.eq("active",Integer.valueOf(status)));
		}
		criteria.add(Restrictions.eq("terminal.outlet.outletId",outletId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Terminal> terminalList = (List<Terminal>) getHibernateTemplate().findByCriteria(criteria);
		return terminalList;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getTerminalByID(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Terminal getTerminalByID(Integer terminalId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Terminal as ter where ter.terminalId=:terminalId")
				.setParameter("terminalId",terminalId);
		List<Terminal> list=query.list();
		return  list.size()>0 ? list.get(0) : null ; 
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getMerchantListFromMerchantID(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Merchant> getMerchantListFromMerchantID(Integer merchantId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Merchant as mer where mer.merchantId=:merchantId")
				.setParameter("merchantId",merchantId);
		List<Merchant> list=query.list();
		return list;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.MerchantDao#getOutletList(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Outlet> getOutletList(Integer merchant) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Outlet as outlt where outlt.merchant.merchantId=:merchantId")
				.setParameter("merchantId",merchant);
		List<Outlet> list=query.list();
		return list;
	}

	@Override
	// bugId-505 by:rajlaxmi for:showing datatable with active/deactive status
	public List<Outlet> showAllOutlets(String mobileNumber, Object object, String countryId, String status,
			String location) {
		Criteria criteria = getSession().createCriteria(Outlet.class) ;
		@SuppressWarnings("unchecked")
		List<Outlet> outlets = (List<Outlet>) criteria.list();
		return outlets;
		
	}

	@Override
	public boolean checkIfEmailExists(MerchantDTO merchantDTO) {
		Query query=getSessionFactory().getCurrentSession().createQuery("select primaryeMailAddress from Merchant mer where mer.primaryeMailAddress=:email")
				.setParameter("email",merchantDTO.getPrimaryeMailAddress());
		List list=query.list();
		if(list.size()==0)
			return false;
		else
			return true;
	}

	@Override
	public boolean checkForUpdateEmail(MerchantDTO merchantDTO) {
		Query query=getSessionFactory().getCurrentSession().createQuery("select primaryeMailAddress from Merchant mer where mer.primaryeMailAddress=:email and mer.merchantId=:merchantID")
				.setParameter("email",merchantDTO.getPrimaryeMailAddress());
		query.setParameter("merchantID", merchantDTO.getMerchantId());
		List list=query.list();
		if(list.size()==0)
			return false;
		else
			return true;
	}

	@Override
	public boolean checkIfMobileExists(MerchantDTO merchantDTO) {
		Query query=getSessionFactory().getCurrentSession().createQuery("select primaryContactPhone from Merchant mer where mer.primaryContactMobile=:mobileNumber")
				.setParameter("mobileNumber",merchantDTO.getPrimaryContactMobile());
		List list=query.list();
		if(list.size()==0)
			return false;
		else
			return true;
	}
	@Override
	public boolean checkForUpdateMobile(MerchantDTO merchantDTO) {
		Query query=getSessionFactory().getCurrentSession().createQuery("select primaryContactPhone from Merchant mer where mer.primaryContactMobile=:mobileNumber and mer.merchantId=:merchantID")
				.setParameter("mobileNumber",merchantDTO.getPrimaryContactMobile());
		query.setParameter("merchantID", merchantDTO.getMerchantId());
		List list=query.list();
		if(list.size()==0)
			return false;
		else
			return true;
	}

	@Override
	public Terminal getTerminalByMobileNumber(String terminalMobileNumber) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from Terminal as ter where ter.mobileNumber=:mobileNumber")
				.setParameter("mobileNumber",terminalMobileNumber);
		List<Terminal> list=query.list();
		return  list.size()>0 ? list.get(0) : null ; 
	}
}
