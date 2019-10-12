<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<%-- <title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title> --%>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<%-- <script type="text/javascript"
	src="<%=request.getContextPath()%>/js/webUser.js"></script> --%>

<script type="text/javascript">
	
	function searchSubmit() {
		if (document.getElementById("clearingHouseName").value.length <= 0
				&& document.getElementById("currency").value.length <= 0
				&& document.getElementById("guaranteeAccount").value.length <= 0
				&& document.getElementById("centralBankAccount").value.length <= 0) {
			alert("Please Enter any Search Criteria Key");
			return false;
		} else {
			document.clearingHouseManagement.method = "post";
			document.clearingHouseManagement.action = "searchClearingHouse.htm";
			document.clearingHouseManagement.submit();
		}
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	function clearingHouseDetail(url,clearingHouseId){
		document.getElementById('clearingPoolId').value=clearingHouseId;
		submitlink(url,'clearingHouseManagement');
	}
	//@End
</script>

</head>

<body onload="check()">

	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_CH_MGMT"
							text="Clearing Houses" /></span>
				</h3>
			</div>
			<div class="col-md-3 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
			<form:form name="clearingHouseManagement" id="clearingHouseManagement" method="post"
				commandName="clearingHouseDTO" class="form-inline">
				<jsp:include page="csrf_token.jsp"/>
				<div class="col-md-3 col-md-offset-10">
					<authz:authorize ifAnyGranted="ROLE_addClearingHouseAdminActivityAdmin">
					<%-- 	<a href="javascript:submitForm('clearingHouseManagement','addClearanceHouseForm.htm')" onclick="removeMsg()"><strong><spring:message
									code="LABEL_ADD_CLEARING_HOUSE" text="Add Clearing House"></spring:message></strong></a> --%>				
				 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_CLEARING_HOUSE" text="Add Clearing House"/>"
							onclick="javascript:submitForm('clearingHouseManagement','addClearanceHouseForm.htm')" /> 
				
 					</authz:authorize>
				</div>
				<br />
				<br />
				<div class="box-body">
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-5" style="margin-top: 4px;"><spring:message
									code="LABEL_CH_NAME" text="Clearing House Name" /></label>
							<form:input path="clearingHouseName" id="clearingHouseName"
								cssClass="form-control" maxlength="32" />
						</div>
					<!-- 	@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
						<form:hidden path="clearingPoolId" />
						<!--@End  -->
						<div class="col-sm-6">
							<label class="col-sm-5"> <spring:message
									code="LABEL_CURRENCY" text="Currency" /></label>
							<form:select path="currency" cssClass="dropdown chosen-select" id="currency">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${currencyList}" itemLabel="currencyName"
									itemValue="currencyId" />

							</form:select>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-5" style="margin-top: 4px;"> <spring:message
									code="LABEL_GUARANTEE_ACCOUNT" text="Guarantee Account" /></label>
							<form:input path="guaranteeAccount" cssClass="form-control"
								maxlength="12" id="guaranteeAccount" />
						</div>
						<div class="col-sm-6">
							<label class="col-sm-5" style="margin-top: 4px;"> <spring:message
									code="LABEL_CENTRAL_BANK_ACCOUNT" text="CentralBank Account" /></label>
							<form:input path="centralBankAccount" cssClass="form-control"
								maxlength="12" id="centralBankAccount" />
						</div>
					</div>
					<br />
					<div class="box-footer">
						<input type="button"
							value="<spring:message code="LABEL_SEARCH" text="Search"/>"
							onclick="searchSubmit();" class="btn btn-primary pull-right" style="margin-right: 60px;"></input>
					</div>
					<br /> <br />
				</div>
			</form:form>
		</div>
		<div class="box">
			<div class="box-body table-responsive">
				<table id="example1" class="table table-bordered table-striped"
					style="text-align: center;">
					<thead>
						<tr>
							<th><spring:message code="LABEL_CH_NAME"
									text="Clearing House Name" /></th>
							<th><spring:message code="LABEL_CONTACT_PERSON"
									text="ContactPerson" /></th>
							<th><spring:message code="LABEL_MOBILE_NO"
									text="MobileNumber" /></th>
							<th><spring:message code="LABEL_CURRENCY" text="Currency" /></th>
							<th><spring:message code="LABEL_MESSAGE_TYPE"
									text="Message Type" /></th>
							<th><spring:message code="LABEL_STATUS" text="Status" /></th>
							<authz:authorize
								ifAnyGranted="ROLE_editClearingHouseAdminActivityAdmin">
								<th align="center"><spring:message code="LABEL_ACTION_EDIT"
										text="Action" /></th>
							</authz:authorize>
							<authz:authorize
								ifNotGranted="ROLE_gimsupportlead,ROLE_gimsupportexec">
								<th align="center"><spring:message code="LABEL_REPORT"
										text="File" /></th>
							</authz:authorize>
						</tr>
					</thead>
					<tbody>

						<c:forEach items="${page.results}" var="clearingHousePool">
							<tr>

								<td><c:out value="${clearingHousePool.clearingPoolName}" /></td>
								<td><c:out value="${clearingHousePool.contactPerson}" /></td>
								<td><c:out value="${clearingHousePool.mobileNumber}" /></td>
								<td><c:out
										value="${clearingHousePool.currency.currencyName}" /></td>
								<td><c:out value="${clearingHousePool.messageType}" /></td>
								<c:if test="${clearingHousePool.status==1}">
									<c:set var="status">
										<spring:message code="LABEL_ACTIVE" text="Active" />
									</c:set>
								</c:if>
								<c:if test="${clearingHousePool.status==0}">
									<c:set var="status">
										<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
									</c:set>
								</c:if>
								<td><c:out value="${status}" /></td>
								<authz:authorize
									ifAnyGranted="ROLE_editClearingHouseAdminActivityAdmin">
									<td align="center">
									
										<!-- 	@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
									<%-- <a
										href="javascript:submitForm('clearingHouseManagement','editClearanceHouse.htm?clearingPoolId=<c:out value="${clearingHousePool.clearingPoolId}"/>')" />
										<spring:message code="LABEL_CH_EDIT" text="Edit" /> --%>
										<a
										href="javascript:clearingHouseDetail('editClearanceHouse.htm','<c:out value="${clearingHousePool.clearingPoolId}"/>')" />
										<spring:message code="LABEL_CH_EDIT" text="Edit" />
										
										</td>
								</authz:authorize>
								<authz:authorize
									ifNotGranted="ROLE_gimsupportlead,ROLE_gimsupportexec">
									<td><%-- <a
										href="javascript:submitForm('clearingHouseManagement','viewClearingHousePool.htm?clearingPoolId=<c:out value="${clearingHousePool.clearingPoolId}"/>')" />
										<spring:message code="LABEL_SETTLEMENT" text="Settlement" /> --%>
										<a
										href="javascript:clearingHouseDetail('viewClearingHousePool.htm','<c:out value="${clearingHousePool.clearingPoolId}"/>')" />
										<spring:message code="LABEL_SETTLEMENT" text="Settlement" />
										<!-- End -->
										</td>
								</authz:authorize>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

	</div>
</body>
</html>
