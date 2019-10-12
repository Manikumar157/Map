<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<%-- <title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title> --%>
<%-- 
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script> --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bankManagement.js"></script>


<!-- Loading language definition file -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG" />.js"></script>


<script type="text/javascript">
	function validate() {

		var gimCharge = document.getElementById("gimShare").value;
		var issuerBankCharge = document.getElementById("issuerBank").value;
		var acquirerBankScharge = document.getElementById("aquirerBank").value;
		var gimBanksSCCharge = parseFloat(gimCharge)
				+ parseFloat(issuerBankCharge)
				+ parseFloat(acquirerBankScharge);
		var numericPattern=  /^\d{0,3}(\.\d{1,2})?$/gm;

		if (gimCharge == "") {
			alert('<spring:message code="LABEL_GIM_SHARE_PERCENTAGE" text="Please enter GIM share"/>');
			return false;
		} else if (gimCharge.charAt(0) == " "
				|| gimCharge.charAt(gimCharge.length - 1) == " ") {
			alert('<spring:message code="LABEL_GIM_SERICE_CHARGE_DIGITS" text="Please enter a valid Service Charge for GIM"/>');
			return false;
		} else if (issuerBankCharge == "") {
			alert('<spring:message code="LABEL_ISSUER_BANK_SHARE" text="Please enter a Issuer Bank share "/>');
			return false;
		} else if (issuerBankCharge.charAt(0) == " "
				|| issuerBankCharge.charAt(issuerBankCharge.length - 1) == " ") {
			alert('<spring:message code="LABEL_ISSUER_BANK_DIGITS" text="Please enter a valid Issuer Bank share"/>');
			return false;
		} else if (isNaN(gimCharge) || parseFloat(gimCharge) < 0) {
			alert('<spring:message code="LABEL_GIM_SERICE_CHARGE_DIGITS" text="Please enter a valid  GIM Share "/>');
			return false;
		} else if (isNaN(issuerBankCharge) || parseFloat(issuerBankCharge) < 0) {
			alert('<spring:message code="LABEL_ISSUER_BANK_DIGITS" text="Please enter a valid Share value for issuer bank"/>');
			return false;
		} else if (acquirerBankScharge == "") {
			alert('<spring:message code="LABEL_ACQUIRER_BANK_SHARE" text="Please enter a valid acquirer Bank Share"/>');
			return false;
		} else if (acquirerBankScharge.charAt(0) == " "
				|| acquirerBankScharge.charAt(acquirerBankScharge.length - 1) == " ") {
			alert('<spring:message code="LABEL_ACQUIRER_BANK_DIGITS" text="Please enter a valid Share value for acquirer bank"/>');
			return false;
		} else if (isNaN(acquirerBankScharge)
				|| parseFloat(acquirerBankScharge) < 0) {
			alert('<spring:message code="LABEL_ACQUIRER_BANK_DIGITS" text="Please enter a valid Share value for acquirer bank"/>');
			return false;
		} else if (gimBanksSCCharge != 100) {
			alert('<spring:message code="LABEL_GIM_BANK_CHARGE" text="The sum of Bank service charge and GIM service charge should be 100%"/>');
			return false;
		}
  
	     else if (gimCharge.search(numericPattern) == -1
				|| (gimCharge.charAt(0) == '.')) {

			alert("<spring:message code='LABEL_GIM_SHARE_NUMBER_VALID' text='Please enter Partner Share'/>");
			return false;
		}

		else if (issuerBankCharge.search(numericPattern) == -1
				|| (issuerBankCharge.charAt(0) == '.')) {

			alert("<spring:message code='LABEL_ISSUER_BANK_SHARE_NUMBER_VALID' text='Please enter valid issuer bank share'/>");
			return false;
		} else if (acquirerBankScharge.search(numericPattern) == -1
				|| (acquirerBankScharge.charAt(0) == '.')) {

			alert('<spring:message code="LABEL_ACQUIRER_BANK_DIGITS" text="Please enter a valid Share value for acquirer bank"/>');
			return false;
		}

		document.interBankFeeForm.submit();

	}
</script>

</head>
<body>
	<div class="col-lg-12">
		<div class="box" style="height:350px;">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_INTERBANK_FEE"
							text="Inter Bank Fee" /></span>
				</h3>
			</div>
				<div class="col-sm-6 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
			<div class="box-body">
			<div class="col-sm-6 col-md-offset-3 table_border" style="/* border: 1px solid; *//* border-radius: 15px; */"><br />
				<form:form class="form-inline" name="interBankFeeForm"
					action="saveInterBankFee.htm" method="post"
					commandName="interBankFeeDTO">
					<jsp:include page="csrf_token.jsp"/>
					<div class="row">
						<div class="col-sm-12">
							<label class="col-sm-6" style="margin-top: 4px;"> <spring:message
									code="LABEL_GIM_SHARE" text="GIM Share"></spring:message><font
								color="red">*</font>
							</label>
							<form:input path="gimShare" cssClass="form-control" maxlength="6"/>
							<FONT color="red"><form:errors path="gimShare" /></FONT>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<label class="col-sm-6" style="margin-top: 4px;"> <spring:message
									code="ISSUER_BANK_SHARE" text="Issuer Bank Share"></spring:message>
								<font color="red">*</font></label>
							<form:input path="issuerBank" cssClass="form-control" maxlength="6"/>
							<FONT color="red"><form:errors path="issuerBank" /></FONT>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12">
							<label class="col-sm-6" style="margin-top: 4px;"> <spring:message
									code="ACQUIRER_BANK_SHARE" text="Acquirer Bank Share"></spring:message><font
								color="red">*</font></label>
							<form:input path="aquirerBank" cssClass="form-control" maxlength="6"/>
							<FONT color="red"><form:errors path="aquirerBank" /></FONT>
						</div>
					</div>
					<authz:authorize
						ifAnyGranted="ROLE_addInterBankFeeAdminActivityAdmin">
						<div class="box-footer">
							<input type="button" value="<spring:message code="LABEL_SAVE"/>"
								onclick="validate();" class="btn btn-primary col-md-offset-9" />
						</div>
					</authz:authorize>
				</form:form>
				</div>
			</div>
		</div>

	</div>
</body>
</html>
