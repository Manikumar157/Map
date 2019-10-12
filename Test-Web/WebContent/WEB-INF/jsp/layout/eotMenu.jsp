	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<style>
#list {
padding: 1px 1px !important; 
}
#list1 {
padding: 1px 1px !important; 
}
#list2 {
padding: 1px 1px !important; 
}
#list3 {
padding: 1px 1px !important; 
}
#list4 {
padding: 1px 1px !important; 
}
#list5 {
padding: 1px 1px !important; 
}
#list6 {
padding: 1px 1px !important; 
}
#list7 {
padding: 1px 1px !important; 
}
#list8 {
padding: 1px 1px !important; 
}
#list9 {
padding: 1px 1px !important; 
}
#list10 {
padding: 1px 1px !important; 
}
#list11 {
padding: 1px 1px !important; 
}
#list12 {
padding: 1px 1px !important; 
}
#list14 {
padding: 1px 1px !important; 
}
li a {
color :#FFFFFF;
}

#skin-select #toggle.active {
    height: 40px;
}
#skin-select #toggle {
	height: 41px;
}
</style>
            <div id="skin-select" style="width: 60px; height: 1157px; margin-top:5px;margin-left:30px;">
                <a id="toggle" style="top: 0px;">
                    <span class="fa icon-menu"></span>
                </a>
				<form:form name="menuForm" id="menuForm" method="post">
				<jsp:include page="/WEB-INF/jsp/csrf_token.jsp"/>
                <div class="skin-part">
                    <div id="tree-wrap">
                        <div class="side-bar">
                        <div style="cursor:pointer">
                            <ul id="menu-showhide" class="topnav" scrollable="auto">
                                <li class="devider-title">
                                    <h3>
                                        <span>Menu</span>
                                    </h3>
                                </li>
                                <!-- <div style="cursor:pointer"> -->
                                 <authz:authorize ifAnyGranted="ROLE_viewCurrencyAdminActivityAdmin,ROLE_viewExchangeRateAdminActivityAdmin,ROLE_viewLocationsAdminActivityAdmin,ROLE_viewLocateUSAdminActivityAdmin,ROLE_viewClearingHouseAdminActivityAdmin,ROLE_viewStakeHolderAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" title="Master Data"><i class="fas fa-globe" style="margin-left:4px; left:-5px;"></i><span><spring:message code="LABEL_MASTER_DATA" text="Master Data" /></span></a>							
									<ul id="list"> 
									<authz:authorize ifAnyGranted="ROLE_viewCurrencyAdminActivityAdmin">
										<li><a id="showCurrencies" title="Currency" href="javascript:submitForm('menuForm','showCurrencies.htm')"><spring:message code="LABEL_CURRENCY" text="Currencies" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewExchangeRateAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_EXCHANGE_RATE" text="Exchange Rate" />" id="showExchangeRateForm" href="javascript:submitForm('menuForm','showExchangeRateForm.htm')"><spring:message code="LABEL_EXCHANGE_RATE" text="Exchange Rate" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewCurrencyConverterAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_CURRENCY_CONVERTOR" text="Currency Converter" />" id="showCurrencyConverterForm" href="javascript:submitForm('menuForm','showCurrencyConverterForm.htm')"><spring:message code="LABEL_CURRENCY_CONVERTER" text="Currency Converter" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewLocationsAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_LOCATIONS" text="Locations"/>" id="showCountries" href="javascript:submitForm('menuForm','showCountries.htm')"><spring:message code="LABEL_LOCATIONS" text="Locations"/></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewLocateUSAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_LOCATE_US" text="Locate US" />" id="showLocateUSForm" href="javascript:submitForm('menuForm','showLocateUsForm.htm')"><spring:message code="LABEL_LOCATE_US" text="Locate US" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewClearingHouseAdminActivityAdmin">
										<li><a title="Clearing Houses" id="showClearanceHouseForm" href="javascript:submitForm('menuForm','showClearanceHouseForm.htm')"><spring:message code="LABEL_CH_MGMT" text="Clearing Houses" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewStakeHolderAdminActivityAdmin">
										<li><a title="Stake Holders" id="showStakeHolderForm" href="javascript:submitForm('menuForm','showStakeHolderForm.htm')"><spring:message code="LABEL_STAKEHOLDERFORM" text="Stake Holders" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewHelpDeskAdminActivityAdmin">
										<li><a id="showHelpDesk" title="HelpDesk" href="javascript:submitForm('menuForm','showHelpDeskConfig.htm')"><spring:message code="LABEL_HELPDESK_CONFIG" text="HelpDesk Config" /></a></li>
									</authz:authorize>
									</ul>
									</li>
								</authz:authorize>
									
								<authz:authorize ifAnyGranted="ROLE_viewBankGroupsAdminActivityAdmin,ROLE_viewBanksAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list1" title="Bank management"><i class="fa fa-bank" style="margin-left:4px;"></i><span><spring:message code="LABEL_BANK_MANAGEMENT" text="Bank management" /></span></a>
									<ul id="list1">
									<authz:authorize ifAnyGranted="ROLE_viewBankGroupsAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_BANKGROUPS" text="Bank Groups" />" id="showBankGroup" onClick="activateItem(this)" href="javascript:submitForm('menuForm','showBankGroup.htm')"><spring:message code="LABEL_BANKGROUPS" text="Bank Groups" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewBanksAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_BANK_MNMT" text="Banks" />" id="showBankManagementForm" href="javascript:submitForm('menuForm','showBankManagementForm.htm')"><spring:message code="LABEL_BANK_MNMT" text="Banks" /></a></li>
									</authz:authorize>
									<%-- Customer profile Moved to profile management : Bidyut
									
									<authz:authorize ifAnyGranted="ROLE_viewCustomerProfilesAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_CUSTOMER_PROFILES" text="Customer Profiles" />" id="showCustomerProfiles" href="javascript:submitForm('menuForm','showCustomerProfiles.htm')"><spring:message code="LABEL_CUSTOMER_PROFILES" text="Customer Profiles" /></a></li>
									</authz:authorize> --%>
									</ul>
									</li>
								</authz:authorize>
								
								<authz:authorize ifAnyGranted="ROLE_viewProfileManagementAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list2" title="Profile Management"><i class="far fa-address-card" style="margin-left:4px;"></i><span><spring:message code="LABEL_PROFILE_MANAGEMENT" text="Profile Mangement" /></span></a>
									<ul id="list2">
									
									<authz:authorize ifAnyGranted="ROLE_viewCustomerProfilesAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_CUSTOMER_PROFILES" text="Customer Profiles" />" id="showCustomerProfiles" href="javascript:submitForm('menuForm','showCustomerProfiles.htm')"><spring:message code="LABEL_CUSTOMER_PROFILES" text="Customer Profiles" /></a></li>
									</authz:authorize>
									</ul>
									</li>
								</authz:authorize>
								
								<authz:authorize ifAnyGranted="ROLE_viewBillerAdminActivityAdmin,ROLE_viewOperatorsAdminActivityAdmin,ROLE_viewSenelecBillsAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list3" title="Aggregator"><i class="fontello-warehouse" style="margin-left:4px;"></i><span><spring:message code="LABEL_AGGREGATOR" text="Aggregator" /></span></a>
									<ul id="list3">
										<!-- Added by Ajinkya for remittance,date:20160802 -->
									<%-- <authz:authorize ifAnyGranted="ROLE_viewRemittanceAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_REMITTANCE" text="Remittance" />" id="remittanceCompany" href="javascript:submitForm('menuForm','remittanceCompany.htm')"><spring:message code="LABEL_REMITTANCE" text="Remittance" /></a></li>
									</authz:authorize> --%>
									<authz:authorize ifAnyGranted="ROLE_viewBillerAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_BILLER" text="BILLER" />" id="searchBiller" href="javascript:submitForm('menuForm','searchBiller.htm')"><spring:message code="LABEL_BILLER" text="BILLER" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewOperatorsAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_OPERATOR" text="Operators"/>" id="showOperators" href="javascript:submitForm('menuForm','showOperators.htm')"><spring:message code="LABEL_OPERATOR" text="Operators"/></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewSenelecBillsAdminActivityAdmin">
										<li><a title="Bill Payment" id="getSenelecBills" href="javascript:submitForm('menuForm','getSenelecBills.htm')"><spring:message code="LABEL_SENELEC_BILLS" text="Senelec Bills" /></a></li>
									</authz:authorize>
									</ul>
									</li>
								</authz:authorize>
								
								<authz:authorize ifAnyGranted="ROLE_addInterBankFeeAdminActivityAdmin,ROLE_viewServiceChargeRulesAdminActivityAdmin,ROLE_viewTransacationRulesAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list4" title="Rule Engine"><i class="fa fa-cogs" style="margin-left:4px;"></i><span><spring:message code="LABEL_RULE_ENGINE" text="Rule Engine" /></span></a>
									<ul id="list4">
									<authz:authorize ifAnyGranted="ROLE_addInterBankFeeAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_INTER_BANK_FEE" text="Inter Bank Fee" />" id="interBankForm" href="javascript:submitForm('menuForm','interBankForm.htm')"><spring:message code="LABEL_INTER_BANK_FEE" text="Inter Bank Fee" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewServiceChargeRulesAdminActivityAdmin">
										<%-- <li><a title="<spring:message code="LABEL_SC_MGMT" text="Service Charge Rules" />" id="searchServiceChargeRules" href="javascript:submitForm('menuForm','searchServiceChargeRules.htm?ruleLevel=1')"><spring:message code="LABEL_SC_MGMT" text="Service Charge Rules" /></a></li> --%>
										<li><a title="<spring:message code="LABEL_SC_MGMT" text="Service Charge Rules" />" id="searchServiceChargeRules" href="javascript:serviceDetail('searchServiceChargeRules.htm','1')"><spring:message code="LABEL_SC_MGMT" text="Service Charge Rules" /></a></li>
									<input type="hidden" name="ruleLevel" id="ruleLevel" value=""/>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewTransacationRulesAdminActivityAdmin">	
										<%-- <li><a title="<spring:message code="LABEL_TR_RULE" text="Transacation Rules" />" id="searchTransactionRules" href="javascript:submitForm('menuForm','searchTransactionRules.htm?ruleLevel=1')"><spring:message code="LABEL_TR_RULE" text="Transacation Rules" /></a></li> --%>
										<li><a title="<spring:message code="LABEL_TR_RULE" text="Transacation Rules" />" id="searchTransactionRules" href="javascript:serviceDetail('searchTransactionRules.htm','0')"><spring:message code="LABEL_TR_RULE" text="Transacation Rules" /></a></li>
									<input type="hidden" name="ruleLevel" id="ruleLevel" value=""/>
									</authz:authorize>
									
									<authz:authorize ifAnyGranted="ROLE_viewSMSAlertsAdminActivityAdmin">
									<%-- <li><a href="javascript:submitForm('menuForm','searchSmsAlertRules.htm')" id="showSmsAlertPackages"><spring:message code="LABEL_SMS_ALERTS" text="SMS Alerts"/></a></li> --%>
									<li><a href="javascript:smsAlert('searchSmsAlertRules.htm','menuForm')"><spring:message code="LABEL_SMS_ALERTS" text="SMS Alerts"/></a></li>		
									<input type="hidden" name="form" id="form" value=""/>		
									</authz:authorize>
									
									<authz:authorize ifAnyGranted="ROLE_addCommissionAdminActivityAdmin"> 
									<li><a href="javascript:submitForm('menuForm','searchMerchantCommission.htm')" id="marchantCommission"><spring:message code="LABEL_Merchant_Commission" text="Agent Commission"/></a></li>
									</authz:authorize> 
									
									<li><a href="javascript:submitForm('menuForm','commissionSplitForm.htm')" id="commissionSplitForm"><spring:message code="LABEL_COMMISION_SPLITS" text="Commision Splits"/></a></li>
									</ul>
									
									
									</li>
								</authz:authorize>	
								
								<authz:authorize ifAnyGranted="ROLE_viewMISAdminActivityAdmin,ROLE_viewTransactionReportsAdminActivityAdmin,ROLE_viewAccountReportsAdminActivityAdmin,
								              ROLE_viewReversalAdminActivityAdmin,ROLE_viewExternalTransactionsAdminActivityAdmin,ROLE_viewTransactionSupportAdminActivityAdmin,
								              ROLE_viewPendingTransactionsAdminActivityAdmin,ROLE_viewVoidAdminActivityAdmin,ROLE_viewSettlementDetailsAdminActivityAdmin,
								              ROLE_viewTransactionsAdminActivityAdmin">
								              
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list5" title="Transactions & Reports" ><i class="far fa-chart-bar" style="margin-left:4px;"></i><span><spring:message code="LABEL_TRANSACCTION_REPORTS" text="Transactions & Reports" /></span></a>
									<ul id="list5">
									<authz:authorize ifAnyGranted="ROLE_viewTransactionsAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_VIEW_TXN" text="View Transactions" />" id="selectTransactionForm" href="javascript:submitForm('menuForm','selectTransactionForm.htm')"><spring:message code="LABEL_VIEW_TXN" text="View Transactions" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewAccountTransferAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_ACC_TXN" text="View Transactions" />" id="selectAccountTransactionForm" href="javascript:submitForm('menuForm','accountTransfer.htm')"><spring:message code="LABEL_ACC_TXN" text="Account Transaction" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewMISAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_MIS" text="MIS" />" id="showTxnSummary" href="javascript:submitForm('menuForm','showTxnSummary.htm')"><spring:message code="LABEL_MIS" text="MIS" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerCommissionReportAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_BUSINESSPARTNER_COMMISSION" text="Business Partner Commission" />" id="businessPartnerCommission" href="javascript:submitForm('menuForm','showTxnBusinessPartner.htm')"><spring:message code="LABEL_BUSINESSPARTNER_COMMISSION" text="Commission Reports" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewTransactionReportsAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_TXN_REPORTS" text="Transaction Reports" />" id="showCharts" href="javascript:submitForm('menuForm','showCharts.htm')"><spring:message code="LABEL_TXN_REPORTS" text="Transaction Reports" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewAccountReportsAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_ACC_REPORTS" text="Account Reports" />" id="txnReports" href="javascript:submitForm('menuForm','txnReports.htm')"><spring:message code="LABEL_ACC_REPORTS" text="Account Reports" /></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewReversalAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_REVERSAL_LEFT" text="Reversal" />" id="showAdjustmentForm" href="javascript:submitForm('menuForm','showAdjustmentForm.htm')"><spring:message code="LABEL_REVERSAL_LEFT" text="Reversal" /></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewExternalTransactionsAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_EXTERNAL_TRANSACTIONS" text="External Transactions" />" id="showExternalTransactions" href="javascript:submitForm('menuForm','showExternalTransactions.htm')"><spring:message code="LABEL_EXTERNAL_TRANSACTIONS" text="External Transactions" /></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewTransactionSupportAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_TXN_SUPPORT" text="Transaction Support" />" id="selectTransactionSupportForm" href="javascript:submitForm('menuForm','selectTransactionSupportForm.htm')"><spring:message code="LABEL_TXN_SUPPORT" text="Transaction Support" /></a></li> 
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewPendingTransactionsAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_PENDING_TXNS" text="Pending Transactions" />" id="showPendingTransactions" href="javascript:submitForm('menuForm','showPendingTransactions.htm')"><spring:message code="LABEL_PENDING_TXNS" text="Pending Transactions" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewVoidAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_VOID" text="Void" />" id="cancellation" href="javascript:submitForm('menuForm','cancellation.htm')"><spring:message code="LABEL_VOID" text="Void" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewSettlementDetailsAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_SETTLEMT_DETAILS" text="Settlement Details" />" id="settlementDetails" href="javascript:submitForm('menuForm','settlementDetails.htm')"><spring:message code="LABEL_SETTLEMT_DETAILS" text="Settlement Details" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewCustomerRegistrationCommissionReportAdminActivityAdmin">	
										<li style="padding: 0px 0 20px 0px;"><a title="<spring:message code="LABEL_CUS_REG_COMM_RPT" text="Customer Registration Commission Report" />" id="cusAppCommReport" href="javascript:submitForm('menuForm','cusAppCommReport.htm')"><spring:message code="LABEL_CUS_REG_COMM_RPT" text="Customer Registration Commission Report" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewMerchantCommissionReportAdminActivityAdmin">	
										<li style="padding: 0px 0 0px 0px;"><a title="<spring:message code="LABEL_MERCHANT_REPORT" text="Merchant Report" />" id="merchantAppReport" href="javascript:submitForm('menuForm','merchantReport.htm')"><spring:message code="LABEL_MERCHANT_REPORT" text="Merchant Report" /></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewBankFloatDepositAdminActivityAdmin">	
										<li style="padding: 0px 0 0px 0px;"><a title="<spring:message code="LABEL_BANK_FLOAT_DEPOSIT" text="Bank Float Deposit" />" id="bankFloatDeposit" href="javascript:submitForm('menuForm','bankFloatDeposit.htm')"><spring:message code="LABEL_BANK_FLOAT_DEPOSIT" text="Bank Float Deposit" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewNonRegisteredUSSDCustomerReportAdminActivityAdmin">	
										<li style="padding: 0px 0 0px 0px;"><a title="<spring:message code="LABEL_NON_REG_USSD_CUSTOMERS" text="Non Reg USSD Customers" />" id="nonRegUssdCustomer" href="javascript:submitForm('menuForm','nonRegUssdCustomer.htm')"><spring:message code="LABEL_NON_REG_USSD_CUSTOMERS" text="Non Reg USSD Customers" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewTransactionVolumeReportAdminActivityAdmin">	
										<li style="padding: 0px 0 0px 0px;"><a title="<spring:message code="LABEL_TRANSACTION_VOLUME_REPORT" text="Transaction Volume Report" />" id="transactionVolume" href="javascript:submitForm('menuForm','transactionVolume.htm')"><spring:message code="LABEL_TRANSACTION_VOLUME_REPORT" text="Transaction Volume Report" /></a></li>
									</authz:authorize>
									<%-- <li><a title="<spring:message code="LABEL_TXN_DASHBOARD" text="DashBoard" />" id="dashBoard" href="javascript:submitForm('menuForm','getTxnSummaryDashBoard.htm')"><spring:message code="LABEL_TXN_DASHBOARD" text="DashBoard" /></a></li> --%>
									</ul>
									</li>
								</authz:authorize>	
								
								<authz:authorize ifAnyGranted="ROLE_viewUserManagementAdminActivityAdmin,ROLE_addAssignPrivilegeAdminActivityAdmin,ROLE_viewCustomersAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list6" title="User Management"><i class="fas fa-users" style="margin-left:4px;"></i><span><spring:message code="LABEL_USER_MANAGEMENT" text="User Management" /></span></a>
									<ul id="list6">
									<authz:authorize ifAnyGranted="ROLE_viewUserManagementAdminActivityAdmin">
										<li><a title="Web Users" id="showWebUserForm" href="javascript:submitForm('menuForm','showWebUserForm.htm')"><spring:message code="LABEL_USER_MNMT" text="Web Users" /></a></li>
									</authz:authorize>
									<authz:authorize  ifAnyGranted="ROLE_addAssignPrivilegeAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_PRIVILEGE" text="Assign Privilege"/>" id="showPrivilegeDetails" href="javascript:submitForm('menuForm','showPrivilegeDetails.htm')"><spring:message code="LABEL_PRIVILEGE" text="Assign Privilege"/></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewCustomersAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_CUSTOMER_REGISTRATION" text="Customers" />" id="searchCustomer" href="javascript:searchDetail('searchCustomer.htm','0')"><spring:message code="LABEL_CUSTOMER_REGISTRATION" text="Customers" /></a></li>
										<input type="hidden" name="type" id="type" value=""/>																
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewMerchantAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_MERCHANT" text="Merchant" />" id="searchMerchant" href="javascript:submitForm('menuForm','searchMerchant.htm')"><spring:message code="LABEL_MERCHANT" text="Merchant" /></a></li>
									</authz:authorize>
									<%-- <!-- vinod change -->
									<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_BUSINESSPARTNER_SHOWFORM" text="Business Partner" />" id="searchCustomer" href="javascript:submitForm('menuForm','businessPartner.htm')"><spring:message code="LABEL_BUSINESSPARTNER_SHOWFORM" text="Business Partner" /></a></li>
									</authz:authorize>
									<!-- vinod change -->
									
									<authz:authorize ifAnyGranted="ROLE_viewMerchantAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_MERCHANT" text="Merchant" />" id="searchMerchant" href="javascript:submitForm('menuForm','searchMerchant.htm')"><spring:message code="LABEL_MERCHANT" text="Merchant" /></a></li>
									</authz:authorize> --%>
									</ul>
									</li>
								</authz:authorize>	
								<authz:authorize ifAnyGranted="ROLE_viewAuditLogsAdminActivityAdmin,ROLE_viewAccessLogsAdminActivityAdmin,ROLE_viewApplicationVersionAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list7" title="Logs and Security"><i class="fas fa-lock" style="margin-left:4px;"></i><span><spring:message code="LABEL_LOGS_SECURITY" text="Logs and Security" /></span></a>
									<ul id="list7">
									<authz:authorize ifAnyGranted="ROLE_viewAuditLogsAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_AUDIT_LOG" text="Audit Logs" />" id="searchAuditLogForm" href="javascript:submitForm('menuForm','searchAuditLogForm.htm')"><spring:message code="LABEL_AUDIT_LOG" text="Audit Logs" /></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewAccessLogsAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_ACCESSLOG_UI" text="Access Logs" />" id="showAccessLog" href="javascript:submitForm('menuForm','showAccessLog.htm')"><spring:message code="LABEL_ACCESSLOG_UI" text="Access Logs" /></a></li>
									</authz:authorize>	
									<authz:authorize ifAnyGranted="ROLE_viewApplicationVersionAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_APP_VERSION_INFORMATION" text="Application Version " />" id="showVersionFrom" href="javascript:submitForm('menuForm','showVersionFrom.htm')"><spring:message code="LABEL_APP_VERSION_INFORMATION" text="Application Version " /></a></li>
									</authz:authorize>
									</ul>
									</li>
								</authz:authorize>	
								<!-- #-Start-# Dynamic Menu configuration @11-05-2018 by sudhanshu -->
								<authz:authorize ifAnyGranted="ROLE_addConfigurationAdminActivityAdmin,ROLE_viewConfigurationAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list8" title="Menu Config"><i class="fa fa-cogs" style="margin-left:4px;"></i><span><spring:message code="LABEL_CONFIG" text="Configuration" /></span></a>
									<ul id="list8">
										<li><a title="<spring:message code="LABEL_MENU_MERCHANT" text="Merchant" />" id="merchant" href="javascript:submitForm('menuForm','dynamicMenuConfigForm.htm?appType=1')"><spring:message code="LABEL_MENU_MERCHANT" text="Merchant" /></a></li>
										<li><a title="<spring:message code="LABEL_MENU_SOLE_MERCHANT" text="Sole Merchant" />" id="soleMerchant" href="javascript:submitForm('menuForm','dynamicMenuConfigForm.htm?appType=2')"><spring:message code="LABEL_MENU_SOLE_MERCHANT" text="Sole Merchant" /></a></li>
										<li><a title="<spring:message code="LABEL_MENU_CUSTOMER" text="Customer" />" id="menuCustomer" href="javascript:submitForm('menuForm','dynamicMenuConfigForm.htm?appType=0')"><spring:message code="LABEL_MENU_CUSTOMER" text="Customer" /></a></li>
										<li><a title="<spring:message code="LABEL_WEB_COLOR" text="Web Color" />" id="webColor" href="javascript:submitForm('menuForm','webColorConfigForm.htm')"><spring:message code="LABEL_WEB_COLOR" text="Web Color" /></a></li>
									</ul>
									</li>
								</authz:authorize>	
								<!-- #-End-# Dynamic Menu configuration @11-05-2018 by sudhanshu -->
									<!-- // vineeth changes, on 29-08-2018 -->							

								<authz:authorize ifAnyGranted="ROLE_addAgentAdminActivityAdmin,ROLE_viewAgentAdminActivityAdmin,ROLE_editAgentAdminActivityAdmin,,ROLE_deleteAgentAdminActivityAdmin,
												ROLE_addSoleMerchantAdminActivityAdmin,ROLE_viewSoleMerchantAdminActivityAdmin,ROLE_editSoleMerchantAdminActivityAdmin,,ROLE_deleteSoleMerchantAdminActivityAdmin,
												ROLE_addAgentSoleMerchantAdminActivityAdmin,ROLE_viewAgentSoleMerchantAdminActivityAdmin,ROLE_editAgentSoleMerchantAdminActivityAdmin,,ROLE_deleteAgentSoleMerchantAdminActivityAdmin">
									<c:if test="${roleAccess eq 2}">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list9" title="Menu Config"><i class="fas fa-user-cog" style="margin-left:4px;"></i><span><spring:message code="LABEL_AGENT_CHANNEL_APPROVALS" text="Channel Approvals" /></span></a>
									
									</c:if>
									<c:if test="${roleAccess ne 2}">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list9" title="Menu Config"><i class="fas fa-user-cog" style="margin-left:4px;"></i><span><spring:message code="LABEL_AGENT_MANAGEMENT" text="Agent Management" /></span></a>
									
									</c:if>
									<ul id="list9">
									<authz:authorize ifAnyGranted="ROLE_addAgentAdminActivityAdmin,ROLE_viewAgentAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_MENU_AGENT" text="Agent" />" id="merchant1" href="javascript:searchDetail('searchAgent.htm','1')"><spring:message code="LABEL_MENU_AGENT" text="Agent" /></a></li>
										
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_addSoleMerchantAdminActivityAdmin,ROLE_viewSoleMerchantAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_MENU_SOLE_MERCHANT" text="Merchant" />" id="soleMerchant1" href="javascript:searchDetail('searchAgent.htm','2')"><spring:message code="LABEL_MENU_SOLE_MERCHANT" text="Sole Merchant" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_addAgentSoleMerchantAdminActivityAdmin,ROLE_viewAgentSoleMerchantAdminActivityAdmin">									
										<li><a title="<spring:message code="LABEL_MENU_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant" />" id="menuCustomer1" href="javascript:searchDetail('searchAgent.htm','3')"><spring:message code="LABEL_MENU_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_addDownloadQRCodeAdminActivityAdmin,ROLE_viewDownloadQRCodeAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_DOWNLOAD_QR_CODE" text="Download QR Code" />" id="merchant1" href="javascript:searchDetail('searchQRCodeForm.htm','1')"><spring:message code="LABEL_DOWNLOAD_QR_CODE" text="Download QR Code" /></a></li>
										
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_addAgentCashOutAdminActivityAdmin,ROLE_viewAgentCashOutAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_AGENT_CASHOUT" text="Agent CashOut" />" id="agentCashOut" href="javascript:searchDetail('searchAgentCashOut.htm','1')"><spring:message code="LABEL_AGENT_CASHOUT" text="Agent CashOut" /></a></li>
										
									</authz:authorize>
									</ul>
									</li>
									<input type="hidden" name="type" id="type" value=""/>
								</authz:authorize>	
								
								
								<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerAdminActivityAdmin,ROLE_addBusinessPartnerAdminActivityAdmin,ROLE_viewBusinessPartnerTxnReportAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list10" title="Logs and Security"><i class="fas fa-user-tie" style="margin-left:4px;"></i>
									<c:if test="${roleAccess eq 2}">
									<span><spring:message code="LABEL_PRINCIPAL_AGENT_MGMT" text="Principal Agent Mgmt" /></span>
									</c:if>
									<c:if test="${roleAccess eq 3 || roleAccess eq 29 || roleAccess eq 1 || roleAccess eq 24 || roleAccess eq 25}">
									<span><spring:message code="LABEL_BUSINESS_PARTNER_MGMT" text="Business Partner Mgmt" /></span>
									</c:if>
									</a>
									<ul id="list10">
									
									<c:if test="${roleAccess eq 2}">
									<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_PRINCIPAL_AGENT_SHOWFORM" text="Principal Agent" />" id="searchCustomer" href="javascript:submitForm('menuForm','businessPartner.htm')"><spring:message code="LABEL_PRINCIPAL_AGENT_SHOWFORM" text="Principal Agent" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerTxnReportAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_PRINCIPAL_AGENT_TRANSACTIONS" text="Principal Agent Transactions" />" id="searchBusinessPartner" href="javascript:submitForm('menuForm','businessPartnerTransaction.htm')"><spring:message code="LABEL_PRINCIPAL_AGENT_TRANSACTIONS" text="Principal Agent Transactions" /></a></li>										
									</authz:authorize>
									</c:if>
									<c:if test="${roleAccess eq 3 || roleAccess eq 29 || roleAccess eq 1 || roleAccess eq 24}">
									<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_BUSINESSPARTNER_SHOWFORM" text="Business Partner" />" id="searchCustomer" href="javascript:submitForm('menuForm','businessPartner.htm')"><spring:message code="LABEL_BUSINESSPARTNER_SHOWFORM" text="Business Partner" /></a></li>
									</authz:authorize>
									</c:if>
									<c:if test="${roleAccess eq 3 || roleAccess eq 29 || roleAccess eq 1 || roleAccess eq 24 || roleAccess eq 25}">
									<authz:authorize ifAnyGranted="ROLE_viewBusinessPartnerTxnReportAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_BUSINESSPARTNER_TRANSACTIONS" text="Business Partner Transactions" />" id="searchBusinessPartner" href="javascript:submitForm('menuForm','businessPartnerTransaction.htm')"><spring:message code="LABEL_BUSINESSPARTNER_TRANSACTIONS" text="Business Partner Transactions" /></a></li>										
									</authz:authorize>
									</c:if>
									</ul>
									</li>
								</authz:authorize>	
								
								
								
								<!-- BulkPayment Partner -->
								<authz:authorize ifAnyGranted="ROLE_viewBulkPaymentPartnerAdminActivityAdmin,ROLE_addBulkPaymentPartnerAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list11" title="Logs and Security"><i class="fas fa-user-tie" style="margin-left:4px;"></i>
									<span><spring:message code="BULK_PAYMENT_PARTNER_MGMT" text="BulkPayment MGMT" /></span>
									</a>
									<ul id="list11">
									
									<authz:authorize ifAnyGranted="ROLE_viewBulkPaymentPartnerAdminActivityAdmin">	
										<li><a title="<spring:message code="LABEL_BULKPAYMENT_PARTNER_SHOWFORM" text="BulkPayment Partner" />" id="searchCustomer" href="javascript:submitForm('menuForm','bulkpaymentpartnerPartner.htm')"><spring:message code="LABEL_BULKPAYMENT_PARTNER_SHOWFORM" text="BulkPayment Partner" /></a></li>
									</authz:authorize>
									</ul>
									</li>
								</authz:authorize>	
								
								
								<!-- Start Changes by vinod joshi -->
								<authz:authorize ifAnyGranted="ROLE_viewBulkPaymentAdminActivityAdmin">
								              
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list12" title="Bulk Payment" ><i class="far fa-chart-bar" style="margin-left:4px;"></i><span><spring:message code="LABEL_BULK_PAYMENT" text="Bulk Payment" /></span></a>
											<ul id="list12">
											<authz:authorize ifAnyGranted="ROLE_viewBulkPaymentAdminActivityAdmin">	
												<li><a title="<spring:message code="LABEL_BULK_PAYMENT_FILE_UPLOAD" text="Upload File" />" id="transactionTest" href="javascript:submitForm('menuForm','uploadTransactionDetails.htm')"><spring:message code="LABEL_BULK_PAYMENT_FILE_UPLOAD" text="Bulk upload" /></a></li> 
											</authz:authorize>
											
											<authz:authorize ifAnyGranted="ROLE_viewBulkPaymentAdminActivityAdmin">	
												<li><a title="<spring:message code="LABEL_DOWNLOAD_FAILED_FILES" text="Download Failed Files" />" id="downloadFailFile" href="javascript:submitForm('menuForm','showFailedFiles.htm')"><spring:message code="LABEL_DOWNLOAD_FAILED_FILES" text="Download Failed Files" /></a></li> 
											</authz:authorize>
											
											<authz:authorize ifAnyGranted="ROLE_viewBulkPaymentAdminActivityAdmin">	
												<li><a title="<spring:message code="LABEL_BULK_PAY_TXN_REPORT" text="Bulk Payment Transaction Report" />" id="bulkPayTxnReport" href="javascript:submitForm('menuForm','bulkPayTxnReport.htm')"><spring:message code="LABEL_BULK_PAY_TXN_REPORT" text="Bulk Payment Transaction Report" /></a></li> 
											</authz:authorize>
											</ul>
											<input type="hidden" name=templetID id=templetID value=4>
									</li>
								</authz:authorize>	
								
								<authz:authorize ifAnyGranted="ROLE_viewDashBoardAdminActivityAdmin">
								              
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list13" title="Transaction Dashboard" ><i class="far fa-chart-bar" style="margin-left:4px;"></i><span><spring:message code="LABLE_TXN_DASHBOARD" text="Transaction Dashboard" /></span></a>
											<ul id="list13">
											<authz:authorize ifAnyGranted="ROLE_viewDashBoardAdminActivityAdmin">	
												<li><a title="<spring:message code="LABEL_TXN_DASHBOARD" text="DashBoard" />" id="dashBoard" href="javascript:submitForm('menuForm','getTxnSummaryDashBoard.htm')"><spring:message code="LABEL_TXN_DASHBOARD" text="DashBoard" /></a></li> 
											</authz:authorize>
											</ul>
											<input type="hidden" name=templetID id=templetID value=4>
									</li>
								</authz:authorize>
								
								
								<authz:authorize ifAnyGranted="ROLE_viewSMSLogAdminActivityAdmin,ROLE_viewSMSCampaignAdminActivityAdmin,ROLE_addSMSCampaignAdminActivityAdmin">
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list14" title="SMS"><i class="far fa-address-card" style="margin-left:4px;"></i><span><spring:message code="LABEL_SMS" text="SMS" /></span></a>
									<ul id="list14">
									
									<authz:authorize ifAnyGranted="ROLE_viewSMSLogAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_SMS_LOG" text="SMS Log" />" id="showSMSLog" href="javascript:submitForm('menuForm','viewSmsDetails.htm')"><spring:message code="LABEL_SMS_LOG" text="SMS LOGS" /></a></li>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_viewSMSCampaignAdminActivityAdmin,ROLE_addSMSCampaignAdminActivityAdmin">
										<li><a title="<spring:message code="LABEL_CAMPAIGN" text="Campaign" />" id="showSMSCampaign" href="javascript:submitForm('menuForm','addSmsCampaign.htm')"><spring:message code="LABEL_CAMPAIGN" text="Campaign" /></a></li>
									</authz:authorize>
									</ul>
									</li>
								</authz:authorize>
								
								<%-- <!-- Start Changes by vinod joshi -->
								<authz:authorize ifAnyGranted="ROLE_addAgentAdminActivityAdmin,ROLE_viewAgentAdminActivityAdmin,ROLE_editAgentAdminActivityAdmin,,ROLE_deleteAgentAdminActivityAdmin,
												ROLE_addSoleMerchantAdminActivityAdmin,ROLE_viewSoleMerchantAdminActivityAdmin,ROLE_editSoleMerchantAdminActivityAdmin,,ROLE_deleteSoleMerchantAdminActivityAdmin,
												ROLE_addAgentSoleMerchantAdminActivityAdmin,ROLE_viewAgentSoleMerchantAdminActivityAdmin,ROLE_editAgentSoleMerchantAdminActivityAdmin,ROLE_deleteAgentSoleMerchantAdminActivityAdmin,ROLE_viewBulkPaymentPartnerAdminActivityAdmin,ROLE_addBulkPaymentPartnerAdminActivityAdmin">
								              
									<li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list13" title="BulkPayment Partner" ><i class="far fa-chart-bar" style="margin-left:4px;"></i><span><spring:message code="BULK_PAYMENT_PARTNER_MGMT" text="BulkPayment Partner" /></span></a>
											<ul id="list13">
												<li><a title="<spring:message code="LABEL_BULKPAYMENT_PARTNER_SHOWFORM" text="BulkPayment Partner" />" id="searchCustomer" href="javascript:submitForm('menuForm','businessPartner.htm')"><spring:message code="LABEL_BULKPAYMENT_PARTNER_SHOWFORM" text="BulkPaymentPartner" /></a></li>
											</ul>
									</li>
								</authz:authorize>	 --%>
								
								
								
								<!-- Start Changes by vinod joshi -->
						
										<!-- // vineeth chnages over-->

								<%-- <authz:authorize ifAnyGranted="ROLE_addConfigurationAdminActivityAdmin,ROLE_viewConfigurationAdminActivityAdmin", > --%>
									<%-- <li style="border-bottom: 1px solid rgb(243, 213, 6);"><a class="tooltip-tip" data-toggle="#list8" title="Theme Config"><i class="fa fa-paint-brush"></i><span><spring:message code="LABEL_THEME_CONFIG" text="Theme Config" /></span></a>
									<ul id="list8">
										<li><a title="<spring:message code="LABEL_MENU_MERCHANT" text="Merchant" />" id="merchantTheme" href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=1')"><spring:message code="LABEL_MENU_MERCHANT" text="Merchant" /></a></li>
										<li><a title="<spring:message code="LABEL_MENU_SOLE_MERCHANT" text="Sole Merchant" />" id="soleMerchantTheme" href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=2')"><spring:message code="LABEL_MENU_SOLE_MERCHANT" text="Sole Merchant" /></a></li>
										<li><a title="<spring:message code="LABEL_MENU_CUSTOMER" text="Customer" />" id="customerTheme" href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=0')"><spring:message code="LABEL_MENU_CUSTOMER" text="Customer" /></a></li>
									</ul>
									</li> --%>
								<%-- </authz:authorize>	 --%>
								</ul>
									</div>
                                <!-- </ul> -->
                                </div>
                            </div>
                        </div>
                         </form:form>
                    </div>
                  
               
       <script>
       //@start Vineeth Date:16/10/2018 purpose:cross site Scripting -->
		function searchDetail(url,operatorId){
			document.getElementById('type').value=operatorId;
			submitlink(url,'menuForm');
		}//@end
		
		
		 //@start @Vinod Joshi Date:17/10/2018 purpose:cross site Scripting for Tx rules and Service Charge rules -->
		function serviceDetail(url,serviceChargeRuleId){
			document.getElementById('ruleLevel').value=serviceChargeRuleId;
			submitlink(url,'menuForm');
		}
		//@end
	  
		//@start @Vinod Joshi Date:17/10/2018 purpose:cross site Scripting for Tx rules and Service Charge rules -->
		function smsAlert(url,form){
		document.getElementById('form').value=form;
		submitlink(url,form);
		}
		</script>