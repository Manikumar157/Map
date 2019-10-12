<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<div id="csrfToken">
	<%@include file="csrf_token.jsp"%>
</div>
<c:if test="${otp eq 'otp'}">
	<div>
	<div class="col-sm-6">
		<label class="col-sm-4"><spring:message
				code="LABEL_OTPSTATUS" text="OTP" /><font color="red"> *</font></label>
		<form:input path="otp" maxlength="9" id="otp" name="otp"
			cssClass="form-control"></form:input>
			<input type="button" id="otpButton" class="btn btn-primary"
		value="<spring:message code="" text="Get OTP"/>"></input>	
	</div>
	<div class="col-sm-5 col-sm-offset-4">
		<div class="col-sm-12"
			style="color: #ba0101; font-weight: bold; font-size: 12px; margin: auto;">
			<spring:message code="${message1}" text="" />
		</div>
	</div>	
	</div>

</c:if>
<c:if test="${otp ne 'otp'}">
<c:if test="${entity eq 'profileName'}">
<b><spring:message code="LABEL_PROFILE" text="Profiles"></spring:message></b>
</c:if>
<select id="<c:out value="${entity}"></c:out>" name="<c:out value="${entity}"></c:out>" class="dropdown">
       <option value=""  selected="selected"><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
	   <!-- style="font-weight:lighter;" -->
	   <c:if test="${status eq 1}">	   
        <option value="0" ><spring:message code="LABEL_ALL" text="Select All"></spring:message></option>          
      </c:if>     
	<c:forEach items="${list}" var="ct">
		<option	 value="<c:out value="${ct[id]}"/>"><c:out value="${ct[value]}"/></option>
		<!-- style="font-weight:lighter;" -->
	</c:forEach>
</select>
</c:if>






