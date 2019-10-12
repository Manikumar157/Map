/* Copyright EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: LocationServiceImpl.java
 *
 * Date Author Changes
 * 19 May, 2016 Swadhin Created
 */
package com.eot.banking.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eot.banking.common.EOTConstants;
import com.eot.banking.dao.BankDao;
import com.eot.banking.dao.LocationDao;
import com.eot.banking.dao.OperatorDao;
import com.eot.banking.dto.CityDTO;
import com.eot.banking.dto.CountryDTO;
import com.eot.banking.dto.CountryRequest;
import com.eot.banking.dto.CurrencyConvertorDTO;
import com.eot.banking.dto.CurrencyDTO;
import com.eot.banking.dto.ExchangeRateDTO;
import com.eot.banking.dto.HelpDeskDTO;
import com.eot.banking.dto.LocateUSDTO;
import com.eot.banking.dto.ProvenienceDTO;
import com.eot.banking.dto.QuarterDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.RESTCall;
import com.eot.entity.Bank;
import com.eot.entity.BankTellers;
import com.eot.entity.BusinessPartner;
import com.eot.entity.City;
import com.eot.entity.Country;
import com.eot.entity.CountryNames;
import com.eot.entity.CountryNamesPK;
import com.eot.entity.Currency;
import com.eot.entity.CurrencyConverter;
import com.eot.entity.ExchangeRate;
import com.eot.entity.HelpDesk;
import com.eot.entity.Language;
import com.eot.entity.LocateUS;
import com.eot.entity.LocateUsNetworkTypeMapping;
import com.eot.entity.LocateUsNetworkTypeMappingPK;
import com.eot.entity.LocationType;
import com.eot.entity.NetworkType;
import com.eot.entity.Provenience;
import com.eot.entity.Quarter;

/**
 * The Class LocationServiceImpl.
 */
@Service("locationService")
@Transactional(readOnly=true)
public class LocationServiceImpl implements LocationService{
	
	
	private static final String CountryURL= "http://localhost:8080/MobileChannelHandler/rest/service/country";

	/** The location dao. */
	@Autowired
	private LocationDao locationDao ;

	/** The operator dao. */
	@Autowired
	private OperatorDao operatorDao;

	/** The bank dao. */
	@Autowired
	private BankDao bankDao ;
	
	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCurrencyList()
	 */
	@Override
	public List<Currency> getCurrencyList() {		
		return locationDao.getCurrencies();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCountriesList(java.lang.Integer)
	 */
	@Override
	public Page getCountriesList(Integer pageNumber) {		
		return locationDao.getCountries(pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCityList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getCityList(Integer countryId,Integer pageNumber) {		
		return locationDao.getCities(countryId,pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getQuarterList(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getQuarterList(Integer cityId,Integer pageNumber) {		
		return locationDao.getQuarters(cityId,pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#addCountries(com.eot.banking.dto.CountryDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addCountries(CountryDTO countryDTO) throws EOTException {

		for(String countryName:countryDTO.getCountry()){
			CountryNames countryNames=locationDao.getCountryByName(countryName);	
			if(countryNames!=null){
				throw new EOTException(ErrorConstants.COUNTRY_NAME_EXIST);
			}
		}	
		Country country=locationDao.getCountryByCode(countryDTO.getCountryCodeAlpha2());
		if(country!=null){
			throw new EOTException(ErrorConstants.COUNTRY_CODE_EXIST);
		}	

		Country countryCode3=locationDao.getCountryByCode3(countryDTO.getCountryCodeAlpha3());
		if(countryCode3!=null){
			throw new EOTException(ErrorConstants.COUNTRY_CODE3_EXIST);
		}

		Country countryCodeNumeric=locationDao.getCountryByCodeNumeric(countryDTO.getCountryCodeNumeric());
		if(countryCodeNumeric!=null){
			throw new EOTException(ErrorConstants.COUNTRY_CODE_NUMERIC_EXIST);
		}

		Country countryIsdCode=locationDao.getCountryByIsdCode(countryDTO.getIsdCode());
		if(countryIsdCode!=null){
			throw new EOTException(ErrorConstants.COUNTRY_ISDCODE_EXIST);
		}


		country=new Country();		
		country.setCountry(countryDTO.getCountry()[0]);
		country.setCountryCodeAlpha2(countryDTO.getCountryCodeAlpha2().toUpperCase());
		country.setCountryCodeAlpha3(countryDTO.getCountryCodeAlpha3().toUpperCase());		
		country.setCountryCodeNumeric(countryDTO.getCountryCodeNumeric());
		country.setIsdCode(countryDTO.getIsdCode());
		country.setMobileNumberLength(countryDTO.getMobileNumberLength());		
		Currency currency=new Currency();
		currency.setCurrencyId(countryDTO.getCurrencyId());
		country.setCurrency(currency);
		locationDao.save(country);	

		//List<Language> list=locationDao.getLanguageList();// not in use so commented, by Sudhanshu, dated : 09-07-2018
			for (int i = 0; i < countryDTO.getCountry().length; i++) {
				CountryNamesPK countryNamesPK = new CountryNamesPK();
				CountryNames countryNames = new CountryNames();
				countryNamesPK.setCountryId(country.getCountryId());
				//countryNamesPK.setLanguageCode(list.get(i).getLanguageCode());// instead of list from db, we are taking from dto, so commented, by Sudhanshu, dated : 09-07-2018
				countryNamesPK.setLanguageCode(countryDTO.getLanguageCode()[i]);
				countryNames.setComp_id(countryNamesPK);
				countryNames.setCountryName(countryDTO.getCountry()[i]);
				locationDao.save(countryNames);
		}
		//saveOrUpdateCountryInFI(country);
	}


	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCountry(java.lang.Integer)
	 */
	@Override
	public CountryDTO getCountry(Integer countryId)  throws EOTException{

		Country country=locationDao.getCountryDetails(countryId);	
		if(country==null){
			throw new EOTException(ErrorConstants.INVALID_COUNTRY);
		}else{				
			List<CountryNames> countryNames=locationDao.getCountryNames(countryId);			
			CountryDTO countryDTO=new CountryDTO();
			countryDTO.setCountryId(country.getCountryId());			
			countryDTO.setCountryCodeAlpha2(country.getCountryCodeAlpha2());
			countryDTO.setCountryCodeAlpha3(country.getCountryCodeAlpha3());		      
			countryDTO.setCountryCodeNumeric(country.getCountryCodeNumeric());
			countryDTO.setIsdCode(country.getIsdCode());
			countryDTO.setMobileNumberLength(country.getMobileNumberLength());		      
			countryDTO.setCurrencyId(country.getCurrency().getCurrencyId());

			int i = 0 ;			
			String[] country1=new String[countryNames.size()];			
			String[] languageCode=new String[countryNames.size()];	
			for (CountryNames cn : countryNames) {
				country1[i] = cn.getCountryName();
				languageCode[i]=cn.getComp_id().getLanguageCode();

				i++;				
			}	
			countryDTO.setCountry(country1);	
			countryDTO.setLanguageCode(languageCode);
			return countryDTO;

		}
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#updateCountries(com.eot.banking.dto.CountryDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateCountries(CountryDTO countryDTO) throws EOTException {

		for(String countryName:countryDTO.getCountry()){
			CountryNames countryNames=locationDao.getCountryByNameId(countryDTO.getCountryId(),countryName);	
			if(countryNames!=null){
				throw new EOTException(ErrorConstants.COUNTRY_NAME_EXIST);
			}
		}	

		Country country1=locationDao.getCountryByIdCode(countryDTO.getCountryId(),countryDTO.getCountryCodeAlpha2());
		if(country1!=null){
			throw new EOTException(ErrorConstants.COUNTRY_CODE_EXIST);
		}

		Country countryCode3=locationDao.getCountryByIdCode3(countryDTO.getCountryId(),countryDTO.getCountryCodeAlpha3());
		if(countryCode3!=null){
			throw new EOTException(ErrorConstants.COUNTRY_CODE3_EXIST);
		}		

		Country countryCodeNumeric=locationDao.getCountryByIdCodeNumeric(countryDTO.getCountryId(),countryDTO.getCountryCodeNumeric());
		if(countryCodeNumeric!=null){
			throw new EOTException(ErrorConstants.COUNTRY_CODE_NUMERIC_EXIST);
		}

		Country countryIsdCode=locationDao.getCountryByIdIsdCode(countryDTO.getCountryId(),countryDTO.getIsdCode());
		if(countryIsdCode!=null){
			throw new EOTException(ErrorConstants.COUNTRY_ISDCODE_EXIST);
		}		

		else{
			Country country=locationDao.getCountryDetails(countryDTO.getCountryId());
			country.setCountry(countryDTO.getCountry()[0]);
			country.setCountryCodeAlpha2(countryDTO.getCountryCodeAlpha2().toUpperCase());
			country.setCountryCodeAlpha3(countryDTO.getCountryCodeAlpha3().toUpperCase());		
			country.setCountryCodeNumeric(countryDTO.getCountryCodeNumeric());
			country.setIsdCode(countryDTO.getIsdCode());
			country.setMobileNumberLength(countryDTO.getMobileNumberLength());
			Currency currency=new Currency();
			currency.setCurrencyId(countryDTO.getCurrencyId());
			country.setCurrency(currency);            
			locationDao.update(country);
			List<Language> list=locationDao.getLanguageList();

			for(int i=0;i<countryDTO.getCountry().length;i++){
				CountryNamesPK countryNamesPK = new CountryNamesPK();            
				CountryNames countryNames = new CountryNames();			
				countryNamesPK.setCountryId(country.getCountryId());
				//countryNamesPK.setLanguageCode(list.get(i).getLanguageCode());// instead of list from db, we are taking from dto, so commented, by Sudhanshu, dated : 09-07-2018	
				countryNamesPK.setLanguageCode(countryDTO.getLanguageCode()[i]);
				countryNames.setComp_id(countryNamesPK);
				countryNames.setCountryName(countryDTO.getCountry()[i]);	

				locationDao.saveOrUpdate(countryNames);

			}
			
			//saveOrUpdateCountryInFI(country);
		}		
	}


	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#addCities(com.eot.banking.dto.CityDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addCities(CityDTO cityDTO) throws EOTException {
		
		String countryId = EOTConstants.DEFAULT_COUNTRY.toString();
		if(null != cityDTO.getCountryId() && !cityDTO.getCountryId().equals(""))
			countryId = cityDTO.getCountryId();

		City city=locationDao.getCity(cityDTO.getCity(),new Integer(countryId));
		if(city!=null){
			throw new EOTException(ErrorConstants.CITY_NAME_EXIST);
		}
		city=new City();
		city.setCity(cityDTO.getCity());
		Country country = new Country();
		country.setCountryId(Integer.parseInt(countryId));
		city.setCountry(country);
		locationDao.save(city);		
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCity(java.lang.Integer)
	 */
	@Override
	public CityDTO getCity(Integer cityId) throws EOTException {

		City city=locationDao.getCityDetails(cityId);
		if(city==null){
			throw new EOTException(ErrorConstants.INVALID_CITY);
		}else{
			CityDTO cityDTO=new CityDTO();
			cityDTO.setCityId(city.getCityId());
			cityDTO.setCity(city.getCity());
			cityDTO.setCountryId(city.getCountry().getCountryId().toString());
			return cityDTO;
		}
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#updateCities(com.eot.banking.dto.CityDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateCities(CityDTO cityDTO) throws EOTException {
		City city= locationDao.getCity(cityDTO.getCityId(), cityDTO.getCity(), Integer.parseInt(cityDTO.getCountryId()));

		if(city!=null){
			throw new EOTException(ErrorConstants.CITY_NAME_EXIST);
		}

		city=locationDao.getCityDetails(cityDTO.getCityId());
		if(city==null){
			throw new EOTException(ErrorConstants.INVALID_CITY);
		}else{
			city.setCity(cityDTO.getCity());		
			locationDao.update(city);		
		}		
	}


	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#addQuarters(com.eot.banking.dto.QuarterDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addQuarters(QuarterDTO quarterDTO) throws EOTException{
		Quarter quarter=locationDao.getQuarter(quarterDTO.getQuarter(),quarterDTO.getCityId());
		if(quarter!=null){
			throw new EOTException(ErrorConstants.QUARTER_NAME_EXIST);
		}
		quarter=new Quarter();
		quarter.setQuarter(quarterDTO.getQuarter());
		quarter.setDescription(quarterDTO.getDescription());

		City city=new City();
		city.setCityId(quarterDTO.getCityId());
		quarter.setCity(city);

		locationDao.save(quarter);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getQuarterDetails(java.lang.Long)
	 */
	@Override
	public QuarterDTO getQuarterDetails(Long quarterId) throws EOTException {

		Quarter quarter=locationDao.getQuarterDetails(quarterId);
		if(quarter==null){
			throw new EOTException(ErrorConstants.INVALID_QUARTER);
		}else{
			QuarterDTO quarterDTO=new QuarterDTO();
			quarterDTO.setQuarterId(quarter.getQuarterId());
			quarterDTO.setQuarter(quarter.getQuarter());
			quarterDTO.setDescription(quarter.getDescription());
			quarterDTO.setCityId(quarter.getCity().getCityId());

			return quarterDTO;
		}
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#updateQuarters(com.eot.banking.dto.QuarterDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateQuarters(QuarterDTO quarterDTO) throws EOTException {
		Quarter quarter=locationDao.getQuarter(quarterDTO.getQuarterId(),quarterDTO.getQuarter(),quarterDTO.getCityId());
		if(quarter!=null){
			throw new EOTException(ErrorConstants.QUARTER_NAME_EXIST);
		}

		quarter=locationDao.getQuarterDetails(quarterDTO.getQuarterId());
		if(quarter==null){
			throw new EOTException(ErrorConstants.INVALID_QUARTER);
		}else{
			quarter.setQuarter(quarterDTO.getQuarter());
			quarter.setDescription(quarterDTO.getDescription());

			locationDao.update(quarter);
		}		

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#addCurrency(com.eot.banking.dto.CurrencyDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addCurrency(CurrencyDTO currencyDTO) throws EOTException {

		Currency currency=locationDao.getCurrencyByName(currencyDTO.getCurrencyName());

		if(currency!=null){
			throw new EOTException(ErrorConstants.CURRENCY_NAME_EXIST);
		}

		currency=locationDao.getCurrencyByCode(currencyDTO.getCurrencyCode());

		if(currency!=null){
			throw new EOTException(ErrorConstants.CURRENCY_CODE_EXIST);
		}

		currency=locationDao.getNumericCurrency(currencyDTO.getCurrencyCodeNumeric());

		if(currency!=null){
			throw new EOTException(ErrorConstants.CURRENCY_CODE_NUMERIC_EXIST);
		}

		currency=new Currency();
		currency.setCurrencyName(currencyDTO.getCurrencyName());
		currency.setCurrencyCode(currencyDTO.getCurrencyCode().toUpperCase());
		currency.setCurrencyCodeNumeric(currencyDTO.getCurrencyCodeNumeric());
		locationDao.save(currency);	
		
		//saveOrUpdateCurrencyInFI(currencyDTO, currency);


	}

	private int saveOrUpdateCurrencyInFI(CurrencyDTO currencyDTO, Currency currency) {
		//currencyDTO.setLanguage("English");
		currencyDTO.setWalletCurrencyId(currency.getCurrencyId());
		com.eot.banking.utils.JSONAdaptor adaptor = new com.eot.banking.utils.JSONAdaptor() ;
		String payload = adaptor.toJSON(currencyDTO);
		ByteArrayOutputStream response = new ByteArrayOutputStream();
		
		/*
		 * @bidyut
		 * as fi system is not applicable here
		 * commented the call to fi
		 */
		//response=RESTCall.sendPOSTRequest(payload, CurrencyURL);
		CurrencyDTO responseObject = adaptor.fromJSON(response.toString(), CurrencyDTO.class);
		
		return responseObject.getStatus();
	}
	
	private int saveOrUpdateCountryInFI(Country country) {
		
		CountryRequest request =new CountryRequest();
		
		request.setCountry(country.getCountry());
		request.setCountryCodeAlpha2(country.getCountryCodeAlpha2());
		request.setCountryCodeAlpha3(country.getCountryCodeAlpha3());
		request.setCountryCodeNumeric(country.getCountryCodeNumeric());
		request.setIsdcode(country.getIsdCode());
		request.setMobileNumberLength(country.getMobileNumberLength());
		request.setWalletCurrencyId(country.getCurrency().getCurrencyId());
		request.setWalletCountryId(country.getCountryId());
		//request.setLanguage("English");
		
		com.eot.banking.utils.JSONAdaptor adaptor = new com.eot.banking.utils.JSONAdaptor() ;
		String payload = adaptor.toJSON(request);
		ByteArrayOutputStream response = new ByteArrayOutputStream();
		response=RESTCall.sendPOSTRequest(payload, CountryURL);
		CountryRequest responseObject = adaptor.fromJSON(response.toString(), CountryRequest.class);
		
		return responseObject.getStatus();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#updateCurrency(com.eot.banking.dto.CurrencyDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateCurrency(CurrencyDTO currencyDTO) throws EOTException{

		Currency currency=locationDao.getCurrencyByName(currencyDTO.getCurrencyId(), currencyDTO.getCurrencyName());

		if(currency!=null){
			throw new EOTException(ErrorConstants.CURRENCY_NAME_EXIST);
		}

		currency=locationDao.getCurrencyDetailsByIdCode(currencyDTO.getCurrencyId(),currencyDTO.getCurrencyCode());

		if(currency!=null){
			throw new EOTException(ErrorConstants.CURRENCY_CODE_EXIST);
		}

		currency=locationDao.getNumericCurrency(currencyDTO.getCurrencyId(), currencyDTO.getCurrencyCodeNumeric());

		if(currency!=null){
			throw new EOTException(ErrorConstants.CURRENCY_CODE_NUMERIC_EXIST);
		}

		currency=locationDao.getCurrencyDetails(currencyDTO.getCurrencyId());
		currency.setCurrencyName(currencyDTO.getCurrencyName());
		currency.setCurrencyCode(currencyDTO.getCurrencyCode().toUpperCase());
		currency.setCurrencyCodeNumeric(currencyDTO.getCurrencyCodeNumeric());
		locationDao.update(currency);
		//saveOrUpdateCurrencyInFI(currencyDTO, currency);

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCurrencyDetails(java.lang.Integer)
	 */
	@Override
	public CurrencyDTO getCurrencyDetails(Integer currencyId)throws EOTException {
		Currency currency=locationDao.getCurrencyDetails(currencyId);
		if(currency==null){
			throw new EOTException(ErrorConstants.INVALID_CURRENCY);
		}else{
			CurrencyDTO currencyDTO=new CurrencyDTO();
			currencyDTO.setCurrencyId(currency.getCurrencyId());
			currencyDTO.setCurrencyName(currency.getCurrencyName());
			currencyDTO.setCurrencyCode(currency.getCurrencyCode());
			currencyDTO.setCurrencyCodeNumeric(currency.getCurrencyCodeNumeric());
			return currencyDTO;
		}
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getLanguageList()
	 */
	@Override
	public List<Language> getLanguageList() {

		return locationDao.getLanguageList();
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getAllCurrencyList(java.lang.Integer)
	 */
	@Override
	public Page getAllCurrencyList(Integer pageNumber) {

		return locationDao.getAllCurrencies(pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#addExchangeRate(com.eot.banking.dto.ExchangeRateDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addExchangeRate(ExchangeRateDTO exchangeRateDTO) throws EOTException {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		BankTellers teller = bankDao.getTellerByUsername(userName);

		ExchangeRate exchangeRate = locationDao.getExchangeRateFromCurrencyId(exchangeRateDTO.getCurrencyId(),teller.getBank().getBankId());

		if(exchangeRate == null){

			exchangeRate = new ExchangeRate();
			exchangeRate.setBuyingRate(exchangeRateDTO.getBuyingRate());
			exchangeRate.setSellingRate(exchangeRateDTO.getSellingRate());
			exchangeRate.setUpdatedDate(new Date());

			Currency currency = new Currency();
			currency.setCurrencyId(exchangeRateDTO.getCurrencyId());
			exchangeRate.setCurrency(currency);

			Bank bank = new Bank();
			bank.setBankId(teller.getBank().getBankId());
			exchangeRate.setBank(bank);

			locationDao.save(exchangeRate);
		}else{

			exchangeRate.setBuyingRate(exchangeRateDTO.getBuyingRate());
			exchangeRate.setSellingRate(exchangeRateDTO.getSellingRate());
			exchangeRate.setUpdatedDate(new Date());

			Currency currency = new Currency();
			currency.setCurrencyId(exchangeRateDTO.getCurrencyId());
			exchangeRate.setCurrency(currency);

			Bank bank = new Bank();
			bank.setBankId(teller.getBank().getBankId());
			exchangeRate.setBank(bank);

			locationDao.update(exchangeRate);
		}

	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getAllExchangeRate(int)
	 */
	@Override
	public Page getAllExchangeRate(int pageNumber) throws EOTException {
		return locationDao.getAllExchangeRate(pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getLocationTypes(java.lang.String)
	 */
	@Override
	public List<LocationType> getLocationTypes(String locale){
		return locationDao.getAllActiveLocationType(locale,EOTConstants.ACTIVE);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getCountryList(java.lang.String)
	 */
	@Override
	public List<Country> getCountryList(String locale) {
		return operatorDao.getCountries(locale);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#saveLocationDetails(com.eot.banking.dto.LocateUSDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void saveLocationDetails(LocateUSDTO locateUsdto) throws EOTException {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		BankTellers teller = bankDao.getTellerByUsername(userName);

		LocateUS locateUS = new LocateUS();

		locateUS.setAddress(locateUsdto.getAddress());
		locateUS.setStatus(locateUsdto.getStatus());
		Country country = locationDao.getCountry(locateUsdto.getCountryId());

		City city = new City();
		if(null != locateUsdto.getCityId() )
			city =locationDao.getCityDetails(locateUsdto.getCityId());
		
		Quarter quarter = new Quarter();
		if(null != locateUsdto.getQuarterId() )
			quarter =locationDao.getQuarterDetails(locateUsdto.getQuarterId().longValue());

		LocationType locationType = new LocationType();
		if(null != locateUsdto.getLocationType())
			locationType =locationDao.getLocationType(locateUsdto.getLocationType());
		
		Bank bank = new Bank();
		if(null != teller)
			bank.setBankId(teller.getBank().getBankId());

		locateUS.setCountry(country);
		locateUS.setCity(city);
		locateUS.setQuarter(quarter);
		locateUS.setLocationType(locationType);
		locateUS.setBank(bank);

		locationDao.save(locateUS);

	//	saveNetworkType(locateUsdto.getNetworkTypeId(), locateUS);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getAllLocationDetail(int)
	 */
	@Override
	public Page getAllLocationDetail(int pageNumber){
		return locationDao.getAllLocationDetail(pageNumber);
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#updateLocationDetails(com.eot.banking.dto.LocateUSDTO)
	 */
	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateLocationDetails(LocateUSDTO locateUsdto) throws EOTException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		BankTellers teller = bankDao.getTellerByUsername(userName);

		LocateUS locateUS = locationDao.getLocationDetailsFromLocatUsId(locateUsdto.getLocatUsId());

		locateUS.setAddress(locateUsdto.getAddress());
		locateUS.setStatus(locateUsdto.getStatus());

	/*	Country country = new Country();
		country.setCountryId(locateUsdto.getCountryId());

		City city = new City();
		city.setCityId(locateUsdto.getCityId());

		Quarter quarter = new Quarter();
		quarter.setQuarterId(locateUsdto.getQuarterId().longValue());

		LocationType locationType = new LocationType();
		locationType.setLocationTypeId(locateUsdto.getLocationType());

		Bank bank = new Bank();
		bank.setBankId(teller.getBank().getBankId());*/
		
		Country country = locationDao.getCountry(locateUsdto.getCountryId());
		City city =locationDao.getCityDetails(locateUsdto.getCityId());
		Quarter quarter =locationDao.getQuarterDetails(locateUsdto.getQuarterId().longValue());
		LocationType locationType =locationDao.getLocationType(locateUsdto.getLocationType());

		Bank bank = new Bank();
		bank.setBankId(teller.getBank().getBankId());

		locateUS.setCountry(country);
		locateUS.setCity(city);
		locateUS.setQuarter(quarter);
		locateUS.setLocationType(locationType);
		locateUS.setBank(bank);

	//	locationDao.deleteNetWorkType(locateUsdto.getLocatUsId());

		locationDao.update(locateUS);


/*		LocateUsNetworkTypeMapping locateUsNetworkTypeMapping =new LocateUsNetworkTypeMapping();
		locateUsNetworkTypeMapping.setLocateUs(locateUS);
		saveNetworkType(locateUsdto.getNetworkTypeId(), locateUS);*/
	}

	/* (non-Javadoc)
	 * @see com.eot.banking.service.LocationService#getLocationDetails(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LocateUSDTO getLocationDetails(Long locatUsId) throws EOTException {

		LocateUS locateUS = locationDao.getLocationDetailsFromLocatUsId(locatUsId);

		LocateUSDTO locateUSDTO = new LocateUSDTO();

		locateUSDTO.setAddress(locateUS.getAddress());
		locateUSDTO.setCityId(locateUS.getCity().getCityId());
		locateUSDTO.setCity(locateUS.getCity().getCityId());
		locateUSDTO.setCountryId(locateUS.getCountry().getCountryId());
		locateUSDTO.setLocationType(locateUS.getLocationType().getLocationTypeId());
		locateUSDTO.setLocatUsId(locateUS.getLocatUsId());
		locateUSDTO.setQuarterId(locateUS.getQuarter().getQuarterId().intValue());
		locateUSDTO.setQuarter(locateUS.getQuarter().getQuarterId());
		locateUSDTO.setStatus(locateUS.getStatus()); 

		Set<LocateUsNetworkTypeMapping> locateUsNetworkTypeMappings = locateUS.getLocateUsNetworkTypeMappings();
		if(locateUsNetworkTypeMappings.size()!=0){
			Integer[] networkTypeId=new Integer[locateUsNetworkTypeMappings.size()];
			int i=0;
			for (LocateUsNetworkTypeMapping locateUsNetworkTypeMapping : locateUsNetworkTypeMappings) {			
				networkTypeId[i]=locateUsNetworkTypeMapping.getNetworkType().getNetworkTypeId();		
				i++;
			}
			locateUSDTO.setNetworkTypeId(networkTypeId);
		}

		return locateUSDTO;
	}

	@Override
	public Page getProvenienceList(Integer countryId, int pageNumber) throws EOTException {
		return locationDao.getProvenienceList(countryId,pageNumber);
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addProveniences(ProvenienceDTO provenienceDTO) throws EOTException {
		Provenience provenience=locationDao.getProvenience(provenienceDTO.getProvenienceName(),Integer.parseInt( provenienceDTO.getCountryId()));
		if(provenience!=null){
			throw new EOTException(ErrorConstants.PROVENIENCE_NAME_EXIST);
		}
		provenience=new Provenience();
		/*provenience.setProvenienceId(provenienceDTO.getProvenienceId());*/
		provenience.setProvenienceName(provenienceDTO.getProvenienceName());
		Country country = new Country();
		country.setCountryId(Integer.parseInt(provenienceDTO.getCountryId()));
		provenience.setCountry(country);
		locationDao.save(provenience);		
	}


	@Override
	public ProvenienceDTO getProvenience(Integer provenienceId) throws EOTException {

		Provenience provenience=locationDao.getProvenienceDetails(provenienceId);
		if(provenience==null){
			throw new EOTException(ErrorConstants.INVALID_PROVENIENCE);
		}else{
			ProvenienceDTO provenienceDTO=new ProvenienceDTO();
			provenienceDTO.setProvenienceId(provenience.getProvenienceId());;
			provenienceDTO.setProvenienceName(provenience.getProvenienceName());
			provenienceDTO.setCountryId(provenience.getCountry().getCountryId().toString());
			return provenienceDTO;
		}
	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void updateProveniences(ProvenienceDTO provenienceDTO) throws EOTException {
		Provenience provenience= locationDao.getProvenience(provenienceDTO.getProvenienceName(), Integer.parseInt( provenienceDTO.getCountryId()));

		if(provenience!=null){
			throw new EOTException(ErrorConstants.PROVENIENCE_NAME_EXIST);
		}
		provenience=locationDao.getProvenienceDetails(provenienceDTO.getProvenienceId());;
		if(provenience==null){
			throw new EOTException(ErrorConstants.INVALID_PROVENIENCE);
		}else{
			provenience.setProvenienceName(provenienceDTO.getProvenienceName());
			locationDao.update(provenience);		
		}		

	}	

	@Override
	public List<LocateUSDTO> getNetworkTypes() {
		return locationDao.getNetworkTypes();
	}

	private void saveNetworkType(Integer[] networkTypeId, LocateUS locateUS){

		for(int i=0;i<networkTypeId.length;i++){

			NetworkType networkType = new NetworkType();
			networkType.setNetworkTypeId(networkTypeId[i]);

			LocateUsNetworkTypeMappingPK locateUsNetworkTypeMappingPK = new LocateUsNetworkTypeMappingPK();
			locateUsNetworkTypeMappingPK.setLocatUsId(locateUS.getLocatUsId());
			locateUsNetworkTypeMappingPK.setNetworkTypeId(networkType.getNetworkTypeId());
			LocateUsNetworkTypeMapping locateUsNetworkTypeMapping = new LocateUsNetworkTypeMapping();
			locateUsNetworkTypeMapping.setComp_id(locateUsNetworkTypeMappingPK);
			locateUsNetworkTypeMapping.setLocateUs(locateUS);
			locateUsNetworkTypeMapping.setNetworkType(networkType);
			locationDao.saveOrUpdate(locateUsNetworkTypeMapping);
		}
	}

	@Override
	public Page getAllConversionRate(int pageNumber) throws EOTException {
		return locationDao.getAllConversionRate(pageNumber);

	}

	@Override
	@Transactional( readOnly=false, propagation=Propagation.REQUIRES_NEW, rollbackFor=EOTException.class)
	public void addCurruncyConvertorRate(CurrencyConvertorDTO currencyConvertorDTO) throws EOTException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		BankTellers teller = bankDao.getTellerByUsername(userName);

		CurrencyConverter currencyConverter = locationDao.getCurrencyConvertorFromCurrencyIds(currencyConvertorDTO.getBaseCurrencyId(), currencyConvertorDTO.getCounterCurrencyId(),teller.getBank().getBankId() );

		if(currencyConverter == null){
			currencyConverter = new CurrencyConverter();
		}
		Currency baseCurrency = new Currency();
		baseCurrency.setCurrencyId(currencyConvertorDTO.getBaseCurrencyId());
		Currency counterCurrency = new Currency();
		counterCurrency.setCurrencyId(currencyConvertorDTO.getCounterCurrencyId());
		Bank bank = new Bank();
		bank.setBankId(teller.getBank().getBankId());
		currencyConverter.setBank(bank);
		currencyConverter.setConversionRate(currencyConvertorDTO.getConversionRate());
		currencyConverter.setCurrencyByBaseCurrencyId(baseCurrency);
		currencyConverter.setCurrencyByCounterCurrencyId(counterCurrency);
		currencyConverter.setUpdatedDate(new Date());
		locationDao.saveOrUpdate(currencyConverter);
	}

	@Override
	public Currency getCurrencyByCurrencyCode(String currencyCode) {
		return locationDao.getCurrencyByCode(currencyCode);
	}
	@Transactional(readOnly=false)
	@Override
	public void addHelpDesk(HelpDeskDTO helpDeskDTO) throws EOTException{
		
		HelpDesk helpDesk = null;
		if(null != helpDeskDTO.getMobileNumber() && !helpDeskDTO.getMobileNumber().equals("")) {
			 helpDesk = locationDao.getHelpDeskByContactNumber(helpDeskDTO.getMobileNumber());
			if(helpDesk!=null){
				throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
			}
		}
		if(null != helpDeskDTO.getEmailId() && !helpDeskDTO.getEmailId().equals("")) {
			helpDesk = locationDao.getHelpDeskByEmailId(helpDeskDTO.getEmailId());
			if(helpDesk!=null){
				throw new EOTException(ErrorConstants.EMAIL_ALREADY_EXISTS);
			}
		}
			helpDesk = new HelpDesk();
			helpDesk.setMobileNumber(helpDeskDTO.getMobileNumber());
			helpDesk.setEmailId(helpDeskDTO.getEmailId());
			helpDesk.setStatus(helpDeskDTO.getStatus());
			locationDao.save(helpDesk);
	}

	@Override
	public Page getAllHelpDeskList(Integer pageNumber) {
		return locationDao.getAllHelpDeskList(pageNumber);
	}
	
	@Override
	public HelpDeskDTO getHelpDeskDetails(Integer helpDeskId)throws Exception {
		HelpDesk helpDesk=locationDao.getHelpDeskDetails(helpDeskId);
		if(helpDesk==null){
			throw new Exception();
		}else{
			HelpDeskDTO helpDeskDTO=new HelpDeskDTO();
			helpDeskDTO.setHelpDeskId(helpDesk.getId());
			helpDeskDTO.setMobileNumber(helpDesk.getMobileNumber());
			helpDeskDTO.setEmailId(helpDesk.getEmailId());
			helpDeskDTO.setStatus(helpDesk.getStatus());
			return helpDeskDTO;
		}
	}

	@Transactional(readOnly=false)
	@Override
	public void updateHelpDesk(HelpDeskDTO helpDeskDTO) throws EOTException, Exception {
		HelpDesk helpDesk=locationDao.getHelpDeskDetails(helpDeskDTO.getHelpDeskId());
		if(helpDesk==null){
			throw new Exception();
		}
		
		HelpDesk helpDesk1 = null;
		
		if(null != helpDeskDTO.getMobileNumber() && !helpDeskDTO.getMobileNumber().equals("")) {
			if( ! helpDesk.getMobileNumber().equals(helpDeskDTO.getMobileNumber())){
				helpDesk1 = locationDao.getHelpDeskByContactNumber(helpDeskDTO.getMobileNumber());
				if( helpDesk1 != null ){
					throw new EOTException(ErrorConstants.MOBILE_NUMBER_REGISTERED_ALREADY);
				}
			}
		}
		if(null != helpDeskDTO.getEmailId() && !helpDeskDTO.getEmailId().equals("")) {
			if( ! helpDesk.getEmailId().equals(helpDeskDTO.getEmailId())){
				helpDesk1 = locationDao.getHelpDeskByEmailId(helpDeskDTO.getEmailId());
				if( helpDesk1 != null ){
					throw new EOTException(ErrorConstants.EMAIL_ALREADY_EXISTS);
				}
			}		
		}
		helpDesk.setEmailId(helpDeskDTO.getEmailId());
		helpDesk.setMobileNumber(helpDeskDTO.getMobileNumber());
		helpDesk.setStatus(helpDeskDTO.getStatus());
		locationDao.update(helpDesk);
		
	}

}

