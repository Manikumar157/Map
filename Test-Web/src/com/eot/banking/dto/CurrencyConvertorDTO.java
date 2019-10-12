package com.eot.banking.dto;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrencyConvertorDTO.
 */
public class CurrencyConvertorDTO implements Serializable {
	
	/** The base currency id. */
	private Integer baseCurrencyId;
	
	/** The counter currency id. */
	private Integer counterCurrencyId;
	
	/** The conversion rate. */
	private Double conversionRate;
	/**
	 * Gets the base currency id.
	 *
	 * @return the base currency id
	 */

	public Integer getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(Integer baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	public Integer getCounterCurrencyId() {
		return counterCurrencyId;
	}

	public void setCounterCurrencyId(Integer counterCurrencyId) {
		this.counterCurrencyId = counterCurrencyId;
	}

	public Double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate) {
		this.conversionRate = conversionRate;
	}
}