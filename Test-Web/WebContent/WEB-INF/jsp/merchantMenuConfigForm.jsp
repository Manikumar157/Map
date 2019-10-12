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
	padding-top: 10px;
	padding-bottom: 10px;
	background-image: url(/EOT-Wallet-Core-Web/images/android_frame.png);
    background-position: center;
    background-size: 100% 100%;
    padding: 50px 17px;
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
</style>

</head>
<body>
	<div class="col-md-12">
<ul class="nav nav-tabs">
  <li class="nav-item" >
    <a class="nav-link active" style="background:white" href="#">Menu Config</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=${appId}')">Theme Config</a>
  </li>  
</ul>
	<div class="box" style="height: 430px;">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_MERCHANT_MENU_CONFIG"
							text="Merchant Menu Config" /></span>
				</h3>
			</div>
			<!-- /.box-header -->
			<div class="col-md-6 col-md-offset-5">
				<div style="color: #ba0101; font-weight: bold; font-size: 12px; margin-left: 60px">
					<spring:message code="${success_message}" text="" />
				</div>
			</div>
			
			<div class="box-body">
			<div class="col-sm-12" style="margin-top: 10px;">
			
				<form:form name="customerMenuConfigForm" id="customerMenuConfigForm"
					action="" method="post" commandName="dynamicMenuConfDTO">
					<jsp:include page="csrf_token.jsp" />
					<!-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting -->
					<form:hidden path="bankId" />
					<form:hidden path="profileId" />
					<form:hidden path="appId" />
					<!--  @End-->
					<div class="col-md-3 col-md-offset-10">
						<span>
							<a href="javascript:submitForm('customerMenuConfigForm','addConfiguration.htm?appType=${ appId }')" onclick="removeMsg()">
							<strong><spring:message	code="LABEL_CONFIGURE_MENU" text="Configure Menu"></spring:message></strong>
							</a>
						</span>
					</div>
					
					<div class="col-md-12">
						<div class="box">
							<!-- /.box-header -->
						<div class="box">
							<div class="box-body table-responsive">
								<table id="example1"
									class="table table-bordered table-striped">
									<thead>
										<tr>
											<th>Bank Name</th>
											<th>Profile Name</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${menuConfigList}" var="menuConfig"
											varStatus="status">
											<tr>
												<td><c:out value="${menuConfig.bankName}" /></td>
												<td><c:out value="${menuConfig.profileName}" /></td>
												<td style="text-align: center;"><span> 
												<!-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting -->
												<%-- <a href="javascript:void(0);"
														onclick="submitForm('customerMenuConfigForm', 'loadConfigurationBybank.htm?bankId=${menuConfig.bankId}&profileId=${menuConfig.profileId}&appId=${ appId }')">Edit</a> --%>
														<a
														href="javascript:void(0);"
														onclick="merchantDetails('${menuConfig.bankId}', '${menuConfig.profileId}', '${appId}')">Edit</a>
														<!--  @End-->
												</span> <span style="margin-left: 10px; margin-right: 10px;">|</span>
													<span> <a href="javascript:void(0);"
														onclick="showBankConfigurationView('${menuConfig.bankId}', '${menuConfig.profileId}', '${menuConfig.bankName}')">View</a>
												</span></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<!-- Modal -->
								<div class="modal fade" id="myModal" role="dialog">
									<div class="modal-dialog" style="width: 30%;">
										<div class="modal-content col-lg-12 padding-0">
											<div class="modal-header col-lg-12">
												<button type="button" class="close" data-dismiss="modal">&times;</button>
												<div class="modal-title" id="modal-heading"></div>
											</div>
											<div class="modal-body col-lg-12">
										        <div id="menu-preview"></div>
										     </div>
											<div class="modal-footer col-lg-12">
												<button type="button" class="btn btn-default"
													data-dismiss="modal">Close</button>
											</div>
										</div>
									</div>
								</div>
							</div>
					</div>
						</div>
					</div>
				</form:form>
			</div>

		</div>
	</div>
</div>





</body>
</html>
