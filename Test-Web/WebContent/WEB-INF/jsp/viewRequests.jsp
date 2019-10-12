<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<style>
tr th {
	text-align: center;
}
</style>
<jsp:include page="/WEB-INF/jsp/layout/common.jsp"/>

<form:form class="form-inline" role="form">
        <c:set value="${(page.currentPage-1)*10}" var="j"></c:set> 
        
         <table id="example1" class="table table-bordered table-hover" style="text-align:center;">
               <thead> 
               <tr>
				<td colspan="9" style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
					<spring:message code="${message}" text=""/>
				</td>
			  </tr>
               <tr>    
                <th><spring:message code="LABEL_SLNO" text="Serial Num"/></th>          
                <th><spring:message code="LABEL_TXN_TYPE" text="Type"/></th>
                <th><spring:message code="LABEL_TXNDATE" text="Date"/></th>
                <th><spring:message code="LABEL_TXN_STATUS" text="Action"/></th> 
                <th><spring:message code="LABEL_ACTION" text="Action"/></th> 
              </tr>
              </thead>
              <tbody>
              <c:forEach items="${page.results}" var="txn">
 			  <tr <c:if test="${j%2==0}"> bgcolor="white" </c:if> <c:if test="${j%2!=0}"> bgcolor="#d2d3f1" </c:if>>
			 	<c:set var="j" value="${j + 1}" scope="page"/>    
			 	<td align="center" height="23px"><c:out value="${j}" /></td>
				<td align="left"><c:out value="${txn.transactionType.description}" /></td>
				<td align="center"><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${txn.transactionTime}" /></td>
				<td align="right">
					<c:choose>
					<c:when test="${ txn.status == 1 }"> 
						<spring:message code="LABEL_TXN_STATUS_SUCCESS" text="Success"/>
					</c:when>
					<c:otherwise>
						<spring:message code="LABEL_TXN_STATUS_FAILURE" text="Failure"/>
					</c:otherwise>
					</c:choose>
				</td>
				<td align="right">
					<c:if test="${ txn.status != 1 && ( txn.transactionType.transactionType == 55 || txn.transactionType.transactionType == 80 ) }">
						<a href="#" onClick="reinitiateRequest(<c:out value="${txn.requestId}" />)"><spring:message code="LABEL_REINITIATE" text="Re-Initiate"/></a>
					</c:if>
				</td>
			  </tr>
			</c:forEach>
			</tbody>
		
       </table> 
       </form:form>
       
 <jsp:include page="/WEB-INF/jsp/layout/commonScript.jsp"/> 