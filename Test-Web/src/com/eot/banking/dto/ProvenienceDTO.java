package com.eot.banking.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

// TODO: Auto-generated Javadoc
/**
 * The Class ProvenienceDTO.
 */
public class ProvenienceDTO implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The provenienc id. */
	private Integer provenienceId;
	
	/** The provenience. */
	private String provenienceName;
	
	/** The country id. */
	private String countryId;
	
	/**
	 * Gets the provenienc id.
	 *
	 * @return the provenienc id
	 */
	
	/**
	 * Gets the provenience.
	 *
	 * @return the provenience
	 */
	public String getProvenienceName() {
		return provenienceName;
	}
	
	/**
	 * Sets the provenience.
	 *
	 * @param provenience the new provenience
	 */
	public void setProvenienceName(String provenienceName) {
		this.provenienceName = provenienceName;
	}
	
	/**
	 * Gets the country id.
	 *
	 * @return the country id
	 */
	public String getCountryId() {
		return countryId;
	}
	
	/**
	 * Sets the country id.
	 *
	 * @param countryId the new country id
	 */
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public Integer getProvenienceId() {
		return provenienceId;
	}

	public void setProvenienceId(Integer provenienceId) {
		this.provenienceId = provenienceId;
	}

}
