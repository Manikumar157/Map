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
						<span><c:out value="${ level }" />
						<strong><spring:message code="LABEL_SCRULE" text="Service Charge Rule "></spring:message></strong></span>
					</h3>
				</div>
	<form:form name="serviceChargeForm" action="saveServiceCharge.htm"
					id="serviceChargeForm" method="post" commandName="serviceChargeDTO">
                  <jsp:include page="csrf_token.jsp" />
					<div class="col-sm-5 col-sm-offset-10">
						<a
							href="javascript:submitForm('serviceChargeForm','<c:out value="listServiceChargeRules.htm?pageNumber=1"/>')"><strong><spring:message
								code="LINK_VIEWSCRULE" text="Rule">
							</spring:message></strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					</div>
					<br />
					<br />
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>


					<div class="box-body">


						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_RULE_NAME" text="Rule Name:" /></label>
								
									<c:out value="${serviceChargeDTO.ruleName}" />
								
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_APPLICABLE_FROM" text="Applicable From:" /></label>
								
									<c:out value="${serviceChargeDTO.applicableFrom}" />
								
							</div>
						</div>


						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_APPLICABLE_TO" text="Applicable To:" /></label>
							
									<c:out value="${serviceChargeDTO.applicableTo}" />
								
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_TIME_ZONE" text="Time Zone:" /></label>
								
									<c:out value="${serviceChargeDTO.timeZone.timeZoneDesc}" />
								
							</div>
						</div>



						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_IMPOSED_ON" text="Imposed On:" /></label>
							
									<c:choose>
										<c:when test="${serviceChargeDTO.imposedOn==0}">
											<spring:message code="LABEL_CUSTOMER" text="customer:" />
										</c:when>
										<c:otherwise>
											<spring:message code="LABEL_SELECT_BANK_OTHER_PARTY"
												text="Other Party"></spring:message>
										</c:otherwise>
									</c:choose>
							
									<form:errors path="imposedOn" /></font>
								
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message
										code="LABEL_TRANSACTION_TYPE" text="Transaction Type"></spring:message></label>
								<div class="col-sm-5" style="margin-top: 4px;">

									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${ serviceChargeDTO.scRuleTxns }"
										var="txnType">
										<c:forEach
											items="${txnType.transactionType.transactionTypesDescs}"
											var="transactionTypesDescs">
											<c:if test="${transactionTypesDescs.comp_id.locale==lang}">
												<c:out value="${transactionTypesDescs.description}" />	-
														</c:if>
										</c:forEach>
										<c:if test="${txnType.sourceType==1}">
											<c:set var="source">
												<spring:message code="LABEL_SOURCE_WALLET" text="Wallet" />
											</c:set>
										</c:if>
										<c:if test="${txnType.sourceType==2}">
											<c:set var="source">
												<spring:message code="LABEL_SOURCE_CARD" text="Card" />
											</c:set>
										</c:if>
										<c:if test="${txnType.sourceType==3}">
											<c:set var="source">
												<spring:message code="LABEL_SOURCE_BANKACC"
													text="Bank Account" />
											</c:set>
										</c:if>
										<c:if test="${txnType.sourceType==0}">
											<c:set var="source">
												<spring:message code="LABEL_SOURCE_OTHERS" text="Others" />
											</c:set>
										</c:if>
										<c:if test="${txnType.sourceType==4}">
											<c:set var="source">
												<spring:message code="LABEL_SOURCE_FI" text="fi" />
											</c:set>
										</c:if>
										<c:out value="${source}" />
										<br />

									</c:forEach>


								            </div>
							              </div>
					                 	</div>
						</div>



						<div class="box">
							<div class="box-body table-responsive">
								<table id="example" class="table table-bordered table-striped"
									style="text-align: center;">
									<thead>
										<tr>
											<th><spring:message code="LABEL_TH_MIN_TXNVALUE"
													text="minTxnValue:" /></th>
											<th><spring:message code="LABEL_TH_MAX_TXNVALUE"
													text="maxTxnValue:" /></th>
											<th><spring:message code="LABEL_TH_SC_PERCENTAGE"
													text="SCPercentage:" /></th>
											<th><spring:message code="LABEL_TH_SC_FIXED"
													text="SCFixed:" /></th>
											<th><spring:message code="LABEL_TH_DISCOUNT_LIMIT"
													text="DiscountLimit:" /></th>
											<th><spring:message code="LABEL_TH_MIN_SC" text="MinSC:" /></th>
											<th><spring:message code="LABEL_TH_MAX_SC" text="MaxSC:" /></th>

										</tr>
									</thead>
									<tbody>



										<%
											int j = 0;
										%>
										<c:if
											test="${(serviceChargeDTO.serviceChargeRuleId != null) }">
											<c:forEach items="${serviceChargeDTO.scRuleValue}"
												var="scValue">
												<tr>
													<td><c:out
															value="${scValue.minTxnValue}"></c:out></td>
													<td ><c:out
															value="${scValue.maxTxnValue}"></c:out></td>
													<td ><c:out
															value="${scValue.serviceChargePct}"></c:out></td>
													<td ><c:out
															value="${scValue.serviceChargeFxd}"></c:out></td>
													<td ><c:out
															value="${scValue.discountLimit}"></c:out></td>
													<td ><c:out
															value="${scValue.minServiceCharge}"></c:out></td>
													<td ><c:out
															value="${scValue.maxServiceCharge}"></c:out></td>
												</tr>
											</c:forEach>

										</c:if>

 
									       </tbody>
									    </table>
									   </div>
									</div>
									
							
						

						

						<div class="box">
							<div class="box-body table-responsive">
							
							
								<table id="example2" class="table table-bordered table-striped"
									style="text-align: center;">
					<div class="box-header">
							<h3 class="box-title">
								<span><strong><spring:message
											code="HEADER_APPLICABLE_FOR_DAYS" text="Applicable For Days:"></spring:message></span>
							</h3>
						</div>
									<thead>
										<tr>
											<th><spring:message code="LABEL_TH_DAYS" text="day:" /></th>
											<th><spring:message code="LABEL_TH_FHOURS"
													text="FromHours:" /></th>
											<th><spring:message code="LABEL_TH_TOHOURS"
													text="ToHours:" /></th>


										</tr>
									</thead>
									<tbody>


										<c:if
											test="${(serviceChargeDTO.serviceChargeRuleId != null) }">
											<c:forEach items="${serviceChargeDTO.scDays}" var="sc">
												<tr>
													<c:if test="${ sc.day == 1 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_SUNDAY" text="sunday:" />
														</c:set>
													</c:if>
													<c:if test="${ sc.day == 2 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_MONDAY" text="monday:" />
														</c:set>
													</c:if>
													<c:if test="${ sc.day == 3 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_TUESDAY"
																text="tuesday:" />
														</c:set>
													</c:if>
													<c:if test="${ sc.day == 4 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_WEDNESDAY"
																text="wednesday:" />
														</c:set>
													</c:if>
													<c:if test="${ sc.day == 5 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_THURSDAY"
																text="thursday:" />
														</c:set>
													</c:if>
													<c:if test="${ sc.day == 6 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_FRIDAY" text="friday:" />
														</c:set>
													</c:if>
													<c:if test="${ sc.day == 7 }">
														<c:set var="day">
															<spring:message code="LABEL_SELECT_SATURDAY"
																text="saturday:" />
														</c:set>
													</c:if>
													<td ><c:out
															value="${day}"></c:out></td>
													<td ><c:out
															value="${sc.fromhh}"></c:out></td>
													<td ><c:out
															value="${sc.tohh}"></c:out></td>
									</div>				
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
				   </div>
				<div>
			</div>					
</body>
</html>
