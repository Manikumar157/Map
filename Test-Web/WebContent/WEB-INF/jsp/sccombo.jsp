<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<select id="<c:out value="${entity}"></c:out>" name="<c:out value="${entity}"></c:out>" class="dropdown_small">
	<option value="" selected="selected"><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
	<c:forEach items="${list}" var="ct">
		<option	value="<c:out value="${ct[id]}"/>"><c:out value="${ct[value]}"/></option>
	</c:forEach>
</select>






