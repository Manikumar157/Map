<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<script type="text/javascript">
	function validate() {
		var mobileNumber = document.getElementById("mobileNumber").value;
		var emailId = document.getElementById("emailId").value;
		var pattern = '^\[a-zA-ZÀ-ÿ\' ]*$';
		var numericPattern = '^[0-9]*$';
		var codePattern = '^\[A-Za-z]*$';
		var emailIdPattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
		var numbers =  /^[0-9]+$/;
		
		var Alertmsg={
		 "validEmailID":"<spring:message code="ERROR_MESSAGE_VALID_EMAIL_ID" text="Please enter valid Email ID."></spring:message>",
		 "emailIdMinLen":"<spring:message code="ERROR_MESSAGE_EMAIL_MINLEN" text="Email ID should be of minimum 11 characters"></spring:message>",
		 "mobileNumLength":"<spring:message code="VALIDATION_MOBILE_LENGTH" text="Please enter valid Mobile Number of Length"/>"
		}
		if (mobileNumber == "" && emailId=="") {
			alert("Please enter a Mobile Number or Email ID");
			document.helpDeskForm.mobileNumber.focus();
			return false;
		}else if($.trim(document.helpDeskForm.mobileNumber.value) != "" && !mobileNumber.match(numbers) ){
			alert('Mobile Number should contain only numbers');
			document.helpDeskForm.mobileNumber.focus();
			return false;
		}else if($.trim(document.helpDeskForm.mobileNumber.value) != "" && $.trim(document.helpDeskForm.mobileNumber.value).length < mobileNumberlen ){
			alert(Alertmsg.mobileNumLength+" "+mobileNumberlen);
			document.helpDeskForm.mobileNumber.focus();
			return false;
		}else if($.trim(document.helpDeskForm.emailId.value) != "" && $.trim(document.helpDeskForm.emailId.value).length < 11 ){
			alert(Alertmsg.emailIdMinLen);
			document.helpDeskForm.emailId.focus();
			return false;
		}
		else if($.trim(emailId)!="" && emailIdPattern.test(emailId) == false) {			
				alert(Alertmsg.validEmailID);
	            return false;
	        }
		else {
			document.helpDeskForm.action = "saveHelpDesk.htm";
			document.helpDeskForm.submit();
		}
	}
 	function cancelForm() {
		document.helpDeskForm.action = "showHelpDeskConfig.htm";
		document.helpDeskForm.submit();
	} 
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	 function helpDeskDetail(helpDeskId){
		document.getElementById('helpDeskId').value=helpDeskId;
		submitlink('editHelpDesk.htm','helpDeskForm');
	}
	//@End
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>
<style>
.table_border {
	height:120px;
}
</style>
</head>
 <body>
	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_HELPDESK_CONFIGURATION" text=" HelpDesk Configuration" /></span>
				 </h3>
			</div>
			<h5 class="box-title">
					<span style="margin-left: 20px;font-weight: bold"><spring:message code="(Please Enter Mobile Number and EmailID)" text=" (Please Enter Mobile Number and EmailID)" /></span>
				 </h5>
			<br />
			<div class="col-md-6 col-md-offset-5">
			<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
			</div>
			<!-- /.box-header -->
			<authz:authorize ifAnyGranted="ROLE_bankadmin">
			<%-- <span style="margin-left: 3px" class="box-title"><spring:message code="(Enter MobileNumber and EmailID)" text="(Enter MobileNumber and EmailID)"/></span> --%>
			<div class="box-body" style="height:150px;">
				<!-- form start -->
				
				<div class="col-sm-12 table_border" ><!-- style="border: 1px solid;border-radius: 15px;" -->
				<form:form name="helpDeskForm" id="helpDeskForm" action="saveHelpDesk.htm"
						method="post" commandName="helpDeskDTO" class="form-inline">
						<jsp:include page="csrf_token.jsp"/>
						<br/>
					<div class="row">
						<form:hidden path="helpDeskId"/>
						<div class="col-md-1"></div>
						<div class="col-md-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_MOBILE_NO" text="Mobile Number"></spring:message>
							<!-- <font color="red">*</font>--></label> 
							<form:input path="mobileNumber" cssClass="form-control" maxlength="${mobilenumberLength}"/>
							<%-- <FONT color="red">
							<form:errors path="currencyName" /></FONT> --%>
							
						</div>
						<div class="col-md-5">
							<label class="col-sm-3"  style="margin-left:-10px;"><spring:message
									code="LABEL_EMAILID" text="Email ID"></spring:message><!-- <font
								color="red">*</font> --></label>
							<form:input path="emailId" cssClass="form-control"
								maxlength="30" />
							<%-- <FONT color="red">
							<form:errors path="currencyName" /></FONT> --%>
							
						</div>
					</div>

						<div class="box-footer class="col-sm-12">
							<div class="col-sm-6">
							<c:choose>							
								<c:when test="${(helpDeskDTO.helpDeskId eq null) and count lt 10}">
									<c:set var="buttonName" value="LABEL_ADD" scope="page" />
								</c:when>
								<c:when test="${(helpDeskDTO.helpDeskId ne null)}">
									<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
								</c:when>
							</c:choose>
							<c:if test="${buttonName ne null}">
							<input type="button" id="submitButton"  style="margin-left:400px;"
								value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
								onclick="validate();" class="btn btn-primary" ></input>
								</c:if>
								</div>
							 <c:if test="${helpDeskDTO.helpDeskId ne null}">
								<div class="col-sm-6">
								<input type="button" style="margin-left:40px;" class="btn btn-primary"
									value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
									onclick="cancelForm();" /></div>
							</c:if> 
						</div><br>
				</form:form>
				</div>
			</div>
			</authz:authorize>
			</div>
			<div class="box">
			<div class="box-body">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_MOBILE_NO" text="Mobile NUmber" /></th>										
							<%-- 	<authz:authorize ifAnyGranted="ROLE_editCurrencyAdminActivityAdmin">	 --%>								
									<%-- <th><spring:message code="LABEL_ACTION" text="Action" /></th> --%>
							<%-- 	</authz:authorize> --%>
								<th><spring:message code="LABEL_EMAILID" text="Email ID" /></th>										
								<%-- <authz:authorize ifAnyGranted="ROLE_editCurrencyAdminActivityAdmin"> --%>									
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								<%-- </authz:authorize> --%>
							</tr>
						</thead>
						<tbody>
							<%
								int i = 0;
							%>
							<c:forEach items="${page.results}" var="helpDesk">
								<tr>
									<td><c:out value="${helpDesk.mobileNumber}" /></td>
									<%-- <authz:authorize ifAnyGranted="ROLE_editCurrencyAdminActivityAdmin"> --%>
										<%-- <td>
											<a href="javascript:helpDeskDetail('<c:out value="${helpDesk.id}"/>');" />
										<spring:message code="LABEL_EDIT" text="Edit" /></td>	 --%>								
									<%-- </authz:authorize> --%>
									<td><c:out value="${helpDesk.emailId}" /></td>
									<%-- <authz:authorize ifAnyGranted="ROLE_editCurrencyAdminActivityAdmin"> --%>
										<td>
											<a href="javascript:helpDeskDetail('<c:out value="${helpDesk.id}"/>');" />
										<spring:message code="LABEL_EDIT" text="Edit" /></td>										
								<%-- 	</authz:authorize> --%>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				
				
			</div>
			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>
	<script type="text/javascript">
			var mobileNumberlen='${mobilenumberLength}';	 
	</script>
	</body>
</html>
