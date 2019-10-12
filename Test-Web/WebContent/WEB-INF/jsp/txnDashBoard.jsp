<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml">
<%@include file="tag/commonTagLib.jsp"%>
<%@ page errorPage="errorPage.jsp"%>

<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><spring:message code="LABEL_DASHBOARD" text="DashBoard"/></title>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<style>
h3.box-title label h4 {
    font-weight: 800;
    color: #000;
}

.vl {
  border-left: 2px solid #81ba9d;
  	position: absolute;
    left: 25%;
    margin-left: 0px;
    top: 0;
    height: 293px;
}
.col-sm-offset-10 {
	float: right;
    margin-right: 79%;
}
.col-sm-offset-9 {
	float: right;
    margin-right: 75%;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
	if($("#partnerType").val()==11){
		document.getElementById("partners").style.display='none';
		document.getElementById("partnerlabel").style.display='none';
		}
	$("#partnerType").change(function() {
		if(this.value==0){
			document.getElementById("partners").value=0;
			this.form.submit();
		}
		else if(this.value=='11'){
			document.getElementById("partners").style.display='none';
			document.getElementById("partnerlabel").style.display='none';
			this.form.submit();
			}
//alert(this.value)
//this.form.submit();
$csrfToken = $("#csrfToken").val();
$.post("getPartnersByType.htm", {
	partnerType : this.value,
	csrfToken : $csrfToken
}, function(data) {
	document.getElementById("partners").innerHTML="";
	document.getElementById("partners").innerHTML = data;
	//setTokenValFrmAjaxResp();
	applyChosen();
});
	});
	$("#partners").change(function() {
		//alert(this.value)
		this.form.submit();
	});
});

function getPartnerBalance(balanceId){
	$.post("getAccBalanceByUserId.htm?balanceId="+balanceId, {
		partnerType : this.value
	}, function(data) {
		if(balanceId == 1){
			document.getElementById("partnerBalanceDiv").innerHTML="";
			document.getElementById("partnerBalanceDiv").innerHTML = data.toFixed(2);
		}else{
			document.getElementById("partnerBalanceDiv1").innerHTML="";
			document.getElementById("partnerBalanceDiv1").innerHTML = data.toFixed(2);
		}
		
		
	});
}

function getPartnerBalance1(){
	$.post("getAccBalanceByUserIdCommission.htm", {
		partnerType : this.value
		
	}, function(data) {

		document.getElementById("partnerBalanceDiv1").innerHTML="";
		document.getElementById("partnerBalanceDiv1").innerHTML = parseFloat(data.value).toFixed(2);
		
	});
}
</script>
</head>


<body>
<div id="pageWrpr">
    <div id="cntnt">
    	<!--Page inner Content-->
			<!--Page Right Content-->
				<!--Message area-->
				
				<!--Message Area Ends-->
	 </div>
                    </div>		
<form:form commandName="dashBoardDTO" name="searchDashBoardForm" method="post" id="searchDashBoardForm" autocomplete="off">
<div id="csrfToken">
<%@include file="csrf_token.jsp" %>
</div>
	<h4 style="font-size: 25px;">
		<span style="font-weight: bold"><spring:message code="LABEL_DASHBOARD_DAILY_TXN_COUNT" text="Daily Transaction count"/></span>
		</h4>
		
		 <div class="box">
	<div class="box-header">
	<%-- <h4 class="box-title">
	<spring:message code="LABEL_DASHBOARD" text="Transactions"/>
			<span><spring:message code="LABEL_SEARCH_TODAY_DATA" text="Today's Data"/></span>
			
	</h4> --%>
	
	
	<%-- <h3 class="box-title">
			<label class="col-sm-5">
			<span><spring:message code="LABEL_COUNT" text="Count"/></span></label> 
	</h3>  --%>
	
	
	</div> 
	<div class="box-body">
	<authz:authorize ifAnyGranted="ROLE_viewDashBoardBalanceAdminActivityAdmin">	
	<div class="row">
	<div class="col-sm-5">
			<label class="col-sm-6"><spring:message code="LABEL_PARTNER_BALANCE" text="Float Balance"/></label> 
	</div>
	<div class="col-sm-3" id="partnerBalanceDiv">
	<fmt:formatNumber type="number" maxFractionDigits="2" value="${floatBalance}"/>
	</div>
	<div class="col-sm-3">
	<img src="<%=request.getContextPath()%>/images/refresh-option.png" alt="logo" class="img_resize" onclick="javascript:getPartnerBalance('1');" style="cursor: pointer; width: 20px;"/>
			<%-- <input type="button" class="btn-default btn" value="<spring:message code="LABEL_REFRESH" text="Refresh"/>" onclick="javascript:getPartnerBalance();" /> --%> 
	</div>
	
</div>
<authz:authorize ifAnyGranted="ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3,ROLE_salesofficer,ROLE_bulkpaymentpartner">
<div class="row">
	<div class="col-sm-5">
			<label class="col-sm-6"><spring:message code="LABEL_PARTNER_COMMISSION_BALANCE" text="Commission Balance"/></label> 
	</div>
	<div class="col-sm-3" id="partnerBalanceDiv1">
	<fmt:formatNumber type="number" maxFractionDigits="2" value="${commissionBalance}"/>
	</div>
	<div class="col-sm-3">
	<img src="<%=request.getContextPath()%>/images/refresh-option.png" alt="logo" class="img_resize" onclick="javascript:getPartnerBalance('2');" style="cursor: pointer; width: 20px;"/>
			<%-- <input type="button" class="btn-default btn" value="<spring:message code="LABEL_REFRESH" text="Refresh"/>" onclick="javascript:getPartnerBalance();" /> --%> 
	</div>
	
</div>
</authz:authorize>
</authz:authorize>
<%-- <authz:authorize ifAnyGranted="ROLE_viewPartnerLevelAdminActivityAdmin">		
<div class="row">
	<div class="col-sm-3">
			<label class="col-sm-6"><spring:message code="LABEL_DASHBOARD_PARTNER_TYPE" text="Partner Type"/></label> 
	</div>
	<div class="col-sm-3">
	<form:select path="partnerType" id="partnerType">
		<form:option value="0">All</form:option>
		<form:option value="1">Partner L1</form:option>
		<form:option value="11">m-GURUSH Agent</form:option>
	<!-- <option value="2">Partner L2</option>
		<option value="3">Partner L3</option> -->
	</form:select>
	</div>
	<div class="col-sm-3" id="partnerlabel">
			<label class="col-sm-3"><spring:message code="LABEL_DASHBOARD_PARTNERS" text="Partners"/></label> 
	</div>
	<div class="col-sm-3">
		<form:select path="partnerId" id="partners">
			<option value="0">All</option>
			<form:options items="${partners}" itemLabel="name" itemValue="id"></form:options>
		</form:select>
	</div>
</div>
</authz:authorize>	 --%>
	<div class="row">
		<div class="col-sm-6">
		<h3 class="box-title">
			<label class="col-sm-12"><h4><spring:message code="LABEL_DASHBOARD_TRANSACTION_TYPE" text="Transaction Type"/></h4></label> 
		</h3>
		</div>
		
		<div class="col-sm-3">
		<h3 class="box-title">
			<label class="col-sm-12 box-title" style="margin-left: -21px;"><h4><spring:message code="LABEL_TODAYS_COUNT" text="Today's count"/></h4></label> 
		</h3>
		</div>
		<%-- <div class="col-sm-3">
		<h3 class="box-title">
			<label class="col-sm-12 box-title" style="margin-left: -21px;"><h4><spring:message code="YESTERDAYS_LABEL_COUNT" text="Yesterday's count"/></h4></label> 
		</h3>
		</div> --%>
	 </div>	
	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_BE" text="Balance Enquiry"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.balanceEnquiry}"/></label>			
			<%-- <label class="col-sm-5 col-sm-offset-3"><fmt:formatNumber type="number" value="${dashBoardDTO.balanceEnquiry}" /></label> --%>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_tsi_Upload}"/></label> --%>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_MS" text="Mini Statement"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.miniStatement}"/></label>
			<%-- <label class="col-sm-5 col-sm-offset-3"><fmt:formatNumber type="number" value="${dashBoardDTO.miniStatement}" /></label> --%>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_new_rtlr}"/></label> --%>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_CD" text="Cash Deposit"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9" ><c:out value="${dashBoardDTO.cashDeposit}"/></label>
			<%-- <label class="col-sm-5 col-sm-offset-3"><fmt:formatNumber type="number" value="${dashBoardDTO.cashDeposit}" /></label> --%>
			
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_extin_rtlr}"/></label> --%>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_CW" text="Cash Withdrawal"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cashWithdrawal}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.cashWithdrawal}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_new_rtlr_apprv}"/></label> --%>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_PAY" text="mGurush Pay"/></label> 
		</div>
		<div class="col-sm-3">
		<%-- <label class="col-sm-5"><c:out value="${dashBoardDTO.pay}"/></label> --%>
		<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.pay}" /></label>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_SM" text="Send Money"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><c:out value="${dashBoardDTO.sendMoney}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.sendMoney}" /></label>
		</div>
	</div>
	 
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_SALE" text="Sale"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.sale}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.sale}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_extin_rtlr_apprv}"/></label> --%>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_BP" text="Bill Payment"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.billPayment}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.billPayment}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_new_rtlr_rej}"/></label> --%>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_TOPUP" text="Buy Airtime"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.topup}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.topup}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.yday_extin_rtlr_rej}"/></label> --%>
		</div>
	 </div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_SMSCASH" text="Send SMS Cash"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><c:out value="${dashBoardDTO.smsCash}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.smsCash}" /></label>
		</div>
	</div>	
	
		
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_FLOAT_MGMT" text="Float Management"/></label> 
		</div>
		<div class="col-sm-3">
		<%-- <label class="col-sm-5"><c:out value="${dashBoardDTO.pay}"/></label> --%>
		<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" value="${dashBoardDTO.floatManagment}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_MERCHANT_PAYOUT" text="Merchant Payout"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.merchantPayout}"/></label>			
		</div>
		<div class="col-sm-3">
			
		</div>
	 </div>	
	 
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_CASH_IN" text="Cash In"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.cashIn}"/></label>			
		</div>
		<div class="col-sm-3">
			
		</div>
	 </div>	
	 
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_CASH_OUT" text="Cash Out"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.cashOut}"/></label>			
		</div>
		<div class="col-sm-3">
			
		</div>
	 </div>	
	 
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_TRFR_EMONEY" text="Transfer e-Money"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.transferEMoney}"/></label>			
		</div>
		<div class="col-sm-3">
			
		</div>
	 </div>	
	 
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_BULK_PAYMENT" text="Bulk Payment"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.bulkPayment}"/></label>			
		</div>
		<div class="col-sm-3">
			
		</div>
	 </div>	
	
				
	</div> 
	</div>
	
	<h4 style="font-size: 25px;">
		<span style="font-weight: bold"><spring:message code="LABEL_DASHBOARD_MOBILE_USER_STATISTICS" text="User Registration Statistics"/></span>
		</h4>
<div class="box">
	<div class="box-body">
	<div class="row">
	<div class="col-sm-12" >
				<b style="margin-left: 83px; font-size: medium;"><spring:message code="TODAYS_LABEL_COUNT" text="Today"/></b>
				<b style="margin-left: 400px; font-size: medium;"><spring:message code="LABEL_DASHBOARD_CUMULATIVE" text="Cumulative"/></b>
	</div>
	<%-- <div class="col-sm-3" style="margin-left: 683px; font-size: medium;">
				<b><spring:message code="LABEL_DASHBOARD_CUMULATIVE" text="Cumulative"/></b>
	</div> --%>
	</div>
	<div class="row">
	<div class="col-sm-2" style="margin-left: -23px;">
		<h3 class="box-title">
				<label class="col-sm-10"><h4><spring:message code="LABEL_DASHBOARD_USER_TYPE" text="User Type"/><h4></label>
		</h3>
	</div>	
	<div class="col-sm-2" style="margin-left: -23px;">
		<h3 class="box-title">
			<label class="col-sm-10 box-title" style="margin-left: 7px;"><h4><spring:message code="LABEL_DASHBOARD_TOTAL" text="Total"/></h4></label> 
		</h3> 
		</div>
	<div class="col-sm-2" style="margin-left: -23px;">
		<h3 class="box-title">
			<label class="col-sm-10 box-title" ><h4><spring:message code="LABEL_TODAYS_KYC_APPROVED" text="KYC Approved"/></h4></label> 
		</h3> 
		</div>
	<div class="col-sm-2" style="margin-left: -23px;">
		<h3 class="box-title">
			<label class="col-sm-10 box-title" style="margin-left: -21px;"><h4><spring:message code="LABEL_TODAYS_KYC_APPROVAL_PENDING" text="Pending"/></h4></label> 
		</h3> 
		</div>
	<div class="col-sm-2" style="margin-left: -23px;">
		<h3 class="box-title">
			<label class="col-sm-10 box-title" style="margin-left: -21px;"><h4><spring:message code="LABEL_TODAYS_KYC_PENDING" text="Pending"/></h4></label> 
		</h3> 
		</div>
	<div class="col-sm-2" style="margin-left: -23px;">
		<h3 class="box-title">
			<label class="col-sm-10 box-title" style="margin-left: -21px;"><h4><spring:message code="LABEL_TODAYS_KYC_REJECTED" text="Pending"/></h4></label> 
		</h3> 
		</div>
	<div class="col-sm-1" style="margin-left: -23px;">
		<h3 class="box-title">
			<label class="col-sm-10 box-title" style="margin-left: -14px;"><h4><spring:message code="LABEL_DASHBOARD_TOTAL" text="Total"/></h4></label> 
		</h3>
		</div>
	</div>
			
	<%-- <div class="row">
		<div class="col-sm-3">
			<label class="col-sm-15"><spring:message code="LABEL_DASHBOARD_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.agentSoleMerchant}"/></label>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" value="${musDashboardDTO.agentSoleMerchant}" /></label>
		</div>
		<div class="vl"></div>
		<div class="col-sm-2">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.activeAgentSoleMerchant}"/></label>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" value="${musDashboardDTO.activeAgentSoleMerchant}" /></label>
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.newAgentSoleMerchant}"/></label>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" value="${musDashboardDTO.newAgentSoleMerchant}" /></label>
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.totalAgentSoleMerchant}"/></label>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" value="${musDashboardDTO.totalAgentSoleMerchant}" /></label>
		</div>
	 </div>	 --%>
			
	<div class="row">
		<div class="col-sm-2">
			<label class="col-sm-15"><spring:message code="LABEL_DASHBOARD_AGENT" text="Agent"/></label> 
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15"><fmt:formatNumber type="number" value="${musDashboardDTO.agent}" /></label>
		</div>
		<div class="vl"></div>
		<div class="col-sm-2" style="margin-left: -30px;" >
			<label class="col-sm-15"><fmt:formatNumber type="number" value="${musDashboardDTO.agentKycApproved}" /></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><fmt:formatNumber type="number" value="${musDashboardDTO.agentKycApprovalPending}" /></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15 "><fmt:formatNumber type="number" value="${musDashboardDTO.agentKycPending}" /></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15 "><fmt:formatNumber type="number" value="${musDashboardDTO.agentKycRejected}" /></label>
		</div>
		<div class="col-sm-2" style="margin-right: -75px;">
			<label class="col-sm-15 "><fmt:formatNumber type="number" value="${musDashboardDTO.totalAgent}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-2">
			<label class="col-sm-15"><spring:message code="LABEL_DASHBOARD_SOLE_MERCHANT" text="Merchant"/></label> 
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.soleMerchant}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.merchantKycApproved}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.merchantKycApprovalPending}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.merchantKycPending}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.merchantKycRejected}"/></label>
		</div>
		<div class="col-sm-2" style="margin-right: -75px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.totalSoleMerchant}"/></label>
		</div>
		
	</div>	
	
	<div class="row">
		<div class="col-sm-2">
			<label class="col-sm-15"><spring:message code="LABEL_DASHBOARD_REG_CUSTOMER" text="Customers"/></label> 
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.registeredCustomer}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.kycApproved}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.kycApprovalPending}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.kycPending}"/></label>
		</div>
		<div class="col-sm-2" style="margin-left: -30px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.kycRejected}"/></label>
		</div>
		<div class="col-sm-2" style="margin-right: -75px;">
			<label class="col-sm-15"><c:out value="${musDashboardDTO.totalRegisteredCustomer}"/></label>
		</div>
	</div>	
	<%-- <div class="row">
		<div class="col-sm-3">
			<label class="col-sm-15"><spring:message code="LABEL_DASHBOARD_SIGN_UP_CUSTOMER" text="Sign-Up Customer"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-15 col-sm-offset-10"><c:out value="${musDashboardDTO.signupCustomer}"/></label>
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15 col-sm-offset-10"><c:out value="${musDashboardDTO.activeSignUpCustomer}"/></label>
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15 col-sm-offset-10"><c:out value="${musDashboardDTO.newSignUpCustomer}"/></label>
		</div>
		<div class="col-sm-2">
			<label class="col-sm-15 col-sm-offset-10"><c:out value="${musDashboardDTO.totalSignupCustomerr}"/></label>
		</div>
	</div>	 --%>
	</div>
	</div> 
</form:form>
</body>
</html>


