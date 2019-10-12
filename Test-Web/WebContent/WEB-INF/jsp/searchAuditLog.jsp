<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>	

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
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
	    //ifFormat          : "%d/%m/%Y %H:%M",
	    ifFormat          : "%d/%m/%Y",
	    daFormat          : "%Y/%m/%d",
	    showsTime		  : false,
	    timeInterval      : 01
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
	
	
</script>

</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_AUDIT_LOG" text="Audit Logs" /></span>
		</h3>
	</div><br/>
	<div class="col-sm-6 col-sm-offset-5" id="msg" style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message code="${message}" text="" /></div>
	<form:form name="searchAuditLogForm" id="searchAuditLogForm" class="form-inline"  commandName="auditLogDTO">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_MODULE" text="Module"/></label> 
					<form:select path="module" cssClass="dropdown chosen-select">
						<form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--">
						</spring:message></form:option>
						<%-- <form:option value="Bank">Bank</form:option> --%>
						<form:option value="Biller">Biller</form:option>
						<%-- <form:option value="Branch">Branch</form:option> 
						<form:option value="ClearingHousePool">ClearingHousePool</form:option>--%>
						<form:option value="Customer">Customer</form:option>
						<%-- <form:option value="CustomerCard">CustomerCard</form:option>
						<form:option value="CustomerProfiles">CustomerProfiles</form:option> --%>
						<form:option value="Profiles">Profiles</form:option>
						<%-- <form:option value="ElectricityBill">ElectricityBill</form:option> --%>
						<form:option value="MobileRequest">MobileRequest</form:option>
						<form:option value="Operator">Operator</form:option>
						<form:option value="OperatorDenomination">OperatorDenomination</form:option>
						<form:option value="OperatorVoucher">OperatorVoucher</form:option>
						<form:option value="ServiceChargeRule">ServiceChargeRule</form:option>
						<%-- <form:option value="StakeHolder">StakeHolder</form:option> --%>
						<form:option value="TransactionRule">TransactionRule</form:option>
						<form:option value="WebUser">WebUser</form:option>
						</form:select>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_USER_ID" text="User ID"/></label>
					<form:input path="userId" id="autocomplete" cssClass="form-control" /> 
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_FROM_DATE" text="fromDate" /></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly" id="fromDate" cssClass="form-control datepicker"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_TO_DATE" text="To" /></label> 
					<form:input path="toDate"  id="toDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"/>
				</div>
			</div><br>
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="javascript: submitAuditForm(this.form);"></input><br/><br/>
			</div>
</div>
</form:form>
</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="tableData" class="table table-bordered table-striped" style="text-align:center;width: 100%">
						<thead>
							<tr>
								<th><spring:message code="LABEL_USER_ID" text="User ID"/></th>
								<th><spring:message code="LABEL_DATE_TIME" text="DateTime"/></th>
								<th><spring:message code="LABEL_MODULE" text="Module"/></th>
								<th><spring:message code="LABEL_FIELD_NAME" text="Field Name"/></th>
								<th><spring:message code="LABEL_MESSAGE" text="Message"/></th>
								<th><spring:message code="LABEL_NEW_VALUE" text="New Value"/></th>
								<th><spring:message code="LABEL_OLD_VALUE" text="Old Value"/></th>
							</tr>
						</thead>
						<%-- <tbody>
				            <c:forEach items="${page.results}" var="log">
				            
				            <tr>
				            
							    <td><c:out value="${log.updatedBy}" /></td>
							    <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${log.updatedDate}" /></td>		
							    <td align="left" width="20"><c:out value="${log.entityId}" /></td>
							    <td><c:out value="${log.entityName}" /></td>
							    <td><c:out value="${log.entityAttribute}" /></td>
							    <td><c:out value="${log.message}" /></td>
							    <td style="word-break:break-all;">
							  
							    <c:choose> <c:when test="${!fn:startsWith(log.newValue,'com.thinkways') }"> 
							   	  <c:choose>
							    <c:when test="${fn:contains(log.newValue, 'com.eot.entity.')}">  
								     <c:forEach var="item" items="${fn:split(log.newValue,'.')}" varStatus="loop">
								     <c:if test="${loop.index != 0 && loop.index != 1 && loop.index != 2}">																					
												     ${item}  
									 </c:if>    
									</c:forEach>
							     </c:when>
							     <c:otherwise>
							     <c:forEach var="item" items="${fn:split(log.newValue,'')}" varStatus="loop">
							       ${item} 
							       </c:forEach></c:otherwise> 
							     </c:choose>
							    </c:when>							    
							    <c:otherwise>N/A</c:otherwise> 
							    </c:choose>
							    </td>
							    <td style="word-break:break-all;">
							    <c:choose> <c:when test="${!fn:startsWith(log.oldValue,'com.thinkways')}"> 
							   <c:choose>
							    <c:when test="${fn:contains(log.oldValue, 'com.eot.entity.')}">  
								     <c:forEach var="item" items="${fn:split(log.oldValue,'.')}" varStatus="loop">
								     <c:if test="${loop.index != 0 && loop.index != 1 && loop.index != 2}">																					
												     ${item}  
									 </c:if>    
									</c:forEach>
							     </c:when>
							     <c:otherwise>
							     <c:forEach var="item" items="${fn:split(log.oldValue,'')}" varStatus="loop">
							       ${item} 
							       </c:forEach></c:otherwise> 
							     </c:choose>
							    </c:when> 
							    <c:otherwise>N/A</c:otherwise> 
							    </c:choose>	    </td>
							   
							</tr>
							</c:forEach>
						</tbody> --%>
					</table>
				</div>
	</div>
	
</div>

</body>
<script type="text/javascript">window.onload=function(){check();check1();};</script>
<script type="text/javascript">
var a ;
var arr=new Array();
a="<c:out value='${userList}'/>";
var c=a.substring(a.indexOf ('[')+1,a.lastIndexOf(']'));

for(var i=0;i<c.split(",").length;i++){
	//arr[i]="\""+c.split(",")[i]+"\"";
	arr[i]=c.split(", ")[i];
	//alert(arr[i])
}
$(document).ready(function() {
    $("#autocomplete").autocomplete({
    source:arr
           
});
  });
  
  
function submitAuditForm(form){
        var fromDate=document.getElementById("fromDate").value;
        var toDate=document.getElementById("toDate").value;            
 		if(fromDate!="" && toDate!=""){     			
        	/* if(fromDate == toDate){
           		alert("<spring:message code='SAME_DATES' text='From Date and To Date are similar'/>");
           		return false;
         	} */ if(!validdate(fromDate,toDate)){
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
 						if(!validToDate(toDate)){
	        	 					return false;  
	        	 	
	        				}
     			if (!compareDate(toDate)){
                    alert("<spring:message code='LABEL.TODATE.TODAY' text='Please enter To Date Less than Todays date'/>");
                    return false;
                 }
     			 
     	}
 		
		form.action="searchAuditLogForm.htm";
		form.submit();
	
} 
  
</script>

</html>
