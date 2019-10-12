<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
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

<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>
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

function  voidTxn(){	
	return confirm("<spring:message code='ALERT_VOID' text='Are you sure you want to reverse The transaction'/>");
}
function cancelTxn(){	
	return confirm("<spring:message code='ALERT_CANCEL' text='Are you sure you want to Cancel The Transaction'/>");
}
function rejectTxn(){
	return confirm("<spring:message code='ALERT_REJECT' text='Are you sure you want to Reject The Transaction'/>");
}
function displayHideBox(boxNumber)
{
    if(document.getElementById("LightBox"+boxNumber).style.display=="none") {
        document.getElementById("LightBox"+boxNumber).style.display="block";
        document.getElementById("grayBG").style.display="block";
    } else {
        document.getElementById("LightBox"+boxNumber).style.display="none";
        document.getElementById("grayBG").style.display="none";
    }
}
function displayDepositDiv(){
	
			document.getElementById("transactionDiv").style.visibility ="visible" ;
			displayHideBox("1");
	
	
}
function displayHideBox(boxNumber)
{
    if(document.getElementById("LightBox"+boxNumber).style.display=="none") {
        document.getElementById("LightBox"+boxNumber).style.display="block";
        document.getElementById("grayBG").style.display="block";
    } else {
        document.getElementById("LightBox"+boxNumber).style.display="none";
        document.getElementById("grayBG").style.display="none";
    }
}


	function submitURL(formId, URL) {
		
		var frmId = document.getElementById(formId);
		var anchorTags = document.getElementsByTagName("a");
			if (frmId != null) {
				if (approve()) {
					for(var i=0;i<anchorTags.length;i++){
						anchorTags[i].removeAttribute("href");
					}
					status = false;
					frmId.method = "post";
					frmId.action = URL;
					frmId.submit();
				}
			}
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
	
	 function searchSubmit(){
		 var numericPattern=  /^[1-9]\d*(\.\d+)?$/;
		 var amount = document.getElementById("amount").value;
		 var fromDate = document.getElementById("fromDate").value;
		 var toDate = document.getElementById("toDate").value;
		 
		 if(amount != ""){
		       if(!amount.match(numericPattern)){
			 alert("please enter valid amount");
			 return false;
		 }
		 }
		 if(fromDate!="" && toDate!=""){
        	 if(!validdate(fromDate,toDate)){
        		 	return false;  
        		}
        	 if(!compareFromDate(fromDate,toDate)){
                 alert("<spring:message code='COMPARE_DATES' text='From Date should not be greater than To Date'/>");
                 return false;
             }    
                          
          }    
                     
          if(fromDate!=""){
        	  if(!validFromDate(fromDate)){
      		 	return false;  
      		}  
        	  if (!compareDate(fromDate)){
                  alert("<spring:message code='LABEL.FROMDATE.TODAY' text='Please enter From Date Less than Todays date'/>");
                   return false;
                  }
           }
          if(toDate!=""){
        	  if (!validToDate(toDate)){
                 
                    return false;
                   }
        	  if (!compareDate(toDate)){
                  alert("<spring:message code='LABEL.TODATE.TODAY' text='Please enter To Date Less than Todays date'/>");
                    return false;
                   }
          }   		
		 
		 document.cancellationForm.method = "post";
		 document.cancellationForm.action = "showAdjustmentForm.htm";
		 document.cancellationForm.submit();
	 }
	

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
		function validToDate(toDate)  
		{  
		 
		if (validateToDate(toDate)==false)  
		{  

		return false;  
		}  
		return true ; 
		}   
		function validateToDate(toDate)
		{
		//var DateOfBirth=new String("12/912/2012");
			var monthdays = finaldays(12);  
		var Char1 = toDate.charAt(2);
		var Char2 = toDate.charAt(5);
		var chsep= "/" ;
		var fcpos1=toDate.indexOf(chsep); 
		var fcpos2=toDate.indexOf(chsep,fcpos1+1);  
		if (fcpos1==-1 || fcpos2==-1 ){  
			
		alert(Alertmsg.toDateFromat);  
		return false;  
		} 

		if (toDate.length<10 || toDate.length>10){  

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

		day = toDate.substring(0,2);
		month = toDate.substring(3,5);
		year = toDate.substring(6,10);


		if(!validDay(day))
		{
		alert(Alertmsg.validToDay);
		return false;
		}

		if(!validMonth(month)){	
		alert(Alertmsg.validToMonth);
		
		return false;
		}

		if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
			
			alert(Alertmsg.validToDay);  
			return false ; 
			} 
		/*if(!validYear(year)){
				
		alert('Invalid year in date of birth');
		DateOfBirth=null;
		return false;
		}*/

		} // end func
		function validFromDate(fromdate)  
		{  
		 
		if (validateFromDate(fromdate)==false)  
		{  

		return false;  
		}  
		return true ; 
		}   
		function validateFromDate(fromDate)
		{
		//var DateOfBirth=new String("12/912/2012");
			var monthdays = finaldays(12);  
		var Char1 = fromDate.charAt(2);
		var Char2 = fromDate.charAt(5);
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


		if(!validDay(day))
		{
		alert(Alertmsg.validFromDay);
		DateOfBirth=null;
		return false;
		}

		if(!validMonth(month)){	
		alert(Alertmsg.validFromMonth);
		DateOfBirth=null;
		return false;
		}

		if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
			
			alert(Alertmsg.validFromDay);  
			return false ; 
			} 
		/*if(!validYear(year)){
				
		alert('Invalid year in date of birth');
		DateOfBirth=null;
		return false;
		}*/

		} // end func
		function validFromDate(fromdate)  
		{  
		 
		if (validateFromDate(fromdate)==false)  
		{  

		return false;  
		}  
		return true ; 
		}   

		 $(document).ready(function() {
			   $("#txnType").val($("#txnTypeId").val());
			});
</script>
<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>

<%-- <link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" /> --%>
</head>
<body onload="check(),check1()">
	<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_REVERSAL" text="Reversal Transactions" /></span>
		</h3>
	<br/>
	<div class="col-sm-5 col-sm-offset-3" style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message code="${message}" text="" /></div>
	</div>
	<br/>
	<form  class="form-inline" id="cancellation" name="cancellationForm" method="post">
	<jsp:include page="csrf_token.jsp"/>
	<div class="pull-right">
		<a href="javascript:submitForm('cancellation','uploadReversalDetails.htm')"><strong><spring:message code="LABEL_UPLOAD_REVERSAL" text="Upload Adjustment Details" /></strong></a>&nbsp; &nbsp; &nbsp;
	</div><br/><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"></spring:message></label> 
					<input id="mobileNumber" name="mobileNumber" class="form-control"  type="text" value="<c:out value="${mobileNumber}"/>" class="text_feild"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></label> 
					<input id="customerName" class="form-control" name="customerName" type="text" value="<c:out value="${customerName}" />" />
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_AMOUNT" text="Amount"></spring:message></label> 
					<input id="amount" name="amount"  type="text" value="<c:out value="${amount}"/>" class="form-control numbers"/>
				</div>
				<input type="hidden" id="txnTypeId" name="txnTypeId" value="${ txnType}"/>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXN_TYPE" text="Txn Type"/></label> 
					<select id="txnType" class="dropdown chosen-select" name="txnType">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<option value="128"><spring:message code="LABEL_PAY" text="m-GURUSH Pay"/></option>
						<option value="90"><spring:message code="LABEL_SALE" text="Sale"/></option>
						<%-- <option value="55"><spring:message code="LABEL_TRF_DIRECT" text="Send Money"/></option> --%>
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /></label> 
					<input name="fromDate" id="fromDate" onfocus="this.blur()"  value="${fromDate}" class="form-control datepicker" ></input>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TODATE" text="To" /></label> 
					<input name="toDate" id="toDate" onfocus="this.blur()"  value="${toDate}" class="form-control datepicker"></input>
				</div>
			</div><br/>
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SEARCH" text="Search"/>" onclick="searchSubmit();"></input>
			</div><br /><br />
			                                    <input type="hidden" id="transactionId" name="transactionId" />
												<input type="hidden" id="amount" name="amount" />
												<input type="hidden" id="customerName" name="customerName" />
												<input type="hidden" id="mobileNumber" name="mobileNumber" />
												<input type="hidden" id="transactionType" name="transactionType" />
												<input type="hidden" id="txnDate" name="txnDate" />
	
</div>

</form>
</div>
	
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name" /></th>
								<th><spring:message code="LABEL_MOBILE_NO" text="Mobile Number" /></th>
								<th><spring:message code="LABEL_TXN_TYPE" text="TXN Type" /></th>
								<th><spring:message code="LABEL_TXN_DATE" text="TXN Date" /></th>
								<th><spring:message code="LABEL_AMOUNT" text="Amount" /></th>
								<th><spring:message code="LABEL_TXN_ID" text="TXN ID" /></th>
								<th><spring:message code="LABEL_Action" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="txnDetails" varStatus="status" >
											<tr>
												<td><c:out value="${txnDetails.FirstName} ${txnDetails.LastName}" /></td>
												<td><c:out value="${txnDetails.MobileNumber}" /></td>
													<c:if test="${txnDetails.TransactionType==128}">
												<c:set var="status"><spring:message code="LABEL_PAY" text="m-GURUSH Pay"/></c:set>	
												</c:if>
												<c:if test="${txnDetails.TransactionType==90}">
												<c:set var="status"><spring:message code="LABEL_SALE" text="Sale"/></c:set>	
												</c:if>
												<c:if test="${txnDetails.TransactionType==55}">
												<c:set var="status"><spring:message code="LABEL_TRF_DIRECT" text="Send Money"/></c:set>	
												</c:if>	
												<td><c:out value="${status}"/></td>	
												<!-- by:rajlaxmi for:removing time from data table  -->											
												<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${txnDetails.TransactionDate}"/></td>	
												<fmt:setLocale value="en_US" scope="session"/>												
												<td text-align="right"><fmt:formatNumber value="${txnDetails.amount}" pattern="0.00"></fmt:formatNumber></td>
												<td><c:out value="${txnDetails.TransactionID}" /></td>
												<%-- <td><a href="javascript:submitForm('cancellation','viewAdjustmentTransaction.htm?transactionId=<c:out value="${txnDetails.TransactionID}"/>&amount=<c:out value="${txnDetails.amount}"/>&customerName=<c:out value="${txnDetails.FirstName} ${txnDetails.LastName}"/>&mobileNumber=<c:out value="${txnDetails.MobileNumber}"/>&transactionType=<c:out value="${txnDetails.TransactionType}"/>&txnDate=<c:out value="${txnDetails.TransactionDate}"/>')"/><spring:message code="LABEL_REVERSAL_ACTION" text="Reversal" /></td> --%>
												<td><a href="javascript:submitReversal('viewAdjustmentTransaction.htm','<c:out value="${txnDetails.TransactionID}"/>','<c:out value="${txnDetails.amount}"/>','<c:out value="${txnDetails.FirstName} ${txnDetails.LastName}"/>','<c:out value="${txnDetails.MobileNumber}"/>','<c:out value="${txnDetails.TransactionType}"/>','<c:out value="${txnDetails.TransactionDate}"/>')"/><spring:message code="LABEL_REVERSAL_ACTION" text="Reversal" /></td>
											</tr>
												
											</c:forEach>
											
									
						</tbody>
						
					</table>
				</div>
			
				
				
				
	</div>
	
</div>	
<script>
/* @Author name <vinod joshi>, Purpuse:- Cross site Scripting */
function submitReversal(url,TransactionID,amount,Name,MobileNumber,TransactionType,TransactionDate){
	   document.getElementById("transactionId").value =TransactionID ;
	   document.getElementById("amount").value =amount ;
	   document.getElementById("customerName").value =Name ;
	   document.getElementById("mobileNumber").value =MobileNumber ;
	   document.getElementById("transactionType").value =TransactionType ;
	   document.getElementById("txnDate").value =TransactionDate ;
	 submitlink(url,'cancellation');
	 
	 
}

	window.onload=function(){
	check();
	check1();
	};
</script>				
</body>
</html>
