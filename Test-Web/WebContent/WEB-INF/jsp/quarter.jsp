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

		var quarter = document.getElementById("quarter").value;
		var description = document.getElementById("description").value.trim();
		var pattern = '^\[a-zA-ZÀ-ÿ0-9-\' ]{1,30}$';
		var regex = /^[a-zA-Z ]*$/;

		if (quarter == "") {
			alert("<spring:message code='LABEL.QUARTER.NOTEMPTY' text='Please enter City/County Name'/>");
			return false;
		} else if (quarter.charAt(0) == "0") {
			alert("<spring:message code='LABEL.QUARTER.VALID' text='Please enter a valid City/County'/>");
			return false;
		} else if (!regex.test(quarter)) {

			alert("<spring:message code='LABEL.QUARTER.NONUMERIC'  text='Please enter alphabetic characters only in City/County Name'/>")
			return false;
		}

	/* 	else if (quarter.charAt(0) == " "
				|| quarter.charAt(quarter.length - 1) == " ") {
			alert("<spring:message code='LABEL.QUARTER.SPACE' text='Please remove unwanted spaces in City/County Name'/>");
			return false;
		} */ else if (quarter.search(pattern) == -1) {
			alert("<spring:message code='LABEL.QUARTER.DIGITS' text='Please enter City/County Name with out any special characters'/>");
			return false;
		} /* else if (quarter.length > 32) {
			alert("<spring:message code='LABEL.QUARTER.LENGTH' text='City/County Name should not exceed more than 32 characters'/>");
			return false;
		} */ else if (description == "") {
			alert("<spring:message code='LABEL.DESC.NOTEMPTY' text='Please enter Description'/>");
			return false;
		}/*  else if (description.charAt(0) == " "
				|| description.charAt(description.length - 1) == " ") {
			alert('<spring:message code="LABEL.DESCRIPTION.BLANK" text="Please remove the white space from description"/>');
			return false;
		}else if(!regex.test(description)){
			alert("Please enter alphabetic characters only in Description")
		}else if (description.length > 180) {
			alert('<spring:message code="LABEL.DESCRIPTION.LENGTH" text="Description should not exceed more than 180 characters"/>');
			return false;
		} */ else {
			document.quarterForm.action = "saveQuarters.htm";
			document.quarterForm.submit();
		}
	}
	function cancelForm() {
		document.quarterForm.action = "viewQuarters.htm";
		document.quarterForm.submit();
	}

	function textCounter(field, cntfield, maxlimit) {
		if (field.value.length > maxlimit) // if too long...trim it!
			field.value = field.value.substring(0, maxlimit);
		// otherwise, update 'characters left' counter
		else
			cntfield.value = maxlimit - field.value.length;
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	function quarterDetail(url,quarterId,cityId){
		document.getElementById('cityId').value=cityId;
		document.getElementById('quarterId').value=quarterId;
		submitlink(url,'quarterForm');
	}
	
	function viewCity(url,countryId){
		document.getElementById('countryId').value=countryId;
		submitlink(url,'quarterForm');
	}	
	
</script>
</head>
<body>
	<form:form name="quarterForm" id="quarterForm" class="form-inline"
		action="saveQuarters.htm" method="post" commandName="quarterDTO">
		<jsp:include page="csrf_token.jsp"/>
		<input type="hidden" name="countryId" id="countryId" value="${city.countryId}"/>
		<div class="col-md-12">
			
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">
							<span><spring:message code="TITLE_QUARTERS"
									text="Quarters" />-<c:out value="${city.city}" /></span>
						</h3>
						<br/>
						
		           <%--  <label class="col-sm-offset-9"><a
									href="javascript:submitForm('quarterForm','viewCities.htm?countryId=${city.countryId}')"><spring:message
											code="LABEL_VIEWCITIES" text="View Cities" /> </a></label> --%>
					
					 <label class="col-sm-offset-9"><a
					href="javascript:viewCity('viewCities.htm','<c:out value="${city.countryId}" />')"><spring:message
																code="LABEL_VIEWCITIES" text="View Cities" /> </a></label>					
											
					</div>
					
					<br/>
					
					<div class="col-md-3 col-md-offset-4">
								<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
									<spring:message code="${message}" text="" />
								</div>
							</div>
					<!-- /.box-header -->
					<div class="box-body">
						<!-- form start -->
						<authz:authorize ifAnyGranted="ROLE_addQuarterAdminActivityAdmin">
						<div class="row">
						<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 15px; margin-top:9px;">
								
						<div class="row">
							<form:hidden path="cityId" />
							<form:hidden path="quarterId" />
							
							<div class="col-md-12" style="margin-top:15px;">
								<label class="col-sm-4"><spring:message
										code="LABEL_QUARTERNAME" text="Quarter Name" /><font
									color="red">*</font></label>
								<form:input path="quarter" cssClass="form-control" />
								<FONT color="red">
								<form:errors path="quarter" /></FONT>
							</div>
						</div>
						<br />
						<div class="row">
							<div class="col-md-12">
								<label class="col-sm-4"><spring:message
										code="LABEL_DESC" text="Description" /><font color="red">*</font></label>
								<form:textarea path="description"
									onKeyDown="textCounter(document.quarterForm.description,180,180)"
									onKeyUp="textCounter(document.quarterForm.description,180,180)" />
								<FONT color="red">
								<form:errors path="description" /></FONT>
							</div>
						</div>
					<c:choose>
						<c:when test="${(quarterDTO.quarterId eq null) }">
							<c:set var="buttonName" value="LABEL_ADD" scope="page" />
						</c:when>
						<c:otherwise>
							<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
						</c:otherwise>
					</c:choose>
					<div class="col-sm-6 col-sm-offset-8">
							<input type="button" class="btn-primary btn" id="submitButton"
							value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
							onclick="validate();"></input>
							<c:if test="${quarterDTO.quarterId ne null}">
							<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
								onclick="cancelForm();"></input>
						</c:if><br />
				</div>
				</div>
				</div>
					</authz:authorize>
				</div>
				</div>
		
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_QUARTERNAME"
										text="Quarter Name" /></th>
								<th><spring:message code="LABEL_DESCRIPTION"
										text="Description" /></th>
								<authz:authorize
									ifAnyGranted="ROLE_editQuarterAdminActivityAdmin">
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</authz:authorize>
							</tr>
						</thead>
						<tbody>
								<c:forEach items="${page.results}" var="quarter">
									<tr>
										<td><c:out value="${quarter.quarter}" /></td>
										<td><c:out value="${quarter.description}" /></td>
										<authz:authorize
											ifAnyGranted="ROLE_editQuarterAdminActivityAdmin">
											<td>
											<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
											<%-- <a
												href="javascript:submitForm('quarterForm','editQuarter.htm?quarterId=<c:out value="${quarter.quarterId}"/>&cityId=<c:out value="${quarter.city.cityId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" />
											</a> --%> <a
												href="javascript:quarterDetail('editQuarter.htm','<c:out value="${quarter.quarterId}"/>','<c:out value="${quarter.city.cityId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" />
											</a> 
											<!-- @END --></td>
										</authz:authorize>
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
