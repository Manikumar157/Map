<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>
<head>

<%-- <title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title> --%>

<%-- <link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" />
<link
	href="<%=request.getContextPath()%>/css/ui-lightness/jquery-ui-1.8.14.custom.css"
	rel="stylesheet" type="text/css" /> --%>

<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/bankManagement.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script>


<!-- Loading language definition file -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG" />.js"></script>

<script type="text/javascript">
	function searchSubmit() {
		/* var recordNo=document.getElementById('recordNo').value;
		var from=document.getElementById('fromDate').value;
		var to=document.getElementById('toDate').value;
		var fromAmount=document.getElementById('fromAmount').value;
		var toAmount=document.getElementById('toAmount').value;
		if(recordNo!=""){
			if(isNaN(recordNo) || recordNo.charAt(0) == " " || recordNo.charAt(recordNo.length-1) == " "){
			alert("<spring:message code='VALID_RECORD_NO' text='Please give valid record no.'/>");
			return false;
			}
		}
		if(from!=""){
			if(to==""){
		        alert("<spring:message code='VALID_TO_DATE' text='Please enter a valid to date'/>");
			    return false;
			}
			if(!compareDate(from,to)){
				alert("<spring:message code='VALID_FROM_TO_DATE' text='To date should not be less than  or equal to from date'/>");
				return false;
			}
			if(compareDate(getCurrentDate(),to)){
				alert("<spring:message code='VALID_TO_CURRENT_DATE' text='To date should not be greater than current date'/>");
				return false;
			}

		}
		if(to!=""){
			if(from==""){
				alert("<spring:message code='VALID_FROM_DATE' text='Please enter a from valid date'/>");
			return false;
			}
		}
		if(fromAmount!=""){
			if(toAmount==""){
				alert("<spring:message code='VALID_TO_AMOUNT' text='Please enter a valid to amount'/>");
				return false;
			}
			if(isNaN(fromAmount)){
				alert("<spring:message code='VALID_FORM_AMOUNT' text='Please enter a valid form amount'/>");
				return false;
			}
			if(isNaN(toAmount)){
				alert("<spring:message code='VALID_TO_AMOUNT' text='Please enter a valid to amount'/>");
				return false;
			}
			if(parseInt(toAmount)<=parseInt(fromAmount)){
				alert("<spring:message code='VALID_TO_FORM_AMOUNT' text='To amount should be greater than from amount'/>");
				return false;
			}
		}
		if(toAmount!=""){
			if(fromAmount==""){
				alert("<spring:message code='VALID_FORM_AMOUNT' text='Please enter a valid form amount'/>");
			    return false;
			}
		} */
		document.bankManageMentForm.method = "post";
		document.bankManageMentForm.action = "searchBank.htm";
		document.bankManageMentForm.submit();
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting
	function bankDetail(url,bankId){
		document.getElementById('bankId').value=bankId;
		submitlink(url,'bankManageMentForm');
	}
	//@END
</script>
</head>
<body>
	<form:form name="bankManageMentForm" id="bankManageMentForm" commandName="bankDTO"
		class="form-inline">
		<jsp:include page="csrf_token.jsp"/>
		<form:hidden path="countryId" value="1" id="countryId" />
		<div class="col-lg-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="LABEL_BANK_MNMT" text="Banks" /></span>
					</h3>
				</div>
				<br />
				<div class="col-md-3 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
				<div class="col-md-3 col-md-offset-10">
					<authz:authorize ifAnyGranted="ROLE_addBanksAdminActivityAdmin">
					<%-- 	<a href="javascript:submitForm('bankManageMentForm','addBankForm.htm')" onclick="removeMsg()"><strong><spring:message
									code="LABEL_ADD_BANK" text="Add Bank"></spring:message></strong></a> --%>
					 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_BANK" text="Add Bank"/>"
							onclick="javascript:submitForm('bankManageMentForm','addBankForm.htm')" /> 
					</authz:authorize>
				</div>
				<div class="box-body">
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_BANKNAME" text="Bank Name"></spring:message></label>
							<form:input path="bankName" id="bankName" cssClass="form-control" />
						</div>
						<!-- //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
						<form:hidden path="bankId" />
						<!--  @End-->
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_SWIFT_CODE" text="BIC/SWIFT Code"></spring:message></label>
							<form:input path="swiftCode" maxlength="12"
								cssClass="form-control" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_BANKCODE" text="Bank Code"></spring:message></label>
							<form:input path="bankCode" cssClass="form-control" />
						</div>

						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_CURRENCY_NAME" text="Currency:" /></label>
							<form:select path="currencyId" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</form:option>
								<form:options items="${masterData.currencyList}"
									itemLabel="currencyName" itemValue="currencyId"></form:options>
							</form:select>
							<FONT color="red"><form:errors path="currencyId" /></FONT>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-6">
								<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_STATUS" text="Status"/></label>
							<form:select path="status" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--" />
								</form:option>
								<form:option value="1">
									<spring:message code="LABEL_ACTIVE" text="Active" />
								</form:option>
								<form:option value="0">
									<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
								</form:option>
							</form:select>
						</div>
						<div class="col-sm-6">
							<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY" text="Country" /></label>
								<label class="col-sm-4"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="South Sudan" /></label>		
							
							<%--commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018		
						<form:select path="countryId" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>

								<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${masterData.countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang }">
											<option value="<c:out value="${country.countryId}"/>"
												<c:if test="${country.countryId eq bankDTO.countryId}" >selected=true</c:if>>
												<c:out value="${cNames.countryName}" />
											</option>
										</c:if>
									</c:forEach>
								</c:forEach>

							</form:select> --%>
						</div>
					</div>
					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"
							value="<spring:message code="LABEL_SEARCH" text="Search"/>"
							onclick="searchSubmit();" style="margin-right: 60px;"></input> <br />
						<br />
					</div>
				</div>
			</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<!-- <th width="3%"></th> -->
								<th><spring:message code="LABEL_BANKNAME" text="BankName" /></th>
								<th><spring:message code="LABEL_SWIFT_CODE"
										text="BIC/Swift Code" /></th>
								<th><spring:message code="LABEL_BANKCODE" text="Bank Code" /></th>
								<th><spring:message code="LABEL_COUNTRY" text="Country" /></th>
								<th><spring:message code="LABEL_CURRENCY_CODE" text="Currency" /></th>
								<th><spring:message code="LABEL_TIMEZONE" text="TimeZone" /></th>
								<th><spring:message code="LABEL_STATUS" text="Status" /></th>
								<authz:authorize ifAnyGranted="ROLE_editBanksAdminActivityAdmin">
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</authz:authorize>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="bankinfo">
								<tr>
									<!-- <td></td> -->
									<td><c:out value="${bankinfo.BankName}" /></td>
									<td><c:out value="${bankinfo.SwiftCode}" /></td>
									<td><c:out value="${bankinfo.BankCode}" /></td>
									<td>
									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${bankinfo.country.countryNames}"
											var="cname">
											<c:if test="${cname.comp_id.languageCode==lang }">
												<c:out value="${cname.countryName}" />
											</c:if>
										</c:forEach> <c:out value="${bankinfo.Country}" /></td>
									<td><c:out value="${bankinfo.CurrencyCode}" /></td>
									<td><c:out value="${bankinfo.TimeZoneDesc}" /></td>
									<c:if test="${bankinfo.status==1}">
										<c:set var="status">
											<spring:message code="LABEL_ACTIVE" text="Active" />
										</c:set>
									</c:if>
									<c:if test="${bankinfo.status==0}">
										<c:set var="status">
											<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
										</c:set>
									</c:if>
									<td><c:out value="${status}" /></td>
									<authz:authorize
										ifAnyGranted="ROLE_editBanksAdminActivityAdmin">
										<td>
										<!-- //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
										<%-- <a
											href="javascript:submitForm('bankManageMentForm','editBankForm.htm?bankId=<c:out value="${bankinfo.BankID}"/> ')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a> --%>
													
													<a
											href="javascript:bankDetail('editBankForm.htm','<c:out value="${bankinfo.BankID}"/> ')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
													<!-- @END -->
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
	<script type="text/javascript">
		var a;
		var arr = new Array();
		a = "<c:out value='${bankList}'/>";
		var c = a.substring(a.indexOf('[') + 1, a.lastIndexOf(']'));

		for ( var i = 0; i < c.split(",").length; i++) {
			//arr[i]="\""+c.split(",")[i]+"\"";
			arr[i] = c.split(", ")[i];
			//alert(arr[i])
		}
		$(document).ready(function() {
			$("#bankName").autocomplete({
				source : arr

			});
		});
	</script>
</body>

</html>
