<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>

<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile"></spring:message></title>
<link rel="shortcut icon" href="images/favicon.gif" />

<script>
/* document.attachEvent("onkeydown", my_onkeydown_handler);
function my_onkeydown_handler()
{
switch (event.keyCode)
{

case 116 : // 'F5'
event.returnValue = false;
event.keyCode = 0;
window.status = "We have disabled F5";
break;
}
} */

var fn = function (e)
{

    if (!e)
        var e = window.event;

    var keycode = e.keyCode;
    if (e.which)
        keycode = e.which;

    var src = e.srcElement;
    if (e.target)
        src = e.target;    

    // 116 = F5
    if (116 == keycode)
    {
        // Firefox and other non IE browsers
        if (e.preventDefault)
        {
            e.preventDefault();
            e.stopPropagation();
        }
        // Internet Explorer
        else if (e.keyCode)
        {
            e.keyCode = 0;
            e.returnValue = false;
            e.cancelBubble = true;
        }

        return false;
    }
}

// Assign function to onkeydown event
document.onkeydown = fn;

</script>

<%@ page language="java" %>
<%
    String username=(String) session.getAttribute("username");
    if(username==null) username="";
%>

</head>
<body>
<form name="welcomeform">
<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">      
        <table align="center" width="100%" border="0">
                <tr>
                  <td width="75%">&nbsp;</td>
                  <td style="text-align:right; padding-right:10px;" width="25%"><b> <spring:message code="LABEL_WELCOME" text="Welcome:"/> <%=username%> </b></td>
                </tr>
                <tr>
                   <td colspan="2"><img src="images/header_01.jpg" alt="" width="1003" height="125" /></td>
                </tr>
                <tr>
                <td colspan="3" valign="top"> </td>
                </tr>
                <tr><td colspan="2"><table width="100%"><tr><td width="31%" height="32" class="leftlink"><input type=text name="clock" size=21 style="border-color:white; border-style:solid;font-weight:none;" value="${clock}"/></td>
                    <td width="57%" align="right" class="leftlink">
                             <a href="#" onclick="home();"><spring:message code="LABEL_HOME" text="Home"/></a> | 
                             <a href="changePassword.htm"><spring:message code="LABEL_CHANGE_PASSWORD" text="Change Password"/></a> | 
                             <a href="#" onclick="logout();"><spring:message code="LABEL_LOGOUT" text="Logout"/>&nbsp;&nbsp;&nbsp;</a>
                     </td>
                     </tr></table></td>
                  </tr>
        </table>
</td>
  </tr>
</table>
</form>
<script type="text/javascript">
function clockTick()
{
var now=new Date();
document.welcomeform.clock.value = now.toLocaleString();
document.welcomeform.clock.blur();
setTimeout("clockTick()", 1000);
}
clockTick(); 


</script>

</body>
</html>
