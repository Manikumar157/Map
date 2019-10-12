<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
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
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>

<%-- <link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>--%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/webUser.js"></script>

<script type="text/javascript">

 var Alertmsg={
 
 "username":"<spring:message code="NotEmpty.webUserDTO.userName" text="Please enter User Name"/>",
 "firstname":"<spring:message code="NotEmpty.webUserDTO.firstName" text="Please enter First Name"/>",
 "mobilenumber":"<spring:message code="NotEmpty.webUserDTO.mobileNumber" text="Please enter valid Mobile Number"/>",
 "userrole":"<spring:message code="NotNull.webUserDTO.roleId" text="Please select User Role"/>",
 "usernamevalid":"<spring:message code="VALID_USERNAME" text="Please enter UserName with out any special characters "/>",
 "firstnamevalid":"<spring:message code="VALID_FIRSTNAME" text="Please enter only Characters in First Name "/>",
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
 "confirmResetPassword" : "<spring:message code="CONFIRM_RESET_PASSWORD" text="Do you really want to reset the password for this user &#63;"/>",
 "useNameLength" : "<spring:message code="VALID_USERNAME_LENGTH" text="Please enter UserName minimum length is 6 characters"/>",
 "firstNameLength" : "<spring:message code="VALID_FIRSTNAME_LENGTH" text="Please enter FirstName minimum length is 2 characters"/>"	,
 "mobileNumLength":"<spring:message code="VALIDATION_MOBILE_LENGTH" text="Please enter valid Mobile Number of Length"/>",
 "countryName":"<spring:message code="VALIDATION_COUNTRY" text="Please select Country name" />",
 "middleName":"<spring:message code="VALID_MIDDLENAME" text="Please enter only Characters in Middle Name "/>",
 "lastName":"<spring:message code="VALID_LASTNAME" text="Please enter only Characters in Last Name "/>",
 };	
 
 
 $(document).ready(function() {		
		var role = document.getElementById("roleId").value ;
		$("#bank_chosen").css("width","180px")	;
		
		if( role == 15){
			$(".bank").show();
		}else{
			$(".bank").hide();
		}
		

		if( role == 3 || role == 12 || role == 13 || role == 14 || role == 16 || role == 8 || role == 9 || role == 10 || role == 21  || role == 2 || role == 22
				|| role == 29 || role == 30){
			
			$(".bankdet").show();
		}else{
			$(".bankdet").hide();
		}

		
		if( role ==7 ){
			
				$(".bankgroup").show();
			}else{
				$(".bankgroup").hide();
			}
		
		 if( role ==23 ){
			
			$(".businessdet").show();
		}else{
			$(".businessdet").hide();
		} 
		 
		
		/* @Added by vinod joshi purpuse:- While editing businessPartner Drop dwon is not coming */
		if( role ==23 ||  role ==24 || role ==25 || role ==26){
			 $(".businessdet").show();
		 }else{
			$(".businessdet").hide();
		 }
		
		if( role ==28 ){
			
			$(".bulkdet").show();
		}else{
			$(".bulkdet").hide();
		} 
		
		
		 
		 if( role ==23 || role ==25 || role ==26){
				
				$(".accessmodedet").show();
			}else{
				$(".accessmodedet").hide();
			} 
		
		$("#bankId").change(function() {
		
			$bankId = document.getElementById("bankId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getBranches.htm", {
				bankId : $bankId,
				csrfToken : $csrfToken
			}, function(data) {
			
				document.getElementById("branchdiv").innerHTML="";
				document.getElementById("branchdiv").innerHTML = data;
				setTokenValFrmAjaxResp();
			});
			
		});
		
		/*  vinod changes */
		/*  $("#roleId").change(function() {
			
			$roleId = document.getElementById("roleId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getBusinessPartner.htm", {
				csrfToken : $csrfToken
			}, function(data) {
			
				document.getElementById("businessdiv").innerHTML="";
				document.getElementById("businessdiv").innerHTML = data;
				setTokenValFrmAjaxResp();
				
			});
			
		});  */
		/*  vinod changes */
		
		$("#roleId").change(function() {
			var role = document.getElementById("roleId").value ;

			$("#bank_chosen").css("width","180px");
			
			if( role == 15){
				$(".bank").show();
			}else{
				$(".bank").hide();
			}
			
			if(role == 3 || role == 12 || role == 13 || role == 14 || role == 16 || role == 8 || role == 9 || role == 10 || role == 21  || role == 2 || role == 22
					|| role == 29 || role == 30){
				$(".bankdet").show();
			}else{
				$(".bankdet").hide();
			}
			if( role == 7 ){
				$(".bankgroup").show();
			}else{
				$(".bankgroup").hide();
			}

			if( role ==23 ||  role ==24 || role ==25 || role ==26 ){
				
				/* $csrfToken = $("#csrfToken").val();
				$.post("getBusinessPartner.htm", {
					csrfToken : $csrfToken
				}, function(data) {
				
					document.getElementById("businessdiv").innerHTML="";
					document.getElementById("businessdiv").innerHTML = data;
					//setTokenValFrmAjaxResp();
					
				},'html'); */
				$(".businessdet").show();

			}else{
				$(".businessdet").hide();
			}
			
			if( role ==28 ){
				
				$(".bulkdet").show();
			}else{
				$(".bulkdet").hide();
			} 
			
			if( role ==23 || role ==25 || role ==26){
				$(".accessmodedet").show();

			}else{
				$(".accessmodedet").hide();
			}
			
			
		});
	/* 
		commenting for making country as by default SouthSudan,
		by vineeth ,on 12-11-2018
	
	$("#countryId").change(function() {
			
			$country = document.getElementById("countryId").value;
			$.post("getMobileNumberLength.htm", {
				country : $country
			}, function(data) {
				document.getElementById("mobileNum").innerHTML="";
				document.getElementById("mobileNum").innerHTML = data;
			});
		});	 */	
		
	});
 
 function changeStatus(flag){
	 document.webUserManageMentForm.status[1].disabled=flag;
 }
 
 function viewUsers(url,pageNumber){
		document.getElementById('pageNumber').value=pageNumber;
		submitlink(url,'webUserManageMentForm');
	}

</script>

</head>

<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_USER_MNMT" text="Web Users" /></span>
		</h3>
	<div class="col-md-5 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
	<spring:message code="${message}" text="" />
		<c:if	test="${ message eq 'SAVE_USER_SUCCESS' }">
				-<c:out value="${userId}"/>
		</c:if>
	</div>
	</div>
	<div class="pull-right">
		<%-- <b><a href="javascript:submitForm('webUserManageMentForm','<c:out value="searchWebUser.htm?pageNumber=1"/>')"><spring:message code="VIEW_USERS" text="View Users" /> </a> &nbsp; &nbsp; &nbsp; &nbsp;</b> --%>
		<b><a href="javascript:viewUsers('searchWebUser.htm','1')"><spring:message code="VIEW_USERS" text="View Users" /></a> &nbsp; &nbsp; &nbsp; &nbsp;</b>
	</div><br/>
	
	
	<form:form class="form-inline" name="webUserManageMentForm" id="webUserManageMentForm" action="saveUser.htm" method="post" commandName="webUserDTO">
	<jsp:include page="csrf_token.jsp"/>
	<div class="box-body">
		<form:hidden path="countryId" value="1" id="countryId" />
		<form:hidden path="language" id="language" value="en_US"/>
		<input type="hidden" name="pageNumber" id="pageNumber" value=""/>
	<div class="pull-right">
	<authz:authorize ifAnyGranted="ROLE_admin,ROLE_superadmin,ROLE_bankadmin,ROLE_businesspartnerL1,ROLE_bankgroupadmin">
	<c:if	test="${ action ne 'add' }">
	<c:if	test="${webUserDTO.accountLock =='Y'}">
	<b><%-- <a href="javascript:submitForm('webUserManageMentForm','unblockUser.htm?userName=${webUserDTO.userName}')" --%>
		<a href="javascript:submitForm('webUserManageMentForm','unblockUser.htm')"
		onclick="return confirm('<spring:message code="ALERT_UNBLOCK_USER" text="Do you want to unblock the user?"/>');"> <spring:message
		code="UNBLOCK_USER" text="Unlock User"></spring:message>
	</a></b> 
	</c:if>
	</c:if>
	</authz:authorize>
	
		<c:if
			test="${ action ne 'add' }">
			<c:if	test="${webUserDTO.status!='Y' && webUserDTO.accountLock !='Y'}">
			
			<%-- <b><a href="javascript:submitForm('webUserManageMentForm','resetUserPassword.htm')" onclick="return confirmReset();"> <spring:message
				code="LABEL_RESET_PASSWORD" text="Reset Password"></spring:message>
			</a></b> --%>
			
			<b><a href="javascript:submitForm('webUserManageMentForm','resetUserPassword.htm')" onclick="return confirmReset('webUserManageMentForm','resetUserPassword.htm');"> <spring:message
				code="LABEL_RESET_PASSWORD" text="Reset Password"></spring:message>
			</a></b>
			
			
			</c:if>
		</c:if>
	</div>
	<div class="row col-md-12">
	<c:if test="${ action ne 'add' }">
	<c:if	test="${webUserDTO.userName!=null}">
	<label class="col-md-1"><spring:message code="LABEL_USER_ID" text="User ID" /></label>
	<div class="col-md-2"><c:out value="${webUserDTO.userName}"></c:out> </div>
	</c:if>
	</c:if>
	</div>
			<div class="row">
			<form:hidden path="userName" />
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_FIRST_NAME" text="First Name" /><font color="red">*</font></label> 
					<form:input path="firstName" id="firstName" maxlength="32" cssClass="form-control" /> 
					<FONT color="red"><form:errors path="firstName" /></FONT>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_MIDDLE_NAME" text="Middle Name" /></label> 
					<form:input path="middleName" maxlength="32" cssClass="form-control" /> <FONT color="red"><form:errors path="middleName" /></FONT>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_LAST_NAME" text="Last Name" /></label> 
					<form:input path="lastName" maxlength="32" cssClass="form-control" /> <FONT color="red"><form:errors path="lastName" /></FONT>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_EMAIL" text="Email" /><font color="red">*</font></label> 
					<form:input path="email" maxlength="50" cssClass="form-control" /> <FONT color="red"><form:errors path="email" /></FONT>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_COUNTRY" text="Country:" /><!-- <font color="red">*</font> -->
					</label>
					<label class="col-sm-4" style="margin-top:4px;"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan" /></label> 
					<%--  commenting for making country as by default SouthSudan,
						by vineeth ,on 12-11-2018 
					<select class="dropdown chosen-select" id="countryId" name="countryId">
					<option value=""><spring:message code="LABEL_SELECT"
						text="--Please Select--" /> 
					</option>
					
					<c:set var="lang"
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
					
				</select> --%>
					
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MOBILE_NUM" text="Mobile Number" /><font color="red">*</font></label> 
					 <form:input path="mobileNumber" id="mobileNumber" maxlength="${mobilenumberLength}" cssClass="form-control" /> 			
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_STATUS" text="Status" /><font color="red">*</font></label> 
					<c:if test="${ action eq 'add' }">
					<c:set var="flag" value="true" />
					</c:if>
				<form:radiobutton path="status" value="N" /><spring:message code="LABEL_ACTIVE" text="Active" /> &nbsp; &nbsp;
				<form:radiobutton path="status" value="Y" disabled="${flag}"/><spring:message code="LABEL_DEACTIVATE" text="Inactive" /> 
				<FONT color="red"><form:errors path="status" /></FONT>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message  code="LABEL_LANGUAGE" text="Language" /><!-- <font color="red">*</font> -->
					</label> 
					<label class="col-sm-4" style="margin-top:4px;"><spring:message
									code="LABEL_DEFAULT_LANGUAGE" text="English" /></label>
				
				<%-- commenting to make a default language as English,
									by vineeth on 12-11-2018
				
				<form:select class="dropdown chosen-select" path="language">
					<form:options items="${filteredLanguage}" itemLabel="description" itemValue="languageCode" />
				<!-- Abu kalam azad   Date:24/07/2018 purpose:removed unwanted language according to bug 5767 start... -->
				<c:forEach items="${language}" var="language">
													<c:if test="${language.languageCode eq 'en_US'}">
														<option value="<c:out value="${language.languageCode}"/>"
															<c:if test="${webUserDTO.language eq 'en_US'}">selected=true</c:if>>
															<c:out value="${language.description}" />
														</option>
													</c:if>
													<c:if test="${language.languageCode eq 'fr_FR'}">
														<option value="<c:out value="${language.languageCode}"/>"
															<c:if test="${webUserDTO.language eq 'fr_FR'}">selected=true</c:if>>
															<c:out value="${language.description}" />
														</option>
													</c:if>
													<c:if test="${language.languageCode eq 'pt_PT'}">
														<option value="<c:out value="${language.languageCode}"/>"
															<c:if test="${webUserDTO.language eq 'pt_PT'}">selected=true</c:if>>
															<c:out value="${language.description}" />
														</option>
													</c:if>
												</c:forEach>
												<!-- ...End -->
					</form:select> --%>
				</div>
			</div>
			<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankadmin,ROLE_ccadmin,ROLE_bankgroupadmin,ROLE_superadmin,ROLE_mGurush,ROLE_businesspartnerL1,ROLE_businesspartnerL2,ROLE_businesspartnerL3">
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_USER_TYPE" text="User Type" />
						<c:if test="${webUserDTO.roleId eq null }">
						<font color="red">*</font>
						</c:if>
				</label> 			
					<c:if test="${webUserDTO.roleId ne null }">
						
						  <c:if test="${webUserDTO.roleId == 1}">
												 <c:set var="description" ><spring:message code="LABEL_GIM_ADMIN" text="Gim Admin"/></c:set>
												 </c:if>
												<c:if test="${webUserDTO.roleId==2}">
								
												 <c:set var="description" ><spring:message code="LABEL_BANK_ADMIN" text="Bank Admin"/> </c:set></c:if>
								
												<c:if test="${webUserDTO.roleId==3}">
												<c:set var="description" ><spring:message code="LABEL_BANK_TELLER" text="Bank Teller"/> </c:set>
												</c:if>
													<c:if test="${webUserDTO.roleId==4}">
													<c:set var="description" ><spring:message code="LABEL_CUSTOMER_CARE_ADMIN" text="Customer Care Admin"/>
													</c:set>
													</c:if>
										
												<c:if test="${webUserDTO.roleId==5}">
										         <c:set var="description" ><spring:message code="LABEL_CUSTOMER_CARE_EXECUTE" text="Customer Care Executive"/></c:set>
										        </c:if>
										        
												<c:if test="${webUserDTO.roleId==7}">
												<c:set var="description" ><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Bank Group Admin"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==8}">
												<c:set var="description" ><spring:message code="LABEL_PARAMETER" text="Parameter"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==9}">
												<c:set var="description" ><spring:message code="LABEL_CONTROL" text="Control"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==10}">
												<c:set var="description" ><spring:message code="LABEL_ACOUNTING" text="Accounting"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==12}">
												<c:set var="description" ><spring:message code="LABEL_BRANCH_MANAGER" text="Branch Manager"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==13}">
												<c:set var="description" ><spring:message code="LABEL_RELATIONSHIP_MANAGER" text="Relationship Manager"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==14}">
												<c:set var="description" ><spring:message code="LABEL_PERSONAL_RELATIONSHIP_EXEC" text="Personal Relationship Exec"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==15}">
												<c:set var="description" ><spring:message code="LABEL_SUPER_ADMIN" text="Super Admin"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==7}">
												<c:set var="description" ><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Bank Group Admin"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==16}">
												<c:set var="description" ><spring:message code="LABEL_SUPPORT_BANK" text="Support Customer Care"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==17}">
												<c:set var="description" ><spring:message code="LABEL_GIM_BACKOFFICE_LEAD" text="Gim Backoffice Lead"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==18}">
												<c:set var="description" ><spring:message code="LABEL_GIM_BACKOFFICE_EXEC" text="Gim Backoffice Exec"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==19}">
												<c:set var="description" ><spring:message code="LABEL_GIM_SUPPORT_LEAD" text="Gim Support Lead"/></c:set>
												</c:if>	
																							
												<c:if test="${webUserDTO.roleId==20}">
												<c:set var="description" ><spring:message code="LABEL_GIM_SUPPORT_EXEC" text="Gim Support Exec"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==21}">
												<c:set var="description" ><spring:message code="LABEL_INFORMATION_DESK" text="Information Desk"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==22}">
												<c:set var="description" ><spring:message code="LABEL_OPERATION" text="Operation"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==23}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_ADMIN" text="Business Partner Admin"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==24}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_L1_USER" text="Business Partner L1"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==25}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==26}">
												<c:set var="description" ><spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></c:set>
												</c:if>
												
												<c:if test="${webUserDTO.roleId==27}">
												<c:set var="description" ><spring:message code="LABEL_SALES_OFFICERS" text=" Sales Officer"/></c:set>
												</c:if> 
												
												<c:if test="${webUserDTO.roleId==28}">
												<c:set var="description" ><spring:message code="LABEL_BULKPAY_PARTNER_ADMIN" text=" Bulk Payment Partner"/></c:set>
												</c:if> 
												
												<c:if test="${webUserDTO.roleId==29}">
												<c:set var="description" ><spring:message code="LABEL_COMMERCIAL_OFFICER" text="Commercial Officer"/></c:set>
												</c:if> 
												
												<c:if test="${webUserDTO.roleId==30}">
												<c:set var="description" ><spring:message code="LABEL_SUPPORT_CALL_CENTER" text="Support Call center"/></c:set>
												</c:if> 
												
												<form:hidden path="roleId" id="roleId" cssClass="form-control"/>	
												<label><c:out value="${description}"/></label>
																					
						 						
					</c:if> 
					<c:if test="${webUserDTO.roleId eq null }">
					<form:select path="roleId" id="roleId"	cssClass="dropdown chosen-select">
						<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
												<authz:authorize ifAllGranted="ROLE_admin">
														<%-- <form:option value="1"><spring:message code="LABEL_GIM_ADMIN" text="Gim Admin"/></form:option>
													    <form:option value="15"><spring:message code="LABEL_SUPER_ADMIN" text="Super Admin"/> </form:option> --%>
														<form:option value="7"><spring:message code="LABEL_BANK_GROUP_ADMIN" text="Bank Group Admin"/> </form:option>
														<%-- <form:option value="5"> <spring:message code="LABEL_CUSTOMER_CARE_EXECUTE" text="Customer Care Executive"/></form:option> --%>
														<%-- <form:option value="17"><spring:message code="LABEL_GIM_BACKOFFICE_LEAD" text="Office Lead"/></form:option>
													    <form:option value="18"><spring:message code="LABEL_GIM_BACKOFFICE_Exec" text="Office Exec"/> </form:option> 
														<form:option value="19"><spring:message code="LABEL_GIM_SUPPORT_LEAD" text="Support Lead"/> </form:option>
														<form:option value="20"> <spring:message code="LABEL_GIM_SUPPORT_EXEC" text="Support Exec"/></form:option>
														<%-- <form:option value="23"> <spring:message code="LABEL_BUSINESS_PARTNER_ADMIN" text="Business Partner Admin"/></form:option> --%>
													</authz:authorize>
													
														<%-- <authz:authorize ifAllGranted="ROLE_mGurush,ROLE_bankadmin">
														<form:option value="24"> <spring:message code="LABEL_BUSINESS_PARTNER_L1" text="Business Partner L1"/></form:option>
														</authz:authorize> --%>
														
														<authz:authorize ifAllGranted="ROLE_businesspartnerL1">
														<form:option value="25"> <spring:message code="LABEL_BUSINESS_PARTNER_L2" text="Business Partner L2"/></form:option>
														<form:option value="27"> <spring:message code="LABEL_SALES_OFFICERS" text="Sales Officers"/></form:option>
														</authz:authorize>
														
														<authz:authorize ifAllGranted="ROLE_businesspartnerL2">
														<form:option value="26"> <spring:message code="LABEL_BUSINESS_PARTNER_L3" text="Business Partner L3"/></form:option>
														<form:option value="27"> <spring:message code="LABEL_SALES_OFFICERS" text="Sales Officers"/></form:option>
														</authz:authorize>
														
														<authz:authorize ifAllGranted="ROLE_businesspartnerL3">
														<form:option value="27"> <spring:message code="LABEL_SALES_OFFICERS" text="Sales Officers"/></form:option>
														</authz:authorize>
														
													<authz:authorize ifAllGranted="ROLE_bankadmin">
														<form:option value="3"><spring:message code="LABEL_BANK_TELLER" text="Supervisor"/></form:option>
													<%-- 	<form:option value="8"><spring:message code="LABEL_PARAMETER" text="Parameter"/></form:option>
														<form:option value="9"> <spring:message code="LABEL_CONTROL" text="Control"/></form:option> --%>
														<form:option value="10"><spring:message code="LABEL_ACOUNTING" text="Accounting"/></form:option>
														<form:option value="22"><spring:message code="LABEL_OPERATION" text="Operation"/></form:option>
														<%-- <form:option value="21"><spring:message code="LABEL_INFORMATION_DESK" text="Information Desk"/></form:option> --%>
														<form:option value="16"><spring:message code="LABEL_SUPPORT_BANK" text="Support Customer Care"/></form:option>
														<%-- <form:option value="12"><spring:message code="LABEL_BRANCH_MANAGER" text="Branch Manager"/></form:option> 
														<form:option value="13"><spring:message code="LABEL_RELATIONSHIP_MANAGER" text="Relationship Manager"/></form:option>
														<form:option value="14"><spring:message code="LABEL_PERSONAL_RELATIONSHIP_EXEC" text="Personal Relationship Exec"/></form:option>--%>
														<form:option value="24"> <spring:message code="LABEL_BUSNIESS_PARTNER_ADMIN" text="Principal Agent Admin"/></form:option>
														<form:option value="28"> <spring:message code="LABEL_BULKPAY_PARTNER_ADMIN" text="BulkPay Partner Admin"/></form:option>
														<form:option value="29"> <spring:message code="LABEL_COMMERCIAL_OFFICER" text="Commercial Officer"/></form:option>
														<form:option value="30"> <spring:message code="LABEL_SUPPORT_CALL_CENTER" text="Support Call center"/></form:option>
													</authz:authorize>
													<authz:authorize ifAllGranted="ROLE_superadmin">
														<form:option value="2"> <spring:message code="LABEL_BANK_ADMIN" text="Bank Admin"/></form:option>
													</authz:authorize>
													<authz:authorize ifAnyGranted="ROLE_ccadmin">
														<form:option value="4"><spring:message code="LABEL_CUSTOMER_CARE_ADMIN" text="Customer Care Admin"/></form:option>
														<form:option value="5"> <spring:message code="LABEL_CUSTOMER_CARE_EXECUTE" text="Customer Care Executive"/></form:option>
													</authz:authorize>
													<authz:authorize ifAllGranted="ROLE_bankgroupadmin">
														<form:option value="2"> <spring:message code="LABEL_BANK_ADMIN" text="Bank Admin"/></form:option>
														<form:option value="3"><spring:message code="LABEL_BANK_TELLER" text="Bank Teller"/></form:option>
													</authz:authorize>
												</form:select> <FONT color="red"><form:errors path="roleId" /></FONT>
												</c:if>
				</div>
											
				<div class="col-sm-6 bankdet" id="">
												<label class="col-sm-5 bankdet" style="margin-top:4px;">
													<spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font>
												</label>
												<div class="bankdet" style="margin-top:4px;">
													<form:select path="bankId" class="dropdown" id="bankId">
														<form:option value="" selected="selected">
															<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
														</form:option>
														<form:options items="${masterData.bankList}" itemValue="bankId" itemLabel="bankName"></form:options>
													 </form:select> 
													 <font color="RED"><form:errors path="bankId" cssClass="" /></font>
												</div>
				</div>
											
												<div class="col-sm-6 bankgroup" >
													<label class="col-sm-5 bankgroup" style="margin-top:4px;">
														<spring:message code="LABEL_BANKGROUP" text="BankGroup" />
														<font color="red">*</font>
													</label>
													<div class="bankgroup" style="margin-top:4px;">
													<form:select id="groupId" class="dropdown" path="bankGroupId">
														<form:option value="" selected="selected">
															<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
														</form:option>
														<form:options items="${masterData.bankGroupList}" itemLabel="bankGroupName" itemValue="bankGroupId" />
													</form:select>
												<font color="RED"><form:errors path="bankGroupId"></form:errors></font>
												</div>
												</div>
			</div>
									<div class="row">			
												<div class="col-sm-6 bank">
													<label class="col-sm-5 bank" style="margin-top:4px;">
														<spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font>
													</label>
													<div class="bank" style="margin-top:4px;">
														<form:select path="bank" class="dropdown" id="bank">
														<form:option value="" selected="selected">
															<spring:message code="LABEL_WUSER_SELECT"
																text="--Please Select--"></spring:message>
														</form:option>
														<form:options items="${masterData.bankList}" itemValue="bankId" itemLabel="bankName"></form:options>
													    </form:select> 
													    <font color="RED"><form:errors path="bankId" cssClass="" /></font>
												    </div>
												</div>
												
												<div class="col-sm-6">
												<label class="col-sm-5 bankdet" style="margin-top:4px;"><spring:message code="LABEL_BRANCH" text="Branch" /><font color="red">*</font></label>
													<div id="branchdiv" class="bankdet" style="margin-top:4px;">
														<form:select id="branchId" class="dropdown" path="branchId">
														<form:option value="" selected="selected">
															<spring:message code="LABEL_WUSER_SELECT"
																text="--Please Select--"></spring:message>
														</form:option>
														<form:options items="${masterData.branchList}"
															itemLabel="location" itemValue="branchId" />
														</form:select>
													<font color="RED"><form:errors path="branchId"></form:errors></font>
													</div>
											</div>
									</div>
									
									
									<div class="row">			
												<div class="col-sm-6">
												<label class="col-sm-5 bulkdet" style="margin-top:4px;"><spring:message code="LABEL_BULKPAYMENT_PARTNERS" text="Bulk partners" /><font color="red">*</font></label>
													<div id="bulkdetdiv" class="bulkdet" style="margin-top:4px;">
														<c:if test="${webUserDTO eq null }">
														<form:select id="bulkPaymentPartnerId" class="dropdown" path="bulkPaymentPartnerId">
														<form:option value="" selected="selected">
															<spring:message code="LABEL_WUSER_SELECT"
																text="--Please Select--"></spring:message>
														</form:option>
														<c:forEach items="${bulkPaymentPartnerList}" var="ct">
																<form:option value="${ct.id}"><c:out value="${ct.name}" /></form:option>
														</c:forEach> 
														</form:select>
														</c:if>
														
														
														<c:if test="${webUserDTO ne null }"> 
														<c:choose>
														<c:when test='${businessPartnerName ne null && businessPartnerName != "undefined" && businessPartnerName != ""}'>
														
														<form:hidden path="bulkPaymentPartnerId" id="bulkPaymentPartnerId" cssClass="form-control" value="${businessPartnerName.id}"/>	
											<%-- 	<form:input path="businessPartnerId" cssClass="form-control" value="${businessPartnerName.name}" readonly="true"/> --%>
												<label><c:out value="${businessPartnerName.name}"/></label>
														</c:when>
														<c:otherwise>
														<form:select id="bulkPaymentPartnerId" class="dropdown" path="bulkPaymentPartnerId">
														<form:option value="" selected="selected" >
																<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
															</form:option>
															<c:forEach items="${bulkPaymentPartnerList}" var="ct">
																<form:option value="${ct.id}"><c:out value="${ct.name}" /></form:option>
														</c:forEach> 
														</form:select>
														</c:otherwise>	
														</c:choose>
														</c:if>	
														
													<font color="RED"><form:errors path="bulkPaymentPartnerId"></form:errors></font>
													</div>
											</div>
									</div>
									
									
									
									<div class="row">
												<!-- vinod changes  -->
										 <div class="col-sm-6">
												<label class="col-sm-5 businessdet" style="margin-top:4px;">
													<c:if test="${roleAccess eq 2}">
													<spring:message code="LABEL_PARTNERS" text="Partners" />
													</c:if>
													<c:if test="${roleAccess ne 2}">
													<spring:message code="TITLE_VIEW_BUISNESS_PARTNERS" text="Partners" />
													</c:if>
														<c:if test="${webUserDTO eq null }">
													<font color="red">*</font>
													</c:if>
												</label>
													<div id="businessdiv" class="businessdet" style="margin-top:4px;">
														<c:if test="${webUserDTO eq null }">
														<form:select id="businessPartnerId" class="dropdown" path="businessPartnerId">
														
															<form:option value="" selected="selected" >
																<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
															</form:option>
														<c:forEach items="${businessNameList}" var="ct">
																<form:option value="${ct.id}"><c:out value="${ct.name}" /></form:option>
														</c:forEach>
														</form:select>
														</c:if>
														
													 	<c:if test="${webUserDTO ne null }"> 
														<c:choose>
														<c:when test='${businessPartnerName ne null && businessPartnerName != "undefined" && businessPartnerName != ""}'>
														
														<form:hidden path="businessPartnerId" id="businessPartnerId" cssClass="form-control" value="${businessPartnerName.id}"/>	
											<%-- 	<form:input path="businessPartnerId" cssClass="form-control" value="${businessPartnerName.name}" readonly="true"/> --%>
												<label><c:out value="${businessPartnerName.name}"/></label>
														</c:when>
														<c:otherwise>
														<form:select id="businessPartnerId" class="dropdown" path="businessPartnerId">
														<form:option value="" selected="selected" >
																<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
															</form:option>
															<c:forEach items="${businessNameList}" var="ct">
																<form:option value="${ct.id}"><c:out value="${ct.name}" /></form:option>
														</c:forEach> 
														</form:select>
														</c:otherwise>	
														</c:choose>
														</c:if>	
														
													</div>
											</div>
											
					<div class="col-sm-6">
					<label class="col-sm-5 accessmodedet" style="margin-top:4px;"><spring:message code="LABEL_ACCESS_MODE" text="Access Mode" /><font color="red">*</font></label> 
						<div id="accessmodediv" class="accessmodedet" style="margin-top:4px;">
						<form:radiobutton path="accessMode" value="10" /><spring:message code="LABEL_WEB_MODE" text="Web" /> &nbsp; &nbsp;
						<form:radiobutton path="accessMode" value="20" /><spring:message code="LABEL_MOBILE_MODE" text="Mobile" /> &nbsp; &nbsp;
						<form:radiobutton path="accessMode" value="30" /><spring:message code="LABEL_All_MODE" text="All" />  &nbsp; &nbsp;
						<form:errors path="accessMode" /></FONT>
					</div>
				  	</div>
					</div>	 
							<!-- vinod changes  -->
											
										
								
				<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5 otp" style="margin-top:4px;"><spring:message code="LABEL_OTPSTATUS" text="OTP" /><font color="red">*</font></label> 
					<form:radiobutton path="TFA" value="1" /><spring:message code="LABEL_ENABLE" text="Enable" /> &nbsp; &nbsp;
					<form:radiobutton path="TFA" value="0" /><spring:message code="LABEL_DISABLE" text="Disable" /> <FONT color="red">
					<form:errors path="TFA" /></FONT>
				</div>
				
				<%-- <div class="col-sm-6">
					<label class="col-sm-5 otp" style="margin-top:4px;"><spring:message code="LABEL_ACCESS_MODE" text="Access Mode" /><font color="red">*</font></label> 
					<form:radiobutton path="accessMode" value="10" /><spring:message code="LABEL_WEB_MODE" text="Web" /> &nbsp; &nbsp;
					<form:radiobutton path="accessMode" value="20" /><spring:message code="LABEL_MOBILE_MODE" text="Mobile" /> &nbsp; &nbsp;
					<form:radiobutton path="accessMode" value="30" /><spring:message code="LABEL_All_MODE" text="All" />  &nbsp; &nbsp;
					<form:errors path="accessMode" /></FONT>
				</div> --%>
				</div>
		</authz:authorize>
			</div>
			<div class="col-sm-6 col-sm-offset-10">
			<c:choose>
				<c:when test="${ action eq 'add' }">
				<script>window.onload=function(){ changeStatus(true);};</script>
					<c:set var="buttonName" value="LABEL_ADD" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
				</c:otherwise>
			</c:choose>
				<div class="btn-toolbar">
				<input type="button" id="submitButton" class="btn-primary btn" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>" onclick="validate('${ buttonName }');"></input> 
				<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
				</div>
			</div>
		<br /><br /><br/><br/>
		<div id="mobileNum" style="visibility:hidden"/>
		</form:form>
</div>
</div>
<script type="text/javascript">
			var mobileNumberlen='${mobilenumberLength}';	
			document.getElementById('mobileNum').innerHTML=mobileNumberlen;
			window.onload=function(){
				check();}; 
</script>

</body>
</html>