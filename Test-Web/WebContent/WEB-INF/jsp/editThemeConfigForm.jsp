<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>
	<head>
		<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/editThemeConfig.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jscolor.js"></script>
		<link href="<%=request.getContextPath()%>/css/themeConfig.css" rel="stylesheet" type="text/css" />
	</head>
	<style>
	.mobileColorSection {
	    /* border: 2px solid black;
	    border-radius: 10px; */
	    height: 410px;
	    background-image: url("<%=request.getContextPath()%>/images/android_frame.png");
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
	.tabSection{
		height: 90px;
	    border: 1px solid #f6f5f5;
	    padding: 0px;
	    margin-top: 2px;
	}
	.tabSection div{
		 height: 88px;
   		 border: 1px solid #f6f5f5;
	}
	   
	.gridSection{
		 margin-top: 2px;
    	 padding: 0px;
         height: 60px;
         border: 1px solid #f6f5f5;
	}
	.gridSection div{
         height: 60px;
         border: 1px solid #f6f5f5;
	}
	.backgroundSection{
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
img#previousLogo {
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
					<span><spring:message code="LABEL_THEME_CONFIG" text="Theme Config" /></span>
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
				<form:form name="themeConfigForm" id="themeConfigForm" action="" method="post" commandName="themeConfDTO" enctype="multipart/form-data" autocomplete="off">
					<jsp:include page="csrf_token.jsp" />
					<div class="col-md-12 padding-0">
						<!-- form start -->
						<div class="row">
							
							<%-- <div class="col-md-3 col-md-offset-10">
								<a href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=${themeConfDTO.appType}')"><strong><spring:message
									code="LABEL_BACK" text="Back to Theme Config"></spring:message></strong></a>
							</div>
						 --%>
							 <div class="col-sm-6">
								<label class="col-sm-4"><spring:message	code="LABEL_BANK" text="Bank" /></label>
								<div class="col-sm-5" style="margin-top:4px;"><c:out value="${themeConfDTO.bankName}" /></div>
							</div> 
							
						</div>
						<!-- Mobile Color Configuration starts -->
						<div class="row">
							<div class="col-sm-6 col-lg-8">
									<div class="panel panel-default">
									  <div class="panel-heading">
									  <spring:message
										code="LABEL_MOBILE_COLOR_CONFIGURATION" text="Mobile Color Configuration" />
										</div>
									  <div class="panel-body" id="menu-list-holder">
						 
						 		 		<form:hidden path="appType"></form:hidden>
						 			 		<form:hidden path="bankId"></form:hidden>
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6">
														<spring:message code="LABEL_TOOL_BAR_COLOR" text="Tool Bar Color"></spring:message>
														<font color="red">*</font>
													</label>
													<!-- <input class="jscolor" value="ab2567"> -->
													 <form:input class="jscolor" path="toolBarColor" value="${themeConfDTO.toolBarColor}"  maxlength="6" actionElem = "#toolbarcolorElem"/> 
													<FONT color="red"> <form:errors path="toolBarColor" /></FONT>
												</div>
												
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_SCREEN_BACKGROUND_COLOR" text="Screen Background Color"></spring:message><font
														color="red">*</font></label>
													
													 <form:input class="jscolor" path="screenBackgroundColor" value="${themeConfDTO.screenBackgroundColor}" maxlength="6" actionElem = "#backgroundcolorElem"/>
													<FONT color="red"> <form:errors path="screenBackgroundColor" /></FONT>
												</div>
											</div>
											
											
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_TAB_COLOR_SELECTED" text="Tab Color Selected"></spring:message><font
														color="red">*</font></label>
													<form:input path="tabColorSelected" class="jscolor" value="${themeConfDTO.tabColorSelected}" maxlength="6" actionElem = "#tabcolorSelectElem"/> 
													<FONT color="red"> <form:errors path="tabColorSelected" /></FONT>
												</div>
												
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_TAB_COLOR_UNSELECTED" text="Tab Color Unselected"></spring:message><font
														color="red">*</font></label>
													<form:input path="tabColorUnselected" class="jscolor" value="${themeConfDTO.tabColorUnselected}" maxlength="6" actionElem = "#tabcolorUnSelectElem"/> 
													<FONT color="red"> <form:errors path="tabColorUnselected" /></FONT>
												</div>
											</div>
											
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_GRID_COLOR_SELECTED" text="Grid Color Selected"></spring:message><font
														color="red">*</font></label>
													<form:input path="gridColorSelected" class="jscolor" value="${themeConfDTO.gridColorSelected}" maxlength="6" actionElem = "#gridcolorSelectElem"/> 
													<FONT color="red"> <form:errors path="gridColorSelected" /></FONT>
												</div>
												
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_GRID_COLOR_UNSELECTED" text="Grid Color Unselected"></spring:message><font
														color="red">*</font></label>
													<form:input path="gridColorUnselected" class="jscolor" value="${themeConfDTO.gridColorUnselected}" maxlength="6" actionElem = "#gridcolorUnSelectElem"/> 
													<FONT color="red"> <form:errors path="gridColorUnselected" /></FONT>
												</div>
											</div>
										
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_LIST_HEADER_COLOR" text="List Header Color"></spring:message><font
														color="red">*</font></label>
													<form:input path="listHeaderColor" class="jscolor" value="${themeConfDTO.listHeaderColor}" maxlength="6" actionElem = "#headercolorElem"/> 
													<FONT color="red"> <form:errors path="listHeaderColor" /></FONT>
												</div>

												<div class="col-sm-12">
														<label class="col-sm-6"><spring:message
																code="LABEL_TEXT_COLOR" text="Text Color" /><font color="red">*</font></label>
														<form:radiobutton path="textColor" value="000000" label="Black" actionElem = ".textColorElem"></form:radiobutton>
														&nbsp; &nbsp;
														<form:radiobutton path="textColor" value="FFFFFF" label="White" actionElem = ".textColorElem"></form:radiobutton>
													</div>
												</div>
										
										</div>
									</div>
								</div>
								<div class="col-sm-6 col-lg-4">
									<div class="mobileColorSection" <%=request.getContextPath()%>/images/android_frame.png>
										<div class="mobileSection">
										<!-- AUTHOR: Pavan DATE: 28 JUN 2018 - Bug Fix: Selected theme colour display in edit mode.  -->
											<div style="background: #${themeConfDTO.toolBarColor}" class="toolbarSection col-lg-12 mobilecolorElem textColorElem" id="toolbarcolorElem">
												<p style="color: #${themeConfDTO.textColor}">Toolbar Section</p>
											</div>
											<div style="background: #${themeConfDTO.tabColorUnselected}" class="tabSection col-lg-12 mobilecolorElem" id="tabcolorUnSelectElem" >
												<div style="background: #${themeConfDTO.tabColorSelected}" class="col-lg-4 mobilecolorElem textColorElem" id="tabcolorSelectElem" >
													<p style="color: #${themeConfDTO.textColor}">Tab Section</p>
												</div>
												<div class="col-lg-4 mobilecolorElem textColorElem">
													<p style="color: #${themeConfDTO.textColor}">Tab Section</p>
												</div>
												<div class="col-lg-4 mobilecolorElem textColorElem">
													<p style="color: #${themeConfDTO.textColor}">Tab Section</p>
												</div>
											</div>
											<div class="gridSection col-lg-12 mobilecolorElem">
												<div style="background: #${themeConfDTO.gridColorSelected}" class="col-lg-6 mobilecolorElem textColorElem" id="gridcolorSelectElem" >
													<p style="color: #${themeConfDTO.textColor}">Grid Section</p>
												</div>
												<div style="background: #${themeConfDTO.gridColorUnselected}" class="col-lg-6 mobilecolorElem textColorElem" id="gridcolorUnSelectElem">
													<p style="color: #${themeConfDTO.textColor}">Grid Section</p>										
												</div>
											</div>
											<div style="background: #${themeConfDTO.screenBackgroundColor}" class="backgroundSection col-lg-12 mobilecolorElem textColorElem" id="backgroundcolorElem">
												<p style="color: #${themeConfDTO.textColor}">Background Section</p>
											</div>
										</div>
									</div>
								</div>
						</div>
						
						<!-- Mobile Color Configuration end -->
						
						<!-- Web Color Configuration start -->
						
						
						<!-- Web Color Configuration end -->
					
					<!--  Save Button start -->
					<div class="col-md-3 col-md-offset-10" style="margin-left: 43.333333%;">
			
						<div class="space"></div>
							
							<input type="button" class="btn-primary btn"
							value="<spring:message code="LABEL_UPDATE" text="Update"/>"
							onclick="updateThemeConfig()" />
							
							<div class="space"></div>
							
							<input type="button" class="btn-primary btn"
							value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
							onclick="javascript:submitForm('menuForm','themeConfigForm.htm?appType=${themeConfDTO.appType}')" />
							
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
