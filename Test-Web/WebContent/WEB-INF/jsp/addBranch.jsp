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

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#cities").change(function() {
		document.getElementById("cityId").value = document.getElementById("city").value ;
		$city = document.getElementById("city").value;
		$csrfToken = $("#csrfToken").val();
		$.post("getQuarters.htm", {
			city : $city,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("quarters").innerHTML="";
			document.getElementById("quarters").innerHTML = data;
			setTokenValFrmAjaxResp();
		});
	});
	$("#quarters").change(function() {
		document.getElementById("quarterId").value = document.getElementById("quarter").value ;
	});
	
});

 function validate(){
 var location=document.getElementById("location").value;
 var address=document.getElementById("address").value;
 var description= document.getElementById("description").value;
 var city=document.getElementById("city").value;
 var quarter=document.getElementById("quarter").value;
 var branchCode=document.getElementById("branchCode").value;
 var numpattern='^[0-9]*$'; 
 var namepattern='^[a-zA-ZÀ-ÿ-\' ]*$';
 var codePattern='^\[0-9A-Za-zÀ-ÿ]*$';
 
       if(location==""){
           alert("<spring:message code="NotEmpty.branchDTO.location" text="Please enter Location"/>");
           return false;
       }else if(location.charAt(0) == " " || location.charAt(location.length-1) == " "){
           alert("<spring:message code="LABEL.LOCATION.SPACE" text="Please Remove Unwanted Spaces in Branch Name"/>");
           return false;
       }else if(location.search(namepattern)==-1){
           alert("<spring:message code="LABEL.LOCATION.DIGITS" text="Please enter Branch Name with out any special characters"/>");
           return false;
       }else if(location.length > 32){
           alert("<spring:message code="LABEL.LOCATION.LENGTH" text="Branch Name should not exceed more than 32 characters"/>");
           return false;
       }else if(branchCode==""){
           alert("<spring:message code="NotEmpty.branchDTO.branchCode" text="Please enter Branch Code"/>");
           return false;
       }else if(branchCode.charAt(0) == " " || branchCode.charAt(branchCode.length-1) == " "){
           alert("<spring:message code="LABEL.BRANCH.SPACE" text="Please remove white spaces in Branch Code"/>");
           return false;
       }else if(branchCode.length < 5 || branchCode.length > 5 ){
           alert("<spring:message code="LABEL_BRANCHCODE_LENGTH" text="Please enter Branch Code length should be 5"/>");
           return false;
       }else if(!checkSpace(branchCode)){	
 	      alert("<spring:message code="LABEL.BRANCH.SPACE" text="Please remove white spaces in Branch Code"/>");
	      return false;
   		}else if(branchCode.search(codePattern)==-1 ){
   	           alert("<spring:message code="LABEL_BRANCHCODE_DIGITS" text="Please enter Branch Code should  with out special characters"/>");
   	           return false;
   	    }else if(branchCode.search(numpattern)==-1 ){
   	           alert("<spring:message code="LABEL_BRANCHCODE_VALID" text="Please enter valid Branch Code"/>");
   	           return false;
   	   }else if(address==""){
           alert("<spring:message code="NotEmpty.branchDTO.address" text="Please enter Address"/>");
           return false;
       }
       /*@-- Author name <vinod joshi>, Date<7/10/2018>, purpose of change < unwantede conditions not allowing white space and special characters> ,
		 @-- Start 
        */
        
       /* else if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
           alert("<spring:message code="LABEL.ADDRESS.SPACE" text="Please remove unwanted spaces in Address"/>");
           return false;
       }else if(address.search(namepattern)==-1){
           alert("Please enter Address with out any special characters");
           return false;
       } */
       
       /*@-- End */
       
       else if(city==""){
           alert("<spring:message code="NotNull.branchDTO.cityId" text="Please select State"/>");
           return false;
       }else if(quarter==""){
           alert("<spring:message code="NotNull.branchDTO.quarterId" text="Please select City/County"/>");
           return false;
       }else if(quarter=="select"){
           alert("<spring:message code="VALID_EMPTY_QUARTER" text="Please select City/County"/>");
           return false;    
       }else if(description.charAt(0) == " " || description.charAt(description.length-1) == " "){
    		 alert("<spring:message code="LABEL.DESCRIPTION.BLANK1" text="Please remove the white space from desription"/>");        
      		 return false;    
       }else if(description.length > 180){
  		 alert("<spring:message code="LABEL.DESCRIPTION.LENGTH" text="Description should not exceed more than 180 characters"/>");        
  		 return false;
       }else{
           document.addBranch.action="saveBranch.htm";
           document.addBranch.submit();
       } 
 }
 
 function textCounter(field,cntfield,maxlimit) {
	 if (field.value.length > maxlimit) // if too long...trim it!
	 field.value = field.value.substring(0, maxlimit);
	 // otherwise, update 'characters left' counter
	 else
	 cntfield.value = maxlimit - field.value.length;
 }
 
 function viewBranch(url,bankId){
	 document.getElementById('bankId').value=bankId;
		submitlink(url,'addBranch');
 }
 
 
 function cancelForm(){
	document.getElementById("location").value="";
	document.getElementById("address").value="";
    document.getElementById("description").value="";
	document.getElementById("cityId").value="";
	document.getElementById("quarterId").value="";
	document.getElementById("branchCode").value="";
	document.addBranch.action="searchBranch.htm";
    document.addBranch.submit();
 }
 function checkSpace(bankCode)
 {
 var count=0;
 for(var i=0;i<bankCode.length;i++){
 if(bankCode.charAt(i)==" "){
 count++;
 }
 }
 if(count>0){
 return false;
 }
 else{
 return true;
 }
 }
</script>

</head>

<body>
	<div class="col-lg-12">
		<form:form name="addBranch" action="saveBranch.htm" method="post" id="addBranch"
			commandName="branchDTO">
			<jsp:include page="csrf_token.jsp"/>
			<form:hidden path="branchId" />
			<form:hidden path="countryId" />
			<form:hidden path="bankId" />
			<form:hidden path="cityId" />
			<form:hidden path="quarterId" />
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_ADD_BRANCH"
								text="Add Branch Details" /> - <c:out
								value="${masterData.bank.bankName}" /></span>
					</h3>
				</div>
				<br />
				<div class="col-md-3 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
				</div>

				<%-- <div class="pull-right">
					<strong><a href="javascript:submitForm('menuForm','searchBranch.htm?bankId=${masterData.bank.bankId}')"><spring:message
								code="LABEL_VIEW_BRANCH" text="View Branches" /></a></strong> &nbsp; &nbsp; &nbsp;
				</div> --%>
				
				<div class="pull-right">
					<strong><a href="javascript:viewBranch('searchBranch.htm','<c:out value="${masterData.bank.bankId}"/>')"><spring:message
							code="LABEL_VIEW_BRANCH" text="View Branches" /></a></strong> &nbsp; &nbsp; &nbsp;
				</div>
				
				<br /> <br />
				<div class="box-body">
					<form class="form-inline" role="form">
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4" style="margin-top: 4px;"><spring:message
										code="LABEL_BRANCH_NAME" text="Brnach Name" /><font
									color="red">*</font></label>
								<div class="col-sm-5">
								<form:input path="location" id="location"
									cssClass="form-control" maxlength="32" style="width:180px;"/>
								<font color="RED"> <form:errors path="location" /></font>
							</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4"><spring:message
										code="LABEL_BRANCHCODE" text="Branch Code" /><font
									color="red">*</font></label>
								<div class="col-sm-5">
								<form:input path="branchCode" cssClass="form-control"
									maxlength="5" style="width:180px;"/>
								<FONT color="red"> <form:errors path="branchCode" /></FONT>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4" style="margin-top: 4px;"><spring:message
										code="LABEL_ADDRESS" text="Address" /><font color="red">*</font></label>
								<div class="col-sm-5">
								<form:textarea path="address" id="address" rows="2" cols="19"
									onKeyDown="textCounter(document.addBranch.address,60,60)"
									onKeyUp="textCounter(document.addBranch.address,180,180)" />
								<font color="RED"> <form:errors path="address" /></font>
								</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4" style="margin-top: 4px;"><spring:message
										code="LABEL_DESC" text="Description" /></label>
								<div class="col-sm-5">
								<form:textarea path="description" id="description"
									rows="2" cols="19"
									onKeyDown="textCounter(document.addBranch.description,180,180)"
									onKeyUp="textCounter(document.addBranch.description,180,180)" />
								<font color="RED"> <form:errors path="description" /></font>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-4" style="margin-top: 4px;"><spring:message
										code="LABEL_CITY" text="City" /><font color="red">*</font></label>
								<div id="cities" class="col-sm-5">
								<form:select path="cityId" class="dropdown" id="city">
									<option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</option>
									<form:options items="${masterData.cityList}" itemValue="cityId"
										itemLabel="city"></form:options>
								</form:select>
								<font color="RED"> <form:errors path="cityId" /></font>
							</div>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-4" style="margin-top: 4px;"><spring:message
										code="LABEL_QUARTER" text="Quarter" /><font color="red">*</font></label>
								<div id="quarters" class="col-sm-5">
								<form:select path="quarterId" class="dropdown" id="quarter">
									<option value="">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</option>
									<form:options items="${quarterList}" itemValue="quarterId"
										itemLabel="quarter"></form:options>
								</form:select>
								<font color="RED"> <form:errors path="quarterId" /></font>
								</div>
							</div>
						</div>
						<br />
						<c:choose>
							<c:when test="${(branchDTO.branchId eq null)}">
								<c:set var="buttonName" value="LABEL_ADD" scope="page" />
							</c:when>
							<c:otherwise>
								<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
							</c:otherwise>
						</c:choose>
						<div class="col-sm-6 col-sm-offset-10">
							<div class="btn-toolbar">
								<input type="button" class="btn-primary btn" id="submitButton"
									value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
									onclick="validate();"></input>
								<%-- <c:if test="${branchDTO.branchId eq null}"> commented bcz bugNo:5296--%>
									<input type="button" class="btn-primary btn"
										value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
										onclick="cancelForm();" />
							<%-- 	</c:if> --%>

							</div>
						</div>
					</form>
					<br /> <br />
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>
