<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

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
	function validate() {
		var codePattern = '^\[0-9A-Za-z]*$';
		var numberPattern = '^\[0-9]*$';
		var cardNo = document.getElementById("cardNo").value;
		var cvv = document.getElementById("cvv").value;
		var cardExpiry = document.getElementById("cardExpiry").value;

		if (cardNo == "") {
			alert('<spring:message code="CARD_NOT_EMPTY" text="Please enter card number"/>');
			return false;
		} else if (cardNo.length != 15) {
			alert('<spring:message code="INVALID_CARD_NO_LENGTH" text="Please enter valid card number of 15 characters"/>');
			return false;
		} else if (cardNo.search(codePattern) == -1) {
			alert('<spring:message code="INVALID_CARD_NO" text="Please enter valid card number"/>');
			return false;
		} else if (!checForAllZeros(document.getElementById("cardNo").value)) {
			alert('<spring:message code="INVALID_CARD_NO_LENGTH" text="Please enter valid card number of 15 characters"/>');
			return false;
		} else if (cvv == "") {
			alert('<spring:message code="CVV_EMPTY" text="Please enter CVV2"/>');
			return false;
		} else if (cvv.search(numberPattern) == -1) {
			alert('<spring:message code="INVALID_CVV" text="Please enter valid CVV2"/>');
			return false;
		} else if (!checForAllZeros(document.getElementById("cvv").value)) {
			alert('<spring:message code="INVALID_CVV" text="Please enter valid CVV2"/>');
			return false;
		} else if (cardExpiry == "") {
			alert('<spring:message code="CARD_EXPIRY_EMPTY" text="Please enter card expiry date"/>');
			return false;
		} else if (cardExpiry.search(numberPattern) == -1) {
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;
		} else if (cardExpiry.length != 4) {
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;
		} else if (cardExpiry.substring(2, 4) > 12
				|| cardExpiry.substring(2, 4) <= 00) {
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;
		}
		document.addCardForm.submit();

	}

	function changeStatus(flag) {
		document.addCardForm.status[1].disabled = flag;
	}

	function checForAllZeros(value) {

		var count = 0;
		for ( var i = 0; i < value.length; i++) {

			if (value.charAt(i) == 0) {
				count++;
			}
		}
		if (count == value.length) {
			return false;
		} else {
			return true;
		}

	}
	//@start Vinod joshi  Date:18/12/2018 purpose:cross site Scripting -->
	function viewOrgnization(url,bankId,frmId){
		document.getElementById('bankId').value=bankId;
		submitlink(url,'addCardForm');
	}
</script>


</head>
<body>
	<div class="col-md-12">
		<div class="box" style="height:430px;">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_CARD" text="Card" /></span>
				</h3>
			</div>
			<br />
			<!-- /.box-header -->
			<div class="col-sm-6 col-sm-offset-3" style="border: 1px solid;border-radius: 15px;">
			<form:form name="addCardForm" action="saveCard.htm" method="post" id="addCardForm"
				commandName="cardDto">
				<jsp:include page="csrf_token.jsp"/>
				<div class="box-body">
					<!-- form start -->
						<div class="col-md-6 col-md-offset-8">
							<%-- <label class="col-sm-7"><a
								href="javascript:submitForm('menuForm','editBankForm.htm?bankId=${cardDto.bankId}')"> <spring:message
										code="LABEL_VIEW_BANK" text="View Bank"></spring:message>
							</a></label> --%>
							
							<label class="col-sm-7"><a href="javascript:viewOrgnization('editBankForm.htm','<c:out value="${cardDto.bankId}"/>','addCardForm')"><spring:message
										code="LABEL_VIEW_BANK" text="View Bank"></spring:message>
							</a></label>
							
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_CARD_NUMBER" text="Card Number"></spring:message> <font
								color="red">*</font></label>
							<div class="col-md-6">
							<form:input path="cardNo" cssClass="form-control" maxlength="15" />
							<FONT color="red"><form:errors path="cardNo" /></FONT>
							</div>
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message code="LABEL_CVV2"
									text="CVV2"></spring:message><font color="red">*</font></label>
							<div class="col-md-6">
							<form:input path="cvv" cssClass="form-control" maxlength="3" />
							<FONT color="red"><form:errors path="cvv" /></FONT>
							</div>
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_CARD_EXPIRY_DATE" text="Expiry Date(YYMM)"></spring:message><font
								color="red">*</font></label>
							<div class="col-md-6">
							<form:input path="cardExpiry" cssClass="form-control"
								maxlength="4" />
							<FONT color="red"><form:errors path="cardExpiry" /></FONT>
							</div>
					</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
							<div class="col-md-6">
							<form:radiobutton path="status" value="2" label="Active"
								checked="true"></form:radiobutton>
							&nbsp; &nbsp;
							<form:radiobutton path="status" value="3" label="Inactive"></form:radiobutton>
							</div>
					</div>
				
				<form:input path="bankId" type="hidden" />
				<form:input path="cardId" type="hidden" />
				<%-- <form:hidden path="bankId" name="bankId" type="hidden" id="bankId" type=bankId value="${cardDto.bankId}"/> --%>
				<div class="col-sm-6 col-sm-offset-9">
					<input type="button" class="btn btn-primary"
						value="<spring:message code="LABEL_SUBMIT"/>"
						onclick="validate();" />
				</div>
			</form:form>
			</div>
		</div>
		</div>
	</div>
	<%-- <table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table>
			<tr>
				<td width="159" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><jsp:include page="left.jsp" /></td>
					</tr>
				</table>
				</td>
				<td width="844" valign="top">
				<table width="98%" border="0" height="400px" align="left" cellpadding="0" cellspacing="0">
					<tr height="20px">
						<td align="left" class="headding" width="20%"
							style="font-size: 15px; font-weight: bold;"><spring:message
							code="LABEL_CARD" text="Card" /></td>
					</tr>
					<tr height="20px">
						<td align="center" colspan="2">
						<div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
							code="${message}" text="" /></div>
						</td>
					</tr>
					<form:form name="addCardForm" action="saveCard.htm"	method="post" commandName="cardDto">
						<tr height="120px">
							<td valign="top" colspan="2">
							<table style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7" align="center"  width="60%" cellpadding="4" cellspacing="4">
								<tr>
								<td colspan="3" align="right">
								<b><a href="editBankForm.htm?bankId=${cardDto.bankId}">
								<spring:message code="LABEL_VIEW_BANK" text="View Bank"></spring:message>
								</a></b>
								</td>
								</tr>
								
								<tr>
									<td>
									<spring:message code="LABEL_CARD_NUMBER" text="Card Number"></spring:message>
									<font color="red">*</font>
									</td>
									<td>
									<form:input path="cardNo" cssClass="text_feild" maxlength="15"/>
									<FONT color="red"><form:errors	path="cardNo" /></FONT>
									</td>									
								</tr>
								<tr>
									<td><spring:message code="LABEL_CVV2"
										text="CVV2"></spring:message><font color="red">*</font></td>
									<td><form:input path="cvv" cssClass="text_feild" maxlength="3"/><FONT color="red"><form:errors
										path="cvv" /></FONT></td>	
								</tr>
								<tr>
								    <td><spring:message code="LABEL_CARD_EXPIRY_DATE"
										text="Expiry Date(YYMM)"></spring:message><font color="red">*</font></td>
									<td><form:input path="cardExpiry" cssClass="text_feild" maxlength="4"/><FONT color="red"><form:errors
										path="cardExpiry" /></FONT></td>								
								</tr>
								<tr>
								    <td><spring:message code="LABEL_STATUS" text="Status" /><font color="red">*</font></td>
                                    <td>
                                    <form:radiobutton path="status" value="2"  label="Active" checked="true"></form:radiobutton> 
                                    <form:radiobutton  path="status" value="3" label="Inactive"></form:radiobutton>
                                    </td>
									<form:input path="bankId" type="hidden" />
									<form:input path="cardId" type="hidden" />
								</tr>
								<tr>									
									<td align="right" colspan="5"><input type="button"  value="<spring:message code="LABEL_SUBMIT"/>" onclick="validate();" /></td>
								</tr>								
							</table>
							</td>
						</tr>
						<tr height="30px">
							<td colspan="2"></td>
						</tr>
						<c:if test="${cardDto.cardId==null }">
						<script>window.onload=function(){changeStatus(true);};</script>
						</c:if>
												
					</form:form>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table> --%>
</body>
</html>
