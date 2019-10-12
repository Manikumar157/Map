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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	 var pane = $('.text_area');
	    pane.val($.trim(pane.val()).replace(/\s*[\r\n]+\s*/g, '\n')
	                               .replace(/(<[^\/][^>]*>)\s*/g, '$1')
	                               .replace(/\s*(<\/[^>]+>)/g, '$1'));
});
function validate(txnId,customerId){
	var comment=document.getElementById("comment").value;
	
	if(comment==""){
		alert('<spring:message code="ALERT_COMMENT" text="Please enter Comment"/>');
		return false;
	/* }else if(!checkWordLen(comment)){
		alert('<spring:message code="ALERT_WORD_LENGTH" text="Please enter words in comment not exceed more than 18"/>');
		return false; */
	}else if(comment.charAt(0) == " " || comment.charAt(comment.length-1) == " "){
	  	 alert('<spring:message code="LABEL.COMMENT.BLANK" text="Please remove the white space from comment"/>');        
	     return false;	
	}else{
	 document.rejectTransaction.action="viewRejectTransaction.htm?txnId="+txnId+"&referenceId="+customerId;
	 document.rejectTransaction.submit();    
	}
}
function checkWordLen(text) {
   
    var status = true;
    splitString = text.split(" ");
    for (var i=0; i <=splitString.length -1; i++) {
          if (splitString[i].length > 18) {
               status = false;
                break;
          }
    }
    return status;
}


function cancelForm(){
	 document.rejectTransaction.action="showPendingTransactions.htm";
	 document.rejectTransaction.submit();
}

function textCounter(field,cntfield,maxlimit) {
	 if (field.value.length > maxlimit){
	 field.value = field.value.substring(0, maxlimit);
	 alert('<spring:message code="ALERT_COMMENT_LIMIT" text="You cant enter more than 50 characters "/>' );
	
	 }
	 else{
	 cntfield.value = maxlimit - field.value.length;
	
	 }
}
 
 
</script>

<style type="text/css">
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}

#BankListdiv {
	position: center;
	visibility: visible;
	overflow: hidden;
	border: purple;
	background-color: white;
	border: 1px solid #333;
	padding: 5px;
}
</style>

</head>
<body>
<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table width="1003" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="159" valign="top">
				      <table width="100%" border="0" cellspacing="0" cellpadding="0">
					     <tr>
						   <td><jsp:include page="left.jsp" /></td>
					     </tr>
				     </table>
				</td>
				<td width="844" align="left" valign="top">
					<table width="98%" border="0" height="400px" align="center" cellpadding="0" cellspacing="0">
						<tr height="20px">
							<td align="left" class="headding" style="font-size: 15px; font-weight: bold;">
							   <spring:message code="LABEL_REJECTED_TXN" text="Rejected Transaction" />
							</td>
							
						</tr>
						
						<tr height="20px">
							<td align="center" align="left">
								<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
									<spring:message code="${message}" text="" />
								</div>
							</td>
						</tr>
						<tr>
							<td valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										<form:form action="viewRejectTransaction.htm" method="post" name="rejectTransaction">
										<table style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7" width="100%" border="0" cellspacing="3" cellpadding="3">
										<tr style="height: 30px">
										<td colspan="4" align=right><b><a href="showPendingTransactions.htm"><spring:message code="LABEL_VIEW_PENDING_TXNS" text="View Pending Transactions" /></a></b></td>
										</tr>
										    <tr>											
												<td><b><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name" />:</b></td>       
												<td>
												 <c:out value="${rejectTxn.customer.firstName} ${rejectTxn.customer.middleName} ${rejectTxn.customer.lastName}" />
												</td>	
												<td><b><spring:message
													code="LABEL_MOBILE_NO" text="Mobile Number" />:</b></td>
												<td width="30%"><c:out value="${rejectTxn.customer.mobileNumber}" /></td>
											</tr>
											
											<tr>											
												<td><b><spring:message code="LABEL_TXNID" text="TxnId" />:</b></td>       
												<td>
												 <c:out value="${rejectTxn.transactionId}" />
												</td>	
												<td><b><spring:message
													code="LABEL_AMOUNT" text="Amount" />:</b></td>
												<td width="30%"><c:out value="${rejectTxn.amount}" /></td>
											</tr>
											
											<tr>
											    <td valign="top"><b><spring:message
													code="LABEL_TXN_DATE" text="Txn Date"/>:</b></td>						
												<td width="30%"><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${rejectTxn.transactionDate}"/></td>
												<td valign="top"><b><spring:message
													code="LABEL_STATUS" text="Status"/>:</b></td>
												<td align="left">
												<spring:message code="LABEL_REJECTED" text="Rejected"/>
												</td>
												
											</tr>
										    <tr>
												<td valign="top"><b><spring:message
													code="LABEL_TXN_TYPE" text="Transaction Type"/>:</b></td>
												<td valign="top"><c:out value="${rejectTxn.transactionType.description}" /></td>
												<td valign="top"><b><spring:message
													code="LABEL_COMMENT" text="Comment"/>:</b></td>
												
												<td valign="top"><textarea  rows="2" cols="20" class="text_area" id="myTextarea" readonly="readonly">
												<c:out value="${rejectTxn.comment}" />
												</textarea></td>
												
												
												
												</td>
												
											</tr> 
											<tr style="height: 30px">
											</tr>
											
											<%--  <tr>
											 
												<td valign="top"></td>
												<td align="left"><spring:message
													code="LABEL_YOU_HAVE" text="You have"/> <input readonly type="text" name="countdown" size="1" value="255" style="border-color:white; border-style:solid;font-weight:normal;"  /><spring:message
													code="LABEL_CHARACTERS_LEFT" text="characters left."/> </td>
													
											</tr> --%>
											
											<tr>
												
											
											</tr>
										</table>
										</form:form> 
									</td>
								</tr>						
								
							</table>
							
							</td>
						</tr>
					</table>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table>
</body>
</html>
