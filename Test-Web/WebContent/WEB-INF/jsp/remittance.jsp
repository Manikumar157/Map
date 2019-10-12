<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<script type="text/javascript"> 
  
  function validate(){  
  var shortNamePattern='/^[a-zA-Z ]*$/';
  var compnyName=document.getElementById("remittanceCompanyName").value;
  var transferType=document.getElementById("remittanceTransferType").value;
  var remittanceStatus=document.getElementById("remittanceStatus").value;
  if(compnyName.length<1){
	  alert("Please select company name");
	  document.getElementById("remittanceCompanyName").focus();
	  return false;
  }
  if(compnyName.charAt(0) == " " || compnyName.charAt(compnyName.length-1) == " "){
      alert("Please remove unwanted spaces in company name");
      return false;
  }
  if(!compnyName.match(/^[a-zA-Z ]*$/)){
	  alert("Company name should be albhabet only");
	  document.getElementById("remittanceCompanyName").focus();
	  return false;
  }
  if(transferType.length<1){
	  alert("Please select transfer type");
	  document.getElementById("remittanceTransferType").focus();
	  return false;
  }
  if(remittanceStatus.length<1){
	  alert("Please select status");
	  document.getElementById("remittanceStatus").focus();
	  return false;
  }
 
  document.remittanceForm.action="addRemittanceCompany.htm";
  document.remittanceForm.submit();
  }
  function resetRemittance(){
	  document.getElementById("remittanceCompanyName").value="";
	  document.getElementById("remittanceTransferType").value="";
	  document.getElementById("remittanceStatus").value="";
  }
  
function removeMessage(){
	 var successMessage  = document.getElementById("successMessage").value;
	 if(successMessage != null ||  successMessage == undefined) {
		$("#successMessage").hide();
	
		 }
}
</script>
</head>
 <body>
	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="TITLE_REMITTANCE" text="Remittance Companies" /></span>
				</h3>
			</div>
			<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101 !important; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
			<!-- /.box-header -->
			<div class="box-body" style="height:350px;">
			<div class="col-sm-3"></div>
				<!-- form start -->
				<div class="col-sm-6 table_border" ><!-- style="border: 1px solid;border-radius: 15px;" -->
				<form:form name="remittanceForm" id="remittanceForm" action="addRemittanceCompany.htm"
						method="post" commandName="remittanceDTO" class="form-inline">
						<jsp:include page="csrf_token.jsp"/>
						<authz:authorize ifAnyGranted="ROLE_viewRemittanceAdminActivityAdmin">
						<br/>
					<div class="row">
						<div class="col-md-12">
							<label class="col-sm-6"><spring:message
									code="LABEL_COMPANY_NAME" text="Company Name"></spring:message><font
								color="red">*</font></label>
							<form:input path="remittanceCompanyName" cssClass="form-control"
								maxlength="30" onclick="removeMessage();" />
							<FONT color="red">
							<form:errors path="remittanceCompanyName" /></FONT>
						</div>
					</div>
					<br />
					<div class="row">
						<div class="col-md-12">
						<label class="col-sm-6"><spring:message code="LABEL_TRANSFER_TYPE" text="Transfer Type" /><font color="red">*</font></label> 
					<form:select path="remittanceTransferType" id="remittanceTransferType" multiple="multiple" class="multiple">
					<form:option class="text_space" value="1"><spring:message code="LABEL_TRF_TYPE_NATIONAL" text="National" /></form:option>
					<form:option class="text_space" value="2"><spring:message code="LABEL_TRF_TYPE_REGIONAL" text="Regional" /></form:option>
					<form:option class="text_space" value="3"><spring:message code="LABEL_TRF_TYPE_INTERNATIONAL" text="International" /></form:option>
					</form:select>
                   </div>
                   </div>
					<br/>
						<div class="row">
							<div class="col-md-12">
								<label class="col-sm-6"><spring:message
										code="LABEL_COMPANY_STATUS"
										text="Status"></spring:message><font
									color="red">*</font></label>
								<form:select path="remittanceStatus" cssClass="dropdown">
												<form:option value="">--Please select--</form:option>
												<form:option value="10"><spring:message code="LABEL_ACTIVE" text="Active"/></form:option>
												<form:option value="20"><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></form:option>
												</form:select>
								<FONT color="red">
								<form:errors path="remittanceStatus" /></FONT>
							</div>
						</div> <!-- /.box-body -->

						<div class="box-footer">
							<c:choose>
								<c:when test="${(remittanceDTO.remittanceCompanyId eq null) }">
									<c:set var="buttonName" value="LABEL_ADD" scope="page" />
								</c:when>
								<c:otherwise>
									<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
								</c:otherwise>
							</c:choose>
							<input type="button" id="submitButton"
								value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
								onclick="validate();" class="btn btn-primary pull-right" ></input>
							<c:if test="${remittanceDTO.remittanceCompanyId ne null}">
								<input type="button"
									value="<spring:message code="LABEL_RESET" text="Reset"/>"
									onclick="resetRemittance();" /><br/><br/>
							</c:if>
						</div>
						</authz:authorize>
						<form:hidden path="remittanceCompanyId" value="${remittance.remittanceCompanyId}"/>
				</form:form>
				</div>
			</div>
			</div>
			<div class="box">
			<div class="box-body">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_COMPANY_NAME"
										text="Company Name" /></th>
								<th><spring:message code="LABEL_TRANSFER_TYPE"
										text="Transfer Type" /></th>
								<th><spring:message code="LABEL_COMPANY_STATUS" text="Status"/></th>
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
							<c:forEach items="${page.results}" var="remittance">
								<tr>
									<td><c:out value="${remittance.remittanceCompanyName}" /></td>
									<td><c:forEach items="${remittance.remittanceCompaniesTransferTypes}" var="trfTypeSet">
														<c:if test="${trfTypeSet.comp_id.transferTypeId==1}">
														<c:set var="trfType"><spring:message code="LABEL_TRF_TYPE_NATIONAL" text="National" /></c:set>
														</c:if>
														<c:if test="${trfTypeSet.comp_id.transferTypeId==2}">
														<c:set var="trfType"><spring:message code="LABEL_TRF_TYPE_REGIONAL" text="Regional" /></c:set>
														</c:if>
														<c:if test="${trfTypeSet.comp_id.transferTypeId==3}">
														<c:set var="trfType"><spring:message code="LABEL_TRF_TYPE_INTERNATIONAL" text="International" /></c:set>
														</c:if>
														 <c:out value="${trfType}" /><br/>	
														</c:forEach>
														</td>
									<c:if test="${remittance.status==10}">
										                  	<c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>
									                    </c:if>
									                    <c:if test="${remittance.status==20}">
										                    <c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
									                    </c:if>
									                    <td align="center"><c:out value="${status}" /></td>
						                                <authz:authorize ifAnyGranted="ROLE_viewRemittanceAdminActivityAdmin">
														<td><a href="javascript:submitForm('remittanceForm','editRemittanceCompany.htm?remittanceCompanyId=<c:out value="${remittance.remittanceCompanyId}"/>')">
												<spring:message code="LABEL_EDIT" text="Edit" /></a>
														</td></authz:authorize>
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
