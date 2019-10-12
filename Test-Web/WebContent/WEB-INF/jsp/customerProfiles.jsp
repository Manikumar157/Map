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
<script type="text/javascript">
	
	function characterLimit(obj){
        if(obj.value.length>255){
               obj.value=obj.value.substring(0,255);
       }
        return false;
	}

	function saveUserProfile(){
		var customerProfile=document.customerProfileForm.profileName.value;
		var description=document.customerProfileForm.description.value.trim();
		var authorizedAmount=document.customerProfileForm.authorizedAmount.value;
		var profileType=document.customerProfileForm.profileType.value;
		 var pattern='^[A-Za-zÀ-ÿ ]*$';
		 var singleSpace=/^(([ a-zA-ZÀ-ÿ]))+(\s\s([ a-zA-ZÀ-ÿ])*$)/;
		 var numPattern='^[0-9]+(\.[0-9]{1,2})?$';
		if(customerProfile==""){
			alert('<spring:message code="NotEmpty.branchDTO.profileName" text="Please enter Customer Profile"/>');
	           return false;
		} else if(customerProfile.search(pattern)==-1){
			alert('<spring:message code="ENTER_ONLY_ALPHABETS_IN_CUSTOMER_PROFILE" text="Please enter only alphabets in Customer Profile field"/>');
	           return false;       
		} /* else if(customerProfile.charAt(0) == " " || customerProfile.charAt(customerProfile.length-1) == " "){
			alert('<spring:message code="CUSTOMER_PROFILE_VALID" text="Please enter a valid Customer Profile"/>');
	           return false;
		} else if(!customerProfile.search(singleSpace)){
			alert('<spring:message code="ONLY_SINGLE_SPACE_IN_CUSTOMER_PROFILE" text="Customer Profile must not contain more than one space between words"/>');
			return false;profileType
		}*/  else if(authorizedAmount==""){
			alert('<spring:message code="VALIDATION_MAX_ACC_BAL" text="Please enter max account balance"/>');
	           return false;
		}else if(profileType==""){
			alert('<spring:message code="VALIDATION_PROFILE_TYPE" text="Please Select Profile Type"/>');
	           return false;
		}else if(authorizedAmount.search(numPattern)==-1){
			alert('<spring:message code="VALIDATION_UPTO_TWO_DECIMAL_PLACES" text=" Please enter valid account balance amount with 2 decimal values"/>');
	           return false;
		}else if(description==""){
			alert('<spring:message code="NotEmpty.branchDTO.description" text="Please enter Description"/>');
	           return false;
		 }/* else if(description.charAt(0) == " " || description.charAt(description.length-1) == " "){
		  	 alert('<spring:message code="LABEL.DESCRIPTION.BLANK1" text="Please remove the white spaces from desription"/>');        
		     return false;       
		}else if(description.search(pattern)==-1){
			alert('<spring:message code="ENTER_ONLY_ALPHABETS_IN_DESCRIPTION" text="Please enter only alphabets in Description field"/>');
	           return false; 
		} else if(!description.search(singleSpace)){
			alert('<spring:message code="ONLY_SINGLE_SPACE_IN_DESCRIPTION" text="Description must not contain more than one space between words"/>');
			return false;
		} */   
		document.customerProfileForm.method="post";
		document.customerProfileForm.submit();
	}
	
	function cancelUserProfile(){
		document.customerProfileForm.action="showCustomerProfiles.htm";
		document.customerProfileForm.submit();
	}

	function editUserProfile(profileId){
		document.customerProfileForm.profileId.value=profileId;
		document.customerProfileForm.action="editCustomerProfiles.htm";
		document.customerProfileForm.method = "post";
		document.customerProfileForm.submit();
	}
	
	 function textCounter(field,cntfield,maxlimit) {
		 if (field.value.length > maxlimit) // if too long...trim it!
		 field.value = field.value.substring(0, maxlimit);
		 // otherwise, update 'characters left' counter
		 else
		 cntfield.value = maxlimit - field.value.length;
	 }
	
</script>
</head>
<body>

<div class="col-lg-12">
<form:form name="customerProfileForm" id="customerProfileForm" class="form-inline" action="saveCustomerProfile.htm" method="post" commandName="customerProfileDTO">
<jsp:include page="csrf_token.jsp"/>
<div class="box" style="height:419px;">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_ADD_CUSTOMER_PROFILE" text="Add Customer Profile" /></span>
		</h3>
	</div>
	<div class="col-sm-4 col-sm-offset-4">
		<div class="col-sm-11 col-sm-offset-2" style="color: #ba0101; font-weight: bold; font-size: 12px;">
			<spring:message code="${message}" text="" />
		</div>	
	</div>
	<div class="box-body">
	<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 22px;"><br />
		<authz:authorize ifAnyGranted="ROLE_addCustomerProfilesAdminActivityAdmin">	
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message	code="LABEL_CUSTOMER_PROFILE" text="Customer Profile" /><font color="red">*</font></label> 
					<form:input path="profileName" id="profileName" cssClass="form-control" maxlength="50"/>
					<font color="red"><form:errors path="profileName"  /></font>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message	code="LABEL_AUTHORIZED_AMOUNT" text="Authorized Amount" /><font color="red">*</font></label> 
					<!--  changed by vineeth,on 13-08-2018, bug no:5876, value should not display in exponential format -->
					<%-- <form:input path="authorizedAmount" id="authorizedAmount" cssClass="form-control"/> --%>
					<fmt:formatNumber value="${customerProfileDTO.authorizedAmount}" var="stat" pattern="0.0" maxIntegerDigits="9"></fmt:formatNumber>
					<form:input path="authorizedAmount" id="authorizedAmount"  value="${stat}" maxlength="9" cssClass="form-control"/>
					<!-- changes over -->
					<font color="red"><form:errors path="authorizedAmount" /></font>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message	code="LABEL_PROFILE_TYPE" text="Profile Type" /><font color="red">*</font></label> 
					
					<form:select path="profileType" id="profileType" 		cssClass="dropdown chosen-select">
						<form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
						<form:option value="0"><spring:message code="LABEL_CUSTOMER" text="Customer"/></form:option>
						<form:option value="1"><spring:message code="LABEL_AGENT" text="Agent"/> </form:option>
						<form:option value="2"><spring:message code="LABEL_MERCHANT" text="Merchant"/> </form:option>
						<%-- <form:option value="3"><spring:message code="LABEL_MENU_AGENT_SOLE_MERCHANT" text="Agent Sole Merchant"/> </form:option> --%>
						<%-- <form:option value="99"><spring:message code="LABEL_SELF" text="Self"/> </form:option> --%>
														
					</form:select> <FONT color="red"><form:errors path="profileType" /></FONT>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_DESCRIPTION" text="Description" /><font color="red">*</font></label> 
					<form:textarea path="description" id="description" cols="19" rows="4" onKeyDown="textCounter(document.customerProfileForm.description,180,180)" onKeyUp="textCounter(document.customerProfileForm.description,180,180)"/>
					<font color="red"><form:errors path="description" /></font>
				</div>
			</div>
			<c:if test="${customerProfileDTO.profileId ne null}">
			<div class="col-sm-5 col-sm-offset-7">
				<input type="button" onclick="javascript:saveUserProfile();" class="btn btn-primary" value="<spring:message code="LABEL_UPDATE" text="Update" />" />
				<div class="space"></div>
				<input type="button" onclick="javascript:cancelUserProfile();" class="btn btn-primary" value="<spring:message code="LABEL_CANCEL" text="Cancel" />" /><br/><br/>
			</div><br/><br/>
			</c:if>
			<c:if test="${customerProfileDTO.profileId eq null}">
			<div class="box-footer">
				<input type="button" onclick="javascript:saveUserProfile();" class="btn btn-primary pull-right" value="<spring:message code="LABEL_SAVE" text="Save" />" />
			</div>
			</c:if>
			</authz:authorize>
			</div>
		
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_CUSTOMER_PROFILE" text="Customer Profile" /></th>
								<th><spring:message code="LABEL_AUTHORIZED_AMOUNT" text="Authorized Amount" /></th>
								<th><spring:message code="LABEL_DESCRIPTION" text="Description" /></th>
								<th><spring:message code="LABEL_PROFILE_TYPE" text="Type" /></th>
								<authz:authorize ifAnyGranted="ROLE_editCustomerProfilesAdminActivityAdmin">	
									<th><spring:message code="LABEL_ACTION" text="Action" /></th>
								</authz:authorize>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${page.results}" var="profileData">
							<tr>
								<td><c:out value="${profileData.profileName}" /></td>
								<fmt:setLocale value="en_US" scope="session"/>
								<td><fmt:formatNumber value="${profileData.authorizedAmount}" pattern="0.0"></fmt:formatNumber></td>
								<td><c:out value="${profileData.description}" /></td>
								<td>
									<c:if test="${profileData.customerType eq 0}">
										<c:out value="Customer"></c:out>
									</c:if>
									<c:if test="${profileData.customerType eq 1}">
										<c:out value="Agent"></c:out>
									</c:if>
									<c:if test="${profileData.customerType eq 2}">
										<c:out value="Merchant"></c:out>
									</c:if>
									<c:if test="${profileData.customerType eq 3}">
										<c:out value="Agent Sole Merchant"></c:out>
									</c:if>
									<%-- <c:if test="${profileData.customerType eq 99}">
										<c:out value="Self"></c:out>
									</c:if> --%>
								
								
								</td>
								<authz:authorize ifAnyGranted="ROLE_editCustomerProfilesAdminActivityAdmin">
									<td><a href="javascript:editUserProfile('<c:out value="${profileData.profileId}"/>')"> <spring:message code="LABEL_EDIT" text="Edit" /></a></td>
								</authz:authorize>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<form:hidden path="profileId"/>
</form:form>
</div>	

<%-- <table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table>
			<tr>
				<td width="159" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><jsp:include page="left.jsp" /></td>
					</tr>
				</table>
				</td>
				<td width="844" align="left" valign="top">
				<table width="98%" border="0" height="400px" align="left" cellpadding="0" cellspacing="0">
					<tr height="20px">
						<td align="left" class="headding" colspan="2"
											style="font-size: 15px; font-weight: bold;"><spring:message
											code="LABEL_ADD_CUSTOMER_PROFILE" text="Add Customer Profile" /></td>
					</tr>
					<tr height="10px">
						<td align="center" colspan="2">
						<div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
							code="${message}" text="" /></div>
						</td>
					</tr>
					<form:form name="customerProfileForm" action="saveCustomerProfile.htm"
						method="post" commandName="customerProfileDTO">
						<tr height="50px">
							<td valign="top" colspan="2">
							<authz:authorize ifAnyGranted="ROLE_addCustomerProfilesAdminActivityAdmin">
							<table
								style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
								align="center" width="60%" cellpadding="4" cellspacing="4">
								
								<tr>
									<td width="15%"></td>
									<td><spring:message	code="LABEL_CUSTOMER_PROFILE" text="Customer Profile" /><font color="red">*</font></td>
									<td>
										<form:input path="profileName" id="profileName" cssClass="text_feild" maxlength="50"/>
										<font color="red"><form:errors path="profileName" /></font>
									</td>
								</tr>
								<tr>
									<td width="15%"></td>
									<td><spring:message	code="LABEL_AUTHORIZED_AMOUNT" text="Authorized Amount" /><font color="red">*</font></td>
									<td>
										<form:input path="authorizedAmount" id="authorizedAmount" cssClass="text_feild" maxlength="50"/>
										<font color="red"><form:errors path="authorizedAmount" /></font>
									</td>
								</tr>
								<tr>
									<td></td>
									<td><spring:message code="LABEL_DESCRIPTION" text="Description" /><font color="red">*</font></td>
									<td>
										<form:textarea path="description" id="description" cols="26" rows="5" 
											class="text_area" onKeyDown="textCounter(document.customerProfileForm.description,180,180)" onKeyUp="textCounter(document.customerProfileForm.description,180,180)"/>
										<font color="red"><form:errors path="description" /></font>
									</td>
								</tr>
								<c:if test="${customerProfileDTO.profileId ne null}">
									<tr>
								    	<td align="right" colspan="3"><input type="button" onclick="javascript:saveUserProfile();" value="<spring:message code="LABEL_UPDATE" text="Update" />" />
								    	<input type="button" onclick="javascript:cancelUserProfile();" value="<spring:message code="LABEL_CANCEL" text="Cancel" />" /></td>
									</tr>
								</c:if>
								<c:if test="${customerProfileDTO.profileId eq null}">
									<tr>
								    	<td align="right" colspan="3"><input type="button" onclick="javascript:saveUserProfile();" value="<spring:message code="LABEL_SAVE" text="Save" />" /></td>
									</tr>
								</c:if>
								
							</table>
								</authz:authorize>
							</td>
							
						</tr>
		
						<tr>
							<td valign="top" colspan="2">
								<table width="60%" align="center" border="0" cellpadding="0" cellspacing="0">
									<tr class="tableheading">
										<th align="center"><spring:message
											code="LABEL_CUSTOMER_PROFILE" text="Customer Profile" /></th>
										<th align="center"><spring:message
											code="LABEL_AUTHORIZED_AMOUNT" text="Authorized Amount" /></th>	
										<th align="center"><spring:message
											code="LABEL_DESCRIPTION" text="Description" /></th>
										<authz:authorize ifAnyGranted="ROLE_editCustomerProfilesAdminActivityAdmin">	
										<th align="center"><spring:message
											code="LABEL_ACTION" text="Action" /></th>
											</authz:authorize>
									</tr>
									<%int i=0; %>
									<c:forEach items="${page.results}" var="profileData">
										<tr height="23px" class="" <% if(i%2!=0)%> bgColor="#d2d3f1" <% i++; %>>
											<td align="center"><c:out
												value="${profileData.profileName}" /></td>
												<fmt:setLocale value="en_US" scope="session"/>
												<td align="center"><fmt:formatNumber value="${profileData.authorizedAmount}" pattern="0.0"></fmt:formatNumber></td>	
											<td align="center"><c:out value="${profileData.description}" /></td>
											<authz:authorize ifAnyGranted="ROLE_editCustomerProfilesAdminActivityAdmin">
											<td align="center"><a
												href="javascript:editUserProfile('<c:out value="${profileData.profileId}"/>')">
											<spring:message code="LABEL_EDIT" text="Edit" /></a></td>
											</authz:authorize>
											
										</tr>
									</c:forEach>
									<tr  bgcolor="#30314f" style="color:white;">
							 			<c:if test="${page.totalPages>1}">
								            <td colspan="7" align="right" height="25px">
								            <a	<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?pageNumber=<c:out value="1" />" </c:if> style="color:white;"><c:out value="[ First " /></a>
											<a	<c:if test="${page.currentPage!='1'}">href="<c:out value="${page.requestPage}"/>?pageNumber=<c:out value="${page.currentPage-1}" />" </c:if> style="color:white;"><c:out value=" / Prev ]"/></a> 
											<c:forEach var="i" begin="${page.startPage}" end="${page.endPage}" step="1">
											<c:if test="${page.currentPage!=i}"> <a href="<c:out value="${page.requestPage}"/>?pageNumber=<c:out value="${i}" />" style="color:white;"><c:out value="${i}" /></a></c:if>
											<c:if test="${page.currentPage==i}"><b><c:out value="${i}" /></b></c:if>
											</c:forEach> 
											<a <c:if test="${page.currentPage<page.totalPages}">href="<c:out value="${page.requestPage}"/>?pageNumber=<c:out value="${page.currentPage+1}" />"</c:if> style="color:white;"><c:out value="[ Next / " /></a> 
											<a <c:if test="${page.totalPages!=page.currentPage}">href="<c:out value="${page.requestPage}"/>?pageNumber=<c:out value="${page.totalPages}" />"</c:if> style="color:white;"><c:out value="Last ]" /></a>
								            </td>
							            </c:if>
								    </tr>	
								</table>
							</td>
						</tr>
						<form:hidden path="profileId"/>
					</form:form>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table> --%>
</body>
</html>
