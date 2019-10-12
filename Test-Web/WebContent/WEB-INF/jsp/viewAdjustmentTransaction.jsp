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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>
<script type="text/javascript">

function validate(transactionId,transactionType,requestType){
	
	var pattern='^[A-Za-zÀ-ÿ ]*$';
	var singleSpace=/^(([ a-zA-ZÀ-ÿ]))+(\s\s([ a-zA-ZÀ-ÿ])*$)/;
	var comment=document.getElementById("comment").value;
	var adjustedAmount=document.getElementById("adjustedAmount").value;
	//var adjustedFee=document.getElementById("adjustedFee").value;
	var adjustmentTxnType = document.getElementById("adjustmentTxnType").value;
	var numericPattern='^[0-9]*$';
	
	/* if(adjustedAmount==""){
		alert('<spring:message code="LABEL_ADJUST_AMOUNT_BLANK" text="Please enter Adjusted Amount"/>');
		return false;
	} else if(adjustedAmount.search(numericPattern)==-1){
		alert('<spring:message code="LABEL_ADJUST_AMOUNT" text="Please enter Adjusted Amount Only in Numerics"/>');
		return false;
	} else if(adjustedFee==""){
		alert('<spring:message code="LABEL_ADJUST_FEE_BLANK" text="Please enter Adjusted Fee"/>');
		return false;
	} else if(adjustedFee.search(numericPattern)==-1){
		alert('<spring:message code="LABEL_ADJUST_FEE" text="Please enter Adjusted Fee Only in Numerics"/>');
		return false;
	} else if(adjustmentTxnType==""){
		alert('<spring:message code="LABEL_TXN_TYPE_BLANK" text="Please select transaction type"/>');
		return false;
	} else */ if(comment==""){
		alert('<spring:message code="ALERT_COMMENT" text="Please enter Comment"/>');
		return false;	
	} else if(comment.charAt(0) == " " || comment.charAt(comment.length-1) == " "){
	  	 alert('<spring:message code="LABEL.COMMENT.BLANK" text="Please remove the white space from comment"/>');        
	     return false;	
	} else if(comment.search(pattern)==-1){
		alert('<spring:message code="ENTER_VALID_COMMENT" text="Comment should allow alphanumeric and single white spaces"/>');
	    return false;	      
	} else if(!comment.search(singleSpace)){
		 alert('<spring:message code="ENTER_VALID_COMMENT" text="Comment should allow alphanumeric and single white spaces"/>');
	     return false;	
	} else{
		 /* <!--  @Author: Vinod Joshi ,Validation and Cross Site Scripting. > ,
			//Start--> */
		if(requestType=="reversal"){
		var msg ="Do you want to Reversal";
		if(confirm(msg)){
		/*  document.adjustmentTransaction.action="submitAdjustmentTxn.htm?txnType="+transactionId+"&transactionType="+adjustmentTxnType+"&adjustedAmount="+adjustedAmount+"&comment="+comment;
		 document.adjustmentTransaction.submit(); */
			   var url = 'submitAdjustmentTxn.htm';
			   document.getElementById("txnType").value =transactionId ;
			   document.getElementById("adjustedAmount").value =adjustedAmount ;
			   /* document.getElementById("adjustedFee").value =adjustedFee ; */
			   document.getElementById("adjustmentTxnType").value =adjustmentTxnType ;
			   document.getElementById("comment").value =comment ;
			   submitlink(url,'adjustmentTransaction');
		}}
		if(requestType=="initiate"){
	    var msg ="Do you want to Initiate";
		if(confirm(msg)){
			 /* document.adjustmentTransaction.action="initiateAdjustmentTxn.htm?txnType="+transactionId+"&transactionType="+adjustmentTxnType+"&adjustedAmount="+adjustedAmount+"&comment="+comment;
			 document.adjustmentTransaction.submit();  */
			   var url = 'initiateAdjustmentTxn.htm';
			   document.getElementById("txnType").value =transactionId ;
			   document.getElementById("adjustedAmount").value =adjustedAmount ;
			   /* document.getElementById("adjustedFee").value =adjustedFee ; */
			   document.getElementById("adjustmentTxnType").value =adjustmentTxnType ;
			   document.getElementById("comment").value =comment ;
			   submitlink(url,'adjustmentTransaction');
		}}
	}}/*End */
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
	 document.adjustmentTransaction.action="showAdjustmentForm.htm";
	 document.adjustmentTransaction.submit();
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

function imposeMaxLength(Object, MaxLen)
{
  return (Object.value.length <= MaxLen);
}
</script> 
 
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
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_REVERSAL_TXNS" text="Reversal Transaction" /></span>
		</h3>
	</div>
	<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<spring:message code="${message}" text="" />
	</div><br/>
	<div>
	</div>
	<div class="box-body">
		<form:form class="form-inline" action="viewRejectTransaction.htm" method="post" name="adjustmentTransaction" id="adjustmentTransaction">
			<jsp:include page="csrf_token.jsp"/>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${customerName}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number" />:</label> 
					<div class="col-sm-5"><c:out value="${mobileNumber}" /></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXNID" text="TxnId" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${transactionId}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_AMOUNT" text="Amount" />:</label> 
					<div class="col-sm-5"><c:out value="${amount}" /></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXN_TYPE" text="Transaction Type"/>:</label> 
					<c:if test="${transactionType==128}">
					<c:set var="status"><spring:message code="LABEL_PAY" text="m-GURUSH Pay"/></c:set>	
					</c:if>
					<c:if test="${transactionType==90}">
					<c:set var="status"><spring:message code="LABEL_SALE" text="Sale"/></c:set>
					</c:if>
					<c:if test="${transactionType==55}">
					<c:set var="status"><spring:message code="LABEL_TRF_DIRECT" text="Send Money"/></c:set>	
					</c:if>	
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${status}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXN_DATE" text="Txn Date" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${txnDate}" /></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ADJUSTED_AMOUNT" text="Adjusted Amount" /></label> 
					<%-- <input id="adjustedAmount" name="adjustedAmount"  type="text" class="form-control" value="${adjustedAmount}" /> --%>
					<div class="col-sm-5"><c:out value="${amount}" /></div>
					<input id="adjustedAmount" name="adjustedAmount"  type="hidden" class="form-control" value="${amount}" />
				</div>
				<div class="col-sm-6">
				<input id="adjustmentTxnType" name="adjustmentTxnType"  type="hidden" class="form-control" value="${transactionType}" />
					<%-- <label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_ADJUSTED_FEE" text="Adjusted Fee" /><font color="red">*</font></label> 
					<input id="adjustedFee" name="adjustedFee"  type="text" class="form-control" value="${adjustedFee}" /> --%>
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXN_TYPE" text="Transaction Type"/></label> 
					<%-- <select id="adjustmentTxnType" class="dropdown_big chosen-select" name="adjustmentTxnType">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<option value="95"><spring:message code="LABEL_CREDIT" text="Credit"/></option>
						<option value="99"><spring:message code="LABEL_DEBIT" text="Debit"/></option>
					</select> --%>
					<c:if test="${transactionType==95}">
					<c:set var="status"><spring:message code="LABEL_DEPOSIT" text="Deposit"/></c:set>	
					</c:if>
					<c:if test="${transactionType==99}">
					<c:set var="status"><spring:message code="LABEL_WITHDRAWAL" text="Withdrawl"/></c:set>
					</c:if>
					<c:if test="${transactionType==61}">
					<c:set var="status"><spring:message code="LABEL_REVERSAL_LEFT" text="Reversal"/></c:set>	
					</c:if>	
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${status}" /></div>
				</div>
			</div>
			<div class="row">
				<%-- <div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_REVERSED_TXN_TYPE" text="Reversed Txn Type"/><font color="red">*</font></label> 
					<select id="adjustmentTxnType" class="dropdown_big chosen-select" name="adjustmentTxnType">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<option value="95"><spring:message code="LABEL_CREDIT" text="Credit"/></option>
						<option value="99"><spring:message code="LABEL_DEBIT" text="Debit"/></option>
					</select>
					<c:if test="${transactionType==95}">
					<c:set var="status"><spring:message code="LABEL_DEPOSIT" text="Deposit"/></c:set>	
					</c:if>
					<c:if test="${transactionType==99}">
					<c:set var="status"><spring:message code="LABEL_WITHDRAWAL" text="Withdrawl"/></c:set>
					</c:if>
					<c:if test="${transactionType==61}">
					<c:set var="status"><spring:message code="LABEL_REVERSAL_LEFT" text="Reversal"/></c:set>	
					</c:if>	
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${status}" /></div>
				</div> --%>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_COMMENT" text="Comment"/><font color="red">*</font></label> 
					<textarea id="comment" name="comment" onkeypress="imposeMaxLength(this,50);" value="${comment}" rows="2" cols="19"></textarea>
				</div>
			</div>
			<div class="col-sm-6 col-sm-offset-9">
				<div class="btn-toolbar">
					<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_INITIATE" text="Initiate"/>" onclick="validate(${transactionId},${transactionType},'initiate');"/>
					<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="validate(${transactionId},${transactionType},'reversal');"/>													
					<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel" />" onclick=" javascript:cancelForm(); " />	
				</div>
			</div><br/><br/>
			  <input type="hidden" id="txnType" name="txnType" />
			  <input type="hidden" id="adjustedAmount" name="adjustedAmount" />
			  <input type="hidden" id="adjustedFee" name="adjustedFee" />
			  <input type="hidden" id="adjustmentTxnType" name="adjustmentTxnType" />
			  <input type="hidden" id="comment" name="comment" />
			  <input type="hidden" id="mobileNumber" name="mobileNumber" value="${mobileNumber}"/>
		</form:form>
</div>
</div>
	
</div>
</body>
</html>
