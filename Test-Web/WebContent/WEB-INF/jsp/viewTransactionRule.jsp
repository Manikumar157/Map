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
<title><spring:message code="LABEL_TITLE" text="EOT Mobile"></spring:message></title>

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/transactionRules.js"></script>

<style>
.grayBox {
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =         80);
}

.box_content {
	position: fixed;
	top: 27%;
	left: 20%;
	right: 40%;
	width: 60%;
	padding: 16px;
	z-index: 1002;
	overflow: auto;
}

.tr_color {
	bgcolor: #d2d3f1;
}
</style>

<link href="../../css/style.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
-->
</style>
</head>
<body>


<div class="row">
		<div class="col-lg-12">
			<div class="box">

				<div class="box-header">
					<h3 class="box-title">
						<span>
						<strong><c:out
								value="${level}" /> <spring:message code="LABEL_TR_RULE"
								text="titleTRM:" /></strong></span>
					</h3>
				</div>

<form:form name="transactionRuleForm" action="saveTransactionRule.htm"
					id="transactionRuleForm" method="post" commandName="transactionRulesDTO">
					
					<jsp:include page="csrf_token.jsp" />
					<input id="pageNumber" type="hidden" name="pageNumber" value=""/>
					<div class="col-sm-5 col-sm-offset-10">
						
						<%-- <a href="javascript:submitForm('transactionRuleForm','<c:out value="listTransactionRules.htm?pageNumber=1"/>')"><strong><spring:message
								code="LINK_VIEWTRRULE" text="View Rules">
							</spring:message></strong></a> --%>
							<a href="javascript:viewTxRules('listTransactionRules.htm','1')"><strong><spring:message code="LINK_VIEWTRRULE" text="View Rules">
							</spring:message></strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					</div>
					
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
					
					<div class="box-body">


						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message code="LABEL_TXNTYPE" text="Transaction Type" /></label>
								
									<c:out value="${transactionRulesDTO.transDesc}" />
								
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message code="LABEL_TH_MAXVAL_LIMIT"
											text="Max. Value Limit" /></label>
								
									<c:out value="${transactionRulesDTO.maxValueLimit}" />
								
							</div>
						</div>
						
						
						
						
						
						
						
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message code="LABEL_APPROVAL_LIMIT"
											text="Approval Limit" /></label>
								
									<c:out value="${transactionRulesDTO.approvalLimit}" />
								
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message code="LABEL_CALCULATED_ON" text="Calculated On" /></label>
								
									<c:choose>
											<c:when test="${transactionRulesDTO.ruleType==0}">
												<spring:message code="LABEL_DEBIT" text="Debit" />
											</c:when>
											<c:otherwise>
												<spring:message code="LABEL_CREDIT" text="Credit" />
											</c:otherwise>
										</c:choose>
								
							</div>
						</div>
						
						
						
						
						<div class="box">																
						<div class="box-body table-responsive">
								<table id="example" class="table table-bordered table-striped"
									style="text-align: center;">
									<thead>
										<tr>
											<th><spring:message
											code="LABEL_TH_MAXCUMVAL_LIMIT" text="Max. Cum. ValueLimit" /></th>
											<th><spring:message code="LABEL_TH_MAX_TXNVALUE"
													text="maxTxnValue:" /></th>
											
											<th><spring:message
											code="LABEL_TH_ALLPER" text="Allowed Per" /></th>
											
											<th><spring:message
											code="LABEL_TH_ALLPER_UNIT" text="Allowed Per [Unit]" /></th>
											
											

										</tr>
									</thead>
									<tbody>
									
									<c:if
													test="${(transactionRulesDTO.transactionRuleId != null) }">
													<c:forEach items="${transactionRulesDTO.trRuleValues}"
														var="trs">
														<tr height="25px">
															<td ><c:out
																value="${trs.maxCumValueLimit}"></c:out></td>
															<td ><c:out value="${trs.maxNumTimes}"></c:out></td>
															<td ><c:out value="${trs.allowedPer}"></c:out></td>
															<td ><c:choose>
																<c:when test="${trs.allowedPerUnit==1}"><spring:message code="LABEL_SELECT_DAYS" text="Days"></spring:message></c:when>
																<c:when test="${trs.allowedPerUnit==2}"><spring:message code="LABEL_SELECT_WEEKS" text="Weeks"></spring:message></c:when>
																<c:otherwise><spring:message code="LABEL_SELECT_MONTHS" text="Months"></spring:message></c:otherwise>
															</c:choose>
															<div style="display: none"><c:out
																value="${trs.allowedPerUnit}"></c:out></div>
															</td>
														</tr>
													</c:forEach>
												</c:if>
									
									</tbody>
									
							</table>		
						</div>
					</div>	
							
				</form:form>
					
					</div>
					</div>
					

</body>
</html>
