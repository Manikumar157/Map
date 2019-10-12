

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<html>

<head>

<%-- <link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" /> --%>

<title><spring:message code="LABEL_TITLE" text="EOT Mobile"/></title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="stylesheet" href="css/bootstrap.css"/>
    <!-- Bootstrap theme -->
    <!--  <link rel="stylesheet" href="css/bootstrap-theme.min.css"> -->

    <link rel="stylesheet" href="css/theme.css"/>
    <link rel="stylesheet" href="css/style_V.1.0.css"/>
    <link rel="stylesheet" href="css/responsive.css"/>


<style type="text/css">

<!--

.style7 {

	font-family: Arial;

	font-size: x-small;

}

-->

</style>



</head>




<body>

    <div id="preloader">
        <div id="status">&nbsp;</div>
    </div>
	<br><br><br><br>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <!-- Comtainer -->
                <div class="paper-wrap bevel tlbr">
                    <div id="paper-top">
                        <div class="row">
                            <div class="col-lg-12 no-pad">
                                <!--     -->
                                <a class="navbar-brand logo-text" href="#">WALLET</a>
                            </div>
                        </div>
                    </div>
					<div style="min-height:100px;" class="wrap-fluid" id="paper-bg">
					We apologise for the inconvenience. The m-GURUSH-Wallet page you've requested is not available at this time. There are several possible reasons you are unable to reach the page:
					<ul>
					<br>
					<li>If you tried to load a bookmarked page, or followed a link from another Web site, it's possible that the URL you've requested has been moved</li><br>
					<li>The web address you entered is incorrect</li><br>
					<li>Technical difficulties are preventing us from display of the requested page</li><br>
					</ul>
					We want to help you to access what you are looking for
					<ul><br>
					<li><a href="<%=request.getContextPath()%>/changeLocale.htm?lang=en_US" target="_blank">Visit our Login page</a></li><br>
					<!-- <li><a href="http://www.gim-uemoa.org/" target="_blank">Visit our site map</a></li><br> -->
					<li><a href="https://mgurush.com/" target="_blank">Visit our site map</a></li><br>
					<li>Try returning to the page later</li>
					</ul>
					</div>
                </div>
                <!-- Container -->
            </div>
        </div>
    </div>

    <!-- 
    ================================================== -->
    <!-- Main jQuery Plugins -->
    <script type='text/javascript' src="js/jquery.js"></script>
    <script type='text/javascript' src='js/image-background.js'></script>
</body>

</html>
