<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<html>
<head>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/calendar.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/<spring:message code="CALENDAR_LANG"/>.js"></script>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<link href="<%=request.getContextPath()%>/css/style.css"
	rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>

<script type="text/javascript">
	var Alertmsg={
			 "bankId":'<spring:message code="ALERT_BANK_ID" text="Please select Entity"/>',
			 "transactions":'<spring:message code="ALERT_SELECT_TRANSACTION" text="Please select transaction type"/>',
			 "profileId":'<spring:message code="ALERT_SELECT_PROFILE_ID" text="Please select profile Id"/>',
			 "commission":'<spring:message code="ALERT_ADD_COMMISSION_PERCENTAGE" text="Please enter commission percentage"/>',
			 "commissionValue":'<spring:message code="ALERT_ADD_COMMISSION_PERCENTAGE_LIMIT" text="Commission percentage should not exceed maxximum limit of 99.99 "/>',
			 "commissionValueLowLimit":'<spring:message code="ALERT_ADD_COMMISSION_PERCENTAGE_LOW_LIMIT" text="Commission percentage can not be zero "/>',
			 "commissionValidValue":'<spring:message code="ALERT_ADD_COMMISSION_PERCE_INVALID_VALUE" text="Enter a valid commission percentage "/>',

	};

	function UpdateMarchantCommissionForm(){
		var commissionValue = $("#commission").val() ;
		var commissionPattern = '/^[0-9]+(\\.[0-9]+)?$'
		var commission =	$("#commission").val().split(".");
		var decimal = commission[0];
		if(commission[1] != null){
		var fraction = commission[1];
		}
		    if(document.getElementById("bankId").value=="") {
		         alert(Alertmsg.bankId);
		         document.editCommissionForm.bankId.focus();
		         return false;
		        }else if($("#transactionTypeId").val() == null){
		        	 alert(Alertmsg.transactions);
			         document.editCommissionForm.transactions.focus();
			         return false;
		       }
		        else if($("#transactionTypeId").val() == ""){
		        	 alert(Alertmsg.transactions);
			         document.editCommissionForm.transactions.focus();
			         return false;
		       }
		        else if(document.getElementById("profileId").value =="") {
		         alert(Alertmsg.profileId);
		         document.editCommissionForm.profileId.focus();
		         return false;
		        }
			 else if(document.getElementById("profileId").value =="select") {
		         alert(Alertmsg.profileId);
		         document.editCommissionForm.profileId.focus();
		         return false;
		        }
		    
		    
			 else if(document.getElementById("commission").value =="") {
		         alert(Alertmsg.commission);
		         document.editCommissionForm.commission.focus();
		         return false;
		        }
			
		 	 else  if(decimal.length >2){
				    alert(Alertmsg.commissionValue);
			         document.editCommissionForm.commission.focus();
			         return false;
			 } 
		    if(fraction != null){
		        if(fraction.length >2){
				    alert(Alertmsg.commissionValue);
			         document.editCommissionForm.commission.focus();
			         return false;
			  }
			 }
			 else  if(decimal >=100){
				    alert(Alertmsg.commissionValue);
			         document.editCommissionForm.commission.focus();
			         return false;
			 }
			/*  }	 else  if((decimal ==0 || decimal ==00 ) && (fraction ==0 || fraction ==00 ) ){
				    alert(Alertmsg.commissionValueLowLimit);
			         document.editCommissionForm.commission.focus();
			         return false;
			 } */
			 else if  (!commissionValue.match("[0-9]{1,13}(\\.[0-9]*)?")){
				 alert(Alertmsg.commissionValidValue);
		         document.editCommissionForm.commission.focus();
		         return false;
				 
			 }
			/*  else if(commissionValue.search(commissionPattern) == -1){
				 alert(Alertmsg.commissionValidValue);
		         document.editCommissionForm.commission.focus();
		         return false;
		        }
		 */
		
		 document.editCommissionForm.action="updateCommissionSlabs.htm";
		 document.editCommissionForm.submit();
	}
	
	$(document).ready(function() {
		
		$("#bankId").change(function() {
			document.getElementById("bankId").value = document.getElementById("bankId").value ;
			$bankId = document.getElementById("bankId").value;
			$csrfToken = $("#csrfToken").val();
			$.post("getCustomerProfilesForCommision.htm", {
				csrfToken : $csrfToken,
				bankId : $bankId
			}, function(data) {
				document.getElementById("profiles").innerHTML="";
				document.getElementById("profiles").innerHTML = data;
			});
		});
	});
	
</script>
</head>
<body >
	
	<div class="col-lg-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message	code="LABEL_COMMISION" text="Commission" /></span>
				</h3>
			</div>
			
			<div class="col-sm-6 col-sm-offset-4" style="color: #ba0101; font-weight: bold; font-size: 12px;">
					<spring:message code="${message}" text="" />
			</div>
			<form:form name="editCommissionForm" commandName="commissionDTO">
				<jsp:include page="csrf_token.jsp"/>
				<div class="box-body">
					<authz:authorize ifNotGranted="ROLE_bankadmin,ROLE_bankteller,ROLE_superadmin,ROLE_parameter,ROLE_audit,ROLE_accounting">
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;">
									<spring:message code="LABEL_BANKNAME" text="Bank Name" />
								</label>
								<form:select path="bankId" cssClass="dropdown chosen-select">
									<form:option value="">
										<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
									</form:option>
									<form:options items="${masterData.bankList}" itemLabel="bankName" itemValue="bankId" />
							    </form:select>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;">
									 <spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" />
								</label>
								<select class="dropdown chosen-select" id="transactionTypeId" name="transactionTypeId"  class="dropdown"  disabled="disabled" >
									<option value=""> <spring:message 	code="LABEL_SELECT" text="--Please Select--" />
									<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${masterData.transTypeList}" var="transTypeList">
										<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
											<c:if test="${transTypeList.transactionType!=84 }">
												 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
													 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${transTypeList.transactionType eq commissionDTO.transactionTypeId}">selected=true</c:if>>
														<c:out 	value="${transactionTypesDescs.description}"/>	
													</option>
													</c:if>		
												</c:if>  						
											</c:forEach>
										</c:forEach>
									</option>
								</select>
								<form:hidden path="transactionTypeId" value = "${commissionDTO.transactionTypeId}"/>
							</div>
						</div>
					</authz:authorize>
						<div class="row">
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;">
									<spring:message code="LABEL_PROFILE" text="Profile" />
								</label>
								<div id="profiles">
									<form:select path="profileId" cssClass="dropdown chosen-select">
										<form:option value="">
											<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
										</form:option>
										<form:options items="${Profile}" itemLabel="profileName" itemValue="profileId" />
							    	</form:select>
							    </div>
							    <font color="RED"><form:errors path="profileId"></form:errors></font>
							</div>
							<div class="col-sm-6">
								<label class="col-sm-5" style="margin-top: 4px;">
									<spring:message code="LABEL_COMMISSION" text="Commisssion" />
								</label>
								<form:input path="commission" cssClass="form-control" cssStyle="width:153px" maxlength="5"/> 
							</div>
						</div>
						<div class="box-footer">
							<input type="button" value="<spring:message code="LABEL_UPDATE" text="Update"/>" onclick="UpdateMarchantCommissionForm();" class="btn btn-primary pull-right" >
						</div>
						<form:hidden path="commisionId"/>
					<br /> <br />
				</div>
			</form:form>
		</div>
	</div>
	

 <!-- *****************************old design********************************* -->

<%-- <table width="1003" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="top.jsp" /></td>
	</tr>
	<tr>
		<td>
		<table width="1003" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="159" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><jsp:include page="left.jsp" /></td>
					</tr>

				</table>
				</td>
				<td width="844" align="left" valign="top">
				<table width="98%" border="0" height="40px" align="center"
					cellpadding="0" cellspacing="0">
					<tr>
						<td align="left" class="headding" width="25%"
							style="font-size: 15px; font-weight: bold;" valign="top"><spring:message
							code="LABEL_COMMISION" text="Commission" />
						</td>
					</tr>
					<tr height="20px">
						<td align="center" colspan="2">
						<div style="color: #ba0101; font-weight: bold; font-size: 12px;"><spring:message
							code="${message}" text="" /></div> 
						</td>
					</tr>
				</table>
				<form:form name="editCommissionForm" commandName="commissionDTO">
					<table
						style="-moz-border-radius: 5px; -webkit-border-radius: 5px; border: 1px solid #a6a6a7"
						align="center" width="98%" cellpadding="4" cellspacing="4">
						<tr>
						<authz:authorize ifNotGranted="ROLE_bankadmin,ROLE_bankteller,ROLE_superadmin,ROLE_parameter,ROLE_audit,ROLE_accounting">
							<td><spring:message code="LABEL_BANKNAME" text="Bank Name" /></td>
							<td><form:select path="bankId" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${masterData.bankList}"
									itemLabel="bankName" itemValue="bankId" />
							    </form:select></td>
							<td>
							      <spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" />
							</td>
							
							<td>
							
								<select class="dropdown" id="transactionTypeId" name="transactionTypeId"  class="dropdown"  disabled="disabled" >
												<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
												
													<c:set var="lang" value="${language}"></c:set>
													
														<c:forEach items="${masterData.transTypeList}" var="transTypeList">
														<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
														<c:if test="${transTypeList.transactionType!=84 }">
																	 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
																	 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${transTypeList.transactionType eq commissionDTO.transactionTypeId}" >selected=true</c:if>>
																	<c:out 	value="${transactionTypesDescs.description}"/>	
																	</option>
																	</c:if>		
																	</c:if>  						
														</c:forEach>
														</c:forEach>
														</option>
													</select>
													<form:hidden path="transactionTypeId" value = "${commissionDTO.transactionTypeId}"/>
							
							
							
							
							
					                          <select  id="transactionTypeId" name="transactionTypeId" >
																		
																	<c:set var="lang" value="${language}"></c:set>
																	<c:forEach items="${masterData.transTypeList}" var="transTypeList" >
																	
																	<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs" varStatus="cnt">
																	 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
																	<c:set value="f" var="sel"/>
																	<c:forEach items="${serviceChargeDTO.transactions}" var="arrVal"> 
																	 <c:if test="${transTypeList.transactionType eq arrVal}"><c:set value="true" var="sel"/></c:if>
																	</c:forEach>
																	
																	<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin">	
																	 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${sel}"> selected=true</c:if> >
																	<c:out 	value="${transactionTypesDescs.description}"/>	
																	</option>
																	</authz:authorize>
																	
																	<authz:authorize ifNotGranted="ROLE_admin,ROLE_bankgroupadmin">	
																	<c:if test="${transTypeList.transactionType!=84 }">
																	 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${sel}"> selected=true</c:if> >
																	<c:out 	value="${transactionTypesDescs.description}"/>	
																	</option>
																	</c:if>
																	</authz:authorize>
																	</c:if>		  						
																</c:forEach>
																</c:forEach>
													
													         </select>
																		
							</td>
						</authz:authorize>
						
						</tr>
						<tr>
							<td><spring:message code="LABEL_PROFILE" text="Profile" /></td>
							<td>
							<div id="profiles">
							<form:select path="profileId" cssClass="dropdown">
								<form:option value="">
									<spring:message code="LABEL_SELECT" text="--Please Select--"></spring:message>
								</form:option>
								<form:options items="${Profile}"
									itemLabel="profileName" itemValue="profileId" />
							    </form:select>
							    </div>
							    <font color="RED"><form:errors path="profileId"></form:errors></font>
							
							</td>
							</tr>
							<tr>	
							<td><spring:message code="LABEL_COMMISSION" text="Commisssion" /></td>
							<td><form:input path="commission"  cssStyle="width:153px" maxlength="5"/> 
							</td>	
							</tr>
						
						<tr>
							<td align="right" colspan="4"><input type="button"
								value="<spring:message code="LABEL_UPDATE" text="Update"/>"
								onclick="UpdateMarchantCommissionForm();"></input></td>
						</tr>
							</table>
							<form:hidden path="commisionId"/>
							
				</form:form></td>
			</tr>

		</table>

		</td>
	</tr>
	<tr>
		<td><jsp:include page="footer.jsp" /></td>
	</tr>
</table> --%>

</body>

</html>
