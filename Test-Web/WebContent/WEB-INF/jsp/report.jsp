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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>	
<script type="text/javascript"  src="<%=request.getContextPath()%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/highcharts-3d.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/exporting.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>

<script>
	var pageSummData =  ${chartData};
	
</script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/js/transactionReportCharts.js"></script>
<script type="text/javascript"> 

var Alertmsg={
		
		 "fromDateFormat":"<spring:message code="VALID_TRANSACTION_REPORT_FROM_DATE_FORMAT" text="The  from date format must be : dd/mm/yyyy"/>",
		 "toDateFormat":"<spring:message code="VALID_TRANSACTION_REPORT_TO_DATE_FORMAT" text="The  to date format must be : dd/mm/yyyy"/>",
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

 	function resetForm(){
 		document.getElementById("transactionType").value="";
 		document.getElementById("fromDate").value="";
 		document.getElementById("toDate").value="";
 	}
 	
  	function searchChart(id){	  	
       var fromDate=document.getElementById("fromDate").value;
       var toDate=document.getElementById("toDate").value;   
       <authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
       var bank=document.getElementById("bankId").value;
       if(bank==""){
    	   alert("<spring:message code='LABEL.BANK.NOTEMPTY' text='Please Select Bank'/>");
    	   return false;
       }
       </authz:authorize>
       var today=new Date();    
       if(fromDate==""){
	   alert("<spring:message code='LABEL.FROMDATE' text='Please enter From Date'/>");
	   return false;
	   }else if (toDate==""){
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
       if(id=='barReport'){
       document.txnSummaryform.action="barChartDataValidation.htm";
       document.txnSummaryform.submit();
  	   }else
  	   document.txnSummaryform.action="pieChartDataValidation.htm";
       document.txnSummaryform.submit();      	
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
     alert(Alertmsg.toDateFormat);  
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

     	alert(Alertmsg.toDateFormat);  
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

</script>
<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>
</head>
<style>
.highcharts-credits{
display:none;
}
</style>
<body onload="check(),check1()">

<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_REPORT" text="Transaction Report"/></span>
		</h3>
	</div><br/>
	<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message code="${message}" text="" /> </div>
	<form:form name="txnSummaryform" id="txnSummaryform" class="form-inline" method="post" commandName="txnSummaryDTO">
	<jsp:include page="csrf_token.jsp"/>	
	<div class="box-body">
		<authz:authorize ifAnyGranted="ROLE_admin,ROLE_gimbackofficelead,ROLE_gimbackofficeexec">
			<div class="row">
				<div class="col-sm-7">
					<div class="col-sm-3">
					<form:radiobutton path="chartType" class="col-sm-3" value="gim" checked="true" id="gim" onchange="resetForm();" />
					<spring:message code="LABEL_GIM" text="Gim"/> 
					</div>
					<div class="col-sm-3">
					<form:radiobutton path="chartType" class="col-sm-3" value="country" id="country" onchange="resetForm();" />
					<spring:message code="LABEL_COUNTRY" text="Country"/>
					</div>
					<div class="col-sm-3">
					<form:radiobutton path="chartType" class="col-sm-3" value="bankGroup" id="bankGroup" onchange="resetForm();"/>
					<spring:message code="LABEL_BANKGROUP" text="Bank Group"/>
					</div>
					<div class="col-sm-3">
					<form:radiobutton path="chartType" class="col-sm-3" value="bank" id="bank" onchange="resetForm();"/>
					<spring:message code="LABEL_BANK" text="Bank"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></label> 
					<select class="dropdown chosen-select" id="transactionType" name="transactionType"  class="dropdown">
						<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
						<c:set var="lang" value="${language}"></c:set>
							<c:forEach items="${transTypeList}" var="transTypeList">
							<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
										 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
										 <option value="<c:out value="${transTypeList.transactionType}"/>"  <c:if test="${transTypeList.transactionType eq txnSummaryDTO.transactionType}" >selected=true</c:if> >
										<c:out 	value="${transactionTypesDescs.description}"/>	
										</option>
										</c:if>		  						
							</c:forEach>
							</c:forEach>
							</option>
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="fromDate" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label>
					<form:input path="toDate" onfocus="this.blur()" readonly="readonly"  cssClass="form-control datepicker"></form:input> 
					<font color="red"><form:errors path="toDate" /></font>
				</div>
			</div>
			</authz:authorize>
			
			<authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
			<div class="row">
					<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font></label>
					<form:select path="bankId" id="bankId" cssClass="dropdown">
						<form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message></form:option>
						<form:options items="${masterData.bankList}" itemValue="bankId" itemLabel="bankName"/></form:select> 
					</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></label> 
					<select class="dropdown" id="transactionType" name="transactionType"  class="dropdown">
						<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
						<c:set var="lang" value="${language}"></c:set>
							<c:forEach items="${transTypeList}" var="transTypeList">
							<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
										 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
										 <option value="<c:out value="${transTypeList.transactionType}"/>"  <c:if test="${transTypeList.transactionType eq txnSummaryDTO.transactionType}" >selected=true</c:if> >
										<c:out 	value="${transactionTypesDescs.description}"/>	
										</option>
										</c:if>		  						
							</c:forEach>
							</c:forEach>
							</option>
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="fromDate" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label>
					<form:input path="toDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"></form:input> 
					<font color="red"><form:errors path="toDate" /></font>
				</div>
			</div>
			</authz:authorize>
			<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_superadmin,ROLE_audit,ROLE_accounting">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></label> 
					<select class="dropdown" id="transactionType" name="transactionType"  class="dropdown">
						<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
						<c:set var="lang" value="${language}"></c:set>
							<c:forEach items="${transTypeList}" var="transTypeList">
							<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
										 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
										 <option value="<c:out value="${transTypeList.transactionType}"/>"  <c:if test="${transTypeList.transactionType eq txnSummaryDTO.transactionType}" >selected=true</c:if> >
										<c:out 	value="${transactionTypesDescs.description}"/>	
										</option>
										</c:if>		  						
							</c:forEach>
							</c:forEach>
							</option>
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"></form:input>
					<font color="red"><form:errors path="fromDate" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label>
					<form:input path="toDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"></form:input> 
					<font color="red"><form:errors path="toDate" /></font>
				</div>
			</div>
			</authz:authorize>
			<div class="col-sm-6 col-sm-offset-9">
		<div class="btn-toolbar">
			<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_BAR_REPORT"/>" id="barReport" onclick="searchChart(this.id);" />
			<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_PIE_REPORT"/>" id="pieReport" onclick="searchChart(this.id);" />
		</div>
	</div><br/><br/>
	</div>
	
	</form:form>
	<div class="row">
		
		<c:if test="${message eq null && imgHide ne 'imgHide'}">
		<div class="col-sm-6">
		<div style="font-weight: bold; font-size: 20px;">
			<spring:message code="LABEL_TRANSACTION_SUMMARY" text="TRANSACTION SUMMARY" />			                    	        
		</div>
		</div>
		</c:if>
		</div>
		<div class="row">
		<c:if test="${barReport ne 'barReport'}">	
		
		<div id="txnCountVsTxnType" style="min-width: 300px; max-width: 1000px; height: 400px; margin: 0 auto"></div>
         <br/>
        <div id="txnTypeVsTxnAmount" style="min-width: 300px; height: 400px; max-width: 1000px; margin: 0 auto"></div>
		
						
			<%-- <c:if test="${message eq null && imgHide ne 'imgHide'}">
			<div class="col-sm-6">
				<img id='img1' src="<c:out value="${imgUrl}"/>?transactionType=<c:out value="${transactionType}"/>&fromDate=<c:out value="${fromDate}"/>&toDate=<c:out value="${toDate}"/>&bankId=<c:out value="${bankId}"/>&chartType=<c:out value="${chartType}"/>&imgType=imgCount" width="400" height="400"/>	
			</div>	
			<div class="col-sm-6">
				<img id='img2' src="<c:out value="${imgUrl}"/>?transactionType=<c:out value="${transactionType}"/>&fromDate=<c:out value="${fromDate}"/>&toDate=<c:out value="${toDate}"/>&bankId=<c:out value="${bankId}"/>&chartType=<c:out value="${chartType}"/>&imgType=imgValue" width="400" height="400"/>
			</div>	
			</c:if> --%>
		</c:if>
<!-- 		<div id="pieReportForTxn" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>		 -->
		<c:if test="${barReport eq 'barReport'}">
		<div id="pieReportForTxn" style="min-width: 310px; height: 400px; max-width: 1000px; margin: 0 auto"></div>	
<!-- 		<div id="pieReportForTxnCount" style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>	
 -->			 <%-- <c:if test="${message eq null && imgHide ne 'imgHide'}">
			<div class="col-sm-6">
				<img id='img1' src="<c:out value="${imgUrl}"/>?transactionType=<c:out value="${transactionType}"/>&fromDate=<c:out value="${fromDate}"/>&toDate=<c:out value="${toDate}"/>&bankId=<c:out value="${bankId}"/>&chartType=<c:out value="${chartType}"/>&imgType=imgCount" width="800" height="400"/>
			</div>
			</c:if>  --%>
		</c:if>	
	</div>

</div>
	
</div>	

<script>
	window.onload=function(){
	check();
	check1();
	};
</script>
</body>

</html>
