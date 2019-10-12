<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title><spring:message code="LABEL_TITLE" text="EOT Mobile"/></title>
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
		document.changePasswordform.action="saveChangePassword.htm";
		document.changePasswordform.submit();

		}
	
}
</script>
<body>
<div class="col-md-12">
                        <div class="box">
                            <div class="box-header">
                                <h3 class="box-title">
                                    <span><spring:message code="TITLE_CHANGEPWD" text="Change Password" /></span>
                                </h3>
                            </div><br/>
                            <!-- /.box-header -->
							<br/><br/>
							<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
							<spring:message code="${message}" text="" /></div>
                            <div class="box-body" style="border: 1px solid;border-radius:15px;width:70%;margin-left:150px;">
                                <!-- form start -->
                               <form:form class="form-inline" name="changePasswordform" action="saveChangePassword.htm" method="post" commandName="loginUserDTO">
                               <jsp:include page="csrf_token.jsp"/>
								<div class="row">
								<div class="col-md-3"></div>
                                    <div class="col-md-6">
                                        <label class="col-sm-5"><spring:message code="LABEL_OLD_PASSWORD" text="Old Password"/><font color="red">*</font></label> 
                                       	<form:input class="form-control" path="oldPassword" type="password" maxlength="20"/>
                                    </div>
								</div><br/>
								<div class="row">
								<div class="col-md-3"></div>
                                    <div class="col-md-6">
                                        <label class="col-sm-5"><spring:message code="LABEL_NEW_PASSWORD" text="New Password"/><font color="red">*</font></label> 
                                       	<form:input class="form-control" path="newPassword" type="password" maxlength="20"/>
                                    </div>
								</div><br/>
								<div class="row">
								<div class="col-md-3"></div>
                                    <div class="col-md-6">
                                        <label class="col-sm-5"><spring:message code="LABEL_RENEW_PASSWORD" text="Confirm New Password"/><font color="red">*</font></label> 
                                       	<form:input class="form-control" path="rePassword" type="password" maxlength="20"/>
                                    </div>
								</div>
                                    <!-- /.box-body -->

                                    <div class="box-footer">
                                    	<input type="button" class="btn btn-primary pull-right" value="<spring:message code="LABEL_UPDATE" text="Update"/>" onclick="javaScript:validate();"/>
                                        <br/><br/>
                                    </div>
                                </form:form>
							</div><br><br>
							
                            <!-- /.box-body -->
                        </div>
                        <!-- /.box -->
                    </div>
 </body>
  </html>