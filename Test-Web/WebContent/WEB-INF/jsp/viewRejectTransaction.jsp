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
<title><spring:message code="LABEL_TITLE" text="GIM Mobile" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>
<script type="text/javascript">

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
<div class="row">	
<div class="col-lg-12">
<div class="box">

<div class="box-header">
		<h3 class="box-title">
			<span>   <spring:message code="LABEL_REJECT_TXNS" text="Reject Transactions" > </spring:message></span>
		</h3>
	</div>


						
	<form:form action="viewRejectTransaction.htm" method="post" name="rejectTransaction">

	<jsp:include page="csrf_token.jsp"/>
	
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"> <c:out value="${rejectTxn.customer.firstName} ${rejectTxn.customer.middleName} ${rejectTxn.customer.lastName}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message
													code="LABEL_MOBILE_NO" text="Mobile Number" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${rejectTxn.customer.mobileNumber}" /> </div>
				</div>
			</div>
			
						
						
					<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXNID" text="TxnId" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"> <c:out value="${rejectTxn.transactionId}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message
													code="LABEL_AMOUNT" text="Amount" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${rejectTxn.amount}" /></div>
				</div>
			</div>
				
						
						
								
					<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message
													code="LABEL_TXN_DATE" text="Txn Date"/>:</label> 
					<div class="col-sm-5" style="margin-top:4px;"> <fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${rejectTxn.transactionDate}"/></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><b><spring:message
													code="LABEL_STATUS" text="Status"/>:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:set var="status"><spring:message code="LABEL_PENDING" text="Pending"/></c:set>
													<c:out value="${status}" /></div>
				</div>
			</div>
						
						
									
					<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message
													code="LABEL_TXN_TYPE" text="Transaction Type"/>:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${rejectTxn.transactionType.description}" /></div>
				</div>
				<div class="col-sm-6">
				<label class="col-sm-5" style="margin-top:4px;"><spring:message
													code="LABEL_Comment" text="Comment"/>:</label> 
				
					<textarea autofocus="autofocus" rows="2" cols="20" id="comment" name="comment" class="text_area"  onKeyDown="textCounter(document.rejectTransaction.comment,document.rejectTransaction.countdown,50)" onKeyUp="textCounter(document.rejectTransaction.comment,document.rejectTransaction.countdown,50)" >
												</textarea>
				</div>
			</div>
						
						
					
					
					<div class="col-md-5 col-md-offset-5">
								<input type="button" class="btn btn-primary"
								id="submitButton"
								 value="<spring:message code="LABEL_SUBMIT"  text="Submit"/>"
					 onclick="validate(${rejectTxn.transactionId},${rejectTxn.customer.customerId});" />
								<div class="space"></div>
								<input type="button" class="btn btn-default"
								value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
								 onclick="cancelForm();" />
							</div>
					
					
					</div>
						
</form:form>
</div>
</div>
</div>

</body>
</html>
