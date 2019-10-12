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
<form:form commandName="dashBoardDTO" name="txnSummaryDashBoard" method="post" id="txnSummaryDashBoard" autocomplete="off">
<div id="csrfToken">
<%@include file="csrf_token.jsp" %>
</div>
	<h4 style="font-size: 25px;">
		<span style="font-weight: bold"><spring:message code="LABEL_DASHBOARD_TXN_AMOUNT_SUMMARY" text="Transaction Amount Summary"/></span>
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
	<!--<authz:authorize ifAnyGranted="ROLE_viewPartnerLevelAdminActivityAdmin">	
	<div class="row">
	<div class="col-sm-3">
			<label class="col-sm-6"><spring:message code="LABEL_DASHBOARD_PARTNER_TYPE" text="Partner Type"/></label> 
	</div>
	<div class="col-sm-3">
	<form:select path="partnerType" id="partnerType">
	<option value="0">All</option>
	<form:option value="1">Partner L1</form:option>
	<form:option value="11">m-GURUSH Agent</form:option>
	
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
	</authz:authorize> -->
	<div class="row">
		<div class="col-sm-6">
		<h3 class="box-title">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_TRANSACTION_TYPE" text="Transaction Type"/></label> 
		</h3>
		</div>
		
		<div class="col-sm-3">
		<h3 class="box-title">
			<label class="col-sm-12 box-title" style="margin-left: -21px;"><h4><spring:message code="TODAYS_LABEL_COUNT" text="Today's count"/></h4></label> 
		</h3>
		</div>
		<div class="col-sm-3">
		<h3 class="box-title">
			<label class="col-sm-12 box-title" style="margin-left: -21px;"><h4><spring:message code="CUMMULATIVE_LABEL_COUNT" text="Cumulative"/></h4></label> 
		</h3>
		</div>
	 </div>	
	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_BE" text="Balance Enquiry"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-15 col-sm-offset-9"><c:out value="${dashBoardDTO.balanceEnquiry}"/></label> --%>			
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.balanceEnquiry}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumBalanceEnquiry}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumBalanceEnquiry}" /></label>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_MS" text="Mini Statement"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.miniStatement}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.miniStatement}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumMiniStatement}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumMiniStatement}" /></label>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_CD" text="Cash Deposit"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cashDeposit}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cashDeposit}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumCashDeposit}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumCashDeposit}" /></label>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_CW" text="Cash Withdraw"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cashWithdrawal}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cashWithdrawal}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumCashWithdrawal}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumCashWithdrawal}" /></label>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_SALE" text="Sale"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.sale}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.sale}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumSale}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumSale}" /></label>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_BP" text="Bill Payment"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.billPayment}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.billPayment}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumBillPayment}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumBillPayment}" /></label>
		</div>
	 </div>	
	 <div class="row">
		<div class="col-sm-6">
			<label class="col-sm-12"><spring:message code="LABEL_DASHBOARD_TOPUP" text="Buy Airtime"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.topup}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.topup}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumTopUp}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumTopUp}" /></label>
		</div>
	 </div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_SM" text="Send Money"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.sendMoney}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.sendMoney}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumSendMoney}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumSendMoney}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_SMSCASH" text="Send SMS Cash"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.smsCash}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.smsCash}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumSmsCash}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumSmsCash}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_PAY" text="m-GURUSH Pay"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.pay}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumPay}" /></label>
		</div>
	</div>	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_FLOAT_MGMT" text="Float Management"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.floatManagment}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumFloatManagment}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_MERCHANT_PAYOUT" text="Merchant Payout"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.merchantPayout}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumMerchantPayout}" /></label>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_CASH_IN" text="Cash In"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cashIn}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumCashIn}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_CASH_OUT" text="Cash Out"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cashOut}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumCashOut}" /></label>
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_TRFR_EMONEY" text="Transfer e-money"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.transferEMoney}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumTransferEMoney}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_BULK_PAYMENT" text="Bulk Pay"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.pay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.bulkPayment}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-3"><c:out value="${dashBoardDTO.cumPay}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-9"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${dashBoardDTO.cumBulkPayment}" /></label>
		</div>
	</div>	
				
	</div> 
	</div>
	
	<h4 style="font-size: 25px;">
		<span style="font-weight: bold"><spring:message code="LABEL_DASHBOARD_COMMISSION_SHARE" text="Commission Share"/></span>
		</h4>
<div class="box">
	<div class="box-body">
	<div class="row">
	<div class="col-sm-6">
		<h3 class="box-title">
				<label class="col-sm-12"><h4><spring:message code="LABEL_DASHBOARD_ENTITY" text="Entity"/></h4></label>
		</h3>
	</div>	
	<div class="col-sm-3">
		<h3 class="box-title">
			<label class="col-sm-12 box-title" style="margin-left: -21px;"><h4><spring:message code="TODAYS_LABEL_TODAYS_SHARE" text="Today's share"/></h4></label> 
		</h3> 
		</div>
	<div class="col-sm-3">
		<h3 class="box-title">
			<label class="col-sm-12 box-title" style="margin-left: -21px;"><h4><spring:message code="LABEL_DASHBOARD_CUMALATIVE" text="Cumulative"/></h4></label> 
		</h3>
		</div>
	</div>
			
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_MGURUSH" text="m-GURUSH"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##" maxFractionDigits="2" value="${comissionShareDashBoardDTO.mgurushCommission}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.mgurushCommission}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##" maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumMgurushCommission}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumMgurushCommission}" /></label>
		</div>
	 </div>	
			
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_AGENT" text="Agent"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##"  maxFractionDigits="2" value="${comissionShareDashBoardDTO.agentCommission}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.agentCommission}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##"  maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumAgentCommission}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumAgentCommission}" /></label>
		</div>
	</div>	
	
	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_SOLE_MERCHANT" text="Merchant"/></label> 
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##"  maxFractionDigits="2" value="${comissionShareDashBoardDTO.soleMerchantCommission}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.soleMerchantCommission}" /></label>
		</div>
		<div class="col-sm-3">
			<%-- <label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##"  maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumSoleMerchantCommssion}"/></label> --%>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumSoleMerchantCommssion}" /></label>
		</div>
		
	</div>	
	
<%-- 	<div class="row">
		<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_DASHBOARD_ASM" text="Agent Sole Merchant"/></label> 
		</div>
		<div class="col-sm-3">
			<label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##"  maxFractionDigits="2" value="${comissionShareDashBoardDTO.agentSoleMerchantCommssion}"/></label>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.agentSoleMerchantCommssion}" /></label>
		</div>
		<div class="col-sm-3">
			<label class="col-sm-5"><fmt:formatNumber type="number" pattern="##.##"  maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumAgentSoleMerchantCommssion}"/></label>
			<label class="col-sm-15 col-sm-offset-10"><fmt:formatNumber type="number" minFractionDigits = "2" maxFractionDigits="2" value="${comissionShareDashBoardDTO.cumAgentSoleMerchantCommssion}" /></label>
		</div>
	</div> --%>	
	</div>
	</div> 
</form:form>
</body>
</html>


