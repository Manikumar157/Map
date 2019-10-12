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

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>

<script type="text/javascript">
	var Alertmsg = {
		"chname" : "<spring:message code="NotEmpty.clearingHouseDTO.clearingHouseName" text="Please enter ClearingHouseName"/>",
		"chnameSpace" : "<spring:message code="LABEL.CHNAME.SPACE" text="Please remove unwanted spaces in Clearing House Name"/>",
		"valid" : "<spring:message code="LABEL.NOTEMPTY" text="Please enter valid Settlement Interval"/>",
		"hours" : "<spring:message code="NotNull.clearingHouseDTO.settlementIntervalHours" text="Please enter valid Settlement Interval in Hours"/>",
		"minutes" : "<spring:message code="NotNull.clearingHouseDTO.settlementIntervalMins" text="Please enter valid Settlement Interval in Minutes"/>",
		"cutoffhours" : "<spring:message code="NotNull.clearingHouseDTO.settlementIntervalHo" text="Please enter valid Cut Off Time in Hours"/>",
		"cutoffminutes" : "<spring:message code="NotNull.clearingHouseDTO.settlementInterva" text="Please enter valid Cut Off Time in Minutes"/>",
		"cutoffvalid" : "<spring:message code="LABEL.NOTE" text="Please enter valid Cut Off Time"/>",
		"currency" : "<spring:message code="NotNull.clearingHouseDTO.currency" text="Please select Currency"/>",
		"chDigits" : "<spring:message code="LABEL.CHNAME.DIGITS" text="Please enter Clearing House Name with out any special characters"/>",
		"chLength" : "<spring:message code="LABEL.CHNAME.LENGTH" text="Clearing House Name should not exceed more than 32 characters"/>",
		"descLength" : "<spring:message code="LABEL.DESCRIPTION.LENGTH" text="Description should not exceed more than 180 characters"/>",
        "messageType":"please enter Message Type",
        "chnameValid":"Please Enter valid data in Clearing House Name",
        "messageTypeValid":"Please Enter valid data in Message Type",
        "messageSendValid":"Please Enter valid data in Message Sender",
	};

	function validate() {
		var chname = document.getElementById("clearingHouseName").value;
		var currency = document.getElementById("currency").value;
		var banks = document.getElementById("banks").value;
		var settlementHours = document
				.getElementById("settlementIntervalHours").value;
		var settlementMinutes = document
				.getElementById("settlementIntervalMins").value;
		var startupHours = document.getElementById("startUpTimeHours").value;
		var startupMinutes = document.getElementById("startUpTimeMinutes").value;

		var settlementIntervalHours = document
				.getElementById("settlementIntervalHours").value;
		// vineeth change
		var mobileLength = document.getElementById("mobileNumberLength").value;
		// vineeth chnage over
		var settlementAccount = document.getElementById("settlementAccount").value;
		var guaranteeAccount = document.getElementById("guaranteeAccount").value;
		var centralBankAccount = document.getElementById("centralBankAccount").value;
		var EOTSwiftCode = document.getElementById("EOTSwiftCode").value;
		var contactPerson = document.getElementById("contactPerson").value;
		var mobileNumber = document.getElementById("mobileNumber").value;
		var emailID = document.getElementById("emailID").value;
		var description = document.getElementById("description").value;
		var messageType = document.getElementById("messageType").value;
		var messageSender = document.getElementById("messageSender").value;

		var pattern = '^\[a-zA-ZÀ-ÿ0-9-\' ]*$';

		var accountPattern = '^\[a-zA-ZÀ-ÿ0-9-\]*$';
		var timePattern = '^\[0-9]*$';
		var numPattern = '^[0-9]*$';

	
	if (chname == "") {
			alert(Alertmsg.chname);
			return false;
		} else if (chname.search(numPattern) != -1) {
			alert(Alertmsg.chnameValid);
			return false;
		}

		else if (chname.charAt(0) == "0") {
			alert('<spring:message code="LABEL.CHPOOL.VALID" text="Please enter a valid Clearing House Name"/>');
			return false;
		} else if (chname.charAt(0) == " "
				|| chname.charAt(chname.length - 1) == " ") {
			alert(Alertmsg.chnameSpace);
			return false;
		} else if (chname.search(accountPattern) == -1) {
			alert(Alertmsg.chDigits);
			return false;
		} else if (chname.length > 32) {
			alert(Alertmsg.chLength);
			return false;
		}
		if (settlementHours == "") {
			alert(Alertmsg.hours);
			return false;
		} else if (settlementMinutes == "") {
			alert(Alertmsg.minutes);
			return false;
		} else if (settlementHours.search(timePattern) == -1) {
			alert(Alertmsg.valid);
			return false;
		} else if (settlementMinutes.search(timePattern) == -1) {
			alert(Alertmsg.valid);
			return false;
		} else if (settlementHours == 00 && settlementMinutes == 00) {
			alert(Alertmsg.valid);
			return false;
		} else if (settlementHours > 24 || settlementMinutes > 59) {
			alert(Alertmsg.valid);
			return false;
		} else if (settlementHours == 24 && settlementMinutes > 0) {
			alert(Alertmsg.valid);
			return false;
		}
		if (startupHours == "") {
			alert(Alertmsg.cutoffhours);
			return false;
		} else if (startupMinutes == "") {
			alert(Alertmsg.cutoffminutes);
			return false;
		} else if (startupHours.search(timePattern) == -1) {
			alert(Alertmsg.cutoffvalid);
			return false;
		} else if (startupMinutes.search(timePattern) == -1) {
			alert(Alertmsg.cutoffvalid);
			return false;
		} else if (startupHours > 24 || startupMinutes > 59) {
			alert(Alertmsg.cutoffvalid);
			return false;
		} else if (startupHours == 24 && startupMinutes > 0) {
			alert(Alertmsg.cutoffvalid);
			return false;
		} else if (description.charAt(0) == " "
				|| description.charAt(description.length - 1) == " ") {
			alert('<spring:message code="LABEL.DESCRIPTION.BLANK" text="Please remove the white space from desription"/>');
			return false;
		} else if (description.length > 180) {
			alert(Alertmsg.descLength);
			return false;
		}
		if (currency == "") {
			alert(Alertmsg.currency);
			return false;
		} else if (contactPerson == "") {
			alert('<spring:message code="NotEmpty.clearingHouseDTO.contactPerson" text="Please enter Contact Person Name  "/>');
			return false;
		} else if (messageType == "") {
			alert(Alertmsg.messageType);
			return false;
		}

		else if (messageType.search(numPattern) != -1) {
			alert(Alertmsg.messageTypeValid);
			return false;
		}

		else if (messageType.search(accountPattern) == -1 || messageType == "") {
			alert('<spring:message code="VALIDATION_MESSAGE_TYPE" text="Please enter Valid Message Type in alphanumeric "/>');
			return false;
		}

		else if (!checkForallZeros(messageType)) {
			alert('<spring:message code="VALIDATION_MESSAGE_TYPE" text="Please enter Valid Message Type in alphanumeric "/>');
			return false;
		} else if (contactPerson.search(pattern) == -1) {
			alert('<spring:message code="VALIDATION_CONT_PERSON_CHPOOL" text="Please enter valid Contact Person Name  "/>');
			return false;
		} else if (!checkForallZeros(contactPerson)) {
			alert('<spring:message code="VALIDATION_CONT_PERSON_CHPOOL" text="Please enter valid Contact Person Name  "/>');
			return false;
		} else if (contactPerson.search(numPattern) != -1) {
			alert('<spring:message code="VALIDATION_CONT_PERSON_CHPOOL" text="Please enter valid Contact Person Name  "/>');
			return false;
		} else if (mobileNumber == "") {
			alert('<spring:message code="NotEmpty.clearingHouseDTO.mobileNumber" text="Please enter Mobile Number  "/>');
			return false;
		} else if (mobileNumber.search(numPattern) == -1) {
			alert('<spring:message code="VALIDATION_MOBILE_ACCOUNT" text="Please enter valid Mobile Number in numeric"/>');
			return false;
		} else if (!checkForallZeros(mobileNumber)) {
			alert('<spring:message code="VALIDATION_VALID_MOBILE_ACCOUNT" text="Please enter valid Mobile Number "/>');
			return false;
		} 
		// vineeth changes, fetching mobile nnumber length based on country dynamically, on 07-08-2018
		 else if (mobileNumber.length < 6 || mobileNumber.length > 10) {
			alert("Please enter valid Mobile Number of Length");
			return false;
		} 
		/* 
		else if (mobileNumber.length != "") {
			if (mobileNumber.length != mobileLength) {
			alert("Please enter valid Mobile Number of Length "+mobileLength);
			return false;
		}} */
		// vineeth changes over
		else if (emailID == "") {
			alert('<spring:message code="VALIDATION_EMAIL" text="Please enter Email ID "/>');
			return false;
		} else if (checkEmail(emailID) == false) {
			alert('<spring:message code="VALIDATION_CLPOOL_EMAIL" text="Please enter valid Email ID  "/>');
			return false;
		} else if (settlementAccount == "") {
			alert('<spring:message code="NotEmpty.clearingHouseDTO.settlementAccount" text="Please enter Settlement Account"/>');
			return false;
		} else if (settlementAccount.search(numPattern) == -1) {
			alert('<spring:message code="VALIDATION_SETTLEMENT_ACCOUNT" text="Please enter Valid Settlement Account"/>');
			return false;
		} else if (!checkAllZero(settlementAccount)) {
			alert('<spring:message code="VALIDATION_SETTLEMENT_ACCOUNT" text="Please enter Settlement Account in alphanumeric"/>');
			return false;
		} else if (guaranteeAccount == "") {
			alert('<spring:message code="NotEmpty.clearingHouseDTO.guaranteeAccount" text="Please enter Guarantee Account "/>');
			return false;
		} else if (guaranteeAccount.search(numPattern) == -1) {
			alert('<spring:message code="VALIDATION_GUARANTEE_ACCOUNT" text="Please enter Valid Guarantee Account"/>');
			return false;
		} else if (!checkAllZero(guaranteeAccount)) {
			alert('<spring:message code="VALIDATION_GUARANTEE_ACCOUNT" text="Please enter Valid Guarantee Account in alphanumeric"/>');
			return false;
		} else if (centralBankAccount == "") {
			alert('<spring:message code="NotEmpty.clearingHouseDTO.centralBankAccount" text="Please enter CentralBank Account "/>');
			return false;
		} else if (centralBankAccount.search(numPattern) == -1) {
			alert('<spring:message code="VALIDATION_CENTRAL_BANK_ACCOUNT" text="Please enter Valid CentralBank Account"/>');
			return false;
		} else if (!checkAllZero(centralBankAccount)) {
			alert('<spring:message code="VALIDATION_CENTRAL_BANK_ACCOUNT" text="Please enter Valid CentralBank Account in alphanumeric"/>');
			return false;
		} else if (settlementAccount == guaranteeAccount) {
			alert('<spring:message code="VALIDATION_SAME_SETTLEMENT_GUARENTEE_ACCOUNT" text="Settlement Account number and Guarantee Account number should not be same"/>');
			return false;
		} else if (settlementAccount == centralBankAccount) {
			alert('<spring:message code="VALIDATION_SAME_SETTLEMENT_GUARANTEE_ACCOUNT" text="Settlement account number and Central bank Account number should not be same"/>');
			return false;
		} else if (guaranteeAccount == centralBankAccount) {
			alert('<spring:message code="VALIDATION_SAME_GUARANTEE_CENTRALBANK_ACCOUNT" text="guarantee account number and Central bank Account number should not be same"/>');
			return false;
		} else if (EOTSwiftCode == "") {
			alert('<spring:message code="NotEmpty.clearingHouseDTO.GIMUEMOASwiftCode" text="Please enter GIM-UEMOA-SwiftCode "/>');
			return false;
		} else if (EOTSwiftCode.search(accountPattern) == -1) {
			alert('<spring:message code="VALIDATION_GIMUEMOASWIFT_ACCOUNT" text="Please enter Valid GIMUEMOASwiftCode in alphanumeric "/>');
			return false;
		} else if (!checkAllZero(EOTSwiftCode)) {
			alert('<spring:message code="VALIDATION_GIMUEMOASWIFT_ACCOUNT" text="Please enter Valid GIMUEMOASwiftCode in alphanumeric "/>');
			return false;
		} else if (messageSender == "") {
			alert('<spring:message code="VALIDATION_MESSAGE_SENDER" text="Please enter Message sender "/>');
			return false;
		} else if (messageSender.search(numPattern) != -1) {
			alert(Alertmsg.messageSendValid);
			return false;
		}

		else if (messageSender.search(accountPattern) == -1) {
			alert('<spring:message code="VALIDATION_MESSAGE_SENDER_NUMERIC" text="Please enter Valid Message sender in alphanumeric "/>');
			return false;
		}

		else if (!checkAllZero(messageSender)) {
			alert('<spring:message code="VALIDATION_MESSAGE_SENDER_NUMERIC" text="Please enter Valid Message sender in alphanumeric "/>');
			return false;
		} else {
			document.clearingPool.action = "saveClearanceHouse.htm";
			document.clearingPool.submit();
		}
	}
	function disableForm(formID) {
		alert("disable form - " + formID);
		$("#target :input").attr("disabled", 'true');
	}

	function textCounter(field, cntfield, maxlimit) {
		if (field.value.length > maxlimit) // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
		// otherwise, update 'characters left' counter
		else
			cntfield.value = maxlimit - field.value.length;
	}

	function checkForallZeros(value) {

		var count = 0;
		for (var i = 0; i < value.length; i++) {

			if (value.charAt(i) == 0) {
				count++;
			}
		}
		if (count == value.length) {
			return false;
		} else {
			return true;
		}

	}
	function checkAllZero(value) {

		var count = 0;
		for (var i = 0; i < value.length; i++) {

			if (value.charAt(i) == 0) {
				count++;
			}
		}
		if (count == value.length) {
			return false;
		} else {
			return true;
		}

	}

	function checkEmail() {
		var email = document.getElementById('emailID');
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if (email.value != "" && !filter.test(email.value)) {
			return false;
		} else {
			return true;
		}
	}
</script>

<style type="text/css">
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}

#BankListdiv {
	position: center;
	visibility: visible;
	overflow: hidden;
	border: purple;
	background-color: white;
	border: 1px solid #333;
	padding: 5px;
}
</style>
<%-- <title><spring:message code="LABEL_TITLE" text="GIM Mobile"></spring:message></title> --%>
</head>
<body>
	<div class="row">
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="LABEL_CH_MGMT"
								text="Clearing Houses" /></span>
					</h3>
				</div>
				<br />
				<div class="col-md-3 col-md-offset-4"
					style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
				<form:form class="form-inline" id="clearingPool"
					action="saveClearanceHouse.htm" method="post" name="clearingPool"
					commandName="clearingHouseDTO">
					<jsp:include page="csrf_token.jsp" />
					<div class="box-body">
						<authz:authorize ifAnyGranted="ROLE_execuser">
							<a href="#" onclick="disableForm('clearingPool');">Click me</a>
						</authz:authorize>
						<form:hidden path="clearingPoolId"></form:hidden>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_CH_NAME" text="Clearing House Name" /><font
									color="red">*</font></label>
								<form:input path="clearingHouseName" id="clearingHouseName"
									cssClass="form-control" maxlength="32" />
								<font color="RED"> <form:errors path="clearingHouseName" /></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_CURRENCY" text="Currency" /><font color="red">*</font></label>
								<form:select path="currency" cssClass="dropdown chosen-select">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</form:option>
									<form:options items="${masterData.currencyList}"
										itemLabel="currencyName" itemValue="currencyId" />
								</form:select>
								<font color="RED"> <form:errors path="currency" /></font>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_SETTLEMENT_INTVL" text="Settlement Interval" /><font
									color="red">*</font></label>

								<div class="col-sm-6" style="margin-left: 25px;">
									<strong><spring:message code="LABEL_SETTLEMENT_HOURS"
											text="Hours" /></strong> &nbsp;
									<form:input path="settlementIntervalHours" size="2"
										maxlength="2" />
									<font color="RED"> <form:errors
										path="settlementIntervalHours"></form:errors></font> &nbsp; <strong><spring:message
											code="LABEL_SETTLEMENT_MINS" text="Mins" /> </label></strong>&nbsp;
									<form:input path="settlementIntervalMins" size="2"
										maxlength="2" />
									<font color="RED"> <form:errors
										path="settlementIntervalMins"></form:errors></font>
								</div>

							</div>
							<div class="col-sm-6">
								<label class="col-sm-3"><spring:message
										code="LABEL_START_UP_TIME" text="Cut-Off Time" /><font
									color="red">*</font></label>
								<div class="col-sm-6" style="margin-left: 25px;">
									<strong><spring:message code="LABEL_SETTLEMENT_HOURS"
											text="Hours" /></strong>&nbsp;
									<form:input path="startUpTimeHours" maxlength="2" size="2" />
									<font color="RED"> <form:errors path="startUpTimeHours"></form:errors></font>
									&nbsp; <strong><spring:message
											code="LABEL_SETTLEMENT_MINS" text="Mins" /></strong> &nbsp;
									<form:input path="startUpTimeMinutes" maxlength="2" size="2" />
									<font color="RED"> <form:errors
										path="startUpTimeMinutes"></form:errors></font>
								</div>

							</div>
						</div>

						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_FILE_FORMAT" text="File Format" /></label>
								<form:select path="fileFormat" cssClass="dropdown chosen-select">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</form:option>
									<form:option value="ddMMyyHHmm.csv">ddMMyyHHmm.csv</form:option>
									<form:option value="ddMMyy.csv">ddMMyy.csv</form:option>
								</form:select>
								<font color="RED"> <form:errors path="fileFormat"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_MESSAGE_TYPE" text="Message Type" /><font
									color="red">*</font></label>
								<form:input path="messageType" cssClass="form-control"
									maxlength="10" />
								<font color="RED"> <form:errors path="messageType"></form:errors></font>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_DESCRIPTION" text="Description" /></label>
								<form:textarea path="description" rows="3" cols="19"
									onKeyDown="textCounter(document.clearingPool.description,180,180)"
									onKeyUp="textCounter(document.clearingPool.description,180,180)" />
								<font color="RED"> <form:errors path="description"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANKS" text="Banks" /></label>
								<form:select path="banks" multiple="true"
									items="${masterData.bankList}" itemValue="bankId"
									itemLabel="bankName" />
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_CONTACT_PERSON" text="Contact Person" /><font
									color="red">*</font></label>
								<form:input path="contactPerson" cssClass="form-control"
									maxlength="32" />
								<font color="RED"> <form:errors path="contactPerson"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_MOBILE_NO" text="MobileNumber" /><font
									color="red">*</font></label>
								<form:input path="mobileNumber" cssClass="form-control"
									maxlength="20" />
								<font color="RED"> <form:errors path="mobileNumber"></form:errors></font>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_EMAILID" text="EmailID" /><font color="red">*</font></label>
								<form:input path="emailID" cssClass="form-control"
									maxlength="32" />
								<font color="RED"> <form:errors path="emailID"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
								<form:radiobutton path="status" value="1" />
								<spring:message code="LABEL_ACTIVE" text="Active" />
								&nbsp; &nbsp;
								<form:radiobutton path="status" value="0" />
								<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
								<FONT color="red"> <form:errors path="status" /></FONT>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_SETTLEMENT_ACCOUNT" text="Settlement Account" /><font
									color="red">*</font></label>
								<form:input path="settlementAccount" cssClass="form-control"
									maxlength="12" />
								<font color="RED"> <form:errors path="settlementAccount"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_GUARANTEE_ACCOUNT" text="Guarantee Account" /><font
									color="red">*</font></label>
								<form:input path="guaranteeAccount" cssClass="form-control"
									maxlength="12" />
								<font color="RED"> <form:errors path="guaranteeAccount"></form:errors></font>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_CENTRAL_BANK_ACCOUNT" text="CentralBank Account" /><font
									color="red">*</font></label>
								<form:input path="centralBankAccount" cssClass="form-control"
									maxlength="12" />
								<font color="RED"> <form:errors path="centralBankAccount"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_GIM-UEMOA-SWIFT-CODE" text="GIM-UEMOA-SwiftCode" /><font
									color="red">*</font></label>
								<form:input path="EOTSwiftCode" cssClass="form-control"
									maxlength="4" />
								<font color="RED"> <form:errors path="EOTSwiftCode"></form:errors></font>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5"><spring:message
										code="LABEL_MESSAGE_SENDER" text="Message Sender" /><font
									color="red">*</font></label>
								<form:input path="messageSender" cssClass="form-control"
									maxlength="12" />
								<font color="RED"> <form:errors path="messageSender"></form:errors></font>
							</div>
						</div>
						<c:choose>
							<c:when test="${(clearingHouseDTO.clearingPoolId eq null)}">
								<c:set var="buttonName" value="LABEL_ADD" scope="page" />
							</c:when>
							<c:otherwise>
								<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
							</c:otherwise>
						</c:choose>

						<div class="btn-toolbar pull-right">
							<input type="button" class="btn-primary btn" id="submitButton"
								value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
								onclick="validate();"></input> <input type="button"
								class="btn-primary btn"
								value="<spring:message code="LABEL_CH_CANCEL" text="Cancel" />"
								onclick="cancelForm();" />
						</div>
						<br /> <br /> <br />
						<!-- // vineeth, on 07-08-2018 -->
						<form:hidden path="mobileNumberLength"></form:hidden>
					<!-- 	// vineeth change over -->
				</form:form>
			</div>

		</div>

	</div>
</body>
</html>
