/* Copyright � EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: LocationDaoImpl.java
 *
 * Date Author Changes
 * 19 May, 2016 Swadhin Created
 */
package com.eot.banking.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.eot.banking.dao.LocationDao;
import com.eot.banking.dto.LocateUSDTO;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PaginationHelper;
import com.eot.entity.BusinessPartner;
import com.eot.entity.City;
import com.eot.entity.Country;
import com.eot.entity.CountryNames;
import com.eot.entity.Currency;
import com.eot.entity.CurrencyConverter;
import com.eot.entity.ExchangeRate;
import com.eot.entity.HelpDesk;
import com.eot.entity.Language;
import com.eot.entity.LocateUS;
import com.eot.entity.LocateUsNetworkTypeMapping;
import com.eot.entity.LocateUsNetworkTypeMappingPK;
import com.eot.entity.LocationType;
import com.eot.entity.Provenience;
import com.eot.entity.Quarter;
import com.eot.entity.SecurityQuestion;
import com.eot.entity.TimeZone;

//Sql Injection is done by vineeth,on 17-10-2018
/**
 * The Class LocationDaoImpl.
 */
public class LocationDaoImpl extends BaseDaoImpl implements LocationDao{

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountries(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getCountries(Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Country.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Country.class);
		criteria1.addOrder(Order.asc("country"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<Country> countries = criteria1.list();

		return PaginationHelper.getPage(countries, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCities(java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getCities(Integer countryId, Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(City.class);
		criteria.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(City.class);
		criteria1.addOrder(Order.asc("city"));
		criteria1.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<City> city = criteria1.list();

		return PaginationHelper.getPage(city, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getQuarters(java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page getQuarters(Integer cityId,Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Quarter.class);
		criteria.createCriteria("city").add(Restrictions.eq("cityId",Integer.valueOf(cityId)));
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Quarter.class);
		criteria1.addOrder(Order.asc("city"));
		criteria1.createCriteria("city").add(Restrictions.eq("cityId",Integer.valueOf(cityId)));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		List<Quarter> quarter = criteria1.list();

		return PaginationHelper.getPage(quarter, totalCount, appConfig.getResultsPerPage(), pageNumber);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllCountries()
	 */
	@Override
	public List<Country> getAllCountries() {

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Country.class);
		criteria1.addOrder(Order.asc("country"));

		return criteria1.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllCities(java.lang.Integer)
	 */
	/*
	Author : Koushik
	Date : 21/06/2018
	Purpose : Bug 5311
	//Start
	*/
	@Override
	public List<City> getAllCities(Integer countryId) {
		if(countryId!=null) {
			Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(City.class);
			criteria1.addOrder(Order.asc("city"));
			criteria1.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
			return criteria1.list();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllQuarters(java.lang.Integer)
	 */
	@Override
	public List<Quarter> getAllQuarters(Integer cityId) {
		if(cityId!=null) {
			Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Quarter.class);
			criteria1.addOrder(Order.asc("city"));
			criteria1.createCriteria("city").add(Restrictions.eq("cityId",Integer.valueOf(cityId)));
			return criteria1.list();
		}
		return null;
	}
	//End

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getTimeZones()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TimeZone> getTimeZones() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from TimeZone timezone order by timezone.timeZoneDesc" ).setCacheable(true);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getQuestions(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SecurityQuestion> getQuestions(String locale) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from SecurityQuestion where locale=:locale").setCacheable(true);
		query.setParameter("locale",locale);
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCity(java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public City getCity(String cityName,Integer countryID)  {
		Query query = getSessionFactory().getCurrentSession().createQuery("from City where city=:cityName and country.countryId=:countryID");
		query.setParameter("cityName", cityName);
		query.setParameter("countryID", countryID);
		List<City> cityList = query.list();
		return cityList.size() >0 ? cityList.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getQuarter(java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Quarter getQuarter(String quarter,Integer cityId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Quarter qr where qr.quarter=:quarter and qr.city.cityId=:cityId");
		query.setParameter("quarter", quarter);
		query.setParameter("cityId", cityId);
		List<Quarter> quarterList = query.list();
		return quarterList.size() >0 ? quarterList.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountry(java.lang.Integer)
	 */
	@Override
	public Country getCountry(Integer countryID) {
		return getHibernateTemplate().get(Country.class, countryID);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCurrencies()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Currency> getCurrencies() {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency currency order by currency.currencyName");
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryDetails(java.lang.Integer)
	 */
	@Override
	public Country getCountryDetails(Integer countryId) {
		return getHibernateTemplate().get(Country.class,countryId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCityDetails(java.lang.Integer)
	 */
	@Override
	public City getCityDetails(Integer cityId) {
		return getHibernateTemplate().get(City.class,cityId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getQuarterDetails(java.lang.Long)
	 */
	@Override
	public Quarter getQuarterDetails(Long quarterId) {		
		return getHibernateTemplate().get(Quarter.class,quarterId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CountryNames getCountryByName(String countryName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from CountryNames as cn where cn.countryName=:countryName").setParameter("countryName", countryName);
		List<CountryNames> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCityByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public City getCityByName(String city) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from City as b where b.city=:city").setParameter("city", city);
		List<City> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getQuarterByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Quarter getQuarterByName(String quarter) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Quarter as b where b.quarter=:quarter").setParameter("quarter", quarter);
		List<Quarter> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCurrencyDetails(java.lang.Integer)
	 */
	@Override
	public Currency getCurrencyDetails(Integer currencyId) {		
		return getHibernateTemplate().get(Currency.class,currencyId);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCurrencyByCode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Currency getCurrencyByCode(String currencyCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency cur  where cur.currencyCode=:currencyCode").setParameter("currencyCode", currencyCode);
		List<Currency> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getLanguageList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Language> getLanguageList() {	
		/*---Author - Koushik---Date - 09-07-2018---Bug id 5603 fixed---start---*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from Language").setCacheable(true);	
		/*end*/
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryNames(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CountryNames> getCountryNames(Integer countryId) {		
		Query query = getSessionFactory().getCurrentSession().createQuery("from CountryNames cn where cn.comp_id.countryId=:countryId order BY LanguageCode").setParameter("countryId", countryId).setCacheable(true);	
		return query.list();

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllCurrencies(java.lang.Integer)
	 */
	@Override
	public Page getAllCurrencies(Integer pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Currency.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Currency.class);
		criteria1.addOrder(Order.asc("currencyName"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/

		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCurrencyDetailsByIdCode(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Currency getCurrencyDetailsByIdCode(Integer currencyId, String currencyCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency cur  where cur.currencyId!=:currencyId and cur.currencyCode=:currencyCode")
				.setParameter("currencyId", currencyId)
				.setParameter("currencyCode", currencyCode);
		List<Currency> list = query.list();
		return list.size()>0 ? list.get(0) : null ;

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByCode(java.lang.String)
	 */
	@Override
	public Country getCountryByCode(String countryCodeAlpha2) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryCodeAlpha2=:countryCodeAlpha2")
				.setParameter("countryCodeAlpha2", countryCodeAlpha2);		       
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByIdCode(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Country getCountryByIdCode(Integer countryId,String countryCodeAlpha2) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryId!=:countryId and cu.countryCodeAlpha2=:countryCodeAlpha2")
				.setParameter("countryId", countryId)
				.setParameter("countryCodeAlpha2", countryCodeAlpha2);
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByNameId(java.lang.Integer, java.lang.String)
	 */
	@Override
	public CountryNames getCountryByNameId(Integer countryId, String countryName) {	
		Query query = getSessionFactory().getCurrentSession().createQuery("from CountryNames cn  where cn.comp_id.countryId!=:countryId and cn.countryName=:countryName")
				.setParameter("countryId", countryId)
				.setParameter("countryName", countryName);
		List<CountryNames> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getNumericCurrency(java.lang.Integer)
	 */
	@Override
	public Currency getNumericCurrency(Integer currencyCodeNumeric) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency cur  where cur.currencyCodeNumeric=:currencyCodeNumeric").setParameter("currencyCodeNumeric", currencyCodeNumeric);
		List<Currency> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getNumericCurrency(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Currency getNumericCurrency(Integer currencyId,Integer currencyCodeNumeric) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency cur  where cur.currencyId!=:currencyId and cur.currencyCodeNumeric=:currencyCodeNumeric")
				.setParameter("currencyId", currencyId)
				.setParameter("currencyCodeNumeric", currencyCodeNumeric);
		List<Currency> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCity(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	@Override
	public City getCity(Integer cityId, String cityName, Integer countryID) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from City where city=:cityName and country.countryId=:countryID and city.cityId!=:cityId");
		query.setParameter("cityName", cityName);
		query.setParameter("countryID", countryID);
		query.setParameter("cityId", cityId);
		List<City> cityList = query.list();
		return cityList.size() >0 ? cityList.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getQuarter(java.lang.Long, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Quarter getQuarter(Long quarterId, String quarter, Integer cityId) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Quarter qr where qr.quarter=:quarter and qr.city.cityId=:cityId and qr.quarterId !=:quarterId");
		query.setParameter("quarter", quarter);
		query.setParameter("cityId", cityId);
		query.setParameter("quarterId", quarterId);
		List<Quarter> quarterList = query.list();
		return quarterList.size() >0 ? quarterList.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByCode3(java.lang.String)
	 */
	@Override
	public Country getCountryByCode3(String countryCodeAlpha3) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryCodeAlpha3=:countryCodeAlpha3")
				.setParameter("countryCodeAlpha3", countryCodeAlpha3);		       
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByCodeNumeric(java.lang.Integer)
	 */
	@Override
	public Country getCountryByCodeNumeric(Integer countryCodeNumeric) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryCodeNumeric=:countryCodeNumeric")
				.setParameter("countryCodeNumeric", countryCodeNumeric);		       
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByIsdCode(java.lang.Integer)
	 */
	@Override
	public Country getCountryByIsdCode(Integer isdCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.isdCode=:isdCode")
				.setParameter("isdCode", isdCode);		       
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByIdCode3(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Country getCountryByIdCode3(Integer countryId,String countryCodeAlpha3) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryId!=:countryId and cu.countryCodeAlpha3=:countryCodeAlpha3")
				.setParameter("countryId", countryId)
				.setParameter("countryCodeAlpha3", countryCodeAlpha3);
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByIdCodeNumeric(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Country getCountryByIdCodeNumeric(Integer countryId,Integer countryCodeNumeric) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryId!=:countryId and cu.countryCodeNumeric=:countryCodeNumeric")
				.setParameter("countryId", countryId)
				.setParameter("countryCodeNumeric", countryCodeNumeric);
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCountryByIdIsdCode(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Country getCountryByIdIsdCode(Integer countryId, Integer isdCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Country cu  where cu.countryId!=:countryId and cu.isdCode=:isdCode")
				.setParameter("countryId", countryId)
				.setParameter("isdCode", isdCode);
		List<Country> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCurrencyByName(java.lang.String)
	 */
	@Override
	public Currency getCurrencyByName(String currencyName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency cur where cur.currencyName=:currencyName").setParameter("currencyName", currencyName);
		List<Currency> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getCurrencyByName(java.lang.Integer, java.lang.String)
	 */
	@Override
	public Currency getCurrencyByName(Integer currencyId, String currencyName) {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Currency cur where cur.currencyId!=:currencyId and cur.currencyName=:currencyName")
				.setParameter("currencyId", currencyId).setParameter("currencyName", currencyName);
		List<Currency> list = query.list();
		return list.size()>0 ? list.get(0) : null ;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getExchangeRateFromCurrencyId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ExchangeRate getExchangeRateFromCurrencyId(Integer currencyId, Integer bankId) {
		@SuppressWarnings("unchecked")
	/*	List<ExchangeRate> list = getHibernateTemplate().find("from ExchangeRate er where er.currency.currencyId =? and" +
				" er.bank.bankId =?", currencyId,bankId);*/
		
		Query query = getSessionFactory().getCurrentSession().createQuery("from ExchangeRate er where er.currency.currencyId =:currencyId and er.bank.bankId =:bankId")
				.setParameter("currencyId", currencyId).setParameter("bankId", bankId);
		List<ExchangeRate> list = query.list();
		return list.size() > 0 ? list.get(0) : null;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllExchangeRate(int)
	 */
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

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllActiveLocationType(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<LocationType> getAllActiveLocationType(String locale, Integer status) {
		@SuppressWarnings("unchecked")
	/*	List<LocationType> list = getHibernateTemplate().find("from LocationType lt where lt.locale =? and" +
				" lt.status =?", locale,status);*/
		
		Query query = getSessionFactory().getCurrentSession().createQuery("from LocationType lt where lt.locale =:locale and lt.status =:status")
				.setParameter("locale", locale).setParameter("status", status);
		List<LocationType> list = query.list();
		
		return list;
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getAllLocationDetail(int)
	 */
	@Override
	public Page getAllLocationDetail(int pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(LocateUS.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(LocateUS.class);
		criteria1.addOrder(Order.asc("locatUsId"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());
*/
		return PaginationHelper.getPage( criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.dao.LocationDao#getLocationDetailsFromLocatUsId(java.lang.Long)
	 */
	@Override
	public LocateUS getLocationDetailsFromLocatUsId(Long locatUsId) {
		/*@SuppressWarnings("unchecked")
		List<LocateUS> list = getHibernateTemplate().find("from LocateUS lu where lu.locatUsId=?", locatUsId);*/
		Query query = getSessionFactory().getCurrentSession().createQuery("from LocateUS lu where lu.locatUsId =:locatUsId")
				.setParameter("locatUsId", locatUsId);
		List<LocateUS> list = query.list();
		
		return list.size()>0 ? list.get(0) : null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Page getProvenienceList(Integer countryId, int pageNumber) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Provenience.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());
		
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(Provenience.class);
		criteria.createCriteria("country").add(Restrictions.eq("countryId",Integer.valueOf(countryId)));
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Provenience getProvenience(String provenience,Integer countryID)  {
		Query query = getSessionFactory().getCurrentSession().createQuery("from Provenience where provenienceName=:provenienceName and country.countryId=:countryID");
		query.setParameter("provenienceName", provenience);
		query.setParameter("countryID", countryID);
		List<Provenience> prList = query.list();
		return prList.size() >0 ? prList.get(0) : null ;
	}

	@Override
	public Provenience getProvenienceDetails(Integer provenienceId) {
		return getHibernateTemplate().get(Provenience.class,provenienceId);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<LocateUSDTO> getNetworkTypes() {	

		Query query=getSessionFactory().getCurrentSession().createQuery("from NetworkType");


		return query.list();

	}
	
	@Override
	public void deleteNetWorkType(Long locatUsId) {
		//@Start, Optimized query, by Murari, dated : 23-07-2018
		String qry = "delete from LocateUsNetworkTypeMapping as nt where nt.locateUs.locatUsId =:locatUsId";
		Query query = getSessionFactory().getCurrentSession().createQuery(qry).setParameter("locatUsId", locatUsId);
		//@End
		/*String qry = "delete from LocateUsNetworkTypeMapping as nt where nt.locateUs.locatUsId =" +locatUsId;
		Query query = getSessionFactory().getCurrentSession().createQuery(qry);*/
		query.executeUpdate();
	}

	@Override
	public Page getAllConversionRate(int pageNumber) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CurrencyConverter.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().get(0).toString());

		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(CurrencyConverter.class);
		criteria1.addOrder(Order.asc("updatedDate"));
		/*criteria1.setFirstResult((pageNumber-1)*appConfig.getResultsPerPage());
		criteria1.setMaxResults(appConfig.getResultsPerPage());*/
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}
	@SuppressWarnings("unchecked")
	@Override
	public CurrencyConverter getCurrencyConvertorFromCurrencyIds(Integer baseCurrencyId, Integer counterCurrencyId,
			Integer bankId) {
		
		List<CurrencyConverter> list = getHibernateTemplate().find("from CurrencyConverter cc where cc.currencyByBaseCurrencyId.currencyId =? and cc.currencyByCounterCurrencyId.currencyId =? and cc.bank.bankId =?", baseCurrencyId,counterCurrencyId,bankId);
		
		Query query = getSessionFactory().getCurrentSession().createQuery("from CurrencyConverter cc where cc.currencyByBaseCurrencyId.currencyId=:baseCurrencyId and cc.currencyByCounterCurrencyId.currencyId=:counterCurrencyId and cc.bank.bankId =:bankId");
		query.setParameter("baseCurrencyId", baseCurrencyId);
		query.setParameter("counterCurrencyId", counterCurrencyId);
		query.setParameter("bankId", bankId);
		List<Provenience> prList = query.list();
		
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Page getAllHelpDeskList(Integer pageNumber) {
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(HelpDesk.class);
		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		int totalCount = Integer.parseInt(criteria.list().isEmpty()?"0":criteria.list().get(0).toString());
		Criteria criteria1 = getSessionFactory().getCurrentSession().createCriteria(HelpDesk.class);
		
		return PaginationHelper.getPage(criteria1.list(), totalCount, appConfig.getResultsPerPage(), pageNumber);
	}

	@Override
	public HelpDesk getHelpDeskDetails(Integer helpDeskId) {		
		return getHibernateTemplate().get(HelpDesk.class,helpDeskId);
	}

	@Override
	public HelpDesk getHelpDeskByContactNumber(String mobileNumber) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from HelpDesk hd where mobileNumber=:mobileNumber")
                .setParameter("mobileNumber",mobileNumber);
		List<HelpDesk> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}

	@Override
	public HelpDesk getHelpDeskByEmailId(String emailId) {
		Query query=getSessionFactory().getCurrentSession().createQuery("from HelpDesk hd where emailId=:emailId")
                .setParameter("emailId",emailId);
		List<HelpDesk> list=query.list();
		return list.size()>0 ? list.get(0) :null;
	}
	
	@Override
	public LocationType getLocationType(Integer locationTypeId) {
		return getHibernateTemplate().get(LocationType.class,locationTypeId);
	}

}
