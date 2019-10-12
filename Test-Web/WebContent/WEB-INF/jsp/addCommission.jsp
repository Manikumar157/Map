<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<link type="text/css" rel="stylesheet" 	href="<%=request.getContextPath()%>/css/style.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>

<script type="text/javascript">
	var Alertmsg={
		 "bankId":'<spring:message code="ALERT_BANK_ID" text="Please select bank"/>',
		 "transactions":'<spring:message code="ALERT_SELECT_TRANSACTION" text="Please select transaction type"/>',
		 
	};

	function submitSearchMarchantForm(){
		
	       var commission=document.getElementById("commission").value;
	       
	       var csrfToken = document.getElementById("csrfToken").value;
		
		   if(document.getElementById("bankId").value=="" ||document.getElementById("bankId").value==null )
			{
			alert("Please select Entity");
			return false;
			}
		 	if(commission.search( '^[0-9]+(\.[0-9]{1,2})?$')==-1)
			{
			alert("Please enter commission with out any special characters ");
			return false;
			}
		 
		 document.addCommissionForm.action="searchMerchantCommission.htm"+"&csrfToken="+csrfToken;
		 document.addCommissionForm.appendChild(csrfToken);
		 document.addCommissionForm.submit();
	}
	function addCommission(){
		 document.addCommissionForm.action="showAddCommissionForm.htm";
		 document.addCommissionForm.submit();
	}
	
	$(document).ready(function() {
		
		$("#bankId").change(function() {
			document.getElementById("bankId").value = document.getElementById("bankId").value ;
			$bankId = document.getElementById("bankId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getCustomerProfilesForCommision.htm", {
				bankId : $bankId
			}, function(data) {
				document.getElementById("profiles").innerHTML="";
				document.getElementById("profiles").innerHTML = data;
			});
		});
	});
	
</script>
<style>
.dropdown{
    font-size: 14px;
    font-family: Verdana;
    COLOR: #000000;
	width:155px;
	height:30px;
}
</style>
</head>
<body>
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_VIEW_Commission" text="View Commission"></spring:message></span>
				</h3>
			</div>
			
			<div class="col-sm-6 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
			</div>
			<form:form name="addCommissionForm" commandName="commissionDTO">
				<jsp:include page="csrf_token.jsp"/>
				<div class="col-md-3 col-md-offset-9">
				
					<authz:authorize ifAnyGranted="ROLE_addCommissionAdminActivityAdmin">
					<authz:authorize ifNotGranted="ROLE_bankadmin,ROLE_bankteller,ROLE_superadmin,ROLE_parameter,ROLE_audit,ROLE_accounting">
						<label class="col-sm-5" style="margin-top: 4px;width: 200px"> <a href="#" onclick="addCommission()">
							<b><spring:message code="LABEL_ADD_COMMISSION" text="Add Commission"></spring:message></b></a>
						</label>
					</authz:authorize>
					</authz:authorize>
				</div>
				<br />
				<br />
				<div class="box-body">
					<authz:authorize ifNotGranted="ROLE_bankadmin,ROLE_bankteller,ROLE_superadmin,ROLE_parameter,ROLE_audit,ROLE_accounting">
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;">
									<spring:message code="LABEL_BANKNAME" text="Entity Name" />
									<font color="red">*</font></label>
									<form:select path="bankId" id="bankId" cssClass="dropdown chosen-select">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</form:option>
									<form:options items="${masterData.bankList}" itemLabel="bankName" itemValue="bankId" />
								 </form:select>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5">  <spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></label>
								<form:select path="transactionTypeId"  id="transactionTypeId"  name="transactionTypeId" cssClass="dropdown chosen-select"  >
									<form:option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" ></spring:message></form:option>
									<c:set var="lang" value="${language}">
									</c:set>
									<c:forEach items="${masterData.transTypeList}" var="transTypeList" >
										<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs" varStatus="cnt">
											<c:if test="${transactionTypesDescs.comp_id.locale==lang}">
												<c:set value="f" var="sel"/>
												<c:forEach items="${serviceChargeDTO.transactions}" var="arrVal"> 
													<c:if test="${transTypeList.transactionType eq arrVal}"><c:set value="true" var="sel"/></c:if>
												</c:forEach>
																
												<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin">	
													<c:if test="${transTypeList.transactionType eq commissionDTO.transactionTypeId}">
													 	<option value="<c:out value="${transTypeList.transactionType}"/>" selected="selected" >
															<c:out 	value="${transactionTypesDescs.description}"/>	
														</option>
													 </c:if>
													 <c:if test="${transTypeList.transactionType ne commissionDTO.transactionTypeId}">
													 	<option value="<c:out value="${transTypeList.transactionType}"/>" >
															<c:out 	value="${transactionTypesDescs.description}"/>	
														</option>
													 </c:if>
												</authz:authorize>
																		
												<authz:authorize ifNotGranted="ROLE_admin,ROLE_bankgroupadmin">	
													<c:if test="${transTypeList.transactionType!=84 }">
													 <c:if test="${transTypeList.transactionType eq commissionDTO.transactionTypeId}">
													 	<option value="<c:out value="${transTypeList.transactionType}"/>" selected="selected" >
															<c:out 	value="${transactionTypesDescs.description}"/>	
														</option>
													 </c:if>
													 <c:if test="${transTypeList.transactionType ne commissionDTO.transactionTypeId}">
													 	<option value="<c:out value="${transTypeList.transactionType}"/>" >
															<c:out 	value="${transactionTypesDescs.description}"/>	
														</option>
													 </c:if>
														
													</c:if>
												</authz:authorize>
											</c:if>		  						
										</c:forEach>
									</c:forEach>
								</form:select>
							</div>
						</div>
									
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"> <spring:message code="LABEL_PROFILE" text="Profile" /></label>
								<div id="profiles">
									<form:select path="profileId" cssClass="dropdown chosen-select">
										<form:option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
										</form:option>
										<form:options items="${Profile}" itemLabel="profileName" itemValue="profileId" />
									</form:select>
								</div>
								<font color="RED"><form:errors path="profileId"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message code="LABEL_COMMISSION" text="Commisssion" /></label>
								<form:input path="commission" id="commission" cssStyle="width:153px" cssClass="form-control" /> 
							</div>
						</div>
										
						<div class="row">
							<%-- <div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;"><spring:message code="LABEL_COMMISSION" text="Commisssion" /></label>
								<form:input path="commission"  cssStyle="width:153px" cssClass="form-control"/> 
							</div> --%>
						</div>
						<br>
						<div class="box-footer">
							<input type="button" path="btnSearch"value="<spring:message code="LABEL_SEARCH" text="Search"/>" onclick="submitSearchMarchantForm();" class="btn btn-primary pull-right" ></input>
						</div>
						<br /> <br />
					</authz:authorize>
				</div>
			</form:form>
		</div>
		
		<div class="box">
			<div class="box-body table-responsive">
				<table id="example1" class="table table-bordered table-striped"	style="text-align: center;">
				<%-- <form:hidden path="commisionId">
				 </form:form> --%>
				<thead>
						<tr>
							<th><spring:message	code="LABEL_PROFILE" text="Profile" /></th>
							<th><spring:message code="LABEL_BANK_NAME"	text="Entity Name"/></th>									
							<th ><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type"/></th>
							<th><spring:message code="LABEL_COMMISSION" text="Commission"/></th>		
							<th><spring:message code="LABEL_ACTION"	text="Action" /></th>
						</tr>
				</thead>
				<tbody>
					 <%int i=0; %>
								<c:forEach items="${page.results}" var="commission">									    							       
									<tr height="23px" class="" <% if(i%2!=0)%> bgColor="#d2d3f1" <% i++; %>>										
										<td ><c:out value="${commission.profileName}" /></td>									
										<td ><c:out value="${commission.bankName}" /></td>
										<td ><c:out value="${commission.transactionTypeName}" /></td>										
										<td ><c:out	value="${commission.commission}" /></td>
										<td >
										<authz:authorize ifAnyGranted="ROLE_editCommissionAdminActivityAdmin">
											<%-- <a href="editCommission.htm?commissionId=<c:out value="${commission.commisionId}"/>"> --%>
											<form:form name="editCommission" id="editCommission" >
				<jsp:include page="csrf_token.jsp"/>
											<a href="javascript:submitForm('editCommission','editCommission.htm?commissionId=<c:out value="${commission.commisionId}"/>')">
													<spring:message code="LABEL_EDIT" text="Edit" /></a>
													</form:form>
										</authz:authorize>
										</td>
									</tr>								
								</c:forEach>
								
				</tbody>
				</table>
			</div>
		</div>
		
	</div>	

	</body>


</html>
