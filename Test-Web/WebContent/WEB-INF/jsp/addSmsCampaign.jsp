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

 $(document).ready(function() {		
	 var target = document.getElementById("target").value ;
		
		if( target == "Custom"){
			$(".customdiv").show();
		}else{
			$(".customdiv").hide();
		}
	});

 function checkcustomdiv(){
	 	var target = document.getElementById("target").value ;

	 	if( target == "Custom"){
	 		$(".customdiv").show();
	 	}else{
	 		$(".customdiv").hide();
	 	}
 }

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
    	
    	
        var title = document.getElementById("title").value;
        var message = document.getElementById("message").value;
        var target = document.getElementById("target").value;
        var pattern='^\[0-9,]*$';

        if(title=="") {
	         alert('Please enter Message Title');
	         document.smsCampaignViewForm.title.focus();
	         return false;
		 }else if(message=="") {
	         alert('Please enter Message');
	         document.smsCampaignViewForm.message.focus();
	         return false;
		 }else if(target=="") {
	         alert('Please enter select Target');
	         document.smsCampaignViewForm.target.focus();
	         return false;
		 }
        if(target=="Custom") {
        	var mobileNumbers = document.getElementById("mobileNumbers").value;
        	if(mobileNumbers=="") {
	   	         alert('Please enter Mobile Numbers');
	   	         document.smsCampaignViewForm.mobileNumbers.focus();
	   	         return false;
   			 }else if(mobileNumbers.search(pattern)==-1){
                	 alert('Please enter valid Mobile Numbers');
                     document.smsCampaignViewForm.mobileNumbers.focus();
                     return false;
      	    }
        	
		 }
        if(confirm('Do you want to add SMS Campaign ?')) {
			document.smsCampaignViewForm.action = "sendSmsCampaign.htm";
			document.smsCampaignViewForm.submit();
        }else{
        	return false;
        }
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
			document.smsCampaignViewForm.reset();
		/* 	document.smsCampaignViewForm.title.value="";
			document.smsCampaignViewForm.message.value="";
			document.smsCampaignViewForm.target.value="";
			document.smsCampaignViewForm.mobileNumbers.value=""; */
			$(".customdiv").hide();
		}

	</script>
</head>
<body onload="check(),check1();">
	<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  added commandName=customerDTO for customerDTO submision -->
	<form:form id="smsCampaignViewForm" name="smsCampaignViewForm" class="form-inline"
		commandName="smsCampaignDTO" method="post">
		<jsp:include page="csrf_token.jsp" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title"><span><spring:message code="LABEL_SMS_CAMPAIGN" text="Sms Campaign"/></span></h3>
				</div>
				<div class="col-sm-6">
					<div class="col-sm-6 col-sm-offset-9"
						style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message1}" text="" />
					</div>
				</div>
				<br /> <br />
				<div class="box-body">
				<authz:authorize ifAnyGranted="ROLE_bankadmin">
				<div class="row">
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_MESSAGE_TITLE" text="Message Title"/><font color="red">*</font></label>
						<form:input id="title" path="title" type="text" maxlength="50" cssClass="form-control"/> 
					</div> 
 
					<div class="col-sm-6">
						<label class="col-sm-6"><spring:message code="LABEL_MESSAGE" text="Message"/><font color="red">*</font></label>
						<form:textarea id="message" path="message" type="text" rows="4" cols="25"  maxlength="140" cssClass="form-control"/> 
					</div> 
				</div>
				<div class="row">

					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_TARGET" text="Target:"></spring:message><font color="red">*</font></label> 
						<form:select path="target" id="target" onchange="checkcustomdiv();">
						<form:option value="All">
							<spring:message code="LABEL_ALL" text="All"></spring:message>							
						</form:option>
						<%-- <form:option value="PrincipalAgent">
							<spring:message code="LABEL_BUSINESS_PARTNER_L1" text="PrincipalAgent"></spring:message>
						</form:option> --%>
						<form:option value="SuperAgent">
							<spring:message code="LABEL_BUSINESS_PARTNER_L2" text="SuperAgent"></spring:message>
						</form:option>
						<form:option value="Merchant">
							<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant"></spring:message>
						</form:option>
						<form:option value="Agent">
							<spring:message code="LABEL_AGENT" text="Agent"></spring:message>
						</form:option>
						 <form:option value="Customer">
							<spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						</form:option>
						<form:option value="Custom">
							<spring:message code="LABEL_CUSTOM" text="custom"></spring:message>
						</form:option>
					</form:select> <font color="RED"><form:errors path="title"></form:errors></font>
				</div>
				<div class="col-sm-6 customdiv">
						<label class="col-sm-6"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"/><font color="red">*</font><br />
						<spring:message code="LABEL_CAMA_SEPERATED_MOBILE_NUMBERS" text="(cama seperated Mobile Number's)"/></label>
						<form:textarea id="mobileNumbers" path="mobileNumbers" rows="4" cols="25" type="text" cssClass="form-control"/> 
				</div> 
			<br/>
			<div class="col-md-3 col-md-offset-10">
			<input type="button" id="submitButton" class="btn btn-primary" value="<spring:message code="LABEL_SEND" text="Send"/>" onclick="validate();"></input>
				<c:if test="${(customerDTO.customerId eq null) }">
				 <input type="button" class="btn btn-primary" value="<spring:message code="LABEL_RESET" text="reset"/>" onclick="clearForm();"></input>
				</c:if>
			</div>
			</div>
			
		</authz:authorize>
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
								<th><spring:message code="LABEL_MESSAGE_TITLE" text=" Message Title" /></th>		
								<th><spring:message code="LABEL_TARGET" text="Target" /></th>
								<th><spring:message code="LABEL_TXNDATE" text="Date and Time" /></th>
								<th><spring:message code="LABEL_MESSAGE" text="Message" /></th>		
	
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
							<c:forEach items="${page.results}" var="smsCampaign">
								<c:set var="j" value="${ j+1 }"></c:set>
								<tr>
									<td><c:out value="${j}"/></td>
									<td><c:out value="${smsCampaign.Title}" /></td>
									<td><c:out value="${smsCampaign.Target}" /></td>
									<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${smsCampaign.CreatedDate}" /></td>
									<td><c:out value="${smsCampaign.Message}" /></td>			
								</tr>
							</c:forEach>
						</tbody>
					</table>

				</div>
			</div>
		</div>

	</form:form>
</body>
</html>
