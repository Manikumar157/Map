<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
	<body>
	<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_TERMINAL_DETAILS" text="Terminal Details "> </spring:message></span>
		</h3>
	</div><br/>
	<form:form id="viewTerminalForm">
	<jsp:include page="csrf_token.jsp"/>
	<div class="col-sm-5 col-sm-offset-10">
		<a href="javascript:submitForm('viewTerminalForm','searchTerminal.htm')"><strong><spring:message code="LINK_SEARCH_CUSTOMERS" text="Search Terminal"> </spring:message></strong></a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	</div><br/><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_TERMINAL_ID" text="Terminal Id : "/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${terminalDTO.terminalId}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="MobileNo.:"></spring:message></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${terminalDTO.mobileNumber}" /> </div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TERMINAL_MERCHANT" text="Merchant Id :"/></label> 
					<div class="col-sm-5"><c:out value="${terminalDTO.merchantId}" /></div> 
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_OUTLET_ID" text="Outlet Id :"/></label> 
					<div class="col-sm-5"><c:out value="${terminalDTO.outletId}" />	</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ACCOUNT_NUMBER" text="Account Number :"/></label>
					<div class="col-sm-5"><c:out value="${terminalDTO.accountNumber}" /></div> 
					
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_STATUS" text="Status :"/></label> 
					<div class="col-sm-5"><c:out value="${terminalDTO.active}" /></div>
				</div>
			</div>
</div>
</form:form>
</div>
</div>	
</body>
</html>