<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
	<head>
	
		<%-- <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/calendar-system.css" /> --%>
		
		<!-- Loading Calendar JavaScript files -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/zapatec.js"></script>
		
		<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script> --%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
	    <script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>	  
   		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script>
		<script type="text/javascript"  src="<%=request.getContextPath()%>/js/bankManagement.js"></script>
        
	<style type="text/css">
	<!--
	.style1 {
	color: #FFFFFF;n
	font-weight: bold;
	}
	-->
	.add_cus {
    text-align: right;
    margin-right: 20px;
}

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
			 "contactNumberLength":"<spring:message code="VALID_CONTACT_NUMBER_LENGTH" text="Contact Number length cant be less than 9."/>",
			 "contactNumber":"<spring:message code="VALID_CONTACT_NUMBER" text="Please enter the Contact Number "/>",
			 "numberAllZeros":"<spring:message code="ERROR_MESSAGE_CONTACT_NUMBER_ALL_ZEROS" text="Contact Number should not contain all zeros"></spring:message>",
			 "name":"<spring:message code="VALID_NAME" text="Please enter the Name "/>",
			 "nameLength":"<spring:message code="VALID_NAME_LENGTH" text="Name should be minimum 2 characters"/>",
			 "NameAlphabet":"<spring:message code="ERROR_MESSAGE_LAST_NAME_ALPHABET" text=" Name should contain only alphabets."></spring:message>",
			 "namePatt":"<spring:message code="ERROR_MESSAGE_BUSINESS_PARTNER_NAME_ALPHABET" text=" Please valid enter Partner Name."></spring:message>",
			 "nameWhiteSpac":"<spring:message code="ERROR_MESSAGE_NAME_TWO_WHITE_SPACES" text="should not contain two consecutive white spaces."></spring:message>",
			
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
    	
    	var mobileLength = mobileNumberlen;	
        var fromDate = document.getElementById("fromDate").value;
    	var toDate = document.getElementById("toDate").value;
    	var transactionId = document.getElementById("transactionId").value;
        var name = document.getElementById("name").value;
        var refTransactionId = document.getElementById("refTransactionId").value;
        var transactionType = document.getElementById("transactionType").value;
        var partnerCode = document.getElementById("partnerCode").value;
        var CodePattern = '^\[0-9]{0,4}$';
  //      var middleName = document.getElementById("middleName").value;
   //     var lastName = document.getElementById("lastName").value;
        var pattern='^\[a-zA-ZÀ-ÿ0-9\ ]*$'; 
        var strAlphabet = '^\[a-zA-Z0-9 ]{0,30}$';
        var alphaNumericCode='/^[a-z0-9]+$/i';
        var numbers =  /^[0-9]+$/;
        
        
        
        /*if (isNaN(contactNumber)) { 
				alert('<spring:message code="MOBILENUM_CHARACTER" text="Enter a valid mobile number"/>');
				return false;
			} else if($.trim(document.businessViewForm.contactNumber.value) == ""){
				alert(Alertmsg.contactNumber);
				document.businessViewForm.contactNumber.focus();
				return false;
			}
			else if($.trim(document.businessViewForm.contactNumber.value).search(CodeNoALLZEROS) != -1){
				alert(Alertmsg.numberAllZeros);
				document.businessViewForm.contactNumber.focus();
				return false;
			} */
			/* if($.trim(document.BusinessPartnerRegistrationForm.contactNumber.value).search(CodePattern) == -1){
				alert(Alertmsg.numberPatt);
				document.BusinessPartnerRegistrationForm.contactNumber.focus();
				return false;
			} 
			
			else  if(document.businessViewForm.contactNumber.value != "" && document.businessViewForm.contactNumber.value.length != mobileNumberlen){
				alert(Alertmsg.contactNumberLength+" of length "+mobileLength);
				document.businessViewForm.contactNumber.focus();
				return false;
			}
				
			/* 	if(document.businessViewForm.contactNumber.value != "" && document.businessViewForm.contactNumber.value.length < 9){
				alert(Alertmsg.contactNumberLength);
				document.businessViewForm.contactNumber.focus();
				return false; 
			}*/
          if(transactionId != "" && !transactionId.match(/^[0-9]*$/)){
    		alert('Transaction ID should contain only Number');
    		document.businessViewForm.transactionId.focus();
    		return false;
    	    }
           
          else if (name.match(numbers)) {
				alert('<spring:message code="name_VALIDATION" text="Please enter only Alphabets in Name"/>');
				return false;
			}/*  else  if($.trim(document.businessViewForm.name.value) == ""){
				alert(Alertmsg.name);
				document.businessViewForm.name.focus();
				return false;
			} */
			else if(document.businessViewForm.name.value != "" && document.businessViewForm.name.value.length < 2){
				alert(Alertmsg.nameLength);
				document.businessViewForm.name.focus();
				return false;
			}
			
			else if(document.businessViewForm.name.value != "" && $.trim(document.businessViewForm.name.value).search(strAlphabet) == -1){
				alert(Alertmsg.namePatt);
				document.businessViewForm.name.focus();
				return false;
			}
			else if(document.businessViewForm.name.value != "" && document.businessViewForm.name.value.indexOf("  ") != -1){
				alert("Partner/Agent "+Alertmsg.nameWhiteSpac);
				document.businessViewForm.name.focus();
				return false;
			}else if(document.businessViewForm.transactionType.value != "" && transactionType.match(numbers)){
				alert("Ref Tran Type should contains only Aplphabets");
				document.businessViewForm.transactionType.focus();
				return false;
			}else if(document.businessViewForm.transactionType.value != "" && document.businessViewForm.transactionType.value.indexOf("  ") != -1){
				alert("Ref Tran Type "+Alertmsg.nameWhiteSpac);
				document.businessViewForm.transactionType.focus();
				return false;
			}else if(refTransactionId != "" && !refTransactionId.match(/^[0-9]*$/)){
      		    alert('Ref Tran ID should contain only Number');
      		    document.businessViewForm.refTransactionId.focus();
      		    return false;
			}else if(partnerCode != "" && !partnerCode.match(/^[0-9]*$/)){
      		    alert('Partner Code should contain only Number');
      		    document.businessViewForm.partnerCode.focus();
      		    return false;
			}
			/* else if (middleName.search(pattern) == -1) {
				alert('<spring:message code="VALID_MIDDLENAME" text="Please enter only Alphabets in Middle Name"/>');
				return false;
			} else if (lastName.search(pattern) == -1) {
				alert('<spring:message code="VALID_LASTNAME" text="Please enter only Alphabets in Last Name"/>');
				return false;
			} */
		
		
			else if (fromDate == "") {

				alert('<spring:message code="LABEL.FROMDATE" text="Please enter From Date"/>');
				return false;
			} else if (toDate == "") {
				alert('<spring:message code="LABEL.TODATE" text="Please enter To Date"/>');
				return false;
			}
			if (fromDate != "" && toDate != "") {
				if (!validdate(fromDate, toDate)) {
					return false;
				}
				if (!compareFromDate(fromDate, toDate)) {
					alert("<spring:message code='COMPARE_DATES' text='From Date should not be greater than To Date'/>");
					return false;
				}

			}

			if (fromDate != "") {
				if (!validFromDate(fromDate)) {
					return false;
				}
				if (!compareDate(fromDate)) {
					alert("<spring:message code='LABEL.FROMDATE.TODAY' text='Please enter From Date Less than Todays date'/>");
					return false;
				}

			}
			if (toDate != "") {
				if (!validToDate(toDate)) {

					return false;
				}
				if (!compareDate(toDate)) {
					alert("<spring:message code='LABEL.TODATE.TODAY' text='Please enter To Date Less than Todays date'/>");
					return false;
				}

			}

			/*	<authz:authorize ifAllGranted="ROLE_admin">
			 var bankId = document.getElementById("bankId").value;
			var branchId = document.getElementById("branchId").value; */
			/*  if(bankId != ""){
			 	if(branchId =="" || branchId =="select"){
			 		 alert('<spring:message code="VALID_EMPTY_BANK_BRANCH" text="Please select the branch"/>');
			          return false;
			 	}
			 } 
			</authz:authorize>*/
			/* document.businessViewForm.contactNumberV.value = document.businessViewForm.contactNumber.value; */
			document.businessViewForm.transactionIdV.value = document.businessViewForm.transactionId.value;
			document.businessViewForm.nameV.value = document.businessViewForm.name.value;
			fromChange();
			toChange();
			document.businessViewForm.action = "showTxnBusinessPartner.htm"
			document.businessViewForm.submit();

		}
	
		$(document)
				.ready(
						function() {
							var contactNumber = $("#contactNumber").val();
							var name = $("#name").val();
							/* var middleName = $("#middleName").val();
							var lastName = $("#lastName").val();
							var bankGroupId = $("#bankGroupId").val();
							var branchId = $("#branchId").val();
							var bankId = $("#bankId").val(); */
				//			var countryId = $("#countryId").val();
				//			var custType = $("custType").val();
				//			$("#custType").val($('#custTypeCode').val());
							if (contactNumber != '' && contactNumber != undefined
									|| name != ''
									&& name != undefined
									/* || middleName != ''
									&& middleName != undefined
									|| lastName != ''
									&& lastName != undefined */
									/* || bankGroupId != ''
									&& bankGroupId != undefined || bankId != ''
									&& bankId != undefined || branchId != ''
									&& branchId != undefined || countryId != ''
									&& countryId != undefined || custType != ''
									&& custType != undefined */) {
								$("#dwnldLink").show();
							}

							/* $("#bankId")
									.change(
											function() {
												$bankId = document
														.getElementById("bankId").value;
												$csrfToken = $("#csrfToken")
														.val();
												$
														.post(
																"getBranches.htm",
																{
																	bankId : $bankId,
																	csrfToken : $csrfToken
																},
																function(data) {
																	document
																			.getElementById("branchdiv").innerHTML = "";
																	document
																			.getElementById("branchdiv").innerHTML = data;
																	setTokenValFrmAjaxResp();
																	applyChosen();
																});

											}); */

						});
		
		function fromChange(){			   
            var x=	$("#fromDate").val();
        	$("#fromDateV").val(x);	        
		}
		function toChange(){			   
 		    var x=	$("#toDate").val();
	    	$("#toDateV").val(x);	        
		};
       

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
		 //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
		function customerDetail(url,operatorId){
			document.businessViewForm.contactNumberV.value = document.businessViewForm.contactNumber.value;
			document.businessViewForm.nameV.value = document.businessViewForm.name.value;
			fromChange();
			toChange();
			document.getElementById('id').value=operatorId;
			submitlink(url,'businessViewForm');
		}
		function customerDetailEdit(url,operatorId){
			document.businessViewForm.contactNumberV.value = document.businessViewForm.contactNumber.value;
			document.businessViewForm.nameV.value = document.businessViewForm.name.value;
			fromChange();
			toChange();
			document.getElementById('id').value=operatorId;
			submitlink(url,'businessViewForm');
		}
		//@end
		function clearForm(){

	/* @Author name <vinod joshi>, Date<9/3/2018>, purpose of change <give the BusinessPartnerRegistray validation> */
	
		document.businessViewForm.reset();
		document.businessViewForm.name.value="";
		document.businessViewForm.fromDate.value="";
		document.businessViewForm.toDate.value="";
//		document.businessViewForm.code.value="";
//		document.businessViewForm.contactPerson.value="";
		document.businessViewForm.contactNumber.value="";
//		document.businessViewForm.emailId.value="";
//		document.businessViewForm.commissionPercent.value="";

		document.businessViewForm.contactNumberV.value = document.businessViewForm.contactNumber.value;
		document.businessViewForm.nameV.value = document.businessViewForm.name.value;
		fromChange();
		toChange();
		document.businessViewForm.action = "showTxnBusinessPartner.htm"
		document.businessViewForm.submit();
		
		}
		
		// Naqui: export pdf for businessPartners
		function businessPartnersPDF(){
				
				 var url="exportToPDFBusinessPartnersCommission.htm";
				 submitlink(url,"businessViewForm"); 
				 for(var i=0;i<150000;i++);
				 document.body.style.cursor = 'default';
				 canSubmit = true; 
			}
		
		function businessPartnersExcel(){
			
			 var url="businessPartnerCommissionExcelReport.htm";
			 submitlink(url,"businessViewForm"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
		}
	</script>
	</head>
<body onload="check(),check1()">
<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  added commandName=customerDTO for customerDTO submision -->
<form:form id="businessViewForm" name="businessViewForm" class="form-inline"  commandName="BusinessPartnerDTO"  method="post">
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_VIEW_BUISNESS_PARTNERS_COMMISSION" text="Commission Reports"></spring:message></span>
		</h3>
	</div>
		<div class="col-sm-6">
			<div class="col-sm-9 col-sm-offset-9" style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div>
		</div>

 	<form:hidden path="id" /> 
  
 	
<%-- 	<div class="add_cus">
	<authz:authorize ifAnyGranted="ROLE_addCustomerAdminActivityAdmin,ROLE_addBusinessPartnerAdminActivityAdmin">
		<a href="javascript:submitForm('businessViewForm','addBusinessPartner.htm')"><strong><spring:message code="ADD_BUSINESS_PARTNER" text="Add Business Partner"/></strong></a>
	</authz:authorize>
	<span id="dwnldLink" style="display:none;">
	<c:if test="${page.results.size() gt 0}">
	<authz:authorize ifNotGranted="ROLE_parameter">
	<a  href="javascript:submitForm('businessViewForm','exportToXLSForCustomerDetails.htm')"><strong><spring:message code="LABEL_CUSTOMER_DETAILS_REPORT" text="Customer Details" /></strong></a>
	|<a  href="javascript:submitForm('businessViewForm','exportToXLSForCustomerAccountDetails.htm')"><strong><spring:message code="LABEL_CUSTOMER_BALANCE_REPORT" text="Account Details" /></strong></a>&nbsp;&nbsp;&nbsp;
	</authz:authorize>
	</c:if>
	</span>
	</div>--%><br/><br/> 
	<div class="box-body">
	
	<authz:authorize ifAnyGranted="ROLE_mGurush,ROLE_bankteller,ROLE_bussinesspartnerL1,ROLE_businesspartnerL2,ROLE_admin,ROLE_bankadmin,ROLE_addBusinessPartnerAdminActivityAdmin,ROLE_accounting">
		
			
			<div class="row">

				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXN_REF" text="Transaction ID"></spring:message></label> 
					<input id="transactionId" name="transactionId"  value="<c:out value="${transactionId}"/>" class="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_PARTNER_AGENT_NAME" text="Agent/partner Name"/></label> 
					<input id="name" name="name" type="text" maxlength="30" value="<c:out value="${name}" />" class="form-control" />
				</div>
				</div>
				
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_REF_TRAN_TYPE" text="Ref Tran Type"/></label> 
					<input id="transactionType" name="transactionType" type="text" maxlength="30" value="<c:out value="${transactionType}" />" class="form-control" />
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_REF_TRAN_ID" text="Ref Tran ID"></spring:message></label> 
					<input id="refTransactionId" name="refTransactionId"  value="<c:out value="${refTransactionId}"/>" class="form-control" />
				</div>
				</div>
				
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_PARTNER_AGENT_CODE" text="Partner/Agent Code"/></label> 
					<input id="partnerCode" name="partnerCode" type="text" maxlength="30" value="<c:out value="${partnerCode}" />" class="form-control" />
				</div>
				</div>
				
			</authz:authorize>			
			
	
					<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankteller,ROLE_mGurush,ROLE_bussinesspartnerL1,ROLE_businesspartnerL2,ROLE_bankgroupadmin,ROLE_backofficelea,ROLE_backofficeexe,ROLE_gimsupportlead,ROLE_gimsupportexec,ROLE_bankadmin,ROLE_addBusinessPartnerAdminActivityAdmin,ROLE_accounting">
					<div class="row">	
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" onfocus="this.blur()"  value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" onfocus="this.blur()"  value="${toDate}" class="form-control datepicker" onclick="  "></input>	
					<font color="red"></font>
				</div>
				</div>
				<br/>
			</authz:authorize>
			</div>
			
			<div class="box-footer">								
				<%-- <input type="button" value="<spring:message code='LABEL_CLEAR_SEARCH' text = 'Clear Search'/>" class="btn btn-primary pull-right" onclick="clearForm();" /> --%>   
				<input type="button"  value="<spring:message code="LABEL_SEARCH" />" class="btn btn-primary pull-right" style="margin-right:20px;" onclick="validate();" />				
				<br/><br/>
			</div>
			
			</div>
	
			
			<%-- <form:hidden path="contactNumber" /> 
 			<form:hidden path="name" /> 
 			<form:hidden path="fromDate" /> 
 			<form:hidden path="toDate" />  --%>
			
				<input type="hidden" name="roleId" value="${roleId}"/>
				<input type="hidden" name="fromDateV" id="fromDateV" value="${fromDate}"/>
				<input type="hidden" name="toDateV" id="toDateV" value="${toDate}"/>
				<input type="hidden" name="nameV" id="nameV" value="${name}"/>
				<input type="hidden" name="contactNumberV" id="contactNumberV" value="${contactNumber}"/>
				<input type="hidden" name="transactionIdV" id="transactionIdV" value="${transactionId}"/>
				<input type="hidden" name="mobileNumberlen" value="${mobileNumLength}"/>
</div>
</div>
 			<div class="box">
 			<%-- <input type="hidden" name="pageV" id="pageV" value="${page}"/> --%>
 		<%-- 		 <form:hidden path="page" name="page" value="${page}"/>  --%>
				<div class="box-body table-responsive">
				<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
                	<%-- <span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span> --%>
					<span style="float:right; margin-right: 5px;">
						<a href="#" onclick="javascript:businessPartnersExcel();" style="text-decoration:none;margin-left:-11px;"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
							<%-- <span><spring:message code="LABEL_EXCEL" text="Excel"/></span> --%>
						</a>
					</span>
					
					<span style="margin-right: 30px; float:right">
						<a href="#" style="text-decoration:none;margin-left:10px;" onclick="javascript:businessPartnersPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
							<%-- <span><spring:message code="LABEL_PDF" text="PDF"/></span> --%>
						</a>
					</span>					
   			   </div>
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
							<th><spring:message code="SI_NO" text="SI No"/></th>
							<th><spring:message code="LABEL_TXN_REF" text="Transaction ID"/></th>
							<th><spring:message code="LABEL_REF_TRAN_TYPE" text="Ref Tran Type"/></th>
							<th><spring:message code="LABEL_REF_TRAN_ID" text="Ref Tran ID"/></th>
							<th><spring:message code="LABEL_SERVICE_CHARGES" text="Serivce Charges"/></th>	
							<th><spring:message code="LABEL_COMMISSION_AMOUT" text="Commission Amount"/></th>
							<th><spring:message code="LABEL_PARTNER_AGENT_NAME" text="Partner/Agent Name"/></th>
							<th><spring:message code="LABEL_PARTNER_AGENT_CODE" text="Partner/Agent Code"/></th>
							<th><spring:message code="LABEL_TRANSACTION_DATE_TIME" text="Transaction Date & Time"/></th>
								
								<%-- <th><spring:message code="LABEL_NAME" text="Name"/></th>
								<th><spring:message code="LABEL_CONTACT_NO" text="Contact Number"/></th>
								<th><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type"/></th>
								<th><spring:message code="LABEL_AMOUNT" text="Amount"/></th>
								<th><spring:message code="LABEL_TRANSACTION_DATE_TIME" text="Transaction Date & Time"/></th> --%>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
					            <c:forEach items="${page.results}" var="bpart" >
					            <c:set var="j" value="${ j+1 }"></c:set>
					            <tr>
					            	<td><c:out value="${j}"/></td>
					            	<td><c:out value="${bpart.TransactionID}" /></td>
					            	<td><c:out value="${bpart.TransactionType}" /></td>
					            	<td><c:out value="${bpart.RefTransactionID}" /></td>
								    <td><c:out value="${bpart.ServiceCharge}" /></td>										  						                                        	
                                    <td><c:out value="${bpart.CommissionAmount}" /></td>
                                    <td><c:out value="${bpart.PartnerName}" /></td>
                                    <td><c:out value="${bpart.PartnerCode}" /></td>	
 									<td>
										<fmt:formatDate pattern = "dd-MM-yyyy HH:mm:ss"  value = "${bpart.TransactionDate}" />
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
var mobileNumberlen='${mobileNumLength}';
var a ;
var arr=new Array();
a="<c:out value='${customerList}'/>";
var c=a.substring(a.indexOf ('[')+1,a.lastIndexOf(']'));

for(var i=0;i<c.split(",").length;i++){
	//arr[i]="\""+c.split(",")[i]+"\"";
	arr[i]=c.split(", ")[i];
	//alert(arr[i])
}
$("#name").autocomplete({source:arr});
  </script>
	</body>
</html>
