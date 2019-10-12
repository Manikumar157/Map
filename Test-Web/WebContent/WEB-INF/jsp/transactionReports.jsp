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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>	
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<script type="text/javascript">
	var Alertmsg={
		 "validToDate":'<spring:message code="VALID_TODATE" text="Please enter valid To Date"/>',
		 "validAccHead":'<spring:message code="ALERT_ACCOUNT_HEAD" text="Please select Account Head"/>',
		 "VALID_FROM_DATE":'<spring:message code="VALID_FROM_DATE" text="Please select From Date"/>',
		 "VALID_TO_DATE":'<spring:message code="VALID_TO_DATE" text="Please select To Date"/>',
		 "validClearingHouse":'<spring:message code="NotNull.bankDTO.clearingHouses" text="Please select Clearing House Name"/>',
		 "validBank":'<spring:message code="LABEL.BANK.NOTEMPTY" text="Please select Bank Name"/>',
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
		 
	};
	function check(){	
		Zapatec.Calendar.setup({
	    firstDay          : 1,
	    timeFormat        : "12",
	    electric          : false,
	    inputField        : "fromDate1",
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
	    inputField        : "toDate1",
	    button            : "trigger1",
	    ifFormat          : "%d/%m/%Y",
	    daFormat          : "%Y/%m/%d",
	    timeInterval          : 01
  	});
  }
	function check2(){	
		Zapatec.Calendar.setup({
	    firstDay          : 1,
	    timeFormat        : "12",
	    electric          : false,
	    inputField        : "fromDate2",
	    button            : "trigger2",
	    ifFormat          : "%d/%m/%Y",
	    daFormat          : "%Y/%m/%d",
	    timeInterval          : 01
	  });
 
	}

	function check3(){	
		Zapatec.Calendar.setup({
	    firstDay          : 1,
	    timeFormat        : "12",
	    electric          : false,
	    inputField        : "toDate2",
	    button            : "trigger3",
	    ifFormat          : "%d/%m/%Y",
	    daFormat          : "%Y/%m/%d",
	    timeInterval          : 01
  	});
  }

	function enableCH(form){
		if(form.name=="accountLedger"){
			if(form.reportType[0].checked == true){
				document.getElementById("chPoolLedger").style.display="none";
				$.post("getAccountHeadByBankId.htm", {
					bankId : null
				}, function(data) {
					document.getElementById("accHeadDiv").innerHTML="";
					document.getElementById("accHeadDiv").innerHTML = data;
					
				});
			}else if(form.reportType[1].checked == true){
				document.getElementById("chPoolLedger").style.display="";
				form.accountNumber.options.length=1;
				form.bankId.options.length=1;
				form.clearingPoolId.value="-1";
			}
			document.getElementById("fromDate1").value="";
			document.getElementById("toDate1").value="";

			
		}else if(form.name=="trialBalance"){
			if(form.reportType[0].checked == true){
				document.getElementById("chPoolTrial").style.display="none";
				form.clearingPoolId.value="-1";
				form.bankId.options.length=0;
			}else if(form.reportType[1].checked == true){
				document.getElementById("chPoolTrial").style.display="";
			}
			document.getElementById("fromDate2").value="";
			document.getElementById("toDate2").value="";
			
		}
	}
	$(document).ready(function() {	
		$("#chpoolLedgerDiv").change(function() {
			document.accountLedger.accountNumber.options.length=1;
			$clearingPoolId = document.accountLedger.clearingPoolId.value;
			
			$.post("getBrankByChPoolID.htm", {
				clearingPoolId : $clearingPoolId
			}, function(data) {
				document.getElementById("bankLedgerDiv").innerHTML="";
				document.getElementById("bankLedgerDiv").innerHTML = data;
				
			});
			
		});


		$("#chpoolTrialDiv").change(function() {
			
			$clearingPoolId = document.trialBalance.clearingPoolId.value;
		
			$.post("getBrankByChPoolID.htm", {
				clearingPoolId : $clearingPoolId
			}, function(data) {
				document.getElementById("bankTrialDiv").innerHTML="";
				document.getElementById("bankTrialDiv").innerHTML = data;
				
			});
			
		});

		$("#bankLedgerDiv").change(function() {
			
			$bankId = document.accountLedger.bankId.value;
			if($bankId=='select' || $bankId==""){
				$bankId=-1;
			}
			$.post("getAccountHeadByBankId.htm", {
				bankId : $bankId
			}, function(data) {
				document.getElementById("accHeadDiv").innerHTML="";
				document.getElementById("accHeadDiv").innerHTML = data;
				
			});
			
		});
		
	});
	
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
	
	function submitAccForm(form){
		var bankName="";
		if(form.reportType!=undefined && form.reportType[1].checked == true){
			if(form.clearingPoolId!=undefined && form.clearingPoolId.value=="-1"){
				alert(Alertmsg.validClearingHouse);
				form.clearingPoolId.focus();
				return false;
			}
			if(form.bankId!=undefined && form.bankId.value=="select"){
				alert(Alertmsg.validBank);
				form.bankId.focus();
				return false;
			}else{
				bankName=form.bankId.options[form.bankId.selectedIndex].firstChild.nodeValue;
			}
		}else if(form.reportType!=undefined && form.reportType[0].checked == true){
			bankName="GIM";
		}
		if(form.reportType==undefined){
			if(form.bankId!=undefined && form.bankId.value==""){
				alert(Alertmsg.validBank);
				form.bankId.focus();
				return false;
			}else if(form.bankId!=undefined && form.bankId.value!=""){
				bankName=form.bankId.options[form.bankId.selectedIndex].firstChild.nodeValue;
			}
		}
		if(form.accountNumber!=undefined && (form.accountNumber.value=="" || form.accountNumber.value=="select")){
			alert(Alertmsg.validAccHead);
			form.accountNumber.focus();
			return false;
		}
		
	    if(form.fromDate.value==""){
			   alert("<spring:message code='LABEL.FROMDATE' text='Please enter From Date'/>");
			   return false;
		}else if (form.toDate.value==""){
			   alert("<spring:message code='LABEL.TODATE' text='Please enter To Date'/>");
			   return false;
		}else if(!validdate(form.fromDate.value,form.toDate.value)){
        	 	return false;  
        	 	
        }else if (!compareDate(form.fromDate.value)){
			   alert("<spring:message code='LABEL.FROMDATE.TODAY' text='Please enter From Date Less than Todays date'/>");
			   return false;
		}else if (!compareDate(form.toDate.value)){
			   alert("<spring:message code='LABEL.TODATE.TODAY' text='Please enter To Date Less than Todays date'/>");
			   return false;
		}else if(!compareFromDate(form.fromDate.value,form.toDate.value)){
			
			   alert("<spring:message code='COMPARE_DATES' text='From Date should not be greater than To Date'/>");
			   return false;
		}		
		document.getElementById('msg').innerHTML="";
		
		if(form.name=='accountLedger'){
			form.action="accountLedgerReport.htm";
			form.submit();
		}else if(form.name=='trialBalance'){
			
			form.action="trialBalanceReport.htm?bankName="+bankName;
			form.submit();
		}
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
</script>
</head>
<body onload=check2();>

<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_ACC_REPORTS" text="Account Reports" /></span>
		</h3>
	</div><br />
	<div class="col-sm-6 col-sm-offset-5">
		<div id="msg" style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<spring:message code="${message}" text="" />
		</div>
	</div>
	<form:form class="form-inline" id="accountLedger" name="accountLedger" commandName="txnSummaryDTO" autocomplete="off">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box-body table_border" style="/* border:1px solid; */ /* border-radius:15px; */ width:80%;margin-left:100px;">
		<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_ACCOUNT_LEDGER" text="Account Ledger" /></span>
		</h3>
		</div><br />
		<authz:authorize ifAllGranted="ROLE_admin,ROLE_gimbackofficelead,ROLE_gimbackofficeexec">
		<div class="row">
			<div class="col-sm-6">
			<label><form:radiobutton value="0" id="reportType" path="reportType" onclick="enableCH(this.form);"/>&nbsp;<spring:message code="LABEL_GIM" text="GIM" /></label>
			<label><form:radiobutton value="1" id="reportType" path="reportType" onclick="enableCH(this.form);"/>&nbsp;<spring:message code="LABEL_BANK" text="Bank" /></label>
			</div>
		</div>
		<div class="row" id="chPoolLedger" style="display:none">
			<div class="col-sm-6">
			<spring:message code="LABEL_CHOUSE" text="Clearing House" /><font color="red">*</font>
			<div id="chpoolLedgerDiv">
				<form:select path="clearingPoolId" id="clearingPoolId" cssClass="dropdown">
					<form:option value="-1">
						<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
					</form:option>
					<c:forEach items="${masterData.chPoolList}" var="chPoolData">
						<form:option value="${chPoolData.clearingPoolId}">${chPoolData.clearingPoolName}</form:option>
					</c:forEach>
				</form:select>
			</div>
			</div>
			<div class="col-sm-6">
			<spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font>
			<div id="bankLedgerDiv">
				<form:select path="bankId" id="bankId" cssClass="dropdown">
					<form:option value="">
						<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
					</form:option>
				</form:select></div>
			</div>
		</div>
		</authz:authorize>
		
		<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankadmin,ROLE_superadmin,ROLE_audit,ROLE_accounting,ROLE_gimbackofficelead,ROLE_gimbackofficeexec">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ACCOUNT_HEAD" text="Account Head" /><font color="red">*</font></label> 
					<div id="accHeadDiv">
					<form:select path="accountNumber" id="accountNumber" cssClass="dropdown_big chosen-select">
						<form:option value="">
							<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
						</form:option>
						<c:forEach items="${masterData.accountHeadList}" var="accHead">
							<form:option value="${accHead.accountNumber}">${accHead.accountHead}</form:option>
						</c:forEach>
					</form:select></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROM_DATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate"  id="fromDate1" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker" />
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TO_DATE" text="To" /><font color="red">*</font></label>
					<form:input path="toDate"  id="toDate1" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker" /> 
				</div>
			</div>
		</authz:authorize>
		<authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font></label> 
					<div id="bankLedgerDiv">
						<form:select path="bankId" id="bankId" cssClass="dropdown">
							<form:option value="">
								<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
							</form:option>
							<form:options items="${masterData.bankList}" itemValue="bankId" itemLabel="bankName"/>
						</form:select>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ACCOUNT_HEAD" text="Account Head" /><font color="red">*</font></label> 
					<div id="accHeadDiv">
						<form:select path="accountNumber" id="accountNumber" cssClass="dropdown">
							<form:option value="">
								<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
							</form:option>
							<c:forEach items="${masterData.accountHeadList}" var="accHead">
								<form:option value="${accHead.accountNumber}">${accHead.accountHead}</form:option>
							</c:forEach>
						</form:select></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROM_DATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate"  id="fromDate1" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker" style="width:180px;"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TO_DATE" text="To" /><font color="red">*</font></label>
					<form:input path="toDate"  id="toDate1" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker" style="width:180px;"/> 
				</div>
			</div>
		</authz:authorize>
			<div class="box-footer">
			<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="submitAccForm(this.form);"></input>
			</div><br/>
</div><br/>
</form:form>
</div>
<form:form name="trialBalance" id="trialBalance" class="form-inline" commandName="txnSummaryDTO" autocomplete="off">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box">
	<br />
	<div class="box-body table_border" style="/* border:1px solid; border-radius:15px; */ width:80%;margin-left:100px;">
		<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_TRIAL_BALANCE" text="Trial Balance" /></span>
		</h3>
		</div><br />
		<authz:authorize ifAllGranted="ROLE_admin,ROLE_gimbackofficelead,ROLE_gimbackofficeexec">
		<div class="row">
			<div class="col-sm-6">
			<label><form:radiobutton value="0" id="reportType" path="reportType" onclick="enableCH(this.form);"/>&nbsp;<spring:message code="LABEL_GIM" text="GIM" /></label>
			<label><form:radiobutton value="1" id="reportType" path="reportType" onclick="enableCH(this.form);"/>&nbsp;<spring:message code="LABEL_BANK" text="Bank" /></label>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_CHOUSE" text="Clearing House" /><font color="red">*</font></label>
			<div id="chpoolTrialDiv">
				<form:select path="clearingPoolId" id="clearingPoolId" cssClass="dropdown">
					<form:option value="-1">
						<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
					</form:option>
					<c:forEach items="${masterData.chPoolList}" var="chPoolData">
						<form:option value="${chPoolData.clearingPoolId}">${chPoolData.clearingPoolName}</form:option>
					</c:forEach>
				</form:select></div>
			</div>
			<div class="col-sm-6">
			<label class="col-sm-5"><spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font></label>
			<div id="bankTrialDiv">
				<form:select path="bankId" id="bankId" cssClass="dropdown">
					<form:option value="">
						<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
					</form:option>
				</form:select></div>
			</div>
		</div>
		</authz:authorize>
		<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankadmin,ROLE_superadmin,ROLE_audit,ROLE_accounting,ROLE_gimbackofficelead,ROLE_gimbackofficeexec">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROM_DATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate" id="fromDate2" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TO_DATE" text="To" /><font color="red">*</font></label> 
					<form:input path="toDate"  id="toDate2" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"/> 
				</div>
			</div>
		</authz:authorize>
		
		<authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font></label> 
					<div id="bankLedgerDiv">
						<form:select path="bankId" id="bankId" cssClass="dropdown">
							<form:option value="">
								<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
							</form:option>
							<form:options items="${masterData.bankList}" itemValue="bankId" itemLabel="bankName"/>
						</form:select>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROM_DATE" text="From" /><font color="red">*</font></label> 
					<form:input path="fromDate"  id="fromDate2" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_TO_DATE" text="To" /><font color="red">*</font></label>
					<form:input path="toDate"  id="toDate2" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"/> 
				</div>
			</div>
		</authz:authorize>
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="submitAccForm(this.form);"></input>
			</div><br/>
			</div><br/>
		</div>
		</form:form>
</div>		
</body>
<script type="text/javascript">
window.onload=function(){
	check();
	check1();
	check2();
	check3();
};
</script>
</html>
