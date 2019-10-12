<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>

<head>

<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type='text/javascript' src='https://code.jquery.com/jquery-2.1.1.min.js'></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/cusRegApp.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>

<script type="text/javascript"> 
var Alertmsg={
		 "fromDateFormat":"<spring:message code="VALID_ACCOUNT_REPORT_FROM_DATE_FORMAT" text="From date format must be : dd/mm/yyyy"/>",
		 "toDateFromat":"<spring:message code="VALID_ACCOUNT_REPORT_TO_DATE_FORMAT" text="To date format must be : dd/mm/yyyy"/>",
		 "validFromDay":"<spring:message code="VALID_ACCOUNT_REPORT_FROM_DATE" text="Please enter valid from date"/>",
		 "validToDay":"<spring:message code="VALID_TO_DAY" text="Please  enter  a valid to date"/>",
		 "validFromMonth":"<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		 "validToMonth":"<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		 "validfDay":"<spring:message code="VALID_FROM_DAY" text="Please  enter  a valid  days in from date"/>",
		 "validTodays":"<spring:message code="VALID_TO_DAYS" text="Please  enter  a valid  days in to date"/>",
		 "validFMonth":"<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		 "validToMonth":"<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		 "validTxnId" : "<spring:message code="VALID_TXN_ID" text="Please enter valid transaction id"/>",		
		 
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

 
  	function searchTxnReport(){	  	
  		 var fromDate=document.getElementById("fromDate").value;
         var toDate=document.getElementById("toDate").value;  
         var txnId=document.getElementById("txnId").value;

         if(isNaN(txnId)){
        	 alert(Alertmsg.validTxnId);
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
         submitlink("cusAppCommReport.htm","cusRegAppform");
  	}
  	
   $(document).ready(function() {

	   var tableOfResults=null;
	    tableOfResults = $('#txnSummaryData').DataTable({
			"processing": true,
			"serverSide": true,
			"paging" : true,
			"searching": false,
			"lengthChange": false,
			"scrollX": true,
			"SearchTerm":"",
		    /* "Page":2,
		    "Draw":10,
		    "EntityToSearch":"1", */
			  "ajax" : {
				"url" : 'showTxnSummaryDataCustomerRegistration.htm',
				"type" : "POST",
				  "data": function (data) {
					 	var SearchParameters = {};
		                var info = (tableOfResults == null) ? { page: 0, length: 10 } : tableOfResults.page.info();
		                SearchParameters.SearchTerm = '';
		                SearchParameters.pageNumber = data.start/data.length + 1;
		                SearchParameters.Draw = data.draw;

						/* var additionalArgs = ["mobileNumber","bankGroupId","bank","bankId","branchId","branch","location","transactionType","profileId","profileName","fromDate","toDate"]; */ 
						var additionalArgs = ["mobileNumber","bankGroupId","bank","bankId","branchId","branch","location","transactionType","profileId","profileName","status","fromDate","toDate","requestChannel"];
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
		                
		                return SearchParameters;
		            },
		        	"error":function(errorThrown){
		        		ShowDataTable(null);
			        }
			}, 
			"columns": [
				{ "data": "SerNo" }, 
	            { "data": "TransactionID" },
	            { "data": "amount" },
	            { "data": "BusinessName" },
	            { "data": "FirstName" },
	            { "data": "MobileNumber" },
	            { "data": "AgentName" },
	            { "data": "Agent_Code" },
	            { "data": "AgentMobileNumber" },
	            { "data": "SuperAgentName" },
	            { "data": "SuperAgentCode" },
	            { "data": "TransactionType" },
	            { "data": "TransactionDate" }
	            
	           
	        ],
	        
			"columnDefs": [ {"ordering": true},
			{ "orderable": false, "targets": 0 }
],
	       
	        "order": [[ 1, 'desc' ]]
		});




	   
	   	$("body").on("change","#bankId", function() {			
			$bankId = document.getElementById("bankId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getBranchesForTxnReport.htm", {
				bankId : $bankId,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("branchdiv").innerHTML="";
				document.getElementById("branchdiv").innerHTML = data;
				setTokenValFrmAjaxResp();
				applyChosen();
			});
			
		});
		
		$("#branchdiv").change(function() {
			document.getElementById("branch").value = document.getElementById("location").value ;
		});
		
		$("#branchId").change(function() {
			document.getElementById("branch").value = document.getElementById("branchId").value ;			
		});

	   	$("body").on("change","#bankId", function() {
			$bankId = document.getElementById("bankId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getCustomerProfilesForTxnReport.htm", {
				bankId : $bankId,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("profiles").innerHTML="";
				document.getElementById("profiles").innerHTML = data;
/* 				setTokenValFrmAjaxResp();
				applyChosen(); */
			});
		});		

		$("#profiles").change(function() {
			document.getElementById("profileId").value = document.getElementById("profileName").value ;
		});	
		
		/* $("#bankdiv").change(function() {	
			document.getElementById("bank").value = document.getElementById("bankName").value ;
		}); */

	/* 	commenting to make a default country as SouthSudan,
		by vineeth on 13-11-2018
		
	 	$("body").on("change","#countryId", function() {
				$countryId = document.getElementById("countryId").value;
				$csrfToken = $("#csrfToken").val();
				$.post("getBanksByCountry.htm", {
					countryId : $countryId,
					csrfToken : $csrfToken
				}, function(data) {
					document.getElementById("bankdiv").innerHTML="";
					document.getElementById("bankdiv").innerHTML = data;
					$('#bankId').trigger('change');
		//			 document.getElementById("branchdiv").innerHTML="";
		//			document.getElementById("branchdiv").innerHTML = data; 
					setTokenValFrmAjaxResp();
					applyChosen();
				});
			});  */


		$("#partnerType").change(function() {
			//alert(this.value)
			//this.form.submit();
			$csrfToken = $("#csrfToken").val();
			$.post("getPartnersByType.htm", {
				partnerType : this.value,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("partners").innerHTML="";
				document.getElementById("partners").innerHTML = data;
				//setTokenValFrmAjaxResp();
				applyChosen();
			});
		});

					
	});
  
    var dminyear = 1900;  
   var dmaxyear = 2200;  
   var chsep= "/" ; 
   function checkinteger(str1){  
   var x;  
   for (x = 0; x < str1.length; x++){   
     
   var cr = str1.charAt(x);  
   if (((cr < "0") || (cr > "9")))   
   return false;  
   }  
   return true;  
   }  
   function getcharacters(s, chsep1){  
   var x;  
   var Stringreturn = "";  
   for (x = 0; x < s.length; x++){   
   var cr = s.charAt(x);  
   if (chsep.indexOf(cr) == -1)   
   Stringreturn += cr;  
   }  
   return Stringreturn;  
   }  
   function februarycheck(cyear)  
   {  
   return (((cyear % 4 == 0) && ( (!(cyear % 100 == 0)) || (cyear % 400 == 0))) ? 29 : 28 );  
   }  
   function finaldays(nr) {  
   for (var x = 1; x <= nr; x++) {  
   this[x] = 31;  
   if (x==4 || x==6 || x==9 || x==11)  
   {  
   this[x] = 30}; 
   if (x==2)  
   {  
   this[x] = 29};  
   }   
   return this;  
   }   
   var dminyear = 1900;  
   var dmaxyear = 2200;  
   var chsep= "/" ; 
   function checkinteger(str1){  
   var x;  
   for (x = 0; x < str1.length; x++){   
     
   var cr = str1.charAt(x);  
   if (((cr < "0") || (cr > "9")))   
   return false;  
   }  
   return true;  
   }  
   function getcharacters(s, chsep1){  
   var x;  
   var Stringreturn = "";  
   for (x = 0; x < s.length; x++){   
   var cr = s.charAt(x);  
   if (chsep.indexOf(cr) == -1)   
   Stringreturn += cr;  
   }  
   return Stringreturn;  
   }  
   function februarycheck(cyear)  
   {  
   return (((cyear % 4 == 0) && ( (!(cyear % 100 == 0)) || (cyear % 400 == 0))) ? 29 : 28 );  
   }  
   function finaldays(nr) {  
   for (var x = 1; x <= nr; x++) {  
   this[x] = 31;  
   if (x==4 || x==6 || x==9 || x==11)  
   {  
   this[x] = 30}; 
   if (x==2)  
   {  
   this[x] = 29};  
   }   
   return this;  
   }   
   function validdate(fromDate,toDate)  
   {  

   if (dtvalid(fromDate,toDate)==false)  
   {  

   return false;  
   }  
   return true ; 
   }  

   function dtvalid(fromDate,toDate)
   {
   //var DateOfBirth=new String("12/912/2012");
   	var monthdays = finaldays(12);  
   var fChar1 = fromDate.charAt(2);
   var fChar2 = fromDate.charAt(5);
   var chsep= "/" ;
   var fcpos1=fromDate.indexOf(chsep); 
   var fcpos2=fromDate.indexOf(chsep,fcpos1+1);  

   if (fcpos1==-1 || fcpos2==-1 ){  
   	
   alert(Alertmsg.fromDateFormat);  
   return false;  
   } 

   if (fromDate.length<10 || fromDate.length>10){  

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

   day = fromDate.substring(0,2);
   month = fromDate.substring(3,5);
   year = fromDate.substring(6,10);

   var tChar1 = toDate.charAt(2);
   var tChar2 = toDate.charAt(5);
   var chsep= "/" ;
   var tcpos1=toDate.indexOf(chsep); 
   var tcpos2=toDate.indexOf(chsep,tcpos1+1);  
   if (tcpos1==-1 || tcpos2==-1 ){  
   alert(Alertmsg.toDateFromat);  
   return false;  
   } 

   if (fromDate.length<10 || fromDate.length>10){  

   	alert(Alertmsg.fromDateFormat);  
   	return false;  
   } 
   /*if ( Char1 =='/' && Char2 == '/' )
   {
    alert ('Please check foramt');
   return false;
   }*/




   if(!validDay(day))
   {
   alert(Alertmsg.validFromDay);

   return false;
   }

   if(!validMonth(month)){	
   alert(Alertmsg.validFromMonth);

   return false;
   }

   if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
   	
   	alert(Alertmsg.validFromDay);  
   	return false ; 
   	} 


   var tday;
   var tmonth;
   var tyear;

   tday = toDate.substring(0,2);
   tmonth = toDate.substring(3,5);
   tyear = toDate.substring(6,10);

   if (toDate.length<10 || toDate.length>10){  

   	alert(Alertmsg.toDateFromat);  
   	return false;  
   } 


   if(!validDay(tday))
   {
   alert(Alertmsg.validToDay);

   return false;
   }

   if(!validMonth(tmonth)){	
   alert(Alertmsg.validToMonth);

   return false;
   }

   if ((tmonth==2 && tday>februarycheck(tyear))   || tday > monthdays[tmonth]){  
   	
   	alert(Alertmsg.validToDay);  
   	return false ; 
   	} 


   /*if(!validYear(year)){
   		
   alert('Invalid year in date of birth');
   DateOfBirth=null;
   return false;
   }*/

   } // end func




   function validDay(day)
   {
   	
   if (IsNumeric(day) && day.length<3)
   {
   if( day >0 && day <32)
   {
   return true;
   }
   else
   {
   return false;
   }
   }
   else
   {
   return false;
   }

   }// end func
   function IsNumeric(sText)
   {
   	
   var ValidChars = "0123456789.";
   var IsNumber=true;
   var Char;

   for (var i = 0; i < sText.length && IsNumber == true; i++)
   {
   Char = sText.charAt(i);
   if (ValidChars.indexOf(Char) == -1)
   {
   IsNumber = false;
   }
   }

   return IsNumber;
   } // end func

   function validMonth(month)
   {
   if ( IsNumeric(month) )
   {
   if( month >0 && month <13)
   {
   return true;
   }
   else
   {
   return false;
   }
   }
   else
   {
   return false;
   }
   }// end func



   function validYear(year)
   {
   var d = new Date();
   var currentYear = d.getFullYear();

   if( year.length!= 4) { return false; }

   if ( IsNumeric(year) )
   {
   if( year >0 && year <=currentYear)
   {
   return true;
   }
   else
   {
   return false;
   }

   }
   else
   {
   return false;
   }

   }
   

	function misExcel(){
			
			 submitlink("xlsCusRegReport.htm","cusRegAppform"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
			 $.unblockUI();
		}
	
	function misPDF(){
			
			 submitlink("pdfCusRegReport.htm","cusRegAppform"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
		}
 	
	function ExportFile(url,form){
		var fromDate=document.getElementById("fromDate").value;
	    var toDate=document.getElementById("toDate").value;  
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
		submitlink(url,form);
		$("body").css("cursor", "default");
	}		
		
</script>
</head>
<body onload="check(),check1()">
<form:form class="form-inline" name="cusRegAppform" id="cusRegAppform" method="post" commandName="txnSummaryDTO">
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
		
			<span><spring:message code="LABEL_CUS_REG_COMM_RPT" text="MIS"/></span>
		</h3>
	</div>
		<div class="col-sm-6 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
		</div>
	<div class="box-body">
		
			<authz:authorize ifAnyGranted="ROLE_superadmin,ROLE_bankadmin,ROLE_accounting,ROLE_businesspartnerL2">
				
				<form:hidden path="profileId" />
				<form:hidden path="branch" />
				<form:hidden path="bank" />
				
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CUS_NAME" text="Mobile Number"></spring:message></label> 
					<form:input id="customerName" path="customerName"  type="text" cssClass="form-control" />
				</div>
				 <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_CUST_MOBILE" text="Agent/Merchant Code"/></label>
					<form:input id="mobileNumber" path="mobileNumber" type="text"  cssClass="form-control"/> 
				</div> 
			</div>
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_AGENT_NAME" text="Mobile Number"></spring:message></label> 
					<form:input id="agentName" path="agentName"  type="text" cssClass="form-control" /> 
				</div>
				 <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_AGENT_MO" text="Agent/Merchant Code"/></label>
					<form:input id="agentMobileNumber" path="agentMobileNumber" type="text"  cssClass="form-control"/> 
				</div> 
			</div>
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_AGENT_CODE" text="Mobile Number"></spring:message></label> 
					<form:input id="merchantCode" path="merchantCode"  type="text" cssClass="form-control" />
				</div>
				  <div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TRANSACTION_ID" text="Agent/Merchant Code"/></label>
					<form:input id="txnId" path="txnId" type="text"  cssClass="form-control"/> 
				</div> 
			</div>
				<authz:authorize ifAnyGranted="ROLE_superadmin,ROLE_bankadmin,ROLE_accounting">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_SUPER_AGENT_CODE" text="Mobile Number"></spring:message></label> 
					<form:input id="superAgentCode" path="superAgentCode"  type="text" cssClass="form-control" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BUSINESS_PARTNER_NAME" text="Agent/Merchant Code"/></label>
					<form:input id="superAgentName" path="superAgentName" type="text"  cssClass="form-control"/> 
				</div>
				 
			</div>
			</authz:authorize>
			<%-- <div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BUSNIESS_NAME" text="Business Name"></spring:message></label> 
					<form:input id="businessName" path="businessName"  type="text" cssClass="form-control" />
				</div>
			</div> --%>
			
			<authz:authorize ifAnyGranted="ROLE_bankadmin">
				<form:hidden path="bankId" value="${masterData.bankId}"/>
			</authz:authorize>
			
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate"  cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="fromDate" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label> 
					<form:input path="toDate"  cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="toDate" /></font>
				</div>
			</div><br/>
			</authz:authorize>
			
			
			
			
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SEARCH"/>" id="searchId" onclick="searchTxnReport();" />
				<br/><br/>
			</div>
		
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive"  id="table" style=" width: 100%;">
				
				<div class="exprt" style="margin-bottom: 14px;margin-right: 3px; float:right;">
                	<%-- <span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span> --%>
					<span>
						<a href="#" onclick="javascript:misExcel();" style="text-decoration:none;margin-left:-11px;"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
							<%-- <span><spring:message code="LABEL_EXCEL" text="Excel"/></span> --%>
						</a>
					</span>
					
					<span>
					<a href="#" style="text-decoration:none;margin-left: 10px;" onclick="javascript:misPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
						<%-- <span><spring:message code="LABEL_PDF" text="PDF"/></span> --%>
					</a>
					</span>					
   			   </div>
				
					<table id="txnSummaryData" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_SL_NO" text="Sl.no."/></th> 
								<th><spring:message code="LABEL_TRANSACTION_ID" text="Transaction Id" /></th>
								<th><spring:message code="LABEL_COMMISSION_AMT" text="Name" /></th>
								<th><spring:message code="LABEL_BUSNIESS_NAME" text="Business Name" /></th>
								<th><spring:message code="LABEL_CUS_NAME" text="Name" /></th>
								<th><spring:message code="LABEL_CUST_MOBILE" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_AGENT_NAME" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_AGENT_CODE" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_AGENT_MO" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_BUSINESS_PARTNER_NAME" text="Agent/Merchant Code"/></th>
								<th><spring:message code="LABEL_SUPER_AGENT_CODE" text="Amount"/></th>
								<th><spring:message code="LABEL_TXN_TYPE" text="Txn Type"/></th>
								<th><spring:message code="LABEL_TRANSACTION_DATE_TIME" text="Transaction Date & Time"/></th>
							</tr>
						</thead>
					</table>
				</div>
	</div>
	
</div>
</form:form>
	
</body>

</html>
