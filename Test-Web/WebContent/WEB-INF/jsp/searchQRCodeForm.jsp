<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#state").change(function() {
		$city = document.getElementById("state").value;
		$csrfToken = $("#csrfToken").val();
		$.post("getQuarters.htm", {
			city : $city,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("city").innerHTML="";
			document.getElementById("city").innerHTML = data;
			setTokenValFrmAjaxResp();
		});
	});
	
});
function downloadQRCode(mobileNumber){
	$csrfToken = $("#csrfToken").val();
	$.post("exportQRCode.htm", {
		mobileNumber : "211"+mobileNumber,
		csrfToken : $csrfToken
	}, function(data) {
		
	});
	//setTokenValFrmAjaxResp();
}
/* function customerDetail(url,operatorId){
	document.getElementById('customerId').value=operatorId;
	submitlink(url,'agentViewForm');
} */

function qrCodePDF(url,customerId){
	 
	document.getElementById('customerId').value=customerId;
	submitlink(url,'searchCustomerforQRCode');
	// submitlink("exportQRCode.htm","searchAuditLogForm"); 
	 for(var i=0;i<150000;i++);{
	 document.body.style.cursor = '';
	 canSubmit = true; 
	 }
}

function bulkQrCodePDF(){
	 
	submitlink('bulkExportQRCode.htm','searchCustomerforQRCode');
	// submitlink("exportQRCode.htm","searchAuditLogForm"); 
	 for(var i=0;i<150000;i++);{
	 document.body.style.cursor = '';
	 canSubmit = true; 
	 }
}

function searchCustomer(form){
	
	form.action="searchQRCodeForm.htm";
	form.submit();
}
</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_QR_CODE" text="QR Codes" /></span>
		</h3>
	</div><br/>
	<div class="col-sm-6 col-sm-offset-5" id="msg" style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message code="${message}" text="" /></div>
	<form:form name="searchCustomerforQRCode" id="searchCustomerforQRCode" class="form-inline"  commandName="qrCodeDTO">
	<form:hidden path="customerId"  />
	<div id="csrfToken">
	<jsp:include page="csrf_token.jsp"/>
	</div>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_AGT_TYPE" text="Type"/></label> 
					<form:select path="custType" cssClass="dropdown chosen-select">
						<form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--">
						</spring:message></form:option>
						<form:option value="1">Agent</form:option>
						<form:option value="2">Merchant</form:option>
						</form:select>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_STATE" text="State"/></label>
					<form:select path="state" cssClass="dropdown chosen-select">
						<form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--">
						</spring:message></form:option>
						<form:options items="${stateList }" itemValue="cityId" itemLabel="city"></form:options>
						</form:select>
				</div> 
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_QUARTER" text="City" /></label> 
					<form:select path="city" cssClass="dropdown">
						<form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--">
						</spring:message></form:option>
						</form:select>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_AGENT_CODE" text="Agent Code" /></label> 
					<form:input path="agentCode"  id="agentCode" cssClass="form-control"/>
				</div>
			</div>
			<div class="row">
				
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_AGENT_MOBILE_NUMBER" text="Mobile Number" /></label> 
					<form:input path="mobileNumber"  id="mobileNumber" cssClass="form-control"/>
				</div>
			</div>
			
			<br>
			<div class="box-footer">
				<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="searchCustomer(this.form);"></input><br/><br/>
			</div>
			
			
				
</div>
</form:form>
</div>
			<div class="box">
			
					<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
					
					<span style="margin-right: 30px; float:right; margin-top: 15px;">
					<a href="#" style="text-decoration:none;margin-left:11px" onclick="javascript:bulkQrCodePDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
						<%-- <span> <spring:message code="LABEL_PDF" text="PDF"/></span> --%>
					</a>
					</span>					
   			   </div>
				
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;width: 100%">
						<thead>
							<tr>
								<th><spring:message code="LABEL_SR_NO" text="Sr. No."/></th>
								<th><spring:message code="LABEL_CUST_CODE" text="Code"/></th>
								<th><spring:message code="LABEL_MOBILE_NUMBER" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_NAME" text="Name"/></th>
								<%-- <th><spring:message code="LABEL_QR_IMG" text="QR Image"/></th> --%>
								<th><spring:message code="LABEL_Action" text="Action"/></th>
							</tr>
						</thead>
						<tbody>
						<c:set var="j" value="0"></c:set>
				            <c:forEach items="${page.results}" var="customer">
				            <c:set var="j" value="${ j+1 }"></c:set>
				            <tr>
				            	<td><c:out value="${j}" /></td>
							    <td><c:out value="${customer.agentCode}" /></td>
							    <td><c:out value="${customer.mobileNumber}" /></td>		
							    <td><c:out value="${customer.firstName}" /> <c:out value="${customer.lastName }"></c:out></td>
							   <!--  <td><a href="javascript:customerDetail('viewAgent.htm','<c:out value="${cust.customerId}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a></td> -->
							    <td><a href="javascript:qrCodePDF('exportQRCode.htm','<c:out value="${customer.customerId}"/>');">Download QR Code</a></td>
							    
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
	</div>
	
</div>
</body>
</html>