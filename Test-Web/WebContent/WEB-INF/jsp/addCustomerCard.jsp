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
		var codePattern = '^\[0-9A-Za-z]*$';
		var numberPattern = '^\[0-9]*$';
		var cardNo = document.getElementById("cardNo").value;
		var cvv = document.getElementById("cvv").value;
		var cardExpiry = document.getElementById("cardExpiry").value;
		var namepattern = '^[a-zA-ZÀ-ÿ-\' ]*$';
		var accAlias = document.getElementById("alias").value;

		if (cardNo == "") {
			alert('<spring:message code="MOBILE_ID_NOT_EMPTY" text="Please enter Mobile ID"/>');
			return false;
		} else if (cardNo.length != 15) {
			alert('<spring:message code="INVALID_MOBILEID_LENGTH" text="Please enter valid Mobile ID of 15 characters"/>');
			return false;
		} else if (cardNo.search(codePattern) == -1) {
			alert('<spring:message code="INVALID_MOBILE_ID" text="Please enter valid Mobile ID"/>');
			return false;
		} else if (!checForAllZeros(document.getElementById("cardNo").value)) {
			alert('<spring:message code="INVALID_MOBILEID_LENGTH" text="Please enter valid Mobile ID of 15 characters"/>');
			return false;
		} else if (cvv == "") {
			alert('<spring:message code="CVV_EMPTY" text="Please enter CVV2"/>');
			return false;
		} else if (cvv.search(numberPattern) == -1) {
			alert('<spring:message code="INVALID_CVV" text="Please enter valid CVV2"/>');
			return false;
		} else if (!checForAllZeros(document.getElementById("cvv").value)) {
			alert('<spring:message code="INVALID_CVV" text="Please enter valid CVV2"/>');
			return false;
		} else if (cardExpiry == "") {
			alert('<spring:message code="CARD_EXPIRY_EMPTY" text="Please enter card expiry date"/>');
			return false;
		} else if (cardExpiry.search(numberPattern) == -1) {
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;
		} else if (cardExpiry.length != 4) {
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;
		} else if (cardExpiry.substring(2, 4) > 12
				|| cardExpiry.substring(2, 4) <= 00) {
			alert('<spring:message code="INVALID_CARD_EXPIRY" text="Please enter valid card expiry date"/>');
			return false;
		} else if (accAlias == "") {
			alert("<spring:message code="VALIDATION_CARDALIAS_EMPTY" text="Please enter card alias"/>");
			return false;
		} else if (accAlias.charAt(0) == " "
				|| accAlias.charAt(accAlias.length - 1) == " ") {
			alert("<spring:message code="VALIDATION_CARDALIAS_SPACE" text="Please Remove Unwanted Spaces in card alias"/>");
			return false;
		} else if (accAlias.search(namepattern) == -1) {
			alert("<spring:message code="VALIDATION_CARDALIAS_DIGITS" text="Please enter card alias with out any special characters"/>");
			return false;
		} else if (accAlias.length > 16) {
			alert("<spring:message code="VALIDATION_CARDALIAS_LENGTH" text="Card alias should not exceed more than 16 characters"/>");
			return false;
		} else {
			document.addCustomerCardForm.action = "saveCustomerCard.htm";
			document.addCustomerCardForm.submit();
		}
	}

	function checForAllZeros(value) {

		var count = 0;
		for (var i = 0; i < value.length; i++) {

			if (value.charAt(i) == 0) {
				count++;
			}
		}
		if (count == value.length) {
			return false;
		} else {
			return true;
		}

	}

	function cancelForm() {
		document.addCustomerCardForm.action = "addCustomerCard.htm";
		document.addCustomerCardForm.submit();
	}

	function disableSelect() {
		document.getElementById('branchId').disabled = true;
		document.getElementById('customerBankAccountNumber').disabled = true;
	}
</script>



</head>
<body>
	<div class="col-md-12">
		<div class="box" style="height: 430px;">
			<div class="box-header">
			<div style="float:left">
				<h3 class="box-title">
					<span><spring:message code="LABEL_ADD_CARD" text="Add Card" /></span>
				</h3></div>
				<div style="float:right;padding:15px;">
				<label> <a
								href="viewCustomer.htm?customerId=<c:out value="${cardDto.customerId}"/>"><spring:message
										code="LABEL_VIEW_CUSTOMER" text="View Customer" /> </a>
							</label></div>
			</div>
			<br />
			<div class="col-sm-6 col-sm-offset-3 table_border">
				<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
				<form:form name="addCustomerCardForm" action="saveCustomerCard.htm"
					method="post" commandName="cardDto">

					<div class="add_bank">
						<!-- form start -->
						<div class="col-md-12 view_cus" style="float:right;">
							
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"> <spring:message
									code="LABEL_MOBILE_ID" text="Mobile ID" /><font color="red">*</font>
							</label>
							<div class="col-md-6">
								<form:input path="cardNo" id="cardNo"
									cssClass="text_feild form-control" maxlength="15" />
								<FONT color="red">
								<form:errors path="cardNo" /></FONT>
							</div>
							<form:hidden path="customerId" />
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message code="LABEL_CVV2"
									text="CVV2" /><font color="red">*</font></label>
							<div class="col-md-6">
								<form:input path="cvv" id="cvv"
									cssClass="text_feild form-control" maxlength="30" />
								<FONT color="red">
								<form:errors path="cvv" /></FONT>
							</div>
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_CARD_EXPIRY_DATE" text="Expiry Date(YYMM)" /><font
								color="red">*</font></label>
							<div class="col-md-6">
								<form:input path="cardExpiry" id="cardExpiry"
									cssClass="text_feild form-control" maxlength="30" />
								<FONT color="red">
								<form:errors path="cardExpiry" /></FONT>
							</div>
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_CARD_ALIAS" text="Card Alias" /><font color="red">*</font></label>
							<div class="col-md-6">
								<form:input path="alias" id="alias"
									cssClass="text_feild form-control" maxlength="30" />
								<FONT color="red">
								<form:errors path="alias" /></FONT>
							</div>
						</div>
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
							<div class="col-md-6">
								<form:radiobutton path="status" value="2" label="Active"
									checked="true"></form:radiobutton>
								<form:radiobutton path="status" value="3" label="Inactive"></form:radiobutton>
								<FONT color="red">
								<form:errors path="alias" /></FONT>
							</div>
							<form:input path="cardId" type="hidden" />
						</div>
						<c:choose>
							<c:when test="${(cardDto.cardId eq null) }">
								<c:set var="buttonName" value="LABEL_ADD" scope="page" />
							</c:when>
							<c:otherwise>
								<script>
																disableSelect();
															</script>
								<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
							</c:otherwise>
						</c:choose>
						<div class="col-sm-6 col-sm-offset-9">
							<input type="button" id="submitButton" class="btn btn-primary"
								value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
								onclick="validate();" />
							<c:if test="${cardDto.cardId ne null}">
								<input type="button"
									value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
									onclick="cancelForm();" />
							</c:if>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div class="box">
			<div class="box-body table-responsive">
				<table id="example1" class="table table-bordered table-striped"
					style="text-align: center;">
					<tr class="tableheading">
						<th align="left" width="15%"><spring:message
								code="LABEL_CARD_ALIAS" text="Card Alias" /></th>
						<th align="center" width="15%"><spring:message
								code="LABEL_STATUS" text="Status" /></th>
						<th align="center" width="15%"><spring:message
								code="LABEL_EDIT" text="Edit" /></th>
					</tr>
					<%int i=0; %>
					<c:forEach items="${page.results}" var="customerCardAcc">

						<tr height="23px" class="" <% if(i%2!=0)%> bgColor="#d2d3f1"
							<% i++; %>>
							<td align="center"><c:out value="${customerCardAcc.alias}" /></td>

							<c:if test="${customerCardAcc.status==1}">
								<c:set var="status">
									<spring:message code="LABEL_ACTIVE" text="Active" />
								</c:set>
							</c:if>
							<c:if test="${customerCardAcc.status==2}">
								<c:set var="status">
									<spring:message code="LABEL_ACTIVE" text="Active" />
								</c:set>
							</c:if>
							<c:if test="${customerCardAcc.status==3}">
								<c:set var="status">
									<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
								</c:set>
							</c:if>
							<td align="center"><c:out value="${status}" /></td>
							<td align="center"><a
								href="editCustomerCard.htm?cardId=<c:out value="${customerCardAcc.cardId}"/>&referenceId=<c:out value="${customerCardAcc.referenceId}"/>">
									<spring:message code="LABEL_EDIT" text="Edit" />
							</a></td>
						</tr>
					</c:forEach>
					<tr bgcolor="#30314f" style="color: white;">
						<c:if test="${page.totalPages>1}">
							<td colspan="7" align="right" height="25px"><a
								<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="1" />" </c:if>
								style="color: white;"><c:out value="[ First " /></a> <a
								<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${page.currentPage-1}" />" </c:if>
								style="color: white;"><c:out value=" / Prev ]" /></a> <c:forEach
									var="i" begin="${page.startPage}" end="${page.endPage}"
									step="1">
									<c:if test="${page.currentPage!=i}">
										<a
											href="<c:out value="${page.requestPage}"/>?&customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${i}" />"
											style="color: white;"><c:out value="${i}" /></a>
									</c:if>
									<c:if test="${page.currentPage==i}">
										<b><c:out value="${i}" /></b>
									</c:if>
								</c:forEach> <a
								<c:if test="${page.currentPage<page.totalPages}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${page.currentPage+1}" />"</c:if>
								style="color: white;"><c:out value="[ Next / " /></a> <a
								<c:if test="${page.totalPages!=page.currentPage}">href="<c:out value="${page.requestPage}"/>?customerId=<c:out value="${bankAccountDTO.customerId}" />&pageNumber=<c:out value="${page.totalPages}" />"</c:if>
								style="color: white;"><c:out value="Last ]" /></a></td>
						</c:if>
					</tr>
				</table>
			</div>
		</div>
	</div>
	</div>





</body>
</html>

