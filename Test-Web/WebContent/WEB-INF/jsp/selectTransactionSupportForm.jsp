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
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">
	function validate() {

		var mnumber = document.getElementById("mobileNumber").value;
		var isdCode = document.getElementById("isdCode").value;
		var isdNum = isdCode.split(",");
		var mobileNumLen = isdNum[1];
		var numPattern = '^[0-9]*$';
		if (document.getElementById('isdCode').value != '') {
			mobilepattern = '^\[0-9]{' + mobileNumLen + '}$';
		}

		if (isdCode == "") {
			alert("<spring:message code="VALIDATION_COUNTRY" text="Please select Country name" />");
			return false;
		} else if (mnumber == "") {
			alert("<spring:message code="VALIDATION_MOBILE_DIGITS" text="Please enter Mobile Number" />");
		} else if (mnumber.charAt(0) == " "
				|| mnumber.charAt(mnumber.length - 1) == " ") {
			alert("<spring:message code='VALIDATION_MOBILE_SPACE' text='Please remove  space in Mobile Number'/>");
			return false;
		} else if (mnumber.search(numPattern) == -1) {
			alert("<spring:message code="VALIDATION_MOBILE_NUMBER" text="Please enter Mobile Number only in digits" />");
		} else if (document.getElementById('isdCode').value != ''
				&& mnumber.search(mobilepattern) == -1) {
			alert("<spring:message code="VALIDATION_MOBILE_LENGTH" text="Please enter valid Mobile Number of Length"/>"
					+ " " + mobileNumLen);
			document.transactionForm.mobileNumber.focus();
		} else if (document.getElementById('transactionType').value == "") {
			alert("<spring:message code="VALIDATION_TRASACTION_TYPE" text="Please select Transaction Type" />");
		} else {
			var mobileNumber = isdNum[0].concat(mnumber);
			/* document.transactionForm.action = "getAccountDetailsForSupport.htm?mobileNumber="+ mobileNumber;
			document.transactionForm.submit(); */
			var mobileNum = document.getElementById("mobileNumber").value;
			mobileNum = mobileNumber;
			url = "getAccountDetailsForSupport.htm";
			document.transactionForm.submit();
			//submitlink(url,'transactionForm')
			
			
			
		}

	}
</script>

</head>
<body>
	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_TXN_SUPPORT"
							text="Transaction Service" /></span>
				</h3>
			</div>
			<br />
			<div class="box-body">
				<div class="col-sm-6 col-sm-offset-5">
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
			</div>
			<!-- /.box-header -->
			<div class="box-body table_border"
				style="/* border: 1px solid */; width: 70%; margin-left: 150px;/*  border-radius: 15px; */">
				<div class="pull-right">
				<!-- Vineeth, Date:22/06/2018,
				Adding form tag to the anchor  -->
				<form id="transactionFormTest">
				<input type="hidden" name=templetID id=templetID value=3></input>
				<jsp:include page="csrf_token.jsp"/>
					<%-- <a href="javascript:submitForm('transactionFormTest','uploadTransactionDetails.htm')"><b><spring:message
								code="LABEL_UPLOAD_BULK_TXN" text="Upload Transaction Details" /></b></a> --%>
				</form>
				</div><br /><br />
				<!-- form start -->
				<form id="selectTransactionForm" class="form-inline"
					action="getAccountDetails.htm" method="post" name="transactionForm">
					<jsp:include page="csrf_token.jsp"/>
					<div class="row">
					<input type="hidden" id="isdCode" value="${isdCode},${mobileNumLength}"/>
						<div class="col-md-3"></div>
						<div class="col-md-6">
							<label class="col-sm-6"><spring:message
									code="LABEL_COUNTRY" text="Country:" /><!-- <font color="red">*</font> -->
									</label>
									<div class="col-sm-5">
									<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label>
						
						<%-- 		commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018
							<select class="dropdown chosen-select" id="isdCode" name="isdCode">
								<option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</option>
								<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang }">
											<option
												value="${country.isdCode},${country.mobileNumberLength}">${cNames.countryName}</option>
										</c:if>
									</c:forEach>
								</c:forEach>
							</select> --%>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3"></div>
						<div class="col-md-6">
							<label class="col-sm-6"><strong><spring:message
										code="LABEL_MOBILE_NO" text="Mobile Number" /></strong><font
								color="red">*</font></label> 
								<div class="col-sm-5">
								<input id="mobileNumber"
								name="mobileNumber" maxlength="${mobileNumLength}" class="form-control" />
								</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-3"></div>
						<div class="col-md-6">
							<label class="col-sm-6"><spring:message code="LABEL_TXN"
									text="Txn Type" /><font color="red">*</font></label> 
									<div class="col-sm-5">
									<select
								name="transactionType" id="transactionType" class="dropdown chosen-select">
								<option value="">
									<spring:message code="LABEL_SELECT" text="Please select" />
								</option>
								<option value="30">
									<spring:message code="LABEL_TITLE_BALANCE"
										text="Balance Enquiry" />
								</option>
								<option value="35">
									<spring:message code="LABEL_STATEMENT" text="Ministatement" />
								</option>
								<option value="98">
									<spring:message code="LABEL_TXN_STMT" text="Txn Statement" />
								</option>
							</select>
							</div>
						</div>
					</div>
					
					<!-- /.box-body -->

					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							onclick="validate();"
							value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" /> <br />
						<br />
					</div>
				</form>
			</div><br />

			<!-- /.box-body -->
		</div>
		<!-- /.box -->
	</div>
</body>
</html>
