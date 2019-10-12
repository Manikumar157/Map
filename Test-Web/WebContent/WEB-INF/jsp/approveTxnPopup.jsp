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
<script type="text/javascript">
function  approve(){	
	return confirm("<spring:message code='ALERT_APPROVE' text='Are you sure you want to Approve The Transaction'/>");
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
    document.approveTxnForm.action="showPendingTransactions.htm";
    document.approveTxnForm.submit();
}
function displayDepositDiv(){
	
	
			document.getElementById("transactionDiv").style.visibility ="visible" ;
			displayHideBox("1");
	
	
}
function printPage(){
	document.getElementById('savePrint').style.display="none";
	document.getElementById('buttonId').style.display="none";
	
     var printContents = document.getElementById("LightBox1").innerHTML;
     var originalContents = document.body.innerHTML;

     document.body.innerHTML = '<table width="80%" align="center"><tr><td colspan="2"><img src="images/header_01.jpg" width="900" height="166" border="0" style="align:center"/></td></tr><tr><td colspan="2" align="center"><c:out value="${transactionReceipt.description}" /></td></tr><tr><td style="width:160px"></td><td>'+printContents+'</td></tr></table>';

     window.print();

     document.body.innerHTML = originalContents;
     document.getElementById('savePrint').style.display="block";
     document.getElementById('buttonId').style.display="block";
}
</script>
<style>
.grayBox {
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =   80);
}

.box_content {
	position: fixed;
	top: 27%;
	left: 30%;
	right: 40%;
	width: 60%;
	padding: 16px;
	z-index: 1002;
	overflow: auto;
}

.style2 {
	font-weight: bold
}
</style>
<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
</head>
<body>
<form:form name="approveTxnForm">
<jsp:include page="csrf_token.jsp"/>
	<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
		
		
				<td width="844" align="left" valign="top">
			<table width="98%" border="0" height="400px" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top">
						<!--******************start hidden div***************-->
						<div id="transactionDiv" style="visibility: visible; position: relative;">
	
						<div id="grayBG" class="grayBox" style="display: block;"></div>
						<div width="800px" id="LightBox1" class="box_content" style="display: block;">
							<table cellpadding="3" height="300px;" cellspacing="0" width="80%" border="0">
								<tr align="left">
									<td colspan="2" bgcolor="#FFFFFF" style="padding: 10px;">
									<div onclick="displayHideBox('1'); return false;"
										style="cursor: pointer;" align="right" id="buttonId"><img
										src="images/delete_icon.jpg" alt="" width="9" height="9" />
									<br />
									</div>
									<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0"
										style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #333333">
		
										<tr>
											<td width="100%" height="151" colspan="2" valign="top"><br />
												<table width="97%" align="center" cellpadding="3" cellspacing="3">
													<tr>
														<td><spring:message code="LABEL_MOBILE_NO" text="Mobile Number:" />:</td>
														<td><c:out value="${transactionReceipt.mobileNumber}" /></td>
													</tr>
													<tr>
														<td><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name:" />:</td>
														<td><c:out value="${transactionReceipt.customerName}" /></td>
													</tr>
													<tr>
														<td><spring:message code="LABEL_ACCOUNT_NO" text="Account No :" />:</td>
														<td><c:out value="${transactionReceipt.accountAlias}" /></td>
													</tr>
													<c:if test="${ transactionReceipt.transactionType == 95 || transactionReceipt.transactionType == 99 }">
													<tr>
														<td><spring:message code="LABEL_AMOUNT_FCFA" text="Amount [FCFA] :" />:</td>
														<td><c:out value="${transactionReceipt.amount}"></c:out>
														</td>
													</tr>
													</c:if>
													<c:if test="${ transactionReceipt.transactionType != 35 && transactionReceipt.transactionType != 98 }">
													<tr>
														<td><spring:message code="LABEL_AVAILABEL_BALANCE" text="Available Balance [FCFA] :" />:</td>
														<td><c:out value="${transactionReceipt.balance}"></c:out> </td>
													</tr>
													</c:if>
													<tr>
														<td><spring:message code="LABEL_TXN_DATE" text="Txn Date:" />:</td>
														<td><%=new java.util.Date()%></td>
													</tr>
													<tr>
														<td><spring:message code="LABEL_TXNID" text="Transaction ID" />:</td>
														<td><c:out value="${transactionReceipt.authCode}" /></td>
													</tr>
													<tr>
														<td><spring:message code="LABEL_DESCRIPTION" text="Description:" />:</td>
														<td><c:out value="${transactionReceipt.description}" /> </td>
													</tr>
												</table>
												<c:if test="${ transactionReceipt.transactionType == 35 }">
													<table style="border: 1px solid #333333;" width="100%" cellpadding="3" cellspacing="3">
														<tr>
															<td><strong><spring:message code="LABEL_AMOUNT" text="Amount" /> </strong></td>
															<td><strong><spring:message code="LABEL_TRANSACTION_TYPE" text="Tranction Type" /></strong></td>
															<td><strong><spring:message code="LABEL_TRANSACTION_DATE" text="Tranction Date" /></strong></td>
															<td><strong><spring:message code="LABEL_DESCRIPTION" text="Description" /></strong></td>
														</tr>
														<c:forEach items="${transactionReceipt.txnList}" var="txn">
															<tr>
																<td><c:out value="${txn.amount}"></c:out></td>
																<td><c:out value="${txn.transType}"></c:out></td>
																<td><fmt:formatDate value="${txn.transDate.time}" pattern="MM/dd/yyyy" /></td>
																<td><c:out value="${txn.transDesc}"></c:out></td>
															</tr>
														</c:forEach>
													</table>
												</c:if>
												<c:if test="${ transactionReceipt.transactionType == 98 }">
													<table style="border: 1px solid #333333;" width="100%" cellpadding="3" cellspacing="3">
														<tr>
															<td align="center"><a href="generatePDFTxnStatement.htm" ><b>Click to download</a> </td>
														</tr>
													</table>
												</c:if>
												<c:if test="${ transactionReceipt.transactionType != 98 }">
												<table style="border: 1px solid #333333;display: block;" width="100%" cellpadding="3" cellspacing="3" id="savePrint">
														<tr>
															<td align="left"><a href="saveTxnStatement.htm" ><b>Save</a> </td>
															<td align="right" style="width: 86%;">&nbsp;</td>
															<td align="right"><a href="javascript:printPage();" ><b>Print</a> </td>
														</tr>
												</table>
											  </c:if>
												
											</td>
										</tr>
									</table>
									
									<!--******************end hidden div***************-->
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
		
		<tr>
			<td><jsp:include page="footer.jsp" /></td>
		</tr>
	</table>
	</form:form>
</body>
</html>
