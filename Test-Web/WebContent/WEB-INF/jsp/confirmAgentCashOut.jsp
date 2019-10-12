<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>

<!-- Loading Calendar JavaScript files -->
<script type='text/javascript'
	src='https://code.jquery.com/jquery-2.1.1.min.js'></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>

<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	n font-weight: bold;
}

-->
.add_cus {
	text-align: right;
	margin-right: 20px;
}

textarea#message {
  resize: none;
}

</style>


<script>

	var Alertmsg={
			
			"fromDateFormat":"<spring:message code="VALID_VALID_ACCOUNT_REPORT_FROM_DATE_FORMAT" text="From date format must be : dd/mm/yyyy"/>",
			 "toDateFromat":"<spring:message code="VALID_ACCOUNT_REPORT_TO_DATE_FORMAT" text="To date format must be : dd/mm/yyyy"/>",
			 "validFromDay":"<spring:message code="VALID_DAY" text="Please enter valid from date"/>",
			 "validToDay":"<spring:message code="VALID_TO_DAY" text="Please  enter  a valid to date"/>",
			 "validFromMonth":"<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
			 "validToMonth":"<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
			 "validfDay":"<spring:message code="VALID_FROM_DAY" text="Please  enter  a valid  days in from date"/>",
			 "validTodays":"<spring:message code="VALID_TO_DAYS" text="Please  enter  a valid  days in to date"/>",
			 "validFMonth":"<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
			 };    
	function check(){	
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "fromDate",
        button            : "trigger",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      }); 
	 
  	}

 	function check1(){	
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "toDate",
        button            : "trigger1",
        ifFormat          : "%d/%m/%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval          : 01
      });
	 
  	}
	
    function validate(){
    	
    	
        var otp = document.getElementById("otp").value;
        var pattern='^\[0-9]*$';

        if(otp!="" && otp.search(pattern)==-1) {
	         alert('Please enter Valid OTP');
	         document.agentCashOutConfirmForm.otp.focus();
	         return false;
		 }

			document.agentCashOutConfirmForm.action = "processTransaction.htm";
			document.agentCashOutConfirmForm.submit();
        }

	</script>
</head>
<body onload="check(),check1();">
	<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  added commandName=customerDTO for customerDTO submision -->
	<form:form id="agentCashOutConfirmForm" name="agentCashOutConfirmForm" class="form-inline"
		commandName="transactionParamDTO" method="post">
		<jsp:include page="csrf_token.jsp" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title"><span><spring:message code="LABEL_AGENT_CASHOUT_CONFIRM" text=" Agent CashOut Confirm"/></span></h3>
				</div>
				<div class="col-sm-6">
					<div class="col-sm-6 col-sm-offset-9"
						style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
				<br /> <br />
				<div class="box-body">
				<form:hidden path="transactionId" />
				<authz:authorize ifAnyGranted="ROLE_businesspartnerL2">
				<form:hidden path="transactionType" value="${transactionParamDTO.transactionType}"/>
				<form:hidden path="transactionId" value="${transactionParamDTO.transactionId}"/>
				<div class="row">
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_AGENT_NAME" text="Agent Name"/></label>
						<div class="col-sm-5">
							<c:out value="${transactionParamDTO.name} " />
						</div>						
					</div> 
 
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_AGENT_MO" text="Agent Mobile Number"/></label>
						<div class="col-sm-5">
							<c:out value="${transactionParamDTO.customerMobileNo} " />
						</div>
					</div> 
				</div>
				<div class="row">
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_AGENT_CODE" text="Agent Code"/></label>
						<div class="col-sm-5">
							<c:out value="${transactionParamDTO.customerCode} " />
						</div>
					</div> 
 
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_AMOUNT" text="Amount"/></label>
						<div class="col-sm-5">
							<c:out value="${transactionParamDTO.amount} " />
						</div>
					</div> 
				</div>
				<div class="row">
					<div class="col-sm-6">
							<label class="col-sm-5"><spring:message
										code="LABEL_OTPSTATUS" text="OTP" /><font color="red"> *</font></label>
							<form:input path="otp" maxlength="9" id="otp" name="otp"
									cssClass="form-control"></form:input>	
					</div> 
				</div>
			<br/>
			<div class="col-md-3 col-md-offset-10">
				<input type="button" id="button" class="btn-primary btn" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="validate();" />
			</div>
			</authz:authorize>
			<br/>
				</div>
			</div>
		</div>

	</form:form>
</body>
</html>
