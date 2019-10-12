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

.grayBox {
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =       80);
}

.box_content {
	position: fixed;
	top: 27%;
	left: 21%;
	right: 40%;
	width: 67%;
	padding: 16px;
	z-index: 1002;
	overflow: auto;
}
td{
	padding-left: 5px;
}
.style2 {
	font-weight: bold
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
    	
    	
        var code = document.getElementById("customerCode").value;
        var mobileNumber = document.getElementById("customerMobileNo").value;
        var fromDate=document.getElementById("fromDate").value.trim();
        var toDate=document.getElementById("toDate").value.trim(); 
        var pattern='^\[1-9][0-9]*$';

        if(code!="" && code.search(pattern)==-1) {
	         alert('Please enter Valid Agent Code');
	         document.agentCashOutViewForm.agentCode.focus();
	         return false;
		 }else if(mobileNumber!="" && mobileNumber.search(pattern)==-1) {
	         alert('Please enter valid Mobile Number');
	         document.agentCashOutViewForm.message.focus();
	         return false;
		 }
        
        if(fromDate ==""){
      	  alert("<spring:message code='LABEL.FROMDATE' text='Please enter From Date'/>");
      	 return false;
       }else if(toDate ==""){
      	 alert("<spring:message code='LABEL.TODATE' text='Please enter To Date'/>");
      	 return false;
       }else if(!validdate(fromDate,toDate)){
      	 	return false;  
       }else if (!compareDate(fromDate)){
    	   alert("<spring:message code='LABEL.FROMDATE.TODAY' text='Please enter From Date Less than Todays date'/>");
	  	   return false;
	   }else if (!compareDate(toDate)){
	     	   alert("<spring:message code='LABEL.TODATE.TODAY' text='Please enter To Date Less than Todays date'/>");
	  	   return false;
	   }else if(!compareFromDate(fromDate,toDate)){
	  	   alert("<spring:message code='COMPARE_DATES' text='From Date should not be greater than To Date'/>");
	  	   return false;
	   }

			document.agentCashOutViewForm.action = "searchAgentCashOut.htm";
			document.agentCashOutViewForm.submit();
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
			for (var x = 1; x <= nr; x++) {
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

			for (var i = 0; i < sText.length && IsNumber == true; i++) {
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

		} // end func
		function validFromDate(fromdate) {

			if (validateFromDate(fromdate) == false) {

				return false;
			}
			return true;
		}
		
		function compareFromDate(fromdt,todt)
		{
			var fromdate=fromdt.split('/');
			var toDate=todt.split('/');
			var strFromDate=fromdate[0];
			var strFromMonth=fromdate[1];
			var strToDate=toDate[0];
			var strToMonth=toDate[1];

			if(parseInt(fromdate[0])<10)
			{
				strFromDate=fromdate[0].replace('0','');
			}
			if(parseInt(fromdate[1])<10)
			{
				strFromMonth=fromdate[1].replace('0','');
			}
			if(parseInt(toDate[0])<10)
			{
				strToDate=toDate[0].replace('0','');
			}
			if(parseInt(toDate[1])<10)
			{
				strToMonth=toDate[1].replace('0','');
			}
			var mon1 = parseInt(strFromMonth);
			var dt1 = parseInt(strFromDate);
			var yr1 = parseInt(fromdate[2]);

			var mon2 = parseInt(strToMonth);
			var dt2 = parseInt(strToDate);
			var yr2 = parseInt(toDate[2]); 
			var fromDate = new Date(yr1, mon1-1, dt1);
			var toDate = new Date(yr2, mon2-1, dt2);
			if( fromDate <= toDate )

				return true;
			else
				return false;

		}
	function clearForm(){
		document.agentCashOutViewForm.reset();
	}
	
	function cashOutDetail(url, transactionId) {
		document.getElementById('transactionId').value = transactionId;
		submitlink(url, 'agentCashOutViewForm');
	}

	function displayDepositDiv() {
		document.getElementById("depositDiv").style.visibility = "visible";
	}
	function onSubmit() {
		document.depositConf.submit();
	}

	function openNewWindow(url) {
		window.open(url, 'mGurush',
				'width=800,height=800,toolbar=1,resizable=0');
		return false;
	}
	function clearForm() {
		document.userRegistrationForm.reset();
	}
	function clearForm() {
		document.userRegistrationForm.reset();
	}
	function onSubmit() {
		document.accountDetails.submit();
	}
	function displayDepositDiv() {

		if (checkValidate() == true) {
			if (document.getElementById("customerId").value == "1") {
				document.getElementById("transactionDiv").style.visibility = "visible";
				displayHideBox("1");
			} else {
				alert("<spring:message code='VALID_CUSTOMER_NOT_EXIST' text='Customer Doesnt Exist'/>");
				return false;
			}
		}
	}
	function displayHideBox(boxNumber) {
		if (document.getElementById("LightBox" + boxNumber).style.display == "none") {
			document.getElementById("LightBox" + boxNumber).style.display = "block";
			document.getElementById("grayBG").style.display = "block";
		} else {
			document.getElementById("LightBox" + boxNumber).style.display = "none";
			document.getElementById("grayBG").style.display = "none";
		}
	}

	function printPage() {
		
		var mainContent = document.getElementById("LightBox1").innerHTML;
		var toPrint = '<table width="80%" align="center"><tr><td colspan="2"><img src="<%=request.getContextPath()%>/images/header_01.JPG" width="900" height="166" border="0" style="align:center"/></td></tr><tr><td colspan="2" align="center"><c:out value="${transactionReceipt.description}" /></td></tr><tr><td style="width:160px"></td><td>'
			+ mainContent + '</td></tr></table>';

        var popupWin = window.open('', '_blank', 'width=750,height=600,location=no,left=200px');

        popupWin.document.open();

        popupWin.document.write('<html><head><title>Transactions</title><style>#savePrint, #buttonId{display: none !important;}</style></head><body onload="window.print();window.close();">')

        popupWin.document.write(toPrint);

        popupWin.document.write('</html>');

        popupWin.document.close();
	}

</script>
</head>
<body onload="check(),check1();">
	<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  added commandName=customerDTO for customerDTO submision -->
	<form:form id="agentCashOutViewForm" name="agentCashOutViewForm" class="form-inline"
		commandName="transactionParamDTO" method="post">
		<jsp:include page="csrf_token.jsp" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title"><span><spring:message code="LABEL_AGENT_CASHOUT" text=" Agent CashOut"/></span></h3>
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
				<div class="row">
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_AGENT_CODE" text="Agent Code"/></label>
						<form:input id="customerCode" path="customerCode" type="text" maxlength="6" cssClass="form-control"/> 
					</div> 
 
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_AGENT_MO" text="Agent Mobile Number"/></label>
						<form:input id="customerMobileNo" path="customerMobileNo" type="text" maxlength="9" cssClass="form-control"/> 
					</div> 
				</div>
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly"  cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="fromDate" /></font>
					<form:hidden path="fromDate" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<form:input path="toDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="toDate" /></font>
					<form:hidden path="toDate"/>
				</div>
			</div>
			<br/>
			<div class="col-md-3 col-md-offset-10">
			<input type="button" id="submitButton" class="btn btn-primary" value="<spring:message code="LABEL_SEARCH" text="Search"/>" onclick="validate();"></input>
			<input type="button" class="btn btn-primary" value="<spring:message code="LABEL_RESET" text="reset"/>" onclick="clearForm();"></input>
			</div>
			</authz:authorize>
			<br/>
				</div>
			</div>
			<div class="box">
				<div class="box-body table-responsive">


				<%-- 	<div class="exprt"
						style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
						<span style="float: right; margin-right: 8px;"> <a href="#"
							onclick="javascript:transactionVolumeDetailsExcel();"
							style="text-decoration: none; margin-left: -11px"><img
								src="<%=request.getContextPath()%>/images/excel.jpg" /> </a>
						</span> <span style="margin-right: 30px; float: right"> <a
							href="#" style="text-decoration: none; margin-left: 11px"
							onclick="javascript:transactionVolumeDetailsPDF();"><img
								src="<%=request.getContextPath()%>/images/pdf.jpg" /> </a>
						</span>
					</div> --%>

					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="SI_NO" text="SI No"/></th>
								<th><spring:message code="LABEL_AGENT_NAME" text="Agent Name" /></th>		
								<th><spring:message code="LABEL_AGENT_MO" text="Agent Mobile Number" /></th>
								<th><spring:message code="LABEL_AGENT_CODE" text="Agent Code" /></th>	
								<th><spring:message code="LABEL_TXNDATE" text="Date and Time" /></th>
								<th><spring:message code="LABEL_AMOUNT" text="Amount" /></th>
								<th><spring:message code="LABEL_ACTION" text="Action" /></th>				
	
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
							<c:forEach items="${page.results}" var="agentCashOut">
								<c:set var="j" value="${ j+1 }"></c:set>
								<tr>
									<td><c:out value="${j}"/></td>
									<td><c:out value="${agentCashOut.name}" /></td>
									<td><c:out value="${agentCashOut.CustomerMobileNo}" /></td>
									<td><c:out value="${agentCashOut.CustomerCode}" /></td>	
									<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${agentCashOut.TransactionDate}" /></td>										
									<td><c:out value="${agentCashOut.Amount}" /></td>
									<td>
									<a href="javascript:cashOutDetail('validateAgentCashOut.htm','<c:out value="${agentCashOut.TransactionID}"/>')"><spring:message code="LABEL_CASHOUT" text=" Agent CashOut"/></a>
									</td>	
								</tr>
							</c:forEach>
						</tbody>
					</table>

				</div>
			</div>
		</div>
		<div id="transactionDiv"
		style="visibility: visible; position: relative;">

		<div id="grayBG" class="grayBox" style="display: block;"></div>
		<div width="800px" id="LightBox1" class="box_content"
			style="display: block;">
			<table cellpadding="3" height="300px;" cellspacing="0" width="90%"
				border="0">
				<tr align="left">
					<td colspan="2" bgcolor="#FFFFFF" style="padding: 10px;">
						<div onclick="displayHideBox('1');" style="cursor: pointer;"
							align="right" id="buttonId">
							<img src="images/delete_icon.jpg" alt="" width="9" height="9" />
							<br />
						</div>
						<table width="95%" border="0" align="center" cellpadding="0"
							cellspacing="0"
							style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #333333">

							<tr>
								<td width="100%" height="151" colspan="2" valign="top"><br />
									<table width="97%" align="center" cellpadding="3"
										cellspacing="3">
										<tr>
											<c:if test="${transactionReceipt.mobileNumber ne null && transactionReceipt.mobileNumber ne ''}">
											<td><spring:message code="LABEL_MOBILE_NO"
													text="Mobile Number:" />:</td>
											<td><c:out value="${transactionReceipt.mobileNumber}" /></td>
											</c:if>
										</tr>
										<tr>
											<td><spring:message code="LABEL_CUSTOMER_NAME"
													text="Customer Name:" />:</td>
											<td><c:out value="${transactionReceipt.customerName}" /></td>
										</tr>
										<tr>
											<td><spring:message code="LABEL_ACCOUNT_NO"
													text="Account No :" />:</td>
											<td><c:out value="${transactionReceipt.accountAlias}" /></td>
										</tr>
										<c:if
											test="${ transactionReceipt.transactionType == 95 || transactionReceipt.transactionType == 99 }">
											<tr>
												<td><spring:message code="LABEL_AMOUNT_FCFA"
														text="Amount [SSP] :" />:</td>
												<td>
												<fmt:formatNumber type = "number" minFractionDigits = "2" maxFractionDigits="2" value = "${transactionReceipt.amount}" />
												<%-- <c:out value="${transactionReceipt.amount}"></c:out> --%>
												</td>
											</tr>
										</c:if>
																			
						 					<c:if test="${ transactionReceipt.transactionType == 137 || transactionReceipt.transactionType == 138 || transactionReceipt.transactionType == 143}">
											<tr>
												<td><spring:message code="LABEL_AMOUNT_FCFA"
														text="Amount [SSP] :" />:</td>
												<td>
												<fmt:formatNumber type = "number" minFractionDigits = "2" maxFractionDigits="2" value = "${transactionReceipt.amount}" />
												<%-- <c:out value="${transactionReceipt.amount}"></c:out> --%>
												</td>
											</tr>
										</c:if> 
										<c:if
											test="${ transactionReceipt.transactionType != 35 && transactionReceipt.transactionType != 98}">
											<tr>
												<td><spring:message code="LABEL_AVAILABEL_BALANCE"
														text="Available Balance [SSP] :" />:</td>
												<td><c:out value="${transactionReceipt.balance}"></c:out>
												</td>
											</tr>
										</c:if>

										<tr>
											<td><spring:message code="LABEL_TXN_DATE"
													text="Txn Date:" />:</td>
											<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="<%=new java.util.Date()%>" /></td>
										</tr>

										<tr>
											<td><spring:message code="LABEL_DESCRIPTION"
													text="Description:" />:</td>
											<td><c:out value="${transactionReceipt.description}" />
											</td>
										</tr>
									</table> <c:if test="${ transactionReceipt.transactionType == 35 }">
										<table style="border: 1px solid #333333;" width="99.4%"
											cellpadding="3" cellspacing="3">
											<tr>
												<td><strong><spring:message
															code="LABEL_TRANSACTION_DATE" text="Tranction Date" /></strong></td>
												<td><strong><spring:message
															code="LABEL_DESCRIPTION" text="Description" /></strong></td>
												<td><strong><spring:message
															code="LABEL_AMOUNT" text="Amount" /> </strong></td>
												<td><strong><spring:message
															code="LABEL_TRANSACTION_TYPE" text="Tranction Type" /></strong></td>
											</tr>
											<c:forEach items="${transactionReceipt.txnList}" var="txn">
												<tr>
													<td><fmt:formatDate value="${txn.transDate.time}"
															pattern="dd/MM/yyyy" /></td>
													<td><c:out value="${txn.transDesc}"></c:out></td>
													<td style="float:right">
													<fmt:formatNumber type = "number" minFractionDigits = "2" maxFractionDigits="2" value = "${txn.amount}" />         
													<%-- <c:out value="${txn.amount}"></c:out> --%></td>
													<td style="text-align:center;"><c:out value="${txn.transType}"></c:out></td>
												</tr>
											</c:forEach>
										</table>
									</c:if> <c:if test="${ transactionReceipt.transactionType == 98 }">
										<table width="100%"
											cellpadding="3" cellspacing="3">
											<tr>
												<td align="center"><a
													href="generatePDFTxnStatement.htm"><b><spring:message
																code="LABEL_CLICK_DOWNLOAD" text="Click to download" /></b></a>
												</td>
											</tr>
										</table>
									</c:if> <c:if test="${ transactionReceipt.transactionType != 98 }">
										<table style="display: block;"
											width="100%" cellpadding="3" cellspacing="3" id="savePrint">
											<tr>
												<td align="left"><a href="saveTxnStatement.htm"><b><spring:message
																code="LABEL_SAVE" text="Save" /></b></a></td>
												<td align="right" style="width: 93%;">&nbsp;</td>
												<td><a href="javascript:printPage();"><b><spring:message
																code="LABEL_PRINT" text="Print" /></b></a></td>
											</tr>
										</table>
									</c:if></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
	</form:form>
</body>
</html>
