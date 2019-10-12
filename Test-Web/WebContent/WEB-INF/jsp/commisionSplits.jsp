<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/zapatec.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><spring:message code="LABEL_TITLE" text="EOT Mobile" /></title>

<script src="<%=request.getContextPath()%>/js/jquery.min.2.0.0.js"></script>

<style type="text/css">
.tabs {
	border-bottom: 1px solid #CCCCCC;
}

ul.tabs li {
	list-style: none;
	float: left;
	padding: 0.4em 2em;
	text-decoration: none;
	color: #FFFFFF;
	font-size: 100%;
	line-height: 1.3;
	background-color: #5E6569;
	margin: 0 3px 0 0;
	border-radius: 4px 4px 0px 0px;
}

ul.tabs li:hover {
	background-color: #6D6E70;
	color: #FFFFFF;
	line-height: 1.3;
	list-style: none;
	float: left;
	margin: 0 3px 0 0;
	padding: 0.4em 2em;
}

ul.tabs {
	border-bottom-right-radius: 4px;
	clear: both;
	color: #222222;
	float: left;
	font-weight: bold;
	height: 28px;
	margin: 0;
	margin-left: 10px;
	padding: 0.2em 0.2em 0;
	width: 97%;
}

.data_store_field {
	height: 357px;
	overflow-y: scroll;
	border: 1px solid rgba(175, 168, 168, 0.49);
}

.data_header_field {
	background: #d2d3f1;
	margin-bottom: 10px;
	margin-top: 10px;
}

.header_align {
	margin-top: 17px;
}

.privilege_border {
	box-shadow: 0px 0px 12px 1px rgba(177, 172, 172, 0.59);
}

label {
	margin-left: 18px;
}

.col-sm-12.button_alignment {
	margin-top: 35px;
	text-align: center;
}

input.btn-primary.btn.assign_button {
	margin-right: 10px;
}

input.btn-default.btn.reset_button {
	margin-right: 10px;
}

.role_alignment {
	text-align: right;
	margin-left: -15px;
}

.user_alignment {
	text-align: right;
	margin-left: -15px;
}

.privilege_Form {
	height: 275px;
}

.header_align label {
	margin-left: 7px !important;
}

.header_align input {
	width: 100px !important;
}
</style>

<script type="text/javascript">

		
		
	function submitCommissionSplits(url,formId){
		var jsonObj = [];
		var jsonObjTemp = [];
		var isValid = false;
		
			var depositCommission = 0;
			var withdrawCommission = 0;
			var test_arr = $("input[name*='commisionPct']");
			$.each(test_arr, function(i, item) {  //i=index, item=element in array
				var idName = $(item).attr('id');
				if(idName.indexOf('mGURUSH') != -1 && idName.indexOf('115') != -1){
					if(validateCommission($(item).val())){
					depositCommission += parseInt( $(item).val());
					createJsonData('mGURUSH', '115', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('PRINCIPAL_AGENT') != -1 && idName.indexOf('115') != -1){
					if(validateCommission($(item).val())){
						
					depositCommission += parseInt( $(item).val());
					createJsonData('PRINCIPAL_AGENT', '115', $(item).val(),jsonObj );
					}
				}
				if(idName.indexOf('SUPER_AGENT')!= -1 && idName.indexOf('115') != -1){
					if(validateCommission($(item).val())){
					depositCommission += parseInt( $(item).val());
					createJsonData('SUPER_AGENT', '115', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('AGNT') !=-1 && idName.indexOf('115') != -1){
					if(validateCommission($(item).val())){
					depositCommission += parseInt( $(item).val());
					createJsonData('AGENT', '115', $(item).val(),jsonObj );
					}
					}
					
						
				if(idName.indexOf('mGURUSH') != -1 && idName.indexOf('116') != -1){
					if(validateCommission($(item).val())){
					withdrawCommission += parseInt( $(item).val());
					createJsonData('mGURUSH', '116', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('PRINCIPAL_AGENT') != -1 && idName.indexOf('116') != -1){
					if(validateCommission($(item).val())){
					withdrawCommission += parseInt( $(item).val());
					createJsonData('PRINCIPAL_AGENT', '116', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('SUPER_AGENT') != -1 && idName.indexOf('116') != -1){
					if(validateCommission($(item).val())){
					withdrawCommission += parseInt( $(item).val());
					createJsonData('SUPER_AGENT', '116', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('AGNT') != -1 && idName.indexOf('116') != -1){
					if(validateCommission($(item).val())){
					withdrawCommission += parseInt( $(item).val());
					createJsonData('AGENT', '116', $(item).val(),jsonObj );
					}
					}

				if(idName.indexOf('mGURUSH') != -1 && idName.indexOf('135') != -1){
					if(validateFlatAmount($(item).val())){
					createJsonData('mGURUSH', '135', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('PRINCIPAL_AGENT') != -1 && idName.indexOf('135') != -1){
					if(validateFlatAmount($(item).val())){
					createJsonData('PRINCIPAL_AGENT', '135', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('SUPER_AGENT') != -1 && idName.indexOf('135') != -1){
					if(validateFlatAmount($(item).val())){
					createJsonData('SUPER_AGENT', '135', $(item).val(),jsonObj );
					}
					}
				if(idName.indexOf('AGNT') != -1 && idName.indexOf('135') != -1){
					if(validateFlatAmount($(item).val())){
					createJsonData('AGENT', '135', $(item).val(),jsonObj );
					}
					}
				
				
			});
			isValid =true;
			if(depositCommission> 0 && depositCommission !=100){
				isValid = false;
				}
			if(withdrawCommission> 0 && withdrawCommission !=100){
				isValid = false;
				}

			if(depositCommission == 0 && withdrawCommission == 0){
				isValid = false;
				}
			
		
			
		if(document.getElementById(formId)!=null && isValid && validateJsonData(jsonObj)){
			var new_json = JSON.stringify(jsonObj);
			document.getElementById(formId).method = "post";
			document.getElementById("commdata").value = new_json;
			document.getElementById(formId).action = url;
			document.getElementById(formId).submit();
	}else{

		alert("Not a valid commision configuration")
		}
		
}

	/* function createJsonData(businessPartner, txnType, ele) {
		var eleValue = ele;
		
		 <c:if test="${commissionJson ne '[]'}">
		eleValue = ele;
		</c:if>
		<c:if test="${commissionJson eq '[]'}">
		eleValue = ele.value
		</c:if> 
		if(validateCommission(eleValue)){ 
		var inputData={};
		inputData["businessPartner"]=businessPartner;
		inputData["transactionType"]=txnType;
		inputData["commisionPct"]=eleValue;
		
		//var inputData ='{"businessPartner" : "'+businessPartner+'", "transactionType" : "'+txnType+'", "commisionPct" : "'+ele.value+'"}';
		jsonObjTemp.push(inputData);
		if(validateSumTotal()){
			var newData = JSON.stringify(jsonObjTemp);
			var obj = JSON.parse(newData);
			for(var i=0; i< obj.length; i++){
				var inputPushData={};
				inputPushData["businessPartner"]=obj[i].businessPartner;
				inputPushData["transactionType"]=obj[i].transactionType;
				inputPushData["commisionPct"]=obj[i].commisionPct;
				jsonObj.push(inputPushData);
				isValid=true;
				}
			commissionPersent=0;
			jsonObjTemp=[];
		}
	 } 
	}
 */
	function createJsonData(businessPartner, txnType, eleValue,jsonObj) {
		
		var inputData={};
		inputData["businessPartner"]=businessPartner;
		inputData["transactionType"]=txnType;
		inputData["commisionPct"]=eleValue;
		jsonObj.push(inputData);
		
	
	}
 var commissionPersent =0;
	function validateCommission(value){
		var x = parseFloat(value);
		if (isNaN(x) || x < 0 || x > 100) {
		    alert("Value should be between 0 to 100");
		    jsonObjTemp.splice(jsonObjTemp.length-4, 4);
		    isValid=false;
		    return false;
		}
		return true;
		}

	function validateFlatAmount(value){
		var x = parseFloat(value);
		if (isNaN(x) || x < 0) {
		    alert("Please enter a valid amount");
		    jsonObjTemp.splice(jsonObjTemp.length-4, 4);
		    isValid=false;
		    return false;
		}
		return true;
		}

	function validateSumTotal(){
		if(jsonObj.length > 3){
			var newData = JSON.stringify(jsonObjTemp);
			var obj = JSON.parse(newData);
			
			for(var i =0; i < obj.length; i++){
			commissionPersent += parseInt(obj[i].commisionPct)

			if(i== obj.length-1 && commissionPersent != 100){
				alert("Commission sum total should be 100");
				commissionPersent=0;
				jsonObjTemp=[];
				jsonObj.splice(obj.length-4, 4);
				isValid=false;
				return false;
				}
			}
			
		}else{
			isValid=false;
			return false;
		}
		return true;
		}

	function validateJsonData(jsonObj){
		var newData = JSON.stringify(jsonObj);
		var obj = JSON.parse(newData);
		var isValid_mGURUSH_115 = false;
		var isValid_PRINCIPAL_AGENT_115 = false;
		var isValid_SUPER_AGENT_115 = false;
		var isValid_AGENT_115 = false;

		var isValid_mGURUSH_116 = false;
		var isValid_PRINCIPAL_AGENT_116 = false;
		var isValid_SUPER_AGENT_116 = false;
		var isValid_AGENT_116 = false;
		
		for(var i=0; i< obj.length; i++){
			isValidData = false;
			if(obj[i].transactionType == '115'){
			if((obj[i].businessPartner =='mGURUSH' &&  obj[i].transactionType == '115' &&  obj[i].commisionPct !='' )) {
				isValid_mGURUSH_115 = true;
				}
			if( (obj[i].businessPartner =='PRINCIPAL_AGENT' &&  obj[i].transactionType == '115' &&  obj[i].commisionPct !='' )){
				isValid_PRINCIPAL_AGENT_115 = true;
				}
			if( (obj[i].businessPartner =='SUPER_AGENT' &&  obj[i].transactionType == '115' &&  obj[i].commisionPct !='' )){
				isValid_SUPER_AGENT_115 = true;
				}
			if((obj[i].businessPartner =='AGENT' &&  obj[i].transactionType == '115' &&  obj[i].commisionPct !='' )){
				isValid_AGENT_115 = true;
				}
			}

			
			if(obj[i].transactionType == '116'){
				if((obj[i].businessPartner =='mGURUSH' &&  obj[i].transactionType == '116' &&  obj[i].commisionPct !='' )) {
					isValid_mGURUSH_116 = true;
					}
				if( (obj[i].businessPartner =='PRINCIPAL_AGENT' &&  obj[i].transactionType == '116' &&  obj[i].commisionPct !='' )){
					isValid_PRINCIPAL_AGENT_116 = true;
					}
				if( (obj[i].businessPartner =='SUPER_AGENT' &&  obj[i].transactionType == '116' &&  obj[i].commisionPct !='' )){
					var isValid_SUPER_AGENT_116 = true;
					}
				if((obj[i].businessPartner =='AGENT' &&  obj[i].transactionType == '116' &&  obj[i].commisionPct !='' )){
					isValid_AGENT_116 = true;
					}
				}

			
		}
		if((isValid_mGURUSH_115 && isValid_PRINCIPAL_AGENT_115 && isValid_SUPER_AGENT_115 && isValid_AGENT_115 ) || (isValid_mGURUSH_116 && isValid_PRINCIPAL_AGENT_116 && isValid_SUPER_AGENT_116 && isValid_AGENT_116)){
			
			return true;
		}else{
			return false;
			}
	}	
</script>
<style>
.container {
	width: 900px;
}
</style>
</head>
<body>

	<div class="col-md-12">
		<div class="box">
			<div class="box-header">
				<h3 class="box-title">
					<span><spring:message code="LABEL_COMMISION_SPLITS"
							text="Commision Splits" /></span>
				</h3>
			</div>
			<div class="col-sm-5 col-sm-offset-5"
				style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
			<br />

			<form:form class="form-inline privilege_form"
				name="commisionSplitsForm" id="commisionSplitsForm" method="post"
				commandName="commisionSplitsDTO">
				<jsp:include page="csrf_token.jsp" />
				<div class="container">
					<div class="col-lg-12 privilege_border">
						<div class="col-lg-12 data_header_field"
							style="padding-left: 0px; padding-right: 0px;">
							<div class="col-md-4 header1_text header_align">
								Transaction Type</div>
							<div class="col-md-2 header2_text header_align">mGurush</div>
							<div class="col-md-2 header2_text header_align">Principal
								Agent</div>
							<div class="col-md-2 header2_text header_align">Super Agent
							</div>
							<div class="col-md-2 header2_text header_align">Agent</div>
						</div>

						<div class="col-lg-12 data_store_field">

							<input type="hidden" id="commdata" name="commdata" />
							<div class="row">
								<c:if
									test="${commisionSplitsDTO.commissionSplitList.size() eq 12}">
									<c:forEach items="${commisionSplitsDTO.commissionSplitList}"
										var="commission" varStatus="appRow">


										<c:if
											test="${commission.transactionType eq 115  and appRow.index eq 0}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Deposit (in %)</strong>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and appRow.index eq 0}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Withdrawal(in %)</strong>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115 and appRow.index eq 4}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Deposit (in %)</strong>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116 and appRow.index eq 4}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Withdrawal (in %)</strong>
											</div>
										</c:if>

										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										
										
										
										<c:if
											test="${commission.transactionType eq 135 and appRow.index eq 8}">
											<div class="col-md-4 name_field header_align">
												<strong>Customer Approval (Flat Amount)</strong>
											</div>
										</c:if>

										<c:if
											test="${commission.transactionType eq 135  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" disabled/>
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 135  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" disabled/>
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 135  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" disabled/>
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 135  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>



									</c:forEach>
								</c:if>


								<c:if
									test="${commisionSplitsDTO.commissionSplitList.size() eq 4}">
									<c:forEach items="${commisionSplitsDTO.commissionSplitList}"
										var="commission" varStatus="appRow">


										<c:if
											test="${commission.transactionType eq 115  and appRow.index eq 0}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Deposit</strong>
											</div>
										</c:if>

										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 115  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116 and appRow.index eq 0}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Withdrawal</strong>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType eq 116  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_${commission.transactionType }"
													class="form-control"
													value="<fmt:formatNumber type="number" pattern="###" value="${commission.commisionPct}" />" />
												</label>
											</div>
										</c:if>



									</c:forEach>
								</c:if>

								<c:if
									test="${commisionSplitsDTO.commissionSplitList.size() eq 4}">
									<c:forEach items="${commisionSplitsDTO.commissionSplitList}"
										var="commission" varStatus="appRow">


										<c:if
											test="${commission.transactionType ne 115  and appRow.index eq 0}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Deposit</strong>
											</div>
										</c:if>

										<c:if
											test="${commission.transactionType ne 115  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_115" class="form-control" value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 115  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_115" class="form-control"
													value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 115  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_115" class="form-control"
													value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 115  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_115" class="form-control" value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 116 and appRow.index eq 0}">
											<div class="col-md-4 name_field header_align">
												<strong>Cash Withdrawal</strong>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 116  and commission.businessPartner eq 'mGURUSH'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_mGURUSH_116" class="form-control" value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 116  and commission.businessPartner eq 'PRINCIPAL_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_PRINCIPAL_AGENT_116" class="form-control"
													value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 116  and commission.businessPartner eq 'SUPER_AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_SUPER_AGENT_116" class="form-control"
													value="" />
												</label>
											</div>
										</c:if>
										<c:if
											test="${commission.transactionType ne 116  and commission.businessPartner eq 'AGENT'}">
											<div class="col-md-2 header_align">
												<label> <input name="commisionPct"
													id="commisionPct_AGNT_116" class="form-control" value="" />
												</label>
											</div>
										</c:if>



									</c:forEach>
								</c:if>

							</div>
							<c:if
								test="${commisionSplitsDTO.commissionSplitList.size() eq 0}">
								<div class="row">
									<div class="col-md-4 name_field header_align">
										<strong>Cash Deposit</strong>
									</div>
									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_mGURUSH_115"
											id="commisionPct_mGURUSH_115" class="form-control" value="0" /> <!-- onchange="createJsonData('mGURUSH','115',this)" -->
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_PRINCIPAL_AGENT_115"
											id="commisionPct_PRINCIPAL_AGENT_115" class="form-control" value="0"  />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_SUPER_AGENT_115"
											id="commisionPct_SUPER_AGENT_115" class="form-control" value="0"  />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_AGENT_115"
											id="commisionPct_AGNT_115" class="form-control" value="0" />
										</label>
									</div>

								</div>
								<div class="row">
									<div class="col-md-4 name_field header_align">
										<strong>Cash Withdrawal</strong>
									</div>
									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_mGURUSH_116"
											id="commisionPct_mGURUSH_116" class="form-control" value="0" />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_PRINCIPAL_AGENT_116"
											id="commisionPct_PRINCIPAL_AGENT_116" class="form-control" value="0"  />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_SUPER_AGENT_116"
											id="commisionPct_SUPER_AGENT_116" class="form-control" value="0"  />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_AGENT_116"
											id="commisionPct_AGNT_116" class="form-control" value="0"  />
										</label>
									</div>

								</div>
								
								
								<div class="row">
									<div class="col-md-4 name_field header_align">
										<strong>Customer Approval</strong>
									</div>
									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_mGURUSH_116"
											id="commisionPct_mGURUSH_135" class="form-control" value="0" />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_PRINCIPAL_AGENT_116"
											id="commisionPct_PRINCIPAL_AGENT_135" class="form-control" value="0"  />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_SUPER_AGENT_116"
											id="commisionPct_SUPER_AGENT_135" class="form-control" value="0"  />
										</label>
									</div>

									<div class="col-md-2 header_align">
										<label> <input name="commisionPct_AGENT_116"
											id="commisionPct_AGNT_135" class="form-control" value="0"  />
										</label>
									</div>

								</div>
							</c:if>

							<%-- <c:forEach	items="${transTypeList}" var="transType" varStatus="appRow">
							<div class="row">
								<div class="col-md-4 name_field header_align">
									<strong><c:out value="${transType.description }"></c:out></strong>
								</div>
								<div class="col-md-2 header_align">
									<label> <form:input path="commisionPct" cssClass="form-control" onchange="createJsonData('mGURUSH','${transType.transactionType }',this)"/>
									</label>
								</div>

								<div class="col-md-2 header_align">
									<label> <form:input path="commisionPct" cssClass="form-control" onchange="createJsonData('PRINCIPAL_AGENT','${transType.transactionType }',this)" />
									</label>
								</div>

								<div class="col-md-2 header_align">
									<label> <form:input path="commisionPct" cssClass="form-control" onchange="createJsonData('SUPER_AGENT','${transType.transactionType }',this)" />
									</label>
								</div>

								<div class="col-md-2 header_align">
									<label> <form:input path="commisionPct" cssClass="form-control" onchange="createJsonData('AGENT','${transType.transactionType }',this)" />
									</label>
								</div>

							</div>
							</c:forEach> --%>
						</div>
					</div>
					<div class="col-sm-12 button_alignment">
						<input type="button" class="btn-primary btn assign_button"
							value="Submit"
							onclick="submitCommissionSplits('setCommissionSplits.htm', 'commisionSplitsForm')" />
					</div>

				</div>
				<!-- /.box-body -->
			</form:form>
		</div>
		<br />
		<!-- /.box -->
	</div>

</body>
</html>