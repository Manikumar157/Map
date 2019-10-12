<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		$("#merchantId").change(function() {
			$merchant = document.getElementById("merchantId").value;
			$csrfToken = $("#csrfToken").val();
			
			$.post("getOutletList.htm", {
				merchant : $merchant,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("outletDiv").innerHTML="";
				document.getElementById("outletDiv").innerHTML = data;
				setTokenValFrmAjaxResp();
				applyChosen();
			});
		});
	});	

	
	function validate() {
		var mobileNumber = $("#mobileNumber").val();
		var status = $("#active").val();
		var outletId = $("#outletId").val();
		var merchantId = $("#merchantId").val();
		var mobileNumberlength = document.addTerminalForm.mobileNumber.length;
		
		if(mobileNumber == ""){
			alert("<spring:message code='ALERT_MOBILE_NUMBER' text='Please enter mobile number'/>");
			return false;
		}
		if(!mobileNumber.match(/[0-9]/)){
			alert("<spring:message code='VALID_MOBILE_NUMBER' text='Please enter valid mobile number'/>");
			return false;
		}
		if(mobileNumber.length!=10){
			alert("Mobile Number lenght should be 10");
			return false;
		}
		if(status==""){
			alert("<spring:message code='ALERT_STATUS' text='Please select a status'/>");
			return false;
		}
		/* if(outletId==""){
			alert("<spring:message code='ALERT_OUTLET' text='Please select a outlet'/>");
			return false;
		}
		if(merchantId==""){
			alert("<spring:message code='ALERT_MERCHANT' text='Please select a merchant'/>");
			return false;
		} */
		
		document.addTerminalForm.action = "saveTerminal.htm";
		document.addTerminalForm.submit();	
			
	}
	function cancelForm(){
		document.addTerminalForm.action="searchTerminal.htm?outletId="+$("#outletId").val();
		document.addTerminalForm.submit();
	}
	</script>
	</head>
<body>
<form:form id="addTerminalForm" name="addTerminalForm" commandName="terminalDTO" class="form-inline">
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_ADD_TERMINAL" text="Add Terminal"></spring:message></span>
		</h3>
	</div><br/><br/>
	<form:hidden path="merchantId" />
	<form:hidden path="outletId" />
	<form:hidden path="terminalId" />
			<div style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div>
				<div class="row">
						<div class="col-md-6">
							<label class="col-sm-5"> <spring:message
									code="LABEL_MOBILE_NO" text="Mobile Number" /><font color="red">*</font></label>
							<form:input path="mobileNumber" cssClass="form-control" maxlength="10"/>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-5"><span style="color:#FF0000;"></span><spring:message code="LABEL_STATUS" text="Status"/></label> 
							<form:select path="active" cssClass="dropdown">
		              		  <form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
		                		<form:option value="10"><spring:message code="LABEL_ACTIVE" text="Active"/></form:option>
		                		<form:option value="20"><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></form:option>		
		         	  		</form:select>
						</div>
				</div>
				<div class="row">
				<div class="col-md-2 col-md-offset-9">
				<c:choose>
					<c:when test="${(terminalDTO.terminalId eq null && message==null) }">
						<c:set var="buttonName" value="LABEL_ADD" scope="page" />
					</c:when>
					<c:when test="${(terminalDTO.terminalId eq null && message!=null) }">
						<c:set var="buttonName" value="LABEL_ADD" scope="page" />
					</c:when>
					<c:when test="${(terminalDTO.terminalId ne null && message!=null) }">
						<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
					</c:when>
					<c:otherwise>
					<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
					</c:otherwise>
					</c:choose>
						<input type="button" class="btn btn-primary pull-right"
							value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
							onclick="validate();"></input> 
							<input type="button" class="btn btn-default" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
				</div><br/><br/>
				</div><br/>
</div>
<!-- /.box -->
</div>	
</form:form>
</body>
</html>
