/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: LocationService.java
*
* Date Author Changes
* 16 May, 2016 Swadhin Created
*/
package com.eot.banking.service;

import java.util.List;

import com.eot.banking.dto.CityDTO;
import com.eot.banking.dto.CountryDTO;
import com.eot.banking.dto.CurrencyConvertorDTO;
import com.eot.banking.dto.CurrencyDTO;
import com.eot.banking.dto.ExchangeRateDTO;
import com.eot.banking.dto.HelpDeskDTO;
import com.eot.banking.dto.LocateUSDTO;
import com.eot.banking.dto.ProvenienceDTO;
import com.eot.banking.dto.QuarterDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.utils.Page;
import com.eot.entity.Country;
import com.eot.entity.Currency;
import com.eot.entity.CurrencyConverter;
import com.eot.entity.ExchangeRate;
import com.eot.entity.Language;
import com.eot.entity.LocationType;

// TODO: Auto-generated Javadoc
/**
 * The Interface LocationService.
 */
public interface LocationService {	
	
	/**
	 * Gets the all currency list.
	 *
	 * @param pageNumber the page number
	 * @return the all currency list
	 */
	public Page getAllCurrencyList(Integer pageNumber);
	
	/**
	 * Gets the currency list.
	 *
	 * @return the currency list
	 */
	public List<Currency> getCurrencyList();
	
	/**
	 * Gets the countries list.
	 *
	 * @param pageNumber the page number
	 * @return the countries list
	 */
	public Page getCountriesList(Integer pageNumber);
	
	/**
	 * Gets the country.
	 *
	 * @param countryId the country id
	 * @return the country
	 * @throws EOTException the eOT exception
	 */
	public CountryDTO getCountry(Integer countryId) throws EOTException;
	
	/**
	 * Adds the countries.
	 *
	 * @param countryDTO the country dto
	 * @throws EOTException the eOT exception
	 */
	public void addCountries(CountryDTO countryDTO) throws EOTException;
	
	/**
	 * Update countries.
	 *
	 * @param countryDTO the country dto
	 * @throws EOTException the eOT exception
	 */
	public void updateCountries(CountryDTO countryDTO)throws EOTException;
	
	/**
	 * Gets the city list.
	 *
	 * @param countryId the country id
	 * @param pageNumber the page number
	 * @return the city list
	 * @throws EOTException the eOT exception
	 */
	public Page getCityList(Integer countryId,Integer pageNumber)  throws EOTException;
	
	/**
	 * Adds the cities.
	 *
	 * @param cityDTO the city dto
	 * @throws EOTException the eOT exception
	 */
	public void addCities(CityDTO cityDTO)throws EOTException;
	
	/**
	 * Gets the city.
	 *
	 * @param cityId the city id
	 * @return the city
	 * @throws EOTException the eOT exception
	 */
	public CityDTO getCity(Integer cityId) throws EOTException;
	
	/**
	 * Update cities.
	 *
	 * @param cityDTO the city dto
	 * @throws EOTException the eOT exception
	 */
	public void updateCities(CityDTO cityDTO) throws EOTException;
	
	/**
	 * Gets the quarter list.
	 *
	 * @param cityId the city id
	 * @param pageNumber the page number
	 * @return the quarter list
	 */
	public Page getQuarterList(Integer cityId,Integer pageNumber);
	
	/**
	 * Adds the quarters.
	 *
	 * @param quarterDTO the quarter dto
	 * @throws EOTException the eOT exception
	 */
	public void addQuarters(QuarterDTO quarterDTO)throws EOTException;
	
	/**
	 * Gets the quarter details.
	 *
	 * @param quarterId the quarter id
	 * @return the quarter details
	 * @throws EOTException the eOT exception
	 */
	public QuarterDTO getQuarterDetails(Long quarterId) throws EOTException;
	
	/**
	 * Update quarters.
	 *
	 * @param quarterDTO the quarter dto
	 * @throws EOTException the eOT exception
	 */
	public void updateQuarters(QuarterDTO quarterDTO) throws EOTException;
	
	/**
	 * Adds the currency.
	 *
	 * @param currencyDTO the currency dto
	 * @throws EOTException the eOT exception
	 */
	public void addCurrency(CurrencyDTO currencyDTO) throws EOTException;
	
	/**
	 * Update currency.
	 *
	 * @param currencyDTO the currency dto
	 * @throws EOTException the eOT exception
	 */
	public void updateCurrency(CurrencyDTO currencyDTO) throws EOTException;
	
	/**
	 * Gets the currency details.
	 *
	 * @param currencyId the currency id
	 * @return the currency details
	 * @throws EOTException the eOT exception
	 */
	public CurrencyDTO getCurrencyDetails(Integer currencyId) throws EOTException;
	
	/**
	 * Gets the language list.
	 *
	 * @return the language list
	 */
	public List<Language> getLanguageList();
	
	/**
	 * Adds the exchange rate.
	 *
	 * @param exchangeRateDTO the exchange rate dto
	 * @throws EOTException the eOT exception
	 */
	public void addExchangeRate(ExchangeRateDTO exchangeRateDTO)throws EOTException;

	/**
	 * Gets the all exchange rate.
	 *
	 * @param pageNumber the page number
	 * @return the all exchange rate
	 * @throws EOTException the eOT exception
	 */
	public Page getAllExchangeRate(int pageNumber)throws EOTException;

	/**
	 * Gets the location types.
	 *
	 * @param locale the locale
	 * @return the location types
	 */
	public List<LocationType> getLocationTypes(String locale);

	/**
	 * Gets the country list.
	 *
	 * @param locale the locale
	 * @return the country list
	 */
	public List<Country> getCountryList(String locale);

	/**
	 * Save location details.
	 *
	 * @param locateUsdto the locate usdto
	 * @throws EOTException the eOT exception
	 */
	public void saveLocationDetails(LocateUSDTO locateUsdto)throws EOTException;

	/**
	 * Gets the all location detail.
	 *
	 * @param pageNumber the page number
	 * @return the all location detail
	 */
	public Page getAllLocationDetail(int pageNumber);

	/**
	 * Update location details.
	 *
	 * @param locateUsdto the locate usdto
	 * @throws EOTException the eOT exception
	 */
	public void updateLocationDetails(LocateUSDTO locateUsdto)throws EOTException;

	/**
	 * Gets the location details.
	 *
	 * @param locatUsId the locat us id
	 * @return the location details
	 * @throws EOTException the eOT exception
	 */
	public LocateUSDTO getLocationDetails(Long locatUsId)throws EOTException;

	/**
	 * Gets the provenience list.
	 *
	 * @param countryId the country id
	 * @param pageNumber the page number
	 * @return the provenience list
	 * @throws EOTException the EOT exception
	 */
	public Page getProvenienceList(Integer countryId, int pageNumber)throws EOTException;

	/**
	 * Adds the proveniences.
	 *
	 * @param provenienceDTO the provenience DTO
	 * @throws EOTException the EOT exception
	 */
	public void addProveniences(ProvenienceDTO provenienceDTO)throws EOTException;

	/**
	 * Update proveniences.
	 *
	 * @param provenienceDTO the provenience DTO
	 * @throws EOTException the EOT exception
	 */
	public void updateProveniences(ProvenienceDTO provenienceDTO)throws EOTException;

	/**
	 * Gets the provenience.
	 *
	 * @param provenienceId the provenience id
	 * @return the provenience
	 * @throws EOTException the EOT exception
	 */
	ProvenienceDTO getProvenience(Integer provenienceId) throws EOTException;


	/**
	 * Gets the network types.
	 *
	 * @return the network types
	 */
	public List<LocateUSDTO> getNetworkTypes();
	
	/**
	 * Gets the all conversion rate.
	 *
	 * @param pageNumber the page number
	 * @return the all conversion rate
	 * @throws EOTException the EOT exception
	 */
	public Page getAllConversionRate(int pageNumber)throws EOTException;
	
	
	/**
	 * Adds the curruncy convertor rate.
	 *
	 * @param currencyConvertorDTO the currency convertor DTO
	 * @throws EOTException the EOT exception
	 */
	public void addCurruncyConvertorRate(CurrencyConvertorDTO currencyConvertorDTO)throws EOTException;
	
	public Currency getCurrencyByCurrencyCode(String currencyCode);

	public void addHelpDesk(HelpDeskDTO helpDeskDTO) throws EOTException;
	
	public Page getAllHelpDeskList(Integer pageNumber);

	HelpDeskDTO getHelpDeskDetails(Integer helpDeskId) throws Exception;

	public void updateHelpDesk(HelpDeskDTO helpDeskDTO) throws EOTException, Exception ;


}
