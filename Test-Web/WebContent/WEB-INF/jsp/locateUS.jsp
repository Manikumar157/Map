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
	<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
	<style type="text/css">
	<!--
	.style1 {
	color: #FFFFFF;
	font-weight: bold;
	}
	td:nth-child(6) {
  		width: 20%;
	}
	-->
	</style>
	<script type="text/javascript">
	/*
	Author : Koushik
	Date : 21/06/2018
	Purpose : Bug 5311
	//Start
	*/
	$(document).ready(function() {
/* 		commenting to make a default country as SouthSudan,
		by vineeth on 12-11-2018
 $("body").on("change","#countryId", function() {
	//	 $("#countryId").change(function() { 
			$country = document.getElementById("countryId").value;
			$csrfToken = $("#csrfToken").val();
			
			$.post("getCityList.htm", {
				country : $country,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("cities").innerHTML="";
				document.getElementById("cities").innerHTML = data;
// 				$('#cityId').trigger('change');
 				setTokenValFrmAjaxResp();
				applyChosen();
			});
		}); 
	*/
// 	$("#cityId").change(function() { 
		$("body").on("change","#cityId", function() {

 		$city = document.getElementById("cityId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getQuartersList.htm", {
				city : $city,
				csrfToken : $csrfToken
			}, function(data) {
				document.getElementById("quarters").innerHTML="";
				document.getElementById("quarters").innerHTML = data;
				setTokenValFrmAjaxResp();
				applyChosen();
			});
		});

	/* 	$("#quarters").change(function() {
			document.getElementById("quarterId").value = document.getElementById("quarters").value ;
		}); */
		//End
	});	

	function isLetter(str) {
		  return str.search(/[a-zA-z]/);
	}
	function validate() {
		var letter=false;
		var alphaNumericAddress = /^([ 0-9a-zA-Z ;'#&,-\/]{0,100})*$/;
		//var alphaNumericAddress = /^[a-zA-ZÀ-ÿ-\' ]*/;
		
		var singleSpace = /^(([ 0-9a-zA-Z;/'#&,-]{0,100}))+(\s\s([ 0-9a-zA-Z;/'#&,-]{0,100})*$)/;
		var locationType = $("#locationType").val();
		var countryId = $("#countryId").val();
		var cityId = $("#cityId").val();
		var quarterId = $("#quarterId").val();
		var status = $("#statusId").val();
		var address = $("#address").val();
		var isnum = /^\d+$/.test(address);
		var onlySpecial = /^[^a-zA-Z0-9]+$/;
//		var networkType = $("#networkType").val();
		$("#cityCode").val(cityId);
		$("#quaterCode").val(quarterId);
		
		if(locationType == ""){
			alert("<spring:message code='ALERT_LOCATION_TYPE' text='Please select a Location Type'/>");
			return false;
		}
		
		else if(countryId == ""){
			alert("<spring:message code='ALERT_COUNTRY' text='Please select a Country'/>");
			return false;
		}
		else if(cityId == ""){
			alert("<spring:message code='ALERT_CITY' text='Please select a State'/>");
			return false;
		}
		if(quarterId == ""){
			alert("<spring:message code='ALERT_QUARTER' text='Please select a City/County'/>");
			return false;
		}
		else if(status == ""){
			alert("<spring:message code='ALERT_STATUS' text='Please select a Status'/>");
			return false;
		}
		else if(address == ""){
			alert("<spring:message code='ALERT_ADDRESS' text='Please enter Address'/>");
			return false;
		}
		/* else if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
            alert("<spring:message code='ALERT_ADDRESS_EMPTY' text='Please remove unwanted space from Address'/>");
            return false;
        }
		else if(address.length<4){
			alert("Please enter Address with minimum 4 characters");
			return false;
		} */
		/* for (var i = 0; i < address.length; i++) {
			if(!isLetter(address.charAt(i))){
				letter=true;
			}
		} */
		/* if(letter===false){
			alert("Please enter Adress with alphabets");
			return false;
		} */
		/* else if((isnum || onlySpecial.test(address))){
			alert("Please enter Adress with alphabets");
			return false;
		} */
		/* if(!address.search(singleSpace)){
			alert("Address must not contain more than one space between words");
			return false;
		} */
		
		/* else if(address.charAt(0) == " " || address.charAt(address.length-1) == " "){
			 alert('<spring:message code="LABEL.ADDRESS.BLANK" text="Please remove the white space from address"/>'); 
			 return false; 	 
	     } */
 		/* else if(address.search(alphaNumericAddress)==-1){
			alert("<spring:message code="ERROR_MESSAGE_ADDRESS_ALPHANUMERIC" text="Address should contain alphanumeric and special characters ,#/-&"></spring:message>");
			return false;
		}  */ 
/* 		else if(networkType == ""){
			alert("<spring:message code='ALERT_NETWORK_TYPE' text='Please select Network Type'/>");
			return false;
		} */
		document.locateUSForm.action = "addLocateUS.htm";
		document.locateUSForm.submit();	
			
	}
	
	 function cancelForm(){
         document.locateUSForm.action="showLocateUsForm.htm";
         document.locateUSForm.submit();
	 }
	
	 function editLocatus(url,locatUsId) {
		 document.getElementById('locatUsId').value=locatUsId;
		 submitlink(url,'locateUSForm');
	}
	</script>
	</head>
<body>
<form:form id="locateUSForm" name="locateUSForm" action="showlocateUSForm.htm" method="post"
 commandName="locateUsdto" class="form-inline">
<jsp:include page="csrf_token.jsp"/>
<form:hidden path="countryId" value="1" id="countryId"/>
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="TITLE_LOCATE_US" text="Locate Us"></spring:message></span>
		</h3>
	</div><br/>
	
			<div class="col-sm-5 col-sm-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text=""/>
			</div>
		<input type="hidden" id="cityCode" name="cityCode"/>
        <input type="hidden" id="quaterCode" name="quaterCode"/>
		<form:hidden path="locatUsId" />
		<div class="box-body">
		<div class="row">
						<div class="col-md-6">
							<label class="col-sm-5"> <spring:message
									code="LABEL_LOCATION_TYPE" text="Location Type" /><font color="red">*</font></label>
							<form:select path="locationType" cssClass="dropdown chosen-select">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								
								<form:options items="${locationTypeList}" itemLabel="locationType"
									itemValue="locationTypeId" />

							</form:select>
						</div>
						<div class="col-md-6">
							<label class="col-sm-5"><spring:message code="LABEL_COUNTRY" text="Country :" /><!-- <font color="red">*</font> -->
							</label> 
							 <label class="col-sm-5"><spring:message code="LABEL_COUNTRY_SOUTH_SUDAN" text="SouthSudan"/>
								</label> 
						<%-- 	 commenting to make a default country as SouthSudan,
							by vineeth on 12-11-2018
							<select class="dropdown chosen-select" id="countryId" name="countryId">
						<option value=""><spring:message code="LABEL_SELECT" text="--Please Select--" />
							<c:set var="lang" value="${language}"></c:set>
								<c:forEach items="${countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
										<c:if test="${cNames.comp_id.languageCode==lang }">
										 <option value="<c:out value="${country.countryId}"/>"  <c:if test="${country.countryId eq locateUsdto.countryId}" >selected=true</c:if> > 
										<c:out 	value="${cNames.countryName}"/>	
										</option>
										</c:if>										
										</c:forEach>
									</c:forEach>
								</option>
							</select> --%>
						</div>
				</div><br/>
			<div class="row">
					
					<div class="col-sm-6">
						<label class="col-sm-5"><spring:message code="LABEL_CITY" text="City:" /><font color="red">*</font></label> 
						<div id="cities"><form:select path="cityId" class="dropdown chosen-select" id="cityId">
							<form:option value=""><spring:message
								code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></form:option>
							<form:options items="${City}" itemValue="cityId"
								itemLabel="city"></form:options>
						</form:select></div>
						<font color="RED"><form:errors path="cityId"></form:errors></font>
					 </div>
						
						<div class="col-sm-6">
							<label class="col-sm-5"><spring:message code="LABEL_QUARTER" text="Quarter:"/><font color="red">*</font></label> 
							<div id="quarters"><form:select path="quarterId" class="dropdown chosen-select" id="quarterId">
								<option value=""><spring:message
									code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
								<form:options items="${quarter}" itemValue="quarterId"
									itemLabel="quarter"></form:options>
							</form:select></div>
							<font color="RED"><form:errors path="quarterId"></form:errors></font><br />
						</div>
						
					</div>
	
				<div class="row">
						<div class="col-sm-6">
					<label class="col-sm-5"><span style="color:#FF0000;"></span><spring:message code="LABEL_STATUS" text="Status"/><font
								color="red">*</font></label> 
					<form:select path="status" id="statusId" cssClass="dropdown chosen-select">
		                <form:option value=""><spring:message code="LABEL_SELECT" text="---Please Select---"/></form:option>
		                <form:option value="10"><spring:message code="LABEL_ACTIVE" text="Active"/></form:option>
		                <form:option value="20"><spring:message code="LABEL_DEACTIVATE" text="Inactive"/></form:option>		
		           </form:select>
				</div>
						<div class="col-md-6">
							<label class="col-sm-5"> <spring:message
									code="LABEL_ADDRESS" text="Address" /><font
								color="red">*</font></label>
								<form:textarea path="address" rows="3" cols="19" />
						</div>
					</div>
					
					<div class="row">
<%-- 						<div class="col-sm-6">
						<label class="col-sm-5"><span style="color:#FF0000;"></span><spring:message code="LABEL_NETWORK_TYPE" text="Network Type"/><font
								color="red">*</font></label> 
						<form:select path="networkTypeId" id="networkType" multiple="multiple" class="multiple">
		                <form:option value="" ><spring:message code="LABEL_SELECT" text="--Please Select--" /></form:option>
		                <form:options items="${networkType}" itemValue="networkTypeId"
									itemLabel="networkType"></form:options>
						</form:select>
						</div> --%>
					<c:choose>
								<c:when test="${(locateUsdto.locatUsId eq null) }">
									<c:set var="buttonName" value="LABEL_ADD" scope="page" />
								</c:when>
								<c:otherwise>
									<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
								</c:otherwise>
							</c:choose>
					<c:choose>
								<c:when test="${(locateUsdto.locatUsId eq null) }">
								<div class="col-sm-6 col-sm-offset-10">
									<div class="btn-toolbar">
							
										<input type="button" class="btn-primary btn" id="submitButton"
													value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
													onclick="validate();"></input>
									</div>
								</div>
								</c:when>
								<c:otherwise>
								<div class="col-sm-6 col-sm-offset-10">
									<div class="btn-toolbar">
							
										<input type="button" class="btn-primary btn" id="submitButton"
													value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
													onclick="validate();"></input>
								
										<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
													onclick="cancelForm();"></input></div>
								</div>
								</c:otherwise>
					</c:choose>
	</div>	
	</div></div>
	<br/>
</form:form>
<div class="col-lg-12">
<div class="box">
			<div class="box-body">
				<div class="box-body table-responsive">
					<table id="example1" class="table table-bordered table-striped"
						style="text-align: center;">
						<thead>
							<tr>
								<th style="text-align: center;"><spring:message code="LABEL_LOCATION_TYPE" text="Location Type" /></th>
								<th style="text-align: center;"><spring:message code="LABEL_COUNTRY" text="Country :" /></th>
								<th style="text-align: center;"><spring:message code="LABEL_CITY" text="City:" /></th>
								<th style="text-align: center;"><spring:message code="LABEL_QUARTER" text="Quarter:"/></th>
								<th style="text-align: center;"><spring:message code="LABEL_STATUS" text="Status"/></th>
								<th style="text-align: center;"><spring:message code="LABEL_ADDRESS" text="Address" /></th>
								<th style="text-align: center;"><spring:message code="LABEL_ACTION" text="Action" /></th>
							</tr>
						</thead>
						<tbody>
							<%
								int i = 0;
							%>
						 	<c:forEach items="${page.results}" var="locationDetails">
								<tr>
									<td><c:out value="${locationDetails.locationType.locationType}" /></td>
									<td><c:set var="lang" value="${language}"></c:set>
										<c:forEach items="${locationDetails.country.countryNames}" var="cname">
										   <c:if test="${cname.comp_id.languageCode==lang }">
										       <c:out 	value="${cname.countryName}" />											
										   </c:if>
										</c:forEach>
									</td>
									<td><c:out value="${locationDetails.city.city}" /></td>
									<td><c:out value="${locationDetails.quarter.quarter}" /></td>
								<c:if test="${locationDetails.status==10}">
									<c:set var="status">
										<spring:message code="LABEL_ACTIVE" text="Active" />
									</c:set>
								</c:if>
								<c:if test="${locationDetails.status==20}">
									<c:set var="status">
										<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
									</c:set>
								</c:if>
								<td><c:out value="${status}" /></td>								
								<td><c:out value="${locationDetails.address}" /></td> 
									<td>
									<a href="javascript:editLocatus('editLocateUS.htm','<c:out value="${locationDetails.locatUsId}"/>')"> 
									<spring:message code="LABEL_EDIT" text="Edit" /></a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<!-- /.box-body -->
		</div>
		</div>
		<!-- /.box -->

	</body>
</html>
