<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<%-- <link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar-en.js"></script> --%>
<script type="text/javascript">
		function checkValidation()
		{
			if(document.loginForm.j_username.value=="")
			{
				alert("<spring:message code="EMPTY_USERNAME" text="Please Enter Valid Username"/>");
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
		function checkreturn(evt){
		
			if(evt){
			  if(evt.keyCode =='13'){
			    checkValidation();
			   }
			}
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
	</script>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body class="ready" style="background-color: white;">

<%response.sendRedirect(request.getContextPath()+"/forwardLogin.htm?message=SESSION_EXPIRED "); %>
	<table width="1003" border="0" align="center" cellpadding="0" cellspacing="0">
	
		<tr>
			<td><img src="<%=request.getContextPath()%>/images/header_01.jpg" alt="" width="1003" height="125" /></td>
		</tr>
		<tr>
			<td height="406">
				<table width="35%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td width="354" height="224" background="images/login_bg.jpg" style="background-repeat: no-repeat;">
							<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td>
										<form  name="loginForm" action="<%=request.getContextPath()%>/login" method="post">
											<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
												<tr>
													<td height="42" align="right" style="text-align: right; padding-right: 10px;">&nbsp;</td>
													<td>&nbsp;</td>
												</tr>
												<tr align="center">
													<td width="99" height="26" align="left"
														style="text-align: right; padding-right: 10px;"><spring:message
														code="LABEL_USERNAME" text="Nom d'utilisateur" /></td>
													<td width="180"><input name="j_username" type="text"
														class="selectbox_login"  maxlength="20" "return false" onDrag="return false" onDrop="return false" onPaste="return false"/></td>
												</tr>
	                                            <tr align="center">
													<td height="46" align="left"
														style="text-align: right; padding-right: 10px;"><spring:message
														code="LABEL_PASSWORD" text="Mot de passe" /></td>
													<td valign="bottom"><input name="j_password"
														type="password" class="selectbox_login" maxlength="20" onCopy="return false" onDrag="return false" onDrop="return false" onPaste="return false" /></td>
											    </tr>
											  	<tr height="20">
													<td colspan="2" align="right" valign="bottom">
														<input  name="Login" value="<spring:message code="LABEL_LOGIN" text="Login" />"
																type="button" onclick="checkValidation()" />
													</td>
												</tr>
											</table>
										</form>
										
									</td>
								</tr>
							</table>
							<table width="135" border="0" cellpadding="0" cellspacing="0" title="Cliquez pour v&#233;rifier - Ce site a choisi le SSL VeriSign pour s&#233;curiser le commerce &#233;lectronique et les communications confidentielles.">
                             <tr>
								 <td width="135" align="center" valign="top"><script type="text/javascript" src="https://seal.verisign.com/getseal?host_name=gim-mobile.gim-uemoa.net&amp;size=M&amp;use_flash=NO&amp;use_transparent=NO&amp;lang=fr"></script><br />
								   <a href="http://www.verisign.fr/ssl/ssl-information-center/" target="_blank"  style="color:#000000; text-decoration:none; font:bold 7px verdana,sans-serif; letter-spacing:.5px; text-align:center; margin:0px; padding:0px;">&#192; propos des certificats SSL</a></td>
							 </tr>
							</table>
						</td>
					</tr>
					<tr height="20">
						<td colspan="2" align="center">
							<c:if test="${message ne null}">
							 <b> <font color="RED"><spring:message code="${message}"></spring:message></font></b>
							</c:if>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
	    	<td width="993" align="center" class="copy_rgt"><spring:message code="LABEL_VERSION_COPYRIGHT" text="v"/><td>${version}</td><spring:message code="LABEL_COPYRIGHT" text="Copyright"/> &copy; <spring:message code="LABEL_COPYRIGHT_STRING" text="2019 All Rights Reserved Trinity Technologies"/></td>
	  	</tr>
	</table>
</body>
</html>
