package com.eot.banking.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class QuarterDTO {
	
	private Long quarterId;
	
	@NotEmpty(message="NotEmpty.quarterDTO.quarter")
	private String quarter;
	
	@NotEmpty(message="NotEmpty.quarterDTO.description")
	private String description;
	 
	private Integer cityId;

	/**
	 * @return the quarterId
	 */
	public Long getQuarterId() {
		return quarterId;
	}

	/**
	 * @return the quarter
	 */
	public String getQuarter() {
		return quarter;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the cityId
	 */
	public Integer getCityId() {
		return cityId;
	}

	/**
	 * @param quarterId the quarterId to set
	 */
	public void setQuarterId(Long quarterId) {
		this.quarterId = quarterId;
	}

	/**
	 * @param quarter the quarter to set
	 */
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

}
