/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: LocationDao.java
*
* Date Author Changes
* 16 May, 2016 Swadhin Created
*/
package com.eot.banking.dao;

import java.util.List;
import java.util.Set;

import com.eot.banking.dto.LocateUSDTO;
import com.eot.banking.utils.Page;
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

// TODO: Auto-generated Javadoc
/**
 * The Interface LocationDao.
 */
public interface LocationDao extends BaseDao{
	
	/**
	 * Gets the all countries.
	 *
	 * @return the all countries
	 */
	List<Country> getAllCountries();
	
	/**
	 * Gets the countries.
	 *
	 * @param pageNumber the page number
	 * @return the countries
	 */
	Page getCountries(Integer pageNumber);
	
	/**
	 * Gets the all cities.
	 *
	 * @param countryId the country id
	 * @return the all cities
	 */
	List<City> getAllCities(Integer countryId);
	
	/**
	 * Gets the cities.
	 *
	 * @param countryId the country id
	 * @param pageNumber the page number
	 * @return the cities
	 */
	Page getCities(Integer countryId,Integer pageNumber);
	
	/**
	 * Gets the all quarters.
	 *
	 * @param cityId the city id
	 * @return the all quarters
	 */
	List<Quarter> getAllQuarters(Integer cityId);
	
	/**
	 * Gets the quarters.
	 *
	 * @param cityId the city id
	 * @param pageNumber the page number
	 * @return the quarters
	 */
	Page getQuarters(Integer cityId,Integer pageNumber);
	
	/**
	 * Gets the time zones.
	 *
	 * @return the time zones
	 */
	List<TimeZone> getTimeZones();

	/**
	 * Gets the questions.
	 *
	 * @param locale the locale
	 * @return the questions
	 */
	List<SecurityQuestion> getQuestions(String locale);
	
	/**
	 * Gets the city.
	 *
	 * @param cityName the city name
	 * @param countryID the country id
	 * @return the city
	 */
	City getCity(String cityName, Integer countryID);
	
	/**
	 * Gets the city.
	 *
	 * @param cityId the city id
	 * @param cityName the city name
	 * @param countryID the country id
	 * @return the city
	 */
	City getCity(Integer cityId,String cityName, Integer countryID);
	
	/**
	 * Gets the quarter.
	 *
	 * @param quarter the quarter
	 * @param cityId the city id
	 * @return the quarter
	 */
	Quarter getQuarter(String quarter, Integer cityId);
	
	/**
	 * Gets the quarter.
	 *
	 * @param quarterId the quarter id
	 * @param quarter the quarter
	 * @param cityId the city id
	 * @return the quarter
	 */
	Quarter getQuarter(Long quarterId,String quarter, Integer cityId);
	
	/**
	 * Gets the country.
	 *
	 * @param countryID the country id
	 * @return the country
	 */
	Country getCountry(Integer countryID);
	
	/**
	 * Gets the currencies.
	 *
	 * @return the currencies
	 */
	List<Currency> getCurrencies();

	/**
	 * Gets the country details.
	 *
	 * @param countryId the country id
	 * @return the country details
	 */
	Country getCountryDetails(Integer countryId);

	/**
	 * Gets the city details.
	 *
	 * @param cityId the city id
	 * @return the city details
	 */
	City getCityDetails(Integer cityId);

	/**
	 * Gets the quarter details.
	 *
	 * @param quarterId the quarter id
	 * @return the quarter details
	 */
	Quarter getQuarterDetails(Long quarterId);

	/**
	 * Gets the country by name.
	 *
	 * @param countryName the country name
	 * @return the country by name
	 */
	CountryNames getCountryByName(String countryName);

	/**
	 * Gets the city by name.
	 *
	 * @param city the city
	 * @return the city by name
	 */
	City getCityByName(String city);

	/**
	 * Gets the quarter by name.
	 *
	 * @param quarter the quarter
	 * @return the quarter by name
	 */
	Quarter getQuarterByName(String quarter);
	
	/**
	 * Gets the currency by name.
	 *
	 * @param currencyId the currency id
	 * @param currencyName the currency name
	 * @return the currency by name
	 */
	Currency getCurrencyByName(Integer currencyId,String currencyName);
	
	/**
	 * Gets the currency by name.
	 *
	 * @param currencyName the currency name
	 * @return the currency by name
	 */
	Currency getCurrencyByName(String currencyName);

	/**
	 * Gets the currency details.
	 *
	 * @param currencyId the currency id
	 * @return the currency details
	 */
	Currency getCurrencyDetails(Integer currencyId);

	/**
	 * Gets the currency by code.
	 *
	 * @param currencyCode the currency code
	 * @return the currency by code
	 */
	Currency getCurrencyByCode(String currencyCode);
	
	/**
	 * Gets the numeric currency.
	 *
	 * @param currencyCodeNumeric the currency code numeric
	 * @return the numeric currency
	 */
	Currency getNumericCurrency(Integer currencyCodeNumeric);
	
	/**
	 * Gets the numeric currency.
	 *
	 * @param currencyId the currency id
	 * @param currencyCodeNumeric the currency code numeric
	 * @return the numeric currency
	 */
	Currency getNumericCurrency(Integer currencyId,Integer currencyCodeNumeric);

	/**
	 * Gets the language list.
	 *
	 * @return the language list
	 */
	List<Language> getLanguageList();

	/**
	 * Gets the country names.
	 *
	 * @param countryId the country id
	 * @return the country names
	 */
	List<CountryNames> getCountryNames(Integer countryId);
	
	/**
	 * Gets the all currencies.
	 *
	 * @param pageNumber the page number
	 * @return the all currencies
	 */
	Page getAllCurrencies(Integer pageNumber);

	/**
	 * Gets the currency details by id code.
	 *
	 * @param currencyId the currency id
	 * @param currencyCode the currency code
	 * @return the currency details by id code
	 */
	Currency getCurrencyDetailsByIdCode(Integer currencyId, String currencyCode);

	/**
	 * Gets the country by code.
	 *
	 * @param countryCodeAlpha2 the country code alpha2
	 * @return the country by code
	 */
	Country getCountryByCode(String countryCodeAlpha2);

	/**
	 * Gets the country by id code.
	 *
	 * @param countryId the country id
	 * @param countryCodeAlpha2 the country code alpha2
	 * @return the country by id code
	 */
	Country getCountryByIdCode(Integer countryId, String countryCodeAlpha2);

	/**
	 * Gets the country by name id.
	 *
	 * @param countryId the country id
	 * @param countryName the country name
	 * @return the country by name id
	 */
	CountryNames getCountryByNameId(Integer countryId, String countryName);

	/**
	 * Gets the country by code3.
	 *
	 * @param countryCodeAlpha3 the country code alpha3
	 * @return the country by code3
	 */
	Country getCountryByCode3(String countryCodeAlpha3);

	/**
	 * Gets the country by code numeric.
	 *
	 * @param countryCodeNumeric the country code numeric
	 * @return the country by code numeric
	 */
	Country getCountryByCodeNumeric(Integer countryCodeNumeric);

	/**
	 * Gets the country by isd code.
	 *
	 * @param isdCode the isd code
	 * @return the country by isd code
	 */
	Country getCountryByIsdCode(Integer isdCode);

	/**
	 * Gets the country by id code3.
	 *
	 * @param countryId the country id
	 * @param countryCodeAlpha3 the country code alpha3
	 * @return the country by id code3
	 */
	Country getCountryByIdCode3(Integer countryId, String countryCodeAlpha3);

	/**
	 * Gets the country by id code numeric.
	 *
	 * @param countryId the country id
	 * @param countryCodeNumeric the country code numeric
	 * @return the country by id code numeric
	 */
	Country getCountryByIdCodeNumeric(Integer countryId,Integer countryCodeNumeric);

	/**
	 * Gets the country by id isd code.
	 *
	 * @param countryId the country id
	 * @param isdCode the isd code
	 * @return the country by id isd code
	 */
	Country getCountryByIdIsdCode(Integer countryId, Integer isdCode);

	/**
	 * Gets the exchange rate from base and counter currency.
	 *
	 * @param currencyId the currency id
	 * @param bankId the bank id
	 * @return the exchange rate from base and counter currency
	 */
	ExchangeRate getExchangeRateFromCurrencyId(Integer currencyId, Integer bankId);

	/**
	 * Gets the all exchange rate.
	 *
	 * @param pageNumber the page number
	 * @return the all exchange rate
	 */
	Page getAllExchangeRate(int pageNumber);

	/**
	 * Gets the all active location type.
	 *
	 * @param locale the locale
	 * @param status the status
	 * @return the all active location type
	 */
	List<LocationType> getAllActiveLocationType(String locale, Integer status);

	/**
	 * Gets the all location detail.
	 *
	 * @param pageNumber the page number
	 * @return the all location detail
	 */
	Page getAllLocationDetail(int pageNumber);

	/**
	 * Gets the location details from locat us id.
	 *
	 * @param locatUsId the locat us id
	 * @return the location details from locat us id
	 */
	LocateUS getLocationDetailsFromLocatUsId(Long locatUsId);

	/**
	 * Gets the provenience list.
	 *
	 * @param countryId the country id
	 * @param pageNumber the page number
	 * @return the provenience list
	 */
	Page getProvenienceList(Integer countryId, int pageNumber);

	/**
	 * Gets the provenience.
	 *
	 * @param provenience the provenience
	 * @param countryID the country id
	 * @return the provenience
	 */
	Provenience getProvenience(String provenience,Integer countryID);

	/**
	 * Gets the provenience details.
	 *
	 * @param provenienceId the provenience id
	 * @return the provenience details
	 */
	Provenience getProvenienceDetails(Integer provenienceId);

	/**
	 * Gets the network types.
	 *
	 * @return the network types
	 */
	List<LocateUSDTO> getNetworkTypes();

	/**
	 * Delete net work type.
	 *
	 * @param networkTypeId the network type id
	 */
	public  void deleteNetWorkType(Long networkTypeId);

	/**
	 * Gets the all conversion rate.
	 *
	 * @param pageNumber the page number
	 * @return the all conversion rate
	 */
	Page getAllConversionRate(int pageNumber);
	
	/**
	 * Gets the currency convertor from currency ids.
	 *
	 * @param baseCurrencyId the base currency id
	 * @param counterCurrencyId the counter currency id
	 * @param bankId the bank id
	 * @return the currency convertor from currency ids
	 */
	public CurrencyConverter getCurrencyConvertorFromCurrencyIds(Integer baseCurrencyId,Integer counterCurrencyId ,Integer bankId);

	Page getAllHelpDeskList(Integer pageNumber);

	HelpDesk getHelpDeskDetails(Integer helpDeskId);

	HelpDesk getHelpDeskByContactNumber(String mobileNumber);

	HelpDesk getHelpDeskByEmailId(String emailId);

	LocationType getLocationType(Integer locationTypeId);
	
}
