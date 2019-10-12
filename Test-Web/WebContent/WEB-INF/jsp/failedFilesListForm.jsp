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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/failedFile.js"></script>
<style>
#status {
    height: 35px;
    position: absolute;
    left: 57%;
    top: 100%;
    background-image: none;
    background-repeat: no-repeat;
    background-position: center;
    margin: -42px 0 0 -100px;
}
</style>
<script type="text/javascript">

 var Alertmsg={
		 "fromDateGreaterCurrentDate" : "<spring:message code="ERROR_MESSAGE_FROM_DATE_GREATER_CURRENT_DATE" text="From date should be less than CURRENT DATE, Please select valid date."></spring:message>",
		 "toDateGreaterCurrentDate" : "<spring:message code="ERROR_MESSAGE_TO_DATE_GREATER_CURRENT_DATE" text="To date should be less than CURRENT DATE, Please select valid date."></spring:message>",
		 "searchAlert" : "<spring:message code="ERROR_MESSAGE_SEARCH_OPTION" text="Please enter any search key."></spring:message>",
		  "toDateFromDate" : "<spring:message code="ERROR_MESSAGE_TODATE_GREATER_FROM_DATE" text="To Date must be greater than or equal to From Date."></spring:message>"
  };	
 
 
</script>

</head>

<body>
<form:form name="failedFileDownloadForm" id="failedFileDownloadForm" class="form-inline"  method="post" commandName="fileUploadDTO" enctype="multipart/form-data">
<div id="csrfToken">
	<jsp:include page="csrf_token.jsp"/>
	</div>
	
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_DOWNLOAD_FAILED_FILES" text="Download Failed Files" /></span>
		</h3>
	<br />
	<div class="col-md-5 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
	<spring:message code="${message}" text="" />
	</div>
	</div><br/>
	
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_FILE_NAME" text="File Name" /></label> 
					<div class="col-sm-6">
					<form:input path="failedFileName" id="failedFileName" maxlength="32" cssClass="form-control" />
					</div>
				</div>
						
			</div>
			<div class="row">
			
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_FROM_DATE" text="From Date" /></label> 
					<div class="col-sm-6">
					<form:input path="fromDate" cssClass="form-control datepicker noFutureDate" id="fromDate" onchange="javascript:compareFromDate();" maxlength="10"/> 
					</div>
				</div>
				
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_TO_DATE" text="To Date" /></label> 
					<div class="col-sm-6">
					<form:input path="toDate" cssClass="form-control datepicker noFutureDate" id="toDate" onchange="javascript:compareFromDate();" maxlength="10"/> 
					</div>
				</div>
			</div>
			
			<div class="box-footer">
			<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SEARCH" text="Search"/>" onclick="javascript:searchFailFileSubmit();" style="margin-right: 60px;"></input>
			<br/><br/>
			</div>
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
				
				
					<table id="example1" class="table table-bordered table-striped" style="text-align:center">
						<thead>
							<tr>
								<th><spring:message code="LABEL_SL_NO" text="Sl.no."/></th> 
								<th><spring:message code="LABEL_FAILED_FILE_DATE" text="Date"/></th>
								<th><spring:message code="LABEL_FILE_NAME" text="File name"/></th>
								<th><spring:message code="LABEL_UPLOADED_BY" text="Uploaded By"/></th>
								<th><spring:message code="LABEL_TOTAL_COUNT" text="Total Count"/></th>
							    <th><spring:message code="LABEL_FAIL_COUNT" text="Fail Count"/></th>
							    <th><spring:message code="LABEL_FAIL_AMOUNT" text="Failed Amount"/></th>
						        <th><spring:message code="LABEL_SUCCESS_COUNT" text="Success Count"/></th>
						        <th><spring:message code="LABEL_SUCCESS_AMOUNT" text="Success Amount"/></th>
						        <th><spring:message code="LABEL_SERVICE_CHARGE" text="Service Charge"/></th>
						        <th><spring:message code="LABEL_DOWNLOAD_PROCESSED_FILE" text="Download Processed File"/></th>
						        <th><spring:message code="LABEL_STATUS" text="Status"/></th>
							</tr>
						</thead>
						<tbody>
						
					<c:set var="j" value="0"></c:set>
					<c:forEach items="${page.results}" var="failedList">
					<c:set var="j" value="${ j+1 }"></c:set>
									<tr>	
											<td><c:out value="${j}" /></td>
											 <td>
											 	<fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss"
													value="${failedList.createdDate}" />
											</td> 
											
										 	<td>
												<c:out value=" ${failedList.fileName}"></c:out>
											</td>
											
											<td>
												<c:out value=" ${failedList.userId}"></c:out>
											</td>
											
											<td>
												<c:out value=" ${failedList.totalCount}"></c:out>
											</td>
											
											<td>
												<c:out value=" ${failedList.failCount}"></c:out>
												<%-- <a href="javascript:downloadFile('fail_'+'<c:out value="${failedList.fileName}"/>');"><c:out value=" ${failedList.failCount}"></c:out></a> --%>
											</td>
											
											<td>
												<fmt:formatNumber type="number" pattern="##.##" value="${failedList.failedAmount}" />
											</td>
											
											<td>
												<c:out value=" ${failedList.successCount}"></c:out>
											</td>
											
											<td>
												<fmt:formatNumber type="number" pattern="##.##" value=" ${failedList.successAmount}" />
											</td>
											<td>
												<fmt:formatNumber type="number" pattern="##.##" value="${failedList.serviceCharge}" />
											</td>
											<td>
												<a href="javascript:downloadFile('<c:out value="${failedList.fileName}"/>');"><c:out value="Download Processed File"></c:out></a>
											</td>
											
											<td>
										    <c:if test="${failedList.status == 0}">
										    <c:out value="Unprocessed"></c:out>
										    </c:if>
										    <c:if test="${failedList.status == 1}">
										    <c:out value="Processed"></c:out>
										    </c:if>  
										  
											</td>
									</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
	</div>
	
</div>
</form:form>
</body>
</html>
