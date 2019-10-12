/* Copyright © EasOfTech 2015. All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of EasOfTech. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms and
 * conditions entered into with EasOfTech.
 *
 * Id: CSRFValidationFilter.java
 *
 * Date Author Changes
 * 19 Dec, 2015 Saroj.Biswal Created
 */
package com.eot.banking.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * The Class CSRFValidationFilter.
 */
public class CSRFValidationFilter implements Filter {

	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession(false);

		if ((request instanceof HttpServletRequest)&& (response instanceof HttpServletResponse)) {

			try {
				if(byPassUris(httpServletRequest)){
					if(null != session){
						boolean isValidRequest = true;
						String token_val = (String) session.getAttribute(CSRFTokenUtil.SESSION_ATTR_KEY);
						if (token_val != null && !httpServletRequest.getRequestURI().endsWith("/login/txnSummary.htm")) {
							isValidRequest = CSRFTokenUtil.isValid(httpServletRequest)/*true*/;
							if (!isValidRequest) {
								String userName = (String) session.getAttribute("userName");
								//Log this as this can be a security threat
								logger.warn("Invalid security Token. Supplied token: " 
										+ request.getParameter("csrfToken")
										+ ". Session token: " + token_val 
										+ ". IP: " + request.getRemoteAddr());
								logger.fatal("CSRF attack detected. Date/Time " + new Date() + " \n. User ID:" + userName);
								RequestDispatcher rd = request.getRequestDispatcher("sessionTimeOut.htm");
								rd.forward(request, response);
								return;
							}
						} else {
							// first request. Send it through...
							CSRFTokenUtil.getToken(session);
						}
					}
				}else{ 
					filterChain.doFilter(request, response); 
					return;
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
		filterChain.doFilter(request, response);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * By pass uris.
	 *
	 * @param httpServletRequest the http servlet request
	 * @return true, if successful
	 */
	public boolean byPassUris(HttpServletRequest httpServletRequest) {

		return (httpServletRequest.getRequestURI().indexOf("txnSummary.htm") == -1 
				&& httpServletRequest.getRequestURI().indexOf("getPhoto.htm") == -1 
				&& httpServletRequest.getRequestURI().indexOf("getBPPhoto.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("forgotPassword.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("forgotPasswordOtp.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showForgotPasswordForm.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("processForgotPassword.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("forwardLogin.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("changeLocale.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("welcome.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("mobileNumLenthRequest.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showCustomerForm.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("viewTransactions.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("viewRequests.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("viewSMSLog.htm") == -1 
				&& httpServletRequest.getRequestURI().indexOf("listServiceChargeRules") == -1
				&& httpServletRequest.getRequestURI().indexOf("suscribedSMS.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("smsPackageDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("subscribeSMSPackage.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("subscribedSC.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("scPackageDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("saveCustomerCard.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getCurrentSubcription.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("subscribeSC.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getSCCurrentSubcription.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getSCSubscribedList.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("logout.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("home.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getStakeHolderMobileNumberLength.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsExternalTransactionSummaryDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsExternalTransactionDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXLSForLocations.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("customerMobileNum.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getMobileNumberLength.htm") == -1 
				&& httpServletRequest.getRequestURI().indexOf("viewCustomer.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getCityList.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("transferRequest.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getBusinessPartner.htm") == -1
				/*Author koushik -- date - 06-07-2018 -- purpose - bug 4373 start*/
			    && httpServletRequest.getRequestURI().indexOf("getBanks.htm") == -1
			    /*end*/
				&& httpServletRequest.getRequestURI().indexOf("getQuartersList.htm") == -1
				/*Author vineeth -- date - 16-07-2018 -- purpose - bug 5693 start*/
				&& httpServletRequest.getRequestURI().indexOf("settlementNetBalance.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsForAgentDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToPdfForAgentDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsForSCR.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToPdfForSCR.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToXlsForTxnRules.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToPdfForTxnRules.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToXlsWebUser.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToPdfWebUser.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToPdfMIS.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToXlsMIS.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToPDFBusinessPartners.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("businessPartnerExcelReport.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToXLSForLocations.htm") == -1
				 && httpServletRequest.getRequestURI().indexOf("exportToPDFLocations.htm") == -1
				/*end*/
				&& httpServletRequest.getRequestURI().indexOf("saveTxnStatement.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showAdjustmentForm.htm") == -1				
				&& httpServletRequest.getRequestURI().indexOf("reinstallApplication.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("resetLoginPin.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("resetTxnPin.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("saveCustomerBankAccount.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("loadPrivilegeDetails.htm") == -1
				&& httpServletRequest.getRequestURI().contains("XLS") == false
				&& httpServletRequest.getRequestURI().contains("PDF") == false
				&& httpServletRequest.getRequestURI().contains("CSV") == false
				&& httpServletRequest.getRequestURI().contains("Report") == false
				&& httpServletRequest.getRequestURI().contains("Chart") == false)
				&& httpServletRequest.getRequestURI().indexOf("getCustomerProfilesByBank.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("loadGridMenu.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("loadMenuIcons.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getConfigurationBybank.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getCustomerProfilesForCommision.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("checkBankProfileExistance.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("fileUploadInstructions.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("saveThemeConfig.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("saveWebColorConfig.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("getPartnersByType.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("getTxnSummaryDashBoard.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("getAccBalanceByUserId.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("showTxnSummaryData.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("datasearchAuditLogForm.htm") == -1
		        && httpServletRequest.getRequestURI().indexOf("datashowAccessLog.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("changeAgentApplicationStatus.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getQuarters.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportQRCode.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("searchQRCodeForm.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("bulkExportQRCode.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("uploadTrnsactionFile.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("bulkpaymentpartnerPartner.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("viewBulkpaymentpartner.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("saveBulkPaymentPartner.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showBulkPaymentForm.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("searchBulkPaymentPartner.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("editBulkPaymentPartner.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("downloadFailedFiles.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("downloadBulkPaymentFailFile.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showTxnMerchantData.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("xlsForBulkPayTxn.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("searchAgentData.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showTxnSummaryDataCustomerRegistration.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("showTxnBusinessPartner.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("cusAppCommReport.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("merchantReport.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToPdfMerchantCommisionReport.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsMerchantCommisionReport.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("searchTxnMerchantData.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("searchCustomerData.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("viewSmsDetails.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("getOTPRequest.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsForBankFloatDeposit.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToPdfForBankFloatDeposit.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsForTransactionVolume.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToPdfForTransactionVolume.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsForNonRegUssdCustomer.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToPdfForNonRegUssdCustomer.htm") == -1
				&& httpServletRequest.getRequestURI().indexOf("exportToXlsForBlockedApplicationDetails.htm") == -1;
		
	}
	
}
