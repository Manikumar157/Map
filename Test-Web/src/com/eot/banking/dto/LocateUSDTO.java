/* Copyright EasOfTech 2015. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: LocateUSDTO.java
*
* Date Author Changes
* 20 May, 2016 Swadhin Created
*/
package com.eot.banking.dto;

// TODO: Auto-generated Javadoc
/**
 * The Class LocateUSDTO.
 */
public class LocateUSDTO  {
	
	/** The locat us id. */
	private Long locatUsId;
	
	/** The location type. */
	private Integer locationType;
	
	/** The city id. */
	private Integer cityId;
	
	/** The quarter id. */
	private Integer quarterId;
	
	/** The country id. */
	private Integer countryId;
	
	/** The city. */
	private Integer city;
	
	/** The quarter. */
	private Long quarter;
	
	/** The status. */
	private Integer status;
	
	/** The address. */
	private String address;
	
	/** The network type id. */
	private Integer[] networkTypeId;
	
	/**
	 * Gets the location type.
	 *
	 * @return the location type
	 */
	public Integer getLocationType() {
		return locationType;
	}
	
	/**
	 * Sets the location type.
	 *
	 * @param locationType the new location type
	 */
	public void setLocationType(Integer locationType) {
		this.locationType = locationType;
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
	 * Gets the quarter id.
	 *
	 * @return the quarter id
	 */
	public Integer getQuarterId() {
		return quarterId;
	}
	
	/**
	 * Sets the quarter id.
	 *
	 * @param quarterId the new quarter id
	 */
	public void setQuarterId(Integer quarterId) {
		this.quarterId = quarterId;
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
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Integer status) {
		this.status = status;
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
	 * Gets the locat us id.
	 *
	 * @return the locat us id
	 */
	public Long getLocatUsId() {
		return locatUsId;
	}

	/**
	 * Sets the locat us id.
	 *
	 * @param locatUsId the new locat us id
	 */
	public void setLocatUsId(Long locatUsId) {
		this.locatUsId = locatUsId;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public Integer getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(Integer city) {
		this.city = city;
	}

	/**
	 * Gets the quarter.
	 *
	 * @return the quarter
	 */
	public Long getQuarter() {
		return quarter;
	}

	/**
	 * Sets the quarter.
	 *
	 * @param quarter the new quarter
	 */
	public void setQuarter(Long quarter) {
		this.quarter = quarter;
	}

	/**
	 * Gets the network type id.
	 *
	 * @return the network type id
	 */
	public Integer[] getNetworkTypeId() {
		return networkTypeId;
	}

	/**
	 * Sets the network type id.
	 *
	 * @param networkTypeId the new network type id
	 */
	public void setNetworkTypeId(Integer[] networkTypeId) {
		this.networkTypeId = networkTypeId;
	}

}
