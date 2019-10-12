<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><spring:message code="LABEL_TITLE" text="GIM Wallet" /></title>
<link rel="shortcut icon" href="images/favicon.gif" />
<script type="text/javascript">

        function checkValidation()
        {
            
            if(document.loginForm.j_username.value=="")
            {
                alert("<spring:message code="EMPTY_USERNAME" text="Please enter valid UserId"/>");
                return false;
            }    
            else if(document.loginForm.j_password.value=="")
            {
                alert("<spring:message code="EMPTY_PASSWORD" text="Please Enter Valid Password"/>");
                return false;
            }else{
                document.loginForm.action ="<%=request.getContextPath()%>/login";
                //document.loginForm.mode.value='login';
                document.loginForm.submit();
            }
            
        }
        function checkreturn(event){
   
            var key;
            if (window.event) {
                  key = window.event.keyCode;
            } else {
                  key = event.which;
            }
            if (key == 13) {
            	
            	checkValidation();
            	
            }

           
            
        }
        function submitForgot(url){
        	document.getElementById('forgetPage').value="showForgotPasswordForm";
        	document.loginForm.action =url;
            document.loginForm.submit();
        }
        function changeLocale(id){
            
            document.loginForm.action ="<%=request.getContextPath()%>/changeLocale.htm";
            document.loginForm.submit();
        }    
        
         function checkItNoPaste(e){
             
             var code = (document.all) ? event.keyCode:e.which;

                var msg = "Sorry, this functionality is disabled.";
                if (parseInt(code)==17) //CTRL
                {
                //alert(msg);
                window.event.returnValue = false;
                }
         }
        
         function noBack() {
     		window.history.forward();
     	}
     	noBack();
     	window.onload = noBack;
     	window.onpageshow = function(evt) {
     		if (evt.persisted)
     			noBack();
     	};
     	window.onunload = function() {
     		void (0);
     	};
     	  
       </script>



<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="css/bootstrap.css" />
<!-- Bootstrap theme -->
<link rel="stylesheet" href="css/theme.css" />
<link rel="stylesheet" href="css/style_v.1.0.css" />
<link rel="stylesheet" href="css/responsive.css" />


<style>
img.img_resize1 {
	width: 150px;
	height: 75px;
	margin-top: -10px;
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
    height: 385px;
    -webkit-transition: all .2s ease-in-out;
    -o-transition: all .2s ease-in-out;
    transition: all .2s ease-in-out;
}

.col-md-offset-4 {
    margin-left: 37.0%;
    margin-top: 95px;
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
	margin-top:20px;
}
</style>
</head>
<!-- <body  class="ready" style="background-color: white;">-->
<body>
	<%response.sendRedirect(request.getContextPath()+"/forwardLogin.htm "); %>
	<div id="preloader">
		<div id="status">&nbsp;</div>
	</div>
	<br />
	<br />
	<br />
	<br />
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

									<form name="loginForm" action="<%=request.getContextPath()%>/login" method="post" autocomplete="off">
									<jsp:include page="/WEB-INF/jsp/csrf_token.jsp"/>
									<input type="hidden" id="forgetPage" name="forgetPage"/>
										<br />
										<div class="form-group">
											<label><spring:message code="LABEL_USERID"
													text="Nom d'utilisateur" /></label> <input name="j_username"
												type="text" class="form-control" maxlength="20"
												onCopy="return false" onDrag="return false"
												onDrop="return false" onPaste="return false" />
										</div>
										<div class="form-group">

											<label><spring:message code="LABEL_PASSWORD"
													text="Mot de passe" /></label> <input name="j_password"
												type="password" class="form-control" maxlength="20"
												onCopy="return false" onDrag="return false"
												onDrop="return false" onPaste="return false"
												onkeypress="checkreturn(event)" />
										</div>
										<br />
																				
										<input class="btn btn-primary" style="border: 1px solid rgba(0, 0, 0, 0.1);" name="Login"
											value="<spring:message code="LABEL_LOGIN" text="Login" />"
											type="button" onclick="checkValidation()" />
											&nbsp;&nbsp;&nbsp;&nbsp;
											<span  style="float:right; cursor: pointer;" onclick="submitForgot('showForgotPasswordForm.htm')" href="#" ><spring:message
												code="LABEL_FORGET_PASSWORD" text="Forgot Password?" /></span>
									</form>
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
						</div>
					</div>

				</div>
			</div>
			
			<!-- Container -->
		</div>
	</div>
	</div>
		<div>
			<a href="https://mgurush.com/?page_id=14" style="font-size: 12px !important;">FAQs</a> &nbsp;&nbsp;&nbsp;&nbsp;
			<a href="https://mgurush.com/?page_id=3" style="font-size: 12px !important;">Privacy Policy</a> &nbsp;&nbsp;&nbsp;&nbsp;
			<a href="https://mgurush.com/terms_and_conditions.pdf" style="font-size: 12px !important;">Terms and Conditions</a>
			<!--Footer-->

			<div class="copyright" style="color: black; position: inherit;">
				<spring:message code="LABEL_VERSION_COPYRIGHT" text="v" />
				${version }
				<spring:message code="LABEL_COPYRIGHT" text="Copyright" />
				&copy;
				<spring:message code="LABEL_COPYRIGHT_STRING"
					text="2019 All Rights Reserved Trinity Technologies" />
			</div>

			<!-- / FOOTER -->
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