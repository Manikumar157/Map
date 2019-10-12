<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<script type="text/javascript">
	function cancelForm() {
		document.operatorForm.action = "showOperators.htm";
		document.operatorForm.submit();
	}

	function disableSelect() {
		document.getElementById('bankId').disabled = true;
	}

    function searchOperator() {
	
     
	document.operatorForm.action = "searchOperators.htm";
	document.operatorForm.submit();
	
}
  //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	function operatorDetail(url,operatorId){
		document.getElementById('operatorId').value=operatorId;
		submitlink(url,'operatorForm');
	}
	//@end

	</script>
<%-- <title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title> --%>

</head>
<body>
	<form:form class="form-inline" name="operatorForm" id="operatorForm"
		action="saveOperator.htm" method="post" commandName="operatorDTO">
		<jsp:include page="csrf_token.jsp"/>
		<form:hidden path="countryId" value="1" id="countryId" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_OPERATOR" text="Operator" /></span>
					</h3>
				</div>
				<br />
				<div class="col-md-3 col-md-offset-10">
				<authz:authorize ifAnyGranted="ROLE_addCustomerAdminActivityAdmin,ROLE_addBusinessPartnerAdminActivityAdmin">
				 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_OPERATOR" text="Search"/>"
							onclick="javascript:submitForm('operatorForm','addOperators.htm')" /> 
				</authz:authorize>
					<%-- <a href="javascript:submitForm('operatorForm','addOperators.htm')" onclick="removeMsg()"><strong><spring:message
								code="LABEL_ADD_OPERATOR" text="Add Operator"></spring:message></strong></a> --%>
				</div>
				<br />
				<br />
				<div class="col-md-3 col-md-offset-4">
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
				<div class="box-body">
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_OPERATORNAME" text="Operator Name" /></label>
							<form:input path="operatorName" id="operatorName"
								cssClass="form-control" maxlength="30" />
							<FONT color="red">
							<form:errors path="operatorName" /></FONT>
						</div>
					<!-- 	@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
						<form:hidden path="operatorId" />
						<!-- @End -->
								<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_STATUS" text="Status" /></label>
							<form:select path="active" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="---Please Select---" />
								</form:option>
								<form:option value="1">
									<spring:message code="LABEL_ACTIVE" text="Active" />
								</form:option>
								<form:option value="0">
									<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
								</form:option>
							</form:select>
						</div>
								
							<%-- 	commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018	
								 <select class="dropdown chosen-select" id="countryId" name="countryId">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${countryList}" var="country">
										<c:forEach items="${country.countryNames}" var="cNames">
											<c:if test="${cNames.comp_id.languageCode==lang }">
												<option value="<c:out value="${country.countryId}"/>"
													<c:if test="${country.countryId eq operatorDTO.countryId}" >selected=true</c:if>>
													<c:out value="${cNames.countryName}" />
												</option>
											</c:if>
										</c:forEach>
									</c:forEach>
								</option>
							</select> --%>
					</div>
					<div class="row">
					<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY" text="Country:" /></label>
								<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="South Sudan" /></label></div>
						<%-- <div class="col-sm-6">
							<label class="col-sm-4"><spring:message code="LABEL_BANK"
									text="Bank" /></label>
							<form:select path="bankId" class="dropdown chosen-select" id="bankId">
								<form:option value="" selected="selected">
									<spring:message code="LABEL_WUSER_SELECT"
										text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${bankList}" itemValue="bankId"
									itemLabel="bankName"></form:options>
							</form:select>
							<font color="RED">
							<form:errors path="bankId" cssClass="" /></font>
						</div> --%>
						
					</div>
					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							id="searchButton"
							value="<spring:message code="LABEL_SEARCH" text="Search"/>"
							onclick="searchOperator();" style="margin-right: 60px;" />
					</div><br />
				</div>
			</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_OPERATORNAME"
										text="Operator Name" /></th>
								<th><spring:message code="LABEL_OPERATOR_ID"
										text="Operator ID" /></th>
								<th><spring:message code="LABEL_COUNTRY" text="Country" /></th>
								<th><spring:message code="LABEL_BANK" text="Bank" /></th>
								<th><spring:message code="LABEL_COMMISSION"
										text="Commission" /></th>
								<th><spring:message code="LABEL_CREATEDDATE"
										text="Created Date" /></th>
								<th><spring:message code="LABEL_STATUS" text="Status" /></th>
								<authz:authorize ifAnyGranted="ROLE_addCustomerAdminActivityAdmin,ROLE_addBusinessPartnerAdminActivityAdmin">
								<th><spring:message code="LABEL_ACTION" text="Action" /></th></authz:authorize>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="operator">
								<tr>
									<td><c:out value="${operator.operatorName}" /></td>
									<td><c:out value="${operator.operatorId}" /></td>
									<td>	<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${operator.country.countryNames}"
											var="cname">
											<c:if test="${cname.comp_id.languageCode==lang }">
												<c:out value="${cname.countryName}" />
											</c:if>
										</c:forEach></td>
									<td><c:out value="${operator.bank.bankName}" /></td>
									<td><c:out value="${operator.commission}" /></td>
									<td><c:out value="${operator.createdDate}" /></td>
									<c:if test="${operator.active==1}">
										<c:set var="status">
											<spring:message code="LABEL_ACTIVE" text="Active" />
										</c:set>
									</c:if>
									<c:if test="${operator.active==0}">
										<c:set var="status">
											<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
										</c:set>
									</c:if>
									<td><c:out value="${status}" /></td>

									<authz:authorize ifAnyGranted="ROLE_addCustomerAdminActivityAdmin,ROLE_addBusinessPartnerAdminActivityAdmin">
									<td><!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  -->
									<%-- <a
										href="javascript:submitForm('operatorForm','editOperator.htm?operatorId=<c:out value="${operator.operatorId}"/>')">
											<spring:message code="LABEL_EDIT" text="Edit" />
									</a> --%>
									
									<a
										href="javascript:operatorDetail('editOperator.htm','<c:out value="${operator.operatorId}"/>')">
											<spring:message code="LABEL_EDIT" text="Edit" />
									</a>
									<!-- End -->
									</td></authz:authorize>
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
