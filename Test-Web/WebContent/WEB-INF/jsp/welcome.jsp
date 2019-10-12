<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%-- <title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title> --%>
<style>
img.ui-datepicker-trigger {
    margin-left: 183px;
    margin-top: -55px;
}
</style>
<script type="text/javascript">
	var Alertmsg = {
		"validToDate" : '<spring:message code="VALID_TODATE" text="Please Enter Valid To Date"/>',
		"fromDateFormat" : "<spring:message code="VALID_ACCOUNT_REPORT_FROM_DATE_FORMAT" text="From date format must be : dd/mm/yyyy"/>",
		"toDateFromat" : "<spring:message code="VALID_ACCOUNT_REPORT_TO_DATE_FORMAT" text="To date format must be : dd/mm/yyyy"/>",
		"validFromDay" : "<spring:message code="VALID_ACCOUNT_REPORT_FROM_DATE" text="Please enter valid from date"/>",
		"validToDay" : "<spring:message code="VALID_TO_DAY" text="Please  enter  a valid to date"/>",
		"validFromMonth" : "<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		"validToMonth" : "<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		"validfDay" : "<spring:message code="VALID_FROM_DAY" text="Please  enter  a valid  days in from date"/>",
		"validTodays" : "<spring:message code="VALID_TO_DAYS" text="Please  enter  a valid  days in to date"/>",
		"validFMonth" : "<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		"validToMonth" : "<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
	};
	function check() {
		Zapatec.Calendar.setup({
			firstDay : 1,
			timeFormat : "12",
			electric : false,
			inputField : "fromDate",
			button : "trigger",
			ifFormat : "%d/%m/%Y",
			daFormat : "%Y/%m/%d",
			timeInterval : 01
		});

	}

	function check1() {
		Zapatec.Calendar.setup({
			firstDay : 1,
			timeFormat : "12",
			electric : false,
			inputField : "toDate",
			button : "trigger1",
			ifFormat : "%d/%m/%Y",
			daFormat : "%Y/%m/%d",
			timeInterval : 01
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
 	function submitWelcomeForm() {
		 var fromDate=document.getElementById("fromDate").value;
         var toDate=document.getElementById("toDate").value;
         
		if(document.txnSummaryform.fromDate.value==""){
			alert("<spring:message code='LABEL.FROMDATE' text='Please enter From Date'/>");
	 	     return false;
		}
		else if(document.txnSummaryform.toDate.value==""){
			alert("<spring:message code='LABEL.TODATE' text='Please enter To Date'/>");
	 	     return false;
		}
		if (!compareDate(fromDate)){
	     	   alert("<spring:message code='LABEL.FROMDATE.TODAY' text='To date should be less than or equal to current date'/>");
	   	   return false;
	   	 }else if (!compareDate(toDate)){
	      	   alert("<spring:message code='LABEL.TODATE.TODAY' text='To date should be less than or equal to current date'/>");
	   	   return false;
	   	 }
		if (document.txnSummaryform.fromDate.value != ""
				&& document.txnSummaryform.toDate.value != "") {

			var date1 = document.txnSummaryform.fromDate.value;
			var date2 = document.txnSummaryform.toDate.value;
			date1 = date1.split("/");
			date2 = date2.split("/");
			var sDate = new Date(date1[2] + "/" + date1[1] + "/" + date1[0]);
			var eDate = new Date(date2[2] + "/" + date2[1] + "/" + date2[0]);
			var daysApart = Math.round((sDate - eDate) / 86400000);
			
			
			if (!validdate(document.txnSummaryform.fromDate.value,
					document.txnSummaryform.toDate.value)) {
				return false;

			} else if (daysApart > 0) {
				document.txnSummaryform.toDate.focus();
				alert(Alertmsg.validToDate);
				return false;
			} else {
				document.txnSummaryform.action = "txnSummary.htm";
				document.txnSummaryform.submit();
			}
		}

		if (document.txnSummaryform.fromDate.value != "") {

			if (!validFromDate(document.txnSummaryform.fromDate.value)) {
				return false;

			} else {
				document.txnSummaryform.action = "txnSummary.htm";
				document.txnSummaryform.submit();
			}
		}
		if (document.txnSummaryform.toDate.value != "") {
			if (!validToDate(document.txnSummaryform.toDate.value)) {
				return false;

			} else {
				document.txnSummaryform.action = "txnSummary.htm";
				document.txnSummaryform.submit();
			}
		}
		document.txnSummaryform.action = "txnSummary.htm";
		document.txnSummaryform.submit();

	} 
	
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

		if ((month == 2 && day > februarycheck(year)) || day > monthdays[month]) {

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

		if ((month == 2 && day > februarycheck(year)) || day > monthdays[month]) {

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

		if ((month == 2 && day > februarycheck(year)) || day > monthdays[month]) {

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
</script>
</head>
<body>
	<div class="row">
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header" style="left: 400px; width: 300px">
					<h3 class="box-title">
						<span><spring:message code="LABEL_TITLE_WELCOME"
								text=" Welcome to m-GURUSH Wallet Services" /></span>
					</h3>
				</div>
				<br />
				<div
					style="color: #ba0101; font-weight: bold; font-size: 12px; left: 420px; width: 300px"
					class="box-header">
					<spring:message code="${message}" text="" />
				</div>
				<!-- /.box-header -->
				<div class="box-body">
					<!-- form start -->
					<form:form name="txnSummaryform" commandName="txnSummaryDTO">
			
						<authz:authorize
							ifNotGranted="ROLE_superadmin,ROLE_parameter,ROLE_audit,ROLE_accounting">
							<div class="row">
								<div class="col-sm-6">
									<label class="col-sm-4"><spring:message
											code="LABEL_BANKNAME" text="Bank Name" /></label> &nbsp; &nbsp;
									<form:select path="bankId" cssClass="dropdown chosen-select">
										<form:option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
										</form:option>
										<form:options items="${masterData.bankList}"
											itemLabel="bankName" itemValue="bankId" />
									</form:select>
								</div>
								<div class="col-sm-6">
									<label class="col-sm-4"><spring:message
											code="LABEL_COUNTRY" text="Country" /></label> &nbsp; &nbsp;
									<form:select path="countryId" cssClass="dropdown chosen-select">
										<form:option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
										</form:option>

										<c:forEach items="${masterData.countryList}" var="country">
											<c:forEach items="${country.countryNames}" var="cNames">
												<c:if test="${cNames.comp_id.languageCode==lang}">
													<form:option value="${country.countryId}"
														label="${cNames.countryName}">
													</form:option>
												</c:if>
											</c:forEach>
										</c:forEach>

									</form:select>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<label class="col-sm-4"><spring:message code="LABEL_FROM_DATE" text="From" /><font color="red">*</font></label>
									<div class="col-sm-5">
										<fmt:formatDate value="${txnSummaryDTO.fromDate}" type="date" pattern="dd/MM/yyyy" var="theFormattedDate" />
											<form:input type="text" path="fromDate" cssClass="form-control datepicker" value="${theFormattedDate}"/>
									</div>
								</div>
								
								<div class="col-sm-6">
									<label class="col-sm-4"><spring:message code="LABEL_TO_DATE" text="To" /><font color="red">*</font></label> 
									<div class="col-sm-5">
										<fmt:formatDate value="${txnSummaryDTO.toDate}" type="date" pattern="dd/MM/yyyy" var="theFormattedDate" />
											<form:input type="text" path="toDate" cssClass="form-control datepicker" value="${theFormattedDate}"/>									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-6">
									<label class="col-sm-4"><spring:message code="LABEL_TRANSACTION_TYPE"
								text="Transaction Type" /></label> 
								<div class="col-sm-4">
								<select class="dropdown chosen-select" id="transactionType" name="transactionType"  class="dropdown" >
												<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
												
													<c:set var="lang" value="${language}"></c:set>
													
														<c:forEach items="${masterData.transTypeList}" var="transTypeList">
														<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
													<%-- 	<c:if test="${transTypeList.transactionType!=84 }"> <<bug no:5294, Author:Vineeth Reddy, 20/06/2018>>
													 As, these are not financial transactions for which transactionTypes 0,5,70,75,101,102,103,104 drop down values will be hidden from this screen.  --%>
															 <c:if test="${(transTypeList.transactionType!=84) && (transTypeList.transactionType!=0)&& (transTypeList.transactionType!=104) && (transTypeList.transactionType!=101) && (transTypeList.transactionType!=102) && (transTypeList.transactionType!=103)&& (transTypeList.transactionType!=5) && (transTypeList.transactionType!=70) && (transTypeList.transactionType!=75)}"> 
																	 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
																	 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${transTypeList.transactionType eq txnSummaryDTO.transactionType}" >selected=true</c:if>>
																	<c:out 	value="${transactionTypesDescs.description}"/>	
																	</option>
																	</c:if>		
																	</c:if>  						
														</c:forEach>
														</c:forEach>
														</option>
													</select>		
								</div>
								</div>
								<%-- <authz:authorize ifAnyGranted ="ROLE_bankadmin,ROLE_bankteller">
								<spring:message code="LABEL_COUNTRY" text="Country" /></td>
							<td><form:select path="countryId" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
														
								<c:forEach items="${masterData.countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang}">
											<form:option value="${country.countryId}"  label="${cNames.countryName}"> 
											</form:option>
										</c:if>
									</c:forEach>
								</c:forEach>
								
							</form:select>
							</authz:authorize> --%>
							</div>
						</authz:authorize>
						<!-- /.box-body -->
							
						<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
								value="<spring:message code="LABEL_SEARCH" text="Search"/>"
								onclick="submitWelcomeForm();"></input>
							<br/> <br/>
						
						</div>
					</form:form>
				</div>
				<!-- /.box-body -->
			</div>
			<!-- /.box -->
		</div>
	</div>
	</div>


<c:out value="${txnSummaryDTO.customerName} "></c:out>


	<%-- 
<table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table width="1003" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="159" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><jsp:include page="left.jsp" /></td>
					</tr>

				</table>
				</td>
				<td width="844" align="left" valign="top">
				<table width="98%" border="0" height="40px" align="center"
					cellpadding="0" cellspacing="0">
					<tr>
						<td align="center" class="headding" width="25%"
							style="font-size: 15px; font-weight: bold;" valign="top"><spring:message
							code="LABEL_TITLE_WELCOME" text=" Welcome to GIM Mobile Services" />
						</td>
					</tr>
					<tr height="20px">
						<td align="center" colspan="2">
						<div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
							code="${message}" text="" /></div> 
						</td>
					</tr>
				</table>
				<form:form name="txnSummaryform" commandName="txnSummaryDTO">
					<table
						style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
						align="center" width="98%" cellpadding="4" cellspacing="4">
						<tr>
						<authz:authorize ifNotGranted="ROLE_bankadmin,ROLE_bankteller,ROLE_superadmin,ROLE_parameter,ROLE_audit,ROLE_accounting">
							<td><spring:message code="LABEL_BANKNAME" text="Bank Name" /></td>
							<td><form:select path="bankId" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${masterData.bankList}"
									itemLabel="bankName" itemValue="bankId" />
							    </form:select></td>
							<td><spring:message code="LABEL_COUNTRY" text="Country" /></td>
							<td><form:select path="countryId" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
														
								<c:forEach items="${masterData.countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang}">
											<form:option value="${country.countryId}"  label="${cNames.countryName}"> 
											</form:option>
										</c:if>
									</c:forEach>
								</c:forEach>
								
							</form:select></td>
						</authz:authorize>
						
						</tr>
						<tr>
							<td><spring:message code="LABEL_FROM_DATE" text="From" /></td>
							<td><form:input path="fromDate" /> <img
								style="" src="<%=request.getContextPath()%>/images/calendar.gif"
								id="trigger" alt="Calendar" title="Calendar" align="top"
								onClick="check()" /></td>
								
							<td><spring:message code="LABEL_TO_DATE" text="To" /></td>
							<td><form:input path="toDate"  /> <img
								style="" src="<%=request.getContextPath()%>/images/calendar.gif"
								id="trigger1" alt="Calendar" title="Calendar" align="top"
								onClick="check1()" /></td>	
						</tr>
						<tr>
						   <td><spring:message code="LABEL_TRANSACTION_TYPE"
								text="Transaction Type" /></td>
							<td>
							<select class="dropdown" id="transactionType" name="transactionType"  class="dropdown" >
												<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
												
													<c:set var="lang" value="${language}"></c:set>
													
														<c:forEach items="${masterData.transTypeList}" var="transTypeList">
														<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
														<c:if test="${transTypeList.transactionType!=84 }">
																	 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
																	 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${transTypeList.transactionType eq txnSummaryDTO.transactionType}" >selected=true</c:if>>
																	<c:out 	value="${transactionTypesDescs.description}"/>	
																	</option>
																	</c:if>		
																	</c:if>  						
														</c:forEach>
														</c:forEach>
														</option>
													</select>
													
							
							<form:select path="transactionType" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${masterData.transTypeList}"
									itemLabel="description" itemValue="transactionType" />
							</form:select>
							
							</td>
							
						<authz:authorize ifAnyGranted ="ROLE_bankadmin,ROLE_bankteller">	
							<td><spring:message code="LABEL_COUNTRY" text="Country" /></td>
							<td><form:select path="countryId" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
														
								<c:forEach items="${masterData.countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang}">
											<form:option value="${country.countryId}"  label="${cNames.countryName}"> 
											</form:option>
										</c:if>
									</c:forEach>
								</c:forEach>
								
							</form:select></td>
					  </authz:authorize>		
						</tr>
						<tr>
							<td align="right" colspan="4"><input type="button"
								value="<spring:message code="LABEL_SEARCH" text="Search"/>"
								onclick="submitForm();"></input></td>
						</tr>

					</table>
				</form:form></td>
			</tr>

		</table>

		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table> --%>

</body>
<script type="text/javascript">
	window.onload = function() {
		check();
		check1();
	};
</script>
</html>
