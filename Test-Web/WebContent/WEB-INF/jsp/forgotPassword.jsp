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
<script type="text/javascript"> 

function validate(){
	var userPattern='^\[a-zA-Z0-9]*$';
	var userName=document.getElementById('userName').value;
	var mobileNumber=document.getElementById('mobileNumber');
	var email = document.getElementById('email');
	var mobilepattern='^\[0-9]{7,15}$';
	if(userName==""){
		alert('<spring:message code="NotEmpty.webUserDTO.userName" text="Please enter User Id"/>' ) ;
		return false;
	}else if(userName.charAt(0) == " " || userName.charAt(userName.length-1) == " "){
		alert('<spring:message code="USERNAME_SPACE" text="Please remove white spaces in User Id"/>') ;
		return false;
    }else if(userName.search(userPattern)==-1 ){
    	alert('<spring:message code="VALID_USERNAME" text="Please enter UserName with out any special characters "/>') ;
		return false;
    }else if(mobileNumber.value==""){
		alert('<spring:message code="NotEmpty.webUserDTO.mobileNumber" text="Please enter valid Mobile Number"/>') ;
		return false;
	}else if(mobileNumber.value.charAt(0) == " " || mobileNumber.value.charAt(mobileNumber.length-1) == " "){
    	alert(' <spring:message code="MOBILENUM_SPACE" text="Please remove white spaces in Mobile Number"/>') ;
    	return false;
    }else if(mobileNumber.value.indexOf("0000000")!=-1){
		alert('<spring:message code="VALID_MOBILE_NUM" text="Please enter valid Mobile Number only in digits"/>') ;
		return false;
	}else if(mobileNumber.value.search(mobilepattern)==-1){
		alert('<spring:message code="MOBILE_NUM_ONLYIN_DIGITS" text="Please enter valid Mobile Number " />') ;
		return false;
	}else if(email.value==""){
    	alert('<spring:message code="EMAIL_EMPTY" text="Please enter  Email Id " />')
    	return false;
    }else if(!checkEmail()){
    	alert('<spring:message code="VALID_EMAIL" text="Please enter valid Email Id " />')
    	return false;
    }else{
    	document.getElementById('forgetPage').value="forgotPassword";
		document.forgotPasswordForm.action="<%=request.getContextPath()%>/forwardLogin.htm";
     	document.forgotPasswordForm.submit();
    }
}
function checkEmail() {
	var email = document.getElementById('email');
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	if (email.value!="" && !filter.test(email.value)) {
		return false;
	}else{
		return true;
	}
}
</script>

<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>
<link rel="stylesheet" href="css/bootstrap.css"/>
    <!-- Bootstrap theme -->
    <!--  <link rel="stylesheet" href="css/bootstrap-theme.min.css"> -->

    <link rel="stylesheet" href="css/theme.css"/>
    <link rel="stylesheet" href="css/style_V.1.0.css"/>
    <link rel="stylesheet" href="css/responsive.css"/>
 <style>
  img.img_resize1 {
	width: 150px;
	height: 75px;
	margin-top: -18px;
	margin-left: 25px;
}

img.img_resize {
	width: 60px !important;
	height: 60px !important;
	margin-top: -27px !important;
}

.language_size {
	margin-top: 17px;
	color: #000;
}
 #paper-bg {
	margin-left: 0px !important;
}  	 	
/*    	 	#paper-bg {
		background: #ffffff;
		width: 100%;
		} */
.form_login_box {
	margin-top: 10px;
}

.label_text_color {
	color: #fff;
	margin-top: 6px;
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
#paper-bg {
	/* background: url(../img/linedpaper.gif) repeat; */
	/* background: url(../img/linedpaper.gif) repeat; */
	background: #ffffff;
	min-height: 10px;
	max-height: 70px;
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
    height: 430px;
    -webkit-transition: all .2s ease-in-out;
    -o-transition: all .2s ease-in-out;
    transition: all .2s ease-in-out;
}

.col-md-offset-4 {
    margin-left: 37.4%;
    margin-top: 75px;
}

.col-md-4 {
    width: 26.99%;
}
.form-control {
	border: 1px solid rgba(0, 0, 0, 0.3)!important;
    border-radius: 4px;
}
.thumbnail a > img{
    margin-left:40px;
}
.topspace {
	margin-top:15px;
}
.btn.btn-primary:hover {
	background-color: #367fa9;
}  
 </style>
</head>
<body>
    <div id="preloader">
        <div id="status">&nbsp;</div>
    </div>
	<br/><br/><br/><br/>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-4 col-md-offset-4 thumbnail">
                <!-- Comtainer -->
                <div class="bevel tlbr">
                    <div id="paper-top" style="margin:auto;">
                        <div class="row">
                            <!-- <div class="col-lg-12 no-pad" style="margin-left:14%;">
                                <a class="navbar-brand logo-text" href="#">WALLET</a>
                            </div> -->
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
                    <div style="min-height:100px; " id="paper-bg">
					<div class="box-header">
					<h4 class="box-title">
						<b><spring:message code="LABEL_FORGET_PASSWORD" text="Forgot Password" /></b>
					</h4>
					</div>
					<div class="pull-right">
					<a href="changeLocale.htm?lang=en_US"><spring:message	code="LABEL_LOGIN" text="Login" /></a>
					</div>
					<form:form name="forgotPasswordForm" method="post" commandName="passwordDTO">
                        <input type="hidden" id="forgetPage" name="forgetPage"/>
                        <div class="row">                              							
                                    <div class="col-md-12">
                                        <label><spring:message code="LABEL_USERID" text="User Id" /><font color="red">*</font></label> 
                                       <div><form:input class="form-control col-sm-4" path="userName" id="userName"></form:input></div>
										<FONT color="red"><form:errors path="userName" /></FONT>
                                    </div>
									
                                    <div class="col-md-12">
                                        <label><spring:message code="LABEL_MOBILE_NUM"
												text="Mobile Number" /><font color="red">*</font></label> 
										<div><form:input class="form-control" path="mobileNumber" id="mobileNumber" maxlength="20" />
											<FONT color="red"><form:errors path="mobileNumber" /></FONT>
											</div>
                                    </div>									
                                    <div class="col-md-12">
                                        <label><spring:message code="LABEL_EMAILID"
												text="EmailId" /><font color="red">*</font></label> 
										<div><form:input class="form-control" path="email" id="email" maxlength="32" />
											<FONT color="red"><form:errors path="email" /></FONT>
											</div>
                                    </div>
									<input type="button" class="btn btn-primary" style="margin-left:15px; border: 1px solid rgba(0, 0, 0, 0.1);" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="javaScript:validate();"/>                        
                                </div>
                                </form:form>
                               <%--  <div class="col-md-12 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px; margin-top:0px;">
										<spring:message code="${message}" text="" />
								</div> --%>
								<div class="topspace">
									<c:if test="${message ne null}">
										<b> <font color="RED">
											<spring:message code="${message}"></spring:message></font></b>
									</c:if>
									<c:if test="${sessionexpired ne null}">
										<b> <font color="RED">sessionexpired</font></b>
									</c:if>
								</div>
                    </div>
                </div>								
                <!-- Container -->
            </div>
        </div>
    </div>
    <div id="footer" style="background:transparent;">   
		<div class="copyright" style="color:black; position: absolute;"><spring:message code="LABEL_VERSION_COPYRIGHT" text="v"/> ${version }<spring:message code="LABEL_COPYRIGHT" text="Copyright"/> &copy; <spring:message code="LABEL_COPYRIGHT_STRING" text="2019 All Rights Reserved Trinity Technologies"/></div>
	</div>
    <!-- 
    ================================================== -->
    <!-- Main jQuery Plugins -->
    <script type='text/javascript' src="js/jquery.js"></script>
    <script type='text/javascript' src='js/image-background.js'></script>

</body>
</html>
