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
		<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/js/themeConfig.js"></script> --%>
		<%-- <script type="text/javascript"	src="<%=request.getContextPath()%>/js/jscolor.js"></script> --%>
		<link href="<%=request.getContextPath()%>/css/themeConfig.css" rel="stylesheet" type="text/css" />
		<%-- <link href="<%=request.getContextPath()%>/css/viewOnlyTheme.css" rel="stylesheet" type="text/css" /> --%>
	
		<style>
		 input.toolBarColorOverride{
			background-color: #${themeConfDTO.toolBarColor};
			color: #fff;
		}
		input.screenBackgroundColorOverride{
			background-color: #${themeConfDTO.screenBackgroundColor};
			color: #fff;
		}
		input.tabColorSelectedOverride{
			background-color: #${themeConfDTO.tabColorSelected};
			color: #fff;
		}input.tabColorUnselectedOverride{
			background-color: #${themeConfDTO.tabColorUnselected};
			color: #fff;
		}
		input.gridColorSelectedOverride{
			background-color: #${themeConfDTO.gridColorSelected};
			color: #fff;
		}
		input.gridColorUnselectedOverride{
			background-color: #${themeConfDTO.gridColorUnselected};
			color: #fff;
		}
		input.listHeaderColorOverride{
			background-color: #${themeConfDTO.listHeaderColor};
			color: #fff;
		} 
		input.bankThemeOverride{
			background-color: #${themeConfDTO.bankTheme};
			color: #fff;
		}
		input.btnColorNameOverride{
			background-color: #${themeConfDTO.btnColorName};
			color: #fff;
		}
		input.menuColorNameOverride{
			background-color: #${themeConfDTO.menuColorName};
			color: #fff;
		} 
		input.subMenuColorOverride{
			background-color: #${themeConfDTO.subMenuColorName};
			color: #fff;
		}
		input.textColorOverride{
			background-color: #${themeConfDTO.textColor};
			color: #fff;
		}
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
	/* AUTHOR: Murari DATE: 05 JULY 2018 - Bug Fix: text color black*/
	input.textColor{
		color:black;
	}
	img#previousLogo {
 width: 108px;
 height: 58px;
}
		
	</style>
	</head>
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
				<form:form name="viewThemeConfigForm" action="" method="post" commandName="themeConfDTO">
					<jsp:include page="csrf_token.jsp" />
					<div class="col-md-12 padding-0">
						<!-- form start -->
						<div class="row">
						
							<div class="col-md-3 col-md-offset-10">
								<a href="javascript:submitForm('menuForm','themeConfigForm.htm?appType=${themeConfDTO.appType}')"><strong><spring:message
									code="LABEL_BACK" text="Back to Theme Config"></spring:message></strong></a>
							</div>
							
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
											<div class="row">
											
											
												<div class="col-sm-12">
													<label class="col-sm-6">
														<spring:message code="LABEL_TOOL_BAR_COLOR" text="Tool Bar Color"></spring:message>
													</label>
													 <form:input path="toolBarColor" class="toolBarColorOverride textColor" readonly="true" value="${themeConfDTO.toolBarColor}" maxlength="6"/> 
												</div>
												
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_SCREEN_BACKGROUND_COLOR" text="Screen Background Color"></spring:message>
													</label>
													 <form:input class="screenBackgroundColorOverride textColor" path="screenBackgroundColor"  readonly="true" value="${themeConfDTO.screenBackgroundColor}" maxlength="6" />
												</div>
											</div>
											
											
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_TAB_COLOR_SELECTED" text="Tab Color Selected"></spring:message>
													</label>
													<form:input path="tabColorSelected" class="tabColorSelectedOverride textColor" readonly="true" value="${themeConfDTO.tabColorSelected}" maxlength="6" /> 
												</div>
												
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_TAB_COLOR_UNSELECTED" text="Tab Color Unselected"></spring:message>
													</label>
													<form:input path="tabColorUnselected" class="tabColorUnselectedOverride textColor" readonly="true" value="${themeConfDTO.tabColorUnselected}" maxlength="6" /> 
												</div>
											</div>
											
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_GRID_COLOR_SELECTED" text="Grid Color Selected"></spring:message>
													</label>
													<form:input path="gridColorSelected" class="gridColorSelectedOverride textColor" readonly="true" value="${themeConfDTO.gridColorSelected}" maxlength="6" /> 
												</div>
												
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_GRID_COLOR_UNSELECTED" text="Grid Color Unselected"></spring:message>
													</label>
													<form:input path="gridColorUnselected" class="gridColorUnselectedOverride textColor" readonly="true" value="${themeConfDTO.gridColorUnselected}" maxlength="6" /> 
												</div>
											</div>
										
											<div class="row">
												<div class="col-sm-12">
													<label class="col-sm-6"><spring:message
															code="LABEL_LIST_HEADER_COLOR" text="List Header Color"></spring:message>
												    </label>
													<form:input path="listHeaderColor" class="listHeaderColorOverride textColor" readonly="true" value="${themeConfDTO.listHeaderColor}" maxlength="6" /> 
												</div>

													<%-- <div class="col-sm-6">
														<label class="col-sm-6"><spring:message
																code="LABEL_TEXT_COLOR" text="Text Color" />
														</label>
															<c:if test="${themeConfDTO.textColor eq 000000}">
																<c:set var="textColor" value="Black"></c:set>
															</c:if>
															<c:if test="${themeConfDTO.textColor eq FFFFFF}">
																<c:set var="textColor" value="White"></c:set>
															</c:if>
															<form:input path="textColor" class="textColorOverride" readonly="true" value="${themeConfDTO.textColor}" maxlength="6" />
															<c:out value="${textColor}" />	
													</div> --%>
												</div>
										</div>
									</div>
								</div>
								<div class="col-sm-6 col-lg-4">
									<div class="mobileColorSection" <%=request.getContextPath()%>/images/android_frame.png>
										<div class="mobileSection">
										<!-- AUTHOR: Pavan DATE: 28 JUN 2018 - Bug Fix: Selected theme colour display in view mode.  -->
										
											<div style="background: #${themeConfDTO.toolBarColor}" class="toolbarSection col-lg-12 mobilecolorElem textColorElem" id="toolbarcolorElem">
												<p style="color: #${themeConfDTO.textColor}">Toolbar Section</p>
											</div>
											<div style="background: #${themeConfDTO.tabColorUnselected}" class="tabSection col-lg-12 mobilecolorElem" id="tabcolorUnSelectElem">
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
		</div>
				</form:form>
			</div>
		</div>	
		</div>
	</div>
</body>
</html>
