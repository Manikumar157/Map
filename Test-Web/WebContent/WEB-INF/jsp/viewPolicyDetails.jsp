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
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
	    <script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
		
	<!-- <link href="../../css/style.css" rel="stylesheet" type="text/css" /> -->
	<script>
	<!--  //@start Vinod Joshi Date:17/12/2018 purpose:cross site Scripting --->
	function submitBillPayment(url,pageNumber){
		document.getElementById('pageNumber').value=pageNumber;
		submitlink(url,'viewBillForm');
	}	
	</script>
	<style type="text/css">
	td{
		text-align:center;
	}
	</style>
		
	</head>
	<body>
	
	<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_VIEW_POLICY_DETAILS" text="View Policy Details"></spring:message></span>
		</h3>
	</div><br/>
	<div class="pull-right">
	<input type="hidden" name="pageNumber" id="pageNumber" value=""/>	
		<%-- <a href="javascript:submitForm('viewBillForm','getSenelecBills.htm?pageNumber=1')"><strong><spring:message code="LABEL_SENELEC_BILLS" text="Senelec Bills" /></strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; --%>
		<a href="javascript:submitBillPayment('getSenelecBills.htm','1')"><strong><spring:message code="LABEL_SENELEC_BILLS" text="Senelec Bills" /></strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	</div><br/><br/>
	<div style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div>
	<div class="box-body">
		<form class="form-inline" id="viewBillForm" role="form">  
			<jsp:include page="csrf_token.jsp"/>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></label>
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${customer}"/></div> 
					
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_TOTAL_DEBIT" text="Total Bill Amount"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${debitAmount}"/></div> 
					
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_POLICY_NO" text="Policy Number"/></label>
					<div class="col-sm-5"><c:out value="${pno}"/></div>  
					
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_TOTAL_CREDIT" text="Total Amount Paid"/></label>
					<div class="col-sm-5"><c:out value="${creditAmount}"/></div> 
					
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_RECORD_NO" text="Record Number"/></label> 
					<div class="col-sm-5"><c:out value="${recordNo}"/></div> 
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_BALANCE" text="Pending Bill Amount"/></label>
					<div class="col-sm-5"><c:out value="${balanceAmount}"/></div> 
					
				</div>
			</div>
			
		</form>
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-hover" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_RECORD_NO" text="Record Number"/></th>
								<th><spring:message code="LABEL_ORDER_NO" text="Order Number"/></th>
								<th><spring:message code="LABEL_RECORD_AMOUNT" text="Amount"/></th>
								<th><spring:message code="LABEL_TXNTYPE" text="Txn Type"/></th>
								<th><spring:message code="LABEL_RECORD_DATE" text="Record Date"/></th>
								<th><spring:message code="LABEL_PAY_DATE" text="Payment Date"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
				            <c:forEach items="${page.results}" var="bill">
				            <c:set var="j" value="${ j+1 }"></c:set>
				            <tr>
							    <td><c:out value="${bill.recordNumber}" /></td>
							    <td><c:out value="${bill.orderNumber}" /></td>		
								<td><c:out value="${bill.recordAmount}" /></td>		
								<td><c:out value="${bill.txnType}" /></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${bill.recordDate}" /></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${bill.paymentDate}" /></td>		
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
<style>
			
</style>	
</div>	
		<%-- <table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
		  	<tr>
		    	<td><jsp:include page="top.jsp"/></td>
		  	</tr>
		  	<tr> 	
			    <td>
				    <table width="1003"  border="0" cellspacing="0" cellpadding="0">
					    <tr>
						    <td width="159" valign="top">
						    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
						    		<tr><td><jsp:include page="left.jsp"/></td></tr>
						    	</table>
						    </td>
						    <td width="844" align="center" valign="top"  >
						        <table width="98%" border="0" height="400px" cellpadding="0" cellspacing="0">
							        <tr>
							        	<td align="left" height="30">
							        	<table width="100%"><tr><td align="left">
							        	<span class="headding" style="font-size: 15px;font-weight: bold;"><spring:message code="TITLE_VIEW_POLICY_DETAILS" text="View Policy Details"></spring:message></span>
							        	</td>							        	
							        	<td align="right"><a href="getSenelecBills.htm"><spring:message code="LABEL_SENELEC_BILLS" text="Senelec Bills" /></a>
							        	</td></tr></table>							        		
							        	</td>
							        	
							        </tr>
							        <tr height="20">
							        	<td align="center"> <div style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div></td>
							        </tr>
							        <tr>
							        <td height="40">
							        
							        <table width="100%" border="0"   cellpadding="4" cellspacing="4" style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7">
							        <tr>
							        <td><b><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></b> </td><td><c:out value="${customer}"/></td>
							        <td><b><spring:message code="LABEL_TOTAL_DEBIT" text="Total Bill Amount"/></b> </td><td><c:out value="${debitAmount}"/></b></td>
							        </tr>
							        <tr>
							        <td><b><spring:message code="LABEL_POLICY_NO" text="Policy Number"/></b> </td><td><c:out value="${pno}"/></td>
							        <td><b><spring:message code="LABEL_TOTAL_CREDIT" text="Total Amount Paid"/></b> </td><td><c:out value="${creditAmount}"/></td>
							        </tr>
							        <tr>
							        <td><b><spring:message code="LABEL_RECORD_NO" text="Record Number"/></b> </td><td><c:out value="${recordNo}"/></td>
							        <td><b><spring:message code="LABEL_BALANCE" text="Pending Bill Amount"/></b></td><td><c:out value="${balanceAmount}"/></td>
							        </tr>
							        </table>
							        
							        </td>
							        </tr>
							        <tr><td height="20"></td></tr>
							        <tr>
							        	<td valign="top">
							        	
							        		<table width="100%" border="0" cellpadding="0" cellspacing="0"  >
									            <tr height="25px" bgcolor="#30314f">     
									            	<td align="center"><span class="style1"><spring:message code="LABEL_RECORD_NO" text="Record Number"/></span></td>
									            	<td align="center"><span class="style1"><spring:message code="LABEL_ORDER_NO" text="Order Number"/></span></td>
									            	<td align="right"><span class="style1"><spring:message code="LABEL_RECORD_AMOUNT" text="Amount"/></span></td>
													<td align="center"><span class="style1"><spring:message code="LABEL_TXNTYPE" text="Txn Type"/></span></td>
													<td align="center"><span class="style1"><spring:message code="LABEL_RECORD_DATE" text="Record Date"/></span></td>
									            	<td align="center"><span class="style1"><spring:message code="LABEL_PAY_DATE" text="Payment Date"/></span></td>
									            </tr>
									            <c:set var="j" value="0"></c:set>
									            <c:forEach items="${page.results}" var="bill">
									            <c:set var="j" value="${ j+1 }"></c:set>
									            <tr height="25px" <c:if test="${ j%2 == 0 }"> bgcolor="#d2d3f1" </c:if>>
												    <td align="center"><c:out value="${bill.recordNumber}" /></td>
												    <td align="center"><c:out value="${bill.orderNumber}" /></td>		
													<td align="right"><c:out value="${bill.recordAmount}" /></td>		
													<td align="center"><c:out value="${bill.txnType}" /></td>
													<td align="center"><fmt:formatDate pattern="dd/MM/yyyy" value="${bill.recordDate}" /></td>
													<td align="center"><fmt:formatDate pattern="dd/MM/yyyy" value="${bill.paymentDate}" /></td>		
												</tr>
												</c:forEach>
										       	<tr  bgcolor="#30314f" style="color:white;">
										 			<c:if test="${page.totalPages>1}">
										            <td colspan="6" align="right" height="25px">
										            <a	<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?pno=<c:out value="${pno}" />&recordNo=<c:out value="${recordNo}" />&pageNumber=<c:out value="1" />" </c:if> style="color:white;"><c:out value="[ First " /></a>
													<a	<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?pno=<c:out value="${pno}" />&recordNo=<c:out value="${recordNo}" />&pageNumber=<c:out value="${page.currentPage-1}" />" </c:if> style="color:white;"><c:out value=" / Prev ]"/></a> 
													<c:forEach var="i" begin="${page.startPage}" end="${page.endPage}" step="1">
													<c:if test="${page.currentPage!=i}"> <a href="<c:out value="${page.requestPage}"/>?pno=<c:out value="${pno}" />&recordNo=<c:out value="${recordNo}" />&pageNumber=<c:out value="${i}" />" style="color:white;"><c:out value="${i}" /></a></c:if>
													<c:if test="${page.currentPage==i}"><b><c:out value="${i}" /></b></c:if>
													</c:forEach> 
													<a <c:if test="${page.currentPage<page.totalPages}">href="<c:out value="${page.requestPage}"/>?pno=<c:out value="${pno}" />&recordNo=<c:out value="${recordNo}" />&pageNumber=<c:out value="${page.currentPage+1}" />"</c:if> style="color:white;"><c:out value="[ Next / " /></a> 
													<a <c:if test="${page.totalPages!=page.currentPage}">href="<c:out value="${page.requestPage}"/>?pno=<c:out value="${pno}" />&recordNo=<c:out value="${recordNo}" />&pageNumber=<c:out value="${page.totalPages}" />"</c:if> style="color:white;"><c:out value="Last ]" /></a>
										            </td>
										            </c:if>
										 		</tr>
										 	</table>
										 </td>
							 		</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
	  		<tr>
	    		<td><jsp:include page="footer.jsp"/></td>
	  		</tr>
		</table> --%>
	</body>
</html>
