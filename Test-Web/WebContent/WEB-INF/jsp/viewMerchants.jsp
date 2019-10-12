<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="org.apache.poi.hssf.record.formula.functions.Count"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
	<head>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/zapatec.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
	    <script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>	  
        <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>
	<style type="text/css">
	<!--
	.style1 {
	color: #FFFFFF;
	font-weight: bold;
	}
	-->
	</style>
	<script type="text/javascript">
	
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
			 "validToMonth":"<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
			 
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
 	
function compareDate(from){
 		
		var dt2  = parseInt(from.substring(0,2),10);
		var mon2 = parseInt(from.substring(3,5),10)-1;
	    var yr2  = parseInt(from.substring(6,10),10);
	  
	    	var toDate = new Date(yr2, mon2, dt2); 	    
		    var currentDate = new Date();
		    var month = currentDate.getMonth();
		    var day = currentDate.getDate();
		    var year = currentDate.getFullYear();
		    var cdate=new Date(year,month,day);
		    var compDate=cdate-toDate;		    
			if(compDate>= 0)
				return true;
			else
				return false;

	}	
    function validate(){
    	
	    	var mobileNumber = document.getElementById("mobileNumber").value;
			var customerName = document.getElementById("merchantName").value;
			var pattern = '^\[a-zA-ZÀ-ÿ0-9\ ]*$';

			if (isNaN(mobileNumber)) {
				alert('<spring:message code="MOBILENUM_CHARACTER" text="Enter a valid mobile number"/>');
				return false;
			}else if(mobileNumber!="" && mobileNumber.length!=10){
				alert("Mobile Number lenght should be 10");
				return false;
			}else if (customerName.search(pattern) == -1) {
				alert('<spring:message code="ALERT_CUSTOMER_NAME" text="Please enter a valid name"/>');
				return false;
			} else if (customerName.search(pattern) == -1) {
				alert('<spring:message code="ALERT_CUSTOMER_NAME" text="Please enter a valid name"/>');
				return false;
			}

			<authz:authorize ifAllGranted="ROLE_admin">
			var bankId = document.getElementById("bankId").value;
			var branchId = document.getElementById("branchId").value;
			/*  if(bankId != ""){
			 	if(branchId =="" || branchId =="select"){
			 		 alert('<spring:message code="VALID_EMPTY_BANK_BRANCH" text="Please select the branch"/>');
			          return false;
			 	}
			 } */
			</authz:authorize>
			document.merchantViewForm.submit();
		}

		$(document).ready(function() {

			$("#bankId").change(function() {
				$bankId = document.getElementById("bankId").value;
				$csrfToken = $("#csrfToken").val();
				$.post("getBranches.htm", {
					bankId : $bankId,
					csrfToken : $csrfToken
				}, function(data) {
					document.getElementById("branchdiv").innerHTML = "";
					document.getElementById("branchdiv").innerHTML = data;
					setTokenValFrmAjaxResp();
					applyChosen();
				});

			});

		});

		var dminyear = 1900;
		var dmaxyear = 2200;
		var chsep = "/";
		function checkinteger(str1) {
			var x;
			for (x = 0; x < str1.length; x++) {

				var cr = str1.charAt(x);
				if (((cr < "0") || (cr > "9")))
					return false;
			}
			return true;
		}
		function getcharacters(s, chsep1) {
			var x;
			var Stringreturn = "";
			for (x = 0; x < s.length; x++) {
				var cr = s.charAt(x);
				if (chsep.indexOf(cr) == -1)
					Stringreturn += cr;
			}
			return Stringreturn;
		}
		function februarycheck(cyear) {
			return (((cyear % 4 == 0) && ((!(cyear % 100 == 0)) || (cyear % 400 == 0))) ? 29
					: 28);
		}
		function finaldays(nr) {
			for ( var x = 1; x <= nr; x++) {
				this[x] = 31;
				if (x == 4 || x == 6 || x == 9 || x == 11) {
					this[x] = 30
				}
				;
				if (x == 2) {
					this[x] = 29
				}
				;
			}
			return this;
		}

		function validdate(fromDate, toDate) {

			if (dtvalid(fromDate, toDate) == false) {

				return false;
			}
			return true;
		}

		function dtvalid(fromDate, toDate) {
			//var DateOfBirth=new String("12/912/2012");
			var monthdays = finaldays(12);
			var fChar1 = fromDate.charAt(2);
			var fChar2 = fromDate.charAt(5);
			var chsep = "/";
			var fcpos1 = fromDate.indexOf(chsep);
			var fcpos2 = fromDate.indexOf(chsep, fcpos1 + 1);

			if (fcpos1 == -1 || fcpos2 == -1) {

				alert(Alertmsg.fromDateFormat);
				return false;
			}

			if (fromDate.length<10 || fromDate.length>10) {

				alert(Alertmsg.fromDateFormat);
				return false;
			}
			/*if ( Char1 =='/' && Char2 == '/' )
			{
			 alert ('Please check foramt');
			return false;
			}*/

			var day;
			var month;
			var year;

			day = fromDate.substring(0, 2);
			month = fromDate.substring(3, 5);
			year = fromDate.substring(6, 10);

			var tChar1 = toDate.charAt(2);
			var tChar2 = toDate.charAt(5);
			var chsep = "/";
			var tcpos1 = toDate.indexOf(chsep);
			var tcpos2 = toDate.indexOf(chsep, tcpos1 + 1);
			if (tcpos1 == -1 || tcpos2 == -1) {
				alert(Alertmsg.toDateFromat);
				return false;
			}

			if (fromDate.length<10 || fromDate.length>10) {

				alert(Alertmsg.fromDateFormat);
				return false;
			}
			/*if ( Char1 =='/' && Char2 == '/' )
			{
			 alert ('Please check foramt');
			return false;
			}*/

			if (!validDay(day)) {
				alert(Alertmsg.validFromDay);

				return false;
			}

			if (!validMonth(month)) {
				alert(Alertmsg.validFromMonth);

				return false;
			}

			if ((month == 2 && day > februarycheck(year))
					|| day > monthdays[month]) {

				alert(Alertmsg.validFromDay);
				return false;
			}

			var tday;
			var tmonth;
			var tyear;

			tday = toDate.substring(0, 2);
			tmonth = toDate.substring(3, 5);
			tyear = toDate.substring(6, 10);

			if (toDate.length<10 || toDate.length>10) {

				alert(Alertmsg.toDateFromat);
				return false;
			}

			if (!validDay(tday)) {
				alert(Alertmsg.validToDay);

				return false;
			}

			if (!validMonth(tmonth)) {
				alert(Alertmsg.validToMonth);

				return false;
			}

			if ((tmonth == 2 && tday > februarycheck(tyear))
					|| tday > monthdays[tmonth]) {

				alert(Alertmsg.validToDay);
				return false;
			}

			/*if(!validYear(year)){
					
			alert('Invalid year in date of birth');
			DateOfBirth=null;
			return false;
			}*/

		} // end func

		function validDay(day) {

			if (IsNumeric(day) && day.length < 3) {
				if (day > 0 && day < 32) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		}// end func
		function IsNumeric(sText) {

			var ValidChars = "0123456789.";
			var IsNumber = true;
			var Char;

			for ( var i = 0; i < sText.length && IsNumber == true; i++) {
				Char = sText.charAt(i);
				if (ValidChars.indexOf(Char) == -1) {
					IsNumber = false;
				}
			}

			return IsNumber;
		} // end func

		function validMonth(month) {
			if (IsNumeric(month)) {
				if (month > 0 && month < 13) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}// end func

		function validYear(year) {
			var d = new Date();
			var currentYear = d.getFullYear();

			if (year.length != 4) {
				return false;
			}

			if (IsNumeric(year)) {
				if (year > 0 && year <= currentYear) {
					return true;
				} else {
					return false;
				}

			} else {
				return false;
			}

		}
		function validToDate(toDate) {

			if (validateToDate(toDate) == false) {

				return false;
			}
			return true;
		}
		function validateToDate(toDate) {
			//var DateOfBirth=new String("12/912/2012");
			var monthdays = finaldays(12);
			var Char1 = toDate.charAt(2);
			var Char2 = toDate.charAt(5);
			var chsep = "/";
			var fcpos1 = toDate.indexOf(chsep);
			var fcpos2 = toDate.indexOf(chsep, fcpos1 + 1);
			if (fcpos1 == -1 || fcpos2 == -1) {

				alert(Alertmsg.toDateFromat);
				return false;
			}

			if (toDate.length<10 || toDate.length>10) {

				alert(Alertmsg.toDateFromat);
				return false;
			}
			/*if ( Char1 =='/' && Char2 == '/' )
			{
			 alert ('Please check foramt');
			return false;
			}*/

			var day;
			var month;
			var year;

			day = toDate.substring(0, 2);
			month = toDate.substring(3, 5);
			year = toDate.substring(6, 10);

			if (!validDay(day)) {
				alert(Alertmsg.validToDay);
				return false;
			}

			if (!validMonth(month)) {
				alert(Alertmsg.validToMonth);

				return false;
			}

			if ((month == 2 && day > februarycheck(year))
					|| day > monthdays[month]) {

				alert(Alertmsg.validToDay);
				return false;
			}
			/*if(!validYear(year)){
					
			alert('Invalid year in date of birth');
			DateOfBirth=null;
			return false;
			}*/

		} // end func
		function validFromDate(fromdate) {

			if (validateFromDate(fromdate) == false) {

				return false;
			}
			return true;
		}
		function validateFromDate(fromDate) {
			//var DateOfBirth=new String("12/912/2012");
			var monthdays = finaldays(12);
			var Char1 = fromDate.charAt(2);
			var Char2 = fromDate.charAt(5);
			var chsep = "/";
			var fcpos1 = fromDate.indexOf(chsep);
			var fcpos2 = fromDate.indexOf(chsep, fcpos1 + 1);
			if (fcpos1 == -1 || fcpos2 == -1) {

				alert(Alertmsg.fromDateFormat);
				return false;
			}

			if (fromDate.length<10 || fromDate.length>10) {

				alert(Alertmsg.fromDateFormat);
				return false;
			}
			/*if ( Char1 =='/' && Char2 == '/' )
			{
			 alert ('Please check foramt');
			return false;
			}*/

			var day;
			var month;
			var year;

			day = fromDate.substring(0, 2);
			month = fromDate.substring(3, 5);
			year = fromDate.substring(6, 10);

			if (!validDay(day)) {
				alert(Alertmsg.validFromDay);
				DateOfBirth = null;
				return false;
			}

			if (!validMonth(month)) {
				alert(Alertmsg.validFromMonth);
				DateOfBirth = null;
				return false;
			}

			if ((month == 2 && day > februarycheck(year))
					|| day > monthdays[month]) {

				alert(Alertmsg.validFromDay);
				return false;
			}
			/*if(!validYear(year)){
					
			alert('Invalid year in date of birth');
			DateOfBirth=null;
			return false;
			}*/

		} // end func
		function validFromDate(fromdate) {

			if (validateFromDate(fromdate) == false) {

				return false;
			}
			return true;
		}
		
		function addMerchant(url){
			submitlink(url,'merchantViewForm');
		}
	</script>
	</head>
<body onload="check(),check1()">
<form:form id="merchantViewForm" name="merchantViewForm" class="form-inline" method="post"  action="searchMerchant.htm">
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_VIEW_MERCHANTS" text="View Merchants"></spring:message></span>
		</h3>
	</div>
		<div class="col-sm-6">
			<div class="col-sm-6 col-sm-offset-10" style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div>
		</div>
	<div class="col-md-4 col-md-offset-10">
	<authz:authorize ifAnyGranted="ROLE_addMerchantAdminActivityAdmin">
		<%-- <a href="javascript:submitForm('merchantViewForm','showMerchantForm.htm')"><strong><spring:message code="LABEL_ADD_MERCHANT" text="Add Merchant"/></strong></a> --%>
	<input type="button" class="btn btn-primary" value="<spring:message code="LABEL_ADD_MERCHANT" text="Add Merchant"/>"
							onclick="javascript:addMerchant('showMerchantForm.htm')" /> 
	
	</authz:authorize>
	</div><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"></spring:message></label> 
					<input id="mobileNumber" maxlength="10" name="mobileNumber"  type="text" value="<c:out value="${mobileNumber}"/>" class="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MERCHANT_NAME" text="Merchant Name"/></label> 
					<input id="merchantName" name="merchantName" type="text" value="<c:out value="${merchantName}" />" class="form-control" />
				</div>
			</div>
			<div class="row">
			<authz:authorize ifAllGranted="ROLE_superadmin">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BRANCH" text="Branch"/></label> 
					<select id="branchId" class="dropdown_big" name="branchId">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.branchList}" var="branches">
							<option value=<c:out value="${branches.branchId}" />  <c:if test="${branches.branchId eq branchId}" >selected=true</c:if> ><c:out value="${branches.location}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_admin">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big chosen-select" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value=<c:out value="${bankGroup.bankGroupId}" /> <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big chosen-select" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value=<c:out value="${bankGroup.bankGroupId}" /> <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_gimbackofficeexec">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big chosen-select" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value=<c:out value="${bankGroup.bankGroupId}" /> <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_gimsupportlead">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big chosen-select" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value=<c:out value="${bankGroup.bankGroupId}" /> <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_gimsupportexec">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big chosen-select" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value=<c:out value="${bankGroup.bankGroupId}" /> <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
				<div class="col-sm-6">
					<%-- <label class="col-sm-5"><spring:message code="LABEL_COUNTRY" text="Country"/></label> 
					<select class="dropdown_big chosen-select" id="countryId" name="countryId">
                       <option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" /></option>
	                     <c:set var="lang" value="${language}"></c:set>
				            <c:forEach items="${masterData.countryList}" var="country">
				            <c:forEach items="${country.countryNames}" var="cNames">
							   <c:if test="${cNames.comp_id.languageCode==lang }">
							     <option value="<c:out value="${country.countryId}"/>"  <c:if test="${country.countryId eq countryId}" >selected=true</c:if> > 
							        <c:out value="${cNames.countryName}"/>	
						         </option>
							  </c:if>										
				            </c:forEach>
				            </c:forEach>														
			       </select> --%>
			       <label class="col-sm-5"><spring:message code="LABEL_COUNTRY" text="Country"/></label>
							<label class="col-sm-5"><spring:message code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
				</div>
			</div>
			<div class="row">
			<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin,ROLE_gimbackofficelead,ROLE_gimbackofficeexec,ROLE_gimsupportlead,ROLE_gimsupportexec">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK" text="Bank"/></label> 
					<select name="bankId" class="dropdown_big chosen-select" id="bankId">
	                	<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
	                	<c:forEach items="${masterData.bankList}" var="banks">
						<option value=<c:out value="${banks.bankId}" /> <c:if test="${banks.bankId eq bankId}" >selected=true</c:if> ><c:out value="${banks.bankName}" /> </option>
						</c:forEach>
	                </select>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BRANCH" text="Branch"/></label> 
					<div id="branchdiv">
						<select id="branchId" class="dropdown_big chosen-select" name="branchId">
							<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
							<c:forEach items="${masterData.branchList}" var="branches">
								<option value=<c:out value="${branches.branchId}" /> <c:if test="${branches.branchId eq branchId}" >selected=true</c:if>><c:out value="${branches.location}" /> </option>
							</c:forEach>
						</select>
					</div>
				</div>
				<%-- <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_FROMDATE" text="From" /></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TODATE" text="To" /></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="row">
				</div><br/><br/> --%>
			</authz:authorize>
			</div>
			<%-- <div class="row">
			<authz:authorize ifAllGranted="ROLE_ccexec,ROLE_bankteller,ROLE_parameter,ROLE_audit,ROLE_bankadmin,ROLE_informationdesk,
			ROLE_accounting,ROLE_operation,ROLE_supportbank,ROLE_branchmanager,ROLE_relationshipmanager,ROLE_personalrelationexec">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
			</div> --%>
			
			<input type="hidden" name="roleId" value="${roleId}"/>
			<input type="hidden" name="bankId" value="${bankId}"/>
			<div class="box-footer">
				<input type="button"  value="<spring:message code="LABEL_SEARCH"/>" class="btn btn-primary pull-right" onclick="validate();" />
				<br/><br/>
			</div>
		
		</div>
		</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_MERCHANT_NAME" text="Merchant Name"/></th>
								<th><spring:message code="LABEL_BANK" text="Bank"/></th>
								<th><spring:message code="LABEL_BRANCH" text="Branch"/></th>
								<th><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_STATUS" text="Status"/></th>
								<th><spring:message code="LABEL_COUNTRY" text="Country"/></th>
								<th><spring:message code="LABEL_CITY" text="City"/></th>
								<th><spring:message code="LABEL_ACTION_EDIT = Action" text="Action"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
					            <c:forEach items="${merchantList}" var="merchant">
					            <c:set var="j" value="${ j+1 }"></c:set>
					            <tr>
								    <td><c:out value="${merchant.merchantName}" /></td>
								    
								    <td><c:set var="count" value="0" scope="page" />
								    	<c:forEach items="${ merchant.terminals }" var="ter">
											<c:if test="${count==0}"><c:out value="${ter.bank.bankName}" /><br/></c:if>
											<c:set var="count" value="${count + 1}" scope="page"/>
										</c:forEach>			
										
										<%-- <c:out value="${merchant.terminals.bank.bankName}" />	 --%>			
													    
								    </td>	
								    <td><c:set var="count" value="0" scope="page" />
								    	<c:forEach items="${ merchant.terminals }" var="ter">
											<c:if test="${count==0}"><c:out value="${ter.branch.location}" /><br/></c:if>
											<c:set var="count" value="${count + 1}" scope="page"/>
										</c:forEach>												    
								    </td>	
									<td><c:out value="${merchant.primaryContactMobile}" /></td>		
									<c:if test="${merchant.active != 20}">
                                        <c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>
                                    </c:if>
                                    <c:if test="${merchant.active == 20}">
                                        <c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
                                    </c:if>
                                                <td><c:out value="${status}" /></td>		
									<td>	
										<c:forEach items="${merchant.country.countryNames}" var="cname">
										<c:if test="${cname.comp_id.languageCode==lang }">
										<c:out 	value="${cname.countryName}" />											
										</c:if>
										</c:forEach>
									</td>	
									<td><c:out value="${merchant.city.city}" /></td>	
									<td>
										<a href="javascript:submitForm('merchantViewForm','viewMerchant.htm?merchantId=<c:out value="${merchant.merchantId}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a>
									</td>
								</tr>
								</c:forEach>
						</tbody>
					</table>
				</div>
	</div>	
</div>
</form:form>
<script>
	window.onload=function(){
	check();
	;
	};
</script>
<script type="text/javascript">
var a ;
var arr=new Array();
a="<c:out value='${customerList}'/>";
var c=a.substring(a.indexOf ('[')+1,a.lastIndexOf(']'));

for(var i=0;i<c.split(",").length;i++){
	//arr[i]="\""+c.split(",")[i]+"\"";
	arr[i]=c.split(", ")[i];
	//alert(arr[i])
}
$("#customerName").autocomplete({source:arr});
  </script>
	</body>
</html>
