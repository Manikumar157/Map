<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bankManagement.js"></script>


<!-- Loading language definition file -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG" />.js"></script>

	
	<script type="text/javascript">

	function validate(){
		var codePattern='^\[0-9A-Za-z]*$';var numberPattern='^\[0-9]*$';
		var cardNo=document.getElementById("cardNo").value;
		var cvv=document.getElementById("cvv").value;
		var cardExpiry=document.getElementById("cardExpiry").value;
		
	    if(cardNo==""){
			alert('<spring:message code="CARD_NOT_EMPTY" text="Please enter card number"/>');
			return false;
	    }else if(cardNo.length!=15){
			alert('<spring:message code="INVALID_CARD_NO_LENGTH" text="Please enter valid card number of 15 characters"/>');
			return false;	
		}else if(cardNo.search(codePattern)==-1){
			alert('<spring:message code="INVALID_CARD_NO" text="Please enter valid card number"/>');
			return false;	
		}else if(!checForAllZeros(document.getElementById("cardNo").value)) {
			alert('<spring:message code="INVALID_CARD_NO_LENGTH" text="Please enter valid card number of 15 characters"/>');
	         return false;
		 }else if(cvv==""){
			alert('<spring:message code="CVV_EMPTY" text="Please enter CVV2"/>');
			return false;	
		}else if(cvv.search(numberPattern)==-1){
			alert('<spring:message code="INVALID_CVV" text="Please enter valid CVV2"/>');
			return false;	
		}else if(!checForAllZeros(document.getElementById("cvv").value)) {
			alert('<spring:message code="INVALID_CVV" text="Please enter valid CVV2"/>');
	         return false;
		 }else if(cardExpiry==""){
			alert('<spring:message code="CARD_EXPIRY_EMPTY" text="Please enter card expiry date"/>');
			return false;	
		}else if(cardExpiry.search(numberPattern)==-1){
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;	
		}else if(cardExpiry.length!=4){
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;	
		}else if(cardExpiry.substring(2,4)>12 || cardExpiry.substring(2,4)<=00){
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;	
		}
		document.addOperatorCardForm.submit();
 	 
	}
	
	function changeStatus(flag){
		 document.addOperatorCardForm.status[1].disabled=flag;
	 }

	function checForAllZeros(value){
		
		var count=0;
		for (var i = 0; i < value.length; i++) {
		   
			if(value.charAt(i)==0){
				count++;
			}
		}
		if(count==value.length){
			return false;
		}else{
			return true;
		}

	}
	</script>
	
</head>
<body>
<div class="col-md-12">
	<div class="box" style="height:450px;">
		<div class="box-header">
			<h3 class="box-title">
				<span><spring:message code="LABEL_CARD" text="Card" /></span>
			</h3>
		</div>
		<div class="col-md-5 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<spring:message code="${message}" text="" />
		</div>
		<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 15px;"><br />
		<!-- /.box-header -->
		<div class="col-md-offset-8">
			<label><a href="javascript:submitForm('addOperatorCardForm','showOperators.htm')"> <spring:message code="LINK_VIEW_OPERATOR" text="View Operators" /></a></label> 
		</div>
		<form:form name="addOperatorCardForm" id ="addOperatorCardForm" class="form-inline" action="saveOperatorCard.htm"	method="post" commandName="cardDto">
		<jsp:include page="csrf_token.jsp"/>
		<div class="box-body">
		
			<!-- form start -->
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_CARD_NUMBER" text="Card Number"></spring:message> <font color="red">*</font></label> 
					<div class="col-md-6">
					<form:input path="cardNo" cssClass="form-control" maxlength="15"/> <FONT color="red"><form:errors	path="cardNo" /></FONT>
					</div>				
				</div>
			</div><br>
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_CVV2" text="CVV2"></spring:message><font color="red">*</font></label> 
					<div class="col-md-6">
					<form:input path="cvv" cssClass="form-control" maxlength="3"/><FONT color="red"><form:errors path="cvv" /></FONT>
					</div>
				</div>
			</div><br/>
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_CARD_EXPIRY_DATE" text="Expiry Date(YYMM)"></spring:message><font color="red">*</font></label> 
					<div class="col-md-6">
					<form:input path="cardExpiry" cssClass="form-control" maxlength="4"/><FONT color="red"><form:errors path="cardExpiry" /></FONT>
					</div>
				</div>
			</div><br/>
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_STATUS" text="Status" /><font color="red">*</font></label> 
					<div class="col-md-7">
					<form:radiobutton path="status" value="2"  label="Active" checked="true"></form:radiobutton> &nbsp; &nbsp;
                    <form:radiobutton  path="status" value="3" label="Inactive"></form:radiobutton>
					</div>
				</div>
			</div>
		
		<form:input path="operatorId" type="hidden" />
		<form:input path="cardId" type="hidden" />
		<div class="col-sm-6 col-sm-offset-6">
		<input type="button"  value="<spring:message code="LABEL_SUBMIT"/>" onclick="validate();" class="btn btn-primary col-md-offset-10" />
		</div>
		</div>
		</form:form>
		</div>
	</div>
</div>
</body>
</html>
