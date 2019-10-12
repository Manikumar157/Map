<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <jsp:include page="/WEB-INF/jsp/layout/common.jsp"/> --%>
<div id="data" class="box-body table-responsive">
<%-- <form:form id="viewTxnsForm">
	<jsp:include page="csrf_token.jsp"/> --%>

        <c:set value="${(page.currentPage-1)*10}" var="j"></c:set> 
        
        <table id="example1" class="table table-bordered table-hover" style="text-align:center;">
              <thead> 
              <c:if test="${message ne null}">
              <tr>
				<td colspan="9" style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
					<spring:message code="${message}" text=""/>
				</td>
			  </tr>
			  </c:if>
              <tr>   
                <th><spring:message code="LABEL_TXNID" text="Transaction ID"/></th>          
                <th ><spring:message code="LABEL_TRANSACTION_TYPE" text="Txn Type"/></th>
                <th><spring:message code="LABEL_ALIAS" text="Acc. Alias"/></th>
                <th><spring:message code="LABEL_TXNDATE" text="Date"/></th>
                <th><spring:message code="LABEL_TXN_AMOUNT" text="Amount"/></th>    
                <th><spring:message code="LABEL_TYPE" text="Type(D/C)"/></th>
                <th><spring:message code="LABEL_SERV_CHARGE" text="Serv. Charge"/></th>    
                <th><spring:message code="LABEL_TXN_STATUS" text="Status"/></th> 
                </tr>          
              </thead>
              <tbody>
              <c:forEach items="${page.results}" var="txj">
              <%-- <c:set var="j" value="${j + 1}" scope="page"/> --%>
              <c:set var="j" value="${(page.currentPage - 1) * page.resultsPerPage }"></c:set>
 			  <tr <c:if test="${j%2==0}"> bgcolor="white" </c:if> 
 			  <c:if test="${j%2!=0}"> bgcolor="#f3f3f3" </c:if>>
			 	    
			 	<td align="left" height="23px"><c:out value="${txj.transactionID}" /></td>
			 	
				<td align="left">
					<c:choose>
						<c:when test="${txj.transactionType==10}"><spring:message code="LABEL_TXNTYPE_ACTIVATION" text="Activation" /></c:when>
						<c:when test="${txj.transactionType==15}"><spring:message code="LABEL_TXNTYPE_CHANGE_PIN" text="Login Pin Change" /></c:when>
						<c:when test="${txj.transactionType==20}"><spring:message code="LABEL_TXNTYPE_TXN_PIN_CHANGE" text="Txn Pin Change" /></c:when>
						<c:when test="${txj.transactionType==25}"><spring:message code="LABEL_TXNTYPE_PROFILE_UPDATE" text="Profile Update" /></c:when>
						<c:when test="${txj.transactionType==30}"><spring:message code="LABEL_TXNTYPE_BAL_ENQ" text="Balance Enquiry" /> </c:when>
						<c:when test="${txj.transactionType==35}"><spring:message code="LABEL_TXNTYPE_MINI_STMT" text="Mini Statement" /></c:when>
						<c:when test="${txj.transactionType==55}"><spring:message code="LABEL_TXNTYPE_SEND_MONEY" text="Send Money" /></c:when>
						<c:when test="${txj.transactionType==60}"><spring:message code="LABEL_REVERSAL_LEFT" text="Reversal" /></c:when>
						<c:when test="${txj.transactionType==61}"><spring:message code="LABEL_REVERSAL_LEFT" text="Reversal" /></c:when>
						<c:when test="${txj.transactionType==65}"><spring:message code="LABEL_TXNTYPE_REACTIVATION" text="Re-Activation" /></c:when>
						<c:when test="${txj.transactionType==70}"><spring:message code="LABEL_TXNTYPE_RESET_PIN" text="Reset Pin" /></c:when>
						<c:when test="${txj.transactionType==75}"><spring:message code="LABEL_TXNTYPE_RESET_TXN_PIN" text="Reset Txn Pin" /></c:when>
						<c:when test="${txj.transactionType==80}"><spring:message code="LABEL_TXNTYPE_TOPUP" text="Buy Airtime" /></c:when>
						<c:when test="${txj.transactionType==81}"><spring:message code="LABEL_TXNTYPE_BILL_PRESENTMENT" text="Bill Presentment" /></c:when>
						<c:when test="${txj.transactionType==82}"><spring:message code="LABEL_TXNTYPE_BILL_PAYMENT" text="Bill Payment" /></c:when>
						<c:when test="${txj.transactionType==83}"><spring:message code="LABEL_TXNTYPE_SMSCASH" text="SMS Cash" /></c:when>
						<c:when test="${txj.transactionType==90}"><spring:message code="LABEL_TXNTYPE_SALE" text="Sale" /></c:when>	
						<c:when test="${txj.transactionType==95}"><spring:message code="LABEL_TXNTYPE_DEPOSIT" text="Deposit" /></c:when>	
						<c:when test="${txj.transactionType==98}"><spring:message code="LABEL_TXNTYPE_TXN_STMT" text="TxnStatement" /></c:when>	
						<c:when test="${txj.transactionType==99}"><spring:message code="LABEL_TXNTYPE_WITHDRAWAL" text="Withdrawal" /></c:when>
						<c:when test="${txj.transactionType==84}"><spring:message code="LABEL_SMS_CASH_OTHERS" text="SMS Cash Others" /></c:when>
						<c:when test="${txj.transactionType==37}"><spring:message code="LABEL_ADD_BANK_ACCOUNT" text="Add Bank Account" /></c:when>
						<c:when test="${txj.transactionType==40}"><spring:message code="LABEL_ADD_CARD" text="Add Card" /></c:when>	
						<c:when test="${txj.transactionType==31}"><spring:message code="LABEL_TXNTYPE_ADD_PAYEE" text="Add Payee" /></c:when>	
						<c:when test="${txj.transactionType==115}"><spring:message code="LABEL_AGENT_DEPOSIT" text="Cash Deposit" /></c:when>
						<c:when test="${txj.transactionType==116}"><spring:message code="LABEL_AGENT_WITHDRAWAL" text="Cash Withdrawal" /></c:when> 
						<c:when test="${txj.transactionType==126}"><spring:message code="LABEL_SMS_CASH_RECEIV" text="SMS Cash Receive" /></c:when>
						<c:when test="${txj.transactionType==128}"><spring:message code="LABEL_PAY" text="m-GURUSH Pay" /></c:when>
						<c:when test="${txj.transactionType==120}"><spring:message code="LABEL_COMMISSION_SAHRE" text="Commission Share" /></c:when> 
						<c:when test="${txj.transactionType==133}"><spring:message code="LABEL_FLOAT_MANAGEMENT" text="Transfer Float" /></c:when>
						<c:when test="${txj.transactionType==140}"><spring:message code="LABEL_MERCHANT_PAY_OUT" text="Merchant Payout" /></c:when> 
						<c:when test="${txj.transactionType==135}"><spring:message code="LABEL_CUST_APPROVAL_COMM" text="Customer Approval Commission" /></c:when>			
						<c:when test="${txj.transactionType==137}"><spring:message code="LABEL_CASH_IN" text="Cash In" /></c:when>
						<c:when test="${txj.transactionType==138}"><spring:message code="LABEL_CASH_OUT" text="Cash Out" /></c:when>
						<c:when test="${txj.transactionType==141}"><spring:message code="LABEL_BULK_PAYMENT" text="Bulk Payment" /></c:when>
						<c:when test="${txj.transactionType==121}"><spring:message code="LABEL_LIMIT" text="Limit" /></c:when>
						<c:when test="${txj.transactionType==146}"><spring:message code="LABEL_PAY_MERCHNAT" text="Pay Merchant" /></c:when>
						 																							
					</c:choose>
				</td>
				<td align="left">
				<c:choose>
					<c:when test="${ txj.cusType == 1 }"> 
						<c:out value="${txj.alias}" />
					</c:when>
					<c:otherwise>
						<spring:message code="LABEL_GIM_CARD" text="GIM-Card"/>
					</c:otherwise>
					</c:choose>		
				
				</td>
				<td align="left"><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${txj.transactionDate}" /></td>
				<td align="right"><%--  <c:out value="${ txj.transactionAmount }"></c:out> --%>
				 <fmt:formatNumber type = "number" maxFractionDigits = "2" minFractionDigits = "2" value = "${txj.transactionAmount}" />
				</td>
			
				<td align="right"><c:out value="${txj.type}" ></c:out></td>
				<td align="right">
				 <%-- <c:out value="${ txj.serviceCharge }"></c:out> --%>
				 <fmt:formatNumber pattern="#.##" type = "number" maxFractionDigits = "2" minFractionDigits = "2" value = "${txj.serviceCharge}" />        
				 </td>
				<td align="right">
					<c:choose>
					<c:when test="${ txj.status == 2000 }"> 
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
		<%-- 	<tr  bgcolor="#30314f" style="color:white;"> 
	 			<c:if test="${page.totalPages>1}">
	            <td align="right" height="20px" colspan="8">
	            <a	<c:if test="${page.currentPage!='1'}"> href="#" onClick="viewTransactions('${ customerId }' ,1)"  </c:if> style="color:white;"><c:out value="[ First " /></a>
				<a	<c:if test="${page.currentPage!='1'}">href="#" onClick="viewTransactions('${ customerId }' ,<c:out value="${page.currentPage-1}" />)" </c:if> style="color:white;"><c:out value=" / Prev ]"/></a> 
				<c:forEach var="i" begin="${page.startPage}" end="${page.endPage}" step="1">
				<c:if test="${page.currentPage!=i}"> <a href="#" onClick="viewTransactions('${ customerId }' ,<c:out value="${i}"/>)" style="color:white;"><c:out value="${i}" /></a></c:if>
				<c:if test="${page.currentPage==i}"><b><c:out value="${i}" /></b></c:if>
				</c:forEach> 
				<a <c:if test="${page.currentPage<page.totalPages}">href="#" onClick="viewTransactions('${ customerId }' ,<c:out value="${page.currentPage+1}" />)"</c:if> style="color:white;"><c:out value="[ Next / " /></a> 
				<a <c:if test="${page.totalPages!=page.currentPage}">href="#" onClick="viewTransactions('${ customerId }' ,<c:out value="${page.totalPages}" />)"</c:if> style="color:white;"><c:out value="Last ]" /></a>
	            </td>
	            </c:if>
	 		</tr> --%>
       </table> 
<%-- </form:form> --%>
</div>
	
    <%--   <jsp:include page="/WEB-INF/jsp/layout/commonScript.jsp"/>   --%>
