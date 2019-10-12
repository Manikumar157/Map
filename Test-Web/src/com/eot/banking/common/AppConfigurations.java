/* Copyright EasOfTech 2016. All rights reserved.
*
* This software is the confidential and proprietary information
* of EasOfTech. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EasOfTech.
*
* Id: AppConfigurations.java
*
* Date Author Changes
* 12 Jul, 2016 Swadhin Created
*/
package com.eot.banking.common;

import java.util.Map;

/**
 * The Class AppConfigurations.
 */
public class AppConfigurations {
	
	/** The mobile txn type map. */
	private Map<String, String> mobileTxnTypeMap ;
	
	/** The app download URL. */
	private String appDownloadURL ;
	
	/** The results per page. */
	private Integer resultsPerPage ;
	
	/** The jasper path. */
	private String jasperPath ;
	
	/** The reportsDownloadPath. */
	private String reportsDownloadPath;
	
	/** The mrh URL. */
	private String mrhURL ;
	
	/** The live profile. */
	private String liveProfile;
	
	/** The white list. */
	private String whiteList;
	
	/** The settlement file location. */
	private String settlementFileLocation;
	
	/** The ussd password. */
	private String ussdPassword;
	
	private String qrCodePath;
	
	private String bulkPaymentReportDownloadPath;
	
	/**
	 * Gets the live profile.
	 *
	 * @return the live profile
	 */
	public String getLiveProfile() {
		return liveProfile;
	}

	/**
	 * Sets the live profile.
	 *
	 * @param liveProfile the new live profile
	 */
	public void setLiveProfile(String liveProfile) {
		this.liveProfile = liveProfile;
	}

	/**
	 * Gets the white list.
	 *
	 * @return the white list
	 */
	public String getWhiteList() {
		return whiteList;
	}

	/**
	 * Sets the white list.
	 *
	 * @param whiteList the new white list
	 */
	public void setWhiteList(String whiteList) {
		this.whiteList = whiteList;
	}

	/**
	 * Gets the mobile txn type map.
	 *
	 * @return the mobile txn type map
	 */
	public Map<String, String> getMobileTxnTypeMap() {
		return mobileTxnTypeMap;
	}

	/**
	 * Sets the mobile txn type map.
	 *
	 * @param mobileTxnTypeMap the mobile txn type map
	 */
	public void setMobileTxnTypeMap(Map<String, String> mobileTxnTypeMap) {
		this.mobileTxnTypeMap = mobileTxnTypeMap;
	}

	/**
	 * Gets the app download URL.
	 *
	 * @return the app download URL
	 */
	public String getAppDownloadURL() {
		return appDownloadURL;
	}

	/**
	 * Sets the app download URL.
	 *
	 * @param appDownloadURL the new app download URL
	 */
	public void setAppDownloadURL(String appDownloadURL) {
		this.appDownloadURL = appDownloadURL;
	}

	/**
	 * Gets the results per page.
	 *
	 * @return the results per page
	 */
	public Integer getResultsPerPage() {
		return resultsPerPage;
	}

	/**
	 * Sets the results per page.
	 *
	 * @param resultsPerPage the new results per page
	 */
	public void setResultsPerPage(Integer resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	/**
	 * Gets the jasper path.
	 *
	 * @return the jasper path
	 */
	public String getJasperPath() {
		return jasperPath;
	}

	/**
	 * Sets the jasper path.
	 *
	 * @param jasperPath the new jasper path
	 */
	public void setJasperPath(String jasperPath) {
		this.jasperPath = jasperPath;
	}

	/**
	 * Gets the mrh URL.
	 *
	 * @return the mrh URL
	 */
	public String getMrhURL() {
		return mrhURL;
	}

	/**
	 * Sets the mrh URL.
	 *
	 * @param mrhURL the new mrh URL
	 */
	public void setMrhURL(String mrhURL) {
		this.mrhURL = mrhURL;
	}

	/**
	 * Gets the settlement file location.
	 *
	 * @return the settlement file location
	 */
	public String getSettlementFileLocation() {
		return settlementFileLocation;
	}

	/**
	 * Sets the settlement file location.
	 *
	 * @param settlementFileLocation the new settlement file location
	 */
	public void setSettlementFileLocation(String settlementFileLocation) {
		this.settlementFileLocation = settlementFileLocation;
	}

	/**
	 * Gets the ussd password.
	 *
	 * @return the ussd password
	 */
	public String getUssdPassword() {
		return ussdPassword;
	}

	/**
	 * Sets the ussd password.
	 *
	 * @param ussdPassword the new ussd password
	 */
	public void setUssdPassword(String ussdPassword) {
		this.ussdPassword = ussdPassword;
	}

	public String getReportsDownloadPath() {
		return reportsDownloadPath;
	}

	public void setReportsDownloadPath(String reportsDownloadPath) {
		this.reportsDownloadPath = reportsDownloadPath;
	}

	public String getQrCodePath() {
		return qrCodePath;
	}

	public void setQrCodePath(String qrCodePath) {
		this.qrCodePath = qrCodePath;
	}

	public String getBulkPaymentReportDownloadPath() {
		return bulkPaymentReportDownloadPath;
	}

	public void setBulkPaymentReportDownloadPath(String bulkPaymentReportDownloadPath) {
		this.bulkPaymentReportDownloadPath = bulkPaymentReportDownloadPath;
	}
	
}
