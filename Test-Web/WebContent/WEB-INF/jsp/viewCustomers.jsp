<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
	<head>
	
		<%-- <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/calendar-system.css" /> --%>
		
		<!-- Loading Calendar JavaScript files -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/zapatec.js"></script>
		<script type='text/javascript' src='https://code.jquery.com/jquery-2.1.1.min.js'></script>
	    <script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>	  
   		<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script> --%>
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
	.container-outer {
		display: block;
        overflow-x: auto;
        white-space: nowrap;
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
			 "customerApprove" : "<spring:message code="CUSOMER_APPROVE_CHECKBOX_SELECT" text="Please select one customer details for Approve"></spring:message>",
			 "customerReject" : "<spring:message code="CUSOMER_REJECT_CHECKBOX_SELECT" text="Please select one customer details for Reject"></spring:message>",
			 "custRejectConfirm" :"<spring:message code="CUSOMER_REJECT_CONFIRMATION" text="Do you want to reject the selected customer details"></spring:message>",
			 "custApproveConfirm" :"<spring:message code="CUSOMER_APPROVE_CONFIRMATION" text="Do you want to approve the selected customer details"></spring:message>",
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
    	
    	
        var fromDate = document.getElementById("fromDate").value;
    		 var toDate = document.getElementById("toDate").value;
    	     var mobileNumber = document.getElementById("mobileNumber").value;
        var firstName = document.getElementById("firstName").value;
        var middleName = document.getElementById("middleName").value;
        var lastName = document.getElementById("lastName").value;
        var pattern='^\[a-zA-ZÀ-ÿ0-9\ ]*$';
        
        
        
		if (isNaN(mobileNumber)) {
				alert('<spring:message code="MOBILENUM_CHARACTER" text="Enter a valid mobile number"/>');
				return false;
			} else if (firstName.search(pattern) == -1) {
				alert('<spring:message code="FIRSTNAME_VALIDATION" text="Please enter only Alphabets in First Name"/>');
				return false;
			} else if (middleName.search(pattern) == -1) {
				alert('<spring:message code="VALID_MIDDLENAME" text="Please enter only Alphabets in Middle Name"/>');
				return false;
			} else if (lastName.search(pattern) == -1) {
				alert('<spring:message code="VALID_LASTNAME" text="Please enter only Alphabets in Last Name"/>');
				return false;
			}

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

			<authz:authorize ifAllGranted="ROLE_admin">
			var bankId = document.getElementById("bankId").value;
			/* var branchId = document.getElementById("branchId").value; */
			/*  if(bankId != ""){
			 	if(branchId =="" || branchId =="select"){
			 		 alert('<spring:message code="VALID_EMPTY_BANK_BRANCH" text="Please select the branch"/>');
			          return false;
			 	}
			 } */
			</authz:authorize>
			/*  var type = document.getElementById("type").value;
			document.customerViewForm.action = "searchCustomer.htm?type="+type;
			document.customerViewForm.submit(); 
			  */
			 
			 url = 'searchCustomer.htm';
			var type = document.getElementById("type").value;
			document.getElementById("type").value = type;
			submitlink(url,'customerViewForm','type'); 
 
		}

		$(document)
				.ready(
						function() {
							var mobileNumber = $("#mobileNumber").val();
							var firstName = $("#firstName").val();
							var middleName = $("#middleName").val();
							var lastName = $("#lastName").val();
							var bankGroupId = $("#bankGroupId").val();
							var branchId = $("#branchId").val();
							var bankId = $("#bankId").val();
					//		var countryId = $("#countryId").val();
							var custType = $("custType").val();
							$("#custType").val($('#custTypeCode').val());
							if (mobileNumber != '' && mobileNumber != undefined
									|| firstName != ''
									&& firstName != undefined
									|| middleName != ''
									&& middleName != undefined
									|| lastName != ''
									&& lastName != undefined
									|| bankGroupId != ''
									&& bankGroupId != undefined || bankId != ''
									&& bankId != undefined || branchId != ''
									&& branchId != undefined || custType != '' 
								/* 	&& countryId != undefined || countryId != '' */
									&& custType != undefined) {
								$("#dwnldLink").show();
							}

							$("#bankId")
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
											});
							
							
											/* 	 var tableOfResults=null;
												    tableOfResults = $('#txnSummaryData').DataTable({
														"processing": true,
														"serverSide": true,
														"paging" : true,
														"searching": false,
														"lengthChange": false,
														"scrollX": true,
														"SearchTerm":"",
														"sortable":false,
														"bAutoWidth": false,
														
														"iDisplayLength": null == $("#recordsLength").val() ? 10 : $("#recordsLength").val() ,
														"lengthMenu": [ [10, 20, 30, -1], [10, 20, 30, "All"] ],
														"pageLength": null == $("#recordsLength").val() ? 10 : $("#recordsLength").val() ,
																
													
												//	     "Page":2,
												//	    "Draw":10,
													//    "EntityToSearch":"1", 
														
														  "ajax" : {
															"url" : 'searchCustomerData.htm',
															"type" : "POST",
															  "data": function (data) {
																 	var SearchParameters = {};
																 	data.length = $("#recordsLength").val();
																 	var info = (tableOfResults == null) ? { page: 0, length: 10, start:0 } : tableOfResults.page.info();
																 	info.length = $("#recordsLength").val();
													                console.log(info);
													                SearchParameters.SearchTerm = '';
													           //     info.length = $("#recordsLength").val();
												//	         if($("#recordsLength").val() == "20"){
												//	        	   SearchParameters.pageNumber = Math.ceil(data.start/data.length + 1);
												//	           }else if($("#recordsLength").val() == "30"){
												//	        	   SearchParameters.pageNumber = Math.ceil(data.start/data.length + 1);
													//           }else{
												//	        	   SearchParameters.pageNumber = Math.ceil(data.start/data.length + 1);
												//	           } 
													     //          SearchParameters.pageNumber = data.start/data.length + 1;
													           SearchParameters.pageNumber = info.page;
													                SearchParameters.Draw = data.draw;

																// var additionalArgs = ["mobileNumber","bankGroupId","bank","bankId","branchId","branch","location","transactionType","profileId","profileName","fromDate","toDate"];  
																	var additionalArgs = ["mobileNumber","fromDate","toDate","custType","customerKycStatus"];
																	var i='', k='';
																	$.each(additionalArgs, function(i, k){
																		if($("#"+k).val()){
																			SearchParameters[k] = $("#"+k).val();
																			//console.log(SearchParameters[k])
																		}
																	});
																	
																	    var tableHeaderIndex=data.order[0].column;
																		var sortBy=data.order[0].dir;
																		var tableHeadervalue=data.columns[tableHeaderIndex].data;
																		
																		console.log(sortBy);
																		console.log(tableHeadervalue);
																		SearchParameters.sortColumn=tableHeadervalue;
																		SearchParameters.sortBy=sortBy;
																		SearchParameters.index=data.start;
																		SearchParameters.length = data.length;
																		
													                
													                return SearchParameters;
													            },
													        	"error":function(errorThrown){
													        		ShowDataTable(null);
													        	
														        }
														}, 
														"columns": [
															{ "data": "Appove/Reject" , "defaultContent": ""}, 
															{ "data": "SerNo" , "defaultContent": ""}, 
												            { "data": "Name" , "defaultContent": ""},
												            { "data": "MobileNumber" , "defaultContent": ""},
												            { "data": "Status" , "defaultContent": ""},
												            { "data": "Gender" , "defaultContent": ""},
												            { "data": "RegisteredDate" , "defaultContent": ""},
												            { "data": "State" , "defaultContent": ""},
												            { "data": "OnBoardedBy" , "defaultContent": ""},
												            { "data": "KYCStatus" , "defaultContent": ""},
												            { "data": "Action" , "defaultContent": ""}
											  
												        ],
												        
														"columnDefs": [ 
															{targets: [0],
															  "render": function (data, type,row) {
															//     return '<a href="javascript:customerDetail('viewAgent.htm','+row.Id+')">' + data + '</a>';
															 //    return "<a href=\"javascript:submitForm('agentViewForm','viewAgent.htm?customerId="+row.Id+"')\">"+row.Action+"</a>";
																	if(${roleAccess == 2} && (row.Kyc == 0 || row.Kyc == 21)){
															 			return "<input type='checkbox' name='manageCustomerCheck' value='"+row.Id+"' id='manageCustomer#"+row.Kyc+"'<input/>";
																	}
															 		else{
															 			return "";
															 		}
															   }},
															   {targets: [10],
																	  "render": function (data, type,row) {
																	//     return '<a href="javascript:customerDetail('viewAgent.htm','+row.Id+')">' + data + '</a>';
																	     return "<a href=\"javascript:submitForm('customerViewForm','viewCustomer.htm?customerId="+row.Id+"')\">"+row.Action+"</a>";
																	     
																	   }},
																	   {targets: [0,1,2,3,4,5,6,7,8,9,10], "sortable":false },
															{"ordering": true},
														{ "orderable": false, "targets": 0 }
											],
												       
												        "order": [[ 1, 'asc' ]]


											}); */

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
			document.getElementById('customerId').value=operatorId;
			submitlink(url,'customerViewForm');
		}
		//@end
		

		
		/*@start Reports with TXn Date*/
		function agentDetailsExcelTxnDate(){
			submitlink("exportToXlsForAgentDetailsTxnDate.htm","customerViewForm"); 
			 for(var i=0;i<150000;i++);{
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
			 $.unblockUI();
			 }
			
		}
		
		function agentDetailsPDFTxnDate(){
			submitlink("exportToPdfForAgentDetailsTxnDate.htm","customerViewForm"); 
			 for(var i=0;i<150000;i++);{
			 document.body.style.cursor = '';
			 canSubmit = true; 
			 }
			
		}
		/*@End Reports with TXn Date */
		

		
		
		function agentDetailsExcel(){

			submitlink("exportToXlsForAgentDetails.htm","customerViewForm"); 
			 for(var i=0;i<150000;i++);{
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
			 $.unblockUI();
			 }
		}
		
		
		function agentDetailsPDF(){
			 
			 submitlink("exportToPdfForAgentDetails.htm","customerViewForm"); 
			 for(var i=0;i<150000;i++);{
			 document.body.style.cursor = '';
			 canSubmit = true; 
			 }
		}
		
		function addCustomer(form,url) {
		     var fromDate = document.getElementById("fromDate").value="";
   			 var toDate = document.getElementById("toDate").value="";
   	    	 var mobileNumber = document.getElementById("mobileNumber").value="";
      		 var firstName = document.getElementById("firstName").value="";
      		 var middleName = document.getElementById("middleName").value="";
      		 var lastName = document.getElementById("lastName").value=""; 
      		var onBoardedBy = document.getElementById("onBoardedBy").value=""; 
			submitlink(url,'customerViewForm');
		}
	</script>
	</head>
<body onload="check(),check1()">
<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  added commandName=customerDTO for customerDTO submision -->
<form:form id="customerViewForm" name="customerViewForm" class="form-inline"  commandName="customerDTO"  method="post">
<jsp:include page="csrf_token.jsp"/>
<form:hidden path="type" value="${custType}" id="custType" name="custType"/>
<input type="hidden" name="custTypeCode" id="custTypeCode" value="${custType}"/> 
<input type="hidden" name="type" id="type" value="${custType}"/> 
<input type="hidden" name="roleId" value="${roleId}"/>
<form:hidden path="customerId" />
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_VIEW_CUSTOMERS" text="View Customers"></spring:message></span>
		</h3>
	</div>
		<div class="col-sm-6">
			<div class="col-sm-7 col-sm-offset-9" style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div>
		</div>
	<div class="col-md-12">
	<c:if test="${page.results.size() gt 0}">
	<span id="dwnldLink" style="display:none; margin-left:64%;">
	<authz:authorize ifNotGranted="ROLE_parameter">
	<a  href="javascript:submitForm('customerViewForm','exportToXLSForCustomerDetails.htm')"><strong><spring:message code="LABEL_CUSTOMER_DETAILS_REPORT" text="Customer Details" /></strong></a>
	|<a  href="javascript:submitForm('customerViewForm','exportToXLSForCustomerAccountDetails.htm')"><strong><spring:message code="LABEL_CUSTOMER_BALANCE_REPORT" text="Account Details" /></strong></a>&nbsp;&nbsp;&nbsp;
	</authz:authorize>
	</span>
	</c:if>
	<authz:authorize ifAnyGranted="ROLE_addCustomerAdminActivityAdmin">
		<%-- <a href="javascript:submitForm('customerViewForm','showCustomerForm.htm')"><strong><spring:message code="LABEL_ADD_CUSTOMER" text="Add Customer"/></strong></a> --%>
		<label style="float:right; margin-right:20px;">
					 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_CUSTOMER" text="Add Clearing House"/>"
							onclick="javascript:addCustomer('customerViewForm','showCustomerForm.htm')" /> 
				</label>
	</authz:authorize>		
	
	</div><br/><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"></spring:message></label> 
					<input id="mobileNumber" name="mobileNumber"  type="text" value="<c:out value="${mobileNumber}"/>" class="form-control" maxlength="9"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FIRST_NAME" text="First Name"/></label> 
					<input id="firstName" name="firstName" type="text" value="<c:out value="${firstName}" />" class="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MIDDLE_NAME" text="Middle Name"/></label> 
					<input id="middleName" name="middleName" type="text" value="<c:out value="${middleName}" />" class="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_LAST_NAME" text="Last Name"/></label> 
					<input id="lastName" name="lastName" type="text" value="<c:out value="${lastName}" />" class="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ONBOARDED_BY" text="OnBoarded By"/></label> 
					<input id="onBoardedBy" name="onBoardedBy" type="text" value="<c:out value="${onBoardedBy}" />" class="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_KYC_STATUS" text="KYC Status" /></label> 
					<form:select class="dropdown chosen-select" id="customerKycStatus" path="customerKycStatus" >
						<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
						<form:option value="0"><spring:message code="LABEL_KYC_PENDING" text="KYC Pending" /></form:option>
						<form:option value="1"><spring:message code="LABEL_KYC_APPROVE_PENDING" text="KYC Approval Pending" /></form:option>
						<form:option value="11"><spring:message code="LABEL_KYC_APPROVED" text="KYC Approved" /></form:option>
						<form:option value="21"><spring:message code="LABEL_KYC_REJECTED" text="KYC Rejected" /></form:option>	
						</form:select>
				</div>
			</div>
			<authz:authorize ifAnyGranted="ROLE_superadmin,ROLE_bankadmin,ROLE_accounting,ROLE_bankteller,ROLE_businesspartnerL2">
				<div class="row">
			<authz:authorize ifAnyGranted="ROLE_superadmin,ROLE_bankadmin,ROLE_accounting,ROLE_bankteller">
			<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CHANNEL" text="Channel" /></label> 
					<form:select class="dropdown chosen-select" id="channel" path="channel" >
				
						<form:option value="1"><spring:message code="LABEL_CHANNEL_MOBILE_USSD" text="Mobile & USSD" /></form:option>
						<form:option value="2"><spring:message code="LABEL_CHANNEL_WEB" text="Web" /></form:option>
						</form:select>
				</div>
				</authz:authorize>
				<authz:authorize ifAnyGranted="ROLE_businesspartnerL2">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CHANNEL" text="Channel" /></label> 
					<form:select class="dropdown chosen-select" id="channel" path="channel" >
				
						<form:option value="1"><spring:message code="LABEL_CHANNEL_MOBILE_USSD" text="Mobile & USSD" /></form:option>
						</form:select>
				</div>
				</authz:authorize>
				</div>
				</authz:authorize>
			<div class="row">
			<%-- <authz:authorize ifAllGranted="ROLE_superadmin">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BRANCH" text="Branch"/></label> 
					<select id="branchId" class="dropdown_big" name="branchId">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.branchList}" var="branches">
							<option value="<c:out value="${branches.branchId}" />"  <c:if test="${branches.branchId eq branchId}" >selected=true</c:if> ><c:out value="${branches.location}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize> --%>
			<authz:authorize ifAllGranted="ROLE_admin">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big chosen-select" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value="<c:out value="${bankGroup.bankGroupId}" />" <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_gimbackofficelead">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value="<c:out value="${bankGroup.bankGroupId}" />" <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
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
							<option value="<c:out value="${bankGroup.bankGroupId}" />" <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_gimsupportlead">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value="<c:out value="${bankGroup.bankGroupId}" />" <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_gimsupportexec">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK_GROUP" text="Bank Group"/></label> 
					<select id="bankGroupId" class="dropdown_big" name="bankGroupId" class="dropdown">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.bankGroupList}" var="bankGroup">
							<option value="<c:out value="${bankGroup.bankGroupId}" />" <c:if test="${bankGroup.bankGroupId eq bankGroupId}" >selected=true</c:if>><c:out value="${bankGroup.bankGroupName}" /> </option>
						</c:forEach>
					</select>
				</div>
			</authz:authorize>
				
			<%-- removing country dropdown and making default country as SouthSudan,
				as discussed with sanjeeb. on 12-11-2018, by Vineeth Reddy
				<div class="col-sm-6">			
					<label class="col-sm-5"><spring:message code="LABEL_COUNTRY" text="Country"/></label> 
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
			       </select>
				</div> --%>
				
		<%-- 	
				--	@HardCode Customer Type is reomved --
						
			<authz:authorize ifAllGranted="ROLE_bankteller">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>								
					 <c:if test="${custType == 2 }">
						<spring:message code="LABEL_SOLE_MERCHANT" text="Merchant"></spring:message></c:if>
					 <c:if test="${custType== 3 }">
					 	<spring:message code="LABEL_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant"></spring:message></c:if>
				
					<select id="custType" name="custType" class="dropdown_big chosen-select" value=""<c:out value="${custType}"/>">
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/> ><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2"<c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
	                </div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_parameter">
				<div class="col-sm-6">

					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" value="<c:out value="${custType}"/>">
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2" <c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_audit">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" value="<c:out value="${custType}"/>">
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2" <c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_audit">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" value="<c:out value="${custType}"/>">
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2" <c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_bankadmin">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" value="<c:out value="${custType}"/>">
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/> ><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2" <c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_informationdesk">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" >
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1"<c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2"<c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_accounting">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" >
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2"<c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_operation">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" >
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2" <c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize>
			<authz:authorize ifAllGranted="ROLE_supportbank">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label>
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<select id="custType" name="custType" class="dropdown_big chosen-select" >
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
	                    <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
	                    <option value="1"<c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
	                    <option value="2"<c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select>
				</div>
			</authz:authorize> --%>
			
			</div>
			<div class="row">
			<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin,ROLE_backofficelea,ROLE_backofficeexe,ROLE_gimsupportlead,ROLE_gimsupportexec">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BANK" text="Bank"/></label> 
					<select name="bankId" class="dropdown_big chosen-select" id="bankId">
	                	<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
	                	<c:forEach items="${masterData.bankList}" var="banks">
						<option value=<c:out value="${banks.bankId}" /> <c:if test="${banks.bankId eq bankId}" >selected=true</c:if> ><c:out value="${banks.bankName}" /> </option>
						</c:forEach>
	                </select>
				</div>
				<%-- <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BRANCH" text="Branch"/></label> 
					<div id="branchdiv">
						<select id="branchId" class="dropdown_big chosen-select" name="branchId">
							<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
							<c:forEach items="${masterData.branchList}" var="branches">
								<option value=<c:out value="${branches.branchId}" /> <c:if test="${branches.branchId eq branchId}" >selected=true</c:if>><c:out value="${branches.location}" /> </option>
							</c:forEach>
						</select>
					</div>
				</div> --%>
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
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5">&nbsp; &nbsp; &nbsp;<spring:message code="LABEL_ACCTYPE" text="Customer Type"/></label> &nbsp;
					<label class="col-sm-5" id="custType" >
					 <c:if test="${custType == 0 }">				 	
						 <spring:message code="LABEL_CUSTOMER" text="Customer"></spring:message>
						 </c:if> </label>
					<%-- <select id="custType" name="custType" class="dropdown_big chosen-select">
	                	<option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></option>				                                         
                        <option value="0" <c:out value="${(custType == 0) ? 'selected' : ''}"/>><spring:message code="LABEL_CUSTOMER" text="Customer"/></option>	
                        <option value="1" <c:out value="${(custType == 1) ? 'selected' : ''}"/>><spring:message code="LABEL_MERCHANT" text="Merchant"/></option>
                        <option value="2" <c:out value="${(custType == 2) ? 'selected' : ''}"/>><spring:message code="LABEL_SOLE_MERCHANT" text="Sole-Merchant"/></option>
	                </select> --%>
				</div>
				</div><br/><br/>
			</authz:authorize>
			</div>
			<div class="row">
			<authz:authorize ifAnyGranted="ROLE_ccexec,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3,ROLE_salesofficer,ROLE_CommercialOfficer,ROLE_SupportCallcenter">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_bankteller">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_parameter">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
			 <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div> 
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_audit">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_bankadmin">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_informationdesk">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_accounting">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_operation">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_supportbank">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_branchmanager">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_relationshipmanager">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				<authz:authorize ifAllGranted="ROLE_personalrelationexec">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<input name="fromDate" id="fromDate" value="${fromDate}" class="form-control datepicker" onclick="  "></input>
					<font color="red"></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<input name="toDate" id="toDate" value="${toDate}" class="form-control datepicker" onclick=""></input>
					<font color="red"></font>
				</div>
				</authz:authorize>
				
			</div> 
			<div class="box-footer">
				<input type="button"  value="<spring:message code="LABEL_SEARCH"/>" class="btn btn-primary pull-right" onclick="validate();" style="margin-right: 60px;" />
				<br/><br/>
			</div>
		
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
				
				<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
					
					<authz:authorize ifAnyGranted="ROLE_bankadmin"> 
		                
		                    	<a href="#" class="mrg" onclick="javascript:apprOrRejectCust('Approve','customerViewForm');">
		                    	<img src="<%=request.getContextPath()%>/images/approved.png" /><span>
		                    	<spring:message code="LABEL_APPROVE" text="Approve"/></span></a>
		                    	
		                    	
		                    	<a href="#" class="mrg" onclick="javascript:apprOrRejectCust('Reject','customerViewForm');"
		                    	data-toggle="modal"  class="btn btn-default ">
		                    	<img src="<%=request.getContextPath()%>/images/rejected.png" /><span>
		                    	<spring:message code="LABEL_REJECT" text="Reject"/></span></a></span>
					
	                    </authz:authorize>
	                 <authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_bankteller">
						<span style="float:right; margin-right:1%">
							<a  href="javascript:submitForm('customerViewForm','exportToXlsForBlockedApplicationDetails.htm')"><strong><spring:message code="LABEL_BLOCK_CUSTOMER" text="Blocked Customers" /></strong></a>
						</span>
					</authz:authorize>
					<span style="float:right; margin-right: 8px;">
						<a href="#" onclick="javascript:agentDetailsExcel();" style="text-decoration:none;margin-left:-11px"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
						</a>
					</span>	
					
					<span style="margin-right: 30px; float:right">
						<a href="#" style="text-decoration:none;margin-left:10px" onclick="javascript:agentDetailsPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
						</a>
					</span>	
								
   			   </div>
				
					<table id="example1" class="table table-bordered table-striped container-outer" style="text-align:center;">
						<thead>
							<tr>
								<authz:authorize ifAnyGranted="ROLE_bankadmin"> 
									<th>
				                     	<input type="checkbox" id="checkAll_id" onclick="javascript:checkAll(this.id);"/>
				                    </th>
			                    </authz:authorize>
								<th><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></th>
								<th><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_STATUS" text="Status"/></th>
								<th><spring:message code="LABEL_GENDER" text="Gender"/></th>
								<th><spring:message code="LABEL_REGISTEREDDATE" text="Registered Date"/></th>
								<th><spring:message code="LABEL_CITY" text="City"/></th>
								<th><spring:message code="LABEL_ONBOARDED_BY" text="OnBoarded By"/></th>								
								<th><spring:message code="AG_NAME" text="Agent Name"/></th>
								<th><spring:message code="SUPER_AGENT_CODE" text="Super Agent Code"/></th>
								<th><spring:message code="SUPER_AGENT_NAME" text="Super Agent Name"/></th>
								<th><spring:message code="LABEL_KYC_STATUS" text="KYC Status"/></th>
								<th><spring:message code="LABEL_APPROVED_BY" text="Approved By"/></th>
								<th><spring:message code="LABEL_APPROVAL_DATE" text="Approval Date"/></th>
								<th><spring:message code="LABEL_ACTION_EDIT = Action" text="Action"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
					            <c:forEach items="${page.results}" var="cust">
					            <c:set var="j" value="${ j+1 }"></c:set>
					            <tr>
					            	
								 <authz:authorize ifAnyGranted="ROLE_bankadmin"> 
									<td> <c:if test="${cust.kyc_status eq 1}">
				                     	<input type="checkbox" name="manageCustomerCheck" value="${cust.CustomerID}" id="manageCustomer#<c:out value='${cust.kyc_status}'/>"/> 
				                     </c:if>	</td>
			                     </authz:authorize>
									
								 <%--    <td><c:out value="${cust.firstName} ${cust.middleName} ${cust.lastName}" /></td> --%>
								       <td><c:out value="${cust.CustomerName}" /></td>
								    
									<td><c:out value="${cust.MobileNumber}" /></td>		
									<c:if test="${cust.Active != 40}">
                                        <c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>
                                    </c:if>
                                    <c:if test="${cust.Active == 40}">
                                        <c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
                                    </c:if>
                                    <td><c:out value="${status}" /></td>		
                                    <c:if test="${cust.Gender eq 'Male' || cust.Gender eq 'male'}">
										<c:set var="gender" ><spring:message code="LABEL_MALE" text="Male" /></c:set>		    
                                    </c:if>
                                   	<c:if test="${cust.Gender eq 'Female' || cust.Gender eq 'female'}">
                                       	<c:set var="gender" > <spring:message code="LABEL_FEMALE" text="Female" /></c:set>
                                   	</c:if>
												
									<td><c:out value="${gender}" /></td>	
									<td>	
										<fmt:formatDate pattern = "dd-MM-yyyy HH:mm:ss"  value = "${cust.CreatedDate}" />
									</td>	
									<td><c:out value="${cust.City}" /></td>	
									<td>									
									<c:choose>
									<c:when test="${cust.OnbordedBy eq 'self' || cust.OnbordedBy eq 'Self' || cust.OnbordedBy eq 'SELF'}">												    
                       			 	<c:set var="onBordedBy" ><spring:message code="LABEL_SELF" text="Self" /></c:set>
	                 				 	 </c:when>
	                 				 	 <c:otherwise>
	                 				 	 <c:set var="onBordedBy" value="${cust.OnbordedBy}"></c:set>
	                 				 	 </c:otherwise>
	                 				 	 </c:choose>
									<c:out value="${onBordedBy}" />	
									</td>
									 <td><c:out value="${cust.AgentName}" /></td>
									 <td><c:out value="${cust.Code}" /></td>
									 <td><c:out value="${cust.Name}" /></td>
									 
									<c:if test="${cust.kyc_status == 0}">
                                        <c:set var="kycStatus" ><spring:message code="LABEL_KYC_PENDING" text="KYC Pending"/></c:set>
                                    </c:if>
                                    <c:if test="${cust.kyc_status == 1}">
                                        <c:set var="kycStatus" ><spring:message code="LABEL_KYC_APPROVE_PENDING" text="KYC Approval Pending"/></c:set>
                                    </c:if>
                                    <c:if test="${cust.kyc_status == 11}">
                                        <c:set var="kycStatus" ><spring:message code="LABEL_KYC_APPROVED" text="KYC Approved"/></c:set>
                                    </c:if>
                                    <c:if test="${cust.kyc_status == 21}">
                                        <c:set var="kycStatus" ><spring:message code="LABEL_KYC_REJECTED" text="KYC Rejected"/></c:set>
                                    </c:if>
                                               
                                    <td><c:out value="${kycStatus}" /></td>
                                    <td><c:out value="${cust.ApprovedBy}" /></td>                                  
                                    <td><fmt:formatDate pattern = "dd-MM-yyyy HH:mm:ss"  value = "${cust.ApprovalDate}" /></td>
									<td>
										<a href="javascript:customerDetail('viewCustomer.htm','<c:out value="${cust.CustomerID}"/>')"><spring:message code="LABEL_VIEW" text="Edit" /></a>
									</td>
								</tr>
								</c:forEach>
						</tbody>
					</table>
					
					
				<%-- <div class="box">
				<div class="box-body table-responsive"  id="table" style="padding: 20px 10px 20px 10px;">
					
					<table id="txnSummaryData" class="table table-bordered table-striped horizontal-scroll-wrapper">
					<form:select class="dropdown chosen-select" id="recordsLength" path="" >
						<form:option value="10">10</form:option>
						<form:option value="20">20</form:option>
						<form:option value="30">30</form:option>	
						</form:select>
						<thead>
							<tr>
							
								<th><input type="checkbox" id="checkAll_id" onclick="javascript:checkAll(this.id);"/></th> 
								<th><spring:message code="LABEL_SL_NO" text="Sl.no."/></th> 		
								<th><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></th>
								<th><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_STATUS" text="Status"/></th>
								<th><spring:message code="LABEL_GENDER" text="Gender"/></th>
								<th><spring:message code="LABEL_CREATED_DATE" text="Registered Date"/></th>
								<th><spring:message code="LABEL_CITY" text="City"/></th>
								<th><spring:message code="LABEL_ONBOARDED_BY" text="OnBoarded By"/></th>
								<th><spring:message code="LABEL_KYC_STATUS" text="KYC Status"/></th>
								<th><spring:message code="LABEL_ACTION_EDIT = Action" text="Action"/></th>
							</tr>
						</thead>
					</table>
				</div>
	</div>	 --%>
	
				</div>
				<input type="hidden" name="manageCustomer" id="manageCustomer"/>
				<input type="hidden" name="kycStatus" id="kycStatus"/>
				<input type="hidden" name="approvalType" id="approvalType" value="Bulk"/>
	</div>	
</div>

 <%-- <c:if test="${displayRejectDialog eq true}">  --%>
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" onclick="closeModel()" data-dismiss="modal">&times; </button>
          <h4 class="modal-title">Reason for rejection?</h4>
        </div>
        <div class="modal-body">
          <input class="form-control" style="width: 100%;" name="rejectComment" id="rejectComment" type="text"/>
        </div>
        <div class="modal-footer">
          <!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
          <button type="button" class="btn btn-default" onclick="apprOrRejectCustSubmit(true,'Reject','customerViewForm')">Submit</button>
        </div>
      </div>
      
    </div>
  </div>
  <%-- </c:if>  --%>
  
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
//$("#firstName").autocomplete({source:arr});
  </script>
	</body>
</html>
