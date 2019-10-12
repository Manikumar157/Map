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
<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/editWebColorConfig.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jscolor.js"></script>
<link href="<%=request.getContextPath()%>/css/themeConfig.css"
	rel="stylesheet" type="text/css" />
</head>
<style>
.mobileColorSection {
	/* border: 2px solid black;
	    border-radius: 10px; */
	height: 410px;
	background-image:
		url("<%=request.getContextPath()%>/images/android_frame.png");
	background-position: center;
	background-size: 100% 100%;
}

.mobileSection {
	height: 318px;
	width: 267px;
	margin: 0 auto;
	position: relative;
	top: 46px;
	border: 1px solid #f6f5f5;
}

.toolbarSection {
	height: 61px;
	border: 1px solid #f6f5f5;
}

.tabSection {
	height: 90px;
	border: 1px solid #f6f5f5;
	padding: 0px;
	margin-top: 2px;
}

.tabSection div {
	height: 88px;
	border: 1px solid #f6f5f5;
}

.gridSection {
	margin-top: 2px;
	padding: 0px;
	height: 60px;
	border: 1px solid #f6f5f5;
}

.gridSection div {
	height: 60px;
	border: 1px solid #f6f5f5;
}

.backgroundSection {
	margin-top: 4px;
	height: 97px;
	border: 1px solid #f6f5f5;
}

.webBackground {
    border: 1px solid #f6f5f5;
    height: 327px;
}
.webSection {
    border: 1px solid #f6f5f5;
    height: 269px;
    margin-top: 27px !important;
    margin: 0 auto;
    width: 245px;
}
.leftSidebar {
    height: 177px;
    border: 1px solid #f6f5f5;
}
.rightContainer {
    height: 177px;
    border: 1px solid #f6f5f5;
}
.header {
    border: 1px solid #f6f5f5;
    height: 90px;
}
img#headerlogo {
    width: 108px;
    height: 58px;
}
ul.list-unstyled.components li {
    background: #3f8634;
    padding: 6px;
    border: 1px solid #f3d606;;
}
.leftSidebar {
    height: 177px;
    border: 1px solid #f6f5f5;
    background: #316b2b;
    overflow: auto;
}
ul.list-unstyled.collapse.in.submenu li {
    background-color: #326b2d;
    z-index: 9999999;
    border: none;
}
img#previousLogo {
 width: 108px;
 height: 58px;
}

/* 	.mobilecolorElem {
	    height: 60px;
	    border: 1px solid black;
	} */
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
<body>
	<div class="col-md-12">
		<div class="box" style="height: 430px;">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_WEB_COLOR_CONFIGURATION"
							text="Web Color Configuration" /></span>
				</h3>
			</div>
			<div class="col-md-6 col-md-offset-5">
				<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>
			</div>
			<!-- /.box-header -->
			<div class="box-body">
				<div class="col-sm-12" style="margin-top: 10px;">
					<form:form name="editWebColorConfigForm"
						id="editWebColorConfigForm" action="" method="post"
						commandName="themeConfDTO" enctype="multipart/form-data"
						autocomplete="off">
						<jsp:include page="csrf_token.jsp" />
						<div class="col-md-12 padding-0">
							<!-- form start -->
							<div class="row">

								<div class="col-md-3 col-md-offset-10">
									<a
										href="javascript:submitForm('menuForm','webColorConfigForm.htm')"><strong><spring:message
												code="LABEL_BACK" text="Back to Web Color"></spring:message></strong></a>
								</div>

								<div class="col-sm-6">
									<label class="col-sm-4"><spring:message
											code="LABEL_BANK" text="Bank" /></label>
									<div class="col-sm-5" style="margin-top: 4px;">
										<c:out value="${themeConfDTO.bankName}" />
										<form:hidden path="bankId"/>
									</div>
								</div>

							</div>
				
							<div class="row">
								<div class="col-sm-12 col-lg-8">
									<div class="panel panel-default">
										<div class="panel-heading">
											<spring:message code="LABEL_WEB_COLOR_CONFIGURATION"
												text="Mobile Color Configuration" />
										</div>
										<div class="panel-body" id="menu-list-holder">
											<div class="row">
												<div class="col-sm-6">
													<label class="col-sm-5"><spring:message
															code="LABEL_BANK_THEME" text="Bank Theme" /><font
														color="red">*</font></label>
													<form:input path="bankTheme" class="jscolor"
														value="${themeConfDTO.bankTheme}" maxlength="6" />
													<FONT color="red"> <form:errors path="bankTheme" /></FONT>
												</div>
												<c:if test="${themeConfDTO.logo ne null }">
													<div class="col-sm-6">
														<label class="col-sm-5">Previous Logo:</label> <span><img
															id="previousLogo" class="img_resize_for_entity"
															src="data:image/png;base64,<c:out value="${themeConfDTO.logo }"></c:out>" /></span>
													</div>
												</c:if>
											</div>

											<div class="row">
												<div class="col-sm-6">
													<label class="col-sm-5"><spring:message
															code="LABEL_BRANDING_BTN_COLOR" text="Button Color" /><font
														color="red">*</font></label>
													<form:input path="btnColorName" class="jscolor"
														value="${themeConfDTO.btnColorName}" maxlength="6" />
													<FONT color="red"> <form:errors path="btnColorName" /></FONT>
												</div>

												<div class="col-sm-6">
													<label class="col-sm-5"><spring:message
															code="LABEL_BANK_LOGO" text="Logo" /><font color="red">*</font></label>
													<input type="file" name="logoImg" id="logoImg"
														value="upload" />
												</div>
											</div>

											<div class="row">
												<div class="col-sm-6">
													<label class="col-sm-5"><spring:message
															code="LABEL_BRANDING_SUB_MENU_COLOR"
															text="Sub Menu Color" /><font color="red">*</font></label>
													<form:input path="subMenuColorName" class="jscolor"
														value="${themeConfDTO.subMenuColorName}" maxlength="6" />
													<FONT color="red"> <form:errors
															path="subMenuColorName" /></FONT>
												</div>

												<div class="col-sm-6">
													<label class="col-sm-5"><spring:message
															code="LABEL_BRANDING_MENU_COLOR" text="Menu Color" /><font
														color="red">*</font></label>
													<form:input path="menuColorName" class="jscolor"
														value="${themeConfDTO.menuColorName}" maxlength="6" />
													<FONT color="red"> <form:errors path="menuColorName" /></FONT>
												</div>
											</div>

										</div>
									</div>
								</div>
								<div class="col-sm-12 col-lg-4">
							<div style="background: #${themeConfDTO.bankTheme}" class="col-lg-12 webBackground" id="bankThemeElem">
								<div class="webSection">
									<div class="header col-lg-12">
										<c:if test="${themeConfDTO.logo eq null}">
											<img src="/EOT-Wallet-Core-Web/images/Easybank logo.png" id="headerlogo">
										</c:if>
										<c:if test="${themeConfDTO.logo ne null}">
										<img id="previousLogo" class="img_resize_for_entity"
															src="data:image/png;base64,<c:out value="${themeConfDTO.logo }"></c:out>" />
										</c:if>
									</div>
									<div class="webContainer">
										<div class="leftSidebar col-lg-5">
											<p>Navigation</p>
											 <ul  class="list-unstyled components">
									            
									            <li style="background: #${themeConfDTO.menuColorName}" class="listMenu"><!-- Link with dropdown items -->
									                <a href="#homeSubmenu1" data-toggle="collapse" aria-expanded="false">Home</a>
									                <ul class="collapse list-unstyled submenu" id="homeSubmenu1">
									                    <li><a href="#">Tab 1</a></li>
									                    <li><a href="#">Tab 2</a></li>
									                    <li><a href="#">TAb 3</a></li>
									                </ul>
												</li>
									
									            <li style="background: #${themeConfDTO.menuColorName}" class="listMenu"><!-- Link with dropdown items -->
									                <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false">Pages</a>
									                <ul class="collapse list-unstyled submenu" id="homeSubmenu">
									                    <li><a href="#">Page 1</a></li>
									                    <li><a href="#">Page 2</a></li>
									                    <li><a href="#">Page 3</a></li>
									                </ul>
												</li>
												
												<li style="background: #${themeConfDTO.menuColorName}" class="listMenu"><!-- Link with dropdown items -->
									                <a href="#homeSubmenu2" data-toggle="collapse" aria-expanded="false">Slides</a>
									                <ul class="collapse list-unstyled submenu" id="homeSubmenu2">
									                    <li><a href="#">Slides 1</a></li>
									                    <li><a href="#">Slides 2</a></li>
									                    <li><a href="#">Slides 3</a></li>
									                </ul>
												</li>
									            
									        </ul>
										</div>
										
									</div>
									
								</div>
							</div>
						</div>
							</div>

							<!-- Web Color Configuration end -->

							<!--  Save Button start -->
							<div class="col-md-3 col-md-offset-10"
								style="margin-left: 43.333333%;">

								<div class="space"></div>

								<a onclick="updateThemeConfig()" class="btn-primary btn"><spring:message
										code="LABEL_UPDATE" text="Update" /></a>

							</div>
							<!--  Save Button end -->
						</div>

					</form:form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
