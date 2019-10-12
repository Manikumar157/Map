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

<%-- <link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/css/calendar-system.css" />
<link href="../../css/style.css" rel="stylesheet" type="text/css" />--%>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script> 
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
-->
</style>

<script type="text/javascript">

function serviceChargeRulesExcel(){

	submitlink("exportToXlsForSCR.htm","searchSCForm"); 
	 for(var i=0;i<150000;i++);{
	 document.body.style.cursor = 'default';
	 canSubmit = true; 
	 $.unblockUI();
	 }
}
function serviceChargeRulesPDF(){
	 
	 submitlink("exportToPdfForSCR.htm","searchSCForm"); 
	 for(var i=0;i<150000;i++);{
	 document.body.style.cursor = '';
	 canSubmit = true; 
	 }
}

$(document).ready(function(){

	  applyChosen();

});
//@start Vinod joshi  Date:16/10/2018 purpose:cross site Scripting -->
 function serviceDetail(url,serviceChargeRuleId,ruleLevel){
	document.getElementById('roleId').value=serviceChargeRuleId;
	document.getElementById('ruleLevel').value=ruleLevel;
	submitlink(url,'searchSCForm');
} 

function addRule(){
	
	var ruleLevel = document.getElementsByName("ruleLevel");
	var bankId = document.searchSCForm.bankId.value ;
	
	var selectedRule = 1 ;
	
	if(ruleLevel[0].checked){
		selectedRule = 1;
	}if(ruleLevel[1].checked){
		selectedRule = 2;
	}
	
	document.searchSCForm.action="showServiceChargeRuleForm.htm";
    document.searchSCForm.submit();
	
}

$(document).ready(function() {
	
	var ruleLevel = document.getElementById("ruleLevel") ;
	
	if( ruleLevel.value == 1 || ruleLevel.value == 4 ){
		$("#bankgroups").hide();
		$("#banks").hide();
		$("#profiles").hide();
	}else if( ruleLevel.value == 2 ){
		$("#bankgroups").show();
		$("#banks").hide();
		$("#profiles").hide();
	}else if( ruleLevel.value == 3 ){
		$("#bankgroups").hide();
		$("#banks").show();
		$("#profiles").show();
		applyChosen();
	}
	
	$("#ruleLevels").change(function() {
		
		var ruleLevel = document.getElementById("ruleLevel") ;
		//alert(ruleLevel.value);
		if( ruleLevel.value == 1 || ruleLevel.value == 4 ){
			$("#bankgroups").hide();
			$("#banks").hide();
			$("#profiles").hide();
		}else if( ruleLevel.value == 2 ){
			$("#bankgroups").show();
			$("#banks").hide();
			$("#profiles").hide();
		}else if( ruleLevel.value == 3 ){
			$("#bankgroups").hide();
			$("#banks").show();
			$("#profiles").show();
			applyChosen();
		}
		
	});
	
	
	


$(document).change(function() {
	$("#bankGroupName").change(function() {		
		$bankGroupName = document.getElementById("bankGroupName").value;
		if($bankGroupName.length<1)
			$bankGroupName=0;
		
		$csrfToken = $("#csrfToken").val();
		
		$.post("getBanks.htm", {
			bankGroupId : $bankGroupName,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("bank").innerHTML="";
			document.getElementById("bank").innerHTML = data;
			applyChosen();
			/* <!--  Author name <vinod joshi>, Date<7/5/2018>, purpose of change < Token problem, logging out page> ,
			//Start--> */
			setTokenValFrmAjaxResp();
			/* End */
		});
	});
});
	$("#bank").change(function() {
		document.getElementById("bankId").value= document.getElementById("bankName").value;;
		$bankName = document.getElementById("bankName").value;
		if($bankName.length<1)
			$bankName=0;
		$csrfToken = $("#csrfToken").val();
		
		$.post("getCustomerProfiles.htm", {
			bankId : $bankName,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("profiles").innerHTML="";
			document.getElementById("profiles").innerHTML = data;
			applyChosen();
			/* <!--  Author name <vinod joshi>, Date<7/5/2018>, purpose of change < Token problem, logging out page> ,
			//Start--> */
			setTokenValFrmAjaxResp();
			/* End */
		});
	});

	$("#profiles").change(function() {
		document.getElementById("profileId").value = document.getElementById("profileName").value ;
		document.getElementById("bankId").value = document.getElementById("bankName").value ;
		applyChosen();
	});
				


	$("#bankName").change(function() {
		var bankName = document.getElementById("bankName").value;
		document.getElementById("bankId").value= bankName;
		$bankName = bankName;
		if($bankName.length<1)
			$bankName=0;
		$csrfToken = $("#csrfToken").val();
		$.post("getCustomerProfiles.htm", {
			bankId : $bankName,
			csrfToken : $csrfToken
		}, function(data) {
			document.getElementById("profiles").innerHTML="";
			document.getElementById("profiles").innerHTML = data;
			document.getElementById("profileId").value = "";
			applyChosen();
			setTokenValFrmAjaxResp();
		});
	});

	$("#profiles").change(function() {
		document.getElementById("profileId").value = document.getElementById("profileName").value ;
		document.getElementById("bankId").value = document.getElementById("bankName").value ;
		
		/* //applyChosen(); */
	});
				
});

function showForm(url,ruleLevel){
	document.getElementById('ruleLevel1').value=ruleLevel;
	submitlink(url,'searchSCForm');
}

$(document).ready(function() {	
	$("#profileName").change(function() {
		document.getElementById("profileId").value = document.getElementById("profileName").value ;

	});
	
		$("#bankName").change(function() {
			document.getElementById("bankId").value = document.getElementById("bankName").value ;
		});
	
});


		function validate(){
			
			var ruleLevel = document.getElementById("ruleLevel").value;
			var bankGroup=document.getElementById("bankGroupName").value;
			var bankName = document.getElementById("bankName").value;
			var profileName = document.getElementById("profileId").value;
			//alert("pf : "+profileName)
			 if(ruleLevel==2 && bankGroup ==""){
				 alert('<spring:message code="VALID_EMPTY_BANK_GROUP" text="Please select the Bank Group"/>' );
			     return false;
			 }else if(ruleLevel==3 && bankName ==""){
				  alert('<spring:message code="VALID_EMPTY_BANK_NAME" text="Please select the Bank Name"/>');
			     return false;
			 }else if(ruleLevel==3 && profileName ==""){
				 alert('<spring:message code="VALID_EMPTY_PROFILE" text="Please select the Profile" />');
			     return false;
			 }else{
				 document.searchSCForm.action="searchServiceChargeRules.htm";
				 document.searchSCForm.submit();
			 }  
			
		}
		
		
</script>
</head>
<body>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><spring:message code="LABEL_SERVICE_CHARGE_RULES" text="Service Charge Rules"></spring:message></span>
		</h3>
	</div><br/>
	<form:form name="searchSCForm" id="searchSCForm" class="form-inline"  method="post" commandName="scSearchDTO">  
 		<jsp:include page="csrf_token.jsp"/>
 		<form:hidden path="pageNumber" value="1"/>
 		<form:hidden path="bankId"/> 
 		<form:hidden path="profileId"/>
 		<form:hidden path="roleId"/>
 		<input id="ruleLevel1" type="hidden" name="ruleLevel1" value=""/>
 		<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankadmin">	
 		<authz:authorize ifAnyGranted="ROLE_addServiceChargeRulesAdminActivityAdmin">	
			<div class="col-md-3 col-md-offset-10">
				<%-- <a href="javascript:submitForm('searchSCForm','showServiceChargeRuleForm.htm?ruleLevel=1')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule">
				<a href="javascript:showForm('showServiceChargeRuleForm.htm','1')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule">
				</spring:message></strong></a> --%>
				
				 <input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:showForm('showServiceChargeRuleForm.htm','1')" />
			</div>
			</authz:authorize>
		</authz:authorize>
		<authz:authorize ifAnyGranted="ROLE_bankgroupadmin">
			<div class="col-md-3 col-md-offset-10">
				<%-- <a href="javascript:showForm('searchSCForm','showServiceChargeRuleForm.htm?ruleLevel=2')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule"> 
				<a href="javascript:showForm('showServiceChargeRuleForm.htm','2')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule">
				</spring:message></strong></a>--%>
				
				<input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:showForm('showServiceChargeRuleForm.htm','2')" />
				
			</div>
		</authz:authorize>
		<authz:authorize ifAnyGranted="ROLE_parameter">
		<div class="col-md-3 col-md-offset-10">
			<%-- <a href="javascript:submitForm('searchSCForm','showServiceChargeRuleForm.htm?ruleLevel=3')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule">
			<a href="javascript:showForm('showServiceChargeRuleForm.htm','3')" ><strong><spring:message code="LABEL_ADD_RULE" text="Add Rule">
			</spring:message></strong></a> --%>
			
			<input type="button" class="btn btn-primary"
							value="<spring:message code="LABEL_ADD_RULE" text="Add Clearing House"/>"
					onclick="javascript:showForm('showServiceChargeRuleForm.htm','3')" />
		</div>
		</authz:authorize>
	<div class="box-body">
			<div class="row">
				<div class="col-sm-4" id="ruleLevels">
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_RULE_TYPE" text="Rule Type"/></label> 
					<form:select path="ruleLevel" class="dropdown chosen-select">
     						<form:option value="1"><spring:message code="LABEL_GLOBAL" text="Global"/></form:option>
	      					<form:option value="4" ><spring:message code="LABEL_INTER_BANK" text="Inter Bank"/></form:option>
	      					<c:if test="${bankGroupList!=null}"><form:option value="2" ><spring:message code="LABEL_BANK_GROUPS" text="Bank Groups"/></form:option> </c:if>
	      					<form:option value="3" ><spring:message code="LABEL_PROFILES" text="Profiles"/></form:option>
     				</form:select>
				</div>
			<%-- <div  class="col-sm-4" id="bankgroups">
				
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANKGROUP" text="Bank Group"/></label>
        			<form:select path="bankGroupId" id="bankGroupName" class="dropdown chosen-select">
							<option value=""><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
							<form:options items="${bankGroupList}"	itemValue="bankGroupId" itemLabel="bankGroupName"></form:options>
					</form:select>
				
			</div>
			<div  class="col-sm-4" id="banks">
				
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_BANK" text="Bank"/></label>
        			<form:select path="bankId"	id="bankName" cssClass="dropdown chosen-select">
   							<option value=""><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
							<form:options items="${bankList}" itemValue="bankId" itemLabel="bankName"></form:options>
					</form:select>
				
			</div> --%>
			<div  class="col-sm-4" id="profiles">
				
					<label class="col-sm-5" style="margin-top:4px;"><spring:message code="LABEL_PROFILES" text="Profiles"></spring:message></label>
								    					<form:select path="profileId" id="profileName" cssClass="dropdown col-sm-5 chosen-select">
   							<option value=""><spring:message code="LABEL_WUSER_SELECT" text="--Please Select--"></spring:message></option>
							<form:options items="${profileList}" itemValue="profileId" itemLabel="profileName"></form:options>
					</form:select>
				
			</div>
			</div>
			<div class="box-footer" style="padding-bottom:20px;">
				<input type="button" class="btn btn-primary pull-right" id="submitButton" value="<spring:message code="LABEL_SEARCH" text="Submit"/>" onclick="validate();" style="margin-right: 60px;"/>
				
			</div>
			</div>
</form:form>
</div>
			<div class="box">
				<div class="box-body table-responsive">
				
					<div class="exprt" style="margin-top: 0px; margin-bottom: 4px; height: 30px;">
	                	<%-- <span><spring:message code="LABEL_EXPORT_AS" text="Export as"/> </span> --%>
						<span style="float:right; margin-right: 5px;">
							<a href="#" onclick="javascript:serviceChargeRulesExcel();" style="text-decoration:none;margin-left:-16px;"><img src="<%=request.getContextPath()%>/images/excel.jpg" />
								<%-- <span><spring:message code="LABEL_EXCEL" text="Excel"/></span> --%>
							</a>
						</span>
						
						<span style="margin-right: 30px; float:right">
							<a href="#" style="text-decoration:none;margin-left: 9px;" onclick="javascript:serviceChargeRulesPDF();"><img src="<%=request.getContextPath()%>/images/pdf.jpg" />
								<%-- <span><spring:message code="LABEL_PDF" text="PDF"/></span> --%>
							</a>
						</span>					
   			  	 </div>	
				
					<table id="example1" class="table table-bordered table-striped" style="text-align:center;">
						<thead>
							<tr>
								<th><spring:message code="LABEL_RULE_LEVEL" text="Rule Level" /></th>
								<th><spring:message code="LABEL_RULE_NAME" text="ruleName:" /></th>
								<th><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /></th>
								<th><spring:message code="LABEL_APPLICABLE_FROM" text="Applicable From:" /></th>
								<th><spring:message code="LABEL_APPLICABLE_TO" text="Applicable To:" /></th>
								<th><spring:message code="LABEL_TIME_ZONE" text="Time Zone:" /></th>
								<th><spring:message code="LABEL_IMPOSED_ON" text="Imposed On:" /></th>
								<th><spring:message code="LABEL_ACTION" text="action:"/></th>
							</tr>
						</thead>
						<tbody>
						<c:set var="j" value="${(page.currentPage - 1) * page.resultsPerPage }"></c:set>
						<c:forEach items="${page.results}" var="scrules">
							<c:set var="j" value="${ j+1 }"></c:set>
							<tr>
							<%-- <c:if test="${ j%2 == 0 }"> bgcolor="#d2d3f1" </c:if> --%>
								<td><c:if test="${ scrules.ruleLevel == 1  }"><spring:message code="LABEL_GLOBAL" text="Global"/></c:if>
									<c:if test="${ scrules.ruleLevel == 2  }"><spring:message code="LABEL_BANKGROUP" text="Bank Group"/></c:if>
									<c:if test="${ scrules.ruleLevel == 3  }"><spring:message code="LABEL_CUSTOMER_PROFILE" text="Customer Profile"/></c:if>
									<c:if test="${ scrules.ruleLevel == 4  }"><spring:message code="LABEL_INTER_BANK" text="Inter Bank"/></c:if>
								</td>
								<td><c:out value="${scrules.serviceChargeRuleName}" /></td>
								<td>
								<c:set var="lang" value="${language}"></c:set>
									<c:forEach items="${ scrules.serviceChargeRuleTxns }" var="txnType">
									<c:forEach items="${txnType.transactionType.transactionTypesDescs}" var="transactionTypesDescs">
									 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
											<c:out 	value="${transactionTypesDescs.description}"/>	-
									</c:if>		
										</c:forEach>
										<c:if test="${txnType.sourceType==1}">
					                     <c:set var="source"><spring:message code="LABEL_SOURCE_WALLET" text="Wallet" /></c:set>
				                        </c:if>
				                        <c:if test="${txnType.sourceType==2}">
					                     <c:set var="source"><spring:message code="LABEL_SOURCE_CARD" text="Card" /></c:set>
				                        </c:if>
				                         <c:if test="${txnType.sourceType==3}">
					                     <c:set var="source"><spring:message code="LABEL_SOURCE_BANKACC" text="Bank Account" /></c:set>
				                        </c:if>
				                         <c:if test="${txnType.sourceType==4}">
					                     <c:set var="source"><spring:message code="LABEL_SOURCE_FI" text="FI" /></c:set>
				                        </c:if>
				                         <c:if test="${txnType.sourceType==0}">
					                     <c:set var="source"><spring:message code="LABEL_SOURCE_OTHERS" text="Others" /></c:set>
				                        </c:if>
				                        <c:out value="${source}" /><br/>										
										
									</c:forEach>
								</td>
								<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${scrules.applicableFrom}" /></td>
								<td><fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${scrules.applicableTo}" /></td>
								<td><c:out value="${scrules.timeZone.timeZoneDesc}" /></td>
								<td>
								<c:choose>
									<c:when test="${scrules.imposedOn==0}"><spring:message code="LABEL_CUSTOMER" text="Customer"/></c:when>
									<c:otherwise><spring:message code="LABEL_SELECT_BANK_OTHER_PARTY" text="Other Party"/></c:otherwise>
								</c:choose>
								</td>
								<authz:authorize ifAnyGranted="ROLE_admin">
						        	<c:choose>
								        	<c:when test="${ scrules.ruleLevel==1 || scrules.ruleLevel==4}">
					        			<authz:authorize ifAnyGranted="ROLE_editServiceChargeRulesAdminActivityAdmin">	
											<%-- <td><a href="javascript:submitForm('searchSCForm','editServiceChargeRule.htm?scRuleId=${scrules.serviceChargeRuleId}&ruleLevel=${scrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td> --%> 
							 					<td><a href="javascript:serviceDetail('editServiceChargeRule.htm','<c:out value="${scrules.serviceChargeRuleId}"/>','<c:out value="${scrules.ruleLevel}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a></td>
							 			</authz:authorize>
										 	</c:when>
										 	
										 	<c:otherwise>
							 			<authz:authorize ifNotGranted="ROLE_editServiceChargeRulesAdminActivityAdmin">
										 <%-- <td><a href="javascript:submitForm('searchSCForm','viewServiceChargeRule.htm?scRuleId=<c:out value="${scrules.serviceChargeRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td>  --%>
									 	 <td><a href="javascript:serviceDetail('editServiceChargeRule.htm','<c:out value="${scrules.serviceChargeRuleId}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a></td> 
									 	</authz:authorize>
										 	</c:otherwise>
								 	</c:choose>
							 	</authz:authorize>
							 	
							 	<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_bankgroupadmin">
							 	<authz:authorize ifAnyGranted="ROLE_editServiceChargeRulesAdminActivityAdmin">	
						        	<c:choose>
							        	<c:when test="${ scrules.ruleLevel==2}">
											<%-- <td><a href="javascript:submitForm('searchSCForm','editServiceChargeRule.htm?scRuleId=${scrules.serviceChargeRuleId}&ruleLevel=${scrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td> --%>	
									 		<td><a href="javascript:serviceDetail('editServiceChargeRule.htm','<c:out value="${scrules.serviceChargeRuleId}"/>','<c:out value="${scrules.ruleLevel}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a></td> 
									 	</c:when>
									 	<c:otherwise>
									 	<%-- <td><a href="javascript:submitForm('searchSCForm','viewServiceChargeRule.htm?scRuleId=<c:out value="${scrules.serviceChargeRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td> --%>
									 	<td><a href="javascript:serviceDetail('editServiceChargeRule.htm','<c:out value="${scrules.serviceChargeRuleId}"/>','<c:out value="${scrules.ruleLevel}"/>')"><spring:message code="LABEL_VIEW" text="View" /></a></td> 
									 	</c:otherwise>
								 	</c:choose>
							 	</authz:authorize>
							 	</authz:authorize>
							 	
							 	<authz:authorize ifAnyGranted="ROLE_parameter">
							 	<authz:authorize ifAnyGranted="ROLE_editServiceChargeRulesAdminActivityAdmin">	
						        	<c:choose>
							        	<c:when test="${ scrules.ruleLevel==3}">
											<td><a href="javascript:submitForm('searchSCForm','editServiceChargeRule.htm?scRuleId=${scrules.serviceChargeRuleId}&ruleLevel=${scrules.ruleLevel}')"/><spring:message code="LABEL_EDIT" text="Edit:"/></td>	
									 	</c:when>
									 	<c:otherwise>
									 	<td><a href="javascript:submitForm('searchSCForm','viewServiceChargeRule.htm?scRuleId=<c:out value="${scrules.serviceChargeRuleId}"/>')"/><spring:message code="LABEL_VIEW" text="View"/></td>
									 	</c:otherwise>
								 	</c:choose>
							 	</authz:authorize>
							 	</authz:authorize>
							 	
							 		
							 	
							 	<authz:authorize ifNotGranted="ROLE_parameter,ROLE_bankadmin,ROLE_bankgroupadmin,ROLE_admin">
					 	    <td>
						 		<spring:message code="LABEL_TXN_AMT_NA" text="N/A"/>
			 			   </td>
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