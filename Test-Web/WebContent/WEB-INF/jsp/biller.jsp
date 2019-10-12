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
		var pattern = '^\[a-zA-ZÀ-ÿ-\' 0-9 ]*$';
		var whitespaceChars = " \t\n\r\f";
		var billerName = document.getElementById("billerName").value;
		var billerType = document.getElementById("billerType").value;
		var bankId = document.getElementById("bankId").value;
		var country = document.getElementById("countryId").value;
		 if(!billerName.match(/^[a-zA-Z0-9]*$/ ))
		 {		alert("<spring:message code='ALERT_BILLER_NAME_VALIDATION' text='Biller Name should be alphanumeric value'/>");
			return false;
			} 
		if (billerName == "") {
			alert("<spring:message code='NotEmpty.billerDTO.billerName' text='Please enter BillerName'/>");
			document.operatorForm.operatorName.focus();
		} else if (billerName.charAt(0) == " "
				|| billerName.charAt(billerName.length - 1) == " ") {
			alert("<spring:message code='LABEL.BILLERNAME.SPACE' text='Please remove unwanted spaces in Biller Name'/>");
			return false;
		} else if (billerName.search(pattern) == -1) {
			alert("<spring:message code='LABEL.BILLERNAME.DIGITS' text='Please enter Biller Name with out any special characters'/>");
			return false;
		} else if (billerName.length > 100) {
			alert("<spring:message code='LABEL.BILLERNAME.LENGTH' text='Biller Name should not exceed 100 characters'/>");
			return false;
		} else if (billerType == "") {
			alert("<spring:message code='NotNull.billerDTO.billerTypeId' text='Please select Biller Type'/>");
			return false;
		} else if (bankId == "") {
			alert("<spring:message code='NotNull.billerDTO.bankId' text='Please select bank'/>");
			return false;
		} else if (country == "") {
			alert("<spring:message code='NotNull.billerDTO.countryId' text='Please select Country'/>");
			return false;
		} else {
			document.billerForm.action = "saveBillersForm.htm";
			document.billerForm.submit();
		}
	}

	function cancelForm() {
		document.billerForm.action = "showBillersForm.htm";
		document.billerForm.submit();
	}

	function searchSubmit() {
		document.billerForm.method = "post";
		document.billerForm.action = "searchBillerPage.htm";
		document.billerForm.submit();
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	function billerDetail(url,billerId){
		document.getElementById('billerId').value=billerId;
		submitlink(url,'billerForm');
	}
	//@end
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>

</head>
<body>
	<form:form class="form-inline" id="billerForm" name="billerForm"
		action="saveBiller.htm" method="post" commandName="billerDTO">
		<jsp:include page="csrf_token.jsp"/>
		<form:hidden path="countryId" value="1" id="countryId" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="LABEL_BILLER" text="Biller" /></span>
					</h3>
				</div>
				<div class="col-md-6 col-md-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
				<div class="col-md-3 col-md-offset-10">
					<%-- <a href="javascript:submitForm('billerForm','showBillersForm.htm')" onclick="removeMsg()"><strong><spring:message
								code="LABEL_ADD_BILLER" text="Add Biller"></spring:message></strong></a> --%>
					 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_BILLER" text="Add Clearing House"/>"
							onclick="javascript:submitForm('billerForm','showBillersForm.htm')" />
				</div>
				<div class="box-body">
					<div class="row">
						<form:hidden path="billerId" />
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_BILLERNAME" text="Biller Name" /></label>
							<form:input path="billerName" id="billerName"
								cssClass="form-control" maxlength="100" />
							<font color="red"> <form:errors path="billerName" /></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_BILLERTYPE" text="Biller Type" /></label>
							<form:select path="billerTypeId" class="dropdown chosen-select" id="billerType">
								<form:option value="" selected="selected">
									<spring:message code="LABEL_WUSER_SELECT"
										text="--Please Select--"></spring:message>
									<form:options items="${billerTypeList}"
										itemValue="billerTypeId" itemLabel="billerType" />
								</form:option>

							</form:select>
							<font color="RED"> <form:errors path="billerTypeId"
								cssClass="" /></font>
						</div>
					</div>
					<div class="row">
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
							<font color="RED"> <form:errors path="bankId" cssClass="" /></font>
						</div> --%>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY" text="Country:" /></label> 
								<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="South Sudan" /></label>	
								
							<%-- 	commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018	
							<select class="dropdown chosen-select" id="countryId" name="countryId">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</option>
								<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang }">
											<option value="<c:out value="${country.countryId}"/>"
												<c:if test="${country.countryId eq billerDTO.countryId}" >selected=true</c:if>>
												<c:out value="${cNames.countryName}" />
											</option>
										</c:if>
									</c:forEach>
								</c:forEach>

							</select> <font color="red"> <form:errors path="countryId" /></font> --%>
						</div>
					</div>
					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							value="<spring:message code="LABEL_SEARCH" text="Search"/>"
							onclick="searchSubmit();" style="margin-right: 60px;"></input>
					</div><br/><br/>
				</div>
			</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_BILLERNAME"
										text="Biller Name" /></th>
								<th><spring:message code="LABEL_COUNTRY" text="Country" /></th>
								<th><spring:message code="LABEL_BANK" text="Bank" /></th>
								<th><spring:message code="LABEL_BILLERTYPE"
										text="Biller Type" /></th>
								<th><spring:message code="LABEL_PARTIAL_PAYMENTS"
										text="PARTIALPAYMENTS" /></th>
								<authz:authorize
									ifAllGranted="ROLE_editBillerAdminActivityAdmin">
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</authz:authorize>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="biller">
								<tr>
									<td><c:out value="${biller.billerName}" /></td>
									<td><c:set var="lang" value="${language}"></c:set> <c:forEach
											items="${biller.country.countryNames}" var="cname">
											<c:if test="${cname.comp_id.languageCode==lang }">
												<c:out value="${cname.countryName}" />
											</c:if>
										</c:forEach></td>

									<td><c:out value="${biller.bank.bankName}" /></td>

									<td><c:out value="${biller.billerType.billerType}" /></td>

									<c:if test="${biller.partialPayments==1}">
										<c:set var="status">
											<spring:message code="LABEL_YES" text="Yes" />
										</c:set>
									</c:if>
									<c:if test="${biller.partialPayments==0}">
										<c:set var="status">
											<spring:message code="LABEL_NO" text="No" />
										</c:set>
									</c:if>
									<td><c:out value="${status}" /></td>
									<authz:authorize
										ifAllGranted="ROLE_editBillerAdminActivityAdmin">
										<td>
										
									<!-- 	@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  -->
										<%-- <a
											href="javascript:submitForm('billerForm','editBiller.htm?billerId=<c:out value="${biller.billerId}"/>')">
												<spring:message code="LABEL_EDIT" text="Edit" />
										</a> --%>
										<a
											href="javascript:billerDetail('editBiller.htm','<c:out value="${biller.billerId}"/>')">
												<spring:message code="LABEL_EDIT" text="Edit" />
										</a>
										<!--@..End  -->
										</td>
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
