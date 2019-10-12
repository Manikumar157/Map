<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/calendar.js"></script>

<!-- Loading language definition file -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>

<script type="text/javascript">
function validation(){
var txnFile=document.getElementById("txnFile").value;
var file = document.uploadTransactionForm.txnFile.value.substring(document.uploadTransactionForm.txnFile.value.lastIndexOf("."));

if(txnFile==""){
alert("<spring:message code="VALID_TRANSACTION_FILE" text="Please select transaction file"/>");
return false;
}else  if((file.toLowerCase()!= ".csv")){
alert("<spring:message code="VALID_CSV_FILE" text="Please select .CSV File only"/>");
return false;
} else {
	submitlink('uploadTrnsactionFile.htm','uploadTransactionForm');
}
}	
</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_TXN_FILEUPLOADING" text="Bulk Payment File Upload" /></span>
		</h3>
	</div><br/>
	<div style="color: #ba0101; font-weight: bold; font-size: 12px; margin-left:340px;">
		<spring:message code="${message}" text="" />
	</div>
	<form:form class="form-inline" name="uploadTransactionForm" id="uploadTransactionForm" action="uploadTrnsactionFile.htm" method="post" commandName="transactionParamDTO" enctype="multipart/form-data">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box-body">
	<div class="row">
			<div class="col-md-3"></div>
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_UPLOAD_TXN_FILE" text="Upload File (.csv only)" /><font color="red">*</font></label> 
				   	<form:input path="txnFile" class="form-control" type="file" id="txnFile" />
				   	<a style="margin-left: 16px;" href="javascript:submitForm('uploadTransactionForm','fileUploadInstructions.htm?templeteId=<c:out value="${transactionParamDTO.templateId}"/>');" ><spring:message code="LABEL_DOWNLOAD_FILE_INST"
											      text="(Download Sample File)" /></a>
				</div>
		</div><br/><br/>
		
			<div class="box-footer">
			<div class="col-md-6">
			<a style="margin-left: 16px;" href="javascript:submitForm('uploadTransactionForm','downloadBulkPaymentFailFile.htm')" ><font size="3px;" face="bold"><spring:message code="LABEL_DOWNLOAD_FILE_INST"
											      text="Download Processed File" /></font></a>
			</div>
			<div class="col-md-6"></div>
			<%-- <input type="button" class="btn-primary btn pull-right" style="margin-left:15px; margin-right:15px;"
							value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
							onclick="javascript:submitForm('uploadTransactionForm','selectTransactionSupportForm.htm')" /> --%>
			<!-- 	change over	 -->					
			<input type="button" class="btn btn-primary pull-right" value="<spring:message code="BUTTON_SUBMIT" text="Submit"/>" onclick="validation();" />
			</div><br/><br/>
			<div class="row">
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_TOTAL_PROCESSED_RECORD" text="Total Proccessed Record" /></label> 
				   	<c:out value="${UploadFileDetails.totalProcessedRecord }"></c:out> 
				</div>
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_TOTAL_PROCESSED_VALUE" text="Total Proccessed Amount" /></label> 
				   	<c:out value="${UploadFileDetails.totalProcessedValue }"></c:out>
				</div>
			 
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_TOTAL_SUCCESS_RECORD" text="Total Success Record" /></label> 
				   <c:out value="${UploadFileDetails.totalSuccessRecord }"></c:out> 
				</div>
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_TOTAL_SUCCESS_VALUE" text="Total Success Amount" /></label> 
				   	<c:out value="${UploadFileDetails.totalSuccessValue }"></c:out> 
				</div>
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_TOTAL_FAILED_RECORD" text="Total Failed Record" /></label> 
				   	<c:out value="${UploadFileDetails.totalFailedRecord }"></c:out>
				</div>
				<div class="col-md-6">
					<label class="col-sm-5"><spring:message code="LABEL_TOTAL_FAILED_VALUE" text="Total Failed Amount" /></label> 
				   	<c:out value="${UploadFileDetails.totalFailedValue }"></c:out>
				</div>
		</div>
		</div>
</form:form>
</div>

</div>
</body>
</html>