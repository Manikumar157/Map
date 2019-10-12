<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page import="org.apache.log4j.Logger" %>  
<%@ page isErrorPage="true" %>

<head>
<title><spring:message code="LABEL_PAGE_TITLE" text=""/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<script type="text/javascript" src="<c:url value="/js/utils/util.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/utils/rightClickDisable.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/styles/global.css" />"></link>

<style type="text/css">

<!--

.style7 {

	font-family: Arial;

	font-size: x-small;

}

-->

</style>
<!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css"/>
    <!-- Bootstrap theme -->
    <!--  <link rel="stylesheet" href="css/bootstrap-theme.min.css"> -->

    <link rel="stylesheet" href="css/theme.css"/>
    <link rel="stylesheet" href="css/style_v.1.0.css"/>
    <link rel="stylesheet" href="css/responsive.css"/>


</head>


<body>

    <div id="preloader">
        <div id="status">&nbsp;</div>
    </div>
	<br/><br/><br/><br/>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <!-- Comtainer -->
                <div class="paper-wrap bevel tlbr">
                    <div id="paper-top">
                        <div class="row">
                            <div class="col-lg-12 no-pad">
                                <a class="navbar-brand logo-text" href="#">WALLET</a>
                            </div>
                        </div>
                    </div>
					<div style="min-height:100px;" class="wrap-fluid" id="paper-bg">
					<p style="color:#ff0033;text-align:center;font-weight:bold;">Internal Server Error<br>Please Contact System Administrator.</p>
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
