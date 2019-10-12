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
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript">	
		
		/* function cancelForm(){
		  //  document.processTransactionForm.action="getAccountDetails.htm";   5264 should redirect to transactions home page once clicked at cancel button
		    document.processTransactionForm.action="selectTransactionForm.htm";
		    document.processTransactionForm.submit();
		} */
		function cancelForm(){
			if(document.getElementById("transactionType").value == "137"  || document.getElementById("transactionType").value == "138"
					||( document.processTransactionForm.businessPartnerCode.value != null && document.processTransactionForm.businessPartnerCode.value != "" && document.processTransactionForm.businessPartnerCode.value != undefined)){
				 document.processTransactionForm.action="businessPartnerTransaction.htm"; 
			}else{
				 document.processTransactionForm.action="selectTransactionForm.htm";
			}
		//    document.processTransactionForm.action="selectTransactionForm.htm";
		    document.processTransactionForm.submit();
		}
		function onSubmit(){			
			
			document.processTransactionForm.button.disabled="true";
			document.processTransactionForm.method = "post";
			document.processTransactionForm.action="processTransaction.htm";
			document.processTransactionForm.submit();
			
		}	
		
	</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<spring:message code="TITLE_WELCOME_TRANSACTION_SERVICE" text="Transaction Service" /> - <c:if test="${accountDetailsDTO.transactionType == 95}"><spring:message
										code="LABEL_DEPOSIT" text="Deposit" /></c:if>
								<c:if test="${accountDetailsDTO.transactionType == 30}"><spring:message
										code="LABEL_TITLE_BALANCE" text="Balance Enquiry" /></c:if>
								
								<c:if test="${accountDetailsDTO.transactionType ==35}"><spring:message
										code="LABEL_STATEMENT" text="Ministatement" /></c:if>
									<c:if test="${accountDetailsDTO.transactionType ==98}"><spring:message
										code="LABEL_TXN_STMT" text="Txn Statement" /></c:if>
										
										<c:if test="${accountDetailsDTO.transactionType ==99}"><spring:message
										code="LABEL_WITHDRAWAL" text="Withdrawal" /></c:if>
								<c:if test="${accountDetailsDTO.transactionType == 137}">
								   <spring:message code="LABEL_CASH_IN" text="Cash In" /></c:if>
								<c:if test="${accountDetailsDTO.transactionType ==138}">
								   <spring:message code="LABEL_CASH_OUT" text="Cash Out" /></c:if>
		</h3>
	</div>
	<div class="col-md-5 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
	<spring:message code="${message}" text="" />
	</div>
	<form:form method="post" action="processTransaction.htm" name="processTransactionForm" commandName="accountDetailsDTO">
	<jsp:include page="csrf_token.jsp"/>
	<input type="hidden" name="mobileNumber" value="${ accountDetailsDTO.mobileNumber }"></input>
	<input type="hidden" name="customerId" value="${ accountDetailsDTO.customerId }"></input>
	<input type="hidden" name="customerName" value="${ accountDetailsDTO.customerName }"></input>
	<input type="hidden" id="transactionType" name="transactionType" value="${ accountDetailsDTO.transactionType }"></input>
	 <input type="hidden" name="businessPartnerCode" value="${ accountDetailsDTO.businessPartnerCode }"></input>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;">
					<c:out value="${accountDetailsDTO.mobileNumber}" />
						<c:forEach items="${accountDetailsDTO.accountList}" var="account">
						<input type="hidden" name="accountAlias" value="${account.account.alias}"></input>
						</c:forEach> 
						<input type="hidden" name="amount" value="${accountDetailsDTO.amount}"></input>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name " /> :</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${accountDetailsDTO.customerName}" /> </div>
				</div>
			</div>
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_SELECTED_ACCOUNT" text="Selected Account :" /></label>
					<div class="col-sm-5">
					<c:forEach items="${accountDetailsDTO.accountList}" var="account">
						<c:out value="${account.account.alias}"></c:out></br>
					</c:forEach>
					</div> 
					
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TXN_AMOUNT" text="Transaction Amount" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${accountDetailsDTO.amount}"></c:out></div>
				</div>
			</div>
			<div class="btn-toolbar">
			<c:if test="${message ne 'ERROR_9051'}">
				<input type="button" id="button" class="btn-primary btn pull-right"  value="<spring:message code="LABEL_CONFIRM" text="Confirm"/>" onclick="onSubmit();" />
				</c:if>
				<input type="button" class="btn-primary btn pull-right" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
			</div>
			</div>
			
</form:form>
</div>
</div>
</body>
</html>
