<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<%-- <script type="text/javascript"
	src="<%=request.getContextPath()%>/js/webUser.js"></script> --%>

<script type="text/javascript">
function viewUsers(url,pageNumber){
	document.getElementById('pageNumber').value=pageNumber;
	submitlink(url,'viewWebUserForm');
}
</script>

</head>

<body onload="check()">
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_USER_MNMT" text="Web Users" /></span>
		</h3>
	</div><br/>
	<%-- <div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
							code="${message}" text="" />
	</div> --%>
	<div class="pull-right">
		<%-- <a href="javascript:submitForm('viewWebUserForm','searchWebUser.htm?pageNumber=1')" ><strong><spring:message code="LABEL_VIEW_WEB_USER" text="View Users"></spring:message></strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; --%>
			<a href="javascript:viewUsers('searchWebUser.htm','1')"><strong><spring:message code="LABEL_VIEW_WEB_USER" text="View Users"></spring:message></strong></a> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
	</div><br/><br/>
	<input type="hidden" name="pageNumber" id="pageNumber" value=""/>
	<form class="form-inline" name = "viewWebUserForm" id="viewWebUserForm" role="form">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_USERID" text="User ID" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${webUserDTO.userName}"/></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_WEBUSER_NAME" text=" Name" />:</label> 
					<div class="col-sm-5"><c:out value="${webUserDTO.firstName} ${webUserDTO.middleName} ${webUserDTO.lastName}"/> </div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NUM" text="Mobile Number" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${webUserDTO.mobileNumber}"/></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3"><spring:message code="LABEL_EMAIL" text="Email" />:</label> 
					<div class="col-sm-5"><c:out value="${webUserDTO.email}"/></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_COUNTRY" text="Country:" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${webUserDTO.countryName}"/></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3" style="margin-top:4px;"><spring:message code="LABEL_USER_TYPE" text="User Type" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${webUserDTO.roleName}"/></div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_CREATED_DATE" text="Created Date" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><fmt:formatDate pattern="dd/MM/yyyy" value="${webUserDTO.createdDate}" /></div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-3" style="margin-top:4px;"><spring:message code="LABEL_STATUS" text="Status"/>:</label>
					<c:if test="${webUserDTO.status eq 'N'}">
                        <c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>

                    </c:if>
                    <c:if test="${webUserDTO.status eq 'Y'}">
                        <c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
                    </c:if> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${status}" /></div>
				</div>
			</div>
			<c:if test='${businessPartnerName ne null && businessPartnerName != "undefined" && businessPartnerName != ""}'>                               
			<div class="row">
			<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_BUSNIESS_PARTNER_NAME" text="Super Agent" />:</label> 
					<div class="col-sm-5" style="margin-top:4px;"><c:out value="${businessPartnerName.name}"/></div>
				</div>
			</div>
			 </c:if> 
			
</div>
</form>
</div>
	
</div>
</body>
</html>
