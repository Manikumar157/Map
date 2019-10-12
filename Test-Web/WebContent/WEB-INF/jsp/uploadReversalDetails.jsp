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
<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript">
function validation(){
var reversalFile=document.getElementById("reversalFile").value;
var file = document.uploadReversalForm.reversalFile.value.substring(document.uploadReversalForm.reversalFile.value.lastIndexOf("."));

if(reversalFile==""){
alert("<spring:message code="VALID_REVERSAL_FILE" text="Please select Reversal File"/>");
return false;
}else  if((file.toLowerCase()!= ".csv")){
alert("<spring:message code="VALID_CSV_FILE" text="Please select .CSV File only"/>");
return false;
} else {
document.uploadReversalForm.submit();
}
}	

function cancelForm(){
	 document.uploadReversalForm.action="showAdjustmentForm.htm";
	 document.uploadReversalForm.submit();
}

</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_REVERSAL_FILEUPLOADING" text="Reversal File Uploading" /></span>
		</h3>
	</div><br/>
	<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<spring:message code="${message}" text="" />
	</div>
	<form:form class="form-inline" id="uploadReversalForm" name="uploadReversalForm" action="uploadReversalFile.htm" method="post" commandName="reversalDTO" enctype="multipart/form-data">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box-body">
			<div class="col-md-3"></div>
				<div class="col-md-8">
					<label class="col-sm-5"><spring:message code="LABEL_UPLOAD_REVERSAL_FILE" text="Upload Reversal File (.csv only)" /><font color="red">*</font></label> 
				   <form:input path="reversalFile" type="file" id="reversalFile" />
				</div>
			</div><br/><br/>
		
			<div class="box-footer">
			<input type="button" class="btn btn-primary col-sm-offset-7" value="<spring:message code="BUTTON_SUBMIT" text="Submit"/>" onclick="validation();" />
			<input type="button" class="btn-primary btn"
							value="<spring:message code="LABEL_CANCEL" text="Cancel" />"	onclick="cancelForm();" />	
			</div><br/><br/>
			</form:form>
</div>
</div>
</body>
</html>