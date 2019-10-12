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
		var bankGroupName = document.getElementById("bankGroupName").value;
		var bankGroupShortName = document.getElementById("bankGroupShortName").value;
		var pattern = '^\[a-zA-ZÀ-ÿ0-9-\ ]*$';
		var shortNamePattern = '^\[a-zA-Z]*$';
		var bankGroup = bankGroupShortName.length;
		var str = bankGroupShortName.split(" ");

		if (bankGroupName == "") {
			alert("<spring:message code='NotEmpty.bankGroupDTO.bankGroupName' text='please enter Bank Group Name'/>");
			return false;
		} else if (bankGroupName.charAt(0) == " "
				|| bankGroupName.charAt(bankGroupName.length - 1) == " ") {
			alert("<spring:message code='LABEL.BANKGROUP.SPACE' text='Please remove unwanted spaces'/>");
			return false;
		} else if (bankGroupName.search(pattern) == -1) {
			alert("<spring:message code='LABEL.BANKGROUP.SPECIAL' text='Please enter Bank Group Name without any special characters'/>");
			return false;
		} else if (bankGroupName.length > 100) {
			alert("<spring:message code='LABEL.BANKGROUP.LENGTH' text='BankGroupName should not exceed more than 100 characters'/>");
			return false;
		} else if (bankGroupShortName == "") {
			alert("<spring:message code='NotEmpty.bankGroupDTO.bankGroupShortName' text='please enter Bank Group Short Name'/>");
			return false;
		} else if (bankGroupShortName.charAt(0) == " "
				|| bankGroupShortName.charAt(bankGroupShortName.length - 1) == " ") {
			alert("<spring:message code='LABEL.BANKGROUPSHORT.SPACE' text='Please remove space in Bank Group Short Name'/>");
			return false;
		} else if (!checkSpace(bankGroupShortName)) {
			alert("<spring:message code='LABEL.BANKGROUPSHORT.SPACE' text='Please remove space in Bank Group Short Name '/>");
			return false;
		} else if (bankGroupShortName.search(shortNamePattern) == -1) {
			alert("<spring:message code='LABEL.BANKGROUPSHORT.SPECIAL' text='Please enter Bank Group Short Name only in characters'/>");
			return false;
		} else {
			document.bankGroupForm.action = "saveBankGroup.htm";
			document.bankGroupForm.submit();
		}
	}
	function cancelForm() {
		document.bankGroupForm.action = "showBankGroup.htm";
		document.bankGroupForm.submit();
	}
	function checkSpace(bankGroupShortName) {
		var count = 0;
		for ( var i = 0; i < bankGroupShortName.length; i++) {
			if (bankGroupShortName.charAt(i) == " ") {
				count++;
			}
		}
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	function bangGroupDetail(url,bankGroupId){
		document.getElementById('bankGroupId').value=bankGroupId;
		submitlink(url,'bankGroupForm');
	}
	//@End
	
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>

</head>
<body>

	<div class="col-lg-12">
		<form:form class="form-inline" name="bankGroupForm" id="bankGroupForm"
			action="saveBankGroup.htm" method="post" commandName="bankGroupDTO">
			<jsp:include page="csrf_token.jsp"/>
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<c:if test="${bankGroupDTO.bankGroupId ne null}">
							<span> <spring:message code="TITLE_BANKGROUP"
									text="Bank Group" />-<c:out
									value="${bankGroupDTO.bankGroupName}" />
							</span>
						</c:if>
						<c:if test="${bankGroupDTO.bankGroupId eq null}">
							<span> <spring:message code="TITLE_BANKGROUPS"
									text="Bank Groups" />
							</span>
						</c:if>
					</h3>
				</div>
				<div class="col-md-5 col-md-offset-10">
								<c:if test="${bankGroupDTO.bankGroupId ne null}">
									<a href="javascript:submitForm('bankGroupForm','showBankGroup.htm')"><span style="margin-left: -50px;"><strong><spring:message
											code="TITLE_BANKGROUPS" text="Bank Groups" ></spring:message></strong></a>
											
											
								</c:if>
							</div>
				<br />
				<div class="col-md-3 col-md-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>

				<div class="box-body" style="height:230px;">
				<authz:authorize
						ifAnyGranted="ROLE_addBankGroupsAdminActivityAdmin">
				<div class="col-sm-6 col-md-offset-3 table_border"> <!-- style="border: 1px solid;border-radius: 15px;" -->
					
						
							
							<form:hidden path="bankGroupId" />
							<div class="col-sm-12" style="margin-top:20px">
								<label class="col-sm-6" style="margin-top: 4px;"><spring:message
										code="LABEL_BANKGROUPNAME" text="Bank Group Name" /><font
									color="red">*</font></label>
								<form:input path="bankGroupName" cssClass="form-control"
									maxlength="50" />
								<FONT color="red"> <form:errors path="bankGroupName" /></FONT>
							</div>
						<div class="col-sm-12">
							<label class="col-sm-6" style="margin-top: 4px;"><spring:message
									code="LABEL_BANKGROUPSHORTNAME" text="Bank Group Short Name" /><font
								color="red">*</font></label>
							<form:input path="bankGroupShortName" cssClass="form-control"
								maxlength="10" />
							<FONT color="red"> <form:errors path="bankGroupShortName" /></FONT>
						</div>
						<c:choose>
							<c:when test="${(bankGroupDTO.bankGroupId eq null) }">
								<c:set var="buttonName" value="LABEL_ADD" scope="page" />
							</c:when>
							<c:otherwise>
								<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
							</c:otherwise>
						</c:choose>
						<div class="col-sm-6 col-sm-offset-8">
						<div class="btn-toolbar">
							<input type="button" class="btn-primary btn" id="submitButton"
							value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
							onclick="validate();"></input>
							<c:if test="${bankGroupDTO.bankGroupId ne null}">
							<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
								onclick="cancelForm();"></input>
							</c:if>
						</div>
						</div>
					</authz:authorize>
				</div>
				</div>
			</div>
		<c:if test="${(bankGroupDTO.bankGroupId eq null) }">
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<c:if test="${bankGroupDTO.bankGroupId ne null}">
							<thead>
								<tr>
									<th><spring:message code="LABEL_BANK" text="Bank" /></th>
									<th><spring:message code="LABEL_BANKCODE" text="Bank Code" /></th>
									<th><spring:message code="LABEL_COUNTRY" text="Country" /></th>
									<th><spring:message code="LABEL_STATUS" text="Status" /></th>
								</tr>
							</thead>
							<tbody>
								<%
									int i = 0;
								%>
								<%-- <c:forEach items="${page.results}" var="bankGroup">
									<tr>
										<td><c:out value="${bankGroup.bankName}" /></td>
										<td><c:out value="${bankGroup.bankCode}" /></td>
										<td><c:out value="${bankGroup.country.country}" /></td>
										<c:if test="${bankGroup.status==1}">
											<c:set var="status">
												<spring:message code="LABEL_ACTIVE" text="Active" />
											</c:set>
										</c:if>
										<c:if test="${bankGroup.status==0}">
											<c:set var="status">
												<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
											</c:set>
										</c:if>
										<td><c:out value="${status}" /></td>
									</tr>
								</c:forEach> --%>
							</tbody>
						</c:if>
						<c:if test="${bankGroupDTO.bankGroupId eq null}">
							<thead>
								<tr>
									<th><spring:message code="LABEL_BANKGROUPNAME"
											text="Bank Group Name" /></th>
									<th><spring:message code="LABEL_BANKGROUPSHORTNAME"
											text="Bank Group Short Name" /></th>
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.results}" var="bankGroup">
									<tr>
										<td align="left" style="padding-right: 3px;"><c:out
												value="${bankGroup.bankGroupName}" /></td>
										<td align="center"><c:out
												value="${bankGroup.bankGroupShortName}" /></td>
										<authz:authorize
											ifAnyGranted="ROLE_editBankGroupsAdminActivityAdmin">
											<td align="center">
											<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
											<%-- <a
												href="javascript:submitForm('bankGroupForm','editBankGroup.htm?bankGroupId=<c:out value="${bankGroup.bankGroupId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" />
											</a> --%>
											<a
												href="javascript:bangGroupDetail('editBankGroup.htm','<c:out value="${bankGroup.bankGroupId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" />
											</a>
											</td>
										</authz:authorize>
										<authz:authorize
											ifNotGranted="ROLE_viewBankGroupsAdminActivityAdmin">
											<td align="center"><%-- <a
												href="javascript:submitForm('bankGroupForm','editBankGroup.htm?bankGroupId=<c:out value="${bankGroup.bankGroupId}"/>')">
													<spring:message code="LABEL_VIEW" text="View" />
											</a> --%>
											<a
												href="javascript:bangGroupDetail('editBankGroup.htm','<c:out value="${bankGroup.bankGroupId}"/>')">
													<spring:message code="LABEL_VIEW" text="View" />
											</a>
											<!-- End -->
											</td>
										</authz:authorize>
									</tr>
								</c:forEach>
							</tbody>
						</c:if>
					</table>
				</div>
			</div>
			</c:if>

		</form:form>
	</div>
</body>
</html>
