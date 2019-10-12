<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>

<script type="text/javascript">
		function checkValidation()
		{
			if(document.otpForm.otp.value=="")
			{
				alert("<spring:message code="VALID_OTP" text="Please enter One Time Password"/>");
				return false;
			}
			
			document.otpForm.action ="verifyOTPForm.htm";
			document.otpForm.submit();
				
	 	}
		
		function cancel()
		{
			
			document.otpForm.action ="login.jsp";
			document.otpForm.submit();
				
	 	}
	
	</script>
<link href="css/style.css" rel="stylesheet" type="text/css" />

<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="css/bootstrap.css" />
<!-- Bootstrap theme -->
<link rel="stylesheet" href="css/theme.css" />
<link rel="stylesheet" href="css/style_v.1.0.css" />
<link rel="stylesheet" href="css/responsive.css" />
<style>
img.img_resize1 {
	width: 170px;
	height: 40px;
	margin-top: -18px;
	margin-left: 25px;
}
.form-group {
    margin-bottom: 15px;
    margin-top: 15px;
}
img.img_resize {
	width: 60px !important;
	height: 60px !important;
	margin-top: -27px !important;
}

#paper-bg {
	margin-left: 0px !important;
}
/* img.img_resize {
    width: 352px;
    height: 69px;
    margin-top: -19px;
} */
.form_login_box {
	margin-top: 10px;
}

.label_text_color {
	color: #fff;
	margin-top: 6px;
}

.africa_graph {
	margin-top: 80px;
}

#skin-select {
	position: absolute;
	top: 125px;
	width: 225px;
	left: 0;
	z-index: 1000;
	background: #5cb85c;
}

input.btn.btn.btn-primary.pull-right.login_button_color {
	background-color: #3e3c38;
	border-color: #3e3c38;
	margin-right: 17px;
}
/* img.img_resize {
    width: 176px;
    height: 40px!important;
     margin-top: -12px!important;
} */
.dropdown_size {
	margin-top: 9px;
	margin-bottom: -9px;
	color: #000;
}

#paper-bg {
	/* background: url(../img/linedpaper.gif) repeat; */
	/* background: url(../img/linedpaper.gif) repeat; */
	background: #ffffff;
	min-height: 1100px;
	margin-left: 200px;
	/* padding: 36px 25px 60px 25px; */
	width: 100%;
}

.paper-wrap.bevel.tlbr {
	opacity: 0.6;
}

a.reset_password {
	margin-left: 30px;
}
.btn.btn-primary {
    background-color: #3c8dbc;
    border-color: #367fa9;
    color: #fff !important;
}
.thumbnail {
    display: block;
    padding: 4px;
    margin-bottom: 20px;
    line-height: 1.42857143;
    background-color: #fff;
    border: 1px solid #31708f;
    border-radius: 14px;
    -webkit-transition: all .2s ease-in-out;
    -o-transition: all .2s ease-in-out;
    transition: all .2s ease-in-out;
}

.col-md-offset-4 {
    margin-left: 37.5%;
    margin-top: 180px;
}

.col-md-4 {
    width: 26.99%;
}
.form-control {
	border: 1px solid rgba(0, 0, 0, 0.3)!important;
    border-radius: 4px;
}
.thumbnail a > img{
    margin-left:30px;
}
</style>
</head>
<!-- <body class="ready" style="background-color: white;"> -->

<body>
<%-- <table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td><img src="<%=request.getContextPath()%>/images/header_01.jpg" width="1003" height="125" /></td>
	</tr>
	<tr>
		<td height="406">
			<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0" style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7">
				<tr height="20px"></tr>
				<tr>
					<form:form name="otpForm" action="verifyOTPForm.htm" method="post">
						<td align="right" ><spring:message code="LABEL_OTP" text="Please Enter OTP" /></td>
						<td align="right"><input name="otp" type="password" id="otp" /></td>
						<td align="right"><input  type="button" value="<spring:message code="LABEL_SUBMIT" text="Submit" />" onclick="checkValidation();"/></td>
						<td align="center"><input  type="button" value="<spring:message code="LABEL_CANCEL" text="Cancel" />" onclick="cancel();"/></td>
					</form:form>
				</tr>
				<tr height="40px">
			    	<td colspan="4" align="center">
			    		<div style="color: #ba0101;font-weight: bold;font-size: 12px;">
			    			<spring:message code="${message}" text="" /><br/>
			    		</div>
			    	</td>
				</tr>
				<tr height="20px">
					<td colspan="4" align="right">
			    		<a href="welcome.htm"><spring:message code="RESEND_OTP" text="Click here to generate OTP again." /></a>
			    	</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr> 
</table> --%>
	
<!--  vineeth chnages -->
		
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4 col-md-offset-4 thumbnail">
				<!-- Comtainer -->
				<div class="bevel tlbr">
					<div id="paper-top">
						<div class="row">
							<div class="col-lg-12 no-pad">
								<!--     -->
								<a class="navbar-brand logo-text" href="#">
									<!-- WALLET --> <img
									src="<%=request.getContextPath()%>/images/Easybank logo.png"
									alt="logo" class="img_resize1" />
								</a>
							</div>
						</div>
					</div>


					<!-- CONTENT -->
					<div style="min-height: 100px;" id="paper-bg">

						<div class="row">
							<div class="col-lg-12">
								<div class="account-box">

									<form name="otpForm"
										action="verifyOTPForm.htm" method="post"
										autocomplete="off">
										<jsp:include page="csrf_token.jsp" />
										<div class="form-group">
											<label><spring:message code="LABEL_OTP" text="Please Enter OTP" /></label>
											<input name="otp"  id="otp"
												type="password" class="form-control" maxlength="20"
												onCopy="return false" onDrag="return false"
												onDrop="return false" onPaste="return false" />
										</div>
										<div class="form-group">
												
												<input class="btn btn btn-primary" style="border: 1px solid rgba(0, 0, 0, 0.1);" name="Submit"
											value="<spring:message code="LABEL_SUBMIT" text="Submit" />"
											type="button" onclick="checkValidation()" />
											&nbsp;&nbsp;&nbsp;&nbsp;
												<input class="btn btn btn-primary" style="border: 1px solid rgba(0, 0, 0, 0.1);" name="Cancel"
											value="<spring:message code="LABEL_CANCEL" text="Cancel" />"
											type="button" onclick="cancel()" />	
											
										</div>
										<div>
			    								<a style="float:right" href="welcome.htm"><spring:message
												code="RESEND_OTP" text="Click here to generate OTP again." /></a>
										</div>
									</form>									
								</div>								
							</div>
							
						</div>
											<div>
								<c:if test="${message ne null}">
									<b> <font color="RED">
										<spring:message code="${message}"></spring:message></font></b>												
								</c:if>
							</div>
					</div>

				</div>
			</div>

			<!--Footer-->

			<div class="copyright" style="color: black; position: absolute;">
				<spring:message code="LABEL_VERSION_COPYRIGHT" text="v" />
				${version }
				<spring:message code="LABEL_COPYRIGHT" text="Copyright" />
				&copy;
				<spring:message code="LABEL_COPYRIGHT_STRING"
					text="2019 All Rights Reserved Trinity Technologies" />
			</div>

			<!-- / FOOTER -->
			<!-- Container -->
		</div>
	</div>		
	
	<!-- 
    ================================================== -->
	<!-- Main jQuery Plugins -->
	<script type='text/javascript'
		src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type='text/javascript'
		src='<%=request.getContextPath()%>/js/image-background.js'></script>					
</body>
</html>