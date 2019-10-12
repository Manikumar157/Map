<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz"
	uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<%-- <link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/webUser.js"></script>
<style>
#status {
    height: 35px;
    position: absolute;
    left: 57%;
    top: 100%;
    background-image: none;
    background-repeat: no-repeat;
    background-position: center;
    margin: -42px 0 0 -100px;
}
</style>
<script type="text/javascript">

 var Alertmsg={
 
 "username":"<spring:message code="NotEmpty.webUserDTO.userName" text="Please enter User Name"/>",
 "firstname":"<spring:message code="NotEmpty.webUserDTO.firstName" text="Please enter First Name"/>",
 "mobilenumber":"<spring:message code="NotEmpty.webUserDTO.mobileNumber" text="Please enter valid Mobile Number"/>",
 "userrole":"<spring:message code="NotNull.webUserDTO.roleId" text="Please select User Role"/>",
 "usernamevalid":"<spring:message code="VALID_USERNAME" text="Please enter UserName with out any special characters "/>",
 "firstnamevalid":"<spring:message code="VALID_FIRSTNAME" text="Please enter  First Name with out any special characters "/>",
 "namevalid":"<spring:message code="VALID_NAME" text="Please enter a valid name "/>",
 "mobilenumbervalid":"<spring:message code="MOBILE_NUM_ONLYIN_DIGITS" text="Please enter valid Mobile Number" />",
 "mobilenumberdigits":"<spring:message code="VALID_MOBILE_NUM" text="Please enter valid Mobile Number only in digits"/>", 
 "emailnotempty":"<spring:message code="LABEL.EMAIL.NOTEMPTY" text="Please enter EMail Id"/>",
 "emailvalid":"<spring:message code="VALID_EMAIL" text="Please enter valid EMail Id"/>",
 "userNameSpace":"<spring:message code="USERNAME_SPACE" text="Please remove white spaces in User Name"/>",
 "firstNameSpace":"<spring:message code="FIRSTNAME_SPACE" text="Please remove white spaces in First Name"/>",
 "mobileNumberSpace":"<spring:message code="MOBILENUM_SPACE" text="Please remove white spaces in Mobile Number"/>",
 "selectBankGroup":"<spring:message code="VALID_EMPTY_BANK_GROUP" text="Please select Bank Group"/>",
 "selectBank":"<spring:message code="VALID_EMPTY_BANK_NAME" text="Please select Bank Name"/>",
 "selectBranch":"<spring:message code="VALID_EMPTY_BANK_BRANCH" text="Please select the Branch"/>",
 "confirmResetPassword" : "<spring:message code="CONFIRM_RESET_PASSWORD" text="Do you really want to reset the password for this user &#63; "/>",
 "useNameLength" : "<spring:message code="VALID_USERNAME_LENGTH" text="Please enter UserName minimum length is 6 characters"/>",
 "firstNameLength" : "<spring:message code="VALID_FIRSTNAME_LENGTH" text="Please enter FirstName minimum length is 2 characters"/>"	,
 "mobileNumLength":"<spring:message code="VALIDATION_MOBILE_LENGTH" text="Please enter valid Mobile Number of Length"/>",
 "countryName":"<spring:message code="VALIDATION_COUNTRY" text="Please select Country name" />"
 };	
 
 
 $(document).ready(function() {		
		var role = document.getElementById("roleId").value ;
	
		if( role == 3 || role == 2){
			
			$(".bankdet").show();
		}else{
			$(".bankdet").hide();
		}
		
		if( role ==7 ){
			
				$(".bankgroup").show();
			}else{
				$(".bankgroup").hide();
			}
		
		$("#bankId").change(function() {
		
			$bankId = document.getElementById("bankId").value;
		
			$.post("getBranches.htm", {
				bankId : $bankId
			}, function(data) {
				document.getElementById("branchdiv").innerHTML="";
				document.getElementById("branchdiv").innerHTML = data;
				
			});
			
		});
		$("#roleId").change(function() {
			var role = document.getElementById("roleId").value ;
			//alert(role);
			if( role == 3 || role == 2){
				$(".bankdet").show();
			}else{
				$(".bankdet").hide();
			}
			if( role == 7 ){
				$(".bankgroup").show();
			}else{
				$(".bankgroup").hide();
			}
			
		});
		
	/* 	commenting for making country as by default SouthSudan,
		by vineeth ,on 12-11-2018
		$("#countryId").change(function() {
			
			$country = document.getElementById("countryId").value;
			$.post("getMobileNumberLength.htm", {
				country : $country
			}, function(data) {
				document.getElementById("mobileNum").innerHTML="";
				document.getElementById("mobileNum").innerHTML = data.match(/\d/g).join("");
			});
		});		 */
		
	});
 
 function changeStatus(flag){
	 document.webUserManageMentForm.status[1].disabled=flag;
 }
 
 function searchSubmit(){
	 /*bugId:450 by:rajlaxmi for:validation in mobile field and email  */
	 var mobileNumber = $("#mobileNumber").val();
	 var emailValid = $("#email").val();
	 var emailPattern = /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
		
	  if (mobileNumber != "") {
			if (mobileNumber.charAt(0) == " "
					|| mobileNumber
							.charAt(mobileNumber.length - 1) == " ") {
				alert("<spring:message code='ALERT_VALIDATION_MOB' text='Please remove unwanted spaces in Mobile Number'/>");
				return false;
			}
			if ((!mobileNumber.match(/^[0-9]*$/))) {
				alert("<spring:message code='VALIDATION_VALID_MOBILE' text='Please Enter valid Mobile Number'/>");
				return false;
			}

		}
	 
		  if (emailValid != ""
				&& !emailValid.match(emailPattern)) {

			alert("<spring:message code='ALERT_EMAIL_VALID_ID' text='Please enter valid Email ID'/>");
			return false;
		}
	
	    document.webUserManageMentForm.method = "post";
		document.webUserManageMentForm.action = "searchWebUser.htm";
		document.webUserManageMentForm.submit();
 }
//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
 function webUserDetail(url,userName){
 	document.getElementById('userName').value=userName;
 	submitlink(url,'webUserManageMentForm');
 }
 //@end
	
 // Naqui: export excel for Locations
	function webUserExcel(){
			
			 submitlink("exportToXlsWebUser.htm","webUserManageMentForm"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
			 $.unblockUI();
		}
	
	// Naqui: export excel for Locations
	function webUserPDF(){
			
			 submitlink("exportToPdfWebUser.htm","webUserManageMentForm"); 
			 for(var i=0;i<150000;i++);
			 document.body.style.cursor = 'default';
			 canSubmit = true; 
		}
 
</script>

</head>

<body onload="check()">
<form:form name="webUserManageMentForm" id="webUserManageMentForm" class="form-inline"  method="post" commandName="webUserDTO">
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_USER_MNMT" text="Web Users" /></span>
		</h3>
	<br />
	<div class="col-md-5 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
	<spring:message code="${message}" text="" />
	</div>
	</div><br/>
	<div class="col-md-3 col-md-offset-10">
			<authz:authorize ifAnyGranted="ROLE_addUserManagementAdminActivityAdmin">
				<label>
					<%-- <a href="javascript:submitForm('webUserManageMentForm','showUserForm.htm')" onclick="removeMsg()"><b><spring:message code="LABEL_ADD_WEB_USER" text="Add User"></spring:message></b></a> --%>
					 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_WEB_USER" text="Add Clearing House"/>"
							onclick="javascript:submitForm('webUserManageMentForm','showUserForm.htm')" /> 
				</label>
			</authz:authorize>
	</div>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_USERID" text="User ID" /></label> 
					<div class="col-sm-6">
					<form:input path="userName" id="userName" maxlength="32" cssClass="form-control" />
					<FONT color="red"><form:errors path="userName" /></FONT>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_MOBILE_NUM" text="Mobile Number" /></label>
					<div class="col-sm-6">
					<form:input path="mobileNumber" id="mobileNumber" maxlength="9" cssClass="form-control"  /> 
					</div>
				</div>			
			</div>
			<div class="row">
			<%-- commenting for making country as by default SouthSudan,
				by vineeth ,on 12-11-2018 	
			<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_COUNTRY" text="Country:" /></label> 
					<div class="col-sm-6">
					<select class="dropdown chosen-select" id="countryId" name="countryId">
						<option value=""><spring:message code="LABEL_SELECT"
							text="--Please Select--" /> <c:set var="lang"
							value="${lang}"></c:set> 
							<c:forEach items="${countryList}" var="country">
							
							<c:forEach items="${country.countryNames}" var="cNames">
											<c:if test="${cNames.comp_id.languageCode==lang }">
														 <option value="<c:out value="${country.countryId}"/>"  <c:if test="${country.countryId eq webUserDTO.countryId}" >selected=true</c:if> > 
														<c:out 	value="${cNames.countryName}"/>	
														</option>
														</c:if>		
							</c:forEach>
						</c:forEach>
						</option>
					</select>
					</div>
				</div> --%>
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_EMAIL" text="Email" /></label> 
					<div class="col-sm-6">
					<form:input path="email" maxlength="32" cssClass="form-control" /> 
					<FONT color="red"><form:errors path="email" /></FONT>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4"><span style="color:#FF0000;"></span><spring:message code="LABEL_STATUS" text="Status"/></label> 
					<div class="col-sm-6">
					<form:select path="status" cssClass="dropdown chosen-select">
		                <form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
		                <form:option value="N"><spring:message code="LABEL_ACTIVE" text="Active"/></form:option>
		                <form:option value="Y"><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></form:option>		
		           </form:select>
		           </div>
				</div>			
			</div>
			<div class="row">
			<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankadmin,ROLE_ccadmin,ROLE_bankgroupadmin,ROLE_superadmin,ROLE_mGurush,ROLE_businesspartnerL1,ROLE_businesspartnerL2">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_USER_TYPE" text="User Type" /></label> 
					<div class="col-sm-6">
					<form:select path="roleId" id="roleId" 
													cssClass="dropdown chosen-select">
													<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
													<authz:authorize ifAllGranted="ROLE_admin">
															<%-- <form:option value="1"><spring:message code="LABEL_GIM_ADMIN" text="Gim Admin"/></form:option>
															 <form:option value="15"><spring:message code="LABEL_SUPER_ADMIN" text="Super Admin"/> </form:option>
 --%>															<form:option value="7"><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Organization Group Admin"/> </form:option>
															<%-- <form:option value="5"><spring:message code="LABEL_CUSTOMER_CARE_EXECUTE" text="Customer Care Executive(Support)"/></form:option>
															<form:option value="17"><spring:message code="LABEL_GIM_BACKOFFICE_LEAD" text="GIM Back Office Lead"/></form:option>
														    <form:option value="18"><spring:message code="LABEL_GIM_BACKOFFICE_EXEC" text="GIM Back Office Exec"/> </form:option>
															<form:option value="19"><spring:message code="LABEL_GIM_SUPPORT_LEAD" text="GIM Support Lead"/> </form:option>
															<form:option value="20"> <spring:message code="LABEL_GIM_SUPPORT_EXEC" text="GIM Support Exec"/></form:option>
 --%>															<form:option value="2"> <spring:message code="LABEL_BANK_ADMIN" text="Organization Admin"/></form:option>
															<form:option value="3"><spring:message code="LABEL_BANK_TELLER" text="Bank Teller"/></form:option>
															<%-- <form:option value="8"> <spring:message code="LABEL_PARAMETER" text="Parameter"/></form:option> --%>
															<%-- <form:option value="9"> <spring:message code="LABEL_CONTROL" text="Control"/></form:option> --%>
															<%-- <form:option value="21"><spring:message code="LABEL_INFORMATION_DESK" text="Information Desk"/></form:option> --%>
															<%-- <form:option value="12"><spring:message code="LABEL_BRANCH_MANAGER" text="Branch Manager"/></form:option> 
															<form:option value="13"><spring:message code="LABEL_RELATIONSHIP_MANAGER" text="Relationship Manager"/></form:option>
															<form:option value="14"><spring:message code="LABEL_PERSONAL_RELATIONSHIP_EXEC" text="Personal Relationship Exec"/></form:option>--%>
															<%-- <form:option value="23"> <spring:message code="LABEL_BUSINESS_PARTNER_ADMIN" text="Business Partner Admin"/></form:option> --%>
															<form:option value="22"><spring:message code="LABEL_OPERATION" text="Operation"/></form:option>
															<form:option value="10"> <spring:message code="LABEL_ACOUNTING" text="Accounting"/></form:option>
															<form:option value="16"><spring:message code="LABEL_SUPPORT_BANK" text="Support Customer Care"/></form:option>
															<form:option value="24"> <spring:message code="LABEL_BUSINESS_PARTNER_L1_USER" text="Principal Agent Admin"/></form:option>
															<form:option value="25"> <spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner Admin"/></form:option>
															<form:option value="26"> <spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner Admin"/></form:option>
														</authz:authorize>
														
														<authz:authorize ifAllGranted="ROLE_mGurush">
														<form:option value="24"> <spring:message code="LABEL_BUSINESS_PARTNER_L1_USER" text="Principal Agent Admin"/></form:option>
														</authz:authorize>
														
														<authz:authorize ifAllGranted="ROLE_businesspartnerL1">
														<form:option value="25"> <spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/></form:option>
														<form:option value="27"> <spring:message code="LABEL_SALES_OFFICERS" text="Sales Officer"/></form:option>
														</authz:authorize>
														
														<authz:authorize ifAllGranted="ROLE_businesspartnerL2">
														<%-- <form:option value="26"> <spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></form:option> --%>
														<form:option value="27"> <spring:message code="LABEL_SALES_OFFICERS" text="Sales Officer"/></form:option>
														</authz:authorize>
													
													<authz:authorize ifAllGranted="ROLE_bankadmin">
													    <form:option value="2"> <spring:message code="LABEL_BANK_ADMIN" text="Bank Admin"/></form:option>
														<form:option value="3"><spring:message code="LABEL_BANK_TELLER" text="Bank Teller"/></form:option>
														<%-- <form:option value="8"> <spring:message code="LABEL_PARAMETER" text="Parameter"/></form:option>
														<form:option value="9"> <spring:message code="LABEL_CONTROL" text="Control"/></form:option> --%>
														<form:option value="10"> <spring:message code="LABEL_ACOUNTING" text="Accounting"/></form:option>
														<%-- <form:option value="21"><spring:message code="LABEL_INFORMATION_DESK" text="Information Desk"/></form:option> --%>
														<form:option value="16"><spring:message code="LABEL_SUPPORT_BANK" text="Support Customer Care"/></form:option>
														<%-- <form:option value="12"><spring:message code="LABEL_BRANCH_MANAGER" text="Branch Manager"/></form:option>
														<form:option value="13"><spring:message code="LABEL_RELATIONSHIP_MANAGER" text="Relationship Manager"/></form:option>
														<form:option value="14"><spring:message code="LABEL_PERSONAL_RELATIONSHIP_EXEC" text="Personal Relationship Exec"/></form:option>
														 --%><form:option value="22"><spring:message code="LABEL_OPERATION" text="Operation"/></form:option>
														<form:option value="24"> <spring:message code="LABEL_BUSINESS_PARTNER_L1_USER" text="Principal Agent Admin"/></form:option>
														<form:option value="25"> <spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/></form:option>
														<%-- <form:option value="26"> <spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></form:option> --%>
														<form:option value="27"> <spring:message code="LABEL_SALES_OFFICERS" text="Sales Officer"/></form:option>
														<form:option value="28"> <spring:message code="LABEL_BULKPAY_PARTNER_ADMIN" text="BulkPay Partner Admin"/></form:option>
														<form:option value="29"> <spring:message code="LABEL_COMMERCIAL_OFFICER" text="Commercial Officer"/></form:option>
														<form:option value="30"> <spring:message code="LABEL_SUPPORT_CALL_CENTER" text="Support Call center"/></form:option>
													</authz:authorize>
													<authz:authorize ifAllGranted="ROLE_superadmin">
													 <form:option value="15"><spring:message code="LABEL_SUPER_ADMIN" text="Super Admin"/> </form:option>
														<form:option value="2"> <spring:message code="LABEL_BANK_ADMIN" text="Bank Admin"/></form:option>
													</authz:authorize>
													<authz:authorize ifAnyGranted="ROLE_ccadmin">
														<form:option value="4"><spring:message code="LABEL_CUSTOMER_CARE_ADMIN" text="Customer Care Admin"/></form:option>
														<form:option value="5"> <spring:message code="LABEL_CUSTOMER_CARE_EXECUTE" text="Customer Care Executive(Support)"/></form:option>
													</authz:authorize>
													<authz:authorize ifAllGranted="ROLE_bankgroupadmin">
														<form:option value="2"> <spring:message code="LABEL_BANK_ADMIN" text="Bank Admin"/></form:option>
														<form:option value="3"><spring:message code="LABEL_BANK_TELLER" text="Bank Teller"/></form:option>
													</authz:authorize>
												</form:select> <FONT color="red"><form:errors path="roleId" /></FONT>
					</div>
				</div>
				</authz:authorize>
				
						
			</div>
			<div class="box-footer">
			<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SEARCH" text="Search"/>" onclick="searchSubmit();" style="margin-right: 60px;"></input>
			<br/><br/>
			</div>
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
				
				<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
                	<%-- <span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span> --%>
					<span style="float:right; margin-right: 5px;">
						<a href="#" onclick="javascript:webUserExcel();" style="text-decoration:none;margin-left:-11px;"><img src="<%=request.getContextPath()%>/images/excel.jpg" /><span>
							<%-- <spring:message code="LABEL_EXCEL" text="Excel"/></span> --%>
						</a>
					</span>
					
					<span style="margin-right: 30px; float:right">
						<a href="#" style="text-decoration:none;margin-left:10px;" onclick="javascript:webUserPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
							<%-- <span><spring:message code="LABEL_PDF" text="PDF"/></span> --%>
						</a>
					</span>					
   			   </div>
				
				
					<table id="webUserTable" class="table table-bordered table-striped" style="text-align:center">
						<thead>
							<tr>
								<th><spring:message code="LABEL_USERID" text="User Id" /></th>
								<th><spring:message code="LABEL_FIRST_NAME" text="First Name" /></th>
								<th><spring:message code="LABEL_LAST_NAME" text="Last Name" /></th>
								<th><spring:message code="LABEL_MOBILE_NUM" text="Mobile Number" /></th>
								<th><spring:message code="LABEL_USER_TYPE" text="User Type" /></th>
								<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_admin,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
										<th><spring:message code="LABEL_BUSINESS_PARTNER" text="Business Partner" /></th>
								</authz:authorize>
								<th><spring:message code="LABEL_CREATE_DATE" text="Created Date" /></th>
								<th><spring:message code="LABEL_STATUS" text="Status" /></th>
								<th><spring:message code="LABEL_ACTION_EDIT" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="user">
											<tr>
												<td align="left"><c:out value="${user.userName}" /></td>
												<td align="left"><c:out value="${user.firstName}" /></td>
												<td align="left"><c:out value="${user.lastName}" /></td>
												<td align="left"><c:out value="${user.mobileNumber}" /></td>
												<td align="left">
												 <c:if test="${user.webUserRole.roleId == 1}">
												 <c:set var="description" ><spring:message code="LABEL_GIM_ADMIN" text="Gim Admin"/></c:set>
												 </c:if>
												<c:if test="${user.webUserRole.roleId==2}">
								
												 <c:set var="description" ><spring:message code="LABEL_BANK_ADMIN" text="Organization Admin"/> </c:set></c:if>
								
												<c:if test="${user.webUserRole.roleId==3}">
												<c:set var="description" ><spring:message code="LABEL_BANK_TELLER" text="Bank Teller"/> </c:set>
												</c:if>
													<c:if test="${user.webUserRole.roleId==4}">
													<c:set var="description" ><spring:message code="LABEL_CUSTOMER_CARE_ADMIN" text="Customer Care Admin"/>
													</c:set>
													</c:if>
										
												<c:if test="${user.webUserRole.roleId==5}">
										         <c:set var="description" ><spring:message code="LABEL_CUSTOMER_CARE_EXECUTE" text="Customer Care Executive"/></c:set>
										        </c:if>
										        
												<c:if test="${user.webUserRole.roleId==7}">
												<c:set var="description" ><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Organization Group Admin"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==8}">
												<c:set var="description" ><spring:message code="LABEL_PARAMETER" text="Parameter"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==9}">
												<c:set var="description" ><spring:message code="LABEL_CONTROL" text="Control"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==10}">
												<c:set var="description" ><spring:message code="LABEL_ACOUNTING" text="Accounting"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==12}">
												<c:set var="description" ><spring:message code="LABEL_BRANCH_MANAGER" text="Branch Manager"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==13}">
												<c:set var="description" ><spring:message code="LABEL_RELATIONSHIP_MANAGER" text="Relationship Manager"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==14}">
												<c:set var="description" ><spring:message code="LABEL_PERSONAL_RELATIONSHIP_EXEC" text="Personal Relationship Exec"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==15}">
												<c:set var="description" ><spring:message code="LABEL_SUPER_ADMIN" text="Super Admin"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==7}">
												<c:set var="description" ><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Bank Group Admin"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==16}">
												<c:set var="description" ><spring:message code="LABEL_SUPPORT_BANK" text="Support Customer Care"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==17}">
												<c:set var="description" ><spring:message code="LABEL_GIM_BACKOFFICE_LEAD" text="Gim Backoffice Lead"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==18}">
												<c:set var="description" ><spring:message code="LABEL_GIM_BACKOFFICE_EXEC" text="Gim Backoffice Exec"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==19}">
												<c:set var="description" ><spring:message code="LABEL_GIM_SUPPORT_LEAD" text="Gim Support Lead"/></c:set>
												</c:if>	
																							
												<c:if test="${user.webUserRole.roleId==20}">
												<c:set var="description" ><spring:message code="LABEL_GIM_SUPPORT_EXEC" text="Gim Support Exec"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==21}">
												<c:set var="description" ><spring:message code="LABEL_INFORMATION_DESK" text="Information Desk"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==22}">
												<c:set var="description" ><spring:message code="LABEL_OPERATION" text="Operation"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==23}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_ADMIN" text="Business Partner Admin"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==24}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_L1_USER" text="Principal Agent Admin"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==25}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==26}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==27}">
												<c:set var="description" ><spring:message code="LABEL_SALES_OFFICERS" text=" Sales Officer"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==28}">
												<c:set var="description" ><spring:message code="LABEL_BULKPAY_PARTNER_ADMIN" text="BulkPay Partner Admin"/></c:set>
												</c:if>
												
												<c:if test="${user.webUserRole.roleId==29}">
												<c:set var="description" ><spring:message code="LABEL_COMMERCIAL_OFFICER" text="Commercial Officer"/></c:set>
												</c:if> 
												
												<c:if test="${user.webUserRole.roleId==30}">
												<c:set var="description" ><spring:message code="LABEL_SUPPORT_CALL_CENTER" text="Support Call center"/></c:set>
												</c:if> 
												
												<c:out
													value="${description}" />
												</td>
												
												<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_admin,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
												<td>
													<c:forEach items="${businessPartnerUsers}" var="bpItms" >
													<%-- <c:out value="${bpItms.webUserName eq user.userName}"></c:out> --%>
														<c:if test="${bpItms.webUserName eq user.userName}">
															<c:out value="${bpItms.businessPartnerUserName}"/>
														</c:if>
													</c:forEach>
												</td>
												</authz:authorize>
												
												<td align="left"><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss"
													value="${user.createdDate}" /></td>
													<c:if test="${user.credentialsExpired eq 'N'}">
				                                        <c:set var="status" ><spring:message code="LABEL_ACTIVE" text="Active"/></c:set>

				                                    </c:if>
				                                    <c:if test="${user.credentialsExpired eq 'Y'}">
				                                        <c:set var="status" ><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></c:set>
				                                    </c:if>
                                                <td align="left"><c:out value="${status}" /></td>
												
												<td align="center">
											
<%-- 											<authz:authorize ifAllGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<c:choose>										
											<c:when test="${user.webUserRole.roleId == 1 }">
											<a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
											</c:when>
												</c:choose>
											
											</authz:authorize> --%> 
											
											
											<!-- @@@vinod changes -->
											<authz:authorize ifAnyGranted="ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3,ROLE_bankteller">
											
											
											<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
												<authz:authorize ifAnyGranted="ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">	
											 | <a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
													</authz:authorize>
											<%-- <c:choose>
											<c:when test="${user.webUserRole.roleId == 24 }">
											
											<authz:authorize ifAllGranted="ROLE_editUserManagementAdminActivityAdmin">
										
													<a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
													</authz:authorize>
											
											</c:when></c:choose> --%>
											</authz:authorize>
											
											
											<authz:authorize ifAllGranted="ROLE_bankadmin">
												<c:choose>
												
												<c:when test="${fn:containsIgnoreCase(user.userName, masterData.loggedInUser)==true}">
											
		<!-- ..... @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  -->
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</c:when>
													
												<c:when test="${user.webUserRole.roleId == 2 || user.webUserRole.roleId == 25 || user.webUserRole.roleId == 26 || user.webUserRole.roleId == 27}">
												<authz:authorize ifAllGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</authz:authorize>
													</c:when>
													<c:otherwise>
												<authz:authorize ifAllGranted="ROLE_editUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','editUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a> --%>
													
													
													<a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
												
													</authz:authorize>
													</c:otherwise>
															
													</c:choose>
												
												</authz:authorize>
												
												
												
												
												
												<authz:authorize ifAllGranted="ROLE_bankgroupadmin">
												<c:choose>
												
												<c:when test="${fn:containsIgnoreCase(user.userName, masterData.loggedInUser)==true}">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</c:when>
													
												<c:when test="${user.webUserRole.roleId == 2 || user.webUserRole.roleId == 3}">
													<authz:authorize ifAllGranted="ROLE_editUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','editUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a> --%>
													
													<a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
													</authz:authorize>
												</c:when>
<%-- 												<c:when test="${user.webUserRole.roleId == 2 }">
												<authz:authorize ifAllGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</authz:authorize>
													</c:when> --%>
												<c:otherwise>
													<authz:authorize ifAllGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</authz:authorize>
												</c:otherwise>
															
													</c:choose>
												
												</authz:authorize>
												
												<authz:authorize ifAllGranted="ROLE_superadmin">
												<c:choose>
												
												<c:when test="${fn:containsIgnoreCase(user.userName, masterData.loggedInUser)==true}">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</c:when>
													
												<c:when test="${user.webUserRole.roleId == 15 ||user.webUserRole.roleId == 1}">
												<authz:authorize ifAllGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</authz:authorize>
													</c:when>
													<c:otherwise>
												<authz:authorize ifAllGranted="ROLE_editUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','editUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a> --%>
													<a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
													</authz:authorize>
													</c:otherwise>
															
													</c:choose>
												
												</authz:authorize>
												
												<authz:authorize ifAllGranted="ROLE_admin">
												
													<c:choose>
												
												<c:when test="${user.webUserRole.roleId == 15 || user.webUserRole.roleId == 7 || user.webUserRole.roleId == 5 || user.webUserRole.roleId == 17 || user.webUserRole.roleId == 18 || user.webUserRole.roleId == 19 ||user.webUserRole.roleId == 20}">
												<authz:authorize ifAllGranted="ROLE_editUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','editUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a> --%>
													<a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
													</authz:authorize>
													</c:when>
												
												
												<c:when test="${user.webUserRole.roleId !=1}">										
												<authz:authorize ifAllGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
													</authz:authorize>
												
												
													</c:when>
											
												</c:choose>
												
												<c:choose>
												
												<c:when test="${fn:containsIgnoreCase(user.userName, masterData.loggedInUser)==true}">
												<authz:authorize ifNotGranted="ROLE_viewUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','viewUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a> --%>
													<a
													href="javascript:webUserDetail('viewUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_VIEW" text="view" /></a>
												</authz:authorize>
												</c:when>
												<c:when test="${user.webUserRole.roleId == 1}">
												
													<authz:authorize ifAnyGranted="ROLE_editUserManagementAdminActivityAdmin">
												<%-- <a
													href="javascript:submitForm('webUserManageMentForm','editUser.htm?userID=<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a> --%>
													<a
													href="javascript:webUserDetail('editUser.htm','<c:out value="${user.userName}"/>')"><spring:message
													code="LABEL_EDIT" text="Edit" /></a>
			<!-- ............ @End-->				
													</authz:authorize>
													</c:when>
												</c:choose>
											
												</authz:authorize>
												
												
												
												
												</td>
											</tr>
										</c:forEach>
						</tbody>
					</table>
				</div>
				<div id="mobileNum" style="visibility:hidden"/>
	</div>
	
</div>
</form:form>
</body>
</html>
