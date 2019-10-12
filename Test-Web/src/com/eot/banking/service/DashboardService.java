/* Copyright EOT 2018. All rights reserved.
*
* This software is the confidential and proprietary information
* of EOT. You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms and
* conditions entered into with EOT.
*
* Id: DashboardService.java
*
* Date Author Changes
* 17 Dec, 2018 Sudhanshu Created
*/
package com.eot.banking.service;

import java.util.List;
import java.util.Map;

import com.eot.banking.dto.DashboardDTO;
import com.eot.entity.BusinessPartner;

/**
 * The Interface DashboardService.
 */
public interface DashboardService {
	
	/**
	 * Gets the daily transaction.
	 *
	 * @return the daily transaction
	 */
	public DashboardDTO getDailyTransaction(DashboardDTO dashboardDTO);
	
	/**
	 * Gets the transaction summary.
	 *
	 * @return the transaction summary
	 */
	public DashboardDTO getTransactionSummary(DashboardDTO dashboardDTO);
	
	/**
	 * Gets the commmission sahre.
	 *
	 * @return the commmission sahre
	 */
	public DashboardDTO getCommmissionSahre(DashboardDTO dashboardDTO);
	
	/**
	 * Gets the mobile user statistics.
	 *
	 * @return the mobile user statistics
	 */
	public DashboardDTO getMobileUserStatistics(DashboardDTO dashboardDTO);
	
	public List<BusinessPartner> loadBusinessPartnerByType(Integer partnerType) throws Exception;
	
	public List<Map<String, Object>> loadAccBalaceByUserId(String loginUser,Integer roleId);
}
