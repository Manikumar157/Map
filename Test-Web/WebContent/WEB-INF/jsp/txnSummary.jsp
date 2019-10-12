<%-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <jsp:include page="../jsp/taglib.jsp" /> --%>
<html>
<head>

<%-- <link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" /> --%>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>

<!-- <script type="text/javascript">
  setTimeout(function () { location.reload(true); }, 60000);
  

</script> -->

</head>
<body> 
<!-- <div class="wrap-fluid" id="paper-bg">
 -->
                <div class="row">
                    <div class="col-lg-12">
                        <div class="box">
                            <div class="box-header">
                                
                                <h3 class="box-title"><i class="fontello-doc"></i> 
                                    <span><spring:message
								code="LABEL_TXN_SUMMERY" text=" Transaction Summary " /></span>
                                </h3>
                            </div>
                            <div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
								code="${message}" text="" /></div>
                            <!-- /.box-header -->
                            <form:form name="txnSummaryform" commandName="txnSummaryDTO">
                            <jsp:include page="csrf_token.jsp"/>
								<form:hidden path="bankId" />
								<form:hidden path="countryId" />
								<form:hidden path="transactionType" />
								<form:hidden path="fromDate" />
								<form:hidden path="toDate" />
							</form:form>
							<br />
                            <div class="box-body table-responsive">
                                <table class="table table-bordered table-hover" style="text-align:center">
                                    <thead>
                                        <tr>
                                            <th><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></th>
                                            <th><spring:message code="LABEL_NO_OF_TRANSACTIONS" text="Number Of Txns" /></th>
                                            <th><spring:message code="LABEL_TXN_AMOUNT" text="Transaction Amount" /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${pageSummData}" var="txnInfo">
								<tr>
									<td>
									<c:if test="${txnInfo.txnName eq 'Deposit'}">
                                       <c:set var="status" ><spring:message code="LABEL_DEPOSIT" text="Deposit"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Withdrawal'}">
                                       <c:set var="status" ><spring:message code="LABEL_WITHDRAWAL" text="Withdrawl"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Balance Enquiry'}">
                                       <c:set var="status" ><spring:message code="LABEL_TITLE_BALANCE" text="Balance Enquiry"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Mini Statement'}">
                                       <c:set var="status" ><spring:message code="LABEL_STATEMENT" text="Mini Statement"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'TxnStatement'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXN_STMT" text="Txn Statement"/></c:set>
                                    </c:if>
                                      <c:if test="${txnInfo.txnName eq 'Activation'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_ACTIVATION" text="Activation"/></c:set>
                                    </c:if>
                                      <c:if test="${txnInfo.txnName eq 'Txn Pin Change'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_TXN_PIN_CHANGE" text="TXN Pin Change"/></c:set>
                                    </c:if>
                                      <c:if test="${txnInfo.txnName eq 'Transfer Direct'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_TRF_DIRECT" text="Transfer Direct"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Send Money'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_SEND_MONEY" text="Send Money"/></c:set>
                                    </c:if>
                                      <c:if test="${txnInfo.txnName eq 'Reset Pin'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_RESET_PIN" text="Reset Pin"/></c:set>
                                    </c:if>
                                      <c:if test="${txnInfo.txnName eq 'Reset Txn Pin'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_RESET_TXN_PIN" text="Reset TXN Pin"/></c:set>
                                    </c:if>
                                      <c:if test="${txnInfo.txnName eq 'Sale'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_SALE" text="Sale"/></c:set>
                                    </c:if>
                                     <c:if test="${txnInfo.txnName eq 'Buy Airtime'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_TOPUP" text="Buy Airtime"/></c:set>
                                    </c:if>
                                     <c:if test="${txnInfo.txnName eq 'ReActivation'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_REACTIVATION" text="Re-Activation"/></c:set>
                                    </c:if>
                                     <c:if test="${txnInfo.txnName eq 'Bill Payment'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_BILL_PAYMENT" text="Bill Payment"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Reversal'}">
                                       <c:set var="status" ><spring:message code="LABEL_REVERSAL" text="Reversal"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Adjustment'}">
                                       <c:set var="status" ><spring:message code="LABEL_ADJUSTMENT" text="Adjustment"/></c:set>
                                    </c:if>
                                     <c:if test="${txnInfo.txnName eq 'SMS Cash'}">
                                       <c:set var="status" ><spring:message code="LABEL_SMSCASH" text="SMS Cash"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Add Account'}">
                                       <c:set var="status" ><spring:message code="LABEL_ADD_BANK_ACCOUNT" text="Add Bank Account"/></c:set>
                                    </c:if>
                                     <c:if test="${txnInfo.txnName eq 'Add Card'}">
                                       <c:set var="status" ><spring:message code="LABEL_ADD_CARD" text="Add Card"/></c:set>
                                    </c:if>
                                   
									<c:out value="${status}"/></td>
									<%-- <c:out
										value="${txnInfo.txnName}" /> --%>
								    <td><c:out
										value="${txnInfo.noOfTrns}" /></td>
									<td><c:out
										value="${txnInfo.sum}" /></td>
								</tr>
							</c:forEach>
                                        
                                    </tbody>
                                    <%-- <tfoot>
                                        <tr>
                                            <th><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></th>
                                            <th><spring:message code="LABEL_NO_OF_TRANSACTIONS" text="Number Of Txns" /></th>
                                            <th><spring:message code="LABEL_TXN_AMOUNT" text="Transaction Amount" /></th>
                                        </tr>
                                    </tfoot> --%>
                                </table>
                            </div>
                            <!-- /.box-body -->
                        </div>
                        <!-- /.box -->
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">
                        <div class="box">
                            <div class="box-body table-responsive">
                                <table id="example1" class="table table-bordered table-striped" style="text-align:center">
                                    <thead>
                                        <tr>
                                            <th><spring:message code="LABEL_TXNID" text="Transaction ID" /></th>
											<th><spring:message code="LABEL_TXN_TYPE" text="Transaction Type" /></th>
											<th><spring:message code="LABEL_ALIAS" text="Account Alias" /></th>
											<th><spring:message code="LABEL_TXNDATE" text="Date" /></th>
											<th><spring:message code="LABEL_TXN_AMOUNT" text="Transaction Amount" /></th>
											<th><spring:message code="LABEL_SERV_CHARGE" text="Serv. Charge" /></th>
											<th><spring:message code="LABEL_TXN_STATUS" text="Status" /></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <% int i = 0; %>
                                        <c:forEach items="${pageListData.results}" var="txnListInfo">
									<tr>
										<td><c:out value="${txnListInfo.txnId}" /></td>
									 
									 <c:if test="${txnListInfo.txnName eq 'Deposit'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_DEPOSIT" text="Deposit"/></c:set>
                                    </c:if>
                                    <c:if test="${txnListInfo.txnName eq 'Withdrawal'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_WITHDRAWAL" text="Withdrawl"/></c:set>
                                    </c:if>
                                    <c:if test="${txnListInfo.txnName eq 'Balance Enquiry'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TITLE_BALANCE" text="Balance Enquiry"/></c:set>
                                    </c:if>
                                    <c:if test="${txnListInfo.txnName eq 'Mini Statement'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_STATEMENT" text="Mini Statement"/></c:set>
                                    </c:if>
                                    <c:if test="${txnListInfo.txnName eq 'TxnStatement'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXN_STMT" text="Txn Statement"/></c:set>
                                    </c:if>
                                      <c:if test="${txnListInfo.txnName eq 'Activation'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_ACTIVATION" text="Activation"/></c:set>
                                    </c:if>
                                      <c:if test="${txnListInfo.txnName eq 'Txn Pin Change'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_TXN_PIN_CHANGE" text="TXN Pin Change"/></c:set>
                                    </c:if>
                                      <c:if test="${txnListInfo.txnName eq 'Transfer Direct'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_TRF_DIRECT" text="Transfer Direct"/></c:set>
                                    </c:if>
                                    <c:if test="${txnInfo.txnName eq 'Send Money'}">
                                       <c:set var="status" ><spring:message code="LABEL_TXNTYPE_SEND_MONEY" text="Send Money"/></c:set>
                                    </c:if>
                                      <c:if test="${txnListInfo.txnName eq 'Reset Pin'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_RESET_PIN" text="Reset Pin"/></c:set>
                                    </c:if>
                                      <c:if test="${txnListInfo.txnName eq 'Reset Txn Pin'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_RESET_TXN_PIN" text="Reset TXN Pin"/></c:set>
                                    </c:if>
                                      <c:if test="${txnListInfo.txnName eq 'Sale'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_SALE" text="Sale"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'Buy Airtime'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_TOPUP" text="Buy Airtime"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'ReActivation'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_REACTIVATION" text="Re-Activation"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'Bill Payment'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_TXNTYPE_BILL_PAYMENT" text="Bill Payment"/></c:set>
                                    </c:if>
                                    <c:if test="${txnListInfo.txnName eq 'Reversal'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_REVERSAL" text="Reversal"/></c:set>
                                    </c:if>
                                    <c:if test="${txnListInfo.txnName eq 'Adjustment'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_ADJUSTMENT" text="Adjustment"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'SMS Cash'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_SMS_CASH" text="SMS Cash"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'SMS Cash Others'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_SMS_CASH_OTHERS" text="SMS Cash Others"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'Add Account'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_ADD_BANK_ACCOUNT" text="Add Bank Account"/></c:set>
                                    </c:if>
                                     <c:if test="${txnListInfo.txnName eq 'Add Card'}">
                                       <c:set var="txnName" ><spring:message code="LABEL_ADD_CARD" text="Add Card"/></c:set>
                                    </c:if>
									    <td><c:out value="${txnName}" /></td>
										<td><c:out value="${txnListInfo.alias}" /></td>
										<td><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${txnListInfo.txnDate}" /></td>
										<td><c:out value="${txnListInfo.txnAmount}" /></td>
										<td><c:out value="${txnListInfo.servCharge}" /></td>
										<td>
										<c:choose>
											<c:when test="${ txnListInfo.txnStatus == 2000 }"> 
												<spring:message code="LABEL_TXN_STATUS_SUCCESS" text="Success"/>
											</c:when>
											<c:otherwise>
												<spring:message code="LABEL_TXN_STATUS_FAILURE" text="Failure"/>
											</c:otherwise>
										</c:choose>	
										</td>
									</tr>
								</c:forEach>
                                    </tbody>
                                
                                </table>
                            </div>
                            <!-- /.box-body -->
                        </div>
                        <!-- /.box -->
                    </div>
                </div>	

        <!-- / END OF CONTENT -->

</body>
</html>
