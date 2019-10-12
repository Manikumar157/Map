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
<title><spring:message code="LABEL_TITLE" text="EOT MOBILE" /></title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/sole-merchant-menu.js"></script>
<script>
	var Alertmsg = {
		"emptyIcon" : "<spring:message code="EMPTY_ICON_LIST" text="Please select any menu."/>",
		"emptyBankName" : "<spring:message code="EMPTY_BANK_NAME" text="Please select any bank."/>",
		"emptyTab" : "<spring:message code="EMPTY_TAB" text="Please select any tab."/>",
		"emptyMenu" : "<spring:message code="EMPTY_MENU" text="Please select any menu."/>",
		"emptyMenuIcon" : "<spring:message code="EMPTY_MENU_ICON" text="Please select icon."/>",
		"ERROR_8001" : "<spring:message code="ERROR_8001" text="Configuration for this bank is already exist. Do you want to edit"/>",
		"ERROR_8002" : "<spring:message code="ERROR_8002" text="Configuration for this bank and profile is already exist. Do you want to edit"/>"
	};
	/* var $selectedMenuIcon = [];
	var $selectedIcon; */

	var $selectedDetails = [];
	var $selectedTabId = null;
	var $tabMenuIconList = [];
	var $selectedMenu;
</script>

<style>
.padding-0 {
	padding: 0px !important;
}

.tabs-list span {
	margin-right: 15px;
}

.tabs-heading {
	text-decoration: underline;
}

.tabs-list label {
	font-weight: normal !important;
}

.box {
	height: auto !important;
	float: left;
	width: 100%;
	background: white !important;
}

.mobileSection {
	/* border: 4px solid black; */
	height: 400px;
	overflow: auto;
	border-radius: 22px;
	margin-top: 10px;
/* 	vineeth change */
	/* padding-top: 10px;
	padding-bottom: 10px;
	background-image: url(/EOT-Wallet-Core-Web/images/android_frame.png);
    background-position: center;
    background-size: 100% 100%;
    padding: 50px 17px; */
}

.imageBox img {
	width: 50px;
	height: 50px;
}

.icon {;
	height: 200px;
	width: 200px;
	overflow: auto;
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 10px;
	padding-right: 10px;
}

.mobileMenu {
	padding: 0px 5px;
	min-height: 100px;
}

.mobileMenu .menu-text {
	font-size: 12px;
	word-wrap: break-word;
}
.nav-tabs>li>a {
	color: black !important;
	border: 0px !important;
}
.nav-tabs>li>a:hover {
	color: black;
	border: 0px;
	background: #eaeaea !important;
}
.nav-tabs>li>a.active {
	background: white !important;
    border: 0px solid black !important;
    color: black !important;
}
/* // vineeth change */

.mobileView{
	padding-top: 10px;
	padding-bottom: 10px;
	background-image: url(/EOT-Wallet-Core-Web/images/android_frame1.png);
    background-position: center;
    background-size: 100% 100%;
    padding: 50px 17px;
    height:517px;
}
.switch {
      display: inline-block;
   width: 40px;
   height: 17px;
   position: absolute;
   top: 4px;
   right: 0px;
}

.switch input {display:none;}

.slider {
 position: absolute;
 cursor: pointer;
 top: 0;
 left: 0;
 right: 0;
 bottom: 0;
 background-color: #ccc;
 -webkit-transition: .4s;
 transition: .4s;
}

.slider:before {
   position: absolute;
   content: "";
   height: 10px;
   width: 10px;
   left: 2px;
   bottom: 4px;
   background-color: white;
   -webkit-transition: .4s;
   transition: .4s;
}

input:checked + .slider {
 background-color: #2196F3;
}

input:focus + .slider {
 box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
 -webkit-transform: translateX(26px);
 -ms-transform: translateX(26px);
 transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
 border-radius: 34px;
}

.slider.round:before {
 border-radius: 50%;
}

/* // change over */
</style>

</head>
<body>
	<div class="col-md-12">
	<ul class="nav nav-tabs">
  <li class="nav-item" >
    <a class="nav-link active" style="background:white" href="#">Menu Config</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=${appId }')">Theme Config</a>
  </li>  
</ul>
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_SOLE_MERCHANT_MENU_CONFIG"
							text="Merchant Menu Config" /></span>
				</h3>
			</div>
			<!-- /.box-header -->
			<div class="col-sm-8" style="margin-top: 10px;">
				<div class="col-md-7 col-md-offset-2"
					style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${success_message}" text="" />
				</div>
				<form:form name="customerMenuConfigForm" id="customerMenuConfigForm"
					action="" method="post" commandName="dynamicMenuConfDTO">
					<jsp:include page="csrf_token.jsp" />
					<div class="col-md-12 padding-0">
						<!-- form start -->
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANK" text="Bank" /><font color="red">*</font></label>
								<c:set var="bankStatus" value="false" />
								<c:if test="${dynamicMenuConfDTO.bankId ne null}">
									<form:hidden path="bankId" id="editBankId" />
									<c:set var="bankStatus" value="true" />
								</c:if>
								<form:select path="bankId" id="bankId" disabled="${bankStatus}"
									cssClass="dropdown_big chosen-select" multiple="false">
									<form:option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</form:option>appId
									<form:options items="${bankList}" itemLabel="bankName"
										itemValue="bankId" />
								</form:select>
								<font color="RED"><form:errors path="bankId"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANK_PROFILE" text="Profile" /></label>
								<div id="profile-list-holder">
									<c:set var="profileStatus" value="false" />
									<c:if test="${dynamicMenuConfDTO.profileId ne null}">
										<form:hidden path="profileId" id="editProfileId" />
										<c:set var="profileStatus" value="true" />
									</c:if>
									<form:select path="profileId" disabled="${bankStatus}"
										cssClass="dropdown_big chosen-select" multiple="false">
										<form:option value="">
											<spring:message code="LABEL_WUSER_SELECT"
												text="--Please Select--"></spring:message>
										</form:option>
										<form:options items="${bankProfileList}"
											itemLabel="profileName" itemValue="profileId" />
									</form:select>
								</div>
								<font color="RED"><form:errors path="profileId"></form:errors></font>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<label class="col-sm-12 tabs-heading"><spring:message
										code="LABEL_TABS" text="Tabs" /><font color="red">*</font></label>
								<div class="col-sm-12 tabs-list">
									<form:radiobuttons class="tab-list-radio" path="tabId"
										items="${tabList}" itemLabel="menuName" itemValue="menuId" />
								</div>
							</div>
						</div>

						<form:textarea style="display: none;" id="selected-menu-icon" path="selectedMenuIcon"></form:textarea>

						<form:input type="text" path="appId" value="${ appId }"
							style="display: none;"></form:input>
						<div class="row">
							<div class="col-sm-12">
								<div class="col-sm-12">
									<div class="panel panel-default">
										<div class="panel-heading">Menu List</div>
										<div class="panel-body" id="menu-list-holder">
											<c:if test="${menuList.size() > 0 }">
												<c:forEach items="${menuList}" var="ct" varStatus="status">
													<%-- <c:if test="${status.index%3 == 0}">
												<c:set var="counter" value="0" />
												<div class="col-md-12 col-xs-12 col-lg-12 col-sm-12 padding-0">
											</c:if> --%>
													<div class="col-md-6 col-xs-12 col-lg-6 col-sm-6">
														<c:if
															test="${dynamicMenuConfDTO.editMenuList.contains(ct[id])}">
															<c:forEach items="${dynamicMenuConfDTO.editMenuIconList}"
																var="menuIcon">
																<c:if test="${ menuIcon.get(ct[id]) != null }">
																	<c:set var="iconId" value="${menuIcon.get(imageId)}" />
																	<img
																		onclick="javascript:getIconList('${ct[functionalCode]}', '${ct[id]}');"
																		menuId="${ct[id]}" class="edit-page-icons"
																		width="20px" height="20px"
																		functionalCode="${ct[functionalCode]}"
																		src="data:image/png;base64,${menuIcon.get(ct[id])}" />
																</c:if>
															</c:forEach>
															<!-- 	vineeth change	 -->
															<%-- <input type="checkbox" checked class="${ entity }"
																id="${ entity }${ct[id]}" value="${ct[id]}"
																functionalCode="${ct[functionalCode]}"
																selectedicon="${iconId}" /> --%>
																<!-- 	vineeth change	end -->
															<label for="${ entity }${ct[id]}">${ct[value1]}</label>
														</c:if>
														<c:if
															test="${!dynamicMenuConfDTO.editMenuList.contains(ct[id])}">
														<!-- 	vineeth change	 -->
															<%-- <input type="checkbox" class="${ entity }"
																id="${ entity }${ct[id]}" value="${ct[id]}"
																functionalCode="${ct[functionalCode]}" /> --%>
															<label for="${ entity }${ct[id]}">${ct[value1]}</label>
												
												
														<label class="switch">
															<input type="checkbox" class="${ entity }"
																id="${ entity }${ct[id]}" value="${ct[id]}"
																functionalCode="${ct[functionalCode]}" ><span class="slider round"></span>
															</label>
												<!-- 	vineeth change over	 -->
													
														</c:if>
													</div>
													<%-- <c:if test="${counter == 2}">
												</div>
											</c:if>
											<c:set var="counter" value="${counter+1}" /> --%>
												</c:forEach>
											</c:if>

											<c:if test="${menuList.size() == 0 }">
										Please select any tab.
									</c:if>

										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12">
								<div class="col-sm-12">
									<div class="panel panel-default">
										<div class="panel-heading">
											Icon List <span id="icons-for"></span>
										</div>
										<div class="panel-body" id="menu-icon-list-holder">
											Please select any tab.</div>
									</div>
								</div>
							</div>
						</div>

						<!-- End radio button -->
					</div>

					<div class="col-sm-12 col-md-12">
					   <!-- Abu kalam Azad Date:03/08/2018 purpose:bug 5819  start-->
					<input type="button" class="pull-right btn btn-primary" style="margin-left: 20px"
							value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
							onclick="cancelMenuConfig('customerMenuConfigForm', 'dynamicMenuConfigForm.htm', '${ appId }')" />
					<!-- End -->
					
						<c:if test="${dynamicMenuConfDTO.bankId ne null }">
							<!-- <a type="button" href="javascript:void(0);"
								class="pull-right btn btn-primary"
								onclick="submitMobileMenuConfig('customerMenuConfigForm', 'editDynamicMenuConfiguration.htm')"
								name="submit">Submit</a> -->
								
								<input type="button" class="pull-right btn btn-primary" style="margin-left: 20px"
							value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
							onclick="submitMobileMenuConfig('customerMenuConfigForm', 'editDynamicMenuConfiguration.htm')" />
						</c:if>
						
						<c:if
							test="${dynamicMenuConfDTO.bankId eq null || dynamicMenuConfDTO.bankId eq undefined }">
							<!-- <a type="button" href="javascript:void(0);"
								class="pull-right btn btn-primary"
								onclick="submitMobileMenuConfig('customerMenuConfigForm', 'saveDynamicMenuConfiguration.htm')"
								name="submit">Submit</a> -->
								
								<input type="button" class="pull-right btn btn-primary" style="margin-left: 20px"
							value="<spring:message code="LABEL_SUBMIT" text="Submit"/>"
							onclick="submitMobileMenuConfig('customerMenuConfigForm', 'saveDynamicMenuConfiguration.htm')" />
						</c:if>
						
						
					</div>

				</form:form>
			</div>

			<div class="col-sm-4">
			<div class="mobileView">
				<div class="mobileSection col-lg-12"></div>
				</div>
			</div>

		</div>
	</div>






</body>
</html>
