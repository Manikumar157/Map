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
<%-- <title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title> --%>
<script type="text/javascript">
	function validate() {
		var countryName = document.getElementsByName("country");
		var currency = document.getElementById("currencyId").value;
		var countryCodeAlpha2 = document.getElementById("countryCodeAlpha2").value;
		var countryCodeAlpha3 = document.getElementById("countryCodeAlpha3").value;
		var countryCodeNumeric = document.getElementById("countryCodeNumeric").value;
		var isdCode = document.getElementById("isdCode").value;
		var mobNumLength = document.getElementById("mobileNumberLength").value;
		var pattern = '^\[a-zA-ZÀ-ÿ-\' ]{1,100}$';
		var codePattern = '^\[A-Za-zÀ-ÿ]*$';
		var numericPattern = '^[0-9]*$';
		var mobnumericPattern = '^[0-9]*$';

		for ( var i = 0; i < countryName.length; i++) {
			if (countryName[i].value == "") {
				alert("<spring:message code='NotEmpty.countryDTO.country' text='Please enter Country Name'/>");
				return false;
			} else if (countryName[i].value.search(pattern) == -1) {
				alert("<spring:message code='LABEL.COUNTRY.DIGITS' text='Please enter Country Name with out any special characters'/>");
				return false;
			} else if (countryName[i].value.charAt(0) == " "
					|| countryName[i].value
							.charAt(countryName[i].value.length - 1) == " ") {
				alert("<spring:message code='LABEL.COUNTRY.BLANK1' text='Please remove unwanted spaces in Country Name'/>");
				return false;
			}
		}
		if (countryCodeNumeric == "") {
			alert("<spring:message code='NotNull.countryDTO.countryCodeNumeric' text='Please enter Country Code Numeric'/>");
			return false;
		} else if (countryCodeNumeric.search(numericPattern) == -1) {
			alert("<spring:message code='LABEL.COUNTRYNUMERICCODE.DIGITS' text='Please enter Country Code Numeric only in digits'/>");
			return false;
		} else if (countryCodeNumeric.length != 3) {
			alert("<spring:message code='LABEL.COUNTRYNUMERICCODE.LENGTH' text='Please enter Country Code Numeric maximum of 3 digits'/>");
			return false;
		} else if (!checkForallZeros(countryCodeNumeric)) {
			alert("<spring:message code='LABEL.VALID.COUNTRY.NUMERICCODE.ZEROS' text='Please enter a valid Country Code Numeric '/>");
			return false;
		} else if (countryCodeAlpha2 == "") {
			alert("<spring:message code='NotEmpty.countryDTO.countryCodeAlpha2' text='Please enter Country Code Alpha2'/>");
			return false;
		} else if (countryCodeAlpha2.search(codePattern) == -1) {

			alert("<spring:message code='LABEL.COUNTRYCODEALPHA2.CHARACTERS' text='Please enter Country Code Alpha2  in Characters with  Capital Letters '/>");
			return false;
		} else if (countryCodeAlpha2.length != 2) {
			alert("<spring:message code='LABEL.COUNTRYCODEALPHA2.LENGTH' text='Please enter Country Code Alpha2 Maximum of 2 characters'/>");
			return false;
		} else if (countryCodeAlpha3 == "") {
			alert("<spring:message code='NotEmpty.countryDTO.countryCodeAlpha3' text='Please enter Country Code Alpha3'/>");
			return false;
		} else if (countryCodeAlpha3.search(codePattern) == -1) {
			alert("<spring:message code='LABEL.COUNTRYCODEALPHA3.CHARACTERS' text='Please enter Country Code Alpha3 only in characters '/>");
			return false;
		} else if (countryCodeAlpha3.length != 3) {
			alert("<spring:message code='LABEL.COUNTRYCODEALPHA3.LENGTH' text='Please enter Country Code Alpha3 maximum of 3 characters'/>");
			return false;
		} else if (isdCode == "") {
			alert("<spring:message code='NotNull.countryDTO.isdCode' text='Please enter ISD CODE'/>");
			return false;
		} else if (!checkForallZeros(isdCode)) {
			alert("<spring:message code='LABEL.VALID.ISD.ZEROS' text='Please enter a valid ISD code'/>");
			return false;
		} else if (isdCode.length > 5) {
			alert("<spring:message code='LABEL.ISDCODE.LENGTH' text='Please enter ISD CODE  maximum of 5 digits'/>");
			return false;
		} else if (isdCode.search(numericPattern) == -1) {
			alert("<spring:message code='LABEL.ISDCODE.DIGITS' text='Please enter ISD Code Numeric only in digits'/>");
			return false;
		} else if (mobNumLength == "") {
			alert("<spring:message code='NotNull.countryDTO.mobileNumberLength' text='Please enter Mobile Number Length'/>");
			return false;
		} else if (mobNumLength.search(mobnumericPattern) == -1) {
			alert("<spring:message code='LABEL.MOBILENUM.LENGTH1' text='Please enter valid Mobile Number Length'/>");
			return false;
		} else if (mobNumLength > 10 || mobNumLength < 5) {
			alert("<spring:message code='LABEL.MOBILENUM.LENGTH1' text='Please enter valid Mobile Number Length'/>");
			return false;
		} else if (mobNumLength.length > 2) {
			alert("<spring:message code='LABEL.MOBILENUM.LENGTH' text='Please enter Mobile Number Length  maximum of 2 digits'/>");
			return false;
		} else if (currency == "") {
			alert("<spring:message code='NotNull.countryDTO.currencyId' text='Please select Currency Name'/>");
			return false;
		} else {
			document.countryForm.action = "saveCountries.htm";
			document.countryForm.submit();
		}
	}
	function cancelForm() {
		document.countryForm.action = "showCountries.htm";
		document.countryForm.submit();
	}

	function checkForallZeros(value) {

		var count = 0;
		for ( var i = 0; i < value.length; i++) {

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
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	function countryDetail(url,countryId){
		document.getElementById('countryId').value= countryId;
		submitlink(url,'countryForm');
	}
	//@End
	
	// Naqui: export excel for Locations
	function locationsExcel(){
			
			 submitlink("exportToXLSForLocations.htm","countryForm"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
			 $.unblockUI();
		}
	
	// Naqui: export excel for Locations
	function locationsPDF(){
			
			 submitlink("exportToPDFLocations.htm","countryForm"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
		}
</script>
</head>
<body>
	<form:form name="countryForm" id="countryForm" action="saveCountries.htm" method="post"
		commandName="countryDTO" class="form-horizontal">
		<jsp:include page="csrf_token.jsp"/>
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_COUNTRY" text="Countries" /></span>
					</h3><br />
					<div class="col-md-3 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
				</div>
				<br />
				
				<fieldset>
					<form:hidden path="countryId" />
					<authz:authorize ifAnyGranted="ROLE_addLocationsAdminActivityAdmin">
						<div class="box-body">
							<div class="row">
								<c:set var="colCount" value="0"></c:set>
								<div class="control-group col-md-2" style="margin-top: 2.3%;">
									<label class="control-label" for="name"><spring:message
											code="LABEL_COUNTRYNAME" text="Country Name"></spring:message><font
										color="red">*</font></label>
								</div>
								<c:forEach items="${languageList}" var="ll" varStatus="ccnt">
									<c:set var="colCount" value="${ccnt.count}"></c:set>
									<!-- vinod joshi,6/9/2018 changed the code here for displaying only three countries As per requirement -->
								<c:choose><c:when test="${(ll.description=='English')||(ll.description=='French')||(ll.description=='Portugais')}"> 
									<div class="control-group col-md-3">
										<label class="control-label" for="name">
										<c:out value="${ll.description}"></c:out></label> 
										<div class="controls">
											<input type="text" name="country"
												value="${countryDTO.country[ccnt.count-1]}"
												id="country<c:out value="${ccnt.count-1}"/>"
												class="form-control" maxlength="32" />
												<input type="hidden" name="languageCode" value="${ll.languageCode}"/>
										</div> 
										
										<FONT color="red"> <form:errors path="country" /></FONT>
									</div></c:when></c:choose>
									<!-- vinod joshi,6/9/2018 changed the code here for displaying only three countries As per requirement -->
								</c:forEach>
							</div>
							<div class="row">
								<div class="control-group col-md-2" style="margin-top: 2.3%;">
									<label class="control-label" for="name"> <spring:message
											code="LABEL_COUNTRY_CODE" text="Country Code"></spring:message><font
										color="red">*</font>
									</label>
								</div>
								<div class="control-group col-md-3">
									<label class="control-label" for="name"> <spring:message
											code="LABEL_COUNTRY_CODE_NUMERIC" text="Country Code" />
									</label>
									<div class="controls">
										<form:input path="countryCodeNumeric" cssClass="form-control"
											maxlength="3" />
										<FONT color="red"> <form:errors
											path="countryCodeNumeric" /></FONT>
									</div>
								</div>
								<div class="control-group col-md-3">
									<label class="control-label" for="name"><spring:message
											code="LABEL_COUNTRY_CODEALPHA2" text="Country Code" /></label>
									<div class="controls">
										<form:input path="countryCodeAlpha2" cssClass="form-control"
											maxlength="2" />
										<FONT color="red"> <form:errors
											path="countryCodeAlpha2" /></FONT>
									</div>
								</div>
								<div class="control-group col-md-3">
									<label class="control-label" for="name"> <spring:message
											code="LABEL_COUNTRY_CODEALPHA3" text="Country Code" />
									</label>
									<div class="controls">
										<form:input path="countryCodeAlpha3" cssClass="form-control"
											maxlength="3" />
										<FONT color="red"> <form:errors
											path="countryCodeAlpha3" /></FONT>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="control-group col-md-2" style="margin-top: 1.5%;">
									<label class="control-label" for="name"><spring:message
											code="LABEL_COUNTRY_ISDCODE" text="ISD CODE"></spring:message><font
										color="red">*</font></label>
								</div>
								<div class="control-group col-md-3">
									<div class="controls" style="margin-top: 11%;">
										<form:input path="isdCode" cssClass="form-control"
											maxlength="5" />
										<FONT color="red"> <form:errors path="isdCode" /></FONT>
									</div>
								</div>
								<div class="control-group col-md-3" style="margin-top: 1.5%;">
									<label class="control-label" for="name"><spring:message
											code="LABEL_COUNTRY_MOBILENUM_LENGTH"
											text="Mobile Number Length"></spring:message><font
										color="red">*</font></label>
								</div>
								<div class="control-group col-md-3">
									<div class="controls" style="margin-top: 11%;">
										<form:input path="mobileNumberLength" cssClass="form-control"
											maxlength="2" />
										<FONT color="red"> <form:errors
											path="mobileNumberLength" /></FONT>
									</div>
								</div>
								<div class="control-group col-md-2" style="margin-top: 2%;">
									<label class="control-label" for="name"><spring:message
											code="LABEL_CURRENCY" text="Currency">
											<font color="red">*</font>
										</spring:message></label>
								</div>
								<div class="control-group col-md-1" style="margin-top: 2.3%;">
									<form:select path="currencyId" cssClass="dropdown chosen-select"
										id="currencyId" style="width:225px;">
										<form:option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
										</form:option>
										<form:options items="${currencyList}" itemLabel="currencyName"
											itemValue="currencyId"></form:options>
									</form:select>
									<font color="red"> <form:errors path="currencyId" /></font>
								</div>
							</div>
							<c:choose>
								<c:when test="${(countryDTO.countryId eq null) }">
									<c:set var="buttonName" value="LABEL_ADD" scope="page" />
								</c:when>
								<c:otherwise>
									<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
								</c:otherwise>
							</c:choose>
							<div class="col-sm-6 col-sm-offset-10">
								<div class="btn-toolbar">
									<input type="button" class="btn-primary btn" id="submitButton"
													value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
													onclick="validate();"></input>
									<c:if test="${countryDTO.countryId ne null}">
									<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
														onclick="cancelForm();" />
									</c:if>
								</div>
							</div>
				</div>
					</authz:authorize>
				</fieldset>
			</div>
		</div>
		<div class="col-lg-12">
			<div class="box">
				<div class="box-body">
					<div class="box-body table-responsive">
						
				<%-- @Commented by Vinod joshi, Reason:- Not Required.
				 
				 <div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
                	<span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span>
					<span style="float:right; margin-right: 5px;">
						<a href="#" onclick="javascript:locationsExcel();" style="text-decoration:none; margin-left: -1px;"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
							<span><spring:message code="LABEL_EXCEL" text="Excel"/></span>
						</a>
					</span>
					
					<span style="margin-right: 30px; float:right">
						<a href="#" style="text-decoration:none; margin-left: 8px;" onclick="javascript:locationsPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
						<span><spring:message code="LABEL_PDF" text="PDF"/></span>
						</a>
					</span>					
   			   </div> --%>
						
						
						<table id="example1" class="table table-bordered table-striped"
							style="text-align: center;">
							<thead>
								<tr>
									<th><spring:message code="LABEL_COUNTRYNAME"
											text="Country Name" /></th>
									<th><spring:message code="LABEL_COUNTRY_CODEALPHA2"
											text="Country Code Alpha 2" /></th>
									<th><spring:message code="LABEL_COUNTRY_ISDCODE"
											text="ISD CODE" /></th>
									<th><spring:message code="LABEL_COUNTRY_MOBILENUM_LENGTH"
											text="Mobile Number Length" /></th>
									<th><spring:message code="LABEL_CURRENCY" text="Currency" /></th>
									<%-- <th><spring:message code="TITLE_PROVENIENCE=Provenience" text="Provenience" /></th> --%>
									<th><spring:message code="LABEL_CITY" text="City"></spring:message></th>
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</tr>
							</thead>
							<tbody>
								<%
									int i = 0;
								%>
								<c:forEach items="${page.results}" var="country">
									<tr>
										<td><c:forEach
												items="${country.countryNames}" var="cname">
												<c:if test="${cname.comp_id.languageCode==lang }">
													<c:out value="${cname.countryName}" />
												</c:if>
											</c:forEach></td>
										<td><c:out
												value="${country.countryCodeAlpha2}" /></td>
										<td><c:out value="${country.isdCode}" /></td>
										<td><c:out
												value="${country.mobileNumberLength}" /></td>
										<td><c:out
												value="${country.currency.currencyName}" /></td>
										<%-- <td><a
											href="javascript:submitForm('countryForm','viewProvenience.htm?countryId=<c:out value="${country.countryId}"/>')">
												<spring:message code="LABEL_VIEWPROVENIENCE" text="viewprovinces"></spring:message>
										</a></td> --%>
										<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
										<td><%-- <a
											href="javascript:submitForm('countryForm','viewCities.htm?countryId=<c:out value="${country.countryId}"/>')">
												<spring:message code="LABEL_VIEWCITIES" text="View Cities"></spring:message> --%>
												<a
											href="javascript:countryDetail('viewCities.htm','<c:out value="${country.countryId}"/>')">
												<spring:message code="LABEL_VIEWCITIES" text="View Cities"></spring:message>
										</a> </td>
										<td><authz:authorize
												ifAnyGranted="ROLE_editLocationsAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('countryForm','editCountry.htm?countryId=<c:out value="${country.countryId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" />
												</a> --%>
												 <a
													href="javascript:countryDetail('editCountry.htm','<c:out value="${country.countryId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" />
												</a>
												<!-- End -->
												</authz:authorize>
												<authz:authorize
												ifNotGranted="ROLE_editLocationsAdminActivityAdmin">
												 <spring:message code="LABEL_NA" text="--NA--" />
												</authz:authorize>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</form:form>

</body>
</html>
