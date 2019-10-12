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
		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/webColorConfig.js"></script>
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
					<span><spring:message code="LABEL_WEB_COLOR_CONFIGURATION" text="Web Color Configuration" /></span>
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
				<form:form name="webColorConfigForm" id="webColorConfigForm" action="" method="post" commandName="themeConfDTO" enctype="multipart/form-data" autocomplete="off">
					<jsp:include page="csrf_token.jsp" />
					<!-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting -->
					<form:hidden path="bankId" />
					<!--  @End-->
					<div class="col-md-3 col-md-offset-10">
						<span>
							<a href="javascript:submitForm('webColorConfigForm','addWebColorConfigForm.htm')" onclick="removeMsg()">
							<strong><spring:message	code="LABEL_CONFIGURE_WEB_COLOR" text="Configure Web Color"></spring:message></strong>
							</a>
						</span>
					</div>
					
					<!-- <div class="col-sm-6 col-sm-offset-9"></div> -->
					<!-- Table start -->
		
		<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_BANK_NAME" text="Bank Name" /></th>
								<th><spring:message code="LABEL_ACTION" text="Action" /></th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach items="${page.results}" var="mobiletheamcolorconfig">
								<tr>
									<td><c:out value="${mobiletheamcolorconfig.bank.bankName}" /></td>
									
									<td>
									<!-- //@start Murari  Date:30/07/2018 purpose:cross site Scripting -->
										<%-- <a href="javascript:submitForm('webColorConfigForm',
										'viewWebColorConfigForm.htm?bankID=<c:out value="${mobiletheamcolorconfig.bank.bankId}"/>')">
											<spring:message	code="LABEL_VIEW" text="View" />
										</a> --%>
										<a href="javascript:webColorView('<c:out value="${mobiletheamcolorconfig.bank.bankId}"/>')">
											<spring:message	code="LABEL_VIEW" text="View" />
										</a>
									<!-- 	vineeth, 03-07-2018, added pipe in between view and edit links -->
														<!-- &nbsp; &nbsp; -->
											<span style="margin-left: 10px; margin-right: 10px;">|</span>
											<%-- <a href="javascript:submitForm('webColorConfigForm',
										'editWebColorConfigForm.htm?bankID=<c:out value="${mobiletheamcolorconfig.bank.bankId}"/>')">
											<spring:message	code="LABEL_EDIT" text="Edit" />
										</a> --%>
										 <a href="javascript:webColorEdit('<c:out value="${mobiletheamcolorconfig.bank.bankId}"/>')">
											<spring:message	code="LABEL_EDIT" text="Edit" />
										</a> 
										<!--  @End-->
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			
		<!-- Table end -->
				</form:form>
			</div>
		</div>	
		</div>
	</div>
</body>
</html>
