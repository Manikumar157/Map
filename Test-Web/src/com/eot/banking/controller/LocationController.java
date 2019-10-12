/* Copyright � EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: LocationController.java
 *
 * Date Author Changes
 * 20 May, 2016 Swadhin Created
 */
package com.eot.banking.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.eot.banking.common.AppConfigurations;
import com.eot.banking.common.EOTConstants;
import com.eot.banking.dto.CityDTO;
import com.eot.banking.dto.CountryDTO;
import com.eot.banking.dto.CurrencyConvertorDTO;
import com.eot.banking.dto.CurrencyDTO;
import com.eot.banking.dto.CustomerDTO;
import com.eot.banking.dto.ExchangeRateDTO;
import com.eot.banking.dto.HelpDeskDTO;
import com.eot.banking.dto.LocateUSDTO;
import com.eot.banking.dto.ProvenienceDTO;
import com.eot.banking.dto.QuarterDTO;
import com.eot.banking.dto.TransactionParamDTO;
import com.eot.banking.dto.TransactionReceiptDTO;
import com.eot.banking.dto.TxnStatementDTO;
import com.eot.banking.exceptions.EOTException;
import com.eot.banking.exceptions.ErrorConstants;
import com.eot.banking.service.CustomerService;
import com.eot.banking.service.LocationService;
import com.eot.banking.utils.ExcelWrapper;
import com.eot.banking.utils.Page;
import com.eot.banking.utils.PdfFileGenerator;
import com.eot.dtos.basic.Transaction;
import com.eot.entity.Country;
import com.eot.entity.WebUser;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationController.
 */
@Controller
public class LocationController extends PageViewController  {

	/** The location service. */
	@Autowired
	private LocationService locationService;
	
	/** The customer service. */
	@Autowired
	private CustomerService customerService;

	/** The locale resolver. */
	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	private ExcelWrapper wrapper;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	PdfFileGenerator pdfGenerator;
	
	@Autowired
	private AppConfigurations appConfigurations ;

	/**
	 * Show countries.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/showCountries.htm")
	public String showCountries(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{			   
			model.put("countryDTO",new CountryDTO());
			model.put("message", request.getParameter("message"));
			model.put("currencyList",locationService.getCurrencyList());
			model.put("languageList",locationService.getLanguageList());
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
			Page page = locationService.getCountriesList(pageNumber) ;
			page.requestPage = "showCountries.htm";
			model.put("page",page);	
			model.put("lang",localeResolver.resolveLocale(request).toString());			   
		}catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"Country");
		}	
		return "country";
	}

	/**
	 * Save countries.
	 *
	 * @param countryDTO the country dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @return the string
	 * @throws EOTException the eOT exception
	 */
	@RequestMapping("/saveCountries.htm")
	public String saveCountries(@Valid CountryDTO countryDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request) throws EOTException{

		try{
			countryDTO.setLanguage(null != localeResolver.resolveLocale(request) ? localeResolver.resolveLocale(request).toString(): "en_US");
			if(result.hasErrors()){
				model.put("countryDTO",countryDTO);
			}else if(countryDTO.getCountryId()== null){	
				locationService.addCountries(countryDTO);
				model.put("countryDTO",new CountryDTO());
				model.put("message","ADD_COUNTRY_SUCCESS");
				//return "redirect:/showCountries.htm?csrfToken=" + request.getSession().getAttribute("csrfToken")+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
				return "country";	
			}else{
				locationService.updateCountries(countryDTO);	
				model.put("countryDTO",new CountryDTO());
				model.put("message","EDIT_COUNTRY_SUCCESS");
				//return "redirect:/showCountries.htm?csrfToken=" + request.getSession().getAttribute("csrfToken")+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
				return "country";	
			}
		}catch(EOTException e){
			model.put("message",e.getErrorCode());				
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			try{
				model.put("currencyList",locationService.getCurrencyList());
				model.put("languageList",locationService.getLanguageList());
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
				Page page = locationService.getCountriesList(pageNumber) ;
				page.requestPage = "showCountries.htm";
				model.put("page",page);	
				model.put("lang",localeResolver.resolveLocale(request).toString());	

			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "country";		
	}

	/**
	 * Edits the countries.
	 *
	 * @param countryId the country id
	 * @param model the model
	 * @param countryDTO the country dto
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/editCountry.htm")
	public String editCountries(Map<String,Object> model,CountryDTO countryDTO,HttpServletRequest request,HttpServletResponse response){
		try{   
			//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
			model.put("countryDTO",locationService.getCountry(countryDTO.getCountryId())); 
			//@End
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);        
		}finally{
			model.put("currencyList",locationService.getCurrencyList());
			model.put("languageList",locationService.getLanguageList());
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
			Page page = locationService.getCountriesList(pageNumber) ;
			page.requestPage = "showCountries.htm";
			model.put("page",page);	
			model.put("lang",localeResolver.resolveLocale(request).toString());	
			pageLogger(request,response,"EditCountry");
		}
		return "country";

	}

	/**
	 * View cities.
	 *
	 * @param countryId the country id
	 * @param cityDTO the city dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/viewCities.htm")
	public String viewCities(CityDTO cityDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try{			
			
			//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting		
			model.put("cityDTO",cityDTO);
			model.put("message", request.getParameter("message"));
			model.put("country",locationService.getCountry(Integer.parseInt(cityDTO.getCountryId())));
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = locationService.getCityList(Integer.parseInt(cityDTO.getCountryId()),pageNumber);
			//@End
			page.requestPage = "viewCities.htm";
			model.put("page",page);	
			model.put("lang", localeResolver.resolveLocale(request).toString());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"City");
		}	
		return "city";
	}

	/**
	 * Save cities.
	 *
	 * @param cityDTO the city dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @return the string
	 * @throws EOTException the eOT exception
	 */
	@RequestMapping("/saveCities.htm")
	public String saveCities(@Valid CityDTO cityDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException{
		try{	
			if(result.hasErrors()){
				model.put("cityDTO",cityDTO);
			}		
			else if(cityDTO.getCityId()==null){			
				locationService.addCities(cityDTO);
				model.put("cityDTO",new CityDTO());
				model.put("message","ADD_CITY_SUCCESS");
				//return "redirect:/viewCities.htm?countryId="+cityDTO.getCountryId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}else{
				locationService.updateCities(cityDTO);
				model.put("cityDTO",new CityDTO());
				model.put("message","EDIT_CITY_SUCCESS");
				//return "redirect:/viewCities.htm?countryId="+cityDTO.getCountryId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		}catch(EOTException e){
			model.put("message",e.getErrorCode());	
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);	
		}finally{
			try {
				String countryId = EOTConstants.DEFAULT_COUNTRY.toString();
				if(null != cityDTO.getCountryId() && !cityDTO.getCountryId().equals(""))
					countryId = cityDTO.getCountryId();
				model.put("country",locationService.getCountry(Integer.parseInt(countryId)));
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = locationService.getCityList(Integer.parseInt(countryId), pageNumber);
				page.requestPage = "viewCities.htm";
				model.put("page",page);	
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}		
		return "city";
	}

	/**
	 * Edits the cities.
	 *
	 * @param cityId the city id
	 * @param countryId the country id
	 * @param model the model
	 * @param cityDTO the city dto
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	// @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/editCity.htm")
	public String editCities(Map<String,Object> model,CityDTO cityDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("cityDTO",locationService.getCity(cityDTO.getCityId()));				
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			model.put("cityList",locationService.getCityList(Integer.parseInt(cityDTO.getCountryId()),pageNumber));
			model.put("country",locationService.getCountry(Integer.parseInt(cityDTO.getCountryId())));

		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = locationService.getCityList(Integer.parseInt(cityDTO.getCountryId()), pageNumber);
				page.requestPage = "viewCities.htm";
				model.put("page",page);
				model.put("country",locationService.getCountry(Integer.parseInt(cityDTO.getCountryId())));
				//@End
				model.put("lang", localeResolver.resolveLocale(request).toString());
			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
				pageLogger(request,response,"EditCity");
			}	
		}		
		return "city";
	}

	/**
	 * View quarters.
	 *
	 * @param cityId the city id
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	// @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	@RequestMapping("/viewQuarters.htm")
	public String viewQuarters(CityDTO cityDTO,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{

			QuarterDTO quarterDTO=new QuarterDTO();
			quarterDTO.setCityId(cityDTO.getCityId());
			model.put("quarterDTO",quarterDTO);
			model.put("message", request.getParameter("message"));
			model.put("city",locationService.getCity(cityDTO.getCityId()));

			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = locationService.getQuarterList(cityDTO.getCityId(), pageNumber);
			//@End
			page.requestPage = "viewQuarters.htm";
			model.put("page",page);	

		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"Quarter");
		}			
		return "quarter";
	}

	/**
	 * Adds the quarters.
	 *
	 * @param quarterDTO the quarter dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/saveQuarters.htm")
	public String addQuarters(@Valid QuarterDTO quarterDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			QuarterDTO dto  = null;
			if(result.hasErrors()){
				model.put("quarterDTO",quarterDTO);
			}else if(quarterDTO.getQuarterId()==null){
				locationService.addQuarters(quarterDTO);
				dto = new QuarterDTO();
				dto.setCityId(quarterDTO.getCityId());
				model.put("quarterDTO",dto);
				model.put("message","ADD_QUARTER_SUCCESS");
				//return "redirect:/viewQuarters.htm?cityId="+quarterDTO.getCityId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}else{
				locationService.updateQuarters(quarterDTO);
				model.put("quarterDTO",new QuarterDTO());
				model.put("message","EDIT_QUARTER_SUCCESS");
				//return "redirect:/viewQuarters.htm?cityId="+quarterDTO.getCityId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}	
		}catch(EOTException e){
			model.put("message",e.getErrorCode());	
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				model.put("city",locationService.getCity(quarterDTO.getCityId()));
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = locationService.getQuarterList(quarterDTO.getCityId(), pageNumber);
				page.requestPage = "viewQuarters.htm";
				model.put("page",page);					
			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}
		return "quarter";
	}

	/**
	 * Edits the quarters.
	 *
	 * @param quarterId the quarter id
	 * @param cityId the city id
	 * @param model the model
	 * @param quarterDTO the quarter dto
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	@RequestMapping("/editQuarter.htm")	
	public String editQuarters(Map<String,Object> model,QuarterDTO quarterDTO,HttpServletRequest request
			,HttpServletResponse response){				
		try{	
			model.put("quarterDTO",locationService.getQuarterDetails(quarterDTO.getQuarterId()));
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			model.put("quarterList",locationService.getQuarterList(quarterDTO.getCityId(),pageNumber));	
			model.put("city",locationService.getCity(quarterDTO.getCityId()));
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = locationService.getQuarterList(quarterDTO.getCityId(), pageNumber);
				//@End
				page.requestPage = "viewQuarters.htm";
				model.put("page",page);	
			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
			finally{
				pageLogger(request,response,"EditQuarter");
			}	
		}		
		return "quarter";		
	}

	/**
	 * Show currencies.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/showCurrencies.htm")
	public String showCurrencies(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllCurrencyList(pageNumber);
			model.put("currencyDTO",new CurrencyDTO());	
			model.put("message",request.getParameter("message"));
			model.put("page",page);	
		}catch(Exception ex){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"Currency");
		}	
		return "currency";	
	}

	/**
	 * Save currency.
	 *
	 * @param currencyDTO the currency dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping("/saveCurrency.htm")
	public String saveCurrency(@Valid CurrencyDTO currencyDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request){

		try{
			currencyDTO.setLanguage(null != localeResolver.resolveLocale(request) ? localeResolver.resolveLocale(request).toString(): "en_US");
			if(result.hasErrors()){
				model.put("currencyDTO",currencyDTO);
			}else if(currencyDTO.getCurrencyId()== null){		
				locationService.addCurrency(currencyDTO);	
				model.put("currencyDTO",new CurrencyDTO());
				model.put("message","ADD_CURRENCY_SUCCESS");
			//	return "redirect:/showCurrencies.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			return "currency";
			}else{
				locationService.updateCurrency(currencyDTO);				
				model.put("message","EDIT_CURRENCY_SUCCESS");
				model.put("currencyDTO",new CurrencyDTO());
				//return "redirect:/showCurrencies.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
				return "currency";
			}
			
		}catch(EOTException e){
			model.put("message",e.getErrorCode());				
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);			
		}finally{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllCurrencyList(pageNumber);
			page.requestPage = "showCurrencies.htm";
			model.put("page",page);	
		}
		return "currency";		
	}

	/**
	 * Edits the currency.
	 *
	 * @param currencyId the currency id
	 * @param model the model
	 * @param currencyDTO the currency dto
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/editCurrency.htm")
	public String editCurrency(Map<String,Object> model,CurrencyDTO currencyDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
			model.put("currencyDTO", locationService.getCurrencyDetails(currencyDTO.getCurrencyId()));	
			//@End
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
				Page page = locationService.getAllCurrencyList(pageNumber);
				page.requestPage = "showCurrencies.htm";
				model.put("page",page);	
			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);	
			}
			pageLogger(request,response,"EditCurrency");
		}		
		return "currency";
	}

	/**
	 * Show exchange rate.
	 *
	 * @param exchangeRateDTO the exchange rate dto
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping("/showExchangeRateForm.htm")
	public String showExchangeRate( ExchangeRateDTO exchangeRateDTO, Map<String,Object> model, HttpServletRequest request ){
		try{
			model.put("currencyList",locationService.getCurrencyList());
			model.put("exchangeRateDTO",new ExchangeRateDTO());	
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllExchangeRate(pageNumber);
			page.requestPage = "showExchangeRateForm.htm";
			model.put("message",request.getParameter("message"));
			model.put("page",page);
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);	
		}
		return "exchangeRate";
	}

	/**
	 * Adds the exchange rate.
	 *
	 * @param exchangeRateDTO the exchange rate dto
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping("/addExchangeRate.htm")
	public String addExchangeRate( ExchangeRateDTO exchangeRateDTO, Map<String,Object> model, HttpServletRequest request ){
		model.put("exchangeRateDTO",exchangeRateDTO);	
		try {
			locationService.addExchangeRate(exchangeRateDTO);
			model.put("message","ADD_EXCHANGE_RATE");
		} catch (EOTException e) {
			model.put("currencyList",locationService.getCurrencyList());
			model.put("message",e.getErrorCode());
			return "exchangeRate";
		}catch(Exception e){
			model.put("currencyList",locationService.getCurrencyList());
			model.put("message",ErrorConstants.SERVICE_ERROR);
			return "exchangeRate";
		}
		return "redirect:/showExchangeRateForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
	}

	/**
	 * Show locate us.
	 *
	 * @param locateUsdto the locate usdto
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping("/showLocateUsForm.htm")
	public String showLocateUs(LocateUSDTO locateUsdto, Map<String,Object> model, HttpServletRequest request ){
		try{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllLocationDetail(pageNumber);
			page.requestPage = "showLocateUsForm.htm";
			model.put("locateUsdto", new LocateUSDTO());
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locationTypeList", locationService.getLocationTypes(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("countryList",locationService.getCountryList(localeResolver.resolveLocale(request).toString()));
			Integer countryId = locateUsdto.getCountryId() != null ? locateUsdto.getCountryId() : EOTConstants.DEFAULT_COUNTRY ;
			model.put("City",countryId != null?customerService.getCityList(countryId):null);
	//		model.put("networkType", locationService.getNetworkTypes());
			model.put("message",request.getParameter("message"));
			model.put("page",page);
		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}
		return "locateUS";
	}

	/**
	 * Adds the locate us.
	 *
	 * @param locateUsdto the locate usdto
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping("/addLocateUS.htm")
	public String addLocateUS(LocateUSDTO locateUsdto, Map<String,Object> model, HttpServletRequest request ){
		try{
			String quarterId = request.getParameter("quaterCode");
			String cityId = request.getParameter("cityCode");
			locateUsdto.setCity(Integer.parseInt(cityId));
			locateUsdto.setCityId(Integer.parseInt(cityId));
			locateUsdto.setQuarter(Long.parseLong(quarterId));
			locateUsdto.setQuarterId(Integer.parseInt(quarterId));

			if(locateUsdto.getLocatUsId() == null){
				locationService.saveLocationDetails(locateUsdto);
				model.put("message","ADD_LOCATION_DETAILS");
				return "locateUS";
				//return "redirect:/showLocateUsForm.htm?&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}else{
				locationService.updateLocationDetails(locateUsdto);
				model.put("message","UPDATE_LOCATION_DETAILS");
				return "locateUS";
				//return "redirect:/showLocateUsForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		}catch(EOTException e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllLocationDetail(pageNumber);
			page.requestPage = "showLocateUsForm.htm";
			model.put("locateUsdto", new LocateUSDTO());
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locationTypeList", locationService.getLocationTypes(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("countryList",locationService.getCountryList(localeResolver.resolveLocale(request).toString()));
			Integer countryId = locateUsdto.getCountryId() != null ? locateUsdto.getCountryId() : EOTConstants.DEFAULT_COUNTRY ;
			model.put("City",countryId != null?customerService.getCityList(countryId):null);
		//	model.put("networkType", locationService.getNetworkTypes());
			
			model.put("page",page);
			}catch (Exception e) {
				e.printStackTrace();
			}
			}
		return "locateUS";

	}

	/**
	 * Edits the locate us.
	 *
	 * @param locatUsId the locat us id
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/editLocateUS.htm")
	public String editLocateUS(@RequestParam Long locatUsId,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
		try{
			LocateUSDTO locateUSDTO = locationService.getLocationDetails(locatUsId);
			model.put("locateUsdto",locateUSDTO);
			model.put("City",customerService.getCityList(locateUSDTO.getCountryId()));
			model.put("quarter",customerService.getQuarterList(locateUSDTO.getCityId()));
		} catch (EOTException e) {
			model.put("message",e.getErrorCode());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);        
		}finally{
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllLocationDetail(pageNumber);
			page.requestPage = "showLocateUsForm.htm";
			model.put("language",localeResolver.resolveLocale(request));
			model.put("locationTypeList", locationService.getLocationTypes(localeResolver.resolveLocale(request).toString().substring(0, 2)));
			model.put("countryList",locationService.getCountryList(localeResolver.resolveLocale(request).toString()));
	//		model.put("networkType", locationService.getNetworkTypes());
			model.put("page",page);
		}
		return "locateUS";

	}
	
	/**
	 * View provenience.
	 *
	 * @param countryId the country id
	 * @param provenienceDTO the provenience dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/viewProvenience.htm")
	public String viewProvenience(@RequestParam Integer countryId,ProvenienceDTO provenienceDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){		
		try{			
			provenienceDTO = new ProvenienceDTO();	   
			provenienceDTO.setCountryId(countryId.toString());		
			model.put("provenienceDTO",provenienceDTO);
			model.put("message", request.getParameter("message"));
			model.put("country",locationService.getCountry(countryId));
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			Page page = locationService.getProvenienceList(countryId,pageNumber);
			page.requestPage = "viewProvenience.htm";
			model.put("page",page);	
			model.put("lang", localeResolver.resolveLocale(request).toString());
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			pageLogger(request,response,"Provenience");
		}	
		return "provenience";
	}
	
	/**
	 * Save provenience.
	 *
	 * @param provenienceDTO the provenience dto
	 * @param result the result
	 * @param model the model
	 * @param request the request
	 * @return the string
	 * @throws EOTException the EOT exception
	 */
	@RequestMapping("/saveProvenience.htm")
	public String saveProvenience(@Valid ProvenienceDTO provenienceDTO,BindingResult result,Map<String,Object> model,HttpServletRequest request)throws EOTException{
		try{	
			if(result.hasErrors()){
				model.put("provenienceDTO",provenienceDTO);
			}		
			else if(provenienceDTO.getProvenienceId()==null){			
				locationService.addProveniences(provenienceDTO);
				model.put("provenienceDTO",new ProvenienceDTO());
				model.put("message","ADD_PROVENIENCE_SUCCESS");
				return "redirect:/viewProvenience.htm?countryId="+provenienceDTO.getCountryId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}else{
				locationService.updateProveniences(provenienceDTO);
				model.put("provenienceDTO",new ProvenienceDTO());
				model.put("message","EDIT_PROVENIENCE_SUCCESS");
				return "redirect:/viewProvenience.htm?countryId="+provenienceDTO.getCountryId()+"&csrfToken=" + request.getSession().getAttribute("csrfToken");
			}
		}catch(EOTException e){
			model.put("message",e.getErrorCode());	
		}catch(Exception e){
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);	
		}finally{
			try {
				model.put("country",locationService.getCountry(Integer.parseInt(provenienceDTO.getCountryId())));
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = locationService.getProvenienceList(Integer.parseInt(provenienceDTO.getCountryId()), pageNumber);
				page.requestPage = "viewProvenience.htm";
				model.put("page",page);	
			} catch (Exception e) {
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}
		}		
		return "provenience";
	}
	
	/**
	 * Edits the provenience.
	 *
	 * @param provenienceId the provenience id
	 * @param countryId the country id
	 * @param model the model
	 * @param provenienceDTO the provenience dto
	 * @param request the request
	 * @param response the response
	 * @return the string
	 */
	@RequestMapping("/editProvenience.htm")
	public String editProvenience(@RequestParam Integer provenienceId,@RequestParam Integer countryId,Map<String,Object> model,ProvenienceDTO provenienceDTO,HttpServletRequest request,HttpServletResponse response){
		try{
			model.put("provenienceDTO",locationService.getProvenience(provenienceId));				
			int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
			model.put("provenienceList",locationService.getProvenienceList(countryId, pageNumber));
			model.put("country",locationService.getCountry(countryId));

		}catch(Exception e){
			model.put("message",ErrorConstants.SERVICE_ERROR);
		}finally{
			try{
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));
				Page page = locationService.getProvenienceList(countryId, pageNumber);
				page.requestPage = "viewProvenience.htm";
				model.put("page",page);
				model.put("country",locationService.getCountry(countryId));
				model.put("lang", localeResolver.resolveLocale(request).toString());
			}catch(Exception e){
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
				pageLogger(request,response,"EditProvenience");
			}	
		}		
		return "provenience";
	}
	
	@RequestMapping("/showCurrencyConverterForm.htm")
	public String showCurrencyConverter( CurrencyConvertorDTO currencyConvertorDTO, Map<String,Object> model, HttpServletRequest request ){
		try{
			model.put("currencyList",locationService.getCurrencyList());
			model.put("currencyConvertorDTO",new CurrencyConvertorDTO());	
			int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
			Page page = locationService.getAllConversionRate(pageNumber);
			page.requestPage = "showCurrencyConverterForm.htm";
			model.put("message",request.getParameter("message"));
			model.put("page",page);
		}catch (Exception e) {
			e.printStackTrace();
			model.put("message",ErrorConstants.SERVICE_ERROR);	
		}
		return "showCurrencyConverter";
	}
	
	
		@RequestMapping("/addCurrencyConverter.htm")
		public String addCurrencyConverter( CurrencyConvertorDTO currencyConvertorDTO, Map<String,Object> model, HttpServletRequest request ){
			model.put("currencyConvertorDTO",currencyConvertorDTO);	
			try {
				locationService.addCurruncyConvertorRate(currencyConvertorDTO);
				model.put("message","ADD_CURRENCY_CONVERTOR_RATE");
			} catch (EOTException e) {
				model.put("currencyList",locationService.getCurrencyList());
				model.put("message",e.getErrorCode());
				return "showCurrencyConverter";
			}catch(Exception e){
				model.put("currencyList",locationService.getCurrencyList());
				model.put("message",ErrorConstants.SERVICE_ERROR);
				e.printStackTrace();
				return "showCurrencyConverter";
			}
			return "redirect:/showCurrencyConverterForm.htm?csrfToken=" + request.getSession().getAttribute("csrfToken");
		}
		
		
		@RequestMapping("/exportToXLSForLocations.htm")
		public void exportToXLSLocation(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {

			String viewPage = null;
			List<Country> list = null;
			HSSFWorkbook wb = null;

			try {
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
				Page page = locationService.getCountriesList(pageNumber) ;
				page.requestPage = "showCountries.htm";
				model.put("page",page);	
				model.put("lang",localeResolver.resolveLocale(request).toString());
				list = page.getResults();

				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				WebUser webUser=customerService.getUser(userName);

					wb = wrapper.createSpreadSheetForLocations(list, localeResolver.resolveLocale(request), messageSource, webUser, EOTConstants.LOCATION_DETAILS_PAGE_HEADER);
					String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename="+ EOTConstants.LOCATION_REPORT_NAME
						+ date + "_" + System.currentTimeMillis() + "_report.xls");
				OutputStream os = response.getOutputStream();

				wb.write(os);
				os.flush();
				os.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		@RequestMapping("/exportToPDFLocations.htm")
		public void exportToPDFLocation(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response,HttpSession session) {
			
			String viewPage = null;
			List<Country> list = null;
			
			try {
				int pageNumber = request.getParameter("pageNumber") == null ? 1 : new Integer(request.getParameter("pageNumber"));			
				Page page = locationService.getCountriesList(pageNumber) ;
				page.requestPage = "showCountries.htm";
				model.put("page",page);	
				model.put("lang",localeResolver.resolveLocale(request).toString());
				list = page.getResults();
				String userName = SecurityContextHolder.getContext().getAuthentication().getName();
				WebUser webUser=customerService.getUser(userName);
				String dt1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				model.put("userName", webUser.getUserName());
				model.put("date", dt1);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			generatePDFReport(EOTConstants.JASPER_LOCATION_JRXML_NAME, EOTConstants.LOCATION_REPORT_NAME, list, model, request, response);
		}
		
		/**
		 * Show Helpdesk Information.
		 *
		 * @param model the model
		 * @param request the request
		 * @param response the response
		 * @return the string
		 */
		@RequestMapping("/showHelpDeskConfig.htm")
		public String showHelpDesk(Map<String,Object> model,HttpServletRequest request,HttpServletResponse response){
			try{
				int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
				Page page = locationService.getAllHelpDeskList(pageNumber);									
				model.put("page",page);	
				model.put("count",page.results.size());
			}catch(Exception ex){
				ex.printStackTrace();
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{		
					try {
						model.put("mobilenumberLength",customerService.getMobileNumLength(EOTConstants.DEFAULT_COUNTRY));
					} catch (EOTException e) {
						model.put("message",ErrorConstants.SERVICE_ERROR);
					}
				model.put("helpDeskDTO",new HelpDeskDTO());
				pageLogger(request,response,"Currency");
			}	
			return "helpDesk";	
		}

		/**
		 * Save HelpDesk.
		 *
		 * @param HelpDeskDTO the HelpDesk dto
		 * @param result the result
		 * @param model the model
		 * @param request the request
		 * @return the string
		 */
		@RequestMapping("/saveHelpDesk.htm")
		public String saveHelpDesk(HelpDeskDTO helpDeskDTO,Map<String,Object> model,HttpServletRequest request){

			try{
				if(helpDeskDTO.getHelpDeskId()==null){
					locationService.addHelpDesk(helpDeskDTO);
					model.put("message","ADD_HELP_DESK_SUCCESS");
					model.put("helpDeskDTO",new HelpDeskDTO());					
				}else{
					locationService.updateHelpDesk(helpDeskDTO);
					model.put("helpDeskDTO",new HelpDeskDTO());	
					model.put("message","EDIT_HELP_DESK_SUCCESS");
					}				
			}catch (EOTException e) {
				model.put("helpDeskDTO",helpDeskDTO);
				model.put("message",e.getErrorCode());
			}catch(Exception e){
				model.put("helpDeskDTO",helpDeskDTO);
				e.printStackTrace();
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
				try {
					int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
					Page page = locationService.getAllHelpDeskList(pageNumber);
					page.requestPage = "showHelpDeskConfig.htm";
					model.put("page",page);
					model.put("count",page.results.size());
					model.put("mobilenumberLength",customerService.getMobileNumLength(EOTConstants.DEFAULT_COUNTRY));
				} catch (EOTException e) {
					model.put("message",ErrorConstants.SERVICE_ERROR);
				}				
			}
			return "helpDesk";		
		}
		/**
		 * Edits the HelpDesk Details.
		 *
		 * @param helpDeskId the helpDesk id
		 * @param model the model
		 * @param helpDeskDTO the HelpDesk dto
		 * @param request the request
		 * @param response the response
		 * @return the string
		 */
		@RequestMapping("/editHelpDesk.htm")
		public String editHelpDesk(Map<String,Object> model,HelpDeskDTO helpDeskDTO,HttpServletRequest request,HttpServletResponse response){
			
			try{
				model.put("helpDeskDTO", locationService.getHelpDeskDetails(helpDeskDTO.getHelpDeskId()));	
			}catch(Exception e){
				e.printStackTrace();
				model.put("message",ErrorConstants.SERVICE_ERROR);
			}finally{
				try{
					int pageNumber = request.getParameter("pageNumber") != null ? new Integer(request.getParameter("pageNumber")) : 0 ;
					Page page = locationService.getAllHelpDeskList(pageNumber);
					page.requestPage = "showHelpDeskConfig.htm";
					model.put("page",page);
					model.put("count",page.results.size());
					model.put("mobilenumberLength",customerService.getMobileNumLength(EOTConstants.DEFAULT_COUNTRY));
				}catch(Exception e){
					e.printStackTrace();
					model.put("message",ErrorConstants.SERVICE_ERROR);	
				}
				pageLogger(request,response,"EditHelpDesk");
			}		
			return "helpDesk";
		}


}
