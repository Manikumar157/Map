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
     	var pattern='^\[a-zA-ZÀ-ÿ-\' 0-9 ]*$';
 		var whitespaceChars = " \t\n\r\f";
	 	var billerName=document.getElementById("billerName").value;
	  	var billerType=document.getElementById("billerType").value;
	  //	var bankId=document.getElementById("bankId").value;
		var country=document.getElementById("countryId").value;
	  if(billerName==""){	  
		  alert("<spring:message code='NotEmpty.billerDTO.billerName' text='Please enter Biller Name'/>");
		  document.operatorForm.operatorName.focus();		  
	  }else if (billerName.charAt(0).match(/^[0-9]*$/)) {
			alert("<spring:message code='LABEL.BILLERNAME.NUMBERS' text='Biller name should start with alphabets only'/>");
			return false;
	  }else if(billerName.charAt(0) == " "){
          alert("<spring:message code='LABEL.BILLERNAME.SPACE' text='Please remove unwanted white spaces in Biller Name'/>");
          return false;
      }else if(billerName.search(pattern)==-1){
	      alert("<spring:message code='LABEL.BILLERNAME.DIGITS' text='Please enter Biller Name with out any special characters'/>");
		  return false;
	  }else if(billerName.length > 100){
	      alert("<spring:message code='LABEL.BILLERNAME.LENGTH' text='Biller Name should not exceed 100 characters'/>");
		  return false;
	  }else if(billerType==""){
		  alert("<spring:message code='NotNull.billerDTO.billerTypeId' text='Please select Biller Type'/>");
		  return false;
	  }/* else if(bankId==""){
		  alert("<spring:message code='NotNull.billerDTO.bankId' text='Please select bank'/>");
		  return false;
	  } */else if(country==""){
		  alert("<spring:message code='NotNull.billerDTO.countryId' text='Please select Country'/>");
		  return false;
	  }else{
		 //bellow line commented by bidyut as bank selection option is removed
		 //document.getElementById('bankId').disabled = false;
		  document.billerForm.action="saveBillersForm.htm";
		  document.billerForm.submit();
	}
}
  	
function disableSelect(){
  	document.getElementById('bankId').disabled = true;
}
function cancelForm(){
document.billerForm.action="searchBiller.htm";
document.billerForm.submit();
}
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>

</head>
<body>
<form:form name="billerForm" id="billerForm" action="saveBiller.htm" method="post" commandName="billerDTO">
<jsp:include page="csrf_token.jsp"/>
<form:hidden path="countryId" value="1" id="countryId" />
<div class="col-md-12">
	<div class="box" style="height:470px;">
		<div class="box-header">
			<h3 class="box-title">
				<span><spring:message code="LABEL_BILLER" text="Biller" /></span>
			</h3>
		</div>
		<div class="col-md-5 col-md-offset-5" style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message code="${message}" text="" /></div>
		<!-- /.box-header -->
		<div class="box-body">
		<div class="col-sm-6 col-md-offset-3" style="border: 1px solid;border-radius: 15px;"><br />
			<!-- form start -->
			<form class="form-inline" role="form">
			<div class="row">
			<form:hidden path="billerId" />
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_BILLERNAME" text="Biller Name" /><font color="red">*</font></label>
					<div class="col-md-6"> 
					<form:input path="billerName" id="billerName" cssClass="form-control" maxlength="100"/><FONT color="red">
					<form:errors path="billerName" /></FONT>
					</div>
				</div>
			</div><br/>
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_BILLERTYPE" text="Biller Type" /><font color="red">*</font></label>
					<div class="col-md-6"> 
				   <form:select path="billerTypeId" class="dropdown chosen-select" id="billerType">
						<form:option value="" selected="selected">
							<spring:message code="LABEL_WUSER_SELECT"
								text="--Please Select--"></spring:message>
							<form:options items="${billerTypeList}"
								itemValue="billerTypeId" itemLabel="billerType" />
						</form:option>
					</form:select> 
					<font color="RED"><form:errors path="billerTypeId" cssClass="" /></font>
					</div>
				</div>
			</div><br/>
			<div class="row">
				<%-- 
				commented by bidyut as the bank selection should belong to default bank
				
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_BANK" text="Bank" /><font color="red">*</font></label> 
					<div class="col-md-6"> 
					<form:select path="bankId" class="dropdown chosen-select" id="bankId">
					<form:option value="" selected="selected">
						<spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message>
					</form:option>
					<form:options items="${bankList}" itemValue="bankId" itemLabel="bankName"></form:options>
				</form:select> 
				<font color="RED"><form:errors path="bankId" cssClass="" /></font>
				</div>
				</div> --%>
			</div><br/>
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_COUNTRY" text="Country:" />
					<!-- <font color="red">*</font> --></label> 
					<div class="col-md-6"> 
					<label class="col-sm-10"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="South Sudan" /></label>
									
				<%-- 	commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018				
					<select class="dropdown chosen-select" id="countryId" name="countryId">
					<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" /></option>
						<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${countryList}" var="country">
									<c:forEach items="${country.countryNames}" var="cNames">
												<c:if test="${cNames.comp_id.languageCode==lang }">
												 <option value="<c:out value="${country.countryId}"/>"  <c:if test="${country.countryId eq billerDTO.countryId}" >selected=true</c:if> > 
												<c:out 	value="${cNames.countryName}"/>	
												</option>
												</c:if>										
									</c:forEach>
									</c:forEach>
									
				</select> <font color="red"><form:errors path="countryId" /></font> --%>
				</div>
				</div>
			</div><br/>
			<div class="row">
				<div class="col-md-12">
					<label class="col-sm-5"><spring:message code="LABEL_PARTIAL_PAYMENTS"  text="PARTIALPAYMENTS" /></label>
					<div class="col-md-6">
					<form:radiobutton path="partialPayments" value="1" ></form:radiobutton><spring:message code="LABEL_YES" text="Yes" /> &nbsp; &nbsp;
					<form:radiobutton path="partialPayments" value="0"></form:radiobutton><spring:message code="LABEL_NO" text="No" /> 
				</div>
				</div>
			</div>
				<!-- /.box-body -->
									<c:choose>
										<c:when test="${(billerDTO.billerId eq null) }">
											<c:set var="buttonName" value="LABEL_ADD" scope="page" />
										</c:when>
										<c:otherwise>
										<script>disableSelect();</script>
											<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
										</c:otherwise>
									</c:choose>
				<div class="col-md-6 col-md-offset-8">
				<input type="button" class="btn btn-primary" id="submitButton" value="<spring:message code="${ buttonName }" text="${ buttonName }"/>" onclick="validate();" /> 
				<div class="space"></div>
				<input type="button" class="btn btn-primary" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
				</div>
			</form>
			</div>
		</div>
	</div>
	<!-- /.box -->
</div>
</form:form>
</body>
</html>
