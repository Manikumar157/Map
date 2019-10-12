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
	function validate() {
		
		var charPattern = /^[A-Za-z]+$/;
		var pattern = '^\[a-zA-ZÀ-ÿ-\'+ 0-9 ]*$';
		var whitespaceChars = " \t\n\r\f";
		var numPattern = '^[0-9 ]*$';
		//var zeroPattern=/^[1-9]{1}[0-9]{0,2}(\.\d{1,2})?$/;
		var numericPattern=  /^\d{0,3}(\.\d{1,2})?$/gm;
		var opname = document.getElementById("operatorName").value;
		var country = document.getElementById("countryId").value;
		var comission = document.getElementById("comission").value;
		//var bankId = document.getElementById("bankId").value;
		document.operatorForm.countryId.value = country;
		
		
		
		if (opname == "") {
			alert("<spring:message code='NotEmpty.operatorDTO.operatorName' text='Please enter Operator Name'/>");
			document.operatorForm.operatorName.focus();
			return false;
		}
		if(opname.charAt(0).match(/^[0-9]*$/)){
			 alert("<spring:message code='LABEL.OPERATORNAME.FIRSTLETTER' text='Please enter first letter should be alphabet'/>");
				return false;
		 }
		
		 
		 		
	     if (opname.charAt(0) == " "
				|| opname.charAt(opname.length - 1) == " ") {
			alert("<spring:message code='LABEL.OPERATORNAME.SPACE' text='Please remove unwanted spaces in Operator Name'/>");
			return false;
		}  if (opname.search(pattern) == -1) {
			alert("<spring:message code='LABEL.OPERATORNAME.DIGITS' text='Please enter Operator Name with out any special characters'/>");
			return false;
		}  if (opname.length > 50) {
			alert("<spring:message code='LABEL.OPERATORNAME.LENGTH' text='Operator Name should not exceed more than 100 characters'/>");
			return false;
		} if (country == "") {
			alert("<spring:message code='NotEmpty.operatorDTO.countryId' text='Please select Country Name'/>");
			return false;
		} /*  if (bankId == "") {
			alert("<spring:message code='NotNull.operatorDTO.bankId' text='Please select Bank Name'/>");
			return false;
		}  */
		if(comission == ""){
			alert("<spring:message code='LABEL.COMISSION' text='Please enter a valid Commission'/>");
			return false;
		}
		if(comission.charAt(0) == " " || comission.charAt(comission.length-1) == " "){
	         alert("<spring:message code='LABEL.COMISSION.SPACE' text='Please Remove unwanted  Spaces in Commission'/>");
	         document.operatorForm.address.focus();
	         return false;
	         }
	
		/* if (comission.search(numPattern) == -1) {
			alert("<spring:message code='NotEmpty.operatorDTO.comission' text='Please enter Comission only in digits '/>");
			return false;
		}  */ if (comission > 100) {
			alert("<spring:message code='VALID_OPERATOR_COMISSION_LIMIT' text='Please enter Commission value not more than 100'/>");
			return false;
		} 
		if(!numericPattern.test( comission )){
	   		
	   		 alert("<spring:message code='LABEL.COMISSION' text='Please enter a valid Commission'/>");
	   		  return false;
	   		  }
		if((comission.charAt(0)== '.') ){
			alert("<spring:message code='LABEL.COMISSION' text='Please enter a valid Commission'/>");
		  return false;
	  }
			
		 if(comission!='0' && comission.charAt(0)== '0'){
			 alert("<spring:message code='LABEL.COMISSION' text='Please enter a valid Commission'/>");
	   		  return false;
	   		  }
		 
		 var a = 0, rdbtn=document.getElementsByName("active")
			for(var i=0;i<rdbtn.length;i++) {
			if(rdbtn.item(i).checked == false) {
			a++;
			}
			}
			if(a == rdbtn.length) {
			alert("Please enter Status");
			return false;
			} 
		
			//commented by bidyut as bank selection should be default in login
			//document.getElementById('bankId').disabled = false;
			document.operatorForm.action = "saveOperator.htm";
			document.operatorForm.submit();
		}

	
	function cancelForm() {
		document.operatorForm.action = "showOperators.htm";
		document.operatorForm.submit();
	}

	function disableSelect() {
		//document.getElementById('bankId').disabled = true;
	}
	//@start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting 
	function viewDetail(url,pageNumber,operatorId){
		document.getElementById('pageNumber').value=pageNumber;
		document.getElementById('operatorId').value=operatorId;
		submitlink(url,'operatorForm');
	}
	//@end
	
</script>

<%-- <title><spring:message code="LABEL_TITLE" text="GIM MOBILE" /></title> --%>
</head>
<body>
	<form:form name="operatorForm" id="operatorForm" action="saveOperator.htm" method="post"
		commandName="operatorDTO">
		<jsp:include page="csrf_token.jsp"/>
		<div class="col-md-12">
			<div class="box">
				<div class="box-header">
					<h3 class="box-title">
						<span><spring:message code="TITLE_OPERATOR"
								text="Add Operator" /></span>
					</h3>
				</div>
				<br />
				<div class="col-md-4 col-md-offset-5">
					<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
						<spring:message code="${message}" text="" />
					</div>
				</div>
				<!-- /.box-header -->
				<div class="box-body" style="height:470px;">
				<div class="col-sm-6 col-md-offset-3 table_border" style="/* border: 1px solid;border-radius: 15px; */"><br />
					<!-- form start -->
					<div class="col-md-10 col-md-offset-5">
						<c:if test="${operatorDTO.operatorId ne null}">
							<c:if test="${operatorDTO.active ne 0}">
							<!-- @start Abu kalam Azad  Date:27/07/2018 purpose:cross site Scripting --> 
								<a
									href="javascript:viewDetail('viewDenominations.htm','1','${operatorDTO.operatorId}')"><b><spring:message
										code="TITLE_OPERATORDENOMIN" text="Add Denomination" /></b></a> 
										<input type="hidden" name="pageNumber" id="pageNumber" value=""/>
										| 
                                          <%-- <a
									href="javascript:viewDetail('uploadVoucherForm.htm?pageNumber=1','${operatorDTO.operatorId}')"><b><spring:message
											code="TITLE_VIEWVOUCHERS" text="View Vouchers" /></b></a> --%>
									<!-- commented by biyut as there is no option for in m-GURUSH -->		
									
									<a
									href="javascript:viewDetail('uploadVoucherForm.htm','1','${operatorDTO.operatorId}')"><b><spring:message
											code="TITLE_VIEWVOUCHERS" text="View Vouchers" /></b></a>
											
									<%-- | <a
									href="javascript:viewDetail('addOperatorCard.htm','${operatorDTO.operatorId}')"><b><spring:message
											code="LABEL_ADD_CARD" text="Add Card" /></b></a> --%>
											<%-- 
										| 	<a
									href="javascript:submitForm('operatorForm','viewDenominations.htm?pageNumber=1&operatorId=${operatorDTO.operatorId}')"><b><spring:message
										code="TITLE_OPERATORDENOMIN" text="Add Denomination" /></b></a> 
                                          <a
									href="javascript:submitForm('operatorForm','uploadVoucherForm.htm?pageNumber=1&operatorId=${operatorDTO.operatorId}')"><b><spring:message
											code="TITLE_VIEWVOUCHERS" text="View Vouchers" /></b></a> 
								|	<a
									href="javascript:submitForm('operatorForm','addOperatorCard.htm?operatorId=${operatorDTO.operatorId}')"><b><spring:message
											code="LABEL_ADD_CARD" text="Add Card" /></b></a> --%>
											<!-- @End -->
											
							</c:if>
						</c:if>
					</div>
					<form class="form-inline" role="form">
						<div class="row">
						<form:hidden path="countryId" value="1" id="countryId" />
							<div class="col-md-12">
								<form:hidden path="operatorId" />
								<label class="col-sm-4"><spring:message
										code="LABEL_OPERATORNAME" text="Operator Name" /><font
									color="red">*</font></label>
								<div class="col-md-5">
								<form:input path="operatorName" id="operatorName"
									cssClass="form-control" maxlength="30" />
								<FONT color="red"> <form:errors path="operatorName" /></FONT>
								</div>
							</div>
						</div>
						<br>
							<div class="row">
								<div class="col-md-12">
									<label class="col-sm-4"><spring:message
											code="LABEL_COUNTRY" text="Country:" /> <!-- <font color="red">*</font> -->
											</label>
									<div class="col-md-5">
								<label class="col-sm-10"><spring:message
									code="LABEL_COUNTRY_SOUTH_SUDAN" text="South Sudan" /></label>
								
								<%--   	commenting to make a default country as SouthSudan,
									by vineeth on 13-11-2018
									<select class="dropdown chosen-select" id="countryId" name="countryId">
										<option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--" />
											<c:set var="lang" value="${language}"></c:set>
											<c:forEach items="${countryList}" var="country">
												<c:forEach items="${country.countryNames}" var="cNames">
													<c:if test="${cNames.comp_id.languageCode==lang }">
														<option value="<c:out value="${country.countryId}"/>"
															<c:if test="${country.countryId eq operatorDTO.countryId}" >selected=true</c:if>>
															<c:out value="${cNames.countryName}" />
														</option>
													</c:if>
												</c:forEach>
											</c:forEach>
										</option>
									</select> <font color="red"> <form:errors path="countryId" /></font> --%>
								</div>
								</div>
							</div> <br />
						<%--  <div class="row">
							<div class="col-md-12">
								<label class="col-sm-4"><spring:message
										code="LABEL_BANK" text="Bank" /><font color="red">*</font></label>
								<div class="col-md-5">
								<form:select path="bankId" class="dropdown chosen-select" id="bankId">
									<form:option value="" selected="selected">
										<spring:message code="LABEL_WUSER_SELECT"
											text="--Please Select--"></spring:message>
									</form:option>
									<form:options items="${bankList}" itemValue="bankId"
										itemLabel="bankName"></form:options>
								</form:select>
								<font color="RED"> <form:errors path="bankId" cssClass="" /></font>
								</div>
							</div>
						</div>
						<br /> --%>
						<div class="row">
							<div class="col-md-12">
								<label class="col-sm-4"><spring:message
										code="LABEL_COMMISSION" text="Commission(%)" /><font
									color="red">*</font></label>
								<div class="col-md-5">
								<form:input path="comission" id="comission"
									cssClass="form-control" maxlength="6" />
								</div>
							</div>
						</div>
						<br />
						<div class="row">
							<div class="col-md-12">
								<label class="col-sm-4"><spring:message
										code="LABEL_STATUS" text="Status" /><font color="red">*</font></label>
								<div class="col-md-5">
								<form:radiobutton path="active" value="1"  />
								<spring:message code="LABEL_ACTIVE" text="Active" />
								&nbsp; &nbsp;
								<form:radiobutton path="active" value="0" />
								<spring:message code="LABEL_DEACTIVATE" text="Inactive" />
								<FONT color="red"> <form:errors path="active" /></FONT>
								</div>
							</div>
						</div>
						<!-- /.box-body -->
						<c:choose>
							<c:when test="${(operatorDTO.operatorId eq null) }">
								<c:set var="buttonName" value="LABEL_ADD" scope="page" />
							</c:when>
							<c:otherwise>
								<script>
									disableSelect();
								</script>
								<c:set var="buttonName" value="LABEL_UPDATE" scope="page" />
							</c:otherwise>
						</c:choose>
						<div class="col-md-5 col-md-offset-7">
								<input type="button" class="btn btn-primary"
								id="submitButton"
								value="<spring:message code="${ buttonName }" text="${ buttonName }"/>"
								onclick="validate();" />
								<div class="space"></div>
								<%-- <input type="button" class="btn btn-default"
								value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" --%>
								<input type="button" class="btn btn-primary"
								value="<spring:message code="LABEL_CANCEL" text="Cancel"/>"
								onclick="cancelForm();" />
							</div>
					</form>
				</div>
			</div>
			<!-- /.box -->
		</div>
		</div>
	</form:form>
</body>
</html>
