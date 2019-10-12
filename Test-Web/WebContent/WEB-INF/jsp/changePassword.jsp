<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<html>
<head>
<link rel="shortcut icon" href="images/favicon.gif" />
<!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <!-- Bootstrap theme -->
    <link rel="stylesheet" href="css/theme.css"/>
    <link rel="stylesheet" href="css/style_v.1.0.css"/>
    <link rel="stylesheet" href="css/responsive.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile"/></title>
<style>
img.img_resize1 {
	width: 170px;
	height: 40px;
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
     height:420px;
    -webkit-transition: all .2s ease-in-out;
    -o-transition: all .2s ease-in-out;
    transition: all .2s ease-in-out;
}

.col-md-offset-4 {
    margin-left: 37.0%;
    margin-top: 140px;
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
.navbar-brand {
    margin-left: -20px;
}
.logo-text {
	font-size: 29px;
}
.topspace {
	margin-top:20px;
}
</style>
</head>
<script type="text/javascript">

function validate(){
	var oldPwd=document.getElementById("oldPassword").value;
	var newPwd=document.getElementById("newPassword").value;
	var renewPwd=document.getElementById("rePassword").value;
	if((oldPwd)==""){
		alert("<spring:message code="EMPTY_CHANGE_PASSWORD" text="Please enter old Password"/>");
		
	}else if(newPwd==""){
		alert("<spring:message code="EMPTY_NEW_CHANGE_PASSWORD" text="Please enter new  Password"/>");
	}else if((renewPwd)==""){
		alert("<spring:message code="EMPTY_NEW_CONFIRM_CHANGE_PASSWORD" text="Please enter confirm  Password"/>");
	}else if((oldPwd)==(newPwd) || (oldPwd)==(renewPwd)){
		alert("<spring:message code="SAME_PASSWORD" text="Old password and New password   should not be same"/>");
    }else if(newPwd != renewPwd){		
		alert("<spring:message code="SAME_NEWPASSWORD" text="New password and confirm new password should be same"/>");
	}else if( oldPwd.length<6 ){
		alert("<spring:message code="EMPTY_OLD_PASSWORD_LENGTH" text="Old password length should not be less than 6 character"/>");
	}else if( newPwd.length<6 ){
		alert("<spring:message code="EMPTY_PASSWORD_LENGTH" text="New Password length should not be less than 6 character"/>");
	}
	else {
        document.changePasswordform.method="post";
		document.changePasswordform.action="saveChangePasswordForm.htm";
		document.changePasswordform.submit();

		}
	
}
</script>
<body>
<div class="container-fluid">
        <div class="row">
            <div class="col-md-4 col-md-offset-4 thumbnail">
                <!-- Comtainer -->
                <div class="bevel tlbr">
                    <div id="paper-top">
                        <div class="row">
                            <div class="col-lg-12 no-pad">
                                <a class="navbar-brand logo-text" href="#"><!-- WALLET -->
                                	<spring:message code="TITLE_CHANGEPWD" text="Change Password" />
                                </a>
                            </div>
                        </div>
                    </div>
                    
                    
                    <!-- CONTENT -->
                    <div style="min-height:100px;" id="paper-bg">

                        <div class="row">
                            <div class="col-lg-12">
                                <div class="account-box">
                                    <form:form name="changePasswordform" action="saveChangePasswordForm.htm" method="post" commandName="loginUserDTO" >
									<jsp:include page="csrf_token.jsp"/>
									<div class="pull-right">
                                     <a href="changeLocale.htm?lang=en_US"> <spring:message	code="LABEL_LOGIN" text="Login" /></a> <%-- |
								 	 <a href="welcome.htm"> <spring:message	code="LABEL_HOME" text="Home" /></a> --%>
									</div>
									<br/>
                                        <div class="form-group">
                                            <label><spring:message code="LABEL_OLD_PASSWORD" text="Old Password"/><font color="red">*</font></label>
                                            <form:input path="oldPassword" type="password" maxlength="20" cssClass="form-control"/>
                                        </div>
                                        <div class="form-group">
										
                                            <label><spring:message code="LABEL_NEW_PASSWORD" text="New Password"/><font color="red">*</font></label>
                                            <form:input path="newPassword" type="password" maxlength="20" cssClass="form-control"/>
                                        </div>
                                        <div class="form-group">
										
                                            <label><spring:message code="LABEL_RENEW_PASSWORD" text="Confirm New Password"/><font color="red">*</font></label>
                                            <form:input path="rePassword" type="password" maxlength="20" cssClass="form-control"/>
                                        </div>
                                        <br/>
                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                        <input type="button" class="btn btn btn-primary pull-right" value="<spring:message code="LABEL_UPDATE" text="Update"/>" onclick="javaScript:validate();"/>	
                                    <div class="topspace">
                                    	 <c:if test="${message ne null}">
                             				<b> <font color="RED"><spring:message code="${message}" ></spring:message></font></b>
                            			</c:if>
										
                                    </div>
                                    </form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
				
				<!--Footer-->
				   
					<div class="copyright" style="color:black; position: absolute;"><spring:message code="LABEL_VERSION_COPYRIGHT" text="v"/> ${version }<spring:message code="LABEL_COPYRIGHT" text="Copyright"/> &copy; <spring:message code="LABEL_COPYRIGHT_STRING" text="2019 All Rights Reserved Trinity Technologies"/></div>
				
                <!-- / FOOTER -->
                <!-- Container -->
            </div>
        </div>
 </body>
  </html>