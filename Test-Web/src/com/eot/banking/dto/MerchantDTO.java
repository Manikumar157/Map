/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: MerchantDTO.java
*
* Date Author Changes
* 22 May, 2016 Swadhin Created
*/
package com.eot.banking.dto;

import java.io.Serializable;

import com.eot.entity.Country;

/**
 * The Class MerchantDTO.
 */
public class MerchantDTO implements Serializable {

    /** The merchant id. */
    private Integer merchantId;

    /** The merchant name. */
    private String merchantName;

    /** The address. */
    private String address;

    /** The primary contact name. */
    private String primaryContactName;

    /** The primary contact address. */
    private String primaryContactAddress;

    /** The primary contact phone. */
    private String primaryContactPhone;

    /** The primary contact mobile. */
    private String primaryContactMobile;

    /** The primarye mail address. */
    private String primaryeMailAddress;

    /** The alternate contact name. */
    private String alternateContactName;

    /** The alternate contact address. */
    private String alternateContactAddress;

    /** The alternate contact phone. */
    private String alternateContactPhone;

    /** The alternate contact mobile. */
    private String alternateContactMobile;

    /** The alternatee mail address. */
    private String alternateeMailAddress;

    /** The active. */
    private int active;

    /** The city id. */
    private Integer cityId;

    /** The country id. */
    private Integer countryId;

    /** The quarter id. */
    private Long quarterId;
    
    /** The merchant country. */
    private Country merchantCountry;
    
    /** The city name. */
    private String cityName;
    
    /** The quarter name. */
    private String quarterName;

	/**
	 * Gets the merchant id.
	 *
	 * @return the merchant id
	 */
	public Integer getMerchantId() {
		return merchantId;
	}

	/**
	 * Sets the merchant id.
	 *
	 * @param merchantId the new merchant id
	 */
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	/**
	 * Gets the merchant name.
	 *
	 * @return the merchant name
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * Sets the merchant name.
	 *
	 * @param merchantName the new merchant name
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the primary contact name.
	 *
	 * @return the primary contact name
	 */
	public String getPrimaryContactName() {
		return primaryContactName;
	}

	/**
	 * Sets the primary contact name.
	 *
	 * @param primaryContactName the new primary contact name
	 */
	public void setPrimaryContactName(String primaryContactName) {
		this.primaryContactName = primaryContactName;
	}

	/**
	 * Gets the primary contact address.
	 *
	 * @return the primary contact address
	 */
	public String getPrimaryContactAddress() {
		return primaryContactAddress;
	}

	/**
	 * Sets the primary contact address.
	 *
	 * @param primaryContactAddress the new primary contact address
	 */
	public void setPrimaryContactAddress(String primaryContactAddress) {
		this.primaryContactAddress = primaryContactAddress;
	}

	/**
	 * Gets the primary contact phone.
	 *
	 * @return the primary contact phone
	 */
	public String getPrimaryContactPhone() {
		return primaryContactPhone;
	}

	/**
	 * Sets the primary contact phone.
	 *
	 * @param primaryContactPhone the new primary contact phone
	 */
	public void setPrimaryContactPhone(String primaryContactPhone) {
		this.primaryContactPhone = primaryContactPhone;
	}

	/**
	 * Gets the primary contact mobile.
	 *
	 * @return the primary contact mobile
	 */
	public String getPrimaryContactMobile() {
		return primaryContactMobile;
	}

	/**
	 * Sets the primary contact mobile.
	 *
	 * @param primaryContactMobile the new primary contact mobile
	 */
	public void setPrimaryContactMobile(String primaryContactMobile) {
		this.primaryContactMobile = primaryContactMobile;
	}

	/**
	 * Gets the primarye mail address.
	 *
	 * @return the primarye mail address
	 */
	public String getPrimaryeMailAddress() {
		return primaryeMailAddress;
	}

	/**
	 * Sets the primarye mail address.
	 *
	 * @param primaryeMailAddress the new primarye mail address
	 */
	public void setPrimaryeMailAddress(String primaryeMailAddress) {
		this.primaryeMailAddress = primaryeMailAddress;
	}

	/**
	 * Gets the alternate contact name.
	 *
	 * @return the alternate contact name
	 */
	public String getAlternateContactName() {
		return alternateContactName;
	}

	/**
	 * Sets the alternate contact name.
	 *
	 * @param alternateContactName the new alternate contact name
	 */
	public void setAlternateContactName(String alternateContactName) {
		this.alternateContactName = alternateContactName;
	}

	/**
	 * Gets the alternate contact address.
	 *
	 * @return the alternate contact address
	 */
	public String getAlternateContactAddress() {
		return alternateContactAddress;
	}

	/**
	 * Sets the alternate contact address.
	 *
	 * @param alternateContactAddress the new alternate contact address
	 */
	public void setAlternateContactAddress(String alternateContactAddress) {
		this.alternateContactAddress = alternateContactAddress;
	}

	/**
	 * Gets the alternate contact phone.
	 *
	 * @return the alternate contact phone
	 */
	public String getAlternateContactPhone() {
		return alternateContactPhone;
	}

	/**
	 * Sets the alternate contact phone.
	 *
	 * @param alternateContactPhone the new alternate contact phone
	 */
	public void setAlternateContactPhone(String alternateContactPhone) {
		this.alternateContactPhone = alternateContactPhone;
	}

	/**
	 * Gets the alternate contact mobile.
	 *
	 * @return the alternate contact mobile
	 */
	public String getAlternateContactMobile() {
		return alternateContactMobile;
	}

	/**
	 * Sets the alternate contact mobile.
	 *
	 * @param alternateContactMobile the new alternate contact mobile
	 */
	public void setAlternateContactMobile(String alternateContactMobile) {
		this.alternateContactMobile = alternateContactMobile;
	}

	/**
	 * Gets the alternatee mail address.
	 *
	 * @return the alternatee mail address
	 */
	public String getAlternateeMailAddress() {
		return alternateeMailAddress;
	}

	/**
	 * Sets the alternatee mail address.
	 *
	 * @param alternateeMailAddress the new alternatee mail address
	 */
	public void setAlternateeMailAddress(String alternateeMailAddress) {
		this.alternateeMailAddress = alternateeMailAddress;
	}

	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * Gets the city id.
	 *
	 * @return the city id
	 */
	public Integer getCityId() {
		return cityId;
	}

	/**
	 * Sets the city id.
	 *
	 * @param cityId the new city id
	 */
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	/**
	 * Gets the country id.
	 *
	 * @return the country id
	 */
	public Integer getCountryId() {
		return countryId;
	}

	/**
	 * Sets the country id.
	 *
	 * @param countryId the new country id
	 */
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	/**
	 * Gets the quarter id.
	 *
	 * @return the quarter id
	 */
	public Long getQuarterId() {
		return quarterId;
	}

	/**
	 * Sets the quarter id.
	 *
	 * @param quarterId the new quarter id
	 */
	public void setQuarterId(Long quarterId) {
		this.quarterId = quarterId;
	}

	/**
	 * Gets the merchant country.
	 *
	 * @return the merchant country
	 */
	public Country getMerchantCountry() {
		return merchantCountry;
	}

	/**
	 * Sets the merchant country.
	 *
	 * @param merchantCountry the new merchant country
	 */
	public void setMerchantCountry(Country merchantCountry) {
		this.merchantCountry = merchantCountry;
	}

	/**
	 * Gets the city name.
	 *
	 * @return the city name
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * Sets the city name.
	 *
	 * @param cityName the new city name
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * Gets the quarter name.
	 *
	 * @return the quarter name
	 */
	public String getQuarterName() {
		return quarterName;
	}

	/**
	 * Sets the quarter name.
	 *
	 * @param quarterName the new quarter name
	 */
	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

}
