<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<c:forEach items="${selectedList}" var="list">
	<c:choose>
		<c:when test="${list eq 1}">
			<c:set var="s1" value="${list}"></c:set>
		</c:when>
		<c:when test="${list eq 2}">
			<c:set var="s2" value="${list}"></c:set>
		</c:when>
		<c:when test="${list eq 3}">
			<c:set var="s3" value="${list}"></c:set>
		</c:when>
	</c:choose>	
</c:forEach>

<input type="checkbox" name="sourceType" value="1" id="sourceType<c:if test="${s1 eq 1}">${s1}</c:if>"   onclick="checkBox(this.id);"/><spring:message code="LABEL_WALLET" text="Wallet"/>
<input type="checkbox" name="sourceType" value="2" id="sourceType<c:if test="${s2 eq 2}">${s2}</c:if>"   onclick="checkBox(this.id);"/><spring:message code="LABEL_CARD" text="Card"/>
<input type="checkbox" name="sourceType" value="3" id="sourceType<c:if test="${s3 eq 3}">${s3}</c:if>"  onclick="checkBox(this.id);"/><spring:message code="LABEL_BANKACCOUNT" text="Bank Account"/>
