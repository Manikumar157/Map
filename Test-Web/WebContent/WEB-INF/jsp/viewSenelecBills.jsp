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
		<title><spring:message code="LABEL_TITLE" text="EOT Mobile"></spring:message></title>
		
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/calendar-system.css" />
		
		<!-- Loading Calendar JavaScript files -->
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/zapatec.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
	    <script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
		
	<!--  <link href="../../css/style.css" rel="stylesheet" type="text/css" /> -->

	<script type="text/javascript">
	function check(){
		Zapatec.Calendar.setup({
        firstDay          : 1,
        timeFormat        : "12",
        electric          : false,
        inputField        : "fromDate",
        button            : "trigger",
        ifFormat          : "%d-%m-%Y",
        daFormat          : "%Y/%m/%d",
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
        ifFormat          : "%d-%m-%Y",
        daFormat          : "%Y/%m/%d",
        timeInterval      : 01
      });
	 
  }
	
	function searchSubmit(){
		var recordNo=document.getElementById('recordNo').value;
		var from=document.getElementById('fromDate').value;
		var to=document.getElementById('toDate').value;
		var fromAmount=document.getElementById('fromAmount').value;
		var toAmount=document.getElementById('toAmount').value;
		if(recordNo!=""){
			if(isNaN(recordNo) || recordNo.charAt(0) == " " || recordNo.charAt(recordNo.length-1) == " "){
			alert("<spring:message code='VALID_RECORD_NO' text='Please give valid record no.'/>");
			return false;
			}
		}
		if(from!=""){
			if(to==""){
		        alert("<spring:message code='VALID_TO_DATE' text='Please enter a valid to date'/>");
			    return false;
			}
			if(!compareDate(from,to)){
				alert("<spring:message code='VALID_FROM_TO_DATE' text='To date should not be less than  or equal to from date'/>");
				return false;
			}
			if(compareDate(getCurrentDate(),to)){
				alert("<spring:message code='VALID_TO_CURRENT_DATE' text='To date should not be greater than current date'/>");
				return false;
			}

		}
		if(to!=""){
			if(from==""){
				alert("<spring:message code='VALID_FROM_DATE' text='Please enter a from valid date'/>");
			return false;
			}
		}
		if(fromAmount!=""){
			if(toAmount==""){
				alert("<spring:message code='VALID_TO_AMOUNT' text='Please enter a valid to amount'/>");
				return false;
			}
			if(isNaN(fromAmount)){
				alert("<spring:message code='VALID_FORM_AMOUNT' text='Please enter a valid from amount'/>");
				return false;
			}
			if(isNaN(toAmount)){
				alert("<spring:message code='VALID_TO_AMOUNT' text='Please enter a valid to amount'/>");
				return false;
			}
			if(parseInt(toAmount)<=parseInt(fromAmount)){
				alert("<spring:message code='VALID_TO_FORM_AMOUNT' text='To amount should be greater than from amount'/>");
				return false;
			}
		}
		if(toAmount!=""){
			if(fromAmount==""){
				alert("<spring:message code='VALID_FORM_AMOUNT' text='Please enter a valid form amount'/>");
			    return false;
			}
		}
		document.senelecBillViewForm.method = "post";
		document.senelecBillViewForm.action = "searchSenelecBill.htm";
		document.senelecBillViewForm.submit();
	}
	
	function getCurrentDate(){
		var date = new Date();
		var d  = date.getDate();
		var day = (d < 10) ? '0' + d : d;
		var m = date.getMonth() + 1;
		var month = (m < 10) ? '0' + m : m;
		var yy = date.getYear();
		var year = (yy < 1000) ? yy + 1900 : yy;
		var cdate=day + "-" + month + "-" + year;
     return cdate;
    }
	
	function compareDate(fromdt,todt)
	{
	    var dt1  = parseInt(fromdt.substring(0,2),10);
	    var mon1 = parseInt(fromdt.substring(3,5),10)-1;
	    var yr1  = parseInt(fromdt.substring(6,10),10);
	    var dt2  = parseInt(todt.substring(0,2),10);
	    var mon2 = parseInt(todt.substring(3,5),10)-1;
	    var yr2  = parseInt(todt.substring(6,10),10);
	    var fromDate = new Date(yr1, mon1, dt1);
	    var toDate = new Date(yr2, mon2, dt2); 
		if( fromDate < toDate )return true;
		else return false;
		
	}
	
	function removeMsg(){
		document.getElementById('msg').innerHTML="";
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	function seneBillDetail(url,policyNo,recordNo){
		document.getElementById('policyNo').value=policyNo;
		document.getElementById('recordNo').value=recordNo;
		submitlink(url,'senelecBillViewForm');
	}
	//@end
	</script>
	
	</head>
	<body>
	<div class="col-lg-12">
	<form:form class="form-inline" name="senelecBillViewForm" id="senelecBillViewForm" method="post"  action="searchSenelecBill.htm" commandName="senelecBillSearchDTO">
	<jsp:include page="csrf_token.jsp"/>
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_VIEW_SENELEC_BILLS" text="View Senelec Bills"></spring:message></span>
		</h3>
	</div><br/>
	<div id="msg" style="color: #ba0101;font-weight: bold;font-size: 12px; text-align: center"><spring:message code="${message}" text=""/></div>
	<div class="col-md-3 col-md-offset-10">
		
		 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_REPORT" text="Report"/>"
							onclick="javascript:submitForm('senelecBillViewForm','senelecPdfReport.htm')" /> 
		<%-- <a href="javascript:submitForm('senelecBillViewForm','senelecPdfReport.htm')" ><strong><spring:message code="LABEL_REPORT" text="Report"></spring:message></strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; --%>
	</div><br/><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_POLICY_NO" text="Policy No"></spring:message></label> 
					<form:input path="policyNo" maxlength="12"  cssClass="form-control"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_RECORD_NO" text="Record No"></spring:message></label> 
					<form:input path="recordNo"  cssClass="form-control" />
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_FROM_DATE" text="From Date"></spring:message></label> 
					<form:input path="fromDate" onfocus="this.blur()" readonly="readonly" cssClass="form-control datepicker"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_TO_DATE" text="Todate"/></label> 
					<form:input path="toDate" onfocus="this.blur()" readonly="readonly"  cssClass="form-control datepicker"/>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_FROM_AMOUNT" text="Amount From"></spring:message></label> 
					<form:input path="fromAmount"  cssClass="form-control"/>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_TO_AMOUNT" text="Amount To"/></label> 
					<form:input path="toAmount"  cssClass="form-control" />
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_STATUS" text="Status"/></label> 
					<form:select path="status" cssClass="dropdown chosen-select">
	                <form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
	                <form:option value="1"><spring:message code="LABEL_PAID" text="Paid"/></form:option>
	                <form:option value="0"><spring:message code="LABEL_NOT_PAID" text="Not Paid"/></form:option>		
	                <form:option value="2"><spring:message code="LABEL_FILE_GENERATED" text="File Generated"/></form:option>
	                <form:option value="3"><spring:message code="LABEL_OLD_BILLS" text="Old Bills"/></form:option>										                
	                </form:select>
				</div>
			</div>
			<br/>
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right"  value="<spring:message code="LABEL_SEARCH"/>" onclick="searchSubmit();" style="margin-right: 60px;"/>
				<br/><br/>
			</div>
</div>
</div>
</form:form>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_POLICY_NO" text="Policy Number"/></th>
								<th><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></th>
								<th><spring:message code="LABEL_RECORD_NO" text="Record No."/></th>
								<th><spring:message code="LABEL_RECORD_AMOUNT" text="Amt To be Paid"/></th>
								<th><spring:message code="LABEL_TXNTYPE" text="Txn Type"/></th>
								<th><spring:message code="LABEL_PAY_DATE" text="Payment Date"/></th>
								<th><spring:message code="LABEL_TXN_DATE" text="Txn Date"/></th>
								<th><spring:message code="LABEL_AMOUNT_PAID" text="Amount Paid"/></th>
								<th><spring:message code="LABEL_STATUS" text="Status"/></th>
								<th><spring:message code="LABEL_DETAILS" text="Details"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
				            <c:forEach items="${page.results}" var="bill">
				            <c:set var="j" value="${ j+1 }"></c:set>
				            <tr>
							    <td><c:out value="${bill.senelecCustomers.policyNumber}" /></td>
							    <td><c:out value="${bill.senelecCustomers.customerName}" /></td>
							    <td><c:out value="${bill.recordNumber}" /></td>		
								<td><c:out value="${bill.recordAmount}" /></td>		
								<td><c:out value="${bill.txnType}" /></td>	
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${bill.paymentDate}" /></td>		
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${bill.transaction.transactionDate}" /></td>		
								<td><c:out value="${bill.amountPaid}" /></td>	
								<td><c:choose><c:when test="${bill.status==0}"><font color=red><spring:message code="LABEL_NOT_PAID" text="Not Paid"/></font></c:when><c:when test="${bill.status==1}"><spring:message code="LABEL_PAID" text="Paid"/></c:when><c:when test="${bill.status==2}"><spring:message code="LABEL_FILE" text="File"/></c:when><c:otherwise><spring:message code="LABEL_OLD_BILL" text="Old Bill"/></c:otherwise> </c:choose></td>
							    <td><%-- <a href="javascript:submitForm('senelecBillViewForm','viewDetailsByPolicyNo.htm?pno=${bill.senelecCustomers.policyNumber}&recordNo=${bill.recordNumber}')"><spring:message code="LABEL_VIEW" text="View"/></a> --%>
							    <a href="javascript:seneBillDetail('viewDetailsByPolicyNo.htm','${bill.senelecCustomers.policyNumber}','${bill.recordNumber}')"><spring:message code="LABEL_VIEW" text="View"/></a></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
	</div>
	
</div>
	
	<script type="text/javascript">
    window.onload=function(){
	check();
	check1();
    };
</script>
</body>
</html>
