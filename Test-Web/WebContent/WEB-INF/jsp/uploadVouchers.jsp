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
<%-- <link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" /> --%>

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/js/calendar.js"></script> --%>

<!-- Loading language definition file -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>

<script type="text/javascript">
function validation(){
var denominationFile=document.getElementById("denominationFile").value;
var file = document.uploadVouchersForm.denominationFile.value.substring(document.uploadVouchersForm.denominationFile.value.lastIndexOf("."));

if(denominationFile==""){
alert("<spring:message code="VALID_DENOMINATION_FILE" text="Please select Denomination File"/>");
return false;
}else  if((file.toLowerCase()!= ".csv")){
alert("<spring:message code="VALID_CSV_FILE" text="Please select .CSV File only"/>");
return false;
} else {
document.uploadVouchersForm.submit();
}
}	
</script>
</head>
<body>
<div class="col-md-12">
<form:form name="uploadVouchersForm" id="uploadVouchersForm" class="form-inline" action="uploadVouchersFile.htm" method="post" commandName="operatorDenominationDTO" enctype="multipart/form-data">
<form:hidden path="operatorId" />
<jsp:include page="csrf_token.jsp"/>
	<div class="box">
		<div class="box-header">
			<h3 class="box-title">
				<span><c:out value="${operatoName.operatorName}"></c:out>-<spring:message code="LABEL_DENOMINATION_FILEUPLOADING" text="Denomination File Uploading" /></span>
			</h3>
		</div><br/>
		<!-- /.box-header -->
		<div class="box-body">
			<!-- form start -->
			<div class="row">
			<div class="col-md-2">
			
			</div>
			<div class="col-md-7">
				<!--  changes by vineeth -->
			<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
				<!--  changes over vineeth -->
			</div>
			
			<div class="col-md-3">
				<label class="col-sm-7"><a href="javascript:submitForm('uploadVouchersForm','showOperators.htm')"> <spring:message code="LINK_VIEW_OPERATOR" text="View Operators" /> </a></label> 
			</div>
			</div>
			<div class="row">
			<div class="col-md-3"></div>
				<div class="col-md-6">
					<label class="col-sm-6"><spring:message code="LABEL_UPLOAD_DENOMINATIONFILE" text="Upload Denomination File (.csv only)" /><font color="red">*</font></label> 
					<form:input path="voucherFile" type="file" id="denominationFile" />
				<a href="javascript:submitForm('uploadVouchersForm','fileUploadInstructions.htm?templeteId=<c:out value="${operatorDenominationDTO.templateId}"/>');" ><spring:message code="LABEL_DOWNLOAD_FILE_INST"
											text="(Download instructions)" /></a>
					<input type="button" value="<spring:message code="BUTTON_SUBMIT" text="Submit"/>" onclick="validation();" class="btn btn-primary pull-right"/> 
				</div>
			</div><br/>
			<c:if test="${fn:length(operatorDenominationDTO.voucherErrorList) > 0}">
			<table>
				
				<c:forEach items="${operatorDenominationDTO.voucherErrorList}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out>  <spring:message code="LABEL_OPERATORVOUCHER_FILE_VALUE_NOT_EXIST" text="Denomination does not exist." /></FONT></td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			<c:if test="${fn:length(operatorDenominationDTO.voucherSlNumErrorList) > 0}">
			<table>
				
				<c:forEach items="${operatorDenominationDTO.voucherSlNumErrorList}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out>  <spring:message code="LABEL_OPERATORVOUCHER_FILE_VOUCHER_SERAIL_NUMBER_ALREADY_EXIST" text="Voucher Serail number is Already  exist." /></FONT></td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			<c:if test="${fn:length(operatorDenominationDTO.voucherNumErrorList) > 0}">
			<table>
				<c:forEach items="${operatorDenominationDTO.voucherNumErrorList}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out>  <spring:message code="LABEL_OPERATORVOUCHER_FILE_VOUCHER_NUMBER_ALREADY_EXIST" text="Voucher number is Already  exist." /></FONT></td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			<c:if test="${fn:length(operatorDenominationDTO.voucherInvalidSlNum) > 0}">
			<table>
			<tr><td><FONT color="RED"><spring:message code="LABEL_INVALID_VOUCHER_SLNUMBER" text="Invalid voucher serail number in csv file" /></FONT></td></tr>
				<c:forEach items="${operatorDenominationDTO.voucherInvalidSlNum}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out>  </FONT></td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			<c:if test="${fn:length(operatorDenominationDTO.invalidVoucherNumbers) > 0}">
			<table>
			<tr><td><FONT color="RED"><spring:message code="LABEL_INVALID_VOUCHER_NUMBER" text="Invalid voucher number in csv file" /></FONT></td></tr>
				<c:forEach items="${operatorDenominationDTO.invalidVoucherNumbers}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out>  <spring:message code="LABEL_INVALID_VOUCHER_NUMBER" text="Invalid voucher number" /></FONT></td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			<c:if test="${fn:length(operatorDenominationDTO.csvvoucherSlNum) > 0}">
			<table>
				<tr><td><FONT color="RED"><spring:message code="LABEL_DUPLICATE_VOUCHER_SLNUM" text="The Voucher Serial Number values are duplicated in CSV File" /></FONT></td></tr>
				<c:forEach items="${operatorDenominationDTO.csvvoucherSlNum}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out></font> </td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			<c:if test="${fn:length(operatorDenominationDTO.csvvoucherNum) > 0}">
			<table>
			<tr><td><FONT color="RED"><spring:message code="LABEL_DUPLICATE_CSV_VOUCHER_NUMBER" text="Duplicate Voucher Number values are in CSV File" /></FONT></td><tr>
				<c:forEach items="${operatorDenominationDTO.csvvoucherNum}" var="error">
					<tr>
						<td align="center" colspan="7"><FONT color="RED"><spring:message code="LABEL_BRANCH_FILE_ERROR_ROW_NO" text="Row Number" /> <c:out value="${error }">
						</c:out></FONT> </td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
		</div>
		<!-- <div class="box-footer">
			<button type="submit" class="btn btn-primary pull-right">Submit</button><br/><br/><br />
		</div> -->
	</div>
	<div class="box">
		<div class="box-body table-responsive">
		<!-- //@start Murari  Date:06/08/2018 purpose : bug 5845 -->
		<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
		<!-- End -->
			<!-- <table id="example1" class="table table-hover" style="text-align:center;"> -->
				<thead>
					<tr>
						<th><spring:message code="LABEL_DENOMINATION" text="Denomination" /></th>
						<th><spring:message code="LABEL_VOUCHERSERIALNUMBER" text="VoucherSerialNumber" /></th>
						<th><spring:message code="LABEL_VOUCHERNUMBER" text="VoucherNumber" /></th>
						<th><spring:message code="LABEL_STATUS" text="Status" /></th>
						<th><spring:message code="LABEL_OPERATOR" text="Operator" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.results}" var="vouchers">
					<tr>
						<td><c:out value="${vouchers.operatorDenomination.denomination}" /></td>
						<td><c:out value="${vouchers.voucherSerialNumber}" /></td>
						<td><c:out value="${vouchers.voucherNumber}" /></td>
						<c:if test="${vouchers.active==1}">
							<c:set var="status" value="Active"></c:set>
						</c:if>
						<c:if test="${vouchers.active==0}">
							<c:set var="status" value="Inactive"></c:set>
						</c:if>
						<td><c:out value="${status}" /></td>
						<td><c:out value="${vouchers.operator.operatorName}" /></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	</form:form>
</div>
</body>
</html>