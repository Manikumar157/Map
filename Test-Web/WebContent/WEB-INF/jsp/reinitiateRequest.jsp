<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<table align="center" style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7" align="center" width="60%" cellpadding="4" cellspacing="4">
	
	<tr>
		<td style="color: #ba0101;font-weight: bold;font-size: 12px;" align="center">
			<spring:message code="${message}" text=""/>
		</td>
	</tr>
	<c:if test="${ response != null }">
		<tr>
			<td align="left"><b><spring:message code="LABEL_TXN_TYPE" text="Transaction Type"/> </td>
			<td><c:out value="${ response.request.transactionType.description }" ></c:out></td>
		</tr>
		<tr>
			<td><b><spring:message code="LABEL_TRANSACTION_DATE" text="Transaction Date"/> </td>
			<td><c:out value="${ response.request.transmissionTime }" ></c:out></td>
		</tr>
		<tr>
			<td><b><spring:message code="LABEL_STATUS" text="Status"/> </td>
			<td align="right">
				<c:choose>
					<c:when test="${ response.status == 0 }"> 
						<spring:message code="LABEL_TXN_STATUS_SUCCESS" text="Success"/>
					</c:when>
					<c:otherwise>
						<spring:message code="LABEL_TXN_STATUS_FAILURE" text="Failure"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td><b><spring:message code="LABEL_TRANSACTION_RESP" text="Transaction Response"/> </td>
			<td><c:out value="${ response.message }" ></c:out></td>
		</tr>
	</c:if>
</table>


