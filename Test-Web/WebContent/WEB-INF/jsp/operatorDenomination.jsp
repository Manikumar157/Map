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
<script type="text/javascript">
	function validate() {

		var denomination = document.getElementById("denomination").value;
		var numPattern = '^[0-9]*$';

		if (denomination == "") {
			alert("<spring:message code="NotNull.operatorDenominationDTO.denomination" text="Please enter Denomination"/>");
			return false;
		} else if (document.denominationForm.denomination.value.charAt(0) == "0") {
			alert("<spring:message code="LABEL.DENOMINATION.VALID" text="Please enter a valid denomination"/>");
			return false;
		} else if (document.denominationForm.denomination.value
				.search(numPattern) == -1) {
			alert("<spring:message code="LABEL.DENOMINATION.NUMBER" text="Please enter Denomination only in digits"/>");
			return false;
		} else {
			document.denominationForm.action ="saveDenomination.htm";
			document.denominationForm.submit();
		}
	}
	function cancelForm() {
		document.denominationForm.action = "viewDenominations.htm?pageNumber=1";
		document.denominationForm.submit();
	}
	function denominationDetail(url,denominationId){
		document.getElementById('denominationId').value=denominationId;
		submitlink(url,'denominationForm');
	}
</script>

</head>
<body>
	<form:form name="denominationForm" id="denominationForm" class="form-inline"
		action="saveDenomination.htm" method="post"
		commandName="operatorDenominationDTO">
		<jsp:include page="csrf_token.jsp"/>
		<div class="col-md-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><c:out value="${operatoName.operatorName}"></c:out>-<spring:message
								code="TITLE_OPERATORDENOMIN" text="Denominations" /></span>
					</h3>
				<div class="col-md-6 col-md-offset-4">
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
				</div>
				<!-- /.box-header -->
				<div class="box-body" style="height:300px;">
				<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 15px;"><br />
					<!-- form start -->
						<div class="col-md-9 col-md-offset-7">
							<label class="col-sm-7"> <a href="javascript:submitForm('denominationForm','showOperators.htm')">
									<spring:message code="LINK_VIEW_OPERATOR" text="View Operators" />
							</a></label>
						</div>
					<div class="row">
						<form:hidden path="operatorId" />
						<form:hidden path="denominationId" />
						<div class="col-md-12">
							<label class="col-sm-5"><spring:message
									code="LABEL_DENOMINATION" text="Denomination" /><font
								color="red">*</font></label>
							<form:input path="denomination" cssClass="form-control" maxlength="9"/>
							<FONT color="red">
							<form:errors path="denomination" /></FONT>
						</div>
					</div>
					<br />
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-5"><spring:message
									code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
							<form:radiobutton path="active" value="1" label="Active"></form:radiobutton>
							&nbsp; &nbsp;
							<form:radiobutton path="active" value="0" label="Inactive"></form:radiobutton>
						</div>
					</div>
				
				<c:choose>
					<c:when test="${(operatorDenominationDTO.denominationId eq null) }">
						<c:set var="buttonName" value="LABEL_ADD" scope="page" />
					</c:when>
					<c:otherwise>
						<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
					</c:otherwise>
				</c:choose>
				<div class="col-md-6 col-md-offset-7">
					<input class="btn btn-primary" type="button"
						id="submitButton"
						value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
						onclick="validate();"></input>
					<div class="space"></div>
					<c:if test="${operatorDenominationDTO.denominationId ne null}">
						<input class="btn btn-default" type="button"
							value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
							onclick="cancelForm();" />
					</c:if>
				</div>
				</div>
				</div>
			</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_DENOMINATION"
										text="Denomination" /></th>
								<th><spring:message code="LABEL_STATUS" text="Status" /></th>
								<th><spring:message code="LABEL_ACTION" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="denomination">
								<tr>
									<td><c:out value="${denomination.denomination}" /></td>
								
								<td>
									<c:if test="${denomination.active==1}">
										<c:set var="status" value="Active"></c:set>
									</c:if>
									<c:if test="${denomination.active==0}">
										<c:set var="status" value="Inactive"></c:set>
									</c:if>
									<c:out value="${status}" /></td>
									<td><a
										href="javascript:denominationDetail('editDenomination.htm?pageNumber=1','<c:out value="${denomination.denominationId}"/>')">
											<spring:message code="LABEL_VOUCHER_EDIT" text="EDIT" />
									</a></td>
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
