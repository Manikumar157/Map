<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>

<script
	src="<%=request.getContextPath()%>/js/jquery.min.2.0.0.js"></script>

<style type="text/css">
.tabs {
	border-bottom: 1px solid #CCCCCC;
}

ul.tabs li {
	list-style: none;
	float: left;
	padding: 0.4em 2em;
	text-decoration: none;
	color: #FFFFFF;
	font-size: 100%;
	line-height: 1.3;
	background-color: #5E6569;
	margin: 0 3px 0 0;
	border-radius: 4px 4px 0px 0px;
}

ul.tabs li:hover {
	background-color: #6D6E70;
	color: #FFFFFF;
	line-height: 1.3;
	list-style: none;
	float: left;
	margin: 0 3px 0 0;
	padding: 0.4em 2em;
}

ul.tabs {
	border-bottom-right-radius: 4px;
	clear: both;
	color: #222222;
	float: left;
	font-weight: bold;
	height: 28px;
	margin: 0;
	margin-left: 10px;
	padding: 0.2em 0.2em 0;
	width: 97%;
}

.data_store_field {
    height: 357px;
    overflow-y: scroll;
    border: 1px solid rgba(175, 168, 168, 0.49);
}
.data_header_field {
    background: #d2d3f1;
    margin-bottom: 10px;
    margin-top: 10px;
}
.header_align {
    margin-top: 17px;
}
.privilege_border {
    box-shadow: 0px 0px 12px 1px rgba(177, 172, 172, 0.59);
}
label {
    margin-left: 18px;
}
.col-sm-12.button_alignment {
    margin-top: 35px;
    text-align: center;
}
input.btn-primary.btn.assign_button {
    margin-right: 10px;
}
input.btn-default.btn.reset_button {
    margin-right: 10px;
}
.role_alignment {
    text-align: right;
    margin-left: -15px;
}
.user_alignment {
    text-align: right;
    margin-left: -15px;
}
.privilege_Form {
    height: 275px;
}
</style>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(".tabItems").show();
						$(".tabItems").hide();
						$("#tabs-1").show();

						$("#roleName")
								.change(
										function() {
											 document.getElementById("roleId").value = document
													.getElementById("roleName").value;
													$csrfToken = $("#csrfToken").val();
											$roleId = document
													.getElementById("roleName").value;
											$.post("getUsersByRoleId.htm",{
													roleId : $roleId,
													csrfToken : $csrfToken
													},
															function(data) {
																document.getElementById("usersId").innerHTML = "";
																document.getElementById("usersId").innerHTML = data;
																setTokenValFrmAjaxResp();
															});
										});
					});

	function displayTab(val) {
		$(".li_tabs").removeClass("active");
		$("#liTabs" + val).addClass("active");
		$(".tabItems").hide();
		$("#tabs-" + val).show();
	}

	function resetFrom() {
	}

	function canclePage() {
		onSubmit('privilegeAssignDTOForm', 'backToAssignPrivilege.htm');
	}

	function resetFrom() {
		onSubmit('privilegeAssignDTOForm', 'showAssignPrivilege.htm');
	}

	function checkAll(obj, topMenuId) {
		var chkBox = "ckBox" + topMenuId;
		if (obj.checked) {
			// Iterate each checkbox
			$('.' + chkBox).each(function() {
				this.checked = true;
			});
		} else {
			$('.' + chkBox).each(function() {
				this.checked = false;
			});
		}
	}

	function submitAPrevlForm() {
		document.privilegeAssignDTOForm.action = "assignPrivilegeDetails.htm";
		document.privilegeAssignDTOForm.submit();
	}

	function loadPriv() {
		if (document.getElementById("roleName").value == ""
				|| document.getElementById("roleName").value == " ") {
			alert("Please select Role");
			return false;
		}
		document.privilegeAssignDTOForm.action = "loadPrivilegeDetails.htm";
		document.privilegeAssignDTOForm.submit();
	}
</script>
<style>
.container {
	width: 900px;
} 
</style>
</head>
<body>

	<div class="col-md-12">
	<div class="box">
	<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_ASSIGN_PRIVILEGE"
							text="Assign Privilege" /></span>
				</h3>
			</div>
		<div class="col-sm-5 col-sm-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
			<br />
			
			<form:form class="form-inline privilege_form" name="privilegeAssignDTOForm"	id="privilegeAssignDTOForm" method="post"	commandName="assignPrivilegeUIDTO">
				<jsp:include page="csrf_token.jsp"/>
				<!-- /.box-header -->
				<div class="box-body table_border"	style="/* border: 1px solid; border-radius: 15px; */ width: 50%; margin: 0 auto;">
					<div class="row">
						<!-- <div class="col-md-3"></div> -->
						<div class="col-md-12">
							<label class="col-sm-5 role_alignment"><spring:message code="LABEL_ROLE" text="Role" /><font color="red">*</font></label>
							<div class="col-sm-6">
								<form:select path="roleName" cssClass="dropdown_big" >
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--" />
									</form:option>
									<form:options items="${roleList}" itemLabel="roleName"
										itemValue="roleId"></form:options>
								</form:select>
							</div>
							<font color="red">
							<form:errors path="roleName" /></font>
						</div>
						<form:hidden path="roleId" />
					</div>
					<br />
					<div class="row">
						<!-- <div class="col-md-3"></div> -->
						<div class="col-md-12">
							<label class="col-sm-5 user_alignment"><spring:message code="LABEL_USER" text="User" /></label>
							<div class="col-sm-6" id="usersId">
								<form:select path="userId" cssClass="dropdown_big">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--" />
									</form:option>
									<form:options items="${userList}" itemLabel="userName"
										itemValue="userName"></form:options>
								</form:select>
							</div>
							<font color="red">
							<form:errors path="userId" /></font>
						</div>
					</div>
					<br />
					<!-- /.box-body -->

					<div class="box-footer">
						<input type="button" class="btn btn-primary pull-right"	name="Next" value="Next" onclick="loadPriv();" />
					</div><br />
				</div><br />
				<c:if test="${assignPrivilegeUIDTO.applicationTypeDTOList.size() > 0}">
					<%-- <div class="box-body">
						<div class="table-responsive container">
							<form class="form-inline" role="form">
								<c:forEach
									items="${assignPrivilegeUIDTO.applicationTypeDTOList}"
									var="applicationName" varStatus="appRow">
									<table class="table table-hover"
										style="text-align: center; overflow-y: auto; height: 200px;">
										<thead>
											<tr class="header" bgColor="#d2d3f1">
												<div>
													<th style="width: 400px">Page</th>
												</div>
												<c:forEach items="${assignPrivilegeUIDTO.privilegeNames}"
													var="privilegeName" varStatus="subMenuRow">
													<div>
														<th><c:out value="${privilegeName}" /></th>
													</div>
												</c:forEach>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${applicationName.topMenuList}"
												var="topMenu" varStatus="topMenuRow">
												<c:set var="checkBox" value="ckBox${topMenu.topMenuId}"></c:set>
												<c:forEach items="${topMenu.leftMenuList}" var="leftMenu"
													varStatus="leftMenuRow">
													<tr>
														<td><strong><c:out
																	value="${leftMenu.leftMenuName}" /></strong></td>
														<c:forEach items="${leftMenu.subMenuList}" var="subMenu"
															varStatus="subMenuRow">
															<c:if test="${subMenu.applicable eq true}">
																<td class="column_lines" style="text-align: center;">
																	<form:checkbox
																		value="${leftMenu.leftMenuId}_${subMenu.subMenuId}"
																		path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue"
																		disabled="false" class="${checkBox}" />
																</td>
															</c:if>

															<c:if test="${subMenu.applicable eq false}">
																<td><form:checkbox
																		value="${leftMenu.leftMenuId}_${subMenu.subMenuId}"
																		path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue"
																		disabled="true" /></td>
															</c:if>
														</c:forEach>
													</tr>
												</c:forEach>
											</c:forEach>
										</tbody>
									</table>
								</c:forEach>
							</form>
						</div>
						<br />
						<br />
						<div class="col-sm-6 col-sm-offset-5">
								<input type="button" class="btn-primary btn" value="Assign"
									onclick="submitAPrevlForm()" /> <input type="button"
									class="btn-default btn" name="Reset" value="Reset"
									onclick="loadPriv();" />
						</div>
						<br />
					</div> --%>
					
					<div class="container">
					<form class="form-inline" role="form">
						<c:forEach	items="${assignPrivilegeUIDTO.applicationTypeDTOList}"	var="applicationName" varStatus="appRow">
							<div class="col-lg-12 privilege_border">
								<div class="col-lg-12 data_header_field">
									<div class="col-md-4 header1_text header_align">
										Page
									</div>
									<!-- menun order changed and functionality order changed by vineeth. on 26-07-2018, bug no:5711  -->
									<c:forEach items="${assignPrivilegeUIDTO.privilegeNames}"	var="privilegeName" varStatus="subMenuRow">
										<c:if test="${privilegeName ne 'Delete'}">
										<div class="col-md-2 header2_text header_align">
											<c:out value="${privilegeName}" />
										</div>
										</c:if>
									</c:forEach>
									<c:forEach items="${assignPrivilegeUIDTO.privilegeNames}"	var="privilegeName" varStatus="subMenuRow">
										<c:if test="${privilegeName eq 'Delete'}">
										<div class="col-md-2 header2_text header_align">
											<c:out value="${privilegeName}" />
										</div>
										</c:if>
									</c:forEach>
								<!-- 	// vineeth change over -->
								</div>

								<div class="col-lg-12 data_store_field">
									<c:forEach items="${applicationName.topMenuList}" var="topMenu" varStatus="topMenuRow">
										<c:set var="checkBox" value="ckBox${topMenu.topMenuId}"></c:set>
										<c:forEach items="${topMenu.leftMenuList}" var="leftMenu" varStatus="leftMenuRow">
											<div class="row">
												<div class="col-md-4 name_field">
													<strong><c:out value="${leftMenu.leftMenuName}" /></strong>
												</div>
												<!-- ************************ -->
												<c:forEach items="${leftMenu.subMenuList}" var="subMenu" varStatus="subMenuRow">
													<c:if test="${subMenu.subMenuId eq 1}">
														<c:if test="${subMenu.applicable eq true}">
															<div class="col-md-2 checkbox1_field">
																<label>
																	<form:checkbox value="${leftMenu.leftMenuId}_${subMenu.subMenuId}" path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="false" class="${checkBox}" />
																</label>
															</div>
														</c:if>
														<c:if test="${subMenu.applicable eq false}">
															<div class="col-md-2 checkbox2_field">
																<label>	
																	<form:checkbox	value="${leftMenu.leftMenuId}_${subMenu.subMenuId}"	path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="true" />
																</label>
															</div>
														</c:if>
													</c:if>
												</c:forEach>
												<!-- ************************ -->
												<c:forEach items="${leftMenu.subMenuList}" var="subMenu" varStatus="subMenuRow">
													<c:if test="${subMenu.subMenuId eq 2}">
														<c:if test="${subMenu.applicable eq true}">
														
															<div class="col-md-2 checkbox1_field">
																<label>
																	<form:checkbox value="${leftMenu.leftMenuId}_${subMenu.subMenuId}" path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="false" class="${checkBox}" />
																</label>
															</div>
														</c:if>
														<c:if test="${subMenu.applicable eq false}">
														
															<div class="col-md-2 checkbox2_field">
																<label>	
																	<form:checkbox	value="${leftMenu.leftMenuId}_${subMenu.subMenuId}"	path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="true" />
																</label>
															</div>
														</c:if>
													</c:if>
												</c:forEach>
												<!-- ************************ -->
												<!-- menun order changed and functionality order changed by vineeth. on 26-07-2018, bug no:5711  -->
												<c:forEach items="${leftMenu.subMenuList}" var="subMenu" varStatus="subMenuRow">
												<c:if test="${subMenu.subMenuId eq 4}">
													 <c:if test="${subMenu.applicable eq true}">
														
															<div class="col-md-2 checkbox1_field">
																<label>
																	<form:checkbox value="${leftMenu.leftMenuId}_${subMenu.subMenuId}" path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="false" class="${checkBox}" />
																</label>
															</div>
														</c:if>
														<c:if test="${subMenu.applicable eq false}">
														
															<div class="col-md-2 checkbox2_field">
																<label>	
																	<form:checkbox	value="${leftMenu.leftMenuId}_${subMenu.subMenuId}"	path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="true" />
																</label>
															</div>
														</c:if>
													</c:if>
												</c:forEach>						
												<!-- ************************ -->
												<c:forEach items="${leftMenu.subMenuList}" var="subMenu" varStatus="subMenuRow">
												<c:if test="${subMenu.subMenuId eq 3}">
														<c:if test="${subMenu.applicable eq true}">
														
															<div class="col-md-2 checkbox1_field">
																<label>
																	<form:checkbox value="${leftMenu.leftMenuId}_${subMenu.subMenuId}" path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="false" class="${checkBox}" />
																</label>
															</div>
														</c:if>
														<c:if test="${subMenu.applicable eq false}">
														
															<div class="col-md-2 checkbox2_field">
																<label>	
																	<form:checkbox	value="${leftMenu.leftMenuId}_${subMenu.subMenuId}"	path="applicationTypeDTOList[${appRow.index}].topMenuList[${topMenuRow.index}].leftMenuList[${leftMenuRow.index}].subMenuList[${subMenuRow.index}].selectedValue" disabled="true" />
																</label>
															</div>
														</c:if>
													</c:if>
												</c:forEach>
											</div>
										</c:forEach>
									</c:forEach>
								</div>
							</div>
							<div class="col-sm-12 button_alignment">
											<input type="button" class="btn-primary btn assign_button" value="Assign"
												onclick="submitAPrevlForm()" /> <input type="button"
												class="btn-default btn reset_button" name="Reset" value="Reset"
												onclick="loadPriv();" />
							</div>
						</c:forEach>
					</form>
				</div>
			</c:if>
				<!-- /.box-body -->
		</form:form>
	</div>
		<br />
		<!-- /.box -->
</div>

</body>
</html>