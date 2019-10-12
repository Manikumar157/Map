<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/ddlevelsmenu-sidebar.css" />
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /> </title>
<script type="text/javascript" src="js/ddlevelsmenu.js"></script>
<script type="text/javascript">
	ddlevelsmenu.setup("ddsidemenubar", "sidebar");
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body>
	<table width="187" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="187"><table width="15%" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td colspan="2" valign="top" bgcolor="#30314f"><img
							src="images/img04.jpg" alt="" width="187" height="35" />
						</td>
					</tr>
					<tr>
						<td valign="top" bgcolor="#30314f"><div id="ddsidemenubar"
								class="markermenu">
								<ul>
								
								<authz:authorize ifAnyGranted="ROLE_viewCurrencyAdminActivityAdmin">
									<li><a href="showCurrencies.htm"><spring:message code="LABEL_CURRENCY" text="Currencies" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewLocationsAdminActivityAdmin">
									<li><a href="showCountries.htm"><spring:message code="LABEL_LOCATIONS" text="Locations"/></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewClearingHouseAdminActivityAdmin">
									<li><a href="showClearanceHouseForm.htm"><spring:message code="LABEL_CH_MGMT" text="Clearing House" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewBankGroupsAdminActivityAdmin">
									<li><a href="showBankGroup.htm"><spring:message code="LABEL_BANKGROUPS" text="Bank Groups" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewBanksAdminActivityAdmin">
									<li><a href="showBankManagementForm.htm"><spring:message code="LABEL_BANK_MNMT" text="Banks" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewBillerAdminActivityAdmin">
									<li><a href="searchBiller.htm"><spring:message code="LABEL_BILLER" text="BILLER" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewOperatorsAdminActivityAdmin">
									<li><a href="showOperators.htm"><spring:message code="LABEL_OPERATOR" text="Operators"/></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewStakeHolderAdminActivityAdmin">
									<li><a href="showStakeHolderForm.htm"><spring:message code="LABEL_STAKEHOLDERFORM" text="Stake Holder" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewUserManagementAdminActivityAdmin">
									<li><a href="showWebUserForm.htm"><spring:message code="LABEL_USER_MNMT" text="User Management" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_addInterBankFeeAdminActivityAdmin">
									<li><a href="interBankForm.htm"><spring:message code="LABEL_INTER_BANK_FEE" text="Inter Bank Fee" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewServiceChargeRulesAdminActivityAdmin">
									<li><a href="searchServiceChargeRules.htm?ruleLevel=1"><spring:message code="LABEL_SC_MGMT" text="Service Charge Rules" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewTransacationRulesAdminActivityAdmin">
									<li><a href="searchTransactionRules.htm?ruleLevel=1"><span><spring:message code="LABEL_TR_RULE" text="Transacation Rules" /></span></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewCustomersAdminActivityAdmin">
									<li><a href="searchCustomer.htm"><spring:message code="LABEL_CUSTOMER_REGISTRATION" text="Customers" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewSenelecBillsAdminActivityAdmin">
									<li><a href="getSenelecBills.htm"><spring:message code="LABEL_SENELEC_BILLS" text="Senelec Bills" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewAuditLogsAdminActivityAdmin">
									<li><a href="searchAuditLogForm.htm"><spring:message code="LABEL_AUDIT_LOG" text="Audit Logs" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewAccessLogsAdminActivityAdmin">
									<li><a href="showAccessLog.htm"><spring:message code="LABEL_ACCESSLOG_UI" text="Access Logs" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewApplicationVersionAdminActivityAdmin">
									<li><a href="showVersionFrom.htm"><spring:message code="LABEL_APP_VERSION_INFORMATION" text="Application Version " /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewTransactionSupportAdminActivityAdmin">
									<li><a href="selectTransactionSupportForm.htm"><spring:message code="LABEL_TXN_SUPPORT" text="Transaction Support" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewMISAdminActivityAdmin">
									<li><a href="showTxnSummary.htm"><spring:message code="LABEL_MIS" text="MIS" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewTransactionReportsAdminActivityAdmin">
									<li><a href="showCharts.htm"><spring:message code="LABEL_TXN_REPORTS" text="Transaction Reports" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewAccountReportsAdminActivityAdmin">
									<li><a href="txnReports.htm"><spring:message code="LABEL_ACC_REPORTS" text="Account Reports" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewReversalAdminActivityAdmin">
									<li><a href="showAdjustmentForm.htm"><spring:message code="LABEL_REVERSAL_LEFT" text="Reversal" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewExternalTransactionsAdminActivityAdmin">
									<li><a href="showExternalTransactions.htm"><spring:message code="LABEL_EXTERNAL_TRANSACTIONS" text="External Transactions" /></a></li>
								</authz:authorize>
								
								<authz:authorize ifAnyGranted="ROLE_viewCustomerProfilesAdminActivityAdmin">
									<li><a href="showCustomerProfiles.htm"><spring:message code="LABEL_CUSTOMER_PROFILES" text="Customer Profiles" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewTransactionsAdminActivityAdmin">
									<li><a href="selectTransactionForm.htm"><spring:message code="LABEL_TXN" text="Transactions" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewPendingTransactionsAdminActivityAdmin">
									<li><a href="showPendingTransactions.htm"><spring:message code="LABEL_PENDING_TXNS" text="Pending Transactions" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewSettlementDetailsAdminActivityAdmin">
									<li><a href="settlementDetails.htm"><spring:message code="LABEL_SETTLEMT_DETAILS" text="Settlement Details" /></a></li>
								</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_viewVoidAdminActivityAdmin">
									<li><a href="cancellation.htm"><spring:message code="LABEL_VOID" text="Void" /></a></li>
								</authz:authorize>
								
								<authz:authorize ifAnyGranted="ROLE_addAssignPrivilegeAdminActivityAdmin">
									<li><a href="showPrivilegeDetails.htm"><spring:message code="LABEL_PRIVILEGE" text="Assign Privilege"/></a></li>
								</authz:authorize>
								
								<!-- bank admin -->
								<%-- <li><a href="searchWebUser.htm"><spring:message code="LABEL_USER_MNMT" text="Web Users" /></a></li> --%>
								
								</ul>
							
							</div>
					</tr>
					<tr>
						<td height="19" valign="top" bgcolor="#30314f">&nbsp;</td>
					</tr>
					<tr>
						<td height="114" valign="bottom"><img
							src="images/lft_bot_img.jpg" alt="" width="187" height="117" />
						</td>
					</tr>
					<tr>
						<td height="40" valign="bottom"></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>