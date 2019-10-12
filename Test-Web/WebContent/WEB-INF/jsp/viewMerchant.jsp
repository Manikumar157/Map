<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>

<html>
	<head>
		<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> --%>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/customerServices.js"></script>
		<script type="text/javascript">
		
		var Alertmsg={"ALERT_BLOCK_APP":"<spring:message code='ALERT_BLOCK_APP' text='Do you want to block the application &#63;'/>",
				"ALERT_UNBLOCK_APP":"<spring:message code='ALERT_UNBLOCK_APP' text='Do you want to unblock the application &#63;'/>",
				 "ALERT_ACTIVATE_CUSTOMER":"<spring:message code='ALERT_ACTIVATE_CUSTOMER' text='Do you want to activate the customer &#63;'/>",
				 "ALERT_DEACTIVATE_CUSTOMER":"<spring:message code='ALERT_DEACTIVATE_CUSTOMER' text='Do you want to de-activate the customer &#63;'/>",
				 "ALERT_RESET_CUST_PIN" : "<spring:message code='ALERT_RESET_CUST_PIN' text='Do you want to reset the customer PIN &#63;'/>",
				 "ALERT_RESCHEDULE_SMS" : "<spring:message code='ALERT_RESCHEDULE_SMS' text='Do you want to re-send the SMS to the customer &#63;'/>",
				 "ALERT_REINIT_REQUEST" : "<spring:message code='ALERT_REINIT_REQUEST' text='Do you want to re-initiate the request &#63;'/>",
				 "ALERT_REINSTALL_APP" : "<spring:message code='ALERT_REINSTALL_APP' text='Do you want to re-install the application &#63; (This will disable the existing application.)'/>"
				 };	
			 
			 setTimeout("showHideDiv()", 600000);// 10 minutes
			 
			 function showHideDiv() {
			        var link = document.getElementById('link');
			        link.style.display = "none";
			}
		
		</script>
		
		<style type="text/css">
		<!--
		.style1 {
		color: #FFFFFF;
		font-weight: bold;
		}
		-->
		</style>
<style>
td{
text-align:center;
}			
</style>
	</head>
	<body>
	
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_MERCHANT_DETAILS" text="Merchant Details"> </spring:message></span>
		</h3>
	</div>
	<form:form id="viewMerchantForm">
	<%-- <form:hidden path="merchantId"></form:hidden> --%>
	<jsp:include page="csrf_token.jsp"/>
	<div class="col-sm-5 col-sm-offset-8">
		<a href="javascript:submitForm('viewMerchantForm','editMerchant.htm?merchantId=<c:out value="${merchantDTO.merchantId}"/>')"><strong><spring:message code="EDIT_MERCHANT" text="Edit Merchant"> </spring:message></strong></a> |
		<a href="javascript:submitForm('viewMerchantForm','showMechantOutlets.htm?merchantId=<c:out value="${merchantDTO.merchantId}"/>')"><strong><spring:message code="LABLE_OUTLET" text="Outlet"> </spring:message></strong></a> |
		<a href="javascript:submitForm('viewMerchantForm','searchMerchant.htm')"><strong><spring:message code="MERCHANT_SEARCH" text="Search Merchants"> </spring:message></strong></a>  &nbsp; &nbsp; &nbsp;
	</div><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CUSTOMER_NAME" text="Customer Name"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${merchantDTO.merchantName}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="MobileNo.:"></spring:message></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${merchantDTO.primaryContactMobile}" /> </div>
				</div>
			</div>
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_CITY" text="City:"/></label>
					<div class="col-sm-5"><c:out value="${merchantDTO.cityName}" /></div> 
					
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_QUARTER" text="Quarter:"/></label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${merchantDTO.quarterName}" /></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_ADDRESS" text="Address:"/></label> 
					<div class="col-sm-5"><c:out value="${merchantDTO.address}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_COUNTRY" text="Country :"/></label> 
					<div class="col-sm-5" style="margin-top:4px;">
					<c:set var="lang" value="${language}"></c:set>
					<c:forEach items="${merchantDTO.merchantCountry.countryNames}" var="cname">
					<c:if test="${cname.comp_id.languageCode==lang }">
					<c:out 	value="${cname.countryName}" />											
					</c:if>
					</c:forEach>
					</div> 
				</div>
			</div>
			<div class="row">
			<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_EMAILID" text="Email Id:"/></label>
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${merchantDTO.primaryeMailAddress}" /></div> 
					
				</div>
			</div>
			</div>
			
</form:form>
</div>
</div>
</body>
</html>