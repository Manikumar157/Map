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
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<%-- <link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" /> --%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/clearingHouseManagement.js"></script>
<script type="text/javascript">

var Alertmsg={
		
		 "settlementDateFormat":"<spring:message code="VALID_VERSION_RELEASE_DATE_FORMAT" text=" Release date format must be : dd/mm/yyyy"/>",
		 "validSettlementDay":"<spring:message code="VALID_VERSION_RELEASE_DAY" text="Please enter valid  Release date"/>",
		 "validSettlementMonth":"<spring:message code="VALID_VERSION_RELEASE_MONTH" text="Please  enter  a valid  month in Release date"/>",
		 "validSettlementDay":"<spring:message code="VALID_VERSION_RELEASE_DAY" text="Please  enter  a valid  days in Release  date "/>",
		 "validSettlementMonth":"<spring:message code="VALID_VERSION_RELEASE_MONTH" text="Please  enter  a valid  month in Release  date"/>",
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
    
   		 var versionNumber=document.getElementById("versionNumber").value;
 	 	 var releaseDate=document.getElementById("releaseDate").value;
    	var pattern='^\[0-9.\]*$';
    	var objRegExp  = /(^\d{1,2}.\d{1,2}.\d{1,2}$)/;
    	var dt1  = parseInt(releaseDate.substring(0,2),10);
	    var mon1 = parseInt(releaseDate.substring(3,5),10)-1;
	    var yr1  = parseInt(releaseDate.substring(6,10),10);
	     var relase = new Date(yr1, mon1, dt1);
	      var cdate=new Date();
	
		var month = cdate.getMonth();
		var day = cdate.getDate();
		var year = cdate.getFullYear();
		var ccdate=new Date(year,month,day); 
	   if(versionNumber==""){
		 alert('<spring:message code="LABEL.VERSION.VALID" text="Please enter a  Version Number"/>');  
		return false; 
	   }else if(versionNumber.charAt(0) == "0"){
		 alert('<spring:message code="LABEL.VERSION.VALID" text="Please enter a valid Version  Number"/>');        
		return false; 	
	   }else if(versionNumber.charAt(0) == " " || versionNumber.charAt(versionNumber.length-1) == " "){
		 alert('<spring:message code="LABEL.VERSION.SPACE" text="Please remove unwanted white sapce"/>');
        return false;
       }else if(versionNumber.search(pattern)==-1){
    	   alert('<spring:message code="LABEL.VERSION.VALID" text="Please enter a valid Version Number"/>');
        return false;
       }else if(objRegExp.test(versionNumber)==false){
    	   alert('<spring:message code="LABEL.VERSION.VALID" text="Please enter a valid Version Number"/>');
        return false; 
	   }else if(releaseDate==""){
    	   alert('<spring:message code="LABEL.RELEASE.DATE" text="Please select Release Date"/>');
           return false;
   	   }else if(!validdate(releaseDate)){
   		
   	 	return false;  
   	 	
   		 }else if(relase<ccdate){
    	   alert('<spring:message code="LABEL.RELEASE.VALID.RELEASEDATE" text="Please select Release Date not less than todays date"/>');
           return false;
   	   }else{
		   document.applicationVersion.action="saveVersion.htm";
		   document.applicationVersion.submit();       
	   }
 } 

 
 function updateValidate(){      
	    
	    var versionNumber=document.getElementById("versionNumber").value;
	    document.applicationVersion.action="updateVersion.htm";
	    document.applicationVersion.submit();    
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
	 document.applicationVersion.action="showVersionFrom.htm";
	   document.applicationVersion.submit();
 }
 
 function validdate(releaseDate)  
 {  
  
 if (validdatereleaseDate(releaseDate)==false)  
 {  

 return false;  
 }  
 return true ; 
 }  

 function validdatereleaseDate(releaseDate)
 {
 //var DateOfBirth=new String("12/912/2012");
 	var monthdays = finaldays(12);  
 var Char1 = releaseDate.charAt(2);
 var Char2 = releaseDate.charAt(5);
 var chsep= "/" ;
 var fcpos1=releaseDate.indexOf(chsep); 
 var fcpos2=releaseDate.indexOf(chsep,fcpos1+1);  
 if (fcpos1==-1 || fcpos2==-1 ){  
 	
 alert(Alertmsg.settlementDateFormat);  
 return false;  
 } 

 if (releaseDate.length<10 || releaseDate.length>10){  

 	alert(Alertmsg.settlementDateFormat);  
 	return false;  
 } 
 /*if ( Char1 =='/' && Char2 == '/' )
 {
  alert ('Please check foramt');
 return false;
 }*/

 var day;
 var month;
 var year;

 day = releaseDate.substring(0,2);
 month = releaseDate.substring(3,5);
 year = releaseDate.substring(6,10);


 if(!validDay(day))
 {
 alert(Alertmsg.validSettlementDay);

 return false;
 }

 if(!validMonth(month)){	
 alert(Alertmsg.validSettlementMonth);
 
 return false;
 }

 if ((month==2 && day>februarycheck(year))   || day > monthdays[month]){  
 	
 	alert(Alertmsg.validSettlementDay);  
 	return false ; 
 	} 
 /*if(!validYear(year)){
 		
 alert('Invalid year in date of birth');
 DateOfBirth=null;
 return false;
 }*/

 } // end func

 function IsNumeric(sText)
 {
 var ValidChars = "0123456789.";
 var IsNumber=true;
 var Char;

 for (i = 0; i < sText.length && IsNumber == true; i++)
 {
 Char = sText.charAt(i);
 if (ValidChars.indexOf(Char) == -1)
 {
 IsNumber = false;
 }
 }

 return IsNumber;
 } // end func


 function validDay(day)
 {
 if ( IsNumeric(day) && day.length<3)
 {
 if( day >0 && day <32)
 {
 return true;
 }
 else
 {
 return false;
 }
 }
 else
 {
 return false;
 }

 }// end func


 function validMonth(month)
 {
 if ( IsNumeric(month) )
 {
 if( month >0 && month <13)
 {
 return true;
 }
 else
 {
 return false;
 }
 }
 else
 {
 return false;
 }
 }// end func



 function validYear(year)
 {
 var d = new Date();
 var currentYear = d.getFullYear();

 if( year.length!= 4) { return false; }

 if ( IsNumeric(year) )
 {
 if( year >0 && year <=currentYear)
 {
 return true;
 }
 else
 {
 return false;
 }

 }
 else
 {
 return false;
 }

 }
 
 
 //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting -->
	function applicationVersionDetail(url,versionNumber){
		document.getElementById('versionNumber').value=versionNumber;
		submitlink(url,'applicationVersion');
	}
	//@end
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
<div class="box" style="height:230px;">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_APP_VERSION_INFORMATION" text="Application Version" /></span>
		</h3>
	</div>
	<div class="col-sm-5 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
		<spring:message code="${message}" text="" />
	</div>
	<div class="box-body">
	<div class="col-sm-6 col-sm-offset-3" style="border: 1px solid;border-radius:15px;"><br />
	<form:form action="saveVersion.htm" class="form-inline"  method="post" name="applicationVersion" id="applicationVersion" commandName="versionDTO">
		<jsp:include page="csrf_token.jsp"/>
		<!-- //@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting versionnumber should available ifNot Granted   -->
		<authz:authorize ifNotGranted="ROLE_addApplicationVersionAdminActivityAdmin">
		<form:hidden path="versionNumber" />
		</authz:authorize>
		<!--  @end-->
		  <authz:authorize ifAnyGranted="ROLE_addApplicationVersionAdminActivityAdmin">  
				<div class="col-sm-12">
					<label class="col-sm-6"><spring:message code="LABEL_VERSION_NUMBER" text="Version Number"/><font color="red">*</font></label> 
					<c:if test="${ versionDTO.versionNumber ne null }">
						<c:set var="flag" value="true" />
					</c:if>
					<form:input path="versionNumber" id="versionNumber" cssClass="form-control" readonly="${flag}" maxlength="32"/> 
					<font color="RED"><form:errors path="versionNumber" /></font>
				</div>
				<div class="col-sm-12">
					<label class="col-sm-6" style="margin-top:4px;"><spring:message code="LABEL_RELEASE_DATE" text="Release Date" /><font color="red">*</font></label> 
					<form:input path="releaseDate" class="form-control datepicker" />
				</div>
			
			<div class="">
			<c:choose>
				<c:when test="${(versionDTO.versionNumber eq null)}">
					<c:set var="buttonName" value="LABEL_ADD" scope="page" />
					<input type="button" class="btn btn-primary col-sm-offset-8" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>" onclick="validate();" />
				</c:when>
				<c:otherwise>
					<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
			 		<input type="button" class="btn btn-primary col-sm-offset-8" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>" onclick="updateValidate();" />
				</c:otherwise>
			</c:choose>
			<c:if test="${versionDTO.versionNumber ne null}">
				<input type="button" value="<spring:message code="LABEL_CANCEL"  text="Cancel" />"	class="btn btn-primary" onclick="cancelForm();" />													
			</c:if>
			</div>
		</authz:authorize>
		</form:form>
		</div>
</div>
</div>
			<div class="box">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_RELEASE_NUMBER" text="Release Number" /></th>
								<th><spring:message code="LABEL_RELEASE_DATE" text="Release Date" /></th>
								<th><spring:message code="LABEL_VERSION_NUMBER" text="Version Number" /></th>
								<authz:authorize ifAnyGranted="ROLE_editApplicationVersionAdminActivityAdmin">
								<th><spring:message code="LABEL_ACTION_EDIT" text="Action" /></th>
								</authz:authorize>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${page.results}" var="version">
							<tr>
								<td><c:out value="${version.releaseNumber}" /></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy" value="${version.releaseDate}" /></td>
								<td>
								
				<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting  -->
								<%-- <a href="javascript:submitForm('applicationVersion','showVersionDetailsFrom.htm?versionNumber=<c:out value="${version.versionNumber}"/>')" /> --%>
								<a href="javascript:applicationVersionDetail('showVersionDetailsFrom.htm','<c:out value="${version.versionNumber}"/>')" />
								<c:out value="${version.versionNumber}" /></td>	
								 <authz:authorize ifAnyGranted="ROLE_editApplicationVersionAdminActivityAdmin">  
								<td align="left"><%-- <a href="javascript:submitForm('applicationVersion','getVersion.htm?versionNumber=<c:out value="${version.versionNumber}"/>')" /><spring:message code="LABEL_EDIT" text="Edit" /> --%>
								<a href="javascript:applicationVersionDetail('getVersion.htm','<c:out value="${version.versionNumber}"/>')" /><spring:message code="LABEL_EDIT" text="Edit" /></td>
				<!--End  -->
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
