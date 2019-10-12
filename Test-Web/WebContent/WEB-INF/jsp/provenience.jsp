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
	  var provenience = $("#provenienceName").val();
	  var pattern='^\[a-zA-ZÀ-ÿ0-9-\' ]{1,32}$';
	  if(provenience==""){
	        alert("<spring:message code="LABEL.PROVENIENCE.VALID" text="Please enter valid provenience name"/>");
	        return false;
	  }else if(provenience.charAt(0) == "0"){
	      alert("<spring:message code='LABEL.PROVENIENCE.VALID' text='Please enter valid provenience name'/>");
	      return false;
	  }else if(provenience.charAt(0) == " " || provenience.charAt(provenience.length-1) == " "){
	        alert("<spring:message code='LABEL.PROVENIENCE.SPACE' text='Please remove unwanted spaces in provenience Name'/>");
	        return false;
	  }else if(provenience.search(pattern)==-1){
	        alert("<spring:message code='LABEL.PROVENIENCE.DIGITS' text='Please enter provenience Name with out any special characters'/>");
	        return false;
	  }else if(provenience.length > 32){
	      alert("<spring:message code='LABEL.PROVENIENCE.LENGTH' text='Provenience Name should not exceed more than 32 characters'/>");
	      return false;
	  }
	  else{
	  document.provenienceForm.action="saveProvenience.htm";
      document.provenienceForm.submit();
	  }
  
}
  function cancelForm(){
         document.provenienceForm.action="viewProvenience.htm";
         document.provenienceForm.submit();
}
</script>
</head>
<body>
<form:form class="form-inline" name="provenienceForm" id="provenienceForm" method="post" commandName="provenienceDTO">
<jsp:include page="csrf_token.jsp"/>
<div class="col-md-12">

	<div class="box">
		<div class="box-header">
			<h3 class="box-title">
				<span><spring:message code="TITLE_PROVENIENCE" text="Provenience"/>-
					<c:forEach  items="${country.languageCode}" var="cnt" varStatus="i">
					<c:if test="${cnt eq lang}" >
					<c:out value="${country.country[i.count-1]}"/>
					</c:if>
					</c:forEach>
				</span>
			</h3><br />
			<label class="col-sm-offset-9">
				<a href="javascript:submitForm('provenienceForm','showCountries.htm')"><spring:message code="LABEL_VIEWCOUNTRIES" text="View Countries"/> </a>
			</label>
		</div><br/>
		<div class="col-md-4 col-md-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;">
			 <spring:message code="${message}" text="" />
			 </div>
		<!-- /.box-header -->
		<div class="box-body">
			<!-- form start -->
			<authz:authorize ifAnyGranted="ROLE_addCityAdminActivityAdmin">
			<div class="row">
			<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 15px;">
			<br/>
			 
			<div class="row">
			<div class="col-md-4 col-md-offset-1"></div>
			<form:hidden path="provenienceId"/>
			<form:hidden path="countryId"/>
				<div class="col-md-12 col-md-offset-1">
					<label class="col-sm-3"><spring:message code="LABEL_PROVENIENCE" text="Provenience"/><font color="red">*</font></label> 
				   <form:input path="provenienceName" cssClass="form-control" maxlength="32"/>
				   <FONT color="red"><form:errors path="provenienceName" /></FONT>
				</div>
			</div><br/>
		
		 <c:choose>
			<c:when test="${(provenienceDTO.provenienceId eq null) }">
				<c:set var="buttonName" value="LABEL_ADD" scope="page" />
			 </c:when>
			<c:otherwise>
				<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
			</c:otherwise>
		</c:choose> 
		<div class="col-sm-6 col-sm-offset-8">
				<input type="button" class="btn-primary btn" id="submitButton" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
				onclick="validate();"></input>
				 <c:if test="${provenienceDTO.provenienceId ne null}">
				<input type="button" class="btn-default btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
					onclick="cancelForm();"></input>
			 </c:if> 
		</div><br /><br />
		</div>
	</div>
	</authz:authorize>
	</div>
	
	<div class="box">
		<div class="box-body table-responsive">
			<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
				<thead>
					<tr>
						<th style="width:"><spring:message code="LABEL_PROVENIENCE" text="Provenience" /></th>
						<th><spring:message code="LABEL_ACTION" text="Action"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.results}" var="provenience">
					<tr>
						 <td><c:out value="${provenience.provenienceName}" /></td>	 																																
						<td>
						<authz:authorize ifAnyGranted="ROLE_editCityAdminActivityAdmin">
							<a href="javascript:submitForm('provenienceForm','editProvenience.htm?provenienceId=<c:out value="${provenience.provenienceId}"/>&countryId=<c:out value="${provenience.country.countryId}"/>')">
								<spring:message	code="LABEL_EDIT" text="Edit" />
							</a> | </authz:authorize>
							<a href="javascript:submitForm('provenienceForm','viewCities.htm?provenienceId=<c:out value="${provenience.provenienceId}"/>')">
								<spring:message code="LABEL_VIEWCITIES" text="View Cities"/> 
							</a>
						</td> 
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

</form:form>

</body>
</html>
