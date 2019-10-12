package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eot.banking.dao.ThemeDao;
import com.eot.banking.dto.ThemeConfDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Branding;
import com.eot.entity.Mobiletheamcolorconfig;

@Repository("themeDao")
public class ThemeDaoImpl extends BaseDaoImpl implements ThemeDao{

	@Override
	public Page getBanksWithThemes(int pageNumber, ThemeConfDTO themeDTO) {
		
		int totalCount=0;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Mobiletheamcolorconfig.class);

		if( themeDTO.getBankId() != null && ! "".equals(themeDTO.getBankId()) ){
			criteria.add(Restrictions.eq("bank.bankId", themeDTO.getBankId()));
		}
		
		if( themeDTO.getAppType() != null && ! "".equals(themeDTO.getAppType()) ){
			criteria.add(Restrictions.eq("appTypeId", themeDTO.getAppType().toString()));
		}
		
		criteria.addOrder(Order.desc("updatedDate"));
		
		List list = criteria.list();
		
		if(list!=null && list.size()>0){
			
			totalCount=list.size();
		}
		  
		return PaginationHelper.getPage(list, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public Page getBanksWithWebThemes(int pageNumber, ThemeConfDTO themeDTO) {
		
		int totalCount=0;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Branding.class);
		criteria.addOrder(Order.desc("updatedDate"));
		
		List list = criteria.list();
		
		if(list!=null && list.size()>0){
			
			totalCount=list.size();
		}
		  
		return PaginationHelper.getPage(list, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	
	@Override
	public Mobiletheamcolorconfig getUniqueMobileTheme(Integer bankId, String appType) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query = getSessionFactory().getCurrentSession().createQuery("from Mobiletheamcolorconfig m where m.bank.bankId=:bankId and m.appTypeId=:appType")
				.setParameter("bankId", bankId)
				.setParameter("appType", appType);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Mobiletheamcolorconfig m where m.bank.bankId="+bankId+" and m.appTypeId="+appType);*/
		return query.list().size()>0 ? (Mobiletheamcolorconfig) query.list().get(0) : null;
	}

	@Override
	public Branding getUniqueWebTheme(Integer bankId) {
		//Query query = getSessionFactory().getCurrentSession().createQuery("from Branding m where m.bank.bankId="+bankId+" and m.appTypeId='"+appType+"'");
		//Now theme is unique on the basis of bank only
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		Query query = getSessionFactory().getCurrentSession().createQuery("from Branding m where m.bank.bankId=:bankId").setParameter("bankId", bankId);
		//@End
		/*Query query = getSessionFactory().getCurrentSession().createQuery("from Branding m where m.bank.bankId="+bankId);*/
		return query.list().size()>0 ? (Branding) query.list().get(0) : null;
	}

	
}
