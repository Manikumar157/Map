<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>

<head>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bankManagement.js"></script>
<script type="text/javascript"> 

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

function searchExternalTxns(){
	
	   var mobileNumber = document.getElementById("mobileNumber").value;
	   var beneficiaryMobileNumber = document.getElementById("beneficiaryMobileNumber").value;
	   var fromDate=document.getElementById("fromDate").value;
       var toDate=document.getElementById("toDate").value;
       var pattern= '^\[a-zA-ZÀ-ÿ0-9-\ ]*$';
	   
       
      if(document.getElementById("transactionId").value.search(pattern)==-1)
     {
    	alert("please enter valid Transaction ID");
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
	document.externalTransactionsForm.action="searchExternalTxns.htm";
	document.externalTransactionsForm.submit();
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

</script>

<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>

</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_EXTERNAL_TXNS" text="External Transactions" /></span>
		</h3>
	</div>
	<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message code="${message}" text="" /></div>
	<div class="box-body">
		<form:form class="form-inline" name="externalTransactionsForm" id="externalTransactionsForm" method="post" commandName="externalTransactionDTO">
			<jsp:include page="csrf_token.jsp"/>
			<c:if test="${externalTransactionDTO.fromDate ne null}">
			<c:if test="${page.results.size() gt 0}">
			<div class="col-sm-6 col-sm-offset-6">
				
				<span><a href="javascript:submitForm('externalTransactionsForm','exportToXlsExternalTransactionDetails.htm')"><b><spring:message code="LABEL_EXTERNAL_TXN_DETAILS" text="External Transaction Details"/></b></a></span> 
					<span>|</span> 
					<span><a href="javascript:submitForm('externalTransactionsForm','exportToXlsExternalTransactionSummaryDetails.htm')"><b><spring:message code="LABEL_EXTERNAL_TXN_SUMMARY_DETAILS" text="External Summary Details"/></b></a></span>
				
				</div>
				</c:if>
			</c:if>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_OPERATOR" text="Operator" /></label> 
					<form:select path="externalEntityId" class="dropdown chosen-select" id="externalEntityId">
						<form:option value="" selected="selected">
							<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
						</form:option>
						<form:options items="${operatorList}" itemValue="operatorId" itemLabel="operatorName"></form:options>
					</form:select> 
					<font color="RED"><form:errors path="externalEntityId" cssClass="" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TRANSACTION_ID" text="Transaction ID"/></label> 
					<form:input path="transactionId" id="transactionId" cssClass="form-control" maxlength="20"/>
					<font color="red"><form:errors path="transactionId" /></font>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CUSTOMER_MOBILE_NUMBER" text="Customer Mobile Number" /></label> 
					<form:input path="mobileNumber" id="mobileNumber" cssClass="form-control" maxlength="20"/><FONT color="red"><form:errors path="mobileNumber" /></FONT>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BENIFICIARY_MOBILE_NUMBER" text="Benificiary Mobile Number:" /></label> 
					<form:input path="beneficiaryMobileNumber" id="beneficiaryMobileNumber" cssClass="form-control" maxlength="20"/><FONT
										color="red"><form:errors path="beneficiaryMobileNumber" /></FONT>
				</div>
			</div>
			<div class="row">
			<!--bugId:113 by:rajlaxmi for:showing dates as mandatory field  -->
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FROMDATE" text="From" /><font color="red">*</font></label></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly" id="fromDate" cssClass="form-control datepicker"></form:input>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TODATE" text="To" /><font color="red">*</font></label></label>
					<form:input path="toDate" onfocus="this.blur()" readonly="readonly" id="toDate" cssClass="form-control datepicker"></form:input> 
				
				</div>
			</div><br/>
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right" id="searchButton" value="<spring:message code="LABEL_SEARCH" text="Search"/>" onclick="searchExternalTxns();"/>
			</div><br/><br/>
		</form:form>
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_OPERATOR" text="Operator" /></th>
								<th><spring:message code="LABEL_CUSTOMER_MOBILE_NUMBER" text="Mobile Number" /></th>
								<th><spring:message code="LABEL_BENIFICIARY_MOBILE_NUMBER" text="Benificiary Mobile Number" /></th>	
								<th><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></th>
								<th><spring:message code="LABEL_AMOUNT" text="Amount" /></th>
								<th><spring:message code="LABEL_SERV_CHARGE" text="Serv. Charge" /></th>											
								<th><spring:message code="LABEL_TRANSACTION_ID" text="Transaction ID" /></th>	
								<th><spring:message code="LABEL_TXN_DATE" text="Transaction Date" /></th>
								<th><spring:message code="LABEL_STATUS" text="Status" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="externalTransactions">
							<tr>
								<td><c:out value="${externalTransactions.EntityName}" /></td>
								<td><c:out	value="${externalTransactions.MobileNumber}" /></td>
								<td><c:out value="${externalTransactions.BeneficiaryMobileNumber}" /></td>	
								<%-- <c:if test="${externalTransactions.transactionType==84}">
									<c:set var="txnType" ><spring:message code="LABEL_SMS_CASH_OTHERS" text="SMS Cash Others" /></c:set>
								</c:if>
								<c:if test="${externalTransactions.transactionType==55}">
									<c:set var="txnType"><spring:message code="LABEL_TRANSFER" text="Transfer" /></c:set>
								</c:if>
								<td align="left"><c:out value="${txnType}" /></td> --%>
								<td> <spring:message code="LABEL_SMS_CASH_OTHERS" text="SMS Cash Others" /></td>
								<td><c:out value="${externalTransactions.amount}" /></td>
								<td><c:out value="${externalTransactions.ServiceChargeAmount}" /></td>	
								<td><c:out value="${externalTransactions.TransactionID}" /></td>	
								<!-- by:rajlaxmi for:removing time from datatable -->
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${externalTransactions.TransactionDate}" /></td>
								<c:if test="${externalTransactions.status==2000}">
									<c:set var="status" ><spring:message code="LABEL_SUCCES" text="Success" /></c:set>
								</c:if>
								<c:if test="${externalTransactions.status!=2000}">
									<c:set var="status"><spring:message code="LABEL_FAILUIRE" text="Failure" /></c:set>
								</c:if>
								<td><c:out value="${status}" /></td>
								<%-- <td align="center"><c:out value="${externalTransactions.status}" /></td> --%>
								
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
	</div>
	
</div>
</body>
</html>
