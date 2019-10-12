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
    left: 50%;
    margin-left: -96px;
    top: 0;
    height: 254px;
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
			document.getElementById("partnerBalanceDiv").innerHTML = data.toFixed(2) +" SSP";
		}else{
			document.getElementById("partnerBalanceDiv1").innerHTML="";
			document.getElementById("partnerBalanceDiv1").innerHTML = data.toFixed(2) +" SSP";
		}
		
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
		<span style="font-weight: bold;"><spring:message code="LABEL_DASHBOARD_DAILY_TXN_COUNT" text="BulkPayment Partner"/></span>
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
	<%-- <authz:authorize ifAnyGranted="ROLE_viewDashBoardBalanceAdminActivityAdmin"> --%>	
	<div class="row">
	<div class="col-sm-5">
			<%-- <label class="col-sm-6" style="font-size: 22px;"><spring:message code="LABEL_PARTNER_BALANCE" text="Balance"/></label>  --%>
			<label class="col-sm-6"><spring:message code="LABEL_PARTNER_BALANCE" text="Float Balance"/></label>
	</div>
	<div class="col-sm-3" id="partnerBalanceDiv" style="font-size: 22px;">
	<fmt:formatNumber  type="number" maxFractionDigits="2" value="${floatBalance}"/> SSP
	</div>
	<div class="col-sm-3">
	<img src="<%=request.getContextPath()%>/images/refresh-option.png" alt="logo" class="img_resize" onclick="javascript:getPartnerBalance('1');" style="cursor: pointer; width: 20px;"/>
			<%-- <input type="button" class="btn-default btn" value="<spring:message code="LABEL_REFRESH" text="Refresh"/>" onclick="javascript:getPartnerBalance();" /> --%> 
	</div>
    </div>
     <div class="row">
	<div class="col-sm-5">
			<label class="col-sm-6"><spring:message code="LABEL_PARTNER_COMMISSION_BALANCE" text="Commission Balance"/></label> 
	</div>
	<div class="col-sm-3" id="partnerBalanceDiv1" style="font-size: 22px;">
	<fmt:formatNumber type="number" maxFractionDigits="2" value="${commissionBalance}"/> SSP
	</div>
	<div class="col-sm-3">
	<img src="<%=request.getContextPath()%>/images/refresh-option.png" alt="logo" class="img_resize" onclick="javascript:getPartnerBalance('2');" style="cursor: pointer; width: 20px;"/>
			<%-- <input type="button" class="btn-default btn" value="<spring:message code="LABEL_REFRESH" text="Refresh"/>" onclick="javascript:getPartnerBalance();" /> --%> 
	</div>
</div> 
	      </div> 
	        </div>
</form:form>
</body>
</html>


