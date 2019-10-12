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
		var currencyName = document.getElementById("currencyName").value;
		var currencyCode = document.getElementById("currencyCode").value;
		var currencyCodeNumeric = document
				.getElementById("currencyCodeNumeric").value;
		var pattern = '^\[a-zA-ZÀ-ÿ\' ]*$';
		var numericPattern = '^[0-9]*$';
		var codePattern = '^\[A-Za-z]*$';

		if (currencyName == "") {
			alert("<spring:message code='NotEmpty.currencyDTO.currencyName' text='Please enter Currency Name'/>");
			document.currencyForm.currencyName.focus();
			return false;
		} else if (currencyName.charAt(0) == " "
				|| currencyName.charAt(currencyName.length - 1) == " ") {
			alert("<spring:message code='LABEL.CURRENCY.SPACE' text='Please remove unwanted spaces in Currency Name'/>");
			document.currencyForm.currencyName.focus();
			return false;
		} else if (currencyName.search(pattern) == -1) {
			alert("<spring:message code='LABEL.CURRENCY.DIGITS' text='Please enter currency name in alphabets'/>");
			document.currencyForm.currencyName.focus();
			return false;
		} else if (currencyName.length > 30) {
			alert("<spring:message code='LABEL.CURRENCY.LENGTH' text='Currency Name should not exceed more than 30 characters'/>");
			document.currencyForm.currencyName.focus();
			return false;
		} else if (currencyCode == "") {
			alert("<spring:message code='NotEmpty.currencyDTO.currencyCode' text='Please enter Currency Code'/>");
			document.currencyForm.currencyCode.focus();
			return false;
		} else if (currencyCode.length > 3) {
			alert("<spring:message code='LABEL.CURRENCYCODE.LENGTH' text='Please enter Currency Code maximum of 3 characters'/>");
			document.currencyForm.currencyCode.focus();
			return false;
		} else if (currencyCode.search(codePattern) == -1) {
			alert("<spring:message code='LABEL.CURRENCYCODE.CHARACTERS' text='Please enter Currency Code only in alphabets'/>");
			document.currencyForm.currencyCode.focus();
			return false;
		} else if (currencyCodeNumeric == "") {
			alert("<spring:message code='NotNull.currencyDTO.currencyCodeNumeric' text='Please enter Currency Code Numeric'/>");
			document.currencyForm.currencyCodeNumeric.focus();
			return false;
		} else if (currencyCodeNumeric.search(numericPattern) == -1) {
			alert("<spring:message code='LABEL.CURRENCYCODENUMERIC.DIGITS' text='Please enter Currency Code Numeric only in digits'/>");
			document.currencyForm.currencyCodeNumeric.focus();
			return false;
		} else if (currencyCodeNumeric.length > 3) {
			alert("<spring:message code='LABEL.CURRENCYCODENUMERIC.LENGTH' text='Please enter Currency Code Numeric maximum of 3 digits'/>");
			document.currencyForm.currencyCodeNumeric.focus();
			return false;
		} else {
			document.currencyForm.action = "saveCurrency.htm";
			document.currencyForm.submit();
		}
	}
	function cancelForm() {
		document.currencyForm.action = "showCurrencies.htm";
		document.currencyForm.submit();
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	 function currencyDetail(currencyId){
		document.getElementById('currencyId').value=currencyId;
		submitlink('editCurrency.htm','currencyForm');
	}
	//@End
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>

</head>
 <body>
	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_CURRENCY" text="Currency" /></span>
				</h3>
			</div>
			<br />
			<div class="col-md-6 col-md-offset-5">
			<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
			</div>
			<!-- /.box-header -->
			<authz:authorize ifAnyGranted="ROLE_addCurrencyAdminActivityAdmin" ifNotGranted="ROLE_bankadmin">
			<div class="box-body" style="height:300px;">
			<div class="col-sm-3"></div>
				<!-- form start -->
				<div class="col-sm-6 table_border" ><!-- style="border: 1px solid;border-radius: 15px;" -->
				<form:form name="currencyForm" id="currencyForm" action="saveCurrency.htm"
						method="post" commandName="currencyDTO" class="form-inline">
						<jsp:include page="csrf_token.jsp"/>
						<br/>
					<div class="row">
						<form:hidden path="currencyId" />
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_CURRENCY_NAME" text="Currency Name"></spring:message><font
								color="red">*</font></label>
							<form:input path="currencyName" cssClass="form-control"
								maxlength="30" />
							<FONT color="red">
							<form:errors path="currencyName" /></FONT>
						</div>
					</div>
					<br />
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_CURRENCY_CODE" text="Currency Code"></spring:message><font
								color="red">*</font></label>
							<form:input path="currencyCode" cssClass="form-control"
								maxlength="3" />
							<FONT color="red">
							<form:errors path="currencyCode" /></FONT>
						</div>
					</div>
					<br/>
						<div class="row">
							<div class="col-md-12">
								<label class="col-sm-6"><spring:message
										code="LABEL_CURRENCY_CODE_NUMERIC"
										text="Currency Code Numeric"></spring:message><font
									color="red">*</font></label>
								<form:input path="currencyCodeNumeric" cssClass="form-control"
									maxlength="3" />
								<FONT color="red">
								<form:errors path="currencyCodeNumeric" /></FONT>
							</div>
						</div> <!-- /.box-body -->

						<div class="box-footer">
							<c:choose>
								<c:when test="${(currencyDTO.currencyId eq null) }">
									<c:set var="buttonName" value="LABEL_ADD" scope="page" />
								</c:when>
								<c:otherwise>
									<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
								</c:otherwise>
							</c:choose>
							<input type="button" id="submitButton"
								value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
								onclick="validate();" class="btn btn-primary pull-right" ></input>
							<c:if test="${currencyDTO.currencyId ne null}">
								<input type="button" style="margin-right:20px;"
									value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
									onclick="cancelForm();" class="btn btn-primary pull-right"/><br><br>
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
								<th><spring:message code="LABEL_CURRENCY_NAME"
										text="Currency Name" /></th>
								<th><spring:message code="LABEL_CURRENCY_CODE"
										text="Currency Code" /></th>
								<th><spring:message code="LABEL_CURRENCY_CODE_NUMERIC" text="Code Numeric"/></th>
								<authz:authorize
									ifAnyGranted="ROLE_editCurrencyAdminActivityAdmin">
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</authz:authorize>
							</tr>
						</thead>
						<tbody>
							<%
								int i = 0;
							%>
							<c:forEach items="${page.results}" var="currency">
								<tr>
									<td><c:out value="${currency.currencyName}" /></td>
									<td><c:out value="${currency.currencyCode}" /></td>
									<td><c:out value="${currency.currencyCodeNumeric}" /></td>
									<authz:authorize ifAnyGranted="ROLE_editCurrencyAdminActivityAdmin">
										<td><%-- <a href="javascript:submitForm('currencyForm','editCurrency.htm?currencyId=<c:out value="${currency.currencyId}"/>')">
												<spring:message code="LABEL_EDIT" text="Edit" /> --%>
												<a href="javascript:currencyDetail('<c:out value="${currency.currencyId}"/>');" />
										<spring:message code="LABEL_EDIT" text="Edit" /></td>
										</a></td>
									</authz:authorize>
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
	</body>
</html>
