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
<!-- Loading Calendar JavaScript files -->
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/transactionRules.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	
	$("#ruleLevels").change(function() {
		var ruleLevel = document.getElementById("ruleLevelCustomerProfile").checked ;
		if( ruleLevel ){
			$("#profiles").show();
		}else{
			$("#profiles").hide();
		}
		
	});
	
});

	var AlertMsg={
		"empty":"<spring:message code='ALERT_SC_ALLFIELD' text='Please Fill all values.'/>",
		"maxCum":"<spring:message code='ALERT_TR_MAXCUMVALUE_LIMIT1' text='Please enter Valid MaxCumValueLimit.'/>",		
		"maxNum":"<spring:message code='ALERT_TR_MAXNUM_TIMES' text='Please give valid Max NumTimes.'/>",
		"allowedPer":"<spring:message code='ALERT_TR_ALLPER' text='Please give valid Allowed Per.'/>",
		"allowedPerUnit":"<spring:message code='ALERT_TR_ALLPERUNIT' text='Please give valid Allowed PerUnit.'/>",
		"edit":"<spring:message code='LABEL_EDIT' text='Edit'/>",
		"delet":"<spring:message code='LABEL_DELETE' text='Delete'/>",
		"maxVal":"<spring:message code='NotNull.transactionRulesDTO.maxValueLimit' text='Please enter Max ValueLimit'/>",
		"maxCumVal":"<spring:message code='ALERT_TR_MAXCUMVALUE' text='Please enter MaxCumValueLimit greater or equal MaxValueLimit.'/>",
		"trRuleExist":"<spring:message code='ERROR_9012' text='Transaction Rule already exist.'/>",
		"deleteAlert":"<spring:message code='ALERT_DELETE_ROW' text='Are you sure want to delete?'/>",
		"cumValexceedweek":"<spring:message code='MAX_CUMVAL_EXCEED_WEEK' text='Max. Cumilative limit should not exceed Weeks Max. Cumilative limit'/>",
		"nooftimeexceedweek":"<spring:message code='MAX_NOOFTIMES_EXCEED_WEEK' text='Max No.of Times should not exceed weeks Max No. of Times'/>",
		"cumValexceedmonth":"<spring:message code='MAX_CUMVAL_EXCEED_MONTH' text='Max. Cumilative limit should not exceed Months Max. Cumilative limit'/>",
		"nooftimeexceedmonth":"<spring:message code='MAX_NOOFTIMES_EXCEED_MONTH' text='Max No.of Times should not exceed Months Max No. of Times'/>",
		"cumValgreaterday":"<spring:message code='MAX_CUMVAL_GREATER_DAY' text='Max. Cumilative limit should be greater than Days Max. Cumilative limit'/>",
		"nooftimegreaterday":"<spring:message code='MAX_NOOFTIMES_GREATER_DAY' text='Max No. of Times should be greater than Days Max No. of Times'/>",
		"cumValgreaterweek":"<spring:message code='MAX_CUMVAL_GREATER_WEEK' text='Max. Cumilative limit should  be greater than Weeks Max. Cumilative limit'/>",
		"nooftimegreaterweek":"<spring:message code='MAX_NOOFTIMES_GREATER_WEEK' text='Max No. of Times should be greater than Weeks Max No .of Times'/>",
		"wrongMaxVal":"<spring:message code='ALERT_TR_MAXVALUE' text='Please enter valid MaxValueLimit.'/>"
							
		};


	function validate() {
		
		var docF = document.transactionRuleForm;
		var maxVal=docF.maxValueLimit.value;
		var appLimit=docF.approvalLimit.value;
	
		if (docF.transactions.value == "") {
			alert("<spring:message code='NotNull.transactionRulesDTO.transactions' text='transaction:'/>");			
			return false;
		}//else if (docF.sourceType[0].checked == false && docF.sourceType[1].checked == false && docF.sourceType[2].checked == false && docF.sourceType[3].checked == false) {
		else if (docF.sourceType.checked == false) {
			alert("<spring:message code='NotNull.transactionRulesDTO.sourceType' text='Please select Source Type'/>");			
			return false;
		}else if(maxVal == "") {
			alert("<spring:message code='NotNull.transactionRulesDTO.maxValueLimit' text='Please enter Max ValueLimit'/>");			
			return false;
		}else if(isNaN(maxVal) || maxVal.indexOf(".")!=-1 || maxVal.charAt(0) == " " || maxVal.charAt(maxVal.length-1) == " "){
			alert(AlertMsg.wrongMaxVal);
			return false;
		}else {
			 <authz:authorize ifAnyGranted="ROLE_admin">
			if(document.getElementById("Global").checked==true){
				
			if(docF.transactions.value == 95 || docF.transactions.value == 99 ) {
				if(appLimit == "") {
					alert("<spring:message code='APPROVAL_LIMIT_EMPTY' text='Please enter approval limit'/>");			
					return false;
				}
				if(isNaN(appLimit) || appLimit.indexOf(".")!=-1 || appLimit.charAt(0) == " " || appLimit.charAt(appLimit.length-1) == " "){
					alert("<spring:message code='APPROVAL_LIMIT_INVALID' text='Please enter valid approval limit'/>");			
					return false;
				}
				if(parseInt(appLimit)>parseInt(maxVal)){
					alert("<spring:message code='APPROVAL_LIMIT_EXCEED' text='Approval limit should be less than max.value limit'/>");			
					return false;
				}
				
			}
			}
			</authz:authorize>
			
			<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_bankgroupadmin">
			
				
			if(docF.transactions.value == 95 || docF.transactions.value == 99 ) {
				if(appLimit == "") {
					alert("<spring:message code='APPROVAL_LIMIT_EMPTY' text='Please enter approval limit'/>");			
					return false;
				}
				if(isNaN(appLimit) || appLimit.indexOf(".")!=-1 || appLimit.charAt(0) == " " || appLimit.charAt(appLimit.length-1) == " "){
					alert("<spring:message code='APPROVAL_LIMIT_INVALID' text='Please enter valid approval limit'/>");			
					return false;
				}
				if(parseInt(appLimit)>parseInt(maxVal)){
					alert("<spring:message code='APPROVAL_LIMIT_EXCEED' text='Approval limit should be less than max.value limit'/>");			
					return false;
				}
			}
			
			</authz:authorize>
			
			writeTxnRuleValues();
			if (cvlimit.length==undefined || cvlimit.length==0) {
				alert("<spring:message code='ALERT_TR_RULE_VALUE' text='trRuleValue:'/>");					
				return false;
			}
			document.getElementById('maxValueLimit').disabled=false
			document.transactionRuleForm.method = "post";
			document.transactionRuleForm.action = "saveTransactionRule.htm";
			document.transactionRuleForm.submit();
		}
	}
	
	function switchRadio(status){
		var txn=document.getElementById("transactions").value;
		
		if(txn==95 || txn == 141){
			<authz:authorize ifAnyGranted="ROLE_admin">
			if(txn==95 && document.getElementById("Global").checked==true){
			 if(document.getElementById("Global").checked==true){
			
				 //document.getElementById("approvalLimit").value="";
			 	document.getElementById("appLimit").style.display="block";
			 }
			}
			 	</authz:authorize>
			 	
			<authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_bankgroupadmin,ROLE_parameter">
			 //document.getElementById("approvalLimit").value="";
			document.getElementById("appLimit").style.display="block"; 
			</authz:authorize>
			document.getElementById("credit").checked=true;
			document.getElementById("credit").disabled=false;
			document.getElementById("debit").disabled=true;
		}else if(txn==95 &&  document.getElementById("InterBank").checked==true){
			
			<authz:authorize ifAnyGranted="ROLE_admin">
			
				 document.getElementById("approvalLimit").value="";
			 	document.getElementById("appLimit").style.display="none";
			</authz:authorize>
			document.getElementById("credit").checked=true;
			document.getElementById("credit").disabled=false;
			document.getElementById("debit").disabled=true;
			
			/* if(status==0)
			 document.getElementById("approvalLimit").value="";
			document.getElementById("appLimit").style.display="block";  */
			
		}else if(txn==""){
			document.getElementById("debit").checked=true;
			document.getElementById("debit").disabled=false;
			document.getElementById("credit").disabled=false;
			if(status==0)
			document.getElementById("approvalLimit").value="";
			document.getElementById("appLimit").style.display="none";
		}else{
			document.getElementById("debit").checked=true;
			document.getElementById("debit").disabled=false;
			document.getElementById("credit").disabled=true;
			if(status==0)
			document.getElementById("approvalLimit").value="";
			document.getElementById("appLimit").style.display="none";
		}
		
		 if(txn==99){
			
			
			<authz:authorize ifAnyGranted="ROLE_admin">
			 if(txn==99 && document.getElementById("Global").checked==true){
			 if(document.getElementById("Global").checked==true){
				 //document.getElementById("approvalLimit").value="";
			 	document.getElementById("appLimit").style.display="block";
			 	}
		 	}
			 </authz:authorize>
			 <authz:authorize ifAnyGranted="ROLE_bankadmin,ROLE_bankgroupadmin,ROLE_parameter">
				// document.getElementById("approvalLimit").value="";
				document.getElementById("appLimit").style.display="block"; 
				</authz:authorize>
			 
			 } else if(txn==99 &&  document.getElementById("InterBank").checked==true){
				
				<authz:authorize ifAnyGranted="ROLE_admin">
					// document.getElementById("approvalLimit").value="";
				 	document.getElementById("appLimit").style.display="none";
				 </authz:authorize>
				 /* document.getElementById("approvalLimit").value="";
				 document.getElementById("appLimit").style.display="block"; */
				 
				 
				 }
			 
		
		
	}
	
	function hideApprovalLimit(){
		
		var ruleType=document.getElementById("InterBank").value;
		var txn=document.getElementById("transactions").value;
		
		if(txn==95 || txn==99 ){
			document.getElementById("approvalLimit").value="";
			document.getElementById("appLimit").style.display="none";
			document.bankManageMentForm.ruleLevel.checked=true;
		}
	}
	function showApprovalLimit(){
		var ruleType=document.getElementById("Global").value;
		var txn=document.getElementById("transactions").value;
		if(txn==95 || txn==99){
		document.getElementById("approvalLimit").value="";
		document.getElementById("appLimit").style.display="block";
		 document.bankManageMentForm.ruleLevel[0].checked=true;
		}
	}
	
	function cancelForm(){
	    /* document.transactionRuleForm.action="searchTransactionRules.htm?ruleLevel=1";
	    document.transactionRuleForm.submit(); */
	    
		 	var rule = 1;
	    	var ruleLevel = document.getElementById('ruleLevel').value=rule;
	        url = "searchTransactionRules.htm";
	    	submitlink(url,'transactionRuleForm');
	}
	
	function readValuesFromTxtBox(){
		var docF = document.transactionRuleForm;
		var maxCum=(docF.maxCumValueLimit.value).split(",");
		var noofTime=(docF.maxNumTimes.value).split(",");
		var alPer=(docF.allowedPer.value).split(",");
		var alPerUnit=(docF.allowedPerUnit.value).split(",");
		var combo = document.getElementById("allperunit") ;
		if(maxCum.length!=0){
		for(var i=0;i<maxCum.length;i++){	
		var allperunitVal = combo.options[alPerUnit[i]].firstChild.nodeValue ;
		var e = maxCum[i];var f=noofTime[i];var g=alPer[i];var h=alPerUnit[i];	
			
		cvlimit[l]=e; ntimes[l]=f; allper[l]=g;allperunit[l]=h; l++; 

		addTxnRow(e,f,g,allperunitVal,1,-1);
		
		}		
		}
		
		clearTxnField();
		cvlimit.length!=0?document.getElementById('maxValueLimit').disabled=true:document.getElementById('maxValueLimit').disabled=false;
		
	}
	
</script>

<style>
.grayBox {
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =       80);
}

.box_content {
	position: fixed;
	top: 27%;
	left: 20%;
	right: 40%;
	width: 60%;
	padding: 16px;
	z-index: 1002;
	overflow: auto;
}

.tr_color {
	bgcolor: #d2d3f1;
}
</style>

<style type="text/css">
td{
text-align:center;
}
</style>
</head>
<body>
<form:form name="transactionRuleForm" id="transactionRuleForm" action="saveTransactionRule.htm" method="post" commandName="transactionRulesDTO" >
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><c:out value="${level}"/> <spring:message code="TITLE_TRMANAGEMENT" text="titleTRM:"/></span>
		</h3>
	</div>
	<!-- //@Start, changed class purpose bug 5682, by Murari, dated : 20-07-2018 -->
	<div class="col-md-3 col-md-offset-10">
	<!--  end -->
		<%-- <strong><a href="javascript:submitForm('transactionRuleForm','<c:out value="listTransactionRules.htm?pageNumber=1"/>')"><spring:message code="LINK_VIEWSCRULE" text="Rule" /> </a></strong> --%>
		<strong><a href="javascript:viewTxRules('listTransactionRules.htm','1')"><spring:message code="LINK_VIEWSCRULE" text="Rule" /> </a></strong>
	</div>
		<div class="col-sm-5 col-sm-offset-4">
			<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
		</div>
	<div class="box-body">
	<div style="border: 1px solid #ddd;border-radius:10px"><br />
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_TXNTYPE" text="txnType:"/><font color="red">*</font></label> 
					<div class="col-sm-5">
					<select class="dropdown chosen-select" id="transactions" name="transactions"  class="dropdown" onchange="switchRadio(0);">
					<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" />
					
						<c:set var="lang" value="${language}"></c:set>
						
							<c:forEach items="${masterData.transTypeList}" var="transTypeList">
							<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs">
										<c:if test="${transactionTypesDescs.comp_id.locale==lang}">
										<authz:authorize ifAnyGranted="ROLE_admin,ROLE_bankgroupadmin">	
										<c:if test="${transTypeList.transactionType!=84 }">
										<option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${transTypeList.transactionType eq transactionRulesDTO.transactions}" >selected=true</c:if> >
										<c:out 	value="${transactionTypesDescs.description}"/>	
										</option>
										</c:if>
										</authz:authorize>
										<authz:authorize ifNotGranted="ROLE_admin,ROLE_bankgroupadmin">	
										<c:if test="${transTypeList.transactionType!=84 }">
										 <option value="<c:out value="${transTypeList.transactionType}"/>" <c:if test="${transTypeList.transactionType eq transactionRulesDTO.transactions}" >selected=true</c:if> >
										<c:out 	value="${transactionTypesDescs.description}"/>	
										</option>
										</c:if>
										</authz:authorize>
										</c:if>		  						
							</c:forEach>
							</c:forEach>
							</option>
						</select>
						<font color="red"><form:errors path="transactions"/></font>
					</div>
				</div>
				<authz:authorize ifAnyGranted="ROLE_admin">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_RULE_TYPE" text="Rule Type:"/><font color="red">*</font>:</label> 
					<div class="col-sm-7">
					<form:radiobutton id="Global" path="ruleLevel" value="1" label="Global"  onclick="showApprovalLimit()" checked="checked"></form:radiobutton>&nbsp; &nbsp;
					<form:radiobutton id="InterBank" path="ruleLevel" value="4" label="InterBank" onclick="hideApprovalLimit()"></form:radiobutton>
					</div>
				</div>
				</authz:authorize>
				<authz:authorize ifAnyGranted="ROLE_bankgroupadmin,ROLE_parameter,ROLE_bankadmin">
					<%-- <form:input path="ruleLevel" type="hidden"/> --%>
					<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_RULE_TYPE" text="Rule Type:"/><font color="red">*</font>:</label> 
					
					<form:radiobutton id="ruleLevel" path="ruleLevel" value="3" label="Profile"  onclick="showApprovalLimit()" checked="checked"></form:radiobutton>&nbsp; &nbsp
				</div>
				</authz:authorize>
			</div>
			<div class="row">
				 <div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message	code="LABEL_SOURCE_TYPE" text="Source Type" /><font color="red">*</font></label> 
					<div class="col-sm-6">
					<div >
					   <form:checkbox path="sourceType" checked="true" value="1"/>&nbsp;<spring:message code="LABEL_SOURCE_WALLET" text="Wallet" /> &nbsp; &nbsp;
                      <%--  <form:checkbox path="sourceType" value="2"/>&nbsp;<spring:message code="LABEL_SOURCE_CARD" text="Card" /> &nbsp; &nbsp;
                       <form:checkbox path="sourceType" value="4"/>&nbsp;<spring:message code="LABEL_SOURCE_FI" text="FI" /> &nbsp; &nbsp; &nbsp; --%>
                    </div>
                    <div >   
                    <%--    <form:checkbox path="sourceType" value="3"/>&nbsp;<spring:message code="LABEL_SOURCE_BANKACC" text="Bank Account" /> &nbsp; &nbsp;
                       <form:checkbox path="sourceType" value="0"/>&nbsp;<spring:message code="LABEL_SOURCE_OTHERS" text="Others" /> --%>																			
				 	<font color="red"><form:errors path="sourceType" /></font>
					</div>
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;"><spring:message code="LABEL_CALCULATED_ON" text="Calculated On:"/><font color="red">*</font>:</label> 
					<div class="col-sm-7">
					<form:radiobutton path="ruleType" value="0" checked="true" id="debit"/><spring:message code="LABEL_DEBIT" text="Debit:"/>&nbsp; &nbsp;
					<form:radiobutton path="ruleType" value="1" id="credit"/><spring:message code="LABEL_CREDIT" text="Credit:"/>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_TH_MAXVAL_LIMIT" text="MaxValLimit:"/><font color="red">*</font></label> 
					<div class="col-sm-5">
					<form:input path="maxValueLimit" maxlength="15" cssClass="form-control" style="width:180px;"/> <font color="red"><form:errors path="maxValueLimit"/></font>
					</div>
				</div>
				<div id="appLimit" style="display:none;">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="LABEL_APPROVAL_LIMIT" text="Approval Limit"/> <font color="red">*</font></label> 
					<div class="col-sm-6">
					<form:input path="approvalLimit" maxlength="15" cssClass="form-control" style="width:180px;"/>
					</div>
				</div>
				</div>
				<authz:authorize ifAnyGranted="ROLE_parameter, ROLE_bankadmin">																	
					<c:if test="${ transactionRulesDTO.ruleLevel == 1 or  transactionRulesDTO.ruleLevel == 2 }">
						<c:set var="style" value="display:none"/>
					</c:if>
					<div class="col-sm-6">
		        		<label class="col-sm-4"><spring:message code="LABEL_SELECT_PROFILE" text="Customer Profile" />
		        		<font color="red">*</font></label>
			        	<div class="col-sm-6"><form:select path="profileId" id="profileId" items="${customerProfileList}" itemLabel="profileName" itemValue="profileId" cssClass="dropdown"/> </div>
					</div>
			</authz:authorize>
			</div>
	</div>
</div>
			<div class="box-body table responsive">
			<table style="border-collapse: collapse;border:1px solid #ddd; width:100%;height:70px;">
			<tr>
				<th><spring:message code="LABEL_TH_MAXCUMVAL_LIMIT" text="MaxCumValLimit:"/><font color="red">*</font></th>
				<th><spring:message code="LABEL_TH_MAXNUM_TIMES" text="MaxNumTimes:"/><font color="red">*</font></th>
				<th><spring:message code="LABEL_TH_ALLPER" text="AllowedPer:"/><font color="red">*</font></th>
				<th><spring:message code="LABEL_TH_ALLPER_UNIT" text="AllowedPerUnit:"/><font color="red">*</font></th>
				<th></th>
			</tr>	
			
			<tr>
				<td><input name="cvlimit" type="text" class="medium_text_feild" id="cvlimit" maxlength="16"/></td>
				<td><input name="ntimes" type="text" class="small_text_feild" id="ntimes" maxlength="10"/></td>
				<td><input name="allper" type="text" class="small_text_feild" id="allper" maxlength="6"/></td>
				<td>
				<!-- changed class = "dropdown chosen-select" to class="dropdown" purpose bug 5685,  by Murari, dated : 19-07-2018-->
				<select name="allperunit" id="allperunit" class="dropdown">
					<option value="" selected="true"><spring:message code="LABEL_SELECT" text="select:"/></option>
					<option value="1"><spring:message code="LABEL_SELECT_DAYS" text="Days:"/></option>
					<option value="2"><spring:message code="LABEL_SELECT_WEEKS" text="Weeks:"/></option>
					<option value="3"><spring:message code="LABEL_SELECT_MONTHS" text="Months:"/></option>
				</select>
				</td>
				<td><div id="txnAddDiv"><input type="button" id="addTxn" value="<spring:message code="BUTTON_ADD" text="add:"/>" onclick="addTxnDetails(<c:out value="${transactionRulesDTO.transactionRuleId}"/>);"/></div>
	               <div id="txnEditDiv" style="display: none;"> <input type="button" id="updateTxn"  value="<spring:message code="LABEL_UPDATE" text="Update:"/>" onclick="updateTxnRow()" />
	               </div></td>
			</tr>
			</table>
		<div style="height: 150px; overflow: scroll;border:1px solid #ddd;">
		<table style="width:100%;" cellspacing="0" cellpadding="0">
		<tbody>
		<tr bgColor="#d2d3f1">
		<th height="25px"><spring:message code="LABEL_TH_MAXCUMVAL_LIMIT" text="MaxCumValueLimit:"/></th>
		<th height="25px"><spring:message code="LABEL_TH_MAXNUM_TIMES" text="MaxNumTimes:"/></th>
		<th height="25px"><spring:message code="LABEL_TH_ALLPER" text="AllowedPer:"/></th>
		<th height="25px"><spring:message code="LABEL_TH_ALLPER_UNIT" text="AllowedPerUnit:"/></th>
		<th height="25px"><spring:message code="LABEL_ACTION" text="action:"/></th>
		</tr>
		</tbody>
		<tr>
			<td colspan="8" align="center">
				<div style="height: 150px;">
				<table id="tblGrid" width="100%" border="0" cellspacing="0"
					cellpadding="0">
					<tbody>
					
					<%int k=0; %>
       		  			<c:if test="${(transactionRulesDTO.transactionRuleId != null) }"> 
       		  			<% System.out.println("inside");%>
         	  				<c:forEach items="${transactionRulesDTO.trRuleValues}" var="trs">
         					<tr <%if(k%2!=0){%>bgColor="#d2d3f1"<%} %>>															
		<td align="center" width="10%" height="25px"><c:out value="${trs.maxCumValueLimit}"></c:out></td>
		<td align="center" width="15%"><c:out value="${trs.maxNumTimes}"></c:out></td>
		<td align="center" width="10%"><c:out value="${trs.allowedPer}"></c:out></td>	
		<td align="center" width="10%"> 
		<c:choose> 
		<c:when test="${trs.allowedPerUnit==1}">Days</c:when>
		<c:when test="${trs.allowedPerUnit==2}">Weeks</c:when>
		<c:otherwise>Months</c:otherwise>
		</c:choose><div style="display: none"><c:out value="${trs.allowedPerUnit}"></c:out></div></td>							
		<td align="center" width="10%">
		<a onclick="txnEdit();" style="cursor:pointer"><spring:message code="LABEL_EDIT" text="Edit:"/></a> | 
		<a onclick="deleteTxnRow();" style="cursor:pointer"><spring:message code="LABEL_DELETE" text="delete:"/></a>
		</td>
	</tr>	
	<%k++; %>
	</c:forEach>                
        				</c:if> 

					</tbody>
				</table>
				</div>
				<input id="tindex" type="hidden" name="tindex" /> 		
				<form:input path="transactionRuleId" type="hidden" ></form:input> 									
				<form:input path="maxCumValueLimit" type="hidden"></form:input> 
				<form:input path="maxNumTimes" type="hidden"></form:input> 
				<form:input path="allowedPer" type="hidden"></form:input> 
				<form:input path="allowedPerUnit" type="hidden"></form:input>
				<input id="pageNumber" type="hidden" name="pageNumber" value=""/>	
				
				<c:if test="${(transactionRulesDTO.transactionRuleId != null && message==null) }">         
				<script>window.onload=setTRRuleArraysByTableData();switchRadio(1);</script>
				</c:if> 
				<c:if test="${(message != null) }">         
				<script>window.onload=readValuesFromTxtBox();switchRadio(1);</script>
				</c:if>
			</td>
		</tr>
		</table>
		
		</div>
		
	<br />
	<!-- //@Start, changed class purpose bug 5682, by Murari, dated : 20-07-2018 -->    
	<div class="col-md-3 col-md-offset-10">
	<!--  end -->
		<div class="btn-toolbar">
			<input type="button" class="btn-primary btn" value="<spring:message code="BUTTON_SUBMIT" text="submit"/>"  onclick="validate();" />
			<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
		</div>
	</div>
	<br /><br /><br />
</div>		
</div>			
</div>
</form:form>
</body>
</html>
