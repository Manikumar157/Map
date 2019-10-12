package com.eot.banking.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.dao.BaseDao;

/**
 * @author Sajeev
 */
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {
	
	protected AppConfigurations appConfig ;

    public AppConfigurations getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfigurations appConfig) {
		this.appConfig = appConfig;
	}

	public Serializable save(Object obj) {
        return getHibernateTemplate().save(obj);
    }

    public void update(Object obj) {
    	Object obj1 = getHibernateTemplate().getSessionFactory().getCurrentSession().merge(obj);
        getHibernateTemplate().update(obj1);
        /*getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
        getHibernateTemplate().getSessionFactory().getCurrentSession().clear();*/
    }
    
    public void delete(Object obj) {
        getHibernateTemplate().delete(obj);
    }
    
    public void deleteList(Set<Object> objs) {
        getHibernateTemplate().deleteAll(objs);
    }

	public void saveList(List<Object> list) {
		getHibernateTemplate().saveOrUpdateAll(list) ;
	}

	public void flush() {
		getHibernateTemplate().flush() ;		
	}
	
	public void saveOrUpdate(Object obj){
		getHibernateTemplate().saveOrUpdate(obj);
	}

	@Override
	public void merge(Object obj) {
		getHibernateTemplate().merge(obj);
		
	}
	
	@Override
	public void evict(Object obj) {
		getHibernateTemplate().evict(obj);
		
	}
	
}
