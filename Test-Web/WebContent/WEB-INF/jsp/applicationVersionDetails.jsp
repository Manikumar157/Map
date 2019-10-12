<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<%-- <link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" /> --%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%-- <title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>
<script type="text/javascript">

var Alertmsg={
		 "validToDate":'<spring:message code="VALID_TODATE" text="Please Enter Valid To Date"/>'
	};
	function check(){	
		Zapatec.Calendar.setup({
	    firstDay          : 1,
	    timeFormat        : "12",
	    electric          : false,
	    inputField        : "releaseDate",
	    button            : "trigger",
	    ifFormat          : "%d/%m/%Y",
	    daFormat          : "%Y/%m/%d",
	    timeInterval          : 01
	  });

	}
 
 
 function validate(){      
    
    var functionalityName=document.getElementById("functionalityName").value;
    var currentVersion=document.getElementById("currentVersion").value;      
    var moduleName=document.getElementById("moduleName").value;
    var description= document.getElementById("description").value;
    var pattern='^\[a-zA-ZÀ-ÿ0-9-.\' ]*$';
    	      
	   if(functionalityName==""){
		   alert('<spring:message code="VALID_VERSION_VALID_FUNCTIONALITY" text="Please enter functionality name"/>');  
		 return false; 
	   }else if(functionalityName.charAt(0) == " " || functionalityName.charAt(functionalityName.length-1) == " "){
		   alert('<spring:message code="VALID_VERSION_FUNCTIONALITY_SPACE" text="Please remove the unwanted white space in functionality name"/>'); 
	         return false;
		 }else if(functionalityName.search(pattern)==-1){
	    	   alert('<spring:message code="VALID_VERSION_FUNCTIONALITY_INVALID" text="Please enter a valid functionality name"/>'); 
	           return false;
	  	  }else if(!checkAllZero(functionalityName)){
	  		 alert('<spring:message code="VALID_VERSION_FUNCTIONALITY_INVALID" text="Please enter a valid functionality name"/>'); 
	           return false;
	  		  
	  	  }else if(moduleName ==""){
			   alert('<spring:message code="VALID_VERSION_EMPTY_MODULE_NAME" text="Please enter module name"/>');  
				 return false; 
			 }else if(moduleName.charAt(0) == " " || moduleName.charAt(moduleName.length-1) == " "){
			   alert('<spring:message code="VALID_VERSION_SPACE_MODULE_NAME" text="Please remove the unwanted white space in module name"/>'); 
	         return false;
		   }else if(moduleName.search(pattern) ==-1){
	    	   alert('<spring:message code="VALID_VERSION_MODULE_NAME_INVALID" text="Please enter a valid module name"/>'); 
	           return false;
	  	   }else if(!checkAllZero(moduleName)){
	  		 alert('<spring:message code="VALID_VERSION_MODULE_NAME_INVALID" text="Please enter a valid module name"/>'); 
		           return false;
		  		  
		  	  }else if(currentVersion==""){
		   alert('<spring:message code="VALID_VERSION_VALID_CURRENTVERSION" text="Please enter current version"/>');  
			 return false; 
		 }else if(currentVersion.charAt(0) == "0"){
		   alert('<spring:message code="VALID_VERSION_ZERO_CURRENTVERSION" text="Please enter a valid Version Number"/>');        
		 return false; 	
	   }else if(currentVersion.charAt(0) == " " || currentVersion.charAt(currentVersion.length-1) == " "){
		   alert('<spring:message code="VALID_VERSION_SPACE_CURRENTVERSION" text="Please remove the unwanted white space in current version"/>'); 
         return false;
	   }else if(currentVersion.search(pattern)==-1 ){
    	   alert('<spring:message code="VALID_VERSION_CURRENTVERSION_INVALID" text="Please enter a valid current verion "/>'); 
           return false;
  	   }else if(!checkAllZero(currentVersion)){
  		 alert('<spring:message code="VALID_VERSION_ZERO_CURRENTVERSION" text="Please enter a valid Version Number"/>'); 
	           return false;
	  		  
	  	  }else if(description ==""){
		   alert('<spring:message code="VALID_VERSION_EMPTY_DESCRIPTION" text="Please enter description "/>');  
			 return false; 
		 }/* else if(description.charAt(0) == " " || description.charAt(description.length-1) == " "){
		   alert('<spring:message code="VALID_VERSION_SPACE_DESCRIPTION" text="Please remove the unwanted white space in description"/>'); 
         return false;  
       }else if( description.search(pattern)==-1 ){
    	   alert('<spring:message code="VALID_VERSION_MODULE_NAME_DESCRIPTION" text="Please enter a valid descrption"/>'); 
           return false;
  	   	}else if(!checkAllZero(description)){
  		 alert('<spring:message code="VALID_VERSION_MODULE_NAME_DESCRIPTION" text="Please enter a valid descrption"/>'); 
	           return false;
	  		  
	  	  } */else{
		   document.applicationVersion.action="saveVersionDetails.htm";
		   document.applicationVersion.submit();         
	   }
 } 
 
 function disableForm(formID){
	 alert("disable form - "+formID);
	 $("#target :input").attr("disabled",'true');
 }
 
 function textCounter(field,cntfield,maxlimit) {
	 if (field.value.length > maxlimit) // if too long...trim it!
	 field.value = field.value.substring(0, maxlimit);
	 // otherwise, update 'characters left' counter
	 else
	 cntfield.value = maxlimit - field.value.length;
 }
 function cancelForm(){
	 document.applicationVersion.action="showVersionDetailsFrom.htm";
	   document.applicationVersion.submit();
 }
 
 function checkAllZero(value){
		
		var count=0;
		for (var i = 0; i < value.length; i++) {
		   
			if(value.charAt(i)==0){
				count++;
			}
		}
		if(count==value.length){
			return false;
		}else{
			return true;
		}

	}
 
//@start Vinod joshi  Date:19/12/2018 purpose:cross site Scripting -->
 function editVerson(url,versionId,versionNumber){
		document.getElementById('versionId').value=versionId;
		document.getElementById('versionNumber').value=versionNumber;
		submitlink(url,'applicationVersion');
	}
</script>

<style type="text/css">
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}

#BankListdiv {
	position: center;
	visibility: visible;
	overflow: hidden;
	border: purple;
	background-color: white;
	border: 1px solid #333;
	padding: 5px;
}
</style>

</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_APP_VERSION" text="Application Version" /></span>
		</h3>
	</div>
	<div class="col-sm-6 col-sm-offset-5">
		<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
			<spring:message code="${message}" text="" />
		</div>
	</div>
	<form:form action="saveVersionDetails.htm" class="form-inline" id="applicationVersion" method="post" name="applicationVersion" commandName="versionDTO">
	<jsp:include page="csrf_token.jsp"/>
	 <authz:authorize ifAnyGranted="ROLE_addApplicationVersionAdminActivityAdmin">
	<div class="col-sm-5 col-sm-offset-10">
		<a 	href="javascript:submitForm('applicationVersion','showVersionFrom.htm')"><strong><spring:message code="LABLE_VIEW_VERSION" text="View Versions" ></spring:message></strong></a>
	</div><br/><br/>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-6">
				<form:hidden path="versionId"></form:hidden>
					<form:hidden path="versionNumber"></form:hidden>
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_CHANNEL" text="Channel Type" /><font color="red">*</font></label> 
					<form:select path="channel" cssClass="dropdown chosen-select"> 
						<form:option value="Mobile Application">Mobile Application</form:option>
						<form:option value="USSD Application">USSD Application</form:option>
						<form:option value="WEB Application">WEB Application</form:option>
					</form:select>
					<font color="RED"> <form:errors path="channel"></form:errors></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_FUNCTIONALITY_NAME" text="Functionality Name" /><font color="red">*</font></label> 
					<form:input path="functionalityName" id="functionalityName" cssClass="form-control" maxlength="32"/> 
					<font color="RED"><form:errors path="functionalityName" /></font>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
				<form:hidden path="versionNumber"></form:hidden>
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_MODULE_NAME" text="Module Name" /><font color="red">*</font></label> 
					<form:input path="moduleName" id="moduleName" cssClass="form-control" maxlength="32"/> 
					<font color="RED"><form:errors path="moduleName" /></font>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_CURRENT_VERSION" text="Current Version" /><font color="red">*</font></label> 
					<form:input path="currentVersion" id="currentVersion" cssClass="form-control" maxlength="32"/> 
					<font color="RED"><form:errors path="currentVersion" /></font>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_DESCRIPTION" text="Description"/><font color="red">*</font></label> 
					<form:textarea path="description" rows="2" cols="19" onKeyDown="textCounter(document.applicationVersion.description,180,180)" onKeyUp="textCounter(document.applicationVersion.description,180,180)"/> <font color="RED">
					<form:errors path="description"></form:errors></font>
				</div>
			</div>
			<c:choose>
				<c:when test="${(versionDTO.versionId eq null)}">
				  <c:set var="buttonName" value="LABEL_ADD" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
				</c:otherwise>
			</c:choose>
			<div class="col-sm-6 col-sm-offset-10">
					<input type="button" class="btn-primary btn" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>" onclick="validate();" />
					<c:if test="${versionDTO.versionId ne null}">
						<input type="button" class="btn-default btn"
							value="<spring:message code="LABEL_CANCEL" text="Cancel" />"	onclick="cancelForm();" />													
					</c:if><br /><br />
			</div><br /><br />
		</div>
		</authz:authorize>
		</form:form>
</div>
	<div class="box">
		<div class="box-body table-responsive">
			<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
				<thead>
					<tr>
						<th><spring:message code="LABEL_CHANNEL" text="Channel" /></th>
						<th><spring:message code="LABEL_MODULE_NAME" text="Module Name" /></th>
						<th><spring:message code="LABEL_FUNCTIONALITY_NAME" text="Functionality Name" /></th>
						<th><spring:message code="LABEL_CURRENT_VERSION" text="Current Version" /></th>
						 <authz:authorize ifAnyGranted="ROLE_editApplicationVersionAdminActivityAdmin">
						<th><spring:message code="LABEL_ACTION_EDIT" text="Action" /></th>
						</authz:authorize>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.results}" var="versionDetails">
					<tr>
						<td><c:out value="${versionDetails.channel}" /></td>
						<td><c:out value="${versionDetails.moduleName}" /></td>
						<td><c:out value="${versionDetails.functionalityName}" /></td>
						<td><c:out value="${versionDetails.currentVersion}" /></td>
						<authz:authorize ifAnyGranted="ROLE_editApplicationVersionAdminActivityAdmin">	
						<%-- <td><a href="javascript:submitForm('applicationVersion','getVersionDetails.htm?versionId=${versionDetails.versionId}&versionNumber=${versionDetails.version.versionNumber}')" /> <spring:message code="LABEL_EDIT" text="Edit" /></td> --%>
						<td><a href="javascript:editVerson('getVersionDetails.htm','<c:out value="${versionDetails.versionId}"/>','<c:out value="${versionDetails.version.versionNumber}"/>')" /> <spring:message code="LABEL_EDIT" text="Edit" /></td>
						</authz:authorize>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
</body>
</html>
