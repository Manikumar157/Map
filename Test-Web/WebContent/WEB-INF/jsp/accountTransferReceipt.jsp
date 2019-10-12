<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript">
function validate(){
	
	// var mnumber=document.getElementById("mobileNumber").value;
	// var businessPartnerCode=document.getElementById("businessPartnerCode").value;
//	 var isdNum=isdCode.split(",");
//	 var mobileNumLen=isdNum[1];
	 var numPattern='^[0-9]*$';

	
   
    /* if(document.getElementById("transactionType").value == 95 || document.getElementById("transactionType").value == 99){
		document.transactionForm.action ="getBusinessPartnerDetails.htm";
		document.transactionForm.submit();
    } 	 
    else{ */
    var url = "confAccountToAccount.htm";
   // submitForm('selectTransactionForm',url);
    document.selectTransactionForm.action ="confAccountToAccount.htm";
	document.selectTransactionForm.submit();
    

	// }
			}
</script>

<script type="text/javascript">
	function openNewWindow(url) {
		window.open(url, 'mGurush',
				'width=800,height=800,toolbar=1,resizable=0');
		return false;
	}
	function clearForm() {
		document.userRegistrationForm.reset();
	}
	function clearForm() {
		document.userRegistrationForm.reset();
	}
	function onSubmit() {
		document.accountDetails.submit();
	}
	function displayDepositDiv() {

		if (checkValidate() == true) {
			if (document.getElementById("customerId").value == "1") {
				document.getElementById("transactionDiv").style.visibility = "visible";
				displayHideBox("1");
			} else {
				alert("<spring:message code='VALID_CUSTOMER_NOT_EXIST' text='Customer Doesnt Exist'/>");
				return false;
			}
		}
	}
	function displayHideBox(boxNumber) {
		if (document.getElementById("LightBox" + boxNumber).style.display == "none") {
			document.getElementById("LightBox" + boxNumber).style.display = "block";
			document.getElementById("grayBG").style.display = "block";
		} else {
			document.getElementById("LightBox" + boxNumber).style.display = "none";
			document.getElementById("grayBG").style.display = "none";
		}
	}
	function checkValidate() {
		if (document.getElementById("customerId").value == "") {
			alert("<spring:message code='VALID_CUSTOMER_ID' text='Please enter Customer ID'/>");
			return false;
		} else if (document.getElementById("datetrans").checked == true) {
			if (document.getElementById("fromDate").value == "") {
				alert("<spring:message code='VALID_FROM_DATE' text='Please select From Date' />");
				return false;
			} else if (document.getElementById("toDate").value == "") {
				alert("<spring:message code='VALID_TO_DATE' text='Please select To Date' /> ");
				return false;
			} else
				return true;
		} else if (document.getElementById("datetrans").checked == false) {
			if (document.getElementById("not").value == "") {
				alert("<spring:message code='VALID_NO_OF_TRANSACTIONS' text='Please enter No Of Transactions'/>");
				return false;
			} else
				return true;
		} else {
			return true;
		}
	}
	function typeSelect() {
		if (document.getElementById("datetrans").checked == true) {
			document.getElementById("not").disabled = true;
			document.getElementById("not").value = "";
			document.getElementById("fromDate").disabled = false;
			document.getElementById("toDate").disabled = false;
		}
		if (document.getElementById("datetrans").checked == false) {
			document.getElementById("not").disabled = false;
			document.getElementById("fromDate").value = "";
			document.getElementById("fromDate").disabled = true;
			document.getElementById("toDate").value = "";
			document.getElementById("toDate").disabled = true;
		}
	}
	// vineeth changes
<%-- 	 function printPage() {
		document.getElementById('savePrint').style.display = "none";
		document.getElementById('buttonId').style.display = "none";

		var printContents = document.getElementById("LightBox1").innerHTML;
		var originalContents = document.body.innerHTML;

		document.body.innerHTML = '<table width="80%" align="center"><tr><td colspan="2"><img src="images/header_01.jpg" width="900" height="166" border="0" style="align:center"/></td></tr><tr><td colspan="2" align="center"><c:out value="${transactionReceipt.description}" /></td></tr><tr><td style="width:160px"></td><td>'
				+ printContents + '</td></tr></table>';

		window.print();
		<img
		src="<%=request.getContextPath()%>/images/Easybank logo.png"
		alt="logo" class="img_resize1" />
		document.body.innerHTML = originalContents;
		document.getElementById('savePrint').style.display = "block";
		document.getElementById('buttonId').style.display = "block";
	}  --%>
	function printPage() {
		
		var mainContent = document.getElementById("LightBox1").innerHTML;
		var toPrint = '<table width="80%" align="center"><tr><td colspan="2"><img src="<%=request.getContextPath()%>/images/header_01.JPG" width="900" height="166" border="0" style="align:center"/></td></tr><tr><td colspan="2" align="center"><c:out value="${transactionReceipt.description}" /></td></tr><tr><td style="width:160px"></td><td>'
			+ mainContent + '</td></tr></table>';

        var popupWin = window.open('', '_blank', 'width=750,height=600,location=no,left=200px');

        popupWin.document.open();

        popupWin.document.write('<html><head><title>Transactions</title><style>#savePrint, #buttonId{display: none !important;}</style></head><body onload="window.print();window.close();">')

        popupWin.document.write(toPrint);

        popupWin.document.write('</html>');

        popupWin.document.close();
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
	filter: alpha(opacity =       80);
}

.box_content {
	position: fixed;
	top: 27%;
	left: 21%;
	right: 40%;
	width: 67%;
	padding: 16px;
	z-index: 1002;
	overflow: auto;
}
td{
	padding-left: 5px;
}
.style2 {
	font-weight: bold
}
</style>


</head>
<body>

	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
				<c:if test="${roleAccess eq 2}">
					<span><spring:message
							code="TITLE_WELCOME_PRINCIPAL_AGENT_TRANSACTION_SERVICE"
							text="View Principal Agent Transactions" /></span> <!--Author:Vineeth Reddy /message changed from Transaction to Transaction Services bug no:5341 -->
					</c:if>
					<c:if test="${roleAccess ne 2}">
					<span><spring:message
							code="TITLE_WELCOME_BUSINESS_PARTNER_TRANSACTION_SERVICE"
							text="View BusinessPartner Transactions" /></span> 
					</c:if>
				</h3>
			</div>
			<br />
			<!-- /.box-header -->
			<div class="col-sm-5 col-sm-offset-4">
				<div class="col-sm-5 col-sm-offset-4"
					style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
			</div>
			<div class="box-body"
				style="border: 1px solid; width: 70%; margin-left: 150px; border-radius: 15px;">
				<!-- form start -->
				<form id="selectTransactionForm" class="form-inline"
					action="confAccountToAccount.htm" method="post" name="selectTransactionForm">
					<jsp:include page="csrf_token.jsp" />
					<div class="row">
				<%-- 	<input type="hidden" id="isdCode" value="${isdCode},${mobileNumLength}"/> --%>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_COUNTRY" text="Country:" /><!-- <font color="red">*</font> -->
									</label>
									<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
									
						<%-- 			commenting to make a default country as SouthSudan,
									by vineeth on 12-11-2018
							<select class="dropdown" id="isdCode" name="isdCode">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${countryList}" var="country">
										<c:forEach items="${country.countryNames}" var="cNames">
											<c:if test="${cNames.comp_id.languageCode==lang }">
												<option
													value="${country.isdCode},${country.mobileNumberLength}">${cNames.countryName}</option>
											</c:if>
										</c:forEach>
									</c:forEach>
							</select> --%>
						</div>
					</div>
					<br />
					<%-- <div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message code="LABEL_TXN_TYPE"
									text="Txn Type" /><font color="red">*</font></label> 
									<div class="col-sm-5">
									<select  onchange="getval(this);"
								name="transactionType" id="transactionType" class="dropdown chosen-select transactionType">
								<option value="">
									<spring:message code="LABEL_SELECT" text="Please select" />
								</option>
								<authz:authorize ifAnyGranted="ROLE_bankteller,ROLE_bankadmin,ROLE_businesspartnerL1,ROLE_businesspartnerL2">
								<authz:authorize ifAnyGranted="ROLE_bankteller,ROLE_businesspartnerL1,ROLE_businesspartnerL2">
								<option value="30">
									<spring:message code="LABEL_TITLE_BALANCE"
										text="Balance Enquiry" />
								</option>
								<authz:authorize ifAnyGranted="ROLE_bankteller">
									<option value="95"><spring:message
										code="LABEL_DEPOSIT" text="Deposit" /></option>
										<form:option value="24"> <spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></form:option>
													
								</authz:authorize>
									<option value="35"><spring:message
										code="LABEL_STATEMENT" text="Ministatement" /></option>
									<option value="98"><spring:message
										code="LABEL_TXN_STMT" text="Txn Statement" /></option>
									<authz:authorize ifAnyGranted="ROLE_businesspartnerL2">
									<option value="137"><spring:message
										code="LABEL_CASH_IN" text="Cash In" /></option>
									<option value="138"><spring:message
										code="LABEL_CASH_OUT" text="Cash Out" /></option>
									</authz:authorize>
									</authz:authorize>
									<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_businesspartnerL1">
									
									<option value="143"><spring:message
										code="LABEL_TRANSFER_EMONEY" text="Transfer eMoney" /></option>
										</authz:authorize>
								<authz:authorize ifAnyGranted="ROLE_bankteller">
									<option value="99"><spring:message
										code="LABEL_WITHDRAWAL" text="Withdrawal" /></option>
								</authz:authorize>
								</authz:authorize>
							</select>
							</div>
						</div>
					</div> --%>
						<%-- <div class="row">
						<div class="col-md-12 businessCode">
							<c:if test="${roleAccess eq 2}">
							<label class="col-sm-6"><strong><spring:message
										code="LABEL_BUSINESS_PARTNER_PRINCIPAL_AGENT_CODE" text="Principal Agent Code" /></strong><font
								color="red">*</font></label> 
								</c:if>
								<c:if test="${roleAccess ne 2}">
								<label class="col-sm-6"><strong><spring:message
										code="LABEL_BUSINESS_PARTNER_CODE" text="Business Partner Code" /></strong><font
								color="red">*</font></label> 
									</c:if>
								<div class="col-sm-5">
								<input id="businessPartnerCode"
								name="businessPartnerCode" maxlength="10" class="form-control" />
						</div>
												
					</div>

						<div class="col-md-12 businessdet">
							<label class="col-sm-6 "><strong><spring:message
										code="LABEL_AGENT_MOBILE_NO" text="Agent Mobile Number" /></strong><font
								color="red">*</font></label> 
								<div class="col-sm-5 ">
								<input id="mobileNumber"
								name="mobileNumber" maxlength="9" class="form-control" />
								</div>
						
					</div>
					</div> --%>
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_FROM_ACC" text="From" /><font color="red">*</font></label>
							<div class="col-sm-5">
								<select cssClass="dropdown chosen-select" id="fromAccount" name="fromAccount"
									style="width: 200px;">
									<option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</option>
									<%-- <options items="${accountsList}" itemLabel="accountAlias"	itemValue="accountNumber"></options> --%>
									<c:forEach items="${accountsList}" var="accounts">
										<option value="<c:out value="${accounts.accountNumber}"/>">
											<c:out value="${accounts.accountAlias}" />
										</option>
									</c:forEach>
									<select>
										<%-- <font color="red"> <form:errors path="accountNumber" /></font> --%>
							</div>


						</div>

						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_TO_ACC" text="To" /><font color="red">*</font></label>
							<div class="col-sm-5">
								<select cssClass="dropdown chosen-select" id="toAccount" name ="toAccount"
									style="width: 200px;">
									<option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</option>
									<%-- <options items="${accountsList}" itemLabel="accountAlias"	itemValue="accountNumber"></options> --%>
									<c:forEach items="${accountsList}" var="accounts">
										<option value="<c:out value="${accounts.accountNumber}"/>">
											<c:out value="${accounts.accountAlias}" />
										</option>
									</c:forEach>
									</select>
										<%-- <font color="red"> <form:errors path="accountNumber" /></font> --%>
							</div>


						</div>
					</div>
					<!-- /.box-body -->

					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							onclick="validate();"
							value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" /> <br />
						<br />
					</div>
					<br />
				</form>
			</div>
			<br />

			<!-- /.box-body -->

		</div>
		<!-- /.box -->
	</div>
	<!--******************start hidden div***************-->
	<div id="transactionDiv"
		style="visibility: visible; position: relative;">

		<div id="grayBG" class="grayBox" style="display: block;"></div>
		<div width="800px" id="LightBox1" class="box_content"
			style="display: block;">
			<table cellpadding="3" height="300px;" cellspacing="0" width="90%"
				border="0">
				<tr align="left">
					<td colspan="2" bgcolor="#FFFFFF" style="padding: 10px;">
						<div onclick="displayHideBox('1');" style="cursor: pointer;"
							align="right" id="buttonId">
							<img src="images/delete_icon.jpg" alt="" width="9" height="9" />
							<br />
						</div>
						<table width="95%" border="0" align="center" cellpadding="0"
							cellspacing="0"
							style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #333333">

							<tr>
								<td width="100%" height="151" colspan="2" valign="top"><br />
									<table width="97%" align="center" cellpadding="3"
										cellspacing="3">

										<tr>
											<td><spring:message code="LABEL_ACCOUNT_NO"
													text="Account No :" />:</td>
											<td><c:out value="${transactionReceipt.accountAlias}" /></td>
										</tr>

											<tr>
												<td><spring:message code="LABEL_AMOUNT_FCFA"
														text="Amount [SSP] :" />:</td>
												<td>
												<fmt:formatNumber type = "number" minFractionDigits = "2" maxFractionDigits="2" value = "${transactionReceipt.amount}" />
												<%-- <c:out value="${transactionReceipt.amount}"></c:out> --%>
												</td>
											</tr>
 
										<%-- 	<tr>
												<td><spring:message code="LABEL_AVAILABEL_BALANCE"
														text="Available Balance [SSP] :" />:</td>
												<td><c:out value="${transactionReceipt.balance}"></c:out>
												</td>
											</tr> --%>

										<tr>
											<td><spring:message code="LABEL_TXN_DATE"
													text="Txn Date:" />:</td>
											<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="<%=new java.util.Date()%>" /></td>
										</tr>

										<tr>
											<td><spring:message code="LABEL_DESCRIPTION"
													text="Description:" />:</td>
											<td><c:out value="${transactionReceipt.description}" />
											</td>
										</tr>
									</table>
									<%--  <c:if test="${ transactionReceipt.transactionType == 35 }">
										<table style="border: 1px solid #333333;" width="99.4%"
											cellpadding="3" cellspacing="3">
											<tr>
												<td><strong><spring:message
															code="LABEL_TRANSACTION_DATE" text="Tranction Date" /></strong></td>
												<td><strong><spring:message
															code="LABEL_DESCRIPTION" text="Description" /></strong></td>
												<td><strong><spring:message
															code="LABEL_AMOUNT" text="Amount" /> </strong></td>
												<td><strong><spring:message
															code="LABEL_TRANSACTION_TYPE" text="Tranction Type" /></strong></td>
											</tr>
											<c:forEach items="${transactionReceipt.txnList}" var="txn">
												<tr>
													<td><fmt:formatDate value="${txn.transDate.time}"
															pattern="dd/MM/yyyy" /></td>
													<td><c:out value="${txn.transDesc}"></c:out></td>
													<td style="float:right">
													<fmt:formatNumber type = "number" minFractionDigits = "2" maxFractionDigits="2" value = "${txn.amount}" />         
													<c:out value="${txn.amount}"></c:out></td>
													<td style="text-align:center;"><c:out value="${txn.transType}"></c:out></td>
												</tr>
											</c:forEach>
										</table>
									</c:if>  --%>
									<c:if test="${ transactionReceipt.transactionType == 144 }">
										<table width="100%"
											cellpadding="3" cellspacing="3">
											<tr>
												<td align="center"><a
													href="generatePDFTxnStatement.htm"><b><spring:message
																code="LABEL_CLICK_DOWNLOAD" text="Click to download" /></a>
												</td>
											</tr>
										</table>
									</c:if> 
								<%-- 	<c:if test="${ transactionReceipt.transactionType != 98 }">
										<table style="display: block;"
											width="100%" cellpadding="3" cellspacing="3" id="savePrint">
											<tr>
												<td align="left"><a href="saveTxnStatement.htm"><b><spring:message
																code="LABEL_SAVE" text="Save" /></a></td>
												<td align="right" style="width: 93%;">&nbsp;</td>
												<td><a href="javascript:printPage();"><b><spring:message
																code="LABEL_PRINT" text="Print" /></a></td>
											</tr>
										</table>
									</c:if> --%>
									</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
