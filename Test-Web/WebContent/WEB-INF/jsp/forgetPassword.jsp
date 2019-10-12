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
	var userPattern='^\[a-zA-Z0-9 ]*$';
	var userName=document.getElementById('userName').value;
	var mobileNumber=document.getElementById('mobileNumber');
	var email = document.getElementById('email');
	var mobilepattern='^\[0-9]{12}$';
	if(userName==""){
		alert('<spring:message code="NotEmpty.webUserDTO.userName" text="Please enter User Name"/>' ) ;
		return false;
	}else if(userName.charAt(0) == " " || userName.charAt(userName.length-1) == " "){
		alert('<spring:message code="USERNAME_SPACE" text="Please remove white spaces in User Name"/>') ;
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
    	alert("Please enter  email Id")
    	return false;
    }else if(!checkEmail()){
    	alert("Please enter valid email Id")
    	return false;
    }else{
		document.forgetPasswordForm.action="forgetPassword.htm";
     	document.forgetPasswordForm.submit();
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

</head>
<body>
<table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table>
			<tr>
				<td width="159" valign="top">
				
				</td>
				<td width="844" valign="top">
				<table width="98%" border="0" height="400px" align="center" cellpadding="0" cellspacing="0">
					<tr height="20px">
						<td align="left" class="headding" width="20%"
							style="font-size: 15px; font-weight: bold;"><spring:message
							code="TITLE_FORGETPASSWORD" text="Forget Password" /></td>
					</tr>
					<tr height="20px">
						<td align="center" colspan="2">
						<div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
							code="${message}" text="" /></div>
						</td>
					</tr>
					<form:form name="forgetPasswordForm" method="post" commandName="passwordDTO">
						<tr height="117px">
							<td valign="top" colspan="2">
							<table style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
								align="center"  width="50%" cellpadding="4" cellspacing="4">
								<tr>											
								<td><spring:message code="LABEL_USERNAME" text="User Name" /><font color="red">*</font></td>											
								<td><form:input path="userName" id="userName"></form:input>
								<FONT color="red"><form:errors path="userName" /></FONT></td>							
								</tr>
								<tr>
								<td><spring:message code="LABEL_MOBILE_NUM"
												text="Mobile Number" /><font color="red">*</font></td>
											<td><form:input path="mobileNumber" id="mobileNumber" maxlength="20" />
											<FONT color="red"><form:errors path="mobileNumber" /></FONT></td>
								</tr>
								<tr>
								   <td><spring:message code="LABEL_EMAILID"
												text="EmailId" /><font color="red">*</font></td>
											<td><form:input path="email" id="email" maxlength="20" />
											<FONT color="red"><form:errors path="email" /></FONT></td>								
								</tr>
								
								<tr>
									 <td height="44">&nbsp;</td><td></td>
								  <td align="center">
										<input type="button" value="<spring:message code="LABEL_SUBMIT" text="Submit"/>" onclick="javaScript:validate();"/>		
										</td>
									</tr>
									<tr>
									<td></td><td colspan="2" align="center">
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr height="10px">
							<td colspan="2"></td>
						</tr>						
						
					</form:form>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table>
</body>
</html>
