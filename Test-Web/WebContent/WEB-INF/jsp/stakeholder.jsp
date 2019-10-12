<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>

<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/stakeholder.js"></script>
<script type="text/javascript">
	var Alertmsg = {
		"stakeHolderOrganization" : "<spring:message code="LABEL.STAKEORGANIZATION.NOTEMPTY" text="Please enter Stake Holder Organization"/>",
		"stakeHolderOrganizationSpace" : "<spring:message code="LABEL.STAKEORGANIZATION.SPACE" text="Please remove unwanted spaces in Stake Holder Organization"/>",
		"address" : "<spring:message code="LABEL.ADDRESS.NOTEMPTY" text="Please enter Address"/>",
		"contactPerson" : "<spring:message code="LABEL.CONTACTPERSON.NOTEMPTY" text="Please enter Contact Person"/>",
		"contactpersonSpace" : "<spring:message code="LABEL.CONTACTPERSON.SPACE" text="Please remove unwanted spaces in Contact Person"/>",
		"contactAddress" : "<spring:message code="LABEL.CONTACTADDRESS.NOTEMPTY" text="Please enter Contact Address"/>",
		"email" : "<spring:message code="LABEL.EMAIL.NOTEMPTY" text="Please enter Email Address"/>",
		"emailValid" : "<spring:message code="LABEL.EMAIL.NUMBER" text="Please enter valid Email Address"/>",
		"country" : "<spring:message code="VALID_SELECT_COUNTRY" text="Please select Country Name"/>",
		"contactmobile" : "<spring:message code="LABEL.CONTACTMOBILE.NOTEMPTY" text="Please enter Contact Mobile"/>",
		"contactmobileSpace" : "<spring:message code="LABEL.CONTACTMOBILE.SPACE" text="Please remove unwanted spaces in Contact Mobile"/>",
		"stakeHolderOrganizationDigits" : "<spring:message code="LABEL.STAKEORGANIZATION.DIGITS" text="Please enter Stake Holder Organization with out any special characters"/>",
		"contactpersonDigits" : "<spring:message code="LABEL.CONTACTPERSON.DIGITS" text="Please enter only charcters in Contact Person"/>",
		"contactmobileCharacters" : "<spring:message code="LABEL.CONTACTMOBILE.CHARACTERS" text="Please enter Valid Contact Mobile Number"/>",
		"addressSpace" : "<spring:message code="LABEL.ADDRESS.SPACE" text="Please remove unwanted Spaces in Address"/>",
		"contactaddressSpace" : "<spring:message code="LABEL.CONTACTADDRESS.SPACE" text="Please remove unwanted spaces in contact address"/>",
		"stakeHolderOrganizationLength" : "<spring:message code="LABEL.STAKEORGANIZATION.LENGTH" text="Stake Holder Organization should not exceed more than 32 characters"/>",
		"validMobileLength" : "<spring:message code="VALIDATION_STK_MOBILE_DIGITS" text="Please enter a valid mobile number of length"/>",
		"contactAddressAlphaNumeric" : "<spring:message code="ERROR_MESSAGE_CONTACT_ADDRESS_ALPHANUMERIC" text="Contact Address should contain alphanumeric and special characters ,#/-&"></spring:message>",
		"addressAlphaNumeric" : "<spring:message code="ERROR_MESSAGE_ADDRESS_ALPHANUMERIC" text="Address should contain alphanumeric and special characters ,#/-&"></spring:message>",
	};

	
	$(document)
			.ready(
					function() {

						$("#countryId")
								.change(
										function() {

											$country = document
													.getElementById("countryId").value;

											$
													.post(
															"getStakeHolderMobileNumberLength.htm",
															{

																country : $country

															},
															function(data) {
																document
																		.getElementById("mobileNum").innerHTML = "";
																document
																		.getElementById("mobileNum").innerHTML = data
																		.match(
																				/\d/g)
																		.join(
																				"");
															});
										});

					});
	

	
	function validate() {

		var alphaNumericAddress = /^([ 0-9a-zA-Z;/'#&,-]{0,100})*$/;
		var stakeholderorganization = document
				.getElementById("stakeholderOrganization").value;
		var address = document.getElementById("address").value;
		var contactperson = document.getElementById("contactPersonName").value;
		var contactaddress = document.getElementById("contactAddress").value;
		var country = document.getElementById("countryId").value;
		var email = document.getElementById("emailAddress").value;
		var mobileNumber = document.getElementById("contactMobile").value;
		var contactPhone = document.getElementById("contactPhone").value;
		var emailfilter = '^\([a-zA-Z0-9_.-])+@([a-z])+.([a-z])+$';
		var pattern = '^\[a-zA-ZÀ-ÿ0-9-\' ]*$';
		var mobilephpattern = '^\[0-9]{10,15}$';
		var numPattern = '^[0-9]*$';
		//  var filter = '/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/';
		var mobileNumLen = 0;
		
		if (country != null) {
			mobileNumLen = parseInt(document.getElementById('mobileNum').innerHTML);

		}

		if (document.getElementById('countryId').value != '') {
			mobilepattern = '^\[0-9]{' + mobileNumLen + '}$';
		}

		if (stakeholderorganization == "") {
			alert(Alertmsg.stakeHolderOrganization);
			return false;
		} else if (stakeholderorganization.charAt(0) == " "
				|| stakeholderorganization
						.charAt(stakeholderorganization.length - 1) == " ") {
			alert(Alertmsg.stakeHolderOrganizationSpace);
			return false;
		} else if (stakeholderorganization.search(pattern) == -1) {
			alert(Alertmsg.stakeHolderOrganizationDigits);
			return false;
		} else if (stakeholderorganization.length > 32) {
			alert(Alertmsg.stakeHolderOrganizationLength);
			return false;
		}else if(stakeholderorganization.search(numPattern)!=-1)
			{
			alert("Please Enter valid data in 'Organization' field");
			return false;
			}
		
		
		else if (contactperson == "") {
			alert(Alertmsg.contactPerson);
			return false;
		} else if (contactperson.charAt(0) == " "
				|| contactperson.charAt(contactperson.length - 1) == " ") {
			alert(Alertmsg.contactpersonSpace);
			return false;
		} else if (contactperson.search(pattern) == -1) {
			alert(Alertmsg.contactpersonDigits);
			return false;
		} else if (contactperson.length < 2) {
			alert("Contact person name should be minimum two characters");
			return false;
		} else if (contactperson.search(numPattern) != -1) {
			alert('<spring:message code="VALIDATION_CONT_PERSON_CHPOOL" text="Please enter valid Contact Person Name  "/>');
			return false;
		}else if (country == "") {
			alert(Alertmsg.country);
			return false;
		} else if ((mobileNumber != "" && mobileNumber.search(mobilepattern) == -1)) {
			alert(Alertmsg.validMobileLength + mobileNumLen);
			return false;
		} else if ((contactPhone != "" && contactPhone.search(mobilephpattern) == -1)) {
			alert('<spring:message code="VALIDATION_PHONE_DIGITS" text="Please enter a valid Phone number"/>');
			return false;
		} else if (email != "" && (email.search(emailfilter) == -1)) {
			alert('<spring:message code="VALID_EMAIL_ID" text="Please enter a valid email id"/>');
			return false;
		} else if (address == "") {
			alert(Alertmsg.address);
			return false;
		} else if (address.length > 200) {
			alert('<spring:message code="LABEL.ADDRESS.LENGTH" text="Address should not exceed more than 180 characters"/>');
			return false;
		} else if (address.charAt(0) == " "
				|| address.charAt(address.length - 1) == " ") {
			alert(Alertmsg.addressSpace);
			return false;
		} else if (contactaddress == "") {
			alert(Alertmsg.contactAddress);
			return false;
		} else if (contactaddress.length > 200) {
			alert('<spring:message code="LABEL.ADDRESS.LENGTH" text="Address should not exceed more than 180 characters"/>');
			return false;
		} else if (contactaddress.charAt(0) == " "
				|| contactaddress.charAt(contactaddress.length - 1) == " ") {
			alert(Alertmsg.contactaddressSpace);
			return false;
		} else if (address.search(alphaNumericAddress)) {
			alert(Alertmsg.addressAlphaNumeric);
			return fase;
		} else if (contactaddress.search(alphaNumericAddress)) {
			alert(Alertmsg.contactAddressAlphaNumeric);
			return false;
		}  else {
			document.StakeHolder.action = "saveStakeHolderForm.htm";
			document.StakeHolder.submit();
		}
	}

	function textCounter(field, cntfield, maxlimit) {
		if (field.value.length > maxlimit) // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
		// otherwise, update 'characters left' counter
		else
			cntfield.value = maxlimit - field.value.length;
	}
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>

</head>

<body>
	<form:form class="form-inline" name="StakeHolder" id="StakeHolder"
		action="saveStakeHolderForm.htm" method="post"
		commandName="stakeHolderDTO">
		<jsp:include page="csrf_token.jsp" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_STAKEHOLDER"
								text="Stake-Holders" /></span>
					</h3>
					<br />
					<div class="col-sm-6 col-sm-offset-3"
						style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
				<br />
				<div id="mobileNum" style="visibility: hidden"></div>
				<div class="box-body">
					<form:hidden path="stakeholderId"></form:hidden>
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"> <spring:message
									code="LABEL_STAKEHOLDER" text="StakeHolder Organization" /><font
								color="red">*</font></label>
							<form:input path="stakeholderOrganization"
								cssClass="form-control" maxlength="32" />
							<font color="red"> <form:errors
								path="stakeholderOrganization" /></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_CONTACT_PERSONNAME" text="Contact Person Name" /><font
								color="red">*</font></label>
							<form:input path="contactPersonName" cssClass="form-control"
								maxlength="30" />
							<font color="red"> <form:errors path="contactPersonName" /></font>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_ADDRESS" text="Address" /><font color="red">*</font></label>
							<form:textarea path="address" rows="3" cols="19"
								onKeyDown="textCounter(document.StakeHolder.address,180,180)"
								onKeyUp="textCounter(document.StakeHolder.address,180,180)" />
							<font color="red"> <form:errors path="address" /></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_CONTACT_ADDRESS" text="ContactAddress" /><font
								color="red">*</font></label>
							<form:textarea path="contactAddress" rows="3" cols="19"
								onKeyDown="textCounter(document.StakeHolder.contactAddress,180,180)"
								onKeyUp="textCounter(document.StakeHolder.contactAddress,180,180)" />
							<font color="red"> <form:errors path="contactAddress" /></font>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_EMAIL_ADDRESS" text="Email Address" /></label>
							<form:input path="emailAddress" cssClass="form-control"
								maxlength="32" />
							<font color="red"> <form:errors path="emailAddress" /></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY" text="Country" /><font color="red">*</font></label>
							<select class="dropdown chosen-select" id="countryId"
								name="countryId">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</option>
								<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${masterData.countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang }">
											<option value="<c:out value="${country.countryId}"/>"
												<c:if test="${country.countryId eq stakeHolderDTO.countryId}" >selected=true</c:if>>
												<c:out value="${cNames.countryName}" />
											</option>
										</c:if>
									</c:forEach>
								</c:forEach>
							</select><font color="RED"> <form:errors path="countryId"
								cssClass="" /></font>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_CONTACT_PHONE" text=" Phone Number" /></label>
							<form:input path="contactPhone" cssClass="form-control"
								maxlength="15" />
							<font color="red"> <form:errors path="contactPhone" /></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_CONTACT_MOBILE" text="Mobile Number" /></label>
							<form:input path="contactMobile" cssClass="form-control"
								maxlength="15" />
							<font color="red"> <form:errors path="contactMobile" /></font>
						</div>
					</div>
					
					<c:choose>
						<c:when test="${(stakeHolderDTO.stakeholderId eq null)}">
							<c:set var="buttonName" value="LABEL_ADD" scope="page" />
						</c:when>
						<c:otherwise>
							<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
						</c:otherwise>
					</c:choose>
					<div class="col-sm-5 col-sm-offset-10">
						<input type="button" class="btn btn-primary"
							value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
							onclick="validate();" />
						<div class="space"></div>
						<c:if test="${stakeHolderDTO.stakeholderId ne null}">
							<input class="btn btn-primary" type="button"
								value="<spring:message code="LABEL_CANCEL"
		text="Cancel" />"
								onclick="cancelForm();" />
						</c:if>
					</div>
					<br /> <br />
				</div>
			</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>

								<th><spring:message code="LABEL_STAKEHOLDER"
										text="StakeHolder Organization" /></th>
								<th><spring:message code="LABEL_CONTACT_PERSONNAME"
										text="Contact Person Name" /></th>
								<th><spring:message code="LABEL_CONTACT_PHONE"
										text="Phone Number" /></th>
								<th><spring:message code="LABEL_CONTACT_MOBILE"
										text="Mobile Number" /></th>
								<th><spring:message code="LABEL_EMAIL_ADDRESS" text="Email" /></th>
								<th><spring:message code="LABEL_ACCOUNTNUMBER"
										text="Account Number" /></th>
								<th><spring:message code="LABEL_ACTION" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${masterData.stakeHolderList}"
								var="stakeholder">
								<tr>
									<td align="left"><c:out
											value="${stakeholder.stakeholderOrganization}" /></td>
									<td align="left"><c:out
											value="${stakeholder.contactPersonName}" /></td>
									<td align="left"><c:out
											value="${stakeholder.contactPhone}" /></td>
									<td align="left"><c:out
											value="${stakeholder.contactMobile}" /></td>
									<td align="left"><c:out
											value="${stakeholder.emailAddress}" /></td>
									<td align="left"><c:out
											value="${stakeholder.account.accountNumber}" /></td>
									<td align="left">
									<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
									<a href="javascript:stakeHolderDetail('<c:out value="${stakeholder.stakeholderId}"/>');" />
										<spring:message code="LABEL_EDIT" text="Edit" /></td>
									<%-- <a href="javascript:submitForm('StakeHolder','editStakeHolderForm.htm?stakeholderId=<c:out value="${stakeholder.stakeholderId}"/>')" />
										<spring:message code="LABEL_EDIT" text="Edit" /></td> --%>
										<!-- End -->
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>

		</div>
	</form:form>
	<script type="text/javascript">
		var mobileNumberlen = '${mobilenumberLength}';
		document.getElementById('mobileNum').innerHTML = mobileNumberlen;
		window.onload = function() {
			check();
		};
	</script>
</body>

</html>
