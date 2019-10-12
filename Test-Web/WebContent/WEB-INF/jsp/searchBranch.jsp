<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>

<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> 
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/css/ui-lightness/jquery-ui-1.8.14.custom.css"
	rel="stylesheet" type="text/css" />
<%-- <script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script> --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script> 
<script type="text/javascript">

	$(document).ready(function() {
		
		$("#cities").change(function() {
			$city = document.getElementById("cityId").value;
			if($city.length<1)
				$city=0;
			$csrfToken = $("#csrfToken").val();
			$.post("getQuartersList.htm", {
				city : $city,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("quarters").innerHTML = "";
				document.getElementById("quarters").innerHTML = data;
				setTokenValFrmAjaxResp();
				applyChosen();
				
			});
		});
       
			$("#quarters").change(function() {
             document.getElementById("quarterId").value = document.getElementById("quarter").value;
			 });							
													
       
	});

					

	function searchSubmit() {
		document.searchBranch.method = "post";
		document.searchBranch.action = "searchBranch.htm";
		document.searchBranch.submit();
	}
	
	function viewOrgnization(url,bankId,frmId){
		document.getElementById('bankId').value=bankId;
		submitlink(url,'searchBranch');
	}
	function viewBranch(url,bankId,countryId,frmId){
		document.getElementById('bankId').value=bankId;
		document.getElementById('countryId').value=countryId;
		submitlink(url,'searchBranch');
	}
	
</script>
<style>
form:select {
	width:161px;
	font-weight:lighter;
	height:30px;
}
form:options {
	width:161px;
	font-weight:lighter;
	height:30px;
}
.dropdown{
    font-size: 13px;
    font-family: Verdana;
    COLOR: #000000;
	width:161px;
	font-weight:lighter;
	height:30px;
}


</style>
</head>

<body>
	<div class="col-lg-12">
		<form:form class="form-inline" name="searchBranch" method="post" id="searchBranch"
			commandName="branchDTO">
			<jsp:include page="csrf_token.jsp" />
			<form:hidden path="branchId" />
			<form:hidden path="countryId" />
			<form:hidden path="bankId" />
			<%-- <form:hidden path="cityId" /> --%>
		<%-- 	<form:hidden path="quarterId" /> --%>
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_VIEW_BRANCH"
								text="View Branch Details" /> - <c:out
								value="${masterData.bank.bankName}" /></span>
					</h3>
				</div>
				<br />
				<div class="col-sm-6">
			<div class="col-sm-5 col-sm-offset-9" style="color: #ba0101;font-weight: bold;font-size: 12px;"><spring:message code="${message}" text=""/></div>
		</div>
				<div class="pull-right">
					<%-- <a
						href="javascript:submitForm('menuForm','editBankForm.htm?bankId=${masterData.bank.bankId}')"><b><spring:message
								code="LABEL_VIEW_BANK" text="View Bank"></spring:message></b></a> | <a
						href="javascript:submitForm('menuForm','AddBranchForm.htm?bankId=<c:out value="${bankId}"/>&countryId=<c:out value="${countryId}"/>')"><b><spring:message
								code="LABEL_ADD_BRANCH" text="Add Branch" /></b></a> &nbsp; &nbsp;
					&nbsp; --%>
					
					<a
						href="javascript:viewOrgnization('editBankForm.htm','<c:out value="${masterData.bank.bankId}"/>','searchBranch')"><b><spring:message
								code="LABEL_VIEW_BANK" text="View Bank"></spring:message></b></a> | <a
						href="javascript:viewBranch('AddBranchForm.htm','<c:out value="${bankId}"/>','<c:out value="${countryId}"/>','searchBranch')"><b><spring:message
								code="LABEL_ADD_BRANCH" text="Add Branch" /></b></a> &nbsp; &nbsp;
					&nbsp;
					
				</div>
				<br /> <br />
				<div class="box-body">
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-5" style="margin-top: 4px;"><spring:message
									code="LABEL_BRANCH_NAME" text="Brnach Name" /></label>
							<form:input path="location" id="location" cssClass="form-control"
								maxlength="32" style="width:180px;" />
							<font color="RED"> <form:errors path="location" /></font>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-5"><spring:message
									code="LABEL_BRANCHCODE" text="Branch Code" /></label>
							<form:input path="branchCode" cssClass="form-control"
								maxlength="5" style="width:180px;" />
							<FONT color="red"> <form:errors path="branchCode" /></FONT>
						</div>
					</div>
					<div class="row">
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_CITY"
								text="City:" /></label>
					<div id="cities">
								<form:select path="cityId" class="dropdown_big chosen-select" id="cityId">
									<option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</option>
									<form:options items="${masterData.cityList}" itemValue="cityId"
										itemLabel="city"></form:options>
								</form:select>
							</div>
						<font color="RED"> <form:errors path="cityId"></form:errors></font>
					 </div>
						<div class="col-sm-6">
							<label class="col-sm-5" style="margin-top: 4px;"><spring:message
									code="LABEL_QUARTER" text="Quarter" /></label>
									<div id ="quarters" style="font-weight:lighter;">
							<form:select path="quarterId" class="dropdown_big chosen-select"
								id="quarter">
								<option value="" style='width:161px; height:30px; font-weight:lighter;'>
									<spring:message code="LABEL_WUSER_SELECT"
										text="--Please Select--"></spring:message>
								</option>
								<form:options items="${quarterList}" itemValue="quarterId"
									itemLabel="quarter"></form:options>
							</form:select></div>
							<font color="RED"> <form:errors path="quarterId" /></font>
						</div>
					</div>
					<br />
					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							value="<spring:message code="LABEL_SEARCH" text="Search"/>"
							onclick="searchSubmit();"></input> <br /> <br />
					</div>
				</div>
				
			</div>

			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_BRANCHCODE"
										text="Branch Code" /></th>
								<th><spring:message code="LABEL_BRANCH_NAME"
										text="Branch Name" /></th>
								<th><spring:message code="LABEL_ADDRESS" text="Address" /></th>
								<th><spring:message code="LABEL_CITY" text="City" /></th>
								<th><spring:message code="LABEL_QUARTER" text="Quarter" /></th>
								<th><spring:message code="LABEL_ACTION" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="branch">
								<tr>
									<td><c:out value="${branch.branchCode}" /></td>
									<td><c:out value="${branch.location}" /></td>
									<td><c:out value="${branch.address}" /></td>
									<td><c:out value="${branch.city.city}" /></td>
									<td><c:out value="${branch.quarter.quarter}" /></td>
									<%-- <td><a
										href="javascript:submitForm('menuForm','editBranchDetails.htm?branchId=<c:out value="${branch.branchId}"/>&bankId=<c:out value="${bankId}"/>')" />
										<spring:message code="LABEL_BRANCH_EDIT" text="Edit" /></td> --%>
										
									<td><a href="javascript:editBranch('editBranchDetails.htm','<c:out value="${branch.branchId}"/>','<c:out value="${bankId}"/>')"><spring:message
										code="LABEL_BRANCH_EDIT" text="Edit"></spring:message>
											</a></td>
										
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</form:form>
	</div>
	<script type="text/javascript">
		var a;
		var arr = new Array();
		a = "<c:out value='${branchList}'/>";
		var c = a.substring(a.indexOf('[') + 1, a.lastIndexOf(']'));

		for ( var i = 0; i < c.split(",").length; i++) {
			//arr[i]="\""+c.split(",")[i]+"\"";
			arr[i] = c.split(", ")[i];
			//alert(arr[i])
		}
		$(document).ready(function() {
			$("#location").autocomplete({
				source : arr

			});
		});
		
		function editBranch(url,branchId,bankId){
			document.getElementById('branchId').value=branchId;
			document.getElementById('bankId').value=bankId;
			submitlink(url,'searchBranch');
		}	

		
	</script>
</body>
</html>
