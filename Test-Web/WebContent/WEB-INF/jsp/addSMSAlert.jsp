<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<head>
<script type="text/javascript">
var AlertMsg={
		"edit":"<spring:message code='LABEL_EDIT' text='Edit'/>",
		"delet":"<spring:message code='LABEL_DELETE' text='Delete'/>",
		"deleteAlert":"<spring:message code='ALERT_DELETE_ROW' text='Are you sure want to delete?'/>",
		"subscriptionTypeSelect":"<spring:message code='SUBSCRIPTION_TYPE_SELECT' text='Please select subscription type'/>",
		"costPerPackage":"<spring:message code='ALERT_COST_PER_PACKAGE' text='Please add cost per package'/>",
		"costPerPackageValid":"<spring:message code='ALERT_COST_PER_PACKAGE' text='Please add valid cost per package'/>",
		"costPerSMS":"<spring:message code='ALERT_COST_PER_SMS' text='Please add cost per SMS'/>",
		"costPerSMSValid":"<spring:message code='COST_PER_SMS_VALIDATION' text='Please add valid cost per SMS'/>",
		"differentSubscriptionType":"<spring:message code='DIFFERENT_SUBSCRIPTION_TYPE' text='Please select different subscription type'/>",
		"numberOfSMS":"<spring:message code='ALERT_NUMBER_OF_SMS' text='Please add valid number of SMS'/>",
		"SMSRuleExist":"<spring:message code='ERROR_10012' text='SMS package name already exist, please try with another name'/>"
}

function viewSMSAlert(url,pageNumber){
	document.getElementById('pageNumber').value=pageNumber;
	submitlink(url,'addSMSAlertForm');
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
td, th {
    padding: 0;
    text-align: center;
}
.col-sm-12.col-sm-offset-10.button_size {
    margin-top: -80px;
}
.table {
    width: 100%;
    max-width: 100%;
    margin-bottom: 20px;
    font-size: 12px;
    color: #666;
    height: 325px;
}
.btn-toolbar {
    margin-left: -5px;
    margin-top: 14px;
}
</style>
<script type="text/javascript"	src="<%=request.getContextPath()%>/js/addSMSAlert.js"></script>
</head>
<body>
<form:form id="addSMSAlertForm" name="addSMSAlertForm" action="saveSMSAlert.htm" method="post" autocomplete="off" commandName="smsAletDTO" >
<input type="hidden" name="pageNumber" id="pageNumber" value=""/>
<jsp:include page="csrf_token.jsp"/>
<div class="col-lg-12">
<div class="box">
	<div class="box-header">
		<h3 class="box-title">
			<span><c:out value="${level}"/> <spring:message code="TITLE_SMS_ALERT" text="SMS Alert"/></span>
		</h3>
	</div><br/>
	
	<div class="pull-right">
		<%-- <b><a href="javascript:submitForm('addSMSAlertForm','<c:out value="searchSmsAlertRules.htm?pageNumber=1"/>')"><spring:message code="LINK_VIEW_SMS_RULES" text="View SMS Rules" /> </a> &nbsp; &nbsp; &nbsp; &nbsp;</b> --%>
		<a href="javascript:viewSMSAlert('searchSmsAlertRules.htm','1')"><spring:message code="LINK_VIEW_SMS_RULES" text="View SMS Rules" /> </a> &nbsp; &nbsp; &nbsp; &nbsp;</b>	
	</div><br/>
	
	
	
		<div class="col-md-5 col-md-offset-4">
			<div style="color: #ba0101; font-weight: bold; font-size: 12px;">
				<spring:message code="${message}" text="" />
			</div>
		</div>
	<div class="box-body">
	<div style="border: 1px solid;border-radius:10px"><br />
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4" style="margin-top:4px;">
						<spring:message code="PACKAGE_NAME" text="Package name"/><font color="red">*</font><br/><br/>
					</label> 
					<div class="col-sm-5">
					<form:input path="packageName" maxlength="40" cssClass="text_feild" /> <font color="red"><form:errors path="packageName"/></font>&nbsp; &nbsp;
					</div>
					<div class="col-sm-5">
					</div>
				</div>
				<div class="col-sm-6">
					<label class="col-sm-5"><spring:message code="LABEL_TRANSACTION_TYPE" text="Transaction Type" /><font color="red">*</font></label> 
					<select id="transactions" name="transactions"  multiple="multiple" class="multiple">
					<option value=""><spring:message 	code="LABEL_SELECT" text="--Please Select--" /></option>
					<c:set var="lang" value="${language}"></c:set>
					<c:forEach items="${masterData.transTypeList}" var="transTypeList" >
					
					<c:forEach items="${transTypeList.transactionTypesDescs}" var="transactionTypesDescs" varStatus="cnt">
					 <c:if test="${transactionTypesDescs.comp_id.locale==lang}">
					<c:set value="f" var="sel"/>
					<c:forEach items="${smsAletDTO.transactions}" var="arrVal"> 
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
		</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<label class="col-sm-4"><spring:message code="COST_PER_SMS" text="Cost per SMS"/><font color="red">*</font></label> 
					<div class="col-sm-6">
					<form:input path="costPerSms" maxlength="15" cssClass="text_feild" /> <font color="red"><form:errors path="costPerSms"/></font>
			</div>
				</div>
			</div>
	</div>
</div>
			<div class="box-body table responsive">
			<table style="border:1px solid; width:100%;height:70px;">
			<tr>
				<th><spring:message code="SUBSCRIPTION_TYPE" text="Subscription type"/><font color="red">*</font></th>
				<th><spring:message code="COST_FOR_PACKAGE" text="Cost for package"/><font color="red">*</font></th>
				<th><spring:message code="NUMBER_OF_SMS" text="Number of SMS"/><font color="red">*</font></th>
				<th></th>
			</tr>	
			
			<tr >
				<td>
				<select name="subscriptionType" class="dropdown" id="subscriptionId">
						<option value="" selected="true"><spring:message code="LABEL_SELECT" text="select:"/></option>
						<option value="1"><spring:message code="LABEL_SELECT_DAILY" text="Daily"/></option>
						<option value="2"><spring:message code="LABEL_SELECT_WEEKLY" text="Weekly"/></option>
						<option value="3"><spring:message code="LABEL_SELECT_MONTHLY" text="Monthly"/></option>
						<option value="4"><spring:message code="LABEL_SELECT_YEARLY" text="Yearly"/></option>
					</select>
				</td>
				<td><input name="costPerPackage" id="costPerPackage" type="text" class="small_text_feild"  maxlength="10"/></td>
				<td><input name="numberOfSMS" id="numberOfSMS" type="text" class="small_text_feild"  maxlength="6"/></td>
				<td>
				<div id="txnAddDiv"><input type="button" id="addTxn" value="<spring:message code="BUTTON_ADD" text="add:"/>" onclick="addTxnDetails(<c:out value="${smsAletDTO.smsAlertRuleId}"/>);"/></div>
	            <div id="txnEditDiv" style="display: none;"> <input type="button" id="updateTxn"  value="<spring:message code="LABEL_UPDATE" text="Update:"/>" onclick="updateTxnRow()" /></div>
			 </td>
			</tr>
			</table>
		
		<input name="subscription" id="subscription" type="hidden"/>
		<table style="width:100%;" cellspacing="0" cellpadding="0">
		<thead>
		<tr bgColor="#d2d3f1">
		<th width="8%" height="25px"><spring:message code="SUBSCRIPTION_TYPE" text="Subscription type"/><font color="red">*</font></th>
		<th width="8%" height="25px"><spring:message code="COST_FOR_PACKAGE" text="Cost for package"/><font color="red">*</font></th>
		<th width="8%" height="25px"><spring:message code="NUMBER_OF_SMS" text="Number of SMS"/><font color="red">*</font></th>
		<th width="8%" height="25px"><spring:message code="LABEL_ACTION" text="action:"/></th>
		</tr>
		</thead>
		<tr>
		<td colspan="8" align="center">
			<div style="height: 150px; overflow: scroll;border:1px solid;">
			<table id="tblGrid" width="100%" border="0" cellspacing="0"
				cellpadding="0">
				<tbody>
		<c:if test="${(smsAletDTO.smsAlertRuleId != null) }"> 
	       <c:forEach items="${smsAletDTO.smsalertrulevalues}" var="trs" varStatus="theCount">
					<tr id="smsTxnDetails_${theCount.count-1}">
						<td align="center" width="8%" height="25px" class="subscriptionType"><c:choose> 
										<c:when test="${trs.subscriptionType==1}">Daily</c:when>
										<c:when test="${trs.subscriptionType==2}">Weekly</c:when>
										<c:when test="${trs.subscriptionType==3}">Monthly</c:when>
										<c:otherwise>Yearly</c:otherwise>
										</c:choose><div style="display: none"><c:out value="${trs.subscriptionType}"></c:out></div>
						</td>
						<td align="center" width="8%" class="costPerPackage"><c:out value="${trs.costPerPackage}"></c:out>
						</td>
						<td align="center" width="8%" class="numberOfSMS"><c:out value="${trs.numberOfSms}"></c:out>
						</td>
						<td align="center" width="8%">
						<a onclick="txnEdit(this);" style="cursor:pointer"><spring:message code="LABEL_EDIT" text="Edit:"/></a> | 
						<a onclick="deleteTxnRow(this);" style="cursor:pointer"><spring:message code="LABEL_DELETE" text="delete:"/></a>
						</td>
					</tr>
					
				</c:forEach>

			</c:if>
		</tbody>
		</table></div></td></tr>
		</table>
		</div>
		</div>
	<form:input path="smsAlertRuleId" type="hidden"></form:input>
	<input id="tindex" type="hidden" name="tindex" /> 
	 
	<c:if test="${(message!=null) }">         
	<script>window.onload=readValuesFromTxtBox();</script>
	</c:if>
	<!-- //@Start, changed class purpose bug 5682, by Murari, dated : 20-07-2018 -->
	<div class="col-md-3 col-md-offset-10 button_size">
	<!--  end -->
		<div class="btn-toolbar">
		<input type="button" class="btn-primary btn" value="<spring:message code="BUTTON_SUBMIT" text="submit"/>"  onclick="validateSMSAlertForm();" />
		<input type="button" class="btn-primary btn" value="<spring:message code="LABEL_CANCEL" text="Cancel"/>" onclick="cancelForm();" />
		</div>
	</div>	
	</div>	
</form:form>
<script >
function validateSMSAlertForm()
{ 
	var namePattern ="/^[a-zA-Z\s]+$/";
	var smsCost = "/^[0-9]\d*(\.\d+)?$/";
	var namePattern1='^\[a-zA-ZÀ-ÿ-\'+ 0-9 ]*$';
	var packageName = document.getElementById("packageName").value;
	var transactions = document.getElementById("transactions").value;
	var costPerSms = document.getElementById("costPerSms").value;
	var subscriptionType =  document.getElementById("subscriptionId").value;
	var packageNameAllZeros='^\[0]{0,10}$';
	
		
	if( packageName == ""){
		alert("<spring:message code='ALERT_PACKAGE_NAME' text='Please enter package name'/>");
		return false;
	}
	 if(packageName.search(packageNameAllZeros) != -1){
			alert("Enter valid data in Package Name field");
			return false;
		}
	if(!packageName.match(/^[a-zA-Z0-9]+(\s{0,1}[a-zA-Z0-9 -,])*$/)){
		alert("<spring:message code='ALERT_NAME_VALIDATION_SMS' text='Please add valid package name'/>");
		return false;
	}
	 if (packageName.search(namePattern1) == -1) {
			alert("<spring:message code='ALERT_PACKAGE_NAME_VALIDATION_SMS' text='Please enter package name without any special characters'/>");
			return false;
	 }

	 
	if( transactions == ""){
		alert("<spring:message code='ALERT_TRANSACTIOIN_TYPE_VALIDATION_NULL_SELECT' text='Please select transaction type'/>");
		return false;}
	if( costPerSms == ""){
		alert("<spring:message code='ALERT_NUMBER_VALIDATION_SMS_COST_NULL_SELECT' text='Please add cost per SMS'/>");
		return false;}
	if(costPerSms==0){
		alert("<spring:message code='ALERT_NUMBER_VALIDATION_COST_PER_SMS' text='Please add valid cost per SMS value'/>");
		return false;
	}
	if(!costPerSms.match(/^0*[1-9][0-9]*(\.[0-9]+)?|0+\.[0-9]*[1-9][0-9]*$/)){
		alert("<spring:message code='ALERT_NUMBER_VALIDATION_COST_PER_SMS' text='Please add valid cost per SMS value'/>");
		return false;
	}
	if(!costPerSms.match(/^\d{0,3}(\.\d{1,2})?$/gm)){
		alert("<spring:message code='ALERT_NUMBER_VALIDATION_COST_PER_SMS' text='Please add valid cost per SMS value'/>");
		return false;
	}
	 if($("table#tblGrid tr").length == 0)
		{
		alert("<spring:message code='ALERT_EMPTY_SMS_PACKAGE_SUBMIT' text='Please add Package details'/>");
		return false;
		} 
	 if($("#subscriptionId").val() != ""){
			alert("Complete current action");
			return false;
		}
	 else{
		writeSMSRuleValues();
	 }
	document.addSMSAlertForm.action = "addSMSAlerts.htm";
	document.addSMSAlertForm.submit();	
	}
function cancelForm(){
    document.addSMSAlertForm.action="searchSmsAlertRules.htm";
    document.addSMSAlertForm.submit();
}

</script>
</body>
</html>