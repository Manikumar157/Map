
package com.eot.banking.dto;

import java.io.Serializable;


/**
 * The Class ExchangeRateDTO.
 */
public class ExchangeRateDTO implements Serializable {
	
	/** The base currency id. */
	private Integer currencyId;
	
	/** The counter currency. */
	private Double buyingRate;
	
	/** The conversion rate. */
	private Double sellingRate;

	/**
	 * Gets the currency id.
	 *
	 * @return the currency id
	 */
	public Integer getCurrencyId() {
		return currencyId;
	}

	/**
	 * Sets the currency id.
	 *
	 * @param currencyId the new currency id
	 */
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * Gets the buying rate.
	 *
	 * @return the buying rate
	 */
	public Double getBuyingRate() {
		return buyingRate;
	}

	/**
	 * Sets the buying rate.
	 *
	 * @param buyingRate the new buying rate
	 */
	public void setBuyingRate(Double buyingRate) {
		this.buyingRate = buyingRate;
	}

	/**
	 * Gets the selling rate.
	 *
	 * @return the selling rate
	 */
	public Double getSellingRate() {
		return sellingRate;
	}

	/**
	 * Sets the selling rate.
	 *
	 * @param sellingRate the new selling rate
	 */
	public void setSellingRate(Double sellingRate) {
		this.sellingRate = sellingRate;
	}

}