<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script language="JavaScript">
function logout(){
	var url ="<%=request.getContextPath()%>/login.htm?action=logout";	
	document.welcomeform.method="post";
	document.welcomeform.action=url;
	document.welcomeform.submit();	
	}
function home(){
	var url="<%=request.getContextPath()%>/home.htm?action=home";
	document.welcomeform.method="post";
	document.welcomeform.action=url;
	document.welcome.submit();
}

</script>

<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title>Welcome to GIM !</title>
</head>
<body>
<form name="welcomeform">
<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">      
        <table align="center" width="100%" border="0">
				<tr>
				   <td><img src="images/header_01.jpg" alt="" width="1003" height="125" /></td>
			    </tr>
			    <tr>
        <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            </table></td>
        </tr>
		</table>
</td>
  </tr>
</table>
</form>
</body>
</html>