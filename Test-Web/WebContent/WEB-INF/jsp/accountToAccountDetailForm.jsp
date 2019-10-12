<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>

<script type="text/javascript">
	var businessPartnerCode = document.getElementById("businessPartnerCode");
var Alertmsg={
	
		 "fromDateFormat":"<spring:message code="VALID_VALID_ACCOUNT_REPORT_FROM_DATE_FORMAT" text="The aggrement from date format must be : dd/mm/yyyy"/>",
		 "toDateFromat":"<spring:message code="VALID_ACCOUNT_REPORT_TO_DATE_FORMAT" text="The aggrement to date format must be : dd/mm/yyyy"/>",
		 "validFromDay":"<spring:message code="VALID_DAY" text="Please enter valid from date"/>",
		 "validToDay":"<spring:message code="VALID_TO_DAY" text="Please  enter  a valid to date"/>",
		 "validFromMonth":"<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		 "validToMonth":"<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		 "validfDay":"<spring:message code="VALID_FROM_DAY" text="Please  enter  a valid  days in from date"/>",
		 "validTodays":"<spring:message code="VALID_TO_DAYS" text="Please  enter  a valid  days in to date"/>",
		 "validFMonth":"<spring:message code="VALID_FROM_MONTH" text="Please  enter  a valid  month in from date"/>",
		 "validToMonth":"<spring:message code="VALID_TO_MONTH" text="Please  enter  a valid  month in to date"/>",
		 
		 };    

		function cancelForm(){
			if(document.getElementById("transactionType").value == "137"  || document.getElementById("transactionType").value == "138"
					||( document.processTransactionForm.businessPartnerCode.value != null && document.processTransactionForm.businessPartnerCode.value != "" && document.processTransactionForm.businessPartnerCode.value != undefined)){
				 document.processTransactionForm.action="businessPartnerTransaction.htm"; 
			}else{
				 document.processTransactionForm.action="selectTransactionForm.htm";
			}
		//    document.processTransactionForm.action="selectTransactionForm.htm";
		    document.processTransactionForm.submit();
		}
		function onSubmit(){
			
			if( document.getElementById("transactionType").value == "95"  || document.getElementById("transactionType").value == "99"  || document.getElementById("transactionType").value == "143"
					|| document.getElementById("transactionType").value == "137"  || document.getElementById("transactionType").value == "138" ){
				 var txnAmount = document.getElementById("amount").value;
				 var amountPattren = '^\[0-9]*$';
				if(txnAmount.search(amountPattren) == -1){
					alert("<spring:message code="VALID_TXN_AMT"/>");
					return false;
				}else if(txnAmount == 0){
					alert("<spring:message code="VALID_TXN_AMT"/>");
					return false;
				}
			}
			if ( document.getElementById("transactionType").value == 98 ) {
				
				//alert("-"+document.getElementById("fromDate").value+"-");
				
				var fromdt = document.getElementById("fromDate").value ;
				var todt = document.getElementById("toDate").value;
				
				if(fromdt==""){
					alert("<spring:message code="VALID_FROM_DATE"/>");
					return false;
				}
				if(todt==""){
					alert("<spring:message code="VALID_TO_DATE"/>");
					return false;
				}
				if(!validdate(fromdt,todt)){
					
				 	return false;  
				 	
					 }
				var dt1  = parseInt(fromdt.substring(0,2),10);
			    var mon1 = parseInt(fromdt.substring(3,5),10)-1;
			    var yr1  = parseInt(fromdt.substring(6,10),10);
			    var dt2  = parseInt(todt.substring(0,2),10);
			    var mon2 = parseInt(todt.substring(3,5),10)-1;
			    var yr2  = parseInt(todt.substring(6,10),10);
			    var fromDate = new Date(yr1, mon1, dt1);
			    var toDate = new Date(yr2, mon2, dt2); 
			    
			    if( fromDate > toDate ){
					alert("<spring:message code="VALID_DATE_RANGE"/>");
					return false;
				}
			    if( fromDate > new Date() ){
			    	alert("<spring:message code="ALERT_INVALID_FROM_DATE"/>");
					return false;
			    }
			    if( toDate > new Date() ){
			    	alert("<spring:message code="ALERT_INVALID_TO_DATE"/>");
					return false;
			    }
				
			} 
			
			document.processTransactionForm.button.disabled="true";
			document.processTransactionForm.method = "post";
			document.processTransactionForm.action="processTransaction.htm";
			document.processTransactionForm.submit();
			
		}
		
		function fromDateCheck(){
			Zapatec.Calendar.setup({
	        firstDay          : 1,
	        timeFormat        : "12",
	        electric          : false,
	        inputField        : "fromDate",
	        button            : "trigger",
	        ifFormat          : "%d/%m/%Y",
	        daFormat          : "%Y/%m/%d",
	        timeInterval      : 01
	      });
		 
	   }
		function toDateCheck(){
			Zapatec.Calendar.setup({
	        firstDay          : 1,
	        timeFormat        : "12",
	        electric          : false,
	        inputField        : "toDate",
	        button            : "trigger1",
	        ifFormat          : "%d/%m/%Y",
	        daFormat          : "%Y/%m/%d",
	        timeInterval      : 01
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
		function openNewWindow( url )
		{
				window.open (url,"mywindow","menubar=0,toolbar=0,resizable=1,width=450,height=450");
		}
	</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_WELCOME_TRANSACTION_SERVICE" text="Transaction Service" /> - 
				<c:if test="${accountDetailsDTO.transactionType == 30}">
					<spring:message code="LABEL_TITLE_BALANCE" text="Balance Enquiry" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType ==35}">
					<spring:message code="LABEL_STATEMENT" text="Ministatement" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType ==98}">
					<spring:message code="LABEL_TXN_STMT" text="Txn Statement" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType == 95}">
				   <spring:message code="LABEL_DEPOSIT" text="Deposit" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType ==99}">
				   <spring:message code="LABEL_WITHDRAWAL" text="Withdrawal" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType == 137}">
				   <spring:message code="LABEL_CASH_IN" text="Cash In" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType ==138}">
				   <spring:message code="LABEL_CASH_OUT" text="Cash Out" /></c:if>
				<c:if test="${accountDetailsDTO.transactionType ==143}">
				   <spring:message code="LABEL_TRANSFER_EMONEY" text="Transfer eMoney" /></c:if>
				   	<c:if test="${accountDetailsDTO.transactionType ==144}">
				   <spring:message code="LABEL_TRANSFER_ACCOUNT_TO_ACCOUNT" text="Account to Account Transfer" /></c:if>
			</span>
		</h3>
	</div><br/>
	
	<%-- <div class="col-md-3 col-md-offset-4 ">
		<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
			<spring:message code="${message}" text="" />
		</div>
	</div> --%>
	
	<!--@  Author name <vinod joshi>, Date<7/11/2018>, purpose of change < Not Displaying proper message on JSP page> ,
		@  Start--> 
	<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<spring:message code="${message}" text="" />
		</div>
	</div>
	<!--@ End -->
	
	<div class="box-body">
	<form:form method="post" class="form-inline" action="processTransaction.htm" name="processTransactionForm" commandName="accountDetailsDTO">
			<jsp:include page="csrf_token.jsp"/>
				<input type="hidden" name="mobileNumber" value="${ accountDetailsDTO.mobileNumber }"></input>
					<input type="hidden" name="customerId" value="${ accountDetailsDTO.customerId }"></input>
					<input type="hidden" name="customerName" value="${ accountDetailsDTO.customerName }"></input>
					<input type="hidden" id="transactionType" name="transactionType" value="${ accountDetailsDTO.transactionType }"></input>
					<input type="hidden" name="businessPartnerCode" value="${ accountDetailsDTO.businessPartnerCode }"></input>
					<input type="hidden" name="fromAccountNumber" value="${ accountDetailsDTO.fromAccountNumber }"></input>
					<input type="hidden" name="toAccountNo" value="${ accountDetailsDTO.toAccountNumber }"></input>
					
			<div class="row">
				<%-- <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="FROM_ACCOUNT_ALIAS" text="From AccountAlias" /></label> 
				</div> --%>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="FROM_ACCOUNT_ALIAS" text="From Account" /></label> 
					<div class="col-sm-5">
					<c:out value="${accountDetailsDTO.fromAccountAlias}" />
					</div>
				</div>
			</div>
			<div class="row">
				<%-- <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="TO_ACCOUNT_ALIAS" text="To AccountAlias" /></label> 
				</div> --%>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="TO_ACCOUNT_ALIAS" text="To Account" /></label> 
					<div class="col-sm-5">
					<c:out value="${accountDetailsDTO.toAccountAlias}" />
					</div>
				</div>
			</div>
			
			<c:if test="${accountDetailsDTO.customerId ne null}">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ID_NUMBER" text="IDNumber" /></label> 
					<div class="col-sm-5">
					<c:if test="${fn:length(accountDetailsDTO.idNumber)>0}">
					<c:out value="${accountDetailsDTO.idNumber}" />
					</c:if>
					<c:if test="${fn:length(accountDetailsDTO.idNumber) eq 0}">
					<spring:message code="LABEL_NOT_AVAILABLE" text="Not Available"/>
					</c:if>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ID_PROOF" text="ID Proof" /></label> 
					<div class="col-sm-5">
					<a href="#" onclick="return openNewWindow('<%=request.getContextPath()%>/getPhoto.htm?type=idproof&customerId=<c:out value="${accountDetailsDTO.customerId}" />')">
					<img src="<%=request.getContextPath()%>/getPhoto.htm?type=idproof&customerId=<c:out value="${accountDetailsDTO.customerId}" />" onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';" alt="<spring:message code="LABEL_ID_PROOF_NOT_FOUND" text="ID Proof not found"/>" width="50" height="50" /></a>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_SIGNATURE" text="Signature" /></label> 
					<div class="col-sm-5">
					<a href="#" onclick="return openNewWindow('<%=request.getContextPath()%>/getPhoto.htm?type=signature&customerId=<c:out value="${accountDetailsDTO.customerId}" />')">
					<img src="<%=request.getContextPath()%>/getPhoto.htm?type=signature&customerId=<c:out value="${accountDetailsDTO.customerId}" />" onerror="this.src='<%=request.getContextPath()%>/images/default_photo.png';" alt="<spring:message code="LABEL_SIGNATURE_NOT_FOUND" text="Signature not found" />" width="50" height="50" /></a>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_IDTYPE" text="IDType" /></label> 
					<div class="col-sm-5">
					<c:if test="${fn:length(accountDetailsDTO.idType) ne 0}">
						<c:out value="${accountDetailsDTO.idType}" />
					</c:if>
					<c:if test="${fn:length(accountDetailsDTO.idType) eq 0}">
						<spring:message code="LABEL_NOT_AVAILABLE" text="Not Available"/>
					</c:if>
					</div>
				</div>
			</div>
			</c:if>
			<c:if test="${accountDetailsDTO.transactionType == 144  }">
			<div class="row">
			<div class="col-sm-6">
				<label class="col-sm-5" style="margin-top:4px;"><spring:message
					code="LABEL_TXN_AMOUNT" text="Transaction Amount" />:</label>
				<div class="col-sm-5"><input type="text" name="amount" id="amount" /></div>
				</div>
			</div>
			</c:if>
			<c:if test="${ accountDetailsDTO.transactionType == 98 }">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From Date:" /><span style="color: #FF0000;">*&nbsp;</span></label> 
					<input type="text" name="fromDate" id="fromDate" class="form-control datepicker"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To Date:" /><span style="color: #FF0000;">*&nbsp;</span></label> 
					<input type="text" name="toDate" id="toDate" class="form-control datepicker"/>
				</div>
			</div>
			</c:if>
			<div class="col-sm-6 col-sm-offset-10">
				<div class="btn-toolbar">
				<input type="button" id="button" class="btn-primary btn" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="onSubmit();" />
				<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
				</div>
			</div><br/><br/>
		</form:form>	
			<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
		<form class="form-inline" role="form">
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-7" style="margin-top:4px;">Mobile Number:</label> 
					
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-7" style="margin-top:4px;">Customer Name:</label> 
				
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-7" style="margin-top:4px;">Account:</label> 
					
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-7" style="margin-top:4px;">TXN Date:</label> 
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-7" style="margin-top:4px;">Description:</label> 
				</div>
			</div>
			<div class="col-sm-6 col-sm-offset-8">
				<div class="btn-toolbar">
					<input type="button" class="btn-primary btn" value="Click To Download"></input>
				</div>
			</div> <br /><br />
</div>
</div>
	
      
    </div>
  </div>
					
</div>
</div>
	
</div>
	<c:if test="${ accountDetailsDTO.transactionType == 98 }">
		<script type="text/javascript">
			window.onload=function(){
				fromDateCheck();
				toDateCheck();
			};
		</script>
	</c:if>
</body>
</html>
