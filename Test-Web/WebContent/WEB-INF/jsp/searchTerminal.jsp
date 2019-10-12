<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
 <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
 <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-ui-1.8.14.custom.min.js"></script>
<script type="text/javascript">
	
		$(document).ready(function() {

			$("#banksID").change(function() {
				$bankId = document.getElementById("bankId").value;
				$csrfToken = $("#csrfToken").val();
				$.post("getBranches.htm", {
					bankId : $bankId,
					csrfToken : $csrfToken
				}, function(data) {
					document.getElementById("branchdiv").innerHTML = "";
					document.getElementById("branchdiv").innerHTML = data;
					setTokenValFrmAjaxResp();
					applyChosen();
				});

			});

		}); 

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

$(document).ready(function() {
	   $("#terStatus").val($("#terminalstatus").val());
	});
	
function terminalSearch(){
	if(validate()){
		document.terminalViewForm.method = "post";
		document.terminalViewForm.action = "searchTerminal.htm";
		document.terminalViewForm.submit();
		
	}
}	

function validate(){
	
    	var mobileNumber = document.getElementById("mobileNumber").value;

		if (isNaN(mobileNumber)) {
			alert('<spring:message code="MOBILENUM_CHARACTER" text="Enter a valid mobile number"/>');
			return false;
		}else if (mobileNumber!="" && mobileNumber.length!=10) {
			alert("Mobile Number length should be 10");
			return false;
		}  
		/* 
		document.outletViewForm.action = "searchOutlet.htm";
		document.outletViewForm.submit(); */
		return true;
	}
	
</script>
</head>
<body>
<form:form id="terminalViewForm" name="terminalViewForm" class="form-inline" method="post" >
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_VIEW_TERMINALS" text="View Terminals"></spring:message></span>
		</h3>
	</div>
		<div class="">
			<div style="color: #ba0101;font-weight: bold;font-size: 12px;text-align:center"><spring:message code="${message}" text=""/></div>
		</div>
	<div class="col-md-4 col-md-offset-9">
	<authz:authorize ifAnyGranted="ROLE_addMerchantAdminActivityAdmin">
		<a href="javascript:submitForm('terminalViewForm','addTerminal.htm?outletId=<c:out value="${outletId}"/>')"><strong><spring:message code="LABEL_ADD_TERMINAL" text="Add Terminal"/></strong></a>&nbsp;&nbsp;|
		
	</authz:authorize>
		<a href="javascript:submitForm('terminalViewForm','viewOutlet.htm?outletId=<c:out value="${outletId}"/>')"><strong><spring:message code="LABEL_OUTLET_VIEW" text="View Outlet" /></strong></a>
	</div><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"></spring:message></label> 
					<input id="mobileNumber" name="mobileNumber" maxlength="10" type="text" value="<c:out value="${mobileNumber}"/>" class="form-control" />
				</div>
				<div class="col-sm-6">
				<label class="col-sm-5"><spring:message code="LABEL_BANK" text="Bank"/></label> 
				<div id="banksID">
					<select name="bankId" class="dropdown_big chosen-select" id="bankId">
	                	<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
	                	<c:forEach items="${masterData.bankList}" var="banks">
						<option value=<c:out value="${banks.bankId}" /> <c:if test="${banks.bankId eq bankId.trim()}" >selected=true</c:if> ><c:out value="${banks.bankName}" /> </option>
						
						</c:forEach>
	                </select>
	                </div>
				</div>
			</div>
			<div class="row">
			<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin,ROLE_gimbackofficelead,ROLE_gimbackofficeexec,ROLE_gimsupportlead,ROLE_gimsupportexec">
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_BRANCH" text="Branch"/></label> 
					<div id="branchdiv"><select id="branchId" class="dropdown_big chosen-select" name="branchId">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
						<c:forEach items="${masterData.branchList}" var="branches">
							<option value="<c:out value="${branches.branchId}" />"  <c:if test="${branches.branchId eq branchId}" >selected=true</c:if> ><c:out value="${branches.location}" /> </option>
						</c:forEach>
					</select></div>
				</div>
			<input type="hidden" id="terminalstatus" name="terminalstatus" value="${ status}"/>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_STATUS" text="Status"/></label> 
					<div id="branchdiv">
						<select id="terStatus" name="status" class="dropdown_big chosen-select" name="terStatus">
							<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--"/></option>
								<option value="10">Active </option>
								<option value="20">Inactive </option>
						</select>
					</div>
				</div>
				
			</authz:authorize>
			</div>
			<input type="hidden" name="roleId" value="${roleId}"/>
			<input type="hidden" name="outletId" value="${outletId}"/>
			<input type="hidden" name="merchantId" value="${merchantId}"/>
			<div class="box-footer">
				<input type="button"  value="<spring:message code="LABEL_SEARCH"/>" class="btn btn-primary pull-right" onclick="terminalSearch()"/>
				<br/><br/>
			</div>
		
		</div>
		</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_MOBILE_NO" text="Mobile Number"/></th>
								<th><spring:message code="LABEL_BANK" text="Bank"/></th>
								<th><spring:message code="LABEL_BRANCH" text="Branch"/></th>
								<th><spring:message code="LABEL_STATUS" text="Status"/></th>
								<th><spring:message code="LABEL_ACTION_EDIT = Action" text="Action"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="j" value="0"></c:set>
					            <c:forEach items="${terminalList}" var="terminal">
					            <c:set var="j" value="${ j+1 }"></c:set>
					            <tr>
								    <td><c:out value="${terminal.mobileNumber}" /></td>
								     <td><c:out value="${terminal.bank.bankName}" /></td>
								      <td><c:out value="${terminal.branch.location}" /></td>
									<c:if test="${terminal.active == 10}">
                                        <c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>
                                    </c:if>
                                    <!-- by:rajlaxmi for:showing value as deactive  -->
                                    <c:if test="${terminal.active == 20}">
                                        <c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
                                    </c:if>
                                                <td><c:out value="${status}" /></td>		
									<td>
										<a href="javascript:submitForm('terminalViewForm','editTerminal.htm?terminalId=<c:out value="${terminal.terminalId}"/>')"><spring:message code="LABEL_EDIT" text="Edit" /></a>
									</td>
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
