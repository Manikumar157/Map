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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
.pull-right {
    float: right!important;
    margin-right: 164px;
}
.cancelbttn {
    float: right!important;
    margin-right: -139px;
}-->
</style>
<script type="text/javascript">
	$(document).ready(
			function() {
				$("#countryId").change(function() {
					/*  @BugId-503 by:rajlaxmi for:dropdown reset issue */
					$("#cityId option:gt(0)").remove();
					$("#quarterId option:gt(0)").remove();
					updateChosen();
					
					$country = document.getElementById("countryId").value;
					if($country.length<1)
						$country=0;
					$csrfToken = $("#csrfToken").val();

					$.post("getCityList.htm", {
						country : $country,
						csrfToken : $csrfToken
					}, function(data) {
						$country = document.getElementById("countryId").value;
						
						document.getElementById("cities").innerHTML = data;
				
						setTokenValFrmAjaxResp();
						applyChosen();
					});
				});
				
				$("#cities").change(function() {
					$city = document.getElementById("cityId").value;
					if($city.length<1)
						$city=0;
					$csrfToken = $("#csrfToken").val();
					$.post("getQuartersList.htm", {
						city : $city,
						csrfToken : $csrfToken
					}, function(data) {
						document.getElementById("quarters").innerHTML = "";
						document.getElementById("quarters").innerHTML = data;
						setTokenValFrmAjaxResp();
						applyChosen();
						
					});
				});

				
			});

	function validate() {
		var merchantName = $("#merchantName").val();
		var primaryContactName = $("#primaryContactName").val();
		var primaryContactAddress = $("#primaryContactAddress").val();
		var primaryContactPhone = $("#primaryContactPhone").val();
		var primaryContactMobile = $("#primaryContactMobile").val();
		var primaryeMailAddress = $("#primaryeMailAddress").val();
		var alternateContactName = $("#alternateContactName").val();
		var alternateContactAddress =$("#alternateContactAddress").val();
		var alternateContactPhone = $("#alternateContactPhone").val();
		var alternateContactMobile = $("#alternateContactMobile").val();
		var alternateeMailAddress = $("#alternateeMailAddress").val();
		var emailPattern = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
		var countryId = $("#countryId").val();
		var cityId = $("#cityId").val();
		var quarterId = $("#quarterId").val();
		var address = $("#address").val();
		var actives = $("#actives").val();

		/*  @bugId-503 date : 25/07/2016 by : rajlaxmi for:corrected alert sequence */
		if (merchantName == "") {
			alert("<spring:message code='ALERT_MERCHANT_NAME' text='Please Enter  Merchant Name'/>");
			
			return false;
		}
		if (merchantName.charAt(0) == " "
				|| merchantName.charAt(merchantName.length - 1) == " ") {
			alert("<spring:message code='ALERT_VALIDATION_MERCHANT_SPACE' text='Please remove unwanted spaces in merchant Name'/>");
			document.merchantForm.merchantName.focus();
			return false;
		}
		if (! merchantName.match(/^[a-zA-Z\s]+$/)) {
			alert("<spring:message code='ALERT_MERCHANT_NAME_VALID' text='Merchant Name should contain only alphabets'/>");
			return false;
		}

		if (primaryContactName == "") {
			alert("<spring:message code='ALERT_PRIMARY_CONTACT_NAME' text='Please Enter Primary Contact Name'/>");
			return false;
		}
		if (primaryContactName.charAt(0) == " "
				|| primaryContactName.charAt(primaryContactName.length - 1) == " ") {
			alert("<spring:message code='ALERT_VALIDATION_PRIMARY_CONTACT_NAME_SPACE' text='Please remove unwanted spaces in Primary Contact Name'/>");
			document.merchantForm.primaryContactName.focus();
			return false;
		}
		if (!primaryContactName.match(/^[a-zA-Z\s]+$/)) {
			alert("<spring:message code='ALERT_PRIMARY_CONTACT_NAME_VALID' text='Primary Contact Name should contain only alphabets'/>");
			return false;
		}

		if (primaryeMailAddress == "") {
			alert("<spring:message code='ALERT_EMAIL_ID' text='Please enter email id'/>");
			return false;
		}
		if (!primaryeMailAddress.match(emailPattern)) {
			alert("<spring:message code='ALERT_EMAIL_VALID_ID' text='Please enter valid Email ID'/>");
			return false;
		}

		if (primaryContactMobile == "") {
			alert("<spring:message code='ALERT_PRIMARY_CONTACT_MOBILE' text='Please Enter Primary Contact Mobile'/>");
			return false;
		}
		if (primaryContactMobile.charAt(0) == " "
				|| primaryContactMobile.charAt(primaryContactMobile.length - 1) == " ") {
			alert("<spring:message code='ALERT_VALIDATION_PRIMARY_MOBILE_NO_SPACE' text='Please remove unwanted spaces in Primary Contact Number'/>");
			
			return false;
		}

		if (primaryContactMobile.length<10 || primaryContactMobile.length>15
				|| (!primaryContactMobile.match(/^[0-9]*$/))) {

			alert("<spring:message code='ALERT_VALIDATION_PRIMARY_MOBILE_NUMBER_LENGHT' text='Please enter a valid Mobile number ,number should have minimum 10 digits and mximum 14 digits'/>");
			return false;
		}

		if(primaryContactPhone == ""){
			alert("<spring:message code='ALERT_VALID_PHONE_NUMBER_NULL' text='Primary Contact Phone should not be null'/>");
			return false;
		}
		if (primaryContactPhone.charAt(0) == " "
			|| primaryContactPhone.charAt(primaryContactPhone.length - 1) == " ") {
		alert("<spring:message code='ALERT_VALIDATION_PRIMARY_PHONE_NO_SPACE' text='Please remove unwanted spaces in Primary Phone Number'/>");


		return false;
	}
		if (primaryContactPhone.length<6 || primaryContactPhone.length>15|| (!primaryContactPhone.match(/^[0-9]*$/))) {
			alert('<spring:message code="ALERT_VALID_PHONE_NUMBER" text="Please enter a valid Primary Contact Phone ,number should have minimum 6 digits and maximum 14 digits"/>');
			return false;
		}
		
		

		if (alternateContactName != "") {
			if (!alternateContactName.match(/^[a-zA-Z\s]+$/)) {
				alert("<spring:message code='ALERT_PRIMARY_CONTACT_NAME_CRITERIA' text='Alternate Contact Name should have alphabets only'/>");
				return false;
			}

			if (alternateContactName.charAt(0) == " "
					|| alternateContactName
							.charAt(alternateContactName.length - 1) == " ") {
				alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_NAME_SPACE' text='Please remove unwanted spaces in Alternate Contact Name'/>");
				document.showOutletForm.alternateContactName.focus();
				return false;
			}}
		

		if (alternateContactPhone != "") {
			if (alternateContactPhone.charAt(0) == " "
					|| alternateContactPhone
							.charAt(alternateContactPhone.length - 1) == " ") {
				alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_PHONE_SPACE' text='Please remove unwanted spaces in Alternate Contact Phone'/>");
				return false;
			}
			if (alternateContactPhone.length<10 || alternateContactPhone.length>15
					|| (!alternateContactPhone.match(/^[0-9]*$/))) {
				alert("<spring:message code='ALERT_VALID_ALTERNATE_PHONE_NUMBER' text='Please enter a valid contact phone number ,number should have minimum 10 digits and maximum 14 digits'/>");
				return false;
			}

		}

		if (alternateContactMobile != "") {
			
			 if(alternateContactMobile.charAt(0) == " " || alternateContactMobile.charAt(alternateContactMobile.length-1) == " "){
		         alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_MOBILE_SPACE' text='Please remove unwanted spaces in Alternate Contact Mobile'/>");
		         
		         return false;
		         } 
			if (alternateContactMobile.length<10 || alternateContactMobile.length>15
					|| (!alternateContactMobile.match(/^[0-9]*$/))) {
				alert("<spring:message code='ALERT_VALIDATION_ALTERNATE_CONTACT_MOBILE_LENGTH' text='Please enter a valid alternate mobile number ,number should have minimum 10 digits and maximum 14 digits'/>");
				return false;
			}

		}

		if (alternateeMailAddress != ""
				&& !alternateeMailAddress.match(emailPattern)) {

			alert("<spring:message code='VALIDATION_ALT_EMAIL' text='Please enter valid Alternate Email ID'/>");
			return false;
		}

		if (primaryContactAddress == "") {
			alert('<spring:message code="ALERT_PRIMARY_CONTACT_ADDRESS" text="Please Enter primary Contact address"/>');
			return false;
		}
		if (primaryContactAddress.length > 200) {
			alert('<spring:message code="ALERT_PRIMARY_ADDRESS_LENGTH" text="Primary Contact Address should not exceed more than 200 characters"/>');
			return false;
		}
		if (primaryContactAddress.charAt(0) == " "
			|| primaryContactAddress.charAt(primaryContactAddress.length - 1) == " ") {
		alert("<spring:message code='ALERT_PRIMARY_CONTACT_ADDRESS_SPACE' text='Please remove unwanted spaces in primary contact address'/>");
		return false;
	}

		if (alternateContactAddress != "") {
			if (alternateContactMobile.length > 200) {
				alert("<spring:message code='ALERT_ALTERNATE_ADDRESS_LENGTH' text='Alternate Contact Address should not exceed more than 200 characters'/>");
				return false;
			}
		}

		if (address == "") {
			alert("<spring:message code='ALERT_ADDRESS' text='Please Enter Address'/>");
			return false;
		} else if (address.length > 200) {
			alert("<spring:message code='ALERT_ADDRESS_LENGTH' text='Address should not exceed more than 200 characters'/>");
			return false;
		}
		if (address.charAt(0) == " "
			|| address.charAt(address.length - 1) == " ") {
		alert("<spring:message code='ALERT_ADDRESS_SPACE' text='Please remove unwanted spaces in Address'/>");
		return false;
	}

		if (countryId == "") {
			alert("<spring:message code='ALERT_COUNTRY' text='Please select a country'/>");
			return false;
		}
		if (cityId == "" || cityId =="select") {
			alert("<spring:message code='ALERT_CITY' text='Please select a city'/>");
			return false;
		}
		if (quarterId == "" || quarterId == "select"  ) {
			alert("<spring:message code='ALERT_QUARTER' text='Please select a quarter'/>");
			return false;
		}
		if (address == "") {
			alert("<spring:message code='ALERT_ADDRESS' text='Please Enter Address'/>");
			return false;
		}
		if (actives == "" || actives == "select") {
			alert("<spring:message code='ALERT_STATUS' text=' Please select status'/>");
			return false;
		}
		document.merchantForm.action = "saveMerchant.htm";
		document.merchantForm.submit();
	}
	
	function cancelForm() {
		
		document.merchantForm.action = "searchMerchant.htm?merchantAction=cancelEdit";
		document.merchantForm.submit();
	}
</script>
</head>
<body>
	<form:form id="merchantForm" name="merchantForm"
		action="merchantDetails.htm" method="post" commandName="merchantDTO"
		class="form-inline">
		<jsp:include page="csrf_token.jsp" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_MERCHANT" text="Merchant"></spring:message></span>
					</h3>
				</div>
				<div class="col-sm-5 col-sm-offset-10">
				<a href="javascript:submitForm('merchantForm','searchMerchant.htm?merchantAction=cancelEdit')"><strong><spring:message code="LABLE_ VIEW_MERCHANT" text="View Merchant"> </spring:message></strong></a>
				</div>
				<br />

				<div style="color: #ba0101; font-weight: bold; font-size: 12px;text-align: center;
vertical-align: middle">
					<spring:message code="${message}" text="" />
				</div>
				<div class="box-body">
					<form:hidden path="merchantId" />
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_MERCHANT_NAME" text="Merchant Name"></spring:message><font
								color="red">*</font></label>
							<form:input path="merchantName" cssClass="form-control"
								maxlength="50"></form:input>
							<font color="RED"> <form:errors path="merchantName"></form:errors></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_NAME" text="Primary Contact Name"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryContactName" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors path="primaryContactName"></form:errors></font>
						</div>

					</div>
					<br />


					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_EMAIL_ADDRESS" text="Primary Email Address"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryeMailAddress" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors path="primaryeMailAddress"></form:errors></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_MOBILE"
									text="Primary Contact Mobile"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryContactMobile"
								cssClass="form-control required number"></form:input>
							<font color="RED"> <form:errors
								path="primaryContactMobile"></form:errors></font>
						</div>
					</div>

					<div class="row">

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_PHONE" text="Primary Contact Phone"></spring:message><font
								color="red">*</font></label>
							<form:input path="primaryContactPhone" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors path="primaryContactPhone"></form:errors></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_NAME"
									text="Alternate Contact Name"></spring:message> </label>
							<form:input path="alternateContactName" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors
								path="alternateContactName"></form:errors></font>
						</div>

					</div>

					<div class="row">

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_PHONE"
									text="Alternate Contact Phone"></spring:message> </label>
							<form:input path="alternateContactPhone" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors
								path="alternateContactPhone"></form:errors></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_MOBILE"
									text="Alternate Contact Mobile"></spring:message> </label>
							<form:input path="alternateContactMobile" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors
								path="alternateContactMobile"></form:errors></font>
						</div>

					</div>


					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_EMAIL_ADDRESS"
									text="Alternate Email Address"></spring:message> </label>
							<form:input path="alternateeMailAddress" cssClass="form-control"></form:input>
							<font color="RED"> <form:errors
								path="alternateeMailAddress"></form:errors></font>
						</div>
						<div class="col-sm-6">

							<label class="col-sm-4"><spring:message
									code="LABEL_PRIMARY_CONTACT_ADDRESS"
									text="Primary Contact Address"></spring:message><font
								color="red">*</font></label>
							<form:textarea path="primaryContactAddress" cssClass="text_area"
								onKeyDown="textCounter(document.customerRegistrationForm.address,180,180)"
								onKeyUp="textCounter(document.customerRegistrationForm.address,180,180)"
								style="width: 180px; height: 100px;"></form:textarea>
							<font color="RED"> <form:errors
								path="primaryContactAddress"></form:errors></font>
						</div>


					</div>

					<div class="row">

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ALTERNATE_CONTACT_ADDRESS"
									text="Alternate Contact Address"></spring:message></label>
							<form:textarea path="alternateContactAddress"
								cssClass="text_area"
								onKeyDown="textCounter(document.customerRegistrationForm.address,180,180)"
								onKeyUp="textCounter(document.customerRegistrationForm.address,180,180)"
								style="width: 180px; height: 100px;"></form:textarea>
							<font color="RED"> <form:errors
								path="alternateContactAddress"></form:errors></font>
						</div>

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ADDRESS" text="Address:" /><font color="red">*</font></label>
							<form:textarea path="address" cssClass="text_area"
								onKeyDown="textCounter(document.customerRegistrationForm.address,180,180)"
								onKeyUp="textCounter(document.customerRegistrationForm.address,180,180)"
								style="width: 180px; height: 100px;"></form:textarea>
							<font color="RED"> <form:errors path="address"></form:errors></font>
						</div>



					</div>


					<div class="row">
						<div class="col-md-6">
							<%-- <label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY" text="Country :" /><font color="red">*</font></label>
							<select class="dropdown chosen-select" id="countryId" name="countryId">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${masterData.countryList}" var="country">
										<c:forEach items="${country.countryNames}" var="cNames">
											<c:if test="${cNames.comp_id.languageCode==lang }">
												<option value="<c:out value="${country.countryId}"/>"
													<c:if test="${country.countryId eq merchantDTO.countryId}" >selected=true</c:if>>
													<c:out value="${cNames.countryName}" />
												</option>
											</c:if>
										</c:forEach>
									</c:forEach>
								</option>
							</select> --%>
							 <label class="col-sm-5"><spring:message code="LABEL_COUNTRY" text="Country"/></label>
							<label class="col-sm-5"><spring:message code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
							<form:hidden path="countryId" value="1"/>
						</div>


						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message code="LABEL_CITY"
									text="City:" /><font color="red">*</font></label>
							<div id="cities">
								<form:select path="cityId" class="dropdown_big chosen-select">
									<option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</option>
									<form:options items="${City}" itemValue="cityId"
										itemLabel="city"></form:options>
								</form:select>
							</div>
							<font color="RED"> <form:errors path="cityId"></form:errors></font>
						</div>
					</div>
					<br />
					<div class="row">

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_QUARTER" text="Quarter:" /><font color="red">*</font></label>
							<div id="quarters">
								<form:select path="quarterId" class="dropdown_big chosen-select"
									id="quarterId">
									<option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</option>
									<form:options items="${quarter}" itemValue="quarterId"
										itemLabel="quarter"></form:options>
								</form:select>
							</div>
							<font color="RED"> <form:errors path="quarterId"></form:errors></font><br />
						</div>

						<div class="col-sm-6">
							<label class="col-sm-4"><span style="color: #FF0000;"></span>
								<spring:message code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
							<form:select path="active" id="actives" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="---Please Select---" />
								</form:option>
								<form:option value="10">
									<spring:message code="LABEL_ACTIVE" text="Active" />
								</form:option>
								<form:option value="20">
									<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
								</form:option>
							</form:select>
						</div>

					</div>
				</div>
				<br />


			</div>
		</div>
		<div class="box-footer">

			<input type="button" id="submitButton"
				value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
				onclick="validate();" class="btn btn-primary pull-right"></input>
   <!--by:rajlaxmi for:adding cancel bttn -->
				<input type="button" class="btn btn-default cancelbttn" 
				value="<spring:message code="LABEL_CANCEL" text="cancel"/>"
				 onclick="cancelForm();"></input>
				
		</div>
		<br />
	</form:form>
	<br />

	<!-- /.box -->

</body>
</html>
