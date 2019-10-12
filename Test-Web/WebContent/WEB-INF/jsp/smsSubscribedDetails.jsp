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
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css"></link>
          <link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap-theme.min.css"></link>
          <link rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.min.css"></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/customerServices.js"></script>



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
	left: 30%;
	right: 40%;
	width: 60%;
	padding: 16px;
	z-index: 1002;
	overflow: auto;
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
</style>
<script type="text/javascript">


</script>

</head>
<body>
<table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td colspan="2" style="display:none"><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td width="159" valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
				</tr>
			</table>
		</td>
		<td width="844" align="left" valign="middle">
			<table width="98%" border="0" height="400px" align="center" cellpadding="0" cellspacing="0">
						<tr height="20px">
							<td width="20%" align="left" valign="top" class="headding"
								style="font-size: 15px; font-weight: bold;"><spring:message
								code="CURRENT_SUBSCRIPTION"
								text="Current Subscription" /></td>
						</tr>
		</table></td>
		<td width="844" align="left" valign="top">
			<table width="98%" border="0" height="400px" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="top">
						<!--******************start hidden div***************-->
						<div id="transactionDiv" style="visibility: visible; position: relative;">
	
						<div id="grayBG" class="grayBox" style="display: block;"></div>
						<div width="800px" id="LightBox1" class="box_content" style="display: block;">
							<table cellpadding="3" height="264px;" cellspacing="0" width="80%" border="0">
								<tr align="left">
									<td colspan="2" bgcolor="#FFFFFF" style="padding: 10px;">
									<div
										style="cursor: pointer;" align="right" id="buttonId"><img
										src="images/delete_icon.jpg" class="cross_icon" alt="" width="9" height="9" onclick="javascript:submitForm('smsSubscriptionDetails','viewCustomer.htm?customerId=<c:out value="${currentSubscription.customer.customerId}')"/>" />
									<br />
									</div>
									<form:form name="smsSubscriptionDetails" id="smsSubscriptionDetails">
									<jsp:include page="csrf_token.jsp"/>
									<div class="popup_content col-lg-12">
										<div class=" mob_no. col-lg-12">
											<div class=" mobile_no. col-lg-6 col-sm-6">
												<label><spring:message code="LABEL_SUBSCRIPTION_TYPE" text="Subscription type" /></label>
											</div>
											<div class=" mobile_no.col-lg-6 col-sm-6">
										<c:choose> 
										<c:when test="${currentSubscription.subscriptionType==1}"><spring:message code="LABEL_DAILY" text="Daily" /></c:when>
										<c:when test="${currentSubscription.subscriptionType==2}"><spring:message code="LABEL_WEEKLY" text="Weekly" /></c:when>
										<c:when test="${currentSubscription.subscriptionType==3}"><spring:message code="LABEL_MONTHLY" text="Monthly" /></c:when>
										<c:otherwise><spring:message code="LABEL_YEARLY" text="Yearly" /></c:otherwise>
										</c:choose><div style="display: none"><p><c:out value="${currentSubscription.subscriptionType}"></c:out></p>
											</div><br/>
										</div></div>
										<div class=" Subscription Name col-lg-12">
											<div class="col-lg-6 col-sm-6">
												<label><spring:message code="LABEL_SUBSCRIPTION_NAME"  text="Subscription Name" /></label>
											</div>
											<div class="col-lg-6 col-sm-6">
												<p><c:out value="${currentSubscription.smsalertrule.smsAlertRuleName}"></c:out></p>
											</div>
										</div>	
										<div class=" Start Date col-lg-12">
											<div class="col-lg-6 col-sm-6">
												<label><spring:message
										        code="SUBSCRIPTION_START_DATE"
													text="Subscription Start Dt." /></label>
											</div>
											<div class="col-lg-6 col-sm-6">
												<p><fmt:formatDate pattern="yyyy-MM-dd" value="${currentSubscription.subscriptionStartDate}"></fmt:formatDate></p>
											</div>
										</div>	
										<div class=" End Date col-lg-12">
											<div class="col-lg-6 col-sm-6">
												<label><spring:message
													code="SUBSCRIPTION_END_DATE"
													text="Subscription End Dt." /></label>
											</div>
											<div class="col-lg-6 col-sm-6">
												<p><fmt:formatDate pattern="yyyy-MM-dd" value="${currentSubscription.subscriptionEndDate}"></fmt:formatDate></p>
											</div>
										</div>
										<div class="SMS col-lg-12">
											<div class="col-lg-6 col-sm-6">
												<label><spring:message code="LABEL_NUMBER_OF_SMS" text="No. of SMS" /></label>
											</div>
											<div class="col-lg-6 col-sm-6">
												<p><c:out value="${currentSubscription.noofSubscribedSms}"></c:out></p>
											</div>
										</div>	
										<div class=" Sms Used col-lg-12">
											<div class="col-lg-6 col-sm-6">
												<label><spring:message code="LABEL_NUMBER_OF_SMS_USED" text="No. of SMS Used" /></label>
											</div>
											<div class="col-lg-6 col-sm-6">
											<p>	<c:out value="${currentSubscription.noofSmsUsed}"></c:out></p>
											</div>
										</div>	
										
											
										<div class=" des_details col-lg-12">
											<div class="col-lg-6 col-sm-6">
												<label><spring:message
													 code="LABEL_STATUS" text="Status:" /></label>
											</div>
											<div class="col-lg-6 col-sm-6">
											<c:choose>
											<c:when test="${currentSubscription.status==10}"><spring:message code="LABEL_ACTIVE" text="Active" /></c:when>
											<c:otherwise><spring:message code="LABEL_INACTIVE" text="Inactive" /></c:otherwise>
											</c:choose>
											<div style="display: none"><p>	<c:out value="${currentSubscription.status}"></c:out></p>
											</div>
										</div>	
																					
									</div></div></form:form>
									<!--******************end hidden div***************-->
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
