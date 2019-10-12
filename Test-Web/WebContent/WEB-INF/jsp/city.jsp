<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>

<head>
<script type="text/javascript"> 
  
  function validate(){
  var city=document.getElementById("city").value;
  var pattern='^\[a-zA-ZÀ-ÿ0-9-\' ]{1,32}$';
  var regex = /^[a-zA-Z ]*$/;
  if(city==""){
        alert("<spring:message code="NotEmpty.cityDTO.city" text="Please Enter state name"/>");
        return false;
  }else if(city.charAt(0) == "0"){
      alert("<spring:message code='LABEL.CITY.VALID' text='Please enter a valid state name'/>");
      return false;
  }else if(city.charAt(0) == " " || city.charAt(city.length-1) == " "){
        alert("<spring:message code='LABEL.CITY.SPACE' text='Please remove unwanted spaces in State Name'/>");
        return false;
  }else if(city.search(pattern)==-1){
        alert("<spring:message code='LABEL.CITY.DIGITS' text='Please enter state name with out any special characters'/>");
        return false;
  }else if(city.length > 32){
      alert("<spring:message code='LABEL.CITY.LENGTH' text='State name should not exceed more than 32 characters'/>");
      return false;
}
  else if(!regex.test(city)){
      alert("<spring:message code='LABEL.CITY.NONUMERIC' text='Please enter alphabetic characters only in State Name'/>");
      return false;
}
  else{
        document.cityForm.action="saveCities.htm";
        document.cityForm.submit();
  }
}
 function cancelForm(){
	// vineeth changes on 02-08-2018, bug no: 5806	
	 	document.cityForm.city.value="";
	 	document.getElementById('cityId').value="";
    // change over
		document.cityForm.action="viewCities.htm";
         document.cityForm.submit();
}
 // @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting 
 function cityDetails(url,cityId,countryId){
		document.getElementById('cityId').value=cityId;
		document.getElementById('countryId').value=countryId;
		submitlink(url,'cityForm');
	}
 //@End
</script>

<title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title>

</head>
<body>
<form:form class="form-inline" name="cityForm" id="cityForm" action="saveCities.htm" method="post" commandName="cityDTO">
<jsp:include page="csrf_token.jsp"/>
<form:hidden path="cityId"/>
			<form:hidden path="countryId"/>
<div class="col-md-12">

	<div class="box">
		<div class="box-header">
			<h3 class="box-title">
				<span><spring:message code="TITLE_CITIES" text="Cities"/>-
					<c:forEach  items="${country.languageCode}" var="cnt" varStatus="i">
					<c:if test="${cnt eq lang}" >
					<c:out value="${country.country[i.count-1]}"/>
					</c:if>
					</c:forEach>
				</span>
			</h3><br />
			<label class="col-sm-offset-9">
				<a href="javascript:submitForm('cityForm','showCountries.htm')"><spring:message code="LABEL_VIEWCOUNTRIES" text="View Countries"/> </a>
			</label> 
			<div class="col-md-3 col-md-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
			 <spring:message code="${message}" text="" />
			 </div>
		</div><br/>
		<!-- /.box-header -->
		
		<div class="box-body">
			<!-- form start -->
			<authz:authorize ifAnyGranted="ROLE_addCityAdminActivityAdmin">
			<div class="row">
			<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 15px;">
			
			<div class="row">
			<div class="col-md-3" style="margin-top:20px"></div>
				<div class="col-md-12">
					<label class="col-sm-3"><spring:message code="LABEL_CITY_NAME" text="State Name"/><font color="red">*</font></label> 
				   <form:input path="city" id="city" cssClass="form-control" maxlength="32"/>
				   <FONT color="red"><form:errors path="city" /></FONT>
				</div>
			</div><br/>
		
		<c:choose>
			<c:when test="${(cityDTO.cityId eq null) }">
				<c:set var="buttonName" value="LABEL_ADD" scope="page" />
			</c:when>
			<c:otherwise>
				<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
			</c:otherwise>
		</c:choose>
		<div class="col-sm-6 col-sm-offset-8">
				<input type="button" class="btn-primary btn" id="submitButton"
				value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
				onclick="validate();"></input>
				<c:if test="${cityDTO.cityId ne null}">
				<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
					onclick="cancelForm();"></input>
			</c:if>
		</div><br /><br />
		</div>
	</div>
	</authz:authorize>
	</div>
	</div>
	<div class="box">
		<div class="box-body table-responsive">
			<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
				<thead>
					<tr>
						<th style="width:"><spring:message code="LABEL_CITYNAME" text="State Name" /></th>
						<th><spring:message code="LABEL_ACTION" text="Action"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.results}" var="city">
					<tr>
						<td><c:out value="${city.city}" /></td>																																		
						<td>
						<authz:authorize ifAnyGranted="ROLE_editCityAdminActivityAdmin">
						 <!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting --> 
							<%-- <a href="javascript:submitForm('cityForm','editCity.htm?cityId=<c:out value="${city.cityId}"/>&countryId=<c:out value="${city.country.countryId}"/>')">
								<spring:message	code="LABEL_EDIT" text="Edit" />
							</a> --%>  <a href="javascript:cityDetails('editCity.htm','<c:out value="${city.cityId}"/>','<c:out value="${city.country.countryId}"/>')">
								<spring:message	code="LABEL_EDIT" text="Edit" />
							</a>| </authz:authorize>
							<a href="javascript:cityDetails('viewQuarters.htm','<c:out value="${city.cityId}"/>','<c:out value="${city.country.countryId}"/>')">
								<spring:message code="LABEL_VIEWQUARTERS" text="Quarters"/> 
							</a>
							<%-- <a href="javascript:submitForm('cityForm','viewQuarters.htm?cityId=<c:out value="${city.cityId}"/>')">
								<spring:message	code="LABEL_EDIT" text="Edit" />
							</a> --%>
							<!-- ..@END -->
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
</form:form>

</body>
</html>
