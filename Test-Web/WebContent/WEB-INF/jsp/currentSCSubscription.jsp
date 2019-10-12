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
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bootstrap.min.css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bootstrap-theme.min.css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/font-awesome.min.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/customerServices.js"></script>



<script type="text/javascript">
		
		
</script>

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
	filter: alpha(opacity =   80);
}

.box_content {
	position: fixed;
	top: 27%;
	left: 20%;
	right: 40%;
	margin-top: -80px;
	width: 60%;
	padding: 16px;
	z-index: 1002;
	
}
  
/*---------popup box---------*/
.popup_content.col-lg-12 {
	border: 1px solid black;
	padding: 5px;
}

div#buttonId {
	margin-top: -15px;
}

.print_page.col-lg-6.col-sm-6 {
	color: #ba0101;
}

.save_page.col-lg-6.col-sm-6 {
	color: #ba0101;
	text-align: -webkit-right;
}

.print_save.col-lg-12 {
	border: 1px solid black;
}

.style2 {
	font-weight: bold
}

td {
    padding-top: .5em;
    padding-bottom: .5em;
}
.header{
    color: #30314f;
    font-family: Verdana,Arial,Helvetica,sans-serif;
    font-size: 10px;
}
</style>
<script type="text/javascript">


</script>

</head>
<body>
	<table >
		<tr>
			<td colspan="2" style="display: none"><jsp:include
					page="top.jsp" /></td>
		</tr>
		<tr>
			
			<td width="844" align="left" valign="middle">
				<table width="98%" border="0" height="400px" align="center"
					cellpadding="0" cellspacing="0">
					<tr height="20px">
						<td width="20%" align="left" valign="top" class="headding"
							style="font-size: 15px; font-weight: bold;"><spring:message
								code="CURRENT_SUBSCRIPTION" text="Current Subscription" /></td>
					</tr>
				</table>
			</td>
			<td width="844" align="left" valign="top">
				<table width="98%" border="0" height="400px" align="center"
					cellpadding="0" cellspacing="0">
					<tr>
						<td valign="top">
							<!--******************start hidden div***************-->
							<div id="transactionDiv"
								style="visibility: visible; position: relative;">

								<div id="grayBG" class="grayBox" style="display: block;"></div>
								<div width="800px" id="LightBox1" class="box_content"
									style="display: block;">
									<table cellpadding="3" height="400px;" cellspacing="0"
										width="800px" border="0">
										<tr align="left">
										
											<td colspan="2" bgcolor="#FFFFFF" style="padding: 30px;">
											<c:choose>
												<c:when test="${currentSCSubscription ne null}">
													<c:set var="customerID" value="${currentSCSubscription.customer.customerId }" scope="page" />
												</c:when>
												<c:otherwise>
													<c:set var="customerID" value="${currentSCSubscriptionList.get(0).customer.customerId }" scope="page" />
												</c:otherwise>
											</c:choose>
												<div style="cursor: pointer;" align="right" id="buttonId">
													<img src="images/delete_icon.jpg" class="cross_icon" alt=""
														width="9" height="9"
														onclick="javascript:submitForm('scSubscriptionDetails','viewCustomer.htm?customerId=${customerID}')" />
													<br />
												</div> <form:form name="scSubscriptionDetails"
													id="scSubscriptionDetails">
													<jsp:include page="csrf_token.jsp" />
													<c:choose>
														<c:when test="${currentSCSubscription ne null}">
															
															<div class="popup_content col-lg-12">
																<div class=" col-lg-12">
																	<div class=" col-lg-6 col-sm-6">
																		<label><spring:message
																				code="SUBSCRIPTION_TYPE" text="Subscription type" /></label>
																	</div>
																	<div class=" col-lg-6 col-sm-6">
																		<c:choose>
																			<c:when
																				test="${currentSCSubscription.subscriptionType==1}"><spring:message code="LABEL_DAILY" text="Daily" /></c:when>
																			<c:when
																				test="${currentSCSubscription.subscriptionType==2}"><spring:message code="LABEL_WEEKLY" text="Weekly" /></c:when>
																			<c:when
																				test="${currentSCSubscription.subscriptionType==3}"><spring:message code="LABEL_MONTHLY" text="Monthly" /></c:when>
																			<c:otherwise><spring:message code="LABEL_YEARLY" text="Yearly" /></c:otherwise>
																		</c:choose>
																		<div style="display: none">
																			<p>
																				<c:out
																					value="${currentSCSubscription.subscriptionType}"></c:out>
																			</p>
																		</div>
																		<br />
																	</div>
																</div>

																<div class="  col-lg-12">
																	<div class="col-lg-6 col-sm-6">
																		<label><spring:message
																				code="SUBSCRIPTION_START_DATE"
																				text="Subscription Start Dt." /></label>
																	</div>
																	<div class="col-lg-6 col-sm-6">
																		<p>
																			<fmt:formatDate pattern="yyyy-MM-dd"
																				value="${currentSCSubscription.subscriptionStartDate}"></fmt:formatDate>
																		</p>
																	</div>
																</div>
																<div class="col-lg-12">
																	<div class="col-lg-6 col-sm-6">
																		<label><spring:message
																				code="SUBSCRIPTION_END_DATE"
																				text="Subscription End Dt." /></label>
																	</div>
																	<div class="col-lg-6 col-sm-6">
																		<p>
																			<fmt:formatDate pattern="yyyy-MM-dd"
																				value="${currentSCSubscription.subscriptionEndDate}"></fmt:formatDate>
																		</p>
																	</div>
																</div>
																<div class="col-lg-12">
																	<div class="col-lg-6 col-sm-6">
																		<label><spring:message
																				code="NUMBER_OF_SUBSCRIBED_TXN"
																				text="No. of Subscribed Txn" /></label>
																	</div>
																	<div class="col-lg-6 col-sm-6">
																		<p>
																			<c:out
																				value="${currentSCSubscription.noofSubscribedTxn}"></c:out>
																		</p>
																	</div>
																</div>
																<div class=" col-lg-12">
																	<div class="col-lg-6 col-sm-6">
																		<label><spring:message
																				code="NUMBER_OF_CONSUMED_TXN"
																				text="No. of Consumed Txn" /></label>
																	</div>
																	<div class="col-lg-6 col-sm-6">
																		<p>
																			<c:out
																				value="${currentSCSubscription.noofConsumedTxn}"></c:out>
																		</p>
																	</div>
																</div>


																<div class=" col-lg-12">
																	<div class="col-lg-6 col-sm-6">
																		<label><spring:message
																				code="LABEL_STATUS" text="Status:" /></label>
																	</div>
																	<div class="col-lg-6 col-sm-6">
																		<c:choose>
																			<c:when test="${currentSCSubscription.status==10}"><spring:message code="LABEL_ACTIVE" text="Active" /></c:when>
																			<c:otherwise><spring:message code="LABEL_INACTIVE" text="Inactive" /></c:otherwise>
																		</c:choose>
																		<div style="display: none">
																			<p>
																				<c:out value="${currentSCSubscription.status}"></c:out>
																			</p>
																		</div>
																	</div>

																</div>
															</div>
														</c:when>
														<c:otherwise>
															<table id="example" style="width: 100%;" cellspacing="0"
																cellpadding="0">
																<thead>
																	<tr bgColor="#d2d3f1">
																		<th width="8%" height="25px" class="header"><spring:message
																				code="SUBSCRIPTION_TYPE" text="Subscription type" /></th>
																		<th width="10.5%" height="25px" class="header"><spring:message
																				code="SUBSCRIPTION_END_DATE"
																				text="Subscription End Dt." /></th>
																		<th width="11.5%" height="25px" class="header"><spring:message
																				code="SUBSCRIPTION_START_DATE"
																				text="Subscription Start Dt." /></th>
																		<th width="10.5%" height="25px" class="header"><spring:message
																				code="NUMBER_OF_SUBSCRIBED_TXN"
																				text="No. of Subscribed Txn" /></th>
																		<th width="10.5%" height="25px" class="header"><spring:message
																				code="NUMBER_OF_CONSUMED_TXN"
																				text="No. of Consumed Txn" /></th>
																		<th width="5%" height="25px" class="header"><spring:message
																				code="LABEL_STATUS" text="Status:" /></th>
																	</tr>
																</thead>



																

																	<td colspan="8" align="center">
																		<div
																			style="height: 300px; overflow: scroll; border: 1px solid;">
																			<table id="tblGrid" width="100%" border="0"
																				cellspacing="0" cellpadding="0">

																				<tbody>
																					<c:forEach items="${currentSCSubscriptionList }"
																						var="currentSCSubscriptionList">
																						<tr style="border-bottom: 1px solid #efecef;">
																						<td align="center" width="8%"><c:choose>
																							<c:when
																								test="${currentSCSubscriptionList.subscriptionType==1}"><spring:message code="LABEL_DAILY" text="Daily" /></c:when>
																							<c:when
																								test="${currentSCSubscriptionList.subscriptionType==2}"><spring:message code="LABEL_WEEKLY" text="Weekly" /></c:when>
																							<c:when
																								test="${currentSCSubscriptionList.subscriptionType==3}"><spring:message code="LABEL_MONTHLY" text="Monthly" /></c:when>
																							<c:otherwise><spring:message code="LABEL_YEARLY" text="Yearly" /></c:otherwise>
																						</c:choose>
																						<div style="display: none">
																							
																								<c:out
																									value="${currentSCSubscriptionList.subscriptionType}"></c:out>
																						</div>
																						</td>
																						<td align="center" width="11%">
																								<fmt:formatDate pattern="yyyy-MM-dd" value="${currentSCSubscriptionList.subscriptionEndDate}"/>
																						</td>
																						
																						<td align="center" width="11.5%">
																						
            
																						<fmt:formatDate pattern="yyyy-MM-dd" value="${currentSCSubscriptionList.subscriptionStartDate}"/>
																						</td>
																						
																						<td align="center" width="10.5%"><c:out
																								value="${currentSCSubscriptionList.noofSubscribedTxn}"></c:out>
																						</td>
																						<td align="center" width="10.5%"><c:out
																								value="${currentSCSubscriptionList.noofConsumedTxn}"></c:out>
																						</td>
																						<td align="center" width="5%">
																						<c:choose>
																								<c:when test="${currentSCSubscriptionList.status==10}"><spring:message code="LABEL_ACTIVE" text="Active" /></c:when>
																								<c:otherwise><spring:message code="LABEL_INACTIVE" text="Inactive" /></c:otherwise>
																						</c:choose>
																						
																						</td>

																						</tr>

																					</c:forEach>


																				</tbody>
																			</table>
																		</div>
																	</td>
																</tr>
															</table>
														</c:otherwise>
													</c:choose>
												</form:form> <!--******************end hidden div***************-->
											</td>
										</tr>
									</table>
								</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
		</tr>
	</table>
</body>
</html>
