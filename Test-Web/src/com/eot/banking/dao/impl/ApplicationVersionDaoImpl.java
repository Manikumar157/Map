package com.eot.banking.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.eot.banking.dao.ApplicationVersionDao;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.Version;
import com.eot.entity.VersionDetails;

public class ApplicationVersionDaoImpl extends BaseDaoImpl implements ApplicationVersionDao {

	@SuppressWarnings("unchecked")
	@Override
	public Page getApplicationVersionList(Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Version.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Version.class);
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
		List<Version> versions = criteria1.list();

		return PaginationHelper.getPage(versions, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Version getApplicationVersion(String versionNumber) {

		Query query = getSessionFactory().getCurrentSession().createQuery("from Version as v where v.versionNumber=:versionNumber").setParameter("versionNumber", versionNumber);

		List<Version> versionList =  query.list();

		return versionList.size()>0 ? versionList.get(0) : null ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page getApplicationVersionDetailsList(String versionNumber,Integer pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(VersionDetails.class);
		criteria.add(Restrictions.eq("version.versionNumber", versionNumber));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(VersionDetails.class);
		criteria1.add(Restrictions.eq("version.versionNumber", versionNumber));
		criteria1.addOrder(Order.asc("channel"));
		criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
		List<VersionDetails> versionDetails = criteria1.list();

		return PaginationHelper.getPage(versionDetails, totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public VersionDetails getApplicationVersionDetails(Integer versionId) {

		return getHibernateTemplate().get(VersionDetails.class,versionId);
	}

	@Override
	public Integer getMaxReleaseNumber() {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Version.class);
		criteria.setProjection(Projections.max("releaseNumber"));
		Integer releaseNumber = (Integer)criteria.uniqueResult();

		return releaseNumber != null ? releaseNumber : 0;

	}

	@SuppressWarnings("unchecked")
	@Override
	public String getApplicationVersionDao() {
		Integer releaseNumber = getMaxReleaseNumber();
		Query query = getSessionFactory().getCurrentSession().createQuery("select versionNumber from Version as v where v.releaseNumber=:releaseNumber").setParameter("releaseNumber", releaseNumber);
		List<String> versionList = query.list();
		String version=versionList.get(0);
		return version;
	}

}
